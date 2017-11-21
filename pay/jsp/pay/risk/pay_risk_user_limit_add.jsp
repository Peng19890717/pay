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
	$("#userTypeTabForUserRisk").textbox({required:false});
	$("#userTypeDivForUserRisk").hide();
	// 商户状态类型选择切换
	$("input[type=radio][name=userTypeTmp]").change(function(){
		//卡限额，指定用户类型无效（默认为4）
		if(document.getElementById("addPayRiskTranType").value=="7"){
			for(var i=1; i<=3;i++)document.getElementById("addPayRiskUserType"+i).checked=false;
			$("#userTypeTabForUserRisk").textbox({required:false});
			$("#userTypeDivForUserRisk").hide();
			$("#xListTypeFlagForAdd").css("display","none");
			document.getElementById("addPayRiskUserType").value="4";
			return;
		}
		if($(this).val()==3){
			$("#userTypeTabForUserRisk").textbox({required:true});
			$("#userTypeDivForUserRisk").show();
			$("#xListTypeFlagForAdd").css("display","none");
		}else{
			$("#userTypeTabForUserRisk").textbox({required:false});
			$("#userTypeDivForUserRisk").hide();
			$("#xListTypeFlagForAdd").css("display","");
		}
		document.getElementById("addPayRiskUserType").value = $(this).val();
	});
	// 限额生效时间初始化加载处理
	$("#payRiskUserLimitStartDate").datebox({required:false});
	$("#payRiskUserLimitEndDate").datebox({required:false});
	$("#timeScopeDivForUserRisk").hide();
	// 限额生效时间范围选择切换
	$("input[type=radio][name=limitTimeFlag]").change(function(){
		if($(this).val()==1){
			$("#payRiskUserLimitStartDate").datebox({required:true});
			$("#payRiskUserLimitEndDate").datebox({required:true});
			$("#timeScopeDivForUserRisk").show();
		}else{
			$("#payRiskUserLimitStartDate").datebox({required:false});
			$("#payRiskUserLimitEndDate").datebox({required:false});
			$("#timeScopeDivForUserRisk").hide();
		}
	});
});
function disableRiskTranType(tranType){
	$("#tranTypeLimitTranForAdd").css("display","none");
	$("#tranTypeLimitCardForAdd").css("display","none");
	$("#tranTypeLimitConsumeForAdd").css("display","none");
	$("#tranTypeLimitOtherConsumeForAdd").css("display","none");
	if(tranType=="<%=com.PayConstant.TRAN_TYPE_CONSUME %>"){
		$("#tranTypeLimitConsumeForAdd").css("display","");
		$("#tranTypeLimitOtherConsumeForAdd").css("display","none");
	} else if(tranType=="7"){
		$("#tranTypeLimitCardForAdd").css("display","");
		for(var i=1; i<=3;i++)document.getElementById("addPayRiskUserType"+i).checked=false;
		$("#userTypeTabForUserRisk").textbox({required:false});
		$("#userTypeDivForUserRisk").hide();
		$("#xListTypeFlagForAdd").css("display","none");
		document.getElementById("addPayRiskUserType").value="4";
	} else {
		document.getElementById("addPayRiskUserType2").checked=true;
		document.getElementById("addPayRiskUserType").value="2";
		$("#tranTypeLimitTranForAdd").css("display","");
		$("#tranTypeLimitOtherConsumeForAdd").css("display","");
	}
}
</script>
<div class="easyui-layout" data-options="fit:true">
    <div data-options="region:'center'">
        <form id="addPayRiskUserLimitForm" method="post">
        	<input type="hidden" name="busCode" value=""/>
        	<input id="addPayRiskUserType" type="hidden" name="userType" value="2"/>
        	<input type="hidden" name="maxCardNum" value="0"/>
            <table cellpadding="5" width="100%" style="margin-top:-10px;">
                <tr><td  width="160">&nbsp;</td><td width="200">&nbsp;</td><td width="120">&nbsp;</td><td>&nbsp;</td></tr>
                    <tr>
                        <td align="right">用户状态类型：</td>
                        <td colspan="3">
                        	<input id="addPayRiskUserType1" type="radio" name="userTypeTmp"  value="1" data-options="required:true" />实名
                        	<input id="addPayRiskUserType2" type="radio" name="userTypeTmp"  value="2" data-options="required:true" checked="checked"/>非实名
                        	<input id="addPayRiskUserType3" type="radio" name="userTypeTmp"  value="3" data-options="required:true"/>指定用户&nbsp;
                        	<div style="display: inLine" id="userTypeDivForUserRisk"><input type='text' class='easyui-textbox' name='userCode' class='easyui-textbox' id='userTypeTabForUserRisk' 
                        		missingMessage="请输入用户编号" validType="length[0,20]" invalidMessage="请填写合法的用户号" data-options="required:true"></div>
                        </td>
                    </tr>
                    <tr id="xListTypeFlagForAdd">
                        <td align="right">用户名单状态：</td>
                        <td colspan="3">
                        	<select name="xListType">
                        		<option value="1">白名单</option>
                        		<option value="4">红名单</option>
                        	</select>
                        </td>
                    </tr>
                    <tr>
                        <td align="right">交易类型：</td>
                        <td colspan="3">
                        	<select id="addPayRiskTranType" name="tranType" onchange="disableRiskTranType(this.value)">
                        		<option value="1">消费</option>
                        		<option value="2">充值</option>
                        		<!-- option value="3">结算</option>
                        		<option value="4">退款</option -->
                        		<option value="5">提现</option>
                        		<option value="6">转账</option>
                        		<option value="7">卡限额</option>
                        	</select>
                        </td>
                    </tr>
                    <tr>
                    	<td align="right">生效时间：</td>
                        <td colspan="3">
	                       	<input type="radio" name="limitTimeFlag"  value="0" data-options="required:true" checked="checked"/>永久&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	                       	<input type="radio" name="limitTimeFlag"  value="1" data-options="required:true"/>按交易时间&nbsp;
	                       	<div style="display: inLine" id="timeScopeDivForUserRisk">
	                        	<input class="easyui-datebox" style="width:100px" data-options="editable:false,required:true" id="payRiskUserLimitStartDate" name="startDate" missingMessage="请输入开始时间"/>
	                        	 ~ 
	                        	<input class="easyui-datebox" style="width:100px" data-options="editable:false,required:true" id="payRiskUserLimitEndDate" name="endDate" missingMessage="请输入结束时间"/>
                           	</div>
                       	</td>
                    </tr>
                    <tr id="tranTypeLimitConsumeForAdd">
                        <td colspan="4">
                        <%
	                    for(int i=0; i<com.PayConstant.TRAN_TYPE_CONSUMES.length; i++){
	                     	if(i==0){//账户支付，只限制成功交易部分
	                     	%>
	                      <table cellpadding="5" width="800">
	                      	<tr><td bgcolor="#CCCCCC" colspan="4"><%=com.PayConstant.TRAN_TYPE_CONSUMES[i] %>
	                      	<input type="hidden" name="dayTimes<%="0"+i %>" value="1000000000"/>
		                  	<input type="hidden" name="monthTimes<%="0"+i %>" value="1000000000"/>
	                      	</td></tr>
	                       	<tr>
		                        <td width="160" align="right">单笔最小金额：</td>
		                        <td width="200"><input class="easyui-numberbox" type="text" id="addPayRiskUserLimitMinAmt<%="0"+i %>Temp" missingMessage="请输入单笔最小金额" data-options="required:true" precision="2" max="99999999999" value="0"/>元</td>
		                        <td width="120" align="right">单笔最大金额：</td>
		                        <td><input class="easyui-numberbox" type="text" id="addPayRiskUserLimitMaxAmt<%="0"+i %>Temp" missingMessage="请输入单笔最大金额" data-options="required:true" precision="2" max="99999999999" value="99999999999"/>元</td>
		                    </tr>
		                    <tr>
		                        <td align="right">日成功交易次数上限：</td>
		                        <td><input class="easyui-numberbox" type="text" max="1000000000" id="addPayRiskUserLimitDaySucTimes<%="0"+i %>" name="daySucTimes<%="0"+i %>" missingMessage="请输入日成功交易次数上限" data-options="required:true" validType="length[1,10]" invalidMessage="日交易次数上限必填" value="1000000000"/>次</td>
		                        <td align="right">日成功交易额上限：</td>
		                        <td><input class="easyui-numberbox" type="text" id="addPayRiskUserLimitDaySucAmt<%="0"+i %>Temp" missingMessage="请输入日成功交易额上限" data-options="required:true" precision="2" max="99999999999" value="99999999999"/>元</td>
		                    </tr>
		                    <tr>
		                        <td align="right">月成功交易次数上限：</td>
		                        <td><input class="easyui-numberbox" type="text" max="1000000000" id="addPayRiskUserLimitMonthSucTimes<%="0"+i %>" name="monthSucTimes<%="0"+i %>" missingMessage="请输入月成功交易次数上限" data-options="required:true" validType="length[1,10]" invalidMessage="月交易次数上限必填" value="1000000000"/>次</td>
		                        <td align="right">月成功交易额上限：</td>
		                        <td><input class="easyui-numberbox" type="text" id="addPayRiskUserLimitMonthSucAmt<%="0"+i %>Temp" missingMessage="请输入月成功交易额上限" data-options="required:true" precision="2" max="99999999999" value="99999999999"/>元</td>
		                    </tr>
	                      </table>
	                      <%} else {
	                      %><table cellpadding="5" width="800">
	                      	<tr><td bgcolor="#CCCCCC" colspan="4"><%=com.PayConstant.TRAN_TYPE_CONSUMES[i] %></td></tr>
	                       	<tr>
		                        <td width="160" align="right">单笔最小金额：</td>
		                        <td width="200"><input class="easyui-numberbox" type="text" id="addPayRiskUserLimitMinAmt<%="0"+i %>Temp" missingMessage="请输入单笔最小金额" data-options="required:true" precision="2" max="99999999999" value="0"/>元</td>
		                        <td width="120" align="right">单笔最大金额：</td>
		                        <td><input class="easyui-numberbox" type="text" id="addPayRiskUserLimitMaxAmt<%="0"+i %>Temp" missingMessage="请输入单笔最大金额" data-options="required:true" precision="2" max="99999999999" value="99999999999"/>元</td>
		                    </tr>
		                    <tr>
		                        <td align="right">日交易次数上限：</td>
		                        <td><input class="easyui-numberbox" type="text" max="1000000000" id="addPayRiskUserLimitDayTimes<%="0"+i %>" name="dayTimes<%="0"+i %>" 
		                        	missingMessage="请输入日交易次数上限" data-options="required:true" validType="length[1,10]" invalidMessage="日交易次数上限必填" value="1000000000"/>次</td>
		                        <td align="right">日成功交易次数上限：</td>
		                        <td><input class="easyui-numberbox" type="text" max="1000000000" id="addPayRiskUserLimitDaySucTimes<%="0"+i %>" name="daySucTimes<%="0"+i %>" missingMessage="请输入日成功交易次数上限" data-options="required:true" validType="length[1,10]" invalidMessage="日交易次数上限必填" value="1000000000"/>次</td>
		                    </tr>
		                    <tr>
		                        <td align="right">日交易额上限：</td>
		                        <td><input class="easyui-numberbox" type="text" id="addPayRiskUserLimitDayAmt<%="0"+i %>Temp" missingMessage="请输入日交易限额上限" 
		                        	data-options="required:true" precision="2" max="99999999999" value="99999999999"/>元</td>
		                        <td align="right">日成功交易额上限：</td>
		                        <td><input class="easyui-numberbox" type="text" id="addPayRiskUserLimitDaySucAmt<%="0"+i %>Temp" missingMessage="请输入日成功交易额上限" data-options="required:true" precision="2" max="99999999999" value="99999999999"/>元</td>
		                    </tr>
		                    <tr>
		                        <td align="right">月交易次数上限：</td>
		                        <td><input class="easyui-numberbox" type="text" max="1000000000" id="addPayRiskUserLimitMonthTimes<%="0"+i %>" name="monthTimes<%="0"+i %>" 
		                        	missingMessage="请输入月交易次数上限" data-options="required:true" validType="length[1,10]" invalidMessage="月交易次数上限必填" value="1000000000"/>次</td>
		                        <td align="right">月成功交易次数上限：</td>
		                        <td><input class="easyui-numberbox" type="text" max="1000000000" id="addPayRiskUserLimitMonthSucTimes<%="0"+i %>" name="monthSucTimes<%="0"+i %>" missingMessage="请输入月成功交易次数上限" data-options="required:true" validType="length[1,10]" invalidMessage="月交易次数上限必填" value="1000000000"/>次</td>
		                    </tr>
		                    <tr>
		                        <td align="right">月交易额上限：</td>
		                        <td><input class="easyui-numberbox" type="text" id="addPayRiskUserLimitMonthAmt<%="0"+i %>Temp" 
		                        	missingMessage="请输入月交易限额上限" data-options="required:true" precision="2" max="99999999999" value="99999999999"/>元</td>
		                        <td align="right">月成功交易额上限：</td>
		                        <td><input class="easyui-numberbox" type="text" id="addPayRiskUserLimitMonthSucAmt<%="0"+i %>Temp" missingMessage="请输入月成功交易额上限" data-options="required:true" precision="2" max="99999999999" value="99999999999"/>元</td>
		                    </tr>
	                      </table><%
	                      	}
	                      } %>
                       	</td>
                    </tr>
                    <tr id="tranTypeLimitOtherConsumeForAdd">
                       <td colspan="4">
                       <table cellpadding="5" width="800">
	                       	<tr>
		                        <td width="160" align="right">单笔最小金额：</td>
		                        <td width="200"><input class="easyui-numberbox" type="text" id="addPayRiskUserLimitMinAmtTemp" missingMessage="请输入单笔最小金额" data-options="required:true" precision="2" max="99999999999" value="0"/>元</td>
		                        <td width="120" align="right">单笔最大金额：</td>
		                        <td><input class="easyui-numberbox" type="text" id="addPayRiskUserLimitMaxAmtTemp" missingMessage="请输入单笔最大金额" data-options="required:true" precision="2" max="99999999999" value="99999999999"/>元</td>
		                    </tr>
		                    <tr>
		                        <td align="right">日交易次数上限：</td>
		                        <td><input class="easyui-numberbox" type="text" max="1000000000" id="addPayRiskUserLimitDayTimes" name="dayTimes" 
		                        	missingMessage="请输入日交易次数上限" data-options="required:true" validType="length[1,10]" invalidMessage="日交易次数上限必填" value="1000000000"/>次</td>
		                        <td align="right">日成功交易次数上限：</td>
		                        <td><input class="easyui-numberbox" type="text" max="1000000000" id="addPayRiskUserLimitDaySucTimes" name="daySucTimes" missingMessage="请输入日成功交易次数上限" data-options="required:true" validType="length[1,10]" invalidMessage="日交易次数上限必填" value="1000000000"/>次</td>
		                    </tr>
		                    <tr>
		                        <td align="right">日交易额上限：</td>
		                        <td><input class="easyui-numberbox" type="text" id="addPayRiskUserLimitDayAmtTemp" missingMessage="请输入日交易限额上限" 
		                        	data-options="required:true" precision="2" max="99999999999" value="99999999999"/>元</td>
		                        <td align="right">日成功交易额上限：</td>
		                        <td><input class="easyui-numberbox" type="text" id="addPayRiskUserLimitDaySucAmtTemp" missingMessage="请输入日成功交易额上限" data-options="required:true" precision="2" max="99999999999" value="99999999999"/>元</td>
		                    </tr>
		                    <tr>
		                        <td align="right">月交易次数上限：</td>
		                        <td><input class="easyui-numberbox" type="text" max="1000000000" id="addPayRiskUserLimitMonthTimes" name="monthTimes" 
		                        	missingMessage="请输入月交易次数上限" data-options="required:true" validType="length[1,10]" invalidMessage="月交易次数上限必填" value="1000000000"/>次</td>
		                        <td align="right">月成功交易次数上限：</td>
		                        <td><input class="easyui-numberbox" type="text" max="1000000000" id="addPayRiskUserLimitMonthSucTimes" name="monthSucTimes" missingMessage="请输入月成功交易次数上限" data-options="required:true" validType="length[1,10]" invalidMessage="月交易次数上限必填" value="1000000000"/>次</td>
		                    </tr>
		                    <tr>
		                        <td align="right">月交易额上限：</td>
		                        <td><input class="easyui-numberbox" type="text" id="addPayRiskUserLimitMonthAmtTemp" 
		                        	missingMessage="请输入月交易限额上限" data-options="required:true" precision="2" max="99999999999" value="99999999999"/>元</td>
		                        <td align="right">月成功交易额上限：</td>
		                        <td><input class="easyui-numberbox" type="text" id="addPayRiskUserLimitMonthSucAmtTemp" missingMessage="请输入月成功交易额上限" data-options="required:true" precision="2" max="99999999999" value="99999999999"/>元</td>
		                    </tr>
	                      </table>
                       	</td>
                    </tr>
                    <tr id="tranTypeLimitCardForAdd">
                        <td colspan="4">
	                      <table cellpadding="5" width="100%">
	                       	<tr>
		                        <td width="160" align="right">信用卡单卡单笔最高限额：</td>
		                        <td><input class="easyui-numberbox" type="text" id="addPayRiskUserLimitCrebitCardOnceAmtTemp" missingMessage="请输入限额" data-options="required:true" precision="2" max="99999999999" value="99999999999"/>元</td>
		                    </tr>
		                    <tr>
		                        <td align="right">信用卡单卡日累计最高限额：</td>
		                        <td><input class="easyui-numberbox" type="text" id="addPayRiskUserLimitCrebitCardDayAmtTemp" missingMessage="请输入限额" data-options="required:true" precision="2" max="99999999999" value="99999999999"/>元</td>
		                    </tr>
		                    <tr>
		                        <td align="right">信用卡两次卡交易时间间隔：</td>
		                        <td><input class="easyui-numberbox" type="text" id="addPayRiskUserLimitCrebitCardTxnInterval" missingMessage="请输时间间隔" data-options="required:true" name="crebitCardTxnInterval" validType="length[1,10]" value="-1"/>秒（-1为无限制）</td>
		                    </tr>
		                    <tr>
		                        <td align="right">借记卡单卡单笔最高限额：</td>
		                        <td><input class="easyui-numberbox" type="text" id="addPayRiskUserLimitDebitCardOnceAmtTemp" missingMessage="请输入限额" data-options="required:true" precision="2" max="99999999999" value="99999999999"/>元</td>
		                    </tr>
		                    <tr>
		                        <td align="right">借记卡单卡日累计最高限额：</td>
		                        <td><input class="easyui-numberbox" type="text" id="addPayRiskUserLimitDebitCardDayAmtTemp" missingMessage="请输入限额" data-options="required:true" precision="2" max="99999999999" value="99999999999"/>元</td>
		                    </tr>
		                    <tr>
		                        <td align="right">借记卡两次卡交易时间间隔：</td>
		                        <td><input class="easyui-numberbox" type="text" id="addPayRiskUserLimitDebitCardTxnInterval" missingMessage="请输入时间间隔" data-options="required:true"  name="debitCardTxnInterval" validType="length[1,10]" value="-1"/>秒（-1为无限制）</td>
		                    </tr>
	                      </table>
                       	</td>
                    </tr>
                    <tr>
                        <td width="160" align="right">启用标志：</td>
                        <td colspan="3">
                        	<input type="radio" name="isUse" value="0" checked="checked"/> 可用
                        	<input type="radio" name="isUse" value="1"/> 不可用
                        </td>
                    </tr>
                    <tr>
                        <td width="160" align="right" valign="top">备注：</td>
                        <td colspan="3">
                        	<textarea class="textbox-text" autocomplete="off" rows="10" cols="80" name="remark"></textarea>
                        </td>
                    </tr>
            </table>
           	<input type="hidden" name="minAmt" id="addPayRiskUserLimitMinAmt" value="0">
			<input type="hidden" name="maxAmt" id="addPayRiskUserLimitMaxAmt" value="99999999999">
			<input type="hidden" name="dayAmt" id="addPayRiskUserLimitDayAmt" value="99999999999">
			<input type="hidden" name="daySucAmt" id="addPayRiskUserLimitDaySucAmt" value="99999999999">
			<input type="hidden" name="monthAmt" id="addPayRiskUserLimitMonthAmt" value="99999999999">
			<input type="hidden" name="monthSucAmt" id="addPayRiskUserLimitMonthSucAmt" value="99999999999">
			
			<%
            for(int i=0; i<com.PayConstant.TRAN_TYPE_CONSUMES.length; i++){ %>
            <input type="hidden" name="minAmt<%="0"+i %>" id="addPayRiskUserLimitMinAmt<%="0"+i %>" value="0">
			<input type="hidden" name="maxAmt<%="0"+i %>" id="addPayRiskUserLimitMaxAmt<%="0"+i %>" value="99999999999">
			<input type="hidden" name="dayAmt<%="0"+i %>" id="addPayRiskUserLimitDayAmt<%="0"+i %>" value="99999999999">
			<input type="hidden" name="daySucAmt<%="0"+i %>" id="addPayRiskUserLimitDaySucAmt<%="0"+i %>" value="99999999999">
			<input type="hidden" name="monthAmt<%="0"+i %>" id="addPayRiskUserLimitMonthAmt<%="0"+i %>" value="99999999999">
			<input type="hidden" name="monthSucAmt<%="0"+i %>" id="addPayRiskUserLimitMonthSucAmt<%="0"+i %>" value="99999999999">	
            <%} %>
			
			<input type="hidden" name="crebitCardOnceAmt" id="addPayRiskUserLimitCrebitCardOnceAmt" value="99999999999">
			<input type="hidden" name="crebitCardDayAmt" id="addPayRiskUserLimitCrebitCardDayAmt" value="99999999999">
			<input type="hidden" name="debitCardOnceAmt" id="addPayRiskUserLimitDebitCardOnceAmt" value="99999999999">
			<input type="hidden" name="debitCardDayAmt" id="addPayRiskUserLimitDebitCardDayAmt" value="99999999999">
        </form>
    </div>
    <div data-options="region:'south',border:false" style="text-align:right;padding:5px;">
        <a class="easyui-linkbutton" data-options="iconCls:'icon-ok'" href="javascript:addPayRiskUserLimitFormSubmit()" style="width:80px">确定</a>
        <a class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" href="javascript:void(0)" 
            onclick="$('#addPayRiskUserLimitForm').form('clear')" style="width:80px">取消</a>
    </div>
</div>
<script type="text/javascript">
$('#addPayRiskUserLimitForm').form({
    url:'<%=path %>/addPayRiskUserLimit.htm',
    onSubmit: function(){
        return $(this).form('validate');
    },
   success:function(data){
        if(data=='<%=JWebConstant.OK %>'){
            topCenterMessage('<%=JWebConstant.OK %>','添加成功！');
            closeTabFreshList('payRiskUserLimit',payRiskUserLimitAddPageTitle,payRiskUserLimitListPageTitle,'payRiskUserLimitList','<%=path %>/payRiskUserLimit.htm');
        } else topCenterMessage('<%=JWebConstant.ERROR %>',data);
    }
});
function addPayRiskUserLimitFormSubmit(){
    $('#addPayRiskUserLimitForm').submit();
}

$('#addPayRiskUserLimitMinAmtTemp').numberbox({
	onChange:function(value){
		if(value.length>0)
		document.getElementById("addPayRiskUserLimitMinAmt").value=Math.ceil(parseFloat(value)*100);
	}
});
$('#addPayRiskUserLimitMaxAmtTemp').numberbox({
	onChange:function(value){
		if(value.length>0)
		document.getElementById("addPayRiskUserLimitMaxAmt").value=Math.ceil(parseFloat(value)*100);
	}
});
$('#addPayRiskUserLimitDayAmtTemp').numberbox({
	onChange:function(value){
		if(value.length>0)
		document.getElementById("addPayRiskUserLimitDayAmt").value=Math.ceil(parseFloat(value)*100);
	}
});
$('#addPayRiskUserLimitDaySucAmtTemp').numberbox({
	onChange:function(value){
		if(value.length>0)
		document.getElementById("addPayRiskUserLimitDaySucAmt").value=Math.ceil(parseFloat(value)*100);
	}
});
$('#addPayRiskUserLimitMonthAmtTemp').numberbox({
	onChange:function(value){
		if(value.length>0)
		document.getElementById("addPayRiskUserLimitMonthAmt").value=Math.ceil(parseFloat(value)*100);
	}
});
$('#addPayRiskUserLimitMonthSucAmtTemp').numberbox({
	onChange:function(value){
		if(value.length>0)
		document.getElementById("addPayRiskUserLimitMonthSucAmt").value=Math.ceil(parseFloat(value)*100);
	}
});


<%for(int i=0; i<com.PayConstant.TRAN_TYPE_CONSUMES.length; i++){%>
	$('#addPayRiskUserLimitMinAmt<%="0"+i %>Temp').numberbox({
		onChange:function(value){
			if(value.length>0)
			document.getElementById("addPayRiskUserLimitMinAmt<%="0"+i %>").value=Math.ceil(parseFloat(value)*100);
		}
	});
	$('#addPayRiskUserLimitMaxAmt<%="0"+i %>Temp').numberbox({
		onChange:function(value){
			if(value.length>0)
			document.getElementById("addPayRiskUserLimitMaxAmt<%="0"+i %>").value=Math.ceil(parseFloat(value)*100);
		}
	});
	$('#addPayRiskUserLimitDayAmt<%="0"+i %>Temp').numberbox({
		onChange:function(value){
			if(value.length>0)
			document.getElementById("addPayRiskUserLimitDayAmt<%="0"+i %>").value=Math.ceil(parseFloat(value)*100);
		}
	});
	$('#addPayRiskUserLimitDaySucAmt<%="0"+i %>Temp').numberbox({
		onChange:function(value){
			if(value.length>0)
			document.getElementById("addPayRiskUserLimitDaySucAmt<%="0"+i %>").value=Math.ceil(parseFloat(value)*100);
		}
	});
	$('#addPayRiskUserLimitMonthAmt<%="0"+i %>Temp').numberbox({
		onChange:function(value){
			if(value.length>0)
			document.getElementById("addPayRiskUserLimitMonthAmt<%="0"+i %>").value=Math.ceil(parseFloat(value)*100);
		}
	});
	$('#addPayRiskUserLimitMonthSucAmt<%="0"+i %>Temp').numberbox({
		onChange:function(value){
			if(value.length>0)
			document.getElementById("addPayRiskUserLimitMonthSucAmt<%="0"+i %>").value=Math.ceil(parseFloat(value)*100);
		}
	});
<%}%>

$('#addPayRiskUserLimitCrebitCardOnceAmtTemp').numberbox({
	onChange:function(value){
		if(value.length>0)
		document.getElementById("addPayRiskUserLimitCrebitCardOnceAmt").value=Math.ceil(parseFloat(value)*100);
	}
});
$('#addPayRiskUserLimitCrebitCardDayAmtTemp').numberbox({
	onChange:function(value){
		if(value.length>0)
		document.getElementById("addPayRiskUserLimitCrebitCardDayAmt").value=Math.ceil(parseFloat(value)*100);
	}
});
$('#addPayRiskUserLimitDebitCardOnceAmtTemp').numberbox({
	onChange:function(value){
		if(value.length>0)
		document.getElementById("addPayRiskUserLimitDebitCardOnceAmt").value=Math.ceil(parseFloat(value)*100);
	}
});
$('#addPayRiskUserLimitDebitCardDayAmtTemp').numberbox({
	onChange:function(value){
		if(value.length>0)
		document.getElementById("addPayRiskUserLimitDebitCardDayAmt").value=Math.ceil(parseFloat(value)*100);
	}
});
$(document).ready(function() {
	setTimeout(initHidenInfo, 100);
});
function initHidenInfo(){
	$("#tranTypeLimitCardForAdd").css("display","none");
	$("#tranTypeLimitTranForAdd").css("display","none");
	$("#tranTypeLimitOtherConsumeForAdd").css("display","none");
}
</script>
