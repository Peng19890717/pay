package com.pay.order.service;

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
import util.Tools;

import com.jweb.dao.JWebUser;
import com.pay.merchantinterface.service.PayRequest;
import com.pay.order.dao.PayOrder;
import com.pay.order.dao.PayOrderDAO;
import com.pay.user.dao.PayBusinessMember;
import com.pay.user.dao.PayBusinessMemberDAO;
import com.pay.user.dao.PaySalesmanMerchantRelationDAO;

/**
 * Object PAY_ORDER service. 
 * @author Administrator
 *
 */
public class PayOrderService {
	private static final Log log = LogFactory.getLog(PayOrderService.class);
    /**
     * Get records list(json).获取符合条件的当日订单列表
     * @param flag 
     * @return
     * @throws Exception 
     */
    public String getPayOrderList(String timeRange, PayOrder payOrder,int page,int rows,String sort,String order) throws Exception{
    	//记录订单的总金额
        try {
            PayOrderDAO payOrderDAO = new PayOrderDAO();
            Map<String,String> custMap = new HashMap();
            //调用持久层
            List list = payOrderDAO.getPayOrderList(timeRange,payOrder, page, rows, sort, order,custMap);
            JSONObject json = new JSONObject();
            json.put("total", String.valueOf(payOrderDAO.getPayOrderCount(timeRange,payOrder)));
            JSONArray row = new JSONArray();
            for(int i = 0; i<list.size(); i++){
                row.put(i, ((PayOrder)list.get(i)).toJson(custMap));
            }
            //查询总金额
            Long [] money = payOrderDAO.getTotalOrderMoney(timeRange,payOrder);
            json.put("rows", row);
            json.put("totalOrderMoney", String.valueOf(money[0]));
            json.put("totalFeeMoney", String.valueOf(money[1]));
            json.put("totalChannelFee", String.valueOf(money[2]));
            json.put("totalAppMoney", String.valueOf(money[3]));
            return json.toString();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
    /**
     * Get records list(json).获取符合条件的当日订单列表
     * @param request 
     * @return
     */
    public String getBussMemOrder(String timeRange,HttpServletRequest request, PayOrder payOrder,int page,int rows,String sort,String order){
        try {
        	//获取session中的用户
        	JWebUser user = (JWebUser)request.getSession().getAttribute("user");
        	//获取该用户及其子用户
        	PayBusinessMemberDAO dao = new PayBusinessMemberDAO();
    		List <PayBusinessMember> busiMemList = dao.getList(timeRange);
    		Map <String,PayBusinessMember>merchantMap = new HashMap<String,PayBusinessMember>();
    		for(int i = 0; i<busiMemList.size(); i++){
    			PayBusinessMember tmp =  busiMemList.get(i);
    			merchantMap.put(tmp.userId, tmp);
    		}
    		JSONObject json = new JSONObject();
    		JSONArray row = new JSONArray();
    		//如果当前登录用户不在业务员关系表中
    		if(merchantMap.get(user.id)==null){
    			json.put("rows", row);
    			json.put("total", "0");
    			json.put("totalOrderMoney", "0");
	            json.put("totalFeeMoney", "0");
	            json.put("totalAppMoney", "0");
	            return json.toString();
    		}
    		//创建树形结构，root为根节点
    		for(int i = 0; i<busiMemList.size(); i++){
    			PayBusinessMember tmp =  busiMemList.get(i);
    			PayBusinessMember parent = merchantMap.get(tmp.parentId);
    			if(parent!=null)parent.subList.add(tmp);
    		}
    		List<String> tmpList = new ArrayList<String>();
    		getAllDelMem(tmpList,merchantMap.get(user.id));
    		
    		PayOrderDAO payOrderDAO = new PayOrderDAO();
    		Map<String,String> custMap = new HashMap<String,String>();
    		//业务员选的本人
        	if("myself".equals(request.getParameter("searchPayOrderOrdByPerson"))){
        		List<String> myself = new ArrayList<String>();
        		myself.add(user.id);
        		//查询该用户对应的商户
        		List<String> myselfMerList = new PaySalesmanMerchantRelationDAO().findMerListByUlist(timeRange , myself);
        		//调用持久层查询这些商户对应的订单
                List myselfOrderList = payOrderDAO.getBussMemOrderList(timeRange, payOrder, page, rows, sort, order,custMap,myselfMerList);
                json.put("total", String.valueOf(payOrderDAO.getBussMemOrderCount(timeRange, payOrder,myselfMerList)));
                for(int i = 0; i<myselfOrderList.size(); i++){
                    row.put(i, ((PayOrder)myselfOrderList.get(i)).toJson(custMap));
                }
        		//查询总金额
                Long [] myselfOrderMoney = payOrderDAO.getTotalBussMemOrderMoney(timeRange, payOrder,myselfMerList);
                json.put("rows", row);
                json.put("totalOrderMoney", String.valueOf(myselfOrderMoney[0]));
                json.put("totalFeeMoney", String.valueOf(myselfOrderMoney[1]));
                json.put("totalAppMoney", String.valueOf(myselfOrderMoney[2]));
            } else {
            	//业务员选的全部
            	//查询这些用户对应的商户
            	List<String> merIdList = new PaySalesmanMerchantRelationDAO().findMerListByUlist(timeRange, tmpList);
            	//调用持久层查询这些商户对应的订单
            	List list = payOrderDAO.getBussMemOrderList(timeRange, payOrder, page, rows, sort, order,custMap,merIdList);
            	json.put("total", String.valueOf(payOrderDAO.getBussMemOrderCount(timeRange, payOrder,merIdList)));
            	for(int i = 0; i<list.size(); i++){
            		row.put(i, ((PayOrder)list.get(i)).toJson(custMap));
            	}
            	//查询总金额
            	Long [] money = payOrderDAO.getTotalBussMemOrderMoney(timeRange, payOrder,merIdList);
            	json.put("rows", row);
            	json.put("totalOrderMoney", String.valueOf(money[0]));
            	json.put("totalFeeMoney", String.valueOf(money[1]));
            	json.put("totalAppMoney", String.valueOf(money[2]));
            }
            return json.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
    /**
	 * 获取该节点及其下面的子节点列表
	 * @param list
	 * @param mem
	 */
	public void getAllDelMem(List<String> list,PayBusinessMember mem){
		list.add(mem.userId);
		for(int i = 0; i<mem.subList.size(); i++){
			PayBusinessMember tmpMem =  mem.subList.get(i);
			getAllDelMem(list,tmpMem);
		}
	}
    /**
     * 查询订单详情
     * @param flag 
     * @param payOrder
     * @param page
     * @param rows
     * @param parameter
     * @param parameter2
     * @return
     */
	public PayOrder getPayOrderDetail(String flag, PayOrder payOrder) {
		 try {
	            PayOrderDAO payOrderDAO = new PayOrderDAO();
	            PayOrder order=new PayOrder();
	            //调用持久层
	            order = payOrderDAO.getPayOrderDetail(flag,payOrder);
	            //JSONObject json = order.toJson();
	            //return json.toString();
	            return order;
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
		 return null;
	}
	
	/**
	 * 导出交易列表
	 * @param flag 
	 * @param payOrder
	 * @return
	 */
	public byte[] exportExcelForPayOrderList(String flag, PayOrder payOrder) {
		PayOrderDAO payOrderDao = new PayOrderDAO();
    	String randomName = Tools.getUniqueIdentify();
    	//临时目录
    	File tmpFile = new File(JWebConstant.APP_PATH+"/dat/download/"+randomName);
    	tmpFile.mkdir();
    	//文件模板名称
    	String templetName ="order";
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
        	list = payOrderDao.getPayOrderList(flag,payOrder,step,step+excelRecordCount);
        	while(list.size()==excelRecordCount){
        		//加入Excel
        		writeListToExcel(list,templetFile,new File(tmpFile.getAbsolutePath()+"/"+(fileNum++)+".xls"));
        		step = step + excelRecordCount;
        		//取得业务数据
        		list = payOrderDao.getPayOrderList(flag,payOrder,step,step+excelRecordCount);
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
  			PayOrder order = (PayOrder)list.get(i);
  			if(order.merno != null && !"".equals(order.merno))setCellValue(ws, 0, i + 1,order.merno);
  			if(order.storeName != null && !"".equals(order.storeName))setCellValue(ws, 1, i + 1,order.storeName);
  			if(order.prdordno != null && !"".equals(order.prdordno))setCellValue(ws, 2, i + 1,order.prdordno);
  			if(order.createtime != null)setCellValue(ws, 3, i + 1,new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(order.createtime));
  			if(order.prdname != null && !"".equals(order.prdname))setCellValue(ws, 4, i + 1,order.prdname);
  			if(order.txamt != null)setCellValue(ws, 5, i + 1,String.format("%.2f",(float)order.txamt*0.01));
  			if(order.fee != null)setCellValue(ws, 6, i + 1,String.format("%.2f",(float)order.fee*0.01));
  			if(order.channelFee != null)setCellValue(ws, 7, i + 1,String.format("%.2f",(float)order.channelFee*0.01));
  			if(order.payordno != null && !"".equals(order.payordno))setCellValue(ws, 8, i + 1,order.payordno);
  			
  			if("00".equals(order.paytype)){
  				setCellValue(ws, 9, i + 1,"支付帐号");
  			}else if("01".equals(order.paytype)){
  				setCellValue(ws, 9, i + 1,"网上银行");
  			}else if("02".equals(order.paytype)){
  				setCellValue(ws, 9, i + 1,"终端");
  			}else if("03".equals(order.paytype)){
  				setCellValue(ws, 9, i + 1,"快捷支付");
  			}else if("04".equals(order.paytype)){
  				setCellValue(ws, 9, i + 1,"混合支付");
  			}else if("05".equals(order.paytype)){
  				setCellValue(ws, 9, i + 1,"支票/汇款");
  			}
  			
  			if("00".equals(order.ordstatus)){
  				setCellValue(ws, 10, i + 1,"等待付款");
  			}else if("01".equals(order.ordstatus)){
  				setCellValue(ws, 10, i + 1,"付款成功");
  			}else if("02".equals(order.ordstatus)){
  				setCellValue(ws, 10, i + 1,"付款失败");
  			}else if("03".equals(order.ordstatus)){
  				setCellValue(ws, 10, i + 1,"退款申请");
  			}else if("04".equals(order.ordstatus)){
  				setCellValue(ws, 10, i + 1,"等待退款");
  			}else if("05".equals(order.ordstatus)){
  				setCellValue(ws, 10, i + 1,"退款成功");
  			}else if("06".equals(order.ordstatus)){
  				setCellValue(ws, 10, i + 1,"退款失败");
  			}else if("07".equals(order.ordstatus)){
  				setCellValue(ws, 10, i + 1,"同意撤销");
  			}else if("08".equals(order.ordstatus)){
  				setCellValue(ws, 10, i + 1,"拒绝撤销");
  			}else if("09".equals(order.ordstatus)){
  				setCellValue(ws, 10, i + 1,"撤销成功");
  			}else if("10".equals(order.ordstatus)){
  				setCellValue(ws, 10, i + 1,"撤销失败");
  			}else if("12".equals(order.ordstatus)){
  				setCellValue(ws, 10, i + 1,"预付款");
  			}else if("17".equals(order.ordstatus)){
  				setCellValue(ws, 10, i + 1,"实付审核中");
  			}else if("18".equals(order.ordstatus)){
  				setCellValue(ws, 10, i + 1,"实付审核通过");
  			}else if("19".equals(order.ordstatus)){
  				setCellValue(ws, 10, i + 1,"实付审核拒绝");
  			}else if("99".equals(order.ordstatus)){
  				setCellValue(ws, 10, i + 1,"超时");
  			}
  		
  			if("00".equals(order.prdordstatus)){
  				setCellValue(ws, 11, i + 1,"等待付款");
  			}else if("01".equals(order.prdordstatus)){
  				setCellValue(ws, 11, i + 1,"付款成功");
  			}else if("02".equals(order.prdordstatus)){
  				setCellValue(ws, 11, i + 1,"支付处理中");
  			}else if("03".equals(order.prdordstatus)){
  				setCellValue(ws, 11, i + 1,"退款审核中");
  			}else if("04".equals(order.prdordstatus)){
  				setCellValue(ws, 11, i + 1,"退款处理中");
  			}else if("05".equals(order.prdordstatus)){
  				setCellValue(ws, 11, i + 1,"退款成功");
  			}else if("06".equals(order.prdordstatus)){
  				setCellValue(ws, 11, i + 1,"退款失败");
  			}else if("07".equals(order.prdordstatus)){
  				setCellValue(ws, 11, i + 1,"撤销审核中");
  			}else if("08".equals(order.prdordstatus)){
  				setCellValue(ws, 11, i + 1,"同意撤销");
  			}else if("09".equals(order.prdordstatus)){
  				setCellValue(ws, 11, i + 1,"撤销成功");
  			}else if("10".equals(order.prdordstatus)){
  				setCellValue(ws, 11, i + 1,"撤销失败");
  			}else if("11".equals(order.prdordstatus)){
  				setCellValue(ws, 11, i + 1,"订单作废");
  			}else if("12".equals(order.prdordstatus)){
  				setCellValue(ws, 11, i + 1,"预付款");
  			}else if("13".equals(order.prdordstatus)){
  				setCellValue(ws, 11, i + 1,"延迟付款审核中");
  			}else if("14".equals(order.prdordstatus)){
  				setCellValue(ws, 11, i + 1,"冻结");
  			}else if("15".equals(order.prdordstatus)){
  				setCellValue(ws, 11, i + 1,"同意延迟付款");
  			}else if("16".equals(order.prdordstatus)){
  				setCellValue(ws, 11, i + 1,"拒绝延迟退款");
  			}else if("17".equals(order.prdordstatus)){
  				setCellValue(ws, 11, i + 1,"实付审核中");
  			}else if("18".equals(order.prdordstatus)){
  				setCellValue(ws, 11, i + 1,"实付审核通过");
  			}else if("19".equals(order.prdordstatus)){
  				setCellValue(ws, 11, i + 1,"实付审核拒绝");
  			}else if("99".equals(order.prdordstatus)){
  				setCellValue(ws, 11, i + 1,"超时");
  			}
  			
  			if("0".equals(order.prdordtype)){
  				setCellValue(ws, 12, i + 1,"消费");
  			}else if("1".equals(order.prdordtype)){
  				setCellValue(ws, 12, i + 1,"充值");
  			}else if("2".equals(order.prdordtype)){
  				setCellValue(ws, 12, i + 1,"担保支付");
  			}else if("3".equals(order.prdordtype)){
  				setCellValue(ws, 12, i + 1,"商户充值");
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
  	
  	/**
  	 * 根据订单编号，更新订单银行信息
  	 * @param order
  	 * @throws Exception
  	 */
	public void updateOrderForBanks(PayOrder order) throws Exception {
		new PayOrderDAO().updateOrderForBanks(order);
	}
	/**
  	 * H5快捷更新卡信息
  	 * @param order
  	 * @throws Exception
  	 */
	public void updateOrderForQuickPayH5(PayRequest payRequest) throws Exception {
		new PayOrderDAO().updateOrderForQuickPayH5(payRequest);
	}
//	/**
//	 * 根据订单号，查询订单信息
//	 * @param merchantId
//	 * @param merchantOrderId
//	 * @return
//	 * @throws Exception
//	 */
//	public PayOrder getPayOrderByMerInfo(String merchantId,String merchantOrderId) throws Exception {
//		return new PayOrderDAO().getPayOrderByMerInfo(merchantId,merchantOrderId);
//	}
	/**
	 * 订单结算
	 * @param prdordno
	 * @param flag
	 */
	public void settlementPayProOrder(String prdordno, String flag) {
		try {
			new PayOrderDAO().settlementPayProOrder(flag,prdordno);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}