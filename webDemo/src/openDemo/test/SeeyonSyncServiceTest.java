package openDemo.test;

import java.util.List;

import com.seeyon.client.CTPRestClient;
import com.seeyon.client.CTPServiceClientManager;

import openDemo.entity.sync.SeeyonPositionModel;

public class SeeyonSyncServiceTest extends AbstractSyncService {
	private static String REQUEST_ADDRESS = "http://oa.lonch.com.cn:8081";
	// 定义REST动态客户机
	private CTPRestClient client = null;

	public static void main(String[] args) throws Exception {
		SeeyonSyncServiceTest service = new SeeyonSyncServiceTest();
		service.testGetPosData();
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

	@Override
	public void testSync() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void testGetPosData() throws Exception {
		if (authenticate()) {
			String ouId = "1670980387146058224";
			// System.out.println(client.get("orgPosts/all/" + ouId, String.class));
			// System.out.println(client.get("orgLevels/all/" + ouId, String.class));
			List<SeeyonPositionModel> depts = client.getList("orgDepartments/all/" + ouId, SeeyonPositionModel.class);

			for (SeeyonPositionModel dept : depts) {
				System.out.println(dept.getId() + "=" + dept.getName() + "=" + dept.getSuperior() + "="
						+ dept.getIsDeleted() + "=" + dept.getEnabled());
			}
		} else {
			// TODO
		}
	}

	@Override
	public void testGetDeptData() throws Exception {
		if (authenticate()) {
			String ouId = "1670980387146058224";
			List<SeeyonPositionModel> depts = client.getList("orgDepartments/all/" + ouId, SeeyonPositionModel.class);

			for (SeeyonPositionModel dept : depts) {
				System.out.println(dept.getId() + "=" + dept.getName() + "=" + dept.getSuperior() + "="
						+ dept.getIsDeleted() + "=" + dept.getEnabled());
			}
		} else {
			// TODO
		}
	}

	@Override
	public void testGetUserData() throws Exception {
		if (authenticate()) {
			String ouId = "1670980387146058224";
			String deptId = "-5414753157123981018";
			// System.out.println(client.get("orgMembers/all/count/" + ouId, String.class));
			List<SeeyonPositionModel> depts = client.getList("orgDepartments/all/" + ouId, SeeyonPositionModel.class);

			for (SeeyonPositionModel dept : depts) {
				System.out.println(dept.getId() + "=" + dept.getName() + "=" + dept.getSuperior() + "="
						+ dept.getIsDeleted() + "=" + dept.getEnabled());
			}
		} else {
			// TODO
		}
	}

}
