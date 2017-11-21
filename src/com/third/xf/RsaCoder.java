package com.third.xf;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;

public class RsaCoder
{
  public static final String KEY_ALGORITHM = "RSA";
  public static final String PUBLIC_KEY = "RSAPublicKey";
  public static final String PRIVATE_KEY = "RSAPrivateKey";
  public static final String CHAR_SET = "utf-8";
  private static final int KEY_SIZE = 1024;
  private static final int MAX_BLOCK_SIZE = 128;
  private Map<String, Object> KEY_MAP;

  public RsaCoder()
    throws NoSuchAlgorithmException
  {
    KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");

    keyPairGen.initialize(1024);

    KeyPair keyPair = keyPairGen.generateKeyPair();

    RSAPublicKey publicKey = (RSAPublicKey)keyPair.getPublic();
    RSAPrivateKey privateKey = (RSAPrivateKey)keyPair.getPrivate();

    this.KEY_MAP = new HashMap(2);
    this.KEY_MAP.put("RSAPublicKey", publicKey);
    this.KEY_MAP.put("RSAPrivateKey", privateKey);
  }

  public static byte[] decryptByPrivateKey(byte[] data, byte[] key) throws Exception
  {
    PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(key);
    KeyFactory keyFactory = KeyFactory.getInstance("RSA");
    PrivateKey privateKey = keyFactory.generatePrivate(pkcs8KeySpec);
    Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
    cipher.init(2, privateKey);
    return cipher.doFinal(data);
  }

  public static String decryptByPrivateKey(String data, String key) throws IOException, Exception
  {
    byte[] dataByte = decryptByPrivateKey(Base64.decodeBase64(data), Base64.decodeBase64(key));
    return new String(dataByte);
  }

  public static byte[] decryptByPublicKey(byte[] data, byte[] key) throws Exception
  {
    X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(key);
    KeyFactory keyFactory = KeyFactory.getInstance("RSA");
    PublicKey publicKey = keyFactory.generatePublic(x509KeySpec);
    Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
    cipher.init(2, publicKey);
    return cipher.doFinal(data);
  }

  public static String decryptByPublicKey(String data, String key) throws IOException, Exception
  {
    byte[] dataByte = decryptByPublicKey(Base64.decodeBase64(data), Base64.decodeBase64(key));
    return new String(dataByte);
  }

  public static byte[] encryptByPublicKey(byte[] data, byte[] key) throws Exception
  {
    X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(key);
    KeyFactory keyFactory = KeyFactory.getInstance("RSA");
    PublicKey publicKey = keyFactory.generatePublic(x509KeySpec);
    Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
    cipher.init(1, publicKey);
    return cipher.doFinal(data);
  }

  public static String encryptByPublicKey(String data, String key) throws UnsupportedEncodingException, IOException, Exception
  {
    byte[] signByte = encryptByPublicKey(data.getBytes("utf-8"), Base64.decodeBase64(key));
    return Base64.encodeBase64String(signByte);
  }

  public static byte[] encryptByPrivateKey(byte[] data, byte[] key) throws Exception
  {
    PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(key);
    KeyFactory keyFactory = KeyFactory.getInstance("RSA");
    PrivateKey privateKey = keyFactory.generatePrivate(pkcs8KeySpec);
    Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
    cipher.init(1, privateKey);
    return cipher.doFinal(data);
  }

  public static String encryptByPrivateKey(String data, String key) throws IOException, Exception
  {
    byte[] signByte = encryptByPrivateKey(data.getBytes(), Base64.decodeBase64(key));
    return Base64.encodeBase64String(signByte);
  }

  public static boolean checkPublicEncrypt(String data, String sign, String pvKey) throws IOException, Exception
  {
    return data.equals(decryptByPrivateKey(sign, pvKey));
  }

  public static boolean checkPrivateEncrypt(String data, String sign, String pbKey) throws IOException, Exception {
    return data.equals(decryptByPublicKey(sign, pbKey));
  }

  public static String[] splitString(String data, int len)
    throws UnsupportedEncodingException
  {
    String[] results = new String[0];
    byte[] dataBytes = data.getBytes("utf-8");
    if ((data != null) && (data.trim().length() != 0)) {
      int x = dataBytes.length / len;
      int y = dataBytes.length % len;
      int z = 0;
      if (y != 0) {
        z = 1;
      }
      results = new String[x + z];

      int offset = 0;
      for (int i = 0; i < x + z; i++) {
        byte[] arr = new byte[len];
        int offsetLen = 0;
        if ((i == x + z - 1) && (y != 0)) {
          System.arraycopy(dataBytes, offset, arr, 0, y);
        } else {
          System.arraycopy(dataBytes, offset, arr, 0, len);
          while (arr[(arr.length - 1)] < 0) {
            offsetLen++;
            arr = new byte[len - offsetLen];
            System.arraycopy(dataBytes, offset, arr, 0, len - offsetLen);
          }
        }
        offset += len - offsetLen;
        results[i] = trimToEmpty(new String(arr, "utf-8"));
      }
    }
    return results;
  }

  private static String trimToEmpty(String str) {
    return str == null ? "" : str.trim();
  }

  private static byte[][] splitArray(byte[] data, int len)
  {
    int x = data.length / len;
    int y = data.length % len;
    int z = 0;
    if (y != 0) {
      z = 1;
    }
    byte[][] arrays = new byte[x + z][];

    for (int i = 0; i < x + z; i++) {
      byte[] arr = new byte[len];
      if ((i == x + z - 1) && (y != 0))
        System.arraycopy(data, i * len, arr, 0, y);
      else {
        System.arraycopy(data, i * len, arr, 0, len);
      }
      arrays[i] = arr;
    }
    return arrays;
  }

  private static String bcd2Str(byte[] bytes) {
    char[] temp = new char[bytes.length * 2];

    for (int i = 0; i < bytes.length; i++) {
      char val = (char)((bytes[i] & 0xF0) >> 4 & 0xF);
      temp[(i * 2)] = ((char)(val > '\t' ? val + 'A' - 10 : val + '0'));

      val = (char)(bytes[i] & 0xF);
      temp[(i * 2 + 1)] = ((char)(val > '\t' ? val + 'A' - 10 : val + '0'));
    }
    return new String(temp);
  }

  private static byte[] ASCII_To_BCD(byte[] ascii, int asc_len) {
    byte[] bcd = new byte[asc_len / 2];
    int j = 0;
    for (int i = 0; i < (asc_len + 1) / 2; i++) {
      bcd[i] = asc_to_bcd(ascii[(j++)]);
      bcd[i] = ((byte)((j >= asc_len ? 0 : asc_to_bcd(ascii[(j++)])) + (bcd[i] << 4)));
    }
    return bcd;
  }

  private static byte asc_to_bcd(byte asc)
  {
    byte bcd;
    if ((asc >= 48) && (asc <= 57)) {
      bcd = (byte)(asc - 48);
    }
    else
    {
      if ((asc >= 65) && (asc <= 70)) {
        bcd = (byte)(asc - 65 + 10);
      }
      else
      {
        if ((asc >= 97) && (asc <= 102))
          bcd = (byte)(asc - 97 + 10);
        else
          bcd = (byte)(asc - 48); 
      }
    }
    return bcd;
  }

  public static String encryptByPublicKeyWithSplit(String data, String key)
    throws Exception
  {
    StringBuffer result = new StringBuffer();
    String[] datas = splitString(data, 117);
    for (String splitData : datas) {
      if (splitData != null) {
        byte[] signByte = encryptByPublicKey(splitData.getBytes("utf-8"), Base64.decodeBase64(key));
        result.append(bcd2Str(signByte));
      }
    }
    return result.toString();
  }

  public static String encryptByPrivateKeyWithSplit(String data, String key)
    throws Exception
  {
    StringBuffer result = new StringBuffer();
    String[] datas = splitString(data, 117);
    for (String splitData : datas) {
      if (splitData != null) {
        byte[] signByte = encryptByPrivateKey(splitData.getBytes("utf-8"), Base64.decodeBase64(key));
        result.append(bcd2Str(signByte));
      }
    }
    return result.toString();
  }

  public static String decryptByPublicKeyWithSplit(String data, String key)
    throws Exception
  {
    StringBuffer result = new StringBuffer();
    byte[] bytes = data.getBytes("utf-8");
    byte[][] dataArrays = splitArray(ASCII_To_BCD(bytes, bytes.length), 128);
    for (byte[] dataArray : dataArrays) {
      byte[] dataByte = decryptByPublicKey(dataArray, Base64.decodeBase64(key));
      result.append(new String(dataByte));
    }
    return result.toString();
  }

  public static String decryptByPrivateKeyWithSplit(String data, String key)
    throws Exception
  {
    StringBuffer result = new StringBuffer();
    byte[] bytes = data.getBytes("utf-8");
    byte[][] dataArrays = splitArray(ASCII_To_BCD(bytes, bytes.length), 128);
    for (byte[] dataArray : dataArrays) {
      byte[] dataByte = decryptByPrivateKey(dataArray, Base64.decodeBase64(key));
      result.append(new String(dataByte));
    }
    return result.toString();
  }

  public byte[] getPrivateKey()
  {
    Key key = (Key)this.KEY_MAP.get("RSAPrivateKey");
    return key.getEncoded();
  }

  public byte[] getPublicKey()
  {
    Key key = (Key)this.KEY_MAP.get("RSAPublicKey");
    return key.getEncoded();
  }

  public String getPrivateKeyBase64()
  {
    Key key = (Key)this.KEY_MAP.get("RSAPrivateKey");
    return Base64.encodeBase64String(key.getEncoded());
  }

  public String getPublicKeyBase64()
  {
    Key key = (Key)this.KEY_MAP.get("RSAPublicKey");
    return Base64.encodeBase64String(key.getEncoded());
  }
}