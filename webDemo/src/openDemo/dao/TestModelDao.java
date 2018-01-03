package openDemo.dao;

import openDemo.controller.TestModel;

public class TestModelDao extends GenericDaoImpl<TestModel> {
	public static final String TABLENAME = "test_model";

	@Override
	String generateTableName() {
		return TABLENAME;
	}

	@Override
	String generateInsertSql() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("INSERT INTO ").append(TABLENAME);
		buffer.append("(name, sex, orgName, orgCode, deptName, deptCode, userId, status)");
		buffer.append(" VALUES ");
		buffer.append("(?, ?, ?, ?, ?, ?, ?, ?)");

		return buffer.toString();
	}

	@Override
	String generateUpdateSql() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	Object[] getInsertObjectParamArray(TestModel model) {
		Object[] params = { model.getName(), model.getSex(), model.getOrgName(), model.getOrgCode(),
				model.getDeptName(), model.getDeptCode(), model.getUserId(), model.getStatus() };
		return params;
	}

	@Override
	Object[] getUpdateObjectParamArray(TestModel model) {
		// TODO Auto-generated method stub
		return null;
	}

}
