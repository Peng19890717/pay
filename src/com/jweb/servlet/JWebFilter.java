package com.jweb.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.jweb.dao.JWebUser;

/**
 * @author HD
 */
public class JWebFilter implements Filter {
	private static final Log log = LogFactory.getLog(JWebFilter.class);
	/**
	 * 本过滤器的配置信息
	 */
	protected FilterConfig filterConfig = null;

	/**
	 * 系统缺省的语言编码
	 */
	protected String defaultencoding = null;

	/**
	 * 初始化过滤器
	 */
	public void init(FilterConfig filterConfig) throws ServletException {
		this.filterConfig = filterConfig;
		this.defaultencoding = filterConfig.getInitParameter("defaultencoding");
		this.defaultencoding = "UTF-8";
	}
	/**
	 * 过滤系统请求
	 */
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		//设置请求的语言编码
		request.setCharacterEncoding(this.defaultencoding);
		//根据用户进行功能和权限过滤
		if(checkLimit((HttpServletRequest)request,
				(HttpServletResponse)response)){
			chain.doFilter(request, response);        
		}
	}
	/**
	 * 过滤器销毁
	 */
	public void destroy() {
		this.defaultencoding = null;
		this.filterConfig = null;
	}
	/**
	 * 受权限控制的URL，并且非自己权限的，不做任何处理
	 * @param request
	 * @param response
	 * @return
	 */
	private boolean checkLimit(HttpServletRequest request,
			HttpServletResponse response) {
		JWebUser user = (JWebUser) request.getSession().getAttribute("user");
		if(user == null)return true;
		//取得访问路径
		String uri = request.getRequestURI();
		while(uri.indexOf("//")>=0)uri = uri.replaceAll("//", "/");
		String actionPath = uri.substring((request.getContextPath()+"/").length());
		//查看url是否为受权限控制的url
		Map urlMap = (Map) JWEB_ACTION_URL_MAP.get(actionPath);
		//受权限控制
		if(urlMap != null){
			//非登录用户或者非受控制的url，不做处理
			if(user.actionUrlMap.get(actionPath) == null)return false;
		}
		return true;
	}
	public static Map JWEB_ACTION_URL_MAP = new HashMap();
}
