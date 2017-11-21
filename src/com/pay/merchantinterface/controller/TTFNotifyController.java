package com.pay.merchantinterface.controller;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import util.PayUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pay.merchantinterface.service.NotifyInterface;
import com.pay.order.dao.PayOrder;
import com.third.ttf.SumpaySubmit;
/**
 * 统统付通知接口
 * @author Administrator
 */
@Controller
public class TTFNotifyController {
	private static final Log log = LogFactory.getLog(TTFNotifyController.class);
    /**
     * 通知地址
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("TTFNotify")
    public String TTFNotify(HttpServletRequest request,HttpServletResponse response) {
    	log.info("统统付通知开始=============");
    	OutputStream os = null;
    	InputStream is = null;
    	try {
    		request.setCharacterEncoding("utf-8");
    		//取得通知信息
    		StringBuffer req = new StringBuffer("");
    		is = request.getInputStream();
    		os = response.getOutputStream();
    		byte[] b = new byte[1024];
    		int len = -1;
    		while((len = is.read(b)) != -1) req.append(new String(b,0,len,"utf-8"));
    		log.info("通知响应==================\n"+req.toString());
    		/**
    		 * {"feeAmount":0,"oboType":"00","obostatus":"00","orderAmount":0.01,"orderId":"1504150878530","resp_code":"000000","resp_msg":"success",,"tradeId":"B122351"
    		 * "sign":"NmtTltWH7w+q9+H7xrmO0v6WaUuC24BgxQtd+taGf0EAllVBPCjgztHRIce+71yc/vBEXc/cbtP5ejp61+Dd1ZgjubcRGFl83q50ecmtbHBD1bcDIERkHT3BNeOpf9A+8XQ0UnsI/1on4WbH+CqgICnJjdOU2kTIBio3rQlpME39EAdgS5N3KELYBrQRuQY3lyB7kX3ZIbytN/vufw1FqHUEDSkWx3crnFuOW7A/Q3ptN8jpw9NwQdjsQ7kwkKLYw1rGLgLJiQkDpiATFQrG2jtCsesoX+l6OsOufmjNrv9X0/YZQTCQdD7QSd7i6c9GeVxBYMXBuJw8u4cSH0I73Q==","sign_type":"RSA"}
    		 */
    		Gson gson = new Gson();
    		Map<String, String> params = gson.fromJson(req.toString(), new TypeToken<Map<String, String>>() {
    		}.getType());
    		//验签
    		if(SumpaySubmit.getSignVeryfy(params, params.get("sign"))){
    			if (params.get("resp_code").equals("000000")) {
    				if ("1".equals(params.get("status"))) {//支付成功
    					PayOrder tmpPayOrder = new PayOrder();
                    	tmpPayOrder.payordno = params.get("order_no");
                    	tmpPayOrder.actdat = new Date();
                    	tmpPayOrder.ordstatus="01";
                    	new NotifyInterface().notifyMer(tmpPayOrder);
    				}
    				os.write("{'resp_code':'000000'}".getBytes());
    			} else {
    				os.write("{'resp_code':'000001','resp_msg':'失败'}".getBytes());
    			}
    		}else{
    			os.write("{resp_code:000001,resp_msg:失败}".getBytes());
    			throw new Exception("验签失败");
    		}
        } catch (Exception e) {
        	log.info(PayUtil.exceptionToString(e));
        } finally {
        	if(os!=null)try {os.close();} catch (Exception e) {}
        	if(is != null) try { is.close(); } catch (Exception e) {}
        }
        return null;
    }
}