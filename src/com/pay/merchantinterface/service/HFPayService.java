package com.pay.merchantinterface.service;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import net.sf.json.JSONObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import util.JWebConstant;
import util.PayUtil;
import util.Tools;
import com.PayConstant;
import com.jweb.dao.Blog;
import com.pay.cardbin.dao.PayCardBin;
import com.pay.cardbin.service.PayCardBinService;
import com.pay.merchantinterface.dao.PayReceiveAndPaySign;
import com.pay.merchantinterface.dao.PayReceiveAndPaySignDAO;
import com.pay.order.dao.PayOrder;
import com.pay.order.dao.PayProductOrder;
import com.pay.order.dao.PayReceiveAndPay;
import com.pay.order.dao.PayReceiveAndPayDAO;
import com.third.hengfeng.uitl.CAUtil;
import com.third.hengfeng.uitl.DateUtils;
import com.third.hengfeng.uitl.HttpRequestor;
import com.third.hengfeng.uitl.HttpUtils;
import com.third.hengfeng.vo.Channel;
import com.third.hengfeng.vo.Requester;
/**
 * 易联服务类
 * @author Administrator
 *
 */
public class HFPayService {
	
	
	private static final Log log = LogFactory.getLog(HFPayService.class);
	private static String pubKeyPath = JWebConstant.APP_PATH+JWebConstant.PATH_DIV+"config"+JWebConstant.PATH_DIV+PayConstant.PAY_CONFIG.get("HF_PUB_KEY");//公钥
	private static String priKeyPaht = JWebConstant.APP_PATH+JWebConstant.PATH_DIV+"config"+JWebConstant.PATH_DIV+PayConstant.PAY_CONFIG.get("HF_PRV_KEY");//商户私钥
	private static String priKeyPassWord = PayConstant.PAY_CONFIG.get("HF_PRV_PASSWROD");//商户私钥密码;
	
	public static String privateKey =PayConstant.PAY_CONFIG.get("PAY_HF_WX_PIV_KEY");
	
	//恒丰扫码收到系统提供的公钥，在收到异步回调通知或者响应报文时，进行验签使用
	public static String hfbankPublicKey = PayConstant.PAY_CONFIG.get("PAY_HF_WX_PUB_KEY");
	
	public static Map<String, String> BANK_CODE = new HashMap<String,String>();
	static {
		BANK_CODE.put("PSBC", "01000000");//PSBC//中国邮政储蓄银行
		BANK_CODE.put("ICBC", "01020000");//ICBC//中国工商银行
		BANK_CODE.put("ABC", "01030000");//ABC//中国农业银行
		BANK_CODE.put("BOC", "01040000");///BOC//中国银行
		BANK_CODE.put("CCB", "01050000");//CCB//中国建设银行
		BANK_CODE.put("BOCOM", "03010000");//BCM//交通银行
		BANK_CODE.put("CNCB", "03020000");//CITIC//中信银行
		BANK_CODE.put("CEB", "03030000");//CEB//中国光大银行
		BANK_CODE.put("HXB", "03040000");//HXB//华夏银行
		BANK_CODE.put("CMBC", "03050000");//CMBC//民生银行
		BANK_CODE.put("GDB", "03060000");//CGB//广发银行
		BANK_CODE.put("CMB", "03080000");//CMB//招商银行
		BANK_CODE.put("CIB", "03090000");//CIB//兴业银行
		BANK_CODE.put("SPDB", "03100000");//CIB//浦发银行
		BANK_CODE.put("EGBANK", "03110000");//EGB//恒丰银行
		BANK_CODE.put("ZSBC", "03160000");//ZSBC//浙商银行
		BANK_CODE.put("BOHC", "03180000");//BOHC//渤海银行
		BANK_CODE.put("BCCB", "04030000");//BCCB//北京银行
		BANK_CODE.put("PAB", "04100000");//PAB//平安银行
		BANK_CODE.put("GZCB", "04135810");//GZCB//广州银行
	}
	/**
	 * 单笔代付。
	 * @param payRequest
	 * @throws Exception
	 */
	 public void receivePaySingle(PayRequest payRequest) throws Exception{
		 try {
		 	
		 	PayReceiveAndPay rp = payRequest.receiveAndPaySingle;//已经添加的代扣记录。
		 	JSONObject jsonData = new JSONObject();//要发送的报文。
			JSONObject jsonhead = new JSONObject();//报文头信息。
			JSONObject jsonbody = new JSONObject();//报文体信息。
			// head
			jsonhead.put("merchantCode", PayConstant.PAY_CONFIG.get("HF_MERCHANTCODE"));//商户号。
			jsonhead.put("version", "1.0");//版本
			jsonhead.put("transCode", "500010");//接口编码。
			jsonhead.put("terminalType", "api");//接口类型。
			// body
			jsonbody.put("businessSeqNo", rp.id);//业务流水。
			jsonbody.put("busiTradeType", "09900");//业务代码。  09900：其他代付类型。
			jsonbody.put("payeeAcctName",rp.accountName);//收款方名称。
			jsonbody.put("payeeAcctNo", rp.accountNo);//收款方帐号。
			if("0".equals(payRequest.accountProp)){
				jsonbody.put("accountType","00" );//账户类型， 00：对私    01：对公。
			}else{
				jsonbody.put("accountType","01" );//账户类型， 00：对私    01：对公。
			}
			jsonbody.put("currency", "CNY");//币种。
			jsonbody.put("transAmount", String.format("%.2f", (Double.parseDouble(payRequest.amount))/100d));//交易金额，单位元，精确两位小数。
			jsonbody.put("idCardType", "01");//开户证件类型，01：身份证。
			jsonbody.put("idCardNo", rp.certId);//证件号码。
			jsonbody.put("phoneNo",rp.tel);//手机号。
			jsonData.put("head", jsonhead);
			jsonData.put("body", jsonbody);
			JSONObject newjson = new JSONObject();//最终要发送的报文数据。
			
			PrivateKey privKey = CAUtil.getPrivateKey(priKeyPaht,priKeyPassWord);//读取私钥文件。
			byte[] byteData = CAUtil.signRSA(jsonData.toString().getBytes("utf-8"), true,privKey);
			jsonData.getJSONObject("head").put("sign", new String(byteData, "utf-8"));				
			newjson.put("head", jsonData.getJSONObject("head"));
			newjson.put("body", jsonData.getJSONObject("body"));
			log.info("恒丰代付请求报文："+newjson.toString());
			String res = new HttpRequestor().post(PayConstant.PAY_CONFIG.get("HF_PAY_URL"), newjson.toString());
			log.info("恒丰代付响应报文:"+res);
			JSONObject resJsonObj = JSONObject.fromObject(res);
			String sign = resJsonObj.getJSONObject("head").getString("sign");
			resJsonObj.getJSONObject("head").remove("sign");
			PublicKey peerPubKey = CAUtil.getPublicKey(pubKeyPath);
			boolean flag = CAUtil.verifyRSA(resJsonObj.toString().getBytes("utf-8"),sign.getBytes("utf-8"), true, peerPubKey);
			if (!flag) {
				throw new Exception("恒丰代付验签失败!");
			} else {
				if("000000".equals(resJsonObj.getJSONObject("head").get("retCode"))){
					rp.status="0";
					rp.errorMsg = "操作成功";
					try{new PayReceiveAndPayDAO().updatePayReceiveAndPayById(rp);} catch(Exception e){}
					HFQueryThread hfquerythread =new HFQueryThread(payRequest,priKeyPaht,priKeyPassWord,pubKeyPath);
					hfquerythread.start();
				}else{
					rp.status="2";
					rp.errorMsg = (String)resJsonObj.getJSONObject("head").get("retMsg");
					try{new PayReceiveAndPayDAO().updatePayReceiveAndPayById(rp);} catch(Exception e){}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.info(PayUtil.exceptionToString(e));
			throw e;
		}
	 }
		/**
		 * 代扣请求入口
		 * @param payRequest
		 * @throws Exception
		 */
		 
	    public void receivePaySingleInfo(PayRequest payRequest) throws Exception{
	    	try {
	    		PayReceiveAndPay rp = payRequest.receiveAndPaySingle;//已经添加的代扣记录。
	        	List<PayReceiveAndPay> AuthList = new ArrayList<PayReceiveAndPay>();
	        	AuthList.add(rp);
	        	//验证是否已经签约，如果已经签约，直接调用代扣接口，如果为签约，待用签约接口，并更新签约成功返回签约编码。
	        	//本地签约检查，根据账号
	        	PayReceiveAndPaySignDAO payReceiveAndPaySignDAO = new PayReceiveAndPaySignDAO();
	    		Map<String,PayReceiveAndPaySign> signedSuccessMap = 
	    			payReceiveAndPaySignDAO.getSignedSuccessRecord(AuthList,PayConstant.PAY_CONFIG.get("PAY_CHANNEL_HF"));//通过卡号查询是否已经签约。
	    		PayReceiveAndPaySign payReceiveAndPaySign= new PayReceiveAndPaySign();
	    		if(signedSuccessMap.get(AuthList.get(0).accNo)!=null){
	    			//已经签约
	    			payReceiveAndPaySign = signedSuccessMap.get(AuthList.get(0).accNo);
	    			payReceiveAndPaySign.signProtocolChannel = PayConstant.PAY_CONFIG.get("PAY_CHANNEL_HF");
	    			//已签约，进行代收处理
	    			String res_receivePaySingle = receivePaySingle(payRequest, payReceiveAndPaySign);
	    			JSONObject resJsonObj = JSONObject.fromObject(res_receivePaySingle);
	    			String sign = resJsonObj.getJSONObject("head").getString("sign");
	    			resJsonObj.getJSONObject("head").remove("sign");
	    			PublicKey peerPubKey = CAUtil.getPublicKey(pubKeyPath);
	    			boolean flag = CAUtil.verifyRSA(resJsonObj.toString().getBytes("utf-8"),sign.getBytes("utf-8"), true, peerPubKey);
	    			if(!flag){
	    				throw new Exception("验签失败！");
	    			}else{
	    			/**
	    			 * [{"head":{"retCode":"000000","version":"V1.0","transCode":"400010","serviceSn":"20170309000009455","MerId":"0101057340001271","retMsg":"交易处理成功",
	    			 * "sign":"K2ZFyFRx2bhLZ8ELZsndYqoCsFKESRfLfwuXAoAGO3rcXTird9+6LC3w=="}}]
	    			 */
	    				if("000000".equals(resJsonObj.getJSONObject("head").getString("retCode"))){
	    					payRequest.setRespInfo("000");
							new PayReceiveAndPayDAO().updatePayReceiveAndPay(AuthList);
							//查单
							HFQueryThread hfquerythread =new HFQueryThread(payRequest,priKeyPaht,priKeyPassWord,pubKeyPath);
							hfquerythread.start();
	    				}else{
	    					//失败
	    					AuthList.get(0).status="2";
							AuthList.get(0).setRetCode("-1");
							AuthList.get(0).errorMsg = resJsonObj.getJSONObject("head").getString("retMsg");
			       			payRequest.receivePayRes = "-1";
			       			new PayReceiveAndPayDAO().updatePayReceiveAndPay(AuthList);
	    				}
	    			}
	    		} else {
	    			//签约--------开始-----
	    			payReceiveAndPaySign.signProtocolChannel = PayConstant.PAY_CONFIG.get("PAY_CHANNEL_HF");
					String resProtlcol = signProtocol(payRequest);
					JSONObject resJsonObj = JSONObject.fromObject(resProtlcol);
	    			String sign = resJsonObj.getJSONObject("head").getString("sign");
	    			resJsonObj.getJSONObject("head").remove("sign");
	    			PublicKey peerPubKey = CAUtil.getPublicKey(pubKeyPath);
	    			boolean flag = CAUtil.verifyRSA(resJsonObj.toString().getBytes("utf-8"),sign.getBytes("utf-8"), true, peerPubKey);
	    			if(!flag){
	    				throw new Exception("验签失败！");
	    			}else{
	    				/**
						 * [{"head":{"merchantCode":"0101057340001271","version":"1.0","transCode":"800003","terminalType":"api","retCode":"999999",
						 * "retMsg":"999999 发送失败,服务器通讯错误!null",
						 * "sign":"Z6kPfTw/KE1feIqgm9gy87E8G4shz6SzOIg97lSuLcA=="}}]
						 */
	    				if("000000".equals(resJsonObj.getJSONObject("head").getString("retCode"))){
							//AuthList.get(0).protocolNo=resJsonObj.getJSONObject("body").getString("orderId");
							AuthList.get(0).signProtocolChannel=PayConstant.PAY_CONFIG.get("PAY_CHANNEL_HF");
							AuthList.get(0).status="1";
							payReceiveAndPaySignDAO.addPayReceiveAndPaySignBatch(AuthList);
							//进行代收业务处理
							String res = receivePaySingle(payRequest, payReceiveAndPaySign);
							JSONObject resPayReceiveAndPay = JSONObject.fromObject(res);
			    			String signPayReceiveAndPay = resPayReceiveAndPay.getJSONObject("head").getString("sign");
			    			resPayReceiveAndPay.getJSONObject("head").remove("sign");
			    			PublicKey peerPubKeyPayReceiveAndPay = CAUtil.getPublicKey(pubKeyPath);
			    			boolean flagPayReceiveAndPay = CAUtil.verifyRSA(resPayReceiveAndPay.toString().getBytes("utf-8"),signPayReceiveAndPay.getBytes("utf-8"), true, peerPubKeyPayReceiveAndPay);
			    			if(!flagPayReceiveAndPay){
			    				throw new Exception("验签失败！");
			    			}else{
			    			/**
			    			 * [{"head":{"retCode":"000000","version":"V1.0","transCode":"400010","serviceSn":"20170309000009455","MerId":"0101057340001271","retMsg":"交易处理成功",
			    			 * "sign":"K2ZFyFRx2bhLZ8ELZsndYqoCsFKESRfLfwuXAoAGO3rcXTird9+6LC3w=="}}]
			    			 */
			    				if("000000".equals(resPayReceiveAndPay.getJSONObject("head").getString("retCode"))){
			    					payRequest.setRespInfo("000");
			    					AuthList.get(0).status="0";//标记该笔代收业务的状态为初始化。
									new PayReceiveAndPayDAO().updatePayReceiveAndPay(AuthList);
									//查单
									HFQueryThread hfquerythread =new HFQueryThread(payRequest,priKeyPaht,priKeyPassWord,pubKeyPath);
									hfquerythread.start();
			    				}else{
			    					//失败
			    					AuthList.get(0).status="2";
									AuthList.get(0).setRetCode("-1");
									AuthList.get(0).errorMsg = resPayReceiveAndPay.getJSONObject("head").getString("retMsg");
					       			payRequest.receivePayRes = "-1";
					       			new PayReceiveAndPayDAO().updatePayReceiveAndPay(AuthList);
			    				}
			    			}
	    				}else{
	    					//失败
	    					AuthList.get(0).status="2";
							AuthList.get(0).setRetCode("-1");
							AuthList.get(0).errorMsg = resJsonObj.getJSONObject("head").getString("retMsg");
			       			payRequest.receivePayRes = "-1";
			       			new PayReceiveAndPayDAO().updatePayReceiveAndPay(AuthList);
	    				}
	    			}
	    		}
			} catch (Exception e) {
				e.printStackTrace();
			}
	    }
		/**
		 * 执行协议签约(代扣使用)
		 * @param payRequest
		 * @return  
		 * @throws Exception
		 */
		 public String signProtocol(PayRequest payRequest) throws Exception{
			 try {
				    PayCardBin cardBin = PayCardBinService.getCardBinByCardNo(payRequest.accNo);//通过卡号获取卡类信息。
					JSONObject jsonData = new JSONObject();//要发送的报文。
					JSONObject jsonhead = new JSONObject();//报文头信息。
					JSONObject jsonbody = new JSONObject();//报文体信息。
					// head
					jsonhead.put("merchantCode", PayConstant.PAY_CONFIG.get("HF_MERCHANTCODE"));//商户号。
					jsonhead.put("version", "1.0");//版本
					jsonhead.put("transCode", "800003");//接口编码。
					jsonhead.put("terminalType", "api");//接口类型。
					// body
					jsonbody.put("businessSeqNo",Tools.getUniqueIdentify());//业务流水。
					jsonbody.put("accountNo", payRequest.accNo);//付款卡号。
					jsonbody.put("accountName", cardBin.bankName);//付款银行名称。
					jsonbody.put("idCardType", "01");//证件类型。
					jsonbody.put("idCardNo", payRequest.credentialNo);//证件号码。
					jsonbody.put("bankPhoneNo", payRequest.tel);//手机号。
					jsonbody.put("bankCode", BANK_CODE.get(cardBin.bankCode));//银行编码。
					jsonData.put("head", jsonhead);
					jsonData.put("body", jsonbody);
					JSONObject newjson = new JSONObject();//最终要发送的报文数据。
					log.info(jsonData.toString());
					PrivateKey privKey = CAUtil.getPrivateKey(priKeyPaht,priKeyPassWord);//读取私钥文件。
					byte[] byteData = CAUtil.signRSA(jsonData.toString().getBytes("utf-8"), true,privKey);
					jsonData.getJSONObject("head").put("sign", new String(byteData, "utf-8"));				
					newjson.put("head", jsonData.getJSONObject("head"));
					newjson.put("body", jsonData.getJSONObject("body"));
					log.info("恒丰协议签约请求数据: "+newjson.toString());
					String res = new HttpRequestor().post(PayConstant.PAY_CONFIG.get("HF_PAY_URL"), newjson.toString());
					log.info("恒丰协议签约返回数据: "+res);
					return res;
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		 }
		/**
		 * 执行单笔代扣
		 * @param payRequest
		 * @param receiveAndPaySingle
		 * @return [{"head":{"retCode":"000000","version":"V1.0","transCode":"400010","serviceSn":"20170309000009455","MerId":"0101057340001271","retMsg":"交易处理成功","sign":"K2ZFyFRx2blHjVIUBbWOGtkrkmfMlf1yxtJMeHHS4HNHCqHFoW/Nxiext91qKTa6t8jgG+S4OyX+g733JV4nM4ngTQEyNec06Kj+EeTzAqpsKP8FPMEDBKQXCNbTX4dOmKWdfaTjNh6p5aOgh3nQBZe1Q5p1fAqqXNhoECP63nCrkdiFl7ujLV+78v0AWAyxoFr8j4XN7Yjmrcje6bqUN+FXg7i3xqbObNIyDQlODymtl3tyqsebVtjHM/1kVsXe+qDpphdo7z9jGvT6g7cR9GkqdXpjwol2mX0fyPe2hLZ8ELZsndYqoCsFKESRfLfwuXAoAGO3rcXTird9+6LC3w=="}}]
		 * @throws Exception
		 */
		 public String receivePaySingle(PayRequest payRequest,PayReceiveAndPaySign payReceiveAndPaySign) throws Exception{
			 try {
				    PayCardBin cardBin = PayCardBinService.getCardBinByCardNo(payRequest.accNo);//通过卡号获取卡类信息。
				    JSONObject jsonData = new JSONObject();//要发送的报文。
					JSONObject jsonhead = new JSONObject();//报文头信息。
					JSONObject jsonbody = new JSONObject();//报文体信息。
					// head
					jsonhead.put("merchantCode", PayConstant.PAY_CONFIG.get("HF_MERCHANTCODE"));//商户号。
					jsonhead.put("version", "1.0");//版本
					jsonhead.put("transCode", "400010");//接口编码。
					jsonhead.put("terminalType", "api");//接口类型。
					// body
					jsonbody.put("businessSeqNo", payRequest.receiveAndPaySingle.id);//业务流水。
					jsonbody.put("busiTradeType", "09900");//业务代码。  	09900：其他代付类型。
					jsonbody.put("payerAcctNo",payRequest.accNo);//付款方帐号，
					jsonbody.put("payerAcctName", payRequest.accName);//付款方名称
					if("0".equals(payRequest.accountProp)){//对私
						jsonbody.put("accountType", "00");//账户类型， 00：对私    01：对公。
					}else{
						jsonbody.put("accountType", "01");//对公
					}
					if("0".endsWith(cardBin.cardType)){//借记卡
						jsonbody.put("cdFlag", "D");//D:借记 C：贷记
					}else{//贷记卡
						jsonbody.put("cdFlag", "C");//D:借记 C：贷记
					}
					jsonbody.put("currency", "CNY");//币种。
					jsonbody.put("transAmount", String.format("%.2f", Double.parseDouble(payRequest.amount)/100d));//交易金额，单位元，精确两位小数。
					
					jsonData.put("head", jsonhead);
					jsonData.put("body", jsonbody);
					JSONObject newjson = new JSONObject();//最终要发送的报文数据。
					log.info(jsonData.toString());
					PrivateKey privKey = CAUtil.getPrivateKey(priKeyPaht,priKeyPassWord);//读取私钥文件。
					byte[] byteData = CAUtil.signRSA(jsonData.toString().getBytes("utf-8"), true,privKey);
					jsonData.getJSONObject("head").put("sign", new String(byteData, "utf-8"));				
					newjson.put("head", jsonData.getJSONObject("head"));
					newjson.put("body", jsonData.getJSONObject("body"));
					log.info("恒丰代扣请求参数: "+newjson.toString());
					String res = new HttpRequestor().post(PayConstant.PAY_CONFIG.get("HF_PAY_URL"), newjson.toString());
					log.info("恒丰代扣返回参数: "+res);
					return res;
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		} 
	 
	 
	 /**
	  * 代收付查询
	  * @param payRequest
	  * @param rp
	  * @return
	  * @throws Exception
	  */
	public boolean receivePaySingleQuery(PayRequest request,PayReceiveAndPay rp) throws Exception{
		Blog log = new Blog();
		try{
		
			JSONObject jsonData = new JSONObject();//要发送的报文。
			JSONObject jsonhead = new JSONObject();//报文头信息。
			JSONObject jsonbody = new JSONObject();//报文体信息。
			// head
			jsonhead.put("merchantCode", PayConstant.PAY_CONFIG.get("HF_MERCHANTCODE"));//商户号。
			jsonhead.put("version", "1.0");//版本
			jsonhead.put("transCode", "400011");//接口编码。
			jsonhead.put("terminalType", "api");//接口类型。
			// body
			jsonbody.put("orgBusinessSeqNo", rp.id);//业务流水。
			jsonData.put("head", jsonhead);
			jsonData.put("body", jsonbody);
			JSONObject newjson = new JSONObject();//最终要发送的报文数据。
			PrivateKey privKey = CAUtil.getPrivateKey(priKeyPaht,priKeyPassWord);//读取私钥文件。
			byte[] byteData = CAUtil.signRSA(jsonData.toString().getBytes("utf-8"), true,privKey);
			jsonData.getJSONObject("head").put("sign", new String(byteData, "utf-8"));				
			newjson.put("head", jsonData.getJSONObject("head"));
			newjson.put("body", jsonData.getJSONObject("body"));
			log.info("恒丰代收付查询请求报文:"+newjson.toString());
			String res = new HttpRequestor().post(PayConstant.PAY_CONFIG.get("HF_PAY_URL"), newjson.toString());
			/**
			 * [{"body":{"retCode":"","orgBusiTradeType":"","transStatus":"","orgServiceSn":"","orgBusinessSeqNo":"","retMsg":""},
			 * "head":{"retCode":"9999","version":"V1.0","transCode":"400011","MerId":"0101057340001271",
			 * "retMsg":"响应码: kk1001, 响应信息： 交易流水不存在",
			 * "sign":"BwjlOjn9X4kggdNy3x1dWgNAxbtNjQvijgS7RDiLAnw8Il4N+
			 * nO2mieDrjPOnfH7Hi2yz+MZGhs2OxYBuzuNY+vJ812sgkgq8XynL1A9Dn7xq
			 * Mz5/I/lJFHgm8L4owVhkV5Z9GU0Fa4pQvM81WEzFxY40uVr0ilNl6NY5G8dty34o
			 * T/i4YLkIqPXm95MY0wsjXETVv2zmV+2z7nSdKS3B2OB+ELoT7u0BPn9J7+8hdQ1Z
			 * BV3M9Boe59POG+S5UPENjjWoffoLsJyyKI+cvKTqvTyJWkw+fwJek+AT4Q3iQImwc8Eo
			 * zn8hn5jC8WL23T2zwth3Io44FqQMA/MhA8mug=="}}]
			 */
			log.info("恒丰代收付查询响应报文:"+res);
			JSONObject resJsonObj = JSONObject.fromObject(res);
			String sign = resJsonObj.getJSONObject("head").getString("sign");
			resJsonObj.getJSONObject("head").remove("sign");
			PublicKey peerPubKey = CAUtil.getPublicKey(pubKeyPath);
			boolean flag = CAUtil.verifyRSA(resJsonObj.toString().getBytes("utf-8"),sign.getBytes("utf-8"), true, peerPubKey);
			if (!flag) {
				throw new Exception("验签失败");
			} else {
				if("000000".equals(resJsonObj.getJSONObject("head").getString("retCode"))){
					if("1".equals(resJsonObj.getJSONObject("body").getString("transStatus"))){
						request.setRespCode("000");
						request.receiveAndPaySingle.setRespCode("000");
						request.receivePayRes = "0";
						request.respDesc="交易成功";
						rp.errorMsg = "交易成功";
						return true;
					}else if("0".equals(resJsonObj.getJSONObject("body").getString("transStatus"))){
						request.setRespCode("-1");
						request.receiveAndPaySingle.setRespCode("-1");
						request.receivePayRes = "-1";
						request.respDesc=resJsonObj.getJSONObject("head").getString("retMsg");
						rp.errorMsg = request.respDesc;
						return false;
					}
				}else{
					throw new Exception("恒丰代付查询请求失败");
				} 
				return false;
			}
		} catch (Exception e){
			e.printStackTrace();
			log.info(PayUtil.exceptionToString(e));
			request.setRespCode("-1");
			rp.setRespCode(request.respCode);
			request.receivePayRes = "-1";
			request.respDesc=e.getMessage();
			rp.errorMsg = request.respDesc;
			return false;
		}	
	}
	
	
	/**
	 * 微信扫码下单
	 * @param request
	 * @param payRequest
	 * @param prdOrder
	 * @param payOrder
	 * @param productType 业务标示：选择扫码机构的编码 alipay005-支付宝服务商-扫码支付   alipay006-支付宝服务商-条码支付  wechat005-微信服务商-扫码支付  wechat006-微信服务商-条码支付
	 * @return
	 */
	public String scanPay(HttpServletRequest request,PayRequest payRequest,PayProductOrder prdOrder,PayOrder payOrder,String productType){
		try {
			
			Map<String, Object> reqMap = new HashMap<String, Object>();
			
			Channel channel = new Channel();
			channel.setChannelCode("20");
			reqMap.put("channel", channel);
			Requester requester = new Requester();
			requester.setOrgNo("1O1");
			requester.setTellerId("1");
			requester.setReqJournalNo(payOrder.payordno); //请求流水号，生成规则请各接入系统自己定义，此处是demo使用
			requester.setReqTransDate(DateUtils.getDefaultDate());
			requester.setReqTransTime(DateUtils.getDefaultTime());
			reqMap.put("requester", requester);
			
			//组装业务报文数据
			reqMap.put("acceptBizNo", PayConstant.PAY_CONFIG.get("PAY_HF_WX_BIZ_NO"));//渠道号
			reqMap.put("merCustNo", PayConstant.PAY_CONFIG.get("PAY_HF_WX_MERCHAT_NO"));//商户号
			reqMap.put("merOrderNo", payOrder.payordno); //商户订单号
			reqMap.put("channelType", "1");//支付渠道   1代表移动（安卓、IOS）  2代表 PC  3代表 WAP  4代表e-pos
			reqMap.put("txAmt", String.format("%.2f", ((double)payOrder.txamt)/100d));//交易金额：单位元
			reqMap.put("ccy", "CNY");//币种
			
			reqMap.put("payMethod", "18");//支付方式  18-扫码  19-条码
			reqMap.put("payChannelNo", productType);//支付渠道  alipay005-支付宝服务商-扫码支付   alipay006-支付宝服务商-条码支付  wechat005-微信服务商-扫码支付  wechat006-微信服务商-条码支付
			reqMap.put("notifyUrl", PayConstant.PAY_CONFIG.get("PAY_HF_WX_NOTIFY_URL"));//异步通知URL
			
			Map<String, Object> payReqExtractMap = new HashMap<String, Object>();
			wechatOrAlipay(payReqExtractMap, productType);
			
			reqMap.put("payReqExtract", payReqExtractMap);//支付对象
			log.info("恒丰微信/支付宝扫码请求报文："+reqMap.toString());
			
			//发送请求，获取返回responseMap
			HashMap<String,Object> responseMap = HttpUtils.httpPostAndSign(PayConstant.PAY_CONFIG.get("HF_WX_PAY_URL"), privateKey,reqMap);
			com.alibaba.fastjson.JSONObject jsonObject = null;;
			if(null!=responseMap){
				String content = (String) responseMap.get("content");
				String responseSign = (String) responseMap.get("sign");
				jsonObject = com.alibaba.fastjson.JSONObject.parseObject(content);
				
				/**{"tellerId":"1","payInfo":{"merOrderNo":"1490842185878","payObject":{"prepay_id":"wx2017033010494944fd790c6d0590645425","code_url":"weixin://wxpay/bizpayurl?pr=yKA7it2"},
				 * "orderJrnNo":"PM0032017033000000003"},"orgNo":"1O1","reqJournalNo":"1490842185785","zipped":"N","timeZone":8,"approvalFlag":false,"messages":[],"transDate":"2017-03-30",
				 * "systemNo":"CORE","encrypted":"N","journalNo":"PM0022017033000003002","authLevel":"00  ","transTime":"10:53:08","appCode":"00",
				 * "resultType":"N","serviceCode":"000000","accountingDate":"2017-03-30","pagination":{}}
				 */
				log.info("恒丰微信/支付宝扫码响应报文："+content);
				
				//验签
				boolean result = RSA.verify(content, responseSign, hfbankPublicKey, "UTF-8");
				if(result){
					if("N".equals(jsonObject.get("resultType"))){//下单成功处理。
						String urlCode = "wechat005".equals(productType)?jsonObject.getJSONObject("payInfo").getJSONObject("payObject").get("code_url").toString()
								:jsonObject.getJSONObject("payInfo").getJSONObject("payObject").get("qrCode").toString();
						return "<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"no\"?>" +
						"<message merchantId=\""+payRequest.merchantId+"\" merchantOrderId=\""+payRequest.merchantOrderId+"\" " +
						"codeUrl=\""+ urlCode +"\" respCode=\"000\" respDesc=\"处理成功\"/>";
					}else{//业务处理错误。
						return "<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"no\"?>" +
			       		   "<message merchantId=\""+payRequest.merchantId+"\" merchantOrderId=\""+payRequest.merchantOrderId+"\" " +
			       			"codeUrl=\"\" respCode=\"-1\" respDesc=\""+jsonObject.get("errorMsg").toString()+"\"/>";  
					}
				}else throw new Exception("验签失败");
			}else throw new Exception("(HF)渠道错误！");
		} catch (Exception e) {
			e.printStackTrace();
			return "<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"no\"?>" +
				"<message merchantId=\""+payRequest.merchantId+"\" merchantOrderId=\""+payRequest.merchantOrderId+"\" " +
				"codeUrl=\"\" respCode=\"-1\" respDesc=\""+e.getMessage()+"\"/>";
		}
	}
	
	/**
	 * 扫码查询单个订单信息
	 * @throws Exception
	 */
	public void channelQuery(PayOrder payOrder) throws Exception{
		
		Map<String, Object> reqMap = new HashMap<String, Object>();
		Channel channel = new Channel();
		channel.setChannelCode("20");
		reqMap.put("channel", channel);
		Requester requester = new Requester();
		requester.setOrgNo("1O1");
		requester.setTellerId("1");
		requester.setReqJournalNo(Long.toString(System.currentTimeMillis())); //请求流水号，生成规则请各接入系统自己定义，此处是demo使用
		requester.setReqTransDate(DateUtils.getDefaultDate());
		requester.setReqTransTime(DateUtils.getDefaultTime());
		reqMap.put("requester", requester);
		//组装业务参数
		reqMap.put("acceptBizNo", PayConstant.PAY_CONFIG.get("PAY_HF_WX_BIZ_NO"));//渠道号
		reqMap.put("merCustNo", PayConstant.PAY_CONFIG.get("PAY_HF_WX_MERCHAT_NO"));//商户号
		reqMap.put("merOrderNo", payOrder.payordno);//订单号  1490603002988
		
		//发送请求，获取返回responseMap
		log.info("恒丰查询单个订单请求报文："+ reqMap.toString());
		HashMap<String,Object> responseMap = HttpUtils.httpPostAndSign(PayConstant.PAY_CONFIG.get("HF_WX_PAY_QUERY_URL"), privateKey,reqMap);
		com.alibaba.fastjson.JSONObject jsonObject = null;;
		String content = null;
		if(null!=responseMap){
			content = (String) responseMap.get("content");
			String responseSign = (String) responseMap.get("sign");
			jsonObject = com.alibaba.fastjson.JSONObject.parseObject(content);
			/**
			 * {"terminalNo":null,"accountingDate":"2017-03-30","pagination":{"orderRule":null,"pageNo":null,"totalPages":null,"orderBy":null,"pageSize":null,
			 * "totalCounts":null},"transTime":"11:52:37","serviceCode":"000000","zipped":"N","timeZone":8,"appCode":"00","journalNo":"PM2662017033000003266",
			 * "voucherNo":null,"reqJournalNo":"1490845756525","tellerId":"1","encrypted":"N","orgNo":"1O1","systemNo":"CORE","transDate":"2017-03-30",
			 * "payOrderInfo":{"seqNo":"PM0022017033000000002","merCustNo":"101703220601","acceptBizNo":"HF0004","platOrderNo":"HF00041490841027448",
			 * "custName":null,"credNo":null,"channelType":"1","bizType":null,"txAmt":0.01,"ccy":"CNY","payMethod":"","payChannelNo":"wechat005","remark":null,
			 * "orderJrnNo":"PM0022017033000000002","merOrderNo":"1490841027448","payReqExtract":"{\"attach\":\"\",\"authCode\":\"\",\"body\":\"门店品牌名-城市分店名-实际商品名称\",
			 * \"deviceInfo\":\"\",\"goodsDetail\":[],\"goodsTag\":\"\",\"limitPay\":\"\",\"spbillCreateIp\":\"127.0.0.1\",\"thirdSubMchId\":\"\",\"timeExpire\":\"\"}",
			 * "payBankCardType":null,"status":"04","thirdStatus":"00","csNotifyStatus":null,"thirdResCode":null,"thirdResMsg":null,"payFee":null,"settleNo":null,
			 * "settleAmt":null,"proitAmt":0.00,"daySettleNo":null,"dayProitNo":null,"settleTime":null,"settleStatus":"","createDate":1490841232000,"payTime":null,
			 * "refundTime":null,"lastUpdateTime":1490841232000,"startTime":null,"endTime":null,"transationStartTime":null,"transationEndTime":null,
			 * "payStartTime":null,"payEndTime":null,"settleStartTime":null,"settleEndTime":null,"null":false},"messages":[],"approvalFlag":false,
			 * "authLevel":"00  ","resultType":"N"}
			 */
			log.info("恒丰查询单个订单响应报文："+content);
			
			//验签，验证返回的报文是否合法
			boolean result = RSA.verify(content, responseSign, hfbankPublicKey, "UTF-8");
			if(result){
				if("N".equals(jsonObject.get("resultType"))){//查询成功
					if("01".equals(jsonObject.getJSONObject("payOrderInfo").get("status"))){
						payOrder.ordstatus="01";//支付成功
			        	new NotifyInterface().notifyMer(payOrder);//支付成功
					}else if("02".equals(jsonObject.getJSONObject("payOrderInfo").get("status"))){
						payOrder.ordstatus="02";//支付失败。
			        	new NotifyInterface().notifyMer(payOrder);//支付失败。
					}
				}else throw new Exception("恒丰扫码查询请求失败");
			}else throw new Exception("验签失败");
		}	
	}
	
	/**
	 * 扫码支付对象（微信或者支付宝）
	 * @param payReqExtractMap
	 * @param productType
	 */
	public void wechatOrAlipay(Map<String, Object> payReqExtractMap , String productType){
		if("wechat005".equals(productType)){//微信扫码
			payReqExtractMap.put("spbillCreateIp", PayConstant.PAY_CONFIG.get("PAY_HF_WX_IP"));//终端IP
			payReqExtractMap.put("body", PayConstant.PAY_CONFIG.get("COMMON_PRODUCT_DESC")==null||PayConstant.PAY_CONFIG.get("COMMON_PRODUCT_DESC").length()==0?
					"_":PayConstant.PAY_CONFIG.get("COMMON_PRODUCT_DESC"));//订单描述
		}else if("alipay005".equals(productType)){//支付宝扫码
			payReqExtractMap.put("subject", "线上支付");//订单标题
			payReqExtractMap.put("storeId", "10000");//商户门店编号
			payReqExtractMap.put("terminalId", "3213213");//机具终端编号
		}
	}
}
class HFQueryThread extends Thread{
	
	private static final Log log = LogFactory.getLog(HFQueryThread.class);
	private  PayRequest payRequest= new PayRequest();
	private  String priKeyPaht="";
	private  String priKeyPassWord="";
	private  String pubKeyPath="";
	public HFQueryThread(){};
	public HFQueryThread(PayRequest payRequest,String priKeyPaht,String priKeyPassWord,String pubKeyPath){
		
		this.payRequest=payRequest;
		this.priKeyPaht=priKeyPaht;
		this.priKeyPassWord=priKeyPassWord;
		this.pubKeyPath=pubKeyPath;
	}
	@Override
	public void run() {
		try {
			Thread.sleep(1000*60*1);
			for(int i=0;i<3;i++){
				if(query()){
					break;
				}
				Thread.sleep(1000*60);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public boolean query()throws Exception{
		boolean flag=false;
		try {
			PayReceiveAndPay rp = payRequest.receiveAndPaySingle;//已经添加的代扣记录。
			JSONObject jsonData = new JSONObject();//要发送的报文。
			JSONObject jsonhead = new JSONObject();//报文头信息。
			JSONObject jsonbody = new JSONObject();//报文体信息。
			// head
			jsonhead.put("merchantCode", "0101057340001271");//商户号。
			jsonhead.put("version", "1.0");//版本
			jsonhead.put("transCode", "400011");//接口编码。
			jsonhead.put("terminalType", "api");//接口类型。
			// body
			jsonbody.put("orgBusinessSeqNo", rp.id);//业务流水。
			jsonData.put("head", jsonhead);
			jsonData.put("body", jsonbody);
			JSONObject newjson = new JSONObject();//最终要发送的报文数据。
			PrivateKey privKey = CAUtil.getPrivateKey(priKeyPaht,priKeyPassWord);//读取私钥文件。
			byte[] byteData = CAUtil.signRSA(jsonData.toString().getBytes("utf-8"), true,privKey);
			jsonData.getJSONObject("head").put("sign", new String(byteData, "utf-8"));				
			newjson.put("head", jsonData.getJSONObject("head"));
			newjson.put("body", jsonData.getJSONObject("body"));
			log.info("恒丰代收付查询请求报文:"+newjson.toString());
			String res = new HttpRequestor().post(PayConstant.PAY_CONFIG.get("HF_PAY_URL"), newjson.toString());
			log.info("恒丰代收付查询响应报文:"+res);
			JSONObject resJsonObj = JSONObject.fromObject(res);
			String sign = resJsonObj.getJSONObject("head").getString("sign");
			resJsonObj.getJSONObject("head").remove("sign");
			PublicKey peerPubKey = CAUtil.getPublicKey(pubKeyPath);
			boolean bl = CAUtil.verifyRSA(resJsonObj.toString().getBytes("utf-8"),sign.getBytes("utf-8"), true, peerPubKey);
			if (!bl) {
				throw new Exception("恒丰查询验签失败");
			} else {
				if("000000".equals(resJsonObj.getJSONObject("head").getString("retCode"))){
					if("1".equals(resJsonObj.getJSONObject("body").getString("transStatus"))){
						log.info("zhfu");
						rp.status="1";
						rp.respCode="000";
						rp.errorMsg="交易成功";
						try{new PayReceiveAndPayDAO().updatePayReceiveAndPayById(rp);} catch(Exception e){}
						flag=true;
						List<PayReceiveAndPay> list = new ArrayList<PayReceiveAndPay>();
						list.add(rp);
						new PayInterfaceOtherService().receivePayNotify(payRequest,list);
					} else if("0".equals(resJsonObj.getJSONObject("body").getString("transStatus"))){
						rp.status="2";
						rp.respCode="-1";
						rp.errorMsg=resJsonObj.getJSONObject("body").getString("retMsg");
						try{new PayReceiveAndPayDAO().updatePayReceiveAndPayById(rp);} catch(Exception e){}
						flag=false;
//						List<PayReceiveAndPay> list = new ArrayList<PayReceiveAndPay>();
//						list.add(rp);
//						new PayInterfaceOtherService().receivePayNotify(payRequest,list);
					}
				}else if("999999".equals(resJsonObj.getJSONObject("head").getString("retCode"))){
					rp.status="2";
					rp.respCode="-1";
					rp.errorMsg=resJsonObj.getJSONObject("head").getString("retMsg");
					try{new PayReceiveAndPayDAO().updatePayReceiveAndPayById(rp);} catch(Exception e){}
					flag=false;
//					List<PayReceiveAndPay> list = new ArrayList<PayReceiveAndPay>();
//					list.add(rp);
//					new PayInterfaceOtherService().receivePayNotify(payRequest,list);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return flag;
		}
		return flag;
	}
}
