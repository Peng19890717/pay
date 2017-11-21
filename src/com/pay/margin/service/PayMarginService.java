package com.pay.margin.service;

import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import util.Tools;

import com.jweb.service.TransactionService;
import com.pay.margin.dao.PayMargin;
import com.pay.margin.dao.PayMarginDAO;
import com.pay.margin.dao.PayMarginRcvDet;
import com.pay.margin.dao.PayMarginRcvDetDAO;
import com.pay.refund.dao.PayRefund;

/**
 * Object PAY_MARGIN service. 
 * @author Administrator
 *
 */
public class PayMarginService extends TransactionService{
    /**
     * Get records list(json).
     * @return
     */
    public String getPayMarginList(PayMargin payMargin,int page,int rows,String sort,String order){
        try {
        	//调用持层
            PayMarginDAO payMarginDAO = new PayMarginDAO();
            List list = payMarginDAO.getPayMarginList(payMargin, page, rows, sort, order);
            JSONObject json = new JSONObject();
            json.put("total", String.valueOf(payMarginDAO.getPayMarginCount(payMargin)));
            JSONArray row = new JSONArray();
            for(int i = 0; i<list.size(); i++){
                row.put(i, ((PayMargin)list.get(i)).toJson());
            }
            //查询总金额
            Long money = payMarginDAO.getTotalMarginMoney(payMargin);
            json.put("rows", row);
            json.put("totalMarginMoney", String.valueOf(money));
            return json.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
    /**
     * add PayMargin
     * @param payMargin
     * @throws Exception
     */
    public void addPayMargin(PayMargin payMargin) throws Exception {
//    	payMargin.setMarginAmt(payMargin.getMarginAmt()*100);
//    	payMargin.setPaidInAmt(payMargin.getPaidInAmt()*100);
        new PayMarginDAO().addPayMargin(payMargin);
        //调用商户保证金收取记录的业务层，添加记录
        PayMarginRcvDet payMarginRcvDet=new PayMarginRcvDet();
        payMarginRcvDet.setSeqNo(Tools.getUniqueIdentify());
        payMarginRcvDet.setCustId(payMargin.getCustId());
//        payMarginRcvDet.pactNo = payMargin.pactNo;
        payMarginRcvDet.setPaidInAmt(payMargin.getPaidInAmt());
        payMarginRcvDet.setMarginAc(payMargin.getMarginAc());
        payMarginRcvDet.setCustProvisionAcNo(payMargin.getCustProvisionAcNo());
        payMarginRcvDet.setMark(payMargin.getMark());
        payMarginRcvDet.marginCurAmt = payMargin.paidInAmt;
        new PayMarginRcvDetDAO().addPayMarginRcvDet(payMarginRcvDet);
    }
    /**
     * remove PayMargin
     * @param seqNo
     * @throws Exception
     */
    public void removePayMargin(String seqNo) throws Exception {
        new PayMarginDAO().removePayMargin(seqNo);
    }
    /**
     * 判断保证金是否存在
     * @param custId
     * @param pactNo
     * @throws Exception
     */
//    public boolean isExistMargin(String custId,String pactNo) throws Exception {
    public boolean isExistMargin(String custId) throws Exception {
//        return new PayMarginDAO().isExistMargin(custId,pactNo);
    	return new PayMarginDAO().isExistMargin(custId);
    }
    /**
     * detail PayMargin
     * @param seqNo
     * @throws Exception
     */
    public PayMargin detailPayMargin(String seqNo) throws Exception {
        return new PayMarginDAO().detailPayMargin(seqNo);
    }
	public void appendPayMargin(PayMargin payMargin) throws Exception {
		PayMarginDAO payMarginDAO=new PayMarginDAO();
		PayMarginRcvDetDAO pmrdd = new PayMarginRcvDetDAO();
		transaction.beignTransaction(payMarginDAO,pmrdd);
		try {
			transaction.beignTransaction(payMarginDAO,pmrdd);
			if(payMarginDAO.appendPayMargin(payMargin) > 0){
				//调用商户保证金收取记录的业务层，添加记录
		        PayMarginRcvDet payMarginRcvDet=new PayMarginRcvDet();
		        payMarginRcvDet.setSeqNo(Tools.getUniqueIdentify());
		        payMarginRcvDet.setCustId(payMargin.getCustId());
//		        payMarginRcvDet.pactNo = payMargin.pactNo;
		        payMarginRcvDet.setPaidInAmt(payMargin.getPaidInAmt());
		        payMarginRcvDet.setMarginAc("");
		        payMarginRcvDet.setCustProvisionAcNo("");
		        payMarginRcvDet.setMark(payMargin.getMark());
		        payMarginRcvDet.marginCurAmt = payMargin.paidInAmt;
		        pmrdd.addPayMarginRcvDet(payMarginRcvDet);
			}
	        transaction.endTransaction();
		} catch (Exception e) {
			transaction.rollback();
			throw e;
		}
	}
	public void appendPayMarginForRefund(PayRefund payRefund) throws Exception{
		PayMarginDAO payMarginDAO = new PayMarginDAO();
		PayMarginRcvDetDAO pmrdd = new PayMarginRcvDetDAO();
		try {
			transaction.beignTransaction(payMarginDAO,pmrdd);
			PayMargin margin = payMarginDAO.getPayMarginByMerNo(payRefund.merno);
			//保证金账号不能存在
			if(margin == null){
				margin = new PayMargin();
				margin.seqNo=Tools.getUniqueIdentify();
				margin.custId=payRefund.merno;
				margin.pactNo="";
				margin.paidInAmt=0l-payRefund.rfamt;
				margin.marginAc="";
				margin.custProvisionAcNo="";
				margin.marginRaSign="1";
				margin.mark="系统默认创建";
				margin.creOperId="admin";
				margin.lstUptOperId="admin";
				payMarginDAO.addPayMargin(margin);
			} else {
				margin.paidInAmt = 0l-payRefund.rfamt;
				if(payMarginDAO.appendPayMargin(margin)==0)return;
			}		
			PayMarginRcvDet payMarginRcvDet=new PayMarginRcvDet();
	        payMarginRcvDet.setSeqNo(Tools.getUniqueIdentify());
	        payMarginRcvDet.setCustId(margin.getCustId());
	//        payMarginRcvDet.pactNo = margin.pactNo;
	        payMarginRcvDet.setPaidInAmt(margin.getPaidInAmt());
	        payMarginRcvDet.setMarginAc("");
	        payMarginRcvDet.setCustProvisionAcNo("");
	        payMarginRcvDet.setMark("保证金退款，退款单号："+payRefund.refordno);
	        payMarginRcvDet.marginCurAmt = margin.paidInAmt;
	        pmrdd.addPayMarginRcvDet(payMarginRcvDet);
	        transaction.endTransaction();
		} catch (Exception e) {
			transaction.rollback();
			throw e;
		}
	}
}