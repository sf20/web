package openDemo.service.sync.jomoo;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.rpc.ServiceException;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import openDemo.entity.OuInfoModel;
import openDemo.entity.PositionModel;
import openDemo.entity.UserInfoModel;
import openDemo.service.sync.AbstractSyncService;
import openDemo.service.sync.jomoows.LBEBusinessService;
import openDemo.service.sync.jomoows.LBEBusinessWebServiceLocator;
import openDemo.service.sync.jomoows.LbRecord;
import openDemo.service.sync.jomoows.LoginResult;
import openDemo.service.sync.jomoows.QueryOption;
import openDemo.service.sync.jomoows.QueryResult;

@Service
public class JomooSyncService1 extends AbstractSyncService implements JomooConfig1 {
	public static Logger LOGGER = LogManager.getLogger(JomooSyncService1.class);
	private List<UserInfoModel> sharedDataModelList;
	private String syncServiceName;

	public JomooSyncService1() {
		super.setApikey(apikey);
		super.setSecretkey(secretkey);
		super.setBaseUrl(baseUrl);
		super.setIsPosIdProvided(false);
		syncServiceName = this.getClass().getSimpleName();
		super.setSyncServiceName(syncServiceName);
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
		return false;
	}

	@Override
	protected void setRootOrgParentId(List<OuInfoModel> newList) {
		// 无需设置
	}

	@Override
	protected void changePropValues(List<UserInfoModel> newList) {
		for (UserInfoModel tempModel : newList) {

			// userName <= ID 员工工号作为用户登录名
			tempModel.setUserName(tempModel.getID());
		}
	}

	@Override
	protected List<OuInfoModel> getOuInfoModelList(String mode) throws Exception {
		LbRecord[] deptRecords = queryGetResult("BC_APP_EDIC_OAOrg_V");
		List<OuInfoModel> deptDataModelList = mapRecordToDept(deptRecords);

		// 获取同步部门数据
		List<OuInfoModel> depts = getFourCompanyDepts(deptDataModelList);

		LbRecord[] userRecords = queryGetResult("BC_APP_EDIC_UserInfo_V");
		LOGGER.info("调用客户接口结束");
		List<UserInfoModel> userDataModelList = mapRecordToUser(userRecords);
		List<UserInfoModel> userList = new ArrayList<UserInfoModel>();
		// 只同步客户指定四个组织下的且职等大于一职等的员工
		for (UserInfoModel user : userDataModelList) {
			String level = user.getSpare1();
			if (StringUtils.isNotBlank(level) && level.length() >= 3) {
				int intLevel = Integer.parseInt(level.substring(2, 3));
				// 职等大于一职等
				if (intLevel > 1 && isUserInDepts(user, depts)) {
					userList.add(user);
				}
			}
		}
		sharedDataModelList = userList;

		return depts;
	}

	@Override
	protected List<PositionModel> getPositionModelList(String mode) throws Exception {
		// LbRecord[] posRecords = queryGetResult("BC_APP_EDIC_OADeptPost_V");
		// List<PositionModel> dataModelList = mapRecordToPos(posRecords);
		//
		// // 岗位数据存在同岗位名不同岗位id将部门名称设置为岗位类别名
		// for (PositionModel pos : dataModelList) {
		// pos.setpNameClass(getPositionNameClassFromOrgs(pos.getpNameClass()));
		// }

		return getPosListFromUsers(sharedDataModelList);
	}

	@Override
	protected List<UserInfoModel> getUserInfoModelList(String mode) throws Exception {

		return sharedDataModelList;
	}

	@Override
	protected Map<String, List<UserInfoModel>> compareUserList(List<UserInfoModel> fullList,
			List<UserInfoModel> newList) {
		Map<String, List<UserInfoModel>> map = new HashMap<String, List<UserInfoModel>>();

		List<UserInfoModel> usersToSyncAdd = new ArrayList<UserInfoModel>();
		List<UserInfoModel> usersToSyncUpdate = new ArrayList<UserInfoModel>();
		List<UserInfoModel> usersToSyncDisable = new ArrayList<UserInfoModel>();

		for (UserInfoModel newUser : newList) {
			UserInfoModel userInFullList = null;
			for (UserInfoModel fullUser : fullList) {
				if (fullUser.equals(newUser)) {
					userInFullList = fullUser;
					break;
				}
			}
			// 待新增用户
			if (userInFullList == null) {
				usersToSyncAdd.add(newUser);
			}
			// 存在用户更新
			else {
				usersToSyncUpdate.add(newUser);
			}
		}

		// 被删除的用户
		for (UserInfoModel oldUser : fullList) {
			// 手工创建账号则不删除,第三方ID(THIRDSYSTEMUSERNO)为空则是手动创建
			if (StringUtils.isNotBlank(oldUser.getID())) {
				if (!newList.contains(oldUser)) {
					usersToSyncDisable.add(oldUser);
				}
			}
		}

		map.put(MAPKEY_USER_SYNC_ADD, usersToSyncAdd);
		map.put(MAPKEY_USER_SYNC_UPDATE, usersToSyncUpdate);
		map.put(MAPKEY_USER_SYNC_DISABLE, usersToSyncDisable);

		LOGGER.info("用户同步[" + syncServiceName + "]新增Size: " + usersToSyncAdd.size());
		LOGGER.info("用户同步[" + syncServiceName + "]更新Size: " + usersToSyncUpdate.size());
		LOGGER.info("用户同步[" + syncServiceName + "]禁用Size: " + usersToSyncDisable.size());

		return map;
	}

	private List<UserInfoModel> mapRecordToUser(LbRecord[] userRecords) {
		List<UserInfoModel> userInfoList = new ArrayList<UserInfoModel>();
		for (LbRecord temp : userRecords) {
			Object[] values = temp.getValues();

			UserInfoModel userInfo = new UserInfoModel();
			userInfo.setID(String.valueOf(values[1]));
			userInfo.setUserName(String.valueOf(values[1]));
			userInfo.setCnName(String.valueOf(values[24]));
			userInfo.setSex(String.valueOf(values[29]));
			userInfo.setMobile(String.valueOf(values[28]));
			userInfo.setMail(String.valueOf(values[3]));
			userInfo.setOrgOuCode(String.valueOf(values[25]));
			userInfo.setPostionName(String.valueOf(values[4]));
			userInfo.setEntryTime(String.valueOf(values[7]));
			// 是否冻结无效
			// userInfo.setStatus(String.valueOf(values[2]));
			// 职等薪级
			userInfo.setSpare1(String.valueOf(values[11]));
			userInfoList.add(userInfo);
		}
		return userInfoList;
	}

	private List<OuInfoModel> mapRecordToDept(LbRecord[] userRecords) {
		List<OuInfoModel> ouInfoList = new ArrayList<OuInfoModel>();
		for (LbRecord temp : userRecords) {
			Object[] values = temp.getValues();

			OuInfoModel ouInfo = new OuInfoModel();
			ouInfo.setID(String.valueOf(values[1]));
			ouInfo.setOuName(String.valueOf(values[2]));
			ouInfo.setParentID(String.valueOf(values[3]));
			// ouInfo.setManagerId(String.valueOf(values[4]));
			ouInfoList.add(ouInfo);
		}
		return ouInfoList;
	}

	private List<PositionModel> mapRecordToPos(LbRecord[] userRecords) {
		List<PositionModel> positionList = new ArrayList<PositionModel>();
		for (LbRecord temp : userRecords) {
			Object[] values = temp.getValues();

			PositionModel pos = new PositionModel();
			pos.setpNo(String.valueOf(values[2]));
			pos.setpNames(String.valueOf(values[3]));
			pos.setpNameClass(String.valueOf(values[1]));
			positionList.add(pos);
		}
		return positionList;
	}

	/**
	 * 调用客户接口
	 * 
	 * @param objectName
	 * @return
	 * @throws ServiceException
	 * @throws RemoteException
	 */
	private LbRecord[] queryGetResult(String objectName) throws ServiceException, RemoteException {
		LBEBusinessWebServiceLocator locator = new LBEBusinessWebServiceLocator();
		LBEBusinessService service = locator.getLBEBusinessServiceImplPort();

		LoginResult loginResult = service.login("FCUser", "FCUser", "myapp", "plain", "");
		String sessionId = loginResult.getSessionId();

		QueryOption queryOption = new QueryOption();
		queryOption.setBatchNo(1);
		queryOption.setBatchSize(20000);

		QueryResult queryResult = service.query(sessionId, objectName, null, null, queryOption);
		if (queryResult.getResult() <= 0) {
			LOGGER.error("获取数据失败:" + queryResult.getMessage());
		}
		LbRecord[] records = queryResult.getRecords();

		service.logout(sessionId);

		return records;
	}

	/**
	 * 查询指定组织及子部门数据
	 * 
	 * @param deptDataModelList
	 * @return
	 */
	private List<OuInfoModel> getFourCompanyDepts(List<OuInfoModel> deptDataModelList) {
		List<OuInfoModel> fourCompanyDepts = new ArrayList<OuInfoModel>();
		// 九牧厨卫股份有限公司及其子部门
		String parentId1 = "D0002";
		List<OuInfoModel> depts1 = findAllChildrenDepts(parentId1, deptDataModelList);
		OuInfoModel parentDept1 = new OuInfoModel();
		parentDept1.setID(parentId1);
		parentDept1.setOuName("九牧厨卫股份有限公司");
		depts1.add(0, parentDept1);
		fourCompanyDepts.addAll(depts1);
		// 九牧集团有限公司及其子部门
		String parentId2 = "D0491";
		List<OuInfoModel> depts2 = findAllChildrenDepts(parentId2, deptDataModelList);
		OuInfoModel parentDept2 = new OuInfoModel();
		parentDept2.setID(parentId2);
		parentDept2.setOuName("九牧集团有限公司");
		depts2.add(0, parentDept2);
		fourCompanyDepts.addAll(depts2);
		// 石狮九牧物流有限公司及其子部门
		String parentId3 = "D3986";
		List<OuInfoModel> depts3 = findAllChildrenDepts(parentId3, deptDataModelList);
		OuInfoModel parentDept3 = new OuInfoModel();
		parentDept3.setID(parentId3);
		parentDept3.setOuName("石狮九牧物流有限公司");
		depts3.add(0, parentDept3);
		fourCompanyDepts.addAll(depts3);
		// 永春九牧厨卫有限公司及其子部门
		String parentId4 = "D3987";
		List<OuInfoModel> depts4 = findAllChildrenDepts(parentId4, deptDataModelList);
		OuInfoModel parentDept4 = new OuInfoModel();
		parentDept4.setID(parentId4);
		parentDept4.setOuName("永春九牧厨卫有限公司");
		depts4.add(0, parentDept4);
		fourCompanyDepts.addAll(depts4);

		return fourCompanyDepts;
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
	 * 判断人员是否在给到的部门集合里
	 * 
	 * @param user
	 * @param depts
	 * @return
	 */
	private boolean isUserInDepts(UserInfoModel user, List<OuInfoModel> depts) {
		String deptId = user.getOrgOuCode();
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
}
