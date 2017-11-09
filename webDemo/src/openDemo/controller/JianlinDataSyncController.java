package openDemo.controller;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import openDemo.service.sync.jianlin.JianlinSyncService;
import openDemo.timer.SyncTimerService;

@Controller
public class JianlinDataSyncController implements InitializingBean {
	@Autowired
	private JianlinSyncService jianlinSyncService;

	@Override
	public void afterPropertiesSet() throws Exception {
		// SyncTimerService syncTimerService = new SyncTimerService(20, 30);
		// syncTimerService.singleAddTimingService(jianlinSyncService);
	}

}