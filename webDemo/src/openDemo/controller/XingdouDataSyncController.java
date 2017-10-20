package openDemo.controller;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import openDemo.service.sync.xingdou.XingdouSyncService;
import openDemo.timer.SyncTimerService;

@Controller
public class XingdouDataSyncController implements InitializingBean {
	@Autowired
	private XingdouSyncService xingdouSyncService;

	@Override
	public void afterPropertiesSet() throws Exception {
		// SyncTimerService syncTimerService = new SyncTimerService(16, 30);
		// syncTimerService.singleAddTimingService(xingdouSyncService);
	}

}