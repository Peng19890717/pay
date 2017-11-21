package com.third.mobao;

import java.io.UnsupportedEncodingException;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class BASEUtil
{

  public static String encode(String data)
  {
    try
    {
      return new BASE64Encoder().encode(data.getBytes("UTF-8"));
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }return null;
  }

  public static String decode(String str)
  {
    byte[] bt = (byte[])null;
    try {
      BASE64Decoder decoder = new BASE64Decoder();
      bt = decoder.decodeBuffer(str);
      return new String(bt, "UTF-8");
    } catch (Exception e) {
      e.printStackTrace();
    }return null;
  }
}