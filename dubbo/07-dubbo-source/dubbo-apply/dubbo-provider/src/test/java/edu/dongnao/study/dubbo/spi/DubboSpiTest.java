package edu.dongnao.study.dubbo.spi;

import org.junit.Test;

import com.alibaba.dubbo.common.extension.ExtensionLoader;


/**
 * DubboSPITest
 * 
 */
public class DubboSpiTest {
	
	@Test
    public void sayHello() throws Exception {
        ExtensionLoader<Robot> extensionLoader = 
            ExtensionLoader.getExtensionLoader(Robot.class);
        
        Robot defautRobot = extensionLoader.getDefaultExtension();
        defautRobot.sayHello();
        
        Robot optimusPrime = extensionLoader.getExtension("optimusPrime");
        optimusPrime.sayHello();
        
        Robot bumblebee = extensionLoader.getExtension("bumblebee");
        bumblebee.sayHello();
    }
}

