package com.pay.cardbin.dao;

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
import com.pay.bank.dao.PayBank;
import com.pay.cardbin.service.PayCardBinService;
/**
 * Table PAY_CARD_BIN DAO. 
 * @author Administrator
 *
 */
public class PayCardBinDAO  extends BaseDAO {
    private static final Log log = LogFactory.getLog(PayCardBinDAO.class);
    public static synchronized PayCardBin getPayCardBinValue(ResultSet rs)throws SQLException {
        PayCardBin payCardBin = new PayCardBin();
        payCardBin.binId = rs.getString("BIN_ID");
        payCardBin.bankCode = rs.getString("BANK_CODE");
        payCardBin.cardType = rs.getString("CARD_TYPE");
        payCardBin.cardName = rs.getString("CARD_NAME");
        payCardBin.binNo = rs.getString("BIN_NO");
        payCardBin.cardLength = rs.getLong("CARD_LENGTH");
        payCardBin.bankNo = rs.getString("BANK_NO");
        payCardBin.extensions = rs.getString("EXTENSIONS");
        payCardBin.enableFlag = rs.getString("ENABLE_FLAG");
        payCardBin.memo = rs.getString("MEMO");
        payCardBin.version = rs.getString("VERSION");
        payCardBin.gmtCreate = rs.getTimestamp("GMT_CREATE");
        payCardBin.gmtModify = rs.getTimestamp("GMT_MODIFY");
        payCardBin.bankName = rs.getString("BANK_NAME");
        return payCardBin;
    }
    public String addPayCardBin(PayCardBin payCardBin) throws Exception {
        String sql = "insert into PAY_CARD_BIN("+
            "BIN_ID," + 
            "BANK_CODE," + 
            "CARD_TYPE," + 
            "CARD_NAME," + 
            "BIN_NO," + 
            "CARD_LENGTH," + 
            "BANK_NAME)values((select max(to_number(BIN_ID))+1 from PAY_CARD_BIN),?,?,?,?,?,?)";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            int n=1;
            ps.setString(n++,payCardBin.bankCode);
            ps.setString(n++,payCardBin.cardType);
            ps.setString(n++,payCardBin.cardName);
            ps.setString(n++,payCardBin.binNo);
            ps.setLong(n++,payCardBin.cardLength);
            ps.setString(n++,payCardBin.bankName);
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
     * @param payCardBin
     * @return
     */
    private String setPayCardBinSql(PayCardBin payCardBin) {
        StringBuffer sql = new StringBuffer();
        
        if(payCardBin.bankCode != null && payCardBin.bankCode.length() !=0) {
            sql.append(" BANK_CODE = ? and ");
        }
        if(payCardBin.bankName != null && payCardBin.bankName.length() !=0) {
            sql.append(" BANK_NAME = ? and ");
        }
        if(payCardBin.cardName != null && payCardBin.cardName.length() !=0) {
            sql.append(" CARD_NAME = ? and ");
        }
        if(payCardBin.binNo != null && payCardBin.binNo.length() !=0) {
            sql.append(" BIN_NO = ? and ");
        }
        String tmp = sql.toString();
        if(tmp.length()>0)tmp=tmp.substring(0,tmp.lastIndexOf(" and "));
        return tmp;
    }
    /**
     * Set query parameter.
     * @param ps
     * @param payCardBin
     * @param n
     * @return
     * @throws SQLException
     */
    private int setPayCardBinParameter(PreparedStatement ps,PayCardBin payCardBin,int n)throws SQLException {
        if(payCardBin.bankCode != null && payCardBin.bankCode.length() !=0) {
            ps.setString(n++,payCardBin.bankCode);
        }
        if(payCardBin.bankName != null && payCardBin.bankName.length() !=0) {
            ps.setString(n++,payCardBin.bankName);
        }
        if(payCardBin.cardName != null && payCardBin.cardName.length() !=0) {
            ps.setString(n++,payCardBin.cardName);
        }
        if(payCardBin.binNo != null && payCardBin.binNo.length() !=0) {
            ps.setString(n++,payCardBin.binNo);
        }
        return n;
    }
    /**
     * Get records count.
     * @param payCardBin
     * @return
     */
    public int getPayCardBinCount(PayCardBin payCardBin) {
        String sqlCon = setPayCardBinSql(payCardBin);
        String sql = "select count(rownum) recordCount from PAY_CARD_BIN " +(sqlCon.length()==0?"":" where "+ sqlCon);
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            int n = 1;
            setPayCardBinParameter(ps,payCardBin,n);
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
     * @param payCardBin
     * @param page
     * @param rows
     * @param sort
     * @param order
     * @return
     * @throws Exception
     */
    public List getPayCardBinList(PayCardBin payCardBin,int page,int rows,String sort,String order) throws Exception{
        sort = Tools.paraNameToDBColumnName(sort);
        String sqlCon = setPayCardBinSql(payCardBin);
        String sortOrder = sort == null || sort.length()==0?" order by GMT_MODIFY desc":(" order by " + sort +("desc".equals(order)?" desc ":" asc "));
        String sql = "select * from (" +
                "  select rownum rowno,tmp1.* from (" +
                "   select tmp.*  from PAY_CARD_BIN tmp " +(sqlCon.length()==0?"":" where "+ sqlCon) +sortOrder+
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
            setPayCardBinParameter(ps,payCardBin,n);
            rs = ps.executeQuery();
            while(rs.next()){
                list.add(getPayCardBinValue(rs));
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(rs, ps, con);
        }
    }
    public Map getCardBinMap() throws Exception{        
        String sql = "select * from PAY_CARD_BIN";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Map map = new HashMap();
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while(rs.next()){            	
            	PayCardBin bin = getPayCardBinValue(rs);
            	map.put(bin.binNo+","+bin.cardLength, bin);
            }
            return map;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(rs, ps, con);
        }
    }
    public List getBankList() throws Exception{
        String sql = "select * from PAY_BANK order by id";
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
            	PayBank bank = new PayBank();
            	bank.bankCode = rs.getString("BANK_CODE");
            	bank.bankName = rs.getString("BANK_NAME");
            	bank.bankNo = rs.getString("BANK_NO");
            	PayCardBinService.BANK_CODE_NAME_MAP.put(bank.bankCode, bank.bankName);
            	PayCardBinService.BANK_NAME_MAP.put(bank.bankName, bank);
                list.add(bank);
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(rs, ps, con);
        }
    }
    public List getList() throws Exception{
        String sql = "select * from PAY_CARD_BIN";
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
                list.add(getPayCardBinValue(rs));
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
     * remove PayCardBin
     * @param binId
     * @throws Exception     
     */
    public void removePayCardBin(String binId) throws Exception {
        String sql = "delete from PAY_CARD_BIN where BIN_ID=?";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            ps.setString(1,binId);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(null, ps, con);
        }
    }
    /**
     * detail PayCardBin
     * @param binId
     * @return PayCardBin
     * @throws Exception
     */
    public PayCardBin detailPayCardBin(String binId) throws Exception {
        String sql = "select * from PAY_CARD_BIN where BIN_ID=?";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            ps.setString(1,binId);
            rs = ps.executeQuery();
            if(rs.next())return getPayCardBinValue(rs); 
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(rs, ps, con);
        }
        return null;
    }
    /**
     * update PayCardBin
     * @param payCardBin
     * @throws Exception
     */
    public void updatePayCardBin(PayCardBin payCardBin) throws Exception {
        String sqlTmp = "";
        if(payCardBin.bankCode != null)sqlTmp = sqlTmp + " BANK_CODE=?,";
        if(payCardBin.cardType != null)sqlTmp = sqlTmp + " CARD_TYPE=?,";
        if(payCardBin.cardName != null)sqlTmp = sqlTmp + " CARD_NAME=?,";
        if(payCardBin.binNo != null)sqlTmp = sqlTmp + " BIN_NO=?,";
        if(payCardBin.cardLength != null)sqlTmp = sqlTmp + " CARD_LENGTH=?,";
        if(payCardBin.bankNo != null)sqlTmp = sqlTmp + " BANK_NO=?,";
        if(payCardBin.version != null)sqlTmp = sqlTmp + " VERSION=?,";
        if(payCardBin.gmtModify != null)sqlTmp = sqlTmp + " GMT_MODIFY=to_date(?,'yyyy-mm-dd hh24:mi:ss'),";
        if(payCardBin.bankName != null)sqlTmp = sqlTmp + " BANK_NAME=?,";
        if(sqlTmp.length()==0)return;
        String sql = "update PAY_CARD_BIN "+        
              "set " + 
              sqlTmp.substring(0,sqlTmp.length()-1) + 
            " where BIN_ID=?"; 
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            int n=1;
            if(payCardBin.bankCode != null)ps.setString(n++,payCardBin.bankCode);
            if(payCardBin.cardType != null)ps.setString(n++,payCardBin.cardType);
            if(payCardBin.cardName != null)ps.setString(n++,payCardBin.cardName);
            if(payCardBin.binNo != null)ps.setString(n++,payCardBin.binNo);
            if(payCardBin.cardLength != null)ps.setLong(n++,payCardBin.cardLength);
            if(payCardBin.bankNo != null)ps.setString(n++,payCardBin.bankNo);
            if(payCardBin.version != null)ps.setString(n++,payCardBin.version);
            if(payCardBin.gmtModify != null)ps.setString(n++,new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payCardBin.gmtModify));
            if(payCardBin.bankName != null)ps.setString(n++,payCardBin.bankName);
            ps.setString(n++,payCardBin.binId);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }finally {
            close(null, ps, con);
        }
    }
}