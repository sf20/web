package openDemo.controller;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import openDemo.service.sync.seeyon.SeeyonSyncService;
import openDemo.timer.SyncTimerService;

@Controller
public class SeeyonDataSyncController implements InitializingBean {
	@Autowired
	private SeeyonSyncService seeyonSyncService;

	@Override
	public void afterPropertiesSet() throws Exception {
		// SyncTimerService syncTimerService = new SyncTimerService(18, 30);
		// syncTimerService.singleAddTimingService(seeyonSyncService);
	}

}