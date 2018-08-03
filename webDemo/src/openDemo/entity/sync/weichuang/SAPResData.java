package openDemo.entity.sync.weichuang;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 返回json数据中d对应的模型对象
 * 
 * @author yanl
 *
 */
public class SAPResData<T> {
	private List<T> results;
	@JsonProperty("__next")
	private String nextPageUrl;

	public List<T> getResults() {
		return results;
	}

	public void setResults(List<T> results) {
		this.results = results;
	}

	public String getNextPageUrl() {
		return nextPageUrl;
	}

	public void setNextPageUrl(String nextPageUrl) {
		this.nextPageUrl = nextPageUrl;
	}

}
