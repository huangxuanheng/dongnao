package com.study.mike.spring.sample.tx;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.study.mike.spring.sample.tx.entity.User;
import com.study.mike.spring.sample.tx.service.UserService;

@Configuration
@ComponentScan("com.study.mike.spring.sample.tx")
@ImportResource("classpath:com/study/mike/spring/sample/tx/application.xml")
@EnableTransactionManagement
public class TxMain {

	public static void main(String[] args) {
		try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(TxMain.class);) {
			User user = new User();
			user.setId("2234561");
			user.setUserName("mike-666666666");

			UserService userService = context.getBean(UserService.class);
			userService.insertUser(user);
			// userService.addUser(user);
		}
	}
}
