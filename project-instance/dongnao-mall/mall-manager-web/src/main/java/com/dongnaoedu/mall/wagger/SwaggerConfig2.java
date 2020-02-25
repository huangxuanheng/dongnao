package com.dongnaoedu.mall.wagger;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

//@Configuration
//@EnableSwagger2
//@Profile({"dev", "test"})
public class SwaggerConfig2 {

    @Bean
    public Docket swagger() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("default")
                .apiInfo(new ApiInfoBuilder().title("SSP School API").version("1.0.0").build())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.pobo.pbsimu.opcontest.controller"))
                .build()
                .globalOperationParameters(globalOperationParameters());
    }


    private List<Parameter> globalOperationParameters() {
        List<Parameter> parameters = new ArrayList<>();
        parameters.add(new ParameterBuilder().name("ACCESS-TOKEN").description("ACCESS-TOKEN").required(false).parameterType("header").modelRef(new ModelRef("string")).build());
        return parameters;

    }
}