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
JWebAction actionSearch = ((JWebAction)user.actionMap.get("pMgJS2A2Q3rKTOL1"));
//冻结资金
JWebAction actionAdd = ((JWebAction)user.actionMap.get("pMaWbo22Q3rKTOQ1"));
//解冻资金
JWebAction actionUpdate = ((JWebAction)user.actionMap.get("pMaWbo22Q3rKTOQ2"));
//解冻明细
JWebAction actionUnFrozenDetail = ((JWebAction)user.actionMap.get("J9S961UM3JLXPT2Q61"));
%>
<script type="text/javascript">
var payAccProfileFrozenListPageTitle="冻结资金";
var payAccProfileFrozenAddPageTitle="资金冻结";
var payAccProfileFrozenDetailPageTitle="payAccProfileFrozen详情";
var payAccProfileFrozenUpdatePageTitle="资金解冻";
$(document).ready(function(){
});
</script>
<div class="easyui-layout" data-options="fit:true">
<div data-options="region:'center'">
	<table id="payAccProfileFrozenList" style="width:150%;height:100%" rownumbers="true" pagination="true"
	    fit="true" data-options="border:false,collapsible:true,autoRowHeight:true,pageSize:20,singleSelect:true,url:'<%=path 
	        %>/payAccProfileFrozen.htm?flag=0',method:'post',toolbar:'#payAccProfileFrozenListToolBar'">
	       <thead>
	        <tr>
	           <th field="id" width="9%" align="left">冻结编号</th>
	           <th field="accType" width="3%" align="left" sortable="true" formatter="format_accType">客户类型</th>
	           <th field="accNo" width="4%" align="left" sortable="true">客户编号</th>
	           <th field="storeName" width="15%" align="left" sortable="true">客户名称</th>
	           <th field="amt" width="5%" align="left" sortable="true">初始冻结金额</th>
	           <th field="curAmt" width="5%" align="left" sortable="true">当前冻结金额</th>
	           <th field="beginTime" width="8%" align="left" sortable="true">冻结开始时间</th>
	           <th field="endTime" width="8%" align="left" sortable="true">冻结结束时间</th>
	           <th field="status" width="3%" align="left" sortable="true" formatter="format_status">状态</th>
	           <th field="optUser" width="4%" align="left" sortable="true">最后操作人</th>
	           <th field="createTime" width="8%" align="left" sortable="true">记录创建时间</th>
	           <th field="updateTime" width="8%" align="left" sortable="true">最后操作时间</th>
	           <th field="operation" data-options="formatter:formatPayAccProfileFrozenOperator" width="8%" align="left">操作</th>
	           <th field="remark" width="15%" align="left" sortable="true">备注</th>
	       </tr>
	       </thead>
	</table>
</div>
<div id="payAccProfileFrozenListToolBar" data-options="border:false" style="text-align:left;padding:5px 5px 5px 5px;">
    客户类型
       <select class="easyui-combobox" panelHeight="auto" id="searchPayAccProfileFrozenAccType" 
			data-options="editable:false" name="searchPayAccProfileFrozenAccType"  style="width:130px">
	    		   <option value="">请选择</option>
	               <option value="0">个人</option>
	               <option value="1">商户</option>
	    </select>
    客户编号<input type="text" id="searchPayAccProfileFrozenAccNo" name="searchPayAccProfileFrozenAccNo" class="easyui-textbox" style="width:130px"/>
    登记时间
    	<input class="easyui-datebox" style="width:100px"  data-options="editable:false" id="searchPayAccProfileFrozenCreateTimeStart" name="searchPayAccProfileFrozenCreateTimeStart"/>
 		~<input class="easyui-datebox" style="width:100px" data-options="editable:false" id="searchPayAccProfileFrozenCreateTimeEnd" name="searchPayAccProfileFrozenCreateTimeEnd"/>
    状态
     <select class="easyui-combobox" panelHeight="auto" id="searchPayAccProfileFrozenStatus" 
			data-options="editable:false" name="searchPayAccProfileFrozenStatus"  style="width:130px">
	    		   <option value="">请选择</option>
	               <option value="0">冻结</option>
	               <option value="1">解冻</option>
	               <option value="2">过期</option>
	    </select>
    <%if(actionSearch != null){//角色判断%>
    <a href="javascript:searchPayAccProfileFrozenList()" class="easyui-linkbutton" iconCls="icon-search"><%=actionSearch.name %></a>
    <%} %>
    <%if(actionAdd != null){//角色判断%>
    <a href="javascript:addPayAccProfileFrozenPageOpen()" class="easyui-linkbutton"
        iconCls="icon-add">资金冻结</a>
    <%} %>
    
</div>
<div data-options="region:'south',border:false" style="text-align:left;margin-left:7px;margin-top:5px;margin-bottom:5px;">
	<span id="payAccProfileFrozenGetTotalMoneyId">&nbsp;</span>
</div>
<div id="payAccProfileFrozenWindow" class="easyui-window" title="资金解冻" 
    data-options="iconCls:'icon-edit',closed:true,modal:true" style="width:400px;height:260px;padding:5px;">
    <div class="easyui-layout" data-options="fit:true">
        <div data-options="region:'center'">
            <form id="payAccProfileFrozenForm" method="post">
            	<input type="hidden" name="updateId" id="setPayAccProfileFrozenId">
                <table cellpadding="5">
                    <tr><td align="right">客户编号：</td><td><span id="accNo"></span></td></tr>
                    <tr><td align="right">当前冻结：</td><td><span id="curAmt"></span></td></tr>
                    <tr><td align="right">解冻金额：</td><td>
                    <input class="easyui-numberbox" type="text" id="accUnfrozenMoney" name="money" missingMessage="请输入解冻金额"
                                data-options="required:true" precision="2" max="99999999999"/>&nbsp;&nbsp;元
                    </td></tr>
                    <tr><td align="right">&nbsp;</td><td><input class="easyui-textbox" type="text" name="remark" id="payAccProfileFrozenRemark"
				    	validType="length[0,150]" invalidMessage="内容请控制在150个字" data-options="multiline:true" style="width:200px;height:70px"/></td></tr>
                </table>
            </form>
        </div> 
        <div data-options="region:'south',border:false" style="text-align:right;padding:5px 0 0;">
            <a class="easyui-linkbutton" data-options="iconCls:'icon-ok'" href="javascript:void(0)" 
            	id="payAccProfileFrozenSubmit" style="width:80px" onclick="$('#payAccProfileFrozenForm').submit()">确定</a>
            <a class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" href="javascript:void(0)" 
                onclick="$('#payAccProfileFrozenWindow').window('close')" style="width:80px">取消</a>
        </div>
    </div>
</div>
<div id="payAccProfileFrozenCheckWindow" class="easyui-window" title="审核"
    data-options="iconCls:'icon-edit',closed:true,modal:true" style="width:400px;height:200px;padding:5px;">
    <div class="easyui-layout" data-options="fit:true">
        <div data-options="region:'center'">
            <form id="payAccProfileFrozenCheckForm" method="post">
            	<input type="hidden" name="id" id="setPayAccProfileFrozenCheckId">
                <table cellpadding="5">
                    <tr><td align="right">审核结果：</td><td>
                    	<input type="radio" name="status" value="0" checked>通过
                  		<input type="radio" name="status" value="4">不通过</td></tr>
                    <tr><td align="right">&nbsp;</td><td><input class="easyui-textbox" type="text" name="remark" id="payAccProfileFrozenCheckRemark"
				              validType="length[0,150]" invalidMessage="内容请控制在150个字" data-options="multiline:true"
				              style="width:200px;height:70px"/></td></tr>
                </table>
            </form>
        </div>
        <div data-options="region:'south',border:false" style="text-align:right;padding:5px 0 0;">
            <a class="easyui-linkbutton" data-options="iconCls:'icon-ok'" href="javascript:void(0)" 
            	id="payAccProfileFrozenCheckFormPass" style="width:80px" onclick="$('#payAccProfileFrozenCheckForm').submit()">确定</a>
            <a class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" href="javascript:void(0)" 
                onclick="$('#payAccProfileFrozenCheckWindow').window('close')" style="width:80px">取消</a>
        </div>
    </div>
</div>
</div>
<script type="text/javascript">

//资金解冻
function payAccProfileFrozenWindowOpen(updateId){
	$('#payAccProfileFrozenForm').form('clear');
	document.getElementById('setPayAccProfileFrozenId').value=updateId;
		$.ajax({
			url:"../../detailPayAccProfileFrozen.htm",
			type:"post",
			data:{updateId:updateId},
			success:function(ress){
				if(ress!=null){
					ress=JSON.parse(ress);
					document.getElementById('accNo').innerText=ress.accNo;
					document.getElementById('curAmt').innerText=ress.curAmt;
				}
			}
		});
    $('#payAccProfileFrozenWindow').window('open');
}
//资金冻结审核
function payAccProfileFrozenCheckWindowOpen(checkId){
	$('#payAccProfileFrozenCheckWindow').form('clear');
	$('#payAccProfileFrozenCheckWindow').window('open');
	document.getElementById('setPayAccProfileFrozenCheckId').value=checkId;
	$("input[name='status']").get(0).checked=true;
}
$('#payAccProfileFrozenList').datagrid({
    onLoadError: function(data){
        topCenterMessage('<%=JWebConstant.ERROR %>','数据加载失败！');
    },
    onLoadSuccess: function(data){
        $(this).datagrid('fixRownumber');//行号自适应宽度
        $("#payAccProfileFrozenGetTotalMoneyId").html("冻结总金额："+(parseFloat(data.totalFrozenMoney)*0.01).toFixed(2)
            	+"元");
    }
});
function searchPayAccProfileFrozenList(){
    $('#payAccProfileFrozenList').datagrid('load',{
          accType:$('#searchPayAccProfileFrozenAccType').combobox('getValue'),
          accNo:$('#searchPayAccProfileFrozenAccNo').val(),
          createTimeStart:$('#searchPayAccProfileFrozenCreateTimeStart').datebox('getValue'),
          createTimeEnd:$('#searchPayAccProfileFrozenCreateTimeEnd').datebox('getValue'),
          status:$('#searchPayAccProfileFrozenStatus').combobox('getValue')
    });  
}
function addPayAccProfileFrozenPageOpen(){
    openTab('addPayAccProfileFrozenPage',payAccProfileFrozenAddPageTitle,'<%=path %>/jsp/pay/accprofile/pay_acc_profile_frozen_add.jsp');
}
function detailPayAccProfileFrozenPageOpen(id){
    openTab('detailPayAccProfileFrozenPage',payAccProfileFrozenDetailPageTitle,'<%=path %>/detailPayAccProfileFrozen.htm?id='+id);
}

function removePayAccProfileFrozen(id){
    $.messager.confirm('提示', '确认删除?', function(r){
    if(!r)return;
    $('#testUserList').datagrid('loading');
    try{
        $.post('<%=path %>/removePayAccProfileFrozen.htm',
            {id:id},
            function(data){
                $('#payAccProfileFrozenList').datagrid('reload');
                if(data=='<%=JWebConstant.OK %>'){
                    topCenterMessage('<%=JWebConstant.OK %>','删除成功！');
                } else topCenterMessage('<%=JWebConstant.ERROR %>',data);
            },
           'json'
        );
    }catch(e){alert(e);}
   });
}
function format_accType(data,row, index){
	if(data=="0"){
		return "个人";
	}else if(data=="1"){
		return "商户";
	}
}
function format_status(data,row, index){
	if(data=="0"){
		return "冻结";
	}else if(data=="1"){
		return "解冻";
	}else if(data=="2"){
		return "过期";
	}else if(data=="3"){
		return "未审";
	}else if(data=="4"){
		return "不通过";
	}
}
//更新资金状态
function updatePayAccProfileFrozenStatus(url,accNo,columName,value,info){
    $.messager.confirm('提示', info, function(r){
    if(!r)return;
    $('#testUserList').datagrid('loading');
    try{
        $.post('<%=path %>/'+url,
            {accNo:accNo,columName:columName,value:value},
            function(data){
                $('#payAccProfileFrozenList').datagrid('reload');
                if(data=='<%=JWebConstant.OK %>'){
                    topCenterMessage('<%=JWebConstant.OK %>','操作成功!');
                } else topCenterMessage('<%= JWebConstant.ERROR %>',data);
            },
           'text'
        );
    }catch(e){alert(e);}
   });
}

function formatPayAccProfileFrozenOperator(val,row,index){
	 var tmp=
	        <%if(actionUpdate !=null ){//角色判断%>
	        ((row.status=='0')?
	        ("<a href=\"javascript:payAccProfileFrozenWindowOpen('"+row.id+"')\"><%=actionUpdate.name %></a>&nbsp;&nbsp;&nbsp;&nbsp;")
	        :(row.status=='3'?"<a href=\"javascript:payAccProfileFrozenCheckWindowOpen('"+row.id+"')\"><%="审核" %></a>&nbsp;&nbsp;&nbsp;&nbsp;":""))+
	        <%}%>
	        <%if(actionUnFrozenDetail != null){//角色判断%>
        	"<a href=\"javascript:payAccUnFrozenDetail('"+row.id+"')\"><%=actionUnFrozenDetail.name %></a>"+
        	<%}%>
	        "";
	        if(row.status!="0"&&row.status!="3") //tmp = "";
	        tmp=
	        <%if(actionUnFrozenDetail != null){//角色判断%>
        	"<a href=\"javascript:payAccUnFrozenDetail('"+row.id+"')\"><%=actionUnFrozenDetail.name %></a>"+
        	<%}%>
	        "";
	     return tmp;
}
//跳到解冻明细列表页面 
function payAccUnFrozenDetail(accFrozenId){
	var tabTmp = $('#mainTabs'),title='解冻资金明细';
	/**当前的tab 是否存在如果存在就 将其关闭**/  
	if(tabTmp.tabs('exists', title))$('#mainTabs').tabs('close',title);
    /**添加一个tab标签**/
	tabTmp.tabs('add',{id:'unFrozenDetail',title: title,selected: true,closable: true,border:false});
    $('#unFrozenDetail').panel('refresh','<%=path %>/jsp/pay/accprofile/pay_acc_profile_unfrozen_log.jsp?accFrozenId='+accFrozenId);
}
//解冻操作
$('#payAccProfileFrozenForm').form({
    url:'<%=path %>/updatePayAccProfileFrozen.htm',
    onSubmit: function(){
    	var f = $(this).form('validate');
    	if(f)$('#payAccProfileFrozenWindow').window('close');
       	return f;
    },
    success:function(data){
    	$('#payAccProfileFrozenList').datagrid('reload');
        if(data=='<%=JWebConstant.OK %>'){
           topCenterMessage('<%=JWebConstant.OK %>','解冻成功！');
        } else topCenterMessage('<%=JWebConstant.ERROR %>',data);
    }
});
//审核操作
$('#payAccProfileFrozenCheckForm').form({
    url:'<%=path %>/payAccProfileFrozenCheck.htm',
    onSubmit: function(){
    	var f = $(this).form('validate');
    	if(f)$('#payAccProfileFrozenCheckWindow').window('close');
       	return f;
    },
    success:function(data){
    	$('#payAccProfileFrozenList').datagrid('reload');
        if(data=='<%=JWebConstant.OK %>'){
           topCenterMessage('<%=JWebConstant.OK %>','审核成功！');
        } else topCenterMessage('<%=JWebConstant.ERROR %>',data);
    }
});
</script>
