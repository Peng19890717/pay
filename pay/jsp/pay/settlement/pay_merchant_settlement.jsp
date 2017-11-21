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
JWebAction actionSearch = ((JWebAction)user.actionMap.get("pwONhjl2Q3rKTOI1"));
JWebAction actionManualSettlementRun = ((JWebAction)user.actionMap.get("pwONhjl2Q3rKTOI7"));
JWebAction actionStlApply = ((JWebAction)user.actionMap.get("pwONhjl2Q3rKTOI2"));
JWebAction actionStlReApply = ((JWebAction)user.actionMap.get("pwONhjl2Q3rKTOI3"));
JWebAction actionDetail = ((JWebAction)user.actionMap.get("pwONhjl2Q3rKTOI6"));
JWebAction actionReceiveDetail = ((JWebAction)user.actionMap.get("q1JzHol2Q3rKTOQ1"));
JWebAction actionStlReCheck = ((JWebAction)user.actionMap.get("pwONhjl2Q3rKTOI4"));
JWebAction actionSetResult = ((JWebAction)user.actionMap.get("pwONhjl2Q3rKTOI5"));
%>
<script type="text/javascript">
var payMerchantSettlementListPageTitle="商户结算管理";
/*
var payMerchantSettlementStatusMap = new Map();
$(document).ready(function(){
	payMerchantSettlementStatusMap.set("0", "初始状态");
	payMerchantSettlementStatusMap.set("1", "待审核");
	payMerchantSettlementStatusMap.set("2", "审核通过");
	payMerchantSettlementStatusMap.set("3", "审核失败");
	payMerchantSettlementStatusMap.set("4", "结算成功");
	payMerchantSettlementStatusMap.set("5", "结算失败");
});*/
function formatPayMerchantSettlementStlStatus(data,row, index){
	if(data=="0")return "初始状态";
	else if(data=="1")return "待审核";
	else if(data=="2")return "审核通过";
	else if(data=="3")return "审核失败";
	else if(data=="4")return "结算成功";
	else if(data=="5")return "结算失败";
	else return "";
	//return payMerchantSettlementStatusMap.get(data);
}
function formatPayMerchantSettlementWay(data,row, index){
	if(data=="0")return "自动";
	else if(data=="1")return "线下";
	else return "";
	//return payMerchantSettlementStatusMap.get(data);
}
function formatPayMerchantSettlementStlPeriod(data,row, index){
	if(data=="D")return "日结";
	else if(data=="W")return "周结";
	else if(data=="M")return "月结";
	else return "";
}
//跳到交易列表页面 
function detailPayMerchantSettlementPageOpen(stlMerId,stlFromTime,stlToTime){
	var tabTmp = $('#mainTabs'),title='交易查询';
	/**当前的tab 是否存在如果存在就 将其关闭**/  
	if(tabTmp.tabs('exists', title))$('#mainTabs').tabs('close',title);
    /**添加一个tab标签**/
	tabTmp.tabs('add',{id:'orderShowList',title: title,selected: true,closable: true,border:false});
    $('#orderShowList').panel('refresh','<%=path %>/jsp/pay/order/pay_order.jsp?merno='+stlMerId+'&createtimeStart='+stlFromTime+'&createtimeEnd='+stlToTime+'&ordstatus=01');
}
function detailReceiveSettlementPageOpen(custId,createTimeStart,createTimeEnd){
	if(typeof(createTimeStart) == "undefined" || typeof(createTimeEnd) == "undefined"){
		return;
	}
	var tabTmp = $('#mainTabs'),title='代收付';
	/**当前的tab 是否存在如果存在就 将其关闭**/  
	if(tabTmp.tabs('exists', title))$('#mainTabs').tabs('close',title);
    /**添加一个tab标签**/
	tabTmp.tabs('add',{id:'payReceiveAndPayShowList',title: title,selected: true,closable: true,border:false});
    $('#payReceiveAndPayShowList').panel('refresh','<%=path %>/jsp/pay/order/pay_receive_and_pay.jsp?custId='+custId+'&createTimeStart='+createTimeStart+'&createTimeEnd='+createTimeEnd+'&status=1'+'&type=0');
}

//excel导出js
function payMerchantSettlementExportExcel(){
	$.messager.confirm('提示', '您确认将当前记录导出至excel吗？', function(r){
       if(!r)return;
	   // $('#payMerchantSettlementList').datagrid('loading');
	    try{
			window.location.href="<%=path %>/payMerchantSettlementExportExcel.htm?"+
		 	"stlId="+$('#searchPayMerchantSettlementStlId').val()+//结算批次
	        "&stlMerId="+$('#searchPayMerchantSettlementStlMerId').val()+//商户编号
		    "&storeName="+$('#searchPayMerchantSettlementStoreName').val()+//商户名称
	       	"&stlFromTime="+$('#searchPayMerchantSettlementStlFromTime').datebox('getValue')+//交易开始时间。
	       	"&stlToTime="+$('#searchPayMerchantSettlementStlToTime').datebox('getValue')+//交易结束时间
	    	"&stlApplDateStart="+$('#searchPayMerchantSettlementStlApplDateStart').datebox('getValue')+//结算开始时间。
	       	"&stlApplDateEnd="+$('#searchPayMerchantSettlementStlApplDateEnd').datebox('getValue')+//结算结束时间
	       	"&stlStatus="+$('#searchPayMerchantSettlementStlStatus').combobox('getValue')+//结算状态
	       	"&stlPeriod="+$('#searchPayMerchantSettlementCustSetPeriod').combobox('getValue')+//结算周期
	       	"&settlementWay="+$('#searchPayMerchantSettlementSettlementWay').combobox('getValue');//结算周期
   	  	}catch(e){alert(e);}
   	  
   	});
}
</script>
<div class="easyui-layout" data-options="fit:true">
	 <div data-options="region:'center'">
			<table id="payMerchantSettlementList" style="width:148%;height:100%" rownumbers="true" pagination="true"
			    fit="true" data-options="border:false,collapsible:true,autoRowHeight:true,pageSize:20,url:'<%=path 
			        %>/payMerchantSettlement.htm?flag=0',method:'post',toolbar:'#payMerchantSettlementListToolBar',checkOnSelect:true">
			       <thead>
			        <tr>
			        	<th data-options="field:'check',checkbox:true"></th>
			            <th field="stlId" width="7%" align="center">结算批<br/>次号</th>
			            <th field="stlMerId" width="4%" align="center" sortable="true">商户编号</th>
			            <th field="storeName" width="8%" align="center">商户名称<br/>（开户行名称）</th>
			            <th field="stlFromTime" width="5%" align="center" sortable="true">交易开始<br/>日期</th>
			            <th field="stlToTime" width="5%" align="center" sortable="true">交易结束<br/>日期</th>
			            <th field="stlApplDate" width="5%" align="center" sortable="true">结算日期<br/>(跑批)</th>
			            <th field="stlApplystlCount" width="4%"align="center">交易结算<br/>笔数</th>
			            <th field="stlApplystlamt" width="4%"align="center">交易结算<br/>金额</th>
			            <th field="stlRefundCount" width="4%"align="center">退款笔数</th>
			            <th field="stlRefundAmt" width="4%"align="center">退款金额</th>
			            <th field="stlFee" width="4%"align="center">交易结算<br/>手续费</th>
			            <th field="chargeWay" width="5%"align="center">手续费<br/>收取方式</th>
			            <th field="agentStlIn" width="4%"align="center">代理分润</th>
			            <th field="stlFromTimeReceive" width="5%" align="center" sortable="true">代收开始<br/>日期</th>
			            <th field="stlToTimeReceive" width="5%" align="center" sortable="true">代收结束<br/>日期</th>
			            <th field="receiveCount" width="4%"align="center">代收笔数</th>
			            <th field="receiveAmt" width="4%"align="center">代收金额</th>
			            <th field="receiveFee" width="4%"align="center">代收<br/>手续费</th>
			           <!--  <th field="stlFee" width="4%"align="center">总手续费</th> -->
			            <th field="stlNetrecamt" width="4%" align="center">应结<br/>算额</th>
			            <th field="stlAmtOver" width="4%" align="center">已结<br/>算额</th>
			            <th field="stlPeriod" data-options="formatter:formatPayMerchantSettlementStlPeriod" width="3%" align="center">交易结算<br/>周期</th>
			            <th field="stlPeriodDaySet" width="3%" align="center">交易<br/>结算日</th>
			            <th field="stlPeriodAgent" data-options="formatter:formatPayMerchantSettlementStlPeriod" width="3%" align="center">代理结算<br/>周期</th>
			            <th field="stlPeriodDaySetAgent" width="3%" align="center">代理结算日</th>
			            <th field="stlPeriodReceive" data-options="formatter:formatPayMerchantSettlementStlPeriod" width="3%" align="center">代收结算<br/>周期</th>
			            <th field="stlPeriodSetReceive" width="3%" align="center">代收<br/>结算日</th>
			            <th field="stlStatus" data-options="formatter:formatPayMerchantSettlementStlStatus" width="4%" align="center">状态</th>
			            <th field="operation" data-options="formatter:formatPayMerchantSettlementOperator" width="15%" align="left">操作</th>
			            <th field="depositBankCode" width="5%" align="center">开户行</th>
			            <th field="depositBankBrchName" width="10%" align="left">开户行网点</th>
			            <th field="custStlBankAcNo" width="11%" align="left">结算账号</th>
			            <th field="stlAutoFlag" width="5%" align="center">清算类型</th>
			            <th field="settlementWay" width="5%" align="center" data-options="formatter:formatPayMerchantSettlementWay">结算类型</th>
			            <th field="remark" width="5%" align="center">备注</th>
			            <th field="stlApplicants" width="5%" align="center">申请人</th>
			            <th field="stlApplTime" width="6%" align="center" sortable="true">申请时间</th>
			            <th field="stlAuditPer" width="5%" align="center">审核人</th>
			            <th field="stlAuditTime" width="6%" align="center" sortable="true">审核时间</th>
			            <th field="stlSucTime" width="6%" align="center" sortable="true">结算成功<br/>时间</th>
			            <th field="stlSucOperator" width="5%" align="center">结算人</th>
			       </tr>
			       </thead>
			</table>
	</div>
<div id="payMerchantSettlementListToolBar" data-options="border:false" style="text-align:left;padding:5px 5px 5px 5px;">
	结算批次号<input type="text" id="searchPayMerchantSettlementStlId" name="searchPayMerchantSettlementStlId" class="easyui-textbox" value=""  style="width:130px"/>
	商户编号<input type="text" id="searchPayMerchantSettlementStlMerId" name="searchPayMerchantSettlementStlMerId" class="easyui-textbox" value=""  style="width:130px"/>
	商户名称<input type="text" id="searchPayMerchantSettlementStoreName" name="searchPayMerchantSettlementStoreName" class="easyui-textbox" value=""  style="width:130px"/>
           交易日期<input type="text" id="searchPayMerchantSettlementStlFromTime" name="searchPayMerchantSettlementStlFromTime"
		 data-options="editable:false" class="easyui-datebox" value=""  style="width:100px"/>
    ~<input type="text" id="searchPayMerchantSettlementStlToTime" name="searchPayMerchantSettlementStlToTime"
		 data-options="editable:false" class="easyui-datebox" value=""  style="width:100px"/>
           结算日期<input type="text" id="searchPayMerchantSettlementStlApplDateStart" name="searchPayMerchantSettlementStlApplDateStart"
		 data-options="editable:false" class="easyui-datebox" value=""  style="width:100px"/>
    ~<input type="text" id="searchPayMerchantSettlementStlApplDateEnd" name="searchPayMerchantSettlementStlApplDateEnd"
		 data-options="editable:false" class="easyui-datebox" value=""  style="width:100px"/>
    结算状态<select class="easyui-combobox" panelHeight="auto" id="searchPayMerchantSettlementStlStatus" 
			data-options="editable:false" name="stlStatus"  style="width:130px">
	    		   <option value="">请选择</option>
	               <option value="0">初始状态</option>
	               <option value="1">待审核</option>
	               <option value="2">审核通过</option>
	               <option value="3">审核失败</option>
	               <option value="4">结算成功</option>
	               <option value="5">结算失败</option>
	    </select>
     结算周期<select class="easyui-combobox" panelHeight="auto" id="searchPayMerchantSettlementCustSetPeriod" 
			data-options="editable:false" name="searchPayMerchantSettlementCustSetPeriod"  style="width:130px">
	    		   <option value="">请选择</option>
	               <option value="D">日结</option>
	               <option value="W">周结</option>
	    </select>
     结算类型<select class="easyui-combobox" panelHeight="auto" id="searchPayMerchantSettlementSettlementWay" 
			data-options="editable:false" name="settlementWay"  style="width:130px">
	    		   <option value="">请选择</option>
	               <option value="0">自动</option>
	               <option value="1">线下</option>
	    </select>
    <%if(actionSearch != null){//角色判断%>
    <a href="javascript:searchPayMerchantSettlementList()" class="easyui-linkbutton" iconCls="icon-search"><%=actionSearch.name %></a>
    <%} %>
    <%if(actionManualSettlementRun != null){//角色判断%>
    <a href="javascript:manualSettlementRunWindowOpen()" class="easyui-linkbutton"
        iconCls="icon-config"><%=actionManualSettlementRun.name %></a>
    <%} %>
    <%if(actionStlApply != null){//角色判断%>
    <a href="javascript:stlApplySelectSubmit()" class="easyui-linkbutton"
        iconCls="icon-config"><%=actionStlApply.name %></a>
    <%} %>
    <%if(actionStlReApply != null){//角色判断%>
    <a href="javascript:stlReApplySelectSubmit()" class="easyui-linkbutton"
        iconCls="icon-config"><%=actionStlReApply.name %></a>
    <%} %>
</div>
<div data-options="region:'south',border:false" style="text-align:left;margin-left:7px;margin-top:5px;margin-bottom:5px;">
		<%if(actionSearch != null){//角色判断%>
		<a href="javascript:payMerchantSettlementExportExcel()"  class="easyui-linkbutton" iconCls="icon-config">excel导出</a>
		<%} %>
		<span id="getSettlementTotalMoneyId">&nbsp;</span>
</div>
<div id="manualSettlementRunWindow" class="easyui-window" title="手动结算跑批" 
    data-options="iconCls:'icon-config',closed:true,modal:true" style="width:300px;height:200px;padding:5px;">
    <div class="easyui-layout" data-options="fit:true">
        <div data-options="region:'center'">
            <form id="manualSettlementRunForm" method="post">
                <table cellpadding="5">
                    <tr><td width="50%">&nbsp;</td><td width="50%">&nbsp;</td></tr>
                    <tr>
                        <td align="right">结算日期：</td>
                        <td><input type="text" id="manualSettlementRunDate" name="manualSettlementRunDate" missingMessage="请输入结算日期"
                        	class="easyui-datebox" value=""  style="width:100px" data-options="required:true" editable="fasle"/></td>
                    </tr>
                    <tr>
                        <td align="right">结算商户号：</td>
                        <td><input type="text" id="manualSettlementRunMerNo" name="manualSettlementRunMerNo"
                        	class="easyui-textbox" value=""  style="width:100px"/></td>
                    </tr>
                </table>
            </form>
        </div>
        <div data-options="region:'south',border:false" style="text-align:right;padding:5px 0 0;">
            <a class="easyui-linkbutton" data-options="iconCls:'icon-ok'" href="javascript:void(0)"
            	onclick="$('#manualSettlementRunForm').submit()" style="width:80px">确定</a>
            <a class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" href="javascript:void(0)" 
                onclick="$('#manualSettlementRunWindow').window('close')" style="width:80px">取消</a>
        </div>
    </div>
</div>
	<div id="reCheckPayMerchantSettlementWindow" class="easyui-window" title="结算复审" 
	    data-options="iconCls:'icon-edit',closed:true,modal:true" style="width:400px;height:500px;padding:5px;">
	    <div class="easyui-layout" data-options="fit:true">
	        <div data-options="region:'center'">
	            <form id="reCheckPayMerchantSettlementForm" method="post">
	            	<input type="hidden" id="reCheckPayMerchantSettlementStlId0" name="stlId" value="">
	                <table cellpadding="5">
						<tr><td width="40%">&nbsp;</td><td width="60%">&nbsp;</td></tr>
						<tr><tdalign="center">结算批次号：</td><td><span id="reCheckPayMerchantSettlementStlId"></span></td></tr>
						<tr><td align="right">商户编号：</td><td><span id="reCheckPayMerchantSettlementStlMerId"></span></td></tr>
						<tr><td align="right">商户名称：</td><td><span id="reCheckPayMerchantSettlementStoreName"></span></td></tr>
						<tr><td align="right">交易日期：</td><td><span id="reCheckPayMerchantSettlementStlFromTime"></span>到<span id="reCheckPayMerchantSettlementStlToTime"></span></td></tr>
						<tr><td align="right">结算总笔数：</td><td><span id="reCheckPayMerchantSettlementStlApplystlCount"></span></td></tr>
						<tr><td align="right">结算总金额：</td><td><span id="reCheckPayMerchantSettlementStlTotalordamt"></span></td></tr>
						<tr><td align="right">结算手续费：</td><td><span id="reCheckPayMerchantSettlementStlFee"></span></td></tr>
		                <tr><td align="right">应结算金额：</td><td><span id="reCheckPayMerchantSettlementStlNetrecamt"></span></td></tr>
		                <tr><td align="right">结算周期：</td><td><span id="reCheckPayMerchantSettlementCustSetPeriod"></span></td></tr>
		                <tr><td align="right">结算日：</td><td><span id="reCheckPayMerchantSettlementCustStlTimeSet"></span></td></tr>
		                <tr><td align="right">开户行：</td><td><span id="reCheckPayMerchantSettlementDepositBankCode"></span></td></tr>
		                <tr><td align="right">网店名称：</td><td><span id="reCheckPayMerchantSettlementDepositBankBrchName"></span></td></tr>
		                <tr><td align="right">开户名称：</td><td><span id="reCheckPayMerchantSettlementCustBankDepositName"></span></td></tr>
		                <tr><td align="right">结算账号：</td><td><span id="reCheckPayMerchantSettlementCustStlBankAcNo"></span></td></tr>
		                <tr><td align="right">审核时间：</td><td><span id="reCheckPayMerchantSettlementStlApplTime"></span></td></tr>
		                <tr><td align="right">审核状态：</td><td><span id="reCheckPayMerchantSettlementStlStatus"></span></td></tr>
		                <tr><td align="right">结算申请人：</td><td><span id="reCheckPayMerchantSettlementStlApplicants"></span></td></tr>
		                <tr><td align="right">备注：</td><td><span id="reCheckPayMerchantSettlementRemarkInfo"></span></td></tr>
	                    <tr><td align="right">审核建议：</td><td>
	                    	<input type="radio" id="reCheckPayStlStatus2" name="stlStatus" value="2" onclick="reCheckPayMerchantSettlementRemarkRequired('0')" checked>审核通过
	                  		<input type="radio" name="stlStatus" value="3" onclick="reCheckPayMerchantSettlementRemarkRequired('1')">审核失败</td></tr>
	                    <tr><td align="right">&nbsp;</td><td><input class="easyui-textbox" type="text" name="remark" id="reCheckPayMerchantSettlementRemark"
					              validType="length[0,50]" invalidMessage="协议内容请控制在50个字" data-options="multiline:true"
					              style="width:200px;height:70px"/></td></tr>
	                </table>
	            </form>
	        </div>
	        <div data-options="region:'south',border:false" style="text-align:right;padding:5px 0 0;">
	            <a class="easyui-linkbutton" data-options="iconCls:'icon-ok'" href="javascript:void(0)" 
	            	onclick="$('#reCheckPayMerchantSettlementForm').submit()" style="width:80px">确定</a>
	            <a class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" href="javascript:void(0)" 
	                onclick="$('#reCheckPayMerchantSettlementWindow').window('close')" style="width:80px">取消</a>
	        </div>
	    </div>
	</div>
	<div id="setResultPayMerchantSettlementWindow" class="easyui-window" title="结算结果设置" 
	    data-options="iconCls:'icon-edit',closed:true,modal:true" style="width:400px;height:200px;padding:5px;">
	    <div class="easyui-layout" data-options="fit:true">
	        <div data-options="region:'center'">
	            <form id="setResultPayMerchantSettlementForm" method="post">
	            	<input type="hidden" id="setResultPayMerchantSettlementStlId" name="stlId" value="">
	                <table cellpadding="5">
	                    <tr><td align="right">结算结果：</td><td>
	                    	<input type="radio" id="reCheckPayStlStatus4" name="stlStatus" value="4" onclick="setResultPayMerchantSettlementRequired('0')" checked>结算成功
	                  		<input type="radio" name="stlStatus" value="5" onclick="setResultPayMerchantSettlementRequired('1')">结算失败</td></tr>
	                    <tr><td align="right">&nbsp;</td><td><input class="easyui-textbox" type="text" name="remark" id="setResultPayMerchantSettlementRemark"
					              validType="length[0,300]" invalidMessage="内容请控制在300个字" data-options="multiline:true"
					              style="width:200px;height:70px"/></td></tr>
	                </table>
	            </form>
	        </div>
	        <div data-options="region:'south',border:false" style="text-align:right;padding:5px 0 0;">
	            <a class="easyui-linkbutton" data-options="iconCls:'icon-ok'" href="javascript:void(0)" 
	            	onclick="$('#setResultPayMerchantSettlementForm').submit()" style="width:80px">确定</a>
	            <a class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" href="javascript:void(0)" 
	                onclick="$('#setResultPayMerchantSettlementWindow').window('close')" style="width:80px">取消</a>
	        </div>
	    </div>
	</div>
</div>
<script type="text/javascript">
$('#payMerchantSettlementList').datagrid({
    onLoadError: function(data){
        topCenterMessage('<%=JWebConstant.ERROR %>','数据加载失败！');
    },
    onLoadSuccess: function(data){
        $(this).datagrid('fixRownumber');//行号自适应宽度
        //循环判断操作为新增的不能选择
        for (var i = 0; i < data.rows.length; i++) {
            //根据operate让某些行不可选
            if (data.rows[i].stlStatus == "1" || data.rows[i].stlStatus == "2" ||data.rows[i].stlStatus == "4") {
                $("input[type='checkbox']")[i + 1].disabled = true;
            }
        }
        $("#getSettlementTotalMoneyId").html("交易总笔数："+data.totalApplyCount+"，交易总额："+(parseFloat(data.totalApplyAmt)*0.01).toFixed(2)
        	+"元，退款总笔数:"+data.totalRefundCount+"，退款总额："+(parseFloat(data.totalRefundAmt)*0.01).toFixed(2)+"元，手续费总额："
        	+(parseFloat(data.totalFee)*0.01).toFixed(2)+"元，应结算总金额:"+(parseFloat(data.totalNetreAmt)*0.01).toFixed(2)+"元");
    },
    onClickRow: function(rowIndex, rowData) {  
        if (rowData.stlStatus == "1" || rowData.stlStatus == "2" ||rowData.stlStatus == "4") {
            $("#payMerchantSettlementList").datagrid('uncheckRow',rowIndex);
        }
    },
    onCheckAll: function(rows) {  
        $("input[type='checkbox']").each(function(index, el) {  
            if (el.disabled) {  
                $("#payMerchantSettlementList").datagrid('uncheckRow', index - 1);
            }  
        });
    }
});
function formatPayMerchantSettlementOperator(val,row,index){
     var tmp=
     	<%if(actionDetail != null){//角色判断%>
        "<a href=\"javascript:detailPayMerchantSettlementPageOpen('"+row.stlMerId+"','"+row.stlFromTime+"','"+row.stlToTime+"')\"><%=actionDetail.name %></a>&nbsp;&nbsp;"+
        <%}%>
        <%if(actionReceiveDetail != null){//角色判断%>
        "<a href=\"javascript:detailReceiveSettlementPageOpen('"+row.stlMerId+"','"+row.stlFromTimeReceive+"','"+row.stlToTimeReceive+"')\"><%=actionReceiveDetail.name %></a>&nbsp;&nbsp;"+
        <%}%>
        <%if(actionStlReCheck != null){//角色判断%>
        (row.stlStatus=="1"?"<a href=\"javascript:reCheckPayMerchantSettlementWindowOpen("+index+")\"><%=actionStlReCheck.name %></a>&nbsp;&nbsp;":"")+
        <%}%>
        <%if(actionSetResult != null){//角色判断%>
        (row.stlStatus=="2"?"<a href=\"javascript:setResultPayMerchantSettlementWindowOpen('"+row.stlId+"')\"><%=actionSetResult.name %></a>":"")+
        <%}%>
        "";
     return tmp;
}
$('#manualSettlementRunDate').datebox().datebox('calendar').calendar({
	validator: function(date){
		return true;
		var now = new Date();
		var d2 = new Date(now.getFullYear(), now.getMonth(), now.getDate());
		return date<=d2;
	}
});
$('#manualSettlementRunForm').form({
    url:'<%=path %>/manualSettlementRun.htm',
    onSubmit: function(){
    	var f = $(this).form('validate');
    	if(f)$('#manualSettlementRunWindow').window('close');
       	return f;
    },
    success:function(data){
        if(data=='<%=JWebConstant.OK %>'){
           topCenterMessage('<%=JWebConstant.OK %>','提交成功，开始跑批！');
        } else topCenterMessage('<%=JWebConstant.ERROR %>',data);
    }
});
$('#reCheckPayMerchantSettlementForm').form({
    url:'<%=path %>/reCheckSettlement.htm',
    onSubmit: function(){
    	var f = $(this).form('validate');
    	if(f)$('#reCheckPayMerchantSettlementWindow').window('close');
       	return f;
    },
    success:function(data){
    	$('#payMerchantSettlementList').datagrid('reload');
        if(data=='<%=JWebConstant.OK %>'){
           topCenterMessage('<%=JWebConstant.OK %>','复审成功！');
        } else topCenterMessage('<%=JWebConstant.ERROR %>',data);
    }
});
$('#setResultPayMerchantSettlementForm').form({
    url:'<%=path %>/setResultSettlement.htm',
    onSubmit: function(){
    	var f = $(this).form('validate');
    	if(f)$('#setResultPayMerchantSettlementWindow').window('close');
       	return f;
    },
    success:function(data){
    	$('#payMerchantSettlementList').datagrid('reload');
        if(data=='<%=JWebConstant.OK %>'){
           topCenterMessage('<%=JWebConstant.OK %>','设置成功！');
        } else topCenterMessage('<%=JWebConstant.ERROR %>',data);
    }
});
function reCheckPayMerchantSettlementWindowOpen(index){
    $('#reCheckPayMerchantSettlementWindow').window('open');
    $('#reCheckPayMerchantSettlementForm').form('clear');
    document.getElementById('reCheckPayStlStatus2').checked=true;
    $('#reCheckPayMerchantSettlementRemark').textbox({required:false});
    var node = $('#payMerchantSettlementList').treegrid('getRows')[index];
    document.getElementById('reCheckPayMerchantSettlementStlId0').value=node.stlId;
    $('#reCheckPayMerchantSettlementStlId').text(node.stlId);
    $('#reCheckPayMerchantSettlementStlMerId').text(node.stlMerId);
    $('#reCheckPayMerchantSettlementStoreName').text(node.storeName);
    $('#reCheckPayMerchantSettlementStlFromTime').text(node.stlFromTime);
    $('#reCheckPayMerchantSettlementStlToTime').text(node.stlToTime);
    $('#reCheckPayMerchantSettlementStlApplystlCount').text(node.stlApplystlCount);
    $('#reCheckPayMerchantSettlementStlTotalordamt').text(node.stlTotalordamt);
    $('#reCheckPayMerchantSettlementStlFee').text(node.stlFee);
    $('#reCheckPayMerchantSettlementStlNetrecamt').text(node.stlNetrecamt);
    $('#reCheckPayMerchantSettlementCustSetPeriod').text(node.custSetPeriod);
    $('#reCheckPayMerchantSettlementCustStlTimeSet').text(node.custStlTimeSet);
    $('#reCheckPayMerchantSettlementDepositBankCode').text(node.depositBankCode);
    $('#reCheckPayMerchantSettlementDepositBankBrchName').text(node.depositBankBrchName);
    $('#reCheckPayMerchantSettlementCustBankDepositName').text(node.custBankDepositName);
    $('#reCheckPayMerchantSettlementCustStlBankAcNo').text(node.custStlBankAcNo);
    $('#reCheckPayMerchantSettlementStlApplTime').text(node.stlApplTime);
    $('#reCheckPayMerchantSettlementStlStatus').text(payMerchantSettlementStatusMap.get(node.stlStatus));
    $('#reCheckPayMerchantSettlementStlApplicants').text(node.stlApplicants);
    $('#reCheckPayMerchantSettlementRemarkInfo').text(node.remark);
}
function setResultPayMerchantSettlementWindowOpen(stlId){
	$('#setResultPayMerchantSettlementWindow').window('open');
	$('#setResultPayMerchantSettlementForm').form('clear');
	document.getElementById('setResultPayMerchantSettlementStlId').value=stlId;
	$('#setResultPayMerchantSettlementRemark').textbox('setValue','');
    document.getElementById('reCheckPayStlStatus4').checked=true;
    $('#setResultPayMerchantSettlementRemark').textbox({required:false});
}
function searchPayMerchantSettlementList(){
    $('#payMerchantSettlementList').datagrid('load',{
          stlId:$('#searchPayMerchantSettlementStlId').val(),
          stlMerId:$('#searchPayMerchantSettlementStlMerId').val(),
          storeName:$('#searchPayMerchantSettlementStoreName').val(),
          stlFromTime:$('#searchPayMerchantSettlementStlFromTime').datebox('getValue'),
          stlToTime:$('#searchPayMerchantSettlementStlToTime').datebox('getValue'),
          stlApplDateStart:$('#searchPayMerchantSettlementStlApplDateStart').datebox('getValue'),
          stlApplDateEnd:$('#searchPayMerchantSettlementStlApplDateEnd').datebox('getValue'),
          stlStatus:$('#searchPayMerchantSettlementStlStatus').combobox('getValue'),
          searchCustSetPeriod:$('#searchPayMerchantSettlementCustSetPeriod').combobox('getValue'),
          settlementWay:$('#searchPayMerchantSettlementSettlementWay').combobox('getValue')
    });  
}
function manualSettlementRunWindowOpen(){
    $('#manualSettlementRunForm').form('clear');
    $('#manualSettlementRunWindow').window('open');
}
//结算申请
function stlApplySelectSubmit(){
	var checkedItems = $('#payMerchantSettlementList').datagrid('getChecked');
	var stlIds = '';
	$.each(checkedItems, function(index, item){
		if(item.stlStatus=='0')stlIds = stlIds + ',' + item.stlId;
	});
	if(stlIds.indexOf(',')==0)stlIds = stlIds.substring(1,stlIds.length);
	else {
		topCenterMessage('<%=JWebConstant.ERROR %>','未选择记录或选择记录无效');
		return;
	}
	$.messager.confirm('提示', '申请提交<br/>', function(r){
        if(!r)return;
	    $('#payMerchantSettlementList').datagrid('loading');
	    try{
	        $.post('<%=path %>/applyPayMerchantSettlement.htm',
	            {stlIds:stlIds},
	            function(data){
	                $('#payMerchantSettlementList').datagrid('reload');
	                if(data=='<%=JWebConstant.OK %>'){
	                    topCenterMessage('<%=JWebConstant.OK %>','申请成功！');
	                } else topCenterMessage('<%=JWebConstant.ERROR %>',data);
	            },
	           'text'
	        );
	    }catch(e){alert(e);}
    });
}
//重新发起结算
function stlReApplySelectSubmit(){
	var checkedItems = $('#payMerchantSettlementList').datagrid('getChecked');
	var stlIds = '';
	$.each(checkedItems, function(index, item){
		if(item.stlStatus=='3'||item.stlStatus=='5')stlIds = stlIds + ',' + item.stlId;
	});
	if(stlIds.indexOf(',')==0)stlIds = stlIds.substring(1,stlIds.length);
	else {
		topCenterMessage('<%=JWebConstant.ERROR %>','未选择记录或选择记录无效');
		return;
	}
	$.messager.confirm('提示', '重新结算申请提交<br/>', function(r){
        if(!r)return;
	    $('#payMerchantSettlementList').datagrid('loading');
	    try{
	        $.post('<%=path %>/reApplyPayMerchantSettlement.htm',
	            {stlIds:stlIds},
	            function(data){
	                $('#payMerchantSettlementList').datagrid('reload');
	                if(data=='<%=JWebConstant.OK %>'){
	                    topCenterMessage('<%=JWebConstant.OK %>','重新结算申请成功！');
	                } else topCenterMessage('<%=JWebConstant.ERROR %>',data);
	            },
	           'text'
	        );
	    }catch(e){alert(e);}
    });
}
function reCheckPayMerchantSettlementRemarkRequired(flag){
	if(flag == '0'){
		$('#reCheckPayMerchantSettlementRemark').textbox({
			required:false
	    });
	} else {
	 	$('#reCheckPayMerchantSettlementRemark').textbox({
			required:true
	    });
	    $('#reCheckPayMerchantSettlementRemark').textbox('textbox').focus();
	}
}
function setResultPayMerchantSettlementRequired(flag){
	if(flag == '0'){
		$('#setResultPayMerchantSettlementRemark').textbox({
			required:false
	    });
	} else {
	 	$('#setResultPayMerchantSettlementRemark').textbox({
			required:true
	    });
	    $('#setResultPayMerchantSettlementRemark').textbox('textbox').focus();
	}
}
</script>
