package openDemo.entity.sync.zhongxinjiantou;

import javax.xml.bind.annotation.XmlElement;

public class ZxjtOuInfoModel {
	private String ID;
	private String parentID;
	private String ouName;
	private String status;

	@XmlElement(name = "orgid")
	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

	@XmlElement(name = "parentorgid")
	public String getParentID() {
		return parentID;
	}

	public void setParentID(String parentID) {
		this.parentID = parentID;
	}

	@XmlElement(name = "orgname")
	public String getOuName() {
		return ouName;
	}

	public void setOuName(String ouName) {
		this.ouName = ouName;
	}

	@XmlElement(name = "orgstatus")
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}