package com.dongnaoedu.mall.wagger;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//@Configuration
//@ComponentScan(value = "org.xx.interceptor")
class WebMvcConfig implements WebMvcConfigurer {
 
    @Value("${swagger.show}")
    private boolean swaggerShow;
 
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        if (this.swaggerShow) {
            registry.addResourceHandler("swagger-ui.html")
                    .addResourceLocations("classpath:/META-INF/resources/");
 
            registry.addResourceHandler("/webjars/**")
                    .addResourceLocations("classpath:/META-INF/resources/webjars/");
        }
    }
}