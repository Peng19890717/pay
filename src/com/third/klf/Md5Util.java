package com.third.klf;

//import java.security.MessageDigest;
//import java.util.TreeMap;
//
//import com.PayConstant;
//
//public class SignUtil {
//    // 签名
//	private static String sign;
//
//	// 服务端返回的数据
//	private static String responseData;
//	public static final String MD5_KEY = PayConstant.PAY_CONFIG.get("KLF_WG_MD5_KEY");  
//
//	public static String querySign(TreeMap<String,String> param){
//		StringBuffer paramstr =new StringBuffer();
//		for (String pkey:param.keySet()){
//			String pvalue=param.get(pkey);
//			if(null != pvalue && "" != pvalue){
//				paramstr.append(pkey+"="+pvalue+",");
//			}
//		}		
//		String paramSrc=paramstr.substring(0, paramstr.length()-1);
//		sign=sign(paramSrc);
//		paramstr.append("sign=" + sign);
//	
//		return paramstr.toString();
//	}
//	/**
//	 * 对原串进行签名
//	 * 
//	 * @param paramSrc
//	 *            the source to be signed
//	 * @return
//	 */
//	public static String sign(String paramSrc) {
//		StringBuffer strbuff = new StringBuffer();
//		strbuff.append(paramSrc + "+key=" + MD5_KEY);
//		String sign = null;
//		try {
//			sign = getMD5Str(strbuff.toString());
//			System.out.println("签名:" + sign);
//		} catch (Exception e1) {
//			e1.printStackTrace();
//		}
//		return sign;
//	}
//	/**
//	 * MD5签名
//	 */
//	public static String getMD5Str(String str) {
//		return getMD5Str(str, "UTF-8");
//	}
//	/**
//	 * MD5签名
//	 */
//	public static String getMD5Str(String str, String encode) {
//		if (null == str) return null;
//		MessageDigest messageDigest = null;
//		try {
//			messageDigest = MessageDigest.getInstance("MD5");
//			messageDigest.reset();
//			messageDigest.update(str.getBytes(encode));
//		} catch (Exception e) {
//			e.printStackTrace();
//			return null;
//		}
//		byte[] byteArray = messageDigest.digest();
//		StringBuffer md5StrBuff = new StringBuffer();
//		for (int i = 0; i < byteArray.length; i++) {
//			if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)
//				md5StrBuff.append("0").append(Integer.toHexString(0xFF & byteArray[i]));
//			else
//				md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
//		}
//		return md5StrBuff.toString();
//	}
//}


import java.nio.charset.Charset;
import java.security.MessageDigest;

/**
 * MD5加密工具类
 */
public class Md5Util {

   
	public  static String MD5(String s) {
        char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
        try {
            byte[] btInput = s.getBytes(Charset.forName("UTF-8"));
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str).toUpperCase();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

}
