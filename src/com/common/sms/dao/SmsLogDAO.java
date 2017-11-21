package com.common.sms.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;

import util.JWebConstant;
import util.Tools;
import java.sql.ResultSet;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;
import com.jweb.dao.BaseDAO;
/**
 * Table SMS_LOG DAO. 
 * @author Administrator
 *
 */
public class SmsLogDAO  extends BaseDAO {
    private static final Log log = LogFactory.getLog(SmsLogDAO.class);
    public static synchronized SmsLog getSmsLogValue(ResultSet rs)throws SQLException {
        SmsLog smsLog = new SmsLog();
        smsLog.id = rs.getString("ID");
        smsLog.tel = rs.getString("TEL");
        smsLog.content = rs.getString("CONTENT");
        smsLog.userId = rs.getString("USER_ID");
        try {smsLog.userName =  rs.getString("USER_NAME");} catch (Exception e) {}
        smsLog.createId = rs.getString("CREATE_ID");
        smsLog.createTime = rs.getTimestamp("CREATE_TIME");
        smsLog.status = rs.getString("STATUS");
        smsLog.remark = rs.getString("REMARK");
        return smsLog;
    }
    public String addSmsLog(List smsLogList) throws Exception {
        String sql = "insert into "+JWebConstant.DB_TABLE_PREFIX+"SMS_LOG("+
            "ID," + 
            "TEL," + 
            "CONTENT," + 
            "USER_ID," + 
            "CREATE_ID," + 
            "CREATE_TIME,STATUS,REMARK)values(?,?,?,?,?,sysdate,?,?)";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            for(int i = 0; i<smsLogList.size(); i++){
            	SmsLog smsLog = (SmsLog)smsLogList.get(i);
	            int n=1;
	            ps.setString(n++,smsLog.id);
	            ps.setString(n++,smsLog.tel);
	            ps.setString(n++,smsLog.content);
	            ps.setString(n++,smsLog.userId);
	            ps.setString(n++,smsLog.createId);
	            ps.setString(n++,smsLog.status);
	            ps.setString(n++,smsLog.remark);
	            ps.addBatch();
            }
            ps.executeBatch();
            return "";
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }finally {
            close(null, ps, con);
        }
    }
    public List getList() throws Exception{        
        String sql = "select * from "+JWebConstant.DB_TABLE_PREFIX+"SMS_LOG";
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
                list.add(getSmsLogValue(rs));
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
     * @param smsLog
     * @return
     */
    private String setSmsLogSql(SmsLog smsLog) {
        StringBuffer sql = new StringBuffer();
        
        if(smsLog.tel != null && smsLog.tel.length() !=0) {
            sql.append(" TEL = ? and ");
        }
        if(smsLog.content != null && smsLog.content.length() !=0) {
            sql.append(" CONTENT like ? and ");
        }
        if(smsLog.createTimeStart != null && smsLog.createTimeStart.length() !=0) {
			sql.append(" CREATE_TIME >= to_date(?,'yyyy-mm-dd hh24:mi:ss') and ");
		}
		if(smsLog.createTimeEnd != null && smsLog.createTimeEnd.length() !=0) {
			sql.append(" CREATE_TIME <= to_date(?,'yyyy-mm-dd hh24:mi:ss') and ");
		}
        String tmp = sql.toString();
        if(tmp.length()>0)tmp=tmp.substring(0,tmp.lastIndexOf(" and "));
        return tmp;
    }
    /**
     * Set query parameter.
     * @param ps
     * @param smsLog
     * @param n
     * @return
     * @throws SQLException
     */
    private int setSmsLogParameter(PreparedStatement ps,SmsLog smsLog,int n)throws SQLException {
        if(smsLog.tel != null && smsLog.tel.length() !=0) {
            ps.setString(n++,smsLog.tel);
        }
        if(smsLog.content != null && smsLog.content.length() !=0) {
            ps.setString(n++,"%"+smsLog.content+"%");
        }
        if(smsLog.createTimeStart != null && smsLog.createTimeStart.length() !=0) {
			ps.setString(n++, smsLog.createTimeStart);
		}
		if(smsLog.createTimeEnd != null && smsLog.createTimeEnd.length() !=0) {
			ps.setString(n++, smsLog.createTimeEnd);
		}
        return n;
    }
    /**
     * Get records count.
     * @param smsLog
     * @return
     */
    public int getSmsLogCount(SmsLog smsLog) {
        String sqlCon = setSmsLogSql(smsLog);
        String sql = "select count(rownum) recordCount from "+JWebConstant.DB_TABLE_PREFIX+"SMS_LOG " +(sqlCon.length()==0?"":" where "+ sqlCon);
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            int n = 1;
            setSmsLogParameter(ps,smsLog,n);
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
     * @param smsLog
     * @param page
     * @param rows
     * @param sort
     * @param order
     * @return
     * @throws Exception
     */
    public List getSmsLogList(SmsLog smsLog,int page,int rows,String sort,String order) throws Exception{
        sort = Tools.paraNameToDBColumnName(sort);
        String sqlCon = setSmsLogSql(smsLog);
        String sortOrder = sort == null || sort.length()==0?" order by ID desc":(" order by " + sort +("desc".equals(order)?" desc ":" asc "));
        String sql = "select * from (" +
                "  select rownum rowno,tmp1.* from (" +
                "   select tmp.*  from "+
                JWebConstant.DB_TABLE_PREFIX+"SMS_LOG tmp " +
                (sqlCon.length()==0?"":" where "+ sqlCon) +sortOrder+
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
            setSmsLogParameter(ps,smsLog,n);
            rs = ps.executeQuery();
            while(rs.next()){
                list.add(getSmsLogValue(rs));
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
     * remove SmsLog
     * @param id
     * @throws Exception     
     */
    public void removeSmsLog(String id) throws Exception {
        String sql = "delete from "+JWebConstant.DB_TABLE_PREFIX+"SMS_LOG where ID=?";
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
     * update SmsLog
     * @param smsLog
     * @throws Exception
     */
    public void updateSmsLog(SmsLog smsLog) throws Exception {
        String sqlTmp = "";
        if(smsLog.id != null)sqlTmp = sqlTmp + " ID=?,";
        if(smsLog.tel != null)sqlTmp = sqlTmp + " TEL=?,";
        if(smsLog.content != null)sqlTmp = sqlTmp + " CONTENT=?,";
        if(smsLog.userId != null)sqlTmp = sqlTmp + " USER_ID=?,";
        if(smsLog.createId != null)sqlTmp = sqlTmp + " CREATE_ID=?,";
        if(smsLog.createTime != null)sqlTmp = sqlTmp + " CREATE_TIME=to_date(?,'yyyy-mm-dd hh24:mi:ss'),";
        if(sqlTmp.length()==0)return;
        String sql = "update "+JWebConstant.DB_TABLE_PREFIX+"SMS_LOG "+        
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
            if(smsLog.id != null)ps.setString(n++,smsLog.id);
            if(smsLog.tel != null)ps.setString(n++,smsLog.tel);
            if(smsLog.content != null)ps.setString(n++,smsLog.content);
            if(smsLog.userId != null)ps.setString(n++,smsLog.userId);
            if(smsLog.createId != null)ps.setString(n++,smsLog.createId);
            if(smsLog.createTime != null)ps.setString(n++,new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(smsLog.createTime));
            ps.setString(n++,smsLog.id);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }finally {
            close(null, ps, con);
        }
    }

}