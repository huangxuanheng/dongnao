package edu.dongnao.study.dubbo.consumer;

import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RestController;

import edu.dongnao.study.dubbo.DemoService;

/**
 * dubbo @Reference 注解的使用示例
 * AnnotationDemoAction
 */
@RestController
public class AnnotationDemoAction {
	
	@Reference
	private DemoService demoService;	// 通过@Reference注解来获得客户端接口代理

	public String doSayHello(String name) {
		return demoService.sayHello(name);
	}
}
