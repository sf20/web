package openDemo.timer;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import openDemo.service.sync.CustomTimerTask;

/**
 * 同步定时器
 * 
 * @author yanl
 *
 */
public class SyncTimerService {
	// 定时器间隔执行时间周期 单位毫秒
	private static final long PERIOD = 60 * 60 * 1000;

	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static final Logger LOGGER = LogManager.getLogger(SyncTimerService.class);

	// 每次定时器执行时间参数
	private int timerExecHour = 23;
	private int timerExecMinute = 00;
	private int timerExecSecond = 00;

	// 定时执行的service类名
	private String serviceClassName;
	// 用于执行定时任务的定时器
	private ScheduledExecutorService executor;
	// 定时器每次执行定时任务的预定时间
	private long expectedExecTimeMills;

	public SyncTimerService(int execHour, int execMinute) {
		this.timerExecHour = execHour;
		this.timerExecMinute = execMinute;

		Calendar calendar = Calendar.getInstance();
		// 设置定时器首次执行时间
		calendar.set(Calendar.HOUR_OF_DAY, timerExecHour);
		calendar.set(Calendar.MINUTE, timerExecMinute);
		calendar.set(Calendar.SECOND, timerExecSecond);
		expectedExecTimeMills = calendar.getTimeInMillis();
	}

	/**
	 * 增加定时服务
	 * 
	 * @param timerTask
	 */
	public void singleAddTimingService(CustomTimerTask timerTask) {
		// 保证只有一个定时器在运行
		if (executor == null) {
			addTimingService(timerTask);
		}
	}

	private void addTimingService(final CustomTimerTask timerTask) {
		serviceClassName = timerTask.getClass().getSimpleName();
		executor = Executors.newSingleThreadScheduledExecutor();

		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				try {
					// 执行同步方法
					LOGGER.info("定时同步[" + serviceClassName + "]开始");
					timerTask.execute();
					LOGGER.info("定时同步[" + serviceClassName + "]结束");

					// 实际任务结束时间
					long taskEndTime = System.currentTimeMillis();
					// 预定与实际进行比较
					long delay = compareGetDelay(taskEndTime);
					LOGGER.info(System.lineSeparator());

					// 继续设置下一个定时任务
					executor.schedule(this, delay, TimeUnit.MILLISECONDS);
				} catch (Exception e) {
					shutdownAndPrintLog(serviceClassName, e);
				}
			}
		};

		// 定时器设定首次任务
		long currentTime = System.currentTimeMillis();
		executor.schedule(task, compareGetDelay(currentTime), TimeUnit.MILLISECONDS);
		// executor.schedule(task, 0, TimeUnit.MILLISECONDS);
	}

	/**
	 * 比较实际执行完时间和预定执行时间得到定时器延时执行时间差
	 * 
	 * @param actualExecTimeMills
	 * @return
	 */
	private long compareGetDelay(long actualExecTimeMills) {
		long delay = 0;

		if (actualExecTimeMills <= expectedExecTimeMills) {
			// 未到本次预定执行时间时
			delay = expectedExecTimeMills - actualExecTimeMills;
		} else {
			// 超过本次预定执行时间时
			// 超时时间差计算
			long timeGap = actualExecTimeMills - expectedExecTimeMills;
			// 调整下次预定执行时间（时间差可能超过一个定时器间隔周期）
			expectedExecTimeMills = expectedExecTimeMills + ((timeGap / PERIOD + 1) * PERIOD);
			// 用下次预定执行时间去计算延时时间
			delay = expectedExecTimeMills - actualExecTimeMills;
		}

		LOGGER.info("预定下次[" + serviceClassName + "]执行时间: " + DATE_FORMAT.format(expectedExecTimeMills));
		LOGGER.info("距离下次[" + serviceClassName + "]执行还有: " + timeMillsToHMS(delay));

		return delay;
	}

	/**
	 * 将毫秒数转化为相应的时分秒格式：hh小时mm分钟ss秒
	 * 
	 * @param delay
	 * @return
	 */
	private String timeMillsToHMS(long delay) {
		long oneSecondTimeMills = 1000;
		long oneMinuteTimeMills = 60 * oneSecondTimeMills;
		long oneHourMills = 60 * oneMinuteTimeMills;

		long hour = delay / oneHourMills;
		long minute = (delay - hour * oneHourMills) / oneMinuteTimeMills;
		long second = (delay - hour * oneHourMills - minute * oneMinuteTimeMills) / oneSecondTimeMills;

		StringBuffer hmsStr = new StringBuffer();
		if (hour > 0) {
			hmsStr.append(hour).append("小时");
		}
		if (minute > 0) {
			hmsStr.append(minute).append("分钟");
		}
		if (second >= 0) {
			hmsStr.append(second).append("秒");
		}

		return hmsStr.toString();
	}

	/**
	 * 关闭定时器同时记录异常日志
	 * 
	 * @param className
	 * @param e
	 */
	private void shutdownAndPrintLog(String className, Exception e) {
		shutdownExecutor();
		LOGGER.info("发生异常::定时器[" + className + "]已停止");
		LOGGER.error("定时同步[" + className + "]出现异常", e);
	}

	public void shutdownExecutor() {
		if (executor != null) {
			executor.shutdown();
			LOGGER.info("线程执行器已关闭");
		}
	}
}
