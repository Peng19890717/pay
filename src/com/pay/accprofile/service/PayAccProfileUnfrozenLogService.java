package com.pay.accprofile.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;
import org.json.JSONObject;

import com.jweb.service.TransactionService;
import com.pay.accprofile.dao.PayAccProfileFrozen;
import com.pay.accprofile.dao.PayAccProfileFrozenDAO;
import com.pay.accprofile.dao.PayAccProfileUnfrozenLog;
import com.pay.accprofile.dao.PayAccProfileUnfrozenLogDAO;
import com.pay.merchant.dao.PayAccProfile;
import com.pay.merchant.dao.PayAccProfileDAO;

/**
 * Object PAY_ACC_PROFILE_UNFROZEN_LOG service. 
 * @author Administrator
 *
 */
public class PayAccProfileUnfrozenLogService extends TransactionService{
	private static final Log log = LogFactory.getLog(PayAccProfileUnfrozenLogService.class);
    /**
     * Get records list(json).
     * @return
     */
    public String getPayAccProfileUnfrozenLogList(PayAccProfileUnfrozenLog payAccProfileUnfrozenLog,int page,int rows,String sort,String order){
        try {
            PayAccProfileUnfrozenLogDAO payAccProfileUnfrozenLogDAO = new PayAccProfileUnfrozenLogDAO();
            List list = payAccProfileUnfrozenLogDAO.getPayAccProfileUnfrozenLogList(payAccProfileUnfrozenLog, page, rows, sort, order);
            JSONObject json = new JSONObject();
            json.put("total", String.valueOf(payAccProfileUnfrozenLogDAO.getPayAccProfileUnfrozenLogCount(payAccProfileUnfrozenLog)));
            JSONArray row = new JSONArray();
            for(int i = 0; i<list.size(); i++){
                row.put(i, ((PayAccProfileUnfrozenLog)list.get(i)).toJson());
            }
            json.put("rows", row);
            return json.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
	/**
	 * 解冻处理
	 * @param id
	 * @param money
	 * @param user
	 * @return
	 */
	public String checkPayAccProfileUnfrozenLog(PayAccProfileUnfrozenLog payAccProfileUnfrozenLog) throws Exception {
		PayAccProfileFrozen payAccProfileFrozenNew = new PayAccProfileFrozen();
		PayAccProfileFrozenDAO payAccProfileFrozenDAO = new PayAccProfileFrozenDAO();
		PayAccProfileDAO payAccProfileDAO = new PayAccProfileDAO(); 
		PayAccProfileUnfrozenLogDAO payAccProfileUnfrozenLogDAO = new PayAccProfileUnfrozenLogDAO();
		try {
    		//事务启动
    		transaction.beignTransaction(payAccProfileFrozenDAO,payAccProfileDAO,payAccProfileUnfrozenLogDAO);
    		PayAccProfileUnfrozenLog tmp = payAccProfileUnfrozenLogDAO.detailPayAccProfileUnfrozenLog(payAccProfileUnfrozenLog.id);
    		PayAccProfileFrozen payAccProfileFrozen = payAccProfileFrozenDAO.detailPayAccProfileFrozen(tmp.accFrozenId);
    		//检查解冻金额和剩余冻结金额是否匹配
    		//根据日志审核状态，进行解冻操作
    		if("1".equals(payAccProfileUnfrozenLog.flag)){//通过
    			if("0".equals(tmp.flag)){//解冻状态为0时可以解冻
    				if(tmp.unfrozenAmt>payAccProfileFrozen.curAmt)throw new Exception("解冻金额（"
    	    				+new BigDecimal(tmp.unfrozenAmt).divide(new BigDecimal(100)).toPlainString()+"元）不能大于当前冻结金额（"
    	    				+new BigDecimal(payAccProfileFrozen.curAmt).divide(new BigDecimal(100)).toPlainString()+"元）");
    				//更新解冻日志状态和日志备注
		        	payAccProfileUnfrozenLog.accFrozenId = tmp.accFrozenId;
		        	payAccProfileUnfrozenLog.remark = (tmp.remark==null?"":tmp.remark + "；") +payAccProfileUnfrozenLog.remark; 
		    		payAccProfileUnfrozenLogDAO.checkPayAccProfileUnfrozenLog(payAccProfileUnfrozenLog,tmp.unfrozenAmt);
		    		//更改冻结记录
		    		payAccProfileFrozenNew.setId(payAccProfileFrozen.getId());
		    		payAccProfileFrozenNew.setCurAmt(payAccProfileFrozen.getCurAmt()-tmp.unfrozenAmt);
		    		if(payAccProfileFrozen.getCurAmt()-tmp.unfrozenAmt==0) payAccProfileFrozenNew.setStatus("1");
		    		payAccProfileFrozenNew.setOptUser(payAccProfileUnfrozenLog.userId);
		    		payAccProfileFrozenNew.setUpdateTime(new Date());
		    		payAccProfileFrozenDAO.updatePayAccProfileFrozen(payAccProfileFrozenNew);
		    	 	//更改商户余额
		        	PayAccProfile payAccProfile = payAccProfileDAO.detailPayAccProfileByAcTypeAndAcNo(
		        			payAccProfileFrozen.getAccType(),payAccProfileFrozen.getAccNo());
		        	log.info(payAccProfile.toString().replaceAll("\n",";"));
		        	payAccProfile.cashAcBal+=tmp.unfrozenAmt;
		        	payAccProfile.consAcBal+=tmp.unfrozenAmt;
		        	payAccProfileDAO.updatePayAccProfile(payAccProfile);
    			}
    		} else if("2".equals(payAccProfileUnfrozenLog.flag)){//不通过
    			PayAccProfileUnfrozenLog pt = new PayAccProfileUnfrozenLog();
    			pt.id = payAccProfileUnfrozenLog.id;
    			pt.flag = "2";
    			pt.remark = (tmp.remark==null?"":tmp.remark+"；")+payAccProfileUnfrozenLog.remark;
    			pt.optTime = new Date();
    			pt.userId = payAccProfileUnfrozenLog.userId;
    			payAccProfileUnfrozenLogDAO.updatePayAccProfileUnfrozenLog(pt);
    		} else throw new Exception("状态非法");
    		//事务提交
			transaction.endTransaction();
		} catch (Exception e) {
			//事务回滚
			transaction.rollback();
			throw e;
		}
		return "";
	}
}