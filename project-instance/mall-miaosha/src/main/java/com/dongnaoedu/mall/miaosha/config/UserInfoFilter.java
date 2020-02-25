/**
 * 
 */
package com.dongnaoedu.mall.miaosha.config;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.jasig.cas.client.validation.AbstractTicketValidationFilter;
import org.jasig.cas.client.validation.Assertion;

/**
 * 这里的作用是，免去了每次都去session里面那userId的麻烦
 * 
 * @author 动脑学院.Tony老师
 * @see 专注在职IT人员能力提升，咨询顾问QQ: 2729 772 006
 */
public class UserInfoFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HashMap m = new HashMap(request.getParameterMap());
        // 获取登录信息，并且设置为userId参数
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        Object attribute = httpServletRequest.getSession()
                .getAttribute(AbstractTicketValidationFilter.CONST_CAS_ASSERTION);
        if (attribute == null) {
            chain.doFilter(request, response);
        } else {
            Assertion assertion = (Assertion) attribute;
            String userId = assertion.getPrincipal().getName();
            m.put("userId", new String[] { userId }); // 这就是对的
            HttpServletRequest req = (HttpServletRequest) request;
            ParameterRequestWrapper wrapRequest = new ParameterRequestWrapper(req, m);
            request = wrapRequest;
            chain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {
    }

}
