package com.pay.user.controller;

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
import com.pay.user.dao.PaySalesmanMerchantRelation;
import com.pay.user.service.PaySalesmanMerchantRelationService;
@Controller
public class PaySalesmanMerchantRelationController {
	private static final Log log = LogFactory.getLog(PaySalesmanMerchantRelationController .class);
    @RequestMapping("paySalesmanMerchantRelation")
    public String getPaySalesmanMerchantRelationList(HttpServletRequest request,HttpServletResponse response,
            @ModelAttribute("paySalesmanMerchantRelation") PaySalesmanMerchantRelation paySalesmanMerchantRelation) {
        JWebUser user = (JWebUser)request.getSession().getAttribute("user");
        //登录和权限判断
        if(!JWebUserService.checkUser(user,request))return "/jsp/jweb/jump_login.jsp";
        //处理标识，空：跳转、0：列表
        String flag = request.getParameter("flag");
        new Blog(this.getClass().getSimpleName()+".getPaySalesmanMerchantRelationList","取得PaySalesmanMerchantRelation列表",user.id,request);
        OutputStream os = null;
        try {
            if(flag == null)return "/jsp/pay/user/pay_salesman_merchant_relation.jsp";
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
            os.write(new PaySalesmanMerchantRelationService().getPaySalesmanMerchantRelationList(paySalesmanMerchantRelation,page,rows,
                request.getParameter("sort"),request.getParameter("order")).getBytes("utf-8"));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(os != null)try {os.close();} catch (IOException e1) {}
        }
        return null;
    }
    /**
     * 查看我的商户
     * @param request
     * @param response
     * @param paySalesmanMerchantRelation
     * @return
     */
    @RequestMapping("myMerchants")
    public String myMerchants(HttpServletRequest request,HttpServletResponse response) {
    	JWebUser user = (JWebUser)request.getSession().getAttribute("user");
    	//登录和权限判断
    	if(!JWebUserService.checkUser(user,request))return "/jsp/jweb/jump_login.jsp";
    	//处理标识，空：跳转、0：列表
    	String flag = request.getParameter("flag");
    	new Blog(this.getClass().getSimpleName()+".getPaySalesmanMerchantRelationList","取得PaySalesmanMerchantRelation列表",user.id,request);
    	OutputStream os = null;
    	try {
    		if(flag == null)return "/jsp/pay/user/pay_saleman_merchants.jsp";
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
    		os.write(new PaySalesmanMerchantRelationService().getMersByUid(user.id,page,rows,
    				request.getParameter("sort"),request.getParameter("order")).getBytes("utf-8"));
    	} catch (Exception e) {
    		e.printStackTrace();
    	} finally {
    		if(os != null)try {os.close();} catch (IOException e1) {}
    	}
    	return null;
    }
    @RequestMapping("addPaySalesmanMerchantRelation")
    public String addPaySalesmanMerchantRelation(HttpServletRequest request,HttpServletResponse response,
            @ModelAttribute("paySalesmanMerchantRelation") PaySalesmanMerchantRelation paySalesmanMerchantRelation) {
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
            log = new Blog(this.getClass().getSimpleName()+".addPaySalesmanMerchantRelation","添加PaySalesmanMerchantRelation",user.id,request);
            new PaySalesmanMerchantRelationService().addPaySalesmanMerchantRelation(paySalesmanMerchantRelation);
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
    @RequestMapping("removePaySalesmanMerchantRelation")
    public String removePaySalesmanMerchantRelation(HttpServletRequest request,HttpServletResponse response) {
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
            log = new Blog(this.getClass().getSimpleName()+".removePaySalesmanMerchantRelation","删除PaySalesmanMerchantRelation",user.id,request);
            new PaySalesmanMerchantRelationService().removePaySalesmanMerchantRelation(request.getParameter("merNo"));
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
    /**
     * 业务员商户导出
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("paySalesmanMerchatRelationExportExcel")
    public String paySalesmanMerchatRelationExportExcel(HttpServletRequest request,HttpServletResponse response,@ModelAttribute("paySalesmanMerchantRelation") PaySalesmanMerchantRelation paySalesmanMerchantRelation){

    	JWebUser user = (JWebUser)request.getSession().getAttribute("user");
        OutputStream os = null;
        System.out.println("flag==="+request.getParameter("flag"));
        try {
            if((request.getParameter("flag"))==null){
                log.info(user.id);
                return "/jsp/pay/user/pay_salesman_merchant_relation.jsp";
            }
            os = response.getOutputStream();
            //登录和权限判断
            if(!JWebUserService.checkUser(user,request)){
               os.write(((String)request.getSession().getAttribute("LOGIN_INFO")).getBytes());
               return null;
            }
        	byte [] b = null;
        		b = new PaySalesmanMerchantRelationService().getPaySalesmanMerchantRelation(paySalesmanMerchantRelation);        	
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