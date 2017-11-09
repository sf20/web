package openDemo.entity.sync.jianlin;

import javax.xml.bind.annotation.XmlElement;

public class JianlinPositionModel {
	private String pNo;
	private String pNames;
	private String pNameClass;
	private String status;

	@XmlElement(name = "E0101")
	public String getpNo() {
		return pNo;
	}

	public void setpNo(String pNo) {
		this.pNo = pNo;
	}

	@XmlElement(name = "MC0000")
	public String getpNames() {
		return pNames;
	}

	public void setpNames(String pNames) {
		this.pNames = pNames;
	}

	public String getpNameClass() {
		return pNameClass;
	}

	public void setpNameClass(String pNameClass) {
		this.pNameClass = pNameClass;
	}

	@XmlElement(name = "Disable")
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}