package com.dongnaoedu.network.iot.protocol;

import com.dongnaoedu.network.iot.common.message.GrozaDupPublishMessageStoreService;
import io.netty.channel.Channel;
import io.netty.handler.codec.mqtt.MqttMessageIdVariableHeader;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;


/**
 * PUBACK连接处理
 */
@Slf4j
public class PubAck {

    private GrozaDupPublishMessageStoreService grozaDupPublishMessageStoreService;

    public PubAck(GrozaDupPublishMessageStoreService grozaDupPublishMessageStoreService) {
        this.grozaDupPublishMessageStoreService = grozaDupPublishMessageStoreService;
    }

    public void processPubAck(Channel channel, MqttMessageIdVariableHeader variableHeader) {
        int messageId = variableHeader.messageId();
        log.info("PUBACK - clientId: {}, messageId: {}", (String) channel.attr(AttributeKey.valueOf("clientId")).get(), messageId);
        grozaDupPublishMessageStoreService.remove((String) channel.attr(AttributeKey.valueOf("clientId")).get(), messageId);

    }
}
