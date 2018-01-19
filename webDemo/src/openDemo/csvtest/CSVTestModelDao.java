package openDemo.csvtest;

import openDemo.dao.GenericDaoImpl;

public class CSVTestModelDao extends GenericDaoImpl<CSVTestModel> {
	public static final String TABLENAME = "test_model";

	@Override
	public String generateTableName() {
		return TABLENAME;
	}

	@Override
	public String generateInsertSql() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("INSERT INTO ").append(TABLENAME);
		buffer.append("(name, sex, orgName, orgCode, deptName, deptCode, userId, status)");
		buffer.append(" VALUES ");
		buffer.append("(?, ?, ?, ?, ?, ?, ?, ?)");

		return buffer.toString();
	}

	@Override
	public String generateUpdateSql() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object[] getInsertObjectParamArray(CSVTestModel model) {
		Object[] params = { model.getName(), model.getSex(), model.getOrgName(), model.getOrgCode(),
				model.getDeptName(), model.getDeptCode(), model.getUserId(), model.getStatus() };
		return params;
	}

	@Override
	public Object[] getUpdateObjectParamArray(CSVTestModel model) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String generateGetAllByOrgIdSql() {
		// TODO Auto-generated method stub
		return null;
	}

}
