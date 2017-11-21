package com.third.jyt.mockClientMsgBase;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.junit.Assert;
import org.xml.sax.InputSource;

import util.JWebConstant;

import com.PayConstant;
import com.third.jyt.baseUtil.AppException;
import com.third.jyt.baseUtil.CryptoUtils;
import com.third.jyt.baseUtil.DESHelper;
import com.third.jyt.baseUtil.DateTimeUtils;
import com.third.jyt.baseUtil.HttpClient431Util;
import com.third.jyt.baseUtil.RSAHelper;
import com.third.jyt.baseUtil.StringUtil;

public class MockClientMsgBase_RNpay {
	
	private static final Log log = LogFactory.getLog(MockClientMsgBase_RNpay.class);
	//测试环境    外部访问地址    						
	public static String APP_SERVER_URL = PayConstant.PAY_CONFIG.get("JYT_PAY_URL");
	public static String pfxPath = JWebConstant.APP_PATH+JWebConstant.PATH_DIV+"config"+JWebConstant.PATH_DIV+PayConstant.PAY_CONFIG.get("JYT_PRI_KEY");
	public static String certPath = JWebConstant.APP_PATH+JWebConstant.PATH_DIV+"config"+JWebConstant.PATH_DIV+PayConstant.PAY_CONFIG.get("JYT_PUB_KEY");
	private static String certPasswd = PayConstant.PAY_CONFIG.get("JYT_PRI_KEY_PASSWORD");// 证书密码
	
	//测试环境测试商户
	public static String MERCHANT_ID =  PayConstant.PAY_CONFIG.get("JYT_MERCHANT_ID");	//替换为自己的商户号
	
	public static String RESP_MSG_PARAM_SEPARATOR = "&";
	
	/**返回报文merchant_id字段前缀*/
	public static String RESP_MSG_PARAM_PREFIX_MERCHANT_ID = "merchant_id=";
	
	/**返回报文xml_enc字段前缀*/
	public static String RESP_MSG_PARAM_PREFIX_XML_ENC = "xml_enc=";
	/**返回报文xml_enc字段前缀*/
	public static String RESP_MSG_PARAM_PREFIX_KEY_ENC = "key_enc=";
	
	/**返回报文sign字段前缀*/
	public static String RESP_MSG_PARAM_PREFIX_SIGN = "sign=";
	
	public RSAHelper rsaHelper = new RSAHelper();
	{
		try {
				PrivateKey prikey = rsaHelper.getPrivateKeyFromPfx(pfxPath, certPasswd);// 获取证书私钥
				PublicKey pk = rsaHelper.getPublicKeyFromCer(certPath);
				rsaHelper.initKey(prikey, pk);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	/**
	 * 生成实名支付交易流水号
	 * @return
	 */
	public static String getOrderId() {
	    return "D" + MERCHANT_ID + RandomStringUtils.randomNumeric(17);
	}
	
	/**
	 * 获取交易流水号
	 * <p> Create Date: 2014-5-10 </p>
	 * @return
	 */
	public String getTranFlow(){
		return MERCHANT_ID+RandomStringUtils.randomNumeric(18);
	}
	
	
	/**
	 * 获得报文头字符串
	 * <p> Create Date: 2014-5-10 </p>
	 * @param tranCode
	 * @return
	 */
	public String getMsgHeadXml(String tranCode){
		StringBuffer headXml = new StringBuffer();
		headXml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?><message><head><version>1.0.0</version>");
		headXml.append("<tran_type>01</tran_type>")
			   .append("<merchant_id>").append(MERCHANT_ID).append("</merchant_id>");
		headXml.append("<tran_date>").append(DateTimeUtils.getNowDateStr(DateTimeUtils.DATE_FORMAT_YYYYMMDD))
		       .append("</tran_date>");
		headXml.append("<tran_time>").append(DateTimeUtils.getNowDateStr(DateTimeUtils.DATETIME_FORMAT_HHMMSS))
		       .append("</tran_time><tran_flowid>").append(getTranFlow())
		       .append("</tran_flowid><tran_code>").append(tranCode).append("</tran_code>");
		headXml.append("</head>");
		
		return headXml.toString();
	}
	
	public String sendMsg(String xml,String sign) throws Exception{
		log.info("金运通上送报文："+xml);
		byte[] des_key = DESHelper.generateDesKey() ;
		Map<String,String> paramMap = new HashMap<String,String>();
		paramMap.put("merchant_id", MERCHANT_ID);
		paramMap.put("xml_enc", encryptXml(xml,des_key));
		paramMap.put("key_enc", encryptKey(des_key));
		paramMap.put("sign", sign);
		// 获取执行结果
		String res = HttpClient431Util.doPost(paramMap, APP_SERVER_URL);
		if(res  == null){
			log.error("服务器连接失败");
			throw new AppException("测试异常");
		}else{
			log.info("连接服务器成功,返回结果"+res);
		}
		String []respMsg = res.split(RESP_MSG_PARAM_SEPARATOR);
		String merchantId = respMsg[0].substring(RESP_MSG_PARAM_PREFIX_MERCHANT_ID.length());
		String respXmlEnc = respMsg[1].substring(RESP_MSG_PARAM_PREFIX_XML_ENC.length());
		String respKeyEnc = respMsg[2].substring(RESP_MSG_PARAM_PREFIX_KEY_ENC.length());
		String respSign = respMsg[3].substring(RESP_MSG_PARAM_PREFIX_SIGN.length());
		byte respKey[] = decryptKey(respKeyEnc) ;
		String respXml = decrytXml( respXmlEnc,respKey ) ;
		Assert.assertTrue("返回报文校验失败",verifyMsgSign(respXml,respSign));
		return respXml;
	}
	public String notifyMessageToXml(String merchantId,String respXmlEnc,String respKeyEnc,String respSign)throws Exception{
		byte respKey[] = decryptKey(respKeyEnc) ;
		String respXml = decrytXml( respXmlEnc,respKey ) ;
		Assert.assertTrue("返回报文校验失败",verifyMsgSign(respXml,respSign));
		return respXml;
	}
	
	public String getMsgRespCode(String respMsg) throws Exception{
        SAXReader saxReader = new SAXReader();
		Document doc = saxReader.read(new InputSource(new StringReader(respMsg)));
		//解析报文头
		Node packageHead = doc.selectSingleNode("/message/head");
		Node respCodeNode = packageHead.selectSingleNode("resp_code");
		return respCodeNode.getText();
	}
	
	public String getMsgState(String respMsg) throws Exception{
        SAXReader saxReader = new SAXReader();
		Document doc = saxReader.read(new InputSource(new StringReader(respMsg)));
		
		//解析报文头
		Node packageHead = doc.selectSingleNode("/message/body");
		if(packageHead==null)
			return null;
		
		Node tranStateNode = packageHead.selectSingleNode("tran_state");
		if(tranStateNode==null)
			return null;
		
		return tranStateNode.getText();
	}
	public String getOrderId(String respMsg) throws Exception{
        SAXReader saxReader = new SAXReader();
		Document doc = saxReader.read(new InputSource(new StringReader(respMsg)));
		//解析报文头
		Node packageHead = doc.selectSingleNode("/message/body");
		if(packageHead==null)
			return null;
		
		Node orderIdNode = packageHead.selectSingleNode("order_id");
		if(orderIdNode==null)
			return null;
		return orderIdNode.getText();
	}
	
	private String encryptKey( byte[] key){
		
		byte[] enc_key = null ;
		try {
			enc_key = rsaHelper.encryptRSA(key, false, "UTF-8") ;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return StringUtil.bytesToHexString(enc_key) ;
	}
	
	private byte[] decryptKey(String hexkey){
		byte[] key = null ;
		byte[] enc_key = StringUtil.hexStringToBytes(hexkey) ;
		
		try {
			key = rsaHelper.decryptRSA(enc_key, false, "UTF-8") ;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return key ;
	}
	
	private String encryptXml( String xml, byte[] key){
		
		String enc_xml = CryptoUtils.desEncryptToHex(xml, key) ;
		
		return enc_xml;
	}
	
	public String decrytXml(String xml_enc, byte[] key) {
		String xml = CryptoUtils.desDecryptFromHex(xml_enc, key) ;
		return xml;
	}

	public boolean verifyMsgSign(String xml, String sign)
	{
		byte[] bsign = StringUtil.hexStringToBytes(sign) ;
		
		boolean ret = false ;
		try {
			ret = rsaHelper.verifyRSA(xml.getBytes("UTF-8"), bsign, false, "UTF-8") ;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return ret;
	}
	public String signMsg( String xml ){
		String hexSign = null ;
		
		try {
			byte[] sign = rsaHelper.signRSA(xml.getBytes("UTF-8"), false, "UTF-8") ;
			hexSign = StringUtil.bytesToHexString(sign) ;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return hexSign;
	}
	//验签
	public boolean verifySign(byte[] plainBytes, byte[] signBytes){
		boolean flag = false;
		try {
			flag = rsaHelper.verifyRSA(plainBytes, signBytes, false, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}
	
}
