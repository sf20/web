package openDemo.entity.sync.zhongxinjiantou;

import javax.xml.bind.annotation.XmlElement;

public class ZxjtUserInfoModel implements Comparable<ZxjtUserInfoModel>{
	private String ID;
	private String userName;
	private String cnName;
	private String sex;
	private String orgOuCode;
	private String postionNo;
	private String mobile;
	private String mail;
	private String entryTime;
	private String status;
	// 用户顺序
	private int weightSeq;

	@XmlElement(name = "employeeid")
	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

	// 无法自动映射 后期已手动设置
	// @XmlElement(name = "employeeid")
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@XmlElement(name = "employeename")
	public String getCnName() {
		return cnName;
	}

	public void setCnName(String cnName) {
		this.cnName = cnName;
	}

	@XmlElement(name = "gender")
	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	@XmlElement(name = "orgid")
	public String getOrgOuCode() {
		return orgOuCode;
	}

	public void setOrgOuCode(String orgOuCode) {
		this.orgOuCode = orgOuCode;
	}

	@XmlElement(name = "jobrank")
	public String getPostionNo() {
		return postionNo;
	}

	public void setPostionNo(String postionNo) {
		this.postionNo = postionNo;
	}

	@XmlElement(name = "mobilephone")
	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	@XmlElement(name = "officeemail")
	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	@XmlElement(name = "joindate")
	public String getEntryTime() {
		return entryTime;
	}

	public void setEntryTime(String entryTime) {
		this.entryTime = entryTime;
	}

	@XmlElement(name = "status")
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@XmlElement(name = "weightseq")
	public int getWeightSeq() {
		return weightSeq;
	}

	public void setWeightSeq(int weightSeq) {
		this.weightSeq = weightSeq;
	}

	@Override
	public int compareTo(ZxjtUserInfoModel o) {
		// 用户顺序相同时比较员工编号
		if (this.weightSeq - o.getWeightSeq() == 0) {
			return this.ID.compareTo(o.getID());
		} else {
			return this.weightSeq - o.getWeightSeq();
		}
	}

}