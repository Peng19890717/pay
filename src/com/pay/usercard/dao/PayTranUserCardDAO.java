package com.pay.usercard.dao;

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
        if(payTranUserCard.cardNo != null && payTranUserCard.cardNo.length() !=0) {
            sql.append(" CARD_NO = ? and ");
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
        if(payTranUserCard.cardNo != null && payTranUserCard.cardNo.length() !=0) {
            ps.setString(n++,payTranUserCard.cardNo);
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
        String sql = "select * from (" +
                "  select rownum rowno,tmp1.* from (" +
                "   select tmp.*  from PAY_TRAN_USER_CARD tmp " +(sqlCon.length()==0?"":" where "+ sqlCon) +" order by BAK_OPEN_TIME desc"+
                "  ) tmp1 "+
                ")  where rowno > "+((page-1)*rows)+ " and rowno<= " + (page*rows) + " order by BAK_OPEN_TIME desc";
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
}