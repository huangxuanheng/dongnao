package edu.dongnao.dnrpc.simple.test;

import java.lang.reflect.Method;

import org.junit.Test;

import edu.dongnao.dnrpc.simple.RpcServer;
import edu.dongnao.dnrpc.simple.example.StudentService;
import edu.dongnao.dnrpc.simple.example.StudentServiceImpl;

/**
 * ServerTest
 * 
 */
public class ServerTest {
	
	@Test
	public void startServer() {
		RpcServer server = new RpcServer();
		server.start(9998, "edu.dongnao.dnrpc.simple.example");
	}
	
	public static void main(String[] args) {
	}
}

