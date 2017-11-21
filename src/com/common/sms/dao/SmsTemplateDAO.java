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
 * Table SMS_TEMPLATE DAO. 
 * @author Administrator
 *
 */
public class SmsTemplateDAO  extends BaseDAO {
    private static final Log log = LogFactory.getLog(SmsTemplateDAO.class);
    public static synchronized SmsTemplate getSmsTemplateValue(ResultSet rs)throws SQLException {
        SmsTemplate smsTemplate = new SmsTemplate();
        smsTemplate.id = rs.getString("ID");
        smsTemplate.name = rs.getString("NAME");
        smsTemplate.content = rs.getString("CONTENT");
        smsTemplate.remark = rs.getString("REMARK");
        smsTemplate.modifyId = rs.getString("MODIFY_ID");
        smsTemplate.modifyTime = rs.getTimestamp("MODIFY_TIME");
        smsTemplate.createId = rs.getString("CREATE_ID");
        smsTemplate.createTime = rs.getTimestamp("CREATE_TIME");
        return smsTemplate;
    }
    public String addSmsTemplate(SmsTemplate smsTemplate) throws Exception {
        String sql = "insert into "+JWebConstant.DB_TABLE_PREFIX+"SMS_TEMPLATE("+
            "ID," + 
            "NAME," + 
            "CONTENT," + 
            "REMARK," + 
            "MODIFY_ID," + 
            "MODIFY_TIME," + 
            "CREATE_ID," + 
            "CREATE_TIME)values(?,?,?,?,?,sysdate,?,sysdate)";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            int n=1;
            ps.setString(n++,Tools.getUniqueIdentify());
            ps.setString(n++,smsTemplate.name);
            ps.setString(n++,smsTemplate.content);
            ps.setString(n++,smsTemplate.remark);
            ps.setString(n++,smsTemplate.modifyId);
            ps.setString(n++,smsTemplate.createId);
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
        String sql = "select * from "+JWebConstant.DB_TABLE_PREFIX+"SMS_TEMPLATE";
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
                list.add(getSmsTemplateValue(rs));
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
     * @param smsTemplate
     * @return
     */
    private String setSmsTemplateSql(SmsTemplate smsTemplate) {
        StringBuffer sql = new StringBuffer();
        
        if(smsTemplate.name != null && smsTemplate.name.length() !=0) {
            sql.append(" NAME like ? and ");
        }
        if(smsTemplate.content != null && smsTemplate.content.length() !=0) {
            sql.append(" CONTENT like ? and ");
        }
        String tmp = sql.toString();
        if(tmp.length()>0)tmp=tmp.substring(0,tmp.lastIndexOf(" and "));
        return tmp;
    }
    /**
     * Set query parameter.
     * @param ps
     * @param smsTemplate
     * @param n
     * @return
     * @throws SQLException
     */
    private int setSmsTemplateParameter(PreparedStatement ps,SmsTemplate smsTemplate,int n)throws SQLException {
        if(smsTemplate.name != null && smsTemplate.name.length() !=0) {
            ps.setString(n++,"%"+smsTemplate.name+"%");
        }
        if(smsTemplate.content != null && smsTemplate.content.length() !=0) {
            ps.setString(n++,"%"+smsTemplate.content+"%");
        }
        return n;
    }
    /**
     * Get records count.
     * @param smsTemplate
     * @return
     */
    public int getSmsTemplateCount(SmsTemplate smsTemplate) {
        String sqlCon = setSmsTemplateSql(smsTemplate);
        String sql = "select count(rownum) recordCount from "+JWebConstant.DB_TABLE_PREFIX+"SMS_TEMPLATE " +(sqlCon.length()==0?"":" where "+ sqlCon);
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            int n = 1;
            setSmsTemplateParameter(ps,smsTemplate,n);
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
     * @param smsTemplate
     * @param page
     * @param rows
     * @param sort
     * @param order
     * @return
     * @throws Exception
     */
    public List getSmsTemplateList(SmsTemplate smsTemplate,int page,int rows,String sort,String order) throws Exception{
        sort = Tools.paraNameToDBColumnName(sort);
        String sqlCon = setSmsTemplateSql(smsTemplate);
        String sortOrder = sort == null || sort.length()==0?" order by ID desc":(" order by " + sort +("desc".equals(order)?" desc ":" asc "));
        String sql = "select * from (" +
                "  select rownum rowno,tmp1.* from (" +
                "   select tmp.*  from "+JWebConstant.DB_TABLE_PREFIX+"SMS_TEMPLATE tmp " +(sqlCon.length()==0?"":" where "+ sqlCon) +sortOrder+
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
            setSmsTemplateParameter(ps,smsTemplate,n);
            rs = ps.executeQuery();
            while(rs.next()){
                list.add(getSmsTemplateValue(rs));
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
     * remove SmsTemplate
     * @param id
     * @throws Exception     
     */
    public void removeSmsTemplate(String id) throws Exception {
        String sql = "delete from "+JWebConstant.DB_TABLE_PREFIX+"SMS_TEMPLATE where ID=?";
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
     * update SmsTemplate
     * @param smsTemplate
     * @throws Exception
     */
    public void updateSmsTemplate(SmsTemplate smsTemplate) throws Exception {
        String sql = "update "+JWebConstant.DB_TABLE_PREFIX+"SMS_TEMPLATE set NAME=?,CONTENT=?,REMARK=?,MODIFY_ID=?,MODIFY_TIME=sysdate where ID=?"; 
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            int n=1;
            ps.setString(n++,smsTemplate.name);
            ps.setString(n++,smsTemplate.content);
            ps.setString(n++,smsTemplate.remark);
            ps.setString(n++,smsTemplate.modifyId);
            ps.setString(n++,smsTemplate.id);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }finally {
            close(null, ps, con);
        }
    }

}