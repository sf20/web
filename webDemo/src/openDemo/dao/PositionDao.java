package openDemo.dao;

import openDemo.entity.PositionModel;

public class PositionDao extends GenericDaoImpl<PositionModel> {
	public static final String TABLENAME_POSITION = "position";

	@Override
	String generateGetByIdSql() {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT * FROM ").append(TABLENAME_POSITION).append(" WHERE pNo = ?");
		return sql.toString();
	}

	@Override
	String generateDeleteByIdSql() {
		StringBuffer sql = new StringBuffer();
		sql.append("DELETE FROM ").append(TABLENAME_POSITION).append(" WHERE pNo = ?");
		return sql.toString();
	}

	@Override
	String generateInsertSql() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("INSERT INTO ").append(TABLENAME_POSITION);
		buffer.append("(pNo, pNames) VALUES(?, ?)");

		return buffer.toString();
	}

	@Override
	String generateUpdateSql() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("UPDATE ").append(TABLENAME_POSITION).append(" SET ");
		buffer.append("pNames = ?");
		buffer.append(" WHERE pNo = ?");

		return buffer.toString();
	}

	@Override
	Object[] getInsertObjectParamArray(PositionModel position) {
		Object[] params = { position.getpNo(), position.getpNames() };
		return params;
	}

	@Override
	Object[] getUpdateObjectParamArray(PositionModel position) {
		Object[] params = { position.getpNames(), position.getpNo() };
		return params;
	}

	@Override
	String generateGetAllSql() {
		StringBuffer sql = new StringBuffer();
		sql.append(
				"SELECT DISTINCT POSITIONNO AS pNo, POSITIONNAME AS pNames FROM CORE_POSITIONINFO WHERE ORGID = ? AND INFOTYPE = 'Item' and ISDELETED = 0");
		return sql.toString();
	};
}
