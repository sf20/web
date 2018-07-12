package openDemo.entity.sync.meilitianyuan;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 美丽田园用户json数据模型
 * 
 * @author yanl
 *
 */
public class MeiliTianyuanUserInfoModel {

	/**
	 * 用户ID(同步必传)
	 */
	@JsonProperty("UserAccount")
	private String ID;

	/**
	 * 用户名(同步必传)
	 */
	@JsonIgnore
	private String userName;

	/**
	 * 中文姓名(同步必传)
	 */
	@JsonProperty("UserName")
	private String cnName;

	/**
	 * 性别
	 */
	@JsonProperty("sex")
	private String sex;

	/**
	 * 移动电话
	 */
	@JsonProperty("Mobile")
	private String mobile;

	/**
	 * 电子邮件
	 */
	@JsonProperty("Email")
	private String mail;

	/**
	 * 部门编号
	 */
	@JsonProperty("DepartmentID")
	private String orgOuCode;

	/**
	 * 岗位编号
	 */
	// @JsonProperty("UserTitle")
	@JsonIgnore
	private String postionNo;

	/**
	 * 岗位名
	 */
	@JsonProperty("UserTitleName")
	private String postionName;

	/**
	 * 入职日期
	 */
	@JsonProperty("EntryTime")
	private String entryTime;

	/**
	 * 员工性质：1：美田员工，2：加盟商，3：贝黎诗
	 */
	@JsonProperty("IsInsiders")
	private String status;

	/**
	 * 员工状态：1：离职，２：在职，３：脱岗
	 */
	@JsonProperty("EmployeeStatus")
	private String deleteStatus;

	/**
	 * 扩展字段：岗位等级
	 */
	@JsonIgnore
	private String spare1;

	/**
	 * 扩展字段：上级领导
	 */
	@JsonProperty("ManagerName")
	private String spare2;

	/**
	 * 扩展字段：专业等级
	 */
	@JsonProperty("MajorLevelName")
	private String spare3;

	/**
	 * 扩展字段：美容等级
	 */
	@JsonProperty("cosLeveName")
	private String spare4;

	/**
	 * 扩展字段：美体等级
	 */
	@JsonProperty("bodLevelName")
	private String spare5;

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

	public String getSpare3() {
		return spare3;
	}

	public void setSpare3(String spare3) {
		this.spare3 = spare3;
	}

	public String getSpare4() {
		return spare4;
	}

	public void setSpare4(String spare4) {
		this.spare4 = spare4;
	}

	public String getSpare5() {
		return spare5;
	}

	public void setSpare5(String spare5) {
		this.spare5 = spare5;
	}

}
