package com.pay.risk.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import com.jweb.dao.Blog;
import com.jweb.dao.JWebUser;
import com.jweb.service.JWebUserService;
import com.pay.contract.service.PayContractService;
import com.pay.risk.dao.PayRiskMerchantLimit;
import com.pay.risk.service.PayRiskMerchantLimitService;
@Controller
public class PayRiskMerchantLimitController {
    @RequestMapping("payRiskMerchantLimit")
    public String getPayRiskMerchantLimitList(HttpServletRequest request,HttpServletResponse response,
            @ModelAttribute("payRiskMerchantLimit") PayRiskMerchantLimit payRiskMerchantLimit) {
        JWebUser user = (JWebUser)request.getSession().getAttribute("user");
        //登录和权限判断
        if(!JWebUserService.checkUser(user,request))return "/jsp/jweb/jump_login.jsp";
        //处理标识，空：跳转、0：列表
        String flag = request.getParameter("flag");
        OutputStream os = null;
        Blog log = null;
        try {
        	log=new Blog(this.getClass().getSimpleName()+".payRiskMerchantLimit","取得PayRiskMerchantLimit列表",user.id,request);
            if(flag == null)return "/jsp/pay/risk/pay_risk_merchant_limit.jsp";
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
            os.write(new PayRiskMerchantLimitService().getPayRiskMerchantLimitList(payRiskMerchantLimit,page,rows,
                request.getParameter("sort"),request.getParameter("order")).getBytes("utf-8"));
        } catch (Exception e) {
            e.printStackTrace();
            if(log!=null)log.info("查询列表失败,"+e.getMessage());
        } finally {
            if(os != null)try {os.close();} catch (IOException e1) {}
        }
        return null;
    }
    @RequestMapping("addPayRiskMerchantLimit")
    public String addPayRiskMerchantLimit(HttpServletRequest request,HttpServletResponse response,
            @ModelAttribute("payRiskMerchantLimit") PayRiskMerchantLimit payRiskMerchantLimit) {
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
            log = new Blog(this.getClass().getSimpleName()+".addPayRiskMerchantLimit","添加PayRiskMerchantLimit",user.id,request);
            payRiskMerchantLimit.createUser = user.name;
            payRiskMerchantLimit.updateUser = user.name;
            if("2".equals(payRiskMerchantLimit.limitType)){
            	//判断商户是否存在
                if(!new PayContractService().existMerchant(payRiskMerchantLimit.limitCompCode))throw new Exception("商户不存在");
            }
            new PayRiskMerchantLimitService().addPayRiskMerchantLimit(payRiskMerchantLimit);
            os.write(JWebConstant.OK.getBytes());
        } catch (Exception e) {
        	e.printStackTrace();
        	if(e.toString().contains("java.sql.SQLException: ORA-00001: 违反唯一约束条件 (PAY_DB.PAY_RISK_MERCHANT_LIMIT_PK)")) {
        		try {os.write("此限额已存在".getBytes("utf-8"));} catch (Exception e1) {} 
        	} else {
        		try {os.write(e.getMessage().getBytes("utf-8"));} catch (Exception e1) {}
        	}
        	if(log!=null)log.info("添加失败,"+e.getMessage());
        } finally {
            if(os != null)try {os.close();} catch (IOException e1) {}
        }
        return null;
    }
    @RequestMapping("detailPayRiskMerchantLimit")
    public String detailPayRiskMerchantLimit(HttpServletRequest request,HttpServletResponse response) {
        JWebUser user = (JWebUser)request.getSession().getAttribute("user");
        //登录和权限判断
        if(!JWebUserService.checkUser(user,request))return "/jsp/jweb/jump_login.jsp";
        try {
            request.setAttribute("payRiskMerchantLimit",
                new PayRiskMerchantLimitService().detailPayRiskMerchantLimit(request.getParameter("id")));
        } catch (Exception e) {
            e.printStackTrace();
        } 
        return "/jsp/pay/risk/pay_risk_merchant_limit_detail.jsp";
    }
    @RequestMapping("removePayRiskMerchantLimit")
    public String removePayRiskMerchantLimit(HttpServletRequest request,HttpServletResponse response) {
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
            log = new Blog(this.getClass().getSimpleName()+".removePayRiskMerchantLimit","删除PayRiskMerchantLimit",user.id,request);
            new PayRiskMerchantLimitService().removePayRiskMerchantLimit(request.getParameter("id"));
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
    @RequestMapping("updatePayRiskMerchantLimit")
    public String updatePayRiskMerchantLimit(HttpServletRequest request,HttpServletResponse response,
            @ModelAttribute("payRiskMerchantLimit") PayRiskMerchantLimit payRiskMerchantLimit) {
        JWebUser user = (JWebUser)request.getSession().getAttribute("user");
        OutputStream os = null;
        Blog log = null;
        try {
            //处理标识,show跳转,其他更新
            if("show".equals(request.getParameter("flag"))){
                log = new Blog(this.getClass().getSimpleName()+".updatePayRiskMerchantLimit","取得PayRiskMerchantLimit",user.id,request);
                request.setAttribute("payRiskMerchantLimitUpdate",
                    new PayRiskMerchantLimitService().detailPayRiskMerchantLimit(request.getParameter("id")));
                return "/jsp/pay/risk/pay_risk_merchant_limit_update.jsp";
            }
            os = response.getOutputStream();
            request.setCharacterEncoding("UTF-8");
            response.setContentType("text/html;charset=UTF-8");
            //登录和权限判断
            if(!JWebUserService.checkUser(user,request)){
               os.write(((String)request.getSession().getAttribute("LOGIN_INFO")).getBytes());
               return null;
            }
            log = new Blog(this.getClass().getSimpleName()+".updatePayRiskMerchantLimit","修改PayRiskMerchantLimit",user.id,request);
            payRiskMerchantLimit.updateUser = user.name;
            payRiskMerchantLimit.updateTime = new Date();
            if("2".equals(payRiskMerchantLimit.limitType)){
            	//判断商户是否存在
                if(!new PayContractService().existMerchant(payRiskMerchantLimit.limitCompCode))throw new Exception("商户不存在");
            }
            new PayRiskMerchantLimitService().updatePayRiskMerchantLimit(payRiskMerchantLimit);
            os.write(JWebConstant.OK.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
            try {os.write(e.getMessage().getBytes("utf-8"));} catch (Exception e1) {}
            if(log!=null)log.info("修改失败,"+e.getMessage());
        } finally {
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