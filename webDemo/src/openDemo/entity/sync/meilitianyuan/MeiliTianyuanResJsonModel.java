package openDemo.entity.sync.meilitianyuan;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 美丽田园接口返回的json数据模型
 * 
 * @author yanl
 *
 */
public class MeiliTianyuanResJsonModel {
	private int currentPage;
	private int pageSize;
	private int totalPage;
	private int totalRow;
	@JsonProperty("UserList")
	private List<MeiliTianyuanUserInfoModel> UserList;

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}

	public int getTotalRow() {
		return totalRow;
	}

	public void setTotalRow(int totalRow) {
		this.totalRow = totalRow;
	}

	public List<MeiliTianyuanUserInfoModel> getUserList() {
		return UserList;
	}

	public void setUserList(List<MeiliTianyuanUserInfoModel> userList) {
		UserList = userList;
	}

}
