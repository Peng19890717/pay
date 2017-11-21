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
        <form id="accountFileForm" method="post">
            <table cellpadding="5" width="100%" style="margin-top:-10px;">
                <tr><td width="100">&nbsp;</td><td>&nbsp;</td></tr>
                    <tr>
                        <td align="right">支付渠道：</td>
                        <td>
                        <select class="easyui-combobox" panelHeight="auto" id="accountFileId" data-options="required:true,editable:false" name="accountId">
                        	<option value=""></option>
					        <%
			           		java.util.Iterator<String> it = PayCoopBankService.CHANNEL_MAP_ALL.keySet().iterator();
			           		while(it.hasNext()){
			           			String key = it.next();
			           			String value = ((PayCoopBank)PayCoopBankService.CHANNEL_MAP_ALL.get(key)).getBankName();
			           			%><option value="<%=key%>"><%=value%></option>
			           		<%}%>
						  </select>
                        </td>
                    </tr>
                    <tr>
                        <td align="right">账单日期：</td>
                        <td><input class="easyui-datebox" type="text" id="accountFileDate" name="accountDate" missingMessage="请输入账单日期" value="" 
                        	data-options="required:true" invalidMessage="参数值必填"/>
                        </td>
                    </tr>
                    <tr>
                        <td align="right">账号信息：</td>
                        <td>（格式：“账号:密码[回车]...”）</td>
                    </tr>
                    <tr>
                    	<td>&nbsp;</td>
                        <td>
                        	<input class="easyui-textbox" type="text" id="accountInfos" name="accInfos"
                          		data-options="multiline:true" style="width:400px;height:600px"/>
                        </td>
                    </tr>
            </table>
        </form>
    </div>
    <div data-options="region:'south',border:false" style="text-align:right;padding:5px;">
        <a class="easyui-linkbutton" data-options="iconCls:'icon-ok'" href="javascript:accountFileFormSubmit()" style="width:80px">下载</a>
        <a class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" href="javascript:void(0)" 
            onclick="$('#accountFileForm').form('clear')" style="width:80px">取消</a>
    </div>
</div>
<script type="text/javascript">
$('#accountFileForm').form({
    url:'<%=path %>/accountFileDownload.htm?flag=0',
    onSubmit: function(){
        return $(this).form('validate');
    },
    success:function(data){
        if(data=='<%=JWebConstant.OK %>'){
            topCenterMessage('<%=JWebConstant.OK %>','下载成功！');
        } else topCenterMessage('<%=JWebConstant.ERROR %>',data);
    }
});
function accountFileFormSubmit(){
    $('#accountFileForm').submit();
}
</script>
