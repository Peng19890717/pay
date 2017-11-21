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
PayMerchantUser payMerchantUser = (PayMerchantUser)request.getAttribute("payMerchantUserUpdate");
%>
<script type="text/javascript">
$(document).ready(function(){});
</script>
<div class="easyui-layout" data-options="fit:true">
    <div data-options="region:'center'">
        <%if(payMerchantUser != null){ %>
        <form id="updatePayMerchantUserForm" method="post">
            <table cellpadding="5">
                <tr><td width="20%">&nbsp;</td><td width="80%">&nbsp;</td></tr>
                    <tr>
                        <td align="right">userId：</td>
                        <td><input class="easyui-textbox" type="text" id="updatePayMerchantUserUserId" name="userId" missingMessage="请输入userId"
                                validType="length[1,20]" invalidMessage="userId为1-20个字符" data-options="required:true"
                                value="<%=payMerchantUser.userId != null ? payMerchantUser.userId : "" %>"/></td>
                    </tr>
                    <tr>
                        <td align="right">custId：</td>
                        <td><input class="easyui-textbox" type="text" id="updatePayMerchantUserCustId" name="custId" missingMessage="请输入custId"
                                validType="length[1,20]" invalidMessage="custId为1-20个字符" data-options="required:true"
                                value="<%=payMerchantUser.custId != null ? payMerchantUser.custId : "" %>"/></td>
                    </tr>
                    <tr>
                        <td align="right">userNam：</td>
                        <td><input class="easyui-textbox" type="text" id="updatePayMerchantUserUserNam" name="userNam" missingMessage="请输入userNam"
                                validType="length[1,20]" invalidMessage="userNam为1-20个字符" data-options="required:true"
                                value="<%=payMerchantUser.userNam != null ? payMerchantUser.userNam : "" %>"/></td>
                    </tr>
                    <tr>
                        <td align="right">userPwd：</td>
                        <td><input class="easyui-textbox" type="text" id="updatePayMerchantUserUserPwd" name="userPwd" missingMessage="请输入userPwd"
                                validType="length[1,20]" invalidMessage="userPwd为1-20个字符" data-options="required:true"
                                value="<%=payMerchantUser.userPwd != null ? payMerchantUser.userPwd : "" %>"/></td>
                    </tr>
                    <tr>
                        <td align="right">creaSign：</td>
                        <td><input class="easyui-textbox" type="text" id="updatePayMerchantUserCreaSign" name="creaSign" missingMessage="请输入creaSign"
                                validType="length[1,20]" invalidMessage="creaSign为1-20个字符" data-options="required:true"
                                value="<%=payMerchantUser.creaSign != null ? payMerchantUser.creaSign : "" %>"/></td>
                    </tr>
                    <tr>
                        <td align="right">random：</td>
                        <td><input class="easyui-textbox" type="text" id="updatePayMerchantUserRandom" name="random" missingMessage="请输入random"
                                validType="length[1,20]" invalidMessage="random为1-20个字符" data-options="required:true"
                                value="<%=payMerchantUser.random != null ? payMerchantUser.random : "" %>"/></td>
                    </tr>
                    <tr>
                        <td align="right">userDate：</td>
                        <td><input class="easyui-datebox" data-options="editable:false,required:true" id="updatePayMerchantUserUserDate" name="userDate"missingMessage="请输入userDate"
                                value="<%=payMerchantUser.userDate != null ? new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payMerchantUser.userDate):"" %>"/></td>
                    </tr>
                    <tr>
                        <td align="right">state：</td>
                        <td><input class="easyui-textbox" type="text" id="updatePayMerchantUserState" name="state" missingMessage="请输入state"
                                validType="length[1,20]" invalidMessage="state为1-20个字符" data-options="required:true"
                                value="<%=payMerchantUser.state != null ? payMerchantUser.state : "" %>"/></td>
                    </tr>
                    <tr>
                        <td align="right">descInf：</td>
                        <td><input class="easyui-textbox" type="text" id="updatePayMerchantUserDescInf" name="descInf" missingMessage="请输入descInf"
                                validType="length[1,20]" invalidMessage="descInf为1-20个字符" data-options="required:true"
                                value="<%=payMerchantUser.descInf != null ? payMerchantUser.descInf : "" %>"/></td>
                    </tr>
                    <tr>
                        <td align="right">tel：</td>
                        <td><input class="easyui-textbox" type="text" id="updatePayMerchantUserTel" name="tel" missingMessage="请输入tel"
                                validType="length[1,20]" invalidMessage="tel为1-20个字符" data-options="required:true"
                                value="<%=payMerchantUser.tel != null ? payMerchantUser.tel : "" %>"/></td>
                    </tr>
                    <tr>
                        <td align="right">email：</td>
                        <td><input class="easyui-textbox" type="text" id="updatePayMerchantUserEmail" name="email" missingMessage="请输入email"
                                validType="length[1,20]" invalidMessage="email为1-20个字符" data-options="required:true"
                                value="<%=payMerchantUser.email != null ? payMerchantUser.email : "" %>"/></td>
                    </tr>
                    <tr>
                        <td align="right">loginFailCount：</td>
                        <td><input class="easyui-numberbox" type="text" id="updatePayMerchantUserLoginFailCount" name="loginFailCount" missingMessage="请输入loginFailCount"
                                data-options="required:true" validType="length[1,10]" invalidMessage="请输入正确loginFailCount"
                                value="<%=payMerchantUser.loginFailCount != null ? payMerchantUser.loginFailCount : "" %>"/></td>
                    </tr>
                    <tr>
                        <td align="right">currentLoginTime：</td>
                        <td><input class="easyui-datebox" data-options="editable:false,required:true" id="updatePayMerchantUserCurrentLoginTime" name="currentLoginTime"missingMessage="请输入currentLoginTime"
                                value="<%=payMerchantUser.currentLoginTime != null ? new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payMerchantUser.currentLoginTime):"" %>"/></td>
                    </tr>
                    <tr>
                        <td align="right">preset1：</td>
                        <td><input class="easyui-textbox" type="text" id="updatePayMerchantUserPreset1" name="preset1" missingMessage="请输入preset1"
                                validType="length[1,20]" invalidMessage="preset1为1-20个字符" data-options="required:true"
                                value="<%=payMerchantUser.preset1 != null ? payMerchantUser.preset1 : "" %>"/></td>
                    </tr>
                    <tr>
                        <td align="right">lastUppwdDate：</td>
                        <td><input class="easyui-datebox" data-options="editable:false,required:true" id="updatePayMerchantUserLastUppwdDate" name="lastUppwdDate"missingMessage="请输入lastUppwdDate"
                                value="<%=payMerchantUser.lastUppwdDate != null ? new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payMerchantUser.lastUppwdDate):"" %>"/></td>
                    </tr>
                    <tr>
                        <td align="right">userExpiredTime：</td>
                        <td><input class="easyui-numberbox" type="text" id="updatePayMerchantUserUserExpiredTime" name="userExpiredTime" missingMessage="请输入userExpiredTime"
                                data-options="required:true" validType="length[1,10]" invalidMessage="请输入正确userExpiredTime"
                                value="<%=payMerchantUser.userExpiredTime != null ? payMerchantUser.userExpiredTime : "" %>"/></td>
                    </tr>
                    <tr>
                        <td align="right">mailflg：</td>
                        <td><input class="easyui-textbox" type="text" id="updatePayMerchantUserMailflg" name="mailflg" missingMessage="请输入mailflg"
                                validType="length[1,20]" invalidMessage="mailflg为1-20个字符" data-options="required:true"
                                value="<%=payMerchantUser.mailflg != null ? payMerchantUser.mailflg : "" %>"/></td>
                    </tr>
            </table>
        </form>
        <%} %>
    </div>
    <div data-options="region:'south',border:false" style="text-align:right;padding:5px;">
        <a class="easyui-linkbutton" data-options="iconCls:'icon-ok'" href="javascript:updatePayMerchantUserFormSubmit()" style="width:80px">确定</a>
        <a class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" href="javascript:void(0)" 
            onclick="$('#mainTabs').tabs('close',payMerchantUserUpdatePageTitle)" style="width:80px">取消</a>
    </div>
</div>
<script type="text/javascript">
$('#updatePayMerchantUserForm').form({
    url:'<%=path %>/updatePayMerchantUser.htm',
    onSubmit: function(){
        return $(this).form('validate');
    },
    success:function(data){
        $('#payMerchantUserList').datagrid('reload');
        if(data=='<%=JWebConstant.OK %>'){
            topCenterMessage('<%=JWebConstant.OK %>','修改成功！');
            closeTabFreshList('payMerchantUser',payMerchantUserUpdatePageTitle,payMerchantUserListPageTitle,'payMerchantUserList','<%=path %>/payMerchantUser.htm');
        } else topCenterMessage('<%=JWebConstant.ERROR %>',data);
    }
});
function updatePayMerchantUserFormSubmit(){
    $('#updatePayMerchantUserForm').submit();
}
</script>
