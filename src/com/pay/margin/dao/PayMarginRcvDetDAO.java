package com.pay.margin.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import util.Tools;

import com.jweb.dao.BaseDAO;
/**
 * Table PAY_MARGIN_RCV_DET DAO. 
 * @author Administrator
 *
 */
public class PayMarginRcvDetDAO  extends BaseDAO {
    private static final Log log = LogFactory.getLog(PayMarginRcvDetDAO.class);
    public static synchronized PayMarginRcvDet getPayMarginRcvDetValue(ResultSet rs)throws SQLException {
        PayMarginRcvDet payMarginRcvDet = new PayMarginRcvDet();
        payMarginRcvDet.seqNo = rs.getString("SEQ_NO");
        payMarginRcvDet.custId = rs.getString("CUST_ID");
        payMarginRcvDet.paidInAmt = rs.getLong("PAID_IN_AMT");
        payMarginRcvDet.marginAc = rs.getString("MARGIN_AC");
        payMarginRcvDet.custProvisionAcNo = rs.getString("CUST_PROVISION_AC_NO");
        payMarginRcvDet.marginRcvTime = rs.getTimestamp("MARGIN_RCV_TIME");
        payMarginRcvDet.mark = rs.getString("MARK");
        return payMarginRcvDet;
    }
    public String addPayMarginRcvDet(PayMarginRcvDet payMarginRcvDet) throws Exception {
        String sql = "insert into PAY_MARGIN_RCV_DET("+
            "SEQ_NO," + 
            "CUST_ID," + 
//            "PACT_NO," + 
            "PAID_IN_AMT," + 
            "MARGIN_AC," + 
            "CUST_PROVISION_AC_NO," + 
            "MARK," +
            "MARGIN_CUR_AMT)values(?,?,?,?,?,?," +
            "(select sum(PAID_IN_AMT) from PAY_MARGIN where CUST_ID=?))";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            int n=1;
            ps.setString(n++,payMarginRcvDet.seqNo);
            ps.setString(n++,payMarginRcvDet.custId);
//            ps.setString(n++,payMarginRcvDet.pactNo);
            ps.setLong(n++,payMarginRcvDet.paidInAmt);
            ps.setString(n++,payMarginRcvDet.marginAc);
            ps.setString(n++,payMarginRcvDet.custProvisionAcNo);
            ps.setString(n++,payMarginRcvDet.mark);
            ps.setString(n++,payMarginRcvDet.custId);
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
     * Set query condition sql.
     * @param payMarginRcvDet
     * @return
     */
    private String setPayMarginRcvDetSql(PayMarginRcvDet payMarginRcvDet) {
        StringBuffer sql = new StringBuffer();
        
        if(payMarginRcvDet.seqNo != null && payMarginRcvDet.seqNo.length() !=0) {
            sql.append(" SEQ_NO = ? and ");
        }
        String tmp = sql.toString();
        if(tmp.length()>0)tmp=tmp.substring(0,tmp.lastIndexOf(" and "));
        return tmp;
    }
    /**
     * Set query parameter.
     * @param ps
     * @param payMarginRcvDet
     * @param n
     * @return
     * @throws SQLException
     */
    private int setPayMarginRcvDetParameter(PreparedStatement ps,PayMarginRcvDet payMarginRcvDet,int n)throws SQLException {
        if(payMarginRcvDet.seqNo != null && payMarginRcvDet.seqNo.length() !=0) {
            ps.setString(n++,payMarginRcvDet.seqNo);
        }
        return n;
    }
    /**
     * remove PayMarginRcvDet
     * @param seqNo
     * @throws Exception     
     */
    public void removePayMarginRcvDet(String seqNo) throws Exception {
        String sql = "delete from PAY_MARGIN_RCV_DET where SEQ_NO=?";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            ps.setString(1,seqNo);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(null, ps, con);
        }
    }
    /**
     * detail PayMarginRcvDet
     * @param seqNo
     * @return PayMarginRcvDet
     * @throws Exception
     */
    public PayMarginRcvDet detailPayMarginRcvDet(String seqNo) throws Exception {
        String sql = "select * from PAY_MARGIN_RCV_DET where SEQ_NO=?";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            ps.setString(1,seqNo);
            rs = ps.executeQuery();
            if(rs.next())return getPayMarginRcvDetValue(rs); 
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(rs, ps, con);
        }
        return null;
    }
    /**
     * update PayMarginRcvDet
     * @param payMarginRcvDet
     * @throws Exception
     */
    public void updatePayMarginRcvDet(PayMarginRcvDet payMarginRcvDet) throws Exception {
        String sqlTmp = "";
        if(payMarginRcvDet.seqNo != null)sqlTmp = sqlTmp + " SEQ_NO=?,";
        if(payMarginRcvDet.custId != null)sqlTmp = sqlTmp + " CUST_ID=?,";
        if(payMarginRcvDet.paidInAmt != null)sqlTmp = sqlTmp + " PAID_IN_AMT=?,";
        if(payMarginRcvDet.marginAc != null)sqlTmp = sqlTmp + " MARGIN_AC=?,";
        if(payMarginRcvDet.custProvisionAcNo != null)sqlTmp = sqlTmp + " CUST_PROVISION_AC_NO=?,";
        if(payMarginRcvDet.marginRcvTime != null)sqlTmp = sqlTmp + " MARGIN_RCV_TIME=to_date(?,'yyyy-mm-dd hh24:mi:ss'),";
        if(payMarginRcvDet.mark != null)sqlTmp = sqlTmp + " MARK=?,";
        if(sqlTmp.length()==0)return;
        String sql = "update PAY_MARGIN_RCV_DET "+        
              "set " + 
              sqlTmp.substring(0,sqlTmp.length()-1) + 
            " where SEQ_NO=?"; 
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            int n=1;
            if(payMarginRcvDet.seqNo != null)ps.setString(n++,payMarginRcvDet.seqNo);
            if(payMarginRcvDet.custId != null)ps.setString(n++,payMarginRcvDet.custId);
            if(payMarginRcvDet.paidInAmt != null)ps.setLong(n++,payMarginRcvDet.paidInAmt);
            if(payMarginRcvDet.marginAc != null)ps.setString(n++,payMarginRcvDet.marginAc);
            if(payMarginRcvDet.custProvisionAcNo != null)ps.setString(n++,payMarginRcvDet.custProvisionAcNo);
            if(payMarginRcvDet.marginRcvTime != null)ps.setString(n++,new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payMarginRcvDet.marginRcvTime));
            if(payMarginRcvDet.mark != null)ps.setString(n++,payMarginRcvDet.mark);
            ps.setString(n++,payMarginRcvDet.seqNo);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }finally {
            close(null, ps, con);
        }
    }
    /**
     * 根据商户编号查询该商户的保证金收取记录
     * @param payMarginRcvDet
     * @return
     */
	public List<PayMarginRcvDet> getPayMarginRcvDetListByCustId(PayMarginRcvDet payMarginRcvDet) throws Exception {
		String sql = "select * from PAY_MARGIN_RCV_DET where CUST_ID=? order by MARGIN_RCV_TIME desc";
		log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List list = new ArrayList();
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            ps.setString(1,payMarginRcvDet.custId);
//            ps.setString(2,payMarginRcvDet.pactNo);
            rs = ps.executeQuery();
            while(rs.next()){
                list.add(getPayMarginRcvDetValue(rs));
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