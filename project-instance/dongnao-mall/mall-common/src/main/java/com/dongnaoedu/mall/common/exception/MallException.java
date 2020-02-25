package com.dongnaoedu.mall.common.exception;

/**
 * 全局异常
 * 
 * @author allen
 */
public class MallException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	private String msg;

	public MallException(String msg) {
		super(msg);
		this.msg = msg;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
}
