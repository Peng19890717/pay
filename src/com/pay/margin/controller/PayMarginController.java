package com.pay.margin.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
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
import com.pay.margin.dao.PayMargin;
import com.pay.margin.dao.PayMarginRcvDet;
import com.pay.margin.dao.PayMarginRcvDetDAO;
import com.pay.margin.service.PayMarginService;
import com.pay.merchant.service.PayMerchantService;
@Controller
public class PayMarginController {
    @RequestMapping("payMargin")
    public String getPayMarginList(HttpServletRequest request,HttpServletResponse response,
            @ModelAttribute("payMargin") PayMargin payMargin) {
        JWebUser user = (JWebUser)request.getSession().getAttribute("user");
        //登录和权限判断
        if(!JWebUserService.checkUser(user,request))return "/jsp/jweb/jump_login.jsp";
        //处理标识，空：跳转、0：列表
        String flag = request.getParameter("flag");
        OutputStream os = null;
        Blog log = null;
        try {
        	log = new Blog(this.getClass().getSimpleName()+".payMargin","查询PayMargin列表",user.id,request);
            if(flag == null)return "/jsp/pay/margin/pay_margin.jsp";
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
            os.write(new PayMarginService().getPayMarginList(payMargin,page,rows,
                request.getParameter("sort"),request.getParameter("order")).getBytes("utf-8"));
        } catch (Exception e) {
            e.printStackTrace();
            if(log!=null)log.info("查询列表失败,"+e.getMessage());
        } finally {
            if(os != null)try {os.close();} catch (IOException e1) {}
        }
        return null;
    }
    @RequestMapping("addPayMargin")
    public String addPayMargin(HttpServletRequest request,HttpServletResponse response,
            @ModelAttribute("payMargin") PayMargin payMargin) {
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
            log = new Blog(this.getClass().getSimpleName()+".addPayMargin","添加PayMargin",user.id,request);
            //添加时判断添加的商户信息是否正确
            if(!new PayMerchantService().isExistMerchant(payMargin.custType,payMargin.custId))throw new Exception("客户不存在");
            //添加时判断添加的合同信息是否正确
            //if(!new PayContractService().isExistContract(payMargin.custId,payMargin.pactNo))throw new Exception("合同不存在");
            //判断保证金是否已经存在
//            if(new PayMarginService().isExistMargin(payMargin.custId,payMargin.pactNo))throw new Exception("此合同保证金已存在");
            if(new PayMarginService().isExistMargin(payMargin.custId))throw new Exception("客户证金已存在");
            //设置合同序列号
            payMargin.seqNo = Tools.getUniqueIdentify();
            //设置建立人
            payMargin.creOperId = user.id;
            //设置最后维护人
            payMargin.lstUptOperId = user.id;
            //调用业务层
            new PayMarginService().addPayMargin(payMargin);
            os.write(JWebConstant.OK.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
            try {os.write(e.getMessage().getBytes("utf-8"));} catch (Exception e1) {}
            if(log!=null)log.info("添加失败,"+e.getMessage());
        } finally {
            if(os != null)try {os.close();} catch (IOException e1) {}
        }
        return null;
    }
    @RequestMapping("detailPayMargin")
    public String detailPayMargin(HttpServletRequest request,HttpServletResponse response) {
        JWebUser user = (JWebUser)request.getSession().getAttribute("user");
        Blog log = null;
        //登录和权限判断
        if(!JWebUserService.checkUser(user,request))return "/jsp/jweb/jump_login.jsp";
        try {
        	log = new Blog(this.getClass().getSimpleName()+".detailPayMargin","查询PayMargin详情",user.id,request);
            PayMargin margin = new PayMarginService().detailPayMargin(request.getParameter("seqNo"));
            request.setAttribute("payMargin",margin);
            PayMarginRcvDet payMarginRcvDet=new PayMarginRcvDet();
            payMarginRcvDet.setCustId(margin.custId);
            payMarginRcvDet.setPactNo(margin.pactNo);
            log = new Blog(this.getClass().getSimpleName()+".detailPayMargin","查询PayMarginRcvDetList",user.id,request);
            request.setAttribute("payMarginRcvDetList",
            		new PayMarginRcvDetDAO().getPayMarginRcvDetListByCustId(payMarginRcvDet));
        } catch (Exception e) {
            e.printStackTrace();
            if(log!=null)log.info("查询失败,"+e.getMessage());
        } 
        return "/jsp/pay/margin/pay_margin_detail.jsp";
    }
    @RequestMapping("appendPayMargin")
    public String appendPayMargin(HttpServletRequest request,HttpServletResponse response) {
        JWebUser user = (JWebUser)request.getSession().getAttribute("user");
        //登录和权限判断
        if(!JWebUserService.checkUser(user,request))return "/jsp/jweb/jump_login.jsp";
        OutputStream os = null;
        Blog log = null;
        PayMarginService service = new PayMarginService();
        try {
        	if("show".equals(request.getParameter("flag"))){
        		request.setAttribute("payMargin",service.detailPayMargin(request.getParameter("seqNo")));
        		return "/jsp/pay/margin/pay_margin_append.jsp";
        	} else {
				os = response.getOutputStream();
				request.setCharacterEncoding("UTF-8");
				response.setContentType("text/html;charset=UTF-8");
				PayMargin payMargin = new PayMargin();
				payMargin.seqNo = request.getParameter("seqNo");
				payMargin.custId = request.getParameter("custId");
				payMargin.pactNo = request.getParameter("pactNo");
				try {
					payMargin.paidInAmt = (long)(Double.parseDouble(request.getParameter("paidInAmt"))*100);
				} catch (Exception e) {new Exception("扣缴金额错误");}
				payMargin.mark = request.getParameter("mark");
				log = new Blog(this.getClass().getSimpleName()+".appendPayMargin","扣缴保证金",user.id,request);
				service.appendPayMargin(payMargin);
				os.write(JWebConstant.OK.getBytes());
        	}
        } catch (Exception e) {
        	 e.printStackTrace();
             try {os.write(e.getMessage().getBytes("utf-8"));} catch (Exception e1) {}
             if(log!=null)log.info("续缴失败,"+e.getMessage());
        } finally {
            if(os != null)try {os.close();} catch (IOException e1) {}
        }
        return null;
    }
    @RequestMapping("removePayMargin")
    public String removePayMargin(HttpServletRequest request,HttpServletResponse response) {
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
            log = new Blog(this.getClass().getSimpleName()+".removePayMargin","删除PayMargin",user.id,request);
            new PayMarginService().removePayMargin(request.getParameter("seqNo"));
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