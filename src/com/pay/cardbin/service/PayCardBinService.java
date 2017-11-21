package com.pay.cardbin.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.pay.bank.dao.PayBank;
import com.pay.cardbin.dao.PayCardBin;
import com.pay.cardbin.dao.PayCardBinDAO;

/**
 * Object PAY_CARD_BIN service. 
 * @author Administrator
 *
 */
public class PayCardBinService {
	public static Map<String, String> BANK_CODE_NAME_MAP = new HashMap();
	public static Map<String, PayBank> BANK_NAME_MAP = new HashMap();
	public static List<PayBank> BANK_CODE_NAME_LIST = new ArrayList();
	static Map  <String,PayCardBin>CARD_BIN_MAP= new HashMap<String, PayCardBin>();
	static {
		init();
	}
	public static void init(){
		try {
			PayCardBinDAO dao = new PayCardBinDAO();
			CARD_BIN_MAP = dao.getCardBinMap();
			BANK_CODE_NAME_LIST = dao.getBankList();
			util.PayUtil.synNotifyForAllNode("0");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 根据卡号取得卡bin
	 * @param cardNo
	 * @return
	 */
	public static PayCardBin getCardBinByCardNo(String cardNo){
		if(cardNo == null || cardNo.length()>20 || cardNo.length()<14)return null;
		//卡前缀长度使用频率排序，6最多，2最小
		int [] binLenTimes = new int []{9,6,11,10,8,7,5,12,3,4};
		//卡前缀长度为3-11
		for(int i=0; i<binLenTimes.length; i++){
			PayCardBin cardBin = CARD_BIN_MAP.get(cardNo.substring(0,binLenTimes[i])+","+cardNo.length());
			if(cardBin != null)return cardBin;
		}
		return null;
	}
    /**
     * Get records list(json).
     * @return
     */
    public String getPayCardBinList(PayCardBin payCardBin,int page,int rows,String sort,String order){
        try {
            PayCardBinDAO payCardBinDAO = new PayCardBinDAO();
            List list = payCardBinDAO.getPayCardBinList(payCardBin, page, rows, sort, order);
            JSONObject json = new JSONObject();
            json.put("total", String.valueOf(payCardBinDAO.getPayCardBinCount(payCardBin)));
            JSONArray row = new JSONArray();
            for(int i = 0; i<list.size(); i++){
                row.put(i, ((PayCardBin)list.get(i)).toJson());
            }
            json.put("rows", row);
            return json.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
    /**
     * add PayCardBin
     * @param payCardBin
     * @throws Exception
     */
    public void addPayCardBin(PayCardBin payCardBin) throws Exception {
        new PayCardBinDAO().addPayCardBin(payCardBin);
        init();
    }
    /**
     * remove PayCardBin
     * @param binId
     * @throws Exception
     */
    public void removePayCardBin(String binId) throws Exception {
        new PayCardBinDAO().removePayCardBin(binId);
        init();
    }
    /**
     * update PayCardBin
     * @param payCardBin
     * @throws Exception
     */
    public void updatePayCardBin(PayCardBin payCardBin) throws Exception {
        new PayCardBinDAO().updatePayCardBin(payCardBin);
        init();
    }
    /**
     * detail PayCardBin
     * @param binId
     * @throws Exception
     */
    public PayCardBin detailPayCardBin(String binId) throws Exception {
        return new PayCardBinDAO().detailPayCardBin(binId);
    }
}