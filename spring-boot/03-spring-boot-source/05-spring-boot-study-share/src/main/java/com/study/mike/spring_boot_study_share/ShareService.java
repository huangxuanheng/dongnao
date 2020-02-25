package com.study.mike.spring_boot_study_share;

public class ShareService {
	private String name;

	private String desc;

	private String url;

	public void service() {
		System.out.println(this.toString());
	}

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

	@Override
	public String toString() {
		return "ShareService [name=" + name + ", desc=" + desc + ", url=" + url + "]";
	}

}
