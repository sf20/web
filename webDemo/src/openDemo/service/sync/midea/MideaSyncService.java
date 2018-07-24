package openDemo.service.sync.midea;

import java.io.IOException;
import java.security.MessageDigest;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import openDemo.common.PrintUtil;
import openDemo.entity.OuInfoModel;
import openDemo.entity.PositionModel;
import openDemo.entity.UserInfoModel;
import openDemo.entity.sync.midea.MideaOuInfoModel;
import openDemo.entity.sync.midea.MideaResJsonModel;
import openDemo.entity.sync.midea.MideaUserInfoModel;
import openDemo.service.sync.AbstractSyncService;
import openDemo.utils.HttpClientUtil4Sync;

@Service
public class MideaSyncService extends AbstractSyncService implements MideaConfig {
	private static final String REQUEST_USER_URL = "https://umc.mideadc.com/api/uc/v1/account/search";
	private static final String REQUEST_DEPT_URL = "https://umc.mideadc.com/api/uc/v1/department/search";
	private static final String RESPONSE_STATUS_OK = "0";
	private static final String RESPONSE_STATUS_NORECORD = "10003";
	private static final String SEARCH_BEGIN_TIME = "2018-05-20 00:00:00";
	private static final String TIMESTAMP_FORMAT_YMD = "yyyy-MM-dd";
	private static final String TIMESTAMP_FORMAT_HMS = " 00:00:00";
	// 全量增量区分
	private static final String MODE_FULL = "1";
	private static final String MODE_UPDATE = "2";
	// 记录日志
	private static final Logger logger = LogManager.getLogger(MideaSyncService.class);
	private ObjectMapper mapper;

	public MideaSyncService() {
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
		// 员工状态
		String status = user.getStatus();
		// 非1-试用期 2-正式 8-实习状态的过期
		if (!("1".equals(status) || "2".equals(status) || "8".equals(status))) {
			return true;
		} else {
			return false;
		}
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
			if (entryTime != null && entryTime.length() > 8) {
				try {
					tempModel.setEntryTime(DATE_FORMAT.format(YYMMDD_DATE_FORMAT.parse(entryTime.substring(0, 8))));
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

			// userName <= ID 使用用户账号作为登录名
			tempModel.setUserName(tempModel.getID());
		}
	}

	@Override
	protected List<OuInfoModel> getOuInfoModelList(String mode) throws Exception {
		List<MideaOuInfoModel> dataModelList = new ArrayList<MideaOuInfoModel>();

		if (MODE_FULL.equals(mode)) {
			MideaOuInfoModel rootOrg = new MideaOuInfoModel();
			String rootOrgId = "DC30003353";
			rootOrg.setID(rootOrgId);
			rootOrg.setOuName("置业集团");
			dataModelList.add(rootOrg);

			Date beginTime = DATE_FORMAT.parse(SEARCH_BEGIN_TIME);
			Date endTime = getNextMonthDate(beginTime);
			for (int i = 0; i < calcRequestTimes(); i++) {
				// 每次获取数据日期间隔不能大于30天
				dataModelList.addAll(requestGetOrgModel(beginTime, endTime));

				// 调整下一次请求的开始结束时间
				beginTime = endTime;
				endTime = getNextMonthDate(beginTime);
			}

			// 将“物业总公司”挂到“置业集团”下
			for (MideaOuInfoModel tempModel : dataModelList) {
				// “物业总公司”的部门编号为“DC4203317441”
				if ("DC4203317441".equals(tempModel.getID())) {
					tempModel.setParentID(rootOrgId);
					break;
				}
			}
		} else {
			// 获取增量数据
			Date endTime = new Date();
			Date beginTime = getYesterdayDate(endTime);

			dataModelList = requestGetOrgModel(beginTime, endTime);
		}

		List<OuInfoModel> newList = copyCreateEntityList(dataModelList, OuInfoModel.class);

		return newList;
	}

	private List<MideaOuInfoModel> requestGetOrgModel(Date beginTime, Date endTime) throws Exception {
		String jsonString = requestGetJson(REQUEST_DEPT_URL, beginTime, endTime);

		// 将json字符串转为用户json对象数据模型
		MideaResJsonModel<MideaOuInfoModel> resJsonModel = mapper.readValue(jsonString,
				new TypeReference<MideaResJsonModel<MideaOuInfoModel>>() {
				});

		// 返回数据状态判断
		Map<String, String> resHeadMap = resJsonModel.getHead();
		String responseCode = resHeadMap.get("responseCode");
		if (!RESPONSE_STATUS_OK.equals(responseCode)) {
			if (RESPONSE_STATUS_NORECORD.equals(responseCode)) {
				return new ArrayList<MideaOuInfoModel>(0);
			} else {
				throw new IOException(
						"获取客户接口[" + this.getClass().getSimpleName() + "]数据错误：" + resHeadMap.get("responseMessage"));
			}
		}

		return resJsonModel.getBody();
	}

	@Override
	protected List<PositionModel> getPositionModelList(String mode) throws Exception {
		List<UserInfoModel> userInfoModelList = getUserInfoModelList(mode);

		return getPosListFromUsers(userInfoModelList);
	}

	@Override
	protected List<UserInfoModel> getUserInfoModelList(String mode) throws Exception {
		List<MideaUserInfoModel> dataModelList = new ArrayList<MideaUserInfoModel>();

		if (MODE_FULL.equals(mode)) {
			Date beginTime = DATE_FORMAT.parse(SEARCH_BEGIN_TIME);
			Date endTime = getNextMonthDate(beginTime);
			for (int i = 0; i < calcRequestTimes(); i++) {
				// 每次获取数据日期间隔不能大于30天
				dataModelList.addAll(requestGetUserModel(beginTime, endTime));

				// 调整下一次请求的开始结束时间
				beginTime = endTime;
				endTime = getNextMonthDate(beginTime);
			}
		} else {
			// 获取增量数据
			Date endTime = new Date();
			Date beginTime = getYesterdayDate(endTime);

			dataModelList = requestGetUserModel(beginTime, endTime);
		}

		List<UserInfoModel> newList = copyCreateEntityList(dataModelList, UserInfoModel.class);

		return newList;
	}

	private Date getNextMonthDate(Date beginTime) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(beginTime);
		calendar.add(Calendar.DAY_OF_YEAR, 30);

		return calendar.getTime();
	}

	private List<MideaUserInfoModel> requestGetUserModel(Date beginTime, Date endTime) throws Exception {
		String jsonString = requestGetJson(REQUEST_USER_URL, beginTime, endTime);

		// 将json字符串转为用户json对象数据模型
		MideaResJsonModel<MideaUserInfoModel> resJsonModel = mapper.readValue(jsonString,
				new TypeReference<MideaResJsonModel<MideaUserInfoModel>>() {
				});

		// 返回数据状态判断
		Map<String, String> resHeadMap = resJsonModel.getHead();
		String responseCode = resHeadMap.get("responseCode");
		if (!RESPONSE_STATUS_OK.equals(responseCode)) {
			if (RESPONSE_STATUS_NORECORD.equals(responseCode)) {
				return new ArrayList<MideaUserInfoModel>(0);
			} else {
				throw new IOException(
						"获取客户接口[" + this.getClass().getSimpleName() + "]数据错误：" + resHeadMap.get("responseMessage"));
			}
		}

		return resJsonModel.getBody();
	}

	private String requestGetJson(String requestUrl, Date beginTime, Date endTime) throws Exception {
		// 获取客户接口参数值
		String customerCode = "c201807101";
		String secretId = "c3641786958f4bdeb9b01dddda9578fa";
		String secretKey = "b77a32abdd8f4ca2a6505ba463482927";
		// 本次请求时间戳
		long timeMillis = System.currentTimeMillis();
		// 本次请求随机5位值
		int random = (int) ((Math.random() * 9 + 1) * 10000);

		SortedMap<String, String> map = new TreeMap<>();
		map.put("customerCode", customerCode);
		map.put("secretId", secretId);
		map.put("timestamp", Long.toString(timeMillis));
		map.put("nonce", Integer.toString(random));
		map.put("beginModifyTimestamp", DateFormatUtils.format(beginTime, TIMESTAMP_FORMAT_YMD) + TIMESTAMP_FORMAT_HMS);
		map.put("endModifyTimestamp", DateFormatUtils.format(endTime, TIMESTAMP_FORMAT_YMD) + TIMESTAMP_FORMAT_HMS);

		// 签名操作
		StringBuilder sb = new StringBuilder();
		for (Entry<String, String> entry : map.entrySet()) {
			sb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
		}
		sb.append("key=").append(secretKey);
		String signature = md5(sb.toString());
		map.put("signature", signature);

		// 访问
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = HttpClientUtil4Sync.doPost(requestUrl, mapper.writeValueAsString(map));

		return jsonString;
	}

	private int calcRequestTimes() throws ParseException {
		Date beginTime = DATE_FORMAT.parse(SEARCH_BEGIN_TIME);
		Date endTime = new Date();

		long oneMonthTime = 30 * 24 * 60 * 60 * 1000l;
		long timeGap = endTime.getTime() - beginTime.getTime();
		long intVal = timeGap / oneMonthTime;
		long modVal = timeGap % oneMonthTime > 0 ? 1 : 0;

		return (int) (intVal + modVal);
	}

	/**
	 * 生成 md5
	 *
	 * @param data
	 *            待处理数据
	 * @return MD5结果
	 */
	private String md5(String data) throws Exception {
		java.security.MessageDigest md = MessageDigest.getInstance("MD5");
		byte[] array = md.digest(data.getBytes("UTF-8"));
		StringBuilder sb = new StringBuilder();
		for (byte item : array) {
			sb.append(Integer.toHexString((item & 0xFF) | 0x100).substring(1, 3));
		}
		// 转换成大写
		return sb.toString().toUpperCase();
	}

	public static void main(String[] args) throws Exception {
		MideaSyncService service = new MideaSyncService();
		List<UserInfoModel> dataModelList = service.getUserInfoModelList(MODE_FULL);
		System.out.println(dataModelList.size());
		PrintUtil.logPrintUsers(dataModelList);
	}
}
