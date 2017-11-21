package com.third.cx;
import java.io.IOException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;

public class test {
	
	public static final String AES_KEY="CRu3Yc58KCPRo4PQpJa1yw==";
	public static final String RSA_PIV_KEY="MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBANMsS9dadOOnGuZ7E+LsCJN38Pg6QQh6NnRYSyEg3bh/zu4kAkRKMJUDKTV0cQtwZw9Tpoovc1jnvWmllLcmdjy3WHJcbH+OR+OIWN1sSAer9yiHMxoNFYfrJBc8bS2OxLcHDN3DXrGzDVv3C0tl+KUJLy9jtcl79N6A6HvKyQkXAgMBAAECgYEAlzLEKrFtuAJR1GyTVIrDqTLbqh+rqLI4gx0kzdeGaS+5rfDCXrrwBgF5Y/i3aAVXBTZTq+VFpYZnrFkOHgS/6nQxphhUFzhBATsRaKRw02ZgKEDpSktQM/n+jrSPc7AN/wA4S8a1OLUxtXJcCPfFkbImPwByZMD5mIWUyR4e1EECQQDrW06JksUPv+ydqUgGLki63i9wYyZ0kUxI17ui66Gl1Y5wCiZpPOwyNYT9dvD1xUZj8aBWGkw9tglc5nYNIcypAkEA5bH4fsGjjpHGzDUNPO9u0FgoHF5eoccJGlSD/3iN5gA1BEILkhRU/rlccOs16I9SYfvYS/Jbrv1VxF/dyi3/vwJAY3RsFbqNx4AEzwLvypzYYw7s4QMlBvE3WTjAs7H61wcNb876OFJkRTtUfX87h7/bZPSAGqJ2QUWhYz6H+DimMQJAOad8WkS8ixKoNaWzVvI6fG1EVCqT0kOlU0iXgU5/P5YZuKHjSZ104CMiwu0nMOKYqSOP3TBONxQ7rPjrMM5u/QI/YHbUmgvTpKWNyiPIDr0dCLbScV2T9M5GMogNA2I3SMpxyjObSLCvpTwEM9pO+gSKnltYQHPxhb2smCXoMS9j";
	public static final String RSA_PUB_KEY="MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDJvXIrpaJ7GnVcGW1HxLV+nLr6n5ncPk2JgMho8gilFISY4Xa7GKcOWLoJDUUaySHel09O2/gmq92FkpK6AdPct+nMkMRWRqCCU/rXgT9Mmhe8V8X29YoKsDlF+xLHNxxJ58eZDdqNSxmHZ03NIdeHf94TFZQ+O8gvOt7NpX1R9wIDAQAB";
	public static final String urlPath="https://gateway.xywallet.com/xywallet-api";
									
	public static void main(String[] args) {
		//queryResult();
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Map<String, String> sParaTemp = new HashMap<String, String>();
			sParaTemp.put("service", "agentpay.api.singlepay");
			sParaTemp.put("signMethod", "RSA");
			sParaTemp.put("timestamp", sdf.format(new Date()));
			sParaTemp.put("charset", "UTF-8");
			sParaTemp.put("v", "1.1");
			sParaTemp.put("format", "json");
			sParaTemp.put("canary", "hjgftgyhujklmknhgyuhijkadf");
			//业务参数
			sParaTemp.put("appId", "88888");
			sParaTemp.put("merBatchNo", "256678553");
			sParaTemp.put("notifyUrl", "http://www.baidu.com");
			
			//收款人明细参数
			Map<String, String> payeeDetailsParams = new HashMap<String, String>();
			payeeDetailsParams.put("serialNumber", "1");
			payeeDetailsParams.put("payeeName", "张志国");//收款人名称
			payeeDetailsParams.put("payeeAcct", "6217000010074078455");//收款人账号
			payeeDetailsParams.put("bankName", "中国建设银行");//开户行名称
			payeeDetailsParams.put("bankCode", "CCB");//开户行编号
			payeeDetailsParams.put("applyAmount", "0.01");//金额
			payeeDetailsParams.put("currency", "CNY");
			
			
			JSONArray payeeDetailsJson = JSONArray.fromObject(payeeDetailsParams);
			String aesResult = AESUtil.encrypt(payeeDetailsJson.toString(), AES_KEY);
			sParaTemp.put("payeeDetails", aesResult);
			if ("RSA".equalsIgnoreCase(sParaTemp.get("signMethod"))) {
				sParaTemp.put("sign", GatewayUtil.signRequest(sParaTemp, RSA_PIV_KEY, "RSA"));
			}
			System.out.println("代付发出参数:"+sParaTemp);
			String resultString = GatewayUtil.httpPostByDefaultTime(new URI(urlPath), sParaTemp, "UTF-8");
			System.out.println("代付响应的参数:"+resultString);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  catch (Exception e) {
		     e.printStackTrace();
		 }
	}
	public static void queryResult() {
    	
    	try {
    		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Map<String, String> sParaTemp = new HashMap<String, String>();
			sParaTemp.put("service", "agentpay.api.querysinglepay");
			sParaTemp.put("signMethod", "RSA");
			sParaTemp.put("timestamp", sdf.format(new Date()));
			sParaTemp.put("charset", "UTF-8");
			sParaTemp.put("v", "1.1");
			sParaTemp.put("format", "json");
			sParaTemp.put("canary", String.valueOf(System.currentTimeMillis()));
			//业务参数
			sParaTemp.put("appId", "88888");
			sParaTemp.put("merBatchNo", "256678553");
			if ("RSA".equalsIgnoreCase(sParaTemp.get("signMethod"))) {
				sParaTemp.put("sign", GatewayUtil.signRequest(sParaTemp,RSA_PIV_KEY, "RSA"));
			}
			System.out.println("代付查询请求参数:"+sParaTemp);
			String resultString = GatewayUtil.httpPostByDefaultTime(new URI(urlPath), sParaTemp, "UTF-8");
			System.out.println("代付查询接口响应:"+resultString);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  catch (Exception e) {
		     e.printStackTrace();
		 }
	}
	public String agentPayNotify(HttpServletRequest request) {
		String result =RequestUtil.getRequestBody(request);
		System.out.println("=================agentpay接收到代付通知====================");
		System.out.println("#agentPayNotify,pay(agentPayNotifyParas,Model,HttpServletRequest) - start");
		System.out.println("#agentPayNotify,接收到代付通知信息为:" + result);
		return "success";
		
	}
}
