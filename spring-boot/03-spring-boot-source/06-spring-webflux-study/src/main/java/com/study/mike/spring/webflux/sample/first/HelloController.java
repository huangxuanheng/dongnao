package com.study.mike.spring.webflux.sample.first;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

	@GetMapping("/hello")
	public String handle(String name) {
		return "Hello " + name;
	}
}