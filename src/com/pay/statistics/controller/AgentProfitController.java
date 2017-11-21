package com.pay.statistics.controller;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import util.Tools;

import com.jweb.dao.JWebUser;
import com.jweb.service.JWebUserService;
import com.pay.statistics.service.AgentProfitService;
/**
 * 代理分润控制类
 * @author Administrator
 *
 */
@Controller
public class AgentProfitController {
	private static final Log log = LogFactory.getLog(AgentProfitController.class);
    /**
     * 代理分润报表下载
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("agentProfitTableDownload")
    public String agentProfitTableDownload(HttpServletRequest request,HttpServletResponse response) {

    	JWebUser user = (JWebUser)request.getSession().getAttribute("user");
        OutputStream os = null;
        try {
            if((request.getParameter("flag"))==null){
                log.info(user.id);
                return "/jsp/pay/statistics/agent_profit.jsp";
            }
            os = response.getOutputStream();
            //登录和权限判断
            if(!JWebUserService.checkUser(user,request)){
               os.write(((String)request.getSession().getAttribute("LOGIN_INFO")).getBytes());
               return null;
            }
            String agentProfitDateStart = request.getParameter("agentProfitDateStart");
            String agentProfitDateEnd = request.getParameter("agentProfitDateEnd");
        	byte [] b = null;
        	if("0".equals(request.getParameter("agentProfitType"))){
        		b = new AgentProfitService().getAgentProfitForTran(agentProfitDateStart,agentProfitDateEnd);
        	} else if("0".equals(request.getParameter("agentProfitType"))){
        		b = new AgentProfitService().getAgentProfitForPay(agentProfitDateStart,agentProfitDateEnd);
        	}
        	if(b!=null&&b.length>0){
        		response.setContentType("application/vnd.ms-excel");
                response.addHeader("Content-Disposition","attachment;filename="
                		+new SimpleDateFormat("yyyy-MM-dd").format(new Date())+"_"+Tools.getUniqueIdentify()+".xls");
        		os.write(b);
        	} else os.write("无记录".getBytes("utf-8"));
        	os.flush();
        } catch (Exception e) {
            e.printStackTrace();
            if(log!=null)log.info("导出失败,"+e.getMessage());
        } finally {
            if(os != null)try {os.close();} catch (IOException e1) {}
        }
        return null;
    }
}