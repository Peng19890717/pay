<%@page import="java.text.SimpleDateFormat"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ page import="util.JWebConstant"%>
<%@ page import="com.jweb.dao.JWebUser"%>
<%@ page import="com.jweb.dao.JWebAction"%>
<%@ page import="com.jweb.service.JWebUserService"%>
<%@ page import="com.pay.account.dao.PayAccEntry"%>
<%
String path = request.getContextPath();
//登录和权限判断
JWebUser user = (JWebUser)session.getAttribute("user");
if(!JWebUserService.checkUser(user,request)){
%><script>window.location.href='<%=path %>/jsp/jweb/login.jsp'</script><%
    return;
}
PayAccEntry payAccEntry = (PayAccEntry)request.getAttribute("payAccEntry");
%>
<div class="easyui-layout" data-options="fit:true">
    <div data-options="region:'center'">
        <%if(payAccEntry != null){ %>
        <table cellpadding="5" width="100%" style="margin-top:-10px;">
            <tr><td width="100">&nbsp;</td><td>&nbsp;</td></tr>
            <tr>
                <td align="right">txnCod：</td>
                <td><%=payAccEntry.txnCod %></td>
            </tr>
            <tr>
                <td align="right">txnSubCod：</td>
                <td><%=payAccEntry.txnSubCod %></td>
            </tr>
            <tr>
                <td align="right">accSeq：</td>
                <td><%=payAccEntry.accSeq %></td>
            </tr>
            <tr>
                <td align="right">drCrFlag：</td>
                <td><%=payAccEntry.drCrFlag %></td>
            </tr>
            <tr>
                <td align="right">subjectFrom：</td>
                <td><%=payAccEntry.subjectFrom %></td>
            </tr>
            <tr>
                <td align="right">subject：</td>
                <td><%=payAccEntry.subject %></td>
            </tr>
            <tr>
                <td align="right">accOrgNo：</td>
                <td><%=payAccEntry.accOrgNo %></td>
            </tr>
            <tr>
                <td align="right">rmkCod：</td>
                <td><%=payAccEntry.rmkCod %></td>
            </tr>
        </table>
        <%} %>
    </div>
    <div data-options="region:'south',border:false" style="text-align:right;padding:5px;">
        <a class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" href="javascript:void(0)" 
            onclick="$('#mainTabs').tabs('close',payAccEntryDetailPageTitle)" style="width:80px">关闭</a>
    </div>
</div>
