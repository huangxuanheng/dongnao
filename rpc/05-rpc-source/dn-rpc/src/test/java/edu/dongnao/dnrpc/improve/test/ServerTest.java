package edu.dongnao.dnrpc.improve.test;

import org.junit.Test;

import edu.dongnao.dnrpc.improve.RpcServer;


/**
 * ServerTest
 * 
 */
public class ServerTest {
	
	@Test
	public void startServer() {
		RpcServer server = new RpcServer();
		server.start(9998, "edu.dongnao.dnrpc.improve.example");
	}
}

