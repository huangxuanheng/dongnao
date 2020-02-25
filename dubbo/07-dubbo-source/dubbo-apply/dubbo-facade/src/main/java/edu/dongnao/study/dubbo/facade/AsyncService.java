package edu.dongnao.study.dubbo.facade;

import java.util.concurrent.CompletableFuture;

/**
 * AsyncService
 * 
 */
public interface AsyncService {
	CompletableFuture<String> sayHello(String name);
}

