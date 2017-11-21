package com.pay.cardbin.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import util.JWebConstant;

import com.jweb.dao.Blog;
import com.jweb.dao.JWebUser;
import com.jweb.service.JWebUserService;
import com.pay.bank.dao.PayBank;
import com.pay.cardbin.dao.PayCardBin;
import com.pay.cardbin.service.PayCardBinService;
import com.pay.coopbank.service.PayCoopBankService;
@Controller
public class PayCardBinController {
    @RequestMapping("payCardBin")
    public String getPayCardBinList(HttpServletRequest request,HttpServletResponse response,
            @ModelAttribute("payCardBin") PayCardBin payCardBin) {
        JWebUser user = (JWebUser)request.getSession().getAttribute("user");
        //登录和权限判断
        if(!JWebUserService.checkUser(user,request))return "/jsp/jweb/jump_login.jsp";
        //处理标识，空：跳转、0：列表
        String flag = request.getParameter("flag");
        new Blog(this.getClass().getSimpleName()+".getPayCardBinList","取得PayCardBin列表",user.id,request);
        OutputStream os = null;
        try {
            if(flag == null)return "/jsp/pay/cardbin/pay_card_bin.jsp";
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
            os.write(new PayCardBinService().getPayCardBinList(payCardBin,page,rows,
                request.getParameter("sort"),request.getParameter("order")).getBytes("utf-8"));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(os != null)try {os.close();} catch (IOException e1) {}
        }
        return null;
    }
    @RequestMapping("addPayCardBin")
    public String addPayCardBin(HttpServletRequest request,HttpServletResponse response,
            @ModelAttribute("payCardBin") PayCardBin payCardBin) {
    	JWebUser user = (JWebUser)request.getSession().getAttribute("user");
    	//登录和权限判断
    	if(!JWebUserService.checkUser(user,request))return "/jsp/jweb/jump_login.jsp";
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
            log = new Blog(this.getClass().getSimpleName()+".addPayCardBin","添加PayCardBin",user.id,request);
            String bankCode = payCardBin.getBankCode();
            if(bankCode != null && !"".equals(bankCode)){
            	String bankName = PayCoopBankService.BANK_CODE_NAME_MAP.get(bankCode);
            	payCardBin.setBankName(bankName);
            	String str = "";
            	if("0".equals(payCardBin.cardType)){
            		str += "借记卡";
            	}
            	if("1".equals(payCardBin.cardType)){
            		str += "贷记卡";
            	}
            	if("2".equals(payCardBin.cardType)){
            		str += "准贷记卡";
            	}
            	if("3".equals(payCardBin.cardType)){
            		str += "预付卡";
            	}
            	payCardBin.setCardName(bankName+str);
            	new PayCardBinService().addPayCardBin(payCardBin);
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
    @RequestMapping("removePayCardBin")
    public String removePayCardBin(HttpServletRequest request,HttpServletResponse response) {
    	JWebUser user = (JWebUser)request.getSession().getAttribute("user");
    	//登录和权限判断
    	if(!JWebUserService.checkUser(user,request))return "/jsp/jweb/jump_login.jsp";
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
            log = new Blog(this.getClass().getSimpleName()+".removePayCardBin","删除PayCardBin",user.id,request);
            String binId = request.getParameter("binId");
            if(binId != null && !"".equals(binId)) new PayCardBinService().removePayCardBin(binId);
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
    @RequestMapping("updatePayCardBin")
    public String updatePayCardBin(HttpServletRequest request,HttpServletResponse response,
            @ModelAttribute("payCardBin") PayCardBin payCardBin) {
    	JWebUser user = (JWebUser)request.getSession().getAttribute("user");
    	//登录和权限判断
    	if(!JWebUserService.checkUser(user,request))return "/jsp/jweb/jump_login.jsp";
        OutputStream os = null;
        Blog log = null;
        try {
            //处理标识,show跳转,其他更新
            if("show".equals(request.getParameter("flag"))){
                log = new Blog(this.getClass().getSimpleName()+".updatePayCardBin","取得PayCardBin",user.id,request);
                request.setAttribute("payCardBinUpdate",
                    new PayCardBinService().detailPayCardBin(request.getParameter("binId")));
                return "/jsp/pay/cardbin/pay_card_bin_update.jsp";
            }
            os = response.getOutputStream();
            request.setCharacterEncoding("UTF-8");
            response.setContentType("text/html;charset=UTF-8");
            //登录和权限判断
            if(!JWebUserService.checkUser(user,request)){
               os.write(((String)request.getSession().getAttribute("LOGIN_INFO")).getBytes());
               return null;
            }
            log = new Blog(this.getClass().getSimpleName()+".updatePayCardBin","修改PayCardBin",user.id,request);
            String bankCode = payCardBin.getBankCode();
            if(bankCode != null && !"".equals(bankCode)){
            	String bankName = PayCoopBankService.BANK_CODE_NAME_MAP.get(bankCode);
            	payCardBin.setBankName(bankName);
            	String str = "";
            	if("0".equals(payCardBin.cardType)){
            		str += "借记卡";
            	}
            	if("1".equals(payCardBin.cardType)){
            		str += "贷记卡";
            	}
            	if("2".equals(payCardBin.cardType)){
            		str += "准贷记卡";
            	}
            	if("3".equals(payCardBin.cardType)){
            		str += "预付卡";
            	}
            	payCardBin.setCardName(bankName+str);
            }
            payCardBin.setGmtModify(new Date());
            new PayCardBinService().updatePayCardBin(payCardBin);
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
}