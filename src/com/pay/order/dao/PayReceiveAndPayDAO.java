package com.pay.order.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import util.JWebConstant;
import util.PayUtil;
import util.Tools;

import com.jweb.dao.BaseDAO;
import com.pay.merchantinterface.service.PayRequest;
/**
 * Table PAY_RECEIVE_AND_PAY DAO. 
 * @author Administrator
 *
 */
public class PayReceiveAndPayDAO  extends BaseDAO {
    private static final Log log = LogFactory.getLog(PayReceiveAndPayDAO.class);
    public static synchronized PayReceiveAndPay getPayReceiveAndPayValue(ResultSet rs)throws SQLException {
        PayReceiveAndPay payReceiveAndPay = new PayReceiveAndPay();
        payReceiveAndPay.id = rs.getString("ID");
        payReceiveAndPay.type = rs.getString("TYPE");
        payReceiveAndPay.custType = rs.getString("CUST_TYPE");
        payReceiveAndPay.custId = rs.getString("CUST_ID");
        payReceiveAndPay.channelId = rs.getString("CHANNEL_ID");
        payReceiveAndPay.merTranNo = rs.getString("MER_TRAN_NO");
        payReceiveAndPay.channelTranNo = rs.getString("CHANNEL_TRAN_NO");
        payReceiveAndPay.corpAcctNo = rs.getString("CORP_ACCT_NO");
        payReceiveAndPay.accountProp = rs.getString("ACCOUNT_PROP");
        payReceiveAndPay.timeliness = rs.getString("TIMELINESS");
        payReceiveAndPay.appointmentTime = rs.getString("APPOINTMENT_TIME");
        payReceiveAndPay.sn = rs.getString("SN");
        payReceiveAndPay.subMerchantId = rs.getString("SUB_MERCHANT_ID");
        payReceiveAndPay.accountType = rs.getString("ACCOUNT_TYPE");
        payReceiveAndPay.accountNo = rs.getString("ACCOUNT_NO");
        payReceiveAndPay.accountName = rs.getString("ACCOUNT_NAME");
        payReceiveAndPay.bankName = rs.getString("BANK_NAME");
        payReceiveAndPay.bankCode = rs.getString("BANK_CODE");
        payReceiveAndPay.drctBankCode = rs.getString("DRCT_BANK_CODE");
        payReceiveAndPay.protocolNo = rs.getString("PROTOCOL_NO");
        payReceiveAndPay.currency = rs.getString("CURRENCY");
        payReceiveAndPay.amount = rs.getLong("AMOUNT");
        payReceiveAndPay.idType = rs.getString("ID_TYPE");
        payReceiveAndPay.certId = rs.getString("CERT_ID");
        payReceiveAndPay.tel = rs.getString("TEL");
        payReceiveAndPay.summary = rs.getString("SUMMARY");
        payReceiveAndPay.status = rs.getString("STATUS");
        payReceiveAndPay.retCode = rs.getString("RET_CODE");
        payReceiveAndPay.errorMsg = rs.getString("ERROR_MSG");
        payReceiveAndPay.feeCode = rs.getString("FEE_CODE");
        payReceiveAndPay.feeInfo = rs.getString("FEE_INFO");
        payReceiveAndPay.batNo = rs.getString("BAT_NO");
        payReceiveAndPay.createTime = rs.getTimestamp("CREATE_TIME");
        payReceiveAndPay.bankGeneralName = rs.getString("BANK_GENERAL_NAME");
        payReceiveAndPay.fee = rs.getLong("FEE");
        payReceiveAndPay.feeChannel = rs.getLong("FEE_CHANNEL");
        payReceiveAndPay.completeTime = rs.getTimestamp("COMPLETE_TIME");
        payReceiveAndPay.tranType = rs.getString("TRAN_TYPE");
        payReceiveAndPay.receivePayNotifyUrl = rs.getString("RECEIVE_PAY_NOTIFY_URL");
        payReceiveAndPay.bussFromType = rs.getString("BUSS_FROM_TYPE");
        payReceiveAndPay.actime = rs.getTimestamp("ACTIME");
        payReceiveAndPay.stlbatno = rs.getString("STLBATNO");
        payReceiveAndPay.stlTime = rs.getTimestamp("STL_TIME");
        payReceiveAndPay.stlsts = rs.getString("STLSTS");
        payReceiveAndPay.preOptCashAcBal = rs.getLong("PRE_OPT_CASH_AC_BAL");
        payReceiveAndPay.deductMoneyFlag = rs.getString("DEDUCT_MONEY_FLAG");
        payReceiveAndPay.returnMoneyFlag = rs.getString("RETURN_MONEY_FLAG");
        return payReceiveAndPay;
    }
    public String addPayReceiveAndPay(PayReceiveAndPay payReceiveAndPay) throws Exception {
        String sql = "insert into PAY_RECEIVE_AND_PAY("+
            "ID," + 
            "TYPE," + 
            "CUST_TYPE," + 
            "CUST_ID," + 
            "CHANNEL_ID," + 
            "MER_TRAN_NO," + 
            "CHANNEL_TRAN_NO," + 
            "CORP_ACCT_NO," + 
            "ACCOUNT_PROP," + 
            "TIMELINESS," + 
            "APPOINTMENT_TIME," + 
            "SN," + 
            "SUB_MERCHANT_ID," + 
            "ACCOUNT_TYPE," + 
            "ACCOUNT_NO," + 
            "ACCOUNT_NAME," + 
            "BANK_NAME," + 
            "BANK_CODE," + 
            "DRCT_BANK_CODE," + 
            "PROTOCOL_NO," + 
            "CURRENCY," + 
            "AMOUNT," + 
            "ID_TYPE," + 
            "CERT_ID," + 
            "TEL," + 
            "SUMMARY," + 
            "STATUS," + 
            "RET_CODE," + 
            "ERROR_MSG," + 
            "FEE_CODE," + 
            "FEE_INFO," + 
            "BAT_NO," + 
            "BANK_GENERAL_NAME," + 
            "FEE," + 
            "FEE_CHANNEL,TRAN_TYPE,RECEIVE_PAY_NOTIFY_URL,BUSS_FROM_TYPE,PRE_OPT_CASH_AC_BAL)values(" +
            	"?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?," +
            	"(select CASH_AC_BAL from PAY_ACC_PROFILE where ac_type=? and PAY_AC_NO=?))";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            log.info(payReceiveAndPay.toString().replaceAll("\n",";"));
            int n=1;
            ps.setString(n++,payReceiveAndPay.id);
            ps.setString(n++,payReceiveAndPay.type);
            ps.setString(n++,payReceiveAndPay.custType);
            ps.setString(n++,payReceiveAndPay.custId);
            ps.setString(n++,payReceiveAndPay.channelId);
            ps.setString(n++,payReceiveAndPay.merTranNo);
            ps.setString(n++,payReceiveAndPay.channelTranNo);
            ps.setString(n++,payReceiveAndPay.corpAcctNo);
            ps.setString(n++,payReceiveAndPay.accountProp);
            ps.setString(n++,payReceiveAndPay.timeliness);
            ps.setString(n++,payReceiveAndPay.appointmentTime);
            ps.setString(n++,payReceiveAndPay.sn);
            ps.setString(n++,payReceiveAndPay.subMerchantId);
            ps.setString(n++,payReceiveAndPay.accountType);
            ps.setString(n++,payReceiveAndPay.accountNo);
            ps.setString(n++,payReceiveAndPay.accountName);
            ps.setString(n++,payReceiveAndPay.bankName);
            ps.setString(n++,payReceiveAndPay.bankCode);
            ps.setString(n++,payReceiveAndPay.drctBankCode);
            ps.setString(n++,payReceiveAndPay.protocolNo);
            ps.setString(n++,payReceiveAndPay.currency);
            ps.setLong(n++,payReceiveAndPay.amount);
            ps.setString(n++,payReceiveAndPay.idType);
            ps.setString(n++,payReceiveAndPay.certId);
            ps.setString(n++,payReceiveAndPay.tel);
            ps.setString(n++,payReceiveAndPay.summary);
            ps.setString(n++,payReceiveAndPay.status);
            ps.setString(n++,payReceiveAndPay.retCode);
            ps.setString(n++,payReceiveAndPay.errorMsg);
            ps.setString(n++,payReceiveAndPay.feeCode);
            ps.setString(n++,payReceiveAndPay.feeInfo);
            ps.setString(n++,payReceiveAndPay.batNo);
            ps.setString(n++,payReceiveAndPay.bankGeneralName);
            ps.setLong(n++,payReceiveAndPay.fee);
            ps.setLong(n++,payReceiveAndPay.feeChannel);
            ps.setString(n++,payReceiveAndPay.tranType);
            ps.setString(n++,payReceiveAndPay.receivePayNotifyUrl==null?"":payReceiveAndPay.receivePayNotifyUrl);
            ps.setString(n++,payReceiveAndPay.bussFromType==null||payReceiveAndPay.bussFromType.length()==0?"0":payReceiveAndPay.bussFromType);
            ps.setString(n++,payReceiveAndPay.custType);
            ps.setString(n++,payReceiveAndPay.custId);
            ps.executeUpdate();
            return "";
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }finally {
            close(null, ps, con);
        }
    }
    public String addPayReceiveAndPayBatch(PayRequest payRequest) throws Exception {
        String sql = "insert into PAY_RECEIVE_AND_PAY("+
            "ID," + 
            "TYPE," + 
            "CUST_TYPE," + 
            "CUST_ID," + 
            "CHANNEL_ID," + 
            "MER_TRAN_NO," + 
            "CHANNEL_TRAN_NO," + 
            "CORP_ACCT_NO," + 
            "ACCOUNT_PROP," + 
            "TIMELINESS," + 
            "APPOINTMENT_TIME," + 
            "SN," + 
            "SUB_MERCHANT_ID," + 
            "ACCOUNT_TYPE," + 
            "ACCOUNT_NO," + 
            "ACCOUNT_NAME," + 
            "BANK_NAME," + 
            "BANK_CODE," + 
            "DRCT_BANK_CODE," + 
            "PROTOCOL_NO," + 
            "CURRENCY," + 
            "AMOUNT," + 
            "ID_TYPE," + 
            "CERT_ID," + 
            "TEL," + 
            "SUMMARY," + 
            "STATUS," + 
            "RET_CODE," + 
            "ERROR_MSG," + 
            "FEE_CODE," + 
            "FEE_INFO," + 
            "BAT_NO," + 
            "BANK_GENERAL_NAME," + 
            "FEE," + 
            "FEE_CHANNEL,TRAN_TYPE,RECEIVE_PAY_NOTIFY_URL,PRE_OPT_CASH_AC_BAL)values(" +
            "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?," +
            "(select CASH_AC_BAL from PAY_ACC_PROFILE where ac_type=? and PAY_AC_NO=?))";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            for(int i=0; i<payRequest.receivePayList.size(); i++){
            	PayReceiveAndPay payReceiveAndPay = payRequest.receivePayList.get(i);
            	log.info(payReceiveAndPay.toString().replaceAll("\n",";"));
	            int n=1;
	            ps.setString(n++,payReceiveAndPay.id);
	            ps.setString(n++,payReceiveAndPay.type);
	            ps.setString(n++,payReceiveAndPay.custType);
	            ps.setString(n++,payReceiveAndPay.custId);
	            ps.setString(n++,payReceiveAndPay.channelId);
	            ps.setString(n++,payReceiveAndPay.merTranNo);
	            ps.setString(n++,payReceiveAndPay.channelTranNo);
	            ps.setString(n++,payReceiveAndPay.corpAcctNo);
	            ps.setString(n++,payReceiveAndPay.accountProp);
	            ps.setString(n++,payReceiveAndPay.timeliness);
	            ps.setString(n++,payReceiveAndPay.appointmentTime);
	            ps.setString(n++,payReceiveAndPay.sn);
	            ps.setString(n++,payReceiveAndPay.subMerchantId);
	            ps.setString(n++,payReceiveAndPay.accountType);
	            ps.setString(n++,payReceiveAndPay.accountNo);
	            ps.setString(n++,payReceiveAndPay.accountName);
	            ps.setString(n++,payReceiveAndPay.bankName);
	            ps.setString(n++,payReceiveAndPay.bankCode);
	            ps.setString(n++,payReceiveAndPay.drctBankCode);
	            ps.setString(n++,payReceiveAndPay.protocolNo);
	            ps.setString(n++,payReceiveAndPay.currency);
	            ps.setLong(n++,payReceiveAndPay.amount);
	            ps.setString(n++,payReceiveAndPay.idType);
	            ps.setString(n++,payReceiveAndPay.certId);
	            ps.setString(n++,payReceiveAndPay.tel);
	            ps.setString(n++,payReceiveAndPay.summary);
	            ps.setString(n++,payReceiveAndPay.status);
	            if("0".equals(JWebConstant.SYS_CONFIG.get("DEBUG"))){
	            	ps.setString(n++,"000");
		            ps.setString(n++,"处理完成");
	            } else {
	            	ps.setString(n++,"074");
		            ps.setString(n++,"处理中");
	            }
	            ps.setString(n++,payReceiveAndPay.feeCode);
	            ps.setString(n++,payReceiveAndPay.feeInfo);
	            ps.setString(n++,payReceiveAndPay.batNo);
	            ps.setString(n++,payReceiveAndPay.bankGeneralName);
	            ps.setLong(n++,payReceiveAndPay.fee);
	            ps.setLong(n++,payReceiveAndPay.feeChannel);
	            ps.setString(n++,payReceiveAndPay.tranType);
	            ps.setString(n++,payReceiveAndPay.receivePayNotifyUrl==null?"":payReceiveAndPay.receivePayNotifyUrl);
	            ps.setString(n++,payReceiveAndPay.custType);
	            ps.setString(n++,payReceiveAndPay.custId);
	            ps.addBatch();
            }
            ps.executeBatch();
            return "";
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }finally {
            close(null, ps, con);
        }
    }
    public void receivePayQuery(PayRequest payRequest) throws Exception{
        String sql = "select * from PAY_RECEIVE_AND_PAY where CUST_ID=? and MER_TRAN_NO=?";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            ps.setString(1,payRequest.merchantId);
            ps.setString(2,payRequest.queryTranId);
            rs = ps.executeQuery();
            while(rs.next()){
            	PayReceiveAndPay rp = getPayReceiveAndPayValue(rs);
            	if(payRequest.tranId.length()==0)payRequest.tranId=rp.merTranNo;
            	payRequest.receivePayList.add(rp);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(rs, ps, con);
        }
    }
    public boolean existTranIdSnIds(PayRequest request) throws Exception {
		String sql = "select * from PAY_RECEIVE_AND_PAY where ";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sqlTmp = "";
        try {
            con = connection();
            for(int i=0; i<request.receivePayList.size(); i++){
            	PayReceiveAndPay rp = request.receivePayList.get(i);
            	if(rp.sn.length()>0)
    			sqlTmp = sqlTmp + " SN='"+request.merchantId+"_"+rp.sn+
    					"' or ";
            }
            sqlTmp += " CHANNEL_TRAN_NO='"+request.merchantId+"_"+request.tranId+"'";
            log.info(sql+sqlTmp);
            ps = con.prepareStatement(sql+sqlTmp);
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
    /**
     * Set query condition sql.
     * @param payReceiveAndPay
     * @return
     */
    private String setPayReceiveAndPaySql(PayReceiveAndPay payReceiveAndPay) {
        StringBuffer sql = new StringBuffer();
        if(payReceiveAndPay.id != null && payReceiveAndPay.id.length() !=0) {
            sql.append(" ID = ? and ");
        }
        if(payReceiveAndPay.type != null && payReceiveAndPay.type.length() !=0) {
            sql.append(" TYPE = ? and ");
        }
        if(payReceiveAndPay.custType != null && payReceiveAndPay.custType.length() !=0) {
            sql.append(" CUST_TYPE = ? and ");
        }
        if(payReceiveAndPay.custId != null && payReceiveAndPay.custId.length() !=0) {
            sql.append(" CUST_ID = ? and ");
        }
        if(payReceiveAndPay.merTranNo != null && payReceiveAndPay.merTranNo.length() !=0) {
            sql.append(" MER_TRAN_NO = ? and ");
        }
        if(payReceiveAndPay.sn != null && payReceiveAndPay.sn.length() !=0) {
            sql.append(" SN = ? and ");
        }
        if(payReceiveAndPay.accountNo != null && payReceiveAndPay.accountNo.length() !=0) {
            sql.append(" ACCOUNT_NO = ? and ");
        }
        if(payReceiveAndPay.accountName != null && payReceiveAndPay.accountName.length() !=0) {
            sql.append(" ACCOUNT_NAME = ? and ");
        }
        if(payReceiveAndPay.certId != null && payReceiveAndPay.certId.length() !=0) {
            sql.append(" CERT_ID = ? and ");
        }
        if(payReceiveAndPay.tel != null && payReceiveAndPay.tel.length() !=0) {
            sql.append(" TEL = ? and ");
        }
        if(payReceiveAndPay.status != null && payReceiveAndPay.status.length() !=0) {
            sql.append(" STATUS = ? and ");
        }
        if(payReceiveAndPay.batNo != null && payReceiveAndPay.batNo.length() !=0) {
            sql.append(" BAT_NO = ? and ");
        }
        if(payReceiveAndPay.channelId != null && payReceiveAndPay.channelId.length() !=0) {
            sql.append(" CHANNEL_ID = ? and ");
        }
        if(payReceiveAndPay.bussFromType != null && payReceiveAndPay.bussFromType.length() !=0) {
        	sql.append(" BUSS_FROM_TYPE = ? and ");
        }
        if(payReceiveAndPay.deductMoneyFlag != null && payReceiveAndPay.deductMoneyFlag.length() !=0) {
        	sql.append(" DEDUCT_MONEY_FLAG = ? and ");
        }
        if(payReceiveAndPay.returnMoneyFlag != null && payReceiveAndPay.returnMoneyFlag.length() !=0) {
        	sql.append(" RETURN_MONEY_FLAG = ? and ");
        }
        if(payReceiveAndPay.createTimeStart != null && payReceiveAndPay.createTimeStart.length()!=0) {
            sql.append(" CREATE_TIME >= to_date(?,'yyyy-mm-dd hh24:mi:ss') and ");
        }
        if(payReceiveAndPay.createTimeEnd != null && payReceiveAndPay.createTimeEnd.length()!=0) {
            sql.append(" CREATE_TIME <= to_date(?,'yyyy-mm-dd hh24:mi:ss') and ");
        }
        String tmp = sql.toString();
        if(tmp.length()>0)tmp=tmp.substring(0,tmp.lastIndexOf(" and "));
        return tmp;
    }
    /**
     * Set query parameter.
     * @param ps
     * @param payReceiveAndPay
     * @param n
     * @return
     * @throws SQLException
     */
    private int setPayReceiveAndPayParameter(PreparedStatement ps,PayReceiveAndPay payReceiveAndPay,int n)throws SQLException {
    	if(payReceiveAndPay.id != null && payReceiveAndPay.id.length() !=0) {
    		ps.setString(n++,payReceiveAndPay.id);
    	}
        if(payReceiveAndPay.type != null && payReceiveAndPay.type.length() !=0) {
            ps.setString(n++,payReceiveAndPay.type);
        }
        if(payReceiveAndPay.custType != null && payReceiveAndPay.custType.length() !=0) {
            ps.setString(n++,payReceiveAndPay.custType);
        }
        if(payReceiveAndPay.custId != null && payReceiveAndPay.custId.length() !=0) {
            ps.setString(n++,payReceiveAndPay.custId);
        }
        if(payReceiveAndPay.merTranNo != null && payReceiveAndPay.merTranNo.length() !=0) {
            ps.setString(n++,payReceiveAndPay.merTranNo);
        }
        if(payReceiveAndPay.sn != null && payReceiveAndPay.sn.length() !=0) {
            ps.setString(n++,payReceiveAndPay.sn);
        }
        if(payReceiveAndPay.accountNo != null && payReceiveAndPay.accountNo.length() !=0) {
            ps.setString(n++,payReceiveAndPay.accountNo);
        }
        if(payReceiveAndPay.accountName != null && payReceiveAndPay.accountName.length() !=0) {
            ps.setString(n++,payReceiveAndPay.accountName);
        }
        if(payReceiveAndPay.certId != null && payReceiveAndPay.certId.length() !=0) {
            ps.setString(n++,payReceiveAndPay.certId);
        }
        if(payReceiveAndPay.tel != null && payReceiveAndPay.tel.length() !=0) {
            ps.setString(n++,payReceiveAndPay.tel);
        }
        if(payReceiveAndPay.status != null && payReceiveAndPay.status.length() !=0) {
            ps.setString(n++,payReceiveAndPay.status);
        }
        if(payReceiveAndPay.batNo != null && payReceiveAndPay.batNo.length() !=0) {
            ps.setString(n++,payReceiveAndPay.batNo);
        }//渠道号
        if(payReceiveAndPay.channelId != null && payReceiveAndPay.channelId.length() !=0) {
            ps.setString(n++,payReceiveAndPay.channelId);
        }
        if(payReceiveAndPay.bussFromType != null && payReceiveAndPay.bussFromType.length() !=0) {
        	ps.setString(n++,payReceiveAndPay.bussFromType);
        }
        if(payReceiveAndPay.deductMoneyFlag != null && payReceiveAndPay.deductMoneyFlag.length() !=0) {
        	ps.setString(n++,payReceiveAndPay.deductMoneyFlag);
        }
        if(payReceiveAndPay.returnMoneyFlag != null && payReceiveAndPay.returnMoneyFlag.length() !=0) {
        	ps.setString(n++,payReceiveAndPay.returnMoneyFlag);
        }
        if(payReceiveAndPay.createTimeStart != null && payReceiveAndPay.createTimeStart.length()!=0) {
        	ps.setString(n++, payReceiveAndPay.createTimeStart+" 00:00:00");
        }
        if(payReceiveAndPay.createTimeEnd != null && payReceiveAndPay.createTimeEnd.length()!=0) {
        	ps.setString(n++, payReceiveAndPay.createTimeEnd+" 23:59:59");
        }
        return n;
    }
    /**
     * Get records count.
     * @param payReceiveAndPay
     * @return
     */
    public int getPayReceiveAndPayCount(PayReceiveAndPay payReceiveAndPay) {
        String sqlCon = setPayReceiveAndPaySql(payReceiveAndPay);
        String sql = "select count(rownum) recordCount from PAY_RECEIVE_AND_PAY " +(sqlCon.length()==0?"":" where "+ sqlCon);
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            int n = 1;
            setPayReceiveAndPayParameter(ps,payReceiveAndPay,n);
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
     * 查询代付审核分组数
     * @param payReceiveAndPay
     * @return
     */
    public int getPayReceiveAndPayCheckCount(PayReceiveAndPay payReceiveAndPay) {
    	String sqlCon = setPayReceiveAndPaySql(payReceiveAndPay);
    	String sql = "select count(*) recordCount from (select count(bat_no) from PAY_RECEIVE_AND_PAY " +(sqlCon.length()==0?"":" where "+ sqlCon)+" group by bat_no) tmp ";
    	log.info(sql);
    	Connection con = null;
    	PreparedStatement ps = null;
    	ResultSet rs = null;
    	try {
    		con = connection();
    		ps = con.prepareStatement(sql);
    		int n = 1;
    		setPayReceiveAndPayParameter(ps,payReceiveAndPay,n);
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
     * @param payReceiveAndPay
     * @param page
     * @param rows
     * @param sort
     * @param order
     * @return
     * @throws Exception
     */
    public List getPayReceiveAndPayList(PayReceiveAndPay payReceiveAndPay,int page,int rows,String sort,String order) throws Exception{
        sort = Tools.paraNameToDBColumnName(sort);
        String sqlCon = setPayReceiveAndPaySql(payReceiveAndPay);
        String sortOrder = sort == null || sort.length()==0?" order by CREATE_TIME desc":(" order by " + sort +("desc".equals(order)?" desc ":" asc "));
        String sql = "select * from (" +
                "  select rownum rowno,tmp1.* from (" +
                "   select tmp.*  from PAY_RECEIVE_AND_PAY tmp " +(sqlCon.length()==0?"":" where "+ sqlCon) +sortOrder+
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
            setPayReceiveAndPayParameter(ps,payReceiveAndPay,n);
            rs = ps.executeQuery();
            while(rs.next()){
                list.add(getPayReceiveAndPayValue(rs));
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
     * detail PayReceiveAndPay
     * @param id
     * @return PayReceiveAndPay
     * @throws Exception
     */
    public PayReceiveAndPay detailPayReceiveAndPay(String id) throws Exception {
        String sql = "select * from PAY_RECEIVE_AND_PAY where ID=?";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            ps.setString(1,id);
            rs = ps.executeQuery();
            if(rs.next())return getPayReceiveAndPayValue(rs); 
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(rs, ps, con);
        }
        return null;
    }
    /**
     * detail PayReceiveAndPay
     * @param id
     * @return PayReceiveAndPay
     * @throws Exception
     */
    public List getBatchPayReceiveAndPayById(String id) throws Exception {
        String sql = "select * from PAY_RECEIVE_AND_PAY where bat_no=(select bat_no from PAY_RECEIVE_AND_PAY where ID=?)";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List list = new ArrayList();
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            ps.setString(1,id);
            rs = ps.executeQuery();
            while(rs.next())list.add(getPayReceiveAndPayValue(rs)); 
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(rs, ps, con);
        }
        return list;
    }
    /**
     * remove PayReceiveAndPay
     * @param id
     * @throws Exception     
     */
    public int updatePayReceiveAndPay(List<PayReceiveAndPay> list) throws Exception {
        String sql = "update PAY_RECEIVE_AND_PAY set STATUS=?,RET_CODE=?,ERROR_MSG=?,COMPLETE_TIME=sysdate where SN=?";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        int count = 0;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            for(int i=0; i<list.size(); i++){
            	PayReceiveAndPay rp = list.get(i);
            	if("1".equals(rp.status))rp.retCode="000";
                else if("2".equals(rp.status))rp.retCode="-1";
            	log.info("STATUS="+rp.status+";RET_CODE="+rp.retCode+";ERROR_MSG="+rp.errorMsg+";SN="+rp.sn);
            	int n=1;
            	ps.setString(n++,rp.status);
            	ps.setString(n++,rp.retCode);
            	ps.setString(n++,rp.errorMsg);
            	ps.setString(n++,rp.sn);
            	ps.addBatch();
            	if("000".equals(rp.retCode))count++;
            }
            ps.executeBatch();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(null, ps, con);
        }
        return count;
    }
    /**
     * remove PayReceiveAndPay
     * @param id
     * @throws Exception     
     */
    public int updatePayReceiveAndPayById(PayReceiveAndPay rp) throws Exception {
        String sql = "update PAY_RECEIVE_AND_PAY set STATUS=?,RET_CODE=?,ERROR_MSG=?,COMPLETE_TIME=sysdate,CHANNEL_TRAN_NO=? where ID=?";
        log.info(sql);
        if("1".equals(rp.status))rp.retCode="000";
        else if("2".equals(rp.status))rp.retCode="-1";
        log.info("STATUS="+rp.status+";RET_CODE="+rp.retCode+";ERROR_MSG="+rp.errorMsg+";CHANNEL_TRAN_NO="+rp.channelTranNo+";ID="+rp.id);
        Connection con = null;
        PreparedStatement ps = null;
        int count = 0;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
        	int n=1;
        	ps.setString(n++,rp.status);
        	ps.setString(n++,rp.retCode);
        	ps.setString(n++,rp.errorMsg);
        	ps.setString(n++,rp.channelTranNo);
        	ps.setString(n++,rp.id);
        	ps.execute();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(null, ps, con);
        }
        return count;
    }
    /**
	 * 查询代收付的总金额
	 * @param payReceiveAndPay
	 * @return
	 */
    public Long [] getTotalReceiveAndPayMoney(PayReceiveAndPay payReceiveAndPay) {
    	Long l1 = null,l2 = null;
    	String sqlCon = setPayReceiveAndPaySql(payReceiveAndPay);
        String sql = 
        		" SELECT SUM(tmp.AMOUNT) totalReceiveAndPayMoney,SUM(tmp.FEE) totalReceiveAndPayFeeMoney " +
        				" FROM" +
                		" (SELECT * FROM PAY_RECEIVE_AND_PAY ) tmp" +
                		(sqlCon.length()==0?"":" where "+ sqlCon);
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            int n = 1;
            setPayReceiveAndPayParameter(ps,payReceiveAndPay,n);
            rs = ps.executeQuery();
            if (rs.next()) {
            	l1 = rs.getLong("totalReceiveAndPayMoney");
            	l2 = rs.getLong("totalReceiveAndPayFeeMoney");
            }
            return new Long[]{l1==null?0l:l1,l2==null?0l:l2};
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(rs, ps, con);
        }
        return new Long[]{0l,0l};
    }
    /**
     * remove PayReceiveAndPay
     * @param id
     * @throws Exception     
     */
    public void updatePayReceiveAndPayForStlsts(List<PayReceiveAndPay> list) throws Exception {
        String sql = "update PAY_RECEIVE_AND_PAY set ACTIME=sysdate,STL_TIME=sysdate,STLSTS='2' where ID=?";
        log.info(sql);
        
        Connection con = null;
        PreparedStatement ps = null;

        try {
            con = connection();
            ps = con.prepareStatement(sql);
            for(int i=0; i<list.size(); i++){
	        	PayReceiveAndPay rp = list.get(i);
	        	log.info("ID="+rp.id);
	        	ps.setString(1,rp.id);
	        	ps.addBatch();
            }
        	ps.executeBatch();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(null, ps, con);
        }
    }
	public List getPayReceiveAndPayList(PayReceiveAndPay payReceiveAndPay,
			long start, long end) throws Exception {
		String sqlCon = setPayReceiveAndPaySql(payReceiveAndPay);
        String sql = "select * from (" +
        		"  select rownum rowno,tmp1.* from (" +
        		 "   select tmp.*  from PAY_RECEIVE_AND_PAY tmp " +(sqlCon.length()==0?"":" where "+ sqlCon) + " order by CREATE_TIME desc" +
                "  ) tmp1 "+
                ")  where rowno > "+start+ " and rowno<= " + end + " order by CREATE_TIME desc";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List list = new ArrayList();
        try {
            con = connection();
            ps = con.prepareStatement(sql.toString());
            int n = 1;
            setPayReceiveAndPayParameter(ps,payReceiveAndPay,n);
            rs = ps.executeQuery();
            while(rs.next()){
            	list.add(getPayReceiveAndPayValue(rs));
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(rs, ps, con);
        }
	}
	public void settlementReceiveAndPay(String id) throws Exception{
		String sql = "update PAY_RECEIVE_AND_PAY set STLSTS = ? , STL_TIME = sysdate where id = ? ";
		log.info(sql);
		log.info("STLSTS=2"+";id="+id);
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
        	int n=1;
        	ps.setString(n++,"2");
        	ps.setString(n++,id);
        	ps.execute();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(null, ps, con);
        }
	}
	public void returnReceiveAndPayAcc(String id) throws Exception{
		String sql = "update PAY_RECEIVE_AND_PAY set RETURN_MONEY_FLAG='1',SUMMARY=SUMMARY||';退回时间"+
				new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())+"' where id = ? ";
		log.info(sql);
		log.info("ID="+id);
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
        	ps.setString(1,id);
        	ps.execute();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(null, ps, con);
        }
	}
	/**
	 * 查询代付列表
	 * @param payReceiveAndPay
	 * @param page
	 * @param rows
	 * @param sort
	 * @param order
	 * @return
	 * @throws Exception
	 */
	public List<PayReceiveAndPay> getPayReceiveAndPayCheckList(PayReceiveAndPay payReceiveAndPay,int page,int rows,String sort,String order) throws Exception{
        String sqlCon = setPayReceiveAndPaySql(payReceiveAndPay);
        String sql = "select * from (" +
                "  select rownum rowno,tmp1.* from (" +
                "  select tmp.BAT_NO,count( tmp.BAT_NO) cou,max(tmp.cust_id) cust_id,max(tmp.create_time) create_time,max(tmp.tran_type) tran_type,sum(tmp.amount) amount,sum(tmp.fee) fee "+
                "from PAY_RECEIVE_AND_PAY tmp " +(sqlCon.length()==0?"":" where "+ sqlCon) + " GROUP BY tmp.BAT_NO order by CREATE_TIME desc "+
                "  ) tmp1 "+
                ")  where rowno > "+((page-1)*rows)+ " and rowno<= " + (page*rows) + " order by CREATE_TIME desc ";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<PayReceiveAndPay> list = new ArrayList<PayReceiveAndPay>();
        try {
            con = connection();
            ps = con.prepareStatement(sql.toString());
            int n = 1;
            setPayReceiveAndPayParameter(ps,payReceiveAndPay,n);
            rs = ps.executeQuery();
            while(rs.next()){
            	PayReceiveAndPay receiveAndPay = new PayReceiveAndPay();
            	receiveAndPay.custId = rs.getString("CUST_ID");
            	receiveAndPay.tranType = rs.getString("TRAN_TYPE");
            	receiveAndPay.batNo = rs.getString("BAT_NO");
            	receiveAndPay.fee = rs.getLong("FEE");
            	receiveAndPay.amount = rs.getLong("AMOUNT");
            	receiveAndPay.cou = rs.getLong("cou");
            	receiveAndPay.createTime = rs.getTimestamp("CREATE_TIME");
            	list.add(receiveAndPay);
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(rs, ps, con);
        }
    }
	public void updatePayReceiveAndPaySn(PayReceiveAndPay rp)throws Exception{
		String sql = "update PAY_RECEIVE_AND_PAY set SN = ? ,BAT_NO=? where id = ? ";
		log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
        	int n=1;
        	ps.setString(n++,rp.sn);
        	ps.setString(n++,rp.batNo);
        	ps.setString(n++,rp.id);
        	ps.execute();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(null, ps, con);
        }
	}
	/**
	 * 更新代收付状态。
	 * @param rp
	 * @throws Exception
	 */
	public void updatePayReceiveAndPaystatus(PayReceiveAndPay rp)throws Exception{
		String sql = "update PAY_RECEIVE_AND_PAY set STATUS=?,RET_CODE=?,ERROR_MSG=? where SN = ? ";
		log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
        	int n=1;
        	ps.setString(n++,rp.status);
        	ps.setString(n++,rp.retCode);
        	ps.setString(n++,rp.errorMsg);
        	ps.setString(n++,rp.sn);
        	ps.execute();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(null, ps, con);
        }
	}
	public PayReceiveAndPay getRPByChannelTranNo(String channelTranNo) throws Exception{
		String sql = "select * from PAY_RECEIVE_AND_PAY where CHANNEL_TRAN_NO=?";
        log.info(sql);
        log.info("CHANNEL_TRAN_NO="+channelTranNo);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            ps.setString(1,channelTranNo);
            rs = ps.executeQuery();
            if(rs.next())return getPayReceiveAndPayValue(rs); 
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(rs, ps, con);
        }
        return null;
	}
	public boolean busIsOpen(String merno,String type){
		String sql = "select MERNO from PAY_OPENED_MER_PAY where MERNO='"+merno+"' and TYPE='"+type+"'";
        log.info(sql);
        log.info("MERNO="+merno+",TYPE="+type);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            if(rs.next())return true; 
        } catch (Exception e) {
            log.info(PayUtil.exceptionToString(e));
        } finally {
            close(rs, ps, con);
        }
        return false;		
	}
}