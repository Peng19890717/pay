package com.pay.settlement.service;

import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import com.pay.custstl.service.PayCustStlInfoService;
import com.pay.merchant.dao.PayMerchant;
import com.pay.merchantinterface.dao.PayInterfaceDAO;
import com.pay.settlement.dao.PayMerchantSettlement;
import com.pay.settlement.dao.PayMerchantSettlementDAO;

/**
 * Object PAY_MERCHANT_SETTLEMENT service. 
 * @author Administrator
 *
 */
public class PayMerchantSettlementService {
	private static final Log log = LogFactory.getLog(PayMerchantSettlementService.class);
    /**
     * Get records list(json).
     * @return
     */
    public String getPayMerchantSettlementList(PayMerchantSettlement payMerchantSettlement,int page,int rows,String sort,String order){
        try {
            PayMerchantSettlementDAO payMerchantSettlementDAO = new PayMerchantSettlementDAO();
            Map sysUserMap = new HashMap();
            List list = payMerchantSettlementDAO.getPayMerchantSettlementList(payMerchantSettlement,
            		page, rows, sort, order,sysUserMap);
            JSONObject json = new JSONObject();
            json.put("total", String.valueOf(payMerchantSettlementDAO.getPayMerchantSettlementCount(payMerchantSettlement)));
            JSONArray row = new JSONArray();
            for(int i = 0; i<list.size(); i++){
                row.put(i, ((PayMerchantSettlement)list.get(i)).toJson(sysUserMap));
            }
            json.put("rows", row);
            //查询总金额
            Long[] money=payMerchantSettlementDAO.getTotalSettlementMoney(payMerchantSettlement);
            json.put("totalApplyCount", String.valueOf(money[0]));
            json.put("totalApplyAmt", String.valueOf(money[1]));
            json.put("totalRefundCount", String.valueOf(money[2]));
            json.put("totalRefundAmt", String.valueOf(money[3]));
            json.put("totalFee", String.valueOf(money[4]));
            json.put("totalNetreAmt", String.valueOf(money[5]));
            return json.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
//    /**
//     * add PayMerchantSettlement
//     * @param payMerchantSettlement
//     * @throws Exception
//     */
//    public void addPayMerchantSettlement(PayMerchantSettlement payMerchantSettlement) throws Exception {
//        new PayMerchantSettlementDAO().addPayMerchantSettlement(payMerchantSettlement);
//    }
    /**
     * recheck PayMerchantSettlement
     * @param id
     * @throws Exception
     */
    public void reCheckSettlement(PayMerchantSettlement payMerchantSettlement) throws Exception {
        new PayMerchantSettlementDAO().reCheckSettlement(payMerchantSettlement);
    }
    /**
     * set result PayMerchantSettlement
     * @param id
     * @throws Exception
     */
    public void setResultSettlement(PayMerchantSettlement stl) throws Exception {
    	PayMerchantSettlementDAO stlDAO = new PayMerchantSettlementDAO();
    	PayMerchantSettlement stlTmp = stlDAO.detailPayMerchantSettlement(stl.stlId);
    	PayMerchant mer = new PayInterfaceDAO().getCertByMerchantId(stlTmp.stlMerId);
    	stlTmp.stlStatus = stl.stlStatus;
    	//交易手续费收取方式，0结算收取 1预存手续费收取
		if("1".equals(mer.chargeWay)) {
//			stl.stlNetrecamt = stlTmp.stlNetrecamt+stlTmp.stlFee;
//			stlTmp.stlNetrecamt = stlTmp.stlNetrecamt+stlTmp.stlFee;
		} else {
//			stl.stlNetrecamt = stlTmp.stlNetrecamt;
		}
    	stlDAO.setResultSettlement(stlTmp);
        //结算方式 0自动结算到虚拟账户 1线下打款 2手动结算到虚拟账户 
        if("2".equals(mer.settlementWay)){
        	PayAutoSettlementListener astl = new PayAutoSettlementListener();
        	if(stlTmp.stlNetrecamt==0)stlTmp.stlNetrecamt = stlTmp.stlAmtOver;
        	astl.stlToAccProfile(mer,stlTmp);
        }
    }
    /**
     * apply PayMerchantSettlement
     * @param stlIds
     * @param payMerchantSettlement
     * @throws Exception
     */
    public void applyPayMerchantSettlement(String stlIds,
    		PayMerchantSettlement payMerchantSettlement) throws Exception {
    	if(stlIds==null || stlIds.length() == 0)return;
    	PayMerchantSettlementDAO stlDAO = new PayMerchantSettlementDAO();
    	String [] stlId = stlIds.split(",");
    	//清算设置
    	stlDAO.applyPayMerchantSettlement(stlId,payMerchantSettlement);
    }
    /**
     * reApply PayMerchantSettlement
     * @param stlIds
     * @param payMerchantSettlement
     * @throws Exception
     */
    public void reApplyPayMerchantSettlement(String stlIds,
    		PayMerchantSettlement payMerchantSettlement) throws Exception {
    	if(stlIds==null || stlIds.length() == 0)return;
    	String [] stlId = stlIds.split(",");
        new PayMerchantSettlementDAO().reApplyPayMerchantSettlement(stlId,payMerchantSettlement);
    }
    /**
     * detail PayMerchantSettlement
     * @param stlId
     * @throws Exception
     */
    public PayMerchantSettlement detailPayMerchantSettlement(String stlId) throws Exception {
        return new PayMerchantSettlementDAO().detailPayMerchantSettlement(stlId);
    }
	/**
	 * 导出列表
	 * @param payOrder
	 * @return
	 */
	public byte[] exportExcelForPayMerchantSettlementList(PayMerchantSettlement payMerchantSettlement) {
		 PayMerchantSettlementDAO payMerchantSettlementDAO = new PayMerchantSettlementDAO();
		 Map sysUserMap = new HashMap();
    	String randomName = Tools.getUniqueIdentify();
    	//临时目录
    	File tmpFile = new File(JWebConstant.APP_PATH+"/dat/download/"+randomName);
    	tmpFile.mkdir();
    	//文件模板名称
    	String templetName ="payMerchantSettlement";
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
        	list = payMerchantSettlementDAO.getPayMerchantSettlementList_excel(payMerchantSettlement, step, step + excelRecordCount, null, null, sysUserMap);
        	while(list.size()==excelRecordCount){
        		//加入Excel
        		writeListToExcel(list,templetFile,new File(tmpFile.getAbsolutePath()+"/"+(fileNum++)+".xls"));
        		step = step + excelRecordCount;
        		//取得业务数据
        		list = payMerchantSettlementDAO.getPayMerchantSettlementList_excel(payMerchantSettlement, step, step + excelRecordCount, null, null, sysUserMap);
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
  			PayMerchantSettlement payMerchantSettlement = (PayMerchantSettlement)list.get(i);
  			if(payMerchantSettlement.stlId!=null && !"".endsWith(payMerchantSettlement.stlId))setCellValue(ws, 0, i + 1,payMerchantSettlement.stlId);
  			if(payMerchantSettlement.stlMerId!=null && !"".endsWith(payMerchantSettlement.stlMerId))setCellValue(ws, 1, i + 1,payMerchantSettlement.stlMerId);
  			if(payMerchantSettlement.merchant.storeName!=null && !"".endsWith(payMerchantSettlement.merchant.storeName))setCellValue(ws, 2, i + 1,payMerchantSettlement.merchant.storeName);
  			if(payMerchantSettlement.stlFromTime!=null)setCellValue(ws, 3, i + 1,new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payMerchantSettlement.stlFromTime));
  			if(payMerchantSettlement.stlToTime!=null)setCellValue(ws, 4, i + 1,new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payMerchantSettlement.stlToTime));
  			if(payMerchantSettlement.stlApplDate!=null)setCellValue(ws, 5, i + 1,new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payMerchantSettlement.stlApplDate));
  			if(payMerchantSettlement.stlApplystlCount!=null)setCellValue(ws, 6, i + 1,payMerchantSettlement.stlApplystlCount.toString());
  			if(payMerchantSettlement.stlApplystlamt!=null)setCellValue(ws, 7, i + 1,String.format("%.2f",((double)payMerchantSettlement.stlApplystlamt)*0.01));
  			if(payMerchantSettlement.stlRefundCount!=null)setCellValue(ws, 8, i + 1,payMerchantSettlement.stlRefundCount.toString());
  			if(payMerchantSettlement.stlRefundAmt!=null)setCellValue(ws, 9, i + 1,String.format("%.2f",((double)payMerchantSettlement.stlRefundAmt)*0.01));
  			if(payMerchantSettlement.stlFee!=null)setCellValue(ws, 10, i + 1,String.format("%.2f",((double)payMerchantSettlement.stlFee)*0.01));
  			if(payMerchantSettlement.stlNetrecamt!=null)setCellValue(ws, 11, i + 1,String.format("%.2f",((double)payMerchantSettlement.stlNetrecamt)*0.01));
  			if(payMerchantSettlement.stlPeriod!=null && !"".equals(payMerchantSettlement.stlPeriod)){
  				if(payMerchantSettlement.stlPeriod.equalsIgnoreCase("D")) setCellValue(ws, 12, i + 1,"日结");
  				if(payMerchantSettlement.stlPeriod.equalsIgnoreCase("W")) setCellValue(ws, 12, i + 1,"周结");
  			}
  			if(payMerchantSettlement.stlPeriodDaySet!=null && !"".equals(payMerchantSettlement.stlPeriodDaySet)){
  				setCellValue(ws, 13, i + 1,"D".equals(payMerchantSettlement.stlPeriod)?"T+"+payMerchantSettlement.stlPeriodDaySet:
  		    	PayCustStlInfoService.getShowInfoByStlPeriod(payMerchantSettlement.stlPeriod,payMerchantSettlement.stlPeriodDaySet));
  			}
  			if(payMerchantSettlement.stlStatus!=null && !"".equals(payMerchantSettlement.stlStatus)){
  				if(payMerchantSettlement.stlStatus.equalsIgnoreCase("0")) setCellValue(ws, 14, i + 1,"初始化状态");
				if(payMerchantSettlement.stlStatus.equalsIgnoreCase("1")) setCellValue(ws, 14, i + 1,"待审核状态");
				if(payMerchantSettlement.stlStatus.equalsIgnoreCase("2")) setCellValue(ws, 14, i + 1,"审核通过状态");
				if(payMerchantSettlement.stlStatus.equalsIgnoreCase("3")) setCellValue(ws, 14, i + 1,"审核未通过状态");
				if(payMerchantSettlement.stlStatus.equalsIgnoreCase("4")) setCellValue(ws, 14, i + 1,"结算成功状态");
				if(payMerchantSettlement.stlStatus.equalsIgnoreCase("5")) setCellValue(ws, 14, i + 1,"结算失败状态");
  			}
  			if(payMerchantSettlement.merchant.payCustStlInfo.depositBankCode!=null && !"".equals(payMerchantSettlement.merchant.payCustStlInfo.depositBankCode))
  				setCellValue(ws, 15, i + 1,payMerchantSettlement.merchant.payCustStlInfo.depositBankCode);
  			if(payMerchantSettlement.merchant.payCustStlInfo.depositBankBrchName!=null && !"".equals(payMerchantSettlement.merchant.payCustStlInfo.depositBankBrchName))
  				setCellValue(ws, 16, i + 1,payMerchantSettlement.merchant.payCustStlInfo.depositBankBrchName);
  			if(payMerchantSettlement.merchant.payCustStlInfo.custStlBankAcNo!=null && !"".equals(payMerchantSettlement.merchant.payCustStlInfo.custStlBankAcNo))
  				setCellValue(ws, 17, i + 1,payMerchantSettlement.merchant.payCustStlInfo.custStlBankAcNo);
  			if(payMerchantSettlement.stlAutoFlag!=null && !"".equals(payMerchantSettlement.stlAutoFlag)){
  				if(payMerchantSettlement.stlAutoFlag.equalsIgnoreCase("0")) setCellValue(ws, 18, i + 1,"自动");
  				if(payMerchantSettlement.stlAutoFlag.equalsIgnoreCase("1")) setCellValue(ws, 18, i + 1,"手动");
  			}
  			if(payMerchantSettlement.remark!=null && !"".equals(payMerchantSettlement.remark)){
  				setCellValue(ws, 19, i + 1,payMerchantSettlement.remark);
  			}
  			if(payMerchantSettlement.stlApplicants!=null && !"".equals(payMerchantSettlement.stlApplicants)){
  				setCellValue(ws, 20, i + 1,payMerchantSettlement.stlApplicants);
  			}
  			if(payMerchantSettlement.stlApplTime!=null){
  				setCellValue(ws, 21, i + 1,new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payMerchantSettlement.stlApplTime));
  			}
  			if(payMerchantSettlement.stlAuditPer!=null && !"".equals(payMerchantSettlement.stlAuditPer)){
  				PayMerchantSettlementDAO paymerchantsettlementdao = new PayMerchantSettlementDAO();
  				String userName = paymerchantsettlementdao.select_user_messageByID(payMerchantSettlement.stlAuditPer);
  				if(userName!=null && !"".equals(userName)) setCellValue(ws, 22, i + 1,userName);
  			}
  			if(payMerchantSettlement.stlAuditTime!=null){
  				setCellValue(ws, 23, i + 1,new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payMerchantSettlement.stlAuditTime));
  			}
  			if(payMerchantSettlement.stlSucTime!=null){
  				setCellValue(ws, 24, i + 1,new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payMerchantSettlement.stlSucTime));
  			}
  			if(payMerchantSettlement.stlSucOperator!=null && !"".equals(payMerchantSettlement.stlSucOperator)){
  				PayMerchantSettlementDAO paymerchantsettlementdao = new PayMerchantSettlementDAO();
  				String userName = paymerchantsettlementdao.select_user_messageByID(payMerchantSettlement.stlSucOperator);
  				if(userName!=null && !"".equals(userName)) setCellValue(ws, 25, i + 1,userName);
  			}
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