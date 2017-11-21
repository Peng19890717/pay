package com.pay.risk.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import sun.beans.editors.DoubleEditor;
import sun.beans.editors.FloatEditor;
import sun.beans.editors.IntegerEditor;
import sun.beans.editors.LongEditor;
import util.JWebConstant;

import com.jweb.dao.Blog;
import com.jweb.dao.JWebUser;
import com.jweb.service.JWebUserService;
import com.pay.risk.dao.PayRiskExceptRule;
import com.pay.risk.dao.PayRiskExceptRuleParam;
import com.pay.risk.service.PayRiskExceptRuleParamService;
import com.pay.risk.service.PayRiskExceptRuleService;
@Controller
public class PayRiskExceptRuleController {
    @RequestMapping("payRiskExceptRule")
    public String getPayRiskExceptRuleList(HttpServletRequest request,HttpServletResponse response,
            @ModelAttribute("payRiskExceptRule") PayRiskExceptRule payRiskExceptRule) {
        JWebUser user = (JWebUser)request.getSession().getAttribute("user");
        //登录和权限判断
        if(!JWebUserService.checkUser(user,request))return "/jsp/jweb/jump_login.jsp";
        //处理标识，空：跳转、0：列表
        String flag = request.getParameter("flag");
        OutputStream os = null;
        Blog log = null;
        try {
        	log=new Blog(this.getClass().getSimpleName()+".payRiskExceptRule","取得PayRiskExceptRule列表",user.id,request);
            if(flag == null)return "/jsp/pay/risk/pay_risk_except_rule.jsp";
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
            os.write(new PayRiskExceptRuleService().getPayRiskExceptRuleList(payRiskExceptRule,page,rows,
                request.getParameter("sort"),request.getParameter("order")).getBytes("utf-8"));
        } catch (Exception e) {
            e.printStackTrace();
            if(log!=null)log.info("查询列表失败,"+e.getMessage());
        } finally {
            if(os != null)try {os.close();} catch (IOException e1) {}
        }
        return null;
    }
    @RequestMapping("addPayRiskExceptRule")
    public String addPayRiskExceptRule(HttpServletRequest request,HttpServletResponse response,
            @ModelAttribute("payRiskExceptRule") PayRiskExceptRule payRiskExceptRule) {
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
            log = new Blog(this.getClass().getSimpleName()+".addPayRiskExceptRule","添加PayRiskExceptRule",user.id,request);
            payRiskExceptRule.createName = user.name;
            payRiskExceptRule.updateName = user.name;
            new PayRiskExceptRuleService().addPayRiskExceptRule(payRiskExceptRule,request);
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
    @RequestMapping("detailPayRiskExceptRule")
    public String detailPayRiskExceptRule(HttpServletRequest request,HttpServletResponse response) {
        JWebUser user = (JWebUser)request.getSession().getAttribute("user");
        //登录和权限判断
        if(!JWebUserService.checkUser(user,request))return "/jsp/jweb/jump_login.jsp";
        try {
            PayRiskExceptRule detailPayRiskExceptRule = new PayRiskExceptRuleService().detailPayRiskExceptRule(request.getParameter("ruleCode"));
			request.setAttribute("payRiskExceptRule",detailPayRiskExceptRule);
			// 根据规则id查询对应规则参数
			Set<PayRiskExceptRuleParam> payRiskExceptRuleParamSet = getPayRiskExceptRuleParam(detailPayRiskExceptRule);
			request.setAttribute("payRiskExceptRuleParamSet", payRiskExceptRuleParamSet);
        } catch (Exception e) {
            e.printStackTrace();
        } 
        return "/jsp/pay/risk/pay_risk_except_rule_detail.jsp";
    }
	
    @RequestMapping("removePayRiskExceptRule")
    public String removePayRiskExceptRule(HttpServletRequest request,HttpServletResponse response) {
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
            log = new Blog(this.getClass().getSimpleName()+".removePayRiskExceptRule","删除PayRiskExceptRule",user.id,request);
            new PayRiskExceptRuleService().removePayRiskExceptRule(request.getParameter("ruleCode"));
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
    @RequestMapping("updatePayRiskExceptRule")
    public String updatePayRiskExceptRule(HttpServletRequest request,HttpServletResponse response,
            @ModelAttribute("payRiskExceptRule") PayRiskExceptRule payRiskExceptRule) {
        JWebUser user = (JWebUser)request.getSession().getAttribute("user");
        OutputStream os = null;
        Blog log = null;
        try {
            //处理标识,show跳转,其他更新
            if("show".equals(request.getParameter("flag"))){
                log = new Blog(this.getClass().getSimpleName()+".updatePayRiskExceptRule","取得PayRiskExceptRule",user.id,request);
                request.setAttribute("payRiskExceptRuleUpdate",new PayRiskExceptRuleService().detailPayRiskExceptRule(request.getParameter("ruleCode")));
                // 根据规则id查询对应规则参数
                Set<PayRiskExceptRuleParam> payRiskExceptRuleParamSet = getPayRiskExceptRuleParam(payRiskExceptRule);
    			request.setAttribute("updatePayRiskExceptRuleParamSet", payRiskExceptRuleParamSet);
                return "/jsp/pay/risk/pay_risk_except_rule_update.jsp";
            }
            os = response.getOutputStream();
            request.setCharacterEncoding("UTF-8");
            response.setContentType("text/html;charset=UTF-8");
            //登录和权限判断
            if(!JWebUserService.checkUser(user,request)){
               os.write(((String)request.getSession().getAttribute("LOGIN_INFO")).getBytes());
               return null;
            }
            log = new Blog(this.getClass().getSimpleName()+".updatePayRiskExceptRule","修改PayRiskExceptRule",user.id,request);
            new PayRiskExceptRuleService().updatePayRiskExceptRule(payRiskExceptRule,request);
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
    
    @RequestMapping("abledPayRiskExceptRuleStatus")
    public String abledPayRiskExceptRuleStatus(HttpServletRequest request,HttpServletResponse response) {
    	return updateStatus(request,response,"启用");
    }
    
    @RequestMapping("disabledPayRiskExceptRuleStatus")
    public String disabledPayRiskExceptRuleStatus(HttpServletRequest request,HttpServletResponse response) {
    	return updateStatus(request,response,"禁用");
    }
    
    /**
     * 修改规则的启用禁用状态
     * @param request
     * @param response
     * @param info
     * @return
     */
    private String updateStatus(HttpServletRequest request,HttpServletResponse response,String info) {
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
            log = new Blog(this.getClass().getSimpleName()+".updateStatus",info+"PayRiskExceptRule",user.id,request);
            new PayRiskExceptRuleService().updatePayRiskExceptRuleStatus(request.getParameter("ruleCode"),request.getParameter("columName"),request.getParameter("value"));
            os.write(JWebConstant.OK.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
            try {os.write(e.getMessage().getBytes("utf-8"));} catch (Exception e1) {}
            if(log!=null)log.info(info+"失败,"+e.getMessage());
        } finally {
            if(os != null)try {os.close();} catch (IOException e1) {}
        }
        return null;
	}
    
    /**
     * 通过规则id查询对应规则参数集合
     * @param detailPayRiskExceptRule
     * @return
     * @throws Exception
     */
    private Set<PayRiskExceptRuleParam> getPayRiskExceptRuleParam(PayRiskExceptRule detailPayRiskExceptRule) throws Exception {
		List<PayRiskExceptRuleParam> payRiskExceptRuleParamList = new PayRiskExceptRuleParamService().detailByPayRiskExceptRule(detailPayRiskExceptRule.ruleCode);
		Set<PayRiskExceptRuleParam> PayRiskExceptRuleParamSet = new TreeSet<PayRiskExceptRuleParam>();
		if(payRiskExceptRuleParamList != null){
			for (PayRiskExceptRuleParam payRiskExceptRuleParam : payRiskExceptRuleParamList) {
				PayRiskExceptRuleParamSet.add(payRiskExceptRuleParam);
			}
		}
		return PayRiskExceptRuleParamSet;
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