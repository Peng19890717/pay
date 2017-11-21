<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ page import="util.JWebConstant"%>
<%@ page import="com.jweb.dao.JWebUser"%>
<%@ page import="com.jweb.dao.JWebAction"%>
<%@ page import="com.jweb.service.JWebUserService"%>
<%@ page import="com.pay.business.dao.PayBusinessParameter"%>
<%
String path = request.getContextPath();
//登录和权限判断
JWebUser user = (JWebUser)session.getAttribute("user");
if(!JWebUserService.checkUser(user,request)){
%><script>window.location.href='<%=path %>/jsp/jweb/login.jsp'</script><%
    return;
}
PayBusinessParameter payBusinessParameter = (PayBusinessParameter)request.getAttribute("payBusinessParameterUpdate");
%>
<script type="text/javascript">
$(document).ready(function(){});
</script>
<div class="easyui-layout" data-options="fit:true">
    <div data-options="region:'center'">
        <%if(payBusinessParameter != null){ %>
        <form id="updatePayBusinessParameterForm" method="post">
            <table cellpadding="5" width="100%" style="margin-top:-10px;">
                <tr><td width="100">&nbsp;</td><td>&nbsp;</td></tr>
                    <tr>
                        <td align="right">变量名：</td>
                        <td><input class="easyui-textbox" type="text" id="updatePayBusinessParameterName" name="name" missingMessage="请输入变量名"
                                validType="length[1,100]" invalidMessage="参数名为1-100个字符" data-options="required:true"
                                value="<%=payBusinessParameter.name != null ? payBusinessParameter.name : "" %>"/></td>
                    </tr>
                    <tr>
                        <td align="right">变量值：</td>
                        <td><input class="easyui-textbox" type="text" id="updatePayBusinessParameterValue" name="value" missingMessage="请输入变量值"
                                validType="length[1,256]" invalidMessage="参数值为1-256个字符" data-options="required:true"
                                value="<%=payBusinessParameter.value != null ? payBusinessParameter.value : "" %>"/></td>
                    </tr>
                    <tr>
                        <td align="right">备注：</td>
                        <td><input class="easyui-textbox" type="text" id="updatePayBusinessParameterRemark" name="remark" missingMessage="请输入备注"
                                validType="length[0,200]" invalidMessage="备注最大为 200 个字符" data-options="multiline:true" style="width:200px;height:70px"
                                value="<%=payBusinessParameter.remark != null ? payBusinessParameter.remark : "" %>"/></td>
                    </tr>
            </table>
        </form>
        <%} %>
    </div>
    <div data-options="region:'south',border:false" style="text-align:right;padding:5px;">
        <a class="easyui-linkbutton" data-options="iconCls:'icon-ok'" href="javascript:updatePayBusinessParameterFormSubmit()" style="width:80px">确定</a>
        <a class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" href="javascript:void(0)" 
            onclick="$('#mainTabs').tabs('close',payBusinessParameterUpdatePageTitle)" style="width:80px">取消</a>
    </div>
</div>
<script type="text/javascript">
$('#updatePayBusinessParameterForm').form({
    url:'<%=path %>/updatePayBusinessParameter.htm',
    onSubmit: function(){
        return $(this).form('validate');
    },
    success:function(data){
        $('#payBusinessParameterList').datagrid('reload');
        if(data=='<%=JWebConstant.OK %>'){
            topCenterMessage('<%=JWebConstant.OK %>','修改成功！');
            closeTabFreshList('payBusinessParameter',payBusinessParameterUpdatePageTitle,payBusinessParameterListPageTitle,'payBusinessParameterList','<%=path %>/payBusinessParameter.htm');
        } else topCenterMessage('<%=JWebConstant.ERROR %>',data);
    }
});
function updatePayBusinessParameterFormSubmit(){
    $('#updatePayBusinessParameterForm').submit();
}
</script>
