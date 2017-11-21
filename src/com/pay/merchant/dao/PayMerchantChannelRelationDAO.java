package com.pay.merchant.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.jweb.dao.BaseDAO;
/**
 * Table PAY_MERCHANT_CHANNEL_RELATION DAO. 
 * @author Administrator
 *
 */
public class PayMerchantChannelRelationDAO  extends BaseDAO {
    private static final Log log = LogFactory.getLog(PayMerchantChannelRelationDAO.class);
    public static synchronized PayMerchantChannelRelation getPayMerchantChannelRelationValue(ResultSet rs)throws SQLException {
        PayMerchantChannelRelation payMerchantChannelRelation = new PayMerchantChannelRelation();
        payMerchantChannelRelation.merno = rs.getString("CUST_ID");
        payMerchantChannelRelation.storeName = rs.getString("STORE_NAME");
        return payMerchantChannelRelation;
    }
    public String addPayMerchantChannelRelation(PayMerchantChannelRelation payMerchantChannelRelation) throws Exception {
        String sql = "insert into PAY_MERCHANT_CHANNEL_RELATION("+
            "MERNO," + 
            "CHANNEL_ID," + 
            "TRAN_TYPE)values(?,?,?)";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            int n=1;
            ps.setString(n++,payMerchantChannelRelation.merno);
            ps.setString(n++,payMerchantChannelRelation.channelId);
            ps.setString(n++,payMerchantChannelRelation.tranType);
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
        String sql = "select * from PAY_MERCHANT_CHANNEL_RELATION";
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
                list.add(getPayMerchantChannelRelationValue(rs));
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
     * @param payMerchantChannelRelation
     * @return
     */
    private String setPayMerchantChannelRelationSql(PayMerchantChannelRelation payMerchantChannelRelation) {
        StringBuffer sql = new StringBuffer();
        if(payMerchantChannelRelation.merno != null && payMerchantChannelRelation.merno.length() !=0) {
            sql.append(" MERNO = ? and ");
        }
        if(payMerchantChannelRelation.channelId != null && payMerchantChannelRelation.channelId.length() !=0) {
            sql.append(" CHANNEL_ID = ? and ");
        }
        if(payMerchantChannelRelation.tranType != null && payMerchantChannelRelation.tranType.length() !=0) {
            sql.append(" TRAN_TYPE = ? and ");
        }
        String tmp = sql.toString();
        if(tmp.length()>0)tmp=tmp.substring(0,tmp.lastIndexOf(" and "));
        return tmp;
    }
    /**
     * Set query parameter.
     * @param ps
     * @param payMerchantChannelRelation
     * @param n
     * @return
     * @throws SQLException
     */
    private int setPayMerchantChannelRelationParameter(PreparedStatement ps,PayMerchantChannelRelation payMerchantChannelRelation,int n)throws SQLException {
        if(payMerchantChannelRelation.merno != null && payMerchantChannelRelation.merno.length() !=0) {
            ps.setString(n++,payMerchantChannelRelation.merno);
        }
        if(payMerchantChannelRelation.channelId != null && payMerchantChannelRelation.channelId.length() !=0) {
            ps.setString(n++,payMerchantChannelRelation.channelId);
        }
        if(payMerchantChannelRelation.tranType != null && payMerchantChannelRelation.tranType.length() !=0) {
            ps.setString(n++,payMerchantChannelRelation.tranType);
        }
        return n;
    }
    /**
     * Get records count.
     * @param payMerchantChannelRelation
     * @return
     */
    public int getPayMerchantChannelRelationCount(PayMerchantChannelRelation payMerchantChannelRelation) {
        String sqlCon = setPayMerchantChannelRelationSql(payMerchantChannelRelation);
        String sql = "select count(rownum) recordCount from" +
		        		"( SELECT rownum rowno,tmp.*" +
		        	    " FROM" +
			        		" (select DISTINCT merchant.cust_id,merchant.store_name " +
			                "  from PAY_MERCHANT_CHANNEL_RELATION  chanel "
			                + " left join (select cust_id,store_name from pay_merchant_root) merchant"
			                + " on chanel.merno = merchant.cust_id"
			                + " left join (select bank_code,bank_name from pay_coop_bank ) bank"
			                + " on bank.bank_code = chanel.channel_id "+
			                (sqlCon.length()==0?"":" where "+ sqlCon) +
			                " ) tmp" + (payMerchantChannelRelation.openedMerchantPayFlag != null && payMerchantChannelRelation.openedMerchantPayFlag.length() !=0
	                			?" where cust_id in(select MERNO from PAY_OPENED_MER_PAY)":"") +
			              ")";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            int n = 1;
            setPayMerchantChannelRelationParameter(ps,payMerchantChannelRelation,n);
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
     * @param payMerchantChannelRelation
     * @param page
     * @param rows
     * @param sort
     * @param order
     * @return
     * @throws Exception
     */
    public List getPayMerchantChannelRelationList(PayMerchantChannelRelation payMerchantChannelRelation,int page,int rows,String sort,String order) throws Exception{
        String sqlCon = setPayMerchantChannelRelationSql(payMerchantChannelRelation);
        String sql = 
        		" SELECT * " +
                "FROM" +
        	    "( SELECT rownum rowno,tmp.*" +
        	    " FROM" +
	        		" (select DISTINCT merchant.cust_id,merchant.store_name " +
	                "  from PAY_MERCHANT_CHANNEL_RELATION  chanel "
	                + " left join (select cust_id,store_name from pay_merchant_root) merchant"
	                + " on chanel.merno = merchant.cust_id"
	                + " left join (select bank_code,bank_name from pay_coop_bank ) bank"
	                + " on bank.bank_code = chanel.channel_id "+
	                (sqlCon.length()==0?"":" where "+ sqlCon) +
	                " ) tmp" + (payMerchantChannelRelation.openedMerchantPayFlag != null && payMerchantChannelRelation.openedMerchantPayFlag.length() !=0
	                			?" where cust_id in(select MERNO from PAY_OPENED_MER_PAY)":"") +
		        	" ) tmp1" +
	                "  where rowno > "+((page-1)*rows)+ " and rowno<= " + (page*rows) ;
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List list = new ArrayList();
        try {
            con = connection();
            ps = con.prepareStatement(sql.toString());
            int n = 1;
            setPayMerchantChannelRelationParameter(ps,payMerchantChannelRelation,n);
            rs = ps.executeQuery();
            while(rs.next()){
                list.add(getPayMerchantChannelRelationValue(rs));
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
     * remove PayMerchantChannelRelation
     * @param merno
     * @throws Exception     
     */
    public void removePayMerchantChannelRelation(String merno) throws Exception {
        String sql = "delete from PAY_MERCHANT_CHANNEL_RELATION where MERNO=?";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            ps.setString(1,merno);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(null, ps, con);
        }
    }
    /**
     * 根据商户编号查询支付通道列表
     * @param merno
     * @param channelId
     * @return
     * @throws Exception
     */
    public List<PayMerchantChannelRelation> selectPayChannelByMerno(String merno) throws Exception {
        String sql = " SELECT *"
        		+ " FROM PAY_MERCHANT_CHANNEL_RELATION chanel"
        		+ " LEFT JOIN"
        		+ " (SELECT cust_id,store_name FROM pay_merchant_root) merchant"
        		+ " ON chanel.merno = merchant.cust_id"
        		+ " LEFT JOIN"
        		+ " (SELECT bank_code,bank_name FROM pay_coop_bank) bank"
        		+ " ON bank.bank_code = chanel.channel_id where merno = ?";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            ps.setString(1,merno);
            rs = ps.executeQuery();
            List<PayMerchantChannelRelation> list = new ArrayList<PayMerchantChannelRelation>();
            while(rs.next()){
            	PayMerchantChannelRelation payMerchantChannelRelation = getPayMerchantChannelRelationValue(rs);
                payMerchantChannelRelation.channelId = rs.getString("CHANNEL_ID");
                payMerchantChannelRelation.tranType = rs.getString("TRAN_TYPE");
                payMerchantChannelRelation.channelName = rs.getString("BANK_NAME");
                list.add(payMerchantChannelRelation);
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