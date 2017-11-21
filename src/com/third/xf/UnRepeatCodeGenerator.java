package com.third.xf;

import java.util.Calendar;
import java.util.UUID;

public class UnRepeatCodeGenerator {
	
	public static String createUnRepeatCode(String merchantId, String service, String merchantNo)
		    throws Exception
		  {
		    if ((merchantId == null) || ("".equals(merchantId)))
		      throw new Exception("merchantId:商户号不能为空");
		    if ((service == null) || ("".equals(service)))
		      throw new Exception("service:请求的服务名称不能为空");
		    if ((merchantNo == null) || ("".equals(merchantNo))) {
		      merchantNo = String.valueOf(Calendar.getInstance().getTimeInMillis());
		    }
		    StringBuilder strBuffer = new StringBuilder();
		    String randomVal = UUID.randomUUID().toString();
		    strBuffer.append(merchantId).append(service).append(merchantNo).append(randomVal);

		    String reqSn = UcfDigestUtils.digestMD5(strBuffer.toString());
		    if (("".equals(reqSn)) || (reqSn == null))
		      throw new Exception("获取reqSn失败，请重试");
		    return reqSn;
		  }

		  public static void main(String[] args) {
		    try {
		      System.out.println(createUnRepeatCode("1111", "2222", "3333"));
		    } catch (Exception e) {
		      e.printStackTrace();
		    }
		  }
}
