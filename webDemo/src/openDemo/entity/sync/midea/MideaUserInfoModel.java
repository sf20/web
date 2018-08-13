package openDemo.entity.sync.midea;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 美的物业用户json数据模型
 * 
 * @author yanl
 *
 */
public class MideaUserInfoModel {

	/**
	 * 用户ID(同步必传)
	 */
	@JsonProperty("uid")
	private String ID;

	/**
	 * 用户名(同步必传)
	 */
	@JsonIgnore
	private String userName;

	/**
	 * 中文姓名(同步必传)
	 */
	@JsonProperty("cn")
	private String cnName;

	/**
	 * 工号
	 */
	@JsonProperty("employeeNumber")
	private String userNo;

	/**
	 * 性别
	 */
	@JsonProperty("smart-gender")
	private String sex;

	/**
	 * 移动电话
	 */
	@JsonProperty("mobile")
	private String mobile;

	/**
	 * 电子邮件
	 */
	@JsonProperty("mail")
	private String mail;

	/**
	 * 部门编号
	 */
	@JsonProperty("departmentNumber")
	private String orgOuCode;

	/**
	 * 岗位编号
	 */
	@JsonIgnore
	private String postionNo;

	/**
	 * 岗位名
	 */
	@JsonProperty("customized-positionname")
	private String postionName;

	/**
	 * 入职日期
	 */
	// @JsonProperty("customized-joinsysdate")
	@JsonProperty("customized-hiredate")
	private String entryTime;

	/**
	 * 员工状态
	 */
	@JsonProperty("smart-status")
	private String status;

	/**
	 * 扩展字段：职级
	 */
	@JsonProperty("customized-postionattr")
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

	public String getUserNo() {
		return userNo;
	}

	public void setUserNo(String userNo) {
		this.userNo = userNo;
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
