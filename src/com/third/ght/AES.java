package com.third.ght;

import java.io.PrintStream;
import java.security.Key;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class AES{
  public static final String KEY_ALGORITHM = "AES";
  public static final String CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";

  private static Key toKey(byte[] key)
    throws Exception
  {
    SecretKey secretKey = new SecretKeySpec(key, "AES");
    return secretKey;
  }

  public static byte[] decrypt(byte[] data, byte[] key)
    throws Exception
  {
    Key k = toKey(key);

    Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");

    cipher.init(2, k);

    return cipher.doFinal(data);
  }

  public static byte[] encrypt(byte[] data, byte[] key)
    throws Exception
  {
    Key k = toKey(key);

    Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");

    cipher.init(1, k);

    return cipher.doFinal(data);
  }

  public static byte[] initKey()
    throws Exception
  {
    KeyGenerator kg = KeyGenerator.getInstance("AES");

    kg.init(128);

    SecretKey secretKey = kg.generateKey();

    return secretKey.getEncoded();
  }

  public static void main(String[] args)
  {
    String data = "{\"merchId\":\"123456789\",\"timeStamp\":\"20140424\",\"version\":\"1.0\",\"orderNo\":\"993932\",\"orderDate\":\"20140424\",\"amount\":\"10000\",\"account\":\"13657458526\",\"chargeCards\":[{\"cardNo\":\"2667915483748918\",\"chargeAmt\":\"10000\"}],\"sign\":\"3232wwewew\"}";
    String aesKey = "WgpZmlYFyCNyrphu90Mazw==";
    try
    {
      byte[] key = Base64.decode(aesKey);

      byte[] encrData = encrypt(data.getBytes(), key);
      System.out.println(Base64.encode(encrData));
      System.out.println("key=== " + new String(decrypt(encrData, key)) + " ");
      System.out.println("key===" + key.length + " ");
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }
}