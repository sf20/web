package openDemo.entity.sync.midea;

import java.util.List;
import java.util.Map;

/**
 * 美的物业接口返回的json数据模型
 * 
 * @author yanl
 *
 * @param <T>
 *            body类型：员工类型或组织类型
 */
public class MideaResJsonModel<T> {
	private Map<String, String> head;
	private List<T> body;

	public Map<String, String> getHead() {
		return head;
	}

	public void setHead(Map<String, String> head) {
		this.head = head;
	}

	public List<T> getBody() {
		return body;
	}

	public void setBody(List<T> body) {
		this.body = body;
	}

}
