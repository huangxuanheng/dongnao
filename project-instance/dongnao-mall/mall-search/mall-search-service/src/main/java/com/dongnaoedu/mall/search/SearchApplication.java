package com.dongnaoedu.mall.search;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

//@Configuration
//@EnableApolloConfig
@SpringBootApplication
@EnableTransactionManagement
//@EnableScheduling
@ComponentScan(basePackages = {"com.dongnaoedu.mall"})
@MapperScan(basePackages = "com.dongnaoedu.mall.search.mapper")
public class SearchApplication {

	public static void main(String[] args) {
		SpringApplication.run(SearchApplication.class, args);
	}
}
