package edu.dongnao.rental.house.provider;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

/**
 * 房源服务启动入口
 * 
 */
@SpringBootApplication
@EnableDubbo
@ServletComponentScan	// 加载Dubbo的DispatcherServlet
public class HouseProviderApplicatio {
	
    public static void main(String[] args ) {
        SpringApplication.run(HouseProviderApplicatio.class, args);
    }
}
