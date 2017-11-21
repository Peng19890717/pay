<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ page import="util.JWebConstant"%>
<%@ page import="com.jweb.dao.JWebUser"%>
<%@ page import="com.jweb.dao.JWebAction"%>
<%@ page import="com.jweb.service.JWebUserService"%>
<%@ page import="com.pay.bank.dao.PayBank"%>
<%
String path = request.getContextPath();
//登录和权限判断
JWebUser user = (JWebUser)session.getAttribute("user");
if(!JWebUserService.checkUser(user,request)){
%><script>window.location.href='<%=path %>/jsp/jweb/login.jsp'</script><%
    return;
}
PayBank payBank = (PayBank)request.getAttribute("payBankUpdate");
%>
<script type="text/javascript">
$(document).ready(function(){});
</script>
<div class="easyui-layout" data-options="fit:true">
    <div data-options="region:'center'">
        <%if(payBank != null){ %>
        <form id="updatePayBankForm" method="post">
        	<input type="hidden" name="id" value="<%=payBank.id != null ? payBank.id : "" %>"/>
            <table cellpadding="5" width="100%" style="margin-top:-10px;">
                <tr><td width="100">&nbsp;</td><td>&nbsp;</td></tr>
                    <tr>
                        <td align="right">银行编码：</td>
                        <td><input class="easyui-textbox" type="text" id="updatePayBankBankCode" name="bankCode" missingMessage="请输入银行编码"
                                validType="length[1,20]" invalidMessage="bankCode为1-20个字符" data-options="required:true"
                                value="<%=payBank.bankCode != null ? payBank.bankCode : "" %>"/></td>
                    </tr>
                    <tr>
                        <td align="right">银行名称：</td>
                        <td><input class="easyui-textbox" type="text" id="updatePayBankBankName" name="bankName" missingMessage="请输入银行名称"
                                validType="length[1,20]" invalidMessage="bankName为1-20个字符" data-options="required:true"
                                value="<%=payBank.bankName != null ? payBank.bankName : "" %>"/></td>
                    </tr>
            </table>
        </form>
        <%} %>
    </div>
    <div data-options="region:'south',border:false" style="text-align:right;padding:5px;">
        <a class="easyui-linkbutton" data-options="iconCls:'icon-ok'" href="javascript:updatePayBankFormSubmit()" style="width:80px">确定</a>
        <a class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" href="javascript:void(0)" 
            onclick="$('#mainTabs').tabs('close',payBankUpdatePageTitle)" style="width:80px">取消</a>
    </div>
</div>
<script type="text/javascript">
$('#updatePayBankForm').form({
    url:'<%=path %>/updatePayBank.htm',
    onSubmit: function(){
        return $(this).form('validate');
    },
    success:function(data){
        $('#payBankList').datagrid('reload');
        if(data=='<%=JWebConstant.OK %>'){
            topCenterMessage('<%=JWebConstant.OK %>','修改成功！');
            closeTabFreshList('payBank',payBankUpdatePageTitle,payBankListPageTitle,'payBankList','<%=path %>/payBank.htm');
        } else topCenterMessage('<%=JWebConstant.ERROR %>',data);
    }
});
function updatePayBankFormSubmit(){
    $('#updatePayBankForm').submit();
}
</script>
