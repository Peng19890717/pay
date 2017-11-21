package com.pay.settlement.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;

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
import com.pay.settlement.dao.PayMerchantSettlement;
import com.pay.settlement.service.PayAutoSettlementListener;
import com.pay.settlement.service.PayMerchantSettlementService;
@Controller
public class PayMerchantSettlementController {
    @RequestMapping("payMerchantSettlement")
    public String getPayMerchantSettlementList(HttpServletRequest request,HttpServletResponse response,
            @ModelAttribute("payMerchantSettlement") PayMerchantSettlement payMerchantSettlement) {
        JWebUser user = (JWebUser)request.getSession().getAttribute("user");
        //登录和权限判断
        if(!JWebUserService.checkUser(user,request))return "/jsp/jweb/jump_login.jsp";
        //处理标识，空：跳转、0：列表
        String flag = request.getParameter("flag");
        OutputStream os = null;
        Blog log = null;
        try {
        	log = new Blog(this.getClass().getSimpleName()+".payMerchantSettlement","查询PayMerchantSettlement列表",user.id,request);
            if(flag == null)return "/jsp/pay/settlement/pay_merchant_settlement.jsp";
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
            os.write(new PayMerchantSettlementService().getPayMerchantSettlementList(payMerchantSettlement,page,rows,
                request.getParameter("sort"),request.getParameter("order")).getBytes("utf-8"));
        } catch (Exception e) {
            e.printStackTrace();
            if(log!=null)log.info("查询列表失败,"+e.getMessage());
        } finally {
            if(os != null)try {os.close();} catch (IOException e1) {}
        }
        return null;
    }
//    @RequestMapping("addPayMerchantSettlement")
//    public String addPayMerchantSettlement(HttpServletRequest request,HttpServletResponse response,
//            @ModelAttribute("payMerchantSettlement") PayMerchantSettlement payMerchantSettlement) {
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
//            log = new Blog(this.getClass().getSimpleName()+".addPayMerchantSettlement","添加PayMerchantSettlement",user.id,request);
//            new PayMerchantSettlementService().addPayMerchantSettlement(payMerchantSettlement);
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
    @RequestMapping("manualSettlementRun")
    public String manualSettlementRun(HttpServletRequest request,HttpServletResponse response) {
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
            log = new Blog(this.getClass().getSimpleName()+".manualSettlementRun","跑批开始",user.id,request);
            if(PayAutoSettlementListener.manual_run_flag
            		||PayAutoSettlementListener.auto_run_flag)throw new Exception("跑批正在进行...");
            new Timer().schedule(new PayAutoSettlementListener(
            		new SimpleDateFormat("yyyy-MM-dd").parse(request.getParameter("manualSettlementRunDate")),
            		request.getParameter("manualSettlementRunMerNo"),"1"),
            	0);
            os.write(JWebConstant.OK.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
            try {os.write(e.getMessage().getBytes("utf-8"));} catch (Exception e1) {}
            if(log!=null)log.info("操作失败,"+e.getMessage());
        } finally {
            if(os != null)try {os.close();} catch (IOException e1) {}
        }
        return null;
    }
    @RequestMapping("applyPayMerchantSettlement")
    public String applyPayMerchantSettlement(HttpServletRequest request,HttpServletResponse response,
            @ModelAttribute("payMerchantSettlement") PayMerchantSettlement payMerchantSettlement) {
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
            log = new Blog(this.getClass().getSimpleName()+".applyPayMerchantSettlement","申请PayMerchantSettlement",user.id,request);
            payMerchantSettlement.stlApplicants=user.id;
            payMerchantSettlement.stlApplIp = request.getRemoteAddr();
            new PayMerchantSettlementService().applyPayMerchantSettlement(request.getParameter("stlIds"),payMerchantSettlement);
            os.write(JWebConstant.OK.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
            try {os.write(e.getMessage().getBytes("utf-8"));} catch (Exception e1) {}
            if(log!=null)log.info("申请失败,"+e.getMessage());
        } finally {
            if(os != null)try {os.close();} catch (IOException e1) {}
        }
        return null;
    }
    @RequestMapping("reApplyPayMerchantSettlement")
    public String reApplyPayMerchantSettlement(HttpServletRequest request,HttpServletResponse response,
            @ModelAttribute("payMerchantSettlement") PayMerchantSettlement payMerchantSettlement) {
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
            log = new Blog(this.getClass().getSimpleName()+".reApplyPayMerchantSettlement","重新结算申请PayMerchantSettlement",user.id,request);
            payMerchantSettlement.stlApplicants=user.id;
            payMerchantSettlement.stlApplIp = request.getRemoteAddr();
            new PayMerchantSettlementService().reApplyPayMerchantSettlement(request.getParameter("stlIds"),payMerchantSettlement);
            os.write(JWebConstant.OK.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
            try {os.write(e.getMessage().getBytes("utf-8"));} catch (Exception e1) {}
            if(log!=null)log.info("重新结算申请失败,"+e.getMessage());
        } finally {
            if(os != null)try {os.close();} catch (IOException e1) {}
        }
        return null;
    }
    @RequestMapping("reCheckSettlement")
    public String reCheckSettlement(HttpServletRequest request,HttpServletResponse response,
            @ModelAttribute("payMerchantSettlement") PayMerchantSettlement payMerchantSettlement) {
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
            log = new Blog(this.getClass().getSimpleName()+".reCheckSettlement","复审PayMerchantSettlement",user.id,request);
            if(!"2".equals(payMerchantSettlement.stlStatus)
            	&&!"3".equals(payMerchantSettlement.stlStatus))throw new Exception("状态异常");
            payMerchantSettlement.stlAuditPer=user.id;
            payMerchantSettlement.stlAuditIp = request.getRemoteAddr();
            new PayMerchantSettlementService().reCheckSettlement(payMerchantSettlement);
            os.write(JWebConstant.OK.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
            try {os.write(e.getMessage().getBytes("utf-8"));} catch (Exception e1) {}
            if(log!=null)log.info("复审失败,"+e.getMessage());
        } finally {
            if(os != null)try {os.close();} catch (IOException e1) {}
        }
        return null;
    }
    @RequestMapping("setResultSettlement")
    public String setResultSettlement(HttpServletRequest request,HttpServletResponse response,
            @ModelAttribute("payMerchantSettlement") PayMerchantSettlement payMerchantSettlement) {
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
            log = new Blog(this.getClass().getSimpleName()+".setResultSettlement","结算结果设置PayMerchantSettlement",user.id,request);
            if(!"4".equals(payMerchantSettlement.stlStatus)
            	&&!"5".equals(payMerchantSettlement.stlStatus))throw new Exception("状态异常");
            payMerchantSettlement.stlSucOperator=user.id;
            payMerchantSettlement.remark = payMerchantSettlement.remark.trim();
            new PayMerchantSettlementService().setResultSettlement(payMerchantSettlement);
            os.write(JWebConstant.OK.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
            try {os.write(e.getMessage().getBytes("utf-8"));} catch (Exception e1) {}
            if(log!=null)log.info("结算结果设置失败,"+e.getMessage());
        } finally {
            if(os != null)try {os.close();} catch (IOException e1) {}
        }
        return null;
    }
    
    /**
     * 商户结算管理excel导出
     * @param request
     */
    @RequestMapping("payMerchantSettlementExportExcel")
    public String payMerchantSettlementExcel(HttpServletRequest request,HttpServletResponse response,@ModelAttribute("payMerchantSettlement") PayMerchantSettlement payMerchantSettlement) {
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
            log = new Blog(this.getClass().getSimpleName()+".payMerchantSettlementExportExcel","导出PayMerchantSettlementExportExcel",user.id,request);
            os.write(new PayMerchantSettlementService().exportExcelForPayMerchantSettlementList(payMerchantSettlement));
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
        binder.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd"), true));
        binder.registerCustomEditor(int.class, new IntegerEditor());
        binder.registerCustomEditor(long.class, new LongEditor());
        binder.registerCustomEditor(float.class, new FloatEditor());
        binder.registerCustomEditor(double.class, new DoubleEditor());
    }
}