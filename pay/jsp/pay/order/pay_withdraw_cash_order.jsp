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
JWebAction actionSearch = ((JWebAction)user.actionMap.get("pFv6cD42Q3rKTOI5"));
JWebAction actionDetail = ((JWebAction)user.actionMap.get("pFv6cD42Q3rKTOI4"));

/* JWebAction actionWithdrawForSucc = ((JWebAction)user.actionMap.get("pFv7mCC2Q3rKTPI1"));
JWebAction actionWithdrawForFail = ((JWebAction)user.actionMap.get("pFv7mCC2Q3rKTPI2")); */
JWebAction actionWithdrawForBack = ((JWebAction)user.actionMap.get("pFv7mCC2Q3rKTPI3"));
JWebAction actionWithdrawForReset = ((JWebAction)user.actionMap.get("pFv7mCC2Q3rKTPI4"));
JWebAction actionSetWithdrawResult = ((JWebAction)user.actionMap.get("pIZJ2in2Q3rKTOM1"));
%>
<script type="text/javascript">
var payWithdrawCashOrderListPageTitle="提现订单列表";
var payWithdrawCashOrderDetailPageTitle="提现订单详情";
$(document).ready(function(){});

// 订单状态
function format_ordstatus(data,row, index){
	if(data=="00"){
		return "未处理";
	}else if(data=="01"){
		return "提现处理中";
	}else if(data=="02"){
		return "提现成功";
	}else if(data=="03"){
		return "提现失败";
	}else if(data=="04"){
		return "已退回支付账号";
	}else if(data=="99"){
		return "超时";
	}
}

// 线上/线下状态 
function format_withdrawWay(data,row, index){
	if(data=="0"){
		return "线上自动到账";
	}else if(data=="1"){
		return "线下打款";
	}
}

// excel导出
function payWithdrawCashOrderListExportExcel(){
	$.messager.confirm('提示', '您确认将当前记录导出至excel吗？', function(r){
        if(!r)return;
	    try{
	    	var tmp = "<%=path %>/payWithdrawCashOrderListExportExcel.htm?"+
			"casordno="+$('#searchPayWithdrawCashOrderCasordno').val()+
	        "&ordstatus="+$('#searchPayWithdrawCashOrderOrdstatus').datebox('getValue')+
	        "&actTime="+$('#searchPayWithdrawCashOrderActTime').datebox('getValue')+
	        "&actTimeEnd="+$('#searchPayWithdrawCashOrderActTimeEnd').datebox('getValue')+
	        "&sucTime="+$('#searchPayWithdrawCashOrderSucTime').datebox('getValue')+
			"&custId="+$('#searchPayWithdrawCashOrderCustId').val()+
			"&bankpayacno="+$('#searchPayWithdrawCashOrderBankpayacno').val()+
			"&bankpayusernm="+$('#searchPayWithdrawCashOrderBankpayusernm').val();
	        window.location.href= tmp;
	    }catch(e){alert(e);}
    });
}
</script>
<div class="easyui-layout" data-options="fit:true">
	 <div data-options="region:'center'">
		<table id="payWithdrawCashOrderList" style="width:100%;height:100%" rownumbers="true" pagination="true"
		    fit="true" data-options="border:false,collapsible:true,autoRowHeight:true,pageSize:20,singleSelect:true,url:'<%=path 
		        %>/payWithdrawCashOrder.htm?flag=0',method:'post',toolbar:'#payWithdrawCashOrderListToolBar'">
		       <thead>
		        <tr>
		           <th field="casordno" width="6%" align="left" sortable="true">提现订单号</th>
		           <th field="actTime" width="6%" align="left" sortable="true">申请提现时间</th>
		           <th field="txamt" width="6%" align="left" sortable="true">提现金额</th>
		           <th field="fee" width="3%" align="left" sortable="true">手续费</th>
		           <th field="ordstatus" width="6%" align="left" sortable="true" formatter="format_ordstatus">订单状态</th>
		           <th field="bankName" width="6%" align="left" sortable="true">银行名称</th>
		           <th field="bankpayacno" width="6%" align="left" sortable="true" formatter="hideBankCardNoFormatter">收款结算帐号</th>
		           <th field="bankpayusernm" width="4%" align="left" sortable="true">收款人</th>
		           <th field="custId" width="4%" align="left" sortable="true">客户编号</th>
		           <th field="bankjrnno" width="7%" align="left" sortable="true">渠道流水号</th>
		           <th field="sucTime" width="7%" align="left" sortable="true">完成时间</th>
		           <th field="withdrawChannel" width="7%" align="left" sortable="true">提现渠道</th>
		           <th field="bankerror" width="11%" align="left" sortable="true">备注</th>
		           <th field="operation" data-options="formatter:formatPayWithdrawCashOrderOperator" width="12%" align="left">操作</th>
		           <th field="updateUser" width="4%" align="left" sortable="true">操作员</th>
		           <th field="withdrawWay" width="4%" align="left" sortable="true" formatter="format_withdrawWay">线上/线下</th>
		       </tr>
		       </thead>
		</table>
	</div>
	
	<div id="setPayWithdrawCashOrderResultWindow" class="easyui-window" title="提现结果设置" 
	    	data-options="iconCls:'icon-edit',closed:true,modal:true" style="width:450px;height:250px;padding:5px;">
	    <div class="easyui-layout" data-options="fit:true">
	        <div data-options="region:'center'">
	            <form id="setResultPayWithdrawCashOrderForm" method="post">
	            	<input type="hidden" id="setResultPayWithdrawCashOrderCasordnoId" name="casordno">
	                <table cellpadding="5">
	                    <tr>
	                    	<td>银行流水号：</td>
	                    	<td><input class="easyui-textbox" type="text" name="bankjrnno" data-options="required:true" id="setResultPayWithdrawCashOrderBankjrnno"></td>
	                    </tr>
	                    <tr><td align="right">提现结果：</td><td>
	                    	<input type="radio" name="ordstatus" value="02" onclick="setResultPayWithdrawCashOrderRequired('0')" checked>提现成功
	                  		<input type="radio" name="ordstatus" value="03" onclick="setResultPayWithdrawCashOrderRequired('1')">提现失败</td></tr>
	                    <tr><td align="right">&nbsp;</td><td><input class="easyui-textbox" type="text" name="bankerror" id="setResultPayWithdrawCashOrderRemark"
					              validType="length[0,300]" invalidMessage="内容请控制在300个字" data-options="multiline:true"
					              style="width:200px;height:70px"/></td></tr>
	                </table>
	            </form>
	        </div>
	        <div data-options="region:'south',border:false" style="text-align:right;padding:5px 0 0;">
	            <a class="easyui-linkbutton" data-options="iconCls:'icon-ok'" href="javascript:void(0)" 
	            	onclick="$('#setResultPayWithdrawCashOrderForm').submit()" style="width:80px">确定</a>
	            <a class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" href="javascript:void(0)" 
	                onclick="$('#setPayWithdrawCashOrderResultWindow').window('close')" style="width:80px">取消</a>
	        </div>
	    </div>
	</div>
	
	
<div id="payWithdrawCashOrderListToolBar" data-options="border:false" style="text-align:left;padding:5px 5px 5px 5px;">
    提现订单号：
    	<input type="text" id="searchPayWithdrawCashOrderCasordno" name="searchPayWithdrawCashOrderCasordno" class="easyui-textbox" value=""  style="width:130px"/>
    订单状态：
    	<select class="easyui-combobox" id="searchPayWithdrawCashOrderOrdstatus" data-options="editable:false" name="searchPayWithdrawCashOrderOrdstatus"  style="width:130px">
   		   <option value="">请选择</option>
           <option value="00">未处理</option>
           <option value="01">提现处理中</option>
           <option value="02">提现成功</option>
           <option value="03">提现失败</option>
           <option value="04">已退回支付帐号</option>
           <option value="99">超时</option>
        </select>
    提现订单日期：
    	<input type="text" id="searchPayWithdrawCashOrderActTime" name="searchPayWithdrawCashOrderActTime" data-options="editable:false" class="easyui-datebox" value=""  style="width:100px"/>
    	~
    	<input type="text" id="searchPayWithdrawCashOrderActTimeEnd" name="searchPayWithdrawCashOrderActTimeEnd" data-options="editable:false" class="easyui-datebox" value=""  style="width:100px"/>
   提现完成日期：
    	<input type="text" id="searchPayWithdrawCashOrderSucTime" name="searchPayWithdrawCashOrderSucTime" data-options="editable:false" class="easyui-datebox" value=""  style="width:100px"/>
    用户编号：
    	<input type="text" id="searchPayWithdrawCashOrderCustId" name="searchPayWithdrawCashOrderCustId" class="easyui-textbox" value=""  style="width:130px"/>
    收款结算帐号：
    	<input type="text" id="searchPayWithdrawCashOrderBankpayacno" name="searchPayWithdrawCashOrderBankpayacno" class="easyui-textbox" value=""  style="width:130px"/>
    收款人姓名：
    	<input type="text" id="searchPayWithdrawCashOrderBankpayusernm" name="searchPayWithdrawCashOrderBankpayusernm" class="easyui-textbox" value=""  style="width:130px"/>
    	
    <%if(actionSearch != null){//角色判断%>
    <a href="javascript:searchPayWithdrawCashOrderList()" class="easyui-linkbutton" iconCls="icon-search"><%=actionSearch.name %></a>
    <%} %>
</div>
	<div data-options="region:'south',border:false" style="text-align:left;margin-left:7px;margin-top:5px;margin-bottom:5px;">
		<%if(actionSearch != null){//角色判断%>
		<a href="javascript:payWithdrawCashOrderListExportExcel()"  class="easyui-linkbutton" iconCls="icon-config">excel导出</a>
		<%} %>
		&nbsp;&nbsp;&nbsp;&nbsp;
		<span id="getpayWithdrawCashOrderTotalId"></span>
	</div>
</div>

<div id="payWithdrawCashOrderOpenWindow" class="easyui-window" title="提现失败" 
    data-options="iconCls:'icon-add',closed:true,modal:true" style="width:400px;height:230px;padding:5px;">
    <div class="easyui-layout" data-options="fit:true">
        <div data-options="region:'center'">
            <form id="payWithdrawCashOrderFormOperationStatus" method="post">
            	<input type="hidden" name="url" id="urlIdForFial">
            	<input type="hidden" name="flag" id="flagIdForPayWithdrawCash">
            	<input type="hidden" name="casordno" id="casordnoId">
            	<input type="hidden" name="columName" id="columNameId">
            	<input type="hidden" name="value" id="valueId">
                <table cellpadding="5">
                    <tr><td width="25%">&nbsp;</td><td width="75%">&nbsp;</td></tr>
                  <tr>
                      <td align="right" valign="top">失败原因：</td>
                      <td>
                     		<input class="easyui-textbox" type="text" id="payWithdrawCashOrderRemark" name="remark" data-options="required:true,multiline:true"
                 			validType="length[0,200]" invalidMessage="失败原因请控制在200个字符以内" style="width:200px;height:70px"/>
                      </td>
                  </tr>
                </table>
            </form>
        </div>
        <div data-options="region:'south',border:false" style="text-align:right;padding:5px 0 0;">
            <a class="easyui-linkbutton" data-options="iconCls:'icon-ok'" href="javascript:operationStatusForm()" style="width:80px">保存</a>
            <a class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" href="javascript:void(0)" 
                onclick="$('#payWithdrawCashOrderOpenWindow').window('close')" style="width:80px">取消</a>
        </div>
    </div>
</div>

<script type="text/javascript">
$('#payWithdrawCashOrderList').datagrid({
    onLoadError: function(data){
        topCenterMessage('<%=JWebConstant.ERROR %>','数据加载失败！');
    },
    onLoadSuccess: function(data){
        $(this).datagrid('fixRownumber');//行号自适应宽度
        $("#getpayWithdrawCashOrderTotalId").html("汇总笔数  ：" + data.countPayWithdrawCashOrder + " 笔&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;汇总金额 ：  " + (parseFloat(data.totalPayWithdrawCashOrder)*0.01).toFixed(2) + " 元&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;手续费总额：  "+
        		(parseFloat(data.totalPayWithdrawFee)*0.01).toFixed(2));
    }
});
function formatPayWithdrawCashOrderOperator(val,row,index){
     var tmp=
        <%if(actionDetail != null){//角色判断%>
        "<a href=\"javascript:detailPayWithdrawCashOrderPageOpen('"+row.casordno+"')\"><%=actionDetail.name %></a>&nbsp;&nbsp;"+
        <%}%>        
        // 提现成功
        <%-- <%if(actionWithdrawForSucc != null){//角色判断%>
	        ('02' == row.ordstatus ? "" : ('00' == row.ordstatus ? "<a href=\"javascript:setStatusPayWithdrawCashOrderPageOpen('payWithdrawCashSuc.htm','"+row.casordno+"','ORDSTATUS','02','您确定要对（ "+row.casordno+" ）订单进行提现成功操作吗？')\"><%=actionWithdrawForSucc.name %></a>&nbsp;&nbsp;" : ('99' == row.ordstatus ? "<a href=\"javascript:setStatusPayWithdrawCashOrderPageOpen('payWithdrawCashSucForTimeout.htm','"+row.casordno+"','ORDSTATUS','02','您确定要对（ "+row.casordno+" ）订单进行提现成功操作吗（超时）？')\"><%=actionWithdrawForSucc.name %></a>&nbsp;&nbsp;" : ""))) + 
        <%}%>
        // 提现失败
        <%if(actionWithdrawForFail != null){//角色判断%>
        	('02' == row.ordstatus ? "" : ('00' == row.ordstatus ? "<a href=\"javascript:operationStatusForFail('payWithdrawCashFail.htm','"+row.casordno+"','ORDSTATUS','03','您确定要对（ "+row.casordno+" ）订单进行提现失败操作吗？','')\"><%=actionWithdrawForFail.name %></a>&nbsp;&nbsp;" : ('99' == row.ordstatus ? "<a href=\"javascript:operationStatusForFail('payWithdrawCashFail.htm','"+row.casordno+"','ORDSTATUS','03','您确定要对（ "+row.casordno+" ）订单进行提现失败操作吗（超时）？','flag')\"><%=actionWithdrawForFail.name %></a>&nbsp;&nbsp;" : ""))) + 
        <%}%> --%>
        // 退回账户
        <%if(actionWithdrawForBack != null){//角色判断%>
        	('02' == row.ordstatus ? "" : ('03' == row.ordstatus ? "<a href=\"javascript:setStatusPayWithdrawCashOrderPageOpen('payWithdrawCashBack.htm','"+row.casordno+"','ORDSTATUS','"+row.custId+"','您确定要对（ "+row.casordno+" ）订单进行退回操作吗？')\"><%=actionWithdrawForBack.name %></a>&nbsp;&nbsp;" : "")) + 
        <%}%>
        // 重新提现
        <%if(actionWithdrawForReset != null){//角色判断%>
	        ('02' == row.ordstatus ? "" : ('03' == row.ordstatus ? "<a href=\"javascript:setStatusPayWithdrawCashOrderPageOpen('payWithdrawCashReset.htm','"+row.casordno+"','ORDSTATUS','"+row.withdrawWay+"','您确定要对（ "+row.casordno+" ）订单进行重新提现操作吗？')\"><%=actionWithdrawForReset.name %></a>&nbsp;&nbsp;" : "")) + 
        <%}%>
        //提现结果
        <%if(actionSetWithdrawResult != null){%>
        (row.ordstatus=="00" || row.ordstatus=="01" ?"<a href=\"javascript:setPayWithdrawCashOrderResultWindowOpen('"+row.casordno+"')\"><%=actionSetWithdrawResult.name %></a>":"")+
        <%}%>
        "";
     return tmp;
}
function searchPayWithdrawCashOrderList(){
    $('#payWithdrawCashOrderList').datagrid('load',{
          casordno:$('#searchPayWithdrawCashOrderCasordno').val(),
          ordstatus:$('#searchPayWithdrawCashOrderOrdstatus').datebox('getValue'),
          actTime:$('#searchPayWithdrawCashOrderActTime').datebox('getValue'),
          actTimeEnd:$('#searchPayWithdrawCashOrderActTimeEnd').datebox('getValue'),
          sucTime:$('#searchPayWithdrawCashOrderSucTime').datebox('getValue'),
          custId:$('#searchPayWithdrawCashOrderCustId').val(),
          bankpayacno:$('#searchPayWithdrawCashOrderBankpayacno').val(),
          bankpayusernm:$('#searchPayWithdrawCashOrderBankpayusernm').val()
    });  
}
function detailPayWithdrawCashOrderPageOpen(casordno){
    openTab('detailPayWithdrawCashOrderPage',payWithdrawCashOrderDetailPageTitle,'<%=path %>/detailPayWithdrawCashOrder.htm?casordno='+casordno);
}

// 更新状态
function setStatusPayWithdrawCashOrderPageOpen(url,casordno,columName,value,info){
    $.messager.confirm('提示', info, function(r){
    if(!r)return;
    $('#testUserList').datagrid('loading');
    try{
        $.post('<%=path %>/'+url,
            {casordno:casordno,columName:columName,value:value},
            function(data){
                $('#payWithdrawCashOrderList').datagrid('reload');
                if(data=='<%=JWebConstant.OK %>'){
                    topCenterMessage('<%=JWebConstant.OK %>','操作成功!');
                } else topCenterMessage('<%=JWebConstant.ERROR %>',data);
            },
           'text'
        );
    }catch(e){alert(e);}
   });
}

function operationStatusForFail(url,casordno,columName,value,info,flag){
	$('#payWithdrawCashOrderFormOperationStatus').form('clear');
	$('#urlIdForFial').val(url); 
	$('#casordnoId').val(casordno); 
	$('#columNameId').val(columName); 
	$('#valueId').val(value); 
	$('#flagIdForPayWithdrawCash').val(flag); 
	$('#payWithdrawCashOrderOpenWindow').window('open');
}

function operationStatusForm(){
	var formSubmit = $('#payWithdrawCashOrderFormOperationStatus');
	formSubmit.submit();
}
$('#payWithdrawCashOrderFormOperationStatus').form({
    url:'<%=path%>/payWithdrawCashFail.htm',
    onSubmit: function(){
    	var f = $(this).form('validate');
    	if(f) $('#payWithdrawCashOrderOpenWindow').window('close');
       	return f;
    },
    success:function(data){
    	$('#payWithdrawCashOrderList').datagrid('reload');
        if(data=='<%=JWebConstant.OK %>'){
           topCenterMessage('<%=JWebConstant.OK %>','提交成功！');
        } else topCenterMessage('<%=JWebConstant.ERROR %>',data);
    }
});
//设置提现结果
$('#setResultPayWithdrawCashOrderForm').form({
    url:'<%=path %>/setPayWithdrawCashResult.htm',
    onSubmit: function(){
    	var f = $(this).form('validate');
    	if(f)$('#setPayWithdrawCashOrderResultWindow').window('close');
       	return f;
    },
    success:function(data){
    	$('#setResultPayWithdrawCashOrderForm').form('clear');
    	$('#payWithdrawCashOrderList').datagrid('reload');
        if(data=='<%=JWebConstant.OK %>'){
           topCenterMessage('<%=JWebConstant.OK %>','设置成功！');
        } else topCenterMessage('<%=JWebConstant.ERROR %>',data);
    }
});
function setPayWithdrawCashOrderResultWindowOpen(casordno){
	document.getElementById('setResultPayWithdrawCashOrderCasordnoId').value=casordno;
    $('#setPayWithdrawCashOrderResultWindow').window('open');
}
function setResultPayWithdrawCashOrderRequired(flag){
	//提现成功
	if(flag == '0'){
		$('#setResultPayWithdrawCashOrderRemark').textbox({
			required:false
	    });
		$('#setResultPayWithdrawCashOrderBankjrnno').textbox({
			required:true
	    });
	} else {
		//提现失败
	 	$('#setResultPayWithdrawCashOrderRemark').textbox({
			required:true
	    });
	 	$('#setResultPayWithdrawCashOrderBankjrnno').textbox({
			required:false
	    });
	    $('#setResultPayWithdrawCashOrderRemark').textbox('textbox').focus();
	}
}
</script>
