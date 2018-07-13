package openDemo.service.sync.kaiying;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import openDemo.common.PrintUtil;
import openDemo.entity.OuInfoModel;
import openDemo.entity.PositionModel;
import openDemo.entity.UserInfoModel;
import openDemo.entity.sync.kaiying.KaiyingOuInfoModel;
import openDemo.entity.sync.kaiying.KaiyingPositionModel;
import openDemo.entity.sync.kaiying.KaiyingResJsonModel;
import openDemo.service.sync.AbstractSyncService;
import openDemo.utils.HttpClientUtil4Sync;

@Service
public class KaiyingSyncService extends AbstractSyncService implements KaiyingConfig {
	// 用户接口请求参数值
	private static final String REQUEST_ORG_URL = "http://api-oa.kingnet.com/api/org/list";
	private static final String ENABLE_STATUS = "active";
	private static final int RESPONSE_STATUS_OK = 1;

	private ObjectMapper mapper;

	public KaiyingSyncService() {
		super.setApikey(apikey);
		super.setSecretkey(secretkey);
		super.setBaseUrl(baseUrl);
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
		// 获取客户接口列表数据
		String appId = "QzEBCnBDKhOzigSiJRu1qsQ5CnynG8F5";
		String appKey = "fkV0BqhNyxptbTV1vkc35QQPTylNXDek";
		long ts = System.currentTimeMillis() / 1000;

		String message = "app_id=" + appId + "&ts=" + ts;
		String hash = getMd5(message + appKey);

		String url = requestUrl + "?app_id=" + appId + "&ts=" + ts + "&encryptionType=md5" + "&sign=" + hash;
		String jsonString = HttpClientUtil4Sync.doGet(url);

		// 将json字符串转为用户json对象数据模型
		KaiyingResJsonModel<T> resJsonModel = null;
		// 类型判断传入不同类型参数
		if (classType.isAssignableFrom(KaiyingPositionModel.class)) {
			resJsonModel = mapper.readValue(jsonString, new TypeReference<KaiyingResJsonModel<KaiyingPositionModel>>() {
			});
		} else if (classType.isAssignableFrom(KaiyingOuInfoModel.class)) {
			resJsonModel = mapper.readValue(jsonString, new TypeReference<KaiyingResJsonModel<KaiyingOuInfoModel>>() {
			});
		}

		// 返回数据状态判断
		if (RESPONSE_STATUS_OK != resJsonModel.getCode()) {
			throw new IOException("获取客户接口[" + classType.getSimpleName() + "]数据错误：" + resJsonModel.getMsg());
		}

		return resJsonModel.getData();
	}

	@Override
	protected boolean isOrgExpired(OuInfoModel org) {
		String status = org.getStatus();
		// 部门状态:(active：有效,inactive：删除)
		if (!ENABLE_STATUS.equals(status)) {
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
		return false;
	}

	@Override
	protected void setRootOrgParentId(List<OuInfoModel> newList) {
		// 无需设置
	}

	@Override
	protected void changePropValues(List<UserInfoModel> newList) {
	}

	@Override
	protected List<OuInfoModel> getOuInfoModelList(String mode) throws Exception {
		List<KaiyingOuInfoModel> modelList = getDataModelList(mode, REQUEST_ORG_URL, KaiyingOuInfoModel.class);
		List<OuInfoModel> newList = copyCreateEntityList(modelList, OuInfoModel.class);

		return newList;
	}

	@Override
	protected List<PositionModel> getPositionModelList(String mode) throws Exception {
		// 后期调整为同步职位名 不需要获取岗位列表
		return new ArrayList<PositionModel>(0);
	}

	@Override
	protected List<UserInfoModel> getUserInfoModelList(String mode) throws Exception {
		return new ArrayList<UserInfoModel>(0);
	}

	public static void main(String[] args) throws Exception {
		// String appId = "QzEBCnBDKhOzigSiJRu1qsQ5CnynG8F5";
		// long ts = System.currentTimeMillis() / 1000;
		//
		// String message = "app_id=" + appId + "&ts=" + ts;
		// String appKey = "fkV0BqhNyxptbTV1vkc35QQPTylNXDek";
		// try {
		// // Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
		// // SecretKeySpec secret_key = new SecretKeySpec(appKey.getBytes(),
		// // "HmacSHA256");
		// // sha256_HMAC.init(secret_key);
		// //
		// Base64.encodeBase64String(sha256_HMAC.doFinal(message.getBytes()));
		//
		// String hash = getMd5(message + appKey);//
		// SHA256Util.SHA256Encrypt(message);
		//
		// System.out.println(message);
		// System.out.println(hash);
		// String url = "http://api-oa.kingnet.com/api/post/list?app_id=" +
		// appId + "&ts=" + ts + "&encryptionType=md5"
		// + "&sign=" + hash;
		// String result = HttpClientUtil4Sync.doGet(url);
		// System.out.println(result);
		// } catch (Exception e) {
		// System.out.println("Error");
		// }

		KaiyingSyncService service = new KaiyingSyncService();
		List<PositionModel> ouInfoModelList = service.getPositionModelList(null);
		PrintUtil.printPoss(ouInfoModelList);
	}

	public static String getMd5(String plainText) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(plainText.getBytes());
			byte b[] = md.digest();

			int i;

			StringBuilder buf = new StringBuilder("");
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}
			// 32位加密
			return buf.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}

	}
}
