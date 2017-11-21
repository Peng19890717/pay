package com.third.syx.util;

import java.net.URLDecoder;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Map;


public class RSA
{

    public static final String SIGN_ALGORITHMS = "SHA1WithRSA";

    /**
     * RSA签名
     * 
     * @param content
     *            待签名数据
     * @param privateKey
     *            商户私钥
     * @param input_charset
     *            编码格式
     * @return 签名值
     */
    public static String sign(String content, String privateKey,
            String input_charset)
    {
        try
        {
        	System.out.println(Base64.decode(privateKey));
            PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(
                    Base64.decode(privateKey));
            KeyFactory keyf = KeyFactory.getInstance("RSA");
            PrivateKey priKey = keyf.generatePrivate(priPKCS8);

            java.security.Signature signature = java.security.Signature
                    .getInstance(SIGN_ALGORITHMS);

            signature.initSign(priKey);
            signature.update(content.getBytes(input_charset));

            byte[] signed = signature.sign();

            return Base64.encode(signed);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * RSA验签名检查
     * 
     * @param content
     *            待签名数据
     * @param sign
     *            签名值
     * @param allscore_public_key
     *            支付宝公钥
     * @param input_charset
     *            编码格式
     * @return 布尔值
     */
    public static boolean doCheck(Map<String,String> sParaTemp, String sign,String allscore_public_key, String input_charset){
        try {
        	//除去数组中的空值和签名参数
    		Map<String, String> sParaForSign = AllscoreCore.paraFilter(sParaTemp);
        	sign = URLDecoder.decode(sign);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            byte[] encodedKey = Base64.decode(allscore_public_key);
            PublicKey pubKey = keyFactory
                    .generatePublic(new X509EncodedKeySpec(encodedKey));
            java.security.Signature signature = java.security.Signature
                    .getInstance(SIGN_ALGORITHMS);
            signature.initVerify(pubKey);
            signature.update(AllscoreCore.createLinkString(sParaForSign).getBytes(input_charset));
            sign = sign.replaceAll("\\*", "+");
        	sign = sign.replaceAll("-", "/");
            boolean bverify = signature.verify(Base64.decode(sign));
            return bverify;

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * 得到私钥
     * 
     * @param key
     *            密钥字符串（经过base64编码）
     * @throws Exception
     */
    public static PrivateKey getPrivateKey(String key) throws Exception
    {

        byte[] keyBytes;

        keyBytes = Base64.decode(key);

        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);

        KeyFactory keyFactory = KeyFactory.getInstance("RSA");

        PrivateKey privateKey = keyFactory.generatePrivate(keySpec);

        return privateKey;
    }
    
//    public static void main(String[] args) {
//    	Map requestParams = new HashMap();
//    	Map params = new HashMap();
//    	
//    	params.put("sign", "N8nL2U-iTdnPHsk*L97EQkTA-o0KVPC7GKRNXVajM2fFZOReskueop2-2nQsCK9RWNcVcvLy0b-yR2Oq7-DeXsJQAqrSb9U4C91rwGiSsHbnM9m4npxSkS9S1etJlgj*bh2UWbCqRKIxD04DBVVWst7ikQiUvkaHzVrEelNW2SM=");
//    	params.put("body", "2000000003");
//    	params.put("notifyTime", "20170609105619");
//    	params.put("tradeStatus", "2");
//    	params.put("inputCharset", "UTF-8");
//    	params.put("subject", "在线充值");
//    	params.put("transTime", "20170609105311");
//    	params.put("notifyId", "notifyId");
//    	params.put("merchantId", "001016113001855");
//    	params.put("transAmt", "0.1");
//    	params.put("localOrderId", "51342977");
//    	params.put("outOrderId", "20170609105310873010051583971328");
//    	
//    	for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
//    		String name = (String) iter.next();
//    		String[] values = (String[]) requestParams.get(name);
//    		String valueStr = "";
//    		for (int i = 0; i < values.length; i++) {
//    			valueStr = (i == values.length - 1) ? valueStr + values[i]
//    					: valueStr + values[i] + ",";
//    		}
//    		params.put(name, valueStr);
//    		
//    		System.out.println("name="+name+",valueStr="+valueStr);
//    	}
//    	System.out.println("=================="+Payment.getContent(params));
//    	String privateKey = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAIenWLfaSfE4yaDuHWFgmO++uSBGlTuOJmbHEJNSxzdiiQ2aWsEuZH1vbDpqA94zep6aZVCv2nKzcE6j/ldwulEibDTC3IygEfBSgZJCtLSvylKmnDrA8RNngwtRhHaqcTbZJc/hGjCvhjVOnD61xud+35UJ9j1wbInbebRm4P27AgMBAAECgYA9WWKXK2K7dEpDvMyOyIgCDQheX22IVJ5rZuXJKuur0aVYAJHAwkFyNR0GQgHj0PZImlzto8owi5RkDGivlOy9rhebj97n1lxeM2VSK0QnTQzGJeTBYUj94QOKQLw6uoPjXPDp+aOH3aqXZb6qX0rvNeK8gD/FnZ4oerYBEt+MwQJBAOh+lfkduSczCWv8Wy+r2jN66D70p+S52ht/BuGQ1YW7RnaHPR+tloiIyBzwYqf4ALFEg1gzZ9cZ9+bI0clN+1sCQQCVXlS6XfYspSGMsDKsQ/zpd6V19W+wXHC9UNW7tHp3yYm7luvewIR6zVM10RFjsc6RiuoBmgQL+cCUt9TZSXUhAkEA5f5T3lzJDT4v0+ORUVdwQ/0SQzVD4cD5Qrom1H2yvNjGm/qtPOk8QVUOoZIsBn4QMUzVois4TlIiKuv+nGXIgQJAOv7oHjgQjG+ogaXyTcIIubntG1sWgyTvzoaH9LyDBp8OjI4+mVuWt94zOHVcFfFbsnaYmItZf9VkcBjvs+bkoQJAbCZr57b89/BBGviEII7f2qPAOKfeVIi0Mg2OtMNe3rj/llch/Fh8latj4l/Qc3kyfzfjSIqso4bnFn5mIXxMpw==";
//    	String sign2 = sign(Payment.getContent(params), privateKey, "UTF-8");
//    	String mysign = Base64.encode(sign2.getBytes());
//    	String content = "body=2000000003&inputCharset=UTF-8&localOrderId=44523&merchantId=001015013101118&notifyId=847929&notifyTime=20170609101301&outOrderId=20170609100922872999027388583936&subject=在线充值&tradeStatus=2&transAmt=2.2&transTime=20170609100332";
//    	String sign = "N8nL2U-iTdnPHsk*L97EQkTA-o0KVPC7GKRNXVajM2fFZOReskueop2-2nQsCK9RWNcVcvLy0b-yR2Oq7-DeXsJQAqrSb9U4C91rwGiSsHbnM9m4npxSkS9S1etJlgj*bh2UWbCqRKIxD04DBVVWst7ikQiUvkaHzVrEelNW2SM=";
//    	String public_key = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCHp1i32knxOMmg7h1hYJjvvrkgRpU7jiZmxxCTUsc3YokNmlrBLmR9b2w6agPeM3qemmVQr9pys3BOo/5XcLpRImw0wtyMoBHwUoGSQrS0r8pSppw6wPETZ4MLUYR2qnE22SXP4Rowr4Y1Tpw+tcbnft+VCfY9cGyJ23m0ZuD9uwIDAQAB";
//    	boolean doCheck = RSA.doCheck(Payment.getContent(params), mysign, public_key, "UTF-8");
//		System.out.println(doCheck);
//		
//    	
//	}
//    
//    
   
}
