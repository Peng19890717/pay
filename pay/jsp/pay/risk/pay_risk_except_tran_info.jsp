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
JWebAction actionSearch = ((JWebAction)user.actionMap.get("pEfx7dB2Q3rKTOI2"));
JWebAction actionOperation = ((JWebAction)user.actionMap.get("pEfx7dB2Q3rKTOI3"));
%>
<script type="text/javascript">
var payRiskExceptTranInfoListPageTitle="非正常交易";
var payRiskExceptTranInfoOperationTitle="处理设置";
$(document).ready(function(){});
// 客户类型
function entityType_formatter(data,row,index) {
	if(data=="<%=com.PayConstant.CUST_TYPE_USER %>"){
		return "个人";
	}else if(data=="<%=com.PayConstant.CUST_TYPE_MERCHANT %>"){
		return "商户";
	}
}	

// 监控类型
function tranType_formatter(data,row,index) {
	//	PRDORDTYPE	商品订单类型 0-消费；1-充值  2-担保支付   3商户充值
	if(data=="1"){
		return "消费";
	}else if(data=="2"){
		return "充值";
	}else if(data=="3"){
		return "结算";
	}else if(data=="4"){
		return "退款";
	}else if(data=="5"){
		return "提现";
	}else if(data=="6"){
		return "转账";
	}
}

// 订单状态
/* function ordstatus_formatter(data,row,index) {
	if(data=="00"){
		return "等待付款";
	}else if(data=="01"){
		return "付款成功";
	}else if(data=="02"){
		return "付款失败";
	}else if(data=="03"){
		return "退款处理中";
	}
} */

// 异常类型
function warnType_formatter(data,row,index) {
	if(data=="1"){
		return "异常交易";
	}else if(data=="2"){
		return "可疑交易";
	}
}
	
// 预警项
function ruleLevelItem_formatter(data,row,index) {
	if(data=="0"){
		return "交易突增";
	} else if(data=="1"){
		return "疑似套现 ";
	} else if(data=="2"){
		return "分单 ";
	} else if(data=="3"){
		return "非常规时段交易";
	} else if(data=="4"){
		return "零交易";
	} else if(data=="5"){
		return "其它 ";
	} else if(data=="6"){
		return "无";
	}
}	

// 监控状态
function exceptTranFlag_formatter(data,row,index) {
	if(data=="0"){
		return "未处理";
	} else if(data=="1"){
		return "已释放";
	} else if(data=="2"){
		return "已确认";
	}
}
</script>
<table id="payRiskExceptTranInfoList" style="width:100%;height:100%" rownumbers="true" pagination="true"
    fit="true" data-options="border:false,collapsible:true,autoRowHeight:true,pageSize:20,singleSelect:true,url:'<%=path 
        %>/payRiskExceptTranInfo.htm?flag=0',method:'post',toolbar:'#payRiskExceptTranInfoListToolBar'">
       <thead>
        <tr>
           <th field="entityType" width="5%" align="left" sortable="true" formatter="entityType_formatter">客户类型</th>
           <th field="custId" width="7%" align="left" sortable="true">客户编号</th>
           <th field="storeName" width="12%" align="left" sortable="true">客户名称</th>
           <th field="tranId" width="9%" align="left" sortable="true">交易ID</th>
           <!-- <th field="ordamt" width="5%" align="left" sortable="true">商品金额</th> -->
           <th field="tranType" width="5%" align="left" sortable="true" formatter="tranType_formatter">监控类型</th>
           <!-- <th field="ordstatus" width="5%" align="left" sortable="true" formatter="ordstatus_formatter">订单状态</th> -->
           <th field="ruleName" width="9%" align="left" sortable="true">规则名称</th>
           <th field="warnType" width="5%" align="left" sortable="true" formatter="warnType_formatter">异常类型</th>
           <th field="ruleLevelItem" width="9%" align="left" sortable="true" formatter="ruleLevelItem_formatter">预警项</th>
           <th field="regdtTime" width="6%" align="left" sortable="true">监控时间</th>
           <th field="exceptTranFlag" width="5%" align="left" sortable="true" formatter="exceptTranFlag_formatter">监控状态</th>
           <th field="updateTime" width="6%" align="left" sortable="true">操作日期</th>
           <th field="updateUser" width="5%" align="left" sortable="true">操作员</th>
           <th field="operation" data-options="formatter:formatPayRiskExceptTranInfoOperator" width="19%" align="left">操作</th>
       </tr>
       </thead>
</table>
<div id="payRiskExceptTranInfoListToolBar" data-options="border:false" style="text-align:left;padding:5px 5px 5px 5px;">
    客户类型：
    <select class="easyui-combobox" id="searchPayRiskExceptTranInfoEntityType" name="searchPayRiskExceptTranInfoEntityType" missingMessage="请选择客户类型" data-options="editable:false">
   		<option value="">请选择</option>
   		<option value="<%=com.PayConstant.CUST_TYPE_USER %>">个人</option>
   		<option value="<%=com.PayConstant.CUST_TYPE_MERCHANT %>">商户</option>
   	</select>
    客户编号：
    <input type="text" id="searchPayRiskExceptTranInfoCustId" name="searchPayRiskExceptTranInfoCustId" class="easyui-textbox" value=""  style="width:130px"/>
   监控时间：
    <input type="text" id="searchPayRiskExceptTranInfoRegdtTime" name="searchPayRiskExceptTranInfoRegdtTime" data-options="editable:false" class="easyui-datebox" value=""  style="width:130px"/>
   	~
    <input type="text" id="searchPayRiskExceptTranInfoRegdtTimeSearchEnd" name="searchPayRiskExceptTranInfoRegdtTimeSearchEnd" data-options="editable:false" class="easyui-datebox" value=""  style="width:130px"/>
    规则名称：  
    <input type="text" id="searchPayRiskExceptTranInfoRuleName" name="searchPayRiskExceptTranInfoRuleName" class="easyui-textbox" value=""  style="width:130px"/>
    异常交易类型：
    <select class="easyui-combobox" id="searchPayRiskExceptTranInfoWarnType" name="searchPayRiskExceptTranInfoWarnType" missingMessage="请选择异常类型" data-options="editable:false">
   		<option value="">请选择</option>
   		<option value="1">异常</option>
   		<option value="2">可疑</option>
    </select>
    监控状态：
    <select class="easyui-combobox" id="searchPayRiskExceptTranInfoExceptTranFlag" name="searchPayRiskExceptTranInfoExceptTranFlag" missingMessage="请选择交易状态" data-options="editable:false">
   		<option value="">请选择</option>
   		<option value="0">未处理</option>
   		<option value="1">已释放</option>
   		<option value="2">已确认</option>
    </select>
    监控类型：
    <select class="easyui-combobox" id="searchPayRiskExceptTranInfoTranType" name="searchPayRiskExceptTranInfoTranType" missingMessage="请选择交易类型" data-options="editable:false">
   		<option value="">请选择</option>
   		<option value="1">消费</option>
   		<option value="2">充值</option>
   		<option value="3">结算</option>
   		<option value="4">退款</option>
   		<option value="5">提现</option>
   		<option value="6">转账</option>
    </select>
    <%if(actionSearch != null){//角色判断%>
    <a href="javascript:searchPayRiskExceptTranInfoList()" class="easyui-linkbutton" iconCls="icon-search"><%=actionSearch.name %></a>
    <%} %>
</div>

<div id="payRiskExceptTranInfoOperationStatusWindow" class="easyui-window" title="处理设置" 
    data-options="iconCls:'icon-add',closed:true,modal:true" style="width:400px;height:230px;padding:5px;">
    <div class="easyui-layout" data-options="fit:true">
        <div data-options="region:'center'">
            <form id="payRiskExceptTranInfoFormOperationStatus" method="post">
            	<input type="hidden" name="id" id="hiddenId">
                <table cellpadding="5">
                    <tr><td width="25%">&nbsp;</td><td width="75%">&nbsp;</td></tr>
                  <tr>
                      <td align="right">监控状态：</td>
                      <td>
                      	<select class="easyui-combobox"  name="exceptTranFlag" missingMessage="请选择交易状态" data-options="editable:false">
					   		<option value="1" selected="selected">释放</option>
					   		<option value="2">确认</option>
					    </select>
                	  </td>
                  </tr>
                  <tr>
                      <td align="right" valign="top">备注：</td>
                      <td>
                      		<textarea class="textbox-text" autocomplete="off" name="remark" id="remarkIdForExceptTranInfo"></textarea>   
                      </td>
                  </tr>
                </table>
            </form>
        </div>
        <div data-options="region:'south',border:false" style="text-align:right;padding:5px 0 0;">
            <a class="easyui-linkbutton" data-options="iconCls:'icon-ok'" href="javascript:operationStatusForm()" style="width:80px">保存</a>
            <a class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" href="javascript:void(0)" 
                onclick="$('#payRiskExceptTranInfoOperationStatusWindow').window('close')" style="width:80px">取消</a>
        </div>
    </div>
</div>


<script type="text/javascript">
$('#payRiskExceptTranInfoList').datagrid({
    onLoadError: function(data){
        topCenterMessage('<%=JWebConstant.ERROR %>','数据加载失败！');
    },
    onLoadSuccess: function(data){
        $(this).datagrid('fixRownumber');//行号自适应宽度
    }
});
function formatPayRiskExceptTranInfoOperator(val,row,index){
     var tmp=
        <%if(actionOperation != null){//角色判断%>
        	(0 == row.exceptTranFlag) ? "<a href=\"javascript:operationStatusPayRiskExceptTranInfo('"+row.id+"')\"><%=actionOperation.name %></a>&nbsp;" : "" +
        <%}%>
        "";
     return tmp;
}
function searchPayRiskExceptTranInfoList(){
    $('#payRiskExceptTranInfoList').datagrid('load',{
          entityType:$('#searchPayRiskExceptTranInfoEntityType').datebox('getValue'),
          custId:$('#searchPayRiskExceptTranInfoCustId').val(),
          regdtTime:$('#searchPayRiskExceptTranInfoRegdtTime').datebox('getValue'),
          regdtTimeSearchEnd:$('#searchPayRiskExceptTranInfoRegdtTimeSearchEnd').datebox('getValue'),
          ruleName:$('#searchPayRiskExceptTranInfoRuleName').val(),
          warnType:$('#searchPayRiskExceptTranInfoWarnType').datebox('getValue'),
          exceptTranFlag:$('#searchPayRiskExceptTranInfoExceptTranFlag').datebox('getValue'),
          tranType:$('#searchPayRiskExceptTranInfoTranType').datebox('getValue'),
    });  
}

function operationStatusPayRiskExceptTranInfo(id){
	$('#remarkIdForExceptTranInfo').val("");
	$('#hiddenId').val(id); // 携带ID为隐藏域赋值
	$('#payRiskExceptTranInfoOperationStatusWindow').window('open');
}

function operationStatusForm(){
	$('#payRiskExceptTranInfoFormOperationStatus').submit();
}

$('#payRiskExceptTranInfoFormOperationStatus').form({
    url:'<%=path%>/payRiskExceptTranInfoOperationStatus.htm',
    onSubmit: function(){
       	$('#payRiskExceptTranInfoOperationStatusWindow').window('close');
       	$('#payRiskExceptTranInfoList').datagrid('loading');
    },
    success:function(data){
    	$('#payRiskExceptTranInfoList').datagrid('reload');
        if(data=='<%=JWebConstant.OK %>'){
           topCenterMessage('<%=JWebConstant.OK %>','提交成功！');
        } else topCenterMessage('<%=JWebConstant.ERROR %>',data);
    }
});
</script>
