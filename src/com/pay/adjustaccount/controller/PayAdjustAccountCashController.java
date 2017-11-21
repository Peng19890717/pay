package com.pay.adjustaccount.controller;

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

import com.PayConstant;
import com.jweb.dao.Blog;
import com.jweb.dao.JWebUser;
import com.jweb.service.JWebUserService;
import com.pay.adjustaccount.dao.PayAdjustAccountCash;
import com.pay.adjustaccount.dao.PayAdjustAccountCashDAO;
import com.pay.adjustaccount.service.PayAdjustAccountCashService;
import com.pay.coopbank.dao.PayCoopBank;
import com.pay.merchant.service.PayMerchantService;
import com.pay.merchantinterface.service.ZX_BJ_PayService;
import com.pay.user.service.PayTranUserInfoService;
@Controller
public class PayAdjustAccountCashController {
    @RequestMapping("payAdjustAccountCash")
    public String getPayAdjustAccountCashList(HttpServletRequest request,HttpServletResponse response,
            @ModelAttribute("payAdjustAccountCash") PayAdjustAccountCash payAdjustAccountCash) {
        JWebUser user = (JWebUser)request.getSession().getAttribute("user");
        //登录和权限判断
        if(!JWebUserService.checkUser(user,request))return "/jsp/jweb/jump_login.jsp";
        //处理标识，空：跳转、0：列表
        String flag = request.getParameter("flag");
        new Blog(this.getClass().getSimpleName()+".getPayAdjustAccountCashList","取得PayAdjustAccountCash列表",user.id,request);
        OutputStream os = null;
        try {
            if(flag == null)return "/jsp/pay/adjustaccount/pay_adjust_account_cash.jsp";
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
            os.write(new PayAdjustAccountCashService().getPayAdjustAccountCashList(payAdjustAccountCash,page,rows,
                request.getParameter("sort"),request.getParameter("order")).getBytes("utf-8"));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(os != null)try {os.close();} catch (IOException e1) {}
        }
        return null;
    }
    @RequestMapping("addPayAdjustAccountCash")
    public String addPayAdjustAccountCash(HttpServletRequest request,HttpServletResponse response,
            @ModelAttribute("payAdjustAccountCash") PayAdjustAccountCash payAdjustAccountCash) {
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
            log = new Blog(this.getClass().getSimpleName()+".addPayAdjustAccountCash","添加PayAdjustAccountCash",user.id,request);
            //根据用户输入的类别   判断用户或者商户是否存在
            if(payAdjustAccountCash.getAcType().equals(PayConstant.CUST_TYPE_USER)){
            	if(new PayTranUserInfoService().detailPayTranUserInfoByUserId(payAdjustAccountCash.getCustId())==null)
            		throw new Exception("客户编号不存在！");
            }
            if(payAdjustAccountCash.getAcType().equals(PayConstant.CUST_TYPE_MERCHANT)){
            	if(new PayMerchantService().detailPayMerchantByCustId(payAdjustAccountCash.getCustId())==null)
            		throw new Exception("商户编号不存在！");
            }
            //增加金额注意 - 号
            if(payAdjustAccountCash.getAmt()==null || payAdjustAccountCash.getAmt().SIZE==0 || payAdjustAccountCash.getAmt().toString().substring(0, 1).equals("-"))
            	throw new Exception("输入金额不合法！");
            payAdjustAccountCash.setId(Tools.getUniqueIdentify());
            payAdjustAccountCash.setApplyIp(request.getRemoteAddr());
            payAdjustAccountCash.setApplyTime(new Date());
            payAdjustAccountCash.setApplyUser(user.getId());
            payAdjustAccountCash.setCheckTime(new Date());
            payAdjustAccountCash.setStatus("0");//待审核
            
            new PayAdjustAccountCashService().addPayAdjustAccountCash(payAdjustAccountCash);
            
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
    @RequestMapping("detailPayAdjustAccountCash")
    public String detailPayAdjustAccountCash(HttpServletRequest request,HttpServletResponse response) {
        JWebUser user = (JWebUser)request.getSession().getAttribute("user");
        Blog log = null;
        //登录和权限判断
        if(!JWebUserService.checkUser(user,request))return "/jsp/jweb/jump_login.jsp";
        log = new Blog(this.getClass().getSimpleName()+".detailPayAdjustAccountCash","查看PayAdjustAccountCash",user.id,request);
        try {
            request.setAttribute("payAdjustAccountCash",
                new PayAdjustAccountCashService().detailPayAdjustAccountCash(request.getParameter("id")));
        } catch (Exception e) {
            e.printStackTrace();
            if(log!=null)log.info("查看失败,"+e.getMessage());
        } 
        return "/jsp/pay/adjustaccount/pay_adjust_account_cash_detail.jsp";
    }
    @RequestMapping("removePayAdjustAccountCash")
    public String removePayAdjustAccountCash(HttpServletRequest request,HttpServletResponse response) {
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
            log = new Blog(this.getClass().getSimpleName()+".removePayAdjustAccountCash","删除PayAdjustAccountCash",user.id,request);
            new PayAdjustAccountCashService().removePayAdjustAccountCash(request.getParameter("id"));
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
    @RequestMapping("updatePayAdjustAccountCash")
    public String updatePayAdjustAccountCash(HttpServletRequest request,HttpServletResponse response,
            @ModelAttribute("payAdjustAccountCash") PayAdjustAccountCash payAdjustAccountCash) {
        JWebUser user = (JWebUser)request.getSession().getAttribute("user");
        OutputStream os = null;
        Blog log = null;
        try {
            //处理标识,show跳转,其他更新
            if("show".equals(request.getParameter("flag"))){
                log = new Blog(this.getClass().getSimpleName()+".updatePayAdjustAccountCash","取得PayAdjustAccountCash",user.id,request);
                request.setAttribute("payAdjustAccountCashUpdate",
                    new PayAdjustAccountCashService().detailPayAdjustAccountCash(request.getParameter("id")));
                return "/jsp/pay/adjustaccount/pay_adjust_account_cash_update.jsp";
            }
            os = response.getOutputStream();
            request.setCharacterEncoding("UTF-8");
            response.setContentType("text/html;charset=UTF-8");
            //登录和权限判断
            if(!JWebUserService.checkUser(user,request)){
               os.write(((String)request.getSession().getAttribute("LOGIN_INFO")).getBytes());
               return null;
            }
            log = new Blog(this.getClass().getSimpleName()+".updatePayAdjustAccountCash","修改PayAdjustAccountCash",user.id,request);
            new PayAdjustAccountCashService().updatePayAdjustAccountCash(payAdjustAccountCash);
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
     * 审核
     * @param request
     * @param response
     * @param payAdjustAccountCash
     * @return
     */
    @RequestMapping("setpayAdjustAccountCashCheck")
    public String setpayAdjustAccountCashCheck(HttpServletRequest request,HttpServletResponse response,
            @ModelAttribute("payAdjustAccountCash") PayAdjustAccountCash payAdjustAccountCash) {
    	
        JWebUser user = (JWebUser)request.getSession().getAttribute("user");
        PayAdjustAccountCashService payAdjustAccountCashService = new PayAdjustAccountCashService();
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
            log = new Blog(this.getClass().getSimpleName()+".setpayAdjustAccountCashCheck","审核PayAdjustAccountCash",user.id,request);
            //审核操作
            payAdjustAccountCash = 
            		new PayAdjustAccountCashDAO().detailPayAdjustAccountCash(request.getParameter("updateId"));
            if(PayConstant.CUST_TYPE_CHARGE.equals(payAdjustAccountCash.acType)){
            	payAdjustAccountCashService.setPayAdjustAccountCashCheckForCharge(request,payAdjustAccountCash, user);
            } else {
            	payAdjustAccountCashService.setPayAdjustAccountCashCheck(request,payAdjustAccountCash, user);
            }
            os.write(JWebConstant.OK.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
            try {os.write(e.getMessage().getBytes("utf-8"));} catch (Exception e1) {}
            if(log!=null)log.info("审核失败,"+e.getMessage());
        } finally {
            if(os != null)try {os.close();} catch (IOException e1) {}
        }
        return null;
    }
    
    
    /**
     * 对账文件下载
     * @param request
     * @param response
     * @param 
     * @return
     */
	@RequestMapping("accountFileDownload")
    public String bjzxAccountFileDownload(HttpServletRequest request,HttpServletResponse response) {
    	JWebUser user = (JWebUser)request.getSession().getAttribute("user");
        OutputStream os = null;
        Blog log = null;
        try {
            if((request.getParameter("flag"))==null){
                log = new Blog(this.getClass().getSimpleName()+".accountFileDownload","取得accountFileDownload",user.id,request);
                return "/jsp/pay/account/account_file_download.jsp";
            }
            os = response.getOutputStream();
            //登录和权限判断
            if(!JWebUserService.checkUser(user,request)){
               os.write(((String)request.getSession().getAttribute("LOGIN_INFO")).getBytes());
               return null;
            }
            log = new Blog(this.getClass().getSimpleName()+".accountFileDownload","accountFileDownload",user.id,request);
            if((PayConstant.PAY_CONFIG.get("PAY_CHANNEL_ZXBJ")).equals(request.getParameter("accountId"))){
            	byte [] b = new ZX_BJ_PayService().accountFileDownloadExportExcel(request);
            	if(b.length>0){
            		response.setContentType("application/zip");
                    response.addHeader("Content-Disposition","attachment;filename="
                    		+new SimpleDateFormat("yyyy-MM-dd").format(new Date())+"_"+Tools.getUniqueIdentify()+".zip");
            		os.write(b);
            	} else os.write("无交易记录".getBytes("utf-8"));
            	os.flush();
            }
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