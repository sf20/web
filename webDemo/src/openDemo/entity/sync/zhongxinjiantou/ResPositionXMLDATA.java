package openDemo.entity.sync.zhongxinjiantou;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "root")
public class ResPositionXMLDATA {
	private int totalCount;
	private List<ZxjtPositionModel> items;

	@XmlElement(name = "totalCount")
	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	@XmlElementWrapper(name = "items")
	@XmlElement(name = "item")
	public List<ZxjtPositionModel> getItems() {
		return items;
	}

	public void setItems(List<ZxjtPositionModel> items) {
		this.items = items;
	}

}
