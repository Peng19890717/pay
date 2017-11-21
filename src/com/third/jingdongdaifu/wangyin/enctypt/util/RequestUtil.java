package com.third.jingdongdaifu.wangyin.enctypt.util;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import util.JWebConstant;

import com.PayConstant;
import com.alibaba.fastjson.JSON;
import com.pay.merchantinterface.service.JDPayService;
import com.third.jingdongdaifu.wangyin.util.CodeConst;



public class RequestUtil {
	private static final Log log = LogFactory.getLog(RequestUtil.class);
	//RSA加密使用
	private static final String passWord=PayConstant.PAY_CONFIG.get("JD_DF_PASSWROD");//秘钥文件密码
	private static final String pri="jd.pfx";//秘钥文件名（该文件包含公钥和私钥）
	private static final String pub="jd.cer";//代付证书文件名
	//签名使用
	private static final String singKey=PayConstant.PAY_CONFIG.get("JD_DF_KEY");//签名key，测试环境测试的都是test，生产上一个会员对应一个key
	/**
	 * 请求demo(带证书加密) 外部商户https参考使用
	 * @throws Exception 
	 */
	public String tradeRequestSSL(Map<String, String> paramMap,String url,String encryptType) throws Exception{
		//请求数据，http请求时将此map中的数据放入Form表单中
		Map<String,String> requestMap = enctyptData(paramMap,encryptType);
		log.info("京东代付请求参数:"+requestMap);
		WyHttpClientUtil util  = new WyHttpClientUtil();
		String content = util.postSSLUrlWithParams(url,requestMap);
		log.info("京东代付响应参数:"+content);
		return content;
	
	}
	
	/**
	 * 请求demo 内部http请求参考使用
	 * @param paramMap
	 * @param url
	 * @return
	 * @throws Exception
	 */
	public String tradeRequest(Map<String, String> paramMap,String url) throws Exception{
		Map<String,String> requestMap = null;
		String payTool=paramMap.get("pay_tool");
		if("TRAN".equals(payTool)){//支付工具是代付（银行卡）的需做数据加密
			requestMap = enctyptData(paramMap,Contants.encryptType_DES);
			System.out.println("------>encrypt_data="+requestMap.get("encrypt_data"));
		}else if("XJK".equals(payTool)||"ACCT".equals(payTool)){//余额和小金库的支付工具不用加密，只做签名
			requestMap=enctyptData(paramMap,null);
		}else{
			requestMap=enctyptData(paramMap,null);//其他不用加密，只做签名
		}
		 //http请求
		WyHttpClientUtil util  = new WyHttpClientUtil();
		String content = util.postUrlWithParams(url,requestMap);
		System.out.println("------>response_data="+content);
		return content;
	}
	
	/**
	 * 加密数据  RSA或3DES
	 * @param paramMap
	 * @param encrtyptType
	 * @return
	 * @throws Exception
	 */
	private Map<String,String> enctyptData(Map<String, String> paramMap,String encrtyptType) throws Exception{
		Map<String,String> requestMap = new HashMap<String,String>();
		//签名
		String sign = EnctyptUtil.sign(paramMap, singKey);
		if(StringUtils.isEmpty(encrtyptType)){
			paramMap.put("sign_type",Contants.singType);
			paramMap.put("sign_data",sign);//设置签名数据
			requestMap = paramMap;
		}else{
			requestMap.put("sign_type",Contants.singType);//签名类型
			requestMap.put("encrypt_type",encrtyptType);//加密类型
			requestMap.put("customer_no",paramMap.get("customer_no"));//提交者会员号
			requestMap.put("sign_data",sign);//设置签名数据
			if("RSA".equals(encrtyptType)){
				//测试路径，请更改实际文件路径
				String rootPath = JWebConstant.APP_PATH+JWebConstant.PATH_DIV+"config"+JWebConstant.PATH_DIV;//"D:/myeclipse10/EnctyptDemo/src/rsa/";
				//数据加密
				String res= EnctyptUtil.signEnvelop(paramMap, passWord, rootPath+pri, rootPath+pub);
				requestMap.put("encrypt_data",res);//设置加密数据
			}
		}
		return requestMap;			
	}
	
	
	
	
	
	/**
	 * 请求返回数据后处理
	 * @param data
	 * @return 返回验证签名通过后的数据
	 * @throws UnsupportedEncodingException
	 */
	public Map<String,String> verifySingReturnData(String data) throws UnsupportedEncodingException{

		Map<String,String> map = new HashMap<String,String>();
		if(null!=data && !"".equals(data)){
			try{
				map = JSON.parseObject(data,Map.class);
			}catch(Exception e){
				return map;
			}
			//必须签名验证，确保数据一致性
			map=EnctyptUtil.verifySign(map,singKey);
		}else{
			map.put("response_code", CodeConst.RETURN_PARAM_NULL);
			map.put("response_message", "返回数据为空");
		}
		return map;
	}
	
	/**
	 * 签名验证demo（通知接口接收到数据后需要验签）
	 * @param data
	 * @return 返回验证签名通过后的数据
	 * @throws UnsupportedEncodingException
	 */
	public Map<String,String> verifySingNotify(String data) throws UnsupportedEncodingException{
		
		String paramSplitStr[]=data.split("\\&");
        Map<String, String> mapStr = new HashMap<String, String>();
        for(int i=0;i<paramSplitStr.length;i++){
            mapStr.put(paramSplitStr[i].substring(0,paramSplitStr[i].indexOf("=")),paramSplitStr[i].substring(paramSplitStr[i].indexOf("=")+1,paramSplitStr[i].length()));
        }
        //必须签名验证，确保数据一致性
        Map<String,String> map=EnctyptUtil.verifySign(mapStr,singKey);
		return map;
	
	}

}
