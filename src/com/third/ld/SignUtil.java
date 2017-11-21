package com.third.ld;
import java.io.InputStream;
import java.security.PrivateKey;
import java.io.File;
import java.security.Signature;
import java.io.FileInputStream;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.KeyFactory;
import java.security.cert.X509Certificate;

public class SignUtil {
	
	public static PrivateKey getPk(String filePath){
		byte[] b = null;
	    InputStream in = null;
		try {
			in = new FileInputStream(new File(filePath));
			b = new byte[20480];
		    in.read(b);
		    PKCS8EncodedKeySpec peks = new PKCS8EncodedKeySpec(b);
		    KeyFactory kf = KeyFactory.getInstance("RSA");
		    return kf.generatePrivate(peks);
		} catch (Exception e) {
			
		}
		return null;
	}
	
	public static String sign(String plain,String merId){
		try{
			Signature sig = Signature.getInstance("SHA1withRSA");
	        sig.initSign(PkCertFactory.getPk(merId));
	        sig.update(plain.getBytes("gbk"));
	        byte signData[] = sig.sign();
	        String sign = new String(Base64.encode(signData));
	        System.out.println("签名：sign="+sign);
	        return sign;
		}catch(Exception ex){
			RuntimeException rex = new RuntimeException(ex.getMessage());
			rex.setStackTrace(ex.getStackTrace());
			throw rex;
		}
	}
	
	/**
	 * 验签
	 * @param sign
	 * @param plain
	 * @return
	 */
	public static boolean verify(String sign,String plain){
		X509Certificate cert = PkCertFactory.getCert();
		try{
			byte[] signData = Base64.decode(sign.getBytes());
			 Signature sig = Signature.getInstance("SHA1withRSA");
	            sig.initVerify(cert);
	            sig.update(plain.getBytes("gbk"));
	            boolean b = sig.verify(signData);
	            return b;
		}catch(Exception ex){
			RuntimeException rex = new RuntimeException(ex.getMessage());
			rex.setStackTrace(ex.getStackTrace());
			throw rex;
		}
	}
	
	
}
