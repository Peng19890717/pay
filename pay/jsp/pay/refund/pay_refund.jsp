<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
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
JWebAction actionSearch = ((JWebAction)user.actionMap.get("puPiSzn2Q3rKTOP1"));
JWebAction actionAdd = ((JWebAction)user.actionMap.get("ACTION_ID_ADD"));
JWebAction actionDetail = ((JWebAction)user.actionMap.get("puQhdex2Q3rKTOL1"));
JWebAction actionOrderDetail = ((JWebAction)user.actionMap.get("puQhdex2Q3rKTOL2"));
JWebAction actionSetBankstsResult = ((JWebAction)user.actionMap.get("pz4VHDZ2Q3rKTP31"));
String merno = request.getParameter("merno");
String prdordno = request.getParameter("oriprdordno");
%>
<script type="text/javascript">
var detailPayRefundPageTitle="退款详情";
var detailPayOrderPageTitle="订单详情";
$(document).ready(function(){});

function formatBanksts(data,row, index){
	if(data=="00"){
		return "处理中";
	}else if(data=="01"){
		return "成功";
	}else if(data=="02"){
		return "失败";
	}else{
		return "";
	}
}
function formatStlsts(data,row, index){
	if(data=="0"){
		return "未结算";
	}else if(data=="1"){
		return "已清算";
	}else if(data=="2"){
		return "已结算";
	}else{
		return "";
	}
}
</script>
<div class="easyui-layout" data-options="fit:true">
	 <div data-options="region:'center'">
	<table id="payRefundList" style="width:100%;height:100%" rownumbers="true" pagination="true"
	    fit="true" data-options="border:false,collapsible:true,autoRowHeight:true,pageSize:20,singleSelect:true,url:'<%=path 
	        %>/payRefund.htm?flag=0',method:'post',toolbar:'#payRefundListToolBar'">
	       <thead>
	        <tr>
	           <th field="refordno" width="10%" align="left" sortable="true">退款订单号</th>
	           <th field="oripayordno" width="10%" align="left" sortable="true">支付订单号</th>
	           <th field="oriprdordno" width="10%" align="left" sortable="true">商品订单号</th>
	           <th field="merno" width="7%" align="left" sortable="true">商户编号</th>
	           <th field="rfamt" width="7%" align="left" sortable="true">退款金额</th>
	           <th field="banksts" width="5%" align="left" sortable="true" formatter="formatBanksts">退款状态</th>
	           <th field="stlsts" width="5%" align="left" sortable="true" formatter="formatStlsts">结算状态</th>
	           <th field="custId" width="10%" align="left" sortable="true">用户编号</th>
	           <th field="rfordtime" width="10%" align="left" sortable="true">退款时间</th>
	           <th field="operation" data-options="formatter:formatPayRefundOperator" width="15%" align="left">操作</th>
	           <th field="rfsake" width="12%" align="left" sortable="true">备注</th>
	       </tr>
	       </thead>
	</table>
	</div>
	
	<div id="setPayRefundBankstsResultWindow" class="easyui-window" title="退款结果设置" 
	    	data-options="iconCls:'icon-edit',closed:true,modal:true" style="width:400px;height:200px;padding:5px;">
	    <div class="easyui-layout" data-options="fit:true">
	        <div data-options="region:'center'">
	            <form id="setResultPayRefundBankstsForm" method="post">
	            	<input type="hidden" id="setResultPayRefundBankstsPayordnoId" name="refordno">
	                <table cellpadding="5">
	                    <tr><td align="right">退款结果：</td><td>
	                    	<input type="radio" name="banksts" value="01" onclick="setResultPayRefundBankstsRequired('0')" checked>退款成功
	                  		<input type="radio" name="banksts" value="02" onclick="setResultPayRefundBankstsRequired('1')">退款失败</td></tr>
	                    <tr><td align="right">&nbsp;</td><td><input class="easyui-textbox" type="text" name="rfsake" id="setResultPayRefundBankstsRemark"
					              validType="length[0,50]" invalidMessage="内容请控制在50个字" data-options="multiline:true"
					              style="width:200px;height:70px"/></td></tr>
	                </table>
	            </form>
	        </div>
	        <div data-options="region:'south',border:false" style="text-align:right;padding:5px 0 0;">
	            <a class="easyui-linkbutton" data-options="iconCls:'icon-ok'" href="javascript:void(0)" 
	            	onclick="$('#setResultPayRefundBankstsForm').submit()" style="width:80px">确定</a>
	            <a class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" href="javascript:void(0)" 
	                onclick="$('#setPayRefundBankstsResultWindow').window('close')" style="width:80px">取消</a>
	        </div>
	    </div>
	</div>
	
	<div id="payRefundListToolBar" data-options="border:false" style="text-align:left;padding:5px 5px 5px 5px;">
		商户编号<input type="text" id="searchPayRefundCustId" name="searchPayRefundCustId" class="easyui-textbox" value=""  style="width:130px"/>
		状态<select class="easyui-combobox" panelHeight="auto" id="searchPayRefundBanksts" data-options="editable:false" name="searchPayRefundBanksts" style="width:120px">
	   	   <option value="">请选择</option>
	          <option value="00">退款处理中</option>
	          <option value="01">退款成功</option>
	          <option value="02">退款失败</option>
	    </select>
		退款日期<input class="easyui-datebox" style="width:100px" id="searchPayRefundRfreqdateStart" name="searchPayRefundRfreqdateStart" data-options="editable:false" />~
			<input class="easyui-datebox" style="width:100px" id="searchPayRefundRfreqdateEnd" name="searchPayRefundRfreqdateEnd" data-options="editable:false" />
		退款订单号<input type="text" id="searchPayRefundRefordno" name="searchPayRefundRefordno" class="easyui-textbox" value=""  style="width:130px"/>
		商品订单号<input type="text" id="searchPayRefundOriprdordno" name="searchPayRefundOriprdordno" class="easyui-textbox" 
				    value="<%=prdordno==null?"":prdordno %>"  style="width:130px"/>
	    <%if(actionSearch != null){//角色判断%>
	    <a href="javascript:searchPayRefundList()" class="easyui-linkbutton" iconCls="icon-search"><%=actionSearch.name %></a>
	    <%} %>
	    <%if(actionAdd != null){//角色判断%>
	    <a href="javascript:addPayRefundWindowOpen()" class="easyui-linkbutton"
	        iconCls="icon-add"><%=actionAdd.name %></a>
	    <%} %>
	</div>
	<div data-options="region:'south',border:false" style="text-align:left;margin-left:7px;margin-top:5px;margin-bottom:5px;">
		<%if(actionSearch != null){//角色判断%>
	    <a href="javascript:payRefundExportExcel()" class="easyui-linkbutton" iconCls="icon-config">导出Excel</a>
	    <%} %>
	</div>
</div>

<script type="text/javascript">
$('#payRefundList').datagrid({
	onBeforeLoad: function(param){
		<%if(prdordno!=null){%>
		param.flag='0';
        param.oriprdordno=$('#searchPayRefundOriprdordno').val();
        <%}%>
    },
    onLoadError: function(data){
        topCenterMessage('<%=JWebConstant.ERROR %>','数据加载失败！');
    },
    onLoadSuccess: function(data){
        $(this).datagrid('fixRownumber');//行号自适应宽度
    }
});
function payRefundExportExcel(){
	window.location.href="<%=path %>/payRefundExportExcel.htm?"+
		"custId="+$('#searchPayRefundCustId').val()+
		"&banksts="+$('#searchPayRefundBanksts').combobox('getValue')+
		"&rfreqdateStart="+$('#searchPayRefundRfreqdateStart').datebox('getValue')+
		"&rfreqdateEnd="+$('#searchPayRefundRfreqdateEnd').datebox('getValue')+
		"&refordno="+$('#searchPayRefundRefordno').val()+
		"&oriprdordno="+$('#searchPayRefundOriprdordno').val()
		;
}
function formatPayRefundOperator(val,row,index){
     var tmp=
         <%if(actionDetail !=null ){%>
         "<a href=\"javascript:detailPayRefundPageOpen('"+row.refordno+"')\"><%=actionDetail.name %></a>&nbsp;"+
         <%}%>
         <%if(actionOrderDetail !=null ){%>
         "<a href=\"javascript:detailPayOrderPageOpen('"+row.oripayordno+"')\">&nbsp;&nbsp;<%=actionOrderDetail.name %></a>&nbsp;&nbsp;"+
         <%}%>
         <%if(actionSetBankstsResult != null){%>
         (row.banksts=="00"?"<a href=\"javascript:setPayRefundBankstsResultWindowOpen('"+row.refordno+"')\"><%=actionSetBankstsResult.name %></a>":"")+
         <%}%>
         "";
     return tmp;
}
//向后台提交的数据
function searchPayRefundList(){        
    $('#payRefundList').datagrid('load',{  
          custId:$('#searchPayRefundCustId').val(),//用户编号
          banksts:$('#searchPayRefundBanksts').combobox('getValue'),//订单状态
          rfreqdateStart:$('#searchPayRefundRfreqdateStart').datebox('getValue'),//退款日期始
          rfreqdateEnd:$('#searchPayRefundRfreqdateEnd').datebox('getValue'),//开始日期止
          refordno:$('#searchPayRefundRefordno').val(),//退款订单号
          oriprdordno:$('#searchPayRefundOriprdordno').val()//商品订单号
    });  
}
//跳转到订单详情页面
function detailPayOrderPageOpen(payordno){
    openTab('detailPayOrderPage',detailPayOrderPageTitle,'<%=path %>/payOrderDetail.htm?payordno='+payordno);
}
//跳转到退款详情页面
function detailPayRefundPageOpen(refordno){
	var tabTmp = $('#mainTabs'),title='退款详情';
	if(tabTmp.tabs('exists',title))tabTmp.tabs('close',title);
	tabTmp.tabs('add',{id:'tabTest1',title: title,closable: true,border:false});
    $('#tabTest1').panel('refresh','<%=path %>/payRefundDetail.htm?refordno='+refordno);
    tabTmp.tabs('select',title);
    openTab('detailPayRefundPage',detailPayRefundPageTitle,'<%=path %>/payRefundDetail.htm?refordno='+refordno);
}
//跳转到订单详情页面
function setResultPayRefundBankstsWindowOpen(payordno){
    openTab('detailPayOrderPage',detailPayOrderPageTitle,'<%=path %>/payOrderDetail.htm?payordno='+payordno);
}
//设置退款结果
$('#setResultPayRefundBankstsForm').form({
    url:'<%=path %>/setResultRefund.htm',
    onSubmit: function(){
    	var f = $(this).form('validate');
    	if(f)$('#setPayRefundBankstsResultWindow').window('close');
       	return f;
    },
    success:function(data){
    	$('#setResultPayRefundBankstsForm').form('clear');
    	$('#payRefundList').datagrid('reload');
        if(data=='<%=JWebConstant.OK %>'){
           topCenterMessage('<%=JWebConstant.OK %>','设置成功！');
        } else topCenterMessage('<%=JWebConstant.ERROR %>',data);
    }
});
function setPayRefundBankstsResultWindowOpen(refordno){
	document.getElementById('setResultPayRefundBankstsPayordnoId').value=refordno;
    $('#setPayRefundBankstsResultWindow').window('open');
}
function setResultPayRefundBankstsRequired(flag){
	//退款成功
	if(flag == '0'){
		$('#setResultPayRefundBankstsRemark').textbox({
			required:false
	    });
	} else {
		//退款失败
	 	$('#setResultPayRefundBankstsRemark').textbox({
			required:true
	    });
	    $('#setResultPayRefundBankstsRemark').textbox('textbox').focus();
	}
}
</script>