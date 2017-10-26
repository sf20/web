package openDemo.service.sync.seeyon;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import com.seeyon.client.CTPRestClient;
import com.seeyon.client.CTPServiceClientManager;

import openDemo.entity.OuInfoModel;
import openDemo.entity.PositionModel;
import openDemo.entity.UserInfoModel;
import openDemo.entity.sync.seeyon.SeeyonOuInfoModel;
import openDemo.entity.sync.seeyon.SeeyonPositionModel;
import openDemo.entity.sync.seeyon.SeeyonUserInfoModel;
import openDemo.service.sync.AbstractSyncService2;

@Service
public class SeeyonSyncService extends AbstractSyncService2 implements SeeyonConfig {
	// 请求数据接口地址
	private static String REQUEST_ADDRESS = "http://oa.lonch.com.cn:8081";
	// 记录日志
	private Logger logger = LogManager.getLogger(SeeyonSyncService.class);
	// 定义REST动态客户机
	private CTPRestClient client = null;

	public SeeyonSyncService() {
		super.setApikey(apikey);
		super.setSecretkey(secretkey);
		super.setBaseUrl(baseUrl);
		// 无全量增量区分
		// super.setModeFull(MODE_FULL);
		// super.setModeUpdate(MODE_UPDATE);
		super.setLogger(logger);
	}

	/**
	 * 获取所有单位信息
	 * 
	 * @return
	 */
	private List<SeeyonOuInfoModel> getAllOrgs() {
		List<SeeyonOuInfoModel> allOrgs = new ArrayList<SeeyonOuInfoModel>();
		if (authenticate()) {
			// 取所有单位信息
			allOrgs = client.getList("orgAccounts", SeeyonOuInfoModel.class);
		}

		return allOrgs;
	}

	/**
	 * 获取指定单位的所有岗位(包含停用)
	 * 
	 * @param allOrgs
	 * @return
	 */
	private List<SeeyonPositionModel> getPosData(List<SeeyonOuInfoModel> allOrgs) {
		List<SeeyonPositionModel> posList = new ArrayList<SeeyonPositionModel>();

		for (SeeyonOuInfoModel org : allOrgs) {
			String ouId = org.getOrgAccountId();
			String ouName = org.getName();

			// 获取指定单位的所有岗位(包含停用)
			List<SeeyonPositionModel> poss = client.getList("orgPosts/all/" + ouId, SeeyonPositionModel.class);

			// 设置岗位的分级类别
			for (SeeyonPositionModel pos : poss) {
				pos.setpNameClass(ouName);
			}

			posList.addAll(poss);
		}

		return posList;
	}

	/**
	 * 获取指定单位的所有部门(包含停用)
	 * 
	 * @param allOrgs
	 * @return
	 */
	private List<SeeyonOuInfoModel> getOrgData(List<SeeyonOuInfoModel> allOrgs) {
		List<SeeyonOuInfoModel> orgList = new ArrayList<SeeyonOuInfoModel>();
		// 所有部门数据也包含单位数据
		orgList.addAll(allOrgs);

		for (SeeyonOuInfoModel org : allOrgs) {
			String ouId = org.getOrgAccountId();

			// 获取指定单位的所有部门(包含停用)
			List<SeeyonOuInfoModel> depts = client.getList("orgDepartments/all/" + ouId, SeeyonOuInfoModel.class);
			orgList.addAll(depts);
		}

		return orgList;
	}

	/**
	 * 获取得指定单位的所有人员（包含停用人员）
	 * 
	 * @param allOrgs
	 * @return
	 */
	private List<SeeyonUserInfoModel> getUserData(List<SeeyonOuInfoModel> allOrgs) {
		List<SeeyonUserInfoModel> userList = new ArrayList<SeeyonUserInfoModel>();

		for (SeeyonOuInfoModel org : allOrgs) {
			String ouId = org.getOrgAccountId();

			// 取得指定单位的所有人员（包含停用人员）
			List<SeeyonUserInfoModel> users = client.getList("orgMembers/all/" + ouId, SeeyonUserInfoModel.class);
			userList.addAll(users);
		}

		return userList;
	}

	/**
	 * 登录认证
	 * 
	 * @return
	 */
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
	protected boolean isPosExpired(PositionModel pos) {
		// enabled 是否启用
		String status = pos.getStatus();
		// isDeleted 是否被删除
		String deleteStatus = pos.getDeleteStatus();
		// 未启用或已删除场合 岗位过期
		if ("false".equals(status) || "true".equals(deleteStatus)) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	protected boolean isOrgExpired(OuInfoModel org) {
		// enabled 是否启用
		String status = org.getStatus();
		// isDeleted 是否被删除
		String deleteStatus = org.getDeleteStatus();
		// 未启用或已删除场合 组织过期
		if ("false".equals(status) || "true".equals(deleteStatus)) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	protected boolean isUserExpired(UserInfoModel user) {
		// enabled 账户状态：true为启用，false为停用
		String status = user.getStatus();
		// state 人员状态：1为在职，2 为离职
		String deleteStatus = user.getDeleteStatus();
		// 停用或离职场合 人员过期
		if ("false".equals(status) || "2".equals(deleteStatus)) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	protected void setRootOrgParentId(List<OuInfoModel> newList) {
		for (OuInfoModel org : newList) {
			// 客户数据中根组织的上级部门id为"-1"
			if ("-1".equals(org.getParentID())) {
				org.setParentID(null);
				break;
			}
		}
	}

	@Override
	protected void changePropValues(List<UserInfoModel> newList) {
		for (UserInfoModel tempModel : newList) {
			// 入职日期修改
			String entryTime = tempModel.getEntryTime();
			if (StringUtils.isNotEmpty(entryTime)) {
				long timeMills = Long.valueOf(entryTime);
				if (timeMills > 0) {
					tempModel.setEntryTime(DATE_FORMAT.format(new Date(timeMills)));
				} else {
					// 为负值时不同步
					tempModel.setEntryTime(null);
				}
			}

			// 出生日期修改
			String birthday = tempModel.getBirthday();
			if (StringUtils.isNotEmpty(birthday)) {
				long timeMills = Long.valueOf(birthday);
				if (timeMills > 0) {
					tempModel.setBirthday(DATE_FORMAT.format(new Date(timeMills)));
				} else {
					// 为负值时不同步
					tempModel.setBirthday(null);
				}
			}

			// 性别字符串转换 1：男 2：女
			String sex = tempModel.getSex();
			if ("1".equals(sex)) {
				tempModel.setSex("男");
			} else if ("2".equals(sex)) {
				tempModel.setSex("女");
			}
		}
	}

	@Override
	protected List<PositionModel> getPositionModelList(String mode) throws Exception {
		List<SeeyonPositionModel> posData = getPosData(getAllOrgs());
		List<PositionModel> newList = copyCreateEntityList(posData, PositionModel.class);

		return newList;
	}

	@Override
	protected List<OuInfoModel> getOuInfoModelList(String mode) throws Exception {
		List<SeeyonOuInfoModel> orgData = getOrgData(getAllOrgs());
		List<OuInfoModel> newList = copyCreateEntityList(orgData, OuInfoModel.class);

		return newList;
	}

	@Override
	protected List<UserInfoModel> getUserInfoModelList(String mode) throws Exception {
		List<SeeyonUserInfoModel> userData = getUserData(getAllOrgs());
		List<UserInfoModel> newList = copyCreateEntityList(userData, UserInfoModel.class);

		return newList;
	}

}
