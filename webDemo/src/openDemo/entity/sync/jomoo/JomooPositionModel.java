package openDemo.entity.sync.jomoo;

/**
 * 公司岗位数据模型
 * 
 * @author yanl
 */
public class JomooPositionModel {
	/*
	 * 岗位编号
	 */
	private String pNo;

	/*
	 * 所属部门id
	 */
	private String orgId;

	/*
	 * 岗位类别
	 */
	private String pNameClass;

	/*
	 * 岗位成员
	 */
	private String postMember;

	/*
	 * 一级类别；二级类别；岗位 (最后是岗位)
	 */
	private String pNames;

	public String getpNo() {
		return pNo;
	}

	public void setpNo(String pNo) {
		this.pNo = pNo;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getpNameClass() {
		return pNameClass;
	}

	public void setpNameClass(String pNameClass) {
		this.pNameClass = pNameClass;
	}

	public String getPostMember() {
		return postMember;
	}

	public void setPostMember(String postMember) {
		this.postMember = postMember;
	}

	public String getpNames() {
		return pNames;
	}

	public void setpNames(String pNames) {
		this.pNames = pNames;
	}

}
