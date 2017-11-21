package com.pay.merchant.dao;

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
 * Table PAY_STL_PERIOD_OPT_LOG DAO. 
 * @author Administrator
 *
 */
public class PayStlPeriodOptLogDAO  extends BaseDAO {
    private static final Log log = LogFactory.getLog(PayStlPeriodOptLogDAO.class);
    public static synchronized PayStlPeriodOptLog getPayStlPeriodOptLogValue(ResultSet rs)throws SQLException {
        PayStlPeriodOptLog payStlPeriodOptLog = new PayStlPeriodOptLog();
        payStlPeriodOptLog.id = rs.getString("ID");
        payStlPeriodOptLog.merNo = rs.getString("MER_NO");
        payStlPeriodOptLog.accBalance = rs.getLong("ACC_BALANCE");
        payStlPeriodOptLog.frozenAmt = rs.getLong("FROZEN_AMT");
        payStlPeriodOptLog.custSetPeriodOld = rs.getString("CUST_SET_PERIOD_OLD");
        payStlPeriodOptLog.custStlTimeSetOld = rs.getString("CUST_STL_TIME_SET_OLD");
        payStlPeriodOptLog.stlType = rs.getString("STL_TYPE");
        payStlPeriodOptLog.createTime = rs.getTimestamp("CREATE_TIME");
        return payStlPeriodOptLog;
    }
    public String addPayStlPeriodOptLog(PayStlPeriodOptLog payStlPeriodOptLog) throws Exception {
        String sql = "insert into PAY_STL_PERIOD_OPT_LOG("+
            "ID," + 
            "MER_NO," + 
            "ACC_BALANCE," + 
            "FROZEN_AMT," + 
            "CUST_SET_PERIOD_OLD," + 
            "CUST_STL_TIME_SET_OLD," + 
            "CREATE_TIME,STL_TYPE)values(?,?,?,?,?,?,sysdate,?)";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            int n=1;
            ps.setString(n++,payStlPeriodOptLog.id);
            ps.setString(n++,payStlPeriodOptLog.merNo);
            ps.setLong(n++,payStlPeriodOptLog.accBalance);
            ps.setLong(n++,payStlPeriodOptLog.frozenAmt);
            ps.setString(n++,payStlPeriodOptLog.custSetPeriodOld);
            ps.setString(n++,payStlPeriodOptLog.custStlTimeSetOld);
            ps.setString(n++,payStlPeriodOptLog.stlType);
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
     * @param payStlPeriodOptLog
     * @return
     */
    private String setPayStlPeriodOptLogSql(PayStlPeriodOptLog payStlPeriodOptLog) {
        StringBuffer sql = new StringBuffer();
        
        if(payStlPeriodOptLog.merNo != null && payStlPeriodOptLog.merNo.length() !=0) {
            sql.append(" MER_NO = ? and ");
        }
        String tmp = sql.toString();
        if(tmp.length()>0)tmp=tmp.substring(0,tmp.lastIndexOf(" and "));
        return tmp;
    }
    /**
     * Set query parameter.
     * @param ps
     * @param payStlPeriodOptLog
     * @param n
     * @return
     * @throws SQLException
     */
    private int setPayStlPeriodOptLogParameter(PreparedStatement ps,PayStlPeriodOptLog payStlPeriodOptLog,int n)throws SQLException {
        if(payStlPeriodOptLog.merNo != null && payStlPeriodOptLog.merNo.length() !=0) {
            ps.setString(n++,payStlPeriodOptLog.merNo);
        }
        return n;
    }
    /**
     * Get records count.
     * @param payStlPeriodOptLog
     * @return
     */
    public int getPayStlPeriodOptLogCount(PayStlPeriodOptLog payStlPeriodOptLog) {
        String sqlCon = setPayStlPeriodOptLogSql(payStlPeriodOptLog);
        String sql = "select count(rownum) recordCount from PAY_STL_PERIOD_OPT_LOG " +(sqlCon.length()==0?"":" where "+ sqlCon);
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            int n = 1;
            setPayStlPeriodOptLogParameter(ps,payStlPeriodOptLog,n);
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
     * @param payStlPeriodOptLog
     * @param page
     * @param rows
     * @param sort
     * @param order
     * @return
     * @throws Exception
     */
    public List getPayStlPeriodOptLogList(PayStlPeriodOptLog payStlPeriodOptLog,int page,int rows,String sort,String order) throws Exception{
        sort = Tools.paraNameToDBColumnName(sort);
        String sqlCon = setPayStlPeriodOptLogSql(payStlPeriodOptLog);
        String sortOrder = sort == null || sort.length()==0?" order by ID desc":(" order by " + sort +("desc".equals(order)?" desc ":" asc "));
        String sql = "select * from (" +
                "  select rownum rowno,tmp1.* from (" +
                "   select tmp.*  from PAY_STL_PERIOD_OPT_LOG tmp " +(sqlCon.length()==0?"":" where "+ sqlCon) +sortOrder+
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
            setPayStlPeriodOptLogParameter(ps,payStlPeriodOptLog,n);
            rs = ps.executeQuery();
            while(rs.next()){
                list.add(getPayStlPeriodOptLogValue(rs));
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(rs, ps, con);
        }
    }
    public long[] getMerchantCashInfo(PayMerchant mer) {
        String accCashSql = "select CASH_AC_BAL from PAY_ACC_PROFILE where PAY_AC_NO='"+mer.custId+"'";
        String frozenSql = "select sum(CUR_AMT) frozenBal from PAY_ACC_PROFILE_FROZEN where acc_no='"+mer.custId+"' group by acc_no";
        log.info(accCashSql);
        log.info(frozenSql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        long [] tmp = new long[]{0l,0l};
        try {
        	
            con = connection();
            ps = con.prepareStatement(accCashSql);
            rs = ps.executeQuery();
            if (rs.next()) tmp[0] = rs.getLong("CASH_AC_BAL");
            rs.close();
            ps.close();
            ps = con.prepareStatement(frozenSql);
            rs = ps.executeQuery();
            if (rs.next()) tmp[1] = rs.getLong("frozenBal");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(rs, ps, con);
        }
        return tmp;
    }
}