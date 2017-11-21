package com.pay.contract.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import sun.beans.editors.DoubleEditor;
import sun.beans.editors.FloatEditor;
import sun.beans.editors.IntegerEditor;
import sun.beans.editors.LongEditor;
import util.JWebConstant;

import com.jweb.dao.Blog;
import com.jweb.dao.JWebUser;
import com.jweb.service.JWebUserService;
import com.pay.contract.dao.PayContract;
import com.pay.contract.service.PayContractService;
@Controller
public class PayContractController {
    @RequestMapping("payContract")
    public String getPayContractList(HttpServletRequest request,HttpServletResponse response,
            @ModelAttribute("payContract") PayContract payContract) {
        JWebUser user = (JWebUser)request.getSession().getAttribute("user");
        //登录和权限判断
        if(!JWebUserService.checkUser(user,request))return "/jsp/jweb/jump_login.jsp";
        //处理标识，空：跳转、0：列表
        String flag = request.getParameter("flag");
        OutputStream os = null;
        Blog log = null;
        try {
        	log = new Blog(this.getClass().getSimpleName()+".payContract","获取合同列表",user.id,request);
            if(flag == null)return "/jsp/pay/contract/pay_contract.jsp";
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
            //调用业务层
            os.write(new PayContractService().getPayContractList(payContract,page,rows,
                request.getParameter("sort"),request.getParameter("order")).getBytes("utf-8"));
        } catch (Exception e) {
            e.printStackTrace();
            if(log!=null)log.info("查询列表失败,"+e.getMessage());
        } finally {
            if(os != null)try {os.close();} catch (IOException e1) {}
        }
        return null;
    }
    /**
     * 合同详情
     * @param request
     * @param response
     * @param payContract
     * @return
     */
    @RequestMapping("payContractDetail")
    public String payOrderDetail(HttpServletRequest request,HttpServletResponse response,
    		@ModelAttribute("payContract") PayContract payContract) {
    	//获取session中的用户
        JWebUser user = (JWebUser)request.getSession().getAttribute("user");
        Blog log = null;
        //登录和权限判断
        if(!JWebUserService.checkUser(user,request))return "/jsp/jweb/jump_login.jsp";
        try {
        	//调用业务层
        	log = new Blog(this.getClass().getSimpleName()+".payContractDetail","查询PayContract详情",user.id,request);
        	PayContract contract = new PayContractService().getPayContractDetail(payContract);
        	request.setAttribute("payContract", contract);
			request.getRequestDispatcher("/jsp/pay/contract/pay_contract_detail.jsp").forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
			if(log!=null)log.info("查询详情失败,"+e.getMessage());
		}
        return null;
    }
    
    /**
     * 查询合同版本号PACT_VERS_NO
     * @param request
     * @param response
     * @param pactType
     * @return
     */
    @RequestMapping("payContractPactVersNo")
    public String payContractPactVersNo(HttpServletRequest request,HttpServletResponse response,@RequestParam String pactType) {
    	//获取session中的用户
        JWebUser user = (JWebUser)request.getSession().getAttribute("user");
        //登录和权限判断
        if(!JWebUserService.checkUser(user,request))return "/jsp/jweb/jump_login.jsp";
        //处理标识，空：跳转、0：列表
        String flag = request.getParameter("flag");
        OutputStream os = null;
        Blog log = null;
        try {
        	os = response.getOutputStream();
            request.setCharacterEncoding("UTF-8");
            response.setContentType("text/html;charset=UTF-8");
            log = new Blog(this.getClass().getSimpleName()+".payContractPactVersNo","查询PayContract合同版本号",user.id,request);
            //调用业务层
            os.write(new PayContractService().getPayContractPactVersNo(pactType).getBytes());
        } catch (Exception e) {
            e.printStackTrace();
            if(log!=null)log.info("查询合同版本号失败,"+e.getMessage());
        } finally {
            if(os != null)try {os.close();} catch (IOException e1) {}
        }
        return null;
    }
    @RequestMapping("addPayContract")
    public String addPayContract(HttpServletRequest request,HttpServletResponse response,
            @ModelAttribute("payContract") PayContract payContract) {
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
            log = new Blog(this.getClass().getSimpleName()+".addPayContract","添加PayContract",user.id,request);
            PayContractService service = new PayContractService();
            //判断商户是否存在
            if(!service.existMerchant(payContract.custId))throw new Exception("商户不存在");
            //设置合同序列号  取列号最大值+1，如果空：100000001
            String seqNo=service.getSeqNo();
            payContract.setSeqNo(seqNo);
            //设置建立人员
            payContract.setCreOperId(user.getId());
            //设置最后维护人员
            payContract.setLstUptOperId(user.getId());
            //调用业务层
            service.addPayContract(payContract);
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
    @RequestMapping("addUpdatePayContract")
    public String addUpdatePayContract(HttpServletRequest request,HttpServletResponse response,
    		@ModelAttribute("payContract") PayContract payContract) {
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
    		log = new Blog(this.getClass().getSimpleName()+".addUpdatePayContract","保存修改PayContract",user.id,request);
    		PayContractService service = new PayContractService();
    		//判断商户是否存在
    		if(!service.existMerchant(payContract.custId))throw new Exception("商户不存在");
    		//设置合同序列号  取列号最大值+1，如果空：100000001
    		String seqNo=service.getSeqNo();
    		payContract.setSeqNo(seqNo);
    		//设置最后维护人员
    		payContract.setLstUptOperId(user.id);
    		//设置最后维护时间
    		payContract.setLstUptTime(new Date());
    		//调用业务层
    		service.updatePayContract(payContract);
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
    @RequestMapping("startPayContract")
    public String startPayContract(HttpServletRequest request,HttpServletResponse response) {
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
            log = new Blog(this.getClass().getSimpleName()+".startPayContract","启用PayContract",user.id,request);
            if(new PayContractService().updatePayContractColum("PACT_STATUS","1",request.getParameter("seqNo"))==0){
            	throw new Exception("没有更新");
            }
            os.write(JWebConstant.OK.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
            try {os.write(e.getMessage().getBytes("utf-8"));} catch (Exception e1) {}
            if(log!=null)log.info("操作失败,"+e.getMessage());
        } finally {
            if(os != null)try {os.close();} catch (IOException e1) {}
        }
        return null;
    }
    @RequestMapping("endPayContract")
    public String endPayContract(HttpServletRequest request,HttpServletResponse response) {
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
            log = new Blog(this.getClass().getSimpleName()+".endPayContract","启用PayContract",user.id,request);
            if(new PayContractService().updatePayContractColum("PACT_STATUS","2",request.getParameter("seqNo"))==0){
            	throw new Exception("没有更新");
            }
            os.write(JWebConstant.OK.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
            try {os.write(e.getMessage().getBytes("utf-8"));} catch (Exception e1) {}
            if(log!=null)log.info("操作失败,"+e.getMessage());
        } finally {
            if(os != null)try {os.close();} catch (IOException e1) {}
        }
        return null;
    }
    /**
     * 修改合同
     * @param request
     * @param response
     * @param payContract
     * @return
     */
    @RequestMapping("updatePayContract")
    public String updatePayContract(HttpServletRequest request,HttpServletResponse response,
            @ModelAttribute("payContract") PayContract payContract) {
        JWebUser user = (JWebUser)request.getSession().getAttribute("user");
        OutputStream os = null;
        Blog log = null;
        try {
            //处理标识,show跳转,其他更新
            if("show".equals(request.getParameter("flag"))){
                log = new Blog(this.getClass().getSimpleName()+".updatePayContract","取得PayContract",user.id,request);
                request.setAttribute("payContractUpdate",
                    new PayContractService().detailPayContract(request.getParameter("seqNo")));
                return "/jsp/pay/contract/pay_contract_update.jsp";
            }
            os = response.getOutputStream();
            request.setCharacterEncoding("UTF-8");
            response.setContentType("text/html;charset=UTF-8");
            //登录和权限判断
            if(!JWebUserService.checkUser(user,request)){
               os.write(((String)request.getSession().getAttribute("LOGIN_INFO")).getBytes());
               return null;
            }
            log = new Blog(this.getClass().getSimpleName()+".updatePayContract","修改PayContract",user.id,request);
            new PayContractService().updatePayContract(payContract);
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
     * 设置合同提醒
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("remindPayContract")
    public String remindPayContract(HttpServletRequest request,HttpServletResponse response) {
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
            log = new Blog(this.getClass().getSimpleName()+".remindPayContract","设置合同提醒",user.id,request);
            new PayContractService().setRemindPayContractExpires(
            		request.getParameter("expires"),request.getParameter("mobile"),request.getParameter("email"));
            os.write(JWebConstant.OK.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
            try {os.write(e.getMessage().getBytes("utf-8"));} catch (Exception e1) {}
            if(log!=null)log.info("操作失败,"+e.getMessage());
        } finally {
            if(os != null)try {os.close();} catch (IOException e1) {}
        }
        return null;
    }
    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd"), true));
        binder.registerCustomEditor(int.class, new IntegerEditor());
        binder.registerCustomEditor(long.class, new LongEditor());
        binder.registerCustomEditor(float.class, new FloatEditor());
        binder.registerCustomEditor(double.class, new DoubleEditor());
    }
}