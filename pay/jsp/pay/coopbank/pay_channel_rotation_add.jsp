<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ page import="util.JWebConstant"%>
<%@ page import="com.jweb.dao.JWebUser"%>
<%@ page import="com.jweb.dao.JWebAction"%>
<%@ page import="com.jweb.service.JWebUserService"%>
<%@ page import="com.pay.coopbank.service.PayCoopBankService"%>
<%@ page import="com.pay.coopbank.service.PayChannelRotationService"%>
<%@ page import="com.pay.coopbank.dao.PayCoopBank"%>
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
        <form id="addPayChannelRotationForm" method="post">
            <table cellpadding="5" width="100%" style="margin-top:-10px;">
                <tr><td width="100">&nbsp;</td><td>&nbsp;</td></tr>
                    <tr>
                        <td align="right">支付方式：</td>
                        <td><select id="addPayChannelRotationType" name="type" data-option="editable:false" class="easyui-combobox">
							 	<option value="2">扫码</option>
							</select>
                        </td>
                    </tr>
                    <tr>
                        <td align="right">支付渠道：</td>
                        <td>
                        <select class="easyui-combobox" panelHeight="auto" id="addPayChannelRotationChannelId" data-options="required:true,editable:false" name="channelId">
                        	<option value=""></option>
					        <%
			           		java.util.Iterator<String> it = PayCoopBankService.CHANNEL_MAP_ALL.keySet().iterator();
			           		while(it.hasNext()){
			           			String key = it.next();
			           			String value = ((PayCoopBank)PayCoopBankService.CHANNEL_MAP_ALL.get(key)).getBankName();
			           			%><option value="<%=key%>"><%=value%></option>
			           		<%}%>
						  </select>
                        </td>
                    </tr>
                    <tr>
                        <td align="right">每天账号限额：</td>
                        <td><input class="easyui-numberbox" type="text" id="addPayChannelRotationMaxSumAmt" name="maxSumAmt" missingMessage="请输入账号最大限额" value="0" 
                        	data-options="required:true" invalidMessage="参数值必填" max="99999999999"/>（单位：元，0为无限制）
                        </td>
                    </tr>
                    <tr>
                        <td align="right">账号信息：</td>
                        <td>（格式：“账号:密码[回车]...”）</td>
                    </tr>
                    <tr>
                    	<td>&nbsp;</td>
                        <td>
                        	<input class="easyui-textbox" type="text" id="addPayChannelRotationAccInfos" name="accInfos"
                          		data-options="multiline:true" style="width:400px;height:600px"/>
                        </td>
                    </tr>
            </table>
        </form>
    </div>
    <div data-options="region:'south',border:false" style="text-align:right;padding:5px;">
        <a class="easyui-linkbutton" data-options="iconCls:'icon-ok'" href="javascript:addPayChannelRotationFormSubmit()" style="width:80px">确定</a>
        <a class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" href="javascript:void(0)" 
            onclick="$('#addPayChannelRotationForm').form('clear')" style="width:80px">取消</a>
    </div>
</div>
<script type="text/javascript">
$('#addPayChannelRotationForm').form({
    url:'<%=path %>/addPayChannelRotation.htm',
    onSubmit: function(){
        return $(this).form('validate');
    },
    success:function(data){
        if(data=='<%=JWebConstant.OK %>'){
            topCenterMessage('<%=JWebConstant.OK %>','添加成功！');
            closeTabFreshList('payChannelRotation',payChannelRotationAddPageTitle,payChannelRotationListPageTitle,'payChannelRotationList','<%=path %>/payChannelRotation.htm');
        } else topCenterMessage('<%=JWebConstant.ERROR %>',data);
    }
});
function addPayChannelRotationFormSubmit(){
    $('#addPayChannelRotationForm').submit();
}
function getChannelMaxLimitAmt(cn){
	try{
		var maxAmt = $.ajax({
			url: '<%=path %>/getChannelMaxLimitAmt.htm',
			data:{channelId:cn},
			type: 'post',
			dataType: 'json',//发送类型
			async: false,
			cache: false
		}).responseText;//接收类型
		$('#addPayChannelRotationMaxSumAmt').numberbox('setValue',parseInt(maxAmt));
	}catch(e){
		alert(e);
		$('#addPayChannelRotationMaxSumAmt').numberbox('setValue','0');
	};
}
$("#addPayChannelRotationChannelId").combobox({
	onChange: function (n,o) {
		getChannelMaxLimitAmt(n);
	}
});
</script>
