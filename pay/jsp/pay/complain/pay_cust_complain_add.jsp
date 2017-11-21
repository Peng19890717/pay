<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ page import="util.JWebConstant"%>
<%@ page import="com.jweb.dao.JWebUser"%>
<%@ page import="com.jweb.dao.JWebAction"%>
<%@ page import="com.jweb.service.JWebUserService"%>
<%@ page import="com.pay.coopbank.dao.PayCoopBank"%>
<%@page import="com.pay.coopbank.service.PayCoopBankService"%>
<%
String path = request.getContextPath();
//登录和权限判断
JWebUser user = (JWebUser)session.getAttribute("user");
if(!JWebUserService.checkUser(user,request)){
%><script>window.location.href='<%=path %>/jsp/jweb/login.jsp'</script><%
    return;
}
%>
<link rel="stylesheet" type="text/css" href="<%=path %>/js/upload/uploadify.css"  />
<script type="text/javascript" src="<%=path %>/js/upload/jquery.uploadify.min.js"></script>
<script type="text/javascript" src="<%=path %>/js/jquery.form.js"></script>
<script type="text/javascript">
$(document).ready(function(){});
</script>
<div class="easyui-layout" data-options="fit:true">
    <div data-options="region:'center'">
        <form id="addPayCustComplainForm" method="post">
            <table cellpadding="5" width="100%" style="margin-top:-10px;">
                <tr><td width="100">&nbsp;</td><td>&nbsp;</td></tr>
                	<tr>
                        <td align="right">投诉类型：</td>
                        <td>
	                        <select name="type" id="addPayCustComplainType" class="easyui-combobox">
		                         <option value="0">用户</option>
		                         <option value="1">商户</option>  
	                    	</select> 
						</td>
                    </tr>
                    <tr>
                        <td align="right">所属商户号：</td>
                        <td><input class="easyui-textbox"  type="text" name="custId" id="addPayCustComplainCustId"  validType="length[1,15]" 
                        	missingMessage="请输入商户号" invalidMessage="商户编号为1-15个字符"  data-options="required:true"/></td>
                    </tr>
                    <tr>
                        <td align="right">投诉交易金额：</td>
                        <td><input class="easyui-numberbox" type="text" id="addPayCustComplainTransAmtTemp" missingMessage="请输入投诉交易金额"
                                data-options="required:true" precision="2" min="0.01" max="99999999999" invalidMessage=""/>&nbsp;&nbsp;元</td>
                    </tr>
                    <tr>
                        <td align="right">冻结金额：</td>
                        <td><input class="easyui-numberbox" type="text" id="addPayCustComplainFrozenAmtTemp" missingMessage="请输入冻结金额"
                                data-options="required:true" precision="2" min="0.01" max="99999999999" invalidMessage=""/>&nbsp;&nbsp;元</td>
                    </tr>
                    <tr>
                        <td align="right">冻结方式：</td>
                        <td><select name="isFrozenFlag" id="addPayCustComplainIsFrozenFlag" class="easyui-combobox"  data-options="required:true">
	                        	<option value="0">自动冻结</option>
		                        <option value="1">手动冻结</option>
	                    	</select></td>
                    </tr>
                    <!-- <tr>
                        <td align="right">资金处理结果：</td>
                        <td>
	                        <select name="optStatus" id="addPayCustComplainOptStatus" class="easyui-combobox"  data-options="required:true">
	                        	<option value="0">初始</option>
		                        <option value="1">返还</option>
		                        <option value="2">部分冻结</option>
		                        <option value="3">全部扣减</option>
	                    	</select> 
						</td>
                    </tr> -->
                    <tr>
                        <td align="right">客户姓名：</td>
                        <td><input class="easyui-textbox" type="text" id="addPayCustComplainName" name="name" data-options="required:true"/></td>
                    </tr>
                    <tr>
                        <td align="right">客户电话：</td>
                        <td><input class="easyui-textbox" type="text" id="addPayCustComplainTel" name="tel" data-options="required:true"/></td>
                    </tr>
                    <tr>
                        <td align="right">渠道：</td>
                        <td><select  class="easyui-combobox" panelHeight="auto" id="addPayCustComplainChannel" data-options="panelHeight:500,editable:false,required:true" name="channel">
					 		<option value=""></option>
					  		<%for(int i=0; i<PayCoopBankService.CHANNEL_LIST_ALL.size(); i++){
								PayCoopBank channel = PayCoopBankService.CHANNEL_LIST_ALL.get(i);%>
								<option value="<%=channel.bankCode %>"><%=channel.bankName %></option>
							<%}%>
					 	</select></td>
                    </tr>
                    <tr>
                        <td align="right">投诉凭证：</td>
                        <td><input type="file" name="uploadStartFile" id="addPayCustComplainStartFileTemp"></td>
                    </tr>
                    <tr>
                        <td align="right">完成凭证：</td>
                        <td><input type="file" name="uploadEndFile" id="addPayCustComplainEndFileTemp"></td>
                    </tr>
                    <tr>
                        <td align="right">订单号：</td>
                        <td><input class="easyui-textbox" type="text" id="addPayCustComplainOrderIds" name="orderIds" missingMessage="请输入订单号"
                             data-options="required:true"/></td>
                    </tr>
                    <tr>
                        <td align="right" valign="top">投诉内容：</td>
	                    <td><input class="easyui-textbox" type="text" id="addPayCustComplainCContent" name="cContent" data-options="multiline:true" style="width:240px;height:70px"/></td>
                    </tr>
                    <tr>
                        <td align="right" valign="top">备注：</td>
                        <td><input class="easyui-textbox" type="text" id="addPayCustComplainRemark" name="remark" data-options="multiline:true" style="width:240px;height:70px"/></td>
                    </tr>
                    <!-- <tr>
                        <td align="right">扣减金额：</td>
                        <td><input class="easyui-numberbox" type="text" id="addPayCustComplainDeductAmtTemp" missingMessage="请输入扣减金额"
                                data-options="required:true" precision="2" max="99999999999" invalidMessage=""/>&nbsp;&nbsp;元</td>
                    </tr> -->
                    
                    <input type="hidden" name="transAmt" id="addPayCustComplainTransAmt">
                    <input type="hidden" name="frozenAmt" id="addPayCustComplainFrozenAmt">
                    <input type="hidden" name="deductAmt" id="addPayCustComplainDeductAmt" value="0">
                    <input type="hidden" name="endFile" id="addPayCustComplainEndFile">
                    <input type="hidden" name="startFile" id="addPayCustComplainStartFile">
                    <input type="hidden" name="id" id="addCustPlainId">
                    <input type="hidden" name="optStatus" id="addPayCustComplainOptStatus" value="0">
            </table>
        </form>
    </div>
    <div data-options="region:'south',border:false" style="text-align:right;padding:5px;">
        <a class="easyui-linkbutton" data-options="iconCls:'icon-ok'" href="javascript:addPayCustComplainFormSubmit()" style="width:80px">确定</a>
        <a class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" href="javascript:void(0)" 
            onclick="$('#addPayCustComplainForm').form('clear')" style="width:80px">取消</a>
    </div>
</div>
<script type="text/javascript">
$(function(){
	$('#addPayCustComplainStartFileTemp').change(function(){
		var options = {
			url:'<%=path %>/startFileUploadForCustComplain.htm',
			type : 'post',
			dataType : 'json',
			success : function(data) {
				$('#addPayCustComplainStartFile').val(data.saveUrl);
				$('#addCustPlainId').val(data.custPlainId);
			}
		};
		$('#addPayCustComplainForm').ajaxSubmit(options);
	});
	$('#addPayCustComplainEndFileTemp').change(function(){
		var options = {
			url:'<%=path %>/endFileUploadForCustComplain.htm',
			type : 'post',
			dataType : 'json',
			success : function(data) {
				$('#addPayCustComplainEndFile').val(data.saveUrl);
				$('#addCustPlainId').val(data.custPlainId);
			}
		};
		$('#addPayCustComplainForm').ajaxSubmit(options);
	});
});
$('#addPayCustComplainTransAmtTemp').numberbox({
	onChange:function(value){
		if(value.length>0){
			document.getElementById("addPayCustComplainTransAmt").value=parseInt((parseFloat(value)*100).toPrecision(12));
		}
	}
});
$('#addPayCustComplainFrozenAmtTemp').numberbox({
	onChange:function(value){
		if(value.length>0){
			document.getElementById("addPayCustComplainFrozenAmt").value=parseInt((parseFloat(value)*100).toPrecision(12));
		}
	}
});
/*
$('#addPayCustComplainDeductAmtTemp').numberbox({
	onChange:function(value){
		if(value.length>0){
			document.getElementById("addPayCustComplainDeductAmt").value=parseInt((parseFloat(value)*100).toPrecision(12));
		}
	}
});
*/
$('#addPayCustComplainForm').form({
    url:'<%=path %>/addPayCustComplain.htm',
    onSubmit: function(){
        return $(this).form('validate');
    },
    success:function(data){
        if(data=='<%=JWebConstant.OK %>'){
            topCenterMessage('<%=JWebConstant.OK %>','添加成功！');
            closeTabFreshList('payCustComplain',payCustComplainAddPageTitle,payCustComplainListPageTitle,'payCustComplainList','<%=path %>/payCustComplain.htm');
        } else topCenterMessage('<%=JWebConstant.ERROR %>',data);
    }
});
function addPayCustComplainFormSubmit(){
	if('' == $.trim($('#addPayCustComplainStartFile').val())){
		topCenterMessage('<%=JWebConstant.ERROR %>','请上传投诉凭证');
		return;
	}
	/*
	if('' == $.trim($('#addPayCustComplainEndFile').val())){
		topCenterMessage('<%=JWebConstant.ERROR %>','请上传完成凭证');
		return;
	}*/
    $('#addPayCustComplainForm').submit();
}
</script>
