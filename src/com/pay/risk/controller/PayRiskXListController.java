package com.pay.risk.controller;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
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
import com.pay.account.service.PayBankAccountCheckFileService;
import com.pay.contract.service.PayContractService;
import com.pay.refund.dao.PayRefund;
import com.pay.refund.service.PayRefundService;
import com.pay.risk.dao.PayRiskXList;
import com.pay.risk.service.PayRiskXListService;
@Controller
public class PayRiskXListController {
    @RequestMapping("payRiskXList")
    public String getPayRiskXListList(HttpServletRequest request,HttpServletResponse response,
            @ModelAttribute("payRiskXList") PayRiskXList payRiskXList) {
        JWebUser user = (JWebUser)request.getSession().getAttribute("user");
        //登录和权限判断
        if(!JWebUserService.checkUser(user,request))return "/jsp/jweb/jump_login.jsp";
        //处理标识，空：跳转、0：列表
        String flag = request.getParameter("flag");
        OutputStream os = null;
        Blog log = null;
        try {
        	log=new Blog(this.getClass().getSimpleName()+".payRiskXList","取得PayRiskXList列表",user.id,request);
            if(flag == null)return "/jsp/pay/risk/pay_risk_x_list.jsp";
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
                if(page==0)page=1;
            } catch (Exception e1) {e1.printStackTrace();}
            try {
                rows = Integer.parseInt(request.getParameter("rows"));//用户设置的每页记录条数
            } catch (Exception e1) {}
            os.write(new PayRiskXListService().getPayRiskXListList(payRiskXList,page,rows,
                request.getParameter("sort"),request.getParameter("order")).getBytes("utf-8"));
        } catch (Exception e) {
            e.printStackTrace();
            if(log!=null)log.info("查询列表失败,"+e.getMessage());
        } finally {
            if(os != null)try {os.close();} catch (IOException e1) {}
        }
        return null;
    }
    @RequestMapping("addPayRiskXList")
    public String addPayRiskXList(HttpServletRequest request,HttpServletResponse response,
            @ModelAttribute("payRiskXList") PayRiskXList payRiskXList) {
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
            log = new Blog(this.getClass().getSimpleName()+".addPayRiskXList","添加PayRiskXList",user.id,request);
            payRiskXList.setCreateUser(user.getId());
            if(com.PayConstant.CUST_TYPE_USER.equals(payRiskXList.clientType));
            else if(com.PayConstant.CUST_TYPE_MERCHANT.equals(payRiskXList.clientType)){
            	//判断商户是否存在
                if(!new PayContractService().existMerchant(payRiskXList.clientCode))throw new Exception("商户不存在");
            }else if(com.PayConstant.CUST_TYPE_MOBLIE.equals(payRiskXList.clientType)){
            	//判断手机号是否存在 是否增加到 x 名单里
                if(new PayRiskXListService().isExistPayRiskXList(com.PayConstant.CUST_TYPE_MOBLIE, payRiskXList.clientType))throw new Exception("手机号已存在");
            }else if(com.PayConstant.CUST_TYPE_CARD.equals(payRiskXList.clientType)){
            	//判断卡号是否存在 是否增加到 x 名单里
            	if(new PayRiskXListService().isExistPayRiskXList(com.PayConstant.CUST_TYPE_CARD, payRiskXList.clientType))throw new Exception("银行卡号已存在");
            }
            new PayRiskXListService().addPayRiskXList(payRiskXList);
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
    @RequestMapping("updatePayRiskXList")
    public String updatePayRiskXList(HttpServletRequest request,HttpServletResponse response,
            @ModelAttribute("payRiskXList") PayRiskXList payRiskXList) {
        JWebUser user = (JWebUser)request.getSession().getAttribute("user");
        payRiskXList.setUpdateUser(user.id);//设置维护人
        payRiskXList.setUpdateDatetime(new Date());//设置维护时间
        payRiskXList.setRegdtTime(new Date());//设置登陆时间
        OutputStream os = null;
        Blog log = null;
        try {
            //处理标识,show跳转,其他更新
            if("show".equals(request.getParameter("flag"))){
                log = new Blog(this.getClass().getSimpleName()+".updatePayRiskXList","取得PayRiskXList",user.id,request);
                request.setAttribute("payRiskXListUpdate",
                    new PayRiskXListService().detailPayRiskXList(request.getParameter("id")));
                return "/jsp/pay/risk/pay_risk_x_list_update.jsp";
            }
            os = response.getOutputStream();
            request.setCharacterEncoding("UTF-8");
            response.setContentType("text/html;charset=UTF-8");
            //登录和权限判断
            if(!JWebUserService.checkUser(user,request)){
               os.write(((String)request.getSession().getAttribute("LOGIN_INFO")).getBytes());
               return null;
            }
            log = new Blog(this.getClass().getSimpleName()+".updatePayRiskXList","修改PayRiskXList",user.id,request);
            new PayRiskXListService().updatePayRiskXList(payRiskXList);
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
     * 批量导入
     * @param request
     * @param response
     * @param file
     * @return
     */
    @RequestMapping("manualxListRun")
    public String manualAccountRun(HttpServletRequest request,HttpServletResponse response,@RequestParam("manualxListFile") CommonsMultipartFile file) {
        JWebUser user = (JWebUser)request.getSession().getAttribute("user");
        OutputStream os = null;
        Blog log = null;
        try {
        	if(file.isEmpty()) throw new Exception("导入的文件不能为空");
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
            // 读取上传的文件
            new PayRiskXListService().readxListFile(uploadPath);
            os.write(JWebConstant.OK.getBytes());
            
        } catch (Exception e) {
            e.printStackTrace();
            try {os.write(e.getMessage().getBytes("utf-8"));} catch (Exception e1) {}
            if(log!=null)log.info("导入文件失败,"+e.getMessage());
        } finally {
            if(os != null)try {os.close();} catch (IOException e1) {}
        }
        return null;
    }
    /**
     * 下载excel模版
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("payXListExportExcel")
    public String payXListExportExcel(HttpServletRequest request,HttpServletResponse response) {
        JWebUser user = (JWebUser)request.getSession().getAttribute("user");
        //登录和权限判断
        if(!JWebUserService.checkUser(user,request))return "/jsp/jweb/jump_login.jsp";
        Blog log = null;
        OutputStream os = null;
        try {
        	os = response.getOutputStream();
            log = new Blog(this.getClass().getSimpleName()+".payXListExportExcel","模版下载",user.id,request);
            //调用业务层
            new PayRiskXListService().exportExcel(request,response,os);
        } catch (Exception e) {
            e.printStackTrace();
            if(log!=null)log.info("模版下载失败,"+e.getMessage());
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