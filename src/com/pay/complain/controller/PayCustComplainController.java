package com.pay.complain.controller;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import sun.beans.editors.DoubleEditor;
import sun.beans.editors.FloatEditor;
import sun.beans.editors.IntegerEditor;
import sun.beans.editors.LongEditor;
import util.JWebConstant;
import util.Tools;

import com.jweb.dao.Blog;
import com.jweb.dao.JWebUser;
import com.jweb.service.JWebUserService;
import com.pay.complain.dao.PayCustComplain;
import com.pay.complain.service.PayCustComplainService;
@Controller
public class PayCustComplainController {
    @RequestMapping("payCustComplain")
    public String getPayCustComplainList(HttpServletRequest request,HttpServletResponse response,
            @ModelAttribute("payCustComplain") PayCustComplain payCustComplain) {
        JWebUser user = (JWebUser)request.getSession().getAttribute("user");
        //登录和权限判断
        if(!JWebUserService.checkUser(user,request))return "/jsp/jweb/jump_login.jsp";
        //处理标识，空：跳转、0：列表
        String flag = request.getParameter("flag");
        new Blog(this.getClass().getSimpleName()+".getPayCustComplainList","取得PayCustComplain列表",user.id,request);
        OutputStream os = null;
        try {
            if(flag == null)return "/jsp/pay/complain/pay_cust_complain.jsp";
            request.setCharacterEncoding("UTF-8");
            response.setContentType("text/html;charset=UTF-8");
            os = response.getOutputStream();
            int page = 1;
            int rows = JWebConstant.WEB_RECORD_COUNT_PER_PAGE;
            try {
                rows = Integer.parseInt((String)JWebConstant.SYS_CONFIG.get("WEB_RECORD_COUNT_PER_PAGE"));//系统配置的每页记录条数
            } catch (Exception e) {}
            try {
                page = Integer.parseInt(request.getParameter("page"));
            } catch (Exception e1) {}
            try {
                rows = Integer.parseInt(request.getParameter("rows"));//用户设置的每页记录条数
            } catch (Exception e1) {}
            os.write(new PayCustComplainService().getPayCustComplainList(payCustComplain,page,rows,
                request.getParameter("sort"),request.getParameter("order")).getBytes("utf-8"));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(os != null)try {os.close();} catch (IOException e1) {}
        }
        return null;
    }
    @RequestMapping("addPayCustComplain")
    public String addPayCustComplain(HttpServletRequest request,HttpServletResponse response,
            @ModelAttribute("payCustComplain") PayCustComplain payCustComplain) {
        JWebUser user = (JWebUser)request.getSession().getAttribute("user");
        OutputStream os = null;
        Blog log = null;
        try {
            os = response.getOutputStream();
            request.setCharacterEncoding("UTF-8");
            response.setContentType("text/html;charset=UTF-8");
            //登录和权限判断
            if(!JWebUserService.checkUser(user,request)){
               os.write(((String)request.getSession().getAttribute("LOGIN_INFO")).getBytes());
               return null;
            }
            log = new Blog(this.getClass().getSimpleName()+".addPayCustComplain","添加PayCustComplain",user.id,request);
            payCustComplain.id = (String) request.getSession().getAttribute("custPlainId");
            new PayCustComplainService().addPayCustComplain(payCustComplain,user);
            request.getSession().removeAttribute("custPlainId");
            os.write(JWebConstant.OK.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
            try {os.write(e.getMessage().getBytes("utf-8"));} catch (Exception e1) {}
            if(log!=null)log.info("添加失败,"+e.getMessage());
        } finally {
            if(os != null)try {os.close();} catch (IOException e1) {}
        }
        return null;
    }
    @RequestMapping("updatePayCustComplain")
    public String updatePayCustComplain(HttpServletRequest request,HttpServletResponse response,
            @ModelAttribute("payCustComplain") PayCustComplain payCustComplain) {
        JWebUser user = (JWebUser)request.getSession().getAttribute("user");
        OutputStream os = null;
        Blog log = null;
        try {
            //处理标识,show跳转,其他更新
            if("show".equals(request.getParameter("flag"))){
                log = new Blog(this.getClass().getSimpleName()+".updatePayCustComplain","取得PayCustComplain",user.id,request);
                request.setAttribute("payCustComplainUpdate",
                    new PayCustComplainService().detailPayCustComplain(request.getParameter("id")));
                return "/jsp/pay/complain/pay_cust_complain_update.jsp";
            }
            os = response.getOutputStream();
            request.setCharacterEncoding("UTF-8");
            response.setContentType("text/html;charset=UTF-8");
            //登录和权限判断
            if(!JWebUserService.checkUser(user,request)){
               os.write(((String)request.getSession().getAttribute("LOGIN_INFO")).getBytes());
               return null;
            }
            log = new Blog(this.getClass().getSimpleName()+".updatePayCustComplain","修改PayCustComplain",user.id,request);
            new PayCustComplainService().updatePayCustComplain(payCustComplain,request,user);
            os.write(JWebConstant.OK.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
            try {os.write(e.getMessage().getBytes("utf-8"));} catch (Exception e1) {}
            if(log!=null)log.info("修改失败,"+e.getMessage());
        } finally {
            if(os != null)try {os.close();} catch (IOException e1) {}
        }
        return null;
    }
    @RequestMapping("startFileUploadForCustComplain")
    public String startFileUploadForCustComplain(@RequestParam("uploadStartFile") MultipartFile uploadFile,
    		HttpServletRequest request,HttpServletResponse response) throws Exception{
    	JWebUser user = (JWebUser)request.getSession().getAttribute("user");
        //登录和权限判断
        if(!JWebUserService.checkUser(user,request))return null;
        OutputStream os = null;
        //如果没有投诉id则生成
        String custPlainId = (String) request.getSession().getAttribute("custPlainId");
        if("".equals(custPlainId) || custPlainId == null) {
        	custPlainId = Tools.getUniqueIdentify();
        	request.getSession().setAttribute("custPlainId", custPlainId);
        }
        //文件名：投诉id_文件名
        String fileName = custPlainId + "_"+uploadFile.getOriginalFilename();
        JSONObject jsonObject = new JSONObject();
		try {
        	String realPath = request.getSession().getServletContext().getRealPath("/upload");
        	File file = new File(realPath, fileName);
        	//如果存在该文件，删除之前的
        	if(file.exists()) file.delete();
            if(null != uploadFile){
            	uploadFile.transferTo(file);
            } 
			os = response.getOutputStream();
			request.setCharacterEncoding("UTF-8");
			response.setContentType("application/json;charset=UTF-8");
			jsonObject.put("saveUrl", "/upload/" + fileName);
			jsonObject.put("custPlainId", custPlainId);
			os.write(jsonObject.toString().getBytes("utf-8"));
		} catch (Exception e) {
			e.printStackTrace();
			jsonObject.put("errorMessage", e.getMessage());
            try {os.write(jsonObject.toString().getBytes("utf-8"));} catch (Exception e1) {}
		} finally {
			if(os != null)try {os.close();} catch (IOException e1) {}
		}
        return null;  
    }
    @RequestMapping("endFileUploadForCustComplain")
    public String endFileUploadForCustComplain(@RequestParam("uploadEndFile") MultipartFile uploadFile,
    		HttpServletRequest request,HttpServletResponse response) throws Exception{
    	JWebUser user = (JWebUser)request.getSession().getAttribute("user");
        //登录和权限判断
        if(!JWebUserService.checkUser(user,request))return null;
        OutputStream os = null;
        //如果没有投诉id则生成
        String custPlainId = (String) request.getSession().getAttribute("custPlainId");
        if("".equals(custPlainId) || custPlainId == null) {
        	custPlainId = Tools.getUniqueIdentify();
        	request.getSession().setAttribute("custPlainId", custPlainId);
        }
        //文件名：投诉id_文件名
        String fileName = custPlainId + "_"+uploadFile.getOriginalFilename();
        JSONObject jsonObject = new JSONObject();
		try {
        	String realPath = request.getSession().getServletContext().getRealPath("/upload");
        	File file = new File(realPath, fileName);
        	//如果存在该文件，删除之前的
        	if(file.exists()) file.delete();
            if(null != uploadFile){
            	uploadFile.transferTo(file);
            } 
			os = response.getOutputStream();
			request.setCharacterEncoding("UTF-8");
			response.setContentType("application/json;charset=UTF-8");
			jsonObject.put("saveUrl", "/upload/" + fileName);
			jsonObject.put("custPlainId", custPlainId);
			os.write(jsonObject.toString().getBytes("utf-8"));
		} catch (Exception e) {
			e.printStackTrace();
			jsonObject.put("errorMessage", e.getMessage());
            try {os.write(jsonObject.toString().getBytes("utf-8"));} catch (Exception e1) {}
		} finally {
			if(os != null)try {os.close();} catch (IOException e1) {}
		}
        return null;  
    }
    @RequestMapping("updateUploadFile")
    public String updateUploadFile(@RequestParam("updateUploadFile") MultipartFile uploadFile,
    		HttpServletRequest request,HttpServletResponse response) throws Exception{
    	JWebUser user = (JWebUser)request.getSession().getAttribute("user");
    	//登录和权限判断
    	if(!JWebUserService.checkUser(user,request))return null;
    	OutputStream os = null;
    	//文件名：投诉id_文件名
    	String custPlainId = request.getParameter("id");
    	String fileName = custPlainId + "_"+uploadFile.getOriginalFilename();
    	JSONObject jsonObject = new JSONObject();
    	try {
    		String realPath = request.getSession().getServletContext().getRealPath("/upload");
    		File file = new File(realPath, fileName);
    		//如果存在该文件，删除之前的
    		if(file.exists()) file.delete();
    		if(null != uploadFile){
    			uploadFile.transferTo(file);
    		} 
    		os = response.getOutputStream();
    		request.setCharacterEncoding("UTF-8");
    		response.setContentType("application/json;charset=UTF-8");
    		jsonObject.put("saveUrl", "/upload/" + fileName);
    		os.write(jsonObject.toString().getBytes("utf-8"));
    	} catch (Exception e) {
    		e.printStackTrace();
    		jsonObject.put("errorMessage", e.getMessage());
    		try {os.write(jsonObject.toString().getBytes("utf-8"));} catch (Exception e1) {}
    	} finally {
    		if(os != null)try {os.close();} catch (IOException e1) {}
    	}
    	return null;  
    }
    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(int.class, new IntegerEditor());
        binder.registerCustomEditor(long.class, new LongEditor());
        binder.registerCustomEditor(float.class, new FloatEditor());
        binder.registerCustomEditor(double.class, new DoubleEditor());
        binder.registerCustomEditor(Date.class, new CustomDateEditor(null, true) {
            @Override  
            public void setAsText(String text) throws IllegalArgumentException {
                if(text != null && text.length()>0)
                try {
                    setValue(new SimpleDateFormat("yyyy-MM-dd").parse(text));
                } catch (Exception e) {
                    try {setValue(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(text));} catch (Exception e1) {}
                }
            }
        });
    }
}