package com.pay.qtbao.dao;

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
 * Table PAY_FUNDIN_FUNDOUT DAO. 
 * @author Administrator
 *
 */
public class PayFundinFundoutDAO  extends BaseDAO {
    private static final Log log = LogFactory.getLog(PayFundinFundoutDAO.class);
    public static synchronized PayFundinFundout getPayFundinFundoutValue(ResultSet rs)throws SQLException {
        PayFundinFundout payFundinFundout = new PayFundinFundout();
        payFundinFundout.merchantid = rs.getString("MERCHANTID");
        payFundinFundout.application = rs.getString("APPLICATION");
        payFundinFundout.userid = rs.getString("USERID");
        payFundinFundout.orderno = rs.getString("ORDERNO");
        payFundinFundout.amt = rs.getString("AMT");
        payFundinFundout.remark = rs.getString("REMARK");
        payFundinFundout.timestamp = rs.getTimestamp("TIMESTAMP");
        payFundinFundout.sign = rs.getString("SIGN");
        payFundinFundout.custType = rs.getString("CUST_TYPE");
        return payFundinFundout;
    }
    public String addPayFundinFundout(PayFundinFundout payFundinFundout) throws Exception {
        String sql = "insert into PAY_FUNDIN_FUNDOUT("+
            "MERCHANTID," + 
            "APPLICATION," + 
            "USERID," + 
            "ORDERNO," + 
            "AMT," + 
            "REMARK," + 
            "TIMESTAMP," + 
            "SIGN)values(?,?,?,?,?,?,to_date(?,'yyyy-mm-dd hh24:mi:ss'),?)";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            int n=1;
            ps.setString(n++,payFundinFundout.merchantid);
            ps.setString(n++,payFundinFundout.application);
            ps.setString(n++,payFundinFundout.userid);
            ps.setString(n++,payFundinFundout.orderno);
            ps.setString(n++,payFundinFundout.amt);
            ps.setString(n++,payFundinFundout.remark);
            ps.setString(n++,new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payFundinFundout.timestamp));
            ps.setString(n++,payFundinFundout.sign);
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
        String sql = "select * from PAY_FUNDIN_FUNDOUT";
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
                list.add(getPayFundinFundoutValue(rs));
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
     * @param payFundinFundout
     * @return
     */
    private String setPayFundinFundoutSql(PayFundinFundout payFundinFundout) {
        StringBuffer sql = new StringBuffer();
        
        if(payFundinFundout.merchantid != null && payFundinFundout.merchantid.length() !=0) {
            sql.append(" MERCHANTID = ? and ");
        }
        if(payFundinFundout.application != null && payFundinFundout.application.length() !=0) {
            sql.append(" APPLICATION = ? and ");
        }
        if(payFundinFundout.custType != null && payFundinFundout.custType.length() !=0) {
        	sql.append(" CUST_TYPE = ? and ");
        }
        if(payFundinFundout.userid != null && payFundinFundout.userid.length() !=0) {
            sql.append(" USERID = ? and ");
        }
        if(payFundinFundout.orderno != null && payFundinFundout.orderno.length() !=0) {
            sql.append(" ORDERNO = ? and ");
        }
        if(payFundinFundout.timestampStart != null && payFundinFundout.timestampStart.length() !=0) {
            sql.append(" TIMESTAMP >=to_date(?,'yyyy-mm-dd hh24:mi:ss') and ");
        }
        if(payFundinFundout.timestampEnd != null && payFundinFundout.timestampEnd.length() !=0) {
            sql.append(" TIMESTAMP <= to_date(?,'yyyy-mm-dd hh24:mi:ss') and ");
        }
        String tmp = sql.toString();
        if(tmp.length()>0)tmp=tmp.substring(0,tmp.lastIndexOf(" and "));
        return tmp;
    }
    /**
     * Set query parameter.
     * @param ps
     * @param payFundinFundout
     * @param n
     * @return
     * @throws SQLException
     */
    private int setPayFundinFundoutParameter(PreparedStatement ps,PayFundinFundout payFundinFundout,int n)throws SQLException {
        if(payFundinFundout.merchantid != null && payFundinFundout.merchantid.length() !=0) {
            ps.setString(n++,payFundinFundout.merchantid);
        }
        if(payFundinFundout.application != null && payFundinFundout.application.length() !=0) {
            ps.setString(n++,payFundinFundout.application);
        }
        if(payFundinFundout.custType != null && payFundinFundout.custType.length() !=0) {
        	ps.setString(n++,payFundinFundout.custType);
        }
        if(payFundinFundout.userid != null && payFundinFundout.userid.length() !=0) {
            ps.setString(n++,payFundinFundout.userid);
        }
        if(payFundinFundout.orderno != null && payFundinFundout.orderno.length() !=0) {
            ps.setString(n++,payFundinFundout.orderno);
        }
        if(payFundinFundout.timestampStart != null && payFundinFundout.timestampStart.length() !=0) {
            ps.setString(n++, payFundinFundout.timestampStart+" 00:00:00");
        }
        if(payFundinFundout.timestampEnd != null && payFundinFundout.timestampEnd.length() !=0) {
        	ps.setString(n++, payFundinFundout.timestampEnd+" 23:59:59");
        }
        return n;
    }
    /**
     * Get records count.
     * @param payFundinFundout
     * @return
     */
    public int getPayFundinFundoutCount(PayFundinFundout payFundinFundout) {
        String sqlCon = setPayFundinFundoutSql(payFundinFundout);
        String sql = "select count(rownum) recordCount from PAY_FUNDIN_FUNDOUT " +(sqlCon.length()==0?"":" where "+ sqlCon);
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            int n = 1;
            setPayFundinFundoutParameter(ps,payFundinFundout,n);
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
     * @param payFundinFundout
     * @param page
     * @param rows
     * @param sort
     * @param order
     * @return
     * @throws Exception
     */
    public List getPayFundinFundoutList(PayFundinFundout payFundinFundout,int page,int rows,String sort,String order) throws Exception{
        sort = Tools.paraNameToDBColumnName(sort);
        String sqlCon = setPayFundinFundoutSql(payFundinFundout);
        String sortOrder = sort == null || sort.length()==0?" order by TIMESTAMP desc":(" order by " + sort +("desc".equals(order)?" desc ":" asc "));
        String sql = "select * from (" +
                "  select rownum rowno,tmp1.* from (" +
                "   select tmp.*  from PAY_FUNDIN_FUNDOUT tmp " +(sqlCon.length()==0?"":" where "+ sqlCon) +sortOrder+
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
            setPayFundinFundoutParameter(ps,payFundinFundout,n);
            rs = ps.executeQuery();
            while(rs.next()){
                list.add(getPayFundinFundoutValue(rs));
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
     * remove PayFundinFundout
     * @param merchantid
     * @throws Exception     
     */
    public void removePayFundinFundout(String merchantid) throws Exception {
        String sql = "delete from PAY_FUNDIN_FUNDOUT where MERCHANTID=?";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            ps.setString(1,merchantid);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(null, ps, con);
        }
    }
    /**
     * detail PayFundinFundout
     * @param merchantid
     * @return PayFundinFundout
     * @throws Exception
     */
    public PayFundinFundout detailPayFundinFundout(String merchantid) throws Exception {
        String sql = "select * from PAY_FUNDIN_FUNDOUT where MERCHANTID=?";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            ps.setString(1,merchantid);
            rs = ps.executeQuery();
            if(rs.next())return getPayFundinFundoutValue(rs); 
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(rs, ps, con);
        }
        return null;
    }
    /**
     * update PayFundinFundout
     * @param payFundinFundout
     * @throws Exception
     */
    public void updatePayFundinFundout(PayFundinFundout payFundinFundout) throws Exception {
        String sqlTmp = "";
        if(payFundinFundout.merchantid != null)sqlTmp = sqlTmp + " MERCHANTID=?,";
        if(payFundinFundout.application != null)sqlTmp = sqlTmp + " APPLICATION=?,";
        if(payFundinFundout.userid != null)sqlTmp = sqlTmp + " USERID=?,";
        if(payFundinFundout.orderno != null)sqlTmp = sqlTmp + " ORDERNO=?,";
        if(payFundinFundout.amt != null)sqlTmp = sqlTmp + " AMT=?,";
        if(payFundinFundout.remark != null)sqlTmp = sqlTmp + " REMARK=?,";
        if(payFundinFundout.timestamp != null)sqlTmp = sqlTmp + " TIMESTAMP=to_date(?,'yyyy-mm-dd hh24:mi:ss'),";
        if(payFundinFundout.sign != null)sqlTmp = sqlTmp + " SIGN=?,";
        if(sqlTmp.length()==0)return;
        String sql = "update PAY_FUNDIN_FUNDOUT "+        
              "set " + 
              sqlTmp.substring(0,sqlTmp.length()-1) + 
            " where MERCHANTID=?"; 
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            int n=1;
            if(payFundinFundout.merchantid != null)ps.setString(n++,payFundinFundout.merchantid);
            if(payFundinFundout.application != null)ps.setString(n++,payFundinFundout.application);
            if(payFundinFundout.userid != null)ps.setString(n++,payFundinFundout.userid);
            if(payFundinFundout.orderno != null)ps.setString(n++,payFundinFundout.orderno);
            if(payFundinFundout.amt != null)ps.setString(n++,payFundinFundout.amt);
            if(payFundinFundout.remark != null)ps.setString(n++,payFundinFundout.remark);
            if(payFundinFundout.timestamp != null)ps.setString(n++,new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payFundinFundout.timestamp));
            if(payFundinFundout.sign != null)ps.setString(n++,payFundinFundout.sign);
            ps.setString(n++,payFundinFundout.merchantid);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }finally {
            close(null, ps, con);
        }
    }

}