package openDemo.service.sync.meilitianyuan;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import openDemo.common.PrintUtil;
import openDemo.entity.OuInfoModel;
import openDemo.entity.PositionModel;
import openDemo.entity.UserInfoModel;
import openDemo.entity.sync.meilitianyuan.MeiliTianyuanOuInfoModel;
import openDemo.entity.sync.meilitianyuan.MeiliTianyuanResJsonModel;
import openDemo.entity.sync.meilitianyuan.MeiliTianyuanUserInfoModel;
import openDemo.service.sync.AbstractSyncService;

@Service
public class MeiliTianyuanSyncService extends AbstractSyncService implements MeiliTianyuanConfig {
	// 全量增量区分
	private static final String MODE_FULL = "1";
	private static final String MODE_UPDATE = "2";
	private static final int PAGESIZE = 2000;

	// 记录日志
	private List<UserInfoModel> sharedDataModelList;
	private ObjectMapper mapper;

	public MeiliTianyuanSyncService() {
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
		// 是否有效
		String status = org.getStatus();
		// 1 有效 ，0删除
		if ("0".equals(status)) {
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
		// 员工状态
		String deleteStatus = user.getDeleteStatus();
		// 1：离职，２：在职，３：脱岗
		if (!"2".equals(deleteStatus)) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	protected void setRootOrgParentId(List<OuInfoModel> newList) {
		for (OuInfoModel org : newList) {
			// 客户数据中根组织的上级部门id为JT
			if ("JT".equals(org.getParentID())) {
				org.setParentID(null);
				break;
			}
		}
	}

	@Override
	protected void changePropValues(List<UserInfoModel> newList) {
		for (UserInfoModel tempModel : newList) {
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
		List<MeiliTianyuanOuInfoModel> dataModelList = getDataModelList(mode, MeiliTianyuanOuInfoModel.class);
		List<OuInfoModel> newList = copyCreateEntityList(dataModelList, OuInfoModel.class);

		// 加盟店部门组织不同步 最上级组织“加盟”的id为BFJM
		String parentId = "BFJM";
		String parentOuName = "加盟";
		OuInfoModel parentDept = new OuInfoModel();
		parentDept.setID(parentId);
		parentDept.setOuName(parentOuName);
		List<OuInfoModel> deptsToRemove = findAllChildrenDepts(parentId, newList);
		deptsToRemove.add(parentDept);

		Iterator<OuInfoModel> iterator = newList.iterator();
		while (iterator.hasNext()) {
			OuInfoModel ouInfoModel = iterator.next();
			for (OuInfoModel temp : deptsToRemove) {
				if (temp.getID().equals(ouInfoModel.getID())) {
					iterator.remove();
					break;
				}
			}
		}

		return newList;
	}

	@Override
	protected List<PositionModel> getPositionModelList(String mode) throws Exception {
		List<MeiliTianyuanUserInfoModel> dataModelList = getDataModelList(mode, MeiliTianyuanUserInfoModel.class);
		Iterator<MeiliTianyuanUserInfoModel> iterator = dataModelList.iterator();
		while (iterator.hasNext()) {
			MeiliTianyuanUserInfoModel model = iterator.next();
			// 员工性质：1：美田员工，2：加盟商，3：贝黎诗
			String status = model.getStatus();
			String postionName = model.getPostionName();
			// 加盟店人员和保洁人员不同步
			if ("2".equals(status) || (StringUtils.isNotBlank(postionName) && postionName.contains("保洁"))) {
				iterator.remove();
			}
		}
		List<UserInfoModel> newList = copyCreateEntityList(dataModelList, UserInfoModel.class);
		sharedDataModelList = newList;

		return getPosListFromUsers(newList);
	}

	@Override
	protected List<UserInfoModel> getUserInfoModelList(String mode) throws Exception {

		return sharedDataModelList;
	}

	/**
	 * 获取客户接口数据
	 * 
	 * @param mode
	 * @param classType
	 * @return
	 * @throws Exception
	 */
	private <T> List<T> getDataModelList(String mode, Class<T> classType) throws Exception {
		WFWebServiceLocator locator = new WFWebServiceLocator();
		WFWebServiceSoap service = locator.getWFWebServiceSoap();
		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

		List<T> dataModelList = new ArrayList<T>();
		if (classType.isAssignableFrom(MeiliTianyuanOuInfoModel.class)) {
			dataModelList = mapper.readValue(service.orgList(), new TypeReference<List<MeiliTianyuanOuInfoModel>>() {
			});
		} else if (classType.isAssignableFrom(MeiliTianyuanUserInfoModel.class)) {
			String userList = service.userList(PAGESIZE, 0);
			MeiliTianyuanResJsonModel resJsonModel = mapper.readValue(userList, MeiliTianyuanResJsonModel.class);
			dataModelList.addAll((List<T>) resJsonModel.getUserList());

			int totalRow = resJsonModel.getTotalRow();
			int totalPage = totalRow / PAGESIZE + (totalRow % PAGESIZE > 0 ? 1 : 0);
			for (int page = 1; page < totalPage; page++) {
				userList = service.userList(PAGESIZE, page);
				resJsonModel = mapper.readValue(userList, MeiliTianyuanResJsonModel.class);
				dataModelList.addAll((List<T>) resJsonModel.getUserList());
			}
		}

		return dataModelList;
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

	public static void main(String[] args) throws Exception {
		// MeiliTianyuanSyncService service = new MeiliTianyuanSyncService();
		// service.getPositionModelList(null);
		// List<UserInfoModel> userInfoModelList =
		// service.getUserInfoModelList(null);
		// PrintUtil.logPrintUsers(userInfoModelList);
		// List<OuInfoModel> positionModelList =
		// service.getOuInfoModelList(null);
		// List<OuInfoModel> childrenDepts =
		// service.findAllChildrenDepts("BFJM", positionModelList);
		// PrintUtil.printOrgs(positionModelList);
	}
}
