package com.pay.user.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.jweb.dao.BaseDAO;

public class PayAccProfileDao extends BaseDAO {
	
	private static final Log log = LogFactory.getLog(PayTranUserCardDAO.class);
	
	/*
	 * 该方法执行更改PAY_ACC_PROFILE表中AC_STATUS 中状态。
	 * 
	 * 参数:1:客户用户名，2要该更的账户的类型。
	 * **/
	public void FrozePayAccProfile(String userId,String type){
		if(userId!=null&&!"".equalsIgnoreCase(userId)&&type!=null&&!"".equalsIgnoreCase(type))
		{
			  String sql = "update PAY_ACC_PROFILE SET AC_STATUS='"+type+"' where PAY_AC_NO='"+userId+"'";
			  log.info(sql);
		        Connection con = null;
		        PreparedStatement ps = null;
		        ResultSet rs = null;
		        try {
		            con = connection();
		            ps = con.prepareStatement(sql);
		            ps.execute();
		        } catch (Exception e) {
		            e.printStackTrace();
		        } finally {
		            close(rs, ps, con);
		        }
		}
	}
}
