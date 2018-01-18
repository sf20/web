package openDemo.service.sync.allinpay;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import openDemo.entity.OuInfoModel;
import openDemo.entity.PositionModel;
import openDemo.entity.UserInfoModel;
import openDemo.service.sync.AbstractSyncService;
import openDemo.utils.SftpDownloadUtil;

@Service
public class AllinpaySyncService extends AbstractSyncService implements AllinpayConfig {
	// 登录参数
	private static final String HOST = "116.228.64.54";
	private static final int PORT = 8002;
	private static final String USERNAME = "sftpuser";
	private static final String PASSWORD = "1qaz@WSX";
	// 字符集编码
	private static final String CHARSET_GBK = "GBK";
	private static final String CHARSET_UTF8 = "UTF-8";
	// 文件名
	private static final String POSITION_FILE = "ywlinkstation.txt";
	private static final String OUINFO_FILE = "ywlinkdept.txt";
	private static final String USERINFO_FILE = "ywlinkman.txt";
	// 分隔符
	private static final String SEPARATOR = "|";
	// 分隔符正则转译
	private static final String SEPARATOR_REGEX = "\\|";

	public AllinpaySyncService() {
		super.setApikey(apikey);
		super.setSecretkey(secretkey);
		super.setBaseUrl(baseUrl);
		// 无全量增量区分
		// super.setModeFull(MODE_FULL);
		// super.setModeUpdate(MODE_UPDATE);
		// 人员信息中未提供岗位id
		super.setIsPosIdProvided(false);
		super.setSyncServiceName(this.getClass().getSimpleName());
	}

	@Override
	protected boolean isPosExpired(PositionModel pos) {
		return false;
	}

	@Override
	protected boolean isOrgExpired(OuInfoModel org) {
		// 部门状态（0：有效/1:无效）
		String status = org.getStatus();

		if ("1".equals(status)) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	protected boolean isUserExpired(UserInfoModel user) {
		// 是否活动用户（0/禁用;1/活动）
		String status = user.getStatus();
		// 是否被删除（0/否;1/是）
		String deleteStatus = user.getDeleteStatus();

		if ("0".equals(status) || "1".equals(deleteStatus)) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	protected void setRootOrgParentId(List<OuInfoModel> newList) {
		for (OuInfoModel org : newList) {
			// 客户数据中根组织的上级部门id为0 有多个根组织
			if ("0".equals(org.getParentID())) {
				org.setParentID(null);
			}
		}
	}

	@Override
	protected void changePropValues(List<UserInfoModel> newList) {
		for (UserInfoModel tempModel : newList) {

			// 性别字符串转换 0：男 1：女
			String sex = tempModel.getSex();
			if ("0".equals(sex)) {
				tempModel.setSex("男");
			} else if ("1".equals(sex)) {
				tempModel.setSex("女");
			}
		}
	}

	@Override
	public List<PositionModel> getPositionModelList(String mode) throws Exception {
		List<PositionModel> modelList = new ArrayList<PositionModel>();
		List<String> lines = SftpDownloadUtil.downloadAsString(HOST, PORT, USERNAME, PASSWORD, POSITION_FILE,
				CHARSET_GBK, CHARSET_UTF8);

		String[] tempStrArr = null;
		for (String line : lines) {
			if (StringUtils.isBlank(line) || !line.contains(SEPARATOR)) {
				continue;
			}
			tempStrArr = line.split(SEPARATOR_REGEX);

			PositionModel pos = new PositionModel();
			pos.setpNo(tempStrArr[0]);
			pos.setpNames(tempStrArr[1]);

			modelList.add(pos);
		}

		return modelList;
	}

	@Override
	public List<OuInfoModel> getOuInfoModelList(String mode) throws Exception {
		List<OuInfoModel> modelList = new ArrayList<OuInfoModel>();
		List<String> lines = SftpDownloadUtil.downloadAsString(HOST, PORT, USERNAME, PASSWORD, OUINFO_FILE, CHARSET_GBK,
				CHARSET_UTF8);

		String[] tempStrArr = null;
		for (String line : lines) {
			if (StringUtils.isBlank(line) || !line.contains(SEPARATOR)) {
				continue;
			}
			tempStrArr = line.split(SEPARATOR_REGEX);

			if (tempStrArr.length != 4) {
				continue;
			}

			OuInfoModel ouInfo = new OuInfoModel();
			ouInfo.setID(tempStrArr[0]);
			ouInfo.setOuName(tempStrArr[1]);
			ouInfo.setParentID(tempStrArr[2]);
			ouInfo.setStatus(tempStrArr[3]);

			modelList.add(ouInfo);
		}
		return modelList;
	}

	@Override
	public List<UserInfoModel> getUserInfoModelList(String mode) throws Exception {
		List<UserInfoModel> modelList = new ArrayList<UserInfoModel>();
		List<String> lines = SftpDownloadUtil.downloadAsString(HOST, PORT, USERNAME, PASSWORD, USERINFO_FILE,
				CHARSET_GBK, CHARSET_UTF8);

		String[] tempStrArr = null;
		for (String line : lines) {
			if (StringUtils.isBlank(line) || !line.contains(SEPARATOR)) {
				continue;
			}
			tempStrArr = line.split(SEPARATOR_REGEX);

			if (tempStrArr.length != 12) {
				continue;
			}

			// 同步AD域用户组织（id为10432）下的用户不同步
			if ("10432".equals(tempStrArr[5])) {
				continue;
			}

			UserInfoModel userInfo = new UserInfoModel();
			userInfo.setID(tempStrArr[0]);
			userInfo.setUserName(tempStrArr[1]);
			userInfo.setCnName(tempStrArr[2]);
			userInfo.setSex(tempStrArr[3]);
			userInfo.setBirthday(tempStrArr[4]);
			userInfo.setOrgOuCode(tempStrArr[5]);
			userInfo.setPostionName(tempStrArr[6]);
			userInfo.setMobile(tempStrArr[7]);
			userInfo.setMail(tempStrArr[8]);
			userInfo.setEntryTime(tempStrArr[9]);
			userInfo.setStatus(tempStrArr[10]);
			userInfo.setDeleteStatus(tempStrArr[11]);

			modelList.add(userInfo);
		}
		return modelList;
	}

}
