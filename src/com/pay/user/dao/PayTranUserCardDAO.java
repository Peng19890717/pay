package com.pay.user.dao;

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
 * Table PAY_TRAN_USER_CARD DAO. 
 * @author Administrator
 *
 */
public class PayTranUserCardDAO  extends BaseDAO {
    private static final Log log = LogFactory.getLog(PayTranUserCardDAO.class);
    public static synchronized PayTranUserCard getPayTranUserCardValue(ResultSet rs)throws SQLException {
        PayTranUserCard payTranUserCard = new PayTranUserCard();
        payTranUserCard.id = rs.getString("ID");
        payTranUserCard.userId = rs.getString("USER_ID");
        payTranUserCard.cardBank = rs.getString("CARD_BANK");
        payTranUserCard.cardType = rs.getString("CARD_TYPE");
        payTranUserCard.cardStatus = rs.getString("CARD_STATUS");
        payTranUserCard.cardStaRes = rs.getString("CARD_STA_RES");
        payTranUserCard.cardNo = rs.getString("CARD_NO");
        payTranUserCard.cardBankBranch = rs.getString("CARD_BANK_BRANCH");
        payTranUserCard.bakOpenTime = rs.getTimestamp("BAK_OPEN_TIME");
        payTranUserCard.bakOpenNum = rs.getLong("BAK_OPEN_NUM");
        payTranUserCard.bakCloseTime = rs.getTimestamp("BAK_CLOSE_TIME");
        payTranUserCard.bakCloseNum = rs.getLong("BAK_CLOSE_NUM");
        payTranUserCard.bakCloseRes = rs.getString("BAK_CLOSE_RES");
        payTranUserCard.bakUserId = rs.getString("BAK_USER_ID");
        payTranUserCard.bakUpdTime = rs.getTimestamp("BAK_UPD_TIME");
        payTranUserCard.revFlag = rs.getString("REV_FLAG");
        return payTranUserCard;
    }
    public String addPayTranUserCard(PayTranUserCard payTranUserCard) throws Exception {
        String sql = "insert into PAY_TRAN_USER_CARD("+
            "ID," + 
            "USER_ID," + 
            "CARD_BANK," + 
            "CARD_TYPE," + 
            "CARD_STATUS," + 
            "CARD_STA_RES," + 
            "CARD_NO," + 
            "CARD_BANK_BRANCH)values(?,?,?,?,?,?,?,?)";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            int n=1;
            ps.setString(n++,payTranUserCard.id);
            ps.setString(n++,payTranUserCard.userId);
            ps.setString(n++,payTranUserCard.cardBank);
            ps.setString(n++,payTranUserCard.cardType);
            ps.setString(n++,payTranUserCard.cardStatus);
            ps.setString(n++,payTranUserCard.cardStaRes);
            ps.setString(n++,payTranUserCard.cardNo);
            ps.setString(n++,payTranUserCard.cardBankBranch);
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
        String sql = "select * from PAY_TRAN_USER_CARD";
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
                list.add(getPayTranUserCardValue(rs));
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(rs, ps, con);
        }
    }
    public List getListByUserId(String userId) throws Exception{
        String sql = "select * from PAY_TRAN_USER_CARD where USER_ID=?";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List list = new ArrayList();
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            ps.setString(1, userId);
            rs = ps.executeQuery();
            while(rs.next()){
                list.add(getPayTranUserCardValue(rs));
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(rs, ps, con);
        }
    }
    public PayTranUserCard getUserBindCardByCardNo(String userId,String cardNo) throws Exception{
        String sql = "select c.* from PAY_TRAN_USER_INFO u,PAY_TRAN_USER_CARD c where u.USER_ID=c.USER_ID and u.USER_ID=? " +
        		"and c.CARD_NO=?";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            ps.setString(1, userId);
            ps.setString(2, cardNo);
            rs = ps.executeQuery();
            if(rs.next())return getPayTranUserCardValue(rs);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(rs, ps, con);
        }
        return null;
    }
    /**
     * Set query condition sql.
     * @param payTranUserCard
     * @return
     */
    private String setPayTranUserCardSql(PayTranUserCard payTranUserCard) {
        StringBuffer sql = new StringBuffer();
        
        if(payTranUserCard.userId != null && payTranUserCard.userId.length() !=0) {
            sql.append(" USER_ID = ? and ");
        }
        String tmp = sql.toString();
        if(tmp.length()>0)tmp=tmp.substring(0,tmp.lastIndexOf(" and "));
        return tmp;
    }
    /**
     * Set query parameter.
     * @param ps
     * @param payTranUserCard
     * @param n
     * @return
     * @throws SQLException
     */
    private int setPayTranUserCardParameter(PreparedStatement ps,PayTranUserCard payTranUserCard,int n)throws SQLException {
        if(payTranUserCard.userId != null && payTranUserCard.userId.length() !=0) {
            ps.setString(n++,payTranUserCard.userId);
        }
        return n;
    }
    /**
     * Get records count.
     * @param payTranUserCard
     * @return
     */
    public int getPayTranUserCardCount(PayTranUserCard payTranUserCard) {
        String sqlCon = setPayTranUserCardSql(payTranUserCard);
        String sql = "select count(rownum) recordCount from PAY_TRAN_USER_CARD " +(sqlCon.length()==0?"":" where "+ sqlCon);
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            int n = 1;
            setPayTranUserCardParameter(ps,payTranUserCard,n);
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
     * @param payTranUserCard
     * @param page
     * @param rows
     * @param sort
     * @param order
     * @return
     * @throws Exception
     */
    public List getPayTranUserCardList(PayTranUserCard payTranUserCard,int page,int rows,String sort,String order) throws Exception{
        sort = Tools.paraNameToDBColumnName(sort);
        String sqlCon = setPayTranUserCardSql(payTranUserCard);
        String sortOrder = sort == null || sort.length()==0?" order by ID desc":(" order by " + sort +("desc".equals(order)?" desc ":" asc "));
        String sql = "select * from (" +
                "  select rownum rowno,tmp1.* from (" +
                "   select tmp.*  from PAY_TRAN_USER_CARD tmp " +(sqlCon.length()==0?"":" where "+ sqlCon) +sortOrder+
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
            setPayTranUserCardParameter(ps,payTranUserCard,n);
            rs = ps.executeQuery();
            while(rs.next()){
                list.add(getPayTranUserCardValue(rs));
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
     * remove PayTranUserCard
     * @param id
     * @throws Exception     
     */
    public void removePayTranUserCard(String id) throws Exception {
        String sql = "delete from PAY_TRAN_USER_CARD where ID=?";
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
     * detail PayTranUserCard
     * @param id
     * @return PayTranUserCard
     * @throws Exception
     */
    public PayTranUserCard detailPayTranUserCard(String id) throws Exception {
        String sql = "select * from PAY_TRAN_USER_CARD where ID=?";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            ps.setString(1,id);
            rs = ps.executeQuery();
            if(rs.next())return getPayTranUserCardValue(rs); 
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(rs, ps, con);
        }
        return null;
    }
    /**
     * update PayTranUserCard
     * @param payTranUserCard
     * @throws Exception
     */
    public void updatePayTranUserCard(PayTranUserCard payTranUserCard) throws Exception {
        String sqlTmp = "";
        if(payTranUserCard.id != null)sqlTmp = sqlTmp + " ID=?,";
        if(payTranUserCard.userId != null)sqlTmp = sqlTmp + " USER_ID=?,";
        if(payTranUserCard.cardBank != null)sqlTmp = sqlTmp + " CARD_BANK=?,";
        if(payTranUserCard.cardType != null)sqlTmp = sqlTmp + " CARD_TYPE=?,";
        if(payTranUserCard.cardStatus != null)sqlTmp = sqlTmp + " CARD_STATUS=?,";
        if(payTranUserCard.cardStaRes != null)sqlTmp = sqlTmp + " CARD_STA_RES=?,";
        if(payTranUserCard.cardNo != null)sqlTmp = sqlTmp + " CARD_NO=?,";
        if(payTranUserCard.cardBankBranch != null)sqlTmp = sqlTmp + " CARD_BANK_BRANCH=?,";
        if(payTranUserCard.bakOpenTime != null)sqlTmp = sqlTmp + " BAK_OPEN_TIME=to_date(?,'yyyy-mm-dd hh24:mi:ss'),";
        if(payTranUserCard.bakOpenNum != null)sqlTmp = sqlTmp + " BAK_OPEN_NUM=?,";
        if(payTranUserCard.bakCloseTime != null)sqlTmp = sqlTmp + " BAK_CLOSE_TIME=to_date(?,'yyyy-mm-dd hh24:mi:ss'),";
        if(payTranUserCard.bakCloseNum != null)sqlTmp = sqlTmp + " BAK_CLOSE_NUM=?,";
        if(payTranUserCard.bakCloseRes != null)sqlTmp = sqlTmp + " BAK_CLOSE_RES=?,";
        if(payTranUserCard.bakUserId != null)sqlTmp = sqlTmp + " BAK_USER_ID=?,";
        if(payTranUserCard.bakUpdTime != null)sqlTmp = sqlTmp + " BAK_UPD_TIME=to_date(?,'yyyy-mm-dd hh24:mi:ss'),";
        if(payTranUserCard.revFlag != null)sqlTmp = sqlTmp + " REV_FLAG=?,";
        if(sqlTmp.length()==0)return;
        String sql = "update PAY_TRAN_USER_CARD "+        
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
            if(payTranUserCard.id != null)ps.setString(n++,payTranUserCard.id);
            if(payTranUserCard.userId != null)ps.setString(n++,payTranUserCard.userId);
            if(payTranUserCard.cardBank != null)ps.setString(n++,payTranUserCard.cardBank);
            if(payTranUserCard.cardType != null)ps.setString(n++,payTranUserCard.cardType);
            if(payTranUserCard.cardStatus != null)ps.setString(n++,payTranUserCard.cardStatus);
            if(payTranUserCard.cardStaRes != null)ps.setString(n++,payTranUserCard.cardStaRes);
            if(payTranUserCard.cardNo != null)ps.setString(n++,payTranUserCard.cardNo);
            if(payTranUserCard.cardBankBranch != null)ps.setString(n++,payTranUserCard.cardBankBranch);
            if(payTranUserCard.bakOpenTime != null)ps.setString(n++,new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payTranUserCard.bakOpenTime));
            if(payTranUserCard.bakOpenNum != null)ps.setLong(n++,payTranUserCard.bakOpenNum);
            if(payTranUserCard.bakCloseTime != null)ps.setString(n++,new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payTranUserCard.bakCloseTime));
            if(payTranUserCard.bakCloseNum != null)ps.setLong(n++,payTranUserCard.bakCloseNum);
            if(payTranUserCard.bakCloseRes != null)ps.setString(n++,payTranUserCard.bakCloseRes);
            if(payTranUserCard.bakUserId != null)ps.setString(n++,payTranUserCard.bakUserId);
            if(payTranUserCard.bakUpdTime != null)ps.setString(n++,new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payTranUserCard.bakUpdTime));
            if(payTranUserCard.revFlag != null)ps.setString(n++,payTranUserCard.revFlag);
            ps.setString(n++,payTranUserCard.id);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }finally {
            close(null, ps, con);
        }
    }
    /**
     * update PayTranUserCard
     * @param payTranUserCard
     * @throws Exception
     */
    public int updatePayTranUserCardForXF(PayTranUserCard payTranUserCard) throws Exception {
        String sql = "update PAY_TRAN_USER_CARD set CARD_STATUS='0' where USER_ID=? and CARD_NO=?"; 
        log.info(sql);
        log.info("USER_ID="+payTranUserCard.userId+";CARD_NO="+payTranUserCard.cardNo);
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            int n=1;
            ps.setString(n++,payTranUserCard.userId);
            ps.setString(n++,payTranUserCard.cardNo);
            return ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }finally {
            close(null, ps, con);
        }
    }
    /**
     * update PayTranUserCard
     * @param payTranUserCard
     * @throws Exception
     */
    public void updatePayTranUserCardInfoForXF(PayTranUserCard payTranUserCard) throws Exception {
        String sql = "update PAY_TRAN_USER_CARD set CARD_BANK=?,CARD_BANK_BRANCH=? where USER_ID=? and CARD_NO=?"; 
        log.info(sql);
        log.info("CARD_BANK="+payTranUserCard.cardBank+";CARD_BANK_BRANCH="+payTranUserCard.cardBankBranch+";USER_ID="+payTranUserCard.userId+
        		";CARD_NO="+payTranUserCard.cardNo);
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            int n=1;
            ps.setString(n++,payTranUserCard.cardBank);
            ps.setString(n++,payTranUserCard.cardBankBranch);
            ps.setString(n++,payTranUserCard.userId);
            ps.setString(n++,payTranUserCard.cardNo);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }finally {
            close(null, ps, con);
        }
    }
}