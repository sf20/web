package openDemo.service.sync;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import openDemo.dao.OuInfoDao;
import openDemo.dao.PositionDao;
import openDemo.dao.UserInfoDao;
import openDemo.entity.OuInfoModel;
import openDemo.entity.PositionModel;
import openDemo.entity.ResultEntity;
import openDemo.entity.UserInfoModel;
import openDemo.service.SyncOrgService;
import openDemo.service.SyncPositionService;
import openDemo.service.SyncUserService;

public abstract class AbstractSyncService implements CustomTimerTask {
	// 自定义map的key
	public static final String MAPKEY_USER_SYNC_ADD = "userSyncAdd";
	public static final String MAPKEY_USER_SYNC_UPDATE = "userSyncUpdate";
	public static final String MAPKEY_USER_SYNC_DISABLE = "userSyncDisable";
	public static final String MAPKEY_ORG_SYNC_ADD = "orgSyncAdd";
	public static final String MAPKEY_ORG_SYNC_UPDATE = "orgSyncUpdate";
	public static final String MAPKEY_ORG_SYNC_DELETE = "orgSyncDelete";
	public static final String MAPKEY_POS_SYNC_ADD = "posSyncAdd";
	public static final String MAPKEY_POS_SYNC_UPDATE = "posSyncUpdate";
	// 请求同步接口成功返回码
	public static final String SYNC_CODE_SUCCESS = "0";
	// 岗位类别的默认值
	public static final String POSITION_CLASS_DEFAULT = "未分类";
	public static final String POSITION_CLASS_SEPARATOR = ";";
	// 日期格式化用
	public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
	public static final SimpleDateFormat YYMMDD_DATE_FORMAT = new SimpleDateFormat("yyyyMMdd");
	// 记录日志
	public static Logger LOGGER = LogManager.getLogger(AbstractSyncService.class);

	// 请求同步接口的service
	@Autowired
	protected SyncPositionService positionService;
	@Autowired
	protected SyncOrgService orgService;
	@Autowired
	protected SyncUserService userService;
	// 用于存放数据库查询到的数据的集合
	private List<PositionModel> positionListFromDB;
	private List<OuInfoModel> ouInfoListFromDB;
	private List<UserInfoModel> userInfoListFromDB;

	// 参数配置
	private String apikey;
	private String secretkey;
	private String baseUrl;
	// 全量增量区分
	private String modeFull = "1";// 默认为1
	private String modeUpdate = "2";// 默认为2
	// 是否提供岗位id标志（客户提供的岗位数据中没有岗位id或者提供了包含岗位id的岗位数据但人员数据中关联的只有岗位名时需设置该值为false）
	private boolean isPosIdProvided = true;// 默认为已提供
	// 子类同步service的类名
	private String syncServiceName;

	public void setApikey(String apikey) {
		this.apikey = apikey;
	}

	public void setSecretkey(String secretkey) {
		this.secretkey = secretkey;
	}

	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}

	public void setModeFull(String modeFull) {
		this.modeFull = modeFull;
	}

	public void setModeUpdate(String modeUpdate) {
		this.modeUpdate = modeUpdate;
	}

	/**
	 * 客户提供的岗位数据中没有岗位id或者提供了包含岗位id的岗位数据但人员数据中关联的只有岗位名时需设置该值为false
	 * 
	 * @param isPosIdProvided
	 */
	public void setIsPosIdProvided(boolean isPosIdProvided) {
		this.isPosIdProvided = isPosIdProvided;
	}

	/**
	 * 设置子类同步service的类名
	 * 
	 * @param syncServiceName
	 */
	public void setSyncServiceName(String syncServiceName) {
		this.syncServiceName = syncServiceName;
	}

	@Override
	public void execute() {
		try {
			sync();
		} catch (Exception e) {
			LOGGER.error("定时同步[" + syncServiceName + "]出现异常", e);
		}
	}

	/**
	 * 对外提供的同步方法
	 * 
	 * @throws Exception
	 */
	public void sync() throws Exception {
		LOGGER.info("定时同步[" + syncServiceName + "]开始");
		ouInfoListFromDB = getOuInfoListFromDB();
		int orgCount = ouInfoListFromDB.size();
		if (orgCount > 0) {
			// 组织增量同步
			opOrgSync(modeUpdate, false);
		} else {
			// 组织全量同步
			// opOrgSync(modeFull, false);
		}

		positionListFromDB = getPositionsFromDB();
		int posCount = positionListFromDB.size();
		if (posCount > 0) {
			// 岗位增量同步
			opPosSync(modeUpdate);
		} else {
			// 岗位全量同步
			// opPosSync(modeFull);
		}

		userInfoListFromDB = getUserInfoListFromDB();
		int userCount = userInfoListFromDB.size();
		if (userCount > 0) {
			// 用户增量同步
			opUserSync(modeUpdate, true);
		} else {
			// 用户全量同步
			// opUserSync(modeFull, true);
		}
		LOGGER.info("定时同步[" + syncServiceName + "]结束");

		// 释放内存
		ouInfoListFromDB = null;
		positionListFromDB = null;
		userInfoListFromDB = null;
	}

	/**
	 * 岗位同步 如不用同步岗位数据需用空实现覆盖此方法
	 * 
	 * @param mode
	 *            全量增量区分
	 * @throws Exception
	 */
	public void opPosSync(String mode) throws Exception {
		List<PositionModel> newList = getPositionModelList(mode);
		LOGGER.info("岗位同步[" + syncServiceName + "]Total Size: " + newList.size());

		removeExpiredPos(newList);
		if (!isPosIdProvided) {
			// 仅全量模式下执行
			if (modeFull.equals(mode)) {
				compareDataWithDB(newList, apikey);
			}
		}
		setFullPosNames(newList);

		// 全量模式
		if (modeFull.equals(mode)) {
			LOGGER.info("岗位同步[" + syncServiceName + "]新增Size: " + newList.size());
			syncAddPosOneByOne(newList);
		}
		// 增量模式
		else {
			Map<String, List<PositionModel>> map = null;
			if (isPosIdProvided) {
				map = comparePosList1(positionListFromDB, newList);
			} else {
				map = comparePosList2(positionListFromDB, newList);
			}

			List<PositionModel> posToSyncAdd = map.get(MAPKEY_POS_SYNC_ADD);
			if (posToSyncAdd != null && posToSyncAdd.size() > 0) {
				syncAddPosOneByOne(posToSyncAdd);
			}

			List<PositionModel> posToSyncUpdate = map.get(MAPKEY_POS_SYNC_UPDATE);
			if (posToSyncUpdate != null && posToSyncUpdate.size() > 0) {
				syncUpdatePosOneByOne(posToSyncUpdate);
			}
		}
	}

	/**
	 * 组织同步
	 * 
	 * @param mode
	 *            全量增量区分
	 * @param isBaseInfo
	 *            同步接口需传字段
	 * @throws Exception
	 */
	public void opOrgSync(String mode, boolean isBaseInfo) throws Exception {
		List<OuInfoModel> newList = getOuInfoModelList(mode);
		LOGGER.info("组织同步[" + syncServiceName + "]Total Size: " + newList.size());

		// 此处将需要删除的组织从集合中去除 提升同步新增效率
		List<OuInfoModel> expiredOrgs = removeExpiredOrgs(newList, mode);
		setRootOrgParentId(newList);

		// 全量模式
		if (modeFull.equals(mode)) {
			// 此处再次同步删除过期组织
			if (expiredOrgs.size() > 0) {
				LOGGER.info("组织同步[" + syncServiceName + "]删除Size: " + expiredOrgs.size());
				syncDeleteOrgOneByOne(expiredOrgs, false);
			}

			LOGGER.info("组织同步[" + syncServiceName + "]新增Size: " + newList.size());
			// 进行多次同步
			for (int i = 0; i < 5; i++) {
				syncAddOrUpdateOrgOneByOne(newList, isBaseInfo);
			}
		}
		// 增量模式
		else {
			Map<String, List<OuInfoModel>> map = compareOrgList(ouInfoListFromDB, newList);

			List<OuInfoModel> orgsToSyncDelete = map.get(MAPKEY_ORG_SYNC_DELETE);
			if (orgsToSyncDelete != null && orgsToSyncDelete.size() > 0) {
				syncDeleteOrgOneByOne(orgsToSyncDelete, true);
			}

			List<OuInfoModel> orgsToSyncAdd = map.get(MAPKEY_ORG_SYNC_ADD);
			if (orgsToSyncAdd != null && orgsToSyncAdd.size() > 0) {
				syncAddOrUpdateOrgOneByOne(orgsToSyncAdd, isBaseInfo);
			}

			List<OuInfoModel> orgsToSyncUpdate = map.get(MAPKEY_ORG_SYNC_UPDATE);
			if (orgsToSyncUpdate != null && orgsToSyncUpdate.size() > 0) {
				syncAddOrUpdateOrgOneByOne(orgsToSyncUpdate, isBaseInfo);
			}
		}
	}

	/**
	 * 用户同步
	 * 
	 * @param mode
	 *            全量增量区分
	 * @param islink
	 *            是否同步用户基本信息
	 * @throws Exception
	 */
	public void opUserSync(String mode, boolean islink) throws Exception {
		List<UserInfoModel> newList = getUserInfoModelList(mode);
		LOGGER.info("用户同步[" + syncServiceName + "]Total Size: " + newList.size());

		changePropValues(newList);
		// 此处将需要删除的用户从集合中去除 提升同步新增效率
		List<UserInfoModel> expiredUsers = removeExpiredUsers(newList, mode);
		if (!isPosIdProvided) {
			setPositionNoToUser(newList);
		}

		// 全量模式
		if (modeFull.equals(mode)) {
			// 同步禁用过期用户
			if (expiredUsers.size() > 0) {
				LOGGER.info("用户同步[" + syncServiceName + "]禁用Size: " + expiredUsers.size());
				syncDisableUserOneByOne(expiredUsers, false);
			}

			LOGGER.info("用户同步[" + syncServiceName + "]新增Size: " + newList.size());
			syncAddOrUpdateUserOneByOne(newList, islink);
		}
		// 增量模式
		else {
			// 与增量list进行比较
			Map<String, List<UserInfoModel>> map = compareUserList(userInfoListFromDB, newList);

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
		}

	}

	/**
	 * 岗位全量数据集合与最新获取岗位数据集合进行比较(已提供岗位编号)
	 * 
	 * @param fullList
	 *            全量岗位数据集合
	 * @param newList
	 *            最新获取岗位数据集合
	 * @return
	 */
	protected Map<String, List<PositionModel>> comparePosList1(List<PositionModel> fullList,
			List<PositionModel> newList) {
		Map<String, List<PositionModel>> map = new HashMap<String, List<PositionModel>>();
		List<PositionModel> posToSyncAdd = new ArrayList<PositionModel>();
		List<PositionModel> posToSyncUpdate = new ArrayList<PositionModel>();

		for (PositionModel newPos : newList) {
			// 岗位不存在新增
			if (!fullList.contains(newPos)) {
				posToSyncAdd.add(newPos);
			} else {
				String newPosNo = newPos.getpNo();
				if (newPosNo != null) {
					for (PositionModel fullPos : fullList) {
						// 根据岗位编号进行比较
						if (newPosNo.equals(fullPos.getpNo())) {
							String newPosName = newPos.getpNames();
							// 岗位名发生更新
							if (newPosName != null && !newPosName.equals(fullPos.getpNames())) {
								posToSyncUpdate.add(newPos);
							}
							break;
						}
					}
				}
			}
		}

		map.put(MAPKEY_POS_SYNC_ADD, posToSyncAdd);
		map.put(MAPKEY_POS_SYNC_UPDATE, posToSyncUpdate);
		LOGGER.info("岗位同步[" + syncServiceName + "]新增Size: " + posToSyncAdd.size());
		LOGGER.info("岗位同步[" + syncServiceName + "]更新Size: " + posToSyncUpdate.size());

		return map;
	}

	/**
	 * 岗位全量数据集合与最新获取岗位数据集合进行比较(未提供岗位编号)
	 * 
	 * @param fullList
	 *            全量岗位数据集合
	 * @param newList
	 *            最新获取岗位数据集合
	 * @return
	 */
	protected Map<String, List<PositionModel>> comparePosList2(List<PositionModel> fullList,
			List<PositionModel> newList) {
		Map<String, List<PositionModel>> map = new HashMap<String, List<PositionModel>>();
		List<PositionModel> posToSyncAdd = new ArrayList<PositionModel>();

		// 待新增岗位
		for (PositionModel newPos : newList) {
			String newPosName = newPos.getpNames();

			if (newPosName != null) {
				boolean isPosNameExist = false;

				for (PositionModel fullPos : fullList) {
					// 根据岗位名进行比较
					if (newPosName.equals(fullPos.getpNames())) {
						isPosNameExist = true;
						break;
					}
				}

				// 岗位名不存在
				if (!isPosNameExist) {
					posToSyncAdd.add(newPos);
				}
			}
		}

		map.put(MAPKEY_POS_SYNC_ADD, posToSyncAdd);
		LOGGER.info("岗位同步[" + syncServiceName + "]新增Size: " + posToSyncAdd.size());

		return map;
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
			// 待新增组织
			if (!fullList.contains(newOrg)) {
				// 非过期组织
				if (!isOrgExpired(newOrg)) {
					orgsToSyncAdd.add(newOrg);
				} else {
					// logger.info("包含过期组织：" + newOrg.getOuName());
				}
			}
			// 已经存在的组织比较
			else {
				// 组织过期待删除
				if (isOrgExpired(newOrg)) {
					orgsToSyncDelete.add(newOrg);
				} else {
					// 组织更新
					orgsToSyncUpdate.add(newOrg);
				}
			}
		}

		map.put(MAPKEY_ORG_SYNC_ADD, orgsToSyncAdd);
		map.put(MAPKEY_ORG_SYNC_UPDATE, orgsToSyncUpdate);
		map.put(MAPKEY_ORG_SYNC_DELETE, orgsToSyncDelete);

		LOGGER.info("组织同步[" + syncServiceName + "]新增Size: " + orgsToSyncAdd.size());
		LOGGER.info("组织同步[" + syncServiceName + "]更新Size: " + orgsToSyncUpdate.size());
		LOGGER.info("组织同步[" + syncServiceName + "]删除Size: " + orgsToSyncDelete.size());

		return map;
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
			// 待新增用户
			if (!fullList.contains(newUser)) {
				if (!isUserExpired(newUser)) {
					usersToSyncAdd.add(newUser);
				} else {
					// logger.info("包含过期员工：" + newUser.getID());
				}
			}
			// 已经存在的用户比较
			else {
				if (isUserExpired(newUser)) {
					// 用户过期禁用
					usersToSyncDisable.add(newUser);
				} else {
					// 存在用户更新
					usersToSyncUpdate.add(newUser);
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

	/**
	 * 根据用户数据集合生成岗位对象集合（客户未提供岗位数据仅提供包含了岗位名的人员数据时调用该方法）
	 * 
	 * @param userModelList
	 * @return
	 */
	protected List<PositionModel> getPosListFromUsers(List<UserInfoModel> userModelList) {
		// 使用Set保证无重复
		Set<String> posNames = new HashSet<String>();
		for (UserInfoModel modle : userModelList) {
			posNames.add(modle.getPostionName());
		}

		List<PositionModel> list = new ArrayList<PositionModel>(posNames.size());
		PositionModel temp = null;
		for (String posName : posNames) {
			temp = new PositionModel();
			// 随机生成岗位编号
			temp.setpNo(UUID.randomUUID().toString());
			temp.setpNames(posName);
			list.add(temp);
		}

		return list;
	}

	/**
	 * 关联岗位编号到用户（人员数据中只有岗位名没有岗位id数据）
	 * 
	 * @param newList
	 */
	protected void setPositionNoToUser(List<UserInfoModel> newList) {

		for (UserInfoModel user : newList) {
			String pNameInUser = user.getPostionName();

			if (pNameInUser != null) {
				for (PositionModel pos : positionListFromDB) {
					// 根据岗位名(不带岗位类别)进行查找
					if (pNameInUser.equals(getPositionName(pos.getpNames()))) {
						user.setPostionNo(pos.getpNo());
						break;
					}
				}
			}
		}
	}

	/**
	 * 关联数据库岗位编号到用户（人员数据中只有岗位名没有岗位id数据）
	 * 
	 * @param newList
	 * @throws SQLException
	 */
	protected void setDBPositionNoToUser(List<UserInfoModel> newList) throws SQLException {
		// 获取数据库岗位数据
		List<PositionModel> positionListDB = getPositionsFromDB();

		for (UserInfoModel user : newList) {
			String pNameInUser = user.getPostionName();

			if (pNameInUser != null) {
				for (PositionModel pos : positionListDB) {
					// 根据岗位名(不带岗位类别)进行查找
					if (pNameInUser.equals(pos.getpNames())) {
						user.setPostionNo(pos.getpNo());
						break;
					}
				}
			}
		}

		positionListDB = null;
	}

	/**
	 * 设置岗位名为带类别岗位名
	 * 
	 * @param newList
	 */
	protected void setFullPosNames(List<PositionModel> newList) {
		String prefix = POSITION_CLASS_DEFAULT + POSITION_CLASS_SEPARATOR;
		for (PositionModel pos : newList) {
			// 岗位类别名
			String pNameClass = pos.getpNameClass();
			if (StringUtils.isBlank(pNameClass)) {
				pos.setpNames(prefix + pos.getpNames());
			} else {
				pos.setpNames(pNameClass + POSITION_CLASS_SEPARATOR + pos.getpNames());
			}
		}
	}

	/**
	 * 从pNames中得到岗位名(pNames格式: 一级类别;二级类别;岗位名)
	 * 
	 * @param pNames
	 * @return
	 */
	protected String getPositionName(String pNames) {
		if (pNames == null) {
			return null;
		}

		String[] arr = pNames.split(POSITION_CLASS_SEPARATOR);
		int len = arr.length;
		if (len == 0) {
			return null;
		}

		// 最后是岗位名
		return arr[len - 1];
	}

	/**
	 * 不同组织存在同岗位名时调用该方法获取组织名作为岗位类别名(前提：1人员中关联的是岗位id，2同岗位名对应多个岗位id，3岗位数据中有所属组织id)
	 * 
	 * @param orgId
	 * @return
	 */
	protected String getPositionNameClassFromOrgs(String orgId) {
		String pNameClass = null;
		if (orgId != null) {
			for (OuInfoModel org : ouInfoListFromDB) {
				if (orgId.equals(org.getID())) {
					pNameClass = org.getOuName();
					break;
				}
			}
		}
		return pNameClass;
	}

	/**
	 * 去除过期岗位
	 * 
	 * @param list
	 */
	protected void removeExpiredPos(List<PositionModel> list) {
		for (Iterator<PositionModel> iterator = list.iterator(); iterator.hasNext();) {
			PositionModel pos = iterator.next();
			if (isPosExpired(pos)) {
				iterator.remove();
				// logger.info("删除了过期岗位：" + pos.getpNames());
			}
		}
	}

	/**
	 * 去除过期组织
	 * 
	 * @param list
	 * @param mode
	 * @return 过期组织集合
	 */
	protected List<OuInfoModel> removeExpiredOrgs(List<OuInfoModel> list, String mode) {
		List<OuInfoModel> expiredOrgs = new ArrayList<OuInfoModel>();
		// 仅全量模式下执行
		if (modeFull.equals(mode)) {
			for (Iterator<OuInfoModel> iterator = list.iterator(); iterator.hasNext();) {
				OuInfoModel org = iterator.next();
				if (isOrgExpired(org)) {
					expiredOrgs.add(org);
					iterator.remove();
					// logger.info("删除了过期组织：" + org.getOuName());
				}
			}

		}
		return expiredOrgs;
	}

	/**
	 * 删除过期员工
	 * 
	 * @param list
	 * @param mode
	 * @return 过期员工集合
	 */
	protected List<UserInfoModel> removeExpiredUsers(List<UserInfoModel> list, String mode) {
		List<UserInfoModel> expiredUsers = new ArrayList<UserInfoModel>();
		// 仅全量模式下执行
		if (modeFull.equals(mode)) {
			for (Iterator<UserInfoModel> iterator = list.iterator(); iterator.hasNext();) {
				UserInfoModel user = iterator.next();
				if (isUserExpired(user)) {
					expiredUsers.add(user);
					iterator.remove();
				}
			}
		}
		return expiredUsers;
	}

	/**
	 * 逐个岗位同步新增
	 * 
	 * @param posToSync
	 */
	protected void syncAddPosOneByOne(List<PositionModel> posToSync) {
		List<PositionModel> tempList = new ArrayList<PositionModel>();
		ResultEntity resultEntity = null;
		for (PositionModel pos : posToSync) {
			tempList.add(pos);

			try {
				resultEntity = positionService.syncPos(tempList, apikey, secretkey, baseUrl);

				if (!SYNC_CODE_SUCCESS.equals(resultEntity.getCode())) {
					printLog("岗位同步[" + syncServiceName + "]新增失败 ", pos.getpNames(), resultEntity);
				}
			} catch (IOException e) {
				LOGGER.error("岗位同步[" + syncServiceName + "]新增失败 " + pos.getpNames(), e);
			}

			tempList.clear();
		}
	}

	/**
	 * 逐个岗位同步更新
	 * 
	 * @param posToSync
	 */
	protected void syncUpdatePosOneByOne(List<PositionModel> posToSync) {
		ResultEntity resultEntity = null;
		for (PositionModel pos : posToSync) {
			try {
				// 同步岗位名不需要带分级类别
				resultEntity = positionService.changePosName(pos.getpNo(), getPositionName(pos.getpNames()), apikey,
						secretkey, baseUrl);

				if (!SYNC_CODE_SUCCESS.equals(resultEntity.getCode())) {
					printLog("岗位同步[" + syncServiceName + "]更新失败 ", pos.getpNames(), resultEntity);
				}
			} catch (IOException e) {
				LOGGER.error("岗位同步[" + syncServiceName + "]更新失败 " + pos.getpNames(), e);
			}

		}
	}

	/**
	 * 逐个组织同步新增或更新
	 * 
	 * @param orgsToSyncAddOrUpdate
	 * @param isBaseInfo
	 */
	protected void syncAddOrUpdateOrgOneByOne(List<OuInfoModel> orgsToSyncAddOrUpdate, boolean isBaseInfo) {
		List<OuInfoModel> tempList = new ArrayList<OuInfoModel>();
		ResultEntity resultEntity = null;
		for (OuInfoModel org : orgsToSyncAddOrUpdate) {
			tempList.add(org);

			try {
				resultEntity = orgService.ous(isBaseInfo, tempList, apikey, secretkey, baseUrl);
				if (SYNC_CODE_SUCCESS.equals(resultEntity.getCode())) {
					// 设置部门主管
					String managerId = org.getManagerId();
					// 亿利数据同步部门数据有不需要同步的managerId字段
					if (StringUtils.isNotBlank(managerId) && !"ElionSyncService".equals(syncServiceName)) {
						resultEntity = orgService.setManager(managerId, org.getID(), false, apikey, secretkey,
								baseUrl);
						if (!SYNC_CODE_SUCCESS.equals(resultEntity.getCode())) {
							printLog("设置部门主管[" + syncServiceName + "]失败 ", org.getOuName() + managerId, resultEntity);
						}
					}
				} else {
					printLog("组织同步[" + syncServiceName + "]失败 ", org.getOuName(), resultEntity);
				}
			} catch (IOException e) {
				LOGGER.error("组织同步[" + syncServiceName + "]失败 " + org.getOuName(), e);
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
				LOGGER.error("组织同步[" + syncServiceName + "]删除失败 " + org.getOuName(), e);
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
				LOGGER.error("用户同步[" + syncServiceName + "]失败 " + user.getID(), e);
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
				LOGGER.error("用户同步[" + syncServiceName + "]禁用失败 " + user.getID(), e);
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
				LOGGER.error("用户同步[" + syncServiceName + "]删除失败 " + user.getID(), e);
			}

			tempList.clear();
		}
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
	 * 将要同步的岗位数据和数据库中的岗位数据进行比较后替换岗位编号(已提供岗位编号不需要比较)
	 * 
	 * @param newList
	 * @param apiKey
	 * @throws SQLException
	 */
	protected void compareDataWithDB(List<PositionModel> newList, String apiKey) throws SQLException {
		List<PositionModel> positionListDB = new ArrayList<PositionModel>();
		// 获取数据库岗位数据
		PositionDao dao = new PositionDao();
		positionListDB = dao.getAllByOrgId(apiKey);

		for (PositionModel newPos : newList) {
			String newPosNames = newPos.getpNames();

			if (newPosNames != null) {
				for (PositionModel fullPos : positionListDB) {
					// 岗位名存在时将岗位编号用数据库中岗位编号替换
					if (newPosNames.equals(fullPos.getpNames())) {
						newPos.setpNo(fullPos.getpNo());
						break;
					}
				}
			}

		}

		positionListDB = null;
	}

	/**
	 * 计算得到昨天的日期
	 * 
	 * @param date
	 * @return
	 */
	protected static Date getYesterdayDate(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		// 修改为返回三天前的日期，防止出现异常造成单日数据漏同步
		c.add(Calendar.DAY_OF_MONTH, -3);
		return c.getTime();
	}

	/**
	 * 同步返回错误信息日志记录
	 * 
	 * @param type
	 * @param errKey
	 * @param resultEntity
	 */
	protected void printLog(String type, String errKey, ResultEntity resultEntity) {
		LOGGER.error(type + "ID：" + errKey + " 错误信息：" + resultEntity.getCode() + "-" + resultEntity.getMessage());
	}

	/**
	 * 从数据库获取岗位数据
	 */
	protected List<PositionModel> getPositionsFromDB() throws SQLException {
		PositionDao dao = new PositionDao();
		return dao.getAllByOrgId(apikey);
	}

	/**
	 * 从数据库获取组织数据
	 */
	protected List<OuInfoModel> getOuInfoListFromDB() throws SQLException {
		OuInfoDao dao = new OuInfoDao();
		return dao.getAllByOrgId(apikey);
	}

	/**
	 * 从数据库获取人员数据
	 */
	protected List<UserInfoModel> getUserInfoListFromDB() throws SQLException {
		UserInfoDao dao = new UserInfoDao();
		return dao.getAllByOrgId(apikey);
	}

	/**
	 * 判断组织是否过期
	 * 
	 * @param org
	 * @return
	 */
	protected abstract boolean isOrgExpired(OuInfoModel org);

	/**
	 * 判断岗位是否过期
	 * 
	 * @param pos
	 * @return
	 */
	protected abstract boolean isPosExpired(PositionModel pos);

	/**
	 * 判断用户是否过期
	 * 
	 * @param user
	 * @return
	 */
	protected abstract boolean isUserExpired(UserInfoModel user);

	/**
	 * 设置根组织的父节点id为null
	 * 
	 * @param newList
	 */
	protected abstract void setRootOrgParentId(List<OuInfoModel> newList);

	/**
	 * 根据实际情况更改属性值
	 * 
	 * @param newList
	 */
	protected abstract void changePropValues(List<UserInfoModel> newList);

	/**
	 * 获取组织对象集合
	 * 
	 * @param mode
	 *            全量增量区分
	 * @return
	 */
	protected abstract List<OuInfoModel> getOuInfoModelList(String mode) throws Exception;

	/**
	 * 获取岗位对象集合
	 * 
	 * @param mode
	 *            全量增量区分
	 * @return
	 */
	protected abstract List<PositionModel> getPositionModelList(String mode) throws Exception;

	/**
	 * 获取用户对象集合
	 * 
	 * @param mode
	 *            全量增量区分
	 * @return
	 */
	protected abstract List<UserInfoModel> getUserInfoModelList(String mode) throws Exception;

}
