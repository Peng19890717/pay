package com.pay.refund.controller;

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
import util.Tools;

import com.jweb.dao.Blog;
import com.jweb.dao.JWebUser;
import com.jweb.service.JWebUserService;
import com.pay.refund.dao.PayRefund;
import com.pay.refund.service.PayRefundService;
@Controller
public class PayRefundController {
    @RequestMapping("payRefund")
    public String getPayRefundList(HttpServletRequest request,HttpServletResponse response,
            @ModelAttribute("payRefund") PayRefund payRefund) {
        JWebUser user = (JWebUser)request.getSession().getAttribute("user");
        Blog log = null;
        //登录和权限判断
        if(!JWebUserService.checkUser(user,request))return "/jsp/jweb/jump_login.jsp";
        //处理标识，空：跳转、0：列表
        String flag = request.getParameter("flag");
        OutputStream os = null;
        try {
        	log = new Blog(this.getClass().getSimpleName()+".payRefund","查询PayRefund列表",user.id,request);
            if(flag == null)return "/jsp/pay/refund/pay_refund.jsp";
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
            //调用业务层
            os.write(new PayRefundService().getPayRefundList(payRefund,page,rows,
                request.getParameter("sort"),request.getParameter("order")).getBytes("utf-8"));
        } catch (Exception e) {
            e.printStackTrace();
            if(log!=null)log.info("查询列表失败,"+e.getMessage());
        } finally {
            if(os != null)try {os.close();} catch (IOException e1) {}
        }
        return null;
    }
    @RequestMapping("payRefundExportExcel")
    public String payRefundExportExcel(HttpServletRequest request,HttpServletResponse response,
            @ModelAttribute("payRefund") PayRefund payRefund) {
        JWebUser user = (JWebUser)request.getSession().getAttribute("user");
        //登录和权限判断
        if(!JWebUserService.checkUser(user,request))return "/jsp/jweb/jump_login.jsp";
        OutputStream os = null;
        Blog log = null;
        try {
            os = response.getOutputStream();
            response.setContentType("application/zip");
            response.addHeader("Content-Disposition","attachment;filename="
            		+new SimpleDateFormat("yyyy-MM-dd").format(new Date())+"_"+Tools.getUniqueIdentify()+".zip");
            log = new Blog(this.getClass().getSimpleName()+".payRefundExportExcel","导出PayRefundExcel",user.id,request);
            //调用业务层
            os.write(new PayRefundService().exportExcel(payRefund));
        } catch (Exception e) {
            e.printStackTrace();
            if(log!=null)log.info("导出失败,"+e.getMessage());
        } finally {
            if(os != null)try {os.close();} catch (IOException e1) {}
        }
        return null;
    }
    /**
     * 退款详情
     * @param request
     * @param response
     * @param payRefund
     * @return
     */
    @RequestMapping("payRefundDetail")
    public String payRefundDetail(HttpServletRequest request,HttpServletResponse response,
    		@ModelAttribute("payRefund") PayRefund payRefund) {
    	//获取session中的用户
        JWebUser user = (JWebUser)request.getSession().getAttribute("user");
        Blog log = null;
        //登录和权限判断
        if(!JWebUserService.checkUser(user,request))return "/jsp/jweb/jump_login.jsp";
        //调用业务层
        log = new Blog(this.getClass().getSimpleName()+".payRefundDetail","查询PayRefund详情",user.id,request);
        PayRefund refund = new PayRefundService().getPayRefundDetail(payRefund);
        request.setAttribute("payRefundDetail", refund);
        try {
			request.getRequestDispatcher("/jsp/pay/refund/pay_refund_detail.jsp").forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
			if(log!=null)log.info("查询详情失败,"+e.getMessage());
		}
        return null;
    }
    /**
     * 设置退款结果
     * @param request
     * @param response
     * @param payRefund
     * @return
     */
    @RequestMapping("setResultRefund")
    public String setResultSettlement(HttpServletRequest request,HttpServletResponse response,
            @ModelAttribute("payRefund") PayRefund payRefund) {
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
            log = new Blog(this.getClass().getSimpleName()+".setResultRefund","退款结果设置PayRefund",user.id,request);
            if(!"01".equals(payRefund.banksts)
            	&&!"02".equals(payRefund.banksts))throw new Exception("状态异常");
            payRefund.operId=user.id;
            payRefund.rfsake = payRefund.rfsake.trim();
            new PayRefundService().setResultRefund(payRefund);
            os.write(JWebConstant.OK.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
            try {os.write(e.getMessage().getBytes("utf-8"));} catch (Exception e1) {}
            if(log!=null)log.info("退款结果设置失败,"+e.getMessage());
        } finally {
            if(os != null)try {os.close();} catch (IOException e1) {}
        }
        return null;
    }
    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd"), true));
        binder.registerCustomEditor(int.class, new IntegerEditor());
        binder.registerCustomEditor(long.class, new LongEditor());
        binder.registerCustomEditor(float.class, new FloatEditor());
        binder.registerCustomEditor(double.class, new DoubleEditor());
    }
}