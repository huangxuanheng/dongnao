package com.study.mike.spring_boot_study_share_starter;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.study.mike.spring_boot_study_share.ShareService;

@Configuration
@EnableConfigurationProperties(ShareProperties.class)
public class ShareAutoConfiguration {

	@Bean
	public ShareService getShareServie(ShareProperties shareProperties) {
		ShareService ss = new ShareService();
		ss.setName(shareProperties.getName());
		ss.setDesc(shareProperties.getDesc());
		ss.setUrl(shareProperties.getUrl());

		return ss;
	}
}
