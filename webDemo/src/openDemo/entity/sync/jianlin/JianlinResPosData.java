package openDemo.entity.sync.jianlin;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "HrData")
public class JianlinResPosData {

	private List<JianlinPositionModel> list;

	@XmlElement(name = "Table")
	public List<JianlinPositionModel> getList() {
		return list;
	}

	public void setList(List<JianlinPositionModel> list) {
		this.list = list;
	}

}
