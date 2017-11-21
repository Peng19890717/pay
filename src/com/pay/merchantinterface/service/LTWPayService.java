package com.pay.merchantinterface.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import util.JWebConstant;
import util.PayUtil;

import com.PayConstant;
import com.pay.cardbin.dao.PayCardBin;
import com.pay.cardbin.service.PayCardBinService;
import com.pay.order.dao.PayReceiveAndPay;
import com.pay.order.dao.PayReceiveAndPayDAO;
import com.third.ld.DataTransUtil;
import com.third.wo.FileUtils;
import com.third.wo.UniPaySignUtilsCer;

public class LTWPayService {
	private static final Log log = LogFactory.getLog(LTWPayService.class);
	private static String charset = "utf-8";
	public static String certPath = JWebConstant.APP_PATH+JWebConstant.PATH_DIV+"config"+JWebConstant.PATH_DIV+PayConstant.PAY_CONFIG.get("LTW_PRV_KEY");//私钥
	public static Map<String, String> PAY_BANK = new HashMap<String, String>();
	static{
		PAY_BANK.put("CNCB", "CITIC");//中信
		PAY_BANK.put("ZSBC", "CZ");//浙商银行
		PAY_BANK.put("CIB", "CIB");//兴业银行
		PAY_BANK.put("PSBC", "PSBC");//中国邮政储蓄银行
		PAY_BANK.put("CMBC", "CMBC");//中国民生银行
		PAY_BANK.put("ABC", "ABC");//中国农业银行
		PAY_BANK.put("BOC", "BOC");//中国银行
		PAY_BANK.put("CEB", "CEB");//中国光大银行
		PAY_BANK.put("BCCB", "BCCB");//中国光大银行
		PAY_BANK.put("WZCB", "BOWZ");//温州银行
		PAY_BANK.put("GZCB", "BOGZ");//广州银行
		PAY_BANK.put("BOHC", "CBHB");//渤海银行
		PAY_BANK.put("PAB", "SPA");//平安银行
		PAY_BANK.put("SPDB", "SPDB");//上海浦东发展银行
		PAY_BANK.put("EGBANK", "EGB");//恒丰银行
		PAY_BANK.put("ICBC", "ICBC");//中国工商银行
		PAY_BANK.put("BOCOM", "COMM");//交通银行
		PAY_BANK.put("CCB", "CCB");//中国建设银行
		PAY_BANK.put("GDB", "GDB");//广东发展银行
		PAY_BANK.put("CMB", "CMB");//招商银行
		PAY_BANK.put("HXB", "HXB");//华夏银行
		PAY_BANK.put("HSBANK", "HSB");//徽商银行
		PAY_BANK.put("NJBC", "NJCB");//南京银行
		PAY_BANK.put("HKBCHINA", "HKB");//汉口银行
	}
	/**
	 * 单笔代付
	 * @param payRequest
	 * @throws Exception
	 */
	public void receivePaySingle(PayRequest payRequest) throws Exception{
		try {
			PayReceiveAndPay rp = payRequest.receiveAndPaySingle;//已经添加的代扣记录。
			PayCardBin cardBin = PayCardBinService.getCardBinByCardNo(payRequest.accNo);//通过卡号获取卡类信息。
			Map<String, String> params = new HashMap<String, String>();
			params.put("interfaceVersion", "1.0.0.1");
			params.put("tranType", "DF02");//DF01-资金代付沃账户 DF02-资金代付银行卡 DF03-协议资金代付银行卡 DF04-资金代付酬金账户
			params.put("merNo", PayConstant.PAY_CONFIG.get("LTW_MERNO"));
			params.put("orderDate", new SimpleDateFormat("yyyyMMdd").format(new Date()));
			params.put("reqTime", new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
			params.put("orderNo", rp.id);
			params.put("amount", payRequest.amount);
			params.put("bizCode", "017");
			params.put("payeeAcc", rp.accountNo);//卡号
			params.put("woType", "4");//1-对私内部用户号 2-对公内部用户号3-对私注册号，4-对私银行账号，5-对公银行账号 51-对私快捷业务协议号 52-对私快捷协议号
			params.put("payeeBankCode", PAY_BANK.get(cardBin.bankCode)); //测试环境支持ICBC
			params.put("payeeName", rp.accountName);//tranType=DF02时必填
			params.put("callbackUrl", PayConstant.PAY_CONFIG.get("LTW_DF_NOTIFY_URL"));
			params.put("signType", "RSA_SHA256");//MD5、RSA_SHA256
			//签名
			String signMsg = UniPaySignUtilsCer.merSign(params, "RSA_SHA256", true, FileUtils.readFile(certPath).toByteArray(), PayConstant.PAY_CONFIG.get("LTW_PRV_PASSWORD"));
			params.put("signMsg", signMsg);
			System.out.println(params);
			log.info("联通沃单笔代付请求数据："+params);
			String reqData = UniPaySignUtilsCer.createReqString(params);
			String resData = new String(new DataTransUtil().doPost(PayConstant.PAY_CONFIG.get("LTW_DF_PAY_URL"), reqData.getBytes(charset)),charset);
			log.info("联通沃单笔代付响应数据---------"+resData);
			/**
			 * interfaceVersion=1.0.0.1&tranType=DF02&merNo=301100710007122&orderNo=20171113104216&amount=10&
			 * orderDate=20171113&reqTime=20171113104216&transRst=1&transDis=测试&payTime=20171113104240&payJnlno=201711130000450043
			 * &bizCode=017&payeeAcc=6222020409031144804&woType=4&payeeBankCode=ICBC&payeeName=刘强强&payeeBankBranch=&merExtend=&signType=RSA_SHA256&
			 * signMsg=cbz7+BdnaLYiZBLvg+5MUSJp/Fa9ExNu15+BQBLKntiYWowg11L4kyVhSQhVAgrraCgCvZoTxY8miw/1mmBAlpGedYL2z+BWum5Y9gppyheol+LxlqwN7DFuv69pCjLPfc2VXJqyn/Xes6HuMIglAjSUJ1a7B+jUVMPcG2fz12E=&identityInfo=
			 */
			//验签
			Map<String,String> map = UniPaySignUtilsCer.rep2Map(resData,"\\&","\\=");
			if(UniPaySignUtilsCer.merVerify(map, "RSA_SHA256", map.get("signMsg"), PayConstant.PAY_CONFIG.get("LTW_PUB_CERT"),true)){//验签成功
				if("1".equals(map.get("transRst"))){//0-处理中，1-交易成功，3-交易失败，4-退票，5-失败
					rp.status="1";
					rp.setRetCode("000");
					rp.errorMsg = "交易成功";
	       			payRequest.receivePayRes = "0";
	       			try{new PayReceiveAndPayDAO().updatePayReceiveAndPayById(rp);} catch(Exception e){};
    			}else if("3".equals(map.get("transRst"))){
    				rp.status="2";
					rp.setRetCode("-1");
					rp.errorMsg = map.get("transDis");
	       			payRequest.receivePayRes = "-1";
	       			try{new PayReceiveAndPayDAO().updatePayReceiveAndPayById(rp);} catch(Exception e){};
    			}else if("0".equals(map.get("transRst"))){//处理中
					LTWQueryThread ltwQueryThread = new LTWQueryThread(payRequest);
					ltwQueryThread.start();
				}
    		}else throw new Exception("验签失败");
		} catch (Exception e) {
			e.printStackTrace();
			log.info(PayUtil.exceptionToString(e));
			throw e;
		}
	}
	/**
	 * 单笔代付查询(单笔)
	 * @param payRequest
	 * @param rp
	 * @return
	 * @throws Exception
	 */
	public void receivePaySingleQuery(PayRequest request,PayReceiveAndPay rp)throws Exception{
		try {
			Map<String, String> params = new HashMap<String, String>();
			params.put("interfaceVersion", "1.0.0.0");
			params.put("tranType", "DF02");//DF01-资金代付沃账户 DF02-资金代付银行卡 DF03-协议资金代付银行卡 DF04-资金代付酬金账户
			params.put("merNo", PayConstant.PAY_CONFIG.get("LTW_MERNO"));
			params.put("orderDate", new SimpleDateFormat("yyyyMMdd").format(rp.createTime));//原商品订单日期
			params.put("reqTime", new SimpleDateFormat("yyyyMMddHHmmss").format(rp.createTime));//订单请求时间
			params.put("orderNo", rp.id);//订单号
			params.put("signType", "RSA_SHA256");//MD5、RSA_SHA256
			//签名
			String signMsg = UniPaySignUtilsCer.merSign(params, "RSA_SHA256", true, FileUtils.readFile(certPath).toByteArray(), PayConstant.PAY_CONFIG.get("LTW_PRV_PASSWORD"));
			params.put("signMsg", signMsg);
			log.info("单笔代付查询请求数据："+params);
			String reqData = UniPaySignUtilsCer.createReqString(params);
			String resData = new String(new DataTransUtil().doPost(PayConstant.PAY_CONFIG.get("LTW_DF_QUERY_PAY_URL"), reqData.getBytes(charset)),charset);
			log.info("单笔代付查询响应数据："+resData);
			//验签
			Map<String,String> map = UniPaySignUtilsCer.rep2Map(resData,"\\&","\\=");
			if(UniPaySignUtilsCer.merVerify(map, "RSA_SHA256", map.get("signMsg"), PayConstant.PAY_CONFIG.get("LTW_PUB_CERT"),true)){//验签成功
				if("1".equals(map.get("transRst"))){
					request.setRespCode("000");
					rp.setRespCode("000");
					request.receivePayRes = "0";
					request.respDesc="交易成功";
					rp.errorMsg = "交易成功";
				}else if("3".equals(map.get("transRst"))){
					request.setRespCode("-1");
					rp.setRespCode("000");
					request.receivePayRes = "-1";
					request.respDesc = map.get("transDis");
					rp.errorMsg = request.respDesc;
				}else {
					request.setRespCode("-1");
					request.receiveAndPaySingle.setRespCode("-1");
					request.receivePayRes = "-1";
					request.respDesc=map.get("transDis");
					rp.errorMsg = request.respDesc;
				}
			}else throw new Exception("验签失败");
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
}

class LTWQueryThread extends Thread{
	private static final Log log = LogFactory.getLog(LTWQueryThread.class);
	private  PayRequest payRequest = new PayRequest();
	public LTWQueryThread(){};
	public LTWQueryThread(PayRequest payRequest){
		this.payRequest = payRequest;
	}
	@Override
	public void run() {
		try {
			for(int i=0; i<CJPayService.CJ_RECEIVE_PAY_SEARCH_TIME_FREQUENCY.length; i++){
				try {if(query())break;} catch (Exception e) {}
				log.info("联通沃支付代付查询第"+(i+1)+"次,等待时间"+CJPayService.CJ_RECEIVE_PAY_SEARCH_TIME_FREQUENCY[i]+"秒");
				try {Thread.sleep(CJPayService.CJ_RECEIVE_PAY_SEARCH_TIME_FREQUENCY[i]*1000);} catch (Exception e) {}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public boolean query()throws Exception{
		boolean flag = false;
		try {
			PayReceiveAndPay rp = payRequest.receiveAndPaySingle;//已经添加的代扣记录。
			Map<String, String> params = new HashMap<String, String>();
			params.put("interfaceVersion", "1.0.0.0");
			params.put("tranType", "DF02");//DF01-资金代付沃账户 DF02-资金代付银行卡 DF03-协议资金代付银行卡 DF04-资金代付酬金账户
			params.put("merNo", PayConstant.PAY_CONFIG.get("LTW_MERNO"));
			params.put("orderDate", new SimpleDateFormat("yyyyMMdd").format(rp.createTime));//原商品订单日期
			params.put("reqTime", new SimpleDateFormat("yyyyMMddHHmmss").format(rp.createTime));//订单请求时间
			params.put("orderNo", rp.id);//订单号
			params.put("signType", "RSA_SHA256");//MD5、RSA_SHA256
			//签名
			String signMsg = UniPaySignUtilsCer.merSign(params, "RSA_SHA256", true, FileUtils.readFile(LTWPayService.certPath).toByteArray(), PayConstant.PAY_CONFIG.get("LTW_PRV_PASSWORD"));
			params.put("signMsg", signMsg);
			log.info("单笔代付查询请求数据："+params);
			String reqData = UniPaySignUtilsCer.createReqString(params);
			String resData = new String(new DataTransUtil().doPost(PayConstant.PAY_CONFIG.get("LTW_DF_QUERY_PAY_URL"), reqData.getBytes("utf-8")),"utf-8");
			log.info("单笔代付查询响应数据："+resData);
			//验签
			Map<String,String> map = UniPaySignUtilsCer.rep2Map(resData,"\\&","\\=");
			if(UniPaySignUtilsCer.merVerify(map, "RSA_SHA256", map.get("signMsg"), PayConstant.PAY_CONFIG.get("LTW_PUB_CERT"),true)){//验签成功
				if("1".equals(map.get("transRst"))){//成功
					rp.status="1";
					rp.respCode="000";
					rp.errorMsg="交易成功";
					try{new PayReceiveAndPayDAO().updatePayReceiveAndPayById(rp);} catch(Exception e){}
					List<PayReceiveAndPay> list = new ArrayList<PayReceiveAndPay>();
					list.add(rp);
					new PayInterfaceOtherService().receivePayNotify(payRequest,list);
					flag = true;
				}else if("3".equals(map.get("transRst"))){//失败
					rp.status="2";
					rp.respCode="-1";
					rp.errorMsg = map.get("transRst");
					try{new PayReceiveAndPayDAO().updatePayReceiveAndPayById(rp);} catch(Exception e){}
					List<PayReceiveAndPay> list = new ArrayList<PayReceiveAndPay>();
					list.add(rp);
					new PayInterfaceOtherService().receivePayNotify(payRequest,list);
					flag = true;
				}else{
					rp.status="2";
					rp.respCode="-1";
					rp.errorMsg=map.get("transRst");
					try{new PayReceiveAndPayDAO().updatePayReceiveAndPayById(rp);} catch(Exception e){}
					List<PayReceiveAndPay> list = new ArrayList<PayReceiveAndPay>();
					list.add(rp);
					new PayInterfaceOtherService().receivePayNotify(payRequest,list);
					flag = true;
				}
			}else throw new Exception("验签失败");
			return flag;
		} catch (Exception e) {
			e.printStackTrace();
			return flag;
		}
	}
}