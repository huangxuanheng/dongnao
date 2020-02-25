package edu.dongnao.rental.house.provider.config;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * ElasticSearch配置及示例化
 */
@Configuration
public class ElasticSearchConfig {
    @Value("${elasticsearch.host}")
    private String esHost;

    @Value("${elasticsearch.port}")
    private int esPort;

    @Value("${elasticsearch.cluster.name}")
    private String esName;
    
    @Bean
    public TransportClient esClient() throws UnknownHostException {
        Settings settings = Settings.builder()
                .put("cluster.name", this.esName)
                .put("client.transport.sniff", true)
                .build();
        
        TransportAddress master = new TransportAddress(InetAddress.getByName(esHost), esPort);
        PreBuiltTransportClient preBuiltTransportClient = new PreBuiltTransportClient(settings);
		TransportClient client = preBuiltTransportClient.addTransportAddress(master);
        
		// 关闭client处理
        Runtime.getRuntime().addShutdownHook(new Thread() {
        	@Override
        	public void run() {
        		client.close();
        		preBuiltTransportClient.close();
        	}
        });
        return client;
    }
}
