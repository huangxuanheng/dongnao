package com.dn.spring.samples.config;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;

import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;

public class GetResourceTest {

	public static void main(String[] args) throws IOException {
		Enumeration<URL> enu = GetResourceTest.class.getClassLoader().getResources("com/dn/spring/samples/CBean.class");
		while (enu.hasMoreElements()) {
			System.out.println(enu.nextElement());
		}

		ClassPathScanningCandidateComponentProvider scan = new ClassPathScanningCandidateComponentProvider(true);
		scan.findCandidateComponents("com/dn/spring");
	}
}
