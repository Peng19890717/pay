package com.pay.settlement.service;

import java.util.TimerTask;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.PayConstant;
import com.pay.settlement.dao.PayDbBakDAO;
/**
 * 数据库备份任务
 * @author Administrator
 *
 */
public class PayDbBackupListener extends TimerTask {
	private static final Log log = LogFactory.getLog(PayDbBackupListener.class);
	public void run(){
		transBackRun();
	}
	/**
	 * 交易数据备份
	 */
	public void transBackRun(){
		try {
			if(Integer.parseInt(PayConstant.PAY_CONFIG.get("PAY_DB_BAK_TRANS_DAYS")) >= 7){
				log.info("数据库备份开始===交易备份=================");
				PayDbBakDAO dao = new PayDbBakDAO();
				dao.createDblink();
				dao.cutTransDate();
				log.info("数据库备份结束===交易备份=================");
			}
		} catch (Exception e) {e.printStackTrace();}
	}
}
