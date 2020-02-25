package com.dongnaoedu.mall.manager.dto.front;

import java.io.Serializable;

/**
 * @author allen
 */
public class CommonDto implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long userId;

	private String imgData;

	private String token;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getImgData() {
		return imgData;
	}

	public void setImgData(String imgData) {
		this.imgData = imgData;
	}
}
