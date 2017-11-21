package com.pay.merchantinterface.controller;
import java.io.OutputStream;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import util.PayUtil;
import util.SHA1;

import com.PayConstant;
import com.pay.merchantinterface.service.FYVerificationIDService;
@Controller
public class FYVerificationIDController {
	private static final Log log = LogFactory.getLog(FYVerificationIDController.class);
	/**
     * 身份证验证
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("verificationID")
    public String verificationID(HttpServletRequest request,HttpServletResponse response) {
    	OutputStream os = null;
        try {
    		request.setCharacterEncoding("UTF-8");
    		response.setCharacterEncoding("UTF-8");
    		response.setHeader("Content-type","text/html;charset=UTF-8");
    		os = response.getOutputStream();
    		//接收到数据
    		String userName = request.getParameter("userName");
    		String userId = request.getParameter("userId");
    		String sign = request.getParameter("sign");
            //验签
    		log.info(SHA1.SHA1String(userName+userId+PayConstant.PAY_CONFIG.get("SYS_COMM_PWD_WITH_INNER_PLT")));
            if(sign!=null && sign.equals(SHA1.SHA1String(userName+userId+PayConstant.PAY_CONFIG.get("SYS_COMM_PWD_WITH_INNER_PLT")))){
            	new FYVerificationIDService();
				Map<String,String> resultMap = FYVerificationIDService.VerificationID(userName,userId);
            	if(resultMap==null){
            		os.write("{\"resCode\":\"002\",\"resCode\":\"002\",\"resMsg\":\"验证失败\",\"imgData\":\"\"}".getBytes("utf-8"));
            	}else{
            		if(resultMap.get("responsecode").equals("0000")){
            			os.write(("{\"resCode\":\"000\",\"resCode\":\"000\",\"resMsg\":\"验证成功\",\"imgData\":\""+resultMap.get("userid")+"\"}").getBytes("utf-8"));
            		}else{
            			os.write(("{\"resCode\":\"002\",\"resCode\":\"002\",\"resMsg\":\""+resultMap.get("responsemsg")+"\",\"imgData\":\"\"}").getBytes("utf-8"));
            		}
            	}
            } else os.write("{\"resCode\":\"001\",\"resCode\":\"001\",\"resMsg\":\"验签失败\",\"imgData\":\"\"}".getBytes("utf-8"));
        } catch (Exception e) {
        	log.info(PayUtil.exceptionToString(e));
        	if(os!=null)try {
        		os.write(("{\"resCode\":\"-1\",\"resMsg\":\""+e.getMessage()+"\"}").getBytes("utf-8"));
        	} catch (Exception e1) {}
        } finally {
        	if(os!=null)try {os.close();} catch (Exception e) {}
        }
        return null;
    }
}
