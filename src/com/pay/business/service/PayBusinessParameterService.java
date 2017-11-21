package com.pay.business.service;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import util.DataTransUtil;
import util.JWebConstant;
import util.SHA1;
import util.Tools;

import com.PayConstant;
import com.pay.business.dao.PayBusinessParameter;
import com.pay.business.dao.PayBusinessParameterDAO;
import com.pay.merchantinterface.service.CJPayService;
import com.pay.merchantinterface.service.NotifyInterface;
import com.pay.merchantinterface.service.PayChannelService;

/**
 * Object PAY_BUSINESS_PARAMETER service. 
 * @author Administrator
 *
 */
public class PayBusinessParameterService {
    /**
     * Get records list(json).
     * @return
     */
    public String getPayBusinessParameterList(PayBusinessParameter payBusinessParameter,int page,int rows,String sort,String order){
        try {
            PayBusinessParameterDAO payBusinessParameterDAO = new PayBusinessParameterDAO();
            List list = payBusinessParameterDAO.getPayBusinessParameterList(payBusinessParameter, page, rows, sort, order);
            JSONObject json = new JSONObject();
            json.put("total", String.valueOf(payBusinessParameterDAO.getPayBusinessParameterCount(payBusinessParameter)));
            JSONArray row = new JSONArray();
            for(int i = 0; i<list.size(); i++){
                row.put(i, ((PayBusinessParameter)list.get(i)).toJson());
            }
            json.put("rows", row);
            return json.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
    /**
     * add PayBusinessParameter
     * @param payBusinessParameter
     * @throws Exception
     */
    public void addPayBusinessParameter(PayBusinessParameter payBusinessParameter) throws Exception {
        new PayBusinessParameterDAO().addPayBusinessParameter(payBusinessParameter);
    }
    /**
     * remove PayBusinessParameter
     * @param name
     * @throws Exception
     */
    public void removePayBusinessParameter(String name) throws Exception {
        new PayBusinessParameterDAO().removePayBusinessParameter(name);
    }
    /**
     * update PayBusinessParameter
     * @param payBusinessParameter
     * @throws Exception
     */
    public void updatePayBusinessParameter(PayBusinessParameter payBusinessParameter) throws Exception {
        new PayBusinessParameterDAO().updatePayBusinessParameter(payBusinessParameter);
    }
    /**
     * detail PayBusinessParameter
     * @param name
     * @throws Exception
     */
    public PayBusinessParameter detailPayBusinessParameter(String name) throws Exception {
        return new PayBusinessParameterDAO().detailPayBusinessParameter(name);
    }
    /**
     * 将数据库中的业务参数加载到内存
     * @param initFlag 0：系统启动不进行远程同步；1：修改远程同步
     * @throws Exception 
     */
	public static void executePayBusinessParameter(String initFlag) throws Exception {
		try {
			List<PayBusinessParameter> tmp = new PayBusinessParameterDAO().getList();
			for (PayBusinessParameter payBusinessParameter : tmp) {
				PayConstant.PAY_CONFIG.put(payBusinessParameter.name,payBusinessParameter.value);
			}
			PayChannelService.init();
			NotifyInterface.init();
			CJPayService.init();
			if("0".equals(initFlag))return;
			Thread t = new Thread(){
				public void run(){
					try {
						String random = Tools.getUniqueIdentify();
						System.out.println("同步商户门户："+PayConstant.PAY_CONFIG.get("PAY_MER_GATEWAY_URL"));
						String msg = new String(new DataTransUtil().doPost(PayConstant.PAY_CONFIG.get("PAY_MER_GATEWAY_URL"),("random="+random+"&sign="+
										SHA1.SHA1String(random+PayConstant.PAY_CONFIG.get("SYS_COMM_PWD_WITH_INNER_PLT"))).getBytes("utf-8")),"utf-8");
						if(!JWebConstant.OK.equals(msg))throw new Exception("同步商户门户错误（"+msg+"）");
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			};
			t.start();
			Thread t1 = new Thread(){
				public void run(){
					try {
						String random = Tools.getUniqueIdentify();
						System.out.println("同步用户门户："+PayConstant.PAY_CONFIG.get("PAY_USER_GATEWAY_URL"));
						String msg1 = new String(new DataTransUtil().doPost(PayConstant.PAY_CONFIG.get("PAY_USER_GATEWAY_URL"),("random="+random+"&sign="+
								SHA1.SHA1String(random+PayConstant.PAY_CONFIG.get("SYS_COMM_PWD_WITH_INNER_PLT"))).getBytes("utf-8")),"utf-8");
						if(!JWebConstant.OK.equals(msg1))throw new Exception("同步用户门户错误（"+msg1+"）");
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			};
			t1.start();
			util.PayUtil.synNotifyForAllNode("11");
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	/**
	 * 判断添加的业务参数是否存在
	 * @param name
	 * @return
	 */
	public boolean existName(String name) {
		return new PayBusinessParameterDAO().existName(name);
	}
}