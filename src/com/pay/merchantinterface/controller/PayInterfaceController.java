package com.pay.merchantinterface.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import util.JWebConstant;
import util.PayUtil;
import util.Tools;

import com.PayConstant;
import com.pay.cardbin.dao.PayCardBin;
import com.pay.cardbin.service.PayCardBinService;
import com.pay.coopbank.dao.PayChannelRotation;
import com.pay.coopbank.dao.PayCoopBank;
import com.pay.coopbank.dao.PayCoopBankDAO;
import com.pay.coopbank.service.PayChannelRotationLogService;
import com.pay.coopbank.service.PayChannelRotationService;
import com.pay.coopbank.service.PayCoopBankService;
import com.pay.fee.service.PayFeeRateService;
import com.pay.merchantinterface.dao.PayTranUserQuickCard;
import com.pay.merchantinterface.service.BFBPayService;
import com.pay.merchantinterface.service.BFPayService;
import com.pay.merchantinterface.service.BNSPayService;
import com.pay.merchantinterface.service.CJPayService;
import com.pay.merchantinterface.service.CXPayService;
import com.pay.merchantinterface.service.CXWeiXinService;
import com.pay.merchantinterface.service.CYPayService;
import com.pay.merchantinterface.service.CertUtil;
import com.pay.merchantinterface.service.DHPayService;
import com.pay.merchantinterface.service.EjPayService;
import com.pay.merchantinterface.service.FY2PayService;
import com.pay.merchantinterface.service.FY3PayService;
import com.pay.merchantinterface.service.FYPayService;
import com.pay.merchantinterface.service.GFBPayService;
import com.pay.merchantinterface.service.GHTPayService;
import com.pay.merchantinterface.service.JDPayService;
import com.pay.merchantinterface.service.JYTPayService;
import com.pay.merchantinterface.service.KBNEWPayService;
import com.pay.merchantinterface.service.KBPayService;
import com.pay.merchantinterface.service.KLFPayService;
import com.pay.merchantinterface.service.LDPayService;
import com.pay.merchantinterface.service.MOBAOPayService;
import com.pay.merchantinterface.service.MerchantXmlUtil;
import com.pay.merchantinterface.service.NotifyInterface;
import com.pay.merchantinterface.service.PLTPayService;
import com.pay.merchantinterface.service.PayChannelService;
import com.pay.merchantinterface.service.PayInterfaceService;
import com.pay.merchantinterface.service.PayOrderInterfaceService;
import com.pay.merchantinterface.service.PayRequest;
import com.pay.merchantinterface.service.PayService;
import com.pay.merchantinterface.service.PayTranUserQuickCardService;
import com.pay.merchantinterface.service.QFPayService;
import com.pay.merchantinterface.service.RBPayService;
import com.pay.merchantinterface.service.SWTPayService;
import com.pay.merchantinterface.service.SXFNEWpayService;
import com.pay.merchantinterface.service.SXFpayService;
import com.pay.merchantinterface.service.SXYPayService;
import com.pay.merchantinterface.service.SYXPayService;
import com.pay.merchantinterface.service.TFBpayService;
import com.pay.merchantinterface.service.TTFPayService;
import com.pay.merchantinterface.service.WFTPayService;
import com.pay.merchantinterface.service.WLTPayService;
import com.pay.merchantinterface.service.XFPayService;
import com.pay.merchantinterface.service.YBPayService;
import com.pay.merchantinterface.service.YHJPayService;
import com.pay.merchantinterface.service.YLTFPayService;
import com.pay.merchantinterface.service.YSBPayService;
import com.pay.merchantinterface.service.YSPayService;
import com.pay.merchantinterface.service.YTZFPayService;
import com.pay.merchantinterface.service.YYTPayService;
import com.pay.merchantinterface.service.ZFTPayService;
import com.pay.merchantinterface.service.ZXPayService;
import com.pay.merchantinterface.service.ZX_BJ_PayService;
import com.pay.order.dao.PayOrder;
import com.pay.order.dao.PayProductOrder;
import com.pay.order.service.PayOrderService;
import com.pay.risk.service.RiskService;
import com.pay.user.dao.PayTranUserInfo;
import com.pay.user.service.PayTranUserInfoService;
/**
 * 商户接口处理类
 * @author Administrator
 *
 */
@Controller
public class PayInterfaceController {
	private static final Log log = LogFactory.getLog(PayInterfaceController.class);
	public String application = "";
	/**
	 * 商户接口 报文格式有两种，主要区别：是否赋给变量msg
	 * 一种【msg=PD94bW...g==|onRlXt...97FP5iA==】
	 * 一种【PD94bW...g==|onRlXt...97FP5iA==】
	 * @param request
	 * @param response
	 * @return
	 */
    @RequestMapping("pay")
    public String addPayOrder(HttpServletRequest request,HttpServletResponse response) {
    	PayRequest payRequest = null;
    	OutputStream os = null;
    	InputStream is = null;
        try {
//        	if(!"1".equals(PayConstant.PAY_CONFIG.get("server_type")))throw new Exception();
        	request.setCharacterEncoding("UTF-8");
            response.setContentType("text/html;charset=UTF-8");
//            System.out.println(request.getParameter("msg"));
			StringBuffer req = new StringBuffer("");
			is = request.getInputStream();
			byte[] b = new byte[1024];
			int len = -1;
			while((len = is.read(b)) != -1)req.append(new String(b,0,len,"utf-8"));
			String tmp = java.net.URLDecoder.decode(req.toString(), "utf-8");
			String msg= req.toString();
			String [] paras = Tools.split(tmp, "&");
			if(paras.length >= 1){
				for(int i=0;i<paras.length; i++){
					if(paras[i].startsWith("msg=")){
						msg = paras[i].substring(4);
						break;
					}
				}
			}
			String rIp = request.getHeader("x-forwarded-for");
			rIp=rIp==null?request.getRemoteHost():rIp;
			log.info(rIp+",msg=============\n"+msg);
			PayInterfaceService service = new PayInterfaceService(msg);
			//业务解析处理
            payRequest =  service.parseXml(request,this);
            try {
        		//Date tt = new java.text.SimpleDateFormat("yyyyMMddHHmmss").parse(payRequest.orderTime);
        		payRequest.orderTime = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
			} catch (Exception e) {}
            if(is!=null)is.close();
            //支付订单
            if("SubmitOrder".equals(payRequest.application)||"CertPayOrder".equals(payRequest.application)
            		||"CertPayOrderH5".equals(payRequest.application)||"WeiXinScanOrder".equals(payRequest.application)
            		||"WeiXinWapOrder".equals(payRequest.application)||"ZFBScanOrder".equals(payRequest.application)
            		||"QQScanOrder".equals(payRequest.application)){
            	request.getSession().setAttribute("payRequest", payRequest);
	            //操作成功，跳转到收银台或网银
	            if("000".equals(payRequest.respCode)){
	            	//网银支付
	            	if("SubmitOrder".equals(payRequest.application)||"CertPayOrderH5".equals(payRequest.application)){
	            		//快捷H5跳转
	            		if("CertPayOrderH5".equals(payRequest.application)){
	            			if(PayUtil.isMobile(request.getHeader("USER-AGENT")))request.getSession().setAttribute("client-type","mobile");
	            			//商户支持用户互通，取得绑定卡列表
	            			if("1".equals(payRequest.merchant.userInterflowFlag))request.setAttribute("bindCardList", 
	            					new PayService().getPayTranUserQuickCardByPayerId(payRequest.merchantId,payRequest.payerId));
	            			return "/jsp/pay/payinterface/checkoutquick.jsp";
	            		}
		            	//未指定银行/存在买家，跳到收银台
		            	if(payRequest.bankId==null || payRequest.bankId.length() == 0
		            			|| payRequest.tranUserMap.get(payRequest.payerId)!=null)return "/jsp/pay/payinterface/checkout.jsp";
		            	//指定银行，跳网银
		            	else {
		            		//选择最低手续费渠道
		            		String tranType = PayConstant.TRAN_TYPE_CONSUME;
		            		if("0".equals(payRequest.accountType))tranType=PayConstant.TRAN_TYPE_CONSUME_B2C_DEBIT;
		            		else if("1".equals(payRequest.accountType))tranType=PayConstant.TRAN_TYPE_CONSUME_B2C_CREBIT;
		            		else if("4".equals(payRequest.accountType))tranType=PayConstant.TRAN_TYPE_CONSUME_B2B;
		        			PayCoopBank payChannel = new PayFeeRateService().getMinFeeChannel(payRequest,"web",payRequest.bankId,
		        				tranType,payRequest.accountType,payRequest.payOrder.txamt);
		        			if(payChannel == null)throw new Exception("支付渠道错误");
		        			request.setAttribute("productOrder",payRequest.productOrder);
		        			request.setAttribute("payOrder",payRequest.payOrder);
		        			//跳转支付渠道后，启动查单线程(渠道掉单处理——渠道支付结果查询标识，是否主动查询，0不查询、1查询)
		        			if("1".equals(PayConstant.PAY_CONFIG.get("NOTIFY_SEARCH_CHANNEL_FLAG"))
		        					&&"1".equals(JWebConstant.SYS_CONFIG.get("DEBUG")))
		        				new PayChannelService().searchOrderForChannelNotifyFail(payRequest.payOrder.payordno);
		            		//先锋支付
		            		if(PayConstant.PAY_CONFIG.get("PAY_CHANNEL_XF").equals(payChannel.bankCode)){
	            				new XFPayService().pay(request,payRequest,payRequest.productOrder,payRequest.payOrder);
	            				if("0".equals(JWebConstant.SYS_CONFIG.get("DEBUG")))return "/jsp/pay/payinterface/test_jump.jsp";
	            				else return "/jsp/pay/payinterface/xf_jump.jsp";
		            		//摩宝支付
		            		} else if(PayConstant.PAY_CONFIG.get("PAY_CHANNEL_MOBAO").equals(payChannel.bankCode)){
		            			new MOBAOPayService().pay(request,payRequest,payRequest.productOrder,payRequest.payOrder);
		            			if("0".equals(JWebConstant.SYS_CONFIG.get("DEBUG")))return "/jsp/pay/payinterface/test_jump.jsp";
	            				else return "/jsp/pay/payinterface/mobao_jump.jsp";
	            			//融保支付
		            		} else if(PayConstant.PAY_CONFIG.get("PAY_CHANNEL_RB").equals(payChannel.bankCode)){
		            			new RBPayService().pay(request,payRequest,payRequest.productOrder,payRequest.payOrder);
		            			if("0".equals(JWebConstant.SYS_CONFIG.get("DEBUG")))return "/jsp/pay/payinterface/test_jump.jsp";
	            				else return "/jsp/pay/payinterface/rb_jump.jsp";
		            		//首信易支付
		            		} else if(PayConstant.PAY_CONFIG.get("PAY_CHANNEL_SXY").equals(payChannel.bankCode)){
		            			new SXYPayService().pay(request,payRequest,payRequest.productOrder,payRequest.payOrder);
		            			if("0".equals(JWebConstant.SYS_CONFIG.get("DEBUG")))return "/jsp/pay/payinterface/test_jump.jsp";
	            				else return "/jsp/pay/payinterface/sxy_jump.jsp";
		            		//富友支付。
		            		}else if(PayConstant.PAY_CONFIG.get("PAY_CHANNEL_FY").equals(payChannel.bankCode)){
		            			new FYPayService().pay(request,payRequest,payRequest.productOrder,payRequest.payOrder);
		            			if("0".equals(JWebConstant.SYS_CONFIG.get("DEBUG")))return "/jsp/pay/payinterface/test_jump.jsp";
	            				else return "/jsp/pay/payinterface/fy_jump.jsp";
		            		//富友2支付。
		            		}else if(PayConstant.PAY_CONFIG.get("PAY_CHANNEL_FY2").equals(payChannel.bankCode)){
		            			new FY2PayService().pay(request,payRequest,payRequest.productOrder,payRequest.payOrder);
		            			if("0".equals(JWebConstant.SYS_CONFIG.get("DEBUG")))return "/jsp/pay/payinterface/test_jump.jsp";
	            				else return "/jsp/pay/payinterface/fy_jump.jsp";
		            		//富友3支付。
		            		}else if(PayConstant.PAY_CONFIG.get("PAY_CHANNEL_FY3").equals(payChannel.bankCode)){
		            			new FY3PayService().pay(request,payRequest,payRequest.productOrder,payRequest.payOrder);
		            			if("0".equals(JWebConstant.SYS_CONFIG.get("DEBUG")))return "/jsp/pay/payinterface/test_jump.jsp";
	            				else return "/jsp/pay/payinterface/fy_jump.jsp";
		            		//畅捷支付。
		            		} else if(PayConstant.PAY_CONFIG.get("PAY_CHANNEL_CJ").equals(payChannel.bankCode)){
		            			new CJPayService().pay(request,payRequest,payRequest.productOrder,payRequest.payOrder);
		            			if("0".equals(JWebConstant.SYS_CONFIG.get("DEBUG")))return "/jsp/pay/payinterface/test_jump.jsp";
	            				else return "/jsp/pay/payinterface/cj_jump.jsp";
		            		//宝付支付。
		            		}else if(PayConstant.PAY_CONFIG.get("PAY_CHANNEL_BF").equals(payChannel.bankCode)){
		            			new BFPayService().pay(request,payRequest,payRequest.productOrder,payRequest.payOrder);
		            			if("0".equals(JWebConstant.SYS_CONFIG.get("DEBUG")))return "/jsp/pay/payinterface/test_jump.jsp";
		            			else return "/jsp/pay/payinterface/bf_jump.jsp";
		            		//京东支付
		            		} else if(PayConstant.PAY_CONFIG.get("PAY_CHANNEL_JD").equals(payChannel.bankCode)){
		            			new JDPayService().pay(request,payRequest,payRequest.productOrder,payRequest.payOrder);
		            			if("0".equals(JWebConstant.SYS_CONFIG.get("DEBUG")))return "/jsp/pay/payinterface/test_jump.jsp";
	            				else return "/jsp/pay/payinterface/jd_jump.jsp";
		            		}//银生宝支付
		            		else if(PayConstant.PAY_CONFIG.get("PAY_CHANNEL_YSB").equals(payChannel.bankCode)){
		            			new YSBPayService().pay(request,payRequest,payRequest.productOrder,payRequest.payOrder);
		            			if("0".equals(JWebConstant.SYS_CONFIG.get("DEBUG")))return "/jsp/pay/payinterface/test_jump.jsp";
	            				else return "/jsp/pay/payinterface/ysb_jump.jsp";
	            			//金运通网关支付。
		            		} else if(PayConstant.PAY_CONFIG.get("PAY_CHANNEL_JYT").equals(payChannel.bankCode)){
		            			new JYTPayService().pay(request,payRequest,payRequest.productOrder,payRequest.payOrder);
		            			if("0".equals(JWebConstant.SYS_CONFIG.get("DEBUG")))return "/jsp/pay/payinterface/test_jump.jsp";
	            				else return "/jsp/pay/payinterface/jyt_jump.jsp";
		            		//易势网关。
			            	}else if(PayConstant.PAY_CONFIG.get("PAY_CHANNEL_YS").equals(payChannel.bankCode)){
		            			new YSPayService().pay(request,payRequest,payRequest.productOrder,payRequest.payOrder);
		            			if("0".equals(JWebConstant.SYS_CONFIG.get("DEBUG")))return "/jsp/pay/payinterface/test_jump.jsp";
	            				else return "/jsp/pay/payinterface/ys_jump.jsp";
		            		//创新网关
			            	}else if(PayConstant.PAY_CONFIG.get("PAY_CHANNEL_CX").equals(payChannel.bankCode)){
		            			new CXPayService().pay(request,payRequest,payRequest.productOrder,payRequest.payOrder);
		            			if("0".equals(JWebConstant.SYS_CONFIG.get("DEBUG")))return "/jsp/pay/payinterface/test_jump.jsp";
	            				else return "/jsp/pay/payinterface/cx_jump.jsp";
		            		//银盈通网关。
			            	} else if(PayConstant.PAY_CONFIG.get("PAY_CHANNEL_YYT").equals(payChannel.bankCode)){
			            		if("0".equals(JWebConstant.SYS_CONFIG.get("DEBUG"))){
			    					return "/jsp/pay/payinterface/test_jump.jsp";
			    				}else{
			    					response.setCharacterEncoding("utf-8");
			    					response.setHeader("Content-type","text/html;charset=UTF-8");
			    					PrintWriter writer = response.getWriter();
			    					String res = new  YYTPayService().pay(request,payRequest,payRequest.productOrder,payRequest.payOrder);
			    					writer.write(res);
			    					writer.flush();
			    					writer.close();
			    					return null;
			    				}
		            		//易汇金
			            	} else if(PayConstant.PAY_CONFIG.get("PAY_CHANNEL_YHJ").equals(payChannel.bankCode)){
		            			if("0".equals(JWebConstant.SYS_CONFIG.get("DEBUG")))return "/jsp/pay/payinterface/test_jump.jsp";
	            				else {
	            					response.sendRedirect(new YHJPayService().pay(request,payRequest,payRequest.productOrder,payRequest.payOrder));
	            					return null;
	            				}
		            		//天付宝商户接口网关支付。
			            	}else if(PayConstant.PAY_CONFIG.get("PAY_CHANNEL_TFB").equals(payChannel.bankCode)){
		            			if("0".equals(JWebConstant.SYS_CONFIG.get("DEBUG")))return "/jsp/pay/payinterface/test_jump.jsp";
	            				else {
	            					String cipherData =new TFBpayService().pay(request,payRequest,payRequest.productOrder,payRequest.payOrder);
	            					response.sendRedirect(PayConstant.PAY_CONFIG.get("TFB_WG_PAY_URL") + "?cipher_data="+ URLEncoder.encode(cipherData, "utf-8"));
	            					return null;
	            				}
		            		//随行付商户接口网关支付。
			            	}else if(PayConstant.PAY_CONFIG.get("PAY_CHANNEL_SXF").equals(payChannel.bankCode)){
		            			if("0".equals(JWebConstant.SYS_CONFIG.get("DEBUG")))return "/jsp/pay/payinterface/test_jump.jsp";
	            				else {
	            					new SXFpayService().pay(request,payRequest,payRequest.productOrder,payRequest.payOrder);
	            					return "/jsp/pay/payinterface/sxf_jump.jsp";
	            				}
							//高汇通商户接口网关支付
			            	}else if(PayConstant.PAY_CONFIG.get("PAY_CHANNEL_GHT").equals(payChannel.bankCode)){
							    if("0".equals(JWebConstant.SYS_CONFIG.get("DEBUG")))return "/jsp/pay/payinterface/test_jump.jsp";
		            		    else {
									new GHTPayService().pay(request,payRequest,payRequest.productOrder,payRequest.payOrder);
									return "/jsp/pay/payinterface/ght_jump.jsp";
								}
							//国付宝商户接口网关支付
			            	}else if(PayConstant.PAY_CONFIG.get("PAY_CHANNEL_GFB").equals(payChannel.bankCode)) {
			            		if("0".equals(JWebConstant.SYS_CONFIG.get("DEBUG")))return "/jsp/pay/payinterface/test_jump.jsp";
			            		else {
			            			new GFBPayService().pay(request,payRequest,payRequest.productOrder,payRequest.payOrder);
			            			return "/jsp/pay/payinterface/gfb_jump.jsp";
			            		}
							//邦付宝商户接口网关支付
							}else if(PayConstant.PAY_CONFIG.get("PAY_CHANNEL_BFB").equals(payChannel.bankCode)){
		            			if("0".equals(JWebConstant.SYS_CONFIG.get("DEBUG")))return "/jsp/pay/payinterface/test_jump.jsp";
	            				else {
	            					new BFBPayService().pay(request,payRequest,payRequest.productOrder,payRequest.payOrder);
	            					return "/jsp/pay/payinterface/bfb_jump.jsp";
	            				}
		            		//亿联通付商户接口网关支付
			            	}else if(PayConstant.PAY_CONFIG.get("PAY_CHANNEL_YLTF").equals(payChannel.bankCode)){
		            			if("0".equals(JWebConstant.SYS_CONFIG.get("DEBUG")))return "/jsp/pay/payinterface/test_jump.jsp";
	            				else {
	            					new YLTFPayService().pay(request,payRequest,payRequest.productOrder,payRequest.payOrder);
	            					return "/jsp/pay/payinterface/yltf_jump.jsp";
	            				}
		            		//商银信商户接口网银支付。
			            	}else if(PayConstant.PAY_CONFIG.get("PAY_CHANNEL_SYX").equals(payChannel.bankCode)){
		            			if("0".equals(JWebConstant.SYS_CONFIG.get("DEBUG")))return "/jsp/pay/payinterface/test_jump.jsp";
	            				else {
	            					new SYXPayService().pay(request,payRequest,payRequest.productOrder,payRequest.payOrder);
	            					return "/jsp/pay/payinterface/syx_jump.jsp";
	            				}
		            		//联动商户接口网银支付
			            	}else if(PayConstant.PAY_CONFIG.get("PAY_CHANNEL_LD").equals(payChannel.bankCode)){
							    if("0".equals(JWebConstant.SYS_CONFIG.get("DEBUG")))return "/jsp/pay/payinterface/test_jump.jsp";
		            		    else {
									new LDPayService().pay(request,payRequest,payRequest.productOrder,payRequest.payOrder);
									return "/jsp/pay/payinterface/ld_jump.jsp";
								}
							  //易宝商户接口网银支付
			            	}else if(PayConstant.PAY_CONFIG.get("PAY_CHANNEL_YB").equals(payChannel.bankCode)){
							    if("0".equals(JWebConstant.SYS_CONFIG.get("DEBUG")))return "/jsp/pay/payinterface/test_jump.jsp";
		            		    else {
									new YBPayService().pay(request,payRequest,payRequest.productOrder,payRequest.payOrder);
									return "/jsp/pay/payinterface/yb_jump.jsp";
								}
							//统统付商户接口网银支付
			            	}else if(PayConstant.PAY_CONFIG.get("PAY_CHANNEL_TTF").equals(payChannel.bankCode)){
							    if("0".equals(JWebConstant.SYS_CONFIG.get("DEBUG")))return "/jsp/pay/payinterface/test_jump.jsp";
		            		    else {
									new TTFPayService().pay(request,payRequest,payRequest.productOrder,payRequest.payOrder);
									return "/jsp/pay/payinterface/ttf_jump.jsp";
								}
							//新酷宝收银台网关支付。
			            	}else if(PayConstant.PAY_CONFIG.get("PAY_CHANNEL_KBNEW").equals(payChannel.bankCode)){
			    				if("0".equals(JWebConstant.SYS_CONFIG.get("DEBUG")))return "/jsp/pay/payinterface/test_jump.jsp";
			    				else{
			    					new KBNEWPayService().pay(request,payRequest,payRequest.productOrder,payRequest.payOrder);
			    					return "/jsp/pay/payinterface/kb_new_jump.jsp";
			    				}
			    			//新随行付网银。
			            	}else if(PayConstant.PAY_CONFIG.get("PAY_CHANNEL_SXFNEW").equals(payChannel.bankCode)){
			    				if("0".equals(JWebConstant.SYS_CONFIG.get("DEBUG")))return "/jsp/pay/payinterface/test_jump.jsp";
			    				else{
			    					new SXFNEWpayService().pay(request,payRequest,payRequest.productOrder,payRequest.payOrder);
			    					return "/jsp/pay/payinterface/sxf_new_jump.jsp";
			    				}
			    			//易通支付网银。
			            	}else if(PayConstant.PAY_CONFIG.get("PAY_CHANNEL_YTZF").equals(payChannel.bankCode)){
			            		if("0".equals(JWebConstant.SYS_CONFIG.get("DEBUG"))){
			    					return "/jsp/pay/payinterface/test_jump.jsp";
			    				}else{
			    					response.setCharacterEncoding("utf-8");
			    					response.setHeader("Content-type","text/html;charset=UTF-8");
			    					PrintWriter writer = response.getWriter();
			    					String res = new  YTZFPayService().pay(request,payRequest,payRequest.productOrder,payRequest.payOrder);
			    					writer.write(res);
			    					writer.flush();
			    					writer.close();
			    					return null;
			    				}
			            	}
		            	}
		            //快捷支付
	            	} else if("CertPayOrder".equals(payRequest.application)){
	            		//取得卡类型，0借记卡、1贷记卡
	            		PayCardBin cardBin = PayCardBinService.getCardBinByCardNo(payRequest.cardNo);
	            		if(cardBin == null) throw new Exception("卡号非法");
            	    	//快捷支付路由,选择最低手续费渠道
	            		String tranType=PayConstant.TRAN_TYPE_CONSUME_QUICK_DEBIT;
	            		if("1".equals(cardBin.cardType))tranType=PayConstant.TRAN_TYPE_CONSUME_QUICK_CREBIT;
	        			PayCoopBank payChannel = new PayFeeRateService().getMinFeeChannel(payRequest,"quick",cardBin.bankCode,
	        					tranType,cardBin.cardType,payRequest.payOrder.txamt);
	        			//默认快捷走融宝
	        			if(payChannel==null)throw new Exception("快捷支付渠道错误（空）");
	        			String testXml="<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"no\"?>" +
	            				"<message merchantId=\""+payRequest.merchantId+"\" " +
	            						"merchantOrderId=\""+payRequest.merchantOrderId+"\" bindId=\"\" " +
	            						"respCode=\"000\" respDesc=\"下单成功（测试）\" " +
	            						"bankId=\""+cardBin.bankCode+"\" bankName=\""+cardBin.bankName+"\"/>";
	        			payRequest.bankId = cardBin.bankCode;
	            		payRequest.bankName = cardBin.bankName;
	            		payRequest.payOrder.bankCardType = cardBin.cardType;
	            		payRequest.payOrder.bankcod = payRequest.bankId;
	            		payRequest.payOrder.payChannel = payChannel.bankCode;
	            		new PayOrderService().updateOrderForBanks(payRequest.payOrder);
	        			//先锋支付
	        			if(PayConstant.PAY_CONFIG.get("PAY_CHANNEL_XF").equals(payChannel.bankCode)){
	        				String res = CertUtil.createTransStr(testXml);
	            			if("1".equals(JWebConstant.SYS_CONFIG.get("DEBUG"))) {
	            				String xml = new XFPayService().quickPay(request,payRequest,
										payRequest.productOrder,payRequest.payOrder);
	            				log.info(xml);
								res = CertUtil.createTransStr(xml);
							}
	            			if(os==null)os = response.getOutputStream();
	            			os.write(res.getBytes("utf-8"));
            				return null;
	        			//融保支付
	            		} else if (PayConstant.PAY_CONFIG.get("PAY_CHANNEL_RB").equals(payChannel.bankCode)){
	            			String res = CertUtil.createTransStr(testXml);
	            			if("1".equals(JWebConstant.SYS_CONFIG.get("DEBUG"))){
	            				String xml = new RBPayService().quickPay(request,payRequest,payRequest.productOrder,payRequest.payOrder);
	            				log.info(xml);
	            				res = CertUtil.createTransStr(xml);
	            			}
	            			if(os==null)os = response.getOutputStream();
	            			os.write(res.getBytes("utf-8"));
            				return null;
            			//畅捷快捷支付
	            		} else if (PayConstant.PAY_CONFIG.get("PAY_CHANNEL_CJ").equals(payChannel.bankCode)){
	            			String res = CertUtil.createTransStr(testXml);
	            			if("1".equals(JWebConstant.SYS_CONFIG.get("DEBUG"))) {
	            				String xml = new CJPayService().quickPay(request,payRequest,
										payRequest.productOrder,payRequest.payOrder);
	            				log.info(xml);
								res = CertUtil.createTransStr(xml);
							}
	            			if(os==null)os = response.getOutputStream();
	            			os.write(res.getBytes("utf-8"));
            				return null;
            			//宝付快捷支付 
	            		} else if (PayConstant.PAY_CONFIG.get("PAY_CHANNEL_BF").equals(payChannel.bankCode)){
	            			String res = CertUtil.createTransStr(testXml);
	            			if("1".equals(JWebConstant.SYS_CONFIG.get("DEBUG"))){
	            				String xml = new BFPayService().quickPay(request,payRequest,
		        						payRequest.productOrder,payRequest.payOrder);
	            				log.info(xml);
	            				res = CertUtil.createTransStr(xml);
	            			}
	            			if(os==null)os = response.getOutputStream();
	            			os.write(res.getBytes("utf-8"));
            				return null;
	        			//平台快捷支付。
	            		} else if (PayConstant.PAY_CONFIG.get("PAY_CHANNEL_PLTPAY").equals(payChannel.bankCode)){
	            			String res = CertUtil.createTransStr(testXml);
	            			if("1".equals(JWebConstant.SYS_CONFIG.get("DEBUG"))) {
	            				String xml = new PLTPayService().quickPay(request,payRequest,
										payRequest.productOrder,payRequest.payOrder);
	            				log.info(xml);
								res = CertUtil.createTransStr(xml);
							}
	            			if(os==null)os = response.getOutputStream();
	            			os.write(res.getBytes("utf-8"));
	        				return null;
	        			//富友快捷支付。
	            		} else if (PayConstant.PAY_CONFIG.get("PAY_CHANNEL_FY").equals(payChannel.bankCode)){
	            			String res = CertUtil.createTransStr(testXml);
	            			if("1".equals(JWebConstant.SYS_CONFIG.get("DEBUG"))){
	            				String xml = new FYPayService().quickPay(request,payRequest,
		        						payRequest.productOrder,payRequest.payOrder);
	            				log.info(xml);
	            				res = CertUtil.createTransStr(xml);
	            			}
	            			if(os==null)os = response.getOutputStream();
	            			os.write(res.getBytes("utf-8"));
            				return null;
	            		}else if (PayConstant.PAY_CONFIG.get("PAY_CHANNEL_FY2").equals(payChannel.bankCode)){
	            			String res = CertUtil.createTransStr(testXml);
	            			if("1".equals(JWebConstant.SYS_CONFIG.get("DEBUG"))){
	            				String xml = new FY2PayService().quickPay(request,payRequest,
		        						payRequest.productOrder,payRequest.payOrder);
	            				log.info(xml);
	            				res = CertUtil.createTransStr(xml);
	            			}
	            			if(os==null)os = response.getOutputStream();
	            			os.write(res.getBytes("utf-8"));
            				return null;
	            		}else if (PayConstant.PAY_CONFIG.get("PAY_CHANNEL_FY3").equals(payChannel.bankCode)){
	            			String res = CertUtil.createTransStr(testXml);
	            			if("1".equals(JWebConstant.SYS_CONFIG.get("DEBUG"))){
	            				String xml = new FY3PayService().quickPay(request,payRequest,
		        						payRequest.productOrder,payRequest.payOrder);
	            				log.info(xml);
	            				res = CertUtil.createTransStr(xml);
	            			}
	            			if(os==null)os = response.getOutputStream();
	            			os.write(res.getBytes("utf-8"));
            				return null;
            			//京东支付快捷支付。
	            		} else if(PayConstant.PAY_CONFIG.get("PAY_CHANNEL_JD").equals(payChannel.bankCode)){
	            			String res = CertUtil.createTransStr(testXml);
	            			if("1".equals(JWebConstant.SYS_CONFIG.get("DEBUG"))) {
	            				String xml = new JDPayService().quickPay(request,payRequest,
										payRequest.productOrder,payRequest.payOrder);
	            				log.info(xml);
								res = CertUtil.createTransStr(xml);
							}
	            			if(os==null)os = response.getOutputStream();
	            			os.write(res.getBytes("utf-8"));
	        				return null;
	        			//易联快捷支付。
	            		}else if(PayConstant.PAY_CONFIG.get("PAY_CHANNEL_YL").equals(payChannel.bankCode)){
	            			String res = CertUtil.createTransStr(testXml);
//	            			if("1".equals(JWebConstant.SYS_CONFIG.get("DEBUG"))) {
//								res = CertUtil.createTransStr(new YLPayService().quickPay(request,payRequest,
//										payRequest.productOrder,payRequest.payOrder));
//							}
	            			if(os==null)os = response.getOutputStream();
	            			os.write(res.getBytes("utf-8"));
	        				return null;
	        			//金运通快捷支付。
	            		} else if(PayConstant.PAY_CONFIG.get("PAY_CHANNEL_JYT").equals(payChannel.bankCode)){
	            			String res = CertUtil.createTransStr(testXml);
	            			if("1".equals(JWebConstant.SYS_CONFIG.get("DEBUG"))) {
	            				String xml = new JYTPayService().quickPay(request,payRequest,
										payRequest.productOrder,payRequest.payOrder);
	            				log.info(xml);
								res = CertUtil.createTransStr(xml);
							}
	            			if(os==null)os = response.getOutputStream();
	            			os.write(res.getBytes("utf-8"));
	        				return null;
	        			//创新快捷支付。
	            		}else if (PayConstant.PAY_CONFIG.get("PAY_CHANNEL_CX").equals(payChannel.bankCode)){
	            			String res = CertUtil.createTransStr(testXml);
	            			if("1".equals(JWebConstant.SYS_CONFIG.get("DEBUG"))){
	            				String xml = new CXPayService().quickPay(request,payRequest,
		        						payRequest.productOrder,payRequest.payOrder);
	            				log.info(xml);
	            				res = CertUtil.createTransStr(xml);
	            			}
	            			if(os==null)os = response.getOutputStream();
	            			os.write(res.getBytes("utf-8"));
            				return null;
            			//高汇通快捷支付 
	            		} else if (PayConstant.PAY_CONFIG.get("PAY_CHANNEL_GHT").equals(payChannel.bankCode)){
	            			String res = CertUtil.createTransStr(testXml);
	            			if("1".equals(JWebConstant.SYS_CONFIG.get("DEBUG"))) {
	            				String xml = new GHTPayService().quickPay(request,payRequest,
										payRequest.productOrder,payRequest.payOrder);
	            				log.info(xml);
								res = CertUtil.createTransStr(xml);
							}
	            			if(os==null)os = response.getOutputStream();
	            			os.write(res.getBytes("utf-8"));
            				return null;
						//邦付宝快捷支付。
	            		} else if (PayConstant.PAY_CONFIG.get("PAY_CHANNEL_BFB").equals(payChannel.bankCode)){
	            			String res = CertUtil.createTransStr(testXml);
	            			if("1".equals(JWebConstant.SYS_CONFIG.get("DEBUG"))){
	            				String xml = new BFBPayService().quickPay(request,payRequest,
		        						payRequest.productOrder,payRequest.payOrder);
	            				log.info(xml);
	            				res = CertUtil.createTransStr(xml);
	            			}
	            			if(os==null)os = response.getOutputStream();
	            			os.write(res.getBytes("utf-8"));
            				return null;
            			//联动快捷支付 
	            		}else if (PayConstant.PAY_CONFIG.get("PAY_CHANNEL_LD").equals(payChannel.bankCode)){
	            			String res = CertUtil.createTransStr(testXml);
	            			if("1".equals(JWebConstant.SYS_CONFIG.get("DEBUG"))) {
	            				String xml = new LDPayService().quickPay(request,payRequest,
										payRequest.productOrder,payRequest.payOrder);
	            				log.info(xml);
								res = CertUtil.createTransStr(xml);
							}
	            			if(os==null)os = response.getOutputStream();
	            			os.write(res.getBytes("utf-8"));
            				return null;
            			//长盈快捷支付
	            		} else if (PayConstant.PAY_CONFIG.get("PAY_CHANNEL_CY").equals(payChannel.bankCode)) {
	            			String res = CertUtil.createTransStr(testXml);
	            			if("1".equals(JWebConstant.SYS_CONFIG.get("DEBUG"))) {
	            				String xml = new CYPayService().quickPay(request,payRequest,
										payRequest.productOrder,payRequest.payOrder);
	            				log.info(xml);
								res = CertUtil.createTransStr(xml);
	            			}
	            			if(os==null)os = response.getOutputStream();
	            			os.write(res.getBytes("utf-8"));
            				return null;
            				//沃雷特快捷支付
	            		} else if (PayConstant.PAY_CONFIG.get("PAY_CHANNEL_WLT").equals(payChannel.bankCode)) {
	            			String res = CertUtil.createTransStr(testXml);
	            			if("1".equals(JWebConstant.SYS_CONFIG.get("DEBUG"))) {
	            				String xml = new WLTPayService().quickPay(request,payRequest,
										payRequest.productOrder,payRequest.payOrder);
	            				log.info(xml);
								res = CertUtil.createTransStr(xml);
	            			}
	            			if(os==null)os = response.getOutputStream();
	            			os.write(res.getBytes("utf-8"));
            				return null;
	            		}else throw new Exception("不支持的银行卡号/无支付通道");
            		} else if("WeiXinScanOrder".equals(payRequest.application)){ //微信扫码
	        			String channelId = PayCoopBankService.MERCHANT_CHANNEL_MAP.get(payRequest.merchantId+",10");//通过商户指定渠道获取
	        			PayCoopBankDAO channelDAO = new PayCoopBankDAO();
	        			PayCoopBank payChannel = null;
	        			PayChannelRotation channelRotation = null;
	        			boolean rotationFlag = false;
	        			if(channelId != null)payChannel = channelDAO.getChannelById(channelId);//取得微信通道
	        			else {//通过渠道轮询取得渠道信息
	        				channelRotation = new PayChannelRotationService().getPayChannelRotationByRule(payRequest);
		        			if(channelRotation != null){
		        				payChannel = channelDAO.getChannelById(channelRotation.channelId);
		        				rotationFlag = true;
		        			}
	        			}
	        			if(payChannel==null)payChannel = channelDAO.getChannelById(PayConstant.PAY_CONFIG.get("WEI_XIN_SCAN_CHANNEL"));//取得微信通道（通用指定）
	        			if(payChannel==null)payChannel = new PayFeeRateService().getMinFeeChannelForScan(payRequest,"10",payRequest.payOrder.txamt);//通过手续费最低取得
	        			//判断金额范围
	        			if(payChannel != null){
	        				Long maxAmt = PayFeeRateService.CHANNEL_TRAN_MAX_AMT.get(payChannel.bankCode+",10");
	        				if(maxAmt>0&&payRequest.payOrder.txamt>maxAmt)throw new Exception("超出最大限额"+String.format("%.2f", ((double)maxAmt)/100d)+"元");
	        			}
	        			if(payChannel==null)throw new Exception("微信扫码支付渠道错误（空）");
	        			String supported[] = payChannel.payScanFlag.split(",");
	        			if(supported.length<3||"0".equals(supported[1]))throw new Exception("渠道"+payChannel.bankCode+"不支持微信扫码支付");
	        			String testXml="<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"no\"?>" +
	        				"<message merchantId=\""+payRequest.merchantId+"\" " +
	        					"merchantOrderId=\""+payRequest.merchantOrderId+"\" codeUrl=\""+
	        					(PayConstant.PAY_CONFIG.get("WEI_XIN_SCAN_TEST_URL")==null?"":
	        						PayConstant.PAY_CONFIG.get("WEI_XIN_SCAN_TEST_URL")+"?payordno="+payRequest.payOrder.payordno)
	        					+"\"  respCode=\"000\" respDesc=\"下单成功（测试）\"/>";
	        			payRequest.bankId = "";
	            		payRequest.bankName = "";
	            		payRequest.payOrder.bankCardType = "";
	            		payRequest.payOrder.bankcod = "";
	            		payRequest.payOrder.payChannel = payChannel.bankCode;
	            		if(rotationFlag&&channelRotation != null)new PayChannelRotationLogService().addChannelRotationLog(payRequest,channelRotation.merchantId);//添加渠道轮询信息
	            		new PayOrderService().updateOrderForBanks(payRequest.payOrder);
	        			String res = null;
	        			if("1".equals(JWebConstant.SYS_CONFIG.get("DEBUG"))){//正式环境
	        				//创新微信扫码
	        				if(payChannel.bankCode.equals(PayConstant.PAY_CONFIG.get("PAY_CHANNEL_CX"))){
	        					String xml = new CXWeiXinService().scanPay(request,payRequest,payRequest.productOrder,payRequest.payOrder,"WECHAT");
	        					log.info(xml);
	        					res = CertUtil.createTransStr(xml);
	        				//雅酷微信扫码
//	        				} else if(payChannel.bankCode.equals(PayConstant.PAY_CONFIG.get("PAY_CHANNEL_YK"))){
//	        					String xml = new YKPayService().scanPay_New(request,payRequest,payRequest.productOrder,payRequest.payOrder,"wcpay");
//	        					log.info(xml);
//	        					res = CertUtil.createTransStr(xml);
	        				//中信-广州微信扫码
	        				} else if(payChannel.bankCode.equals(PayConstant.PAY_CONFIG.get("PAY_CHANNEL_ZX"))){
	        					String xml = new ZXPayService().scanPay(request,payRequest,payRequest.productOrder,payRequest.payOrder,"pay.weixin.native",channelRotation);
	        					log.info(xml);
	        					res = CertUtil.createTransStr(xml);
	        				//商物通微信扫码。
	        				}else if(payChannel.bankCode.equals(PayConstant.PAY_CONFIG.get("PAY_CHANNEL_SWT"))){
	        					String xml = new SWTPayService().scanPay(request,payRequest,payRequest.productOrder,payRequest.payOrder,"CP00000017");
	        					log.info(xml);
	        					res = CertUtil.createTransStr(xml);
	        				//中信北京微信扫码。
	        				}else if(payChannel.bankCode.equals(PayConstant.PAY_CONFIG.get("PAY_CHANNEL_ZXBJ"))){
	        					String xml = new ZX_BJ_PayService().scanPay(request,payRequest,payRequest.productOrder,payRequest.payOrder,"pay.weixin.native",channelRotation);
	        					log.info(xml);
	        					res = CertUtil.createTransStr(xml);
	        				//雅酷-道合微信扫码。
	        				}/**
	        				else if(payChannel.bankCode.equals(PayConstant.PAY_CONFIG.get("PAY_CHANNEL_DH"))){
	        					String xml = new DHPayService().scanPay(request,payRequest,payRequest.productOrder,payRequest.payOrder,"wcpay");
	        					log.info(xml);
	        					res = CertUtil.createTransStr(xml);
	        				//网上有名微信扫码。
	        				}else if(payChannel.bankCode.equals(PayConstant.PAY_CONFIG.get("PAY_CHANNEL_WSYM"))){
	        					String xml = new WSYMPayService().scanPay(request,payRequest,payRequest.productOrder,payRequest.payOrder,"BGWXZF");
	        					log.info(xml);
	        					res = CertUtil.createTransStr(xml);
	        				//恒丰微信扫码
	        				}else if(payChannel.bankCode.equals(PayConstant.PAY_CONFIG.get("PAY_CHANNEL_HF"))){
	        					String xml = new HFPayService().scanPay(request,payRequest,payRequest.productOrder,payRequest.payOrder,"wechat005");
	        					log.info(xml);
	        					res = CertUtil.createTransStr(xml);
	        				//北京农商
	        				}*/else if(payChannel.bankCode.equals(PayConstant.PAY_CONFIG.get("PAY_CHANNEL_BNS"))){
	        					String xml = new BNSPayService().scanPay(request,payRequest,payRequest.productOrder,payRequest.payOrder,"wechat");
	        					log.info(xml);
	        					res = CertUtil.createTransStr(xml);
	        					//支付通微信扫码。
	        				}else if(payChannel.bankCode.equals(PayConstant.PAY_CONFIG.get("PAY_CHANNEL_ZFT"))){
	        					String xml = new ZFTPayService().scanPay(request,payRequest,payRequest.productOrder,payRequest.payOrder,"wechat",channelRotation);
	        					log.info(xml);
	        					res = CertUtil.createTransStr(xml);
	        					//易势微信扫码。
	        				}else if(payChannel.bankCode.equals(PayConstant.PAY_CONFIG.get("PAY_CHANNEL_YS"))){
	        					String xml = new YSPayService().scanPay(request,payRequest,payRequest.productOrder,payRequest.payOrder,"1");
	        					log.info(xml);
	        					res = CertUtil.createTransStr(xml);
	        					//酷宝微信扫码。
	        				}else if(payChannel.bankCode.equals(PayConstant.PAY_CONFIG.get("PAY_CHANNEL_KB"))){
	        					String xml = new KBPayService().scanPay(request,payRequest,payRequest.productOrder,payRequest.payOrder,"12");
	        					log.info(xml);
	        					res = CertUtil.createTransStr(xml);
	        				//溢+ 微信扫码。
	        				}else if(payChannel.bankCode.equals(PayConstant.PAY_CONFIG.get("PAY_CHANNEL_EMAX"))){
	        					String xml = new EjPayService().scanPay(request,payRequest,payRequest.productOrder,payRequest.payOrder,"1");
	        					log.info(xml);
	        					res = CertUtil.createTransStr(xml);
	        				//酷宝新扫码接口。
	        				}else if(payChannel.bankCode.equals(PayConstant.PAY_CONFIG.get("PAY_CHANNEL_KBNEW"))){
	        					String xml = new KBNEWPayService().scanPay(request,payRequest,payRequest.productOrder,payRequest.payOrder,"WXZF");
	        					log.info(xml);
	        					res = CertUtil.createTransStr(xml);
	        				//钱方微信扫码。
	        				}else if(payChannel.bankCode.equals(PayConstant.PAY_CONFIG.get("PAY_CHANNEL_QF"))){
	        					String xml = new QFPayService().scanPay(request,payRequest,payRequest.productOrder,payRequest.payOrder,"800201");
	        					log.info(xml);
	        					res = CertUtil.createTransStr(xml);
	        				//快乐付微信扫码
	        				}else if(payChannel.bankCode.equals(PayConstant.PAY_CONFIG.get("PAY_CHANNEL_KLF"))){
	        					String xml = new KLFPayService().scanPay(request,payRequest,payRequest.payOrder,payRequest.productOrder,"WEIXIN_NATIVE");
	        					log.info(xml);
	        					res = CertUtil.createTransStr(xml);
	        				}
	        			}
	        			else res = CertUtil.createTransStr(testXml);
	        			if(os==null)os = response.getOutputStream();
	        			os.write(res.getBytes("utf-8"));
	    				return null;
            		} else if("WeiXinWapOrder".equals(payRequest.application)){ //微信WAP
            			String channelId = PayCoopBankService.MERCHANT_CHANNEL_MAP.get(payRequest.merchantId+",18");//通过商户指定渠道获取
            			PayCoopBankDAO channelDAO = new PayCoopBankDAO();
	        			PayCoopBank payChannel = null;
	        			PayChannelRotation channelRotation = null;
	        			boolean rotationFlag = false;
	        			if(channelId != null)payChannel = channelDAO.getChannelById(channelId);//取得微信通道
	        			else {//通过渠道轮询取得渠道信息
	        				channelRotation = new PayChannelRotationService().getPayChannelRotationByRule(payRequest);
		        			if(channelRotation != null){
		        				payChannel = channelDAO.getChannelById(channelRotation.channelId);
		        				rotationFlag = true;
		        			}
	        			}
	        			if(payChannel==null)payChannel = channelDAO.getChannelById(PayConstant.PAY_CONFIG.get("WEI_XIN_WAP_CHANNEL"));//取得微信通道（通用指定）
	        			if(payChannel==null)payChannel = new PayFeeRateService().getMinFeeChannelForScan(payRequest,"18",payRequest.payOrder.txamt);//通过手续费最低取得
	        			//判断金额范围
	        			if(payChannel != null){
	        				Long maxAmt = PayFeeRateService.CHANNEL_TRAN_MAX_AMT.get(payChannel.bankCode+",18");
	        				if(maxAmt>0&&payRequest.payOrder.txamt>maxAmt)throw new Exception("超出最大限额"+String.format("%.2f", ((double)maxAmt)/100d)+"元");
	        			}
	        			if(payChannel==null)throw new Exception("微信WAP支付渠道错误（空）");
	        			String supported[] = payChannel.payScanFlag.split(",");
	        			if(supported.length<3||"0".equals(supported[2]))throw new Exception("渠道"+payChannel.bankCode+"不支持微信WAP支付");
	        			String testXml="<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"no\"?>" +
	        				"<message merchantId=\""+payRequest.merchantId+"\" " +
	        					"merchantOrderId=\""+payRequest.merchantOrderId+"\" codeUrl=\""+
	        					(PayConstant.PAY_CONFIG.get("WEI_XIN_SCAN_TEST_URL")==null?"":
	        						PayConstant.PAY_CONFIG.get("WEI_XIN_SCAN_TEST_URL")+"?payordno="+payRequest.payOrder.payordno)
	        					+"\"  respCode=\"000\" respDesc=\"下单成功（测试）\"/>";
	        			payRequest.bankId = "";
	            		payRequest.bankName = "";
	            		payRequest.payOrder.bankCardType = "";
	            		payRequest.payOrder.bankcod = "";
	            		payRequest.payOrder.payChannel = payChannel.bankCode;
	            		if(rotationFlag&&channelRotation != null)new PayChannelRotationLogService().addChannelRotationLog(payRequest,channelRotation.merchantId);//添加渠道轮询信息
	            		new PayOrderService().updateOrderForBanks(payRequest.payOrder);
	        			String res = null;
	        			if("1".equals(JWebConstant.SYS_CONFIG.get("DEBUG"))){//生产模式
	        				
	        			}
	        			else res = CertUtil.createTransStr(testXml);
	        			if(os==null)os = response.getOutputStream();
	        			os.write(res.getBytes("utf-8"));
	    				return null;
	            	} else if("ZFBScanOrder".equals(payRequest.application)){//支付宝扫码
	            		String channelId = PayCoopBankService.MERCHANT_CHANNEL_MAP.get(payRequest.merchantId+",17");//通过商户指定渠道获取
	        			PayCoopBank payChannel = null;
	        			PayCoopBankDAO channelDAO = new PayCoopBankDAO();
	        			PayChannelRotation channelRotation = null;
	        			boolean rotationFlag = false;
	        			if(channelId != null)payChannel = channelDAO.getChannelById(channelId);//取得微信通道
	        			else {//通过渠道轮询取得渠道信息
	        				channelRotation = new PayChannelRotationService().getPayChannelRotationByRule(payRequest);
		        			if(channelRotation != null){
		        				payChannel = channelDAO.getChannelById(channelRotation.channelId);
		        				rotationFlag = true;
		        			}
	        			}
	        			if(payChannel==null)payChannel = channelDAO.getChannelById(PayConstant.PAY_CONFIG.get("ZFB_SCAN_CHANNEL"));//取得支付宝默认通道（通用指定）
	        			if(payChannel==null)payChannel = new PayFeeRateService().getMinFeeChannelForScan(payRequest,"17",payRequest.payOrder.txamt);//通过手续费最低取得
	        			//判断金额范围
	        			if(payChannel != null){
	        				Long maxAmt = PayFeeRateService.CHANNEL_TRAN_MAX_AMT.get(payChannel.bankCode+",17");
	        				if(maxAmt>0&&payRequest.payOrder.txamt>maxAmt)throw new Exception("超出最大限额"+String.format("%.2f", ((double)maxAmt)/100d)+"元");
	        			}
	        			if(payChannel==null)throw new Exception("支付宝扫码支付渠道错误（空）");
	        			String supported[] = payChannel.payScanFlag.split(",");
	        			if(supported.length<3||"0".equals(supported[0]))throw new Exception("渠道"+payChannel.bankCode+"不支持支付宝扫码支付");
	        			String testXml="<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"no\"?>" +
	        				"<message merchantId=\""+payRequest.merchantId+"\" " +
	        					"merchantOrderId=\""+payRequest.merchantOrderId+"\" codeUrl=\""+
	        					(PayConstant.PAY_CONFIG.get("WEI_XIN_SCAN_TEST_URL")==null?"":
	        						PayConstant.PAY_CONFIG.get("WEI_XIN_SCAN_TEST_URL")+"?payordno="+payRequest.payOrder.payordno)
	        					+"\"  respCode=\"000\" respDesc=\"下单成功（测试）\"/>";
	        			payRequest.bankId = "";
	            		payRequest.bankName = "";
	            		payRequest.payOrder.bankCardType = "";
	            		payRequest.payOrder.bankcod = "";
	            		payRequest.payOrder.payChannel = payChannel.bankCode;
	            		if(rotationFlag&&channelRotation != null)new PayChannelRotationLogService().addChannelRotationLog(payRequest,channelRotation.merchantId);//添加渠道轮询信息
	            		new PayOrderService().updateOrderForBanks(payRequest.payOrder);
	        			String res = null;
	        			if("1".equals(JWebConstant.SYS_CONFIG.get("DEBUG"))){//正式环境
//	        				//雅酷支付宝扫码。
//	        				if(payChannel.bankCode.equals(PayConstant.PAY_CONFIG.get("PAY_CHANNEL_YK"))){
//	        					String xml = new YKPayService().scanPay_New(request,payRequest,payRequest.productOrder,payRequest.payOrder,"alipay");
//	        					log.info(xml);
//	        					res = CertUtil.createTransStr(xml);
//	        				//创新支付宝扫码。
//	        				} else 
	        				if(payChannel.bankCode.equals(PayConstant.PAY_CONFIG.get("PAY_CHANNEL_CX"))){
		        				String xml=new CXWeiXinService().scanPay(request,payRequest,payRequest.productOrder,payRequest.payOrder,"ALIPAY");
	        					log.info(xml);
	        					res = CertUtil.createTransStr(xml);
	        				//中信支付宝扫码。
	        				} else if(payChannel.bankCode.equals(PayConstant.PAY_CONFIG.get("PAY_CHANNEL_ZX"))){
		        				String xml=new ZXPayService().scanPay(request,payRequest,payRequest.productOrder,payRequest.payOrder,"pay.alipay.native",channelRotation);
	        					log.info(xml);
	        					res = CertUtil.createTransStr(xml);
		        			//商物通支付宝扫码。
		        			}else if(payChannel.bankCode.equals(PayConstant.PAY_CONFIG.get("PAY_CHANNEL_SWT"))){
		        				String xml=new SWTPayService().scanPay(request,payRequest,payRequest.productOrder,payRequest.payOrder,"CP00000018");
	        					log.info(xml);
	        					res = CertUtil.createTransStr(xml);
		        			//中信北京支付宝扫码。
		        			}else if(payChannel.bankCode.equals(PayConstant.PAY_CONFIG.get("PAY_CHANNEL_ZXBJ"))){
		        				String xml=new ZX_BJ_PayService().scanPay(request,payRequest,payRequest.productOrder,payRequest.payOrder,"pay.alipay.native",channelRotation);
	        					log.info(xml);
	        					res = CertUtil.createTransStr(xml);
		        			//雅酷-道合
		        			}/**else if(payChannel.bankCode.equals(PayConstant.PAY_CONFIG.get("PAY_CHANNEL_DH"))){
		        				String xml=new DHPayService().scanPay(request,payRequest,payRequest.productOrder,payRequest.payOrder,"alipay");
	        					log.info(xml);
	        					res = CertUtil.createTransStr(xml);
	        				//网上有名支付宝扫码。
		        			}else if(payChannel.bankCode.equals(PayConstant.PAY_CONFIG.get("PAY_CHANNEL_WSYM"))){
		        				String xml=new WSYMPayService().scanPay(request,payRequest,payRequest.productOrder,payRequest.payOrder,"BGZFBZF");
	        					log.info(xml);
	        					res = CertUtil.createTransStr(xml);
	        				//恒丰支付宝扫码。
		        			}else if(payChannel.bankCode.equals(PayConstant.PAY_CONFIG.get("PAY_CHANNEL_HF"))){
	        					String xml = new HFPayService().scanPay(request,payRequest,payRequest.productOrder,payRequest.payOrder,"alipay005");
	        					log.info(xml);
	        					res = CertUtil.createTransStr(xml);
	        				//支付通支付宝扫码。
	        				}*/else if(payChannel.bankCode.equals(PayConstant.PAY_CONFIG.get("PAY_CHANNEL_ZFT"))){
	        					String xml = new ZFTPayService().scanPay(request,payRequest,payRequest.productOrder,payRequest.payOrder,"alipay",channelRotation);
	        					log.info(xml);
	        					res = CertUtil.createTransStr(xml);
	        				//易势支付宝扫码。
	        				}else if(payChannel.bankCode.equals(PayConstant.PAY_CONFIG.get("PAY_CHANNEL_YS"))){
	        					String xml = new YSPayService().scanPay(request,payRequest,payRequest.productOrder,payRequest.payOrder,"2");
	        					log.info(xml);
	        					res = CertUtil.createTransStr(xml);
	        				//酷宝支付宝扫码。
	        				}else if(payChannel.bankCode.equals(PayConstant.PAY_CONFIG.get("PAY_CHANNEL_KB"))){
	        					String xml = new KBPayService().scanPay(request,payRequest,payRequest.productOrder,payRequest.payOrder,"22");
	        					log.info(xml);
	        					res = CertUtil.createTransStr(xml);
	        				//溢+ 支付宝扫码。
	        				}else if(payChannel.bankCode.equals(PayConstant.PAY_CONFIG.get("PAY_CHANNEL_EMAX"))){
	        					String xml = new EjPayService().scanPay(request,payRequest,payRequest.productOrder,payRequest.payOrder,"2");
	        					log.info(xml);
	        					res = CertUtil.createTransStr(xml);
	        				//酷宝新扫码接口。
	        				}else if(payChannel.bankCode.equals(PayConstant.PAY_CONFIG.get("PAY_CHANNEL_KBNEW"))){
	        					String xml = new KBNEWPayService().scanPay(request,payRequest,payRequest.productOrder,payRequest.payOrder,"ZFBZF");
	        					log.info(xml);
	        					res = CertUtil.createTransStr(xml);
	        				//钱房支付宝扫码。
	        				}else if(payChannel.bankCode.equals(PayConstant.PAY_CONFIG.get("PAY_CHANNEL_QF"))){
	        					String xml = new QFPayService().scanPay(request,payRequest,payRequest.productOrder,payRequest.payOrder,"800101");
	        					log.info(xml);
	        					res = CertUtil.createTransStr(xml);
	        				//快乐付支付宝扫码
	        				}else if(payChannel.bankCode.equals(PayConstant.PAY_CONFIG.get("PAY_CHANNEL_KLF"))){
	        					String xml = new KLFPayService().scanPay(request,payRequest,payRequest.payOrder,payRequest.productOrder,"ALIPAY_NATIVE");
	        					log.info(xml);
	        					res = CertUtil.createTransStr(xml);
	        				}
	        			} else res = CertUtil.createTransStr(testXml);
	        			if(os==null)os = response.getOutputStream();
	        			os.write(res.getBytes("utf-8"));
	    				return null;
	            	} else if("ZFBWapOrder".equals(payRequest.application)){//支付宝WAP
	            		String channelId = PayCoopBankService.MERCHANT_CHANNEL_MAP.get(payRequest.merchantId+",17");//通过商户指定渠道获取
	        			PayCoopBank payChannel = null;
	        			PayCoopBankDAO channelDAO = new PayCoopBankDAO();
	        			if(channelId != null)payChannel = channelDAO.getChannelById(channelId);//取得支付宝默认通道
	        			if(payChannel==null)payChannel = channelDAO.getChannelById(PayConstant.PAY_CONFIG.get("ZFB_SCAN_CHANNEL"));//取得支付宝默认通道（通用指定）
	        			if(payChannel==null)payChannel = new PayFeeRateService().getMinFeeChannelForScan(payRequest,"17",payRequest.payOrder.txamt);//通过手续费最低取得
	        			if(payChannel==null)throw new Exception("支付宝扫码支付渠道错误（空）");
	        			String supported[] = payChannel.payScanFlag.split(",");
	        			if(supported.length<3||"0".equals(supported[4]))throw new Exception("渠道"+payChannel.bankCode+"不支持支付宝扫码支付");
	        			String testXml="<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"no\"?>" +
	        				"<message merchantId=\""+payRequest.merchantId+"\" " +
	        					"merchantOrderId=\""+payRequest.merchantOrderId+"\" codeUrl=\""+
	        					(PayConstant.PAY_CONFIG.get("WEI_XIN_SCAN_TEST_URL")==null?"":
	        						PayConstant.PAY_CONFIG.get("WEI_XIN_SCAN_TEST_URL")+"?payordno="+payRequest.payOrder.payordno)
	        					+"\"  respCode=\"000\" respDesc=\"下单成功（测试）\"/>";
	        			payRequest.bankId = "";
	            		payRequest.bankName = "";
	            		payRequest.payOrder.bankCardType = "";
	            		payRequest.payOrder.bankcod = "";
	            		payRequest.payOrder.payChannel = payChannel.bankCode;
	            		new PayOrderService().updateOrderForBanks(payRequest.payOrder);
	        			String res = null;
	        			if("1".equals(JWebConstant.SYS_CONFIG.get("DEBUG"))){//正式环境
	        				//雅酷支付宝扫码。
//	        				if(payChannel.bankCode.equals(PayConstant.PAY_CONFIG.get("PAY_CHANNEL_YK"))){
//	        					String xml = new YKPayService().scanPay_New(request,payRequest,payRequest.productOrder,payRequest.payOrder,"alipay");
//	        					log.info(xml);
//	        					res = CertUtil.createTransStr(xml);
//	        				//创新支付宝扫码。
//	        				} else 
	        				if(payChannel.bankCode.equals(PayConstant.PAY_CONFIG.get("PAY_CHANNEL_CX"))){
		        				String xml=new CXWeiXinService().scanPay(request,payRequest,payRequest.productOrder,payRequest.payOrder,"ALIPAY");
	        					log.info(xml);
	        					res = CertUtil.createTransStr(xml);
	        				//中信支付宝扫码。
	        				} else if(payChannel.bankCode.equals(PayConstant.PAY_CONFIG.get("PAY_CHANNEL_ZX"))){
//		        				String xml=new ZXPayService().scanPay(request,payRequest,payRequest.productOrder,payRequest.payOrder,"pay.alipay.native");
//	        					log.info(xml);
//	        					res = CertUtil.createTransStr(xml);
		        			//商物通支付宝扫码。
		        			}else if(payChannel.bankCode.equals(PayConstant.PAY_CONFIG.get("PAY_CHANNEL_SWT"))){
		        				String xml=new SWTPayService().scanPay(request,payRequest,payRequest.productOrder,payRequest.payOrder,"CP00000018");
	        					log.info(xml);
	        					res = CertUtil.createTransStr(xml);
		        			//中信北京支付宝扫码。
		        			}else if(payChannel.bankCode.equals(PayConstant.PAY_CONFIG.get("PAY_CHANNEL_ZXBJ"))){
//		        				String xml=new ZX_BJ_PayService().scanPay(request,payRequest,payRequest.productOrder,payRequest.payOrder,"pay.alipay.native");
//	        					log.info(xml);
//	        					res = CertUtil.createTransStr(xml);
		        			//雅酷-道合
		        			}else if(payChannel.bankCode.equals(PayConstant.PAY_CONFIG.get("PAY_CHANNEL_DH"))){
		        				String xml=new DHPayService().scanPay(request,payRequest,payRequest.productOrder,payRequest.payOrder,"alipay");
	        					log.info(xml);
	        					res = CertUtil.createTransStr(xml);
	        				//酷宝支付宝扫码。
		        			}else if(payChannel.bankCode.equals(PayConstant.PAY_CONFIG.get("PAY_CHANNEL_KB"))){
		        				String xml=new KBPayService().scanPay(request,payRequest,payRequest.productOrder,payRequest.payOrder,"22");
	        					log.info(xml);
	        					res = CertUtil.createTransStr(xml);
		        			}
	        			} else res = CertUtil.createTransStr(testXml);
	        			if(os==null)os = response.getOutputStream();
	        			os.write(res.getBytes("utf-8"));
	    				return null;
	            	} else if("QQScanOrder".equals(payRequest.application)){//QQ扫码
	            		String channelId = PayCoopBankService.MERCHANT_CHANNEL_MAP.get(payRequest.merchantId+",27");//通过商户指定渠道获取
	            		PayCoopBankDAO channelDAO = new PayCoopBankDAO();
	        			PayCoopBank payChannel = null;
	        			PayChannelRotation channelRotation = null;
	        			boolean rotationFlag = false;
	        			if(channelId != null)payChannel = channelDAO.getChannelById(channelId);//取得QQ通道
	        			else {//通过渠道轮询取得渠道信息
	        				channelRotation = new PayChannelRotationService().getPayChannelRotationByRule(payRequest);
		        			if(channelRotation != null){
		        				payChannel = channelDAO.getChannelById(channelRotation.channelId);
		        				rotationFlag = true;
		        			}
	        			}
	        			if(payChannel==null)payChannel = channelDAO.getChannelById(PayConstant.PAY_CONFIG.get("QQ_SCAN_CHANNEL"));//取得支付宝默认通道（通用指定）
	        			if(payChannel==null)payChannel = new PayFeeRateService().getMinFeeChannelForScan(payRequest,"27",payRequest.payOrder.txamt);//通过手续费最低取得
	        			//判断金额范围
	        			if(payChannel != null){
	        				Long maxAmt = PayFeeRateService.CHANNEL_TRAN_MAX_AMT.get(payChannel.bankCode+",27");
	        				if(maxAmt>0&&payRequest.payOrder.txamt>maxAmt)throw new Exception("超出最大限额"+String.format("%.2f", ((double)maxAmt)/100d)+"元");
	        			}
	        			if(payChannel==null)throw new Exception("QQ扫码支付渠道错误（空）");
	        			String supported[] = payChannel.payScanFlag.split(",");
	        			if(supported.length<3||"0".equals(supported[3]))throw new Exception("渠道"+payChannel.bankCode+"不支持QQ扫码支付");
	        			String testXml="<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"no\"?>" +
	        				"<message merchantId=\""+payRequest.merchantId+"\" " +
	        					"merchantOrderId=\""+payRequest.merchantOrderId+"\" codeUrl=\""+
	        					(PayConstant.PAY_CONFIG.get("WEI_XIN_SCAN_TEST_URL")==null?"":
	        						PayConstant.PAY_CONFIG.get("WEI_XIN_SCAN_TEST_URL")+"?payordno="+payRequest.payOrder.payordno)
	        					+"\"  respCode=\"000\" respDesc=\"下单成功（测试）\"/>";
	        			payRequest.bankId = "";
	            		payRequest.bankName = "";
	            		payRequest.payOrder.bankCardType = "";
	            		payRequest.payOrder.bankcod = "";
	            		payRequest.payOrder.payChannel = payChannel.bankCode;
	            		if(rotationFlag&&channelRotation != null)new PayChannelRotationLogService().addChannelRotationLog(payRequest,channelRotation.merchantId);//添加渠道轮询信息
	            		new PayOrderService().updateOrderForBanks(payRequest.payOrder);
	        			String res = null;
	        			if("1".equals(JWebConstant.SYS_CONFIG.get("DEBUG"))){//正式环境
	        				if(payChannel.bankCode.equals(PayConstant.PAY_CONFIG.get("PAY_CHANNEL_ZXBJ"))){//中信QQ扫码。
		        				String xml=new WFTPayService().scanPayQQ(request,payRequest,payRequest.productOrder,payRequest.payOrder,channelRotation);
	        					log.info(xml);
	        					res = CertUtil.createTransStr(xml);
	        				} else if(payChannel.bankCode.equals(PayConstant.PAY_CONFIG.get("PAY_CHANNEL_EMAX"))) {
	        					//扫码类型，1 微信,2 支付宝,5 QQ扫码
	        					String xml=new EjPayService().scanPay(request,payRequest,payRequest.productOrder,payRequest.payOrder,"5");
	        					log.info(xml);
	        					res = CertUtil.createTransStr(xml);
	        				}
	        			} else res = CertUtil.createTransStr(testXml);
	        			if(os==null)os = response.getOutputStream();
	        			os.write(res.getBytes("utf-8"));
	    				return null;
	            	}
	            } else {
	            	if("SubmitOrder".equals(payRequest.application)||"CertPayOrderH5".equals(payRequest.application)){
		            	//操作失败跳转到提示页面
		            	request.setAttribute("payRequest", payRequest);
		            	return "/jsp/pay/payinterface/info.jsp";
	            	} else {
	            		if(os==null)os = response.getOutputStream();
	            		PayRequest tmpRequest = new PayRequest();
	            		tmpRequest.merchantId=payRequest.merchantId;
	            		tmpRequest.merchantOrderId=payRequest.merchantOrderId;
	            		tmpRequest.bindId=payRequest.bindId;
	            		tmpRequest.respCode=payRequest.respCode;
	            		tmpRequest.respDesc=payRequest.respDesc;
	            		tmpRequest.payMode="";
	            		String xml = new MerchantXmlUtil().errorToXml(tmpRequest);
	            		log.info(xml);
	            		os.write(CertUtil.createTransStr(xml).getBytes("utf-8"));
	            	}
	            }
            } else if("QueryOrder".equals(payRequest.application)
            		||"RefundOrder".equals(payRequest.application)
            		||"CancelOrder".equals(payRequest.application)){
            	//其他操作，直接输出操作结果
            	if(os==null)os = response.getOutputStream();
                os.write(payRequest.responseStr.getBytes("utf-8"));
            } else if("AccountFile".equals(payRequest.application)){
            	//对账文件输出
            	if(payRequest.accountFilePath.length()==0){
            		if(os==null)os = response.getOutputStream();
            		os.write(service.createErrorZipFile(payRequest));
            		return null;
            	}
            	if(!new File(payRequest.accountFilePath).exists()){
            		payRequest.accountFilePath=
            				JWebConstant.APP_PATH+"/dat/merchant_account_file/null.zip";
            	}
            	FileInputStream fis = new FileInputStream(payRequest.accountFilePath);
            	if(os==null)os = response.getOutputStream();
    			while ((len = fis.read(b)) != -1)os.write(b, 0, len);
            } else if("CertPayReSms".equals(payRequest.application)){
            	if(os==null)os = response.getOutputStream();
                os.write(payRequest.responseStr.getBytes("utf-8"));
            } else if("CertPayConfirm".equals(payRequest.application)){
            	if(os==null)os = response.getOutputStream();
                os.write(payRequest.responseStr.getBytes("utf-8"));
            } else if("GuaranteeNotice".equals(payRequest.application)){
            	if(os==null)os = response.getOutputStream();
                os.write(payRequest.responseStr.getBytes("utf-8"));
            } else if("RealNameAuth".equals(payRequest.application)){
            	if(os==null)os = response.getOutputStream();
                os.write(payRequest.responseStr.getBytes("utf-8"));
            } else if("ReceivePay".equals(payRequest.application)){
            	if(os==null)os = response.getOutputStream();
                os.write(payRequest.responseStr.getBytes("utf-8"));
            } else if("ReceivePayBatch".equals(payRequest.application)){
            	throw new Exception("此功能已关闭");
//            	if(os==null)os = response.getOutputStream();
//                os.write(payRequest.responseStr.getBytes("utf-8"));
            } else if("ReceivePayQuery".equals(payRequest.application)){
            	if(os==null)os = response.getOutputStream();
                os.write(payRequest.responseStr.getBytes("utf-8"));
            } else throw new Exception("未知的应用");
        } catch (Exception e) {
        	//e.printStackTrace();
        	log.info(PayUtil.exceptionToString(e));
        	String orderInfo = "";
        	try {orderInfo=payRequest.merchantId;} catch (Exception e2) {}
        	payRequest = new PayRequest();
        	payRequest.application = application;
        	payRequest.respCode="-1";
        	if(e.getMessage()!=null&&e.getMessage().indexOf("ORA-00001")!=-1)payRequest.respDesc = "商户订单号重复"+(orderInfo.length()==0?"":"("+orderInfo+")");
        	else payRequest.respDesc = e.getMessage()==null?"空指针":e.getMessage();
        	if("SubmitOrder".equals(payRequest.application)||"CertPayOrderH5".equals(payRequest.application)){
        		request.setAttribute("payRequest", payRequest);
        		return "/jsp/pay/payinterface/info.jsp";
        	} else {
        		try {
        			if(os==null)os = response.getOutputStream();
        			String xml = new MerchantXmlUtil().errorToXml(payRequest);
        			log.info(xml);
            		os.write(CertUtil.createTransStr(xml).getBytes("utf-8"));
				} catch (Exception e2) {}
        	}
        } finally {
        	if(os!=null)try {os.close();} catch (Exception e2) {}
        	if(is!=null)try {is.close();} catch (Exception e2) {}
        }
        return null;
    }
    /**
     * 测试银行通知地址
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("testNotify")
    public String testNotify(HttpServletRequest request,HttpServletResponse response) {
        try {
            new NotifyInterface().testNofify(request.getParameter("payordno"));
        } catch (Exception e) {
        	//e.printStackTrace();
        	log.info(PayUtil.exceptionToString(e));
        	PayRequest payRequest = new PayRequest();
        	payRequest.respCode="-1";
        	payRequest.respDesc = e.getMessage();
            request.setAttribute("payRequest", payRequest);
            return "/jsp/pay/payinterface/info.jsp";
        }
        return null;
    }
    
    /**
     * 确认付款页面跳转
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("confirmThePayment")
    public String confirmThePayment(HttpServletRequest request,HttpServletResponse response) {
    	String bankId = request.getParameter("banks");//*银行编号
    	String bankCardType = request.getParameter("bankCardType");
    	PayRequest payRequest = (PayRequest)request.getSession().getAttribute("payRequest");
		try {
			if(payRequest == null)throw new Exception("支付已过期");
			if("0".equals(bankCardType)||"1".equals(bankCardType)||"4".equals(bankCardType))payRequest.accountType=bankCardType;
			if(!"00".equals(payRequest.payOrder.ordstatus))throw new Exception("订单已经处理完成");//订单已经处理完成
			if(payRequest.payOrder.payChannel!=null&&payRequest.payOrder.payChannel.length()>0)throw new Exception("订单重复提交");
			payRequest.payOrder.paytype="01";
			//账户余额支付
			if("ACCOUNT".equals(bankId)){
				request.setAttribute("bankId", bankId);
				PayTranUserInfoService ptuiService = new PayTranUserInfoService();
				if(payRequest.tranUserMap.get(payRequest.payerId) == null){
					//验证码判断
					String validCode = (String)request.getSession().getAttribute("validCode");
					if(validCode == null || request.getParameter("validCode") == null ||
							!validCode.equals(request.getParameter("validCode").toLowerCase())){
						request.getSession().setAttribute("validCode",null);//每个验证码使用后更新
						request.setAttribute("info", "图片验证码错误");
						return "/jsp/pay/payinterface/checkout.jsp";
					}
					//用户密码判断
					String info = ptuiService.checkLoginPwd(payRequest,request);
					request.setAttribute("info", info);
					return "/jsp/pay/payinterface/checkout.jsp";
				}
				String info = ptuiService.checkPayPwd(
						(PayTranUserInfo)payRequest.tranUserMap.get(payRequest.payerId),request.getParameter("payPwd"));
				//支付密码判断
				if(info.length()>0){
					request.setAttribute("info", info);
					return "/jsp/pay/payinterface/checkout.jsp";
				}
				payRequest.payOrder.paytype="00";
				//用户网银支付风控检查
				info = new RiskService().checkUserLimit(payRequest,payRequest.payerId,PayConstant.TRAN_TYPE_CONSUME);
				//违反风控规则
				if(info!=null && info.length()>0){
					request.setAttribute("info", info);
					return "/jsp/pay/payinterface/checkout.jsp";
				}
				//支付处理
				info = new PayService().pay(payRequest,request);
				if(info.length()>0){
					request.setAttribute("info", info);
					return "/jsp/pay/payinterface/checkout.jsp";
				}
				//汇总用户账户支付信息
				if(!payRequest.isSaveUserSum){
					new com.pay.risk.service.PayRiskDayTranSumService().updateRiskTranSumForUser(payRequest);
					payRequest.isSaveUserSum = true;
				}
				request.getSession().removeAttribute("payRequest");
				request.setAttribute("payRequest", payRequest);
				return "/jsp/pay/payinterface/account_pay_over.jsp";
			}
			request.setCharacterEncoding("UTF-8");
			PayProductOrder prdOrder = payRequest.productOrder;
			PayOrder payOrder = payRequest.payOrder;
			String accountType = request.getParameter("bankCardType");//*卡种
			if(!PayConstant.CARD_BIN_TYPE_JIEJI.equals(accountType)
					&&!PayConstant.CARD_BIN_TYPE_DAIJI.equals(accountType)
					&&!"4".equals(accountType))throw new Exception("卡种非法");
			if(PayCoopBankService.BANK_CODE_NAME_MAP.get(bankId) == null)throw new Exception("银行非法");
			//取得交易费率最小的渠道
			String tranType = "";
			if("0".equals(payRequest.accountType))tranType=PayConstant.TRAN_TYPE_CONSUME_B2C_DEBIT;
    		else if("1".equals(payRequest.accountType))tranType=PayConstant.TRAN_TYPE_CONSUME_B2C_CREBIT;
    		else if("4".equals(payRequest.accountType))tranType=PayConstant.TRAN_TYPE_CONSUME_B2B;
			PayCoopBank payChannel = new PayFeeRateService().getMinFeeChannel(payRequest,"web",bankId,
					tranType,request.getParameter("bankCardType"),payOrder.txamt);
			payOrder.bankCardType = accountType;
			if(payChannel == null)throw new Exception("支付渠道错误");
			request.setAttribute("productOrder",prdOrder);
			request.setAttribute("payOrder",payOrder);
			//汇总用户网银支付信息
			if(!payRequest.isSaveUserSum){
				new com.pay.risk.service.PayRiskDayTranSumService().updateRiskTranSumForUser(payRequest);
				payRequest.isSaveUserSum = true;
			}
			//用户网银支付风控检查
			if(payRequest.tranUserMap.get(payRequest.payerId) != null){
				String info = new RiskService().checkUserLimit(payRequest,payRequest.payerId,PayConstant.TRAN_TYPE_CONSUME);
				//违反风控规则
				if(info!=null && info.length()>0){
					request.setAttribute("webInfo", info);
					return "/jsp/pay/payinterface/checkout.jsp";
				}
			}
			//（直连网银）跳转支付渠道后，启动查单线程(渠道掉单处理——渠道支付结果查询标识，是否主动查询，0不查询、1查询)
			if("1".equals(PayConstant.PAY_CONFIG.get("NOTIFY_SEARCH_CHANNEL_FLAG"))
					&&"1".equals(JWebConstant.SYS_CONFIG.get("DEBUG")))
				new PayChannelService().searchOrderForChannelNotifyFail(payRequest.payOrder.payordno);
			//先锋接口
			if(PayConstant.PAY_CONFIG.get("PAY_CHANNEL_XF").equals(payChannel.bankCode)){
				new XFPayService().pay(request,prdOrder,payOrder);
				if("0".equals(JWebConstant.SYS_CONFIG.get("DEBUG")))return "/jsp/pay/payinterface/test_jump.jsp";
				else return "/jsp/pay/payinterface/xf_jump.jsp";
			//摩宝接口
			} else if(PayConstant.PAY_CONFIG.get("PAY_CHANNEL_MOBAO").equals(payChannel.bankCode)){
				new MOBAOPayService().pay(request,prdOrder,payOrder);
				if("0".equals(JWebConstant.SYS_CONFIG.get("DEBUG")))return "/jsp/pay/payinterface/test_jump.jsp";
				else return "/jsp/pay/payinterface/mobao_jump.jsp";
			//融保支付
			} else if(PayConstant.PAY_CONFIG.get("PAY_CHANNEL_RB").equals(payChannel.bankCode)){
				new RBPayService().pay(request,prdOrder,payOrder);
				if("0".equals(JWebConstant.SYS_CONFIG.get("DEBUG")))return "/jsp/pay/payinterface/test_jump.jsp";
				else return "/jsp/pay/payinterface/rb_jump.jsp";
			//首信易支付
			} else if(PayConstant.PAY_CONFIG.get("PAY_CHANNEL_SXY").equals(payChannel.bankCode)){
				new SXYPayService().pay(request,prdOrder,payOrder);
				if("0".equals(JWebConstant.SYS_CONFIG.get("DEBUG")))return "/jsp/pay/payinterface/test_jump.jsp";
				else return "/jsp/pay/payinterface/sxy_jump.jsp";
			//富友支付。
			} else if(PayConstant.PAY_CONFIG.get("PAY_CHANNEL_FY").equals(payChannel.bankCode)){
				new FYPayService().pay(request,prdOrder,payOrder);
				if("0".equals(JWebConstant.SYS_CONFIG.get("DEBUG")))return "/jsp/pay/payinterface/test_jump.jsp";
				else return "/jsp/pay/payinterface/fy_jump.jsp";
			//富友支付2
			}else if(PayConstant.PAY_CONFIG.get("PAY_CHANNEL_FY2").equals(payChannel.bankCode)){
				new FY2PayService().pay(request,prdOrder,payOrder);
				if("0".equals(JWebConstant.SYS_CONFIG.get("DEBUG")))return "/jsp/pay/payinterface/test_jump.jsp";
				else return "/jsp/pay/payinterface/fy_jump.jsp";
			//富友支付3
			}else if(PayConstant.PAY_CONFIG.get("PAY_CHANNEL_FY3").equals(payChannel.bankCode)){
				new FY3PayService().pay(request,prdOrder,payOrder);
				if("0".equals(JWebConstant.SYS_CONFIG.get("DEBUG")))return "/jsp/pay/payinterface/test_jump.jsp";
				else return "/jsp/pay/payinterface/fy_jump.jsp";
			//畅捷支付。
			} else if(PayConstant.PAY_CONFIG.get("PAY_CHANNEL_CJ").equals(payChannel.bankCode)){
				new CJPayService().pay(request,prdOrder,payOrder);
				if("0".equals(JWebConstant.SYS_CONFIG.get("DEBUG")))return "/jsp/pay/payinterface/test_jump.jsp";
				else return "/jsp/pay/payinterface/cj_jump.jsp";
			//宝付支付。
			} else if(PayConstant.PAY_CONFIG.get("PAY_CHANNEL_BF").equals(payChannel.bankCode)){
				new BFPayService().pay(request,prdOrder,payOrder);
				if("0".equals(JWebConstant.SYS_CONFIG.get("DEBUG")))return "/jsp/pay/payinterface/test_jump.jsp";
				else return "/jsp/pay/payinterface/bf_jump.jsp";
			//京东支付
			} else if(PayConstant.PAY_CONFIG.get("PAY_CHANNEL_JD").equals(payChannel.bankCode)){
				new JDPayService().pay(request,prdOrder,payOrder);
				if("0".equals(JWebConstant.SYS_CONFIG.get("DEBUG")))return "/jsp/pay/payinterface/test_jump.jsp";
				else return "/jsp/pay/payinterface/jd_jump.jsp";
			//银生宝网关支付。
			} else if(PayConstant.PAY_CONFIG.get("PAY_CHANNEL_YSB").equals(payChannel.bankCode)){
				new YSBPayService().pay(request,prdOrder,payOrder);
				if("0".equals(JWebConstant.SYS_CONFIG.get("DEBUG")))return "/jsp/pay/payinterface/test_jump.jsp";
				else return "/jsp/pay/payinterface/ysb_jump.jsp";
			//金运通网关支付。
			}else if(PayConstant.PAY_CONFIG.get("PAY_CHANNEL_JYT").equals(payChannel.bankCode)){
				new JYTPayService().pay(request,prdOrder,payOrder);
				if("0".equals(JWebConstant.SYS_CONFIG.get("DEBUG")))return "/jsp/pay/payinterface/test_jump.jsp";
				else return "/jsp/pay/payinterface/jyt_jump.jsp";
			//易势收银台网关。
			}else if(PayConstant.PAY_CONFIG.get("PAY_CHANNEL_YS").equals(payChannel.bankCode)){
				new YSPayService().pay(request,prdOrder,payOrder);
				if("0".equals(JWebConstant.SYS_CONFIG.get("DEBUG")))return "/jsp/pay/payinterface/test_jump.jsp";
				else return "/jsp/pay/payinterface/ys_jump.jsp";
			//创新网关。
			}else if(PayConstant.PAY_CONFIG.get("PAY_CHANNEL_CX").equals(payChannel.bankCode)){
				new CXPayService().pay(request,prdOrder,payOrder);
				if("0".equals(JWebConstant.SYS_CONFIG.get("DEBUG")))return "/jsp/pay/payinterface/test_jump.jsp";
				else return "/jsp/pay/payinterface/cx_jump.jsp";
			//易汇金
        	} else if(PayConstant.PAY_CONFIG.get("PAY_CHANNEL_YHJ").equals(payChannel.bankCode)){
    			if("0".equals(JWebConstant.SYS_CONFIG.get("DEBUG")))return "/jsp/pay/payinterface/test_jump.jsp";
				else {
					response.sendRedirect(new YHJPayService().pay(request,payRequest.productOrder,payRequest.payOrder));
					return null;
				}
			//天付宝收银台网关支付。
        	}else if(PayConstant.PAY_CONFIG.get("PAY_CHANNEL_TFB").equals(payChannel.bankCode)){
        		if("0".equals(JWebConstant.SYS_CONFIG.get("DEBUG")))return "/jsp/pay/payinterface/test_jump.jsp";
        		else {
        			String cipherData =new TFBpayService().pay(request,prdOrder,payOrder);
        			response.sendRedirect(PayConstant.PAY_CONFIG.get("TFB_WG_PAY_URL") + "?cipher_data="+ URLEncoder.encode(cipherData, "utf-8"));
        			return null;
        		}
        	//随行付收银台网关支付。
        	}else if(PayConstant.PAY_CONFIG.get("PAY_CHANNEL_SXF").equals(payChannel.bankCode)){
        		if("0".equals(JWebConstant.SYS_CONFIG.get("DEBUG")))return "/jsp/pay/payinterface/test_jump.jsp";
        		else {
        			new SXFpayService().pay(request,prdOrder,payOrder);
        			return "/jsp/pay/payinterface/sxf_jump.jsp";
        		}
        	//新：随行付收银台网关支付。
        	}else if(PayConstant.PAY_CONFIG.get("PAY_CHANNEL_SXFNEW").equals(payChannel.bankCode)){
        		if("0".equals(JWebConstant.SYS_CONFIG.get("DEBUG")))return "/jsp/pay/payinterface/test_jump.jsp";
        		else {
        			new SXFNEWpayService().pay(request,prdOrder,payOrder);
        			return "/jsp/pay/payinterface/sxf_new_jump.jsp";
        		}
        	//高汇通收银台网关支付
        	}else if(PayConstant.PAY_CONFIG.get("PAY_CHANNEL_GHT").equals(payChannel.bankCode)){
				if("0".equals(JWebConstant.SYS_CONFIG.get("DEBUG")))return "/jsp/pay/payinterface/test_jump.jsp";
				else{
					new GHTPayService().pay(request,prdOrder,payOrder);
					return "/jsp/pay/payinterface/ght_jump.jsp";
				}
			//国付宝网关支付
        	}else if (PayConstant.PAY_CONFIG.get("PAY_CHANNEL_GFB").equals(payChannel.bankCode)) {
        		if("0".equals(JWebConstant.SYS_CONFIG.get("DEBUG")))return "/jsp/pay/payinterface/test_jump.jsp";
        		else{
        			new GFBPayService().pay(request, prdOrder, payOrder);
        			return "/jsp/pay/payinterface/gfb_jump.jsp";
        		}
			// 邦付宝收银台网关支付
			}else if (PayConstant.PAY_CONFIG.get("PAY_CHANNEL_BFB").equals(
					payChannel.bankCode)) {
				if ("0".equals(JWebConstant.SYS_CONFIG.get("DEBUG")))
					return "/jsp/pay/payinterface/test_jump.jsp";
				else {
					new BFBPayService().pay(request, prdOrder, payOrder);
					return "/jsp/pay/payinterface/bfb_jump.jsp";
				}
			//亿联通付收银台网关支付
			}else if (PayConstant.PAY_CONFIG.get("PAY_CHANNEL_YLTF").equals(
					payChannel.bankCode)) {
				if ("0".equals(JWebConstant.SYS_CONFIG.get("DEBUG")))
					return "/jsp/pay/payinterface/test_jump.jsp";
				else {
					new YLTFPayService().pay(request, prdOrder, payOrder);
					return "/jsp/pay/payinterface/yltf_jump.jsp";
				}
			//商银信收银台网关支付
			}else if (PayConstant.PAY_CONFIG.get("PAY_CHANNEL_SYX").equals(
					payChannel.bankCode)) {
				if ("0".equals(JWebConstant.SYS_CONFIG.get("DEBUG")))
					return "/jsp/pay/payinterface/test_jump.jsp";
				else {
					new SYXPayService().pay(request, prdOrder, payOrder);
					return "/jsp/pay/payinterface/syx_jump.jsp";
				}
//				银盈通收银台网关支付
        	} else if(PayConstant.PAY_CONFIG.get("PAY_CHANNEL_YYT").equals(payChannel.bankCode)){
	    		if("0".equals(JWebConstant.SYS_CONFIG.get("DEBUG"))){
					return "/jsp/pay/payinterface/test_jump.jsp";
				}else{
					response.setCharacterEncoding("utf-8");
					response.setHeader("Content-type","text/html;charset=UTF-8");
					PrintWriter writer = response.getWriter();
					String res = new  YYTPayService().pay(request, prdOrder, payOrder);
					writer.write(res);
					writer.flush();
					writer.close();
					return null;
				}
	    	//联动收银台网关支付
			}else if(PayConstant.PAY_CONFIG.get("PAY_CHANNEL_LD").equals(payChannel.bankCode)){
				if("0".equals(JWebConstant.SYS_CONFIG.get("DEBUG")))return "/jsp/pay/payinterface/test_jump.jsp";
				else{
					new LDPayService().pay(request,prdOrder,payOrder);
					return "/jsp/pay/payinterface/ld_jump.jsp";
				}
			//易宝收银台网关支付。
        	}else if(PayConstant.PAY_CONFIG.get("PAY_CHANNEL_YB").equals(payChannel.bankCode)){
				if("0".equals(JWebConstant.SYS_CONFIG.get("DEBUG")))return "/jsp/pay/payinterface/test_jump.jsp";
				else{
					new YBPayService().pay(request,prdOrder,payOrder);
					return "/jsp/pay/payinterface/yb_jump.jsp";
				}
			//统统付收银台网关支付
        	}else if (PayConstant.PAY_CONFIG.get("PAY_CHANNEL_TTF").equals(
					payChannel.bankCode)) {
				if ("0".equals(JWebConstant.SYS_CONFIG.get("DEBUG")))
					return "/jsp/pay/payinterface/test_jump.jsp";
				else {
					new TTFPayService().pay(request, prdOrder, payOrder);
					return "/jsp/pay/payinterface/ttf_jump.jsp";
				}
			//新酷宝收银台网关支付。
        	}else if(PayConstant.PAY_CONFIG.get("PAY_CHANNEL_KBNEW").equals(payChannel.bankCode)){
				if("0".equals(JWebConstant.SYS_CONFIG.get("DEBUG")))return "/jsp/pay/payinterface/test_jump.jsp";
				else{
					new KBNEWPayService().pay(request,prdOrder,payOrder);
					return "/jsp/pay/payinterface/kb_new_jump.jsp";
				}
			//易通支付收银台网关支付。
        	}else if(PayConstant.PAY_CONFIG.get("PAY_CHANNEL_YTZF").equals(payChannel.bankCode)){
	    		if("0".equals(JWebConstant.SYS_CONFIG.get("DEBUG"))){
					return "/jsp/pay/payinterface/test_jump.jsp";
				}else{
					response.setCharacterEncoding("utf-8");
					response.setHeader("Content-type","text/html;charset=UTF-8");
					PrintWriter writer = response.getWriter();
					String res = new  YTZFPayService().pay(request, prdOrder, payOrder);
					writer.write(res);
					writer.flush();
					writer.close();
					return null;
				}
        	}else throw new Exception("无可选择的支付渠道");
		} catch (Exception e) {
			//e.printStackTrace();
			log.info(PayUtil.exceptionToString(e));
        	payRequest = new PayRequest();
        	payRequest.respCode="-1";
        	if(e.getMessage()!=null&&e.getMessage().indexOf("ORA-00001")!=-1)payRequest.respDesc = "商户订单号重复";
        	else payRequest.respDesc = e.getMessage();
            request.setAttribute("payRequest", payRequest);
            return "/jsp/pay/payinterface/info.jsp";
		}
    }
    /**
     * 确认付款页面跳转
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("confirmTheQuickH5Payment")
    public String confirmTheQuickH5Payment(HttpServletRequest request,HttpServletResponse response) {
    	PayRequest payRequest = (PayRequest)request.getSession().getAttribute("payRequest");
    	log.info(this.getClass().getSimpleName()+".confirmTheQuickH5Payment确认付款页面跳转");
		try {
			if(payRequest == null)throw new Exception("支付已过期");
			if(!"00".equals(payRequest.payOrder.ordstatus))throw new Exception("订单已经处理完成");//订单已经处理完成
			payRequest.payOrder.paytype="03";
			request.setCharacterEncoding("UTF-8");
			payRequest.checkCode = request.getParameter("checkCode");
			if(payRequest.checkCode.length()<4||payRequest.checkCode.length()>6)throw new Exception("请正确输入验证码");
			if("1".equals(JWebConstant.SYS_CONFIG.get("DEBUG"))){//生产模式
				Map<String,String> map = PayUtil.parseXmlAttributeToMap(new PayOrderInterfaceService().certPayConfirm(payRequest));
				if(!"000".equals(map.get("respCode"))){
					payRequest.payOrder.ordstatus = "-1";
					throw new Exception(map.get("respDesc"));
				} else payRequest.payOrder.ordstatus = "01";
			} else {//测试模式
				String msgCode = PayConstant.PAY_CONFIG.get("PAY_QUICK_MSG_CODE_TEST");
    			if(msgCode==null||msgCode.length()==0)msgCode="123456";
    			if(msgCode.equals(payRequest.checkCode)){
    				payRequest.payOrder.ordstatus="01";
    				payRequest.productOrder.ordstatus="01";
    				payRequest.payOrder.bankjrnno = String.valueOf(System.currentTimeMillis());
    				new NotifyInterface().notifyMer(payRequest.payOrder);
    			} else throw new Exception("短信验证码错误");
			}
			request.setAttribute("payOver","OK");
		} catch (Exception e) {
			//e.printStackTrace();
			log.info(PayUtil.exceptionToString(e));
			request.setAttribute("payOver","ERROR");
			if(e.getMessage()!=null&&e.getMessage().indexOf("ORA-00001")!=-1)request.setAttribute("info","重复提交，请重新下单！");
			else request.setAttribute("info",e.getMessage());
		}
		return "/jsp/pay/payinterface/checkoutquick_sms.jsp";
    }
    /**
     * 快捷H5-通过卡号，获取银行卡绑定信息
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("getBindCardByNo")
    public String getBindCardByNo(HttpServletRequest request,HttpServletResponse response) {
    	PayRequest payRequest = (PayRequest)request.getSession().getAttribute("payRequest");
    	OutputStream os = null;
    	log.info(this.getClass().getSimpleName()+".getBindCardByNo快捷H5-通过卡号，获取银行卡绑定信息");
        try {
        	os = response.getOutputStream();
        	if(payRequest == null){
        		os.write(("{\"respDesc\":\"交易过期\"}").getBytes("utf-8"));
        		return null;
        	}
            String cardNo = request.getParameter("cardNo");
            PayCardBin cardBin = PayCardBinService.getCardBinByCardNo(cardNo);
            if(cardBin !=null){
            	request.getSession().setAttribute("quickPayH5cardBin", cardBin);
            	PayTranUserQuickCard card = new PayTranUserQuickCardService().getBindCardByNo(cardNo);
            	if(card!=null){
            		request.getSession().setAttribute("quickPayH5Card", card);
            		os.write(("{\"cardType\":\""+cardBin.cardType+"\",\"cardInfo\":\""+cardBin.bankName+"（"+PayConstant.CARD_BIN_TYPE.get(cardBin.cardType)+"）"+"\"," +
                			"\"credentialNo\":\""+PayUtil.getStarInfo(card.credentialNo)+"\"," +
                			"\"name\":\""+PayUtil.getStarInfo(card.name)+"\"," +
                			"\"mobileNo\":\""+PayUtil.getStarInfo(card.mobileNo)+"\"," +
                			"\"cvv2\":\""+PayUtil.getStarInfo(card.cvv2)+"\"," +
                			"\"validPeriod\":\""+PayUtil.getStarInfo(card.validPeriod)+"\"}").getBytes("utf-8"));
            	} else {
            		request.getSession().removeAttribute("quickPayH5Card");
            		os.write(("{\"cardType\":\""+cardBin.cardType+"\",\"cardInfo\":\""+cardBin.bankName+"（"+PayConstant.CARD_BIN_TYPE.get(cardBin.cardType)+"）"+"\"," +
            			"\"credentialNo\":\"\",\"name\":\"\",\"mobileNo\":\"\",\"cvv2\":\"\",\"validPeriod\":\"\"}").getBytes("utf-8"));
            	}
            	//获取渠道信息
            	String tranType=PayConstant.TRAN_TYPE_CONSUME_QUICK_DEBIT;
        		if("1".equals(cardBin.cardType))tranType=PayConstant.TRAN_TYPE_CONSUME_QUICK_CREBIT;
        		PayCoopBank channel = new PayFeeRateService().getMinFeeChannel(payRequest,"quick",cardBin.bankCode,
            			tranType,cardBin.cardType,payRequest.payOrder.txamt);
            	request.getSession().setAttribute("payChannel",channel);
            } else {
            	request.getSession().removeAttribute("quickPayH5cardBin");
            	os.write(("{\"cardInfo\":\"\",\"credentialNo\":\"\",\"name\":\"\",\"mobileNo\":\"\",\"cvv2\":\"\",\"validPeriod\":\"\"}").getBytes("utf-8"));
            }
        } catch (Exception e) {
        	//e.printStackTrace();
        	log.info(PayUtil.exceptionToString(e));
        	try {os.write(("{\"cardInfo\":\"\",\"credentialNo\":\"\",\"name\":\"\",\"mobileNo\":\"\",\"cvv2\":\"\",\"validPeriod\":\"\"}").getBytes("utf-8"));} catch (Exception e2) {}
        } finally {
        	try {if(os!=null)os.close();} catch (Exception e2) {}
        }
        return null;
    }
    /**
     * 快捷H5-重新发送验证码
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("sendSmsForQuickPayH5")
    public String sendSmsForQuickPayH5(HttpServletRequest request,HttpServletResponse response) {
    	PayRequest payRequest = (PayRequest)request.getSession().getAttribute("payRequest");
    	OutputStream os = null;
        try {
        	os = response.getOutputStream();
        	if(payRequest == null){
        		os.write(("{\"respDesc\":\"交易过期\"}").getBytes("utf-8"));
        		return null;
        	}
        	if("1".equals(JWebConstant.SYS_CONFIG.get("DEBUG"))){//生产模式
        		Map<String,String> map = PayUtil.parseXmlAttributeToMap(new PayOrderInterfaceService().sendCertPayReSms(payRequest));
        		if(!"000".equals(map.get("respCode")))throw new Exception(map.get("respDesc"));
        	}
			os.write(("{\"respDesc\":\"\"}").getBytes("utf-8"));
        } catch (Exception e) {
        	//e.printStackTrace();
        	log.info(PayUtil.exceptionToString(e));
        	try {os.write(("{\"respDesc\":\""+e.getMessage()+"\"}").getBytes("utf-8"));} catch (Exception e2) {}
        } finally {
        	try {if(os!=null)os.close();} catch (Exception e2) {}
        }
        return null;
    }
    /**
     * 快捷H5-下订单,发送验证码
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("createOrderForQuickPayH5")
    public String sendSmsForQuickPayH5(HttpServletRequest request,HttpServletResponse response
    		,@ModelAttribute("PayTranUserQuickCard") PayTranUserQuickCard newCard) {
    	PayRequest payRequest = (PayRequest)request.getSession().getAttribute("payRequest");
    	PayCoopBank payChannel = (PayCoopBank)request.getSession().getAttribute("payChannel");
    	PayCardBin cardBin = (PayCardBin)request.getSession().getAttribute("quickPayH5cardBin");
    	log.info(this.getClass().getSimpleName()+".createOrderForQuickPayH5快捷H5-下订单,发送验证码");
        try {
        	if(payRequest == null)throw new Exception("交易过期");
        	if(!"00".equals(payRequest.payOrder.ordstatus))throw new Exception("订单已经处理完成");//订单已经处理完成
        	if(cardBin == null)throw new Exception("请正确输入卡号");
        	if(payChannel==null)throw new Exception("不支持的银行卡号/无支付通道");
        	PayTranUserQuickCard card = (PayTranUserQuickCard)request.getSession().getAttribute("quickPayH5Card");
        	PayTranUserQuickCardService quickCardService = new PayTranUserQuickCardService();
        	if(("0".equals(cardBin.cardType) &&"1".equals(payRequest.merchant.payWaySupported.substring(3,4)))
					||("1".equals(cardBin.cardType) && "1".equals(payRequest.merchant.payWaySupported.substring(7,8)))){//不支持快捷
        		throw new Exception("该商户不支持此类交易（借记卡快捷）");
			}
        	payRequest.cardNo=newCard.cardNo;
        	payRequest.credentialNo = newCard.credentialNo;
        	payRequest.cvv2=newCard.cvv2;
    		payRequest.validPeriod=newCard.validPeriod;
        	//曾经绑定过卡
        	if(card != null){
        		payRequest.credentialNo=card.credentialNo;
        		payRequest.userName=card.name;
        		payRequest.userMobileNo=card.mobileNo;
        		
        		//证件号有修改，检查其有效性
        		if(!newCard.credentialNo.equals(PayUtil.getStarInfo(card.credentialNo))
        			&&!(newCard.credentialNo.indexOf("*")>=0)){
        			if(Tools.isChinaIDCard(newCard.credentialNo)) payRequest.credentialNo=newCard.credentialNo;
        			else throw new Exception("证件号非法");
        		}
        		if(!newCard.name.equals(PayUtil.getStarInfo(card.name))
        			&&!(newCard.name.indexOf("*")>=0)) payRequest.userName=newCard.name;
        		if(!newCard.mobileNo.equals(PayUtil.getStarInfo(card.mobileNo))
        			&&!(newCard.mobileNo.indexOf("*")>=0))payRequest.userMobileNo=newCard.mobileNo;
        		payRequest.productOrder.credentialNo=payRequest.credentialNo;
        		payRequest.payOrder.bankpayusernm=payRequest.userName;
        		payRequest.productOrder.mobile=payRequest.userMobileNo;
        	} else {//未绑定过卡
        		payRequest.credentialNo=newCard.credentialNo;
        		payRequest.userName=newCard.name;
        		payRequest.userMobileNo=newCard.mobileNo;
        		quickCardService.addPayTranUserQuickCard(payRequest);
        	}
			payRequest.bankId = cardBin.bankCode;
    		payRequest.bankName = cardBin.bankName;
    		payRequest.payOrder.bankCardType = cardBin.cardType;
    		payRequest.payOrder.bankcod = payRequest.bankId;
    		payRequest.payOrder.payChannel = payChannel.bankCode;
    		payRequest.payOrder.payacno = payRequest.cardNo;
    		payRequest.payOrder.bankpayacno = payRequest.cardNo;
    		payRequest.payOrder.credentialNo = payRequest.credentialNo;
    		payRequest.payOrder.bankpayusernm = payRequest.userName;
    		payRequest.payOrder.mobile = payRequest.userMobileNo;
    		payRequest.productOrder.credentialNo = payRequest.credentialNo;
    		payRequest.productOrder.mobile = payRequest.userMobileNo;
    		//更新订单信息
        	new PayOrderService().updateOrderForQuickPayH5(payRequest);
    		boolean orderSuccess = false;
			//融保支付
    		if (PayConstant.PAY_CONFIG.get("PAY_CHANNEL_RB").equals(payChannel.bankCode)){
    			if("1".equals(JWebConstant.SYS_CONFIG.get("DEBUG"))){//生产模式
    				Map<String,String> map = PayUtil.parseXmlAttributeToMap(
    						new RBPayService().quickPay(request,payRequest,payRequest.productOrder,payRequest.payOrder));
    				if("000".equals(map.get("respCode")))orderSuccess=true;
    				else throw new Exception(map.get("respDesc"));
    			} else orderSuccess=true;//测试模式
    		//畅捷
    		} else if(PayConstant.PAY_CONFIG.get("PAY_CHANNEL_CJ").equals(payChannel.bankCode)){
    			if("1".equals(JWebConstant.SYS_CONFIG.get("DEBUG"))){//生产模式
    				String res = new CJPayService().quickPay(request,payRequest,payRequest.productOrder,payRequest.payOrder);
    				Map<String,String> map = PayUtil.parseXmlAttributeToMap(res);
    				if("000".equals(map.get("respCode")))orderSuccess=true;
    				else throw new Exception(map.get("respDesc"));
    			} else orderSuccess=true;//测试模式
    		//平台快捷支付。
    		} else if (PayConstant.PAY_CONFIG.get("PAY_CHANNEL_PLTPAY").equals(payChannel.bankCode)){
    			if("1".equals(JWebConstant.SYS_CONFIG.get("DEBUG"))){//生产模式
    				String res = new PLTPayService().quickPay(request,payRequest,payRequest.productOrder,payRequest.payOrder);
    				Map<String,String> map = PayUtil.parseXmlAttributeToMap(res);
    				if("000".equals(map.get("respCode")))orderSuccess=true;
    				else throw new Exception(map.get("respDesc"));
    			} else orderSuccess=true;//测试模式
    		//宝付快捷支付。
    		} else if (PayConstant.PAY_CONFIG.get("PAY_CHANNEL_BF").equals(payChannel.bankCode)){
    			if("1".equals(JWebConstant.SYS_CONFIG.get("DEBUG"))){//生产模式
    				String res = new BFPayService().quickPay(request,payRequest,payRequest.productOrder,payRequest.payOrder);
    				Map<String,String> map = PayUtil.parseXmlAttributeToMap(res);
    				if("000".equals(map.get("respCode")))orderSuccess=true;
    				else throw new Exception(map.get("respDesc"));
    			} else orderSuccess=true;//测试模式
    		//富友快捷支付
    		} else if (PayConstant.PAY_CONFIG.get("PAY_CHANNEL_FY").equals(payChannel.bankCode)){
    			if("1".equals(JWebConstant.SYS_CONFIG.get("DEBUG"))){//生产模式
    				String res = new FYPayService().quickPay(request,payRequest,payRequest.productOrder,payRequest.payOrder);
    				Map<String,String> map = PayUtil.parseXmlAttributeToMap(res);
    				if("000".equals(map.get("respCode")))orderSuccess=true;
    				else throw new Exception(map.get("respDesc"));
    			} else orderSuccess=true;//测试模式
    		//京东快捷支付
    		} else if (PayConstant.PAY_CONFIG.get("PAY_CHANNEL_JD").equals(payChannel.bankCode)){
    			if("1".equals(JWebConstant.SYS_CONFIG.get("DEBUG"))){//生产模式
    				String res = new JDPayService().quickPay(request,payRequest,payRequest.productOrder,payRequest.payOrder);
    				Map<String,String> map = PayUtil.parseXmlAttributeToMap(res);
    				if("000".equals(map.get("respCode")))orderSuccess=true;
    				else throw new Exception(map.get("respDesc"));
    			} else orderSuccess=true;//测试模式
    		//高汇通快捷支付H5
    		} else if(PayConstant.PAY_CONFIG.get("PAY_CHANNEL_GHT").equals(payChannel.bankCode)){
    			if("1".equals(JWebConstant.SYS_CONFIG.get("DEBUG"))){//生产模式
    				String res = new GHTPayService().quickPay(request,payRequest,payRequest.productOrder,payRequest.payOrder);
    				Map<String,String> map = PayUtil.parseXmlAttributeToMap(res);
    				if("000".equals(map.get("respCode")))orderSuccess=true;
    				else throw new Exception(map.get("respDesc"));
    			} else orderSuccess=true;//测试模式
    		//金运通快捷支付H5
    		} else if (PayConstant.PAY_CONFIG.get("PAY_CHANNEL_JYT").equals(payChannel.bankCode)){
    			if("1".equals(JWebConstant.SYS_CONFIG.get("DEBUG"))){//生产模式
    				String res = new JYTPayService().quickPay(request,payRequest,payRequest.productOrder,payRequest.payOrder);
    				Map<String,String> map = PayUtil.parseXmlAttributeToMap(res);
    				if("000".equals(map.get("respCode")))orderSuccess=true;
    				else throw new Exception(map.get("respDesc"));
    			} else orderSuccess=true;//测试模式
    		//创新快捷支付H5
    		} else if (PayConstant.PAY_CONFIG.get("PAY_CHANNEL_CX").equals(payChannel.bankCode)){
    			if("1".equals(JWebConstant.SYS_CONFIG.get("DEBUG"))){//生产模式
    				String res = new CXPayService().quickPay(request,payRequest,payRequest.productOrder,payRequest.payOrder);
    				Map<String,String> map = PayUtil.parseXmlAttributeToMap(res);
    				if("000".equals(map.get("respCode")))orderSuccess=true;
    				else throw new Exception(map.get("respDesc"));
    			} else orderSuccess=true;//测试模式else throw new Exception("不支持的银行卡号/无支付通道");
    		//邦付宝快捷H5
    		} else if (PayConstant.PAY_CONFIG.get("PAY_CHANNEL_BFB").equals(payChannel.bankCode)){
    			if("1".equals(JWebConstant.SYS_CONFIG.get("DEBUG"))){//生产模式
    				String res = new BFBPayService().quickPay(request,payRequest,payRequest.productOrder,payRequest.payOrder);
    				Map<String,String> map = PayUtil.parseXmlAttributeToMap(res);
    				if("000".equals(map.get("respCode")))orderSuccess=true;
    				else throw new Exception(map.get("respDesc"));
    			} else orderSuccess=true;//测试模式else throw new Exception("不支持的银行卡号/无支付通道");
    		//先锋快捷H5
    		} else if(PayConstant.PAY_CONFIG.get("PAY_CHANNEL_XF").equals(payChannel.bankCode)){
    			if("1".equals(JWebConstant.SYS_CONFIG.get("DEBUG"))){//生产模式
    				String res = new XFPayService().quickPay(request,payRequest,payRequest.productOrder,payRequest.payOrder);
    				Map<String,String> map = PayUtil.parseXmlAttributeToMap(res);
    				if("000".equals(map.get("respCode")))orderSuccess=true;
    				else throw new Exception(map.get("respDesc"));
    			} else orderSuccess=true;//测试模式
    		//联动快捷支付H5
    		}else if(PayConstant.PAY_CONFIG.get("PAY_CHANNEL_LD").equals(payChannel.bankCode)){
    			if("1".equals(JWebConstant.SYS_CONFIG.get("DEBUG"))){//生产模式
    				String res = new LDPayService().quickPay(request,payRequest,payRequest.productOrder,payRequest.payOrder);
    				Map<String,String> map = PayUtil.parseXmlAttributeToMap(res);
    				if("000".equals(map.get("respCode")))orderSuccess=true;
    				else throw new Exception(map.get("respDesc"));
    			} else orderSuccess=true;//测试模式
    		//长盈快捷支付H5
    		}else if(PayConstant.PAY_CONFIG.get("PAY_CHANNEL_CY").equals(payChannel.bankCode)){
    			if("1".equals(JWebConstant.SYS_CONFIG.get("DEBUG"))){//生产模式
    				String res = new CYPayService().quickPay(request, payRequest, payRequest.productOrder,payRequest.payOrder);
    				Map<String,String> map = PayUtil.parseXmlAttributeToMap(res);
    				if("000".equals(map.get("respCode")))orderSuccess=true;
    				else throw new Exception(map.get("respDesc"));
    			} else orderSuccess=true;//测试模式
    		}else throw new Exception("不支持的银行卡号/无支付通道");
    		//更新绑定卡信息 如果原卡成功绑定过，就不更新，如果本次下单成功就更新
    		if(orderSuccess&&card!=null)quickCardService.updatePayTranUserQuickCard(payRequest);
    		return "/jsp/pay/payinterface/checkoutquick_sms.jsp";
        } catch (Exception e) {
        	//e.printStackTrace();
        	log.info(PayUtil.exceptionToString(e));
        	request.setAttribute("info",e.getMessage());
        	return "/jsp/pay/payinterface/checkoutquick.jsp";
        }
    }
}