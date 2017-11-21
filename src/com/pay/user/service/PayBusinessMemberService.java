package com.pay.user.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.pay.user.dao.PayBusinessMember;
import com.pay.user.dao.PayBusinessMemberDAO;

/**
 * Object PAY_BUSINESS_MEMBER service. 
 * @author Administrator
 *
 */
public class PayBusinessMemberService {
    /**
     * 获取业务员的Json数据
     * @return
     */
	public String getJson() {
		try {
            PayBusinessMemberDAO payBusinessMemberDAO = new PayBusinessMemberDAO();
            List<PayBusinessMember> list = payBusinessMemberDAO.getList("onemonth");
            JSONObject json = new JSONObject();
            json.put("total", list.size());
            JSONArray row = new JSONArray();
            PayBusinessMember payBusinessMember = new PayBusinessMember();
            //放入最外层节点用于显示（该节点id=0,pId=00）
            payBusinessMember.setRealName("全部业务员");
            payBusinessMember.setUserId("0");
            row.put(payBusinessMember.toJson("00"));
            for(int i = 0; i<list.size(); i++){
            	PayBusinessMember member = (PayBusinessMember) list.get(i);
            	row.put(member.toJson(member.parentId));
            }
            json.put("rows", row);
            return json.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
	}

	public List<PayBusinessMember> getBusiMemWithoutRelation() throws Exception{
		return new PayBusinessMemberDAO().getBusiMemWithoutRelation();
	}
	/**
	 * 删除该节点及其子节点
	 * @param userId
	 * @throws Exception
	 */
	public void removeBusiMem(String userId) throws Exception{
		PayBusinessMemberDAO dao = new PayBusinessMemberDAO();
		List <PayBusinessMember>list = dao.getList("bussMemOrder");
		Map <String,PayBusinessMember>merchantMap = new HashMap<String,PayBusinessMember>();
		for(int i = 0; i<list.size(); i++){
			PayBusinessMember tmp =  list.get(i);
			merchantMap.put(tmp.userId, tmp);
		}
		//创建树形结构，root为根节点
		for(int i = 0; i<list.size(); i++){
			PayBusinessMember tmp =  list.get(i);
			PayBusinessMember parent = merchantMap.get(tmp.parentId);
			if(parent!=null)parent.subList.add(tmp);
		}
		List<String> tmpList = new ArrayList<String>();
		getAllDelMem(tmpList,merchantMap.get(userId));
		dao.removeBusiMem(tmpList);
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
}