package com.pay.usercard.dao;

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
 * Table PAY_SAME_CARD_IN_OUT DAO. 
 * @author Administrator
 *
 */
public class PaySameCardInOutDAO  extends BaseDAO {
    private static final Log log = LogFactory.getLog(PaySameCardInOutDAO.class);
    public static synchronized PaySameCardInOut getPaySameCardInOutValue(ResultSet rs)throws SQLException {
        PaySameCardInOut paySameCardInOut = new PaySameCardInOut();
        paySameCardInOut.id = rs.getString("ID");
        paySameCardInOut.custId = rs.getString("CUST_ID");
        paySameCardInOut.bankName = rs.getString("BANK_NAME");
        paySameCardInOut.bankCode = rs.getString("BANK_CODE");
        paySameCardInOut.accountType = rs.getString("ACCOUNT_TYPE");
        paySameCardInOut.accountNo = rs.getString("ACCOUNT_NO");
        paySameCardInOut.accountName = rs.getString("ACCOUNT_NAME");
        paySameCardInOut.credentialType = rs.getString("CREDENTIAL_TYPE");
        paySameCardInOut.credentialNo = rs.getString("CREDENTIAL_NO");
        paySameCardInOut.tel = rs.getString("TEL");
        paySameCardInOut.status = rs.getString("STATUS");
        paySameCardInOut.remark = rs.getString("REMARK");
        paySameCardInOut.createTime = rs.getTimestamp("CREATE_TIME");
        return paySameCardInOut;
    }
    public String addPaySameCardInOut(PaySameCardInOut paySameCardInOut) throws Exception {
        String sql = "insert into PAY_SAME_CARD_IN_OUT("+
            "ID," + 
            "CUST_ID," + 
            "BANK_NAME," + 
            "BANK_CODE," + 
            "ACCOUNT_TYPE," + 
            "ACCOUNT_NO," + 
            "ACCOUNT_NAME," + 
            "CREDENTIAL_TYPE," + 
            "CREDENTIAL_NO," + 
            "TEL," + 
            "STATUS," + 
            "REMARK," + 
            "CREATE_TIME)values(?,?,?,?,?,?,?,?,?,?,?,?,to_date(?,'yyyy-mm-dd hh24:mi:ss'))";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            int n=1;
            ps.setString(n++,paySameCardInOut.id);
            ps.setString(n++,paySameCardInOut.custId);
            ps.setString(n++,paySameCardInOut.bankName);
            ps.setString(n++,paySameCardInOut.bankCode);
            ps.setString(n++,paySameCardInOut.accountType);
            ps.setString(n++,paySameCardInOut.accountNo);
            ps.setString(n++,paySameCardInOut.accountName);
            ps.setString(n++,paySameCardInOut.credentialType);
            ps.setString(n++,paySameCardInOut.credentialNo);
            ps.setString(n++,paySameCardInOut.tel);
            ps.setString(n++,paySameCardInOut.status);
            ps.setString(n++,paySameCardInOut.remark);
            ps.setString(n++,new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(paySameCardInOut.createTime));
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
        String sql = "select * from PAY_SAME_CARD_IN_OUT";
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
                list.add(getPaySameCardInOutValue(rs));
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
     * @param paySameCardInOut
     * @return
     */
    private String setPaySameCardInOutSql(PaySameCardInOut paySameCardInOut) {
        StringBuffer sql = new StringBuffer();
        
        if(paySameCardInOut.custId != null && paySameCardInOut.custId.length() !=0) {
            sql.append(" CUST_ID = ? and ");
        }
        if(paySameCardInOut.accountNo != null && paySameCardInOut.accountNo.length() !=0) {
            sql.append(" ACCOUNT_NO = ? and ");
        }
        if(paySameCardInOut.credentialNo != null && paySameCardInOut.credentialNo.length() !=0) {
            sql.append(" CREDENTIAL_NO = ? and ");
        }
        String tmp = sql.toString();
        if(tmp.length()>0)tmp=tmp.substring(0,tmp.lastIndexOf(" and "));
        return tmp;
    }
    /**
     * Set query parameter.
     * @param ps
     * @param paySameCardInOut
     * @param n
     * @return
     * @throws SQLException
     */
    private int setPaySameCardInOutParameter(PreparedStatement ps,PaySameCardInOut paySameCardInOut,int n)throws SQLException {
        if(paySameCardInOut.custId != null && paySameCardInOut.custId.length() !=0) {
            ps.setString(n++,paySameCardInOut.custId);
        }
        if(paySameCardInOut.accountNo != null && paySameCardInOut.accountNo.length() !=0) {
            ps.setString(n++,paySameCardInOut.accountNo);
        }
        if(paySameCardInOut.credentialNo != null && paySameCardInOut.credentialNo.length() !=0) {
            ps.setString(n++,paySameCardInOut.credentialNo);
        }
        return n;
    }
    /**
     * Get records count.
     * @param paySameCardInOut
     * @return
     */
    public int getPaySameCardInOutCount(PaySameCardInOut paySameCardInOut) {
        String sqlCon = setPaySameCardInOutSql(paySameCardInOut);
        String sql = "select count(rownum) recordCount from PAY_SAME_CARD_IN_OUT " +(sqlCon.length()==0?"":" where "+ sqlCon);
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            int n = 1;
            setPaySameCardInOutParameter(ps,paySameCardInOut,n);
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
     * @param paySameCardInOut
     * @param page
     * @param rows
     * @param sort
     * @param order
     * @return
     * @throws Exception
     */
    public List getPaySameCardInOutList(PaySameCardInOut paySameCardInOut,int page,int rows,String sort,String order) throws Exception{
        sort = Tools.paraNameToDBColumnName(sort);
        String sqlCon = setPaySameCardInOutSql(paySameCardInOut);
        String sortOrder = sort == null || sort.length()==0?" order by ID desc":(" order by " + sort +("desc".equals(order)?" desc ":" asc "));
        String sql = "select * from (" +
                "  select rownum rowno,tmp1.* from (" +
                "   select tmp.*  from PAY_SAME_CARD_IN_OUT tmp " +(sqlCon.length()==0?"":" where "+ sqlCon) +sortOrder+
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
            setPaySameCardInOutParameter(ps,paySameCardInOut,n);
            rs = ps.executeQuery();
            while(rs.next()){
                list.add(getPaySameCardInOutValue(rs));
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
     * remove PaySameCardInOut
     * @param id
     * @throws Exception     
     */
    public void removePaySameCardInOut(String id) throws Exception {
        String sql = "delete from PAY_SAME_CARD_IN_OUT where ID=?";
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
     * detail PaySameCardInOut
     * @param id
     * @return PaySameCardInOut
     * @throws Exception
     */
    public PaySameCardInOut detailPaySameCardInOut(String id) throws Exception {
        String sql = "select * from PAY_SAME_CARD_IN_OUT where ID=?";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            ps.setString(1,id);
            rs = ps.executeQuery();
            if(rs.next())return getPaySameCardInOutValue(rs); 
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(rs, ps, con);
        }
        return null;
    }
    /**
     * update PaySameCardInOut
     * @param paySameCardInOut
     * @throws Exception
     */
    public void updatePaySameCardInOut(PaySameCardInOut paySameCardInOut) throws Exception {
        String sqlTmp = "";
        if(paySameCardInOut.id != null)sqlTmp = sqlTmp + " ID=?,";
        if(paySameCardInOut.custId != null)sqlTmp = sqlTmp + " CUST_ID=?,";
        if(paySameCardInOut.bankName != null)sqlTmp = sqlTmp + " BANK_NAME=?,";
        if(paySameCardInOut.bankCode != null)sqlTmp = sqlTmp + " BANK_CODE=?,";
        if(paySameCardInOut.accountType != null)sqlTmp = sqlTmp + " ACCOUNT_TYPE=?,";
        if(paySameCardInOut.accountNo != null)sqlTmp = sqlTmp + " ACCOUNT_NO=?,";
        if(paySameCardInOut.accountName != null)sqlTmp = sqlTmp + " ACCOUNT_NAME=?,";
        if(paySameCardInOut.credentialType != null)sqlTmp = sqlTmp + " CREDENTIAL_TYPE=?,";
        if(paySameCardInOut.credentialNo != null)sqlTmp = sqlTmp + " CREDENTIAL_NO=?,";
        if(paySameCardInOut.tel != null)sqlTmp = sqlTmp + " TEL=?,";
        if(paySameCardInOut.status != null)sqlTmp = sqlTmp + " STATUS=?,";
        if(paySameCardInOut.remark != null)sqlTmp = sqlTmp + " REMARK=?,";
        if(paySameCardInOut.createTime != null)sqlTmp = sqlTmp + " CREATE_TIME=to_date(?,'yyyy-mm-dd hh24:mi:ss'),";
        if(sqlTmp.length()==0)return;
        String sql = "update PAY_SAME_CARD_IN_OUT "+        
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
            if(paySameCardInOut.id != null)ps.setString(n++,paySameCardInOut.id);
            if(paySameCardInOut.custId != null)ps.setString(n++,paySameCardInOut.custId);
            if(paySameCardInOut.bankName != null)ps.setString(n++,paySameCardInOut.bankName);
            if(paySameCardInOut.bankCode != null)ps.setString(n++,paySameCardInOut.bankCode);
            if(paySameCardInOut.accountType != null)ps.setString(n++,paySameCardInOut.accountType);
            if(paySameCardInOut.accountNo != null)ps.setString(n++,paySameCardInOut.accountNo);
            if(paySameCardInOut.accountName != null)ps.setString(n++,paySameCardInOut.accountName);
            if(paySameCardInOut.credentialType != null)ps.setString(n++,paySameCardInOut.credentialType);
            if(paySameCardInOut.credentialNo != null)ps.setString(n++,paySameCardInOut.credentialNo);
            if(paySameCardInOut.tel != null)ps.setString(n++,paySameCardInOut.tel);
            if(paySameCardInOut.status != null)ps.setString(n++,paySameCardInOut.status);
            if(paySameCardInOut.remark != null)ps.setString(n++,paySameCardInOut.remark);
            if(paySameCardInOut.createTime != null)ps.setString(n++,new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(paySameCardInOut.createTime));
            ps.setString(n++,paySameCardInOut.id);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }finally {
            close(null, ps, con);
        }
    }

}