package com.study.mike.sample.mybatis.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.util.StringUtils;

import com.study.mike.sample.mybatis.model.User;

//@Component
public class UserDaoUseJdbcTemplate {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public void addUser(User user) throws SQLException {
		String sql = "insert into t_user(id,name,sex,age,address,phone,wechat,email,account,password) "
				+ " values(?,?,?,?,?,?,?,?,?,?)";
		jdbcTemplate.update(sql, user.getId(), user.getName(), user.getSex(), user.getAge(), user.getAddress(),
				user.getPhone(), user.getWechat(), user.getEmail(), user.getAccount(), user.getPassword());
	}

	public List<User> queryUsers(String likeName, int minAge, int maxAge, String sex) throws SQLException {
		// 1、根据查询条件动态拼接SQL语句
		StringBuffer sql = new StringBuffer(
				"select id,name,sex,age,address,phone,wechat,email,account,password from t_user where 1 = 1 ");
		List<Object> argList = new ArrayList<>();
		if (!StringUtils.isEmpty(likeName)) {
			sql.append(" and name like ? ");
			argList.add("%" + likeName + "%");
		}

		if (minAge >= 0) {
			sql.append(" and age >= ? ");
			argList.add(minAge);
		}

		if (maxAge >= 0) {
			sql.append(" and age <= ? ");
			argList.add(maxAge);
		}

		if (!StringUtils.isEmpty(sex)) {
			sql.append(" and sex = ? ");
			argList.add(sex);
		}

		return jdbcTemplate.query(sql.toString(), argList.toArray(), new RowMapper<User>() {
			public User mapRow(ResultSet rs, int rowNum) throws SQLException {
				User u = new User();
				u.setId(rs.getString("id"));
				u.setName(rs.getString("name"));
				u.setSex(rs.getString("sex"));
				u.setAge(rs.getInt("age"));
				u.setPhone(rs.getString("phone"));
				u.setEmail(rs.getString("email"));
				u.setWechat(rs.getString("wechat"));
				u.setAccount(rs.getString("account"));
				u.setPassword(rs.getString("password"));

				return u;
			}
		});
	}
}
