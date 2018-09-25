package openDemo.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;
import openDemo.common.EncryptUtil;
import openDemo.entity.OuInfoModel;
import openDemo.entity.ResultEntity;
import openDemo.entity.UserInfoModel;
import openDemo.service.SyncOrgService;
import openDemo.service.SyncUserService;
import openDemo.utils.HttpClientUtil4Sync;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
@RequestMapping("BeiFangDaLu")
public class BeiFangDaLuController {
	@Autowired
	private SyncUserService userService;
	@Autowired
	private SyncOrgService orgService;
	// 参数配置
	private static final String API_KEY = "11f62229-70f6-41c0-8675-1334ad32e9a0";// 731255cc-29ca-4be6-9434-9b5ba971a525
	private static final String SECRECT_KEY = "d2331646-0d1d-4348-ae65-c126b8496538";// 731255cc-29ca-4be6-9434-9b5ba971a525
	private static final String BASE_URL = "http://api.qida.yunxuetang.com.cn/";// http://api.yunxuetang.cn/

	private static final String NPRLAND_URL_DEV = "http://ceshi.vili7.cn/zdht/manage/getMemberInfoForYXT.do";
	private static final String NPRLAND_URL_PRO = "http://admin.zdhtbio.com/manage/getMemberInfoForYXT.do";
	private static final String APP_ID = "EW";
	private static final String KEY_CODE = "code";
	private static final String KEY_DATA = "data";
	private static final String CODE_OK = "0";
	private static final String CODE_NG = "-1";
	// 直销的会员
	private static final String MEMBERCODE_QC = "QC";
	private static final String MEMBERCODE_CN = "CN";
	private static final String MEMBERCODE_JM = "JM";

	private static final Logger LOGGER = LogManager.getLogger(BeiFangDaLuController.class);

	@RequestMapping(value = "/nprland/userregloginpc", method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public Object userRegAndLoginPC(HttpServletRequest request, String userName, String pwd, String fromurl) {
		JSONObject jsonObject = new JSONObject();
		// 用户名密码非空check
		if (StringUtils.isBlank(userName) || StringUtils.isBlank(pwd)) {
			jsonObject.put(KEY_CODE, CODE_NG);
			jsonObject.put(KEY_DATA, "用户名密码不能为空!");
			return jsonObject;
		}

		// 请求客户接口参数
		JSONObject params = new JSONObject();
		params.put("appId", APP_ID);
		params.put("userId", userName);
		params.put("pwd", EncryptUtil.getMd5(pwd));

		// json解析
		UserInfoModel userModel = new UserInfoModel();
		try {
			String response = null;
			if (userName.startsWith(MEMBERCODE_QC) || userName.startsWith(MEMBERCODE_CN)
					|| userName.startsWith(MEMBERCODE_JM)) {
				response = HttpClientUtil4Sync.doPost(NPRLAND_URL_DEV, params.toString());
			}
			JSONObject resJsonObj = JSONObject.fromObject(response);
			// {"resultData":"","message":"用户名或密码错误","status":"0"}
			if (resJsonObj.getString("status").equals("0")) {
				jsonObject.put(KEY_CODE, CODE_NG);
				jsonObject.put(KEY_DATA, resJsonObj.getString("message"));
				return jsonObject;
			}

			// {"resultData":{"memberCode":"CN3686858","memberLevel":"超级金卡","memberName":"JM9262812_902","sex":""},"message":"SUCCESS","status":"1"}
			JSONObject userJsonObj = JSONObject.fromObject(resJsonObj.get("resultData"));
			// memberCode和userName相同
			userModel.setID(userName);
			userModel.setUserName(userName);
			userModel.setPassword(pwd);
			userModel.setCnName(userJsonObj.getString("memberName"));
			userModel.setSex(userJsonObj.getString("sex"));
			userModel.setSpare1(userJsonObj.getString("memberLevel"));
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			jsonObject.put(KEY_CODE, CODE_NG);
			jsonObject.put(KEY_DATA, "查询接口异常请稍后重试!");
			return jsonObject;
		}

		// 同步人员数据到平台
		List<UserInfoModel> userList = new ArrayList<UserInfoModel>();
		userList.add(userModel);
		try {
			ResultEntity resultEntity = userService.userSync(true, userList, API_KEY, SECRECT_KEY, BASE_URL);
			if (!CODE_OK.equals(resultEntity.getCode())) {
				LOGGER.error("登陆同步失败!" + " userName：" + userName + " 错误信息：" + resultEntity.getCode() + "-"
						+ resultEntity.getMessage());
				jsonObject.put(KEY_CODE, CODE_NG);
				jsonObject.put(KEY_DATA, "同步失败请联系管理员!");
				return jsonObject;
			}
		} catch (IOException e) {
			LOGGER.error(e.getMessage(), e);
			jsonObject.put(KEY_CODE, CODE_NG);
			jsonObject.put(KEY_DATA, "网络异常请稍后重试!");
			return jsonObject;
		}

		String urlString = null;

		ResultEntity resultEntity = null;
		/*
		 * String secretkey = SECRECT_KEY; QidaSsoService ssoService = new
		 * QidaSsoService(); try { resultEntity = ssoService.sso(userName,
		 * API_KEY, secretkey, ""); } catch (IOException e) {
		 * jsonObject.put(KEY_CODE, CODE_NG); jsonObject.put(KEY_DATA,
		 * "登陆失败请联系管理员!"); return jsonObject; }
		 */

		JSONObject jsonObj = JSONObject.fromObject(resultEntity);
		urlString = jsonObj.getString("data");

		if (StringUtils.isNotBlank(fromurl)) {
			urlString += "&fromUrl=" + fromurl;
		}

		// 成功后返回url
		jsonObject.put(KEY_CODE, CODE_OK);
		jsonObject.put(KEY_DATA, urlString);
		return jsonObject;
	}

	@RequestMapping(value = "/nprland/userreglogin", method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public Object userRegAndLogin(HttpServletRequest request, String userName, String pwd) {
		JSONObject jsonObject = new JSONObject();
		// 用户名密码非空check
		if (StringUtils.isBlank(userName) || StringUtils.isBlank(pwd)) {
			jsonObject.put(KEY_CODE, CODE_NG);
			jsonObject.put(KEY_DATA, "用户名密码不能为空!");
			return jsonObject;
		}

		// 请求客户接口参数
		JSONObject params = new JSONObject();
		params.put("appId", APP_ID);
		params.put("userId", userName);
		params.put("pwd", EncryptUtil.getMd5(pwd));

		// json解析
		UserInfoModel userModel = new UserInfoModel();
		OuInfoModel orgModel = new OuInfoModel();
		try {
			String response = null;
			if (userName.startsWith(MEMBERCODE_QC) || userName.startsWith(MEMBERCODE_CN)
					|| userName.startsWith(MEMBERCODE_JM)) {
				response = HttpClientUtil4Sync.doPost(NPRLAND_URL_DEV, params.toString());
			}
			JSONObject resJsonObj = JSONObject.fromObject(response);
			// {"resultData":"","message":"用户名或密码错误","status":"0"}
			if (resJsonObj.getString("status").equals("0")) {
				jsonObject.put(KEY_CODE, CODE_NG);
				jsonObject.put(KEY_DATA, resJsonObj.getString("message"));
				return jsonObject;
			}

			// {"resultData":{"memberCode":"CN3686858","memberLevel":"超级金卡","memberName":"JM9262812_902","sex":""},"message":"SUCCESS","status":"1"}
			JSONObject userJsonObj = JSONObject.fromObject(resJsonObj.get("resultData"));
			// memberCode和userName相同
			userModel.setID(userName);
			userModel.setUserName(userName);
			userModel.setPassword(pwd);
			userModel.setCnName(userJsonObj.getString("memberName"));
			userModel.setSex(userJsonObj.getString("sex"));
			userModel.setSpare1(userJsonObj.getString("memberLevel"));
			// 事业部
			orgModel.setOuName(userJsonObj.getString("businessName"));
			// 分公司名称
			orgModel.setParentOuName(userJsonObj.getString("companyName"));
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			jsonObject.put(KEY_CODE, CODE_NG);
			jsonObject.put(KEY_DATA, "查询接口异常请稍后重试!");
			return jsonObject;
		}

		String secretKey = SECRECT_KEY;
		// 同步上级部门数据
		OuInfoModel parentOuInfoModel = new OuInfoModel();
		String parentOuName = orgModel.getParentOuName();
		if (StringUtils.isNotBlank(parentOuName)) {
			parentOuInfoModel.setOuName(parentOuName);
			try {
				ResultEntity resultEntity = orgService.getOucodeByName(parentOuName, API_KEY, secretKey, BASE_URL);
				if (CODE_OK.equals(resultEntity.getCode())) {
					JSONObject data = JSONObject.fromObject(resultEntity.getData());
					// 组织存在 获取数据库中的id
					parentOuInfoModel.setID(data.getString("Key"));
				} else {
					// 组织不存在 创建新的id
					parentOuInfoModel.setID(UUID.randomUUID().toString());
				}
				// 同步部门
				List<OuInfoModel> orgList = new ArrayList<OuInfoModel>();
				orgList.add(parentOuInfoModel);
				resultEntity = orgService.ous(false, orgList, API_KEY, secretKey, BASE_URL);
				if (!CODE_OK.equals(resultEntity.getCode())) {
					LOGGER.error("登陆部门同步失败!" + " userName：" + userName + " 错误信息：" + resultEntity.getCode() + "-"
							+ resultEntity.getMessage());
				}
			} catch (IOException e) {
				LOGGER.error(e.getMessage(), e);
			}
		}
		// 同步本级部门数据
		OuInfoModel ouInfoModel = new OuInfoModel();
		String ouName = orgModel.getOuName();
		if (StringUtils.isNotBlank(ouName)) {
			ouInfoModel.setOuName(ouName);
			ouInfoModel.setParentID(parentOuInfoModel.getID());
			try {
				ResultEntity resultEntity = orgService.getOucodeByName(ouName, API_KEY, secretKey, BASE_URL);
				if (CODE_OK.equals(resultEntity.getCode())) {
					JSONObject data = JSONObject.fromObject(resultEntity.getData());
					// 组织存在 获取数据库中的id
					ouInfoModel.setID(data.getString("Key"));
				} else {
					// 组织不存在 创建新的id
					ouInfoModel.setID(UUID.randomUUID().toString());
				}
				// 同步部门
				List<OuInfoModel> orgList = new ArrayList<OuInfoModel>();
				orgList.add(ouInfoModel);
				resultEntity = orgService.ous(false, orgList, API_KEY, secretKey, BASE_URL);
				if (!CODE_OK.equals(resultEntity.getCode())) {
					LOGGER.error("登陆部门同步失败!" + " userName：" + userName + " 错误信息：" + resultEntity.getCode() + "-"
							+ resultEntity.getMessage());
				}
			} catch (IOException e) {
				LOGGER.error(e.getMessage(), e);
			}
		}

		// 同步人员数据到平台
		List<UserInfoModel> userList = new ArrayList<UserInfoModel>();
		userModel.setOrgOuCode(ouInfoModel.getID());
		userList.add(userModel);
		try {
			ResultEntity resultEntity = userService.userSync(true, userList, API_KEY, secretKey, BASE_URL);
			if (!CODE_OK.equals(resultEntity.getCode())) {
				LOGGER.error("登陆同步失败!" + " userName：" + userName + " 错误信息：" + resultEntity.getCode() + "-"
						+ resultEntity.getMessage());
				jsonObject.put(KEY_CODE, CODE_NG);
				jsonObject.put(KEY_DATA, "同步失败请联系管理员!");
				return jsonObject;
			}
		} catch (IOException e) {
			LOGGER.error(e.getMessage(), e);
			jsonObject.put(KEY_CODE, CODE_NG);
			jsonObject.put(KEY_DATA, "网络异常请稍后重试!");
			return jsonObject;
		}

		// 获取登录人信息
		/*
		 * String salt = getNonce4Create(4); String signature =
		 * getSignature4APP(API_KEY, salt, userName);
		 * 
		 * Third4Login thirdLogin = new Third4Login();
		 * thirdLogin.setApiKey(API_KEY); thirdLogin.setSalt(salt);
		 * thirdLogin.setSignature(signature); thirdLogin.setUserName(userName);
		 * JSONObject jsonObj = JSONObject.fromObject(thirdLogin); String
		 * serverUrl = QiDaCustomRedisConstants.TRANS_SERVER_CXH5URL;
		 * 
		 * String urlString = null; try { String resultStr =
		 * HttpClientUtil4Sync.sendPost(request, serverUrl +
		 * "/users/thirdtokens", jsonObj); urlString =
		 * JSONObject.fromObject(resultStr).getString("url"); } catch (Exception
		 * e) { LOGGER.error(e.getMessage(), e); jsonObject.put(KEY_CODE,
		 * CODE_NG); jsonObject.put(KEY_DATA, "登陆失败请联系管理员!"); return jsonObject;
		 * }
		 */

		// 同步成功后返回url
		jsonObject.put(KEY_CODE, CODE_OK);
		jsonObject.put(KEY_DATA, "同步完毕!");
		return jsonObject;
	}

}
