package com.pay.coopbank.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
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
import com.pay.coopbank.dao.PayChannelFrozen;
import com.pay.coopbank.service.PayChannelFrozenService;
@Controller
public class PayChannelFrozenController {
    @RequestMapping("payChannelFrozen")
    public String getPayChannelFrozenList(HttpServletRequest request,HttpServletResponse response,
            @ModelAttribute("payChannelFrozen") PayChannelFrozen payChannelFrozen) {
        JWebUser user = (JWebUser)request.getSession().getAttribute("user");
        //登录和权限判断
        if(!JWebUserService.checkUser(user,request))return "/jsp/jweb/jump_login.jsp";
        //处理标识，空：跳转、0：列表
        String flag = request.getParameter("flag");
        new Blog(this.getClass().getSimpleName()+".getPayChannelFrozenList","取得PayChannelFrozen列表",user.id,request);
        OutputStream os = null;
        try {
            if(flag == null)return "/jsp/pay/coopbank/pay_channel_frozen.jsp";
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
            os.write(new PayChannelFrozenService().getPayChannelFrozenList(payChannelFrozen,page,rows,
                request.getParameter("sort"),request.getParameter("order")).getBytes("utf-8"));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(os != null)try {os.close();} catch (IOException e1) {}
        }
        return null;
    }
    @RequestMapping("addPayChannelFrozen")
    public String addPayChannelFrozen(HttpServletRequest request,HttpServletResponse response,
            @ModelAttribute("payChannelFrozen") PayChannelFrozen payChannelFrozen) {
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
            log = new Blog(this.getClass().getSimpleName()+".addPayChannelFrozen","添加PayChannelFrozen",user.id,request);
            payChannelFrozen.id=Tools.getUniqueIdentify();
            payChannelFrozen.optId=user.id;
            payChannelFrozen.createTime=new Date();
            new PayChannelFrozenService().addPayChannelFrozen(payChannelFrozen);
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
    @RequestMapping("detailPayChannelFrozen")
    public String detailPayChannelFrozen(HttpServletRequest request,HttpServletResponse response) {
        JWebUser user = (JWebUser)request.getSession().getAttribute("user");
        Blog log = null;
        //登录和权限判断
        if(!JWebUserService.checkUser(user,request))return "/jsp/jweb/jump_login.jsp";
        log = new Blog(this.getClass().getSimpleName()+".detailPayChannelFrozen","查看PayChannelFrozen",user.id,request);
        try {
            request.setAttribute("payChannelFrozen",
                new PayChannelFrozenService().detailPayChannelFrozen(request.getParameter("id")));
        } catch (Exception e) {
            e.printStackTrace();
            if(log!=null)log.info("查看失败,"+e.getMessage());
        } 
        return "/jsp/pay/coopbank/pay_channel_frozen_detail.jsp";
    }
    @RequestMapping("removePayChannelFrozen")
    public String removePayChannelFrozen(HttpServletRequest request,HttpServletResponse response) {
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
            log = new Blog(this.getClass().getSimpleName()+".removePayChannelFrozen","删除PayChannelFrozen",user.id,request);
            new PayChannelFrozenService().removePayChannelFrozen(request.getParameter("id"));
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
    @RequestMapping("updatePayChannelFrozen")
    public String updatePayChannelFrozen(HttpServletRequest request,HttpServletResponse response,
            @ModelAttribute("payChannelFrozen") PayChannelFrozen payChannelFrozen) {
        JWebUser user = (JWebUser)request.getSession().getAttribute("user");
        OutputStream os = null;
        Blog log = null;
        try {
            //处理标识,show跳转,其他更新
            if("show".equals(request.getParameter("flag"))){
                log = new Blog(this.getClass().getSimpleName()+".updatePayChannelFrozen","取得PayChannelFrozen",user.id,request);
                request.setAttribute("payChannelFrozenUpdate",
                    new PayChannelFrozenService().detailPayChannelFrozen(request.getParameter("id")));
                return "/jsp/pay/coopbank/pay_channel_frozen_update.jsp";
            }
            os = response.getOutputStream();
            request.setCharacterEncoding("UTF-8");
            response.setContentType("text/html;charset=UTF-8");
            //登录和权限判断
            if(!JWebUserService.checkUser(user,request)){
               os.write(((String)request.getSession().getAttribute("LOGIN_INFO")).getBytes());
               return null;
            }
            log = new Blog(this.getClass().getSimpleName()+".updatePayChannelFrozen","修改PayChannelFrozen",user.id,request);
            payChannelFrozen.optId=user.id;
            payChannelFrozen.createTime=new Date();
            new PayChannelFrozenService().updatePayChannelFrozen(payChannelFrozen);
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
     * 解冻
     * @param request
     * @param response
     * @param channelUnFrozenId
     * @return
     */
    @RequestMapping("unFrozenPayChannelFrozen")
	public String unFrozenPayChannelFrozen(HttpServletRequest request,HttpServletResponse response,
			@ModelAttribute("payChannelFrozen") PayChannelFrozen payChannelFrozen) {
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
            //更新
            String id = request.getParameter("channelUnFrozen");
            String unfrozenMoney = request.getParameter("unfrozenMoney");
            String remark = payChannelFrozen.remark;
            //增加金额注意 - 号
            if(unfrozenMoney==null || unfrozenMoney.equals("") || unfrozenMoney.substring(0, 1).equals("-"))
            	throw new Exception("输入金额不合法！");
            payChannelFrozen = new PayChannelFrozenService().detailPayChannelFrozen(id);
            //判断解冻金额是否超出冻结金额
            Long dMoney = payChannelFrozen.curAmt;
            Long jmoney = new BigDecimal(unfrozenMoney).multiply(new BigDecimal("100")).longValue();
            if(jmoney>dMoney) throw new Exception("解冻金额不能大于剩余冻结金额！");
            new PayChannelFrozenService().updatePayChannelFrozen(payChannelFrozen, jmoney,user,remark);
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