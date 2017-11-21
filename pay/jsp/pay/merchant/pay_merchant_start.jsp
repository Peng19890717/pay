<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ page import="util.JWebConstant"%>
<%@ page import="com.jweb.dao.JWebUser"%>
<%@ page import="com.jweb.dao.JWebAction"%>
<%@ page import="com.jweb.service.JWebUserService"%>
<%@ page import="com.pay.fee.service.PayFeeRateService"%>
<%@ page import="com.pay.merchant.service.PayMerchantService"%>
<%@ page import="com.pay.fee.dao.PayFeeRate"%>
<%@ page import="com.PayConstant"%>
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
java.util.List feeList = new PayFeeRateService().getAllPayFeeRate();
String custId = new PayMerchantService().getMerchantNo();
%>
<title>商户开户</title>
<style type="text/css">
	.stl{
		float:left;margin-right:110px;
	}
</style>
<link rel="stylesheet" type="text/css" href="<%=path %>/js/upload/uploadify.css"  />
<script type="text/javascript" src="<%=path %>/js/upload/jquery.uploadify.min.js"></script>
<script type="text/javascript" src="<%=path %>/js/jquery.form.js"></script>

<script type="text/javascript">
	$(function(){
		$('#fileUploadForAddMerchant').change(function(){
			var fileName = $('#fileUploadForAddMerchant').val();
			var fileExtension = fileName.substring(fileName.lastIndexOf('.'));
			if(fileExtension != '.zip' && fileExtension != '.rar') {
				topCenterMessage('<%=JWebConstant.ERROR%>','对不起，上传文件格式为(zip,rar)');
				return ;			
			}
			var options = {
				url:'<%=path %>/fileUploadForAddMerchant.htm',
				type : 'post',
				dataType : 'json',
				success : function(data) {
					$('#uploadFileUrlId').val(data.saveUrl);
				}
			};
			$('#payMerchantAddForm').ajaxSubmit(options);
		});
	});
</script>

<div class="easyui-layout" data-options="fit:true">
    <div data-options="region:'center'">
	<form method="post" action="<%=path %>/addPayMerchant.htm" class="pageForm required-validate" style="height:auto;" id="payMerchantAddForm" enctype="multipart/form-data">
	<input type="hidden"  name="merStatus" value="1" />
	<input type="hidden"  name="codeTypeId" value="CA" />
	<input type="hidden"  name="custStlType" value="00" />
	<input type="hidden"  name="minStlBalance" value="0"/>
	<input type="hidden"  name="postCode" value=""/>
	<input type="hidden"  name="compayEmail" value=""/>
	<input type="hidden"  name="riskLevel" value="0"/>
	<input type="hidden" name="contractPic" id="uploadFileUrlId">
	<div class="merchant-lable">基本信息</div>
	<hr color="#ccc">
    <table cellpadding="5" style="margin-left:20px;" width="100%">
		<tr>
			<td align="right" width="120">商户编号：</td>
     		<td width="250"><input class="easyui-textbox" type="text" id="addPayMentCustId" name="custId"
					data-options="readonly:true,required:true" value="<%=custId %>" style="width:200px"/>
     		</td>
     		<td align="right" width="120">登记时间：</td>
     		<td><input class="easyui-textbox" type="text" id="addPayMentCreateTime" name="createTime"
     			data-options="readonly:true,required:true" value=<%=new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date()) %> style="width:200px"/>
     		</td>
    	</tr>
    	<tr>
    		<td align="right">商户类型：</td>
    		<td><select name="merType" id="addPayMentMerType" style="width:200px">
			   		<option value="0">一般商户 </option>
			   		<option value="1">平台商户 </option>	
			   		<option value="2">担保商户 </option>	 					  
		 		</select>
		 	</td>
    		<td align="right">登记人员：</td>
    		<td><input class="easyui-textbox" type="text" id="addPayMentCreateUser" name="createUser"
    			data-options="readonly:true,required:true" value="<%=user.name %>" style="width:200px"/>
			</td>
    	</tr>
   		<tr>
   			<td align="right">商户操作员账号：</td>
   			<td><input class="easyui-textbox" type="text" id="addPayMentMerchantOperatorId" name="merchantOperatorId" missingMessage="请输入商户操作员账号"
                        invalidMessage="操作员账号为8-16位数字、字母组合" data-options="required:true" value="<%=custId+"admin" %>" style="width:200px"/>
   			</td>
   			<td align="right">商户对接IP（生产）</td>
   			<td>
   				<input class="easyui-textbox" type="text" id="addPayMentInterfaceIp" name="interfaceIp" missingMessage="请输入商户IP"
                        validType="IPS[]" invalidMessage="IP错误" data-options="required:true" value="127.0.0.1" style="width:200px"/>（127.0.0.1为无限制,多个IP以“;”分割）
   			</td>
   		</tr>
   		<tr>
   			<td align="right">上级代理编号：</td>
   			<td>
   				<input class="easyui-textbox" type="text" id="addPayMentParentId" name="parentId" missingMessage="请输入上级代理编号"
                        validType="checkPayMentParentId[0]" style="width:150px"/>
   			</td>
   			<td align="right">业务员编号：</td>
        	<td>
        		<input class="easyui-textbox" type="text" id="addPayMentRelationUserId" name="userId" missingMessage="请输入业务员编号"
                    validType="checkPayMentRelationUserId[1]"/>
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
							<option value="<%= key%>"><%= map.get(key) %></option>
						<%
				 	}
   				 %>
   				</select>
   			</td>
   		</tr>
    </table>
   	<div class="merchant-lable">经营信息</div>
   	<hr color="#ccc">
  	<table cellpadding="5" style="margin-left:20px;" width="100%">
		<tr>
			<td align="right" width="120">商户简称：</td>
   			<td width="250"><input class="easyui-textbox" type="text" id="addPayMentStoreShortname" name="storeShortname" missingMessage="请输入商户简称"
                       validType="length[1,50]" invalidMessage="商户简称为1-50个字符" data-options="required:true" style="width:200px"/>
   			</td>
   			<td align="right" width="120">注册地址：</td>
   			<td>
   				<select name="province" id="add_province"></select>
				<select name="city" id="add_city"></select>
				<select name="region" id="add_area"></select>
				<input class="easyui-textbox" type="text" id="addPayMentMerAddr" name="merAddr" missingMessage="请输入详细地址"
                        validType="length[1,30]" invalidMessage="详细地址为1-30个字符" data-options="required:true" style="width:200px"/>
   			</td>
   		</tr>
    		<tr>
    		<td align="right" width="120">商户名称：</td>
   			<td><input class="easyui-textbox" type="text" id="addPayMentStoreName" name="storeName" missingMessage="请输入商户名称"
                       validType="length[1,50]" invalidMessage="商户名称为1-50个字符" data-options="required:true" style="width:200px"/>
   			</td>
   			<td align="right">公司网站：</td>
    			<td><input class="easyui-textbox" type="text" id="addPayMentWebAddress" name="webAddress" missingMessage="请输入商户网址"
                        validType="length[7,100]" invalidMessage="商户网址为7-100位字符" value="http://" data-options="required:true" style="width:200px"/>
    		</td>
   		</tr>
    		<tr>
	   			<td align="right">公司电话：</td>
	   			<td><input class="easyui-textbox" type="text" id="addPayMentCompayTelNo" name="compayTelNo" data-options="required:true"
	                        validType="length[1,20]" invalidMessage="公司电话为1-20位字符" style="width:200px"/>
	   			</td>
	   			<td align="right">公司传真：</td>
	   			<td>
	   				<input class="easyui-textbox" type="text" id="addPayMentCompayFax" name="compayFax"  missingMessage="请输入公司传真"
	                	validType="length[1,20]" invalidMessage="公司传真为1-20位字符" style="width:200px"/>
	   			</td>
   		</tr>
	 	<tr>
   			<td align="right">ICP备案号：</td>
   			<td><input class="easyui-textbox" type="text" id="addPayMentIcpNo" name="icpNo" missingMessage="请输入ICP备案号"
                	validType="length[1,20]" invalidMessage="ICP备案号为1-20位字符"  data-options="required:true" style="width:200px"/>
   			</td>
   			<td align="right">ICP证件号：</td>
   			<td>
                <input class="easyui-textbox" type="text" id="addPayMentIcpCertNo" name="icpCertNo"
                	validType="length[1,20]" invalidMessage="ICP备案号为1-20位字符" style="width:200px"/>
   			</td>
   		</tr>
   		<tr>
   			<td align="right">税务登记证号：</td>
   			<td><input class="easyui-textbox" type="text" id="addPayMentTaxRegistrationNo" name="taxRegistrationNo" missingMessage="请输入税务登记证号"
                        validType="length[1,20]" invalidMessage="税务登记证号为1-20位字符"  data-options="required:true" style="width:200px"/>
   			</td>
   			<td align="right">组织机构代码：</td>
   			<td><input class="easyui-textbox" type="text" id="addPayMentOrganizationNo" name="organizationNo" missingMessage="请输入组织机构代码"
                        validType="length[1,20]" invalidMessage="组织机构代码为1-20位字符"  data-options="required:true" style="width:200px"/>
   			</td>
   		</tr>
   		<tr>
   			<td align="right">注册资本：</td>
   			<td><input class="easyui-numberbox" type="text" id="addPayMentRegCapital" name="regCapital" missingMessage="请输入注册资本"
                        validType="length[1,15]" invalidMessage="注册资本为1-15位数字"  data-options="required:true" style="width:200px"/>
   			</td>
   			<td align="right">营业执照注册号：</td>
   			<td><input class="easyui-textbox" type="text" id="addPayMentBusinessLicenceNo" name="businessLicenceNo" missingMessage="请输入营业执照注册号"
                	validType="length[1,50]" invalidMessage="营业执照注册号为1-50位字符" data-options="required:true" style="width:200px"/></td>
   		</tr>
	 	<tr>
	 		<td rowspan="2" align="right" valign="top">营业执照经营范围：</td>
   			<td rowspan="2">
                 <input class="easyui-textbox" type="text" id="addPayMentBizRange" name="bizRange" data-options="required:true,multiline:true"
                 	validType="length[0,200]" invalidMessage="营业执照经营范围内容请控制在200个字符以内" style="width:200px;height:50px"/>
   			</td>
   			<td align="right" valign="top">营业执照经营期限：</td>
   			<td valign="top"><input type="text" id="addPayMentBusinessLicenceBeginDate" name="businessLicenceBeginDate" class="easyui-datebox"
   					data-options="editable:false,required:true"  missingMessage="请选择营业执照经营开始期限" style="width:95px"/>~
    			<input type="text" id="addPayMentBusinessLicenceEndDate" name="businessLicenceEndDate" class="easyui-datebox"
					data-options="editable:false,required:true"  missingMessage="请选择营业执照经营结束期限" style="width:95px"/></td>
   		</tr>
   		<tr>
   			<td align="right" valign="top">支持的业务类型：</td>
   			<td valign="top">
   			<input type="checkbox" id="payWayJieji"/>网银借记卡支付&nbsp;&nbsp;
   			<input type="checkbox" id="payWayXinyong"/>网银贷记卡支付&nbsp;&nbsp;
   			<input type="checkbox" id="payWayB2b"/>B2B支付&nbsp;&nbsp;
   			<input type="checkbox" id="payWayKuaijie"/>快捷借记卡支付&nbsp;&nbsp;
   			<input type="checkbox" id="payWayKuaijieDJ"/>快捷贷记卡支付<br/>
   			<input type="checkbox" id="payWayDaishou"/>代收&nbsp;&nbsp;
   			<input type="checkbox" id="payWayDaifu"/>代付
   			<input type="checkbox" id="payWayQyb"/>企业宝
   			<input type="checkbox" id="payWayWXScan"/>微信扫码
   			<input type="checkbox" id="payWayWXWap"/>微信WAP
   			<input type="checkbox" id="payWayZFBScan"/>支付宝扫码
   			<input type="checkbox" id="payWayQQScan"/>QQ扫码
   			<input type="hidden"   id="payWaySupportedId" name="payWaySupported"/>
   			</td>
   		</tr>
   		<tr>
    		<td align="right">用户是否互通：</td>
    		<td><select name="userInterflowFlag" id="addPayMentUserInterflowFlag" style="width:200px">
			   		<option value="0">否 </option>	
			   		<option value="1">是 </option>
		 		</select>
		 	</td>
    	</tr>
 	</table>
	<div class="merchant-lable">企业法人/经办人信息</div>
	<hr color="#ccc">
	<table cellpadding="5" style="margin-left:20px;" width="100%">
		<tr>
			<td align="right" width="120">法人姓名：</td>
   			<td width="250"><input type="hidden" value="1" name="lawPersonType"/>
     				<input class="easyui-textbox" type="text" id="addPayMentMerLawPerson" name="merLawPerson" missingMessage="请输入法人姓名"
                        validType="length[1,20]" invalidMessage="法人姓名为1-20位字符" value="" data-options="required:true" style="width:200px"/>
   			</td>
   			<td align="right" width="120">经办人：</td>
    		<td><input class="easyui-textbox" type="text" id="addPayMentBizContact" name="bizContact" missingMessage="请输入经办人"
                        validType="length[1,20]" invalidMessage="经办人为1-20位字符"  data-options="required:true" style="width:200px"/>
    		</td>
   		</tr>
    		<tr>
    		<td align="right" width="120">法人证件类型：</td>
   			<td><select name="lawPersonCretType" id="addPayMentLawPersonCretTypeId" style="width:200px">
   				<%	
           			java.util.Iterator<String> it = com.PayConstant.CERT_TYPE.keySet().iterator();
           			while(it.hasNext()){
           				String key = it.next();
           				String value = com.PayConstant.CERT_TYPE.get(key);
           				%><option value="<%=key%>" <%="01".equals(key)?"selected":"" %>><%=value%></option>
           			<%}
           		%>					
   			</td>
   			<td align="right">经办人手机：</td>
    		<td><input class="easyui-textbox" type="text" id="updateattentionLineTel" name="attentionLineTel" missingMessage="请输入经办人联系电话"
                       validType="mobile[]" invalidMessage="请输入正确手机号码（11位）" data-options="required:true"
                       value="" style="width:200px"/>
				 	接收商户账号信息
    		</td>
   		</tr>
    	<tr>
   			<td align="right">法人证件号码：</td>
   			<td><input class="easyui-textbox" type="text" id="addPayMentLawPersonCretNo" name="lawPersonCretNo" missingMessage="请输入法人代表证件号码"
                       validType="idcard['#addPayMentLawPersonCretTypeId']" invalidMessage=""  data-options="required:true" style="width:200px"/>
   			</td>
   			<td align="right">经办人邮箱：</td>
   			<td><input class="easyui-textbox" type="text" id="addPayMentattentionLineEmail" name="attentionLineEmail"  missingMessage="请输入经办人邮箱"
            	validType="email" invalidMessage="请输入正确邮箱，最多50位字符" data-options="required:true" style="width:200px"/>
				 	接收商户账号信息
   			</td>
   		</tr>
 	</table>
 	<div class="merchant-lable">结算信息</div>
	<hr color="#ccc">
	<table cellpadding="5" style="margin-left:20px;" width="100%">
		<tr>
			<td align="right" width="120">开户行姓名：</td>
   			<td width="250"><input class="easyui-textbox" type="text" id="addPayMentCustBankDepositName" name="custBankDepositName"  missingMessage="请输入商户银行开户姓名"
            	validType="length[1,50]" invalidMessage="商户银行开户姓名为1-50位字符" data-options="required:true" style="width:200px"/>
   			</td>
   			<td align="right" width="120">商户开户银行编码：</td>
   			<td>
            	<select name="depositBankCode" style="width:200px">
            		<%
            		for(int i=0; i<PayCardBinService.BANK_CODE_NAME_LIST.size(); i++){
            			PayBank bank = PayCardBinService.BANK_CODE_NAME_LIST.get(i);
            		 	%><option value="<%=bank.bankCode %>"><%=bank.bankName %></option><%
            		}%>
				</select>
   			</td>
   		</tr>
    	<tr>
    		<td align="right" width="120">开户行网点名称：</td>
   			<td><input class="easyui-textbox" type="text" id="addPayMentDepositBankBrchName" name="depositBankBrchName"  missingMessage="请输入开户银行网点名称"
            	validType="length[1,50]" invalidMessage="开户银行网点名称为1-50位字符" data-options="required:true" style="width:200px"/>
   			</td>
   			<td align="right">商户结算银行账号：</td>
    		<td><input class="easyui-textbox" type="text" id="addPayMentCustStlBankAcNo" name="custStlBankAcNo"  missingMessage="请输入商户结算银行账号"
                        	validType="length[1,30]" invalidMessage="商户结算银行账号为1-30位字符" data-options="required:true" style="width:200px"/>
    		</td>
   		</tr>
   		<tr>
   			<td align="right" width="120">结算账户开户行行号：</td>
   			<td><input class="easyui-textbox" type="text" id="addPayMentcustStlBankNumber" name="custStlBankNumber" validType="length[1,50]" style="width:200px"/>
   			</td>
   			<td align="right"></td>
    		<td></td>
   		</tr>
   		<tr>
    		<td align="right" width="120">结算账号开户省份：</td>
   			<td><input class="easyui-textbox" type="text" id="addPayMentcustStlBankProvince" name="custStlBankProvince" validType="length[1,50]" invalidMessage="开户省份" style="width:200px"/>
   			</td>
   			<td align="right">结算账号开户城市：</td>
    		<td><input class="easyui-textbox" type="text" id="addPayMentcustStlBankCity" name="custStlBankCity" validType="length[1,30]" style="width:200px"/>
    		</td>
   		</tr>
   		<tr>
    		<td align="right" width="120">结算账号开户身份证号：</td>
   			<td><input class="easyui-textbox" type="text" id="addPayMentcustStlIdno" name="custStlIdno" validType="length[15,18]" invalidMessage="请输入15或18位身份证号" style="width:200px"/>
   			</td>
   			<td align="right">结算账号开户手机号：</td>
    		<td><input class="easyui-textbox" type="text" id="addPayMentcustStlMobileno" name="custStlMobileno" validType="length[11,11]" invalidMessage="请输入11位手机号" style="width:200px"/>
    		</td>
   		</tr>
   		
    	<tr>
   			<td align="right" valign="top">交易结算周期：</td>
   			<td valign="top">
   			<select name="custSetPeriod" id="custSetPeriod" onchange="javascript:getPerIod()" style="width:200px">
				<option value="D" >按天</option>
				<option value="W" >按周</option>
				<option value="M" >按月</option>
			</select>
   			</td>
   			<td align="right" valign="top">交易结算时间点设置：</td>
   			<td valign="top">
   			  <div class="unit" id="dayStlAdd" >
	                <select name="custSetFrey" id="custSetFreyAdd" class="easyui-combobox"  data-options="required:true" style="width:200px">
                        <%for(int i=0; i<=30; i++){ 
	                		%><option value="<%=i %>" <%=i==1?"selected":"" %>>T+<%=i %></option><%
	                	} %>
                    </select>
                    <font color="#f00">（T+0时，实时结算到虚拟账户）</font>
			  </div>
			  <div class="unit" id="stlnamAdd"  style="display:none">
				<div id="custStlWeekSetTypeAdd">
				   <% String [] week = {"星期一","星期二","星期三","星期四","星期五","星期六","星期日"};
					for(int i = 0; i<7; i++){ 
						%><input type="checkbox" name="custStlWeekSet"  value="<%=i+1 %>"/><%=week[i] %><%
					} %>
				   <font color="red"><b>*</b></font>
				</div>
			  </div>
			  <div class="unit" id="stlMonthAdd"  style="display:none">
				<div id="custStlMonthSetTypeAdd">
				   <%for(int i = 1; i<=28; i++){ 
						%><input type="radio" name="custStlMonthSet" <%=i==1?"checked":"" %>  value="<%=i %>"/><%=i %><%=i%15==0?"<br/>":"" %><%
					} %>
				</div>
			  </div>
			</td>
   		</tr>
   		<tr>
   			<td align="right" valign="top">代理结算周期：</td>
   			<td valign="top">
   			<select name="custSetPeriodAgent" id="custSetPeriodAgent" onchange="javascript:getPerIodAgent()" style="width:200px">
				<option value="D" >按天</option>
				<option value="W" >按周</option>
				<option value="M" >按月</option>
			</select>
   			</td>
   			<td align="right" valign="top">代理结算时间点设置：</td>
   			<td valign="top">
   				<div class="unit" id="dayStlAgentAdd" >
	                <select name="custSetFreyAgent" id="custSetFreyAgentAdd" class="easyui-combobox"  data-options="required:true" style="width:200px">
                        <%for(int i=1; i<=30; i++){ 
	                		%><option value="<%=i %>" <%=i==7?"selected":"" %>>T+<%=i %></option><%
	                	} %>
                    </select>
				</div>
				<div class="unit" id="stlnamAddAgent"  style="display:none">
				<div id="custStlWeekSetTypeAgentAdd">
				   <% String [] weekAgent = {"星期一","星期二","星期三","星期四","星期五","星期六","星期日"};
					for(int i = 0; i<7; i++){ 
						%><input type="checkbox" name="custStlWeekSetAgent"  value="<%=i+1 %>"/><%=weekAgent[i] %><%
					} %>
				   <font color="red"><b>*</b></font>
				</div>
			  </div>
			  <div class="unit" id="stlMonthAgentAdd"  style="display:none">
				<div id="custStlMonthSetTypeAgentAdd">
				   <%for(int i = 1; i<=28; i++){ 
						%><input type="radio" name="custStlMonthSetAgent" <%=i==1?"checked":"" %>  value="<%=i %>"/><%=i %><%=i%15==0?"<br/>":"" %><%
					} %>
				</div>
			  </div>
			  </td>
   		</tr>
   		<tr>
   			<td align="right" valign="top">代收结算周期：</td>
   			<td valign="top">
   			<select name="custSetPeriodDaishou" id="custSetPeriodDaishou" onchange="javascript:getPerIodDaishou()" style="width:200px">
				<option value="D" >按天</option>
				<option value="W" >按周</option>
				<option value="M" >按月</option>
			</select>
   			</td>
   			<td align="right" valign="top">代收结算时间点设置：</td>
   			<td valign="top">
   				<div class="unit" id="dayStlDaishouAdd" >
	                <select name="custSetFreyDaishou" id="custSetFreyDaishouAdd" class="easyui-combobox"  data-options="required:true" style="width:200px">
                        <%for(int i=1; i<=30; i++){ 
	                		%><option value="<%=i %>" <%=i==7?"selected":"" %>>T+<%=i %></option><%
	                	} %>
                    </select>
				</div>
				<div class="unit" id="stlnamAddDaishou"  style="display:none">
				<div id="custStlWeekSetTypeDaishouAdd">
				   <% String [] weekDaishou = {"星期一","星期二","星期三","星期四","星期五","星期六","星期日"};
					for(int i = 0; i<7; i++){ 
						%><input type="checkbox" name="custStlWeekSetDaishou"  value="<%=i+1 %>"/><%=weekDaishou[i] %><%
					} %>
				   <font color="red"><b>*</b></font>
				</div>
			  </div>
			  <div class="unit" id="stlMonthDaishouAdd"  style="display:none">
				<div id="custStlMonthSetTypeDaishouAdd">
				   <%for(int i = 1; i<=28; i++){ 
						%><input type="radio" name="custStlMonthSetDaishou" <%=i==1?"checked":"" %>  value="<%=i %>"/><%=i %><%=i%15==0?"<br/>":"" %><%
					} %>
				</div>
			  </div>
			  </td>
   		</tr>
   		<tr>
   			<td align="right" valign="top">结算账号类型：</td>
   			<td>
	   			<select name="bankStlAcNoType" id="bankStlAcNoType" onchange="javascript:setIssuer(this)" style="width:200px">
					<option value="1" >对私</option>
					<option value="2" >对公</option>                
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
	   			<select name="settlementWay" id="addSettlementWay" style="width:200px">
					<option value="0">自动结算到虚拟账户</option>
					<option value="1" selected>线下结算</option>
					<option value="2">手动结算到虚拟账户</option>
					<!-- <option value="3">实时结算到虚拟账户</option> -->
				</select>
				<font color="#f00">（T+N时，此项有作用）</font>
   			</td>
   		</tr>
   		<tr>
   			<td align="right" valign="top">联行号：</td>
   			<td>
	   			<input class="easyui-textbox" type="text" id="addPayMentIssuer" name="issuer" missingMessage="请输入联行号"
            		validType="length[1,32]" invalidMessage="联行号为1-32位字符" disabled="disabled" style="width:200px"/>
   			</td>
   			<td align="right" valign="top">交易手续费收取方式：</td>
   			<td><select name="chargeWay" style="width:200px">
				<option value="0" >结算收取</option>
				<option value="1" >预存手续费收取</option>
			</select></td>
   		</tr>
 	</table>
	<div class="merchant-lable">商户费率</div>
	<hr color="#ccc"> <!-- 交易类型 1消费 2充值 3结算 4退款 5提现  6转账 7消费B2C借记卡 8消费B2C贷记卡 9消费B2B 10直接到银行卡 11快捷 -->
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
							%><option value="<%=fee.feeCode %>"><%=fee.feeName %></option><%
							} %>
					</select>
					
     		</td>
     		<td align="right" width="120">网银贷记卡：</td>
     		<td><select name="settleFeeCode8" class="easyui-combobox" id="settleFeeCode8" validType="inputExistInCombobox['settleFeeCode8']">
						<%for(int i = 0; i<feeList.size(); i++){
							PayFeeRate fee = (PayFeeRate)feeList.get(i);
							if(!com.PayConstant.CUST_TYPE_MERCHANT.equals(fee.custType)||!"1".equals(fee.tranType))continue;
							%><option value="<%=fee.feeCode %>"><%=fee.feeName %></option><%
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
							%><option value="<%=fee.feeCode %>"><%=fee.feeName %></option><%
							} %>
					</select>
     		</td>
     		<td align="right" width="120">微信：</td>
     		<td><select name="settleFeeCode10" class="easyui-combobox" id="settleFeeCode10" validType="inputExistInCombobox['settleFeeCode10']">
						<%for(int i = 0; i<feeList.size(); i++){
							PayFeeRate fee = (PayFeeRate)feeList.get(i);
							if(!com.PayConstant.CUST_TYPE_MERCHANT.equals(fee.custType)||!"1".equals(fee.tranType))continue;
							%><option value="<%=fee.feeCode %>"><%=fee.feeName %></option><%
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
							%><option value="<%=fee.feeCode %>"><%=fee.feeName %></option><%
							} %>
					</select>
     		</td>
     		<td align="right" width="120">快捷贷记卡：</td>
     		<td><select name="settleFeeCode12" class="easyui-combobox" id="settleFeeCode12" validType="inputExistInCombobox['settleFeeCode12']">
						<%for(int i = 0; i<feeList.size(); i++){
							PayFeeRate fee = (PayFeeRate)feeList.get(i);
							if(!com.PayConstant.CUST_TYPE_MERCHANT.equals(fee.custType)||!"1".equals(fee.tranType))continue;
							%><option value="<%=fee.feeCode %>"><%=fee.feeName %></option><%
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
							%><option value="<%=fee.feeCode %>"><%=fee.feeName %></option><%
							} %>
					</select>
     		</td>
     		<td align="right" width="120">微信wap：</td>
     		<td><select name="settleFeeCode18" class="easyui-combobox" id="settleFeeCode18" validType="inputExistInCombobox['settleFeeCode18']">
						<%for(int i = 0; i<feeList.size(); i++){
							PayFeeRate fee = (PayFeeRate)feeList.get(i);
							if(!com.PayConstant.CUST_TYPE_MERCHANT.equals(fee.custType)||!"1".equals(fee.tranType))continue;
							%><option value="<%=fee.feeCode %>"><%=fee.feeName %></option><%
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
							%><option value="<%=fee.feeCode %>"><%=fee.feeName %></option><%
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
   		<!-- 
   			修改代理商费率：
   				原：代理分润费率：name="settleFeeCode13"
   				原：代理费率：name="settleFeeCode14"
   			==============
   			
   			新增费率说明 ： 
   			
   			19 == 代理分润【网银借记卡】
   			20 == 代理分润【网银贷记卡】
   			21 == 代理分润【B2B】
   			22 == 代理分润【微信】
   			23 == 代理分润【快捷借记卡】
   			24 == 代理分润【快捷贷记卡】
   			25 == 代理分润【支付宝扫码】
   			26 == 代理分润【微信wap】
   		 -->
   		<tr>
			<td align="right" width="120">网银借记卡：</td>
     		<td width="250"><select name="settleFeeCode19" class="easyui-combobox" id="settleFeeCode19" validType="inputExistInCombobox['settleFeeCode19']">
						<%for(int i = 0; i<feeList.size(); i++){
							PayFeeRate fee = (PayFeeRate)feeList.get(i);
							if(!com.PayConstant.CUST_TYPE_MERCHANT.equals(fee.custType)||!"1".equals(fee.tranType))continue;
							%><option value="<%=fee.feeCode %>"><%=fee.feeName %></option><%
							} %>
					</select>
					
     		</td>
     		<td align="right" width="120">网银贷记卡：</td>
     		<td><select name="settleFeeCode20" class="easyui-combobox" id="settleFeeCode20" validType="inputExistInCombobox['settleFeeCode20']">
						<%for(int i = 0; i<feeList.size(); i++){
							PayFeeRate fee = (PayFeeRate)feeList.get(i);
							if(!com.PayConstant.CUST_TYPE_MERCHANT.equals(fee.custType)||!"1".equals(fee.tranType))continue;
							%><option value="<%=fee.feeCode %>"><%=fee.feeName %></option><%
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
							%><option value="<%=fee.feeCode %>"><%=fee.feeName %></option><%
							} %>
					</select>
     		</td>
     		<td align="right" width="120">微信：</td>
     		<td><select name="settleFeeCode22" class="easyui-combobox" id="settleFeeCode22" validType="inputExistInCombobox['settleFeeCode22']">
						<%for(int i = 0; i<feeList.size(); i++){
							PayFeeRate fee = (PayFeeRate)feeList.get(i);
							if(!com.PayConstant.CUST_TYPE_MERCHANT.equals(fee.custType)||!"1".equals(fee.tranType))continue;
							%><option value="<%=fee.feeCode %>"><%=fee.feeName %></option><%
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
							%><option value="<%=fee.feeCode %>"><%=fee.feeName %></option><%
							} %>
					</select>
     		</td>
     		<td align="right" width="120">快捷贷记卡：</td>
     		<td><select name="settleFeeCode24" class="easyui-combobox" id="settleFeeCode24" validType="inputExistInCombobox['settleFeeCode24']">
						<%for(int i = 0; i<feeList.size(); i++){
							PayFeeRate fee = (PayFeeRate)feeList.get(i);
							if(!com.PayConstant.CUST_TYPE_MERCHANT.equals(fee.custType)||!"1".equals(fee.tranType))continue;
							%><option value="<%=fee.feeCode %>"><%=fee.feeName %></option><%
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
							%><option value="<%=fee.feeCode %>"><%=fee.feeName %></option><%
							} %>
					</select>
     		</td>
     		<td align="right" width="120">微信wap：</td>
     		<td><select name="settleFeeCode26" class="easyui-combobox" id="settleFeeCode26" validType="inputExistInCombobox['settleFeeCode26']">
						<%for(int i = 0; i<feeList.size(); i++){
							PayFeeRate fee = (PayFeeRate)feeList.get(i);
							if(!com.PayConstant.CUST_TYPE_MERCHANT.equals(fee.custType)||!"1".equals(fee.tranType))continue;
							%><option value="<%=fee.feeCode %>"><%=fee.feeName %></option><%
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
							%><option value="<%=fee.feeCode %>"><%=fee.feeName %></option><%
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
							%><option value="<%=fee.feeCode %>"><%=fee.feeName %></option><%
							} %>
					</select> 
     		</td>
    		<td align="right">退款费率：</td>
    		<td><select name="settleFeeCode4" class="easyui-combobox" id="settleFeeCode4" validType="inputExistInCombobox['settleFeeCode4']">
						<%for(int i = 0; i<feeList.size(); i++){
							PayFeeRate fee = (PayFeeRate)feeList.get(i);
							if(!com.PayConstant.CUST_TYPE_MERCHANT.equals(fee.custType)||!"4".equals(fee.tranType))continue;
							%><option value="<%=fee.feeCode %>"><%=fee.feeName %></option><%
							} %>
					</select> 
		 	</td>
		</tr>
   		<tr>
    		<td align="right">提现费率：</td>
    		<td><select name="settleFeeCode5" class="easyui-combobox" id="settleFeeCode5" validType="inputExistInCombobox['settleFeeCode5']">
						<%for(int i = 0; i<feeList.size(); i++){
							PayFeeRate fee = (PayFeeRate)feeList.get(i);
							if(!com.PayConstant.CUST_TYPE_MERCHANT.equals(fee.custType)||!"5".equals(fee.tranType))continue;
							%><option value="<%=fee.feeCode %>"><%=fee.feeName %></option><%
							} %>
					</select>
			</td>
   			<td align="right">转账费率：</td>
   			<td><select name="settleFeeCode6" class="easyui-combobox" id="settleFeeCode6" validType="inputExistInCombobox['settleFeeCode6']">
						<%for(int i = 0; i<feeList.size(); i++){
							PayFeeRate fee = (PayFeeRate)feeList.get(i);
							if(!com.PayConstant.CUST_TYPE_MERCHANT.equals(fee.custType)||!"6".equals(fee.tranType))continue;
							%><option value="<%=fee.feeCode %>"><%=fee.feeName %></option><%
							} %>
					</select> 
   			</td>
   		</tr>
   		<tr>
    		<td align="right">商户代收费率：</td>
    		<td><select name="settleFeeCode15" class="easyui-combobox" id="settleFeeCode15" validType="inputExistInCombobox['settleFeeCode15']">
						<%for(int i = 0; i<feeList.size(); i++){
							PayFeeRate fee = (PayFeeRate)feeList.get(i);
							if(!com.PayConstant.CUST_TYPE_MERCHANT.equals(fee.custType)||!"15".equals(fee.tranType))continue;
							%><option value="<%=fee.feeCode %>"><%=fee.feeName %></option><%
							} %>
					</select>
			</td>
   			<td align="right">商户代付费率：</td>
   			<td><select name="settleFeeCode16" class="easyui-combobox" id="settleFeeCode16" validType="inputExistInCombobox['settleFeeCode16']">
						<%for(int i = 0; i<feeList.size(); i++){
							PayFeeRate fee = (PayFeeRate)feeList.get(i);
							if(!com.PayConstant.CUST_TYPE_MERCHANT.equals(fee.custType)||!"16".equals(fee.tranType))continue;
							%><option value="<%=fee.feeCode %>"><%=fee.feeName %></option><%
							} %>
					</select> 
   			</td>
   		</tr>
   		<tr>
    		<td align="right">代理代收费率：</td>
    		<td><select name="settleFeeCode29" class="easyui-combobox" id="settleFeeCode29" validType="inputExistInCombobox['settleFeeCode29']">
						<%for(int i = 0; i<feeList.size(); i++){
							PayFeeRate fee = (PayFeeRate)feeList.get(i);
							if(!com.PayConstant.CUST_TYPE_MERCHANT.equals(fee.custType)||!"15".equals(fee.tranType))continue;
							%><option value="<%=fee.feeCode %>"><%=fee.feeName %></option><%
							} %>
					</select>
			</td>
   			<td align="right">代理代付费率：</td>
   			<td><select name="settleFeeCode30" class="easyui-combobox" id="settleFeeCode30" validType="inputExistInCombobox['settleFeeCode30']">
						<%for(int i = 0; i<feeList.size(); i++){
							PayFeeRate fee = (PayFeeRate)feeList.get(i);
							if(!com.PayConstant.CUST_TYPE_MERCHANT.equals(fee.custType)||!"16".equals(fee.tranType))continue;
							%><option value="<%=fee.feeCode %>"><%=fee.feeName %></option><%
							} %>
					</select> 
   			</td>
   		</tr>
   		<tr>
    		<td align="right">代理分润比例—消费：</td>
    		<td><input class="easyui-numberbox" type="text" id="agentGoalDiscountRate" name="agentGoalDiscountRate"
                   min="0" max="99" invalidMessage="分润比例为1-99"  data-options="required:true" style="width:30px" value="0"/>%（0-99）
			</td>
   			<td align="right">代理分润比例—代付：</td>
   			<td><input class="easyui-numberbox" type="text" id="agentPayRate" name="agentPayRate"
                   min="0" max="99" invalidMessage="分润比例为1-99"  data-options="required:true" style="width:30px" value="0"/>%（0-99）</td>
   		</tr>
   		<tr>
    		<td align="right">税金率：</td>
    		<td><input class="easyui-numberbox" type="text" id="agentTaxRate" name="agentTaxRate"
                   min="0" max="99" invalidMessage="税金率为1-99"  data-options="required:true" style="width:30px" value="5"/>%（0-99）
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
   			<td width="250"><input class="easyui-textbox" type="text" id="addPayMentServContact" name="servContact" missingMessage="请输入客服联系人"
   				data-options="required:true" validType="length[1,20]" invalidMessage="客服联系人为1-20位字符" style="width:200px"/>
   			</td>
   			<td align="right" width="120">技术联系人：</td>
   			<td><input class="easyui-textbox" type="text" id="addPayMentTechContact" name="techContact" missingMessage="请输入技术联系人"
   				data-options="required:true" validType="length[1,20]" invalidMessage="技术联系人为1-20位字符" style="width:200px"/>
   			</td>
   		</tr>
    	<tr>
    		<td align="right" width="120">客服电话：</td>
   			<td><input class="easyui-textbox" type="text" id="addPayMentServTelNo" name="servTelNo" missingMessage="请输入客服电话"
   				data-options="required:true" validType="length[1,20]" invalidMessage="客服联系电话为1-20位字符" style="width:200px"/>
   			</td>
   			<td align="right">技术联系人电话：</td>
   			<td><input class="easyui-textbox" type="text" id="addPayMentTechTelNo" name="techTelNo" missingMessage="请输入技术联系人电话"
   				data-options="required:true" validType="length[1,20]" invalidMessage="技术联系电话为1-20位字符" style="width:200px"/>
   			</td>
   		</tr>
    	<tr>
   			<td align="right">客服邮箱：</td>
   			<td><input class="easyui-textbox" type="text" id="addPayMentServEmail" name="servEmail"
                       validType="email" invalidMessage="请输入正确邮箱，最多50位字符" style="width:200px"/>
   			</td>
   			<td align="right">技术联系人邮箱：</td>
   			<td><input class="easyui-textbox" type="text" id="addPayMentTechEmail" name="techEmail"
                       validType="email" invalidMessage="请输入正确邮箱，最多50位字符" style="width:200px"/>
    		</td>
   		</tr>
 	</table>
	<div class="merchant-lable">企业认证</div>
	<hr color="#ccc">
	<table id="fileuploadFlag" style="margin-left:20px;" width="100%">
		<tr>
			<td colspan="3">
				<div style="color:red;" class="unit">
					&nbsp;&nbsp;&nbsp;上传文件格式为(zip,rar)
				</div>
			</td>
		</tr>
		<tr><td>&nbsp;</td><td>&nbsp;</td><td><br/><input type="file" name="uploadFile" id="fileUploadForAddMerchant"></td></tr>
	</table>
	<div class="merchant-lable">电子证书</div>
	<hr color="#ccc">
	<table style="margin-left:20px;">
		<tr>
			<td><textarea rows="10" cols="80" id="cfca_cert" name="cert"></textarea></td>
		</tr>
	</table>
	</form>
    </div>
    <div data-options="region:'south',border:false" style="text-align:center;padding:5px;">
    	<a class="easyui-linkbutton" data-options="iconCls:'icon-ok'" href="javascript:void(0)" 
			onclick="submitMerchantStart()" style="width:80px">确定</a>
		<a class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" href="javascript:void(0)" 
       		onclick="$('#payMerchantAddForm').form('clear')" style="width:80px">重置</a><br/><br/>
    </div>
</div>
<script type="text/javascript">
addressInit('add_province', 'add_city', 'add_area');
function getPerIod(){
    var perIod = document.getElementById("custSetPeriod").value;  
    
    if(perIod == 'D'){
		$("#stlnamAdd").hide();
		$("#custStlWeekSetTypeAdd").hide();
		$("#stlMonthAdd").hide();
		$("#custSetFreyAdd").removeAttr("disabled");
		$("#dayStlAdd").show();
    }else if(perIod == 'W'){
       	$("#stlnamAdd").show();
       	$("#custStlWeekSetTypeAdd").show();
       	$("#stlMonthAdd").hide();
       	$("#custSetFreyAdd").attr("disabled","disabled");
		$("#dayStlAdd").hide();
    }else if(perIod == 'M'){
       	$("#stlMonthAdd").show();
       	$("#custStlWeekSetTypeAdd").hide();
       	$("#dayStlAdd").hide();
       	$("#custSetFreyAdd").attr("disabled","disabled");
    }
}
function getPerIodAgent(){
	var perIodAgent = document.getElementById("custSetPeriodAgent").value; 
	if(perIodAgent == 'D'){
		$("#stlnamAddAgent").hide();
		$("#custStlWeekSetTypeAgentAdd").hide();
		$("#stlMonthAgentAdd").hide();
		$("#custSetFreyAgentAdd").removeAttr("disabled");
		$("#dayStlAgentAdd").show();
    }else if(perIodAgent == 'W'){
       	$("#stlnamAddAgent").show();
       	$("#custStlWeekSetTypeAgentAdd").show();
    	$("#stlMonthAgentAdd").hide();
       	$("#custSetFreyAgentAdd").attr("disabled","disabled");
		$("#dayStlAgentAdd").hide();
    }else if(perIodAgent == 'M'){
    	$("#stlMonthAgentAdd").show();
       	$("#stlnamAddAgent").show();
       	$("#custStlWeekSetTypeAgentAdd").hide();
       	$("#custSetFreyAgentAdd").attr("disabled","disabled");
		$("#dayStlAgentAdd").hide();
    }
}
function getPerIodDaishou(){
	var perIodDaishou = document.getElementById("custSetPeriodDaishou").value; 
	if(perIodDaishou == 'D'){
		$("#stlnamAddDaishou").hide();
		$("#stlMonthDaishouAdd").hide();
		$("#custSetFreyDaishouAdd").removeAttr("disabled");
		$("#dayStlDaishouAdd").show();
    }else if(perIodDaishou == 'W'){
       	$("#stlnamAddDaishou").show();
       	$("#custStlWeekSetTypeDaishouAdd").show();
    	$("#dayStlDaishouAdd").hide();
       	$("#custSetFreyDaishouAdd").attr("disabled","disabled");
		$("#stlMonthDaishouAdd").hide();
    }else if(perIodDaishou == 'M'){
    	$("#stlMonthDaishouAdd").show();
       	$("#custStlMonthSetTypeDaishouAdd").show();
       	$("#stlnamAddDaishou").hide();
       	$("#custSetFreyDaishouAdd").attr("disabled","disabled");
		$("#dayStlDaishouAdd").hide();
    }
}
function checkMerchantAddAddress(){
	if(document.getElementById("add_province").value == "省"){
		topCenterMessage("<%=JWebConstant.ERROR %>","请选择省市区/县");
		return false;
	}
	return true;
}
// 验证结算时间点	
function checkAddSelTime(){
	var custSetPeriod = $("#custSetPeriod").val();
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
	return true;
}
//验证代理结算时间点	
function checkAddSelTimeAgent(){
	var custSetPeriodAgent = $("#custSetPeriodAgent").val();
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
	return true;
}
//验证代收结算时间点	
function checkAddSelTimeDaishou(){
	var custSetPeriodDaishou = $("#custSetPeriodDaishou").val();
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
	return true;
}
function submitMerchantStart() {
	var val1 = $('#payWayJieji').is(':checked') == true ? 0 : 1;
	var val2 = $('#payWayXinyong').is(':checked') == true ? 0 : 1;
	var val3 = $('#payWayB2b').is(':checked') == true ? 0 : 1;
	var val4 = $('#payWayKuaijie').is(':checked') == true ? 0 : 1;
	var val5 = $('#payWayKuaijieDJ').is(':checked') == true ? 0 : 1;
	var val6 = $('#payWayDaishou').is(':checked') == true ? 0 : 1;
	var val7 = $('#payWayDaifu').is(':checked') == true ? 0 : 1;
	var val8 = $('#payWayQyb').is(':checked') == true ? 0 : 1;
	var val9 = $('#payWayWXScan').is(':checked') == true ? 0 : 1;
	var val10 = $('#payWayWXWap').is(':checked') == true ? 0 : 1;
	var val11 = $('#payWayZFBScan').is(':checked') == true ? 0 : 1;
	var val12 = $('#payWayQQScan').is(':checked') == true ? 0 : 1;
	$('#payWaySupportedId').val(val1 + '' + val2  + '' + val3  + '' + val4+ '' + val6+ '' + val7+ '' + val8+ '' + val5+ '' + val9+ '' + val10+ '' + val11 + '' + val12);
	if('' == $.trim($('#uploadFileUrlId').val())){
		topCenterMessage('<%=JWebConstant.ERROR %>','请上传企业认证资料');
		return;
	}
	var businessLicenceBeginDate= new Date(Date.parse($('#addPayMentBusinessLicenceBeginDate').datebox('getValue').replace(/-/g,  "/")));   
	var businessLicenceEndDate= new Date(Date.parse($('#addPayMentBusinessLicenceEndDate').datebox('getValue').replace(/-/g,  "/")));   
	var now = new Date(new Date().getFullYear(),new Date().getMonth(),new Date().getDate()); 
	if((businessLicenceEndDate <= now) || (businessLicenceBeginDate >= businessLicenceEndDate)){
		topCenterMessage('<%=JWebConstant.ERROR %>','营业执照经营期限不合法');
		return false;
	} 
	$('#payMerchantAddForm').submit();
}
try{
	$('#payMerchantAddForm').form({
	    url:'<%=path %>/addPayMerchant.htm',
	    onSubmit: function(){
	        if($(this).form('validate') && checkMerchantAddAddress() 
	        	&& checkAddSelTime() && checkAddSelTimeAgent())return true;
	        return false;
	    },
	    success:function(data){
	        if(data=='<%=JWebConstant.OK %>'){
	           topCenterMessage('<%=JWebConstant.OK %>','添加成功！');
	           closeTabFreshList('payMerchant',payMerchantAddPageTitle,payMerchantListPageTitle,'payMerchantList','<%=path %>/payMerchant.htm');
	        } else topCenterMessage('<%=JWebConstant.ERROR %>',data);
	    }
	});
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
	  			if(value.length<3 || value.length>20 ){
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
