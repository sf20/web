package openDemo.service.sync.shimao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
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
import openDemo.entity.sync.shimao.ShiMaoObjRelationModel;
import openDemo.entity.sync.shimao.ShiMaoResJsonModel;
import openDemo.entity.sync.shimao.ShiMaoUserInfoModel;
import openDemo.service.sync.AbstractSyncService;

@Service
public class ShiMaoSyncService extends AbstractSyncService implements ShiMaoConfig {
	// 请求参数
	public static String PARAM_SOURCESYS = "CaresoftDC";
	public static int PARAM_PAGESIZE = 10000;
	// WebServiceLocator
	private GetPersonInfoServiceLocator locator = new GetPersonInfoServiceLocator();
	// 用于复用的对象集合
	private List<PositionModel> sharedPositionModelList = new ArrayList<PositionModel>();
	private Map<String, String> sharedPosNoDeptIdMap = new HashMap<String, String>();
	private Map<String, String> sharedPosNoAndNameMap = new HashMap<String, String>();
	private Map<String, String> sharedUserIdPosNoMap = new HashMap<String, String>();
	// json解析用
	private ObjectMapper mapper;

	public static Logger logger = LogManager.getLogger(ShiMaoSyncService.class);

	public ShiMaoSyncService() {
		super.setApikey(apikey);
		super.setSecretkey(secretkey);
		super.setBaseUrl(baseUrl);
		super.setIsPosIdProvided(false);
		super.setSyncServiceName(this.getClass().getSimpleName());

		// 创建用于json反序列化的对象
		mapper = new ObjectMapper();
		// 忽略json中多余的属性字段
		mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
	}

	@Override
	protected boolean isPosExpired(PositionModel pos) {
		return false;
	}

	@Override
	protected boolean isOrgExpired(OuInfoModel org) {
		return false;
	}

	@Override
	protected boolean isUserExpired(UserInfoModel user) {
		// 在职状态 ：3表示在职、0表示离职
		String status = user.getStatus();
		if ("0".equals(status)) {
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
	}

	@Override
	protected List<OuInfoModel> getOuInfoModelList(String mode) throws Exception {
		// 获取人员组织关系数据
		List<ShiMaoObjRelationModel> objRelationList = (List<ShiMaoObjRelationModel>) getDataModelList(mode,
				ShiMaoObjRelationModel.class);

		// 获取部门数据
		List<OuInfoModel> ouInfoModelAllList = new ArrayList<OuInfoModel>();
		for (ShiMaoObjRelationModel tempModel : objRelationList) {
			// A002直接上级 O组织 对象1为S组织 对象2为O组织
			if ("A002".equals(tempModel.getRelationType()) && "O".equals(tempModel.getObjType1())) {
				OuInfoModel ouInfoModel = new OuInfoModel();
				ouInfoModel.setID(tempModel.getObjID1());
				ouInfoModel.setOuName(tempModel.getObjName1());
				ouInfoModel.setParentID(tempModel.getObjID2());
				ouInfoModelAllList.add(ouInfoModel);
			}
		}

		// 只对接福建和苏沪区域
		// 苏沪地区
		List<OuInfoModel> ouInfoModelList = new ArrayList<OuInfoModel>();
		String parentId1 = "50300001";
		List<OuInfoModel> depts1 = findAllChildrenDepts(parentId1, ouInfoModelAllList);
		OuInfoModel parentDept1 = new OuInfoModel();
		parentDept1.setID(parentId1);
		parentDept1.setOuName("苏沪地区公司");
		depts1.add(0, parentDept1);
		ouInfoModelList.addAll(depts1);
		// 福建地区
		String parentId2 = "55000952";
		List<OuInfoModel> depts2 = findAllChildrenDepts(parentId2, ouInfoModelAllList);
		OuInfoModel parentDept2 = new OuInfoModel();
		parentDept2.setID(parentId2);
		parentDept2.setOuName("福建地区公司");
		depts2.add(0, parentDept2);
		ouInfoModelList.addAll(depts2);

		// 福建和苏沪区域下的岗位
		List<String> pNoList = new ArrayList<String>();
		Set<String> pNamesList = new HashSet<String>();
		for (ShiMaoObjRelationModel tempModel : objRelationList) {
			// A003组织和岗位关系 对象1为S岗位 对象2为O组织
			String deptId = tempModel.getObjID2();
			if ("A003".equals(tempModel.getRelationType()) && isDeptIdInDepts(deptId, ouInfoModelList)) {
				String pNo = tempModel.getObjID1();
				String pNames = tempModel.getObjName1();
				pNoList.add(pNo);
				sharedPosNoDeptIdMap.put(pNo, deptId);
				sharedPosNoAndNameMap.put(pNo, pNames);
				// 保存不重复岗位名
				if (pNamesList.add(pNames)) {
					PositionModel pos = new PositionModel();
					pos.setpNo(pNo);
					pos.setpNames(pNames);
					sharedPositionModelList.add(pos);
				}
			}
		}

		// 福建和苏沪区域下的人员账号
		for (ShiMaoObjRelationModel tempModel : objRelationList) {
			// A008岗位和人员关系 对象1为S岗位 对象2为P人员
			String posNo = tempModel.getObjID1();
			if ("A008".equals(tempModel.getRelationType()) && pNoList.contains(posNo)) {
				sharedUserIdPosNoMap.put(tempModel.getObjID2(), posNo);
			}
		}

		return ouInfoModelList;
	}

	@Override
	protected List<PositionModel> getPositionModelList(String mode) throws Exception {

		return sharedPositionModelList;
	}

	@Override
	protected List<UserInfoModel> getUserInfoModelList(String mode) throws Exception {
		List<ShiMaoUserInfoModel> shiMaoUserInfoModelList = (List<ShiMaoUserInfoModel>) getDataModelList(mode,
				ShiMaoUserInfoModel.class);
		List<UserInfoModel> userInfoModelList = copyCreateEntityList(shiMaoUserInfoModelList, UserInfoModel.class);

		// 只对接福建和苏沪区域员工账号
		List<UserInfoModel> newList = new ArrayList<UserInfoModel>();
		// 设置岗位和部门
		for (UserInfoModel user : userInfoModelList) {
			String userId = user.getID();
			// 账号过滤
			if (sharedUserIdPosNoMap.containsKey(userId)) {
				String posNo = sharedUserIdPosNoMap.get(userId);
				user.setPostionName(sharedPosNoAndNameMap.get(posNo));
				if (sharedPosNoDeptIdMap.containsKey(posNo)) {
					user.setOrgOuCode(sharedPosNoDeptIdMap.get(posNo));
				}
				newList.add(user);
			}
		}

		return newList;
	}

	private <T> List<T> getDataModelList(String mode, Class<T> listClassType) throws Exception {
		// 调用WebService对象
		GetPersonInfoServiceSoap service = locator.getGetPersonInfoServiceSoap();

		int pageIndex = 1;
		List<T> pageDataModelList = null;
		ShiMaoResJsonModel<T> resJsonModel = null;
		List<T> totalDataModelList = new ArrayList<T>();
		do {
			if (listClassType.isAssignableFrom(ShiMaoUserInfoModel.class)) {
				String response = service.getPersonAllInfo(pageIndex, PARAM_PAGESIZE, PARAM_SOURCESYS);
				JsonNode dataNode = mapper.readTree(response).get("Data");
				// System.out.println(dataNode.asText());
				resJsonModel = mapper.readValue(dataNode.asText(),
						new TypeReference<ShiMaoResJsonModel<ShiMaoUserInfoModel>>() {
						});
			} else if (listClassType.isAssignableFrom(ShiMaoObjRelationModel.class)) {
				String response = service.getObjRelationAllInfo(pageIndex, PARAM_PAGESIZE, PARAM_SOURCESYS);
				JsonNode dataNode = mapper.readTree(response).get("Data");
				// System.out.println(dataNode.asText());
				resJsonModel = mapper.readValue(dataNode.asText(),
						new TypeReference<ShiMaoResJsonModel<ShiMaoObjRelationModel>>() {
						});
			}
			pageDataModelList = resJsonModel.getDataList();
			// System.out.println(resJsonModel.getTotal().get(0).get("count").trim()
			// + "--" + pageDataModelList.size());

			totalDataModelList.addAll(pageDataModelList);
			pageIndex++;
		} while (pageDataModelList.size() > 0);

		return totalDataModelList;
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
			if (ouInfo.getParentID().equals(parentId)) {
				parentList.add(ouInfo);
			} else
				childList.add(ouInfo);
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
		ShiMaoSyncService service = new ShiMaoSyncService();
		service.getOuInfoModelList(null);
		// List<PositionModel> dataModelList =
		// service.getPositionModelList(null);
		List<UserInfoModel> dataModelList = service.getUserInfoModelList(null);
		System.out.println(dataModelList.size());
		PrintUtil.logPrintUsers(dataModelList);
	}
}
