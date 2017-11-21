package com.pay.refund.service;

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

import com.PayConstant;
import com.jweb.service.TransactionService;
import com.pay.merchant.dao.PayAccProfile;
import com.pay.merchant.dao.PayMerchant;
import com.pay.merchant.dao.PayMerchantDAO;
import com.pay.merchantinterface.dao.PayInterfaceDAO;
import com.pay.merchantinterface.service.PayOrderInterfaceService;
import com.pay.order.dao.PayProductOrder;
import com.pay.order.dao.PayProductOrderDAO;
import com.pay.refund.dao.PayRefund;
import com.pay.refund.dao.PayRefundDAO;
import com.pay.risk.service.PayRiskDayTranSumService;

/**
 * Object PAY_REFUND service. 
 * @author Administrator
 *
 */
public class PayRefundService extends TransactionService{
	private static final Log log = LogFactory.getLog(PayRefundService.class);
    /**
     * Get records list(json).查询退款订单列表
     * @return
     */
    public String getPayRefundList(PayRefund payRefund,int page,int rows,String sort,String order){
        try {
        	//调用持久层
            PayRefundDAO payRefundDAO = new PayRefundDAO();
            List list = payRefundDAO.getPayRefundList(payRefund, page, rows, sort, order);
            JSONObject json = new JSONObject();
            json.put("total", String.valueOf(payRefundDAO.getPayRefundCount(payRefund)));
            JSONArray row = new JSONArray();
            for(int i = 0; i<list.size(); i++){
                row.put(i, ((PayRefund)list.get(i)).toJson());
            }
            json.put("rows", row);
            return json.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
    /**
     * 查询退款详情
     * @param payRefund
     * @return
     */
	public PayRefund getPayRefundDetail(PayRefund payRefund) {
		try {
			PayRefundDAO payRefundDAO=new PayRefundDAO();
			PayRefund refund=new PayRefund();
            //调用持久层
			refund = payRefundDAO.getPayRefundDetail(payRefund);
            return refund;
        } catch (Exception e) {
            e.printStackTrace();
        }
	 return null;
	}
    /**
     * 导出退款列表，zip文件，其中有多个Excel
     * @param payRefund 条件
     * @return
     */
    public byte [] exportExcel(PayRefund payRefund){
    	PayRefundDAO payRefundDAO = new PayRefundDAO();
    	String randomName = Tools.getUniqueIdentify();
    	//临时目录
    	File tmpFile = new File(JWebConstant.APP_PATH+"/dat/download/"+randomName);
    	tmpFile.mkdir();
    	//文件模板名称
    	String templetName ="refund";
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
        	list = payRefundDAO.getPayRefundList(payRefund,step,step+excelRecordCount);
        	while(list.size()==excelRecordCount){
        		//加入Excel
        		writeListToExcel(list,templetFile,new File(tmpFile.getAbsolutePath()+"/"+(fileNum++)+".xls"));
        		step = step + excelRecordCount;
        		//取得业务数据
        		list = payRefundDAO.getPayRefundList(payRefund,step,step+excelRecordCount);
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
  			PayRefund refund = (PayRefund)list.get(i);
  			if(refund.refordno != null && !"".equals(refund.refordno))setCellValue(ws, 0, i + 1,refund.refordno);
  			if(refund.oriprdordno != null && !"".equals(refund.oriprdordno))setCellValue(ws, 1, i + 1,refund.oriprdordno);
  			if(refund.merno != null && !"".equals(refund.merno))setCellValue(ws, 2, i + 1,refund.merno);
  			if(refund.rfamt != null)setCellValue(ws, 3, i + 1,String.valueOf(refund.rfamt));
  			if(refund.banksts != null && !"".equals(refund.banksts))setCellValue(ws, 4, i + 1,refund.banksts);
  			if(refund.custId != null && !"".equals(refund.custId))setCellValue(ws, 5, i + 1,refund.custId);
  			if(refund.rfordtime != null)setCellValue(ws, 6, i + 1,new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(refund.rfordtime));
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
	public void setResultRefund(PayRefund payRefund) throws Exception {
		PayRefundDAO dao = new PayRefundDAO();
		PayOrderInterfaceService pois = new PayOrderInterfaceService();
		PayRiskDayTranSumService prtss = new PayRiskDayTranSumService();
		PayProductOrderDAO productOrderDAO = new PayProductOrderDAO();
		try {
			transaction.beignTransaction(dao,pois,prtss,productOrderDAO);
			PayRefund refundTmp = dao.getPayRefundById(payRefund.refordno);
			PayMerchant merchant = new PayInterfaceDAO().getCertByMerchantId(refundTmp.merno);
			//设置退款状态  银行网关状态，00: 退款处理中 01:退款成功  02：退款失败，默认00
			if(dao.setResultRefund(payRefund)>=0){
				if("01".equals(payRefund.banksts)){//退款成功
					PayProductOrder prdOrder = productOrderDAO.getProductOrderById(refundTmp.merno, refundTmp.oriprdordno);
					//退款成功，更新清算信息，已结算的金额扣除保证金
					if(refundTmp !=null && prdOrder != null){
//						pois.updateSettlementForRefundResult(prdOrder,refundTmp);//修改结算信息
						prtss.updateRiskSuccessRefundSum(refundTmp);
					}
				} else {//退款失败，会还到虚拟账户
					if("D".equals(merchant.custSetPeriod)&&"0".equals(merchant.custSetFrey)){//实时结算退款
						PayAccProfile payAccProfile = new PayAccProfile();
						payAccProfile.acType=PayConstant.CUST_TYPE_MERCHANT;
						payAccProfile.payAcNo = merchant.custId;
						payAccProfile.cashAcBal = 0-refundTmp.rfamt;
						payAccProfile.consAcBal = 0-refundTmp.rfamt;
						new com.pay.merchant.dao.PayAccProfileDAO().updatePayAccProfileForRefund(payAccProfile);
					} else {
						if("2".equals(refundTmp.stlsts)){//已经结算过，但是失败了，把钱退回虚拟账户
							PayAccProfile payAccProfile = new PayAccProfile();
							payAccProfile.acType=PayConstant.CUST_TYPE_MERCHANT;
							payAccProfile.payAcNo = merchant.custId;
							payAccProfile.cashAcBal = 0-refundTmp.rfamt;
							payAccProfile.consAcBal = 0-refundTmp.rfamt;
							try {
								PayAccProfile payAccProfileTmp = new com.pay.merchant.dao.PayAccProfileDAO().detailPayAccProfileByAcTypeAndAcNo("1",merchant.custId);
					        	if(payAccProfileTmp!=null)log.info(payAccProfileTmp.toString().replaceAll("\n",";"));
							} catch (Exception e) {}
							new com.pay.merchant.dao.PayAccProfileDAO().updatePayAccProfileForRefund(payAccProfile);
						}
					}
				}
			}
			transaction.endTransaction();
		} catch (Exception e) {
			transaction.rollback();
			throw e;
		}
	}
}