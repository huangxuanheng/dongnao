package com.study.mike.mybatis.config;

import java.io.InputStream;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.sql.DataSource;

public class Configuration {

	private Map<String, MappedStatement> mappedStatements;
	private DataSource dataSource;
	private Set<Class<?>> mappers = new HashSet<>();

	public Set<Class<?>> getMappers() {
		return mappers;
	}

	public void addMappedStatement(MappedStatement ms) {
		this.mappedStatements.put(ms.getId(), ms);
	}

	public MappedStatement getMappedStatement(String id) {
		return this.mappedStatements.get(id);
	}

	public boolean hasMappedStatement(String id) {
		return this.mappedStatements.containsKey(id);
	}

	public void addXmlMapper(InputStream inputStream, String resource) {

	}

	public void addMapper(Class<?> type) {

	}

	public void addMappers(String packageName) {

	}

	public void addMappers(String packageName, Class<?> superType) {

	}

	public void addMappersWithAnnotation(String packageName, Class<?> annotation) {

	}

	public void addMappers(String packageName, Class<?> superType, Class<?> annotation) {

	}

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

}
