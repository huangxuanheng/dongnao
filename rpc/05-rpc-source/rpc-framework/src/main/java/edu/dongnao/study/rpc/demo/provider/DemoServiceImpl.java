package edu.dongnao.study.rpc.demo.provider;

import edu.dongnao.study.rpc.demo.DemoService;
import edu.dongnao.study.rpc.server.Service;

@Service(DemoService.class)
public class DemoServiceImpl implements DemoService {
	/**
	 * 代码实现
	 */
	public String sayHello(String name) {
		return "Hello " + name;
	}
}
