package com.third.syx.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.PayConstant;


public class AllscoreCore {

    /**
     * 生成签名结果
     * @param sArray 要签名的数组
     * @return 签名结果字符串
     */
    public static String buildMysignRSA(Map<String, String> sArray,String privateKey) {
        String prestr = createLinkString(sArray); //把数组所有元素，按照“参数=参数值”的模式用“&”字符拼接成字符串
		String inputCharset = "UTF-8";  //（不可以修改）
		if(null==privateKey){
			privateKey =  PayConstant.PAY_CONFIG.get("SYX_PRIVATEKEY"); //安全密钥(奥斯卡公司提供)
		}
    	String mysign = RSA.sign(prestr,privateKey,inputCharset);
    	mysign = Base64.encode(mysign.getBytes());
        return mysign;
    }    

  
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
    public static String ChineseToUnicode(String s) {
   	 String as[] = new String[s.length()];
   	 String s1 = "";
   	 for (int i = 0; i < s.length(); i++) {
   	  as[i] = Integer.toHexString(s.charAt(i) & 0xffff);
   	  s1 = s1 + "\\u" + as[i];
   	 }
   	 return s1;
   	}
    /**
	 * 把中文转成Unicode码
	 * @param str
	 * @return
	 */
    public static String chinaToUnicode(String str){
		String result="";
		for (int i = 0; i < str.length(); i++){
            int chr1 = str.charAt(i);
            if(chr1>=19968&&chr1<=171941){//汉字范围 \u4e00-\u9fa5 (中文)
                result+="\\u" + Integer.toHexString(chr1);
            }else{
            	result+=str.charAt(i);
            }
        }
		return result;
	}
    
    /**
     * 生成签名结果
     * @param sArray 要签名的数组
     * @return 签名结果字符串
     */
    public static String buildWaitSign(Map<String, String> sArray) {
        String prestr = createLinkString(sArray); //把数组所有元素，按照“参数=参数值”的模式用“&”字符拼接成字符串

        return prestr;
    }
}
