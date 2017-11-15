package openDemo.controller;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import openDemo.service.sync.elion.ElionSyncService;
import openDemo.timer.SyncTimerService;

@Controller
public class ElionDataSyncController implements InitializingBean, DisposableBean {
	@Autowired
	private ElionSyncService elionSyncService;
	private SyncTimerService syncTimerService;

	@Override
	public void afterPropertiesSet() throws Exception {
		// syncTimerService = new SyncTimerService(06, 30);
		// syncTimerService.singleAddTimingService(elionSyncService);
	}

	@Override
	public void destroy() throws Exception {
		syncTimerService.shutdownExecutor();
	}
}