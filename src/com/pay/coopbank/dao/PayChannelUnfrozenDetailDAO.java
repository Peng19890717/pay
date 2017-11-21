package com.pay.coopbank.dao;

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
 * Table PAY_CHANNEL_UNFROZEN_DETAIL DAO. 
 * @author Administrator
 *
 */
public class PayChannelUnfrozenDetailDAO  extends BaseDAO {
    private static final Log log = LogFactory.getLog(PayChannelUnfrozenDetailDAO.class);
    public static synchronized PayChannelUnfrozenDetail getPayChannelUnfrozenDetailValue(ResultSet rs)throws SQLException {
        PayChannelUnfrozenDetail payChannelUnfrozenDetail = new PayChannelUnfrozenDetail();
        payChannelUnfrozenDetail.id = rs.getString("ID");
        payChannelUnfrozenDetail.amt = rs.getLong("AMT");
        payChannelUnfrozenDetail.frozenId = rs.getString("FROZEN_ID");
        payChannelUnfrozenDetail.createTime = rs.getTimestamp("CREATE_TIME");
        payChannelUnfrozenDetail.remark = rs.getString("REMARK");
        payChannelUnfrozenDetail.optId = rs.getString("OPT_ID");
        return payChannelUnfrozenDetail;
    }
    public String addPayChannelUnfrozenDetail(PayChannelUnfrozenDetail payChannelUnfrozenDetail) throws Exception {
        String sql = "insert into pay_channel_unfrozen_detail("+
            "ID," + 
            "AMT," + 
            "FROZEN_ID," + 
            "CREATE_TIME," + 
            "REMARK," + 
            "OPT_ID)values(?,?,?,to_date(?,'yyyy-mm-dd hh24:mi:ss'),?,?)";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            int n=1;
            ps.setString(n++,payChannelUnfrozenDetail.id);
            ps.setLong(n++,payChannelUnfrozenDetail.amt);
            ps.setString(n++,payChannelUnfrozenDetail.frozenId);
            ps.setString(n++,new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payChannelUnfrozenDetail.createTime));
            ps.setString(n++,payChannelUnfrozenDetail.remark);
            ps.setString(n++,payChannelUnfrozenDetail.optId);
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
        String sql = "select * from pay_channel_unfrozen_detail";
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
                list.add(getPayChannelUnfrozenDetailValue(rs));
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
     * @param payChannelUnfrozenDetail
     * @return
     */
    private String setPayChannelUnfrozenDetailSql(PayChannelUnfrozenDetail payChannelUnfrozenDetail) {
        StringBuffer sql = new StringBuffer();
        
        if(payChannelUnfrozenDetail.frozenId != null && payChannelUnfrozenDetail.frozenId.length() !=0) {
            sql.append(" FROZEN_ID = ? and ");
        }
        if(payChannelUnfrozenDetail.createTime != null) {
            sql.append(" CREATE_TIME = to_date(?,'yyyy-mm-dd hh24:mi:ss') and ");
        }
        String tmp = sql.toString();
        if(tmp.length()>0)tmp=tmp.substring(0,tmp.lastIndexOf(" and "));
        return tmp;
    }
    /**
     * Set query parameter.
     * @param ps
     * @param payChannelUnfrozenDetail
     * @param n
     * @return
     * @throws SQLException
     */
    private int setPayChannelUnfrozenDetailParameter(PreparedStatement ps,PayChannelUnfrozenDetail payChannelUnfrozenDetail,int n)throws SQLException {
        if(payChannelUnfrozenDetail.frozenId != null && payChannelUnfrozenDetail.frozenId.length() !=0) {
            ps.setString(n++,payChannelUnfrozenDetail.frozenId);
        }
        if(payChannelUnfrozenDetail.createTime != null) {
            ps.setString(n++, new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payChannelUnfrozenDetail.createTime));
        }
        return n;
    }
    /**
     * Get records count.
     * @param payChannelUnfrozenDetail
     * @return
     */
    public int getPayChannelUnfrozenDetailCount(PayChannelUnfrozenDetail payChannelUnfrozenDetail) {
        String sqlCon = setPayChannelUnfrozenDetailSql(payChannelUnfrozenDetail);
        String sql = "select count(rownum) recordCount from pay_channel_unfrozen_detail " +(sqlCon.length()==0?"":" where "+ sqlCon);
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            int n = 1;
            setPayChannelUnfrozenDetailParameter(ps,payChannelUnfrozenDetail,n);
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
     * @param payChannelUnfrozenDetail
     * @param page
     * @param rows
     * @param sort
     * @param order
     * @return
     * @throws Exception
     */
    public List getPayChannelUnfrozenDetailList(PayChannelUnfrozenDetail payChannelUnfrozenDetail,int page,int rows,String sort,String order) throws Exception{
        sort = Tools.paraNameToDBColumnName(sort);
        String sqlCon = setPayChannelUnfrozenDetailSql(payChannelUnfrozenDetail);
        String sortOrder = sort == null || sort.length()==0?" order by ID desc":(" order by " + sort +("desc".equals(order)?" desc ":" asc "));
        String sql = "select * from (" +
                "  select rownum rowno,tmp1.* from (" +
                "   select tmp.*  from pay_channel_unfrozen_detail tmp " +(sqlCon.length()==0?"":" where "+ sqlCon) +sortOrder+
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
            setPayChannelUnfrozenDetailParameter(ps,payChannelUnfrozenDetail,n);
            rs = ps.executeQuery();
            while(rs.next()){
                list.add(getPayChannelUnfrozenDetailValue(rs));
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
     * remove PayChannelUnfrozenDetail
     * @param id
     * @throws Exception     
     */
    public void removePayChannelUnfrozenDetail(String id) throws Exception {
        String sql = "delete from pay_channel_unfrozen_detail where ID=?";
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
     * detail PayChannelUnfrozenDetail
     * @param id
     * @return PayChannelUnfrozenDetail
     * @throws Exception
     */
    public PayChannelUnfrozenDetail detailPayChannelUnfrozenDetail(String id) throws Exception {
        String sql = "select * from pay_channel_unfrozen_detail where ID=?";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            ps.setString(1,id);
            rs = ps.executeQuery();
            if(rs.next())return getPayChannelUnfrozenDetailValue(rs); 
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(rs, ps, con);
        }
        return null;
    }
    /**
     * update PayChannelUnfrozenDetail
     * @param payChannelUnfrozenDetail
     * @throws Exception
     */
    public void updatePayChannelUnfrozenDetail(PayChannelUnfrozenDetail payChannelUnfrozenDetail) throws Exception {
        String sqlTmp = "";
        if(payChannelUnfrozenDetail.id != null)sqlTmp = sqlTmp + " ID=?,";
        if(payChannelUnfrozenDetail.amt != null)sqlTmp = sqlTmp + " AMT=?,";
        if(payChannelUnfrozenDetail.frozenId != null)sqlTmp = sqlTmp + " FROZEN_ID=?,";
        if(payChannelUnfrozenDetail.createTime != null)sqlTmp = sqlTmp + " CREATE_TIME=to_date(?,'yyyy-mm-dd hh24:mi:ss'),";
        if(payChannelUnfrozenDetail.remark != null)sqlTmp = sqlTmp + " REMARK=?,";
        if(payChannelUnfrozenDetail.optId != null)sqlTmp = sqlTmp + " OPT_ID=?,";
        if(sqlTmp.length()==0)return;
        String sql = "update pay_channel_unfrozen_detail "+        
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
            if(payChannelUnfrozenDetail.id != null)ps.setString(n++,payChannelUnfrozenDetail.id);
            if(payChannelUnfrozenDetail.amt != null)ps.setLong(n++,payChannelUnfrozenDetail.amt);
            if(payChannelUnfrozenDetail.frozenId != null)ps.setString(n++,payChannelUnfrozenDetail.frozenId);
            if(payChannelUnfrozenDetail.createTime != null)ps.setString(n++,new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payChannelUnfrozenDetail.createTime));
            if(payChannelUnfrozenDetail.remark != null)ps.setString(n++,payChannelUnfrozenDetail.remark);
            if(payChannelUnfrozenDetail.optId != null)ps.setString(n++,payChannelUnfrozenDetail.optId);
            ps.setString(n++,payChannelUnfrozenDetail.id);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }finally {
            close(null, ps, con);
        }
    }

}