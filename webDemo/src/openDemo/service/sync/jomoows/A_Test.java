package openDemo.service.sync.jomoows;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.rpc.ServiceException;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import openDemo.common.PrintUtil;
import openDemo.entity.OuInfoModel;
import openDemo.entity.PositionModel;
import openDemo.entity.UserInfoModel;
import openDemo.entity.sync.jomoo.JomooUserInfoModel;
import openDemo.service.sync.jomoo.JomooSyncService2;

public class A_Test {
	public static Logger LOGGER = LogManager.getLogger(A_Test.class);

	public static void main(String[] args) throws ServiceException, RemoteException {
		A_Test ins = new A_Test();
		// 岗位
//		 LbRecord[] posRecords =
//		 ins.queryGetResult("BC_APP_EDIC_OADeptPost_V");
//		 System.out.println(posRecords.length);
//		 List<PositionModel> posList = mapRecordToPos(posRecords);
//		 for (PositionModel pos : posList) {
//		 System.out.println(pos.getpNo() + "=" + pos.getpNames() + "=" +
//		 pos.getpNameClass() + "=" + pos.getStatus());
//		 }

		// 部门 根组织parentId: AllOrg
//		LbRecord[] deptRecords = ins.queryGetResult("BC_APP_EDIC_OAOrg_V");
//		System.out.println(deptRecords.length);
//		List<OuInfoModel> deptList = mapRecordToDept(deptRecords);
//		for (OuInfoModel dept : deptList) {
//			LOGGER.info(dept.getID() + "=" + dept.getOuName() + "=" + dept.getParentID());
//		}
//		String keyDeptId = "D3986";
//		List<OuInfoModel> listToTree = ins.listToTree(keyDeptId, deptList);
//		for (OuInfoModel dept : listToTree) {
//			System.out.println(dept.getID() + "=" + dept.getOuName() + "=" + dept.getParentID());
//		}

		// 人员
//		LbRecord[] userRecords = ins.queryGetResult("BC_APP_EDIC_UserInfo_V");
//		System.out.println(userRecords.length);
//		List<UserInfoModel> userList = mapRecordToUser(userRecords);
		// 只同步职等为xx的员工
//		Set<String> deptIdSet = new HashSet<String>();
//		for (UserInfoModel user : userList) {
//			String level = user.getSpare1();
//			if (StringUtils.isNotBlank(level) && level.length() >= 3) {
//				int intLevel = Integer.parseInt(level.substring(2, 3));
//				// 职等不为一职等的员工
//				if (intLevel > 1) {
//					deptIdSet.add(user.getOrgOuCode());
//				}
//			}
//		}
//		// 只同步“xx”部门
//		List<OuInfoModel> ouInfoList = new ArrayList<OuInfoModel>();
//		for (OuInfoModel dept : listToTree) {
//			String id = dept.getID();
//			for (String deptId : deptIdSet) {
//				if (deptId.equals(id)) {
//					ouInfoList.add(dept);
//					break;
//				}
//			}
//		}
//		// Collections.sort(deptIdList);
//		for (OuInfoModel ou : ouInfoList) {
//			System.out.println(ou.getID() + "=" + ou.getOuName());
//		}
//		for(UserInfoModel user : userList){
//			LOGGER.info(user.getID() + "=" + user.getCnName() + "=" + user.getSex() + "=" + user.getMobile() + "="
//					+ user.getMail() + "=" + user.getOrgOuCode() + "=" + user.getPostionName() + "=" + user.getEntryTime()
//					+ "=" + user.getStatus() + "=" + user.getSpare1());
//		}

		// 服务商
		 LbRecord[] servicerRecords =
		 ins.queryGetResult("BC_APP_ServicerInfo_V");
		 System.out.println(servicerRecords.length);
		 List<UserInfoModel> userList = mapRecordToServicer(servicerRecords);
		 PrintUtil.logPrintUsers(userList);
	}

	private static List<UserInfoModel> mapRecordToServicer(LbRecord[] userRecords) {
		List<UserInfoModel> userInfoList = new ArrayList<UserInfoModel>();
		for (LbRecord temp : userRecords) {
			Object[] values = temp.getValues();

			UserInfoModel userInfo = new UserInfoModel();
			userInfo.setID(String.valueOf(values[1]));
			userInfo.setUserName(String.valueOf(values[1]));
			userInfo.setCnName(String.valueOf(values[2]));
			userInfo.setSex(String.valueOf(values[10]));
			userInfo.setMobile(String.valueOf(values[12]));
			// 管理区域经理
			String realManager = String.valueOf(values[9]);
			if (StringUtils.isNotBlank(realManager)) {
				// 管理区域经理名字进行md5编码作为部门id
				// userInfo.setOrgOuCode(getMd5(realManager));
				// 部门名显示 管理区域经理：xxx
				// userInfo.setDeptName(namePrefix + realManager);
			}
			userInfo.setPostionName(String.valueOf(values[11]));
			// 网点状态
			userInfo.setStatus(String.valueOf(values[8]));
			// 开通状态
			userInfo.setDeleteStatus(String.valueOf(values[26]));
//			// 网点名称
//			userInfo.setSpare1(String.valueOf(values[4]));
//			// 省份
//			userInfo.setSpare2(String.valueOf(values[5]));
//			// 城市
//			userInfo.setSpare3(String.valueOf(values[6]));
//			// 技师上岗证
//			userInfo.setSpare4(String.valueOf(values[14]));
//			// 信息员上岗证
//			userInfo.setSpare5(String.valueOf(values[17]));
//			// 备件员上岗证
//			userInfo.setSpare6(String.valueOf(values[18]));
//			// 不良品上岗证
//			userInfo.setSpare7(String.valueOf(values[19]));
//			// 结算员上岗证
//			userInfo.setSpare8(String.valueOf(values[20]));
//			// 兼职备件
//			userInfo.setSpare9(String.valueOf(values[21]));
//			// 兼职维修
//			userInfo.setSpare10(String.valueOf(values[24]));
			userInfoList.add(userInfo);
		}
		return userInfoList;
	}

	private static List<UserInfoModel> mapRecordToUser(LbRecord[] userRecords) {
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

	private static List<OuInfoModel> mapRecordToDept(LbRecord[] userRecords) {
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

	private static List<PositionModel> mapRecordToPos(LbRecord[] userRecords) {
		List<PositionModel> positionList = new ArrayList<PositionModel>();
		for (LbRecord temp : userRecords) {
			Object[] values = temp.getValues();

			PositionModel pos = new PositionModel();
			pos.setpNo(String.valueOf(values[2]));
			pos.setpNames(String.valueOf(values[3]));
			pos.setpNameClass(String.valueOf(values[1]));
			pos.setStatus(String.valueOf(values[4]));
			positionList.add(pos);
		}
		return positionList;
	}

	private LbRecord[] queryGetResult(String objectName) throws ServiceException, RemoteException {
		LBEBusinessWebServiceLocator locator = new LBEBusinessWebServiceLocator();
		LBEBusinessService service = locator.getLBEBusinessServiceImplPort();

		LoginResult loginResult = service.login("FCUser", "FCUser", "myapp", "plain", "");
		String sessionId = loginResult.getSessionId();
		System.out.println("sessionId: " + sessionId);

		QueryOption queryOption = new QueryOption();
		queryOption.setBatchNo(1);
		queryOption.setBatchSize(20000);

		QueryResult queryResult = service.query(sessionId, objectName, null, null, queryOption);
		if (queryResult.getResult() <= 0) {
			System.out.println("获取数据失败:" + queryResult.getMessage());
		}
		System.out.println(queryResult.getCount());
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
	private List<OuInfoModel> listToTree(String parentId, List<OuInfoModel> allList) {
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
			resultList.addAll(listToTree(ouInfo.getID(), childList));
		}

		return resultList;
	}

}
