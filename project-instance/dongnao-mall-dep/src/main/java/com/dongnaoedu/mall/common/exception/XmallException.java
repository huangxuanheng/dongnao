package com.dongnaoedu.mall.common.exception;

/**
 * 异常处理
 * 
 * @author allen
 *
 */
public class XmallException extends RuntimeException {

	private String msg;

	public XmallException(String msg) {
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
