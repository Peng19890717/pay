<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ page import="util.JWebConstant"%>
<%@ page import="com.jweb.dao.JWebUser"%>
<%@ page import="com.jweb.dao.JWebAction"%>
<%@ page import="com.jweb.service.JWebUserService"%>
<%@ page import="com.pay.risk.dao.PayRiskMerchantLimit"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
String path = request.getContextPath();
//登录和权限判断
JWebUser user = (JWebUser)session.getAttribute("user");
if(!JWebUserService.checkUser(user,request)){
%><script>window.location.href='<%=path %>/jsp/jweb/login.jsp'</script><%
    return;
}
PayRiskMerchantLimit payRiskMerchantLimit = (PayRiskMerchantLimit)request.getAttribute("payRiskMerchantLimitUpdate");
%>
<script type="text/javascript">
$(document).ready(function(){});
$(function(){
	// 商户状态类型初始化加载处理
	var limitTypeVal = "${payRiskMerchantLimitUpdate.limitType}";
	var limitTimeVal = "${ payRiskMerchantLimitUpdate.limitTimeFlag}";
	if(limitTypeVal == 2){
		$("#appointForUpdate").textbox({required:true});
		$("#divForUpdate").show();
		$("#limitBusCodeFlagForUpdate").css("display","none");
	}
	if(limitTypeVal ==1){
		$("#appointForUpdate").textbox({required:false});
		$("#divForUpdate").hide();
		$("#limitBusCodeFlagForUpdate").css("display","");
	}
	
	// 商户状态类型选择切换
	$("#addAllForUpdate,#addOnlyForUpdate").change(function(){
		if($(this).val()==2){
			$("#appointForUpdate").textbox({required:true});
			$("#divForUpdate").show();
			$("#limitBusCodeFlagForUpdate").css("display","none");
		}else{
			$("#appointForUpdate").textbox({required:false});
			$("#divForUpdate").hide();
			$("#limitBusCodeFlagForUpdate").css("display","");
		}
	});
	
	// 限额生效时间初始化加载处理
	if(limitTimeVal == 0) {
		$("#startDateForUpdate").datebox({required:false});
		$("#endDateForUpdate").datebox({required:false});
		$("#timeScopeDivForUpdate").hide();
	}
	if(limitTimeVal == 1){
		$("#startDateForUpdate").datebox({required:true});
		$("#endDateForUpdate").datebox({required:true});
		$("#timeScopeDivForUpdate").show();
	}
	
	// 限额生效时间范围选择切换
	$("#timeEver,#timeScope").change(function(){
		if($(this).val()==1){
			$("#startDateForUpdate").datebox({required:true});
			$("#endDateForUpdate").datebox({required:true});
			$("#timeScopeDivForUpdate").show();
		}else{
			$("#startDateForUpdate").datebox({required:false});
			$("#endDateForUpdate").datebox({required:false});
			$("#timeScopeDivForUpdate").hide();
		}
	});
});
</script>
<div class="easyui-layout" data-options="fit:true">
    <div data-options="region:'center'">
        <form id="updatePayRiskMerchantLimitForm" method="post">
        	<input type="hidden" value="${ payRiskMerchantLimitUpdate.id }" name="id">
        	<table cellpadding="5" width="100%" style="margin-top:-10px;">
                <tr><td  width="100">&nbsp;</td><td width="200">&nbsp;</td><td width="120">&nbsp;</td><td>&nbsp;</td></tr>
                    <tr>
                        <td align="right">商户状态类型：</td>
                        <td	colspan="3">
                        	<input type="radio" name="limitType"  value="1" id="addAllForUpdate" data-options="required:true" <c:if test="${payRiskMerchantLimitUpdate.limitType eq '1'}">checked="checked"</c:if> />所有商户
                        	<input type="radio" name="limitType"  value="2" id="addOnlyForUpdate" data-options="required:true" <c:if test="${payRiskMerchantLimitUpdate.limitType eq '2'}">checked="checked"</c:if>/>指定商户&nbsp;
                        	<div style="display: inLine" id="divForUpdate"><input type='text' id='appointForUpdate' class='easyui-textbox' name='limitCompCode' class='easyui-textbox' value="<c:if test="${ payRiskMerchantLimitUpdate.limitCompCode eq '-1' }"></c:if><c:if test="${ payRiskMerchantLimitUpdate.limitCompCode != '-1' }">${ payRiskMerchantLimitUpdate.limitCompCode }</c:if>"  missingMessage="请输入指定商户号"
                                validType="length[0,20]" invalidMessage="请填写合法的商户号" data-options="required:true"></div>
                        </td>
                    </tr>
                    <tr id="limitBusCodeFlagForUpdate">
                        <td align="right">请选择风险级别：</td>
                        <td	colspan="3">
                        	<select class="easyui-combobox" name="limitRiskLevel" missingMessage="请选择风险级别" data-options="editable:false">
                        		<option value="0" <c:if test="${ payRiskMerchantLimitUpdate.limitRiskLevel eq '0' }">selected="selected"</c:if>>低</option>
                        		<option value="1" <c:if test="${ payRiskMerchantLimitUpdate.limitRiskLevel eq '1' }">selected="selected"</c:if>>中</option>
                        		<option value="2" <c:if test="${ payRiskMerchantLimitUpdate.limitRiskLevel eq '2' }">selected="selected"</c:if>>高</option>
                        	</select>
                        </td>
                    </tr>
                    <tr>
                        <td align="right">请选择限额类型：</td>
                        <td	colspan="3">
                        	<select class="easyui-combobox" name="limitCompType" missingMessage="请选择限额类型" data-options="editable:false">
                        		<option value="1" <c:if test="${ payRiskMerchantLimitUpdate.limitCompType eq '1' }">selected="selected"</c:if>>消费</option>
                        		<option value="2" <c:if test="${ payRiskMerchantLimitUpdate.limitCompType eq '2' }">selected="selected"</c:if>>充值</option>
                        		<option value="3" <c:if test="${ payRiskMerchantLimitUpdate.limitCompType eq '3' }">selected="selected"</c:if>>结算</option>
                        		<option value="4" <c:if test="${ payRiskMerchantLimitUpdate.limitCompType eq '4' }">selected="selected"</c:if>>退款</option>
                        		<option value="5" <c:if test="${ payRiskMerchantLimitUpdate.limitCompType eq '5' }">selected="selected"</c:if>>提现</option>
                        		<option value="6" <c:if test="${ payRiskMerchantLimitUpdate.limitCompType eq '6' }">selected="selected"</c:if>>转账</option>
                        		<option value="15" <c:if test="${ payRiskMerchantLimitUpdate.limitCompType eq '15' }">selected="selected"</c:if>>代收</option>
                        		<option value="16" <c:if test="${ payRiskMerchantLimitUpdate.limitCompType eq '16' }">selected="selected"</c:if>>代付</option>
                        	</select>
                        </td>
                    </tr>
                    <tr>
                    	<td align="right">限额生效时间：</td>
                        <td	colspan="3">
	                       	<input type="radio" name="limitTimeFlag"  value="0" data-options="required:true" id="timeEver"  <c:if test="${ payRiskMerchantLimitUpdate.limitTimeFlag eq '0' }">checked="checked"</c:if>/>永久
	                       	<input type="radio" name="limitTimeFlag"  value="1" data-options="required:true" id="timeScope" <c:if test="${ payRiskMerchantLimitUpdate.limitTimeFlag eq '1' }">checked="checked"</c:if>/>
	                       	<div style="display: inLine" id="timeScopeDivForUpdate">
	                        	<input class="easyui-datebox" style="width:100px" data-options="editable:false,required:true" id="startDateForUpdate" name="limitStartDate" missingMessage="请输入开始时间" value="<c:if test="${ payRiskMerchantLimitUpdate.limitTimeFlag eq '0' }"></c:if><c:if test="${ payRiskMerchantLimitUpdate.limitTimeFlag eq '1' }">${ payRiskMerchantLimitUpdate.limitStartDate }</c:if>"/>
	                        	 ~ 
	                        	<input class="easyui-datebox" style="width:100px" data-options="editable:false,required:true" id="endDateForUpdate" name="limitEndDate" missingMessage="请输入结束时间" value="<c:if test="${ payRiskMerchantLimitUpdate.limitTimeFlag eq '0' }"></c:if><c:if test="${ payRiskMerchantLimitUpdate.limitTimeFlag eq '1' }">${ payRiskMerchantLimitUpdate.limitEndDate }</c:if>"/>
                           	</div>
                       	</td>
                    </tr>
                    
                    <tr>
						<td align="right">单笔最小金额：</td>
						<td>
							<input class="easyui-numberbox" type="text" id="updatePayRiskMerchantLimitLimitMinAmtTemp" missingMessage="请输入单笔最小金额"
								data-options="required:true" precision="2" max="99999999999" value="${ payRiskMerchantLimitUpdate.limitMinAmt/100 }"/>元
						</td>
						<td align="right">
							 单笔最大金额：
						</td>
						<td>
							<input class="easyui-numberbox" type="text" id="updatePayRiskMerchantLimitLimitMaxAmtTemp" missingMessage="请输入单笔最大金额"
								data-options="required:true" precision="2" max="99999999999" value="${ payRiskMerchantLimitUpdate.limitMaxAmt/100 }"/>元
						</td>
					</tr>
					<tr>
						<td align="right">日交易次数上限：</td>
						<td>
							<input class="easyui-numberbox" type="text" id="updatePayRiskMerchantLimitLimitDayTimes" name="limitDayTimes" missingMessage="请输入日交易次数上限" 
								data-options="required:true" validType="length[1,10]" max="1000000000" invalidMessage="日交易次数上限必填" value="${ payRiskMerchantLimitUpdate.limitDayTimes }"/>次
						</td>
						<td align="right">日成功交易次数上限：</td>
						<td>
							<input class="easyui-numberbox" type="text" id="updatePayRiskMerchantLimitLimitDaySuccessTimes" name="limitDaySuccessTimes" missingMessage="请输入日成功交易次数上限" 
								data-options="required:true" validType="length[1,10]" max="1000000000" invalidMessage="日交易成功次数上限必填" value="${ payRiskMerchantLimitUpdate.limitDaySuccessTimes }"/>次
						</td>
					</tr>
					<tr>
						<td align="right">日交易额上限：</td>
						<td>
							<input class="easyui-numberbox" type="text" id="updatePayRiskMerchantLimitLimitDayAmtTemp" missingMessage="请输入日交易限额上限" 
								data-options="required:true" precision="2" max="99999999999" value="${ payRiskMerchantLimitUpdate.limitDayAmt/100 }"/>元
						</td>
						<td align="right">
							日成功交易额上限：
						</td>
						<td>
							<input class="easyui-numberbox" type="text" id="updatePayRiskMerchantLimitLimitDaySuccessAmtTemp" missingMessage="请输入日成功交易额上限" 
								data-options="required:true" precision="2" max="99999999999" value="${ payRiskMerchantLimitUpdate.limitDaySuccessAmt/100 }"/>元
						</td>
					</tr>
					<tr>
						<td align="right">月交易次数上限：</td>
						<td>
							<input class="easyui-numberbox" type="text" id="updatePayRiskMerchantLimitLimitMonthTimes" name="limitMonthTimes" missingMessage="请输入月交易次数上限" 
								data-options="required:true" validType="length[1,10]" max="1000000000" invalidMessage="月交易次数上限必填" value="${ payRiskMerchantLimitUpdate.limitMonthTimes }"/>次
						</td>
						<td align="right">
							月成功交易次数上限：
						</td>
						<td>
							<input class="easyui-numberbox" type="text" id="updatePayRiskMerchantLimitLimitMonthSuccessTimes" name="limitMonthSuccessTimes" missingMessage="请输入月成功交易次数上限" 
								data-options="required:true" validType="length[1,10]" max="1000000000" invalidMessage="月交易次数上限必填"	 value="${ payRiskMerchantLimitUpdate.limitMonthSuccessTimes }"/>次
						</td>
					</tr>
					<tr>
						<td align="right">月交易额上限：</td>
						<td>
							<input class="easyui-numberbox" type="text" id="updatePayRiskMerchantLimitLimitMonthAmtTemp" missingMessage="请输入月交易限额上限" 
								data-options="required:true" precision="2" max="99999999999" value="${ payRiskMerchantLimitUpdate.limitMonthAmt/100 }"/>元
						</td>
						<td align="right">
							月成功交易额上限：
						</td>
						<td>
							<input class="easyui-numberbox" type="text" id="updatePayRiskMerchantLimitLimitMonthSuccessAmtTemp" missingMessage="请输入月成功交易额上限" 
								data-options="required:true" precision="2" max="99999999999" value="${ payRiskMerchantLimitUpdate.limitMonthSuccessAmt/100 }"/>元
						</td>
					</tr>
                    
                    <tr>
                        <td align="right">启用标志：</td>
                        <td	colspan="3">
                        	<input type="radio" name="isUse" value="0" <c:if test="${payRiskMerchantLimitUpdate.isUse eq '0'}">checked="checked"</c:if>/> 不可用
                        	<input type="radio" name="isUse" value="1" <c:if test="${payRiskMerchantLimitUpdate.isUse eq '1'}">checked="checked"</c:if>/> 可用
                        </td>
                    </tr>
                    <tr>
                        <td align="right" valign="top">备注：</td>
                        <td	colspan="3">
                        	<textarea class="textbox-text" autocomplete="off" rows="10" cols="80" id="updatePayRiskMerchantLimitLimitAddinfo" name="limitAddinfo">${ payRiskMerchantLimitUpdate.limitAddinfo }</textarea>
                        </td>
                    </tr>
            </table>
            <input type="hidden" name="limitMinAmt" id="updatePayRiskMerchantLimitLimitMinAmt">
			<input type="hidden" name="limitMaxAmt" id="updatePayRiskMerchantLimitLimitMaxAmt">
			<input type="hidden" name="limitDayAmt" id="updatePayRiskMerchantLimitLimitDayAmt">
			<input type="hidden" name="limitDaySuccessAmt" id="updatePayRiskMerchantLimitLimitDaySuccessAmt">
			<input type="hidden" name="limitMonthAmt" id="updatePayRiskMerchantLimitLimitMonthAmt">
			<input type="hidden" name="limitMonthSuccessAmt" id="updatePayRiskMerchantLimitLimitMonthSuccessAmt">
        </form>
    </div>
    <div data-options="region:'south',border:false" style="text-align:right;padding:5px;">
        <a class="easyui-linkbutton" data-options="iconCls:'icon-ok'" href="javascript:updatePayRiskMerchantLimitFormSubmit()" style="width:80px">确定</a>
        <a class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" href="javascript:void(0)" 
            onclick="$('#mainTabs').tabs('close',payRiskMerchantLimitUpdatePageTitle)" style="width:80px">取消</a>
    </div>
</div>
<script type="text/javascript">
$('#updatePayRiskMerchantLimitForm').form({
    url:'<%=path %>/updatePayRiskMerchantLimit.htm',
    onSubmit: function(){
        return $(this).form('validate');
    },
    success:function(data){
        $('#payRiskMerchantLimitList').datagrid('reload');
        if(data=='<%=JWebConstant.OK %>'){
            topCenterMessage('<%=JWebConstant.OK %>','修改成功！');
            closeTabFreshList('payRiskMerchantLimit',payRiskMerchantLimitUpdatePageTitle,payRiskMerchantLimitListPageTitle,'payRiskMerchantLimitList','<%=path %>/payRiskMerchantLimit.htm');
        } else topCenterMessage('<%=JWebConstant.ERROR %>',data);
    }
});

$('#updatePayRiskMerchantLimitLimitMinAmtTemp').numberbox({
	onChange:function(value){
		if(value.length>0)
		document.getElementById("updatePayRiskMerchantLimitLimitMinAmt").value=Math.ceil(parseFloat(value)*100);
	}
});
$('#updatePayRiskMerchantLimitLimitMaxAmtTemp').numberbox({
	onChange:function(value){
		if(value.length>0)
		document.getElementById("updatePayRiskMerchantLimitLimitMaxAmt").value=Math.ceil(parseFloat(value)*100);
	}
});
$('#updatePayRiskMerchantLimitLimitDayAmtTemp').numberbox({
	onChange:function(value){
		if(value.length>0)
		document.getElementById("updatePayRiskMerchantLimitLimitDayAmt").value=Math.ceil(parseFloat(value)*100);
	}
});
$('#updatePayRiskMerchantLimitLimitDaySuccessAmtTemp').numberbox({
	onChange:function(value){
		if(value.length>0)
		document.getElementById("updatePayRiskMerchantLimitLimitDaySuccessAmt").value=Math.ceil(parseFloat(value)*100);
	}
});
$('#updatePayRiskMerchantLimitLimitMonthAmtTemp').numberbox({
	onChange:function(value){
		if(value.length>0)
		document.getElementById("updatePayRiskMerchantLimitLimitMonthAmt").value=Math.ceil(parseFloat(value)*100);
	}
});
$('#updatePayRiskMerchantLimitLimitMonthSuccessAmtTemp').numberbox({
	onChange:function(value){
		if(value.length>0)
		document.getElementById("updatePayRiskMerchantLimitLimitMonthSuccessAmt").value=Math.ceil(parseFloat(value)*100);
	}
});

function updatePayRiskMerchantLimitFormSubmit(){
    $('#updatePayRiskMerchantLimitForm').submit();
}
</script>
