package openDemo.controller;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import openDemo.service.sync.AllinpaySyncService;
import openDemo.timer.SyncTimerService;

@Controller
public class AllinpayDataSyncController implements InitializingBean {
	@Autowired
	private AllinpaySyncService allinpaySyncService;

	@Override
	public void afterPropertiesSet() throws Exception {
		// SyncTimerService syncTimerService = new SyncTimerService(16, 30);
		// syncTimerService.singleAddTimingService(allinpaySyncService);
	}

}