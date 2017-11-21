package com.pay.coopbank.service;

import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jxl.CellType;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableCell;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;
import org.json.JSONObject;

import util.JWebConstant;
import util.Tools;

import com.pay.coopbank.dao.PayChannelRotationLog;
import com.pay.coopbank.dao.PayChannelRotationLogDAO;
import com.pay.merchantinterface.service.PayRequest;

/**
 * Object PAY_CHANNEL_ROTATION_LOG service. 
 * @author Administrator
 *
 */
public class PayChannelRotationLogService {
	
	private static final Log log = LogFactory.getLog(PayChannelRotationLogService.class);
	
    /**
     * Get records list(json).
     * @return
     */
    public String getPayChannelRotationLogList(PayChannelRotationLog payChannelRotationLog,int page,int rows,String sort,String order){
        try {
            PayChannelRotationLogDAO payChannelRotationLogDAO = new PayChannelRotationLogDAO();
            List list = payChannelRotationLogDAO.getPayChannelRotationLogList(payChannelRotationLog, page, rows, sort, order);
            JSONObject json = new JSONObject();
            json.put("total", String.valueOf(payChannelRotationLogDAO.getPayChannelRotationLogCount(payChannelRotationLog)));
            JSONArray row = new JSONArray();
            for(int i = 0; i<list.size(); i++){
                row.put(i, ((PayChannelRotationLog)list.get(i)).toJson());
            }
            //汇总
            json.put("rows", row);
            Long [] money = payChannelRotationLogDAO.getTotalChannelRotationLog(payChannelRotationLog);
            json.put("totalDayAmt", String.valueOf(money[0]));
            json.put("totalDaySuccessAmt", String.valueOf(money[1]));
            json.put("totalDayCount", String.valueOf(money[2]));
            json.put("totalDaySuccessCount", String.valueOf(money[3]));
            json.put("totalChannelFee", String.valueOf(money[4]));
            return json.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
	/**
	 * 添加渠道轮询信息
	 * @param payRequest
	 * @throws Exception 
	 */
	public void addChannelRotationLog(PayRequest payRequest,String channelAcc) throws Exception {
		PayChannelRotationLog prl = new PayChannelRotationLog();
		prl.channelId = payRequest.payOrder.payChannel;
		prl.day = new java.text.SimpleDateFormat("yyyyMMdd").format(payRequest.curTime);
		prl.dayAmt = payRequest.payOrder.txamt;
		prl.daySuccessAmt = 0l;
		prl.dayCount = 1l;
		prl.daySuccessCount = 0l;
		prl.channelFee = 0l;
		prl.channelAcc = channelAcc;
		new PayChannelRotationLogDAO().updateOrAddPayChannelRotationLog(payRequest,prl);
	}
	/**
	 * 导出Excel
	 */
	public byte[] exportExcelForChannelRotationLog(String flag, PayChannelRotationLog payChannelRotationLog) {
		PayChannelRotationLogDAO payChannelRotationLogDAO = new PayChannelRotationLogDAO();
		String randomName = Tools.getUniqueIdentify();
		//临时目录
    	File tmpFile = new File(JWebConstant.APP_PATH+"/dat/download/"+randomName);
    	tmpFile.mkdir();
    	//文件模板名称
    	String templetName ="channelRotationLog";
    	//模板文件
    	File templetFile = new File(JWebConstant.APP_PATH+"/templet/"+templetName+".xls");
    	log.info(" read templet file:"+JWebConstant.APP_PATH+"/templet/"+templetName+".xls");
    	//每个Excel最多条数
    	long excelRecordCount = 30000;
    	int fileNum = 0;
    	try {
    		List list = new ArrayList();
        	long step = 0;
        	//取得业务数据
        	list = payChannelRotationLogDAO.getPayChannelRotationLogList(flag,payChannelRotationLog,step,step+excelRecordCount);
        	while(list.size()==excelRecordCount){
        		//加入Excel
        		writeListToExcel(list, templetFile, new File(tmpFile.getAbsolutePath()+"/"+(fileNum++)+".xls"));
        		step = step + excelRecordCount;
        		list = payChannelRotationLogDAO.getPayChannelRotationLogList(flag,payChannelRotationLog,step,step+excelRecordCount);
        	}
        	//加入结尾的记录到Excel
        	if(list.size()<excelRecordCount){
        		writeListToExcel(list, templetFile, new File(tmpFile.getAbsolutePath()+"/"+(fileNum++)+".xls"));
        	}
        	File zipfile=new File(JWebConstant.APP_PATH+"/dat/download/"+
            		new SimpleDateFormat("yyyy-MM-dd").format(new Date())+"_"+randomName+".zip");
        	File srcfile[] = new File[fileNum];
        	for(int i=0; i<srcfile.length; i++)srcfile[i] = new File(tmpFile.getAbsolutePath()+"/"+(i)+".xls");
        	Tools.zipFiles(srcfile, zipfile);
			FileInputStream fis = new FileInputStream(zipfile);
			byte [] b = new byte[fis.available()]; 
			fis.read(b);
			fis.close();
			Tools.deleteFile(tmpFile);
			Tools.deleteFile(zipfile);
			return b;
		} catch (Exception e) {
			 e.printStackTrace();
		}
    	return null;
	}
	private void writeListToExcel(List list,File templetFile,File tmpFile){
    	Workbook rw = null;
		WritableWorkbook wwb = null; 
		WritableSheet ws = null;
		try {
			if (!Tools.copy(templetFile.getAbsolutePath(), tmpFile.getAbsolutePath())) return;
			rw = jxl.Workbook.getWorkbook(tmpFile);
			wwb = Workbook.createWorkbook(tmpFile, rw);
			ws = wwb.getSheet(0);
			writeData(list, ws);
			wwb.write();
			wwb.close();
			rw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
	//循环输入数据
	public void writeData(List list,WritableSheet ws){
		for (int i=0; i < list.size(); i++) {
			PayChannelRotationLog payChannelRotationLog = (PayChannelRotationLog)list.get(i);
			if (payChannelRotationLog.channelId != null && !"".equals(payChannelRotationLog.channelId)) setCellValue(ws, 0, i+1, payChannelRotationLog.channelId);
			if (payChannelRotationLog.channelAcc != null && !"".equals(payChannelRotationLog.channelAcc)) setCellValue(ws, 1, i+1, payChannelRotationLog.channelAcc);
			if (payChannelRotationLog.day != null && !"".equals(payChannelRotationLog.day)) setCellValue(ws, 2, i+1, payChannelRotationLog.day);
			if (payChannelRotationLog.dayAmt != null && !"".equals(payChannelRotationLog.dayAmt)) setCellValue(ws, 3, i+1, String.format("%.2f",(float)payChannelRotationLog.dayAmt*0.01));
			if (payChannelRotationLog.daySuccessAmt != null && !"".equals(payChannelRotationLog.daySuccessAmt)) setCellValue(ws, 4, i+1, String.format("%.2f",(float)payChannelRotationLog.daySuccessAmt*0.01));
			if (payChannelRotationLog.dayCount != null && !"".equals(payChannelRotationLog.dayCount)) setCellValue(ws, 5, i+1, payChannelRotationLog.dayCount.toString());
			if (payChannelRotationLog.daySuccessCount != null && !"".equals(payChannelRotationLog.daySuccessCount)) setCellValue(ws, 6, i+1, payChannelRotationLog.daySuccessCount.toString());
			if (payChannelRotationLog.channelFee != null && !"".equals(payChannelRotationLog.channelFee)) setCellValue(ws, 7, i+1, String.format("%.2f",(float)payChannelRotationLog.channelFee*0.01));
		}
	}
  	private void setCellValue(WritableSheet writeSheet, int x, int y,
			String value) {
		try {
			WritableCell cell = writeSheet.getWritableCell(x, y);
			if (cell.getType() == CellType.EMPTY) {
				Label write = new Label(x, y, value);
				writeSheet.addCell(write);
			} else {
				Label write = (Label) cell;
				write.setString(value);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}