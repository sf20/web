package openDemo.test;

import java.util.Date;

public abstract class AbstractSyncServiceTest {

	public void test() throws Exception {
		Date startDate = new Date();
		System.out.println("同步中......");

		serviceSync();

		System.out.println("同步时间：" + calcMinutesBetween(startDate, new Date()));
	}

	private long calcMinutesBetween(Date d1, Date d2) {
		return Math.abs((d2.getTime() - d1.getTime())) / 1000;
	}

	public abstract void serviceSync() throws Exception;
}
