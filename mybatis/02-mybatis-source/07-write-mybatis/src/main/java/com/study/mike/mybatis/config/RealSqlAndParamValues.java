package com.study.mike.mybatis.config;

public class RealSqlAndParamValues {

	private String sql;

	private Object[] paramValues;

	public RealSqlAndParamValues(String sql, Object[] paramValues) {
		super();
		this.sql = sql;
		this.paramValues = paramValues;
	}

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	public Object[] getParamValues() {
		return paramValues;
	}

	public void setParamValues(Object[] paramValues) {
		this.paramValues = paramValues;
	}

}
