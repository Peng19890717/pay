<%@page import="java.text.SimpleDateFormat"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page import="util.JWebConstant"%>
<%@ page import="com.pay.contract.dao.PayContract"%>
<%
PayContract  contract= (PayContract)request.getAttribute("payContract");
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
	<div class="pageContent">
	   <table  style="margin-left:40px;margin-top:20px">
	    	<tr>
	    		<td height="30">商户编号：</td>
	    		<td height="30">${payContract.custId }</td>
	    	</tr>
	    	<tr>
	    		<td height="30">合同名称：</td>
	    		<td height="30">${payContract.pactName }</td>
	    	</tr>
	    	<tr>
	    		<td height="30">合同类型：</td>
	    		<td height="30">${payContract.pactType }</td>
	    	</tr>
	    	<tr>
	    		<td height="30">合同版本号：</td>
	    		<td height="30">${payContract.pactVersNo }</td>
	    	</tr>
	    	<tr>
	    		<td height="30">订立合同介质：</td>
	    		<td height="30">
	    			<c:if test="${payContract.crePactMed eq 0}">
	    				电子
	    			</c:if>
	    			<c:if test="${payContract.crePactMed eq 1}">
	    				书面
	    			</c:if>
	    		</td>
	    	</tr>
	    	<tr>
	    		<td height="30">订立合同渠道：</td>
	    		<td height="30">
	    			<c:if test="${payContract.crePactChnl eq 'WAP'}">
	    				WAP网页
	    			</c:if>
	    			<c:if test="${payContract.crePactChnl eq 'WEB'}">
	    				WEB网页
	    			</c:if>
	    			<c:if test="${payContract.crePactChnl eq 'MOB'}">
	    				手机
	    			</c:if>
	    			<c:if test="${payContract.crePactChnl eq 'TLR'}">
	    				柜面
	    			</c:if>
	    			<c:if test="${payContract.crePactChnl eq 'INB'}">
	    				网银
	    			</c:if>
	    		</td>
	    	</tr>
	    	<tr>
	    		<td height="30">订立合同日期：</td>
	    		<td height="30"><%=contract.crePactTime %></td>
	    	</tr>
	    	<tr>
	    		<td height="30">合同生效日期：</td>
	    		<td height="30"><%= new SimpleDateFormat("yyyy-MM-dd").format(contract.pactTakeEffDate)%></td>
	    	</tr>
	    	<tr>
	    		<td height="30">合同失效日期：</td>
	    		<td height="30"><%= new SimpleDateFormat("yyyy-MM-dd").format(contract.pactLoseEffDate)%></td>
	    	</tr>
	    	<tr>
	    		<td height="30">签约人：</td>
	    		<td height="30"><span class="nameHiddenFormat">${payContract.contractSignMan }</span></td>
	    	</tr>
	    	<tr>
	    		<td height="30" valign="top">售卖商品描述：</td>
	    		<td height="30">${payContract.sellProductInfo }</td>
	    	</tr>
	    	<tr>
	    		<td height="30">客户业务人员签字人：</td>
	    		<td height="30"><span class="nameHiddenFormat">${payContract.custBilaSignName }</span></td>
	    	</tr>
	    	<tr>
	    		<td height="30">业务人员签字人：</td>
	    		<td height="30"><span class="nameHiddenFormat">${payContract.bilaSignName }</span></td>
	    	</tr>
	    	<tr>
	    		<td height="30" valign="top">最后修改原因：</td>
	    		<td height="30">${payContract.updateInfo }</td>
	    	</tr>
	    	<tr>
	    		<td height="30" valign="top">协议内容：</td>
	    		<td height="30">${payContract.pactContent2 }</td>
	    	</tr>
	    </table>
	</div>