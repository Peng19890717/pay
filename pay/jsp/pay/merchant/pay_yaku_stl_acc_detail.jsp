<%@page import="java.text.SimpleDateFormat"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ page import="util.JWebConstant"%>
<%@ page import="com.jweb.dao.JWebUser"%>
<%@ page import="com.jweb.dao.JWebAction"%>
<%@ page import="com.jweb.service.JWebUserService"%>
<%@ page import="com.pay.merchant.dao.PayYakuStlAcc"%>
<%
String path = request.getContextPath();
//登录和权限判断
JWebUser user = (JWebUser)session.getAttribute("user");
if(!JWebUserService.checkUser(user,request)){
%><script>window.location.href='<%=path %>/jsp/jweb/login.jsp'</script><%
    return;
}
PayYakuStlAcc payYakuStlAcc = (PayYakuStlAcc)request.getAttribute("payYakuStlAcc");
%>
<div class="easyui-layout" data-options="fit:true">
    <div data-options="region:'center'">
        <%if(payYakuStlAcc != null){ %>
        <table cellpadding="5" width="100%" style="margin-top:-10px;">
            <tr><td width="100">&nbsp;</td><td>&nbsp;</td></tr>
            <tr>
                <td align="right">merno：</td>
                <td><%=payYakuStlAcc.merno %></td>
            </tr>
            <tr>
                <td align="right">accName：</td>
                <td><%=payYakuStlAcc.accName %></td>
            </tr>
            <tr>
                <td align="right">bankCode：</td>
                <td><%=payYakuStlAcc.bankCode %></td>
            </tr>
            <tr>
                <td align="right">bankBranchName：</td>
                <td><%=payYakuStlAcc.bankBranchName %></td>
            </tr>
            <tr>
                <td align="right">accNo：</td>
                <td><%=payYakuStlAcc.accNo %></td>
            </tr>
            <tr>
                <td align="right">bankBranchCode：</td>
                <td><%=payYakuStlAcc.bankBranchCode %></td>
            </tr>
            <tr>
                <td align="right">province：</td>
                <td><%=payYakuStlAcc.province %></td>
            </tr>
            <tr>
                <td align="right">city：</td>
                <td><%=payYakuStlAcc.city %></td>
            </tr>
            <tr>
                <td align="right">credentialNo：</td>
                <td><%=payYakuStlAcc.credentialNo %></td>
            </tr>
            <tr>
                <td align="right">tel：</td>
                <td><%=payYakuStlAcc.tel %></td>
            </tr>
        </table>
        <%} %>
    </div>
    <div data-options="region:'south',border:false" style="text-align:right;padding:5px;">
        <a class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" href="javascript:void(0)" 
            onclick="$('#mainTabs').tabs('close',payYakuStlAccDetailPageTitle)" style="width:80px">关闭</a>
    </div>
</div>
