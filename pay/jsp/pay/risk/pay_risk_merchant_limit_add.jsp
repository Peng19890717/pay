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
$(function(){
	// 商户状态类型初始化加载处理
	$("#appointTab").textbox({required:false});
	$("#limitTypeDiv").hide();
	
	// 商户状态类型选择切换
	$("#addAllForAdd,#addOnlyForAdd").change(function(){
		if($(this).val()==2){
			$("#appointTab").textbox({required:true});
			$("#limitTypeDiv").show();
			$("#limitBusCodeFlag").css("display","none");
		}else{
			$("#appointTab").textbox({required:false});
			$("#limitTypeDiv").hide();
			$("#limitBusCodeFlag").css("display","");
		}
	});
	
	// 限额生效时间初始化加载处理
	$("#PayRiskMerchantLimitLimitStartDate").datebox({required:false});
	$("#PayRiskMerchantLimitLimitEndDate").datebox({required:false});
	$("#timeScopeDiv").hide();
	
	// 限额生效时间范围选择切换
	$("input[type=radio][name=limitTimeFlag]").change(function(){
		if($(this).val()==1){
			$("#PayRiskMerchantLimitLimitStartDate").datebox({required:true});
			$("#PayRiskMerchantLimitLimitEndDate").datebox({required:true});
			$("#timeScopeDiv").show();
		}else{
			$("#PayRiskMerchantLimitLimitStartDate").datebox({required:false});
			$("#PayRiskMerchantLimitLimitEndDate").datebox({required:false});
			$("#timeScopeDiv").hide();
		}
	});
});
</script>
<div class="easyui-layout" data-options="fit:true">
    <div data-options="region:'center'">
        <form id="addPayRiskMerchantLimitForm" method="post">
            <table cellpadding="5" width="100%" style="margin-top:-10px;">
                <tr><td  width="100">&nbsp;</td><td width="200">&nbsp;</td><td width="120">&nbsp;</td><td>&nbsp;</td></tr>
                    <tr>
                        <td align="right">商户状态类型：</td>
                        <td colspan="3">
                        	<input type="radio" name="limitType"  value="1" id="addAllForAdd" data-options="required:true" checked="checked"/>所有商户
                        	<input type="radio" name="limitType"  value="2" id="addOnlyForAdd" data-options="required:true" />指定商户&nbsp;
                        	<div style="display: inLine" id="limitTypeDiv"><input type='text' class='easyui-textbox' name='limitCompCode' class='easyui-textbox' id='appointTab' missingMessage="请输入指定商户号"
                                validType="length[0,20]" invalidMessage="请填写合法的商户号" data-options="required:true"></div>
                        </td>
                    </tr>
                    <tr id="limitBusCodeFlag">
                        <td align="right">请选择风险级别：</td>
                        <td colspan="3">
                        	<select class="easyui-combobox" name="limitRiskLevel" missingMessage="请选择风险级别" data-options="editable:false">
                        		<option value="0">低</option>
                        		<option value="1" selected="selected">中</option>
                        		<option value="2">高</option>
                        	</select>
                        </td>
                    </tr>
                    <tr>
                        <td align="right">请选择限额类型：</td>
                        <td colspan="3">
                        	<select class="easyui-combobox" name="limitCompType" missingMessage="请选择限额类型" data-options="editable:false">
                        		<option value="1" selected="selected">消费</option>
                        		<option value="2">充值</option>
                        		<option value="3">结算</option>
                        		<option value="4">退款</option>
                        		<option value="5">提现</option>
                        		<option value="6">转账</option>
                        		<option value="15">代收</option>
                        		<option value="16">代付</option>
                        	</select>
                        </td>
                    </tr>
                    <tr>
                    	<td align="right">限额生效时间：</td>
                        <td colspan="3">
	                       	<input type="radio" name="limitTimeFlag"  value="0" data-options="required:true" checked="checked"/>永久&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	                       	<input type="radio" name="limitTimeFlag"  value="1" data-options="required:true"/>按交易时间&nbsp;
	                       	<div style="display: inLine" id="timeScopeDiv">
	                        	<input class="easyui-datebox" style="width:100px" data-options="editable:false,required:true" id="PayRiskMerchantLimitLimitStartDate" name="limitStartDate" missingMessage="请输入开始时间"/>
	                        	 ~ 
	                        	<input class="easyui-datebox" style="width:100px" data-options="editable:false,required:true" id="PayRiskMerchantLimitLimitEndDate" name="limitEndDate" missingMessage="请输入结束时间"/>
                           	</div>
                       	</td>
                    </tr>
                    <tr>
                        <td align="right">单笔最小金额：</td>
                        <td>
                        	<input class="easyui-numberbox" type="text" id="addPayRiskMerchantLimitLimitMinAmtTemp" missingMessage="请输入单笔最小金额"
                                data-options="required:true" precision="2" max="99999999999" />元
                        </td>
                        <td align="right">
                       		 单笔最大金额：
                        </td>
                        <td>
                        	<input class="easyui-numberbox" type="text" id="addPayRiskMerchantLimitLimitMaxAmtTemp" missingMessage="请输入单笔最大金额"
                                data-options="required:true" precision="2" max="99999999999" />元
                        </td>
                    </tr>
                    <tr>
                        <td align="right">日交易次数上限：</td>
                        <td>
                        	<input class="easyui-numberbox" type="text" max="1000000000" id="addPayRiskMerchantLimitLimitDayTimes" name="limitDayTimes" missingMessage="请输入日交易次数上限" data-options="required:true" validType="length[1,10]" invalidMessage="日交易次数上限必填"/>次
                        </td>
                        <td align="right">
                       		日成功交易次数上限：
                       	</td>
                        <td>
                        	<input class="easyui-numberbox" type="text" max="1000000000" id="addPayRiskMerchantLimitLimitDaySuccessTimes" name="limitDaySuccessTimes" missingMessage="请输入日成功交易次数上限" data-options="required:true" validType="length[1,10]" invalidMessage="日交易次数上限必填"/>次
                        </td>
                    </tr>
                    <tr>
                        <td align="right">日交易额上限：</td>
                        <td>
                        	<input class="easyui-numberbox" type="text" id="addPayRiskMerchantLimitLimitDayAmtTemp" missingMessage="请输入日交易限额上限" data-options="required:true" precision="2" max="99999999999" />元
                        </td>
                        <td align="right">
                       		日成功交易额上限：
                       	</td>
                        <td>
                        	<input class="easyui-numberbox" type="text" id="addPayRiskMerchantLimitLimitDaySuccessAmtTemp" missingMessage="请输入日成功交易额上限" data-options="required:true" precision="2" max="99999999999" />元
                        </td>
                    </tr>
                    <tr>
                        <td align="right">月交易次数上限：</td>
                        <td>
                        	<input class="easyui-numberbox" type="text" max="1000000000" id="addPayRiskMerchantLimitLimitMonthTimes" name="limitMonthTimes" missingMessage="请输入月交易次数上限" data-options="required:true" validType="length[1,10]" invalidMessage="月交易次数上限必填"/>次
                        </td>
                        <td align="right">
                        	月成功交易次数上限：
                        </td>
                        <td>
                       		<input class="easyui-numberbox" type="text" max="1000000000" id="addPayRiskMerchantLimitLimitMonthSuccessTimes" name="limitMonthSuccessTimes" missingMessage="请输入月成功交易次数上限" data-options="required:true" validType="length[1,10]" invalidMessage="月交易次数上限必填"/>次
                        </td>
                    </tr>
                    <tr>
                        <td align="right">月交易额上限：</td>
                        <td>
                        	<input class="easyui-numberbox" type="text" id="addPayRiskMerchantLimitLimitMonthAmtTemp" missingMessage="请输入月交易限额上限" data-options="required:true" precision="2" max="99999999999"/>元
                        </td>
                        <td align="right">
                        	月成功交易额上限：
                        </td>
                        <td>
                       		<input class="easyui-numberbox" type="text" id="addPayRiskMerchantLimitLimitMonthSuccessAmtTemp" missingMessage="请输入月成功交易额上限" data-options="required:true" precision="2" max="99999999999"/>元
                        </td>
                    </tr>
                    <tr>
                        <td align="right">启用标志：</td>
                        <td colspan="3">
                        	<input type="radio" name="isUse" value="0"/> 不可用
                        	<input type="radio" name="isUse" value="1" checked="checked"/> 可用
                        </td>
                    </tr>
                    <tr>
                        <td align="right" valign="top">备注：</td>
                        <td colspan="3">
                        	<textarea class="textbox-text" autocomplete="off" rows="10" cols="80" id="addPayRiskMerchantLimitLimitAddinfo" name="limitAddinfo"></textarea>
                        </td>
                    </tr>
            </table>
           	<input type="hidden" name="limitMinAmt" id="addPayRiskMerchantLimitLimitMinAmt">
			<input type="hidden" name="limitMaxAmt" id="addPayRiskMerchantLimitLimitMaxAmt">
			<input type="hidden" name="limitDayAmt" id="addPayRiskMerchantLimitLimitDayAmt">
			<input type="hidden" name="limitDaySuccessAmt" id="addPayRiskMerchantLimitLimitDaySuccessAmt">
			<input type="hidden" name="limitMonthAmt" id="addPayRiskMerchantLimitLimitMonthAmt">
			<input type="hidden" name="limitMonthSuccessAmt" id="addPayRiskMerchantLimitLimitMonthSuccessAmt">
        </form>
    </div>
    <div data-options="region:'south',border:false" style="text-align:right;padding:5px;">
        <a class="easyui-linkbutton" data-options="iconCls:'icon-ok'" href="javascript:addPayRiskMerchantLimitFormSubmit()" style="width:80px">确定</a>
        <a class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" href="javascript:void(0)" 
            onclick="$('#addPayRiskMerchantLimitForm').form('clear')" style="width:80px">取消</a>
    </div>
</div>
<script type="text/javascript">
$('#addPayRiskMerchantLimitForm').form({
    url:'<%=path %>/addPayRiskMerchantLimit.htm',
    onSubmit: function(){
        return $(this).form('validate');
    },
    success:function(data){
        if(data=='<%=JWebConstant.OK %>'){
            topCenterMessage('<%=JWebConstant.OK %>','添加成功！');
            closeTabFreshList('payRiskMerchantLimit',payRiskMerchantLimitAddPageTitle,payRiskMerchantLimitListPageTitle,'payRiskMerchantLimitList','<%=path %>/payRiskMerchantLimit.htm');
        } else topCenterMessage('<%=JWebConstant.ERROR %>',data);
    }
});

$('#addPayRiskMerchantLimitLimitMinAmtTemp').numberbox({
	onChange:function(value){
		if(value.length>0)
		document.getElementById("addPayRiskMerchantLimitLimitMinAmt").value=Math.ceil(parseFloat(value)*100);
	}
});
$('#addPayRiskMerchantLimitLimitMaxAmtTemp').numberbox({
	onChange:function(value){
		if(value.length>0)
		document.getElementById("addPayRiskMerchantLimitLimitMaxAmt").value=Math.ceil(parseFloat(value)*100);
	}
});
$('#addPayRiskMerchantLimitLimitDayAmtTemp').numberbox({
	onChange:function(value){
		if(value.length>0)
		document.getElementById("addPayRiskMerchantLimitLimitDayAmt").value=Math.ceil(parseFloat(value)*100);
	}
});
$('#addPayRiskMerchantLimitLimitDaySuccessAmtTemp').numberbox({
	onChange:function(value){
		if(value.length>0)
		document.getElementById("addPayRiskMerchantLimitLimitDaySuccessAmt").value=Math.ceil(parseFloat(value)*100);
	}
});
$('#addPayRiskMerchantLimitLimitMonthAmtTemp').numberbox({
	onChange:function(value){
		if(value.length>0)
		document.getElementById("addPayRiskMerchantLimitLimitMonthAmt").value=Math.ceil(parseFloat(value)*100);
	}
});
$('#addPayRiskMerchantLimitLimitMonthSuccessAmtTemp').numberbox({
	onChange:function(value){
		if(value.length>0)
		document.getElementById("addPayRiskMerchantLimitLimitMonthSuccessAmt").value=Math.ceil(parseFloat(value)*100);
	}
});

function addPayRiskMerchantLimitFormSubmit(){
    $('#addPayRiskMerchantLimitForm').submit();
}
</script>
