package com.pay.account.controller;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import sun.beans.editors.DoubleEditor;
import sun.beans.editors.FloatEditor;
import sun.beans.editors.IntegerEditor;
import sun.beans.editors.LongEditor;
import util.JWebConstant;
import util.Tools;

import com.jweb.dao.Blog;
import com.jweb.dao.JWebUser;
import com.jweb.service.JWebUserService;
import com.pay.account.dao.PayBankAccountCheck;
import com.pay.account.dao.PayBankAccountSum;
import com.pay.account.service.PayBankAccountCheckFileService;
import com.pay.account.service.PayBankAccountCheckService;
@Controller
public class PayBankAccountCheckController {
    @RequestMapping("payBankAccountCheck")
    public String getPayBankAccountCheckList(HttpServletRequest request,HttpServletResponse response,
            @ModelAttribute("payBankAccountCheck") PayBankAccountCheck payBankAccountCheck) {
        JWebUser user = (JWebUser)request.getSession().getAttribute("user");
        //登录和权限判断
        if(!JWebUserService.checkUser(user,request))return "/jsp/jweb/jump_login.jsp";
        //处理标识，空：跳转、0：列表
        String flag = request.getParameter("flag");
        OutputStream os = null;
        Blog log = null;
        try {
        	log = new Blog(this.getClass().getSimpleName()+".payBankAccountCheck","获取对账记录",user.id,request);
            if(flag == null)return "/jsp/pay/account/pay_bank_account_check.jsp";
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
            log = new Blog(this.getClass().getSimpleName()+".payBankAccountCheck","查询PayBankAccountCheck列表",user.id,request);
            os.write(new PayBankAccountCheckService().getPayBankAccountCheckList(payBankAccountCheck,page,rows,
                request.getParameter("sort"),request.getParameter("order")).getBytes("utf-8"));
        } catch (Exception e) {
            e.printStackTrace();
            if(log!=null)log.info("查询列表失败,"+e.getMessage());
        } finally {
            if(os != null)try {os.close();} catch (IOException e1) {}
        }
        return null;
    }
    @RequestMapping("payBankAccountSum")
    public String payBankAccountSum(HttpServletRequest request,HttpServletResponse response,
            @ModelAttribute("payBankAccountSum") PayBankAccountSum payBankAccountSum) {
        JWebUser user = (JWebUser)request.getSession().getAttribute("user");
        //登录和权限判断
        if(!JWebUserService.checkUser(user,request))return "/jsp/jweb/jump_login.jsp";
        //处理标识，空：跳转、0：列表
        String flag = request.getParameter("flag");
        OutputStream os = null;
        Blog log = null;
        try {
        	log = new Blog(this.getClass().getSimpleName()+".payBankAccountSum","汇总银行对账记录",user.id,request);
            if(flag == null)return "/jsp/pay/account/pay_bank_account_sum.jsp";
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
            log = new Blog(this.getClass().getSimpleName()+".payBankAccountSum","获取PayBankAccountSum总金额",user.id,request);
            os.write(new PayBankAccountCheckService().payBankAccountSumList(payBankAccountSum,page,rows,
                request.getParameter("sort"),request.getParameter("order")).getBytes("utf-8"));
        } catch (Exception e) {
            e.printStackTrace();
            if(log!=null)log.info("获取总金额失败,"+e.getMessage());
        } finally {
            if(os != null)try {os.close();} catch (IOException e1) {}
        }
        return null;
    }
    @RequestMapping("updatePayBankAccountCheck")
    public String updatePayBankAccountCheck(HttpServletRequest request,HttpServletResponse response,
            @ModelAttribute("payBankAccountCheck") PayBankAccountCheck payBankAccountCheck) {
        JWebUser user = (JWebUser)request.getSession().getAttribute("user");
        OutputStream os = null;
        Blog log = null;
        try {
            //处理标识,show跳转,其他更新
            if("show".equals(request.getParameter("flag"))){
                log = new Blog(this.getClass().getSimpleName()+".updatePayBankAccountCheck","取得PayBankAccountCheck",user.id,request);
                return "/jsp/pay/account/pay_bank_account_check_update.jsp";
            }
            os = response.getOutputStream();
            request.setCharacterEncoding("UTF-8");
            response.setContentType("text/html;charset=UTF-8");
            //登录和权限判断
            if(!JWebUserService.checkUser(user,request)){
               os.write(((String)request.getSession().getAttribute("LOGIN_INFO")).getBytes());
               return null;
            }
            log = new Blog(this.getClass().getSimpleName()+".updatePayBankAccountCheck","修改PayBankAccountCheck",user.id,request);
            new PayBankAccountCheckService().updatePayBankAccountCheck(payBankAccountCheck);
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
    @RequestMapping("setResultBankAccount")
    public String setResultBankAccount(HttpServletRequest request,HttpServletResponse response,
            @ModelAttribute("payBankAccountCheck") PayBankAccountCheck payBankAccountCheck) {
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
            log = new Blog(this.getClass().getSimpleName()+".setResultBankAccount","处理结果设置PayBankAccountCheck",user.id,request);
            if(!"0".equals(payBankAccountCheck.status)
            	&&!"1".equals(payBankAccountCheck.status))throw new Exception("状态异常");
            payBankAccountCheck.lastUpdateUser=user.id;
            payBankAccountCheck.remark = payBankAccountCheck.remark.trim();
            new PayBankAccountCheckService().setResultBankAccount(payBankAccountCheck);
            os.write(JWebConstant.OK.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
            try {os.write(e.getMessage().getBytes("utf-8"));} catch (Exception e1) {}
            if(log!=null)log.info("处理结果设置失败,"+e.getMessage());
        } finally {
            if(os != null)try {os.close();} catch (IOException e1) {}
        }
        return null;
    }
    /**
     * 手动对账，文件上传
     * @param request
     * @param response
     * @param payBankAccountCheck
     * @return
     */
    @RequestMapping("manualAccountRun")
    public String manualAccountRun(HttpServletRequest request,HttpServletResponse response,@RequestParam("manualAccountFile") CommonsMultipartFile file) {
        JWebUser user = (JWebUser)request.getSession().getAttribute("user");
        OutputStream os = null;
        Blog log = null;
        try {
        	if(file.isEmpty()) throw new Exception("上传的对账文件不能为空");
            os = response.getOutputStream();
            request.setCharacterEncoding("UTF-8");
            response.setContentType("text/html;charset=UTF-8");
            //登录和权限判断
            if(!JWebUserService.checkUser(user,request)){
               os.write(((String)request.getSession().getAttribute("LOGIN_INFO")).getBytes());
               return null;
            }
            // 构建文件名称以及上传路径
            String filename = Tools.getUniqueIdentify();
            String uploadPath = request.getSession().getServletContext().getRealPath("\\upload") + "\\" +filename;
            // 文件上传操作
            File destFile = new File(uploadPath);
            FileUtils.copyInputStreamToFile(file.getInputStream(), destFile);
            //读取上传的对账文件
            new PayBankAccountCheckFileService(request.getParameter("payChannel")).readFile(uploadPath);
            os.write(JWebConstant.OK.getBytes());
            
        } catch (Exception e) {
            e.printStackTrace();
            try {os.write(e.getMessage().getBytes("utf-8"));} catch (Exception e1) {}
            if(log!=null)log.info("上传对账文件失败,"+e.getMessage());
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