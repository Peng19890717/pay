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
JWebAction actionSearch = ((JWebAction)user.actionMap.get("pG41ZP52Q3rKTPC2"));
JWebAction actionAdd = ((JWebAction)user.actionMap.get("pG41ZP52Q3rKTPC3"));
JWebAction actionDetail = ((JWebAction)user.actionMap.get("ACTION_DETAIL_ID"));
JWebAction actionRemove = ((JWebAction)user.actionMap.get("qeGhluE2Q3rKTPD1"));
JWebAction actionUpdate = ((JWebAction)user.actionMap.get("pG8U88q2Q3rKTON1"));
%>
<script type="text/javascript">
var payAdjustAccountCashListPageTitle="调账管理";
var payAdjustAccountCashAddPageTitle="调账申请";
var payAdjustAccountCashDetailPageTitle="payAdjustAccountCash详情";
var payAdjustAccountCashUpdatePageTitle="修改payAdjustAccountCash";
$(document).ready(function(){});

//格式化客户类型
function formatPayAdjustAccountCashAcType(data,row,index) {
	if(data=="<%=com.PayConstant.CUST_TYPE_USER %>") return "个人账户";
	if(data=="<%=com.PayConstant.CUST_TYPE_MERCHANT %>") return "商户账户";
	if(data=="<%=com.PayConstant.CUST_TYPE_CHARGE %>") return "商户手续费";
	return "";
}
//格式化审核状态
function formatPayAdjustAccountCashStatus(data,row,index) {
	if(data=="0") return "待审核";
	if(data=="1") return "审核通过";
	if(data=="2") return "审核失败";
	return "";
}
//调账类型
function formatPayAdjustAccountCashAdjustType(data,row,index) {
	if(data=="0") return "增加";
	if(data=="1") return "减少";
	return "";
}
//调账类型
function formatPayAdjustAccountCashAdjustBussType(data,row,index) {
	if(data=="0") return "普通调账";
	if(data=="1") return "线下充值调账";
	if(data=="2") return "代理分润调账";
	if(data=="3") return "投诉调账";
	if(data=="4") return "保证金调账";
	return "";
}
//审核时间
function formatPayAdjustAccountCashCheckTime(data,row,index) {
	if(row.status=="0"){
		return "";
	}else{
		return data;
	}
}

</script>

<div class="easyui-layout" data-options="fit:true">
	 <div data-options="region:'center'">
	 	<table id="payAdjustAccountCashList" style="width:100%;height:100%" rownumbers="true" pagination="true"
    	fit="true" data-options="border:false,collapsible:true,autoRowHeight:true,pageSize:20,singleSelect:true,url:'<%=path 
        %>/payAdjustAccountCash.htm?flag=0',method:'post',toolbar:'#payAdjustAccountCashListToolBar'">
	       <thead>
	        <tr>
	           <th field="id" width="9%" align="left">调账编号</th>
	           <th field="custId" width="5%" align="left" sortable="true">客户编号</th>
	           <th field="acType" width="5%" align="left" sortable="true" data-options="formatter:formatPayAdjustAccountCashAcType">客户类型</th>
	           <th field="storeName" width="15%" align="left" sortable="true">客户名称</th>
	           <th field="amt" width="6%" align="left" sortable="true">调账金额</th>
	           <th field="adjustType" width="4%" align="left" sortable="true" data-options="formatter:formatPayAdjustAccountCashAdjustType">调账类型</th>
	           <th field="adjustBussType" width="5%" align="left" sortable="true" data-options="formatter:formatPayAdjustAccountCashAdjustBussType">业务类型</th>
	           <!-- <th field="cashAcBal" width="9%" align="left" sortable="true">账户余额</th> -->
	           <th field="status" width="4%" align="left" sortable="true" data-options="formatter:formatPayAdjustAccountCashStatus">审核状态</th>
	           <th field="applyName" width="4%" align="left" sortable="true">申请人</th>
	           <th field="applyTime" width="8%" align="left" sortable="true">申请时间</th>
	           <th field="checkTime" width="8%" align="left" sortable="true" data-options="formatter:formatPayAdjustAccountCashCheckTime">审核时间</th>
			   <th field="operation" data-options="formatter:formatPayAdjustAccountCashOperator" width="6%" align="left">操作</th>
	           <th field="remark" width="30%" data-options="formatter:formatPayAdjustAccountCashRemark" align="left" sortable="true">备注</th>
	           <th field="checkName" width="6%" align="left" sortable="true">审核人</th>
	       </tr>
	       </thead>
		</table>
	 </div>
	 <div id="payAdjustAccountCashListToolBar" data-options="border:false" style="text-align:left;padding:5px 5px 5px 5px;">
	          客户编号<input type="text" id="searchPayAdjustAccountCashCustId" name="searchPayAdjustAccountCashCustId" class="easyui-textbox" value=""  style="width:130px"/>
	          客户类型<select class="easyui-combobox" panelHeight="auto" id="searchPayAdjustAccountCashAcType" 
				data-options="editable:false" name="searchPayAdjustAccountCashAcType"  style="width:130px">
		    		   <option value="">请选择</option>
		               <option value="<%=com.PayConstant.CUST_TYPE_USER %>">个人账户</option>
		               <option value="<%=com.PayConstant.CUST_TYPE_MERCHANT %>">商户账户</option>
		               <option value="<%=com.PayConstant.CUST_TYPE_CHARGE %>">商户手续费</option>
		    </select>
	          <!-- 申请人ID<input type="text" id="searchPayAdjustAccountCashApplyUser" name="searchPayAdjustAccountCashApplyUser" class="easyui-textbox" value=""  style="width:130px"/> -->
	          <!-- 审核人ID<input type="text" id="searchPayAdjustAccountCashCheckUser" name="searchPayAdjustAccountCashCheckUser" class="easyui-textbox" value=""  style="width:130px"/> -->
	          <!-- 申请时间<input type="text" id="searchPayAdjustAccountCashApplyTime" name="searchPayAdjustAccountCashApplyTime" class="easyui-datebox" value=""  style="width:130px"/> -->
	          审核状态<select class="easyui-combobox" panelHeight="auto" id="searchPayAdjustAccountCashStatus" 
				data-options="editable:false" name="searchPayAdjustAccountCashStatus"  style="width:130px">
		    		   <option value="">请选择</option>
		               <option value="0">待审核</option>
		               <option value="1">审核成功</option>
		               <option value="2">审核失败</option>
		    </select>
	         业务类型<select class="easyui-combobox" panelHeight="auto" id="searchPayAdjustAccountCashAdjustBussType" 
				data-options="editable:false" name="searchPayAdjustAccountCashAdjustBussType"  style="width:130px">
		               <option value="0">普通调账</option>
		               <option value="1">线下充值调账</option>
		               <option value="2">代理分润调账</option>
		               <option value="3">投诉调账</option>
		               <option value="4">保证金调账</option>
		    </select>     
	    <%if(actionSearch != null){//角色判断%>
	    <a href="javascript:searchPayAdjustAccountCashList()" class="easyui-linkbutton" iconCls="icon-search">查询</a>
	    <%} %>
	    <%if(actionAdd != null){//角色判断%>
	    <a href="javascript:addPayAdjustAccountCashPageOpen()" class="easyui-linkbutton"
	        iconCls="icon-add">调账申请</a>
	    <%} %>
	</div>
	<div data-options="region:'south',border:false" style="text-align:left;margin-left:7px;margin-top:5px;margin-bottom:5px;">
		<span id="payAdjustAccountCashGetTotalMoneyId">&nbsp;</span>
	</div>
</div>

<div id="payAdjustAccountCashCheckWindow" class="easyui-window" title="审核" 
    data-options="iconCls:'icon-edit',closed:true,modal:true" style="width:400px;height:200px;padding:5px;">
    <div class="easyui-layout" data-options="fit:true">
        <div data-options="region:'center'">
            <form id="payAdjustAccountCashForm" method="post">
            	<input type="hidden" name="updateId" id="setPayAdjustAccountCashId">
                <table cellpadding="5">
                    <tr><td align="right">处理结果：</td><td>
                    	<input type="radio" name="status" value="1" onclick="payAdjustAccountCashCheckRequired('1')" checked>通过
                  		<input type="radio" name="status" value="2" onclick="payAdjustAccountCashCheckRequired('2')">不通过</td></tr>
                    <tr><td align="right">&nbsp;</td><td><input class="easyui-textbox" type="text" name="remark" id="payAdjustAccountCashCheckRemark"
				              validType="length[0,50]" invalidMessage="内容请控制在50个字" data-options="multiline:true"
				              style="width:200px;height:70px"/></td></tr>
                </table>
            </form>
        </div>
        <div data-options="region:'south',border:false" style="text-align:right;padding:5px 0 0;">
            <a class="easyui-linkbutton" data-options="iconCls:'icon-ok'" href="javascript:void(0)" 
            	id="payAdjustAccountCashCheckPass" style="width:80px" onclick="$('#payAdjustAccountCashForm').submit()">确定</a>
            <a class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" href="javascript:void(0)" 
                onclick="$('#payAdjustAccountCashCheckWindow').window('close')" style="width:80px">取消</a>
        </div>
    </div>
</div>
<script type="text/javascript">

//手动绑定 submit 事件
/*
$("#payAdjustAccountCashCheckPass").click(function(){
	$("#payAdjustAccountCashForm").submit();
});*/

$('#payAdjustAccountCashList').datagrid({
    onLoadError: function(data){
        topCenterMessage('<%=JWebConstant.ERROR %>','数据加载失败！');
    },
    onLoadSuccess: function(data){
        $(this).datagrid('fixRownumber');//行号自适应宽度
        $("#payAdjustAccountCashGetTotalMoneyId").html("调账总金额："+(parseFloat(data.totalAccountCashMoney)*0.01).toFixed(2)
            	+"元");
    }
});
function formatPayAdjustAccountCashOperator(val,row,index){
     var tmp=
        <%if(actionDetail != null){//角色判断%>
        "<a href=\"javascript:detailPayAdjustAccountCashPageOpen('"+row.id+"')\"><%=actionDetail.name %></a>&nbsp;"+
        <%}%>
        <%if(actionUpdate != null){//角色判断%>
        // 原有的修改方法    "<a href=\"javascript:updatePayAdjustAccountCashPageOpen('"+row.id+"')\">    <=actionUpdate.name %></a>&nbsp;"+
        
        //未审核显示
        (row.status=='0'?"<a href=\"javascript:payAdjustAccountCashCheckWindowOpen('"+row.id+"')\"><%=actionUpdate.name %></a>&nbsp;&nbsp;&nbsp;":"")+
        <%}%>
        <%if(actionRemove !=null ){//角色判断%>
          (row.status=='0'?"<a href=\"javascript:removePayAdjustAccountCash('"+row.id+"')\"><%=actionRemove.name %></a>":"")+
        <%}%>
        "";
     return tmp;
}
function searchPayAdjustAccountCashList(){
    $('#payAdjustAccountCashList').datagrid('load',{
          custId:$('#searchPayAdjustAccountCashCustId').val(),
          acType:$('#searchPayAdjustAccountCashAcType').combobox('getValue'),//商户类型
          //applyUser:$('#searchPayAdjustAccountCashApplyUser').val(),
          // checkUser:$('#searchPayAdjustAccountCashCheckUser').val(),
          //applyTime:$('#searchPayAdjustAccountCashApplyTime').datebox('getValue'),
          status:$('#searchPayAdjustAccountCashStatus').combobox('getValue'),//审核状态
          adjustBussType:$('#searchPayAdjustAccountCashAdjustBussType').combobox('getValue')//审核状态
    });  
}
function addPayAdjustAccountCashPageOpen(){
    openTab('addPayAdjustAccountCashPage',payAdjustAccountCashAddPageTitle,'<%=path %>/jsp/pay/adjustaccount/pay_adjust_account_cash_add.jsp');
}
function detailPayAdjustAccountCashPageOpen(id){
    openTab('detailPayAdjustAccountCashPage',payAdjustAccountCashDetailPageTitle,'<%=path %>/detailPayAdjustAccountCash.htm?id='+id);
}
function updatePayAdjustAccountCashPageOpen(id){
    openTab('updatePayAdjustAccountCashPage',payAdjustAccountCashUpdatePageTitle,'<%=path %>/updatePayAdjustAccountCash.htm?flag=show&id='+id);
}
//审核
function payAdjustAccountCashCheckWindowOpen(updateId){
	$('#payAdjustAccountCashCheckWindow').form('clear');
	$('#payAdjustAccountCashCheckWindow').window('open');
	document.getElementById('setPayAdjustAccountCashId').value=updateId;
	$("input[name='status']").get(0).checked=true;
}

//审核操作
$('#payAdjustAccountCashForm').form({
    url:'<%=path %>/setpayAdjustAccountCashCheck.htm',
    onSubmit: function(){
    	var f = $(this).form('validate');
    	if(f)$('#payAdjustAccountCashCheckWindow').window('close');
       	return f;
    },
    success:function(data){
    	$('#payAdjustAccountCashList').datagrid('reload');
        if(data=='<%=JWebConstant.OK %>'){
           topCenterMessage('<%=JWebConstant.OK %>','审核成功！');
        } else topCenterMessage('<%=JWebConstant.ERROR %>',data);
    }
});

function payAdjustAccountCashCheckRequired(flag){
	if(flag == '1'){
		$('#payAdjustAccountCashCheckRemark').textbox({
			required:false
	    });
	} else {
	 	$('#payAdjustAccountCashCheckRemark').textbox({
			required:true
	    });
	    $('#payAdjustAccountCashCheckRemark').textbox('textbox').focus();
	}
}
function formatPayAdjustAccountCashRemark(val,row,index){
	return "<span title='"+row.remark+"'>"+(row.remark == undefined ? "" : row.remark)+"</span>";
}
//删除该节点及其子节点
function removePayAdjustAccountCash(id){
	 $.messager.confirm('提示','您确定要删除该调账记录吗？', function(r){
		    if(!r)return;
		    try{
		        $.post('<%=path %>/removePayAdjustAccountCash.htm',
		            {id:id},
		            function(data){
		                $('#payAdjustAccountCashList').datagrid('reload');
		                if(data=='<%=JWebConstant.OK %>'){
		                    topCenterMessage('<%=JWebConstant.OK %>','操作成功!');
		                } else topCenterMessage('<%=JWebConstant.ERROR %>',data);
		            },
		           'text'
		        );
		    }catch(e){alert(e);}
	});
}
</script>
