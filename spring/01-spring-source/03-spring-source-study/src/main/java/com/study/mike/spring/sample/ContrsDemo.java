package com.study.mike.spring.sample;

import com.study.mike.spring.sample.config.BeanE;
import com.study.mike.spring.sample.config.BeanF;

public class ContrsDemo {

	private int age;

	private String name;

	private BeanE be;

	private BeanF bf;

	public ContrsDemo(int age, String name, BeanE be, BeanF bf) {
		super();
		this.age = age;
		this.name = name;
		this.be = be;
		this.bf = bf;
	}

	public ContrsDemo(String name, BeanE be, BeanF bf) {
		super();
		this.name = name;
		this.be = be;
		this.bf = bf;
	}

	public ContrsDemo(BeanE be, BeanF bf) {
		super();
		this.be = be;
		this.bf = bf;
	}

}
