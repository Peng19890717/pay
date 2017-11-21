<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ page import="util.JWebConstant"%>
<%@ page import="com.jweb.dao.JWebUser"%>
<%@ page import="com.jweb.dao.JWebAction"%>
<%@ page import="com.jweb.service.JWebUserService"%>
<%@ page import="com.pay.coopbank.service.PayCoopBankService"%>
<%@ page import="com.pay.coopbank.service.PayChannelRotationService"%>
<%@ page import="com.pay.coopbank.dao.PayCoopBank"%>
<%
String path = request.getContextPath();
//登录和权限判断
JWebUser user = (JWebUser)session.getAttribute("user");
if(!JWebUserService.checkUser(user,request)){
%><script>window.location.href='<%=path %>/jsp/jweb/login.jsp'</script><%
    return;
}
%>
<div class="easyui-layout" data-options="fit:true">
    <div data-options="region:'center'">
        <form id="agentProfitTableDownloadForm" method="post">
            <table cellpadding="5" width="100%" style="margin-top:-10px;">
                <tr><td width="100">&nbsp;</td><td>&nbsp;</td></tr>
                <tr>
                    <td align="right">分润日期从：</td>
                        <td><input class="easyui-datebox" type="text" id="agentProfitDateStart" name="agentProfitDateStart"  value=""data-options="required:true" style="width:100px"/>
                        	到<input class="easyui-datebox" type="text" id="agentProfitDateEnd" name="agentProfitDateEnd" value="" data-options="required:true" style="width:100px"/>
                        </td>
                    </tr>
                    <tr>
                        <td align="right">分润类别：</td>
                        <td>
                        <select class="easyui-combobox" panelHeight="auto" id="agentProfitType" data-options="required:true,editable:false" name="agentProfitType">
			           		<option value="0">消费分润</option>
			           		<option value="1">代付分润</option>
			           		<!-- <option value="2">代收分润</option> -->
						</select>
                        </td>
                    </tr>
            </table>
        </form>
    </div>
    <div data-options="region:'south',border:false" style="text-align:right;padding:5px;">
        <a class="easyui-linkbutton" data-options="iconCls:'icon-ok'" href="javascript:agentProfitTableDownloadFormSubmit()" style="width:80px">下载</a>
    </div>
</div>
<script type="text/javascript">
$('#agentProfitTableDownloadForm').form({
    url:'<%=path %>/agentProfitTableDownload.htm?flag=0',
    onSubmit: function(){
        return $(this).form('validate');
    },
    success:function(data){
        if(data=='<%=JWebConstant.OK %>'){
            topCenterMessage('<%=JWebConstant.OK %>','下载成功！');
        } else topCenterMessage('<%=JWebConstant.ERROR %>',data);
    }
});
function agentProfitTableDownloadFormSubmit(){
    $('#agentProfitTableDownloadForm').submit();
}
</script>
