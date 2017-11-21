package com.pay.accprofile.service;

import java.sql.Connection;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;
import org.json.JSONObject;

import util.Tools;

import com.PayConstant;
import com.jweb.dao.BaseDAO;
import com.jweb.dao.JWebUser;
import com.jweb.service.TransactionService;
import com.pay.accprofile.dao.PayAccProfileFrozen;
import com.pay.accprofile.dao.PayAccProfileFrozenDAO;
import com.pay.accprofile.dao.PayAccProfileUnfrozenLog;
import com.pay.accprofile.dao.PayAccProfileUnfrozenLogDAO;
import com.pay.merchant.dao.PayAccProfile;
import com.pay.merchant.dao.PayAccProfileDAO;
import com.pay.merchant.service.PayAccProfileService;

/**
 * Object PAY_ACC_PROFILE_FROZEN service. 
 * @author Administrator
 *
 */
public class PayAccProfileFrozenService extends TransactionService{
	private static final Log log = LogFactory.getLog(PayAccProfileFrozenService.class);
    /**
     * Get records list(json).
     * @return
     */
    public String getPayAccProfileFrozenList(PayAccProfileFrozen payAccProfileFrozen,int page,int rows,String sort,String order){
        try {
            PayAccProfileFrozenDAO payAccProfileFrozenDAO = new PayAccProfileFrozenDAO();
            List list = payAccProfileFrozenDAO.getPayAccProfileFrozenList(payAccProfileFrozen, page, rows, sort, order);
            JSONObject json = new JSONObject();
            json.put("total", String.valueOf(payAccProfileFrozenDAO.getPayAccProfileFrozenCount(payAccProfileFrozen)));
            JSONArray row = new JSONArray();
            for(int i = 0; i<list.size(); i++){
                row.put(i, ((PayAccProfileFrozen)list.get(i)).toJson());
            }
            //查询总金额
            Long money = payAccProfileFrozenDAO.getTotalFrozenMoney(payAccProfileFrozen);
            json.put("rows", row);
            json.put("totalFrozenMoney", String.valueOf(money));
            return json.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
	public void updatePayAccProfileFrozenStatus(String accNo,
			String columName, String operation) throws Exception {
		new PayAccProfileFrozenDAO().updatePayAccProfileFrozenStatus(accNo,columName,operation);
	}
	public PayAccProfileFrozen detailPayAccProfileFrozenStatus(String id) throws Exception {
		return new PayAccProfileFrozenDAO().detailPayAccProfileFrozen(id);
	}
	/**
	 * 解冻处理
	 * @param id
	 * @param money
	 * @param user
	 * @return
	 */
	public String updatePayAccProfileUnfrozen(PayAccProfileFrozen payAccProfileFrozen ,Long money,JWebUser user) throws Exception {
		PayAccProfileFrozen payAccProfileFrozenNew = new PayAccProfileFrozen();
		PayAccProfileFrozenDAO payAccProfileFrozenDAO = new PayAccProfileFrozenDAO();
		PayAccProfileDAO payAccProfileDAO = new PayAccProfileDAO(); 
		PayAccProfileUnfrozenLogDAO payAccProfileUnfrozenLogDAO = new PayAccProfileUnfrozenLogDAO();
		try {
    		//事务启动
    		transaction.beignTransaction(payAccProfileFrozenDAO,payAccProfileDAO,payAccProfileUnfrozenLogDAO);
    		//添加解冻资金日志
    		PayAccProfileUnfrozenLog unfrozenLog = new PayAccProfileUnfrozenLog();
    		unfrozenLog.id = Tools.getUniqueIdentify();
    		unfrozenLog.unfrozenAmt = money;
    		unfrozenLog.flag = "0".equals(PayConstant.PAY_CONFIG.get("MERCHANT_CASH_UNFROZEN_CHECK_FLAG"))?"1":"0";//unfrozenLog.flag:0未审 1已审 2作废
    		unfrozenLog.userId = user.id;
    		unfrozenLog.accFrozenId = payAccProfileFrozen.id;
    		unfrozenLog.remark = payAccProfileFrozen.remark;
    		payAccProfileUnfrozenLogDAO.addPayAccProfileUnfrozenLog(unfrozenLog);
    		if("0".equals(PayConstant.PAY_CONFIG.get("MERCHANT_CASH_UNFROZEN_CHECK_FLAG"))){//解冻商户资金审核标识 0不需要审核 1需要审核
	    		//更改冻结记录
	    		payAccProfileFrozenNew.setId(payAccProfileFrozen.getId());
	    		payAccProfileFrozenNew.setCurAmt(payAccProfileFrozen.getCurAmt()-money);
	    		if(payAccProfileFrozen.getCurAmt()-money==0) payAccProfileFrozenNew.setStatus("1");
	    		payAccProfileFrozenNew.setOptUser(user.getId());
	    		payAccProfileFrozenNew.setUpdateTime(new Date());
	    		payAccProfileFrozenDAO.updatePayAccProfileFrozen(payAccProfileFrozenNew);
	    	 	//更改商户余额
	        	PayAccProfile payAccProfile = payAccProfileDAO.detailPayAccProfileByAcTypeAndAcNo(payAccProfileFrozen.getAccType(),payAccProfileFrozen.getAccNo());
	        	log.info(payAccProfile.toString().replaceAll("\n",";"));
	        	payAccProfile.cashAcBal+=money;
	        	payAccProfile.consAcBal+=money;
	        	payAccProfileDAO.updatePayAccProfile(payAccProfile);
    		}
    		//事务提交
			transaction.endTransaction();
		} catch (Exception e) {
			//事务回滚
			transaction.rollback();
			throw e;
		}
		return "";
	}
	/**
     * 冻结资金
     * @param payAccProfileFrozen
     * @throws Exception
     */
    public void addPayAccProfileFrozen(PayAccProfileFrozen payAccProfileFrozen) throws Exception {
    	BaseDAO baseDAO = new BaseDAO();
    	Connection con = baseDAO.connection();
    	try {
    		con.setAutoCommit(false);
    		//扣除账户余额
    		if("1".equals(PayConstant.PAY_CONFIG.get("MERCHANT_CASH_FROZEN_CHECK_FLAG"))){//审核
    			payAccProfileFrozen.curAmt = 0l;
    			payAccProfileFrozen.setStatus("3");//待审
    		} else {//冻结
    			PayAccProfile payAccProfile = new PayAccProfileService().detailPayAccProfileByAcTypeAndAcNo(
	    				payAccProfileFrozen.getAccType(), payAccProfileFrozen.getAccNo());
	    		payAccProfile.setCashAcBal(payAccProfile.getCashAcBal()-payAccProfileFrozen.getAmt());
	    		payAccProfile.setConsAcBal(payAccProfile.getConsAcBal()-payAccProfileFrozen.getAmt());
	    		new PayAccProfileDAO().updatePayAccProfile(payAccProfile,con);
    		}
    		//添加冻结记录
    		new PayAccProfileFrozenDAO().addPayAccProfileFrozen(payAccProfileFrozen,con);
    		con.commit();
		} catch (Exception e) {
			con.rollback();
			throw e;
		}finally{
			con.setAutoCommit(true);
			baseDAO.close(null, null, con);
		}
    }
	/**
     * 冻结资金审核
     * @param payAccProfileFrozen
     * @throws Exception
     */
    public void checkPayAccProfileFrozen(PayAccProfileFrozen payAccProfileFrozen) throws Exception {
    	PayAccProfileDAO payAccProfileDAO = new PayAccProfileDAO();
    	PayAccProfileFrozenDAO payAccProfileFrozenDAO = new PayAccProfileFrozenDAO();
    	try {
    		transaction.beignTransaction(payAccProfileDAO,payAccProfileFrozenDAO);
    		PayAccProfileFrozen tmp = new PayAccProfileFrozen();
    		tmp.id = payAccProfileFrozen.id;
    		tmp.status = payAccProfileFrozen.status;
    		tmp.remark = payAccProfileFrozen.remark;
    		//扣除账户余额
    		if("0".equals(payAccProfileFrozen.status)){
	    		PayAccProfile payAccProfile = new PayAccProfileService().detailPayAccProfileByAcTypeAndAcNo(
	    				payAccProfileFrozen.getAccType(), payAccProfileFrozen.getAccNo());
	    		payAccProfile.setCashAcBal(payAccProfile.getCashAcBal()-payAccProfileFrozen.getAmt());
	    		payAccProfile.setConsAcBal(payAccProfile.getConsAcBal()-payAccProfileFrozen.getAmt());
	    		payAccProfileDAO.updatePayAccProfile(payAccProfile);
	    		tmp.curAmt = payAccProfileFrozen.amt;
    		} else  payAccProfileFrozen.setStatus("4");//作废
    		tmp.updateTime = new Date();
    		payAccProfileFrozenDAO.updatePayAccProfileFrozen(tmp);
    		transaction.endTransaction();
		} catch (Exception e) {
			transaction.rollback();
			throw e;
		}
    }
}