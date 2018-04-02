package openDemo.service.sync.jrtt;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import openDemo.dao.OuInfoDao;
import openDemo.dao.UserInfoDao;
import openDemo.entity.OuInfoModel;
import openDemo.entity.ResultEntity;
import openDemo.entity.UserInfoModel;
import openDemo.entity.sync.jrtt.ToutiaoOuInfoModel;
import openDemo.entity.sync.jrtt.ToutiaoResJsonModel;
import openDemo.entity.sync.jrtt.ToutiaoUserInfoModel;
import openDemo.service.SyncOrgService;
import openDemo.service.SyncPositionService;
import openDemo.service.SyncUserService;

/**
 * 今日头条同步Service
 * 
 * @author MKT-28
 *
 */
@Service
public class JrttSyncService {
	// 文件名标准 1=商业化的数据 2=CQC的数据
	private static final String FILE_NAME_PREFIX_OUINFO1 = "department.1";
	private static final String FILE_NAME_PREFIX_OUINFO2 = "department.2";
	private static final String FILE_NAME_PREFIX_USERINFO1 = "employee.1";
	private static final String FILE_NAME_PREFIX_USERINFO2 = "employee.2";
	// 字符集编码
	private static final String CHARSET_UTF8 = "UTF-8";
	// 自定义map的key
	private static final String MAPKEY_USER_SYNC_ADD = "userSyncAdd";
	private static final String MAPKEY_USER_SYNC_UPDATE = "userSyncUpdate";
	private static final String MAPKEY_USER_SYNC_DISABLE = "userSyncDisable";
	private static final String MAPKEY_ORG_SYNC_ADD = "orgSyncAdd";
	private static final String MAPKEY_ORG_SYNC_UPDATE = "orgSyncUpdate";
	private static final String MAPKEY_ORG_SYNC_DELETE = "orgSyncDelete";
	// 请求同步接口成功返回码
	private static final String SYNC_CODE_SUCCESS = "0";
	// TODO 正式环境/测试环境切换
	private static final String BASE_URL = "http://api.qida.yunxuetang.com.cn/";
	// 记录日志
	private static final Logger logger = LogManager.getLogger(JrttSyncService.class);

	// 请求同步接口的service
	@Autowired
	protected SyncPositionService positionService;
	@Autowired
	protected SyncOrgService orgService;
	@Autowired
	protected SyncUserService userService;

	private String syncServiceName;
	// json解析对象
	private ObjectMapper mapper;

	public JrttSyncService() {
		syncServiceName = this.getClass().getSimpleName();

		// 创建用于json反序列化的对象
		mapper = new ObjectMapper();
		// 忽略json中多余的属性字段
		mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
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
				// 商业化数据同步
				if (fileName.contains(FILE_NAME_PREFIX_OUINFO1)) {
					String jsonString = readJsonString(fileItem, CHARSET_UTF8, CHARSET_UTF8);
					opOrgSync(false, parseJsonToOuInfoModelList(jsonString), JrttConfig1.apikey, JrttConfig1.secretkey);
				}
				// CQC数据同步
				else if (fileName.contains(FILE_NAME_PREFIX_OUINFO2)) {
					String jsonString = readJsonString(fileItem, CHARSET_UTF8, CHARSET_UTF8);
					opOrgSync(false, parseJsonToOuInfoModelList(jsonString), JrttConfig2.apikey, JrttConfig2.secretkey);
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
				// 商业化数据同步
				if (fileName.contains(FILE_NAME_PREFIX_USERINFO1)) {
					String jsonString = readJsonString(fileItem, CHARSET_UTF8, CHARSET_UTF8);
					List<UserInfoModel> ouInfoModelList = parseJsonToUserInfoModelList(jsonString);
					opUserSync(true, ouInfoModelList, JrttConfig1.apikey, JrttConfig1.secretkey);
				}
				// CQC数据同步
				else if (fileName.contains(FILE_NAME_PREFIX_USERINFO2)) {
					String jsonString = readJsonString(fileItem, CHARSET_UTF8, CHARSET_UTF8);
					List<UserInfoModel> ouInfoModelList = parseJsonToUserInfoModelList(jsonString);
					opUserSync(true, ouInfoModelList, JrttConfig2.apikey, JrttConfig2.secretkey);
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
	 * 通过复制属性值的方法将数据模型集合转换为同步用的对象集合
	 * 
	 * @param fromList
	 *            数据模型集合
	 * @param toListClassType
	 *            复制目标对象的类型
	 * @return 复制后的对象集合
	 * @throws ReflectiveOperationException
	 */
	protected <E, T> List<T> copyCreateEntityList(List<E> fromList, Class<T> toListClassType)
			throws ReflectiveOperationException {
		List<T> entityList = null;

		if (fromList != null) {
			int listSize = fromList.size();
			entityList = new ArrayList<T>(listSize);

			for (int i = 0; i < listSize; i++) {
				T instance = toListClassType.newInstance();
				BeanUtils.copyProperties(instance, fromList.get(i));
				entityList.add(instance);
			}
		}

		return entityList;
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
	 * @param isBaseInfo
	 *            同步接口需传字段
	 * @param dataList
	 * @param apiKey
	 * @param secretKey
	 * @throws Exception
	 */
	private void opOrgSync(boolean isBaseInfo, List<OuInfoModel> dataList, String apiKey, String secretKey)
			throws Exception {
		logger.info("组织同步[" + syncServiceName + "]Total Size: " + dataList.size());

		// 将上级部门数据先进行同步
		sortOrgList(dataList);

		List<OuInfoModel> ouInfoListFromDB = getOuInfoListFromDB(apiKey);
		Map<String, List<OuInfoModel>> map = compareOrgList(ouInfoListFromDB, dataList);
		List<OuInfoModel> orgsToSyncDelete = map.get(MAPKEY_ORG_SYNC_DELETE);
		if (orgsToSyncDelete != null && orgsToSyncDelete.size() > 0) {
			syncDeleteOrgOneByOne(orgsToSyncDelete, true, apiKey, secretKey);
		}

		List<OuInfoModel> orgsToSyncAdd = map.get(MAPKEY_ORG_SYNC_ADD);
		if (orgsToSyncAdd != null && orgsToSyncAdd.size() > 0) {
			// 进行多次同步（上级部门需要先同步进平台，否则同步会失败）
			for (int i = 0; i < 3; i++) {
				syncAddOrUpdateOrgOneByOne(orgsToSyncAdd, isBaseInfo, apiKey, secretKey);
			}
		}

		List<OuInfoModel> orgsToSyncUpdate = map.get(MAPKEY_ORG_SYNC_UPDATE);
		if (orgsToSyncUpdate != null && orgsToSyncUpdate.size() > 0) {
			syncAddOrUpdateOrgOneByOne(orgsToSyncUpdate, isBaseInfo, apiKey, secretKey);
		}

		ouInfoListFromDB = null;
	}

	/**
	 * 从数据库获取组织数据
	 * 
	 * @param apiKey
	 * @return
	 * @throws SQLException
	 */
	protected List<OuInfoModel> getOuInfoListFromDB(String apiKey) throws SQLException {
		OuInfoDao dao = new OuInfoDao();
		return dao.getAllByOrgId(apiKey);
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
	 * @param islink
	 *            是否同步用户基本信息
	 * @param dataList
	 * @param apiKey
	 * @param secretKey
	 * @throws Exception
	 */
	private void opUserSync(boolean islink, List<UserInfoModel> dataList, String apiKey, String secretKey)
			throws Exception {
		logger.info("用户同步[" + syncServiceName + "]Total Size: " + dataList.size());

		// id作为用户登录名
		for (UserInfoModel tempModel : dataList) {
			// userName <= id
			tempModel.setUserName(tempModel.getID());
		}

		List<UserInfoModel> userInfoListFromDB = getUserInfoListFromDB(apiKey);
		Map<String, List<UserInfoModel>> map = compareUserList(userInfoListFromDB, dataList);
		List<UserInfoModel> usersToDisable = map.get(MAPKEY_USER_SYNC_DISABLE);
		if (usersToDisable != null && usersToDisable.size() > 0) {
			syncDisableUserOneByOne(usersToDisable, true, apiKey, secretKey);
		}

		List<UserInfoModel> usersToSyncAdd = map.get(MAPKEY_USER_SYNC_ADD);
		if (usersToSyncAdd != null && usersToSyncAdd.size() > 0) {
			syncAddOrUpdateUserOneByOne(usersToSyncAdd, islink, apiKey, secretKey);
		}

		List<UserInfoModel> usersToSyncUpdate = map.get(MAPKEY_USER_SYNC_UPDATE);
		if (usersToSyncUpdate != null && usersToSyncUpdate.size() > 0) {
			syncAddOrUpdateUserOneByOne(usersToSyncUpdate, islink, apiKey, secretKey);
		}

		userInfoListFromDB = null;
	}

	/**
	 * 从数据库获取人员数据
	 * 
	 * @param apiKey
	 * @return
	 * @throws SQLException
	 */
	protected List<UserInfoModel> getUserInfoListFromDB(String apiKey) throws SQLException {
		UserInfoDao dao = new UserInfoDao();
		return dao.getAllByOrgId(apiKey);
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
	protected void syncAddOrUpdateOrgOneByOne(List<OuInfoModel> orgsToSyncAddOrUpdate, boolean isBaseInfo,
			String apikey, String secretkey) {
		List<OuInfoModel> tempList = new ArrayList<OuInfoModel>();
		ResultEntity resultEntity = null;
		for (OuInfoModel org : orgsToSyncAddOrUpdate) {
			tempList.add(org);

			try {
				resultEntity = orgService.ous(isBaseInfo, tempList, apikey, secretkey, BASE_URL);
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
	protected void syncDeleteOrgOneByOne(List<OuInfoModel> orgsToSyncDelete, boolean ifPringLog, String apikey,
			String secretkey) {
		List<String> tempList = new ArrayList<String>();
		ResultEntity resultEntity = null;
		for (OuInfoModel org : orgsToSyncDelete) {
			tempList.add(org.getID());

			try {
				resultEntity = orgService.deleteous(tempList, apikey, secretkey, BASE_URL);

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
	protected void syncAddOrUpdateUserOneByOne(List<UserInfoModel> usersToSyncAddOrUpdate, boolean islink,
			String apikey, String secretkey) {
		List<UserInfoModel> tempList = new ArrayList<UserInfoModel>();
		ResultEntity resultEntity = null;
		for (UserInfoModel user : usersToSyncAddOrUpdate) {
			tempList.add(user);

			try {
				resultEntity = userService.userSync(islink, tempList, apikey, secretkey, BASE_URL);
				if (!SYNC_CODE_SUCCESS.equals(resultEntity.getCode())) {
					// 忽略邮箱再同步一次
					user.setMail(null);
					tempList.set(0, user);
					resultEntity = userService.userSync(islink, tempList, apikey, secretkey, BASE_URL);
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
	protected void syncDisableUserOneByOne(List<UserInfoModel> usersToDisable, boolean ifPringLog, String apikey,
			String secretkey) {
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
				resultEntity = userService.disabledusersSync(tempList, apikey, secretkey, BASE_URL);
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
	protected void syncDeleteUserOneByOne(List<UserInfoModel> usersToDelete, boolean ifPringLog, String apikey,
			String secretkey) {
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
				resultEntity = userService.deletedusersSync(tempList, apikey, secretkey, BASE_URL);
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

	/**
	 * 同步返回错误信息日志记录
	 * 
	 * @param type
	 * @param errKey
	 * @param resultEntity
	 */
	protected void printLog(String type, String errKey, ResultEntity resultEntity) {
		logger.error(type + "ID：" + errKey + " 错误信息：" + resultEntity.getCode() + "-" + resultEntity.getMessage());
	}
}
