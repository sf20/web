package openDemo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import openDemo.service.sync.aiqinhai.AiqinhaiSyncService;

@Controller
@RequestMapping("aiqinhai")
public class AiqinhaiDataSyncController {
	@Autowired
	private AiqinhaiSyncService aiqinhaiSyncService;

	@RequestMapping(value = "/datasync", method = RequestMethod.GET)
	@ResponseBody
	public void dataSync() throws Exception {
		aiqinhaiSyncService.execute();
	}
}