package openDemo.service.sync.weichuang;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.message.BasicHeader;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import openDemo.entity.OuInfoModel;
import openDemo.entity.PositionModel;
import openDemo.entity.UserInfoModel;
import openDemo.service.sync.AbstractSyncService;
import openDemo.utils.HttpClientUtil4Sync;

@Service
public class SAPSyncService extends AbstractSyncService implements JiayangConfig {
	// 参数配置
	private static final String REQUEST_ODATA_ADDRESS = "https://api15.sapsf.cn/odata/v2/";
	private static final String REQUEST_SERVER_ADDRESS = "https://api15.sapsf.cn/";
	private static final String REQUEST_URL_GET_SAML = "oauth/idp";
	private static final String REQUEST_URL_GET_TOKEN = "oauth/token";
	private static final String REQUEST_ENTITY_USER = "User?$format=json";
	private static final String REQUEST_ENTITY_DEPT = "Department";
	private static final String REQUEST_ENTITY_JOB = "Job";

	// 全量增量区分
	private static final String MODE_FULL = "1";
	private static final String MODE_UPDATE = "2";
	// json解析用
	private ObjectMapper mapper;

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
		return false;
	}

	@Override
	protected boolean isPosExpired(PositionModel pos) {
		return false;
	}

	@Override
	protected boolean isUserExpired(UserInfoModel user) {
		String status = user.getStatus();
		// isAvailable为true有效为false无效
		if ("false".equals(status)) {
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
			// 性别字符串转换1：男 2：女
			String sex = tempModel.getSex();
			if ("1".equals(sex)) {
				tempModel.setSex("男");
			} else if ("2".equals(sex)) {
				tempModel.setSex("女");
			}

			// ID <= userName 用户登录名作为用户ID
			tempModel.setID(tempModel.getUserName());
		}
	}

	@Override
	protected List<OuInfoModel> getOuInfoModelList(String mode) throws java.lang.Exception {

		return null;
	}

	@Override
	protected List<PositionModel> getPositionModelList(String mode) throws java.lang.Exception {

		return null;
	}

	@Override
	protected List<UserInfoModel> getUserInfoModelList(String mode) throws java.lang.Exception {

		return null;
	}

	private List getDataModelList(String mode) throws Exception {
		String requestUrl = REQUEST_ODATA_ADDRESS + REQUEST_ENTITY_USER;

		String response = HttpClientUtil4Sync.doGet(requestUrl, null, getAuthHeader());
		System.out.println(response);
		// JsonNode jsonNode = mapper.readTree(response);
		// jsonNode.get("").toString();

		return null;
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
		String requestUrl = REQUEST_SERVER_ADDRESS + REQUEST_URL_GET_TOKEN;

		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("company_id", "shanghaimi");
		paramMap.put("client_id", "OGQxYTA1YjJlZmQ3MTUwODZlNWY4NmI0MjgzMA");
		// paramMap.put("api_key", "");
		paramMap.put("assertion", getSAMLAssertion());
		paramMap.put("grant_type", "urn:ietf:params:oauth:grant-type:saml2-bearer");

		String response = HttpClientUtil4Sync.doPost(requestUrl, paramMap);
		JsonNode jsonNode = mapper.readTree(response);

		return jsonNode.get("access_token").asText();
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
		paramMap.put("user_id", "SFINT");
		paramMap.put("token_url", "https://api15.sapsf.cn/oauth/token");
		paramMap.put("private_key",
				"TUlJRXZnSUJBREFOQmdrcWhraUc5dzBCQVFFRkFBU0NCS2d3Z2dTa0FnRUFBb0lCQVFDcmZvaGJ1SFNzYlFrbVBuSDBiVUMrZ3ZkUTlIM0JIYjdEclhBR1lWS2pBS1I0RnJZN096dVJCYnlXVzlVY0o0Z1RJa1JuRU8wTVBsb3o0NDdoZzV5bmRHbElsRGhGamh2Qkg4K25JSEJPZGZJUFRyR3QzZXBiWk5wbThERWQ3NEJYeVhzVVVOWitoUHREaTdWajRYeFRhc1BsbVhOOVVZNnBXcWxraWtRc2xwZDBlaG1qQWdmK09oK0s5a05QU1hxVWQvSkNzN0ZMajVpc3FhaU1leHR1MVF1eXFDT0FFODh1L0hsUnR1elpPcXExZ3BRc2hwb1FISUxMenRTV0xZUytGd2JLL05nVnUzb2w4ei9EVWJkamRmcU9yZm4wYXZkeDBVTm1Fa2pmK0ZWdml6SGRwVFJaalFUbnpOSm90bGpVOWN4SDd4a0dFdVVQTkZLcTR3SzFBZ01CQUFFQ2dnRUFGc0ZiWnBBN0t3WHN5TjVRVUphT21MallDQzNvNGM4SW1QNVNkd1pDNk00VkJMbyt3KzFBSWlZWXQvckhkRXpxaVY5cXBDSmkwZFVVTXY1bTN4V1BtOGllajdyYkhKK1duekdSWkNPeHd6ZHpQd1M2SHMvcllPVW85YSs1VkxlSzJCc3I4YTRvUi9GdFJiRXNDR3pMQ21hSVI5YnN0QncyU2RKMTE1bGhJbWxCV1RvcERNZGM4VnduaEswb0p6VGtYVTZqRVlZa1NWVGNqaXZ5ck44OWhBdnN0QTBaK2tIdHMwMzdGUks5eGY4aFpsNEY0VS9tNm81bzRBb2dJSk1BZWxMN25YQ2VTU0xzU09pWHJINjRoUVlUYW5USnUwRXRmZmRMQ004ZE9qUlpydzNTSWdEdWtJWXk0TVlEOTUxaTlzWTVROXgxQ1NBOVFGS1A0dERGb1FLQmdRRGFFVmhzQVNJOVVsT3czNG9EczRsQW5va0VKZEpUSkNObWdsNUJGUDQ0cXBYTzliQW1GWW5UZUJzMmFtRVYxVTlrZnJIUisyMnFNL2NyMndPcDc2NmpUMnhXVkFHNVhTeGw1OGowVGVmN0YvTnVsakxua2RRVWRBY1d3NHVRdzBXNU9TUGlWN3pZOVlxZHAzb1BoUDFNdzVlOC9VNHdUell2cG5MM09CUUNCd0tCZ1FESlV6L0JycFJncTZIZmRHbEh1UXFLNlZ6bkN2SW5TNmhRT0tWWUltbUE3dFQvK2lYNDlkaWxJTUJXWEhLNVVicUtsZnFON1pJMzJwTjFJYkVGSCsxTjJJdHRWUEZhdVhjaUJuRHVTRDRFdU5RRlBpK1Z6SHZIN3MvTUVwRG5hVEV6VDJIYjVINGYvOGIwV2pRQlhwNllFdnNrNWFsY2E5Q0NoYnFGY2xGMll3S0JnUUNMd2VZYmdqYUJlb3ZzcGJkOFc4b3hRZW1xWU5zRk1mb0dMck5kbnFpM2hJeHlrWCtOU095R1BVbWpmRll3ZWY5aDl0amNMUG5CeFlBNStTbkY5YmpGTFpmUFZCT3V5d296WFhPckwycm53QmZoM2gxTjBHWk5uWGNWbmNtS3c3dWgwLzh0aHZHZlV1Njl6NklvNUNDNjIvZ1p4WFlnL0FOTVVxOEh4a2I4YlFLQmdRQzRCV0JCcVZIM0N1cnJWWHptRWoyUXU3NHZjZEhtR1VIQUVnY1FpR1BuZG1jN2syc2lKWlhoV2c1MDRndjdXTGtwOCswUnpoWFk5L0hSSHZxdnRIODBxTHJKTk50b0VoN1MreEErdmNueWh2OUZ5NUd6V3pYSmlvQm1DSkpxODd3RktvcElkSkxVclBjdVkwVWwrMWI3cWwzbFhBZHljeG9rTkJoSjRqU1VBUUtCZ0UwVDdqRDhYREpRSi9CU0lLaEptQVZMaDR6Y1FaRjBvaGdSOEF1bHNhTmFJNERZbWR1dVl2YndYTnBDZytzbndWWDR6am85aElCUWVkWXNKelpOK2FrWWpza0U2WTdBTDZGS3pDakJTSUJWQ2FkMjlDZzlqUXNNa1kzUDYzc1pUck00SVdhc2gyTHorRTZBckIwNlNJb2h5MUc0My9may9QZFA5bXhSZlZaaCMjI3NoYW5naGFpbWk=");

		return HttpClientUtil4Sync.doPost(requestUrl, paramMap);
	}

	public static void main(String[] args) throws IOException, Exception {
		SAPSyncService service = new SAPSyncService();
		service.getDataModelList(null);
		// ObjectMapper objectMapper = service.mapper;
		// String content = "";
		// JsonNode jsonNode = objectMapper.readTree(content);
		// System.out.println(jsonNode.get("").toString());
		// System.out.println(service.getToken());
	}
}
