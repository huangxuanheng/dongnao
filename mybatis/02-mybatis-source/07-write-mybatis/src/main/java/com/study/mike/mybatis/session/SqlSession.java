package com.study.mike.mybatis.session;

public interface SqlSession {

	<T> T getMapper(Class<T> type);
}
