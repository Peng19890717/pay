package com.pay.contract.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.pay.contract.dao.PayContract;
import com.pay.contract.dao.PayContractDAO;

/**
 * Object PAY_CONTRACT service. 
 * @author Administrator
 *
 */
public class PayContractService {
	/**
	 * 合同到期时间保存文件
	 */
	public static String contractExpiresFileName = "remind_contract_expires_info.txt";
    /**
     * Get records list(json).
     * @return
     */
    public String getPayContractList(PayContract payContract,int page,int rows,String sort,String order){
        try {
            PayContractDAO payContractDAO = new PayContractDAO();
            //调用持久层
            List list = payContractDAO.getPayContractList(payContract, page, rows, sort, order);
            JSONObject json = new JSONObject();
            json.put("total", String.valueOf(payContractDAO.getPayContractCount(payContract)));
            JSONArray row = new JSONArray();
            for(int i = 0; i<list.size(); i++){
                row.put(i, ((PayContract)list.get(i)).toJson());
            }
            json.put("rows", row);
            return json.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
    /**
     * 合同详情
     */
    public PayContract getPayContractDetail(PayContract payContract) {
		try {
			PayContractDAO payContractDAO=new PayContractDAO();
			PayContract contract=new PayContract();
            //调用持久层
			contract = payContractDAO.getPayContractDetail(payContract);
            return contract;
        } catch (Exception e) {
            e.printStackTrace();
        }
	 return null;
	}
    /**
     * add PayContract
     * @param payContract
     * @throws Exception
     */
    public void addPayContract(PayContract payContract) throws Exception {
        new PayContractDAO().addPayContract(payContract);
    }
    /**
     * detail PayContract
     * @param seqNo
     * @throws Exception
     */
    public PayContract detailPayContract(String seqNo) throws Exception {
        return new PayContractDAO().detailPayContract(seqNo);
    }
    /**
     * update PayContract
     * @param payContract
     * @throws Exception
     */
    public void updatePayContract(PayContract payContract) throws Exception {
        new PayContractDAO().updatePayContract(payContract);
    }
    /**
     * 查询合同版本号
     * @param pactType
     * @return
     */
	public String getPayContractPactVersNo(String pactType) {
		return new PayContractDAO().getPayContractPactVersNo(pactType);
	}
	public boolean existMerchant(String parameter) {
		return new PayContractDAO().existMerchant(parameter);
	}
	/**
	 * 获取序列号的最大值
	 * @return
	 */
	public String getSeqNo() {
		return new PayContractDAO().getSeqNo();
	}
	/**
	 * 检查合同是否存在
	 * @param custId
	 * @param contractId
	 * @return
	 */
	public boolean isExistContract(String custId,String contractId){
		return new PayContractDAO().isExistContract(custId,contractId);
	}
	/**
	 * 修改合同列值
	 * @param string
	 * @param string2
	 * @param parameter
	 * @return
	 * @throws Exception 
	 */
	public int updatePayContractColum(String column, String value,String seqNo) throws Exception {
		return new PayContractDAO().updatePayContractColumn(column,value,seqNo);
	}
	public Map getRemindPayContractExpires() throws Exception{
		Map map = new HashMap();
		if(new File(contractExpiresFileName).exists()){
			BufferedReader br = new BufferedReader(new FileReader(contractExpiresFileName));
			String line = null;
			while((line = br.readLine())!=null){
				String [] es = line.split("=");
				if(es.length!=2)continue;
				map.put(es[0].trim(),es[1].trim());
			}
			br.close();
		}
		return map;
	}
	public void setRemindPayContractExpires(String expires,String mobile,String email) throws Exception{
		Map map = new HashMap();
		FileOutputStream fos = new FileOutputStream(contractExpiresFileName);
		fos.write(("expires="+(expires == null?"":expires.replaceAll("，", ","))+"\r\n").getBytes());
		fos.write(("mobile="+(mobile == null?"":mobile.replaceAll("，", ","))+"\r\n").getBytes());
		fos.write(("email="+(expires == null?"":email.replaceAll("，", ","))+"\r\n").getBytes());
		fos.close();
	}
	public List getExpiresMerchant(String firsttDate,String secondDate,String thirddDate) {
		return  new PayContractDAO().getExpiresMerchant(firsttDate,secondDate,thirddDate);
	}
}