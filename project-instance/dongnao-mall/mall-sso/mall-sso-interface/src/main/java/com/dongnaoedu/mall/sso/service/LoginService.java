package com.dongnaoedu.mall.sso.service;

import com.dongnaoedu.mall.manager.dto.front.Member;

/**
 * @author allen
 */
public interface LoginService {

	/**
	 * 登录
	 * 
	 * @param username
	 * @param password
	 * @return
	 */
	Member userLogin(String username, String password);

	/**
	 * 通过token获取
	 * 
	 * @param token
	 * @return
	 */
	Member getUserByToken(String token);

	/**
	 * 注销
	 * 
	 * @param token
	 * @return
	 */
	int logout(String token);
}
