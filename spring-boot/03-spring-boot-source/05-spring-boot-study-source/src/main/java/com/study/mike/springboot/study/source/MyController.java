package com.study.mike.springboot.study.source;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MyController implements ApplicationRunner {

	@RequestMapping("/index")
	public String toIndex() {
		return "index";
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		System.out.println("************************* MyController(ApplicationRunner).run " + args.getSourceArgs()[0]);
	}
}
