package edu.dongnao.dnrpc.improve.test;

import org.junit.Test;

import edu.dongnao.dnrpc.improve.RpcClientProxy;
import edu.dongnao.dnrpc.improve.example.Student;
import edu.dongnao.dnrpc.improve.example.StudentService;


/**
 * ClientTest
 * 
 */
public class ClientTest {
	
	@Test
	public void test() {
		// 本地没有接口实现，通过代理获得接口实现实例
		RpcClientProxy proxy = new RpcClientProxy();
		StudentService service = proxy.getProxy(StudentService.class);
		
		System.out.println(service.getInfo());
		
		Student student = new Student();
		student.setAge(23);
		student.setName("hashmap");
		student.setSex("男");
		System.out.println(service.printInfo(student));
	}
	
}

