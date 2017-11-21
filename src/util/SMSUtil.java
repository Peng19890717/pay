package util;

import java.net.URLEncoder;

import com.PayConstant;
import com.jweb.dao.Blog;

public class SMSUtil {
	public String send(String mobile,String msg) throws Exception{
		new Blog().info(mobile+":"+msg);
		String sign = MD5.getDigest(mobile+msg+PayConstant.PAY_CONFIG.get("SMS_GATEWAY_PWD"));
		byte [] b = ("user="+PayConstant.PAY_CONFIG.get("SMS_GATEWAY_USER")+"&num="+mobile+"&msg="
				+URLEncoder.encode(msg,"utf-8")+"&sign="+sign).getBytes();
		return new String(new DataTransUtil().doPost(PayConstant.PAY_CONFIG.get("SMS_GATEWAY_URL"),b),"utf-8");
	}
	public void sendWithThread(String mobile,String msg) throws Exception{
		new SendSmsWithThread(mobile,msg).start();
	}
//	<?xml version="1.0" encoding="utf-8"?>
//	<SubmitResult xmlns="http://120.55.205.5/">
//	<code>2</code>
//	<msg>提交成功</msg>
//	<smsid>438338946</smsid>
//	</SubmitResult>
	public static void main(String [] args) throws Exception{
		String mobile="13120033859",msg="商户掉单，商户号334，支付订单号afdsf。";
		String sign = MD5.getDigest(mobile+msg+"91317d41efd0ff9b46c6667df6f8e230");
		byte [] b = ("user=pay_merchant_user&num="+mobile+"&msg="
				+URLEncoder.encode(msg,"utf-8")+"&sign="+sign).getBytes();
		System.out.println(new String(new DataTransUtil().doPost("http://www.qtongpay.com/smp/send.htm",b),"utf-8"));
	}
}
class SendSmsWithThread extends Thread{
	String mobile;
	String msg;
	public SendSmsWithThread(String mobile,String msg){
		this.mobile = mobile;
		this.msg = msg;
	}
	public void run() {
		try {
			new SMSUtil().send(mobile, msg);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
