package com.third.xf;

import java.util.Map;

public class UcfForOnline {
	
	public static String createSign(String signKey, String signName, Map<String, String> params, String type) throws Exception
		  {
		    if ("MD5".equals(type))
		      return UcfDigestUtils.encode(signKey, UcfDigestUtils.getSignData(signName, params), type);
		    if ("RSA".equals(type)) {
		      try {
		        return RsaCoder.encryptByPublicKey(UcfDigestUtils.digestMD5(UcfDigestUtils.getSignData(signName, params)).toLowerCase(), signKey);
		      } catch (Exception e) {
		        throw new RuntimeException(e);
		      }
		    }
		    return "";
		  }
	public static Boolean verify(String signKey, String signName, String sign, Map<String, String> params, String type)
		    throws Exception
		  {
		    boolean isVerifySuccess = false;
		    if ((null == sign) || ("".equals(sign)) || (null == signKey) || ("".equals(signKey))) {
		      return Boolean.valueOf(isVerifySuccess);
		    }
		    if ("MD5".equals(type)) {
		      String signOut = UcfDigestUtils.encode(signKey, UcfDigestUtils.getSignData(signName, params), type);
		      if (sign.equals(signOut))
		        isVerifySuccess = true;
		    }
		    else if ("RSA".equals(type)) {
		      try {
		        String inputSign = RsaCoder.decryptByPublicKey(sign, signKey);
		        String mySign = UcfDigestUtils.digestMD5(UcfDigestUtils.getSignData(signName, params)).toLowerCase();
		        isVerifySuccess = inputSign.equals(mySign);
		      } catch (Exception e) {
		        isVerifySuccess = false;
		        throw new RuntimeException(e);
		      }
		    }
		    return Boolean.valueOf(isVerifySuccess);
		  }
}
