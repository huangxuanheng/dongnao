package edu.dongnao.study.dubbo.consumer;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;


/**
 * 使用Dubbo注解的方式使用dubbo
 * AnnotationConsumerConfiguration
 */
//@Configuration
@EnableDubbo(scanBasePackages = "edu.dongnao.study.dubbo.consumer")
@PropertySource("classpath:/dubbo/dubbo-consumer.properties")
@ComponentScan(value = { "edu.dongnao.study.dubbo.consumer" })
public class AnnotationConsumerConfiguration {
	
	/**
	 * Dubbo注解方式配置服务，运行示例。
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(
				AnnotationConsumerConfiguration.class);
		context.start();
		final AnnotationDemoAction annotationAction = context.getBean(AnnotationDemoAction.class);
		String hello = annotationAction.doSayHello("world");
		System.out.println(hello);
		context.close();
	}
}
