package com.pay.account.controller;

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
import com.pay.account.dao.PayAccEntry;
import com.pay.account.service.PayAccEntryService;
@Controller
public class PayAccEntryController {
    @RequestMapping("payAccEntry")
    public String getPayAccEntryList(HttpServletRequest request,HttpServletResponse response,
            @ModelAttribute("payAccEntry") PayAccEntry payAccEntry) {
        JWebUser user = (JWebUser)request.getSession().getAttribute("user");
        //登录和权限判断
        if(!JWebUserService.checkUser(user,request))return "/jsp/jweb/jump_login.jsp";
        //处理标识，空：跳转、0：列表
        String flag = request.getParameter("flag");
        new Blog(this.getClass().getSimpleName()+".getPayAccEntryList","取得PayAccEntry列表",user.id,request);
        OutputStream os = null;
        try {
            if(flag == null)return "/jsp/pay/accentry/pay_acc_entry.jsp";
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
            os.write(new PayAccEntryService().getPayAccEntryList(payAccEntry,page,rows,
                request.getParameter("sort"),request.getParameter("order")).getBytes("utf-8"));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(os != null)try {os.close();} catch (IOException e1) {}
        }
        return null;
    }
    @RequestMapping("addPayAccEntry")
    public String addPayAccEntry(HttpServletRequest request,HttpServletResponse response,
            @ModelAttribute("payAccEntry") PayAccEntry payAccEntry) {
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
            //使用交易码，子交易码，记账序号联合主键来判断当前要添加的信息中是否存在。
           boolean isrepeat= new PayAccEntryService().isRepeat(payAccEntry);
           
           if(isrepeat){
        	   try {os.write("记账序号重复,请重新选填".getBytes("utf-8"));} catch (Exception e1) {}
              
           }else{
        	   payAccEntry.setId(Tools.getUniqueIdentify());
               log = new Blog(this.getClass().getSimpleName()+".addPayAccEntry","添加PayAccEntry",user.id,request);
               new PayAccEntryService().addPayAccEntry(payAccEntry);
               os.write(JWebConstant.OK.getBytes());
           }
        } catch (Exception e) {
            e.printStackTrace();
            try {os.write(e.getMessage().getBytes("utf-8"));} catch (Exception e1) {}
            if(log!=null)log.info("添加失败,"+e.getMessage());
        } finally {
            if(os != null)try {os.close();} catch (IOException e1) {}
        }
        return null;
    }
    @RequestMapping("detailPayAccEntry")
    public String detailPayAccEntry(HttpServletRequest request,HttpServletResponse response) {
        JWebUser user = (JWebUser)request.getSession().getAttribute("user");
        Blog log = null;
        //登录和权限判断
        if(!JWebUserService.checkUser(user,request))return "/jsp/jweb/jump_login.jsp";
        log = new Blog(this.getClass().getSimpleName()+".detailPayAccEntry","查看PayAccEntry",user.id,request);
        try {
            request.setAttribute("payAccEntry",
                new PayAccEntryService().detailPayAccEntry(request.getParameter("txnCod")));
        } catch (Exception e) {
            e.printStackTrace();
            if(log!=null)log.info("查看失败,"+e.getMessage());
        } 
        return "/jsp/pay/accentry/pay_acc_entry_detail.jsp";
    }
    @RequestMapping("removePayAccEntry")
    public String removePayAccEntry(HttpServletRequest request,HttpServletResponse response) {
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
            log = new Blog(this.getClass().getSimpleName()+".removePayAccEntry","删除PayAccEntry",user.id,request);
            new PayAccEntryService().removePayAccEntry(request.getParameter("id"));
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
    @RequestMapping("updatePayAccEntry")
    public String updatePayAccEntry(HttpServletRequest request,HttpServletResponse response,
            @ModelAttribute("payAccEntry") PayAccEntry payAccEntry) {
        JWebUser user = (JWebUser)request.getSession().getAttribute("user");
        OutputStream os = null;
        Blog log = null;
        try {
            //处理标识,show跳转,其他更新
            if("show".equals(request.getParameter("flag"))){
                log = new Blog(this.getClass().getSimpleName()+".updatePayAccEntry","取得PayAccEntry",user.id,request);
                request.setAttribute("payAccEntryUpdate",
                    new PayAccEntryService().detailPayAccEntry(request.getParameter("id")));
                return "/jsp/pay/accentry/pay_acc_entry_update.jsp";
            }
            os = response.getOutputStream();
            request.setCharacterEncoding("UTF-8");
            response.setContentType("text/html;charset=UTF-8");
            //登录和权限判断
            if(!JWebUserService.checkUser(user,request)){
               os.write(((String)request.getSession().getAttribute("LOGIN_INFO")).getBytes());
               return null;
            }
            log = new Blog(this.getClass().getSimpleName()+".updatePayAccEntry","修改PayAccEntry",user.id,request);
            new PayAccEntryService().updatePayAccEntry(payAccEntry);
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