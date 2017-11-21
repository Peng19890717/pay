<%@page import="com.PayConstant"%>
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
        <form id="addPayAdjustAccountCashForm" method="post">
            <table cellpadding="5" width="100%" style="margin-top:-10px;">
                <tr><td width="100">&nbsp;</td><td>&nbsp;</td></tr>
                    <tr>
                        <td align="right">客户编号：</td>
                        <td><input class="easyui-textbox" type="text" id="addPayAdjustAccountCashCustId" name="custId" missingMessage="请输入客户编号"
                                validType="length[1,50]" invalidMessage="" data-options="required:true"/>
                        </td>
                    </tr>
                    <tr>
                        <td align="right">账户类型：</td>
                        <td><select class="easyui-combobox" panelHeight="auto" 
                        		data-options="required:true,editable:false" name="acType" missingMessage="">
					             <option value="<%=PayConstant.CUST_TYPE_USER %>">个人账户</option>
					             <option value="<%=PayConstant.CUST_TYPE_MERCHANT %>">商户账户</option>
					             <option value="<%=PayConstant.CUST_TYPE_CHARGE %>">商户手续费</option>
							</select></td>
                    </tr>
                    <tr>
                        <td align="right">调账类型：</td>
                        <td><select class="easyui-combobox" panelHeight="auto" 
                        		data-options="required:true,editable:false" name="adjustType" missingMessage="">
					             <option value="0">增加</option>
					             <option value="1">减少</option>
							</select></td>
                    </tr>
                    <tr>
                        <td align="right">业务类型：</td>
                        <td><select class="easyui-combobox" panelHeight="auto" 
                        		data-options="required:true,editable:false" name="adjustBussType" missingMessage="">
					             <option value="0">普通调账</option>
					             <option value="1">线下充值调账</option>
					             <option value="2">代理分润调账</option>
					             <option value="3">投诉调账</option>
					             <option value="4">保证金调账</option>
							</select></td>
                    </tr>
                    <tr>
                        <td align="right">调账金额：</td>
                        <td><input class="easyui-numberbox" type="text" id="addPayAdjustAccountCashAmtTemp" missingMessage="请输入调账金额"
                                data-options="required:true" precision="2" max="99999999999" invalidMessage=""/>&nbsp;&nbsp;元</td>
                    </tr>
                    <tr>
                        <td align="right">备注：</td>
                        <td><input class="easyui-textbox" type="text" id="addPayAdjustAccountCashRemark" name="remark" missingMessage="请输入备注"
                                validType="length[0,200]"  data-options="multiline:true" invalidMessage="备注最大为 200 个字符"  style="width:200px;height:70px"/></td>
                    </tr>
                    
                    <input type="hidden" name="amt" id="addPayAdjustAccountCashAmt">
                    
            </table>
        </form>
    </div>
    <div data-options="region:'south',border:false" style="text-align:right;padding:5px;">
        <a class="easyui-linkbutton" data-options="iconCls:'icon-ok'" href="javascript:addPayAdjustAccountCashFormSubmit()" style="width:80px">确定</a>
        <a class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" href="javascript:void(0)" 
            onclick="$('#addPayAdjustAccountCashForm').form('clear')" style="width:80px">取消</a>
    </div>
</div>
<script type="text/javascript">

$('#addPayAdjustAccountCashAmtTemp').numberbox({
	onChange:function(value){
		if(value.length>0){
			document.getElementById("addPayAdjustAccountCashAmt").value=parseInt((parseFloat(value)*100).toPrecision(12));
		}
	}
});


$('#addPayAdjustAccountCashForm').form({
    url:'<%=path %>/addPayAdjustAccountCash.htm',
    onSubmit: function(){
        return $(this).form('validate');
    },
    success:function(data){
        if(data=='<%=JWebConstant.OK %>'){
            topCenterMessage('<%=JWebConstant.OK %>','添加成功！');
            closeTabFreshList('payAdjustAccountCash',payAdjustAccountCashAddPageTitle,payAdjustAccountCashListPageTitle,'payAdjustAccountCashList','<%=path %>/payAdjustAccountCash.htm');
        } else topCenterMessage('<%=JWebConstant.ERROR %>',data);
    }
});
function addPayAdjustAccountCashFormSubmit(){
    $('#addPayAdjustAccountCashForm').submit();
}
</script>
