package com.dongnaoedu.mall.manager.shiro;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.Filter;

import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.config.Ini;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.util.CollectionUtils;
import org.apache.shiro.web.filter.mgt.DefaultFilter;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.core.RedisTemplate;

import com.dongnaoedu.mall.manager.pojo.TbShiroFilter;
import com.dongnaoedu.mall.manager.service.SystemService;

/**
 * shiro配置项
 * 
 * <pre>
 * shiro Cache交于Redis进行管理
 * shiro Session交于Spring Session进行管理
 * shiro 全面Java Config化，杜绝xml配置。
 * </pre>
 * 
 * @author allen
 */
@Configuration
@EnableCaching
public class ShiroConfiguration {
	private static final Logger log = LoggerFactory.getLogger(ShiroConfiguration.class);
	
	// 配置中的过滤链
	// public static String definitions;
	
	// 权限service，这样是null，因为spring boot的默认加载机制决定的
	// 使用构造参数，或者工具类的方式
//	@Autowired
//	private SystemService systemService;
//	@Autowired
//	private RedisTemplate redisTemplate;
	
	
	// Shiro生命周期处理器
    @Bean(name = "lifecycleBeanPostProcessor")
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }
    
    // 处理认证匹配处理器：如果自定义需要实现继承HashedCredentialsMatcher
    // 指定加密方式方式，也可以在这里加入缓存，当用户超过五次登陆错误就锁定该用户禁止不断尝试登陆
    @Bean(name = "hashedCredentialsMatcher")
    public HashedCredentialsMatcher hashedCredentialsMatcher() {
        HashedCredentialsMatcher credentialsMatcher = new HashedCredentialsMatcher();
        credentialsMatcher.setHashAlgorithmName("MD5");
        credentialsMatcher.setHashIterations(2);
        credentialsMatcher.setStoredCredentialsHexEncoded(true);
        return credentialsMatcher;
    }

    // 配置自定义Realm
	@Bean(name = "shiroRealm")
	@DependsOn(value = {"lifecycleBeanPostProcessor", "redisCacheManager"})
	public ShiroRealm shiroRealm(RedisCacheManager redisCacheManager) {
		ShiroRealm realm = new ShiroRealm();
		//realm.setCredentialsMatcher(hashedCredentialsMatcher());
		
		//realm.setCachingEnabled(true);
		//realm.setCacheManager(redisCacheManager);
		
		//realm.setAuthorizationCachingEnabled(true);
		//realm.setAuthenticationCachingEnabled(false); //禁用认证缓存
		
		return realm;
	}
	
	// 安全管理器
    @Bean(name = "securityManager")
    public DefaultWebSecurityManager securityManager(RedisCacheManager redisCacheManager){
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        // 设置realm
        securityManager.setRealm(shiroRealm(redisCacheManager));
        // 缓存，认证授权数据
        // securityManager.setCacheManager(redisCacheManager);
        // 缓存，session
        // securityManager.setSessionManager(sessionManager);
        return securityManager;
    }
    
    // Shiro过滤器 核心
    @Bean(name = "shiroFilter")
    public ShiroFilterFactoryBean shiroFilterFactoryBean(DefaultWebSecurityManager  securityManager, SystemService systemService){
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        // Shiro的核心安全接口,这个属性是必须的
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        // 身份认证失败，则跳转到登录页面的配置
        shiroFilterFactoryBean.setLoginUrl("/login");
        // 登录成功之后的 跳转页面
        shiroFilterFactoryBean.setSuccessUrl("/");
        // 权限认证失败，则跳转到指定页面
        shiroFilterFactoryBean.setUnauthorizedUrl("/403");
        
//        Map<String, Filter> filters = new LinkedHashMap<String, Filter>();
//        LogoutFilter logoutFilter = new LogoutFilter();
//        logoutFilter.setRedirectUrl("/login");
//        filters.put("logout", logoutFilter);
//        shiroFilterFactoryBean.setFilters(filters);
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        // anon 表示不需要认证以及授权
        // authc 表示需要认证 没有登录是不能进行访问的
        // perms 表示需要该权限才能访问的页面 /user/* = perms[/*]
        // roles 表示需要角色才能访问的页面 /* = roles[管理员]
/*
        Map<String, String> map = new LinkedHashMap<>();
        map.put("/logout", "logout");
        map.put("/user/**", "authc,roles[user]");
        map.put("/shop/**", "authc,roles[shop]");
        map.put("/admin/**", "authc,roles[admin]");
        map.put("/login", "anon");//anon 可以理解为不拦截
        map.put("/ajaxLogin", "anon");//anon 可以理解为不拦截
        map.put("/statistic/**",  "anon");//静态资源不拦截
        map.put("/**",  "authc,roles[user]");//其他资源全部拦截
        shiroFilterFactoryBean.setFilterChainDefinitionMap(map);
*/
        Map<String, Filter> filterMap = new LinkedHashMap<>();
        filterMap.put(DefaultFilter.perms.toString(), new WebPermissionsAuthorizationFilter());
        filterMap.put(DefaultFilter.roles.toString(), new WebPermissionsAuthorizationFilter());
        shiroFilterFactoryBean.setFilters(filterMap);
        
        // 1.从properties或yml中读取
//        StringBuilder stringBuilder = new StringBuilder();
//        urlFilterList.forEach(s -> stringBuilder.append(s).append("\n"));
//        shiroFilterFactoryBean.setFilterChainDefinitions(stringBuilder.toString());
        
        // 2.从数据库中动态读取
        String definitions = "";
        // 数据库动态权限
     	List<TbShiroFilter> list = systemService.getShiroFilter();
		for (TbShiroFilter tbShiroFilter : list) {
			// 字符串拼接权限
			definitions = definitions + tbShiroFilter.getName() + " = " + tbShiroFilter.getPerms() + "\n";
		}
		log.info(definitions);
		
		// 从配置文件加载权限配置
		Ini ini = new Ini();
		ini.load(definitions);
		Ini.Section section = ini.getSection("urls");
		if (CollectionUtils.isEmpty(section)) {
			section = ini.getSection("");
		}
		shiroFilterFactoryBean.setFilterChainDefinitionMap(section);
		
        return shiroFilterFactoryBean;
    }
    
	// 缓存管理器
    @Primary
    @Bean
	@DependsOn(value = "redisTemplate")
    public RedisCacheManager redisCacheManager(RedisTemplate redisTemplate) {
		log.info("--------------redis cache init---------------");
		RedisCacheManager redisCacheManager = new RedisCacheManager();
		redisCacheManager.setRedisTemplate(redisTemplate);
		log.info("--------------redis cache ---------------" + redisCacheManager);
		return redisCacheManager;
	}
	
	// session管理器
//	@Bean
//    public DefaultWebSessionManager shiroRedisSessionManager(RedisTemplate redisTemplateWithJdk) {
//		
//	}
    
    // 开启Shiro注解
    @Bean
    @ConditionalOnMissingBean
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator daap = new DefaultAdvisorAutoProxyCreator();
        daap.setProxyTargetClass(true);
        return daap;
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(DefaultWebSecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor aasa = new AuthorizationAttributeSourceAdvisor();
        aasa.setSecurityManager(securityManager);
        return aasa;
    }
	
}
