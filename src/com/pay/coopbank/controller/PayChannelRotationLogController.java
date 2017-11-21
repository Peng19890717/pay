package com.pay.coopbank.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import sun.beans.editors.DoubleEditor;
import sun.beans.editors.FloatEditor;
import sun.beans.editors.IntegerEditor;
import sun.beans.editors.LongEditor;
import util.JWebConstant;
import util.Tools;

import com.jweb.dao.Blog;
import com.jweb.dao.JWebUser;
import com.jweb.service.JWebUserService;
import com.pay.coopbank.dao.PayChannelRotationLog;
import com.pay.coopbank.service.PayChannelRotationLogService;
import com.pay.order.controller.PayOrderController;
import com.pay.order.service.PayOrderService;
@Controller
public class PayChannelRotationLogController {
	
	private static final Log log = LogFactory.getLog(PayChannelRotationLogController.class);
	
    @RequestMapping("payChannelRotationLog")
    public String getPayChannelRotationLogList(HttpServletRequest request,HttpServletResponse response,
            @ModelAttribute("payChannelRotationLog") PayChannelRotationLog payChannelRotationLog) {
        JWebUser user = (JWebUser)request.getSession().getAttribute("user");
        //登录和权限判断
        if(!JWebUserService.checkUser(user,request))return "/jsp/jweb/jump_login.jsp";
        //处理标识，空：跳转、0：列表
        String flag = request.getParameter("flag");
        new Blog(this.getClass().getSimpleName()+".getPayChannelRotationLogList","取得PayChannelRotationLog列表",user.id,request);
        OutputStream os = null;
        try {
            if(flag == null)return "/jsp/pay/coopbank/pay_channel_rotation_log.jsp";
            request.setCharacterEncoding("UTF-8");
            response.setContentType("text/html;charset=UTF-8");
            os = response.getOutputStream();
            int page = 1;
            int rows = JWebConstant.WEB_RECORD_COUNT_PER_PAGE;
            try {
                rows = Integer.parseInt((String)JWebConstant.SYS_CONFIG.get("WEB_RECORD_COUNT_PER_PAGE"));//系统配置的每页记录条数
            } catch (Exception e) {}
            try {
                page = Integer.parseInt(request.getParameter("page"));
            } catch (Exception e1) {}
            try {
                rows = Integer.parseInt(request.getParameter("rows"));//用户设置的每页记录条数
            } catch (Exception e1) {}
            os.write(new PayChannelRotationLogService().getPayChannelRotationLogList(payChannelRotationLog,page,rows,
                request.getParameter("sort"),request.getParameter("order")).getBytes("utf-8"));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(os != null)try {os.close();} catch (IOException e1) {}
        }
        return null;
    }
    /**
     * 导出Excel
     */
    @RequestMapping(value="payChannelRotationLogExportExcel")
    public String payChannelRotationLogExportExcel (HttpServletRequest request,HttpServletResponse response,@ModelAttribute("payChannelRotationLog") PayChannelRotationLog payChannelRotationLog) {
        JWebUser user = (JWebUser)request.getSession().getAttribute("user");
        //登录和权限判断
        if(!JWebUserService.checkUser(user,request))return "/jsp/jweb/jump_login.jsp";
        OutputStream os = null;
        try {
            os = response.getOutputStream();
            response.setContentType("application/zip");
            response.addHeader("Content-Disposition","attachment;filename="
            		+new SimpleDateFormat("yyyy-MM-dd").format(new Date())+"_"+Tools.getUniqueIdentify()+".zip");
         	//调用业务层
            log.info("渠道轮询日志:导出payChannelRotationLogExportExcel");
            String timeRange = (String)request.getSession().getAttribute("timeRange");
            os.write(new PayChannelRotationLogService().exportExcelForChannelRotationLog(timeRange,payChannelRotationLog));
        }catch (Exception e) {
        	e.printStackTrace();
            if(log!=null)log.info("导出失败,"+e.getMessage());
		}finally {
            if(os != null)try {os.close();} catch (IOException e1) {}
        }
    	return null;
    }
    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(int.class, new IntegerEditor());
        binder.registerCustomEditor(long.class, new LongEditor());
        binder.registerCustomEditor(float.class, new FloatEditor());
        binder.registerCustomEditor(double.class, new DoubleEditor());
        binder.registerCustomEditor(Date.class, new CustomDateEditor(null, true) {
            @Override  
            public void setAsText(String text) throws IllegalArgumentException {
                if(text != null && text.length()>0)
                try {
                    setValue(new SimpleDateFormat("yyyy-MM-dd").parse(text));
                } catch (Exception e) {
                    try {setValue(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(text));} catch (Exception e1) {}
                }
            }
        });
    }
}