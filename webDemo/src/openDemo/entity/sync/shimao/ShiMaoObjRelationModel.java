package openDemo.entity.sync.shimao;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 世贸人员组织关系json数据模型
 * 
 * @author yanl
 *
 */
public class ShiMaoObjRelationModel {
	private int rownum;

	@JsonProperty("ObjType1")
	private String objType1;

	@JsonProperty("ObjID1")
	private String objID1;

	@JsonProperty("ObjName1")
	private String objName1;

	@JsonProperty("RelationType")
	private String relationType;

	@JsonProperty("ObjType2")
	private String objType2;

	@JsonProperty("ObjID2")
	private String objID2;

	@JsonProperty("ObjName2")
	private String objName2;

	@JsonProperty("SourceSys")
	private String sourceSys;

	public int getRownum() {
		return rownum;
	}

	public void setRownum(int rownum) {
		this.rownum = rownum;
	}

	public String getObjType1() {
		return objType1;
	}

	public void setObjType1(String objType1) {
		this.objType1 = objType1;
	}

	public String getObjID1() {
		return objID1;
	}

	public void setObjID1(String objID1) {
		this.objID1 = objID1;
	}

	public String getObjName1() {
		return objName1;
	}

	public void setObjName1(String objName1) {
		this.objName1 = objName1;
	}

	public String getRelationType() {
		return relationType;
	}

	public void setRelationType(String relationType) {
		this.relationType = relationType;
	}

	public String getObjType2() {
		return objType2;
	}

	public void setObjType2(String objType2) {
		this.objType2 = objType2;
	}

	public String getObjID2() {
		return objID2;
	}

	public void setObjID2(String objID2) {
		this.objID2 = objID2;
	}

	public String getObjName2() {
		return objName2;
	}

	public void setObjName2(String objName2) {
		this.objName2 = objName2;
	}

	public String getSourceSys() {
		return sourceSys;
	}

	public void setSourceSys(String sourceSys) {
		this.sourceSys = sourceSys;
	}

}
