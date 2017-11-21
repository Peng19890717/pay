package com.pay.risk.service;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.aop.ThrowsAdvice;

import util.Tools;

import com.pay.risk.dao.PayRiskXList;
import com.pay.risk.dao.PayRiskXListDAO;

/**
 * Object PAY_RISK_X_LIST service. 
 * @author Administrator
 *
 */
public class PayRiskXListService {
    /**
     * Get records list(json).
     * @return
     */
    public String getPayRiskXListList(PayRiskXList payRiskXList,int page,int rows,String sort,String order){
        try {
            PayRiskXListDAO payRiskXListDAO = new PayRiskXListDAO();
            List list = payRiskXListDAO.getPayRiskXListList(payRiskXList, page, rows, sort, order);
            JSONObject json = new JSONObject();
            json.put("total", String.valueOf(payRiskXListDAO.getPayRiskXListCount(payRiskXList)));
            JSONArray row = new JSONArray();
            for(int i = 0; i<list.size(); i++){
                row.put(i, ((PayRiskXList)list.get(i)).toJson());
            }
            json.put("rows", row);
            return json.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
    /**
     * update PayRiskXList
     * @param payRiskXList
     * @throws Exception
     */
    public void updatePayRiskXList(PayRiskXList payRiskXList) throws Exception {
        new PayRiskXListDAO().updatePayRiskXList(payRiskXList);
    }
    /**
     * add PayRiskXList
     * @param payRiskXList
     * @throws Exception
     */
    public void addPayRiskXList(PayRiskXList payRiskXList) throws Exception {
    	payRiskXList.setId(Tools.getUniqueIdentify());
    	payRiskXList.setRegdtTime(new Date());
    	payRiskXList.setCreateDatetime(new Date());
        new PayRiskXListDAO().addPayRiskXList(payRiskXList);
    }
    /**
     * detail PayRiskXList
     * @param id
     * @throws Exception
     */
    public PayRiskXList detailPayRiskXList(String id) throws Exception {
        return new PayRiskXListDAO().detailPayRiskXList(id);
    }
    
    /**
     * 判断记录是否存在
     * @param id
     * @throws Exception
     */
    public Boolean isExistPayRiskXList(String type,String code) throws Exception {
        return new PayRiskXListDAO().isExistPayRiskXList(type,code);
    }
    /**
     * 读取批量文件
     * @param file
     * @throws IOException 
     * @throws ZipException 
     */
	public void readxListFile(String filePath)  throws Exception {
		if(filePath == null)return;
		File file = new File(filePath);
		if(!file.exists())return;
		List<PayRiskXList> payRiskXLists = new ArrayList<PayRiskXList>();
		try {
			Workbook wk = Workbook.getWorkbook(file);
			Sheet sheet = wk.getSheet(0);
			
			//判断模版是否正确
			String[] modelStr = new String[]{"客户类型（用户/商户/手机号/银行卡号）","客户编号","名单类型（白/黑/红）","备注"};
			Cell[] cellArr = sheet.getRow(0);
			
			if(cellArr!=null && cellArr.length>0){
				for (int i = 0; i < cellArr.length; i++) {
					boolean one = false;
					for (int j = 0; j < modelStr.length; j++) {
						if(cellArr[i]!=null && !"".equals(cellArr[i]) && modelStr[j]!=null && !"".equals(modelStr[j])){
							if(cellArr[i].getContents().trim().equals(modelStr[j])){
								one = true;
								break;
							}
						}
					}
					if(one==false) throw new Exception("请上传正确模版！");
				}
			}
			
			int totalCount = sheet.getRows()-1;
			if(totalCount > 5000) throw new Exception("数据最多为5000条！");
			for (int i = 1; i <= totalCount; i++) {
				PayRiskXList xList = new PayRiskXList();
				xList.setId(Tools.getUniqueIdentify());
				xList.setRegdtTime(new Date());
				xList.setCreateDatetime(new Date());
				//设置客户类型
				String clientType = sheet.getCell(0,i).getContents().trim();
				if("用户".equals(clientType)){
					xList.setClientType("0");
				}else if("商户".equals(clientType)){
					xList.setClientType("1");
				}else if("手机号".equals(clientType)){
					xList.setClientType("2");
				}else if("银行卡号".equals(clientType)){
					xList.setClientType("3");
				}else continue;
				//设置客户编码
				String clientCode = sheet.getCell(1,i).getContents().trim();
				if(clientCode==null || "".equals(clientCode))continue;
				if(new PayRiskXListDAO().existsClientCode(clientCode,xList.getClientType()))continue;
				xList.setClientCode(clientCode);
				//设置名单类型
				String xType = sheet.getCell(2,i).getContents().trim();
				if("白名单".equals(xType) || "白".equals(xType)){
					xList.setxType("1");
				}else if("黑名单".equals(xType) || "黑".equals(xType)){
					xList.setxType("3");
				}else if("红名单".equals(xType) || "红".equals(xType)){
					xList.setxType("4");
				}else continue;
				//设置备注
				xList.setCasBuf(sheet.getCell(3,i).getContents().trim());
				payRiskXLists.add(xList);
			}
			new PayRiskXListDAO().addPayRiskXListBatch(payRiskXLists);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("读取excel数据错误（"+e.getMessage()+"）");
		}
	}
	/**
     * 下载模版
     * @param response 
     * @param request 
     * @param os 
     * @return
     * @throws Exception 
     */
	public byte[] exportExcel(HttpServletRequest request, HttpServletResponse response, OutputStream os) throws Exception {
		String path = request.getRealPath("/");
    	File file = new File(path+"\\templet\\x_list.xls");
    	if(!file.isFile() || !file.exists() ||file.isDirectory())throw new Exception("下载的文件路径不存在或不正确！");
    	response.setContentType("application/vnd.ms-excel");
    	response.setContentLength((int)file.length());
    	String fileName = URLEncoder.encode("模版x_list_"+new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())+".xls","utf-8");
    	response.setHeader("Content-Disposition", "attachment;filename="+fileName);
    	FileInputStream fis = new FileInputStream(file);
    	BufferedInputStream buff = new BufferedInputStream(fis);
    	byte b[] = new byte[1024];
    	long k = 0;
    	while(k<file.length()){
    		int j = buff.read(b,0,1024);
    		k+=j;
    		os.write(b, 0, j);
    	}
    	os.flush();
    	os.close();
    	buff.close();
		return null;
	}
}