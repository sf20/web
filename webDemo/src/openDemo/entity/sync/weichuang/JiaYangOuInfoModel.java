package openDemo.entity.sync.weichuang;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 嘉扬组织单位json数据模型
 * 
 * @author yanl
 *
 */
public class JiaYangOuInfoModel {

	/**
	 * 组织单位ID
	 */
	@JsonProperty("depcode")
	private String ID;

	/**
	 * 组织单位名称
	 */
	@JsonProperty("title")
	private String ouName;

	/**
	 * 父节点ID
	 */
	@JsonProperty("admincode")
	private String parentID;

	/**
	 * 所属公司部门编号
	 */
	@JsonProperty("compid")
	private String compId;

	/**
	 * 所属公司名
	 */
	@JsonProperty("comtitle")
	private String compName;

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

	public String getCompId() {
		return compId;
	}

	public void setCompId(String compId) {
		this.compId = compId;
	}

	public String getCompName() {
		return compName;
	}

	public void setCompName(String compName) {
		this.compName = compName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
