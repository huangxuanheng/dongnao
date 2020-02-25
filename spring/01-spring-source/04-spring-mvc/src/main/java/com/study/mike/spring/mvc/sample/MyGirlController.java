package com.study.mike.spring.mvc.sample;

import org.springframework.stereotype.Controller;

import com.study.mike.spring.mvc.annotation.RequestMapping;
import com.study.mike.spring.mvc.mvc.ModelAndView;

@Controller
@RequestMapping("/myGirl")
public class MyGirlController {

	@RequestMapping("do1")
	public ModelAndView do1(String pos) {
		return null;
	}
}
