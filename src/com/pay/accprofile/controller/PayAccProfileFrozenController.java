package com.pay.accprofile.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import sun.beans.editors.DoubleEditor;
import sun.beans.editors.FloatEditor;
import sun.beans.editors.IntegerEditor;
import sun.beans.editors.LongEditor;
import util.JWebConstant;
import util.Tools;

import com.PayConstant;
import com.jweb.dao.Blog;
import com.jweb.dao.JWebUser;
import com.jweb.service.JWebUserService;
import com.pay.accprofile.dao.PayAccProfileFrozen;
import com.pay.accprofile.service.PayAccProfileFrozenService;
import com.pay.merchant.dao.PayAccProfile;
import com.pay.merchant.service.PayAccProfileService;
import com.pay.merchant.service.PayMerchantService;
import com.pay.user.service.PayTranUserInfoService;
@Controller
public class PayAccProfileFrozenController {
    @RequestMapping("payAccProfileFrozen")
    public String getPayAccProfileFrozenList(HttpServletRequest request,HttpServletResponse response,
            @ModelAttribute("payAccProfileFrozen") PayAccProfileFrozen payAccProfileFrozen) {
        JWebUser user = (JWebUser)request.getSession().getAttribute("user");
        //登录和权限判断
        if(!JWebUserService.checkUser(user,request))return "/jsp/jweb/jump_login.jsp";
        //处理标识，空：跳转、0：列表
        String flag = request.getParameter("flag");
        new Blog(this.getClass().getSimpleName()+".getPayAccProfileFrozenList","取得PayAccProfileFrozen列表",user.id,request);
        OutputStream os = null;
        try {
            if(flag == null)return "/jsp/pay/accprofile/pay_acc_profile_frozen.jsp";
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
            os.write(new PayAccProfileFrozenService().getPayAccProfileFrozenList(payAccProfileFrozen,page,rows,
                request.getParameter("sort"),request.getParameter("order")).getBytes("utf-8"));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(os != null)try {os.close();} catch (IOException e1) {}
        }
        return null;
    }
    /**
     * 冻结资金
     * @param request
     * @param response
     * @param payAccProfileFrozen
     * @return
     */
    @RequestMapping("addPayAccProfileFrozen")
    public String addPayAccProfileFrozen(HttpServletRequest request,HttpServletResponse response,
            @ModelAttribute("payAccProfileFrozen") PayAccProfileFrozen payAccProfileFrozen) {
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
            log = new Blog(this.getClass().getSimpleName()+".addPayAccProfileFrozen","添加PayAccProfileFrozen",user.id,request);
            
            //根据用户输入的类别   判断用户或者商户是否存在
            if(PayConstant.CUST_TYPE_USER.equals(payAccProfileFrozen.getAccType())){
            	if(new PayTranUserInfoService().detailPayTranUserInfoByUserId(payAccProfileFrozen.getAccNo())==null)
            		throw new Exception("客户编号不存在！");
            }
            if(PayConstant.CUST_TYPE_MERCHANT.equals(payAccProfileFrozen.getAccType())){
            	if(new PayMerchantService().detailPayMerchantByCustId(payAccProfileFrozen.getAccNo())==null)
            		throw new Exception("商户编号不存在！");
            }
            //增加金额注意 - 号
            if(payAccProfileFrozen.getAmt()==null || payAccProfileFrozen.getAmt().SIZE==0 
            		|| payAccProfileFrozen.getAmt().toString().substring(0, 1).equals("-"))
            	throw new Exception("输入金额不合法！");
            //判断冻结金额是否超过剩余金额
            PayAccProfile payAccProfile = new PayAccProfileService().detailPayAccProfileByAcTypeAndAcNo(payAccProfileFrozen.getAccType(), payAccProfileFrozen.getAccNo());
            if(payAccProfile.cashAcBal < payAccProfileFrozen.amt || payAccProfile.consAcBal < payAccProfileFrozen.amt)
            	throw new Exception("冻结金额不能大于剩余金额！");
            //客户资金冻结
            payAccProfileFrozen.setId(Tools.getUniqueIdentify());
            payAccProfileFrozen.setCurAmt(payAccProfileFrozen.getAmt());
            payAccProfileFrozen.setOptUser(user.id);
            payAccProfileFrozen.setUpdateTime(new Date());
            payAccProfileFrozen.setStatus("0");
            new PayAccProfileFrozenService().addPayAccProfileFrozen(payAccProfileFrozen);
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
    
    /**
     * 冻结资金
     * @param request
     * @param response
     * @param payAccProfileFrozen
     * @return
     */
    @RequestMapping("updatePayAccProfileFrozen")
    public @ResponseBody String updatePayAccProfileFrozen(HttpServletRequest request,HttpServletResponse response,
            @ModelAttribute("payAccProfileFrozen") PayAccProfileFrozen payAccProfileFrozen) {
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
            //处理标识,show跳转,其他更新
            if("show".equals(request.getParameter("flag"))){
                request.setAttribute("payAccProfileFrozenUpdate", new PayAccProfileFrozenService().detailPayAccProfileFrozenStatus(request.getParameter("id")));
                return "/jsp/pay/accprofile/pay_acc_profile_frozen_update.jsp";
            }
           
            //更新
            String updateId = request.getParameter("updateId");
            String money = request.getParameter("money");
            String remark = payAccProfileFrozen.remark;
            //增加金额注意 - 号
            if(money==null || money.equals("") || money.substring(0, 1).equals("-"))
            	throw new Exception("输入金额不合法！");
            payAccProfileFrozen = new PayAccProfileFrozenService().detailPayAccProfileFrozenStatus(updateId);
            //判断解冻金额是否超出冻结金额
            Long dMoney = payAccProfileFrozen.getCurAmt();
            Long jmoney = new BigDecimal(money).multiply(new BigDecimal("100")).longValue();
            if(jmoney>dMoney){
            	throw new Exception("解冻金额不能大于冻结金额！");
            }
            payAccProfileFrozen.remark = remark;
            new PayAccProfileFrozenService().updatePayAccProfileUnfrozen(payAccProfileFrozen, jmoney, user);
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
    /**
     * 冻结资金审核
     * @param request
     * @param response
     * @param payAccProfileFrozen
     * @return
     */
    @RequestMapping("payAccProfileFrozenCheck")
    public @ResponseBody String payAccProfileFrozenCheck(HttpServletRequest request,HttpServletResponse response,
            @ModelAttribute("payAccProfileFrozen") PayAccProfileFrozen payAccProfileFrozen) {
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
            log = new Blog(this.getClass().getSimpleName()+".payAccProfileFrozenCheck","审核payAccProfileFrozenCheck",user.id,request);
            String remark = payAccProfileFrozen.remark;
            String status = payAccProfileFrozen.status;
            if(!"0".equals(status)&&!"4".equals(status))throw new Exception("审核状态错误");
            payAccProfileFrozen = new PayAccProfileFrozenService().detailPayAccProfileFrozenStatus(payAccProfileFrozen.id);
            payAccProfileFrozen.remark = payAccProfileFrozen.remark+"；"+remark;
            payAccProfileFrozen.status = status;
            new PayAccProfileFrozenService().checkPayAccProfileFrozen(payAccProfileFrozen);
            os.write(JWebConstant.OK.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
            try {os.write(e.getMessage().getBytes("utf-8"));} catch (Exception e1) {}
            if(log!=null)log.info("审核失败,"+e.getMessage());
        } finally {
            if(os != null)try {os.close();} catch (IOException e1) {}
        }
        return null;
    }
    @RequestMapping("detailPayAccProfileFrozen")
	public String detailPayAccProfileFrozen(HttpServletRequest request,HttpServletResponse response,String updateId) {
		ServletOutputStream os = null;
		try {
			if(updateId!=null && !"".equals(updateId)){
				PayAccProfileFrozen payFrozen = new PayAccProfileFrozenService().detailPayAccProfileFrozenStatus(updateId);
				if(payFrozen!=null){
					response.setContentType("text/html; charset=UTF-8");
					response.setCharacterEncoding("UTF-8");
					os = response.getOutputStream();
					os.write(payFrozen.toJson().toString().getBytes("utf-8"));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(os!=null)os.close();
			} catch (Exception e2) {
				// TODO: handle exception
			}
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