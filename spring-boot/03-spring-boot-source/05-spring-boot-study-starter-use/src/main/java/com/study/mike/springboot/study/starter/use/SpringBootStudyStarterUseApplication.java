package com.study.mike.springboot.study.starter.use;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.study.mike.spring_boot_study_share.ShareService;

@SpringBootApplication
public class SpringBootStudyStarterUseApplication implements ApplicationRunner {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootStudyStarterUseApplication.class, args);
	}

	@Autowired
	private ShareService shareService;

	@Override
	public void run(ApplicationArguments args) throws Exception {
		this.shareService.service();
	}

}
