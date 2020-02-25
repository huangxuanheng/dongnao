package com.dongnao.mall;


import static org.junit.Assume.assumeTrue;

import org.junit.Test;

public class LoginRepositoryTest {

	@Test
	public void loginTest() {
		String userName = "admin";
		String passWord = "admin";
		LoginRepository LoginRe= new LoginRepository();
		boolean loginResult = LoginRe.login(userName, passWord);
		assumeTrue(loginResult);
	}

}
