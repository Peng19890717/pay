package com.pay.risk.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import util.Tools;
import java.sql.ResultSet;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;
import com.jweb.dao.BaseDAO;
/**
 * Table PAY_RISK_USER_LIMIT_SUB DAO. 
 * @author Administrator
 *
 */
public class PayRiskUserLimitSubDAO  extends BaseDAO {
    private static final Log log = LogFactory.getLog(PayRiskUserLimitSubDAO.class);
    public static synchronized PayRiskUserLimitSub getPayRiskUserLimitSubValue(ResultSet rs)throws SQLException {
        PayRiskUserLimitSub payRiskUserLimitSub = new PayRiskUserLimitSub();
        payRiskUserLimitSub.id = rs.getString("ID");
        payRiskUserLimitSub.limitBusClient = rs.getString("LIMIT_BUS_CLIENT");
        payRiskUserLimitSub.minAmt = rs.getLong("MIN_AMT");
        payRiskUserLimitSub.maxAmt = rs.getLong("MAX_AMT");
        payRiskUserLimitSub.dayTimes = rs.getLong("DAY_TIMES");
        payRiskUserLimitSub.daySucTimes = rs.getLong("DAY_SUC_TIMES");
        payRiskUserLimitSub.dayAmt = rs.getLong("DAY_AMT");
        payRiskUserLimitSub.daySucAmt = rs.getLong("DAY_SUC_AMT");
        payRiskUserLimitSub.monthTimes = rs.getLong("MONTH_TIMES");
        payRiskUserLimitSub.monthSucTimes = rs.getLong("MONTH_SUC_TIMES");
        payRiskUserLimitSub.monthAmt = rs.getLong("MONTH_AMT");
        payRiskUserLimitSub.monthSucAmt = rs.getLong("MONTH_SUC_AMT");
        payRiskUserLimitSub.riskUserLimitId = rs.getString("RISK_USER_LIMIT_ID");
        return payRiskUserLimitSub;
    }
    public String addPayRiskUserLimitSub(PayRiskUserLimitSub payRiskUserLimitSub) throws Exception {
        String sql = "insert into PAY_RISK_USER_LIMIT_SUB("+
            "ID," + 
            "LIMIT_BUS_CLIENT," + 
            "MIN_AMT," + 
            "MAX_AMT," + 
            "DAY_TIMES," + 
            "DAY_SUC_TIMES," + 
            "DAY_AMT," + 
            "DAY_SUC_AMT," + 
            "MONTH_TIMES," + 
            "MONTH_SUC_TIMES," + 
            "MONTH_AMT," + 
            "MONTH_SUC_AMT," + 
            "RISK_USER_LIMIT_ID)values(?,?,?,?,?,?,?,?,?,?,?,?,?)";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            int n=1;
            ps.setString(n++,payRiskUserLimitSub.id);
            ps.setString(n++,payRiskUserLimitSub.limitBusClient);
            ps.setLong(n++,payRiskUserLimitSub.minAmt);
            ps.setLong(n++,payRiskUserLimitSub.maxAmt);
            ps.setLong(n++,payRiskUserLimitSub.dayTimes);
            ps.setLong(n++,payRiskUserLimitSub.daySucTimes);
            ps.setLong(n++,payRiskUserLimitSub.dayAmt);
            ps.setLong(n++,payRiskUserLimitSub.daySucAmt);
            ps.setLong(n++,payRiskUserLimitSub.monthTimes);
            ps.setLong(n++,payRiskUserLimitSub.monthSucTimes);
            ps.setLong(n++,payRiskUserLimitSub.monthAmt);
            ps.setLong(n++,payRiskUserLimitSub.monthSucAmt);
            ps.setString(n++,payRiskUserLimitSub.riskUserLimitId);
            ps.executeUpdate();
            return "";
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }finally {
            close(null, ps, con);
        }
    }
    public List getList() throws Exception{
        String sql = "select * from PAY_RISK_USER_LIMIT_SUB";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List list = new ArrayList();
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while(rs.next()){
                list.add(getPayRiskUserLimitSubValue(rs));
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(rs, ps, con);
        }
    }
    /**
     * Set query condition sql.
     * @param payRiskUserLimitSub
     * @return
     */
    private String setPayRiskUserLimitSubSql(PayRiskUserLimitSub payRiskUserLimitSub) {
        StringBuffer sql = new StringBuffer();
        
        String tmp = sql.toString();
        if(tmp.length()>0)tmp=tmp.substring(0,tmp.lastIndexOf(" and "));
        return tmp;
    }
    /**
     * Set query parameter.
     * @param ps
     * @param payRiskUserLimitSub
     * @param n
     * @return
     * @throws SQLException
     */
    private int setPayRiskUserLimitSubParameter(PreparedStatement ps,PayRiskUserLimitSub payRiskUserLimitSub,int n)throws SQLException {
        return n;
    }
    /**
     * Get records count.
     * @param payRiskUserLimitSub
     * @return
     */
    public int getPayRiskUserLimitSubCount(PayRiskUserLimitSub payRiskUserLimitSub) {
        String sqlCon = setPayRiskUserLimitSubSql(payRiskUserLimitSub);
        String sql = "select count(rownum) recordCount from PAY_RISK_USER_LIMIT_SUB " +(sqlCon.length()==0?"":" where "+ sqlCon);
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            int n = 1;
            setPayRiskUserLimitSubParameter(ps,payRiskUserLimitSub,n);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("recordCount");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(rs, ps, con);
        }
        return 0;
    }
    /**
     * Get records list.
     * @param payRiskUserLimitSub
     * @param page
     * @param rows
     * @param sort
     * @param order
     * @return
     * @throws Exception
     */
    public List getPayRiskUserLimitSubList(PayRiskUserLimitSub payRiskUserLimitSub,int page,int rows,String sort,String order) throws Exception{
        sort = Tools.paraNameToDBColumnName(sort);
        String sqlCon = setPayRiskUserLimitSubSql(payRiskUserLimitSub);
        String sortOrder = sort == null || sort.length()==0?" order by ID desc":(" order by " + sort +("desc".equals(order)?" desc ":" asc "));
        String sql = "select * from (" +
                "  select rownum rowno,tmp1.* from (" +
                "   select tmp.*  from PAY_RISK_USER_LIMIT_SUB tmp " +(sqlCon.length()==0?"":" where "+ sqlCon) +sortOrder+
                "  ) tmp1 "+
                ")  where rowno > "+((page-1)*rows)+ " and rowno<= " + (page*rows) + sortOrder;
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List list = new ArrayList();
        try {
            con = connection();
            ps = con.prepareStatement(sql.toString());
            int n = 1;
            setPayRiskUserLimitSubParameter(ps,payRiskUserLimitSub,n);
            rs = ps.executeQuery();
            while(rs.next()){
                list.add(getPayRiskUserLimitSubValue(rs));
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(rs, ps, con);
        }
    }
    /**
     * remove PayRiskUserLimitSub
     * @param id
     * @throws Exception     
     */
    public void removePayRiskUserLimitSub(String id) throws Exception {
        String sql = "delete from PAY_RISK_USER_LIMIT_SUB where ID=?";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            ps.setString(1,id);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(null, ps, con);
        }
    }
    /**
     * detail PayRiskUserLimitSub
     * @param id
     * @return PayRiskUserLimitSub
     * @throws Exception
     */
    public PayRiskUserLimitSub detailPayRiskUserLimitSub(String id) throws Exception {
        String sql = "select * from PAY_RISK_USER_LIMIT_SUB where ID=?";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            ps.setString(1,id);
            rs = ps.executeQuery();
            if(rs.next())return getPayRiskUserLimitSubValue(rs); 
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(rs, ps, con);
        }
        return null;
    }
    /**
     * update PayRiskUserLimitSub
     * @param payRiskUserLimitSub
     * @throws Exception
     */
    public void updatePayRiskUserLimitSub(PayRiskUserLimitSub payRiskUserLimitSub) throws Exception {
        String sqlTmp = "";
        if(payRiskUserLimitSub.id != null)sqlTmp = sqlTmp + " ID=?,";
        if(payRiskUserLimitSub.limitBusClient != null)sqlTmp = sqlTmp + " LIMIT_BUS_CLIENT=?,";
        if(payRiskUserLimitSub.minAmt != null)sqlTmp = sqlTmp + " MIN_AMT=?,";
        if(payRiskUserLimitSub.maxAmt != null)sqlTmp = sqlTmp + " MAX_AMT=?,";
        if(payRiskUserLimitSub.dayTimes != null)sqlTmp = sqlTmp + " DAY_TIMES=?,";
        if(payRiskUserLimitSub.daySucTimes != null)sqlTmp = sqlTmp + " DAY_SUC_TIMES=?,";
        if(payRiskUserLimitSub.dayAmt != null)sqlTmp = sqlTmp + " DAY_AMT=?,";
        if(payRiskUserLimitSub.daySucAmt != null)sqlTmp = sqlTmp + " DAY_SUC_AMT=?,";
        if(payRiskUserLimitSub.monthTimes != null)sqlTmp = sqlTmp + " MONTH_TIMES=?,";
        if(payRiskUserLimitSub.monthSucTimes != null)sqlTmp = sqlTmp + " MONTH_SUC_TIMES=?,";
        if(payRiskUserLimitSub.monthAmt != null)sqlTmp = sqlTmp + " MONTH_AMT=?,";
        if(payRiskUserLimitSub.monthSucAmt != null)sqlTmp = sqlTmp + " MONTH_SUC_AMT=?,";
        if(payRiskUserLimitSub.riskUserLimitId != null)sqlTmp = sqlTmp + " RISK_USER_LIMIT_ID=?,";
        if(sqlTmp.length()==0)return;
        String sql = "update PAY_RISK_USER_LIMIT_SUB "+        
              "set " + 
              sqlTmp.substring(0,sqlTmp.length()-1) + 
            " where ID=?"; 
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            int n=1;
            if(payRiskUserLimitSub.id != null)ps.setString(n++,payRiskUserLimitSub.id);
            if(payRiskUserLimitSub.limitBusClient != null)ps.setString(n++,payRiskUserLimitSub.limitBusClient);
            if(payRiskUserLimitSub.minAmt != null)ps.setLong(n++,payRiskUserLimitSub.minAmt);
            if(payRiskUserLimitSub.maxAmt != null)ps.setLong(n++,payRiskUserLimitSub.maxAmt);
            if(payRiskUserLimitSub.dayTimes != null)ps.setLong(n++,payRiskUserLimitSub.dayTimes);
            if(payRiskUserLimitSub.daySucTimes != null)ps.setLong(n++,payRiskUserLimitSub.daySucTimes);
            if(payRiskUserLimitSub.dayAmt != null)ps.setLong(n++,payRiskUserLimitSub.dayAmt);
            if(payRiskUserLimitSub.daySucAmt != null)ps.setLong(n++,payRiskUserLimitSub.daySucAmt);
            if(payRiskUserLimitSub.monthTimes != null)ps.setLong(n++,payRiskUserLimitSub.monthTimes);
            if(payRiskUserLimitSub.monthSucTimes != null)ps.setLong(n++,payRiskUserLimitSub.monthSucTimes);
            if(payRiskUserLimitSub.monthAmt != null)ps.setLong(n++,payRiskUserLimitSub.monthAmt);
            if(payRiskUserLimitSub.monthSucAmt != null)ps.setLong(n++,payRiskUserLimitSub.monthSucAmt);
            if(payRiskUserLimitSub.riskUserLimitId != null)ps.setString(n++,payRiskUserLimitSub.riskUserLimitId);
            ps.setString(n++,payRiskUserLimitSub.id);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }finally {
            close(null, ps, con);
        }
    }

}