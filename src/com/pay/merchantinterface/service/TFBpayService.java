package com.pay.merchantinterface.service;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import util.DataTransUtil;
import util.PayUtil;
import com.PayConstant;
import com.pay.cardbin.dao.PayCardBin;
import com.pay.cardbin.service.PayCardBinService;
import com.pay.order.dao.PayOrder;
import com.pay.order.dao.PayProductOrder;
import com.pay.order.dao.PayReceiveAndPay;
import com.pay.order.dao.PayReceiveAndPayDAO;
import com.pay.order.service.PayOrderService;
import com.third.tfb.util.MD5Utils;
import com.third.tfb.util.RSAUtils;
import com.third.tfb.util.RequestUtil;
import com.third.tfb.util.RequestUtils;

/**
 *天付宝接口服务类
 * @author Administrator
 *
 */
public class TFBpayService {
	
		private static final Log log = LogFactory.getLog(TFBpayService.class);
		public static Map<String,String> BANK_MAP_B2C = new HashMap<String,String>();//b2c
		static{
			BANK_MAP_B2C.put("PSBC","1006");//邮政储蓄银行
			BANK_MAP_B2C.put("CMBC","1010");//民生银行
			BANK_MAP_B2C.put("BCCB","1016");//北京银行
			BANK_MAP_B2C.put("BOS","1025");//上海银行
			BANK_MAP_B2C.put("CMB","1012");//招商银行
			BANK_MAP_B2C.put("CNCB","1007");//中信银行
			BANK_MAP_B2C.put("SPDB","1014");//浦发银行
			BANK_MAP_B2C.put("CIB","1013");//兴业银行
			BANK_MAP_B2C.put("HXB","1009");//华夏银行
			BANK_MAP_B2C.put("ABC","1002");//农业银行
			BANK_MAP_B2C.put("GDB","1017");//广发银行
			BANK_MAP_B2C.put("ICBC","1001");//工商银行
			BANK_MAP_B2C.put("BOC","1003");//中国银行
			BANK_MAP_B2C.put("BOCOM","1005");//交通银行
			BANK_MAP_B2C.put("CCB","1004");//建设银行
			BANK_MAP_B2C.put("PAB","1011");//平安银行
			BANK_MAP_B2C.put("CEB","1008");//光大银行
		}
	/**
	 * 收银台下单
	 * @param request
	 * @param prdOrder
	 * @param payOrder
	 * @throws Exception
	 */
	public String pay(HttpServletRequest request,PayProductOrder prdOrder,PayOrder payOrder){
		try{
			payOrder.payChannel = PayConstant.PAY_CONFIG.get("PAY_CHANNEL_TFB"); // 支付渠道
			payOrder.bankcod = request.getParameter("banks"); // 银行代码
			payOrder.bankCardType = String.valueOf(Long.parseLong(request.getParameter("bankCardType")));
			TreeMap<String, String> paramsMap = new TreeMap<String, String>();
			paramsMap.put("spid", PayConstant.PAY_CONFIG.get("TFB_WG_MERNO"));// 商户/平台在国采注册的账号。国采维度唯一，固定长度10位,1800212300
			paramsMap.put("sp_userid", payOrder.merno);//用户号，自定义。
			paramsMap.put("spbillno", payOrder.payordno);//订单号。
			paramsMap.put("money", String.valueOf(payOrder.txamt));//单位分。
			paramsMap.put("cur_type", "1");//币种人民币。
			paramsMap.put("notify_url",PayConstant.PAY_CONFIG.get("TFB_WG_NOTIFY_URL"));
			paramsMap.put("return_url", prdOrder.returl==null||prdOrder.returl.length()==0?"#":prdOrder.returl);
			paramsMap.put("memo", "payOnline");//商品名称。
			paramsMap.put("card_type","1");
			paramsMap.put("bank_segment",BANK_MAP_B2C.get(payOrder.bankcod));//银行编码
			if("0".equals(payOrder.bankCardType)){
				paramsMap.put("user_type", "1");
			}
			if("1".equals(payOrder.bankCardType)){
				paramsMap.put("user_type", "2");
			}
			paramsMap.put("channel", "1");
			paramsMap.put("encode_type", "MD5");
			String key = PayConstant.PAY_CONFIG.get("TFB_WG_MD5_KEY");
			String rsa_public_key=PayConstant.PAY_CONFIG.get("TFB_WG_RSA_PUBLIC_KEY");
			//拼接签名原串
			String paramSrc =RequestUtils. getParamSrc(paramsMap);
			//生成签名
			String sign = MD5Utils.sign(paramSrc,key,"utf-8");
			//rsa加密原串
			String encryptSrc = paramSrc + "&sign=" + sign;//加密原串
			log.info("天付宝网银收银台请求明文:"+encryptSrc);
			//rsa密串
			String cipherData = RSAUtils.encrypt(encryptSrc,"utf-8",rsa_public_key);
			new PayOrderService().updateOrderForBanks(payOrder);
			return cipherData;
		} catch (Exception e){
			e.printStackTrace();
			return null;
		}
	}
	/**
	 * 商户接口下单
	 * @param request
	 * @param payRequest
	 * @param prdOrder
	 * @param payOrder
	 * @throws Exception
	 */
	public String pay(HttpServletRequest request,PayRequest payRequest,PayProductOrder prdOrder,PayOrder payOrder) {
		try{
			payOrder.payChannel = PayConstant.PAY_CONFIG.get("PAY_CHANNEL_TFB"); // 支付渠道
			payOrder.bankcod = payRequest.bankId;  //银行代码
			payOrder.bankCardType = payRequest.accountType;
			TreeMap<String, String> paramsMap = new TreeMap<String, String>();
			paramsMap.put("spid", PayConstant.PAY_CONFIG.get("TFB_WG_MERNO"));// 商户/平台在国采注册的账号。国采维度唯一，固定长度10位,1800212300
			paramsMap.put("sp_userid", payOrder.merno);//用户号，自定义。
			paramsMap.put("spbillno", payOrder.payordno);//订单号。
			paramsMap.put("money", String.valueOf(payOrder.txamt));//单位分。
			paramsMap.put("cur_type", "1");//币种人民币。
			paramsMap.put("notify_url",PayConstant.PAY_CONFIG.get("TFB_WG_NOTIFY_URL"));
			paramsMap.put("return_url", prdOrder.returl==null||prdOrder.returl.length()==0?"#":prdOrder.returl);
			paramsMap.put("memo", "payOnline");//商品名称。
			paramsMap.put("card_type","1");
			paramsMap.put("bank_segment",BANK_MAP_B2C.get(payOrder.bankcod));//银行编码
			if("0".equals(payOrder.bankCardType)){
				paramsMap.put("user_type", "1");
			}
			if("1".equals(payOrder.bankCardType)){
				paramsMap.put("user_type", "2");
			}
			paramsMap.put("channel", "1");
			paramsMap.put("encode_type", "MD5");
			String key = PayConstant.PAY_CONFIG.get("TFB_WG_MD5_KEY");
			String rsa_public_key=PayConstant.PAY_CONFIG.get("TFB_WG_RSA_PUBLIC_KEY");
			//拼接签名原串
			String paramSrc =RequestUtils. getParamSrc(paramsMap);
			//生成签名
			String sign = MD5Utils.sign(paramSrc,key,"utf-8");
			//rsa加密原串
			String encryptSrc = paramSrc + "&sign=" + sign;//加密原串
			log.info("天付宝网银支付商户接口请求明文:"+encryptSrc);
			//rsa密串
			String cipherData = RSAUtils.encrypt(encryptSrc,"utf-8",rsa_public_key);
			new PayOrderService().updateOrderForBanks(payOrder);
			return cipherData;
		} catch (Exception e){
			e.printStackTrace();
			return null;
		}
	}
	/**
	 * 渠道补单
	 * @param payordno 订单号
	 * @throws Exception 异常捕获
	 */
	public void channelQuery(PayOrder payOrder) throws Exception{
		try{
			TreeMap<String, String> paramsMap = new TreeMap<String, String>();
			paramsMap.put("spid", PayConstant.PAY_CONFIG.get("TFB_WG_MERNO"));
			paramsMap.put("spbillno", payOrder.payordno);//订单号。
			paramsMap.put("listid", "");
			paramsMap.put("channel", "1");
			paramsMap.put("encode_type", "MD5");
			String key = PayConstant.PAY_CONFIG.get("TFB_WG_MD5_KEY");
			String rsa_public_key=PayConstant.PAY_CONFIG.get("TFB_WG_RSA_PUBLIC_KEY");
			String rsa_private_key=PayConstant.PAY_CONFIG.get("TFB_WG_RSA_PRIVATE_KEY");
			//拼接签名原串
			String paramSrc =RequestUtils. getParamSrc(paramsMap);
			//生成签名
			String sign = MD5Utils.sign(paramSrc,key,"utf-8");
			//rsa加密原串
			String encryptSrc = paramSrc + "&sign=" + sign;//加密原串
			log.info("天付宝网银查询请求明文:"+encryptSrc);
			//rsa密串
			String cipherData = RSAUtils.encrypt(encryptSrc,"utf-8",rsa_public_key);
			String payResult = new String(new DataTransUtil().doPost(PayConstant.PAY_CONFIG.get("TFB_WG_QUERY_URL"),("cipher_data="+URLEncoder.encode(cipherData, "UTF-8")).getBytes("utf-8")),"GBK");
			log.info("天付宝网银查询响应报文:"+payResult);
			if(RequestUtils.getXmlElement(payResult, "retcode").equals("00")) {
				//获得服务器返回的加密数据
				String cipherResponseData = RequestUtils.getXmlElement(payResult, "cipher_data");
				//对服务器返回的加密数据进行rsa解密
				String responseData = RSAUtils.decrypt(cipherResponseData,rsa_private_key,"GBK");
				log.info("天付宝网银查询响应业务报文:"+responseData);
				//分解解密后的字符串
				HashMap<String, String> map = RequestUtils.parseString(responseData);
				if("1".equals(map.get("result"))){
					payOrder.ordstatus="01";//支付成功
					 new NotifyInterface().notifyMer(payOrder);//支付成功
				}else if("3".equals(map.get("result"))){
					payOrder.ordstatus="02";
					new NotifyInterface().notifyMer(payOrder);
				}
			}
		} catch (Exception e){
			e.printStackTrace();
			throw e;
		}
	}
	/**
	 * 单笔代付
	 * @param payRequest
	 * @throws Exception
	 */
	 public void receivePaySingle(PayRequest payRequest) throws Exception{
    	try {
    		PayReceiveAndPay rp = payRequest.receiveAndPaySingle;
    		if("1".equals(payRequest.receivePayType)){//代付业务。
    			TreeMap<String, String> paramsMap = new TreeMap<String, String>();
    			paramsMap.put("version","1.0");    //固定填1.0
    			paramsMap.put("spid",PayConstant.PAY_CONFIG.get("TFB_WG_MERNO"));//填写国采分配的商户号
    			String sp_serialno = getUniqueIdentify();
    			paramsMap.put("sp_serialno",sp_serialno); //商户交易单号，商户保证其在本系统唯一,每次交易入库需要修改订单号
    			paramsMap.put("sp_reqtime", new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));   //系统发送时间，14位固定长度
    			paramsMap.put("tran_amt", payRequest.amount);    //交易金额，单位为分，不带小数点
    			paramsMap.put("cur_type", "1");     
    			paramsMap.put("pay_type",PayConstant.PAY_CONFIG.get("TFB_DF_PAY_TYPE")==null?"1":PayConstant.PAY_CONFIG.get("TFB_DF_PAY_TYPE"));//普通余额支付填 1；垫资代付填3
    			paramsMap.put("acct_name", payRequest.accName); 
    			paramsMap.put("acct_id", payRequest.accNo);   //收款人账号
    			paramsMap.put("acct_type", "2");   //0 借记卡， 1 贷记卡， 2 对公账户
    			paramsMap.put("mobile", payRequest.tel);
    			PayCardBin cardBin = PayCardBinService.getCardBinByCardNo(payRequest.accNo);
    			paramsMap.put("bank_name", new String(cardBin.bankName.getBytes(),"UTF-8"));
    			paramsMap.put("bank_name", cardBin.bankName);
    			paramsMap.put("business_type", "20101");
    			paramsMap.put("memo","payonlylien");
    			String params=RequestUtil.querySign(paramsMap);
    			log.info("天付宝代付请求参数:"+params);
    			String respData = RequestUtil.sendRequst(PayConstant.PAY_CONFIG.get("TFB_DF_PAY_URL"), params);
    			log.info("天付宝代付响应参数:"+respData);
	    		String retcode = RequestUtil.parseXml(respData,"retcode");
	    		if("00".equals(retcode)){
	    			String cipher_data = RequestUtil.parseXml(respData,"cipher_data");
	    			String responseData = RequestUtil.decryptResponseData(cipher_data);//业务数据解密
		    		log.info("天付宝代付响应业务参数:"+responseData);
		    		Map <String,String> resToMap=RequestUtil.stringToMap(responseData);
		    		String sign = responseData.substring(responseData.indexOf("sign=") + 5, responseData.length());
		    		String source = responseData.substring(0, responseData.lastIndexOf("&sign"));
		    		if (RequestUtil.verify(source, sign)) {
		    			if("1".equals(resToMap.get("serialno_state"))){
	    		        	rp.status="1";
	    					rp.respCode="000";
	    					rp.errorMsg="交易成功";
	    					rp.channelTranNo=sp_serialno;
	    					try{new PayReceiveAndPayDAO().updatePayReceiveAndPayById(rp);} catch(Exception e){}
	    					List<PayReceiveAndPay> list = new ArrayList<PayReceiveAndPay>();
	    					list.add(rp);
	    					new PayInterfaceOtherService().receivePayNotify(payRequest,list);
		    			} else if("2".equals(resToMap.get("serialno_state"))){
		    				rp.status="0";
	    					rp.errorMsg = "处理中";
	    					rp.channelTranNo=sp_serialno;
	    		        	try{new PayReceiveAndPayDAO().updatePayReceiveAndPayById(rp);} catch(Exception e){}
	    		        	TFBDFquery query = new TFBDFquery(rp,payRequest);
	    		        	query.start();
		    			} else if("3".equals(resToMap.get("serialno_state"))){
		    				rp.status="2";
	    					rp.errorMsg = "提交失败";
	    					rp.channelTranNo=sp_serialno;
	    		        	try{new PayReceiveAndPayDAO().updatePayReceiveAndPayById(rp);} catch(Exception e){}
		    			}
		    		} else new Exception("代付提交渠道验签失败");
	    		}else{//代付提交失败。
	    			String retmsg = RequestUtil.parseXml(respData,"retmsg");
	    			rp.status="2";
					rp.errorMsg = retmsg;
					rp.channelTranNo=sp_serialno;
		        	try{new PayReceiveAndPayDAO().updatePayReceiveAndPayById(rp);} catch(Exception e){}
	    		}
    		}
		} catch (Exception e) {
			e.printStackTrace();
		}
	 }
	    	
    /**
     * 代收付查询(单笔)
     * @param request
     * @return
     * @throws Exception
     */
    public boolean receivePaySingleQuery(PayRequest request,PayReceiveAndPay rp) throws Exception{
		try {
			TreeMap<String, String> paramsMap = new TreeMap<String, String>();
			paramsMap.put("version", "1.0");
			paramsMap.put("spid",PayConstant.PAY_CONFIG.get("TFB_WG_MERNO"));
			paramsMap.put("charset", "UTF-8");
			paramsMap.put("sp_serialno", rp.channelTranNo);
			paramsMap.put("sp_reqtime", new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));	
			String params=RequestUtil.querySign(paramsMap);
			log.info("天付宝代付查询请求参数:"+params);
			String respData =new String(new DataTransUtil().doPost(PayConstant.PAY_CONFIG.get("TFB_DF_QUERY_URL"), params.getBytes("utf-8")),"utf-8");
			log.info("天付宝代付查询接口响应:"+respData);
			String retcode = RequestUtil.parseXml(respData,"retcode");
			if("00".equals(retcode)) {
				String cipher_data = RequestUtil.parseXml(respData,"cipher_data");
				String responseData = RequestUtil.decryptResponseData(cipher_data);//业务数据解密
	    		log.info("天付宝代付响应业务参数:"+responseData);
	    		Map <String,String> resToMap=RequestUtil.stringToMap(responseData);
	    		if("1".equals(resToMap.get("serialno_state"))){
					request.setRespCode("000");
					request.receiveAndPaySingle.setRespCode("000");
					request.receivePayRes = "0";
					request.respDesc="交易成功";
					rp.errorMsg = "交易成功";
					return true;
				} else if("3".equals(resToMap.get("serialno_state"))){
					request.setRespCode("-1");
					request.receiveAndPaySingle.setRespCode("000");
					request.receivePayRes = "-1";
					request.respDesc="交易失败";
					rp.errorMsg = request.respDesc;
					return false;
				} else{
					request.setRespCode("0");//处理中
					request.receiveAndPaySingle.setRespCode("000");
					request.receivePayRes = "-1";
					request.respDesc="处理中";
					rp.errorMsg = request.respDesc;
					return false;
				}
			}else throw new Exception(RequestUtil.parseXml(respData,"retmsg").toString());
		} catch (Exception e) {
				e.printStackTrace();
				request.setRespCode("-1");
				rp.setRespCode(request.respCode);
				request.receivePayRes = "-1";
				request.respDesc=e.getMessage();
				rp.errorMsg = e.getMessage();
				return false;
		}
    }
	// 唯一值处理-------------------start
//	static byte [] cmb = {'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z',
//		'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'};
	static byte [] cmb = {'0','1','2','3','4','5','6','7','8','9'};
	public static String getRadixOf62(long src){
		StringBuffer sb = new StringBuffer();		
		while(src > 0){
			int mod = (int) (src%cmb.length);
			sb.append(new String(new byte[]{cmb[mod]}));
			src = src/cmb.length;
		}
		return reverse(sb.toString());
	}
	public static String reverse(String str){   
	    int n=str.length();   
	    char []chars=new char[n];   
	    str.getChars(0, n, chars, 0);//获得了char[]可操作的数组.   
	       
	    int length=chars.length;   
	    StringBuffer sbStr=new StringBuffer();   
	    for(int i=0;i<length;i++){   
	        sbStr.append(chars[length-i-1]+"");//用StringBuffer将其逆转.   
	    }   
	    return sbStr.toString();//转换为String   
	}
	//(时间戳的62进制前缀 + 1开始的累加值，直到累加值大于countPerSecondS，然后重取得时间戳，累加值从1开始)
	public static String curTimeStr = getRadixOf62(System.currentTimeMillis());
	public static long identifySeed = 1;
	public static String countPerSecondS = "9999999999999";
	public static long countPerSecond = new Long(countPerSecondS);
	public static synchronized String getUniqueIdentify() {
		if (identifySeed >= countPerSecond) {
			identifySeed = 1;
			curTimeStr = getRadixOf62(System.currentTimeMillis());
		}
		String tmp = String.valueOf(identifySeed);
		int len = tmp.length();
		for(int i = 0; i<countPerSecondS.length()-len;i++)tmp = "0"+tmp;
		identifySeed ++;
		return curTimeStr+getRadixOf62(Long.parseLong("1"+tmp));
	}
	// 唯一值处理-------------------end
	public static void main(String [] args){
		System.out.println(getUniqueIdentify());
	}
}
class TFBDFquery extends Thread{
	private static final Log log = LogFactory.getLog(TFBDFquery.class);
	public PayRequest payRequest= new PayRequest();
	PayReceiveAndPay rp = new PayReceiveAndPay();
	public TFBDFquery(PayReceiveAndPay rp,PayRequest payRequest) {
		this.payRequest=payRequest;
		this.rp=rp;
	}
	@Override
	public void run() {
		try {
			for(int i=0; i<CJPayService.CJ_RECEIVE_PAY_SEARCH_TIME_FREQUENCY.length; i++){
				try {if(query())break;} catch (Exception e) {}
				log.info("天付宝代付查询第"+(i+1)+"次,等待时间"+CJPayService.CJ_RECEIVE_PAY_SEARCH_TIME_FREQUENCY[i]+"秒");
				try {Thread.sleep(CJPayService.CJ_RECEIVE_PAY_SEARCH_TIME_FREQUENCY[i]*1000);} catch (Exception e) {}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public boolean query(){
		try {
			TreeMap<String, String> paramsMap = new TreeMap<String, String>();
			paramsMap.put("version", "1.0");
			paramsMap.put("spid",PayConstant.PAY_CONFIG.get("TFB_WG_MERNO"));
			paramsMap.put("charset", "UTF-8");
			paramsMap.put("sp_serialno", rp.channelTranNo);
			paramsMap.put("sp_reqtime", new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));	
			String params=RequestUtil.querySign(paramsMap);
			log.info("天付宝代付查询请求参数:"+paramsMap);
			String respData =new String(new DataTransUtil().doPost(PayConstant.PAY_CONFIG.get("TFB_DF_QUERY_URL"), params.getBytes("utf-8")),"utf-8");
			log.info("天付宝代付查询接口响应:"+respData);
			String retcode = RequestUtil.parseXml(respData,"retcode");
			if("00".equals(retcode)){
				String cipher_data = RequestUtil.parseXml(respData,"cipher_data");
				String responseData = RequestUtil.decryptResponseData(cipher_data);//业务数据解密
	    		log.info("天付宝代付响应业务参数:"+responseData);
	    		Map <String,String> resToMap=RequestUtil.stringToMap(responseData);
				if("1".equals(resToMap.get("serialno_state"))){
					rp.status="1";
					rp.respCode="000";
					rp.errorMsg="交易成功";
					try{new PayReceiveAndPayDAO().updatePayReceiveAndPayById(rp);} catch(Exception e){}
					List<PayReceiveAndPay> list = new ArrayList<PayReceiveAndPay>();
					list.add(rp);
					new PayInterfaceOtherService().receivePayNotify(payRequest,list);
					return true;
				} else if("3".equals(resToMap.get("serialno_state"))){
					rp.status="2";
					rp.respCode="-1";
					rp.errorMsg="交易失败";
					try{new PayReceiveAndPayDAO().updatePayReceiveAndPayById(rp);} catch(Exception e){}
//					List<PayReceiveAndPay> list = new ArrayList<PayReceiveAndPay>();
//					list.add(rp);
//					new PayInterfaceOtherService().receivePayNotify(payRequest,list);
					return true;
				} else{
					return false;
				}
			} else throw new Exception(RequestUtil.parseXml(respData,"retmsg").toString());
		} catch (Exception e) {
			e.printStackTrace();
			return true;
		}
	}
}

