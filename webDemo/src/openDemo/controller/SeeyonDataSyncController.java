package openDemo.controller;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import openDemo.service.sync.seeyon.SeeyonSyncService;
import openDemo.timer.SyncTimerService;

@Controller
public class SeeyonDataSyncController implements InitializingBean, DisposableBean {
	@Autowired
	private SeeyonSyncService seeyonSyncService;
	private SyncTimerService syncTimerService;

	@Override
	public void afterPropertiesSet() throws Exception {
		// syncTimerService = new SyncTimerService(06, 30);
		// syncTimerService.singleAddTimingService(seeyonSyncService);
	}

	@Override
	public void destroy() throws Exception {
		syncTimerService.shutdownExecutor();
	}
}