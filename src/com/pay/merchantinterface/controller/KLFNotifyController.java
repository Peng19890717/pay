package com.pay.merchantinterface.controller;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import util.PayUtil;
import com.PayConstant;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.pay.merchantinterface.service.NotifyInterface;
import com.pay.order.dao.PayOrder;
import com.pay.order.dao.PayReceiveAndPay;
import com.pay.order.dao.PayReceiveAndPayDAO;
import com.pay.order.service.PayReceiveAndPayService;
import com.third.klf.Md5Util;

@Controller
public class KLFNotifyController {
	private static final Log log = LogFactory.getLog(KLFNotifyController.class);

	@RequestMapping("KLFNotify")
	public String KLFNotify(HttpServletRequest request,HttpServletResponse response) {
		log.info("快乐付扫码通知开始==========================");
		OutputStream os = null;
		InputStream is = null;
		try {
			request.setCharacterEncoding("utf-8");
			// 取得通知信息
			StringBuffer req = new StringBuffer("");
			is = request.getInputStream();
			os = response.getOutputStream();
			byte[] b = new byte[1024];
			int len = -1;
			while ((len = is.read(b)) != -1)req.append(new String(b, 0, len, "utf-8"));
			log.info("扫码通知响应==================\n" + req.toString());
			JSONObject jsonObject = JSON.parseObject(req.toString());
			if ("100".equals(jsonObject.getString("status"))) {
				Map<String, String> params = new TreeMap<String, String>();
				params.put("status", jsonObject.getString("status"));
				params.put("result_code", jsonObject.getString("result_code"));
				params.put("out_trade_no", jsonObject.getString("out_trade_no"));
				params.put("total_fee", jsonObject.getString("total_fee"));
				if (Md5Util.MD5(params + PayConstant.PAY_CONFIG.get("KLF_MD5_KEY")).equals(jsonObject.getString("sign"))){
					if("0".equals(params.get("result_code"))){
						PayOrder tmpPayOrder = new PayOrder();
						tmpPayOrder.payordno = params.get("out_trade_no");
						tmpPayOrder.actdat = new Date();
						tmpPayOrder.ordstatus = "01";
						new NotifyInterface().notifyMer(tmpPayOrder);
					}else{
						PayOrder tmpPayOrder = new PayOrder();
		            	tmpPayOrder.payordno = params.get("out_trade_no");
		            	tmpPayOrder.actdat = new Date();
		            	tmpPayOrder.ordstatus="02";
		            	new NotifyInterface().notifyMer(tmpPayOrder);
					}
				} else throw new Exception("扫码通知验签失败");
			}else throw new Exception("支付未完成");
			os.write("success".getBytes());
		} catch (Exception e) {
			log.info(PayUtil.exceptionToString(e));
		} finally {
			if (os != null)try {os.close();} catch (Exception e) {}
			if (is != null)try {is.close();} catch (Exception e) {}
		}
		return null;
	}
	@RequestMapping("KLFDFNotify")
	public String KLFDFNotify(HttpServletRequest request,HttpServletResponse response){
		log.info("快乐付代付通知开始==========================");
		OutputStream os = null;
		InputStream is = null;
		try {
			request.setCharacterEncoding("utf-8");
			// 取得通知信息
			StringBuffer req = new StringBuffer("");
			is = request.getInputStream();
			os = response.getOutputStream();
			byte[] b = new byte[1024];
			int len = -1;
			while ((len = is.read(b)) != -1)
				req.append(new String(b, 0, len, "utf-8"));       
			log.info("代付通知响应==================\n" + req.toString());
			JSONObject jsonObject = JSON.parseObject(req.toString());
		    if("100".equals(jsonObject.getString("p2_Status"))){
				Map<String, String> params = new TreeMap<String, String>();
				params.put("p2_Status", jsonObject.getString("p2_Status"));
				params.put("p1_MerchantNo", jsonObject.getString("p1_MerchantNo"));
				params.put("p3_TotalFee", jsonObject.getString("p3_TotalFee"));
				params.put("p4_orderNum", jsonObject.getString("p4_orderNum"));
				if (Md5Util.MD5(params + PayConstant.PAY_CONFIG.get("KLF_DF_MD5_KEY")).equals(jsonObject.getString("sign"))){
					PayReceiveAndPayDAO dao = new PayReceiveAndPayDAO();
        			PayReceiveAndPay rp = new PayReceiveAndPay();
    				rp.id=params.get("p4_orderNum");
    				rp.status="1";
					rp.retCode="000";
					rp.errorMsg="交易成功";
					dao.updatePayReceiveAndPaystatus(rp);
					new PayReceiveAndPayService().notifyReceiveAndPayMer(rp);
				} else throw new Exception("代付通知验签失败");
			}else throw new Exception("支付未完成");
			os.write("success".getBytes());
		} catch (Exception e) {
			log.info(PayUtil.exceptionToString(e));
		} finally {
			if (os != null)try {os.close();} catch (Exception e) {}
			if (is != null)try {is.close();} catch (Exception e) {}
		}
		return null;
	}
}
