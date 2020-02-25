package com.dongnaoedu.mall.manager.shiro;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.dongnaoedu.mall.manager.pojo.TbUser;
import com.dongnaoedu.mall.manager.service.UserService;

/**
 * @author allen
 */
public class ShiroRealm extends AuthorizingRealm {

	private static final Logger log = LoggerFactory.getLogger(ShiroRealm.class);

	@Autowired
	private UserService userService;

	/**
	 * 返回权限信息
	 * 
	 * @param principal
	 * @return
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principal) {
		log.info("##################执行Shiro权限认证##################");
		// 获取用户名
		String username = principal.getPrimaryPrincipal().toString();
		if (username != null) {
			//权限信息对象info,用来存放查出的用户的所有的角色（role）及权限（permission）
			SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
			// 用户的角色集合
			info.setRoles(userService.getRoles(username));
			// 用户的权限集合
			info.setStringPermissions(userService.getPermissions(username));
			
			return info;
		}
		// 返回null的话，就会导致任何用户访问被拦截的请求时，都会自动跳转到unauthorizedUrl指定的地址
		return null;
	}

	/**
	 * 先执行登录验证
	 * 
	 * @param token
	 * @return
	 * @throws AuthenticationException
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		log.info("验证当前Subject时获取到token为：" + token.toString());
		
		String username = token.getPrincipal().toString();
		// 查出是否有此用户
		TbUser tbUser = userService.getUserByUsername(username);
		if (tbUser != null) {
            /**
             * 四个参数：
             * principal：认证的实体信息，可以是username，也可以是数据库表对应的用户的实体对象
             * credentials：数据库中的密码（经过加密的密码）
             * credentialsSalt：盐值（使用用户名）
             * realmName：当前realm对象的name，调用父类的getName()方法即可
             */
			// 盐值
			ByteSource credentialsSalt = ByteSource.Util.bytes(username);
			
			// 得到用户账号和密码存放到authenticationInfo中用于Controller层的权限判断 第三个参数随意不能为null
			AuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(
					tbUser.getUsername(),
					tbUser.getPassword(), 
					tbUser.getUsername());
			return authenticationInfo;
		} else {
			return null;
		}
	}
}
