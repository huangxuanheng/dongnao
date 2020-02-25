package com.study.mike.spring.mvc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.study.mike.spring.mvc.handler.BeanNameUrlHandlerMapping;
import com.study.mike.spring.mvc.handler.HandlerAdapter;
import com.study.mike.spring.mvc.handler.HandlerMapping;
import com.study.mike.spring.mvc.handler.SimpleControllerHandlerAdapter;
import com.study.mike.spring.mvc.mvc.ModelAndView;
import com.study.mike.spring.mvc.mvc.View;

public class DispatcherServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = -560428787709797085L;

	private static final String CONTEXT_CONFIG_LOCATION_PARAM_NAME = "contextConfigLocation";

	private static final String ROOT_APPLICATION_CONTEXT_ATTRIBUTE_NAME = "root_application_context";
	private static final String WEB_APPLICATION_CONTEXT_ATTRIBUTE_NAME = "web_application_context";

	protected final Log logger = LogFactory.getLog(getClass());

	private GenericXmlApplicationContext webApplicationContext;

	private List<HandlerMapping> handlerMappings;

	private List<HandlerAdapter> handlerAdapters;

	public void init() throws ServletException {
		String contextConfigLocation = this.getInitParameter(CONTEXT_CONFIG_LOCATION_PARAM_NAME);
		if (StringUtils.isEmpty(contextConfigLocation)) {
			contextConfigLocation = this.getServletContext().getInitParameter(CONTEXT_CONFIG_LOCATION_PARAM_NAME);
		}

		if (StringUtils.isEmpty(contextConfigLocation)) {
			throw new ServletException("没有 spring mvc指定[" + CONTEXT_CONFIG_LOCATION_PARAM_NAME + "]参数");
		}

		this.webApplicationContext = new GenericXmlApplicationContext();
		// 设置它的父容器，如果有放在ServletContext中root容器
		this.webApplicationContext.setParent(
				(ApplicationContext) this.getServletContext().getAttribute(ROOT_APPLICATION_CONTEXT_ATTRIBUTE_NAME));
		// 加载xml Bean定义配置
		this.webApplicationContext.load(contextConfigLocation);
		// 刷新
		this.webApplicationContext.refresh();

		// 将webApplicationContext也放入ServletContext中
		this.getServletContext().setAttribute(WEB_APPLICATION_CONTEXT_ATTRIBUTE_NAME, webApplicationContext);

		// 初始化 MVC相关组件
		initStrategies(webApplicationContext);
	}

	/**
	 * 初始化 MVC相关组件的策略提供者，从applicationContext中获取
	 * 
	 * @param applicationContext
	 */
	private void initStrategies(ApplicationContext applicationContext) {
		// 1、initHandlerMapping
		initHandlerMappings(applicationContext);

		// 2、initHandlerAdapter
		initHandlerAdapters(applicationContext);

	}

	private void initHandlerAdapters(ApplicationContext applicationContext) {
		// 查找applicationContext中所有的HandlerMapping类型的Bean
		Map<String, HandlerAdapter> matchingBeans = BeanFactoryUtils.beansOfTypeIncludingAncestors(applicationContext,
				HandlerAdapter.class, true, false);
		if (!matchingBeans.isEmpty()) {
			this.handlerAdapters = new ArrayList<>(matchingBeans.values());
		}

		if (CollectionUtils.isEmpty(this.handlerAdapters)) {
			// 初始化为默认的
			SimpleControllerHandlerAdapter df = new SimpleControllerHandlerAdapter();
			this.handlerAdapters = Collections.singletonList(df);
		}

	}

	private void initHandlerMappings(ApplicationContext applicationContext) {
		// 查找applicationContext中所有的HandlerMapping类型的Bean
		Map<String, HandlerMapping> matchingBeans = BeanFactoryUtils.beansOfTypeIncludingAncestors(applicationContext,
				HandlerMapping.class, true, false);
		if (!matchingBeans.isEmpty()) {
			this.handlerMappings = new ArrayList<>(matchingBeans.values());
			// We keep HandlerMappings in sorted order.
			AnnotationAwareOrderComparator.sort(this.handlerMappings);
		}

		if (CollectionUtils.isEmpty(this.handlerMappings)) {
			// 初始化为默认的
			BeanNameUrlHandlerMapping df = new BeanNameUrlHandlerMapping();
			df.setApplicationContext(applicationContext);
			this.handlerMappings = Collections.singletonList(df);
		}

	}

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 在这里可把一些Dispatcher持有的对象放入到Request中，以被后续处理过程中可能需要使用到
		req.setAttribute(WEB_APPLICATION_CONTEXT_ATTRIBUTE_NAME, webApplicationContext);

		this.doDispatch(req, resp);

	}

	private void doDispatch(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Object handler = null;
		ModelAndView mv = null;
		Exception dispatchException = null;
		try {
			// 1、获取请求对应的handler
			handler = this.getHandler(req);
			// 2、如果没有对应的handler
			if (handler == null) {
				noHandlerFound(req, resp);
				return;
			}
			// 3、如果有对应的handler，获得handler的Adapter

			HandlerAdapter ha = this.getHandlerAdapter(handler);

			// 4、执行adapter
			mv = ha.handle(req, resp, handler);

		} catch (Exception e) {
			dispatchException = e;
		}
		// 5、转发给view
		processDispatchResult(req, resp, handler, mv, dispatchException);
	}

	private void processDispatchResult(HttpServletRequest req, HttpServletResponse resp, Object handler,
			ModelAndView mv, Exception dispatchException) throws ServletException {
		// 1、 如果有异常,生成对应的异常视图
		if (dispatchException != null) {
			mv = doProcessException(req, resp, handler, dispatchException);
		}
		// 2、渲染视图
		render(req, resp, mv);

	}

	private ModelAndView doProcessException(HttpServletRequest req, HttpServletResponse resp, Object handler,
			Exception dispatchException) {
		// TODO Auto-generated method stub
		return null;
	}

	private void render(HttpServletRequest req, HttpServletResponse resp, ModelAndView mv) throws ServletException {
		// 1、判断MV的view是否是一个viewName，如是转为对应的view
		View view;
		String viewName = mv.getViewName();
		if (viewName != null) {
			// 将viewName转为对应的view.
			view = resolveViewName(viewName, mv.getModel(), req);
			if (view == null) {
				throw new ServletException("Could not resolve view with name '" + mv.getViewName()
						+ "' in servlet with name '" + getServletName() + "'");
			}
		} else {
			// 是一个真正的view对象
			view = mv.getView();
			if (view == null) {
				throw new ServletException("ModelAndView [" + mv + "] neither contains a view name nor a "
						+ "View object in servlet with name '" + getServletName() + "'");
			}
		}

		try {
			if (mv.getStatus() != null) {
				resp.setStatus(mv.getStatus().value());
			}
			view.render(mv.getModel(), req, resp);
		} catch (Exception ex) {
			if (logger.isDebugEnabled()) {
				logger.debug("Error rendering view [" + view + "]", ex);
			}
			throw ex;
		}
	}

	private View resolveViewName(String viewName, Map<String, Object> model, HttpServletRequest req) {
		// TODO Auto-generated method stub
		return null;
	}

	private HandlerAdapter getHandlerAdapter(Object handler) {
		for (HandlerAdapter ha : this.handlerAdapters) {
			if (ha.supports(handler)) {
				return ha;
			}
		}
		return null;
	}

	private void noHandlerFound(HttpServletRequest req, HttpServletResponse response) throws IOException {
		// TODO 如果没有特定的处理规则，就直接404了
		response.sendError(HttpServletResponse.SC_NOT_FOUND);

	}

	private Object getHandler(HttpServletRequest req) throws Exception {
		Object handler = null;
		for (HandlerMapping hm : this.handlerMappings) {
			handler = hm.getHandler(req);
			if (handler != null) {
				return handler;
			}
		}
		return null;
	}

	@Override
	public void destroy() {
		this.webApplicationContext.close();
	}
}
