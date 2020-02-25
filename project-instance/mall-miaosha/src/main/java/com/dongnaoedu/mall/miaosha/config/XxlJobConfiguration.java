/**
 * 
 */
package com.dongnaoedu.mall.miaosha.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.xxl.job.core.executor.XxlJobExecutor;

/**
 * xxl-job配置
 * 
 * @author 动脑学院.Tony老师
 * @see 专注在职IT人员能力提升，咨询顾问QQ: 2729 772 006
 */
@Configuration
@ComponentScan(basePackages = "com.dongnaoedu.mall.miaosha.schedule")
public class XxlJobConfiguration {
    private Logger logger = LoggerFactory.getLogger(XxlJobConfiguration.class);

    @Bean(initMethod = "start", destroyMethod = "destroy")
    @ConfigurationProperties(prefix = "xxl-job")
    public XxlJobExecutor xxlJobExecutor() {
        logger.info(">>>>>>>>>>> xxl-job config init.");
        return new XxlJobExecutor();

    }
}
