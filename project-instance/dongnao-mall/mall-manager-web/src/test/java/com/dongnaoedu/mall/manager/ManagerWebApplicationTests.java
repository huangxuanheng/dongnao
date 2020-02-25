package com.dongnaoedu.mall.manager;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.dongnaoedu.mall.manager.pojo.TbShiroFilter;
import com.dongnaoedu.mall.manager.service.ItemService;
import com.dongnaoedu.mall.manager.service.SystemService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ManagerWebApplicationTests {
	@Autowired
	ItemService itemService;
	@Autowired
	SystemService systemService;
	
	
	@Test
	public void test() {
		System.out.println("11111111");
//		itemService.getAllItemCount();
		
		List<TbShiroFilter> list = systemService.getShiroFilter();
		list.forEach(System.out::println);
		
	}

}
