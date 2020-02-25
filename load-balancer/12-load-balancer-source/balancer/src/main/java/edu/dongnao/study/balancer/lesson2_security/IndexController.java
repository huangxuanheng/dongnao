package edu.dongnao.study.balancer.lesson2_security;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 首页模块控制层
 * IndexController
 * 
 */
@Controller
public class IndexController {
	// 用map模拟数据库用户表账户和密码信息
	private static Map<String, String> userAccount = new HashMap<String, String>();
	static {
		userAccount.put("admin", "q2w3DDDf");
		userAccount.put("hash", "hash94Map");
	}
	
	@RequestMapping("login.html")
	public String tologin() {
		return "login.html";	// 登录失败的页面
	}
	
	/**
	 * 登录处理方法
	 * @param userName 用户名
	 * @param password 密码
	 * @param code 验证码
	 * @return 响应页面
	 */
	@RequestMapping("login")
	public String login(String userName, String password, String code) {
		// 省略验证码的处理
		// ...code验证处理
		
		// 验证用户名和密码
		String value = userAccount.get(userName);
		if(value.equals(password)) {
			return "login_success.html";	// 登录成功的页面
		}
		return "login_fail.html";	// 登录失败的页面
	}
	
	@RequestMapping("server/ip")
	@ResponseBody
	public String serverInfo(HttpServletRequest request) {
		String ip = searchIp() + " : "+request.getLocalPort();
		return "本机地址，" + ip;
	}
	
	public String searchIp() {
		Enumeration<NetworkInterface> netInterfaces = null;
		StringBuffer sb = new StringBuffer();
		try {   
		    netInterfaces = NetworkInterface.getNetworkInterfaces();
		    while (netInterfaces.hasMoreElements()) {   
		        NetworkInterface ni = netInterfaces.nextElement();   
		        System.out.println("DisplayName:" + ni.getDisplayName());
		        System.out.println("Name:" + ni.getName());  
		        Enumeration<InetAddress> ips = ni.getInetAddresses();   
		        while (ips.hasMoreElements()) {   
		        	String temp = ips.nextElement().getHostAddress();
		        	if(temp.indexOf(".")> -1 && (! temp.startsWith("127"))) {
		        		System.out.println("IP: " + temp);
		        		sb.append(temp).append(" ");
		        	}
		        }   
		    }   
		} catch (Exception e) {   
		    e.printStackTrace();   
		}
		return sb.toString();
	}
	
}

