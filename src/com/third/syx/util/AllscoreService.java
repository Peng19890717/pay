package com.third.syx.util;

import java.util.Map;

import com.PayConstant;




public class AllscoreService {

	
    /**
     * 银行卡支付
     * @param sParaTemp 请求参数集合
     * @return 表单提交HTML信息
     */
    public static String bankPay(Map<String, String> sParaTemp) {

    	//增加基本配置
        //sParaTemp.put("service", "directPay");

        String strButtonName = "确认";

        return AllscoreSubmit.buildFormRSA(sParaTemp, PayConstant.PAY_CONFIG.get("SYX_PAY_URL")+"/serviceDirect.htm", "post", strButtonName);
    }
	 /**
     * 代付相关
     * @param sParaTemp 请求参数集合
     * @return 表单提交HTML信息
     */
	
	
    public static String agentpayCA(Map<String, String> sParaTemp,String methodName) {
    	  String publicKey = PayConstant.PAY_CONFIG.get("SYX_PUBLICKEY"); //安全密钥(奥斯卡公司提供)
    	  try {
	    	  if(sParaTemp.keySet().contains("bankCardNo")){
	    		
					String bankCardNo_m= RSAEncrypt.encryptForPuKey(publicKey, sParaTemp.get("bankCardNo"));
					sParaTemp.put("bankCardNo", bankCardNo_m);
	    	  }
	    	  if(sParaTemp.keySet().contains("cardId")){
	      		
					String cardId_m= RSAEncrypt.encryptForPuKey(publicKey, sParaTemp.get("cardId"));
					sParaTemp.put("cardId", cardId_m);
	    	  }
	    	  if(sParaTemp.keySet().contains("phoneNo")){
	      		
					String phoneNo_m= RSAEncrypt.encryptForPuKey(publicKey, sParaTemp.get("phoneNo"));
					sParaTemp.put("phoneNo", phoneNo_m);
	    	  }
	    	  if(sParaTemp.keySet().contains("realName")){
	      		
					String realName_m= RSAEncrypt.encryptForPuKey(publicKey, sParaTemp.get("realName"));
					sParaTemp.put("realName", realName_m);
	    	  }
    	  } catch (Exception e) {
				e.printStackTrace();
		  }
    	//增加基本配置
        String strButtonName = "确认";
        return AllscoreSubmit.buildFormCA(sParaTemp, PayConstant.PAY_CONFIG.get("SYX_PUBLICKEY")+methodName, "post", strButtonName);
    } 
   
}
