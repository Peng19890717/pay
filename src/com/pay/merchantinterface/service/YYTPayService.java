package com.pay.merchantinterface.service;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.PayConstant;
import com.pay.order.dao.PayOrder;
import com.pay.order.dao.PayProductOrder;
import com.pay.order.service.PayOrderService;
import com.yst.m2.sdk.M2;
import com.yst.m2.sdk.ReturnObj;

public class YYTPayService {
	
	private static final Log log = LogFactory.getLog(YYTPayService.class);
	public static Map<String, String> YYT_B2C_BANK_CODE = new HashMap<String, String>();
	static {
		YYT_B2C_BANK_CODE.put("ICBC","102100099996");//工商
		YYT_B2C_BANK_CODE.put("ABC","103100000026");//农业
		YYT_B2C_BANK_CODE.put("BOC","104100000004");//中国
		YYT_B2C_BANK_CODE.put("CCB","105100000017");//建设
		YYT_B2C_BANK_CODE.put("BOCOM","301290000007");//交通
		YYT_B2C_BANK_CODE.put("CMB","308584000013");//招商
		YYT_B2C_BANK_CODE.put("CNCB","302100011000");//中信
		YYT_B2C_BANK_CODE.put("CEB","303100000006");//光大银行。
//		YYT_B2C_BANK_CODE.put("PSBC","403100000004");//邮政
		YYT_B2C_BANK_CODE.put("CMBC","305100000013");//民生
		YYT_B2C_BANK_CODE.put("GDB","306581000003");//广发
//		YYT_B2C_BANK_CODE.put("CIB","309391000011");//兴业
//		YYT_B2C_BANK_CODE.put("SPDB","310290000013");//浦发
		YYT_B2C_BANK_CODE.put("BCCB","313100000013");//北京
//		YYT_B2C_BANK_CODE.put("SHRCB","322290000011");//上海农商
		YYT_B2C_BANK_CODE.put("HXB","304100040000");//华夏
		
	}

	/**
	 * 收银台下单
	 * @param request
	 * @param prdOrder
	 * @param payOrder
	 * @throws Exception
	 */
	public String pay(HttpServletRequest request,PayProductOrder prdOrder,PayOrder payOrder){
		try{
			payOrder.payChannel = PayConstant.PAY_CONFIG.get("PAY_CHANNEL_YYT"); // 支付渠道
			payOrder.bankcod = request.getParameter("banks"); // 银行代码
			Map<String, String> T604_Map = new HashMap<String, String>();
			String ProductName=PayConstant.PAY_CONFIG.get("COMMON_PRODUCT_DESC")==null||PayConstant.PAY_CONFIG.get("COMMON_PRODUCT_DESC").length()==0?
					"_":PayConstant.PAY_CONFIG.get("COMMON_PRODUCT_DESC");//商品名称
			//请求协议
			T604_Map.put("login_token", "");
			T604_Map.put("transcode", "T604");//交易码
		    T604_Map.put("version", "0140");//版本号
		    T604_Map.put("ordersn", payOrder.payordno);//流水号
			T604_Map.put("platform", "03");//平台
			T604_Map.put("ownerid",  "EBC");//运营商
			T604_Map.put("merchno", PayConstant.PAY_CONFIG.get("YYT_MERNO"));//商户号
		    T604_Map.put("dsorderid", payOrder.payordno);//订单号
		    T604_Map.put("product",ProductName);//商品名称
		    T604_Map.put("userno",PayConstant.PAY_CONFIG.get("YYT_USERNO"));//用户id
		    T604_Map.put("mediumno", PayConstant.PAY_CONFIG.get("YYT_MEDIUMNO"));//钱包ID
		    T604_Map.put("cardno",PayConstant.PAY_CONFIG.get("YYT_CARDNO"));//钱包账号
		    T604_Map.put("currency","CNY");//支付订单币种
		    T604_Map.put("transcurrency","CNY");//扣款币种
		    T604_Map.put("amount",String.format("%.2f", Double.parseDouble(String.valueOf(payOrder.txamt))/100d));//金额
		    T604_Map.put("ebcbankid",YYT_B2C_BANK_CODE.get(payOrder.bankcod));//发卡行ID
			T604_Map.put("cardtype", "01");//卡类型
			T604_Map.put("usertype", "0");//用户类型
			T604_Map.put("banktburl",payOrder.returl==null?PayConstant.PAY_CONFIG.get("YYT_DSYBURL"):payOrder.returl);//银行同步通知地址
			T604_Map.put("dsyburl",PayConstant.PAY_CONFIG.get("YYT_DSYBURL"));//异步通知地址
		    String setPayOrderInfo_json = com.yst.m2.sdk.util.JsonUtil.to_json(T604_Map);
		    log.info("银盈通直连网银请求数据:"+setPayOrderInfo_json);
	        //向接口发送数据
		    ReturnObj ret_obj = M2.send("bonuspay_general@T604", setPayOrderInfo_json);
		    //接口请求完成后，设置返回数据    
		    String ret_data = ret_obj.get_data();//设置服务返回的数据
		    if (!ret_obj.is_ok()) {
		        //如果服务处理失败，则返回m2的异常信息数据
		        ret_data = ret_obj.to_json();
		    }
			new PayOrderService().updateOrderForBanks(payOrder);
			return ret_data;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	/**
	 * 商户接口下单
	 * @param request
	 * @param payRequest
	 * @param prdOrder
	 * @param payOrder
	 * @throws Exception
	 */
	public String pay(HttpServletRequest request,PayRequest payRequest,PayProductOrder prdOrder,PayOrder payOrder) throws Exception{
		try{
			payOrder.payChannel = PayConstant.PAY_CONFIG.get("PAY_CHANNEL_YYT"); // 支付渠道
			payOrder.bankcod = payRequest.bankId; // 银行代码
			payOrder.bankCardType = payRequest.accountType;
			Map<String, String> T604_Map = new HashMap<String, String>();
			String ProductName=PayConstant.PAY_CONFIG.get("COMMON_PRODUCT_DESC")==null||PayConstant.PAY_CONFIG.get("COMMON_PRODUCT_DESC").length()==0?
					"_":PayConstant.PAY_CONFIG.get("COMMON_PRODUCT_DESC");//商品名称
			//请求协议
			T604_Map.put("login_token", "");
			T604_Map.put("transcode", "T604");//交易码
		    T604_Map.put("version", "0140");//版本号
		    T604_Map.put("ordersn", payOrder.payordno);//流水号
			T604_Map.put("platform", "03");//平台
			T604_Map.put("ownerid",  "EBC");//运营商
			T604_Map.put("merchno", PayConstant.PAY_CONFIG.get("YYT_MERNO"));//商户号
		    T604_Map.put("dsorderid", payOrder.payordno);//订单号
		    T604_Map.put("product",ProductName);//商品名称
		    T604_Map.put("userno",PayConstant.PAY_CONFIG.get("YYT_USERNO"));//用户id
		    T604_Map.put("mediumno", PayConstant.PAY_CONFIG.get("YYT_MEDIUMNO"));//钱包ID
		    T604_Map.put("cardno",PayConstant.PAY_CONFIG.get("YYT_CARDNO"));//钱包账号
		    T604_Map.put("currency","CNY");//支付订单币种
		    T604_Map.put("transcurrency","CNY");//扣款币种
		    T604_Map.put("amount",String.format("%.2f", Double.parseDouble(String.valueOf(payOrder.txamt))/100d));//金额
		    T604_Map.put("ebcbankid",YYT_B2C_BANK_CODE.get(payOrder.bankcod));//发卡行ID
			T604_Map.put("cardtype", "01");//卡类型
			T604_Map.put("usertype", "0");//用户类型
			T604_Map.put("banktburl",payOrder.returl==null?PayConstant.PAY_CONFIG.get("YYT_DSYBURL"):payOrder.returl);//银行同步通知地址
			T604_Map.put("dsyburl",PayConstant.PAY_CONFIG.get("YYT_DSYBURL"));//异步通知地址
			//将map转成json字符串			
		    String setPayOrderInfo_json = com.yst.m2.sdk.util.JsonUtil.to_json(T604_Map);
		    log.info("银盈通直连网银请求数据:"+setPayOrderInfo_json);
	        //向接口发送数据
		    ReturnObj ret_obj = M2.send("bonuspay_general@T604", setPayOrderInfo_json);
		    //接口请求完成后，设置返回数据    
		    String ret_data = ret_obj.get_data();//设置服务返回的数据
		    if (!ret_obj.is_ok()) {
		        //如果服务处理失败，则返回m2的异常信息数据
		        ret_data = ret_obj.to_json();
		    }
			new PayOrderService().updateOrderForBanks(payOrder);
			return ret_data;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 渠道补单---网关
	 * @param payordno 订单号
	 * @throws Exception 异常捕获
	 */
	public void channelQuery(PayOrder payOrder) throws Exception{
		try{
			Map<String, String> T613_Map = new HashMap<String,String>();
			T613_Map.put("transcode", "T613");//交易码
		    T613_Map.put("version", "0140");//版本号
		    T613_Map.put("ordersn", payOrder.payordno);//流水号
		    T613_Map.put("merchno", PayConstant.PAY_CONFIG.get("YYT_MERNO"));//商户号
			T613_Map.put("platform", "03");//平台
			T613_Map.put("ownerid",  "EBC");//运营商
		    T613_Map.put("dsorderid", payOrder.payordno);//商户订单号
		    T613_Map.put("flag", "1");//对账类型
			//将map转成json字符串
		    String setPayOrderInfo_json = com.yst.m2.sdk.util.JsonUtil.to_json(T613_Map);
		    log.info("银盈通网关查询请求参数:"+setPayOrderInfo_json);
	        //向接口发送数据
		    ReturnObj ret_obj = M2.send("bonuspay_general@T613", setPayOrderInfo_json);
		    //接口请求完成后，设置返回数据
		    String ret_data = ret_obj.get_data();
		    /**
		    *{"sign":"1C016339365FDA55099636E1D6FB9412F3BD9619454E155B",
		    *"returncode":"34","errtext":"处理中,请等待[bank returncode 02] : 交易异常银联没有返回消息","transcode":"T613","ordersn":"1494309479717",
		    *"merchno":"611300000000007","ownerid":"EBC","orderid":"201705091151288315000186","op_ret_code":"34"}
		     */
		    if (!ret_obj.is_ok()) {
		        //如果服务处理失败，则返回m2的异常信息数据
		        ret_data = ret_obj.to_json();
		        new Exception(ret_data);
		    }
		    log.info("银盈通网关查询响应参数:"+ret_data);
		    JSONObject jsonObject = JSONObject.fromObject(ret_data);
//		    		JSON.parseObject(ret_data); 
	        if("00".equals(jsonObject.getString("returncode"))){
	        	payOrder.ordstatus="01";//支付成功
	        	new NotifyInterface().notifyMer(payOrder);//支付成功
	        }else if("34".equals(jsonObject.getString("returncode"))||"62".equals(jsonObject.getString("returncode"))||"99".equals(jsonObject.getString("returncode"))){
	        	//处理中不处理。
	        }else{
	        	payOrder.ordstatus="02";//支付失败。
	        	new NotifyInterface().notifyMer(payOrder);//支付成功
	        }
		} catch (Exception e){
			e.printStackTrace();
			throw e;
		}
	}
}
