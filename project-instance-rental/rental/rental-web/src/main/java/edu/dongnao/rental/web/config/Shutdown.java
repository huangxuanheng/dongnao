package edu.dongnao.rental.web.config;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.dubbo.config.ProtocolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class Shutdown implements ServletContextListener, ApplicationContextAware {
	private Logger logger = LoggerFactory.getLogger(getClass());
	private ApplicationContext applicationContext;
	
	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		Map<String, ProtocolConfig> protocolConfigMap = applicationContext.getBeansOfType(ProtocolConfig.class);
		if(protocolConfigMap != null && !protocolConfigMap.isEmpty()) {
			Iterator<Entry<String, ProtocolConfig>> iterator = protocolConfigMap.entrySet().iterator();
			while(iterator.hasNext()) {
				Entry<String, ProtocolConfig> entry = iterator.next();
				logger.warn(entry.getKey()+" will be destroy.");
				entry.getValue().destroy();
			}
		}
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
}
