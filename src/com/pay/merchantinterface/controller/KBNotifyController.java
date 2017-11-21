package com.pay.merchantinterface.controller;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import util.JWebConstant;
import util.PayUtil;
import com.PayConstant;
import com.pay.merchantinterface.service.NotifyInterface;
import com.pay.order.dao.PayOrder;
import com.third.kb.Utils;

@Controller
public class KBNotifyController {
	private static final Log log = LogFactory.getLog(KBNotifyController.class);
	private static String pubKeyPath = JWebConstant.APP_PATH+JWebConstant.PATH_DIV+"config"+JWebConstant.PATH_DIV+PayConstant.PAY_CONFIG.get("KB_PUB_KEY");//公钥
	private static String priKeyPaht = JWebConstant.APP_PATH+JWebConstant.PATH_DIV+"config"+JWebConstant.PATH_DIV+PayConstant.PAY_CONFIG.get("KB_PRV_KEY");//商户私钥
	
	@RequestMapping("KBNotify")
    public String KBNotify(HttpServletRequest request,HttpServletResponse response) {
		OutputStream os = null;
    	InputStream is = null;
    	log.info("酷宝通知开始================");
        try {
        	 request.setCharacterEncoding("UTF-8");
        	 is = request.getInputStream();
        	 os = response.getOutputStream();
        	 String res = "";
     		 byte[] b = new byte[1024];
     		 int len = -1;
     		 while((len = is.read(b)) != -1){
     			res += new String(b,0,len,"UTF-8");
     		 }
     		 log.info("酷宝扫码异步通知数据:" + res);
     		 /**
     		  * acDate=20170707&backParam=&bankAbbr=CMBC&ch_ord_no=4009082001201707079332693918&charset=00&fee=0&merchantId=888010080990001&orderId=J4TBXMM33JLXPT2Q1&orderTime=20170707115807&payTime=20170707115934&purchaserId=J4TBXMM33JLXPT2Q1&returnCode=000000&returnMessage=SUCCESS
     		  * &serverCert=308203653082024DA00302010202081BF6C2A9D997293C300D06092A864886F70D0101050500305B310B300906035504061302636E310B300906035504080C02626A3110300E06035504070C076265696A696E67310F300D060355040A0C064D75726F6E67310B3009060355040B0C024954310F300D06035504030C06726F6F744341301E170D3135303131393130303530365A170D3435303131393130303530365A305A310B300906035504061302636E310B300906035504080C02626A3110300E06035504070C076265696A696E67310F300D060355040A0C064D75726F6E67310B3009060355040B0C024954310E300C06035504030C05506179434130819F300D06092A864886F70D010101050003818D0030818902818100CEA9A82C9F5E6BF6AE9426EAF879CF0B8614B1C59818B841DBEE3BD44A09F03DEBD59871D975BFE42F19177342D00988A13912B153F0CC72C16EBD23C8596610F45978797414C5D211FB3DE1DA3944A8D75E7A0749EFE2E36DC46FCC42A373FFCD8F4B1C69E6D9789EEB51BF01AABE43E940652DE8F890004A965B07E49A614D0203010001A381B13081AE301D0603551D0E0416041439E6E108F96F61A1FBEA466C406666E545742BD530818C0603551D23048184308181801407C69254961EB26785D105D969445BF72CF3CCA7A15FA45D305B310B300906035504061302636E310B300906035504080C02626A3110300E06035504070C076265696A696E67310F300D060355040A0C064D75726F6E67310B3009060355040B0C024954310F300D06035504030C06726F6F74434182081BF6C2A9D76A7CF2300D06092A864886F70D010105050003820101003498678C1547E2D86CCBEE81971E54221EFBDFFF85F3BAA9BB5C6EC217E02DDD48641B132BB4AAF87BE91461304F7457554465F767D29E61266DC0212F8D1DAE06743B6D62125BE989DF80B889FCA6DC25D67F28581A8C44B400D754F3B0542F56603432131E690F8FC4D632717F914A199E080FABECCD4D931EF4288330B4E9CF684409E3CA2D026EDDD4DFC473C7ADEE759E15CD2937CA95DC2F347FAE4E67257CE9941E361EBAAEC1445CB01DBE74E3BDFF62E4FF6D378C0559E4D75AD42C10B78A1999280EA9A5C3FCE22808A722D3D9C9E8929CF02D0CDB7938E30478FBE21D5F53E7A1646F3510A6A4E062748453B23620273927D0F8A3A8622714DAEB&serverSign=6C10D025AD00E96981CFD7301CBE5D95E64F19AB414B3061E6D2360A11623E58742938DCA4303CB9393F8CCD156A406ABA7DAAEA9BED4AC135E35AB579537A01C0F65375BF65A4715C69C4A43A2F1CADDB65B8E38113B9A0265C3D183541066795C149A523D02AA54CFCDF37EBB02566C4883E9D666319FF212C00FAC46BF456
     		  * &service=OFFLINERESULT&signType=RSA&status=SUCCESS&totalAmount=1&tradeNo=201707070040554133&version=1.2
     		  */
     		 Map<String, String> configLoadMap = new HashMap<String, String>();
    		 configLoadMap.put("certPath", priKeyPaht);
    		 configLoadMap.put("rootCertPath", pubKeyPath);
    		 Map<String,String> resultMap = Utils.analysis(res, configLoadMap,"OFFLINERESULT");
    		 Boolean verifySign = Utils.verifySign(res, configLoadMap, resultMap, PayConstant.PAY_CONFIG.get("KB_PRV_PASSWROD"), "GBK");
             //验签
			 if(Utils.verifySign(res, configLoadMap, resultMap, PayConstant.PAY_CONFIG.get("KB_PRV_PASSWROD"), "GBK")){
				 if("000000".equals(resultMap.get("returnCode")) && "SUCCESS".equals(resultMap.get("status"))){
                	PayOrder tmpPayOrder = new PayOrder();
                	tmpPayOrder.payordno = resultMap.get("orderId");
	            	tmpPayOrder.actdat = new Date();
	            	tmpPayOrder.ordstatus="01";
	            	new NotifyInterface().notifyMer(tmpPayOrder);
	            	//返回确认收到支付成功结果
	            	//签名
	            	String resData = "{\"result\":\"success\"}";
	                os.write(resData.getBytes());
	             }else{
                	PayOrder tmpPayOrder = new PayOrder();
                	tmpPayOrder.payordno = resultMap.get("orderId");
	            	tmpPayOrder.actdat = new Date();
	            	tmpPayOrder.ordstatus="02";
	            	new NotifyInterface().notifyMer(tmpPayOrder);
	             }
			 }else throw new Exception("验签失败");
        } catch (Exception e) {
        	log.info(PayUtil.exceptionToString(e));
        	e.printStackTrace();
        } finally {
        	if(is!=null)try {is.close();} catch (Exception e) {}
        	if(os!=null)try {os.close();} catch (Exception e) {}
        }
        return null;
    }
}
