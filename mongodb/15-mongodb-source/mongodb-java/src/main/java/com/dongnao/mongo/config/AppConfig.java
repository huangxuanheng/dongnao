package com.dongnao.mongo.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ReadPreference;
import com.mongodb.ServerAddress;
import com.mongodb.WriteConcern;

@Configuration
public class AppConfig {

  /*
   * Use the standard Mongo driver API to create a com.mongodb.MongoClient instance.
   */
	@Bean
   public MongoClient mongoClient() {
	   
		MongoCredential createCredential = 
				MongoCredential.createCredential("lison", "lison", "lison".toCharArray());
		
		WriteConcern acknowledged = WriteConcern.JOURNALED;
		MongoClientOptions mco = MongoClientOptions.builder()
				.writeConcern(acknowledged)
				.connectionsPerHost(100)
				.readPreference(ReadPreference.secondary())
				.threadsAllowedToBlockForConnectionMultiplier(5)
				.maxWaitTime(120000).connectTimeout(10000).build();
		List<ServerAddress> asList = Arrays.asList(
                new ServerAddress("192.168.1.142", 27018), 
                new ServerAddress("192.168.1.142", 27017), 
                new ServerAddress("192.168.1.142", 27019));

	   MongoClient client = new MongoClient(asList, mco);
       return client;
   }
}
