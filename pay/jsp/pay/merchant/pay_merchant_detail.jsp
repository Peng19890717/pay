<%@page import="util.PayUtil"%>
<%@page import="java.util.Arrays"%> 
<%@page import="java.text.SimpleDateFormat"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ page import="util.JWebConstant"%>
<%@ page import="com.jweb.dao.JWebUser"%>
<%@ page import="com.jweb.dao.JWebAction"%> 
<%@ page import="com.pay.merchant.dao.PayMerchant"%>
<%@ page import="com.jweb.service.JWebUserService"%>
<%@ page import="com.PayConstant"%>
<%@ page import="com.pay.merchant.service.PayMerchantService"%>
<%@ page import="com.pay.fee.service.PayFeeRateService"%>
<%@ page import="com.pay.fee.dao.PayFeeRate"%>
<%@ page import="com.pay.custstl.dao.PayCustStlInfo"%>
<%@ page import="com.pay.cardbin.service.PayCardBinService"%>
<%@ page import="com.sun.org.apache.xml.internal.security.utils.Base64"%> 
<%
String path = request.getContextPath();
//登录和权限判断
JWebUser user = (JWebUser)session.getAttribute("user");
if(!JWebUserService.checkUser(user,request)){
%><script>window.location.href='<%=path %>/jsp/jweb/login.jsp'</script><%
    return;
}
PayMerchant merchant = (PayMerchant)request.getAttribute("merchantDetail");
if(merchant == null)return;
PayMerchantService service = new PayMerchantService();
PayCustStlInfo payCustStlInfo = (PayCustStlInfo)request.getAttribute("payCustStlInfoDetail");
payCustStlInfo = payCustStlInfo == null ? new PayCustStlInfo() :payCustStlInfo;
java.util.Map feeMap = new PayFeeRateService().getAllPayFeeRateMap();
%>
<script type="text/javascript">
	$(function(){
		hideText("mobileHiddenFormat");
		hideText("emailHiddenFormat");
		hideText("bankCardNoHiddenFormat");
		hideText("nameHiddenFormat");
		hideText("idCardHiddenFormat");
	});
</script>
<div class="easyui-layout" data-options="fit:true">
    <div data-options="region:'center'">
	<div class="merchant-lable">基本信息</div>
	<hr color="#ccc">
    <table cellpadding="5" style="margin-left:20px;" width="100%">
		<tr>
			<td align="right" width="120">商户编号：</td>
     		<td width="250"><%=merchant.custId == null ?"":merchant.custId %></td>
     		<td align="right" width="120">登记时间：</td>
     		<td><%=merchant.createTime == null?"":new SimpleDateFormat("yyyy-MM-dd").format(merchant.createTime) %></td>
    	</tr>
    	<tr>
    		<td align="right">商户类型：</td>
    		<td><%=merchant.merType == null?"":PayConstant.MER_TYPE.get(merchant.merType) %></td>
    		<td align="right">登记人员：</td>
    		<%JWebUser u = service.getOperatorUser(merchant.createUser); %>
    		<td><%=u==null?"":u.name  %></td>
    	</tr>
    	<tr>
    		<td align="right">商户操作员账号：</td>
   			<td><%=merchant.custId == null?"":merchant.custId+"admin" %></td>
   			<td align="right">商户对接IP（生产）</td>
   			<td><%=merchant.interfaceIp == null ?"":merchant.interfaceIp%></td>
   		</tr>
    	<tr>
   			<td align="right">上级代理编号：</td>
     		<td><%= (null==merchant.parentId || "0".equals(merchant.parentId))?"无":merchant.parentId %></td>
     		<td align="right">业务员编号：</td>
     		<td><%= (null==merchant.getUserId() || "0".equals(merchant.getUserId()))?"无":merchant.getUserId() %></td>
   		</tr>
   		<tr>
   			<td align="right" valign="top">风险级别：</td>
   			<td valign="top">
   			<%if("0".equals(merchant.riskLevel))out.print("低"); %>
   			<%if("1".equals(merchant.riskLevel))out.print("中"); %>
   			<%if("2".equals(merchant.riskLevel))out.print("高"); %>
   			</td>
   			<td align="right">业务类型：</td>
    		<td><%=merchant.bizType == null?"":PayConstant.MER_BIZ_TYPE.get(merchant.bizType) %></td>
   		</tr>
    	<tr>
   			<%u = service.getOperatorUser(merchant.checkUser); %>
   			<td align="right" valign="top">审核人员：</td>
   			<td><%=u==null?"":u.name %></td>
   			<td align="right" valign="top">风控报告：</td>
   			<td><%=merchant.riskDesc==null?"":merchant.riskDesc %></td>
   		</tr>
    	<tr>
   			<td align="right" valign="top" rowspan="2">审核意见：</td>
   			<td rowspan="2"><%=merchant.checkInfo==null?"":merchant.checkInfo %></td>
   		</tr>
    </table>
   	<div class="merchant-lable">经营信息</div>
   	<hr color="#ccc">
  	<table cellpadding="5" style="margin-left:20px;" width="100%">
		<tr>
			<td align="right" width="120">商户简称：</td>
   			<td width="250"><%=merchant.storeShortname == null?"":merchant.storeShortname%></td>
   			<td align="right" width="120">注册地址：</td>
   			<td><%=merchant.province == null || merchant.city == null || merchant.region == null?"": merchant.province+merchant.city+merchant.region+merchant.merAddr %></td>
   		</tr>
    	<tr>
    		<td align="right" width="120">商户名称：</td>
   			<td><%=merchant.storeName == null?"":merchant.storeName %></td>
   			<td align="right">公司网站：</td>
    		<td><%=merchant.webAddress == null?"":merchant.webAddress %></td>
   		</tr>
    	<tr>
	   			<td align="right">公司电话：</td>
	   			<td><span class="mobileHiddenFormat"><%=merchant.compayTelNo==null?"":merchant.compayTelNo %></span></td>
	   			<td align="right">公司传真：</td>
	   			<td><%=merchant.compayFax==null?"":merchant.compayFax %></td>
   		</tr>
	 	<tr>
   			<td align="right">ICP备案号：</td>
   			<td><%=merchant.icpNo == null?"":merchant.icpNo %></td>
   			<td align="right">ICP证件号：</td>
   			<td><%=merchant.icpCertNo==null?"":merchant.icpCertNo %></td>
   		</tr>
   		<tr>
   			<td align="right">税务登记证号：</td>
   			<td><%=merchant.taxRegistrationNo == null?"":merchant.taxRegistrationNo %></td>
   			<td align="right">组织机构代码：</td>
   			<td><%=merchant.organizationNo == null?"":merchant.organizationNo %></td>
   		</tr>
   		<tr>
   			<td align="right">注册资本：</td>
   			<td><%=merchant.regCapital == null?"":merchant.regCapital %></td>
   			<td align="right">营业执照注册号：</td>
   			<td><%=merchant.businessLicenceNo == null?"":merchant.businessLicenceNo %></td>
   		</tr>
	 	<tr>
	 		<td align="right" valign="top">营业执照经营范围：</td>
   			<td valign="top"><%=merchant.bizRange==null?"":merchant.bizRange %></td>
   			<td align="right">营业执照经营期限：</td>
   			<td><%=merchant.businessLicenceBeginDate==null?"":new SimpleDateFormat("yyyy-MM-dd").format(merchant.businessLicenceBeginDate) %>~
   				<%=merchant.businessLicenceEndDate==null?"":new SimpleDateFormat("yyyy-MM-dd").format(merchant.businessLicenceEndDate) %>
   			</td>
   		</tr>
   		<tr>
   			<td align="right" valign="top">支持的支付方式：</td>
   			<td valign="top">
	   			<%
	   				char [] charstr = merchant.payWaySupported.toCharArray();
	   			 %>
	   			<%= 48 == charstr[0]  ? "网银借记卡支付&nbsp;&nbsp;" : "" %>
	   			<%= 48 == charstr[1]  ? "网银贷记卡支付&nbsp;&nbsp;" : "" %>
	   			<%= 48 == charstr[2]  ? "B2B支付&nbsp;&nbsp;" : "" %>
	   			<%= 48 == charstr[3]  ? "快捷借记卡支付&nbsp;&nbsp;" : "" %>
	   			<%= 48 == charstr[7]  ? "快捷贷记卡支付" : "" %>
	   			<%= 48 == charstr[4]  ? "代收&nbsp;&nbsp;" : "" %>
	   			<%= 48 == charstr[5]  ? "代付&nbsp;&nbsp;" : "" %>
	   			<%= 48 == charstr[6]  ? "企业宝&nbsp;&nbsp;" : "" %>
	   			<%= 48 == charstr[8]  ? "微信扫码&nbsp;&nbsp;" : "" %>
	   			<%= 48 == charstr[10]  ? "支付宝扫码&nbsp;&nbsp;" : "" %>
	   			<%= 48 == charstr[11]  ? "QQ扫码" : "" %>
   			</td>
   			<td align="right">用户是否互通：</td>
   			<td><%="1".equals(merchant.userInterflowFlag)?"是":"否" %></td>
   		</tr>
   		<tr>
   			<td align="right" valign="top">线下二维码：</td>
   			<td valign="top">
   			<%
   			byte [] b = util.AES.encrypt(merchant.custId.getBytes(),PayConstant.PAY_CONFIG.get("OFFLINE_SCAN_MERNO_PWD"));
			String custId = Base64.encode(b);
   			String url = PayConstant.PAY_CONFIG.get("PAY_SERVER_URL")+"/pos/offlineScan.htm?s="+java.net.URLEncoder.encode(custId);
   			%>
   			<%=url %><br/>
   			<img height="200" width="200" src="<%=request.getContextPath()%>/jsp/pay/payinterface/offline_scan_img.jsp?url=<%=java.net.URLEncoder.encode(url) %>"/>
   			</td>
   			<td align="right">&nbsp;</td>
   			<td>&nbsp;</td>
   		</tr>
 	</table>
	<div class="merchant-lable">企业法人/经办人信息</div>
	<hr color="#ccc">
	<table cellpadding="5" style="margin-left:20px;" width="100%">
		<tr>
			<td align="right" width="120">法人姓名：</td>
   			<td width="250"><%=merchant.merLawPerson == null?"":merchant.merLawPerson%></td>
   			<td align="right" width="120">经办人：</td>
    		<td><%=merchant.bizContact ==null ?"":merchant.bizContact %></td>
   		</tr>
    	<tr>
    		<td align="right">法人证件类型：</td>
   			<td><%=merchant.lawPersonCretType == null?"":PayConstant.CERT_TYPE.get(merchant.lawPersonCretType) %></td>
   			<td align="right">经办人手机：</td>
    		<td><span  class="mobileHiddenFormat"><%=merchant.attentionLineTel ==null?"":merchant.attentionLineTel %></span></td>
   		</tr>
    	<tr>
   			<td align="right">法人证件号码：</td>
   			<td><span class="idCardHiddenFormat"><%=merchant.lawPersonCretNo ==null?"":merchant.lawPersonCretNo %></span></td>
   			<td align="right">经办人邮箱：</td>
   			<td><span  class="emailHiddenFormat"><%=merchant.attentionLineEmail ==null ?"":merchant.attentionLineEmail %></span></td>
   		</tr>
 	</table>
 	<div class="merchant-lable">结算信息</div>
	<hr color="#ccc">
	<table cellpadding="5" style="margin-left:20px;" width="100%">
		<tr>
			<td align="right" width="120">开户行姓名：</td>
   			<td width="250"><span class="nameHiddenFormat"><%=payCustStlInfo.custBankDepositName ==null ?"":payCustStlInfo.custBankDepositName %></span></td>
   			<td align="right" width="120">商户开户银行编码：</td>
   			<td><%=PayCardBinService.BANK_CODE_NAME_MAP.get(merchant.depositBankCode)==null?""
   				:PayCardBinService.BANK_CODE_NAME_MAP.get(merchant.depositBankCode) %></td>
   		</tr>
    	<tr>
    		<td align="right" width="120">开户行网点名称：</td>
   			<td><%=payCustStlInfo.depositBankBrchName == null?"":payCustStlInfo.depositBankBrchName %></td>
   			<td align="right">商户结算银行账号：</td>
    		<td><span class="bankCardNoHiddenFormat"><%=payCustStlInfo.custStlBankAcNo ==null ?"":payCustStlInfo.custStlBankAcNo %></span></td>
   		</tr>
   		<tr>
    		<td align="right" width="120">结算账户开户行行号：</td>
   			<td><%=payCustStlInfo.custStlBankNumber == null ?"":payCustStlInfo.custStlBankNumber %></td>
   			<td align="right"></td>
    		<td></td>
   		</tr>
   		<tr>
    		<td align="right" width="120">结算账户开户省份：</td>
   			<td><%=payCustStlInfo.custStlBankProvince == null ?"":payCustStlInfo.custStlBankProvince %></td>
   			<td align="right">结算账户开户城市：</td>
    		<td><%=payCustStlInfo.custStlBankCity == null ?"":payCustStlInfo.custStlBankCity %></td>
   		</tr>
   		<tr>
    		<td align="right" width="120">结算账户开户身份证号：</td>
   			<td><%=payCustStlInfo.custStlIdno ==null?"":payCustStlInfo.custStlIdno %></td>
   			<td align="right">结算账户开户手机号：</td>
    		<td><%=payCustStlInfo.custStlMobileno ==null ?"":payCustStlInfo.custStlMobileno %></td>
   		</tr>
    	<tr>
   			<td align="right" valign="top">交易结算周期：</td>
   			<td valign="top">
   			<%if("D".equals(merchant.custSetPeriod))out.print("按天"); %>
   			<%if("M".equals(merchant.custSetPeriod))out.print("按月"); %>
   			<%if("W".equals(merchant.custSetPeriod))out.print("按周"); %>
   			</td>
   			<td align="right" valign="top">交易结算时间点：</td>
   			<td>
   			<%if("D".equals(merchant.custSetPeriod))out.print("T+"+merchant.custSetFrey+"<font color=\"#f00\">（T+0时，实时结算到虚拟账户）</font>"); %>
   			<%if("M".equals(merchant.custSetPeriod))out.print(merchant.custStlTimeSet); %>
   			<%if("W".equals(merchant.custSetPeriod))out.print(merchant.custStlTimeSet); %>
   			</td>
   		</tr>
   		<tr>
   			<td align="right" valign="top">代理结算周期：</td>
   			<td valign="top">
   			<%if("D".equals(merchant.custSetPeriodAgent))out.print("按天"); %>
   			<%if("M".equals(merchant.custSetPeriodAgent))out.print("按月"); %>
   			<%if("W".equals(merchant.custSetPeriodAgent))out.print("按周"); %>
   			</td>
   			<td align="right" valign="top">代理结算时间点：</td>
   			<td>
   			<%if("D".equals(merchant.custSetPeriodAgent))out.print("T+"+merchant.custSetFreyAgent); %>
   			<%if("M".equals(merchant.custSetPeriodAgent))out.print(merchant.custStlTimeSetAgent); %>
   			<%if("W".equals(merchant.custSetPeriodAgent))out.print(merchant.custStlTimeSetAgent); %>
   			</td>
   		</tr>
   		<tr>
   			<td align="right" valign="top">代收结算周期：</td>
   			<td valign="top">
   			<%if("D".equals(payCustStlInfo.custSetPeriodDaishou))out.print("按天"); %>
   			<%if("M".equals(payCustStlInfo.custSetPeriodDaishou))out.print("按月"); %>
   			<%if("W".equals(payCustStlInfo.custSetPeriodDaishou))out.print("按周"); %>
   			</td>
   			<td align="right" valign="top">代收结算时间点：</td>
   			<td>
   			<%if("D".equals(payCustStlInfo.custSetPeriodDaishou))out.print("T+"+payCustStlInfo.custStlTimeSetDaishou); %>
   			<%if("W".equals(payCustStlInfo.custSetPeriodDaishou))out.print(PayUtil.parseShow(payCustStlInfo.custStlTimeSetDaishou)); %>
   			
   			<%if("M".equals(payCustStlInfo.custSetPeriodDaishou))out.print(payCustStlInfo.custStlTimeSetDaishou+"日"); %> 
   			</td>
   		</tr>
   		<tr>
   			<td align="right" valign="top">结算账号类型：</td>
   			<td>
  				<%if("1".equals(merchant.bankStlAcNoType))out.print("对私"); %>
  				<%if("2".equals(merchant.bankStlAcNoType))out.print("对公"); %>
   			</td>
   			<td align="right" valign="top">结算类型：</td>
   			<td>
   				<%if("0".equals(merchant.settlementWay))out.print("自动结算到虚拟账户<font color=\"#f00\">（T+N时，此项有作用）</font>"); %>
  				<%if("1".equals(merchant.settlementWay))out.print("线下结算"); %>
  				<%if("2".equals(merchant.settlementWay))out.print("手动结算到虚拟账户"); %>
   			</td>
   		</tr>
   		<tr>
   			<td align="right" valign="top">联行号：</td>
   			<td>
  				<%if("2".equals(merchant.bankStlAcNoType)){%><%=merchant.issuer==null?"":merchant.issuer %><%} %>
   			</td>
   			
   			<td align="right" valign="top">交易手续费收取方式：</td>
   			<td>
				<%if("0".equals(merchant.chargeWay))out.print("结算收取"); %>
  				<%if("1".equals(merchant.chargeWay))out.print("预存手续费收取"); %>
			</td>
   		</tr>
 	</table>
	<div class="merchant-lable">商户费率</div>
	<hr color="#ccc">
	<table cellpadding="5" style="margin-left:20px;" width="100%" >
	
		<tr>
			<td align="right" width="120"><strong>消费结算费率</strong></td>
     		<td colspan="3"></td>
    	</tr>
		<tr>
			<td align="right" width="120">B2C借记卡：</td>
			<td width="250"><%=merchant.feeMap.get(com.PayConstant.CUST_TYPE_MERCHANT+","+merchant.custId+",7")==null?"":
				merchant.feeMap.get(com.PayConstant.CUST_TYPE_MERCHANT+","+merchant.custId+",7").feeName %></td>
			<td align="right" width="120">B2C信用卡：</td>
			<td><%=merchant.feeMap.get(com.PayConstant.CUST_TYPE_MERCHANT+","+merchant.custId+",8")==null?"":
				merchant.feeMap.get(com.PayConstant.CUST_TYPE_MERCHANT+","+merchant.custId+",8").feeName %></td>
    	</tr>
    	<tr>
			<td align="right" width="120">B2B：</td>
			<td width="250"><%=merchant.feeMap.get(com.PayConstant.CUST_TYPE_MERCHANT+","+merchant.custId+",9")==null?"":
				merchant.feeMap.get(com.PayConstant.CUST_TYPE_MERCHANT+","+merchant.custId+",9").feeName %></td>
			<td align="right" width="120">微信：</td>
			<td><%=merchant.feeMap.get(com.PayConstant.CUST_TYPE_MERCHANT+","+merchant.custId+",10")==null?"":
				merchant.feeMap.get(com.PayConstant.CUST_TYPE_MERCHANT+","+merchant.custId+",10").feeName %></td>
    	</tr>
    	<tr>
			<td align="right" width="120">快捷：</td>
			<td width="250"><%=merchant.feeMap.get(com.PayConstant.CUST_TYPE_MERCHANT+","+merchant.custId+",11")==null?"":
				merchant.feeMap.get(com.PayConstant.CUST_TYPE_MERCHANT+","+merchant.custId+",11").feeName %></td>
			<td align="right" width="120">其他消费类型：</td>
			<td><%=merchant.feeMap.get(com.PayConstant.CUST_TYPE_MERCHANT+","+merchant.custId+",12")==null?"":
				merchant.feeMap.get(com.PayConstant.CUST_TYPE_MERCHANT+","+merchant.custId+",12").feeName %></td>
    	</tr>
    	<tr>
			<td align="right" width="120">支付宝扫码：</td>
			<td width="250"><%=merchant.feeMap.get(com.PayConstant.CUST_TYPE_MERCHANT+","+merchant.custId+",17")==null?"":
				merchant.feeMap.get(com.PayConstant.CUST_TYPE_MERCHANT+","+merchant.custId+",17").feeName %></td>
			<td align="right" width="120">微信wap：</td>
			<td><%=merchant.feeMap.get(com.PayConstant.CUST_TYPE_MERCHANT+","+merchant.custId+",18")==null?"":
				merchant.feeMap.get(com.PayConstant.CUST_TYPE_MERCHANT+","+merchant.custId+",18").feeName %></td>
    	</tr>
    	<tr>
			<td align="right" width="120">QQ扫码：</td>
			<td width="250"><%=merchant.feeMap.get(com.PayConstant.CUST_TYPE_MERCHANT+","+merchant.custId+",27")==null?"":
				merchant.feeMap.get(com.PayConstant.CUST_TYPE_MERCHANT+","+merchant.custId+",27").feeName %></td>
			<td align="right" width="120">&nbsp;</td>
			<td>&nbsp;</td>
    	</tr>
    	<tr>
			<td align="right" width="120"><strong>代理结算费率</strong></td>
     		<td colspan="3"></td>
    	</tr>
		<!-- 代理结算费率修改 -->
   		<tr>
			<td align="right" width="120">B2C借记卡：</td>
			<td width="250"><%=merchant.feeMap.get(com.PayConstant.CUST_TYPE_MERCHANT+","+merchant.custId+",19")==null?"":
				merchant.feeMap.get(com.PayConstant.CUST_TYPE_MERCHANT+","+merchant.custId+",19").feeName %></td>
			<td align="right" width="120">B2C信用卡：</td>
			<td><%=merchant.feeMap.get(com.PayConstant.CUST_TYPE_MERCHANT+","+merchant.custId+",20")==null?"":
				merchant.feeMap.get(com.PayConstant.CUST_TYPE_MERCHANT+","+merchant.custId+",20").feeName %></td>
    	</tr>
    	<tr>
			<td align="right" width="120">B2B：</td>
			<td width="250"><%=merchant.feeMap.get(com.PayConstant.CUST_TYPE_MERCHANT+","+merchant.custId+",21")==null?"":
				merchant.feeMap.get(com.PayConstant.CUST_TYPE_MERCHANT+","+merchant.custId+",21").feeName %></td>
			<td align="right" width="120">微信：</td>
			<td><%=merchant.feeMap.get(com.PayConstant.CUST_TYPE_MERCHANT+","+merchant.custId+",22")==null?"":
				merchant.feeMap.get(com.PayConstant.CUST_TYPE_MERCHANT+","+merchant.custId+",22").feeName %></td>
    	</tr>
    	<tr>
			<td align="right" width="120">快捷：</td>
			<td width="250"><%=merchant.feeMap.get(com.PayConstant.CUST_TYPE_MERCHANT+","+merchant.custId+",23")==null?"":
				merchant.feeMap.get(com.PayConstant.CUST_TYPE_MERCHANT+","+merchant.custId+",23").feeName %></td>
			<td align="right" width="120">其他消费类型：</td>
			<td><%=merchant.feeMap.get(com.PayConstant.CUST_TYPE_MERCHANT+","+merchant.custId+",24")==null?"":
				merchant.feeMap.get(com.PayConstant.CUST_TYPE_MERCHANT+","+merchant.custId+",24").feeName %></td>
    	</tr>
    	<tr>
			<td align="right" width="120">支付宝扫码：</td>
			<td width="250"><%=merchant.feeMap.get(com.PayConstant.CUST_TYPE_MERCHANT+","+merchant.custId+",25")==null?"":
				merchant.feeMap.get(com.PayConstant.CUST_TYPE_MERCHANT+","+merchant.custId+",25").feeName %></td>
			<td align="right" width="120">微信wap：</td>
			<td><%=merchant.feeMap.get(com.PayConstant.CUST_TYPE_MERCHANT+","+merchant.custId+",26")==null?"":
				merchant.feeMap.get(com.PayConstant.CUST_TYPE_MERCHANT+","+merchant.custId+",26").feeName %></td>
    	</tr>
    	<tr>
			<td align="right" width="120">QQ扫码：</td>
			<td width="250"><%=merchant.feeMap.get(com.PayConstant.CUST_TYPE_MERCHANT+","+merchant.custId+",28")==null?"":
				merchant.feeMap.get(com.PayConstant.CUST_TYPE_MERCHANT+","+merchant.custId+",28").feeName %></td>
			<td align="right" width="120">&nbsp;</td>
			<td>&nbsp;</td>
    	</tr>
		<tr>
			<td align="right" width="120"><strong>其他费率</strong></td>
     		<td colspan="3"></td>
    	</tr>
		<tr>
			<td align="right" width="120">充值费率：</td>
			<td><%=merchant.feeMap.get(com.PayConstant.CUST_TYPE_MERCHANT+","+merchant.custId+",2")==null?"":
				merchant.feeMap.get(com.PayConstant.CUST_TYPE_MERCHANT+","+merchant.custId+",2").feeName %></td>
			<td align="right">退款费率：</td>
			<td><%=merchant.feeMap.get(com.PayConstant.CUST_TYPE_MERCHANT+","+merchant.custId+",4")==null?"":
				merchant.feeMap.get(com.PayConstant.CUST_TYPE_MERCHANT+","+merchant.custId+",4").feeName %></td>
    	</tr>
    	<tr>
			<td align="right">提现费率：</td>
			<td><%=merchant.feeMap.get(com.PayConstant.CUST_TYPE_MERCHANT+","+merchant.custId+",5")==null?"":
				merchant.feeMap.get(com.PayConstant.CUST_TYPE_MERCHANT+","+merchant.custId+",5").feeName %></td>
   			<td align="right">转账费率：</td>
			<td><%=merchant.feeMap.get(com.PayConstant.CUST_TYPE_MERCHANT+","+merchant.custId+",6")==null?"":
				merchant.feeMap.get(com.PayConstant.CUST_TYPE_MERCHANT+","+merchant.custId+",6").feeName %></td>
   		</tr>
   		<tr>
			<td align="right">商户代收费率：</td>
			<td><%=merchant.feeMap.get(com.PayConstant.CUST_TYPE_MERCHANT+","+merchant.custId+",15")==null?"":
				merchant.feeMap.get(com.PayConstant.CUST_TYPE_MERCHANT+","+merchant.custId+",15").feeName %></td>
   			<td align="right">商户代付费率：</td>
			<td><%=merchant.feeMap.get(com.PayConstant.CUST_TYPE_MERCHANT+","+merchant.custId+",16")==null?"":
				merchant.feeMap.get(com.PayConstant.CUST_TYPE_MERCHANT+","+merchant.custId+",16").feeName %></td>
   		</tr>
   		<tr>
			<td align="right">代理代收费率：</td>
			<td><%=merchant.feeMap.get(com.PayConstant.CUST_TYPE_MERCHANT+","+merchant.custId+",29")==null?"":
				merchant.feeMap.get(com.PayConstant.CUST_TYPE_MERCHANT+","+merchant.custId+",29").feeName %></td>
   			<td align="right">代理代付费率：</td>
			<td><%=merchant.feeMap.get(com.PayConstant.CUST_TYPE_MERCHANT+","+merchant.custId+",30")==null?"":
				merchant.feeMap.get(com.PayConstant.CUST_TYPE_MERCHANT+","+merchant.custId+",30").feeName %></td>
   		</tr>
   		<tr>
			<td align="right">代理入金分润比例：</td>
			<td><%=merchant.agentGoalDiscountRate==null?"":merchant.agentGoalDiscountRate %>%</td>
   			<td align="right">代理代付分润比例：</td>
			<td><%=merchant.agentPayRate==null?"":merchant.agentPayRate %>%</td>
   		</tr>
   		<tr>
			<td align="right">税金率：</td>
			<td><%=merchant.agentTaxRate==null?"":merchant.agentTaxRate %>%</td>
   			<td align="right">&nbsp;</td>
			<td>&nbsp;</td>
   		</tr>
 	</table>
 	<div class="merchant-lable">联系信息</div>
	<hr color="#ccc">
	<table cellpadding="5" style="margin-left:20px;" width="100%">
		<tr>
			<td align="right" width="120">客服联系人：</td>
   			<td width="250"><%=merchant.servContact==null?"":merchant.servContact %></td>
   			<td align="right" width="120">技术联系人：</td>
   			<td><%=merchant.techContact==null?"":merchant.techContact %></td>
   		</tr>
    	<tr>
    		<td align="right" width="120">客服电话：</td>
   			<td><span class="mobileHiddenFormat"><%=merchant.servTelNo==null?"":merchant.servTelNo %></span></td>
   			<td align="right">技术联系人电话：</td>
   			<td><span class="mobileHiddenFormat"><%=merchant.techTelNo==null?"":merchant.techTelNo %></span></td>
   		</tr>
    	<tr>
   			<td align="right">客服邮箱：</td>
   			<td><span class="emailHiddenFormat"><%=merchant.servEmail==null?"":merchant.servEmail %></span></td>
   			<td align="right">技术联系人邮箱：</td>
   			<td><span class="emailHiddenFormat"><%=merchant.techEmail==null?"":merchant.techEmail %></span></td>
   		</tr>
 	 </table>
	<div class="merchant-lable">企业认证</div>
	<hr color="#ccc">
	<table id="fileuploadFlag" style="margin-left:20px;" width="100%">
		<tr><td>&nbsp;</td><td><br/><a href="<%=path + merchant.contractPic%>">认证下载</a></td></tr>
	</table>
	<div class="merchant-lable">电子证书</div>
	<hr color="#ccc">
	<table style="margin-left:20px;">
		<tr>
			<td><%=merchant.cert==null?"":merchant.cert %></td><br/><br/>
		</tr>
	</table>
    </div>
</div>
