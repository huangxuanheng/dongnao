package com.dongnaoedu.network.iot.store.message;

import com.dongnaoedu.network.iot.common.message.GrozaMessageIdService;
import org.springframework.stereotype.Service;

@Service
public class MessageIdServiceImpl implements GrozaMessageIdService {
    @Override
    public int getNextMessageId() {
        return 0;
    }
}
