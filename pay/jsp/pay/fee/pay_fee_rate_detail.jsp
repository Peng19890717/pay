<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ page import="util.JWebConstant"%>
<%@ page import="com.jweb.dao.JWebUser"%>
<%@ page import="com.jweb.dao.JWebAction"%>
<%@ page import="com.jweb.service.JWebUserService"%>
<%@ page import="com.pay.fee.dao.PayFeeRate"%>
<%@ page import="com.pay.fee.dao.PayFeeSection"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
String path = request.getContextPath();
//登录和权限判断
JWebUser user = (JWebUser)session.getAttribute("user");
if(!JWebUserService.checkUser(user,request)){
%><script>window.location.href='<%=path %>/jsp/jweb/login.jsp'</script><%
    return;
}
PayFeeRate payFeeRate = (PayFeeRate)request.getAttribute("payFeeRate");
if(payFeeRate == null)return;
%>
<script type="text/javascript">
$(document).ready(function(){
});
</script>
<div class="easyui-layout" data-options="fit:true">
    <div data-options="region:'center'">
    <div class="merchant-lable">费率添加</div>
    <hr color="#ccc"/>
            <table cellpadding="5" width="100%">
                <tr>
                    <td align="right" width="100">费率代码:</td>
                    <td><%=payFeeRate.feeCode %></td>
                </tr>
                <tr>
                    <td align="right">费率名称:</td>
                    <td><%=payFeeRate.feeName %></td>
                </tr>
                <tr>
                    <td align="right">货币：</td>
                    <td><%=payFeeRate.ccy %></td>
                </tr>
                <tr>
                    <td align="right">客户类型:</td>
                    <td>
                    <%if(com.PayConstant.CUST_TYPE_USER.equals(payFeeRate.custType)){ %><%="个人" %>
                    <%}else if(com.PayConstant.CUST_TYPE_MERCHANT.equals(payFeeRate.custType)){ %><%="商户" %>
                     <%}else if(com.PayConstant.CUST_TYPE_PAY_CHANNEL.equals(payFeeRate.custType)){ %><%="合作渠道" %>
                    <%}else { %><%="未知" %><%} %>
                    </td>
                </tr>
                <tr>
                    <td align="right">交易类型:</td>
                    <td>
                    <%if("1".equals(payFeeRate.tranType)){ %><%="消费" %>
                    <%}else if("2".equals(payFeeRate.tranType)){ %><%="充值" %>
                    <%}else if("3".equals(payFeeRate.tranType)){ %><%="结算" %>
                    <%}else if("4".equals(payFeeRate.tranType)){ %><%="退款" %>
                    <%}else if("5".equals(payFeeRate.tranType)){ %><%="提现" %>
                    <%}else if("6".equals(payFeeRate.tranType)){ %><%="转账" %>
                    <%}else if("13".equals(payFeeRate.tranType)){ %><%="代理" %>
                    <%}else if("15".equals(payFeeRate.tranType)){ %><%="代收" %>
                    <%}else if("16".equals(payFeeRate.tranType)){ %><%="代付" %>
                    <%}else { %><%="未知" %><%} %>
                    </td>
                </tr>
            </table>
        <div class="merchant-lable">费率信息</div>
	    <hr color="#ccc"/>
	   <%if(payFeeRate.feeInfo != null){
	   
	   %><table id="payFeeRateSectionTable" name="payFeeRateSectionTable" style="margin-left:0px;" width="950px">
        	<%String [] fs = payFeeRate.feeInfo.split(";");
        	String tmp = "";
	        for(int i = 0; i<fs.length; i++){
	        	String [] es = fs[i].split(",");
	        	if(es.length != 3)continue;
	        	String [] fees = es[0].split("-");
	        	if(fees.length != 2)continue;
	      	%><tr>
            <td width="20%" align="right">
            	<%=String.format("%.2f", ((double)Double.parseDouble(fees[0]))/100d) %>
            </td>
            <td width="20%">
            	&lt;金额≤<%=String.format("%.2f", ((double)Double.parseDouble(fees[1]))/100d) %>
            </td>
            <td align="right" width="20%">计费方式:</td>
            <td>
            	<%=("0".equals(es[1])?"定额":"比例")%>
            </td>
			<%if("0".equals(es[1])){%>
			 		<td width="10%" align="right">费用金额:</td>
          			<td width="10%"><%="0".equals(es[1])?String.format("%.2f", ((double)Double.parseDouble(es[2]))/100d):"" %></td>
			<%}else {%>
					<td width="15%" align="right">费率比例:</td>
            		<td width="10%"><%="1".equals(es[1])?es[2]+"%":"" %></td>
            	<%}%>	
        	</tr>
        	<%} %>
        </table>
        <%} %>
    </div>
</div>
