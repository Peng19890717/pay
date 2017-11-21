package com.pay.order.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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
import com.pay.merchantinterface.dao.PayInterfaceDAO;
import com.pay.merchantinterface.service.NotifyInterface;
import com.pay.merchantinterface.service.PayChannelService;
import com.pay.order.dao.PayOrder;
import com.pay.order.service.PayOrderService;
import com.sun.org.apache.xml.internal.security.utils.Base64;
@Controller
public class PayOrderController {
	private static final Log log = LogFactory.getLog(PayOrderController.class);
    @RequestMapping("payOrder")
    public String getPayOrderList(HttpServletRequest request,HttpServletResponse response,
        @ModelAttribute("payOrder") PayOrder payOrder) {
    	//获取session中的用户
        JWebUser user = (JWebUser)request.getSession().getAttribute("user");
        //登录和权限判断
        if(!JWebUserService.checkUser(user,request))return "/jsp/jweb/jump_login.jsp";
        //处理标识，空：跳转、0：列表
        String flag = request.getParameter("flag");
        OutputStream os = null;
        Blog log = null;
        String timeRange = "0";
        if(!"".equals(request.getParameter("timeRange")) && request.getParameter("timeRange") != null){
			timeRange = request.getParameter("timeRange");
		}
        request.getSession().setAttribute("timeRange", timeRange);
        try {
        	log = new Blog(this.getClass().getSimpleName()+".payOrder","查询PayOrder列表",user.id,request);
            if(flag == null)return "/jsp/pay/order/pay_order.jsp";
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
            //调用业务层
            os.write(new PayOrderService().getPayOrderList(timeRange,payOrder,page,rows,
                request.getParameter("sort"),request.getParameter("order")).getBytes("utf-8"));
        } catch (Exception e) {
            e.printStackTrace();
            if(log!=null)log.info("查询列表失败,"+e.getMessage());
        } finally {
            if(os != null)try {os.close();} catch (IOException e1) {}
        }
        return null;
    }
    /**
     * 交易备份查询
     * @param request
     * @param response
     * @param payOrder
     * @return
     */
    @RequestMapping("payOrderHistory")
    public String payOrderHistory(HttpServletRequest request,HttpServletResponse response,
    		@ModelAttribute("payOrder") PayOrder payOrder) {
    	//获取session中的用户
    	JWebUser user = (JWebUser)request.getSession().getAttribute("user");
    	//登录和权限判断
    	if(!JWebUserService.checkUser(user,request))return "/jsp/jweb/jump_login.jsp";
    	//处理标识，空：跳转、0：列表
    	String flag = request.getParameter("flag");
    	OutputStream os = null;
    	Blog log = null;
    	try {
    		log = new Blog(this.getClass().getSimpleName()+".payOrder","查询PayOrder列表",user.id,request);
    		if(flag == null)return "/jsp/pay/order/pay_order_backup.jsp";
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
    		//调用业务层
    		os.write(new PayOrderService().getPayOrderList("payOrderHistory",payOrder,page,rows,
    				request.getParameter("sort"),request.getParameter("order")).getBytes("utf-8"));
    	} catch (Exception e) {
    		e.printStackTrace();
    		if(log!=null)log.info("查询列表失败,"+e.getMessage());
    	} finally {
    		if(os != null)try {os.close();} catch (IOException e1) {}
    	}
    	return null;
    }
    /**
     * 业务员交易查询
     * @param request
     * @param response
     * @param payOrder
     * @return
     */
    @RequestMapping("bussMemOrder")
    public String bussMemOrder(HttpServletRequest request,HttpServletResponse response,
            @ModelAttribute("payOrder") PayOrder payOrder) {
        JWebUser user = (JWebUser)request.getSession().getAttribute("user");
        //登录和权限判断
        if(!JWebUserService.checkUser(user,request))return "/jsp/jweb/jump_login.jsp";
        //处理标识，空：跳转、0：列表
        String flag = request.getParameter("flag");
        new Blog(this.getClass().getSimpleName()+".getBussMemOrder","取得业务员订单列表",user.id,request);
        OutputStream os = null;
        String timeRange = request.getParameter("timeRange");
        try {
            if(flag == null)return "/jsp/pay/order/buss_mem_order.jsp";
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
            os.write(new PayOrderService().getBussMemOrder(timeRange==null?"0":timeRange,request,payOrder,page,rows,request.getParameter("sort"),
            		request.getParameter("order")).getBytes("utf-8"));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(os != null)try {os.close();} catch (IOException e1) {}
        }
        return null;
    }
    @RequestMapping("bussMemOrderBackUp")
    public String bussMemOrderBackUp(HttpServletRequest request,HttpServletResponse response,
    		@ModelAttribute("payOrder") PayOrder payOrder) {
    	JWebUser user = (JWebUser)request.getSession().getAttribute("user");
    	//登录和权限判断
    	if(!JWebUserService.checkUser(user,request))return "/jsp/jweb/jump_login.jsp";
    	//处理标识，空：跳转、0：列表
    	String flag = request.getParameter("flag");
    	new Blog(this.getClass().getSimpleName()+".getBussMemOrder","取得业务员订单列表",user.id,request);
    	OutputStream os = null;
    	try {
    		if(flag == null)return "/jsp/pay/order/buss_mem_order_backup.jsp";
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
    		os.write(new PayOrderService().getBussMemOrder("bussMemOrderBackUp",request,payOrder,page,rows,
    				request.getParameter("sort"),request.getParameter("order")).getBytes("utf-8"));
    	} catch (Exception e) {
    		e.printStackTrace();
    	} finally {
    		if(os != null)try {os.close();} catch (IOException e1) {}
    	}
    	return null;
    }
    /**
     * 订单结算
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("settlementPayProOrder")
    public String settlementPayProOrder(HttpServletRequest request,HttpServletResponse response){
    	JWebUser user = (JWebUser)request.getSession().getAttribute("user");
    	//登录和权限判断
    	if(!JWebUserService.checkUser(user,request))return "/jsp/jweb/jump_login.jsp";
    	new Blog(this.getClass().getSimpleName()+".settlementPayProOrder","结算PayProductOrder",user.id,request);
    	OutputStream os = null;
    	try {
    		request.setCharacterEncoding("UTF-8");
    		response.setContentType("text/html;charset=UTF-8");
    		os = response.getOutputStream();
    		String prdordno = request.getParameter("prdordno");
    		String timeRange = "0";
            if(!"".equals(request.getParameter("timeRange")) && request.getParameter("timeRange") != null){
    			timeRange = request.getParameter("timeRange");
    		}
    		new PayOrderService().settlementPayProOrder(timeRange,prdordno);
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
     * 订单详情
     * @param request
     * @param response
     * @param payOrder
     * @return
     */
    @RequestMapping("payOrderDetail")
    public String payOrderDetail(HttpServletRequest request,HttpServletResponse response,
        @ModelAttribute("payOrder") PayOrder payOrder) {
    	//获取session中的用户
        JWebUser user = (JWebUser)request.getSession().getAttribute("user");
        Blog log = null;
		String timeRange = (String) request.getSession().getAttribute("timeRange");
		if(timeRange==null)timeRange="0";//近期记录
        //登录和权限判断
        if(!JWebUserService.checkUser(user,request))return "/jsp/jweb/jump_login.jsp";
        try {
        	//调用业务层
        	log = new Blog(this.getClass().getSimpleName()+".payOrderDetail","查询PayOrder详情",user.id,request);
        	PayOrder order = new PayOrderService().getPayOrderDetail(timeRange,payOrder);
        	try {
        		 order.setVerifystring(new String(Base64.decode(order.getVerifystring()),"utf-8"));
			} catch (Exception e) {
				e.printStackTrace();
			}
        	request.setAttribute("payOrderDetail", order);
			return "/jsp/pay/order/pay_order_detail.jsp";
		} catch (Exception e) {
			e.printStackTrace();
			if(log!=null)log.info("查询详情失败,"+e.getMessage());
		}
        return null;
    }
    /**
     * 商户补单
     */
    @RequestMapping("notifyMer")
    public String notifyMer(HttpServletRequest request,HttpServletResponse response,
        @ModelAttribute("payOrder") PayOrder payOrder) {
    	//获取session中的用户
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
            log = new Blog(this.getClass().getSimpleName()+".notifyMer","商户补单",user.id,request);
            //调用业务层
            new NotifyInterface().notifyMer(new PayInterfaceDAO().getOrderByPrdordno(payOrder.payordno));
            os.write(JWebConstant.OK.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
            try {os.write(e.getMessage().getBytes("utf-8"));} catch (Exception e1) {}
            if(log!=null)log.info("补单失败,"+e.getMessage());
        } finally {
            if(os != null)try {os.close();} catch (IOException e1) {}
        }
        return null;
    }
    /**
     * 商户门户补单
     */
    @RequestMapping("notifyMerFromMer")
    public String notifyMerFromMer(HttpServletRequest request,HttpServletResponse response) {
    	//获取session中的用户
        //JWebUser user = (JWebUser)request.getSession().getAttribute("user");
        OutputStream os = null;
        Blog log = null;
        try {
        	//log = new Blog(this.getClass().getSimpleName()+".notifyMerFromMer","商户补单来自商户门户",user.id,request);
        	os = response.getOutputStream();
        	String merno= request.getParameter("merno");
        	String prdordno= request.getParameter("prdordno");
        	String sign= request.getParameter("sign");
        	String sign_temp = SHA1.SHA1String(merno+prdordno+
					PayConstant.PAY_CONFIG.get("SYS_COMM_PWD_WITH_INNER_PLT"));
        	if(sign.equals(sign_temp)){
        		//验签 TODO
            	PayInterfaceDAO dao = new PayInterfaceDAO();
            	List<PayOrder> list = dao.getPayOrderListByPrdordno(merno,prdordno);
            	if(list.size()==0)os.write("非法订单".getBytes("utf-8"));
            	PayOrder payOrder = list.get(0);
                //调用业务层
                new NotifyInterface().notifyMer(dao.getOrderByPrdordno(payOrder.payordno));
                os.write(JWebConstant.OK.getBytes());
        	}else{
        		throw new Exception("验签失败");
        	}
        } catch (Exception e) {
            e.printStackTrace();
            try {os.write(e.getMessage().getBytes("utf-8"));} catch (Exception e1) {}
            if(log!=null)log.info("补单失败,"+e.getMessage());
        } finally {
            if(os != null)try {os.close();} catch (IOException e1) {}
        }
        return null;
    }
    
    /**
     * 渠道补单
     */
    @RequestMapping("channelQuery")
    public String channelQuery(HttpServletRequest request,HttpServletResponse response,
        @ModelAttribute("payOrder") PayOrder payOrder) {
    	//获取session中的用户
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
            log = new Blog(this.getClass().getSimpleName()+".channelQuery","渠道补单",user.id,request);
            //调用业务层
            new PayChannelService().searchOrderFromChannel(payOrder.payordno);
            os.write(JWebConstant.OK.getBytes());
        } catch (Exception e) {
            try {os.write(e.getMessage().getBytes("utf-8"));} catch (Exception e1) {}
            if(log!=null)log.info("渠道补单失败,"+e.getMessage());
        } finally {
            if(os != null)try {os.close();} catch (IOException e1) {}
        }
        return null;
    }
    /**
     * 交易记录excel导出
     * @param request
     * @param response
     * @param payOrder
     * @return
     */
    @RequestMapping("payOrderExportExcel")
    public String payOrderExportExcel(HttpServletRequest request,HttpServletResponse response,@ModelAttribute("payOrder") PayOrder payOrder) {
        JWebUser user = (JWebUser)request.getSession().getAttribute("user");
        //登录和权限判断
        if(!JWebUserService.checkUser(user,request))return "/jsp/jweb/jump_login.jsp";
        OutputStream os = null;
        try {
            os = response.getOutputStream();
            response.setContentType("application/zip");
            response.addHeader("Content-Disposition","attachment;filename="
            		+new SimpleDateFormat("yyyy-MM-dd").format(new Date())+"_"+Tools.getUniqueIdentify()+".zip");
            //调用业务层
            log.info("导出PayOrderExcel");
            String timeRange = (String)request.getSession().getAttribute("timeRange");
            os.write(new PayOrderService().exportExcelForPayOrderList(timeRange,payOrder));
        } catch (Exception e) {
            e.printStackTrace();
            if(log!=null)log.info("导出失败,"+e.getMessage());
        } finally {
            if(os != null)try {os.close();} catch (IOException e1) {}
        }
        return null;
    }
    /**
     * 交易记录备份excel导出
     * @param request
     * @param response
     * @param payOrder
     * @return
     */
    @RequestMapping("payOrderExportExcelHistory")
    public String payOrderExportExcelHistory(HttpServletRequest request,HttpServletResponse response,@ModelAttribute("payOrder") PayOrder payOrder) {
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
    		log = new Blog(this.getClass().getSimpleName()+".payOrderExportExcel","导出PayOrderExcel",user.id,request);
    		String timeRange = "0";
            if(!"".equals(request.getParameter("timeRange")) && request.getParameter("timeRange") != null){
    			timeRange = request.getParameter("timeRange");
    		}
    		os.write(new PayOrderService().exportExcelForPayOrderList(timeRange,payOrder));
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