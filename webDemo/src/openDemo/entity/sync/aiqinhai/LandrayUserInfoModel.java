package openDemo.entity.sync.aiqinhai;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class LandrayUserInfoModel {
	@JsonProperty("id")
	private String ID;

	@JsonProperty("loginName")
	private String userName;

	@JsonProperty("name")
	private String cnName;

	/**
	 * 增加密码同步
	 */
	@JsonProperty("password")
	private String password;

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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
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

}
