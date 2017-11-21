package com.pay.merchantinterface.dao;

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
import com.pay.merchantinterface.service.PayRequest;
import com.pay.order.dao.PayReceiveAndPay;
/**
 * Table PAY_REAL_NAME_AUTH DAO. 
 * @author Administrator
 *
 */
public class PayRealNameAuthDAO  extends BaseDAO {
    private static final Log log = LogFactory.getLog(PayRealNameAuthDAO.class);
    public static synchronized PayRealNameAuth getPayRealNameAuthValue(ResultSet rs)throws SQLException {
        PayRealNameAuth payRealNameAuth = new PayRealNameAuth();
        payRealNameAuth.id = rs.getString("ID");
        payRealNameAuth.userId = rs.getString("USER_ID");
        payRealNameAuth.way = rs.getString("WAY");
        payRealNameAuth.channelId = rs.getString("CHANNEL_ID");
        payRealNameAuth.sn = rs.getString("SN");
        payRealNameAuth.bankGeneralName = rs.getString("BANK_GENERAL_NAME");
        payRealNameAuth.bankName = rs.getString("BANK_NAME");
        payRealNameAuth.bankCode = rs.getString("BANK_CODE");
        payRealNameAuth.accountType = rs.getString("ACCOUNT_TYPE");
        payRealNameAuth.accountNo = rs.getString("ACCOUNT_NO");
        payRealNameAuth.accountName = rs.getString("ACCOUNT_NAME");
        payRealNameAuth.idType = rs.getString("ID_TYPE");
        payRealNameAuth.certId = rs.getString("CERT_ID");
        payRealNameAuth.tel = rs.getString("TEL");
        payRealNameAuth.status = rs.getString("STATUS");
        payRealNameAuth.field1 = rs.getString("FIELD1");
        payRealNameAuth.field2 = rs.getString("FIELD2");
        payRealNameAuth.remark = rs.getString("REMARK");
        payRealNameAuth.createTime = rs.getTimestamp("CREATE_TIME");
        payRealNameAuth.tranId = rs.getString("TRAN_ID");
        payRealNameAuth.successTime = rs.getTimestamp("SUCCESS_TIME");
        return payRealNameAuth;
    }
    public String addPayRealNameAuth(PayRealNameAuth payRealNameAuth) throws Exception {
        String sql = "insert into PAY_REAL_NAME_AUTH("+
            "ID," + 
            "USER_ID," + 
            "WAY," + 
            "CHANNEL_ID," + 
            "SN," + 
            "BANK_GENERAL_NAME," + 
            "BANK_NAME," + 
            "BANK_CODE," + 
            "ACCOUNT_TYPE," + 
            "ACCOUNT_NO," + 
            "ACCOUNT_NAME," + 
            "ID_TYPE," + 
            "CERT_ID," + 
            "TEL," + 
            "STATUS," + 
            "FIELD1," + 
            "FIELD2," + 
            "REMARK," + 
            "CREATE_TIME," + 
            "TRAN_ID," + 
            "SUCCESS_TIME)values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,to_date(?,'yyyy-mm-dd hh24:mi:ss'),?,to_date(?,'yyyy-mm-dd hh24:mi:ss'))";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            int n=1;
            ps.setString(n++,payRealNameAuth.id);
            ps.setString(n++,payRealNameAuth.userId);
            ps.setString(n++,payRealNameAuth.way);
            ps.setString(n++,payRealNameAuth.channelId);
            ps.setString(n++,payRealNameAuth.sn);
            ps.setString(n++,payRealNameAuth.bankGeneralName);
            ps.setString(n++,payRealNameAuth.bankName);
            ps.setString(n++,payRealNameAuth.bankCode);
            ps.setString(n++,payRealNameAuth.accountType);
            ps.setString(n++,payRealNameAuth.accountNo);
            ps.setString(n++,payRealNameAuth.accountName);
            ps.setString(n++,payRealNameAuth.idType);
            ps.setString(n++,payRealNameAuth.certId);
            ps.setString(n++,payRealNameAuth.tel);
            ps.setString(n++,payRealNameAuth.status);
            ps.setString(n++,payRealNameAuth.field1);
            ps.setString(n++,payRealNameAuth.field2);
            ps.setString(n++,payRealNameAuth.remark);
            ps.setString(n++,new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payRealNameAuth.createTime));
            ps.setString(n++,payRealNameAuth.tranId);
            ps.setString(n++,new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payRealNameAuth.successTime));
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
     * 认证插入，已经存在的账号不插入
     * @param payRequest
     * @return
     * @throws Exception
     */
    public String addPayRealNameAuthBatch(PayRequest payRequest) throws Exception {
    	Map map = getRealNameAuthedList(payRequest.receivePayList);
        String sql = "insert into PAY_REAL_NAME_AUTH("+
            "ID," + 
            "USER_ID," + 
            "WAY," + 
            "CHANNEL_ID," + 
            "SN," + 
            "BANK_GENERAL_NAME," + 
            "BANK_NAME," + 
            "BANK_CODE," + 
            "ACCOUNT_TYPE," + 
            "ACCOUNT_NO," + 
            "ACCOUNT_NAME," + 
            "ID_TYPE," + 
            "CERT_ID," + 
            "TEL," + 
            "STATUS," + 
            "CREATE_TIME," + 
            "TRAN_ID)values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,sysdate,?)";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            for(int i=0; i<payRequest.receivePayList.size(); i++){
	            int n=1;
	            PayReceiveAndPay rp = payRequest.receivePayList.get(i);
	            //已经存在的账号不插入
	            if(rp.respCode.length()>0||map.get(rp.accNo)!=null)continue;
	            ps.setString(n++,Tools.getUniqueIdentify());
	            ps.setString(n++,"");
	            ps.setString(n++,rp.way);
	            ps.setString(n++,rp.channelId);
	            ps.setString(n++,payRequest.merchantId+"_"+rp.sn);
	            ps.setString(n++,rp.bankGeneralName);
	            ps.setString(n++,rp.bankName);
	            ps.setString(n++,rp.bankId);
	            ps.setString(n++,rp.accType);
	            ps.setString(n++,rp.accNo);
	            ps.setString(n++,rp.accName);
	            ps.setString(n++,rp.credentialType);
	            ps.setString(n++,rp.credentialNo);
	            ps.setString(n++,rp.tel);
	            ps.setString(n++,"0");
	            ps.setString(n++,payRequest.merchantId+"_"+payRequest.tranId);
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
     * 认证插入，已经存在的账号不插入
     * @param payRequest
     * @return
     * @throws Exception
     */
    public String addPayRealNameAuthBatch(PayRequest payRequest,List<PayReceiveAndPay> rnAuthList) throws Exception {
    	Map map = getRealNameAuthedList(rnAuthList);
        String sql = "insert into PAY_REAL_NAME_AUTH("+
            "ID," + 
            "USER_ID," + 
            "WAY," + 
            "CHANNEL_ID," + 
            "SN," + 
            "BANK_GENERAL_NAME," + 
            "BANK_NAME," + 
            "BANK_CODE," + 
            "ACCOUNT_TYPE," + 
            "ACCOUNT_NO," + 
            "ACCOUNT_NAME," + 
            "ID_TYPE," + 
            "CERT_ID," + 
            "TEL," + 
            "STATUS," + 
            "CREATE_TIME," + 
            "TRAN_ID)values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,sysdate,?)";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            for(int i=0; i<rnAuthList.size(); i++){
	            int n=1;
	            PayReceiveAndPay rp = rnAuthList.get(i);
	            //已经存在的账号不插入
	            if((rp.respCode.length()>0&&!"000".equals(rp.respCode))||map.get(rp.accNo)!=null)continue;
	            ps.setString(n++,Tools.getUniqueIdentify());
	            ps.setString(n++,"");
	            ps.setString(n++,rp.way);
	            ps.setString(n++,rp.channelId);
	            ps.setString(n++,payRequest.merchantId+"_"+rp.sn);
	            ps.setString(n++,rp.bankGeneralName);
	            ps.setString(n++,rp.bankName);
	            ps.setString(n++,rp.bankId);
	            ps.setString(n++,rp.accType);
	            ps.setString(n++,rp.accNo);
	            ps.setString(n++,rp.accName);
	            ps.setString(n++,rp.credentialType);
	            ps.setString(n++,rp.credentialNo);
	            ps.setString(n++,rp.tel);
	            ps.setString(n++,"0");
	            ps.setString(n++,payRequest.merchantId+"_"+payRequest.tranId);
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
    public List getList() throws Exception{
        String sql = "select * from PAY_REAL_NAME_AUTH";
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
                list.add(getPayRealNameAuthValue(rs));
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
     * @param payRealNameAuth
     * @return
     */
    private String setPayRealNameAuthSql(PayRealNameAuth payRealNameAuth) {
        StringBuffer sql = new StringBuffer();
        
        String tmp = sql.toString();
        if(tmp.length()>0)tmp=tmp.substring(0,tmp.lastIndexOf(" and "));
        return tmp;
    }
    /**
     * Set query parameter.
     * @param ps
     * @param payRealNameAuth
     * @param n
     * @return
     * @throws SQLException
     */
    private int setPayRealNameAuthParameter(PreparedStatement ps,PayRealNameAuth payRealNameAuth,int n)throws SQLException {
        return n;
    }
    /**
     * Get records count.
     * @param payRealNameAuth
     * @return
     */
    public int getPayRealNameAuthCount(PayRealNameAuth payRealNameAuth) {
        String sqlCon = setPayRealNameAuthSql(payRealNameAuth);
        String sql = "select count(rownum) recordCount from PAY_REAL_NAME_AUTH " +(sqlCon.length()==0?"":" where "+ sqlCon);
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            int n = 1;
            setPayRealNameAuthParameter(ps,payRealNameAuth,n);
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
     * @param payRealNameAuth
     * @param page
     * @param rows
     * @param sort
     * @param order
     * @return
     * @throws Exception
     */
    public List getPayRealNameAuthList(PayRealNameAuth payRealNameAuth,int page,int rows,String sort,String order) throws Exception{
        sort = Tools.paraNameToDBColumnName(sort);
        String sqlCon = setPayRealNameAuthSql(payRealNameAuth);
        String sortOrder = sort == null || sort.length()==0?" order by ID desc":(" order by " + sort +("desc".equals(order)?" desc ":" asc "));
        String sql = "select * from (" +
                "  select rownum rowno,tmp1.* from (" +
                "   select tmp.*  from PAY_REAL_NAME_AUTH tmp " +(sqlCon.length()==0?"":" where "+ sqlCon) +sortOrder+
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
            setPayRealNameAuthParameter(ps,payRealNameAuth,n);
            rs = ps.executeQuery();
            while(rs.next()){
                list.add(getPayRealNameAuthValue(rs));
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
     * update PayRealNameAuth
     * @param payRealNameAuth
     * @throws Exception
     */
    public void updatePayRealNameAuth(PayRealNameAuth payRealNameAuth) throws Exception {
        String sqlTmp = "";
        if(payRealNameAuth.id != null)sqlTmp = sqlTmp + " ID=?,";
        if(payRealNameAuth.userId != null)sqlTmp = sqlTmp + " USER_ID=?,";
        if(payRealNameAuth.way != null)sqlTmp = sqlTmp + " WAY=?,";
        if(payRealNameAuth.channelId != null)sqlTmp = sqlTmp + " CHANNEL_ID=?,";
        if(payRealNameAuth.sn != null)sqlTmp = sqlTmp + " SN=?,";
        if(payRealNameAuth.bankGeneralName != null)sqlTmp = sqlTmp + " BANK_GENERAL_NAME=?,";
        if(payRealNameAuth.bankName != null)sqlTmp = sqlTmp + " BANK_NAME=?,";
        if(payRealNameAuth.bankCode != null)sqlTmp = sqlTmp + " BANK_CODE=?,";
        if(payRealNameAuth.accountType != null)sqlTmp = sqlTmp + " ACCOUNT_TYPE=?,";
        if(payRealNameAuth.accountNo != null)sqlTmp = sqlTmp + " ACCOUNT_NO=?,";
        if(payRealNameAuth.accountName != null)sqlTmp = sqlTmp + " ACCOUNT_NAME=?,";
        if(payRealNameAuth.idType != null)sqlTmp = sqlTmp + " ID_TYPE=?,";
        if(payRealNameAuth.certId != null)sqlTmp = sqlTmp + " CERT_ID=?,";
        if(payRealNameAuth.tel != null)sqlTmp = sqlTmp + " TEL=?,";
        if(payRealNameAuth.status != null)sqlTmp = sqlTmp + " STATUS=?,";
        if(payRealNameAuth.field1 != null)sqlTmp = sqlTmp + " FIELD1=?,";
        if(payRealNameAuth.field2 != null)sqlTmp = sqlTmp + " FIELD2=?,";
        if(payRealNameAuth.remark != null)sqlTmp = sqlTmp + " REMARK=?,";
        if(payRealNameAuth.createTime != null)sqlTmp = sqlTmp + " CREATE_TIME=to_date(?,'yyyy-mm-dd hh24:mi:ss'),";
        if(payRealNameAuth.tranId != null)sqlTmp = sqlTmp + " TRAN_ID=?,";
        if(payRealNameAuth.successTime != null)sqlTmp = sqlTmp + " SUCCESS_TIME=to_date(?,'yyyy-mm-dd hh24:mi:ss'),";
        if(sqlTmp.length()==0)return;
        String sql = "update PAY_REAL_NAME_AUTH "+        
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
            if(payRealNameAuth.id != null)ps.setString(n++,payRealNameAuth.id);
            if(payRealNameAuth.userId != null)ps.setString(n++,payRealNameAuth.userId);
            if(payRealNameAuth.way != null)ps.setString(n++,payRealNameAuth.way);
            if(payRealNameAuth.channelId != null)ps.setString(n++,payRealNameAuth.channelId);
            if(payRealNameAuth.sn != null)ps.setString(n++,payRealNameAuth.sn);
            if(payRealNameAuth.bankGeneralName != null)ps.setString(n++,payRealNameAuth.bankGeneralName);
            if(payRealNameAuth.bankName != null)ps.setString(n++,payRealNameAuth.bankName);
            if(payRealNameAuth.bankCode != null)ps.setString(n++,payRealNameAuth.bankCode);
            if(payRealNameAuth.accountType != null)ps.setString(n++,payRealNameAuth.accountType);
            if(payRealNameAuth.accountNo != null)ps.setString(n++,payRealNameAuth.accountNo);
            if(payRealNameAuth.accountName != null)ps.setString(n++,payRealNameAuth.accountName);
            if(payRealNameAuth.idType != null)ps.setString(n++,payRealNameAuth.idType);
            if(payRealNameAuth.certId != null)ps.setString(n++,payRealNameAuth.certId);
            if(payRealNameAuth.tel != null)ps.setString(n++,payRealNameAuth.tel);
            if(payRealNameAuth.status != null)ps.setString(n++,payRealNameAuth.status);
            if(payRealNameAuth.field1 != null)ps.setString(n++,payRealNameAuth.field1);
            if(payRealNameAuth.field2 != null)ps.setString(n++,payRealNameAuth.field2);
            if(payRealNameAuth.remark != null)ps.setString(n++,payRealNameAuth.remark);
            if(payRealNameAuth.createTime != null)ps.setString(n++,new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payRealNameAuth.createTime));
            if(payRealNameAuth.tranId != null)ps.setString(n++,payRealNameAuth.tranId);
            if(payRealNameAuth.successTime != null)ps.setString(n++,new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payRealNameAuth.successTime));
            ps.setString(n++,payRealNameAuth.id);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }finally {
            close(null, ps, con);
        }
    }
	public boolean existTranIdSnIds(PayRequest request) throws Exception {
		String sql = "select * from PAY_REAL_NAME_AUTH where ";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sqlTmp = "";
        try {
            con = connection();
            for(int i=0; i<request.receivePayList.size(); i++){
            	PayReceiveAndPay rp = request.receivePayList.get(i);
    			sqlTmp = sqlTmp + " SN='"+request.merchantId+"_"+rp.sn+
    					"' or ";
            }
            sqlTmp += " TRAN_ID='"+request.merchantId+"_"+request.tranId+"'";
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
	public void updatePayRealNameAuthBySearchChannel(List<PayReceiveAndPay> receivePayList) throws Exception {
		String sql = "update PAY_REAL_NAME_AUTH set WAY=?,CHANNEL_ID=?,BANK_NAME=?,BANK_CODE=?,ACCOUNT_NAME=?,ID_TYPE=?,CERT_ID=?,TEL=?,STATUS=?,SUCCESS_TIME=sysdate,REMARK=? where ACCOUNT_NO=?";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            for(int i=0; i<receivePayList.size(); i++){
	            int n=1;
	            PayReceiveAndPay rp = receivePayList.get(i);
	            log.info("WAY="+rp.way+";CHANNEL_ID="+rp.channelId+";BANK_NAME="+rp.bankName+";BANK_CODE="+rp.bankCode+";ACCOUNT_NAME="+rp.accountNo+
	            		";ID_TYPE="+rp.idType+";CERT_ID="+rp.certId+";TEL="+rp.tel+";STATUS="+rp.status+";REMARK=");
	            ps.setString(n++, rp.way);
	            ps.setString(n++, rp.channelId);
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
	            	ps.setString(n++,"未取得认证结果");
	            	rp.respCode="-1";
	            	rp.respDesc="CJ未取得认证结果";
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
	public Map getRealNameAuthedSuccessList(PayRequest request) throws Exception {
		String sql = "select * from PAY_REAL_NAME_AUTH where ";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Map map = new HashMap();
        String tmp = "";
        try {
        	for(int i=0; i<request.receivePayList.size(); i++) { 
        		PayReceiveAndPay rp = request.receivePayList.get(i);
        		tmp = tmp + "(ACCOUNT_NO='"+rp.accNo+"' and ACCOUNT_NAME='"+rp.accName+"' and CERT_ID='"+rp.credentialNo+"' and TEL='"+rp.tel+"') or ";
        	}
        	if(tmp.length()==0)return map;
        	tmp = tmp.substring(0,tmp.length()-4);
            con = connection();
            ps = con.prepareStatement(sql+"("+tmp+") and STATUS='1'");
            rs = ps.executeQuery();
            while(rs.next()){
            	PayRealNameAuth auth = getPayRealNameAuthValue(rs);
            	map.put(auth.accountNo, auth);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(rs, ps, con);
        }
        return map;
	}
	public Map getRealNameAuthedSuccessList(PayRequest request,List<PayReceiveAndPay> rnAuthList) throws Exception {
		String sql = "select * from PAY_REAL_NAME_AUTH where ";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Map map = new HashMap();
        String tmp = "";
        try {
        	for(int i=0; i<rnAuthList.size(); i++){
        		PayReceiveAndPay rp = rnAuthList.get(i);
        		tmp = tmp + "(ACCOUNT_NO='"+rp.accNo+"' and ACCOUNT_NAME='"+rp.accName+"' and ID_TYPE='"
        				+rp.credentialType+"' and CERT_ID='"+rp.credentialNo+"' and TEL='"+rp.tel+"') or ";
        	}
        	if(tmp.length()==0)return map;
        	tmp = tmp.substring(0,tmp.length()-4);
            con = connection();
            ps = con.prepareStatement(sql+"("+tmp+") and STATUS='1'");
            log.info(sql+"("+tmp+") and STATUS='1'");
            rs = ps.executeQuery();
            while(rs.next()){
            	PayRealNameAuth auth = getPayRealNameAuthValue(rs);
            	map.put(auth.accountNo, auth);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(rs, ps, con);
        }
        return map;
	}
	public Map getRealNameAuthedList(List<PayReceiveAndPay> list) throws Exception {
		String sql = "select ACCOUNT_NO,STATUS from PAY_REAL_NAME_AUTH where ";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Map map = new HashMap();
        String tmp = "";
        try {
        	for(int i=0; i<list.size(); i++)tmp = tmp + "ACCOUNT_NO='"+list.get(i).accNo+"' or ";
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
}