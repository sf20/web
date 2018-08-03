package openDemo.entity.sync.weichuang;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * SAP组织单位json数据模型
 * 
 * @author yanl
 *
 */
public class SAPOuInfoModel {

	/**
	 * 组织单位ID
	 */
	@JsonProperty("externalCode")
	private String ID;

	/**
	 * 组织单位名称
	 */
	@JsonProperty("name")
	private String ouName;

	/**
	 * 父节点ID
	 */
	@JsonProperty("parent")
	private String parentID;

	/**
	 * 组织状态
	 */
	@JsonProperty("status")
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
