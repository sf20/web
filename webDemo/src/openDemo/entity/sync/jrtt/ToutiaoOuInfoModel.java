package openDemo.entity.sync.jrtt;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 组织单位json数据模型
 * 
 * @author yanl
 */
public class ToutiaoOuInfoModel {

	/*
	 * 组织单位ID
	 */
	@JsonProperty("id")
	private String ID;

	/*
	 * 组织单位名称
	 */
	@JsonProperty("name")
	private String ouName;

	/*
	 * 父节点ID
	 */
	@JsonProperty("parent_id")
	private String parentID;

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

}
