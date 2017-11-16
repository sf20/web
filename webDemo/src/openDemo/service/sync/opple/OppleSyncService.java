package openDemo.service.sync.opple;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import openDemo.entity.OuInfoModel;
import openDemo.entity.PositionModel;
import openDemo.entity.UserInfoModel;
import openDemo.entity.sync.opple.OpOuInfoModel;
import openDemo.entity.sync.opple.OpResJsonModel;
import openDemo.entity.sync.opple.OpUserInfoModel;
import openDemo.service.sync.AbstractSyncService2;

@Service
public class OppleSyncService extends AbstractSyncService2 implements OppleConfig {
	// 用户接口请求参数名
	private static final String REQUESTID = "RequestId";
	private static final String SERVICENAME = "ServiceName";
	private static final String SERVICEOPERATION = "ServiceOperation";
	private static final String SERVICEVERSION = "ServiceVersion";
	private static final String MODE = "Mode";
	private static final String ESBREQHEAD = "EsbReqHead";
	private static final String ESBREQDATA = "EsbReqData";
	// 用户接口请求参数值
	private static final String REQUEST_URL = "https://esb.opple.com:50830/esb_emp/json"; // "http://esb.opple.com:50831/esb_emp/json";
	private static final String USERNAME = "yxtuser";
	private static final String PASSWORD = "u#5QTwNDaq";
	private static final String SERVICE_NAME = "YXT_ESB_EmpOrgQuery";
	private static final String SERVICE_VERSION = "1.0";
	private static final String SERVICEOPERATION_EMP = "QueryEmpInfo";
	private static final String SERVICEOPERATION_ORG = "QueryOrgInfo";
	// 全量增量区分
	private static final String MODE_FULL = "1";
	private static final String MODE_UPDATE = "2";
	// json请求及转换时字符集类型
	private static final String CHARSET_UTF8 = "UTF-8";
	// 客户提供接口返回的json数据中组织数据和员工数据的key
	private static final String ORG_RES_DATA_KEY = "SapMiddleOrg";
	private static final String EMP_RES_DATA_KEY = "SapMiddleEmp";
	// 日期格式化用
	private static final SimpleDateFormat YYMMDD_DATE_FORMAT = new SimpleDateFormat("yyyyMMdd");
	// 记录日志
	private static final Logger logger = LogManager.getLogger(OppleSyncService.class);

	private List<UserInfoModel> sharedModelList;
	private ObjectMapper mapper;

	public OppleSyncService() {
		super.setApikey(apikey);
		super.setSecretkey(secretkey);
		super.setBaseUrl(baseUrl);
		super.setModeFull(MODE_FULL);
		super.setModeUpdate(MODE_UPDATE);
		super.setIsPosIdProvided(false);
		super.setSyncServiceName(this.getClass().getSimpleName());

		// 创建用于json反序列化的对象
		mapper = new ObjectMapper();
		// 忽略json中多余的属性字段
		mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
	}

	/**
	 * 向客户提供的接口发送POST请求并获取json数据
	 * 
	 * @param requestJsonParam
	 *            请求参数
	 * @return 响应的json字符串
	 * @throws IOException
	 */
	public String getJsonPost(String requestJsonParam) throws IOException {
		HttpClient httpClient = createSSLHttpClient();// HttpClientBuilder.create().build();

		HttpPost httpPost = new HttpPost(REQUEST_URL);
		HttpResponse httpResponse = null;
		String responseStr = null;
		try {
			// 请求header中增加Auth部分
			httpPost.addHeader("Authorization", getBasicAuthHeader(USERNAME, PASSWORD));

			// 构建消息实体 发送Json格式的数据
			StringEntity entity = new StringEntity(requestJsonParam, ContentType.APPLICATION_JSON);
			entity.setContentEncoding(CHARSET_UTF8);
			httpPost.setEntity(entity);

			// 发送post请求
			httpResponse = httpClient.execute(httpPost);

			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				responseStr = EntityUtils.toString(httpResponse.getEntity(), CHARSET_UTF8);
			}

		} finally {
			if (httpClient != null) {
				HttpClientUtils.closeQuietly(httpClient);
			}

			if (httpResponse != null) {
				HttpClientUtils.closeQuietly(httpResponse);
			}
		}
		// TODO to delete
		logger.info("请求用户接口返回数据：" + responseStr);
		return responseStr;
	}

	/**
	 * 创建用于https请求的HttpClient
	 * 
	 * @return
	 */
	private CloseableHttpClient createSSLHttpClient() {
		try {
			SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
			// 实现一个X509TrustManager接口
			X509TrustManager trustManager = new X509TrustManager() {
				@Override
				public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
				}

				@Override
				public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
				}

				@Override
				public X509Certificate[] getAcceptedIssuers() {
					return null;
				}
			};
			sslContext.init(null, new TrustManager[] { trustManager }, null);

			SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext);
			return HttpClients.custom().setSSLSocketFactory(sslsf).build();
		} catch (Exception e) {
			logger.error("创建SSLClient失败", e);
		}

		return HttpClientBuilder.create().build();
	}

	/**
	 * 请求header中增加Auth部分 Auth类型：Basic
	 * 
	 * @param username
	 * @param password
	 * @return Auth请求头内容
	 * @throws UnsupportedEncodingException
	 */
	private String getBasicAuthHeader(String username, String password) throws UnsupportedEncodingException {
		String auth = username + ":" + password;
		byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(CHARSET_UTF8));
		String authHeader = "Basic " + new String(encodedAuth, CHARSET_UTF8);

		return authHeader;
	}

	/**
	 * 构造符合客户要求的请求报文
	 * 
	 * @param serviceOperation
	 *            可在QueryEmpInfo（员工数据）和QueryOrgInfo（组织架构）中二选一
	 * @param mode
	 *            可在1（全量）和2（增量）中二选一。EMP拥有1和2两种模式。Org只有1，全量模式。
	 * @return
	 * @throws JsonProcessingException
	 */
	public String buildReqJson(String serviceOperation, String mode) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> map = new HashMap<String, Object>();

		Map<String, Object> reqHeadMap = new HashMap<String, Object>();
		reqHeadMap.put(REQUESTID, UUID.randomUUID().toString());
		reqHeadMap.put(SERVICENAME, SERVICE_NAME);
		reqHeadMap.put(SERVICEOPERATION, serviceOperation);
		reqHeadMap.put(SERVICEVERSION, SERVICE_VERSION);
		map.put(ESBREQHEAD, reqHeadMap);

		Map<String, Object> reqDataMap = new HashMap<String, Object>();
		reqDataMap.put(MODE, mode);

		map.put(ESBREQDATA, reqDataMap);

		String str = mapper.writeValueAsString(map);

		return str;
	}

	/**
	 * 向客户接口发送请求并返回员工json数据模型集合
	 * 
	 * @param serviceOperation
	 * @param mode
	 * @return
	 * @throws IOException
	 * @throws ReflectiveOperationException
	 */
	private List<OpUserInfoModel> getUserModelList(String serviceOperation, String mode)
			throws IOException, ReflectiveOperationException {
		String jsonString = getJsonPost(buildReqJson(serviceOperation, mode));

		// 将json字符串转为用户json对象数据模型
		OpResJsonModel<OpUserInfoModel> modle = mapper.readValue(jsonString,
				new TypeReference<OpResJsonModel<OpUserInfoModel>>() {
				});

		return modle.getEsbResData().get(EMP_RES_DATA_KEY);
	}

	@Override
	protected boolean isOrgExpired(OuInfoModel org) {
		// 除了编号为 00000001 之外的所有无parentcode的部门都不同步
		if (org.getParentID() == null && Integer.parseInt(org.getID()) != 1) {
			return true;
		} else {
			// 组织废止日期
			Date endDate = org.getEndDate();
			if (endDate == null) {
				return true;
			} else {
				// 组织废止日期比当前时间早的视为过期
				return endDate.compareTo(new Date()) < 0;
			}
		}
	}

	@Override
	protected boolean isPosExpired(PositionModel pos) {
		return false;
	}

	@Override
	protected boolean isUserExpired(UserInfoModel user) {
		if (user.getExpireDate() != null) {
			return true;
		}
		return false;
	}

	@Override
	protected void setRootOrgParentId(List<OuInfoModel> newList) {
		// 无需设置
	}

	@Override
	protected void changePropValues(List<UserInfoModel> newList) {
		for (UserInfoModel tempModel : newList) {
			// 入职日期修改
			String entryTime = tempModel.getEntryTime();
			if (entryTime != null) {
				try {
					tempModel.setEntryTime(DATE_FORMAT.format(YYMMDD_DATE_FORMAT.parse(entryTime)));
				} catch (ParseException e) {
					logger.warn("日期格式有误 " + tempModel.getID() + "：" + entryTime);
				}
			}

			// 性别字符串转换 1：男 2：女
			String sex = tempModel.getSex();
			if ("1".equals(sex)) {
				tempModel.setSex("男");
			} else if ("2".equals(sex)) {
				tempModel.setSex("女");
			}

			// userName <= ID 使用OpUserId作为登录名
			tempModel.setUserName(tempModel.getID());
		}
	}

	@Override
	protected List<OuInfoModel> getOuInfoModelList(String mode) throws Exception {
		String jsonString = getJsonPost(buildReqJson(SERVICEOPERATION_ORG, MODE_FULL));// Org只有全量模式

		// 将json字符串转为组织单位json对象数据模型
		OpResJsonModel<OpOuInfoModel> modle = mapper.readValue(jsonString,
				new TypeReference<OpResJsonModel<OpOuInfoModel>>() {
				});

		List<OuInfoModel> newList = copyCreateEntityList(modle.getEsbResData().get(ORG_RES_DATA_KEY),
				OuInfoModel.class);

		return newList;
	}

	@Override
	protected List<PositionModel> getPositionModelList(String mode) throws Exception {
		List<OpUserInfoModel> dataModelList = getUserModelList(SERVICEOPERATION_EMP, mode);
		List<UserInfoModel> newList = copyCreateEntityList(dataModelList, UserInfoModel.class);
		// 请求接口数据复用
		sharedModelList = newList;

		return getPosListFromUsers(newList);
	}

	@Override
	protected List<UserInfoModel> getUserInfoModelList(String mode) throws Exception {
		// 人员同步请求数据与岗位同步时请求数据一致
		return sharedModelList;
	}
}
