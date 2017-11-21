package com.pay.coopbank.service;

import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import util.Tools;

import com.jweb.dao.JWebUser;
import com.jweb.service.TransactionService;
import com.pay.coopbank.dao.PayChannelFrozen;
import com.pay.coopbank.dao.PayChannelFrozenDAO;
import com.pay.coopbank.dao.PayChannelUnfrozenDetail;
import com.pay.coopbank.dao.PayChannelUnfrozenDetailDAO;

/**
 * Object pay_channel_frozen service. 
 * @author Administrator
 *
 */
public class PayChannelFrozenService extends TransactionService{
    /**
     * Get records list(json).
     * @return
     */
    public String getPayChannelFrozenList(PayChannelFrozen payChannelFrozen,int page,int rows,String sort,String order){
        try {
            PayChannelFrozenDAO payChannelFrozenDAO = new PayChannelFrozenDAO();
            List list = payChannelFrozenDAO.getPayChannelFrozenList(payChannelFrozen, page, rows, sort, order);
            JSONObject json = new JSONObject();
            json.put("total", String.valueOf(payChannelFrozenDAO.getPayChannelFrozenCount(payChannelFrozen)));
            JSONArray row = new JSONArray();
            for(int i = 0; i<list.size(); i++){
                row.put(i, ((PayChannelFrozen)list.get(i)).toJson());
            }
            json.put("rows", row);
            return json.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
    /**
     * add PayChannelFrozen
     * @param payChannelFrozen
     * @throws Exception
     */
    public void addPayChannelFrozen(PayChannelFrozen payChannelFrozen) throws Exception {
        new PayChannelFrozenDAO().addPayChannelFrozen(payChannelFrozen);
    }
    /**
     * remove PayChannelFrozen
     * @param id
     * @throws Exception
     */
    public void removePayChannelFrozen(String id) throws Exception {
        new PayChannelFrozenDAO().removePayChannelFrozen(id);
    }
    /**
     * update PayChannelFrozen
     * @param payChannelFrozen
     * @throws Exception
     */
    public void updatePayChannelFrozen(PayChannelFrozen payChannelFrozen) throws Exception {
        new PayChannelFrozenDAO().updatePayChannelFrozen(payChannelFrozen);
    }
    /**
     * detail PayChannelFrozen
     * @param id
     * @throws Exception
     */
    public PayChannelFrozen detailPayChannelFrozen(String id) throws Exception {
        return new PayChannelFrozenDAO().detailPayChannelFrozen(id);
    }
    /**
     * 渠道资金解冻
     * @param payChannelFrozen
     * @param jmoney
     * @param user 
     * @throws Exception 
     */
	public void updatePayChannelFrozen(PayChannelFrozen payChannelFrozen,
			Long jmoney, JWebUser user,String remark) throws Exception {
		PayChannelFrozenDAO channelFrozenDAO = new PayChannelFrozenDAO();
		PayChannelUnfrozenDetailDAO unfrozenDetailDAO = new PayChannelUnfrozenDetailDAO();
		try {
			//事务启动
			transaction.beignTransaction(channelFrozenDAO,unfrozenDetailDAO);
			//更新渠道资金冻结表的剩余冻结金额
			payChannelFrozen.curAmt -= jmoney;
			channelFrozenDAO.updatePayChannelFrozen(payChannelFrozen);
			//向渠道解冻明细表中插入记录
			PayChannelUnfrozenDetail unfrozenDetail = new PayChannelUnfrozenDetail();
			unfrozenDetail.setId(Tools.getUniqueIdentify());
			unfrozenDetail.amt = jmoney;
			unfrozenDetail.setFrozenId(payChannelFrozen.id);
			unfrozenDetail.createTime = new Date();
			unfrozenDetail.remark = remark;
			unfrozenDetail.optId = user.getId();
			unfrozenDetailDAO.addPayChannelUnfrozenDetail(unfrozenDetail);
			//事务提交
			transaction.endTransaction();
		} catch (Exception e) {
			//事务回滚
			transaction.rollback();
			throw e;
		}
	}

}