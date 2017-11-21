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
 * Table PAY_NOTIFY_FAIL_URL DAO. 
 * @author Administrator
 *
 */
public class PayNotifyFailUrlDAO  extends BaseDAO {
    private static final Log log = LogFactory.getLog(PayNotifyFailUrlDAO.class);
    public static synchronized PayNotifyFailUrl getPayNotifyFailUrlValue(ResultSet rs)throws SQLException {
        PayNotifyFailUrl payNotifyFailUrl = new PayNotifyFailUrl();
        payNotifyFailUrl.id = rs.getString("ID");
        payNotifyFailUrl.merchantNo = rs.getString("MERCHANT_NO");
        payNotifyFailUrl.url = rs.getString("URL");
        payNotifyFailUrl.errorMsg = rs.getString("ERROR_MSG");
        payNotifyFailUrl.createTime = rs.getTimestamp("CREATE_TIME");
        return payNotifyFailUrl;
    }
    public String addPayNotifyFailUrl(PayNotifyFailUrl payNotifyFailUrl) throws Exception {
        String sql = "insert into PAY_NOTIFY_FAIL_URL("+
            "ID," + 
            "MERCHANT_NO," + 
            "URL," + 
            "ERROR_MSG," + 
            "CREATE_TIME)values(?,?,?,?,to_date(?,'yyyy-mm-dd hh24:mi:ss'))";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            int n=1;
            ps.setString(n++,payNotifyFailUrl.id);
            ps.setString(n++,payNotifyFailUrl.merchantNo);
            ps.setString(n++,payNotifyFailUrl.url);
            ps.setString(n++,payNotifyFailUrl.errorMsg);
            ps.setString(n++,new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payNotifyFailUrl.createTime));
            ps.executeUpdate();
            return "";
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }finally {
            close(null, ps, con);
        }
    }
    /**
     * Set query condition sql.
     * @param payNotifyFailUrl
     * @return
     */
    private String setPayNotifyFailUrlSql(PayNotifyFailUrl payNotifyFailUrl) {
        StringBuffer sql = new StringBuffer();
        
        if(payNotifyFailUrl.merchantNo != null && payNotifyFailUrl.merchantNo.length() !=0) {
            sql.append(" MERCHANT_NO = ? and ");
        }
        if(payNotifyFailUrl.createTime != null) {
            sql.append(" CREATE_TIME >= to_date(?,'yyyy-mm-dd hh24:mi:ss') and ");
        }
        if(payNotifyFailUrl.createEndTime != null) {
            sql.append(" CREATE_TIME <= to_date(?,'yyyy-mm-dd hh24:mi:ss') and ");
        }
        String tmp = sql.toString();
        if(tmp.length()>0)tmp=tmp.substring(0,tmp.lastIndexOf(" and "));
        return tmp;
    }
    /**
     * Set query parameter.
     * @param ps
     * @param payNotifyFailUrl
     * @param n
     * @return
     * @throws SQLException
     */
    private int setPayNotifyFailUrlParameter(PreparedStatement ps,PayNotifyFailUrl payNotifyFailUrl,int n)throws SQLException {
        if(payNotifyFailUrl.merchantNo != null && payNotifyFailUrl.merchantNo.length() !=0) {
            ps.setString(n++,payNotifyFailUrl.merchantNo);
        }
        if(payNotifyFailUrl.createTime != null) {
            ps.setString(
					n++,
					new java.text.SimpleDateFormat("yyyy-MM-dd")
							.format(payNotifyFailUrl.createTime) + " 00:00:00");
        }
        if(payNotifyFailUrl.createEndTime != null) {
            ps.setString(
					n++,
					new java.text.SimpleDateFormat("yyyy-MM-dd")
							.format(payNotifyFailUrl.createEndTime) + " 23:59:59");
        }
        return n;
    }
    /**
     * Get records count.
     * @param payNotifyFailUrl
     * @return
     */
    public int getPayNotifyFailUrlCount(PayNotifyFailUrl payNotifyFailUrl) {
        String sqlCon = setPayNotifyFailUrlSql(payNotifyFailUrl);
        String sql = "select count(rownum) recordCount from PAY_NOTIFY_FAIL_URL " +(sqlCon.length()==0?"":" where "+ sqlCon);
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            int n = 1;
            setPayNotifyFailUrlParameter(ps,payNotifyFailUrl,n);
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
     * @param payNotifyFailUrl
     * @param page
     * @param rows
     * @param sort
     * @param order
     * @return
     * @throws Exception
     */
    public List getPayNotifyFailUrlList(PayNotifyFailUrl payNotifyFailUrl,int page,int rows,String sort,String order) throws Exception{
        sort = Tools.paraNameToDBColumnName(sort);
        String sqlCon = setPayNotifyFailUrlSql(payNotifyFailUrl);
        String sortOrder = sort == null || sort.length()==0?" order by ID desc":(" order by " + sort +("desc".equals(order)?" desc ":" asc "));
        String sql = "select * from (" +
                "  select rownum rowno,tmp1.* from (" +
                "   select tmp.*  from PAY_NOTIFY_FAIL_URL tmp " +(sqlCon.length()==0?"":" where "+ sqlCon) +sortOrder+
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
            setPayNotifyFailUrlParameter(ps,payNotifyFailUrl,n);
            rs = ps.executeQuery();
            while(rs.next()){
                list.add(getPayNotifyFailUrlValue(rs));
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