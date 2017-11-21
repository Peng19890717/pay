package com.pay.settlement.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import util.PayUtil;

import com.PayConstant;
import com.jweb.dao.BaseDAO;
/**
 * 数据库备份DAO. 
 * @author Administrator
 *
 */
public class PayDbBakDAO extends BaseDAO {
    private static final Log log = LogFactory.getLog(PayDbBakDAO.class);
    /**
     * 创建/替换Dblink
     * @throws Exception
     */
    public void createDblink() throws Exception {
    	String [] dbInfo = null;
    	if(PayConstant.PAY_CONFIG.get("PAY_DB_BAK_AUTH_INFO") != null){
    		dbInfo = PayConstant.PAY_CONFIG.get("PAY_DB_BAK_AUTH_INFO").split(",");
    	}
    	//create public  database link PAY_DB_BAK connect to pay_db_bak identified by pay_db123 using '(DESCRIPTION =(ADDRESS_LIST =(ADDRESS =(PROTOCOL = TCP)(HOST =127.0.0.1)(PORT = 1521)))(CONNECT_DATA =(SERVICE_NAME = orcl)))';
    	//127.0.0.1,1521,orcl,pay_db,pay_db123
    	if(dbInfo==null || dbInfo.length!=5)throw new Exception("数据库链接信息错误："+PayConstant.PAY_CONFIG.get("PAY_DB_BAK_AUTH_INFO"));
    	String dropDblink = "drop public database link PAY_DB_BAK";
    	String createDblink = "create public  database link PAY_DB_BAK connect to " +
    			dbInfo[3] + " identified by " + dbInfo[4] +
    			" using '(DESCRIPTION =(ADDRESS_LIST =(ADDRESS =(PROTOCOL = TCP)(HOST = " +
    			dbInfo[0] + ")(PORT = " + dbInfo[1] + ")))(CONNECT_DATA =(SERVICE_NAME = " + dbInfo[2]+")))'";
        log.info(dropDblink);
        log.info(createDblink);
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = connection();
            try {
            	ps = con.prepareStatement(dropDblink);
                ps.executeUpdate();
			} catch (Exception e) {
				log.info(PayUtil.exceptionToString(e));
			} finally {
				try {ps.close();} catch (Exception e2) {}
			}
            ps = con.prepareStatement(createDblink);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(null, ps, con);
        }
    
    }
    public void cutTransDate() throws Exception {
        Connection con = null;
        PreparedStatement ps = null;
        GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
        Date today = new Date();
        try {
            con = connection();
            con.setAutoCommit(false);
        	gc.setTime(today);
    		gc.add(Calendar.DATE,0-Integer.parseInt(PayConstant.PAY_CONFIG.get("PAY_DB_BAK_TRANS_DAYS")));
    		String d = new SimpleDateFormat("yyyy-MM-dd").format(gc.getTime())+" 23:59:59";
    		//产品订单表备份开始
        	String dateCon = " ORDERTIME <=to_date('"+d+"','yyyy-mm-dd hh24:mi:ss')";
        	String sql = "insert into PAY_PRODUCT_ORDER@PAY_DB_BAK select * from PAY_PRODUCT_ORDER where "+dateCon;//产品订单到备份数据库
        	log.info(sql);
        	ps = con.prepareStatement(sql);
            ps.executeUpdate();
            ps.close();
            //交易订单表备份开始
//            sql = "insert into PAY_ORDER@PAY_DB_BAK select payOrder.* from PAY_ORDER payOrder,(select merno,PRDORDNO from PAY_PRODUCT_ORDER where "+dateCon
//            		+") prdOrder where payOrder.merno=prdOrder.merno and payOrder.PRDORDNO=prdOrder.PRDORDNO";
            sql = "insert into PAY_ORDER@PAY_DB_BAK select * from PAY_ORDER where CREATETIME<=to_date('"+d+"','yyyy-mm-dd hh24:mi:ss')";
            log.info(sql);
        	ps = con.prepareStatement(sql);
            ps.executeUpdate();
            ps.close();
            //历史交易订单表备份开始
//            sql = "insert into PAY_ORDER_HISTORY@PAY_DB_BAK select payOrder.* from PAY_ORDER_HISTORY payOrder,(select merno,PRDORDNO from PAY_PRODUCT_ORDER where "+dateCon
//            		+") prdOrder where payOrder.merno=prdOrder.merno and payOrder.PRDORDNO=prdOrder.PRDORDNO";
            sql = "insert into PAY_ORDER_HISTORY@PAY_DB_BAK select * from PAY_ORDER_HISTORY where CREATETIME<=to_date('"+d+"','yyyy-mm-dd hh24:mi:ss')";
            log.info(sql);
        	ps = con.prepareStatement(sql);
            ps.executeUpdate();
            ps.close();
            //删除交易订单表数据
//            sql = "delete from PAY_ORDER payOrder where exists (select 1 from (select merno,PRDORDNO from PAY_PRODUCT_ORDER where "+dateCon
//            		+") prdOrder where payOrder.merno=prdOrder.merno and payOrder.PRDORDNO=prdOrder.PRDORDNO)";//产品订单删除从当前数据库
            sql = "delete from PAY_ORDER where CREATETIME<=to_date('"+d+"','yyyy-mm-dd hh24:mi:ss')";//产品订
            log.info(sql);
        	ps = con.prepareStatement(sql);
            ps.executeUpdate();
            ps.close();
            //删除历史交易订单表数据
//            sql = "delete from PAY_ORDER_HISTORY payOrder where exists (select 1 from (select merno,PRDORDNO from PAY_PRODUCT_ORDER where "+dateCon
//            		+") prdOrder where payOrder.merno=prdOrder.merno and payOrder.PRDORDNO=prdOrder.PRDORDNO)";//产品订单删除从当前数据库
            sql = "delete from PAY_ORDER_HISTORY payOrder where CREATETIME<=to_date('"+d+"','yyyy-mm-dd hh24:mi:ss')";//产品订单删除从当前数据库
            log.info(sql);
        	ps = con.prepareStatement(sql);
            ps.executeUpdate();
            ps.close();
            //删除产品订单表数据
            sql = "delete from PAY_PRODUCT_ORDER where "+dateCon;//产品订单删除从当前数据库
            log.info(sql);
        	ps = con.prepareStatement(sql);
            ps.executeUpdate();
            ps.close();
            //商户信息备份
            sql = "delete from PAY_MERCHANT_ROOT@PAY_DB_BAK";
            log.info(sql);
        	ps = con.prepareStatement(sql);
            ps.executeUpdate();
            ps.close();
            sql = "insert into PAY_MERCHANT_ROOT@PAY_DB_BAK select * from PAY_MERCHANT_ROOT";
            log.info(sql);
        	ps = con.prepareStatement(sql);
            ps.executeUpdate();
            ps.close();
            con.commit();
        } catch (Exception e) {
            e.printStackTrace();
            con.rollback();
            throw e;
        } finally {
        	con.setAutoCommit(true);
            close(null, ps, con);
        }
    }
}