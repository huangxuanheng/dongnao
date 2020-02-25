package com.dongnaoedu.network.iot.common.message;

import com.dongnaoedu.network.iot.internal.InternalMessage;

/**
 * 消息转发,基于kafka
 */
public interface GrozaKafkaService {
    void send(InternalMessage internalMessage);
}
