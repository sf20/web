package openDemo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import openDemo.service.sync.allinpay.AllinpaySyncService;
import openDemo.service.sync.elion.ElionSyncService;
import openDemo.service.sync.leo.LeoSyncService;
import openDemo.service.sync.opple.OppleSyncService;
import openDemo.service.sync.xingdou.XingdouSyncService;

@Controller
@RequestMapping("datasync")
public class AllDataSyncController {
	@Autowired
	private OppleSyncService oppleSyncService;
	@Autowired
	private LeoSyncService leoSyncService;
	@Autowired
	private ElionSyncService elionSyncService;
	@Autowired
	private XingdouSyncService xingdouSyncService;
	@Autowired
	private AllinpaySyncService allinpaySyncService;

	@RequestMapping(value = "/all", method = RequestMethod.GET)
	@ResponseBody
	public void dataSync() {
		oppleSyncService.execute();
		leoSyncService.execute();
		elionSyncService.execute();
		xingdouSyncService.execute();
		allinpaySyncService.execute();
	}
}