package com.study.mike.spring.webflux.sample.freemarker;

import java.io.IOException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.ViewResolverRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.reactive.result.view.freemarker.FreeMarkerConfigurer;

import freemarker.template.TemplateExceptionHandler;

@Configuration
public class WebConfig implements WebFluxConfigurer {

	@Override
	public void configureViewResolvers(ViewResolverRegistry registry) {
		// 配置freemarker viewResovler
		registry.freeMarker(); // 快捷方法

	}

	// Configure Freemarker
	@Bean
	public FreeMarkerConfigurer freeMarkerConfigurer() throws IOException {
		FreeMarkerConfigurer configurer = new FreeMarkerConfigurer();
		// freemaker的配置信息
		freemarker.template.Configuration cfg = new freemarker.template.Configuration(
				freemarker.template.Configuration.VERSION_2_3_28);
		// 指定模板的位置
		cfg.setClassForTemplateLoading(this.getClass(), "/templates/freemarker");
		cfg.setDefaultEncoding("UTF-8");
		// 设置模板异常该如何处理
		cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
		// 设置不要在freemarker内部记录异常日志
		cfg.setLogTemplateExceptions(false);
		// 设置包裹非检查异常为TemplateException-s 异常
		cfg.setWrapUncheckedExceptions(true);

		configurer.setConfiguration(cfg);

		return configurer;
	}
}
