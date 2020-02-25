package com.dongnaoedu.mall.miaosha.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ConsumerConfig;
import com.alibaba.dubbo.config.ProtocolConfig;
import com.alibaba.dubbo.config.ProviderConfig;
import com.alibaba.dubbo.config.RegistryConfig;

/**
 * dubbo配置项。简单使用，provider统一的情况下这么使用
 * 
 * @author 动脑学院.Tony老师
 * @see 专注在职IT人员能力提升，咨询顾问QQ: 2729 772 006
 */
@ConfigurationProperties(prefix = "dubbo", ignoreUnknownFields = false)
public class DubboConfigurationProperties {

    String serviceVersion = "1.0"; // 服务版本号

    ApplicationConfig application;

    ConsumerConfig consumer = new ConsumerConfig();

    ProtocolConfig protocol = new ProtocolConfig();

    ProviderConfig provider = new ProviderConfig();

    RegistryConfig registry = new RegistryConfig();

    public String getServiceVersion() {
        return serviceVersion;
    }

    public void setServiceVersion(String serviceVersion) {
        this.serviceVersion = serviceVersion;
    }

    public ApplicationConfig getApplication() {
        return application;
    }

    public void setApplication(ApplicationConfig application) {
        this.application = application;
    }

    public ConsumerConfig getConsumer() {
        return consumer;
    }

    public void setConsumer(ConsumerConfig consumer) {
        this.consumer = consumer;
    }

    public ProtocolConfig getProtocol() {
        return protocol;
    }

    public void setProtocol(ProtocolConfig protocol) {
        this.protocol = protocol;
    }

    public ProviderConfig getProvider() {
        return provider;
    }

    public void setProvider(ProviderConfig provider) {
        this.provider = provider;
    }

    public RegistryConfig getRegistry() {
        return registry;
    }

    public void setRegistry(RegistryConfig registry) {
        this.registry = registry;
    }

}
