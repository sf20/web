package openDemo.entity.sync.weichuang;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 微创用户json数据模型
 * 
 * @author yanl
 */
public class WeichuangUserInfoModel {

	/**
	 * 用户ID(同步必传)
	 */
	@JsonIgnore
	private String ID;

	/**
	 * 用户名(同步必传)
	 */
	@JsonProperty("EName")
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
	@JsonProperty("Mobile")
	private String mobile;

	/**
	 * 电子邮件
	 */
	@JsonProperty("email")
	private String mail;

	/**
	 * 部门编号
	 */
	@JsonProperty("DepID")
	private String orgOuCode;

	/**
	 * 岗位编号
	 */
	@JsonProperty("JobID")
	private String postionNo;

	/**
	 * 入职日期
	 */
	@JsonIgnore
	private String entryTime;

	/**
	 * 出生日期
	 */
	@JsonIgnore
	private String birthday;

	/**
	 * 人员状态
	 */
	@JsonProperty("EmpStatus")
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

}
