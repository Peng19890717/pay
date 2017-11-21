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

});
</script>
<div class="easyui-layout" data-options="fit:true">
    <div data-options="region:'center'">
    <div class="merchant-lable">费率添加</div>
    <hr color="#ccc"/>
        <form id="addPayFeeRateForm" method="post">
        	<input type="hidden" name="maxFee" value="0"/>
        	<input type="hidden" name="minFee" value="0"/>
        	<input type="hidden" name="startCalAmt" value="0"/>
        	<input type="hidden" name="multiSectionMode" value="0"/>
        	<input type="hidden" name="feeInfo" id="addPayFeeRateReeInfo" value=""/>
        	<input type="hidden" name="bizType" value=""/>
            <table cellpadding="5" width="100%">
                <tr>
                    <td width="100" align="right">费率名称:</td>
                    <td><input class="easyui-textbox" type="text" id="addPayFeeRateFeeName" name="feeName" missingMessage="请输入费率名称"
                        validType="length[1,20]" invalidMessage="费率名称为1-20个字符" data-options="required:true"/></td>
                </tr>
                <tr>
                    <td align="right">货币：</td>
                    <td><input class="easyui-textbox" type="text" id="addPayFeeRateCcy" name="ccy" missingMessage="请输入货币"
                        validType="length[1,20]" invalidMessage="货币为1-20个字符" data-options="required:true,readonly:true" value="CNY"/></td>
                </tr>
                <tr>
                    <td align="right">客户类型:</td>
                    <td>
                    	<select name="custType" id="addPayFeeRateCustType" data-options="editable:false,required:true">
					      <option value="<%=com.PayConstant.CUST_TYPE_USER %>">个人</option>
					      <option value="<%=com.PayConstant.CUST_TYPE_MERCHANT %>">商户</option>
					      <option value="<%=com.PayConstant.CUST_TYPE_PAY_CHANNEL %>">合作渠道</option>
					    </select>
                    </td>
                </tr>
                <tr>
                    <td align="right">类型:</td>
                    <td>
                    	<select name="tranType" id="addPayFeeRateTranType" data-options="editable:false,required:true">
					      <option value="1">消费</option>
					      <option value="2">充值</option>
					      <option value="3">结算</option>
					      <option value="4">退款</option>
					      <option value="5">提现 </option>
					      <option value="6">转账</option>
					      <option value="13">代理</option>
					      <option value="15">代收</option>
					      <option value="16">代付</option>
					    </select>
                    </td>
                </tr>
            </table>
        <div class="merchant-lable">计费设置</div>
	    <hr color="#ccc"/>
	      <table id="payFeeRateSectionTable" name="payFeeRateSectionTable" style="margin-left:60px;" width="950px">
	        <tr>
	            <td width="50%">
	            	<input class="easyui-numberbox" type="text" name="MAX_AMT1_START" id="MAX_AMT1_START" style="width:110px" precision="2" max="99999999999"
	            	    data-options="readonly:true,required:true" value="0.0"/>&lt;金额≤
	            	<input class="easyui-numberbox" type="text" name="MAX_AMT1_END" id="MAX_AMT1_END" style="width:110px" precision="2" max="99999999999"
	            	    data-options="required:true" value="0.0"/>
	            </td>
	            <td width="10%">计费方式:</td>
                <td>
                	<select name="calMode1" id="addPayFeeRateCalMode1" data-options="editable:false,required:true" onchange="disableFeeRatioSection(this.value,1)">
				      <option value="0">定额</option>
				      <option value="1">比例</option>
				    </select>
                </td>
	            <td width="10%" align="right">费用金额</td>
	            <td width="10%">
	            	<input class="easyui-numberbox" type="text" name="FEE_AMT1" id="FEE_AMT1" style="width:150px" precision="2" max="99999999999"
	            	    data-options="required:true" value="0.0"/>
	            </td>
	            <td width="15%" align="right">费率比例(%)</td>
	            <td width="10%">
	            	<input class="easyui-numberbox" type="text" name="FEE_RATIO1" id="FEE_RATIO1" style="width:150px" precision="3" max="99.99"
	            	    data-options="required:true" value="0.0" disabled="disabled"/>
	            </td>
	        </tr>
	        </table>
	        <div align="left" style="margin-top:10px;margin-left:60px">
            <a class="easyui-linkbutton" data-options="iconCls:'icon-add'" href="javascript:addPayFeeRateRow()" style="width:80px">增加一行</a>&nbsp;&nbsp;
       	    <a class="easyui-linkbutton" data-options="iconCls:'icon-remove'" href="javascript:removePayFeeRateRow()" style="width:80px">删除一行</a>
       	   	</div>
      	</form>
    </div>
    <div data-options="region:'south',border:false" style="text-align:center;padding:5px 0 5px;">
        <a class="easyui-linkbutton" data-options="iconCls:'icon-ok'" href="javascript:addPayFeeRateFormSubmit()" style="width:80px">确定</a>
        <a class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" href="javascript:void(0)" 
            onclick="$('#addPayFeeRateForm').form('clear')" style="width:80px">清空</a>
    </div>
</div>
<script type="text/javascript">
$('#addPayFeeRateForm').form({
    url:'<%=path %>/addPayFeeRate.htm',
    onSubmit: function(){
    	if($(this).form('validate') && addPayFeeRateFormCheck())return true;
    	return false;
    },
    success:function(data){
        if(data=='<%=JWebConstant.OK %>'){
        	topCenterMessage('<%=JWebConstant.OK %>','添加成功！');
        	closeTabFreshList('payFeeRate',payFeeRateAddPageTitle,payFeeRateListPageTitle,'payFeeRateList','<%=path %>/payFeeRate.htm');
        } else topCenterMessage('<%=JWebConstant.ERROR %>',data);
    }
});
function addPayFeeRateFormCheck(){
try{
	var pfrsTable=document.getElementById("payFeeRateSectionTable");
	var feeInfo = document.getElementById("addPayFeeRateReeInfo");
	feeInfo.value="";
	for(var i=0;i<pfrsTable.rows.length;i++) {
		var calMode = $('#addPayFeeRateCalMode' + (i+1)).val();
    	var startValue = document.getElementById("MAX_AMT"+(i+1)+"_START").value;
		var endValue = document.getElementById("MAX_AMT"+(i+1)+"_END").value;
		//上一行结束值要大于起始值
		if(parseFloat(endValue) <= parseFloat(startValue)){
			topCenterMessage("<%=JWebConstant.ERROR %>","第"+(i+1)+"行终止值必须大于起始值");
			document.getElementById("MAX_AMT"+(i+1)+"_END").focus();
			return false;
		}
		feeInfo.value = feeInfo.value+parseInt(parseFloat(startValue)*100+0.5)+"-"
			 +parseInt(parseFloat(endValue)*100+0.5)+","+calMode+","+
			(calMode=="0"?parseInt(parseFloat($('#FEE_AMT'+(i+1)).val())*100+0.5):$('#FEE_RATIO'+(i+1)).val())+";";
	}
	
	return true;
	}catch(e){alert(e);}
}
function addPayFeeRateFormSubmit(){
    $('#addPayFeeRateForm').submit();
}
function addPayFeeRateRow(){
try{
	var pfrsTable=document.getElementById("payFeeRateSectionTable");
	var rowIndex = pfrsTable.rows.length;
	if(rowIndex>9){
		topCenterMessage("<%=JWebConstant.ERROR %>","超出最大行数（10行）");
		return;
	}
	var startValue = $("#MAX_AMT"+(rowIndex)+"_START").val();
	var endValue = $("#MAX_AMT"+(rowIndex)+"_END").val();
	//第一行结束值不能为0
	if(parseFloat(endValue)==0){
		topCenterMessage("<%=JWebConstant.ERROR %>","第"+(rowIndex)+"行终止值有误");
		return;
	}
	//上一行结束值要大于起始值
	if(parseFloat(endValue) <= parseFloat(startValue)){
		topCenterMessage("<%=JWebConstant.ERROR %>","第"+(rowIndex)+"行终止值必须大于起始值");
		return;
	}
	var index = pfrsTable.rows.length +1;
	var tr=document.createElement("tr");
    var td=document.createElement("td");
	td.width="25%";
    td.innerHTML = "<input class=\"easyui-numberbox\" type=\"text\" name=\"MAX_AMT"+index+"_START\" id=\"MAX_AMT"+index+
    	"_START\" style=\"width:110px\" precision=\"2\" max=\"99999999999\"data-options=\"required:true\" value=\""+endValue+"\"/>&lt;金额≤"+
    	"<input class=\"easyui-numberbox\" type=\"text\" name=\"MAX_AMT"+index+"_END\" id=\"MAX_AMT"+index+"_END\" style=\"width:110px\" precision=\"2\" max=\"99999999999\" data-options=\"required:true\" value=\"0.0\"/>";
    tr.appendChild(td);
    
    // 计费方式
    td=document.createElement("td");
    td.width="10%";
    td.appendChild(document.createTextNode("计费方式:"));
    tr.appendChild(td);
    td=document.createElement("td");
    td.innerHTML = "<select name=\"calMode"+index+"\" id=\"addPayFeeRateCalMode"+index +"\" onchange=\"disableFeeRatioSection(this.value,"+index+")\">" + 
						"<option value=\"0\">定额</option>" + 
						"<option value=\"1\">比例</option>" + 
					"</select>" ;
    tr.appendChild(td);
    
    td=document.createElement("td");
	td.width="10%";
	td.align="right";
    td.appendChild(document.createTextNode("费用金额"));
    tr.appendChild(td);
    
    td=document.createElement("td");
	td.width="20%";
    td.innerHTML = "<input class=\"easyui-numberbox\" type=\"text\" name=\"FEE_AMT"+index+"\" id=\"FEE_AMT"+index+"\" style=\"width:150px\" "+
    	"precision=\"2\" max=\"99999999999\" data-options=\"required:true\" value=\"0.0\"/>";
    tr.appendChild(td);
    td=document.createElement("td");
	td.width="10%";
	td.align="right";
    td.appendChild(document.createTextNode("费率比例(%)"));
    tr.appendChild(td);
    
    td=document.createElement("td");
	td.width="20%";
    td.innerHTML = "<input class=\"easyui-numberbox\" type=\"text\" name=\"FEE_RATIO"+index+"\" id=\"FEE_RATIO"+index+"\" style=\"width:150px\" "+
    	"precision=\"3\" max=\"99.99\" data-options=\"required:true\" value=\"0.0\" disabled=\"disabled\"/>";
    tr.appendChild(td);
    pfrsTable.appendChild (tr);
    $.parser.parse("#payFeeRateSectionTable");
}catch(e){alert(e);}
}
function removePayFeeRateRow(){
	var pfrsTable=document.getElementById("payFeeRateSectionTable");
	if(pfrsTable.rows.length == 1)return;
	pfrsTable.deleteRow(pfrsTable.rows.length-1); 
}

//费用、费率可编辑设置
function disableFeeRatioSection(value,index){
   	if(value=='0'){
   		$("#FEE_AMT"+index).numberbox("enable");
   	 	$("#FEE_RATIO"+index).numberbox("disable");
   	}
   	else{
   		$("#FEE_AMT"+index).numberbox("disable");
   	 	$("#FEE_RATIO"+index).numberbox("enable");
   	}
}
</script>
