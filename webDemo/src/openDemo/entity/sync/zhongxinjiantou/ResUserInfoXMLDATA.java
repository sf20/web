package openDemo.entity.sync.zhongxinjiantou;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "root")
public class ResUserInfoXMLDATA {
	private int totalCount;
	private List<ZxjtUserInfoModel> items;

	@XmlElement(name = "totalCount")
	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	@XmlElementWrapper(name = "items")
	@XmlElement(name = "item")
	public List<ZxjtUserInfoModel> getItems() {
		return items;
	}

	public void setItems(List<ZxjtUserInfoModel> items) {
		this.items = items;
	}

}
