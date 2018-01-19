package openDemo.dao;

import java.lang.reflect.ParameterizedType;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import openDemo.common.JdbcUtil;

public abstract class GenericDaoImpl<T> implements GenericDao<T> {
	protected static DataSource dataSource;
	private Class<T> entityClass;

	@SuppressWarnings("unchecked")
	public GenericDaoImpl() {
		// 获取数据源
		dataSource = JdbcUtil.getDataSource();
		// 得到T.class
		entityClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
	}

	@Override
	public T getById(String id) throws SQLException {
		return new QueryRunner(dataSource).query(generateGetByIdSql(), new BeanHandler<>(entityClass), id);
	}

	@Override
	public List<T> getAll() throws SQLException {
		return new QueryRunner(dataSource).query(generateGetAllSql(), new BeanListHandler<>(entityClass));
	}

	public List<T> getAllByOrgId(String orgId) throws SQLException {
		return new QueryRunner(dataSource).query(generateGetAllByOrgIdSql(), new BeanListHandler<>(entityClass), orgId);
	}

	@Override
	public int getAllCount() throws SQLException {
		return new QueryRunner(dataSource).query(generateGetAllCountSql(), new ScalarHandler<Long>()).intValue();
	}

	@Override
	public void insert(T t) throws SQLException {
		new QueryRunner(dataSource).update(generateInsertSql(), getInsertObjectParamArray(t));
	}

	@Override
	public void update(T t) throws SQLException {
		new QueryRunner(dataSource).update(generateUpdateSql(), getUpdateObjectParamArray(t));
	}

	@Override
	public void deleteById(String id) throws SQLException {
		new QueryRunner(dataSource).update(generateDeleteByIdSql(), id);
	}

	@Override
	public void insertBatch(List<T> list) throws SQLException {
		int listSize = list.size();
		Object[][] params = new Object[listSize][];
		for (int i = 0; i < listSize; i++) {
			params[i] = getInsertObjectParamArray(list.get(i));
		}

		new QueryRunner(dataSource).batch(generateInsertSql(), params);
	}

	@Override
	public void updateBatch(List<T> list) throws SQLException {
		int listSize = list.size();
		Object[][] params = new Object[listSize][];
		for (int i = 0; i < listSize; i++) {
			params[i] = getUpdateObjectParamArray(list.get(i));
		}

		new QueryRunner(dataSource).batch(generateUpdateSql(), params);
	}

	@Override
	public void deleteByIds(String[] ids) throws SQLException {
		int len = ids.length;
		Object[][] params = new Object[len][];
		for (int i = 0; i < len; i++) {
			params[i] = new Object[] { ids[i] };
		}

		new QueryRunner(dataSource).batch(generateDeleteByIdSql(), params);
	}

	public String generateGetByIdSql() {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT * FROM ").append(generateTableName()).append(" WHERE ID = ?");
		return sql.toString();
	};

	public String generateGetAllSql() {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT * FROM ").append(generateTableName());
		return sql.toString();
	};

	public String generateGetAllCountSql() {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT count(*) FROM ").append(generateTableName());
		return sql.toString();
	};

	public String generateDeleteByIdSql() {
		StringBuffer sql = new StringBuffer();
		sql.append("DELETE FROM ").append(generateTableName()).append(" WHERE ID = ?");
		return sql.toString();
	};

	// String generateTableName() {
	// StringBuffer tableName = new StringBuffer();
	// // 表名前后加"`"
	// tableName.append("`").append("`");
	//
	// return tableName.toString();
	// }

	public abstract String generateTableName();

	public abstract String generateInsertSql();

	public abstract String generateUpdateSql();

	public abstract String generateGetAllByOrgIdSql();

	/**
	 * 批量化新增的参数数组
	 * 
	 * @param t
	 * @return
	 */
	public abstract Object[] getInsertObjectParamArray(T t);

	/**
	 * 批量化更新的参数数组
	 * 
	 * @param t
	 * @return
	 */
	public abstract Object[] getUpdateObjectParamArray(T t);

}
