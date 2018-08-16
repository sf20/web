package openDemo.entity.sync.weichuang;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 嘉扬用户json数据模型
 * 
 * @author yanl
 */
public class JiaYangUserInfoModel {

	/**
	 * 用户ID(同步必传)
	 */
	@JsonIgnore
	private String ID;

	/**
	 * 用户名(同步必传)
	 */
	@JsonProperty("account")
	private String userName;

	/**
	 * 工号
	 */
	@JsonProperty("Badge")
	private String userNo;

	/**
	 * 中文姓名(同步必传)
	 */
	@JsonProperty("name")
	private String cnName;

	/**
	 * 性别
	 */
	@JsonProperty("GENDER")
	private String sex;

	/**
	 * 移动电话
	 */
	@JsonIgnore
	private String mobile;

	/**
	 * 电子邮件
	 */
	// @JsonProperty("EMAIL")
	@JsonIgnore
	private String mail;

	/**
	 * 部门编号
	 */
	@JsonProperty("depcode")
	private String orgOuCode;

	/**
	 * 所属公司组织名
	 */
	@JsonProperty("comtitle")
	private String orgOuName;

	/**
	 * 岗位编号
	 */
	@JsonProperty("jobcode")
	private String postionNo;

	/**
	 * 岗位名
	 */
	@JsonProperty("jobid")
	private String postionName;

	/**
	 * 入职日期
	 */
	@JsonProperty("joindate")
	private String entryTime;

	/**
	 * 人员状态
	 */
	@JsonProperty("STATUS")
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

	public String getUserNo() {
		return userNo;
	}

	public void setUserNo(String userNo) {
		this.userNo = userNo;
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

	public String getOrgOuName() {
		return orgOuName;
	}

	public void setOrgOuName(String orgOuName) {
		this.orgOuName = orgOuName;
	}

	public String getPostionNo() {
		return postionNo;
	}

	public void setPostionNo(String postionNo) {
		this.postionNo = postionNo;
	}

	public String getPostionName() {
		return postionName;
	}

	public void setPostionName(String postionName) {
		this.postionName = postionName;
	}

	public String getEntryTime() {
		return entryTime;
	}

	public void setEntryTime(String entryTime) {
		this.entryTime = entryTime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
