package com.pay.order.controller;

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
import util.SHA1;
import util.Tools;

import com.PayConstant;
import com.jweb.dao.Blog;
import com.jweb.dao.JWebUser;
import com.jweb.service.JWebUserService;
import com.pay.business.dao.PayBusinessParameter;
import com.pay.business.service.PayBusinessParameterService;
import com.pay.order.dao.PayReceiveAndPay;
import com.pay.order.service.PayReceiveAndPayService;
/**
 * 代收付控制类
 * @author Administrator
 *
 */
@Controller
public class PayReceiveAndPayController {
	private static final Log log = LogFactory.getLog(PayReceiveAndPayController.class);
    @RequestMapping("payReceiveAndPay")
    public String getPayReceiveAndPayList(HttpServletRequest request,HttpServletResponse response,
            @ModelAttribute("payReceiveAndPay") PayReceiveAndPay payReceiveAndPay) {
        JWebUser user = (JWebUser)request.getSession().getAttribute("user");
        //登录和权限判断
        if(!JWebUserService.checkUser(user,request))return "/jsp/jweb/jump_login.jsp";
        //处理标识，空：跳转、0：列表
        String flag = request.getParameter("flag");
        new Blog(this.getClass().getSimpleName()+".getPayReceiveAndPayList","取得PayReceiveAndPay列表",user.id,request);
        OutputStream os = null;
        try {
            if(flag == null)return "/jsp/pay/order/pay_receive_and_pay.jsp";
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
            os.write(new PayReceiveAndPayService().getPayReceiveAndPayList(payReceiveAndPay,page,rows,
                request.getParameter("sort"),request.getParameter("order")).getBytes("utf-8"));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(os != null)try {os.close();} catch (IOException e1) {}
        }
        return null;
    }
    @RequestMapping("payReceiveAndPayCheck")
    public String getPayReceiveAndPayCheckList(HttpServletRequest request,HttpServletResponse response,
    		@ModelAttribute("payReceiveAndPay") PayReceiveAndPay payReceiveAndPay) {
    	JWebUser user = (JWebUser)request.getSession().getAttribute("user");
    	//登录和权限判断
    	if(!JWebUserService.checkUser(user,request))return "/jsp/jweb/jump_login.jsp";
    	//处理标识，空：跳转、0：列表
    	String flag = request.getParameter("flag");
    	new Blog(this.getClass().getSimpleName()+".getPayReceiveAndPayList","取得PayReceiveAndPay列表",user.id,request);
    	OutputStream os = null;
    	try {
    		if(flag == null)return "/jsp/pay/order/pay_receive_and_pay_check.jsp";
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
    		os.write(new PayReceiveAndPayService().getPayReceiveAndPayCheckList(payReceiveAndPay,page,rows,
    				request.getParameter("sort"),request.getParameter("order")).getBytes("utf-8"));
    	} catch (Exception e) {
    		e.printStackTrace();
    	} finally {
    		if(os != null)try {os.close();} catch (IOException e1) {}
    	}
    	return null;
    }
    /**
     * 代收付excel导出
     * @param request
     * @param response
     * @param payReceiveAndPay
     * @return
     */
    @RequestMapping("payReceiveAndPayExportExcel")
    public String payMerchantExportExcel(HttpServletRequest request,HttpServletResponse response,@ModelAttribute("payReceiveAndPay") PayReceiveAndPay payReceiveAndPay) {
        JWebUser user = (JWebUser)request.getSession().getAttribute("user");
        //登录和权限判断
        if(!JWebUserService.checkUser(user,request))return "/jsp/jweb/jump_login.jsp";
        new Blog(this.getClass().getSimpleName()+".payReceiveAndPayExportExcel","导出PayReceiveAndPay列表",user.id,request);
        OutputStream os = null;
        Blog log = null;
        try {
            os = response.getOutputStream();
            response.setContentType("application/zip");
            response.addHeader("Content-Disposition","attachment;filename="
            		+new SimpleDateFormat("yyyy-MM-dd").format(new Date())+"_"+Tools.getUniqueIdentify()+".zip");
            log = new Blog(this.getClass().getSimpleName()+".payReceiveAndPayExportExcel","导出PayReceiveAndPayExcel",user.id,request);
            //调用业务层
            os.write(new PayReceiveAndPayService().exportExcelForPayReceiveAndPayList(payReceiveAndPay));
        } catch (Exception e) {
            e.printStackTrace();
            if(log!=null)log.info("导出失败,"+e.getMessage());
        } finally {
            if(os != null)try {os.close();} catch (IOException e1) {}
        }
        return null;
    }
    @RequestMapping("notifyReceiveAndPayMer")
    public String notifyReceiveAndPayMer(HttpServletRequest request,HttpServletResponse response,
            @ModelAttribute("payReceiveAndPay") PayReceiveAndPay payReceiveAndPay) {
        JWebUser user = (JWebUser)request.getSession().getAttribute("user");
        //登录和权限判断
        if(!JWebUserService.checkUser(user,request))return "/jsp/jweb/jump_login.jsp";
        new Blog(this.getClass().getSimpleName()+".notifyReceiveAndPayMer","取得PayReceiveAndPay通知商户",user.id,request);
        OutputStream os = null;
        try {
            request.setCharacterEncoding("UTF-8");
            response.setContentType("text/html;charset=UTF-8");
            os = response.getOutputStream();
            new PayReceiveAndPayService().notifyReceiveAndPayMer(payReceiveAndPay);
            os.write(JWebConstant.OK.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
            try {
				if(os==null)os = response.getOutputStream();
				os.write(e.getMessage().getBytes("utf-8"));
			} catch (IOException e1) {}
        } finally {
            if(os != null)try {os.close();} catch (IOException e1) {}
        }
        return null;
    }
    @RequestMapping("channelReceiveAndPayQuery")
    public String channelReceiveAndPayQuery(HttpServletRequest request,HttpServletResponse response,
            @ModelAttribute("payReceiveAndPay") PayReceiveAndPay payReceiveAndPay) {
        JWebUser user = (JWebUser)request.getSession().getAttribute("user");
        //登录和权限判断
        if(!JWebUserService.checkUser(user,request))return "/jsp/jweb/jump_login.jsp";
        new Blog(this.getClass().getSimpleName()+".channelReceiveAndPayQuery","取得PayReceiveAndPay渠道补单",user.id,request);
        OutputStream os = null;
        try {
            request.setCharacterEncoding("UTF-8");
            response.setContentType("text/html;charset=UTF-8");
            os = response.getOutputStream();
            new PayReceiveAndPayService().channelReceiveAndPayQuery(payReceiveAndPay);
            os.write(JWebConstant.OK.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
			try {
				if(os==null)os = response.getOutputStream();
				os.write(e.getMessage().getBytes("utf-8"));
			} catch (IOException e1) {}
        } finally {
            if(os != null)try {os.close();} catch (IOException e1) {}
        }
        return null;
    }
    /**
     * 代收付结算
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("settlementReceiveAndPay")
    public String settlementReceiveAndPay(HttpServletRequest request,HttpServletResponse response){
    	JWebUser user = (JWebUser)request.getSession().getAttribute("user");
    	//登录和权限判断
    	if(!JWebUserService.checkUser(user,request))return "/jsp/jweb/jump_login.jsp";
    	new Blog(this.getClass().getSimpleName()+".settlementReceiveAndPay","结算PayReceiveAndPay",user.id,request);
    	OutputStream os = null;
    	try {
    		request.setCharacterEncoding("UTF-8");
    		response.setContentType("text/html;charset=UTF-8");
    		os = response.getOutputStream();
    		String id = request.getParameter("id");
    		new PayReceiveAndPayService().settlementReceiveAndPay(id);
    		os.write(JWebConstant.OK.getBytes());
    	} catch (Exception e) {
    		e.printStackTrace();
    		try {
    			if(os==null)os = response.getOutputStream();
    			os.write(e.getMessage().getBytes("utf-8"));
    		} catch (IOException e1) {}
    	} finally {
    		if(os != null)try {os.close();} catch (IOException e1) {}
    	}
    	return null;
    }
    /**
     * 代付失败，设置退回金额标识
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("returnReceiveAndPayAcc")
    public String returnReceiveAndPayAcc(HttpServletRequest request,HttpServletResponse response){
    	JWebUser user = (JWebUser)request.getSession().getAttribute("user");
    	//登录和权限判断
    	if(!JWebUserService.checkUser(user,request))return "/jsp/jweb/jump_login.jsp";
    	new Blog(this.getClass().getSimpleName()+".returnReceiveAndPayAcc","代付失败，设置退回金额标识PayReceiveAndPay",user.id,request);
    	OutputStream os = null;
    	try {
    		request.setCharacterEncoding("UTF-8");
    		response.setContentType("text/html;charset=UTF-8");
    		os = response.getOutputStream();
    		String id = request.getParameter("id");
    		new PayReceiveAndPayService().returnReceiveAndPayAcc(id);
    		os.write(JWebConstant.OK.getBytes());
    	} catch (Exception e) {
    		e.printStackTrace();
    		try {
    			if(os==null)os = response.getOutputStream();
    			os.write(e.getMessage().getBytes("utf-8"));
    		} catch (IOException e1) {}
    	} finally {
    		if(os != null)try {os.close();} catch (IOException e1) {}
    	}
    	return null;
    }
	/**
     * 商户门户代收
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("merchantFaceRAP")
    public String merchantFaceRAP(HttpServletRequest request,HttpServletResponse response) {
    	OutputStream os = null;
        try {
        	log.info("商户门户代收"+request.getParameter("custId"));
    		request.setCharacterEncoding("UTF-8");
    		response.setCharacterEncoding("UTF-8");
    		response.setHeader("Content-type","text/html;charset=UTF-8");
    		os = response.getOutputStream();
    		//接收到数据，custType=1&custId=1000098&tranType=0&sign=f328fdshjfd&transferInfo=账号1_,_对公_,_账号名称1_,_金额（元）_,_手机号1_;_账号2_,_对公_,_账号名称2_,_金额（元）_,_手机号2
    		String custType = request.getParameter("custType");
    		String custId = request.getParameter("custId");
    		String tranType = request.getParameter("tranType");//代收付类型 0代收 1代付
    		String transferInfo = request.getParameter("transferInfo");
    		String sign = request.getParameter("sign");
            //验签
            if(sign!=null && sign.equals(SHA1.SHA1String(custType+custId+tranType+
            		transferInfo+PayConstant.PAY_CONFIG.get("SYS_COMM_PWD_WITH_INNER_PLT")))){
            	os.write(new PayReceiveAndPayService().merchantFaceRAP(request).getBytes("utf-8"));
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
    /**
     * 开启或关闭代付
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("payOpenOrNotReceiveAndPay")
    public String payOpenOrNotReceiveAndPay(HttpServletRequest request,HttpServletResponse response) {
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
    		log = new Blog(this.getClass().getSimpleName()+".payOpenOrNotReceiveAndPay","开启/关闭代付",user.id,request);
    		String paraValue = request.getParameter("paraValue");
    		PayBusinessParameter payBusinessParameter = new PayBusinessParameter();
    		payBusinessParameter.name = "PAY_IS_OPEN";
    		if("0".equals(paraValue)){
    			payBusinessParameter.value = "1";
    		}else if("1".equals(paraValue)){
    			payBusinessParameter.value = "0";
    		}
    		new PayBusinessParameterService().updatePayBusinessParameter(payBusinessParameter);
    		PayBusinessParameterService.executePayBusinessParameter("1");
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