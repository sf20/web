package openDemo.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import openDemo.entity.sync.kaiying.KaiyingResUserJsonModel;
import openDemo.entity.sync.kaiying.KaiyingUserInfoModel;
import openDemo.service.sync.kaiying.KaiyingSyncService;
import openDemo.utils.HttpClientUtil4Sync;

@Controller
@RequestMapping("kingnet")
public class KaiyingDataSyncController {
	private static final String REQUEST_GET_TOKEN = "";
	private static final String REQUEST_GET_USERINFO = "";
	private static final String REDIRECT_URI = "";
	private static final String CLIENT_ID = "";
	private static final String CLIENT_SECRET = "";
	private static final int RESPONSE_STATUS_OK = 1;
	@Autowired
	private KaiyingSyncService kaiyingSyncService;

	private ObjectMapper mapper = new ObjectMapper();;

	@RequestMapping(value = "/datasync", method = RequestMethod.GET)
	@ResponseBody
	public void dataSync() throws Exception {
		kaiyingSyncService.execute();
	}

	@RequestMapping(value = "/callback", method = RequestMethod.GET)
	@ResponseBody
	public void callback(HttpServletRequest request, String code) throws Exception {
		if (StringUtils.isNotBlank(code)) {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("access_token", getToken(code));
			String jsonString = HttpClientUtil4Sync.doGet(REQUEST_GET_USERINFO, params, null);
			KaiyingResUserJsonModel resJsonModel = mapper.readValue(jsonString, KaiyingResUserJsonModel.class);

			// 返回数据状态判断
			if (RESPONSE_STATUS_OK != resJsonModel.getCode()) {
				throw new IOException("获取用户信息接口错误：" + resJsonModel.getMsg());
			}

			KaiyingUserInfoModel userInfo = resJsonModel.getData();
			System.out.println(userInfo.getID());
		}
	}

	/**
	 * 获取token
	 * 
	 * @param code
	 * @return
	 * @throws IOException
	 */
	private String getToken(String code) throws IOException {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("grant_type", "authorization_code");
		params.put("client_id", CLIENT_ID);
		params.put("client_secret", CLIENT_SECRET);
		params.put("redirect_uri", REDIRECT_URI);
		params.put("code", code);

		// 从json字符串中解析token
		JsonNode jsonNode = mapper.readTree(HttpClientUtil4Sync.doGet(REQUEST_GET_TOKEN, params, null));
		String token = jsonNode.get("data").get("access_token").asText();

		return token;
	}
}