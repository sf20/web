package openDemo.controller;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import openDemo.service.sync.allinpay.AllinpaySyncService;
import openDemo.timer.SyncTimerService;

@Controller
public class AllinpayDataSyncController implements InitializingBean, DisposableBean {
	@Autowired
	private AllinpaySyncService allinpaySyncService;
	private SyncTimerService syncTimerService;

	@Override
	public void afterPropertiesSet() throws Exception {
		// syncTimerService = new SyncTimerService(20, 30);
		// syncTimerService.singleAddTimingService(allinpaySyncService);
	}

	@Override
	public void destroy() throws Exception {
		syncTimerService.shutdownExecutor();
	}

}