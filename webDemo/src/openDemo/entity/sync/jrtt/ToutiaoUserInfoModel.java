package openDemo.entity.sync.jrtt;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 用户json数据模型
 * 
 * @author yanl
 */
public class ToutiaoUserInfoModel {

	/*
	 * 用户ID(同步必传)
	 */
	@JsonProperty("id")
	private String ID;

	/*
	 * 用户名(同步必传)
	 */
	@JsonIgnore
	private String userName;

	/*
	 * 中文姓名(同步必传)
	 */
	@JsonProperty("name")
	private String cnName;

	/*
	 * 部门编号
	 */
	@JsonProperty("department_id")
	private String orgOuCode;

	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getCnName() {
		return cnName;
	}

	public void setCnName(String cnName) {
		this.cnName = cnName;
	}

	public String getOrgOuCode() {
		return orgOuCode;
	}

	public void setOrgOuCode(String orgOuCode) {
		this.orgOuCode = orgOuCode;
	}

}
