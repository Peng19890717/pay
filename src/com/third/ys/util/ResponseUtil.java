package com.third.ys.util;

import java.util.Map;

import com.ielpm.mer.sdk.secret.CertUtil;

public class ResponseUtil {
	
	/**
	 * 解析返回数据
	 * @param response
	 * @return
	 */
	public static Map<String,String> parseResponse(String response,CertUtil certUtil){
		//解析返回信息到map中
		Map<String,String> transMap = ParamUtil.getParamsMap(response, "utf-8");
		//获取签名
		String sign = (String) transMap.get("sign");
		sign = sign.replaceAll(" ", "+");
		transMap.remove("sign");
		//验签
		String transData = ParamUtil.getSignMsg(transMap);
		boolean result = false;                                        
		try {
			certUtil.verify(transData, sign);
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(!result){
			transMap.clear();
			transMap.put("tranData", transData);
			transMap.put("sign", sign);
			transMap.put("msg", "验签失败");
		}
		return transMap;
	}
}
