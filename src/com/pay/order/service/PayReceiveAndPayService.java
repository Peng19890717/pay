package com.pay.order.service;

import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import jxl.CellType;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableCell;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;
import org.json.JSONObject;

import util.DataTransUtil;
import util.JWebConstant;
import util.PayUtil;
import util.Tools;

import com.PayConstant;
import com.pay.bank.dao.PayBank;
import com.pay.cardbin.dao.PayCardBin;
import com.pay.cardbin.service.PayCardBinService;
import com.pay.coopbank.dao.PayCoopBank;
import com.pay.coopbank.service.PayCoopBankService;
import com.pay.merchant.dao.PayMerchant;
import com.pay.merchant.dao.PayMerchantDAO;
import com.pay.merchantinterface.service.BFBPayService;
import com.pay.merchantinterface.service.CJPayService;
import com.pay.merchantinterface.service.CXPayService;
import com.pay.merchantinterface.service.CertUtil;
import com.pay.merchantinterface.service.EjPayService;
import com.pay.merchantinterface.service.GFBPayService;
import com.pay.merchantinterface.service.GHTPayService;
import com.pay.merchantinterface.service.HFPayService;
import com.pay.merchantinterface.service.JDPayService;
import com.pay.merchantinterface.service.KBNEWPayService;
import com.pay.merchantinterface.service.KLFPayService;
import com.pay.merchantinterface.service.LDPayService;
import com.pay.merchantinterface.service.LTWPayService;
import com.pay.merchantinterface.service.MerchantXmlUtil;
import com.pay.merchantinterface.service.PayInterfaceOtherService;
import com.pay.merchantinterface.service.PayInterfaceService;
import com.pay.merchantinterface.service.PayRequest;
import com.pay.merchantinterface.service.ReceivePaySingleTest;
import com.pay.merchantinterface.service.SXFNEWpayService;
import com.pay.merchantinterface.service.SXYPayService;
import com.pay.merchantinterface.service.TFBpayService;
import com.pay.merchantinterface.service.TTFPayService;
import com.pay.merchantinterface.service.WSYMPayService;
import com.pay.merchantinterface.service.XFPayService;
import com.pay.merchantinterface.service.YLPayService;
import com.pay.merchantinterface.service.YLTFPayService;
import com.pay.merchantinterface.service.YSBPayService;
import com.pay.merchantinterface.service.YTZFPayService;
import com.pay.merchantinterface.service.ZFTPayService;
import com.pay.order.dao.PayReceiveAndPay;
import com.pay.order.dao.PayReceiveAndPayDAO;
import com.pay.risk.service.RiskService;

/**
 * Object PAY_RECEIVE_AND_PAY service. 
 * @author Administrator
 *
 */
public class PayReceiveAndPayService {
	private static final Log log = LogFactory.getLog(PayReceiveAndPayService.class);
    /**
     * Get records list(json). 
     * @return
     */
    public String getPayReceiveAndPayList(PayReceiveAndPay payReceiveAndPay,int page,int rows,String sort,String order){
        try {
            PayReceiveAndPayDAO payReceiveAndPayDAO = new PayReceiveAndPayDAO();
            List list = payReceiveAndPayDAO.getPayReceiveAndPayList(payReceiveAndPay, page, rows, sort, order);
            JSONObject json = new JSONObject();
            json.put("total", String.valueOf(payReceiveAndPayDAO.getPayReceiveAndPayCount(payReceiveAndPay)));
            JSONArray row = new JSONArray();
            for(int i = 0; i<list.size(); i++){
                row.put(i, ((PayReceiveAndPay)list.get(i)).toJson());
            }
          //查询总金额
            Long [] money = payReceiveAndPayDAO.getTotalReceiveAndPayMoney(payReceiveAndPay);
            json.put("rows", row);
            json.put("totalReceiveAndPayMoney", String.valueOf(money[0]));
            json.put("totalReceiveAndPayFeeMoney", String.valueOf(money[1]));
            return json.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
    public String getPayReceiveAndPayCheckList(PayReceiveAndPay payReceiveAndPay,int page,int rows,String sort,String order){
    	try {
    		PayReceiveAndPayDAO payReceiveAndPayDAO = new PayReceiveAndPayDAO();
    		List list = payReceiveAndPayDAO.getPayReceiveAndPayCheckList(payReceiveAndPay, page, rows, sort, order);
    		JSONObject json = new JSONObject();
    		json.put("total", String.valueOf(payReceiveAndPayDAO.getPayReceiveAndPayCheckCount(payReceiveAndPay)));
    		JSONArray row = new JSONArray();
    		for(int i = 0; i<list.size(); i++){
    			row.put(i, ((PayReceiveAndPay) list.get(i)).toJson());
    		}
    		json.put("rows", row);
    		return json.toString();
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	return "";
    }
    /**
     * 商户补单
     * @param payReceiveAndPay
     * @throws Exception 
     */
	public void notifyReceiveAndPayMer(PayReceiveAndPay payReceiveAndPay) throws Exception {
		List <PayReceiveAndPay>notifyList = new PayReceiveAndPayDAO().getBatchPayReceiveAndPayById(payReceiveAndPay.id);
		if(notifyList.size()==0)throw new Exception("无记录");
		payReceiveAndPay = notifyList.get(0);
		PayRequest payRequest = new PayRequest();
		payRequest.merchantId=payReceiveAndPay.custId;
		payRequest.tranId=payReceiveAndPay.merTranNo;
		//启动查询线程，查询处理结果、通知商户、更新本地记录
		String notifyXml = new MerchantXmlUtil().receivePayNotifyToXml(payRequest,notifyList);
		log.info("代收付通知======="+payReceiveAndPay.receivePayNotifyUrl+"========\n"+notifyXml);
		try {
			new DataTransUtil().doPost(payReceiveAndPay.receivePayNotifyUrl, CertUtil.createTransStr(notifyXml).getBytes("utf-8"));
		} catch (Exception e) {
			e.printStackTrace();
			log.info(PayUtil.exceptionToString(e));
		}
	}
	/**
	 * 渠道补单
	 * @param payReceiveAndPay
	 * @throws Exception 
	 */
	public void channelReceiveAndPayQuery(PayReceiveAndPay payReceiveAndPay) throws Exception {
		PayReceiveAndPayDAO dao = new PayReceiveAndPayDAO();
		List <PayReceiveAndPay>list = dao.getBatchPayReceiveAndPayById(payReceiveAndPay.id);
		if(list.size()==0)throw new Exception("无记录");
		payReceiveAndPay = list.get(0);
		if(PayConstant.PAY_CONFIG.get("PAY_CHANNEL_CJ").equals(payReceiveAndPay.channelId)){
			//单笔代收付查询
			if("0".equals(payReceiveAndPay.tranType)){
				PayRequest payRequest = new PayRequest();
				
				payRequest.setRespCode("074");
				payRequest.receivePayRes = "-1";
				payRequest.respDesc = "处理中";
				payReceiveAndPay.errorMsg = payRequest.respDesc;
				
				payRequest.receiveAndPaySingle = payReceiveAndPay;
				payRequest.merchantId = payReceiveAndPay.custId;
				payRequest.tranId = payReceiveAndPay.merTranNo;
				payRequest.receivePayType = payReceiveAndPay.type;
				new CJPayService().receivePaySingleQuery(payRequest, payReceiveAndPay);
				payReceiveAndPay.retCode=payRequest.respCode;
				payReceiveAndPay.errorMsg = payRequest.respDesc;
				//处理状态，0初始，1成功，2失败，3查询失败 默认0
				if("000".equals(payRequest.respCode))payReceiveAndPay.status="1";
				else if("-1".equals(payRequest.respCode))payReceiveAndPay.status="2";
				else if("0".equals(payRequest.respCode))payReceiveAndPay.status="0";
				else payReceiveAndPay.status="3";
				dao.updatePayReceiveAndPay(list);
			//批量查询
			} else {
				Map <String,PayReceiveAndPay>searchMap = new HashMap<String,PayReceiveAndPay>();
				for(int i=0; i<list.size(); i++){
					PayReceiveAndPay rp = list.get(i);
					searchMap.put(rp.sn, rp);
				}
				PayRequest payRequest = new PayRequest();
				payRequest.merchantId = payReceiveAndPay.custId;
				payRequest.tranId = payReceiveAndPay.merTranNo;
				payRequest.receivePayType = payReceiveAndPay.type;
				new CJPayService().receivePayBatchQueryForRepair(payRequest, searchMap);
				dao.updatePayReceiveAndPay(list);
			}
		//银生宝单笔代扣查询。
		}else if(PayConstant.PAY_CONFIG.get("PAY_CHANNEL_YSB").equals(payReceiveAndPay.channelId)){
			//单笔代收付查询
			if("0".equals(payReceiveAndPay.tranType)){
				PayRequest payRequest = new PayRequest();

				payRequest.setRespCode("074");
				payRequest.receivePayRes = "-1";
				payRequest.respDesc = "处理中";
				payReceiveAndPay.errorMsg = payRequest.respDesc;
				
				payRequest.receiveAndPaySingle = payReceiveAndPay;
				payRequest.merchantId = payReceiveAndPay.custId;
				payRequest.tranId = payReceiveAndPay.merTranNo;
				payRequest.receivePayType = payReceiveAndPay.type;
				new YSBPayService().receivePaySingleQuery(payRequest, payReceiveAndPay);
				payReceiveAndPay.retCode=payRequest.respCode;
				payReceiveAndPay.errorMsg = payRequest.respDesc;
				//处理状态，0初始，1成功，2失败，3查询失败 默认0
				if("000".equals(payRequest.respCode))payReceiveAndPay.status="1";
				else if("-1".equals(payRequest.respCode))payReceiveAndPay.status="2";
				else if("0".equals(payRequest.respCode))payReceiveAndPay.status="0";
				else payReceiveAndPay.status="3";
				dao.updatePayReceiveAndPay(list);
			//批量查询
			}else{
				throw new Exception("未开通银生宝批量代"+("0".equals(payReceiveAndPay.type)?"收":"付")+"渠道!");
			}
		//易联代扣
		} else if (PayConstant.PAY_CONFIG.get("PAY_CHANNEL_YL").equals(payReceiveAndPay.channelId)){
			//单笔代收付查询
			if("0".equals(payReceiveAndPay.tranType)){
				PayRequest payRequest = new PayRequest();

				payRequest.setRespCode("074");
				payRequest.receivePayRes = "-1";
				payRequest.respDesc = "处理中";
				payReceiveAndPay.errorMsg = payRequest.respDesc;
				
				payRequest.receiveAndPaySingle = payReceiveAndPay;
				payRequest.merchantId = payReceiveAndPay.custId;
				payRequest.tranId = payReceiveAndPay.merTranNo;
				payRequest.receivePayType = payReceiveAndPay.type;
				new YLPayService().receivePaySingleQuery(payRequest, payReceiveAndPay);
				payReceiveAndPay.retCode=payRequest.respCode;
				payReceiveAndPay.errorMsg = payRequest.respDesc;
				//处理状态，0初始，1成功，2失败，3查询失败 默认0
				if("000".equals(payRequest.respCode))payReceiveAndPay.status="1";
				else if("-1".equals(payRequest.respCode))payReceiveAndPay.status="2";
				else if("0".equals(payRequest.respCode))payReceiveAndPay.status="0";
				else payReceiveAndPay.status="3";
				dao.updatePayReceiveAndPay(list);
			//批量查询
			}else{
				throw new Exception("未开通易联批量代"+("0".equals(payReceiveAndPay.type)?"收":"付")+"渠道!");
			}
		//京东的代付
		}else if (PayConstant.PAY_CONFIG.get("PAY_CHANNEL_JD").equals(payReceiveAndPay.channelId)){
			//单笔代收付查询
			if("0".equals(payReceiveAndPay.tranType)){
				PayRequest payRequest = new PayRequest();

				payRequest.setRespCode("074");
				payRequest.receivePayRes = "-1";
				payRequest.respDesc = "处理中";
				payReceiveAndPay.errorMsg = payRequest.respDesc;
				
				payRequest.receiveAndPaySingle = payReceiveAndPay;
				payRequest.merchantId = payReceiveAndPay.custId;
				payRequest.tranId = payReceiveAndPay.merTranNo;
				payRequest.receivePayType = payReceiveAndPay.type;
				new JDPayService().receivePaySingleQuery(payRequest, payReceiveAndPay);
				payReceiveAndPay.retCode=payRequest.respCode;
				payReceiveAndPay.errorMsg = payRequest.respDesc;
				//处理状态，0初始，1成功，2失败，3查询失败 默认0
				if("000".equals(payRequest.respCode))payReceiveAndPay.status="1";
				else if("-1".equals(payRequest.respCode))payReceiveAndPay.status="2";
				else if("0".equals(payRequest.respCode))payReceiveAndPay.status="0";
				else payReceiveAndPay.status="3";
				dao.updatePayReceiveAndPay(list);
			//批量查询
			}else{
				throw new Exception("未开通批量代"+("0".equals(payReceiveAndPay.type)?"收":"付")+"渠道!");
			}
		//创新代付查询。
		}else if (PayConstant.PAY_CONFIG.get("PAY_CHANNEL_CX").equals(payReceiveAndPay.channelId)){
			//单笔代收付查询
			if("0".equals(payReceiveAndPay.tranType)){
				PayRequest payRequest = new PayRequest();

				payRequest.setRespCode("074");
				payRequest.receivePayRes = "-1";
				payRequest.respDesc = "处理中";
				payReceiveAndPay.errorMsg = payRequest.respDesc;
				
				payRequest.receiveAndPaySingle = payReceiveAndPay;
				payRequest.merchantId = payReceiveAndPay.custId;
				payRequest.tranId = payReceiveAndPay.merTranNo;
				payRequest.receivePayType = payReceiveAndPay.type;
				new CXPayService().receivePaySingleQuery(payRequest, payReceiveAndPay);
				payReceiveAndPay.retCode=payRequest.respCode;
				payReceiveAndPay.errorMsg = payRequest.respDesc;
				//处理状态，0初始，1成功，2失败，3查询失败 默认0
				if("000".equals(payRequest.respCode))payReceiveAndPay.status="1";
				else if("-1".equals(payRequest.respCode))payReceiveAndPay.status="2";
				else if("0".equals(payRequest.respCode))payReceiveAndPay.status="0";
				else payReceiveAndPay.status="3";
				dao.updatePayReceiveAndPay(list);
			//批量查询
			}else{
				throw new Exception("未开通批量代"+("0".equals(payReceiveAndPay.type)?"收":"付")+"渠道!");
			}
		//恒丰代收付查询。
		}else if (PayConstant.PAY_CONFIG.get("PAY_CHANNEL_HF").equals(payReceiveAndPay.channelId)){
			//单笔代收付查询
			if("0".equals(payReceiveAndPay.tranType)){
				PayRequest payRequest = new PayRequest();

				payRequest.setRespCode("074");
				payRequest.receivePayRes = "-1";
				payRequest.respDesc = "处理中";
				payReceiveAndPay.errorMsg = payRequest.respDesc;
				
				payRequest.receiveAndPaySingle = payReceiveAndPay;
				payRequest.merchantId = payReceiveAndPay.custId;
				payRequest.tranId = payReceiveAndPay.merTranNo;
				payRequest.receivePayType = payReceiveAndPay.type;
				new HFPayService().receivePaySingleQuery(payRequest, payReceiveAndPay);
				payReceiveAndPay.retCode=payRequest.respCode;
				payReceiveAndPay.errorMsg = payRequest.respDesc;
				//处理状态，0初始，1成功，2失败，3查询失败 默认0
				if("000".equals(payRequest.respCode))payReceiveAndPay.status="1";
				else if("-1".equals(payRequest.respCode))payReceiveAndPay.status="2";
				else if("0".equals(payRequest.respCode))payReceiveAndPay.status="0";
				else payReceiveAndPay.status="3";
				dao.updatePayReceiveAndPay(list);
			//批量查询
			}else{
				throw new Exception("未开通批量代"+("0".equals(payReceiveAndPay.type)?"收":"付")+"渠道!");
			}
		//网上有名代付查询
		}else if (PayConstant.PAY_CONFIG.get("PAY_CHANNEL_WSYM").equals(payReceiveAndPay.channelId)){
			//单笔代收付查询
			if("0".equals(payReceiveAndPay.tranType)){
				PayRequest payRequest = new PayRequest();
				payRequest.setRespCode("074");
				payRequest.receivePayRes = "-1";
				payRequest.respDesc = "处理中";
				payReceiveAndPay.errorMsg = payRequest.respDesc;
				payRequest.receiveAndPaySingle = payReceiveAndPay;
				payRequest.merchantId = payReceiveAndPay.custId;
				payRequest.tranId = payReceiveAndPay.merTranNo;
				payRequest.receivePayType = payReceiveAndPay.type;
				new WSYMPayService().receivePaySingleQuery(payRequest, payReceiveAndPay);
				payReceiveAndPay.retCode=payRequest.respCode;
				payReceiveAndPay.errorMsg = payRequest.respDesc;
				//处理状态，0初始，1成功，2失败，3查询失败 默认0
				if("000".equals(payRequest.respCode))payReceiveAndPay.status="1";
				else if("-1".equals(payRequest.respCode))payReceiveAndPay.status="2";
				else if("0".equals(payRequest.respCode))payReceiveAndPay.status="0";
				else payReceiveAndPay.status="3";
				dao.updatePayReceiveAndPay(list);
			//批量查询
			} else {
				throw new Exception("未开通批量代"+("0".equals(payReceiveAndPay.type)?"收":"付")+"渠道!");
			}
		//支付通代收付查询
		}else if (PayConstant.PAY_CONFIG.get("PAY_CHANNEL_ZFT").equals(payReceiveAndPay.channelId)){
			//单笔代收付查询
			if("0".equals(payReceiveAndPay.tranType)){
				new ZFTPayService().receivePaySingleQuery(payReceiveAndPay);
			}
		//天付宝查询
		}else if (PayConstant.PAY_CONFIG.get("PAY_CHANNEL_TFB").equals(payReceiveAndPay.channelId)){
			//单笔代收付查询
			if("0".equals(payReceiveAndPay.tranType)){
				PayRequest payRequest = new PayRequest();

				payRequest.setRespCode("074");
				payRequest.receivePayRes = "-1";
				payRequest.respDesc = "处理中";
				payReceiveAndPay.errorMsg = payRequest.respDesc;
				
				payRequest.receiveAndPaySingle = payReceiveAndPay;
				payRequest.merchantId = payReceiveAndPay.custId;
				payRequest.tranId = payReceiveAndPay.merTranNo;
				payRequest.receivePayType = payReceiveAndPay.type;
				new TFBpayService().receivePaySingleQuery(payRequest, payReceiveAndPay);
				payReceiveAndPay.retCode=payRequest.respCode;
				payReceiveAndPay.errorMsg = payRequest.respDesc;
				//处理状态，0初始，1成功，2失败，3查询失败 默认0
				if("000".equals(payRequest.respCode))payReceiveAndPay.status="1";
				else if("-1".equals(payRequest.respCode))payReceiveAndPay.status="2";
				else if("0".equals(payRequest.respCode))payReceiveAndPay.status="0";
				dao.updatePayReceiveAndPay(list);
			//批量查询
			} else {
				throw new Exception("未开通批量代"+("0".equals(payReceiveAndPay.type)?"收":"付")+"渠道!");
			}
		//邦付宝代付查询
		}else if (PayConstant.PAY_CONFIG.get("PAY_CHANNEL_BFB").equals(payReceiveAndPay.channelId)){
			//单笔代收付查询
			if("0".equals(payReceiveAndPay.tranType)){
				PayRequest payRequest = new PayRequest();

				payRequest.setRespCode("074");
				payRequest.receivePayRes = "-1";
				payRequest.respDesc = "处理中";
				payReceiveAndPay.errorMsg = payRequest.respDesc;
				
				payRequest.receiveAndPaySingle = payReceiveAndPay;
				payRequest.merchantId = payReceiveAndPay.custId;
				payRequest.tranId = payReceiveAndPay.merTranNo;
				payRequest.receivePayType = payReceiveAndPay.type;
				new BFBPayService().receivePaySingleQuery(payRequest, payReceiveAndPay);
				payReceiveAndPay.retCode=payRequest.respCode;
				payReceiveAndPay.errorMsg = payRequest.respDesc;
				//处理状态，0初始，1成功，2失败，3查询失败 默认0
				if("000".equals(payRequest.respCode))payReceiveAndPay.status="1";
				else if("-1".equals(payRequest.respCode))payReceiveAndPay.status="2";
				else if("0".equals(payRequest.respCode))payReceiveAndPay.status="0";
				dao.updatePayReceiveAndPay(list);
			//批量查询
			} else {
				throw new Exception("未开通批量代"+("0".equals(payReceiveAndPay.type)?"收":"付")+"渠道!");
			}
		//随行付代付查询
		}else if (PayConstant.PAY_CONFIG.get("PAY_CHANNEL_SXF").equals(payReceiveAndPay.channelId)){
			//单笔代收付查询
			if("0".equals(payReceiveAndPay.tranType)){
				PayRequest payRequest = new PayRequest();

				payRequest.setRespCode("074");
				payRequest.receivePayRes = "-1";
				payRequest.respDesc = "处理中";
				payReceiveAndPay.errorMsg = payRequest.respDesc;
				
				payRequest.receiveAndPaySingle = payReceiveAndPay;
				payRequest.merchantId = payReceiveAndPay.custId;
				payRequest.tranId = payReceiveAndPay.merTranNo;
				payRequest.receivePayType = payReceiveAndPay.type;
				//new ZFTPayService().receivePaySingleQueryInfo(payRequest, payReceiveAndPay);
				payReceiveAndPay.retCode=payRequest.respCode;
				payReceiveAndPay.errorMsg = payRequest.respDesc;
				//处理状态，0初始，1成功，2失败，3查询失败 默认0
				if("000".equals(payRequest.respCode))payReceiveAndPay.status="1";
				else if("-1".equals(payRequest.respCode))payReceiveAndPay.status="2";
				else if("0".equals(payRequest.respCode))payReceiveAndPay.status="0";
				dao.updatePayReceiveAndPay(list);
			//批量查询
			}
		//新随行付代付查询
		}else if (PayConstant.PAY_CONFIG.get("PAY_CHANNEL_SXFNEW").equals(payReceiveAndPay.channelId)){
			//单笔代收付查询
			if("0".equals(payReceiveAndPay.tranType)){
				PayRequest payRequest = new PayRequest();

				payRequest.setRespCode("074");
				payRequest.receivePayRes = "-1";
				payRequest.respDesc = "处理中";
				payReceiveAndPay.errorMsg = payRequest.respDesc;
				
				payRequest.receiveAndPaySingle = payReceiveAndPay;
				payRequest.merchantId = payReceiveAndPay.custId;
				payRequest.tranId = payReceiveAndPay.merTranNo;
				payRequest.receivePayType = payReceiveAndPay.type;
				new SXFNEWpayService().receivePaySingleQuery(payRequest, payReceiveAndPay);
				payReceiveAndPay.retCode=payRequest.respCode;
				payReceiveAndPay.errorMsg = payRequest.respDesc;
				//处理状态，0初始，1成功，2失败，3查询失败 默认0
				if("000".equals(payRequest.respCode))payReceiveAndPay.status="1";
				else if("-1".equals(payRequest.respCode))payReceiveAndPay.status="2";
				else if("0".equals(payRequest.respCode))payReceiveAndPay.status="0";
				dao.updatePayReceiveAndPay(list);
			//批量查询
			} else {
				throw new Exception("未开通批量代"+("0".equals(payReceiveAndPay.type)?"收":"付")+"渠道!");
			} 
		//首信易代付查询。
		}else if (PayConstant.PAY_CONFIG.get("PAY_CHANNEL_SXY").equals(payReceiveAndPay.channelId)){
			//单笔代收付查询
			if("0".equals(payReceiveAndPay.tranType)){
				PayRequest payRequest = new PayRequest();

				payRequest.setRespCode("074");
				payRequest.receivePayRes = "-1";
				payRequest.respDesc = "处理中";
				payReceiveAndPay.errorMsg = payRequest.respDesc;
				
				payRequest.receiveAndPaySingle = payReceiveAndPay;
				payRequest.merchantId = payReceiveAndPay.custId;
				payRequest.tranId = payReceiveAndPay.merTranNo;
				payRequest.receivePayType = payReceiveAndPay.type;
				new SXYPayService().receivePaySingleQuery(payRequest, payReceiveAndPay);
				payReceiveAndPay.retCode=payRequest.respCode;
				payReceiveAndPay.errorMsg = payRequest.respDesc;
				//处理状态，0初始，1成功，2失败，3查询失败 默认0
				if("000".equals(payRequest.respCode))payReceiveAndPay.status="1";
				else if("-1".equals(payRequest.respCode))payReceiveAndPay.status="2";
				else if("0".equals(payRequest.respCode))payReceiveAndPay.status="0";
				dao.updatePayReceiveAndPay(list);
			//批量查询
			} else {
				throw new Exception("未开通批量代"+("0".equals(payReceiveAndPay.type)?"收":"付")+"渠道!");
			} 
		//高汇通代付查询
		}else if (PayConstant.PAY_CONFIG.get("PAY_CHANNEL_GHT").equals(payReceiveAndPay.channelId)){
			//单笔代收付查询
			if("0".equals(payReceiveAndPay.tranType)){
				PayRequest payRequest = new PayRequest();

				payRequest.setRespCode("074");
				payRequest.receivePayRes = "-1";
				payRequest.respDesc = "处理中";
				payReceiveAndPay.errorMsg = payRequest.respDesc;
				
				payRequest.receiveAndPaySingle = payReceiveAndPay;
				payRequest.merchantId = payReceiveAndPay.custId;
				payRequest.tranId = payReceiveAndPay.merTranNo;
				payRequest.receivePayType = payReceiveAndPay.type;
				new GHTPayService().receivePaySingleQuery(payRequest, payReceiveAndPay);
				payReceiveAndPay.retCode=payRequest.respCode;
				payReceiveAndPay.errorMsg = payRequest.respDesc;
				//处理状态，0初始，1成功，2失败，3查询失败 默认0
				if("000".equals(payRequest.respCode))payReceiveAndPay.status="1";
				else if("-1".equals(payRequest.respCode))payReceiveAndPay.status="2";
				else if("0".equals(payRequest.respCode))payReceiveAndPay.status="0";
				dao.updatePayReceiveAndPay(list);
			//批量查询
			} else {
				throw new Exception("未开通批量代"+("0".equals(payReceiveAndPay.type)?"收":"付")+"渠道!");
			} 
		//溢+ 代收付查询
		}else if(PayConstant.PAY_CONFIG.get("PAY_CHANNEL_EMAX").equals(payReceiveAndPay.channelId)){
			//单笔代收付查询
			if("0".equals(payReceiveAndPay.tranType)){
				PayRequest payRequest = new PayRequest();

				payRequest.setRespCode("074");
				payRequest.receivePayRes = "-1";
				payRequest.respDesc = "处理中";
				payReceiveAndPay.errorMsg = payRequest.respDesc;
				
				payRequest.receiveAndPaySingle = payReceiveAndPay;
				payRequest.merchantId = payReceiveAndPay.custId;
				payRequest.tranId = payReceiveAndPay.merTranNo;
				payRequest.receivePayType = payReceiveAndPay.type;
				new EjPayService().receivePaySingleQuery(payRequest,payReceiveAndPay);
				payReceiveAndPay.retCode=payRequest.respCode;
				payReceiveAndPay.errorMsg = payRequest.respDesc;
				//处理状态，0初始，1成功，2失败，3查询失败 默认0
				if("000".equals(payRequest.respCode))payReceiveAndPay.status="1";
				else if("-1".equals(payRequest.respCode))payReceiveAndPay.status="2";
				else if("0".equals(payRequest.respCode))payReceiveAndPay.status="0";
				dao.updatePayReceiveAndPay(list);
				//批量查询
			} else {
				throw new Exception("未开通批量代"+("0".equals(payReceiveAndPay.type)?"收":"付")+"渠道!");
			} 
		//亿联通付代付查询
		} else if (PayConstant.PAY_CONFIG.get("PAY_CHANNEL_YLTF").equals(payReceiveAndPay.channelId)){
			//单笔代收付查询
			if("0".equals(payReceiveAndPay.tranType)){
				PayRequest payRequest = new PayRequest();

				payRequest.setRespCode("074");
				payRequest.receivePayRes = "-1";
				payRequest.respDesc = "处理中";
				payReceiveAndPay.errorMsg = payRequest.respDesc;
				
				payRequest.receiveAndPaySingle = payReceiveAndPay;
				payRequest.merchantId = payReceiveAndPay.custId;
				payRequest.tranId = payReceiveAndPay.merTranNo;
				payRequest.receivePayType = payReceiveAndPay.type;
				new YLTFPayService().receivePaySingleQuery(payRequest, payReceiveAndPay);
				payReceiveAndPay.retCode=payRequest.respCode;
				payReceiveAndPay.errorMsg = payRequest.respDesc;
				//处理状态，0初始，1成功，2失败，3查询失败 默认0
				if("000".equals(payRequest.respCode))payReceiveAndPay.status="1";
				else if("-1".equals(payRequest.respCode))payReceiveAndPay.status="2";
				else if("0".equals(payRequest.respCode))payReceiveAndPay.status="0";
				dao.updatePayReceiveAndPay(list);
			//批量查询
			} else {
				throw new Exception("未开通批量代"+("0".equals(payReceiveAndPay.type)?"收":"付")+"渠道!");
			}
		//先锋代收付查询
		}else if (PayConstant.PAY_CONFIG.get("PAY_CHANNEL_XF").equals(payReceiveAndPay.channelId)){
			//单笔代收付查询
			if("0".equals(payReceiveAndPay.tranType)){
				PayRequest payRequest = new PayRequest();

				payRequest.setRespCode("074");
				payRequest.receivePayRes = "-1";
				payRequest.respDesc = "处理中";
				payReceiveAndPay.errorMsg = payRequest.respDesc;
				
				payRequest.receiveAndPaySingle = payReceiveAndPay;
				payRequest.merchantId = payReceiveAndPay.custId;
				payRequest.tranId = payReceiveAndPay.merTranNo;
				payRequest.receivePayType = payReceiveAndPay.type;
				String serviceType = "";
				if("0".equals(payReceiveAndPay.type)){//代收 
					serviceType = "REQ_WITHOIDING_QUERY";
				}else{//代付
					serviceType = "REQ_WITHDRAW_QUERY_BY_ID";
				}
				new XFPayService().receivePaySingleQuery(payRequest, payReceiveAndPay, serviceType);
				payReceiveAndPay.retCode=payRequest.respCode;
				payReceiveAndPay.errorMsg = payRequest.respDesc;
				//处理状态，0初始，1成功，2失败，3查询失败 默认0
				if("000".equals(payRequest.respCode))payReceiveAndPay.status="1";
				else if("-1".equals(payRequest.respCode))payReceiveAndPay.status="2";
				else if("0".equals(payRequest.respCode))payReceiveAndPay.status="0";
				dao.updatePayReceiveAndPay(list);
			//批量查询
			} else {
				throw new Exception("未开通批量代"+("0".equals(payReceiveAndPay.type)?"收":"付")+"渠道!");
			}
		//联动代扣查询
		}else if (PayConstant.PAY_CONFIG.get("PAY_CHANNEL_LD").equals(payReceiveAndPay.channelId)){
			//单笔代收付查询
			if("0".equals(payReceiveAndPay.tranType)){
				PayRequest payRequest = new PayRequest();

				payRequest.setRespCode("074");
				payRequest.receivePayRes = "-1";
				payRequest.respDesc = "处理中";
				payReceiveAndPay.errorMsg = payRequest.respDesc;
				
				payRequest.receiveAndPaySingle = payReceiveAndPay;
				payRequest.merchantId = payReceiveAndPay.custId;
				payRequest.tranId = payReceiveAndPay.merTranNo;
				payRequest.receivePayType = payReceiveAndPay.type;
				if("0".equals(payReceiveAndPay.type)){//代收
					new LDPayService().receivePaySingleQuery(payRequest, payReceiveAndPay,"mer_order_info_query");
				}else if("1".equals(payReceiveAndPay.type)){//代付
					new LDPayService().receivePaySingleQuery(payRequest, payReceiveAndPay,"transfer_query");
				}
				payReceiveAndPay.retCode=payRequest.respCode;
				payReceiveAndPay.errorMsg = payRequest.respDesc;
				//处理状态，0初始，1成功，2失败，3查询失败 默认0
				if("000".equals(payRequest.respCode))payReceiveAndPay.status="1";
				else if("-1".equals(payRequest.respCode))payReceiveAndPay.status="2";
				else if("0".equals(payRequest.respCode))payReceiveAndPay.status="0";
				dao.updatePayReceiveAndPay(list);
			//批量查询
			} else throw new Exception("未开通批量代"+("0".equals(payReceiveAndPay.type)?"收":"付")+"渠道!");
			//统统付代付查询
			} else if (PayConstant.PAY_CONFIG.get("PAY_CHANNEL_TTF").equals(payReceiveAndPay.channelId)){
				//单笔代收付查询
				if("0".equals(payReceiveAndPay.tranType)){
					PayRequest payRequest = new PayRequest();

					payRequest.setRespCode("074");
					payRequest.receivePayRes = "-1";
					payRequest.respDesc = "处理中";
					payReceiveAndPay.errorMsg = payRequest.respDesc;
					
					payRequest.receiveAndPaySingle = payReceiveAndPay;
					payRequest.merchantId = payReceiveAndPay.custId;
					payRequest.tranId = payReceiveAndPay.merTranNo;
					payRequest.receivePayType = payReceiveAndPay.type;
					new TTFPayService().receivePaySingleQuery(payRequest, payReceiveAndPay);
					payReceiveAndPay.retCode=payRequest.respCode;
					payReceiveAndPay.errorMsg = payRequest.respDesc;
					//处理状态，0初始，1成功，2失败，3查询失败 默认0
					if("000".equals(payRequest.respCode))payReceiveAndPay.status="1";
					else if("-1".equals(payRequest.respCode))payReceiveAndPay.status="2";
					else if("0".equals(payRequest.respCode))payReceiveAndPay.status="0";
					dao.updatePayReceiveAndPay(list);
				//批量查询
				} else {
					throw new Exception("未开通批量代"+("0".equals(payReceiveAndPay.type)?"收":"付")+"渠道!");
				}
			//新酷宝 代付查询
			} else if (PayConstant.PAY_CONFIG.get("PAY_CHANNEL_KBNEW").equals(payReceiveAndPay.channelId)){
				//单笔代收付查询
				if("0".equals(payReceiveAndPay.tranType)){
					PayRequest payRequest = new PayRequest();
					payRequest.setRespCode("074");
					payRequest.receivePayRes = "-1";
					payRequest.respDesc = "处理中";
					payReceiveAndPay.errorMsg = payRequest.respDesc;
					
					payRequest.receiveAndPaySingle = payReceiveAndPay;
					payRequest.merchantId = payReceiveAndPay.custId;
					payRequest.tranId = payReceiveAndPay.merTranNo;
					payRequest.receivePayType = payReceiveAndPay.type;
					new KBNEWPayService().receivePaySingleQuery(payRequest, payReceiveAndPay);
					payReceiveAndPay.retCode=payRequest.respCode;
					payReceiveAndPay.errorMsg = payRequest.respDesc;
					//处理状态，0初始，1成功，2失败，3查询失败 默认0
					if("000".equals(payRequest.respCode))payReceiveAndPay.status="1";
					else if("-1".equals(payRequest.respCode))payReceiveAndPay.status="2";
					else if("0".equals(payRequest.respCode))payReceiveAndPay.status="0";
					dao.updatePayReceiveAndPay(list);
					//批量查询
				} else {
					throw new Exception("未开通批量代"+("0".equals(payReceiveAndPay.type)?"收":"付")+"渠道!");
				}
				//快乐付代付查询
				}else if (PayConstant.PAY_CONFIG.get("PAY_CHANNEL_KLF").equals(payReceiveAndPay.channelId)){
					//单笔代收付查询
					if("0".equals(payReceiveAndPay.tranType)){
						PayRequest payRequest = new PayRequest();
						payRequest.setRespCode("074");
						payRequest.receivePayRes = "-1";
						payRequest.respDesc = "处理中";
						payReceiveAndPay.errorMsg = payRequest.respDesc;						
						payRequest.receiveAndPaySingle = payReceiveAndPay;
						payRequest.merchantId = payReceiveAndPay.custId;
						payRequest.tranId = payReceiveAndPay.merTranNo;
						payRequest.receivePayType = payReceiveAndPay.type;
						new KLFPayService().receivePaySingleQuery(payRequest, payReceiveAndPay);
						payReceiveAndPay.retCode=payRequest.respCode;
						payReceiveAndPay.errorMsg = payRequest.respDesc;
						//处理状态，0初始，1成功，2失败，3查询失败 默认0
						if("000".equals(payRequest.respCode))payReceiveAndPay.status="1";
						else if("-1".equals(payRequest.respCode))payReceiveAndPay.status="2";
						else if("0".equals(payRequest.respCode))payReceiveAndPay.status="0";
						dao.updatePayReceiveAndPay(list);
					//批量查询
					} else {
						throw new Exception("未开通批量代"+("0".equals(payReceiveAndPay.type)?"收":"付")+"渠道!");
					}
			//易通支付代付查询
			}else if (PayConstant.PAY_CONFIG.get("PAY_CHANNEL_YTZF").equals(payReceiveAndPay.channelId)){
				//单笔代收付查询
				if("0".equals(payReceiveAndPay.tranType)){
					PayRequest payRequest = new PayRequest();
					payRequest.setRespCode("074");
					payRequest.receivePayRes = "-1";
					payRequest.respDesc = "处理中";
					payReceiveAndPay.errorMsg = payRequest.respDesc;						
					payRequest.receiveAndPaySingle = payReceiveAndPay;
					payRequest.merchantId = payReceiveAndPay.custId;
					payRequest.tranId = payReceiveAndPay.merTranNo;
					payRequest.receivePayType = payReceiveAndPay.type;
					new YTZFPayService().receivePaySingleQuery(payRequest, payReceiveAndPay);
					payReceiveAndPay.retCode=payRequest.respCode;
					payReceiveAndPay.errorMsg = payRequest.respDesc;
					//处理状态，0初始，1成功，2失败，3查询失败 默认0
					if("000".equals(payRequest.respCode))payReceiveAndPay.status="1";
					else if("-1".equals(payRequest.respCode))payReceiveAndPay.status="2";
					else if("0".equals(payRequest.respCode))payReceiveAndPay.status="0";
					dao.updatePayReceiveAndPay(list);
				//批量查询
				} else {
					throw new Exception("未开通批量代"+("0".equals(payReceiveAndPay.type)?"收":"付")+"渠道!");
				}
		//国付宝代付
		}else if (PayConstant.PAY_CONFIG.get("PAY_CHANNEL_GFB").equals(payReceiveAndPay.channelId)){
			//单笔代收付查询
			if("0".equals(payReceiveAndPay.tranType)){
				new GFBPayService().receivePaySingleQuery(payReceiveAndPay);
			}
		//联通沃代付
		}else if (PayConstant.PAY_CONFIG.get("PAY_CHANNEL_LTW").equals(payReceiveAndPay.channelId)){
			//单笔代收付查询
			if("0".equals(payReceiveAndPay.tranType)){
				PayRequest payRequest = new PayRequest();

				payRequest.setRespCode("074");
				payRequest.receivePayRes = "-1";
				payRequest.respDesc = "处理中";
				payReceiveAndPay.errorMsg = payRequest.respDesc;
				
				payRequest.receiveAndPaySingle = payReceiveAndPay;
				payRequest.merchantId = payReceiveAndPay.custId;
				payRequest.tranId = payReceiveAndPay.merTranNo;
				payRequest.receivePayType = payReceiveAndPay.type;
				new LTWPayService().receivePaySingleQuery(payRequest, payReceiveAndPay);
				payReceiveAndPay.retCode=payRequest.respCode;
				payReceiveAndPay.errorMsg = payRequest.respDesc;
				//处理状态，0初始，1成功，2失败，3查询失败 默认0
				if("000".equals(payRequest.respCode))payReceiveAndPay.status="1";
				else if("-1".equals(payRequest.respCode))payReceiveAndPay.status="2";
				else if("0".equals(payRequest.respCode))payReceiveAndPay.status="0";
				dao.updatePayReceiveAndPay(list);
			//批量查询
			} else throw new Exception("未开通批量代"+("0".equals(payReceiveAndPay.type)?"收":"付")+"渠道!");
			//统统付代付查询
			} else throw new Exception("未知的代收付渠道");
	}
	public String merchantFaceRAP(HttpServletRequest request) throws Exception {
		String custId = request.getParameter("custId");
		String tranType = request.getParameter("tranType");
		String transferInfo = request.getParameter("transferInfo");

		//检查商户是否支持此服务
		PayMerchant merchant = new PayMerchantDAO().selectByMerchantDetail(custId);
		//解析代收付信息
		String [] ts = Tools.split(transferInfo,"_;_");
		int max = 20;
		try {max = Integer.parseInt(PayConstant.PAY_CONFIG.get("PAY_MERCHANT_FACE_RAP_MAX_COUNT"));} catch (Exception e) {}
		if(ts.length>max) throw new Exception("代收付每批次不超过"+max+"个");
//		Map<String,String> mapTmp = new HashMap<String,String>();
		//代收付类型 0代收 1代付
		if("0".equals(tranType)) {//代收：账号1_,_A_,_身份证号1_,_账号名称1_,_金额（元）_,_手机号1_,_备注1_;_...
			if("1".equals(merchant.payWaySupported.substring(4,5))) throw new Exception("不支持的服务类型（代收）");
			List<PayRequest> list = new ArrayList<PayRequest>();
			Date time = new Date();
			//验证请求，封装请求 
			for(int i=0; i<ts.length; i++){
				String [] es = Tools.split(ts[i],"_,_");
//				if(mapTmp.get(es[0])!=null)throw new Exception("账号【"+es[0]+"】有多条记录，每个账号必须一条");
//				mapTmp.put(es[0],"");
				checkReceive(es,i);
				PayCardBin cardBin = PayCardBinService.getCardBinByCardNo(es[0]);
				if(cardBin==null)throw new Exception("账号【"+es[0]+"】不存在");
				PayBank bank = PayCardBinService.BANK_NAME_MAP.get(cardBin.bankName);
				PayRequest payRequest = new PayRequest();
				payRequest.merchant = merchant;
				payRequest.application="ReceivePay";
				payRequest.merchantId=custId;
				payRequest.accType = cardBin.cardType;//账户类型
				payRequest.accNo = es[0];
				payRequest.cardNo = payRequest.accNo ;
				payRequest.credentialType="01";
				payRequest.credentialNo=es[2];
				payRequest.accName = es[3];
				payRequest.receivePayType = "0";//代收
				payRequest.tranId = Tools.getUniqueIdentify();
				payRequest.accountProp = "0";
				payRequest.bankGeneralName = cardBin.bankName;
				payRequest.bankName = cardBin.bankName;
				payRequest.bankId = bank.bankNo;
				payRequest.protocolNo="";//?
				payRequest.amount = String.valueOf((long)(Double.parseDouble(es[4])*100f));
				payRequest.merchantOrderAmt = payRequest.amount;
				payRequest.tel = es[5];
				payRequest.userMobileNo = payRequest.tel;
				payRequest.summary=es[6];
				payRequest.timestamp=new SimpleDateFormat("yyyyMMddHHmmss").format(time);
				list.add(payRequest);
			}
			//风控检查========开始
			Object [] obj = new Object[]{PayConstant.CUST_TYPE_MERCHANT,custId,list,time};
			String msg = new RiskService().checkMerchantLimit(obj,custId,PayConstant.TRAN_TYPE_DS);
			//违反风控规则
			if(msg!=null)throw new Exception(msg);
			//风控检查========结束
			//发送请求，多线程
			for(int i=0; i<list.size(); i++)new BatchReceiveSender(list.get(i)).start();
		} else if("1".equals(tranType)) {//代付：账号_,_AB_,_收款银行_,_身份证号_,_账号名称_,_金额_,_手机号_,_备注_;_...
			if("1".equals(merchant.payWaySupported.substring(5,6))) throw new Exception("不支持的服务类型（代付）");
			//检查是否开通代付
			if("1".equals(PayConstant.PAY_CONFIG.get("PAY_IS_OPEN")) && !new PayReceiveAndPayDAO().busIsOpen(custId, "16"))throw new Exception(
					PayConstant.PAY_CONFIG.get("PAY_IS_CLOSE_PROMPT")==null?"代付已关闭":PayConstant.PAY_CONFIG.get("PAY_IS_CLOSE_PROMPT"));
			PayRequest payRequest = new PayRequest();
			payRequest.merchant = merchant;
			payRequest.application="ReceivePayBatch";
			payRequest.merchantId=custId;
			payRequest.tranId = Tools.getUniqueIdentify();
			payRequest.receivePayType = "1";//代付
			
			//代付时间段判断
			if(!PayInterfaceService.checkPayTime(payRequest))throw new Exception("代付时间非法(merchant-face)");
			
			payRequest.timestamp=new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
			for(int i=0; i<ts.length; i++){
				String [] es = Tools.split(ts[i],"_,_");
//				if(mapTmp.get(es[0])!=null)throw new Exception("账号【"+es[0]+"】有多条记录，每个账号必须一条");
//				mapTmp.put(es[0],"");
				checkPay(es,i);
				PayBank bank = PayCardBinService.BANK_NAME_MAP.get(es[2]);
				PayReceiveAndPay rp = new PayReceiveAndPay();
				rp.id=Tools.getUniqueIdentify();
				rp.custId=payRequest.merchantId;
				rp.merTranNo=payRequest.tranId;
				rp.channelTranNo=payRequest.merchantId+"_"+payRequest.tranId;
				rp.type="1";
				rp.timeliness="0";
				rp.sn=Tools.getUniqueIdentify();
				rp.amount = new BigDecimal(es[5]).multiply(new BigDecimal("100")).longValue();
				rp.subMerchantId="";
				if("A".equals(es[1])){//对私
					PayCardBin cardBin = PayCardBinService.getCardBinByCardNo(es[0]);
					if(cardBin==null)throw new Exception("账号【"+es[0]+"】非法");
					if(PayCardBinService.BANK_NAME_MAP.get(cardBin.bankName)==null)throw new Exception("账号【"+es[0]+"】非法");
					rp.accountProp="0";
					rp.accountType=cardBin.cardType;
					rp.accType=rp.accountType;
					rp.credentialType="01";
		    		rp.credentialNo=es[3];
		    		rp.bankGeneralName = cardBin.bankName;
		    		rp.bankCode=PayCardBinService.BANK_NAME_MAP.get(cardBin.bankName).bankNo;
		    		rp.idType=rp.credentialType;
		    		rp.certId=rp.credentialNo;
		    		rp.tel = es[6];
				} else if("B".equals(es[1])){//对公
					rp.accountProp="4";
					rp.accountType="";
					rp.accType=rp.accountType;
					rp.bankGeneralName = es[2];
					rp.bankCode=bank.bankNo;
				}
	    		rp.accountNo=es[0];
	    		rp.accNo=rp.accountNo;
	    		rp.accountName=es[4];
	    		rp.accName=rp.accountName;
	    		rp.bankId=rp.bankCode;
	    		rp.bankName = rp.bankGeneralName;
	    		rp.summary = es[7];
	    		payRequest.accountProp=rp.accountProp;
				payRequest.receivePayList.add(rp);
			}
			//风控检查========开始
			Object [] obj = new Object[]{PayConstant.CUST_TYPE_MERCHANT,custId,payRequest.receivePayList,new Date()};
			String msg = new RiskService().checkMerchantLimit(obj,custId,PayConstant.TRAN_TYPE_DF);
			//违反风控规则
			if(msg!=null)throw new Exception(msg);
			//风控检查========结束
			PayInterfaceOtherService otherService = new PayInterfaceOtherService();
			if("0".equals(JWebConstant.SYS_CONFIG.get("DEBUG"))){
				otherService.addPayReceiveAndPayBatch(payRequest);
				otherService.checkMerchantAccountAmt(payRequest, payRequest.receivePayList);
        		//通知商户
        		new ReceivePaySingleTest(payRequest,payRequest.receivePayList).start();
        		payRequest.responseStr = CertUtil.createTransStr(new MerchantXmlUtil().receivePayToXml(payRequest));
        	} else {
        		int N=10;
    			try {N=Integer.parseInt(PayConstant.PAY_CONFIG.get("RECEIVE_PAY_SIN_MUL_COUNT"));} catch (Exception e) {}
        		if(ts.length>N)otherService.receivePayBatch(payRequest);//走批量代付
        		else {//走多次单笔代付
        			Date time = new Date();
        			//验证请求，封装请求
        			for(int i=0; i<ts.length; i++){
        				//代付：账号_,_AB_,_收款银行_,_身份证号_,_账号名称_,_金额_,_手机号_,_备注_;_...
        				//对公： 91080154800004751_,_B_,_上海浦东发展银行_,__,_北京云财富金融服务外包股份有限公司_,_2.0_,__,_支付通公户测试
        				String [] es = Tools.split(ts[i],"_,_");
        				PayCardBin cardBin = PayCardBinService.getCardBinByCardNo(es[0]);
        				if("A".equals(es[1])&&cardBin==null)throw new Exception("账号【"+es[0]+"】不存在");
        				PayBank bank = PayCardBinService.BANK_NAME_MAP.get("A".equals(es[1])?cardBin.bankName:es[2]);
        				payRequest = new PayRequest();
        				payRequest.merchant = merchant;
        				payRequest.application="ReceivePay";
        				payRequest.tranId = Tools.getUniqueIdentify();
        				payRequest.receivePayType = "1";//代收
        				payRequest.timestamp=new SimpleDateFormat("yyyyMMddHHmmss").format(time);
        				if("A".equals(es[1])) payRequest.accountProp = "0";//对私
        				else if("B".equals(es[1])) payRequest.accountProp = "4";//对公
        				payRequest.bankGeneralName = "A".equals(es[1])?cardBin.bankName:es[2];
        				payRequest.bankName = "A".equals(es[1])?cardBin.bankName:es[2];
        				payRequest.bankId = bank.bankNo;
        				payRequest.accNo = es[0];
        				payRequest.cardNo = payRequest.accNo ;
        				payRequest.accName = es[4];
        				payRequest.amount = String.valueOf(new BigDecimal(es[5]).multiply(new BigDecimal("100")).longValue());
        				payRequest.merchantOrderAmt = payRequest.amount;
        				payRequest.tel = es[6];
        				payRequest.userMobileNo = payRequest.tel;
        				payRequest.credentialType="01";
        				payRequest.credentialNo=es[3];
        				payRequest.summary=es[7];
        				payRequest.merchantId=custId;
        				payRequest.accType = "A".equals(es[1])?cardBin.cardType:"";//账户类型
        				new BatchPaySender(payRequest).start();
        			}
        		}
        	}
		} else throw new Exception("未知的交易类型（代收代付）");
		return "{\"resCode\":\"000\",\"resMsg\":\""+PayConstant.RESP_CODE_DESC.get("000")+"\"}";
	}
	private void checkReceive(String [] es,int i) throws Exception{
		//账号1_,_A_,_身份证号1_,_账号名称1_,_金额（元）_,_手机号1_,_备注1
		if(es.length != 7)throw new Exception("第"+(i+1)+"条记录缺少必要信息");
		if(es[0].trim().length()<10||es[0].trim().length()>30)throw new Exception("账号【"+es[0]+"】非法");
		if(!"A".equalsIgnoreCase(es[1].trim())&&!"B".equalsIgnoreCase(es[1].trim()))throw new Exception("对公/对私类型非法(账号"+es[0]+")");
		if(es[2].trim().length()!=15&&es[2].trim().length()!=18)throw new Exception("身份证号【"+es[2]+"】非法(账号"+es[0]+")");
		if(es[3].trim().length()<1||es[3].trim().length()>20)throw new Exception("账户名称【"+es[3]+"】非法(账号"+es[0]+")");
		try {
			if(Float.parseFloat(es[4]) <= 0 || Float.parseFloat(es[4]) > 10000000)throw new Exception();
		} catch (Exception e) {throw new Exception("金额非法(账号"+es[0]+")");}
		if(es[5].trim().length() != 11 || !es[5].startsWith("1"))throw new Exception("手机号【"+es[5]+"】非法(账号"+es[0]+")");
		if(es[6].length()>10)throw new Exception("备注内容超过100个字符(账号"+es[0]+")");
	}
	private void checkPay(String [] es,int i) throws Exception{
		//账号_,_AB_,_收款银行_,_身份证号_,_	账号名称_,_金额_,_手机号_,_备注_;_...
		if(es.length != 8)throw new Exception("第"+(i+1)+"条记录缺少必要信息");
		if(es[0].trim().length()<10||es[0].trim().length()>30)throw new Exception("账号【"+es[0]+"】非法");
		if(!"A".equalsIgnoreCase(es[1].trim())&&!"B".equalsIgnoreCase(es[1].trim()))throw new Exception("对公/对私类型非法(账号"+es[0]+")");
		if("B".equalsIgnoreCase(es[1].trim())&&PayCardBinService.BANK_NAME_MAP.get(es[2].trim())==null)throw new Exception("银行名称【"+es[2]+"】非法(账号"+es[0]+")");
		if("A".equalsIgnoreCase(es[1].trim())){//对私
			if(es[3].trim().length()!=15&&es[3].trim().length()!=18)throw new Exception("身份证号【"+es[3]+"】非法(账号"+es[0]+")");
			if(es[6].trim().length() != 11 || !es[6].startsWith("1"))throw new Exception("手机号【"+es[6]+"】非法(账号"+es[0]+")");
		}
		if(es[4].trim().length()==0||es[4].trim().length()>100)throw new Exception("账户名称【"+es[4]+"】非法(账号"+es[0]+")");
		try {
			if(Float.parseFloat(es[5]) <= 0 || Float.parseFloat(es[5]) > 10000000)throw new Exception();
		} catch (Exception e) {throw new Exception("金额非法(账号"+es[0]+")");}
		if(es[7].length()>100)throw new Exception("备注内容超过100个字符(账号"+es[0]+")");
	}
	/**
	 * 导出代收付excel列表
	 * @param payReceiveAndPay
	 * @return
	 */
	public byte[] exportExcelForPayReceiveAndPayList(
			PayReceiveAndPay payReceiveAndPay) {
		PayReceiveAndPayDAO receiveAndPayDAO = new PayReceiveAndPayDAO();
    	String randomName = Tools.getUniqueIdentify();
    	//临时目录
    	File tmpFile = new File(JWebConstant.APP_PATH+"/dat/download/"+randomName);
    	tmpFile.mkdir();
    	//文件模板名称
    	String templetName ="payReceiveAndPay";
    	//模板文件
    	File templetFile = new File(JWebConstant.APP_PATH+"/templet/"+templetName+".xls");
    	log.info(" read templet file:"+JWebConstant.APP_PATH+"/templet/"+templetName+".xls");
    	//每个Excel最多条数
    	long excelRecordCount = 30000;
    	int fileNum = 0;
        try {
        	List list = new ArrayList();
        	long step = 0;
        	//取得业务数据
        	list = receiveAndPayDAO.getPayReceiveAndPayList(payReceiveAndPay,step,step+excelRecordCount);
        	while(list.size()==excelRecordCount){
        		//加入Excel
        		writePayReceiveAndPayListToExcel(list,templetFile,new File(tmpFile.getAbsolutePath()+"/"+(fileNum++)+".xls"));
        		step = step + excelRecordCount;
        		//取得业务数据
        		list = receiveAndPayDAO.getPayReceiveAndPayList(payReceiveAndPay,step,step+excelRecordCount);
        	}
        	//加入结尾的记录到Excel
        	if(list.size()<excelRecordCount){
        		writePayReceiveAndPayListToExcel(list,templetFile,new File(tmpFile.getAbsolutePath()+"/"+(fileNum++)+".xls"));
        	}
        	File zipfile=new File(JWebConstant.APP_PATH+"/dat/download/"+
        		new SimpleDateFormat("yyyy-MM-dd").format(new Date())+"_"+randomName+".zip");
        	File srcfile[] = new File[fileNum];
        	for(int i=0; i<srcfile.length; i++)srcfile[i] = new File(tmpFile.getAbsolutePath()+"/"+(i)+".xls");
        	Tools.zipFiles(srcfile, zipfile);
			FileInputStream fis = new FileInputStream(zipfile);
			byte [] b = new byte[fis.available()]; 
			fis.read(b);
			fis.close();
			Tools.deleteFile(tmpFile);
			Tools.deleteFile(zipfile);
			return b;
        } catch (Exception e) {
            e.printStackTrace();
        }
		return null;
	}
	private void writePayReceiveAndPayListToExcel(List list, File templetFile,
			File tmpFile) {
		Workbook rw = null;
		WritableWorkbook wwb = null; 
		WritableSheet ws = null;
		try {
			if (!Tools.copy(templetFile.getAbsolutePath(), tmpFile.getAbsolutePath())) return;
			rw = jxl.Workbook.getWorkbook(tmpFile);
			wwb = Workbook.createWorkbook(tmpFile, rw);
			ws = wwb.getSheet(0);
			writeData(list, ws);
			wwb.write();
			wwb.close();
			rw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	/**
	 * 循环输入数据
	 * @param list
	 * @param ws
	 */
	private void writeData(List list, WritableSheet ws) {
  		for (int i=0; i < list.size(); i++) {
  			PayReceiveAndPay payReceiveAndPay = (PayReceiveAndPay)list.get(i);
  			setCellValue(ws, 0, i + 1,payReceiveAndPay.id);
  			if("0".equals(payReceiveAndPay.type)){
				setCellValue(ws, 1, i + 1,"代收");
			} else if("1".equals(payReceiveAndPay.type)){
				setCellValue(ws, 1, i + 1,"代付");
			}			
  			if("0".equals(payReceiveAndPay.tranType)){
  				setCellValue(ws, 2, i + 1,"单笔");
			} else if("1".equals(payReceiveAndPay.tranType)){
				setCellValue(ws, 2, i + 1,"批量");
			} 
  			if(payReceiveAndPay.custId != null && !"".equals(payReceiveAndPay.custId))setCellValue(ws, 3, i + 1,payReceiveAndPay.custId);
  			if(payReceiveAndPay.merTranNo != null && !"".equals(payReceiveAndPay.merTranNo))setCellValue(ws, 4, i + 1,payReceiveAndPay.merTranNo);
  			if(payReceiveAndPay.sn != null && !"".equals(payReceiveAndPay.sn))setCellValue(ws, 5, i + 1,payReceiveAndPay.sn);
  			if(payReceiveAndPay.accountNo != null && !"".equals(payReceiveAndPay.accountNo))setCellValue(ws, 6, i + 1,payReceiveAndPay.accountNo);
  			if(payReceiveAndPay.accountName != null && !"".equals(payReceiveAndPay.accountName))setCellValue(ws, 7, i + 1,payReceiveAndPay.accountName);
  			if(payReceiveAndPay.certId != null && !"".equals(payReceiveAndPay.certId))setCellValue(ws, 8, i + 1,payReceiveAndPay.certId);
  			if(payReceiveAndPay.tel != null && !"".equals(payReceiveAndPay.tel))setCellValue(ws, 9, i + 1,payReceiveAndPay.tel);
  			
  			setCellValue(ws, 10, i + 1," - ");
  			
  			if("0".equals(payReceiveAndPay.status)){
  				setCellValue(ws, 11, i + 1,"初始");
			} else if("1".equals(payReceiveAndPay.status)){
				setCellValue(ws, 11, i + 1,"成功");
			} else if("2".equals(payReceiveAndPay.status)){
				setCellValue(ws, 11, i + 1,"失败");
			}
  			if(payReceiveAndPay.errorMsg != null && !"".equals(payReceiveAndPay.errorMsg))setCellValue(ws, 12, i + 1,payReceiveAndPay.errorMsg);
  			if(payReceiveAndPay.amount != null)setCellValue(ws, 13, i + 1,String.format("%.2f",(float)payReceiveAndPay.amount*0.01));
  			
  			if("0".equals(payReceiveAndPay.bussFromType)) {
  				setCellValue(ws, 14, i + 1,"代收付");
			}else if("1".equals(payReceiveAndPay.bussFromType)) {
				setCellValue(ws, 14, i + 1,"快捷");
			}
  			if(payReceiveAndPay.createTime != null)setCellValue(ws, 15, i + 1,new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payReceiveAndPay.createTime));
  			if(payReceiveAndPay.completeTime != null)setCellValue(ws, 16, i + 1,new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payReceiveAndPay.completeTime));
  			if(payReceiveAndPay.fee != null)setCellValue(ws, 17, i + 1,String.format("%.2f",(float)payReceiveAndPay.fee*0.01));
  			if(payReceiveAndPay.feeChannel != null)setCellValue(ws, 18, i + 1,String.format("%.2f",(float)payReceiveAndPay.feeChannel*0.01));
  			if(payReceiveAndPay.summary != null && !"".equals(payReceiveAndPay.summary))setCellValue(ws, 19, i + 1,payReceiveAndPay.summary);
  			PayCoopBank c = PayCoopBankService.CHANNEL_MAP_ALL.get(payReceiveAndPay.channelId);
  			if(payReceiveAndPay.channelId != null && !"".equals(payReceiveAndPay.channelId))setCellValue(ws, 20, i + 1,c==null?"":c.getBankName());
  		}
		
	}
	private void setCellValue(WritableSheet writeSheet, int x, int y, String value) {
		try {
			WritableCell cell = writeSheet.getWritableCell(x, y);
			if (cell.getType() == CellType.EMPTY) {
				Label write = new Label(x, y, value );
				writeSheet.addCell(write);
			} else {
				Label write = (Label) cell;
				write.setString(value);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 对代收付进行结算
	 * @param id
	 * @throws Exception 
	 */
	public void settlementReceiveAndPay(String id) throws Exception {
		new PayReceiveAndPayDAO().settlementReceiveAndPay(id);
	}
	/**
	 * 对代收付进行结算
	 * @param id
	 * @throws Exception 
	 */
	public void returnReceiveAndPayAcc(String id) throws Exception {
		new PayReceiveAndPayDAO().returnReceiveAndPayAcc(id);
	}
}
class BatchReceiveSender extends Thread {
	private PayRequest payRequest;
	public BatchReceiveSender(PayRequest payRequest){
		this.payRequest = payRequest;
	}
	public void run(){
		try {
			PayInterfaceOtherService otherService = new PayInterfaceOtherService();
			//测试模式
			if("0".equals(JWebConstant.SYS_CONFIG.get("DEBUG"))){
        		payRequest.timestamp=new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        		payRequest.setRespInfo("000");
        		otherService.getRPChannel(payRequest, Long.parseLong(payRequest.amount));
        		PayReceiveAndPay payReceiveAndPay = otherService.addPayReceiveAndPaySingle(payRequest,"0");//代收付产生类型，0代收付，1快捷包装代收付
        		//通知商户
        		List<PayReceiveAndPay> notifyList = new ArrayList<PayReceiveAndPay>();
        		notifyList.add(payReceiveAndPay);
        		otherService.checkMerchantAccountAmt(payRequest,notifyList);
        		payReceiveAndPay.setRetCode("000");
        		new ReceivePaySingleTest(payRequest,notifyList).start();
        		payRequest.responseStr = CertUtil.createTransStr(new MerchantXmlUtil().receivePayToXml(payRequest));
        	} else otherService.receivePaySingle(payRequest);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
class BatchPaySender extends Thread {
	private PayRequest payRequest;
	public BatchPaySender(PayRequest payRequest){
		this.payRequest = payRequest;
	}
	public void run(){
		try {
			new PayInterfaceOtherService().receivePaySingle(payRequest);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}