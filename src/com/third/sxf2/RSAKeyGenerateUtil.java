package com.third.sxf2;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.PublicKey;
import java.util.HashMap;
import java.util.Map;

public class RSAKeyGenerateUtil {
	
	public static Map genKey() throws Exception{
		
		KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
		kpg.initialize(1024);
		KeyPair kep = kpg.generateKeyPair();
		Provider p  = kpg.getProvider();
		PrivateKey pkey = kep.getPrivate();
		PublicKey pubkey = kep.getPublic();

		Map<String,Object> param=new HashMap<String,Object>();
		param.put("PublicKey", new String(Base64Utils.encode(pubkey.getEncoded())));
		param.put("PrivateKey", new String(Base64Utils.encode(pkey.getEncoded())));
		
		return param;
	}

	
	public static void main(String[] args) throws Exception{
			Map param=genKey();
			String pb=(String) param.get("PublicKey");
			String pr=(String) param.get("PrivateKey");
			System.out.println("PublicKey:"+pb);
			System.out.println("PrivateKey:"+pr);
		
	}

}
