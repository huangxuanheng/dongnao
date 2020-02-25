package com.dongnao.mall;


public class LoginService {
	public boolean login(String userName,String passWord) {
		LoginRepository loginRe = new LoginRepository();
		return loginRe.login(userName, passWord);
	}
}
