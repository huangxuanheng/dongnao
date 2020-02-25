package com.dongnaoedu.mall.content;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.dongnaoedu.mall.content.service.bean.ContentBean;
import com.dongnaoedu.mall.content.service.bean.SpringUtil;

@SpringBootApplication
@EnableTransactionManagement
//@EnableScheduling
@ComponentScan(value = {"com.dongnaoedu.mall"})
@MapperScan(basePackages = "com.dongnaoedu.mall.manager.mapper")
public class ContentApplication {

	public static void main(String[] args) {
		ApplicationContext app = SpringApplication.run(ContentApplication.class, args);
		
		SpringUtil.setApplicationContext(app);
		
		ContentBean cb = SpringUtil.getBean(ContentBean.class);
		System.out.println(cb.toString());
	}
}
