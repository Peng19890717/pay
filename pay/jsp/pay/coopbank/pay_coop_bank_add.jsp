<%@page import="com.pay.coopbank.service.PayCoopBankService"%>
<%@page import="com.pay.bank.dao.PayBank"%>
<%@page import="java.util.List"%>
<%@page import="com.PayConstant"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ page import="util.JWebConstant"%>
<%@ page import="com.jweb.dao.JWebUser"%>
<%@ page import="com.jweb.dao.JWebAction"%>
<%@ page import="com.jweb.service.JWebUserService"%>
<%@ page import="com.pay.fee.dao.PayFeeRate"%>
<%@ page import="com.pay.fee.service.PayFeeRateService"%>
<%
String path = request.getContextPath();
//登录和权限判断
JWebUser user = (JWebUser)session.getAttribute("user");
if(!JWebUserService.checkUser(user,request)){
%><script>window.location.href='<%=path %>/jsp/jweb/login.jsp'</script><%
    return;
}
java.util.List feeList = new PayFeeRateService().getAllPayFeeRate();
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
function zhifufangshiWangYin(){
	var wang=document.getElementById("wangyin").checked;
	document.getElementById("payWebFlagId").value=(wang ? "0":"1");
	for(var i=0; i<banks.length; i++){
		var bank = document.getElementById(banks[i]+"_CODE_add");
		bank.checked=wang;
		selectChannelForAdd(bank);
	}
}
function zhifufangshiKuaiJie(){
	var kuai=document.getElementById("kuaijie").checked;
	document.getElementById("payQuickFlagId").value=(kuai ? "0":"1");
	for(var i=0; i<banks.length; i++){
		var bank = document.getElementById(banks[i]+"_CODE_add_quick");
		bank.checked=kuai;
		selectQuickChannelForAdd(bank);
	}
}
function zhifufangshiWithdraw(){
	var withdraw=document.getElementById("withdraw").checked;
	document.getElementById("payWithdrawFlagId").value=(withdraw ? "0":"1");
	for(var i=0; i<banks.length; i++){
		var bank = document.getElementById(banks[i]+"_CODE_add_withdraw");
		bank.checked=withdraw;
	}
}
function zhifufangshiReceive(){
	var receive=document.getElementById("receive").checked;
	document.getElementById("payReceiveFlagId").value=(receive ? "0":"1");
	for(var i=0; i<banks.length; i++){
		var bank = document.getElementById(banks[i]+"_CODE_add_receive");
		bank.checked=receive;
	}
}
</script>
<div class="easyui-layout" data-options="fit:true">
    <div data-options="region:'center'">
        <form id="addPayCoopBankForm" method="post">
        	<input type="hidden" name="payQuickFlag" value="1" id="payQuickFlagId">
        	<input type="hidden" name="payWebFlag" value="1" id="payWebFlagId">
        	<input type="hidden" name="payWithdrawFlag" value="1" id="payWithdrawFlagId">
        	<input type="hidden" name="payReceiveFlag" value="1" id="payReceiveFlagId">
        	<input type="hidden" name="bizRange" value="-"/>
            <table cellpadding="5" width="100%" style="margin-top:-10px;">
                <tr><td width="100">&nbsp;</td><td width="350">&nbsp;</td><td width="100">&nbsp;</td><td>&nbsp;</td></tr>
                    <tr>
                        <td align="right">银行编码：</td>
                        <td style="width:300px"><input class="easyui-textbox" type="text" id="addPayCoopBankBankCode" name="bankCode" missingMessage="请输入银行编码"
                            validType="checkPayCoopBankBankCode[0]" data-options="required:true"/></td>
                        <td align="right">支付系统ID：</td>
                        <td><input class="easyui-textbox" type="text" id="addPayCoopBankPaySysId" name="paySysId" missingMessage="请输入支付系统ID"
                               data-options="required:true"/></td>
                    </tr>
                    <tr>
                        <td align="right">银行名称：</td>
                        <td><input class="easyui-textbox" type="text" id="addPayCoopBankBankName" name="bankName" missingMessage="请输入银行名称"
                               data-options="required:true"/></td>
                        <td align="right">银行联行号：</td>
                        <td><input class="easyui-textbox" type="text" id="addPayCoopBankBankRelNo" name="bankRelNo" missingMessage="请输入银行联行号"
                               data-options="required:true"/></td>
                    </tr>
                    <tr>
                        <td align="right">托管银行标志：</td>
                        <td>
                            <select name="trtBankFlg" id="addPayCoopBankTrtBankFlg" class="easyui-combobox"  data-options="required:true">
		                         <option value="Y">是</option>  
		                         <option value="N">否</option>  
		                    </select> 
                        </td>
                        <td align="right">银行结算账号：</td>
                        <td><input class="easyui-textbox" type="text" id="addPayCoopBankStlAcNo" name="stlAcNo" missingMessage="请输入银行结算账号"
                                data-options="required:true"/></td>
                    </tr>
                    <tr>
                        <td align="right">结算发起时间：</td>
                        <td><input class="easyui-datebox" style="width:100px" id="addPayCoopBankAccountTime" name="accountTime" missingMessage="请输入银行结算账号" data-options="editable:false,required:true"/></td>
                        <td align="right">金融机构许可证：</td>
                        <td><input class="easyui-textbox" type="text" id="addPayCoopBankCertNo" name="certNo" missingMessage="请输入金融机构许可证"
                                data-options="required:true"/></td>
                    </tr>
                    <tr>
                        <td align="right">工商注册号：</td>
                        <td><input class="easyui-textbox" type="text" id="addPayCoopBankRegNo" name="regNo" missingMessage="请输入工商注册号"
                                data-options="required:true"/></td>
                        <td align="right">银行法人代表：</td>
                        <td><input class="easyui-textbox" type="text" id="addPayCoopBankLegRep" name="legRep" missingMessage="请输入银行法人代表"
                                 data-options="required:true"/></td>
                    </tr>
                    <tr>
                        <td align="right">客户经理：</td>
                        <td><input class="easyui-textbox" type="text" id="addPayCoopBankCustMgr" name="custMgr" missingMessage="请输入客户经理"
                                data-options="required:true"/></td>
                        <td align="right">联系电话：</td>
                        <td><input class="easyui-textbox" type="text" id="addPayCoopBankTelNo" name="telNo" missingMessage="请输入联系电话"
                                validType="length[1,20]" invalidMessage="联系电话为1-20位字符" data-options="required:true"/></td>
                    </tr>
                    <tr>
                        <td align="right">Email地址：</td>
                        <td><input class="easyui-textbox" type="text"  id="addPayCoopBankBankEmail" name="bankEmail" missingMessage="请输入Email地址"
                                validType="email" invalidMessage="请输入正确邮箱，最多50位字符" data-options="required:true"/></td>
                        <td align="right">通信地址：</td>
                        <td><input class="easyui-textbox" type="text" id="addPayCoopBankBankAddress" name="bankAddress" missingMessage="请输入通信地址"
                            validType="length[1,20]" invalidMessage="通信地址为1-20个字符"  data-options="required:true"/></td>
                    </tr>
                    <tr>
                        <td align="right">线上退款：</td>
                        <td>
                            <select name="refundOnline" id="addPayCoopBankRefundOnline" class="easyui-combobox"  data-options="required:true">
		                         <option value="0">支持</option>  
		                         <option value="1" selected>不支持</option>  
		                    </select> 
                        </td>
                        <td align="right">线上对账：</td>
                        <td>
                            <select name="accountOnline" id="addPayCoopBankAccountOnline" class="easyui-combobox"  data-options="required:true">
		                         <option value="0">支持</option>  
		                         <option value="1" selected>不支持</option> 
		                    </select> 
                        </td>
                    </tr>
                    <tr>
                        <td align="right">快捷支持重发验证码：</td>
                        <td>
                            <select name="quickResendSms" id="addPayCoopBankquickResendSms" class="easyui-combobox"  data-options="required:true">
		                         <option value="0">支持</option>  
		                         <option value="1" selected>不支持</option>  
		                    </select> 
                        </td>
                        <td align="right">结算周期：</td>
                        <td><select name="stlTimeType" id="stlTimeType" class="easyui-combobox" data-options="required:true">
								<option value="D" >按天</option>
							</select>
							<select name="stlTime" id="stlTime" class="easyui-combobox" data-options="required:true">
		                        <%for(int i=0; i<=30; i++){ 
			                		%><option value="<%=i %>" <%=i==1?"selected":"" %>>T+<%=i %></option><%
			                	} %>
		                    </select>
						</td>
                    </tr>
                    <tr>
                        <td colspan="2"><strong>消费费率（最大额为单笔交易上限）</strong></td>
                        <td></td>
                        <td></td>
                    </tr>
                    <tr>
						<td align="right" width="120">B2C借记卡：</td>
			     		<td width="250"><select name="b2cjjFeeCode" id=b2cjjFeeCode style="width:200px" class="easyui-combobox" validType="inputExistInCombobox['b2cjjFeeCode']">
									<%for(int i = 0; i<feeList.size(); i++){
										PayFeeRate fee = (PayFeeRate)feeList.get(i);
										if(!com.PayConstant.CUST_TYPE_PAY_CHANNEL.equals(fee.custType)||!"1".equals(fee.tranType))continue;
										%><option value="<%=fee.feeCode %>"><%=fee.feeName %></option><%
										} %>
								</select>
								<!-- 最大额<input class="easyui-numberbox" type="text" name="PAY_CHANNEL_MAX_AMT_7" id="PAY_CHANNEL_MAX_AMT_7" style="width:70px" max="9999999" data-options="required:true" value="0"/> -->
			     		</td>
			     		<td align="right" width="120">B2C信用卡：</td>
			     		<td><select name="b2cxyFeeCode" id=b2cxyFeeCode style="width:200px" class="easyui-combobox" validType="inputExistInCombobox['b2cxyFeeCode']">
									<%for(int i = 0; i<feeList.size(); i++){
										PayFeeRate fee = (PayFeeRate)feeList.get(i);
										if(!com.PayConstant.CUST_TYPE_PAY_CHANNEL.equals(fee.custType)||!"1".equals(fee.tranType))continue;
										%><option value="<%=fee.feeCode %>"><%=fee.feeName %></option><%
										} %>
								</select> 
								<!-- 最大额<input class="easyui-numberbox" type="text" name="PAY_CHANNEL_MAX_AMT_8" id="PAY_CHANNEL_MAX_AMT_8" style="width:70px" max="9999999" data-options="required:true" value="0"/> -->
			     		</td>
			    	</tr>
			    	<tr>
						<td align="right" width="120">B2B：</td>
			     		<td width="250"><select name="b2bFeeCode" id=b2bFeeCode style="width:200px" class="easyui-combobox" validType="inputExistInCombobox['b2bFeeCode']">
									<%for(int i = 0; i<feeList.size(); i++){
										PayFeeRate fee = (PayFeeRate)feeList.get(i);
										if(!com.PayConstant.CUST_TYPE_PAY_CHANNEL.equals(fee.custType)||!"1".equals(fee.tranType))continue;
										%><option value="<%=fee.feeCode %>"><%=fee.feeName %></option><%
										} %>
								</select>
								<!-- 最大额<input class="easyui-numberbox" type="text" name="PAY_CHANNEL_MAX_AMT_9" id="PAY_CHANNEL_MAX_AMT_9" style="width:70px" max="9999999" data-options="required:true" value="0"/> -->
			     		</td>
			     		<td align="right" width="120">快捷借记卡：</td>
			     		<td><select name="kjJJFeeCode" id=kjJJFeeCode style="width:200px" class="easyui-combobox" validType="inputExistInCombobox['kjJJFeeCode']">
									<%for(int i = 0; i<feeList.size(); i++){
										PayFeeRate fee = (PayFeeRate)feeList.get(i);
										if(!com.PayConstant.CUST_TYPE_PAY_CHANNEL.equals(fee.custType)||!"1".equals(fee.tranType))continue;
										%><option value="<%=fee.feeCode %>"><%=fee.feeName %></option><%
										} %>
								</select>
								<!-- 最大额<input class="easyui-numberbox" type="text" name="PAY_CHANNEL_MAX_AMT_11" id="PAY_CHANNEL_MAX_AMT_11" style="width:70px" max="9999999" data-options="required:true" value="0"/> -->
			     		</td>
			    	</tr>
			    	<tr>
			     		<td align="right" width="120" valign="top">快捷信用卡：</td>
			     		<td width="250" valign="top"><select name="kjDJFeeCode" id=kjDJFeeCode style="width:200px" class="easyui-combobox" validType="inputExistInCombobox['kjDJFeeCode']">
									<%for(int i = 0; i<feeList.size(); i++){
										PayFeeRate fee = (PayFeeRate)feeList.get(i);
										if(!com.PayConstant.CUST_TYPE_PAY_CHANNEL.equals(fee.custType)||!"1".equals(fee.tranType))continue;
										%><option value="<%=fee.feeCode %>"><%=fee.feeName %></option><%
										} %>
								</select>
								<!-- 最大额<input class="easyui-numberbox" type="text" name="PAY_CHANNEL_MAX_AMT_12" id="PAY_CHANNEL_MAX_AMT_12" style="width:70px" max="9999999" data-options="required:true" value="0"/> -->
			   			</td>
			   			<td align="right" width="120" valign="top">支付宝扫码：</td>
			     		<td>
			     		<select name="addSupportedZFBScanFlag">
							<option value="0">不支持</option>
							<option value="1">支持</option>
						</select>
			     		单笔最大额<input class="easyui-numberbox" type="text" name="PAY_CHANNEL_MAX_AMT_17" id="PAY_CHANNEL_MAX_AMT_17" style="width:70px" max="9999999" data-options="required:true" value="0"/>0为无限制<br/><br/>
			     		<select name="zfbFeeCode" id=zfbFeeCode style="width:200px" class="easyui-combobox" validType="inputExistInCombobox['zfbFeeCode']">
							<%for(int i = 0; i<feeList.size(); i++){
								PayFeeRate fee = (PayFeeRate)feeList.get(i);
								if(!com.PayConstant.CUST_TYPE_PAY_CHANNEL.equals(fee.custType)||!"1".equals(fee.tranType))continue;
								%><option value="<%=fee.feeCode %>"><%=fee.feeName %></option><%
								} %>
						</select>
			     		</td>
			   		</tr>
			   		<tr>
						<td align="right" width="120" valign="top">微信扫码：</td>
			     		<td width="250">
			     		<select name="addSupportedWeixinScanFlag">
							<option value="0">不支持</option>
							<option value="1">支持</option>
						</select>
			     		单笔最大额<input class="easyui-numberbox" type="text" name="PAY_CHANNEL_MAX_AMT_10" id="PAY_CHANNEL_MAX_AMT_10" style="width:70px" max="9999999" data-options="required:true" value="0"/>0无限制<br/><br/>
			     		<select name="yhkFeeCode" id=yhkFeeCode style="width:200px" class="easyui-combobox" validType="inputExistInCombobox['yhkFeeCode']">
							<%for(int i = 0; i<feeList.size(); i++){
								PayFeeRate fee = (PayFeeRate)feeList.get(i);
								if(!com.PayConstant.CUST_TYPE_PAY_CHANNEL.equals(fee.custType)||!"1".equals(fee.tranType))continue;
								%><option value="<%=fee.feeCode %>"><%=fee.feeName %></option><%
								} %>
						</select>
			     		</td>
			     		<td align="right" width="120" valign="top">微信WAP：</td>
			     		<td>
			     		<select name="addSupportedWeixinWAPFlag">
							<option value="0">不支持</option>
							<option value="1">支持</option>
						</select>
			     		单笔最大额<input class="easyui-numberbox" type="text" name="PAY_CHANNEL_MAX_AMT_18" id="PAY_CHANNEL_MAX_AMT_18" style="width:70px" max="9999999" data-options="required:true" value="0"/>0无限制<br/><br/>
			     		<select name="wxwapFeeCode" id=wxwapFeeCode style="width:200px" class="easyui-combobox" validType="inputExistInCombobox['wxwapFeeCode']">
							<%for(int i = 0; i<feeList.size(); i++){
								PayFeeRate fee = (PayFeeRate)feeList.get(i);
								if(!com.PayConstant.CUST_TYPE_PAY_CHANNEL.equals(fee.custType)||!"1".equals(fee.tranType))continue;
								%><option value="<%=fee.feeCode %>"><%=fee.feeName %></option><%
								} %>
						</select>
			     		</td>
			    	</tr>
			    	<tr>
			    		<td align="right" width="120" valign="top">QQ扫码：</td>
			    		<td width="250">
			    			<select name="addSupportedQQScanFlag">
								<option value="0">不支持</option>
								<option value="1">支持</option>
							</select>
				     		单笔最大额<input class="easyui-numberbox" type="text" name="PAY_CHANNEL_MAX_AMT_27" id="PAY_CHANNEL_MAX_AMT_27" style="width:70px" max="9999999" data-options="required:true" value="0"/>0无限制<br/><br/>
				     		<select name="qqFeeCode" id=qqFeeCode style="width:200px" class="easyui-combobox" validType="inputExistInCombobox['qqFeeCode']">
								<%for(int i = 0; i<feeList.size(); i++){
									PayFeeRate fee = (PayFeeRate)feeList.get(i);
									if(!com.PayConstant.CUST_TYPE_PAY_CHANNEL.equals(fee.custType)||!"1".equals(fee.tranType))continue;
									%><option value="<%=fee.feeCode %>"><%=fee.feeName %></option><%
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
						<td align="right"  width="120">转账费率：</td>
			   			<td><select name="zzFeeCode" id=zzFeeCode style="width:200px" class="easyui-combobox" validType="inputExistInCombobox['zzFeeCode']">
									<%for(int i = 0; i<feeList.size(); i++){
										PayFeeRate fee = (PayFeeRate)feeList.get(i); 
										if(!com.PayConstant.CUST_TYPE_PAY_CHANNEL.equals(fee.custType)||!"6".equals(fee.tranType))continue;
										%><option value="<%=fee.feeCode %>"><%=fee.feeName %></option><%
										} %>
								</select> 
			   			</td>
						<td align="right"  width="120">退款费率：</td>
			   			<td><select name="tkFeeCode" id=tkFeeCode style="width:200px" class="easyui-combobox" validType="inputExistInCombobox['tkFeeCode']">
									<%for(int i = 0; i<feeList.size(); i++){
										PayFeeRate fee = (PayFeeRate)feeList.get(i); 
									    if(!com.PayConstant.CUST_TYPE_PAY_CHANNEL.equals(fee.custType)||!"4".equals(fee.tranType))continue;
										%><option value="<%=fee.feeCode %>"><%=fee.feeName %></option><%
										} %>
								</select> 
			   			</td>
			   		</tr>
			   		<tr>
						<td align="right"  width="120">提现费率：</td>
			   			<td><select name="txFeeCode" id=txFeeCode style="width:200px" class="easyui-combobox" validType="inputExistInCombobox['txFeeCode']">
									<%for(int i = 0; i<feeList.size(); i++){
										PayFeeRate fee = (PayFeeRate)feeList.get(i); 
										if(!com.PayConstant.CUST_TYPE_PAY_CHANNEL.equals(fee.custType)||!"5".equals(fee.tranType))continue;
										%><option value="<%=fee.feeCode %>"><%=fee.feeName %></option><%
										}%>
								</select> 
			   			</td>
						<td align="right"  width="120">代收费率：</td>
			   			<td><select name="dsFeeCode" id=dsFeeCode style="width:200px" class="easyui-combobox" validType="inputExistInCombobox['dsFeeCode']">
									<%for(int i = 0; i<feeList.size(); i++){
										PayFeeRate fee = (PayFeeRate)feeList.get(i); 
										if(!com.PayConstant.CUST_TYPE_PAY_CHANNEL.equals(fee.custType)||!"15".equals(fee.tranType))continue;
										%><option value="<%=fee.feeCode %>"><%=fee.feeName %></option><%
										} %>
								</select>
								<!-- 最大额<input class="easyui-numberbox" type="text" name="PAY_CHANNEL_MAX_AMT_15" id="PAY_CHANNEL_MAX_AMT_15" style="width:70px" max="9999999" data-options="required:true" value="0"/> -->
			   			</td>
			   		</tr>
			   		<tr>
						<td align="right"  width="120">代付费率：</td>
			   			<td><select name="dfFeeCode" id=dfFeeCode style="width:200px" class="easyui-combobox" validType="inputExistInCombobox['dfFeeCode']">
									<%for(int i = 0; i<feeList.size(); i++){
										PayFeeRate fee = (PayFeeRate)feeList.get(i); 
										if(!com.PayConstant.CUST_TYPE_PAY_CHANNEL.equals(fee.custType)||!"16".equals(fee.tranType))continue;
										%><option value="<%=fee.feeCode %>"><%=fee.feeName %></option><%
										}%>
								</select> 
			   			</td>
						<td align="right"  width="120"></td>
			   			<td>
			   			</td>
			   		</tr>
                    <tr>
						<td align="right" valign="top">支持银行：</td>
						<td colspan="3">
						<table style="border:solid #add9c0; border-width:1px 0px 0px 1px;">
							<tr>
								<td colspan="7"  bgcolor="#CCCCCC" style="border:solid #add9c0; border-width:0px 1px 1px 0px;">
									<input type="checkbox" onclick="zhifufangshiWangYin()" id="wangyin"/><strong>网银支付（最大限额0为无限制，单位：元）</strong>
								</td>
							</tr>
							<tr>
							<%
							for(int i = 0 ; i < PayCoopBankService.BANK_CODE_NAME_LIST.size(); i++) {
								PayBank payBank = PayCoopBankService.BANK_CODE_NAME_LIST.get(i);%>
								<td style="border:solid #add9c0; border-width:0px 1px 1px 0px;">
								<input type="checkbox" onclick="selectChannelForAdd(this)" id="<%=payBank.bankCode%>_CODE_add"
									 name="payBankCodes" value="<%=payBank.bankCode%>"/><strong><%=payBank.bankName%></strong><br/>
								<input type="checkbox" id="<%=payBank.bankCode%>UserType0" title="<%=payBank.bankCode%>" name="<%=payBank.bankCode%>UserType0" value="0"  onclick="selectChannelUserTypeForAdd(this)"/>借记卡
								<input class="easyui-numberbox" type="text" name="webDebitMaxAmt_<%=payBank.bankCode%>" style="width:70px" max="9999999" data-options="required:true" value="0"/><br/>
								<input type="checkbox" id="<%=payBank.bankCode%>UserType1" title="<%=payBank.bankCode%>" name="<%=payBank.bankCode%>UserType1" value="1"  onclick="selectChannelUserTypeForAdd(this)"/>贷记卡
								<input class="easyui-numberbox" type="text" name="webCreditMaxAmt_<%=payBank.bankCode%>" style="width:70px" max="9999999" data-options="required:true" value="0"/><br/>
								<input type="checkbox" id="<%=payBank.bankCode%>UserType4" title="<%=payBank.bankCode%>" name="<%=payBank.bankCode%>UserType4" value="4"  onclick="selectChannelUserTypeForAdd(this)"/>对&nbsp;&nbsp;&nbsp;&nbsp;公
								<input class="easyui-numberbox" type="text" name="webPublicMaxAmt_<%=payBank.bankCode%>" style="width:70px" max="9999999" data-options="required:true" value="0"/><br/>
								</td><%
								if((i+1)%7==0)out.println("</tr><tr>");
							}%>
							</tr>
						</table><br/>
						<script type="text/javascript">
						function selectChannelForAdd(channel){
							if(channel.checked){
								document.getElementById(channel.value+"UserType0").checked=true;
								document.getElementById(channel.value+"UserType1").checked=true;
								document.getElementById(channel.value+"UserType4").checked=true;
							} else {
								document.getElementById(channel.value+"UserType0").checked=false;
								document.getElementById(channel.value+"UserType1").checked=false;
								document.getElementById(channel.value+"UserType4").checked=false;
							}
							checkWangYinSelectForAdd();
						}
						function selectChannelUserTypeForAdd(cUserType){
							document.getElementById(cUserType.title+"_CODE_add").checked=false;
							if(document.getElementById(cUserType.title+"UserType0").checked 
								||document.getElementById(cUserType.title+"UserType1").checked
								||document.getElementById(cUserType.title+"UserType4").checked)
								document.getElementById(cUserType.title+"_CODE_add").checked=true;
							checkWangYinSelectForAdd();
						}
						function checkWangYinSelectForAdd(){
							document.getElementById("wangyin").checked=false;
							document.getElementById("payWebFlagId").value="1";
							for(var i=0; i<banks.length; i++){
								var bank = document.getElementById(banks[i]+"_CODE_add");
								if(bank.checked){
									document.getElementById("wangyin").checked=true;
									document.getElementById("payWebFlagId").value="0";
									return;
								}
							}
						}
						</script>
						<table style="border:solid #add9c0; border-width:1px 0px 0px 1px;">
							<tr>
								<td colspan="7"  bgcolor="#CCCCCC" style="border:solid #add9c0; border-width:0px 1px 1px 0px;">
									<input type="checkbox" onclick="zhifufangshiKuaiJie()" id="kuaijie"/><strong>快捷支付（最大限额0为无限制，单位：元）</strong>
								</td>
							</tr>
							<tr>
							<%
							for(int i = 0 ; i < PayCoopBankService.BANK_CODE_NAME_LIST.size(); i++) {
								PayBank payBank = PayCoopBankService.BANK_CODE_NAME_LIST.get(i);%>
								<td style="border:solid #add9c0; border-width:0px 1px 1px 0px;">
								<input type="checkbox" onclick="selectQuickChannelForAdd(this)"
									 name="payBankCodesQuick" value="<%=payBank.bankCode%>" id="<%=payBank.bankCode%>_CODE_add_quick"/><strong><%=payBank.bankName%></strong><br/>
								<input type="checkbox" id="<%=payBank.bankCode%>UserTypeQuick0" title="<%=payBank.bankCode%>" name="<%=payBank.bankCode%>UserTypeQuick0" value="0"  onclick="selectQuickChannelUserTypeForAdd(this)"/>借记卡
								<input class="easyui-numberbox" type="text" name="quickDebitMaxAmt_<%=payBank.bankCode%>" style="width:70px" max="9999999" data-options="required:true" value="0"/><br/>
								<input type="checkbox" id="<%=payBank.bankCode%>UserTypeQuick1" title="<%=payBank.bankCode%>" name="<%=payBank.bankCode%>UserTypeQuick1" value="1"  onclick="selectQuickChannelUserTypeForAdd(this)"/>贷记卡
								<input class="easyui-numberbox" type="text" name="quickCeditMaxAmt_<%=payBank.bankCode%>" style="width:70px" max="9999999" data-options="required:true" value="0"/><br/>
								</td>								
								<%
								if((i+1)%7==0)out.println("</tr><tr>");
							}%>
							</tr>
						</table>
						<script type="text/javascript">
						function selectQuickChannelForAdd(channel){
							if(channel.checked){
								document.getElementById(channel.value+"UserTypeQuick0").checked=true;
								document.getElementById(channel.value+"UserTypeQuick1").checked=true;
							} else {
								document.getElementById(channel.value+"UserTypeQuick0").checked=false;
								document.getElementById(channel.value+"UserTypeQuick1").checked=false;
							}
							checkQuaiJieSelectForAdd();
						}
						function selectQuickChannelUserTypeForAdd(cUserType){
							document.getElementById(cUserType.title+"_CODE_add_quick").checked=false;
							if(document.getElementById(cUserType.title+"UserTypeQuick0").checked 
								||document.getElementById(cUserType.title+"UserTypeQuick1").checked)
								document.getElementById(cUserType.title+"_CODE_add_quick").checked=true;
							checkQuaiJieSelectForAdd();
						}
						function checkQuaiJieSelectForAdd(){
							document.getElementById("kuaijie").checked=false;
							document.getElementById("payQuickFlagId").value="1";
							for(var i=0; i<banks.length; i++){
								var bank = document.getElementById(banks[i]+"_CODE_add_quick");
								if(bank.checked){
									document.getElementById("payQuickFlagId").value="0";
									document.getElementById("kuaijie").checked=true;
									return;
								}
							}
						}
						</script>
						<br/>
						<table style="border:solid #add9c0; border-width:1px 0px 0px 1px;">
							<tr>
								<td colspan="7"  bgcolor="#CCCCCC" style="border:solid #add9c0; border-width:0px 1px 1px 0px;">
									<input type="checkbox" onclick="zhifufangshiReceive()" id="receive"/><strong>代收（最大限额0为无限制，单位：元）</strong>
								</td>
							</tr>
							<tr>
							<%
							for(int i = 0 ; i < PayCoopBankService.BANK_CODE_NAME_LIST.size(); i++) {
								PayBank payBank = PayCoopBankService.BANK_CODE_NAME_LIST.get(i);%>
								<td style="border:solid #add9c0; border-width:0px 1px 1px 0px;">
								<input type="checkbox" onclick="selectReceiveChannelForAdd(this)"
									 name="payBankCodesReceive" value="<%=payBank.bankCode%>" id="<%=payBank.bankCode%>_CODE_add_receive"/><strong><%=payBank.bankName%></strong><br/>
								<input class="easyui-numberbox" type="text" name="receiveMaxAmt_<%=payBank.bankCode%>" style="width:70px" max="9999999" data-options="required:true" value="0"/><br/>
								<%
								if((i+1)%7==0)out.println("</tr><tr>");
							}%>
							</tr>
						</table>
						<script type="text/javascript">
						function selectReceiveChannelForAdd(channel){
							try{
							document.getElementById("payReceiveFlagId").value="1";
							document.getElementById("receive").checked=false;
							for(var i=0; i<banks.length; i++){
								var bank = document.getElementById(banks[i]+"_CODE_add_receive");
								if(bank.checked){
									document.getElementById("payReceiveFlagId").value="0";
									document.getElementById("receive").checked=true;
									return;
								}
							}
							} catch (e){alert(e);}
						}
						</script>
						<br/>
						<table style="border:solid #add9c0; border-width:1px 0px 0px 1px;">
							<tr>
								<td colspan="7"  bgcolor="#CCCCCC" style="border:solid #add9c0; border-width:0px 1px 1px 0px;">
									<input type="checkbox" onclick="zhifufangshiWithdraw()" id="withdraw"/><strong>代付</strong>
								</td>
							</tr>
							<tr>
							<%
							for(int i = 0 ; i < PayCoopBankService.BANK_CODE_NAME_LIST.size(); i++) {
								PayBank payBank = PayCoopBankService.BANK_CODE_NAME_LIST.get(i);%>
								<td style="border:solid #add9c0; border-width:0px 1px 1px 0px;">
								<input type="checkbox" onclick="selectWithdrawChannelForAdd(this)"
									 name="payBankCodesWithdraw" value="<%=payBank.bankCode%>" id="<%=payBank.bankCode%>_CODE_add_withdraw"/><strong><%=payBank.bankName%></strong><br/><%
								if((i+1)%7==0)out.println("</tr><tr>");
							}%>
							</tr>
						</table>
						<script type="text/javascript">
						function selectWithdrawChannelForAdd(channel){
							document.getElementById("payWithdrawFlagId").value="1";
							document.getElementById("withdraw").checked=false;
							for(var i=0; i<banks.length; i++){
								var bank = document.getElementById(banks[i]+"_CODE_add_withdraw");
								if(bank.checked){
									document.getElementById("payWithdrawFlagId").value="0";
									document.getElementById("withdraw").checked=true;
									return;
								}
							}
						}
						</script>
						</td>
					</tr>
                    <tr>
                        <td align="right" valign="top">备注信息：</td>
                        <td colspan="3">
                            <input class="easyui-textbox" type="text" id="addPayCoopBankRemark" name="remark"  missingMessage="备注信息请控制在50个字"
                          	validType="length[0,500]" invalidMessage="备注信息为1-50个字符" data-options="multiline:true" style="width:240px;height:70px"/>
                          	</td>
                    </tr>
            </table>
        </form>
    </div>
    <div data-options="region:'south',border:false" style="text-align:right;padding:5px;">
        <a class="easyui-linkbutton" id="savePayCoopBank" data-options="iconCls:'icon-ok'" href="javascript:;" style="width:80px">确定</a>
        <a class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" href="javascript:void(0)" 
            onclick="$('#addPayCoopBankForm').form('clear')" style="width:80px">取消</a>
    </div>
</div>
<script type="text/javascript">
 //为保存按钮绑定事件

function validBankCode(bankCode){
	var result = "yes";
	$.ajax({
		url:"<%=path %>/validBankCode.htm",
		type:"post",
		async:false,
		data:{
			bankCode:bankCode
		},
		dataType:"text",
		success:function(responseText){
			result = responseText;
		},
		error:function(){
			alert("系统错误");
		}
	});
	return result;
}
 
$(function(){
	$("#savePayCoopBank").click(function(){
		$("#addPayCoopBankForm").submit();
	}); 
	$('#addPayCoopBankForm').form({
		    url:'<%=path %>/addPayCoopBank.htm',
		    onSubmit: function(){
		        var addPayCoopBankCheck=$(this).form('validate');
		        return addPayCoopBankCheck;
		    },
		    success:function(data){
		    	//data为添加成功后响应的数据
		        if(data=='<%=JWebConstant.OK %>'){
		           topCenterMessage('<%=JWebConstant.OK %>','添加成功！');
		           closeTabFreshList('payCoopBank',payCoopBankAddPageTitle,payCoopBankListPageTitle,'payCoopBankList','<%=path %>/payCoopBank.htm');
		        } else topCenterMessage('<%=JWebConstant.ERROR %>',data);
		    }
		});
	
	$.extend($.fn.textbox.defaults.rules, {
		checkPayCoopBankBankCode: {
	  		validator: function (value, param) {
	  			if(value.length<0 || value.length>10){
	   			$.fn.textbox.defaults.rules.checkPayCoopBankBankCode.message = '银行编码为1-10位字符';
	   			return false;
	   		} else {
	   			var result = $.ajax({
					url: '<%=path %>/validBankCode.htm',
					data:{bankCode:value},
					type: 'post',
					dataType: 'text',
					async: false,
					cache: false
				}).responseText;
				if(result == '0')return true;
				$.fn.textbox.defaults.rules.checkPayCoopBankBankCode.message = '银行编码已存在';
				return false; 
	   		}
	  		},
		message: ''
	  }
	});
});
/* var intervalId = window.setInterval("am()", 300);
function am(){
	if($("#addPayCoopBankBankCode").next("span").find("input[type=text]").length>0){
		window.clearInterval(intervalId);
		$("#addPayCoopBankBankCode").next("span").find("input[type=text]").blur(function(){
			//在这里添加blur逻辑
			var result = validBankCode($("#addPayCoopBankBankCode").val());
			if(result = "no"){
				$("#addPayCoopBankBankCode").next().next("span").html("<font color='red'>银行编码已存在</font>");
				return false;
			}else{
				$("#addPayCoopBankBankCode").next().next("span").html("");
			}
		});
	}
} */
</script>
