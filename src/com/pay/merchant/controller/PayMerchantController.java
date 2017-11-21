package com.pay.merchant.controller;

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

import com.PayConstant;
import com.jweb.dao.Blog;
import com.jweb.dao.JWebUser;
import com.jweb.service.JWebUserService;
import com.pay.custstl.dao.PayCustStlInfo;
import com.pay.custstl.service.PayCustStlInfoService;
import com.pay.fee.dao.PayCustFee;
import com.pay.fee.service.PayCustFeeService;
import com.pay.merchant.dao.PayAccProfile;
import com.pay.merchant.dao.PayMerchant;
import com.pay.merchant.dao.PayMerchantChannelRelation;
import com.pay.merchant.dao.PayMerchantChannelRelationDAO;
import com.pay.merchant.service.PayAccProfileService;
import com.pay.merchant.service.PayMerchantService;
import com.pay.merchant.service.PayStlPeriodOptLogService;
import com.pay.muser.dao.PayMerchantUser;
import com.pay.muser.service.PayMerchantUserService;
import com.pay.user.dao.PaySalesmanMerchantRelation;
import com.pay.user.service.PaySalesmanMerchantRelationService;
@Controller
public class PayMerchantController {
    @RequestMapping("payMerchant")
    public String getPayMerchantList(HttpServletRequest request,HttpServletResponse response,
            @ModelAttribute("payMerchant") PayMerchant payMerchant) {
        JWebUser user = (JWebUser)request.getSession().getAttribute("user");
        //登录和权限判断
        if(!JWebUserService.checkUser(user,request))return "/jsp/jweb/jump_login.jsp";
        //处理标识，空：跳转、0：列表
        String flag = request.getParameter("flag");
        Blog log = null;
        OutputStream os = null;
        try {
        	log = new Blog(this.getClass().getSimpleName()+".payMerchant","查询PayMerchant列表",user.id,request);
            if(flag == null)return "/jsp/pay/merchant/pay_merchant.jsp";
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
            os.write(new PayMerchantService().getPayMerchantList(payMerchant,page,rows,
                request.getParameter("sort"),request.getParameter("order")).getBytes("utf-8"));
        } catch (Exception e) {
            e.printStackTrace();
            if(log!=null)log.info("查询列表失败,"+e.getMessage());
        } finally {
            if(os != null)try {os.close();} catch (IOException e1) {}
        }
        return null;
    }
    @RequestMapping("addPayMerchant")
    public String addPayMerchant(HttpServletRequest request,HttpServletResponse response,
            @ModelAttribute("payMerchant") PayMerchant payMerchant , PayCustStlInfo payCustStlInfo) {
    	JWebUser user = (JWebUser)request.getSession().getAttribute("user");
        OutputStream os = null;
        Blog log = null;
        boolean flag = true;
        try {
            os = response.getOutputStream();
            request.setCharacterEncoding("UTF-8");
            response.setContentType("text/html;charset=UTF-8");
            //登录和权限判断
            if(!JWebUserService.checkUser(user,request)){
               os.write(((String)request.getSession().getAttribute("LOGIN_INFO")).getBytes());
               return null;
            }
            log = new Blog(this.getClass().getSimpleName()+".addPayMerchant","添加PayMerchant",user.id,request);
            PayMerchantService service = new PayMerchantService();
            PayMerchantUserService serviceU = new PayMerchantUserService();
            // 添加商户
            //检查营业执照注册号是否存在
            if(service.isExistRecordByColumn("BUSINESS_LICENCE_NO", payMerchant.businessLicenceNo))throw new Exception("营业执照注册号已存在");
            //检查公司名称是否存在
            if(service.isExistRecordByColumn("STORE_NAME", payMerchant.storeName))throw new Exception("公司名称已存在");
            //检查税务登记号是否存在
            if(service.isExistRecordByColumn("TAX_REGISTRATION_NO", payMerchant.taxRegistrationNo))throw new Exception("税务登记号已存在");
            //检查组织机构号是否存在
            if(service.isExistRecordByColumn("ORGANIZATION_NO", payMerchant.organizationNo))throw new Exception("组织机构号已存在");
            //检查商户操作员是否存在
            if(serviceU.isExistRecordByColumn("USER_ID", payMerchant.merchantOperatorId))throw new Exception("商户操作员账号已存在");
            // 添加商户费率
            PayCustFee custFee = new PayCustFee();
            PayCustFeeService custFeeService = new PayCustFeeService();
            for (int i = 0; i <= 50; i++) {
            	String feeCode = request.getParameter("settleFeeCode" + i);
            	if(feeCode==null||feeCode.length()==0)continue;
            	custFee.id = Tools.getUniqueIdentify();
            	custFee.custType = com.PayConstant.CUST_TYPE_MERCHANT;
            	custFee.custId = payMerchant.custId;
            	custFee.tranType = Integer.toString(i);
            	custFee.feeCode = feeCode;
            	custFee.createTime = new Date();
            	custFeeService.addPayCustFee(custFee);
            }
            // 商户添加
            service.addPayMerchant(payCustStlInfo,user,payMerchant);
            //开关 保证商户添加成功再进行如下操作
            if(flag == true){
            	//添加业务员商户关系
            	PaySalesmanMerchantRelation paySalesmanMerchantRelation = new PaySalesmanMerchantRelation();
            	if(request.getParameter("userId") != null && !"".equals(request.getParameter("userId"))){
            		paySalesmanMerchantRelation.setUserId(request.getParameter("userId"));
            		paySalesmanMerchantRelation.setMerNo(payMerchant.getCustId());
            		new PaySalesmanMerchantRelationService().addPaySalesmanMerchantRelation(paySalesmanMerchantRelation);
            	}
            	// 完善客户结算信息vo添加客户结算信息
            	String result = null;
            	if("W".equals(payCustStlInfo.custSetPeriod)) {
            		String[] parameterValues = request.getParameterValues("custStlWeekSet");
            		result = arrayToString(parameterValues);
            	} else if("M".equals(payCustStlInfo.custSetPeriod)) {
            		String[] parameterValues1 = request.getParameterValues("custStlMonthSet");
            		result = arrayToString(parameterValues1);
            	}
            	String resultAgent = null;
            	if("W".equals(payCustStlInfo.custSetPeriodAgent)) {
            		String[] parameterValuesAgent = request.getParameterValues("custStlWeekSetAgent");
            		resultAgent = arrayToString(parameterValuesAgent);
            	} else if("M".equals(payCustStlInfo.custSetPeriodAgent)) {
            		String[] parameterValuesAgent1 = request.getParameterValues("custStlMonthSetAgent");
            		resultAgent = arrayToString(parameterValuesAgent1);
            	}
            	payCustStlInfo.setCustStlTimeSet(result);
            	payCustStlInfo.setCustStlTimeSetAgent(resultAgent);
            	
            	//代收
            	String resultTimeDaishou = "";
            	if("D".equals(payCustStlInfo.custSetPeriodDaishou)){
            		String custSetFreyDaishou = request.getParameter("custSetFreyDaishou");
            		payCustStlInfo.setCustStlTimeSetDaishou(custSetFreyDaishou);
            	}else if("W".equals(payCustStlInfo.custSetPeriodDaishou)){
            		String[] custStlWeekSetDaishou = request.getParameterValues("custStlWeekSetDaishou");
            		for (String csd : custStlWeekSetDaishou) {
            			resultTimeDaishou = resultTimeDaishou + csd +"|";
            		}
            		resultTimeDaishou = resultTimeDaishou.substring(0, resultTimeDaishou.length()-1);
            		payCustStlInfo.setCustStlTimeSetDaishou(resultTimeDaishou);
            	}else if("M".equals(payCustStlInfo.custSetPeriodDaishou)){
            		String custStlMonthSetDaishou = request.getParameter("custStlMonthSetDaishou");
            		payCustStlInfo.setCustStlTimeSetDaishou(custStlMonthSetDaishou);
            	}
            	new PayCustStlInfoService().addPayCustStlInfo(payMerchant,user,payCustStlInfo);
            	// 增加商户虚拟帐号
            	PayAccProfile payAccProfile = new PayAccProfile();
            	payAccProfile.setId(Tools.getUniqueIdentify());
            	payAccProfile.setPayAcNo(payMerchant.custId);
            	payAccProfile.setAcType(PayConstant.CUST_TYPE_MERCHANT);
            	new PayAccProfileService().addPayAccProfile(payAccProfile );
            	// 增加商户操作人
            	PayMerchantUser merchantUser = new PayMerchantUser();
            	serviceU.addPayMerchantUser(payMerchant,merchantUser);
            	os.write(JWebConstant.OK.getBytes());
            }
        } catch (Exception e) {
        	flag = false;
            e.printStackTrace();
            try {os.write(e.getMessage().getBytes("utf-8"));} catch (Exception e1) {}
            if(log!=null)log.info("添加失败,"+e.getMessage());
        } finally {
            if(os != null)try {os.close();} catch (IOException e1) {}
        }
        return null;
    }
    /**
     * 数组转换成|分割的字符串
     * @param parameterValues
     * @return
     */
    private String arrayToString(String[] parameterValues) {
    	StringBuffer buffer = new StringBuffer();
    	for(int x = 0 ; x  < parameterValues.length ; x ++) {
    		if(parameterValues.length-1 == x) {
    			buffer.append(parameterValues[x]);
    		} else {
    			buffer.append(parameterValues[x]).append("|");
    		}
    	}
		return buffer.toString();
	}
    @RequestMapping("updatePayMerchant")
    public String updatePayMerchant(HttpServletRequest request,HttpServletResponse response,
            @ModelAttribute("payMerchant") PayMerchant payMerchant,PayCustStlInfo payCustStlInfo) {
        JWebUser user = (JWebUser)request.getSession().getAttribute("user");
        //登录和权限判断
        if(!JWebUserService.checkUser(user,request))return "/jsp/jweb/jump_login.jsp";
        OutputStream os = null;
        Blog log = null;
        boolean flag = true;
        try {
            //处理标识,show跳转,其他更新
            if("show".equals(request.getParameter("flag"))){
                log = new Blog(this.getClass().getSimpleName()+".updatePayMerchant","取得PayMerchant",user.id,request);
                PayMerchant temp = new PayMerchantService().detailPayMerchant(request.getParameter("id"));
                request.setAttribute("payMerchantUpdate",temp);
                if(temp != null) request.setAttribute("payCustStlInfoUpdate",
                	new PayCustStlInfoService().getPayCustStlInfoByMerchantId(temp.custId));
                return "/jsp/pay/merchant/pay_merchant_update.jsp";
            }
            os = response.getOutputStream();
            request.setCharacterEncoding("UTF-8");
            response.setContentType("text/html;charset=UTF-8");
            //登录和权限判断
            if(!JWebUserService.checkUser(user,request)){
               os.write(((String)request.getSession().getAttribute("LOGIN_INFO")).getBytes());
               return null;
            }
            log = new Blog(this.getClass().getSimpleName()+".updatePayMerchant","修改PayMerchant",user.id,request);
            if(flag == true){
            	PayMerchantService service = new PayMerchantService();
                // 完善客户结算信息vo添加客户结算信息
                String result = null;
            	if("W".equals(payCustStlInfo.custSetPeriod)) {
            		String[] parameterValues = request.getParameterValues("custStlWeekSet");
            		result = arrayToString(parameterValues);
            	} else if("M".equals(payCustStlInfo.custSetPeriod)) {
            		String[] parameterValues1 = request.getParameterValues("custStlMonthSet");
            		result = arrayToString(parameterValues1);
            	}
            	
            	String resultAgent = null;
            	if("W".equals(payCustStlInfo.custSetPeriodAgent)) {
            		String[] parameterValuesAgent = request.getParameterValues("custStlWeekSetAgent");
            		resultAgent = arrayToString(parameterValuesAgent);
            	} else if("M".equals(payCustStlInfo.custSetPeriodAgent)) {
            		String[] parameterValuesAgent1 = request.getParameterValues("custStlMonthSetAgent");
            		resultAgent = arrayToString(parameterValuesAgent1);
            	}
            	payCustStlInfo.setCustStlTimeSet(result);
            	payCustStlInfo.setCustStlTimeSetAgent(resultAgent);
            	
            	//代收
            	String resultTimeDaishou = "";
            	if("D".equals(payCustStlInfo.custSetPeriodDaishou)){
            		String custSetFreyDaishou = request.getParameter("custSetFreyDaishou");
            		payCustStlInfo.setCustStlTimeSetDaishou(custSetFreyDaishou);
            	}else if("W".equals(payCustStlInfo.custSetPeriodDaishou)){
            		String[] custStlWeekSetDaishou = request.getParameterValues("custStlWeekSetDaishou");
            		for (String csd : custStlWeekSetDaishou) {
            			resultTimeDaishou = resultTimeDaishou + csd +"|";
    				}
            		resultTimeDaishou = resultTimeDaishou.substring(0, resultTimeDaishou.length()-1);
            		payCustStlInfo.setCustStlTimeSetDaishou(resultTimeDaishou);
            	}else if("M".equals(payCustStlInfo.custSetPeriodDaishou)){
            		String custStlMonthSetDaishou = request.getParameter("custStlMonthSetDaishou");
            		payCustStlInfo.setCustStlTimeSetDaishou(custStlMonthSetDaishou);
            	}
            	//保存结算周期修改日志
            	new PayStlPeriodOptLogService().addPayStlPeriodOptLog(request,payMerchant,payCustStlInfo);
            	// 关于费率
                PayCustFeeService custFeeService = new PayCustFeeService();
                // 删除早期的费率
                custFeeService.removePayCustFeeForCustId(payMerchant.custId);
                // 添加商户新费率
                PayCustFee custFee = new PayCustFee();
                for (int i = 0; i <= 50; i++) {
                	String feeCode = request.getParameter("settleFeeCode" + i);
                	if(feeCode==null||feeCode.length()==0)continue;
                	custFee.id = Tools.getUniqueIdentify();
                	custFee.custType = com.PayConstant.CUST_TYPE_MERCHANT;
                	custFee.custId = payMerchant.custId;
                	custFee.tranType = Integer.toString(i);
                	custFee.feeCode = feeCode;
                	custFee.createTime = new Date();
                	custFeeService.addPayCustFee(custFee);
                }
                //添加业务员商户关系
            	PaySalesmanMerchantRelation paySalesmanMerchantRelation = new PaySalesmanMerchantRelation();
        		paySalesmanMerchantRelation.setUserId(request.getParameter("userId"));
        		paySalesmanMerchantRelation.setMerNo(payMerchant.getCustId());
        		new PaySalesmanMerchantRelationService().updatePaySalesmanMerchantRelation(paySalesmanMerchantRelation);
                service.updatePayMerchant(payMerchant,user,payCustStlInfo);
                os.write(JWebConstant.OK.getBytes());
            }
        } catch (Exception e) {
        	flag = false;
            e.printStackTrace();
            try {os.write(e.getMessage().getBytes("utf-8"));} catch (Exception e1) {}
            if(log!=null)log.info("修改失败,"+e.getMessage());
        } finally {
            if(os != null)try {os.close();} catch (IOException e1) {}
        }
        return null;
    }
    
    @RequestMapping("fileUploadForAddMerchant")
    public String uploadFile(@RequestParam("uploadFile") MultipartFile uploadFile,
    		HttpServletRequest request,HttpServletResponse response) throws Exception{
    	JWebUser user = (JWebUser)request.getSession().getAttribute("user");
        //登录和权限判断
        if(!JWebUserService.checkUser(user,request))return null;
        OutputStream os = null;
        String fileName = new java.text.SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + "_"+Tools.getUniqueIdentify();
        JSONObject jsonObject = new JSONObject();
		try {
        	String realPath = request.getSession().getServletContext().getRealPath("/upload");
            if(null != uploadFile){
            	String tmp = uploadFile.getOriginalFilename();
            	fileName = fileName + tmp.substring(tmp.lastIndexOf("."));
            	uploadFile.transferTo(new File(realPath, fileName));
            } 
			os = response.getOutputStream();
			request.setCharacterEncoding("UTF-8");
			response.setContentType("application/json;charset=UTF-8");
			jsonObject.put("saveUrl", "/upload/" + fileName);
			os.write(jsonObject.toString().getBytes());
		} catch (Exception e) {
			e.printStackTrace();
			jsonObject.put("errorMessage", e.getMessage());
            try {os.write(jsonObject.toString().getBytes("utf-8"));} catch (Exception e1) {}
		} finally {
			if(os != null)try {os.close();} catch (IOException e1) {}
		}
        return null;  
    }
    /**
     *  账户禁用
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("disablePayMerchant")
    public String disablePayMerchant(HttpServletRequest request,HttpServletResponse response) {
    	JWebUser user = (JWebUser)request.getSession().getAttribute("user");
        //登录和权限判断
        if(!JWebUserService.checkUser(user,request))return null;
    	return updateStatus(request, response, "禁用");
    }
     
    /**
     *  账户启用
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("ablePayMerchant")
    public String ableablePayMerchant(HttpServletRequest request,HttpServletResponse response) {
    	JWebUser user = (JWebUser)request.getSession().getAttribute("user");
        //登录和权限判断
        if(!JWebUserService.checkUser(user,request))return null;
    	return updateStatus(request, response, "启用");
    }
    
    /**
     *  账户冻结
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("freezePayMerchant")
    public String freezePayMerchant(HttpServletRequest request,HttpServletResponse response) {
    	JWebUser user = (JWebUser)request.getSession().getAttribute("user");
        //登录和权限判断
        if(!JWebUserService.checkUser(user,request))return null;
    	return updateStatus(request, response, "冻结");
    }
    
    /**
     *  账户解冻
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("thawPayMerchant")
    public String thawPayMerchant(HttpServletRequest request,HttpServletResponse response) {
    	JWebUser user = (JWebUser)request.getSession().getAttribute("user");
        //登录和权限判断
        if(!JWebUserService.checkUser(user,request))return null;
    	return updateStatus(request, response, "解冻");
    }
    
    /**
     *  商户审核
     * @param request
     * @param response
     * @param payMerchant
     * @return
     */
    @RequestMapping("changePayMerchantCheckStatus")
    public String changePayMerchantCheckStatus(HttpServletRequest request,HttpServletResponse response,PayMerchant payMerchant) {
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
            log = new Blog(this.getClass().getSimpleName()+".changePayMerchantCheckStatus","审核PayMerchant",user.id,request);
            payMerchant.checkUser = user.id;
            if(new PayMerchantService().checkMerchant(payMerchant)==0)throw new Exception("商户不存在，审核失败");
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
    /**
     *  商户详情页面跳转
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("payMerchantDetail")
    public String PayMerchantDetail(HttpServletRequest request,HttpServletResponse response) {
        Blog log = null;
        try {
            request.setCharacterEncoding("UTF-8");
            response.setContentType("text/html;charset=UTF-8");
            JWebUser user = (JWebUser)request.getSession().getAttribute("user");
            //登录和权限判断
            if(!JWebUserService.checkUser(user,request))return "/jsp/jweb/jump_login.jsp";
            log = new Blog(this.getClass().getSimpleName()+".payMerchantDetail","PayMerchant详情",user.id,request);
	        String custId = request.getParameter("custId");
	        PayMerchant merchant = new PayMerchantService().selectByMerchantDetail(custId);
	        merchant.custStlTimeSet = merchant.custStlTimeSet==null?"":merchant.custStlTimeSet;
	        merchant.custStlTimeSet = "|"+merchant.custStlTimeSet+"|";
	        merchant.custStlTimeSetAgent = merchant.custStlTimeSetAgent==null?"":merchant.custStlTimeSetAgent;
	        merchant.custStlTimeSetAgent = "|"+merchant.custStlTimeSetAgent+"|";
	        String result = ""; 
	        if(("M").equals(merchant.custSetPeriod)) {
	        	for(int i=0; i<31; i++){
	        		if(merchant.custStlTimeSet.indexOf("|"+(i+1)+"|") != -1)result = result+(i+1)+"日&nbsp;";
	        	}
	        } else if(("W").equals(merchant.custSetPeriod)) {
	        	String [] week = {"星期一","星期二","星期三","星期四","星期五","星期六","星期日"};
	        	for(int i=0; i<week.length; i++){
	        		if(merchant.custStlTimeSet.indexOf("|"+(i+1)+"|") != -1)result = result+week[i]+"&nbsp;";
	        	}
	        }
	        String resultAgent = ""; 
	        if(("M").equals(merchant.custSetPeriodAgent)) {
	        	for(int i=0; i<31; i++){
	        		if(merchant.custStlTimeSetAgent.indexOf("|"+(i+1)+"|") != -1)resultAgent = resultAgent+(i+1)+"日&nbsp;";
	        	}
	        } else if(("W").equals(merchant.custSetPeriodAgent)) {
	        	String [] weekAgent = {"星期一","星期二","星期三","星期四","星期五","星期六","星期日"};
	        	for(int i=0; i<weekAgent.length; i++){
	        		if(merchant.custStlTimeSetAgent.indexOf("|"+(i+1)+"|") != -1)resultAgent = resultAgent+weekAgent[i]+"&nbsp;";
	        	}
	        }
	        merchant.setCustStlTimeSet(result);
	        merchant.setCustStlTimeSetAgent(resultAgent);
	        request.setAttribute("merchantDetail",merchant);
	        request.setAttribute("payCustStlInfoDetail",
	            	new PayCustStlInfoService().getPayCustStlInfoByMerchantId(merchant.custId));
	        return "check".equals(request.getParameter("flag"))?"/jsp/pay/merchant/pay_merchant_check.jsp":
	        	("detail".equals(request.getParameter("flag"))?"/jsp/pay/merchant/pay_merchant_detail.jsp":null);
    	} catch (Exception e) {
            e.printStackTrace();
            if(log!=null)log.info("查询失败,"+e.getMessage());
        }
        return null;
    }
    /**
     * 商户状态更改
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
             log = new Blog(this.getClass().getSimpleName()+".updateStatus",info+"PayMerchant",user.id,request);
             new PayMerchantService().updatePayMerchantStatus(request.getParameter("custId"),request.getParameter("columName"),request.getParameter("value"));
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
     * 商户列表excel导出
     * @param request
     * @param response
     * @param payRefund
     * @return
     */
    @RequestMapping("payMerchantExportExcel")
    public String payMerchantExportExcel(HttpServletRequest request,HttpServletResponse response,@ModelAttribute("payMerchant") PayMerchant payMerchant) {
        JWebUser user = (JWebUser)request.getSession().getAttribute("user");
        //登录和权限判断
        if(!JWebUserService.checkUser(user,request))return "/jsp/jweb/jump_login.jsp";
        new Blog(this.getClass().getSimpleName()+".payMerchantExportExcel","导出PayMerchant列表",user.id,request);
        OutputStream os = null;
        Blog log = null;
        try {
        	payMerchant.province = JWebConstant.getWebEncodeStr(request.getParameter("province"));
        	payMerchant.city = JWebConstant.getWebEncodeStr(request.getParameter("city"));
        	payMerchant.region = JWebConstant.getWebEncodeStr(request.getParameter("region"));
            os = response.getOutputStream();
            response.setContentType("application/zip");
            response.addHeader("Content-Disposition","attachment;filename="
            		+new SimpleDateFormat("yyyy-MM-dd").format(new Date())+"_"+Tools.getUniqueIdentify()+".zip");
            log = new Blog(this.getClass().getSimpleName()+".payMerchantExportExcel","导出PayMerchantExcel",user.id,request);
            //调用业务层
            os.write(new PayMerchantService().exportExcelForPayMerchantList(payMerchant));
        } catch (Exception e) {
            e.printStackTrace();
            if(log!=null)log.info("导出失败,"+e.getMessage());
        } finally {
            if(os != null)try {os.close();} catch (IOException e1) {}
        }
        return null;
    }
    /**
     * 判断商家号是否存在（父子节点）
     * @param parentId
     * @param os
     * @throws Exception
     */
    @RequestMapping("validMerchantId")
	public void validMerchantId(HttpServletRequest request,HttpServletResponse response) throws Exception{
    	JWebUser user = (JWebUser)request.getSession().getAttribute("user");
    	if(!JWebUserService.checkUser(user,request))return;
    	OutputStream os = null;    	
    	try {
    		new PayMerchantService().validMerchantId(request.getParameter("parentId"));
    		os = response.getOutputStream();
    		os.write("1".getBytes());
		} catch (Exception e) {
			if(os==null)os = response.getOutputStream();
			os.write(e.getMessage().getBytes("utf-8"));
		} finally {
            if(os != null)try {os.close();} catch (IOException e1) {}
        }
	}
    /**
     * 判断商户号是否存在
     * @param parentId
     * @param os
     * @throws Exception
     */
    @RequestMapping("validPayYakuStlAccMerno")
    public void validPayYakuStlAccMerno(HttpServletRequest request,HttpServletResponse response) throws Exception{
    	JWebUser user = (JWebUser)request.getSession().getAttribute("user");
    	if(!JWebUserService.checkUser(user,request))return;
    	OutputStream os = null;    	
    	try {
    		Long result= new PayMerchantService().validPayYakuStlAccMerno(request.getParameter("custId"));
    		os = response.getOutputStream();
    		os.write(String.valueOf(result).getBytes());
    	} catch (Exception e) {
    		if(os==null)os = response.getOutputStream();
    		os.write(e.getMessage().getBytes("utf-8"));
    	} finally {
    		if(os != null)try {os.close();} catch (IOException e1) {}
    	}
    }
    @RequestMapping("validMerchantType")
    public void validMerchantType(HttpServletRequest request,HttpServletResponse response) throws Exception{
    	JWebUser user = (JWebUser)request.getSession().getAttribute("user");
    	if(!JWebUserService.checkUser(user,request))return;
    	OutputStream os = null;    	
    	os = response.getOutputStream();
    	try {
    		long count = new PayMerchantService().validMerchantType(request.getParameter("parentId"));
    		if(count > 0){
    			//说明该商户是代理
    			os.write("2".getBytes());
    		}else{
    			os.write("3".getBytes());
    		}
    		os.write("1".getBytes());
    	} catch (Exception e) {
    		if(os==null)os = response.getOutputStream();
    		os.write(e.getMessage().getBytes("utf-8"));
    	} finally {
    		if(os != null)try {os.close();} catch (IOException e1) {}
    	}
    }
    
    /**
     * 根据接收到的商户号查询子节点
     * @param request
     * @param response
     * @param parentId
     * @throws Exception
     */
    @RequestMapping("getPayMerchantAgentJson")
    public void getMenuJson(HttpServletRequest request, HttpServletResponse response, String custId) throws Exception {
    	JWebUser user = (JWebUser)request.getSession().getAttribute("user");
        //登录和权限判断
        if(!JWebUserService.checkUser(user,request))return;
        String res = new PayMerchantService().getSubMerchantList(custId);
//        JSONArray array = payMerchantService.treeMerchantList(list, merchant);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/xml");
        try {
        	System.out.println(res);
            response.getWriter().print(res);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * 设置支付通道
     * @param request
     * @param response
     * @param payMerchantChannelRelation
     * @return
     */
    @RequestMapping("setPayChannelForMerchant")
    public String setPayChannelForMerchant(HttpServletRequest request,HttpServletResponse response,
            @ModelAttribute("payMerchantChannelRelation") PayMerchantChannelRelation payMerchantChannelRelation) {
        JWebUser user = (JWebUser)request.getSession().getAttribute("user");
        //登录和权限判断
        if(!JWebUserService.checkUser(user,request))return "/jsp/jweb/jump_login.jsp";
        //处理标识，空：跳转、0：列表
        String flag = request.getParameter("flag");
        new Blog(this.getClass().getSimpleName()+".setPayChannelForMerchant","取得PayMerchantChannelRelation列表",user.id,request);
        OutputStream os = null;
        try {
            if(flag == null)return "/jsp/pay/merchant/set_pay_channel_for_merchant.jsp";
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
            os.write(new PayMerchantService().getPayMerchantChannelRelationList(payMerchantChannelRelation,page,rows,
                request.getParameter("sort"),request.getParameter("order")).getBytes("utf-8"));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(os != null)try {os.close();} catch (IOException e1) {}
        }
        return null;
    }
    /**
     * 判断商户号是否在商户表中且不在支付通道表中
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping("validMerIdForChannel")
	public void validMerIdForChannel(HttpServletRequest request,HttpServletResponse response) throws Exception{
		JWebUser user = (JWebUser)request.getSession().getAttribute("user");
        //登录和权限判断
        if(!JWebUserService.checkUser(user,request))return;
		OutputStream os = null;
		try {
            os = response.getOutputStream();
            //存在返回1，不存在返回0
            os.write(new PayMerchantService().validByCustId(request.getParameter("custId")).getBytes("utf-8"));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(os != null)try {os.close();} catch (IOException e1) {}
        }
	}
    @RequestMapping("addPayMerchantChannelRelation")
    public String addPayMerchantChannelRelation(HttpServletRequest request,HttpServletResponse response) {
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
            log = new Blog(this.getClass().getSimpleName()+".addPayMerchantChannelRelation","添加PayMerchantChannelRelation",user.id,request);
            //选择一种交易类型添加一条数据
            for(int i = 7; i<50 ;i++){
            	String bankCode = request.getParameter("tranType_"+i);
            	if(bankCode != null && !"".equals(bankCode)){
            		//插入数据
            		PayMerchantChannelRelation payMerchantChannelRelation = new PayMerchantChannelRelation();
            		payMerchantChannelRelation.setMerno(request.getParameter("merno")); 
            		payMerchantChannelRelation.setChannelId(bankCode);
            		payMerchantChannelRelation.setTranType(String.valueOf(i));
            		new PayMerchantService().addPayMerchantChannelRelation(payMerchantChannelRelation);
            	}
            }
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
     * 修改支付通道
     * @param request
     * @param response
     * @param payMerchantChannelRelation
     * @return
     */
    @RequestMapping("updatePayChannelForMerchant")
    public String updatePayChannelForMerchant(HttpServletRequest request,HttpServletResponse response,
            @ModelAttribute("payMerchantChannelRelation") PayMerchantChannelRelation payMerchantChannelRelation) {
        JWebUser user = (JWebUser)request.getSession().getAttribute("user");
        OutputStream os = null;
        Blog log = null;
        try {
            //处理标识,show跳转,其他更新
            if("show".equals(request.getParameter("flag"))){
                log = new Blog(this.getClass().getSimpleName()+".updatePayChannelForMerchant","取得PayMerchantChannelRelation",user.id,request);
                request.setAttribute("merno", payMerchantChannelRelation.merno);
                request.setAttribute("channelMerchantMap",
                		new PayMerchantService().selectPayChannelByMerno(payMerchantChannelRelation.merno));
                return "/jsp/pay/merchant/update_pay_channel_for_merchant.jsp";
            }
            os = response.getOutputStream();
            request.setCharacterEncoding("UTF-8");
            response.setContentType("text/html;charset=UTF-8");
            //登录和权限判断
            if(!JWebUserService.checkUser(user,request)){
               os.write(((String)request.getSession().getAttribute("LOGIN_INFO")).getBytes());
               return null;
            }
            log = new Blog(this.getClass().getSimpleName()+".updatePayChannelForMerchant","修改PayMerchantChannelRelation",user.id,request);
            //删除数据
            new PayMerchantChannelRelationDAO().removePayMerchantChannelRelation(payMerchantChannelRelation.merno);
            //选择一种交易类型添加一条数据
            for(int i = 7; i<50 ;i++){
            	String bankCode = request.getParameter("tranType_"+i);
            	if(bankCode != null && !"".equals(bankCode)){
            		//插入数据
            		PayMerchantChannelRelation relation = new PayMerchantChannelRelation();
            		relation.setMerno(request.getParameter("merno")); 
            		relation.setChannelId(bankCode);
            		relation.setTranType(String.valueOf(i));
            		new PayMerchantService().addPayMerchantChannelRelation(relation);
            	}
            }
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
    @RequestMapping("removePayMerchantChannelRelation")
    public String removePayMerchantChannelRelation(HttpServletRequest request,HttpServletResponse response) {
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
            log = new Blog(this.getClass().getSimpleName()+".removePayMerchantChannelRelation","删除PayMerchantChannelRelation",user.id,request);
            new PayMerchantService().removePayMerchantChannelRelation(request.getParameter("merno"));
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
        binder.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd"), true));
        binder.registerCustomEditor(int.class, new IntegerEditor());
        binder.registerCustomEditor(long.class, new LongEditor());
        binder.registerCustomEditor(float.class, new FloatEditor());
        binder.registerCustomEditor(double.class, new DoubleEditor());
    }
}