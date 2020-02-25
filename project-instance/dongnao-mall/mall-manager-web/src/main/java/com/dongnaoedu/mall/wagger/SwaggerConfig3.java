package com.dongnaoedu.mall.wagger;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * 配置文件中一定要加：
 * swagger.show=true
 * 
 * 还需要注意加载WebMvcConfig
 * 
 * @author allen
 *
 */
//@Configuration
//@EnableSwagger2
public class SwaggerConfig3 {
 
    @Value("${swagger.show}")
    private boolean swaggerShow;
 
    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .enable(swaggerShow)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("org.xx.controller"))
                .paths(PathSelectors.any())
                .build();
    }
 
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("RESTful APIs")
                .description("用于项目前端接口调用")
                .termsOfServiceUrl("")
                .contact("")
                .version("1.0")
                .termsOfServiceUrl("11")
                .build();
    }
}
