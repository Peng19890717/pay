<%@page import="com.pay.coopbank.dao.PayChannelBankRelation"%>
<%@page import="com.pay.coopbank.service.PayCoopBankService"%>
<%@page import="com.pay.bank.dao.PayBank"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="util.JWebConstant"%>
<%@ page import="com.jweb.dao.JWebUser"%>
<%@ page import="com.jweb.dao.JWebAction"%>
<%@ page import="com.jweb.service.JWebUserService"%>
<%@ page import="com.pay.coopbank.dao.PayCoopBank"%>
<%@ page import="com.pay.fee.dao.PayFeeRate"%>
<%@ page import="com.pay.fee.service.PayFeeRateService"%>
<%@page import="com.PayConstant"%>
<%
String path = request.getContextPath();
//登录和权限判断
JWebUser user = (JWebUser)session.getAttribute("user");
if(!JWebUserService.checkUser(user,request)){
%><script>window.location.href='<%=path %>/jsp/jweb/login.jsp'</script><%
    return;
}
PayCoopBank channel = (PayCoopBank)request.getAttribute("payCoopBankUpdate");
String arr[] = channel.payScanFlag.split(",");
List feeList = new PayFeeRateService().getAllPayFeeRate();
Map cbRelationMap = (Map)request.getAttribute("cbRelationMap");
String payChannelMaxAmtB2CJUpdate = (String)request.getAttribute("payChannelMaxAmtB2CJUpdate");
String payChannelMaxAmtB2CXUpdate = (String)request.getAttribute("payChannelMaxAmtB2CXUpdate");
String payChannelMaxAmtB2BUpdate = (String)request.getAttribute("payChannelMaxAmtB2BUpdate");
String payChannelMaxAmtKJJUpdate = (String)request.getAttribute("payChannelMaxAmtKJJUpdate");
String payChannelMaxAmtKJDJUpdate = (String)request.getAttribute("payChannelMaxAmtKJDJUpdate");
String payChannelMaxAmtZFBUpdate = (String)request.getAttribute("payChannelMaxAmtZFBUpdate");
String payChannelMaxAmtQQUpdate = (String)request.getAttribute("payChannelMaxAmtQQUpdate");
String payChannelMaxAmtYHKUpdate = (String)request.getAttribute("payChannelMaxAmtYHKUpdate");
String payChannelMaxAmtWXWAPUpdate = (String)request.getAttribute("payChannelMaxAmtWXWAPUpdate");
String payChannelMaxAmtDSUpdate = (String)request.getAttribute("payChannelMaxAmtDSUpdate");
%>
<script type="text/javascript">
$(document).ready(function(){});
<%
String banksScript = "";
for(int i=0; i<PayCoopBankService.BANK_CODE_NAME_LIST.size() ; i++){
	PayBank payBank = PayCoopBankService.BANK_CODE_NAME_LIST.get(i);
	banksScript = banksScript + "\""+payBank.bankCode+"\",";
}
if(banksScript.length()>0)banksScript = banksScript.substring(0,banksScript.length()-1);
%>
var banks=[
	<%=banksScript %>
];
function zhifufangshiWangGai(){
	var wang=document.getElementById("wanggai").checked;
	document.getElementById("payWebFlagGaiId").value=(wang ? "0":"1");
	for(var i=0; i<banks.length; i++){
		var bank = document.getElementById(banks[i]+"_CODE_update");
		bank.checked=wang;
		selectChannelForUpdate(bank);
	}
}
function zhifufangshiKuaiGai(){
	var kuai=document.getElementById("kuaigai").checked;
	document.getElementById("payQuickFlagGaiId").value=(kuai ? "0":"1");
	for(var i=0; i<banks.length; i++){
		var bank = document.getElementById(banks[i]+"_CODE_update_quick");
		bank.checked=kuai;
		selectQuickChannelForUpdate(bank);
	}
}
function zhifufangshiWithdrawGai(){
	var withdraw=document.getElementById("withdrawGai").checked;
	document.getElementById("payWithdrawFlagGaiId").value=(withdraw ? "0":"1");
	for(var i=0; i<banks.length; i++){
		var bank = document.getElementById(banks[i]+"_CODE_update_withdraw");
		bank.checked=withdraw;
	}
}
function zhifufangshiReceiveGai(){
	var receive=document.getElementById("receiveGai").checked;
	document.getElementById("payReceiveFlagGaiId").value=(receive ? "0":"1");
	for(var i=0; i<banks.length; i++){
		var bank = document.getElementById(banks[i]+"_CODE_update_receive");
		bank.checked=receive;
	}
}
</script>
<div class="easyui-layout" data-options="fit:true">
    <div data-options="region:'center'">
        <%if(channel != null){ %>
        <form id="updatePayCoopBankForm" method="post">
        	<input type="hidden" name="payQuickFlag" value="${payCoopBankUpdate.payQuickFlag }" id="payQuickFlagGaiId">
        	<input type="hidden" name="payWebFlag" value="${payCoopBankUpdate.payWebFlag }" id="payWebFlagGaiId">
        	<input type="hidden" name="payWithdrawFlag" value="${payCoopBankUpdate.payWithdrawFlag }" id="payWithdrawFlagGaiId">
        	<input type="hidden" name="payReceiveFlag" value="${payCoopBankUpdate.payReceiveFlag }" id="payReceiveFlagGaiId">
        	<input type="hidden" name="bankCode" value="${payCoopBankUpdate.bankCode }">
            <table cellpadding="5" width="100%" style="margin-top:-10px;">
                <tr><td width="100">&nbsp;</td><td width="200">&nbsp;</td><td width="100">&nbsp;</td><td>&nbsp;</td></tr>
                    <tr>
                        <td align="right">银行编码：</td>
                        <td>${payCoopBankUpdate.bankCode }</td>
                        <td align="right">支付系统ID：</td>
                        <td><input class="easyui-textbox" type="text" id="addPayCoopBankPaySysId" name="paySysId" missingMessage="请输入支付系统ID" data-options="required:true" value="${payCoopBankUpdate.paySysId }"/></td>
                    </tr>
                    <tr>
                        <td align="right">银行名称：</td>
                        <td><input class="easyui-textbox" type="text" id="addPayCoopBankBankName" name="bankName" missingMessage="请输入银行名称" data-options="required:true" value="${payCoopBankUpdate.bankName }"/></td>
                        <td align="right">银行联行号：</td>
                        <td><input class="easyui-textbox" type="text" id="addPayCoopBankBankRelNo" name="bankRelNo" missingMessage="请输入银行联行号" data-options="required:true" value="${payCoopBankUpdate.bankRelNo }"/></td>
                    </tr>
                    <tr>
                        <td align="right">托管银行标志：</td>
                        <td>
                        	<select name="trtBankFlg" id="addPayCoopBankTrtBankFlg" class="easyui-combobox" data-options="required:true">
		                        <option value="Y" <c:if test="${payCoopBankUpdate.trtBankFlg eq 'Y' }">selected</c:if>>是</option>
		                        <option value="N" <c:if test="${payCoopBankUpdate.trtBankFlg eq 'N' }">selected</c:if>>否</option>  
		                   </select>
                        </td>
                        <td align="right">银行结算账号：</td>
                        <td>
                        	<input class="easyui-textbox" type="text" id="addPayCoopBankStlAcNo" name="stlAcNo" missingMessage="请输入银行结算账号" data-options="required:true" value="${payCoopBankUpdate.stlAcNo}"/>
                        </td>
                    </tr>
                    <tr>
                        <td align="right">结算发起时间：</td>
                        
                        <td>
                        <input class="easyui-datebox" style="width:200px" id="addPayCoopBankAccountTime" name="accountTime" missingMessage="请输入银行结算账号" data-options="editable:false" 
                        	value="<%=channel.accountTime==null?"":new SimpleDateFormat("yyyy-MM-dd").format(channel.accountTime) %>"/>
                        	
                       	</td>
                        <td align="right">金融机构许可证：</td>
                        <td><input class="easyui-textbox" type="text" id="addPayCoopBankCertNo" name="certNo" missingMessage="请输入金融机构许可证号" data-options="required:true" value="${payCoopBankUpdate.certNo}"/></td>
                    </tr>
                    <tr>
                        <td align="right">工商注册号：</td>
                        <td><input class="easyui-textbox" type="text" id="addPayCoopBankRegNo" name="regNo" missingMessage="请输入工商注册号" data-options="required:true" value="${payCoopBankUpdate.regNo}"/></td>
                        <td align="right">银行法人代表：</td>
                        <td><input class="easyui-textbox" type="text" id="addPayCoopBankLegRep" name="legRep" missingMessage="请输入银行法人代表" data-options="required:true" value="${payCoopBankUpdate.legRep}"/></td>
                    </tr>
                    <tr>
                        <td align="right">客户经理：</td>
                        <td><input class="easyui-textbox" type="text" id="addPayCoopBankCustMgr" name="custMgr" missingMessage="请输入客户经理" data-options="required:true" value="${payCoopBankUpdate.custMgr}"/></td>
                        <td align="right">联系电话：</td>
                        <td><input class="easyui-textbox" type="text" id="addPayCoopBankTelNo" name="telNo" missingMessage="请输入联系电话"
                                validType="length[1,20]" invalidMessage="联系电话为1-20个字符" data-options="required:true" value="${payCoopBankUpdate.telNo}"/></td>
                    </tr>
                    <tr>
                        <td align="right">Email地址：</td>
                        <td><input class="easyui-textbox" type="text" id="addPayCoopBankBankEmail" name="bankEmail" missingMessage="请输入Email地址"
                                validType="email" invalidMessage="请输入正确邮箱，最多50位字符" data-options="required:true" value="${payCoopBankUpdate.bankEmail}"/></td>
                        <td align="right">通信地址：</td>
                        <td><input class="easyui-textbox" type="text" id="addPayCoopBankBankAddress" name="bankAddress" missingMessage="请输入通信地址" data-options="required:true" value="${payCoopBankUpdate.bankAddress}"/></td>
                    </tr>
                    <tr>
                        <td align="right">线上退款：</td>
                        <td>
                        	<select name="refundOnline" id="addPayCoopBankRefundOnline" class="easyui-combobox" data-options="required:true">
		                        <option value="0" <c:if test="${payCoopBankUpdate.refundOnline eq '0' }">selected</c:if>>支持</option>
		                        <option value="1" <c:if test="${payCoopBankUpdate.refundOnline eq '1' }">selected</c:if>>不支持</option>  
		                   </select>
                        </td>
                        <td align="right">线上对账：</td>
                        <td>
                        	<select name="accountOnline" id="addPayCoopBankAccountOnline" class="easyui-combobox" data-options="required:true">
		                        <option value="0" <c:if test="${payCoopBankUpdate.accountOnline eq '0' }">selected</c:if>>支持</option>
		                        <option value="1" <c:if test="${payCoopBankUpdate.accountOnline eq '1' }">selected</c:if>>不支持</option>  
		                   </select>
                        </td>
                    </tr>
                    <tr>
                        <td align="right">快捷支持重发验证码：</td>
                        <td>
                        	<select name="quickResendSms" id="addPayCoopBankQuickResendSms" class="easyui-combobox" data-options="required:true">
		                        <option value="0" <c:if test="${payCoopBankUpdate.quickResendSms eq '0' }">selected</c:if>>支持</option>
		                        <option value="1" <c:if test="${payCoopBankUpdate.quickResendSms eq '1' }">selected</c:if>>不支持</option>  
		                   </select>
                        </td>
                        <td align="right">结算周期：</td>
                        <td><select name="stlTimeType" id="addPayCoopBankStlTimeType" class="easyui-combobox" data-options="required:true">
								<option value="D" <%="D".equals(channel.stlTimeType) ? "selected" : "" %>>按天</option>
							</select>
							<select name="stlTime" id="addPayCoopBankStlTime" class="easyui-combobox" data-options="required:true">
		                        <%for(int i=0; i<=30; i++){ 
			                		%><option value="<%=i %>" <%=String.valueOf(i).equals(channel.stlTime)?"selected":"" %>>T+<%=i %></option><%
			                	} %>
		                    </select>
						</td>
                    </tr>
                    <tr>
                        <td align="right"><strong>消费费率</strong></td>
                        <td></td>
                        <td></td>
                        <td></td>
                    </tr>
                     <tr>
						<td align="right" width="120">B2C借记卡：</td>
			     		<td width="250"><select name="b2cjjFeeCode" id="b2cjjFeeCode" style="width:200px" class="easyui-combobox" validType="inputExistInCombobox['b2cjjFeeCode']" >
									<%for(int i = 0; i<feeList.size(); i++){
										PayFeeRate fee = (PayFeeRate)feeList.get(i); 
										if(!com.PayConstant.CUST_TYPE_PAY_CHANNEL.equals(fee.custType)||!"1".equals(fee.tranType))continue;
										%>
											<option value="<%=fee.feeCode %>"
												<%=fee.feeCode.equals(channel.b2cjjFeeCode)?"selected":"" %>><%=fee.feeName %></option><%
										}%>
								</select>
								<%-- 最大额<input class="easyui-numberbox" type="text" name="PAY_CHANNEL_MAX_AMT_7" id="PAY_CHANNEL_MAX_AMT_7" value="<%= payChannelMaxAmtB2CJUpdate == null?0:payChannelMaxAmtB2CJUpdate %>" style="width:70px" max="9999999" data-options="required:true" value="0"/> --%>
			     		</td>
			     		<td align="right" width="120">B2C信用卡：</td>
			     		<td><select name="b2cxyFeeCode" id=b2cxyFeeCode style="width:200px" class="easyui-combobox" validType="inputExistInCombobox['b2cxyFeeCode']" >
									<%
										for(int i = 0; i<feeList.size(); i++){
										PayFeeRate fee = (PayFeeRate)feeList.get(i); 
										if(!com.PayConstant.CUST_TYPE_PAY_CHANNEL.equals(fee.custType)||!"1".equals(fee.tranType))continue;
										%>
											<option value="<%=fee.feeCode %>"
												<%=fee.feeCode.equals(channel.b2cxyFeeCode)?"selected":"" %>><%=fee.feeName %></option><%
										 }%>
								</select>
								<%-- 最大额<input class="easyui-numberbox" type="text" name="PAY_CHANNEL_MAX_AMT_8" id="PAY_CHANNEL_MAX_AMT_8" value="<%= payChannelMaxAmtB2CXUpdate == null?0:payChannelMaxAmtB2CXUpdate %>" style="width:70px" max="9999999" data-options="required:true" value="0"/> --%>
			     		</td>
			    	</tr>
			    	<tr>
						<td align="right" width="120">B2B：</td>
			     		<td width="250"><select name="b2bFeeCode" id=b2bFeeCode style="width:200px" class="easyui-combobox" validType="inputExistInCombobox['b2bFeeCode']" >
									<%for(int i = 0; i<feeList.size(); i++){
										PayFeeRate fee = (PayFeeRate)feeList.get(i); 
										if(!com.PayConstant.CUST_TYPE_PAY_CHANNEL.equals(fee.custType)||!"1".equals(fee.tranType))continue;
										%>
											<option value="<%=fee.feeCode %>"
												<%=fee.feeCode.equals(channel.b2bFeeCode)?"selected":"" %>><%=fee.feeName %></option><% }%>
								</select>
								<%-- 最大额<input class="easyui-numberbox" type="text" name="PAY_CHANNEL_MAX_AMT_9" id="PAY_CHANNEL_MAX_AMT_9" value="<%= payChannelMaxAmtB2BUpdate == null?0:payChannelMaxAmtB2BUpdate %>" style="width:70px" max="9999999" data-options="required:true" value="0"/> --%>
			     		</td>
			     		<td align="right" width="120">快捷借记卡：</td>
			     		<td><select name="kjJJFeeCode" id=kjJJFeeCode style="width:200px" class="easyui-combobox" validType="inputExistInCombobox['kjJJFeeCode']" >
									<%for(int i = 0; i<feeList.size(); i++){
										PayFeeRate fee = (PayFeeRate)feeList.get(i); 
										if(!com.PayConstant.CUST_TYPE_PAY_CHANNEL.equals(fee.custType)||!"1".equals(fee.tranType))continue;
										%>
											<option value="<%=fee.feeCode %>"
												<%=fee.feeCode.equals(channel.kjJJFeeCode)?"selected":"" %>><%=fee.feeName %></option>
										<% }%>
								</select>
								<%-- 最大额<input class="easyui-numberbox" type="text" name="PAY_CHANNEL_MAX_AMT_11" id="PAY_CHANNEL_MAX_AMT_11" value="<%= payChannelMaxAmtKJJUpdate == null?0:payChannelMaxAmtKJJUpdate %>" style="width:70px" max="9999999" data-options="required:true" value="0"/> --%>
			     		</td>
			    	</tr>
			    	<tr>
			    		<td align="right" width="120" valign="top">快捷信用卡：</td>
			     		<td width="250" valign="top"><select name="kjDJFeeCode" id=kjDJFeeCode style="width:200px" class="easyui-combobox" validType="inputExistInCombobox['kjDJFeeCode']" >
									<%for(int i = 0; i<feeList.size(); i++){
										PayFeeRate fee = (PayFeeRate)feeList.get(i); 
										if(!com.PayConstant.CUST_TYPE_PAY_CHANNEL.equals(fee.custType)||!"1".equals(fee.tranType))continue;
										%>
											<option value="<%=fee.feeCode %>"
												<%=fee.feeCode.equals(channel.kjDJFeeCode)?"selected":"" %>><%=fee.feeName %></option>
										<% }%>
								</select>
								<%-- 最大额<input class="easyui-numberbox" type="text" name="PAY_CHANNEL_MAX_AMT_12" id="PAY_CHANNEL_MAX_AMT_12" value="<%= payChannelMaxAmtKJDJUpdate == null?0:payChannelMaxAmtKJDJUpdate %>" style="width:70px" max="9999999" data-options="required:true" value="0"/> --%>
			     		</td>
			     		<td align="right" width="120" valign="top">支付宝扫码：</td>
			     		<td>
			     		<select name="updateSupportedZFBScanFlag">
							<option value="0">不支持</option>
							<%if(!"ZFT".equals(channel.bankCode)){ %>
							<option value="1" <%= "1".equals(arr[0])?"selected":""%>>支持</option>
							<%} %>
						</select>
			     		单笔最大额<input class="easyui-numberbox" type="text" name="PAY_CHANNEL_MAX_AMT_17" id="PAY_CHANNEL_MAX_AMT_17" value="<%= payChannelMaxAmtZFBUpdate == null ?0:payChannelMaxAmtZFBUpdate %>" style="width:70px" max="9999999" data-options="required:true" value="0"/>0为无限制<br/><br/>
			     		<select name="zfbFeeCode" id=zfbFeeCode style="width:200px" class="easyui-combobox" validType="inputExistInCombobox['zfbFeeCode']" >
							<%for(int i = 0; i<feeList.size(); i++){
								PayFeeRate fee = (PayFeeRate)feeList.get(i);
								if(!com.PayConstant.CUST_TYPE_PAY_CHANNEL.equals(fee.custType)||!"1".equals(fee.tranType))continue;
								%><option value="<%=fee.feeCode %>"
									<%=fee.feeCode.equals(channel.zfbFeeCode)?"selected":"" %>><%=fee.feeName %></option><%
								} %>
						</select>
			     		</td>
			    	</tr>
			    	<tr>
			    		<td align="right" width="120" valign="top">微信扫码：</td>
			     		<td width="250">
			     		<select name="updateSupportedWeixinScanFlag">
							<option value="0">不支持</option>
							<%if(!"ZFT".equals(channel.bankCode)){ %>
							<option value="1" <%= "1".equals(arr[1])?"selected":""%>>支持</option>
							<%} %>
						</select>
			     		单笔最大额<input class="easyui-numberbox" type="text" name="PAY_CHANNEL_MAX_AMT_10" id="PAY_CHANNEL_MAX_AMT_10" value="<%= payChannelMaxAmtYHKUpdate == null ?0:payChannelMaxAmtYHKUpdate %>" style="width:70px" max="9999999" data-options="required:true" value="0"/>0无限制<br/><br/>
			     		<select name="yhkFeeCode" id=yhkFeeCode style="width:200px" class="easyui-combobox" validType="inputExistInCombobox['yhkFeeCode']" >
							<%for(int i = 0; i<feeList.size(); i++){
								PayFeeRate fee = (PayFeeRate)feeList.get(i); 
								if(!com.PayConstant.CUST_TYPE_PAY_CHANNEL.equals(fee.custType)||!"1".equals(fee.tranType))continue;
								%>
									<option value="<%=fee.feeCode %>"
										<%=fee.feeCode.equals(channel.yhkFeeCode)?"selected":"" %>><%=fee.feeName %></option>
								<% }%>
						</select>
			     		</td>
			     		<td align="right" width="120" valign="top">微信WAP：</td>
			     		<td>
			     		<select name="updateSupportedWeixinWAPFlag">
							<option value="0">不支持</option>
							<option value="1" <%= "1".equals(arr[2])?"selected":""%>>支持</option>
						</select>
			     		单笔最大额<input class="easyui-numberbox" type="text" name="PAY_CHANNEL_MAX_AMT_18" id="PAY_CHANNEL_MAX_AMT_18" value="<%= payChannelMaxAmtWXWAPUpdate == null?0:payChannelMaxAmtWXWAPUpdate %>" style="width:70px" max="9999999" data-options="required:true" value="0"/>0无限制<br/><br/>
			     		<select name="wxwapFeeCode" id=wxwapFeeCode style="width:200px" class="easyui-combobox" validType="inputExistInCombobox['wxwapFeeCode']" >
							<%for(int i = 0; i<feeList.size(); i++){
								PayFeeRate fee = (PayFeeRate)feeList.get(i);
								if(!com.PayConstant.CUST_TYPE_PAY_CHANNEL.equals(fee.custType)||!"1".equals(fee.tranType))continue;
								%><option value="<%=fee.feeCode %>"
									<%=fee.feeName %>
										<%=fee.feeCode.equals(channel.wxwapFeeCode)?"selected":"" %>><%=fee.feeName %></option><%
								} %>
						</select>
			     		</td>
			    	</tr>
			    	<tr>
			    		<td align="right" width="120" valign="top">QQ扫码：</td>
			     		<td>
				     		<select name="updateSupportedQQScanFlag">
								<option value="0">不支持</option>
								<option value="1" <%= "1".equals(arr[3])?"selected":""%>>支持</option>
							</select>
				     		单笔最大额<input class="easyui-numberbox" type="text" name="PAY_CHANNEL_MAX_AMT_27" id="PAY_CHANNEL_MAX_AMT_27" value="<%= payChannelMaxAmtQQUpdate == null ?0:payChannelMaxAmtQQUpdate %>" style="width:70px" max="9999999" data-options="required:true" value="0"/>0无限制<br/><br/>
				     		<select name="qqFeeCode" id=qqFeeCode style="width:200px" class="easyui-combobox" validType="inputExistInCombobox['qqFeeCode']" >
								<%for(int i = 0; i<feeList.size(); i++){
									PayFeeRate fee = (PayFeeRate)feeList.get(i);
									if(!com.PayConstant.CUST_TYPE_PAY_CHANNEL.equals(fee.custType)||!"1".equals(fee.tranType))continue;
									%><option value="<%=fee.feeCode %>"
										<%=fee.feeCode.equals(channel.qqFeeCode )?"selected":"" %>><%=fee.feeName %></option><%
									} %>
							</select>
						</td>
						<td></td>
						<td></td>
			    	</tr>
			    	<tr>
                        <td align="right"><strong>其他费率</strong></td>
                        <td></td>
                        <td></td>
                        <td></td>
                    </tr>
                    <tr>
						<td align="right" width="120px">转账费率：</td>
			   			<td><select name="zzFeeCode" id=zzFeeCode style="width:200px" class="easyui-combobox" validType="inputExistInCombobox['zzFeeCode']" >
									<%for(int i = 0; i<feeList.size(); i++){
										PayFeeRate fee = (PayFeeRate)feeList.get(i); 
										if(!com.PayConstant.CUST_TYPE_PAY_CHANNEL.equals(fee.custType)||!"6".equals(fee.tranType))continue;
										%>
											<option value="<%=fee.feeCode %>"
												<%=fee.feeCode.equals(channel.zzFeeCode)?"selected":"" %>><%=fee.feeName %></option>
										<% }%>
								</select> 
			   			</td>
						<td align="right" width="120px">退款费率：</td>
			   			<td><select name="tkFeeCode" id=tkFeeCode style="width:200px" class="easyui-combobox" validType="inputExistInCombobox['tkFeeCode']" >
									<%for(int i = 0; i<feeList.size(); i++){
										PayFeeRate fee = (PayFeeRate)feeList.get(i); 
										if(!com.PayConstant.CUST_TYPE_PAY_CHANNEL.equals(fee.custType)||!"4".equals(fee.tranType))continue;
										%>
											<option value="<%=fee.feeCode %>"
												<%=fee.feeCode.equals(channel.tkFeeCode)?"selected":"" %>><%=fee.feeName %></option>
										<% }%>
								</select> 
			   			</td>
			   		</tr>
		   		 	<tr>
						<td align="right" width="120px">提现费率：</td>
			   			<td><select name="txFeeCode" id=txFeeCode style="width:200px" class="easyui-combobox" validType="inputExistInCombobox['txFeeCode']" >
									<%
									for(int i = 0; i<feeList.size(); i++){
										PayFeeRate fee = (PayFeeRate)feeList.get(i); 
										if(!com.PayConstant.CUST_TYPE_PAY_CHANNEL.equals(fee.custType)||!"5".equals(fee.tranType))continue;
										%>
											<option value="<%=fee.feeCode %>"
												<%=fee.feeCode.equals(channel.txFeeCode)?"selected":"" %>><%=fee.feeName %></option>
										<% } %>
								</select> 
			   			</td>
						<td align="right" width="120px">代收费率：</td>
			   			<td><select name="dsFeeCode" id=dsFeeCode style="width:200px" class="easyui-combobox" validType="inputExistInCombobox['dsFeeCode']" >
									<%
									for(int i = 0; i<feeList.size(); i++){
										PayFeeRate fee = (PayFeeRate)feeList.get(i); 
										if(!com.PayConstant.CUST_TYPE_PAY_CHANNEL.equals(fee.custType)||!"15".equals(fee.tranType))continue;
										%>
											<option value="<%=fee.feeCode %>"
												<%=fee.feeCode.equals(channel.dsFeeCode)?"selected":"" %>><%=fee.feeName %></option>
										<% }%>
								</select>
								<%-- 最大额<input class="easyui-numberbox" type="text" name="PAY_CHANNEL_MAX_AMT_15" id="PAY_CHANNEL_MAX_AMT_15" value="<%= payChannelMaxAmtDSUpdate == null?0:payChannelMaxAmtDSUpdate %>" style="width:70px" max="9999999" data-options="required:true" value="0"/> --%>
			   			</td>
			   		</tr>
		   		 	<tr>
						<td align="right" width="120px">代付费率：</td>
			   			<td><select name="dfFeeCode" id=dfFeeCode style="width:200px" class="easyui-combobox" validType="inputExistInCombobox['dfFeeCode']" >
									<%
									for(int i = 0; i<feeList.size(); i++){
										PayFeeRate fee = (PayFeeRate)feeList.get(i); 
										if(!com.PayConstant.CUST_TYPE_PAY_CHANNEL.equals(fee.custType)||!"16".equals(fee.tranType))continue;
										%>
											<option value="<%=fee.feeCode %>"
												<%=fee.feeCode.equals(channel.dfFeeCode)?"selected":"" %>><%=fee.feeName %></option>
										<% }%>
								</select> 
			   			</td>
			   			<td></td>
						<td></td>
			   		</tr>
			   		<tr>
						<td align="right" valign="top">支持银行：</td>
						<td colspan="3">
						<table style="border:solid #add9c0; border-width:1px 0px 0px 1px;">
							<tr>
								<td colspan="7"  bgcolor="#CCCCCC" style="border:solid #add9c0; border-width:0px 1px 1px 0px;">
									<input type="checkbox" <c:if test="${payCoopBankUpdate.payWebFlag eq '0' }">checked</c:if> onclick="zhifufangshiWangGai()" id="wanggai"/><strong>网银支付（最大限额0为无限制，单位：元）</strong>
								</td>
							</tr>
							<tr>
							<%
							String bankCodes = ","+(String)request.getAttribute(("bankCodes"));
							for(int i = 0 ; i < PayCoopBankService.BANK_CODE_NAME_LIST.size(); i++) {
								PayBank payBank = PayCoopBankService.BANK_CODE_NAME_LIST.get(i);
								PayChannelBankRelation r = (PayChannelBankRelation)cbRelationMap.get(channel.bankCode+","+payBank.bankCode);
								%>
								<td style="border:solid #add9c0; border-width:0px 1px 1px 0px;">
								<input type="checkbox" name="payBankCodes" value="<%=payBank.bankCode%>" onclick="selectChannelForUpdate(this)"  id="<%=payBank.bankCode%>_CODE_update"
									<%if(bankCodes.contains(","+payBank.bankCode+",")&&r!=null&&r.supportedUserType!=null) out.print("checked=\"checked\"");%> /><strong><%=payBank.bankName%></strong><br/>
								<input type="checkbox" id="<%=payBank.bankCode%>UserType0_update" title="<%=payBank.bankCode%>" name="<%=payBank.bankCode%>UserType0" value="0"  
									onclick="selectChannelUserTypeForUpdate(this)" <%=r!=null&&r.supportedUserType!=null&&r.supportedUserType.indexOf("0,")!=-1?"checked":"" %>/>借记卡
								<input class="easyui-numberbox" type="text" name="webDebitMaxAmt_<%=payBank.bankCode%>" style="width:70px" max="9999999" data-options="required:true" value="<%= r!=null&&r.supportedUserType!=null&&r.supportedUserType.indexOf("0,")!=-1?r.webDebitMaxAmt/100:0  %>"/><br/>
								<input type="checkbox" id="<%=payBank.bankCode%>UserType1_update" title="<%=payBank.bankCode%>" name="<%=payBank.bankCode%>UserType1" value="1"  
									onclick="selectChannelUserTypeForUpdate(this)"<%=r!=null&&r.supportedUserType!=null&&r.supportedUserType.indexOf("1,")!=-1?"checked":"" %>/>贷记卡
								<input class="easyui-numberbox" type="text" name="webCreditMaxAmt_<%=payBank.bankCode%>" style="width:70px" max="9999999" data-options="required:true" value="<%= r!=null&&r.supportedUserType!=null&&r.supportedUserType.indexOf("1,")!=-1?r.webCreditMaxAmt/100:0 %>"/><br/>
								<input type="checkbox" id="<%=payBank.bankCode%>UserType4_update" title="<%=payBank.bankCode%>" name="<%=payBank.bankCode%>UserType4" value="4"  
									onclick="selectChannelUserTypeForUpdate(this)"<%=r!=null&&r.supportedUserType!=null&&r.supportedUserType.indexOf("4,")!=-1?"checked":"" %>/>对&nbsp;&nbsp;&nbsp;&nbsp;公
								<input class="easyui-numberbox" type="text" name="webPublicMaxAmt_<%=payBank.bankCode%>" style="width:70px" max="9999999" data-options="required:true" value="<%= r!=null&&r.supportedUserType!=null&&r.supportedUserType.indexOf("4,")!=-1?r.webPublicMaxAmt/100:0 %>"/><br/></td><%
								if((i+1)%7==0)out.println("</tr><tr>");
							}%>
							</tr>
						</table><br/>
						<script type="text/javascript">
						function selectChannelForUpdate(channel){
							if(channel.checked){
								document.getElementById(channel.value+"UserType0_update").checked=true;
								document.getElementById(channel.value+"UserType1_update").checked=true;
								document.getElementById(channel.value+"UserType4_update").checked=true;
							} else {
								document.getElementById(channel.value+"UserType0_update").checked=false;
								document.getElementById(channel.value+"UserType1_update").checked=false;
								document.getElementById(channel.value+"UserType4_update").checked=false;
							}
							checkWangYinSelectForUpdate();
						}
						function selectChannelUserTypeForUpdate(cUserType){
							document.getElementById(cUserType.title+"_CODE_update").checked=false;
							if(document.getElementById(cUserType.title+"UserType0_update").checked 
								||document.getElementById(cUserType.title+"UserType1_update").checked
								||document.getElementById(cUserType.title+"UserType4_update").checked)
								document.getElementById(cUserType.title+"_CODE_update").checked=true;
							checkWangYinSelectForUpdate();
						}
						function checkWangYinSelectForUpdate(){
							document.getElementById("wanggai").checked=false;
							document.getElementById("payWebFlagGaiId").value="1";
							for(var i=0; i<banks.length; i++){
								var bank = document.getElementById(banks[i]+"_CODE_update");
								if(bank.checked){
									document.getElementById("wanggai").checked=true;
									document.getElementById("payWebFlagGaiId").value="0";
									return;
								}
							}
						}
						</script>
						<table style="border:solid #add9c0; border-width:1px 0px 0px 1px;">
							<tr>
								<td colspan="7"  bgcolor="#CCCCCC" style="border:solid #add9c0; border-width:0px 1px 1px 0px;">
									<input type="checkbox" <c:if test="${payCoopBankUpdate.payQuickFlag eq '0' }">checked</c:if> onclick="zhifufangshiKuaiGai()" id="kuaigai"/><strong>快捷支付（最大限额0为无限制，单位：元）</strong>
								</td>
							</tr>
							<tr>
							<%
							bankCodes = ","+(String)request.getAttribute(("bankCodes"));
							for(int i = 0 ; i < PayCoopBankService.BANK_CODE_NAME_LIST.size(); i++) {
								PayBank payBank = PayCoopBankService.BANK_CODE_NAME_LIST.get(i);
								PayChannelBankRelation r = (PayChannelBankRelation)cbRelationMap.get(channel.bankCode+","+payBank.bankCode);
								%>
								<td style="border:solid #add9c0; border-width:0px 1px 1px 0px;">
								<input type="checkbox" name="payBankCodesQuick" value="<%=payBank.bankCode%>" onclick="selectQuickChannelForUpdate(this)"  id="<%=payBank.bankCode%>_CODE_update_quick"
									<%if(bankCodes.contains(","+payBank.bankCode+",")&&r!=null&&r.quickUserType!=null) out.print("checked=\"checked\"");%> /><strong><%=payBank.bankName%></strong><br/>
								<input type="checkbox" id="<%=payBank.bankCode%>UserType0_update_quick" title="<%=payBank.bankCode%>" name="<%=payBank.bankCode%>UserTypeQuick0" value="0"  
									onclick="selectQuickChannelUserTypeForUpdate(this)" <%=r!=null&&r.quickUserType!=null&&r.quickUserType.indexOf("0,")!=-1?"checked":"" %>/>借记卡
								<input class="easyui-numberbox" type="text" name="quickDebitMaxAmt_<%=payBank.bankCode%>" style="width:70px" max="9999999" data-options="required:true" 
									value="<%= r!=null&&r.quickUserType!=null&&r.quickUserType.indexOf("0,")!=-1?r.quickDebitMaxAmt/100:0%>"/><br/>
								<input type="checkbox" id="<%=payBank.bankCode%>UserType1_update_quick" title="<%=payBank.bankCode%>" name="<%=payBank.bankCode%>UserTypeQuick1" value="1"  
									onclick="selectQuickChannelUserTypeForUpdate(this)"<%=r!=null&&r.quickUserType!=null&&r.quickUserType.indexOf("1,")!=-1?"checked":"" %>/>贷记卡
								<input class="easyui-numberbox" type="text" name="quickCeditMaxAmt_<%=payBank.bankCode%>" style="width:70px" max="9999999" data-options="required:true" 
									value="<%= r!=null&&r.quickUserType!=null&&r.quickUserType.indexOf("1,")!=-1?r.quickCeditMaxAmt/100:0%>"/><br/>
									</td><%
								if((i+1)%7==0)out.println("</tr><tr>");;
							}%>
							</tr>
						</table>
						<script type="text/javascript">
						function selectQuickChannelForUpdate(channel){
							if(channel.checked){
								document.getElementById(channel.value+"UserType0_update_quick").checked=true;
								document.getElementById(channel.value+"UserType1_update_quick").checked=true;
							} else {
								document.getElementById(channel.value+"UserType0_update_quick").checked=false;
								document.getElementById(channel.value+"UserType1_update_quick").checked=false;
							}
							checkQuaiJieSelectForUpdate();
						}
						function selectQuickChannelUserTypeForUpdate(cUserType){
							document.getElementById(cUserType.title+"_CODE_update_quick").checked=false;
							if(document.getElementById(cUserType.title+"UserType0_update_quick").checked 
								||document.getElementById(cUserType.title+"UserType1_update_quick").checked)
								document.getElementById(cUserType.title+"_CODE_update_quick").checked=true;
							checkQuaiJieSelectForUpdate();
						}
						function checkQuaiJieSelectForUpdate(){
							document.getElementById("kuaigai").checked=false;
							document.getElementById("payQuickFlagGaiId").value="1";
							for(var i=0; i<banks.length; i++){
								var bank = document.getElementById(banks[i]+"_CODE_update_quick");
								if(bank.checked){
									document.getElementById("kuaigai").checked=true;
									document.getElementById("payQuickFlagGaiId").value="0";
									return;
								}
							}
						}
						</script>
						<br/>
						<table style="border:solid #add9c0; border-width:1px 0px 0px 1px;">
							<tr>
								<td colspan="7"  bgcolor="#CCCCCC" style="border:solid #add9c0; border-width:0px 1px 1px 0px;">
									<input type="checkbox" <c:if test="${payCoopBankUpdate.payReceiveFlag eq '0' }">checked</c:if>  onclick="zhifufangshiReceiveGai()" id="receiveGai"/><strong>代收（最大限额0为无限制，单位：元）</strong>
								</td>
							</tr>
							<tr>
							<%
							for(int i = 0 ; i < PayCoopBankService.BANK_CODE_NAME_LIST.size(); i++) {
								PayBank payBank = PayCoopBankService.BANK_CODE_NAME_LIST.get(i);
								PayChannelBankRelation r = (PayChannelBankRelation)cbRelationMap.get(channel.bankCode+","+payBank.bankCode);
								%><td style="border:solid #add9c0; border-width:0px 1px 1px 0px;">
								<input type="checkbox" onclick="selectReceiveChannelForUpdate(this)" name="payBankCodesReceive" value="<%=payBank.bankCode%>" 
									<%=r!=null&&"0".equals(r.receiveUserType)?"checked":"" %> 
									id="<%=payBank.bankCode%>_CODE_update_receive"/><strong><%=payBank.bankName%></strong><br/>
								<input class="easyui-numberbox" type="text" name="receiveMaxAmt_<%=payBank.bankCode%>" style="width:70px" max="9999999" data-options="required:true" value="<%= r!=null&&"0".equals(r.receiveUserType)?r.receiveMaxAmt/100:0%>"/><br/>
									<%
								if((i+1)%7==0)out.println("</tr><tr>");
							}%>
							</tr>
						</table>
						<script type="text/javascript">
						function selectReceiveChannelForUpdate(channel){
							document.getElementById("payReceiveFlagGaiId").value="1";
							document.getElementById("receiveGai").checked=false;
							for(var i=0; i<banks.length; i++){
								var bank = document.getElementById(banks[i]+"_CODE_update_receive");
								if(bank.checked){
									document.getElementById("payReceiveFlagGaiId").value="0";
									document.getElementById("receiveGai").checked=true;
									return;
								}
							}
						}
						</script>
						<br/>
						<table style="border:solid #add9c0; border-width:1px 0px 0px 1px;">
							<tr>
								<td colspan="7"  bgcolor="#CCCCCC" style="border:solid #add9c0; border-width:0px 1px 1px 0px;">
									<input type="checkbox" <c:if test="${payCoopBankUpdate.payWithdrawFlag eq '0' }">checked</c:if>  onclick="zhifufangshiWithdrawGai()" id="withdrawGai"/><strong>代付</strong>
								</td>
							</tr>
							<tr>
							<%
							for(int i = 0 ; i < PayCoopBankService.BANK_CODE_NAME_LIST.size(); i++) {
								PayBank payBank = PayCoopBankService.BANK_CODE_NAME_LIST.get(i);
								PayChannelBankRelation r = (PayChannelBankRelation)cbRelationMap.get(channel.bankCode+","+payBank.bankCode);
								%><td style="border:solid #add9c0; border-width:0px 1px 1px 0px;">
								<input type="checkbox" onclick="selectWithdrawChannelForUpdate(this)" name="payBankCodesWithdraw" value="<%=payBank.bankCode%>" 
									<%=r!=null&&"0".equals(r.withdrawUserType)?"checked":"" %> 
									id="<%=payBank.bankCode%>_CODE_update_withdraw"/><strong><%=payBank.bankName%></strong><br/><%
								if((i+1)%7==0)out.println("</tr><tr>");
							}%>
							</tr>
						</table>
						<script type="text/javascript">
						function selectWithdrawChannelForUpdate(channel){
							document.getElementById("payWithdrawFlagGaiId").value="1";
							document.getElementById("withdrawGai").checked=false;
							for(var i=0; i<banks.length; i++){
								var bank = document.getElementById(banks[i]+"_CODE_update_withdraw");
								if(bank.checked){
									document.getElementById("payWithdrawFlagGaiId").value="0";
									document.getElementById("withdrawGai").checked=true;
									return;
								}
							}
						}
						</script>
						</td>
					</tr>
                    <tr>
                        <td align="right" valign="top">备注信息：</td>
                        <td colspan="3"><input class="easyui-textbox" type="text" id="addPayCoopBankRemark" name="remark"  missingMessage="请输入备注"
                      		validType="length[0,50]" invalidMessage="备注内容请控制在50个字符" data-options="multiline:true" 
                      		style="width:240px;height:70px" value="${payCoopBankUpdate.remark}"/></td>
                    </tr>
            </table>
        </form>
        <%} %>
    </div>
    <div data-options="region:'south',border:false" style="text-align:right;padding:5px;">
        <a class="easyui-linkbutton" data-options="iconCls:'icon-ok'" href="javascript:updatePayCoopBankFormSubmit()" style="width:80px">确定</a>
        <a class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" href="javascript:void(0)" 
            onclick="$('#mainTabs').tabs('close',payCoopBankUpdatePageTitle)" style="width:80px">取消</a>
    </div>
</div>
<script type="text/javascript">
$('#updatePayCoopBankForm').form({
    url:'<%=path %>/updatePayCoopBank.htm',
    onSubmit: function(){
        return $(this).form('validate');
    },
    success:function(data){
        $('#payCoopBankList').datagrid('reload');
        if(data=='<%=JWebConstant.OK %>'){
            topCenterMessage('<%=JWebConstant.OK %>','修改成功！');
            closeTabFreshList('payCoopBank',payCoopBankUpdatePageTitle,payCoopBankListPageTitle,'payCoopBankList','<%=path %>/payCoopBank.htm');
        } else topCenterMessage('<%=JWebConstant.ERROR %>',data);
    }
});
function updatePayCoopBankFormSubmit(){
    $('#updatePayCoopBankForm').submit();
}
</script>
