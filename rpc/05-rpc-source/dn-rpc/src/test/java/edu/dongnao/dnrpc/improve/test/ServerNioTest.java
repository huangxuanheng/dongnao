package edu.dongnao.dnrpc.improve.test;

import java.io.IOException;

import org.junit.Test;

import edu.dongnao.dnrpc.improve.RpcNioServer;


/**
 * ServerTest
 * 
 */
public class ServerNioTest {
	
	@Test
	public void startServer() throws IOException {
		RpcNioServer server = new RpcNioServer(9998, "edu.dongnao.dnrpc.improve.example");
		server.start();
	}
}

