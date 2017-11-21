<%@page import="java.text.SimpleDateFormat"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ page import="util.JWebConstant"%>
<%@ page import="com.jweb.dao.JWebUser"%>
<%@ page import="com.jweb.dao.JWebAction"%>
<%@ page import="com.jweb.service.JWebUserService"%>
<%@ page import="com.pay.merchant.dao.PayMerchantNews"%>
<%
String path = request.getContextPath();
//登录和权限判断
JWebUser user = (JWebUser)session.getAttribute("user");
if(!JWebUserService.checkUser(user,request)){
%><script>window.location.href='<%=path %>/jsp/jweb/login.jsp'</script><%
    return;
}
PayMerchantNews payMerchantNews = (PayMerchantNews)request.getAttribute("payMerchantNews");
%>
<div class="easyui-layout" data-options="fit:true">
    <div data-options="region:'center'">
        <%if(payMerchantNews != null){ %>
        <table cellpadding="5" width="100%" style="margin-top:-10px;">
            <tr><td width="100">&nbsp;</td><td>&nbsp;</td></tr>
            <tr>
                <td align="right">类型：</td>
                <td>
                	<%=payMerchantNews.type == null?"":PayMerchantNews.typeMap.get(payMerchantNews.type)%>
                </td>
            </tr>
            <tr>
                <td align="right">标题：</td>
                <td><%=payMerchantNews.title %></td>
            </tr>
            <tr>
                <td align="right">内容：</td>
                <td><%=payMerchantNews.content %></td>
            </tr>
            <tr>
                <td align="right">创建时间：</td>
                <td><%=payMerchantNews.optTime != null ? new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payMerchantNews.optTime):"" %></td>
            </tr>
            <tr>
                <td align="right">用户ID：</td>
                <td><%=payMerchantNews.optUserId %></td>
            </tr>
            <tr>
                <td align="right">状态：</td>
                <td><%=payMerchantNews.flag2==null?"":PayMerchantNews.flagMap.get(payMerchantNews.flag2) %></td>
            </tr>
        </table>
        <%} %>
    </div>
    <div data-options="region:'south',border:false" style="text-align:right;padding:5px;">
        <a class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" href="javascript:void(0)" 
            onclick="$('#mainTabs').tabs('close',payMerchantNewsDetailPageTitle)" style="width:80px">关闭</a>
    </div>
</div>
