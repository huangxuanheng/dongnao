package com.study.mike.mybatis.type;

import java.lang.reflect.Type;
import java.sql.JDBCType;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public interface TypeHandler<T> {

	Type getType();

	JDBCType getJDBCType();

	void setParameter(PreparedStatement pst, int index, Object paramValue) throws SQLException;

	T getResult(ResultSet rs, String columnName) throws SQLException;

	T getResult(ResultSet rs, int columnIndex) throws SQLException;
}
