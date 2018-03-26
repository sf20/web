package openDemo.service.sync.aia;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import openDemo.entity.OuInfoModel;
import openDemo.entity.PositionModel;
import openDemo.entity.UserInfoModel;
import openDemo.service.sync.AbstractSyncService;

/**
 * 友邦保险同步Service
 * 
 * @author MKT-28
 *
 */
@Service
public class AIASyncService extends AbstractSyncService implements AIAConfig {
	// 文件名标准
	private static final String FILE_NAME_PREFIX_OUINFO = "dept_info_";
	private static final String FILE_NAME_PREFIX_USERINFO = "staff_info_";
	// 字符集编码
	private static final String CHARSET_UTF8 = "UTF-8";
	// 分隔符
	private static final String SEPARATOR = "|";
	// 分隔符正则转译
	private static final String SEPARATOR_REGEX = "\\|";
	// 记录日志
	private static final Logger logger = LogManager.getLogger(AIASyncService.class);

	private String syncServiceName;

	public AIASyncService() {
		super.setApikey(apikey);
		super.setSecretkey(secretkey);
		super.setBaseUrl(baseUrl);
		// 无全量增量区分
		// super.setModeFull(MODE_FULL);
		// super.setModeUpdate(MODE_UPDATE);
		// 人员信息中未提供岗位id
		// super.setIsPosIdProvided(false);
		syncServiceName = this.getClass().getSimpleName();
		super.setSyncServiceName(syncServiceName);
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
		return false;
	}

	@Override
	protected void setRootOrgParentId(List<OuInfoModel> dataList) {
		for (OuInfoModel org : dataList) {
			// 客户数据中根组织的上级部门id为0 有多个根组织
			if ("0".equals(org.getParentID())) {
				org.setParentID(null);
			}
		}
	}

	@Override
	protected void changePropValues(List<UserInfoModel> dataList) {
		// 日期格式：Sun Jun 30 00:00:00 CST 2013
		DateFormat format = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy", Locale.US);
		for (UserInfoModel tempModel : dataList) {
			// 入职日期修改
			String entryTime = tempModel.getEntryTime();
			if (StringUtils.isNotBlank(entryTime)) {
				try {
					tempModel.setEntryTime(DATE_FORMAT.format(format.parse(entryTime)));
				} catch (ParseException e) {
					logger.warn("日期格式有误 " + tempModel.getID() + "：" + entryTime);
				}
			}

			// 性别字符串转换 M：男 F：女
			String sex = tempModel.getSex();
			if ("M".equals(sex)) {
				tempModel.setSex("男");
			} else if ("F".equals(sex)) {
				tempModel.setSex("女");
			}
		}
	}

	@Override
	public List<OuInfoModel> getOuInfoModelList(String mode) throws Exception {
		return null;
	}

	@Override
	public List<PositionModel> getPositionModelList(String mode) throws Exception {
		return null;
	}

	@Override
	public List<UserInfoModel> getUserInfoModelList(String mode) throws Exception {
		return null;
	}

	/**
	 * 异步处理上传文件
	 * 
	 * @param fileItems
	 */
	public void asyncProcess(List<FileItem> fileItems) {
		try {
			// 先同步人员 后同步组织
			syncUserInfoDataFromFile(fileItems);
			syncOuInfoDataFromFile(fileItems);
		} catch (Exception e) {
			logger.error("定时同步[" + syncServiceName + "]出现异常", e);
		}
	}

	/**
	 * 同步部门数据文件
	 * 
	 * @param fileItems
	 * @throws Exception
	 */
	private void syncOuInfoDataFromFile(List<FileItem> fileItems) throws Exception {
		for (FileItem fileItem : fileItems) {
			// fileItem中封装的是上传文件
			if (!fileItem.isFormField()) {
				String fileName = fileItem.getName();
				// 数据同步
				if (fileName.contains(FILE_NAME_PREFIX_OUINFO)) {
					// 组织同步 部门状态全为有效
					List<String> lines = readLines(fileItem, CHARSET_UTF8, CHARSET_UTF8);
					opOrgSync(null, false, mapLinesToOuInfoModelList(lines));

					break;
				}
			}
		}
	}

	/**
	 * 同步人员数据文件
	 * 
	 * @param fileItems
	 */
	private void syncUserInfoDataFromFile(List<FileItem> fileItems) throws Exception {
		for (FileItem fileItem : fileItems) {
			// fileItem中封装的是上传文件
			if (!fileItem.isFormField()) {
				String fileName = fileItem.getName();
				// 数据同步
				if (fileName.contains(FILE_NAME_PREFIX_USERINFO)) {
					List<String> lines = readLines(fileItem, CHARSET_UTF8, CHARSET_UTF8);
					List<UserInfoModel> ouInfoModelList = mapLinesToUserInfoModelList(lines);
					// 岗位同步
					opPosSync(null, getPosListFromUsers(ouInfoModelList));

					// 人员同步 职工全为在职职工
					opUserSync(null, true, ouInfoModelList);

					break;
				}
			}
		}
	}

	/**
	 * 读取文件并获得行字符串集合
	 * 
	 * @param fileItem
	 *            上传的文件
	 * @param fromCharset
	 *            文件原来的字符集编码
	 * @param toCharset
	 *            同步用的字符集编码
	 * @return
	 * @throws IOException
	 */
	private List<String> readLines(FileItem fileItem, String fromCharset, String toCharset) throws IOException {
		List<String> lines = new ArrayList<String>();
		BufferedReader reader = null;
		InputStream ins = null;
		try {
			ins = fileItem.getInputStream();
			// 要读取文件的编码
			reader = new BufferedReader(new InputStreamReader(ins, fromCharset));

			String tempLine = null;
			// 一次读一行
			while ((tempLine = reader.readLine()) != null) {
				// 转为需要的编码
				lines.add(new String(tempLine.getBytes(), toCharset));
			}
		} finally {
			if (ins != null) {
				ins.close();
			}
			if (reader != null) {
				reader.close();
			}
		}

		return lines;
	}

	/**
	 * 组织同步
	 * 
	 * @param mode
	 *            全量增量区分
	 * @param isBaseInfo
	 *            同步接口需传字段
	 * @param dataList
	 * @throws Exception
	 */
	private void opOrgSync(String mode, boolean isBaseInfo, List<OuInfoModel> dataList) throws Exception {
		logger.info("组织同步[" + syncServiceName + "]Total Size: " + dataList.size());

		setRootOrgParentId(dataList);

		Map<String, List<OuInfoModel>> map = compareOrgList(getOuInfoList(), dataList);
		List<OuInfoModel> orgsToSyncDelete = map.get(MAPKEY_ORG_SYNC_DELETE);
		if (orgsToSyncDelete != null && orgsToSyncDelete.size() > 0) {
			syncDeleteOrgOneByOne(orgsToSyncDelete, true);
		}

		List<OuInfoModel> orgsToSyncAdd = map.get(MAPKEY_ORG_SYNC_ADD);
		if (orgsToSyncAdd != null && orgsToSyncAdd.size() > 0) {
			// 进行多次同步（上级部门需要先同步进平台，否则同步会失败）
			for (int i = 0; i < 3; i++) {
				syncAddOrUpdateOrgOneByOne(orgsToSyncAdd, isBaseInfo);
			}
		}

		List<OuInfoModel> orgsToSyncUpdate = map.get(MAPKEY_ORG_SYNC_UPDATE);
		if (orgsToSyncUpdate != null && orgsToSyncUpdate.size() > 0) {
			syncAddOrUpdateOrgOneByOne(orgsToSyncUpdate, isBaseInfo);
		}
	}

	/**
	 * 组织全量数据集合与最新获取组织数据集合进行比较
	 * 
	 * @param fullList
	 *            全量组织数据集合
	 * @param newList
	 *            最新获取组织数据集合
	 * @return 包含 同步新增、更新、 删除等组织集合的Map对象
	 */
	protected Map<String, List<OuInfoModel>> compareOrgList(List<OuInfoModel> fullList, List<OuInfoModel> newList) {
		Map<String, List<OuInfoModel>> map = new HashMap<String, List<OuInfoModel>>();

		List<OuInfoModel> orgsToSyncAdd = new ArrayList<OuInfoModel>();
		List<OuInfoModel> orgsToSyncUpdate = new ArrayList<OuInfoModel>();
		List<OuInfoModel> orgsToSyncDelete = new ArrayList<OuInfoModel>();

		for (OuInfoModel newOrg : newList) {
			OuInfoModel orgInFullList = null;
			for (OuInfoModel fullOrg : fullList) {
				if (fullOrg.equals(newOrg)) {
					orgInFullList = fullOrg;
					break;
				}
			}
			// 待新增组织
			if (orgInFullList == null) {
				orgsToSyncAdd.add(newOrg);
			}
			// 存在组织更新
			else {
				orgsToSyncUpdate.add(newOrg);
			}
		}

		// 被删除的组织
		for (OuInfoModel oldOrg : fullList) {
			if (!newList.contains(oldOrg)) {
				orgsToSyncDelete.add(oldOrg);
			}
		}

		map.put(MAPKEY_ORG_SYNC_ADD, orgsToSyncAdd);
		map.put(MAPKEY_ORG_SYNC_UPDATE, orgsToSyncUpdate);
		map.put(MAPKEY_ORG_SYNC_DELETE, orgsToSyncDelete);

		logger.info("组织同步[" + syncServiceName + "]新增Size: " + orgsToSyncAdd.size());
		logger.info("组织同步[" + syncServiceName + "]更新Size: " + orgsToSyncUpdate.size());
		logger.info("组织同步[" + syncServiceName + "]删除Size: " + orgsToSyncDelete.size());

		return map;
	}

	/**
	 * 岗位同步
	 * 
	 * @param mode
	 *            全量增量区分
	 * @param dataList
	 * @throws Exception
	 */
	private void opPosSync(String mode, List<PositionModel> dataList) throws Exception {
		logger.info("岗位同步[" + syncServiceName + "]Total Size: " + dataList.size());

		compareDataWithDB(dataList, apikey);
		setFullPosNames(dataList);

		logger.info("岗位同步[" + syncServiceName + "]新增Size: " + dataList.size());
		syncAddPosOneByOne(dataList);
	}

	/**
	 * 用户同步
	 * 
	 * @param mode
	 *            全量增量区分
	 * @param islink
	 *            是否同步用户基本信息
	 * @param dataList
	 * @throws Exception
	 */
	private void opUserSync(String mode, boolean islink, List<UserInfoModel> dataList) throws Exception {
		logger.info("用户同步[" + syncServiceName + "]Total Size: " + dataList.size());

		changePropValues(dataList);
		setPositionNoToUser(dataList);

		Map<String, List<UserInfoModel>> map = compareUserList(getUserInfoList(), dataList);
		List<UserInfoModel> usersToDisable = map.get(MAPKEY_USER_SYNC_DISABLE);
		if (usersToDisable != null && usersToDisable.size() > 0) {
			// 此处直接删除
			syncDeleteUserOneByOne(usersToDisable, true);
		}

		List<UserInfoModel> usersToSyncAdd = map.get(MAPKEY_USER_SYNC_ADD);
		if (usersToSyncAdd != null && usersToSyncAdd.size() > 0) {
			syncAddOrUpdateUserOneByOne(usersToSyncAdd, islink);
		}

		List<UserInfoModel> usersToSyncUpdate = map.get(MAPKEY_USER_SYNC_UPDATE);
		if (usersToSyncUpdate != null && usersToSyncUpdate.size() > 0) {
			syncAddOrUpdateUserOneByOne(usersToSyncUpdate, islink);
		}
	}

	/**
	 * 用户全量数据集合与最新获取用户数据集合进行比较
	 * 
	 * @param fullList
	 *            全量用户数据集合
	 * @param newList
	 *            最新获取用户数据集合
	 * @return 包含 同步新增、更新、禁用等用户集合的Map对象
	 */
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
			if (!newList.contains(oldUser)) {
				usersToSyncDisable.add(oldUser);
			}
		}

		map.put(MAPKEY_USER_SYNC_ADD, usersToSyncAdd);
		map.put(MAPKEY_USER_SYNC_UPDATE, usersToSyncUpdate);
		map.put(MAPKEY_USER_SYNC_DISABLE, usersToSyncDisable);

		logger.info("用户同步[" + syncServiceName + "]新增Size: " + usersToSyncAdd.size());
		logger.info("用户同步[" + syncServiceName + "]更新Size: " + usersToSyncUpdate.size());
		logger.info("用户同步[" + syncServiceName + "]禁用Size: " + usersToSyncDisable.size());

		return map;
	}

	/**
	 * 将文件行记录转为组织单位对象集合
	 * 
	 * @param lines
	 * @return
	 */
	private List<OuInfoModel> mapLinesToOuInfoModelList(List<String> lines) {
		List<OuInfoModel> modelList = new ArrayList<OuInfoModel>();

		String[] tempStrArr = null;
		// 忽略title行
		for (int i = 1; i < lines.size(); i++) {
			String lineRecord = lines.get(i);
			if (StringUtils.isBlank(lineRecord) || !lineRecord.contains(SEPARATOR)) {
				continue;
			}
			tempStrArr = lineRecord.split(SEPARATOR_REGEX);

			// ClassID|ClassName|MANAGER_ID|DeptID|Company|Department_CHN|ParentID|ParentPath|Depth|RootID|Child|PrevID|NextID
			if (tempStrArr.length != 13) {
				continue;
			}

			OuInfoModel ouInfo = new OuInfoModel();
			ouInfo.setID(tempStrArr[0]);
			ouInfo.setOuName(tempStrArr[1]);
			ouInfo.setManagerId(tempStrArr[2]);
			ouInfo.setParentID(tempStrArr[6]);

			modelList.add(ouInfo);
		}

		return modelList;
	}

	/**
	 * 将文件行记录转为用户对象集合
	 * 
	 * @param lines
	 * @return
	 */
	private List<UserInfoModel> mapLinesToUserInfoModelList(List<String> lines) {
		List<UserInfoModel> modelList = new ArrayList<UserInfoModel>();

		String[] tempStrArr = null;
		// 忽略title行
		for (int i = 1; i < lines.size(); i++) {
			String lineRecord = lines.get(i);
			if (StringUtils.isBlank(lineRecord) || !lineRecord.contains(SEPARATOR)) {
				continue;
			}
			tempStrArr = lineRecord.split(SEPARATOR_REGEX);

			// GlobalID|Cname|Pinyin|Firstname|Lastname|SEX|Jobtitle|GRADE|MobliePhone|Email_Business|HIRE_DT|Z_CO_JOIN_DT|DEPTID|ClassID|Company_CN|Department_CN|Z_MANAGER_ID
			if (tempStrArr.length != 17) {
				continue;
			}

			UserInfoModel userInfo = new UserInfoModel();
			userInfo.setID(tempStrArr[0]);
			userInfo.setUserName(tempStrArr[0]);
			userInfo.setCnName(tempStrArr[1]);
			userInfo.setSex(tempStrArr[5]);
			userInfo.setPostionName(tempStrArr[6]);// tempStrArr[6]==null?null:tempStrArr[6].split(",")[1]
			userInfo.setSpare1(tempStrArr[7]);
			userInfo.setMobile(tempStrArr[8]);
			userInfo.setMail(tempStrArr[9]);
			userInfo.setEntryTime(tempStrArr[11]);
			userInfo.setOrgOuCode(tempStrArr[13]);

			modelList.add(userInfo);
		}

		return modelList;
	}
}
