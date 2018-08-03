package openDemo.entity.sync.weichuang;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * SAP岗位json数据模型
 * 
 * @author yanl
 */
public class SAPPositionModel {
	/**
	 * 岗位编号
	 */
	@JsonProperty("code")
	private String pNo;

	/**
	 * 岗位类别
	 */
	@JsonIgnore
	private String pNameClass;

	/**
	 * 一级类别；二级类别；岗位 (最后是岗位)
	 */
	@JsonProperty("externalName_zh_CN")
	private String pNames;

	/**
	 * 岗位所属部门
	 */
	@JsonProperty("department")
	private String orgBelongsTo;

	public String getpNo() {
		return pNo;
	}

	public void setpNo(String pNo) {
		this.pNo = pNo;
	}

	public String getpNameClass() {
		return pNameClass;
	}

	public void setpNameClass(String pNameClass) {
		this.pNameClass = pNameClass;
	}

	public String getpNames() {
		return pNames;
	}

	public void setpNames(String pNames) {
		this.pNames = pNames;
	}

	public String getOrgBelongsTo() {
		return orgBelongsTo;
	}

	public void setOrgBelongsTo(String orgBelongsTo) {
		this.orgBelongsTo = orgBelongsTo;
	}

}
