package com.pay.coopbank.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import util.Tools;

import com.jweb.dao.BaseDAO;
/**
 * Table PAY_CHANNEL_BANK_RELATION DAO. 
 * @author Administrator
 *
 */
public class PayChannelBankRelationDAO  extends BaseDAO {
    private static final Log log = LogFactory.getLog(PayChannelBankRelationDAO.class);
    public static synchronized PayChannelBankRelation getPayChannelBankRelationValue(ResultSet rs)throws SQLException {
        PayChannelBankRelation payChannelBankRelation = new PayChannelBankRelation();
        payChannelBankRelation.id = rs.getString("ID");
        payChannelBankRelation.bankCode = rs.getString("BANK_CODE");
        payChannelBankRelation.channelCode = rs.getString("CHANNEL_CODE");
        payChannelBankRelation.supportedUserType = rs.getString("SUPPORTED_USER_TYPE");
        payChannelBankRelation.quickUserType = rs.getString("QUICK_USER_TYPE");
        payChannelBankRelation.withdrawUserType = rs.getString("WITHDRAW_USER_TYPE");
        payChannelBankRelation.receiveUserType = rs.getString("RECEIVE_USER_TYPE");
        payChannelBankRelation.webDebitMaxAmt = rs.getLong("WEB_DEBIT_MAX_AMT");
        payChannelBankRelation.webCreditMaxAmt = rs.getLong("WEB_CREDIT_MAX_AMT");
        payChannelBankRelation.webPublicMaxAmt = rs.getLong("WEB_PUBLIC_MAX_AMT");
        payChannelBankRelation.quickDebitMaxAmt = rs.getLong("QUICK_DEBIT_MAX_AMT");
        payChannelBankRelation.quickCeditMaxAmt = rs.getLong("QUICK_CEDIT_MAX_AMT");
        payChannelBankRelation.receiveMaxAmt = rs.getLong("RECEIVE_MAX_AMT");
        return payChannelBankRelation;
    }
    public String addPayChannelBankRelation(PayChannelBankRelation payChannelBankRelation) throws Exception {
        String sql = "insert into PAY_CHANNEL_BANK_RELATION("+
            "ID," + 
            "BANK_CODE," + 
            "CHANNEL_CODE," +
            "SUPPORTED_USER_TYPE," +
            "QUICK_USER_TYPE," +
            "WITHDRAW_USER_TYPE," +
            "RECEIVE_USER_TYPE," + 
            "WEB_DEBIT_MAX_AMT," + 
            "WEB_CREDIT_MAX_AMT," + 
            "WEB_PUBLIC_MAX_AMT," + 
            "QUICK_DEBIT_MAX_AMT," + 
            "QUICK_CEDIT_MAX_AMT," + 
            "RECEIVE_MAX_AMT)values(?,?,?,?,?,?,?,?,?,?,?,?,?)";
        log.info(sql);
        log.info("ID="+payChannelBankRelation.id+";BANK_CODE="+payChannelBankRelation.bankCode+";CHANNEL_CODE="+payChannelBankRelation.channelCode+
        		";SUPPORTED_USER_TYPE="+payChannelBankRelation.supportedUserType+";QUICK_USER_TYPE="+payChannelBankRelation.quickUserType+
        		";WITHDRAW_USER_TYPE="+payChannelBankRelation.withdrawUserType+";RECEIVE_USER_TYPE="+payChannelBankRelation.receiveUserType+
        		";WEB_DEBIT_MAX_AMT="+payChannelBankRelation.webDebitMaxAmt+";WEB_CREDIT_MAX_AMT="+payChannelBankRelation.webCreditMaxAmt+
        		";WEB_PUBLIC_MAX_AMT="+payChannelBankRelation.webPublicMaxAmt+";QUICK_DEBIT_MAX_AMT="+payChannelBankRelation.quickDebitMaxAmt+
        		";QUICK_CEDIT_MAX_AMT="+payChannelBankRelation.quickCeditMaxAmt+";RECEIVE_MAX_AMT="+payChannelBankRelation.receiveMaxAmt);
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            int n=1;
            ps.setString(n++,payChannelBankRelation.id);
            ps.setString(n++,payChannelBankRelation.bankCode);
            ps.setString(n++,payChannelBankRelation.channelCode);
            ps.setString(n++,payChannelBankRelation.supportedUserType==null?"":payChannelBankRelation.supportedUserType);
            ps.setString(n++,payChannelBankRelation.quickUserType==null?"":payChannelBankRelation.quickUserType);
            ps.setString(n++,payChannelBankRelation.withdrawUserType==null?"":payChannelBankRelation.withdrawUserType);
            ps.setString(n++,payChannelBankRelation.receiveUserType==null?"":payChannelBankRelation.receiveUserType);
            ps.setLong(n++,payChannelBankRelation.webDebitMaxAmt==null?0:payChannelBankRelation.webDebitMaxAmt);
            ps.setLong(n++,payChannelBankRelation.webCreditMaxAmt==null?0:payChannelBankRelation.webCreditMaxAmt);
            ps.setLong(n++,payChannelBankRelation.webPublicMaxAmt==null?0:payChannelBankRelation.webPublicMaxAmt);
            ps.setLong(n++,payChannelBankRelation.quickDebitMaxAmt==null?0:payChannelBankRelation.quickDebitMaxAmt);
            ps.setLong(n++,payChannelBankRelation.quickCeditMaxAmt==null?0:payChannelBankRelation.quickCeditMaxAmt);
            ps.setLong(n++,payChannelBankRelation.receiveMaxAmt==null?0:payChannelBankRelation.receiveMaxAmt);
            ps.executeUpdate();
            return "";
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(null, ps, con);
        }
    }
    public List getList() throws Exception{
        String sql = "select * from PAY_CHANNEL_BANK_RELATION";
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
                list.add(getPayChannelBankRelationValue(rs));
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
     * @param payChannelBankRelation
     * @return
     */
    private String setPayChannelBankRelationSql(PayChannelBankRelation payChannelBankRelation) {
        StringBuffer sql = new StringBuffer();
        String tmp = sql.toString();
        if(tmp.length()>0)tmp=tmp.substring(0,tmp.lastIndexOf(" and "));
        return tmp;
    }
    /**
     * Set query parameter.
     * @param ps
     * @param payChannelBankRelation
     * @param n
     * @return
     * @throws SQLException
     */
    private int setPayChannelBankRelationParameter(PreparedStatement ps,PayChannelBankRelation payChannelBankRelation,int n)throws SQLException {
        return n;
    }
    /**
     * Get records count.
     * @param payChannelBankRelation
     * @return
     */
    public int getPayChannelBankRelationCount(PayChannelBankRelation payChannelBankRelation) {
        String sqlCon = setPayChannelBankRelationSql(payChannelBankRelation);
        String sql = "select count(rownum) recordCount from PAY_CHANNEL_BANK_RELATION " +(sqlCon.length()==0?"":" where "+ sqlCon);
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            int n = 1;
            setPayChannelBankRelationParameter(ps,payChannelBankRelation,n);
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
     * @param payChannelBankRelation
     * @param page
     * @param rows
     * @param sort
     * @param order
     * @return
     * @throws Exception
     */
    public List getPayChannelBankRelationList(PayChannelBankRelation payChannelBankRelation,int page,int rows,String sort,String order) throws Exception{
        sort = Tools.paraNameToDBColumnName(sort);
        String sqlCon = setPayChannelBankRelationSql(payChannelBankRelation);
        String sortOrder = sort == null || sort.length()==0?" order by ID desc":(" order by " + sort +("desc".equals(order)?" desc ":" asc "));
        String sql = "select * from (" +
                "  select rownum rowno,tmp1.* from (" +
                "   select tmp.*  from PAY_CHANNEL_BANK_RELATION tmp " +(sqlCon.length()==0?"":" where "+ sqlCon) +sortOrder+
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
            setPayChannelBankRelationParameter(ps,payChannelBankRelation,n);
            rs = ps.executeQuery();
            while(rs.next()){
                list.add(getPayChannelBankRelationValue(rs));
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
     * 通过银行编码删除银行与渠道之间关联关系
     * @param id
     * @throws Exception     
     */
    public void removePayChannelBankRelation(String bankCode) throws Exception {
        String sql = "delete from PAY_CHANNEL_BANK_RELATION where CHANNEL_CODE=?";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            ps.setString(1,bankCode);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(null, ps, con);
        }
    }
    /**
     * detail PayChannelBankRelation
     * @param id
     * @return PayChannelBankRelation
     * @throws Exception
     */
    public PayChannelBankRelation detailPayChannelBankRelation(String id) throws Exception {
        String sql = "select * from PAY_CHANNEL_BANK_RELATION where ID=?";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            ps.setString(1,id);
            rs = ps.executeQuery();
            if(rs.next())return getPayChannelBankRelationValue(rs); 
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(rs, ps, con);
        }
        return null;
    }
    /**
     * 通过支付渠道获取支持银行列表
     * @param bankCode
     * @return
     * @throws Exception
     */
	public String selectByBankCode(String bankCode) throws Exception {
		String sql = "select BANK_CODE from PAY_CHANNEL_BANK_RELATION where CHANNEL_CODE = ? ";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        StringBuffer buffer = new StringBuffer();
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            ps.setString(1, bankCode);
            rs = ps.executeQuery();
            while(rs.next()){
            	buffer.append(rs.getString("BANK_CODE")).append(",");
            }
            return buffer.toString();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(rs, ps, con);
        }
	}
	public Map selectChannelBankRelationByCode(String channel) throws Exception {
		String sql = "select * from PAY_CHANNEL_BANK_RELATION where CHANNEL_CODE = ? ";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Map map = new HashMap();
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            ps.setString(1, channel);
            rs = ps.executeQuery();
            while(rs.next()){
            	PayChannelBankRelation r = getPayChannelBankRelationValue(rs);
            	map.put(r.channelCode+","+r.bankCode, r);
            }
            return map;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(rs, ps, con);
        }
	}
	public PayChannelBankRelation getRelationByChannelAndBank(String bankCode,String channelCode) throws Exception {
		String sql = "select * from PAY_CHANNEL_BANK_RELATION where BANK_CODE=? and CHANNEL_CODE=?";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            ps.setString(1, bankCode);
            ps.setString(2, channelCode);
            rs = ps.executeQuery();
            if(rs.next())return getPayChannelBankRelationValue(rs);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(rs, ps, con);
        }
        return null;
	}
}