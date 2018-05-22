package openDemo.entity.sync.zhongxinjiantou;

import javax.xml.bind.annotation.XmlElement;

public class ZxjtPosUserMapModel {
	private String positionId;
	private String employeeIDs;

	@XmlElement(name = "positionid")
	public String getPositionId() {
		return positionId;
	}

	public void setPositionId(String positionId) {
		this.positionId = positionId;
	}

	@XmlElement(name = "employeeIDs")
	public String getEmployeeIDs() {
		return employeeIDs;
	}

	public void setEmployeeIDs(String employeeIDs) {
		this.employeeIDs = employeeIDs;
	}

}