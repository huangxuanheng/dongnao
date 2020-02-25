package com.dongnaoedu.network.iot.protocol;

import com.dongnaoedu.network.iot.common.message.GrozaDupPubRelMessageStoreService;
import com.dongnaoedu.network.iot.common.message.GrozaDupPublishMessageStoreService;
import com.dongnaoedu.network.iot.common.session.GrozaSessionStoreService;
import com.dongnaoedu.network.iot.common.session.SessionStore;
import com.dongnaoedu.network.iot.common.subscribe.GrozaSubscribeStoreService;
import io.netty.channel.Channel;
import io.netty.handler.codec.mqtt.MqttMessage;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;

/**
 * @DISCONNECT 连接处理
 */
@Slf4j
public class DisConnect {

    private GrozaSessionStoreService grozaSessionStoreService;

    private GrozaSubscribeStoreService grozaSubscribeStoreService;

    private GrozaDupPublishMessageStoreService grozaDupPublishMessageStoreService;

    private GrozaDupPubRelMessageStoreService grozaDupPubRelMessageStoreService;

    public DisConnect(GrozaSessionStoreService grozaSessionStoreService,
                      GrozaSubscribeStoreService grozaSubscribeStoreService,
                      GrozaDupPublishMessageStoreService grozaDupPublishMessageStoreService,
                      GrozaDupPubRelMessageStoreService grozaDupPubRelMessageStoreService) {
        this.grozaSessionStoreService = grozaSessionStoreService;
        this.grozaSubscribeStoreService = grozaSubscribeStoreService;
        this.grozaDupPublishMessageStoreService = grozaDupPublishMessageStoreService;
        this.grozaDupPubRelMessageStoreService = grozaDupPubRelMessageStoreService;
    }

    public void processDisConnect(Channel channel, MqttMessage msg) {
        String clientId = (String) channel.attr(AttributeKey.valueOf("clientId")).get();
        SessionStore sessionStore = grozaSessionStoreService.get(clientId);
        if (sessionStore != null && sessionStore.isCleanSession()) {
            grozaSubscribeStoreService.removeForClient(clientId);
            grozaDupPublishMessageStoreService.removeByClient(clientId);
            grozaDupPubRelMessageStoreService.removeByClient(clientId);
        }
        log.info("DISCONNECT - clientId: {}, cleanSession: {}", clientId, sessionStore.isCleanSession());
        grozaSessionStoreService.remove(clientId);
        channel.close();
    }

}
