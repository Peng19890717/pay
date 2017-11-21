<%@page import="com.pay.business.dao.PayBusinessParameterDAO"%>
<%@page import="com.pay.user.dao.PayBusinessMemberDAO"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ page import="util.JWebConstant"%>
<%@ page import="com.jweb.dao.JWebUser"%>
<%@ page import="com.jweb.dao.JWebAction"%>
<%@ page import="com.jweb.service.JWebUserService"%>
<%@ page import="com.pay.business.dao.PayBusinessParameterDAO"%>
<%@ page import="com.pay.coopbank.service.PayCoopBankService"%>
<%@ page import="com.pay.coopbank.dao.PayCoopBank"%>
<%
String path = request.getContextPath();
//登录和权限判断
JWebUser user = (JWebUser)session.getAttribute("user");
if(!JWebUserService.checkUser(user,request)){
%><script>window.location.href='<%=path %>/jsp/jweb/login.jsp'</script><%
    return;
}
JWebAction actionSearch = ((JWebAction)user.actionMap.get("pUi4LFD2Q3rKTOO2"));
JWebAction actionPayReceiveAndPaySupplement = ((JWebAction)user.actionMap.get("pXA4uR22Q3rKTOK1"));
JWebAction channelPayReceiveAndPayQuery = ((JWebAction)user.actionMap.get("pXA4uR22Q3rKTOK2"));
JWebAction settlementReceiveAndPay = ((JWebAction)user.actionMap.get("q3rjU6h2Q3rKTPn1"));
JWebAction returnReceiveAndPayAccFlag = ((JWebAction)user.actionMap.get("qbt8xFH2Q3rKTRU1"));
JWebAction payIsOpenReceiveAndPay = ((JWebAction)user.actionMap.get("J4571HGR3JLXPT2R52"));//开/关代付
String custId = request.getParameter("custId");
String type = request.getParameter("type");
String status = request.getParameter("status");
String createTimeStart = request.getParameter("createTimeStart");
String createTimeEnd = request.getParameter("createTimeEnd");
String batNo = request.getParameter("batNo");
%>
<script type="text/javascript">
$(document).ready(function(){});

function formatPayReceiveType(data,row,index) {
	if(data=="0") return "代收";
	if(data=="1") return "代付";
	return "";
}
function formatPayReceiveTranType(data,row,index) {
	if(data=="0") return "单笔";
	if(data=="1") return "批量";	
	return "";
}
function formatPayReceiveStatus(data,row,index) {
	if(data=="0") return "初始";
	if(data=="1") return "成功";
	if(data=="2") return "失败";
	return "";
}
function formatDeductMoneyFlag(data,row,index) {
	if(row.type=="0")return "-";
	if(data=="0") return "未扣除";
	if(data=="1") return "已扣除";
	return "";
}
function formatReturnMoneyFlag(data,row,index) {
	if(row.status=='1'||row.type=="0")return "-";
	if(row.deductMoneyFlag=="0")return "-";
	if(data=="0") return "未退回";
	if(data=="1") return "已退回";
	return "";
}
function formatPayReceiveBussFromType(data,row,index) {
	if(data=="0") return "代收付";
	if(data=="1") return "快捷";
	return "";
}
function format_rap_stlsts(data,row, index){
	if(data=="0"){
		return "未结算";
	} else if(data=="1"){
		return "已清算";
	} else if(data=="2"){
		return "已结算";
	} else return data;
}
</script>
	<div class="easyui-layout" data-options="fit:true">
		<div data-options="region:'center'">
			<table id="payReceiveAndPayList" style="width:200%;height:100%" rownumbers="true" pagination="true"
			    fit="true" data-options="border:false,collapsible:true,autoRowHeight:true,pageSize:20,singleSelect:true,method:'post',toolbar:'#payReceiveAndPayListToolBar'">
			       <thead>
			       	<tr>
			           <th field="id" width="8%" align="left" sortable="true">ID(包装快捷订单号)</th>
			           <th field="type" width="4%" align="left" sortable="true" data-options="formatter:formatPayReceiveType">代收付类型</th>
			           <th field="createTime" width="8%" align="left" sortable="true">创建时间</th>
			           <th field="completeTime" width="8%" align="left" sortable="true">完成时间</th>
			           <th field="channelId" width="8%" align="left" sortable="true">渠道号</th>
			           <th field="tranType" width="4%" align="left" data-options="formatter:formatPayReceiveTranType">单笔/批量</th>
			           <th field="custId" width="4%" align="left" sortable="true">客户编号</th>
			           <th field="merTranNo" width="6%" align="left" sortable="true">商户交易号</th>
			           <th field="sn" width="10%" align="left" sortable="true">明细号</th>
			           <th field="channelTranNo" width="10%" align="left">渠道交易号</th>
			           <th field="accountNo" width="10%" align="left">账户号</th>
			           <th field="bankName" width="7%" align="left">银行</th>
			           <!-- <th field="accountName" width="5%" align="left" formatter="hideNameFormatter">账户名称</th> -->
			           <th field="accountName" width="5%" align="left">账户名称</th>
			           <!-- <th field="certId" width="10%" align="left" formatter="hideIdCardFormatter">证件号</th> -->
			           <th field="certId" width="10%" align="left">证件号</th>
			           <!-- <th field="tel" width="7%" align="left" formatter="hideMobileFormatter">手机号</th> -->
			           <th field="tel" width="7%" align="left">手机号</th>
			           <th field="stlsts" width="4%" align="left" formatter="formatter:format_rap_stlsts">结算状态</th>
			           <th field="status" width="4%" align="left" data-options="formatter:formatPayReceiveStatus">处理状态</th>
			           <th field="deductMoneyFlag" width="4%" align="left" data-options="formatter:formatDeductMoneyFlag">余额扣除</th>
			           <th field="returnMoneyFlag" width="4%" align="left" data-options="formatter:formatReturnMoneyFlag">余额退回</th>
			           <th field="errorMsg" width="8%" align="left" sortable="true">描述</th>
			           <th field="amount" width="5%" align="left" sortable="true">金额</th>
			           <th field="fee" width="5%" align="left" sortable="true">商户手续费</th>
			           <th field="preOptCashAcBal" width="7%" align="left">扣除前余额</th>
			           <th field="bussFromType" width="7%" align="left" data-options="formatter:formatPayReceiveBussFromType">产生类型</th>
			           <th field="feeChannel" width="7%" align="left" sortable="true">渠道费</th>
			           <th field="operation" formatter="formatter:formatPayReceiveAndPayOperator" width="15%" align="left">操作</th>
			           <th field="receivePayNotifyUrl" width="5%" align="left" sortable="true">通知地址</th>
			           <th field="summary" width="20%" align="left" sortable="true">备注</th>
			           <th field="batNo" width="10%" align="left" sortable="true">批次号</th>
			       </tr>
			      </thead>
			</table>
		</div>
		<div id="payReceiveAndPayListToolBar" data-options="border:false" style="text-align:left;padding:5px 5px 5px 5px;">
				 ID<input type="text" id="searchPayReceiveAndPayId" name="searchPayReceiveAndPayId" class="easyui-textbox" value=""  style="width:130px"/>
				 代收付类型
				    <select id="searchPayReceiveAndPayType" name="searchPayReceiveAndPayType" data-option="editable:false" class="easyui-combobox">
				   	 	<option value="">请选择</option>
				   	 	<option value="0" <%= "0".equals(type) ? "selected":"" %>>代收</option>
				   		<option value="1" <%= "1".equals(type) ? "selected":"" %>>代付</option>
				    </select>
				    客户类型
				    <select id="searchPayReceiveAndPayCustType" name="searchPayReceiveAndPayCustType" data-option="editable:false" class="easyui-combobox" value="">
				    	<option value="">请选择</option>
				    	<option value="0">个人</option>
				   		<option value="1">商户</option>
				   		<option value="2">支付渠道</option>
				    </select>
				  处理状态
				    <select id="searchPayReceiveAndPayStatus" name="searchPayReceiveAndPayStatus" data-option="editable:false" class="easyui-combobox" value="">
				    	<option value="">请选择</option>
				    	<option value="0" <%= "0".equals(status) ? "selected":"" %>>初始</option>
				   		<option value="1" <%= "1".equals(status) ? "selected":"" %>>成功</option>
				   		<option value="2" <%= "2".equals(status) ? "selected":"" %>>失败</option>
				    </select>
				  代收付产生类型
				    <select id="searchPayReceiveAndPayBussFromType" name="searchPayReceiveAndPayBussFromType" data-option="editable:false" class="easyui-combobox">
				    	<option value="">请选择</option>
				    	<option value="0">代收付</option>
				   		<option value="1">快捷</option>
				    </select>
				    余额扣除
				    <select id="searchPayReceiveAndPayDeductMoneyFlag" name="searchPayReceiveAndPayDeductMoneyFlag" data-option="editable:false" class="easyui-combobox">
				    	<option value="">请选择</option>
				    	<option value="0">未扣除</option>
				   		<option value="1">已扣除</option>
				    </select>
				    余额退回
				    <select id="searchPayReceiveAndPayReturnMoneyFlag" name="searchPayReceiveAndPayReturnMoneyFlag" data-option="editable:false" class="easyui-combobox">
				    	<option value="">请选择</option>
				    	<option value="0">未退回</option>
				   		<option value="1">已退回</option>
				    </select>
				    客户编号<input type="text" id="searchPayReceiveAndPayCustId" name="searchPayReceiveAndPayCustId" class="easyui-textbox" value="<%=custId==null?"":custId %>"  style="width:130px"/>
				    商户交易号<input type="text" id="searchPayReceiveAndPayMerTranNo" name="searchPayReceiveAndPayMerTranNo" class="easyui-textbox" value=""  style="width:130px"/>
				    明细号<input type="text" id="searchPayReceiveAndPaySn" name="searchPayReceiveAndPaySn" class="easyui-textbox" value=""  style="width:130px"/>  
				    账户号<input type="text" id="searchPayReceiveAndPayAccountNo" name="searchPayReceiveAndPayAccountNo" class="easyui-textbox" value=""  style="width:130px"/>
				    账户名称<input type="text" id="searchPayReceiveAndPayAccountName" name="searchPayReceiveAndPayAccountName" class="easyui-textbox" value=""  style="width:130px"/>
				    证件号<input type="text" id="searchPayReceiveAndPayCertId" name="searchPayReceiveAndPayCertId" class="easyui-textbox" value=""  style="width:130px"/>
				    手机号<input type="text" id="searchPayReceiveAndPayTel" name="searchPayReceiveAndPayTel" class="easyui-textbox" value=""  style="width:130px"/>
				    批次号<input type="text" id="searchPayReceiveAndPayBatNo" name="searchPayReceiveAndPayBatNo" class="easyui-textbox" value="<%=batNo==null?"":batNo %>"  style="width:130px"/>
				    创建日期<input type="text" id="searchPayReceiveAndPayCreateTimeStart" name="searchPayReceiveAndPayCreateTimeStart"
						 data-options="editable:false" class="easyui-datebox" style="width:100px" value="<%=createTimeStart==null?"":createTimeStart %>"/>
				    ~<input type="text" id="searchPayReceiveAndPayCreateTimeEnd" name="searchPayReceiveAndPayCreateTimeEnd"
						 data-options="editable:false" class="easyui-datebox" style="width:100px" value="<%=createTimeEnd==null?"":createTimeEnd %>"/>
				  支付渠道<select class="easyui-combobox" id="searchPayOrderPayChannelForPRP" name="searchPayOrderPayChannelForPRP" validType="inputExistInCombobox['searchPayOrderPayChannelForPRP']">
	    	    			<option value="">请选择</option>
				        <%	
		           			java.util.Iterator<String> it = PayCoopBankService.CHANNEL_MAP_ALL.keySet().iterator();
		           			while(it.hasNext()){
		           				String key = it.next();
		           				String value = ((PayCoopBank)PayCoopBankService.CHANNEL_MAP_ALL.get(key)).getBankName();
		           				%><option value="<%=key%>"><%=value%></option><%
		           				} %>
			  		</select>
			  
    <%if(actionSearch != null){//角色判断%>
    <a href="javascript:searchPayReceiveAndPayList()" class="easyui-linkbutton" iconCls="icon-search"><%=actionSearch.name %></a>
    <%} %>
    <%if(payIsOpenReceiveAndPay != null){//开启或关闭代付%>
    		<a href="javascript:payOpenOrNotReceiveAndPay()" id="isOpenPayButton"
    			class="easyui-linkbutton" iconCls="icon-config"><%= "0".equals(com.PayConstant.PAY_CONFIG.get("PAY_IS_OPEN"))?"关闭代付":"开启代付" %></a>
    <%}%>
</div>
		<div data-options="region:'south',border:false" style="text-align:left;margin-left:7px;margin-top:5px;margin-bottom:5px;">
			<%if(actionSearch != null){//角色判断%>
		   		<a href="javascript:payReceiveAndPayExportExcel()"  class="easyui-linkbutton" iconCls="icon-config">excel导出</a>
			<%} %>
			<span id="getTotalMoneyIdForReceiveAndPay">&nbsp;</span>
		</div>
	</div>
<script type="text/javascript">
function payReceiveAndPayExportExcel(){
	$.messager.confirm('提示', '您确认将当前记录导出至excel吗？', function(r){
        if(!r)return;
	    try{
	    	window.location.href = "<%=path %>/payReceiveAndPayExportExcel.htm?"+
			"id="+$('#searchPayReceiveAndPayId').val()+
	        "&type="+$('#searchPayReceiveAndPayType').datebox('getValue')+
	        "&custType="+$('#searchPayReceiveAndPayCustType').datebox('getValue')+
	        "&custId="+$('#searchPayReceiveAndPayCustId').val()+
	        "&merTranNo="+$('#searchPayReceiveAndPayMerTranNo').val()+
	        "&sn="+$('#searchPayReceiveAndPaySn').val()+
	        "&accountNo="+$('#searchPayReceiveAndPayAccountNo').val()+
	        "&accountName="+$('#searchPayReceiveAndPayAccountName').val()+
	        "&certId="+$('#searchPayReceiveAndPayCertId').val()+
	        "&tel="+$('#searchPayReceiveAndPayTel').val()+
	        "&status="+$('#searchPayReceiveAndPayStatus').datebox('getValue')+
	        "&bussFromType="+$('#searchPayReceiveAndPayBussFromType').datebox('getValue')+
	        "&batNo="+$('#searchPayReceiveAndPayBatNo').val()+
	        "&createTimeStart="+$('#searchPayReceiveAndPayCreateTimeStart').datebox('getValue')+
	        "&createTimeEnd="+$('#searchPayReceiveAndPayCreateTimeEnd').datebox('getValue')+
	        "&deductMoneyFlag="+$('#searchPayReceiveAndPayDeductMoneyFlag').datebox('getValue')+
	        "&returnMoneyFlag="+$('#searchPayReceiveAndPayReturnMoneyFlag').datebox('getValue')+
	        "&channelId="+$('#searchPayOrderPayChannelForPRP').combobox('getValue');
	    }catch(e){alert(e);}
    });
}
function formatPayReceiveAndPayOperator(data,row,index){
     var tmp=
         //商户补单
         <%if(actionPayReceiveAndPaySupplement != null){//角色判断%>
         ("<a href=\"javascript:payReceiveAndPaySupplementWindowOpen('"+row.id+"')\">&nbsp;&nbsp;<%=actionPayReceiveAndPaySupplement.name %></a>")+
         <%}%>
         //渠道补单
         <%if(channelPayReceiveAndPayQuery != null){//角色判断%>
         ("<a href=\"javascript:channelPayReceiveAndPayQuery('"+row.id+"')\">&nbsp;&nbsp;<%=channelPayReceiveAndPayQuery.name %></a>")+
         <%}%>
         //结算
         <%if(settlementReceiveAndPay != null){//角色判断%>
         ( row.type == '0' && row.bussFromType == '0' && (row.stlsts != '2'||row.stlsts==undefined) && row.status=='1' ?"<a href=\"javascript:settlementReceiveAndPay('"+row.id+"')\">&nbsp;&nbsp;<%=settlementReceiveAndPay.name %></a>" : "")+
         <%}%>
          //退回设置
         <%if(returnReceiveAndPayAccFlag != null){//角色判断%>
         (row.status!='0' && row.status!='1' && row.deductMoneyFlag == '1' && row.returnMoneyFlag == '0' ? "<a href=\"javascript:returnReceiveAndPayAccFlag('"+row.id+"')\">&nbsp;&nbsp;<%=returnReceiveAndPayAccFlag.name %></a>" : "")+
         <%}%>
         "";
     return tmp;
}
//商户补单
function payReceiveAndPaySupplementWindowOpen(id){
    $.messager.confirm('提示', '确认要进行商户补单?', function(r){
    if(!r)return;
    $('#payReceiveAndPayList').datagrid('loading');
    try{
        $.post('<%=path %>/notifyReceiveAndPayMer.htm',
            {id:id},
            function(data){
                $('#payReceiveAndPayList').datagrid('reload');
                if(data=='<%=JWebConstant.OK %>'){
                    topCenterMessage('<%=JWebConstant.OK %>','商户补单成功！');
                } else topCenterMessage('<%=JWebConstant.ERROR %>',data);
            }
        );
    }catch(e){alert(e);}
   });
}
//渠道补单
function channelPayReceiveAndPayQuery(id){
    $.messager.confirm('提示', '确认要进行渠道补单?', function(r){
    if(!r)return;
    $('#payReceiveAndPayList').datagrid('loading');
    try{
        $.post('<%=path %>/channelReceiveAndPayQuery.htm',
            {id:id},
            function(data){
                $('#payReceiveAndPayList').datagrid('reload');
                if(data=='<%=JWebConstant.OK %>'){
                    topCenterMessage('<%=JWebConstant.OK %>','渠道补单成功！');
                } else topCenterMessage('<%=JWebConstant.ERROR %>',data);
            }
        );
    }catch(e){alert(e);}
   });
}
//结算
function settlementReceiveAndPay(id){
    $.messager.confirm('提示', '结算状态修改确认（不进行真实结算）', function(r){
    if(!r)return;
    $('#payReceiveAndPayList').datagrid('loading');
    try{
        $.post('<%=path %>/settlementReceiveAndPay.htm',
            {id:id},
            function(data){
                $('#payReceiveAndPayList').datagrid('reload');
                if(data=='<%=JWebConstant.OK %>'){
                    topCenterMessage('<%=JWebConstant.OK %>','结算成功！');
                } else topCenterMessage('<%=JWebConstant.ERROR %>',data);
            }
        );
    }catch(e){alert(e);}
   });
}
function returnReceiveAndPayAccFlag(id){
    $.messager.confirm('提示', '代付失败，扣款金额退回标识设置确认', function(r){
    if(!r)return;
    $('#payReceiveAndPayList').datagrid('loading');
    try{
        $.post('<%=path %>/returnReceiveAndPayAcc.htm',
            {id:id},
            function(data){
                $('#payReceiveAndPayList').datagrid('reload');
                if(data=='<%=JWebConstant.OK %>'){
                    topCenterMessage('<%=JWebConstant.OK %>','设置成功！');
                } else topCenterMessage('<%=JWebConstant.ERROR %>',data);
            }
        );
    }catch(e){alert(e);}
   });
}
var isOpenPay="<%=com.PayConstant.PAY_CONFIG.get("PAY_IS_OPEN") %>";
//开启/关闭代付
function payOpenOrNotReceiveAndPay(){	
    $.messager.confirm('提示', '确认要'+ (isOpenPay == '0'?'关闭':'开启') +'代付吗', function(r){
    if(!r)return;
    try{
        $.post('<%=path %>/payOpenOrNotReceiveAndPay.htm',
            {paraValue:isOpenPay},
            function(data){
                if(data=='<%=JWebConstant.OK %>'){
                	if(isOpenPay == '0'){
                		isOpenPay='1';
                		//设置按钮名称
                		$('#isOpenPayButton').linkbutton({text:'开启代付'});
                	} else {
                		isOpenPay='0';
                		//设置按钮名称
                		$('#isOpenPayButton').linkbutton({text:'关闭代付'});
                	}
                    topCenterMessage('<%=JWebConstant.OK %>',(isOpenPay == '0'?'开启':'关闭')+'成功！');
                } else topCenterMessage('<%=JWebConstant.ERROR %>',data);
            }
        );
    }catch(e){alert(e);}
   });
}
$('#payReceiveAndPayList').datagrid({
	onBeforeLoad: function(param){
		param.flag='0';
		<%if(batNo!=null){%>
        param.batNo=$('#searchPayReceiveAndPayBatNo').val();
        <%}%>
		<%if(custId!=null){%>
        param.custId = $('#searchPayReceiveAndPayCustId').val();
        <%}%>
		<%if(type!=null){%>
        param.type = $('#searchPayReceiveAndPayType').val();
        <%}%>
        <%if(status!=null){%>
        param.status = $('#searchPayReceiveAndPayStatus').val();
        <%}%>
        <%if(createTimeStart!=null){%>
        try{param.createTimeStart=$('#searchPayReceiveAndPayCreateTimeStart').datebox('getValue');}catch(e){
        	param.createTimeStart=document.getElementById("searchPayReceiveAndPayCreateTimeStart").value;}
        <%}%>
        <%if(createTimeEnd!=null){%>
        try{param.createTimeEnd=$('#searchPayReceiveAndPayCreateTimeEnd').datebox('getValue');}catch(e){
        	param.createTimeEnd=document.getElementById("searchPayReceiveAndPayCreateTimeEnd").value;}
        <%}%>
    },
    onLoadError: function(data){
        topCenterMessage('<%=JWebConstant.ERROR %>','数据加载失败！');
    },
    onLoadSuccess: function(data){
        $(this).datagrid('fixRownumber');//行号自适应宽度
        $("#getTotalMoneyIdForReceiveAndPay").html("总金额："+(parseFloat(data.totalReceiveAndPayMoney)*0.01).toFixed(2)
        	+"元，商户手续费总额："+(parseFloat(data.totalReceiveAndPayFeeMoney)*0.01).toFixed(2)+"元");
    }
});
function searchPayReceiveAndPayList(){
    try{
		$('#payReceiveAndPayList').datagrid({  
	    	url:'<%=path %>/payReceiveAndPay.htm?flag=0',  
	    	queryParams:{  
	      	  id:$('#searchPayReceiveAndPayId').val(),
	          type:$('#searchPayReceiveAndPayType').datebox('getValue'),
	          custType:$('#searchPayReceiveAndPayCustType').datebox('getValue'),
	          custId:$('#searchPayReceiveAndPayCustId').val(),
	          merTranNo:$('#searchPayReceiveAndPayMerTranNo').val(),
	          sn:$('#searchPayReceiveAndPaySn').val(),
	          accountNo:$('#searchPayReceiveAndPayAccountNo').val(),
	          accountName:$('#searchPayReceiveAndPayAccountName').val(),
	          certId:$('#searchPayReceiveAndPayCertId').val(),
	          tel:$('#searchPayReceiveAndPayTel').val(),
	          status:$('#searchPayReceiveAndPayStatus').datebox('getValue'),
	          bussFromType:$('#searchPayReceiveAndPayBussFromType').datebox('getValue'),
	          batNo:$('#searchPayReceiveAndPayBatNo').val(),
	          createTimeStart:$('#searchPayReceiveAndPayCreateTimeStart').datebox('getValue'),
	          createTimeEnd:$('#searchPayReceiveAndPayCreateTimeEnd').datebox('getValue'),
	          deductMoneyFlag:$('#searchPayReceiveAndPayDeductMoneyFlag').datebox('getValue'),
	          returnMoneyFlag:$('#searchPayReceiveAndPayReturnMoneyFlag').datebox('getValue'),
	          channelId:$('#searchPayOrderPayChannelForPRP').combobox('getValue')
	    	}  
		}); 
    }catch(e){alert(e);} 
}
</script>
