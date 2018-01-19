package openDemo.dao;

import openDemo.entity.PositionModel;

public class PositionDao extends GenericDaoImpl<PositionModel> {
	public static final String TABLENAME_POSITION = "CORE_POSITIONINFO";

	@Override
	public String generateTableName() {
		return TABLENAME_POSITION;
	};

	@Override
	public String generateGetByIdSql() {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT * FROM ").append(TABLENAME_POSITION).append(" WHERE pNo = ?");
		return sql.toString();
	}

	@Override
	public String generateDeleteByIdSql() {
		StringBuffer sql = new StringBuffer();
		sql.append("DELETE FROM ").append(TABLENAME_POSITION).append(" WHERE pNo = ?");
		return sql.toString();
	}

	@Override
	public String generateInsertSql() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("INSERT INTO ").append(TABLENAME_POSITION);
		buffer.append("(pNo, pNames) VALUES(?, ?)");

		return buffer.toString();
	}

	@Override
	public String generateUpdateSql() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("UPDATE ").append(TABLENAME_POSITION).append(" SET ");
		buffer.append("pNames = ?");
		buffer.append(" WHERE pNo = ?");

		return buffer.toString();
	}

	@Override
	public String generateGetAllByOrgIdSql() {
		StringBuffer sql = new StringBuffer();
		sql.append(
				"SELECT DISTINCT POSITIONNO AS pNo, POSITIONNAME AS pNames FROM CORE_POSITIONINFO WHERE ORGID = ? AND INFOTYPE = 'Item' and ISDELETED = 0");
		return sql.toString();
	}

	@Override
	public Object[] getInsertObjectParamArray(PositionModel position) {
		Object[] params = { position.getpNo(), position.getpNames() };
		return params;
	}

	@Override
	public Object[] getUpdateObjectParamArray(PositionModel position) {
		Object[] params = { position.getpNames(), position.getpNo() };
		return params;
	}

}
