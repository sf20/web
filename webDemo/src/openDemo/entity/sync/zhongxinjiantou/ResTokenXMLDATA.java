package openDemo.entity.sync.zhongxinjiantou;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "root")
public class ResTokenXMLDATA {
	private List<String> tokens;

	/**
	 * XmlElementWrapper注解只能用于集合属性值
	 * 
	 * @return
	 */
	@XmlElementWrapper(name = "reskey")
	@XmlElement(name = "mySign")
	public List<String> getTokens() {
		return tokens;
	}

	public void setTokens(List<String> tokens) {
		this.tokens = tokens;
	}
}
