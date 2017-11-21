<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@ page import="util.JWebConstant"%>
<%@ page import="com.jweb.dao.JWebUser"%>
<%@ page import="com.jweb.dao.JWebAction"%>
<%@ page import="com.jweb.service.JWebUserService"%>
<%@ page import="com.pay.merchant.dao.PayMerchant"%>
<%@ page import="com.pay.fee.service.PayFeeRateService"%>
<%@ page import="com.pay.merchant.service.PayMerchantService"%>
<%@ page import="com.pay.fee.dao.PayFeeRate"%>
<%@ page import="com.PayConstant"%> 
<%@ page import="com.pay.custstl.dao.PayCustStlInfo"%>
<%@ page import="com.pay.cardbin.service.PayCardBinService"%> 
<%@ page import="com.pay.bank.dao.PayBank"%> 
<%@ page import="java.util.Map;"%> 
<%
String path = request.getContextPath();
//登录和权限判断
JWebUser user = (JWebUser)session.getAttribute("user");
if(!JWebUserService.checkUser(user,request)){
%><script>window.location.href='<%=path %>/jsp/jweb/login.jsp'</script><%
    return;
}
PayMerchant payMerchant = (PayMerchant)request.getAttribute("payMerchantUpdate");
Long childCount = new PayMerchantService().validMerchantType(payMerchant.custId);
PayCustStlInfo payCustStlInfo = (PayCustStlInfo)request.getAttribute("payCustStlInfoUpdate");
payCustStlInfo = payCustStlInfo == null ? new PayCustStlInfo() :payCustStlInfo;
String custStlTimeSet=payCustStlInfo.custStlTimeSet ==null?"":payCustStlInfo.custStlTimeSet;
custStlTimeSet = custStlTimeSet == null ? "" : "|"+custStlTimeSet+"|";

String custStlTimeSetAgent=payCustStlInfo.custStlTimeSetAgent ==null?"":payCustStlInfo.custStlTimeSetAgent;
custStlTimeSetAgent = custStlTimeSetAgent == null ? "" : "|"+custStlTimeSetAgent+"|";
java.util.List feeList = new PayFeeRateService().getAllPayFeeRate();
PayMerchantService service = new PayMerchantService();
try{
%>
<link rel="stylesheet" type="text/css" href="<%=path %>/js/upload/uploadify.css"  />
<script type="text/javascript" src="<%=path %>/js/upload/jquery.uploadify.min.js"></script>
<script type="text/javascript" src="<%=path %>/js/jquery.form.js"></script>

<script type="text/javascript">
	$(function(){
		$('#fileUploadForUpdateMerchant').change(function(){
			var fileName = $('#fileUploadForUpdateMerchant').val();
			var fileExtension = fileName.substring(fileName.lastIndexOf('.'));
			if(fileExtension != '.zip' && fileExtension != '.rar') {
				topCenterMessage('<%=JWebConstant.ERROR%>','对不起，上传文件格式为(zip,rar)');
				return ;			
			}
			/*
			var options = {
				url:'<%=path %>/fileUploadForAddMerchant.htm',
				type : 'post',
				dataType : 'json',
				success : function(data) {
					$('#uploadFileUrlId').val(data.saveUrl);
				}
			};
			$('#updatePayMerchantForm').ajaxSubmit(options);
			*/
		});
	});
</script>
<div class="easyui-layout" data-options="fit:true">
    <div data-options="region:'center'">
        <%if(payMerchant != null){ %>
<form id="updatePayMerchantForm" method="post">
	<input type="hidden"  name="id" value="<%=payMerchant.id %>"/>
	<input type="hidden"  name="custId" value="<%=payMerchant.custId %>"/>
	<input type="hidden"  name="merStatus" value="0" />
	<input type="hidden"  name="codeTypeId" value="CA" />
	<input type="hidden"  name="custStlType" value="00" />
	<input type="hidden"  name="minStlBalance" value="0"/>
	<input type="hidden"  name="postCode" value=""/>
	<input type="hidden"  name="compayEmail" value=""/>
	<input type="hidden"  name="riskLevel" value="0"/>
	<input type="hidden" name="contractPic" id="uploadFileUrlId" value="<%=payMerchant.contractPic%>">
		<div class="merchant-lable">基本信息</div>
	<hr color="#ccc">
    <table cellpadding="5" style="margin-left:20px;" width="100%">
		<tr>
			<td align="right" width="120">商户编号：</td>
     		<td width="250"><%=payMerchant.custId %></td>
     		<td align="right" width="120">登记时间：</td>
     		<td><%=new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date()) %></td>
    	</tr>
    	<tr>
    		<td align="right">商户类型：</td>
    		<td><select name="merType" id="updatePayMentMerType">
			   		<option value="0" <%="0".equals(payMerchant.merType) ? "selected" : "" %>> 一般商户 </option>
			   		<option value="1" <%="1".equals(payMerchant.merType) ? "selected" : "" %>> 平台商户 </option>	
			   		<option value="2" <%="2".equals(payMerchant.merType) ? "selected" : "" %>> 担保商户 </option>	 					  
		 		</select>
		 	</td>
    		<td align="right">登记人员：</td>
    		<%JWebUser u = service.getOperatorUser(payMerchant.createUser); %>
    		<td><%=u==null?"":u.name  %></td>
    	</tr>
    	<tr>
    		<td align="right">商户操作员账号：</td>
   			<td><%=payMerchant.custId+"admin" %></td>
   			<td align="right">商户对接IP（生产）</td>
   			<td>
   				<input class="easyui-textbox" type="text" id="updatePayMentInterfaceIp" name="interfaceIp" missingMessage="请输入商户IP"
                        validType="IPS[]" invalidMessage="IP错误" data-options="required:true" value="<%=payMerchant.interfaceIp%>" style="width:300px"/>（127.0.0.1为无限制,多个IP以“;”分割）
   			</td>
   		</tr>
   		
   		<tr>
   			<td align="right" width="120">上级代理编号：</td>
     		<td width="250">
     			<input class="easyui-textbox" type="text" id="updatePayMentParentId" name="parentId"  <%=childCount==0?"":"disabled" %>
                        validType="checkPayMentParentId[0]" value="<%= (null==payMerchant.parentId || "0".equals(payMerchant.parentId))?"":payMerchant.parentId %>" style="width:150px"/>
     		</td>
     		<td align="right">业务员编号：</td>
        	<td>
        		<input class="easyui-textbox" type="text" id="updatePayMentRelationUserId" name="userId" missingMessage="请输入业务员编号" 
                    validType="checkPayMentRelationUserId[1]" value="<%= (null==payMerchant.getUserId() || "0".equals(payMerchant.getUserId()))?"":payMerchant.getUserId() %>"/>
            </td>
   		</tr>
   		<tr>
   			<td align="right">业务类型：</td>
   			<td>
   				<select id="addPayMentBizType" name="bizType">
   				<%
   					Map<String, String> map = PayConstant.MER_BIZ_TYPE;
					for (String key : map.keySet()) {
						%>
							<option value="<%= key%>" <%= key.equals(payMerchant.bizType)?"selected":"" %> ><%= map.get(key) %></option>
						<%
				 	}
   				 %>
   				</select>
   			</td>
   		</tr>
    </table>
	<div class="merchant-lable">经营信息<font color="#f00" size="2">（商户名称、税务登记证号、组织机构代码、营业执照注册号 修改后需要重审）</font></div>
   	<hr color="#ccc">
  	<table cellpadding="5" style="margin-left:20px;" width="100%">
		<tr>
			<td align="right" width="120">商户简称：</td>
   			<td width="250"><input class="easyui-textbox" type="text" id="updatePayMentStoreShortname" name="storeShortname" missingMessage="请输入商户简称"
                        validType="length[1,50]" invalidMessage="商户简称为1-50个字符" data-options="required:true"
					    value="<%=payMerchant.storeShortname==null?"":payMerchant.storeShortname %>" style="width:200px"/>
   			</td>
   			<td align="right" width="120">注册地址：</td>
   			<td>
   				<select name="province" id="update_province"></select>
				<select name="city" id="update_city"></select>
				<select name="region" id="update_area"></select>
				<input class="easyui-textbox" type="text" id="updatePayMentMerAddr" name="merAddr" missingMessage="请输入详细地址"
                       validType="length[1,30]" invalidMessage="详细地址为1-30个字符" data-options="required:true"
                       value="<%=payMerchant.merAddr %>" style="width:200px"/>
   			</td>
   		</tr>
    		<tr>
    		<td align="right" width="120">商户名称：</td>
   			<td><input class="easyui-textbox" type="text" id="updatePayMentStoreName" name="storeName" missingMessage="请输入商户名称"
                        validType="length[1,50]" invalidMessage="商户名称为1-50位字符" value="<%=payMerchant.storeName %>"
						data-options="required:true" style="width:200px"/>
   			</td>
   			<td align="right">公司网站：</td>
    			<td><input class="easyui-textbox" type="text" id="updatePayMentWebAddress" name="webAddress" missingMessage="请输入商户网址"
                        validType="length[7,100]" invalidMessage="商户网址为7-100位字符" value="<%=payMerchant.webAddress %>"
						value="<%=payMerchant.webAddress %>" data-options="required:true" style="width:200px"/>
    		</td>
   		</tr>
    		<tr>
	   			<td align="right">公司电话：</td>
	   			<td><input class="easyui-textbox" type="text" id="updatePayMentCompayTelNo" name="compayTelNo"
	                        validType="length[1,20]" invalidMessage="公司电话为1-20位字符"
	                        value="<%=payMerchant.compayTelNo==null?"":payMerchant.compayTelNo %>" style="width:200px"/>
	   			</td>
	   			<td align="right">公司传真：</td>
	   			<td><input class="easyui-textbox" type="text" id="updatePayMentCompayFax" name="compayFax"
	                        validType="length[1,20]" invalidMessage="公司传真为1-20位字符"
	                        value="<%=payMerchant.compayFax==null?"":payMerchant.compayFax %>" style="width:200px"/>
	   			</td>
   		</tr>
	 	<tr>
   			<td align="right">ICP备案号：</td>
   			<td><input class="easyui-textbox" type="text" id="updatePayMentIcpNo" name="icpNo" missingMessage="请输入ICP号"
	                        validType="length[1,20]" invalidMessage="ICP号为1-20位字符"  data-options="required:true"
	                        value="<%=payMerchant.icpNo %>" style="width:200px"/>
   			</td>
   			<td align="right">ICP证件号：</td>
   			<td><input class="easyui-textbox" type="text" id="updatePayMentIcpCertNo" name="icpCertNo"
	                        validType="length[0,20]" invalidMessage="ICP号为1-20位字符"
	                        value="<%=payMerchant.icpNo %>" style="width:200px"/>
   			</td>
   		</tr>
   		<tr>
   			<td align="right">税务登记证号：</td>
   			<td><input class="easyui-textbox" type="text" id="updatePayMentTaxRegistrationNo" name="taxRegistrationNo" missingMessage="请输入税务登记证号"
	                        validType="length[1,20]" invalidMessage="税务登记证号为1-20位字符"  data-options="required:true"
	                        value="<%=payMerchant.taxRegistrationNo %>" style="width:200px"/>
   			</td>
   			<td align="right">组织机构代码：</td>
   			<td><input class="easyui-textbox" type="text" id="updatePayMentOrganizationNo" name="organizationNo" missingMessage="请输入组织机构代码"
	                        validType="length[1,20]" invalidMessage="组织机构代码为1-20位字符"  data-options="required:true"
	                        value="<%=payMerchant.organizationNo %>" style="width:200px"/>
   			</td>
   		</tr>
   		<tr>
   			<td align="right">注册资本：</td>
   			<td><input class="easyui-numberbox" type="text" id="updatePayMentRegCapital" name="regCapital" missingMessage="请输入注册资本"
	                        validType="length[1,15]" invalidMessage="注册资本为1-15位数字"  data-options="required:true"
	                        value="<%=payMerchant.regCapital %>" style="width:200px"/>
   			</td>
   			<td align="right">营业执照注册号：</td>
   			<td><input class="easyui-textbox" type="text" id="updatePayMentBusinessLicenceNo" name="businessLicenceNo" missingMessage="请输入营业执照注册号"
                	validType="length[1,50]" invalidMessage="营业执照注册号为1-50位字符" 
                	value="<%=payMerchant.businessLicenceNo==null?"":payMerchant.businessLicenceNo %>" data-options="required:true" style="width:200px"/></td>
   		</tr>
	 	<tr>
	 		<td align="right" valign="top" rowspan="2">营业执照经营范围：</td>
   			<td rowspan="2">
                 <input class="easyui-textbox" type="text" id="updatePayMentBizRange" name="bizRange" data-options="required:true,multiline:true"
                 	validType="length[0,200]" invalidMessage="营业执照经营范围内容请控制在200个字符以内" 
                 	value="<%=payMerchant.bizRange==null?"":payMerchant.bizRange %>" style="width:200px;height:70px"/>
   			</td>
   			<td align="right" valign="top">营业执照经营期限：</td>
   			<td valign="top"><input type="text" id="updatePayMentBusinessLicenceBeginDate" name="businessLicenceBeginDate" class="easyui-datebox"
   					data-options="editable:false,required:true"  missingMessage="请选择营业执照经营开始期限" style="width:95px"
   					value="<%=payMerchant.businessLicenceBeginDate==null?"":new SimpleDateFormat("yyyy-MM-dd").format(payMerchant.businessLicenceBeginDate) %>"/>~
    			<input type="text" id="updatePayMentBusinessLicenceEndDate" name="businessLicenceEndDate" class="easyui-datebox"
					data-options="editable:false,required:true"  missingMessage="请选择营业执照经营结束期限" style="width:95px"
					value="<%=payMerchant.businessLicenceEndDate==null?"":new SimpleDateFormat("yyyy-MM-dd").format(payMerchant.businessLicenceEndDate) %>"/></td>
   		</tr>
   		<tr>
   			<%
  				char [] charstr = payMerchant.payWaySupported.toCharArray();
  			%>
   			<td align="right" valign="top">支持的支付方式：</td>
   			<td valign="top">
	   			<input type="checkbox" id="payWayJiejiForUpdate" <%= 48 == charstr[0]  ? "checked" : "" %>/>网银借记卡支付&nbsp;&nbsp;
	   			<input type="checkbox" id="payWayXinyongForUpdate" <%= 48 == charstr[1]  ? "checked" : "" %>/>网银贷记卡支付&nbsp;&nbsp;
	   			<input type="checkbox" id="payWayB2bForUpdate" <%= 48 == charstr[2]  ? "checked" : "" %>/>B2B支付&nbsp;&nbsp;
	   			<input type="checkbox" id="payWayKuaijieForUpdate" <%= 48 == charstr[3]  ? "checked" : "" %>/>快捷借记卡支付&nbsp;&nbsp;
	   			<input type="checkbox" id="payWayKuaijieDJForUpdate" <%= 48 == charstr[7]  ? "checked" : "" %>/>快捷贷记卡支付<br/>
	   			<input type="checkbox" id="payWayDaiShouForUpdate" <%= 48 == charstr[4]  ? "checked" : "" %>/>代收&nbsp;&nbsp;
	   			<input type="checkbox" id="payWayDaiFuForUpdate" <%= 48 == charstr[5]  ? "checked" : "" %>/>代付&nbsp;&nbsp;
	   			<input type="checkbox" id="payWayQybForUpdate" <%= 48 == charstr[6]  ? "checked" : "" %>/>企业宝
	   			<input type="checkbox" id="payWayWXScanForUpdate" <%= 48 == charstr[8]  ? "checked" : "" %>/>微信扫码
	   			<input type="checkbox" id="payWayWXWapForUpdate" <%= 48 == charstr[9]  ? "checked" : "" %>/>微信WAP
	   			<input type="checkbox" id="payWayZFBScanForUpdate" <%= 48 == charstr[10]  ? "checked" : "" %>/>支付宝扫码
	   			<input type="checkbox" id="payWayQQScanForUpdate" <%= 48 == charstr[11]  ? "checked" : "" %>/>QQ扫码
	   			<input type="hidden" name="payWaySupported" id="payWaySupportedIdForUpdate"/>
   			</td>
   		</tr>
   		<tr>
    		<td align="right">用户是否互通：</td>
    		<td><select name="userInterflowFlag" id="updatePayMentUserInterflowFlag">
			   		<option value="0" <%="0".equals(payMerchant.userInterflowFlag) ? "selected" : "" %>>否 </option>	
			   		<option value="1" <%="1".equals(payMerchant.userInterflowFlag) ? "selected" : "" %>>是 </option> 					  
		 		</select>
		 	</td>
    	</tr>
 	</table>
 	<div class="merchant-lable">企业法人/经办人信息<font color="#f00" size="2">（法人姓名、法人证件号码 修改后需要重审）</font></div>
	<hr color="#ccc">
	<table cellpadding="5" style="margin-left:20px;" width="100%">
		<tr>
			<td align="right" width="120">法人姓名：</td>
   			<td width="250"><input type="hidden" value="1" name="lawPersonType"/>
	     				<input class="easyui-textbox" type="text" id="updatePayMentMerLawPerson" name="merLawPerson" missingMessage="请输入法人代表姓名"
	                        validType="length[1,20]" invalidMessage="法人代表姓名为1-20位字符" data-options="required:true"
	                        value="<%=payMerchant.merLawPerson %>" style="width:200px"/>
   			</td>
   			<td align="right" width="120">经办人：</td>
    		<td><input class="easyui-textbox" type="text" id="updatePayMentBizContact" name="bizContact" missingMessage="请输入经办人"
                    validType="length[1,20]" invalidMessage="组业务联系人为1-20位字符"  data-options="required:true"
                    value="<%=payMerchant.bizContact %>" style="width:200px"/>
   			</td>
   		</tr>
    		<tr>
    		<td align="right" width="120">法人证件类型：</td>
   			<td><select name="lawPersonCretType" id="updatePayMentLawPersonCretTypeId" >					
					<%	
              			java.util.Iterator<String> it = com.PayConstant.CERT_TYPE.keySet().iterator();
              			while(it.hasNext()){
              				String key = it.next();
              				String value = com.PayConstant.CERT_TYPE.get(key);
              				%><option value="<%=key%>" 
              				<%
              					if(payMerchant != null && key.equals(payMerchant.lawPersonCretType)){
              						%>selected="selected"
              					<%}%>
              				><%=value%></option>
              			<%}
              		%>
				</select>
   			</td>
   			<td align="right">经办人手机：</td>
    		<td><input class="easyui-textbox" type="text" id="updateattentionLineTel" name="attentionLineTel" missingMessage="请输入经办人联系电话"
	                        validType="mobile[]" invalidMessage="请输入正确手机号码（11位）" data-options="required:true"
	                        value="<%=payMerchant.attentionLineTel %>" style="width:200px"/>
				 	接收商户账号信息
    		</td>
   		</tr>
    	<tr>
   			<td align="right">法人证件号码：</td>
   			<td><input class="easyui-textbox" type="text" id="updatePayMentLawPersonCretNo" name="lawPersonCretNo" missingMessage="请输入法人代表证件号码"
	                        validType="idcard['#updatePayMentLawPersonCretTypeId']" invalidMessage=""  data-options="required:true"
	                        value="<%=payMerchant.lawPersonCretNo %>" style="width:200px"/>
   			</td>
   			<td align="right">经办人邮箱：</td>
   			<td><input class="easyui-textbox" type="text" id="updatePayMentattentionLineEmail" name="attentionLineEmail"  missingMessage="请输入经办人联系邮箱"
                  validType="email" invalidMessage="请输入正确邮箱，最多50位字符" data-options="required:true"
                  value="<%=payMerchant.attentionLineEmail %>" style="width:200px"/>
				 	接收商户账号信息
   			</td>
   		</tr>
 	</table>
 	<div class="merchant-lable">结算信息</div>
	<hr color="#ccc">
	<table cellpadding="5" style="margin-left:20px;" width="100%">
		<tr>
			<td align="right" width="120">开户行姓名：</td>
   			<td width="250"><input class="easyui-textbox" type="text" id="updatePayMentCustBankDepositName" name="custBankDepositName"  missingMessage="请输入商户银行开户姓名"
	                        	validType="length[1,50]" invalidMessage="商户银行开户姓名为1-50位字符" data-options="required:true"
	                        value="<%=payCustStlInfo.custBankDepositName==null?"":payCustStlInfo.custBankDepositName %>" style="width:200px"/>
   			</td>
   			<td align="right" width="120">商户开户银行编码：</td>
   			<td>
	            <select name="depositBankCode" style="width:200px">
					<%
					String bankCode = payCustStlInfo.depositBankCode==null?"":payCustStlInfo.depositBankCode;
            		for(int i=0; i<PayCardBinService.BANK_CODE_NAME_LIST.size(); i++){
            			PayBank bank = PayCardBinService.BANK_CODE_NAME_LIST.get(i);
            		 	%><option value="<%=bank.bankCode %>" <%=bankCode.equals(bank.bankCode)?"selected":"" %>><%=bank.bankName %></option><%
            		}%>
				</select>
   			</td>
   		</tr>
    		<tr>
    		<td align="right" width="120">开户行网点名称：</td>
   			<td><input class="easyui-textbox" type="text" id="updatePayMentDepositBankBrchName" name="depositBankBrchName"  missingMessage="请输入开户银行网点名称"
	                        	validType="length[1,50]" invalidMessage="开户银行网点名称为1-50位字符" data-options="required:true"
	                        	value="<%=payCustStlInfo.depositBankBrchName==null?"":payCustStlInfo.depositBankBrchName %>" style="width:200px"/>
   			</td>
   			<td align="right">商户结算银行账号：</td>
    		<td><input class="easyui-textbox" type="text" id="updatePayMentCustStlBankAcNo" name="custStlBankAcNo"  missingMessage="请输入商户结算银行账号"
	                        	validType="length[1,30]" invalidMessage="商户结算银行账号为1-30位字符" data-options="required:true"
	                        	value="<%=payCustStlInfo.custStlBankAcNo==null?"":payCustStlInfo.custStlBankAcNo %>" style="width:200px"/>
    		</td>
   		</tr>
   		<tr><td align="right" width="120">结算账户开户行行号：</td>
   			<td><input class="easyui-textbox" type="text" id="updatePayMentcustStlBankNumber" name="custStlBankNumber" validType="length[1,50]"
            	value="<%=payCustStlInfo.custStlBankNumber==null?"":payCustStlInfo.custStlBankNumber %>" style="width:200px"/>
   			</td>
   		</tr>
   		<tr>
    		<td align="right" width="120">结算账号开户省份：</td>
   			<td><input class="easyui-textbox" type="text" id="updatePayMentcustStlBankProvince" name="custStlBankProvince" validType="length[1,50]"
            	value="<%=payCustStlInfo.custStlBankProvince==null?"":payCustStlInfo.custStlBankProvince %>" style="width:200px"/>
   			</td>
   			<td align="right">结算账号开户城市：</td>
    		<td><input class="easyui-textbox" type="text" id="updatePayMentcustStlBankCity" name="custStlBankCity" validType="length[1,30]" 
                value="<%=payCustStlInfo.custStlBankCity==null?"":payCustStlInfo.custStlBankCity %>" style="width:200px"/>
    		</td>
   		</tr>
   		<tr>
    		<td align="right" width="120">结算账号开户身份证号：</td>
   			<td><input class="easyui-textbox" type="text" id="updatePayMentcustStlIdno" name="custStlIdno" validType="length[15,18]" invalidMessage="请输入15或18位身份证号" 
            	value="<%=payCustStlInfo.custStlIdno==null?"":payCustStlInfo.custStlIdno %>" style="width:200px"/>
   			</td>
   			<td align="right">结算账号开户手机号：</td>
    		<td><input class="easyui-textbox" type="text" id="updatePayMentcustStlMobileno" name="custStlMobileno" validType="length[11,11]" invalidMessage="请输入11位手机号"
                value="<%=payCustStlInfo.custStlMobileno==null?"":payCustStlInfo.custStlMobileno %>" style="width:200px"/>
    		</td>
   		</tr>
   		
   		
    	<tr>
   			<td align="right" valign="top">交易结算周期：</td>
   			<td valign="top">
	   			<select name="custSetPeriod" id="updateMerchantCustSetPeriod"  onchange="javascript:getPerIodUpdate()"   >
					<option value="D" <%="D".equals(payCustStlInfo.custSetPeriod) ? "selected" : "" %>>按天</option>
					<option value="W" <%="W".equals(payCustStlInfo.custSetPeriod) ? "selected" : "" %>>按周</option>                
					<option value="M" <%="M".equals(payCustStlInfo.custSetPeriod) ? "selected" : "" %>>按月</option>
				</select>
   			</td>
   			<td align="right" valign="top">交易结算时间点设置：</td>
   			<td valign="top">
   			<div class="unit" id="updateMerchantDayStl">
                    <select name="custSetFrey" id="updatePayMentCustSetFrey" class="easyui-combobox"  data-options="required:true" style="width:200px">
                    	<%for(int i=0; i<=30; i++){ 
	                		%><option value="<%=i %>" <%=String.valueOf(i).equals(payCustStlInfo.custSetFrey)?"selected":"" %>>T+<%=i %></option><%
	                	} %>
                    </select>
                    <font color="#f00">（T+0时，实时结算到虚拟账户）</font>
				</div>
				<div id="updateMerchantStlnam"  style="display:none"   >
				<div id="updateMerchantCustStlWeekSetType">
				<% String [] week = {"星期一","星期二","星期三","星期四","星期五","星期六","星期日"};
				for(int i = 0; i<7; i++){ 
					%><input type="checkbox" name="custStlWeekSet"  value="<%=i+1 %>" 
						<%="W".equals(payCustStlInfo.custSetPeriod)&&custStlTimeSet.indexOf("|"+(i+1)+"|")!=-1?"checked":"" %>/><%=week[i] %><%
				} %>
				   <font color="red"><b>*</b></font>
				</div>
			  </div>
			  <div class="unit" id="updateMerchantStlMonth"  style="display:none">
				<div id="updateMerchantCustStlMonthSetType">
				   <%for(int i = 1; i<=28; i++){ 
						%><input type="radio" name="custStlMonthSet" <%=("|"+i+"|").equals(custStlTimeSet)? "checked=checked":"" %>  value="<%=i %>"/><%=i %><%=i%15==0?"<br/>":"" %><%
					} %>
				</div>
			  </div>
   			</td>
   		</tr>
   		
   		<tr>
   			<td align="right" valign="top">代理结算周期：</td>
   			<td valign="top">
	   			<select name="custSetPeriodAgent" id="updateMerchantCustSetPeriodAgent"  onchange="javascript:getPerIodAgentUpdate()"   >
					<option value="D" <%="D".equals(payCustStlInfo.custSetPeriodAgent) ? "selected" : "" %>>按天</option>
					<option value="W" <%="W".equals(payCustStlInfo.custSetPeriodAgent) ? "selected" : "" %>>按周</option>
					<option value="M" <%="M".equals(payCustStlInfo.custSetPeriodAgent) ? "selected" : "" %>>按月</option>                
				</select>
   			</td>
   			<td align="right" valign="top">代理结算时间点设置：</td>
   			<td valign="top">
   				<div class="unit" id="updateMerchantDayStlAgent">
                    <select name="custSetFreyAgent" id="updatePayMentCustSetFreyAgent" class="easyui-combobox"  data-options="required:true" style="width:200px">
                    	<%for(int i=1; i<=30; i++){ 
	                		%><option value="<%=i %>" <%=String.valueOf(i).equals(payCustStlInfo.custSetFreyAgent)?"selected":"" %>>T+<%=i %></option><%
	                	} %>
                    </select>
				</div>
				<div class="unit" id="updateMerchantStlnamAgent"  style="display:none"   >
				<div id="updateMerchantCustStlWeekSetTypeAgent">
				<% String [] weekAgent = {"星期一","星期二","星期三","星期四","星期五","星期六","星期日"};
				for(int i = 0; i<7; i++){ 
					%><input type="checkbox" name="custStlWeekSetAgent"  value="<%=i+1 %>" 
						<%="W".equals(payCustStlInfo.custSetPeriodAgent) && custStlTimeSetAgent.indexOf("|"+(i+1)+"|")!=-1?"checked":"" %>/><%=weekAgent[i] %><%
				} %>
				   <font color="red"><b>*</b></font>
				</div>
			  </div>
			  <div class="unit" id="updateMerchantStlMonthAgent"  style="display:none">
				<div id="updateMerchantCustStlMonthSetTypeAgent">
				   <%for(int i = 1; i<=28; i++){ 
						%><input type="radio" name="custStlMonthSetAgent" <%=("|"+i+"|").equals(custStlTimeSetAgent)? "checked":"" %>  value="<%=i %>"/><%=i %><%=i%15==0?"<br/>":"" %><%
					} %>
				</div>
			  </div>
   			</td>
   		</tr>
   		
   		<tr>
   			<td align="right" valign="top">代收结算周期：</td>
   			<td valign="top">
	   			<select name="custSetPeriodDaishou" id="updateMerchantCustSetPeriodDaishou"  onchange="javascript:getPerIodDaishouUpdate()"   >
					<option value="D" <%="D".equals(payCustStlInfo.custSetPeriodDaishou) ? "selected" : "" %>>按天</option>
					<option value="W" <%="W".equals(payCustStlInfo.custSetPeriodDaishou) ? "selected" : "" %>>按周</option>
					<option value="M" <%="M".equals(payCustStlInfo.custSetPeriodDaishou) ? "selected" : "" %>>按月</option>                
				</select>
   			</td>
   			<td align="right" valign="top">代收结算时间点设置：</td>
   			<td valign="top">
   				<div class="unit" id="updateMerchantDayStlDaishou" <%="D".equals(payCustStlInfo.custSetPeriodDaishou)? "" :"style='display:none'" %>>
                    <select name="custSetFreyDaishou" id="updatePayMentCustSetFreyDaishou" class="easyui-combobox"  data-options="required:true" style="width:200px">
                    	<%
                    	for(int i=1; i<=30; i++){ 
	                		%><option value="<%=i %>" <%= "D".equals(payCustStlInfo.custSetPeriodDaishou) && String.valueOf(i).equals(payCustStlInfo.custStlTimeSetDaishou)?"selected":"" %>>T+<%=i %></option><%
	                	} %>
                    </select>
				</div>
				<div class="unit" id="updateMerchantStlnamDaishou"  <%="W".equals(payCustStlInfo.custSetPeriodDaishou)? "" :"style='display:none'" %>   >
				<div id="updateMerchantCustStlWeekSetTypeDaishou">
				<% String [] weekAgentDaishou = {"星期一","星期二","星期三","星期四","星期五","星期六","星期日"};
				for(int i = 0; i<7; i++){ 
					%><input type="checkbox" name="custStlWeekSetDaishou"  value="<%=i+1 %>" 
						<%="W".equals(payCustStlInfo.custSetPeriodDaishou) && payCustStlInfo.custStlTimeSetDaishou.contains(String.valueOf(i+1))?"checked":"" %>/><%=weekAgentDaishou[i] %><%
				} %>
				   <font color="red"><b>*</b></font>
				</div>
			  </div>
			  <div class="unit" id="updateMerchantStlMonthDaishou"  <%="M".equals(payCustStlInfo.custSetPeriodDaishou)? "" :"style='display:none'" %>>
				<div id="updateMerchantCustStlMonthSetTypeDaishou">
				   <%for(int i = 1; i<=28; i++){ 
						%><input type="radio" name="custStlMonthSetDaishou" <%="M".equals(payCustStlInfo.custSetPeriodDaishou) && String.valueOf(i).equals(payCustStlInfo.custStlTimeSetDaishou)? "checked":"" %>  value="<%=i %>"/><%=i %><%=i%15==0?"<br/>":"" %><%
					} %>
				</div>
			  </div>
   			</td>
   		</tr>
   		
   		<tr>
   			<td align="right" valign="top">结算账号类型：</td>
   			<td>
	   			<select name="bankStlAcNoType" id="bankStlAcNoType" onchange="javascript:setIssuer(this)" style="width:200px">
					<option value="1" <%="1".equals(payMerchant.bankStlAcNoType) ? "selected" : "" %>>对私</option>
					<option value="2" <%="2".equals(payMerchant.bankStlAcNoType) ? "selected" : "" %>>对公</option> 
				</select>
				<script type="text/javascript">
					function setIssuer(obj){
						if(obj.value=='2'){
							$("#addPayMentIssuer").textbox("enable");
							$('#addPayMentIssuer').textbox({required:true});
						} else {
							$("#addPayMentIssuer").textbox("disable");
							$('#addPayMentIssuer').textbox({required:false,});
						}
					}
				</script>
   			</td>
   			<td align="right" valign="top">结算类型：</td>
   			<td>
	   			<select name="settlementWay" style="width:200px">
					<option value="0" <%="0".equals(payMerchant.settlementWay) ? "selected" : "" %>>自动结算到虚拟账户</option>
					<option value="1" <%="1".equals(payMerchant.settlementWay) ? "selected" : "" %>>线下结算</option>
					<option value="2" <%="2".equals(payMerchant.settlementWay) ? "selected" : "" %>>手动结算到虚拟账户</option>          
					<%-- <option value="3" <%="3".equals(payMerchant.settlementWay) ? "selected" : "" %>>实时结算到虚拟账户</option> --%>
				</select>
				<font color="#f00">（T+N时，此项有作用）</font>
   			</td>
   		</tr>
   		<tr>
   			<td align="right" valign="top">联行号：</td>
   			<td>
	   			<input class="easyui-textbox" type="text" id="addPayMentIssuer" name="issuer" missingMessage="请输入联行号"
            		validType="length[1,32]" invalidMessage="联行号为1-32位字符"  value="<%=payMerchant.issuer==null?"":payMerchant.issuer %>" style="width:200px"
            		<%="1".equals(payMerchant.bankStlAcNoType) ? "disabled=\"disabled\"" : "" %>/>
   			</td>
   			<td align="right" valign="top">交易手续费收取方式：</td>
   			<td><select name="chargeWay" style="width:200px">
				<option value="0" <%="0".equals(payMerchant.chargeWay) ? "selected" : "" %>>结算收取</option>
				<option value="1" <%="1".equals(payMerchant.chargeWay) ? "selected" : "" %>>预存手续费收取</option>
			</select></td>
   		</tr>
 	</table>
 	<div class="merchant-lable">商户费率</div>
	<hr color="#ccc">
	<table cellpadding="5" style="margin-left:20px;" width="100%">
		<tr>
			<td align="right" width="120"><strong>消费结算费率</strong></td>
     		<td colspan="3"></td>
    	</tr>
		<tr>
			<td align="right" width="120">网银借记卡：</td>
     		<td width="250"><select name="settleFeeCode7" class="easyui-combobox" id="settleFeeCode7" validType="inputExistInCombobox['settleFeeCode7']">
						<%for(int i = 0; i<feeList.size(); i++){
							PayFeeRate fee = (PayFeeRate)feeList.get(i);
							if(!com.PayConstant.CUST_TYPE_MERCHANT.equals(fee.custType)||!"1".equals(fee.tranType))continue;
							PayFeeRate tmp = payMerchant.feeMap.get(com.PayConstant.CUST_TYPE_MERCHANT+","+payMerchant.custId+",7");
							%><option value="<%=fee.feeCode %>" <%= tmp!=null && fee.feeCode.equals(tmp.feeCode)?"selected":"" %>><%=fee.feeName %></option><%
							} %>
					</select>
     		</td>
     		<td align="right" width="120">网银信用卡：</td>
     		<td><select name="settleFeeCode8" class="easyui-combobox" id="settleFeeCode8" validType="inputExistInCombobox['settleFeeCode8']">
						<%for(int i = 0; i<feeList.size(); i++){
							PayFeeRate fee = (PayFeeRate)feeList.get(i);
							if(!com.PayConstant.CUST_TYPE_MERCHANT.equals(fee.custType)||!"1".equals(fee.tranType))continue;
							PayFeeRate tmp = payMerchant.feeMap.get(com.PayConstant.CUST_TYPE_MERCHANT+","+payMerchant.custId+",8");
							%><option value="<%=fee.feeCode %>" <%= tmp!=null && fee.feeCode.equals(tmp.feeCode)?"selected":"" %>><%=fee.feeName %></option><%
							} %>
					</select> 
     		</td>
    	</tr>
    	<tr>
			<td align="right" width="120">B2B：</td>
     		<td width="250"><select name="settleFeeCode9" class="easyui-combobox" id="settleFeeCode9" validType="inputExistInCombobox['settleFeeCode9']">
						<%for(int i = 0; i<feeList.size(); i++){
							PayFeeRate fee = (PayFeeRate)feeList.get(i);
							if(!com.PayConstant.CUST_TYPE_MERCHANT.equals(fee.custType)||!"1".equals(fee.tranType))continue;
							PayFeeRate tmp = payMerchant.feeMap.get(com.PayConstant.CUST_TYPE_MERCHANT+","+payMerchant.custId+",9");
							%><option value="<%=fee.feeCode %>" <%= tmp!=null && fee.feeCode.equals(tmp.feeCode)?"selected":"" %>><%=fee.feeName %></option><%
							} %>
					</select>
     		</td>
     		<td align="right" width="120">微信：</td>
     		<td><select name="settleFeeCode10" class="easyui-combobox" id="settleFeeCode10" validType="inputExistInCombobox['settleFeeCode10']">
						<%for(int i = 0; i<feeList.size(); i++){
							PayFeeRate fee = (PayFeeRate)feeList.get(i);
							if(!com.PayConstant.CUST_TYPE_MERCHANT.equals(fee.custType)||!"1".equals(fee.tranType))continue;
							PayFeeRate tmp = payMerchant.feeMap.get(com.PayConstant.CUST_TYPE_MERCHANT+","+payMerchant.custId+",10");
							%><option value="<%=fee.feeCode %>" <%= tmp!=null && fee.feeCode.equals(tmp.feeCode)?"selected":"" %>><%=fee.feeName %></option><%
							} %>
					</select> 
     		</td>
    	</tr>
    	<tr>
			<td align="right" width="120">快捷借记卡：</td>
     		<td width="250"><select name="settleFeeCode11" class="easyui-combobox" id="settleFeeCode11" validType="inputExistInCombobox['settleFeeCode11']">
						<%for(int i = 0; i<feeList.size(); i++){
							PayFeeRate fee = (PayFeeRate)feeList.get(i);
							if(!com.PayConstant.CUST_TYPE_MERCHANT.equals(fee.custType)||!"1".equals(fee.tranType))continue;
							PayFeeRate tmp = payMerchant.feeMap.get(com.PayConstant.CUST_TYPE_MERCHANT+","+payMerchant.custId+",11");
							%><option value="<%=fee.feeCode %>" <%= tmp!=null && fee.feeCode.equals(tmp.feeCode)?"selected":"" %>><%=fee.feeName %></option><%
							} %>
					</select>
     		</td>
     		<td align="right" width="120">快捷贷记卡：</td>
     		<td><select name="settleFeeCode12" class="easyui-combobox" id="settleFeeCode12" validType="inputExistInCombobox['settleFeeCode12']">
						<%for(int i = 0; i<feeList.size(); i++){
							PayFeeRate fee = (PayFeeRate)feeList.get(i);
							if(!com.PayConstant.CUST_TYPE_MERCHANT.equals(fee.custType)||!"1".equals(fee.tranType))continue;
							PayFeeRate tmp = payMerchant.feeMap.get(com.PayConstant.CUST_TYPE_MERCHANT+","+payMerchant.custId+",12");
							%><option value="<%=fee.feeCode %>" <%= tmp!=null && fee.feeCode.equals(tmp.feeCode)?"selected":"" %>><%=fee.feeName %></option><%
							} %>
					</select>
     		</td>
    	</tr>
    	<tr>
			<td align="right" width="120">支付宝扫码：</td>
     		<td width="250"><select name="settleFeeCode17" class="easyui-combobox" id="settleFeeCode17" validType="inputExistInCombobox['settleFeeCode17']">
						<%for(int i = 0; i<feeList.size(); i++){
							PayFeeRate fee = (PayFeeRate)feeList.get(i);
							if(!com.PayConstant.CUST_TYPE_MERCHANT.equals(fee.custType)||!"1".equals(fee.tranType))continue;
							PayFeeRate tmp = payMerchant.feeMap.get(com.PayConstant.CUST_TYPE_MERCHANT+","+payMerchant.custId+",17");
							%><option value="<%=fee.feeCode %>" <%= tmp!=null && fee.feeCode.equals(tmp.feeCode)?"selected":"" %>><%=fee.feeName %></option><%
							} %>
					</select>
     		</td>
     		<td align="right" width="120">微信wap：</td>
     		<td><select name="settleFeeCode18" class="easyui-combobox" id="settleFeeCode18" validType="inputExistInCombobox['settleFeeCode18']">
						<%for(int i = 0; i<feeList.size(); i++){
							PayFeeRate fee = (PayFeeRate)feeList.get(i);
							if(!com.PayConstant.CUST_TYPE_MERCHANT.equals(fee.custType)||!"1".equals(fee.tranType))continue;
							PayFeeRate tmp = payMerchant.feeMap.get(com.PayConstant.CUST_TYPE_MERCHANT+","+payMerchant.custId+",18");
							%><option value="<%=fee.feeCode %>" <%= tmp!=null && fee.feeCode.equals(tmp.feeCode)?"selected":"" %>><%=fee.feeName %></option><%
							} %>
					</select> 
     		</td>
    	</tr>
    	<tr>
			<td align="right" width="120">QQ扫码：</td>
     		<td width="250"><select name="settleFeeCode27" class="easyui-combobox" id="settleFeeCode27" validType="inputExistInCombobox['settleFeeCode27']">
						<%for(int i = 0; i<feeList.size(); i++){
							PayFeeRate fee = (PayFeeRate)feeList.get(i);
							if(!com.PayConstant.CUST_TYPE_MERCHANT.equals(fee.custType)||!"1".equals(fee.tranType))continue;
							PayFeeRate tmp = payMerchant.feeMap.get(com.PayConstant.CUST_TYPE_MERCHANT+","+payMerchant.custId+",27");
							%><option value="<%=fee.feeCode %>" <%= tmp!=null && fee.feeCode.equals(tmp.feeCode)?"selected":"" %>><%=fee.feeName %></option><%
							} %>
					</select>
     		</td>
     		<td align="right" width="120">&nbsp;</td>
     		<td>&nbsp;</td>
    	</tr>
    	<tr>
			<td align="right" width="120"><strong>代理结算费率</strong></td>
     		<td colspan="3"></td>
    	</tr>
    	<!-- 代理商费率修改 -->
    	<tr>
			<td align="right" width="120">网银借记卡：</td>
     		<td width="250"><select name="settleFeeCode19" class="easyui-combobox" id="settleFeeCode19" validType="inputExistInCombobox['settleFeeCode19']">
						<%for(int i = 0; i<feeList.size(); i++){
							PayFeeRate fee = (PayFeeRate)feeList.get(i);
							if(!com.PayConstant.CUST_TYPE_MERCHANT.equals(fee.custType)||!"1".equals(fee.tranType))continue;
							PayFeeRate tmp = payMerchant.feeMap.get(com.PayConstant.CUST_TYPE_MERCHANT+","+payMerchant.custId+",19");
							%><option value="<%=fee.feeCode %>" <%= tmp!=null && fee.feeCode.equals(tmp.feeCode)?"selected":"" %>><%=fee.feeName %></option><%
							} %>
					</select>
     		</td>
     		<td align="right" width="120">网银信用卡：</td>
     		<td><select name="settleFeeCode20" class="easyui-combobox" id="settleFeeCode20" validType="inputExistInCombobox['settleFeeCode20']">
						<%for(int i = 0; i<feeList.size(); i++){
							PayFeeRate fee = (PayFeeRate)feeList.get(i);
							if(!com.PayConstant.CUST_TYPE_MERCHANT.equals(fee.custType)||!"1".equals(fee.tranType))continue;
							PayFeeRate tmp = payMerchant.feeMap.get(com.PayConstant.CUST_TYPE_MERCHANT+","+payMerchant.custId+",20");
							%><option value="<%=fee.feeCode %>" <%= tmp!=null && fee.feeCode.equals(tmp.feeCode)?"selected":"" %>><%=fee.feeName %></option><%
							} %>
					</select> 
     		</td>
    	</tr>
    	<tr>
			<td align="right" width="120">B2B：</td>
     		<td width="250"><select name="settleFeeCode21" class="easyui-combobox" id="settleFeeCode21" validType="inputExistInCombobox['settleFeeCode21']">
						<%for(int i = 0; i<feeList.size(); i++){
							PayFeeRate fee = (PayFeeRate)feeList.get(i);
							if(!com.PayConstant.CUST_TYPE_MERCHANT.equals(fee.custType)||!"1".equals(fee.tranType))continue;
							PayFeeRate tmp = payMerchant.feeMap.get(com.PayConstant.CUST_TYPE_MERCHANT+","+payMerchant.custId+",21");
							%><option value="<%=fee.feeCode %>" <%= tmp!=null && fee.feeCode.equals(tmp.feeCode)?"selected":"" %>><%=fee.feeName %></option><%
							} %>
					</select>
     		</td>
     		<td align="right" width="120">微信：</td>
     		<td><select name="settleFeeCode22" class="easyui-combobox" id="settleFeeCode22" validType="inputExistInCombobox['settleFeeCode22']">
						<%for(int i = 0; i<feeList.size(); i++){
							PayFeeRate fee = (PayFeeRate)feeList.get(i);
							if(!com.PayConstant.CUST_TYPE_MERCHANT.equals(fee.custType)||!"1".equals(fee.tranType))continue;
							PayFeeRate tmp = payMerchant.feeMap.get(com.PayConstant.CUST_TYPE_MERCHANT+","+payMerchant.custId+",22");
							%><option value="<%=fee.feeCode %>" <%= tmp!=null && fee.feeCode.equals(tmp.feeCode)?"selected":"" %>><%=fee.feeName %></option><%
							} %>
					</select> 
     		</td>
    	</tr>
    	<tr>
			<td align="right" width="120">快捷借记卡：</td>
     		<td width="250"><select name="settleFeeCode23" class="easyui-combobox" id="settleFeeCode23" validType="inputExistInCombobox['settleFeeCode23']">
						<%for(int i = 0; i<feeList.size(); i++){
							PayFeeRate fee = (PayFeeRate)feeList.get(i);
							if(!com.PayConstant.CUST_TYPE_MERCHANT.equals(fee.custType)||!"1".equals(fee.tranType))continue;
							PayFeeRate tmp = payMerchant.feeMap.get(com.PayConstant.CUST_TYPE_MERCHANT+","+payMerchant.custId+",23");
							%><option value="<%=fee.feeCode %>" <%= tmp!=null && fee.feeCode.equals(tmp.feeCode)?"selected":"" %>><%=fee.feeName %></option><%
							} %>
					</select>
     		</td>
     		<td align="right" width="120">快捷贷记卡：</td>
     		<td><select name="settleFeeCode24" class="easyui-combobox" id="settleFeeCode24" validType="inputExistInCombobox['settleFeeCode24']">
						<%for(int i = 0; i<feeList.size(); i++){
							PayFeeRate fee = (PayFeeRate)feeList.get(i);
							if(!com.PayConstant.CUST_TYPE_MERCHANT.equals(fee.custType)||!"1".equals(fee.tranType))continue;
							PayFeeRate tmp = payMerchant.feeMap.get(com.PayConstant.CUST_TYPE_MERCHANT+","+payMerchant.custId+",24");
							%><option value="<%=fee.feeCode %>" <%= tmp!=null && fee.feeCode.equals(tmp.feeCode)?"selected":"" %>><%=fee.feeName %></option><%
							} %>
					</select>
     		</td>
    	</tr>
    	<tr>
			<td align="right" width="120">支付宝扫码：</td>
     		<td width="250"><select name="settleFeeCode25" class="easyui-combobox" id="settleFeeCode25" validType="inputExistInCombobox['settleFeeCode25']">
						<%for(int i = 0; i<feeList.size(); i++){
							PayFeeRate fee = (PayFeeRate)feeList.get(i);
							if(!com.PayConstant.CUST_TYPE_MERCHANT.equals(fee.custType)||!"1".equals(fee.tranType))continue;
							PayFeeRate tmp = payMerchant.feeMap.get(com.PayConstant.CUST_TYPE_MERCHANT+","+payMerchant.custId+",25");
							%><option value="<%=fee.feeCode %>" <%= tmp!=null && fee.feeCode.equals(tmp.feeCode)?"selected":"" %>><%=fee.feeName %></option><%
							} %>
					</select>
     		</td>
     		<td align="right" width="120">微信wap：</td>
     		<td><select name="settleFeeCode26" class="easyui-combobox" id="settleFeeCode26" validType="inputExistInCombobox['settleFeeCode26']">
						<%for(int i = 0; i<feeList.size(); i++){
							PayFeeRate fee = (PayFeeRate)feeList.get(i);
							if(!com.PayConstant.CUST_TYPE_MERCHANT.equals(fee.custType)||!"1".equals(fee.tranType))continue;
							PayFeeRate tmp = payMerchant.feeMap.get(com.PayConstant.CUST_TYPE_MERCHANT+","+payMerchant.custId+",26");
							%><option value="<%=fee.feeCode %>" <%= tmp!=null && fee.feeCode.equals(tmp.feeCode)?"selected":"" %>><%=fee.feeName %></option><%
							} %>
					</select> 
     		</td>
    	</tr>
    	<tr>
			<td align="right" width="120">QQ扫码：</td>
     		<td width="250"><select name="settleFeeCode28" class="easyui-combobox" id="settleFeeCode28" validType="inputExistInCombobox['settleFeeCode28']">
						<%for(int i = 0; i<feeList.size(); i++){
							PayFeeRate fee = (PayFeeRate)feeList.get(i);
							if(!com.PayConstant.CUST_TYPE_MERCHANT.equals(fee.custType)||!"1".equals(fee.tranType))continue;
							PayFeeRate tmp = payMerchant.feeMap.get(com.PayConstant.CUST_TYPE_MERCHANT+","+payMerchant.custId+",28");
							%><option value="<%=fee.feeCode %>" <%= tmp!=null && fee.feeCode.equals(tmp.feeCode)?"selected":"" %>><%=fee.feeName %></option><%
							} %>
					</select>
     		</td>
     		<td align="right" width="120">&nbsp;</td>
     		<td>&nbsp;</td>
    	</tr>
    	<tr>
			<td align="right" width="120"><strong>其他费率</strong></td>
     		<td colspan="3"></td>
    	</tr>
    	<tr>
    		<td align="right" width="120">充值费率：</td>
     		<td><select name="settleFeeCode2" class="easyui-combobox" id="settleFeeCode2" validType="inputExistInCombobox['settleFeeCode2']">
						<%for(int i = 0; i<feeList.size(); i++){
							PayFeeRate fee = (PayFeeRate)feeList.get(i);
							if(!com.PayConstant.CUST_TYPE_MERCHANT.equals(fee.custType)||!"2".equals(fee.tranType))continue;
							PayFeeRate tmp = payMerchant.feeMap.get(com.PayConstant.CUST_TYPE_MERCHANT+","+payMerchant.custId+",2");
							%><option value="<%=fee.feeCode %>" <%= tmp!=null && fee.feeCode.equals(tmp.feeCode)?"selected":"" %>><%=fee.feeName %></option><%
							} %>
					</select> 
     		</td>
    		<td align="right">退款费率：</td>
			<td>
				<select name="settleFeeCode4" class="easyui-combobox" id="settleFeeCode4" validType="inputExistInCombobox['settleFeeCode4']">
					<%
					for(int i = 0; i<feeList.size(); i++){
						PayFeeRate fee = (PayFeeRate)feeList.get(i); 
						if(!com.PayConstant.CUST_TYPE_MERCHANT.equals(fee.custType)||!"4".equals(fee.tranType))continue;
						PayFeeRate tmp = payMerchant.feeMap.get(com.PayConstant.CUST_TYPE_MERCHANT+","+payMerchant.custId+",4");
						%><option value="<%=fee.feeCode %>" 
						<%= tmp!=null && fee.feeCode.equals(tmp.feeCode)?"selected":"" %>><%=fee.feeName %></option><%
					}%>
				</select>
			</td>
    	</tr>
   		<tr>
   			<td align="right">提现费率：</td>
			<td>
				<select name="settleFeeCode5" class="easyui-combobox" id="settleFeeCode5" validType="inputExistInCombobox['settleFeeCode5']">
					<%
					for(int i = 0; i<feeList.size(); i++){
						PayFeeRate fee = (PayFeeRate)feeList.get(i); 
						if(!com.PayConstant.CUST_TYPE_MERCHANT.equals(fee.custType)||!"5".equals(fee.tranType))continue;
						PayFeeRate tmp = payMerchant.feeMap.get(com.PayConstant.CUST_TYPE_MERCHANT+","+payMerchant.custId+",5");
						%><option value="<%=fee.feeCode %>" <%=
							tmp!=null && fee.feeCode.equals(tmp.feeCode)?"selected":"" %>><%=fee.feeName %></option><%
					}%>
				</select>
			</td>
   			<td align="right">转账费率：</td>
			<td>
				<select name="settleFeeCode6" class="easyui-combobox" id="settleFeeCode6" validType="inputExistInCombobox['settleFeeCode6']">
					<%
					for(int i = 0; i<feeList.size(); i++){
						PayFeeRate fee = (PayFeeRate)feeList.get(i); 
						if(!com.PayConstant.CUST_TYPE_MERCHANT.equals(fee.custType)||!"6".equals(fee.tranType))continue;
						PayFeeRate tmp = payMerchant.feeMap.get(com.PayConstant.CUST_TYPE_MERCHANT+","+payMerchant.custId+",6");
						%><option value="<%=fee.feeCode %>" <%=
							tmp!=null && fee.feeCode.equals(tmp.feeCode)?"selected":"" %>><%=fee.feeName %></option><%
					}%>
				</select>
			</td>
   		</tr>
   		<tr>
   			<td align="right">商户代收费率：</td>
			<td>
				<select name="settleFeeCode15" class="easyui-combobox" id="settleFeeCode15" validType="inputExistInCombobox['settleFeeCode15']">
					<%
					for(int i = 0; i<feeList.size(); i++){
						PayFeeRate fee = (PayFeeRate)feeList.get(i); 
						if(!com.PayConstant.CUST_TYPE_MERCHANT.equals(fee.custType)||!"15".equals(fee.tranType))continue;
						PayFeeRate tmp = payMerchant.feeMap.get(com.PayConstant.CUST_TYPE_MERCHANT+","+payMerchant.custId+",15");
						%><option value="<%=fee.feeCode %>" <%=
							tmp!=null && fee.feeCode.equals(tmp.feeCode)?"selected":"" %>><%=fee.feeName %></option><%
					}%>
				</select>
			</td>
   			<td align="right">商户代付费率：</td>
			<td>
				<select name="settleFeeCode16" class="easyui-combobox" id="settleFeeCode16" validType="inputExistInCombobox['settleFeeCode16']">
					<%
					for(int i = 0; i<feeList.size(); i++){
						PayFeeRate fee = (PayFeeRate)feeList.get(i); 
						if(!com.PayConstant.CUST_TYPE_MERCHANT.equals(fee.custType)||!"16".equals(fee.tranType))continue;
						PayFeeRate tmp = payMerchant.feeMap.get(com.PayConstant.CUST_TYPE_MERCHANT+","+payMerchant.custId+",16");
						%><option value="<%=fee.feeCode %>" <%=
							tmp!=null && fee.feeCode.equals(tmp.feeCode)?"selected":"" %>><%=fee.feeName %></option><%
					}%>
				</select>
			</td>
   		</tr>
   		<tr>
   			<td align="right">代理代收费率：</td>
			<td>
				<select name="settleFeeCode29" class="easyui-combobox" id="settleFeeCode29" validType="inputExistInCombobox['settleFeeCode29']">
					<%
					for(int i = 0; i<feeList.size(); i++){
						PayFeeRate fee = (PayFeeRate)feeList.get(i); 
						if(!com.PayConstant.CUST_TYPE_MERCHANT.equals(fee.custType)||!"15".equals(fee.tranType))continue;
						PayFeeRate tmp = payMerchant.feeMap.get(com.PayConstant.CUST_TYPE_MERCHANT+","+payMerchant.custId+",29");
						%><option value="<%=fee.feeCode %>" <%=
							tmp!=null && fee.feeCode.equals(tmp.feeCode)?"selected":"" %>><%=fee.feeName %></option><%
					}%>
				</select>
			</td>
   			<td align="right">代理代付费率：</td>
			<td>
				<select name="settleFeeCode30" class="easyui-combobox" id="settleFeeCode30" validType="inputExistInCombobox['settleFeeCode30']">
					<%
					for(int i = 0; i<feeList.size(); i++){
						PayFeeRate fee = (PayFeeRate)feeList.get(i); 
						if(!com.PayConstant.CUST_TYPE_MERCHANT.equals(fee.custType)||!"16".equals(fee.tranType))continue;
						PayFeeRate tmp = payMerchant.feeMap.get(com.PayConstant.CUST_TYPE_MERCHANT+","+payMerchant.custId+",30");
						%><option value="<%=fee.feeCode %>" <%=
							tmp!=null && fee.feeCode.equals(tmp.feeCode)?"selected":"" %>><%=fee.feeName %></option><%
					}%>
				</select>
			</td>
   		</tr>
   		<tr>
   			<td align="right">代理分润比例—消费：</td>
			<td>
				<input class="easyui-numberbox" type="text" name="agentGoalDiscountRate" value="<%=payMerchant.agentGoalDiscountRate %>"
                   min="0" max="99" invalidMessage="分润比例为1-99"  data-options="required:true" style="width:30px"/>%（0-99）
			</td>
			<td align="right">代理分润比例—代付：</td>
			<td>
				<input class="easyui-numberbox" type="text" name="agentPayRate" value="<%=payMerchant.agentPayRate %>"
                   min="0" max="99" invalidMessage="分润比例为1-99"  data-options="required:true" style="width:30px"/>%（0-99）
			</td>
   		</tr>
   		<tr>
   			<td align="right">税金率：</td>
			<td>
				<input class="easyui-numberbox" type="text" name="agentTaxRate" value="<%=payMerchant.agentTaxRate %>"
                   min="0" max="99" invalidMessage="税金率为1-99"  data-options="required:true" style="width:30px"/>%（0-99）
			</td> 
			<td align="right">&nbsp;</td>
			<td>&nbsp;</td>
   		</tr>
 	</table>
 	<div class="merchant-lable">联系信息</div>
	<hr color="#ccc">
 	<table cellpadding="5" style="margin-left:20px;" width="100%">
		<tr>
			<td align="right" width="120">客服联系人：</td>
   			<td width="250"><input class="easyui-textbox" type="text" id="updatePayMentServContact" name="servContact" missingMessage="请输入客服联系人"
   				data-options="required:true" validType="length[1,20]" invalidMessage="客服联系人为1-20位字符"
	            value="<%=payMerchant.servContact==null?"":payMerchant.servContact %>" style="width:200px"/>
   			</td>
   			<td align="right" width="120">技术联系人：</td>
   			<td><input class="easyui-textbox" type="text" id="updatePayMentTechContact" name="techContact" missingMessage="请输入技术联系人"
   				data-options="required:true" validType="length[1,20]" invalidMessage="技术联系人为1-20位字符"
	            value="<%=payMerchant.techContact==null?"":payMerchant.techContact %>" style="width:200px"/>
   			</td>
   		</tr>
    	<tr>
    		<td align="right" width="120">客服电话：</td>
   			<td><input class="easyui-textbox" type="text" id="updatePayMentServTelNo" name="servTelNo" missingMessage="请输入客服电话"
   				data-options="required:true" validType="length[1,20]" invalidMessage="客服联系电话为1-20位字符"
	            value="<%=payMerchant.servTelNo==null?"":payMerchant.servTelNo %>" style="width:200px"/>
   			</td>
   			<td align="right">技术联系人电话：</td>
   			<td><input class="easyui-textbox" type="text" id="updatePayMentTechTelNo" name="techTelNo" missingMessage="请输入技术联系人电话"
   				data-options="required:true" validType="length[1,20]" invalidMessage="技术联系电话为1-20位字符"
	            value="<%=payMerchant.techTelNo==null?"":payMerchant.techTelNo %>" style="width:200px"/>
   			</td>
   		</tr>
    	<tr>
   			<td align="right">客服邮箱：</td>
   			<td><input class="easyui-textbox" type="text" id="updatePayMentServEmail" name="servEmail"
	                        validType="email" invalidMessage="请输入正确邮箱，最多50位字符"
	                        value="<%=payMerchant.servEmail==null?"":payMerchant.servEmail %>" style="width:200px"/>
   			</td>
   			<td align="right">技术联系人邮箱：</td>
   			<td><input class="easyui-textbox" type="text" id="updatePayMentTechEmail" name="techEmail"
                	validType="email" invalidMessage="请输入正确邮箱，最多50位字符"
                    value="<%=payMerchant.techEmail==null?"":payMerchant.techEmail %>" style="width:200px"/>
    		</td>
   		</tr>
 	</table>
 	<div class="merchant-lable">企业认证<font color="#f00" size="2">（除“法人身份证反面”外，其他证件修改后需要重审）</font></div>
	<hr color="#ccc">
	<table id="fileuploadFlag" style="margin-left:20px;" width="100%">
		<tr>
			<td colspan="3">
				<div style="color:red;" class="unit">
					&nbsp;&nbsp;&nbsp;上传文件格式为(zip,rar)
				</div>
			</td>
		</tr>
		<tr><td>&nbsp;</td><td>&nbsp;</td><td><br/><input type="file" name="uploadFile" id="fileUploadForUpdateMerchant" value="<%=payMerchant.contractPic%>"></td></tr>
	</table>
	<div class="merchant-lable">电子证书</div>
	<hr color="#ccc">
	<table style="margin-left:20px;">
		<tr>
			<td><textarea rows="10" cols="80" id="cfca_cert" name="cert"><%=payMerchant.cert==null?"":payMerchant.cert %></textarea></td>
		</tr>
	</table>
	</form>
    <%} %>
</div>
<div data-options="region:'south',border:false" style="text-align:center;padding:5px;">
    <a class="easyui-linkbutton" data-options="iconCls:'icon-ok'" href="javascript:updatePayMerchantFormSubmit()" style="width:80px">确定</a>
    <a class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" href="javascript:void(0)" 
        onclick="$('#mainTabs').tabs('close',payMerchantUpdatePageTitle)" style="width:80px">取消</a>
</div>
</div>
<script type="text/javascript">
function updatePayMerchantFormSubmit(){
try{
    var val1 = $('#payWayJiejiForUpdate').is(':checked') == true ? 0 : 1;
	var val2 = $('#payWayXinyongForUpdate').is(':checked') == true ? 0 : 1;
	var val3 = $('#payWayB2bForUpdate').is(':checked') == true ? 0 : 1;
	var val4 = $('#payWayKuaijieForUpdate').is(':checked') == true ? 0 : 1;
	var val5 = $('#payWayKuaijieDJForUpdate').is(':checked') == true ? 0 : 1;
	var val6 = $('#payWayDaiShouForUpdate').is(':checked') == true ? 0 : 1;
	var val7 = $('#payWayDaiFuForUpdate').is(':checked') == true ? 0 : 1;
	var val8 = $('#payWayQybForUpdate').is(':checked') == true ? 0 : 1;
	var val9 = $('#payWayWXScanForUpdate').is(':checked') == true ? 0 : 1;
	var val10 = $('#payWayWXWapForUpdate').is(':checked') == true ? 0 : 1;
	var val11 = $('#payWayZFBScanForUpdate').is(':checked') == true ? 0 : 1;
	var val12 = $('#payWayQQScanForUpdate').is(':checked') == true ? 0 : 1;
	$('#payWaySupportedIdForUpdate').val(val1 + '' + val2  + '' + val3  + '' + val4+ '' + val6+ '' + val7+ '' + val8+ '' + val5+ '' + val9+ '' + val10+ '' + val11+''+val12);
	if('' == $.trim($('#uploadFileUrlId').val())){
		topCenterMessage('<%=JWebConstant.ERROR %>','请上传企业认证资料');
		return;
	}
	var busiLicenceBeginDate= new Date(Date.parse($('#updatePayMentBusinessLicenceBeginDate').datebox('getValue').replace(/-/g,  "/")));   
	var busiLicenceEndDate= new Date(Date.parse($('#updatePayMentBusinessLicenceEndDate').datebox('getValue').replace(/-/g,  "/")));   
	var now = new Date(new Date().getFullYear(),new Date().getMonth(),new Date().getDate()); 
	if((busiLicenceEndDate <= now) || (busiLicenceBeginDate >= busiLicenceEndDate)){
		topCenterMessage('<%=JWebConstant.ERROR %>','营业执照经营期限不合法');
		return;
	} 
	$('#updatePayMerchantForm').submit();
}catch(e){alert(e);}
}
addressInit('update_province', 'update_city', 'update_area','<%=payMerchant.province %>','<%=payMerchant.city %>','<%=payMerchant.region %>');
$('#updatePayMerchantForm').form({
    url:'<%=path %>/updatePayMerchant.htm',
    onSubmit: function(){
    	try{
        if($(this).form('validate') && checkUpdateMerchantAddress() 
	        	&& checkUpdateMerchantSelTime() && checkUpdateMerchantSelTimeAgent() && checkUpdateMerchantSelTimeDaishou())return true;
	    }catch(e){alert(e);}
	    return false;
    },
    success:function(data){
        $('#payMerchantList').datagrid('reload');
        if(data=='<%=JWebConstant.OK %>'){
            topCenterMessage('<%=JWebConstant.OK %>','修改成功！');
            closeTabFreshList('payMerchant',payMerchantUpdatePageTitle,payMerchantListPageTitle,'payMerchantList','<%=path %>/payMerchant.htm');
        } else topCenterMessage('<%=JWebConstant.ERROR %>',data);
    }
});
function getPerIodUpdate(){
	try{
    var perIod = document.getElementById("updateMerchantCustSetPeriod").value;  
    if(perIod == 'D'){
		$("#updateMerchantStlnam").hide();
		$("#updateMerchantCustStlWeekSetType").hide();
		$("#updateMerchantCustStlTimeSetType").hide();
		$("#updateMerchantStlMonth").hide();
		$("#updateMerchantDayStl").show();
    }else if(perIod == 'W'){
       	$("#updateMerchantStlnam").show();
       	$("#updateMerchantCustStlWeekSetType").show();
       	$("#updateMerchantCustStlTimeSetType").hide();
       	$("#updateMerchantStlMonth").hide(); 
		$("#updateMerchantDayStl").hide();
    }else if(perIod == 'M'){
       	$("#updateMerchantStlnam").show();
       	$("#updateMerchantCustStlWeekSetType").hide();
       	$("#updateMerchantCustStlTimeSetType").hide();
       	$("#updateMerchantStlMonth").show(); 
		$("#updateMerchantDayStl").hide();
    }
    }catch(e){alert(e);}
}
function getPerIodAgentUpdate(){
	var perIodAgent = document.getElementById("updateMerchantCustSetPeriodAgent").value; 
	if(perIodAgent == 'D'){
		$("#updateMerchantStlnamAgent").hide();
		$("#updateMerchantCustStlWeekSetTypeAgent").hide();
		$("#updateMerchantStlMonthAgent").hide();
		$("#updatePayMentCustSetFreyAgent").removeAttr("disabled");
		$("#updateMerchantDayStlAgent").show();
    }else if(perIodAgent == 'W'){
       	$("#updateMerchantStlnamAgent").show();
       	$("#updateMerchantCustStlWeekSetTypeAgent").show();
       	$("#updateMerchantStlMonthAgent").hide(); 
		$("#updateMerchantDayStlAgent").hide();
    }else if(perIodAgent == 'M'){
       	$("#updateMerchantStlnamAgent").show();
       	$("#updateMerchantCustStlWeekSetTypeAgent").hide();
       	$("#updateMerchantStlMonthAgent").show(); 
		$("#updateMerchantDayStlAgent").hide();
    }
}
function getPerIodDaishouUpdate(){
	var perIodDaishouUpdate = document.getElementById("updateMerchantCustSetPeriodDaishou").value; 
	if(perIodDaishouUpdate == 'D'){
		$("#updateMerchantStlnamDaishou").hide();
		$("#updateMerchantCustStlWeekSetTypeDaishou").hide();
		$("#updateMerchantStlMonthDaishou").hide();
		$("#updateMerchantCustStlMonthSetTypeDaishou").hide();
		$("#updatePayMentCustSetFreyDaishou").removeAttr("disabled");
		$("#updateMerchantDayStlDaishou").show();
    }else if(perIodDaishouUpdate == 'W'){
       	$("#updateMerchantStlnamDaishou").show();
       	$("#updateMerchantCustStlWeekSetTypeDaishou").show();
       	$("#updateMerchantStlMonthDaishou").hide(); 
       	$("#updateMerchantCustStlMonthSetTypeDaishou").hide(); 
       	$("#updateMerchantDayStlDaishou").hide();
       	$("#updatePayMentCustSetFreyDaishou").attr("disabled","disabled");
    }else if(perIodDaishouUpdate == 'M'){
       	$("#updateMerchantDayStlDaishou").hide();
       	$("#updatePayMentCustSetFreyDaishou").attr("disabled","disabled");
       	$("#updateMerchantStlnamDaishou").hide();
       	$("#updateMerchantCustStlWeekSetTypeDaishou").hide();
       	$("#updateMerchantStlMonthDaishou").show(); 
		$("#updateMerchantCustStlMonthSetTypeDaishou").show();
    }
}

function checkUpdateMerchantAddress(){
	try{
	if(document.getElementById("update_province").value == "省"){
		topCenterMessage("<%=JWebConstant.ERROR %>","请选择省市区/县");
		return false;
	}
	}catch(e){alert(e);}
	return true;
}
// 验证结算时间点	
function checkUpdateMerchantSelTime(){
	try{
	var custSetPeriod = $("#updateMerchantCustSetPeriod").val();
	if(custSetPeriod == 'M'){
		var len = $("input[name='custStlMonthSet']:checked").length;
		if(len==0){
			topCenterMessage("<%=JWebConstant.ERROR %>","请勾选结算时间点设置!");
			return false;
		}
	}
	else if(custSetPeriod == 'W'){
		var len = $("input[name='custStlWeekSet']:checked").length;
		if(len==0){
			topCenterMessage("<%=JWebConstant.ERROR %>","请勾选结算时间点设置!");
			return false;
		}
	}
	}catch(e){alert(e);}
	return true;
}
try{
	getPerIodUpdate();
}catch(e){alert(e);}
// 验证代理结算时间点	
function checkUpdateMerchantSelTimeAgent(){
	try{
	var custSetPeriodAgent = $("#updateMerchantCustSetPeriodAgent").val();
	if(custSetPeriodAgent == 'M'){
		var len = $("input[name='custStlMonthSetAgent']:checked").length;
		if(len==0){
			topCenterMessage("<%=JWebConstant.ERROR %>","请勾选代理结算时间点设置!");
			return false;
		}
	}
	else if(custSetPeriodAgent == 'W'){
		var len = $("input[name='custStlWeekSetAgent']:checked").length;
		if(len==0){
			topCenterMessage("<%=JWebConstant.ERROR %>","请勾选代理结算时间点设置!");
			return false;
		}
	}
	}catch(e){alert(e);}
	return true;
}
try{
	getPerIodAgentUpdate();
}catch(e){alert(e);}
// 验证代收结算时间点	
function checkUpdateMerchantSelTimeDaishou(){
	try{
	var custSetPeriodDaishou = $("#updateMerchantCustSetPeriodDaishou").val();
	if(custSetPeriodDaishou == 'M'){
		var len = $("input[name='custStlMonthSetDaishou']:checked").length;
		if(len==0){
			topCenterMessage("<%=JWebConstant.ERROR %>","请勾选代收结算时间点设置!");
			return false;
		}
	}
	else if(custSetPeriodDaishou == 'W'){
		var len = $("input[name='custStlWeekSetDaishou']:checked").length;
		if(len==0){
			topCenterMessage("<%=JWebConstant.ERROR %>","请勾选代收结算时间点设置!");
			return false;
		}
	}
	}catch(e){alert(e);}
	return true;
}
try{
	getPerIodAgentUpdate();
}catch(e){alert(e);}

$(function(){
	$.extend($.fn.textbox.defaults.rules, {
		checkPayMentParentId: {
	  		validator: function (value, param) {
	  			if(value.length<7 || value.length>20){
	   			$.fn.textbox.defaults.rules.checkPayMentParentId.message = '商家号为7-20位字符';
	   			return false;
	   		} else {
	   			var result = $.ajax({
					url: '<%=path %>/validMerchantId.htm',
					data:{parentId:value},
					type: 'post',
					dataType: 'json',//发送类型
					async: false,
					cache: false
				}).responseText;//接收类型
				if(result == '1')return true;
				$.fn.textbox.defaults.rules.checkPayMentParentId.message = result;
				return false; 
	   		}
	  		},
		message: ''
	  },//校验业务员是否存在
		checkPayMentRelationUserId: {
	  		validator: function (value, param) {
	  			if(value.length<3 || value.length>20){
		   			$.fn.textbox.defaults.rules.checkPayMentRelationUserId.message = '业务员编号为3-20位字符';
		   			return false;
		   		} else {
		   			var result = $.ajax({
						url: '<%=path %>/validPayMentRelationUserId.htm',
						data:{userId:value},
						type: 'post',
						dataType: 'json',//发送类型
						async: false,
						cache: false
					}).responseText;//接收类型
					if(result == '1')return true;
					$.fn.textbox.defaults.rules.checkPayMentRelationUserId.message = result;
					return false; 
		   		}
	  		},
		message: ''
	  }
	});
});
</script>
<%}catch(Exception e){e.printStackTrace();}%>