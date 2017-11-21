package openDemo.timer;

import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import openDemo.service.sync.CustomTimerTask;

public class TimerTest implements CustomTimerTask {
	private static final Logger logger = LogManager.getLogger(TimerTest.class);

	public static void main(String[] args) {

		SyncTimerService syncTimerService = new SyncTimerService(15, 20);
		syncTimerService.singleAddTimingService(new TimerTest());
	}

	public static long randomTime() {
		int randomSecond = new Random().nextInt(60) + 38;
		// if (randomSecond > 60) {
		// throw new RuntimeException();
		// }
		return randomSecond * 1000;
	}

	@Override
	public void execute() throws Exception {
		// 模拟任务执行耗时
		long randomTime = randomTime();
		logger.info("本次执行需要：" + randomTime / 1000 + "秒");
		Thread.sleep(randomTime);
	}
}
