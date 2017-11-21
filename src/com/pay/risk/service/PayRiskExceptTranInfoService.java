package com.pay.risk.service;

import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import com.pay.risk.dao.PayRiskExceptTranInfoDAO;
import com.pay.risk.dao.PayRiskExceptTranInfo;

/**
 * Object PAY_RISK_EXCEPT_TRAN_INFO service. 
 * @author Administrator
 *
 */
public class PayRiskExceptTranInfoService {
    /**
     * Get records list(json).
     * @return
     */
    public String getPayRiskExceptTranInfoList(PayRiskExceptTranInfo payRiskExceptTranInfo,int page,int rows,String sort,String order){
        try {
            PayRiskExceptTranInfoDAO payRiskExceptTranInfoDAO = new PayRiskExceptTranInfoDAO();
            List list = payRiskExceptTranInfoDAO.getPayRiskExceptTranInfoList(payRiskExceptTranInfo, page, rows, sort, order);
            JSONObject json = new JSONObject();
            json.put("total", String.valueOf(payRiskExceptTranInfoDAO.getPayRiskExceptTranInfoCount(payRiskExceptTranInfo)));
            JSONArray row = new JSONArray();
            for(int i = 0; i<list.size(); i++){
                row.put(i, ((PayRiskExceptTranInfo)list.get(i)).toJson());
            }
            json.put("rows", row);
            return json.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
    
    /**
     * update PayRiskExceptTranInfo
     * @param payRiskExceptTranInfo
     * @throws Exception
     */
    public void updatePayRiskExceptTranInfo(PayRiskExceptTranInfo payRiskExceptTranInfo) throws Exception {
        new PayRiskExceptTranInfoDAO().updatePayRiskExceptTranInfo(payRiskExceptTranInfo);
    }
    /**
     * detail PayRiskExceptTranInfo
     * @param id
     * @throws Exception
     */
    public PayRiskExceptTranInfo detailPayRiskExceptTranInfo(String id) throws Exception {
        return new PayRiskExceptTranInfoDAO().detailPayRiskExceptTranInfo(id);
    }

    /**
     * 修改监控状态
     * @param detailPayRiskExceptTranInfo 原来的状态
     * @param payRiskExceptTranInfo 现在的状态
     * @throws Exception 
     */
	public void payRiskExceptTranInfoOperationStatus(PayRiskExceptTranInfo detailPayRiskExceptTranInfo,PayRiskExceptTranInfo payRiskExceptTranInfo) throws Exception {
		if(!"0".equals(detailPayRiskExceptTranInfo.exceptTranFlag)) throw new Exception("该条记录已被修改");
		detailPayRiskExceptTranInfo.exceptTranFlag = payRiskExceptTranInfo.exceptTranFlag;
		detailPayRiskExceptTranInfo.setRemark(payRiskExceptTranInfo.remark);
		new PayRiskExceptTranInfoDAO().updatePayRiskExceptTranInfo(payRiskExceptTranInfo);
	}
}