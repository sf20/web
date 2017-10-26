package openDemo.controller;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import openDemo.service.sync.elion.ElionSyncService;
import openDemo.timer.SyncTimerService;

@Controller
public class ElionDataSyncController implements InitializingBean {
	@Autowired
	private ElionSyncService elionSyncService;

	@Override
	public void afterPropertiesSet() throws Exception {
		// SyncTimerService syncTimerService = new SyncTimerService(20, 30);
		// syncTimerService.singleAddTimingService(elionSyncService);
	}

}