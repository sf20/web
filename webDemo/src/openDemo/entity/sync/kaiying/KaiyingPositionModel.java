package openDemo.entity.sync.kaiying;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 恺英岗位json数据模型
 * 
 * @author yanl
 */
public class KaiyingPositionModel {
	/*
	 * 岗位编号
	 */
	@JsonProperty("post_id")
	private String pNo;

	/*
	 * 岗位类别
	 */
	@JsonProperty("post_type")
	private String pNameClass;

	/*
	 * 一级类别；二级类别；岗位 (最后是岗位)
	 */
	@JsonProperty("post_name")
	private String pNames;

	public String getpNo() {
		return pNo;
	}

	public void setpNo(String pNo) {
		this.pNo = pNo;
	}

	public String getpNameClass() {
		return pNameClass;
	}

	public void setpNameClass(String pNameClass) {
		this.pNameClass = pNameClass;
	}

	public String getpNames() {
		return pNames;
	}

	public void setpNames(String pNames) {
		this.pNames = pNames;
	}

}
