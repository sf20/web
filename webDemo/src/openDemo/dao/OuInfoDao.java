package openDemo.dao;

import org.springframework.stereotype.Repository;

import openDemo.entity.OuInfoModel;

@Repository
public class OuInfoDao extends GenericDaoImpl<OuInfoModel> {
	public static final String TABLENAME_OUINFO = "CORE_ORGOUINFO";

	@Override
	public String generateTableName() {
		return TABLENAME_OUINFO;
	}

	@Override
	public String generateInsertSql() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("INSERT INTO ").append(TABLENAME_OUINFO);
		buffer.append("(ID, OuName, ParentID, Description, Users, isSub, OrderIndex)");
		buffer.append(" VALUES(?, ?, ?, ?, ?, ?, ?)");

		return buffer.toString();
	}

	@Override
	public String generateUpdateSql() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("UPDATE ").append(TABLENAME_OUINFO).append(" SET ");
		buffer.append("OuName = ?,");
		buffer.append("ParentID = ?,");
		buffer.append("Description = ?,");
		buffer.append("Users = ?,");
		buffer.append("isSub = ?,");
		buffer.append("OrderIndex = ?");
		buffer.append(" WHERE ID = ?");

		return buffer.toString();
	}

	@Override
	public String generateGetAllByOrgIdSql() {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT DISTINCT OUCODE AS ID, OUNAME AS ouName FROM CORE_ORGOUINFO WHERE ORGID = ?");
		return sql.toString();
	}

	@Override
	public Object[] getInsertObjectParamArray(OuInfoModel org) {
		Object[] params = { org.getID(), org.getOuName(), org.getParentID(), org.getDescription(), org.getUsers(),
				org.getIsSub(), org.getOrderIndex() };
		return params;
	}

	@Override
	public Object[] getUpdateObjectParamArray(OuInfoModel org) {
		Object[] params = { org.getOuName(), org.getParentID(), org.getDescription(), org.getUsers(), org.getIsSub(),
				org.getOrderIndex(), org.getID() };
		return params;
	}

}
