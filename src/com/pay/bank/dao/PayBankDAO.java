package com.pay.bank.dao;

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
 * Table PAY_BANK DAO. 
 * @author Administrator
 *
 */
public class PayBankDAO  extends BaseDAO {
    private static final Log log = LogFactory.getLog(PayBankDAO.class);
    public static synchronized PayBank getPayBankValue(ResultSet rs)throws SQLException {
        PayBank payBank = new PayBank();
        payBank.id = rs.getString("ID");
        payBank.bankCode = rs.getString("BANK_CODE");
        payBank.bankName = rs.getString("BANK_NAME");
        return payBank;
    }
    public String addPayBank(PayBank payBank) throws Exception {
        String sql = "insert into PAY_BANK("+
            "ID," + 
            "BANK_CODE," + 
            "BANK_NAME)values(?,?,?)";
        log.info(sql);
        log.info("ID="+payBank.id+"BANK_CODE="+payBank.bankCode+";BANK_NAME="+payBank.bankName);
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            int n=1;
            ps.setString(n++,payBank.id);
            ps.setString(n++,payBank.bankCode);
            ps.setString(n++,payBank.bankName);
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
        String sql = "select * from PAY_BANK";
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
                list.add(getPayBankValue(rs));
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
     * @param payBank
     * @return
     */
    private String setPayBankSql(PayBank payBank) {
        StringBuffer sql = new StringBuffer();
        
        String tmp = sql.toString();
        if(tmp.length()>0)tmp=tmp.substring(0,tmp.lastIndexOf(" and "));
        return tmp;
    }
    /**
     * Set query parameter.
     * @param ps
     * @param payBank
     * @param n
     * @return
     * @throws SQLException
     */
    private int setPayBankParameter(PreparedStatement ps,PayBank payBank,int n)throws SQLException {
        return n;
    }
    /**
     * Get records count.
     * @param payBank
     * @return
     */
    public int getPayBankCount(PayBank payBank) {
        String sqlCon = setPayBankSql(payBank);
        String sql = "select count(rownum) recordCount from PAY_BANK " +(sqlCon.length()==0?"":" where "+ sqlCon);
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            int n = 1;
            setPayBankParameter(ps,payBank,n);
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
     * @param payBank
     * @param page
     * @param rows
     * @param sort
     * @param order
     * @return
     * @throws Exception
     */
    public List getPayBankList(PayBank payBank,int page,int rows,String sort,String order) throws Exception{
        sort = Tools.paraNameToDBColumnName(sort);
        String sqlCon = setPayBankSql(payBank);
        String sortOrder = sort == null || sort.length()==0?" order by ID desc":(" order by " + sort +("desc".equals(order)?" desc ":" asc "));
        String sql = "select * from (" +
                "  select rownum rowno,tmp1.* from (" +
                "   select tmp.*  from PAY_BANK tmp " +(sqlCon.length()==0?"":" where "+ sqlCon) +sortOrder+
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
            setPayBankParameter(ps,payBank,n);
            rs = ps.executeQuery();
            while(rs.next()){
                list.add(getPayBankValue(rs));
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
     * remove PayBank
     * @param id
     * @throws Exception     
     */
    public void removePayBank(String id) throws Exception {
        String sql = "delete from PAY_BANK where ID=?";
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
     * detail PayBank
     * @param id
     * @return PayBank
     * @throws Exception
     */
    public PayBank detailPayBank(String id) throws Exception {
        String sql = "select * from PAY_BANK where ID=?";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            ps.setString(1,id);
            rs = ps.executeQuery();
            if(rs.next())return getPayBankValue(rs); 
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(rs, ps, con);
        }
        return null;
    }
    /**
     * update PayBank
     * @param payBank
     * @throws Exception
     */
    public void updatePayBank(PayBank payBank) throws Exception {
        String sqlTmp = "";
        if(payBank.id != null)sqlTmp = sqlTmp + " ID=?,";
        if(payBank.bankCode != null)sqlTmp = sqlTmp + " BANK_CODE=?,";
        if(payBank.bankName != null)sqlTmp = sqlTmp + " BANK_NAME=?,";
        if(sqlTmp.length()==0)return;
        String sql = "update PAY_BANK "+        
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
            if(payBank.id != null)ps.setString(n++,payBank.id);
            if(payBank.bankCode != null)ps.setString(n++,payBank.bankCode);
            if(payBank.bankName != null)ps.setString(n++,payBank.bankName);
            ps.setString(n++,payBank.id);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }finally {
            close(null, ps, con);
        }
    }
    
	public PayBank selectPayBank(PayBank payBank, String str) throws Exception {
		String sql = "select * from PAY_BANK where "+str+" = ? ";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            ps.setString(1,"BANK_CODE".equals(str) ? payBank.getBankCode():payBank.getBankName());
            rs = ps.executeQuery();
            if(rs.next()){
            	PayBank payBankValue = getPayBankValue(rs);
            	if(payBankValue.getId().equals(payBank.getId())) return payBankValue = null ;
				return payBankValue;
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(rs, ps, con);
        }
		return null;
	}
}