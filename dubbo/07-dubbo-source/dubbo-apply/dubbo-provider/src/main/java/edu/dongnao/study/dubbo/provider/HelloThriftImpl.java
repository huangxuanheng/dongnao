package edu.dongnao.study.dubbo.provider;

import org.apache.thrift.TException;

import edu.dongnao.study.dubbo.thrift.HelloThrift.Iface;

/**
 * HelloThriftImpl
 * 
 */
public class HelloThriftImpl implements Iface {

	@Override
	public String sayHello(String para) throws TException {
		return "hello "+para+", this is thrift.";
	}

}

