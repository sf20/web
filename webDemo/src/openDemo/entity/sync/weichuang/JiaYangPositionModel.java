package openDemo.entity.sync.weichuang;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 嘉扬岗位json数据模型
 * 
 * @author yanl
 */
public class JiaYangPositionModel {
	/**
	 * 岗位编号
	 */
	@JsonProperty("jobcode")
	private String pNo;

	/**
	 * 岗位类别
	 */
	@JsonIgnore
	private String pNameClass;

	/**
	 * 一级类别；二级类别；岗位 (最后是岗位)
	 */
	@JsonProperty("jobid")
	private String pNames;

	/**
	 * 所属公司编号
	 */
	@JsonProperty("compid")
	private String compBelongsTo;

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

	public String getCompBelongsTo() {
		return compBelongsTo;
	}

	public void setCompBelongsTo(String compBelongsTo) {
		this.compBelongsTo = compBelongsTo;
	}

}
