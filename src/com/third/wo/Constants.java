package com.third.wo;

import java.util.ArrayList;
import java.util.List;



public class Constants {
	/** 签名方式及字典集合 */
	public static final String SIGNTYPE_RSA_SHA256 ="RSA_SHA256";
	public static final String SIGNTYPE_SM2_SM3 ="SM2_SM3";
	
	public final static List<String> SIGN_TYPE_DICT = new ArrayList<String>(){
		private static final long serialVersionUID = -8399940394002018355L;	{
		add(SIGNTYPE_RSA_SHA256);add(SIGNTYPE_SM2_SM3);
	}};
	/** 字段集编码及字典集合 */
	public static final String CHARSET_UTF8 = "UTF-8";
	
	/** 一些签名验签过程中可能会过滤的字符串 */
	public static final String EXCEPT_SIGNMSG = "signMsg";
	public static final String EXCEPT_HMAC = "hmac";
	public static final String EXCEPT_CERT = "cert";

}
