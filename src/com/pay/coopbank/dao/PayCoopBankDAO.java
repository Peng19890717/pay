package com.pay.coopbank.dao;

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
import com.pay.bank.dao.PayBank;
import com.pay.bank.dao.PayBankDAO;
import com.pay.cardbin.service.PayCardBinService;
import com.pay.coopbank.service.PayCoopBankService;
import com.pay.fee.dao.PayFeeRate;
import com.pay.fee.dao.PayFeeRateDAO;
/**
 * Table PAY_COOP_BANK DAO. 
 * @author Administrator
 *
 */
public class PayCoopBankDAO  extends BaseDAO {
    private static final Log log = LogFactory.getLog(PayCoopBankDAO.class);
    public static synchronized PayCoopBank getPayCoopBankValue(ResultSet rs)throws SQLException {
        PayCoopBank payCoopBank = new PayCoopBank();
        payCoopBank.bankCode = rs.getString("BANK_CODE");
        payCoopBank.paySysId = rs.getString("PAY_SYS_ID");
        payCoopBank.bankName = rs.getString("BANK_NAME");
        payCoopBank.bankRelNo = rs.getString("BANK_REL_NO");
        payCoopBank.bankStatus = rs.getString("BANK_STATUS");
        payCoopBank.accountTime = rs.getTimestamp("ACCOUNT_TIME");
        payCoopBank.trtBankFlg = rs.getString("TRT_BANK_FLG");
        payCoopBank.stlAcNo = rs.getString("STL_AC_NO");
        payCoopBank.certNo = rs.getString("CERT_NO");
        payCoopBank.regNo = rs.getString("REG_NO");
        payCoopBank.legRep = rs.getString("LEG_REP");
        payCoopBank.custMgr = rs.getString("CUST_MGR");
        payCoopBank.telNo = rs.getString("TEL_NO");
        payCoopBank.bankEmail = rs.getString("BANK_EMAIL");
        payCoopBank.bankAddress = rs.getString("BANK_ADDRESS");
        payCoopBank.bizRange = rs.getString("BIZ_RANGE");
        payCoopBank.remark = rs.getString("REMARK");
        payCoopBank.creOperId = rs.getString("CRE_OPER_ID");
        payCoopBank.creTime = rs.getTimestamp("CRE_TIME");
        payCoopBank.lstUptOperId = rs.getString("LST_UPT_OPER_ID");
        payCoopBank.lstUptTime = rs.getTimestamp("LST_UPT_TIME");
        payCoopBank.depSubCode = rs.getString("DEP_SUB_CODE");
        payCoopBank.paySubCode = rs.getString("PAY_SUB_CODE");
        payCoopBank.recSubCode = rs.getString("REC_SUB_CODE");
        payCoopBank.recerrSubCode = rs.getString("RECERR_SUB_CODE");
        payCoopBank.accountOnline = rs.getString("ACCOUNT_ONLINE");
        payCoopBank.refundOnline = rs.getString("REFUND_ONLINE");
        payCoopBank.payQuickFlag = rs.getString("PAY_QUICK_FLAG");
        payCoopBank.payWebFlag = rs.getString("PAY_WEB_FLAG");
        payCoopBank.payWithdrawFlag = rs.getString("PAY_WITHDRAW_FLAG");
        payCoopBank.payReceiveFlag = rs.getString("PAY_RECEIVE_FLAG");
        payCoopBank.quickResendSms = rs.getString("QUICK_RESEND_SMS");
        payCoopBank.stlTime = rs.getString("STL_TIME");
        payCoopBank.stlTimeType = rs.getString("STL_TIME_TYPE");
        payCoopBank.payScanFlag = rs.getString("PAY_SCAN_FLAG");
        return payCoopBank;
    }
    
    public static synchronized PayCoopBank setPayCoopBankRateFee(PayCoopBank payCoopBank,ResultSet rs)throws SQLException {
    //交易类型 1消费 2充值 3结算 4退款 5提现 6转账
    //	public static String TRAN_TYPE_CONSUME="1";
    //	public static String TRAN_TYPE_RECHARGE="2";
    //	public static String TRAN_TYPE_SETTLE="3";
    //	public static String TRAN_TYPE_REFUND="4";
    //	public static String TRAN_TYPE_WITHDRAW="5";
    //	public static String TRAN_TYPE_TRANSFER="6";
    	while(rs.next()){
    		String tranType = rs.getString("TRAN_TYPE");
    		tranType = tranType == null ?"": tranType;
    		if(tranType.equals(PayConstant.TRAN_TYPE_CONSUME)){
    			payCoopBank.setFeeCode(rs.getString("FEE_CODE"));
    			payCoopBank.setFeeName(rs.getString("FEE_Name"));
    		}else if(tranType.equals(PayConstant.TRAN_TYPE_REFUND)){
    			payCoopBank.setTkFeeCode(rs.getString("FEE_CODE"));
    			payCoopBank.setTkFeeName(rs.getString("FEE_Name"));
    		}else if(tranType.equals(PayConstant.TRAN_TYPE_WITHDRAW)){
    			payCoopBank.setTxFeeCode(rs.getString("FEE_CODE"));
    			payCoopBank.setTxFeeName(rs.getString("FEE_Name"));
    		}else if(tranType.equals(PayConstant.TRAN_TYPE_TRANSFER)){
    			payCoopBank.setZzFeeCode(rs.getString("FEE_CODE"));
    			payCoopBank.setZzFeeName(rs.getString("FEE_Name"));
    		}else if(tranType.equals(PayConstant.TRAN_TYPE_B2CJJ)){
    			payCoopBank.setB2cjjFeeCode(rs.getString("FEE_CODE"));
    			payCoopBank.setB2cjjFeeName(rs.getString("FEE_Name"));
    		}else if(tranType.equals(PayConstant.TRAN_TYPE_B2CXY)){
    			payCoopBank.setB2cxyFeeCode(rs.getString("FEE_CODE"));
    			payCoopBank.setB2cxyFeeName(rs.getString("FEE_Name"));
    		}else if(tranType.equals(PayConstant.TRAN_TYPE_B2B)){
    			payCoopBank.setB2bFeeCode(rs.getString("FEE_CODE"));
    			payCoopBank.setB2bFeeName(rs.getString("FEE_Name"));
    		}else if(tranType.equals(PayConstant.TRAN_TYPE_YHK)){
    			payCoopBank.setYhkFeeCode(rs.getString("FEE_CODE"));
    			payCoopBank.setYhkFeeName(rs.getString("FEE_Name"));
    		}else if(tranType.equals(PayConstant.TRAN_TYPE_KJ_JJ)){
    			payCoopBank.setKjJJFeeCode(rs.getString("FEE_CODE"));
    			payCoopBank.setKjJJFeeName(rs.getString("FEE_Name"));
    		}else if(tranType.equals(PayConstant.TRAN_TYPE_KJ_DJ)){
    			payCoopBank.setKjDJFeeCode(rs.getString("FEE_CODE"));
    			payCoopBank.setKjDJFeeName(rs.getString("FEE_Name"));
    		}else if(tranType.equals(PayConstant.TRAN_TYPE_DS)){
    			payCoopBank.setDsFeeCode(rs.getString("FEE_CODE"));
    			payCoopBank.setDsFeeName(rs.getString("FEE_Name"));
    		}else if(tranType.equals(PayConstant.TRAN_TYPE_DF)){
    			payCoopBank.setDfFeeCode(rs.getString("FEE_CODE"));
    			payCoopBank.setDfFeeName(rs.getString("FEE_Name"));
    		}else if(tranType.equals(PayConstant.TRAN_TYPE_ZFB_SCAN)){
    			payCoopBank.setZfbFeeCode(rs.getString("FEE_CODE"));
    			payCoopBank.setZfbFeeName(rs.getString("FEE_Name"));
    		}else if(tranType.equals(PayConstant.TRAN_TYPE_WXWAP)){
    			payCoopBank.setWxwapFeeCode(rs.getString("FEE_CODE"));
    			payCoopBank.setWxwapFeeName(rs.getString("FEE_Name"));
    		}else if(tranType.equals(PayConstant.TRAN_TYPE_QQ_SCAN)){
    			payCoopBank.setQqFeeCode(rs.getString("FEE_CODE"));
    			payCoopBank.setQqFeeName(rs.getString("FEE_Name"));
    		}
    		
    		
    		/**
    		 * public static String ="17"; // 支付宝扫码费率
				public static String ="18"; // 微信WAP费率
    		 */
    	}
		return payCoopBank;
    }
    public String addPayCoopBank(PayCoopBank payCoopBank) throws Exception {
    	String result = payCoopBank.getAccountTime()==null?"":",to_date(?,'yyyy-mm-dd hh24:mi:ss')";
        String insertBankSql = "insert into PAY_COOP_BANK("+
            "BANK_CODE," + 
            "PAY_SYS_ID," + 
            "BANK_NAME," + 
            "BANK_REL_NO," + 
            "TRT_BANK_FLG," + 
            "STL_AC_NO," + 
            "CERT_NO," + 
            "REG_NO," + 
            "LEG_REP," + 
            "CUST_MGR," + 
            "TEL_NO," + 
            "BANK_EMAIL," + 
            "BANK_ADDRESS," + 
            "BIZ_RANGE," + 
            "REMARK," + 
            "CRE_OPER_ID," + 
            "LST_UPT_OPER_ID," + 
            "DEP_SUB_CODE," + 
            "PAY_SUB_CODE," + 
            "REC_SUB_CODE," + 
            "RECERR_SUB_CODE," + 
            "REFUND_ONLINE," + 
            "ACCOUNT_ONLINE," + 
            "PAY_QUICK_FLAG," + 
            "PAY_WEB_FLAG," + 
            "STL_TIME," + 
            "STL_TIME_TYPE," + 
            "PAY_SCAN_FLAG," + 
            "PAY_WITHDRAW_FLAG,QUICK_RESEND_SMS" +
            (payCoopBank.getAccountTime()==null?"":",ACCOUNT_TIME") +
            ",PAY_RECEIVE_FLAG)values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?" + result + ",?)";
        String insertSql = "insert into PAY_CUST_FEE("+
                "ID," + 
                "CUST_TYPE," + 
                "CUST_ID," + 
                "TRAN_TYPE," + 
                "FEE_CODE," + 
                "CREATE_TIME)values(?,?,?,?,?,to_date(?,'yyyy-mm-dd hh24:mi:ss'))";
        log.info(insertSql);
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = connection();
            con.setAutoCommit(false);
            ps = con.prepareStatement(insertBankSql);
            int n=1;
            ps.setString(n++,payCoopBank.bankCode);
            ps.setString(n++,payCoopBank.paySysId);
            ps.setString(n++,payCoopBank.bankName);
            ps.setString(n++,payCoopBank.bankRelNo);
            ps.setString(n++,payCoopBank.trtBankFlg);
            ps.setString(n++,payCoopBank.stlAcNo);
            ps.setString(n++,payCoopBank.certNo);
            ps.setString(n++,payCoopBank.regNo);
            ps.setString(n++,payCoopBank.legRep);
            ps.setString(n++,payCoopBank.custMgr);
            ps.setString(n++,payCoopBank.telNo);
            ps.setString(n++,payCoopBank.bankEmail);
            ps.setString(n++,payCoopBank.bankAddress);
            ps.setString(n++,payCoopBank.bizRange);
            ps.setString(n++,payCoopBank.remark);
            ps.setString(n++,payCoopBank.creOperId);
            ps.setString(n++,payCoopBank.lstUptOperId);
            ps.setString(n++,payCoopBank.depSubCode);
            ps.setString(n++,payCoopBank.paySubCode);
            ps.setString(n++,payCoopBank.recSubCode);
            ps.setString(n++,payCoopBank.recerrSubCode);
            ps.setString(n++,payCoopBank.refundOnline);
            ps.setString(n++,payCoopBank.accountOnline);
            ps.setString(n++,payCoopBank.payQuickFlag);
            ps.setString(n++,payCoopBank.payWebFlag);
            ps.setString(n++,payCoopBank.stlTime);
            ps.setString(n++,payCoopBank.stlTimeType);
            ps.setString(n++,payCoopBank.payScanFlag);
            ps.setString(n++,payCoopBank.payWithdrawFlag);
            ps.setString(n++,payCoopBank.quickResendSms);
            if(payCoopBank.accountTime!=null)ps.setString(n++,new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payCoopBank.accountTime));
            ps.setString(n++,payCoopBank.payReceiveFlag);
            ps.executeUpdate();
            ps.close();
            String [] paras = {
            		PayConstant.TRAN_TYPE_REFUND,
            		PayConstant.TRAN_TYPE_WITHDRAW,
            		PayConstant.TRAN_TYPE_TRANSFER,
            		PayConstant.TRAN_TYPE_B2CJJ,
            		PayConstant.TRAN_TYPE_B2CXY,
            		PayConstant.TRAN_TYPE_B2B,
            		PayConstant.TRAN_TYPE_YHK,
            		PayConstant.TRAN_TYPE_KJ_JJ,
            		PayConstant.TRAN_TYPE_KJ_DJ,
            		PayConstant.TRAN_TYPE_DS,
            		PayConstant.TRAN_TYPE_DF,
            		PayConstant.TRAN_TYPE_ZFB_SCAN,
            		PayConstant.TRAN_TYPE_WXWAP,
            		PayConstant.TRAN_TYPE_QQ_SCAN
        		};
            String [] values = {
        			payCoopBank.getTkFeeCode(),
            		payCoopBank.getTxFeeCode(),
            		payCoopBank.getZzFeeCode(),
            		payCoopBank.getB2cjjFeeCode(),
            		payCoopBank.getB2cxyFeeCode(),
            		payCoopBank.getB2bFeeCode(),
            		payCoopBank.getYhkFeeCode(),
            		payCoopBank.getKjJJFeeCode(),
            		payCoopBank.getKjDJFeeCode(),
            		payCoopBank.getDsFeeCode(),
            		payCoopBank.getDfFeeCode(),
            		payCoopBank.getZfbFeeCode(),
            		payCoopBank.getWxwapFeeCode(),
            		payCoopBank.getQqFeeCode()
        		};
            //退款
            ps = con.prepareStatement(insertSql);
            for(int i=0; i<paras.length;i++){
	            n=1;
	            ps.setString(n++,Tools.getUniqueIdentify());
	            ps.setString(n++,PayConstant.CUST_TYPE_PAY_CHANNEL);
	            ps.setString(n++,payCoopBank.getBankCode());
	            ps.setString(n++,paras[i]);
	            ps.setString(n++,values[i]);
	            ps.setString(n++,new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
	            ps.addBatch();
        	}
            ps.executeBatch();
            con.commit();
            return "";
        } catch (Exception e) {
        	con.rollback();
            e.printStackTrace();
            throw e;
        }finally {
        	con.setAutoCommit(true);
            close(null, ps, con);
        }
    }
    /**
     * 取得开启渠道
     * @return
     * @throws Exception
     */
    public List getList() throws Exception{
        String sql = "select * from PAY_COOP_BANK where bank_status='0'";
        String sqlFeeRate = "SELECT tmp.bank_code,fee.tran_type,rate.* FROM (select * from PAY_COOP_BANK where bank_status='0') tmp " +
        		"LEFT JOIN PAY_CUST_FEE fee ON tmp.bank_code = fee.CUST_ID LEFT JOIN PAY_FEE_RATE rate ON fee.FEE_CODE = rate.FEE_CODE";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<PayCoopBank> list = new ArrayList<PayCoopBank>();
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while(rs.next()){
            	PayCoopBank channel = getPayCoopBankValue(rs); 
                list.add(channel);
                PayCoopBankService.CHANNEL_MAP.put(channel.bankCode,channel);
            }
            rs.close();
            ps.close();
            //载入费率列表
            ps = con.prepareStatement(sqlFeeRate);
            rs = ps.executeQuery();
            while(rs.next()){
            	String channelId = rs.getString("bank_code");
            	PayFeeRate feeRate = PayFeeRateDAO.getPayFeeRateValue(rs);
            	PayCoopBankService.CHANNEL_MAP.get(channelId).feeRateList.add(feeRate);
            	PayCoopBankService.CHANNEL_MAP.get(channelId).feeRateMap.put(channelId+","+feeRate.tranType, feeRate);
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(rs, ps, con);
        }
    }
    public List getListAll() throws Exception{
      String sql = "select * from PAY_COOP_BANK";
      String sqlFeeRate = "SELECT tmp.bank_code,fee.tran_type,rate.* FROM PAY_COOP_BANK tmp " +
      		"LEFT JOIN PAY_CUST_FEE fee ON tmp.bank_code = fee.CUST_ID LEFT JOIN PAY_FEE_RATE rate ON fee.FEE_CODE = rate.FEE_CODE";
      log.info(sql);
      Connection con = null;
      PreparedStatement ps = null;
      ResultSet rs = null;
      List<PayCoopBank> list = new ArrayList<PayCoopBank>();
      try {
          con = connection();
          ps = con.prepareStatement(sql);
          rs = ps.executeQuery();
          while(rs.next()){
          	PayCoopBank channel = getPayCoopBankValue(rs); 
              list.add(channel);
              PayCoopBankService.CHANNEL_MAP_ALL.put(channel.bankCode,channel);
          }
          rs.close();
          ps.close();
          //载入费率列表
          ps = con.prepareStatement(sqlFeeRate);
          rs = ps.executeQuery();
          while(rs.next()){
          	String channelId = rs.getString("bank_code");
          	PayFeeRate feeRate = PayFeeRateDAO.getPayFeeRateValue(rs);
          	PayCoopBankService.CHANNEL_MAP_ALL.get(channelId).feeRateList.add(feeRate);
          	PayCoopBankService.CHANNEL_MAP_ALL.get(channelId).feeRateMap.put(channelId+","+feeRate.tranType, feeRate);
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
     * @param payCoopBank
     * @return
     */
    private String setPayCoopBankSql(PayCoopBank payCoopBank) {
        StringBuffer sql = new StringBuffer();
        
        if(payCoopBank.bankStatus != null && payCoopBank.bankStatus.length() !=0) {
            sql.append(" BANK_STATUS = ? and ");
        }
        if(payCoopBank.bankCode != null && payCoopBank.bankCode.length() !=0) {
            sql.append(" BANK_CODE = ? and ");
        }
        if(payCoopBank.bankName != null && payCoopBank.bankName.length() !=0) {
            sql.append(" BANK_NAME like ? and ");
        }
        if(payCoopBank.trtBankFlg != null && payCoopBank.trtBankFlg.length() !=0) {
            sql.append(" TRT_BANK_FLG = ? and ");
        }
        if(payCoopBank.refundOnline != null && payCoopBank.refundOnline.length() !=0) {
        	sql.append(" REFUND_ONLINE = ? and ");
        }
        if(payCoopBank.accountOnline != null && payCoopBank.accountOnline.length() !=0) {
        	sql.append(" ACCOUNT_ONLINE = ? and ");
        }
        if(payCoopBank.payQuickFlag != null && payCoopBank.payQuickFlag.length() !=0) {
        	sql.append(" PAY_QUICK_FLAG = ? and ");
        }
        if(payCoopBank.payWebFlag != null && payCoopBank.payWebFlag.length() !=0) {
        	sql.append(" PAY_WEB_FLAG = ? and ");
        }
        String tmp = sql.toString();
        if(tmp.length()>0)tmp=tmp.substring(0,tmp.lastIndexOf(" and "));
        return tmp;
    }
    /**
     * Set query parameter.
     * @param ps
     * @param payCoopBank
     * @param n
     * @return
     * @throws SQLException
     */
    private int setPayCoopBankParameter(PreparedStatement ps,PayCoopBank payCoopBank,int n)throws SQLException {
        if(payCoopBank.bankStatus != null && payCoopBank.bankStatus.length() !=0) {
            ps.setString(n++,payCoopBank.bankStatus);
        }
        if(payCoopBank.bankCode != null && payCoopBank.bankCode.length() !=0) {
            ps.setString(n++,payCoopBank.bankCode);
        }
        if(payCoopBank.bankName != null && payCoopBank.bankName.length() !=0) {
            ps.setString(n++,"%"+payCoopBank.bankName+"%");
        }
        if(payCoopBank.trtBankFlg != null && payCoopBank.trtBankFlg.length() !=0) {
            ps.setString(n++,payCoopBank.trtBankFlg);
        }
        if(payCoopBank.refundOnline != null && payCoopBank.refundOnline.length() !=0) {
        	ps.setString(n++,payCoopBank.refundOnline);
        }
        if(payCoopBank.accountOnline != null && payCoopBank.accountOnline.length() !=0) {
        	ps.setString(n++,payCoopBank.accountOnline);
        }
        if(payCoopBank.payQuickFlag != null && payCoopBank.payQuickFlag.length() !=0) {
        	ps.setString(n++,payCoopBank.payQuickFlag);
        }
        if(payCoopBank.payWebFlag != null && payCoopBank.payWebFlag.length() !=0) {
        	ps.setString(n++,payCoopBank.payWebFlag);
        }
        return n;
    }
    /**
     * Get records count.
     * @param payCoopBank
     * @return
     */
    public int getPayCoopBankCount(PayCoopBank payCoopBank) {
        String sqlCon = setPayCoopBankSql(payCoopBank);
        String sql = "select count(rownum) recordCount from PAY_COOP_BANK " +(sqlCon.length()==0?"":" where "+ sqlCon);
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            int n = 1;
            setPayCoopBankParameter(ps,payCoopBank,n);
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
     * @param payCoopBank
     * @param page
     * @param rows
     * @param sort
     * @param order
     * @return
     * @throws Exception
     */
    public List getPayCoopBankList(PayCoopBank payCoopBank,int page,int rows,String sort,String order) throws Exception{
        sort = Tools.paraNameToDBColumnName(sort);
        String sqlCon = setPayCoopBankSql(payCoopBank);
        String sortOrder = sort == null || sort.length()==0?" order by CRE_TIME desc":(" order by " + sort +("desc".equals(order)?" desc ":" asc "));
        String sql = "select * from (" +
                "  select rownum rowno,tmp1.* from (" +
                "   select tmp.* from PAY_COOP_BANK tmp "
                +(sqlCon.length()==0?"":" where "+ sqlCon) +sortOrder+
                "  ) tmp1 "+
                ")  where rowno > "+((page-1)*rows)+ " and rowno<= " + (page*rows) + sortOrder;
        
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        PreparedStatement ratePs = null;
        ResultSet rateRs = null;
        List list = new ArrayList();
        try {
            con = connection();
            ps = con.prepareStatement(sql.toString());
            int n = 1;
            setPayCoopBankParameter(ps,payCoopBank,n);
            rs = ps.executeQuery();
            while(rs.next()){
                list.add(getPayCoopBankValue(rs));
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
     * detail PayCoopBank
     * @param bankCode
     * @return PayCoopBank
     * @throws Exception
     */
    public PayCoopBank detailPayCoopBank(String bankCode) throws Exception {
        String feeRateSql = "select fee.tran_type,fee.fee_code,rate.fee_name from PAY_COOP_BANK bank "
        		+ "LEFT JOIN PAY_CUST_FEE fee ON bank.bank_code = fee.CUST_ID "
        		+ "LEFT JOIN PAY_FEE_RATE rate ON fee.FEE_CODE = rate.FEE_CODE "
        		+ "where bank.BANK_CODE=?";
        
        String sql = "select * from PAY_COOP_BANK bank WHERE bank.BANK_CODE= ?";
        
        log.info(sql);
        log.info(feeRateSql);
        Connection con = null;
        PreparedStatement ps = null;
        PreparedStatement ratePs = null;
        ResultSet rs = null;
        ResultSet rateRs = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            ps.setString(1,bankCode);
            rs = ps.executeQuery();
            
            ratePs = con.prepareStatement(feeRateSql);
            ratePs.setString(1,bankCode);
            rateRs = ratePs.executeQuery();
            if(rs.next()){
            	PayCoopBank cbank = getPayCoopBankValue(rs);
            	return setPayCoopBankRateFee(cbank,rateRs);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(rs, ps, con);
            close(rateRs, ratePs, null);
        }
        return null;
    }
    /**
     * update PayCoopBank
     * @param payCoopBank
     * @throws Exception
     */
    public void updatePayCoopBank(PayCoopBank payCoopBank) throws Exception {
        String sqlTmp = "";
        if(payCoopBank.paySysId != null)sqlTmp = sqlTmp + " PAY_SYS_ID=?,";
        if(payCoopBank.bankName != null)sqlTmp = sqlTmp + " BANK_NAME=?,";
        if(payCoopBank.bankRelNo != null)sqlTmp = sqlTmp + " BANK_REL_NO=?,";
        if(payCoopBank.bankStatus != null)sqlTmp = sqlTmp + " BANK_STATUS=?,";
        if(payCoopBank.trtBankFlg != null)sqlTmp = sqlTmp + " TRT_BANK_FLG=?,";
        if(payCoopBank.stlAcNo != null)sqlTmp = sqlTmp + " STL_AC_NO=?,";
        if(payCoopBank.certNo != null)sqlTmp = sqlTmp + " CERT_NO=?,";
        if(payCoopBank.regNo != null)sqlTmp = sqlTmp + " REG_NO=?,";
        if(payCoopBank.legRep != null)sqlTmp = sqlTmp + " LEG_REP=?,";
        if(payCoopBank.custMgr != null)sqlTmp = sqlTmp + " CUST_MGR=?,";
        if(payCoopBank.telNo != null)sqlTmp = sqlTmp + " TEL_NO=?,";
        if(payCoopBank.bankEmail != null)sqlTmp = sqlTmp + " BANK_EMAIL=?,";
        if(payCoopBank.bankAddress != null)sqlTmp = sqlTmp + " BANK_ADDRESS=?,";
        if(payCoopBank.bizRange != null)sqlTmp = sqlTmp + " BIZ_RANGE=?,";
        if(payCoopBank.remark != null)sqlTmp = sqlTmp + " REMARK=?,";
        if(payCoopBank.creOperId != null)sqlTmp = sqlTmp + " CRE_OPER_ID=?,";
        if(payCoopBank.lstUptOperId != null)sqlTmp = sqlTmp + " LST_UPT_OPER_ID=?,";
        if(payCoopBank.accountTime != null){sqlTmp = sqlTmp + " ACCOUNT_TIME=to_date(?,'yyyy-mm-dd hh24:mi:ss'),";}
        	else sqlTmp = sqlTmp + " ACCOUNT_TIME=null,";
        if(payCoopBank.lstUptTime != null)sqlTmp = sqlTmp + " LST_UPT_TIME=to_date(?,'yyyy-mm-dd hh24:mi:ss'),";
        if(payCoopBank.depSubCode != null)sqlTmp = sqlTmp + " DEP_SUB_CODE=?,";
        if(payCoopBank.paySubCode != null)sqlTmp = sqlTmp + " PAY_SUB_CODE=?,";
        if(payCoopBank.recSubCode != null)sqlTmp = sqlTmp + " REC_SUB_CODE=?,";
        if(payCoopBank.recerrSubCode != null)sqlTmp = sqlTmp + " RECERR_SUB_CODE=?,";
        if(payCoopBank.accountOnline != null)sqlTmp = sqlTmp + " ACCOUNT_ONLINE=?,";
        if(payCoopBank.refundOnline != null)sqlTmp = sqlTmp + " REFUND_ONLINE=?,";
        if(payCoopBank.quickResendSms != null)sqlTmp = sqlTmp + " QUICK_RESEND_SMS=?,";
        if(payCoopBank.payQuickFlag != null)sqlTmp = sqlTmp + " PAY_QUICK_FLAG=?,";
        if(payCoopBank.payWebFlag != null)sqlTmp = sqlTmp + " PAY_WEB_FLAG=?,";
        if(payCoopBank.payWithdrawFlag != null)sqlTmp = sqlTmp + " PAY_WITHDRAW_FLAG=?,";
        if(payCoopBank.payScanFlag != null)sqlTmp = sqlTmp + " PAY_SCAN_FLAG=?,";
        if(payCoopBank.payReceiveFlag != null)sqlTmp = sqlTmp + " PAY_RECEIVE_FLAG=?,";
        if(payCoopBank.stlTime != null)sqlTmp = sqlTmp + " STL_TIME=?,";
        if(payCoopBank.stlTimeType != null)sqlTmp = sqlTmp + " STL_TIME_TYPE=?,";
        if(sqlTmp.length()==0)return;
        String sql = "update PAY_COOP_BANK "+        
              "set " + 
              sqlTmp.substring(0,sqlTmp.length()-1) + 
            " where BANK_CODE=?"; 
        //修改费率中间表语句 先删除 重新添加
        String deleteFeeSql = "delete from PAY_CUST_FEE where CUST_ID = ?";
        String insertSql = "insert into PAY_CUST_FEE("+
                "ID," + 
                "CUST_TYPE," + 
                "CUST_ID," + 
                "TRAN_TYPE," + 
                "FEE_CODE," + 
                "CREATE_TIME)values(?,?,?,?,?,to_date(?,'yyyy-mm-dd hh24:mi:ss'))";
        log.info(insertSql);
//        log.info(insertB2cxyFeeSql);
//        log.info(insertB2bFeeSql);
//        log.info(insertYhkFeeSql);
//        log.info(insertKjJJFeeSql);
//        log.info(insertKjDJFeeSql);
//        log.info(insertDsFeeSql);
//        log.info(insertDfFeeSql);
        log.info(sql);
        log.info(deleteFeeSql);
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = connection();
            con.setAutoCommit(false);
            ps = con.prepareStatement(sql);
            int n=1;
            if(payCoopBank.paySysId != null)ps.setString(n++,payCoopBank.paySysId);
            if(payCoopBank.bankName != null)ps.setString(n++,payCoopBank.bankName);
            if(payCoopBank.bankRelNo != null)ps.setString(n++,payCoopBank.bankRelNo);
            if(payCoopBank.bankStatus != null)ps.setString(n++,payCoopBank.bankStatus);
            if(payCoopBank.trtBankFlg != null)ps.setString(n++,payCoopBank.trtBankFlg);
            if(payCoopBank.stlAcNo != null)ps.setString(n++,payCoopBank.stlAcNo);
            if(payCoopBank.certNo != null)ps.setString(n++,payCoopBank.certNo);
            if(payCoopBank.regNo != null)ps.setString(n++,payCoopBank.regNo);
            if(payCoopBank.legRep != null)ps.setString(n++,payCoopBank.legRep);
            if(payCoopBank.custMgr != null)ps.setString(n++,payCoopBank.custMgr);
            if(payCoopBank.telNo != null)ps.setString(n++,payCoopBank.telNo);
            if(payCoopBank.bankEmail != null)ps.setString(n++,payCoopBank.bankEmail);
            if(payCoopBank.bankAddress != null)ps.setString(n++,payCoopBank.bankAddress);
            if(payCoopBank.bizRange != null)ps.setString(n++,payCoopBank.bizRange);
            if(payCoopBank.remark != null)ps.setString(n++,payCoopBank.remark);
            if(payCoopBank.creOperId != null)ps.setString(n++,payCoopBank.creOperId);
            if(payCoopBank.lstUptOperId != null)ps.setString(n++,payCoopBank.lstUptOperId);
            if(payCoopBank.accountTime != null){ps.setString(n++,new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payCoopBank.accountTime));}
            if(payCoopBank.lstUptTime != null)ps.setString(n++,new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payCoopBank.lstUptTime));
            if(payCoopBank.depSubCode != null)ps.setString(n++,payCoopBank.depSubCode);
            if(payCoopBank.paySubCode != null)ps.setString(n++,payCoopBank.paySubCode);
            if(payCoopBank.recSubCode != null)ps.setString(n++,payCoopBank.recSubCode);
            if(payCoopBank.recerrSubCode != null)ps.setString(n++,payCoopBank.recerrSubCode);
            if(payCoopBank.accountOnline != null)ps.setString(n++,payCoopBank.accountOnline);
            if(payCoopBank.refundOnline != null)ps.setString(n++,payCoopBank.refundOnline);
            if(payCoopBank.quickResendSms != null)ps.setString(n++,payCoopBank.quickResendSms);
            if(payCoopBank.payQuickFlag != null)ps.setString(n++,payCoopBank.payQuickFlag);
            if(payCoopBank.payWebFlag != null)ps.setString(n++,payCoopBank.payWebFlag);
            if(payCoopBank.payWithdrawFlag != null)ps.setString(n++,payCoopBank.payWithdrawFlag);
            if(payCoopBank.payScanFlag != null)ps.setString(n++,payCoopBank.payScanFlag);
            if(payCoopBank.payReceiveFlag != null)ps.setString(n++,payCoopBank.payReceiveFlag);
            if(payCoopBank.stlTime != null)ps.setString(n++,payCoopBank.stlTime);
            if(payCoopBank.stlTimeType != null)ps.setString(n++,payCoopBank.stlTimeType);
            ps.setString(n++,payCoopBank.bankCode);
            ps.executeUpdate();
            ps.close();
            //删除费率
            ps = con.prepareStatement(deleteFeeSql);
            n=1;
            ps.setString(n++, payCoopBank.getBankCode());
            ps.executeUpdate();
            ps.close();
            String [] paras = {
	            		PayConstant.TRAN_TYPE_REFUND,
	            		PayConstant.TRAN_TYPE_WITHDRAW,
	            		PayConstant.TRAN_TYPE_TRANSFER,
	            		PayConstant.TRAN_TYPE_B2CJJ,
	            		PayConstant.TRAN_TYPE_B2CXY,
	            		PayConstant.TRAN_TYPE_B2B,
	            		PayConstant.TRAN_TYPE_YHK,
	            		PayConstant.TRAN_TYPE_KJ_JJ,
	            		PayConstant.TRAN_TYPE_KJ_DJ,
	            		PayConstant.TRAN_TYPE_DS,
	            		PayConstant.TRAN_TYPE_DF,
	            		PayConstant.TRAN_TYPE_ZFB_SCAN,
	            		PayConstant.TRAN_TYPE_WXWAP,
	            		PayConstant.TRAN_TYPE_QQ_SCAN
            		};
            String [] values = {
            			payCoopBank.getTkFeeCode(),
	            		payCoopBank.getTxFeeCode(),
	            		payCoopBank.getZzFeeCode(),
	            		payCoopBank.getB2cjjFeeCode(),
	            		payCoopBank.getB2cxyFeeCode(),
	            		payCoopBank.getB2bFeeCode(),
	            		payCoopBank.getYhkFeeCode(),
	            		payCoopBank.getKjJJFeeCode(),
	            		payCoopBank.getKjDJFeeCode(),
	            		payCoopBank.getDsFeeCode(),
	            		payCoopBank.getDfFeeCode(),
	            		payCoopBank.getZfbFeeCode(),
	            		payCoopBank.getWxwapFeeCode(),
	            		payCoopBank.getQqFeeCode()
            		};
            
            //提现
            ps = con.prepareStatement(insertSql);
            for(int i=0; i<paras.length;i++){
	            n=1;
	            ps.setString(n++,Tools.getUniqueIdentify());
	            ps.setString(n++,PayConstant.CUST_TYPE_PAY_CHANNEL);
	            ps.setString(n++,payCoopBank.getBankCode());
	            ps.setString(n++,paras[i]);
	            ps.setString(n++,values[i]);
	            ps.setString(n++,new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
	            ps.addBatch();
        	}
            ps.executeBatch();
            con.commit();
        } catch (Exception e) {
        	con.rollback();
            e.printStackTrace();
            throw e;
        }finally {
        	con.setAutoCommit(true);
            close(null, ps, con);
        }
    }
	public void updatePayCoopBankStatus(String bankCode,String columName,String operation) throws Exception{
		String sql = "UPDATE PAY_COOP_BANK SET "+columName+" = '"+operation+"' WHERE BANK_CODE = '"+bankCode+"'";
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
	public List getBankList() throws Exception{
        String sql = "select * from PAY_BANK order by ID";
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
            	bank.id=rs.getString("ID");
            	bank.bankCode = rs.getString("BANK_CODE");
            	bank.bankName = rs.getString("BANK_NAME");
            	list.add(bank);
            	PayCoopBankService.BANK_CODE_NAME_MAP.put(bank.bankCode, bank.bankName);
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(rs, ps, con);
        }
    }
	public List getSuppertedBank() throws Exception{        
        String sql = "select b.*,a.SUPPORTED_USER_TYPE from (select cb.bank_code,cb.SUPPORTED_USER_TYPE from PAY_CHANNEL_BANK_RELATION cb,PAY_COOP_BANK c "+
        		"where cb.CHANNEL_CODE=c.bank_code and c.bank_status='0'  group by cb.bank_code,SUPPORTED_USER_TYPE) a left join PAY_BANK b on a.bank_code=b.bank_code  order by id";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List list = new ArrayList();
        Map map = new HashMap();
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while(rs.next()){
            	PayBank bank = new PayBank();
            	bank.id=rs.getString("ID");
            	bank.bankCode = rs.getString("BANK_CODE");
            	bank.bankName = rs.getString("BANK_NAME");
            	String tmp = rs.getString("SUPPORTED_USER_TYPE");
            	if(tmp != null && tmp.indexOf("0,")!=-1)bank.jiejiCard="0";
            	if(tmp != null && tmp.indexOf("1,")!=-1)bank.daijiCard="1";
            	if(tmp != null && tmp.indexOf("4,")!=-1)bank.compCard="4";
            	PayBank preBank = (PayBank)map.get(bank.bankCode);
            	if(preBank==null){
            		map.put(bank.bankCode,bank);
            		list.add(bank);
            	} else {
            		if(tmp != null && tmp.indexOf("0,")!=-1)preBank.jiejiCard="0";
                	if(tmp != null && tmp.indexOf("1,")!=-1)preBank.daijiCard="1";
                	if(tmp != null && tmp.indexOf("4,")!=-1)preBank.compCard="4";
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
	public List getSuppertedBankRealTime() throws Exception{        
        String sql = "select b.*,a.SUPPORTED_USER_TYPE from (select cb.bank_code,cb.SUPPORTED_USER_TYPE from PAY_CHANNEL_BANK_RELATION cb,PAY_COOP_BANK c "+
        		"where cb.CHANNEL_CODE=c.bank_code and c.bank_status='0' and c.STL_TIME_TYPE='D' and c.STL_TIME='0'  group by cb.bank_code,SUPPORTED_USER_TYPE) a " +
        		"left join PAY_BANK b on a.bank_code=b.bank_code  order by id";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List list = new ArrayList();
        Map map = new HashMap();
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while(rs.next()){
            	PayBank bank = new PayBank();
            	bank.id=rs.getString("ID");
            	bank.bankCode = rs.getString("BANK_CODE");
            	bank.bankName = rs.getString("BANK_NAME");
            	String tmp = rs.getString("SUPPORTED_USER_TYPE");
            	if(tmp != null && tmp.indexOf("0,")!=-1)bank.jiejiCard="0";
            	if(tmp != null && tmp.indexOf("1,")!=-1)bank.daijiCard="1";
            	if(tmp != null && tmp.indexOf("4,")!=-1)bank.compCard="4";
            	PayBank preBank = (PayBank)map.get(bank.bankCode);
            	if(preBank==null){
            		map.put(bank.bankCode,bank);
            		list.add(bank);
            	} else {
            		if(tmp != null && tmp.indexOf("0,")!=-1)preBank.jiejiCard="0";
                	if(tmp != null && tmp.indexOf("1,")!=-1)preBank.daijiCard="1";
                	if(tmp != null && tmp.indexOf("4,")!=-1)preBank.compCard="4";
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
	public List getSuppertedQuickBank() throws Exception{        
		String sql = "select d.* from (" +
        		"select a.BANK_CODE from PAY_CHANNEL_BANK_RELATION a ,PAY_COOP_BANK b " +
        			"where (a.QUICK_USER_TYPE='0,' or a.QUICK_USER_TYPE='0,1,') and a.channel_code=b.bank_code and b.BANK_STATUS='0' group by a.BANK_CODE)c " +
        		"left join PAY_BANK d on c.BANK_CODE=d.BANK_CODE";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List <PayBank>list = new ArrayList<PayBank>();
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while(rs.next())list.add(PayBankDAO.getPayBankValue(rs));
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(rs, ps, con);
        }
    }
	public List<PayBank> getSuppertedQuickBankRealTime() throws Exception{        
        String sql = "select d.* from (" +
        		"select a.BANK_CODE from PAY_CHANNEL_BANK_RELATION a ,PAY_COOP_BANK b " +
        			"where (a.QUICK_USER_TYPE='0,' or a.QUICK_USER_TYPE='0,1,') and a.channel_code=b.bank_code and b.BANK_STATUS='0' and b.STL_TIME_TYPE='D' and STL_TIME='0' group by a.BANK_CODE)c " +
        		"left join PAY_BANK d on c.BANK_CODE=d.BANK_CODE";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List <PayBank>list = new ArrayList<PayBank>();
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while(rs.next())list.add(PayBankDAO.getPayBankValue(rs));
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(rs, ps, con);
        }
    }
	/**
	 * 获取支持的银行列表
	 * @return
	 */
	public List<PayCoopBank> getPayBankList() {
		String sql = "select * from(select r.*,c.BANK_STATUS channel_status from PAY_CHANNEL_BANK_RELATION r left join PAY_COOP_BANK c on r.channel_code=c.bank_code) where channel_status='0'";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<PayCoopBank> list = new ArrayList();
        Map <String,PayCoopBank>map = new HashMap<String, PayCoopBank>();
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            Map<String, String> bankNameMap = (Map<String, String>) PayCardBinService.BANK_CODE_NAME_MAP;
            while(rs.next()){
            	PayCoopBank bank = new PayCoopBank();
            	bank.bankCode = rs.getString("BANK_CODE");
            	bank.supportedUserType = rs.getString("SUPPORTED_USER_TYPE");
            	bank.quickUserType = rs.getString("QUICK_USER_TYPE");
            	bank.withdrawUserType = rs.getString("WITHDRAW_USER_TYPE");
            	bank.bankName = bankNameMap.get(bank.getBankCode());
            	PayCoopBank tmp = map.get(bank.bankCode);
            	if(tmp==null){
            		map.put(bank.bankCode, bank);
            		list.add(bank);
            	} else {
            		if(bank.supportedUserType!=null&&bank.supportedUserType.length()!=0){
            			tmp.supportedUserType += bank.supportedUserType;
            		}
            		if(bank.quickUserType!=null&&bank.quickUserType.length()!=0){
            			tmp.quickUserType += bank.quickUserType;
            		}
            		if(bank.withdrawUserType!=null&&bank.withdrawUserType.length()!=0){
            			tmp.withdrawUserType += bank.withdrawUserType;
            		}
            	}
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(rs, ps, con);
        }
        return null;
	}
	public long selectByBankCode(String bankCode) throws Exception {
		String sql = "select count(BANK_CODE) rCount from PAY_COOP_BANK where BANK_CODE = ?";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            ps.setString(1,bankCode);
            rs = ps.executeQuery();
            if(rs.next())return rs.getLong("rCount");
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(rs, ps, con);
        }
        return 0;
    }
	public PayCoopBank getChannelById(String bankCode) throws Exception {
//		String sql = "select * from PAY_COOP_BANK where BANK_CODE = ? and bank_status='0'";
		String sql = "select * from PAY_COOP_BANK where BANK_CODE = ?";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            ps.setString(1,bankCode);
            rs = ps.executeQuery();
            if(rs.next())return getPayCoopBankValue(rs);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(rs, ps, con);
        }
        return null;
    }

	public Map<String, String> getMerchantChannelRelation() throws Exception {
		String sql = "select * from PAY_MERCHANT_CHANNEL_RELATION";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Map map = new HashMap<String, String>();
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while(rs.next()){
            	String merno = rs.getString("MERNO");
            	String channelId = rs.getString("CHANNEL_ID");
            	String tranType = rs.getString("TRAN_TYPE");
            	map.put(merno+","+tranType, channelId);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(rs, ps, con);
        }
        return map;
	}
}