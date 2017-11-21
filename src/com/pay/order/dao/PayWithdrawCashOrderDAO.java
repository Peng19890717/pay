package com.pay.order.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import util.Tools;

import com.PayConstant;
import com.jweb.dao.BaseDAO;
/**
 * Table PAY_WITHDRAW_CASH_ORDER DAO. 
 * @author Administrator
 *
 */
public class PayWithdrawCashOrderDAO  extends BaseDAO {
    private static final Log log = LogFactory.getLog(PayWithdrawCashOrderDAO.class);
    public static synchronized PayWithdrawCashOrder getPayWithdrawCashOrderValue(ResultSet rs)throws SQLException {
        PayWithdrawCashOrder payWithdrawCashOrder = new PayWithdrawCashOrder();
        payWithdrawCashOrder.casordno = rs.getString("CASORDNO");
        payWithdrawCashOrder.actTime = rs.getTimestamp("ACT_TIME");
        payWithdrawCashOrder.sucTime = rs.getTimestamp("SUC_TIME");
        payWithdrawCashOrder.ordstatus = rs.getString("ORDSTATUS");
        payWithdrawCashOrder.txccy = rs.getString("TXCCY");
        payWithdrawCashOrder.txamt = rs.getLong("TXAMT");
        payWithdrawCashOrder.fee = rs.getLong("FEE");
        payWithdrawCashOrder.netrecAmt = rs.getLong("NETREC_AMT");
        payWithdrawCashOrder.withdrawWay = rs.getString("WITHDRAW_WAY");
        payWithdrawCashOrder.bankCode = rs.getString("BANK_CODE");
        payWithdrawCashOrder.bankName = rs.getString("BANK_NAME");
        payWithdrawCashOrder.bankstlDate = rs.getTimestamp("BANKSTL_DATE");
        payWithdrawCashOrder.bankjrnno = rs.getString("BANKJRNNO");
        payWithdrawCashOrder.bankpayacno = rs.getString("BANKPAYACNO");
        payWithdrawCashOrder.bankpayusernm = rs.getString("BANKPAYUSERNM");
        payWithdrawCashOrder.bankerror = rs.getString("BANKERROR");
        payWithdrawCashOrder.backerror = rs.getString("BACKERROR");
        payWithdrawCashOrder.notifyurl = rs.getString("NOTIFYURL");
        payWithdrawCashOrder.custId = rs.getString("CUST_ID");
        payWithdrawCashOrder.payret = rs.getString("PAYRET");
        payWithdrawCashOrder.bnkdat = rs.getTimestamp("BNKDAT");
        payWithdrawCashOrder.bnkjnl = rs.getString("BNKJNL");
        payWithdrawCashOrder.tracenumber = rs.getString("TRACENUMBER");
        payWithdrawCashOrder.tracetime = rs.getTimestamp("TRACETIME");
        payWithdrawCashOrder.updateTime = rs.getTimestamp("UPDATE_TIME");
        payWithdrawCashOrder.updateUser = rs.getString("UPDATE_USER");
        payWithdrawCashOrder.remark = rs.getString("REMARK");
        payWithdrawCashOrder.filed1 = rs.getString("FILED1");
        payWithdrawCashOrder.custType = rs.getString("CUST_TYPE");
        payWithdrawCashOrder.withdrawType = rs.getString("WITHDRAW_TYPE");
        payWithdrawCashOrder.withdrawChannel = rs.getString("WITHDRAW_CHANNEL");
        return payWithdrawCashOrder;
    }
    public int addPayWithdrawCashOrder(PayWithdrawCashOrder payWithdrawCashOrder) throws Exception {
        String sql = "insert into PAY_WITHDRAW_CASH_ORDER("+
            "CASORDNO," + 
            "ACT_TIME," + 
            "SUC_TIME," + 
            "ORDSTATUS," + 
            "TXCCY," + 
            "TXAMT," + 
            "FEE," + 
            "NETREC_AMT," + 
            "WITHDRAW_WAY," + 
            "BANK_CODE," + 
            "BANK_NAME," + 
            "BANKSTL_DATE," + 
            "BANKJRNNO," + 
            "BANKPAYACNO," + 
            "BANKPAYUSERNM," + 
            "BANKERROR," + 
            "BACKERROR," + 
            "NOTIFYURL," + 
            "CUST_ID," + 
            "PAYRET," + 
            "BNKDAT," + 
            "BNKJNL," + 
            "TRACENUMBER," + 
            "TRACETIME," + 
            "UPDATE_TIME," + 
            "UPDATE_USER," + 
            "REMARK," + 
            "FILED1," + 
            "FILED2)values(?,to_date(?,'yyyy-mm-dd hh24:mi:ss'),to_date(?,'yyyy-mm-dd hh24:mi:ss'),?,?,?,?,?,?,?,?,to_date(?,'yyyy-mm-dd hh24:mi:ss'),?,?,?,?,?,?,?,?,to_date(?,'yyyy-mm-dd hh24:mi:ss'),?,?,to_date(?,'yyyy-mm-dd hh24:mi:ss'),to_date(?,'yyyy-mm-dd hh24:mi:ss'),?,?,?,?)";
        log.info(sql);
        log.info(payWithdrawCashOrder.toString().replaceAll("\n",";"));
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            int n=1;
            ps.setString(n++,payWithdrawCashOrder.casordno);
            ps.setString(n++,new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payWithdrawCashOrder.actTime));
            ps.setString(n++,new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payWithdrawCashOrder.sucTime));
            ps.setString(n++,payWithdrawCashOrder.ordstatus);
            ps.setString(n++,payWithdrawCashOrder.txccy);
            ps.setLong(n++,payWithdrawCashOrder.txamt);
            ps.setLong(n++,payWithdrawCashOrder.fee);
            ps.setLong(n++,payWithdrawCashOrder.netrecAmt);
            ps.setString(n++,payWithdrawCashOrder.withdrawWay);
            ps.setString(n++,payWithdrawCashOrder.bankCode);
            ps.setString(n++,payWithdrawCashOrder.bankName);
            ps.setString(n++,new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payWithdrawCashOrder.bankstlDate));
            ps.setString(n++,payWithdrawCashOrder.bankjrnno);
            ps.setString(n++,payWithdrawCashOrder.bankpayacno);
            ps.setString(n++,payWithdrawCashOrder.bankpayusernm);
            ps.setString(n++,payWithdrawCashOrder.bankerror);
            ps.setString(n++,payWithdrawCashOrder.backerror);
            ps.setString(n++,payWithdrawCashOrder.notifyurl);
            ps.setString(n++,payWithdrawCashOrder.custId);
            ps.setString(n++,payWithdrawCashOrder.payret);
            ps.setString(n++,new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payWithdrawCashOrder.bnkdat));
            ps.setString(n++,payWithdrawCashOrder.bnkjnl);
            ps.setString(n++,payWithdrawCashOrder.tracenumber);
            ps.setString(n++,new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payWithdrawCashOrder.tracetime));
            ps.setString(n++,new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payWithdrawCashOrder.updateTime));
            ps.setString(n++,payWithdrawCashOrder.updateUser);
            ps.setString(n++,payWithdrawCashOrder.remark);
            ps.setString(n++,payWithdrawCashOrder.filed1);
            ps.setString(n++,payWithdrawCashOrder.filed2);
            return ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(null, ps, con);
        }
    }
    
    public int insertPayWithdrawCashOrder(PayWithdrawCashOrder payWithdrawCashOrder) throws Exception {
    	//添加提现记录
        String sql = "insert into PAY_WITHDRAW_CASH_ORDER("+
            "CASORDNO," + 
            "ACT_TIME," + 
//            "SUC_TIME," + 
            "ORDSTATUS," + 
            "TXCCY," + 
            "TXAMT," + 
            "FEE," + 
            "NETREC_AMT," + 
            "WITHDRAW_WAY," + 
            "BANK_CODE," + 
            "BANK_NAME," + 
            "BANKSTL_DATE," + 
            "BANKJRNNO," + 
            "BANKPAYACNO," + 
            "BANKPAYUSERNM," + 
            "BANKERROR," + 
            "BACKERROR," + 
            "NOTIFYURL," + 
            "CUST_ID," + 
            "PAYRET," + 
            "BNKDAT," + 
            "BNKJNL," + 
            "TRACENUMBER," + 
            "TRACETIME," + 
            "UPDATE_TIME," + 
            "UPDATE_USER," + 
            "REMARK," + 
            "WITHDRAW_TYPE," +
            "WITHDRAW_CHANNEL," +
            "CUST_TYPE,FILED1)values(?,to_date(?,'yyyy-mm-dd hh24:mi:ss'),?,?,?,?,?,?,?" +
            ",?,to_date(?,'yyyy-mm-dd hh24:mi:ss'),?,?,?,?,?,?,?,?,to_date(?,'yyyy-mm-dd hh24:mi:ss'),?," +
            "?,to_date(?,'yyyy-mm-dd hh24:mi:ss'),to_date(?,'yyyy-mm-dd hh24:mi:ss'),?,?,?,?,?,?)";
        //更新提现账户余额
        String sql1="update PAY_ACC_PROFILE set CASH_AC_BAL=CASH_AC_BAL-?,CONS_AC_BAL=CONS_AC_BAL-?" +
        		" where CASH_AC_BAL-?>=0 and CONS_AC_BAL-?>=0 and AC_TYPE=? and PAY_AC_NO=?";
//        //更新系统账户余额(添加手续费)
//        String sql2="update PAY_ACC_PROFILE set CASH_AC_BAL=CASH_AC_BAL+?,CONS_AC_BAL=CONS_AC_BAL+?" +
//        		" where AC_TYPE=? and PAY_AC_NO=?";
        log.info(sql);
        log.info(sql1);
        log.info(payWithdrawCashOrder.toString().replaceAll("\n",";"));
//        log.info(sql2);
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = connection();
//            con.setAutoCommit(false);
            ps = con.prepareStatement(sql);
            int n=1;
            ps.setString(n++,payWithdrawCashOrder.casordno);
            ps.setString(n++,new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payWithdrawCashOrder.actTime == null ? new Date() : payWithdrawCashOrder.actTime));
//            ps.setString(n++,new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payWithdrawCashOrder.sucTime == null ? new Date() : payWithdrawCashOrder.sucTime));
            ps.setString(n++,payWithdrawCashOrder.ordstatus);
            ps.setString(n++,payWithdrawCashOrder.txccy);
            ps.setLong(n++,payWithdrawCashOrder.txamt);
            ps.setLong(n++,payWithdrawCashOrder.fee);
            ps.setLong(n++,payWithdrawCashOrder.netrecAmt);
            ps.setString(n++,payWithdrawCashOrder.withdrawWay);
            ps.setString(n++,payWithdrawCashOrder.bankCode);
            ps.setString(n++,payWithdrawCashOrder.bankName);
            ps.setString(n++,new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payWithdrawCashOrder.bankstlDate == null ? new Date() : payWithdrawCashOrder.bankstlDate));
            ps.setString(n++,payWithdrawCashOrder.bankjrnno);
            ps.setString(n++,payWithdrawCashOrder.bankpayacno);
            ps.setString(n++,payWithdrawCashOrder.bankpayusernm);
            ps.setString(n++,payWithdrawCashOrder.bankerror);
            ps.setString(n++,payWithdrawCashOrder.backerror);
            ps.setString(n++,payWithdrawCashOrder.notifyurl);
            ps.setString(n++,payWithdrawCashOrder.custId);
            ps.setString(n++,payWithdrawCashOrder.payret);
            ps.setString(n++,new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payWithdrawCashOrder.bnkdat == null ? new Date() : payWithdrawCashOrder.bnkdat));
            ps.setString(n++,payWithdrawCashOrder.bnkjnl);
            ps.setString(n++,payWithdrawCashOrder.tracenumber);
            ps.setString(n++,new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payWithdrawCashOrder.tracetime == null ? new Date() : payWithdrawCashOrder.tracetime));
            ps.setString(n++,new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payWithdrawCashOrder.updateTime == null ? new Date() : payWithdrawCashOrder.updateTime));
            ps.setString(n++,payWithdrawCashOrder.updateUser);
            ps.setString(n++,payWithdrawCashOrder.remark);
            ps.setString(n++,payWithdrawCashOrder.withdrawType);
            ps.setString(n++,payWithdrawCashOrder.withdrawChannel);
            ps.setString(n++,payWithdrawCashOrder.custType);
            ps.setString(n++,payWithdrawCashOrder.filed1);
            ps.executeUpdate();
            ps.close();
            //订单状态:00: 未处理   01:提现处理中  02:提现成功  03:提现失败  04:已退回支付账号  05:重新提现  99:超时 默认00
            if("03".equals(payWithdrawCashOrder.ordstatus)){
//            	con.commit();
            	return 1;
            }
            ps = con.prepareStatement(sql1);
            n=1;
            ps.setLong(n++,payWithdrawCashOrder.txamt+payWithdrawCashOrder.fee);
            ps.setLong(n++,payWithdrawCashOrder.txamt+payWithdrawCashOrder.fee);
            ps.setLong(n++,payWithdrawCashOrder.txamt+payWithdrawCashOrder.fee);
            ps.setLong(n++,payWithdrawCashOrder.txamt+payWithdrawCashOrder.fee);
            ps.setString(n++,payWithdrawCashOrder.custType);
            ps.setString(n++,payWithdrawCashOrder.custId);
            if(ps.executeUpdate()==0)throw new Exception("提现账户余额不足");
//            else {
//            	ps.close();
//            	ps = con.prepareStatement(sql2);
//                n=1;
//                ps.setLong(n++,payWithdrawCashOrder.txamt);
//                ps.setLong(n++,payWithdrawCashOrder.txamt);
//                ps.setString(n++,PayConstant.CUST_TYPE_MERCHANT);//账户类型 0个人,1商户
//                ps.setString(n++,PayConstant.PAY_CONFIG.get("PAY_SYS_ACCOUNT"));
//                if(ps.executeUpdate()==0)throw new Exception("增加提现手续费错误");
////            	con.commit();
//            }
            return 1;
        } catch (Exception e) {
//        	con.rollback();
            e.printStackTrace();
            throw e;
        } finally {
//        	con.setAutoCommit(true);
            close(null, ps, con);
        }
    }
    public int updateWithdrawCashOrderStatus(PayWithdrawCashOrder payWithdrawCashOrder) throws Exception {
        String sql="update PAY_WITHDRAW_CASH_ORDER set " +
        		("02".equals(payWithdrawCashOrder.ordstatus)?"SUC_TIME=?,":"") +//交易成功
        		"ORDSTATUS=?,WITHDRAW_CHANNEL=? where CASORDNO=?";
        log.info(sql);
        log.info("SUC_TIME="+payWithdrawCashOrder.sucTime+";ORDSTATUS="+payWithdrawCashOrder.ordstatus+";WITHDRAW_CHANNEL="+payWithdrawCashOrder.withdrawChannel+
        		";CASORDNO="+payWithdrawCashOrder.casordno);
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            int n=1;
            if("02".equals(payWithdrawCashOrder.ordstatus))ps.setString(n++,
            	new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payWithdrawCashOrder.sucTime == null ? new Date() : payWithdrawCashOrder.sucTime));
            ps.setString(n++,payWithdrawCashOrder.ordstatus);
            ps.setString(n++,payWithdrawCashOrder.withdrawChannel);
            ps.setString(n++,payWithdrawCashOrder.casordno);
            return ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(null, ps, con);
        }
    }
    public List getList() throws Exception{
        String sql = "select * from PAY_WITHDRAW_CASH_ORDER";
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
                list.add(getPayWithdrawCashOrderValue(rs));
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
     * @param payWithdrawCashOrder
     * @return
     */
    private String setPayWithdrawCashOrderSql(PayWithdrawCashOrder payWithdrawCashOrder) {
        StringBuffer sql = new StringBuffer();

        if(payWithdrawCashOrder.casordno != null && payWithdrawCashOrder.casordno.length() !=0) {
            sql.append(" CASORDNO = ? and ");
        }
        if(payWithdrawCashOrder.ordstatus != null && payWithdrawCashOrder.ordstatus.length() !=0) {
            sql.append(" ORDSTATUS = ? and ");
        }
        if(payWithdrawCashOrder.actTime != null) {
            sql.append(" ACT_TIME >= to_date(?,'yyyy-mm-dd hh24:mi:ss') and ");
        }
        if(payWithdrawCashOrder.actTimeEnd != null) {
        	sql.append(" ACT_TIME <= to_date(?,'yyyy-mm-dd hh24:mi:ss') and ");
        }
        if(payWithdrawCashOrder.sucTime != null) {
            sql.append(" trunc(SUC_TIME) = to_date(?,'yyyy-mm-dd') and ");
        }
        if(payWithdrawCashOrder.custId != null && payWithdrawCashOrder.custId.length() !=0) {
            sql.append(" CUST_ID = ? and ");
        }
        if(payWithdrawCashOrder.bankpayacno != null && payWithdrawCashOrder.bankpayacno.length() !=0) {
            sql.append(" BANKPAYACNO = ? and ");
        }
        if(payWithdrawCashOrder.bankpayusernm != null && payWithdrawCashOrder.bankpayusernm.length() !=0) {
            sql.append(" BANKPAYUSERNM like ? and ");
        }
        String tmp = sql.toString();
        if(tmp.length()>0)tmp=tmp.substring(0,tmp.lastIndexOf(" and "));
        return tmp;
    }
    /**
     * Set query parameter.
     * @param ps
     * @param payWithdrawCashOrder
     * @param n
     * @return
     * @throws SQLException
     */
    private int setPayWithdrawCashOrderParameter(PreparedStatement ps,PayWithdrawCashOrder payWithdrawCashOrder,int n)throws SQLException {
        if(payWithdrawCashOrder.casordno != null && payWithdrawCashOrder.casordno.length() !=0) {
            ps.setString(n++,payWithdrawCashOrder.casordno);
        }
        if(payWithdrawCashOrder.ordstatus != null && payWithdrawCashOrder.ordstatus.length() !=0) {
            ps.setString(n++,payWithdrawCashOrder.ordstatus);
        }
        if(payWithdrawCashOrder.actTime != null) {
            ps.setString(n++, new java.text.SimpleDateFormat("yyyy-MM-dd").format(payWithdrawCashOrder.actTime) +" 00:00:00");
        }
        if(payWithdrawCashOrder.actTimeEnd != null) {
            ps.setString(n++, new java.text.SimpleDateFormat("yyyy-MM-dd").format(payWithdrawCashOrder.actTimeEnd) +" 23:59:59");
        }
        if(payWithdrawCashOrder.sucTime != null) {
            ps.setString(n++, new java.text.SimpleDateFormat("yyyy-MM-dd").format(payWithdrawCashOrder.sucTime));
        }
        if(payWithdrawCashOrder.custId != null && payWithdrawCashOrder.custId.length() !=0) {
            ps.setString(n++,payWithdrawCashOrder.custId);
        }
        if(payWithdrawCashOrder.bankpayacno != null && payWithdrawCashOrder.bankpayacno.length() !=0) {
            ps.setString(n++,payWithdrawCashOrder.bankpayacno);
        }
        if(payWithdrawCashOrder.bankpayusernm != null && payWithdrawCashOrder.bankpayusernm.length() !=0) {
            ps.setString(n++,"%" + payWithdrawCashOrder.bankpayusernm +"%");
        }
        return n;
    }
    /**
     * Get records count.
     * @param payWithdrawCashOrder
     * @return
     */
    public int getPayWithdrawCashOrderCount(PayWithdrawCashOrder payWithdrawCashOrder) {
        String sqlCon = setPayWithdrawCashOrderSql(payWithdrawCashOrder);
        String sql = "select count(rownum) recordCount from PAY_WITHDRAW_CASH_ORDER " +(sqlCon.length()==0?"":" where "+ sqlCon);
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            int n = 1;
            setPayWithdrawCashOrderParameter(ps,payWithdrawCashOrder,n);
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
     * @param payWithdrawCashOrder
     * @param page
     * @param rows
     * @param sort
     * @param order
     * @return
     * @throws Exception
     */
    public List getPayWithdrawCashOrderList(PayWithdrawCashOrder payWithdrawCashOrder,int page,int rows,String sort,String order) throws Exception{
        sort = Tools.paraNameToDBColumnName(sort);
        String sqlCon = setPayWithdrawCashOrderSql(payWithdrawCashOrder);
        String sortOrder = sort == null || sort.length()==0?" order by ACT_TIME desc":(" order by " + sort +("desc".equals(order)?" desc ":" asc "));
        String sql = "select * from (" +
                "  select rownum rowno,tmp1.* from (" +
                "   select tmp.*  from PAY_WITHDRAW_CASH_ORDER tmp " +(sqlCon.length()==0?"":" where "+ sqlCon) +sortOrder+
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
            setPayWithdrawCashOrderParameter(ps,payWithdrawCashOrder,n);
            rs = ps.executeQuery();
            while(rs.next()){
                list.add(getPayWithdrawCashOrderValue(rs));
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
     * remove PayWithdrawCashOrder
     * @param casordno
     * @throws Exception     
     */
    public void removePayWithdrawCashOrder(String casordno) throws Exception {
        String sql = "delete from PAY_WITHDRAW_CASH_ORDER where CASORDNO=?";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            ps.setString(1,casordno);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(null, ps, con);
        }
    }
    /**
     * detail PayWithdrawCashOrder
     * @param casordno
     * @return PayWithdrawCashOrder
     * @throws Exception
     */
    public PayWithdrawCashOrder detailPayWithdrawCashOrder(String casordno) throws Exception {
        String sql = "select * from PAY_WITHDRAW_CASH_ORDER where CASORDNO=?";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            ps.setString(1,casordno);
            rs = ps.executeQuery();
            if(rs.next())return getPayWithdrawCashOrderValue(rs); 
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(rs, ps, con);
        }
        return null;
    }
    /**
     * update PayWithdrawCashOrder
     * @param payWithdrawCashOrder
     * @throws Exception
     */
    public void updatePayWithdrawCashOrder(PayWithdrawCashOrder payWithdrawCashOrder) throws Exception {
        String sqlTmp = "";
        if(payWithdrawCashOrder.sucTime != null)sqlTmp = sqlTmp + " SUC_TIME=to_date(?,'yyyy-mm-dd hh24:mi:ss'),";
        if(payWithdrawCashOrder.ordstatus != null)sqlTmp = sqlTmp + " ORDSTATUS=?,";
        if(payWithdrawCashOrder.bankjrnno != null)sqlTmp = sqlTmp + " BANKJRNNO=?,";
        if(payWithdrawCashOrder.bankerror != null)sqlTmp = sqlTmp + " BANKERROR=BANKERROR||?,";
        if(payWithdrawCashOrder.payret != null)sqlTmp = sqlTmp + " PAYRET=?,";
        if(payWithdrawCashOrder.updateTime != null)sqlTmp = sqlTmp + " UPDATE_TIME=to_date(?,'yyyy-mm-dd hh24:mi:ss'),";
        if(payWithdrawCashOrder.updateUser != null)sqlTmp = sqlTmp + " UPDATE_USER=?,";
        if(payWithdrawCashOrder.withdrawWay != null)sqlTmp = sqlTmp + " WITHDRAW_WAY=?,";
        if(sqlTmp.length()==0)return;
        String sql = "update PAY_WITHDRAW_CASH_ORDER "+        
              "set " + 
              sqlTmp.substring(0,sqlTmp.length()-1) + 
            " where (ORDSTATUS='00' or ORDSTATUS='01') and CASORDNO=?"; 
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            int n=1;
            if(payWithdrawCashOrder.sucTime != null)ps.setString(n++,new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payWithdrawCashOrder.sucTime));
            if(payWithdrawCashOrder.ordstatus != null)ps.setString(n++,payWithdrawCashOrder.ordstatus);
            if(payWithdrawCashOrder.bankjrnno != null)ps.setString(n++,payWithdrawCashOrder.bankjrnno);
            if(payWithdrawCashOrder.bankerror != null)ps.setString(n++,payWithdrawCashOrder.bankerror);
            if(payWithdrawCashOrder.payret != null)ps.setString(n++,payWithdrawCashOrder.payret);
            if(payWithdrawCashOrder.updateTime != null)ps.setString(n++,new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payWithdrawCashOrder.updateTime));
            if(payWithdrawCashOrder.updateUser != null)ps.setString(n++,payWithdrawCashOrder.updateUser);
            if(payWithdrawCashOrder.withdrawWay != null)ps.setString(n++,payWithdrawCashOrder.withdrawWay);
            ps.setString(n++,payWithdrawCashOrder.casordno);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }finally {
            close(null, ps, con);
        }
    }
	public void updatePayWithdrawCashOrderStatus(String casordno,String columName, String value) throws Exception {
		String sql = "UPDATE PAY_WITHDRAW_CASH_ORDER SET "+columName+" = ? WHERE CASORDNO = ? ";
        log.info(sql);
        log.info(columName+"="+value+";CASORDNO="+casordno);
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            ps.setString(1, value);
            ps.setString(2, casordno);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(null, ps, con);
        }
	}
	
	public void updatePayWithdrawCashOrderStatus(String[] params) throws Exception {
		String sql = "UPDATE PAY_WITHDRAW_CASH_ORDER SET ORDSTATUS = ?,"
				+ "WITHDRAW_WAY=?,BANKERROR=BANKERROR || '；"
				+("0".equals(params[2])?"线上转线下":"")
				+ "；' WHERE CASORDNO = ? ";
        log.info(sql);
        log.info("ORDSTATUS="+params[1]+";WITHDRAW_WAY=1;CASORDNO"+params[0]);
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            ps.setString(1, params[1]);
            ps.setString(2, "1");
            ps.setString(3, params[0]);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(null, ps, con);
        }
	}
	
	public void updatePayWithdrawCashOrderStatus(PayWithdrawCashOrder pOrder)throws Exception {
		String sql = "UPDATE PAY_WITHDRAW_CASH_ORDER SET " +
				("02".equals(pOrder.ordstatus)?"SUC_TIME = to_date(?,'yyyy-mm-dd hh24:mi:ss'),":"") +
				"ORDSTATUS = ?,BANKJRNNO = ?,BANKERROR = ?,PAYRET = ? WHERE CASORDNO = ? ";
		//更新提现账户余额
        String sql1="update PAY_ACC_PROFILE set CASH_AC_BAL=CASH_AC_BAL+?,CONS_AC_BAL=CONS_AC_BAL+?" +
        		" where AC_TYPE=? and PAY_AC_NO=?";
        //更新系统账户余额(添加手续费)
        String sql2="update PAY_ACC_PROFILE set CASH_AC_BAL=CASH_AC_BAL-?,CONS_AC_BAL=CONS_AC_BAL-?" +
        		" where AC_TYPE=? and PAY_AC_NO=?";
        
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = connection();
//            con.setAutoCommit(false);
            ps = con.prepareStatement(sql);

            log.info(sql);
            log.info("SUC_TIME="+new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date())+
            		";ORDSTATUS="+pOrder.ordstatus+";BANKJRNNO="+pOrder.bankjrnno+";BANKERROR="+pOrder.bankerror+";PAYRET="+pOrder.payret+";CASORDNO="+pOrder.casordno);

            log.info(sql);
            log.info("ORDSTATUS="+pOrder.ordstatus+";BANKJRNNO="+pOrder.bankjrnno+";BANKERROR="+pOrder.bankerror+";PAYRET="+pOrder.payret+";CASORDNO="+pOrder.casordno);

            int n=1;
            if("02".equals(pOrder.ordstatus)){
            	ps.setString(n++, new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date()));
            }
            ps.setString(n++, pOrder.ordstatus);
            ps.setString(n++, pOrder.bankjrnno);
            ps.setString(n++, pOrder.bankerror);
            ps.setString(n++, pOrder.payret); 
            ps.setString(n++, pOrder.casordno);
            if(ps.executeUpdate()>0){
            	if("03".equals(pOrder.ordstatus)){
	            	ps.close();
	            	ps = con.prepareStatement(sql1);
	            	n=1;
	            	ps.setLong(n++,pOrder.txamt);
	                ps.setLong(n++,pOrder.txamt);
	                ps.setString(n++,pOrder.custType);
	                ps.setString(n++,pOrder.custId);
	                log.info(sql1);
	                log.info("CASH_AC_BAL="+pOrder.txamt+";AC_TYPE="+pOrder.custType+";PAY_AC_NO="+pOrder.custId);
	                if(ps.executeUpdate()>0){
	                	ps.close();
	                	ps = con.prepareStatement(sql2);
	                    n=1;
	                    ps.setLong(n++,pOrder.txamt);
	                    ps.setLong(n++,pOrder.txamt);
	                    ps.setString(n++,PayConstant.CUST_TYPE_MERCHANT);//账户类型 0个人,1商户
	                    ps.setString(n++,PayConstant.PAY_CONFIG.get("PAY_SYS_ACCOUNT"));
	                    log.info(sql2);
	                    log.info("CASH_AC_BAL="+pOrder.txamt+";AC_TYPE="+PayConstant.CUST_TYPE_MERCHANT+";PAY_AC_NO="+PayConstant.PAY_CONFIG.get("PAY_SYS_ACCOUNT"));
	                    if(ps.executeUpdate()==0)throw new Exception("提现渠道通知手续费回滚错误");
	                } else throw new Exception("提现渠道通知客户余额回滚错误");
            	}
	        } else throw new Exception("提现渠道通知状态更新错误");
//            con.commit();
        } catch (Exception e) {
//        	con.rollback();
            e.printStackTrace();
            throw e;
        } finally {
//        	con.setAutoCommit(true);
            close(null, ps, con);
        }
	}
	
	/**
	 * 获取提现金额汇总
	 * @param payWithdrawCashOrder
	 * @return
	 */
	public String getTotalPayWithdrawCashOrder(PayWithdrawCashOrder payWithdrawCashOrder) {
		String sqlCon = setPayWithdrawCashOrderSql(payWithdrawCashOrder);
        String sql = "select SUM(TXAMT) as TXAMT_COUNT from PAY_WITHDRAW_CASH_ORDER " +(sqlCon.length()==0?"":" where "+ sqlCon);
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            int n = 1;
            setPayWithdrawCashOrderParameter(ps,payWithdrawCashOrder,n);
            rs = ps.executeQuery();
            if (rs.next()) {
               Long txamtCount= rs.getLong("TXAMT_COUNT");
               return txamtCount.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(rs, ps, con);
        }
        return null;
	}
	public List getPayOrderList(PayWithdrawCashOrder payWithdrawCashOrder,long start, long end) throws Exception {
        String sqlCon = setPayWithdrawCashOrderSql(payWithdrawCashOrder);
        String sql = "select * from (" +
                "  select rownum rowno,tmp1.* from (" +
                "   select tmp.*  from PAY_WITHDRAW_CASH_ORDER tmp " +(sqlCon.length()==0?"":" where "+ sqlCon) +" order by CASORDNO"+
                "  ) tmp1 "+
                ")  where rowno > "+start+ " and rowno<= " + end + " order by CASORDNO";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List list = new ArrayList();
        try {
            con = connection();
            ps = con.prepareStatement(sql.toString());
            int n = 1;
            setPayWithdrawCashOrderParameter(ps,payWithdrawCashOrder,n);
            rs = ps.executeQuery();
            while(rs.next()){
                list.add(getPayWithdrawCashOrderValue(rs));
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(rs, ps, con);
        }
	}
	public void setResultWithdrawCashOrder(
			PayWithdrawCashOrder payWithdrawCashOrder) {
		
	}
	/**
	 * 查询手续费总金额
	 * @param payWithdrawCashOrder
	 * @return
	 */
	public String getTotalPayWithdrawFee(PayWithdrawCashOrder payWithdrawCashOrder) {
		String sqlCon = setPayWithdrawCashOrderSql(payWithdrawCashOrder);
        String sql = "select SUM(FEE) as FEE_COUNT from PAY_WITHDRAW_CASH_ORDER " +(sqlCon.length()==0?"":" where "+ sqlCon);
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            int n = 1;
            setPayWithdrawCashOrderParameter(ps,payWithdrawCashOrder,n);
            rs = ps.executeQuery();
            if (rs.next()) {
               Long feeCount= rs.getLong("FEE_COUNT");
               return feeCount.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(rs, ps, con);
        }
        return null;
	}
}