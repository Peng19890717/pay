package com.third.ght;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GatewayEgCore {
	/** 
     * 除去数组中的空值和签名参数
     * @param sArray 签名参数组
     * @return 去掉空值与签名参数后的新签名参数组
     */
    public static Map<String, String> paraFilter(Map<String, String> sArray) {

        Map<String, String> result = new HashMap<String, String>();

        if (sArray == null || sArray.size() <= 0) {
            return result;
        }

        for (String key : sArray.keySet()) {
            String value = sArray.get(key);
            if (value == null || value.equals("") || key.equalsIgnoreCase("sign")
                || key.equalsIgnoreCase("signType")) {
                continue;
            }
            result.put(key, value);
        }
       
        return result;
    }
	 /**
     * 生成签名结果
     * @param sArray 要签名的数组
     * @return 签名结果字符串
     */
    public static String buildMysignRSA(Map<String, String> sArray, String privateKey, String inputCharset) throws Exception{
        String prestr = createLinkString(sArray); //把数组所有元素，按照“参数=参数值”的模式用“&”字符拼接成字符串
    	//String prestr = "applyClientIp=192.168.105.133&charset=UTF-8&currency=156&isShowBandCard=1&merOrderId=160725480811222&merchantId=001015101501324&notifyUrl=http://192.168.205.8:8050/gatewayeg/&payType=03&payerId=lidong&productDesc=body&productName=test&serviceName=QuickPay&tranAmt=0.01&tranTime=20160112115945&version=V1";
        System.out.println("buildMysignRSA  验签串:"+prestr);
    	String mysign = RSA.sign(prestr,privateKey,inputCharset);
        return mysign;
    }
    /** 
     * 把数组所有元素排序，并按照“参数=参数值”的模式用“&”字符拼接成字符串
     * @param params 需要排序并参与字符拼接的参数组
     * @return 拼接后字符串
     */
    public static String createLinkString(Map<String, String> params) {

        List<String> keys = new ArrayList<String>(params.keySet());
        Collections.sort(keys);

        String prestr = "";
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = params.get(key);
            if(value!=null && value!=""){
	            if (i == keys.size() - 1) {//拼接时，不包括最后一个&字符
	                prestr = prestr + key + "=" + value;
	            } else {
	                prestr = prestr + key + "=" + value + "&";
	            }
            }
        }
        return prestr;
    }
}
