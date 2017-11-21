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
        <form id="addPayMerchantUserForm" method="post" action="addPayMerchantUser.htm">
            <table cellpadding="5">
                <tr><td width="20%">&nbsp;</td><td width="80%">&nbsp;</td></tr>
                    <tr>
                        <td align="right">商户编号：</td>
                        <td><input class="easyui-textbox" type="text" id="addPayMerchantUserCustId" name="custId" missingMessage="请输入商户编号"
                                validType="length[7,12]" invalidMessage="商户编号为1-20个字符" data-options="required:true"/></td>
                    </tr>
                    <tr>
                        <td align="right">用户名称：</td>
                        <td><input class="easyui-textbox" type="text" id="addPayMerchantUserUserNam" name="userNam" missingMessage="请输入用户名称"
                                validType="length[3,8]" invalidMessage="用户名称为3-8个字符" data-options="required:true"/></td>
                    </tr>
                    <tr>
                        <td align="right">用户密码：</td>
                        <td><input class="easyui-textbox" type="password" id="addPayMerchantUserUserPwd" name="userPwd" missingMessage="请输入用户密码"
                                validType="length[6,16]" invalidMessage="用户密码为6-20个字符" data-options="required:true"/></td>
                    </tr>
                    <tr>
                        <td align="right">电话：</td>
                        <td><input class="easyui-textbox" type="text" id="addPayMerchantUserTel" name="tel" missingMessage="请输入电话"
                                validType="length[1,20]" invalidMessage="电话为1-20个字符" data-options="required:true"/></td>
                    </tr>
                    <tr>
                        <td align="right">邮箱：</td>
                        <td><input class="easyui-textbox" type="text" id="addPayMerchantUserEmail" name="email" missingMessage="请输入邮箱"
                                validType="length[1,20]" invalidMessage="邮箱为1-20个字符" data-options="required:true"/></td>
                    </tr>
                    <tr>
                        <td align="right">用户密码有效期：</td>
                        <td><input class="easyui-numberbox" type="text" id="addPayMerchantUserUserExpiredTime" name="userExpiredTime" missingMessage="请输入用户密码有效期"
                                data-options="required:true" validType="length[1,4]" invalidMessage="请输入正确的用户密码有效期" value="90"/></td>
                    </tr>
                    <tr>
                        <td align="right">用户备注信息：</td>
                        <td><input class="easyui-textbox" type="text" id="addPayMerchantUserDescInf" name="descInf" data-options="multiline:true" style="width:400px;height:100px;"/></td>
                    </tr>
                    <tr>
                    	<td colspan="2" align="center">
                    		<a class="easyui-linkbutton" data-options="iconCls:'icon-ok'" href="javascript:addPayMerchantUserFormSubmit()" style="width:80px">确定</a>
                    		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					        <a class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" href="javascript:void(0)" 
					            onclick="$('#addPayMerchantUserForm').form('clear')" style="width:80px">取消</a>
                    	</td>
                    </tr>
            </table>
        </form>
    </div>
</div>
<script type="text/javascript">
$('#addPayMerchantUserForm').form({
    url:'<%=path %>/addPayMerchantUser.htm',
    onSubmit: function(){
        return $(this).form('validate');
    },
    success:function(data){
        if(data=='<%=JWebConstant.OK %>'){
            topCenterMessage('<%=JWebConstant.OK %>','添加成功！');
            closeTabFreshList('payMerchantUser',payMerchantUserAddPageTitle,payMerchantUserListPageTitle,'payMerchantUserList','<%=path %>/addPayMerchantUser.htm');
        } else topCenterMessage('<%=JWebConstant.ERROR %>',data);
    }
});
function addPayMerchantUserFormSubmit(){
    $('#addPayMerchantUserForm').submit();
}
</script>
