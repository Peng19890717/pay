<%@page import="java.text.SimpleDateFormat"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ page import="util.JWebConstant"%>
<%@ page import="com.jweb.dao.JWebUser"%>
<%@ page import="com.jweb.dao.JWebAction"%>
<%@ page import="com.jweb.service.JWebUserService"%>
<%@ page import="com.pay.usercard.dao.PayTranUserCard"%>
<%
String path = request.getContextPath();
//登录和权限判断
JWebUser user = (JWebUser)session.getAttribute("user");
if(!JWebUserService.checkUser(user,request)){
%><script>window.location.href='<%=path %>/jsp/jweb/login.jsp'</script><%
    return;
}
PayTranUserCard payTranUserCard = (PayTranUserCard)request.getAttribute("payTranUserCard");
%>
<div class="easyui-layout" data-options="fit:true">
    <div data-options="region:'center'">
        <%if(payTranUserCard != null){ %>
        <table cellpadding="5" width="100%" style="margin-top:-10px;">
            <tr><td width="100">&nbsp;</td><td>&nbsp;</td></tr>
            <tr>
                <td align="right">id：</td>
                <td><%=payTranUserCard.id %></td>
            </tr>
            <tr>
                <td align="right">userId：</td>
                <td><%=payTranUserCard.userId %></td>
            </tr>
            <tr>
                <td align="right">cardBank：</td>
                <td><%=payTranUserCard.cardBank %></td>
            </tr>
            <tr>
                <td align="right">cardType：</td>
                <td><%=payTranUserCard.cardType %></td>
            </tr>
            <tr>
                <td align="right">cardStatus：</td>
                <td><%=payTranUserCard.cardStatus %></td>
            </tr>
            <tr>
                <td align="right">cardStaRes：</td>
                <td><%=payTranUserCard.cardStaRes %></td>
            </tr>
            <tr>
                <td align="right">cardNo：</td>
                <td><%=payTranUserCard.cardNo %></td>
            </tr>
            <tr>
                <td align="right">cardBankBranch：</td>
                <td><%=payTranUserCard.cardBankBranch %></td>
            </tr>
            <tr>
                <td align="right">bakOpenTime：</td>
                <td><%=payTranUserCard.bakOpenTime != null ? new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payTranUserCard.bakOpenTime):"" %></td>
            </tr>
            <tr>
                <td align="right">bakOpenNum：</td>
                <td><%=payTranUserCard.bakOpenNum %></td>
            </tr>
            <tr>
                <td align="right">bakCloseTime：</td>
                <td><%=payTranUserCard.bakCloseTime != null ? new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payTranUserCard.bakCloseTime):"" %></td>
            </tr>
            <tr>
                <td align="right">bakCloseNum：</td>
                <td><%=payTranUserCard.bakCloseNum %></td>
            </tr>
            <tr>
                <td align="right">bakCloseRes：</td>
                <td><%=payTranUserCard.bakCloseRes %></td>
            </tr>
            <tr>
                <td align="right">bakUserId：</td>
                <td><%=payTranUserCard.bakUserId %></td>
            </tr>
            <tr>
                <td align="right">bakUpdTime：</td>
                <td><%=payTranUserCard.bakUpdTime != null ? new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payTranUserCard.bakUpdTime):"" %></td>
            </tr>
            <tr>
                <td align="right">revFlag：</td>
                <td><%=payTranUserCard.revFlag %></td>
            </tr>
        </table>
        <%} %>
    </div>
    <div data-options="region:'south',border:false" style="text-align:right;padding:5px;">
        <a class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" href="javascript:void(0)" 
            onclick="$('#mainTabs').tabs('close',payTranUserCardDetailPageTitle)" style="width:80px">关闭</a>
    </div>
</div>
