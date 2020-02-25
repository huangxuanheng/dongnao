package com.dongnaoedu.mall.sso.service;

/**
 * @author allen
 */
public interface MemberService {

	/**
	 * 头像上传
	 * 
	 * @param userId
	 * @param token
	 * @param imgData
	 * @return
	 */
	String imageUpload(Long userId, String token, String imgData);
}
