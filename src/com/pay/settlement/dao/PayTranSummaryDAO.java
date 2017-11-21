package com.pay.settlement.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.jweb.dao.BaseDAO;

public class PayTranSummaryDAO extends BaseDAO{

	 private static final Log log = LogFactory.getLog(PayTranSummaryDAO.class);
	 
	 public static synchronized PayTranSummary getPayTranSummaryValue(ResultSet rs) throws Exception{
		 PayTranSummary payTranSummary = new PayTranSummary();
		 payTranSummary.channelNo = rs.getString("pay_channel");
		 payTranSummary.tranTotalFee = rs.getLong("sumAmt");
		 payTranSummary.channelFee = rs.getLong("sumFee");
		 return payTranSummary;
	 }
	 
	 public List getList(PayTranSummary payTranSummary) throws Exception{
		 List list = new ArrayList();
		 if(payTranSummary.getTranTime() == null ){
			 return list;
		 }
		 if(payTranSummary.getTranTimeEnd() == null ){
			 return list;
		 }
		 String startTime = new SimpleDateFormat("yyyy-MM-dd").format(payTranSummary.tranTime)+" 00:00:00";
		 String endTime = new SimpleDateFormat("yyyy-MM-dd").format(payTranSummary.tranTimeEnd)+" 23:59:59";
		 String sql = "select pay_channel,sum(txamt) sumAmt,sum(CHANNEL_FEE) sumFee from pay_order where ordstatus='01' and CREATETIME>=to_date('"
				 +startTime+"','yyyy-mm-dd hh24:mi:ss') and CREATETIME<=to_date('"+endTime+"','yyyy-mm-dd hh24:mi:ss') group by pay_channel";
		 log.info(sql);
		 Connection con = null;
		 PreparedStatement ps = null;
		 ResultSet rs = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while(rs.next()){
                list.add(getPayTranSummaryValue(rs));
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(rs, ps, con);
        }
	 }
}
