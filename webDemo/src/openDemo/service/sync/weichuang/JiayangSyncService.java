package openDemo.service.sync.weichuang;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.sf.json.JSONObject;
import openDemo.common.PrintUtil;
import openDemo.entity.OuInfoModel;
import openDemo.entity.PositionModel;
import openDemo.entity.UserInfoModel;
import openDemo.entity.sync.weichuang.WeichuangUserInfoModel;
import openDemo.service.sync.AbstractSyncService;
import openDemo.utils.HttpClientUtil4Sync;

@Service
public class JiayangSyncService extends AbstractSyncService implements JiayangConfig {
	// 参数配置
	private static final String REQUEST_SERVER_ADDRESS = "http://192.168.1.76:8004";
	private static final String REQUEST_URL_GET_TOKEN = "/KayangWebApi/Data/StartSession";
	private static final String REQUEST_URL_GET_DATA = "/KayangWebApi/Data/Getdata";
	private static final String REQUEST_URL_CLEAR_TOKEN = "/KayangWebApi/Data/CloseSession";

	// 全量增量区分
	private static final String MODE_FULL = "1";
	private static final String MODE_UPDATE = "2";
	// json解析用
	private ObjectMapper mapper;

	public JiayangSyncService() {
		super.setApikey(apikey);
		super.setSecretkey(secretkey);
		super.setBaseUrl(baseUrl);
		super.setModeFull(MODE_FULL);
		super.setModeUpdate(MODE_UPDATE);
		// super.setIsPosIdProvided(false);
		super.setSyncServiceName(this.getClass().getSimpleName());

		// 创建用于json反序列化的对象
		mapper = new ObjectMapper();
		// 忽略json中多余的属性字段
		mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
	}

	@Override
	protected boolean isOrgExpired(OuInfoModel org) {
		return false;
	}

	@Override
	protected boolean isPosExpired(PositionModel pos) {
		return false;
	}

	@Override
	protected boolean isUserExpired(UserInfoModel user) {
		String status = user.getStatus();
		// isAvailable为true有效为false无效
		if ("false".equals(status)) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	protected void setRootOrgParentId(List<OuInfoModel> newList) {
		// 部门根组织的parentId为null,无需设置
	}

	@Override
	protected void changePropValues(List<UserInfoModel> newList) {
		for (UserInfoModel tempModel : newList) {
			// 性别字符串转换1：男 2：女
			String sex = tempModel.getSex();
			if ("1".equals(sex)) {
				tempModel.setSex("男");
			} else if ("2".equals(sex)) {
				tempModel.setSex("女");
			}

			// ID <= userName 用户登录名作为用户ID
			tempModel.setID(tempModel.getUserName());
		}
	}

	@Override
	protected List<OuInfoModel> getOuInfoModelList(String mode) throws java.lang.Exception {

		return null;
	}

	@Override
	protected List<PositionModel> getPositionModelList(String mode) throws java.lang.Exception {

		return null;
	}

	@Override
	protected List<UserInfoModel> getUserInfoModelList(String mode) throws java.lang.Exception {

		return null;
	}

	private List<WeichuangUserInfoModel> getDataModelList(String mode) throws Exception {
		String requestUrl = REQUEST_SERVER_ADDRESS + REQUEST_URL_GET_DATA;

		String token = getToken();
		JSONObject jsonParams = new JSONObject();
		jsonParams.put("funcId", "1");
		jsonParams.put("dataFormat", "json");
		jsonParams.put("dataPart", "D");
		jsonParams.put("accessToken", token);

		String response = HttpClientUtil4Sync.doPost(requestUrl, jsonParams.toString());
		JsonNode jsonNode = mapper.readTree(response);
		// TODO QA
		String rowList = jsonNode.get("Result").get("Emp").get("Row").toString();

		List<WeichuangUserInfoModel> list = mapper.readValue(rowList,
				new TypeReference<List<WeichuangUserInfoModel>>() {
				});

		// 清除token
		clearToken(token);

		return list;
	}

	/**
	 * 获取AccessToken
	 * 
	 * @return
	 * @throws IOException
	 */
	private String getToken() throws IOException {
		String requestUrl = REQUEST_SERVER_ADDRESS + REQUEST_URL_GET_TOKEN;

		JSONObject jsonParams = new JSONObject();
		jsonParams.put("acc", "10758");
		jsonParams.put("pwd", "JPlCdtzhffo=");

		String response = HttpClientUtil4Sync.doPost(requestUrl, jsonParams.toString());
		JsonNode jsonNode = mapper.readTree(response);
		// Result值为token
		return jsonNode.get("Result").asText();
	}

	/**
	 * 清除AccessToken
	 * 
	 * @param token
	 * @throws IOException
	 */
	private void clearToken(String token) throws IOException {
		String requestUrl = REQUEST_SERVER_ADDRESS + REQUEST_URL_CLEAR_TOKEN;

		JSONObject jsonParams = new JSONObject();
		jsonParams.put("accessToken", token);

		String response = HttpClientUtil4Sync.doPost(requestUrl, jsonParams.toString());
		System.out.println(response);
	}

	public static void main(String[] args) throws IOException, Exception {
		JiayangSyncService service = new JiayangSyncService();
		// service.getDataModelList(null);
		ObjectMapper objectMapper = service.mapper;
		String content = "{\"MsgId\": 0,\"Msg\": \"Success(读取数据成功！)\",\"Result\": {\"Emp\": {\"Row\": [{\"EID\": 1,\"Badge\": \"000001\",\"Name\": \"嘉扬\",\"EName\": \"jiayang\",\"CompID\": 8,\"DepID\": 15,\"JobID\": 49,\"ReportTo\": null,\"wfreportto\": null,\"EmpStatus\": 1,\"JobStatus\": 1,\"EmpType\": 1,\"EmpGrade\": 1,\"EmpCustom1\": null,\"EmpCustom2\": null,\"EmpCustom3\": null,\"EmpCustom4\": null,\"EmpCustom5\": null,\"WorkCity\": 1,\"JoinTyp\": 2,\"ConTerm\": null,\"ConEndDate\": null,\"LeaveDate\": null,\"LeaveType\": null,\"LeaveReason\": null,\"Wyear_Adjust\": null,\"Cyear_Adjust\": null,\"Country\": 41,\"CertType\": 1,\"CertNo\": \"310110196001010011\",\"Gender\": 2,\"BirthDay\": \"1960-01-01T00:00:00\",\"email\": \"laidd@kayang.com\",\"Mobile\": \"13901750327\",\"office_phone\": \"021-51035100\",\"EZID\": 100,\"Remark\": null,\"Details\": {\"EmpFamily\": [{\"id\": 325,\"eid\": 1,\"badge\": \"000001\",\"Fname\": \"******\",\"relation\": 1,\"gender\": 1,\"Birthday\": \"1961-07-20T00:00:00\",\"Company\": null,\"Job\": null,\"status\": 5,\"remark\": null},{\"id\": 326,\"eid\": 1,\"badge\": \"000001\",\"Fname\": \"Test\",\"relation\": 2,\"gender\": 2,\"Birthday\": \"1961-11-29T00:00:00\",\"Company\": null,\"Job\": null,\"status\": 5,\"remark\": null},{\"id\": 327,\"eid\": 1,\"badge\": \"000001\",\"Fname\": \"@@@@@@\",\"relation\": 3,\"gender\": 2,\"Birthday\": \"1983-06-15T00:00:00\",\"Company\": null,\"Job\": null,\"status\": 1,\"remark\": null},{\"id\": 329,\"eid\": 1,\"badge\": \"000001\",\"Fname\": \"张某\",\"relation\": 1,\"gender\": 1,\"Birthday\": \"1961-07-20T00:00:00\",\"Company\": \"石油公司\",\"Job\": \"行政部主任\",\"status\": 5,\"remark\": \"aaa\"},{\"id\": 330,\"eid\": 1,\"badge\": \"000001\",\"Fname\": \"李某\",\"relation\": 2,\"gender\": 2,\"Birthday\": \"1961-11-29T00:00:00\",\"Company\": \"服装公司\",\"Job\": \"设计师\",\"status\": 5,\"remark\": \"双方各个梵蒂冈和发货\"},{\"id\": 331,\"eid\": 1,\"badge\": \"000001\",\"Fname\": \"@@@@@@\",\"relation\": 3,\"gender\": 2,\"Birthday\": \"1983-06-15T00:00:00\",\"Company\": null,\"Job\": null,\"status\": 1,\"remark\": null},{\"id\": 337,\"eid\": 1,\"badge\": \"000001\",\"Fname\": null,\"relation\": null,\"gender\": null,\"Birthday\": null,\"Company\": null,\"Job\": null,\"status\": null,\"remark\": null},{\"id\": 335,\"eid\": 1,\"badge\": \"000001\",\"Fname\": \"hh\",\"relation\": 1,\"gender\": 2,\"Birthday\": \"2016-10-19T00:00:00\",\"Company\": null,\"Job\": null,\"status\": 2,\"remark\": null},{\"id\": 336,\"eid\": 1,\"badge\": \"000001\",\"Fname\": \"嘉某某\",\"relation\": null,\"gender\": 3,\"Birthday\": null,\"Company\": null,\"Job\": null,\"status\": 2,\"remark\": null},{\"id\": 343,\"eid\": 1,\"badge\": \"000001\",\"Fname\": \"李某\",\"relation\": 2,\"gender\": 2,\"Birthday\": \"1961-11-29T00:00:00\",\"Company\": \"服装公司\",\"Job\": \"设计师\",\"status\": null,\"remark\": \"双方各个梵蒂冈和发货\"},{\"id\": 344,\"eid\": 1,\"badge\": \"000001\",\"Fname\": \"@@@@@@\",\"relation\": 3,\"gender\": 2,\"Birthday\": \"1983-06-15T00:00:00\",\"Company\": null,\"Job\": null,\"status\": 1,\"remark\": null},{\"id\": 345,\"eid\": 1,\"badge\": \"000001\",\"Fname\": \"hh\",\"relation\": 1,\"gender\": 2,\"Birthday\": \"2016-10-19T00:00:00\",\"Company\": null,\"Job\": null,\"status\": 2,\"remark\": null},{\"id\": 346,\"eid\": 1,\"badge\": \"000001\",\"Fname\": \"嘉某某\",\"relation\": null,\"gender\": 3,\"Birthday\": null,\"Company\": null,\"Job\": null,\"status\": 2,\"remark\": null}],\"EmpEdu\": [{\"ID\": 310,\"EID\": 1,\"badge\": \"000001\",\"BeginDate\": null,\"endDate\": null,\"SchoolName\": null,\"GradType\": null,\"StudyType\": null,\"EduType\": null,\"DegreeType\": null,\"DegreeName\": null,\"Major\": null,\"EduNo\": null,\"EduNoDate\": null,\"DegreeNo\": null,\"DegreeNoDate\": null,\"SchoolPlace\": null,\"Reference\": null,\"Tel\": null,\"isout\": null,\"remark\": null,\"isfulltimehigh\": null,\"EduCert\": null,\"DegreeCert\": null},{\"ID\": 607,\"EID\": 1,\"badge\": \"000001\",\"BeginDate\": \"2016-10-04T00:00:00\",\"endDate\": \"2016-10-20T00:00:00\",\"SchoolName\": \"24234\",\"GradType\": 2,\"StudyType\": 16,\"EduType\": 2,\"DegreeType\": 2,\"DegreeName\": null,\"Major\": null,\"EduNo\": null,\"EduNoDate\": null,\"DegreeNo\": null,\"DegreeNoDate\": null,\"SchoolPlace\": null,\"Reference\": null,\"Tel\": null,\"isout\": null,\"remark\": null,\"isfulltimehigh\": null,\"EduCert\": null,\"DegreeCert\": null}]}}]}}}";
		JsonNode jsonNode = objectMapper.readTree(content);
		String emps = jsonNode.get("Result").get("Emp").get("Row").toString();
		System.out.println(emps);
		List<WeichuangUserInfoModel> list = objectMapper.readValue(emps,
				new TypeReference<List<WeichuangUserInfoModel>>() {
				});
		List<UserInfoModel> userList = service.copyCreateEntityList(list, UserInfoModel.class);
		PrintUtil.printUsers(userList);
	}
}
