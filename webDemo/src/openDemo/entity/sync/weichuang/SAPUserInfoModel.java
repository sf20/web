package openDemo.entity.sync.weichuang;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * SAP公司用户json数据模型
 * 
 * @author yanl
 *
 */
public class SAPUserInfoModel {

	/**
	 * 用户ID(同步必传)
	 */
	@JsonProperty("userId")
	private String ID;

	/**
	 * 用户名(同步必传)
	 */
	@JsonProperty("personIdExternal")
	private String userName;

	/**
	 * 工号
	 */
	@JsonProperty("customString4")
	private String userNo;

	/**
	 * 中文姓名(同步必传)
	 */
	@JsonProperty("formalName")
	private String cnName;

	/**
	 * 性别
	 */
	@JsonProperty("gender")
	private String sex;

	/**
	 * 移动电话
	 */
	// @JsonProperty("phoneNumber")
	private String mobile;

	/**
	 * 电子邮件
	 */
	// @JsonProperty("emailAddress")
	private String mail;

	/**
	 * 部门编号
	 */
	@JsonProperty("department")
	private String orgOuCode;

	/**
	 * 岗位编号
	 */
	@JsonProperty("position")
	private String postionNo;

	/**
	 * 入职日期
	 */
	@JsonProperty("companyEntryDate")
	private String entryTime;

	/**
	 * 员工状态
	 */
	@JsonProperty("emplStatus")
	private String status;

	/**
	 * 扩展字段 1~10
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

	public String getPostionNo() {
		return postionNo;
	}

	public void setPostionNo(String postionNo) {
		this.postionNo = postionNo;
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
