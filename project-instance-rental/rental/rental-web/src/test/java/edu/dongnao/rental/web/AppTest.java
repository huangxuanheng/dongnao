package edu.dongnao.rental.web;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@Configuration
@ActiveProfiles("dev")
public class AppTest {
    
	@Test
	public void test() throws InterruptedException {
		Thread.currentThread().join();
	}
}
