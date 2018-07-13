package openDemo.entity.sync.kaiying;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 恺英用户json数据模型
 * 
 * @author yanl
 */
public class KaiyingUserInfoModel {

	/**
	 * 用户ID(同步必传)
	 */
	@JsonProperty("usr_uid")
	private String ID;

	/**
	 * 用户名(同步必传)
	 */
	@JsonProperty("usr_username")
	private String userName;

	/**
	 * 中文姓名(同步必传)
	 */
	@JsonProperty("usr_cn")
	private String cnName;

	/**
	 * 性别
	 */
	@JsonIgnore
	private String sex;

	/**
	 * 移动电话
	 */
	@JsonIgnore
	private String mobile;

	/**
	 * 电子邮件
	 */
	@JsonProperty("usr_email")
	private String mail;

	/**
	 * 部门编号
	 */
	@JsonProperty("org_code")
	private String orgOuCode;

	/**
	 * 岗位编号
	 */
	@JsonIgnore
	private String postionNo;

	/**
	 * 岗位名 对应客户的职位名
	 */
	@JsonProperty("position")
	private String postionName;

	/**
	 * 入职日期
	 */
	@JsonProperty("entry_date")
	private String entryTime;

	/**
	 * 出生日期
	 */
	@JsonIgnore
	private String birthday;

	/**
	 * 人员状态
	 */
	@JsonProperty("job_status")
	private String status;

	/**
	 * 扩展字段：客户的岗位名
	 */
	@JsonProperty("post_name")
	private String spare1;

	/**
	 * 扩展字段：客户的职级
	 */
	@JsonProperty("usr_rank")
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

	public String getSpare2() {
		return spare2;
	}

	public void setSpare2(String spare2) {
		this.spare2 = spare2;
	}

}
