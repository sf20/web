package openDemo.entity.sync.jrtt;

import java.util.List;

/**
 * 返回json数据对应的模型对象
 * 
 * @author yanl
 *
 */
public class ToutiaoResJsonModel<T> {
	private boolean success;
	private List<T> departments;
	private List<T> employees;

	public boolean getSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public List<T> getDepartments() {
		return departments;
	}

	public void setDepartments(List<T> departments) {
		this.departments = departments;
	}

	public List<T> getEmployees() {
		return employees;
	}

	public void setEmployees(List<T> employees) {
		this.employees = employees;
	}

}
