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

//过滤支持借记卡、信用卡的银行
List<PayBank>list = PayCoopBankService.SUPPORTED_BANK_LIST;
//结算方式 0自动结算到虚拟账户 1线下打款 2自动结算到银行账户 3实时结算到虚拟账户  4实时结算到银行账户
if("3".equals(payRequest.merchant.settlementWay)||"4".equals(payRequest.merchant.settlementWay)){//实时结算通道
	list = PayCoopBankService.SUPPORTED_BANK_LIST_REAL_TIME;
}
List jjList = new ArrayList();
List djList = new ArrayList();
List pubList = new ArrayList();
for(int i=0; i<list.size(); i++){
	PayBank bank = list.get(i);
	if("0".equals(bank.jiejiCard))jjList.add(bank);
	if("1".equals(bank.daijiCard))djList.add(bank);
	if("4".equals(bank.compCard))pubList.add(bank);
}
int nav = -1;
boolean acc=false,debit=false,crebit=false,publicAcc=false;
PayTranUserInfo payer = (PayTranUserInfo)payRequest.tranUserMap.get(payRequest.payerId);
long orderAmt = Long.parseLong(payRequest.merchantOrderAmt);
String info = (String)(request.getAttribute("info")==null?"":request.getAttribute("info"));
String webInfo = (String)(request.getAttribute("webInfo")==null?"":request.getAttribute("webInfo"));
String bankId = (String)(request.getAttribute("bankId")==null?"":request.getAttribute("bankId"));
 %>
<!DOCTYPE>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv= "Pragma" content= "no-cache" />
<meta http-equiv= "Cache-Control" content= "no-cache" />
<meta http-equiv= "Expires" content= "0" />
<script type="text/javascript" src="<%=request.getContextPath() %>/js/jquery-easyui-1.4/jquery.min.js"></script>
<title>收银台</title>
<style>
blockquote,body,button,dd,dl,dt,fieldset,form,h1,h2,h3,h4,h5,h6,hr,input,legend,li,ol,p,pre,td,textarea,th,ul{margin:0;padding:0; color:#333;}
body,button,input,select,textarea{font:12px/1.5 tahoma,arial,'Hiragino Sans GB',\5b8b\4f53,sans-serif}
h1,h2,h3,h4,h5,h6,button,input,select,textarea{font-size:100%}
address,cite,dfn,em,var,i{font-style:normal}
code,kbd,pre,samp{font-family:courier new,courier,monospace}
small{font-size:12px}
sup{vertical-align:text-top}
sub{vertical-align:text-bottom}
legend{color:#000}
ol,ul{list-style:none}
a{text-decoration:none}
a:hover{text-decoration:none}
html{overflow-y:scroll;}
.main .header{ height:78px; border-bottom:2px solid #f38a50;}
.main .header .headers{ width:980px; height:78px; margin:0 auto; overflow:hidden;}
.main .header .headersL{ float:left; height:58px; padding:20px 0px 0px 15px; overflow:hidden;}
.main .header .headersL h2{ float:left; overflow:hidden; width:154px; height:41px; margin-right:14px; }
.main .header .headersL .Cas{ float:left; width:130px; text-align:center; height:40px;margin-top:-2px;}
.main .header .headersL .Cas p{ font-family:"微软雅黑"; font-size:20px; font-weight:600; color:#333;}
.main .header .headersL .Cas a{ color:#333;}
.main .header .headersR{ float:right;}
.main .header .headersR ul{ overflow:hidden; padding-top:55px; padding-right:14px;}
.main .header .headersR ul li{ float:left; margin-right:6px;}
.main .header .headersR ul li a{ color:#005194;}

.main .content{ width:980px; margin:0 auto; padding-top:10px;}
.main .cont{ height:101px; border:1px solid #e5ecf1; padding:15px 0px 0px 40px; overflow:hidden;}
.main .cont .contL,.main .cont .contR{ float:left;}
.main .cont .contL{ margin-right:150px;}
.main .cont p{ font-size:14px; height:24px; line-height:24px; color:#333;}
.main .cont p a{ font-size:30px; color:#f38a50;}
.main .conts h3{font-family:"微软雅黑"; font-size:16px;  height:26px; line-height:26px; margin:8px 0px 10px; border-left:3px solid #f38a50; padding-left:14px;}
.main .conts .change{ border:1px solid #e5ecf1; }
.main .conts .change .changes{ overflow:hidden; height:45px; line-height:45px; background:#f2f7fc; border-bottom:1px solid #e5ecf1;}
.main .conts .change ul li{ float:left; width:150px; text-align:center; font-size:16px; cursor:pointer;}
.main .conts .change ul .current{ height:47px; background:#fff; border-top:3px solid #f38a50; border-bottom:1px solid #fff; border-left:1px solid #e5ecf1; border-right:1px solid #e5ecf1;}

.main .conts .change  .li00{ padding:25px 0px 30px 0px; overflow:hidden; display:none;}
.main .conts .change  .li00 label input{ width:200px; height:35px; line-height:35px; border:1px solid #b5c0c9; [;line-height:1;];margin-top:10px}
.main .conts .change  .li00 p{ font-size:14px; padding-left:45px;}
.main .conts .change  .li00 p span{ font-size:12px;}
.main .conts .change  .li00 ul{ overflow:hidden; padding-top:15px; padding-left:20px;}
.main .conts .change  .li00 ul li{ float:left; width:176px; height:44px; line-height:44px; border:1px solid #e8e8e8; margin-bottom:10px; margin-right:10px; cursor:pointer;}
.main .conts .change  .li00 ul li input{ display:inline-block; vertical-align:middle;margin-right:-5px;}
.main .conts .change  .li00 ul li label{ display:inline-block; vertical-align:middle;}
.main .conts .change  .li00 .bankBill,.main .conts .change  .li01 .bankBills{ display:none; margin-left:20px; margin-top:10px; border:1px solid #999;}
.main .conts .change  .li00 .que,.main .conts .change  .li01 .ques{ display:block; font-size:16px; width:120px; height:35px; text-align:center; line-height:35px; color:#fff; background:#e6793e; margin-left:20px; margin-top:10px; border-radius:2px;}


.main .conts .change  .li01{ padding:25px 0px 30px 0px; overflow:hidden; display:none;}
.main .conts .change  .li01 label input{ width:150px; height:35px; line-height:35px; border:1px solid #b5c0c9; [;line-height:1;]}
.main .conts .change  .li01 p{ font-size:14px; padding-left:45px;}
.main .conts .change  .li01 p span{ font-size:12px;}
.main .conts .change  .li01 ul{ overflow:hidden; padding-top:15px; padding-left:20px;}
.main .conts .change  .li01 ul li{ float:left; width:176px; height:44px; line-height:44px; border:1px solid #e8e8e8; margin-bottom:10px; margin-right:10px; cursor:pointer;}
.main .conts .change  .li01 ul li input{ display:inline-block; vertical-align:middle;margin-right:-5px;}
.main .conts .change  .li01 ul li label{ display:inline-block; vertical-align:middle;}
.main .conts .change  .li01 .bankBill,.main .conts .change  .li02 .bankBills{ display:none; margin-left:20px; margin-top:10px; border:1px solid #999;}
.main .conts .change  .li01 .que,.main .conts .change  .li02 .ques{ display:block; font-size:16px; width:120px; height:35px; text-align:center; line-height:35px; color:#fff; background:#e6793e; margin-left:20px; margin-top:10px; border-radius:2px;}
 
.main .conts .change  .li02{ padding:25px 0px 30px 0px; overflow:hidden; display:none;}
.main .conts .change  .li02 label input{ width:150px; height:35px; line-height:35px; border:1px solid #b5c0c9; [;line-height:1;]}
.main .conts .change  .li02 p{ font-size:14px; padding-left:45px;}
.main .conts .change  .li02 p span{ font-size:12px;}
.main .conts .change  .li02 ul{ overflow:hidden; padding-top:15px; padding-left:20px;}
.main .conts .change  .li02 ul li{ float:left; width:176px; height:44px; line-height:44px; border:1px solid #e8e8e8; margin-bottom:10px; margin-right:10px; cursor:pointer;}
.main .conts .change  .li02 ul li input{ display:inline-block; vertical-align:middle;margin-right:-5px;}
.main .conts .change  .li02 ul li label{ display:inline-block; vertical-align:middle;}
.main .conts .change  .li02 .nexts{ display:block; width:165px; height:45px; text-align:center; line-height:45px; color:#fff; background:#e6793e; border-radius:2px; margin-left:20px;}

.main .content .contP{border-top:1px solid #e5ecf1; padding:20px 0px 30px 0px;}
.main .content .contP .top{ font-size:14px; font-weight:bold; height:30px; line-height:30px;}
.main .content .contP .tops{ height:25px; line-height:25px; color:#666;}
.main .content .contP .topw{ height:25px; line-height:25px; color:#989898;}


.bots{ margin-top:20px; background:#f3f3f3;}
.address{ overflow:hidden; width:940px; padding:20px 30px 0px; margin:0 auto;}
.address .addsL{ float:left;}
.address .addsL .addsPic{ margin-top:10px;}
</style>
</head>
<body>
	<div class="main">
	    <div class="header">
        	<div class="headers">
            	<div class="headersL">
                    <div class="Cas">
                    	<p>收银台</p>
                    </div>
                </div>
            </div>
        </div>
        <div class="content">
        	<div class="cont">
            	<div class="contL">
            		<c:if test="${not empty payRequest.merchantName}">
	    				<p>商户名称：${ payRequest.merchantName }</p>
	    			</c:if>
                    <%-- <p>商品名称：${ payRequest.merchantOrderDesc }</p> --%>
                    <p>商品名称：<%=com.PayConstant.PAY_CONFIG.get("COMMON_PRODUCT_DESC") %></p>
                    <p>订单编号：${ payRequest.merchantOrderId }</p>
                    <p>订单时间：${ payRequest.orderTime }</p>
                </div>
                <div class="contR">
                	<p>应付金额：</p>
                    <p><a href="javascript:;"><fmt:formatNumber value="${ payRequest.merchantOrderAmt / 100 }" type="currency" pattern=",##0.00#"/></a>元</p>
                </div>
            </div>
            <div class="conts">
            	<h3>请选择支付方式</h3>
                <div class="change">
                	<ul class="changes">
                		<%
                		if(PayConstant.CARD_BIN_TYPE_JIEJI.equals(payRequest.accountType)){
                			if(jjList.size()==0&&djList.size()==0){
                				if(!payRequest.merchantId.equals(PayConstant.PAY_CONFIG.get("PAY_RECHARGE_ACC"))
                					&&!payRequest.merchantId.equals(PayConstant.PAY_CONFIG.get("PAY_RECHARGE_MER"))
                					&&!payRequest.merchantId.equals(PayConstant.PAY_CONFIG.get("PAY_RECHARGE_MER_CHARGE"))){
                					%><li class="current">账户支付</li><%
                					nav=0;acc=true;
                				}
                			} else {
                				if(!payRequest.merchantId.equals(PayConstant.PAY_CONFIG.get("PAY_RECHARGE_ACC"))
                					&&!payRequest.merchantId.equals(PayConstant.PAY_CONFIG.get("PAY_RECHARGE_MER"))
                					&&!payRequest.merchantId.equals(PayConstant.PAY_CONFIG.get("PAY_RECHARGE_MER_CHARGE"))){
                					%><li <%=bankId.length()!=0?"class='current'":"" %>>账户支付</li><%
                					nav=0;acc=true;
                				}
                				if(jjList.size()>0){
                					if("0".equals(payRequest.merchant.payWaySupported.substring(0,1))){
                						%><li <%=bankId.length()==0?"class='current'":"" %>>储蓄卡</li><%
                						nav++;debit=true;
                					}
                					if(djList.size()>0&&"0".equals(payRequest.merchant.payWaySupported.substring(1,2))
	                					&&!payRequest.merchantId.equals(PayConstant.PAY_CONFIG.get("PAY_RECHARGE_ACC"))
	                					&&!payRequest.merchantId.equals(PayConstant.PAY_CONFIG.get("PAY_RECHARGE_MER"))
	                					&&!payRequest.merchantId.equals(PayConstant.PAY_CONFIG.get("PAY_RECHARGE_MER_CHARGE"))){
                						if(acc){
                							if(debit);
                							else nav++;
                						} else {
                							if(debit);
                							else nav++;
                						}
                						%><li>信用卡</li><%crebit=true;
	                					}
                				} else {
                					if(djList.size()>0&&"0".equals(payRequest.merchant.payWaySupported.substring(1,2))
	                					&&!payRequest.merchantId.equals(PayConstant.PAY_CONFIG.get("PAY_RECHARGE_ACC"))
	                					&&!payRequest.merchantId.equals(PayConstant.PAY_CONFIG.get("PAY_RECHARGE_MER"))
	                					&&!payRequest.merchantId.equals(PayConstant.PAY_CONFIG.get("PAY_RECHARGE_MER_CHARGE"))){
	                					%><li <%=bankId.length()==0?"class='current'":"" %>>信用卡</li><%nav++;crebit=true;
	                				}
                				}
                			}
                		} else if(PayConstant.CARD_BIN_TYPE_DAIJI.equals(payRequest.accountType)){
                			if(jjList.size()==0&&djList.size()==0){
                				if(!payRequest.merchantId.equals(PayConstant.PAY_CONFIG.get("PAY_RECHARGE_ACC"))
                					&&!payRequest.merchantId.equals(PayConstant.PAY_CONFIG.get("PAY_RECHARGE_MER"))
                					&&!payRequest.merchantId.equals(PayConstant.PAY_CONFIG.get("PAY_RECHARGE_MER_CHARGE"))){
                					%><li class="current">账户支付</li><%
                					nav=0;acc=true;
                				}
                			} else {
                				if(!payRequest.merchantId.equals(PayConstant.PAY_CONFIG.get("PAY_RECHARGE_ACC"))
                					&&!payRequest.merchantId.equals(PayConstant.PAY_CONFIG.get("PAY_RECHARGE_MER"))
                					&&!payRequest.merchantId.equals(PayConstant.PAY_CONFIG.get("PAY_RECHARGE_MER_CHARGE"))){
                					%><li <%=bankId.length()!=0?"class='current'":"" %>>账户支付</li><%
                					nav=0;acc=true;
                				}
                				if(djList.size()>0){
                					if(jjList.size()>0&&"0".equals(payRequest.merchant.payWaySupported.substring(0,1))){
               							%><li <%=payRequest.merchantId.equals(PayConstant.PAY_CONFIG.get("PAY_RECHARGE_ACC"))
               							&&!payRequest.merchantId.equals(PayConstant.PAY_CONFIG.get("PAY_RECHARGE_MER"))
               							&&!payRequest.merchantId.equals(PayConstant.PAY_CONFIG.get("PAY_RECHARGE_MER_CHARGE"))
               							&&bankId.length()==0?"class='current'":""  %>>储蓄卡</li><%nav++;debit=true;
                					}
                					if("0".equals(payRequest.merchant.payWaySupported.substring(1,2))
                						&&!payRequest.merchantId.equals(PayConstant.PAY_CONFIG.get("PAY_RECHARGE_ACC"))
                						&&!payRequest.merchantId.equals(PayConstant.PAY_CONFIG.get("PAY_RECHARGE_MER"))
                						&&!payRequest.merchantId.equals(PayConstant.PAY_CONFIG.get("PAY_RECHARGE_MER_CHARGE"))){
                						%><li <%=bankId.length()==0?"class='current'":"" %>>信用卡</li><%
                						nav++;crebit=true;
                					}
                				} else {
                					if(jjList.size()>0&&"0".equals(payRequest.merchant.payWaySupported.substring(0,1))){
                						%><li <%=bankId.length()==0?"class='current'":"" %>>储蓄卡</li><%nav++;debit=true;
                					}
                				}
                			}
                		} else if("4".equals(payRequest.accountType)&&"0".equals(payRequest.merchant.payWaySupported.substring(2,3))){
                			%><li class="current">对公账户</li><%nav=0;publicAcc=true;
                		}
                		//从支付页面到次页面
                		if(bankId.length()!=0)nav=0;%>
                    </ul>
                    <%
                    if(acc){%>
                    <div class="li00">
                    	<form action="${ pageContext.request.contextPath }/confirmThePayment.htm" method="post" id="confirmThePaymentFormForAcc">
		                    <input type="hidden" name="merchantId"  value="${ payRequest.merchantId }"/>
		                    <input type="hidden" name="merchantOrderId"  value="${ payRequest.merchantOrderId }"/>
		                    <font color="red" size="4"><div style="margin-left:45px;margin-top:-20px;margin-bottom:-10px;" id="loginInfo"><%=info %></div></font>
		                    <%
		                    if(payer!=null){
		                        %><ul>
		                          <li style="width:930px;text-align:left"><input type="radio" name="banks" value="ACCOUNT" 
		                          	<%=orderAmt>payer.accProfile.consAcBal?"disabled" : "checked" %> style="margin-left:20px"/>
		                          <label style="margin-left:20px">余额<%=String.format("%.2f", ((double)payer.accProfile.consAcBal)/100d) %>元</label>
		                          <label style="margin-left:50px">支付<%=String.format("%.2f", ((double)orderAmt)/100d) %>元</label></li>
		                        </ul><%
                        		if(orderAmt<=payer.accProfile.consAcBal && !payRequest.merchantId.equals(PayConstant.PAY_CONFIG.get("PAY_RECHARGE_ACC"))
                					&&!payRequest.merchantId.equals(PayConstant.PAY_CONFIG.get("PAY_RECHARGE_MER"))
                					&&!payRequest.merchantId.equals(PayConstant.PAY_CONFIG.get("PAY_RECHARGE_MER_CHARGE"))){
                        			%><label id="payPwd2" style="margin-left:20px">
	                        		支付密码：<input type="password" name="payPwd" id="payPwd" value="" placeholder="支付密码" autocomplete="off"/></label>
	                      			<a style="margin-left:80px;" href="javascript:void(0);" class="que" onclick="accFormSubmit(document.getElementById('confirmThePaymentFormForAcc',1))">确认付款</a><br/><%
                      			}
                      		} else {%>
                      			<input type="hidden" name="banks"  value="ACCOUNT"/>
			                    <label style="margin-left:20px">手机号码：<input type="text" class="inp01" id="userId" name="userId"/></label><br/>
			                    <label style="margin-left:20px;">登录密码：<input type="password" class="inp02" id="loginPassword" name="loginPassword"/></label><br/>
			                    <label style="margin-left:32px;">验证码：<input style="width:97px;" type="text" id="validCode" name="validCode" maxlength="4"/>
			                    <a href="javascript:void(0);">
			                    	<img src="${ pageContext.request.contextPath }/jsp/jweb/image_code.jsp" width="99" height="34" style="margin-bottom:-12px;"
			                    	onclick="this.src='/pay-user/jsp/login/image_code.jsp?'+Math.random()"/>
			                    </a>
			                    </label><br/>
			                    <a style="margin-left:80px;" class="que" href="javascript:void(0);" onclick="accFormSubmit(document.getElementById('confirmThePaymentFormForAcc'),0)" id="userLoginSubmit">登录</a>
			                    <a  style="margin-left:80px;" href="/pay-user/jsp/login/register.jsp">免费注册</a>&nbsp;&nbsp;&nbsp;&nbsp;<a href="/pay-user/jsp/login/wangJ.jsp">忘记密码</a><br/>
                      		<%} 
                      		%>
                    	</form>
                    </div>
                    <% } 
                    if(debit&&"0".equals(payRequest.merchant.payWaySupported.substring(0,1))){ %>
                  	<div class="li01">
	                  <form action="${ pageContext.request.contextPath }/confirmThePayment.htm" method="post" id="confirmThePaymentFormForSavings">
	                	<input type="hidden" name="bankCardType"  value="<%=PayConstant.CARD_BIN_TYPE_JIEJI %>"/>
	                    <input type="hidden" name="merchantId"  value="${ payRequest.merchantId }"/>
	                    <input type="hidden" name="merchantOrderId"  value="${ payRequest.merchantOrderId }"/>
	                    <%if(webInfo.length()!=0){ %><p><font color="red" size="4"><div style="margin-left:45px;margin-top:-20px;"><%=webInfo %></div></font></p><%} %>
                        <p>支付<span>您需要先拥有一张已开通网上支付功能的</span><span style="color:#fa0000;">储蓄卡</span></p>
                        <%
                        for(int i=0; i<jjList.size(); i++){
                        	PayBank bank = (PayBank)jjList.get(i);
                        	if(i%5==0){
                        		if(i==0)out.println("<ul>");
                        		else out.println("</ul><ul>");
                        	}%>
                        	<li><input type="radio" name="banks" value="<%=bank.bankCode %>" id="<%="d_"+bank.bankCode %>"/>
                          	<label>
                            	<img src="${ pageContext.request.contextPath }/jsp/pay/payinterface/banklogo/<%=bank.bankCode %>.gif"
                           			onclick="document.getElementById('<%="d_"+bank.bankCode %>').checked=true" width="120" height="40"/>
                          	</label></li><%
                          	if(i==jjList.size()-1)out.println("</ul>");
                        }%>
                        <a href="javascript:void(0);" class="que" onclick="formSubmit(document.getElementById('confirmThePaymentFormForSavings'))">确认付款</a>
                        <a href="javascript:void(0)" 
                        	onclick="openWindow('${ pageContext.request.contextPath }/jsp/pay/payinterface/limit_amt.jsp','储蓄卡限额说明')"
                        	style="margin-left:20px">限额说明</a>
	                  </form>
	                  <div class="contP">
                        <p class="top">付款遇到问题：</p>
                        <p class="tops">什么是快捷支付？</p>
                        <p class="topw">快捷支付是最便捷、最安全的网上支付方式。只需您银行卡绑定账号，网上支付时无需网银，支付时在登录状态下输入短信验证码即可完成付款。</p>
                        <p class="tops">怎样开通快捷支付？</p>
                        <p class="topw">支付时选择您拥有的银行卡，输入相应卡有效信息以及身份信息后，立即开通且支付，即可完成开通。第二次支付时在登录状态下直接输入短信验证码即可。</p>
                      </div>
                    </div>
                    <%} if(crebit&&"0".equals(payRequest.merchant.payWaySupported.substring(1,2))){ %>
                  	<div class="li02">
	                  <form action="${ pageContext.request.contextPath }/confirmThePayment.htm" method="post" id="confirmThePaymentFormForCredit">
	                    <input type="hidden" name="bankCardType"  value="<%=PayConstant.CARD_BIN_TYPE_DAIJI %>"/>
	                    <input type="hidden" name="merchantId"  value="${ payRequest.merchantId }"/>
	                  	<input type="hidden" name="merchantOrderId"  value="${ payRequest.merchantOrderId }"/>
	                  	<%if(webInfo.length()!=0){ %><p><font color="red" size="4"><div style="margin-left:45px;margin-top:-20px;"><%=webInfo %></div></font></p><%} %>
                   		<p>网银支付<span>您需要先拥有一张已开通网上支付功能的</span><span style="color:#fa0000;">信用卡</span></p>
                        <%
                        //过滤支持信用卡的银行
                       	for(int i=0; i<djList.size(); i++){
                       		PayBank bank = (PayBank)djList.get(i);
	                       	if(i%5==0){
	                       		if(i==0)out.println("<ul>");
	                       		else out.println("</ul><ul>");
	                       	}%>
                       		<li><input type="radio" name="banks" value="<%=bank.bankCode %>" id="<%="c_"+bank.bankCode %>"/>
                         	<label>
                           		<img src="${ pageContext.request.contextPath }/jsp/pay/payinterface/banklogo/<%=bank.bankCode %>.gif"
                           			onclick="document.getElementById('<%="c_"+bank.bankCode %>').checked=true" width="120" height="40"/>
                         	</label></li><%
                         	if(i==djList.size()-1)out.println("</ul>");
                        }%>
                        <a href="javascript:;" class="ques" onclick="formSubmit(document.getElementById('confirmThePaymentFormForCredit'))">确认付款</a>
                        <a href="javascript:void(0)" 
                        	onclick="openWindow('${ pageContext.request.contextPath }/jsp/pay/payinterface/limit_amt.jsp','储蓄卡限额说明')"
                        	style="margin-left:20px">限额说明</a>
                      </form>
                      <div class="contP">
                        <p class="top">付款遇到问题：</p>
                        <p class="tops">什么是快捷支付？</p>
                        <p class="topw">快捷支付是最便捷、最安全的网上支付方式。只需您银行卡绑定账号，网上支付时无需网银，支付时在登录状态下输入短信验证码即可完成付款。</p>
                        <p class="tops">怎样开通快捷支付？</p>
                        <p class="topw">支付时选择您拥有的银行卡，输入相应卡有效信息以及身份信息后，立即开通且支付，即可完成开通。第二次支付时在登录状态下直接输入短信验证码即可。</p>
                      </div>
                    </div>
                    <%} if(publicAcc&&"0".equals(payRequest.merchant.payWaySupported.substring(2,3))){ %>
                  	<div class="li02">
	                  <form action="${ pageContext.request.contextPath }/confirmThePayment.htm" method="post" id="confirmThePaymentFormForPublic">
	                  	<!-- input type="hidden" name="payChannel"  value="XFPAY"/ -->
	                    <input type="hidden" name="bankCardType"  value="4"/>
	                    <input type="hidden" name="merchantId"  value="${ payRequest.merchantId }"/>
	                  	<input type="hidden" name="merchantOrderId"  value="${ payRequest.merchantOrderId }"/>
                        <%
                        //过滤支持信用卡的银行
                       	for(int i=0; i<pubList.size(); i++){
                       		PayBank bank = (PayBank)pubList.get(i);
	                       	if(i%5==0){
	                       		if(i==0)out.println("<ul>");
	                       		else out.println("</ul><ul>");
	                       	}%>
                       		<li><input type="radio" name="banks" value="<%=bank.bankCode %>" id="<%="p_"+bank.bankCode %>"/>
                         	<label>
                           		<img src="${ pageContext.request.contextPath }/jsp/pay/payinterface/banklogo/<%=bank.bankCode %>.gif"
                           			onclick="document.getElementById('<%="p_"+bank.bankCode %>').checked=true" width="120" height="40"/>
                         	</label></li><%
                         	if(i==pubList.size()-1)out.println("</ul>");
                        }%>
                        <a href="javascript:;" class="ques" onclick="formSubmit(document.getElementById('confirmThePaymentFormForPublic'))">确认付款</a>
                        <a href="javascript:void(0)" 
                        	onclick="openWindow('${ pageContext.request.contextPath }/jsp/pay/payinterface/limit_amt.jsp','储蓄卡限额说明')"
                        	style="margin-left:20px">限额说明</a>
                      </form>
                      <div class="contP">
                        <p class="top">付款遇到问题：</p>
                        <p class="tops">什么是快捷支付？</p>
                        <p class="topw">快捷支付是最便捷、最安全的网上支付方式。只需您银行卡绑定账号，网上支付时无需网银，支付时在登录状态下输入短信验证码即可完成付款。</p>
                        <p class="tops">怎样开通快捷支付？</p>
                        <p class="topw">支付时选择您拥有的银行卡，输入相应卡有效信息以及身份信息后，立即开通且支付，即可完成开通。第二次支付时在登录状态下直接输入短信验证码即可。</p>
                      </div>
                    </div>
                    <%} %>
                </div>
            </div>
        </div>
    </div>
</body>
</html>
<script type="text/javascript">
$(function(){
	$('.change>div').eq(<%=nav==-1?0:nav %>).show().siblings('div').hide();
	$('.changes li').click(function(e) {
        $(this).addClass('current').siblings().removeClass('current');
		$('.change>div').eq($(this).index()).show().siblings('div').hide();
    });
	if(!placeholderSupport()){   // 判断浏览器是否支持 placeholder
			$('[placeholder]').focus(function() {
				var input = $(this);
				if (input.val() == input.attr('placeholder')) {
					input.val('');
					input.removeClass('placeholder');
				}
			}).blur(function() {
				var input = $(this);
				if (input.val() == '' || input.val() == input.attr('placeholder')) {
					input.addClass('placeholder');
					input.val(input.attr('placeholder'));
				}
			}).blur();
		};
});
function placeholderSupport() {
	return 'placeholder' in document.createElement('input');
}
function openWindow(url,name){
	var iHeight = 500;
	var iWidth = 800;
	var iTop = (window.screen.height-30-iHeight)/2; //获得窗口的垂直位置;  
	var iLeft = (window.screen.width-10-iWidth)/2; //获得窗口的水平位置;  
	window.open(url,name,'height='+iHeight+',,innerHeight='+iHeight+',width='+iWidth+',innerWidth='
		+iWidth+',top='+iTop+',left='+iLeft+
		',toolbar=no,menubar=no,scrollbars=yes,resizeable=no,location=no,status=no');  
}
function formSubmit(form){
	for(var i=0;i<form.length;i++){
		var element=form[i];
		if(element.name=="banks"){
			if(element.checked){
				form.submit();
				return;
			}
		}
	}
	alert('请选择银行！');
}
function accFormSubmit(form,flag){
	document.getElementById('loginInfo').innerHTML='';
	if(flag == 0){
		if(form.userId.value.length==0){
			document.getElementById('loginInfo').innerHTML='请输入登录名！';
			return;
		}
		if(form.loginPassword.value.length<6){
			document.getElementById('loginInfo').innerHTML='登录密码输入错误！';
			return;
		}
		if(form.validCode.value.length!=4){
			document.getElementById('loginInfo').innerHTML='请输入4位验证码！';
			return;
		}
	} else if(flag ==1){
		if(form.loginPassword.value.length<6){
			document.getElementById('loginInfo').innerHTML='支付密码输入错误！';
			return;
		}
	}
	form.submit();
}
</script>
