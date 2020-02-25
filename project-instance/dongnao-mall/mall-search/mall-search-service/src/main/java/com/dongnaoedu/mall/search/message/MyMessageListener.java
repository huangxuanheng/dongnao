package com.dongnaoedu.mall.search.message;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class MyMessageListener {

	private final static Logger log = LoggerFactory.getLogger(MyMessageListener.class);

	@JmsListener(destination = "queueDestination")
	public void onMessage(String message) {
		log.info(message);
	}

}
