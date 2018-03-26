package openDemo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import openDemo.service.sync.znzd.ZnzdSyncService;

@Controller
@RequestMapping("znzd")
public class ZnzdDataSyncController {
	@Autowired
	private ZnzdSyncService znzdSyncService;

	@RequestMapping(value = "/datasync", method = RequestMethod.GET)
	@ResponseBody
	public void dataSync() throws Exception {
		znzdSyncService.execute();
	}
}