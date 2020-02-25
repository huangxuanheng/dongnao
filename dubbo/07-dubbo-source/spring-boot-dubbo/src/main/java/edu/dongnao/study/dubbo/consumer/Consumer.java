package edu.dongnao.study.dubbo.consumer;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import edu.dongnao.study.dubbo.DemoService;
/**
 * 客户端启动方式示例
 * Consumer
 */
public class Consumer {
	/**
	 * 通过spring xml启动方式示例
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("consumer.xml");
		context.start();

		DemoService demoService = (DemoService) context.getBean("demoService"); // 获取远程服务代理
		String hello = demoService.sayHello("world"); // 执行远程方法
		System.out.println(hello); // 显示调用结果

		System.out.println();
		System.out.println(demoService);
		context.close();
	}
}
