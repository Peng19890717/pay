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
 * Table SMS_EXAMPLES_GROUP DAO. 
 * @author Administrator
 *
 */
public class SmsExamplesGroupDAO  extends BaseDAO {
    private static final Log log = LogFactory.getLog(SmsExamplesGroupDAO.class);
    public static synchronized SmsExamplesGroup getSmsExamplesGroupValue(ResultSet rs)throws SQLException {
        SmsExamplesGroup smsExamplesGroup = new SmsExamplesGroup();
        smsExamplesGroup.id = rs.getString("ID");
        smsExamplesGroup.name = rs.getString("NAME");
        smsExamplesGroup.createId = rs.getString("CREATE_ID");
        smsExamplesGroup.createTime = rs.getTimestamp("CREATE_TIME");
        return smsExamplesGroup;
    }
    public String addSmsExamplesGroup(SmsExamplesGroup smsExamplesGroup) throws Exception {
        String sql = "insert into "+JWebConstant.DB_TABLE_PREFIX+"SMS_EXAMPLES_GROUP("+
            "ID," + 
            "NAME," + 
            "CREATE_ID," + 
            "CREATE_TIME)values(?,?,?,sysdate)";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            int n=1;
            ps.setString(n++,Tools.getUniqueIdentify());
            ps.setString(n++,smsExamplesGroup.name);
            ps.setString(n++,smsExamplesGroup.createId);
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
        String sql = "select * from "+JWebConstant.DB_TABLE_PREFIX+"SMS_EXAMPLES_GROUP";
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
                list.add(getSmsExamplesGroupValue(rs));
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
     * @param smsExamplesGroup
     * @return
     */
    private String setSmsExamplesGroupSql(SmsExamplesGroup smsExamplesGroup) {
        StringBuffer sql = new StringBuffer();
        
        if(smsExamplesGroup.name != null && smsExamplesGroup.name.length() !=0) {
            sql.append(" NAME like ? and ");
        }
        String tmp = sql.toString();
        if(tmp.length()>0)tmp=tmp.substring(0,tmp.lastIndexOf(" and "));
        return tmp;
    }
    /**
     * Set query parameter.
     * @param ps
     * @param smsExamplesGroup
     * @param n
     * @return
     * @throws SQLException
     */
    private int setSmsExamplesGroupParameter(PreparedStatement ps,SmsExamplesGroup smsExamplesGroup,int n)throws SQLException {
        if(smsExamplesGroup.name != null && smsExamplesGroup.name.length() !=0) {
            ps.setString(n++,"%"+smsExamplesGroup.name+"%");
        }
        return n;
    }
    /**
     * Get records count.
     * @param smsExamplesGroup
     * @return
     */
    public int getSmsExamplesGroupCount(SmsExamplesGroup smsExamplesGroup) {
        String sqlCon = setSmsExamplesGroupSql(smsExamplesGroup);
        String sql = "select count(rownum) recordCount from "+JWebConstant.DB_TABLE_PREFIX+"SMS_EXAMPLES_GROUP " 
        	+(sqlCon.length()==0?"":" where "+ sqlCon);
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            int n = 1;
            setSmsExamplesGroupParameter(ps,smsExamplesGroup,n);
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
     * @param smsExamplesGroup
     * @param page
     * @param rows
     * @param sort
     * @param order
     * @return
     * @throws Exception
     */
    public List getSmsExamplesGroupList(SmsExamplesGroup smsExamplesGroup,int page,int rows,String sort,String order) throws Exception{
        sort = Tools.paraNameToDBColumnName(sort);
        String sqlCon = setSmsExamplesGroupSql(smsExamplesGroup);
        String sortOrder = sort == null || sort.length()==0?" order by ID desc":(" order by " + sort +("desc".equals(order)?" desc ":" asc "));
        String sql = "select * from (" +
                "  select rownum rowno,tmp1.* from (" +
                "   select tmp.*  from "+JWebConstant.DB_TABLE_PREFIX+"SMS_EXAMPLES_GROUP tmp " +(sqlCon.length()==0?"":" where "+ sqlCon) +sortOrder+
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
            setSmsExamplesGroupParameter(ps,smsExamplesGroup,n);
            rs = ps.executeQuery();
            while(rs.next()){
                list.add(getSmsExamplesGroupValue(rs));
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
     * remove SmsExamplesGroup
     * @param id
     * @throws Exception     
     */
    public void removeSmsExamplesGroup(String id) throws Exception {
        String sql = "delete from "+JWebConstant.DB_TABLE_PREFIX+"SMS_EXAMPLES_GROUP where ID=?";
        String sql1 = "update "+JWebConstant.DB_TABLE_PREFIX+"SMS_EXAMPLES set GROUP_ID='' where GROUP_ID=?";
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
     * update SmsExamplesGroup
     * @param smsExamplesGroup
     * @throws Exception
     */
    public void updateSmsExamplesGroup(SmsExamplesGroup smsExamplesGroup) throws Exception {
        String sql = "update "+JWebConstant.DB_TABLE_PREFIX+"SMS_EXAMPLES_GROUP set NAME=? where ID=?"; 
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            int n=1;
            ps.setString(n++,smsExamplesGroup.name);
            ps.setString(n++,smsExamplesGroup.id);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }finally {
            close(null, ps, con);
        }
    }
    public List getAllSmsExamplesGroup() throws Exception {
		String sql = "select * from "+JWebConstant.DB_TABLE_PREFIX+"SMS_EXAMPLES_GROUP order by NAME";
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
		        list.add(getSmsExamplesGroupValue(rs));
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