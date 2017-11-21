package com.pay.coopbank.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import util.Tools;

import com.PayConstant;
import com.jweb.dao.BaseDAO;
/**
 * Table PAY_CHANNEL_ROTATION DAO. 
 * @author Administrator
 *
 */
public class PayChannelRotationDAO  extends BaseDAO {
    private static final Log log = LogFactory.getLog(PayChannelRotationDAO.class);
    public static synchronized PayChannelRotation getPayChannelRotationValue(ResultSet rs)throws SQLException {
        PayChannelRotation payChannelRotation = new PayChannelRotation();
        payChannelRotation.id = rs.getString("ID");
        payChannelRotation.type = rs.getString("TYPE");
        payChannelRotation.channelId = rs.getString("CHANNEL_ID");
        payChannelRotation.merchantId = rs.getString("MERCHANT_ID");
        payChannelRotation.md5Key = rs.getString("MD5_KEY");
        payChannelRotation.status = rs.getString("STATUS");
        payChannelRotation.createTime = rs.getTimestamp("CREATE_TIME");
        payChannelRotation.lastSucUsedTime = rs.getTimestamp("LAST_SUC_USED_TIME");
        payChannelRotation.lastSucTranAmt = rs.getLong("LAST_SUC_TRAN_AMT");
        payChannelRotation.batchNo = rs.getString("BATCH_NO");
        payChannelRotation.cancelTime = rs.getTimestamp("CANCEL_TIME");
        try {payChannelRotation.sumTranAmt=rs.getLong("sumTranAmt");} catch (Exception e) {}
        try {payChannelRotation.sumSuccessTranAmt=rs.getLong("sumSuccessTranAmt");} catch (Exception e) {}
        try {payChannelRotation.sumTranAccount=rs.getLong("sumTranAccount");} catch (Exception e) {}
        try {payChannelRotation.sumSuccessTranAccount=rs.getLong("sumSuccessTranAccount");} catch (Exception e) {}
        try {payChannelRotation.maxSumAmt=rs.getLong("MAX_SUM_AMT");} catch (Exception e) {}
        try{payChannelRotation.sumDayCountZfbScan=rs.getLong("sumDayCountZfbScan");} catch (Exception e) {}
        try{payChannelRotation.sumDayCountWeixinScan=rs.getLong("sumDayCountWeixinScan");} catch (Exception e) {}
        try{payChannelRotation.sumDayCountQqScan=rs.getLong("sumDayCountQqScan");} catch (Exception e) {}
        try{payChannelRotation.sumDaySucCountZfbScan=rs.getLong("sumDaySucCountZfbScan");} catch (Exception e) {}
        try{payChannelRotation.sumDaySucCountWeixinScan=rs.getLong("sumDaySucCountWeixinScan");} catch (Exception e) {}
        try{payChannelRotation.sumDaySucCountQqScan=rs.getLong("sumDaySucCountQqScan");} catch (Exception e) {}
        try{payChannelRotation.sumDayAmtZfbScan=rs.getLong("sumDayAmtZfbScan");} catch (Exception e) {}
        try{payChannelRotation.sumDayAmtWeixinScan=rs.getLong("sumDayAmtWeixinScan");} catch (Exception e) {}
        try{payChannelRotation.sumDayAmtQqScan=rs.getLong("sumDayAmtQqScan");} catch (Exception e) {}
        try{payChannelRotation.sumDaySucAmtZfbScan=rs.getLong("sumDaySucAmtZfbScan");} catch (Exception e) {}
        try{payChannelRotation.sumDaySucAmtWeixinScan=rs.getLong("sumDaySucAmtWeixinScan");} catch (Exception e) {}
        try{payChannelRotation.sumDaySucAmtQqScan=rs.getLong("sumDaySucAmtQqScan");} catch (Exception e) {}
        return payChannelRotation;
    }
    public String addPayChannelRotation(String type,String channelId,Long maxSumAmt,Map<String,String> map) throws Exception {
        String sqlInsert = "insert into PAY_CHANNEL_ROTATION("+
            "ID," + 
            "TYPE," + 
            "CHANNEL_ID," + 
            "MERCHANT_ID," + 
            "MD5_KEY," + 
            "STATUS," + 
            "BATCH_NO," +
            "MAX_SUM_AMT)values(?,?,?,?,?,?,?,?)"; 
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
        	con = connection();
        	//更新本渠道最大交易额
            String sqlLimit = "update PAY_CHANNEL_ROTATION set MAX_SUM_AMT=? where CHANNEL_ID=?";
            log.info(sqlLimit);
            log.info("MAX_SUM_AMT="+maxSumAmt+",CHANNEL_ID="+channelId);
            ps = con.prepareStatement(sqlLimit);
            ps.setLong(1, maxSumAmt);
            ps.setString(2, channelId);
            ps.executeUpdate();
        	//已存在账号判断，若存在，则不再添加
        	String tmp = "";
        	Iterator<String> it = map.keySet().iterator();
        	while(it.hasNext()){
        		String acc = it.next();
        		tmp = tmp + "MERCHANT_ID='"+acc+"' or ";
        	}
        	if(tmp.length()==0)return "";
        	tmp = "("+tmp.substring(0,tmp.length()-4)+") and TYPE='2' and CHANNEL_ID='"+channelId+"'";        	
            String sql = "select MERCHANT_ID from PAY_CHANNEL_ROTATION where "+tmp;
            ps.close();
            log.info(sql);
            ps = con.prepareStatement(sql);
            log.info(sql);
            rs = ps.executeQuery();
            while(rs.next()){
            	String acc = rs.getString("MERCHANT_ID");
            	if(map.get(acc)!=null)map.remove(acc);
            }
            rs.close();
            ps.close();
            log.info(sqlInsert);
            ps = con.prepareStatement(sqlInsert);
            it = map.keySet().iterator();
            String batchNo = Tools.getUniqueIdentify();
        	while(it.hasNext()){
        		int n=1;
        		String acc = it.next();
        		String key = map.get(acc);
        		ps.setString(n++, Tools.getUniqueIdentify());
        		ps.setString(n++, type);
        		ps.setString(n++, channelId);
        		ps.setString(n++, acc);
        		ps.setString(n++, key);
        		ps.setString(n++, "0");
        		ps.setString(n++, batchNo);
        		ps.setLong(n++, maxSumAmt);
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
    /**
     * Set query condition sql.
     * @param payChannelRotation
     * @return
     */
    private String setPayChannelRotationSql(PayChannelRotation payChannelRotation) {
        StringBuffer sql = new StringBuffer();
        
        if(payChannelRotation.type != null && payChannelRotation.type.length() !=0) {
            sql.append(" TYPE = ? and ");
        }
        if(payChannelRotation.channelId != null && payChannelRotation.channelId.length() !=0) {
            sql.append(" CHANNEL_ID = ? and ");
        }
        if(payChannelRotation.merchantId != null && payChannelRotation.merchantId.length() !=0) {
            sql.append(" MERCHANT_ID = ? and ");
        }
        if(payChannelRotation.status != null && payChannelRotation.status.length() !=0) {
            sql.append(" STATUS = ? and ");
        }
        if(payChannelRotation.createTimeStart != null || payChannelRotation.createTimeEnd != null) {
            String tmp = "";
	        if(payChannelRotation.createTimeStart != null) tmp = tmp + " CREATE_TIME >= to_date('"+
	        		new SimpleDateFormat("yyyy-MM-dd").format(payChannelRotation.createTimeStart)+" 00:00:00"+"','yyyy-mm-dd hh24:mi:ss') and ";
	        if(payChannelRotation.createTimeStart != null) tmp = tmp + " CREATE_TIME <= to_date('"+
	        		new SimpleDateFormat("yyyy-MM-dd").format(payChannelRotation.createTimeEnd)+" 23:59:59"+"','yyyy-mm-dd hh24:mi:ss') and ";
	        sql.append(" merchant_id in(select CHANNEL_ACC from PAY_CHANNEL_ROTATION_ORDER where "+tmp.substring(0,tmp.length()-5)+" group by CHANNEL_ACC) and ");
        }
        if(payChannelRotation.batchNo != null && payChannelRotation.batchNo.length() !=0) {
            sql.append(" BATCH_NO = ? and ");
        }
        String tmp = sql.toString();
        if(tmp.length()>0)tmp=tmp.substring(0,tmp.lastIndexOf(" and "));
        return tmp;
    }
    /**
     * Set query parameter.
     * @param ps
     * @param payChannelRotation
     * @param n
     * @return
     * @throws SQLException
     */
    private int setPayChannelRotationParameter(PreparedStatement ps,PayChannelRotation payChannelRotation,int n)throws SQLException {
        if(payChannelRotation.type != null && payChannelRotation.type.length() !=0) {
            ps.setString(n++,payChannelRotation.type);
        }
        if(payChannelRotation.channelId != null && payChannelRotation.channelId.length() !=0) {
            ps.setString(n++,payChannelRotation.channelId);
        }
        if(payChannelRotation.merchantId != null && payChannelRotation.merchantId.length() !=0) {
            ps.setString(n++,payChannelRotation.merchantId);
        }
        if(payChannelRotation.status != null && payChannelRotation.status.length() !=0) {
            ps.setString(n++,payChannelRotation.status);
        }
        if(payChannelRotation.batchNo != null && payChannelRotation.batchNo.length() !=0) {
            ps.setString(n++,payChannelRotation.batchNo);
        }
        return n;
    }
    /**
     * Get records count.
     * @param payChannelRotation
     * @return
     */
    public int getPayChannelRotationCount(PayChannelRotation payChannelRotation) {
        String sqlCon = setPayChannelRotationSql(payChannelRotation);
        String sql = "select count(rownum) recordCount from PAY_CHANNEL_ROTATION " +(sqlCon.length()==0?"":" where "+ sqlCon);
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            int n = 1;
            setPayChannelRotationParameter(ps,payChannelRotation,n);
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
     * @param payChannelRotation
     * @param page
     * @param rows
     * @param sort
     * @param order
     * @return
     * @throws Exception
     */
    public List getPayChannelRotationList(PayChannelRotation payChannelRotation,int page,int rows,String sort,String order) throws Exception{
        sort = Tools.paraNameToDBColumnName(sort);
        String sqlCon = setPayChannelRotationSql(payChannelRotation);
        String sortOrder = sort == null || sort.length()==0?" order by ID desc":(" order by " + sort +("desc".equals(order)?" desc ":" asc "));
        String sql = "select * from (" +
                "  select rownum rowno,tmp1.* from (" +
                "   select tmp.*  from (SELECT tmp.* FROM (select r.*,l.sumTranAmt,l.sumSuccessTranAmt,l.sumTranAccount,l.sumSuccessTranAccount,l.sumDayCountZfbScan,l.sumDayCountWeixinScan," +
                "		l.sumDayCountQqScan,l.sumDaySucCountZfbScan,l.sumDaySucCountWeixinScan,l.sumDaySucCountQqScan,l.sumDayAmtZfbScan,l.sumDayAmtWeixinScan,l.sumDayAmtQqScan,l.sumDaySucAmtZfbScan," +
                "		l.sumDaySucAmtWeixinScan,l.sumDaySucAmtQqScan from PAY_CHANNEL_ROTATION r left join "+ 
                "        (select channel_id,CHANNEL_ACC,sum(DAY_AMT)sumTranAmt, sum(DAY_SUCCESS_AMT)sumSuccessTranAmt, sum(DAY_COUNT)sumTranAccount, sum(DAY_SUCCESS_COUNT)sumSuccessTranAccount," +
                "			sum(DAY_COUNT_ZFB_SCAN)sumDayCountZfbScan,sum(DAY_COUNT_WEIXIN_SCAN)sumDayCountWeixinScan,sum(DAY_COUNT_QQ_SCAN)sumDayCountQqScan," +
                "			sum(DAY_SUC_COUNT_ZFB_SCAN)sumDaySucCountZfbScan,sum(DAY_SUC_COUNT_WEIXIN_SCAN)sumDaySucCountWeixinScan,sum(DAY_SUC_COUNT_QQ_SCAN)sumDaySucCountQqScan," +
                "			sum(DAY_AMT_ZFB_SCAN)sumDayAmtZfbScan,sum(DAY_AMT_WEIXIN_SCAN)sumDayAmtWeixinScan,sum(DAY_AMT_QQ_SCAN)sumDayAmtQqScan," +
                "			sum(DAY_SUC_AMT_ZFB_SCAN)sumDaySucAmtZfbScan,sum(DAY_SUC_AMT_WEIXIN_SCAN)sumDaySucAmtWeixinScan,sum(DAY_SUC_AMT_QQ_SCAN)sumDaySucAmtQqScan " +
                "		from PAY_CHANNEL_ROTATION_LOG group by channel_id,CHANNEL_ACC) l "+ 
                "        on r.channel_id=l.channel_id and r.merchant_id=l.CHANNEL_ACC) tmp " +(sqlCon.length()==0?"":" where "+ sqlCon) +sortOrder+
                "   )tmp  " +
                "  ) tmp1 "+
                ")  where rowno > "+((page-1)*rows)+ " and rowno<= " + (page*rows) + sortOrder;
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<PayChannelRotation> list = new ArrayList<PayChannelRotation>();
        try {
            con = connection();
            ps = con.prepareStatement(sql.toString());
            int n = 1;
            setPayChannelRotationParameter(ps,payChannelRotation,n);
            rs = ps.executeQuery();
            Map<String, Long> merIdMap = new HashMap<String, Long>();
            while(rs.next()){
            	PayChannelRotation pcr = getPayChannelRotationValue(rs);
                list.add(pcr);
                merIdMap.put(pcr.channelId,0l);
            }
            //取得提现总额
//            if(merIdMap.size()>0){
//            	String tmp = "";
//            	rs.close();
//            	ps.close();
//            	Iterator it = merIdMap.keySet().iterator();
//            	while(it.hasNext()){
//            		String key = (String)it.next();
//            		tmp = tmp + " CHANNEL_ID='"+key+"' or ";
//            	}
//            	tmp = tmp.substring(0,tmp.length()-4);
//            	sql = "select channel_id,sum(DAY_SUCCESS_COUNT-CHANNEL_FEE) sumWithDrawTranAmt from PAY_CHANNEL_ROTATION_LOG where ("+tmp+") and CHANNEL_IS_WITHDRAW='1' group by channel_id";
//            	rs = ps.executeQuery();
//            	ps = con.prepareStatement(sql);
//            	while(rs.next()){
//            		String channelId = rs.getString("channel_id");
//            		merIdMap.put(channelId, rs.getLong("sumWithDrawTranAmt"));
//            	}
//            	for(int i=0; i<list.size(); i++){
//            		PayChannelRotation pcr = list.get(i);
//            		pcr.sumWithDrawTranAmt = merIdMap.get(pcr.channelId) != null ? merIdMap.get(pcr.channelId):0l;
//            	}
//            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(rs, ps, con);
        }
    }
    /**
     * remove PayChannelRotation
     * @param id
     * @throws Exception     
     */
    public void removePayChannelRotation(String id) throws Exception {
        String sql = "delete from PAY_CHANNEL_ROTATION where ID=?";
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
     * detail PayChannelRotation
     * @param id
     * @return PayChannelRotation
     * @throws Exception
     */
    public PayChannelRotation detailPayChannelRotation(String id) throws Exception {
        String sql = "select * from PAY_CHANNEL_ROTATION where ID=?";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            ps.setString(1,id);
            rs = ps.executeQuery();
            if(rs.next())return getPayChannelRotationValue(rs); 
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(rs, ps, con);
        }
        return null;
    }
    /**
     * update PayChannelRotation
     * @param payChannelRotation
     * @throws Exception
     */
//    public void updatePayChannelRotation(PayChannelRotation payChannelRotation) throws Exception {
//        String sqlTmp = "";
//        if(payChannelRotation.id != null)sqlTmp = sqlTmp + " ID=?,";
//        if(payChannelRotation.type != null)sqlTmp = sqlTmp + " TYPE=?,";
//        if(payChannelRotation.channelId != null)sqlTmp = sqlTmp + " CHANNEL_ID=?,";
//        if(payChannelRotation.merchantId != null)sqlTmp = sqlTmp + " MERCHANT_ID=?,";
//        if(payChannelRotation.md5Key != null)sqlTmp = sqlTmp + " MD5_KEY=?,";
//        if(payChannelRotation.status != null)sqlTmp = sqlTmp + " STATUS=?,";
//        if(payChannelRotation.createTime != null)sqlTmp = sqlTmp + " CREATE_TIME=to_date(?,'yyyy-mm-dd hh24:mi:ss'),";
//        if(payChannelRotation.lastUsedTime != null)sqlTmp = sqlTmp + " LAST_USED_TIME=to_date(?,'yyyy-mm-dd hh24:mi:ss'),";
//        if(payChannelRotation.batchNo != null)sqlTmp = sqlTmp + " BATCH_NO=?,";
//        if(payChannelRotation.cancelTime != null)sqlTmp = sqlTmp + " CANCEL_TIME=to_date(?,'yyyy-mm-dd hh24:mi:ss'),";
//        if(sqlTmp.length()==0)return;
//        String sql = "update PAY_CHANNEL_ROTATION "+        
//              "set " + 
//              sqlTmp.substring(0,sqlTmp.length()-1) + 
//            " where ID=?"; 
//        log.info(sql);
//        Connection con = null;
//        PreparedStatement ps = null;
//        try {
//            con = connection();
//            ps = con.prepareStatement(sql);
//            int n=1;
//            if(payChannelRotation.id != null)ps.setString(n++,payChannelRotation.id);
//            if(payChannelRotation.type != null)ps.setString(n++,payChannelRotation.type);
//            if(payChannelRotation.channelId != null)ps.setString(n++,payChannelRotation.channelId);
//            if(payChannelRotation.merchantId != null)ps.setString(n++,payChannelRotation.merchantId);
//            if(payChannelRotation.md5Key != null)ps.setString(n++,payChannelRotation.md5Key);
//            if(payChannelRotation.status != null)ps.setString(n++,payChannelRotation.status);
//            if(payChannelRotation.createTime != null)ps.setString(n++,new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payChannelRotation.createTime));
//            if(payChannelRotation.lastUsedTime != null)ps.setString(n++,new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payChannelRotation.lastUsedTime));
//            if(payChannelRotation.batchNo != null)ps.setString(n++,payChannelRotation.batchNo);
//            if(payChannelRotation.cancelTime != null)ps.setString(n++,new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payChannelRotation.cancelTime));
//            ps.setString(n++,payChannelRotation.id);
//            ps.executeUpdate();
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw e;
//        }finally {
//            close(null, ps, con);
//        }
//    }
	public void updateBatchStatusChannelRotation(String status, String batchNo) throws Exception {
        String sql = "update PAY_CHANNEL_ROTATION set STATUS='"+status+"' "+("0".equals(status)?",CANCEL_TIME=sysdate":"")+" where BATCH_NO=?";
        if("2".equals(status))sql = "delete from PAY_CHANNEL_ROTATION where BATCH_NO=?";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            ps.setString(1,batchNo);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(null, ps, con);
        }
	}
	public void updatePayChannelRotationStatus(String id, String status) throws Exception {
		String sql = "update PAY_CHANNEL_ROTATION set STATUS='"+status+"' "+("0".equals(status)?",CANCEL_TIME=sysdate":"")+" where ID=?";
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
	public PayChannelRotation getPayChannelRotationByRule0(String day,String [] pcRotation,long txamt) throws Exception {
//		select tmp.channel_id,tmp.merchant_id,(case when DAY_SUCCESS_COUNT is null then 0 else DAY_SUCCESS_COUNT end)DAY_SUCCESS_COUNT from (
//				  select r.channel_id,r.merchant_id,l.DAY_SUCCESS_AMT,l.DAY_SUCCESS_COUNT from (
//						select * from PAY_CHANNEL_ROTATION where (channel_id='ZXBJ' or channel_id='JYT') and status='1'
//					) r left join 
//				  (select CHANNEL_ID,channel_acc,DAY_SUCCESS_AMT,DAY_SUCCESS_COUNT from PAY_CHANNEL_ROTATION_LOG where DAY='20170626') l 
//				  on r.CHANNEL_ID = l.CHANNEL_ID and r.MERCHANT_ID=l.channel_acc
//				)tmp where DAY_SUCCESS_AMT<=1000+10 or DAY_SUCCESS_AMT is null order by DAY_SUCCESS_COUNT asc;
		String sql = "select tmp.channel_id,tmp.merchant_id,(case when DAY_SUCCESS_COUNT is null then 0 else DAY_SUCCESS_COUNT end)DAY_SUCCESS_COUNT from (";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = connection();
            String tmp = "";
            for(int i=0; i<pcRotation.length; i++)tmp = tmp + "CHANNEL_ID='"+pcRotation[i]+"' or ";
            sql = sql + "select r.channel_id,r.merchant_id,r.MAX_SUM_AMT,l.DAY_SUCCESS_AMT,l.DAY_SUCCESS_COUNT from (select * from PAY_CHANNEL_ROTATION where ("+ tmp.substring(0,tmp.length()-4) + ") and STATUS='1') r left join "
            		+"(select CHANNEL_ID,channel_acc,DAY_SUCCESS_AMT,DAY_SUCCESS_COUNT from PAY_CHANNEL_ROTATION_LOG where DAY='"+day+"') l on r.CHANNEL_ID = l.CHANNEL_ID and r.MERCHANT_ID=l.channel_acc)tmp ";
            //启用最大额限制
            if("1".equals(PayConstant.PAY_CONFIG.get("CHANNEL_ROTATION_CNL_MAX_AMT_FLAG"))){
            	sql = sql + " where DAY_SUCCESS_AMT+"+txamt+"<=MAX_SUM_AMT or DAY_SUCCESS_AMT is null";
            }
            sql = sql+" order by DAY_SUCCESS_COUNT asc";
            ps = con.prepareStatement(sql);
            log.info(sql);
            rs = ps.executeQuery();
            if(rs.next()){
            	String channelId = rs.getString("channel_id");
            	String merchantId = rs.getString("merchant_id");
            	rs.close();
            	ps.close();
            	sql = "select * from PAY_CHANNEL_ROTATION where CHANNEL_ID='"+channelId+"' and MERCHANT_ID='"+merchantId+"'";
            	log.info(sql);
            	ps = con.prepareStatement(sql);
            	rs = ps.executeQuery();
                if(rs.next())return getPayChannelRotationValue(rs); 
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(rs, ps, con);
        }
        return null;
	}
	public PayChannelRotation getPayChannelRotationByRule1(String day,String [] pcRotation,long txamt) throws Exception {
		String sql = "select tmp.channel_id,tmp.merchant_id,LAST_SUC_USED_TIME from (";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = connection();
            String tmp = "";
            for(int i=0; i<pcRotation.length; i++)tmp = tmp + "CHANNEL_ID='"+pcRotation[i]+"' or ";
            sql = sql + "select r.channel_id,r.merchant_id,r.MAX_SUM_AMT,r.LAST_SUC_USED_TIME,l.DAY_SUCCESS_AMT,l.DAY_SUCCESS_COUNT from (select * from PAY_CHANNEL_ROTATION where ("+ tmp.substring(0,tmp.length()-4) + ") and STATUS='1') r left join "
            		+"(select CHANNEL_ID,channel_acc,DAY_SUCCESS_AMT,DAY_SUCCESS_COUNT from PAY_CHANNEL_ROTATION_LOG where DAY='"+day+"') l on r.CHANNEL_ID = l.CHANNEL_ID and r.MERCHANT_ID=l.channel_acc)tmp ";
            //启用最大额限制
            if("1".equals(PayConstant.PAY_CONFIG.get("CHANNEL_ROTATION_CNL_MAX_AMT_FLAG"))){
            	sql = sql + " where DAY_SUCCESS_AMT+"+txamt+"<=MAX_SUM_AMT or DAY_SUCCESS_AMT is null ";
            }
            sql = sql+" order by LAST_SUC_USED_TIME asc";
            log.info(sql);
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            if(rs.next()){
            	String channelId = rs.getString("channel_id");
            	String merchantId = rs.getString("merchant_id");
            	rs.close();
            	ps.close();
            	ps = con.prepareStatement("select * from PAY_CHANNEL_ROTATION where CHANNEL_ID='"+channelId+"' and MERCHANT_ID='"+merchantId+"'");
            	rs = ps.executeQuery();
                if(rs.next())return getPayChannelRotationValue(rs); 
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(rs, ps, con);
        }
        return null;
	}
	public PayChannelRotation getPayChannelRotationByRule2(String day,String [] pcRotation,long txamt) throws Exception {
		String sql = "select tmp.channel_id,tmp.merchant_id,(case when DAY_SUCCESS_AMT is null then 0 else DAY_SUCCESS_AMT end)DAY_SUCCESS_AMT from (";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = connection();
            String tmp = "";
            for(int i=0; i<pcRotation.length; i++)tmp = tmp + "CHANNEL_ID='"+pcRotation[i]+"' or ";
            sql = sql + "select r.channel_id,r.merchant_id,r.MAX_SUM_AMT,l.DAY_SUCCESS_AMT,l.DAY_SUCCESS_COUNT from (select * from PAY_CHANNEL_ROTATION where ("+ tmp.substring(0,tmp.length()-4) + ") and STATUS='1') r left join "
            		+"(select CHANNEL_ID,channel_acc,DAY_SUCCESS_AMT,DAY_SUCCESS_COUNT from PAY_CHANNEL_ROTATION_LOG where DAY='"+day+"') l on r.CHANNEL_ID = l.CHANNEL_ID and r.MERCHANT_ID=l.channel_acc)tmp ";
            //启用最大额限制
            if("1".equals(PayConstant.PAY_CONFIG.get("CHANNEL_ROTATION_CNL_MAX_AMT_FLAG"))){
            	sql = sql + " where DAY_SUCCESS_AMT+"+txamt+"<=MAX_SUM_AMT or DAY_SUCCESS_AMT is null ";
            }
            sql = sql+" order by DAY_SUCCESS_AMT asc";
            log.info(sql);
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            if(rs.next()){
            	String channelId = rs.getString("channel_id");
            	String merchantId = rs.getString("merchant_id");
            	rs.close();
            	ps.close();
            	ps = con.prepareStatement("select * from PAY_CHANNEL_ROTATION where CHANNEL_ID='"+channelId+"' and MERCHANT_ID='"+merchantId+"'");
            	rs = ps.executeQuery();
                if(rs.next())return getPayChannelRotationValue(rs); 
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(rs, ps, con);
        }
        return null;
	}
	public long getChannelMaxLimitAmt(String channelId) throws Exception {
        String sql = "select MAX_SUM_AMT from PAY_CHANNEL_ROTATION where CHANNEL_ID=?";
        log.info(sql);
        log.info("CHANNEL_ID="+channelId);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            ps.setString(1,channelId);
            rs = ps.executeQuery();
            if(rs.next())return rs.getLong("MAX_SUM_AMT"); 
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(rs, ps, con);
        }
        return 0;
    }
	public PayChannelRotation getPayChannelRotationOrderNo(String payordno) throws Exception {
        String sql = "select r.channel_id,r.merchant_id,r.md5_key from PAY_CHANNEL_ROTATION r,PAY_CHANNEL_ROTATION_ORDER o" +
        		" where r.channel_id=o.channel_id and r.merchant_id=o.channel_acc and o.ORDER_ID=?";
        log.info(sql);
        log.info("ORDER_ID="+payordno);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            ps.setString(1,payordno);
            rs = ps.executeQuery();
            if(rs.next()){
            	PayChannelRotation rotation = new PayChannelRotation();
            	rotation.channelId = rs.getString("channel_id");
            	rotation.merchantId = rs.getString("merchant_id");
            	rotation.md5Key = rs.getString("md5_key");
            	return rotation; 
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(rs, ps, con);
        }
        return null;
    }
	public List getPayChannelRotationList(PayChannelRotation payChannelRotation, long start, long end) throws Exception {
		String sqlCon = setPayChannelRotationSql(payChannelRotation);
		String sql="select * from PAY_CHANNEL_ROTATION where   rownum >"+start+" and rownum<="
				+end+ (sqlCon.length() == 0 ? " " : " and " + sqlCon)+"  order by id desc";
		log.info(sql);
		Connection con=null;
		PreparedStatement ps=null;
		ResultSet rs=null;
		List list = new ArrayList();
		try{
			con=connection();
            ps = con.prepareStatement(sql.toString());
            int n = 1;
            setPayChannelRotationParameter(ps,payChannelRotation,n);
            rs = ps.executeQuery();
            while(rs.next()){
            	list.add(getPayChannelRotationValue(rs));
            }
            return list;
		}catch(Exception e){
			e.printStackTrace();
			throw e;
		}finally{
			close(rs, ps, con);
		}
}
}