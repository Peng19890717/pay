package com.pay.merchant.controller;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import util.JWebConstant;

import com.jweb.dao.Blog;
import com.jweb.dao.JWebUser;
import com.jweb.service.JWebUserService;
import com.pay.merchant.dao.PayStlPeriodOptLog;
import com.pay.merchant.service.PayStlPeriodOptLogService;
@Controller
public class PayStlPeriodOptLogController {
    @RequestMapping("payStlPeriodOptLog")
    public String getPayStlPeriodOptLogList(HttpServletRequest request,HttpServletResponse response,
            @ModelAttribute("payStlPeriodOptLog") PayStlPeriodOptLog payStlPeriodOptLog) {
        JWebUser user = (JWebUser)request.getSession().getAttribute("user");
        //登录和权限判断
        if(!JWebUserService.checkUser(user,request))return "/jsp/jweb/jump_login.jsp";
        //处理标识，空：跳转、0：列表
        String flag = request.getParameter("flag");
        new Blog(this.getClass().getSimpleName()+".getPayStlPeriodOptLogList","取得PayStlPeriodOptLog列表",user.id,request);
        OutputStream os = null;
        try {
            if(flag == null)return "/jsp/pay/merchant/pay_stl_period_opt_log.jsp";
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
            os.write(new PayStlPeriodOptLogService().getPayStlPeriodOptLogList(payStlPeriodOptLog,page,rows,
                request.getParameter("sort"),request.getParameter("order")).getBytes("utf-8"));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(os != null)try {os.close();} catch (IOException e1) {}
        }
        return null;
    }
}