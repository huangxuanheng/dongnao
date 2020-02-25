package com.dongnaoedu.network.iot.store.session;

import com.dongnaoedu.network.iot.common.session.GrozaSessionStoreService;
import com.dongnaoedu.network.iot.common.session.SessionStore;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 会话存储接口类
 */
@Service
public class GrozaSessionStoreServiceImpl implements GrozaSessionStoreService {

    private Map<String, SessionStore> sessionCache = new ConcurrentHashMap<String, SessionStore>();

    @Override
    public void put(String clientId, SessionStore sessionStore) {
        sessionCache.put(clientId, sessionStore);
    }

    @Override
    public SessionStore get(String clientId) {
        return sessionCache.get(clientId);
    }

    @Override
    public boolean containsKey(String clientId) {
        return sessionCache.containsKey(clientId);
    }

    @Override
    public void remove(String clientId) {
        sessionCache.remove(clientId);
    }


}
