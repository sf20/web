package openDemo.entity.sync.jianlin;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "HrData")
public class JianlinResDeptData {

	private List<JianlinOuInfoModel> list;

	@XmlElement(name = "Table")
	public List<JianlinOuInfoModel> getList() {
		return list;
	}

	public void setList(List<JianlinOuInfoModel> list) {
		this.list = list;
	}

}
