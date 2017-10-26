package openDemo.entity.sync.seeyon;

public class SeeyonPositionModel {
	private String id;
	private String name;
	private String enabled;
	private String isDeleted;
	private String pNameClass;

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

	public String getpNameClass() {
		return pNameClass;
	}

	public void setpNameClass(String pNameClass) {
		this.pNameClass = pNameClass;
	}

	// =====BeanUtils复制属性用=====
	public String getpNo() {
		return id;
	}

	public String getpNames() {
		return name;
	}

	public String getStatus() {
		return enabled;
	}

	public String getDeleteStatus() {
		return isDeleted;
	}
	// =====BeanUtils复制属性用=====

}
