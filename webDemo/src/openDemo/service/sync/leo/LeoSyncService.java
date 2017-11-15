package openDemo.service.sync.leo;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.message.BasicHeader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import openDemo.entity.OuInfoModel;
import openDemo.entity.PositionModel;
import openDemo.entity.UserInfoModel;
import openDemo.entity.sync.leo.LeoOuInfoModel;
import openDemo.entity.sync.leo.LeoPositionModel;
import openDemo.entity.sync.leo.LeoResData;
import openDemo.entity.sync.leo.LeoResJsonModel;
import openDemo.entity.sync.leo.LeoUserInfoModel;
import openDemo.service.sync.AbstractSyncService2;
import openDemo.utils.HttpClientUtil4Sync;

@Service
public class LeoSyncService extends AbstractSyncService2 implements LeoConfig {
	// 用户接口请求参数值
	private static final String REQUEST_EMP_URL = "https://open.leo.cn/v1/hr/employees/last-updated";
	private static final String REQUEST_ORG_URL = "https://open.leo.cn/v1/hr/origizations/last-updated";
	private static final String REQUEST_POS_URL = "https://open.leo.cn/v1/hr/job-positions/last-updated";
	private static final String REQUEST_PARAM_FROM = "from";
	private static final String REQUEST_PARAM_PAGE = "p";
	private static final String MODE_FULL = "1";
	private static final String MODE_UPDATE = "2";
	private static final String FROM_DATE = "2017-08-01";// TODO
	private static final String ENABLE_STATUS = "1";
	private static final String DELETED_STATUS = "1";
	private static final String USER_DISABLE_STATUS = "8";
	private static final int DEFAULT_PAGE_SIZE = 50;
	private static final int RESPONSE_STATUS_OK = 200;
	// 记录日志
	private static final Logger logger = LogManager.getLogger(LeoSyncService.class);
	private ObjectMapper mapper;

	public LeoSyncService() {
		super.setApikey(apikey);
		super.setSecretkey(secretkey);
		super.setBaseUrl(baseUrl);
		super.setModeFull(MODE_FULL);
		super.setModeUpdate(MODE_UPDATE);
		super.setSyncServiceName(this.getClass().getSimpleName());

		// 创建用于json反序列化的对象
		mapper = new ObjectMapper();
		// 忽略json中多余的属性字段
		mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		// json字符串的日期格式
		mapper.setDateFormat(DATE_FORMAT);
	}

	/**
	 * 向客户接口发送请求并返回json数据模型集合
	 * 
	 * @param <T>
	 * 
	 * @param mode
	 * @param requestUrl
	 * @param classType
	 * @return
	 * @throws IOException
	 */
	private <T> List<T> getDataModelList(String mode, String requestUrl, Class<T> classType) throws IOException {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put(REQUEST_PARAM_FROM, getTimestamp(mode));
		// 用于认证的header信息
		List<Header> authHeader = getAuthHeader();

		List<T> tempList = new ArrayList<T>();
		// 首次请求
		Map<Integer, List<T>> dataMap = requestGetData(requestUrl, paramMap, authHeader, classType);
		tempList.addAll(dataMap.values().iterator().next());

		// 获取total值后请求全部数据
		int total = dataMap.keySet().iterator().next();
		for (int i = 0; i < calcRequestTimes(total, DEFAULT_PAGE_SIZE) - 1; i++) {
			// 请求页码从2开始
			paramMap.put(REQUEST_PARAM_PAGE, i + 2);
			dataMap = requestGetData(requestUrl, paramMap, authHeader, classType);
			tempList.addAll(dataMap.values().iterator().next());
		}

		return tempList;
	}

	/**
	 * 向客户接口请求数据并解析
	 * 
	 * @param requestUrl
	 * @param paramMap
	 * @param headers
	 * @param classType
	 * @return Map集合 key：返回数据total值 value：岗位或组织或人员数据集合
	 * @throws IOException
	 */
	private <T> Map<Integer, List<T>> requestGetData(String requestUrl, Map<String, Object> paramMap,
			List<Header> headers, Class<T> classType) throws IOException {
		String jsonString = HttpClientUtil4Sync.doGet(requestUrl, paramMap, headers);
		// logger.info(jsonString);

		// 将json字符串转为用户json对象数据模型
		LeoResJsonModel<T> resJsonModel = null;
		// 将json字符串中的jobPositions, origizations, employees统一替换成dataList
		String replacement = "dataList";
		// 类型判断传入不同类型参数
		if (classType.isAssignableFrom(LeoPositionModel.class)) {
			jsonString = jsonString.replaceFirst("jobPositions", replacement);
			resJsonModel = mapper.readValue(jsonString, new TypeReference<LeoResJsonModel<LeoPositionModel>>() {
			});
		} else if (classType.isAssignableFrom(LeoOuInfoModel.class)) {
			jsonString = jsonString.replaceFirst("origizations", replacement);
			resJsonModel = mapper.readValue(jsonString, new TypeReference<LeoResJsonModel<LeoOuInfoModel>>() {
			});
		} else if (classType.isAssignableFrom(LeoUserInfoModel.class)) {
			jsonString = jsonString.replaceFirst("employees", replacement);
			resJsonModel = mapper.readValue(jsonString, new TypeReference<LeoResJsonModel<LeoUserInfoModel>>() {
			});
		}

		Map<Integer, List<T>> dataMap = new HashMap<Integer, List<T>>();
		List<T> dataList = new ArrayList<T>();
		// 返回数据状态判断
		if (RESPONSE_STATUS_OK == resJsonModel.getCode()) {
			LeoResData<T> data = resJsonModel.getData();
			if (data != null) {
				dataList = data.getDataList();
				dataMap.put(data.getTotal(), dataList);
			} else {
				throw new IOException("获取客户接口[" + classType.getSimpleName() + "]数据data为null");
			}
		} else {
			throw new IOException("获取客户接口[" + classType.getSimpleName() + "]数据错误：" + resJsonModel.getMessage());
		}

		return dataMap;
	}

	/**
	 * 根据数据总量和每页数量计算应当请求的次数
	 * 
	 * @param totalCount
	 * @param pageSize
	 * @return
	 */
	private int calcRequestTimes(int totalCount, int pageSize) {
		int reqTimes = totalCount / pageSize;
		if (totalCount % pageSize > 0) {
			reqTimes = reqTimes + 1;
		}

		return reqTimes;
	}

	/**
	 * 获取全量或增量模式下请求的时间戳参数值
	 * 
	 * @param mode
	 * @return
	 */
	private int getTimestamp(String mode) {
		// 默认时间戳
		int timestamp = (int) (new Date().getTime() / 1000);
		if (MODE_FULL.equals(mode)) {
			try {
				timestamp = (int) (DATE_FORMAT.parse(FROM_DATE).getTime() / 1000);
			} catch (ParseException e) {
				logger.error("获取时间戳失败", e);
			}
		} else {
			// 当日零点时间
			Calendar c = Calendar.getInstance();
			c.set(Calendar.HOUR_OF_DAY, 0);
			c.set(Calendar.MINUTE, 0);
			c.set(Calendar.SECOND, 0);
			timestamp = (int) (c.getTimeInMillis() / 1000);
		}
		return timestamp;
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
	 * 获取token
	 * 
	 * @return
	 * @throws IOException
	 */
	private String getToken() throws IOException {
		String url = "https://open.leo.cn/v1/authentication/oauth2/get-token";

		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("access_key", "oleo_42db6ee396eb8765435e44446befad8e");
		paramMap.put("secret_key", "5f81f9a50e7c4043efece652b7a82be2d0d90839b9b550b66c1fb865480a6aad");

		// 从json字符串中解析token
		JsonNode jsonNode = mapper.readTree(HttpClientUtil4Sync.doPost(url, paramMap));
		String token = jsonNode.get("data").get("token").asText();

		return token;
	}

	@Override
	protected boolean isOrgExpired(OuInfoModel org) {
		String status = org.getStatus();
		String deleteStatus = org.getDeleteStatus();
		// 是否启用为0或者是否删除为1的场合 组织过期
		if (!ENABLE_STATUS.equals(status) || DELETED_STATUS.equals(deleteStatus)) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	protected boolean isPosExpired(PositionModel pos) {
		String status = pos.getStatus();
		String deleteStatus = pos.getDeleteStatus();
		// 是否启用为0或者是否删除为1的场合 岗位过期
		if (!ENABLE_STATUS.equals(status) || DELETED_STATUS.equals(deleteStatus)) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	protected boolean isUserExpired(UserInfoModel user) {
		String status = user.getStatus();
		String deleteStatus = user.getDeleteStatus();
		// 用户状态为8:离职 或者是否删除为1的场合下过期
		if (USER_DISABLE_STATUS.equals(status) || DELETED_STATUS.equals(deleteStatus)) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	protected void setRootOrgParentId(List<OuInfoModel> newList) {
		for (OuInfoModel org : newList) {
			if ("-2".equals(org.getParentID())) {
				org.setParentID(null);
				break;
			}
		}
	}

	@Override
	protected void changePropValues(List<UserInfoModel> newList) {
		for (UserInfoModel tempModel : newList) {
			// 性别字符串转换 0：男 1：女
			String sex = tempModel.getSex();
			if ("0".equals(sex)) {
				tempModel.setSex("男");
			} else if ("1".equals(sex)) {
				tempModel.setSex("女");
			}

			// userName <= mail 邮箱作为用户登录名
			tempModel.setUserName(tempModel.getMail());
		}
	}

	@Override
	protected List<OuInfoModel> getOuInfoModelList(String mode) throws Exception {
		List<LeoOuInfoModel> modelList = getDataModelList(mode, REQUEST_ORG_URL, LeoOuInfoModel.class);
		List<OuInfoModel> newList = copyCreateEntityList(modelList, OuInfoModel.class);

		return newList;
	}

	@Override
	protected List<PositionModel> getPositionModelList(String mode) throws Exception {
		List<LeoPositionModel> userModelList = getDataModelList(mode, REQUEST_POS_URL, LeoPositionModel.class);
		List<PositionModel> newList = copyCreateEntityList(userModelList, PositionModel.class);

		return newList;
	}

	@Override
	protected List<UserInfoModel> getUserInfoModelList(String mode) throws Exception {
		List<LeoUserInfoModel> modelList = getDataModelList(mode, REQUEST_EMP_URL, LeoUserInfoModel.class);
		List<UserInfoModel> newList = copyCreateEntityList(modelList, UserInfoModel.class);

		return newList;
	}

}
