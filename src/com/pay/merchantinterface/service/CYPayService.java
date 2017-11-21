package com.pay.merchantinterface.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import util.MD5;

import com.PayConstant;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.pay.order.dao.PayOrder;
import com.pay.order.dao.PayOrderDAO;
import com.pay.order.dao.PayProductOrder;
import com.pay.order.service.PayOrderService;
import com.third.cy.BalanceQueryReqModel;
import com.third.cy.KuaiJieReqModel;
import com.third.cy.WangGuanPayReqModel;

/**
 * 长盈
 * @author lqq
 *
 */
public class CYPayService {

	private static final Log log = LogFactory.getLog(CYPayService.class);
	
	public static Map <String,String> PAY_BANK_MAP_NAME = new HashMap <String,String>();//根据银行代码，获取银行名称
	public static Map <String,String> PAY_BANK_MAP_NAME_KJ = new HashMap <String,String>();//根据银行代码，获取银行名称
	static{
		//拼银行名称(网银)
		PAY_BANK_MAP_NAME.put("BRCB", "北京农村商业银行");
		PAY_BANK_MAP_NAME.put("ABC", "中国农业银行");
		PAY_BANK_MAP_NAME.put("HXB", "华夏银行");
		PAY_BANK_MAP_NAME.put("BOCOM", "交通银行");
		PAY_BANK_MAP_NAME.put("GDB", "广发银行");
		PAY_BANK_MAP_NAME.put("PSBC", "中国邮政储蓄银行");
		PAY_BANK_MAP_NAME.put("BOC", "中国银行");
		PAY_BANK_MAP_NAME.put("CIB", "兴业银行");
		PAY_BANK_MAP_NAME.put("CNCB", "中信银行");
		PAY_BANK_MAP_NAME.put("CMB", "招商银行");
		PAY_BANK_MAP_NAME.put("CEB", "中国光大银行");
		PAY_BANK_MAP_NAME.put("CCB", "中国建设银行");
		PAY_BANK_MAP_NAME.put("PAB", "平安银行");
		PAY_BANK_MAP_NAME.put("SPDB", "上海浦东发展银行");
		PAY_BANK_MAP_NAME.put("BCCB", "北京银行");
		PAY_BANK_MAP_NAME.put("CMBC", "中国民生银行");
		PAY_BANK_MAP_NAME.put("BOS", "上海银行");
		PAY_BANK_MAP_NAME.put("ICBC", "中国工商银行");
		
		//拼银行名称（快捷）
		PAY_BANK_MAP_NAME_KJ.put("CIB", "兴业银行");
		PAY_BANK_MAP_NAME_KJ.put("PAB", "平安银行");
		PAY_BANK_MAP_NAME_KJ.put("HKBCHINA", "汉口银行");
		PAY_BANK_MAP_NAME_KJ.put("BOC", "中国银行");
		PAY_BANK_MAP_NAME_KJ.put("NJBC", "南京银行");
		PAY_BANK_MAP_NAME_KJ.put("CEB", "中国光大银行");
		PAY_BANK_MAP_NAME_KJ.put("BOS", "上海银行");
		PAY_BANK_MAP_NAME_KJ.put("CMB", "招商银行");
		PAY_BANK_MAP_NAME_KJ.put("SPDB", "上海浦东发展银行");
		PAY_BANK_MAP_NAME_KJ.put("CCB", "中国建设银行");
		PAY_BANK_MAP_NAME_KJ.put("HSB", "徽商银行");
		PAY_BANK_MAP_NAME_KJ.put("BCCB", "北京银行");
		PAY_BANK_MAP_NAME_KJ.put("ZSBC", "浙商银行");
		PAY_BANK_MAP_NAME_KJ.put("NBBC", "宁波银行");
		PAY_BANK_MAP_NAME_KJ.put("GDB", "广发银行");
		PAY_BANK_MAP_NAME_KJ.put("ABC", "中国农业银行");
		PAY_BANK_MAP_NAME_KJ.put("EGBANK", "恒丰银行");
		PAY_BANK_MAP_NAME_KJ.put("HXB", "华夏银行");
		PAY_BANK_MAP_NAME_KJ.put("BOHC", "渤海银行");
		PAY_BANK_MAP_NAME_KJ.put("CMBC", "中国民生银行");
		PAY_BANK_MAP_NAME_KJ.put("SHRCB", "上海农商银行");
		PAY_BANK_MAP_NAME_KJ.put("CZCB", "浙江稠州商业银行");
		PAY_BANK_MAP_NAME_KJ.put("HNNXS", "湖南省农村信用社联合社");
		PAY_BANK_MAP_NAME_KJ.put("CNCB", "中信银行");
		PAY_BANK_MAP_NAME_KJ.put("PSBC", "中国邮政储蓄银行");
		PAY_BANK_MAP_NAME_KJ.put("ICBC", "中国工商银行");
	}
	
	/**
	 * 备注（暂时不可用，无接口方式，只有跳转长盈收银台）
	 * 收银台下单
	 * @param request
	 * @param prdOrder
	 * @param payOrder
	 * @throws Exception
	 */
	public String pay(HttpServletRequest request, PayProductOrder prdOrder,PayOrder payOrder) throws Exception{
		payOrder.payChannel = PayConstant.PAY_CONFIG.get("PAY_CHANNEL_CY"); // 支付渠道
		payOrder.bankcod = request.getParameter("banks"); // 银行代码
		payOrder.bankCardType = String.valueOf(Long.parseLong(request.getParameter("bankCardType")));//银行卡类型
		String result = "";
		try {
			String bankName = null;
			if (PayConstant.CARD_BIN_TYPE_JIEJI.equals(payOrder.bankCardType)) {
				bankName = PAY_BANK_MAP_NAME.get(payOrder.bankcod);//获取银行代码
			}
			if (bankName == null) {
				throw new Exception("长盈无对应银行（"+payOrder.bankcod+"）");//不支持此银行
			}
			//拼接请求参数
			String src_code = PayConstant.PAY_CONFIG.get("CY_SRC_CODE");//商户唯一标识
			String out_trade_no = payOrder.payordno;//订单号
			String total_fee = String.valueOf(payOrder.txamt);//交易金额
			String time_start = new SimpleDateFormat("yyyyMMddHHmmss").format(payOrder.createtime);//发起交易的时间
			String goods_name = PayConstant.PAY_CONFIG.get("COMMON_PRODUCT_DESC")==null||PayConstant.PAY_CONFIG.get("COMMON_PRODUCT_DESC").length()==0?
					"_":PayConstant.PAY_CONFIG.get("COMMON_PRODUCT_DESC");//商品名称
			String finish_url = "#";//支付完成页面的url
			String mchid = PayConstant.PAY_CONFIG.get("CY_MERNO");//平台商户号
			String cardType = "借记卡";
			String key = PayConstant.PAY_CONFIG.get("CY_MER_PRIVATEKEY");//key
			WangGuanPayReqModel reqModel = new WangGuanPayReqModel(src_code, key, mchid, total_fee, goods_name, out_trade_no, time_start, finish_url, bankName, cardType);
			Map<String, String> reqMap = reqModel.toReqMap();
			log.info("长盈网银下单请求数据："+reqMap.toString());
			String html = this.reqApi(PayConstant.PAY_CONFIG.get("CY_GATEWAYURL"), reqMap);
			JSONObject retjson = JSON.parseObject(html);
			if("0000".equals(retjson.getString("respcd"))){
				JSONObject data = retjson.getJSONObject("data");
				String sign = data.getString("sign");
				//验签
				Map<String,Object> paramMap = (Map<String,Object>)data;
				List<String> params = new ArrayList<String>();
		        List<String> sortedkeys = new ArrayList<String>(paramMap.keySet());
		        Collections.sort(sortedkeys);
		        for (String rk : sortedkeys) {
		        	String value = paramMap.get(rk).toString();
		        	if("".equals(value) || "sign".equals(rk)){
		        		continue;
		        	}else{
		        		params.add(rk + "=" + value);
		        	}
		        }
		        String presign = StringUtils.join(params, "&") + "&key=" + key;
		        String tmpSign = StringUtils.upperCase(MD5.getDigest(presign));
		        if(tmpSign.equals(sign)){
		        	result = data.getString("pay_params");
		        }
			}
			new PayOrderService().updateOrderForBanks(payOrder);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return result;
	}
	/**
	 * 备注（暂时不可用，无接口方式，只有跳转长盈收银台）
	 * 网银 商户接口下单
	 * @param request
	 * @param payRequest
	 * @param prdOrder
	 * @param payOrder
	 * @throws Exception
	 */
	public void pay(HttpServletRequest request,PayRequest payRequest,PayProductOrder prdOrder,PayOrder payOrder) throws Exception{
		payOrder.payChannel = PayConstant.PAY_CONFIG.get("PAY_CHANNEL_CY"); // 支付渠道
		payOrder.bankcod = payRequest.bankId; // 银行代码
		payOrder.bankCardType = payRequest.accountType;//卡类型
		try {
			String bankName = null;
			if (PayConstant.CARD_BIN_TYPE_JIEJI.equals(payOrder.bankCardType)) {
				bankName = PAY_BANK_MAP_NAME.get(payOrder.bankcod);//获取银行代码
			}
			if (bankName == null) {
				throw new Exception("长盈无对应银行（"+payOrder.bankcod+"）");//不支持此银行
			}
			//拼接请求参数
			String src_code = PayConstant.PAY_CONFIG.get("CY_SRC_CODE");//商户唯一标识
			String out_trade_no = payOrder.payordno;//订单号
			String total_fee = String.valueOf(payOrder.txamt);//交易金额
			String time_start = new SimpleDateFormat("yyyyMMddHHmmss").format(payOrder.createtime);//发起交易的时间
			String goods_name = PayConstant.PAY_CONFIG.get("COMMON_PRODUCT_DESC")==null||PayConstant.PAY_CONFIG.get("COMMON_PRODUCT_DESC").length()==0?
					"_":PayConstant.PAY_CONFIG.get("COMMON_PRODUCT_DESC");//商品名称
			String finish_url = "#";//支付完成页面的url
			String mchid = PayConstant.PAY_CONFIG.get("CY_MERNO");//平台商户号
			String cardType = "借记卡";
			String key = PayConstant.PAY_CONFIG.get("CY_MER_PRIVATEKEY");//key
			WangGuanPayReqModel reqModel = new WangGuanPayReqModel(src_code, key, mchid, total_fee, goods_name, out_trade_no, time_start, finish_url, bankName, cardType);
			Map<String, String> paramsMap = reqModel.toReqMap();
			request.setAttribute("paramsMap", paramsMap);
			log.info("长盈网银下单请求数据："+paramsMap.toString());
			new PayOrderService().updateOrderForBanks(payOrder);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 渠道补单（查单）
	 */
	public void channelQuery(PayOrder payOrder) throws Exception{
		try {
			//请求参数
			String src_code = PayConstant.PAY_CONFIG.get("CY_SRC_CODE");//商户唯一标识
			String out_trade_no = payOrder.payordno;//商户订单号
			String start_time = new SimpleDateFormat("yyyyMMdd").format(payOrder.createtime)+"000000";//交易开始时间
			String end_time = new SimpleDateFormat("yyyyMMdd").format(payOrder.createtime)+"235959";//交易结束时间
			String mchid = PayConstant.PAY_CONFIG.get("CY_MERNO");//商户号
			String key = PayConstant.PAY_CONFIG.get("CY_MER_PRIVATEKEY");//key
			BalanceQueryReqModel reqModel = new BalanceQueryReqModel(src_code, key, out_trade_no, start_time, end_time, mchid);
			
	        String url = PayConstant.PAY_CONFIG.get("CY_QUERY_URL");//查询订单URL
	        Map<String, String> reqMap = reqModel.toReqMap();
	        log.info("长盈查单请求："+JSON.toJSONString(reqMap));
	        String html = this.reqApi(url, reqMap);
			log.info("长盈查单响应："+html);
			if("".equals(html) || html == null){
				throw new Exception("长盈查单返回空！");
			}
			JSONObject json = JSONObject.parseObject(html);
			String respcd = json.getString("respcd");
			if("0000".equals(respcd)){
				JSONObject data_ = (JSONObject) json.getJSONArray("data").get(0);
				String order_status = data_.getString("order_status");
				String sign = data_.getString("sign");
				//验签
				Map<String,Object> paramMap = (Map<String,Object>)data_;
				List<String> params = new ArrayList<String>();
		        List<String> sortedkeys = new ArrayList<String>(paramMap.keySet());
		        Collections.sort(sortedkeys);
		        for (String rk : sortedkeys) {
		        	String value = paramMap.get(rk).toString();
		        	if("".equals(value) || "sign".equals(rk)){
		        		continue;
		        	}else{
		        		params.add(rk + "=" + value);
		        	}
		        }
		        String presign = StringUtils.join(params, "&") + "&key=" + key;
		        String tmpSign = StringUtils.upperCase(MD5.getDigest(presign));
				if(tmpSign.equals(sign) && "3".equals(order_status)){
					payOrder.ordstatus="01";//支付成功
					new NotifyInterface().notifyMer(payOrder);//支付成功
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 快捷支付(下单)
	 * @param request
	 * @param payRequest
	 * @param prdOrder
	 * @param payOrder
	 * @return
	 * @throws Exception
	 */
	public String quickPay(HttpServletRequest request,PayRequest payRequest,PayProductOrder prdOrder,PayOrder payOrder) throws Exception{
		try {
			//拼接参数
			String src_code = PayConstant.PAY_CONFIG.get("CY_SRC_CODE");//商户唯一标识
			String mch_id = PayConstant.PAY_CONFIG.get("CY_MERNO");//商户号
			String total_fee = String.valueOf(payRequest.merchantOrderAmt);//金额，单位分
			String bankName = null;
			if (PayConstant.CARD_BIN_TYPE_JIEJI.equals(payOrder.bankCardType)) {
				bankName = PAY_BANK_MAP_NAME_KJ.get(payOrder.bankcod);//获取银行代码
			}
			if (bankName == null) {
				throw new Exception("长盈无对应银行（"+payOrder.bankcod+"）");//不支持此银行
			}
			String cardType = "借记卡";
			String accoutNo = payRequest.cardNo;//银行卡号
			String accountName = payRequest.userName;//开户人姓名
			String idType = "身份证";
			String idNumber = payRequest.credentialNo;//身份证号
			String Mobile = payRequest.userMobileNo;//预留手机号
			String finish_url = "#";//完成后跳转的URL
			String key = PayConstant.PAY_CONFIG.get("CY_MER_PRIVATEKEY");//key
			String out_trade_no = payOrder.payordno;//订单号
			String goods_name = PayConstant.PAY_CONFIG.get("COMMON_PRODUCT_DESC")==null||PayConstant.PAY_CONFIG.get("COMMON_PRODUCT_DESC").length()==0?
					"_":PayConstant.PAY_CONFIG.get("COMMON_PRODUCT_DESC");//商品名称
			String time_start = new SimpleDateFormat("yyyyMMddHHmmss").format(payOrder.createtime);//发起交易的时间
			KuaiJieReqModel kuaiJieReqModel = new KuaiJieReqModel(src_code, key, mch_id, total_fee, bankName, cardType, accoutNo, accountName, idType, idNumber, Mobile, goods_name, out_trade_no, time_start,
	                finish_url);
	        String url = PayConstant.PAY_CONFIG.get("CY_KJ_URL");//快捷URL
	        Map<String, String> reqMap = kuaiJieReqModel.toFastSignReqMap();
	        log.info("长盈快捷下单请求："+JSON.toJSONString(reqMap));
	        String sign = this.reqApi(url, reqMap);
			log.info("长盈快捷下单响应："+sign);
			JSONObject retjson = null;
			try{
				retjson = JSON.parseObject(sign);
			} catch (Exception e) {
				log.error("长盈返回解析json失败！",e);
			}
			if(retjson != null){
				String respcd = retjson.getString("respcd");
				if (respcd.equals("0000")) {
					//0000签约成功
					String data = retjson.getString("data");
					if(data != null && !data.equals("")){
						PayOrderDAO payOrderDAO = new PayOrderDAO();
						payOrder.bankjrnno = data;
						payOrderDAO.updateOrderBankjrnno(payOrder);
					}
					return "<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"no\"?>" +
							"<message merchantId=\""+payRequest.merchantId+"\" " +
							"merchantOrderId=\""+payRequest.merchantOrderId+"\"  bindId=\"\" " +
							"respCode=\"000\""+" respDesc=\"\" " +
							"bankId=\""+payRequest.bankId+"\" bankName=\""+payRequest.bankName+"\"/>";
				} else {
					//下单失败
					return "<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"no\"?>" +
							"<message merchantId=\""+payRequest.merchantId+"\" " +
							"merchantOrderId=\""+payRequest.merchantOrderId+"\" bindId=\"\" " +
							"respCode=\"-1\" respDesc=\""+retjson.getString("respmsg")+"\" " +
							"bankId=\""+payRequest.bankId+"\" bankName=\""+payRequest.bankName+"\"/>";
				}
			} else {
				return "<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"no\"?>" +
						"<message merchantId=\""+payRequest.merchantId+"\" " +
						"merchantOrderId=\""+payRequest.merchantOrderId+"\" bindId=\"\" " +
						"respCode=\"-1\" respDesc=\""+"下单失败！"+"\" " +
						"bankId=\""+payRequest.bankId+"\" bankName=\""+payRequest.bankName+"\"/>";
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	/**
	 * 快捷确认
	 * @param payRequest
	 * @return
	 * @throws Exception 
	 */
	public JSONObject certPayConfirm(PayRequest payRequest) throws Exception {
		try {
			//拼接参数
			String src_code = PayConstant.PAY_CONFIG.get("CY_SRC_CODE");//商户唯一标识
			String mch_id = PayConstant.PAY_CONFIG.get("CY_MERNO");//商户号
			String total_fee = String.valueOf(payRequest.payOrder.txamt);//金额，单位分
			String bankName = null;
			if (PayConstant.CARD_BIN_TYPE_JIEJI.equals(payRequest.payOrder.bankCardType)) {
				bankName = PAY_BANK_MAP_NAME_KJ.get(payRequest.payOrder.bankcod);//获取银行代码
			}
			if (bankName == null) {
				throw new Exception("长盈无对应银行（"+payRequest.payOrder.bankcod+"）");//不支持此银行
			}
			String cardType = "借记卡";
			String accoutNo = payRequest.payOrder.bankpayacno;//银行卡号
			String accountName = payRequest.payOrder.bankpayusernm;//开户人姓名
			String idType = "身份证";
			String idNumber = payRequest.productOrder.credentialNo;//身份证号
			String Mobile = payRequest.productOrder.mobile;//预留手机号
			String finish_url = "#";//完成后跳转的URL
			String key = PayConstant.PAY_CONFIG.get("CY_MER_PRIVATEKEY");//key
			String out_trade_no = payRequest.payOrder.payordno;//订单号
			String goods_name = PayConstant.PAY_CONFIG.get("COMMON_PRODUCT_DESC")==null||PayConstant.PAY_CONFIG.get("COMMON_PRODUCT_DESC").length()==0?
					"_":PayConstant.PAY_CONFIG.get("COMMON_PRODUCT_DESC");//商品名称
			String time_start = new SimpleDateFormat("yyyyMMddHHmmss").format(payRequest.payOrder.createtime);//发起交易的时间
			KuaiJieReqModel kuaiJieReqModel = new KuaiJieReqModel(src_code, key, mch_id, total_fee, bankName, cardType, accoutNo, accountName, idType, idNumber, Mobile, goods_name, out_trade_no, time_start,
	                finish_url);
			kuaiJieReqModel.setCode(payRequest.checkCode);//验证码
			kuaiJieReqModel.setSignSn(payRequest.payOrder.bankjrnno);//交易流水号
			String url = PayConstant.PAY_CONFIG.get("CY_GATEWAYURL");//快捷请求URL
			Map<String, String> reqMap = kuaiJieReqModel.toPayReqMap();
			log.info("长盈快捷确认请求数据："+JSON.toJSONString(reqMap));
			String html = this.reqApi(url, reqMap);
			JSONObject json = JSON.parseObject(html);
			log.info("长盈快捷确认响应数据："+json);
			return json;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
    
    /**
     * 发送请求
     * @param url
     * @param reqMap
     * @return
     */
    protected String reqApi(String url, Map<String, String> reqMap) {
        String html = StringUtils.EMPTY;

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        Iterator<Map.Entry<String, String>> entries = reqMap.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry<String, String> entry = entries.next();
            params.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        }

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost post = new HttpPost(url);

        int connectionRequestTimeout = 20000;
        int connectTimeout = 20000;
        int socketTimeout = 20000;
        RequestConfig config = RequestConfig.custom().setConnectionRequestTimeout(connectionRequestTimeout).setConnectTimeout(connectTimeout).setSocketTimeout(socketTimeout)
                .build();

        CloseableHttpResponse httpResponse = null;
        try {
            post.setEntity(new UrlEncodedFormEntity(params, "utf-8"));
            post.setConfig(config);
            httpResponse = httpClient.execute(post);
            HttpEntity httpEntity = httpResponse.getEntity();
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                html = EntityUtils.toString(httpEntity);
            }
            EntityUtils.consume(httpEntity);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            if (httpResponse != null) {
                try {
                    httpResponse.close();
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
            }
        }
        return html;
    }
}
