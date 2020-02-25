package es.search;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class InitDemo {
    private static TransportClient client;

    public static TransportClient getClient() throws UnknownHostException {
        if (client == null){
            Settings settings = Settings.builder()
                    .put("cluster.name", "es-study")
                    .put("client.transport.sniff", true)
                    .build();

            client = new PreBuiltTransportClient(settings)
                    .addTransportAddress(new TransportAddress(InetAddress.getByName("192.168.90.131"),9300));

        }
        return client;
    }



}
