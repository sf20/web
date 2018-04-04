package openDemo.entity.sync.landray;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class LandrayPositionModel {
	@JsonProperty("id")
	private String pNo;

	@JsonIgnore
	private String pNameClass;

	@JsonProperty("name")
	private String pNames;

	@JsonProperty("isAvailable")
	private String status;

	// 所属部门
	@JsonProperty("parent")
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getOrgBelongsTo() {
		return orgBelongsTo;
	}

	public void setOrgBelongsTo(String orgBelongsTo) {
		this.orgBelongsTo = orgBelongsTo;
	}

}
