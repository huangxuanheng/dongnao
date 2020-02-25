package com.dongnaoedu.network.iot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.dongnaoedu.network.iot"})
public class NettyIotApplication {

    public static void main(String[] args) {
        SpringApplication.run(NettyIotApplication.class, args);
    }
}
