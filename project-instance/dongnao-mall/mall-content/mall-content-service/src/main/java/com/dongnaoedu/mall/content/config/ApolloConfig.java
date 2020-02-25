package com.dongnaoedu.mall.content.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig;
import com.dongnaoedu.mall.content.service.bean.ContentBean;

/**
 * 通过@Configuration注入这个Bean
 * <pre>
 * 注意：要增加@EnableApolloConfig和@Configuration，不然不会生效。
 * 并且留意到@Bean的注解，如果没有这个时，@ApolloConfigChangeListener不会生效。
 * 这个是关键所在，@ApolloConfigChangeListener只能用于Bean注入上，这个和API的方式有明显区别。
 * </pre>
 * @see https://www.cnblogs.com/EasonJim/p/7649047.html
 * 
 * @author allen
 *
 */
@Configuration
@EnableApolloConfig
public class ApolloConfig {

	@Bean
	public ContentBean contentBean() {
		return new ContentBean();
	}
	
}
