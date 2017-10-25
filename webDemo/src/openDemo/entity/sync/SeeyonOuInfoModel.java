package openDemo.entity.sync;

public class SeeyonOuInfoModel {
	private String orgAccountId;
	private String id;
	private String name;
	private String superior;
	private String enabled;
	private String isDeleted;

	public String getOrgAccountId() {
		return orgAccountId;
	}

	public void setOrgAccountId(String orgAccountId) {
		this.orgAccountId = orgAccountId;
	}

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

	public String getSuperior() {
		return superior;
	}

	public void setSuperior(String superior) {
		this.superior = superior;
	}

	public String getEnabled() {
		return enabled;
	}

	public void setEnabled(String enabled) {
		this.enabled = enabled;
	}

	public String getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(String isDeleted) {
		this.isDeleted = isDeleted;
	}

	// =====BeanUtils复制属性用=====
	public String getID() {
		return id;
	}

	public String getOuName() {
		return name;
	}

	public String getParentID() {
		return superior;
	}

	public String getStatus() {
		return enabled;
	}

	public String getDeleteStatus() {
		return isDeleted;
	}
	// =====BeanUtils复制属性用=====

}
