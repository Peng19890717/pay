package com.pay.order.service;

import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import util.Tools;

import com.PayConstant;
import com.jweb.service.TransactionService;
import com.pay.coopbank.dao.PayCoopBank;
import com.pay.coopbank.service.PayCoopBankService;
import com.pay.fee.dao.PayFeeRate;
import com.pay.fee.service.PayFeeRateService;
import com.pay.merchant.dao.PayMerchant;
import com.pay.merchant.dao.PayMerchantDAO;
import com.pay.merchantinterface.service.RBPayService;
import com.pay.merchantinterface.service.SXYPayService;
import com.pay.merchantinterface.service.XFPayService;
import com.pay.order.dao.PayAccProfile;
import com.pay.order.dao.PayAccProfileDAO;
import com.pay.order.dao.PayWithdrawCashOrder;
import com.pay.order.dao.PayWithdrawCashOrderDAO;
import com.pay.risk.service.PayRiskDayTranSumService;
import com.pay.risk.service.RiskService;

/**
 * Object PAY_WITHDRAW_CASH_ORDER service. 
 * @author Administrator
 *
 */
public class PayWithdrawCashOrderService extends TransactionService{
	private static final Log log = LogFactory.getLog(PayOrderService.class);
    /**
     * Get records list(json).
     * @return
     */
    public String getPayWithdrawCashOrderList(PayWithdrawCashOrder payWithdrawCashOrder,int page,int rows,String sort,String order){
        try {
            PayWithdrawCashOrderDAO payWithdrawCashOrderDAO = new PayWithdrawCashOrderDAO();
            List list = payWithdrawCashOrderDAO.getPayWithdrawCashOrderList(payWithdrawCashOrder, page, rows, sort, order);
            JSONObject json = new JSONObject();
            json.put("total", String.valueOf(payWithdrawCashOrderDAO.getPayWithdrawCashOrderCount(payWithdrawCashOrder)));
            JSONArray row = new JSONArray();
            for(int i = 0; i<list.size(); i++){
                row.put(i, ((PayWithdrawCashOrder)list.get(i)).toJson());
            }
            //查询总金额
            json.put("rows", row);
            json.put("totalPayWithdrawCashOrder", new PayWithdrawCashOrderDAO().getTotalPayWithdrawCashOrder(payWithdrawCashOrder));
            json.put("countPayWithdrawCashOrder", new PayWithdrawCashOrderDAO().getPayWithdrawCashOrderCount(payWithdrawCashOrder));
            json.put("totalPayWithdrawFee", new PayWithdrawCashOrderDAO().getTotalPayWithdrawFee(payWithdrawCashOrder));
            return json.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
    /**
     * add PayWithdrawCashOrder
     * @param payWithdrawCashOrder
     * @throws Exception
     */
    public void addPayWithdrawCashOrder(PayWithdrawCashOrder payWithdrawCashOrder) throws Exception {
        new PayWithdrawCashOrderDAO().addPayWithdrawCashOrder(payWithdrawCashOrder);
    }
    /**
     * remove PayWithdrawCashOrder
     * @param casordno
     * @throws Exception
     */
    public void removePayWithdrawCashOrder(String casordno) throws Exception {
        new PayWithdrawCashOrderDAO().removePayWithdrawCashOrder(casordno);
    }
    /**
     * update PayWithdrawCashOrder
     * @param payWithdrawCashOrder
     * @throws Exception
     */
    public void updatePayWithdrawCashOrder(PayWithdrawCashOrder payWithdrawCashOrder) throws Exception {
        new PayWithdrawCashOrderDAO().updatePayWithdrawCashOrder(payWithdrawCashOrder);
    }
    /**
     * detail PayWithdrawCashOrder
     * @param casordno
     * @throws Exception
     */
    public PayWithdrawCashOrder detailPayWithdrawCashOrder(String casordno) throws Exception {
        return new PayWithdrawCashOrderDAO().detailPayWithdrawCashOrder(casordno);
    }
    
    /**
     * 更新提现状态
     * @param casordno
     * @param columName
     * @param value
     * @throws Exception
     */
    public void updatePayWithdrawCashOrderStatus(String casordno,String columName, String value) throws Exception {
    	new PayWithdrawCashOrderDAO().updatePayWithdrawCashOrderStatus(casordno,columName,value);
    }
	public void updatePayWithdrawCashOrderStatus(String[] params) throws Exception {
		new PayWithdrawCashOrderDAO().updatePayWithdrawCashOrderStatus(params);
	}
	
	/**
	 * 退回账户
	 * @param casordno
	 * @throws Exception
	 */
	public void updatePayWithdrawCashOrderReturnAccount(String casordno,String custId) throws Exception{
		PayWithdrawCashOrderDAO payWithdrawCashOrderDAO = new PayWithdrawCashOrderDAO();
		PayAccProfileDAO payAccProfileDAO = new PayAccProfileDAO();
		try {
    		//事务启动
    		transaction.beignTransaction(payWithdrawCashOrderDAO,payAccProfileDAO);
    		//通过 CasordNo 查询退回账户金额（提现金额+手续费）
    		PayWithdrawCashOrder payWithdrawCashOrder = payWithdrawCashOrderDAO.detailPayWithdrawCashOrder(casordno);
    		//更改提现表状态
    		payWithdrawCashOrderDAO.updatePayWithdrawCashOrderStatus(casordno,"ORDSTATUS",PayConstant.WITHDRAW_TYPE_RETURN);
    		//更改余额表账面金额 现金余额
            PayAccProfile payAccProfile = payAccProfileDAO.detailPayAccProfile(custId);
            payAccProfile.cashAcBal+=(payWithdrawCashOrder.getTxamt()+payWithdrawCashOrder.getFee());
            payAccProfile.consAcBal+=(payWithdrawCashOrder.getTxamt()+payWithdrawCashOrder.getFee());
            payAccProfileDAO.updatePayAccProfileCashAcBalAndConsAcBal(payAccProfile);
    		//事务提交
			transaction.endTransaction();
		} catch (Exception e) {
			//事务回滚
			transaction.rollback();
			throw new Exception("退回账户失败！");
		}
	}
	
	/**
     * 更新提现状态
     * @param casordno
     * @param columName
     * @param value
     * @throws Exception
     */
	public void updatePayWithdrawCashOrderStatus(PayWithdrawCashOrder pOrder) throws Exception {
		PayWithdrawCashOrderDAO pwdOrderDAO = new PayWithdrawCashOrderDAO();
		PayRiskDayTranSumService rtsService = new PayRiskDayTranSumService();
		try {
			transaction.beignTransaction(pwdOrderDAO,rtsService);
			pwdOrderDAO.updatePayWithdrawCashOrderStatus(pOrder);
			//订单状态:00: 未处理   01:提现处理中  02:提现成功  03:提现失败  04:已退回支付账号  05:重新提现  99:超时 默认00
			if("02".equals(pOrder.ordstatus))rtsService.updateRiskSuccessRefundSum(pOrder);
			transaction.endTransaction();
		} catch (Exception e) {
			transaction.rollback();
			throw e;
		}
	}
	
	public byte[] payWithdrawCashOrderListExportExcel(PayWithdrawCashOrder payWithdrawCashOrder) {
		PayWithdrawCashOrderDAO cashDao = new PayWithdrawCashOrderDAO();
    	String randomName = Tools.getUniqueIdentify();
    	//临时目录
    	File tmpFile = new File(JWebConstant.APP_PATH+"/dat/download/"+randomName);
    	tmpFile.mkdir();
    	//文件模板名称
    	String templetName ="withdrawCashOrder";
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
        	list = cashDao.getPayOrderList(payWithdrawCashOrder,step,step+excelRecordCount);
        	while(list.size()==excelRecordCount){
        		//加入Excel
        		writeListToExcel(list,templetFile,new File(tmpFile.getAbsolutePath()+"/"+(fileNum++)+".xls"));
        		step = step + excelRecordCount;
        		//取得业务数据
        		list = cashDao.getPayOrderList(payWithdrawCashOrder,step,step+excelRecordCount);
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
  			PayWithdrawCashOrder cashOrder = (PayWithdrawCashOrder)list.get(i);
  			
  			if(cashOrder.casordno != null && !"".equals(cashOrder.casordno))setCellValue(ws, 0, i + 1,cashOrder.casordno);
  			if(cashOrder.actTime != null)setCellValue(ws, 1, i + 1,new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(cashOrder.actTime));
  			if(cashOrder.txamt != null)setCellValue(ws, 2, i + 1,String.format("%.2f",(float)cashOrder.txamt*0.01));
  			if(cashOrder.fee != null)setCellValue(ws, 3, i + 1,String.format("%.2f",(float)cashOrder.fee*0.01));
  			
  			if("00".equals(cashOrder.ordstatus)){
  				setCellValue(ws, 4, i + 1,"未处理");
  			}else if("01".equals(cashOrder.ordstatus)){
  				setCellValue(ws, 4, i + 1,"提现处理中");
  			}else if("02".equals(cashOrder.ordstatus)){
  				setCellValue(ws, 4, i + 1,"提现成功");
  			}else if("03".equals(cashOrder.ordstatus)){
  				setCellValue(ws, 4, i + 1,"提现失败");
  			}else if("04".equals(cashOrder.ordstatus)){
  				setCellValue(ws, 4, i + 1,"已退回支付帐号");
  			}else if("99".equals(cashOrder.ordstatus)){
  				setCellValue(ws, 4, i + 1,"超时");
  			}
  			
  			if(cashOrder.bankName != null && !"".equals(cashOrder.bankName))setCellValue(ws, 5, i + 1,cashOrder.bankName);
  			if(cashOrder.bankpayacno != null && !"".equals(cashOrder.bankpayacno))setCellValue(ws, 6, i + 1,cashOrder.bankpayacno);
  			if(cashOrder.bankpayusernm != null && !"".equals(cashOrder.bankpayusernm))setCellValue(ws, 7, i + 1,cashOrder.bankpayusernm);
  			
  			if("0".equals(cashOrder.withdrawWay)){
  				setCellValue(ws, 8, i + 1,"线上自动到账");
  			}else if("1".equals(cashOrder.withdrawWay)){
  				setCellValue(ws, 8, i + 1,"线下打款");
  			}
  			
  			if(cashOrder.custId != null && !"".equals(cashOrder.custId))setCellValue(ws, 9, i + 1,cashOrder.custId);
  			if(cashOrder.sucTime != null)setCellValue(ws, 10, i + 1,new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(cashOrder.sucTime));
  			if(cashOrder.updateUser != null && !"".equals(cashOrder.updateUser))setCellValue(ws, 11, i + 1,cashOrder.updateUser);
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
	/**
	 * 单笔提现接口。
	 * @param withdrawParams
	 * @return 
	 * @throws Exception
	 */
  	PayWithdrawCashOrderDAO wcoDAO = new PayWithdrawCashOrderDAO();
	PayRiskDayTranSumService rtsService = new PayRiskDayTranSumService();
	public String withdrawCash(HttpServletRequest request) throws Exception{
		try {
			transaction.beignTransaction(wcoDAO,rtsService);
			//保存结果
			PayWithdrawCashOrder payWithdrawCashOrder = new PayWithdrawCashOrder();
			payWithdrawCashOrder.custType = request.getParameter("custType");
			payWithdrawCashOrder.custId = request.getParameter("custId");
			PayMerchant merchant = new PayMerchantDAO().detailPayMerchant(payWithdrawCashOrder.custId);
			payWithdrawCashOrder.casordno = request.getParameter("merchantNo");
			payWithdrawCashOrder.actTime = new Date();
//			payWithdrawCashOrder.sucTime = payWithdrawCashOrder.actTime;
			payWithdrawCashOrder.ordstatus = "00";
			payWithdrawCashOrder.txccy = "CNY";
			payWithdrawCashOrder.txamt =  Long.parseLong(request.getParameter("amount"));
			//取得系统内部提现费率
			PayFeeRate feeRate = new PayFeeRateService().getFeeRateByCust(payWithdrawCashOrder.custType,
					payWithdrawCashOrder.custId, PayConstant.TRAN_TYPE_WITHDRAW);
			Object [] fee = PayFeeRateService.getFeeByFeeRate(feeRate,payWithdrawCashOrder.txamt);
			payWithdrawCashOrder.fee = (Long)fee[0];
			payWithdrawCashOrder.netrecAmt = payWithdrawCashOrder.txamt;
			payWithdrawCashOrder.withdrawWay = PayConstant.PAY_CONFIG.get("SYS_WITHDRAW_CHANNEL");//提现方式:0自动路由、1线下、指定渠道
			payWithdrawCashOrder.bankCode = request.getParameter("bankNo");
			payWithdrawCashOrder.bankpayacno = request.getParameter("accountNo");
			payWithdrawCashOrder.bankpayusernm = request.getParameter("accountName"); 
			payWithdrawCashOrder.custType = request.getParameter("custType");
			payWithdrawCashOrder.custId = request.getParameter("custId");
			payWithdrawCashOrder.withdrawType = request.getParameter("userType");
			payWithdrawCashOrder.issuer = request.getParameter("issuer");
			payWithdrawCashOrder.filed1 = feeRate==null||feeRate.feeInfo==null?"":feeRate.feeInfo;
			// 1：对私 2：对公，对公账户获取银行连号
			if("2".equals(payWithdrawCashOrder.withdrawType)){
				payWithdrawCashOrder.issuer = request.getParameter("issuer");
			}
			payWithdrawCashOrder.mobileNo = request.getParameter("mobileNo");
			payWithdrawCashOrder.certType =  request.getParameter("certType");
			payWithdrawCashOrder.certNo =  request.getParameter("certNo");
			//风控检查
			String msg = null;
			if(PayConstant.CUST_TYPE_MERCHANT.equals(payWithdrawCashOrder.custType)) msg = new RiskService().checkMerchantLimit(
					payWithdrawCashOrder,payWithdrawCashOrder.custId,PayConstant.TRAN_TYPE_WITHDRAW);
			else if (PayConstant.CUST_TYPE_USER.equals(payWithdrawCashOrder.custType)) msg = new RiskService().checkUserLimit(
					payWithdrawCashOrder,payWithdrawCashOrder.custId,PayConstant.TRAN_TYPE_WITHDRAW);
			//违反风控规则
			if(msg!=null)throw new Exception(msg);
			wcoDAO.insertPayWithdrawCashOrder(payWithdrawCashOrder);//添加提现记录， 账户余额扣除
			//交易汇总更新
			rtsService.updateWithdrawSum(payWithdrawCashOrder);
			PayCoopBank channel = null;
			//自动选择费率低的渠道提现
			if("0".equalsIgnoreCase(PayConstant.PAY_CONFIG.get("SYS_WITHDRAW_CHANNEL"))){
				channel = PayFeeRateService.getMinFeeChannelForWithdraw(
					PayConstant.TRAN_TYPE_WITHDRAW,merchant.depositBankCode,payWithdrawCashOrder.txamt);
			//线下提现
			} else if("1".equalsIgnoreCase(PayConstant.PAY_CONFIG.get("SYS_WITHDRAW_CHANNEL"))){
				transaction.endTransaction();
				return "{\"resCode\":\"000\",\"ordstatus\":\"00\",\"resMessage\":\"提现申请成功\"}";
			//指定固定渠道
			} else {
				channel = new PayCoopBank();
				channel.bankCode=PayConstant.PAY_CONFIG.get("SYS_WITHDRAW_CHANNEL");
			}
			if(channel==null||PayCoopBankService.CHANNEL_MAP.get(channel.bankCode) == null) throw new Exception("未知的提现渠道");
			payWithdrawCashOrder.withdrawChannel = channel.bankCode;
			return withdrawByChannel(payWithdrawCashOrder,channel);
		} catch (Exception e) {
			transaction.rollback();
			throw e;
		}
	}
	private String withdrawByChannel(PayWithdrawCashOrder wcOrder,PayCoopBank channel) throws Exception{
		wcOrder.withdrawWay = channel.bankCode;//提现方式:0自动路由、1线下、指定渠道
		//通过先锋渠道完成提现	
		if(PayConstant.PAY_CONFIG.get("PAY_CHANNEL_XF").equalsIgnoreCase(channel.bankCode)){
//			new XFPayService().createWithdraw(wcOrder);
		//融宝提现
		} else if(PayConstant.PAY_CONFIG.get("PAY_CHANNEL_RB").equalsIgnoreCase(channel.bankCode)){
			new RBPayService().createWithdraw(wcOrder);
		} 
		wcoDAO.updateWithdrawCashOrderStatus(wcOrder);
		if("02".equals(wcOrder.ordstatus))rtsService.updateRiskSuccessRefundSum(wcOrder);
		return wcOrder.toJson().toString();
	}
	public void setResultWithdrawCashOrder(PayWithdrawCashOrder pcOrder) throws Exception {
		PayWithdrawCashOrderDAO wcoDAOt = new PayWithdrawCashOrderDAO();
		PayRiskDayTranSumService rtsServicet = new PayRiskDayTranSumService();
		try {
			transaction.beignTransaction(wcoDAOt,rtsService);
			wcoDAOt.updatePayWithdrawCashOrder(pcOrder);
			if("02".equals(pcOrder.ordstatus))rtsServicet.updateRiskSuccessRefundSum(pcOrder);
			transaction.endTransaction();
		} catch (Exception e) {
			transaction.rollback();
			throw e;
		}
	}
}