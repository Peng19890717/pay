package com.jweb.servlet;
import java.util.Enumeration;
import java.util.Map;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import util.JWebConstant;

import com.jweb.dao.JWebUser;

public class SessionListener implements HttpSessionListener {
	private static final Log log = LogFactory.getLog(SessionListener.class);
	/**
	 * Session创建事件
	 */
	public void sessionCreated(HttpSessionEvent se) {
		HttpSession session = se.getSession();
		//log.info("create session,id="+session.getId());
	}
	/**
	 * Session失效事件
	 */
	public void sessionDestroyed(HttpSessionEvent se) {
		//session过期，清除所有session内容
		JWebUser user = (JWebUser) se.getSession().getAttribute("user");
		if(user != null){
			Enumeration e = se.getSession().getAttributeNames();
			log.info(se.getSession().getId()+" destroyed");
			while(e.hasMoreElements())se.getSession().removeAttribute((String)e.nextElement());
			//登录次数未超限的，删除缓存用户
			if(user.loginFailCount < 
				Integer.parseInt((String)JWebConstant.SYS_CONFIG.get("LOGIN_FAIL_MAX_COUNT"))){
				Map userMap = (Map)se.getSession().getServletContext().getAttribute("USER_MAP");
				if(se.getSession().getId().equals(user.sessionId)) userMap.remove(user.id);
			}
		}
	}
}
