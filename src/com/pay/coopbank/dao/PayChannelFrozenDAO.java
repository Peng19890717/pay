package com.pay.coopbank.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import util.Tools;

import com.jweb.dao.BaseDAO;
/**
 * Table PAY_CHANNEL_FROZEN DAO. 
 * @author Administrator
 *
 */
public class PayChannelFrozenDAO  extends BaseDAO {
    private static final Log log = LogFactory.getLog(PayChannelFrozenDAO.class);
    public static synchronized PayChannelFrozen getPayChannelFrozenValue(ResultSet rs)throws SQLException {
        PayChannelFrozen payChannelFrozen = new PayChannelFrozen();
        payChannelFrozen.id = rs.getString("ID");
        payChannelFrozen.channel = rs.getString("CHANNEL");
        payChannelFrozen.srcAmt = rs.getLong("SRC_AMT");
        payChannelFrozen.merNos = rs.getString("MER_NOS");
        try {payChannelFrozen.storeName = rs.getString("store_name");} catch (Exception e) {}
        payChannelFrozen.frozenTime = rs.getTimestamp("FROZEN_TIME");
        payChannelFrozen.orderTxamt = rs.getLong("ORDER_TXAMT");
        payChannelFrozen.orderIds = rs.getString("ORDER_IDS");
        payChannelFrozen.frozenDays = rs.getLong("FROZEN_DAYS");
        payChannelFrozen.status = rs.getString("STATUS");
        payChannelFrozen.curAmt = rs.getLong("CUR_AMT");
        payChannelFrozen.salemanId = rs.getString("SALEMAN_ID");
        payChannelFrozen.createTime = rs.getTimestamp("CREATE_TIME");
        payChannelFrozen.remark = rs.getString("REMARK");
        payChannelFrozen.optId = rs.getString("OPT_ID");
        return payChannelFrozen;
    }
    public String addPayChannelFrozen(PayChannelFrozen payChannelFrozen) throws Exception {
        String sql = "insert into pay_channel_frozen("+
            "ID," + 
            "CHANNEL," + 
            "SRC_AMT," + 
            "MER_NOS," + 
            "FROZEN_TIME," + 
            "ORDER_TXAMT," + 
            "ORDER_IDS," + 
            "FROZEN_DAYS," + 
            "STATUS," + 
            "CUR_AMT," + 
            "SALEMAN_ID," + 
            "CREATE_TIME," + 
            "REMARK," + 
            "OPT_ID)values(?,?,?,?,to_date(?,'yyyy-mm-dd hh24:mi:ss'),?,?,?,?,?,?,to_date(?,'yyyy-mm-dd hh24:mi:ss'),?,?)";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            int n=1;
            ps.setString(n++,payChannelFrozen.id);
            ps.setString(n++,payChannelFrozen.channel);
            ps.setLong(n++,payChannelFrozen.srcAmt);
            ps.setString(n++,payChannelFrozen.merNos);
            ps.setString(n++,new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payChannelFrozen.frozenTime));
            ps.setLong(n++,payChannelFrozen.orderTxamt);
            ps.setString(n++,payChannelFrozen.orderIds);
            ps.setLong(n++,payChannelFrozen.frozenDays);
            ps.setString(n++,payChannelFrozen.status);
            ps.setLong(n++,payChannelFrozen.curAmt);
            ps.setString(n++,payChannelFrozen.salemanId);
            ps.setString(n++,new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payChannelFrozen.createTime));
            ps.setString(n++,payChannelFrozen.remark);
            ps.setString(n++,payChannelFrozen.optId);
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
        String sql = "select * from pay_channel_frozen";
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
                list.add(getPayChannelFrozenValue(rs));
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
     * @param payChannelFrozen
     * @return
     */
    private String setPayChannelFrozenSql(PayChannelFrozen payChannelFrozen) {
        StringBuffer sql = new StringBuffer();
        
        if(payChannelFrozen.channel != null && payChannelFrozen.channel.length() !=0) {
            sql.append(" CHANNEL = ? and ");
        }
        if(payChannelFrozen.merNos != null && payChannelFrozen.merNos.length() !=0) {
            sql.append(" MER_NOS = ? and ");
        }
//        if(payChannelFrozen.frozenTime != null) {
//            sql.append(" FROZEN_TIME = to_date(?,'yyyy-mm-dd hh24:mi:ss') and ");
//        }
        if(payChannelFrozen.orderIds != null && payChannelFrozen.orderIds.length() !=0) {
            sql.append(" ORDER_IDS = ? and ");
        }
        if(payChannelFrozen.status != null && payChannelFrozen.status.length() !=0) {
            sql.append(" STATUS = ? and ");
        }
        if(payChannelFrozen.salemanId != null && payChannelFrozen.salemanId.length() !=0) {
            sql.append(" SALEMAN_ID = ? and ");
        }
        if(payChannelFrozen.frozenTimeStart != null) {
            sql.append(" FROZEN_TIME >=to_date(?,'yyyy-mm-dd hh24:mi:ss') and ");
        }
        if(payChannelFrozen.frozenTimeEnd != null) {
            sql.append(" FROZEN_TIME <= to_date(?,'yyyy-mm-dd hh24:mi:ss') and ");
        }
        String tmp = sql.toString();
        if(tmp.length()>0)tmp=tmp.substring(0,tmp.lastIndexOf(" and "));
        return tmp;
    }
    /**
     * Set query parameter.
     * @param ps
     * @param payChannelFrozen
     * @param n
     * @return
     * @throws SQLException
     */
    private int setPayChannelFrozenParameter(PreparedStatement ps,PayChannelFrozen payChannelFrozen,int n)throws SQLException {
        if(payChannelFrozen.channel != null && payChannelFrozen.channel.length() !=0) {
            ps.setString(n++,payChannelFrozen.channel);
        }
        if(payChannelFrozen.merNos != null && payChannelFrozen.merNos.length() !=0) {
            ps.setString(n++,payChannelFrozen.merNos);
        }
//        if(payChannelFrozen.frozenTime != null) {
//            ps.setString(n++, new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payChannelFrozen.frozenTime));
//        }
        if(payChannelFrozen.orderIds != null && payChannelFrozen.orderIds.length() !=0) {
            ps.setString(n++,payChannelFrozen.orderIds);
        }
        if(payChannelFrozen.status != null && payChannelFrozen.status.length() !=0) {
            ps.setString(n++,payChannelFrozen.status);
        }
        if(payChannelFrozen.salemanId != null && payChannelFrozen.salemanId.length() !=0) {
            ps.setString(n++,payChannelFrozen.salemanId);
        }
        if(payChannelFrozen.frozenTimeStart != null) {
            ps.setString(n++, new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payChannelFrozen.frozenTimeStart));
        }
        if(payChannelFrozen.frozenTimeEnd != null) {
            ps.setString(n++, new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payChannelFrozen.frozenTimeEnd));
        }
        return n;
    }
    /**
     * Get records count.
     * @param payChannelFrozen
     * @return
     */
    public int getPayChannelFrozenCount(PayChannelFrozen payChannelFrozen) {
        String sqlCon = setPayChannelFrozenSql(payChannelFrozen);
        String sql = "select count(rownum) recordCount from pay_channel_frozen " +(sqlCon.length()==0?"":" where "+ sqlCon);
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            int n = 1;
            setPayChannelFrozenParameter(ps,payChannelFrozen,n);
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
     * @param payChannelFrozen
     * @param page
     * @param rows
     * @param sort
     * @param order
     * @return
     * @throws Exception
     */
    public List getPayChannelFrozenList(PayChannelFrozen payChannelFrozen,int page,int rows,String sort,String order) throws Exception{
        sort = Tools.paraNameToDBColumnName(sort);
        String sqlCon = setPayChannelFrozenSql(payChannelFrozen);
        String sortOrder = sort == null || sort.length()==0?" order by ID desc":(" order by " + sort +("desc".equals(order)?" desc ":" asc "));
        String sql = "select tmp.*,m.store_name from(select * from (" +
                "  select rownum rowno,tmp1.* from (" +
                "   select tmp.*  from pay_channel_frozen tmp " +(sqlCon.length()==0?"":" where "+ sqlCon) +sortOrder+
                "  ) tmp1 "+
                ")  where rowno > "+((page-1)*rows)+ " and rowno<= " + (page*rows) + sortOrder+")tmp left join pay_merchant_root m on tmp.MER_NOS=m.cust_id";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List list = new ArrayList();
        try {
            con = connection();
            ps = con.prepareStatement(sql.toString());
            int n = 1;
            setPayChannelFrozenParameter(ps,payChannelFrozen,n);
            rs = ps.executeQuery();
            while(rs.next()){
                list.add(getPayChannelFrozenValue(rs));
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
     * remove PayChannelFrozen
     * @param id
     * @throws Exception     
     */
    public void removePayChannelFrozen(String id) throws Exception {
        String sql = "delete from pay_channel_frozen where ID=?";
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
     * detail PayChannelFrozen
     * @param id
     * @return PayChannelFrozen
     * @throws Exception
     */
    public PayChannelFrozen detailPayChannelFrozen(String id) throws Exception {
        String sql = "select * from pay_channel_frozen where ID=?";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            ps.setString(1,id);
            rs = ps.executeQuery();
            if(rs.next())return getPayChannelFrozenValue(rs); 
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(rs, ps, con);
        }
        return null;
    }
    /**
     * update PayChannelFrozen
     * @param payChannelFrozen
     * @throws Exception
     */
    public void updatePayChannelFrozen(PayChannelFrozen payChannelFrozen) throws Exception {
        String sqlTmp = "";
        if(payChannelFrozen.id != null)sqlTmp = sqlTmp + " ID=?,";
        if(payChannelFrozen.channel != null)sqlTmp = sqlTmp + " CHANNEL=?,";
        if(payChannelFrozen.srcAmt != null)sqlTmp = sqlTmp + " SRC_AMT=?,";
        if(payChannelFrozen.merNos != null)sqlTmp = sqlTmp + " MER_NOS=?,";
        if(payChannelFrozen.frozenTime != null)sqlTmp = sqlTmp + " FROZEN_TIME=to_date(?,'yyyy-mm-dd hh24:mi:ss'),";
        if(payChannelFrozen.orderTxamt != null)sqlTmp = sqlTmp + " ORDER_TXAMT=?,";
        if(payChannelFrozen.orderIds != null)sqlTmp = sqlTmp + " ORDER_IDS=?,";
        if(payChannelFrozen.frozenDays != null)sqlTmp = sqlTmp + " FROZEN_DAYS=?,";
        if(payChannelFrozen.status != null)sqlTmp = sqlTmp + " STATUS=?,";
        if(payChannelFrozen.curAmt != null)sqlTmp = sqlTmp + " CUR_AMT=?,";
        if(payChannelFrozen.salemanId != null)sqlTmp = sqlTmp + " SALEMAN_ID=?,";
        if(payChannelFrozen.createTime != null)sqlTmp = sqlTmp + " CREATE_TIME=to_date(?,'yyyy-mm-dd hh24:mi:ss'),";
        if(payChannelFrozen.remark != null)sqlTmp = sqlTmp + " REMARK=?,";
        if(payChannelFrozen.optId != null)sqlTmp = sqlTmp + " OPT_ID=?,";
        if(sqlTmp.length()==0)return;
        String sql = "update pay_channel_frozen "+        
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
            if(payChannelFrozen.id != null)ps.setString(n++,payChannelFrozen.id);
            if(payChannelFrozen.channel != null)ps.setString(n++,payChannelFrozen.channel);
            if(payChannelFrozen.srcAmt != null)ps.setLong(n++,payChannelFrozen.srcAmt);
            if(payChannelFrozen.merNos != null)ps.setString(n++,payChannelFrozen.merNos);
            if(payChannelFrozen.frozenTime != null)ps.setString(n++,new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payChannelFrozen.frozenTime));
            if(payChannelFrozen.orderTxamt != null)ps.setLong(n++,payChannelFrozen.orderTxamt);
            if(payChannelFrozen.orderIds != null)ps.setString(n++,payChannelFrozen.orderIds);
            if(payChannelFrozen.frozenDays != null)ps.setLong(n++,payChannelFrozen.frozenDays);
            if(payChannelFrozen.status != null)ps.setString(n++,payChannelFrozen.status);
            if(payChannelFrozen.curAmt != null)ps.setLong(n++,payChannelFrozen.curAmt);
            if(payChannelFrozen.salemanId != null)ps.setString(n++,payChannelFrozen.salemanId);
            if(payChannelFrozen.createTime != null)ps.setString(n++,new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payChannelFrozen.createTime));
            if(payChannelFrozen.remark != null)ps.setString(n++,payChannelFrozen.remark);
            if(payChannelFrozen.optId != null)ps.setString(n++,payChannelFrozen.optId);
            ps.setString(n++,payChannelFrozen.id);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }finally {
            close(null, ps, con);
        }
    }

}