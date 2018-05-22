package openDemo.entity.sync.zhongxinjiantou;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "ESBRESP")
public class ResXML {
	private ResHeader header;
	private String data;

	@XmlElement(name = "HEADER")
	public ResHeader getHeader() {
		return header;
	}

	public void setHeader(ResHeader header) {
		this.header = header;
	}

	@XmlElement(name = "XMLDATA")
	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}
}
