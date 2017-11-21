package com.pay.account.service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimerTask;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.pay.account.dao.PayBankAccountCheckDAO;
import com.pay.coopbank.dao.PayCoopBank;
import com.pay.coopbank.service.PayCoopBankService;
/**
 * 银行对账处理类，交易T+2日1点对账
 * @author Administrator
 *
 */
public class PayAutoAccountCheckListener extends TimerTask {
	private static final Log log = LogFactory.getLog(PayAutoAccountCheckListener.class);
	Date checkDate = null;
	public PayAutoAccountCheckListener(Date checkDate){
		GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
		gc.setTime(checkDate);
		//交易T+1
		gc.add(Calendar.DATE,-1);
		this.checkDate = gc.getTime();
	}
	public void run(){
		PayBankAccountCheckDAO accDao = new PayBankAccountCheckDAO();
		//遍历合作渠道列表
		for(int i = 0; i<PayCoopBankService.CHANNEL_LIST.size(); i++){
			PayCoopBank channel = (PayCoopBank)PayCoopBankService.CHANNEL_LIST.get(i);
			//银行未开启或者不支持在线对账，不对账（银行状态 0-正常；1-关闭 默认0）
			if(!"0".equals(channel.bankStatus)||!"0".equals(channel.accountOnline))continue;
			try {
				//清除对账临时表
				accDao.clearPayBankAccountCheckTmp();
				//取得对账文件，文件内容插入临时表
				new PayBankAccountCheckFileService(channel.bankCode).readFile(null);
				//取得以前对账失败并无处理的记录重新对账（3天之内的）
				//accDao.accountCheckOld(bank);
				//取得对账成功交易信息，插入对账表
				//accDao.accountCheckCreate(bank, new SimpleDateFormat("yyyy-MM-dd").format(checkDate));
				
				//支付订单对账
				new PayBankAccountCheckDAO().checkAccount();
				//退款对账 TODO
			} catch (Exception e) {
				StackTraceElement [] obj = e.getStackTrace();
				for(int k = 0; k<obj.length; k++)log.info(obj[k]);
			}
		}
	}
}
