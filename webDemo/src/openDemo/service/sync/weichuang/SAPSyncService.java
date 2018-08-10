package openDemo.service.sync.weichuang;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.http.Header;
import org.apache.http.message.BasicHeader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import openDemo.common.PrintUtil;
import openDemo.entity.OuInfoModel;
import openDemo.entity.PositionModel;
import openDemo.entity.UserInfoModel;
import openDemo.entity.sync.weichuang.SAPOuInfoModel;
import openDemo.entity.sync.weichuang.SAPPositionModel;
import openDemo.entity.sync.weichuang.SAPResJsonModel;
import openDemo.entity.sync.weichuang.SAPUserInfoModel;
import openDemo.service.sync.AbstractSyncService;
import openDemo.utils.HttpClientUtil4Sync;

@Service
public class SAPSyncService extends AbstractSyncService implements WeiChuangConfig {
	// 参数配置
	private static final String REQUEST_SERVER_ADDRESS = "https://api15.sapsf.cn/";
	private static final String REQUEST_URL_GET_SAML = "oauth/idp";
	private static final String REQUEST_URL_GET_TOKEN = "oauth/token";
	private static final String REQUEST_URL_VALIDATE_TOKEN = "oauth/validate";
	private static final String REQUEST_ODATA_ADDRESS = "https://api15.sapsf.cn/odata/v2/";
	private static final String REQUEST_ENTITY_FODEPARTMENT = "FODepartment?$format=json&$select=externalCode,name,parent,status";
	private static final String REQUEST_ENTITY_FOCOMPANY = "FOCompany?$format=json&$select=externalCode,name,status";
	private static final String REQUEST_ENTITY_POSITION = "Position?$format=json&$select=code,department,externalName_zh_CN,company";
	private static final String REQUEST_ENTITY_PERPERSONAL = "PerPersonal?$format=json&$select=personIdExternal,formalName,gender,customString4";
	private static final String REQUEST_ENTITY_PERPHONE = "PerPhone?$format=json&$select=personIdExternal,phoneNumber";
	private static final String REQUEST_ENTITY_PEREMAIL = "PerEmail?$format=json&$select=personIdExternal,emailAddress";
	private static final String REQUEST_ENTITY_EMPEMPLOYMENT = "EmpEmployment?$format=json&$select=personIdExternal,userId";
	private static final String REQUEST_ENTITY_EMPJOB = "EmpJob?$format=json&$select=userId,department,position,companyEntryDate,emplStatus";
	// private static final String REQUEST_ENTITY_USERACCOUNT =
	// "UserAccount?$format=json&$select=username,personIdExternal";

	// 全量增量区分
	private static final String MODE_FULL = "1";
	private static final String MODE_UPDATE = "2";
	// json解析用
	private ObjectMapper mapper;
	// 存放AccessToken
	private String token;

	public SAPSyncService() {
		super.setApikey(apikey);
		super.setSecretkey(secretkey);
		super.setBaseUrl(baseUrl);
		super.setModeFull(MODE_FULL);
		super.setModeUpdate(MODE_UPDATE);
		// super.setIsPosIdProvided(false);
		super.setSyncServiceName(this.getClass().getSimpleName());

		// 创建用于json反序列化的对象
		mapper = new ObjectMapper();
		// 忽略json中多余的属性字段
		mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
	}

	@Override
	protected boolean isOrgExpired(OuInfoModel org) {
		String status = org.getStatus();
		// 非“A”无效
		if (!"A".equals(status)) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	protected boolean isPosExpired(PositionModel pos) {
		return false;
	}

	@Override
	protected boolean isUserExpired(UserInfoModel user) {
		String status = user.getStatus();
		// status为null或者518表示无效
		if (status == null || "518".equals(status)) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	protected void setRootOrgParentId(List<OuInfoModel> newList) {
		// 部门根组织的parentId为null,无需设置
	}

	@Override
	protected void changePropValues(List<UserInfoModel> newList) {
		for (UserInfoModel tempModel : newList) {
			// 性别字符串转换M：男 F：女
			String sex = tempModel.getSex();
			if ("M".equals(sex)) {
				tempModel.setSex("男");
			} else if ("F".equals(sex)) {
				tempModel.setSex("女");
			}

			// 入职日期修改
			String entryTime = tempModel.getEntryTime();
			if (StringUtils.isNotBlank(entryTime)) {
				// 返回日期格式：/Date(1532304000000)/
				tempModel.setEntryTime(DateFormatUtils.format(
						Long.parseLong(entryTime.substring(entryTime.indexOf('(') + 1, entryTime.indexOf(')'))),
						"yyyy-MM-dd"));
			}
		}
	}

	@Override
	protected List<OuInfoModel> getOuInfoModelList(String mode) throws java.lang.Exception {
		List<SAPOuInfoModel> dataModelList = getDeptDataModelList(mode);
		List<OuInfoModel> newList = copyCreateEntityList(dataModelList, OuInfoModel.class);

		return newList;
	}

	@Override
	protected List<PositionModel> getPositionModelList(String mode) throws java.lang.Exception {
		List<SAPPositionModel> dataModelList = getPosDataModelList(mode);
		// 岗位数据存在同岗位名不同岗位id（不同部门存在相同岗位名） 将公司名称设置为岗位类别名
		for (SAPPositionModel pos : dataModelList) {
			pos.setpNameClass(getPositionNameClassFromOrgs(pos.getOrgBelongsTo()));
		}
		List<PositionModel> newList = copyCreateEntityList(dataModelList, PositionModel.class);

		return newList;
	}

	@Override
	protected List<UserInfoModel> getUserInfoModelList(String mode) throws java.lang.Exception {
		List<SAPUserInfoModel> dataModelList = getUserDataModelList(mode);
		List<UserInfoModel> newList = copyCreateEntityList(dataModelList, UserInfoModel.class);

		return newList;
	}

	private List<SAPOuInfoModel> getDeptDataModelList(String mode) throws Exception {
		String response = null;
		SAPResJsonModel<SAPOuInfoModel> resJsonModel = null;

		// 获取公司数据
		String requestCompanyUrl = REQUEST_ODATA_ADDRESS + REQUEST_ENTITY_FOCOMPANY;
		response = HttpClientUtil4Sync.doGet(requestCompanyUrl, null, getAuthHeader());
		resJsonModel = mapper.readValue(response, new TypeReference<SAPResJsonModel<SAPOuInfoModel>>() {
		});
		List<SAPOuInfoModel> companyList = resJsonModel.getData().getResults();

		// 获取部门数据
		String requestDeptUrl = REQUEST_ODATA_ADDRESS + REQUEST_ENTITY_FODEPARTMENT;
		response = HttpClientUtil4Sync.doGet(requestDeptUrl, null, getAuthHeader());
		resJsonModel = mapper.readValue(response, new TypeReference<SAPResJsonModel<SAPOuInfoModel>>() {
		});
		List<SAPOuInfoModel> deptList = resJsonModel.getData().getResults();
		// 设置部门的上级公司
		for (SAPOuInfoModel dept : deptList) {
			if (StringUtils.isBlank(dept.getParentID())) {
				// 部门ID前两位对应公司组织的ID
				dept.setParentID(dept.getID().substring(0, 2));
			}
		}

		List<SAPOuInfoModel> retList = new ArrayList<SAPOuInfoModel>();
		retList.addAll(companyList);
		retList.addAll(deptList);

		return retList;
	}

	private List<SAPPositionModel> getPosDataModelList(String mode) throws Exception {
		String response = null;
		SAPResJsonModel<SAPPositionModel> resJsonModel = null;
		List<SAPPositionModel> retList = new ArrayList<SAPPositionModel>();

		String requestPosUrl = REQUEST_ODATA_ADDRESS + REQUEST_ENTITY_POSITION;
		do {
			response = HttpClientUtil4Sync.doGet(requestPosUrl, null, getAuthHeader());
			resJsonModel = mapper.readValue(response, new TypeReference<SAPResJsonModel<SAPPositionModel>>() {
			});
			retList.addAll(resJsonModel.getData().getResults());

			// 下一分页的链接url
			requestPosUrl = resJsonModel.getData().getNextPageUrl();
		} while (StringUtils.isNotBlank(requestPosUrl));

		return retList;
	}

	private List<SAPUserInfoModel> getUserDataModelList(String mode) throws Exception {
		String response = null;
		SAPResJsonModel<SAPUserInfoModel> resJsonModel = null;

		// 查询登录账号名,中文名,性别,工号信息
		String requestUserUrl = REQUEST_ODATA_ADDRESS + REQUEST_ENTITY_PERPERSONAL;
		List<SAPUserInfoModel> perPersonalList = new ArrayList<SAPUserInfoModel>();
		do {
			response = HttpClientUtil4Sync.doGet(requestUserUrl, null, getAuthHeader());
			resJsonModel = mapper.readValue(response, new TypeReference<SAPResJsonModel<SAPUserInfoModel>>() {
			});
			perPersonalList.addAll(resJsonModel.getData().getResults());

			// 下一分页的链接url
			requestUserUrl = resJsonModel.getData().getNextPageUrl();
		} while (StringUtils.isNotBlank(requestUserUrl));

		// 查询手机信息
		requestUserUrl = REQUEST_ODATA_ADDRESS + REQUEST_ENTITY_PERPHONE;
		List<SAPUserInfoModel> perPhoneList = new ArrayList<SAPUserInfoModel>();
		do {
			response = HttpClientUtil4Sync.doGet(requestUserUrl, null, getAuthHeader());
			resJsonModel = mapper.readValue(response, new TypeReference<SAPResJsonModel<SAPUserInfoModel>>() {
			});
			perPhoneList.addAll(resJsonModel.getData().getResults());

			// 下一分页的链接url
			requestUserUrl = resJsonModel.getData().getNextPageUrl();
		} while (StringUtils.isNotBlank(requestUserUrl));

		// 查询邮箱信息
		requestUserUrl = REQUEST_ODATA_ADDRESS + REQUEST_ENTITY_PEREMAIL;
		List<SAPUserInfoModel> perMailList = new ArrayList<SAPUserInfoModel>();
		do {
			response = HttpClientUtil4Sync.doGet(requestUserUrl, null, getAuthHeader());
			resJsonModel = mapper.readValue(response, new TypeReference<SAPResJsonModel<SAPUserInfoModel>>() {
			});
			perMailList.addAll(resJsonModel.getData().getResults());

			// 下一分页的链接url
			requestUserUrl = resJsonModel.getData().getNextPageUrl();
		} while (StringUtils.isNotBlank(requestUserUrl));

		// 查询ID和UserName关联信息
		requestUserUrl = REQUEST_ODATA_ADDRESS + REQUEST_ENTITY_EMPEMPLOYMENT;
		List<SAPUserInfoModel> empEmploymentList = new ArrayList<SAPUserInfoModel>();
		do {
			response = HttpClientUtil4Sync.doGet(requestUserUrl, null, getAuthHeader());
			resJsonModel = mapper.readValue(response, new TypeReference<SAPResJsonModel<SAPUserInfoModel>>() {
			});
			empEmploymentList.addAll(resJsonModel.getData().getResults());

			// 下一分页的链接url
			requestUserUrl = resJsonModel.getData().getNextPageUrl();
		} while (StringUtils.isNotBlank(requestUserUrl));

		// 查询所在部门ID,所在岗位ID,入职日期,有效无效状态信息
		requestUserUrl = REQUEST_ODATA_ADDRESS + REQUEST_ENTITY_EMPJOB;
		List<SAPUserInfoModel> empJobList = new ArrayList<SAPUserInfoModel>();
		do {
			response = HttpClientUtil4Sync.doGet(requestUserUrl, null, getAuthHeader());
			resJsonModel = mapper.readValue(response, new TypeReference<SAPResJsonModel<SAPUserInfoModel>>() {
			});
			empJobList.addAll(resJsonModel.getData().getResults());

			// 下一分页的链接url
			requestUserUrl = resJsonModel.getData().getNextPageUrl();
		} while (StringUtils.isNotBlank(requestUserUrl));

		// 关联手机
		for (SAPUserInfoModel perPhone : perPhoneList) {
			for (SAPUserInfoModel perPersonal : perPersonalList) {
				if (perPhone.getUserName().equals(perPersonal.getUserName())) {
					perPersonal.setMobile(perPhone.getMobile());
					break;
				}
			}
		}
		// 关联邮箱
		for (SAPUserInfoModel perMail : perMailList) {
			for (SAPUserInfoModel perPersonal : perPersonalList) {
				if (perMail.getUserName().equals(perPersonal.getUserName())) {
					perPersonal.setMail(perMail.getMail());
					break;
				}
			}
		}

		// 关联用户ID
		for (SAPUserInfoModel empEmployment : empEmploymentList) {
			for (SAPUserInfoModel perPersonal : perPersonalList) {
				if (empEmployment.getUserName().equals(perPersonal.getUserName())) {
					perPersonal.setID(empEmployment.getID());
					break;
				}
			}
		}

		// 关联部门ID,岗位ID,入职日期
		for (SAPUserInfoModel empJob : empJobList) {
			for (SAPUserInfoModel perPersonal : perPersonalList) {
				if (empJob.getID().equals(perPersonal.getID())) {
					perPersonal.setOrgOuCode(empJob.getOrgOuCode());
					perPersonal.setPostionNo(empJob.getPostionNo());
					perPersonal.setEntryTime(empJob.getEntryTime());
					perPersonal.setStatus(empJob.getStatus());
					break;
				}
			}
		}

		return perPersonalList;
	}

	/**
	 * token放在请求header的Authorization中
	 * 
	 * @return
	 * @throws IOException
	 */
	private List<Header> getAuthHeader() throws IOException {
		List<Header> headers = new ArrayList<Header>();
		headers.add(new BasicHeader("Authorization", "Bearer " + getToken()));
		return headers;
	}

	/**
	 * 获取AccessToken
	 * 
	 * @return
	 * @throws IOException
	 */
	private String getToken() throws IOException {
		if (!isTokenValidate()) {
			String requestUrl = REQUEST_SERVER_ADDRESS + REQUEST_URL_GET_TOKEN;

			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("company_id", "shanghaimi");
			paramMap.put("client_id", "OGQxYTA1YjJlZmQ3MTUwODZlNWY4NmI0MjgzMA");
			paramMap.put("assertion", getSAMLAssertion());
			paramMap.put("grant_type", "urn:ietf:params:oauth:grant-type:saml2-bearer");

			String response = HttpClientUtil4Sync.doPost(requestUrl, paramMap);
			JsonNode jsonNode = mapper.readTree(response);

			// 刷新token
			token = jsonNode.get("access_token").asText();
		}
		System.out.println(token);
		return token;
	}

	/**
	 * 验证token是否过期
	 * 
	 * @return
	 * @throws IOException
	 */
	private boolean isTokenValidate() throws IOException {
		String requestUrl = REQUEST_SERVER_ADDRESS + REQUEST_URL_VALIDATE_TOKEN;

		List<Header> headers = new ArrayList<Header>();
		headers.add(new BasicHeader("Authorization", "Bearer " + token));
		String response = HttpClientUtil4Sync.doGet(requestUrl, null, headers);
		JsonNode jsonNode = mapper.readTree(response).get("expires_in");
		if (jsonNode == null) {
			return false;
		} else {
			String expiresIn = jsonNode.asText();
			System.out.println("token有效期：" + expiresIn);
			// token有效期小于60秒重新获取token
			if (Integer.parseInt(expiresIn) < 60) {
				return false;
			}
		}

		return true;
	}

	/**
	 * 获取SAML Assertion
	 * 
	 * @return
	 * @throws IOException
	 */
	private String getSAMLAssertion() throws IOException {
		String requestUrl = REQUEST_SERVER_ADDRESS + REQUEST_URL_GET_SAML;

		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("client_id", "OGQxYTA1YjJlZmQ3MTUwODZlNWY4NmI0MjgzMA");
		paramMap.put("user_id", "SFADMIN");
		paramMap.put("token_url", "https://api15.sapsf.cn/oauth/token");
		paramMap.put("private_key",
				"TUlJRXZnSUJBREFOQmdrcWhraUc5dzBCQVFFRkFBU0NCS2d3Z2dTa0FnRUFBb0lCQVFDcmZvaGJ1SFNzYlFrbVBuSDBiVUMrZ3ZkUTlIM0JIYjdEclhBR1lWS2pBS1I0RnJZN096dVJCYnlXVzlVY0o0Z1RJa1JuRU8wTVBsb3o0NDdoZzV5bmRHbElsRGhGamh2Qkg4K25JSEJPZGZJUFRyR3QzZXBiWk5wbThERWQ3NEJYeVhzVVVOWitoUHREaTdWajRYeFRhc1BsbVhOOVVZNnBXcWxraWtRc2xwZDBlaG1qQWdmK09oK0s5a05QU1hxVWQvSkNzN0ZMajVpc3FhaU1leHR1MVF1eXFDT0FFODh1L0hsUnR1elpPcXExZ3BRc2hwb1FISUxMenRTV0xZUytGd2JLL05nVnUzb2w4ei9EVWJkamRmcU9yZm4wYXZkeDBVTm1Fa2pmK0ZWdml6SGRwVFJaalFUbnpOSm90bGpVOWN4SDd4a0dFdVVQTkZLcTR3SzFBZ01CQUFFQ2dnRUFGc0ZiWnBBN0t3WHN5TjVRVUphT21MallDQzNvNGM4SW1QNVNkd1pDNk00VkJMbyt3KzFBSWlZWXQvckhkRXpxaVY5cXBDSmkwZFVVTXY1bTN4V1BtOGllajdyYkhKK1duekdSWkNPeHd6ZHpQd1M2SHMvcllPVW85YSs1VkxlSzJCc3I4YTRvUi9GdFJiRXNDR3pMQ21hSVI5YnN0QncyU2RKMTE1bGhJbWxCV1RvcERNZGM4VnduaEswb0p6VGtYVTZqRVlZa1NWVGNqaXZ5ck44OWhBdnN0QTBaK2tIdHMwMzdGUks5eGY4aFpsNEY0VS9tNm81bzRBb2dJSk1BZWxMN25YQ2VTU0xzU09pWHJINjRoUVlUYW5USnUwRXRmZmRMQ004ZE9qUlpydzNTSWdEdWtJWXk0TVlEOTUxaTlzWTVROXgxQ1NBOVFGS1A0dERGb1FLQmdRRGFFVmhzQVNJOVVsT3czNG9EczRsQW5va0VKZEpUSkNObWdsNUJGUDQ0cXBYTzliQW1GWW5UZUJzMmFtRVYxVTlrZnJIUisyMnFNL2NyMndPcDc2NmpUMnhXVkFHNVhTeGw1OGowVGVmN0YvTnVsakxua2RRVWRBY1d3NHVRdzBXNU9TUGlWN3pZOVlxZHAzb1BoUDFNdzVlOC9VNHdUell2cG5MM09CUUNCd0tCZ1FESlV6L0JycFJncTZIZmRHbEh1UXFLNlZ6bkN2SW5TNmhRT0tWWUltbUE3dFQvK2lYNDlkaWxJTUJXWEhLNVVicUtsZnFON1pJMzJwTjFJYkVGSCsxTjJJdHRWUEZhdVhjaUJuRHVTRDRFdU5RRlBpK1Z6SHZIN3MvTUVwRG5hVEV6VDJIYjVINGYvOGIwV2pRQlhwNllFdnNrNWFsY2E5Q0NoYnFGY2xGMll3S0JnUUNMd2VZYmdqYUJlb3ZzcGJkOFc4b3hRZW1xWU5zRk1mb0dMck5kbnFpM2hJeHlrWCtOU095R1BVbWpmRll3ZWY5aDl0amNMUG5CeFlBNStTbkY5YmpGTFpmUFZCT3V5d296WFhPckwycm53QmZoM2gxTjBHWk5uWGNWbmNtS3c3dWgwLzh0aHZHZlV1Njl6NklvNUNDNjIvZ1p4WFlnL0FOTVVxOEh4a2I4YlFLQmdRQzRCV0JCcVZIM0N1cnJWWHptRWoyUXU3NHZjZEhtR1VIQUVnY1FpR1BuZG1jN2syc2lKWlhoV2c1MDRndjdXTGtwOCswUnpoWFk5L0hSSHZxdnRIODBxTHJKTk50b0VoN1MreEErdmNueWh2OUZ5NUd6V3pYSmlvQm1DSkpxODd3RktvcElkSkxVclBjdVkwVWwrMWI3cWwzbFhBZHljeG9rTkJoSjRqU1VBUUtCZ0UwVDdqRDhYREpRSi9CU0lLaEptQVZMaDR6Y1FaRjBvaGdSOEF1bHNhTmFJNERZbWR1dVl2YndYTnBDZytzbndWWDR6am85aElCUWVkWXNKelpOK2FrWWpza0U2WTdBTDZGS3pDakJTSUJWQ2FkMjlDZzlqUXNNa1kzUDYzc1pUck00SVdhc2gyTHorRTZBckIwNlNJb2h5MUc0My9may9QZFA5bXhSZlZaaCMjI3NoYW5naGFpbWk=");

		return HttpClientUtil4Sync.doPost(requestUrl, paramMap);
	}

	public static void main(String[] args) throws IOException, Exception {
		SAPSyncService service = new SAPSyncService();
		// System.out.println(service.isTokenValidate());
		List<SAPOuInfoModel> dataModelList = service.getDeptDataModelList(null);
		System.out.println(dataModelList.size());
		// List<OuInfoModel> entityList =
		// service.copyCreateEntityList(dataModelList, OuInfoModel.class);
		// PrintUtil.printOrgs(entityList);

		// List<SAPPositionModel> dataModelList =
		// service.getPosDataModelList(null);
		// System.out.println(dataModelList.size());
		// List<PositionModel> entityList =
		// service.copyCreateEntityList(dataModelList, PositionModel.class);
		// PrintUtil.logPrintPoss(entityList);

		// List<SAPUserInfoModel> dataModelList =
		// service.getUserDataModelList(null);
		// System.out.println(dataModelList.size());
		// List<UserInfoModel> entityList =
		// service.copyCreateEntityList(dataModelList, UserInfoModel.class);
		// PrintUtil.logPrintUsers(entityList);

		// ObjectMapper objectMapper = service.mapper;
		// String content = "";
		// JsonNode jsonNode = objectMapper.readTree(content);
		// System.out.println(jsonNode.get("").toString());
		// System.out.println(service.getToken());
	}
}
