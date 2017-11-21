package com.pay.user.service;

import java.io.File;
import java.io.FileInputStream;
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

import com.jweb.dao.JWebUser;
import com.pay.coopbank.dao.PayChannelRotationLog;
import com.pay.merchant.dao.PayMerchant;
import com.pay.merchant.service.PayMerchantService;
import com.pay.statistics.service.AgentProfitService;
import com.pay.user.dao.PayBusinessMemberDAO;
import com.pay.user.dao.PaySalesmanMerchantRelation;
import com.pay.user.dao.PaySalesmanMerchantRelationDAO;

/**
 * Object PAY_SALESMAN_MERCHANT_RELATION service.
 * 
 * @author Administrator
 * 
 */
public class PaySalesmanMerchantRelationService {
	private static final Log log = LogFactory
			.getLog(PaySalesmanMerchantRelationService.class);

	/**
	 * Get records list(json).
	 * 
	 * @return
	 */
	public String getPaySalesmanMerchantRelationList(
			PaySalesmanMerchantRelation paySalesmanMerchantRelation, int page,
			int rows, String sort, String order) {
		try {
			PaySalesmanMerchantRelationDAO paySalesmanMerchantRelationDAO = new PaySalesmanMerchantRelationDAO();
			List list = paySalesmanMerchantRelationDAO
					.getPaySalesmanMerchantRelationList(
							paySalesmanMerchantRelation, page, rows, sort,
							order);
			JSONObject json = new JSONObject();
			json.put(
					"total",
					String.valueOf(paySalesmanMerchantRelationDAO
							.getPaySalesmanMerchantRelationCount(paySalesmanMerchantRelation)));
			JSONArray row = new JSONArray();
			for (int i = 0; i < list.size(); i++) {
				row.put(i, ((PaySalesmanMerchantRelation) list.get(i)).toJson());
			}
			json.put("rows", row);
			return json.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * 根据用户编号查询该用户下的商户
	 * 
	 * @param userId
	 * @param page
	 * @param rows
	 * @param sort
	 * @param order
	 * @return
	 */
	public String getMersByUid(String userId, int page, int rows, String sort,
			String order) {
		try {
			PaySalesmanMerchantRelationDAO paySalesmanMerchantRelationDAO = new PaySalesmanMerchantRelationDAO();
			List list = paySalesmanMerchantRelationDAO.getMersByUid(userId,
					page, rows, sort, order);
			JSONObject json = new JSONObject();
			json.put("total", String.valueOf(paySalesmanMerchantRelationDAO
					.getMersCount(userId)));
			JSONArray row = new JSONArray();
			for (int i = 0; i < list.size(); i++) {
				row.put(i, ((PaySalesmanMerchantRelation) list.get(i)).toJson());
			}
			json.put("rows", row);
			return json.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * add PaySalesmanMerchantRelation
	 * 
	 * @param paySalesmanMerchantRelation
	 * @throws Exception
	 */
	public void addPaySalesmanMerchantRelation(
			PaySalesmanMerchantRelation paySalesmanMerchantRelation)
			throws Exception {

		JWebUser detailPaySalesmanMerchantRelation = new PayBusinessMemberDAO()
				.selectJWebUserForUserId(paySalesmanMerchantRelation.userId);
		PayMerchant detailPayMerchantByCustId = new PayMerchantService()
				.detailPayMerchantByCustId(paySalesmanMerchantRelation.merNo);
		PaySalesmanMerchantRelation userForMerno = new PaySalesmanMerchantRelationService()
				.detailPaySalesmanMerchantRelationForMerno(paySalesmanMerchantRelation.merNo);

		if (detailPaySalesmanMerchantRelation == null)
			throw new Exception("对不起，用户不存在");
		if (detailPayMerchantByCustId == null)
			throw new Exception("对不起，商户号不存在");
		if (userForMerno != null)
			throw new Exception("对不起，该商户已与其他用户产生关联");

		new PaySalesmanMerchantRelationDAO()
				.addPaySalesmanMerchantRelation(paySalesmanMerchantRelation);
	}

	/**
	 * remove PaySalesmanMerchantRelation
	 * 
	 * @param userId
	 * @throws Exception
	 */
	public void removePaySalesmanMerchantRelation(String merNo)
			throws Exception {
		new PaySalesmanMerchantRelationDAO()
				.removePaySalesmanMerchantRelation(merNo);
	}

	/**
	 * detail PaySalesmanMerchantRelation
	 * 
	 * @param userId
	 * @throws Exception
	 */
	public PaySalesmanMerchantRelation detailPaySalesmanMerchantRelation(
			String userId) throws Exception {
		return new PaySalesmanMerchantRelationDAO()
				.detailPaySalesmanMerchantRelation(userId);
	}

	public PaySalesmanMerchantRelation detailPaySalesmanMerchantRelationForMerno(
			String merNo) throws Exception {
		return new PaySalesmanMerchantRelationDAO()
				.detailPaySalesmanMerchantRelationForMerno(merNo);
	}

	public void updatePaySalesmanMerchantRelation(
			PaySalesmanMerchantRelation paySalesmanMerchantRelation)
			throws Exception {
		new PaySalesmanMerchantRelationService()
				.deletePaySalesmanMerchantRelation(paySalesmanMerchantRelation.merNo);
		if (paySalesmanMerchantRelation.userId != null
				&& !"".equals(paySalesmanMerchantRelation.userId)) {
			new PaySalesmanMerchantRelationService()
					.addPaySalesmanMerchantRelation(paySalesmanMerchantRelation);
		}
	}

	public void deletePaySalesmanMerchantRelation(String custId)
			throws Exception {
		new PaySalesmanMerchantRelationDAO()
				.deletePaySalesmanMerchantRelation(custId);
	}

	public byte[] getPaySalesmanMerchantRelation(
			PaySalesmanMerchantRelation paySalesmanMerchantRelation)
			throws Exception {
		PaySalesmanMerchantRelationDAO relationDao = new PaySalesmanMerchantRelationDAO();
		List list = relationDao
				.getPaySalesmanRelationList(paySalesmanMerchantRelation);
		// 文件处理
		String randomName = Tools.getUniqueIdentify();
		// 文件模板名称
		// 模板文件
		File templetFile = new File(JWebConstant.APP_PATH
				+ "/templet/salemanMerRal.xls");
		try {
			String fileName = JWebConstant.APP_PATH + "/dat/download/"
					+ randomName + ".xls";
			log.info(" read file:" + fileName);
			// 取得业务数据
			writeListToExcel(list, templetFile, new File(fileName));
			FileInputStream fis = new FileInputStream(fileName);
			byte[] b = new byte[fis.available()];
			fis.read(b);
			fis.close();
			// Tools.deleteFile(templetFile);
			return b;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public void writeListToExcel(List<Map<String,String>> list, File templetFile, File tmpFile) {
		Workbook rw = null;
		WritableWorkbook wwb = null; 
		WritableSheet ws = null;
		try {
			if (!Tools.copy(templetFile.getAbsolutePath(), tmpFile.getAbsolutePath())) return;
			rw = jxl.Workbook.getWorkbook(tmpFile);
			wwb = Workbook.createWorkbook(tmpFile, rw);
			ws = wwb.getSheet(0);
			writeData(list,ws);
			wwb.write();
			wwb.close();
			rw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
	public void writeData(List<Map<String,String>> list,WritableSheet ws){
		for (int i=0; i < list.size(); i++) {
			Map<String,String> map_temp=list.get(i);
			if(map_temp.get("USER_ID")!=null && ""!= map_temp.get("USER_ID"))setCellValue(ws, 0, i+1, map_temp.get("USER_ID"));
			if(map_temp.get("NAME")!=null && ""!= map_temp.get("NAME"))setCellValue(ws, 1, i+1, map_temp.get("NAME"));
			if(map_temp.get("MER_NO")!=null && ""!= map_temp.get("MER_NO"))setCellValue(ws, 2, i+1, map_temp.get("MER_NO"));
			if(map_temp.get("STORE_NAME")!=null && ""!= map_temp.get("STORE_NAME"))setCellValue(ws, 3, i+1, map_temp.get("STORE_NAME"));
			if(map_temp.get("CASH")!=null && ""!= map_temp.get("CASH"))setCellValue(ws, 4, i+1, map_temp.get("CASH"));
			if(map_temp.get("ONLINE_DATE")!=null && ""!= map_temp.get("ONLINE_DATE"))setCellValue(ws, 5, i+1, map_temp.get("ONLINE_DATE"));
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

