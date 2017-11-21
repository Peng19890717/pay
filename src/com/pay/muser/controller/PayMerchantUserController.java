package com.pay.muser.controller;

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
import com.pay.muser.dao.PayMerchantUser;
import com.pay.muser.service.PayMerchantUserService;
@Controller
public class PayMerchantUserController {
    @RequestMapping("payMerchantUser")
    public String getPayMerchantUserList(HttpServletRequest request,HttpServletResponse response,
            @ModelAttribute("payMerchantUser") PayMerchantUser payMerchantUser) {
        JWebUser user = (JWebUser)request.getSession().getAttribute("user");
        //登录和权限判断
        if(!JWebUserService.checkUser(user,request))return "/jsp/jweb/jump_login.jsp";
        //处理标识，空：跳转、0：列表
        String flag = request.getParameter("flag");
        OutputStream os = null;
        Blog log = null;
        try {
        	log = new Blog(this.getClass().getSimpleName()+".payMerchantUser","查询PayMerchantUser列表",user.id,request);
            if(flag == null)return "/jsp/pay/muser/pay_merchant_user.jsp";
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
            os.write(new PayMerchantUserService().getPayMerchantUserList(payMerchantUser,page,rows,
                request.getParameter("sort"),request.getParameter("order")).getBytes("utf-8"));
        } catch (Exception e) {
            e.printStackTrace();
            if(log!=null)log.info("查询列表失败,"+e.getMessage());
        } finally {
            if(os != null)try {os.close();} catch (IOException e1) {}
        }
        return null;
    }
    @RequestMapping("addPayMerchantUser")
    public String addPayMerchantUser(HttpServletRequest request,HttpServletResponse response,
            @ModelAttribute("payMerchantUser") PayMerchantUser payMerchantUser) {
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
            log = new Blog(this.getClass().getSimpleName()+".addPayMerchantUser","添加PayMerchantUser",user.id,request);
            new PayMerchantUserService().addPayMerchantUser(payMerchantUser);
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
    @RequestMapping("detailPayMerchantUser")
    public String detailPayMerchantUser(HttpServletRequest request,HttpServletResponse response) {
        JWebUser user = (JWebUser)request.getSession().getAttribute("user");
        Blog log = null;
        //登录和权限判断
        if(!JWebUserService.checkUser(user,request))return "/jsp/jweb/jump_login.jsp";
        try {
        	log = new Blog(this.getClass().getSimpleName()+".detailPayMerchantUser","查询PayMerchantUser详情",user.id,request);
            request.setAttribute("payMerchantUser",
                new PayMerchantUserService().detailPayMerchantUser(request.getParameter("userId")));
        } catch (Exception e) {
            e.printStackTrace();
            if(log!=null)log.info("查询详情失败,"+e.getMessage());
        } 
        return "/jsp/pay/muser/pay_merchant_user_detail.jsp";
    }
    @RequestMapping("removePayMerchantUser")
    public String removePayMerchantUser(HttpServletRequest request,HttpServletResponse response) {
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
            log = new Blog(this.getClass().getSimpleName()+".removePayMerchantUser","删除PayMerchantUser",user.id,request);
            new PayMerchantUserService().removePayMerchantUser(request.getParameter("userId"));
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
    @RequestMapping("updatePayMerchantUser")
    public String updatePayMerchantUser(HttpServletRequest request,HttpServletResponse response,
            @ModelAttribute("payMerchantUser") PayMerchantUser payMerchantUser) {
        JWebUser user = (JWebUser)request.getSession().getAttribute("user");
        OutputStream os = null;
        Blog log = null;
        try {
            //处理标识,show跳转,其他更新
            if("show".equals(request.getParameter("flag"))){
                log = new Blog(this.getClass().getSimpleName()+".updatePayMerchantUser","取得PayMerchantUser",user.id,request);
                request.setAttribute("payMerchantUserUpdate",
                    new PayMerchantUserService().detailPayMerchantUser(request.getParameter("userId")));
                return "/jsp/pay/muser/pay_merchant_user_update.jsp";
            }
            os = response.getOutputStream();
            request.setCharacterEncoding("UTF-8");
            response.setContentType("text/html;charset=UTF-8");
            //登录和权限判断
            if(!JWebUserService.checkUser(user,request)){
               os.write(((String)request.getSession().getAttribute("LOGIN_INFO")).getBytes());
               return null;
            }
            log = new Blog(this.getClass().getSimpleName()+".updatePayMerchantUser","修改PayMerchantUser",user.id,request);
            new PayMerchantUserService().updatePayMerchantUser(payMerchantUser);
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
    /**
     * 商户操作员禁用操作
     * @param request
     * @param response
     * @param payMerchantUser
     * @return
     */
    @RequestMapping("disabledPayMerchantUser")
    public String disabledPayMerchantUser(HttpServletRequest request,HttpServletResponse response) {
    	return this.updateStatus(request, response, "禁用");
    }
    /**
     * 商户操作员启用操作
     * @param request
     * @param response
     * @param payMerchantUser
     * @return
     */
    @RequestMapping("abledPayMerchantUser")
    public String abledPayMerchantUser(HttpServletRequest request,HttpServletResponse response) {
    	return this.updateStatus(request, response, "启用");
    }
    /**
     * 重置商户操作员密码操作
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("resetPwdPayMerchantUser")
    public String resetPwdPayMerchantUser(HttpServletRequest request,HttpServletResponse response , PayMerchantUser payMerchantUser) {
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
            log = new Blog(this.getClass().getSimpleName()+".resetPwdPayMerchantUser","重置密码PayMerchantUser",user.id,request);
            new PayMerchantUserService().updatePayMerchantUserPwd(payMerchantUser);
            os.write(JWebConstant.OK.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
            try {os.write(e.getMessage().getBytes("utf-8"));} catch (Exception e1) {}
            if(log!=null)log.info("修改密码失败,"+e.getMessage());
        } finally {
            if(os != null)try {os.close();} catch (IOException e1) {}
        }
        return null;
    }
    /**
     * 商户操作员更改
     * @param request
     * @param response
     * @param info
     * @return
     */
    private String updateStatus(HttpServletRequest request,HttpServletResponse response,String info) {
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
             log = new Blog(this.getClass().getSimpleName()+".updateStatus",info+"PayMerchantUser",user.id,request);
             new PayMerchantUserService().updatePayMerchantUserStatus(request.getParameter("userId"),request.getParameter("columName"),request.getParameter("value"));
             os.write(JWebConstant.OK.getBytes());
         } catch (Exception e) {
             e.printStackTrace();
             try {os.write(e.getMessage().getBytes("utf-8"));} catch (Exception e1) {}
             if(log!=null)log.info(info+"失败,"+e.getMessage());
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