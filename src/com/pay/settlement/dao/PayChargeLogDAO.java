package com.pay.settlement.dao;

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
 * Table PAY_CHARGE_LOG DAO. 
 * @author Administrator
 *
 */
public class PayChargeLogDAO  extends BaseDAO {
    private static final Log log = LogFactory.getLog(PayChargeLogDAO.class);
    public static synchronized PayChargeLog getPayChargeLogValue(ResultSet rs)throws SQLException {
        PayChargeLog payChargeLog = new PayChargeLog();
        payChargeLog.id = rs.getString("ID");
        payChargeLog.custType = rs.getString("CUST_TYPE");
        payChargeLog.custId = rs.getString("CUST_ID");
        payChargeLog.chargeType = rs.getString("CHARGE_TYPE");
        payChargeLog.chargeFrom = rs.getString("CHARGE_FROM");
        payChargeLog.amt = rs.getLong("AMT");
        payChargeLog.remark = rs.getString("REMARK");
        payChargeLog.createId = rs.getString("CREATE_ID");
        payChargeLog.createTime = rs.getTimestamp("CREATE_TIME");
        payChargeLog.settleTime = rs.getTimestamp("SETTLE_TIME");
        payChargeLog.curStorageFee = rs.getLong("cur_storage_fee");
        try {payChargeLog.custName = rs.getString("cust_name");} catch (Exception e) {}
        return payChargeLog;
    }
    public String addPayChargeLog(PayChargeLog payChargeLog) throws Exception {
        String sql = "insert into PAY_CHARGE_LOG("+
            "ID," + 
            "CUST_TYPE," + 
            "CUST_ID," + 
            "CHARGE_TYPE," + 
            "CHARGE_FROM," + 
            "AMT," + 
            "REMARK," + 
            "CREATE_ID,SETTLE_TIME,CUR_STORAGE_FEE)values(?,?,?,?,?,?,?,?,to_date(?,'yyyy-mm-dd hh24:mi:ss'),?)";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            int n=1;
            ps.setString(n++,payChargeLog.id);
            ps.setString(n++,payChargeLog.custType);
            ps.setString(n++,payChargeLog.custId);
            ps.setString(n++,payChargeLog.chargeType);
            ps.setString(n++,payChargeLog.chargeFrom);
            ps.setLong(n++,payChargeLog.amt);
            ps.setString(n++,payChargeLog.remark);
            ps.setString(n++,payChargeLog.createId);
            ps.setString(n++,new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payChargeLog.settleTime));
            ps.setLong(n++,payChargeLog.curStorageFee);
            ps.executeUpdate();
            return "";
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }finally {
            close(null, ps, con);
        }
    }
    public PayChargeLog deleteStlDayLog(PayChargeLog payChargeLog) throws Exception {
    	String search = "select * from PAY_CHARGE_LOG where CUST_TYPE=? and CUST_ID=? and " +
        		" SETTLE_TIME >= to_date(?,'yyyy-mm-dd hh24:mi:ss') and SETTLE_TIME <= to_date(?,'yyyy-mm-dd hh24:mi:ss')";
        String sql = "delete from PAY_CHARGE_LOG where CUST_TYPE=? and CUST_ID=? and " +
        		" SETTLE_TIME >= to_date(?,'yyyy-mm-dd hh24:mi:ss') and SETTLE_TIME <= to_date(?,'yyyy-mm-dd hh24:mi:ss')";
        log.info(search);
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        PayChargeLog oldChargeLog = null;
        try {
            con = connection();
            ps = con.prepareStatement(search);
            int n=1;
            ps.setString(n++,payChargeLog.custType);
            ps.setString(n++,payChargeLog.custId);
            ps.setString(n++,new java.text.SimpleDateFormat("yyyy-MM-dd").format(payChargeLog.settleTime)+" 00:00:00");
        	ps.setString(n++,new java.text.SimpleDateFormat("yyyy-MM-dd").format(payChargeLog.settleTime)+" 23:59:59");
            rs = ps.executeQuery();
            if(rs.next())oldChargeLog = getPayChargeLogValue(rs);
            ps.close();
            ps = con.prepareStatement(sql);
            n=1;
            ps.setString(n++,payChargeLog.custType);
            ps.setString(n++,payChargeLog.custId);
            ps.setString(n++,new java.text.SimpleDateFormat("yyyy-MM-dd").format(payChargeLog.settleTime)+" 00:00:00");
        	ps.setString(n++,new java.text.SimpleDateFormat("yyyy-MM-dd").format(payChargeLog.settleTime)+" 23:59:59");
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(rs, ps, con);
        }
        return oldChargeLog;
    }
    public List getList() throws Exception{
        String sql = "select * from PAY_CHARGE_LOG";
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
                list.add(getPayChargeLogValue(rs));
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
     * @param payChargeLog
     * @return
     */
    private String setPayChargeLogSql(PayChargeLog payChargeLog) {
        StringBuffer sql = new StringBuffer();
        
        if(payChargeLog.custType != null && payChargeLog.custType.length() !=0) {
            sql.append(" CUST_TYPE = ? and ");
        }
        if(payChargeLog.custId != null && payChargeLog.custId.length() !=0) {
            sql.append(" CUST_ID = ? and ");
        }
        if(payChargeLog.chargeType != null && payChargeLog.chargeType.length() !=0) {
            sql.append(" CHARGE_TYPE = ? and ");
        }
        if(payChargeLog.createTimeStart != null) {
            sql.append(" CREATE_TIME >= to_date(?,'yyyy-mm-dd hh24:mi:ss') and ");
        }
        if(payChargeLog.createTimeEnd != null) {
        	sql.append(" CREATE_TIME <= to_date(?,'yyyy-mm-dd hh24:mi:ss') and ");
        }
        
        String tmp = sql.toString();
        if(tmp.length()>0)tmp=tmp.substring(0,tmp.lastIndexOf(" and "));
        return tmp;
    }
    /**
     * Set query parameter.
     * @param ps
     * @param payChargeLog
     * @param n
     * @return
     * @throws SQLException
     */
    private int setPayChargeLogParameter(PreparedStatement ps,PayChargeLog payChargeLog,int n)throws SQLException {
    	if(payChargeLog.custType != null && payChargeLog.custType.length() !=0) {
            ps.setString(n++,payChargeLog.custType);
        }
        if(payChargeLog.custId != null && payChargeLog.custId.length() !=0) {
            ps.setString(n++,payChargeLog.custId);
        }
        if(payChargeLog.chargeType != null && payChargeLog.chargeType.length() !=0) {
            ps.setString(n++,payChargeLog.chargeType);
        }
        if(payChargeLog.createTimeStart != null) {
            ps.setString(n++,new java.text.SimpleDateFormat("yyyy-MM-dd").format(payChargeLog.createTimeStart)+" 00:00:00");
        }
        if(payChargeLog.createTimeEnd != null) {
        	ps.setString(n++,new java.text.SimpleDateFormat("yyyy-MM-dd").format(payChargeLog.createTimeEnd)+" 23:59:59");
        }
        return n;
    }
    /**
     * Get records count.
     * @param payChargeLog
     * @return
     */
    public int getPayChargeLogCount(PayChargeLog payChargeLog) {
        String sqlCon = setPayChargeLogSql(payChargeLog);
        String sql = "select count(rownum) recordCount from PAY_CHARGE_LOG " +(sqlCon.length()==0?"":" where "+ sqlCon);
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            int n = 1;
            setPayChargeLogParameter(ps,payChargeLog,n);
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
     * @param payChargeLog
     * @param page
     * @param rows
     * @param sort
     * @param order
     * @return
     * @throws Exception
     */
    public List getPayChargeLogList(PayChargeLog payChargeLog,int page,int rows,String sort,String order) throws Exception{
        sort = Tools.paraNameToDBColumnName(sort);
        String sqlCon = setPayChargeLogSql(payChargeLog);
        String sortOrder = sort == null || sort.length()==0?" order by CREATE_TIME desc":(" order by " + sort +("desc".equals(order)?" desc ":" asc "));
        String sql = "select * from (" +
                "  select rownum rowno,tmp1.* from (" +
                "   select tmp.*  from " +
                " (select c.*,m.store_name cust_name from PAY_CHARGE_LOG c left join pay_merchant m on c.cust_id=m.cust_id) tmp " +(sqlCon.length()==0?"":" where "+ sqlCon) +sortOrder+
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
            setPayChargeLogParameter(ps,payChargeLog,n);
            rs = ps.executeQuery();
            while(rs.next()){
                list.add(getPayChargeLogValue(rs));
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
     * remove PayChargeLog
     * @param id
     * @throws Exception     
     */
    public void removePayChargeLog(String id) throws Exception {
        String sql = "delete from PAY_CHARGE_LOG where ID=?";
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
     * detail PayChargeLog
     * @param id
     * @return PayChargeLog
     * @throws Exception
     */
    public PayChargeLog detailPayChargeLog(String id) throws Exception {
        String sql = "select * from PAY_CHARGE_LOG where ID=?";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            ps.setString(1,id);
            rs = ps.executeQuery();
            if(rs.next())return getPayChargeLogValue(rs); 
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(rs, ps, con);
        }
        return null;
    }
    /**
     * update PayChargeLog
     * @param payChargeLog
     * @throws Exception
     */
    public void updatePayChargeLog(PayChargeLog payChargeLog) throws Exception {
        String sqlTmp = "";
        if(payChargeLog.id != null)sqlTmp = sqlTmp + " ID=?,";
        if(payChargeLog.custType != null)sqlTmp = sqlTmp + " CUST_TYPE=?,";
        if(payChargeLog.custId != null)sqlTmp = sqlTmp + " CUST_ID=?,";
        if(payChargeLog.chargeType != null)sqlTmp = sqlTmp + " CHARGE_TYPE=?,";
        if(payChargeLog.chargeFrom != null)sqlTmp = sqlTmp + " CHARGE_FROM=?,";
        if(payChargeLog.amt != null)sqlTmp = sqlTmp + " AMT=?,";
        if(payChargeLog.remark != null)sqlTmp = sqlTmp + " REMARK=?,";
        if(payChargeLog.createId != null)sqlTmp = sqlTmp + " CREATE_ID=?,";
        if(payChargeLog.createTime != null)sqlTmp = sqlTmp + " CREATE_TIME=to_date(?,'yyyy-mm-dd hh24:mi:ss'),";
        if(sqlTmp.length()==0)return;
        String sql = "update PAY_CHARGE_LOG "+        
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
            if(payChargeLog.id != null)ps.setString(n++,payChargeLog.id);
            if(payChargeLog.custType != null)ps.setString(n++,payChargeLog.custType);
            if(payChargeLog.custId != null)ps.setString(n++,payChargeLog.custId);
            if(payChargeLog.chargeType != null)ps.setString(n++,payChargeLog.chargeType);
            if(payChargeLog.chargeFrom != null)ps.setString(n++,payChargeLog.chargeFrom);
            if(payChargeLog.amt != null)ps.setLong(n++,payChargeLog.amt);
            if(payChargeLog.remark != null)ps.setString(n++,payChargeLog.remark);
            if(payChargeLog.createId != null)ps.setString(n++,payChargeLog.createId);
            if(payChargeLog.createTime != null)ps.setString(n++,new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payChargeLog.createTime));
            ps.setString(n++,payChargeLog.id);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }finally {
            close(null, ps, con);
        }
    }
    /**
     * 查询手续费总金额
     * @param payChargeLog
     * @return
     * @throws Exception 
     */
	public Long getTotalChargeLogMoney(PayChargeLog payChargeLog) throws Exception {
		Long l1 = null;
		String sqlCon = setPayChargeLogSql(payChargeLog);
        String sql = " SELECT sum(amt) totalChargeLogMoney  FROM " +
                " ( select tmp.*  from " +
                " (select c.*,m.store_name cust_name from PAY_CHARGE_LOG c left join pay_merchant m on c.cust_id=m.cust_id) tmp " +(sqlCon.length()==0?"":" where "+ sqlCon) +
                "  )  ";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List list = new ArrayList();
        try {
            con = connection();
            ps = con.prepareStatement(sql.toString());
            int n = 1;
            setPayChargeLogParameter(ps,payChargeLog,n);
            rs = ps.executeQuery();
            if (rs.next()) {
            	l1 = rs.getLong("totalChargeLogMoney");
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