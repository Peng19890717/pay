package com.pay.risk.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import util.Tools;

import com.PayConstant;
import com.jweb.dao.BaseDAO;
/**
 * Table PAY_RISK_DAY_TRAN_SUM DAO. 
 * @author Administrator
 *
 */
public class PayRiskDayTranSumDAO  extends BaseDAO {
    private static final Log log = LogFactory.getLog(PayRiskDayTranSumDAO.class);
    public static synchronized PayRiskDayTranSum getPayRiskDayTranSumValue(ResultSet rs)throws SQLException {
        PayRiskDayTranSum payRiskDayTranSum = new PayRiskDayTranSum();
        payRiskDayTranSum.id = rs.getString("ID");
        payRiskDayTranSum.timeType = rs.getString("TIME_TYPE");
        payRiskDayTranSum.custType = rs.getString("CUST_TYPE");
//        payRiskDayTranSum.userCode = rs.getString("USER_CODE");
//        payRiskDayTranSum.custId = rs.getString("MER_NO");
        payRiskDayTranSum.custId = rs.getString("CUST_ID");
        payRiskDayTranSum.statDate = rs.getString("STAT_DATE");
        payRiskDayTranSum.allAmount = rs.getLong("ALL_AMOUNT");
        payRiskDayTranSum.refundAmount = rs.getLong("REFUND_AMOUNT");
        payRiskDayTranSum.saleAmount = rs.getLong("SALE_AMOUNT");
        payRiskDayTranSum.rechargeAmount = rs.getLong("RECHARGE_AMOUNT");
        payRiskDayTranSum.transferAmount = rs.getLong("TRANSFER_AMOUNT");
        payRiskDayTranSum.cashAmount = rs.getLong("CASH_AMOUNT");
        payRiskDayTranSum.predepositAmount = rs.getLong("PREDEPOSIT_AMOUNT");
        payRiskDayTranSum.allCount = rs.getLong("ALL_COUNT");
        payRiskDayTranSum.refundCount = rs.getLong("REFUND_COUNT");
        payRiskDayTranSum.saleCount = rs.getLong("SALE_COUNT");
        payRiskDayTranSum.predepositCount = rs.getLong("PREDEPOSIT_COUNT");
        payRiskDayTranSum.lessCount = rs.getLong("LESS_COUNT");
        payRiskDayTranSum.creditCount = rs.getLong("CREDIT_COUNT");
        payRiskDayTranSum.intCount = rs.getLong("INT_COUNT");
        payRiskDayTranSum.averAmount = rs.getLong("AVER_AMOUNT");
        payRiskDayTranSum.maxAmount = rs.getLong("MAX_AMOUNT");
        payRiskDayTranSum.allSuccessAmount = rs.getLong("ALL_SUCCESS_AMOUNT");
        payRiskDayTranSum.refundSuccessAmount = rs.getLong("REFUND_SUCCESS_AMOUNT");
        payRiskDayTranSum.saleSuccessAmount = rs.getLong("SALE_SUCCESS_AMOUNT");
        payRiskDayTranSum.rechargeSuccessAmount = rs.getLong("RECHARGE_SUCCESS_AMOUNT");
        payRiskDayTranSum.transferSuccessAmount = rs.getLong("TRANSFER_SUCCESS_AMOUNT");
        payRiskDayTranSum.cashSuccessAmount = rs.getLong("CASH_SUCCESS_AMOUNT");
        payRiskDayTranSum.predepositSuccessAmount = rs.getLong("PREDEPOSIT_SUCCESS_AMOUNT");
        payRiskDayTranSum.allSuccessCount = rs.getLong("ALL_SUCCESS_COUNT");
        payRiskDayTranSum.refundSuccessCount = rs.getLong("REFUND_SUCCESS_COUNT");
        payRiskDayTranSum.saleSuccessCount = rs.getLong("SALE_SUCCESS_COUNT");
        payRiskDayTranSum.predepositSuccessCount = rs.getLong("PREDEPOSIT_SUCCESS_COUNT");
        payRiskDayTranSum.lessSuccessCount = rs.getLong("LESS_SUCCESS_COUNT");
        payRiskDayTranSum.creditSuccessCount = rs.getLong("CREDIT_SUCCESS_COUNT");
        payRiskDayTranSum.intSuccessCount = rs.getLong("INT_SUCCESS_COUNT");
        payRiskDayTranSum.transferCount = rs.getLong("TRANSFER_COUNT");
        payRiskDayTranSum.transferSuccessCount = rs.getLong("TRANSFER_SUCCESS_COUNT");
        payRiskDayTranSum.cashCount = rs.getLong("CASH_COUNT");
        payRiskDayTranSum.cashSuccessCount = rs.getLong("CASH_SUCCESS_COUNT");
        payRiskDayTranSum.saleClientAmt = rs.getLong("SALE_CLIENT_AMT");
        payRiskDayTranSum.saleClientCount = rs.getLong("SALE_CLIENT_COUNT");
        payRiskDayTranSum.saleClientSuccessAmt = rs.getLong("SALE_CLIENT_SUCCESS_AMT");
        payRiskDayTranSum.saleClientSuccessCount = rs.getLong("SALE_CLIENT_SUCCESS_COUNT");
        payRiskDayTranSum.saleQuickAmt = rs.getLong("SALE_QUICK_AMT");
        payRiskDayTranSum.saleQuickCount = rs.getLong("SALE_QUICK_COUNT");
        payRiskDayTranSum.saleQuickSuccessAmt = rs.getLong("SALE_QUICK_SUCCESS_AMT");
        payRiskDayTranSum.saleQuickSuccessCount = rs.getLong("SALE_QUICK_SUCCESS_COUNT");
        payRiskDayTranSum.saleAccAmt = rs.getLong("SALE_ACC_AMT");
        payRiskDayTranSum.saleAccCount = rs.getLong("SALE_ACC_COUNT");
        payRiskDayTranSum.saleAccSuccessAmt = rs.getLong("SALE_ACC_SUCCESS_AMT");
        payRiskDayTranSum.saleAccSuccessCount = rs.getLong("SALE_ACC_SUCCESS_COUNT");
        payRiskDayTranSum.cardLastTranTime = rs.getTimestamp("CARD_LAST_TRAN_TIME");
        payRiskDayTranSum.rechargeCount = rs.getLong("RECHARGE_COUNT");
        payRiskDayTranSum.rechargeSuccessCount = rs.getLong("RECHARGE_SUCCESS_COUNT");
        payRiskDayTranSum.receiveAmt = rs.getLong("RECEIVE_AMT");
        payRiskDayTranSum.receiveCount = rs.getLong("RECEIVE_COUNT");
        payRiskDayTranSum.receiveSuccessAmt = rs.getLong("RECEIVE_SUCCESS_AMT");
        payRiskDayTranSum.receiveSuccessCount = rs.getLong("RECEIVE_SUCCESS_COUNT");
        payRiskDayTranSum.payAmt = rs.getLong("PAY_AMT");
        payRiskDayTranSum.payCount = rs.getLong("PAY_COUNT");
        payRiskDayTranSum.paySuccessAmt = rs.getLong("PAY_SUCCESS_AMT");
        payRiskDayTranSum.paySuccessCount = rs.getLong("PAY_SUCCESS_COUNT");
        return payRiskDayTranSum;
    }
    public String addPayRiskDayTranSum(PayRiskDayTranSum payRiskDayTranSum) throws Exception {
        String sql = "insert into PAY_RISK_DAY_TRAN_SUM("+
            "ID," + 
            "TIME_TYPE," + 
            "CUST_TYPE," + 
//            "USER_CODE," + 
//            "MER_NO," + 
			"CUST_ID," + 
            "STAT_DATE," + 
            "ALL_AMOUNT," + 
            "REFUND_AMOUNT," + 
            "SALE_AMOUNT," + 
            "RECHARGE_AMOUNT," + 
            "TRANSFER_AMOUNT," + 
            "CASH_AMOUNT," + 
            "PREDEPOSIT_AMOUNT," + 
            "ALL_COUNT," + 
            "REFUND_COUNT," + 
            "SALE_COUNT," + 
            "PREDEPOSIT_COUNT," + 
            "LESS_COUNT," + 
            "CREDIT_COUNT," + 
            "INT_COUNT," + 
            "AVER_AMOUNT," + 
            "MAX_AMOUNT," + 
            "ALL_SUCCESS_AMOUNT," + 
            "REFUND_SUCCESS_AMOUNT," + 
            "SALE_SUCCESS_AMOUNT," + 
            "RECHARGE_SUCCESS_AMOUNT," + 
            "TRANSFER_SUCCESS_AMOUNT," + 
            "CASH_SUCCESS_AMOUNT," + 
            "PREDEPOSIT_SUCCESS_AMOUNT," + 
            "ALL_SUCCESS_COUNT," + 
            "REFUND_SUCCESS_COUNT," + 
            "SALE_SUCCESS_COUNT," + 
            "PREDEPOSIT_SUCCESS_COUNT," + 
            "LESS_SUCCESS_COUNT," + 
            "CREDIT_SUCCESS_COUNT," + 
            "INT_SUCCESS_COUNT," +
            "SALE_CLIENT_AMT," + 
            "SALE_CLIENT_COUNT," + 
            "SALE_CLIENT_SUCCESS_AMT," + 
            "SALE_CLIENT_SUCCESS_COUNT," + 
            "SALE_QUICK_AMT," + 
            "SALE_QUICK_COUNT," + 
            "SALE_QUICK_SUCCESS_AMT," + 
            "SALE_QUICK_SUCCESS_COUNT," + 
            "SALE_ACC_AMT," + 
            "SALE_ACC_COUNT," + 
            "SALE_ACC_SUCCESS_AMT," + 
            "SALE_ACC_SUCCESS_COUNT," +
            "RECEIVE_AMT," + 
            "RECEIVE_COUNT," + 
            "RECEIVE_SUCCESS_AMT," + 
            "RECEIVE_SUCCESS_COUNT," + 
            "PAY_AMT," + 
            "PAY_COUNT," + 
            "PAY_SUCCESS_AMT," + 
            "PAY_SUCCESS_COUNT)values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            int n=1;
            ps.setString(n++,payRiskDayTranSum.id);
            ps.setString(n++,payRiskDayTranSum.timeType);
            ps.setString(n++,payRiskDayTranSum.custType);
//            ps.setString(n++,payRiskDayTranSum.userCode);
//            ps.setString(n++,payRiskDayTranSum.custId);
            ps.setString(n++,payRiskDayTranSum.custId);
            ps.setString(n++,payRiskDayTranSum.statDate);
            ps.setLong(n++,payRiskDayTranSum.allAmount);
            ps.setLong(n++,payRiskDayTranSum.refundAmount);
            ps.setLong(n++,payRiskDayTranSum.saleAmount);
            ps.setLong(n++,payRiskDayTranSum.rechargeAmount);
            ps.setLong(n++,payRiskDayTranSum.transferAmount);
            ps.setLong(n++,payRiskDayTranSum.cashAmount);
            ps.setLong(n++,payRiskDayTranSum.predepositAmount);
            ps.setLong(n++,payRiskDayTranSum.allCount);
            ps.setLong(n++,payRiskDayTranSum.refundCount);
            ps.setLong(n++,payRiskDayTranSum.saleCount);
            ps.setLong(n++,payRiskDayTranSum.predepositCount);
            ps.setLong(n++,payRiskDayTranSum.lessCount);
            ps.setLong(n++,payRiskDayTranSum.creditCount);
            ps.setLong(n++,payRiskDayTranSum.intCount);
            ps.setLong(n++,payRiskDayTranSum.averAmount);
            ps.setLong(n++,payRiskDayTranSum.maxAmount);
            ps.setLong(n++,payRiskDayTranSum.allSuccessAmount);
            ps.setLong(n++,payRiskDayTranSum.refundSuccessAmount);
            ps.setLong(n++,payRiskDayTranSum.saleSuccessAmount);
            ps.setLong(n++,payRiskDayTranSum.rechargeSuccessAmount);
            ps.setLong(n++,payRiskDayTranSum.transferSuccessAmount);
            ps.setLong(n++,payRiskDayTranSum.cashSuccessAmount);
            ps.setLong(n++,payRiskDayTranSum.predepositSuccessAmount);
            ps.setLong(n++,payRiskDayTranSum.allSuccessCount);
            ps.setLong(n++,payRiskDayTranSum.refundSuccessCount);
            ps.setLong(n++,payRiskDayTranSum.saleSuccessCount);
            ps.setLong(n++,payRiskDayTranSum.predepositSuccessCount);
            ps.setLong(n++,payRiskDayTranSum.lessSuccessCount);
            ps.setLong(n++,payRiskDayTranSum.creditSuccessCount);
            ps.setLong(n++,payRiskDayTranSum.intSuccessCount);
            ps.setLong(n++,payRiskDayTranSum.saleClientAmt);
            ps.setLong(n++,payRiskDayTranSum.saleClientCount);
            ps.setLong(n++,payRiskDayTranSum.saleClientSuccessAmt);
            ps.setLong(n++,payRiskDayTranSum.saleClientSuccessCount);
            ps.setLong(n++,payRiskDayTranSum.saleQuickAmt);
            ps.setLong(n++,payRiskDayTranSum.saleQuickCount);
            ps.setLong(n++,payRiskDayTranSum.saleQuickSuccessAmt);
            ps.setLong(n++,payRiskDayTranSum.saleQuickSuccessCount);
            ps.setLong(n++,payRiskDayTranSum.saleAccAmt);
            ps.setLong(n++,payRiskDayTranSum.saleAccCount);
            ps.setLong(n++,payRiskDayTranSum.saleAccSuccessAmt);
            ps.setLong(n++,payRiskDayTranSum.saleAccSuccessCount);
            ps.setLong(n++,payRiskDayTranSum.receiveAmt);
            ps.setLong(n++,payRiskDayTranSum.receiveCount);
            ps.setLong(n++,payRiskDayTranSum.receiveSuccessAmt);
            ps.setLong(n++,payRiskDayTranSum.receiveSuccessCount);
            ps.setLong(n++,payRiskDayTranSum.payAmt);
            ps.setLong(n++,payRiskDayTranSum.payCount);
            ps.setLong(n++,payRiskDayTranSum.paySuccessAmt);
            ps.setLong(n++,payRiskDayTranSum.paySuccessCount);
            ps.executeUpdate();
            return "";
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }finally {
            close(null, ps, con);
        }
    }
    /**
     * update PayRiskDayTranSum
     * @param payRiskDayTranSum
     * @throws Exception
     */
    public void updateOrAddPayRiskTranSumForSale(
    		PayRiskDayTranSum payRiskDayTranSum) throws Exception {
        String sqlUpdate = "update PAY_RISK_DAY_TRAN_SUM "+        
              "set ALL_AMOUNT=ALL_AMOUNT+?,SALE_AMOUNT=SALE_AMOUNT+?,RECHARGE_AMOUNT=RECHARGE_AMOUNT+?,RECHARGE_COUNT=RECHARGE_COUNT+?,ALL_COUNT=ALL_COUNT+?,SALE_COUNT=SALE_COUNT+?," +
              "LESS_COUNT=LESS_COUNT+?,INT_COUNT=INT_COUNT+?,SALE_CLIENT_AMT=SALE_CLIENT_AMT+?,SALE_CLIENT_COUNT=SALE_CLIENT_COUNT+?," +
              "SALE_QUICK_AMT=SALE_QUICK_AMT+?,SALE_QUICK_COUNT=SALE_QUICK_COUNT+?," +
              "SALE_ACC_AMT=SALE_ACC_AMT+?,SALE_ACC_COUNT=SALE_ACC_COUNT+?" +
              " where TIME_TYPE=? and CUST_TYPE=? and  CUST_ID=? and STAT_DATE=?";
        String sqlAdd = "insert into PAY_RISK_DAY_TRAN_SUM("+
                "ID,TIME_TYPE,CUST_TYPE,CUST_ID,STAT_DATE,ALL_AMOUNT,SALE_AMOUNT,RECHARGE_AMOUNT,RECHARGE_COUNT,ALL_COUNT,SALE_COUNT," +
                "LESS_COUNT,INT_COUNT,SALE_CLIENT_AMT,SALE_CLIENT_COUNT,SALE_QUICK_AMT,SALE_QUICK_COUNT,SALE_ACC_AMT,SALE_ACC_COUNT)" +
                " values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        log.info(sqlUpdate);
        log.info(sqlAdd);
        log.info(payRiskDayTranSum);
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = connection();
            //日交易汇总更新
            ps = con.prepareStatement(sqlUpdate);
            int n=1;
            ps.setLong(n++,payRiskDayTranSum.allAmount);
            ps.setLong(n++,payRiskDayTranSum.saleAmount);
            ps.setLong(n++,payRiskDayTranSum.rechargeAmount);
            ps.setLong(n++,payRiskDayTranSum.rechargeCount);
            ps.setLong(n++,payRiskDayTranSum.allCount);
            ps.setLong(n++,payRiskDayTranSum.saleCount);
            ps.setLong(n++,payRiskDayTranSum.lessCount);
            ps.setLong(n++,payRiskDayTranSum.intCount);
            ps.setLong(n++,payRiskDayTranSum.saleClientAmt);
            ps.setLong(n++,payRiskDayTranSum.saleClientCount);
            ps.setLong(n++,payRiskDayTranSum.saleQuickAmt);
            ps.setLong(n++,payRiskDayTranSum.saleQuickCount);
            ps.setLong(n++,payRiskDayTranSum.saleAccAmt);
            ps.setLong(n++,payRiskDayTranSum.saleAccCount);
            ps.setString(n++,payRiskDayTranSum.timeType);
            ps.setString(n++,payRiskDayTranSum.custType);
            ps.setString(n++,payRiskDayTranSum.custId);
            ps.setString(n++,payRiskDayTranSum.statDate);
            //没有当日的汇总，生成新记录
            if(ps.executeUpdate()==0){
                ps.close();
                ps=con.prepareStatement(sqlAdd);
                n=1;
                ps.setString(n++,Tools.getUniqueIdentify());
                ps.setString(n++,payRiskDayTranSum.timeType);
                ps.setString(n++,payRiskDayTranSum.custType);
                ps.setString(n++,payRiskDayTranSum.custId);
                ps.setString(n++,payRiskDayTranSum.statDate);
                ps.setLong(n++,payRiskDayTranSum.allAmount);
                ps.setLong(n++,payRiskDayTranSum.saleAmount);
                ps.setLong(n++,payRiskDayTranSum.rechargeAmount);
                ps.setLong(n++,payRiskDayTranSum.rechargeCount);
                ps.setLong(n++,payRiskDayTranSum.allCount);
                ps.setLong(n++,payRiskDayTranSum.saleCount);
                ps.setLong(n++,payRiskDayTranSum.lessCount);
                ps.setLong(n++,payRiskDayTranSum.intCount);
                ps.setLong(n++,payRiskDayTranSum.saleClientAmt);
                ps.setLong(n++,payRiskDayTranSum.saleClientCount);
                ps.setLong(n++,payRiskDayTranSum.saleQuickAmt);
                ps.setLong(n++,payRiskDayTranSum.saleQuickCount);
                ps.setLong(n++,payRiskDayTranSum.saleAccAmt);
                ps.setLong(n++,payRiskDayTranSum.saleAccCount);
                ps.executeUpdate();
            }
            //月交易汇总更新
            ps.close();
            payRiskDayTranSum.statDate = payRiskDayTranSum.statDate.substring(0,6);
            payRiskDayTranSum.timeType = "1";//0日 1月
            ps = con.prepareStatement(sqlUpdate);
            n=1;
            ps.setLong(n++,payRiskDayTranSum.allAmount);
            ps.setLong(n++,payRiskDayTranSum.saleAmount);
            ps.setLong(n++,payRiskDayTranSum.rechargeAmount);
            ps.setLong(n++,payRiskDayTranSum.rechargeCount);
            ps.setLong(n++,payRiskDayTranSum.allCount);
            ps.setLong(n++,payRiskDayTranSum.saleCount);
            ps.setLong(n++,payRiskDayTranSum.lessCount);
            ps.setLong(n++,payRiskDayTranSum.intCount);
            ps.setLong(n++,payRiskDayTranSum.saleClientAmt);
            ps.setLong(n++,payRiskDayTranSum.saleClientCount);
            ps.setLong(n++,payRiskDayTranSum.saleQuickAmt);
            ps.setLong(n++,payRiskDayTranSum.saleQuickCount);
            ps.setLong(n++,payRiskDayTranSum.saleAccAmt);
            ps.setLong(n++,payRiskDayTranSum.saleAccCount);
            ps.setString(n++,payRiskDayTranSum.timeType);
            ps.setString(n++,payRiskDayTranSum.custType);
            ps.setString(n++,payRiskDayTranSum.custId);
            ps.setString(n++,payRiskDayTranSum.statDate);
            //没有当月的汇总，生成新记录
            if(ps.executeUpdate()==0){
                ps.close();
                ps=con.prepareStatement(sqlAdd);
                n=1;
                ps.setString(n++,Tools.getUniqueIdentify());
                ps.setString(n++,payRiskDayTranSum.timeType);
                ps.setString(n++,payRiskDayTranSum.custType);
                ps.setString(n++,payRiskDayTranSum.custId);
                ps.setString(n++,payRiskDayTranSum.statDate);
                ps.setLong(n++,payRiskDayTranSum.allAmount);
                ps.setLong(n++,payRiskDayTranSum.saleAmount);
                ps.setLong(n++,payRiskDayTranSum.rechargeAmount);
                ps.setLong(n++,payRiskDayTranSum.rechargeCount);
                ps.setLong(n++,payRiskDayTranSum.allCount);
                ps.setLong(n++,payRiskDayTranSum.saleCount);
                ps.setLong(n++,payRiskDayTranSum.lessCount);
                ps.setLong(n++,payRiskDayTranSum.intCount);
                ps.setLong(n++,payRiskDayTranSum.saleClientAmt);
                ps.setLong(n++,payRiskDayTranSum.saleClientCount);
                ps.setLong(n++,payRiskDayTranSum.saleQuickAmt);
                ps.setLong(n++,payRiskDayTranSum.saleQuickCount);
                ps.setLong(n++,payRiskDayTranSum.saleAccAmt);
                ps.setLong(n++,payRiskDayTranSum.saleAccCount);
                ps.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }finally {
            close(null, ps, con);
        }
    }
    /**
     * update PayRiskDayTranSum
     * @param payRiskDayTranSum
     * @throws Exception
     */
    public void updateOrAddPayRiskTranSumForSuccessSale(
    		PayRiskDayTranSum payRiskDayTranSum) throws Exception {
        String sqlUpdate = "update PAY_RISK_DAY_TRAN_SUM "+        
              "set ALL_SUCCESS_AMOUNT=ALL_SUCCESS_AMOUNT+?,SALE_SUCCESS_AMOUNT=SALE_SUCCESS_AMOUNT+?,RECHARGE_SUCCESS_AMOUNT=RECHARGE_SUCCESS_AMOUNT+?,RECHARGE_SUCCESS_COUNT=RECHARGE_SUCCESS_COUNT+?," +
              "ALL_SUCCESS_COUNT=ALL_SUCCESS_COUNT+?,SALE_SUCCESS_COUNT=SALE_SUCCESS_COUNT+?," +
              "LESS_SUCCESS_COUNT=LESS_SUCCESS_COUNT+?,INT_SUCCESS_COUNT=INT_SUCCESS_COUNT+?," +
              "SALE_CLIENT_SUCCESS_AMT=SALE_CLIENT_SUCCESS_AMT+?,SALE_CLIENT_SUCCESS_COUNT=SALE_CLIENT_SUCCESS_COUNT+?," +
              "SALE_QUICK_SUCCESS_AMT=SALE_QUICK_SUCCESS_AMT+?,SALE_QUICK_SUCCESS_COUNT=SALE_QUICK_SUCCESS_COUNT+?," +
              "SALE_ACC_SUCCESS_AMT=SALE_ACC_SUCCESS_AMT+?,SALE_ACC_SUCCESS_COUNT=SALE_ACC_SUCCESS_COUNT+?,CARD_LAST_TRAN_TIME=to_date(?,'yyyy-mm-dd hh24:mi:ss')" +
              " where TIME_TYPE=? and CUST_TYPE=? and  CUST_ID=? and STAT_DATE=?";
        String sqlAdd = "insert into PAY_RISK_DAY_TRAN_SUM("+
                "ID,TIME_TYPE,CUST_TYPE,CUST_ID,STAT_DATE,ALL_SUCCESS_AMOUNT,SALE_SUCCESS_AMOUNT,RECHARGE_SUCCESS_AMOUNT,RECHARGE_SUCCESS_COUNT,ALL_SUCCESS_COUNT,SALE_SUCCESS_COUNT," +
                "LESS_SUCCESS_COUNT,INT_SUCCESS_COUNT,SALE_CLIENT_SUCCESS_AMT,SALE_CLIENT_SUCCESS_COUNT,SALE_QUICK_SUCCESS_AMT,SALE_QUICK_SUCCESS_COUNT,SALE_ACC_SUCCESS_AMT,SALE_ACC_SUCCESS_COUNT" +
                ",CARD_LAST_TRAN_TIME)" +
                "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,sysdate)";
        log.info(sqlUpdate);
        log.info(sqlAdd);
        log.info(payRiskDayTranSum);
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = connection();
            //日交易汇总更新
            ps = con.prepareStatement(sqlUpdate);
            int n=1;
            ps.setLong(n++,payRiskDayTranSum.allSuccessAmount);
            ps.setLong(n++,payRiskDayTranSum.saleSuccessAmount);
            ps.setLong(n++,payRiskDayTranSum.rechargeSuccessAmount);
            ps.setLong(n++,payRiskDayTranSum.rechargeSuccessCount);
            ps.setLong(n++,payRiskDayTranSum.allSuccessCount);
            ps.setLong(n++,payRiskDayTranSum.saleSuccessCount);
            ps.setLong(n++,payRiskDayTranSum.lessSuccessCount);
            ps.setLong(n++,payRiskDayTranSum.intSuccessCount);
            ps.setLong(n++,payRiskDayTranSum.saleClientSuccessAmt);
            ps.setLong(n++,payRiskDayTranSum.saleClientSuccessCount);
            ps.setLong(n++,payRiskDayTranSum.saleQuickSuccessAmt);
            ps.setLong(n++,payRiskDayTranSum.saleQuickSuccessCount);
            ps.setLong(n++,payRiskDayTranSum.saleAccSuccessAmt);
            ps.setLong(n++,payRiskDayTranSum.saleAccSuccessCount);
            ps.setString(n++,new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payRiskDayTranSum.cardLastTranTime));
            ps.setString(n++,payRiskDayTranSum.timeType);
            ps.setString(n++,payRiskDayTranSum.custType);
            ps.setString(n++,payRiskDayTranSum.custId);
            ps.setString(n++,payRiskDayTranSum.statDate);
            //没有当日的汇总，生成新记录
            if(ps.executeUpdate()==0){
                ps.close();
                ps=con.prepareStatement(sqlAdd);
                n=1;
                ps.setString(n++,Tools.getUniqueIdentify());
                ps.setString(n++,payRiskDayTranSum.timeType);
                ps.setString(n++,payRiskDayTranSum.custType);
                ps.setString(n++,payRiskDayTranSum.custId);
                ps.setString(n++,payRiskDayTranSum.statDate);
                ps.setLong(n++,payRiskDayTranSum.allSuccessAmount);
                ps.setLong(n++,payRiskDayTranSum.saleSuccessAmount);
                ps.setLong(n++,payRiskDayTranSum.rechargeSuccessAmount);
                ps.setLong(n++,payRiskDayTranSum.rechargeSuccessCount);
                ps.setLong(n++,payRiskDayTranSum.allSuccessCount);
                ps.setLong(n++,payRiskDayTranSum.saleSuccessCount);
                ps.setLong(n++,payRiskDayTranSum.lessSuccessCount);
                ps.setLong(n++,payRiskDayTranSum.intSuccessCount);
                ps.setLong(n++,payRiskDayTranSum.saleClientSuccessAmt);
                ps.setLong(n++,payRiskDayTranSum.saleClientSuccessCount);
                ps.setLong(n++,payRiskDayTranSum.saleQuickSuccessAmt);
                ps.setLong(n++,payRiskDayTranSum.saleQuickSuccessCount);
                ps.setLong(n++,payRiskDayTranSum.saleAccSuccessAmt);
                ps.setLong(n++,payRiskDayTranSum.saleAccSuccessCount);
                ps.executeUpdate();
            }
            //月交易汇总更新
            ps.close();
            payRiskDayTranSum.statDate = payRiskDayTranSum.statDate.substring(0,6);
            payRiskDayTranSum.timeType = "1";//0日 1月
            ps = con.prepareStatement(sqlUpdate);
            n=1;
            ps.setLong(n++,payRiskDayTranSum.allSuccessAmount);
            ps.setLong(n++,payRiskDayTranSum.saleSuccessAmount);
            ps.setLong(n++,payRiskDayTranSum.rechargeSuccessAmount);
            ps.setLong(n++,payRiskDayTranSum.rechargeSuccessCount);
            ps.setLong(n++,payRiskDayTranSum.allSuccessCount);
            ps.setLong(n++,payRiskDayTranSum.saleSuccessCount);
            ps.setLong(n++,payRiskDayTranSum.lessSuccessCount);
            ps.setLong(n++,payRiskDayTranSum.intSuccessCount);
            ps.setLong(n++,payRiskDayTranSum.saleClientSuccessAmt);
            ps.setLong(n++,payRiskDayTranSum.saleClientSuccessCount);
            ps.setLong(n++,payRiskDayTranSum.saleQuickSuccessAmt);
            ps.setLong(n++,payRiskDayTranSum.saleQuickSuccessCount);
            ps.setLong(n++,payRiskDayTranSum.saleAccSuccessAmt);
            ps.setLong(n++,payRiskDayTranSum.saleAccSuccessCount);
            ps.setString(n++,new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payRiskDayTranSum.cardLastTranTime));
            ps.setString(n++,payRiskDayTranSum.timeType);
            ps.setString(n++,payRiskDayTranSum.custType);
            ps.setString(n++,payRiskDayTranSum.custId);
            ps.setString(n++,payRiskDayTranSum.statDate);
            //没有当月的汇总，生成新记录
            if(ps.executeUpdate()==0){
                ps.close();
                ps=con.prepareStatement(sqlAdd);
                n=1;
                ps.setString(n++,Tools.getUniqueIdentify());
                ps.setString(n++,payRiskDayTranSum.timeType);
                ps.setString(n++,payRiskDayTranSum.custType);
                ps.setString(n++,payRiskDayTranSum.custId);
                ps.setString(n++,payRiskDayTranSum.statDate);
                ps.setLong(n++,payRiskDayTranSum.allSuccessAmount);
                ps.setLong(n++,payRiskDayTranSum.saleSuccessAmount);
                ps.setLong(n++,payRiskDayTranSum.rechargeSuccessAmount);
                ps.setLong(n++,payRiskDayTranSum.rechargeSuccessCount);
                ps.setLong(n++,payRiskDayTranSum.allSuccessCount);
                ps.setLong(n++,payRiskDayTranSum.saleSuccessCount);
                ps.setLong(n++,payRiskDayTranSum.lessSuccessCount);
                ps.setLong(n++,payRiskDayTranSum.intSuccessCount);
                ps.setLong(n++,payRiskDayTranSum.saleClientSuccessAmt);
                ps.setLong(n++,payRiskDayTranSum.saleClientSuccessCount);
                ps.setLong(n++,payRiskDayTranSum.saleQuickSuccessAmt);
                ps.setLong(n++,payRiskDayTranSum.saleQuickSuccessCount);
                ps.setLong(n++,payRiskDayTranSum.saleAccSuccessAmt);
                ps.setLong(n++,payRiskDayTranSum.saleAccSuccessCount);
                ps.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }finally {
            close(null, ps, con);
        }
    }
    /**
     * update PayRiskDayTranSum
     * @param payRiskDayTranSum
     * @throws Exception
     */
    public void updateOrAddPayRiskTranSumForRefund(
    		PayRiskDayTranSum payRiskDayTranSum) throws Exception {
        String sqlUpdate = "update PAY_RISK_DAY_TRAN_SUM "+        
                "set ALL_AMOUNT=ALL_AMOUNT+?,REFUND_AMOUNT=REFUND_AMOUNT+?,ALL_COUNT=ALL_COUNT+?,REFUND_COUNT=REFUND_COUNT+?," +
                "LESS_COUNT=LESS_COUNT+?,INT_COUNT=INT_COUNT+?" +
                " where TIME_TYPE=? and CUST_TYPE=? and  CUST_ID=? and STAT_DATE=?";
        String sqlAdd = "insert into PAY_RISK_DAY_TRAN_SUM("+
                "ID,TIME_TYPE,CUST_TYPE,CUST_ID,STAT_DATE,ALL_AMOUNT,REFUND_AMOUNT,ALL_COUNT,REFUND_COUNT," +
                "LESS_COUNT,INT_COUNT)values(?,?,?,?,?,?,?,?,?,?,?)";
        log.info(sqlUpdate);
        log.info(sqlAdd);
        log.info(payRiskDayTranSum);
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = connection();
            ps = con.prepareStatement(sqlUpdate);
            int n=1;
            ps.setLong(n++,payRiskDayTranSum.allAmount);
            ps.setLong(n++,payRiskDayTranSum.refundAmount);
            ps.setLong(n++,payRiskDayTranSum.allCount);
            ps.setLong(n++,payRiskDayTranSum.refundCount);
            ps.setLong(n++,payRiskDayTranSum.lessCount);
            ps.setLong(n++,payRiskDayTranSum.intCount);
            ps.setString(n++,payRiskDayTranSum.timeType);
            ps.setString(n++,payRiskDayTranSum.custType);
            ps.setString(n++,payRiskDayTranSum.custId);
            ps.setString(n++,payRiskDayTranSum.statDate);
            //没有当日的汇总，生成新记录
            if(ps.executeUpdate()==0){
                ps.close();
                ps=con.prepareStatement(sqlAdd);
                n=1;
                ps.setString(n++,Tools.getUniqueIdentify());
                ps.setString(n++,payRiskDayTranSum.timeType);
                ps.setString(n++,payRiskDayTranSum.custType);
                ps.setString(n++,payRiskDayTranSum.custId);
                ps.setString(n++,payRiskDayTranSum.statDate);
                ps.setLong(n++,payRiskDayTranSum.allAmount);
                ps.setLong(n++,payRiskDayTranSum.refundAmount);
                ps.setLong(n++,payRiskDayTranSum.allCount);
                ps.setLong(n++,payRiskDayTranSum.refundCount);
                ps.setLong(n++,payRiskDayTranSum.lessCount);
                ps.setLong(n++,payRiskDayTranSum.intCount);
                ps.executeUpdate();
            }
            ps.close();
            //月交易汇总更新
            payRiskDayTranSum.statDate = payRiskDayTranSum.statDate.substring(0,6);
            payRiskDayTranSum.timeType = "1";//0日 1月
            ps = con.prepareStatement(sqlUpdate);
            n=1;
            ps.setLong(n++,payRiskDayTranSum.allAmount);
            ps.setLong(n++,payRiskDayTranSum.refundAmount);
            ps.setLong(n++,payRiskDayTranSum.allCount);
            ps.setLong(n++,payRiskDayTranSum.refundCount);
            ps.setLong(n++,payRiskDayTranSum.lessCount);
            ps.setLong(n++,payRiskDayTranSum.intCount);
            ps.setString(n++,payRiskDayTranSum.timeType);
            ps.setString(n++,payRiskDayTranSum.custType);
            ps.setString(n++,payRiskDayTranSum.custId);
            ps.setString(n++,payRiskDayTranSum.statDate);
            //没有当日的汇总，生成新记录
            if(ps.executeUpdate()==0){
                ps.close();
                ps=con.prepareStatement(sqlAdd);
                n=1;
                ps.setString(n++,Tools.getUniqueIdentify());
                ps.setString(n++,payRiskDayTranSum.timeType);
                ps.setString(n++,payRiskDayTranSum.custType);
                ps.setString(n++,payRiskDayTranSum.custId);
                ps.setString(n++,payRiskDayTranSum.statDate);
                ps.setLong(n++,payRiskDayTranSum.allAmount);
                ps.setLong(n++,payRiskDayTranSum.refundAmount);
                ps.setLong(n++,payRiskDayTranSum.allCount);
                ps.setLong(n++,payRiskDayTranSum.refundCount);
                ps.setLong(n++,payRiskDayTranSum.lessCount);
                ps.setLong(n++,payRiskDayTranSum.intCount);
                ps.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(null, ps, con);
        }
    }
    /**
     * update PayRiskDayTranSum
     * @param payRiskDayTranSum
     * @throws Exception
     */
    public void updateOrAddPayRiskTranSumForSuccessRefund(
    		PayRiskDayTranSum payRiskDayTranSum) throws Exception {
        String sqlUpdate = "update PAY_RISK_DAY_TRAN_SUM "+        
                "set ALL_SUCCESS_AMOUNT=ALL_SUCCESS_AMOUNT+?,REFUND_SUCCESS_AMOUNT=REFUND_SUCCESS_AMOUNT+?,ALL_SUCCESS_COUNT=ALL_SUCCESS_COUNT+?,REFUND_SUCCESS_COUNT=REFUND_SUCCESS_COUNT+?," +
                "LESS_SUCCESS_COUNT=LESS_SUCCESS_COUNT+?,INT_SUCCESS_COUNT=INT_SUCCESS_COUNT+?" +
                " where TIME_TYPE=? and CUST_TYPE=? and  CUST_ID=? and STAT_DATE=?";
        String sqlAdd = "insert into PAY_RISK_DAY_TRAN_SUM("+
                "ID,TIME_TYPE,CUST_TYPE,CUST_ID,STAT_DATE,ALL_SUCCESS_AMOUNT,REFUND_SUCCESS_AMOUNT,ALL_SUCCESS_COUNT,REFUND_SUCCESS_COUNT," +
                "LESS_SUCCESS_COUNT,INT_SUCCESS_COUNT)values(?,?,?,?,?,?,?,?,?,?,?)";
        log.info(sqlUpdate);
        log.info(sqlAdd);
        log.info(payRiskDayTranSum);
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = connection();
            ps = con.prepareStatement(sqlUpdate);
            int n=1;
            ps.setLong(n++,payRiskDayTranSum.allSuccessAmount);
            ps.setLong(n++,payRiskDayTranSum.refundSuccessAmount);
            ps.setLong(n++,payRiskDayTranSum.allSuccessCount);
            ps.setLong(n++,payRiskDayTranSum.refundSuccessCount);
            ps.setLong(n++,payRiskDayTranSum.lessSuccessCount);
            ps.setLong(n++,payRiskDayTranSum.intSuccessCount);
            ps.setString(n++,payRiskDayTranSum.timeType);
            ps.setString(n++,payRiskDayTranSum.custType);
            ps.setString(n++,payRiskDayTranSum.custId);
            ps.setString(n++,payRiskDayTranSum.statDate);
            //没有当日的汇总，生成新记录
            if(ps.executeUpdate()==0){
                ps.close();
                ps=con.prepareStatement(sqlAdd);
                n=1;
                ps.setString(n++,Tools.getUniqueIdentify());
                ps.setString(n++,payRiskDayTranSum.timeType);
                ps.setString(n++,payRiskDayTranSum.custType);
                ps.setString(n++,payRiskDayTranSum.custId);
                ps.setString(n++,payRiskDayTranSum.statDate);
                ps.setLong(n++,payRiskDayTranSum.allSuccessAmount);
                ps.setLong(n++,payRiskDayTranSum.refundSuccessAmount);
                ps.setLong(n++,payRiskDayTranSum.allSuccessCount);
                ps.setLong(n++,payRiskDayTranSum.refundSuccessCount);
                ps.setLong(n++,payRiskDayTranSum.lessSuccessCount);
                ps.setLong(n++,payRiskDayTranSum.intSuccessCount);
                ps.executeUpdate();
            }
            ps.close();
            //月交易汇总更新
            payRiskDayTranSum.statDate = payRiskDayTranSum.statDate.substring(0,6);
            payRiskDayTranSum.timeType = "1";//0日 1月
            ps = con.prepareStatement(sqlUpdate);
            n=1;
            ps.setLong(n++,payRiskDayTranSum.allSuccessAmount);
            ps.setLong(n++,payRiskDayTranSum.refundSuccessAmount);
            ps.setLong(n++,payRiskDayTranSum.allSuccessCount);
            ps.setLong(n++,payRiskDayTranSum.refundSuccessCount);
            ps.setLong(n++,payRiskDayTranSum.lessSuccessCount);
            ps.setLong(n++,payRiskDayTranSum.intSuccessCount);
            ps.setString(n++,payRiskDayTranSum.timeType);
            ps.setString(n++,payRiskDayTranSum.custType);
            ps.setString(n++,payRiskDayTranSum.custId);
            ps.setString(n++,payRiskDayTranSum.statDate);
            //没有当月的汇总，生成新记录
            if(ps.executeUpdate()==0){
                ps.close();
                ps=con.prepareStatement(sqlAdd);
                n=1;
                ps.setString(n++,Tools.getUniqueIdentify());
                ps.setString(n++,payRiskDayTranSum.timeType);
                ps.setString(n++,payRiskDayTranSum.custType);
                ps.setString(n++,payRiskDayTranSum.custId);
                ps.setString(n++,payRiskDayTranSum.statDate);
                ps.setLong(n++,payRiskDayTranSum.allSuccessAmount);
                ps.setLong(n++,payRiskDayTranSum.refundSuccessAmount);
                ps.setLong(n++,payRiskDayTranSum.allSuccessCount);
                ps.setLong(n++,payRiskDayTranSum.refundSuccessCount);
                ps.setLong(n++,payRiskDayTranSum.lessSuccessCount);
                ps.setLong(n++,payRiskDayTranSum.intSuccessCount);
                ps.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(null, ps, con);
        }
    }
    /**
     * update PayRiskDayTranSum
     * @param payRiskDayTranSum
     * @throws Exception
     */
    public void updateOrAddPayRiskTranSumForTransfer(
    		PayRiskDayTranSum payRiskDayTranSum) throws Exception {
        String sqlUpdate = "update PAY_RISK_DAY_TRAN_SUM "+        
                "set ALL_AMOUNT=ALL_AMOUNT+?,ALL_SUCCESS_AMOUNT=ALL_SUCCESS_AMOUNT+?,TRANSFER_AMOUNT=TRANSFER_AMOUNT+?,TRANSFER_SUCCESS_AMOUNT=TRANSFER_SUCCESS_AMOUNT+?," +
                "ALL_COUNT=ALL_COUNT+?,ALL_SUCCESS_COUNT=ALL_SUCCESS_COUNT+?,TRANSFER_COUNT=TRANSFER_COUNT+?,TRANSFER_SUCCESS_COUNT=TRANSFER_SUCCESS_COUNT+?," +
                "LESS_COUNT=LESS_COUNT+?,INT_COUNT=INT_COUNT+?,LESS_SUCCESS_COUNT=LESS_SUCCESS_COUNT+?,INT_SUCCESS_COUNT=INT_SUCCESS_COUNT+?" +
                " where TIME_TYPE=? and CUST_TYPE=? and  CUST_ID=? and STAT_DATE=?";
        String sqlAdd = "insert into PAY_RISK_DAY_TRAN_SUM("+
                "ID,TIME_TYPE,CUST_TYPE,CUST_ID,STAT_DATE,ALL_AMOUNT,ALL_SUCCESS_AMOUNT,TRANSFER_AMOUNT,TRANSFER_SUCCESS_AMOUNT," +
                "ALL_COUNT,ALL_SUCCESS_COUNT,TRANSFER_COUNT,TRANSFER_SUCCESS_COUNT,LESS_COUNT,INT_COUNT,LESS_SUCCESS_COUNT,INT_SUCCESS_COUNT)values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        log.info(sqlUpdate);
        log.info(sqlAdd);
        log.info(payRiskDayTranSum);
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = connection();
            ps = con.prepareStatement(sqlUpdate);
            int n=1;
            ps.setLong(n++,payRiskDayTranSum.allAmount);
            ps.setLong(n++,payRiskDayTranSum.allSuccessAmount);
            ps.setLong(n++,payRiskDayTranSum.transferAmount);
            ps.setLong(n++,payRiskDayTranSum.transferSuccessAmount);
            ps.setLong(n++,payRiskDayTranSum.allCount);
            ps.setLong(n++,payRiskDayTranSum.allSuccessCount);
            ps.setLong(n++,payRiskDayTranSum.transferCount);
            ps.setLong(n++,payRiskDayTranSum.transferSuccessCount);
            ps.setLong(n++,payRiskDayTranSum.lessCount);
            ps.setLong(n++,payRiskDayTranSum.intCount);
            ps.setLong(n++,payRiskDayTranSum.lessSuccessCount);
            ps.setLong(n++,payRiskDayTranSum.intSuccessCount);
            ps.setString(n++,payRiskDayTranSum.timeType);
            ps.setString(n++,payRiskDayTranSum.custType);
            ps.setString(n++,payRiskDayTranSum.custId);
            ps.setString(n++,payRiskDayTranSum.statDate);
            int kk = ps.executeUpdate();
            //没有当日的汇总，生成新记录
            if(kk==0){
            	log.info(sqlAdd);
                ps.close();
                ps=con.prepareStatement(sqlAdd);
                n=1;
                ps.setString(n++,Tools.getUniqueIdentify());
                ps.setString(n++,payRiskDayTranSum.timeType);
                ps.setString(n++,payRiskDayTranSum.custType);
                ps.setString(n++,payRiskDayTranSum.custId);
                ps.setString(n++,payRiskDayTranSum.statDate);
                ps.setLong(n++,payRiskDayTranSum.allAmount);
                ps.setLong(n++,payRiskDayTranSum.allSuccessAmount);
                ps.setLong(n++,payRiskDayTranSum.transferAmount);
                ps.setLong(n++,payRiskDayTranSum.transferSuccessAmount);
                ps.setLong(n++,payRiskDayTranSum.allCount);
                ps.setLong(n++,payRiskDayTranSum.allSuccessCount);
                ps.setLong(n++,payRiskDayTranSum.transferCount);
                ps.setLong(n++,payRiskDayTranSum.transferSuccessCount);
                ps.setLong(n++,payRiskDayTranSum.lessCount);
                ps.setLong(n++,payRiskDayTranSum.intCount);
                ps.setLong(n++,payRiskDayTranSum.lessSuccessCount);
                ps.setLong(n++,payRiskDayTranSum.intSuccessCount);
                ps.executeUpdate();
            }
            ps.close();
            //月交易汇总更新
            payRiskDayTranSum.statDate = payRiskDayTranSum.statDate.substring(0,6);
            payRiskDayTranSum.timeType = "1";//0日 1月
            ps = con.prepareStatement(sqlUpdate);
            n=1;
            ps.setLong(n++,payRiskDayTranSum.allAmount);
            ps.setLong(n++,payRiskDayTranSum.allSuccessAmount);
            ps.setLong(n++,payRiskDayTranSum.transferAmount);
            ps.setLong(n++,payRiskDayTranSum.transferSuccessAmount);
            ps.setLong(n++,payRiskDayTranSum.allCount);
            ps.setLong(n++,payRiskDayTranSum.allSuccessCount);
            ps.setLong(n++,payRiskDayTranSum.transferCount);
            ps.setLong(n++,payRiskDayTranSum.transferSuccessCount);
            ps.setLong(n++,payRiskDayTranSum.lessCount);
            ps.setLong(n++,payRiskDayTranSum.intCount);
            ps.setLong(n++,payRiskDayTranSum.lessSuccessCount);
            ps.setLong(n++,payRiskDayTranSum.intSuccessCount);
            ps.setString(n++,payRiskDayTranSum.timeType);
            ps.setString(n++,payRiskDayTranSum.custType);
            ps.setString(n++,payRiskDayTranSum.custId);
            ps.setString(n++,payRiskDayTranSum.statDate);
            //没有当日的汇总，生成新记录
            if(ps.executeUpdate()==0){
                ps.close();
                ps=con.prepareStatement(sqlAdd);
                n=1;
                ps.setString(n++,Tools.getUniqueIdentify());
                ps.setString(n++,payRiskDayTranSum.timeType);
                ps.setString(n++,payRiskDayTranSum.custType);
                ps.setString(n++,payRiskDayTranSum.custId);
                ps.setString(n++,payRiskDayTranSum.statDate);
                ps.setLong(n++,payRiskDayTranSum.allAmount);
                ps.setLong(n++,payRiskDayTranSum.allSuccessAmount);
                ps.setLong(n++,payRiskDayTranSum.transferAmount);
                ps.setLong(n++,payRiskDayTranSum.transferSuccessAmount);
                ps.setLong(n++,payRiskDayTranSum.allCount);
                ps.setLong(n++,payRiskDayTranSum.allSuccessCount);
                ps.setLong(n++,payRiskDayTranSum.transferCount);
                ps.setLong(n++,payRiskDayTranSum.transferSuccessCount);
                ps.setLong(n++,payRiskDayTranSum.lessCount);
                ps.setLong(n++,payRiskDayTranSum.intCount);
                ps.setLong(n++,payRiskDayTranSum.lessSuccessCount);
                ps.setLong(n++,payRiskDayTranSum.intSuccessCount);
                ps.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(null, ps, con);
        }
    }
	public List<PayRiskDayTranSum> getCustTranSum(String custType,String custId, String statDate) {
        String sql = "select * from PAY_RISK_DAY_TRAN_SUM where CUST_TYPE=? and CUST_ID=? and " +
        	"(STAT_DATE='"+statDate+"' or STAT_DATE='"+statDate.substring(0,6)+"')";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List list = new ArrayList();
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            ps.setString(1, custType);
            ps.setString(2, custId);
            rs = ps.executeQuery();
            while(rs.next()){
                list.add(getPayRiskDayTranSumValue(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(rs, ps, con);
        }
        return list;
    }
	public void updateOrAddPayRiskTranSumForWithdraw(PayRiskDayTranSum payRiskDayTranSum) throws Exception {
        String sqlUpdate = "update PAY_RISK_DAY_TRAN_SUM "+        
                "set ALL_AMOUNT=ALL_AMOUNT+?,CASH_AMOUNT=CASH_AMOUNT+?,ALL_COUNT=ALL_COUNT+?,CASH_COUNT=CASH_COUNT+?," +
                "LESS_COUNT=LESS_COUNT+?,INT_COUNT=INT_COUNT+?" +
                " where TIME_TYPE=? and CUST_TYPE=? and  CUST_ID=? and STAT_DATE=?";
        String sqlAdd = "insert into PAY_RISK_DAY_TRAN_SUM("+
                "ID,TIME_TYPE,CUST_TYPE,CUST_ID,STAT_DATE,ALL_AMOUNT,CASH_AMOUNT,ALL_COUNT,CASH_COUNT," +
                "LESS_COUNT,INT_COUNT)values(?,?,?,?,?,?,?,?,?,?,?)";
        log.info(sqlUpdate);
        log.info(sqlAdd);
        log.info(payRiskDayTranSum);
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = connection();
            ps = con.prepareStatement(sqlUpdate);
            int n=1;
            ps.setLong(n++,payRiskDayTranSum.allAmount);
            ps.setLong(n++,payRiskDayTranSum.cashAmount);
            ps.setLong(n++,payRiskDayTranSum.allCount);
            ps.setLong(n++,payRiskDayTranSum.cashCount);
            ps.setLong(n++,payRiskDayTranSum.lessCount);
            ps.setLong(n++,payRiskDayTranSum.intCount);
            ps.setString(n++,payRiskDayTranSum.timeType);
            ps.setString(n++,payRiskDayTranSum.custType);
            ps.setString(n++,payRiskDayTranSum.custId);
            ps.setString(n++,payRiskDayTranSum.statDate);
            //没有当日的汇总，生成新记录
            if(ps.executeUpdate()==0){
                ps.close();
                ps=con.prepareStatement(sqlAdd);
                n=1;
                ps.setString(n++,Tools.getUniqueIdentify());
                ps.setString(n++,payRiskDayTranSum.timeType);
                ps.setString(n++,payRiskDayTranSum.custType);
                ps.setString(n++,payRiskDayTranSum.custId);
                ps.setString(n++,payRiskDayTranSum.statDate);
                ps.setLong(n++,payRiskDayTranSum.allAmount);
                ps.setLong(n++,payRiskDayTranSum.cashAmount);
                ps.setLong(n++,payRiskDayTranSum.allCount);
                ps.setLong(n++,payRiskDayTranSum.cashCount);
                ps.setLong(n++,payRiskDayTranSum.lessCount);
                ps.setLong(n++,payRiskDayTranSum.intCount);
                ps.executeUpdate();
            }
            ps.close();
            //月交易汇总更新
            payRiskDayTranSum.statDate = payRiskDayTranSum.statDate.substring(0,6);
            payRiskDayTranSum.timeType = "1";//0日 1月
            ps = con.prepareStatement(sqlUpdate);
            n=1;
            ps.setLong(n++,payRiskDayTranSum.allAmount);
            ps.setLong(n++,payRiskDayTranSum.cashAmount);
            ps.setLong(n++,payRiskDayTranSum.allCount);
            ps.setLong(n++,payRiskDayTranSum.cashCount);
            ps.setLong(n++,payRiskDayTranSum.lessCount);
            ps.setLong(n++,payRiskDayTranSum.intCount);
            ps.setString(n++,payRiskDayTranSum.timeType);
            ps.setString(n++,payRiskDayTranSum.custType);
            ps.setString(n++,payRiskDayTranSum.custId);
            ps.setString(n++,payRiskDayTranSum.statDate);
            //没有当日的汇总，生成新记录
            if(ps.executeUpdate()==0){
                ps.close();
                ps=con.prepareStatement(sqlAdd);
                n=1;
                ps.setString(n++,Tools.getUniqueIdentify());
                ps.setString(n++,payRiskDayTranSum.timeType);
                ps.setString(n++,payRiskDayTranSum.custType);
                ps.setString(n++,payRiskDayTranSum.custId);
                ps.setString(n++,payRiskDayTranSum.statDate);
                ps.setLong(n++,payRiskDayTranSum.allAmount);
                ps.setLong(n++,payRiskDayTranSum.cashAmount);
                ps.setLong(n++,payRiskDayTranSum.allCount);
                ps.setLong(n++,payRiskDayTranSum.cashCount);
                ps.setLong(n++,payRiskDayTranSum.lessCount);
                ps.setLong(n++,payRiskDayTranSum.intCount);
                ps.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(null, ps, con);
        }
	}
    public void updateOrAddPayRiskTranSumForSuccessWithdraw(
    		PayRiskDayTranSum payRiskDayTranSum) throws Exception {
        String sqlUpdate = "update PAY_RISK_DAY_TRAN_SUM "+        
                "set ALL_SUCCESS_AMOUNT=ALL_SUCCESS_AMOUNT+?,CASH_SUCCESS_AMOUNT=CASH_SUCCESS_AMOUNT+?,ALL_SUCCESS_COUNT=ALL_SUCCESS_COUNT+?,CASH_SUCCESS_COUNT=CASH_SUCCESS_COUNT+?," +
                "LESS_SUCCESS_COUNT=LESS_SUCCESS_COUNT+?,INT_SUCCESS_COUNT=INT_SUCCESS_COUNT+?" +
                " where TIME_TYPE=? and CUST_TYPE=? and  CUST_ID=? and STAT_DATE=?";
        String sqlAdd = "insert into PAY_RISK_DAY_TRAN_SUM("+
                "ID,TIME_TYPE,CUST_TYPE,CUST_ID,STAT_DATE,ALL_SUCCESS_AMOUNT,CASH_SUCCESS_AMOUNT,ALL_SUCCESS_COUNT,CASH_SUCCESS_COUNT," +
                "LESS_SUCCESS_COUNT,INT_SUCCESS_COUNT)values(?,?,?,?,?,?,?,?,?,?,?)";
        log.info(sqlUpdate);
        log.info(sqlAdd);
        log.info(payRiskDayTranSum);
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = connection();
            ps = con.prepareStatement(sqlUpdate);
            int n=1;
            ps.setLong(n++,payRiskDayTranSum.allSuccessAmount);
            ps.setLong(n++,payRiskDayTranSum.cashSuccessAmount);
            ps.setLong(n++,payRiskDayTranSum.allSuccessCount);
            ps.setLong(n++,payRiskDayTranSum.cashSuccessCount);
            ps.setLong(n++,payRiskDayTranSum.lessSuccessCount);
            ps.setLong(n++,payRiskDayTranSum.intSuccessCount);
            ps.setString(n++,payRiskDayTranSum.timeType);
            ps.setString(n++,payRiskDayTranSum.custType);
            ps.setString(n++,payRiskDayTranSum.custId);
            ps.setString(n++,payRiskDayTranSum.statDate);
            //没有当日的汇总，生成新记录
            if(ps.executeUpdate()==0){
                ps.close();
                ps=con.prepareStatement(sqlAdd);
                n=1;
                ps.setString(n++,Tools.getUniqueIdentify());
                ps.setString(n++,payRiskDayTranSum.timeType);
                ps.setString(n++,payRiskDayTranSum.custType);
                ps.setString(n++,payRiskDayTranSum.custId);
                ps.setString(n++,payRiskDayTranSum.statDate);
                ps.setLong(n++,payRiskDayTranSum.allSuccessAmount);
                ps.setLong(n++,payRiskDayTranSum.cashSuccessAmount);
                ps.setLong(n++,payRiskDayTranSum.allSuccessCount);
                ps.setLong(n++,payRiskDayTranSum.cashSuccessCount);
                ps.setLong(n++,payRiskDayTranSum.lessSuccessCount);
                ps.setLong(n++,payRiskDayTranSum.intSuccessCount);
                ps.executeUpdate();
            }
            ps.close();
            //月交易汇总更新
            payRiskDayTranSum.statDate = payRiskDayTranSum.statDate.substring(0,6);
            payRiskDayTranSum.timeType = "1";//0日 1月
            ps = con.prepareStatement(sqlUpdate);
            n=1;
            ps.setLong(n++,payRiskDayTranSum.allSuccessAmount);
            ps.setLong(n++,payRiskDayTranSum.cashSuccessAmount);
            ps.setLong(n++,payRiskDayTranSum.allSuccessCount);
            ps.setLong(n++,payRiskDayTranSum.cashSuccessCount);
            ps.setLong(n++,payRiskDayTranSum.lessSuccessCount);
            ps.setLong(n++,payRiskDayTranSum.intSuccessCount);
            ps.setString(n++,payRiskDayTranSum.timeType);
            ps.setString(n++,payRiskDayTranSum.custType);
            ps.setString(n++,payRiskDayTranSum.custId);
            ps.setString(n++,payRiskDayTranSum.statDate);
            //没有当月的汇总，生成新记录
            if(ps.executeUpdate()==0){
                ps.close();
                ps=con.prepareStatement(sqlAdd);
                n=1;
                ps.setString(n++,Tools.getUniqueIdentify());
                ps.setString(n++,payRiskDayTranSum.timeType);
                ps.setString(n++,payRiskDayTranSum.custType);
                ps.setString(n++,payRiskDayTranSum.custId);
                ps.setString(n++,payRiskDayTranSum.statDate);
                ps.setLong(n++,payRiskDayTranSum.allSuccessAmount);
                ps.setLong(n++,payRiskDayTranSum.cashSuccessAmount);
                ps.setLong(n++,payRiskDayTranSum.allSuccessCount);
                ps.setLong(n++,payRiskDayTranSum.cashSuccessCount);
                ps.setLong(n++,payRiskDayTranSum.lessSuccessCount);
                ps.setLong(n++,payRiskDayTranSum.intSuccessCount);
                ps.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(null, ps, con);
        }
    }
    /**
     * update PayRiskDayTranSum
     * @param payRiskDayTranSum
     * @throws Exception
     */
    public void updateOrAddPayRiskReceivePaySum(
    		PayRiskDayTranSum payRiskDayTranSum) throws Exception {
        String sqlUpdate = "update PAY_RISK_DAY_TRAN_SUM "+        
                "set RECEIVE_AMT=RECEIVE_AMT+?,RECEIVE_COUNT=RECEIVE_COUNT+?,RECEIVE_SUCCESS_AMT=RECEIVE_SUCCESS_AMT+?," +
                "RECEIVE_SUCCESS_COUNT=RECEIVE_SUCCESS_COUNT+?,PAY_AMT=PAY_AMT+?,PAY_COUNT=PAY_COUNT+?,PAY_SUCCESS_AMT=PAY_SUCCESS_AMT+?,PAY_SUCCESS_COUNT=PAY_SUCCESS_COUNT+?" +
                " where TIME_TYPE=? and CUST_TYPE=? and  CUST_ID=? and STAT_DATE=?";
        String sqlAdd = "insert into PAY_RISK_DAY_TRAN_SUM("+
                "ID,TIME_TYPE,CUST_TYPE,CUST_ID,STAT_DATE," +
                "RECEIVE_AMT,RECEIVE_COUNT,RECEIVE_SUCCESS_AMT,RECEIVE_SUCCESS_COUNT,PAY_AMT,PAY_COUNT,PAY_SUCCESS_AMT,PAY_SUCCESS_COUNT)values(?,?,?,?,?,?,?,?,?,?,?,?,?)";
        log.info(sqlUpdate);
        log.info(sqlAdd);
        log.info(payRiskDayTranSum);
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = connection();
            ps = con.prepareStatement(sqlUpdate);
            int n=1;
            ps.setLong(n++,payRiskDayTranSum.receiveAmt);
            ps.setLong(n++,payRiskDayTranSum.receiveCount);
            ps.setLong(n++,payRiskDayTranSum.receiveSuccessAmt);
            ps.setLong(n++,payRiskDayTranSum.receiveSuccessCount);
            ps.setLong(n++,payRiskDayTranSum.payAmt);
            ps.setLong(n++,payRiskDayTranSum.payCount);
            ps.setLong(n++,payRiskDayTranSum.paySuccessAmt);
            ps.setLong(n++,payRiskDayTranSum.paySuccessCount);
            ps.setString(n++,payRiskDayTranSum.timeType);
            ps.setString(n++,payRiskDayTranSum.custType);
            ps.setString(n++,payRiskDayTranSum.custId);
            ps.setString(n++,payRiskDayTranSum.statDate);
            //没有当日的汇总，生成新记录
            if(ps.executeUpdate()==0){
            	try{
	                ps.close();
	                ps=con.prepareStatement(sqlAdd);
	                n=1;
	                ps.setString(n++,Tools.getUniqueIdentify());
	                ps.setString(n++,payRiskDayTranSum.timeType);
	                ps.setString(n++,payRiskDayTranSum.custType);
	                ps.setString(n++,payRiskDayTranSum.custId);
	                ps.setString(n++,payRiskDayTranSum.statDate);
	                ps.setLong(n++,payRiskDayTranSum.receiveAmt);
	                ps.setLong(n++,payRiskDayTranSum.receiveCount);
	                ps.setLong(n++,payRiskDayTranSum.receiveSuccessAmt);
	                ps.setLong(n++,payRiskDayTranSum.receiveSuccessCount);
	                ps.setLong(n++,payRiskDayTranSum.payAmt);
	                ps.setLong(n++,payRiskDayTranSum.payCount);
	                ps.setLong(n++,payRiskDayTranSum.paySuccessAmt);
	                ps.setLong(n++,payRiskDayTranSum.paySuccessCount);
	                ps.executeUpdate();
            	} catch(Exception e){
            		ps.close();
            		ps = con.prepareStatement(sqlUpdate);
                    n=1;
                    ps.setLong(n++,payRiskDayTranSum.receiveAmt);
                    ps.setLong(n++,payRiskDayTranSum.receiveCount);
                    ps.setLong(n++,payRiskDayTranSum.receiveSuccessAmt);
                    ps.setLong(n++,payRiskDayTranSum.receiveSuccessCount);
                    ps.setLong(n++,payRiskDayTranSum.payAmt);
                    ps.setLong(n++,payRiskDayTranSum.payCount);
                    ps.setLong(n++,payRiskDayTranSum.paySuccessAmt);
                    ps.setLong(n++,payRiskDayTranSum.paySuccessCount);
                    ps.setString(n++,payRiskDayTranSum.timeType);
                    ps.setString(n++,payRiskDayTranSum.custType);
                    ps.setString(n++,payRiskDayTranSum.custId);
                    ps.setString(n++,payRiskDayTranSum.statDate);
                    ps.executeUpdate();
            	}
            }
            ps.close();
            //月交易汇总更新
            payRiskDayTranSum.statDate = payRiskDayTranSum.statDate.substring(0,6);
            payRiskDayTranSum.timeType = "1";//0日 1月
            ps = con.prepareStatement(sqlUpdate);
            n=1;
            ps.setLong(n++,payRiskDayTranSum.receiveAmt);
            ps.setLong(n++,payRiskDayTranSum.receiveCount);
            ps.setLong(n++,payRiskDayTranSum.receiveSuccessAmt);
            ps.setLong(n++,payRiskDayTranSum.receiveSuccessCount);
            ps.setLong(n++,payRiskDayTranSum.payAmt);
            ps.setLong(n++,payRiskDayTranSum.payCount);
            ps.setLong(n++,payRiskDayTranSum.paySuccessAmt);
            ps.setLong(n++,payRiskDayTranSum.paySuccessCount);
            ps.setString(n++,payRiskDayTranSum.timeType);
            ps.setString(n++,payRiskDayTranSum.custType);
            ps.setString(n++,payRiskDayTranSum.custId);
            ps.setString(n++,payRiskDayTranSum.statDate);
            //没有当月的汇总，生成新记录
            if(ps.executeUpdate()==0){
            	try{
	                ps.close();
	                ps=con.prepareStatement(sqlAdd);
	                n=1;
	                ps.setString(n++,Tools.getUniqueIdentify());
	                ps.setString(n++,payRiskDayTranSum.timeType);
	                ps.setString(n++,payRiskDayTranSum.custType);
	                ps.setString(n++,payRiskDayTranSum.custId);
	                ps.setString(n++,payRiskDayTranSum.statDate);
	                ps.setLong(n++,payRiskDayTranSum.receiveAmt);
	                ps.setLong(n++,payRiskDayTranSum.receiveCount);
	                ps.setLong(n++,payRiskDayTranSum.receiveSuccessAmt);
	                ps.setLong(n++,payRiskDayTranSum.receiveSuccessCount);
	                ps.setLong(n++,payRiskDayTranSum.payAmt);
	                ps.setLong(n++,payRiskDayTranSum.payCount);
	                ps.setLong(n++,payRiskDayTranSum.paySuccessAmt);
	                ps.setLong(n++,payRiskDayTranSum.paySuccessCount);
	                ps.executeUpdate();
            	} catch(Exception e){
            		ps.close();
            		ps = con.prepareStatement(sqlUpdate);
                    n=1;
                    ps.setLong(n++,payRiskDayTranSum.receiveAmt);
                    ps.setLong(n++,payRiskDayTranSum.receiveCount);
                    ps.setLong(n++,payRiskDayTranSum.receiveSuccessAmt);
                    ps.setLong(n++,payRiskDayTranSum.receiveSuccessCount);
                    ps.setLong(n++,payRiskDayTranSum.payAmt);
                    ps.setLong(n++,payRiskDayTranSum.payCount);
                    ps.setLong(n++,payRiskDayTranSum.paySuccessAmt);
                    ps.setLong(n++,payRiskDayTranSum.paySuccessCount);
                    ps.setString(n++,payRiskDayTranSum.timeType);
                    ps.setString(n++,payRiskDayTranSum.custType);
                    ps.setString(n++,payRiskDayTranSum.custId);
                    ps.setString(n++,payRiskDayTranSum.statDate);
                    ps.executeUpdate();
            	}
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(null, ps, con);
        }
    }
}