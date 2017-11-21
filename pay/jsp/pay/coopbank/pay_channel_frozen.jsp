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
JWebAction actionSearch = ((JWebAction)user.actionMap.get("J95E2EBY3JLXPT2PW2"));
JWebAction actionAdd = ((JWebAction)user.actionMap.get("J95EKOO23JLXPT2Q21"));
JWebAction actionUpdate = ((JWebAction)user.actionMap.get("J95EKOO23JLXPT2Q22"));
//资金解冻
JWebAction actionUnFrozen = ((JWebAction)user.actionMap.get("J989ZS0N3JLXPT2PW1"));
//解冻明细
JWebAction actionUnFrozenDetail = ((JWebAction)user.actionMap.get("J98DOYR83JLXPT2QH1"));
%>
<script type="text/javascript">
var payChannelFrozenListPageTitle="渠道资金冻结";
var payChannelFrozenAddPageTitle="添加冻结资金";
//var payChannelFrozenDetailPageTitle="冻结资金详情";
var payChannelFrozenUpdatePageTitle="修改冻结资金";
$(document).ready(function(){});
function formatPayChannelFrozenStatus (data,row,index){
	if(data=="0")return "初始";
	else if(data=="1")return "处理中";
	else if(data=="2")return "完结";
}
function formatAmt(value, row, index) {
    if (row != null) {
    	return parseFloat(value*0.01).toFixed(2);
    }
}
</script>
<table id="payChannelFrozenList" style="width:100%;height:100%" rownumbers="true" pagination="true"
    fit="true" data-options="border:false,collapsible:true,autoRowHeight:true,pageSize:20,singleSelect:true,url:'<%=path 
        %>/payChannelFrozen.htm?flag=0',method:'post',toolbar:'#payChannelFrozenListToolBar'">
       <thead>
        <tr>
           <th field="id" width="8%" align="left" sortable="true">冻结记录编号</th>
           <th field="channel" width="4%" align="left" sortable="true">渠道</th>
           <th field="srcAmt" width="6%" align="left" formatter="formatAmt" sortable="true">初始冻结金额</th>
           <th field="merNos" width="4%" align="left" sortable="true">商户号</th>
           <th field="storeName" width="6%" align="left" sortable="true">商户名称</th>
           <th field="frozenTime" width="5%" align="left" sortable="true">冻结开始时间</th>
           <th field="orderTxamt" width="6%" align="left" formatter="formatAmt" sortable="true">订单金额</th>
           <th field="orderIds" width="6%" align="left" sortable="true">冻结订单号</th>
           <th field="frozenDays" width="6%" align="left" sortable="true">冻结时长</th>
           <th field="status" width="6%" align="left" sortable="true" formatter="formatPayChannelFrozenStatus">处理结果</th>
           <th field="curAmt" width="6%" align="left" formatter="formatAmt" sortable="true">剩余冻结金额</th>
           <th field="salemanId" width="6%" align="left" sortable="true">业务员编号</th>
           <th field="createTime" width="7%" align="left" sortable="true">登记时间</th>
           <th field="remark" width="6%" align="left" sortable="true">备注</th>
           <th field="optId" width="6%" align="left" sortable="true">登记人员</th>
           <th field="operation" data-options="formatter:formatPayChannelFrozenOperator" width="10%" align="left">操作</th>
       </tr>
       </thead>
</table>
<div id="payChannelFrozenListToolBar" data-options="border:false" style="text-align:left;padding:5px 5px 5px 5px;">
    渠道<input type="text" id="searchPayChannelFrozenChannel" name="searchPayChannelFrozenChannel" class="easyui-textbox" value=""  style="width:130px"/>
    商户号<input type="text" id="searchPayChannelFrozenMerNos" name="searchPayChannelFrozenMerNos" class="easyui-textbox" value=""  style="width:130px"/>
    冻结时间<input type="text" id="searchPayChannelFrozenFrozenTimeStart" name="searchPayChannelFrozenFrozenTime" class="easyui-datebox" value=""  style="width:100px"/>
  ~<input type="text" id="searchPayChannelFrozenFrozenTimeEnd" name="searchPayChannelFrozenFrozenTimeEnd" class="easyui-datebox" value=""  style="width:100px"/>      
    冻结订单号<input type="text" id="searchPayChannelFrozenOrderIds" name="searchPayChannelFrozenOrderIds" class="easyui-textbox" value=""  style="width:130px"/>
    处理结果<select class="easyui-combobox" id="searchPayChannelFrozenStatus" name="searchPayChannelFrozenStatus" data-options="editable:false">					           
	           <option value="0">初始</option>
	           <option value="1">处理中</option>
	           <option value="2">完结</option>
	    </select>
    业务员编号<input type="text" id="searchPayChannelFrozenSalemanId" name="searchPayChannelFrozenSalemanId" class="easyui-textbox" value=""  style="width:130px"/>
    <%if(actionSearch != null){//角色判断%>
    <a href="javascript:searchPayChannelFrozenList()" class="easyui-linkbutton" iconCls="icon-search"><%=actionSearch.name %></a>
    <%} %>
    <%if(actionAdd != null){//角色判断%>
    <a href="javascript:addPayChannelFrozenPageOpen()" class="easyui-linkbutton"
        iconCls="icon-add"><%=actionAdd.name %></a>
    <%} %>
</div>
<div id="payChannelUnFrozenWindow" class="easyui-window" title="解冻" 
 data-options="iconCls:'icon-edit',closed:true,modal:true" style="width:450px;height:320px;padding:5px;">
 <div class="easyui-layout" data-options="fit:true">
     <div data-options="region:'center'">
         <form id="payChannelFrozenForm" method="post">
         	<input type="hidden" name="channelUnFrozen" id="setPayChannelUnFrozenId">
             <table cellpadding="5">
                 <tr><td align="right">渠道：</td><td><span id="channelId"></span></td></tr>
                 <tr><td align="right">商户名称：</td><td><span id="storeNameId"></span></td></tr>
                 <tr><td align="right">初始冻结总额：</td><td><span id="srcAmtId"></span></td></tr>
                 <tr><td align="right">剩余冻结金额：</td><td><span id="curAmtId"></span></td></tr>
                 <tr><td align="right">解冻金额：</td><td>
                 <input class="easyui-numberbox" type="text" id="unfrozenMoneyId" name="unfrozenMoney" missingMessage="请输入解冻金额"
                             data-options="required:true" precision="2" max="99999999999"/>&nbsp;&nbsp;元
                 </td></tr>
                 <tr><td align="right">备注：</td><td>
                 <input class="easyui-textbox" type="text" id="remarkId" name="remark" data-options="multiline:true" style="width:240px;height:70px"/>
                 </td></tr>
             </table>
         </form>
     </div> 
     <div data-options="region:'south',border:false" style="text-align:right;padding:5px 0 0;">
         <a class="easyui-linkbutton" data-options="iconCls:'icon-ok'" href="javascript:void(0)" 
         	id="payChannelUnFrozenSubmit" style="width:80px">确定</a>
         <a class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" href="javascript:void(0)" 
             onclick="$('#payChannelUnFrozenWindow').window('close')" style="width:80px">取消</a>
     </div>
</div>
<script type="text/javascript">
$(document).ready(function(){
	$("#payChannelUnFrozenSubmit").click(function(){
		var money = $("#unfrozenMoneyId").val();
		var leftMoney = ($("#curAmtId").html().split("&"))[0].toString();
		if(money=="" || money<=0 || parseFloat(leftMoney) < money){
			topCenterMessage('<%=JWebConstant.ERROR %>','解冻金额不合法！');
			return;
		}else{
			$.ajax({
				url:"../../unFrozenPayChannelFrozen.htm",
  		      	 data:$("#payChannelFrozenForm").serialize(),
  		      	 success: function(data){
       		 		$('#payChannelUnFrozenWindow').window('close'),
       		 		$('#payChannelFrozenList').datagrid('reload');
    			 }
   		 	 });
		}
	});
});
//解冻
function unFrozenPayChannelFrozenWindowOpen(id,channel,storeName,srcAmt,curAmt){
	$('#payChannelUnFrozenWindow').form('clear');
	$("#setPayChannelUnFrozenId").val(id);
	$("#channelId").html(channel);
	$("#storeNameId").html(storeName);
	$("#srcAmtId").html((parseFloat(srcAmt)*0.01).toFixed(2)+"&nbsp;元");
	$("#curAmtId").html((parseFloat(curAmt)*0.01).toFixed(2)+"&nbsp;元");
    $('#payChannelUnFrozenWindow').window('open');
}
$('#payChannelFrozenList').datagrid({
    onLoadError: function(data){
        topCenterMessage('<%=JWebConstant.ERROR %>','数据加载失败！');
    },
    onLoadSuccess: function(data){
        $(this).datagrid('fixRownumber');//行号自适应宽度
    }
});
function formatPayChannelFrozenOperator(val,row,index){
     var tmp=
        <%if(actionUpdate != null){//角色判断%>
        "<a href=\"javascript:updatePayChannelFrozenPageOpen('"+row.id+"')\"><%=actionUpdate.name %></a>&nbsp;&nbsp;&nbsp;"+
        <%}%>
        <%if(actionUnFrozen != null ){//角色判断%>
        (row.curAmt >0?"<a href=\"javascript:unFrozenPayChannelFrozenWindowOpen('"+row.id+"','"+row.channel+"','"+row.storeName+"','"+row.srcAmt+"','"+row.curAmt+"')\"><%=actionUnFrozen.name %></a>&nbsp;&nbsp;&nbsp;":"")+      
        <%}%>
        <%if(actionUnFrozenDetail != null){//角色判断%>
        "<a href=\"javascript:payChannelUnFrozenDetail('"+row.id+"')\"><%=actionUnFrozenDetail.name %></a>"+
        <%}%>
        "";
     return tmp;
}
//跳到解冻明细列表页面 
function payChannelUnFrozenDetail(frozenId){
	var tabTmp = $('#mainTabs'),title='渠道解冻明细';
	/**当前的tab 是否存在如果存在就 将其关闭**/  
	if(tabTmp.tabs('exists', title))$('#mainTabs').tabs('close',title);
    /**添加一个tab标签**/
	tabTmp.tabs('add',{id:'unFrozenDetail',title: title,selected: true,closable: true,border:false});
    $('#unFrozenDetail').panel('refresh','<%=path %>/jsp/pay/coopbank/pay_channel_unfrozen_detail.jsp?frozenId='+frozenId);
}
function searchPayChannelFrozenList(){
    $('#payChannelFrozenList').datagrid('load',{
          channel:$('#searchPayChannelFrozenChannel').val(),
          merNos:$('#searchPayChannelFrozenMerNos').val(),
          frozenTimeStart:$('#searchPayChannelFrozenFrozenTimeStart').datebox('getValue'),
          frozenTimeEnd:$('#searchPayChannelFrozenFrozenTimeEnd').datebox('getValue'),
          orderIds:$('#searchPayChannelFrozenOrderIds').val(),
          status:$('#searchPayChannelFrozenStatus').combobox('getValue'),
          salemanId:$('#searchPayChannelFrozenSalemanId').val()
    });  
}
function addPayChannelFrozenPageOpen(){
    openTab('addPayChannelFrozenPage',payChannelFrozenAddPageTitle,'<%=path %>/jsp/pay/coopbank/pay_channel_frozen_add.jsp');
}
function updatePayChannelFrozenPageOpen(id){
    openTab('updatePayChannelFrozenPage',payChannelFrozenUpdatePageTitle,'<%=path %>/updatePayChannelFrozen.htm?flag=show&id='+id);
}
</script>
