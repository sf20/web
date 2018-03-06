package openDemo.entity.sync.xnjz;

import com.fasterxml.jackson.annotation.JsonProperty;

public class XnjzOuInfoModel {
	@JsonProperty("id")
	private String ID;

	@JsonProperty("name")
	private String ouName;

	@JsonProperty("parent")
	private String parentID;

	@JsonProperty("isAvailable")
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
