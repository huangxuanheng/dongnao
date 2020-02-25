package com.dongnaoedu.mall.manager.config;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import com.dongnaoedu.mall.common.fastdfs.FileManagerUtils;
import com.dongnaoedu.mall.manager.pojo.TbBase;
import com.dongnaoedu.mall.manager.service.SystemService;

@Configuration
public class ServletConfig {
	
	@Autowired
	InternalResourceViewResolver resolver;
	
	@Autowired
	private SystemService systemService;
	
//	@Value("${fdfs_url}")
//  private String fdfsUrl;
	
	// Spring 初始化的时候加载配置
    @PostConstruct
	public void setConfigure() {
    	Map<String, Object> map = new HashMap<>();
    	map.put("fdfsUrl", FileManagerUtils.getFilePath(null));
    	
    	TbBase base = systemService.getBase();
    	map.put("base", base);
    	
    	resolver.setAttributesMap(map);
		System.out.println(FileManagerUtils.getFilePath(null) + " 加载fastdfs服务地址，加载基础信息：" + base.toString());
		
		
	}
}
