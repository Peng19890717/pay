package com.pay.order.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import util.Tools;

import com.PayConstant;
import com.jweb.dao.BaseDAO;
import com.pay.settlement.dao.PayMerchantSettlement;
/**
 * Table PAY_TRANSFER_ACC_ORDER DAO. 
 * @author Administrator
 *
 */
public class PayTransferAccOrderDAO  extends BaseDAO {
    private static final Log log = LogFactory.getLog(PayTransferAccOrderDAO.class);
    public static synchronized PayTransferAccOrder getPayTransferAccOrderValue(ResultSet rs)throws SQLException {
        PayTransferAccOrder payTransferAccOrder = new PayTransferAccOrder();
        payTransferAccOrder.tranOrdno = rs.getString("TRAN_ORDNO");
        payTransferAccOrder.tranTime = rs.getTimestamp("TRAN_TIME");
        payTransferAccOrder.clearDate = rs.getString("CLEAR_DATE");
        payTransferAccOrder.status = rs.getString("STATUS");
        payTransferAccOrder.ccy = rs.getString("CCY");
        payTransferAccOrder.txamt = rs.getLong("TXAMT");
        payTransferAccOrder.tgetAccNo = rs.getString("TGET_ACC_NO");
        payTransferAccOrder.tgetAccName = rs.getString("TGET_ACC_NAME");
        payTransferAccOrder.tgetAccType = rs.getString("TGET_ACC_TYPE");
        payTransferAccOrder.totamt = rs.getLong("TOTAMT");
        payTransferAccOrder.backError = rs.getString("BACK_ERROR");
        payTransferAccOrder.bankCode = rs.getString("BANK_CODE");
        payTransferAccOrder.custId = rs.getString("CUST_ID");
        payTransferAccOrder.batTranCustOrdno = rs.getString("BAT_TRAN_CUST_ORDNO");
        payTransferAccOrder.batNo = rs.getString("BAT_NO");
        payTransferAccOrder.tranType = rs.getString("TRAN_TYPE");
        payTransferAccOrder.filed1 = rs.getString("FILED1");
        payTransferAccOrder.filed2 = rs.getString("FILED2");
        payTransferAccOrder.cardBankBranch = rs.getString("CARD_BANK_BRANCH");
        payTransferAccOrder.type = rs.getString("TYPE");
        payTransferAccOrder.issuser = rs.getString("ISSUSER");
        payTransferAccOrder.tranSuccessTime = rs.getTimestamp("TRAN_SUCCESS_TIME");
        payTransferAccOrder.custName = rs.getString("CUST_NAME");
        return payTransferAccOrder;
    }
    public String addPayTransferAccOrder(PayTransferAccOrder payTransferAccOrder) throws Exception {
        String sql = "insert into PAY_TRANSFER_ACC_ORDER("+
            "TRAN_ORDNO," + 
            "TRAN_TIME," + 
            "CLEAR_DATE," + 
            "STATUS," + 
            "CCY," + 
            "TXAMT," + 
            "TGET_ACC_NO," + 
            "TGET_ACC_NAME," + 
            "TOTAMT," + 
            "BACK_ERROR," + 
            "BANK_CODE," + 
            "CUST_ID," + 
            "BAT_TRAN_CUST_ORDNO," + 
            "BAT_NO," + 
            "TRAN_TYPE," + 
            "FILED1," + 
            "FILED2," + 
            "CARD_BANK_BRANCH," + 
            "TYPE," + 
            "ISSUSER," + 
            "TRAN_SUCCESS_TIME," +
            "CUST_NAME,TGET_ACC_TYPE)values(?,to_date(?,'yyyy-mm-dd hh24:mi:ss'),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,to_date(?,'yyyy-mm-dd hh24:mi:ss'),?,?)";
        log.info(sql);
        log.info(payTransferAccOrder.toString().replaceAll("\n",";"));
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            int n=1;
            ps.setString(n++,payTransferAccOrder.tranOrdno);
            ps.setString(n++,new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payTransferAccOrder.tranTime));
            ps.setString(n++,payTransferAccOrder.clearDate);
            ps.setString(n++,payTransferAccOrder.status);
            ps.setString(n++,payTransferAccOrder.ccy);
            ps.setLong(n++,payTransferAccOrder.txamt);
            ps.setString(n++,payTransferAccOrder.tgetAccNo);
            ps.setString(n++,payTransferAccOrder.tgetAccName);
            ps.setLong(n++,payTransferAccOrder.totamt);
            ps.setString(n++,payTransferAccOrder.backError);
            ps.setString(n++,payTransferAccOrder.bankCode);
            ps.setString(n++,payTransferAccOrder.custId);
            ps.setString(n++,payTransferAccOrder.batTranCustOrdno);
            ps.setString(n++,payTransferAccOrder.batNo);
            ps.setString(n++,payTransferAccOrder.tranType);
            ps.setString(n++,payTransferAccOrder.filed1);
            ps.setString(n++,payTransferAccOrder.filed2);
            ps.setString(n++,payTransferAccOrder.cardBankBranch);
            ps.setString(n++,payTransferAccOrder.type);
            ps.setString(n++,payTransferAccOrder.issuser);
            ps.setString(n++,new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payTransferAccOrder.tranSuccessTime));
            ps.setString(n++, payTransferAccOrder.custName);
            ps.setString(n++, payTransferAccOrder.tgetAccType);
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
        String sql = "select * from PAY_TRANSFER_ACC_ORDER";
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
                list.add(getPayTransferAccOrderValue(rs));
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
     * @param payTransferAccOrder
     * @return
     */
    private String setPayTransferAccOrderSql(PayTransferAccOrder payTransferAccOrder) {
        StringBuffer sql = new StringBuffer();
        
        if(payTransferAccOrder.tranOrdno != null && payTransferAccOrder.tranOrdno.length() !=0) {
            sql.append(" TRAN_ORDNO = ? and ");
        }
        if(payTransferAccOrder.batTranCustOrdno != null && payTransferAccOrder.batTranCustOrdno.length() !=0) {
            sql.append(" BAT_TRAN_CUST_ORDNO = ? and ");
        }
        if(payTransferAccOrder.batNo != null && payTransferAccOrder.batNo.length() !=0) {
            sql.append(" BAT_NO = ? and ");
        }
        if(payTransferAccOrder.custId != null && payTransferAccOrder.custId.length() !=0) {
            sql.append(" CUST_ID = ? and ");
        }
        if(payTransferAccOrder.tgetAccNo != null && payTransferAccOrder.tgetAccNo.length() !=0) {
            sql.append(" TGET_ACC_NO = ? and ");
        }
        if(payTransferAccOrder.tranTimeStart != null && payTransferAccOrder.tranTimeStart.length() !=0) {
            sql.append(" TRAN_TIME >= to_date(?,'yyyy-mm-dd hh24:mi:ss') and  ");
        }
        if(payTransferAccOrder.tranTimeEnd != null && payTransferAccOrder.tranTimeEnd.length() !=0 ) {
            sql.append(" TRAN_TIME <= to_date(?,'yyyy-mm-dd hh24:mi:ss') and ");
        }
        if(payTransferAccOrder.status != null && payTransferAccOrder.status.length() !=0) {
        	if("02".equals(payTransferAccOrder.status))sql.append(" STATUS != '01' and ");
        	else sql.append(" STATUS = '01' and ");
        }
        String tmp = sql.toString();
        if(tmp.length()>0)tmp=tmp.substring(0,tmp.lastIndexOf(" and "));
        return tmp;
    }
    /**
     * Set query parameter.
     * @param ps
     * @param payTransferAccOrder
     * @param n
     * @return
     * @throws SQLException
     */
    private int setPayTransferAccOrderParameter(PreparedStatement ps,PayTransferAccOrder payTransferAccOrder,int n)throws SQLException {
        if(payTransferAccOrder.tranOrdno != null && payTransferAccOrder.tranOrdno.length() !=0) {
            ps.setString(n++,payTransferAccOrder.tranOrdno);
        }
        if(payTransferAccOrder.batTranCustOrdno != null && payTransferAccOrder.batTranCustOrdno.length() !=0) {
        	ps.setString(n++,payTransferAccOrder.batTranCustOrdno);
        }
        if(payTransferAccOrder.batNo != null && payTransferAccOrder.batNo.length() !=0) {
        	ps.setString(n++,payTransferAccOrder.batNo);
        }
        if(payTransferAccOrder.custId != null && payTransferAccOrder.custId.length() !=0) {
            ps.setString(n++,payTransferAccOrder.custId);
        }
        if(payTransferAccOrder.tgetAccNo != null && payTransferAccOrder.tgetAccNo.length() !=0) {
            ps.setString(n++,payTransferAccOrder.tgetAccNo);
        }
        if(payTransferAccOrder.tranTimeStart != null && payTransferAccOrder.tranTimeStart.length() !=0) {
        	ps.setString(n++,payTransferAccOrder.tranTimeStart+" 00:00:00");
        }
        if(payTransferAccOrder.tranTimeEnd != null && payTransferAccOrder.tranTimeEnd.length() !=0 ) {
        	ps.setString(n++, payTransferAccOrder.tranTimeEnd+" 23:59:59");
        }
//        if(payTransferAccOrder.status != null && payTransferAccOrder.status.length() !=0) {
//            ps.setString(n++,payTransferAccOrder.status);
//        }
        return n;
    }
    /**
     * Get records count.
     * @param payTransferAccOrder
     * @return
     */
    public int getPayTransferAccOrderCount(PayTransferAccOrder payTransferAccOrder) {
        String sqlCon = setPayTransferAccOrderSql(payTransferAccOrder);
        String sql = "select count(rownum) recordCount from PAY_TRANSFER_ACC_ORDER " +(sqlCon.length()==0?"":" where "+ sqlCon);
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            int n = 1;
            setPayTransferAccOrderParameter(ps,payTransferAccOrder,n);
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
     * @param payTransferAccOrder
     * @param page
     * @param rows
     * @param sort
     * @param order
     * @return
     * @throws Exception
     */
    public List getPayTransferAccOrderList(PayTransferAccOrder payTransferAccOrder,int page,int rows,String sort,String order) throws Exception{
        sort = Tools.paraNameToDBColumnName(sort);
        String sqlCon = setPayTransferAccOrderSql(payTransferAccOrder);
        String sortOrder = sort == null || sort.length()==0?" order by TRAN_TIME desc":(" order by " + sort +("desc".equals(order)?" desc ":" asc "));
        String sql = "select * from (" +
                "  select rownum rowno,tmp1.* from (" +
                "   select tmp.*  from PAY_TRANSFER_ACC_ORDER tmp " +(sqlCon.length()==0?"":" where "+ sqlCon) +sortOrder+
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
            setPayTransferAccOrderParameter(ps,payTransferAccOrder,n);
            rs = ps.executeQuery();
            while(rs.next()){
                list.add(getPayTransferAccOrderValue(rs));
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
     * remove PayTransferAccOrder
     * @param tranOrdno
     * @throws Exception     
     */
    public void removePayTransferAccOrder(String tranOrdno) throws Exception {
        String sql = "delete from PAY_TRANSFER_ACC_ORDER where TRAN_ORDNO=?";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            ps.setString(1,tranOrdno);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(null, ps, con);
        }
    }
    /**
     * detail PayTransferAccOrder
     * @param tranOrdno
     * @return PayTransferAccOrder
     * @throws Exception
     */
    public PayTransferAccOrder detailPayTransferAccOrder(String tranOrdno) throws Exception {
        String sql = "select * from PAY_TRANSFER_ACC_ORDER where TRAN_ORDNO=?";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            ps.setString(1,tranOrdno);
            rs = ps.executeQuery();
            if(rs.next())return getPayTransferAccOrderValue(rs); 
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(rs, ps, con);
        }
        return null;
    }
    /**
     * update PayTransferAccOrder
     * @param payTransferAccOrder
     * @throws Exception
     */
    public void updatePayTransferAccOrder(PayTransferAccOrder payTransferAccOrder) throws Exception {
        String sqlTmp = "";
        if(payTransferAccOrder.tranOrdno != null)sqlTmp = sqlTmp + " TRAN_ORDNO=?,";
        if(payTransferAccOrder.tranTime != null)sqlTmp = sqlTmp + " TRAN_TIME=to_date(?,'yyyy-mm-dd hh24:mi:ss'),";
        if(payTransferAccOrder.clearDate != null)sqlTmp = sqlTmp + " CLEAR_DATE=?,";
        if(payTransferAccOrder.status != null)sqlTmp = sqlTmp + " STATUS=?,";
        if(payTransferAccOrder.ccy != null)sqlTmp = sqlTmp + " CCY=?,";
        if(payTransferAccOrder.txamt != null)sqlTmp = sqlTmp + " TXAMT=?,";
        if(payTransferAccOrder.tgetAccNo != null)sqlTmp = sqlTmp + " TGET_ACC_NO=?,";
        if(payTransferAccOrder.tgetAccName != null)sqlTmp = sqlTmp + " TGET_ACC_NAME=?,";
        if(payTransferAccOrder.totamt != null)sqlTmp = sqlTmp + " TOTAMT=?,";
        if(payTransferAccOrder.backError != null)sqlTmp = sqlTmp + " BACK_ERROR=?,";
        if(payTransferAccOrder.bankCode != null)sqlTmp = sqlTmp + " BANK_CODE=?,";
        if(payTransferAccOrder.custId != null)sqlTmp = sqlTmp + " CUST_ID=?,";
        if(payTransferAccOrder.batTranCustOrdno != null)sqlTmp = sqlTmp + " BAT_TRAN_CUST_ORDNO=?,";
        if(payTransferAccOrder.batNo != null)sqlTmp = sqlTmp + " BAT_NO=?,";
        if(payTransferAccOrder.tranType != null)sqlTmp = sqlTmp + " TRAN_TYPE=?,";
        if(payTransferAccOrder.filed1 != null)sqlTmp = sqlTmp + " FILED1=?,";
        if(payTransferAccOrder.filed2 != null)sqlTmp = sqlTmp + " FILED2=?,";
        if(payTransferAccOrder.cardBankBranch != null)sqlTmp = sqlTmp + " CARD_BANK_BRANCH=?,";
        if(payTransferAccOrder.type != null)sqlTmp = sqlTmp + " TYPE=?,";
        if(payTransferAccOrder.issuser != null)sqlTmp = sqlTmp + " ISSUSER=?,";
        if(payTransferAccOrder.tranSuccessTime != null)sqlTmp = sqlTmp + " TRAN_SUCCESS_TIME=to_date(?,'yyyy-mm-dd hh24:mi:ss'),";
        if(sqlTmp.length()==0)return;
        String sql = "update PAY_TRANSFER_ACC_ORDER "+        
              "set " + 
              sqlTmp.substring(0,sqlTmp.length()-1) + 
            " where TRAN_ORDNO=?"; 
        log.info(sql);
        log.info(payTransferAccOrder.toString().replaceAll("\n",";"));
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            int n=1;
            if(payTransferAccOrder.tranOrdno != null)ps.setString(n++,payTransferAccOrder.tranOrdno);
            if(payTransferAccOrder.tranTime != null)ps.setString(n++,new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payTransferAccOrder.tranTime));
            if(payTransferAccOrder.clearDate != null)ps.setString(n++,payTransferAccOrder.clearDate);
            if(payTransferAccOrder.status != null)ps.setString(n++,payTransferAccOrder.status);
            if(payTransferAccOrder.ccy != null)ps.setString(n++,payTransferAccOrder.ccy);
            if(payTransferAccOrder.txamt != null)ps.setLong(n++,payTransferAccOrder.txamt);
            if(payTransferAccOrder.tgetAccNo != null)ps.setString(n++,payTransferAccOrder.tgetAccNo);
            if(payTransferAccOrder.tgetAccName != null)ps.setString(n++,payTransferAccOrder.tgetAccName);
            if(payTransferAccOrder.totamt != null)ps.setLong(n++,payTransferAccOrder.totamt);
            if(payTransferAccOrder.backError != null)ps.setString(n++,payTransferAccOrder.backError);
            if(payTransferAccOrder.bankCode != null)ps.setString(n++,payTransferAccOrder.bankCode);
            if(payTransferAccOrder.custId != null)ps.setString(n++,payTransferAccOrder.custId);
            if(payTransferAccOrder.batTranCustOrdno != null)ps.setString(n++,payTransferAccOrder.batTranCustOrdno);
            if(payTransferAccOrder.batNo != null)ps.setString(n++,payTransferAccOrder.batNo);
            if(payTransferAccOrder.tranType != null)ps.setString(n++,payTransferAccOrder.tranType);
            if(payTransferAccOrder.filed1 != null)ps.setString(n++,payTransferAccOrder.filed1);
            if(payTransferAccOrder.filed2 != null)ps.setString(n++,payTransferAccOrder.filed2);
            if(payTransferAccOrder.cardBankBranch != null)ps.setString(n++,payTransferAccOrder.cardBankBranch);
            if(payTransferAccOrder.type != null)ps.setString(n++,payTransferAccOrder.type);
            if(payTransferAccOrder.issuser != null)ps.setString(n++,payTransferAccOrder.issuser);
            if(payTransferAccOrder.tranSuccessTime != null)ps.setString(n++,new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payTransferAccOrder.tranSuccessTime));
            ps.setString(n++,payTransferAccOrder.tranOrdno);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }finally {
            close(null, ps, con);
        }
    }
	public long getCustTotalAmt(String custType, String custId) throws Exception {
        String sql = "select CASH_AC_BAL from PAY_ACC_PROFILE where AC_TYPE=? and PAY_AC_NO=?";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            ps.setString(1,custType);
            ps.setString(2,custId);
            rs = ps.executeQuery();
            if(rs.next())return rs.getLong("CASH_AC_BAL"); 
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(rs, ps, con);
        }
        return 0l;
    }
	public void checkTargetAcc(String custType, String custId, List<PayTransferAccOrder> tsList) throws Exception {
		String sql = "";
		String userSql = "select '"+PayConstant.CUST_TYPE_USER+"' CUST_TYPE,USER_ID CUST_ID,REAL_NAME CUST_NAME from PAY_TRAN_USER_INFO";
		String merSql = "select '"+PayConstant.CUST_TYPE_MERCHANT+"' CUST_TYPE,CUST_ID,STORE_NAME CUST_NAME from PAY_MERCHANT";
		String userSqlTmp = "";
		String merSqlTmp = "";
		log.info(userSql);
		log.info(merSql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Map<String, String> tmpMap = new HashMap<String, String>();
        try {
            con = connection();
            for(int i = 0; i<tsList.size(); i++){
            	PayTransferAccOrder tsOrder = tsList.get(i);
            	if(com.PayConstant.CUST_TYPE_USER.equals(tsOrder.tgetAccType)){
            		userSqlTmp = userSqlTmp+"(USER_ID='"+tsOrder.tgetAccNo+"' and REAL_NAME='"+tsOrder.tgetAccName+"') or ";
            	} else if(com.PayConstant.CUST_TYPE_MERCHANT.equals(tsOrder.tgetAccType)){
            		merSqlTmp = merSqlTmp+"(CUST_ID='"+tsOrder.tgetAccNo+"' and STORE_NAME='"+tsOrder.tgetAccName+"') or ";
            	}
            }
            log.info(userSqlTmp);
            if(userSqlTmp.length()==0&&merSqlTmp.length()==0)throw new Exception("没有可以转账的客户");
            if(userSqlTmp.length()!=0&&merSqlTmp.length()!=0){
            	userSql = userSql + " where " +userSqlTmp.substring(0,userSqlTmp.length()-4);
            	merSql = merSql + " where " +merSqlTmp.substring(0,merSqlTmp.length()-4);
            	sql = userSql+ " union " +merSql;
            } else {
            	if(userSqlTmp.length()!=0)sql =  userSql + " where " +userSqlTmp.substring(0,userSqlTmp.length()-4);
            	else if(merSqlTmp.length()!=0)sql = merSql + " where " +merSqlTmp.substring(0,merSqlTmp.length()-4); 
            }
            log.info(sql);
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while(rs.next())tmpMap.put(rs.getString("CUST_TYPE")+","+rs.getString("CUST_ID")+","+rs.getString("CUST_NAME"),"");
            for(int i = 0; i<tsList.size(); i++){
            	PayTransferAccOrder tsOrder = tsList.get(i);
            	if(tmpMap.get(tsOrder.tgetAccType+","+tsOrder.tgetAccNo+","+tsOrder.tgetAccName) == null)tsOrder.status="52";//收款方账号不存在
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(rs, ps, con);
        }
	}
	public void checkTransferedAcc(String custType, String custId, List <PayTransferAccOrder>tsList,
			Map<String, PayTransferAccOrder> tsMap) throws Exception {
		String sql = "select BAT_TRAN_CUST_ORDNO from PAY_TRANSFER_ACC_ORDER where CUST_TYPE=?" +
				" and CUST_ID=? and ";
		String sql1Tmp="";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = connection();
            for(int i = 0; i<tsList.size(); i++){
            	PayTransferAccOrder tsOrder = tsList.get(i);
            	if(tsOrder.status==null || tsOrder.status.length() ==0){
            		sql1Tmp = sql1Tmp +"BAT_TRAN_CUST_ORDNO='"+tsOrder.batTranCustOrdno+"' or ";
            	}
            }
            if(sql1Tmp.length()==0)throw new Exception("没有可以转账的记录");
            sql = sql+"("+sql1Tmp.substring(0,sql1Tmp.length()-4)+")";
            log.info(sql);
            ps = con.prepareStatement(sql);
            ps.setString(1,custType);
            ps.setString(2,custId);
            rs = ps.executeQuery();
            while(rs.next()){
            	PayTransferAccOrder tt = tsMap.get(rs.getString("BAT_TRAN_CUST_ORDNO"));
            	if(tt!=null)tt.status="51";//订单号已经完成转账
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(rs, ps, con);
        }
	}
	public void transferAcc(String custType, String custId,List<PayTransferAccOrder> tsList) throws Exception {
		String sqlTo = "update PAY_ACC_PROFILE set CASH_AC_BAL=CASH_AC_BAL+?,CONS_AC_BAL=CONS_AC_BAL+? where AC_TYPE=? and PAY_AC_NO=?";
		String sqlSrc = "update PAY_ACC_PROFILE set CASH_AC_BAL=CASH_AC_BAL-?,CONS_AC_BAL=CONS_AC_BAL-? where" +
				" CASH_AC_BAL-?>0 and CONS_AC_BAL-?>0 and AC_TYPE=? and PAY_AC_NO=?";
		//手续费加到系统账户
		String sqlSys ="update PAY_ACC_PROFILE set CASH_AC_BAL=CASH_AC_BAL+?,CONS_AC_BAL=CONS_AC_BAL+? where AC_TYPE='"+PayConstant.CUST_TYPE_MERCHANT+"' and PAY_AC_NO=?";
		String sqlSave = "insert into PAY_TRANSFER_ACC_ORDER("+
	            "TRAN_ORDNO," + 
	            "TRAN_TIME," + 
	            "STATUS," + 
	            "TXAMT," + 
	            "TGET_ACC_NO," + 
	            "TGET_ACC_NAME," + 
	            "TOTAMT," + 
	            "CUST_ID," + 
	            "BAT_TRAN_CUST_ORDNO," + 
	            "BAT_NO," + 
	            "TRAN_TYPE," + 
	            "TRAN_SUCCESS_TIME," +
	            "CUST_TYPE," +
	            "TGET_ACC_TYPE," +
	            "CUST_NAME,FILED1,FILED2)values(?,to_date(?,'yyyy-mm-dd hh24:mi:ss'),?,?,?,?,?,?,?,?,?,to_date(?,'yyyy-mm-dd hh24:mi:ss'),?,?,?,?,?)";
		long totalAmt = 0l;
		long totalFee = 0l;
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = connection();
//            con.setAutoCommit(false);
            //转入打款
            ps = con.prepareStatement(sqlTo);
            int n=1;
            for(int i = 0; i<tsList.size(); i++){
            	PayTransferAccOrder tsOrder = tsList.get(i);
            	if(tsOrder.status==null || tsOrder.status.length() ==0){
            		n=1;
            		log.info(sqlTo);
            		log.info("CASH_AC_BAL="+tsOrder.txamt+";AC_TYPE="+tsOrder.tgetAccType+";PAY_AC_NO="+tsOrder.tgetAccNo);
            		ps.setLong(n++, tsOrder.txamt);
            		ps.setLong(n++, tsOrder.txamt);
            		ps.setString(n++, tsOrder.tgetAccType);
            		ps.setString(n++, tsOrder.tgetAccNo);
            		totalAmt+=tsOrder.totamt;
            		totalFee = totalFee+(tsOrder.totamt-tsOrder.txamt);
            		ps.addBatch();
            	}
            }
            ps.executeBatch();
        	ps.close();
        	//添加转账记录
        	ps = con.prepareStatement(sqlSave);
        	for(int i = 0; i<tsList.size(); i++){
            	PayTransferAccOrder tsOrder = tsList.get(i);
            	log.info(sqlSave);
            	n=1;
        		ps.setString(n++,Tools.getUniqueIdentify());
                ps.setString(n++,new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(tsOrder.tranTime));
                ps.setString(n++,tsOrder.status==null || tsOrder.status.length()==0?"01":tsOrder.status);
                ps.setLong(n++,tsOrder.txamt);
                ps.setString(n++,tsOrder.tgetAccNo);
                ps.setString(n++,tsOrder.tgetAccName);
                ps.setLong(n++,tsOrder.totamt);
                ps.setString(n++,tsOrder.custId);
                ps.setString(n++,tsOrder.batTranCustOrdno);
                ps.setString(n++,tsOrder.batNo);
                ps.setString(n++,"1");
                ps.setString(n++,new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(tsOrder.tranSuccessTime));
                ps.setString(n++,tsOrder.custType);
                ps.setString(n++,tsOrder.tgetAccType);
                ps.setString(n++,tsOrder.custName);
                ps.setString(n++,tsOrder.filed1);
                ps.setString(n++,tsOrder.filed2);
        		ps.addBatch();
            }
            ps.executeBatch();
        	ps.close();
        	//添加手续费
        	ps = con.prepareStatement(sqlSys);
        	log.info(sqlSys);
        	log.info("CASH_AC_BAL="+totalFee+";PAY_AC_NO="+PayConstant.PAY_CONFIG.get("PAY_SYS_ACCOUNT"));
        	n=1;
    		ps.setLong(n++, totalFee);
    		ps.setLong(n++, totalFee);
    		ps.setString(n++, PayConstant.PAY_CONFIG.get("PAY_SYS_ACCOUNT"));
    		ps.executeUpdate();
        	ps.close();
        	//转出账户减款
        	ps = con.prepareStatement(sqlSrc);
        	log.info(sqlSrc);
        	log.info("CONS_AC_BAL="+totalAmt+";AC_TYPE="+custType+";PAY_AC_NO="+custId);
        	n=1;
        	ps.setLong(n++, totalAmt);
    		ps.setLong(n++, totalAmt);
    		ps.setLong(n++, totalAmt);
    		ps.setLong(n++, totalAmt);
    		ps.setString(n++, custType);
    		ps.setString(n++, custId);
    		//转出账户余额不足
    		if(ps.executeUpdate()==0)throw new Exception("转出账户余额不足");
//    		else con.commit();
        } catch (Exception e) {
            e.printStackTrace();
//            con.rollback();
            throw e;
        } finally {
//        	con.setAutoCommit(true);
            close(null, ps, con);
        }
	}
	/**
	 * 查询总金额
	 * @param payTransferAccOrder
	 * @return
	 */
	public Long[] getTotalTransferAccMoney(
			PayTransferAccOrder payTransferAccOrder) {
		String sqlCon = setPayTransferAccOrderSql(payTransferAccOrder);
        String sql = 
        		" SELECT SUM(TXAMT) totalTxamt,SUM(TOTAMT) totalFeeMoney "+
        		" FROM PAY_TRANSFER_ACC_ORDER " + (sqlCon.length()==0?"":" where "+ sqlCon)+
        		" ORDER BY TRAN_TIME DESC";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            int n = 1;
            setPayTransferAccOrderParameter(ps,payTransferAccOrder,n);
            rs = ps.executeQuery();
            if (rs.next()) {
            	Long l1 = rs.getLong("totalTxamt");
            	Long l2 = rs.getLong("totalFeeMoney");
                return new Long[]{l1==null?0l:l1,l2==null?0l:l2};
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(rs, ps, con);
        }
        return new Long[]{0l,0l,0l};
	}
}