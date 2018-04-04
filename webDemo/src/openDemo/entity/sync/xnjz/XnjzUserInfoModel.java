package openDemo.entity.sync.xnjz;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class XnjzUserInfoModel {
	@JsonProperty("id")
	private String ID;

	@JsonProperty("loginName")
	private String userName;

	@JsonProperty("name")
	private String cnName;

	@JsonProperty("sex")
	private String sex;

	@JsonProperty("mobileNo")
	private String mobile;

	@JsonProperty("email")
	private String mail;

	@JsonProperty("parent")
	private String orgOuCode;

	@JsonIgnore
	private String postionNo;

	@JsonProperty("posts")
	private String[] postionNoList;

	@JsonProperty("isAvailable")
	private String status;

	/**
	 * 专业
	 */
	@JsonProperty("ou_spciality")
	private String spare1;

	/**
	 * 聘任职称级别
	 */
	@JsonProperty("ou_engerpostion2")
	private String spare2;

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

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public String getOrgOuCode() {
		return orgOuCode;
	}

	public void setOrgOuCode(String orgOuCode) {
		this.orgOuCode = orgOuCode;
	}

	public String getPostionNo() {
		return postionNo;
	}

	public void setPostionNo(String postionNo) {
		this.postionNo = postionNo;
	}

	public String[] getPostionNoList() {
		return postionNoList;
	}

	public void setPostionNoList(String[] postionNoList) {
		this.postionNoList = postionNoList;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getSpare1() {
		return spare1;
	}

	public void setSpare1(String spare1) {
		this.spare1 = spare1;
	}

	public String getSpare2() {
		return spare2;
	}

	public void setSpare2(String spare2) {
		this.spare2 = spare2;
	}

}
