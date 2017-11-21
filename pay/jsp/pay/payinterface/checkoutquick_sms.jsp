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
<%
PayRequest payRequest = (PayRequest)session.getAttribute("payRequest");
if(payRequest == null){
	out.println("支付请求不存在");
	return;
}
String path = request.getContextPath();
String info = (String)(request.getAttribute("info")==null?"":request.getAttribute("info"));
String payOver = (String)request.getAttribute("payOver");
if("mobile".equals(session.getAttribute("client-type"))){%>
	<!DOCTYPE html>
	<html><head><meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=no, minimum-scale=1.0, maximum-scale=1.0">
	<script type="text/javascript" src="<%=request.getContextPath() %>/js/jquery-easyui-1.4/jquery.min.js"></script>
	<title>快捷收银台-移动版</title>
	<link rel="stylesheet" type="text/css" href="<%=path %>/jsp/pay/payinterface/checkoutquick_1.css">
	</head>
	 <body>
	<div class="warpper">
	    <%if("OK".equals(payOver)){ %>
    	    <header id="header_weixin">
		        <span>请求提交成功</span>
			    </header>
			    <div class="mains">
			    	<div class="box" style="font-size:15px;"><label>商品名称：</label><span><%=com.PayConstant.PAY_CONFIG.get("COMMON_PRODUCT_DESC") %></span></div>
			    	<%-- <div class="box" style="font-size:15px;"><label>商品名称：</label><span>${ payRequest.merchantOrderDesc }</span></div> --%>
			        <div class="box" style="font-size:15px;"><label>订单编号：</label><span>${ payRequest.merchantOrderId }</span></div>
			        <div class="boxs" style="font-size:15px;"><label>应付金额：</label><span><fmt:formatNumber value="${ payRequest.merchantOrderAmt / 100 }" type="currency" pattern=",##0.00#"/>元</span></div>
			    </div>
		    <div class="main" style="background-color:#f5f5f7;">
		    <section class="con">  
		        <div class="pic"><img src="<%=path %>/images/ok.png"></div>
		    </section>
			</div>
			<%if(payRequest.merchantFrontEndUrl.length()>0){ %>
			<input type="submit" class="btn btn3" onclick="location.href='<%=payRequest.merchantFrontEndUrl %>'" value="请求提交成功！返回商家（支付结果以银行实际扣款为准）">
			<%} else { %>
			<input type="submit" class="btn btn3" value="请求提交成功！（支付结果以银行实际扣款为准）">
			<%} %>
      	<%} else { %>
    	    <header id="header_weixin">
		        <span>填写验证码</span>
		    </header>
		    <div class="mains">
			    	<div class="box" style="font-size:15px;"><label>商品名称：</label><span><%=com.PayConstant.PAY_CONFIG.get("COMMON_PRODUCT_DESC") %></span></div>
			    	<%-- <div class="box" style="font-size:15px;"><label>商品名称：</label><span>${ payRequest.merchantOrderDesc }</span></div> --%>
			        <div class="box" style="font-size:15px;"><label>订单编号：</label><span>${ payRequest.merchantOrderId }</span></div>
			        <div class="boxs" style="font-size:15px;"><label>应付金额：</label><span><fmt:formatNumber value="${ payRequest.merchantOrderAmt / 100 }" type="currency" pattern=",##0.00#"/>元</span></div>
			  </div>
		    <div class="main" style="margin-top:10px;">
			<form action="${ pageContext.request.contextPath }/confirmTheQuickH5Payment.htm" method="post" id="confirmTheQuickH5PaymentForm">
		    <section class="con">  
				<div class="put" style=" border-bottom:none; ">
		            <span class="tit">短信验证码：</span>
		            <span class="in">
						<input type="text" maxlength="6" id="checkCode" name="checkCode"/>
		            </span>
		        </div>         
		    </section>
			</form> 
			</div>
			<p class="warn" id="payInfoBox" style="display:<%=info.length()>0?"":"none" %>;"><span id="payInfo" class="warn"><%=info.replaceAll("200.0分", "2元") %></span></p>
			<input type="submit" class="btn btn3"  name="pay" id="pay" onclick="sendSmsForQuickPayH5Submit()" value="确认支付"> 	
		<%} %>
	</div>
	</body></html>
	<script type="text/javascript">
	function sendSmsForQuickPayH5Submit(){
		document.getElementById("payInfoBox").style.display="none";
		document.getElementById("payInfo").innerHTML="";
		if(document.getElementById("checkCode").value.length<4){
			document.getElementById("payInfoBox").style.display="";
			document.getElementById("payInfo").innerHTML="请正确输入验证码";
			return;
		}
		document.getElementById('pay').disabled="disabled";
		document.getElementById('pay').value="处理中...";
		document.getElementById('confirmTheQuickH5PaymentForm').submit();
	}
	var sendSmsForQuickPayH5WaitHandle;
	var sendSmsForQuickPayH5WaitCount = 60;
	function sendSmsForQuickPayH5(){
		document.getElementById("payInfoBox").style.display="none";
		document.getElementById("payInfo").innerHTML="";
		$.ajax({
			url:"<%=path %>/sendSmsForQuickPayH5.htm",
			type:"post",
			async:false,
			dataType:"json",
			success:function(responseText){
				//sendSmsForQuickPayH5WaitCount = 60;
				//document.getElementById('sendSms').disabled=true;
				//sendSmsForQuickPayH5WaitHandle = setInterval(sendSmsForQuickPayH5Wait, 1000);
				//document.getElementById('sendSms').value='发送验证码('+sendSmsForQuickPayH5WaitCount+')';
				alert(responseText.respDesc);
				if(responseText.respDesc != undefined && responseText.respDesc!=""){
					document.getElementById("payInfoBox").style.display="";
					document.getElementById("payInfo").innerHTML=responseText.respDesc;
				}			
			},
			error:function(){
				document.getElementById("payInfoBox").style.display="";
				document.getElementById("payInfo").innerHTML="系统错误";
			}
		});
	}
	function sendSmsForQuickPayH5Wait () {
	    if (sendSmsForQuickPayH5WaitCount <= 1) {
	        sendSmsForQuickPayH5WaitCount=60;
	        document.getElementById('sendSms').disabled=false;
	        clearInterval(sendSmsForQuickPayH5WaitHandle);
	        document.getElementById('sendSms').value='发送验证码';
	    } else {
	        sendSmsForQuickPayH5WaitCount--;
	        document.getElementById('sendSms').value='发送验证码('+sendSmsForQuickPayH5WaitCount+')';
	    }
	}
	<%if(payOver==null){%>
	$(document).ready(function(){
		sendSmsForQuickPayH5WaitCount = 60;
		document.getElementById('sendSms').disabled=true;
		sendSmsForQuickPayH5WaitHandle = setInterval(sendSmsForQuickPayH5Wait, 1000);
		document.getElementById('sendSms').value='发送验证码('+sendSmsForQuickPayH5WaitCount+')';
	});
	<%}%>
	</script>
<%} else {%>
	<!DOCTYPE>
	<html>
	<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta http-equiv= "Pragma" content= "no-cache" />
	<meta http-equiv= "Cache-Control" content= "no-cache" />
	<meta http-equiv= "Expires" content= "0" />
	<script type="text/javascript" src="<%=request.getContextPath() %>/js/jquery-easyui-1.4/jquery.min.js"></script>
	<title>快捷收银台-PC版</title>
	<link rel="stylesheet" type="text/css" href="<%=path %>/jsp/pay/payinterface/checkoutquick.css">
	<style></style>
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
				            	<%-- =payRequest.merchantName.length()>0?"<p>商户名称："+payRequest.merchantName+"</p>":"" --%>
				                <p>商品名称：<%=com.PayConstant.PAY_CONFIG.get("COMMON_PRODUCT_DESC") %></p>
				                <%-- <p>商品名称：${ payRequest.merchantOrderDesc }</p> --%>
				                <p>订单编号：${ payRequest.merchantOrderId }</p>
				                <p>订单时间：${ payRequest.orderTime }</p>
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
	                    	<%if("OK".equals(payOver)){ %>
	                    		<div style="margin-top:30px;text-align:center;font-size:18px;">
	                    		<img src="<%=path %>/images/ok.png"/><br/><br/>请求提交成功！（支付结果以银行实际扣款为准） <%=payRequest.merchantFrontEndUrl.length()>0?"<a href='"+payRequest.merchantFrontEndUrl+"'>返回商家</a>":"" %>
	                    		</div><br/><br/>
	                    	<%} else { %>
		                    <form action="${ pageContext.request.contextPath }/confirmTheQuickH5Payment.htm" method="post" id="confirmTheQuickH5PaymentForm">
		                    	<table width="100%" border="0" cellspacing="0" cellpadding="0" style="font-size:14px;">
		                    	  <tr>
								    <td width="200">&nbsp;</td>
								    <td colspan="2"><font color="red" size="2"><span id="payInfo"><%=info.replaceAll("200.0分", "2元") %></span></font></td>
								  </tr>
								  <tr>
								    <td height="55" valign="middle" align="right">短信验证码：</td>
								    <td width="120"><input type="text" class="inp02" style="width:110px;height:35px;border:1px solid #95b8e7" maxlength="6" id="checkCode" name="checkCode"/></td>
								    <td valign="middle">
								    <!-- <input type="button" style="width:120px;height:36px;font-size:14px;" name="sendSms" id="sendSms" onclick="sendSmsForQuickPayH5()" value="发送验证码"/> -->
								    </td>
								  </tr>
								  <tr>
								    <td height="55" valign="middle">&nbsp;</td>
								    <td align="left"><input type="button" style="width:110px;height:36px;font-size:14px;" name="pay" 
					            		id="pay" onclick="sendSmsForQuickPayH5Submit()" value="确认支付"/></td>
					            	<td height="55" valign="middle">&nbsp;</td>
								  </tr>
								</table>
					            <br/><br/>
		                    </form>
		                    <%} %>
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
	function sendSmsForQuickPayH5Submit(){
		document.getElementById("payInfo").innerHTML="";
		if(document.getElementById("checkCode").value.length<4){
			document.getElementById("payInfo").innerHTML="请正确输入验证码";
			return;
		}
		document.getElementById('pay').disabled="disabled";
		document.getElementById('pay').value="处理中...";
		document.getElementById('confirmTheQuickH5PaymentForm').submit();
	}
	var sendSmsForQuickPayH5WaitHandle;
	var sendSmsForQuickPayH5WaitCount = 60;
	function sendSmsForQuickPayH5(){
		document.getElementById("payInfo").innerHTML="";
		$.ajax({
			url:"<%=path %>/sendSmsForQuickPayH5.htm",
			type:"post",
			async:false,
			dataType:"json",
			success:function(responseText){
				//sendSmsForQuickPayH5WaitCount = 60;
				//document.getElementById('sendSms').disabled=true;
				//sendSmsForQuickPayH5WaitHandle = setInterval(sendSmsForQuickPayH5Wait, 1000);
				//document.getElementById('sendSms').value='发送验证码('+sendSmsForQuickPayH5WaitCount+')';
				if(responseText.respDesc != undefined && responseText.respDesc!=""){
					document.getElementById("payInfo").innerHTML=responseText.respDesc;
				}			
			},
			error:function(){
				document.getElementById("payInfo").innerHTML="系统错误";
			}
		});
	}
	function sendSmsForQuickPayH5Wait () {
	    if (sendSmsForQuickPayH5WaitCount <= 1) {
	        sendSmsForQuickPayH5WaitCount=60;
	        document.getElementById('sendSms').disabled=false;
	        clearInterval(sendSmsForQuickPayH5WaitHandle);
	        document.getElementById('sendSms').value='发送验证码';
	    } else {
	        sendSmsForQuickPayH5WaitCount--;
	        document.getElementById('sendSms').value='发送验证码('+sendSmsForQuickPayH5WaitCount+')';
	    }
	}
	<%if(payOver==null){%>
	$(document).ready(function(){
		sendSmsForQuickPayH5WaitCount = 60;
		document.getElementById('sendSms').disabled=true;
		sendSmsForQuickPayH5WaitHandle = setInterval(sendSmsForQuickPayH5Wait, 1000);
		document.getElementById('sendSms').value='发送验证码('+sendSmsForQuickPayH5WaitCount+')';
	});
	<%}%>
	</script>
<%} %>