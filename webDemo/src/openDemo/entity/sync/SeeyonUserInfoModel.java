package openDemo.entity.sync;

public class SeeyonUserInfoModel {
	private String id;
	private String name;
	private String loginName;
	private String gender;
	private String telNumber;
	private String emailAddress;
	private String orgDepartmentId;
	private String orgPostId;
	private String createTime;
	private String birthday;
	private String enabled;
	private String state;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getTelNumber() {
		return telNumber;
	}

	public void setTelNumber(String telNumber) {
		this.telNumber = telNumber;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public String getOrgDepartmentId() {
		return orgDepartmentId;
	}

	public void setOrgDepartmentId(String orgDepartmentId) {
		this.orgDepartmentId = orgDepartmentId;
	}

	public String getOrgPostId() {
		return orgPostId;
	}

	public void setOrgPostId(String orgPostId) {
		this.orgPostId = orgPostId;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getEnabled() {
		return enabled;
	}

	public void setEnabled(String enabled) {
		this.enabled = enabled;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	// =====BeanUtils复制属性用=====
	public String getID() {
		return id;
	}

	public String getUserName() {
		return loginName;
	}

	public String getCnName() {
		return name;
	}

	public String getSex() {
		return gender;
	}

	public String getMobile() {
		return null;// 手机不同步
	}

	public String getMail() {
		return emailAddress;
	}

	public String getOrgOuCode() {
		return orgDepartmentId;
	}

	public String getPostionNo() {
		return orgPostId;
	}

	public String getEntryTime() {
		return createTime;
	}

	public String getStatus() {
		return enabled;
	}

	public String getDeleteStatus() {
		return state;
	}
	// =====BeanUtils复制属性用=====

}
