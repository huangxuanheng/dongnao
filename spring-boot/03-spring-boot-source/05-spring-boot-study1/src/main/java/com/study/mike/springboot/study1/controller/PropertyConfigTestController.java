package com.study.mike.springboot.study1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PropertyConfigTestController {
	@Autowired
	private Environment env;

	@GetMapping("/property")
	public String propertyValueTest(String name) {
		return env.getProperty(name);
	}
}
