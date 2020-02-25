package com.dongnaoedu.mall.job.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;

/**
 * 描述: 加载配置
 *
 * @author allen
 **/
@Configuration
@PropertySource("classpath:dubbo.properties")
@ImportResource({ "classpath:dubbo/*.xml" })
public class DubboConfig {

}
