package com.pay.order.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import util.Tools;

import com.PayConstant;
import com.jweb.service.TransactionService;
import com.pay.fee.service.PayFeeRateService;
import com.pay.merchant.dao.PayMerchant;
import com.pay.merchant.dao.PayMerchantDAO;
import com.pay.order.dao.PayTransferAccOrder;
import com.pay.order.dao.PayTransferAccOrderDAO;
import com.pay.risk.service.PayRiskDayTranSumService;
import com.pay.risk.service.RiskService;
import com.pay.user.dao.PayTranUserInfo;
import com.pay.user.dao.PayTranUserInfoDAO;

/**
 * Object PAY_TRANSFER_ACC_ORDER service. 
 * @author Administrator
 *
 */
public class PayTransferAccOrderService extends TransactionService{
    /**
     * Get records list(json).
     * @return
     */
    public String getPayTransferAccOrderList(PayTransferAccOrder payTransferAccOrder,int page,int rows,String sort,String order){
        try {
            PayTransferAccOrderDAO payTransferAccOrderDAO = new PayTransferAccOrderDAO();
            List list = payTransferAccOrderDAO.getPayTransferAccOrderList(payTransferAccOrder, page, rows, sort, order);
            JSONObject json = new JSONObject();
            json.put("total", String.valueOf(payTransferAccOrderDAO.getPayTransferAccOrderCount(payTransferAccOrder)));
            JSONArray row = new JSONArray();
            for(int i = 0; i<list.size(); i++){
                row.put(i, ((PayTransferAccOrder)list.get(i)).toJson());
            }
            json.put("rows", row);
            //查询总金额
            Long[] money=payTransferAccOrderDAO.getTotalTransferAccMoney(payTransferAccOrder);
            json.put("totalTxamt", String.valueOf(money[0]));
            json.put("totalTotamt", String.valueOf(money[1]));
            return json.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
    /**
     * add PayTransferAccOrder
     * @param payTransferAccOrder
     * @throws Exception
     */
    public void addPayTransferAccOrder(PayTransferAccOrder payTransferAccOrder) throws Exception {
        new PayTransferAccOrderDAO().addPayTransferAccOrder(payTransferAccOrder);
    }
    /**
     * remove PayTransferAccOrder
     * @param tranOrdno
     * @throws Exception
     */
    public void removePayTransferAccOrder(String tranOrdno) throws Exception {
        new PayTransferAccOrderDAO().removePayTransferAccOrder(tranOrdno);
    }
    /**
     * update PayTransferAccOrder
     * @param payTransferAccOrder
     * @throws Exception
     */
    public void updatePayTransferAccOrder(PayTransferAccOrder payTransferAccOrder) throws Exception {
        new PayTransferAccOrderDAO().updatePayTransferAccOrder(payTransferAccOrder);
    }
    /**
     * detail PayTransferAccOrder
     * @param tranOrdno
     * @throws Exception
     */
    public PayTransferAccOrder detailPayTransferAccOrder(String tranOrdno) throws Exception {
        return new PayTransferAccOrderDAO().detailPayTransferAccOrder(tranOrdno);
    }
    /**
     * 转账
     * @param request
     * @return
     * @throws Exception 
     */
	public String transferAcc(HttpServletRequest request) throws Exception {
		if(PayConstant.PAY_CONFIG.get("PAY_SYS_ACCOUNT") == null)throw new Exception("系统账户不存在");
		PayRiskDayTranSumService rtsService = new PayRiskDayTranSumService();
		PayTransferAccOrderDAO tsDao = new PayTransferAccOrderDAO();
		try {
			transaction.beignTransaction(rtsService,tsDao);
			String custType = request.getParameter("custType");
			String custId = request.getParameter("custId");
			String custName="";
			if(PayConstant.CUST_TYPE_MERCHANT.equals(custType)){
				PayMerchant mer = new PayMerchantDAO().selectByCustId(custId);
				if(mer!=null)custName = mer.storeName;
			} else if(PayConstant.CUST_TYPE_USER.equals(custType)){
				PayTranUserInfo user = new PayTranUserInfoDAO().detailPayTranUserInfo(custId);
				if(user!=null)custName = user.realName;
			}
			//batTranCustOrdno,tgetCustType,tgetAccNo,tgetAccName,txamt用分号隔开
			String transferInfo = request.getParameter("transferInfo");
			List<PayTransferAccOrder> tsList = new ArrayList<PayTransferAccOrder>();
			Map<String, PayTransferAccOrder> tsMap = new HashMap<String, PayTransferAccOrder>();
			PayFeeRateService feeService = new PayFeeRateService();
			//解析转账,封装列表
			String [] ts = transferInfo.split("_;_");
			if(ts.length==0||ts.length>1000)throw new Exception("每次转账笔数在0-1000之间");
			long totalAmt = 0;
			String batNo = Tools.getUniqueIdentify();
			Date curTime = new Date();
			for(int i = 0; i<ts.length; i++){
				String [] es = ts[i].split("_,_");
				if(es.length!=5&&es.length!=6)continue;
				PayTransferAccOrder tsOrder = new PayTransferAccOrder();
				if(tsMap.get(es[0])!=null)throw new Exception("转账订单号重复（"+es[0]+"）");
				tsOrder.batTranCustOrdno = es[0];
				tsOrder.batNo = batNo;
				tsOrder.tgetAccType = es[1];
				tsOrder.tgetAccNo = es[2];
				tsOrder.tgetAccName = es[3];
				tsOrder.txamt = 0l;
				tsOrder.tranTime = curTime;
				tsOrder.tranSuccessTime = tsOrder.tranTime;
				tsOrder.custName = custName;
				try {
					tsOrder.txamt = (long)(Math.round(Double.parseDouble(es[4])*100d));
					if(tsOrder.txamt<=0||tsOrder.txamt>=10000000000l)throw new Exception();
				} catch (Exception e) {
					tsOrder.status = "50";//金额错误
				}
				if(custId.equals(tsOrder.tgetAccNo))tsOrder.status = "53";//不能给自己转账
				tsOrder.filed2 = es.length==6?es[5]:"";
				tsOrder.custId = custId;
				tsOrder.custType = custType;
				//交易类型 1消费 2充值 3结算 4退款 5提现  6转账
				Object [] fee = PayFeeRateService.getFeeByFeeRate(feeService.getFeeRateByCust(custType, custId, "6"),tsOrder.txamt);
				tsOrder.totamt = tsOrder.txamt+(Long)fee[0];
				tsOrder.filed1=(String) fee[1];//存放转账费率信息
				totalAmt += tsOrder.totamt;
				tsList.add(tsOrder);
				tsMap.put(tsOrder.batTranCustOrdno, tsOrder);
			}
			//检查余额(包括手续费)
			if(tsDao.getCustTotalAmt(custType,custId)<totalAmt)throw new Exception("余额不足");
			//检查账户存在情况，通过账户号、账户名
			tsDao.checkTargetAcc(custType,custId,tsList);
			//检查重复转账
			tsDao.checkTransferedAcc(custType,custId,tsList,tsMap);
			//风控检查 限额类型:1消费 2充值 3结算 4退款 5提现 6转账
			String msg = null;
			if(PayConstant.CUST_TYPE_MERCHANT.equals(custType)) msg = new RiskService().
				checkMerchantLimit(new Object[]{custType,custId,tsList,curTime},custId,PayConstant.TRAN_TYPE_TRANSFER);
			else if (PayConstant.CUST_TYPE_USER.equals(custType)) msg = new RiskService().
				checkUserLimit(new Object[]{custType,custId,tsList,curTime},custId,PayConstant.TRAN_TYPE_TRANSFER);
			//违反风控规则
			if(msg!=null)throw new Exception(msg);
			//转账处理
			tsDao.transferAcc(custType,custId,tsList);
			//交易汇总更新
			rtsService.updateRiskTransferSum(custType,custId,tsList);
			transaction.endTransaction();
			return "{\"resCode\":\"000\",\"resMsg\":\""+PayConstant.RESP_CODE_DESC.get("000")+"\"}";
		} catch (Exception e) {
			transaction.rollback();
			throw e;
		}
	}
}