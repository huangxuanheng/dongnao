package edu.dongnao.study.dubbo.spi;

import com.alibaba.dubbo.common.extension.SPI;

/**
 * Robot
 * 
 */
@SPI("bumblebee")
public interface Robot {
	void sayHello();
}

