package openDemo.entity.sync.shimao;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 世贸接口返回的json数据模型
 * 
 * @author yanl
 *
 * @param <T>
 *            类型：
 */
public class ShiMaoResJsonModel<T> {
	/**
	 * 返回表结构字段 每页的详细数据
	 */
	@JsonProperty("Table")
	private List<T> dataList;

	/**
	 * 每页的详细数据
	 */
	@JsonProperty("Total")
	private List<Map<String, String>> total;

	public List<T> getDataList() {
		return dataList;
	}

	public void setDataList(List<T> dataList) {
		this.dataList = dataList;
	}

	public List<Map<String, String>> getTotal() {
		return total;
	}

	public void setTotal(List<Map<String, String>> total) {
		this.total = total;
	}

}
