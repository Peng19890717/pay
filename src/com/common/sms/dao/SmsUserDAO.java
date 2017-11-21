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
 * Table SMS_USER DAO. 
 * @author Administrator
 *
 */
public class SmsUserDAO  extends BaseDAO {
    private static final Log log = LogFactory.getLog(SmsUserDAO.class);
    public static synchronized SmsUser getSmsUserValue(ResultSet rs)throws SQLException {
        SmsUser smsUser = new SmsUser();
        smsUser.userId = rs.getString("USER_ID");
        smsUser.name = rs.getString("NAME");
        smsUser.tel = rs.getString("TEL");
        smsUser.groupId = rs.getString("GROUP_ID");
        try {smsUser.groupName = rs.getString("GROUP_NAME");} catch (Exception e) {}
        smsUser.remark = rs.getString("REMARK");
        smsUser.createId = rs.getString("CREATE_ID");
        smsUser.createTime = rs.getTimestamp("CREATE_TIME");
        return smsUser;
    }
    public String addSmsUser(SmsUser smsUser) throws Exception {
        String sql = "insert into "+JWebConstant.DB_TABLE_PREFIX+"SMS_USER("+
            "USER_ID," + 
            "NAME," + 
            "TEL," + 
            "GROUP_ID," + 
            "REMARK," + 
            "CREATE_ID," + 
            "CREATE_TIME)values(?,?,?,?,?,?,sysdate)";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            int n=1;
            ps.setString(n++,Tools.getUniqueIdentify());
            ps.setString(n++,smsUser.name);
            ps.setString(n++,smsUser.tel);
            ps.setString(n++,smsUser.groupId);
            ps.setString(n++,smsUser.remark);
            ps.setString(n++,smsUser.createId);
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
        String sql = "select * from "+JWebConstant.DB_TABLE_PREFIX+"SMS_USER";
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
                list.add(getSmsUserValue(rs));
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
     * @param smsUser
     * @return
     */
    private String setSmsUserSql(SmsUser smsUser) {
        StringBuffer sql = new StringBuffer();
        
        if(smsUser.name != null && smsUser.name.length() !=0) {
            sql.append(" NAME like ? and ");
        }
        if(smsUser.tel != null && smsUser.tel.length() !=0) {
            sql.append(" TEL like ? and ");
        }
        if(smsUser.groupId != null && smsUser.groupId.length() !=0) {
            sql.append(" GROUP_ID = ? and ");
        }
        String tmp = sql.toString();
        if(tmp.length()>0)tmp=tmp.substring(0,tmp.lastIndexOf(" and "));
        return tmp;
    }
    /**
     * Set query parameter.
     * @param ps
     * @param smsUser
     * @param n
     * @return
     * @throws SQLException
     */
    private int setSmsUserParameter(PreparedStatement ps,SmsUser smsUser,int n)throws SQLException {
        if(smsUser.name != null && smsUser.name.length() !=0) {
            ps.setString(n++,"%"+smsUser.name+"%");
        }
        if(smsUser.tel != null && smsUser.tel.length() !=0) {
            ps.setString(n++,"%"+smsUser.tel+"%");
        }
        if(smsUser.groupId != null && smsUser.groupId.length() !=0) {
            ps.setString(n++,smsUser.groupId);
        }
        return n;
    }
    /**
     * Get records count.
     * @param smsUser
     * @return
     */
    public int getSmsUserCount(SmsUser smsUser) {
        String sqlCon = setSmsUserSql(smsUser);
        String sql = "select count(rownum) recordCount from "+JWebConstant.DB_TABLE_PREFIX+"SMS_USER " +(sqlCon.length()==0?"":" where "+ sqlCon);
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            int n = 1;
            setSmsUserParameter(ps,smsUser,n);
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
     * @param smsUser
     * @param page
     * @param rows
     * @param sort
     * @param order
     * @return
     * @throws Exception
     */
    public List getSmsUserList(SmsUser smsUser,int page,int rows,String sort,String order) throws Exception{
        sort = Tools.paraNameToDBColumnName(sort);
        String sqlCon = setSmsUserSql(smsUser);
        String sortOrder = sort == null || sort.length()==0?" order by USER_ID desc":(" order by " + sort +("desc".equals(order)?" desc ":" asc "));
        String sql = "select * from (" +
                "  select rownum rowno,tmp1.* from (" +
                "   select tmp.*  from " +
                	" (select su.*,sug.name GROUP_NAME from "+JWebConstant.DB_TABLE_PREFIX+"SMS_USER su left join "
                		+JWebConstant.DB_TABLE_PREFIX+"SMS_USER_GROUP sug on SU.GROUP_ID = sug.id) tmp " 
                	+(sqlCon.length()==0?"":" where "+ sqlCon) +sortOrder+
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
            setSmsUserParameter(ps,smsUser,n);
            rs = ps.executeQuery();
            while(rs.next()){
                list.add(getSmsUserValue(rs));
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
     * remove SmsUser
     * @param id
     * @throws Exception     
     */
    public void removeSmsUser(String id) throws Exception {
        String sql = "delete from "+JWebConstant.DB_TABLE_PREFIX+"SMS_USER where USER_ID=?";
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
     * update SmsUser
     * @param smsUser
     * @throws Exception
     */
    public void updateSmsUser(SmsUser smsUser) throws Exception {
        String sqlTmp = "";
        if(smsUser.userId != null)sqlTmp = sqlTmp + " USER_ID=?,";
        if(smsUser.name != null)sqlTmp = sqlTmp + " NAME=?,";
        if(smsUser.tel != null)sqlTmp = sqlTmp + " TEL=?,";
        if(smsUser.groupId != null)sqlTmp = sqlTmp + " GROUP_ID=?,";
        if(smsUser.remark != null)sqlTmp = sqlTmp + " REMARK=?,";
        if(smsUser.createId != null)sqlTmp = sqlTmp + " CREATE_ID=?,";
        if(smsUser.createTime != null)sqlTmp = sqlTmp + " CREATE_TIME=to_date(?,'yyyy-mm-dd hh24:mi:ss'),";
        if(sqlTmp.length()==0)return;
        String sql = "update "+JWebConstant.DB_TABLE_PREFIX+"SMS_USER "+        
              "set " + 
              sqlTmp.substring(0,sqlTmp.length()-1) + 
            " where USER_ID=?"; 
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            int n=1;
            if(smsUser.userId != null)ps.setString(n++,smsUser.userId);
            if(smsUser.name != null)ps.setString(n++,smsUser.name);
            if(smsUser.tel != null)ps.setString(n++,smsUser.tel);
            if(smsUser.groupId != null)ps.setString(n++,smsUser.groupId);
            if(smsUser.remark != null)ps.setString(n++,smsUser.remark);
            if(smsUser.createId != null)ps.setString(n++,smsUser.createId);
            if(smsUser.createTime != null)ps.setString(n++,new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(smsUser.createTime));
            ps.setString(n++,smsUser.userId);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }finally {
            close(null, ps, con);
        }
    }

}