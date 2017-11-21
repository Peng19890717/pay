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
        <form id="addPayOpenedMerPayForm" method="post">
            <table cellpadding="5" width="100%" style="margin-top:-10px;">
                <tr><td width="100">&nbsp;</td><td>&nbsp;</td></tr>
                    <tr>
                        <td align="right">商户号：</td>
                        <td><input class="easyui-textbox" type="text" id="addPayOpenedMerPayMerno" name="merno" missingMessage="请输入商户号"
                                validType="length[1,20]" invalidMessage="商户号为1-20个字符" data-options="required:true"/></td>
                    </tr>
                    <tr>
                        <td align="right">交易类型：</td>
                        <td><select class="easyui-combobox" id="addPayOpenedMerPayType" name="type" style="width:180px" data-options="editable:false">
					           <option value="16">代付</option>
					           <option value="1">消费</option>
					           <option value="2">充值</option>
					           <option value="3">结算</option>
					           <option value="4">退款</option>
					           <option value="6">转账</option>
					           <option value="6">提现</option>
					           <option value="7">消费B2C借记卡</option>
					           <option value="8">消费B2C信用卡</option>
					           <option value="9">消费B2B</option>
					           <option value="10">微信扫码</option>
					           <option value="11">消费快捷</option>
					           <option value="12">其他消费</option>
					           <option value="13">代理</option>
					           <option value="14">代理商户</option>
					           <option value="15">代收</option>
					           <option value="17">支付宝扫码</option>
					           <option value="18">微信WAP</option>
		            
		     			    </select>
                        </td>
                    </tr>
            </table>
        </form>
    </div>
    <div data-options="region:'south',border:false" style="text-align:right;padding:5px;">
        <a class="easyui-linkbutton" data-options="iconCls:'icon-ok'" href="javascript:addPayOpenedMerPayFormSubmit()" style="width:80px">确定</a>
    </div>
</div>
<script type="text/javascript">
$('#addPayOpenedMerPayForm').form({
    url:'<%=path %>/addPayOpenedMerPay.htm',
    onSubmit: function(){
        return $(this).form('validate');
    },
    success:function(data){
        if(data=='<%=JWebConstant.OK %>'){
            topCenterMessage('<%=JWebConstant.OK %>','添加成功！');
            closeTabFreshList('payOpenedMerPay',payOpenedMerPayAddPageTitle,payOpenedMerPayListPageTitle,'payOpenedMerPayList','<%=path %>/payOpenedMerPay.htm');
        } else topCenterMessage('<%=JWebConstant.ERROR %>',data);
    }
});
function addPayOpenedMerPayFormSubmit(){
    $('#addPayOpenedMerPayForm').submit();
}
</script>
