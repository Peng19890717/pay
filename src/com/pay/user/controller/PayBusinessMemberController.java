package com.pay.user.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;

import sun.beans.editors.DoubleEditor;
import sun.beans.editors.FloatEditor;
import sun.beans.editors.IntegerEditor;
import sun.beans.editors.LongEditor;
import util.JWebConstant;

import com.alibaba.fastjson.JSONObject;
import com.jweb.dao.Blog;
import com.jweb.dao.JWebUser;
import com.jweb.service.JWebUserService;
import com.pay.bank.service.PayBankService;
import com.pay.user.dao.PayBusinessMember;
import com.pay.user.service.PayBusinessMemberService;
@Controller
public class PayBusinessMemberController {
	/**
	 * 跳转到业务员管理页面
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("bussinessManage")
    public String bussinessManage(HttpServletRequest request, HttpServletResponse response) throws Exception {
		JWebUser user = (JWebUser)request.getSession().getAttribute("user");
        //登录和权限判断
        if(!JWebUserService.checkUser(user,request))return "/jsp/jweb/jump_login.jsp";
        Blog log = null;
        try{
        	log = new Blog(this.getClass().getSimpleName()+".bussinessManage","跳转到业务员管理页面",user.id,request);
        	request.setCharacterEncoding("UTF-8");
        	response.setContentType("text/html;charset=UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
            if(log!=null)log.info("跳转失败,"+e.getMessage());
        }
       return "/jsp/pay/user/pay_businessMem_show.jsp";
    }
	/**
	 * 查询所有未关联的业务员
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("findBusiMemLeft")
	public String findBusiMemLeft(HttpServletRequest request, HttpServletResponse response) throws Exception {
		JWebUser user = (JWebUser)request.getSession().getAttribute("user");
		//登录和权限判断
		if(!JWebUserService.checkUser(user,request))return "/jsp/jweb/jump_login.jsp";
		Blog log = null;
		OutputStream os = null;
		try{
			log = new Blog(this.getClass().getSimpleName()+".findBusiMemLeft","查询busiMemLeft列表",user.id,request);
			request.setCharacterEncoding("UTF-8");
			response.setContentType("text/html;charset=UTF-8");
			os = response.getOutputStream();
			//查询没有被关联的业务员
			List<PayBusinessMember> memWithoutRelationList = new PayBusinessMemberService().getBusiMemWithoutRelation();
			JSONArray row = new JSONArray();
			for(int i = 0; i<memWithoutRelationList.size(); i++){
				JSONObject jsonObject = new JSONObject();
				PayBusinessMember member = memWithoutRelationList.get(i);
				jsonObject.put("id", member.userId);
				jsonObject.put("text", member.realName);
				row.put(jsonObject);
			}
			os.write(row.toString().getBytes("utf-8"));
			os.close();
		} catch (Exception e) {
			e.printStackTrace();
			if(log!=null)log.info("查询列表失败,"+e.getMessage());
		}
		return "/jsp/pay/user/pay_businessMem_show.jsp";
	}
	/**
	 * 获取业务员列表的树结构
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("bussinessMemberList")
    public String bussinessMemberList(HttpServletRequest request, HttpServletResponse response) throws Exception {
        JWebUser user = (JWebUser)request.getSession().getAttribute("user");
        //登录和权限判断
        if(!JWebUserService.checkUser(user,request))return "/jsp/jweb/jump_login.jsp";
        Blog log = null;
        OutputStream os = null;
        try {
        	log = new Blog(this.getClass().getSimpleName()+".bussinessMemberList","查询PayBusinessMember列表",user.id,request);
            request.setCharacterEncoding("UTF-8");
            response.setContentType("text/html;charset=UTF-8");
            os = response.getOutputStream();
            String json = new PayBusinessMemberService().getJson();
            //String json="{\"total\":3,\"rows\":[{\"id\":\"1000007\",\"merStatus\":\"0\",\"text\":\"银座商城\",\"checkStatus\":\"1\",\"name\":\"银座商城\",\"iconCls\":\"icon-man\",\"_parentId\":\"1000000\"},{\"id\":\"1000098\",\"merStatus\":\"0\",\"text\":\"京东\",\"checkStatus\":\"1\",\"name\":\"京东\",\"iconCls\":\"icon-man\",\"_parentId\":\"1000000\"},{\"id\":\"1000000\",\"merStatus\":\"0\",\"text\":\"测商\",\"checkStatus\":\"1\",\"name\":\"测商\",\"iconCls\":\"icon-man\"}]}";
            os.write(json.getBytes("utf-8"));
        } catch (Exception e) {
            e.printStackTrace();
            if(log!=null)log.info("查询列表失败,"+e.getMessage());
        } finally {
            if(os != null)try {os.close();} catch (IOException e1) {}
        }
        return null;
    }
	/**
	 * 添加子节点
	 * @param request
	 * @param response
	 * @param payBusinessMember
	 * @return
	 */
	@RequestMapping("addPayBusiMem")
    public String addPayBusiMem(HttpServletRequest request,HttpServletResponse response, PayBusinessMember payBusinessMember) {
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
            log = new Blog(this.getClass().getSimpleName()+".addPayBusiMem","添加PayBusinessMember",user.id,request);
            new PayBankService().addPayBusiMem(payBusinessMember);
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
	@RequestMapping("validPayMentRelationUserId")
	public String validPayMentRelationUserId(HttpServletRequest request,HttpServletResponse response) {
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
			log = new Blog(this.getClass().getSimpleName()+".validPayMentRelationUserId","校验PayBusinessMember",user.id,request);
			new PayBankService().validPayMentRelationUserId(request.getParameter("userId"));
			os.write("1".getBytes());
		} catch (Exception e) {
			e.printStackTrace();
			try {os.write(e.getMessage().getBytes("utf-8"));} catch (Exception e1) {}
			if(log!=null)log.info("添加失败,"+e.getMessage());
		} finally {
			if(os != null)try {os.close();} catch (IOException e1) {}
		}
		return null;
	}
	/**
	 * 删除节点及其子节点
	 * @param request
	 * @param response
	 * @param payBusinessMember
	 * @return
	 */
	@RequestMapping("removeBusiMem")
    public String removeBusiMem(HttpServletRequest request,HttpServletResponse response, PayBusinessMember payBusinessMember) {
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
            log = new Blog(this.getClass().getSimpleName()+".removeBusiMem","删除PayBusinessMember",user.id,request);
            new PayBusinessMemberService().removeBusiMem(payBusinessMember.getUserId());
            os.write(JWebConstant.OK.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
            try {os.write(e.getMessage().getBytes("utf-8"));} catch (Exception e1) {}
            if(log!=null)log.info("删除失败,"+e.getMessage());
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