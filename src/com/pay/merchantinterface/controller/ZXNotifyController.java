package com.pay.merchantinterface.controller;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import util.PayUtil;

import com.PayConstant;
import com.pay.coopbank.dao.PayChannelRotation;
import com.pay.coopbank.dao.PayChannelRotationDAO;
import com.pay.merchantinterface.service.NotifyInterface;
import com.pay.order.dao.PayOrder;
import com.third.zhongxin.util.SignUtils;

@Controller
public class ZXNotifyController {
	private static final Log log = LogFactory.getLog(ZXNotifyController.class);
    
    @RequestMapping("ZXNotify")
    public String ZXNotify(HttpServletRequest request,HttpServletResponse response) {
    	OutputStream os = null;
    	InputStream is = null;
        try {
    		request.setCharacterEncoding("UTF-8");
    		is=request.getInputStream();
    		os = response.getOutputStream();
    		String str_temp="";
    		byte[] b = new byte[1024];
    		int len= -1;
    		while((len = is.read(b))!=-1){
    			str_temp+=new String(b,0,len,"UTF-8");
    		}
    		log.info("中信通知数据:"+str_temp);
    		 DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();   
		     DocumentBuilder builder = factory.newDocumentBuilder();   
		     Document document = builder.parse(new InputSource(new StringReader(str_temp)));   
		     Element root = document.getDocumentElement();   
		     NodeList list = root.getChildNodes();
		     Map<String,String> resultMap=new HashMap<String, String>();
		     for(int i=0;i<list.getLength();i++){
		           Node node = list.item(i);
		           if(!"#text".equals(node.getNodeName())){
		        	   resultMap.put(node.getNodeName(), node.getTextContent());
		           }
		     }
		     PayChannelRotation rotation = new PayChannelRotationDAO().getPayChannelRotationOrderNo(resultMap.get("out_trade_no"));
    		 //验签名。
			if(SignUtils.checkParam(resultMap,rotation!=null?rotation.md5Key:PayConstant.PAY_CONFIG.get("PAY_ZX_WX_KEY"))){
				 if("0".equals(resultMap.get("status")) && "0".equals(resultMap.get("result_code"))){
					 if(resultMap.get("pay_result").equals("0")){
						 PayOrder tmpPayOrder = new PayOrder();
			            	tmpPayOrder.payordno = resultMap.get("out_trade_no");
			            	tmpPayOrder.actdat = new Date();
			            	tmpPayOrder.ordstatus="01";
			            	new NotifyInterface().notifyMer(tmpPayOrder);
					 }
				 }
				 os.write("success".getBytes());
			 } else throw new Exception("验签失败");
    		
        } catch (Exception e) {
        	log.info(PayUtil.exceptionToString(e));
        } finally {
        	if(os!=null)try {os.close();} catch (Exception e) {}
        	if(is != null) try { is.close(); } catch (Exception e) {}
        }
        return null;
    }
    @RequestMapping("ZXBJNotify")
    public String ZXBJNotify(HttpServletRequest request,HttpServletResponse response) {
    	OutputStream os = null;
    	InputStream is = null;
        try {
    		request.setCharacterEncoding("UTF-8");
    		is=request.getInputStream();
    		os = response.getOutputStream();
    		String str_temp="";
    		byte[] b = new byte[1024];
    		int len= -1;
    		while((len = is.read(b))!=-1){
    			str_temp+=new String(b,0,len,"UTF-8");
    		}
    		log.info("中信BJ通知数据:"+str_temp);
    		 DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();   
		     DocumentBuilder builder = factory.newDocumentBuilder();   
		     Document document = builder.parse(new InputSource(new StringReader(str_temp)));   
		     Element root = document.getDocumentElement();   
		     NodeList list = root.getChildNodes();
		     Map<String,String> resultMap=new HashMap<String, String>();
		     for(int i=0;i<list.getLength();i++){
		           Node node = list.item(i);
		           if(!"#text".equals(node.getNodeName())){
		        	   resultMap.put(node.getNodeName(), node.getTextContent());
		           }
		     }
		     PayChannelRotation rotation = new PayChannelRotationDAO().getPayChannelRotationOrderNo(resultMap.get("out_trade_no"));
    		 //验签名。
			if(SignUtils.checkParam(resultMap,rotation!=null?rotation.md5Key:PayConstant.PAY_CONFIG.get("PAY_ZXBJ_WX_KEY"))){
				 if("0".equals(resultMap.get("status")) && "0".equals(resultMap.get("result_code"))){
					 if(resultMap.get("pay_result").equals("0")){
						 PayOrder tmpPayOrder = new PayOrder();
			            	tmpPayOrder.payordno = resultMap.get("out_trade_no");
			            	tmpPayOrder.actdat = new Date();
			            	tmpPayOrder.ordstatus="01";
			            	new NotifyInterface().notifyMer(tmpPayOrder);
					 }
				 }
				 os.write("success".getBytes());
			 } else throw new Exception("验签失败");
    		
        } catch (Exception e) {
        	log.info(PayUtil.exceptionToString(e));
        } finally {
        	if(os!=null)try {os.close();} catch (Exception e) {}
        	if(is != null) try { is.close(); } catch (Exception e) {}
        }
        return null;
    }
}