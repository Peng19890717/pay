package com.pay.account.dao;

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
import com.pay.coopbank.dao.PayCoopBank;
/**
 * Table PAY_BANK_ACCOUNT_CHECK DAO. 
 * @author Administrator
 *
 */
public class PayBankAccountCheckDAO  extends BaseDAO {
    private static final Log log = LogFactory.getLog(PayBankAccountCheckDAO.class);
    public static synchronized PayBankAccountCheck getPayBankAccountCheckValue(ResultSet rs)throws SQLException {
        PayBankAccountCheck payBankAccountCheck = new PayBankAccountCheck();
        payBankAccountCheck.bankcode = rs.getString("BANKCODE");
        try {payBankAccountCheck.bankName = rs.getString("BANK_NAME");} catch (Exception e) {}
        payBankAccountCheck.actdate = rs.getString("ACTDATE");
        payBankAccountCheck.bnkordno = rs.getString("BNKORDNO");
        payBankAccountCheck.cwtype = rs.getString("CWTYPE");
        payBankAccountCheck.chktype = rs.getString("CHKTYPE");
        payBankAccountCheck.bankdate = rs.getString("BANKDATE");
        payBankAccountCheck.banklogno = rs.getString("BANKLOGNO");
        payBankAccountCheck.ccycod = rs.getString("CCYCOD");
        payBankAccountCheck.txnamt = rs.getLong("TXNAMT");
        payBankAccountCheck.fee = rs.getLong("FEE");
        payBankAccountCheck.inamt = rs.getLong("INAMT");
        payBankAccountCheck.bkglogno = rs.getString("BKGLOGNO");
        payBankAccountCheck.bkgdate = rs.getString("BKGDATE");
        payBankAccountCheck.payactno = rs.getString("PAYACTNO");
        payBankAccountCheck.mercode = rs.getString("MERCODE");
        try {payBankAccountCheck.merName = rs.getString("STORE_NAME");} catch (Exception e) {}
        payBankAccountCheck.godordcode = rs.getString("GODORDCODE");
        payBankAccountCheck.bkgtxncod = rs.getString("BKGTXNCOD");
        payBankAccountCheck.bkgtxndate = rs.getString("BKGTXNDATE");
        payBankAccountCheck.bkgtxntim = rs.getString("BKGTXNTIM");
        payBankAccountCheck.chknum = rs.getLong("CHKNUM");
        payBankAccountCheck.srefno = rs.getString("SREFNO");
        payBankAccountCheck.filed1 = rs.getString("FILED1");
        payBankAccountCheck.filed2 = rs.getString("FILED2");
        payBankAccountCheck.status = rs.getString("STATUS");
        payBankAccountCheck.lastUpdateUser = rs.getString("LAST_UPDATE_USER");
        payBankAccountCheck.lastUpdateTime = rs.getTimestamp("LAST_UPDATE_TIME");
        payBankAccountCheck.remark = rs.getString("REMARK");
        payBankAccountCheck.orderTime = rs.getDate("ORDER_TIME");
        payBankAccountCheck.payTime = rs.getDate("PAY_TIME");
        payBankAccountCheck.totalAmount = payBankAccountCheck.fee+payBankAccountCheck.inamt;
        return payBankAccountCheck;
    }
    public static synchronized PayBankAccountSum getPayBankAccountSumValue(ResultSet rs)throws SQLException {
    	PayBankAccountSum accountSum = new PayBankAccountSum();
    	try{accountSum.bankcode=rs.getString("BANKCODE");}catch(Exception e){}
    	try{accountSum.bankName=rs.getString("BANK_NAME");}catch(Exception e){}
    	try{accountSum.actdate=rs.getString("ACTDATE");}catch(Exception e){}
    	try{accountSum.cwType=rs.getString("CWTYPE");}catch(Exception e){}
    	try{accountSum.cwCount=rs.getLong("CW_COUNT");}catch(Exception e){}
    	try{accountSum.cwAmt=rs.getLong("CW_AMT");}catch(Exception e){}
    	try{accountSum.chktype=rs.getString("chktype");}catch(Exception e){}
    	try{accountSum.sumCount=rs.getLong("SUM_COUNT");}catch(Exception e){}
    	try{accountSum.sumAmt=rs.getLong("SUM_AMT");}catch(Exception e){}
    	try{accountSum.sucCount=rs.getLong("sucCount");}catch(Exception e){}
    	try{accountSum.sucAmt=rs.getLong("sucAmt");}catch(Exception e){}
    	try{accountSum.failCount=rs.getLong("failCount");}catch(Exception e){}
    	try{accountSum.failAmt=rs.getLong("failAmt");}catch(Exception e){}
        return accountSum;
    }
    public String addPayBankAccountCheck(PayBankAccountCheck payBankAccountCheck) throws Exception {
        String sql = "insert into PAY_BANK_ACCOUNT_CHECK("+
            "BANKCODE," + 
            "ACTDATE," + 
            "BNKORDNO," + 
            "CWTYPE," + 
            "CHKTYPE," + 
            "BANKDATE," + 
            "BANKLOGNO," + 
            "CCYCOD," + 
            "TXNAMT," + 
            "FEE," + 
            "INAMT," + 
            "BKGLOGNO," + 
            "BKGDATE," + 
            "PAYACTNO," + 
            "MERCODE," + 
            "GODORDCODE," + 
            "BKGTXNCOD," + 
            "BKGTXNDATE," + 
            "BKGTXNTIM," + 
            "CHKNUM," + 
            "SREFNO," + 
            "FILED1," + 
            "FILED2," + 
            "STATUS," + 
            "LAST_UPDATE_USER," + 
            "LAST_UPDATE_TIME," + 
            "REMARK)values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,to_date(?,'yyyy-mm-dd hh24:mi:ss'),?)";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            int n=1;
            ps.setString(n++,payBankAccountCheck.bankcode);
            ps.setString(n++,payBankAccountCheck.actdate);
            ps.setString(n++,payBankAccountCheck.bnkordno);
            ps.setString(n++,payBankAccountCheck.cwtype);
            ps.setString(n++,payBankAccountCheck.chktype);
            ps.setString(n++,payBankAccountCheck.bankdate);
            ps.setString(n++,payBankAccountCheck.banklogno);
            ps.setString(n++,payBankAccountCheck.ccycod);
            ps.setLong(n++,payBankAccountCheck.txnamt);
            ps.setLong(n++,payBankAccountCheck.fee);
            ps.setLong(n++,payBankAccountCheck.inamt);
            ps.setString(n++,payBankAccountCheck.bkglogno);
            ps.setString(n++,payBankAccountCheck.bkgdate);
            ps.setString(n++,payBankAccountCheck.payactno);
            ps.setString(n++,payBankAccountCheck.mercode);
            ps.setString(n++,payBankAccountCheck.godordcode);
            ps.setString(n++,payBankAccountCheck.bkgtxncod);
            ps.setString(n++,payBankAccountCheck.bkgtxndate);
            ps.setString(n++,payBankAccountCheck.bkgtxntim);
            ps.setLong(n++,payBankAccountCheck.chknum);
            ps.setString(n++,payBankAccountCheck.srefno);
            ps.setString(n++,payBankAccountCheck.filed1);
            ps.setString(n++,payBankAccountCheck.filed2);
            ps.setString(n++,payBankAccountCheck.status);
            ps.setString(n++,payBankAccountCheck.lastUpdateUser);
            ps.setString(n++,new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payBankAccountCheck.lastUpdateTime));
            ps.setString(n++,payBankAccountCheck.remark);
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
        String sql = "select * from PAY_BANK_ACCOUNT_CHECK";
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
                list.add(getPayBankAccountCheckValue(rs));
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
     * @param payBankAccountCheck
     * @return
     */
    private String setPayBankAccountSumSql(PayBankAccountSum payBankAccountSum) {
        StringBuffer sql = new StringBuffer();
        if(payBankAccountSum.bankcode != null && payBankAccountSum.bankcode.length() !=0) {
            sql.append(" BANKCODE = ? and ");
        }
        if(payBankAccountSum.bankName != null && payBankAccountSum.bankName.length() !=0) {
        	sql.append(" BANK_NAME like ? and ");
        }
        if(payBankAccountSum.actdateStart != null && payBankAccountSum.actdateStart.length() !=0) {
            sql.append(" ACTDATE >= ? and ");
        }
        if(payBankAccountSum.actdateEnd != null && payBankAccountSum.actdateEnd.length() !=0) {
            sql.append(" ACTDATE <= ? and ");
        }
        if(payBankAccountSum.chktype != null && payBankAccountSum.chktype.length() !=0) {
            sql.append(" CHKTYPE = ? and ");
        }
        String tmp = sql.toString();
        if(tmp.length()>0)tmp=tmp.substring(0,tmp.lastIndexOf(" and "));
        return tmp;
    }
    /**
     * Set query parameter.
     * @param ps
     * @param payBankAccountCheck
     * @param n
     * @return
     * @throws SQLException
     */
    private int setPayBankAccountSumParameter(PreparedStatement ps,PayBankAccountSum payBankAccountSum,int n)throws SQLException {
    	if(payBankAccountSum.bankcode != null && payBankAccountSum.bankcode.length() !=0) {
            ps.setString(n++,payBankAccountSum.bankcode);
        }
    	if(payBankAccountSum.bankName != null && payBankAccountSum.bankName.length() !=0) {
    		ps.setString(n++,"%"+payBankAccountSum.bankName+"%");
    	}
    	if(payBankAccountSum.actdateStart != null && payBankAccountSum.actdateStart.length() !=0) {
            ps.setString(n++,payBankAccountSum.actdateStart.replaceAll("-",""));
        }
    	if(payBankAccountSum.actdateEnd != null && payBankAccountSum.actdateEnd.length() !=0) {
            ps.setString(n++,payBankAccountSum.actdateEnd.replaceAll("-",""));
        }
    	 if(payBankAccountSum.chktype != null && payBankAccountSum.chktype.length() !=0) {
             ps.setString(n++,payBankAccountSum.chktype);
         }
        return n;
    }
    /**
     * Get records count.
     * @param payBankAccountCheck
     * @return
     */
    public int getPayBankAccountSumCount(PayBankAccountSum payBankAccountSum) {
        String sqlCon = setPayBankAccountSumSql(payBankAccountSum);
        String sql = "select count(*)recordCount from(select  count(ACTDATE) from ((" +
        		"select tmp.*,tmp1.BANK_NAME  from PAY_BANK_ACCOUNT_CHECK tmp left join PAY_COOP_BANK tmp1 on tmp.BANKCODE=tmp1.BANK_CODE " +(sqlCon.length()==0?"":" where "+ sqlCon)+
        		")tmp) group by ACTDATE, BANKCODE) tmp1";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            int n = 1;
            setPayBankAccountSumParameter(ps,payBankAccountSum,n);
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
     * @param payBankAccountCheck
     * @param page
     * @param rows
     * @param sort
     * @param order
     * @return
     * @throws Exception
     */
    public List getPayBankAccountSumList(PayBankAccountSum payBankAccountSum,int page,int rows,String sort,String order) throws Exception{
        sort = Tools.paraNameToDBColumnName(sort);
        String sqlCon = setPayBankAccountSumSql(payBankAccountSum);
        order = " order by ACTDATE desc,BANKCODE asc";
        String sql = "select acc.*,bank.BANK_NAME from(" +
        		"select * from (" +
                "  select rownum rowno,tmp2.* from (" +
	                "select  count(ACTDATE)SUM_COUNT,sum(TXNAMT)SUM_AMT,ACTDATE,BANKCODE from (" +
	                "   select tmp.*,tmp1.BANK_NAME  from PAY_BANK_ACCOUNT_CHECK tmp left join PAY_COOP_BANK tmp1 on tmp.BANKCODE=tmp1.BANK_CODE " +(sqlCon.length()==0?"":" where "+ sqlCon) +
	                ") tmp1  group by ACTDATE, BANKCODE "+order+")tmp2" +
                ")  where rowno > "+((page-1)*rows)+ " and rowno<= " + (page*rows) + order+
                ") acc left join PAY_COOP_BANK bank on acc.BANKCODE=bank.BANK_CODE";
        String tmp = "";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List list = new ArrayList();
        Map map = new HashMap();
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            int n = 1;
            setPayBankAccountSumParameter(ps,payBankAccountSum,n);
            rs = ps.executeQuery();
            while(rs.next()){
            	PayBankAccountSum accSum = getPayBankAccountSumValue(rs); 
                list.add(accSum);
                map.put(accSum.actdate+","+accSum.bankcode,accSum);
                tmp = tmp +" (ACTDATE='"+accSum.actdate+"' and BANKCODE='"+accSum.bankcode+"') or ";
            }
            if(tmp.length() >0){
            	tmp = tmp.substring(0,tmp.length()-4);
            	rs.close();
                ps.close();
                sql = "select count(ACTDATE) CW_COUNT,sum(TXNAMT) CW_AMT,ACTDATE,BANKCODE,CWTYPE from(" +
                		"select *  from PAY_BANK_ACCOUNT_CHECK where "+tmp+
                		")PAY_BANK_ACCOUNT_CHECK group by ACTDATE, BANKCODE, CWTYPE";
                log.info(sql);
                ps = con.prepareStatement(sql);
                rs = ps.executeQuery();
                while(rs.next()){
                	PayBankAccountSum accSum1 = getPayBankAccountSumValue(rs);
                	PayBankAccountSum accSum = (PayBankAccountSum)map.get(accSum1.actdate+","+accSum1.bankcode);
                    if("0".equals(accSum1.cwType)){
                    	accSum.sucCount=accSum1.cwCount;
                    	accSum.sucAmt = accSum1.cwAmt;
                    } else {
                    	accSum.failCount=accSum.failCount+accSum1.cwCount;
                    	accSum.failAmt = accSum.failAmt+accSum1.cwAmt;
                    }
                }
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
     * @param payBankAccountCheck
     * @return
     */
    private String setPayBankAccountCheckSql(PayBankAccountCheck payBankAccountCheck) {
        StringBuffer sql = new StringBuffer();
        
        if(payBankAccountCheck.bankcode != null && payBankAccountCheck.bankcode.length() !=0) {
            sql.append(" BANKCODE = ? and ");
        }
        if(payBankAccountCheck.godordcode != null && payBankAccountCheck.godordcode.length() !=0) {
            sql.append(" GODORDCODE = ? and ");
        }
        if(payBankAccountCheck.merName != null && payBankAccountCheck.merName.length() !=0) {
            sql.append(" STORE_NAME like ? and ");
        }
        if(payBankAccountCheck.bnkordno != null && payBankAccountCheck.bnkordno.length() !=0) {
            sql.append(" BNKORDNO = ? and ");
        }
        if(payBankAccountCheck.chktype != null && payBankAccountCheck.chktype.length() !=0) {
            sql.append(" CHKTYPE = ? and ");
        }
        if(payBankAccountCheck.cwtype != null && payBankAccountCheck.cwtype.length() !=0) {
        	if("X".equals(payBankAccountCheck.cwtype))sql.append(" (CWTYPE != '0' and CWTYPE != '-1') and ");
        	else sql.append(" CWTYPE = ? and ");
        }
        if(payBankAccountCheck.status != null && payBankAccountCheck.status.length() !=0) {
            sql.append(" STATUS = ? and ");
        }
        if(payBankAccountCheck.actdateStart != null && payBankAccountCheck.actdateStart.length() !=0) {
            sql.append(" ACTDATE >= ? and ");
        }
        if(payBankAccountCheck.actdateEnd != null && payBankAccountCheck.actdateEnd.length() !=0) {
            sql.append(" ACTDATE <= ? and ");
        }
        String tmp = sql.toString();
        if(tmp.length()>0)tmp=tmp.substring(0,tmp.lastIndexOf(" and "));
        return tmp;
    }
    /**
     * Set query parameter.
     * @param ps
     * @param payBankAccountCheck
     * @param n
     * @return
     * @throws SQLException
     */
    private int setPayBankAccountCheckParameter(PreparedStatement ps,PayBankAccountCheck payBankAccountCheck,int n)throws SQLException {
        if(payBankAccountCheck.bankcode != null && payBankAccountCheck.bankcode.length() !=0) {
            ps.setString(n++,payBankAccountCheck.bankcode);
        }
        if(payBankAccountCheck.godordcode != null && payBankAccountCheck.godordcode.length() !=0) {
            ps.setString(n++,payBankAccountCheck.godordcode);
        }
        if(payBankAccountCheck.merName != null && payBankAccountCheck.merName.length() !=0) {
            ps.setString(n++,"%"+payBankAccountCheck.merName+"%");
        }
        if(payBankAccountCheck.bnkordno != null && payBankAccountCheck.bnkordno.length() !=0) {
            ps.setString(n++,payBankAccountCheck.bnkordno);
        }
        if(payBankAccountCheck.chktype != null && payBankAccountCheck.chktype.length() !=0) {
            ps.setString(n++,payBankAccountCheck.chktype);
        }
        if(payBankAccountCheck.cwtype != null && payBankAccountCheck.cwtype.length() !=0) {
        	if("X".equals(payBankAccountCheck.cwtype));
        	else ps.setString(n++,payBankAccountCheck.cwtype);
        }
        if(payBankAccountCheck.status != null && payBankAccountCheck.status.length() !=0) {
            ps.setString(n++,payBankAccountCheck.status);
        }
        if(payBankAccountCheck.actdateStart != null && payBankAccountCheck.actdateStart.length() !=0) {
            ps.setString(n++,payBankAccountCheck.actdateStart.replaceAll("-",""));
        }
    	if(payBankAccountCheck.actdateEnd != null && payBankAccountCheck.actdateEnd.length() !=0) {
            ps.setString(n++,payBankAccountCheck.actdateEnd.replaceAll("-",""));
        }
        return n;
    }
    /**
     * Get records count.
     * @param payBankAccountCheck
     * @return
     */
    public int getPayBankAccountCheckCount(PayBankAccountCheck payBankAccountCheck) {
        String sqlCon = setPayBankAccountCheckSql(payBankAccountCheck);
        String sql = "select count(rownum) recordCount from (select pbac.*,mer.STORE_NAME,bank.BANK_NAME from PAY_BANK_ACCOUNT_CHECK pbac " +
                		" left join PAY_MERCHANT mer on pbac.mercode=mer.CUST_ID " +
                		" left join PAY_COOP_BANK bank on pbac.BANKCODE=bank.BANK_CODE)" +
                		"PAY_BANK_ACCOUNT_CHECK " +(sqlCon.length()==0?"":" where "+ sqlCon);
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            int n = 1;
            setPayBankAccountCheckParameter(ps,payBankAccountCheck,n);
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
     * @param payBankAccountCheck
     * @param page
     * @param rows
     * @param sort
     * @param order
     * @return
     * @throws Exception
     */
    public List getPayBankAccountCheckList(PayBankAccountCheck payBankAccountCheck,int page,int rows,String sort,String order) throws Exception{
        sort = Tools.paraNameToDBColumnName(sort);
        String sqlCon = setPayBankAccountCheckSql(payBankAccountCheck);
        String sortOrder = sort == null || sort.length()==0?" order by ACTDATE desc":(" order by " + sort +("desc".equals(order)?" desc ":" asc "));
        String sql = "select * from (" +
                "  select rownum rowno,tmp1.* from (" +
                "   select tmp.*  from " +
                		" (select pbac.*,mer.STORE_NAME,bank.BANK_NAME from PAY_BANK_ACCOUNT_CHECK pbac " +
                		" left join PAY_MERCHANT mer on pbac.mercode=mer.CUST_ID " +
                		" left join PAY_COOP_BANK bank on pbac.BANKCODE=bank.BANK_CODE) tmp " +(sqlCon.length()==0?"":" where "+ sqlCon) +sortOrder+
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
            setPayBankAccountCheckParameter(ps,payBankAccountCheck,n);
            rs = ps.executeQuery();
            while(rs.next()){
                list.add(getPayBankAccountCheckValue(rs));
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
     * remove PayBankAccountCheck
     * @param bankcode
     * @throws Exception     
     */
    public void removePayBankAccountCheck(String bankcode) throws Exception {
        String sql = "delete from PAY_BANK_ACCOUNT_CHECK where BANKCODE=?";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            ps.setString(1,bankcode);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(null, ps, con);
        }
    }
    /**
     * detail PayBankAccountCheck
     * @param bankcode
     * @return PayBankAccountCheck
     * @throws Exception
     */
    public PayBankAccountCheck detailPayBankAccountCheck(String bankcode) throws Exception {
        String sql = "select * from PAY_BANK_ACCOUNT_CHECK where BANKCODE=?";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            ps.setString(1,bankcode);
            rs = ps.executeQuery();
            if(rs.next())return getPayBankAccountCheckValue(rs); 
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(rs, ps, con);
        }
        return null;
    }
    /**
     * update PayBankAccountCheck
     * @param payBankAccountCheck
     * @throws Exception
     */
    public void updatePayBankAccountCheck(PayBankAccountCheck payBankAccountCheck) throws Exception {
        String sqlTmp = "";
        if(payBankAccountCheck.bankcode != null)sqlTmp = sqlTmp + " BANKCODE=?,";
        if(payBankAccountCheck.actdate != null)sqlTmp = sqlTmp + " ACTDATE=?,";
        if(payBankAccountCheck.bnkordno != null)sqlTmp = sqlTmp + " BNKORDNO=?,";
        if(payBankAccountCheck.cwtype != null)sqlTmp = sqlTmp + " CWTYPE=?,";
        if(payBankAccountCheck.chktype != null)sqlTmp = sqlTmp + " CHKTYPE=?,";
        if(payBankAccountCheck.bankdate != null)sqlTmp = sqlTmp + " BANKDATE=?,";
        if(payBankAccountCheck.banklogno != null)sqlTmp = sqlTmp + " BANKLOGNO=?,";
        if(payBankAccountCheck.ccycod != null)sqlTmp = sqlTmp + " CCYCOD=?,";
        if(payBankAccountCheck.txnamt != null)sqlTmp = sqlTmp + " TXNAMT=?,";
        if(payBankAccountCheck.fee != null)sqlTmp = sqlTmp + " FEE=?,";
        if(payBankAccountCheck.inamt != null)sqlTmp = sqlTmp + " INAMT=?,";
        if(payBankAccountCheck.bkglogno != null)sqlTmp = sqlTmp + " BKGLOGNO=?,";
        if(payBankAccountCheck.bkgdate != null)sqlTmp = sqlTmp + " BKGDATE=?,";
        if(payBankAccountCheck.payactno != null)sqlTmp = sqlTmp + " PAYACTNO=?,";
        if(payBankAccountCheck.mercode != null)sqlTmp = sqlTmp + " MERCODE=?,";
        if(payBankAccountCheck.godordcode != null)sqlTmp = sqlTmp + " GODORDCODE=?,";
        if(payBankAccountCheck.bkgtxncod != null)sqlTmp = sqlTmp + " BKGTXNCOD=?,";
        if(payBankAccountCheck.bkgtxndate != null)sqlTmp = sqlTmp + " BKGTXNDATE=?,";
        if(payBankAccountCheck.bkgtxntim != null)sqlTmp = sqlTmp + " BKGTXNTIM=?,";
        if(payBankAccountCheck.chknum != null)sqlTmp = sqlTmp + " CHKNUM=?,";
        if(payBankAccountCheck.srefno != null)sqlTmp = sqlTmp + " SREFNO=?,";
        if(payBankAccountCheck.filed1 != null)sqlTmp = sqlTmp + " FILED1=?,";
        if(payBankAccountCheck.filed2 != null)sqlTmp = sqlTmp + " FILED2=?,";
        if(payBankAccountCheck.status != null)sqlTmp = sqlTmp + " STATUS=?,";
        if(payBankAccountCheck.lastUpdateUser != null)sqlTmp = sqlTmp + " LAST_UPDATE_USER=?,";
        if(payBankAccountCheck.lastUpdateTime != null)sqlTmp = sqlTmp + " LAST_UPDATE_TIME=to_date(?,'yyyy-mm-dd hh24:mi:ss'),";
        if(payBankAccountCheck.remark != null)sqlTmp = sqlTmp + " REMARK=?,";
        if(sqlTmp.length()==0)return;
        String sql = "update PAY_BANK_ACCOUNT_CHECK "+        
              "set " + 
              sqlTmp.substring(0,sqlTmp.length()-1) + 
            " where BANKCODE=?"; 
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            int n=1;
            if(payBankAccountCheck.bankcode != null)ps.setString(n++,payBankAccountCheck.bankcode);
            if(payBankAccountCheck.actdate != null)ps.setString(n++,payBankAccountCheck.actdate);
            if(payBankAccountCheck.bnkordno != null)ps.setString(n++,payBankAccountCheck.bnkordno);
            if(payBankAccountCheck.cwtype != null)ps.setString(n++,payBankAccountCheck.cwtype);
            if(payBankAccountCheck.chktype != null)ps.setString(n++,payBankAccountCheck.chktype);
            if(payBankAccountCheck.bankdate != null)ps.setString(n++,payBankAccountCheck.bankdate);
            if(payBankAccountCheck.banklogno != null)ps.setString(n++,payBankAccountCheck.banklogno);
            if(payBankAccountCheck.ccycod != null)ps.setString(n++,payBankAccountCheck.ccycod);
            if(payBankAccountCheck.txnamt != null)ps.setLong(n++,payBankAccountCheck.txnamt);
            if(payBankAccountCheck.fee != null)ps.setLong(n++,payBankAccountCheck.fee);
            if(payBankAccountCheck.inamt != null)ps.setLong(n++,payBankAccountCheck.inamt);
            if(payBankAccountCheck.bkglogno != null)ps.setString(n++,payBankAccountCheck.bkglogno);
            if(payBankAccountCheck.bkgdate != null)ps.setString(n++,payBankAccountCheck.bkgdate);
            if(payBankAccountCheck.payactno != null)ps.setString(n++,payBankAccountCheck.payactno);
            if(payBankAccountCheck.mercode != null)ps.setString(n++,payBankAccountCheck.mercode);
            if(payBankAccountCheck.godordcode != null)ps.setString(n++,payBankAccountCheck.godordcode);
            if(payBankAccountCheck.bkgtxncod != null)ps.setString(n++,payBankAccountCheck.bkgtxncod);
            if(payBankAccountCheck.bkgtxndate != null)ps.setString(n++,payBankAccountCheck.bkgtxndate);
            if(payBankAccountCheck.bkgtxntim != null)ps.setString(n++,payBankAccountCheck.bkgtxntim);
            if(payBankAccountCheck.chknum != null)ps.setLong(n++,payBankAccountCheck.chknum);
            if(payBankAccountCheck.srefno != null)ps.setString(n++,payBankAccountCheck.srefno);
            if(payBankAccountCheck.filed1 != null)ps.setString(n++,payBankAccountCheck.filed1);
            if(payBankAccountCheck.filed2 != null)ps.setString(n++,payBankAccountCheck.filed2);
            if(payBankAccountCheck.status != null)ps.setString(n++,payBankAccountCheck.status);
            if(payBankAccountCheck.lastUpdateUser != null)ps.setString(n++,payBankAccountCheck.lastUpdateUser);
            if(payBankAccountCheck.lastUpdateTime != null)ps.setString(n++,new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payBankAccountCheck.lastUpdateTime));
            if(payBankAccountCheck.remark != null)ps.setString(n++,payBankAccountCheck.remark);
            ps.setString(n++,payBankAccountCheck.bankcode);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }finally {
            close(null, ps, con);
        }
    }
    /**
     * remove PayBankAccountCheck
     * @param bankcode
     * @throws Exception     
     */
    public void clearPayBankAccountCheckTmp() throws Exception {
        String sql = "delete from PAY_BANK_ACCOUNT_CHECK_TMP";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(null, ps, con);
        }
    }
		public void setResultBankAccount(PayBankAccountCheck payBankAccountCheck) throws Exception{
			//Oracle中的||用于字符串的拼接，将原先的备注拼接上新的备注保存到数据库中
			String sql = "update PAY_BANK_ACCOUNT_CHECK set LAST_UPDATE_USER=?," +
	        		" LAST_UPDATE_TIME=sysdate,STATUS=?," +
	        		" REMARK = REMARK||? where BANKCODE=? and ACTDATE=? and BNKORDNO=?";
	        log.info(sql);
	        Connection con = null;
	        PreparedStatement ps = null;
	        try {
	            con = connection();
	            ps = con.prepareStatement(sql);
	            ps.setString(1,payBankAccountCheck.lastUpdateUser);
	            ps.setString(2,payBankAccountCheck.status);
	            ps.setString(3,payBankAccountCheck.remark==null||payBankAccountCheck.remark.length()==0
	            		?"":"；"+payBankAccountCheck.remark);
	            ps.setString(4,payBankAccountCheck.bankcode);
	            //设置时间  传过来的时间格式为2015-12-20
	            ps.setString(5,payBankAccountCheck.actdate.replace("-", ""));
	            ps.setString(6,payBankAccountCheck.bnkordno);
	            ps.executeUpdate();
	        } catch (Exception e) {
	            e.printStackTrace();
	            throw e;
	        } finally {
	            close(null, ps, con);
	        }
		}
	    /**
//     * 交易对账
//     * @param bank
//     * @param checkDate
//     * @throws Exception
//     */
//    public void accountCheckCreate(PayCoopBank bank,String checkDate) throws Exception {
//        String sql = "insert into PAY_BANK_ACCOUNT_CHECK"+ 
//		//正常对账
//		"(select '"+bank.bankCode+"' BANKCODE,acc.ACTDATE,acc.BNKORDNO,'0' CWTYPE,'0' CHKTYPE,to_char(po.BANKSTLDATE,'yyyymmdd') BANKDATE,"+
//		"        acc.BANKLOGNO,po.TXCCY CCYCOD,acc.TXNAMT,acc.FEE,acc.INAMT,'' BKGLOGNO,null BKGDATE,po.BANKPAYACNO PAYACTNO,"+
//		"        po.MERNO MERCODE,po.PRDORDNO GODORDCODE,'' BKGTXNCOD,null BKGTXNDATE,null BKGTXNTIM,1 CHKNUM,'' SREFNO,"+
//		"        '' FILED1,'' FILED2,'0' STATUS,'' LAST_UPDATE_USER,sysdate LAST_UPDATE_TIME,'' REMARK,po.CREATETIME ORDER_TIME,po.ACTDAT PAY_TIME from ("+
//		"  select * from PAY_ORDER where BANKCOD='"+bank.bankCode+"' and ACTDAT>=to_date(?,'yyyy-mm-dd hh24:mi:ss') and ACTDAT<=to_date(?,'yyyy-mm-dd hh24:mi:ss')"+
//		" )po,PAY_BANK_ACCOUNT_CHECK_TMP acc where po.PAYORDNO=acc.bnkordno and po.ORDSTATUS='01')"+
//		"union"+//状态错误
//		"(select '"+bank.bankCode+"' BANKCODE,acc.ACTDATE,acc.BNKORDNO,'1' CWTYPE,'0' CHKTYPE,to_char(po.BANKSTLDATE,'yyyymmdd') BANKDATE,"+
//		"        acc.BANKLOGNO,po.TXCCY CCYCOD,acc.TXNAMT,acc.FEE,acc.INAMT,'' BKGLOGNO,null BKGDATE,po.BANKPAYACNO PAYACTNO,"+
//		"        po.MERNO MERCODE,po.PRDORDNO GODORDCODE,'' BKGTXNCOD,null BKGTXNDATE,null BKGTXNTIM,1 CHKNUM,'' SREFNO,"+
//		"        '' FILED1,'' FILED2,'0' STATUS,'' LAST_UPDATE_USER,sysdate LAST_UPDATE_TIME,'' REMARK,po.CREATETIME ORDER_TIME,po.ACTDAT PAY_TIME from ("+
//		"  select * from PAY_ORDER where BANKCOD='"+bank.bankCode+"' and ACTDAT>=to_date(?,'yyyy-mm-dd hh24:mi:ss') and ACTDAT<=to_date(?,'yyyy-mm-dd hh24:mi:ss')"+
//		" )po,PAY_BANK_ACCOUNT_CHECK_TMP acc where po.ORDSTATUS!='01' and po.PAYORDNO=acc.bnkordno)"+
//		"union"+//银行有系统无
//		" (select '"+bank.bankCode+"' BANKCODE,acc.ACTDATE,acc.BNKORDNO,'2' CWTYPE,'0' CHKTYPE,to_char(po.BANKSTLDATE,'yyyymmdd') BANKDATE,"+
//		"        acc.BANKLOGNO,po.TXCCY CCYCOD,acc.TXNAMT,acc.FEE,acc.INAMT,'' BKGLOGNO,null BKGDATE,po.BANKPAYACNO PAYACTNO,"+
//		"        po.MERNO MERCODE,po.PRDORDNO GODORDCODE,'' BKGTXNCOD,null BKGTXNDATE,null BKGTXNTIM,1 CHKNUM,'' SREFNO,"+
//		"        '' FILED1,'' FILED2,'0' STATUS,'' LAST_UPDATE_USER,sysdate LAST_UPDATE_TIME,'' REMARK,po.CREATETIME ORDER_TIME,po.ACTDAT PAY_TIME "+
//		"      from PAY_BANK_ACCOUNT_CHECK_TMP acc left join PAY_ORDER po on acc.BNKORDNO=po.PAYORDNO where po.PAYORDNO is null)"+
//		"union"+//系统有银行无
//		" (select '"+bank.bankCode+"' BANKCODE,to_char(po.ACTDAT,'yyyymmdd') ACTDATE,po.PAYORDNO,'3' CWTYPE,'0' CHKTYPE,to_char(po.BANKSTLDATE,'yyyymmdd') BANKDATE,"+
//		"        acc.BANKLOGNO,po.TXCCY CCYCOD,po.TXAMT,po.FEE,po.NETRECAMT,'' BKGLOGNO,null BKGDATE,po.BANKPAYACNO PAYACTNO,"+
//		"        po.MERNO MERCODE,po.PRDORDNO GODORDCODE,'' BKGTXNCOD,null BKGTXNDATE,null BKGTXNTIM,1 CHKNUM,'' SREFNO,"+
//		"        '' FILED1,'' FILED2,'0' STATUS,'' LAST_UPDATE_USER,sysdate LAST_UPDATE_TIME,'' REMARK,po.CREATETIME ORDER_TIME,po.ACTDAT PAY_TIME from ("+
//		"  select * from PAY_ORDER where BANKCOD='"+bank.bankCode+"' and ACTDAT>=to_date(?,'yyyy-mm-dd hh24:mi:ss') and ACTDAT<=to_date(?,'yyyy-mm-dd hh24:mi:ss')"+
//		" )po left join PAY_BANK_ACCOUNT_CHECK_TMP acc on po.PAYORDNO=acc.BNKORDNO where acc.BNKORDNO is null)";;
//        log.info(sql);
//        Connection con = null;
//        PreparedStatement ps = null;
//        try {
//            con = connection();
//            ps = con.prepareStatement(sql);
//            int n=1;
//            ps.setString(n++,checkDate+" 00:00:00");
//            ps.setString(n++,checkDate+" 23:59:59");
//            ps.setString(n++,checkDate+" 00:00:00");
//            ps.setString(n++,checkDate+" 23:59:59");
//            ps.setString(n++,checkDate+" 00:00:00");
//            ps.setString(n++,checkDate+" 23:59:59");
//            ps.executeUpdate();
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw e;
//        } finally {
//            close(null, ps, con);
//        }
//    }
    /**
     * 对旧账
     * @param bank
     * @throws Exception
     */
    public void accountCheckOld(PayCoopBank bank) throws Exception {
    	//把旧的系统有，银行无的账，如果对账成功，更新状态。
        String sql = "update PAY_BANK_ACCOUNT_CHECK acc set CWTYPE='0' where exists("+
        	"select 1 from PAY_BANK_ACCOUNT_CHECK_TMP acct "+
        	"where acc.ACTDATE=acct.ACTDATE and acc.BNKORDNO=acct.BNKORDNO " +
        	"and acc.CHKNUM<=5 and acc.BANKCODE='"+bank.bankCode+"' and acc.CWTYPE='3')";
        //增加错账对账次数
        String sql1 = "update PAY_BANK_ACCOUNT_CHECK set CHKNUM = CHKNUM+1 where CHKNUM<=5 and BANKCODE='"+bank.bankCode+"' and CWTYPE='3'";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            ps.executeUpdate();
            ps.close();
            ps = con.prepareStatement(sql1);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(null, ps, con);
        }
    }
    /**
     * 批量插入对账临时数据到数据表
     * @param tmp
     * @throws Exception 
     */
	public void addPayBankAccountCheckBatch(List tmp) throws Exception {
		String sql = "insert into PAY_BANK_ACCOUNT_CHECK_TMP("+
	            "BANKCODE," + 
	            "ACTDATE," + 
	            "BNKORDNO," + 
	            "CHKTYPE," + 
	            "TXNAMT," + 
	            "FEE," + 
	            "INAMT," + 
	            "BANKLOGNO)values(?,?,?,?,?,?,?,?)";
	        log.info(sql);
	        Connection con = null;
	        PreparedStatement ps = null;
	        try {
	            con = connection();
	            ps = con.prepareStatement(sql);
	            for(int i = 0; i<tmp.size(); i++){
	            	PayBankAccountCheckTmp accountCheckTmp = (PayBankAccountCheckTmp)tmp.get(i);
		            int n=1;
		            ps.setString(n++,accountCheckTmp.bankcode);
		            ps.setString(n++,accountCheckTmp.actdate==null?"":accountCheckTmp.actdate);
		            ps.setString(n++,accountCheckTmp.bnkordno);
		            ps.setString(n++,accountCheckTmp.chktype);
		            ps.setLong(n++,accountCheckTmp.txnamt);
		            ps.setLong(n++,accountCheckTmp.fee);
		            ps.setLong(n++,accountCheckTmp.inamt);
		            ps.setString(n++,accountCheckTmp.banklogno);
		            ps.addBatch();
	            }
	            ps.executeBatch();
	        } catch (Exception e) {
	            e.printStackTrace();
	            throw e;
	        }finally {
	            close(null, ps, con);
	        }
	}
	public void createAccountData(String transDate, String payChannel) throws Exception {
		//检查transDate天的交易数据是否已经插入了对账列表中
        String sqlCheckDate = "select count(*) recordCount from PAY_BANK_ACCOUNT_CHECK where " +
        	"ACTDATE='"+transDate+"' "+((payChannel!=null && payChannel.length()>0)?" and BANKCODE=? ":"");
        //如果删除了对账数据，重新插入对账数据
        String sqlAddAccountOrder = "insert into PAY_BANK_ACCOUNT_CHECK"+
			"(select PAY_CHANNEL BANKCODE,to_char(po.ACTDAT,'yyyymmdd'),po.PAYORDNO,'-1' CWTYPE,'1' CHKTYPE,to_char(po.BANKSTLDATE,'yyyymmdd') BANKDATE,"+
			"	'' BANKLOGNO,po.TXCCY CCYCOD,po.TXAMT,po.FEE,po.NETRECAMT,'' BKGLOGNO,null BKGDATE,po.BANKPAYACNO PAYACTNO,"+
			"   po.MERNO MERCODE,po.PRDORDNO GODORDCODE,'' BKGTXNCOD,null BKGTXNDATE,null BKGTXNTIM,0 CHKNUM,'' SREFNO,"+
			"   '' FILED1,'' FILED2,'0' STATUS,'' LAST_UPDATE_USER,sysdate LAST_UPDATE_TIME,'' REMARK,po.CREATETIME ORDER_TIME,po.ACTDAT PAY_TIME,po.ORDSTATUS from ("+
			"  select * from PAY_ORDER where "+((payChannel!=null && payChannel.length()>0)?" PAY_CHANNEL=? ":" PAY_CHANNEL is not null")
				+" and ACTDAT>=to_date(?,'yyyy-mm-dd hh24:mi:ss') and ACTDAT<=to_date(?,'yyyy-mm-dd hh24:mi:ss') and ORDSTATUS='01'"+
			" )po)";
        //退款插入
        String sqlAddAccountRefund = "insert into PAY_BANK_ACCOUNT_CHECK"+
			"(select PAY_CHANNEL BANKCODE,to_char(po.RFREQDATE,'yyyymmdd'),po.PAYORDNO,'-1' CWTYPE,'2' CHKTYPE,to_char(po.BANKSTLDATE,'yyyymmdd') BANKDATE,"+
			"	'' BANKLOGNO,po.TXCCY CCYCOD,po.RFAMT,po.RF_FEE,po.RF_NETRECAMT,'' BKGLOGNO,null BKGDATE,po.BANKPAYACNO PAYACTNO,"+
			"   po.MERNO MERCODE,po.PRDORDNO GODORDCODE,'' BKGTXNCOD,null BKGTXNDATE,null BKGTXNTIM,0 CHKNUM,'' SREFNO,"+
			"   '' FILED1,'' FILED2,'0' STATUS,'' LAST_UPDATE_USER,sysdate LAST_UPDATE_TIME,'' REMARK,po.CREATETIME ORDER_TIME,po.ACTDAT PAY_TIME,BANKSTS " +
			"      FROM(SELECT pOrder.*,pr.RFAMT,pr.RFREQDATE,pr.FEE RF_FEE,pr.NETRECAMT RF_NETRECAMT,pr.BANKSTS"+
			"     FROM(SELECT * FROM pay_refund WHERE RFORDTIME>=to_date(?,'yyyy-mm-dd hh24:mi:ss') AND RFORDTIME  <=to_date(?,'yyyy-mm-dd hh24:mi:ss') and BANKSTS='01'"+
				") pr LEFT JOIN pay_order pOrder "+
			"  on pOrder.payordno=pr.oripayordno and "+((payChannel!=null && payChannel.length()>0)?" PAY_CHANNEL=? ":" PAY_CHANNEL is not null")+
			" )po)";
        log.info(sqlCheckDate);
        log.info(sqlAddAccountOrder);
        log.info(sqlAddAccountRefund);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = connection();
            ps = con.prepareStatement(sqlCheckDate);
            if(payChannel!=null && payChannel.length()>0)ps.setString(1, payChannel);
            rs = ps.executeQuery();
            int n=1;
            if(rs.next()){
            	if(rs.getLong("recordCount")==0){
            		rs.close();ps.close();
            		ps = con.prepareStatement(sqlAddAccountOrder);
            		n=1;
		            if(payChannel!=null && payChannel.length()>0)ps.setString(n++,payChannel);
		            ps.setString(n++,transDate+" 00:00:00");
		            ps.setString(n++,transDate+" 23:59:59");
		            ps.executeUpdate();
		            ps.close();
		            ps = con.prepareStatement(sqlAddAccountRefund);
		            n=1;
		            ps.setString(n++,transDate+" 00:00:00");
		            ps.setString(n++,transDate+" 23:59:59");
		            if(payChannel!=null && payChannel.length()>0)ps.setString(n++,payChannel);
		            ps.executeUpdate();
            	}
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(rs, ps, con);
        }
    }
	public void checkAccount() throws Exception {
		//正常对账
        String sql = "update PAY_BANK_ACCOUNT_CHECK acc set CWTYPE='0',CHKNUM=CHKNUM+1 where exists " +
        	"(select 1 from PAY_BANK_ACCOUNT_CHECK_TMP acct where acc.bnkordno=acct.BNKORDNO and acc.ORDSTATUS='01' and acc.CWTYPE!='0')";
        //状态异常（掉单）
        String sql1 = "update PAY_BANK_ACCOUNT_CHECK acc set CWTYPE='1',CHKNUM=CHKNUM+1 where exists " +
            "(select 1 from PAY_BANK_ACCOUNT_CHECK_TMP acct where acc.bnkordno=acct.BNKORDNO and acc.ORDSTATUS!='01' and acc.CWTYPE!='0')";
        //银行有，平台无
        String sql2 = "delete from PAY_BANK_ACCOUNT_CHECK_TMP acct  where exists "+
        	"(select 1 from PAY_BANK_ACCOUNT_CHECK acc where acc.bnkordno=acct.BNKORDNO)";
        String sql3 = "insert into PAY_BANK_ACCOUNT_CHECK"+
	        "(select BANKCODE,acc.ACTDATE,acc.BNKORDNO,'2' CWTYPE,'1' CHKTYPE,null BANKDATE,"+
			" acc.BANKLOGNO,'CNY' CCYCOD,acc.TXNAMT,acc.FEE,acc.INAMT,'' BKGLOGNO,null BKGDATE,'' PAYACTNO,"+
			" '' MERCODE,'' GODORDCODE,'' BKGTXNCOD,null BKGTXNDATE,null BKGTXNTIM,1 CHKNUM,'' SREFNO,"+
			" '' FILED1,'' FILED2,'0' STATUS,'' LAST_UPDATE_USER,sysdate LAST_UPDATE_TIME,'' REMARK,null ORDER_TIME,null PAY_TIME,null "+
			" from PAY_BANK_ACCOUNT_CHECK_TMP acc)";
        log.info(sql);
        log.info(sql1);
        log.info(sql2);
        log.info(sql3);
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            ps.executeUpdate();
            ps.close();
            ps = con.prepareStatement(sql1);
            ps.executeUpdate();
            ps.close();
            ps = con.prepareStatement(sql2);
            ps.executeUpdate();
            ps.close();
            ps = con.prepareStatement(sql3);
            ps.executeUpdate();
            ps.close();
            con.commit();
        } catch (Exception e) {
            e.printStackTrace();
            con.rollback();
            throw e;
        } finally {
            close(null, ps, con);
        }
    }
}