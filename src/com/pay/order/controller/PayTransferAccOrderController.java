package com.pay.order.controller;

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
import util.SHA1;

import com.PayConstant;
import com.jweb.dao.Blog;
import com.jweb.dao.JWebUser;
import com.jweb.service.JWebUserService;
import com.pay.order.service.PayTransferAccOrderService;
import com.pay.order.dao.PayTransferAccOrder;
/**
 * 转账工具类
 * @author Administrator
 *
 */
@Controller
public class PayTransferAccOrderController {
    @RequestMapping("payTransferAccOrder")
    public String getPayTransferAccOrderList(HttpServletRequest request,HttpServletResponse response,
            @ModelAttribute("payTransferAccOrder") PayTransferAccOrder payTransferAccOrder) {
        JWebUser user = (JWebUser)request.getSession().getAttribute("user");
        //登录和权限判断
        if(!JWebUserService.checkUser(user,request))return "/jsp/jweb/jump_login.jsp";
        //处理标识，空：跳转、0：列表
        String flag = request.getParameter("flag");
        new Blog(this.getClass().getSimpleName()+".getPayTransferAccOrderList","取得PayTransferAccOrder列表",user.id,request);
        OutputStream os = null;
        try {
            if(flag == null)return "/jsp/pay/order/pay_transfer_acc_order.jsp";
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
            os.write(new PayTransferAccOrderService().getPayTransferAccOrderList(payTransferAccOrder,page,rows,
                request.getParameter("sort"),request.getParameter("order")).getBytes("utf-8"));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(os != null)try {os.close();} catch (IOException e1) {}
        }
        return null;
    }
    @RequestMapping("addPayTransferAccOrder")
    public String addPayTransferAccOrder(HttpServletRequest request,HttpServletResponse response,
            @ModelAttribute("payTransferAccOrder") PayTransferAccOrder payTransferAccOrder) {
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
            log = new Blog(this.getClass().getSimpleName()+".addPayTransferAccOrder","添加PayTransferAccOrder",user.id,request);
            new PayTransferAccOrderService().addPayTransferAccOrder(payTransferAccOrder);
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
    @RequestMapping("detailPayTransferAccOrder")
    public String detailPayTransferAccOrder(HttpServletRequest request,HttpServletResponse response) {
        JWebUser user = (JWebUser)request.getSession().getAttribute("user");
        Blog log = null;
        //登录和权限判断
        if(!JWebUserService.checkUser(user,request))return "/jsp/jweb/jump_login.jsp";
        log = new Blog(this.getClass().getSimpleName()+".detailPayTransferAccOrder","查看PayTransferAccOrder",user.id,request);
        try {
            request.setAttribute("payTransferAccOrder",
                new PayTransferAccOrderService().detailPayTransferAccOrder(request.getParameter("tranOrdno")));
        } catch (Exception e) {
            e.printStackTrace();
            if(log!=null)log.info("查看失败,"+e.getMessage());
        } 
        return "/jsp/pay/order/pay_transfer_acc_order_detail.jsp";
    }
    @RequestMapping("removePayTransferAccOrder")
    public String removePayTransferAccOrder(HttpServletRequest request,HttpServletResponse response) {
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
            log = new Blog(this.getClass().getSimpleName()+".removePayTransferAccOrder","删除PayTransferAccOrder",user.id,request);
            new PayTransferAccOrderService().removePayTransferAccOrder(request.getParameter("tranOrdno"));
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
    @RequestMapping("updatePayTransferAccOrder")
    public String updatePayTransferAccOrder(HttpServletRequest request,HttpServletResponse response,
            @ModelAttribute("payTransferAccOrder") PayTransferAccOrder payTransferAccOrder) {
        JWebUser user = (JWebUser)request.getSession().getAttribute("user");
        OutputStream os = null;
        Blog log = null;
        try {
            //处理标识,show跳转,其他更新
            if("show".equals(request.getParameter("flag"))){
                log = new Blog(this.getClass().getSimpleName()+".updatePayTransferAccOrder","取得PayTransferAccOrder",user.id,request);
                request.setAttribute("payTransferAccOrderUpdate",
                    new PayTransferAccOrderService().detailPayTransferAccOrder(request.getParameter("tranOrdno")));
                return "/jsp/pay/order/pay_transfer_acc_order_update.jsp";
            }
            os = response.getOutputStream();
            request.setCharacterEncoding("UTF-8");
            response.setContentType("text/html;charset=UTF-8");
            //登录和权限判断
            if(!JWebUserService.checkUser(user,request)){
               os.write(((String)request.getSession().getAttribute("LOGIN_INFO")).getBytes());
               return null;
            }
            log = new Blog(this.getClass().getSimpleName()+".updatePayTransferAccOrder","修改PayTransferAccOrder",user.id,request);
            new PayTransferAccOrderService().updatePayTransferAccOrder(payTransferAccOrder);
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
     * 转账
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("transferAcc")
    public String transferAcc(HttpServletRequest request,HttpServletResponse response) {
    	Blog log = null;
    	OutputStream os = null;
        try {
        	log = new Blog(this.getClass().getSimpleName(),"转账"+request.getParameter("custId"),"admin",request);
    		request.setCharacterEncoding("UTF-8");
    		response.setCharacterEncoding("UTF-8");
    		response.setHeader("Content-type","text/html;charset=UTF-8");
    		os = response.getOutputStream();
    		//接收到数据
    		String custType = request.getParameter("custType");
    		String custId = request.getParameter("custId");
    		String transferInfo = request.getParameter("transferInfo");
    		String sign = request.getParameter("sign");
            //验签
            if(sign!=null && sign.equals(SHA1.SHA1String(custType+custId+
            		transferInfo+PayConstant.PAY_CONFIG.get("SYS_COMM_PWD_WITH_INNER_PLT")))){
            	//"transferAccRes":"tgetAccNo1,status;tgetAccNo2,status;..."
            	os.write(new PayTransferAccOrderService().transferAcc(request).getBytes("utf-8"));
            } else os.write("{\"resCode\":\"001\",\"resCode\":\"001\",\"resMsg\":\"验签失败\"}".getBytes("utf-8"));
        } catch (Exception e) {
        	e.printStackTrace();
        	if(log!=null)log.info("转账失败,"+e.getMessage());
        	if(os!=null)try {
        		os.write(("{\"resCode\":\"-1\",\"resMsg\":\""+e.getMessage()+"\"}").getBytes("utf-8"));
        	} catch (Exception e1) {}
        } finally {
        	if(os!=null)try {os.close();} catch (Exception e) {}
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