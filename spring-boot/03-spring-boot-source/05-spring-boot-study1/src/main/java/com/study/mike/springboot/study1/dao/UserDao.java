package com.study.mike.springboot.study1.dao;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

//ssm
@Mapper
public interface UserDao {

	@Select("select * from t_user where id = #{id}")
	Map<String, Object> get(@Param("id") String id);
}
