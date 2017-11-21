<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ page import="com.pay.merchantinterface.service.PayRequest"%>
<%
PayRequest payRequest = (PayRequest)request.getAttribute("payRequest");
%>
<!DOCTYPE>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<script type="text/javascript" src="<%=request.getContextPath() %>/js/jquery-easyui-1.4/jquery.min.js"></script>
<title>钱通收银台</title>
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
fieldset,img{border:0;vertical-align:top;}
textarea{resize:none;}
a,input,button,select,textarea{outline:none;}
a,button{cursor:pointer;}
table{border-collapse:collapse;border-spacing:0}
html{overflow-y:scroll;}
.main .header{ height:78px; border-bottom:2px solid #f38a50;}
.main .header .headers{ width:980px; height:78px; margin:0 auto; overflow:hidden;}
.main .header .headersL{ float:left; height:58px; padding:20px 0px 0px 15px; overflow:hidden;}
.main .header .headersL h2{ float:left; overflow:hidden; width:154px; height:41px; margin-right:14px; }
.main .header .headersL .Cas{ float:left; width:130px; text-align:center; height:40px; border-left:2px solid #c9c9c9; margin-top:-2px;}
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

.main .conts .change  .li01{ padding:25px 0px 30px 0px; overflow:hidden; }
.main .conts .change  .li01 p{ font-size:14px; padding-left:45px;}
.main .conts .change  .li01 p span{ font-size:12px;}
.main .conts .change  .li01 ul{ overflow:hidden; padding-top:15px; padding-left:20px;}
.main .conts .change  .li01 ul li{ float:left; width:176px; height:44px; line-height:44px; border:1px solid #e8e8e8; margin-bottom:10px; margin-right:10px; cursor:pointer;}
.main .conts .change  .li01 ul li input{ display:inline-block; vertical-align:middle;margin-right:-5px;}
.main .conts .change  .li01 ul li label{ display:inline-block; vertical-align:middle;}
.main .conts .change  .li01 .bankBill,.main .conts .change  .li02 .bankBills{ display:none; margin-left:20px; margin-top:10px; border:1px solid #999;}
.main .conts .change  .li01 .que,.main .conts .change  .li02 .ques{ display:block; font-size:16px; width:120px; height:35px; text-align:center; line-height:35px; color:#fff; background:#e6793e; margin-left:20px; margin-top:10px; border-radius:2px;}
 
.main .conts .change  .li02{ padding:25px 0px 30px 0px; overflow:hidden; display:none;}
.main .conts .change  .li02 p{ font-size:14px; padding-left:45px;}
.main .conts .change  .li02 p span{ font-size:12px;}
.main .conts .change  .li02 ul{ overflow:hidden; padding-top:15px; padding-left:20px;}
.main .conts .change  .li02 ul li{ float:left; width:176px; height:44px; line-height:44px; border:1px solid #e8e8e8; margin-bottom:10px; margin-right:10px; cursor:pointer;}
.main .conts .change  .li02 ul li input{ display:inline-block; vertical-align:middle;margin-right:-5px;}
.main .conts .change  .li02 ul li label{ display:inline-block; vertical-align:middle;}
.main .conts .change  .li02 .nexts{ display:block; width:165px; height:45px; text-align:center; line-height:45px; color:#fff; background:#e6793e; border-radius:2px; margin-left:20px;}

.main .content .contP{border-top:1px solid #e5ecf1; padding:20px 0px 30px 50px;}
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
                    <h2><a href="javascript:;"><img src="${ pageContext.request.contextPath }/images/logo.png" width="154" height="41" /></a>钱通支付</h2>
                    <div class="Cas">
                    	<p>钱通收银台</p>
                        <a target="_blank" href="https://www.qtongpay.com">www.qtongpay.com</a>
                    </div>
                </div>
                <div class="headersR">
                	<ul>
                    	<!-- li><a href="javascript:;">手机钱通</a>&nbsp;|</li-->
                        <li>客服电话：<a href="javascript:;">4009929819</a></li>
                        <!--li><a href="javascript:;">常见问题</a></li-->
                    </ul>
                </div>
            </div>
        </div>
        <div class="content">
        	<div class="cont">
            	<div class="contL">
            		<c:if test="${not empty payRequest.merchantName}">
	    				<p>商户名称：${ payRequest.merchantName }</p>
	    			</c:if>
                    <p>商品名称：${ payRequest.merchantOrderDesc }</p>
                    <p>订单编号：${ payRequest.merchantOrderId }</p>
                    <p>订单时间：${ payRequest.orderTime }</p>
                </div>
                <div class="contR">
                	<p>应付金额：</p>
                    <p><a href="javascript:;"><%=String.format("%.2f",(double)(payRequest.payOrder.txamt)*0.01) %></a>元</p>
                </div>
            </div>
            <div class="conts">
                <div class="change">
                <div style="margin-left:40px">
                	<h3>支付完成</h3>
                	<div style="text-align:center;font-size:18px;margin-top:50px;">
                	<%if(payRequest!=null){ %>支付完成，<a href="<%=payRequest.merchantFrontEndUrl %>">返回商家</a><%
                	} else {%>超时<%} %>
                	</div>
                </div>
                <div class="contP" style="margin-top:200px">
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
    <div class="bots">
        <div class="address">
            <div class="addsL">
                <p>钱通支付有限公司&nbsp;版权所有&nbsp; ©2015-2025 &nbsp;经营许可证编号：京B2-20100458&nbsp; 客服电话：4009929819</p>
                <div class="addsPic"><img src="${ pageContext.request.contextPath }/images/pic21.png" width="338" height="53" /></div>          
            </div>
        </div>
    </div>
</body>
</html>
