package openDemo.entity.sync.jianlin;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "HrData")
public class JianlinResEmpData {

	private List<JianlinUserInfoModel> list;

	@XmlElement(name = "Table")
	public List<JianlinUserInfoModel> getList() {
		return list;
	}

	public void setList(List<JianlinUserInfoModel> list) {
		this.list = list;
	}

}
