package com.dongnaoedu.mall.manager.config;

import java.util.Locale;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

@Configuration
public class I18nConfig implements WebMvcConfigurer {

	/**
	 * session区域解析器
	 */
//	@Bean
//	public LocaleResolver localeResolver() {
//		SessionLocaleResolver slr = new SessionLocaleResolver();
//		// 这里通过设置China.US可以进行中英文转化
//		slr.setDefaultLocale(Locale.SIMPLIFIED_CHINESE);
//		return slr;
//	}
	
    /**
     * cookie区域解析器
     */
    @Bean
    public LocaleResolver localeResolver() {
        CookieLocaleResolver slr = new CookieLocaleResolver();
        // 设置默认区域
        slr.setDefaultLocale(Locale.CHINA);
        slr.setCookieMaxAge(3600);//设置cookie有效期.
        return slr;
    }
    
//  @Bean
//  public LocaleResolver localeResolver() {
//      FixedLocaleResolver slr = new FixedLocaleResolver ();
//      //设置默认区域
//      slr.setDefaultLocale(Locale.US);
//      return slr;
//  }

	@Bean
	public LocaleChangeInterceptor localeChangeInterceptor() {
		LocaleChangeInterceptor lci = new LocaleChangeInterceptor();
		// 设置请求地址的参数,默认为：locale
		lci.setParamName("lang");
		// lci.setParamName(LocaleChangeInterceptor.DEFAULT_PARAM_NAME);
		return lci;
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(localeChangeInterceptor());
	}

}
