package edu.dongnao.rental.house.provider;

import javax.servlet.annotation.WebServlet;

import org.apache.dubbo.remoting.http.servlet.DispatcherServlet;

@WebServlet(name = "dubboDispatcherServlet", urlPatterns = "/dubbo/*")
public class DubboDispatcherServlet extends DispatcherServlet {
	/** */
	private static final long serialVersionUID = 1L;
	
}
