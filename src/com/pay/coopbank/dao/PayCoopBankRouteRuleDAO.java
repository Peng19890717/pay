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

import com.jweb.dao.BaseDAO;
import com.pay.coopbank.service.PayCoopBankService;
/**
 * Table PAY_COOP_BANK_ROUTE_RULE DAO. 
 * @author Administrator
 *
 */
public class PayCoopBankRouteRuleDAO  extends BaseDAO {
    private static final Log log = LogFactory.getLog(PayCoopBankRouteRuleDAO.class);
    public static synchronized PayCoopBankRouteRule getPayCoopBankRouteRuleValue(ResultSet rs)throws SQLException {
        PayCoopBankRouteRule payCoopBankRouteRule = new PayCoopBankRouteRule();
        payCoopBankRouteRule.id = rs.getString("ID");
        payCoopBankRouteRule.ruleType = rs.getString("RULE_TYPE");
        payCoopBankRouteRule.divisionCount = rs.getLong("DIVISION_COUNT");
        payCoopBankRouteRule.divisionMaxAmt = rs.getLong("DIVISION_MAX_AMT");
        payCoopBankRouteRule.channelCode = rs.getString("CHANNEL_CODE");
        payCoopBankRouteRule.tranType = rs.getString("TRAN_TYPE");
        payCoopBankRouteRule.weight = rs.getLong("WEIGHT");
        payCoopBankRouteRule.remark = rs.getString("REMARK");
        payCoopBankRouteRule.createId = rs.getString("CREATE_ID");
        payCoopBankRouteRule.createTime = rs.getTimestamp("CREATE_TIME");
        return payCoopBankRouteRule;
    }
    /**
     * 添加交易平均分配规则
     * @param payCoopBankRouteRule
     * @return
     * @throws Exception
     */
    public String addPayCoopBankRouteRuleTypeTrade(PayCoopBankRouteRule payCoopBankRouteRule) throws Exception {
        String sql = "insert into PAY_COOP_BANK_ROUTE_RULE("+
            "ID," + 
            "RULE_TYPE," + 
            "DIVISION_COUNT," + 
            "DIVISION_MAX_AMT," + 
            "TRAN_TYPE," + 
            "CREATE_ID)values(?,?,?,?,?,?)";
        log.info(sql);
        log.info("ID="+payCoopBankRouteRule.id+";RULE_TYPE="+payCoopBankRouteRule.ruleType+";DIVISION_COUNT="+payCoopBankRouteRule.divisionCount+
        		";DIVISION_MAX_AMT="+payCoopBankRouteRule.divisionMaxAmt+";TRAN_TYPE="+payCoopBankRouteRule.tranType+
        		";CREATE_ID="+payCoopBankRouteRule.createId);
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            int n=1;
            ps.setString(n++,payCoopBankRouteRule.id);
            ps.setString(n++,payCoopBankRouteRule.ruleType);
            ps.setLong(n++,payCoopBankRouteRule.divisionCount);
            ps.setLong(n++,payCoopBankRouteRule.divisionMaxAmt);
            ps.setString(n++,payCoopBankRouteRule.tranType);
            ps.setString(n++,payCoopBankRouteRule.createId);
            ps.executeUpdate();
            return "";
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }finally {
            close(null, ps, con);
        }
    }
    /**
     * 添加优先值规则
     * @param payCoopBankRouteRule
     * @return
     * @throws Exception
     */
    public String addPayCoopBankRouteRuleTypePrior(PayCoopBankRouteRule payCoopBankRouteRule) throws Exception {
        String sql = "insert into PAY_COOP_BANK_ROUTE_RULE("+
            "ID," + 
            "RULE_TYPE," + 
            "CHANNEL_CODE," + 
            "TRAN_TYPE," + 
            "WEIGHT," + 
            "CREATE_ID," + 
            "CREATE_TIME)values(?,?,?,?,?,?,to_date(?,'yyyy-mm-dd hh24:mi:ss'))";
        log.info(sql);
        log.info("ID="+payCoopBankRouteRule.id+";RULE_TYPE=1;CHANNEL_CODE="+payCoopBankRouteRule.channelCode+
        		";TRAN_TYPE="+payCoopBankRouteRule.tranType+";WEIGHT="+payCoopBankRouteRule.weight+";CREATE_ID="+payCoopBankRouteRule.createId);
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            int n=1;
            ps.setString(n++,payCoopBankRouteRule.id);
            ps.setString(n++,"1");
            ps.setString(n++,payCoopBankRouteRule.channelCode);
            ps.setString(n++,payCoopBankRouteRule.tranType);
            ps.setLong(n++,payCoopBankRouteRule.weight);
            ps.setString(n++,payCoopBankRouteRule.createId);
            ps.setString(n++,new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payCoopBankRouteRule.createTime));
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
        String sql = "select * from PAY_COOP_BANK_ROUTE_RULE";
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
                list.add(getPayCoopBankRouteRuleValue(rs));
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
     * @param payCoopBankRouteRule
     * @return
     */
    private String setPayCoopBankRouteRuleSql(PayCoopBankRouteRule payCoopBankRouteRule) {
        StringBuffer sql = new StringBuffer();
        
        if(payCoopBankRouteRule.id != null && payCoopBankRouteRule.id.length() !=0) {
            sql.append(" ID = ? and ");
        }
        String tmp = sql.toString();
        if(tmp.length()>0)tmp=tmp.substring(0,tmp.lastIndexOf(" and "));
        return tmp;
    }
    /**
     * Set query parameter.
     * @param ps
     * @param payCoopBankRouteRule
     * @param n
     * @return
     * @throws SQLException
     */
    private int setPayCoopBankRouteRuleParameter(PreparedStatement ps,PayCoopBankRouteRule payCoopBankRouteRule,int n)throws SQLException {
        if(payCoopBankRouteRule.id != null && payCoopBankRouteRule.id.length() !=0) {
            ps.setString(n++,payCoopBankRouteRule.id);
        }
        return n;
    }
    /**
     * Get records count.
     * @param payCoopBankRouteRule
     * @return
     */
    public int getPayCoopBankRouteRuleCount(PayCoopBankRouteRule payCoopBankRouteRule) {
        String sqlCon = setPayCoopBankRouteRuleSql(payCoopBankRouteRule);
        String sql = "select count(rownum) recordCount from PAY_COOP_BANK_ROUTE_RULE " +(sqlCon.length()==0?"":" where "+ sqlCon);
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            int n = 1;
            setPayCoopBankRouteRuleParameter(ps,payCoopBankRouteRule,n);
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
     * @param payCoopBankRouteRule
     * @param page
     * @param rows
     * @param sort
     * @param order
     * @return
     * @throws Exception
     */
    public List getPayCoopBankRouteRuleList(PayCoopBankRouteRule payCoopBankRouteRule,int page,int rows,String sort,String order) throws Exception{
        sort = Tools.paraNameToDBColumnName(sort);
        String sqlCon = setPayCoopBankRouteRuleSql(payCoopBankRouteRule);
        String sortOrder = sort == null || sort.length()==0?" order by ID desc":(" order by " + sort +("desc".equals(order)?" desc ":" asc "));
        String sql = "select * from (" +
                "  select rownum rowno,tmp1.* from (" +
                "   select tmp.*  from PAY_COOP_BANK_ROUTE_RULE tmp " +(sqlCon.length()==0?"":" where "+ sqlCon) +sortOrder+
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
            setPayCoopBankRouteRuleParameter(ps,payCoopBankRouteRule,n);
            rs = ps.executeQuery();
            while(rs.next()){
                list.add(getPayCoopBankRouteRuleValue(rs));
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
     * remove PayCoopBankRouteRule
     * @param id
     * @throws Exception     
     */
    public void removePayCoopBankRouteRule(String id) throws Exception {
        String sql = "delete from PAY_COOP_BANK_ROUTE_RULE where ID=?";
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
     * removeAll
     * @throws Exception
     */
    public void removePayCoopBankRouteRuleAll() throws Exception {
        String sql = "delete from PAY_COOP_BANK_ROUTE_RULE ";
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
    /**
     * detail PayCoopBankRouteRule
     * @param id
     * @return PayCoopBankRouteRule
     * @throws Exception
     */
    public PayCoopBankRouteRule detailPayCoopBankRouteRule(String id) throws Exception {
        String sql = "select * from PAY_COOP_BANK_ROUTE_RULE where ID=?";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            ps.setString(1,id);
            rs = ps.executeQuery();
            if(rs.next())return getPayCoopBankRouteRuleValue(rs); 
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(rs, ps, con);
        }
        return null;
    }
    /**
     * update PayCoopBankRouteRule
     * @param payCoopBankRouteRule
     * @throws Exception
     */
    public void updatePayCoopBankRouteRule(PayCoopBankRouteRule payCoopBankRouteRule) throws Exception {
        String sqlTmp = "";
        if(payCoopBankRouteRule.id != null)sqlTmp = sqlTmp + " ID=?,";
        if(payCoopBankRouteRule.ruleType != null)sqlTmp = sqlTmp + " RULE_TYPE=?,";
        if(payCoopBankRouteRule.divisionCount != null)sqlTmp = sqlTmp + " DIVISION_COUNT=?,";
        if(payCoopBankRouteRule.divisionMaxAmt != null)sqlTmp = sqlTmp + " DIVISION_MAX_AMT=?,";
        if(payCoopBankRouteRule.channelCode != null)sqlTmp = sqlTmp + " CHANNEL_CODE=?,";
        if(payCoopBankRouteRule.tranType != null)sqlTmp = sqlTmp + " TRAN_TYPE=?,";
        if(payCoopBankRouteRule.weight != null)sqlTmp = sqlTmp + " WEIGHT=?,";
        if(payCoopBankRouteRule.remark != null)sqlTmp = sqlTmp + " REMARK=?,";
        if(payCoopBankRouteRule.createId != null)sqlTmp = sqlTmp + " CREATE_ID=?,";
        if(payCoopBankRouteRule.createTime != null)sqlTmp = sqlTmp + " CREATE_TIME=to_date(?,'yyyy-mm-dd hh24:mi:ss'),";
        if(sqlTmp.length()==0)return;
        String sql = "update PAY_COOP_BANK_ROUTE_RULE "+        
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
            if(payCoopBankRouteRule.id != null)ps.setString(n++,payCoopBankRouteRule.id);
            if(payCoopBankRouteRule.ruleType != null)ps.setString(n++,payCoopBankRouteRule.ruleType);
            if(payCoopBankRouteRule.divisionCount != null)ps.setLong(n++,payCoopBankRouteRule.divisionCount);
            if(payCoopBankRouteRule.divisionMaxAmt != null)ps.setLong(n++,payCoopBankRouteRule.divisionMaxAmt);
            if(payCoopBankRouteRule.channelCode != null)ps.setString(n++,payCoopBankRouteRule.channelCode);
            if(payCoopBankRouteRule.tranType != null)ps.setString(n++,payCoopBankRouteRule.tranType);
            if(payCoopBankRouteRule.weight != null)ps.setLong(n++,payCoopBankRouteRule.weight);
            if(payCoopBankRouteRule.remark != null)ps.setString(n++,payCoopBankRouteRule.remark);
            if(payCoopBankRouteRule.createId != null)ps.setString(n++,payCoopBankRouteRule.createId);
            if(payCoopBankRouteRule.createTime != null)ps.setString(n++,new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payCoopBankRouteRule.createTime));
            ps.setString(n++,payCoopBankRouteRule.id);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }finally {
            close(null, ps, con);
        }
    }
	public Map<String, PayCoopBankRouteRule> getRouteRule() throws Exception {
        String sql = "select * from PAY_COOP_BANK_ROUTE_RULE";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Map<String, PayCoopBankRouteRule> map = new HashMap<String, PayCoopBankRouteRule>();
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while(rs.next()){
            	PayCoopBankRouteRule rule = getPayCoopBankRouteRuleValue(rs);
            	map.put(rule.ruleType+","+
            			(rule.channelCode==null||rule.channelCode.length()==0?"-1":rule.channelCode)+","+rule.tranType, rule);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(rs, ps, con);
        }
        return map;
    }
	public Map<String, PayCoopBankRouteRule> addPayCoopBankRouteRuleBatch() throws Exception {
		String search = "select channel_code from PAY_COOP_BANK_ROUTE_RULE group by channel_code";
		String sql = "insert into PAY_COOP_BANK_ROUTE_RULE("+
	            "ID," + 
	            "RULE_TYPE," + 
	            "DIVISION_COUNT," + 
	            "DIVISION_MAX_AMT," + 
	            "CHANNEL_CODE," + 
	            "TRAN_TYPE," + 
	            "WEIGHT," + 
	            "REMARK," + 
	            "CREATE_ID," + 
	            "CREATE_TIME)values(?,?,0,0,?,?,0,'','system',sysdate)";
	    log.info(sql);
	    Connection con = null;
	    PreparedStatement ps = null;
	    ResultSet rs = null;
	    Map <String,String> cur= new HashMap<String,String>();
	    Map<String, PayCoopBankRouteRule> map = new HashMap<String, PayCoopBankRouteRule>();
        try {
            con = connection();            
            ps = con.prepareStatement(search);
            rs = ps.executeQuery();
            while(rs.next()){
            	String channelCode = rs.getString("channel_code");
            	cur.put(channelCode==null?"":channelCode,"");
            }
            ps.close();
            ps = con.prepareStatement(sql);
            int n=1;
            PayCoopBankRouteRule rr;
            //每种交易类型总数
            if(cur.get("")==null){
	            for(int j=0; j<PayCoopBankService.tranTypes.length; j++){
					n=1;
					rr = new PayCoopBankRouteRule();
		            rr.id = Tools.getUniqueIdentify();
		            rr.ruleType = "0";
		            rr.divisionCount = 0l;
		            rr.divisionMaxAmt = 0l;
		            rr.channelCode = "";
		            rr.tranType=PayCoopBankService.tranTypes[j];
		            rr.weight = 0l;
		            ps.setString(n++,rr.id);
		            ps.setString(n++,rr.ruleType);
		            ps.setString(n++,rr.channelCode);
		            ps.setString(n++,rr.tranType);
		            ps.addBatch();
		            map.put(rr.ruleType+",-1,"+rr.tranType, rr);
				}
            }
            //每种交易类型每个渠道笔数
            for(int i=0; i<PayCoopBankService.CHANNEL_LIST_ALL.size(); i++){
    			PayCoopBank channel = PayCoopBankService.CHANNEL_LIST_ALL.get(i);
    			for(int j=0; j<PayCoopBankService.tranTypes.length; j++){
    				if(cur.get(channel.bankCode)!=null)continue;
    				n=1;
    				rr = new PayCoopBankRouteRule();
    	            rr.id = Tools.getUniqueIdentify();
    	            rr.ruleType = "1";
    	            rr.divisionCount = 0l;
    	            rr.divisionMaxAmt = 0l;
    	            rr.channelCode = channel.bankCode;
    	            rr.tranType=PayCoopBankService.tranTypes[j];
    	            rr.weight = 0l;
    	            ps.setString(n++,rr.id);
    	            ps.setString(n++,rr.ruleType);
    	            ps.setString(n++,rr.channelCode);
    	            ps.setString(n++,rr.tranType);
    	            ps.addBatch();
    	            map.put(rr.ruleType+","+rr.channelCode+","+rr.tranType, rr);
    			}
            }
            ps.executeBatch();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }finally {
            close(rs, ps, con);
        }
        return map;
	
	}

}