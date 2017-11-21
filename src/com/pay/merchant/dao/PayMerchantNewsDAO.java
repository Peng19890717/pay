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
 * Table PAY_MERCHANT_NEWS DAO. 
 * @author Administrator
 *
 */
public class PayMerchantNewsDAO  extends BaseDAO {
    private static final Log log = LogFactory.getLog(PayMerchantNewsDAO.class);
    public static synchronized PayMerchantNews getPayMerchantNewsValue(ResultSet rs)throws SQLException {
        PayMerchantNews payMerchantNews = new PayMerchantNews();
        payMerchantNews.id = rs.getString("ID");
        payMerchantNews.type = rs.getString("TYPE");
        payMerchantNews.title = rs.getString("TITLE");
        payMerchantNews.content = rs.getString("CONTENT");
        payMerchantNews.optTime = rs.getTimestamp("OPT_TIME");
        payMerchantNews.optUserId = rs.getString("OPT_USER_ID");
        payMerchantNews.flag2 = rs.getString("FLAG");
        return payMerchantNews;
    }
    public String addPayMerchantNews(PayMerchantNews payMerchantNews) throws Exception {
        String sql = "insert into PAY_MERCHANT_NEWS("+
            "ID," + 
            "TYPE," + 
            "TITLE," + 
            "CONTENT," + 
            "OPT_TIME," + 
            "OPT_USER_ID," + 
            "FLAG)values(?,?,?,?,to_date(?,'yyyy-mm-dd hh24:mi:ss'),?,?)";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            int n=1;
            ps.setString(n++,payMerchantNews.id);
            ps.setString(n++,payMerchantNews.type);
            ps.setString(n++,payMerchantNews.title);
            ps.setString(n++,payMerchantNews.content);
            ps.setString(n++,new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payMerchantNews.optTime));
            ps.setString(n++,payMerchantNews.optUserId);
            ps.setString(n++,payMerchantNews.flag2);
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
        String sql = "select * from PAY_MERCHANT_NEWS";
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
                list.add(getPayMerchantNewsValue(rs));
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
     * @param payMerchantNews
     * @return
     */
    private String setPayMerchantNewsSql(PayMerchantNews payMerchantNews) {
        StringBuffer sql = new StringBuffer();
        
        if(payMerchantNews.type != null && payMerchantNews.type.length() !=0) {
            sql.append(" TYPE = ? and ");
        }
        if(payMerchantNews.optTimeStart != null){
        	sql.append(" OPT_TIME >= to_date(?,'yyyy-mm-dd hh24:mi:ss') and ");
        }
        if(payMerchantNews.optTimeEnd != null){
        	sql.append(" OPT_TIME <= to_date(?,'yyyy-mm-dd hh24:mi:ss') and ");
        }
        if(payMerchantNews.flag2 != null && payMerchantNews.flag2.length() !=0) {
            sql.append(" FLAG = ? and ");
        }
        String tmp = sql.toString();
        if(tmp.length()>0)tmp=tmp.substring(0,tmp.lastIndexOf(" and "));
        return tmp;
    }
    /**
     * Set query parameter.
     * @param ps
     * @param payMerchantNews
     * @param n
     * @return
     * @throws SQLException
     */
    private int setPayMerchantNewsParameter(PreparedStatement ps,PayMerchantNews payMerchantNews,int n)throws SQLException {
        if(payMerchantNews.type != null && payMerchantNews.type.length() !=0) {
            ps.setString(n++,payMerchantNews.type);
        }
        if(payMerchantNews.optTimeStart != null){
        	ps.setString(n++, new java.text.SimpleDateFormat("yyyy-MM-dd").format(payMerchantNews.optTimeStart)+" 00:00:00");
        }
        if(payMerchantNews.optTimeEnd != null){
        	ps.setString(n++, new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payMerchantNews.optTimeEnd)+" 23:59:59");
        }
        if(payMerchantNews.flag2 != null && payMerchantNews.flag2.length() !=0) {
            ps.setString(n++,payMerchantNews.flag2);
        }
        return n;
    }
    /**
     * Get records count.
     * @param payMerchantNews
     * @return
     */
    public int getPayMerchantNewsCount(PayMerchantNews payMerchantNews) {
        String sqlCon = setPayMerchantNewsSql(payMerchantNews);
        String sql = "select count(rownum) recordCount from PAY_MERCHANT_NEWS " +(sqlCon.length()==0?"":" where "+ sqlCon);
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            int n = 1;
            setPayMerchantNewsParameter(ps,payMerchantNews,n);
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
     * @param payMerchantNews
     * @param page
     * @param rows
     * @param sort
     * @param order
     * @return
     * @throws Exception
     */
    public List getPayMerchantNewsList(PayMerchantNews payMerchantNews,int page,int rows,String sort,String order) throws Exception{
        sort = Tools.paraNameToDBColumnName(sort);
        String sqlCon = setPayMerchantNewsSql(payMerchantNews);
        String sortOrder = sort == null || sort.length()==0?" order by ID desc":(" order by " + sort +("desc".equals(order)?" desc ":" asc "));
        String sql = "select * from (" +
                "  select rownum rowno,tmp1.* from (" +
                "   select tmp.*  from PAY_MERCHANT_NEWS tmp " +(sqlCon.length()==0?"":" where "+ sqlCon) +sortOrder+
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
            setPayMerchantNewsParameter(ps,payMerchantNews,n);
            rs = ps.executeQuery();
            while(rs.next()){
                list.add(getPayMerchantNewsValue(rs));
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
     * remove PayMerchantNews
     * @param id
     * @throws Exception     
     */
    public void removePayMerchantNews(String id) throws Exception {
        String sql = "delete from PAY_MERCHANT_NEWS where ID=?";
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
     * detail PayMerchantNews
     * @param id
     * @return PayMerchantNews
     * @throws Exception
     */
    public PayMerchantNews detailPayMerchantNews(String id) throws Exception {
        String sql = "select * from PAY_MERCHANT_NEWS where ID=?";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            ps.setString(1,id);
            rs = ps.executeQuery();
            if(rs.next())return getPayMerchantNewsValue(rs); 
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(rs, ps, con);
        }
        return null;
    }
    /**
     * update PayMerchantNews
     * @param payMerchantNews
     * @throws Exception
     */
    public void updatePayMerchantNews(PayMerchantNews payMerchantNews) throws Exception {
        String sqlTmp = "";
        if(payMerchantNews.id != null)sqlTmp = sqlTmp + " ID=?,";
        if(payMerchantNews.type != null)sqlTmp = sqlTmp + " TYPE=?,";
        if(payMerchantNews.title != null)sqlTmp = sqlTmp + " TITLE=?,";
        if(payMerchantNews.content != null)sqlTmp = sqlTmp + " CONTENT=?,";
        if(payMerchantNews.optTime != null)sqlTmp = sqlTmp + " OPT_TIME=to_date(?,'yyyy-mm-dd hh24:mi:ss'),";
        if(payMerchantNews.optUserId != null)sqlTmp = sqlTmp + " OPT_USER_ID=?,";
        if(payMerchantNews.flag2 != null)sqlTmp = sqlTmp + " FLAG=?,";
        if(sqlTmp.length()==0)return;
        String sql = "update PAY_MERCHANT_NEWS "+        
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
            if(payMerchantNews.id != null)ps.setString(n++,payMerchantNews.id);
            if(payMerchantNews.type != null)ps.setString(n++,payMerchantNews.type);
            if(payMerchantNews.title != null)ps.setString(n++,payMerchantNews.title);
            if(payMerchantNews.content != null)ps.setString(n++,payMerchantNews.content);
            if(payMerchantNews.optTime != null)ps.setString(n++,new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payMerchantNews.optTime));
            if(payMerchantNews.optUserId != null)ps.setString(n++,payMerchantNews.optUserId);
            if(payMerchantNews.flag2 != null)ps.setString(n++,payMerchantNews.flag2);
            ps.setString(n++,payMerchantNews.id);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }finally {
            close(null, ps, con);
        }
    }

}