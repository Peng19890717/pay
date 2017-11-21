package com.third.ght;

import java.security.Key;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class AesUtils {
public static String decryptMsg(String ciphertext,String secretKey) throws Exception{
		
		return decrypt(ciphertext, secretKey);
	}

	
	public static String encryptMsg(String plaintext,String secretKey) throws Exception{
		
		return encrypt(plaintext, secretKey);
	}
	
	/*public static void main(String args[]) throws SecurityComponentException {
		String key = "S2cYd7xMbKe7XUsyoGlKIw==";
		String desStr = encrypt("你好",key);
		String id = decrypt(desStr, key);
		System.out.println("" + id);
	}*/

	/**
	 * AES数据解密
	 * 
	 * @param encryptStr
	 * @param key
	 * @return
	 * @throws NameAuthException
	 */
	public static String decrypt(final String encryptStr, final String key) {
		try {
			byte[] encryptStrBytes = Base64.decode(encryptStr);
			byte[] aesKeyBytes = Base64.decode(key);
			byte[] encryptByte = AES.decrypt(encryptStrBytes, aesKeyBytes);
			String decryptStr = new String(encryptByte,"UTF-8");
			return decryptStr;
		} catch (Exception e) {
			e.printStackTrace();
			
		}
		return null;
	}
	private static Key generateKey(String key)throws Exception{ 
        try{            
            SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), "AES"); 
            return keySpec; 
        }catch(Exception e){ 
            e.printStackTrace(); 
            throw e; 
        } 
 
    } 
	private static final String AESTYPE ="AES/ECB/PKCS5Padding"; 
	public static String AES_Encrypt(String keyStr, String plainText) { 
        byte[] encrypt = null; 
        try{ 
            Key key = generateKey(keyStr); 
            Cipher cipher = Cipher.getInstance(AESTYPE); 
            cipher.init(Cipher.ENCRYPT_MODE, key); 
            encrypt = cipher.doFinal(plainText.getBytes("UTF-8"));     
        }catch(Exception e){ 
            e.printStackTrace(); 
        }
        return new String(Base64.encode(encrypt)); 
    }
	
    public static String AES_Decrypt(String keyStr, String encryptData) {
        byte[] decrypt = null; 
        try{ 
            Key key = generateKey(keyStr); 
            Cipher cipher = Cipher.getInstance(AESTYPE); 
            cipher.init(Cipher.DECRYPT_MODE, key); 
            decrypt = cipher.doFinal(Base64.decode(encryptData)); 
        }catch(Exception e){ 
            e.printStackTrace(); 
        } 
        return new String(decrypt).trim(); 
    } 

	/**
	 * AES加密
	 * 
	 * @param str
	 * @param key
	 * @return
	 * @throws NameAuthException
	 */
	public static String encrypt(final String str, final String key){
		try {
			System.out.println("[str]"+str+"[key]"+key);
			byte[] encryptByte = AES.encrypt(str.getBytes("UTF-8"), Base64.decode(key));
			return Base64.encode(encryptByte);
		} catch (Exception e) {
			e.printStackTrace();

		}
		return null;
	}
	
	public static void main(String[] args) throws Exception {  
		String str1 = "1479276240491";
		String str2 ="CRu3Yc58KCPRo4PQpJa1yw==";
			
		System.out.println(encrypt(str1,str2));
		System.out.println(decrypt("DwdrOwpvjdixLR7rk29s2A==",str2));
		
    } 
}
