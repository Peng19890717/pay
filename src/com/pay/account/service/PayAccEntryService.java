package com.pay.account.service;

import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

import com.pay.account.dao.PayAccEntry;
import com.pay.account.dao.PayAccEntryDAO;
import com.pay.account.dao.SimplePayAccSubject;

/**
 * Object PAY_ACC_ENTRY service. 
 * @author Administrator
 *
 */
public class PayAccEntryService {
    /**
     * Get records list(json).
     * @return
     */
    public String getPayAccEntryList(PayAccEntry payAccEntry,int page,int rows,String sort,String order){
        try {
            PayAccEntryDAO payAccEntryDAO = new PayAccEntryDAO();
            List list = payAccEntryDAO.getPayAccEntryList(payAccEntry, page, rows, sort, order);
            JSONObject json = new JSONObject();
            json.put("total", String.valueOf(payAccEntryDAO.getPayAccEntryCount(payAccEntry)));
            JSONArray row = new JSONArray();
            for(int i = 0; i<list.size(); i++){
                row.put(i, ((PayAccEntry)list.get(i)).toJson());
            }
            json.put("rows", row);
            return json.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
    /**
     * add PayAccEntry
     * @param payAccEntry
     * @throws Exception
     */
    public void addPayAccEntry(PayAccEntry payAccEntry) throws Exception {
        new PayAccEntryDAO().addPayAccEntry(payAccEntry);
    }
    /**
     * remove PayAccEntry
     * @param txnCod
     * @throws Exception
     */
    public void removePayAccEntry(String id) throws Exception {
        new PayAccEntryDAO().removePayAccEntry(id);
    }
    /**
     * update PayAccEntry
     * @param payAccEntry
     * @throws Exception
     */
    public void updatePayAccEntry(PayAccEntry payAccEntry) throws Exception {
        new PayAccEntryDAO().updatePayAccEntry(payAccEntry);
    }
    /**
     * detail PayAccEntry
     * @param txnCod
     * @throws Exception
     */
    public PayAccEntry detailPayAccEntry(String id) throws Exception {
        return new PayAccEntryDAO().detailPayAccEntry(id);
    }
    //获取科目信息
    public List<SimplePayAccSubject> getPayAccSUBJECT() throws Exception {
        return new PayAccEntryDAO().getPayAccSUBJECT();
    }
    //用于添加时判断提交的信息是否存在。
    public boolean isRepeat(PayAccEntry payAccEntry) throws Exception{
    	
    	return new PayAccEntryDAO().isRepeat(payAccEntry);
    }

}