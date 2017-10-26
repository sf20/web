package openDemo.test;

import java.util.ArrayList;
import java.util.List;

import com.seeyon.client.CTPRestClient;
import com.seeyon.client.CTPServiceClientManager;

import openDemo.entity.sync.seeyon.SeeyonOuInfoModel;
import openDemo.entity.sync.seeyon.SeeyonPositionModel;
import openDemo.entity.sync.seeyon.SeeyonUserInfoModel;

public class SeeyonSyncServiceTest {
	private static String REQUEST_ADDRESS = "http://oa.lonch.com.cn:8081";
	// 定义REST动态客户机
	private CTPRestClient client = null;

	public static void main(String[] args) throws Exception {
		SeeyonSyncServiceTest service = new SeeyonSyncServiceTest();
		List<SeeyonOuInfoModel> allOrgs = service.getAllOrgs();

		// List<SeeyonPositionModel> posData = service.getPosData(allOrgs);
		// System.out.println(posData.size());
		// List<SeeyonOuInfoModel> deptData = service.getOrgData(allOrgs);
		// System.out.println(deptData.size());
		List<SeeyonUserInfoModel> userData = service.getUserData(allOrgs);
		System.out.println(userData.size());

	}

	/**
	 * 获取全部单位数据
	 * 
	 * @return
	 */
	public List<SeeyonOuInfoModel> getAllOrgs() {
		List<SeeyonOuInfoModel> allOrgs = new ArrayList<SeeyonOuInfoModel>();
		if (authenticate()) {
			allOrgs = client.getList("orgAccounts", SeeyonOuInfoModel.class);

			// for (SeeyonOuInfoModel org : allOrgs) {
			// System.out.println(org.getOrgAccountId() + "=" + org.getId() +
			// "=" + org.getName() + "="
			// + org.getSuperior() + "=" + org.getEnabled() + "=" +
			// org.getIsDeleted());
			// }
		}

		return allOrgs;
	}

	/**
	 * 获取岗位数据
	 * 
	 * @param allOrgs
	 * @return
	 * @throws Exception
	 */
	public List<SeeyonPositionModel> getPosData(List<SeeyonOuInfoModel> allOrgs) throws Exception {
		List<SeeyonPositionModel> posList = new ArrayList<SeeyonPositionModel>();
		if (authenticate()) {
			System.out.println("========================");
			for (SeeyonOuInfoModel org : allOrgs) {
				String ouId = org.getOrgAccountId();

				// total 1141
				List<SeeyonPositionModel> poss = client.getList("orgPosts/all/" + ouId, SeeyonPositionModel.class);
				posList.addAll(poss);

				for (SeeyonPositionModel pos : poss) {
					System.out.println(
							pos.getId() + "=" + pos.getName() + "=" + pos.getEnabled() + "=" + pos.getIsDeleted());
				}
			}

		}

		return posList;
	}

	/**
	 * 获取部门数据
	 * 
	 * @param allOrgs
	 * @return
	 * @throws Exception
	 */
	public List<SeeyonOuInfoModel> getOrgData(List<SeeyonOuInfoModel> allOrgs) throws Exception {
		List<SeeyonOuInfoModel> orgList = new ArrayList<SeeyonOuInfoModel>();
		if (authenticate()) {
			System.out.println("========================");
			for (SeeyonOuInfoModel org : allOrgs) {
				// System.out.println(org.getName());

				String ouId = org.getOrgAccountId();

				// total 258
				List<SeeyonOuInfoModel> depts = client.getList("orgDepartments/all/" + ouId, SeeyonOuInfoModel.class);
				orgList.addAll(depts);

				for (SeeyonOuInfoModel dept : depts) {
					System.out.println(dept.getOrgAccountId() + "=" + dept.getId() + "=" + dept.getName() + "="
							+ dept.getSuperior() + "=" + dept.getEnabled() + "=" + dept.getIsDeleted());
				}
			}

		}

		return orgList;
	}

	/**
	 * 获取人员数据
	 * 
	 * @param allOrgs
	 * @return
	 * @throws Exception
	 */
	public List<SeeyonUserInfoModel> getUserData(List<SeeyonOuInfoModel> allOrgs) throws Exception {
		List<SeeyonUserInfoModel> userList = new ArrayList<SeeyonUserInfoModel>();
		if (authenticate()) {
			System.out.println("====================");
			for (SeeyonOuInfoModel org : allOrgs) {
				String ouId = org.getOrgAccountId();

				// int count = client.get("orgMembers/all/count/" + ouId,
				// Integer.class);
				// System.out.println(count);

				// total 2078
				List<SeeyonUserInfoModel> users = client.getList("orgMembers/all/" + ouId, SeeyonUserInfoModel.class);
				userList.addAll(users);

				for (SeeyonUserInfoModel user : users) {
					System.out.println(user.getId() + "=" + user.getName() + "=" + user.getLoginName() + "="
							+ user.getGender() + "=" + user.getTelNumber() + "=" + user.getEmailAddress() + "="
							+ user.getOrgDepartmentId() + "=" + user.getOrgPostId() + "=" + user.getCreateTime() + "="
							+ user.getBirthday() + "=" + user.getEnabled() + "=" + user.getState());
				}
			}
		}

		return userList;
	}

	private boolean authenticate() {
		// 取得指定服务主机的客户端管理器。
		// 参数为服务主机地址，包含{协议}{Ip}:{端口}，如http://127.0.0.1:8088
		CTPServiceClientManager clientManager = CTPServiceClientManager.getInstance(REQUEST_ADDRESS);
		// 取得REST动态客户机。
		client = clientManager.getRestClient();
		// 登录校验,成功返回true,失败返回false,此过程并会把验证通过获取的token保存在缓存中
		// 再请求访问其他资源时会自动把token放入请求header中。
		return client.authenticate("E-learning", "YUNxuetang123");
	}

	// public static void main(String[] args) throws Exception {
	// SeeyonSyncService service = new SeeyonSyncService();
	// List<UserInfoModel> userInfoModelList =
	// service.getUserInfoModelList(null);
	// for (UserInfoModel user : userInfoModelList) {
	// System.out.println(user.getID() + "=" + user.getCnName() + "=" +
	// user.getUserName() + "=" + user.getSex()
	// + "=" + user.getMobile() + "=" + user.getMail() + "=" +
	// user.getOrgOuCode() + "="
	// + user.getPostionNo() + "=" + user.getEntryTime() + "=" +
	// user.getBirthday() + "="
	// + user.getStatus() + "=" + user.getDeleteStatus());
	// }
	// System.out.println(userInfoModelList.size());
	// }
}
