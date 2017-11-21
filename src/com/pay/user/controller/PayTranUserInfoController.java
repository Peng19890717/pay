package com.pay.user.controller;
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
import com.pay.user.dao.PayTranUserInfo;
import com.pay.user.service.PayAccProfileService;
import com.pay.user.service.PayTranUserInfoService;
@Controller
public class PayTranUserInfoController {
    @RequestMapping("payTranUserInfo")
    public String getPayTranUserInfoList(HttpServletRequest request,HttpServletResponse response,
            @ModelAttribute("payTranUserInfo") PayTranUserInfo payTranUserInfo) {
        JWebUser user = (JWebUser)request.getSession().getAttribute("user");
        //登录和权限判断
        if(!JWebUserService.checkUser(user,request))return "/jsp/jweb/jump_login.jsp";
        //处理标识，空：跳转、0：列表
        String flag = request.getParameter("flag");
        new Blog(this.getClass().getSimpleName()+".getPayTranUserInfoList","取得PayTranUserInfo列表",user.id,request);
        OutputStream os = null;
        try {
            if(flag == null)return "/jsp/pay/user/pay_tran_user_info.jsp";
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
            os.write(new PayTranUserInfoService().getPayTranUserInfoList(payTranUserInfo,page,rows,
                request.getParameter("sort"),request.getParameter("order")).getBytes("utf-8"));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(os != null)try {os.close();} catch (IOException e1) {}
        }
        return null;
    }
//    @RequestMapping("addPayTranUserInfo")
//    public String addPayTranUserInfo(HttpServletRequest request,HttpServletResponse response,
//            @ModelAttribute("payTranUserInfo") PayTranUserInfo payTranUserInfo) {
//        JWebUser user = (JWebUser)request.getSession().getAttribute("user");
//        OutputStream os = null;
//        Blog log = null;
//        try {
//            os = response.getOutputStream();
//            request.setCharacterEncoding("UTF-8");
//            response.setContentType("text/html;charset=UTF-8");
//            //登录和权限判断
//            if(!JWebUserService.checkUser(user,request)){
//               os.write(((String)request.getSession().getAttribute("LOGIN_INFO")).getBytes());
//               return null;
//            }
//            log = new Blog(this.getClass().getSimpleName()+".addPayTranUserInfo","添加PayTranUserInfo",user.id,request);
//            new PayTranUserInfoService().addPayTranUserInfo(payTranUserInfo);
//            os.write(JWebConstant.OK.getBytes());
//        } catch (Exception e) {
//            e.printStackTrace();
//            try {os.write(e.getMessage().getBytes("utf-8"));} catch (Exception e1) {}
//            if(log!=null)log.info("添加失败,"+e.getMessage());
//        } finally {
//            if(os != null)try {os.close();} catch (IOException e1) {}
//        }
//        return null;
//    }
    @RequestMapping("detailPayTranUserInfo")
    public String detailPayTranUserInfo(HttpServletRequest request,HttpServletResponse response) {
        JWebUser user = (JWebUser)request.getSession().getAttribute("user");
        Blog log = null;
        //登录和权限判断
        if(!JWebUserService.checkUser(user,request))return "/jsp/jweb/jump_login.jsp";
        log = new Blog(this.getClass().getSimpleName()+".detailPayTranUserInfo","查看PayTranUserInfo",user.id,request);
        try {
        	
            request.setAttribute("payTranUserInfo",
                new PayTranUserInfoService().detailPayTranUserInfo(request.getParameter("id")));
        } catch (Exception e) {
            e.printStackTrace();
            if(log!=null)log.info("查看失败,"+e.getMessage());
        } 
        return "/jsp/pay/user/pay_tran_user_info_detail.jsp";
    }
    @RequestMapping("removePayTranUserInfo")
    public String removePayTranUserInfo(HttpServletRequest request,HttpServletResponse response) {
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
            log = new Blog(this.getClass().getSimpleName()+".removePayTranUserInfo","删除PayTranUserInfo",user.id,request);
            new PayTranUserInfoService().removePayTranUserInfo(request.getParameter("id"));
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
    @RequestMapping("updatePayTranUserInfo")
    public String updatePayTranUserInfo(HttpServletRequest request,HttpServletResponse response,
            @ModelAttribute("payTranUserInfo") PayTranUserInfo payTranUserInfo) {
        JWebUser user = (JWebUser)request.getSession().getAttribute("user");
        OutputStream os = null;
        Blog log = null;
        //身份证有效期。
        String validTime = request.getParameter("validTime");
        if(validTime!=null&&!"".equalsIgnoreCase(validTime))
        {
        	if(validTime.equalsIgnoreCase("0"))//永久类型。有效期为当前时间后20年。
        	{
        		payTranUserInfo.setValidTime("29991229");
        	}
        	if(validTime.equalsIgnoreCase("1"))//选填有效期时间，
        	{
        		if(request.getParameter("in_validTime")!=null)
        		{
        			payTranUserInfo.setValidTime(request.getParameter("in_validTime").replaceAll("-", ""));
        		}
        	}
        }
        //审核时间。
        payTranUserInfo.setCheckTime(new Date());
        //审核人。
        payTranUserInfo.setCheckUserId(user.id);
        try {
            os = response.getOutputStream();
            request.setCharacterEncoding("UTF-8");
            response.setContentType("text/html;charset=UTF-8");
            //登录和权限判断
            if(!JWebUserService.checkUser(user,request)){
               os.write(((String)request.getSession().getAttribute("LOGIN_INFO")).getBytes());
               return null;
            }
            log = new Blog(this.getClass().getSimpleName()+".updatePayTranUserInfo","认证PayTranUserInfo",user.id,request);
            new PayTranUserInfoService().updatePayTranUserInfo(payTranUserInfo);
            os.write(JWebConstant.OK.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
            try {os.write(e.getMessage().getBytes("utf-8"));} catch (Exception e1) {}
            if(log!=null)log.info("认证失败,"+e.getMessage());
        } finally {
            if(os != null)try {os.close();} catch (IOException e1) {}
        }
        return null;
    }
    @RequestMapping("setUserType")
    public String setUserType(HttpServletRequest request,HttpServletResponse response,
            @ModelAttribute("payTranUserInfo") PayTranUserInfo payTranUserInfo) {
        JWebUser user = (JWebUser)request.getSession().getAttribute("user");
        OutputStream os = null;
        Blog log = null;
        String userId = request.getParameter("userId");
        String userType = request.getParameter("userType");
        try {
            os = response.getOutputStream();
            request.setCharacterEncoding("UTF-8");
            response.setContentType("text/html;charset=UTF-8");
            //登录和权限判断
            if(!JWebUserService.checkUser(user,request)){
               os.write(((String)request.getSession().getAttribute("LOGIN_INFO")).getBytes());
               return null;
            }
            log = new Blog(this.getClass().getSimpleName()+".FrozPayAccProfile","账户冻结",user.id,request);
            new PayTranUserInfoService().setUserTypebyUserId(userId,userType);
            os.write(JWebConstant.OK.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
            try {os.write(e.getMessage().getBytes("utf-8"));} catch (Exception e1) {}
            if(log!=null)log.info("冻结失败,"+e.getMessage());
        } finally {
            if(os != null)try {os.close();} catch (IOException e1) {}
        }
        return null;
    }
    @RequestMapping("FrozPayAccProfile")
    public String FrozPayAccProfile(HttpServletRequest request,HttpServletResponse response,
            @ModelAttribute("payTranUserInfo") PayTranUserInfo payTranUserInfo) {
        JWebUser user = (JWebUser)request.getSession().getAttribute("user");
        OutputStream os = null;
        Blog log = null;
        String userId = request.getParameter("userId");
        String type= request.getParameter("type");
        try {
            os = response.getOutputStream();
            request.setCharacterEncoding("UTF-8");
            response.setContentType("text/html;charset=UTF-8");
            //登录和权限判断
            if(!JWebUserService.checkUser(user,request)){
               os.write(((String)request.getSession().getAttribute("LOGIN_INFO")).getBytes());
               return null;
            }
            log = new Blog(this.getClass().getSimpleName()+".FrozPayAccProfile","账户冻结",user.id,request);
            new PayAccProfileService().ForzePayAccProfilebyUserId(userId,type);
            os.write(JWebConstant.OK.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
            try {os.write(e.getMessage().getBytes("utf-8"));} catch (Exception e1) {}
            if(log!=null)log.info("冻结失败,"+e.getMessage());
        } finally {
            if(os != null)try {os.close();} catch (IOException e1) {}
        }
        return null;
    }
    
    /**
     * 用户管理excel导出
     * @param request
     */
    @RequestMapping("payTranUserInfoExportExcel")
    public String payTranUserInfoExportExcel(HttpServletRequest request,HttpServletResponse response, @ModelAttribute("payTranUserInfo") PayTranUserInfo payTranUserInfo) {
    	
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
            //调用业务层
            log = new Blog(this.getClass().getSimpleName()+".payTranUserInfoExportExcel","导出payTranUserInfoExportExcel",user.id,request);
            PayTranUserInfoService payTranUserInfoservice = new PayTranUserInfoService();
            payTranUserInfo.province = java.net.URLDecoder.decode(payTranUserInfo.province,"UTF-8");
            payTranUserInfo.city = java.net.URLDecoder.decode(payTranUserInfo.city,"UTF-8");
            payTranUserInfo.area = java.net.URLDecoder.decode(payTranUserInfo.area,"UTF-8");
            
            os.write(payTranUserInfoservice.exportExcelList(payTranUserInfo));
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