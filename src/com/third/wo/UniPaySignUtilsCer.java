package com.third.wo;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import ccit.security.bssp.CAUtility;
import ccit.security.bssp.common.TypeConstant;
import com.pay.merchantinterface.service.BFBPayService;
/**
 * 商户接入签名验签 
 * @author 
 * @date 2016年12月18日
 */
public class UniPaySignUtilsCer {
	private static final Log log = LogFactory.getLog(BFBPayService.class);
	
	/**
	 * 商户参数签名
	 * @param reqMap 签名数据Map (注意：null 或 空值 或 一些特殊字符串不参与验签)
	 * @param signType 签名类型  SHA256_RSA，SM2_SM3
	 * @param isIssue  代发签名源串，结尾需添加  |
	 * @return
	 */
	public static String merSign(Map<String, String> reqMap, String signType,boolean isIssue){
		String signResult = null;
		if (!Constants.SIGN_TYPE_DICT.contains(signType)) {
			log.info("不支持此签名类型:"+signType);
		}else if( Constants.SIGNTYPE_RSA_SHA256.equals(signType) || 
				Constants.SIGNTYPE_SM2_SM3.equals(signType)){
			 try {
				 signResult = rsaOrSm2Sign(getSignSourMsg(reqMap,isIssue), signType);
			} catch (Exception e) {
				log.error("商户用非对称加密方式签名-签名异常",e);
			}
		}
		return signResult;
	}
	
	/**
	 * 商户参数签名
	 * @param reqMap 签名数据Map (注意：null 或 空值 或 一些特殊字符串不参与验签)
	 * @param signType 签名类型  SHA256_RSA，SM2_SM3
	 * @return
	 */
	public static String merSign(Map<String, String> reqMap, String signType){
		return merSign(reqMap,signType,false);
		
	}
	
	
	/**
	 * 商户参数签名
	 * @param reqMap 签名数据Map (注意：null 或 空值 或 一些特殊字符串不参与验签)
	 * @param signType 签名类型  SHA256_RSA，SM2_SM3
	 * @param merNo 根据商户号码查找私钥证书
	 * @return
	 */
/*	public static String merSign(Map<String, String> reqMap, String signType,String merNo,boolean isIssue){
		String signResult = null;
		if (!Constants.SIGN_TYPE_DICT.contains(signType)) {
			log.info("不支持此签名类型:"+signType);
		}else if( Constants.SIGNTYPE_RSA_SHA256.equals(signType) || 
				Constants.SIGNTYPE_SM2_SM3.equals(signType)){
			 try {
				 signResult = rsaOrSm2Sign(getSignSourMsg(reqMap,isIssue), signType,merNo);
			} catch (Exception e) {
				log.error("商户用非对称加密方式签名-签名异常",e);
			}
		}
		return signResult;
	}*/
	
	
	
	
	/**
	 * 商户参数签名
	 * @param reqMap 签名数据Map (注意：null 或 空值 或 一些特殊字符串不参与验签)
	 * @param signType 签名类型  SHA256_RSA
	 * @param isIssue  非代发签名 传false
	 * @param P12cert 私钥证书BYTE
	 * @param pincode 私钥密码
	 * @return
	 */
	public static String merSign(Map<String, String> reqMap, String signType,boolean isIssue,byte[] P12cert, String pincode){
		String signResult = null;
		if (!Constants.SIGN_TYPE_DICT.contains(signType)) {
			log.info("不支持此签名类型:"+signType);
		}else if( Constants.SIGNTYPE_RSA_SHA256.equals(signType) || 
				Constants.SIGNTYPE_SM2_SM3.equals(signType)){
			 try {
				 signResult = rsaOrSm2SignFile(getSignSourMsg(reqMap,isIssue), signType, P12cert, pincode);
			} catch (Exception e) {
				log.error("商户用非对称加密方式签名-签名异常",e);
			}
		}
		return signResult;
	}
	
	/*public static String merSign(Map<String, String> reqMap, String signType,String merNo){
		return merSign( reqMap,  signType, merNo,false);
	}*/
	
	/**
	 * 商户验签
	 * @author guodong
	 * @date 2016年12月28日
	 * @param reqMap 请求map参数 (注意：null 或 空值 或 一些特殊字符串不参与验签)
	 * @param signType 签名类型 SHA256_RSA，SM2_SM3
	 * @param cert 联通支付公司证书base64
	 * @param isIssue 代发系统签名
	 * @return true 通过，false 不通过
	 * 
	 */
	public static boolean merVerify(Map<String, String> reqMap, String signType,String signMsg,String cert,boolean isIssue) {
		//log.info("参数信息：reqMap---- "+JSONArray.fromObject(reqMap).toString());
		//log.info("参数信息：signType----  "+signType + " ,isIssue " + isIssue);
		//log.info("参数信息：cert----  "+cert );
		if (!Constants.SIGN_TYPE_DICT.contains(signType)) {
			log.info("不支持此签名类型:"+signType);
			return false;
		}else{
			try {
				return rsaOrSm2Verify(UniPaySignUtilsCer.getSignSourMsg(reqMap,isIssue), signMsg, signType, cert);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return false;
		}
	}
	
	
	public static boolean merVerify(Map<String, String> reqMap, String signType,String signMsg,String cert) {
		return merVerify(reqMap, signType, signMsg,cert,false);
		
	}
	
	/**
	 * 待签名参数预处理，去空、排序
	 * @author guodong
	 * @date 2016年12月28日
	 * @param params 传入参数Map
	 * @return 真实参与签名的参数串
	 */
	private static String getSignSourMsg(Map<String, String> params,boolean isIssue) {
		params = filterNoParams(params);
		String signSource = createLinkString(params,isIssue);
		log.info("待签名参数串：[" + signSource+"]");
		return signSource;
	}
	
	
	
	/**
	 * 过滤无用参数,包括参数值为空及制定参数-hmac、signMsg、cert
	 * @author guodong
	 * @date 2016年12月28日
	 * @param params Map 带过滤的参数
	 * @return Map 过滤后的参数
	 * 
	 */
	private static Map<String, String> filterNoParams(Map<String, String> params) {
		Map<String, String> newParam = new HashMap<String, String>();
		for (Map.Entry<String, String> entry : params.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();
			if (Constants.EXCEPT_SIGNMSG.equalsIgnoreCase(key) || Constants.EXCEPT_CERT.equalsIgnoreCase(key)
					|| Constants.EXCEPT_HMAC.equalsIgnoreCase(key)) {
				continue;
			}
			if (value == null||value.length()==0) {
				continue;
			}
			newParam.put(key, value);
		}
		return newParam;
	}
	

	/**
	 * 排序后生成待签名字符串
	 * @param params Map 待处理的参数
	 * @param params isIssue 代发需
	 * @return String
	 */
	private static String createLinkString(Map<String, String> params,boolean isIssue) {
		List<String> keys = new ArrayList<String>(params.keySet());
		Collections.sort(keys);
		StringBuilder prestr = new StringBuilder();
		for (int i = 0; i < keys.size(); i++) {
			String key = keys.get(i);
			prestr.append(key).append("=").append(params.get(key)).append("|");
		}
		//return prestr.toString();//.substring(0, prestr.length()-1);
		//代发签名，结尾需保留  |
		if(isIssue){
			return prestr.toString();
		}
		return prestr.substring(0, prestr.length()-1);
	}
	/**
	 * 转map为请求数据  key=value &...
	 * @param params
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	public static String createReqString(Map<String, String> params) throws UnsupportedEncodingException {
		List<String> keys = new ArrayList<String>(params.keySet());
		StringBuilder prestr = new StringBuilder();
		for (int i = 0; i < keys.size(); i++) {
			String key = keys.get(i);
			prestr.append(key).append("=").append(URLEncoder.encode(params.get(key),"utf-8")).append("&");
		}
		return prestr.substring(0, prestr.length()-1);
	}
	
	/**
	 * 模拟商户验签(RSA 或者 SM2)
	 * @author guodong
	 * @date 2016年12月21日
	 * @param originalData 原数据
	 * @param signMsg 签名信息
	 * @param algorithm 签名算法
	 * @param cert 证书
	 * @return
	 * @throws Exception
	 * 
	 */
	private static boolean rsaOrSm2Verify(String originalData,String signMsg,String algorithm, String cert) throws Exception{
		/**
		 * 0x00000103：SHA1_RSA签名算法
		 * 0x00000105：SHA256_RSA签名算法
		 * 0x00000104：SM3_SM2签名算法
		*/
		byte[] indata = originalData.getBytes(Constants.CHARSET_UTF8);
		byte[] certbytes = cert.getBytes(Constants.CHARSET_UTF8);
		byte[] signMsgbytes = signMsg.getBytes(Constants.CHARSET_UTF8);
		log.info("originalData:[" + originalData +"]" );
		log.info("cert:[" + cert +"]");
		log.info("signMsg:[" + signMsg +"]");
		int verifyWithCert=1 ;
		if(Constants.SIGNTYPE_RSA_SHA256.equalsIgnoreCase(algorithm)){
			verifyWithCert = CAUtility.verifyWithCert(0x00000105, certbytes, indata, signMsgbytes);
		}else if(Constants.SIGNTYPE_SM2_SM3.equalsIgnoreCase(algorithm)){
			verifyWithCert = CAUtility.EccVerifySignByCert(0x00000104, certbytes, indata, signMsgbytes);
		}
		return verifyWithCert==0?true:false;
	}
	
	
	
	
	
	/**
	 * 模拟商户签名(RSA 或者 SM2)
	 * @author guodong
	 * @date 2016年12月9日
	 * @param originalData 原数据
	 * @param algorithm	签名算法
	 * @return 签名信息
	 * @throws Exception
	 * 
	 */
	private static String rsaOrSm2Sign(String originalData,String algorithm) throws Exception{
		byte[] indata = originalData.getBytes(Constants.CHARSET_UTF8);
		byte[] signBytes = null;
		signBytes = CAUtility.RsaSignByP12(TypeConstant.CA_SHA256WITHRSA, indata);
		return new String(CAUtility.base64Encode(signBytes));
	}
	
	
	/**
	 * 商户签名RSA
	 * @author wangshengyao
	 * @date 2017年10月26日
	 * @param originalData 原数据
	 * @param algorithm 签名算法
	 * @param P12cert 私钥证书
	 * @param pincode 证书密码
	 * @return
	 * @throws Exception
	 */
	private static String rsaOrSm2SignFile(String originalData,String algorithm,byte[] P12cert, String pincode) throws Exception{
		byte[] indata = originalData.getBytes(Constants.CHARSET_UTF8);
		byte[] signBytes = null;
		signBytes = CAUtility.RsaSignByP12Cert(TypeConstant.CA_SHA256WITHRSA, indata, P12cert, pincode);
		return new String(CAUtility.base64Encode(signBytes));
	}
	
	
	
	
	
	/**
	 * 模拟商户签名(RSA 或者 SM2)
	 * @author wangshengyao
	 * @date 2017年03月09日
	 * @param originalData 原数据
	 * @param algorithm	签名算法
	 * @param merNo	商户号码
	 * @return 签名信息
	 * @throws Exception
	 * 
	 */
	/*private static String rsaOrSm2Sign(String originalData,String algorithm,String merNo) throws Exception{
		byte[] indata = originalData.getBytes(Constants.CHARSET_UTF8);
		byte[] signBytes = null;
		signBytes = CAUtility.RsaSignByP12Conf(TypeConstant.CA_SHA256WITHRSA, indata,merNo);
		return new String(CAUtility.base64Encode(signBytes));
	}*/
	
	
	public static String toHexString(String s) {
		//String s = "交易成功";
		  try {
		   byte[] b = s.getBytes();
		   String str = " ";
		   for (int i = 0; i < b.length; i++) {
		    Integer I = new Integer(b[i]);
		    String strTmp = I.toHexString(b[i]);
		    if (strTmp.length() > 2)
		   //  strTmp = "[0x"+strTmp.substring(strTmp.length() - 2)+"]";
		    strTmp = strTmp.substring(strTmp.length() - 2);
		    str = str + strTmp;
		   }
		   System.out.println(str) ;
		   return str;
		  } catch (Exception e) {
		   e.printStackTrace();
		  }
		return null;
	}
	
	public static void main(String[] args) {
		
		merVerifyTest();
		//merSignTest();
	}

	private static void merVerifyTest() {
		//String str = "mp=1$orderid=wsy20170828174608end$passport=$paybalance=1000$payfloodid=20210406020154475468$payresult=1$paymentbalancedetail=02:1000$retype=1$resptime=20170828174640$signmsg=SIx5TT1VxfahVmhfiDUOfYb5+KoHbZCJPgfueMKoO4u2dfUmCnf+WM164K6/dv+Quhyxa0bDpjOa+OSIJqp48Bb2BW2x7BvRvbfugksw3J7dswciexwuQMej81/2ma5GcPt1wN9w/Vs2JEdoIfWYlWBxAJD5PfNduq9X/TQ40tE=$userid=201708281746";
		/*String	cert = "MIICljCCAYCgAwIBAgIEATb+cTALBgkqhkiG9w0BAQswLzELMAkGA1UEBhMCQ04x" +
	            "IDAeBgNVBAMMF0NISU5BIFVOSUNPTSBDTEFTUzEgQ0ExMB4XDTE2MTIyMTA5NTYz" +
	            "NFoXDTE2MTIyNDA5NTYzNFowJDELMAkGA1UEBhMCQ04xFTATBgNVBAMMDOS9oOWl" +
	            "vVJTQTExMTCBnTALBgkqhkiG9w0BAQEDgY0AMIGJAoGBAKxeaim0jaSqo2KS7KU6" +
	            "lHsVUPGOzC7V7yZoTNYsGzjwy/c/kQLkbU1/MeTnIXPjtkFwyWmZXL2drFbUxZ6e" +
	            "SPRd69OzGfG8dpqEw53wLE5BI0xTLv/wQZyMVcoXAsjX5VYa5IlYDAZhPp5IogtM" +
	            "T9BxtYukeP9o+dXglXEylzrDAgMBAAGjTzBNMB8GA1UdIwQYMBaAFKt7hVVYh97I" +
	            "/vJ2QAPvDBz6gj4vMAsGA1UdDwQEAwIHgDAdBgNVHQ4EFgQUp7Om18jYq6TrNUYi" +
	            "FPChtOl5Z0IwCwYJKoZIhvcNAQELA4IBAQBu+Na/ltAA1lxeG8Q2g+7XeipEwFwp" +
	            "FNu+YLswJqXZbFgT7UbsU5ycpDjmcaAoaAiTdfkG08D9JTsaNOAE+J/6iqN0TLb7" +
	            "/HvV5G3SODPYV9iuoLNNOlWnj6UFoat8rhpHzFcMRlcFdM/jOKU3AK7ze06O5zad" +
	            //"jPQq5/1k2BPCaNYb9Umy00J9fSuDmvD6RQt3wL/kY3lQR1yEm5amIUQ2j0ujw9F3" +
	            "CAQq5/1k2BPCaNYb9Umy00J9fSuDmvD6RQt3wL/kY3lQR1yEm5amIUQ2j0ujw9F3" +
//	            "wc8A1lmRHw5plVhhkJaLZ/+nMmYEYpv4Cf9Kh7z3lu0fL0dlEf+uWLe8A42tsQOf\n" +
	            "wc8A1lmRHw5plVhhkJaLZ/+nMmYEYpv4Cf9Kh7z3lu0fL0dlEf+A2345642tsQOf" +
	            "djekKqXD9YXNUbM9s6d27DsIfY/8c4LodqPq+Y7FiC3UO28JigNO6FEe";*/
		//String cert = "MIICljCCAYCgAwIBAgIEATb+cTALBgkqhkiG9w0BAQswLzELMAkGA1UEBhMCQ04xIDAeBgNVBAMMF0NISU5BIFVOSUNPTSBDTEFTUzEgQ0ExMB4XDTE2MTIyMTA5NTYzNFoXDTE2MTIyNDA5NTYzNFowJDELMAkGA1UEBhMCQ04xFTATBgNVBAMMDOS9oOWlvVJTQTExMTCBnTALBgkqhkiG9w0BAQEDgY0AMIGJAoGBAKxeaim0jaSqo2KS7KU6lHsVUPGOzC7V7yZoTNYsGzjwy/c/kQLkbU1/MeTnIXPjtkFwyWmZXL2drFbUxZ6eSPRd69OzGfG8dpqEw53wLE5BI0xTLv/wQZyMVcoXAsjX5VYa5IlYDAZhPp5IogtMT9BxtYukeP9o+dXglXEylzrDAgMBAAGjTzBNMB8GA1UdIwQYMBaAFKt7hVVYh97I/vJ2QAPvDBz6gj4vMAsGA1UdDwQEAwIHgDAdBgNVHQ4EFgQUp7Om18jYq6TrNUYiFPChtOl5Z0IwCwYJKoZIhvcNAQELA4IBAQBu+Na/ltAA1lxeG8Q2g+7XeipEwFwpFNu+YLswJqXZbFgT7UbsU5ycpDjmcaAoaAiTdfkG08D9JTsaNOAE+J/6iqN0TLb7/HvV5G3SODPYV9iuoLNNOlWnj6UFoat8rhpHzFcMRlcFdM/jOKU3AK7ze06O5zadjPQq5/1k2BPCaNYb9Umy00J9fSuDmvD6RQt3wL/kY3lQR1yEm5amIUQ2j0ujw9F3wc8A1lmRHw5plVhhkJaLZ/+nMmYEYpv4Cf9Kh7z3lu0fL0dlEf+uWLe8A42tsQOfdjekKqXD9YXNUbM9s6d27DsIfY/8c4LodqPq+Y7FiC3UO28JigNO6FEe";
		//String cert = "CIICljCCAYCgAwIBAgIEATb+cTALBgkqhkiG9w0BAQswLzELMAkGA1UEBhMCQ04xIDAeBgNVBAMMF0NISU5BIFVOSUNPTSBDTEFTUzEgQ0ExMB4XDTE2MTIyMTA5NTYzNFoXDTE2MTIyNDA5NTYzNFowJDELMAkGA1UEBhMCQ04xFTATBgNVBAMMDOS9oOWlvVJTQTExMTCBnTALBgkqhkiG9w0BAQEDgY0AMIGJAoGBAKxeaim0jaSqo2KS7KU6lHsVUPGOzC7V7yZoTNYsGzjwy/c/kQLkbU1/MeTnIXPjtkFwyWmZXL2drFbUxZ6eSPRd69OzGfG8dpqEw53wLE5BI0xTLv/wQZyMVcoXAsjX5VYa5IlYDAZhPp5IogtMT9BxtYukeP9o+dXglXEylzrDAgMBAAGjTzBNMB8GA1UdIwQYMBaAFKt7hVVYh97I/vJ2QAPvDBz6gj4vMAsGA1UdDwQEAwIHgDAdBgNVHQ4EFgQUp7Om18jYq6TrNUYiFPChtOl5Z0IwCwYJKoZIhvcNAQELA4IBAQBu+Na/ltAA1lxeG8Q2g+7XeipEwFwpFNu+YLswJqXZbFgT7UbsU5ycpDjmcaAoaAiTdfkG08D9JTsaNOAE+J/6iqN0TLb7/HvV5G3SODPYV9iuoLNNOlWnj6UFoat8rhpHzFcMRlcFdM/jOKU3AK7ze06O5zadjPQq5/1k2BPCaNYb9Umy00J9fSuDmvD6RQt3wL/kY3lQR1yEm5amIUQ2j0ujw9F3wc8A1lmRHw5plVhhkJaLZ/+nMmYEYpv4Cf9Kh7z3lu0fL0dlEf+uWLe8A42tsQOfdjekKqXD9YXNUbM9s6d27DsIfY/8c4LodqPq+Y7FiC3UO28JigNO6FEe";
		/*String[] strNum = new String[]{"1","2","3","4","5","6","7","8","9","0","+","a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z","A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
		System.out.println(strNum.length);
		StringBuffer sb = new StringBuffer();
		for (int i = 1; i <= 888; i++) {
			Random run = new  Random();
			Integer n = run.nextInt(63);
			String setNum = strNum[n];
			if(i==1){setNum.toUpperCase();}
			sb.append(setNum);
		}
		System.out.println("随机生成："+ sb.toString());
		System.out.println("原       串："+ cert);
		System.out.println(sb.length());
		System.out.println(cert.length());*/
		String cert = "MIICljCCAYCgAwIBAgIEATb+cTALBgkqhkiG9w0BAQswLzELMAkGA1UEBhMCQ04xIDAeBgNVBAMMF0NISU5BIFVOSUNPTSBDTEFTUzEgQ0ExMB4XDTE2MTIyMTA5NTYzNFoXDTE2MTIyNDA5NTYzNFowJDELMAkGA1UEBhMCQ04xFTATBgNVBAMMDOS9oOWlvVJTQTExMTCBnTALBgkqhkiG9w0BAQEDgY0AMIGJAoGBAKxeaim0jaSqo2KS7KU6lHsVUPGOzC7V7yZoTNYsGzjwy/c/kQLkbU1/MeTnIXPjtkFwyWmZXL2drFbUxZ6eSPRd69OzGfG8dpqEw53wLE5BI0xTLv/wQZyMVcoXAsjX5VYa5IlYDAZhPp5IogtMT9BxtYukeP9o+dXglXEylzrDAgMBAAGjTzBNMB8GA1UdIwQYMBaAFKt7hVVYh97I/vJ2QAPvDBz6gj4vMAsGA1UdDwQEAwIHgDAdBgNVHQ4EFgQUp7Om18jYq6TrNUYiFPChtOl5Z0IwCwYJKoZIhvcNAQELA4IBAQBu+Na/ltAA1lxeG8Q2g+7XeipEwFwpFNu+YLswJqXZbFgT7UbsU5ycpDjmcaAoaAiTdfkG08D9JTsaNOAE+J/6iqN0TLb7/HvV5G3SODPYV9iuoLNNOlWnj6UFoat8rhpHzFcMRlcFdM/jOKU3AK7ze06O5zadjPQq5/1k2BPCaNYb9Umy00J9fSuDmvD6RQt3wL/kY3lQR1yEm5amIUQ2j0ujw9F3wc8A1lmRHw5plVhhkJaLZ/+nMmYEYpv4Cf9Kh7z3lu0fL0dlEf+uWLe8A42tsQOfdjekKqXD9YXNUbM9s6d27DsIfY/8c4LodqPq+Y7FiC3UO28JigNO6FEe";
		String str = "interfaceVersion=1.0.0.1&tranType=DF02&merNo=301100710007122&orderNo=20171110173253&amount=10&orderDate=20171110&reqTime=20171110173253&transRst=1&transDis=测试&payTime=20171110173309&payJnlno=201711100000450016&bizCode=017&payeeAcc=6222020409031144804&woType=4&payeeBankCode=ICBC&payeeName=刘强强&payeeBankBranch=&merExtend=&signType=RSA_SHA256&signMsg=UmDBbZHbaYtXd6IGAg4dKJ7nnE00iw2nfOuQPC8EGclmOagrK8wwLTCBRuZIM/MMnR0Y+iTldLnSAr6HSDAbs1DpQy16i/OI9ovhDoflDFRN8BSlUctm+XzU3LDEuGjK0hCCF3xVhXvQBNSNWT6QUqyXjwRL7pXm7Ec3FTqBgAs=&identityInfo=";
		
		Map<String,String> map = rep2Map(str,"\\&","\\=");
		Boolean re = UniPaySignUtilsCer.merVerify(map, "RSA_SHA256", map.get("signMsg"), cert,true);
		System.out.println(re);
	}

	
	private static void merSignTest() {
		//String str = "identityInfo=, merExtend=, bizCode=017, payeeBankCode=, interfaceVersion=1.0.0.1, reqSysNo=, reqTime=20170906113125, tranType=DF02, payeeName=李雪飞, reqIp=, merNo=301100710007122, payeeAttribution=, signType=RSA_SHA256, callbackUrl=http://ttslb.cs.12366.com/api/unicomPayResult/sourceTransfer.do, payeeAcc=6217920603939203, amount=1, orderNo=LS20170906113125206, woType=4, merAccType=, payeeBankBran=, orderDate=20170906, ftpProtocalType=, payeeUnionBankNo=";
		//System.out.println("签名串："+UniPaySignUtilsCer.merSign(rep2Map(str,"\\,S|, ","\\="), "RSA_SHA256", "301100710007122", true));;
		//String str = "amount=1|bizCode=017|callbackUrl=http://ttslb.cs.12366.com/api/unicomPayResult/sourceTransfer.do|interfaceVersion=1.0.0.1|merNo=301100710007122|orderDate=20170906|orderNo=LS20170906111436253|payeeAcc=6217920603939203|payeeName=李雪飞|reqTime=20170906111436|serviceId=6000|signType=RSA_SHA256|tranType=DF02|woType=4";
		
		//String str = "charSet:UTF-8,merNo:301100710007142,merRefundNo:0800170921235853198037961,orderDate:20170921,orderNo:921235954877,signType:RSA_SHA256";
		//System.out.println("签名串："+UniPaySignUtilsCer.merSign(rep2Map(str,"\\,","\\:"), "RSA_SHA256", "301100710007142", false));;

		
		String str = "amount=10|bizCode=017|callbackUrl=http://www.baidu.com|interfaceVersion=1.0.0.1|merNo=301100710007122|orderDate=20171110|orderNo=20171110134408|payeeAcc=6228410082274725|payeeBankCode=ICBC|payeeName=张志国|reqTime=20171110134408|signType=RSA_SHA256|tranType=DF02|woType=4";
		//System.out.println("签名串："+UniPaySignUtilsCer.merSign(rep2Map(str,"\\|,","\\="), "RSA_SHA256", "301100710007122", true));;

	}
	
	/**
	 * 
	 * @param str
	 * @param dataSplit 数据分隔正则表达式  如：", " 表达式为："\\,S|,"
	 * @param valueSplit  值分隔正则表达式  如: "="  表达式为： "\\="
	 * @return
	 */
	public static Map<String, String> rep2Map(String str,String dataSplit,String valueSplit) {
		Map<String,String> reqMap = new HashMap<String, String>();
		String[] parameters = str.split(dataSplit);
		for (int i = 0; i < parameters.length; i++) {
			String[] values = parameters[i].split(valueSplit,2);
			String key = values[0];
			String value = values.length>1?values[1]:"";
			reqMap.put(key, value);
		}
		return reqMap;
	}
}
