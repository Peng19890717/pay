package com.third.mobao;

import java.io.IOException;
import java.security.MessageDigest;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class MD5Util
{

  public static String MD5Encode(String aData) throws SecurityException { String resultString = null;
    try {
      MessageDigest md = MessageDigest.getInstance("MD5");
      resultString = bytes2HexString(md.digest(aData.getBytes("GBK")));
    } catch (Exception e) {
      e.printStackTrace();
      throw new SecurityException("MD5运算失败");
    }
    return resultString;
  }

  public static String bytes2HexString(byte[] b)
  {
    String ret = "";
    for (int i = 0; i < b.length; i++) {
      String hex = Integer.toHexString(b[i] & 0xFF);
      if (hex.length() == 1) {
        hex = '0' + hex;
      }
      ret = ret + hex.toUpperCase();
    }
    return ret;
  }

  public static String encode(byte[] bstr)
  {
    return new BASE64Encoder().encode(bstr);
  }

  public static byte[] decode(String str)
  {
    byte[] bt = (byte[])null;
    try {
      BASE64Decoder decoder = new BASE64Decoder();
      bt = decoder.decodeBuffer(str);
    } catch (IOException e) {
      e.printStackTrace();
    }

    return bt;
  }
}