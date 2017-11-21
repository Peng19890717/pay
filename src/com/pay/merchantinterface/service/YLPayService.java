package com.pay.merchantinterface.service;
import java.net.HttpURLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import util.JWebConstant;
import util.PayUtil;

import com.PayConstant;
import com.jweb.dao.Blog;
import com.pay.order.dao.PayOrder;
import com.pay.order.dao.PayReceiveAndPay;
import com.pay.order.dao.PayReceiveAndPayDAO;
import com.third.yilian.service.encrypt.TripleDes;
import com.third.yilian.service.util.SslConnection;
import com.third.yilian.service.util.Strings;
import com.third.yilian.service.util.Util;
import com.third.yilian.service.versionSMS.MsgBean;
import com.third.yilian.service.versionSMS.MsgBody;
/**
 * 易联服务类
 * @author Administrator
 *
 */
public class YLPayService {
	private static final Log log = LogFactory.getLog(YLPayService.class);
	private static String mer_pfx_key = JWebConstant.APP_PATH+JWebConstant.PATH_DIV+"config"+JWebConstant.PATH_DIV+PayConstant.PAY_CONFIG.get("YL_MERCHANT_PFX");//商户私钥
	private static String mer_pfx_pass = PayConstant.PAY_CONFIG.get("YL_MERCHANT_PFX_PASS");//商户私钥密码;

	// 唯一值处理-------------------start
	static byte [] cmb = {'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'};
	private static String getRadixOf62(long src){
		StringBuffer sb = new StringBuffer();		
		while(src > 0){
			int mod = (int) (src%cmb.length);
			sb.append(new String(new byte[]{cmb[mod]}));
			src = src/cmb.length;
		}
		return reverse(sb.toString());
	}
	private static String reverse(String str){   
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
	private static String curTimeStr = getRadixOf62(System.currentTimeMillis());
	private static long identifySeed = 1;
	private static String countPerSecondS = "9999999";
	private static long countPerSecond = new Long(countPerSecondS);
	private static synchronized String getUniqueIdentify() {
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
	/**
	 * 单笔代扣。
	 * @param payRequest
	 * @throws Exception
	 */
	 public void receivePaySingleInfo(PayRequest payRequest) throws Exception{
		 try {
		 	PayReceiveAndPayDAO dao = new PayReceiveAndPayDAO();
		 	PayReceiveAndPay rp = payRequest.receiveAndPaySingle;//已经添加的代扣记录。
		 	rp.sn=getUniqueIdentify();
		 	rp.batNo="YCF"+new SimpleDateFormat("yyMMdd").format(new Date())+rp.sn;
		 	dao.updatePayReceiveAndPaySn(rp);
        	List<PayReceiveAndPay> AuthList = new ArrayList<PayReceiveAndPay>();
        	AuthList.add(rp);
	    	MsgBean req_bean = new MsgBean();
			req_bean.setVERSION("2.0");
			req_bean.setMSG_TYPE("200001");//代扣标识。
			req_bean.setBATCH_NO(rp.batNo);//批次号。
			req_bean.setUSER_NAME(PayConstant.PAY_CONFIG.get("PAY_YL_USER_NAME"));//系统后台登录名
			MsgBody body = new MsgBody();
			body.setSN(rp.sn);//流水号.
			body.setACC_NO(rp.accountNo);//卡号。
			body.setACC_NAME(rp.accountName);//姓名。
			body.setID_NO(rp.certId);//身份证号。
		    body.setAMOUNT( String.format("%.2f", Double.parseDouble(payRequest.amount)/100d));//交易金额必填
			body.setRETURN_URL(PayConstant.PAY_CONFIG.get("PAY_YL_NOTIFY_URL"));//异步通知地址
			req_bean.getBODYS().add(body);
			String res = sendAndRead(signANDencrypt(req_bean));
			MsgBean res_bean = decryptANDverify(res);
			//通信请求失败。
			if(!"0000".equals(res_bean.getTRANS_STATE())) {
				AuthList.get(0).status="2";
				AuthList.get(0).setRetCode("-1");
				AuthList.get(0).errorMsg = "易联代扣失败!";
       			payRequest.receivePayRes = "-1";
       			dao.updatePayReceiveAndPay(AuthList);
			}else{
				payRequest.setRespInfo("000");
				dao.updatePayReceiveAndPay(AuthList);
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.info(PayUtil.exceptionToString(e));
			throw e;
		}
	 }
	 /**
	  * 代扣查询
	  * @param payRequest
	  * @param rp
	  * @return
	  * @throws Exception
	  */
	public boolean receivePaySingleQuery(PayRequest payRequest,PayReceiveAndPay rp) throws Exception{
		Blog log = new Blog();
		try{
			MsgBean req_bean = new MsgBean();
			req_bean.setVERSION("2.0");
			req_bean.setMSG_TYPE("200002");
			req_bean.setBATCH_NO(rp.batNo); //同代收交易请求的批次号
			req_bean.setUSER_NAME(PayConstant.PAY_CONFIG.get("PAY_YL_USER_NAME"));//系统后台登录名
			String res = sendAndRead(signANDencrypt(req_bean));
			MsgBean res_bean = decryptANDverify(res);
			log.info("易联渠道补单返回报文:"+res_bean.toXml());
			if("0000".equals(res_bean.getTRANS_STATE())) {
				if(res_bean.getBODYS().get(0).getPAY_STATE().equals("0000")){//成功
					payRequest.setRespCode("000");
					payRequest.receiveAndPaySingle.setRespCode("000");
					payRequest.receivePayRes = "0";
					rp.errorMsg = "处理成功";
					return true;
				} else {
					payRequest.setRespCode("-1");
					rp.setRespCode(payRequest.respCode);
					payRequest.receivePayRes = "-1";
					payRequest.respDesc=res_bean.getBODYS().get(0).getREMARK();
					rp.errorMsg = payRequest.respDesc;
					return false;
				}
			} throw new Exception("(易联查询)请求失败!");
		} catch (Exception e){
			e.printStackTrace();
			log.info(PayUtil.exceptionToString(e));
			payRequest.setRespCode("-1");
			rp.setRespCode(payRequest.respCode);
			payRequest.receivePayRes = "-1";
			payRequest.respDesc=e.getMessage();
			rp.errorMsg = payRequest.respDesc;
			return false;
		}	
	}
    /**
     * 代收包装快捷补单
     * @param payOrder
     * @param rp
     * @throws Exception
     */
	public void tranQueryForRepair(PayOrder payOrder,PayReceiveAndPay rp) throws Exception{
		try{
			MsgBean req_bean = new MsgBean();
			req_bean.setVERSION("2.0");
			req_bean.setMSG_TYPE("200002");
			req_bean.setBATCH_NO(rp.batNo); //同代收交易请求的批次号
			req_bean.setUSER_NAME(PayConstant.PAY_CONFIG.get("PAY_YL_USER_NAME"));//系统后台登录名
			String res = sendAndRead(signANDencrypt(req_bean));
			MsgBean res_bean = decryptANDverify(res);
			log.info("易联渠道补单返回报文:"+res_bean.toXml());
			if("0000".equals(res_bean.getTRANS_STATE())) {
				if(res_bean.getBODYS().get(0).getPAY_STATE().equals("0000")){//成功
					//更新代收信息
					rp.status="1";
					rp.errorMsg = "";
					payOrder.ordstatus="01";//支付成功
					payOrder.actdat = new Date();
		        	try{new PayReceiveAndPayDAO().updatePayReceiveAndPayById(rp);} catch(Exception e){}
				} else {
					rp.status="2";
					rp.errorMsg = res_bean.getBODYS().get(0).getREMARK();
					payOrder.ordstatus="02";
					payOrder.bankerror=rp.errorMsg;
					payOrder.actdat = new Date();
					try{new PayReceiveAndPayDAO().updatePayReceiveAndPayById(rp);} catch(Exception e){}
				}
			} else throw new Exception("(易联查询)请求失败!");
		} catch (Exception e){
			e.printStackTrace();
			log.info(PayUtil.exceptionToString(e));
			throw new Exception("银生宝代扣补单异常："+e.getMessage());
		}	
	}
	/**
	 * 加密处理要发送的报文。
	 * @param req_bean
	 * @return
	 */
	private static String signANDencrypt(MsgBean req_bean) {
		//商户签名
		req_bean.setMSG_SIGN(com.third.yilian.service.encrypt.RSA.sign(req_bean.toSign(),mer_pfx_key,mer_pfx_pass));
		log.info("易联代扣请求明文:" + req_bean.toXml());
		//加密报文
		String key = Util.generateKey(9999,24);
		String req_body_enc = TripleDes.encrypt(key, req_bean.toXml());
		//加密密钥
		String req_key_enc = com.third.yilian.service.encrypt.RSA.encrypt(key, PayConstant.PAY_CONFIG.get("YL_PUB_KEY"));
		return req_body_enc+"|"+req_key_enc;
	}
	/**
	 * 解密处理返回报文。
	 * @param res
	 * @return
	 */
	private static MsgBean decryptANDverify(String res) {
		String msg_sign_enc = res.split("\\|")[0];
		String key_3des_enc = res.split("\\|")[1];
		//解密密钥
		String key_3des = com.third.yilian.service.encrypt.RSA.decrypt(key_3des_enc,mer_pfx_key,mer_pfx_pass);
		//解密报文
		String msg_sign = TripleDes.decrypt(key_3des, msg_sign_enc);
		MsgBean res_bean = new MsgBean();
		res_bean.toBean(msg_sign);
		log.info("易联代扣返回数据：" + res_bean.toXml());
		//验签
		String dna_sign_msg = res_bean.getMSG_SIGN();
		res_bean.setMSG_SIGN("");
		String verify = Strings.isNullOrEmpty(res_bean.getVERSION())? res_bean.toXml(): res_bean.toSign();
		if(!com.third.yilian.service.encrypt.RSA.verify(dna_sign_msg, PayConstant.PAY_CONFIG.get("YL_PUB_KEY"), verify)) {
			log.info("验签失败");
			res_bean.setTRANS_STATE("00A0");
		}
		return res_bean;
	}
	/**
	 * 发送post请求到易联代付
	 * @param req
	 * @return
	 * @throws Exception 
	 */
	public static String sendAndRead(String req) throws Exception {
		try {
			log.info("易联代扣请求密文:  "+req);
			HttpURLConnection connect = new SslConnection().openConnection(PayConstant.PAY_CONFIG.get("PAY_YL_URL"));
	        connect.setReadTimeout(120000);
			connect.setConnectTimeout(30000);
			connect.setRequestMethod("POST");
			connect.setDoInput(true);
			connect.setDoOutput(true);
			connect.connect();
			byte[] put = req.getBytes("UTF-8");
			connect.getOutputStream().write(put);
			connect.getOutputStream().flush();
			connect.getOutputStream().close();
			String res = SslConnection.read(connect);
			connect.getInputStream().close();
			connect.disconnect();
			return res;
		} catch(Exception e) {
			e.printStackTrace();
			log.info(PayUtil.exceptionToString(e));
			throw e;
		}
	}
}
