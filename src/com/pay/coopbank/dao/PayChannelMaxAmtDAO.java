package com.pay.coopbank.dao;

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
 * Table PAY_CHANNEL_MAX_AMT DAO. 
 * @author Administrator
 *
 */
public class PayChannelMaxAmtDAO  extends BaseDAO {
    private static final Log log = LogFactory.getLog(PayChannelMaxAmtDAO.class);
    public static synchronized PayChannelMaxAmt getPayChannelMaxAmtValue(ResultSet rs)throws SQLException {
        PayChannelMaxAmt payChannelMaxAmt = new PayChannelMaxAmt();
        payChannelMaxAmt.id = rs.getString("ID");
        payChannelMaxAmt.bankCode = rs.getString("BANK_CODE");
        payChannelMaxAmt.tranType = rs.getString("TRAN_TYPE");
        payChannelMaxAmt.maxAmt = rs.getLong("MAX_AMT");
        payChannelMaxAmt.createTime = rs.getTimestamp("CREATE_TIME");
        return payChannelMaxAmt;
    }
    public String addPayChannelMaxAmt(PayChannelMaxAmt payChannelMaxAmt) throws Exception {
        String sql = "insert into PAY_CHANNEL_MAX_AMT("+
            "ID," + 
            "BANK_CODE," + 
            "TRAN_TYPE," + 
            "MAX_AMT," + 
            "CREATE_TIME)values(?,?,?,?,to_date(?,'yyyy-mm-dd hh24:mi:ss'))";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            int n=1;
            ps.setString(n++,payChannelMaxAmt.id);
            ps.setString(n++,payChannelMaxAmt.bankCode);
            ps.setString(n++,payChannelMaxAmt.tranType);
            ps.setLong(n++,payChannelMaxAmt.maxAmt);
            ps.setString(n++,new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payChannelMaxAmt.createTime));
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
        String sql = "select * from PAY_CHANNEL_MAX_AMT";
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
                list.add(getPayChannelMaxAmtValue(rs));
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
     * @param payChannelMaxAmt
     * @return
     */
    private String setPayChannelMaxAmtSql(PayChannelMaxAmt payChannelMaxAmt) {
        StringBuffer sql = new StringBuffer();
        
        if(payChannelMaxAmt.id != null && payChannelMaxAmt.id.length() !=0) {
            sql.append(" ID = ? and ");
        }
        String tmp = sql.toString();
        if(tmp.length()>0)tmp=tmp.substring(0,tmp.lastIndexOf(" and "));
        return tmp;
    }
    /**
     * Set query parameter.
     * @param ps
     * @param payChannelMaxAmt
     * @param n
     * @return
     * @throws SQLException
     */
    private int setPayChannelMaxAmtParameter(PreparedStatement ps,PayChannelMaxAmt payChannelMaxAmt,int n)throws SQLException {
        if(payChannelMaxAmt.id != null && payChannelMaxAmt.id.length() !=0) {
            ps.setString(n++,payChannelMaxAmt.id);
        }
        return n;
    }
    /**
     * Get records count.
     * @param payChannelMaxAmt
     * @return
     */
    public int getPayChannelMaxAmtCount(PayChannelMaxAmt payChannelMaxAmt) {
        String sqlCon = setPayChannelMaxAmtSql(payChannelMaxAmt);
        String sql = "select count(rownum) recordCount from PAY_CHANNEL_MAX_AMT " +(sqlCon.length()==0?"":" where "+ sqlCon);
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            int n = 1;
            setPayChannelMaxAmtParameter(ps,payChannelMaxAmt,n);
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
     * @param payChannelMaxAmt
     * @param page
     * @param rows
     * @param sort
     * @param order
     * @return
     * @throws Exception
     */
    public List getPayChannelMaxAmtList(PayChannelMaxAmt payChannelMaxAmt,int page,int rows,String sort,String order) throws Exception{
        sort = Tools.paraNameToDBColumnName(sort);
        String sqlCon = setPayChannelMaxAmtSql(payChannelMaxAmt);
        String sortOrder = sort == null || sort.length()==0?" order by ID desc":(" order by " + sort +("desc".equals(order)?" desc ":" asc "));
        String sql = "select * from (" +
                "  select rownum rowno,tmp1.* from (" +
                "   select tmp.*  from PAY_CHANNEL_MAX_AMT tmp " +(sqlCon.length()==0?"":" where "+ sqlCon) +sortOrder+
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
            setPayChannelMaxAmtParameter(ps,payChannelMaxAmt,n);
            rs = ps.executeQuery();
            while(rs.next()){
                list.add(getPayChannelMaxAmtValue(rs));
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
     * remove PayChannelMaxAmt
     * @param id
     * @throws Exception     
     */
    public void removePayChannelMaxAmt(String bankCode) throws Exception {
        String sql = "delete from PAY_CHANNEL_MAX_AMT where BANK_CODE=?";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            ps.setString(1,bankCode);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(null, ps, con);
        }
    }
    /**
     * detail PayChannelMaxAmt
     * @param id
     * @return PayChannelMaxAmt
     * @throws Exception
     */
    public PayChannelMaxAmt detailPayChannelMaxAmt(String bankCode ,String type) throws Exception {
        String sql = "select * from PAY_CHANNEL_MAX_AMT where BANK_CODE = ? and TRAN_TYPE = ?";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            ps.setString(1,bankCode);
            ps.setString(2,type);
            rs = ps.executeQuery();
            if(rs.next())return getPayChannelMaxAmtValue(rs); 
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(rs, ps, con);
        }
        return null;
    }
    /**
     * update PayChannelMaxAmt
     * @param payChannelMaxAmt
     * @throws Exception
     */
    public void updatePayChannelMaxAmt(PayChannelMaxAmt payChannelMaxAmt) throws Exception {
        String sqlTmp = "";
        if(payChannelMaxAmt.maxAmt != null)sqlTmp = sqlTmp + " MAX_AMT=?,";
        if(payChannelMaxAmt.createTime != null)sqlTmp = sqlTmp + " CREATE_TIME=to_date(?,'yyyy-mm-dd hh24:mi:ss'),";
        if(sqlTmp.length()==0)return;
        String sql = "update PAY_CHANNEL_MAX_AMT "+        
              "set " + 
              sqlTmp.substring(0,sqlTmp.length()-1) + 
            " where BANK_CODE=? and TRAN_TYPE=? "; 
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            int n=1;
            if(payChannelMaxAmt.maxAmt != null)ps.setLong(n++,payChannelMaxAmt.maxAmt);
            if(payChannelMaxAmt.createTime != null)ps.setString(n++,new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payChannelMaxAmt.createTime));
            ps.setString(n++,payChannelMaxAmt.bankCode);
            ps.setString(n++,payChannelMaxAmt.tranType);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }finally {
            close(null, ps, con);
        }
    }

}