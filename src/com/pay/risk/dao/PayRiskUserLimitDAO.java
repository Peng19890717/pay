package com.pay.risk.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import util.Tools;

import com.jweb.dao.BaseDAO;
/**
 * Table PAY_RISK_USER_LIMIT DAO. 
 * @author Administrator
 *
 */
public class PayRiskUserLimitDAO  extends BaseDAO {
    private static final Log log = LogFactory.getLog(PayRiskUserLimitDAO.class);
    public static synchronized PayRiskUserLimit getPayRiskUserLimitValue(ResultSet rs)throws SQLException {
        PayRiskUserLimit payRiskUserLimit = new PayRiskUserLimit();
        payRiskUserLimit.id = rs.getString("ID");
        payRiskUserLimit.userType = rs.getString("USER_TYPE");
        payRiskUserLimit.xListType = rs.getString("X_LIST_TYPE");
        payRiskUserLimit.tranType = rs.getString("TRAN_TYPE");
        payRiskUserLimit.userCode = rs.getString("USER_CODE");
        payRiskUserLimit.busCode = rs.getString("BUS_CODE");
        payRiskUserLimit.startDate = rs.getTimestamp("START_DATE");
        payRiskUserLimit.endDate = rs.getTimestamp("END_DATE");
        payRiskUserLimit.isUse = rs.getString("IS_USE");
        payRiskUserLimit.maxCardNum = rs.getLong("MAX_CARD_NUM");
        payRiskUserLimit.crebitCardOnceAmt = rs.getLong("CREBIT_CARD_ONCE_AMT");
        payRiskUserLimit.crebitCardDayAmt = rs.getLong("CREBIT_CARD_DAY_AMT");
        payRiskUserLimit.crebitCardTxnInterval = rs.getLong("CREBIT_CARD_TXN_INTERVAL");
        payRiskUserLimit.debitCardOnceAmt = rs.getLong("DEBIT_CARD_ONCE_AMT");
        payRiskUserLimit.debitCardDayAmt = rs.getLong("DEBIT_CARD_DAY_AMT");
        payRiskUserLimit.debitCardTxnInterval = rs.getLong("DEBIT_CARD_TXN_INTERVAL");
        payRiskUserLimit.remark = rs.getString("REMARK");
        payRiskUserLimit.createUserId = rs.getString("CREATE_USER_ID");
        payRiskUserLimit.createTime = rs.getTimestamp("CREATE_TIME");
        payRiskUserLimit.updateUserId = rs.getString("UPDATE_USER_ID");
        payRiskUserLimit.updateTime = rs.getTimestamp("UPDATE_TIME");
        payRiskUserLimit.limitTimeFlag = rs.getString("LIMIT_TIME_FLAG");
        return payRiskUserLimit;
    }
    public String addPayRiskUserLimit(PayRiskUserLimit payRiskUserLimit) throws Exception {
        String sql = "insert into PAY_RISK_USER_LIMIT("+
            "ID," + 
            "USER_TYPE," + 
            "X_LIST_TYPE," + 
            "TRAN_TYPE," + 
            "USER_CODE," + 
            "BUS_CODE," + 
            "START_DATE," + 
            "END_DATE," + 
            "IS_USE," + 
            "MAX_CARD_NUM," + 
            "CREBIT_CARD_ONCE_AMT," + 
            "CREBIT_CARD_DAY_AMT," + 
            "CREBIT_CARD_TXN_INTERVAL," + 
            "DEBIT_CARD_ONCE_AMT," + 
            "DEBIT_CARD_DAY_AMT," + 
            "DEBIT_CARD_TXN_INTERVAL," + 
            "REMARK," + 
            "CREATE_USER_ID," + 
            "UPDATE_USER_ID," +
            "LIMIT_TIME_FLAG)values(?,?,?,?,?,?,to_date(?,'yyyy-mm-dd hh24:mi:ss'),to_date(?,'yyyy-mm-dd hh24:mi:ss'),?,?,?,?,?,?,?,?,?,?,?,?)";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            int n=1;
            ps.setString(n++,payRiskUserLimit.id);
            ps.setString(n++,payRiskUserLimit.userType);
            ps.setString(n++,payRiskUserLimit.xListType == null?"":payRiskUserLimit.xListType);
            ps.setString(n++,payRiskUserLimit.tranType);
            ps.setString(n++,payRiskUserLimit.userCode == null?"":payRiskUserLimit.userCode);
            ps.setString(n++,payRiskUserLimit.busCode==null?"":payRiskUserLimit.busCode);
            payRiskUserLimit.startDate = payRiskUserLimit.startDate == null?new Date():payRiskUserLimit.startDate;
            ps.setString(n++,new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payRiskUserLimit.startDate));
            payRiskUserLimit.endDate = payRiskUserLimit.endDate == null?new Date():payRiskUserLimit.endDate;
            ps.setString(n++,new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payRiskUserLimit.endDate));
            ps.setString(n++,payRiskUserLimit.isUse);
            ps.setLong(n++,payRiskUserLimit.maxCardNum);
            ps.setLong(n++,payRiskUserLimit.crebitCardOnceAmt);
            ps.setLong(n++,payRiskUserLimit.crebitCardDayAmt);
            ps.setLong(n++,payRiskUserLimit.crebitCardTxnInterval);
            ps.setLong(n++,payRiskUserLimit.debitCardOnceAmt);
            ps.setLong(n++,payRiskUserLimit.debitCardDayAmt);
            ps.setLong(n++,payRiskUserLimit.debitCardTxnInterval);
            ps.setString(n++,payRiskUserLimit.remark);
            ps.setString(n++,payRiskUserLimit.createUserId);
            ps.setString(n++,payRiskUserLimit.updateUserId);
            ps.setString(n++,payRiskUserLimit.limitTimeFlag);
            ps.executeUpdate();
            if(payRiskUserLimit.subLimitList.size()>0){
            	ps.close();
            	sql = "insert into PAY_RISK_USER_LIMIT_SUB("+
                        "ID," + 
                        "LIMIT_BUS_CLIENT," + 
                        "MIN_AMT," + 
                        "MAX_AMT," + 
                        "DAY_TIMES," + 
                        "DAY_SUC_TIMES," + 
                        "DAY_AMT," + 
                        "DAY_SUC_AMT," + 
                        "MONTH_TIMES," + 
                        "MONTH_SUC_TIMES," + 
                        "MONTH_AMT," + 
                        "MONTH_SUC_AMT," + 
                        "RISK_USER_LIMIT_ID)values(?,?,?,?,?,?,?,?,?,?,?,?,?)";
            	ps = con.prepareStatement(sql);
            	for(int i=0; i<payRiskUserLimit.subLimitList.size(); i++){
                    n=1;
                    PayRiskUserLimitSub payRiskUserLimitSub = payRiskUserLimit.subLimitList.get(i);
                    ps.setString(n++,payRiskUserLimitSub.id);
                    ps.setString(n++,payRiskUserLimitSub.limitBusClient);
                    ps.setLong(n++,payRiskUserLimitSub.minAmt);
                    ps.setLong(n++,payRiskUserLimitSub.maxAmt);
                    ps.setLong(n++,payRiskUserLimitSub.dayTimes);
                    ps.setLong(n++,payRiskUserLimitSub.daySucTimes);
                    ps.setLong(n++,payRiskUserLimitSub.dayAmt);
                    ps.setLong(n++,payRiskUserLimitSub.daySucAmt);
                    ps.setLong(n++,payRiskUserLimitSub.monthTimes);
                    ps.setLong(n++,payRiskUserLimitSub.monthSucTimes);
                    ps.setLong(n++,payRiskUserLimitSub.monthAmt);
                    ps.setLong(n++,payRiskUserLimitSub.monthSucAmt);
                    ps.setString(n++,payRiskUserLimitSub.riskUserLimitId);
                    ps.addBatch();
            	}
            	ps.executeBatch();
            }
            return "";
        } catch (Exception e) {
            e.printStackTrace();
            if(e.getMessage().indexOf("ORA-00001")!=-1)throw new Exception("此限额类型已添加");
            else throw e;
        }finally {
            close(null, ps, con);
        }
    }
    public List<PayRiskUserLimit> getList() throws Exception{
        String sql = "select * from PAY_RISK_USER_LIMIT where IS_USE='0'";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List <PayRiskUserLimit>list = new ArrayList<PayRiskUserLimit>();
        Map<String,PayRiskUserLimit> map = new HashMap<String,PayRiskUserLimit>();
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while(rs.next()){
            	PayRiskUserLimit lim = getPayRiskUserLimitValue(rs); 
                list.add(lim);
                map.put(lim.id,lim);
            }
            rs.close();
            ps.close();
            ps = con.prepareStatement("select ls.* from PAY_RISK_USER_LIMIT_SUB ls left join (select * from PAY_RISK_USER_LIMIT where IS_USE='0') l on ls.risk_user_limit_id=l.id");
        	rs = ps.executeQuery();
            while (rs.next()) {
            	PayRiskUserLimitSub limSub = PayRiskUserLimitSubDAO.getPayRiskUserLimitSubValue(rs);
            	PayRiskUserLimit lim = map.get(limSub.riskUserLimitId);
            	if(lim!=null)lim.subLimitList.add(limSub);
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
     * @param payRiskUserLimit
     * @return
     */
    private String setPayRiskUserLimitSql(PayRiskUserLimit payRiskUserLimit) {
        StringBuffer sql = new StringBuffer();
        
        if(payRiskUserLimit.userType != null && payRiskUserLimit.userType.length() !=0) {
            sql.append(" USER_TYPE = ? and ");
        }
        if(payRiskUserLimit.xListType != null && payRiskUserLimit.xListType.length() !=0) {
            sql.append(" X_LIST_TYPE = ? and ");
        }
        if(payRiskUserLimit.tranType != null && payRiskUserLimit.tranType.length() !=0) {
            sql.append(" TRAN_TYPE = ? and ");
        }
        if(payRiskUserLimit.userCode != null && payRiskUserLimit.userCode.length() !=0) {
            sql.append(" USER_CODE = ? and ");
        }
        String tmp = sql.toString();
        if(tmp.length()>0)tmp=tmp.substring(0,tmp.lastIndexOf(" and "));
        return tmp;
    }
    /**
     * Set query parameter.
     * @param ps
     * @param payRiskUserLimit
     * @param n
     * @return
     * @throws SQLException
     */
    private int setPayRiskUserLimitParameter(PreparedStatement ps,PayRiskUserLimit payRiskUserLimit,int n)throws SQLException {
        if(payRiskUserLimit.userType != null && payRiskUserLimit.userType.length() !=0) {
            ps.setString(n++,payRiskUserLimit.userType);
        }
        if(payRiskUserLimit.xListType != null && payRiskUserLimit.xListType.length() !=0) {
            ps.setString(n++,payRiskUserLimit.xListType);
        }
        if(payRiskUserLimit.tranType != null && payRiskUserLimit.tranType.length() !=0) {
            ps.setString(n++,payRiskUserLimit.tranType);
        }
        if(payRiskUserLimit.userCode != null && payRiskUserLimit.userCode.length() !=0) {
            ps.setString(n++,payRiskUserLimit.userCode);
        }
        return n;
    }
    /**
     * Get records count.
     * @param payRiskUserLimit
     * @return
     */
    public int getPayRiskUserLimitCount(PayRiskUserLimit payRiskUserLimit) {
        String sqlCon = setPayRiskUserLimitSql(payRiskUserLimit);
        String sql = "select count(rownum) recordCount from PAY_RISK_USER_LIMIT " +(sqlCon.length()==0?"":" where "+ sqlCon);
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            int n = 1;
            setPayRiskUserLimitParameter(ps,payRiskUserLimit,n);
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
     * @param payRiskUserLimit
     * @param page
     * @param rows
     * @param sort
     * @param order
     * @return
     * @throws Exception
     */
    public List getPayRiskUserLimitList(PayRiskUserLimit payRiskUserLimit,int page,int rows,String sort,String order) throws Exception{
        sort = Tools.paraNameToDBColumnName(sort);
        String sqlCon = setPayRiskUserLimitSql(payRiskUserLimit);
        String sortOrder = sort == null || sort.length()==0?" order by CREATE_TIME desc":(" order by " + sort +("desc".equals(order)?" desc ":" asc "));
        String sql = "select * from (" +
                "  select rownum rowno,tmp1.* from (" +
                "   select tmp.*  from PAY_RISK_USER_LIMIT tmp " +(sqlCon.length()==0?"":" where "+ sqlCon) +sortOrder+
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
            setPayRiskUserLimitParameter(ps,payRiskUserLimit,n);
            rs = ps.executeQuery();
            while(rs.next()){
                list.add(getPayRiskUserLimitValue(rs));
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
     * remove PayRiskUserLimit
     * @param id
     * @throws Exception     
     */
    public void removePayRiskUserLimit(String id) throws Exception {
        String sql = "delete from PAY_RISK_USER_LIMIT where ID=?";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            ps.setString(1,id);
            if(ps.executeUpdate()>0){
            	ps.close();
            	sql = "delete from PAY_RISK_USER_LIMIT_SUB where RISK_USER_LIMIT_ID=?";
            	ps = con.prepareStatement(sql);
            	ps.setString(1,id);
            	ps.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(null, ps, con);
        }
    }
    /**
     * detail PayRiskUserLimit
     * @param id
     * @return PayRiskUserLimit
     * @throws Exception
     */
    public PayRiskUserLimit detailPayRiskUserLimit(String id) throws Exception {
        String sql = "select * from PAY_RISK_USER_LIMIT where ID=?";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            ps.setString(1,id);
            rs = ps.executeQuery();
            if(rs.next()){
            	PayRiskUserLimit tmp = getPayRiskUserLimitValue(rs);
            	rs.close();
            	ps.close();
            	ps = con.prepareStatement("select * from PAY_RISK_USER_LIMIT_SUB where RISK_USER_LIMIT_ID=?");
            	ps.setString(1,id);
            	rs = ps.executeQuery();
                while (rs.next()) tmp.subLimitList.add(PayRiskUserLimitSubDAO.getPayRiskUserLimitSubValue(rs));
            	return tmp;
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(rs, ps, con);
        }
        return null;
    }
    public String updatePayRiskUserLimit(PayRiskUserLimit payRiskUserLimit) throws Exception {
        String sql = "delete from PAY_RISK_USER_LIMIT_SUB where RISK_USER_LIMIT_ID=?";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            int n=1;
            ps.setString(n++,payRiskUserLimit.id);
            ps.executeUpdate();
            if(payRiskUserLimit.subLimitList.size()>0){
            	ps.close();
            	sql = "update PAY_RISK_USER_LIMIT set " +
            			"IS_USE=?,remark = ?," +
            			("1".equals(payRiskUserLimit.limitTimeFlag)?"START_DATE = to_date(?,'yyyy-mm-dd hh24:mi:ss'),":"") +
            			("1".equals(payRiskUserLimit.limitTimeFlag)?"END_DATE = to_date(?,'yyyy-mm-dd hh24:mi:ss'),":"") +
            			"LIMIT_TIME_FLAG = ? where id = ?";
            	ps = con.prepareStatement(sql);
            	n=1;
            	ps.setString(n++,payRiskUserLimit.isUse);
            	ps.setString(n++,payRiskUserLimit.remark);
            	if("1".equals(payRiskUserLimit.limitTimeFlag)){
	            	ps.setString(n++,new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payRiskUserLimit.startDate));
	            	ps.setString(n++,new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payRiskUserLimit.endDate));
            	}
            	ps.setString(n++,payRiskUserLimit.limitTimeFlag);
            	ps.setString(n++,payRiskUserLimit.id);
            	ps.executeUpdate();
            	ps.close();
            	sql = "insert into PAY_RISK_USER_LIMIT_SUB("+
                        "ID," + 
                        "LIMIT_BUS_CLIENT," + 
                        "MIN_AMT," + 
                        "MAX_AMT," + 
                        "DAY_TIMES," + 
                        "DAY_SUC_TIMES," + 
                        "DAY_AMT," + 
                        "DAY_SUC_AMT," + 
                        "MONTH_TIMES," + 
                        "MONTH_SUC_TIMES," + 
                        "MONTH_AMT," + 
                        "MONTH_SUC_AMT," + 
                        "RISK_USER_LIMIT_ID)values(?,?,?,?,?,?,?,?,?,?,?,?,?)";
            	ps = con.prepareStatement(sql);
            	for(int i=0; i<payRiskUserLimit.subLimitList.size(); i++){
                    n=1;
                    PayRiskUserLimitSub payRiskUserLimitSub = payRiskUserLimit.subLimitList.get(i);
                    ps.setString(n++,payRiskUserLimitSub.id);
                    ps.setString(n++,payRiskUserLimitSub.limitBusClient);
                    ps.setLong(n++,payRiskUserLimitSub.minAmt);
                    ps.setLong(n++,payRiskUserLimitSub.maxAmt);
                    ps.setLong(n++,payRiskUserLimitSub.dayTimes);
                    ps.setLong(n++,payRiskUserLimitSub.daySucTimes);
                    ps.setLong(n++,payRiskUserLimitSub.dayAmt);
                    ps.setLong(n++,payRiskUserLimitSub.daySucAmt);
                    ps.setLong(n++,payRiskUserLimitSub.monthTimes);
                    ps.setLong(n++,payRiskUserLimitSub.monthSucTimes);
                    ps.setLong(n++,payRiskUserLimitSub.monthAmt);
                    ps.setLong(n++,payRiskUserLimitSub.monthSucAmt);
                    ps.setString(n++,payRiskUserLimitSub.riskUserLimitId);
                    ps.addBatch();
            	}
            	ps.executeBatch();
            }
            return "";
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }finally {
            close(null, ps, con);
        }
    }
	public void updatePayRiskUserLimitForCard(PayRiskUserLimit payRiskUserLimit) throws Exception {
        String sql = "update PAY_RISK_USER_LIMIT set "+
                "CREBIT_CARD_ONCE_AMT=?," + 
                "CREBIT_CARD_DAY_AMT=?," + 
                "CREBIT_CARD_TXN_INTERVAL=?," + 
                "DEBIT_CARD_ONCE_AMT=?," + 
                "DEBIT_CARD_DAY_AMT=?," + 
                "DEBIT_CARD_TXN_INTERVAL=?," + 
                "REMARK=?," + 
                "UPDATE_USER_ID=?," +
                ("1".equals(payRiskUserLimit.limitTimeFlag)?"START_DATE=to_date(?,'yyyy-mm-dd hh24:mi:ss'),":"") +
                ("1".equals(payRiskUserLimit.limitTimeFlag)?"END_DATE=to_date(?,'yyyy-mm-dd hh24:mi:ss'),":"") +
                "LIMIT_TIME_FLAG=?," +
                "UPDATE_TIME=to_date(?,'yyyy-mm-dd hh24:mi:ss') where ID=?";
            log.info(sql);
            Connection con = null;
            PreparedStatement ps = null;
            try {
                con = connection();
                ps = con.prepareStatement(sql);
                int n=1;
                ps.setLong(n++,payRiskUserLimit.crebitCardOnceAmt);
                ps.setLong(n++,payRiskUserLimit.crebitCardDayAmt);
                ps.setLong(n++,payRiskUserLimit.crebitCardTxnInterval);
                ps.setLong(n++,payRiskUserLimit.debitCardOnceAmt);
                ps.setLong(n++,payRiskUserLimit.debitCardDayAmt);
                ps.setLong(n++,payRiskUserLimit.debitCardTxnInterval);
                ps.setString(n++,payRiskUserLimit.remark);
                ps.setString(n++,payRiskUserLimit.updateUserId);
                if("1".equals(payRiskUserLimit.limitTimeFlag)){
                	ps.setString(n++,new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payRiskUserLimit.startDate));
                	ps.setString(n++,new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payRiskUserLimit.endDate));
                }
                ps.setString(n++,payRiskUserLimit.limitTimeFlag);
                ps.setString(n++,new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payRiskUserLimit.updateTime));
                ps.setString(n++,payRiskUserLimit.id);
                ps.executeUpdate();
            } catch (Exception e) {
                e.printStackTrace();
                throw e;
            }finally {
                close(null, ps, con);
            }
        }
}