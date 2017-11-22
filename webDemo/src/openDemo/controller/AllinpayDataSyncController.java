package openDemo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import openDemo.service.sync.allinpay.AllinpaySyncService;

@Controller
@RequestMapping("allinpay")
public class AllinpayDataSyncController {
	@Autowired
	private AllinpaySyncService allinpaySyncService;

	@RequestMapping(value = "/datasync", method = RequestMethod.GET)
	@ResponseBody
	public void dataSync() throws Exception {
		allinpaySyncService.execute();
	}
}