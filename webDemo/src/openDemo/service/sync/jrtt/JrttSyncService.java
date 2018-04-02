package openDemo.service.sync.jrtt;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import openDemo.entity.OuInfoModel;
import openDemo.entity.PositionModel;
import openDemo.entity.ResultEntity;
import openDemo.entity.UserInfoModel;
import openDemo.entity.sync.jrtt.ToutiaoOuInfoModel;
import openDemo.entity.sync.jrtt.ToutiaoResJsonModel;
import openDemo.entity.sync.jrtt.ToutiaoUserInfoModel;
import openDemo.service.sync.AbstractSyncService;

/**
 * 今日头条同步Service
 * 
 * @author MKT-28
 *
 */
@Service
public class JrttSyncService extends AbstractSyncService implements JrttConfig {
	// 文件名标准
	private static final String FILE_NAME_PREFIX_OUINFO = "department";
	private static final String FILE_NAME_PREFIX_USERINFO = "employee";
	// 字符集编码
	private static final String CHARSET_UTF8 = "UTF-8";
	// 记录日志
	private static final Logger logger = LogManager.getLogger(JrttSyncService.class);

	private String syncServiceName;
	// json解析对象
	private ObjectMapper mapper;

	public JrttSyncService() {
		super.setApikey(apikey);
		super.setSecretkey(secretkey);
		super.setBaseUrl(baseUrl);
		syncServiceName = this.getClass().getSimpleName();
		super.setSyncServiceName(syncServiceName);

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
		return false;
	}

	@Override
	protected void setRootOrgParentId(List<OuInfoModel> dataList) {
		// 无需设置
	}

	@Override
	protected void changePropValues(List<UserInfoModel> dataList) {
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
			syncOuInfoDataFromFile(fileItems);
			syncUserInfoDataFromFile(fileItems);
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
					String jsonString = readJsonString(fileItem, CHARSET_UTF8, CHARSET_UTF8);
					opOrgSync(null, false, parseJsonToOuInfoModelList(jsonString));

					break;
				}
			}
		}
	}

	/**
	 * 部门json解析
	 * 
	 * @param jsonString
	 * @return
	 * @throws IOException
	 * @throws ReflectiveOperationException
	 */
	private List<OuInfoModel> parseJsonToOuInfoModelList(String jsonString)
			throws IOException, ReflectiveOperationException {
		// 将字符串中的Unicode转换为中文
		jsonString = unicodeToString(jsonString);

		ToutiaoResJsonModel<ToutiaoOuInfoModel> resJsonModel = mapper.readValue(jsonString,
				new TypeReference<ToutiaoResJsonModel<ToutiaoOuInfoModel>>() {
				});
		List<ToutiaoOuInfoModel> departments = resJsonModel.getDepartments();

		return copyCreateEntityList(departments, OuInfoModel.class);
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
					String jsonString = readJsonString(fileItem, CHARSET_UTF8, CHARSET_UTF8);
					List<UserInfoModel> ouInfoModelList = parseJsonToUserInfoModelList(jsonString);

					// 人员同步 职工全为在职职工
					opUserSync(null, true, ouInfoModelList);

					break;
				}
			}
		}
	}

	/**
	 * 人员json解析
	 * 
	 * @param jsonString
	 * @return
	 * @throws IOException
	 * @throws ReflectiveOperationException
	 */
	private List<UserInfoModel> parseJsonToUserInfoModelList(String jsonString)
			throws IOException, ReflectiveOperationException {
		// 将字符串中的Unicode转换为中文
		jsonString = unicodeToString(jsonString);

		ToutiaoResJsonModel<ToutiaoUserInfoModel> resJsonModel = mapper.readValue(jsonString,
				new TypeReference<ToutiaoResJsonModel<ToutiaoUserInfoModel>>() {
				});
		List<ToutiaoUserInfoModel> employees = resJsonModel.getEmployees();

		return copyCreateEntityList(employees, UserInfoModel.class);
	}

	/**
	 * 读取文件并获得json字符串
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
	private String readJsonString(FileItem fileItem, String fromCharset, String toCharset) throws IOException {
		StringBuffer jsonString = new StringBuffer();
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
				jsonString.append(new String(tempLine.getBytes(), toCharset));
			}
		} finally {
			if (ins != null) {
				ins.close();
			}
			if (reader != null) {
				reader.close();
			}
		}

		return jsonString.toString();
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

		// 将上级部门数据先进行同步
		sortOrgList(dataList);

		List<OuInfoModel> ouInfoListFromDB = getOuInfoListFromDB();
		Map<String, List<OuInfoModel>> map = compareOrgList(ouInfoListFromDB, dataList);
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

		ouInfoListFromDB = null;
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

		// id作为用户登录名
		for (UserInfoModel tempModel : dataList) {
			// userName <= id
			tempModel.setUserName(tempModel.getID());
		}

		List<UserInfoModel> userInfoListFromDB = getUserInfoListFromDB();
		Map<String, List<UserInfoModel>> map = compareUserList(userInfoListFromDB, dataList);
		List<UserInfoModel> usersToDisable = map.get(MAPKEY_USER_SYNC_DISABLE);
		if (usersToDisable != null && usersToDisable.size() > 0) {
			syncDisableUserOneByOne(usersToDisable, true);
		}

		List<UserInfoModel> usersToSyncAdd = map.get(MAPKEY_USER_SYNC_ADD);
		if (usersToSyncAdd != null && usersToSyncAdd.size() > 0) {
			syncAddOrUpdateUserOneByOne(usersToSyncAdd, islink);
		}

		List<UserInfoModel> usersToSyncUpdate = map.get(MAPKEY_USER_SYNC_UPDATE);
		if (usersToSyncUpdate != null && usersToSyncUpdate.size() > 0) {
			syncAddOrUpdateUserOneByOne(usersToSyncUpdate, islink);
		}

		userInfoListFromDB = null;
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
	 * 逐个组织同步新增或更新
	 * 
	 * @param orgsToSyncAddOrUpdate
	 * @param isBaseInfo
	 */
	@Override
	protected void syncAddOrUpdateOrgOneByOne(List<OuInfoModel> orgsToSyncAddOrUpdate, boolean isBaseInfo) {
		List<OuInfoModel> tempList = new ArrayList<OuInfoModel>();
		ResultEntity resultEntity = null;
		for (OuInfoModel org : orgsToSyncAddOrUpdate) {
			tempList.add(org);

			try {
				resultEntity = orgService.ous(isBaseInfo, tempList, apikey, secretkey, baseUrl);
				if (!SYNC_CODE_SUCCESS.equals(resultEntity.getCode())) {
					printLog("组织同步[" + syncServiceName + "]失败 ", org.getOuName(), resultEntity);
				}
			} catch (IOException e) {
				logger.error("组织同步[" + syncServiceName + "]失败 " + org.getOuName(), e);
			}

			tempList.clear();
		}
	}

	/**
	 * 逐个组织同步删除
	 * 
	 * @param orgsToSyncDelete
	 * @param ifPringLog
	 */
	@Override
	protected void syncDeleteOrgOneByOne(List<OuInfoModel> orgsToSyncDelete, boolean ifPringLog) {
		List<String> tempList = new ArrayList<String>();
		ResultEntity resultEntity = null;
		for (OuInfoModel org : orgsToSyncDelete) {
			tempList.add(org.getID());

			try {
				resultEntity = orgService.deleteous(tempList, apikey, secretkey, baseUrl);

				if (!SYNC_CODE_SUCCESS.equals(resultEntity.getCode())) {
					if (ifPringLog) {
						printLog("组织同步[" + syncServiceName + "]删除失败 ", org.getOuName(), resultEntity);
					}
				}
			} catch (IOException e) {
				logger.error("组织同步[" + syncServiceName + "]删除失败 " + org.getOuName(), e);
			}

			tempList.clear();
		}

	}

	/**
	 * 逐个用户同步新增/更新
	 * 
	 * @param usersToSyncAddOrUpdate
	 * @param islink
	 */
	@Override
	protected void syncAddOrUpdateUserOneByOne(List<UserInfoModel> usersToSyncAddOrUpdate, boolean islink) {
		List<UserInfoModel> tempList = new ArrayList<UserInfoModel>();
		ResultEntity resultEntity = null;
		for (UserInfoModel user : usersToSyncAddOrUpdate) {
			tempList.add(user);

			try {
				resultEntity = userService.userSync(islink, tempList, apikey, secretkey, baseUrl);
				if (!SYNC_CODE_SUCCESS.equals(resultEntity.getCode())) {
					// 忽略邮箱再同步一次
					user.setMail(null);
					tempList.set(0, user);
					resultEntity = userService.userSync(islink, tempList, apikey, secretkey, baseUrl);
					if (!SYNC_CODE_SUCCESS.equals(resultEntity.getCode())) {
						printLog("用户同步[" + syncServiceName + "]失败 ", user.getID(), resultEntity);
					}
				}
			} catch (IOException e) {
				logger.error("用户同步[" + syncServiceName + "]失败 " + user.getID(), e);
			}

			tempList.clear();
		}
	}

	/**
	 * 逐个用户同步禁用
	 * 
	 * @param usersToDisable
	 * @param ifPringLog
	 */
	@Override
	protected void syncDisableUserOneByOne(List<UserInfoModel> usersToDisable, boolean ifPringLog) {
		List<String> tempList = new ArrayList<String>();
		ResultEntity resultEntity = null;
		for (UserInfoModel user : usersToDisable) {
			// 用户名是admin时忽略
			String userName = user.getUserName();
			if ("admin".equals(userName)) {
				continue;
			}

			tempList.add(userName);

			try {
				resultEntity = userService.disabledusersSync(tempList, apikey, secretkey, baseUrl);
				if (!SYNC_CODE_SUCCESS.equals(resultEntity.getCode())) {
					if (ifPringLog) {
						printLog("用户同步[" + syncServiceName + "]禁用失败 ", user.getID(), resultEntity);
					}
				}
			} catch (IOException e) {
				logger.error("用户同步[" + syncServiceName + "]禁用失败 " + user.getID(), e);
			}

			tempList.clear();
		}
	}

	/**
	 * 逐个用户同步删除（用户被删除后学习记录等数据会被清空且不可恢复，已改为禁用）
	 * 
	 * @param usersToDelete
	 * @param ifPringLog
	 */
	@Override
	protected void syncDeleteUserOneByOne(List<UserInfoModel> usersToDelete, boolean ifPringLog) {
		List<String> tempList = new ArrayList<String>();
		ResultEntity resultEntity = null;
		for (UserInfoModel user : usersToDelete) {
			// 用户名是admin时忽略不删除
			String userName = user.getUserName();
			if ("admin".equals(userName)) {
				continue;
			}

			tempList.add(userName);

			try {
				resultEntity = userService.deletedusersSync(tempList, apikey, secretkey, baseUrl);
				if (!SYNC_CODE_SUCCESS.equals(resultEntity.getCode())) {
					if (ifPringLog) {
						printLog("用户同步[" + syncServiceName + "]删除失败 ", user.getID(), resultEntity);
					}
				}
			} catch (IOException e) {
				logger.error("用户同步[" + syncServiceName + "]删除失败 " + user.getID(), e);
			}

			tempList.clear();
		}
	}

	/**
	 * 十六进制转中文
	 * 
	 * @param str
	 * @return
	 */
	public String unicodeToString(String str) {
		Pattern pattern = Pattern.compile("(\\\\u(\\p{XDigit}{4}))");
		Matcher matcher = pattern.matcher(str);
		char ch;
		while (matcher.find()) {
			ch = (char) Integer.parseInt(matcher.group(2), 16);
			str = str.replace(matcher.group(1), ch + "");
		}
		return str;
	}

	/**
	 * 部门数据排序
	 * 
	 * @param ouinfoList
	 * @return
	 */
	private List<OuInfoModel> sortOrgList(List<OuInfoModel> ouinfoList) {
		List<OuInfoModel> result = new ArrayList<OuInfoModel>();

		List<OuInfoModel> parentList = new ArrayList<OuInfoModel>();
		List<OuInfoModel> childList = new ArrayList<OuInfoModel>();

		Map<String, String> mapAllID = new HashMap<String, String>();
		Map<String, String> mapAllParentID = new HashMap<String, String>();
		// 获取所有部门ID集合
		for (OuInfoModel ouInfo : ouinfoList) {
			mapAllID.put(ouInfo.getID(), ouInfo.getID());
			mapAllParentID.put(ouInfo.getParentID(), ouInfo.getParentID());
		}
		// 获取一级部门集合
		for (OuInfoModel ouInfo : ouinfoList) {
			if (StringUtils.isBlank(ouInfo.getParentID()) || !mapAllID.containsKey(ouInfo.getParentID())) {
				ouInfo.setParentID("");
				// 父级部门集合
				parentList.add(ouInfo);
			} else {
				// 子级部门集合
				childList.add(ouInfo);
			}
		}

		// 遍历父级部门，并获取该部门下的所有子级部门
		for (OuInfoModel ouInfo : parentList) {
			result.add(ouInfo);
			result.addAll(listToTree(ouInfo.getID(), childList));
		}

		return result;
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

		for (OuInfoModel ouInfo : parentList) {
			resultList.add(ouInfo);
			resultList.addAll(listToTree(ouInfo.getID(), childList));
		}

		return resultList;
	}
}
