package com.pay.merchantinterface.controller;
import java.io.PrintWriter;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.PayConstant;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.pay.merchantinterface.service.NotifyInterface;
import com.pay.order.dao.PayOrder;
import com.third.sxf.PaymentUtils;

@Controller
public class SXFNEWNotifyController {
	private static final Log log = LogFactory.getLog(SXFNEWNotifyController.class);
	
/*	public static void main(String[] args) throws Exception {
    	String mercPrivateKey="MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCqIpTCmidCCtopwUuZ1pR51bs21uUkWJcUkpUWjjF6CgLjyCeThI0lknhmTgQ9NTSH1ffba3XGlsi8eauavlmpaS7kEtOEfKTJPn4G0EfnR9caqJ+t552T42X7EssVnIzXmx7U/lD1VJvdbHaNzp6cxeEKCsE63c10Kf2BvuYIG/hCF8Hk+zWcY7MFPKgcF1xNOie24CQQzutWVuc192IBRMZEtQNA6C+9VRl+4XuPms4kWue7XcEmfCN+8rvwWTd0Vck0QzZ5huWqW4+s50m9K7gSt4JxU1gTs+j1CVWM/vcLi0ZuEx8//l2hcc0keaO/iOjTIeXDTYbDUJn/EukZAgMBAAECggEAVFuHJSJoGr/kwlVcVe/meK54AE33GdlvN70ifSeN8rPqEOUacnODxMBmQFuokGzEGiIGnl4e7JOw/nrZkoWVUYGcxWtsQ0HGNx5sQJafQFcHmelIG2M/Z6eV1jLQTWvtDa6VUAi1PZ9xFQ4FjnSHgrY+ycrhWn3FtDZpwx5TUms0zsj6vnEUzUN9K596ElW+6NBG3Q42Roo7lp+FDjV9uepLIDmXz6WOmdN3NGfjv10Qqj+5p1qIbxfEYYksyY9qP5cJ+WubsENgBi+GAPa2+ZC1ftC3jugNgvdwISsfIUfM2vI0k9kp9MC8XHLxHVFVGWUlUnzK80Tvco6joFO7nQKBgQDX0/PNI5dpvLohu1z43PezwO2ut7Q5qa2isDR7Nn7bg2/obS0Waatphr6i5DiCiZNru7xXmmoiONH4Jib4MQiDG3XJZXc0LQwU8xrAsR4RggE/bhhzrMSTWoNVUcETjZ5iT3RXtx8VfYP9aOvVNa3ATugRlimAp6B/luGj/O24ewKBgQDJzWWLlj4fCFW7ne2pq5tl+TBXpWA/Ws//BWNdJlhX+9OM8/kmQTYPZelDIyXjItc1mm4EyZr0Pl/oApLBgxPS4f3eYpgRTLs5g6WlrLUffddPJxThUF27TQ4LzFlX1yjxzRcR9gUQdvHk5yPJZwlx5QCN8WDxNl4vlaMq/TXyewKBgFNXck6lzsMy8IE0E6u50CDmgCG2qG6X2gf3mNx3Z7cmYyFSf1WnSly+foDyNewaagXxAcKmSxOZ82KqgOPYM1xFT0+M0eIg/whb89myy1Shag/vWJvafDIWgp6Aqlj9l8qIuflhKiynQj+ps1+qkcSv86dO4AnhMPXz8n/bnIRfAoGATpTc5e4rYoosWFK3KUrZyGuwl3Ceeg0y0WuXtb4oyJoXVtvqOStLkdNtcedN6ZS3mPf5v4OCA3oGfqIB47jXD1dRNDfQX2wxu8O3zTvji8/E992QE+znkvf+cDmy0LHID+B9C1Fxx2eJ6kjXdqXY17EE9kD4LvWs13q9fw/64eECgYEAgfe02rx4r2uDHGRNePZPq/d135AO0EkviNDR4g1SfV7BZdlvPS57Chs3cvgpdcmY4vyW2I2k69LeEbSar9b5zugRpqFBkgNiIJlcHffiTT7ytXnXI4xq27NPTo1bcXD+pAZhN8SvUyya9lm3LZ9ZTUvOdc9uJJpb/lNF+IQMszA=";
		String sxfPublic="MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA8i9r3leYHPwPZIhdLIpq9wyps7Rm4b1NRdNu17cO85By+CNyTV1q7TIV9uXYowDzmrV1aULRZR1GBx5sEFvYG0RIp7DVrTaf3dRFZIpjMMfva9unlJLQV6J38jvqXB9sxwSd2romadQv8RXJ/IJCQrUBjobLh/O5JXTs97XLXIk3jS9Yuiw3LX4RkXHjt3ya8oZXXEM7yb5OYNp8Qbh2ncYhQZhsam+er8mtO2eugRe4LER2Gyi9NIDXFWyG9p5+B2pXAVFXqzBnCFM2XwI20EIXXDrvktNPcB1RbhcP0vqe1n0gCrxIqFePsHHSIoNxumNcbK6l4nI9Qar4Y285DQIDAQAB";
    	// 获取参数
    	String 
    	_t = "{\"mercNo\":\"600000000001044\",\"orderNo\":\"J7GZ5GQW3JLXPT2Q1\",\"tranCd\":\"1001\",\"resCode\":\"000000\",\"resMsg\":\"SUCCESS\",\"resData\":\"f5Qtj5uD+CfZULgUoURqvx/47Jg1PlKPtZp8HJ1VBQYR9HygZ2N4JNKnTdYhpwP+M7H1eihsXWSqG2QvM+2PF7XUVBMnToFazR/bag1n7jpGL2kCr0EhQvYwTncVN9Si6DdstYaLHhJGpkqbzL3yMXEBDN/IefJlXKbBQu9vFwslziKcv5NYgmvFyJIjqiSzkdpZJPXf+hdRQfAhaDlGWIj01sdF2fqZeFQ48WJSciKXzgRXhRmSil2bH0hROUU7DCWc3PmZwz+ZGWy7s7ongYbtVZg0s5+pVIPPoU3ReSRGWsSViV11ayXMNGSXB8qXMSE/2FY5f7W+8Q0VLHIIb5lChgrMq9zgF+/PP9kfX1isw3kpOwhHN3egxBsGOUhvajaNR2oVnZG1LwVxUkRtxOnIQRDzj8OaYj7UCBaxP4yJ4cpES4wFTKioegMsEwZgDwt9vUfSFyPwPQfBX0XpsatQR1dzmwzHln9LP/VhD+tCkVJs3irzqGAQ3bCBwBtSc7lcnktfMYC9jS83iP8ajnEWUurnRUlHJfJ02kwuvCf2ajI73fX5HgMxxRYKBb4wFsQSabYjvJZuieeT+Ouy1IYFLNtdGRsKhNdNv+OfOdy1jp5LlQBdtLVsdQPgxGeuYUQE57NrgiMHEVDI+Gd6q+gwq62ktAKneIXTwJahm40=\",\"sign\":\"HURwPbmomXO+6DaSaInz5tAdG5cHbZVTwpdzH+NtaGipZ7Q+RLZaG+vIOmtcV6sjFu2VfayRS4FS9kLEcUg5/t1AopmJJsvmU0lJ3jWDrG5zOV3aVdKTpyDKcn2pUF7Dv5FiG4VEJFM87bC+iEuMMsAKvR9zQjZqHlbuhNVhQ54+dOf9J8hhEZmKKQn1Zdx46GyS/1cjyP5cf4QvHsAG6dEsy8MhZgY9y0CWKJd0kF8d6vM89583Eky1jedhtJ76cgu0tBPMNojEv3c0RERe/HAn/z+3ut9h8mcQEA1hhXmrhKGusM1PMYSKatmAf5uKN5REKSiD8dyO/+YwbxlzMA==\",\"encodeType\":\"RSA#RSA\"}";
    	log.info(_t);
    	net.sf.json.JSONObject resJson = net.sf.json.JSONObject.fromObject(_t);
    	String mercNo =resJson.getString("mercNo");
        String orderNo =resJson.getString("orderNo");
    	String tranCd = resJson.getString("tranCd");
    	String resCode = resJson.getString("resCode");
    	String resMsg = resJson.getString("resMsg");
    	if(!"000000".equals(resCode))new Exception("新随行付通知失败");
    	String resData = resJson.getString("resData");
    	String sign = resJson.getString("sign");
    	net.sf.json.JSONObject main = new net.sf.json.JSONObject();
    	//注意签名顺序
    	main.put("mercNo", mercNo);
    	main.put("orderNo", orderNo);
    	main.put("tranCd", tranCd);
    	main.put("resCode", resCode);
    	main.put("resMsg", resMsg);
    	main.put("resData", resData);
    	//验签
    	boolean check=PaymentUtils.doCheck(main.toString(), sign, sxfPublic);
    	System.out.println(check);
    	String resData_temp=PaymentUtils.decrypt(resData, mercPrivateKey);
    	JSONObject jsonObject = JSON.parseObject(resData_temp);
    	System.out.println(jsonObject.getString("tranSts"));
	}*/
    
    @RequestMapping("SXFNEWNotify")
    public static String SXFNEWNotify(HttpServletRequest request,HttpServletResponse response) {
    	log.info("新随行付：通知开始==========================");
    	PrintWriter writer = null;
        try {
        	writer= response.getWriter();
        	//获取商户密钥
        	String mercPrivateKey=PayConstant.PAY_CONFIG.get("SXFNEW_MERCPRIVATEKEY");
			String sxfPublic=PayConstant.PAY_CONFIG.get("SXFNEW_SXFPUBLIC");
        	// 获取参数
        	String _t=request.getParameter("_t");
        	log.info(_t);
        	net.sf.json.JSONObject resJson = net.sf.json.JSONObject.fromObject(_t);
        	String mercNo =resJson.getString("mercNo");
            String orderNo =resJson.getString("orderNo");
        	String tranCd = resJson.getString("tranCd");
        	String resCode = resJson.getString("resCode");
        	String resMsg = resJson.getString("resMsg");
        	if(!"000000".equals(resCode))new Exception("新随行付通知失败");
        	String resData = resJson.getString("resData");
        	String sign = resJson.getString("sign");
        	net.sf.json.JSONObject main = new net.sf.json.JSONObject();
        	//注意签名顺序
        	main.put("mercNo", mercNo);
        	main.put("orderNo", orderNo);
        	main.put("tranCd", tranCd);
        	main.put("resCode", resCode);
        	main.put("resMsg", resMsg);
        	main.put("resData", resData);
        	//验签
        	boolean check=PaymentUtils.doCheck(main.toString(), sign, sxfPublic);
        	if(check){
            	String resData_temp=PaymentUtils.decrypt(resData, mercPrivateKey);
            	JSONObject jsonObject = JSON.parseObject(resData_temp);
            	if("S".equals(jsonObject.getString("tranSts"))){
            		PayOrder tmpPayOrder = new PayOrder();
                	tmpPayOrder.payordno = orderNo;
                	tmpPayOrder.actdat = new Date();
                	tmpPayOrder.ordstatus="01";
                	new NotifyInterface().notifyMer(tmpPayOrder);
				}else if("F".equals(jsonObject.getString("tranSts"))){
					PayOrder tmpPayOrder = new PayOrder();
                	tmpPayOrder.payordno = orderNo;
                	tmpPayOrder.actdat = new Date();
                	tmpPayOrder.ordstatus="02";
                	new NotifyInterface().notifyMer(tmpPayOrder);
				}
            	writer.write("SUCCESS");
            	writer.flush();
        	}else new Exception("新随行付：验签失败");
        } catch (Exception e) {
        	e.printStackTrace();
        } finally {
        	if(writer!=null)try {writer.close();} catch (Exception e) {}
        }
        return null;
    }
}