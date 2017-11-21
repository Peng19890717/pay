package com.third.yhj;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import org.apache.commons.codec.binary.Hex;

public class SignUtils {
	public static String signMd5(String source, String key)
	  {
	    byte[] k_ipad = new byte[64];
	    byte[] k_opad = new byte[64];
	    byte[] keyb;
	    byte[] value;
	    try
	    {
	      keyb = key.getBytes("UTF-8");
	      value = source.getBytes("UTF-8");
	    } catch (UnsupportedEncodingException e) {
	      keyb = key.getBytes();
	      value = source.getBytes();
	    }

	    Arrays.fill(k_ipad, keyb.length, 64, (byte)54);
	    Arrays.fill(k_opad, keyb.length, 64, (byte)92);
	    for (int i = 0; i < keyb.length; i++) {
	      k_ipad[i] = ((byte)(keyb[i] ^ 0x36));
	      k_opad[i] = ((byte)(keyb[i] ^ 0x5C));
	    }

	    MessageDigest md = null;
	    try {
	      md = MessageDigest.getInstance("MD5");
	    } catch (NoSuchAlgorithmException e) {
	      return null;
	    }
	    md.update(k_ipad);
	    md.update(value);
	    byte[] dg = md.digest();
	    md.reset();
	    md.update(k_opad);
	    md.update(dg, 0, 16);
	    dg = md.digest();
	    return Hex.encodeHexString(dg);
	  }
}
