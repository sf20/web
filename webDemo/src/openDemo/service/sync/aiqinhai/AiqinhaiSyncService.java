package openDemo.service.sync.aiqinhai;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import openDemo.entity.OuInfoModel;
import openDemo.entity.PositionModel;
import openDemo.entity.UserInfoModel;
import openDemo.entity.sync.aiqinhai.LandrayUserInfoModel;
import openDemo.entity.sync.landray.LandrayOuInfoModel;
import openDemo.entity.sync.landray.LandrayPositionModel;
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
	private static final String ENDPOINT_ADDRESS = "http://ep.hongsin.cn/sys/webservice/sysSynchroGetOrgWebService";
	// json解析用
	private ObjectMapper mapper;

	private List<OuInfoModel> sharedDataModelList;

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
		// 无需设置
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
			
			// 设置密码加密方式
			tempModel.setEncryptionType("MD5");
		}
	}

	@Override
	protected List<OuInfoModel> getOuInfoModelList(String mode) throws java.lang.Exception {
		List<LandrayOuInfoModel> dataModelList = getDataModelList(mode, LandrayOuInfoModel.class);
		List<OuInfoModel> ouInfoList = copyCreateEntityList(dataModelList, OuInfoModel.class);

		List<OuInfoModel> newList = new ArrayList<OuInfoModel>();
		// 商管系统
		String parentId1 = "1464ae51f7cc23a57e91aa4481f80311";
		List<OuInfoModel> childrenDepts1 = findAllChildrenDepts(parentId1, ouInfoList);
		OuInfoModel parentDept1 = new OuInfoModel();
		parentDept1.setID(parentId1);
		parentDept1.setOuName("商管系统");
		newList.add(parentDept1);
		newList.addAll(childrenDepts1);

		// 融超外管
		String parentId2 = "155152c96abaabd2ca71e8a42c3935a8";
		List<OuInfoModel> childrenDepts2 = findAllChildrenDepts(parentId2, ouInfoList);
		OuInfoModel parentDept2 = new OuInfoModel();
		parentDept2.setID(parentId2);
		parentDept2.setOuName("融超外管");
		newList.add(parentDept2);
		newList.addAll(childrenDepts2);

		sharedDataModelList = newList;

		return newList;
	}

	@Override
	protected List<PositionModel> getPositionModelList(String mode) throws java.lang.Exception {
		List<LandrayPositionModel> dataModelList = getDataModelList(mode, LandrayPositionModel.class);
		// 只同步客户指定二个组织下的岗位
		List<LandrayPositionModel> tempDataModelList = new ArrayList<LandrayPositionModel>();
		for (LandrayPositionModel pos : dataModelList) {
			if (isDeptIdInDepts(pos.getOrgBelongsTo(), sharedDataModelList)) {
				tempDataModelList.add(pos);
			}
		}
		List<PositionModel> newList = copyCreateEntityList(tempDataModelList, PositionModel.class);

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
		List<UserInfoModel> userInfoList = copyCreateEntityList(dataModelList, UserInfoModel.class);

		// 只同步客户指定二个组织下的员工
		List<UserInfoModel> newList = new ArrayList<UserInfoModel>();
		for (UserInfoModel user : userInfoList) {
			if (isDeptIdInDepts(user.getOrgOuCode(), sharedDataModelList)) {
				newList.add(user);
			}
		}

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
		// if (MODE_UPDATE.equals(mode)) {
		// reqParam.setBeginTimeStamp(CUSTOM_DATE_FORMAT.format(getYesterdayDate(new
		// Date())));
		// }
		// 请求count暂时设置为20000，后期变动可修改
		reqParam.setCount(20000);

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

	/**
	 * 递归查询部门下的子部门
	 * 
	 * @param parentId
	 * @param allList
	 * @return
	 */
	private List<OuInfoModel> findAllChildrenDepts(String parentId, List<OuInfoModel> allList) {
		List<OuInfoModel> resultList = new ArrayList<OuInfoModel>();
		List<OuInfoModel> parentList = new ArrayList<OuInfoModel>();
		List<OuInfoModel> childList = new ArrayList<OuInfoModel>();
		for (OuInfoModel ouInfo : allList) {
			if (StringUtils.isNotBlank(ouInfo.getOuName())) {
				if (parentId.equals(ouInfo.getParentID())) {
					parentList.add(ouInfo);
				} else {
					childList.add(ouInfo);
				}
			}
		}

		resultList.addAll(parentList);
		for (OuInfoModel ouInfo : parentList) {
			resultList.addAll(findAllChildrenDepts(ouInfo.getID(), childList));
		}

		return resultList;
	}

	/**
	 * 判断部门id是否在给到的部门集合里
	 * 
	 * @param deptId
	 * @param depts
	 * @return
	 */
	private boolean isDeptIdInDepts(String deptId, List<OuInfoModel> depts) {
		if (StringUtils.isBlank(deptId)) {
			return false;
		}
		boolean flag = false;
		for (OuInfoModel dept : depts) {
			if (deptId.equals(dept.getID())) {
				flag = true;
				break;
			}
		}
		return flag;
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
