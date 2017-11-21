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
JWebAction actionSearch = ((JWebAction)user.actionMap.get("J4DQTSVQ3JLXPT2PW2"));
JWebAction actionAdd = ((JWebAction)user.actionMap.get("J4DQTSVQ3JLXPT2PW3"));
JWebAction actionDetail = ((JWebAction)user.actionMap.get("J4F0KC013JLXPT2QV1"));
JWebAction actionBatchOpt = ((JWebAction)user.actionMap.get("J4DRSFY93JLXPT2Q91"));
JWebAction actionOpt = ((JWebAction)user.actionMap.get("J4F0KC013JLXPT2QV2"));
JWebAction actionRemove = ((JWebAction)user.actionMap.get("J4F0KC013JLXPT2QV3"));
%>
<script type="text/javascript">
var payChannelRotationListPageTitle="渠道轮询";
var payChannelRotationAddPageTitle="轮询设置";
$(document).ready(function(){});
function formatChannelRotationType(data,row,index) {
	if(data=="0"){
		return "网银";
	} else if(data=="1"){
		return "快捷";
	} else if(data=="2"){
		return "扫码";
	}
}
function formatChannelRotationStatus(data,row,index) {
	if(data=="0"){
		return "停用";
	} else if(data=="1"){
		return "启用";
	}
}
</script>
<div class="easyui-layout" data-options="fit:true">
<div data-options="region:'center'">
<table id="payChannelRotationList" style="width:140%;height:100%" rownumbers="true" pagination="true"
    fit="true" data-options="border:false,collapsible:true,autoRowHeight:true,pageSize:20,singleSelect:true,url:'<%=path 
        %>/payChannelRotation.htm?flag=0',method:'post',toolbar:'#payChannelRotationListToolBar'">
       <thead>
       <tr>
           <!-- <th field="id" width="9%" align="left" sortable="true">id</th> -->
           <th field="type" width="3%" align="left" formatter="formatChannelRotationType">支付方式</th>
           <th field="channelId" width="3%" align="left" >渠道编码</th>
           <th field="channelName" width="4%" align="left" >渠道名称</th>
           <th field="merchantId" width="7%" align="left">渠道账号</th>
           <th field="md5Key" width="9%" align="left">上游md5Key</th>
           <th field="maxSumAmt" width="5%" align="left">最大限额</th>
           <th field="status" width="2%" align="left" formatter="formatChannelRotationStatus">状态</th>
           <th field="createTime" width="8%" align="left" sortable="true">登记时间</th>
           <th field="lastSucUsedTime" width="8%" align="left" sortable="true">最近成功时间</th>
           <th field="lastSucTranAmt" width="6%" align="left" sortable="true">最后一笔成功额</th>
           <th field="batchNo" width="9%" align="left">批次号</th>
           <th field="cancelTime" width="8%" align="left">停用时间</th>
           <th field="sumTranAmt" width="4%" align="left">总额</th>
           <th field="sumSuccessTranAmt" width="4%" align="left">成功额</th>
           <th field="sumTranAccount" width="3%" align="left">总数</th>
           <th field="sumSuccessTranAccount" width="3%" align="left">成功数</th>
           <th field="operation" data-options="formatter:formatPayChannelRotationOperator" width="5%" align="left">操作</th>
           <th field="sumDayCountZfbScan" width="4%" align="left">支付宝笔数</th>
           <th field="sumDayCountWeixinScan" width="4%" align="left">微信笔数</th>
           <th field="sumDayCountQqScan" width="3%" align="left">QQ笔数</th>
           <th field="sumDaySucCountZfbScan" width="4%" align="left">支付宝成功数</th>
           <th field="sumDaySucCountWeixinScan" width="4%" align="left">微信成功数</th>
           <th field="sumDaySucCountQqScan" width="4%" align="left">QQ成功笔数</th>
           <th field="sumDayAmtZfbScan" width="4%" align="left">支付宝额</th>
           <th field="sumDayAmtWeixinScan" width="4%" align="left">微信额</th>
           <th field="sumDayAmtQqScan" width="4%" align="left">QQ额</th>
           <th field="sumDaySucAmtZfbScan" width="4%" align="left">支付宝成功额</th>
           <th field="sumDaySucAmtWeixinScan" width="4%" align="left">微信成功额</th>
           <th field="sumDaySucAmtQqScan" width="4%" align="left">QQ成功额</th>
       </tr>
       </thead>
</table>
</div>
<div id="payChannelRotationListToolBar" data-options="border:false" style="text-align:left;padding:5px 5px 5px 5px;">
    支付方式
    <select id="searchPayChannelRotationType" name="searchPayChannelRotationType" data-option="editable:false" class="easyui-combobox">
        <option value="">请选择</option>
   	 	<option value="2">扫码</option>
<!--    	 	<option value="0">网银</option>
   		<option value="1">快捷</option> -->
    </select>
    渠道编码<input type="text" id="searchPayChannelRotationChannelId" name="searchPayChannelRotationChannelId" class="easyui-textbox" value=""  style="width:130px"/>
    渠道账号<input type="text" id="searchPayChannelRotationMerchantId" name="searchPayChannelRotationMerchantId" class="easyui-textbox" value=""  style="width:130px"/>
    状态<select id="searchPayChannelRotationStatus" name="searchPayChannelRotationStatus" data-option="editable:false" class="easyui-combobox">
    	<option value="">请选择</option>
   	 	<option value="">全部</option>
   		<option value="1">启用</option>
   		<option value="0">停用</option>
    </select>
    交易时间从<input type="text" id="searchPayChannelRotationCreateTimeStart" name="searchPayChannelRotationCreateTimeStart" class="easyui-datebox" value=""  style="width:100px"/>
    到<input type="text" id="searchPayChannelRotationCreateTimeEnd" name="searchPayChannelRotationCreateTimeEnd" class="easyui-datebox" value=""  style="width:100px"/>
    批次号<input type="text" id="searchPayChannelRotationBatchNo" name="searchPayChannelRotationBatchNo" class="easyui-textbox" value=""  style="width:130px"/>
    <%if(actionSearch != null){//角色判断%>
    <a href="javascript:searchPayChannelRotationList()" class="easyui-linkbutton" iconCls="icon-search"><%=actionSearch.name %></a>
    <%} %>
    <%if(actionAdd != null){//角色判断%>
    <a href="javascript:addPayChannelRotationPageOpen()" class="easyui-linkbutton"
        iconCls="icon-add"><%=actionAdd.name %></a>
    <%} %>
    <%if(actionBatchOpt != null){//角色判断%>
    <a href="javascript:payChannelRotationBatchOptWindowOpen()" class="easyui-linkbutton"
        iconCls="icon-config"><%=actionBatchOpt.name %></a>
    <%} %>
    <!-- <select id="rotationRuleTest" name="ruleNo" data-option="editable:false" class="easyui-combobox">
	    <option value="WeiXinScanOrder">WeiXinScanOrder</option>
	   	<option value="WeiXinWapOrder">WeiXinWapOrder</option>
		<option value="ZFBScanOrder">ZFBScanOrder</option>
	</select> -->
</div>
 <div data-options="region:'south',border:false" style="text-align:left;margin-left:7px;margin-top:5px;margin-bottom:5px;">
		<%if(actionSearch != null){//角色判断%>
		<a href="javascript:payChannelRotationListExportExcel()"  class="easyui-linkbutton" iconCls="icon-config">excel导出</a>
		<%} %>
		&nbsp;&nbsp;&nbsp;&nbsp;
		<span id="getpayChannelRotationTotalId"></span>
 </div>
 </div>
<div id="payChannelRotationBatchOptWindow" class="easyui-window" title="批量操作" 
    data-options="iconCls:'icon-config',closed:true,modal:true" style="width:300px;height:200px;padding:5px;">
    <div class="easyui-layout" data-options="fit:true">
        <div data-options="region:'center'">
            <form id="payChannelRotationBatchOptForm" method="post">
                <table cellpadding="5">
                    <tr><td width="50%">&nbsp;</td><td width="50%">&nbsp;</td></tr>
                    <tr>
                        <td align="right">操作类型：</td>
                        <td>
	                        <select id="payChannelRotationBatchOpt" name="status" data-option="editable:false" class="easyui-combobox">
	                        	<option value="0">停用</option>
						    	<option value="1">启用</option>
						   		<option value="2">删除</option>
						    </select>
                        </td>
                    </tr>
                    <tr>
                        <td align="right">批次号：</td>
                        <td><input type="text" id="payChannelRotationBatchNo" name="batchNo" class="easyui-textbox" value="" data-options="required:true" style="width:100px"/></td>
                    </tr>
                </table>
            </form>
        </div>
        <div data-options="region:'south',border:false" style="text-align:right;padding:5px 0 0;">
            <a class="easyui-linkbutton" data-options="iconCls:'icon-ok'" href="javascript:void(0)"
            	onclick="$('#payChannelRotationBatchOptForm').submit()" style="width:80px">确定</a>
            <a class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" href="javascript:void(0)" 
                onclick="$('#payChannelRotationBatchOptWindow').window('close')" style="width:80px">取消</a>
        </div>

    </div>
    
</div>
<script type="text/javascript">
$("#rotationRuleTest").combobox({
	onChange: function (n,o) {
	try{
		var channelInfo = $.ajax({
			url: '<%=path %>/rotationRuleTest.htm',
			data:{ruleTestAmt:120,ruleNo:$('#rotationRuleTest').combobox('getValue')},
			type: 'post',
			dataType: 'json',//发送类型
			async: false,
			cache: false
		}).responseText;//接收类型
		alert(channelInfo);
		}catch(e){alert(e);}
	}
});
$('#payChannelRotationList').datagrid({
    onLoadError: function(data){
        topCenterMessage('<%=JWebConstant.ERROR %>','数据加载失败！');
    },
    onLoadSuccess: function(data){
        $(this).datagrid('fixRownumber');//行号自适应宽度
    }
});
function payChannelRotationBatchOptWindowOpen(){
    $('#payChannelRotationBatchOptForm').form('clear');
    $('#payChannelRotationBatchOptWindow').window('open');
}
$('#payChannelRotationBatchOptForm').form({
    url:'<%=path %>/payChannelRotationOptBatch.htm',
    onSubmit: function(){
    	var f = $(this).form('validate');
    	if(f)$('#payChannelRotationBatchOptWindow').window('close');
       	return f;
    },
    success:function(data){
    	$('#payChannelRotationList').datagrid('reload');
        if(data=='<%=JWebConstant.OK %>'){
           topCenterMessage('<%=JWebConstant.OK %>','处理成功！');
        } else topCenterMessage('<%=JWebConstant.ERROR %>',data);
    }
});
function formatPayChannelRotationOperator(val,row,index){
     var tmp=
        <%if(actionDetail != null){//角色判断%>
        "<a href=\"javascript:detailPayChannelRotationPageOpen('"+row.channelId+"','"+row.merchantId+"')\"><%=actionDetail.name %></a>&nbsp;"+
        <%}%>
        <%if(actionOpt != null){//角色判断%>
        "<a href=\"javascript:updatePayChannelRotationStatusOpen('"+row.id+"','"+row.status+"')\">"+(row.status=="1"?"停用":"启用")+"</a>&nbsp;"+
        <%}%>
        <%if(actionRemove != null){//角色判断%>
        "<a href=\"javascript:removePayChannelRotationWindowOpen('"+row.id+"')\"><%=actionRemove.name %></a>&nbsp;"+
        <%}%>
        "";
     return tmp;
}
function searchPayChannelRotationList(){
    $('#payChannelRotationList').datagrid('load',{
          type:$('#searchPayChannelRotationType').combobox('getValue'),
          channelId:$('#searchPayChannelRotationChannelId').val(),
          merchantId:$('#searchPayChannelRotationMerchantId').val(),
          status:$('#searchPayChannelRotationStatus').combobox('getValue'),
          createTimeStart:$('#searchPayChannelRotationCreateTimeStart').datebox('getValue'),
          createTimeEnd:$('#searchPayChannelRotationCreateTimeEnd').datebox('getValue'),
          batchNo:$('#searchPayChannelRotationBatchNo').val()
    });  
}

// excel导出
function payChannelRotationListExportExcel(){
	$.messager.confirm('提示', '您确认将当前记录导出至excel吗？', function(r){
        if(!r)return;
	    try{
	    	var tmp = "<%=path %>/payChannelRotationListExportExcel.htm?"+
	    	"type="+$('#searchPayChannelRotationType').combobox('getValue')+
			"&channelId="+$('#searchPayChannelRotationChannelId').val()+
	        "&merchantId="+$('#searchPayChannelRotationMerchantId').val()+
	        "&status="+$('#searchPayChannelRotationStatus').datebox('getValue')+
	        "&createTimeStart="+$('#searchPayChannelRotationCreateTimeStart').datebox('getValue')+
	        "&createTimeEnd="+$('#searchPayChannelRotationCreateTimeEnd').datebox('getValue')+
			"&batchNo="+$('#searchPayChannelRotationBatchNo').val();
	        window.location.href= tmp;
	    }catch(e){alert(e);}
    });
}
function addPayChannelRotationPageOpen(){
    openTab('addPayChannelRotationPage',payChannelRotationAddPageTitle,'<%=path %>/jsp/pay/coopbank/pay_channel_rotation_add.jsp');
}
function updatePayChannelRotationPageOpen(id){
    openTab('updatePayChannelRotationPage',payChannelRotationUpdatePageTitle,'<%=path %>/updatePayChannelRotation.htm?flag=show&id='+id);
}
function updatePayChannelRotationStatusOpen(id,status){
    $.messager.confirm('提示', '确认'+(status=='0'?'启用':'停用')+'?', function(r){
    if(!r)return;
    $('#testUserList').datagrid('loading');
    try{
        $.post('<%=path %>/updatePayChannelRotationStatus.htm?status='+(status=='0'?'1':'0'),
            {id:id},
            function(data){
                $('#payChannelRotationList').datagrid('reload');
                if(data=='<%=JWebConstant.OK %>'){
                    topCenterMessage('<%=JWebConstant.OK %>','操作成功！');
                } else topCenterMessage('<%=JWebConstant.ERROR %>',data);
            },
           'json'
        );
    }catch(e){alert(e);}
   });
}
function removePayChannelRotationWindowOpen(id){
    $.messager.confirm('提示', '确认删除?', function(r){
    if(!r)return;
    $('#testUserList').datagrid('loading');
    try{
        $.post('<%=path %>/removePayChannelRotation.htm',
            {id:id},
            function(data){
                $('#payChannelRotationList').datagrid('reload');
                if(data=='<%=JWebConstant.OK %>'){
                    topCenterMessage('<%=JWebConstant.OK %>','删除成功！');
                } else topCenterMessage('<%=JWebConstant.ERROR %>',data);
            },
           'json'
        );
    }catch(e){alert(e);}
   });
}
//跳到渠道轮询日志列表页面 
function detailPayChannelRotationPageOpen(channelId,channelAcc){
	var tabTmp = $('#mainTabs'),title='渠道轮询日志';
	/**当前的tab 是否存在如果存在就 将其关闭**/  
	if(tabTmp.tabs('exists', title))$('#mainTabs').tabs('close',title);
    /**添加一个tab标签**/
	tabTmp.tabs('add',{id:'detailPayChannelRotationList',title: title,selected: true,closable: true,border:false});
    $('#detailPayChannelRotationList').panel('refresh','<%=path %>/jsp/pay/coopbank/pay_channel_rotation_log.jsp?channelId='+channelId+'&channelAcc='+channelAcc);
}

</script>
