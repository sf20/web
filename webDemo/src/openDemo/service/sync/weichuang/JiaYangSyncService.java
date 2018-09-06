package openDemo.service.sync.weichuang;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.sf.json.JSONObject;
import openDemo.common.PrintUtil;
import openDemo.entity.OuInfoModel;
import openDemo.entity.PositionModel;
import openDemo.entity.UserInfoModel;
import openDemo.entity.sync.weichuang.JiaYangOuInfoModel;
import openDemo.entity.sync.weichuang.JiaYangPositionModel;
import openDemo.entity.sync.weichuang.JiaYangUserInfoModel;
import openDemo.service.sync.AbstractSyncService;
import openDemo.utils.HttpClientUtil4Sync;

@Service
public class JiaYangSyncService extends AbstractSyncService implements WeiChuangConfig {
	// 参数配置
	private static final String REQUEST_SERVER_ADDRESS = "http://192.168.1.76:8004";
	private static final String REQUEST_URL_GET_TOKEN = "/KayangWebApi/Data/StartSession";
	private static final String REQUEST_URL_GET_DATA = "/KayangWebApi/Data/Getdata";
	private static final String REQUEST_URL_CLEAR_TOKEN = "/KayangWebApi/Data/CloseSession";

	// 全量增量区分
	private static final String MODE_FULL = "1";
	private static final String MODE_UPDATE = "2";
	// 记录日志
	private static final Logger logger = LogManager.getLogger(JiaYangSyncService.class);
	// json解析用
	private ObjectMapper mapper;

	public JiaYangSyncService() {
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
		// TODO 员工是否是一线工人 1：是 2：否
		String deleteStatus = user.getDeleteStatus();
		if ((status == null || "离职".equals(status)) || "1".equals(deleteStatus)) {
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
			// ID <= userName 用户登录名作为用户ID
			tempModel.setID(tempModel.getUserName());
		}
	}

	@Override
	protected List<OuInfoModel> getOuInfoModelList(String mode) throws java.lang.Exception {
		List<JiaYangOuInfoModel> orgDataModelList = getDataModelList(mode, JiaYangOuInfoModel.class);
		// 存放无重复公司数据的map集合 key:公司部门编号 value:公司名
		Map<String, String> compMap = new HashMap<String, String>();
		for (JiaYangOuInfoModel org : orgDataModelList) {
			String companayId = org.getCompId();
			String companayName = org.getCompName();
			if (!compMap.containsKey(companayId)) {
				compMap.put(companayId, companayName);
			}
		}

		// 设置部门所属公司
		for (JiaYangOuInfoModel org : orgDataModelList) {
			// 对没有上级公司的部门进行设置
			if (org.getParentID() == null) {
				String compId = org.getCompId();
				if (compMap.containsKey(compId)) {
					org.setParentID(compId);
				}
			}
		}

		// 将公司添加到部门集合
		for (Map.Entry<String, String> entry : compMap.entrySet()) {
			JiaYangOuInfoModel ouInfo = new JiaYangOuInfoModel();
			ouInfo.setID(entry.getKey());
			ouInfo.setOuName(entry.getValue());
			orgDataModelList.add(0, ouInfo);
		}

		List<OuInfoModel> orgList = copyCreateEntityList(orgDataModelList, OuInfoModel.class);
		return orgList;
	}

	@Override
	protected List<PositionModel> getPositionModelList(String mode) throws java.lang.Exception {
		List<JiaYangPositionModel> dataModelList = getDataModelList(mode, JiaYangPositionModel.class);
		// 岗位数据存在同岗位名不同岗位id（不同部门存在相同岗位名） 将部门名称设置为岗位类别名
		for (JiaYangPositionModel pos : dataModelList) {
			pos.setpNameClass(getPositionNameClassFromOrgs(pos.getCompBelongsTo()));
		}

		List<PositionModel> newList = copyCreateEntityList(dataModelList, PositionModel.class);
		return newList;
	}

	@Override
	protected List<UserInfoModel> getUserInfoModelList(String mode) throws java.lang.Exception {
		List<JiaYangUserInfoModel> dataModelList = getDataModelList(mode, JiaYangUserInfoModel.class);
		List<UserInfoModel> newList = copyCreateEntityList(dataModelList, UserInfoModel.class);

		// 设置公司组织名和岗位名
		for (UserInfoModel user : newList) {
			String orgName = user.getOrgOuName();
			String posName = user.getPostionName();
			// 增加contains条件解决重复设置bug
			if (StringUtils.isNotBlank(posName) && !posName.contains(POSITION_CLASS_SEPARATOR)) {
				if (StringUtils.isNotBlank(orgName)) {
					// 设置岗位名为带部门名分类岗位名
					user.setPostionName(orgName + POSITION_CLASS_SEPARATOR + posName);
				} else {
					user.setPostionName(POSITION_CLASS_DEFAULT + POSITION_CLASS_SEPARATOR + posName);
				}
			}
		}
		// 将岗位编号用数据库已有岗位的编号替换
		setDBPositionNoToUser(newList);

		return newList;
	}

	private <T> List<T> getDataModelList(String mode, Class<T> classType) throws Exception {
		String requestUrl = REQUEST_SERVER_ADDRESS + REQUEST_URL_GET_DATA;

		String token = getToken();
		JSONObject jsonParams = new JSONObject();
		if (classType.isAssignableFrom(JiaYangUserInfoModel.class)) {
			jsonParams.put("funcId", 1);
		} else if (classType.isAssignableFrom(JiaYangOuInfoModel.class)) {
			jsonParams.put("funcId", 2);
		} else if (classType.isAssignableFrom(JiaYangPositionModel.class)) {
			jsonParams.put("funcId", 1);
		}
		jsonParams.put("paras", "{\"U_EID\":50450}");
		jsonParams.put("dataFormat", "json");
		jsonParams.put("dataPart", "D");
		jsonParams.put("accessToken", token);

		String response = HttpClientUtil4Sync.doPost(requestUrl, jsonParams.toString());
		// logger.info(response);
		JsonNode jsonNode = mapper.readTree(response);

		String rowList = "";
		List<T> list = null;
		if (classType.isAssignableFrom(JiaYangUserInfoModel.class)) {
			rowList = jsonNode.get("Result").get("TTRAIN").get("Row").toString();
			list = mapper.readValue(rowList, new TypeReference<List<JiaYangUserInfoModel>>() {
			});
		} else if (classType.isAssignableFrom(JiaYangOuInfoModel.class)) {
			rowList = jsonNode.get("Result").get("dep").get("Row").toString();
			list = mapper.readValue(rowList, new TypeReference<List<JiaYangOuInfoModel>>() {
			});
		} else if (classType.isAssignableFrom(JiaYangPositionModel.class))

		{
			rowList = jsonNode.get("Result").get("TTRAIN").get("Row").toString();
			list = mapper.readValue(rowList, new TypeReference<List<JiaYangPositionModel>>() {
			});
		}

		// 清除token
		clearToken(token);

		return list;
	}

	/**
	 * 获取AccessToken
	 * 
	 * @return
	 * @throws IOException
	 */
	private String getToken() throws IOException {
		String requestUrl = REQUEST_SERVER_ADDRESS + REQUEST_URL_GET_TOKEN;

		JSONObject jsonParams = new JSONObject();
		jsonParams.put("acc", "admin");
		jsonParams.put("pwd", "TOD+ZuS30tc=");

		String response = HttpClientUtil4Sync.doPost(requestUrl, jsonParams.toString());
		JsonNode jsonNode = mapper.readTree(response);
		// Result值为token
		return jsonNode.get("Result").asText();
	}

	/**
	 * 清除AccessToken
	 * 
	 * @param token
	 * @throws IOException
	 */
	private void clearToken(String token) throws IOException {
		String requestUrl = REQUEST_SERVER_ADDRESS + REQUEST_URL_CLEAR_TOKEN;

		JSONObject jsonParams = new JSONObject();
		jsonParams.put("accessToken", token);

		HttpClientUtil4Sync.doPost(requestUrl, jsonParams.toString());
	}

	public static void main(String[] args) throws IOException, Exception {
		JiaYangSyncService service = new JiaYangSyncService();
		List<PositionModel> dataModelList = service.getPositionModelList(null);
		System.out.println(dataModelList.size());
		PrintUtil.logPrintPoss(dataModelList);
	}
}
