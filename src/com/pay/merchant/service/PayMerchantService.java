package com.pay.merchant.service;

import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
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
import com.jweb.dao.JWebUserDAO;
import com.pay.coopbank.service.PayCoopBankService;
import com.pay.custstl.dao.PayCustStlInfo;
import com.pay.custstl.dao.PayCustStlInfoDAO;
import com.pay.merchant.dao.PayMerchant;
import com.pay.merchant.dao.PayMerchantChannelRelation;
import com.pay.merchant.dao.PayMerchantChannelRelationDAO;
import com.pay.merchant.dao.PayMerchantDAO;
import com.pay.muser.dao.PayMerchantUser;
import com.pay.muser.dao.PayMerchantUserDAO;
/**
 * Object PAY_MERCHANT service. 
 * @author Administrator
 *
 */
public class PayMerchantService {
	private static final Log log = LogFactory.getLog(PayMerchantService.class);
    /**
     * Get records list(json).
     * @return
     */
    public String getPayMerchantList(PayMerchant payMerchant,int page,int rows,String sort,String order){
        try {
            PayMerchantDAO payMerchantDAO = new PayMerchantDAO();
            List list = payMerchantDAO.getPayMerchantList(payMerchant, page, rows, sort, order);
            for (Object object : list) {
            	if(object instanceof PayMerchant) {
            		PayMerchant tempPayMerchant = (PayMerchant)object;
            		tempPayMerchant.setMerAddr(tempPayMerchant.getProvince() + tempPayMerchant.getCity() + tempPayMerchant.getRegion() + tempPayMerchant.getMerAddr());
            	}
			}
            org.json.JSONObject json = new org.json.JSONObject();
            json.put("total", String.valueOf(payMerchantDAO.getPayMerchantCount(payMerchant)));
            org.json.JSONArray row = new org.json.JSONArray();
            for(int i = 0; i<list.size(); i++){
                row.put(i, ((PayMerchant)list.get(i)).toJson());
            }
            json.put("rows", row);
            //查询总金额
            Long [] money = payMerchantDAO.getTotalPayMerchantMoney(payMerchant);
            json.put("rows", row);
            json.put("totalCashAcBalMoney", String.valueOf(money[0]));
            json.put("totalPreStorageFeeMoney", String.valueOf(money[1]));
            json.put("totalFrozenBal", String.valueOf(money[2]));
            json.put("totalMarginBal", String.valueOf(money[3]));
            return json.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
    /**
     * add PayMerchant
     * @param payMerchant
     * @throws Exception
     */
    public void addPayMerchant(PayMerchant payMerchant) throws Exception {
        new PayMerchantDAO().addPayMerchant(payMerchant);
    }
    /**
     * remove PayMerchant
     * @param id
     * @throws Exception
     */
    public void removePayMerchant(String id) throws Exception {
        new PayMerchantDAO().removePayMerchant(id);
    }
    /**
     * update PayMerchant
     * @param payMerchant
     * @throws Exception
     */
    public void updatePayMerchant(PayMerchant mer,JWebUser user,PayCustStlInfo payCustStlInfo) throws Exception {
    	PayMerchantDAO pmDao =  new PayMerchantDAO();
    	//如果更新某些字段，需要重审,状态 关闭，未审
    	PayMerchant tmp = pmDao.detailPayMerchant(mer.id);
    	if(!(tmp.storeName != null && tmp.storeName.equals(mer.storeName))||//商城名称，商户对应的商城名称
    		!tmp.merLawPerson.equals(mer.merLawPerson)||//法人代表姓名
    		!tmp.lawPersonCretNo.equals(mer.lawPersonCretNo)||//法人代表证件号码
    		!tmp.taxRegistrationNo.equals(mer.taxRegistrationNo)||//税务登记证号
    		!(tmp.businessLicenceNo!=null&&tmp.businessLicenceNo.equals(mer.businessLicenceNo))||//营业执照注册号
    		!tmp.organizationNo.equals(mer.organizationNo)/*||//组织机构代码
    		!tmp.businessLicencePic.equals(mer.businessLicencePic)||//企业营业执照
    		!tmp.taxRegistrationPic.equals(mer.taxRegistrationPic)||//税务登记证
    		!tmp.organizationPic.equals(mer.organizationPic)||//组织机构代码证
    		!tmp.openingLicensesPic.equals(mer.openingLicensesPic)||//开户许可证
    		!tmp.certPicFront.equals(mer.certPicFront)||//法人身份证正面
    		!tmp.contractPic.equals(mer.contractPic)*/){//您的合同
    		mer.merStatus="1";//关闭
    		mer.checkStatus="0";//未审
    	}
    	//如果审核不通过修改后，变成未审状态
    	if("2".equals(tmp.checkStatus))mer.checkStatus="0";
    	//更新商户信息
    	if(pmDao.updatePayMerchant(mer)>0){
    		//更新商户结算信息
        	payCustStlInfo.setCustId(mer.getCustId());
        	payCustStlInfo.setLstUptTime(new Date());
        	payCustStlInfo.setLstUptOperId(user.getId());
        	new PayCustStlInfoDAO().updatePayCustStlInfo(payCustStlInfo);
        	//更新商户操作员信息。
        	PayMerchantUserDAO PayMerchantUserDAO = new PayMerchantUserDAO();
        	PayMerchantUser payMerchantUser = new PayMerchantUser();
        	payMerchantUser.email=mer.getAttentionLineEmail();
        	payMerchantUser.tel= mer.getAttentionLineTel();
        	payMerchantUser.custId=mer.getCustId();
        	PayMerchantUserDAO.updatePayMerchantUserForMerchant(payMerchantUser);
    	}
    }
    
    /**
     * 商户审核状态更改
     * @param mer
     * @return
     * @throws Exception
     */
    public int checkMerchant(PayMerchant mer) throws Exception {
    	return new PayMerchantDAO().checkMerchant(mer);
    }
    
    /**
     * 获取商户id中最大的商户编号
     * @return 返回最大的商户编号
     */
	public String selectMaxCustId() {
		return new PayMerchantDAO().selectMaxCustId();
	}
	
	
	/**
	 * 组织商户表信息，添加商户信息
	 * @param payCustStlInfo
	 * @param user
	 * @param payMerchant
	 * @throws Exception
	 */
	public void addPayMerchant(PayCustStlInfo payCustStlInfo,JWebUser user, PayMerchant payMerchant) throws Exception {
		// 完善商户vo添加商户信息
        payMerchant.setId(Tools.getUniqueIdentify());
//        String sCustId = new PayMerchantService().selectMaxCustId();
//        Long lCustId = null;
//        if(sCustId == null) {
//        	lCustId = 1000000000000001L;
//        } else{
//        	lCustId = Long.parseLong(sCustId);
//        	lCustId++;
//        }
//        payMerchant.setCustId(lCustId.toString());
        payMerchant.setCreateUser(user.id);
        payMerchant.setCreateTime(new Date());
        payMerchant.setMerOpnBankCode(payCustStlInfo.getDepositBankCode());
    	payMerchant.setBankStlAcNo(payCustStlInfo.getCustStlBankAcNo());
    	payMerchant.setCheckStatus("0");
    	new PayMerchantDAO().addPayMerchant(payMerchant);
	}
	
	/**
	 * 更新商户状态
	 * @param custId 商户id
	 * @param columName 客户名称
	 * @param operation 操作
	 * @throws Exception 
	 */
	public void updatePayMerchantStatus(String custId, String columName,String operation) throws Exception {
		new PayMerchantDAO().updatePayMerchantStatus(custId,columName,operation);
	}
	/**
     * detail PayMerchant
     * @param id
     * @throws Exception
     */
    public PayMerchant detailPayMerchant(String id) throws Exception {
        return new PayMerchantDAO().detailPayMerchant(id);
    }
    
    /**
     * detail PayMerchant
     * @param CUST_ID
     * @throws Exception
     */
    public PayMerchant detailPayMerchantByCustId(String id) throws Exception {
        return new PayMerchantDAO().detailPayMerchantByCustId(id);
    }
    
	/**
	 * 获取商户详情信息
	 * @param custId
	 * @return
	 */
	public PayMerchant selectByMerchantDetail(String custId) {
		return new PayMerchantDAO().selectByMerchantDetail(custId);
	}
	public PayMerchant selectByCustId(String custId) {
		return new PayMerchantDAO().selectByCustId(custId);
	}
	/**
	 * 检查商户是否存在 通过商户编号
	 * @param custId
	 * @return
	 */
	public boolean isExistMerchant(String custType,String custId){
		return new PayMerchantDAO().isExistMerchant(custType,custId);
	}
	/**
	 * 通过表的列检查其值是否存在
	 * @param custId
	 * @return
	 */
	public boolean isExistRecordByColumn(String col,String value){
		return new PayMerchantDAO().isExistRecordByColumn(col,value);
	}
	public String getMerchantNo(){
		return new PayMerchantDAO().getMerchantNo();
	}
	public JWebUser getOperatorUser(String userId){
		try {
			return new JWebUserDAO().getUserById(userId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 导出商户列表excel
	 * @param payMerchant
	 * @return
	 */
	public byte[] exportExcelForPayMerchantList(PayMerchant payMerchant) {
		PayMerchantDAO merchantDao = new PayMerchantDAO();
    	String randomName = Tools.getUniqueIdentify();
    	//临时目录
    	File tmpFile = new File(JWebConstant.APP_PATH+"/dat/download/"+randomName);
    	tmpFile.mkdir();
    	//文件模板名称
    	String templetName ="merchant";
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
        	list = merchantDao.getPayMerchantList(payMerchant,step,step+excelRecordCount);
        	while(list.size()==excelRecordCount){
        		//加入Excel
        		writeListToExcel(list,templetFile,new File(tmpFile.getAbsolutePath()+"/"+(fileNum++)+".xls"));
        		step = step + excelRecordCount;
        		//取得业务数据
        		list = merchantDao.getPayMerchantList(payMerchant,step,step+excelRecordCount);
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
  			PayMerchant payMerchant = (PayMerchant)list.get(i);
  			if(payMerchant.custId != null && !"".equals(payMerchant.custId))setCellValue(ws, 0, i + 1,payMerchant.custId);
  			if(payMerchant.storeName != null && !"".equals(payMerchant.storeName))setCellValue(ws, 1, i + 1,payMerchant.storeName);
  			if("00".equals(payMerchant.bizType)){
				setCellValue(ws, 2, i + 1,"特约商户");
			} else if("A0".equals(payMerchant.bizType)){
				setCellValue(ws, 2, i + 1,"数字点卡 ");
			} else if("A1".equals(payMerchant.bizType)){
				setCellValue(ws, 2, i + 1,"教育培训 ");
			} else if("A2".equals(payMerchant.bizType)){
				setCellValue(ws, 2, i + 1,"网络游戏 ");
			} else if("A3".equals(payMerchant.bizType)){
				setCellValue(ws, 2, i + 1,"旅游票务 ");
			} else if("A4".equals(payMerchant.bizType)){
				setCellValue(ws, 2, i + 1,"鲜花礼品 ");
			} else if("A5".equals(payMerchant.bizType)){
				setCellValue(ws, 2, i + 1,"电子产品 ");
			} else if("A6".equals(payMerchant.bizType)){
				setCellValue(ws, 2, i + 1,"图书音像");
			} else if("A7".equals(payMerchant.bizType)){
				setCellValue(ws, 2, i + 1,"会员论坛");
			} else if("A8".equals(payMerchant.bizType)){
				setCellValue(ws, 2, i + 1,"网站建设");
			} else if("A9".equals(payMerchant.bizType)){
				setCellValue(ws, 2, i + 1,"软件产品");
			} else if("B0".equals(payMerchant.bizType)){
				setCellValue(ws, 2, i + 1,"运动休闲");
			} else if("B1".equals(payMerchant.bizType)){
				setCellValue(ws, 2, i + 1,"彩票");
			} else if("B2".equals(payMerchant.bizType)){
				setCellValue(ws, 2, i + 1,"影视娱乐");
			} else if("B3".equals(payMerchant.bizType)){
				setCellValue(ws, 2, i + 1,"日常用品");
			} else if("B4".equals(payMerchant.bizType)){
				setCellValue(ws, 2, i + 1,"团购");
			} else if("B5".equals(payMerchant.bizType)){
				setCellValue(ws, 2, i + 1,"信用卡还款");
			} else if("B6".equals(payMerchant.bizType)){
				setCellValue(ws, 2, i + 1,"交通违章罚款");
			} else if("B7".equals(payMerchant.bizType)){
				setCellValue(ws, 2, i + 1,"全国公共事业缴费");
			} else if("B8".equals(payMerchant.bizType)){
				setCellValue(ws, 2, i + 1,"支付宝业务");
			} else if("B9".equals(payMerchant.bizType)){
				setCellValue(ws, 2, i + 1,"身份证查询");
			} else if("C1".equals(payMerchant.bizType)){
				setCellValue(ws, 2, i + 1,"购买支付通业务");
			} else if("C2".equals(payMerchant.bizType)){
				setCellValue(ws, 2, i + 1,"其他");
			} else if("C3".equals(payMerchant.bizType)){
				setCellValue(ws, 2, i + 1,"卡卡转账");
			} else if("C4".equals(payMerchant.bizType)){
				setCellValue(ws, 2, i + 1,"ETC业务");
			}
  			
  			if("0".equals(payMerchant.merType)){
  				setCellValue(ws, 3, i + 1,"一般商户");
			} else if("1".equals(payMerchant.merType)){
				setCellValue(ws, 3, i + 1,"平台商户 ");
			} else if("2".equals(payMerchant.merType)){
				setCellValue(ws, 3, i + 1,"担保商户 ");
			}
  			
  			if(payMerchant.merAddr != null && !"".equals(payMerchant.merAddr))setCellValue(ws, 4, i + 1,payMerchant.merAddr);
  			
  			if("0".equals(payMerchant.lawPersonCretType)){
  				setCellValue(ws, 5, i + 1,"身份证");
			} else if("1".equals(payMerchant.lawPersonCretType)){
				setCellValue(ws, 5, i + 1,"护照 ");
			} else if("2".equals(payMerchant.lawPersonCretType)){
				setCellValue(ws, 5, i + 1,"军官证 ");
			} else if("3".equals(payMerchant.lawPersonCretType)){
				setCellValue(ws, 5, i + 1,"武警身份证 ");
			} else if("4".equals(payMerchant.lawPersonCretType)){
				setCellValue(ws, 5, i + 1,"回乡证 ");
			} else if("5".equals(payMerchant.lawPersonCretType)){
				setCellValue(ws, 5, i + 1,"台胞证 ");
			} else if("6".equals(payMerchant.lawPersonCretType)){
				setCellValue(ws, 5, i + 1,"外国公民护照 ");
			} else if("7".equals(payMerchant.lawPersonCretType)){
				setCellValue(ws, 5, i + 1,"户口本  ");
			} else if("9".equals(payMerchant.lawPersonCretType)){
				setCellValue(ws, 5, i + 1,"警官证 ");
			} else if("a".equals(payMerchant.lawPersonCretType)){
				setCellValue(ws, 5, i + 1,"士兵证");
			} else if("b".equals(payMerchant.lawPersonCretType)){
				setCellValue(ws, 5, i + 1,"勘察证件");
			}
  			
  			if(payMerchant.merLawPerson != null && !"".equals(payMerchant.merLawPerson))setCellValue(ws, 6, i + 1,payMerchant.merLawPerson);
  			
  			if("0".equals(payMerchant.merStatus)) {
  				setCellValue(ws, 7, i + 1,"开启");
			}else if("1".equals(payMerchant.merStatus)) {
				setCellValue(ws, 7, i + 1,"关闭");
			}
  			
  			if("0".equals(payMerchant.frozStlSign)) {
  				setCellValue(ws, 8, i + 1,"未冻结");
			}else if("1".equals(payMerchant.frozStlSign)) {
				setCellValue(ws, 8, i + 1,"已冻结");
			}
  			
  			if(payMerchant.createTime != null)setCellValue(ws, 9, i + 1,new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payMerchant.createTime));
  			
  			if("0".equals(payMerchant.checkStatus)) {
  				setCellValue(ws, 10, i + 1,"未审核");
			} else if("1".equals(payMerchant.checkStatus)) {
				setCellValue(ws, 10, i + 1,"审核通过");
			} else if("2".equals(payMerchant.checkStatus)){
				setCellValue(ws, 10, i + 1,"审核失败");
			}
  			if(payMerchant.cashAcBal != null)setCellValue(ws, 11, i + 1,String.format("%.2f",(float)payMerchant.cashAcBal*0.01));
  			if(payMerchant.checkTime != null)setCellValue(ws, 12, i + 1,payMerchant.checkTime == null ? "" : new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payMerchant.checkTime));
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
	public void validMerchantId(String parentId) throws Exception {
		new PayMerchantDAO().validMerchantId(parentId);
	}
	
	
	/**
	 * 查询所有的节点
	 * @return
	 * @throws Exception
	 */
	public String getSubMerchantList(String custId) throws Exception {
		try {
			PayMerchant merchant = detailPayMerchantByCustId(custId);
			List<PayMerchant> list = new PayMerchantDAO().getSubMerchantList(custId);
			Map <String,PayMerchant>merchantMap = new HashMap<String,PayMerchant>();
			list.add(merchant);
			merchantMap.put(merchant.custId, merchant);
			for(int i = 0; i<list.size(); i++){
				PayMerchant tmp =  list.get(i);
				merchantMap.put(tmp.custId, tmp);
			}
			//创建树形结构，merchant为根节点
			for(int i = 0; i<list.size(); i++){
				PayMerchant tmp =  list.get(i);
				PayMerchant parentMer = merchantMap.get(tmp.parentId);
				if(parentMer!=null)parentMer.subMerList.add(tmp);
			}
			//取得相关的节点
			merchantMap = new HashMap<String,PayMerchant>();
			getMerMap(merchantMap,merchant);
			list = new ArrayList<PayMerchant>();
			Iterator it = merchantMap.keySet().iterator();
			while(it.hasNext())list.add(merchantMap.get(it.next()));
			//生成json数据和建立树
			JSONObject json = new JSONObject();
			json.put("total",list.size());
			JSONArray row = new JSONArray();
			for(int i = 0; i<list.size(); i++){
				PayMerchant tMer = (PayMerchant) list.get(i);
				row.put(i,tMer.toJson1(custId));
			}
			json.put("rows",row);
			return json.toString();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	private void getMerMap(Map <String,PayMerchant>merMap,PayMerchant merchant){
		merMap.put(merchant.custId, merchant);
		for(int i=0; i<merchant.subMerList.size(); i++){
			getMerMap(merMap,merchant.subMerList.get(i));
		}
	}
	public long validMerchantType(String parentId) {
		return new PayMerchantDAO().validMerchantType(parentId);
	}
	
	/**
	 * 查询支付通道列表
	 * @param payMerchantChannelRelation
	 * @param page
	 * @param rows
	 * @param sort
	 * @param order
	 * @return
	 */
    public String getPayMerchantChannelRelationList(PayMerchantChannelRelation payMerchantChannelRelation,int page,int rows,String sort,String order){
        try {
            PayMerchantChannelRelationDAO payMerchantChannelRelationDAO = new PayMerchantChannelRelationDAO();
            List list = payMerchantChannelRelationDAO.getPayMerchantChannelRelationList(payMerchantChannelRelation, page, rows, sort, order);
            JSONObject json = new JSONObject();
            json.put("total", String.valueOf(payMerchantChannelRelationDAO.getPayMerchantChannelRelationCount(payMerchantChannelRelation)));
            JSONArray row = new JSONArray();
            for(int i = 0; i<list.size(); i++){
                row.put(i, ((PayMerchantChannelRelation)list.get(i)).toJson());
            }
            json.put("rows", row);
            return json.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
    /**
     * 校验该商户号是否存在商户表中且不存在支付通道关联表中
     * @param custId
     * @return
     * @throws Exception
     */
	public String validByCustId(String custId) throws Exception {
		return String.valueOf(new PayMerchantDAO().validByCustId(custId));
	}
	/**
	 * 添加支付通道
	 * @param payMerchantChannelRelation
	 * @throws Exception
	 */
	public void addPayMerchantChannelRelation(
			PayMerchantChannelRelation payMerchantChannelRelation) throws Exception {
		new PayMerchantChannelRelationDAO().addPayMerchantChannelRelation(payMerchantChannelRelation);
		PayCoopBankService.loadMerchantChannelRelation();
	}
	/**
	 * 根据商户编号查询支付通道
	 * @param merno
	 * @return
	 * @throws Exception
	 */
	public Map selectPayChannelByMerno(String merno) throws Exception {
		//获取该商户的支付通道列表
		List<PayMerchantChannelRelation> list = new PayMerchantChannelRelationDAO().selectPayChannelByMerno(merno);
		Map map = new HashMap();
		for (PayMerchantChannelRelation relation : list) map.put(relation.tranType+","+relation.channelId,"");
		return map;
	}
	/**
     * 根据商户编号删除支付通道
     * @param merno
     * @throws Exception
     */
    public void removePayMerchantChannelRelation(String merno) throws Exception {
        new PayMerchantChannelRelationDAO().removePayMerchantChannelRelation(merno);
        PayCoopBankService.loadMerchantChannelRelation();
    }
	public long validPayYakuStlAccMerno(String custId) throws Exception {
		return new PayMerchantDAO().validPayYakuStlAccMerno(custId);
	}
}