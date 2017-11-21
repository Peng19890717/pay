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
        <form id="addPayMarginForm" method="post">
        	<input type="hidden" id="addPayMarginPaidInAmt" name="paidInAmt" value=""/>
            <table cellpadding="5" width="100%" style="margin-top:-10px">
                <tr><td width="100">&nbsp;</td><td>&nbsp;</td></tr>
                	<tr>
                        <td align="right">客户类型：</td>
                        <td>
                          <select class="easyui-combobox" panelHeight="auto" id="addPayMarginCustType" name="custType">
                        	<option value="<%=com.PayConstant.CUST_TYPE_MERCHANT %>">商户</option>
					        <option value="<%=com.PayConstant.CUST_TYPE_USER %>">个人</option>
						  </select>
                        </td>
                    </tr>
                    <tr>
                        <td align="right">客户编号：</td>
                        <td><input class="easyui-textbox" type="text" id="addPayMarginCustId" name="custId" missingMessage="请输入客户编号"
                                validType="length[1,20]" invalidMessage="客户编号为1-20个字符" data-options="required:true" style="width:200px"/></td>
                    </tr>
                    <!-- tr>
                        <td align="right">合同编号：</td>
                        <td><input class="easyui-textbox" type="text" id="addPayMarginPactNo" name="pactNo" missingMessage="请输入合同编号"
                                validType="length[1,20]" invalidMessage="合同编号为1-20个字符" data-options="required:true" style="width:200px"/></td>
                    </tr -->
                    <tr>
                        <td align="right">实收金额：</td>
                        <td><input class="easyui-numberbox" type="text" id="addPayMarginPaidInAmtTmp" name="paidInAmtTmp" missingMessage="请输入实收保证金金额（数字）"
                                data-options="required:true" precision="2" max="99999999999" style="width:200px"/>&nbsp;&nbsp;元</td>
                    </tr>
                    <tr>
                        <td align="right" valign="top">备注：</td>
                        <td>
                        	<input class="easyui-textbox" type="text" id="addPayMarginMark" name="mark"  missingMessage="请输入备注"
                          		validType="length[0,500]" invalidMessage="备注内容请控制在500个字符" data-options="multiline:true" 
                          	style="width:240px;height:70px"/>
                        </td>
                    </tr>
            </table>
        </form>
    </div>
    <div data-options="region:'south',border:false" style="text-align:center;padding:5px;">
        <a class="easyui-linkbutton" data-options="iconCls:'icon-ok'" href="javascript:addPayMarginFormSubmit()" style="width:80px">确定</a>
        <a class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" href="javascript:void(0)" 
            onclick="$('#addPayMarginForm').form('clear')" style="width:80px">清空</a>
    </div>
</div>
<script type="text/javascript">
$('#addPayMarginForm').form({
    url:'<%=path %>/addPayMargin.htm',
    onSubmit: function(){
        return $(this).form('validate');
    },
    success:function(data){
        if(data=='<%=JWebConstant.OK %>'){
            topCenterMessage('<%=JWebConstant.OK %>','添加成功！');
            closeTabFreshList('payMargin',payMarginAddPageTitle,payMarginListPageTitle,'payMarginList','<%=path %>/payMargin.htm');
        } else topCenterMessage('<%=JWebConstant.ERROR %>',data);
    }
});
$('#addPayMarginPaidInAmtTmp').numberbox({
	onChange:function(value){
		if(value.length>0)
		document.getElementById("addPayMarginPaidInAmt").value=Math.ceil(parseFloat(value)*100);
	}
});
function addPayMarginFormSubmit(){
    $('#addPayMarginForm').submit();
}
</script>
