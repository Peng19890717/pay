package com.common.sms.service;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import util.DataTransUtil;
import util.MD5;
import util.Tools;

import com.common.sms.dao.SmsLog;
import com.common.sms.dao.SmsLogDAO;

/**
 * Object SMS_LOG service. 
 * @author Administrator
 *
 */
public class SmsLogService {
    /**
     * Get records list(json).
     * @return
     */
    public String getSmsLogList(SmsLog smsLog,int page,int rows,String sort,String order){
        try {
            SmsLogDAO smsLogDAO = new SmsLogDAO();
            List list = smsLogDAO.getSmsLogList(smsLog, page, rows, sort, order);
            JSONObject json = new JSONObject();
            json.put("total", String.valueOf(smsLogDAO.getSmsLogCount(smsLog)));
            JSONArray row = new JSONArray();
            for(int i = 0; i<list.size(); i++){
                row.put(i, ((SmsLog)list.get(i)).toJson());
            }
            json.put("rows", row);
            return json.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
    /**
     * add SmsLog
     * @param smsLog
     * @throws Exception
     */
    public void addSmsLog(SmsLog smsLog) throws Exception {
    	String [] mobiles = smsLog.tel.split(",");
    	List smsLogList = new ArrayList();
    	for(int i = 0; i < mobiles.length; i++){
    		SmsLog tmp = new SmsLog();
    		tmp.id = Tools.getUniqueIdentify();
    		tmp.tel = mobiles[i];
    		tmp.content = smsLog.content;
    		tmp.createId = smsLog.createId;
    		tmp.status = "0";
    		tmp.remark = "";
    		send(tmp.tel,tmp.content);
    		smsLogList.add(tmp);
    	}
        new SmsLogDAO().addSmsLog(smsLogList);
    }
    /**
     * remove SmsLog
     * @param id
     * @throws Exception
     */
    public void removeSmsLog(String id) throws Exception {
        new SmsLogDAO().removeSmsLog(id);
    }
    /**
     * update SmsLog
     * @param smsLog
     * @throws Exception
     */
    public void updateSmsLog(SmsLog smsLog) throws Exception {
        new SmsLogDAO().updateSmsLog(smsLog);
    }
    public String send(String mobile,String content){
    	String resStr = "";
//    	try {
//    		String password="f111da0dffbcdf078656d80c0a2b2865";
//    		String sign = MD5.getCryptographByMd5(mobile+content+password);
//    		String url="http://127.0.0.1:8080/smp/send.htm";
//    		byte [] b = ("user=u1&num="+mobile+"&msg="+content+"&sign="+sign).getBytes("utf-8");
//    		String result = new String(new DataTransUtil().doPost(url,b));
//    		if(!"0".equals(new String(result)))throw new Exception("send message fail.("+new String(result)+")");
//		} catch (Exception e) {
//			e.printStackTrace();
//			resStr = e.getMessage();
//		}
		return resStr;
    }
}