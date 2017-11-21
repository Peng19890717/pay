package com.pay.adjustaccount.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;

import util.Tools;

import java.sql.ResultSet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

import com.jweb.dao.BaseDAO;
import com.pay.accprofile.dao.PayAccProfileFrozen;
import com.pay.merchant.dao.PayMerchant;
import com.pay.merchant.dao.PayMerchantDAO;
/**
 * Table PAY_ADJUST_ACCOUNT_CASH DAO. 
 * @author Administrator
 *
 */
public class PayAdjustAccountCashDAO  extends BaseDAO {
    private static final Log log = LogFactory.getLog(PayAdjustAccountCashDAO.class);
    public static synchronized PayAdjustAccountCash getPayAdjustAccountCashValue(ResultSet rs)throws SQLException {
        PayAdjustAccountCash payAdjustAccountCash = new PayAdjustAccountCash();
        payAdjustAccountCash.id = rs.getString("ID");
        payAdjustAccountCash.custId = rs.getString("CUST_ID");
        payAdjustAccountCash.acType = rs.getString("AC_TYPE");
        payAdjustAccountCash.ccy = rs.getString("CCY");
        payAdjustAccountCash.amt = rs.getLong("AMT");
        payAdjustAccountCash.adjustType = rs.getString("ADJUST_TYPE");
        payAdjustAccountCash.applyUser = rs.getString("APPLY_USER");
        payAdjustAccountCash.applyIp = rs.getString("APPLY_IP");
        payAdjustAccountCash.applyTime = rs.getTimestamp("APPLY_TIME");
        payAdjustAccountCash.checkUser = rs.getString("CHECK_USER");
        payAdjustAccountCash.checkIp = rs.getString("CHECK_IP");
        payAdjustAccountCash.checkTime = rs.getTimestamp("CHECK_TIME");
        payAdjustAccountCash.status = rs.getString("STATUS");
        payAdjustAccountCash.remark = rs.getString("REMARK");
        payAdjustAccountCash.cashAcBal = rs.getLong("CASH_AC_BAL");
        payAdjustAccountCash.applyName = rs.getString("APPLY_NAME");
        payAdjustAccountCash.checkName = rs.getString("CHECK_NAME");
        payAdjustAccountCash.adjustBussType = rs.getString("ADJUST_BUSS_TYPE");
        return payAdjustAccountCash;
    }
    public String addPayAdjustAccountCash(PayAdjustAccountCash payAdjustAccountCash) throws Exception {
        String sql = 
        	"insert into PAY_ADJUST_ACCOUNT_CASH("+
            "ID," + 
            "CUST_ID," + 
            "AC_TYPE," + 
            "CCY," + 
            "AMT," + 
            "ADJUST_TYPE," + 
            "APPLY_USER," + 
            "APPLY_IP," + 
            "APPLY_TIME," + 
            "CHECK_USER," + 
            "CHECK_IP," + 
            "CHECK_TIME," + 
            "STATUS," + 
            "REMARK,ADJUST_BUSS_TYPE)values(?,?,?,?,?,?,?,?,to_date(?,'yyyy-mm-dd hh24:mi:ss'),?,?,to_date(?,'yyyy-mm-dd hh24:mi:ss'),?,?,?)";
        log.info(sql);
        log.info("ID="+payAdjustAccountCash.id+";CUST_ID="+payAdjustAccountCash.custId+";AC_TYPE="+payAdjustAccountCash.acType+
        		";CCY="+payAdjustAccountCash.ccy+";AMT="+payAdjustAccountCash.amt+";ADJUST_TYPE="+payAdjustAccountCash.adjustType+
        		"APPLY_USER="+payAdjustAccountCash.applyUser+";APPLY_IP="+payAdjustAccountCash.applyIp+";CHECK_USER="+payAdjustAccountCash.checkUser+";CHECK_IP="+
        		payAdjustAccountCash.checkIp+";STATUS="+payAdjustAccountCash.status+";REMARK="
        		+payAdjustAccountCash.remark+";ADJUST_BUSS_TYPE="+payAdjustAccountCash.adjustBussType);
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            int n=1;
            ps.setString(n++,payAdjustAccountCash.id);
            ps.setString(n++,payAdjustAccountCash.custId);
            ps.setString(n++,payAdjustAccountCash.acType);
            ps.setString(n++,payAdjustAccountCash.ccy);
            ps.setLong(n++,payAdjustAccountCash.amt);
            ps.setString(n++,payAdjustAccountCash.adjustType);
            ps.setString(n++,payAdjustAccountCash.applyUser);
            ps.setString(n++,payAdjustAccountCash.applyIp);
            ps.setString(n++,new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payAdjustAccountCash.applyTime));
            ps.setString(n++,payAdjustAccountCash.checkUser);
            ps.setString(n++,payAdjustAccountCash.checkIp);
            ps.setString(n++,new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payAdjustAccountCash.checkTime));
            ps.setString(n++,payAdjustAccountCash.status);
            ps.setString(n++,payAdjustAccountCash.remark);
            ps.setString(n++,payAdjustAccountCash.adjustBussType);
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
        String sql = "select * from PAY_ADJUST_ACCOUNT_CASH";
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
                list.add(getPayAdjustAccountCashValue(rs));
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
     * @param payAdjustAccountCash
     * @return
     */
    private String setPayAdjustAccountCashSql(PayAdjustAccountCash payAdjustAccountCash) {
        StringBuffer sql = new StringBuffer();
        
        if(payAdjustAccountCash.custId != null && payAdjustAccountCash.custId.length() !=0) {
            sql.append(" CUST_ID = ? and ");
        }
        if(payAdjustAccountCash.acType != null && payAdjustAccountCash.acType.length() !=0) {
            sql.append(" tmp.AC_TYPE = ? and ");
        }
        if(payAdjustAccountCash.applyUser != null && payAdjustAccountCash.applyUser.length() !=0) {
            sql.append(" APPLY_USER = ? and ");
        }
        if(payAdjustAccountCash.checkUser != null && payAdjustAccountCash.checkUser.length() !=0) {
            sql.append(" CHECK_USER = ? and ");
        }
        if(payAdjustAccountCash.applyTime != null) {
            sql.append(" APPLY_TIME = to_date(?,'yyyy-mm-dd hh24:mi:ss') and ");
        }
        if(payAdjustAccountCash.status != null && payAdjustAccountCash.status.length() !=0) {
            sql.append(" STATUS = ? and ");
        }
        if(payAdjustAccountCash.adjustBussType != null && payAdjustAccountCash.adjustBussType.length() !=0) {
            sql.append(" ADJUST_BUSS_TYPE = ? and ");
        }
        String tmp = sql.toString();
        if(tmp.length()>0)tmp=tmp.substring(0,tmp.lastIndexOf(" and "));
        return tmp;
    }
    private String setPayAdjustAccountCashCountSql(PayAdjustAccountCash payAdjustAccountCash) {
        StringBuffer sql = new StringBuffer();
        
        if(payAdjustAccountCash.custId != null && payAdjustAccountCash.custId.length() !=0) {
            sql.append(" CUST_ID = ? and ");
        }
        if(payAdjustAccountCash.acType != null && payAdjustAccountCash.acType.length() !=0) {
            sql.append(" AC_TYPE = ? and ");
        }
        if(payAdjustAccountCash.applyUser != null && payAdjustAccountCash.applyUser.length() !=0) {
            sql.append(" APPLY_USER = ? and ");
        }
        if(payAdjustAccountCash.checkUser != null && payAdjustAccountCash.checkUser.length() !=0) {
            sql.append(" CHECK_USER = ? and ");
        }
        if(payAdjustAccountCash.applyTime != null) {
            sql.append(" APPLY_TIME = to_date(?,'yyyy-mm-dd hh24:mi:ss') and ");
        }
        if(payAdjustAccountCash.status != null && payAdjustAccountCash.status.length() !=0) {
            sql.append(" STATUS = ? and ");
        }
        if(payAdjustAccountCash.adjustBussType != null && payAdjustAccountCash.adjustBussType.length() !=0) {
            sql.append(" ADJUST_BUSS_TYPE = ? and ");
        }
        String tmp = sql.toString();
        if(tmp.length()>0)tmp=tmp.substring(0,tmp.lastIndexOf(" and "));
        return tmp;
    }
    /**
     * Set query parameter.
     * @param ps
     * @param payAdjustAccountCash
     * @param n
     * @return
     * @throws SQLException
     */
    private int setPayAdjustAccountCashParameter(PreparedStatement ps,PayAdjustAccountCash payAdjustAccountCash,int n)throws SQLException {
        if(payAdjustAccountCash.custId != null && payAdjustAccountCash.custId.length() !=0) {
            ps.setString(n++,payAdjustAccountCash.custId);
        }
        if(payAdjustAccountCash.acType != null && payAdjustAccountCash.acType.length() !=0) {
            ps.setString(n++,payAdjustAccountCash.acType);
        }
        if(payAdjustAccountCash.applyUser != null && payAdjustAccountCash.applyUser.length() !=0) {
            ps.setString(n++,payAdjustAccountCash.applyUser);
        }
        if(payAdjustAccountCash.checkUser != null && payAdjustAccountCash.checkUser.length() !=0) {
            ps.setString(n++,payAdjustAccountCash.checkUser);
        }
        if(payAdjustAccountCash.applyTime != null) {
            ps.setString(n++, new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payAdjustAccountCash.applyTime));
        }
        if(payAdjustAccountCash.status != null && payAdjustAccountCash.status.length() !=0) {
            ps.setString(n++,payAdjustAccountCash.status);
        }
        if(payAdjustAccountCash.adjustBussType != null && payAdjustAccountCash.adjustBussType.length() !=0) {
            ps.setString(n++,payAdjustAccountCash.adjustBussType);
        }
        return n;
    }
    /**
     * Get records count.
     * @param payAdjustAccountCash
     * @return
     */
    public int getPayAdjustAccountCashCount(PayAdjustAccountCash payAdjustAccountCash) {
        String sqlCon = setPayAdjustAccountCashCountSql(payAdjustAccountCash);
        String sql = "select count(rownum) recordCount from PAY_ADJUST_ACCOUNT_CASH " +(sqlCon.length()==0?"":" where "+ sqlCon);
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            int n = 1;
            setPayAdjustAccountCashParameter(ps,payAdjustAccountCash,n);
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
     * @param payAdjustAccountCash
     * @param page
     * @param rows
     * @param sort
     * @param order
     * @return
     * @throws Exception
     */
    public List getPayAdjustAccountCashList(PayAdjustAccountCash payAdjustAccountCash,int page,int rows,String sort,String order) throws Exception{
        sort = Tools.paraNameToDBColumnName(sort);
        String sqlCon = setPayAdjustAccountCashSql(payAdjustAccountCash);
        String sortOrder = sort == null || sort.length()==0?" order by APPLY_TIME desc":(" order by " + sort +("desc".equals(order)?" desc ":" asc "));
        String sql = "select * from (" +
                "  SELECT rownum rowno,tmp1.* from (" +
                "  SELECT tmp.*,pro.CASH_AC_BAL,juser1.name APPLY_NAME,juser2.name CHECK_NAME"
                + " FROM PAY_ADJUST_ACCOUNT_CASH tmp "
                + "LEFT JOIN PAY_ACC_PROFILE pro ON tmp.cust_id = pro.PAY_AC_NO "
                + "LEFT JOIN PAY_JWEB_USER juser1 ON tmp.APPLY_USER = juser1.ID "
                + "LEFT JOIN PAY_JWEB_USER juser2 ON tmp.CHECK_USER = juser2.ID" 
                +(sqlCon.length()==0?"":" where "+ sqlCon) + sortOrder +
                " ) tmp1 "+ 
                ")  where rowno > "+((page-1)*rows)+ " and rowno<= " + (page*rows) + sortOrder;
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<PayAdjustAccountCash> list = new ArrayList<PayAdjustAccountCash>();
        List<String> ids = new ArrayList<String> ();
        try {
            con = connection();
            ps = con.prepareStatement(sql.toString());
            int n = 1;
            setPayAdjustAccountCashParameter(ps,payAdjustAccountCash,n);
            rs = ps.executeQuery();
            while(rs.next()){
            	PayAdjustAccountCash adj = getPayAdjustAccountCashValue(rs);
                list.add(adj);
                ids.add(adj.custId);
            }
            Map<String,PayMerchant> merMap = new PayMerchantDAO().getMerchantesByCustIds(ids);
            for(int i=0; i<list.size(); i++)list.get(i).storeName=merMap.get(list.get(i).custId).storeName;
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(rs, ps, con);
        }
    }
    /**
     * remove PayAdjustAccountCash
     * @param id
     * @throws Exception     
     */
    public void removePayAdjustAccountCash(String id) throws Exception {
        String sql = "delete from PAY_ADJUST_ACCOUNT_CASH where ID=?";
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
     * 更新余额 PayAdjustAccountCash
     * @param id
     * @throws Exception     
     */
    public void updatePayAdjustAccountCashCheck(PayAdjustAccountCash payAdjustAccountCash) throws Exception {
        String sql = "update PAY_ADJUST_ACCOUNT_CASH set CHECK_USER=?,CHECK_IP=?,CHECK_TIME=sysdate,STATUS=?,REMARK=?  where ID=?";
        log.info(sql);
        log.info("CHECK_USER="+payAdjustAccountCash.checkUser+";CHECK_IP="+payAdjustAccountCash.checkIp+";STATUS="+payAdjustAccountCash.status+
        		";REMARK"+payAdjustAccountCash.remark+";ID="+payAdjustAccountCash.id);
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            int n=1;
            ps.setString(n++,payAdjustAccountCash.checkUser);
            ps.setString(n++,payAdjustAccountCash.checkIp);
            ps.setString(n++,payAdjustAccountCash.status);
            ps.setString(n++,payAdjustAccountCash.remark);
            ps.setString(n++,payAdjustAccountCash.id);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(null, ps, con);
        }
    }
    /**
     * detail PayAdjustAccountCash
     * @param id
     * @return PayAdjustAccountCash
     * @throws Exception
     */
    public PayAdjustAccountCash detailPayAdjustAccountCash(String id) throws Exception {
        String sql = "SELECT tmp.*,pro.CASH_AC_BAL,juser1.name APPLY_NAME,juser2.name CHECK_NAME "
        		+ "from PAY_ADJUST_ACCOUNT_CASH tmp "
        		+ "LEFT JOIN PAY_ACC_PROFILE pro ON tmp.cust_id = pro.PAY_AC_NO "
        		+ "LEFT JOIN PAY_JWEB_USER juser1 ON tmp.APPLY_USER = juser1.ID "
                + "LEFT JOIN PAY_JWEB_USER juser2 ON tmp.CHECK_USER = juser2.ID " 
        		+ "where tmp.ID=?";
        
        log.info(sql);
        log.info("ID="+id);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            ps.setString(1,id);
            rs = ps.executeQuery();
            if(rs.next())return getPayAdjustAccountCashValue(rs); 
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(rs, ps, con);
        }
        return null;
    }
    
    /**
     * update PayAdjustAccountCash
     * @param payAdjustAccountCash
     * @throws Exception
     */
    public void updatePayAdjustAccountCash(PayAdjustAccountCash payAdjustAccountCash) throws Exception {
        String sqlTmp = "";
        if(payAdjustAccountCash.id != null)sqlTmp = sqlTmp + " ID=?,";
        if(payAdjustAccountCash.custId != null)sqlTmp = sqlTmp + " CUST_ID=?,";
        if(payAdjustAccountCash.acType != null)sqlTmp = sqlTmp + " AC_TYPE=?,";
        if(payAdjustAccountCash.ccy != null)sqlTmp = sqlTmp + " CCY=?,";
        if(payAdjustAccountCash.amt != null)sqlTmp = sqlTmp + " AMT=?,";
        if(payAdjustAccountCash.adjustType != null)sqlTmp = sqlTmp + " ADJUST_TYPE=?,";
        if(payAdjustAccountCash.applyUser != null)sqlTmp = sqlTmp + " APPLY_USER=?,";
        if(payAdjustAccountCash.applyIp != null)sqlTmp = sqlTmp + " APPLY_IP=?,";
        if(payAdjustAccountCash.applyTime != null)sqlTmp = sqlTmp + " APPLY_TIME=to_date(?,'yyyy-mm-dd hh24:mi:ss'),";
        if(payAdjustAccountCash.checkUser != null)sqlTmp = sqlTmp + " CHECK_USER=?,";
        if(payAdjustAccountCash.checkIp != null)sqlTmp = sqlTmp + " CHECK_IP=?,";
        if(payAdjustAccountCash.checkTime != null)sqlTmp = sqlTmp + " CHECK_TIME=to_date(?,'yyyy-mm-dd hh24:mi:ss'),";
        if(payAdjustAccountCash.status != null)sqlTmp = sqlTmp + " STATUS=?,";
        if(payAdjustAccountCash.remark != null)sqlTmp = sqlTmp + " REMARK=?,";
        if(sqlTmp.length()==0)return;
        String sql = "update PAY_ADJUST_ACCOUNT_CASH "+        
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
            if(payAdjustAccountCash.id != null)ps.setString(n++,payAdjustAccountCash.id);
            if(payAdjustAccountCash.custId != null)ps.setString(n++,payAdjustAccountCash.custId);
            if(payAdjustAccountCash.acType != null)ps.setString(n++,payAdjustAccountCash.acType);
            if(payAdjustAccountCash.ccy != null)ps.setString(n++,payAdjustAccountCash.ccy);
            if(payAdjustAccountCash.amt != null)ps.setLong(n++,payAdjustAccountCash.amt);
            if(payAdjustAccountCash.adjustType != null)ps.setString(n++,payAdjustAccountCash.adjustType);
            if(payAdjustAccountCash.applyUser != null)ps.setString(n++,payAdjustAccountCash.applyUser);
            if(payAdjustAccountCash.applyIp != null)ps.setString(n++,payAdjustAccountCash.applyIp);
            if(payAdjustAccountCash.applyTime != null)ps.setString(n++,new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payAdjustAccountCash.applyTime));
            if(payAdjustAccountCash.checkUser != null)ps.setString(n++,payAdjustAccountCash.checkUser);
            if(payAdjustAccountCash.checkIp != null)ps.setString(n++,payAdjustAccountCash.checkIp);
            if(payAdjustAccountCash.checkTime != null)ps.setString(n++,new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payAdjustAccountCash.checkTime));
            if(payAdjustAccountCash.status != null)ps.setString(n++,payAdjustAccountCash.status);
            if(payAdjustAccountCash.remark != null)ps.setString(n++,payAdjustAccountCash.remark);
            ps.setString(n++,payAdjustAccountCash.id);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }finally {
            close(null, ps, con);
        }
    }
    /**
     * update PayAdjustAccountCash
     * @param payAdjustAccountCash
     * @throws Exception
     */
    public void setPayAdjustAccountCashCheck(PayAdjustAccountCash payAdjustAccountCash) throws Exception {
        String sql = "update PAY_ADJUST_ACCOUNT_CASH set REMARK=REMARK+?  where ID=?"; 
        log.info(sql);
        log.info("REMARK="+payAdjustAccountCash.remark+";ID="+payAdjustAccountCash.id);
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            int n=1;
            ps.setString(n++,payAdjustAccountCash.remark!=null&&payAdjustAccountCash.remark.length()>0?";"+payAdjustAccountCash.remark:"");
            ps.setString(n++,payAdjustAccountCash.id);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }finally {
            close(null, ps, con);
        }
    }
    /**
     * 查询调账总金额
     * @param payAdjustAccountCash
     * @return
     * @throws Exception 
     */
	public Long getTotalAccountCashMoney(
			PayAdjustAccountCash payAdjustAccountCash) throws Exception {
		Long l1 = null;
		String sqlCon = setPayAdjustAccountCashSql(payAdjustAccountCash);
		String sql = "SELECT sum(amt) totalAccountCashMoney from (" +
                "  SELECT tmp.*,pro.CASH_AC_BAL,juser1.name APPLY_NAME,juser2.name CHECK_NAME"
                + " FROM PAY_ADJUST_ACCOUNT_CASH tmp "
                + "LEFT JOIN PAY_ACC_PROFILE pro ON tmp.cust_id = pro.PAY_AC_NO "
                + "LEFT JOIN PAY_JWEB_USER juser1 ON tmp.APPLY_USER = juser1.ID "
                + "LEFT JOIN PAY_JWEB_USER juser2 ON tmp.CHECK_USER = juser2.ID" 
                +(sqlCon.length()==0?"":" where "+ sqlCon) + 
                " ) ";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List list = new ArrayList();
        try {
            con = connection();
            ps = con.prepareStatement(sql.toString());
            int n = 1;
            setPayAdjustAccountCashParameter(ps,payAdjustAccountCash,n);
            rs = ps.executeQuery();
            if (rs.next()) {
            	l1 = rs.getLong("totalAccountCashMoney");
            }
            return l1==null?0l:l1;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(rs, ps, con);
        }
	}
}