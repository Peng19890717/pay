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
        <form id="addPayMerchantNewsForm" method="post">
            <table cellpadding="5" width="100%" style="margin-top:-10px;">
                <tr><td width="100">&nbsp;</td><td>&nbsp;</td></tr>
                    <tr>
                        <td align="right">类型：</td>
                        <td>
                            <select id="addPayMerchantNewsType" name="type" data-option="editable:false" class="easyui-combobox">
						   		<option value="1">公告</option>
						   		<option value="0">新闻</option>
						   		<option value="2">通知</option>
						    </select>
                        </td>
                    </tr>
                    <tr>
                        <td align="right">标题：</td>
                        <td><input class="easyui-textbox" style="width:250px;" type="text" id="addPayMerchantNewsTitle" name="title" missingMessage="请输入标题"
                                validType="length[1,15]" invalidMessage="标题为1-15个字符" data-options="required:true"/></td>
                    </tr>
                    <tr>
                        <td align="right">内容：</td>
                        <td><input class="easyui-textbox" data-options="multiline:true,required:true" style="width:300px;height:100px" id="addPayMerchantNewsContent" name="content" missingMessage="请输入内容"
                                validType="length[1,200]" invalidMessage="content为1-200个字符" /></td>
                    </tr>
                    <tr>
                        <td align="right">状态：</td>
                        <td>
                             <select id="addPayMerchantNewsType" name="flag2" data-option="editable:false" class="easyui-combobox">
						   		<option value="0">有效</option>
						   		<option value="1">作废</option>
						    </select>
                        </td>
                    </tr>
            </table>
        </form>
    </div>
    <div data-options="region:'south',border:false" style="text-align:right;padding:5px;">
        <a class="easyui-linkbutton" data-options="iconCls:'icon-ok'" href="javascript:addPayMerchantNewsFormSubmit()" style="width:80px">确定</a>
        <a class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" href="javascript:void(0)" 
            onclick="$('#addPayMerchantNewsForm').form('clear')" style="width:80px">取消</a>
    </div>
</div>
<script type="text/javascript">
$('#addPayMerchantNewsForm').form({
    url:'<%=path %>/addPayMerchantNews.htm',
    onSubmit: function(){
        return $(this).form('validate');
    },
    success:function(data){
        if(data=='<%=JWebConstant.OK %>'){
            topCenterMessage('<%=JWebConstant.OK %>','添加成功！');
            closeTabFreshList('payMerchantNews',payMerchantNewsAddPageTitle,payMerchantNewsListPageTitle,'payMerchantNewsList','<%=path %>/payMerchantNews.htm');
        } else topCenterMessage('<%=JWebConstant.ERROR %>',data);
    }
});
function addPayMerchantNewsFormSubmit(){
    $('#addPayMerchantNewsForm').submit();
}
</script>
