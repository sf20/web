package openDemo.entity.sync.shimao;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 世贸用户json数据模型
 * 
 * @author yanl
 *
 */
public class ShiMaoUserInfoModel {

	/**
	 * 用户ID(同步必传)
	 */
	@JsonProperty("PersonNo")
	private String ID;

	/**
	 * 用户名(同步必传)
	 */
	@JsonIgnore
	private String userName;

	/**
	 * 中文姓名(同步必传)
	 */
	@JsonProperty("Name")
	private String cnName;

	/**
	 * 性别
	 */
	@JsonProperty("Gender")
	private String sex;

	/**
	 * 移动电话
	 */
	@JsonIgnore
	private String mobile;

	/**
	 * 电子邮件
	 */
	@JsonProperty("Mail")
	private String mail;

	/**
	 * 部门编号
	 */
	@JsonIgnore
	private String orgOuCode;

	/**
	 * 部门名称
	 */
	@JsonIgnore
	private String orgOuName;

	/**
	 * 岗位编号
	 */
	@JsonIgnore
	private String postionNo;

	/**
	 * 岗位编号
	 */
	@JsonIgnore
	private String postionName;

	/**
	 * 入职日期
	 */
	// TODO
	@JsonIgnore
	private String entryTime;

	/**
	 * 出生日期
	 */
	@JsonProperty("Birthday")
	private String birthday;

	/**
	 * 员工有效状态
	 */
	@JsonProperty("WorkStatus")
	private String status;

	/**
	 * 扩展字段1
	 */
	@JsonIgnore
	private String spare1;

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

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
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

}
