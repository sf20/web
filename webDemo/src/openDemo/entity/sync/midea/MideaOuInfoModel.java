package openDemo.entity.sync.midea;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 美的物业组织单位json数据模型
 * 
 * @author yanl
 *
 */
public class MideaOuInfoModel {

	/**
	 * 组织单位ID
	 */
	@JsonProperty("o")
	private String ID;

	/**
	 * 组织单位名称
	 */
	@JsonProperty("smart-shortname")
	private String ouName;

	/**
	 * 父节点ID
	 */
	@JsonProperty("smart-parentid")
	private String parentID;

	/**
	 * 组织状态
	 */
	@JsonIgnore
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
