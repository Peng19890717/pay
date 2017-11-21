package com.pay.coopbank.service;

import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

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
import util.PayUtil;
import util.Tools;

import com.PayConstant;
import com.pay.coopbank.dao.PayChannelRotation;
import com.pay.coopbank.dao.PayChannelRotationDAO;
import com.pay.merchant.dao.PayMerchant;
import com.pay.merchant.dao.PayMerchantDAO;
import com.pay.merchantinterface.service.PayRequest;
import com.pay.order.dao.PayOrder;

/**
 * Object PAY_CHANNEL_ROTATION service. 
 * @author Administrator 
 *
 */
public class PayChannelRotationService {
	private static final Log log = LogFactory.getLog(PayChannelRotationService.class);
    /**
     * Get records list(json). 
     * @return
     */
    public String getPayChannelRotationList(PayChannelRotation payChannelRotation,int page,int rows,String sort,String order){
        try {
            PayChannelRotationDAO payChannelRotationDAO = new PayChannelRotationDAO();
            List list = payChannelRotationDAO.getPayChannelRotationList(payChannelRotation, page, rows, sort, order);
            JSONObject json = new JSONObject();
            json.put("total", String.valueOf(payChannelRotationDAO.getPayChannelRotationCount(payChannelRotation)));
            JSONArray row = new JSONArray();
            for(int i = 0; i<list.size(); i++){
                row.put(i, ((PayChannelRotation)list.get(i)).toJson());
            }
            json.put("rows", row);
            return json.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
    /**
     * add PayChannelRotation
     * @param payChannelRotation
     * @throws Exception
     */
    public void addPayChannelRotation(HttpServletRequest request) throws Exception {
    	String type = request.getParameter("type");
    	String channelId = request.getParameter("channelId");
    	Long maxSumAmt = 0l;
    	try {
    		maxSumAmt = Long.parseLong(request.getParameter("maxSumAmt"))*100l;
		} catch (Exception e) {}
    	String accInfos = request.getParameter("accInfos");
    	if(accInfos==null)return;
    	String [] accs = accInfos.replaceAll("：",":").replaceAll(" ","").replaceAll("	", "").split("\n");
    	Map<String,String> map = new HashMap<String,String>();
    	for(int i=0; i<accs.length; i++){
    		String [] tmp = accs[i].split(":");
    		if(tmp.length != 2)continue;
    		map.put(tmp[0].trim(),tmp[1].trim());
    	}
        new PayChannelRotationDAO().addPayChannelRotation(type,channelId,maxSumAmt,map);
    }
    /**
     * remove PayChannelRotation
     * @param id
     * @throws Exception
     */
    public void removePayChannelRotation(String id) throws Exception {
        new PayChannelRotationDAO().removePayChannelRotation(id);
    }
    /**
     * update PayChannelRotation
     * @param payChannelRotation
     * @throws Exception
     */
//    public void updatePayChannelRotation(PayChannelRotation payChannelRotation) throws Exception {
//        new PayChannelRotationDAO().updatePayChannelRotation(payChannelRotation);
//    }
    /**
     * detail PayChannelRotation
     * @param id
     * @throws Exception
     */
    public PayChannelRotation detailPayChannelRotation(String id) throws Exception {
        return new PayChannelRotationDAO().detailPayChannelRotation(id);
    }
	public void updateBatchStatusChannelRotation(String status,String batchNo) throws Exception {
		new PayChannelRotationDAO().updateBatchStatusChannelRotation(status,batchNo);
	}
	public void updatePayChannelRotationStatus(String id,String status) throws Exception {
		new PayChannelRotationDAO().updatePayChannelRotationStatus(id,status);
	}
	/**
	 * 取得渠道最大限额
	 * @param channelId
	 * @return
	 * @throws Exception
	 */
	public long getChannelMaxLimitAmt(String channelId) throws Exception {
		return new PayChannelRotationDAO().getChannelMaxLimitAmt(channelId);
	}
	private static Map<String, String> APPLICATION_PAY_TYPE = new HashMap<String, String>();
	static {
		APPLICATION_PAY_TYPE.put("WeiXinScanOrder", "07");
		APPLICATION_PAY_TYPE.put("WeiXinWapOrder", "10");
		APPLICATION_PAY_TYPE.put("ZFBScanOrder", "11");
		APPLICATION_PAY_TYPE.put("QQScanOrder", "12");
	}
	/**
	 * 通过规则取得渠道账号
	 * @param payRequest
	 * @return
	 * @throws Exception
	 */
	public PayChannelRotation getPayChannelRotationByRule(PayRequest payRequest){
		try {
			//解析轮询的渠道,ZXBJ;ZFT
			if(PayConstant.PAY_CONFIG.get("CHANNEL_ROTATION_CNL_INFO")==null)return null;
			//CHANNEL_ROTATION_CNL_INFO      ZXBJ-07,12;ZFT-07,11
			String [] tmp = PayConstant.PAY_CONFIG.get("CHANNEL_ROTATION_CNL_INFO").
					replaceAll("；",";").replaceAll("—","-").replaceAll("，",",").split(";");
			//支付方式，00 支付账户  01 网上银行 03 快捷支付 07微信扫码 10微信WAP、11支付宝扫码、12QQ扫码
			//检查渠道是否支持该种支付方式
			List<String> list = new ArrayList<String>();
			for(int i=0; i<tmp.length;i++){
				String [] tt = tmp[i].split("-");
				if((","+tt[1]+",").indexOf(","+APPLICATION_PAY_TYPE.get(payRequest.application)+",")>=0)list.add(tt[0]);
			}
			if(list.size()==0)return null;
			String [] pcRotation = new String[list.size()];
			for(int i=0; i<list.size(); i++)pcRotation[i] = list.get(i);
			PayChannelRotationDAO dao = new PayChannelRotationDAO();
			String day = new java.text.SimpleDateFormat("yyyyMMdd").format(new Date());
			//顺序
			if("0".equals(PayConstant.PAY_CONFIG.get("CHANNEL_ROTATION_CNL_RULE"))){
				return dao.getPayChannelRotationByRule0(day,pcRotation,payRequest.payOrder.txamt);
			//闲置最长时间
			} else if("1".equals(PayConstant.PAY_CONFIG.get("CHANNEL_ROTATION_CNL_RULE"))){
				return dao.getPayChannelRotationByRule1(day,pcRotation,payRequest.payOrder.txamt);
			//选择最小额度
			} else if("2".equals(PayConstant.PAY_CONFIG.get("CHANNEL_ROTATION_CNL_RULE"))){
				return dao.getPayChannelRotationByRule2(day,pcRotation,payRequest.payOrder.txamt);
			}
	        return null;
		} catch (Exception e) {
			log.info(PayUtil.exceptionToString(e));
			return null;
		}
    }
	
	
	
	/**
	 * excel导出
	 * @param payChannelRotation
	 * @return
	 */
	public byte[] exportExcelForPayChannelRotationList(PayChannelRotation payChannelRotation) {
		PayChannelRotationDAO payChannelRotationDAO = new PayChannelRotationDAO();
    	String randomName = Tools.getUniqueIdentify();
    	//临时目录
    	File tmpFile = new File(JWebConstant.APP_PATH+"/dat/download/"+randomName);
    	tmpFile.mkdir();
    	//文件模板名称
    	String templetName ="channelRotation";
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
        	list = payChannelRotationDAO.getPayChannelRotationList(payChannelRotation,step,step+excelRecordCount);
        	while(list.size()==excelRecordCount){
        		//加入Excel
        		writeListToExcel(list,templetFile,new File(tmpFile.getAbsolutePath()+"/"+(fileNum++)+".xls"));
        		step = step + excelRecordCount;
        		//取得业务数据
        		list = payChannelRotationDAO.getPayChannelRotationList(payChannelRotation,step,step+excelRecordCount);
        	}
        	//加入结尾的记录到Excel
        	if(list.size()<excelRecordCount){
        		writeListToExcel(list,templetFile,new File(tmpFile.getAbsolutePath()+"/"+(fileNum++)+".xls"));
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
  			PayChannelRotation payChannelRotation = (PayChannelRotation)list.get(i);
  			if(payChannelRotation.channelId != null && !"".equals(payChannelRotation.channelId)){
  				
  				setCellValue(ws, 0, i + 1,payChannelRotation.channelId);
  				if(PayCoopBankService.CHANNEL_MAP_ALL.get((payChannelRotation.channelId)) != null){
  					String bankname=PayCoopBankService.CHANNEL_MAP_ALL.get(payChannelRotation.channelId).bankName;
  					setCellValue(ws, 1, i + 1,bankname);
  				}else{
  				setCellValue(ws, 1, i + 1,payChannelRotation.channelId);
  				}
  			}
  			if(payChannelRotation.merchantId != null && !"".equals(payChannelRotation.merchantId ))setCellValue(ws, 2, i + 1,payChannelRotation.merchantId);
  			if(payChannelRotation.md5Key != null && !"".equals(payChannelRotation.md5Key))setCellValue(ws, 3, i + 1,payChannelRotation.md5Key);
  			if(payChannelRotation.createTime != null)setCellValue(ws, 4, i + 1,new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payChannelRotation.createTime));
  			if(payChannelRotation.lastSucUsedTime != null && !"".equals(payChannelRotation.lastSucUsedTime))setCellValue(ws, 5, i + 1,
  					new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payChannelRotation.lastSucUsedTime));
  			setCellValue(ws, 6, i + 1,payChannelRotation.merchantId+":"+payChannelRotation.md5Key);
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