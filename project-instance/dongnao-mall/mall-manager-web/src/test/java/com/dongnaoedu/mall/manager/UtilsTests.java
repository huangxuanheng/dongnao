package com.dongnaoedu.mall.manager;

import org.apache.shiro.crypto.hash.SimpleHash;
import org.junit.Test;

public class UtilsTests {

	@Test
	public void shiroPass() {
		String hashAlgorithmName = "MD5";// 加密方式
		Object crdentials = "admin";// 密码原值
		Object salt = "admin";// 盐值
		int hashIterations = 2;// 加密1024次
		Object result = new SimpleHash(hashAlgorithmName, crdentials, salt, hashIterations);
		System.out.println(result);

	}

}
