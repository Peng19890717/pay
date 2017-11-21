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
import util.Tools;

import com.PayConstant;
import com.jweb.dao.Blog;
import com.jweb.dao.JWebUser;
import com.jweb.service.JWebUserService;
import com.pay.merchant.dao.PayMerchant;
import com.pay.merchant.service.PayMerchantService;
import com.pay.order.dao.PayAccProfile;
import com.pay.order.dao.PayWithdrawCashOrder;
import com.pay.order.service.PayAccProfileService;
import com.pay.order.service.PayWithdrawCashOrderService;
/**
 * 提现控制类
 * @author Administrator
 *
 */
@Controller
public class PayWithdrawCashOrderController {
    @RequestMapping("payWithdrawCashOrder")
    public String getPayWithdrawCashOrderList(HttpServletRequest request,HttpServletResponse response,
            @ModelAttribute("payWithdrawCashOrder") PayWithdrawCashOrder payWithdrawCashOrder) {
        JWebUser user = (JWebUser)request.getSession().getAttribute("user");
        //登录和权限判断
        if(!JWebUserService.checkUser(user,request))return "/jsp/jweb/jump_login.jsp";
        //处理标识，空：跳转、0：列表
        String flag = request.getParameter("flag");
        new Blog(this.getClass().getSimpleName()+".getPayWithdrawCashOrderList","取得PayWithdrawCashOrder列表",user.id,request);
        OutputStream os = null;
        try {
            if(flag == null)return "/jsp/pay/order/pay_withdraw_cash_order.jsp";
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
            os.write(new PayWithdrawCashOrderService().getPayWithdrawCashOrderList(payWithdrawCashOrder,page,rows,
                request.getParameter("sort"),request.getParameter("order")).getBytes("utf-8"));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(os != null)try {os.close();} catch (IOException e1) {}
        }
        return null;
    }
    
    @RequestMapping("detailPayWithdrawCashOrder")
    public String detailPayWithdrawCashOrder(HttpServletRequest request,HttpServletResponse response) {
        JWebUser user = (JWebUser)request.getSession().getAttribute("user");
        Blog log = null;
        //登录和权限判断
        if(!JWebUserService.checkUser(user,request))return "/jsp/jweb/jump_login.jsp";
        log = new Blog(this.getClass().getSimpleName()+".detailPayWithdrawCashOrder","查看PayWithdrawCashOrder",user.id,request);
        try {
            PayWithdrawCashOrder detailPayWithdrawCashOrder = new PayWithdrawCashOrderService().detailPayWithdrawCashOrder(request.getParameter("casordno"));
            PayAccProfile payAccProfile = new PayAccProfileService().detailPayAccProfile(detailPayWithdrawCashOrder.custId);
            PayMerchant payMerchant = new PayMerchantService().selectByCustId(detailPayWithdrawCashOrder.custId);
            request.setAttribute("payAccProfile",payAccProfile);
            request.setAttribute("payMerchant",payMerchant);
			request.setAttribute("payWithdrawCashOrder",detailPayWithdrawCashOrder);
        } catch (Exception e) {
            e.printStackTrace();
            if(log!=null)log.info("查看失败,"+e.getMessage());
        } 
        return "/jsp/pay/order/pay_withdraw_cash_order_detail.jsp";
    }
    
    @RequestMapping("payWithdrawCashSuc")
    public String payWithdrawCashSuc(HttpServletRequest request,HttpServletResponse response) {
    	String info = "提现成功";
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
            log = new Blog(this.getClass().getSimpleName()+".updateStatus",info+"PayWithdrawCashOrder",user.id,request);
            new PayWithdrawCashOrderService().updatePayWithdrawCashOrderStatus(request.getParameter("casordno"),
           		 request.getParameter("columName"),request.getParameter("value"));
            os.write(JWebConstant.OK.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
            try {os.write(e.getMessage().getBytes("utf-8"));} catch (Exception e1) {}
            if(log!=null)log.info(info+"失败,"+e.getMessage());
        } finally {
            if(os != null)try {os.close();} catch (IOException e1) {}
        }
        return null;
    }
    @RequestMapping("payWithdrawCashSucForTimeout")
    public String payWithdrawCashSucForTimeout(HttpServletRequest request,HttpServletResponse response) {
    	OutputStream os = null;
    	try {
			new PayWithdrawCashOrderService().updatePayWithdrawCashOrderStatus(request.getParameter("casordno"),"WITHDRAW_WAY","1");
		} catch (Exception e) {
			try {os.write(e.getMessage().getBytes("utf-8"));} catch (Exception e1) {}
			e.printStackTrace();
		}
    	String info = "提现成功-超时";
    	JWebUser user = (JWebUser)request.getSession().getAttribute("user");
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
            log = new Blog(this.getClass().getSimpleName()+".updateStatus",info+"PayWithdrawCashOrder",user.id,request);
            new PayWithdrawCashOrderService().updatePayWithdrawCashOrderStatus(request.getParameter("casordno"),
           		 request.getParameter("columName"),request.getParameter("value"));
            os.write(JWebConstant.OK.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
            try {os.write(e.getMessage().getBytes("utf-8"));} catch (Exception e1) {}
            if(log!=null)log.info(info+"失败,"+e.getMessage());
        } finally {
            if(os != null)try {os.close();} catch (IOException e1) {}
        }
        return null;
    }
    @RequestMapping("payWithdrawCashFail")
    public String payWithdrawCashFail(HttpServletRequest request,HttpServletResponse response) {
    	if(!"flag".equals(request.getParameter("flag"))) return this.updateStatus(request, response, "提现失败");
    	OutputStream os = null;
		try {
			new PayWithdrawCashOrderService().updatePayWithdrawCashOrderStatus(request.getParameter("casordno"),"WITHDRAW_WAY","1");
		} catch (Exception e) {
			try {os.write(e.getMessage().getBytes("utf-8"));} catch (Exception e1) {}
			e.printStackTrace();
		}
		String info = "提现失败-超时";
		JWebUser user = (JWebUser)request.getSession().getAttribute("user");
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
            log = new Blog(this.getClass().getSimpleName()+".updateStatus",info+"PayWithdrawCashOrder",user.id,request);
            new PayWithdrawCashOrderService().updatePayWithdrawCashOrderStatus(request.getParameter("casordno"),
           		 request.getParameter("columName"),request.getParameter("value"));
            os.write(JWebConstant.OK.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
            try {os.write(e.getMessage().getBytes("utf-8"));} catch (Exception e1) {}
            if(log!=null)log.info(info+"失败,"+e.getMessage());
        } finally {
            if(os != null)try {os.close();} catch (IOException e1) {}
        }
        return null;
    }
    @RequestMapping("payWithdrawCashBack")
    public String payWithdrawCashBack(HttpServletRequest request,HttpServletResponse response) {
    	String info = "返回支付帐号";
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
            log = new Blog(this.getClass().getSimpleName()+".updateStatus",info+"PayWithdrawCashOrder",user.id,request);
           
            //request.getParameter("value") 客户编号
            new PayWithdrawCashOrderService().updatePayWithdrawCashOrderReturnAccount(request.getParameter("casordno"),request.getParameter("value"));
            os.write(JWebConstant.OK.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
            try {os.write(e.getMessage().getBytes("utf-8"));} catch (Exception e1) {}
            if(log!=null)log.info(info+"失败,"+e.getMessage());
        } finally {
            if(os != null)try {os.close();} catch (IOException e1) {}
        }
        return null;
    }
    @RequestMapping("payWithdrawCashReset")
    public String payWithdrawCashReset(HttpServletRequest request,HttpServletResponse response) {
    	String info = "重新提现";
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
            log = new Blog(this.getClass().getSimpleName()+".updateStatus",info+"PayWithdrawCashOrder",user.id,request);
            /*new PayWithdrawCashOrderService().updatePayWithdrawCashOrderStatus(request.getParameter("casordno"),
           		 request.getParameter("columName"),request.getParameter("value"));*/
            new PayWithdrawCashOrderService().updatePayWithdrawCashOrderStatus(new String[]{request.getParameter("casordno"),"00",request.getParameter("value")});
            os.write(JWebConstant.OK.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
            try {os.write(e.getMessage().getBytes("utf-8"));} catch (Exception e1) {}
            if(log!=null)log.info(info+"失败,"+e.getMessage());
        } finally {
            if(os != null)try {os.close();} catch (IOException e1) {}
        }
        return null;
    }
    
    /**
     * excel导出
     * @param request
     */
    @RequestMapping("payWithdrawCashOrderListExportExcel")
    public String payWithdrawCashOrderListExportExcel(HttpServletRequest request,HttpServletResponse response,@ModelAttribute("payWithdrawCashOrder") PayWithdrawCashOrder payWithdrawCashOrder) {
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
            os.write(new PayWithdrawCashOrderService().payWithdrawCashOrderListExportExcel(payWithdrawCashOrder));
        } catch (Exception e) {
            e.printStackTrace();
            if(log!=null)log.info("导出失败,"+e.getMessage());
        } finally {
            if(os != null)try {os.close();} catch (IOException e1) {}
        }
        return null;
    }
    
    /**
     * 提现状态更改
     * @param request
     * @param response
     * @param info
     * @return
     */
    private String updateStatus(HttpServletRequest request,HttpServletResponse response,String info) {
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
             log = new Blog(this.getClass().getSimpleName()+".updateStatus",info+"PayWithdrawCashOrder",user.id,request);
             new PayWithdrawCashOrderService().updatePayWithdrawCashOrderStatus(request.getParameter("casordno"),
            		 request.getParameter("columName"),request.getParameter("value"));
             os.write(JWebConstant.OK.getBytes());
         } catch (Exception e) {
             e.printStackTrace();
             try {os.write(e.getMessage().getBytes("utf-8"));} catch (Exception e1) {}
             if(log!=null)log.info(info+"失败,"+e.getMessage());
         } finally {
             if(os != null)try {os.close();} catch (IOException e1) {}
         }
         return null;
	}
    /**
     * 提现接口（系统内部使用）
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("withdrawCash")
    public String withdrawCash(HttpServletRequest request,HttpServletResponse response) {
    	OutputStream os = null;
        try {
        	request.setCharacterEncoding("UTF-8");
            response.setContentType("text/html;charset=UTF-8");
        	os = response.getOutputStream();
        	//客户类型 0个人 1商户
        	if(!com.PayConstant.CUST_TYPE_USER.equals(request.getParameter("custType"))
        			&&!com.PayConstant.CUST_TYPE_MERCHANT.equals(request.getParameter("custType")))throw new Exception("客户类型错误");
        	if(request.getParameter("accountNo")==null)throw new Exception("提现账户错误");
        	//提现账户类型 1对私 2对公
        	if(!"1".equals(request.getParameter("userType"))
        			&&!"2".equals(request.getParameter("userType")))throw new Exception("用户类型错误");
        	//
        	if(request.getParameter("userType")!=null
        			&&request.getParameter("userType").equalsIgnoreCase("2")) {
        		if(request.getParameter("issuer").trim()==null)throw new Exception("对公业务联行号空值");
        	}
        	long amount = 0l;
        	try {
        		amount = Long.parseLong(request.getParameter("amount"));
        		if(amount <=0 || amount>10000000000l)throw new Exception("提现金额错误");
			} catch (Exception e) {throw new Exception("提现金额错误");}
        	if(request.getParameter("accountName")==null)throw new Exception("持卡人姓名错误");
        	if(request.getParameter("bankNo")==null)throw new Exception("银行编码错误");
        	if(request.getParameter("merchantNo")==null)throw new Exception("订单号错误");
        	if(request.getParameter("custId")==null)throw new Exception("转出账号错误");
        	//拼接参数值和平台密码值，形成签名元数据。
        	String src=request.getParameter("custType")+request.getParameter("custId")+request.getParameter("accountNo")
        			+request.getParameter("userType")+request.getParameter("amount")
        			+request.getParameter("bankNo")+request.getParameter("accountName")+
        			request.getParameter("merchantNo")+PayConstant.PAY_CONFIG.get("SYS_COMM_PWD_WITH_INNER_PLT");
        	String sign=request.getParameter("sign");
        	//验证签名。
        	if(sign==null || !sign.equals(SHA1.SHA1String(src)))throw new Exception("签名错误");
        	os.write(new PayWithdrawCashOrderService().withdrawCash(request).getBytes("utf-8"));
//            Map<String,String> res = new XFPayService().createWithdraw(request);
//            os.write(("{\"status\":\""+res.get("status")+"\",\"resCode\":\""+res.get("resCode")+"\",\"resMessage\":\""+res.get("resMessage")+"\"}").getBytes("utf-8"));
        } catch (Exception e) {
        	e.printStackTrace();
        	try {
				os.write(("{\"resCode\":\"-1\",\"ordstatus\":\"-1\",\"resMessage\":\""+e.getMessage()+"\"}").getBytes("utf-8"));
			} catch (IOException e1) {}
        } finally {
        	if(os !=null)try {os.close();} catch (Exception e2) {}
        }
        return null;
    }
    /**
     * 设提现结果
     * @param request
     * @param response
     * @param payWithdrawCashOrder
     * @return
     */
    @RequestMapping("setPayWithdrawCashResult")
    public String setResultSettlement(HttpServletRequest request,HttpServletResponse response,
    		@ModelAttribute("payWithdrawCashOrder") PayWithdrawCashOrder payWithdrawCashOrder) {
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
            log = new Blog(this.getClass().getSimpleName()+".setResultWithdrawCashOrder","提现结果设置PayWithdrawCashOrder",user.id,request);
            if(!"02".equals(payWithdrawCashOrder.ordstatus)
            	&&!"03".equals(payWithdrawCashOrder.ordstatus))throw new Exception("状态异常");
            PayWithdrawCashOrderService s = new PayWithdrawCashOrderService();
            PayWithdrawCashOrder tmp = s.detailPayWithdrawCashOrder(payWithdrawCashOrder.casordno);
            tmp.updateUser=user.id;
            tmp.updateTime=new Date();
            tmp.withdrawWay="1";
            tmp.bankerror = payWithdrawCashOrder.bankerror.trim();
            tmp.ordstatus = payWithdrawCashOrder.ordstatus;
            tmp.bankjrnno = payWithdrawCashOrder.bankjrnno;
            if("02".equals(payWithdrawCashOrder.ordstatus)){
            	tmp.payret="00";
            	tmp.sucTime=new Date();
            }
            else tmp.payret="01";
            s.setResultWithdrawCashOrder(tmp);
            os.write(JWebConstant.OK.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
            try {os.write(e.getMessage().getBytes("utf-8"));} catch (Exception e1) {}
            if(log!=null)log.info("提现结果设置失败,"+e.getMessage());
        } finally {
            if(os != null)try {os.close();} catch (IOException e1) {}
        }
        return null;
    }
    
//    /**
//     * 批量  提现接口（系统内部使用）
//     * @param request
//     * @param response
//     * @return
//     */
//    @RequestMapping("withdrawBatchCash")
//    public String withdrawBatchCash(HttpServletRequest request,HttpServletResponse response) {
//    	OutputStream os = null;
//        try {
//        	request.setCharacterEncoding("UTF-8");
//            response.setContentType("text/html;charset=UTF-8");
//        	os = response.getOutputStream();
//        	
//        	if(!"1".equals(request.getParameter("custType"))
//        			&&!"2".equals(request.getParameter("custType")))throw new Exception("客户类型错误");PayConstant.CUST_TYPE_MERCHANT
//        	
//        	if(request.getParameter("batchNo")==null)throw new Exception("批次号错误");
//        	if(request.getParameter("totalAmount")==null)throw new Exception("总金额错误");
//        	if(request.getParameter("count")==null)throw new Exception("总条数错误");
//        	if(request.getParameter("orders")==null)throw new Exception("订单信息错误");
//        	
//        	/**
//        	 * 校验订单信息中必要的信息是否为空值。
//        	 */
//        	JSONObject json = new JSONObject(request.getParameter("orders"));
//        	if(json.size()>0){
//				   for(int i=0;i<json.size();i++){
//				     JSONObject job = json.getJSONObject(i);  // 遍历 jsonarray 数组，把每一个对象转成 json 对象
//			        	if(!"1".equals(job.get("userType"))PayConstant.CUST_TYPE_MERCHANT
//			        			&&!"2".equals(job.get("userType")))throw new Exception("用户类型错误");PayConstant.CUST_TYPE_MERCHANT
//			        	if(job.get("userType")!=null&&"2".equals(job.get("userType")))
//			        	{
//			        		if(job.get("issuer")==null)throw new Exception("对公业务联行号空值");
//			        	}
//			        	long amount = 0l;
//			        	try {
//			        		amount = Long.parseLong(job.get("amount").toString());
//			        		if(amount <=0 || amount>10000000000l)throw new Exception("转账金额错误");
//						} catch (Exception e) {throw new Exception("转账金额错误");}
//			        	if(job.get("accountName")==null)throw new Exception("持卡人姓名错误");
//			        	if(job.get("bankNo")==null)throw new Exception("银行编码错误");
//			        	if(job.get("transCur")==null)throw new Exception("币种错误");
//			        	if(job.get("source")==null)throw new Exception("来源错误");
//			        	if(job.get("accountNo")==null)throw new Exception("提现账户错误");
//				}
//			}
//        	//拼接参数值和平台密码值，形成签名元数据。
//        	String src=request.getParameter("batchNo")+request.getParameter("totalAmount")
//        			+request.getParameter("count")+request.getParameter("orders")
//        			+PayConstant.PAY_CONFIG.get("SYS_COMM_PWD_WITH_INNER_PLT");
//        	String sign=request.getParameter("sign");
//        	//验证签名。
//        	if(sign==null || !sign.equals(SHA1.SHA1String(src)))throw new Exception("签名错误");
//        	os.write(new XFPayService().createWithdrawBatch(request).getBytes("utf-8"));
//
//        } catch (Exception e) {
//        	e.printStackTrace();
//        	try {
//				os.write(("{\"resCode\":\"-1\",\"resMessage\":\""+e.getMessage()+"\"}").getBytes("utf-8"));
//			} catch (IOException e1) {}
//        } finally {
//        	if(os !=null)try {os.close();} catch (Exception e2) {}
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