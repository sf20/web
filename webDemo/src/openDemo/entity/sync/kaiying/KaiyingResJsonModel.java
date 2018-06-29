package openDemo.entity.sync.kaiying;

import java.util.List;

/**
 * 恺英接口返回的json数据模型
 * 
 * @author yanl
 *
 * @param <T>
 *            组织或职位数据
 */
public class KaiyingResJsonModel<T> {
	private int code;
	private String msg;
	private List<T> data;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public List<T> getData() {
		return data;
	}

	public void setData(List<T> data) {
		this.data = data;
	}

}
