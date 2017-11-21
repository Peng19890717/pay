package com.third.yyt;

import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.Security;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import sun.misc.BASE64Decoder;


/**
 *
 * Title: DES加密算法类
 * Copyright: Copyright (c) 2002 yipsilon.com
 * @author yipsilon
 * @version 0.1
 */

public class DSDES {
  public static int _DES = 1;
  public static int _DESede = 2;
  public static int _Blowfish = 3;

  private Cipher p_Cipher;
  private SecretKey p_Key;
  private String p_Algorithm;

  private void selectAlgorithm(int al){
    switch(al){
      default:
      case 1:
        this.p_Algorithm = "DES";
        break;
      case 2:
        this.p_Algorithm = "DESede";
        break;
      case 3:
        this.p_Algorithm = "Blowfish";
        break;
    }
  }

  public DSDES(int algorithm) throws Exception{
    this.selectAlgorithm(algorithm);
    Security.addProvider(new com.sun.crypto.provider.SunJCE());
    this.p_Cipher = Cipher.getInstance(this.p_Algorithm);
  }

  public byte[] getKey(){
    return this.checkKey().getEncoded();
  }

  private SecretKey checkKey(){
    try{
      if(this.p_Key == null){
        KeyGenerator keygen = KeyGenerator.getInstance(this.p_Algorithm);
        this.p_Key = keygen.generateKey();
      }
    }catch(NoSuchAlgorithmException nsae){}
    return this.p_Key;
  }

  public void setKey(byte[] enckey){
    this.p_Key = new SecretKeySpec(enckey,this.p_Algorithm);
  }

  public byte[] encode(byte[] data) throws Exception{
    this.p_Cipher.init(Cipher.ENCRYPT_MODE,this.checkKey());
    return this.p_Cipher.doFinal(data);
  }

  public byte[] decode(byte[] encdata, byte[] enckey) throws Exception{
    this.setKey(enckey);
    this.p_Cipher.init(Cipher.DECRYPT_MODE,this.p_Key);
    return this.p_Cipher.doFinal(encdata);
  }

  public String byte2hex(byte[] b){
    String hs = "";
    String stmp = "";
    for(int i = 0;i < b.length;i++){
      stmp = Integer.toHexString(b[i] & 0xFF);
      if(stmp.length() == 1) hs += "0" + stmp;
      else hs += stmp;
    }
    return hs.toUpperCase();
  }

  public byte[] hex2byte(String hex) throws IllegalArgumentException{
    if(hex.length() % 2 != 0)
      throw new IllegalArgumentException();
    char[] arr = hex.toCharArray();
    byte[] b = new byte[hex.length()/2];
    for(int i = 0,j = 0,l = hex.length();i < l;i++,j++){
      String swap = "" + arr[i++] + arr[i];
      int byteint = Integer.parseInt(swap,16) & 0xFF;
      b[j] = new Integer(byteint).byteValue();
    }
    return b;
  }
  

  public static byte[] strToHex(String arg0) throws Exception{
    byte[] bit = new byte[8];
    for (int k = 0, kk = 0; k < arg0.length(); k += 2, kk++) {
      char tmp1 = arg0.charAt(k);
      char tmp2 = arg0.charAt(k + 1);
      bit[kk] = (byte) ( ( (tmp1 & 0x0f) << 4) | (tmp2 & 0x0f));
    }
    return bit;

  }

  
 
  //根据密钥生成对应的密文
  
  public static String getWhiteData(byte[] mykey ,byte[] data) throws Exception {
	    byte[] key; //密钥文件(byte)
	    //byte[] mykey = {49, 49, 50, 50, 51, 51, 52, 52}; //初始化密钥文件(byte)
	    DSDES des = new DSDES(DSDES._DES); // 声明DES
	    des.setKey(mykey); //设置密钥文件
	    key = des.getKey(); //获取随机生成的密钥--这里获取的实际上是固定的密钥，呵呵
	    byte[] dec = des.decode(data, key); //解密文件,其中转换十六进制密钥为byte
	    return new String(dec);
	  }
  
  	public static String getBlackData( byte[] mykey ,byte[] data) throws Exception {
  		byte[] key; //密钥文件(byte)
  		//byte[] mykey = {49, 49, 50, 50, 51, 51, 52, 52}; //初始化密钥文件(byte)
  		DSDES des = new DSDES(DSDES._DES); // 声明DES
  		des.setKey(mykey); //设置密钥文件
  		key = des.getKey(); //获取随机生成的密钥--这里获取的实际上是固定的密钥，呵呵
  		byte[] enc = des.encode(data); //生成加密文件(byte)
  		String hexenc = des.byte2hex(enc); //生成十六进制加密文件
  		return hexenc;
  	}
  	//解密
  	private byte[] byteMi = null;
    private byte[] byteMing = null;
    private String strMi= "";
    private String strM= ""; 
    private Key key;
  	public  void setDesString(String strMi){  
  	   BASE64Decoder base64De = new BASE64Decoder();   
  	    try
  	    {
  	     this.byteMi = base64De.decodeBuffer(strMi);  
  	      this.byteMing = this.getDesCode(byteMi);  
  	      this.strM = new String(byteMing,"UTF8");  
  	      }  
  	    catch(Exception e)
  	     {
  	      e.printStackTrace();
  	      }  
  	    finally
  	     {
  	      base64De = null;  
  	      byteMing = null;  
  	      byteMi = null;
  	      }  
  	  
  	  }
  	private byte[] getDesCode(byte[] byteD){
  	   Cipher cipher;  
  	    byte[] byteFina=null;  
  	    try{
  	     cipher = Cipher.getInstance("DES");  
  	      cipher.init(Cipher.DECRYPT_MODE,key);  
  	      byteFina = cipher.doFinal(byteD);
  	      }
  	   catch(Exception e)
  	     {
  	      e.printStackTrace();
  	      }
  	   finally
  	     {
  	      cipher=null;
  	      }  
  	    return byteFina;
  	  } 

  
  public static void main(String[] args) throws Exception{

    //String value = "123456";
	//String value = "merchno=100000000000001&dsorderid=1406787028045&amount=0.10&currency=USD&dsyburl=http://test5.bonusepay.com/bonuspay_wk_v1.01/notifytest.jsp&dstburl=http://test5.bonusepay.com/bonuspay_wk_v1.01/returntest.jsp?id=1406787028045&product=test&productdesc=test";
	//byte[] mykey ={49,49,49,49,49,49,49,49}; 
	//byte[] bit = DES.strToHex(value);//对明文进行码制转换
	//String  sss = StringUtil.byte2hex(DES.encrypt(mykey,value.getBytes()));
    //String key = "11111111";
    //byte[] d = DES.getMacStr(key.getBytes(),bit);
    //String tmpStr = DES.getMacStr(key.getBytes(),bit);
    //String tmpStr1 = StringUtil.parseByteStr(tmpStr);
    //System.out.println("1111111======"+sss);
    
    DSDES ds= new DSDES(1);
    //key ="88888888";
    //key ="d3b03885";
    //String kkk = ds.getBlackData(key.getBytes(), value.getBytes());
    //System.out.println("1111111======"+kkk);
	 
	String deses = DSDES.getBlackData("d4d25ccb".getBytes(), "returncode=00&merchno=611100000310964&dsorderid=3600039206124681&amount=71.00&orderid=201704191043324757000106&transdate=20170419&transtime=104350".getBytes("utf-8"));
	System.out.println(deses);
	  
    //String data = DSDES.getWhiteData("b0aea7eb".getBytes(), ds.hex2byte("2E37E2D51A6E2E71C32F7BCA0F9A8E2B2B8CDA58263761D60F950600077C82D67AF3D5C77E023A2CBA3FED702215A45AFA6CDFF47C47FC190EF5B03DDA475E9366F5C60298B8AAD69117D6678626374FF8D85CBE9EC12B9D2D566AB4D871DA086493739CA927AF3C0402F213DDF00C36280BBBF493CA9CC184DA204BD2F9687830973BF6BF6F456B27AEFB2CED8B3A31B630557F01E9F9AB8E5BA0CF4B04C70EE4844BAC717E1ECECDDDA975DEB064CC7E6FFF876725EF97BAFB7B3A8402D7208289F73DE1CCB7842DA7C03FBA5F2A8220AECD7B0ACC62CB9D8E454253EE53BA099405965FDD39D64A0C468D15E089821DB6CB44E90829B57F39D40D94916C171A90A703A24589BEF107E6AC41C82FE07CD168387ABD488F64D7303EB69616BAE0D4907AD20CA4D8BEF8AE527B95FFA49E36A1F3591B5889B33EA8F969086752E453A0655AA4E547F07C2DB0C870ECC38509A7E4BF0D2240"));
    //System.out.println(data);
    
    
    
    
  }
}

