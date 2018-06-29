package openDemo.entity.sync.kaiying;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 恺英组织单位json数据模型
 * 
 * @author yanl
 */
public class KaiyingOuInfoModel {

	/*
	 * 组织单位ID
	 */
	@JsonProperty("org_code")
	private String ID;

	/*
	 * 组织单位名称
	 */
	@JsonProperty("org_name")
	private String ouName;

	/*
	 * 父节点ID
	 */
	@JsonProperty("p_org_code")
	private String parentID;

	/*
	 * 组织状态
	 */
	@JsonProperty("org_statue")
	private String status;

	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

	public String getOuName() {
		return ouName;
	}

	public void setOuName(String ouName) {
		this.ouName = ouName;
	}

	public String getParentID() {
		return parentID;
	}

	public void setParentID(String parentID) {
		this.parentID = parentID;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
