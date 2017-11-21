package com.pay.merchantinterface.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.jweb.dao.BaseDAO;
/**
 * Table PAY_QUICK_PAY_SMS DAO. 
 * @author Administrator
 *
 */
public class PayQuickPaySmsDAO  extends BaseDAO {
    private static final Log log = LogFactory.getLog(PayQuickPaySmsDAO.class);
    public static synchronized PayQuickPaySms getPayQuickPaySmsValue(ResultSet rs)throws SQLException {
        PayQuickPaySms payQuickPaySms = new PayQuickPaySms();
        payQuickPaySms.orderNo = rs.getString("ORDER_NO");
        payQuickPaySms.code = rs.getString("CODE");
        payQuickPaySms.times = rs.getLong("TIMES");
        payQuickPaySms.time = rs.getTimestamp("TIME");
        return payQuickPaySms;
    }
    public String addPayQuickPaySms(PayQuickPaySms payQuickPaySms) throws Exception {
        String sql = "insert into PAY_QUICK_PAY_SMS("+
            "ORDER_NO," + 
            "CODE," + 
            "TIMES)values(?,?,?)";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            int n=1;
            ps.setString(n++,payQuickPaySms.orderNo);
            ps.setString(n++,payQuickPaySms.code);
            ps.setLong(n++,payQuickPaySms.times);
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
     * detail PayQuickPaySms
     * @param orderNo
     * @return PayQuickPaySms
     * @throws Exception
     */
    public PayQuickPaySms detailPayQuickPaySms(String orderNo) throws Exception {
        String sql = "select * from PAY_QUICK_PAY_SMS where ORDER_NO=?";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            ps.setString(1,orderNo);
            rs = ps.executeQuery();
            if(rs.next())return getPayQuickPaySmsValue(rs); 
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(rs, ps, con);
        }
        return null;
    }
    public void removePayQuickPaySms(String orderNo) throws Exception {
        String sql = "delete from PAY_QUICK_PAY_SMS where ORDER_NO=?";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            ps.setString(1,orderNo);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(null, ps, con);
        }
    }
    public void updateQsTimes(PayQuickPaySms qs) throws Exception {
        String sql = "update PAY_QUICK_PAY_SMS set times=? where ORDER_NO=?";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            ps.setLong(1,qs.times);
            ps.setString(2,qs.orderNo);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(null, ps, con);
        }
    }
    public void clearQsBatch(String time) throws Exception{
    	String sql = "delete from PAY_QUICK_PAY_SMS where time<to_date(?,'yyyy-mm-dd hh24:mi:ss')";
        log.info(sql+"("+time+")");
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            ps.setString(1,time);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(null, ps, con);
        }
    }
}