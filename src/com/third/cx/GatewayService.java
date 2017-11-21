package com.third.cx;


import java.io.IOException;
import java.net.URI;
import java.util.Map;

import com.PayConstant;




public class GatewayService {
	public static String directPay(Map<String, String> sParaTemp) {
		try {
			// 微信native支付API
			if ("RSA".equalsIgnoreCase(sParaTemp.get("signMethod"))) {
				sParaTemp.put("sign", GatewayUtil.signRequest(sParaTemp,PayConstant.PAY_CONFIG.get("CXPAY_WEI_XIN_PRI_KEY")));
			}
			String resultString = GatewayUtil.httpPostByDefaultTime(new URI(PayConstant.PAY_CONFIG.get("CXPAY_WEI_XIN_SCAN_URL")), sParaTemp);
			return resultString;
		} catch (IOException e) {
			e.printStackTrace();
		}  catch (Exception e) {
		     e.printStackTrace();
		 }
		return null;
	}
    public static String queryResult(Map<String, String> sParaTemp) {
    	try {
			if ("RSA".equalsIgnoreCase(sParaTemp.get("signMethod"))) {
				sParaTemp.put("sign", GatewayUtil.signRequest(sParaTemp,PayConstant.PAY_CONFIG.get("CXPAY_WEI_XIN_PRI_KEY")));
			}
			String resultString = GatewayUtil.httpPostByDefaultTime(new URI(PayConstant.PAY_CONFIG.get("CXPAY_WEI_XIN_QUERY_URL")), sParaTemp);
			return resultString;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  catch (Exception e) {
		     e.printStackTrace();
		}
		return null;
    } 
}
