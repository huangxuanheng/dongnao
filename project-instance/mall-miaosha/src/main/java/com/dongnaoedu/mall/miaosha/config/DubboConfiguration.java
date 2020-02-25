package com.dongnaoedu.mall.miaosha.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ConsumerConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.alibaba.dubbo.config.spring.context.annotation.DubboComponentScan;

@Configuration
@EnableConfigurationProperties(value = DubboConfigurationProperties.class)
@DubboComponentScan(basePackages = "com.dongnaoedu.mall.manager.service")
public class DubboConfiguration {

    @Autowired
    DubboConfigurationProperties dubboConfigurationProperties;

    @Bean
    public ApplicationConfig applicationConfig() {
        return dubboConfigurationProperties.getApplication();
    }

    @Bean
    public ConsumerConfig consumerConfig() {
        ConsumerConfig consumerConfig = dubboConfigurationProperties.getConsumer();
        consumerConfig.setTimeout(3000);
        consumerConfig.setVersion(dubboConfigurationProperties.getServiceVersion());
        return consumerConfig;
    }

    @Bean
    public RegistryConfig registryConfig() {
        return dubboConfigurationProperties.getRegistry();
    }
}