package openDemo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import openDemo.service.sync.meilitianyuan.MeiliTianyuanSyncService;

@Controller
@RequestMapping("mlty")
public class MeiliTianyuanDataSyncController {
	@Autowired
	private MeiliTianyuanSyncService meiliTianyuanSyncService;

	@RequestMapping(value = "/datasync", method = RequestMethod.GET)
	@ResponseBody
	public void dataSync() throws Exception {
		meiliTianyuanSyncService.execute();
	}
}