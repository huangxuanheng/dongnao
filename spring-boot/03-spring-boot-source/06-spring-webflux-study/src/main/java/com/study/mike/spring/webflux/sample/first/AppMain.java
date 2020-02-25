package com.study.mike.spring.webflux.sample.first;

import java.util.Scanner;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.reactive.HttpHandler;
import org.springframework.http.server.reactive.ReactorHttpHandlerAdapter;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.server.adapter.WebHttpHandlerBuilder;

import reactor.netty.http.server.HttpServer;

//注意注解
@Configuration
@ComponentScan("com.study.mike.spring.webflux.sample.first")
@EnableWebFlux
public class AppMain {

	public static void main(String[] args) {
		// 1、创建ApplicationContext
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppMain.class);

		// 2、构建HttpHandler(http请求的处理器)
		HttpHandler handler = WebHttpHandlerBuilder.applicationContext(context).build();
		// 3、构建与底层http服务器程序API适配的ReactorHttpHandlerAdapter
		ReactorHttpHandlerAdapter adapter = new ReactorHttpHandlerAdapter(handler);
		// 4、开启http服务
		HttpServer.create().port(9000).handle(adapter).bind().block();

		// 5、保持程序不关闭，来接收处理web请求
		// while (true)
		// ;
		// 5、也可提供一个用户交互来控制程序结束
		Scanner sc = new Scanner(System.in);
		while (!"s".equals(sc.nextLine()))
			;
		sc.close();
		context.close();
	}
}
