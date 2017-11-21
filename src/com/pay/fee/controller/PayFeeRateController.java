package com.pay.fee.controller;

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
import com.pay.fee.dao.PayFeeRate;
import com.pay.fee.service.PayFeeRateService;
@Controller
public class PayFeeRateController {
    @RequestMapping("payFeeRate")
    public String getPayFeeRateList(HttpServletRequest request,HttpServletResponse response,
            @ModelAttribute("payFeeRate") PayFeeRate payFeeRate) {
        JWebUser user = (JWebUser)request.getSession().getAttribute("user");
        //登录和权限判断
        if(!JWebUserService.checkUser(user,request))return "/jsp/jweb/jump_login.jsp";
        //处理标识，空：跳转、0：列表
        String flag = request.getParameter("flag");
        OutputStream os = null;
        Blog log = null;
        try {
        	log = new Blog(this.getClass().getSimpleName()+".payFeeRate","查询PayFeeRate列表",user.id,request);
            if(flag == null)return "/jsp/pay/fee/pay_fee_rate.jsp";
            request.setCharacterEncoding("UTF-8");
            response.setContentType("text/html;charset=UTF-8");
            os = response.getOutputStream();
            int page = 1;
            int rows = JWebConstant.WEB_RECORD_COUNT_PER_PAGE;
            try {
                rows = Integer.parseInt((String)JWebConstant.SYS_CONFIG.get("WEB_RECORD_COUNT_PER_PAGE"));//系统配置的每页记录条数
            } catch (Exception e) {
            	e.printStackTrace();
            }
            try {
                page = Integer.parseInt(request.getParameter("page"));
            } catch (Exception e1) {}
            try {
                rows = Integer.parseInt(request.getParameter("rows"));//用户设置的每页记录条数
            } catch (Exception e1) {}
            os.write(new PayFeeRateService().getPayFeeRateList(payFeeRate,page,rows,
                request.getParameter("sort"),request.getParameter("order")).getBytes("utf-8"));
        } catch (Exception e) {
            e.printStackTrace();
            if(log!=null)log.info("查询列表失败,"+e.getMessage());
        } finally {
            if(os != null)try {os.close();} catch (IOException e1) {}
        }
        return null;
    }
    
    @RequestMapping("addPayFeeRate") 
    public String addPayFeeRate(HttpServletRequest request,HttpServletResponse response,
    		@ModelAttribute("payFeeRate") PayFeeRate payFeeRate) {
        JWebUser user = (JWebUser)request.getSession().getAttribute("user");
        OutputStream os = null;
        Blog log = null;
        try {
            os = response.getOutputStream();
            request.setCharacterEncoding("UTF-8");
            response.setContentType("text/html;charset=UTF-8");
            //登录和权限判断
            if(!JWebUserService.checkUser(user,request)){
               os.write(((String)request.getSession().getAttribute("LOGIN_INFO")).getBytes());
               return null;
            }
            log = new Blog(this.getClass().getSimpleName()+".addPayFeeRate","添加PayFeeRate",user.id,request);
            payFeeRate.createUser = user.id;
	    	payFeeRate.lastUpdUser = user.id;
            os.write(new PayFeeRateService().addPayFeeRate(payFeeRate).getBytes("utf-8"));
        } catch (Exception e) {
            e.printStackTrace();
            try {os.write(e.getMessage().getBytes("utf-8"));} catch (Exception e1) {}
            if(log!=null)log.info("添加失败,"+e.getMessage());
        } finally {
            if(os != null)try {os.close();} catch (IOException e1) {}
        }
        return null;
    }
    @RequestMapping("payFeeRateDetail")
    public String payFeeRateDetail(HttpServletRequest request,HttpServletResponse response) {
        JWebUser user = (JWebUser)request.getSession().getAttribute("user");
        OutputStream os = null;
        Blog log = null;
        try {
            request.setCharacterEncoding("UTF-8");
            response.setContentType("text/html;charset=UTF-8");
            //登录和权限判断
            if(!JWebUserService.checkUser(user,request))return "/jsp/jweb/jump_login.jsp";
            log = new Blog(this.getClass().getSimpleName()+".payFeeRateDetail","查询PayFeeRate详情",user.id,request);
            PayFeeRate payFeeRate = new PayFeeRateService().getPayFeeRateById(request.getParameter("feeCode"));
            if(payFeeRate == null)throw new Exception("费率不存在");
            request.setAttribute("payFeeRate", payFeeRate);
            return "/jsp/pay/fee/pay_fee_rate_detail.jsp";
        } catch (Exception e) {
            e.printStackTrace();
            try {os = response.getOutputStream();os.write(e.getMessage().getBytes("utf-8"));} catch (Exception e1) {}
            if(log!=null)log.info("查询失败,"+e.getMessage());
        } finally {
            if(os != null)try {os.close();} catch (IOException e1) {}
        }
        return null;
    }
    @RequestMapping("removePayFeeRate")
    public String removePayFeeRate(HttpServletRequest request,HttpServletResponse response) {
        JWebUser user = (JWebUser)request.getSession().getAttribute("user");
        OutputStream os = null;
        Blog log = null;
        try {
            os = response.getOutputStream();
            request.setCharacterEncoding("UTF-8");
            response.setContentType("text/html;charset=UTF-8");
            //登录和权限判断
            if(!JWebUserService.checkUser(user,request)){
               os.write(((String)request.getSession().getAttribute("LOGIN_INFO")).getBytes());
               return null;
            }
            log = new Blog(this.getClass().getSimpleName()+".removePayFeeRate","删除PayFeeRate",user.id,request);
            new PayFeeRateService().removePayFeeRate(request.getParameter("feeCode"));
            os.write(JWebConstant.OK.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
            try {os.write(e.getMessage().getBytes("utf-8"));} catch (Exception e1) {}
            if(log!=null)log.info("删除失败,"+e.getMessage());
        } finally {
            if(os != null)try {os.close();} catch (IOException e1) {}
        }
        return null;
    }
}