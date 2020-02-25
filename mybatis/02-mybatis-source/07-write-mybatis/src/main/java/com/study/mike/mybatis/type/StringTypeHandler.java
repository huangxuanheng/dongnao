package com.study.mike.mybatis.type;

import java.lang.reflect.Type;
import java.sql.JDBCType;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StringTypeHandler implements TypeHandler<String> {

	@Override
	public Type getType() {
		return String.class;
	}

	@Override
	public JDBCType getJDBCType() {
		return JDBCType.VARCHAR;
	}

	@Override
	public void setParameter(PreparedStatement pst, int index, Object paramValue) throws SQLException {
		pst.setString(index, (String) paramValue);
	}

	@Override
	public String getResult(ResultSet rs, String columnName) throws SQLException {
		return rs.getString(columnName);
	}

	@Override
	public String getResult(ResultSet rs, int columnIndex) throws SQLException {
		return rs.getString(columnIndex);
	}

}
