package com.pay.coopbank.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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
import com.pay.coopbank.dao.PayChannelBankRelationDAO;
import com.pay.coopbank.dao.PayCoopBank;
import com.pay.coopbank.dao.PayCoopBankRouteRule;
import com.pay.coopbank.service.PayCoopBankService;
@Controller
public class PayCoopBankController {
    @RequestMapping("payCoopBank")
    public String getPayCoopBankList(HttpServletRequest request,HttpServletResponse response,
            @ModelAttribute("payCoopBank") PayCoopBank payCoopBank) {
        JWebUser user = (JWebUser)request.getSession().getAttribute("user");
        //登录和权限判断
        if(!JWebUserService.checkUser(user,request))return "/jsp/jweb/jump_login.jsp";
        //处理标识，空：跳转、0：列表
        String flag = request.getParameter("flag");
        OutputStream os = null;
        Blog log = null;
        try {
        	log = new Blog(this.getClass().getSimpleName()+".payCoopBank","查询PayCoopBank列表",user.id,request);
            if(flag == null)return "/jsp/pay/coopbank/pay_coop_bank.jsp";
            request.setCharacterEncoding("UTF-8");
            response.setContentType("text/html;charset=UTF-8");
            os = response.getOutputStream();
            int page = 1;
            int rows = JWebConstant.WEB_RECORD_COUNT_PER_PAGE;
            try {
                rows = Integer.parseInt((String)JWebConstant.SYS_CONFIG.get("WEB_RECORD_COUNT_PER_PAGE"));//系统配置的每页记录条数
            } catch (Exception e) {
            	e.printStackTrace();
            }
            try {
                page = Integer.parseInt(request.getParameter("page"));
            } catch (Exception e1) {}
            try {
                rows = Integer.parseInt(request.getParameter("rows"));//用户设置的每页记录条数
            } catch (Exception e1) {}
            os.write(new PayCoopBankService().getPayCoopBankList(payCoopBank,page,rows,
                request.getParameter("sort"),request.getParameter("order")).getBytes("utf-8"));
        } catch (Exception e) {
            e.printStackTrace();
            if(log!=null)log.info("查询列表失败,"+e.getMessage());
        } finally {
            if(os != null)try {os.close();} catch (IOException e1) {}
        }
        return null;
    }
    @RequestMapping("addPayCoopBank")
    public String addPayCoopBank(HttpServletRequest request,HttpServletResponse response,
            @ModelAttribute("payCoopBank") PayCoopBank payCoopBank) {
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
            log = new Blog(this.getClass().getSimpleName()+".addPayCoopBank","添加PayCoopBank",user.id,request);
            //建立人员
            payCoopBank.setCreOperId(user.id);
            //最后维护人员
            payCoopBank.setLstUptOperId(user.id);
            new PayCoopBankService().addPayCoopBank(payCoopBank,request);
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
    @RequestMapping("detailPayCoopBank")
    public String detailPayCoopBank(HttpServletRequest request,HttpServletResponse response) {
        JWebUser user = (JWebUser)request.getSession().getAttribute("user");
        Blog log = null;
        //登录和权限判断
        if(!JWebUserService.checkUser(user,request))return "/jsp/jweb/jump_login.jsp";
        try {
        	log = new Blog(this.getClass().getSimpleName()+".detailPayCoopBank","查询PayCoopBank详情",user.id,request);
            PayCoopBank detailPayCoopBank = new PayCoopBankService().detailPayCoopBank(request.getParameter("bankCode"));
			request.setAttribute("payCoopBank",detailPayCoopBank);
			PayChannelBankRelationDAO dao = new PayChannelBankRelationDAO();
            request.setAttribute("bankCodes", dao.selectByBankCode(detailPayCoopBank.bankCode));
            request.setAttribute("cbRelationMap", dao.selectChannelBankRelationByCode(detailPayCoopBank.bankCode));
            request.setAttribute("payChannelMaxAmtB2CJUpdate",new PayCoopBankService().detailPayChannelMaxAmt(request.getParameter("bankCode"),"7"));
            request.setAttribute("payChannelMaxAmtB2CXUpdate",new PayCoopBankService().detailPayChannelMaxAmt(request.getParameter("bankCode"),"8"));
            request.setAttribute("payChannelMaxAmtB2BUpdate",new PayCoopBankService().detailPayChannelMaxAmt(request.getParameter("bankCode"),"9"));
            request.setAttribute("payChannelMaxAmtKJJUpdate",new PayCoopBankService().detailPayChannelMaxAmt(request.getParameter("bankCode"),"11"));
            request.setAttribute("payChannelMaxAmtKJDJUpdate",new PayCoopBankService().detailPayChannelMaxAmt(request.getParameter("bankCode"),"12"));
            request.setAttribute("payChannelMaxAmtZFBUpdate",new PayCoopBankService().detailPayChannelMaxAmt(request.getParameter("bankCode"),"17"));
            request.setAttribute("payChannelMaxAmtQQUpdate",new PayCoopBankService().detailPayChannelMaxAmt(request.getParameter("bankCode"),"27"));
            request.setAttribute("payChannelMaxAmtYHKUpdate",new PayCoopBankService().detailPayChannelMaxAmt(request.getParameter("bankCode"),"10"));
            request.setAttribute("payChannelMaxAmtWXWAPUpdate",new PayCoopBankService().detailPayChannelMaxAmt(request.getParameter("bankCode"),"18"));
            request.setAttribute("payChannelMaxAmtDSUpdate",new PayCoopBankService().detailPayChannelMaxAmt(request.getParameter("bankCode"),"15"));
        } catch (Exception e) {
            e.printStackTrace();
            if(log!=null)log.info("查询详情失败,"+e.getMessage());
        } 
        return "/jsp/pay/coopbank/pay_coop_bank_detail.jsp";
    }
    @RequestMapping("updatePayCoopBank")
    public String updatePayCoopBank(HttpServletRequest request,HttpServletResponse response,
            @ModelAttribute("payCoopBank") PayCoopBank payCoopBank) {
        JWebUser user = (JWebUser)request.getSession().getAttribute("user");
        OutputStream os = null;
        Blog log = null;
        try {
            //处理标识,show跳转,其他更新
            if("show".equals(request.getParameter("flag"))){
                log = new Blog(this.getClass().getSimpleName()+".updatePayCoopBank","取得PayCoopBank",user.id,request);
                request.setAttribute("payCoopBankUpdate",new PayCoopBankService().detailPayCoopBank(request.getParameter("bankCode")));
                request.setAttribute("payChannelMaxAmtB2CJUpdate",new PayCoopBankService().detailPayChannelMaxAmt(request.getParameter("bankCode"),"7"));
                request.setAttribute("payChannelMaxAmtB2CXUpdate",new PayCoopBankService().detailPayChannelMaxAmt(request.getParameter("bankCode"),"8"));
                request.setAttribute("payChannelMaxAmtB2BUpdate",new PayCoopBankService().detailPayChannelMaxAmt(request.getParameter("bankCode"),"9"));
                request.setAttribute("payChannelMaxAmtKJJUpdate",new PayCoopBankService().detailPayChannelMaxAmt(request.getParameter("bankCode"),"11"));
                request.setAttribute("payChannelMaxAmtKJDJUpdate",new PayCoopBankService().detailPayChannelMaxAmt(request.getParameter("bankCode"),"12"));
                request.setAttribute("payChannelMaxAmtZFBUpdate",new PayCoopBankService().detailPayChannelMaxAmt(request.getParameter("bankCode"),"17"));
                request.setAttribute("payChannelMaxAmtQQUpdate",new PayCoopBankService().detailPayChannelMaxAmt(request.getParameter("bankCode"),"27"));
                request.setAttribute("payChannelMaxAmtYHKUpdate",new PayCoopBankService().detailPayChannelMaxAmt(request.getParameter("bankCode"),"10"));
                request.setAttribute("payChannelMaxAmtWXWAPUpdate",new PayCoopBankService().detailPayChannelMaxAmt(request.getParameter("bankCode"),"18"));
                request.setAttribute("payChannelMaxAmtDSUpdate",new PayCoopBankService().detailPayChannelMaxAmt(request.getParameter("bankCode"),"15"));
                PayChannelBankRelationDAO dao = new PayChannelBankRelationDAO();
                request.setAttribute("bankCodes", dao.selectByBankCode(payCoopBank.bankCode));
                request.setAttribute("cbRelationMap", dao.selectChannelBankRelationByCode(payCoopBank.bankCode));
                return "/jsp/pay/coopbank/pay_coop_bank_update.jsp";
            }
            os = response.getOutputStream();
            request.setCharacterEncoding("UTF-8");
            response.setContentType("text/html;charset=UTF-8");
            //登录和权限判断
            if(!JWebUserService.checkUser(user,request)){
               os.write(((String)request.getSession().getAttribute("LOGIN_INFO")).getBytes());
               return null;
            }
            log = new Blog(this.getClass().getSimpleName()+".updatePayCoopBank","修改PayCoopBank",user.id,request);
            //最后维护人员
            payCoopBank.setLstUptOperId(user.id);
            //最后维护时间
            payCoopBank.setLstUptTime(new Date());
            new PayCoopBankService().updatePayCoopBank(payCoopBank,request);
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
    /**
     * 银行状态更改
     * @param request
     * @param response
     * @param string
     * @return
     */
	 @RequestMapping("updatePayCoopBankStatus")
    private String updatePayCoopBankStatus(HttpServletRequest request,HttpServletResponse response) {
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
            log = new Blog(this.getClass().getSimpleName()+".updatePayCoopBankStatus","更改PayCoopBank状态",user.id,request);
            new PayCoopBankService().updatePayCoopBankStatus(request.getParameter("bankCode"),
            		"BANK_STATUS",request.getParameter("value"));
            os.write(JWebConstant.OK.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
            try {os.write(e.getMessage().getBytes("utf-8"));} catch (Exception e1) {}
            if(log!=null)log.info("更改PayCoopBank状态失败,"+e.getMessage());
        } finally {
            if(os != null)try {os.close();} catch (IOException e1) {}
        }
        return null;
	}
	@RequestMapping("payBank")
    public String getPayBankList(HttpServletRequest request,HttpServletResponse response) {
        JWebUser user = (JWebUser)request.getSession().getAttribute("user");
        //登录和权限判断
        if(!JWebUserService.checkUser(user,request))return "/jsp/jweb/jump_login.jsp";
        //处理标识，空：跳转、0：列表
        String flag = request.getParameter("flag");
        OutputStream os = null;
        Blog log = null;
        try {
        	log = new Blog(this.getClass().getSimpleName()+".payBank","查询PayBank列表",user.id,request);
            if(flag == null)return "/jsp/pay/coopbank/pay_bank.jsp";
            request.setCharacterEncoding("UTF-8");
            response.setContentType("text/html;charset=UTF-8");
            os = response.getOutputStream();
            os.write(new PayCoopBankService().getPayBankList().getBytes("utf-8"));
        } catch (Exception e) {
            e.printStackTrace();
            if(log!=null)log.info("查询支持银行列表失败,"+e.getMessage());
        } finally {
            if(os != null)try {os.close();} catch (IOException e1) {}
        }
        return null;
    }
	/**
	 * 判断商户号是否存在
	 * @param bankCode
	 * @param os
	 * @throws Exception
	 */
	@RequestMapping("validBankCode")
	public void validBankCode(HttpServletRequest request,HttpServletResponse response) throws Exception{
		JWebUser user = (JWebUser)request.getSession().getAttribute("user");
        //登录和权限判断
        if(!JWebUserService.checkUser(user,request))return;
		OutputStream os = null;
		try {
            os = response.getOutputStream();
            if(new PayCoopBankService().selectByBankCode(request.getParameter("bankCode"))>=1)os.write("1".getBytes());
            else os.write("0".getBytes());
        } catch (Exception e) {
        	os.write("0".getBytes());
            e.printStackTrace();
        } finally {
            if(os != null)try {os.close();} catch (IOException e1) {}
        }
	}
    /**
     * 路由规则调整
     * @param request
     * @param response
     * @param string
     * @return
     */
	@RequestMapping("adjustChannelRouteRule")
    private String adjustChannelRouteRule(HttpServletRequest request,HttpServletResponse response) {
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
            log = new Blog(this.getClass().getSimpleName()+".adjustChannelRouteRule","调整渠道规则",user.id,request);
            new PayCoopBankService().adjustChannelRouteRule(request);
            os.write(JWebConstant.OK.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
            try {os.write(e.getMessage().getBytes("utf-8"));} catch (Exception e1) {}
            if(log!=null)log.info("调整渠道规则失败,"+e.getMessage());
        } finally {
            if(os != null)try {os.close();} catch (IOException e1) {}
        }
        return null;
	}
	/**
	 * 路由规则回显
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("adjustChannelRule")
    public String adjustChannelRule(HttpServletRequest request,HttpServletResponse response) {
        JWebUser user = (JWebUser)request.getSession().getAttribute("user");
        //登录和权限判断
        if(!JWebUserService.checkUser(user,request))return "/jsp/jweb/jump_login.jsp";
        //处理标识，空：跳转、0：列表
        new Blog(this.getClass().getSimpleName()+".adjustChannelRule","取得PayCoopBankRouteRule列表",user.id,request);
        OutputStream os = null;
        try {
        	List<PayCoopBankRouteRule> routeRuleList = new PayCoopBankService().adjustChannelRule();
        	for (PayCoopBankRouteRule payCoopBankRouteRule : routeRuleList) {
        		//查询支付渠道路由规则列表
				if("0".equals(payCoopBankRouteRule.ruleType)){
					if("7".equals(payCoopBankRouteRule.tranType)){
						request.setAttribute("rChannelRouteRuleJJCount", payCoopBankRouteRule.divisionCount);
						request.setAttribute("rChannelRouteRuleJJAmt", payCoopBankRouteRule.divisionMaxAmt/100);
						continue;
					}
					if("8".equals(payCoopBankRouteRule.tranType)){
						request.setAttribute("rChannelRouteRuleDJCount", payCoopBankRouteRule.divisionCount);
						request.setAttribute("rChannelRouteRuleDJAmt", payCoopBankRouteRule.divisionMaxAmt/100);
						continue;
					}
					if("9".equals(payCoopBankRouteRule.tranType)){
						request.setAttribute("rChannelRouteRulePubCount", payCoopBankRouteRule.divisionCount);
						request.setAttribute("rChannelRouteRulePubAmt", payCoopBankRouteRule.divisionMaxAmt/100);
						continue;
					}
					if("11".equals(payCoopBankRouteRule.tranType)){
						request.setAttribute("rChannelRouteRuleQuickJJCount", payCoopBankRouteRule.divisionCount);
						request.setAttribute("rChannelRouteRuleQuickJJAmt", payCoopBankRouteRule.divisionMaxAmt/100);
						continue;
					}if("12".equals(payCoopBankRouteRule.tranType)){
						request.setAttribute("rChannelRouteRuleQuickDJCount", payCoopBankRouteRule.divisionCount);
						request.setAttribute("rChannelRouteRuleQuickDJAmt", payCoopBankRouteRule.divisionMaxAmt/100);
						continue;
					}
					if("15".equals(payCoopBankRouteRule.tranType)){
						request.setAttribute("rChannelRouteRuleReceiveCount", payCoopBankRouteRule.divisionCount);
						request.setAttribute("rChannelRouteRuleReceiveAmt", payCoopBankRouteRule.divisionMaxAmt/100);
						continue;
					}
					if("16".equals(payCoopBankRouteRule.tranType)){
						request.setAttribute("rChannelRouteRulePayCount", payCoopBankRouteRule.divisionCount);
						request.setAttribute("rChannelRouteRulePayAmt", payCoopBankRouteRule.divisionMaxAmt/100);
						continue;
					}
				//查询优先值规则列表
				}else if("1".equals(payCoopBankRouteRule.ruleType)){
					String channelCode = payCoopBankRouteRule.channelCode;
					if("7".equals(payCoopBankRouteRule.tranType)){
						request.setAttribute("channelRouteRuleJJWeight_"+channelCode, payCoopBankRouteRule.weight);
						continue;
					}
					if("8".equals(payCoopBankRouteRule.tranType)){
						request.setAttribute("channelRouteRuleDJWeight_"+channelCode, payCoopBankRouteRule.weight);
						continue;
					}
					if("9".equals(payCoopBankRouteRule.tranType)){
						request.setAttribute("channelRouteRulePubWeight_"+channelCode, payCoopBankRouteRule.weight);
						continue;
					}
					if("11".equals(payCoopBankRouteRule.tranType)){
						request.setAttribute("channelRouteRuleQuickJJWeight_"+channelCode, payCoopBankRouteRule.weight);
						continue;
					}if("12".equals(payCoopBankRouteRule.tranType)){
						request.setAttribute("channelRouteRuleQuickDJWeight_"+channelCode, payCoopBankRouteRule.weight);
						continue;
					}
					if("15".equals(payCoopBankRouteRule.tranType)){
						request.setAttribute("channelRouteRuleReceiveWeight_"+channelCode, payCoopBankRouteRule.weight);
						continue;
					}
					if("16".equals(payCoopBankRouteRule.tranType)){
						request.setAttribute("channelRouteRulePayWeight_"+channelCode, payCoopBankRouteRule.weight);
						continue;
					}
				}
			}
        	request.setAttribute("routeRuleList", routeRuleList);
        	request.getRequestDispatcher("/jsp/pay/coopbank/pay_coop_bank_route_rule.jsp").forward(request,response);
        } catch (Exception e) {
            e.printStackTrace();
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