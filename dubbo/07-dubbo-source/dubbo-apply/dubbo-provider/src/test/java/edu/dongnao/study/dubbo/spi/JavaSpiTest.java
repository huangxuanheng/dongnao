package edu.dongnao.study.dubbo.spi;

import java.util.ServiceLoader;

import org.junit.Test;

/**
 * JavaSpiTest
 * 
 */
public class JavaSpiTest {
	
	@Test
    public void sayHello() throws Exception {
        ServiceLoader<Robot> serviceLoader = ServiceLoader.load(Robot.class);
        System.out.println("Java SPI");
        serviceLoader.forEach(Robot::sayHello);
    }
}

