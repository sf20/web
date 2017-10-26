package openDemo.service.sync.elion;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.rpc.ServiceException;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFactory;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.axis.description.OperationDesc;
import org.apache.axis.description.ParameterDesc;
import org.apache.axis.message.SOAPHeaderElement;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.DOMException;

import openDemo.entity.OuInfoModel;
import openDemo.entity.PositionModel;
import openDemo.entity.UserInfoModel;
import openDemo.entity.sync.elion.EL_INT_COMMON_SYNC_REQ_TypeShape;
import openDemo.entity.sync.elion.EL_INT_DEPT_FULLSYNC_RES;
import openDemo.entity.sync.elion.EL_INT_DEPT_SYNC_RES;
import openDemo.entity.sync.elion.EL_INT_DEPT_SYNC_RESLine;
import openDemo.entity.sync.elion.EL_INT_JOBCD_SYNC_RES;
import openDemo.entity.sync.elion.EL_INT_JOBCD_SYNC_RESLine;
import openDemo.entity.sync.elion.EL_INT_PER_SYNC_RES;
import openDemo.entity.sync.elion.EL_INT_PER_SYNC_RESLine;
import openDemo.service.sync.AbstractSyncService2;

@org.springframework.stereotype.Service
public class ElionSyncService extends AbstractSyncService2 implements ElionConfig {
	// 用户接口请求参数值
	// 请求webservice的TargetEndpointAddress参数
	private static String ENDPOINT_ADDRESS = "http://ehr.elion.com.cn/PSIGW/PeopleSoftServiceListeningConnector/PSFT_HR";
	// 全量同步共通参数
	private static String FULLSYNC_REQ_ELEMENT_NAME = "EL_INT_COMMON_FULLSYNC_REQ";
	private static String FULLSYNC_REQ_ELEMENT_NAMASPACE = "http://xmlns.oracle.com/Enterprise/Tools/schemas/EL_INTERFACE.EL_INT_COMMON_FULLSYNC_REQ.V1";
	// 增量同步共通参数
	private static String SYNC_REQ_ELEMENT_NAME = "EL_INT_COMMON_SYNC_REQ";
	private static String SYNC_REQ_ELEMENT_NAMASPACE = "http://xmlns.oracle.com/Enterprise/Tools/schemas/EL_INTERFACE.EL_INT_COMMON_SYNC_REQ.V1";
	// 岗位全量同步参数
	private static String JOB_FULLSYNC_OPERATION_NAME = "EL_INT_JOBCD_FULLSYNC_OP";
	private static String JOB_FULLSYNC_SOAP_ACTION = "EL_INT_JOBCD_FULLSYNC_OP.v1";
	private static String JOB_FULLSYNC_RES_ELEMENT_NAMASPACE = "http://xmlns.oracle.com/Enterprise/Tools/schemas/EL_INT_JOBCD_FULLSYNC_RES.V1";
	// 岗位增量同步参数
	private static String JOB_SYNC_OPERATION_NAME = "EL_INT_JOBCD_SYNC_OP";
	private static String JOB_SYNC_SOAP_ACTION = "EL_INT_JOBCD_SYNC_OP.v1";
	private static String JOB_SYNC_RES_ELEMENT_NAMASPACE = "http://xmlns.oracle.com/Enterprise/Tools/schemas/EL_INT_JOBCD_SYNC_RES.V1";
	// 部门全量同步参数
	private static String DEPT_FULLSYNC_OPERATION_NAME = "EL_INT_DEPT_FULLSYNC_OP";
	private static String DEPT_FULLSYNC_SOAP_ACTION = "EL_INT_DEPT_FULLSYNC_OP.v1";
	private static String DEPT_FULLSYNC_RES_ELEMENT_NAMASPACE = "http://xmlns.oracle.com/Enterprise/Tools/schemas/EL_INT_DEPT_FULLSYNC_RES.V1";
	// 部门增量同步参数
	private static String DEPT_SYNC_OPERATION_NAME = "EL_INT_DEPT_SYNC_OP";
	private static String DEPT_SYNC_SOAP_ACTION = "EL_INT_DEPT_SYNC_OP.v1";
	private static String DEPT_SYNC_RES_ELEMENT_NAMASPACE = "http://xmlns.oracle.com/Enterprise/Tools/schemas/EL_INT_DEPT_SYNC_RES.V1";
	// 人员全量同步参数
	private static String EMP_FULLSYNC_OPERATION_NAME = "EL_INT_PER_FULLSYNC_OP";
	private static String EMP_FULLSYNC_SOAP_ACTION = "EL_INT_PER_FULLSYNC_OP.v1";
	private static String EMP_FULLSYNC_RES_ELEMENT_NAMASPACE = "http://xmlns.oracle.com/Enterprise/Tools/schemas/EL_INT_PER_FULLSYNC_RES.V1";
	// 人员增量同步参数
	private static String EMP_SYNC_OPERATION_NAME = "EL_INT_PER_SYNC_OP";
	private static String EMP_SYNC_SOAP_ACTION = "EL_INT_PER_SYNC_OP.v1";
	private static String EMP_SYNC_RES_ELEMENT_NAMASPACE = "http://xmlns.oracle.com/Enterprise/Tools/schemas/EL_INT_PER_SYNC_RES.V1";
	// 请求数据参数
	private static final String DATA_SOURCE_ESB = "99";
	private static final String DATA_FROM_INDEX = "1";
	private static final String DATA_TO_INDEX = "5000";
	private static final int PAGE_SIZE = 5000;
	// 生效状态
	private static final String EFFECTIVE_STATUS = "A";
	// 员工主岗标志
	private static final String EMPLOYEE_RECORD = "0";
	// 全量增量区分
	private static final String MODE_FULL = "1";
	private static final String MODE_UPDATE = "2";
	// 日期格式化用
	private static final SimpleDateFormat CUSTOMER_DATE_FORMAT = new SimpleDateFormat("yyyyMMdd");
	// 记录日志
	private static final Logger logger = LogManager.getLogger(ElionSyncService.class);

	public ElionSyncService() {
		super.setApikey(apikey);
		super.setSecretkey(secretkey);
		super.setBaseUrl(baseUrl);
		super.setModeFull(MODE_FULL);
		super.setModeUpdate(MODE_UPDATE);
		super.setLogger(logger);
	}

	/**
	 * 向客户接口发送请求并返回数据模型集合
	 * 
	 * @param <T>
	 * 
	 * @param mode
	 * @param classType
	 * @return
	 * @throws IOException
	 * @throws ServiceException
	 * @throws SOAPException
	 * @throws DOMException
	 */
	private <T> List<T> getDataModelList(String mode, Class<T> classType)
			throws IOException, ServiceException, DOMException, SOAPException {
		Service service = new Service();
		Call call = (Call) service.createCall();
		call.setTargetEndpointAddress(ENDPOINT_ADDRESS);

		// 设置请求参数
		EL_INT_COMMON_SYNC_REQ_TypeShape req = new EL_INT_COMMON_SYNC_REQ_TypeShape();
		req.setReqSystemID(DATA_SOURCE_ESB);
		if (MODE_FULL.equals(mode)) {
			req.setParam1(DATA_FROM_INDEX);
			req.setParam2(DATA_TO_INDEX);
		} else {
			Date today = new Date();
			req.setBeginDate(CUSTOMER_DATE_FORMAT.format(getYesterdayDate(today)));
			req.setEndDate(CUSTOMER_DATE_FORMAT.format(today));
		}

		Object[] lines = null;
		// 请求岗位数据
		if (classType.isAssignableFrom(EL_INT_JOBCD_SYNC_RESLine.class)) {
			if (MODE_FULL.equals(mode)) {
				setPropsBeforeCall(mode, call, JOB_FULLSYNC_SOAP_ACTION, JOB_FULLSYNC_OPERATION_NAME,
						JOB_FULLSYNC_RES_ELEMENT_NAMASPACE, EL_INT_COMMON_SYNC_REQ_TypeShape.class,
						EL_INT_JOBCD_SYNC_RES.class);
			} else {
				setPropsBeforeCall(mode, call, JOB_SYNC_SOAP_ACTION, JOB_SYNC_OPERATION_NAME,
						JOB_SYNC_RES_ELEMENT_NAMASPACE, EL_INT_COMMON_SYNC_REQ_TypeShape.class,
						EL_INT_JOBCD_SYNC_RES.class);
			}
			EL_INT_JOBCD_SYNC_RES res = (EL_INT_JOBCD_SYNC_RES) call.invoke(new java.lang.Object[] { req });
			lines = res.getLine();
		}
		// 请求部门数据
		else if (classType.isAssignableFrom(EL_INT_DEPT_SYNC_RESLine.class)) {
			if (MODE_FULL.equals(mode)) {
				setPropsBeforeCall(mode, call, DEPT_FULLSYNC_SOAP_ACTION, DEPT_FULLSYNC_OPERATION_NAME,
						DEPT_FULLSYNC_RES_ELEMENT_NAMASPACE, EL_INT_COMMON_SYNC_REQ_TypeShape.class,
						EL_INT_DEPT_FULLSYNC_RES.class);

				EL_INT_DEPT_FULLSYNC_RES res = (EL_INT_DEPT_FULLSYNC_RES) call.invoke(new java.lang.Object[] { req });
				lines = res.getLine();
			} else {
				setPropsBeforeCall(mode, call, DEPT_SYNC_SOAP_ACTION, DEPT_SYNC_OPERATION_NAME,
						DEPT_SYNC_RES_ELEMENT_NAMASPACE, EL_INT_COMMON_SYNC_REQ_TypeShape.class,
						EL_INT_DEPT_SYNC_RES.class);

				EL_INT_DEPT_SYNC_RES res = (EL_INT_DEPT_SYNC_RES) call.invoke(new java.lang.Object[] { req });
				lines = res.getLine();
			}
		}
		// 请求人员数据
		else if (classType.isAssignableFrom(EL_INT_PER_SYNC_RESLine.class)) {
			if (MODE_FULL.equals(mode)) {
				setPropsBeforeCall(mode, call, EMP_FULLSYNC_SOAP_ACTION, EMP_FULLSYNC_OPERATION_NAME,
						EMP_FULLSYNC_RES_ELEMENT_NAMASPACE, EL_INT_COMMON_SYNC_REQ_TypeShape.class,
						EL_INT_PER_SYNC_RES.class);
			} else {
				setPropsBeforeCall(mode, call, EMP_SYNC_SOAP_ACTION, EMP_SYNC_OPERATION_NAME,
						EMP_SYNC_RES_ELEMENT_NAMASPACE, EL_INT_COMMON_SYNC_REQ_TypeShape.class,
						EL_INT_PER_SYNC_RES.class);
			}

			EL_INT_PER_SYNC_RES res = (EL_INT_PER_SYNC_RES) call.invoke(new java.lang.Object[] { req });
			lines = res.getLine();

			// 人员数据较多进行多次获取
			int i = 1;
			Object[] tempLines = lines;
			while (tempLines != null) {
				req.setParam1(String.valueOf(PAGE_SIZE * i + 1));
				req.setParam2(String.valueOf(PAGE_SIZE * (i + 1)));

				res = (EL_INT_PER_SYNC_RES) call.invoke(new java.lang.Object[] { req });
				tempLines = res.getLine();
				// 数组合并
				lines = ArrayUtils.addAll(lines, tempLines);
				i++;
			}
		}

		List<T> tempList = new ArrayList<T>();
		if (lines != null && lines.length > 0) {
			tempList = (List<T>) Arrays.asList(lines);
		} else {
			logger.info("获取客户接口[" + classType.getSimpleName() + "]数据为空！");
		}
		return tempList;
	}

	/**
	 * 请求客户接口前设置请求属性
	 * 
	 * @param mode
	 *            全量/增量类型
	 * @param call
	 *            axis中call对象
	 * @param soapAction
	 *            请求属性SOAPAction属性值
	 * @param operationName
	 *            请求webservice的操作名
	 * @param resElementNamaspace
	 *            返回xml中对象命名空间属性值
	 * @param reqClassType
	 *            请求xml对应java类
	 * @param resClassType
	 *            返回xml对应java类
	 * @throws SOAPException
	 * @throws DOMException
	 */
	private <E, T> void setPropsBeforeCall(String mode, Call call, String soapAction, String operationName,
			String resElementNamaspace, Class<E> reqClassType, Class<T> resClassType)
			throws DOMException, SOAPException {
		// 设置共通参数
		String reqElementNamaspace = null;
		String reqElement = null;
		if (MODE_FULL.equals(mode)) {
			reqElement = FULLSYNC_REQ_ELEMENT_NAME;
			reqElementNamaspace = FULLSYNC_REQ_ELEMENT_NAMASPACE;
		} else {
			reqElement = SYNC_REQ_ELEMENT_NAME;
			reqElementNamaspace = SYNC_REQ_ELEMENT_NAMASPACE;
		}
		// 设置OperationDesc
		OperationDesc oper = new OperationDesc();
		oper.setName(operationName);
		ParameterDesc param = new ParameterDesc(new QName(reqElementNamaspace, reqElement), ParameterDesc.IN,
				new QName(reqElementNamaspace, reqClassType.getSimpleName()), reqClassType, false, false);
		oper.addParameter(param);
		oper.setReturnType(new QName(resElementNamaspace, resClassType.getSimpleName()));
		oper.setReturnClass(resClassType);
		oper.setReturnQName(new QName(resElementNamaspace, resClassType.getSimpleName()));
		oper.setStyle(org.apache.axis.constants.Style.DOCUMENT);
		oper.setUse(org.apache.axis.constants.Use.LITERAL);
		// 设置call参数值
		call.setOperation(oper);
		call.setUseSOAPAction(true);
		call.setSOAPActionURI(soapAction);
		call.setEncodingStyle(null);
		call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
		call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
		call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
		call.setOperationName(new QName("", operationName));

		// 请求信息带用户名密码
		addSecurityAuth(call);
	}

	/**
	 * 请求xml的header标签中增加安全认证信息
	 * 
	 * @param call
	 * @throws DOMException
	 * @throws SOAPException
	 */
	private void addSecurityAuth(Call call) throws DOMException, SOAPException {
		String AUTH_PREFIX = "wsse";
		String AUTH_NS = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd";
		SOAPHeaderElement soapHeaderElement = null;

		SOAPFactory soapFactory = SOAPFactory.newInstance();
		SOAPElement wsSecHeaderElm = soapFactory.createElement("Security", AUTH_PREFIX, AUTH_NS);
		SOAPElement userNameTokenElm = soapFactory.createElement("UsernameToken", AUTH_PREFIX, AUTH_NS);
		SOAPElement userNameElm = soapFactory.createElement("Username", AUTH_PREFIX, AUTH_NS);
		SOAPElement passwdElm = soapFactory.createElement("Password", AUTH_PREFIX, AUTH_NS);
		passwdElm.setAttribute("Type",
				"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#PasswordText");

		userNameElm.addTextNode("EL_INTERFACE");
		passwdElm.addTextNode("interface");

		userNameTokenElm.addChildElement(userNameElm);
		userNameTokenElm.addChildElement(passwdElm);
		wsSecHeaderElm.addChildElement(userNameTokenElm);
		soapHeaderElement = new SOAPHeaderElement(wsSecHeaderElm);
		soapHeaderElement.setMustUnderstand(true);
		soapHeaderElement.setActor(null);

		call.addHeader(soapHeaderElement);
	}

	/**
	 * 覆盖父类中的方法
	 * 
	 * @param newList
	 */
	@Override
	protected void setFullPosNames(List<PositionModel> newList) {
		String prefix = POSITION_CLASS_DEFAULT + POSITION_CLASS_SEPARATOR;
		for (PositionModel pos : newList) {
			String pNameClass = pos.getpNameClass();
			if (StringUtils.isBlank(pNameClass)) {
				pos.setpNames(prefix + pos.getpNames());
			} else {
				// 替换岗位类别名中的特殊字符
				pNameClass = pNameClass.replaceAll("&amp;", "&");
				pos.setpNames(pNameClass + POSITION_CLASS_SEPARATOR + pos.getpNames());
			}
		}
	}

	@Override
	protected boolean isPosExpired(PositionModel pos) {
		String status = pos.getStatus();
		// 状态为非生效的场合 岗位过期
		if (!EFFECTIVE_STATUS.equals(status)) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	protected boolean isOrgExpired(OuInfoModel org) {
		String status = org.getStatus();
		// 状态为非生效的场合 组织过期
		if (!EFFECTIVE_STATUS.equals(status)) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	protected boolean isUserExpired(UserInfoModel user) {
		String userName = user.getUserName();
		String status = user.getStatus();
		// 该字段在请求到客户接口数据时已关联EmployeeRecord字段
		String deleteStatus = user.getDeleteStatus();
		String expireDate = user.getExpireDate();
		// UserName为空或者用户状态为非生效或者非主岗或者已经离职的场合下过期
		if (StringUtils.isBlank(userName) || !EFFECTIVE_STATUS.equals(status) || !EMPLOYEE_RECORD.equals(deleteStatus)
				|| StringUtils.isNotEmpty(expireDate)) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	protected void setRootOrgParentId(List<OuInfoModel> newList) {
		for (OuInfoModel org : newList) {
			// 客户数据中根组织的上级部门id为""
			if ("".equals(org.getParentID())) {
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
				try {
					tempModel.setEntryTime(DATE_FORMAT.format(CUSTOMER_DATE_FORMAT.parse(entryTime)));
				} catch (ParseException e) {
					logger.warn("日期格式有误 " + tempModel.getID() + "：" + entryTime);
				}
			}

			// 出生日期修改
			String birthday = tempModel.getBirthday();
			if (StringUtils.isNotEmpty(birthday)) {
				try {
					tempModel.setBirthday(DATE_FORMAT.format(CUSTOMER_DATE_FORMAT.parse(birthday)));
				} catch (ParseException e) {
					logger.warn("日期格式有误 " + tempModel.getID() + "：" + birthday);
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
	protected List<PositionModel> getPositionModelList(String mode) throws Exception {
		List<EL_INT_JOBCD_SYNC_RESLine> modelList = getDataModelList(mode, EL_INT_JOBCD_SYNC_RESLine.class);
		List<PositionModel> newList = copyCreateEntityList(modelList, PositionModel.class);

		return newList;
	}

	@Override
	protected List<OuInfoModel> getOuInfoModelList(String mode) throws Exception {
		List<EL_INT_DEPT_SYNC_RESLine> modelList = getDataModelList(mode, EL_INT_DEPT_SYNC_RESLine.class);
		List<OuInfoModel> newList = copyCreateEntityList(modelList, OuInfoModel.class);

		return newList;
	}

	@Override
	protected List<UserInfoModel> getUserInfoModelList(String mode) throws Exception {
		List<EL_INT_PER_SYNC_RESLine> modelList = getDataModelList(mode, EL_INT_PER_SYNC_RESLine.class);
		List<UserInfoModel> newList = copyCreateEntityList(modelList, UserInfoModel.class);

		return newList;
	}

}
