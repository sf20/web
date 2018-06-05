package openDemo.service.sync.aiqinhai;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import openDemo.entity.OuInfoModel;
import openDemo.entity.PositionModel;
import openDemo.entity.UserInfoModel;
import openDemo.entity.sync.landray.LandrayOuInfoModel;
import openDemo.entity.sync.landray.LandrayPositionModel;
import openDemo.entity.sync.landray.LandrayUserInfoModel;
import openDemo.service.common.landray.oa.ISysSynchroGetOrgWebService;
import openDemo.service.common.landray.oa.ISysSynchroGetOrgWebServiceServiceLocator;
import openDemo.service.common.landray.oa.SysSynchroGetOrgInfoContext;
import openDemo.service.common.landray.oa.SysSynchroOrgResult;
import openDemo.service.sync.AbstractSyncService;

@Service
public class AiqinhaiSyncService extends AbstractSyncService implements AiqinhaiConfig {
	// 全量增量区分
	private static final String MODE_FULL = "1";
	private static final String MODE_UPDATE = "2";
	// 客户接口
	private static final String ENDPOINT_ADDRESS = "http://eptest.hongsin.cn:8080/sys/webservice/sysSynchroGetOrgWebService";
	// 日期格式化用
	private static final SimpleDateFormat CUSTOM_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
	// json解析用
	private ObjectMapper mapper;

	public AiqinhaiSyncService() {
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
		// json字符串的日期格式
		mapper.setDateFormat(YYMMDD_DATE_FORMAT);
	}

	@Override
	protected boolean isOrgExpired(OuInfoModel org) {
		String status = org.getStatus();
		// isAvailable为true有效为false无效
		if ("false".equals(status)) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	protected boolean isPosExpired(PositionModel pos) {
		String status = pos.getStatus();
		// isAvailable为true有效为false无效
		if ("false".equals(status)) {
			return true;
		} else {
			return false;
		}
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
		for (OuInfoModel org : newList) {
			// 客户数据中根组织的上级部门id为0 有多个根组织
			if (org.getParentID() != null) {
				org.setParentID(null);
			}
		}
	}

	@Override
	protected void changePropValues(List<UserInfoModel> newList) {
		for (UserInfoModel tempModel : newList) {
			// 性别字符串转换 M：男 F：女
			String sex = tempModel.getSex();
			if ("M".equals(sex)) {
				tempModel.setSex("男");
			} else if ("F".equals(sex)) {
				tempModel.setSex("女");
			}
		}
	}

	@Override
	protected List<OuInfoModel> getOuInfoModelList(String mode) throws java.lang.Exception {
		List<LandrayOuInfoModel> dataModelList = getDataModelList(mode, LandrayOuInfoModel.class);
		List<OuInfoModel> newList = copyCreateEntityList(dataModelList, OuInfoModel.class);

		return newList;
	}

	@Override
	protected List<PositionModel> getPositionModelList(String mode) throws java.lang.Exception {
		List<LandrayPositionModel> dataModelList = getDataModelList(mode, LandrayPositionModel.class);
		// 岗位数据存在同岗位名不同岗位id（不同部门存在相同岗位名） 将部门名称设置为岗位类别名
		for (LandrayPositionModel pos : dataModelList) {
			pos.setpNameClass(getPositionNameClassFromOrgs(pos.getOrgBelongsTo()));
		}

		List<PositionModel> newList = copyCreateEntityList(dataModelList, PositionModel.class);

		return newList;
	}

	@Override
	protected List<UserInfoModel> getUserInfoModelList(String mode) throws java.lang.Exception {
		List<LandrayUserInfoModel> dataModelList = getDataModelList(mode, LandrayUserInfoModel.class);
		// 设置岗位编号
		for (LandrayUserInfoModel user : dataModelList) {
			String[] postionNoList = user.getPostionNoList();
			if (postionNoList != null && postionNoList.length > 0) {
				// 人员数据中有多个岗位的以第一个岗位为主岗
				user.setPostionNo(postionNoList[0]);
			}
		}

		List<UserInfoModel> newList = copyCreateEntityList(dataModelList, UserInfoModel.class);

		return newList;
	}

	private <T> List<T> getDataModelList(String mode, Class<T> classType) throws java.lang.Exception {
		ISysSynchroGetOrgWebServiceServiceLocator locator = new ISysSynchroGetOrgWebServiceServiceLocator();
		locator.setISysSynchroGetOrgWebServicePortEndpointAddress(ENDPOINT_ADDRESS);
		ISysSynchroGetOrgWebService service = locator.getISysSynchroGetOrgWebServicePort();

		SysSynchroGetOrgInfoContext reqParam = new SysSynchroGetOrgInfoContext();
		JSONObject type = new JSONObject();
		if (classType.isAssignableFrom(LandrayOuInfoModel.class)) {
			type.put("type", "dept");
		} else if (classType.isAssignableFrom(LandrayPositionModel.class)) {
			type.put("type", "post");
		} else if (classType.isAssignableFrom(LandrayUserInfoModel.class)) {
			type.put("type", "person");
		}
		JSONArray paramJson = new JSONArray();
		paramJson.add(type);
		reqParam.setReturnOrgType(paramJson.toString());
		// 设置获取增量数据参数
		if (MODE_UPDATE.equals(mode)) {
			reqParam.setBeginTimeStamp(CUSTOM_DATE_FORMAT.format(getYesterdayDate(new Date())));
		}
		// 请求count暂时设置为10000，后期变动可修改
		reqParam.setCount(10000);

		SysSynchroOrgResult result = service.getUpdatedElements(reqParam);
		int returnState = result.getReturnState();
		String message = result.getMessage();
		// 状态值为1表示操作失败
		if (returnState == 1) {
			throw new IOException("获取客户接口[" + classType.getSimpleName() + "]数据错误：" + message);
		}

		List<T> list = null;
		if (classType.isAssignableFrom(LandrayOuInfoModel.class)) {
			list = mapper.readValue(message, new TypeReference<List<LandrayOuInfoModel>>() {
			});
		} else if (classType.isAssignableFrom(LandrayPositionModel.class)) {
			list = mapper.readValue(message, new TypeReference<List<LandrayPositionModel>>() {
			});
		} else if (classType.isAssignableFrom(LandrayUserInfoModel.class)) {
			list = mapper.readValue(message, new TypeReference<List<LandrayUserInfoModel>>() {
			});
		}

		return list;
	}
	
	public static void main(String[] args) throws Exception {
//		AiqinhaiSyncService service = new AiqinhaiSyncService();
//		List<OuInfoModel> ouInfoModelList = service.getOuInfoModelList(MODE_FULL);
//		System.out.println(ouInfoModelList.size());
//		List<PositionModel> positionModelList = service.getPositionModelList(MODE_FULL);
//		System.out.println(positionModelList.size());
//		List<UserInfoModel> userInfoModelList = service.getUserInfoModelList(MODE_FULL);
//		System.out.println(userInfoModelList.size());
		System.out.println(null == "");
	}
}
