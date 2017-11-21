package com.pay.custstl.dao;

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
 * Table PAY_CUST_STL_INFO DAO. 
 * @author Administrator
 *
 */
public class PayCustStlInfoDAO  extends BaseDAO {
    private static final Log log = LogFactory.getLog(PayCustStlInfoDAO.class);
    public static synchronized PayCustStlInfo getPayCustStlInfoValue(ResultSet rs)throws SQLException {
        PayCustStlInfo payCustStlInfo = new PayCustStlInfo();
        payCustStlInfo.seqNo = rs.getString("SEQ_NO");
        payCustStlInfo.payAcNo = rs.getString("PAY_AC_NO");
        payCustStlInfo.custId = rs.getString("CUST_ID");
        payCustStlInfo.custBankDepositName = rs.getString("CUST_BANK_DEPOSIT_NAME");
        payCustStlInfo.depositBankCode = rs.getString("DEPOSIT_BANK_CODE");
        payCustStlInfo.depositBankBrchName = rs.getString("DEPOSIT_BANK_BRCH_NAME");
        payCustStlInfo.custStlBankAcNo = rs.getString("CUST_STL_BANK_AC_NO");
        payCustStlInfo.custStlType = rs.getString("CUST_STL_TYPE");
        payCustStlInfo.custSetPeriod = rs.getString("CUST_SET_PERIOD");
        payCustStlInfo.custSetFrey = rs.getString("CUST_SET_FREY");
        payCustStlInfo.custSetFreyAgent = rs.getString("CUST_SET_FREY_AGENT");
        payCustStlInfo.custStlTimeSet = rs.getString("CUST_STL_TIME_SET");
        payCustStlInfo.custSetPeriodAgent = rs.getString("CUST_SET_PERIOD_AGENT");
        payCustStlInfo.custStlTimeSetAgent = rs.getString("CUST_STL_TIME_SET_AGENT");
        payCustStlInfo.minStlBalance = rs.getLong("MIN_STL_BALANCE");
        payCustStlInfo.creOperId = rs.getString("CRE_OPER_ID");
        payCustStlInfo.creTime = rs.getTimestamp("CRE_TIME");
        payCustStlInfo.lstUptOperId = rs.getString("LST_UPT_OPER_ID");
        payCustStlInfo.lstUptTime = rs.getTimestamp("LST_UPT_TIME");
        payCustStlInfo.custSetDay = rs.getString("CUST_SET_DAY");
        payCustStlInfo.custSetPeriodDaishou = rs.getString("CUST_SET_PERIOD_DAISHOU");
        payCustStlInfo.custStlTimeSetDaishou = rs.getString("CUST_STL_TIME_SET_DAISHOU");
        payCustStlInfo.custStlBankCity = rs.getString("CUST_STL_BANK_CITY");
        payCustStlInfo.custStlBankProvince = rs.getString("CUST_STL_BANK_PROVINCE");
        payCustStlInfo.custStlIdno = rs.getString("CUST_STL_IDNO");
        payCustStlInfo.custStlMobileno = rs.getString("CUST_STL_MOBILENO");
        payCustStlInfo.custStlBankNumber = rs.getString("CUST_STL_BANK_NUMBER");
        
        return payCustStlInfo;
    }
    public String addPayCustStlInfo(PayCustStlInfo payCustStlInfo) throws Exception {
        String sql = "insert into PAY_CUST_STL_INFO("+
            "SEQ_NO," + 
            "PAY_AC_NO," + 
            "CUST_ID," + 
            "CUST_BANK_DEPOSIT_NAME," + 
            "DEPOSIT_BANK_CODE," + 
            "DEPOSIT_BANK_BRCH_NAME," + 
            "CUST_STL_BANK_AC_NO," + 
            "CUST_STL_TYPE," + 
            "CUST_SET_PERIOD," + 
            "CUST_SET_FREY," + 
            "CUST_SET_FREY_AGENT," + 
            "CUST_STL_TIME_SET," + 
            "CUST_SET_PERIOD_AGENT," + 
            "CUST_STL_TIME_SET_AGENT," + 
            "MIN_STL_BALANCE," + 
            "CRE_OPER_ID," + 
            "CRE_TIME," + 
            "LST_UPT_OPER_ID," + 
            "LST_UPT_TIME," + 
            "CUST_SET_PERIOD_DAISHOU," + 
            "CUST_STL_TIME_SET_DAISHOU," + 
            "CUST_SET_DAY,"+
            
			"CUST_STL_BANK_CITY,"+
			"CUST_STL_BANK_PROVINCE,"+
			"CUST_STL_IDNO,"+
            "CUST_STL_MOBILENO,CUST_STL_BANK_NUMBER)values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,to_date(?,'yyyy-mm-dd hh24:mi:ss'),?,to_date(?,'yyyy-mm-dd hh24:mi:ss'),?,?,?,?,?,?,?,?)";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            int n=1;
            ps.setString(n++,payCustStlInfo.seqNo);
            ps.setString(n++,payCustStlInfo.payAcNo);
            ps.setString(n++,payCustStlInfo.custId);
            ps.setString(n++,payCustStlInfo.custBankDepositName);
            ps.setString(n++,payCustStlInfo.depositBankCode);
            ps.setString(n++,payCustStlInfo.depositBankBrchName);
            ps.setString(n++,payCustStlInfo.custStlBankAcNo);
            ps.setString(n++,payCustStlInfo.custStlType);
            ps.setString(n++,payCustStlInfo.custSetPeriod);
            ps.setString(n++,payCustStlInfo.custSetFrey);
            ps.setString(n++,payCustStlInfo.custSetFreyAgent);
            ps.setString(n++,payCustStlInfo.custStlTimeSet);
            ps.setString(n++,payCustStlInfo.custSetPeriodAgent);
            ps.setString(n++,payCustStlInfo.custStlTimeSetAgent);
            ps.setLong(n++,payCustStlInfo.minStlBalance);
            ps.setString(n++,payCustStlInfo.creOperId);
            ps.setString(n++,new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payCustStlInfo.creTime));
            ps.setString(n++,payCustStlInfo.lstUptOperId);
            ps.setString(n++,new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payCustStlInfo.lstUptTime));
            ps.setString(n++,payCustStlInfo.custSetPeriodDaishou);
            ps.setString(n++,payCustStlInfo.custStlTimeSetDaishou);
            ps.setString(n++,payCustStlInfo.custSetDay);
            
            ps.setString(n++,payCustStlInfo.custStlBankCity);
            ps.setString(n++,payCustStlInfo.custStlBankProvince);
            ps.setString(n++,payCustStlInfo.custStlIdno);
            ps.setString(n++,payCustStlInfo.custStlMobileno);
            ps.setString(n++,payCustStlInfo.custStlBankNumber);
            ps.executeUpdate();
            return "";
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }finally {
            close(null, ps, con);
        }
    }
    public PayCustStlInfo getPayCustStlInfoByMerchantId(String custId) throws Exception{        
        String sql = "select * from PAY_CUST_STL_INFO where CUST_ID='"+custId+"'";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            if(rs.next())return getPayCustStlInfoValue(rs);
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
     * @param payCustStlInfo
     * @return
     */
    private String setPayCustStlInfoSql(PayCustStlInfo payCustStlInfo) {
        StringBuffer sql = new StringBuffer();
        
        if(payCustStlInfo.custId != null && payCustStlInfo.custId.length() !=0) {
            sql.append(" CUST_ID = ? and ");
        }
        String tmp = sql.toString();
        if(tmp.length()>0)tmp=tmp.substring(0,tmp.lastIndexOf(" and "));
        return tmp;
    }
    /**
     * Set query parameter.
     * @param ps
     * @param payCustStlInfo
     * @param n
     * @return
     * @throws SQLException
     */
    private int setPayCustStlInfoParameter(PreparedStatement ps,PayCustStlInfo payCustStlInfo,int n)throws SQLException {
        if(payCustStlInfo.custId != null && payCustStlInfo.custId.length() !=0) {
            ps.setString(n++,payCustStlInfo.custId);
        }
        return n;
    }
    /**
     * Get records count.
     * @param payCustStlInfo
     * @return
     */
    public int getPayCustStlInfoCount(PayCustStlInfo payCustStlInfo) {
        String sqlCon = setPayCustStlInfoSql(payCustStlInfo);
        String sql = "select count(rownum) recordCount from PAY_CUST_STL_INFO " +(sqlCon.length()==0?"":" where "+ sqlCon);
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            int n = 1;
            setPayCustStlInfoParameter(ps,payCustStlInfo,n);
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
     * @param payCustStlInfo
     * @param page
     * @param rows
     * @param sort
     * @param order
     * @return
     * @throws Exception
     */
    public List getPayCustStlInfoList(PayCustStlInfo payCustStlInfo,int page,int rows,String sort,String order) throws Exception{
        sort = Tools.paraNameToDBColumnName(sort);
        String sqlCon = setPayCustStlInfoSql(payCustStlInfo);
        String sortOrder = sort == null || sort.length()==0?" order by SEQ_NO desc":(" order by " + sort +("desc".equals(order)?" desc ":" asc "));
        String sql = "select * from (" +
                "  select rownum rowno,tmp1.* from (" +
                "   select tmp.*  from PAY_CUST_STL_INFO tmp " +(sqlCon.length()==0?"":" where "+ sqlCon) +sortOrder+
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
            setPayCustStlInfoParameter(ps,payCustStlInfo,n);
            rs = ps.executeQuery();
            while(rs.next()){
                list.add(getPayCustStlInfoValue(rs));
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
     * remove PayCustStlInfo
     * @param id
     * @throws Exception     
     */
    public void removePayCustStlInfo(String seqNo) throws Exception {
        String sql = "delete from PAY_CUST_STL_INFO where SEQ_NO=?";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            ps.setString(1,seqNo);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(null, ps, con);
        }
    }
    /**
     * update PayCustStlInfo
     * @param payCustStlInfo
     * @throws Exception
     */
    public void updatePayCustStlInfo(PayCustStlInfo payCustStlInfo) throws Exception {
        String sqlTmp = "";
        if(payCustStlInfo.payAcNo != null)sqlTmp = sqlTmp + " PAY_AC_NO=?,";
        if(payCustStlInfo.custBankDepositName != null)sqlTmp = sqlTmp + " CUST_BANK_DEPOSIT_NAME=?,";
        if(payCustStlInfo.depositBankCode != null)sqlTmp = sqlTmp + " DEPOSIT_BANK_CODE=?,";
        if(payCustStlInfo.depositBankBrchName != null)sqlTmp = sqlTmp + " DEPOSIT_BANK_BRCH_NAME=?,";
        if(payCustStlInfo.custStlBankAcNo != null)sqlTmp = sqlTmp + " CUST_STL_BANK_AC_NO=?,";
        if(payCustStlInfo.custStlType != null)sqlTmp = sqlTmp + " CUST_STL_TYPE=?,";
        if(payCustStlInfo.custSetPeriod != null)sqlTmp = sqlTmp + " CUST_SET_PERIOD=?,";
        if(payCustStlInfo.custSetFrey != null)sqlTmp = sqlTmp + " CUST_SET_FREY=?,";
        if(payCustStlInfo.custSetFreyAgent != null)sqlTmp = sqlTmp + " CUST_SET_FREY_AGENT=?,";
        if(payCustStlInfo.custStlTimeSet != null)sqlTmp = sqlTmp + " CUST_STL_TIME_SET=?,";
        if(payCustStlInfo.custSetPeriodAgent != null)sqlTmp = sqlTmp + " CUST_SET_PERIOD_AGENT=?,";
        if(payCustStlInfo.custStlTimeSetAgent != null)sqlTmp = sqlTmp + " CUST_STL_TIME_SET_AGENT=?,";
        if(payCustStlInfo.minStlBalance != null)sqlTmp = sqlTmp + " MIN_STL_BALANCE=?,";
        if(payCustStlInfo.lstUptOperId != null)sqlTmp = sqlTmp + " LST_UPT_OPER_ID=?,";
        if(payCustStlInfo.custSetPeriodDaishou != null)sqlTmp = sqlTmp + " CUST_SET_PERIOD_DAISHOU=?,";
        if(payCustStlInfo.custStlTimeSetDaishou != null)sqlTmp = sqlTmp + " CUST_STL_TIME_SET_DAISHOU=?,";
        if(payCustStlInfo.lstUptTime != null)sqlTmp = sqlTmp + " LST_UPT_TIME=to_date(?,'yyyy-mm-dd hh24:mi:ss'),";
        if(payCustStlInfo.custSetDay != null)sqlTmp = sqlTmp + " CUST_SET_DAY=?,";
        
        if(payCustStlInfo.custStlBankCity != null)sqlTmp = sqlTmp + " CUST_STL_BANK_CITY=?,";
        if(payCustStlInfo.custStlBankProvince != null)sqlTmp = sqlTmp + " CUST_STL_BANK_PROVINCE=?,";
        if(payCustStlInfo.custStlIdno != null)sqlTmp = sqlTmp + " CUST_STL_IDNO=?,";
        if(payCustStlInfo.custStlMobileno != null)sqlTmp = sqlTmp + " CUST_STL_MOBILENO=?,";
        if(payCustStlInfo.custStlBankNumber != null)sqlTmp = sqlTmp + " CUST_STL_BANK_NUMBER=?,";
        if(sqlTmp.length()==0)return;
        String sql = "update PAY_CUST_STL_INFO "+        
              "set " + 
              sqlTmp.substring(0,sqlTmp.length()-1) + 
            " where CUST_ID=?"; 
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            int n=1;
            if(payCustStlInfo.payAcNo != null)ps.setString(n++,payCustStlInfo.payAcNo);
            if(payCustStlInfo.custBankDepositName != null)ps.setString(n++,payCustStlInfo.custBankDepositName);
            if(payCustStlInfo.depositBankCode != null)ps.setString(n++,payCustStlInfo.depositBankCode);
            if(payCustStlInfo.depositBankBrchName != null)ps.setString(n++,payCustStlInfo.depositBankBrchName);
            if(payCustStlInfo.custStlBankAcNo != null)ps.setString(n++,payCustStlInfo.custStlBankAcNo);
            if(payCustStlInfo.custStlType != null)ps.setString(n++,payCustStlInfo.custStlType);
            if(payCustStlInfo.custSetPeriod != null)ps.setString(n++,payCustStlInfo.custSetPeriod);
            if(payCustStlInfo.custSetFrey != null)ps.setString(n++,payCustStlInfo.custSetFrey);
            if(payCustStlInfo.custSetFreyAgent != null)ps.setString(n++,payCustStlInfo.custSetFreyAgent);
            if(payCustStlInfo.custStlTimeSet != null)ps.setString(n++,payCustStlInfo.custStlTimeSet);
            if(payCustStlInfo.custSetPeriodAgent != null)ps.setString(n++,payCustStlInfo.custSetPeriodAgent);
            if(payCustStlInfo.custStlTimeSetAgent != null)ps.setString(n++,payCustStlInfo.custStlTimeSetAgent);
            if(payCustStlInfo.minStlBalance != null)ps.setLong(n++,payCustStlInfo.minStlBalance);
            if(payCustStlInfo.lstUptOperId != null)ps.setString(n++,payCustStlInfo.lstUptOperId);
            if(payCustStlInfo.custSetPeriodDaishou != null)ps.setString(n++,payCustStlInfo.custSetPeriodDaishou);
            if(payCustStlInfo.custStlTimeSetDaishou != null)ps.setString(n++,payCustStlInfo.custStlTimeSetDaishou);
            if(payCustStlInfo.lstUptTime != null)ps.setString(n++,new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payCustStlInfo.lstUptTime));
            if(payCustStlInfo.custSetDay != null)ps.setString(n++,payCustStlInfo.custSetDay);
            
            if(payCustStlInfo.custStlBankCity != null)ps.setString(n++,payCustStlInfo.custStlBankCity);
            if(payCustStlInfo.custStlBankProvince != null)ps.setString(n++,payCustStlInfo.custStlBankProvince);
            if(payCustStlInfo.custStlIdno != null)ps.setString(n++,payCustStlInfo.custStlIdno);
            if(payCustStlInfo.custStlMobileno != null)ps.setString(n++,payCustStlInfo.custStlMobileno);
            if(payCustStlInfo.custStlBankNumber != null)ps.setString(n++,payCustStlInfo.custStlBankNumber);
            ps.setString(n++,payCustStlInfo.custId);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }finally {
            close(null, ps, con);
        }
    }
}