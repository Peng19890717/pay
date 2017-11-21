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
 * Table PAY_OPENED_MER_PAY DAO. 
 * @author Administrator
 *
 */
public class PayOpenedMerPayDAO  extends BaseDAO {
    private static final Log log = LogFactory.getLog(PayOpenedMerPayDAO.class);
    public static synchronized PayOpenedMerPay getPayOpenedMerPayValue(ResultSet rs)throws SQLException {
        PayOpenedMerPay payOpenedMerPay = new PayOpenedMerPay();
        payOpenedMerPay.merno = rs.getString("MERNO");
        payOpenedMerPay.type = rs.getString("TYPE");
        payOpenedMerPay.createtime = rs.getTimestamp("CREATETIME");
        return payOpenedMerPay;
    }
    public String addPayOpenedMerPay(PayOpenedMerPay payOpenedMerPay) throws Exception {
        String sql = "insert into PAY_OPENED_MER_PAY("+
            "MERNO," + 
            "TYPE," + 
            "CREATETIME)values(?,?,sysdate)";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            int n=1;
            ps.setString(n++,payOpenedMerPay.merno);
            ps.setString(n++,payOpenedMerPay.type);
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
        String sql = "select * from PAY_OPENED_MER_PAY";
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
                list.add(getPayOpenedMerPayValue(rs));
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
     * @param payOpenedMerPay
     * @return
     */
    private String setPayOpenedMerPaySql(PayOpenedMerPay payOpenedMerPay) {
        StringBuffer sql = new StringBuffer();
        
        if(payOpenedMerPay.merno != null && payOpenedMerPay.merno.length() !=0) {
            sql.append(" MERNO = ? and ");
        }
        String tmp = sql.toString();
        if(tmp.length()>0)tmp=tmp.substring(0,tmp.lastIndexOf(" and "));
        return tmp;
    }
    /**
     * Set query parameter.
     * @param ps
     * @param payOpenedMerPay
     * @param n
     * @return
     * @throws SQLException
     */
    private int setPayOpenedMerPayParameter(PreparedStatement ps,PayOpenedMerPay payOpenedMerPay,int n)throws SQLException {
        if(payOpenedMerPay.merno != null && payOpenedMerPay.merno.length() !=0) {
            ps.setString(n++,payOpenedMerPay.merno);
        }
        return n;
    }
    /**
     * Get records count.
     * @param payOpenedMerPay
     * @return
     */
    public int getPayOpenedMerPayCount(PayOpenedMerPay payOpenedMerPay) {
        String sqlCon = setPayOpenedMerPaySql(payOpenedMerPay);
        String sql = "select count(rownum) recordCount from PAY_OPENED_MER_PAY " +(sqlCon.length()==0?"":" where "+ sqlCon);
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            int n = 1;
            setPayOpenedMerPayParameter(ps,payOpenedMerPay,n);
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
     * @param payOpenedMerPay
     * @param page
     * @param rows
     * @param sort
     * @param order
     * @return
     * @throws Exception
     */
    public List getPayOpenedMerPayList(PayOpenedMerPay payOpenedMerPay,int page,int rows,String sort,String order) throws Exception{
        sort = Tools.paraNameToDBColumnName(sort);
        String sqlCon = setPayOpenedMerPaySql(payOpenedMerPay);
        String sortOrder = sort == null || sort.length()==0?" order by MERNO desc":(" order by " + sort +("desc".equals(order)?" desc ":" asc "));
        String sql = "select * from (" +
                "  select rownum rowno,tmp1.* from (" +
                "   select tmp.*  from PAY_OPENED_MER_PAY tmp " +(sqlCon.length()==0?"":" where "+ sqlCon) +sortOrder+
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
            setPayOpenedMerPayParameter(ps,payOpenedMerPay,n);
            rs = ps.executeQuery();
            while(rs.next()){
                list.add(getPayOpenedMerPayValue(rs));
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
     * remove PayOpenedMerPay
     * @param merno
     * @throws Exception     
     */
    public void removePayOpenedMerPay(String merno) throws Exception {
        String sql = "delete from PAY_OPENED_MER_PAY where MERNO=?";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            ps.setString(1,merno);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(null, ps, con);
        }
    }
    /**
     * detail PayOpenedMerPay
     * @param merno
     * @return PayOpenedMerPay
     * @throws Exception
     */
    public PayOpenedMerPay detailPayOpenedMerPay(String merno) throws Exception {
        String sql = "select * from PAY_OPENED_MER_PAY where MERNO=?";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            ps.setString(1,merno);
            rs = ps.executeQuery();
            if(rs.next())return getPayOpenedMerPayValue(rs); 
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(rs, ps, con);
        }
        return null;
    }
    /**
     * update PayOpenedMerPay
     * @param payOpenedMerPay
     * @throws Exception
     */
//    public void updatePayOpenedMerPay(PayOpenedMerPay payOpenedMerPay) throws Exception {
//        String sqlTmp = "";
//        if(payOpenedMerPay.merno != null)sqlTmp = sqlTmp + " MERNO=?,";
//        if(payOpenedMerPay.type != null)sqlTmp = sqlTmp + " TYPE=?,";
//        if(payOpenedMerPay.createtime != null)sqlTmp = sqlTmp + " CREATETIME=to_date(?,'yyyy-mm-dd hh24:mi:ss'),";
//        if(sqlTmp.length()==0)return;
//        String sql = "update PAY_OPENED_MER_PAY "+        
//              "set " + 
//              sqlTmp.substring(0,sqlTmp.length()-1) + 
//            " where MERNO=?"; 
//        log.info(sql);
//        Connection con = null;
//        PreparedStatement ps = null;
//        try {
//            con = connection();
//            ps = con.prepareStatement(sql);
//            int n=1;
//            if(payOpenedMerPay.merno != null)ps.setString(n++,payOpenedMerPay.merno);
//            if(payOpenedMerPay.type != null)ps.setString(n++,payOpenedMerPay.type);
//            if(payOpenedMerPay.createtime != null)ps.setString(n++,new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payOpenedMerPay.createtime));
//            ps.setString(n++,payOpenedMerPay.merno);
//            ps.executeUpdate();
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw e;
//        }finally {
//            close(null, ps, con);
//        }
//    }

}