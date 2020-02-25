package com.study.mike.sample.mybatis.mapper;

import java.util.List;
import java.util.Map;

import com.study.mike.mybatis.annotations.Insert;
import com.study.mike.mybatis.annotations.Mapper;
import com.study.mike.mybatis.annotations.Select;
import com.study.mike.sample.mybatis.model.User;

@Mapper
public interface UserDao {

	@Insert("insert into t_user(id,name,sex,age) values(?,?,?,?)")
	void addUser(User user);

	@Select("select id,name,sex,age,address from t_user where sex = #{sex} order by #{orderColumn}")
	List<User> query(String sex, String orderColumn);

	@Select("select id,name,sex,age,address from t_user where sex = #{sex} order by #{orderColumn}")
	List<Map> query1(String sex, String orderColumn);

	@Select("select count(1) from t_user where sex = #{sex}")
	int query(String sex);

	@Select("select id,name,sex,age,address from t_user where id = #{id}")
	User queryUser(String id);

	@Select("select id,name,sex,age,address from t_user where id = #{id}")
	Map queryUser1(String id);

	@Select("select id,name,sex,age,address from t_user where sex = #{sex} order by #{orderColumn}")
	void query(String sex, String orderColumn, List<User> user);

	void doSomething();
}
