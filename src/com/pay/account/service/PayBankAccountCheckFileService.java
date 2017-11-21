package com.pay.account.service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import jxl.Sheet;
import jxl.Workbook;

import com.PayConstant;
import com.pay.account.dao.PayBankAccountCheckDAO;
import com.pay.account.dao.PayBankAccountCheckTmp;


/**
 * 读取对账文件，根据银行标识
 * @author Administrator
 *
 */
public class PayBankAccountCheckFileService {
	private String bankCode = "";
	public PayBankAccountCheckFileService(){}
	public static boolean checkFlagRun = false;
	public PayBankAccountCheckFileService(String bankCode){
		this.bankCode = bankCode;
	}
	public void readFile(String filePath) throws Exception {
		if(checkFlagRun)throw new Exception("对账程序正在运行，请稍后再试");
		checkFlagRun = true;
		try {
			if("ICBC".equals(bankCode))readFileICBC();//读取工行对账文件
			else if(PayConstant.PAY_CONFIG.get("PAY_CHANNEL_XF").equals(bankCode))readFileXFPay(filePath);//读取先锋对账文件
			else if(PayConstant.PAY_CONFIG.get("PAY_CHANNEL_RB").equals(bankCode))readFileRBPay(filePath);//读取先锋对账文件
		} catch (Exception e) {
			throw e;
		} finally {
			checkFlagRun = false;
		}
	}
	/**
	 * 读取工商银行对账文件，加入到数据库缓存PAY_BANK_ACCOUNT_CHECK_TMP中
	 */
	public void readFileICBC(){
		
	}
	/**
	 * 读取先锋对账文件
	 * @param file
	 * @throws Exception 
	 */
	public void readFileXFPay(String filePath) throws Exception {
		if(filePath == null)return;
		File file = new File(filePath);
		if(!file.exists())return;
		List<PayBankAccountCheckTmp> list=new ArrayList<PayBankAccountCheckTmp>();
		try {
			Workbook wk=Workbook.getWorkbook(file);
			Sheet sheet = wk.getSheet(0);
			//总记录数
			int totalCount = sheet.getRows()-3;
			for (int i = 0; i < totalCount; i++) {
				//只能是某一天的数据
				PayBankAccountCheckTmp tmp=new PayBankAccountCheckTmp();
				String statusStr = sheet.getCell(9, 3+i).getContents().trim();
				if(statusStr.equals("成功"))tmp.setStatus("01");
				else continue;
				//设置支付渠道
				tmp.setBankcode(PayConstant.PAY_CONFIG.get("PAY_CHANNEL_XF"));
				//设置对账日期(2016-01-13 11:34:12)
				try {
					String d = sheet.getCell(7, 3+i).getContents().trim().split(" ")[0].replaceAll("-","");
					if(d.length() != 8)throw new Exception();
					tmp.setActdate(d);
				} catch (Exception e) {
					try {
						String d = sheet.getCell(6, 3+i).getContents().trim().split(" ")[0].replaceAll("-","");
						if(d.length() != 8)throw new Exception();
						tmp.setActdate(d);
					} catch (Exception e2) {}
				}
				//设置银行网关订单号
				tmp.setBnkordno(sheet.getCell(1, 3+i).getContents().trim());
				//设置明细类型
				String chktype = sheet.getCell(3, 3+i).getContents().trim();
				if(chktype!=null && chktype.contains("支付")){
					tmp.setChktype("1");
				}else if(chktype!=null && chktype.contains("退款")){
					tmp.setChktype("2");
				}
				//设置交易额
				tmp.setTxnamt(new Double((Double.parseDouble(sheet.getCell(4, 3+i).getContents().trim()))*100).longValue());
				//设置手续费
				tmp.setFee(new Double((Double.parseDouble(sheet.getCell(5, 3+i).getContents().trim()))*100).longValue());
				//设置净收金额
				tmp.setInamt(new Double((Double.parseDouble(sheet.getCell(4, 3+i).getContents().trim()))*100).longValue());
				//设置银行流水号
				tmp.setBanklogno(sheet.getCell(0, 3+i).getContents().trim());
				list.add(tmp);
			}
			//accountForXFPay(list);
			//插入对账记录到临时表
			PayBankAccountCheckDAO accDao = new PayBankAccountCheckDAO();
			//清除对账临时表
			accDao.clearPayBankAccountCheckTmp();
			List tmp = new ArrayList();
			//插入对账信息
			for(int i = 0; i<list.size(); i++){
				PayBankAccountCheckTmp t=(PayBankAccountCheckTmp)list.get(i);
				tmp.add(t);
				if(tmp.size()==1000){
					accDao.addPayBankAccountCheckBatch(tmp);
					tmp = new ArrayList();
				}
			}
			accDao.addPayBankAccountCheckBatch(tmp);
			//开始对账
			new PayBankAccountCheckService().checkAccount(PayConstant.PAY_CONFIG.get("PAY_CHANNEL_XF"));
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("对账处理错误（"+e.getMessage()+"）");
		} 
	}
	/**
	 * 读取融宝对账文件
	 * @param file
	 * @throws Exception 
	 */
	public void readFileRBPay(String filePath) throws Exception {
		if(filePath == null)return;
		File file = new File(filePath);
		if(!file.exists())return;
		List<PayBankAccountCheckTmp> list=new ArrayList<PayBankAccountCheckTmp>();
		try {
			Workbook wk=Workbook.getWorkbook(file);
			Sheet sheet = wk.getSheet(0);
			//总记录数
			int totalCount = sheet.getRows()-3;
			for (int i = 0; i < totalCount; i++) {
				//只能是某一天的数据
				PayBankAccountCheckTmp tmp=new PayBankAccountCheckTmp();
				String statusStr = sheet.getCell(3, 3+i).getContents().trim();
				if(statusStr.equals("付款完成"))tmp.setStatus("01");
				else continue;
				//设置支付渠道
				tmp.setBankcode(PayConstant.PAY_CONFIG.get("PAY_CHANNEL_RB"));
				//设置对账日期(2016-01-13 11:34:12)
				try {
					String d = sheet.getCell(6, 3+i).getContents().trim().split(" ")[0].replaceAll("-","");
					if(d.length() != 8)throw new Exception();
					tmp.setActdate(d);
				} catch (Exception e) {
					try {
						String d = sheet.getCell(5, 3+i).getContents().trim().split(" ")[0].replaceAll("-","");
						if(d.length() != 8)throw new Exception();
						tmp.setActdate(d);
					} catch (Exception e2) {}
				}
				//设置银行网关订单号
				tmp.setBnkordno(sheet.getCell(1, 3+i).getContents().trim());
				tmp.setChktype("1");
				//设置交易额
				tmp.setTxnamt(new Double((Double.parseDouble(sheet.getCell(4, 3+i).getContents().trim()))*100).longValue());
				//设置手续费
				tmp.setFee(0l);
				//设置净收金额
				tmp.setInamt(new Double((Double.parseDouble(sheet.getCell(4, 3+i).getContents().trim()))*100).longValue());
				//设置银行流水号
				tmp.setBanklogno(sheet.getCell(2, 3+i).getContents().trim());
				list.add(tmp);
			}
			//accountForXFPay(list);
			//插入对账记录到临时表
			PayBankAccountCheckDAO accDao = new PayBankAccountCheckDAO();
			//清除对账临时表
			accDao.clearPayBankAccountCheckTmp();
			List tmp = new ArrayList();
			//插入对账信息
			for(int i = 0; i<list.size(); i++){
				PayBankAccountCheckTmp t=(PayBankAccountCheckTmp)list.get(i);
				tmp.add(t);
				if(tmp.size()==1000){
					accDao.addPayBankAccountCheckBatch(tmp);
					tmp = new ArrayList();
				}
			}
			accDao.addPayBankAccountCheckBatch(tmp);
			//开始对账
			new PayBankAccountCheckService().checkAccount(PayConstant.PAY_CONFIG.get("PAY_CHANNEL_RB"));
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("对账处理错误（"+e.getMessage()+"）");
		} 
	}
}