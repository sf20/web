package openDemo.entity.sync.zhongxinjiantou;

import javax.xml.bind.annotation.XmlElement;

public class ResHeader {
	private String code;
	private String msg;

	@XmlElement(name = "CODE")
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@XmlElement(name = "MSG")
	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	@Override
	public String toString() {
		return "Header [code=" + code + ", msg=" + msg + "]";
	}

}
