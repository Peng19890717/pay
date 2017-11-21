package com.pay.merchantinterface.dao;

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
import com.pay.merchantinterface.service.PayRequest;
import com.pay.user.dao.PayTranUserInfo;
/**
 * Table PAY_TRAN_USER_QUICK_CARD DAO. 
 * @author Administrator
 *
 */
public class PayTranUserQuickCardDAO  extends BaseDAO {
    private static final Log log = LogFactory.getLog(PayTranUserQuickCardDAO.class);
    public static synchronized PayTranUserQuickCard getPayTranUserQuickCardValue(ResultSet rs)throws SQLException {
        PayTranUserQuickCard payTranUserQuickCard = new PayTranUserQuickCard();
        payTranUserQuickCard.id = rs.getString("ID");
        payTranUserQuickCard.credentialType = rs.getString("CREDENTIAL_TYPE");
        payTranUserQuickCard.credentialNo = rs.getString("CREDENTIAL_NO");
        payTranUserQuickCard.cardNo = rs.getString("CARD_NO");
        payTranUserQuickCard.cvv2 = rs.getString("CVV2");
        payTranUserQuickCard.validPeriod = rs.getString("VALID_PERIOD");
        payTranUserQuickCard.bankId = rs.getString("BANK_ID");
        payTranUserQuickCard.status = rs.getString("STATUS");
        payTranUserQuickCard.name = rs.getString("NAME");
        payTranUserQuickCard.mobileNo = rs.getString("MOBILE_NO");
        payTranUserQuickCard.createTime = rs.getTimestamp("CREATE_TIME");
        payTranUserQuickCard.merchantId = rs.getString("MERCHANT_ID");
        payTranUserQuickCard.payerId = rs.getString("PAYER_ID");
        payTranUserQuickCard.bindId = rs.getString("BIND_ID");
        return payTranUserQuickCard;
    }
    public String addPayTranUserQuickCard(PayTranUserQuickCard payTranUserQuickCard) throws Exception {
        String sql = "insert into PAY_TRAN_USER_QUICK_CARD("+
            "ID," + 
            "CREDENTIAL_TYPE," + 
            "CREDENTIAL_NO," + 
            "CARD_NO," + 
            "CVV2," + 
            "VALID_PERIOD," + 
            "BANK_ID," + 
            "NAME," + 
            "MOBILE_NO," + 
            "CREATE_TIME," + 
            "MERCHANT_ID," + 
            "PAYER_ID,"+
            "BIND_ID)values(?,?,?,?,?,?,?,?,?,sysdate,?,?,?)";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            int n=1;
            ps.setString(n++,payTranUserQuickCard.id);
            ps.setString(n++,payTranUserQuickCard.credentialType);
            ps.setString(n++,payTranUserQuickCard.credentialNo);
            ps.setString(n++,payTranUserQuickCard.cardNo);
            ps.setString(n++,payTranUserQuickCard.cvv2);
            ps.setString(n++,payTranUserQuickCard.validPeriod);
            ps.setString(n++,payTranUserQuickCard.bankId);
            ps.setString(n++,payTranUserQuickCard.name);
            ps.setString(n++,payTranUserQuickCard.mobileNo);
            ps.setString(n++,payTranUserQuickCard.merchantId);
            ps.setString(n++,payTranUserQuickCard.payerId);
            ps.setString(n++,payTranUserQuickCard.bindId==null?"":payTranUserQuickCard.bindId);
            ps.executeUpdate();
            return "";
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }finally {
            close(null, ps, con);
        }
    }
    public List getPayTranUserQuickCardByPayerId(String merNo,String payerId) throws Exception{
        String sql = "select * from PAY_TRAN_USER_QUICK_CARD where MERCHANT_ID=? and PAYER_ID=? order by CREATE_TIME desc";
        log.info(sql);
        log.info("MERCHANT_ID="+merNo+",PAYER_ID="+payerId);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List list = new ArrayList();
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            ps.setString(1, merNo);
            ps.setString(2, payerId);
            rs = ps.executeQuery();
            while(rs.next()) list.add(getPayTranUserQuickCardValue(rs));
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(rs, ps, con);
        }
    }
    public boolean exist(String credentialNo,String cardNo ) throws Exception{
        String sql = "select id from PAY_TRAN_USER_QUICK_CARD where CARD_NO=?";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            ps.setString(1, cardNo);
            rs = ps.executeQuery();
            if(rs.next())return true;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(rs, ps, con);
        }
        return false;
    }
    public void loadQuickPayH5Card(PayTranUserInfo user) throws Exception{
        String sql = "select c.* from PAY_TRAN_USER_QUICK_CARD c left join PAY_TRAN_USER_INFO u on c.credential_no=u.CRET_NO and u.USER_ID=?";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            ps.setString(1, user.userId);
            rs = ps.executeQuery();
            while(rs.next()){
            	PayTranUserQuickCard card = getPayTranUserQuickCardValue(rs); 
                user.quickH5CardList.add(card);
                user.quickH5CardMap.put(card.credentialNo+","+card.cardNo, card);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(rs, ps, con);
        }
    }
    /**
     * Set query condition sql.
     * @param payTranUserQuickCard
     * @return
     */
    private String setPayTranUserQuickCardSql(PayTranUserQuickCard payTranUserQuickCard) {
        StringBuffer sql = new StringBuffer();
        
        String tmp = sql.toString();
        if(tmp.length()>0)tmp=tmp.substring(0,tmp.lastIndexOf(" and "));
        return tmp;
    }
    /**
     * Set query parameter.
     * @param ps
     * @param payTranUserQuickCard
     * @param n
     * @return
     * @throws SQLException
     */
    private int setPayTranUserQuickCardParameter(PreparedStatement ps,PayTranUserQuickCard payTranUserQuickCard,int n)throws SQLException {
        return n;
    }
    /**
     * Get records count.
     * @param payTranUserQuickCard
     * @return
     */
    public int getPayTranUserQuickCardCount(PayTranUserQuickCard payTranUserQuickCard) {
        String sqlCon = setPayTranUserQuickCardSql(payTranUserQuickCard);
        String sql = "select count(rownum) recordCount from PAY_TRAN_USER_QUICK_CARD " +(sqlCon.length()==0?"":" where "+ sqlCon);
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            int n = 1;
            setPayTranUserQuickCardParameter(ps,payTranUserQuickCard,n);
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
     * @param payTranUserQuickCard
     * @param page
     * @param rows
     * @param sort
     * @param order
     * @return
     * @throws Exception
     */
    public List getPayTranUserQuickCardList(PayTranUserQuickCard payTranUserQuickCard,int page,int rows,String sort,String order) throws Exception{
        sort = Tools.paraNameToDBColumnName(sort);
        String sqlCon = setPayTranUserQuickCardSql(payTranUserQuickCard);
        String sortOrder = sort == null || sort.length()==0?" order by ID desc":(" order by " + sort +("desc".equals(order)?" desc ":" asc "));
        String sql = "select * from (" +
                "  select rownum rowno,tmp1.* from (" +
                "   select tmp.*  from PAY_TRAN_USER_QUICK_CARD tmp " +(sqlCon.length()==0?"":" where "+ sqlCon) +sortOrder+
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
            setPayTranUserQuickCardParameter(ps,payTranUserQuickCard,n);
            rs = ps.executeQuery();
            while(rs.next()){
                list.add(getPayTranUserQuickCardValue(rs));
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
     * remove PayTranUserQuickCard
     * @param id
     * @throws Exception     
     */
    public void removePayTranUserQuickCard(String id) throws Exception {
        String sql = "delete from PAY_TRAN_USER_QUICK_CARD where ID=?";
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
     * detail PayTranUserQuickCard
     * @param id
     * @return PayTranUserQuickCard
     * @throws Exception
     */
    public PayTranUserQuickCard detailPayTranUserQuickCard(String id) throws Exception {
        String sql = "select * from PAY_TRAN_USER_QUICK_CARD where ID=?";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            ps.setString(1,id);
            rs = ps.executeQuery();
            if(rs.next())return getPayTranUserQuickCardValue(rs); 
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(rs, ps, con);
        }
        return null;
    }
    /**
     * detail PayTranUserQuickCard
     * @param cardNo
     * @return PayTranUserQuickCard
     * @throws Exception
     */
    public PayTranUserQuickCard getBindCardByNo(String cardNo) throws Exception {
        String sql = "select * from PAY_TRAN_USER_QUICK_CARD where CARD_NO=?";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            ps.setString(1,cardNo);
            rs = ps.executeQuery();
            if(rs.next())return getPayTranUserQuickCardValue(rs); 
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(rs, ps, con);
        }
        return null;
    }
    /**
     * update PayTranUserQuickCard
     * @param payTranUserQuickCard
     * @throws Exception
     */
    public void updatePayTranUserQuickCard(PayTranUserQuickCard payTranUserQuickCard) throws Exception {
        String sql = "update PAY_TRAN_USER_QUICK_CARD set STATUS=?,CREDENTIAL_TYPE=?,CREDENTIAL_NO=?,NAME=?,MOBILE_NO=?,BIND_ID=? where ID=?"; 
        log.info(sql);
        log.info("STATUS="+payTranUserQuickCard.status+";CREDENTIAL_TYPE="+payTranUserQuickCard.credentialType+";CREDENTIAL_NO="+payTranUserQuickCard.credentialNo
        		+";NAME="+payTranUserQuickCard.name+";MOBILE_NO="+payTranUserQuickCard.mobileNo+";BIND_ID="+payTranUserQuickCard.bindId+
        		";ID="+payTranUserQuickCard.id);
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            int n=1;
            ps.setString(n++,payTranUserQuickCard.status);
            ps.setString(n++,payTranUserQuickCard.credentialType);
            ps.setString(n++,payTranUserQuickCard.credentialNo);
            ps.setString(n++,payTranUserQuickCard.name);
            ps.setString(n++,payTranUserQuickCard.mobileNo);
            ps.setString(n++,payTranUserQuickCard.bindId);
            ps.setString(n++,payTranUserQuickCard.id);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }finally {
            close(null, ps, con);
        }
    }
	public void updatePayTranUserQuickCard(PayRequest request) throws Exception {
        String sql = "update PAY_TRAN_USER_QUICK_CARD set CREDENTIAL_NO=?,CVV2=?,VALID_PERIOD=?,NAME=?,MOBILE_NO=? where STATUS='0' and CARD_NO=?";
        log.info(sql);
        log.info("CREDENTIAL_NO="+request.credentialNo+";CVV2="+request.cvv2+";VALID_PERIOD="+request.validPeriod+";NAME="+request.userName+
        		";MOBILE_NO="+request.userMobileNo+";CARD_NO="+request.cardNo);
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            int n=1;
            ps.setString(n++,request.credentialNo);
            ps.setString(n++,request.cvv2);
            ps.setString(n++,request.validPeriod);
            ps.setString(n++,request.userName);
            ps.setString(n++,request.userMobileNo);
            ps.setString(n++,request.cardNo);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(null, ps, con);
        }
	}
}