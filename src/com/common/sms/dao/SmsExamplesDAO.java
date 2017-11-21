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
 * Table SMS_EXAMPLES DAO. 
 * @author Administrator
 *
 */
public class SmsExamplesDAO  extends BaseDAO {
    private static final Log log = LogFactory.getLog(SmsExamplesDAO.class);
    public static synchronized SmsExamples getSmsExamplesValue(ResultSet rs)throws SQLException {
        SmsExamples smsExamples = new SmsExamples();
        smsExamples.examplesId = rs.getString("EXAMPLES_ID");
        smsExamples.content = rs.getString("CONTENT");
        smsExamples.groupId = rs.getString("GROUP_ID");
        try {smsExamples.groupName = rs.getString("GROUP_NAME");} catch (Exception e) {}
        smsExamples.createId = rs.getString("CREATE_ID");
        smsExamples.createTime = rs.getTimestamp("CREATE_TIME");
        return smsExamples;
    }
    public String addSmsExamples(SmsExamples smsExamples) throws Exception {
        String sql = "insert into "+JWebConstant.DB_TABLE_PREFIX+"SMS_EXAMPLES("+
            "EXAMPLES_ID," + 
            "CONTENT," + 
            "GROUP_ID," + 
            "CREATE_ID," + 
            "CREATE_TIME)values(?,?,?,?,sysdate)";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            int n=1;
            ps.setString(n++,Tools.getUniqueIdentify());
            ps.setString(n++,smsExamples.content);
            ps.setString(n++,smsExamples.groupId);
            ps.setString(n++,smsExamples.createId);
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
        String sql = "select * from "+JWebConstant.DB_TABLE_PREFIX+"SMS_EXAMPLES";
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
                list.add(getSmsExamplesValue(rs));
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
     * @param smsExamples
     * @return
     */
    private String setSmsExamplesSql(SmsExamples smsExamples) {
        StringBuffer sql = new StringBuffer();
        
        if(smsExamples.content != null && smsExamples.content.length() !=0) {
            sql.append(" CONTENT like ? and ");
        }
        if(smsExamples.groupId != null && smsExamples.groupId.length() !=0) {
            sql.append(" GROUP_ID = ? and ");
        }
        String tmp = sql.toString();
        if(tmp.length()>0)tmp=tmp.substring(0,tmp.lastIndexOf(" and "));
        return tmp;
    }
    /**
     * Set query parameter.
     * @param ps
     * @param smsExamples
     * @param n
     * @return
     * @throws SQLException
     */
    private int setSmsExamplesParameter(PreparedStatement ps,SmsExamples smsExamples,int n)throws SQLException {
        if(smsExamples.content != null && smsExamples.content.length() !=0) {
            ps.setString(n++,"%"+smsExamples.content+"%");
        }
        if(smsExamples.groupId != null && smsExamples.groupId.length() !=0) {
            ps.setString(n++,smsExamples.groupId);
        }
        return n;
    }
    /**
     * Get records count.
     * @param smsExamples
     * @return
     */
    public int getSmsExamplesCount(SmsExamples smsExamples) {
        String sqlCon = setSmsExamplesSql(smsExamples);
        String sql = "select count(rownum) recordCount from "+JWebConstant.DB_TABLE_PREFIX+"SMS_EXAMPLES " +(sqlCon.length()==0?"":" where "+ sqlCon);
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            int n = 1;
            setSmsExamplesParameter(ps,smsExamples,n);
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
     * @param smsExamples
     * @param page
     * @param rows
     * @param sort
     * @param order
     * @return
     * @throws Exception
     */
    public List getSmsExamplesList(SmsExamples smsExamples,int page,int rows,String sort,String order) throws Exception{
        sort = Tools.paraNameToDBColumnName(sort);
        String sqlCon = setSmsExamplesSql(smsExamples);
        String sortOrder = sort == null || sort.length()==0?" order by EXAMPLES_ID desc":(" order by " + sort +("desc".equals(order)?" desc ":" asc "));
        String sql = "select * from (" +
                "  select rownum rowno,tmp1.* from (" +
                "   select tmp.*  from (select e.*,eg.name GROUP_NAME from "+JWebConstant.DB_TABLE_PREFIX+"SMS_EXAMPLES e left join "
                		+JWebConstant.DB_TABLE_PREFIX+"SMS_EXAMPLES_GROUP eg on e.group_id=eg.id) tmp " +(sqlCon.length()==0?"":" where "+ sqlCon) +sortOrder+
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
            setSmsExamplesParameter(ps,smsExamples,n);
            rs = ps.executeQuery();
            while(rs.next()){
                list.add(getSmsExamplesValue(rs));
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
     * remove SmsExamples
     * @param id
     * @throws Exception     
     */
    public void removeSmsExamples(String id) throws Exception {
        String sql = "delete from "+JWebConstant.DB_TABLE_PREFIX+"SMS_EXAMPLES where EXAMPLES_ID=?";
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
     * update SmsExamples
     * @param smsExamples
     * @throws Exception
     */
    public void updateSmsExamples(SmsExamples smsExamples) throws Exception {
        String sql = "update "+JWebConstant.DB_TABLE_PREFIX+"SMS_EXAMPLES set CONTENT=?,GROUP_ID=? where EXAMPLES_ID=?"; 
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            int n=1;
            if(smsExamples.content != null)ps.setString(n++,smsExamples.content);
            if(smsExamples.groupId != null)ps.setString(n++,smsExamples.groupId);
            ps.setString(n++,smsExamples.examplesId);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }finally {
            close(null, ps, con);
        }
    }

}