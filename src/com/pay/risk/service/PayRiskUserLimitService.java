package com.pay.risk.service;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import util.Tools;

import com.PayConstant;
import com.jweb.service.TransactionService;
import com.pay.risk.dao.PayRiskUserLimit;
import com.pay.risk.dao.PayRiskUserLimitDAO;
import com.pay.risk.dao.PayRiskUserLimitSub;
import com.pay.user.dao.PayTranUserInfoDAO;
/**
 * Object PAY_RISK_USER_LIMIT service. 
 * @author Administrator
 *
 */
public class PayRiskUserLimitService extends TransactionService{
	public static Map<String,PayRiskUserLimit> LIMIT_USER_NO_KEY = new HashMap<String,PayRiskUserLimit>();
	public static Map <String,PayRiskUserLimit>LIMIT_NORMAL_KEY = new HashMap<String,PayRiskUserLimit>();
	static {
		loadUserLimit();
	}
	/**
	 * 载入商户限额表，限额表保存在内存，随时调用
	 * 指定商户情况下，商户号+业务类型为主键
	 * 其他情况下，业务类型为主键
	 */
	public static void loadUserLimit(){
		LIMIT_USER_NO_KEY = new HashMap();
		LIMIT_NORMAL_KEY = new HashMap();
		try {
			List<PayRiskUserLimit>list = new PayRiskUserLimitDAO().getList();
			for(int i = 0; i<list.size(); i++){
				PayRiskUserLimit lim = list.get(i);
				//指定用户
				if("3".equals(lim.userType)){
					LIMIT_USER_NO_KEY.put(lim.userCode+","+lim.tranType, lim);
				//非指定用户
				} else {
					LIMIT_NORMAL_KEY.put(lim.userType+","+lim.xListType+","+lim.tranType,lim);
				}
			}
			util.PayUtil.synNotifyForAllNode("10");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
    /**
     * Get records list(json).
     * @return
     */
    public String getPayRiskUserLimitList(PayRiskUserLimit payRiskUserLimit,int page,int rows,String sort,String order){
        try {
            PayRiskUserLimitDAO payRiskUserLimitDAO = new PayRiskUserLimitDAO();
            List list = payRiskUserLimitDAO.getPayRiskUserLimitList(payRiskUserLimit, page, rows, sort, order);
            JSONObject json = new JSONObject();
            json.put("total", String.valueOf(payRiskUserLimitDAO.getPayRiskUserLimitCount(payRiskUserLimit)));
            JSONArray row = new JSONArray();
            for(int i = 0; i<list.size(); i++){
                row.put(i, ((PayRiskUserLimit)list.get(i)).toJson());
            }
            json.put("rows", row);
            return json.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
    /**
     * add PayRiskUserLimit
     * @param request
     * @param payRiskUserLimit
     * @param payRiskUserLimitDAO 
     * @throws Exception
     */
    public void addPayRiskUserLimit(HttpServletRequest request,PayRiskUserLimit payRiskUserLimit) throws Exception {
    	//状态类型 1:实名，2:非实名，3指定用户
    	if("3".equals(payRiskUserLimit.userType)&&
    		new PayTranUserInfoDAO().detailPayTranUserInfoByCustId(payRiskUserLimit.userCode)==null)throw new Exception("用户不存在");
    	payRiskUserLimit.id = Tools.getUniqueIdentify();
    	payRiskUserLimit.userCode=payRiskUserLimit.userCode==null||payRiskUserLimit.userCode.length()==0?"-1":payRiskUserLimit.userCode;
    	PayRiskUserLimitSub uLimitSub = new PayRiskUserLimitSub();
    	if(!PayConstant.TRAN_TYPE_CARD.equals(payRiskUserLimit.tranType)){
    		//取得各种情况消费限额
    		if(PayConstant.TRAN_TYPE_CONSUME.equals(payRiskUserLimit.tranType)){
    			for(int i = 0; i<PayConstant.TRAN_TYPE_CONSUMES.length; i++){
    				uLimitSub = new PayRiskUserLimitSub();
    				uLimitSub.id = Tools.getUniqueIdentify();
    	    		uLimitSub.limitBusClient = "0"+i;
    	    		uLimitSub.minAmt = Long.parseLong(request.getParameter("minAmt0"+i));
    	    		uLimitSub.maxAmt = Long.parseLong(request.getParameter("maxAmt0"+i));
    	    		uLimitSub.dayTimes = Long.parseLong(request.getParameter("dayTimes0"+i));
    	    		uLimitSub.daySucTimes = Long.parseLong(request.getParameter("daySucTimes0"+i));
    	    		uLimitSub.dayAmt = Long.parseLong(request.getParameter("dayAmt0"+i));
    	    		uLimitSub.daySucAmt = Long.parseLong(request.getParameter("daySucAmt0"+i));
    	    		uLimitSub.monthTimes = Long.parseLong(request.getParameter("monthTimes0"+i));
    	    		uLimitSub.monthSucTimes = Long.parseLong(request.getParameter("monthSucTimes0"+i));
    	    		uLimitSub.monthAmt = Long.parseLong(request.getParameter("monthAmt0"+i));
    	    		uLimitSub.monthSucAmt = Long.parseLong(request.getParameter("monthSucAmt0"+i));
    	    		uLimitSub.riskUserLimitId = payRiskUserLimit.id;
    	    		payRiskUserLimit.subLimitList.add(uLimitSub);
    			}
    		} else {
    			//限额
        		uLimitSub.id = Tools.getUniqueIdentify();
        		uLimitSub.limitBusClient = "-1";
        		uLimitSub.minAmt = Long.parseLong(request.getParameter("minAmt"));
        		uLimitSub.maxAmt = Long.parseLong(request.getParameter("maxAmt"));
        		uLimitSub.dayTimes = Long.parseLong(request.getParameter("dayTimes"));
        		uLimitSub.daySucTimes = Long.parseLong(request.getParameter("daySucTimes"));
        		uLimitSub.dayAmt = Long.parseLong(request.getParameter("dayAmt"));
        		uLimitSub.daySucAmt = Long.parseLong(request.getParameter("daySucAmt"));
        		uLimitSub.monthTimes = Long.parseLong(request.getParameter("monthTimes"));
        		uLimitSub.monthSucTimes = Long.parseLong(request.getParameter("monthSucTimes"));
        		uLimitSub.monthAmt = Long.parseLong(request.getParameter("monthAmt"));
        		uLimitSub.monthSucAmt = Long.parseLong(request.getParameter("monthSucAmt"));
        		uLimitSub.riskUserLimitId = payRiskUserLimit.id;
        		payRiskUserLimit.subLimitList.add(uLimitSub);
    		}
    	}
    	new PayRiskUserLimitDAO().addPayRiskUserLimit(payRiskUserLimit);
    	loadUserLimit();
    }
    /**
     * remove PayRiskUserLimit
     * @param id
     * @throws Exception
     */
    public void removePayRiskUserLimit(String id) throws Exception {
        new PayRiskUserLimitDAO().removePayRiskUserLimit(id);
        loadUserLimit();
    }
    /**
     * update PayRiskUserLimit
     * @param payRiskUserLimit
     * @throws Exception
     */
    public void updatePayRiskUserLimit(HttpServletRequest request,PayRiskUserLimit payRiskUserLimit) throws Exception {
    	PayRiskUserLimitSub uLimitSub = new PayRiskUserLimitSub();
    	try {
    		if(!PayConstant.TRAN_TYPE_CARD.equals(payRiskUserLimit.tranType)){
        		//取得各种情况消费限额
        		if(PayConstant.TRAN_TYPE_CONSUME.equals(payRiskUserLimit.tranType)){
        			for(int i = 0; i<PayConstant.TRAN_TYPE_CONSUMES.length; i++){
        				uLimitSub = new PayRiskUserLimitSub();
        				uLimitSub.id = Tools.getUniqueIdentify();
        	    		uLimitSub.limitBusClient = "0"+i;
        	    		uLimitSub.minAmt = Long.parseLong(request.getParameter("minAmt0"+i));
        	    		uLimitSub.maxAmt = Long.parseLong(request.getParameter("maxAmt0"+i));
        	    		uLimitSub.dayTimes = Long.parseLong(request.getParameter("dayTimes0"+i));
        	    		uLimitSub.daySucTimes = Long.parseLong(request.getParameter("daySucTimes0"+i));
        	    		uLimitSub.dayAmt = Long.parseLong(request.getParameter("dayAmt0"+i));
        	    		uLimitSub.daySucAmt = Long.parseLong(request.getParameter("daySucAmt0"+i));
        	    		uLimitSub.monthTimes = Long.parseLong(request.getParameter("monthTimes0"+i));
        	    		uLimitSub.monthSucTimes = Long.parseLong(request.getParameter("monthSucTimes0"+i));
        	    		uLimitSub.monthAmt = Long.parseLong(request.getParameter("monthAmt0"+i));
        	    		uLimitSub.monthSucAmt = Long.parseLong(request.getParameter("monthSucAmt0"+i));
        	    		uLimitSub.riskUserLimitId = payRiskUserLimit.id;
        	    		payRiskUserLimit.subLimitList.add(uLimitSub);
        			}
        		} else {
        			//限额
            		uLimitSub.id = Tools.getUniqueIdentify();
            		uLimitSub.limitBusClient = "-1";
            		uLimitSub.minAmt = Long.parseLong(request.getParameter("minAmt"));
            		uLimitSub.maxAmt = Long.parseLong(request.getParameter("maxAmt"));
            		uLimitSub.dayTimes = Long.parseLong(request.getParameter("dayTimes"));
            		uLimitSub.daySucTimes = Long.parseLong(request.getParameter("daySucTimes"));
            		uLimitSub.dayAmt = Long.parseLong(request.getParameter("dayAmt"));
            		uLimitSub.daySucAmt = Long.parseLong(request.getParameter("daySucAmt"));
            		uLimitSub.monthTimes = Long.parseLong(request.getParameter("monthTimes"));
            		uLimitSub.monthSucTimes = Long.parseLong(request.getParameter("monthSucTimes"));
            		uLimitSub.monthAmt = Long.parseLong(request.getParameter("monthAmt"));
            		uLimitSub.monthSucAmt = Long.parseLong(request.getParameter("monthSucAmt"));
            		uLimitSub.riskUserLimitId = payRiskUserLimit.id;
            		payRiskUserLimit.subLimitList.add(uLimitSub);
        		}
        		new PayRiskUserLimitDAO().updatePayRiskUserLimit(payRiskUserLimit);
        	} else {
        		new PayRiskUserLimitDAO().updatePayRiskUserLimitForCard(payRiskUserLimit);
        	}
    		loadUserLimit();
		} catch (Exception e) {
			throw e;
		}
    }
    /**
     * detail PayRiskUserLimit
     * @param id
     * @throws Exception
     */
    public PayRiskUserLimit detailPayRiskUserLimit(String id) throws Exception {
        return new PayRiskUserLimitDAO().detailPayRiskUserLimit(id);
    }
	/**
	 * 获取用户限额
	 * @param custId
	 * @param userType 状态类型 1:实名，2:非实名，3指定用户，4银行卡
	 * @param xListType 名单类型 1白名单 4红名单，不同名单限额不同
	 * @param limitType 限额类型:1消费 2充值  5提现 6转账
	 * @return
	 */
	public static PayRiskUserLimit getUserLimit(String custId,String userType,String xListType,String limitType){
		//用户号获取
		PayRiskUserLimit lim = LIMIT_USER_NO_KEY.get(custId+","+limitType);
		//未指定用户限额
		if(lim==null) lim = LIMIT_NORMAL_KEY.get(userType+","+xListType+","+limitType);
		return lim;
	}
}