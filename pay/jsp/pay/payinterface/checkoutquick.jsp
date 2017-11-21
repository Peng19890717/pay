<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ page import="com.PayConstant"%>
<%@ page import="com.pay.coopbank.service.PayCoopBankService"%>
<%@ page import="com.pay.merchantinterface.service.PayRequest"%>
<%@ page import="com.pay.bank.dao.PayBank"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="com.pay.user.dao.PayTranUserInfo"%>
<%@ page import="com.pay.merchantinterface.dao.PayTranUserQuickCard"%>
<%
PayRequest payRequest = (PayRequest)session.getAttribute("payRequest");
if(payRequest == null){
	out.println("支付请求不存在");
	return;
}
//过滤支持借记卡、信用卡的银行
List<PayBank>list = PayCoopBankService.SUPPORTED_BANK_LIST_QUICK;
//结算方式 0自动结算到虚拟账户 1线下打款 2自动结算到银行账户 3实时结算到虚拟账户  4实时结算到银行账户
if("3".equals(payRequest.merchant.settlementWay)||"4".equals(payRequest.merchant.settlementWay)){//实时结算通道
	list = PayCoopBankService.SUPPORTED_BANK_LIST_REAL_TIME_QUICK;
}
String path = request.getContextPath();
String info = (String)(request.getAttribute("info")==null?"":request.getAttribute("info"));
String banksInfo="";
for(int i=0; list !=null &&i<list.size(); i++){
	PayBank bank = list.get(i);
	banksInfo = banksInfo + bank.bankName+"，";
}
if(banksInfo.length()>0)banksInfo = banksInfo.substring(0,banksInfo.length()-1);
List<PayTranUserQuickCard> cardList = (List)request.getAttribute("bindCardList");
if("mobile".equals(session.getAttribute("client-type"))){
%>
	<!DOCTYPE html>
	<html><head><meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=no, minimum-scale=1.0, maximum-scale=1.0">
	<style>
	.spanClass .textbox {
		  position: relative;
		  border: 0px solid #95B8E7;
		  background-color: #fff;
		  vertical-align: middle;
		  display: inline-block;
		  overflow: hidden;
		  white-space: nowrap;
		  margin: 0;
		  padding: 0;
		  -moz-border-radius: 5px 5px 5px 5px;
		  -webkit-border-radius: 5px 5px 5px 5px;
		  border-radius: 5px 5px 5px 5px;
		}
	</style>
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/js/jquery-easyui-1.4/themes/default/easyui.css">
	<script type="text/javascript" src="<%=request.getContextPath() %>/js/jquery-easyui-1.4/jquery.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath() %>/js/jquery-easyui-1.4/jquery.easyui.min.js"></script>
	<link rel="stylesheet" type="text/css" href="<%=path %>/jsp/pay/payinterface/checkoutquick_1.css">
	<title>快捷支付收银台-移动版</title>
	<link href="css/cash.css" type="text/css" rel="stylesheet">
	</head>
	 <body>
	<div class="warpper">
	    <header id="header_weixin">
	        <span>快捷收银台</span>
	    </header>
	    <div class="main">
	    	<div class="put" style="border-bottom:solid 1px #dcdcdc; color:#434343;" >
	            <span class="tit">商品名称：</span>
	            <span class="in" style="font-size:15px;"><%=com.PayConstant.PAY_CONFIG.get("COMMON_PRODUCT_DESC") %></span>
	            <%-- <span class="in" style="font-size:15px;">${ payRequest.merchantOrderDesc }</span> --%>
	        </div> 
	        <div class="put" style="border-bottom:solid 1px #dcdcdc; color:#434343;" >
	            <span class="tit">订单编号：</span>
	            <span class="in" style="font-size:15px;">${ payRequest.merchantOrderId }</span>
	        </div>
	        <div class="put" style="border-bottom:solid 1px #dcdcdc; color:#f38520;" >
	            <span class="tit">应付金额：</span>
	            <span class="in" style="font-size:24px;" ><fmt:formatNumber value="${ payRequest.merchantOrderAmt / 100 }" type="currency" pattern=",##0.00#"/>元</span>
	        </div> 
		<form action="${ pageContext.request.contextPath }/createOrderForQuickPayH5.htm" method="post" id="confirmThePaymentFormForH5">
	    <section class="con">  
			<div class="put" style="border-bottom:solid 1px #dcdcdc;" >
	            <span class="tit">银行卡号：</span>
	            <span class="in">
					<!-- <input type="text" id="cardNo" name="cardNo" onblur="loadBindInfo(this.value)" value=""/> -->
					<script language="javascript">
					var cardNoes= [
						<%
				   	for(int i=0; cardList!=null&&i<cardList.size(); i++){
						PayTranUserQuickCard card =cardList.get(i); 
						%>
						{
					        "label":"<%=card.cardNo %>",
					        "value":"<%=card.cardNo+(i==0?",selected:true":"") %>"
					    }<%=i!=cardList.size()-1?",":"" %>
					<%}%>];
					</script>
				   <!--  <input type="text" class="inp01" id="cardNo" name="cardNo" onblur="loadBindInfo(this.value)"/> -->
				   <input type="hidden" id="cardNo" name="cardNo"/>
				   <span class="spanClass">
				   <input type="text" id="cardNoTmp" name="cardNoTmp" class="easyui-combobox"  style="width:200px"
				   		value="<%=cardList!=null&&cardList.size()>0?cardList.get(0).cardNo:"" %>" 
				   		data-options="panelHeight:'auto',prompt:'请输入银行卡号',events:{blur:loadBindInfo},valueField: 'value',textField: 'label',data:cardNoes"/>
				   	</span>
	            </span>
	        </div>
	        <div class="put" id="cardInfoBox" style="border-bottom:solid 1px #dcdcdc;display:none" >
	        	<span class="tit">卡信息：</span>
           		<span class="in" id="cardInfo" style="font-size:15px;"></span>
      		</div> 
	    	<div class="put" style="border-bottom:solid 1px #dcdcdc;" >
	            <span class="tit">身份证号：</span>
	            <span class="in">
					<input type="text" maxlength="18" id="credentialNo" name="credentialNo" value=""/>
	            </span>
	        </div> 
	    	<div class="put" style="border-bottom:solid 1px #dcdcdc;" >
	            <span class="tit">姓名：</span>
	            <span class="in">
	               <input type="text" maxlength="15" id="name" name="name" value="">
	            </span>
	        </div>
			<div class="put" style="border-bottom:solid 1px #dcdcdc;" >
	            <span class="tit">手机号：</span>
	            <span class="in">
	               <input type="text" maxlength="11" id="mobileNo" name="mobileNo" value="" placeholder="银行预留的手机号"/>
	            </span>
	        </div>
	        <div class="put" style="border-bottom:solid 1px #dcdcdc;display:none;" id="cvv2Box">
	            <span class="tit">CVV2：</span>
	            <span class="in">
	               <input type="text" maxlength="3" id="cvv2" name="cvv2" placeholder="信用卡背面的3位数字"/>
	            </span>
	        </div>
	        <div class="put" style="border-bottom:solid 1px #dcdcdc;display:none;" id="validPeriodBox">
	            <span class="tit">卡有效期：</span>
	            <span class="in">
	               <input type="text" maxlength="4" id="validPeriod" name="validPeriod"/>
	            </span>
	        </div>
	        <p class="warn"><span id="payInfo"><%=info %></span></p>
	        <%if(banksInfo.length()>0){ %>
	        <div class="put" style="border-bottom:solid 1px #dcdcdc;" >
	            <span class="tit"></span>
	            <span class="in"  style="font-size:15px;">支持银行：<%=banksInfo %></span>
	        </div>
	        <%} %>
	    </section>
		</form>
	 
	</div>
	<input type="submit" class="btn btn3" name="pay" id="pay" onclick="payFormNext()" value="下一步">
	</div>
	</body></html>
	<script type="text/javascript">
	var cardType = "";
	var cardNoDesc = "";
	var loadBindInfoHandle;
	function loadBindInfo(){
		loadBindInfoHandle = setInterval(loadBindInfoRun, 200);
	}
	function loadBindInfoRun(){
		clearInterval(loadBindInfoHandle);
		var cardNo = $('#cardNoTmp').combobox('getText');
		document.getElementById("payInfo").innerHTML="";
		cardType="";
		cardNoDesc="";
		document.getElementById("cardInfoBox").style.display="none";
		document.getElementById("cardInfo").innerHTML="";
		document.getElementById("payInfo").innerHTML="";
		clearAllValue();
		document.getElementById("cvv2Box").style.display="none";
		document.getElementById("validPeriodBox").style.display="none";
		if(cardNo.length<10){
			document.getElementById("cardInfoBox").style.display="none";
			document.getElementById("cardInfo").innerHTML="";
			document.getElementById("payInfo").innerHTML="卡号不正确";
			return;
		}
		$.ajax({
			url:"<%=path %>/getBindCardByNo.htm",
			type:"post",
			async:false,
			data:{cardNo:cardNo},
			dataType:"json",
			success:function(responseText){
				if(responseText.cardInfo==""){
					cardNoDesc="不支持的卡号";
					document.getElementById("cardInfo").innerHTML="";
					document.getElementById("payInfo").innerHTML=cardNoDesc;
					return;
				}
				if(responseText.respDesc != undefined && responseText.respDesc!=""){
					document.getElementById("cardInfoBox").style.display="none";
					document.getElementById("cardInfo").innerHTML="";
					document.getElementById("payInfo").innerHTML=responseText.respDesc;
					cardNoDesc = responseText.respDesc;
					return;
				}
				cardType = responseText.cardType;
				if(cardType=="1"){//信用卡
					alert("不支持信用卡");
					return;
					document.getElementById("cvv2Box").style.display="";
					document.getElementById("validPeriodBox").style.display="";
				} else {
					document.getElementById("cvv2Box").style.display="none";
					document.getElementById("validPeriodBox").style.display="none";
				}
				if(responseText.cardInfo == undefined){
					document.getElementById("cardInfoBox").style.display="none";
					document.getElementById("cardInfo").innerHTML="";
				} else{
					document.getElementById("cardInfoBox").style.display="";
					document.getElementById("cardInfo").innerHTML=responseText.cardInfo;
				}
				if(responseText.credentialNo == undefined)document.getElementById("credentialNo").value="";
				else document.getElementById("credentialNo").value=responseText.credentialNo;
				if(responseText.name == undefined)document.getElementById("name").value="";
				else document.getElementById("name").value=responseText.name;
				if(responseText.mobileNo == undefined)document.getElementById("mobileNo").value="";
				else document.getElementById("mobileNo").value=responseText.mobileNo;
			},
			error:function(){
				alert("系统错误");
			}
		});
	}
	function clearAllValue(){
		document.getElementById("credentialNo").value="";
		document.getElementById("name").value="";
		document.getElementById("mobileNo").value="";
		document.getElementById("cvv2").value="";
		document.getElementById("validPeriod").value="";
	}
	function payFormNext(){
		document.getElementById("cardNo").value = $('#cardNoTmp').combobox('getText');
		document.getElementById("payInfo").innerHTML="";
		if(cardNoDesc != ""){
			document.getElementById("cardErrorInfo").innerHTML=cardNoDesc;
			return;
		}
		if(document.getElementById("cardNo").value.length<10){
			document.getElementById("payInfo").innerHTML="请正确输入卡号";
			return;
		}
		if(document.getElementById("credentialNo").value.length!=15&&document.getElementById("credentialNo").value.length!=18){
			document.getElementById("payInfo").innerHTML="请正确输入身份证号";
			return;
		}
		if(document.getElementById("name").value.length==0){
			document.getElementById("payInfo").innerHTML="请正确输入姓名";
			return;
		}
		if(document.getElementById("mobileNo").value.length!=11){
			document.getElementById("payInfo").innerHTML="请正确输入手机号";
			return;
		}
		if(cardType=="1"){
			if(document.getElementById("cvv2").value.length!=3){
				document.getElementById("payInfo").innerHTML="请正确输入cvv2";
				return;
			}
			if(document.getElementById("validPeriod").value.length!=4){
				document.getElementById("payInfo").innerHTML="请正确输入卡有效期";
				return;
			}
		}
		document.getElementById("confirmThePaymentFormForH5").submit();
	}
	<%
	if(cardList!=null&&cardList.size()>0){%>
		loadBindInfo();
	<%}%>
	</script>
	<%
} else {%>
	<!DOCTYPE html>
	<html>
	<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta http-equiv= "Pragma" content= "no-cache" />
	<meta http-equiv= "Cache-Control" content= "no-cache" />
	<meta http-equiv= "Expires" content= "0" />
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/js/jquery-easyui-1.4/themes/default/easyui.css">
	<script type="text/javascript" src="<%=request.getContextPath() %>/js/jquery-easyui-1.4/jquery.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath() %>/js/jquery-easyui-1.4/jquery.easyui.min.js"></script>
	<link rel="stylesheet" type="text/css" href="<%=path %>/jsp/pay/payinterface/checkoutquick.css">
	<title>快捷支付收银台-PC版</title>
	<style>
	</style>
	</head>
	<body>
		<div class="main">
		    <div class="header">
	        	<div class="headers">
	            	<div class="headersL">
	                    <div class="Cas">
	                    	<p>快捷收银台</p>
	                    </div>
	                </div>
	            </div>
	        </div>
	        <div class="content">
	        	<div class="cont">
	                <table width="100%" border="0" cellspacing="0" cellpadding="0" style="font-size:14px;">
						  <tr>
						    <td width="40%" valign="middle" align="left">
				            	<%--payRequest.merchantName.length()>0?"<p>商户名称："+payRequest.merchantName+"</p>":"" --%>
				                <p>商品名称：<%=com.PayConstant.PAY_CONFIG.get("COMMON_PRODUCT_DESC") %></p>
				                <%-- <p>商品名称：${ payRequest.merchantOrderDesc }</p> --%>
				                <p>订单编号：${ payRequest.merchantOrderId }</p>
				                <p>订单时间：<%=new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payRequest.curTime) %></p>
						    </td>
						    <td valign="top">
						    	<p>应付金额：</p>
	                    		<p><a href="javascript:;"><fmt:formatNumber value="${ payRequest.merchantOrderAmt / 100 }" type="currency" pattern=",##0.00#"/></a>元</p>
						    </td>
						  </tr>
					</table>
					<br/>
	            </div>
	            <div class="conts" style="margin-top:-1px;">
	                <div class="change">
	                    <div class="li00"  style="margin-top:-15px;">
		                    <form action="${ pageContext.request.contextPath }/createOrderForQuickPayH5.htm" method="post" id="confirmThePaymentFormForH5">
		                    	<table width="100%" border="0" cellspacing="0" cellpadding="0" style="font-size:14px;">
		                    	  <tr>
								    <td width="200" valign="middle" align="right">&nbsp;</td>
								    <td><font color="red" size="2"><span id="payInfo"><%=info %></span></font></td>
								  </tr>
								  <tr>
								    <td height="55" valign="middle" align="right">银行卡号：</td>
								    <td>
								    <script language="javascript">
									var cardNoes= [
										<%
								   	for(int i=0; cardList!=null&&i<cardList.size(); i++){
										PayTranUserQuickCard card =cardList.get(i); 
										%>
										{
									        "label":"<%=card.cardNo %>",
									        "value":"<%=card.cardNo+(i==0?",selected:true":"") %>"
									    }<%=i!=cardList.size()-1?",":"" %>
									<%}%>];
									</script>
								   <!--  <input type="text" class="inp01" id="cardNo" name="cardNo" onblur="loadBindInfo(this.value)"/> -->
								   <input type="hidden" id="cardNo" name="cardNo"/>
								   <input type="text" id="cardNoTmp" name="cardNoTmp" class="easyui-combobox" 
								   		value="<%=cardList!=null&&cardList.size()>0?cardList.get(0).cardNo:"" %>"  style="width:200px; height:35px;" 
								   		data-options="panelHeight:'auto',prompt:'请输入银行卡号',events:{blur:loadBindInfo},valueField: 'value',textField: 'label',data:cardNoes"/>
								    <font color="red" size="2"><span id="cardErrorInfo"></span></font><br/>
								    <span id="cardInfo"></span></td>
								  </tr>
								  <tr>
								    <td height="55" valign="middle" align="right">身份证号：</td>
								    <td><input type="text" maxlength="18" id="credentialNo" name="credentialNo" style="width:200px; height:35px;border:1px solid #95b8e7"/><font color="red" size="2"><span id="credentialNoErrorInfo"></span></font></td>
								  </tr>
								  <tr>
								    <td height="55" valign="middle" align="right">姓名：</td>
								    <td><input type="text" maxlength="15" id="name" name="name" style="width:200px; height:35px;border:1px solid #95b8e7"/><font color="red" size="2"><span id=nameErrorInfo></span></font></td>
								  </tr>
								  <tr>
								    <td height="55" valign="middle" align="right">银行预留手机号：</td>
								    <td><input type="text" class="inp02" maxlength="11" id="mobileNo" name="mobileNo" style="width:200px; height:35px;border:1px solid #95b8e7"/><font color="red" size="2"><span id=mobileNoErrorInfo></span></font></td>
								  </tr>
								  <tr style="display:none;" id="cvv2Box">
								    <td height="55" valign="middle" align="right">CVV2：</td>
								    <td><input type="text" maxlength="3" id="cvv2" name="cvv2" style="width:200px; height:35px;border:1px solid #95b8e7"/><font color="red" size="2"><span id=cvv2ErrorInfo>信用卡背面的3位数字</span></font></td>
								  </tr>
								  <tr style="display:none;" id="validPeriodBox">
								    <td height="55" valign="middle" align="right">卡有效期：</td>
								    <td><input type="text" maxlength="4" id="validPeriod" name="validPeriod" style="width:200px; height:35px;border:1px solid #95b8e7"/><font color="red" size="2"><span id=validPeriodErrorInfo></span></font></td>
								  </tr>
								  <tr>
								    <td height="55" valign="middle">&nbsp;</td>
								    <td align="left"><input type="button" style="width:200px;height:35px;font-size:14px;" name="pay" id="pay" onclick="payFormNext()" value="下一步"/></td>
								  </tr>
								  <%if(banksInfo.length()>0){ %>
								  <tr>
								    <td rowspan="">&nbsp;</td>
								    <td align="left">支持银行：<%=banksInfo %></td>
								  </tr>
								  <%} %>
								</table>
					            <br/><br/>
		                    </form>
		                    <div class="contP">
		                        <p class="top">付款遇到问题：</p>
		                        <p class="tops">什么是快捷支付？</p>
		                        <p class="topw">快捷支付是最便捷、最安全的网上支付方式。只需您银行卡绑定账号，网上支付时无需网银，支付时在登录状态下输入短信验证码即可完成付款。</p>
		                        <p class="tops">怎样开通快捷支付？</p>
		                        <p class="topw">支付时选择您拥有的银行卡，输入相应卡有效信息以及身份信息后，立即开通且支付，即可完成开通。第二次支付时在登录状态下直接输入短信验证码即可。</p>
		                    </div>
	                    </div>
	                </div>
	            </div>
	        </div>
	    </div>
	</body>
	</html>
	<script type="text/javascript">
	var cardType = "";
	var cardNoDesc = "";
	var loadBindInfoHandle;
	function loadBindInfo(){
		loadBindInfoHandle = setInterval(loadBindInfoRun, 200);
	}
	function loadBindInfoRun(){
		clearInterval(loadBindInfoHandle);
		var cardNo = $('#cardNoTmp').combobox('getText');
		document.getElementById("payInfo").innerHTML="";
		cardType="";
		cardNoDesc="";
		clearErrInfo();
		clearAllValue();
		document.getElementById("cvv2Box").style.display="none";
		document.getElementById("validPeriodBox").style.display="none";
		if(cardNo.length<10){
			document.getElementById("cardInfo").innerHTML="";
			document.getElementById("cardErrorInfo").innerHTML="卡号不正确";
			return;
		}
		$.ajax({
			url:"<%=path %>/getBindCardByNo.htm",
			type:"post",
			async:false,
			data:{cardNo:cardNo},
			dataType:"json",
			success:function(responseText){
				if(responseText.cardInfo==""){
					cardNoDesc="不支持的卡号";
					document.getElementById("cardInfo").innerHTML="";
					document.getElementById("cardErrorInfo").innerHTML=cardNoDesc;
					return;
				}
				if(responseText.respDesc != undefined && responseText.respDesc!=""){
					document.getElementById("cardInfo").innerHTML="";
					document.getElementById("cardErrorInfo").innerHTML=responseText.respDesc;
					cardNoDesc = responseText.respDesc;
					return;
				}
				cardType = responseText.cardType;
				if(cardType=="1"){//信用卡
					alert("不支持信用卡");
					return;
					document.getElementById("cvv2Box").style.display="";
					document.getElementById("validPeriodBox").style.display="";
				} else {
					document.getElementById("cvv2Box").style.display="none";
					document.getElementById("validPeriodBox").style.display="none";
				}
				if(responseText.cardInfo == undefined)document.getElementById("cardInfo").innerHTML="";
				else document.getElementById("cardInfo").innerHTML=responseText.cardInfo;
				if(responseText.credentialNo == undefined)document.getElementById("credentialNo").value="";
				else document.getElementById("credentialNo").value=responseText.credentialNo;
				if(responseText.name == undefined)document.getElementById("name").value="";
				else document.getElementById("name").value=responseText.name;
				if(responseText.mobileNo == undefined)document.getElementById("mobileNo").value="";
				else document.getElementById("mobileNo").value=responseText.mobileNo;
			},
			error:function(){
				alert("系统错误");
			}
		});
	}
	function clearErrInfo(){
		document.getElementById("cardErrorInfo").innerHTML="";
		document.getElementById("payInfo").innerHTML="";
		document.getElementById("credentialNoErrorInfo").innerHTML="";
		document.getElementById("nameErrorInfo").innerHTML="";
		document.getElementById("mobileNoErrorInfo").innerHTML="";
		document.getElementById("cvv2ErrorInfo").innerHTML="信用卡背面的3位数字";
		document.getElementById("validPeriodErrorInfo").innerHTML="";
	}
	function clearAllValue(){
		document.getElementById("credentialNo").value="";
		document.getElementById("name").value="";
		document.getElementById("mobileNo").value="";
		document.getElementById("cvv2").value="";
		document.getElementById("validPeriod").value="";
	}
	function payFormNext(){
		document.getElementById("cardNo").value = $('#cardNoTmp').combobox('getText');
		clearErrInfo();
		if(cardNoDesc != ""){
			document.getElementById("cardErrorInfo").innerHTML=cardNoDesc;
			return;
		}
		if(document.getElementById("cardNo").value.length<10){
			document.getElementById("cardErrorInfo").innerHTML="请正确输入卡号";
			return;
		}
		if(document.getElementById("credentialNo").value.length!=15&&document.getElementById("credentialNo").value.length!=18){
			document.getElementById("credentialNoErrorInfo").innerHTML="请正确输入身份证号";
			return;
		}
		if(document.getElementById("name").value.length==0){
			document.getElementById("nameErrorInfo").innerHTML="请正确输入姓名";
			return;
		}
		if(document.getElementById("mobileNo").value.length!=11){
			document.getElementById("mobileNoErrorInfo").innerHTML="请正确输入手机号";
			return;
		}
		if(cardType=="1"){
			if(document.getElementById("cvv2").value.length!=3){
				document.getElementById("cvv2ErrorInfo").innerHTML="请正确输入cvv2";
				return;
			}
			if(document.getElementById("validPeriod").value.length!=4){
				document.getElementById("validPeriodErrorInfo").innerHTML="请正确输入卡有效期";
				return;
			}
		}
		document.getElementById("confirmThePaymentFormForH5").submit();
	}
	<%
	if(cardList!=null&&cardList.size()>0){%>
		loadBindInfo();
	<%}%>
	</script>
<%} %>