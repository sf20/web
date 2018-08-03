package openDemo.entity.sync.weichuang;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * SAP接口返回的json数据模型
 * 
 * @author yanl
 *
 * @param <T>
 */
public class SAPResJsonModel<T> {
	@JsonProperty("d")
	private SAPResData<T> data;

	public SAPResData<T> getData() {
		return data;
	}

	public void setData(SAPResData<T> data) {
		this.data = data;
	}

}
