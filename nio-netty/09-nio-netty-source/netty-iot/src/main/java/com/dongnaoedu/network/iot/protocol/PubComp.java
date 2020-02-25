package com.dongnaoedu.network.iot.protocol;

import com.dongnaoedu.network.iot.common.message.GrozaDupPubRelMessageStoreService;
import io.netty.channel.Channel;
import io.netty.handler.codec.mqtt.MqttMessageIdVariableHeader;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;

/**
 * PUBCOMP连接处理
 */
@Slf4j
public class PubComp {

    private GrozaDupPubRelMessageStoreService grozaDupPubRelMessageStoreService;

    public PubComp(GrozaDupPubRelMessageStoreService grozaDupPubRelMessageStoreService) {
        this.grozaDupPubRelMessageStoreService = grozaDupPubRelMessageStoreService;
    }

    public void processPubComp(Channel channel, MqttMessageIdVariableHeader variableHeader) {
        int messageId = variableHeader.messageId();
        log.info("PUBCOMP - clientId: {}, messageId: {}", (String) channel.attr(AttributeKey.valueOf("clientId")).get(), messageId);
        grozaDupPubRelMessageStoreService.remove((String) channel.attr(AttributeKey.valueOf("clientId")).get(), variableHeader.messageId());
    }
}
