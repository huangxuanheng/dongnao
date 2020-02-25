package com.dongnao.es_hrset_client;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;

public class InitDemo {

	public static RestHighLevelClient getClient() {

		RestHighLevelClient client = new RestHighLevelClient(
				RestClient.builder(new HttpHost("localhost", 9200, "http"),
						new HttpHost("localhost", 9201, "http")));

		return client;
	}
}
