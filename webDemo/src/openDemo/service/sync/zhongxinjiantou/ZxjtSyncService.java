package openDemo.service.sync.zhongxinjiantou;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.xml.bind.JAXBContext;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import openDemo.entity.OuInfoModel;
import openDemo.entity.PositionModel;
import openDemo.entity.UserInfoModel;
import openDemo.entity.sync.zhongxinjiantou.ResHeader;
import openDemo.entity.sync.zhongxinjiantou.ResOuInfoXMLDATA;
import openDemo.entity.sync.zhongxinjiantou.ResPositionXMLDATA;
import openDemo.entity.sync.zhongxinjiantou.ResTokenXMLDATA;
import openDemo.entity.sync.zhongxinjiantou.ResUserInfoXMLDATA;
import openDemo.entity.sync.zhongxinjiantou.ResXML;
import openDemo.entity.sync.zhongxinjiantou.ZxjtOuInfoModel;
import openDemo.entity.sync.zhongxinjiantou.ZxjtPositionModel;
import openDemo.entity.sync.zhongxinjiantou.ZxjtUserInfoModel;
import openDemo.service.sync.AbstractSyncService;
import openDemo.utils.HttpClientUtil4Sync;

@Service
public class ZxjtSyncService extends AbstractSyncService implements ZxjtConfig {
	// 全量增量区分
	private static final String MODE_FULL = "1";
	private static final String MODE_UPDATE = "2";
	
	// 客户接口及参数
	private static final String REQUEST_URL = "https://emsbdmz.csc.com.cn/httphub";
	private static final String REQUEST_PROTOCOL = "SSL";
	private static final String REQUEST_CODE_OK = "100";
	private static final int REQUEST_PARAM_PAGESIZE = 10000;
	private static final int REQUEST_PARAM_PAGESIZE_USER = 2000;
	private static final String REQUEST_PARAM_STATUS_1 = "1";
	private static final String REQUEST_PARAM_STATUS_0 = "0";
	
	public static Logger LOGGER = LogManager.getLogger(ZxjtSyncService.class);
	// 日期格式化用
	private static final SimpleDateFormat CUSTOM_DATE_FORMAT = new SimpleDateFormat("yyyyMMddHHmmssSSS");

	public ZxjtSyncService() {
		super.setApikey(apikey);
		super.setSecretkey(secretkey);
		super.setBaseUrl(baseUrl);
		super.setModeFull(MODE_FULL);
		super.setModeUpdate(MODE_UPDATE);
		// super.setIsPosIdProvided(false);
		super.setSyncServiceName(this.getClass().getSimpleName());
	}

	@Override
	protected boolean isOrgExpired(OuInfoModel org) {
		String status = org.getStatus();
		// status状态为1有效为0无效
		if (REQUEST_PARAM_STATUS_0.equals(status)) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	protected boolean isPosExpired(PositionModel pos) {
		return false;
	}

	@Override
	protected boolean isUserExpired(UserInfoModel user) {
		String status = user.getStatus();
		// status状态为1有效为0无效
		if (REQUEST_PARAM_STATUS_0.equals(status)) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	protected void setRootOrgParentId(List<OuInfoModel> newList) {
		for (OuInfoModel org : newList) {
			// 客户数据中根组织的上级部门id为"org_0"
			if ("org_0".equals(org.getParentID())) {
				org.setParentID(null);
				break;
			}
		}
	}

	@Override
	protected void changePropValues(List<UserInfoModel> newList) {
		for (UserInfoModel tempModel : newList) {
			// 性别字符串转换 1：男 0：女
			String sex = tempModel.getSex();
			if ("1".equals(sex)) {
				tempModel.setSex("男");
			} else if ("0".equals(sex)) {
				tempModel.setSex("女");
			}

			// 邮箱修改
			String mail = tempModel.getMail();
			if (StringUtils.isNotBlank(mail)) {
				// 格式：{"officeEmail":["xxxxxxx"]}
				JSONObject jsonObject = JSONObject.fromObject(mail);
				JSONArray jsonArray = JSONArray.fromObject(jsonObject.get("officeEmail"));
				if (jsonArray != null) {
					tempModel.setMail((String) jsonArray.get(0));
				}
			}

			// 入职日期截取
			String entryTime = tempModel.getEntryTime();
			if (StringUtils.isNotBlank(entryTime)) {
				tempModel.setEntryTime(entryTime.substring(0, 10));
			}

			// userName <= ID 员工工号作为用户登录名
			tempModel.setUserName(tempModel.getID());
		}
	}

	@Override
	protected List<OuInfoModel> getOuInfoModelList(String mode) throws java.lang.Exception {
		String token = getReqToken();

		List<ZxjtOuInfoModel> dataModelList = getOuInfoModelList(token, 1, REQUEST_PARAM_PAGESIZE, REQUEST_PARAM_STATUS_1);
		// 设置部门排序
		for (ZxjtOuInfoModel ouInfoModel : dataModelList) {
			String orgSortNo = ouInfoModel.getOrgSortNo();
			if (StringUtils.isNotBlank(orgSortNo)) {
				String[] splits = orgSortNo.split("/");
				// 取最后的值
				ouInfoModel.setOrderIndex(Integer.parseInt(splits[splits.length - 1]));
			}
		}

		List<ZxjtOuInfoModel> templList = getOuInfoModelList(token, 1, REQUEST_PARAM_PAGESIZE, REQUEST_PARAM_STATUS_0);
		dataModelList.addAll(templList);

		List<OuInfoModel> newList = copyCreateEntityList(dataModelList, OuInfoModel.class);

		return newList;
	}

	@Override
	protected List<PositionModel> getPositionModelList(String mode) throws java.lang.Exception {
		String token = getReqToken();
		
		List<ZxjtPositionModel> dataModelList = getPositionModelList(token, 1, REQUEST_PARAM_PAGESIZE, REQUEST_PARAM_STATUS_1);
		List<PositionModel> newList = copyCreateEntityList(dataModelList, PositionModel.class);

		return newList;
	}

	@Override
	protected List<UserInfoModel> getUserInfoModelList(String mode) throws java.lang.Exception {
		String token = getReqToken();
		
		// stringToJava(readToString("E:\\log\\zxjt_userdata_info.txt"));
		List<ZxjtUserInfoModel> dataModelList = getUserInfoModelList(token, 1, REQUEST_PARAM_PAGESIZE_USER, REQUEST_PARAM_STATUS_1);
LOGGER.debug(dataModelList.size());
		// 设置性别
		// stringToJava(readToString("E:\\log\\zxjt_userdata_basic.txt"));
		List<ZxjtUserInfoModel> basicUserInfoModelList = getBasicUserInfoModelList(token, 1, REQUEST_PARAM_PAGESIZE_USER, REQUEST_PARAM_STATUS_1);
LOGGER.debug(dataModelList.size());
		for (ZxjtUserInfoModel userInfo : dataModelList) {
			for (ZxjtUserInfoModel userBasicInfo : basicUserInfoModelList) {
				if (userInfo.getID().equals(userBasicInfo.getID())) {
					userInfo.setSex(userBasicInfo.getSex());
					break;
				}
			}
		}
		
		// 人员排序
		Collections.sort(dataModelList);
		// 同员工岗位以总部最高职级为准
//		for (ZxjtUserInfoModel user : dataModelList) {
//			String cnName = user.getCnName();
//			if (StringUtils.isNotBlank(cnName)) {
//				for (ZxjtUserInfoModel tempUser : dataModelList) {
//					// 名字相同的员工账号
//					if (cnName.equals(tempUser.getCnName())) {
//						String tempRank = tempUser.getPostionNo();
//						if (StringUtils.isBlank(tempRank)) {
//							continue;
//						}
//						// 每次用最高职级进行比较
//						user.setPostionNo(compareGetHigherRank(user.getPostionNo(), tempRank));
//					}
//				}
//			}
//		}
		
		// 离职员工
		List<ZxjtUserInfoModel> tempList = getUserInfoModelList(token, 1, REQUEST_PARAM_PAGESIZE_USER, REQUEST_PARAM_STATUS_0);
		dataModelList.addAll(0, tempList);
		
		List<UserInfoModel> newList = copyCreateEntityList(dataModelList, UserInfoModel.class);

		return newList;
	}

	/**
	 * 同员工岗位以总部最高职级为准
	 * 
	 * @param rank
	 * @param tempRank
	 * @return
	 */
	@SuppressWarnings("unused")
	private String compareGetHigherRank(String rank, String tempRank) {
		// 默认职级为空 职级id最大
		int higherRank = Integer.MAX_VALUE;
		int rankVal = Integer.MAX_VALUE;
		int tempRankVal = Integer.MAX_VALUE;
		if (StringUtils.isNotBlank(rank)) {
			rankVal = Integer.parseInt(rank);
			// 排除职级名称：无的职级ID为1的干扰
			if (rankVal == 1) {
				// 职级名称为无的职级id设置为比职级为空的小
				rankVal = Short.MAX_VALUE;
			}
		}
		if (StringUtils.isNotBlank(tempRank)) {
			tempRankVal = Integer.parseInt(tempRank);
			// 排除职级名称：无的职级ID为1的干扰
			if (tempRankVal == 1) {
				// 职级名称为无的职级id设置为比职级为空的小
				tempRankVal = Short.MAX_VALUE;
			}
		}

		// 职级id越小越大
		higherRank = rankVal < tempRankVal ? rankVal : tempRankVal;
		if (higherRank == Integer.MAX_VALUE) {
			return null;
		} else if (higherRank == Short.MAX_VALUE) {
			return "1";
		} else {
			return String.valueOf(higherRank);
		}
	}

	/**
	 * 请求获取token
	 * 
	 * @return
	 * @throws Exception
	 */
	private String getReqToken() throws Exception{
		StringBuffer xmlStr = new StringBuffer();
		xmlStr.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>")
		.append("<ESBREQ>")
		.append("<HEADER>")
		.append("<SERVICE_CODE>s-000611S</SERVICE_CODE>")
		.append("<VERSION>1</VERSION>")
		.append("<CONSUMER_CODE>CS-1/65/S0441-307S</CONSUMER_CODE>")
		.append("<PASSWORD>ff808081543ee9f301559a97e857349a</PASSWORD>")
		.append("<MESSAGE_ID>EMSB-COS-061-getTOKEN</MESSAGE_ID>")
		.append("<DTSEND>").append(CUSTOM_DATE_FORMAT.format(new Date())).append("</DTSEND>")
		.append("<EXT1></EXT1>")
		.append("<EXT2></EXT2>")
		.append("<EXT3></EXT3>")
		.append("</HEADER>")
		.append("<XMLDATA><![CDATA[")
		.append("<root>")
		.append("<userName>elearning</userName>")
		.append("<passWord>elearning@123.com</passWord>")
		.append("</root>")
		.append("]]></XMLDATA>")
		.append("</ESBREQ>");

		String response = HttpClientUtil4Sync.doSSLPost(REQUEST_URL, REQUEST_PROTOCOL, xmlStr.toString());

		// xml解析为java对象
		ResXML resXml = (ResXML) JAXBContext.newInstance(ResXML.class).createUnmarshaller()
				.unmarshal(new StringReader(response));
		ResHeader resHeader = resXml.getHeader();
		if (!REQUEST_CODE_OK.equals(resHeader.getCode())) {
			throw new Exception("获取Token出错：" + resHeader.getMsg());
		}

		ResTokenXMLDATA resTokenData = (ResTokenXMLDATA) JAXBContext.newInstance(ResTokenXMLDATA.class)
				.createUnmarshaller().unmarshal(new StringReader(resXml.getData()));

		String token = "";
		List<String> tokens = resTokenData.getTokens();
		if (tokens.size() > 0) {
			token = tokens.get(0);
		}
System.out.println(token);
		return token;
	}
	
	private List<ZxjtOuInfoModel> getOuInfoModelList(String token, int pageNo, int pageSize, String status) throws Exception{
		StringBuffer xmlStr = new StringBuffer();
		xmlStr.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>")
		.append("<ESBREQ>")
		.append("<HEADER>")
		.append("<SERVICE_CODE>s-000236S</SERVICE_CODE>")
		.append("<VERSION>1</VERSION>")
		.append("<CONSUMER_CODE>CS-1/65/S0441-146S</CONSUMER_CODE>")
		.append("<PASSWORD>ff808081543ee9f301559a97e857036a</PASSWORD>")
		.append("<MESSAGE_ID>").append(UUID.randomUUID()).append("</MESSAGE_ID>")
		.append("<DTSEND>").append(CUSTOM_DATE_FORMAT.format(new Date())).append("</DTSEND>")
		.append("<TOKEN>").append(token).append("</TOKEN>")
		.append("<EXT1></EXT1>")
		.append("<EXT2></EXT2>")
		.append("<EXT3></EXT3>")
		.append("</HEADER>")
		.append("<XMLDATA>")
		.append("<![CDATA[")
		.append("<root>")
		.append("<pageNo>").append(pageNo).append("</pageNo>")
		.append("<pageSize>").append(pageSize).append("</pageSize>")
		.append("<status>").append(status).append("</status>")
		.append("</root>")
		.append("]]>")
		.append("</XMLDATA>")
		.append("</ESBREQ>");

		String response = HttpClientUtil4Sync.doSSLPost(REQUEST_URL, REQUEST_PROTOCOL, xmlStr.toString());
		
		ResXML resDeptXml = (ResXML) JAXBContext.newInstance(ResXML.class).createUnmarshaller()
				.unmarshal(new StringReader(response));
		ResHeader resHeader = resDeptXml.getHeader();
		if (!REQUEST_CODE_OK.equals(resHeader.getCode())) {
			throw new Exception("获取部门数据出错：" + resHeader.getMsg());
		}

		ResOuInfoXMLDATA resDeptData = (ResOuInfoXMLDATA) JAXBContext.newInstance(ResOuInfoXMLDATA.class)
				.createUnmarshaller().unmarshal(new StringReader(resDeptXml.getData()));
System.out.println("TotalCount:" + resDeptData.getTotalCount());
		
		return resDeptData.getItems();
	}
	
	private List<ZxjtPositionModel> getPositionModelList(String token, int pageNo, int pageSize, String status) throws Exception{
		StringBuffer xmlStr = new StringBuffer();
		xmlStr.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>")
		.append("<ESBREQ>")
		.append("<HEADER>")
		.append("<SERVICE_CODE>s-000264S</SERVICE_CODE>")
		.append("<VERSION>1</VERSION>")
		.append("<CONSUMER_CODE>CS-1/65/S0441-174S</CONSUMER_CODE>")
		.append("<PASSWORD>ff808081543ee9f301559a97e857064a</PASSWORD>")
		.append("<MESSAGE_ID>").append(UUID.randomUUID()).append("</MESSAGE_ID>")
		.append("<DTSEND>").append(CUSTOM_DATE_FORMAT.format(new Date())).append("</DTSEND>")
		.append("<TOKEN>").append(token).append("</TOKEN>")
		.append("<EXT1></EXT1>")
		.append("<EXT2></EXT2>")
		.append("<EXT3></EXT3>")
		.append("</HEADER>")
		.append("<XMLDATA>")
		.append("<![CDATA[")
		.append("<root>")
		.append("<pageNo>").append(pageNo).append("</pageNo>")
		.append("<pageSize>").append(pageSize).append("</pageSize>")
		.append("<status>").append(status).append("</status>")
		.append("</root>")
		.append("]]>")
		.append("</XMLDATA>")
		.append("</ESBREQ>");

		String response = HttpClientUtil4Sync.doSSLPost(REQUEST_URL, REQUEST_PROTOCOL, xmlStr.toString());
		
		ResXML resPosXml = (ResXML) JAXBContext.newInstance(ResXML.class).createUnmarshaller()
				.unmarshal(new StringReader(response));
		ResHeader resHeader = resPosXml.getHeader();
		if (!REQUEST_CODE_OK.equals(resHeader.getCode())) {
			throw new Exception("获取岗位数据出错：" + resHeader.getMsg());
		}

		ResPositionXMLDATA resPosData = (ResPositionXMLDATA) JAXBContext.newInstance(ResPositionXMLDATA.class)
				.createUnmarshaller().unmarshal(new StringReader(resPosXml.getData()));
System.out.println("TotalCount:" + resPosData.getTotalCount());

		return resPosData.getItems();
	}
	
	private List<ZxjtUserInfoModel> getUserInfoModelList(String token, int pageNo, int pageSize, String status) throws Exception{
		StringBuffer xmlStr = new StringBuffer();
		xmlStr.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>")
		.append("<ESBREQ>")
		.append("<HEADER>")
		.append("<SERVICE_CODE>s-000212S</SERVICE_CODE>")
		.append("<VERSION>1</VERSION>")
		.append("<CONSUMER_CODE>CS-1/65/S0441-122S</CONSUMER_CODE>")
		.append("<PASSWORD>ff808081543ee9f301559a97e857012a</PASSWORD>")
		.append("<MESSAGE_ID>").append(UUID.randomUUID()).append("</MESSAGE_ID>")
		.append("<DTSEND>").append(CUSTOM_DATE_FORMAT.format(new Date())).append("</DTSEND>")
		.append("<TOKEN>").append(token).append("</TOKEN>")
		.append("<EXT1></EXT1>")
		.append("<EXT2></EXT2>")
		.append("<EXT3></EXT3>")
		.append("</HEADER>")
		.append("<XMLDATA>")
		.append("<![CDATA[")
		.append("<root>")
		.append("<pageNo>").append(pageNo).append("</pageNo>")
		.append("<pageSize>").append(pageSize).append("</pageSize>")
		.append("<status>").append(status).append("</status>")
		.append("</root>")
		.append("]]>")
		.append("</XMLDATA>")
		.append("</ESBREQ>");

		String response = HttpClientUtil4Sync.doSSLPost(REQUEST_URL, REQUEST_PROTOCOL, xmlStr.toString());
		
		ResXML resPosXml = (ResXML) JAXBContext.newInstance(ResXML.class).createUnmarshaller()
				.unmarshal(new StringReader(response));
		ResHeader resHeader = resPosXml.getHeader();
		if (!REQUEST_CODE_OK.equals(resHeader.getCode())) {
			throw new Exception("获取人员数据出错：" + resHeader.getMsg());
		}

		ResUserInfoXMLDATA resUserInfoData = (ResUserInfoXMLDATA) JAXBContext.newInstance(ResUserInfoXMLDATA.class)
				.createUnmarshaller().unmarshal(new StringReader(resPosXml.getData()));

		List<ZxjtUserInfoModel> items = resUserInfoData.getItems();
		if (items != null && items.size() > 0) {
System.out.println("TotalCount1:" + resUserInfoData.getTotalCount());
			// 递归分页调用
			items.addAll(getUserInfoModelList(token, pageNo + 1, pageSize, status));
		}else{
			return new ArrayList<ZxjtUserInfoModel>();
		}

		return items;
	}
	
	private List<ZxjtUserInfoModel> getBasicUserInfoModelList(String token, int pageNo, int pageSize, String status) throws Exception{
		StringBuffer xmlStr = new StringBuffer();
		xmlStr.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>")
		.append("<ESBREQ>")
		.append("<HEADER>")
		.append("<SERVICE_CODE>s-000221S</SERVICE_CODE>")
		.append("<VERSION>1</VERSION>")
		.append("<CONSUMER_CODE>CS-1/65/S0441-131S</CONSUMER_CODE>")
		.append("<PASSWORD>ff808081543ee9f301559a97e857021a</PASSWORD>")
		.append("<MESSAGE_ID>").append(UUID.randomUUID()).append("</MESSAGE_ID>")
		.append("<DTSEND>").append(CUSTOM_DATE_FORMAT.format(new Date())).append("</DTSEND>")
		.append("<TOKEN>").append(token).append("</TOKEN>")
		.append("<EXT1></EXT1>")
		.append("<EXT2></EXT2>")
		.append("<EXT3></EXT3>")
		.append("</HEADER>")
		.append("<XMLDATA>")
		.append("<![CDATA[")
		.append("<root>")
		.append("<pageNo>").append(pageNo).append("</pageNo>")
		.append("<pageSize>").append(pageSize).append("</pageSize>")
		.append("<status>").append(status).append("</status>")
		.append("</root>")
		.append("]]>")
		.append("</XMLDATA>")
		.append("</ESBREQ>");

		String response = HttpClientUtil4Sync.doSSLPost(REQUEST_URL, REQUEST_PROTOCOL, xmlStr.toString());
		// xml非法字符替换
		response = response.replace("&", "&amp;");
		
		ResXML resPosXml = (ResXML) JAXBContext.newInstance(ResXML.class).createUnmarshaller()
				.unmarshal(new StringReader(response));
		ResHeader resHeader = resPosXml.getHeader();
		if (!REQUEST_CODE_OK.equals(resHeader.getCode())) {
			throw new Exception("获取人员基本信息数据出错：" + resHeader.getMsg());
		}

		ResUserInfoXMLDATA resUserInfoData = (ResUserInfoXMLDATA) JAXBContext.newInstance(ResUserInfoXMLDATA.class)
				.createUnmarshaller().unmarshal(new StringReader(resPosXml.getData()));

		List<ZxjtUserInfoModel> items = resUserInfoData.getItems();
		if (items != null && items.size() > 0) {
System.out.println("TotalCount2:" + resUserInfoData.getTotalCount());
			// 递归分页调用
			items.addAll(getBasicUserInfoModelList(token, pageNo + 1, pageSize, status));
		}else{
			return new ArrayList<ZxjtUserInfoModel>();
		}

		return items;
	}
	
	public static void main(String[] args) throws Exception {
//		ZxjtSyncService service = new ZxjtSyncService();
		
//		List<OuInfoModel> resDeptData = service.getOuInfoModelList(MODE_FULL);
//		System.out.println(resDeptData.size());
//		for (OuInfoModel model : resDeptData) {
//			System.out.println(
//					model.getID() + "--" + model.getOuName() + "--" + model.getParentID() + "--" + model.getStatus());
//		}

//		List<PositionModel> resPosData = service.getPositionModelList(MODE_FULL);
//		System.out.println(resPosData.size());
//		for (PositionModel model : resPosData) {
//			System.out.println("职级ID:" + model.getpNo() + "--" + "职级名称:" + model.getpNames());
//		}
		
//		List<UserInfoModel> resEmpData = service.getUserInfoModelList(MODE_FULL);
//		System.out.println(resEmpData.size());
//		for (int i = resEmpData.size() - 20; i < resEmpData.size(); i++) {
//			UserInfoModel model = resEmpData.get(i);
//			System.out.println("员工工号:" + model.getID() + "--" + "姓名:" + model.getCnName() + "--" + "邮箱:" + model.getMail()
//					+ "--" + "直属机构代码:" + model.getOrgOuCode() + "--" + "职级:" + model.getPostionNo());
//		}
//		for (UserInfoModel model : resEmpData) {
//			System.out.println(model.getID() + "--" + model.getCnName() + "--" + model.getSex() + "--"
//					+ model.getMobile() + "--" + model.getMail() + "--" + model.getOrgOuCode() + "--"
//					+ model.getPostionNo() + "--" + model.getEntryTime() + "--" + model.getStatus());
//		}
		
//		List<ZxjtUserInfoModel> readToString = stringToJava(readToString("E:\\log\\zxjt_userdata_basic.txt"));
//		System.out.println(readToString);
	}
	
	static List<ZxjtUserInfoModel> stringToJava(String xmlStr) throws Exception {  
		// xml非法字符替换
		xmlStr = xmlStr.replace("&", "&amp;");
		
		ResXML resPosXml = (ResXML) JAXBContext.newInstance(ResXML.class).createUnmarshaller()
				.unmarshal(new StringReader(xmlStr));

		ResUserInfoXMLDATA resUserInfoData = (ResUserInfoXMLDATA) JAXBContext.newInstance(ResUserInfoXMLDATA.class)
				.createUnmarshaller().unmarshal(new StringReader(resPosXml.getData()));
		
		return resUserInfoData.getItems();
	}
	
	static String readToString(String fileName) {  
        String encoding = "UTF-8";  
        File file = new File(fileName);  
        Long filelength = file.length();  
        byte[] filecontent = new byte[filelength.intValue()];  
        try {  
            FileInputStream in = new FileInputStream(file);  
            in.read(filecontent);  
            in.close();  
        } catch (FileNotFoundException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
        try {  
            return new String(filecontent, encoding);  
        } catch (UnsupportedEncodingException e) {  
            System.err.println("The OS does not support " + encoding);  
            e.printStackTrace();  
            return null;  
        }  
    }
}
