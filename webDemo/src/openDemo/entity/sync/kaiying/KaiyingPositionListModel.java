package openDemo.entity.sync.kaiying;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 恺英职位列表json数据模型
 * 
 * @author yanl
 */
public class KaiyingPositionListModel {
	/**
	 * 岗位大类名称
	 */
	@JsonProperty("post_type")
	private String postType;

	/**
	 * 职位列表
	 */
	@JsonProperty("position_list")
	private List<Map<String, String>> positionList;

	public String getPostType() {
		return postType;
	}

	public void setPostType(String postType) {
		this.postType = postType;
	}

	public List<Map<String, String>> getPositionList() {
		return positionList;
	}

	public void setPositionList(List<Map<String, String>> positionList) {
		this.positionList = positionList;
	}

}
