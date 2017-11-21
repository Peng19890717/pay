package com.pay.coopbank.controller;

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
import com.pay.coopbank.dao.PayChannelRotation;
import com.pay.coopbank.service.PayChannelRotationService;
import com.pay.merchantinterface.service.PayRequest;
import com.pay.order.dao.PayOrder;
@Controller
public class PayChannelRotationController {
	private static final Log log = LogFactory.getLog(PayChannelRotationController.class);
    @RequestMapping("payChannelRotation")
    public String getPayChannelRotationList(HttpServletRequest request,HttpServletResponse response,
            @ModelAttribute("payChannelRotation") PayChannelRotation payChannelRotation) {
        JWebUser user = (JWebUser)request.getSession().getAttribute("user");
        //登录和权限判断
        if(!JWebUserService.checkUser(user,request))return "/jsp/jweb/jump_login.jsp";
        //处理标识，空：跳转、0：列表
        String flag = request.getParameter("flag");
        new Blog(this.getClass().getSimpleName()+".getPayChannelRotationList","取得PayChannelRotation列表",user.id,request);
        OutputStream os = null;
        try {
            if(flag == null)return "/jsp/pay/coopbank/pay_channel_rotation.jsp";
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
            os.write(new PayChannelRotationService().getPayChannelRotationList(payChannelRotation,page,rows,
                request.getParameter("sort"),request.getParameter("order")).getBytes("utf-8"));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(os != null)try {os.close();} catch (IOException e1) {}
        }
        return null;
    }
    @RequestMapping("addPayChannelRotation")
    public String addPayChannelRotation(HttpServletRequest request,HttpServletResponse response,
            @ModelAttribute("payChannelRotation") PayChannelRotation payChannelRotation) {
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
            log = new Blog(this.getClass().getSimpleName()+".addPayChannelRotation","添加PayChannelRotation",user.id,request);
            new PayChannelRotationService().addPayChannelRotation(request);
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
    @RequestMapping("detailPayChannelRotation")
    public String detailPayChannelRotation(HttpServletRequest request,HttpServletResponse response) {
        JWebUser user = (JWebUser)request.getSession().getAttribute("user");
        Blog log = null;
        //登录和权限判断
        if(!JWebUserService.checkUser(user,request))return "/jsp/jweb/jump_login.jsp";
        log = new Blog(this.getClass().getSimpleName()+".detailPayChannelRotation","查看PayChannelRotation",user.id,request);
        try {
            request.setAttribute("payChannelRotation",
                new PayChannelRotationService().detailPayChannelRotation(request.getParameter("id")));
        } catch (Exception e) {
            e.printStackTrace();
            if(log!=null)log.info("查看失败,"+e.getMessage());
        } 
        return "/jsp/pay/coopbank/pay_channel_rotation_detail.jsp";
    }
    @RequestMapping("getChannelMaxLimitAmt")
    public String getChannelMaxLimitAmt(HttpServletRequest request,HttpServletResponse response) {
        JWebUser user = (JWebUser)request.getSession().getAttribute("user");
        OutputStream os = null;
        try {
            os = response.getOutputStream();
            request.setCharacterEncoding("UTF-8");
            response.setContentType("text/html;charset=UTF-8");
            //登录和权限判断
            if(!JWebUserService.checkUser(user,request)){
               os.write(((String)request.getSession().getAttribute("LOGIN_INFO")).getBytes());
               return null;
            }
            os.write(String.valueOf(new PayChannelRotationService().
            		getChannelMaxLimitAmt(request.getParameter("channelId"))/100).getBytes());
        } catch (Exception e) {
            e.printStackTrace();
            try {os.write(e.getMessage().getBytes("utf-8"));} catch (Exception e1) {}
            if(log!=null)log.info("获取最大额失败,"+e.getMessage());
        } finally {
            if(os != null)try {os.close();} catch (IOException e1) {}
        }
        return null;
    }
    @RequestMapping("rotationRuleTest")
    public String rotationRuleTest(HttpServletRequest request,HttpServletResponse response) {
        JWebUser user = (JWebUser)request.getSession().getAttribute("user");
        OutputStream os = null;
        try {
            os = response.getOutputStream();
            request.setCharacterEncoding("UTF-8");
            response.setContentType("text/html;charset=UTF-8");
            //登录和权限判断
            if(!JWebUserService.checkUser(user,request)){
               os.write(((String)request.getSession().getAttribute("LOGIN_INFO")).getBytes());
               return null;
            }
            PayRequest payRequest = new PayRequest();
            payRequest.payOrder = new PayOrder();
            payRequest.payOrder.txamt = Long.parseLong(request.getParameter("ruleTestAmt"))*100l;
            payRequest.application=request.getParameter("ruleNo");
            os.write(new PayChannelRotationService().getPayChannelRotationByRule(payRequest).toString().getBytes());
        } catch (Exception e) {
            e.printStackTrace();
            try {os.write(e.getMessage().getBytes("utf-8"));} catch (Exception e1) {}
            if(log!=null)log.info("获取最大额失败,"+e.getMessage());
        } finally {
            if(os != null)try {os.close();} catch (IOException e1) {}
        }
        return null;
    }
    @RequestMapping("updatePayChannelRotationStatus")
    public String updatePayChannelRotationStatus(HttpServletRequest request,HttpServletResponse response) {
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
            log = new Blog(this.getClass().getSimpleName()+".removePayChannelRotation","操作PayChannelRotation",user.id,request);
            new PayChannelRotationService().updatePayChannelRotationStatus(request.getParameter("id"),request.getParameter("status"));
            os.write(JWebConstant.OK.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
            try {os.write(e.getMessage().getBytes("utf-8"));} catch (Exception e1) {}
            if(log!=null)log.info("状态更新失败,"+e.getMessage());
        } finally {
            if(os != null)try {os.close();} catch (IOException e1) {}
        }
        return null;
    }
    @RequestMapping("removePayChannelRotation")
    public String removePayChannelRotation(HttpServletRequest request,HttpServletResponse response) {
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
            log = new Blog(this.getClass().getSimpleName()+".removePayChannelRotation","删除PayChannelRotation",user.id,request);
            new PayChannelRotationService().removePayChannelRotation(request.getParameter("id"));
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
    @RequestMapping("payChannelRotationOptBatch")
    public String payChannelRotationOptBatch(HttpServletRequest request,HttpServletResponse response) {
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
            log = new Blog(this.getClass().getSimpleName()+".payChannelRotationOptBatch","批量处理PayChannelRotation",user.id,request);
            String status = request.getParameter("status");
            if(!"0".equals(status)&&!"1".equals(status)&&!"2".equals(status))throw new Exception("操作状态错误");
            new PayChannelRotationService().updateBatchStatusChannelRotation(status,request.getParameter("batchNo"));
            os.write(JWebConstant.OK.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
            try {os.write(e.getMessage().getBytes("utf-8"));} catch (Exception e1) {}
            if(log!=null)log.info("批量处理失败,"+e.getMessage());
        } finally {
            if(os != null)try {os.close();} catch (IOException e1) {}
        }
        return null;
    }
    
	/**
     * excel导出
     * @param request
     * @param response
     * @param payRefund
     * @return
     */
    @RequestMapping("payChannelRotationListExportExcel")
    public String payMerchantExportExcel(HttpServletRequest request,HttpServletResponse response,@ModelAttribute("payChannelRotation") PayChannelRotation payChannelRotation) {
        JWebUser user = (JWebUser)request.getSession().getAttribute("user");
        //登录和权限判断
        if(!JWebUserService.checkUser(user,request))return "/jsp/jweb/jump_login.jsp";
        new Blog(this.getClass().getSimpleName()+".payChannelRotationList","导出PayChannelRotation列表",user.id,request);
        OutputStream os = null;
        Blog log = null;
        try {
//        	payMerchant.province = JWebConstant.getWebEncodeStr(request.getParameter("province"));
//        	payMerchant.city = JWebConstant.getWebEncodeStr(request.getParameter("city"));
//        	payMerchant.region = JWebConstant.getWebEncodeStr(request.getParameter("region"));
            os = response.getOutputStream();
            response.setContentType("application/zip");
            response.addHeader("Content-Disposition","attachment;filename="
            		+new SimpleDateFormat("yyyy-MM-dd").format(new Date())+"_"+Tools.getUniqueIdentify()+".zip");
            log = new Blog(this.getClass().getSimpleName()+".payChannelRotationListExportExcel","导出payChannelRotationListExcel",user.id,request);
            //调用业务层
            os.write(new PayChannelRotationService().exportExcelForPayChannelRotationList(payChannelRotation));
        } catch (Exception e) {
            e.printStackTrace();
            if(log!=null)log.info("导出失败,"+e.getMessage());
        } finally {
            if(os != null)try {os.close();} catch (IOException e1) {}
        }
        return null;
    }
//    @RequestMapping("updatePayChannelRotation")
//    public String updatePayChannelRotation(HttpServletRequest request,HttpServletResponse response,
//            @ModelAttribute("payChannelRotation") PayChannelRotation payChannelRotation) {
//        JWebUser user = (JWebUser)request.getSession().getAttribute("user");
//        OutputStream os = null;
//        Blog log = null;
//        try {
//            //处理标识,show跳转,其他更新
//            if("show".equals(request.getParameter("flag"))){
//                log = new Blog(this.getClass().getSimpleName()+".updatePayChannelRotation","取得PayChannelRotation",user.id,request);
//                request.setAttribute("payChannelRotationUpdate",
//                    new PayChannelRotationService().detailPayChannelRotation(request.getParameter("id")));
//                return "/jsp/pay/coopbank/pay_channel_rotation_update.jsp";
//            }
//            os = response.getOutputStream();
//            request.setCharacterEncoding("UTF-8");
//            response.setContentType("text/html;charset=UTF-8");
//            //登录和权限判断
//            if(!JWebUserService.checkUser(user,request)){
//               os.write(((String)request.getSession().getAttribute("LOGIN_INFO")).getBytes());
//               return null;
//            }
//            log = new Blog(this.getClass().getSimpleName()+".updatePayChannelRotation","修改PayChannelRotation",user.id,request);
//            new PayChannelRotationService().updatePayChannelRotation(payChannelRotation);
//            os.write(JWebConstant.OK.getBytes());
//        } catch (Exception e) {
//            e.printStackTrace();
//            try {os.write(e.getMessage().getBytes("utf-8"));} catch (Exception e1) {}
//            if(log!=null)log.info("修改失败,"+e.getMessage());
//        } finally {
//            if(os != null)try {os.close();} catch (IOException e1) {}
//        }
//        return null;
//    }
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