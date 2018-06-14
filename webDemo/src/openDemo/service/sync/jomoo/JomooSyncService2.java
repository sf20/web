package openDemo.service.sync.jomoo;

import java.rmi.RemoteException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.rpc.ServiceException;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import openDemo.entity.OuInfoModel;
import openDemo.entity.PositionModel;
import openDemo.entity.UserInfoModel;
import openDemo.entity.sync.jomoo.JomooUserInfoModel;
import openDemo.service.sync.AbstractSyncService;
import openDemo.service.sync.jomoows.LBEBusinessService;
import openDemo.service.sync.jomoows.LBEBusinessWebServiceLocator;
import openDemo.service.sync.jomoows.LbRecord;
import openDemo.service.sync.jomoows.LoginResult;
import openDemo.service.sync.jomoows.QueryOption;
import openDemo.service.sync.jomoows.QueryResult;

@Service
public class JomooSyncService2 extends AbstractSyncService implements JomooConfig2 {
	public static Logger LOGGER = LogManager.getLogger(JomooSyncService2.class);
	private List<UserInfoModel> sharedDataModelList;

	public JomooSyncService2() {
		super.setApikey(apikey);
		super.setSecretkey(secretkey);
		super.setBaseUrl(baseUrl);
		super.setIsPosIdProvided(false);
		super.setSyncServiceName(this.getClass().getSimpleName());
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
		String deleteStatus = user.getDeleteStatus();
		if ("未开通".equals(deleteStatus)) {
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

			// userName <= ID 员工工号作为用户登录名
			tempModel.setUserName(tempModel.getID());
		}
	}

	@Override
	protected List<OuInfoModel> getOuInfoModelList(String mode) throws Exception {
		List<OuInfoModel> newList = new ArrayList<OuInfoModel>();
		// 获取公司内部所有员工
		LbRecord[] userRecords1 = queryGetResult("BC_APP_EDIC_UserInfo_V");
		List<JomooUserInfoModel> dataModelList1 = mapRecordToUser(userRecords1);

		// 查找部门“客服中心”(id为D0066)及其子部门
		LbRecord[] deptRecords = queryGetResult("BC_APP_EDIC_OAOrg_V");
		List<OuInfoModel> deptList = mapRecordToDept(deptRecords);
		String parentId = "D0066";
		List<OuInfoModel> depts = findAllChildrenDepts(parentId, deptList);
		OuInfoModel parentDept = new OuInfoModel();
		parentDept.setID(parentId);
		parentDept.setOuName("客服中心");
		newList.add(parentDept);
		for (OuInfoModel dept : depts) {
			dept.setParentID(null);
			newList.add(dept);
		}

		// 内部员工所属部门为“客服中心”(id为D0066)及其子部门的的属于客服人员
		List<JomooUserInfoModel> tempList = new ArrayList<JomooUserInfoModel>();
		for (JomooUserInfoModel user : dataModelList1) {
			for (OuInfoModel dept : newList) {
				if (dept.getID().equals(user.getOrgOuCode())) {
					tempList.add(user);
					break;
				}
			}
		}

		// 获取服务商员工信息
		LbRecord[] userRecords2 = queryGetResult("BC_APP_ServicerInfo_V");
LOGGER.info("调用客户接口结束");
		List<JomooUserInfoModel> dataModelList2 = mapRecordToServicerUser(userRecords2);

		// 共用集合数据
		// 客服部对接是公司的客服员工+服务商员工
		List<JomooUserInfoModel> sharedUserList = new ArrayList<JomooUserInfoModel>();
		sharedUserList.addAll(tempList);
		sharedUserList.addAll(dataModelList2);
		sharedDataModelList = copyCreateEntityList(sharedUserList, UserInfoModel.class);

		// 从商学院人员数据中提取部门信息
		for (JomooUserInfoModel user : dataModelList2) {
			String deptName = user.getDeptName();
			if (StringUtils.isNotBlank(deptName)) {
				OuInfoModel ouInfo = new OuInfoModel();
				ouInfo.setID(user.getOrgOuCode());
				ouInfo.setOuName(deptName);
				if (!newList.contains(ouInfo)) {
					newList.add(ouInfo);
				}
			}
		}

		return newList;
	}

	@Override
	protected List<PositionModel> getPositionModelList(String mode) throws Exception {

		return getPosListFromUsers(sharedDataModelList);
	}

	@Override
	protected List<UserInfoModel> getUserInfoModelList(String mode) throws Exception {

		return sharedDataModelList;
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

	private List<JomooUserInfoModel> mapRecordToUser(LbRecord[] userRecords) {
		List<JomooUserInfoModel> userInfoList = new ArrayList<JomooUserInfoModel>();
		for (LbRecord temp : userRecords) {
			Object[] values = temp.getValues();

			JomooUserInfoModel userInfo = new JomooUserInfoModel();
			userInfo.setID(String.valueOf(values[1]));
			userInfo.setUserName(String.valueOf(values[1]));
			userInfo.setCnName(String.valueOf(values[24]));
			userInfo.setSex(String.valueOf(values[29]));
			userInfo.setMobile(String.valueOf(values[28]));
			userInfo.setMail(String.valueOf(values[3]));
			userInfo.setOrgOuCode(String.valueOf(values[25]));
			userInfo.setDeptName(String.valueOf(values[26]));
			userInfo.setPostionName(String.valueOf(values[4]));
			userInfo.setEntryTime(String.valueOf(values[7]));
			// 是否冻结无效
			// userInfo.setStatus(String.valueOf(values[2]));
			userInfo.setSpare1(String.valueOf(values[11]));
			userInfoList.add(userInfo);
		}
		return userInfoList;
	}

	private List<JomooUserInfoModel> mapRecordToServicerUser(LbRecord[] userRecords) {
		List<JomooUserInfoModel> userInfoList = new ArrayList<JomooUserInfoModel>();
		String namePrefix = "管理区域经理：";
		for (LbRecord temp : userRecords) {
			Object[] values = temp.getValues();

			JomooUserInfoModel userInfo = new JomooUserInfoModel();
			userInfo.setID(String.valueOf(values[1]));
			userInfo.setUserName(String.valueOf(values[1]));
			userInfo.setCnName(String.valueOf(values[2]));
			userInfo.setSex(String.valueOf(values[10]));
			userInfo.setMobile(String.valueOf(values[12]));
			// 管理区域经理
			String realManager = String.valueOf(values[9]);
			if (StringUtils.isNotBlank(realManager)) {
				// 管理区域经理名字进行md5编码作为部门id
				userInfo.setOrgOuCode(getMd5(realManager));
				// 部门名显示 管理区域经理：xxx
				userInfo.setDeptName(namePrefix + realManager);
			}
			userInfo.setPostionName(String.valueOf(values[11]));
			userInfo.setDeleteStatus(String.valueOf(values[26]));
			userInfoList.add(userInfo);
		}
		return userInfoList;
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

	public String getMd5(String plainText) {
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
