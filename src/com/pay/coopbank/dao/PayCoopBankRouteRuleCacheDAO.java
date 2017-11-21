package com.pay.coopbank.dao;

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

import com.PayConstant;
import com.jweb.dao.BaseDAO;
import com.pay.coopbank.service.PayCoopBankService;
/**
 * Table PAY_COOP_BANK_ROUTE_RULE_CACHE DAO. 
 * @author Administrator
 *
 */
public class PayCoopBankRouteRuleCacheDAO  extends BaseDAO {
    private static final Log log = LogFactory.getLog(PayCoopBankRouteRuleCacheDAO.class);
    public static synchronized PayCoopBankRouteRuleCache getPayCoopBankRouteRuleCacheValue(ResultSet rs)throws SQLException {
        PayCoopBankRouteRuleCache payCoopBankRouteRuleCache = new PayCoopBankRouteRuleCache();
        payCoopBankRouteRuleCache.id = rs.getString("ID");
        payCoopBankRouteRuleCache.curDate = rs.getString("CUR_DATE");
        payCoopBankRouteRuleCache.divisionCount = rs.getLong("DIVISION_COUNT");
        payCoopBankRouteRuleCache.channelCode = rs.getString("CHANNEL_CODE");
        payCoopBankRouteRuleCache.tranType = rs.getString("TRAN_TYPE");
        return payCoopBankRouteRuleCache;
    }
    public String addPayCoopBankRouteRuleCache(PayCoopBankRouteRuleCache payCoopBankRouteRuleCache) throws Exception {
        String sql = "insert into PAY_COOP_BANK_ROUTE_RULE_CACHE("+
            "ID," + 
            "CUR_DATE," + 
            "DIVISION_COUNT," + 
            "CHANNEL_CODE," + 
            "TRAN_TYPE)values(?,?,?,?,?)";
        log.info(sql);
        log.info("ID="+payCoopBankRouteRuleCache.id+";cur_DATE="+payCoopBankRouteRuleCache.curDate+";DIVISION_COUNT="+payCoopBankRouteRuleCache.divisionCount+
        		"CHANNEL_CODE="+payCoopBankRouteRuleCache.channelCode+";TRAN_TYPE="+payCoopBankRouteRuleCache.tranType);
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            int n=1;
            ps.setString(n++,payCoopBankRouteRuleCache.id);
            ps.setString(n++,payCoopBankRouteRuleCache.curDate);
            ps.setLong(n++,payCoopBankRouteRuleCache.divisionCount);
            ps.setString(n++,payCoopBankRouteRuleCache.channelCode);
            ps.setString(n++,payCoopBankRouteRuleCache.tranType);
            ps.executeUpdate();
            return "";
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(null, ps, con);
        }
    }
    public List getList() throws Exception{
        String sql = "select * from PAY_COOP_BANK_ROUTE_RULE_CACHE";
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
                list.add(getPayCoopBankRouteRuleCacheValue(rs));
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(rs, ps, con);
        }
    }
	public Map<String, PayCoopBankRouteRuleCache> getRouteRule(String curDate) throws Exception {
		String sql = "select * from PAY_COOP_BANK_ROUTE_RULE_CACHE where CUR_DATE='"+curDate+"'";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Map<String, PayCoopBankRouteRuleCache> map = new HashMap<String, PayCoopBankRouteRuleCache>();
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while(rs.next()){
            	PayCoopBankRouteRuleCache c = getPayCoopBankRouteRuleCacheValue(rs);
            	map.put(c.curDate+","+c.channelCode+","+c.tranType, c);
            }
            return map;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(rs, ps, con);
        }
	}
	public Map<String, PayCoopBankRouteRuleCache> addPayCoopBankRouteRuleCacheBatch(String curDate) throws Exception {
		String sql = "insert into PAY_COOP_BANK_ROUTE_RULE_CACHE("+
	            "ID," + 
	            "CUR_DATE," + 
	            "DIVISION_COUNT," + 
	            "CHANNEL_CODE," + 
	            "TRAN_TYPE)values(?,?,?,?,?)";
	    log.info(sql);
	    Connection con = null;
	    PreparedStatement ps = null;
	    Map<String, PayCoopBankRouteRuleCache> map = new HashMap<String, PayCoopBankRouteRuleCache>();
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            int n=1;
            PayCoopBankRouteRuleCache rr;
            //每种交易类型总数
            for(int j=0; j<PayCoopBankService.tranTypes.length; j++){
				n=1;
				rr = new PayCoopBankRouteRuleCache();
	            rr.id = Tools.getUniqueIdentify();
	            rr.curDate = curDate;
	            rr.divisionCount = 0l;
	            rr.channelCode = "-1";
	            rr.tranType=PayCoopBankService.tranTypes[j];
	            log.info("ID="+rr.id+";cur_DATE="+curDate+";DIVISION_COUNT="+rr.divisionCount+
	            		"CHANNEL_CODE="+rr.channelCode+";TRAN_TYPE="+rr.tranType);
	            ps.setString(n++,rr.id);
	            ps.setString(n++,curDate);
	            ps.setLong(n++,rr.divisionCount);
	            ps.setString(n++,rr.channelCode);
	            ps.setString(n++,rr.tranType);
	            ps.addBatch();
	            map.put(curDate+","+rr.channelCode+","+rr.tranType, rr);
			}
            //每种交易类型每个渠道笔数
            for(int i=0; i<PayCoopBankService.CHANNEL_LIST_ALL.size(); i++){
    			PayCoopBank channel = PayCoopBankService.CHANNEL_LIST_ALL.get(i);
    			for(int j=0; j<PayCoopBankService.tranTypes.length; j++){
    				n=1;
    				rr = new PayCoopBankRouteRuleCache();
    	            rr.id = Tools.getUniqueIdentify();
    	            rr.curDate = curDate;
    	            rr.divisionCount = 0l;
    	            rr.channelCode = channel.bankCode;
    	            rr.tranType=PayCoopBankService.tranTypes[j];
    	            ps.setString(n++,rr.id);
    	            ps.setString(n++,curDate);
    	            ps.setLong(n++,rr.divisionCount);
    	            ps.setString(n++,rr.channelCode);
    	            ps.setString(n++,rr.tranType);
    	            ps.addBatch();
    	            map.put(curDate+","+rr.channelCode+","+rr.tranType, rr);
    			}
            }
            ps.executeBatch();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }finally {
            close(null, ps, con);
        }
        return map;
	}
	public void updateCache(PayCoopBankRouteRuleCache minCache) throws Exception {
		String sql = "update PAY_COOP_BANK_ROUTE_RULE_CACHE set DIVISION_COUNT=DIVISION_COUNT+1 " +
				"where CUR_DATE=? and TRAN_TYPE=? and (CHANNEL_CODE=? or CHANNEL_CODE='-1')";
	        log.info(sql);
	        log.info("CUR_DATE="+minCache.curDate+";TRAN_TYPE="+minCache.tranType+";CHANNEL_CODE="+minCache.channelCode);
	        Connection con = null;
	        PreparedStatement ps = null;
	        try {
	            con = connection();
	            ps = con.prepareStatement(sql);
	            int n=1;
	            ps.setString(n++,minCache.curDate);
	            ps.setString(n++,minCache.tranType);
	            ps.setString(n++,minCache.channelCode);
	            ps.executeUpdate();
	        } catch (Exception e) {
	            e.printStackTrace();
	            throw e;
	        } finally {
	            close(null, ps, con);
	        }
	}
}