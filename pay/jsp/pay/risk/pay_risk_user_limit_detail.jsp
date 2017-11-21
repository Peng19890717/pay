<%@page import="java.text.SimpleDateFormat"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ page import="util.JWebConstant"%>
<%@ page import="com.jweb.dao.JWebUser"%>
<%@ page import="com.jweb.dao.JWebAction"%>
<%@ page import="com.jweb.service.JWebUserService"%>
<%@ page import="com.pay.risk.dao.PayRiskUserLimit"%>
<%@ page import="com.pay.risk.dao.PayRiskUserLimitSub"%>
<%@ page import="com.PayConstant"%>
<%
String path = request.getContextPath();
//登录和权限判断
JWebUser user = (JWebUser)session.getAttribute("user");
if(!JWebUserService.checkUser(user,request)){
%><script>window.location.href='<%=path %>/jsp/jweb/login.jsp'</script><%
    return;
}
PayRiskUserLimit payRiskUserLimit = (PayRiskUserLimit)request.getAttribute("payRiskUserLimit");
%>
<div class="easyui-layout" data-options="fit:true">
    <div data-options="region:'center'">
        <%if(payRiskUserLimit != null){ %>
        <table cellpadding="5" width="100%" style="margin-top:-10px;">
                <tr><td  width="160">&nbsp;</td><td width="200">&nbsp;</td><td width="120">&nbsp;</td><td>&nbsp;</td></tr>
                    <tr>
                        <td align="right">用户状态类型：</td>
                        <td colspan="3"><%=PayConstant.RISK_USER_TYPE.get(payRiskUserLimit.userType)+
                        	("3".equals(payRiskUserLimit.userType)?"（用户编号："+payRiskUserLimit.userCode+"）":"") %>
                        </td>
                    </tr>
                    <tr id="xListTypeFlagForAdd">
                        <td align="right">用户名单状态：</td>
                        <td colspan="3"><%=PayConstant.X_LIST_TYPE.get(payRiskUserLimit.xListType)==null?"":PayConstant.X_LIST_TYPE.get(payRiskUserLimit.xListType) %></td>
                    </tr>
                    <tr>
                        <td align="right">交易类型：</td>
                        <td colspan="3"><%=PayConstant.TRAN_TYPE.get(payRiskUserLimit.tranType) %></td>
                    </tr>
                    <tr>
                    	<td align="right">生效时间：</td>
                        <td colspan="3">
                        	<%if("0".equals(payRiskUserLimit.limitTimeFlag)){ %>永久
	                       	<%} else if("1".equals(payRiskUserLimit.limitTimeFlag)){ %>
	                       	<%= new SimpleDateFormat("yyyy-MM-dd").format(payRiskUserLimit.startDate) %>到<%= new SimpleDateFormat("yyyy-MM-dd").format(payRiskUserLimit.endDate) %>
	                       	<%} %>
                       	</td>
                    </tr>
                    <%if(PayConstant.TRAN_TYPE_RECHARGE.equals(payRiskUserLimit.tranType)||PayConstant.TRAN_TYPE_WITHDRAW.equals(payRiskUserLimit.tranType)
                    	||PayConstant.TRAN_TYPE_TRANSFER.equals(payRiskUserLimit.tranType)){ 
                    	PayRiskUserLimitSub tmp = payRiskUserLimit.subLimitList.get(0);
                    	%>
                    <tr>
                        <td colspan="4">
	                      <table cellpadding="5" width="100%">
	                       	<tr>
		                        <td width="160" align="right">单笔最小金额：</td>
		                        <td width="200"><%=String.format("%.2f", ((double)tmp.minAmt)/100d) %>元</td>
		                        <td width="120" align="right">单笔最大金额：</td>
		                        <td><%=String.format("%.2f", ((double)tmp.maxAmt)/100d) %>元</td>
		                    </tr>
		                    <tr>
		                        <td align="right">日交易次数上限：</td>
		                        <td><%=tmp.dayTimes %>次</td>
		                        <td align="right">日成功交易次数上限：</td>
		                        <td><%=tmp.daySucTimes %>次</td>
		                    </tr>
		                    <tr>
		                        <td align="right">日交易额上限：</td>
		                        <td><%=String.format("%.2f", ((double)tmp.dayAmt)/100d) %>元</td>
		                        <td align="right">日成功交易额上限：</td>
		                        <td><%=String.format("%.2f", ((double)tmp.daySucAmt)/100d) %>元</td>
		                    </tr>
		                    <tr>
		                        <td align="right">月交易次数上限：</td>
		                        <td><%=tmp.monthTimes %>次</td>
		                        <td align="right">月成功交易次数上限：</td>
		                        <td><%=tmp.monthSucTimes %>次</td>
		                    </tr>
		                    <tr>
		                        <td align="right">月交易额上限：</td>
		                        <td><%=String.format("%.2f", ((double)tmp.monthAmt)/100d) %>元</td>
		                        <td align="right">月成功交易额上限：</td>
		                        <td><%=String.format("%.2f", ((double)tmp.monthSucAmt)/100d) %>元</td>
		                    </tr>
	                      </table>
                       	</td>
                    </tr>
                    <% } else if(PayConstant.TRAN_TYPE_CONSUME.equals(payRiskUserLimit.tranType)){ %>
                    <tr>
                        <td colspan="4">
                        <%
                        java.util.Map<String,PayRiskUserLimitSub> tm = new java.util.HashMap<String,PayRiskUserLimitSub>();
                        for(int i=0; i<com.PayConstant.TRAN_TYPE_CONSUMES.length; i++){ 
	                    	PayRiskUserLimitSub tmp = payRiskUserLimit.subLimitList.get(i);
	                    	tm.put(tmp.limitBusClient, tmp);
	                    }
	                    for(int i=0; i<com.PayConstant.TRAN_TYPE_CONSUMES.length; i++){ 
	                    	PayRiskUserLimitSub tmp = tm.get("0"+i);
	                    	if(i==0){//账户支付，只显示成功交易部分
	                    	%>
	                      <table cellpadding="5" width="800">
	                      	<tr><td bgcolor="#CCCCCC" colspan="4"><%=com.PayConstant.TRAN_TYPE_CONSUMES[i] %></td></tr>
	                       	<tr>
		                        <td width="160" align="right">单笔最小金额：</td>
		                        <td width="200"><%=String.format("%.2f", ((double)tmp.minAmt)/100d) %>元</td>
		                        <td width="120" align="right">单笔最大金额：</td>
		                        <td><%=String.format("%.2f", ((double)tmp.maxAmt)/100d) %>元</td>
		                    </tr>
		                    <tr>
		                        <td align="right">日成功交易次数上限：</td>
		                        <td><%=tmp.daySucTimes %>次</td>
		                        <td align="right">日成功交易额上限：</td>
		                        <td><%=String.format("%.2f", ((double)tmp.daySucAmt)/100d) %>元</td>
		                    </tr>
		                    <tr>
		                        <td align="right">月成功交易次数上限：</td>
		                        <td><%=tmp.monthSucTimes %>次</td>
		                        <td align="right">月成功交易额上限：</td>
		                        <td><%=String.format("%.2f", ((double)tmp.monthSucAmt)/100d) %>元</td>
		                    </tr>
	                      </table><%
	                      } else {
	                      %><table cellpadding="5" width="800">
	                      	<tr><td bgcolor="#CCCCCC" colspan="4"><%=com.PayConstant.TRAN_TYPE_CONSUMES[i] %></td></tr>
	                       	<tr>
		                        <td width="160" align="right">单笔最小金额：</td>
		                        <td width="200"><%=String.format("%.2f", ((double)tmp.minAmt)/100d) %>元</td>
		                        <td width="120" align="right">单笔最大金额：</td>
		                        <td><%=String.format("%.2f", ((double)tmp.maxAmt)/100d) %>元</td>
		                    </tr>
		                    <tr>
		                        <td align="right">日交易次数上限：</td>
		                        <td><%=tmp.dayTimes %>次</td>
		                        <td align="right">日成功交易次数上限：</td>
		                        <td><%=tmp.daySucTimes %>次</td>
		                    </tr>
		                    <tr>
		                        <td align="right">日交易额上限：</td>
		                        <td><%=String.format("%.2f", ((double)tmp.dayAmt)/100d) %>元</td>
		                        <td align="right">日成功交易额上限：</td>
		                        <td><%=String.format("%.2f", ((double)tmp.daySucAmt)/100d) %>元</td>
		                    </tr>
		                    <tr>
		                        <td align="right">月交易次数上限：</td>
		                        <td><%=tmp.monthTimes %>次</td>
		                        <td align="right">月成功交易次数上限：</td>
		                        <td><%=tmp.monthSucTimes %>次</td>
		                    </tr>
		                    <tr>
		                        <td align="right">月交易额上限：</td>
		                        <td><%=String.format("%.2f", ((double)tmp.monthAmt)/100d) %>元</td>
		                        <td align="right">月成功交易额上限：</td>
		                        <td><%=String.format("%.2f", ((double)tmp.monthSucAmt)/100d) %>元</td>
		                    </tr>
	                      </table><%
	                      	}
	                      } %>
                       	</td>
                    </tr>
                    <%} else if(PayConstant.TRAN_TYPE_CARD.equals(payRiskUserLimit.tranType)){ %>
                    <tr>
                        <td colspan="4">
	                      <table cellpadding="5" width="100%">
	                       	<tr>
		                        <td width="160" align="right">信用卡单卡单笔最高限额：</td>
		                        <td><%=String.format("%.2f", ((double)payRiskUserLimit.crebitCardOnceAmt)/100d) %>元</td>
		                    </tr>
		                    <tr>
		                        <td align="right">信用卡单卡日累计最高限额：</td>
		                        <td><%=String.format("%.2f", ((double)payRiskUserLimit.crebitCardDayAmt)/100d) %>元</td>
		                    </tr>
		                    <tr>
		                        <td align="right">信用卡两次卡交易时间间隔：</td>
		                        <td><%=payRiskUserLimit.crebitCardTxnInterval %>秒（-1为无限制）</td>
		                    </tr>
		                    <tr>
		                        <td align="right">借记卡单卡单笔最高限额：</td>
		                        <td><%=String.format("%.2f", ((double)payRiskUserLimit.debitCardOnceAmt)/100d) %>元</td>
		                    </tr>
		                    <tr>
		                        <td align="right">借记卡单卡日累计最高限额：</td>
		                        <td><%=String.format("%.2f", ((double)payRiskUserLimit.debitCardDayAmt)/100d) %>元</td>
		                    </tr>
		                    <tr>
		                        <td align="right">借记卡两次卡交易时间间隔：</td>
		                        <td><%=payRiskUserLimit.debitCardTxnInterval %>秒（-1为无限制）</td>
		                    </tr>
	                      </table>
                       	</td>
                    </tr>
                    <%} %>
                    <tr>
                        <td width="160" align="right">启用标志：</td>
                        <td colspan="3">
                        	<%if("0".equals(payRiskUserLimit.isUse)){ %>启用
	                       	<%} else if("1".equals(payRiskUserLimit.isUse)){ %>
	                       	未启用
	                       	<%} %>
                        </td>
                    </tr>
                    <tr>
                        <td width="160" align="right" valign="top">备注：</td>
                        <td colspan="3"><%=payRiskUserLimit.remark==null?"":payRiskUserLimit.remark %></td>
                    </tr>
            </table>
        <%} %>
    </div>
    <div data-options="region:'south',border:false" style="text-align:right;padding:5px;">
        <a class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" href="javascript:void(0)" 
            onclick="$('#mainTabs').tabs('close',payRiskUserLimitDetailPageTitle)" style="width:80px">关闭</a>
    </div>
</div>
