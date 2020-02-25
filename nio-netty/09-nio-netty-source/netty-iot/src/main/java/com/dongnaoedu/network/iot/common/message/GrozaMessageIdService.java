package com.dongnaoedu.network.iot.common.message;

/**
 * 分布式生成报文标识符
 */
public interface GrozaMessageIdService {
    /**
     * 获取报文标识符
     */
    int getNextMessageId();
}
