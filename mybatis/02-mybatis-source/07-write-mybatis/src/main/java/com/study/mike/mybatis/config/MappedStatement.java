package com.study.mike.mybatis.config;

import java.util.List;

public class MappedStatement {

	private String id;

	private String sql;

	private SqlCommandType sqlCommandType;

	private List<ParameterMap> parameterMaps;

	private ResultHandler resultHandler;

	public RealSqlAndParamValues getRealSqlAndParamValues(Object[] args) {
		// TODO
		return null;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	public SqlCommandType getSqlCommandType() {
		return sqlCommandType;
	}

	public void setSqlCommandType(SqlCommandType sqlCommandType) {
		this.sqlCommandType = sqlCommandType;
	}

	public List<ParameterMap> getParameterMaps() {
		return parameterMaps;
	}

	public void setParameterMaps(List<ParameterMap> parameterMaps) {
		this.parameterMaps = parameterMaps;
	}

	public ResultHandler getResultHandler() {
		return resultHandler;
	}

	public void setResultHandler(ResultHandler resultHandler) {
		this.resultHandler = resultHandler;
	}

}
