package com.common.sms.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import util.JWebConstant;
import util.Tools;

import com.jweb.dao.BaseDAO;
/**
 * Table SMS_USER_GROUP DAO. 
 * @author Administrator
 *
 */
public class SmsUserGroupDAO  extends BaseDAO {
    private static final Log log = LogFactory.getLog(SmsUserGroupDAO.class);
    public static synchronized SmsUserGroup getSmsUserGroupValue(ResultSet rs)throws SQLException {
        SmsUserGroup smsUserGroup = new SmsUserGroup();
        smsUserGroup.id = rs.getString("ID");
        smsUserGroup.name = rs.getString("NAME");
        smsUserGroup.createId = rs.getString("CREATE_ID");
        smsUserGroup.createTime = rs.getTimestamp("CREATE_TIME");
        return smsUserGroup;
    }
    public String addSmsUserGroup(SmsUserGroup smsUserGroup) throws Exception {
        String sql = "insert into "+JWebConstant.DB_TABLE_PREFIX+"SMS_USER_GROUP("+
            "ID," + 
            "NAME," + 
            "CREATE_ID)values(?,?,?)";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            int n=1;
            ps.setString(n++,Tools.getUniqueIdentify());
            ps.setString(n++,smsUserGroup.name);
            ps.setString(n++,smsUserGroup.createId);
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
        String sql = "select * from "+JWebConstant.DB_TABLE_PREFIX+"SMS_USER_GROUP";
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
                list.add(getSmsUserGroupValue(rs));
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
     * @param smsUserGroup
     * @return
     */
    private String setSmsUserGroupSql(SmsUserGroup smsUserGroup) {
        StringBuffer sql = new StringBuffer();
        
        if(smsUserGroup.name != null && smsUserGroup.name.length() !=0) {
            sql.append(" NAME like ? and ");
        }
        String tmp = sql.toString();
        if(tmp.length()>0)tmp=tmp.substring(0,tmp.lastIndexOf(" and "));
        return tmp;
    }
    /**
     * Set query parameter.
     * @param ps
     * @param smsUserGroup
     * @param n
     * @return
     * @throws SQLException
     */
    private int setSmsUserGroupParameter(PreparedStatement ps,SmsUserGroup smsUserGroup,int n)throws SQLException {
        if(smsUserGroup.name != null && smsUserGroup.name.length() !=0) {
            ps.setString(n++,"%"+smsUserGroup.name+"%");
        }
        return n;
    }
    /**
     * Get records count.
     * @param smsUserGroup
     * @return
     */
    public int getSmsUserGroupCount(SmsUserGroup smsUserGroup) {
        String sqlCon = setSmsUserGroupSql(smsUserGroup);
        String sql = "select count(rownum) recordCount from "+JWebConstant.DB_TABLE_PREFIX+"SMS_USER_GROUP " +(sqlCon.length()==0?"":" where "+ sqlCon);
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            int n = 1;
            setSmsUserGroupParameter(ps,smsUserGroup,n);
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
     * @param smsUserGroup
     * @param page
     * @param rows
     * @param sort
     * @param order
     * @return
     * @throws Exception
     */
    public List getSmsUserGroupList(SmsUserGroup smsUserGroup,int page,int rows,String sort,String order) throws Exception{
        sort = Tools.paraNameToDBColumnName(sort);
        String sqlCon = setSmsUserGroupSql(smsUserGroup);
        String sortOrder = sort == null || sort.length()==0?" order by ID desc":(" order by " + sort +("desc".equals(order)?" desc ":" asc "));
        String sql = "select * from (" +
                "  select rownum rowno,tmp1.* from (" +
                "   select tmp.*  from "+JWebConstant.DB_TABLE_PREFIX+"SMS_USER_GROUP tmp " +(sqlCon.length()==0?"":" where "+ sqlCon) +sortOrder+
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
            setSmsUserGroupParameter(ps,smsUserGroup,n);
            rs = ps.executeQuery();
            while(rs.next()){
                list.add(getSmsUserGroupValue(rs));
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
     * remove SmsUserGroup
     * @param id
     * @throws Exception     
     */
    public void removeSmsUserGroup(String id) throws Exception {
        String sql = "delete from "+JWebConstant.DB_TABLE_PREFIX+"SMS_USER_GROUP where ID=?";
        String sql1 = "update "+JWebConstant.DB_TABLE_PREFIX+"SMS_USER set GROUP_ID='' where GROUP_ID=?";
        log.info(sql);
        log.info(sql1);
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            ps.setString(1,id);
            ps.executeUpdate();
            ps.close();
            ps = con.prepareStatement(sql1);
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
     * update SmsUserGroup
     * @param smsUserGroup
     * @throws Exception
     */
    public void updateSmsUserGroup(SmsUserGroup smsUserGroup) throws Exception {
        String sql = "update "+JWebConstant.DB_TABLE_PREFIX+"SMS_USER_GROUP set NAME=? where ID=?"; 
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            int n=1;
            ps.setString(n++,smsUserGroup.name);
            ps.setString(n++,smsUserGroup.id);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }finally {
            close(null, ps, con);
        }
    }
	public List getAllSmsUserGroup() throws Exception {
		String sql = "select * from "+JWebConstant.DB_TABLE_PREFIX+"SMS_USER_GROUP order by NAME";
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
		        list.add(getSmsUserGroupValue(rs));
		    }
		    return list;
		} catch (Exception e) {
		    e.printStackTrace();
		    throw e;
		} finally {
		    close(rs, ps, con);
		}
	}

}