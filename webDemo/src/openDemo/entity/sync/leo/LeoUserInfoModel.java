package openDemo.entity.sync.leo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 利欧公司用户json数据模型
 * 
 * @author yanl
 */
public class LeoUserInfoModel {

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
	 * 性别
	 */
	@JsonProperty("gender")
	private String sex;

	/*
	 * 移动电话
	 */
	@JsonIgnore
	private String mobile;

	/*
	 * 电子邮件
	 */
	@JsonProperty("email")
	private String mail;

	/*
	 * 部门编号
	 */
	@JsonProperty("oid_department")
	private String orgOuCode;

	/*
	 * 岗位编号
	 */
	@JsonProperty("oid_job_position")
	private String postionNo;

	/*
	 * 岗位名
	 */
	@JsonIgnore
	private String postionName;

	/*
	 * 入职日期
	 */
	@JsonProperty("entry_date")
	private String entryTime;

	/*
	 * 出生日期
	 */
	@JsonProperty("birthday")
	private String birthday;

	/*
	 * 人员状态
	 */
	@JsonProperty("status")
	private String status;

	/*
	 * 人员删除状态
	 */
	@JsonProperty("is_delete")
	private String deleteStatus;

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

	public String getDeleteStatus() {
		return deleteStatus;
	}

	public void setDeleteStatus(String deleteStatus) {
		this.deleteStatus = deleteStatus;
	}

}
