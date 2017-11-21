package com.pay.complain.service;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;
import org.json.JSONObject;

import util.PayUtil;
import util.Tools;

import com.jweb.dao.JWebUser;
import com.jweb.service.TransactionService;
import com.pay.accprofile.dao.PayAccProfileFrozen;
import com.pay.accprofile.service.PayAccProfileFrozenService;
import com.pay.adjustaccount.dao.PayAdjustAccountCash;
import com.pay.adjustaccount.service.PayAdjustAccountCashService;
import com.pay.complain.dao.PayCustComplain;
import com.pay.complain.dao.PayCustComplainDAO;
import com.pay.merchant.service.PayMerchantService;

/**
 * Object PAY_CUST_COMPLAIN service. 
 * @author Administrator
 *
 */
public class PayCustComplainService extends TransactionService{
	private static final Log log = LogFactory.getLog(TransactionService.class);
    /**
     * Get records list(json).
     * @return
     */
    public String getPayCustComplainList(PayCustComplain payCustComplain,int page,int rows,String sort,String order){
        try {
            PayCustComplainDAO payCustComplainDAO = new PayCustComplainDAO();
            List <PayCustComplain>list = payCustComplainDAO.getPayCustComplainList(payCustComplain, page, rows, sort, order);
            JSONObject json = new JSONObject();
            json.put("total", String.valueOf(payCustComplainDAO.getPayCustComplainCount(payCustComplain)));
            JSONArray row = new JSONArray();
            for(int i = 0; i<list.size(); i++){
                row.put(i, list.get(i).toJson());
            }
            json.put("rows", row);
            return json.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
    /**
     * add PayCustComplain
     * @param payCustComplain
     * @throws Exception
     */
    public void addPayCustComplain(PayCustComplain payCustComplain,JWebUser user) throws Exception {
    	payCustComplain.optTime = new Date();
    	//冻结处理
    	if("0".equals(payCustComplain.isFrozenFlag)){
	    	PayAccProfileFrozen payAccProfileFrozen = new PayAccProfileFrozen();
	    	payAccProfileFrozen.accType="1";
	    	payAccProfileFrozen.accNo=payCustComplain.custId;
	    	payAccProfileFrozen.amt = payCustComplain.frozenAmt;
	    	payAccProfileFrozen.setId(Tools.getUniqueIdentify());
	        payAccProfileFrozen.setCurAmt(payAccProfileFrozen.getAmt());
	        payAccProfileFrozen.setOptUser(user.id);
	        payAccProfileFrozen.setUpdateTime(new Date());
	        payAccProfileFrozen.setStatus("0");
	        payCustComplain.accFrozenId = payAccProfileFrozen.id;
	        payAccProfileFrozen.remark="投诉冻结，商户号"+payCustComplain.custId+",投诉编号"+payCustComplain.id;
	        if(new PayMerchantService().detailPayMerchantByCustId(payAccProfileFrozen.getAccNo())==null)throw new Exception("商户编号不存在！");
	        new PayAccProfileFrozenService().addPayAccProfileFrozen(payAccProfileFrozen);
    	}
        new PayCustComplainDAO().addPayCustComplain(payCustComplain);
    }
    /**
     * update PayCustComplain
     * @param payCustComplain
     * @throws Exception
     */
    public void updatePayCustComplain(PayCustComplain payCustComplain,HttpServletRequest request,JWebUser user) throws Exception {
    	PayCustComplainDAO payCustComplainDAO = new PayCustComplainDAO();
    	PayAccProfileFrozenService payAccProfileFrozenService = new PayAccProfileFrozenService();
    	PayAdjustAccountCashService payAdjustAccountCashService = new PayAdjustAccountCashService();
    	try {
    		transaction.beignTransaction(payCustComplainDAO,payAccProfileFrozenService,payAdjustAccountCashService);
    		PayCustComplain tmp = payCustComplainDAO.detailPayCustComplain(payCustComplain.id);
	    	if("0".equals(payCustComplain.optStatus)){//初始状态，扣减金额无效
	    		payCustComplain.deductAmt=0l;
	    		
	    	} else if("1".equals(payCustComplain.optStatus)){//处理结束
	    		long deductAmt = payCustComplain.deductAmt;//扣减金额
	        	if(deductAmt>tmp.frozenAmt)throw new Exception("扣减金额不能大于冻结金额");
	        	PayAccProfileFrozen payAccProfileFrozen = new PayAccProfileFrozenService().detailPayAccProfileFrozenStatus(tmp.accFrozenId);
	    		//自动解冻处理
	    		if("1".equals(payCustComplain.unfrozenFlag)&&payAccProfileFrozen != null){
	                //判断解冻金额是否超出冻结金额
	                Long dMoney = payAccProfileFrozen.getCurAmt();
	                Long jmoney = tmp.frozenAmt;
	                if(jmoney>dMoney)throw new Exception("解冻金额不能大于冻结金额！");
	                payAccProfileFrozenService.updatePayAccProfileUnfrozen(payAccProfileFrozen, jmoney, user);
	    		}
	    		//自动调账减款处理
	    		if("0".equals(payCustComplain.deductFlag)&&deductAmt>0){
	    			PayAdjustAccountCash payAdjustAccountCash = new PayAdjustAccountCash();
	    			payAdjustAccountCash.custId=tmp.custId;//客户编号
	    			payAdjustAccountCash.acType=tmp.type;//账户类型 0个人账户,1商户账户
	    			payAdjustAccountCash.adjustType="1";//调账类型 1减少
	    			payAdjustAccountCash.adjustBussType="3";//业务类型 3投诉调账
	    			payAdjustAccountCash.amt=deductAmt;
	    			payAdjustAccountCash.remark="投诉编号"+tmp.id;
	    			payAdjustAccountCash.setId(Tools.getUniqueIdentify());
		            payAdjustAccountCash.setApplyIp(request.getRemoteAddr());
		            payAdjustAccountCash.setApplyTime(new Date());
		            payAdjustAccountCash.setApplyUser(user.getId());
		            payAdjustAccountCash.setCheckTime(new Date());
		            payAdjustAccountCash.setStatus("0");//待审核
		            payCustComplain.accAdjustId = payAdjustAccountCash.id;
		            System.out.println(payAdjustAccountCash);
		            payAdjustAccountCashService.addPayAdjustAccountCash(payAdjustAccountCash);
	    		}
	    		payCustComplain.frozenAmt = tmp.frozenAmt;
	    	} else if("2".equals(payCustComplain.optStatus)){//作废
	    		String id=payCustComplain.id;
	    		payCustComplain = new PayCustComplain();
	    		payCustComplain.id = id;
	    		payCustComplain.optStatus = "2";
	    		payCustComplain.frozenAmt = tmp.frozenAmt;
	    	}
	    	payCustComplainDAO.updatePayCustComplain(payCustComplain);
	        transaction.endTransaction();
    	} catch(Exception e) {
    		log.info(PayUtil.exceptionToString(e));
    		transaction.rollback();
    	}
    }
    /**
     * detail PayCustComplain
     * @param id
     * @throws Exception
     */
    public PayCustComplain detailPayCustComplain(String id) throws Exception {
        return new PayCustComplainDAO().detailPayCustComplain(id);
    }
}