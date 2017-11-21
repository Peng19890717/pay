package com.pay.merchantinterface.dao;

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
import com.pay.order.dao.PayReceiveAndPay;
/**
 * Table PAY_RECEIVE_AND_PAY_SIGN DAO. 
 * @author Administrator
 *
 */
public class PayReceiveAndPaySignDAO  extends BaseDAO {
    private static final Log log = LogFactory.getLog(PayReceiveAndPaySignDAO.class);
    public static synchronized PayReceiveAndPaySign getPayReceiveAndPaySignValue(ResultSet rs)throws SQLException {
        PayReceiveAndPaySign payReceiveAndPaySign = new PayReceiveAndPaySign();
        payReceiveAndPaySign.id = rs.getString("ID");
        payReceiveAndPaySign.corpAcctNo = rs.getString("CORP_ACCT_NO");
        payReceiveAndPaySign.businessCode = rs.getString("BUSINESS_CODE");
        payReceiveAndPaySign.protocolType = rs.getString("PROTOCOL_TYPE");
        payReceiveAndPaySign.sn = rs.getString("SN");
        payReceiveAndPaySign.protocolNo = rs.getString("PROTOCOL_NO");
        payReceiveAndPaySign.bankName = rs.getString("BANK_NAME");
        payReceiveAndPaySign.bankCode = rs.getString("BANK_CODE");
        payReceiveAndPaySign.accountType = rs.getString("ACCOUNT_TYPE");
        payReceiveAndPaySign.accountNo = rs.getString("ACCOUNT_NO");
        payReceiveAndPaySign.accountName = rs.getString("ACCOUNT_NAME");
        payReceiveAndPaySign.idType = rs.getString("ID_TYPE");
        payReceiveAndPaySign.certId = rs.getString("CERT_ID");
        payReceiveAndPaySign.beginDate = rs.getString("BEGIN_DATE");
        payReceiveAndPaySign.endDate = rs.getString("END_DATE");
        payReceiveAndPaySign.tel = rs.getString("TEL");
        payReceiveAndPaySign.status = rs.getString("STATUS");
        payReceiveAndPaySign.field1 = rs.getString("FIELD1");
        payReceiveAndPaySign.field2 = rs.getString("FIELD2");
        payReceiveAndPaySign.remark = rs.getString("REMARK");
        payReceiveAndPaySign.createTime = rs.getTimestamp("CREATE_TIME");
        payReceiveAndPaySign.signProtocolChannel = rs.getString("SIGN_PROTOCOL_CHANNEL");
        return payReceiveAndPaySign;
    }
    public String addPayReceiveAndPaySign(PayReceiveAndPaySign payReceiveAndPaySign) throws Exception {
        String sql = "insert into PAY_RECEIVE_AND_PAY_SIGN("+
            "ID," + 
            "CORP_ACCT_NO," + 
            "BUSINESS_CODE," + 
            "PROTOCOL_TYPE," + 
            "SN," + 
            "PROTOCOL_NO," + 
            "BANK_NAME," + 
            "BANK_CODE," + 
            "ACCOUNT_TYPE," + 
            "ACCOUNT_NO," + 
            "ACCOUNT_NAME," + 
            "ID_TYPE," + 
            "CERT_ID," + 
            "BEGIN_DATE," + 
            "END_DATE," + 
            "TEL," + 
            "STATUS," + 
            "FIELD1," + 
            "FIELD2," + 
            "REMARK," + 
            "CREATE_TIME,SIGN_PROTOCOL_CHANNEL)values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,to_date(?,'yyyy-mm-dd hh24:mi:ss'),?)";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            int n=1;
            ps.setString(n++,payReceiveAndPaySign.id);
            ps.setString(n++,payReceiveAndPaySign.corpAcctNo);
            ps.setString(n++,payReceiveAndPaySign.businessCode);
            ps.setString(n++,payReceiveAndPaySign.protocolType);
            ps.setString(n++,payReceiveAndPaySign.sn);
            ps.setString(n++,payReceiveAndPaySign.protocolNo);
            ps.setString(n++,payReceiveAndPaySign.bankName);
            ps.setString(n++,payReceiveAndPaySign.bankCode);
            ps.setString(n++,payReceiveAndPaySign.accountType);
            ps.setString(n++,payReceiveAndPaySign.accountNo);
            ps.setString(n++,payReceiveAndPaySign.accountName);
            ps.setString(n++,payReceiveAndPaySign.idType);
            ps.setString(n++,payReceiveAndPaySign.certId);
            ps.setString(n++,payReceiveAndPaySign.beginDate);
            ps.setString(n++,payReceiveAndPaySign.endDate);
            ps.setString(n++,payReceiveAndPaySign.tel);
            ps.setString(n++,payReceiveAndPaySign.status);
            ps.setString(n++,payReceiveAndPaySign.field1);
            ps.setString(n++,payReceiveAndPaySign.field2);
            ps.setString(n++,payReceiveAndPaySign.remark);
            ps.setString(n++,new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payReceiveAndPaySign.createTime));
            ps.setString(n++,payReceiveAndPaySign.signProtocolChannel);
            ps.executeUpdate();
            return "";
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }finally {
            close(null, ps, con);
        }
    }
    public Map getSignedList(List <PayReceiveAndPay>receivePayList) throws Exception {
		String sql = "select ACCOUNT_NO,STATUS from PAY_RECEIVE_AND_PAY_SIGN where ";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Map map = new HashMap();
        String tmp = "";
        try {
        	for(int i=0; i<receivePayList.size(); i++)tmp = tmp + "(ACCOUNT_NO='"
        			+receivePayList.get(i).accNo+"' and BUSINESS_CODE='"+receivePayList.get(i).businessCode+"') or ";
        	if(tmp.length()==0)return map;
        	tmp = tmp.substring(0,tmp.length()-4);
            con = connection();
            ps = con.prepareStatement(sql+tmp);
            log.info(sql+tmp);
            rs = ps.executeQuery();
            while(rs.next())map.put(rs.getString("ACCOUNT_NO"),rs.getString("STATUS"));
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(rs, ps, con);
        }
        return map;
	}
    public String addPayReceiveAndPaySignBatch(List <PayReceiveAndPay>receivePayList) throws Exception {
    	Map map = getSignedList(receivePayList);
        String sql = "insert into PAY_RECEIVE_AND_PAY_SIGN("+
            "ID," + 
            "CORP_ACCT_NO," +
            "BUSINESS_CODE," + 
            "PROTOCOL_TYPE," + 
            "SN," + 
            "PROTOCOL_NO," + 
            "BANK_NAME," + 
            "BANK_CODE," + 
            "ACCOUNT_TYPE," + 
            "ACCOUNT_NO," + 
            "ACCOUNT_NAME," + 
            "ID_TYPE," + 
            "CERT_ID," + 
            "BEGIN_DATE," + 
            "END_DATE," + 
            "TEL," + 
            "STATUS,SIGN_PROTOCOL_CHANNEL)values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            for(int i=0; i<receivePayList.size(); i++){
	            int n=1;
	            PayReceiveAndPay rp = receivePayList.get(i);
	            if((rp.respCode.length()>0&&!"000".equals(rp.respCode))||map.get(rp.accNo)!=null)continue;
	            ps.setString(n++,Tools.getUniqueIdentify());
	            ps.setString(n++,rp.corpAcctNo);
	            ps.setString(n++,rp.businessCode);
	            ps.setString(n++,rp.protocolType);
	            ps.setString(n++,rp.sn);
	            ps.setString(n++,rp.protocolNo);
	            ps.setString(n++,rp.bankName);
	            ps.setString(n++,rp.bankId);
	            ps.setString(n++,rp.accType);
	            ps.setString(n++,rp.accNo);
	            ps.setString(n++,rp.accName);
	            ps.setString(n++,rp.credentialType);
	            ps.setString(n++,rp.credentialNo);
	            ps.setString(n++,rp.beginDate);
	            ps.setString(n++,rp.endDate);
	            ps.setString(n++,rp.tel);
	            ps.setString(n++,rp.status);
	            ps.setString(n++,rp.signProtocolChannel);
	            ps.addBatch();
            }
            ps.executeBatch();
            return "";
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(null, ps, con);
        }
    }
    public Map getSignedSuccessRecord(List<PayReceiveAndPay> receivePayList,String channel) throws Exception{
    	Map map = new HashMap();
    	if(receivePayList.size()==0) return map;
    	String tmp = "";
    	for(int i=0; i<receivePayList.size(); i++){
    		PayReceiveAndPay rp = receivePayList.get(i);
	        tmp = tmp + "(ACCOUNT_NO='"+rp.accNo+"' and ACCOUNT_NAME='"+rp.accName+"' and ID_TYPE='"+rp.credentialType+"' and CERT_ID='"+rp.credentialNo
	        	+"' and TEL='"+rp.tel+"') or ";
    	}
    	if(tmp.length()==0)return map;
    	tmp = tmp.substring(0,tmp.length()-4);
        String sql = "select * from PAY_RECEIVE_AND_PAY_SIGN where ("+tmp+") and STATUS='1' and SIGN_PROTOCOL_CHANNEL='"+channel+"'";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while(rs.next()){
            	PayReceiveAndPaySign t = getPayReceiveAndPaySignValue(rs);
            	map.put(t.accountNo, t);
            }
            return map;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(rs, ps, con);
        }
    }
    /**
     * Set query condition sql.
     * @param payReceiveAndPaySign
     * @return
     */
    private String setPayReceiveAndPaySignSql(PayReceiveAndPaySign payReceiveAndPaySign) {
        StringBuffer sql = new StringBuffer();
        
        String tmp = sql.toString();
        if(tmp.length()>0)tmp=tmp.substring(0,tmp.lastIndexOf(" and "));
        return tmp;
    }
    /**
     * Set query parameter.
     * @param ps
     * @param payReceiveAndPaySign
     * @param n
     * @return
     * @throws SQLException
     */
    private int setPayReceiveAndPaySignParameter(PreparedStatement ps,PayReceiveAndPaySign payReceiveAndPaySign,int n)throws SQLException {
        return n;
    }
    /**
     * Get records count.
     * @param payReceiveAndPaySign
     * @return
     */
    public int getPayReceiveAndPaySignCount(PayReceiveAndPaySign payReceiveAndPaySign) {
        String sqlCon = setPayReceiveAndPaySignSql(payReceiveAndPaySign);
        String sql = "select count(rownum) recordCount from PAY_RECEIVE_AND_PAY_SIGN " +(sqlCon.length()==0?"":" where "+ sqlCon);
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            int n = 1;
            setPayReceiveAndPaySignParameter(ps,payReceiveAndPaySign,n);
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
     * @param payReceiveAndPaySign
     * @param page
     * @param rows
     * @param sort
     * @param order
     * @return
     * @throws Exception
     */
    public List getPayReceiveAndPaySignList(PayReceiveAndPaySign payReceiveAndPaySign,int page,int rows,String sort,String order) throws Exception{
        sort = Tools.paraNameToDBColumnName(sort);
        String sqlCon = setPayReceiveAndPaySignSql(payReceiveAndPaySign);
        String sortOrder = sort == null || sort.length()==0?" order by ID desc":(" order by " + sort +("desc".equals(order)?" desc ":" asc "));
        String sql = "select * from (" +
                "  select rownum rowno,tmp1.* from (" +
                "   select tmp.*  from PAY_RECEIVE_AND_PAY_SIGN tmp " +(sqlCon.length()==0?"":" where "+ sqlCon) +sortOrder+
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
            setPayReceiveAndPaySignParameter(ps,payReceiveAndPaySign,n);
            rs = ps.executeQuery();
            while(rs.next()){
                list.add(getPayReceiveAndPaySignValue(rs));
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
     * remove PayReceiveAndPaySign
     * @param id
     * @throws Exception     
     */
    public void clearSignExpired() throws Exception {
    	String today = new SimpleDateFormat("yyyyMMdd").format(new Date());
    	String bak = "insert into PAY_RECEIVE_AND_PAY_SIGN_BAK select * from PAY_RECEIVE_AND_PAY_SIGN where END_DATE<'"+today+"'";
        String sql = "delete from PAY_RECEIVE_AND_PAY_SIGN where END_DATE<'"+today+"'";
        log.info(bak);
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = connection();
            ps = con.prepareStatement(bak);
            ps.executeUpdate();
            ps.close();
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
     * detail PayReceiveAndPaySign
     * @param id
     * @return PayReceiveAndPaySign
     * @throws Exception
     */
    public PayReceiveAndPaySign detailPayReceiveAndPaySign(String id) throws Exception {
        String sql = "select * from PAY_RECEIVE_AND_PAY_SIGN where ID=?";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            ps.setString(1,id);
            rs = ps.executeQuery();
            if(rs.next())return getPayReceiveAndPaySignValue(rs); 
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(rs, ps, con);
        }
        return null;
    }
    /**
     * update PayReceiveAndPaySign
     * @param payReceiveAndPaySign
     * @throws Exception
     */
    public void updatePayReceiveAndPaySign(PayReceiveAndPaySign payReceiveAndPaySign) throws Exception {
        String sqlTmp = "";
        if(payReceiveAndPaySign.id != null)sqlTmp = sqlTmp + " ID=?,";
        if(payReceiveAndPaySign.corpAcctNo != null)sqlTmp = sqlTmp + " CORP_ACCT_NO=?,";
        if(payReceiveAndPaySign.businessCode != null)sqlTmp = sqlTmp + " BUSINESS_CODE=?,";
        if(payReceiveAndPaySign.protocolType != null)sqlTmp = sqlTmp + " PROTOCOL_TYPE=?,";
        if(payReceiveAndPaySign.sn != null)sqlTmp = sqlTmp + " SN=?,";
        if(payReceiveAndPaySign.protocolNo != null)sqlTmp = sqlTmp + " PROTOCOL_NO=?,";
        if(payReceiveAndPaySign.bankName != null)sqlTmp = sqlTmp + " BANK_NAME=?,";
        if(payReceiveAndPaySign.bankCode != null)sqlTmp = sqlTmp + " BANK_CODE=?,";
        if(payReceiveAndPaySign.accountType != null)sqlTmp = sqlTmp + " ACCOUNT_TYPE=?,";
        if(payReceiveAndPaySign.accountNo != null)sqlTmp = sqlTmp + " ACCOUNT_NO=?,";
        if(payReceiveAndPaySign.accountName != null)sqlTmp = sqlTmp + " ACCOUNT_NAME=?,";
        if(payReceiveAndPaySign.idType != null)sqlTmp = sqlTmp + " ID_TYPE=?,";
        if(payReceiveAndPaySign.certId != null)sqlTmp = sqlTmp + " CERT_ID=?,";
        if(payReceiveAndPaySign.beginDate != null)sqlTmp = sqlTmp + " BEGIN_DATE=?,";
        if(payReceiveAndPaySign.endDate != null)sqlTmp = sqlTmp + " END_DATE=?,";
        if(payReceiveAndPaySign.tel != null)sqlTmp = sqlTmp + " TEL=?,";
        if(payReceiveAndPaySign.status != null)sqlTmp = sqlTmp + " STATUS=?,";
        if(payReceiveAndPaySign.field1 != null)sqlTmp = sqlTmp + " FIELD1=?,";
        if(payReceiveAndPaySign.field2 != null)sqlTmp = sqlTmp + " FIELD2=?,";
        if(payReceiveAndPaySign.remark != null)sqlTmp = sqlTmp + " REMARK=?,";
        if(payReceiveAndPaySign.createTime != null)sqlTmp = sqlTmp + " CREATE_TIME=to_date(?,'yyyy-mm-dd hh24:mi:ss'),";
        if(payReceiveAndPaySign.signProtocolChannel != null)sqlTmp = sqlTmp + " SIGN_PROTOCOL_CHANNEL=?,";
        if(sqlTmp.length()==0)return;
        String sql = "update PAY_RECEIVE_AND_PAY_SIGN "+        
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
            if(payReceiveAndPaySign.id != null)ps.setString(n++,payReceiveAndPaySign.id);
            if(payReceiveAndPaySign.corpAcctNo != null)ps.setString(n++,payReceiveAndPaySign.corpAcctNo);
            if(payReceiveAndPaySign.businessCode != null)ps.setString(n++,payReceiveAndPaySign.businessCode);
            if(payReceiveAndPaySign.protocolType != null)ps.setString(n++,payReceiveAndPaySign.protocolType);
            if(payReceiveAndPaySign.sn != null)ps.setString(n++,payReceiveAndPaySign.sn);
            if(payReceiveAndPaySign.protocolNo != null)ps.setString(n++,payReceiveAndPaySign.protocolNo);
            if(payReceiveAndPaySign.bankName != null)ps.setString(n++,payReceiveAndPaySign.bankName);
            if(payReceiveAndPaySign.bankCode != null)ps.setString(n++,payReceiveAndPaySign.bankCode);
            if(payReceiveAndPaySign.accountType != null)ps.setString(n++,payReceiveAndPaySign.accountType);
            if(payReceiveAndPaySign.accountNo != null)ps.setString(n++,payReceiveAndPaySign.accountNo);
            if(payReceiveAndPaySign.accountName != null)ps.setString(n++,payReceiveAndPaySign.accountName);
            if(payReceiveAndPaySign.idType != null)ps.setString(n++,payReceiveAndPaySign.idType);
            if(payReceiveAndPaySign.certId != null)ps.setString(n++,payReceiveAndPaySign.certId);
            if(payReceiveAndPaySign.beginDate != null)ps.setString(n++,payReceiveAndPaySign.beginDate);
            if(payReceiveAndPaySign.endDate != null)ps.setString(n++,payReceiveAndPaySign.endDate);
            if(payReceiveAndPaySign.tel != null)ps.setString(n++,payReceiveAndPaySign.tel);
            if(payReceiveAndPaySign.status != null)ps.setString(n++,payReceiveAndPaySign.status);
            if(payReceiveAndPaySign.field1 != null)ps.setString(n++,payReceiveAndPaySign.field1);
            if(payReceiveAndPaySign.field2 != null)ps.setString(n++,payReceiveAndPaySign.field2);
            if(payReceiveAndPaySign.remark != null)ps.setString(n++,payReceiveAndPaySign.remark);
            if(payReceiveAndPaySign.createTime != null)ps.setString(n++,new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payReceiveAndPaySign.createTime));
            if(payReceiveAndPaySign.signProtocolChannel != null)ps.setString(n++,payReceiveAndPaySign.signProtocolChannel);
            ps.setString(n++,payReceiveAndPaySign.id);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }finally {
            close(null, ps, con);
        }
    }
	public void updatePayReceiveAndPaySignBySearchChannel(List<PayReceiveAndPay> receivePayList) throws Exception {
		String sql = "update PAY_RECEIVE_AND_PAY_SIGN set BANK_NAME=?,BANK_CODE=?,ACCOUNT_NAME=?,ID_TYPE=?,CERT_ID=?,TEL=?,STATUS=?,REMARK=? where ACCOUNT_NO=?";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            for(int i=0; i<receivePayList.size(); i++){
	            int n=1;
	            PayReceiveAndPay rp = receivePayList.get(i);
	            log.info("BANK_NAME="+rp.bankName+";BANK_CODE="+rp.bankId+";ACCOUNT_NAME="+rp.accName+";ID_TYPE="+rp.idType+";CERT_ID="+rp.certId+";TEL="+rp.tel+
	            		";STATUS="+rp.status+";REMARK="+";ACCOUNT_NO="+rp.accountNo);
	            ps.setString(n++, rp.bankName);
	            ps.setString(n++, rp.bankId);
	            ps.setString(n++, rp.accName);
	            ps.setString(n++, rp.credentialType);
	            ps.setString(n++, rp.credentialNo);
	            ps.setString(n++, rp.tel);
	            if("000".equals(rp.respCode)){
	            	ps.setString(n++,"1");
	            	ps.setString(n++,"");
	            	rp.setRespInfo("000");
	            }
	            else if("-1".equals(rp.respCode)){
	            	ps.setString(n++,"2");
	            	ps.setString(n++,"CJ错误："+rp.respDesc);
	            	rp.respDesc="CJ错误："+rp.respDesc;
	            }
	            else if(rp.respCode.length()==0){
	            	ps.setString(n++,"2");
	            	ps.setString(n++,"未取得签约结果");
	            	rp.respCode="-1";
	            	rp.respDesc="CJ未取得签约结果";
	            }
	            ps.setString(n++,rp.accNo);
	            ps.addBatch();
            }
            ps.executeBatch();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }finally {
            close(null, ps, con);
        }
	}
}