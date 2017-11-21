package com.pay.coopbank.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import util.Tools;

import com.PayConstant;
import com.jweb.dao.BaseDAO;
import com.pay.merchantinterface.service.PayRequest;
import com.pay.order.dao.PayOrder;
/**
 * Table PAY_CHANNEL_ROTATION_LOG DAO. 
 * @author Administrator
 *
 */
public class PayChannelRotationLogDAO  extends BaseDAO {
    private static final Log log = LogFactory.getLog(PayChannelRotationLogDAO.class);
    public static synchronized PayChannelRotationLog getPayChannelRotationLogValue(ResultSet rs)throws SQLException {
        PayChannelRotationLog payChannelRotationLog = new PayChannelRotationLog();
        payChannelRotationLog.channelId = rs.getString("CHANNEL_ID");
        payChannelRotationLog.day = rs.getString("DAY");
        payChannelRotationLog.dayAmt = rs.getLong("DAY_AMT");
        payChannelRotationLog.daySuccessAmt = rs.getLong("DAY_SUCCESS_AMT");
        payChannelRotationLog.dayCount = rs.getLong("DAY_COUNT");
        payChannelRotationLog.daySuccessCount = rs.getLong("DAY_SUCCESS_COUNT");
        payChannelRotationLog.channelFee = rs.getLong("CHANNEL_FEE");
        payChannelRotationLog.channelAcc = rs.getString("CHANNEL_ACC");
        payChannelRotationLog.dayCountZfbScan = rs.getLong("DAY_COUNT_ZFB_SCAN");
        payChannelRotationLog.dayCountWeixinScan = rs.getLong("DAY_COUNT_WEIXIN_SCAN");
        payChannelRotationLog.dayCountQqScan = rs.getLong("DAY_COUNT_QQ_SCAN");
        payChannelRotationLog.daySucCountZfbScan = rs.getLong("DAY_SUC_COUNT_ZFB_SCAN");
        payChannelRotationLog.daySucCountWeixinScan = rs.getLong("DAY_SUC_COUNT_WEIXIN_SCAN");
        payChannelRotationLog.daySucCountQqScan = rs.getLong("DAY_SUC_COUNT_QQ_SCAN");
        payChannelRotationLog.dayAmtZfbScan = rs.getLong("DAY_AMT_ZFB_SCAN");
        payChannelRotationLog.dayAmtWeixinScan = rs.getLong("DAY_AMT_WEIXIN_SCAN");
        payChannelRotationLog.dayAmtQqScan = rs.getLong("DAY_AMT_QQ_SCAN");
        payChannelRotationLog.daySucAmtZfbScan = rs.getLong("DAY_SUC_AMT_ZFB_SCAN");
        payChannelRotationLog.daySucAmtWeixinScan = rs.getLong("DAY_SUC_AMT_WEIXIN_SCAN");
        payChannelRotationLog.daySucAmtQqScan = rs.getLong("DAY_SUC_AMT_QQ_SCAN");
        return payChannelRotationLog;
    }
    public String updateOrAddPayChannelRotationLog(PayRequest payRequest,
    		PayChannelRotationLog payChannelRotationLog) throws Exception {
    	String pT = payRequest.payOrder.paytype;
    	long amt = payRequest.payOrder.txamt;
    	String sqlUpdate = "update PAY_CHANNEL_ROTATION_LOG set DAY_AMT=DAY_AMT+?,DAY_COUNT=DAY_COUNT+1," +
    			"DAY_COUNT_ZFB_SCAN=DAY_COUNT_ZFB_SCAN"+("11".equals(pT)?"+1":"")+",DAY_COUNT_WEIXIN_SCAN=DAY_COUNT_WEIXIN_SCAN"+("07".equals(pT)?"+1":"")+",DAY_COUNT_QQ_SCAN=DAY_COUNT_QQ_SCAN"+("12".equals(pT)?"+1":"")+"," +
    			"DAY_AMT_ZFB_SCAN=DAY_AMT_ZFB_SCAN"+("11".equals(pT)?"+"+amt:"")+",DAY_AMT_WEIXIN_SCAN=DAY_AMT_WEIXIN_SCAN"+("07".equals(pT)?"+"+amt:"")+",DAY_AMT_QQ_SCAN=DAY_AMT_QQ_SCAN"+("12".equals(pT)?"+"+amt:"")+" " +
    			"where CHANNEL_ID=? and DAY=? and CHANNEL_ACC=?";
    	if("11".equals(pT)){
    		payChannelRotationLog.dayCountZfbScan=1l;
    		payChannelRotationLog.dayAmtZfbScan=amt;
    	} else if("07".equals(pT)){
    		payChannelRotationLog.dayCountWeixinScan=1l;
    		payChannelRotationLog.dayAmtWeixinScan=amt;
    	} else if("12".equals(pT)){
    		payChannelRotationLog.dayCountQqScan=1l;
    		payChannelRotationLog.dayAmtQqScan=amt;
    	}
        String sqlInsert = "insert into PAY_CHANNEL_ROTATION_LOG("+
            "CHANNEL_ID," + 
            "DAY," + 
            "DAY_AMT," + 
            "DAY_SUCCESS_AMT," + 
            "DAY_COUNT," + 
            "DAY_SUCCESS_COUNT," + 
            "CHANNEL_FEE," + 
            "CHANNEL_ACC," + 
            "DAY_COUNT_ZFB_SCAN," + 
            "DAY_COUNT_WEIXIN_SCAN," + 
            "DAY_COUNT_QQ_SCAN," + 
            "DAY_SUC_COUNT_ZFB_SCAN," + 
            "DAY_SUC_COUNT_WEIXIN_SCAN," + 
            "DAY_SUC_COUNT_QQ_SCAN," + 
            "DAY_AMT_ZFB_SCAN," + 
            "DAY_AMT_WEIXIN_SCAN," + 
            "DAY_AMT_QQ_SCAN," + 
            "DAY_SUC_AMT_ZFB_SCAN," + 
            "DAY_SUC_AMT_WEIXIN_SCAN," + 
            "DAY_SUC_AMT_QQ_SCAN)values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = connection();
            con.setAutoCommit(false);
            log.info(sqlUpdate);
            ps = con.prepareStatement(sqlUpdate);
            int n=1;
            ps.setLong(n++,payChannelRotationLog.dayAmt);
            ps.setString(n++,payChannelRotationLog.channelId);
            ps.setString(n++,payChannelRotationLog.day);
            ps.setString(n++,payChannelRotationLog.channelAcc);
            if(ps.executeUpdate() == 0){
            	ps.close();
	            n=1;
	            ps = con.prepareStatement(sqlInsert);
	            ps.setString(n++,payChannelRotationLog.channelId);
	            ps.setString(n++,payChannelRotationLog.day);
	            ps.setLong(n++,payChannelRotationLog.dayAmt);
	            ps.setLong(n++,payChannelRotationLog.daySuccessAmt);
	            ps.setLong(n++,payChannelRotationLog.dayCount);
	            ps.setLong(n++,payChannelRotationLog.daySuccessCount);
	            ps.setLong(n++,payChannelRotationLog.channelFee);
	            ps.setString(n++,payChannelRotationLog.channelAcc);
	            ps.setLong(n++,payChannelRotationLog.dayCountZfbScan);
	            ps.setLong(n++,payChannelRotationLog.dayCountWeixinScan);
	            ps.setLong(n++,payChannelRotationLog.dayCountQqScan);
	            ps.setLong(n++,payChannelRotationLog.daySucCountZfbScan);
	            ps.setLong(n++,payChannelRotationLog.daySucCountWeixinScan);
	            ps.setLong(n++,payChannelRotationLog.daySucCountQqScan);
	            ps.setLong(n++,payChannelRotationLog.dayAmtZfbScan);
	            ps.setLong(n++,payChannelRotationLog.dayAmtWeixinScan);
	            ps.setLong(n++,payChannelRotationLog.dayAmtQqScan);
	            ps.setLong(n++,payChannelRotationLog.daySucAmtZfbScan);
	            ps.setLong(n++,payChannelRotationLog.daySucAmtWeixinScan);
	            ps.setLong(n++,payChannelRotationLog.daySucAmtQqScan);
	            ps.executeUpdate();
            }
            ps.close();
            n=1;
            ps = con.prepareStatement("insert into PAY_CHANNEL_ROTATION_ORDER(ORDER_ID,CHANNEL_ID,CHANNEL_ACC,CREATE_TIME)values(?,?,?,to_date(?,'yyyy-mm-dd hh24:mi:ss'))");
            ps.setString(n++,payRequest.payOrder.payordno);
            ps.setString(n++,payChannelRotationLog.channelId);
            ps.setString(n++,payChannelRotationLog.channelAcc);
            ps.setString(n++,new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payRequest.payOrder.createtime));
            ps.executeUpdate();
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
    public List getList() throws Exception{
        String sql = "select * from PAY_CHANNEL_ROTATION_LOG";
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
                list.add(getPayChannelRotationLogValue(rs));
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
     * @param payChannelRotationLog
     * @return
     */
    private String setPayChannelRotationLogSql(PayChannelRotationLog payChannelRotationLog) {
        StringBuffer sql = new StringBuffer();
        
        if(payChannelRotationLog.channelId != null && payChannelRotationLog.channelId.length() !=0) {
            sql.append(" CHANNEL_ID = ? and ");
        }
        if(payChannelRotationLog.day != null && payChannelRotationLog.day.length() !=0) {
            sql.append(" DAY = ? and ");
        }
        if(payChannelRotationLog.channelAcc != null && payChannelRotationLog.channelAcc.length() !=0) {
            sql.append(" CHANNEL_ACC = ? and ");
        }
        String tmp = sql.toString();
        if(tmp.length()>0)tmp=tmp.substring(0,tmp.lastIndexOf(" and "));
        return tmp;
    }
    /**
     * Set query parameter.
     * @param ps
     * @param payChannelRotationLog
     * @param n
     * @return
     * @throws SQLException
     */
    private int setPayChannelRotationLogParameter(PreparedStatement ps,PayChannelRotationLog payChannelRotationLog,int n)throws SQLException {
        if(payChannelRotationLog.channelId != null && payChannelRotationLog.channelId.length() !=0) {
            ps.setString(n++,payChannelRotationLog.channelId);
        }
        if(payChannelRotationLog.day != null && payChannelRotationLog.day.length() !=0) {
            ps.setString(n++,payChannelRotationLog.day);
        }
        if(payChannelRotationLog.channelAcc != null && payChannelRotationLog.channelAcc.length() !=0) {
            ps.setString(n++,payChannelRotationLog.channelAcc);
        }
        return n;
    }
    /**
     * Get records count.
     * @param payChannelRotationLog
     * @return
     */
    public int getPayChannelRotationLogCount(PayChannelRotationLog payChannelRotationLog) {
        String sqlCon = setPayChannelRotationLogSql(payChannelRotationLog);
        String sql = "select count(rownum) recordCount from PAY_CHANNEL_ROTATION_LOG " +(sqlCon.length()==0?"":" where "+ sqlCon);
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            int n = 1;
            setPayChannelRotationLogParameter(ps,payChannelRotationLog,n);
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
     * @param payChannelRotationLog
     * @param page
     * @param rows
     * @param sort
     * @param order
     * @return
     * @throws Exception
     */
    public List getPayChannelRotationLogList(PayChannelRotationLog payChannelRotationLog,int page,int rows,String sort,String order) throws Exception{
        sort = Tools.paraNameToDBColumnName(sort);
        String sqlCon = setPayChannelRotationLogSql(payChannelRotationLog);
        String sortOrder = sort == null || sort.length()==0?" order by CHANNEL_ID desc":(" order by " + sort +("desc".equals(order)?" desc ":" asc "));
        String sql = "select * from (" +
                "  select rownum rowno,tmp1.* from (" +
                "   select tmp.*  from PAY_CHANNEL_ROTATION_LOG tmp " +(sqlCon.length()==0?"":" where "+ sqlCon) +sortOrder+
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
            setPayChannelRotationLogParameter(ps,payChannelRotationLog,n);
            rs = ps.executeQuery();
            while(rs.next()){
                list.add(getPayChannelRotationLogValue(rs));
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
     * 更新交易日志和最后成功交易时间和笔数
     * @param order
     * @throws Exception
     */
	public void updatePayChannelRotationLogSuccessTran(PayOrder order) throws Exception {
		String pT = order.paytype;
    	String sqlUpdate = "update PAY_CHANNEL_ROTATION_LOG crl set DAY_SUCCESS_AMT=DAY_SUCCESS_AMT+?,DAY_SUCCESS_COUNT=DAY_SUCCESS_COUNT+1," +
    		"DAY_SUC_COUNT_ZFB_SCAN=DAY_SUC_COUNT_ZFB_SCAN"+("11".equals(pT)?"+1":"")+",DAY_SUC_COUNT_WEIXIN_SCAN=DAY_SUC_COUNT_WEIXIN_SCAN"+("07".equals(pT)?"+1":"")+",DAY_SUC_COUNT_QQ_SCAN=DAY_SUC_COUNT_QQ_SCAN"+("12".equals(pT)?"+1":"")+"," +
    		"DAY_SUC_AMT_ZFB_SCAN=DAY_SUC_AMT_ZFB_SCAN"+("11".equals(pT)?"+"+order.txamt:"")+",DAY_SUC_AMT_WEIXIN_SCAN=DAY_SUC_AMT_WEIXIN_SCAN"+("07".equals(pT)?"+"+order.txamt:"")+",DAY_SUC_AMT_QQ_SCAN=DAY_SUC_AMT_QQ_SCAN"+("12".equals(pT)?"+"+order.txamt:"")+"," +
    		"CHANNEL_FEE=? where exists(select 1 from PAY_CHANNEL_ROTATION_ORDER cro where crl.CHANNEL_ID=cro.CHANNEL_ID and crl.CHANNEL_ACC=cro.CHANNEL_ACC and crl.day=? and cro.ORDER_ID=?)";
    	String sql1 = "update PAY_CHANNEL_ROTATION cr set LAST_SUC_USED_TIME=sysdate,LAST_SUC_TRAN_AMT=? " +
    		"where exists(select 1 from PAY_CHANNEL_ROTATION_ORDER cro where cr.CHANNEL_ID=cro.CHANNEL_ID and cr.MERCHANT_ID=cro.CHANNEL_ACC and cro.ORDER_ID=?) ";
        Connection con = null;
        PreparedStatement ps = null;
        try {
        	log.info(sqlUpdate);
        	log.info("DAY_SUCCESS_AMT="+order.txamt+",CHANNEL_FEE="+order.channelFee+",ORDER_ID="+order.payordno);
            con = connection();
            con.setAutoCommit(false);
            int n=1;
            ps = con.prepareStatement(sqlUpdate);
            ps.setLong(n++,order.txamt);
            ps.setLong(n++,order.channelFee);
            ps.setString(n++,new java.text.SimpleDateFormat("yyyyMMdd").format(order.createtime));
            ps.setString(n++,order.payordno);
            ps.executeUpdate();
            ps.close();
            ps = con.prepareStatement(sql1);
            n=1;
            ps.setLong(n++,order.txamt);
            ps.setString(n++,order.payordno);
            ps.executeUpdate();
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
	/**
	 * 汇总
	 * @param payChannelRotationLog
	 * @return
	 */
	public Long[] getTotalChannelRotationLog(
			PayChannelRotationLog payChannelRotationLog) {
		Long l1 = null,l2 = null,l3 = null,l4 = null,l5 = null;
    	String sqlCon = setPayChannelRotationLogSql(payChannelRotationLog);
        String sql = 
        		" SELECT SUM(DAY_AMT) totalDayAmt ,SUM(DAY_SUCCESS_AMT) totalDaySuccessAmt ,SUM(DAY_COUNT) totalDayCount ,SUM(DAY_SUCCESS_COUNT) totalDaySuccessCount ,SUM(CHANNEL_FEE) totalChannelFee " +
        				" FROM PAY_CHANNEL_ROTATION_LOG" +
                		(sqlCon.length()==0?"":" where "+ sqlCon);
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            int n = 1;
            setPayChannelRotationLogParameter(ps,payChannelRotationLog,n);
            rs = ps.executeQuery();
            if (rs.next()) {
            	l1 = rs.getLong("totalDayAmt");
            	l2 = rs.getLong("totalDaySuccessAmt");
            	l3 = rs.getLong("totalDayCount");
            	l4 = rs.getLong("totalDaySuccessCount");
            	l5 = rs.getLong("totalChannelFee");
            }
            return new Long[]{l1==null?0l:l1,l2==null?0l:l2,l3==null?0l:l3,l4==null?0l:l4,l5==null?0l:l5};
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(rs, ps, con);
        }
        return new Long[]{0l,0l};
	}
	
	/**
	 * excel报表导出
	 * @param flag
	 * @param payChannelRotationLog
	 * @param start
	 * @param end
	 * @return
	 * @throws Exception
	 */
    public List getPayChannelRotationLogList(String flag, PayChannelRotationLog payChannelRotationLog,Long start,Long end) throws Exception{
    	String sqlCon = setPayChannelRotationLogSql(payChannelRotationLog);
        String sql = "select * from (" +
                "  select rownum rowno,tmp1.* from (" +
                "   select tmp.*  from PAY_CHANNEL_ROTATION_LOG tmp " +(sqlCon.length()==0?"":" where "+ sqlCon) +
                "  ) tmp1 "+
                ")  where rowno > "+start+ " and rowno<= " + end ;
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List list = new ArrayList();
        try {
        	if("1".equals(flag)) con = connection(PayConstant.ORDER_SQL_NAME.get("ORDER_DB_BAK_NAME"));
        	else con = connection();
            ps = con.prepareStatement(sql.toString());
            int n = 1;
            setPayChannelRotationLogParameter(ps, payChannelRotationLog, n);
            rs = ps.executeQuery();
            while(rs.next()){
                list.add(getPayChannelRotationLogValue(rs));
            }
            return list;
		} catch (Exception e) {
			e.printStackTrace();
            throw e;
		}finally {
			 close(rs, ps, con);
		}
    }
	
}