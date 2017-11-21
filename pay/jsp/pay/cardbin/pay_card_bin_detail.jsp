<%@page import="java.text.SimpleDateFormat"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ page import="util.JWebConstant"%>
<%@ page import="com.jweb.dao.JWebUser"%>
<%@ page import="com.jweb.dao.JWebAction"%>
<%@ page import="com.jweb.service.JWebUserService"%>
<%@ page import="com.pay.cardbin.dao.PayCardBin"%>
<%
String path = request.getContextPath();
//登录和权限判断
JWebUser user = (JWebUser)session.getAttribute("user");
if(!JWebUserService.checkUser(user,request)){
%><script>window.location.href='<%=path %>/jsp/jweb/login.jsp'</script><%
    return;
}
PayCardBin payCardBin = (PayCardBin)request.getAttribute("payCardBin");
%>
<div class="easyui-layout" data-options="fit:true">
    <div data-options="region:'center'">
        <%if(payCardBin != null){ %>
        <table cellpadding="5" width="100%" style="margin-top:-10px;">
            <tr><td width="100">&nbsp;</td><td>&nbsp;</td></tr>
            <tr>
                <td align="right">binId：</td>
                <td><%=payCardBin.binId %></td>
            </tr>
            <tr>
                <td align="right">bankCode：</td>
                <td><%=payCardBin.bankCode %></td>
            </tr>
            <tr>
                <td align="right">cardType：</td>
                <td><%=payCardBin.cardType %></td>
            </tr>
            <tr>
                <td align="right">cardName：</td>
                <td><%=payCardBin.cardName %></td>
            </tr>
            <tr>
                <td align="right">binNo：</td>
                <td><%=payCardBin.binNo %></td>
            </tr>
            <tr>
                <td align="right">cardLength：</td>
                <td><%=payCardBin.cardLength %></td>
            </tr>
            <tr>
                <td align="right">bankNo：</td>
                <td><%=payCardBin.bankNo %></td>
            </tr>
            <tr>
                <td align="right">extensions：</td>
                <td><%=payCardBin.extensions %></td>
            </tr>
            <tr>
                <td align="right">enableFlag：</td>
                <td><%=payCardBin.enableFlag %></td>
            </tr>
            <tr>
                <td align="right">memo：</td>
                <td><%=payCardBin.memo %></td>
            </tr>
            <tr>
                <td align="right">version：</td>
                <td><%=payCardBin.version %></td>
            </tr>
            <tr>
                <td align="right">gmtCreate：</td>
                <td><%=payCardBin.gmtCreate != null ? new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payCardBin.gmtCreate):"" %></td>
            </tr>
            <tr>
                <td align="right">gmtModify：</td>
                <td><%=payCardBin.gmtModify != null ? new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payCardBin.gmtModify):"" %></td>
            </tr>
            <tr>
                <td align="right">bankName：</td>
                <td><%=payCardBin.bankName %></td>
            </tr>
        </table>
        <%} %>
    </div>
    <div data-options="region:'south',border:false" style="text-align:right;padding:5px;">
        <a class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" href="javascript:void(0)" 
            onclick="$('#mainTabs').tabs('close',payCardBinDetailPageTitle)" style="width:80px">关闭</a>
    </div>
</div>
