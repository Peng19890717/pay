<%@page import="java.text.SimpleDateFormat"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ page import="util.JWebConstant"%>
<%@ page import="com.jweb.dao.JWebUser"%>
<%@ page import="com.jweb.dao.JWebAction"%>
<%@ page import="com.jweb.service.JWebUserService"%>
<%@ page import="com.pay.muser.dao.PayMerchantUser"%>
<%
String path = request.getContextPath();
//登录和权限判断
JWebUser user = (JWebUser)session.getAttribute("user");
if(!JWebUserService.checkUser(user,request)){
%><script>window.location.href='<%=path %>/jsp/jweb/login.jsp'</script><%
    return;
}
PayMerchantUser payMerchantUser = (PayMerchantUser)request.getAttribute("payMerchantUser");
%>
<div class="easyui-layout" data-options="fit:true">
    <div data-options="region:'center'">
        <%if(payMerchantUser != null){ %>
        <table cellpadding="5">
            <tr><td width="20%">&nbsp;</td><td width="80%">&nbsp;</td></tr>
            <tr>
                <td align="right">userId：</td>
                <td><%=payMerchantUser.userId %></td>
            </tr>
            <tr>
                <td align="right">custId：</td>
                <td><%=payMerchantUser.custId %></td>
            </tr>
            <tr>
                <td align="right">userNam：</td>
                <td><%=payMerchantUser.userNam %></td>
            </tr>
            <tr>
                <td align="right">userPwd：</td>
                <td><%=payMerchantUser.userPwd %></td>
            </tr>
            <tr>
                <td align="right">creaSign：</td>
                <td><%=payMerchantUser.creaSign %></td>
            </tr>
            <tr>
                <td align="right">random：</td>
                <td><%=payMerchantUser.random %></td>
            </tr>
            <tr>
                <td align="right">userDate：</td>
                <td><%=payMerchantUser.userDate != null ? new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payMerchantUser.userDate):"" %></td>
            </tr>
            <tr>
                <td align="right">state：</td>
                <td><%=payMerchantUser.state %></td>
            </tr>
            <tr>
                <td align="right">descInf：</td>
                <td><%=payMerchantUser.descInf %></td>
            </tr>
            <tr>
                <td align="right">tel：</td>
                <td><%=payMerchantUser.tel %></td>
            </tr>
            <tr>
                <td align="right">email：</td>
                <td><%=payMerchantUser.email %></td>
            </tr>
            <tr>
                <td align="right">loginFailCount：</td>
                <td><%=payMerchantUser.loginFailCount %></td>
            </tr>
            <tr>
                <td align="right">currentLoginTime：</td>
                <td><%=payMerchantUser.currentLoginTime != null ? new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payMerchantUser.currentLoginTime):"" %></td>
            </tr>
            <tr>
                <td align="right">preset1：</td>
                <td><%=payMerchantUser.preset1 %></td>
            </tr>
            <tr>
                <td align="right">lastUppwdDate：</td>
                <td><%=payMerchantUser.lastUppwdDate != null ? new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payMerchantUser.lastUppwdDate):"" %></td>
            </tr>
            <tr>
                <td align="right">userExpiredTime：</td>
                <td><%=payMerchantUser.userExpiredTime %></td>
            </tr>
            <tr>
                <td align="right">mailflg：</td>
                <td><%=payMerchantUser.mailflg %></td>
            </tr>
        </table>
        <%} %>
    </div>
    <div data-options="region:'south',border:false" style="text-align:right;padding:5px;">
        <a class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" href="javascript:void(0)" 
            onclick="$('#mainTabs').tabs('close',payMerchantUserDetailPageTitle)" style="width:80px">关闭</a>
    </div>
</div>
