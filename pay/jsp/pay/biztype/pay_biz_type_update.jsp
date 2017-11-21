<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ page import="util.JWebConstant"%>
<%@ page import="com.jweb.dao.JWebUser"%>
<%@ page import="com.jweb.dao.JWebAction"%>
<%@ page import="com.jweb.service.JWebUserService"%>
<%@ page import="com.pay.biztype.dao.PayBizType"%>
<%@ page import="com.PayConstant"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
String path = request.getContextPath();
//登录和权限判断
JWebUser user = (JWebUser)session.getAttribute("user");
if(!JWebUserService.checkUser(user,request)){
%><script>window.location.href='<%=path %>/jsp/jweb/login.jsp'</script><%
    return;
}
PayBizType payBizType = (PayBizType)request.getAttribute("payBizTypeUpdate");
%>
<script type="text/javascript">
$(document).ready(function(){});
</script>
<div class="easyui-layout" data-options="fit:true">
    <div data-options="region:'center'">
        <%if(payBizType != null){ %>
        <form id="updatePayBizTypeForm" method="post">
        	<input type="hidden" name="seqNo" value="${payBizTypeUpdate.seqNo }">
            <table cellpadding="5" width="100%" style="margin-top:-10px;">
                <tr><td width="100">&nbsp;</td><td>&nbsp;</td></tr>
                    <tr>
                        <td align="right">业务类型代号：</td>
                        <td>${payBizTypeUpdate.code }</td>
                    </tr>
                    <tr>
                        <td align="right">业务类型名称：</td>
                        <td>
                            <input class="easyui-textbox" type="text" id="updatePayBizTypeName" name="name" missingMessage="请输入业务类型名称"
                                validType="length[1,60]" invalidMessage="业务类型名称为1-60个字符" data-options="required:true"
                                value="${payBizTypeUpdate.name }"/>    
                        </td>
                    </tr>
                    <tr>
                        <td align="right">实名类型：</td>
                        <td>
                        	<select id="updatePayBizTypeIsRealname" class="easyui-combobox" name="isRealname" data-options="editable:false">
	                       		<option value="1" <c:if test="${payBizTypeUpdate.isRealname eq '1' }">selected</c:if>>实名</option>
	                       		<option value="0" <c:if test="${payBizTypeUpdate.isRealname eq '0' }">selected</c:if>>非实名</option>
                        	</select>      
                        </td>
                    </tr>
                    <tr>
                        <td align="right">生效情况：</td>
                        <td>
                        	<select id="updatePayBizTypeIsActive" class="easyui-combobox" name="isActive" data-options="editable:false">
	                       		<option value="1" <c:if test="${payBizTypeUpdate.isActive eq '1' }">selected</c:if>>生效</option>
	                       		<option value="0" <c:if test="${payBizTypeUpdate.isActive eq '0' }">selected</c:if>>不生效</option>
                        	</select>      
                        </td>
                    </tr>
                    <tr>
                        <td align="right">备注信息：</td>
                        <td><input class="easyui-textbox" type="text" id="updatePayBizTypeRemark" name="remark"
                                validType="length[1,200]" invalidMessage="备注信息为1-200个字符" data-options="multiline:true"
                                value="${payBizTypeUpdate.remark }" style="width:240px;height:70px"/></td>
                    </tr>
            </table>
        </form>
        <%} %>
    </div>
    <div data-options="region:'south',border:false" style="text-align:right;padding:5px;">
        <a class="easyui-linkbutton" data-options="iconCls:'icon-ok'" href="javascript:updatePayBizTypeFormSubmit()" style="width:80px">确定</a>
        <a class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" href="javascript:void(0)" 
            onclick="$('#mainTabs').tabs('close',payBizTypeUpdatePageTitle)" style="width:80px">取消</a>
    </div>
</div>
<script type="text/javascript">
$('#updatePayBizTypeForm').form({
    url:'<%=path %>/updatePayBizType.htm',
    onSubmit: function(){
        return $(this).form('validate');
    },
    success:function(data){
        $('#payBizTypeList').datagrid('reload');
        if(data=='<%=JWebConstant.OK %>'){
            topCenterMessage('<%=JWebConstant.OK %>','修改成功！');
            closeTabFreshList('payBizType',payBizTypeUpdatePageTitle,payBizTypeListPageTitle,'payBizTypeList','<%=path %>/payBizType.htm');
        } else topCenterMessage('<%=JWebConstant.ERROR %>',data);
    }
});
function updatePayBizTypeFormSubmit(){
    $('#updatePayBizTypeForm').submit();
}
</script>
