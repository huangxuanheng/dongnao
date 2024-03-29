package edu.dongnao.study.dubbo.hystrix;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.Result;
import com.netflix.hystrix.HystrixCommand;

public class DubboHystrixCommand extends HystrixCommand<Result> {
	private Logger logger = LoggerFactory.getLogger(getClass());
	private Invoker<?> invoker;
	private Invocation invocation;
	
	protected DubboHystrixCommand(Setter setter, Invoker<?> invoker, Invocation invocation) {
		super(setter);
		this.invoker = invoker;
		this.invocation = invocation;
	}

	@Override
	protected Result run() throws Exception {
		logger.debug("通过了熔断器发起调用");
		Result result = invoker.invoke(invocation);
		logger.debug("调用成功，获得结果"+result);
		return result;
	}

}
