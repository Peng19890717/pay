package com.pay.merchant.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import util.Tools;

import com.pay.custstl.dao.PayCustStlInfo;
import com.pay.custstl.dao.PayCustStlInfoDAO;
import com.pay.merchant.dao.PayMerchant;
import com.pay.merchant.dao.PayStlPeriodOptLog;
import com.pay.merchant.dao.PayStlPeriodOptLogDAO;

/**
 * Object PAY_STL_PERIOD_OPT_LOG service. 
 * @author Administrator
 *
 */
public class PayStlPeriodOptLogService {
    /**
     * Get records list(json).
     * @return
     */
    public String getPayStlPeriodOptLogList(PayStlPeriodOptLog payStlPeriodOptLog,int page,int rows,String sort,String order){
        try {
            PayStlPeriodOptLogDAO payStlPeriodOptLogDAO = new PayStlPeriodOptLogDAO();
            List list = payStlPeriodOptLogDAO.getPayStlPeriodOptLogList(payStlPeriodOptLog, page, rows, sort, order);
            JSONObject json = new JSONObject();
            json.put("total", String.valueOf(payStlPeriodOptLogDAO.getPayStlPeriodOptLogCount(payStlPeriodOptLog)));
            JSONArray row = new JSONArray();
            for(int i = 0; i<list.size(); i++){
                row.put(i, ((PayStlPeriodOptLog)list.get(i)).toJson());
            }
            json.put("rows", row);
            return json.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
    public void addPayStlPeriodOptLog(HttpServletRequest request,PayMerchant payMerchant,PayCustStlInfo payCustStlInfo) throws Exception {
    	PayStlPeriodOptLogDAO dao = new PayStlPeriodOptLogDAO();
    	PayStlPeriodOptLog payStlPeriodOptLog = new PayStlPeriodOptLog();
    	PayCustStlInfo oldStl = new PayCustStlInfoDAO().getPayCustStlInfoByMerchantId(payMerchant.custId);
    	if(oldStl==null)return;
    	//取得商户余额,商户冻结金额
    	long [] cash = dao.getMerchantCashInfo(payMerchant);
    	//判断交易结算周期是否改变
    	if("D".equals(payCustStlInfo.custSetPeriod)){
    		if(!oldStl.custSetPeriod.equals(payCustStlInfo.custSetPeriod)
	    		||!oldStl.custSetFrey.equals(payCustStlInfo.custSetFrey)){
	    		payStlPeriodOptLog =  new PayStlPeriodOptLog();
	    		payStlPeriodOptLog.id = Tools.getUniqueIdentify();
	    		payStlPeriodOptLog.merNo = payMerchant.custId;
	    		payStlPeriodOptLog.stlType="0";
	    		payStlPeriodOptLog.accBalance=cash[0];
	    		payStlPeriodOptLog.frozenAmt=cash[1];
	    		payStlPeriodOptLog.custSetPeriodOld=oldStl.custSetPeriod;
	    		if("D".equals(oldStl.custSetPeriod))payStlPeriodOptLog.custStlTimeSetOld=oldStl.custSetFrey;
	    		else payStlPeriodOptLog.custStlTimeSetOld=oldStl.custStlTimeSet;
	    		dao.addPayStlPeriodOptLog(payStlPeriodOptLog);
	    	}
    	} else {
	    	if(!oldStl.custSetPeriod.equals(payCustStlInfo.custSetPeriod)
	    		||!oldStl.custStlTimeSet.equals(payCustStlInfo.custStlTimeSet)){
	    		payStlPeriodOptLog =  new PayStlPeriodOptLog();
	    		payStlPeriodOptLog.id = Tools.getUniqueIdentify();
	    		payStlPeriodOptLog.merNo = payMerchant.custId;
	    		payStlPeriodOptLog.stlType="0";
	    		payStlPeriodOptLog.accBalance=cash[0];
	    		payStlPeriodOptLog.frozenAmt=cash[1];
	    		payStlPeriodOptLog.custSetPeriodOld=oldStl.custSetPeriod;
	    		if("D".equals(oldStl.custSetPeriod))payStlPeriodOptLog.custStlTimeSetOld=oldStl.custSetFrey;
	    		else payStlPeriodOptLog.custStlTimeSetOld=oldStl.custStlTimeSet;
	    		dao.addPayStlPeriodOptLog(payStlPeriodOptLog);
	    	}
    	}
    	//判断代理结算周期是否改变
    	if("D".equals(payCustStlInfo.custSetPeriodAgent)){
    		if(!oldStl.custSetPeriodAgent.equals(payCustStlInfo.custSetPeriodAgent)
	    		||!oldStl.custSetFreyAgent.equals(payCustStlInfo.custSetFreyAgent)){
	    		payStlPeriodOptLog =  new PayStlPeriodOptLog();
	    		payStlPeriodOptLog.id = Tools.getUniqueIdentify();
	    		payStlPeriodOptLog.merNo = payMerchant.custId;
	    		payStlPeriodOptLog.stlType="1";
	    		payStlPeriodOptLog.accBalance=cash[0];
	    		payStlPeriodOptLog.frozenAmt=cash[1];
	    		payStlPeriodOptLog.custSetPeriodOld=oldStl.custSetPeriodAgent;
	    		if("D".equals(oldStl.custSetPeriodAgent))payStlPeriodOptLog.custStlTimeSetOld=oldStl.custSetFreyAgent;
	    		else payStlPeriodOptLog.custStlTimeSetOld=oldStl.custStlTimeSetAgent;
	    		dao.addPayStlPeriodOptLog(payStlPeriodOptLog);
	        }
    	} else {
	    	if(!oldStl.custSetPeriodAgent.equals(payCustStlInfo.custSetPeriodAgent)
	    		||!oldStl.custStlTimeSetAgent.equals(payCustStlInfo.custStlTimeSetAgent)){
	    		payStlPeriodOptLog =  new PayStlPeriodOptLog();
	    		payStlPeriodOptLog.id = Tools.getUniqueIdentify();
	    		payStlPeriodOptLog.merNo = payMerchant.custId;
	    		payStlPeriodOptLog.stlType="1";
	    		payStlPeriodOptLog.accBalance=cash[0];
	    		payStlPeriodOptLog.frozenAmt=cash[1];
	    		payStlPeriodOptLog.custSetPeriodOld=oldStl.custSetPeriodAgent;
	    		if("D".equals(oldStl.custSetPeriodAgent))payStlPeriodOptLog.custStlTimeSetOld=oldStl.custSetFreyAgent;
	    		else payStlPeriodOptLog.custStlTimeSetOld=oldStl.custStlTimeSetAgent;
	    		dao.addPayStlPeriodOptLog(payStlPeriodOptLog);
	        }
    	}
    	//判断代收结算周期是否改变
    	if(!oldStl.custSetPeriodDaishou.equals(payCustStlInfo.custSetPeriodDaishou)
    		||!oldStl.custStlTimeSetDaishou.equals(payCustStlInfo.custStlTimeSetDaishou)){
    		payStlPeriodOptLog =  new PayStlPeriodOptLog();
    		payStlPeriodOptLog.id = Tools.getUniqueIdentify();
    		payStlPeriodOptLog.merNo = payMerchant.custId;
    		payStlPeriodOptLog.stlType="2";
    		payStlPeriodOptLog.accBalance=cash[0];
    		payStlPeriodOptLog.frozenAmt=cash[1];
    		payStlPeriodOptLog.custSetPeriodOld=oldStl.custSetPeriodDaishou;
    		payStlPeriodOptLog.custStlTimeSetOld=oldStl.custStlTimeSetDaishou;
    		dao.addPayStlPeriodOptLog(payStlPeriodOptLog);
    	}
    }
}