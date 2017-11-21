<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ page import="util.JWebConstant"%>
<%@ page import="com.jweb.dao.JWebUser"%>
<%@ page import="com.jweb.dao.JWebAction"%>
<%@ page import="com.jweb.service.JWebUserService"%>
<%
String path = request.getContextPath();
//登录和权限判断
JWebUser user = (JWebUser)session.getAttribute("user");
if(!JWebUserService.checkUser(user,request)){
%><script>window.location.href='<%=path %>/jsp/jweb/login.jsp'</script><%
    return;
}
%>
<script type="text/javascript">
$(document).ready(function(){});
</script>
<div class="easyui-layout" data-options="fit:true">
    <div data-options="region:'center'">
        <form id="addPayBankForm" method="post">
            <table cellpadding="5" width="100%" style="margin-top:-10px;">
                <tr><td width="100">&nbsp;</td><td>&nbsp;</td></tr>
                    <tr>
                        <td align="right">银行编码：</td>
                        <td><input class="easyui-textbox" type="text" id="addPayBankBankCode" name="bankCode" missingMessage="请输入银行编码"
                                validType="length[1,20]" invalidMessage="bankCode为1-20个字符" data-options="required:true"/></td>
                    </tr>
                    <tr>
                        <td align="right">银行名称：</td>
                        <td><input class="easyui-textbox" type="text" id="addPayBankBankName" name="bankName" missingMessage="请输入银行名称"
                                validType="length[1,20]" invalidMessage="bankName为1-20个字符" data-options="required:true"/></td>
                    </tr>
            </table>
        </form>
    </div>
    <div data-options="region:'south',border:false" style="text-align:right;padding:5px;">
        <a class="easyui-linkbutton" data-options="iconCls:'icon-ok'" href="javascript:addPayBankFormSubmit()" style="width:80px">确定</a>
        <a class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" href="javascript:void(0)" 
            onclick="$('#addPayBankForm').form('clear')" style="width:80px">取消</a>
    </div>
</div>
<script type="text/javascript">
$('#addPayBankForm').form({
    url:'<%=path %>/addPayBank.htm',
    onSubmit: function(){
        return $(this).form('validate');
    },
    success:function(data){
        if(data=='<%=JWebConstant.OK %>'){
            topCenterMessage('<%=JWebConstant.OK %>','添加成功！');
            closeTabFreshList('payBank',payBankAddPageTitle,payBankListPageTitle,'payBankList','<%=path %>/payBank.htm');
        } else topCenterMessage('<%=JWebConstant.ERROR %>',data);
    }
});
function addPayBankFormSubmit(){
    $('#addPayBankForm').submit();
}
</script>
