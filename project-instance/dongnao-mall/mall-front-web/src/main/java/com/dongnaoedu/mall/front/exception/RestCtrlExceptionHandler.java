package com.dongnaoedu.mall.front.exception;

import java.net.BindException;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.dongnaoedu.mall.common.exception.MallException;
import com.dongnaoedu.mall.common.pojo.Result;
import com.dongnaoedu.mall.common.utils.ResultUtil;

/**
 * @author allen
 */
@ControllerAdvice
public class RestCtrlExceptionHandler {

	private static Logger log = LoggerFactory.getLogger(RestCtrlExceptionHandler.class);

	@ExceptionHandler(BindException.class)
	@ResponseStatus(value = HttpStatus.OK)
	@ResponseBody
	public Result<Object> bindExceptionHandler(BindException e) {
		String errorMsg = "请求数据校验不合法: ";
		if (e != null) {
			errorMsg = e.getMessage();
			log.warn(errorMsg);
		}
		return new ResultUtil<>().setErrorMsg(errorMsg);
	}

	@ResponseStatus(value = HttpStatus.OK)
	@ExceptionHandler(MallException.class)
	@ResponseBody
	public Result<Object> handleMallException(MallException e) {
		String errorMsg = "Mall exception: ";
		if (e != null) {
			errorMsg = e.getMsg();
			log.warn(e.getMessage());
		}
		return new ResultUtil<>().setErrorMsg(errorMsg);
	}

	@ExceptionHandler(Exception.class)
	@ResponseStatus(value = HttpStatus.OK)
	@ResponseBody
	public Result<Object> handleException(Exception e) {
		String errorMsg = "exception: ";
		if (e != null) {
			log.warn(e.getMessage());
			if (e.getMessage() != null && e.getMessage().contains("Maximum upload size")) {
				errorMsg = "上传文件大小超过5MB限制";
			} else if (e.getMessage().contains("MallException")) {
				errorMsg = e.getMessage();
				errorMsg = StringUtils.substringAfter(errorMsg, "MallException:");
				errorMsg = StringUtils.substringBefore(errorMsg, "\n");
			} else {
				errorMsg = e.getMessage();
			}
		}
		return new ResultUtil<>().setErrorMsg(errorMsg);
	}
}
