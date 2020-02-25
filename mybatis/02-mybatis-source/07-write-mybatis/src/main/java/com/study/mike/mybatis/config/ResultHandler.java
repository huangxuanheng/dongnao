package com.study.mike.mybatis.config;

import java.sql.ResultSet;

public interface ResultHandler {

	Object handle(ResultSet rs, Object[] args) throws Throwable;
}
