package openDemo.entity.sync.leo;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 利欧公司岗位json数据模型
 * 
 * @author yanl
 */
public class LeoPositionModel {
	/*
	 * 岗位编号
	 */
	@JsonProperty("oid")
	private String pNo;

	/*
	 * 岗位类别
	 */
	@JsonProperty("code")
	private String pNameClass;

	/*
	 * 一级类别；二级类别；岗位 (最后是岗位)
	 */
	@JsonProperty("name")
	private String pNames;

	/*
	 * 岗位状态
	 */
	@JsonProperty("status")
	private String status;

	/*
	 * 岗位删除状态
	 */
	@JsonProperty("is_delete")
	private String deleteStatus;

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

}
