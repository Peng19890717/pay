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
        <form id="addPayBizTypeForm" method="post">
            <table cellpadding="5" width="100%" style="margin-top:-10px;">
                <tr><td width="100">&nbsp;</td><td>&nbsp;</td></tr>
                    <tr>
                        <td align="right">业务类型代号：</td>
                        <td><input class="easyui-textbox" type="text" id="addPayBizTypeCode" name="code" missingMessage="请输入业务类型代号"
                                validType="length[1,2]" invalidMessage="定长为2" data-options="required:true"/></td>
                    </tr>
                    <tr>
                        <td align="right">业务类型名称：</td>
                        <td><input class="easyui-textbox" type="text" id="addPayBizTypeName" name="name" missingMessage="请输入业务类型名称"
                                validType="length[1,60]" invalidMessage="业务类型名称为1-60个字符" data-options="required:true"/></td>
                    </tr>
                    <tr>
                        <td align="right">实名类型：</td>
                        <td>
                            <select id="addPayBizTypeIsRealname" class="easyui-combobox" name="isRealname" data-options="editable:false">
	                       		<option value="1" selected="selected">实名</option>
	                       		<option value="0">非实名</option>
                        	</select>
                        </td>
                    </tr>
                    <tr>
                        <td align="right">生效情况：</td>
                        <td>
                        	<select id="addPayBizTypeIsActive" class="easyui-combobox" name="isActive" data-options="editable:false">
	                       		<option value="1" selected="selected">生效</option>
	                       		<option value="0">不生效</option>
                        	</select>
                        </td>
                    </tr>
                    <tr>
                        <td align="right">备注信息：</td>
                        <td><input class="easyui-textbox" type="text" id="addPayBizTypeRemark" name="remark"
                                validType="length[1,200]" invalidMessage="备注信息为1-200个字符" data-options="multiline:true" 
                          	style="width:240px;height:70px"/>
                        </td>
                    </tr>
            </table>
        </form>
    </div>
    <div data-options="region:'south',border:false" style="text-align:right;padding:5px;">
        <a class="easyui-linkbutton" data-options="iconCls:'icon-ok'" href="javascript:addPayBizTypeFormSubmit()" style="width:80px">确定</a>
        <a class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" href="javascript:void(0)" 
            onclick="$('#addPayBizTypeForm').form('clear')" style="width:80px">取消</a>
    </div>
</div>
<script type="text/javascript">
$('#addPayBizTypeForm').form({
    url:'<%=path %>/addPayBizType.htm',
    onSubmit: function(){
        return $(this).form('validate');
    },
    success:function(data){
        if(data=='<%=JWebConstant.OK %>'){
            topCenterMessage('<%=JWebConstant.OK %>','添加成功！');
            closeTabFreshList('payBizType',payBizTypeAddPageTitle,payBizTypeListPageTitle,'payBizTypeList','<%=path %>/payBizType.htm');
        } else topCenterMessage('<%=JWebConstant.ERROR %>',data);
    }
});
function addPayBizTypeFormSubmit(){
    $('#addPayBizTypeForm').submit();
}
</script>
