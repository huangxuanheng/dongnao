package edu.dongnao.study.balancer.lesson1_demo;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import edu.dongnao.study.balancer.lesson1_demo.pojo.User;

/**
 * TestController
 * 
 */
@Controller
public class TestController {
	
	@RequestMapping("/bench/{delay}")
	@ResponseBody
	@SuppressWarnings("rawtypes")
	public Map query(@PathVariable("delay") long delay) {
		try {
			if(delay <= 0) {
				delay = 500;
			}
			Thread.sleep(delay);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		HashMap<String, String> result = new HashMap<String, String>();
		result.put("serverDelayTime", delay+" ms");
		return result;
	}
	
	@RequestMapping("/index")
	public ModelAndView index() {
		User user = new User();
		user.setName("hashMap");
		user.setAge(18);
		user.setSex('男');
		user.setAddress("我住计算机肚子里，"+user.toString());
		ModelAndView mv = new ModelAndView();
		mv.setViewName("/index.html");
		mv.addObject("user", user);
		return mv;
	}
}

