package com.pay.account.service;

import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import util.Tools;

import com.jweb.dao.JWebUser;
import com.pay.account.dao.PayAccSubject;
import com.pay.account.dao.PayAccSubjectDAO;

/**
 * Object PAY_ACC_SUBJECT service. 
 * @author Administrator
 *
 */
public class PayAccSubjectService {
    /**
     * Get records list(json).
     * @return
     */
    public String getPayAccSubjectList(PayAccSubject payAccSubject,int page,int rows,String sort,String order){
        try {
            PayAccSubjectDAO payAccSubjectDAO = new PayAccSubjectDAO();
            List list = payAccSubjectDAO.getPayAccSubjectList(payAccSubject, page, rows, sort, order);
            JSONObject json = new JSONObject();
            json.put("total", String.valueOf(payAccSubjectDAO.getPayAccSubjectCount(payAccSubject)));
            JSONArray row = new JSONArray();
            for(int i = 0; i<list.size(); i++){
                row.put(i, ((PayAccSubject)list.get(i)).toJson());
            }
            json.put("rows", row);
            return json.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
    /**
     * add PayAccSubject
     * @param payAccSubject
     * @throws Exception
     */
    public void addPayAccSubject(JWebUser user,PayAccSubject payAccSubject) throws Exception {
    	//增加科目
    	payAccSubject.setGlCode(Tools.getUniqueIdentify());
    	payAccSubject.setCreateTime(new Date());
    	payAccSubject.setCreateUser(user.getId());
    	payAccSubject.setLastUpdTime(new Date());
    	payAccSubject.setLastUpdUser(user.getId());
    	//暂时没用的字段
    	payAccSubject.setOpnTime(new Date());
    	payAccSubject.setClsTime(new Date());
    	payAccSubject.setLastTxnDate(new Date());

    	new PayAccSubjectDAO().addPayAccSubject(payAccSubject);
    }
    /**
     * remove PayAccSubject
     * @param glCode
     * @throws Exception
     */
    public void removePayAccSubject(String glCode) throws Exception {
        new PayAccSubjectDAO().removePayAccSubject(glCode);
    }
    /**
     * update PayAccSubject
     * @param payAccSubject
     * @throws Exception
     */
    public void updatePayAccSubject(JWebUser user,PayAccSubject payAccSubject) throws Exception {
    	payAccSubject.setLastUpdTime(new Date());
    	payAccSubject.setLastUpdUser(user.getId());
        new PayAccSubjectDAO().updatePayAccSubject(payAccSubject);
    }
    /**
     * detail PayAccSubject
     * @param glCode
     * @throws Exception
     */
    public PayAccSubject detailPayAccSubject(String glCode) throws Exception {
        return new PayAccSubjectDAO().detailPayAccSubject(glCode);
    }
    
    /**
     * 根据科目名字查询
     * @param glName
     * @return
     * @throws Exception
     */
    public PayAccSubject detailPayAccSubjectByGlName(String glName) throws Exception {
        return new PayAccSubjectDAO().detailPayAccSubjectByGlName(glName);
    }
    
    /**
     * 根据科目名字查询多个
     * @param glName
     * @return
     * @throws Exception
     */
    public List<PayAccSubject> detailPayAccSubjectListByGlName(String glName) throws Exception {
        return (List<PayAccSubject>)new PayAccSubjectDAO().detailPayAccSubjectListByGlName(glName);
    }

}