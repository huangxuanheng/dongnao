package edu.dongnao.dnrpc.simple.test;

import org.junit.Test;

import edu.dongnao.dnrpc.simple.RpcClientProxy;
import edu.dongnao.dnrpc.simple.example.Student;
import edu.dongnao.dnrpc.simple.example.StudentService;

/**
 * ClientTest
 * 
 */
public class ClientTest {
	
	@Test
	public void test() {
		// 本地没有接口实现，通过代理获得接口实现实例
		RpcClientProxy proxy = new RpcClientProxy("127.0.0.1", 9998);
		StudentService service = proxy.getProxy(StudentService.class);
		
		System.out.println(service.getInfo());
		
		Student student = new Student();
		student.setAge(23);
		student.setName("hashmap");
		student.setSex("男");
		System.out.println(service.printInfo(student));
	}
	
}

