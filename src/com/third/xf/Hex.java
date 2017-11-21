package com.third.xf;

public class Hex {
	
	public static final String DEFAULT_CHARSET_NAME = "UTF-8";
	private static final char[] DIGITS_LOWER = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
	private static final char[] DIGITS_UPPER = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
	
	public static byte[] decodeHex(char[] data) throws Exception{
		
	    int len = data.length;
	    if ((len & 0x1) != 0) {
	      throw new  Exception("Odd number of characters.");
	    }
	    byte[] out = new byte[len >> 1];
	    int i = 0; for (int j = 0; j < len; i++) {
	      int f = toDigit(data[j], j) << 4;
	      j++;
	      f |= toDigit(data[j], j);
	      j++;
	      out[i] = ((byte)(f & 0xFF));
	    }
	    return out;
	  }
	protected static int toDigit(char ch, int index) throws Exception{
	    int digit = Character.digit(ch, 16);
	    if (digit == -1) {
	      throw new Exception("Illegal hexadecimal character " + ch + " at index " + index);
	    }
	    return digit;
	  }
	 public static String encodeHexString(byte[] data){
	    return new String(encodeHex(data));
	  }
	 public static char[] encodeHex(byte[] data)
	  {
	    return encodeHex(data, true);
	  }
	 public static char[] encodeHex(byte[] data, boolean toLowerCase)
	  {
	    return encodeHex(data, toLowerCase ? DIGITS_LOWER : DIGITS_UPPER);
	  }
	 protected static char[] encodeHex(byte[] data, char[] toDigits)
	  {
	    int l = data.length;
	    char[] out = new char[l << 1];

	    int i = 0; for (int j = 0; i < l; i++) {
	      out[(j++)] = toDigits[((0xF0 & data[i]) >>> 4)];
	      out[(j++)] = toDigits[(0xF & data[i])];
	    }
	    return out;
	  }
}
