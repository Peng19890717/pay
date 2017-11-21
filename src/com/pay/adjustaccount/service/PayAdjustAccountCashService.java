package com.pay.adjustaccount.service;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import util.Tools;

import com.PayConstant;
import com.jweb.dao.JWebUser;
import com.jweb.service.TransactionService;
import com.pay.adjustaccount.dao.PayAdjustAccountCash;
import com.pay.adjustaccount.dao.PayAdjustAccountCashDAO;
import com.pay.merchant.dao.PayMerchant;
import com.pay.merchant.dao.PayMerchantDAO;
import com.pay.order.dao.PayAccProfile;
import com.pay.order.dao.PayAccProfileDAO;
import com.pay.order.dao.PayTransferAccOrder;
import com.pay.order.dao.PayTransferAccOrderDAO;
import com.pay.user.dao.PayTranUserInfo;
import com.pay.user.dao.PayTranUserInfoDAO;

/**
 * Object PAY_ADJUST_ACCOUNT_CASH service. 
 * @author Administrator
 *
 */
public class PayAdjustAccountCashService extends TransactionService{
    /**
     * Get records list(json).
     * @return
     */
    public String getPayAdjustAccountCashList(PayAdjustAccountCash payAdjustAccountCash,int page,int rows,String sort,String order){
        try {
            PayAdjustAccountCashDAO payAdjustAccountCashDAO = new PayAdjustAccountCashDAO();
            List list = payAdjustAccountCashDAO.getPayAdjustAccountCashList(payAdjustAccountCash, page, rows, sort, order);
            JSONObject json = new JSONObject();
            json.put("total", String.valueOf(payAdjustAccountCashDAO.getPayAdjustAccountCashCount(payAdjustAccountCash)));
            JSONArray row = new JSONArray();
            for(int i = 0; i<list.size(); i++){
                row.put(i, ((PayAdjustAccountCash)list.get(i)).toJson());
            }
            //查询总金额
            Long money = payAdjustAccountCashDAO.getTotalAccountCashMoney(payAdjustAccountCash);
            json.put("rows", row);
            json.put("totalAccountCashMoney", String.valueOf(money));
            return json.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
    /**
     * add PayAdjustAccountCash
     * @param payAdjustAccountCash
     * @throws Exception
     */
    public void addPayAdjustAccountCash(PayAdjustAccountCash payAdjustAccountCash) throws Exception {
        new PayAdjustAccountCashDAO().addPayAdjustAccountCash(payAdjustAccountCash);
    }
    /**
     * remove PayAdjustAccountCash
     * @param id
     * @throws Exception
     */
    public void removePayAdjustAccountCash(String id) throws Exception {
        new PayAdjustAccountCashDAO().removePayAdjustAccountCash(id);
    }
    /**
     * update PayAdjustAccountCash
     * @param payAdjustAccountCash
     * @throws Exception
     */
    public void updatePayAdjustAccountCash(PayAdjustAccountCash payAdjustAccountCash) throws Exception {
        new PayAdjustAccountCashDAO().updatePayAdjustAccountCash(payAdjustAccountCash);
    }
    
    /**
     * 审核操作
     * update PayAdjustAccountCash
     * @param payAdjustAccountCash
     * @throws Exception
     */
    public void setPayAdjustAccountCashCheck(HttpServletRequest request, PayAdjustAccountCash payAdjustAccountCash, JWebUser user) throws Exception {
    	PayAdjustAccountCashDAO payAdjustAccountCashDao = new PayAdjustAccountCashDAO();
    	PayAccProfileDAO payAccProfileDAO = new PayAccProfileDAO();
    	PayTransferAccOrderDAO payTransferAccOrderDAO = new PayTransferAccOrderDAO();
    	//商户
    	PayMerchantDAO payMerchantDAO = new PayMerchantDAO();
    	//用户
    	PayTranUserInfoDAO payTranUserInfoDAO = new PayTranUserInfoDAO();
    	try {
    		//事务启动
    		transaction.beignTransaction(payAdjustAccountCashDao,payAccProfileDAO,payTransferAccOrderDAO,payMerchantDAO,payTranUserInfoDAO);
    		//声明要更改的实体
    		payAdjustAccountCash.setCheckIp(request.getRemoteAddr());
    		payAdjustAccountCash.setCheckUser(user.getId());
    		payAdjustAccountCash.setStatus(request.getParameter("status"));
    		String remark = request.getParameter("remark");
    		payAdjustAccountCash.remark = payAdjustAccountCash.remark==null?""+remark:payAdjustAccountCash.remark+"；"+remark;
            //通过
            if(payAdjustAccountCash.getStatus().equals("1")){
            	//定义操作金额
            	Long transferAmount = 0L;
            	 //更改金额表金额
                PayAccProfile payAccProfile = payAccProfileDAO.detailPayAccProfile(payAdjustAccountCash.custId);
                if(payAdjustAccountCash.adjustType.equals("0"))transferAmount=payAdjustAccountCash.amt;
                else transferAmount=0-payAdjustAccountCash.amt;
                payAccProfileDAO.updatePayAccProfileAmt(payAccProfile,transferAmount);
                //在转账表增加转账数据    [ 如果减少金额为 - ]
                PayTransferAccOrder payTransferAccOrder = new PayTransferAccOrder();
                payTransferAccOrder.setTranOrdno(Tools.getUniqueIdentify());
                payTransferAccOrder.setTranTime(new Date());
                payTransferAccOrder.setStatus(PayConstant.TRANSFER_STATUS_SUCCESS);
                payTransferAccOrder.setCcy("CNY");
                payTransferAccOrder.setTxamt(transferAmount<0?0-transferAmount:transferAmount);
                payTransferAccOrder.setTotamt(transferAmount<0?0-transferAmount:transferAmount);
                payTransferAccOrder.setTgetAccNo(payAdjustAccountCash.getCustId());
                //根据帐号查询名字  用户或者商户   0用户 1商户 2支付渠道 
                if(payAdjustAccountCash.acType.equals(PayConstant.CUST_TYPE_USER)){
                	PayTranUserInfo payTranUserInfo = payTranUserInfoDAO.detailPayTranUserInfoByCustId(payAdjustAccountCash.getCustId());
                	if(payTranUserInfo!=null)payTransferAccOrder.setTgetAccName(payTranUserInfo.getRealName());
                }
                if(payAdjustAccountCash.acType.equals(PayConstant.CUST_TYPE_MERCHANT)){
                	PayMerchant payMerchant = payMerchantDAO.detailPayMerchantByCustId(payAdjustAccountCash.getCustId());
                	if(payMerchant!=null)payTransferAccOrder.setTgetAccName(payMerchant.getStoreName());
                }
                //客户ID 为系统虚拟账户
                payTransferAccOrder.setCustId(PayConstant.PAY_CONFIG.get("PAY_SYS_ACCOUNT"));
                payTransferAccOrder.setBatTranCustOrdno(Tools.getUniqueIdentify());
                payTransferAccOrder.setBatNo(Tools.getUniqueIdentify());
                payTransferAccOrder.setTranType(PayConstant.TRANSFER_TYPE_TRANSFER);
                payTransferAccOrder.setType("1"); 
                payTransferAccOrder.setTranSuccessTime(new Date());
                payTransferAccOrder.setCustType("1");
                payTransferAccOrder.setTgetAccType("1");
                payTransferAccOrder.setCustName("支付平台");
                payTransferAccOrder.tgetAccType=payAdjustAccountCash.acType;
                payTransferAccOrder.filed2="系统调账（调账员ID："+user.getId()+"）";
                payTransferAccOrderDAO.addPayTransferAccOrder(payTransferAccOrder);
            }
            //更改状态
            payAdjustAccountCashDao.updatePayAdjustAccountCashCheck(payAdjustAccountCash);
    		//事务提交
			transaction.endTransaction();
		} catch (Exception e) {
			//事务回滚
			transaction.rollback();
			throw e;
		}
    	
    }
    
    /**
     * detail PayAdjustAccountCash
     * @param id
     * @throws Exception
     */
    public PayAdjustAccountCash detailPayAdjustAccountCash(String id) throws Exception {
        return new PayAdjustAccountCashDAO().detailPayAdjustAccountCash(id);
    }
    
    /**
     * 查询余额表
     * @param payAcNo
     * @return
     * @throws Exception
     */
    public PayAccProfile detailPayAccProfile(String payAcNo) throws Exception {
        return new PayAccProfileDAO().detailPayAccProfile(payAcNo);
    }
    
	public void setPayAdjustAccountCashCheckForCharge(
			HttpServletRequest request,PayAdjustAccountCash adjustAccountCash,JWebUser user) throws Exception {
		//查询该调账详情
		adjustAccountCash.setCheckIp(request.getRemoteAddr());
		adjustAccountCash.setCheckTime(new Date());
		adjustAccountCash.setCheckUser(user.getId());
        Long newAmt = null;
		//通过
        if("1".equals(request.getParameter("status"))){
        	//把调账金额存入商户的预存手续费中
        	//增加
        	if("0".equals(adjustAccountCash.adjustType)){
        		newAmt = adjustAccountCash.amt;
        	} else if("1".equals(adjustAccountCash.adjustType)){
        		newAmt = 0 - adjustAccountCash.amt;
        	}
        	new PayMerchantDAO().updatePayMerchantForPreStorageFee(newAmt,adjustAccountCash.custId);
        }
        adjustAccountCash.status = request.getParameter("status");
        adjustAccountCash.remark = adjustAccountCash.remark==null?""+request.getParameter("remark"):
        	adjustAccountCash.remark+"；"+request.getParameter("remark");
        new PayAdjustAccountCashDAO().updatePayAdjustAccountCash(adjustAccountCash);
	}
}