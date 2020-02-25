package com.study.mike.springboot.study1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.study.mike.springboot.study1.dao.UserDao;

@RestController // = @controller + @responseBody
public class UserController {

	@Autowired
	private UserDao userDao;

	@GetMapping("/user/get")
	public Object getUser(String id) {
		return this.userDao.get(id);
	}
}
