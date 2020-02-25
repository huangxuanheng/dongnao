package com.study.mike.spring_boot_study_share_starter;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "mike.share")
public class ShareProperties {
	/**
	 * 服务的名称
	 */
	private String name;

	/**
	 * 服务的描述
	 */
	private String desc;

	/**
	 * 服务的地址
	 */
	private String url;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}
