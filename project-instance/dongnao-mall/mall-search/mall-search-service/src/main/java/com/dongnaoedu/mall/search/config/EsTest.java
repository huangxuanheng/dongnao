package com.dongnaoedu.mall.search.config;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @Auther: allen
 * @Date: 2018/11/8 17:08
 */
public class EsTest {
    public static void main(String[] args) {
        //设置集群名称
        Settings settings = Settings.builder().put("cluster.name", "cluster-es").put("client.transport.sniff", true).build();
        //创建client
        TransportClient client = null;
        try {
            client = new PreBuiltTransportClient(settings)
                    .addTransportAddress(new TransportAddress(InetAddress.getByName("192.168.100.241"), 9300));//集群ip
            System.out.println("ESClient连接建立成功");
        } catch (UnknownHostException e) {
            System.out.println("ESClient连接建立失败");
            e.printStackTrace();
        }
    }
}
