package com.study.mike.springboot.study1.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HelloController {

	@GetMapping("/thymeleaf")
	public String handle(String name) {
		return "hello";
	}
}
