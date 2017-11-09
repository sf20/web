package openDemo.service.sync.jianlin;

import java.io.StringReader;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.rpc.ServiceException;

import org.springframework.stereotype.Service;

import openDemo.entity.OuInfoModel;
import openDemo.entity.PositionModel;
import openDemo.entity.UserInfoModel;
import openDemo.entity.sync.jianlin.JianlinOuInfoModel;
import openDemo.entity.sync.jianlin.JianlinPositionModel;
import openDemo.entity.sync.jianlin.JianlinResDeptData;
import openDemo.entity.sync.jianlin.JianlinResEmpData;
import openDemo.entity.sync.jianlin.JianlinResPosData;
import openDemo.entity.sync.jianlin.JianlinUserInfoModel;
import openDemo.service.sync.AbstractSyncService2;

@Service
public class JianlinSyncService extends AbstractSyncService2 implements JianlinConfig {
	// 全量增量区分
	private static final String MODE_FULL = "0";
	private static final String MODE_UPDATE = "1";
	// 登录参数
	private static final String LOGIN_PARAM_S1 = "yunxt";
	private static final String LOGIN_PARAM_S2 = "yunxt321";
	// 用户接口要求时间格式
	private static final SimpleDateFormat CUSTOM_DATE_FORMAT = new SimpleDateFormat("yyyyMMdd");

	private WebServiceLocator locator = new WebServiceLocator();

	public JianlinSyncService() {
		super.setApikey(apikey);
		super.setSecretkey(secretkey);
		super.setBaseUrl(baseUrl);
		super.setModeFull(MODE_FULL);
		super.setModeUpdate(MODE_UPDATE);
		super.setSyncServiceName(this.getClass().getSimpleName());
	}

	private <T> List getDataModelList(String mode, Class<T> listClassType)
			throws ServiceException, RemoteException, JAXBException {
		// 调用WebService对象
		WebServiceSoap service = locator.getWebServiceSoap();
		// 调用login方法取得凭证
		String sessionID = service.login(LOGIN_PARAM_S1, LOGIN_PARAM_S2);

		// 读取数据总数
		int count = 0;
		List retList = new ArrayList();

		if (listClassType.isAssignableFrom(JianlinPositionModel.class)) {
			if (MODE_FULL.equals(mode)) {
				count = service.orgBeginGetPosition(sessionID, MODE_FULL, null);
			} else {
				count = service.orgBeginGetPosition(sessionID, MODE_UPDATE,
						CUSTOM_DATE_FORMAT.format(getYesterdayDate(new Date())));
			}

			if (count > 0) {
				// 返回xml绑定java对象
				JAXBContext context = JAXBContext.newInstance(JianlinResPosData.class);
				// 读取数据
				String segment = service.getSegment(sessionID, 1, count);

				// xml解析为java对象
				JianlinResPosData resData = (JianlinResPosData) context.createUnmarshaller()
						.unmarshal(new StringReader(segment));

				retList = resData.getList();
			}
		} else if (listClassType.isAssignableFrom(JianlinOuInfoModel.class)) {
			if (MODE_FULL.equals(mode)) {
				count = service.orgBeginGetDept(sessionID, MODE_FULL, null);
			} else {
				count = service.orgBeginGetDept(sessionID, MODE_UPDATE,
						CUSTOM_DATE_FORMAT.format(getYesterdayDate(new Date())));
			}

			if (count > 0) {
				// 返回xml绑定java对象
				JAXBContext context = JAXBContext.newInstance(JianlinResDeptData.class);
				// 读取数据
				String segment = service.getSegment(sessionID, 1, count);

				// xml解析为java对象
				JianlinResDeptData resData = (JianlinResDeptData) context.createUnmarshaller()
						.unmarshal(new StringReader(segment));

				retList = resData.getList();
			}
		} else if (listClassType.isAssignableFrom(JianlinUserInfoModel.class)) {
			if (MODE_FULL.equals(mode)) {
				count = service.empBeginGetEmployee(sessionID, MODE_FULL, null);
			} else {
				count = service.empBeginGetEmployee(sessionID, MODE_UPDATE,
						CUSTOM_DATE_FORMAT.format(getYesterdayDate(new Date())));
			}

			if (count > 0) {
				// 返回xml绑定java对象
				JAXBContext context = JAXBContext.newInstance(JianlinResEmpData.class);
				// 读取数据
				String segment = service.getSegment(sessionID, 1, count);

				// xml解析为java对象
				JianlinResEmpData resData = (JianlinResEmpData) context.createUnmarshaller()
						.unmarshal(new StringReader(segment));

				retList = resData.getList();
			}
		}

		// 清除服务器上缓存
		service.clearCache(sessionID);
		// 注销凭证
		service.logOut(sessionID);

		return retList;
	}

	@Override
	protected boolean isPosExpired(PositionModel pos) {
		// Disable 作废标志 1启用0停用
		String status = pos.getStatus();
		// 状态为0停用的场合 岗位过期
		if ("0".equals(status)) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	protected boolean isOrgExpired(OuInfoModel org) {
		// Disable 作废标志 0启用1停用
		String status = org.getStatus();
		// 状态为1停用的场合 组织过期
		if ("1".equals(status)) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	protected boolean isUserExpired(UserInfoModel user) {
		// STATUS 人员状态：担任 兼任
		String status = user.getStatus();
		// a0191 人员类别：在职人员 实习生 派遣人员 残疾人员 退休返聘 离职人员 临时工
		String deleteStatus = user.getDeleteStatus();
		// 人员状态为兼任或者人员类别为离职人员/临时工的场合 员工不同步
		if ("兼任".equals(status) || "离职人员".equals(deleteStatus) || "临时工".equals(deleteStatus)) {
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
	protected List<PositionModel> getPositionModelList(String mode) throws Exception {
		List<JianlinPositionModel> dataModelList = (List<JianlinPositionModel>) getDataModelList(mode,
				JianlinPositionModel.class);
		List<PositionModel> newList = copyCreateEntityList(dataModelList, PositionModel.class);

		return newList;
	}

	@Override
	protected List<OuInfoModel> getOuInfoModelList(String mode) throws Exception {
		List<JianlinOuInfoModel> dataModelList = (List<JianlinOuInfoModel>) getDataModelList(mode,
				JianlinOuInfoModel.class);
		List<OuInfoModel> newList = copyCreateEntityList(dataModelList, OuInfoModel.class);

		return newList;
	}

	@Override
	protected List<UserInfoModel> getUserInfoModelList(String mode) throws Exception {
		List<JianlinUserInfoModel> dataModelList = (List<JianlinUserInfoModel>) getDataModelList(mode,
				JianlinUserInfoModel.class);
		List<UserInfoModel> newList = copyCreateEntityList(dataModelList, UserInfoModel.class);

		return newList;
	}

	public static void main(String[] args) throws Exception {
		JianlinSyncService service = new JianlinSyncService();

		// List<UserInfoModel> userInfoModelList =
		// service.getUserInfoModelList(MODE_FULL);
		// service.removeExpiredUsers(userInfoModelList, MODE_FULL);
		// for (UserInfoModel user : userInfoModelList) {
		// logger.info(user.getID() + "=" + user.getUserName() + "=" +
		// user.getCnName() + "=" + user.getSex() + "="
		// + user.getMail() + "=" + user.getOrgOuCode() + "=" +
		// user.getPostionNo() + "=" + user.getStatus()
		// + "=" + user.getDeleteStatus());
		// }
		// System.out.println(userInfoModelList.size());

		// List<PositionModel> positionModelList =
		// service.getPositionModelList(MODE_FULL);
		// service.removeExpiredPos(positionModelList);
		// for (PositionModel pos : positionModelList) {
		// logger.info(pos.getpNo() + "=" + pos.getpNames() + "=" +
		// pos.getStatus());
		// }
		// System.out.println(positionModelList.size());

		// List<OuInfoModel> deptModelList =
		// service.getOuInfoModelList(MODE_FULL);
		// service.removeExpiredOrgs(deptModelList, MODE_FULL);
		// for (OuInfoModel dept : deptModelList) {
		// System.out
		// .println(dept.getID() + "=" + dept.getOuName() + "=" +
		// dept.getParentID() + "=" + dept.getStatus());
		// }
		// System.out.println(deptModelList.size());
	}
}
