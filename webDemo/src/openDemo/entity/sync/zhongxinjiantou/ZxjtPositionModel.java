package openDemo.entity.sync.zhongxinjiantou;

import javax.xml.bind.annotation.XmlElement;

public class ZxjtPositionModel {
	private String pNo;
	private String pNames;
	private String pNameClass;
	private String status;

	@XmlElement(name = "rankid")
	public String getpNo() {
		return pNo;
	}

	public void setpNo(String pNo) {
		this.pNo = pNo;
	}

	@XmlElement(name = "rankname")
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

	@XmlElement(name = "rankstatus")
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}