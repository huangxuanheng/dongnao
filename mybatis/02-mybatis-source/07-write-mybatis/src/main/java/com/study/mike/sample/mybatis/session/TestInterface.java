package com.study.mike.sample.mybatis.session;

import java.util.List;

import com.study.mike.mybatis.annotations.Param;

public interface TestInterface {

	String do1(@Param("xname") String name);

	Integer do2(String aaa);

	List<String> do3(String aaa);
}
