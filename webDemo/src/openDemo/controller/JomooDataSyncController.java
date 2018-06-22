package openDemo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import openDemo.service.sync.jomoo.JomooSyncService1;
import openDemo.service.sync.jomoo.JomooSyncService2;

@Controller
@RequestMapping("jomoo")
public class JomooDataSyncController {
	@Autowired
	private JomooSyncService1 jomooSyncService1;
	@Autowired
	private JomooSyncService2 jomooSyncService2;

	@RequestMapping(value = "/datasync", method = RequestMethod.GET)
	@ResponseBody
	public void dataSync() throws Exception {
		jomooSyncService1.execute();
		jomooSyncService2.execute();
	}
}