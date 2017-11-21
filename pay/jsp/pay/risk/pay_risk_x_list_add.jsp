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
$(document).ready(function(){
	$("#addPayRiskXListClientType").combobox({
		onSelect:function(param){
			if(param.text=="个人" || param.text=="商户"){
				$("#clientName").html("客户编码：");
			}else if(param.text=="手机号"){
				$("#clientName").html("手机号：");
			}else if(param.text=="银行卡号"){
				$("#clientName").html("银行卡号：");
			}
		}
	})
});
</script>
<div class="easyui-layout" data-options="fit:true">
    <div data-options="region:'center'">
        <form id="addPayRiskXListForm" method="post">
            <table cellpadding="5" width="100%" style="margin-top:-10px;">
                <tr><td width="100">&nbsp;</td><td>&nbsp;</td></tr>
                    <tr>
                        <td align="right">客户类型：</td>
                        <td>
                        	<select class="easyui-combobox" panelHeight="auto" id="addPayRiskXListClientType" 
                        		data-options="required:true,editable:false" name="clientType" missingMessage="请选择客户类型">
					             <option value=""></option>
					             <option value="<%=com.PayConstant.CUST_TYPE_USER %>">个人</option>
					             <option value="<%=com.PayConstant.CUST_TYPE_MERCHANT %>">商户</option>
					             <option value="<%=com.PayConstant.CUST_TYPE_MOBLIE %>">手机号</option>
					             <option value="<%=com.PayConstant.CUST_TYPE_CARD %>">银行卡号</option>
							</select>
                        </td>
                    </tr>
                    <tr>
                        <td align="right" id="clientName">客户编码：</td>
                        <td><input class="easyui-textbox" type="text" id="addPayRiskXListClientCode" name="clientCode" missingMessage="请输入客户编码"
                                validType="length[1,20]" invalidMessage="客户编码为1-20个字符" data-options="required:true"/></td>
                    </tr>
                    <tr>
                        <td align="right">X名单状态：</td>
                        <td>
                            <select name="xType" id="addPayRiskXListXType" class="easyui-combobox"  
                             panelHeight="auto" data-options="required:true,editable:false" missingMessage="请选择名单状态">
                        		<option value=""></option>
                        		<option value="1" selected="selected">白名单</option>
                        		<option value="3">黑名单</option>  
                        		<option value="4">红名单</option>
                    		</select> 
                        </td>
                    </tr>
                    <tr>
                        <td align="right">备注：</td>
                        <td><input class="easyui-textbox" type="text" id="addPayRiskXListCasBuf" name="casBuf" missingMessage="请输入备注"
                                validType="length[1,200]" invalidMessage="备注控制在1-200个字符" data-options="multiline:true,required:true"
                         	    style="width:240px;height:70px"/></td>
                    </tr>
            </table>
        </form>
    </div>
    <div data-options="region:'south',border:false" style="text-align:right;padding:5px;">
        <a class="easyui-linkbutton" data-options="iconCls:'icon-ok'" href="javascript:addPayRiskXListFormSubmit()" style="width:80px">确定</a>
        <a class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" href="javascript:void(0)" 
            onclick="$('#addPayRiskXListForm').form('clear')" style="width:80px">取消</a>
    </div>
</div>
<script type="text/javascript">

$('#addPayRiskXListForm').form({
    url:'<%=path %>/addPayRiskXList.htm',
    onSubmit: function(){
        return $(this).form('validate');
    },
    success:function(data){
        if(data=='<%=JWebConstant.OK %>'){
            topCenterMessage('<%=JWebConstant.OK %>','添加成功！');
            closeTabFreshList('payRiskXList',payRiskXListAddPageTitle,payRiskXListListPageTitle,'payRiskXListList','<%=path %>/payRiskXList.htm');
        } else topCenterMessage('<%=JWebConstant.ERROR %>',data);
    }
});
function addPayRiskXListFormSubmit(){
    $('#addPayRiskXListForm').submit();
}

</script>
