<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ page import="util.JWebConstant"%>
<%@ page import="com.jweb.dao.JWebUser"%>
<%@ page import="com.jweb.dao.JWebAction"%>
<%@ page import="com.jweb.service.JWebUserService"%>
<%@ page import="com.pay.contract.service.PayContractService"%>
<%
String path = request.getContextPath();
//登录和权限判断
JWebUser user = (JWebUser)session.getAttribute("user");
if(!JWebUserService.checkUser(user,request)){
%><script>window.location.href='<%=path %>/jsp/jweb/login.jsp'</script><%
    return;
}
JWebAction actionSearch = ((JWebAction)user.actionMap.get("pv6Ilnm2Q3rKTOR2"));
JWebAction actionAdd = ((JWebAction)user.actionMap.get("pv6Ilnm2Q3rKTOR1"));
JWebAction actionRemove = ((JWebAction)user.actionMap.get("ACTION_ID_REMOVE"));
JWebAction actionUpdate = ((JWebAction)user.actionMap.get("pvufgW22Q3rKTOK1"));
JWebAction actionDetail = ((JWebAction)user.actionMap.get("pv7tD5p2Q3rKTOI1"));
JWebAction actionStart = ((JWebAction)user.actionMap.get("pw911ww2Q3rKTOI1"));
JWebAction actionEnd= ((JWebAction)user.actionMap.get("pw911ww2Q3rKTOI2"));
JWebAction actionRemind= ((JWebAction)user.actionMap.get("pwq54jY2Q3rKTOI1"));
java.util.Map expiresMap = new PayContractService().getRemindPayContractExpires();
%>
<script type="text/javascript">
var payContractListPageTitle="合同管理";
var payContractAddPageTitle="添加合同";
var payContractDetailPageTitle="合同详情";
var payContractUpdatePageTitle="修改合同";
$(document).ready(function(){});
function format_bizType(data,row,index) {
	if(data=="00"){
		return "特约商户";
	} else if(data=="A0"){
		return "数字点卡 ";
	} else if(data=="A1"){
		return "教育培训 ";
	} else if(data=="A2"){
		return "网络游戏 ";
	} else if(data=="A3"){
		return "旅游票务 ";
	} else if(data=="A4"){
		return "鲜花礼品 ";
	} else if(data=="A5"){
		return "电子产品 ";
	} else if(data=="A6"){
		return "图书音像";
	} else if(data=="A7"){
		return "会员论坛";
	} else if(data=="A8"){
		return "网站建设";
	} else if(data=="A9"){
		return "软件产品";
	} else if(data=="B0"){
		return "运动休闲";
	} else if(data=="B1"){
		return "彩票";
	} else if(data=="B2"){
		return "影视娱乐";
	} else if(data=="B3"){
		return "日常用品";
	} else if(data=="B4"){
		return "团购";
	} else if(data=="B5"){
		return "信用卡还款";
	} else if(data=="B6"){
		return "交通违章罚款";
	} else if(data=="B7"){
		return "全国公共事业缴费";
	} else if(data=="B8"){
		return "支付宝业务";
	} else if(data=="B9"){
		return "身份证查询";
	} else if(data=="C1"){
		return "购买支付通业务";
	} else if(data=="C2"){
		return "其他";
	} else if(data=="C3"){
		return "卡卡转账";
	} else if(data=="C4"){
		return "ETC业务";
	}
}
function format_pactStatus(data,row,index) {
	if(data=="1"){
		return "正常";
	} else if(data=="2"){
		return "禁用";
	}
}

</script>
<table id="payContractList" style="width:100%;height:100%" rownumbers="true" pagination="true"
    fit="true" data-options="border:false,collapsible:true,autoRowHeight:true,pageSize:20,singleSelect:true,url:'<%=path 
        %>/payContract.htm?flag=0',method:'post',toolbar:'#payContractListToolBar'">
       <thead>
        <tr>
           <th field="seqNo" width="12%" align="left" sortable="true">合同编号</th>
           <th field="pactName" width="12%" align="left" sortable="true">合同名称</th>
           <th field="custId" width="10%" align="left" sortable="true">商户编号</th>
           <th field="storeName" width="12%" align="left" sortable="true">商户名称</th>
           <th field="contractSignMan" width="8%" align="left" sortable="true" formatter="hideNameFormatter">签约人</th>
           <th field="bizType" width="12%" align="left" sortable="true" formatter="format_bizType">合作业务类型</th>
           <th field="pactStatus" width="10%" align="left" sortable="true"  formatter="format_pactStatus">合同状态</th>
           <th field="pactTakeEffDate" width="10%" align="left" sortable="true">生效日期</th>
           <th field="pactLoseEffDate" width="10%" align="left" sortable="true">失效日期</th>
           <th field="lstUptTime" width="10%" align="left" sortable="true">修改日期</th>
           <th field="lstUptOperName" width="8%" align="left" sortable="true">修改人</th>
           <th field="operation" data-options="formatter:formatPayContractOperator" width="10%" align="left">操作</th>
       </tr>
       </thead>
</table>
<div id="payContractListToolBar" data-options="border:false" style="text-align:left;padding:5px 5px 5px 5px;">
	合同编号<input type="text" id="searchPayContractSeqNo" name="searchPayContractSeqNo" class="easyui-textbox" value=""  style="width:130px"/>
	商户编号<input type="text" id="searchPayContractCustId" name="searchPayContractCustId" class="easyui-textbox" value=""  style="width:130px"/>
	状态<select class="easyui-combobox" panelHeight="auto" id="searchPayContractPactStatus" data-options="editable:false" name="searchPayContractPactStatus">
  	           <option value="">全部</option>
               <option value="1">正常</option>
               <option value="2">禁用</option>
		</select>
	失效日期<input class="easyui-datebox" style="width:100px" id="searchPayContractPactLoseEffDateStart" name="searchPayContractPactLoseEffDateStart" data-options="editable:false" />~
			<input class="easyui-datebox" style="width:100px" id="searchPayContractPactLoseEffDateEnd" name="searchPayContractPactLoseEffDateEnd" data-options="editable:false" />
	修改日期<input class="easyui-datebox" style="width:100px" id="searchPayContractLstUptTimeStartStart" name="searchPayContractLstUptTimeStartStart" data-options="editable:false" />~
			<input class="easyui-datebox" style="width:100px" id="searchPayContractLstUptTimeStartEnd" name="searchPayContractLstUptTimeStartEnd" data-options="editable:false" />
			<%if(actionSearch != null){//角色判断%>
		    <a href="javascript:searchPayContractList()" class="easyui-linkbutton" iconCls="icon-search"><%=actionSearch.name %></a>
		    <%} %>
		    <%if(actionAdd != null){//角色判断%>
		    <a href="javascript:addPayContractTab()" class="easyui-linkbutton"
		        iconCls="icon-add"><%=actionAdd.name %></a>
		    <%} %>
		    <%if(actionRemind != null){//角色判断%>
		    <a href="javascript:void(0)" onclick="$('#remindPayContractWindow').window('open')" class="easyui-linkbutton"
		        iconCls="icon-config"><%=actionRemind.name %></a>
		    <%} %>
</div>
<div id="remindPayContractWindow" class="easyui-window" title="合同到期提醒设置"
	data-options="closed:true,minimizable:false,collapsible:false,maximizable:false,resizable:false,iconCls:'icon-edit',modal:true" 
 	style="width:400px;padding-top:15px;padding-left:35px;padding-bottom:15px;">
     <form id="remindPayContractForm" method="post">
         <table cellpadding="5">
             <tr>
                 <td align="right">提醒规则:</td>
                 <td>
	                 <select id="remindPayContractExpires" name="expires" style="width:150px;">
				   		<option value="30" <%="30".equals(expiresMap.get("expires"))?"selected":"" %>>提前30天 </option>
				   		<option value="60" <%="60".equals(expiresMap.get("expires"))?"selected":"" %>>提前60天 </option>	
				   		<option value="90" <%="90".equals(expiresMap.get("expires"))?"selected":"" %>>提前90天 </option>	 					  
			 		 </select>
                 </td>
             </tr>
             <tr>
                 <td align="right">提醒手机号:</td>
                 <td><input class="easyui-textbox" type="text" id="remindPayContractMobile" name="mobile" style="width:150px;"
                 	value="<%=expiresMap.get("mobile")==null?"":expiresMap.get("mobile") %>"/>多个以“,”分隔</td>
             </tr>
             <tr>
                 <td align="right">邮箱:</td>
                 <td><input class="easyui-textbox" type="text" id="remindPayContractEmail" name="email" style="width:150px;"
                 	value="<%=expiresMap.get("email")==null?"":expiresMap.get("email") %>"/>多个以“,”分隔</td>
             </tr>
              <tr>
                  <td></td>
                  <td>
                  	<a class="easyui-linkbutton" data-options="iconCls:'icon-ok'" href="javascript:void(0)" 
						onclick="$('#remindPayContractForm').submit()" style="width:80px">确定</a>&nbsp;&nbsp;
					<a class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" href="javascript:void(0)" 
			       		onclick="$('#remindPayContractForm').form('clear')" style="width:80px">重置</a><br/><br/>
				  </td>
              </tr>
          </table>
      </form>
</div>
<script type="text/javascript">
$('#payContractList').datagrid({
    onLoadError: function(data){
        topCenterMessage('<%=JWebConstant.ERROR %>','数据加载失败！');
    },
    onLoadSuccess: function(data){
        $(this).datagrid('fixRownumber');//行号自适应宽度
    }
});
function formatPayContractOperator(val,row,index){
     var tmp=
         <%if(actionDetail !=null ){//角色判断%>
         "<a href=\"javascript:detailPayContractPageOpen('"+row.seqNo+"')\"><%=actionDetail.name %></a>&nbsp;&nbsp;"+
         <%}%>
         <%if(actionUpdate != null){//角色判断%>
         "<a href=\"javascript:updatePayContractPageOpen('"+row.seqNo+"')\"><%=actionUpdate.name %></a>&nbsp;&nbsp;"+
         <%}%>
         <%if(actionStart !=null ){//角色判断%>
         (row.pactStatus=="2"?"<a href=\"javascript:startPayContract('"+row.seqNo+"')\"><%=actionStart.name %></a>&nbsp;&nbsp;":"")+
         <%}%>
         <%if(actionEnd !=null ){//角色判断%>
         (row.pactStatus=="1"?"<a href=\"javascript:endPayContract('"+row.seqNo+"')\"><%=actionEnd.name %></a>&nbsp;&nbsp;":"")+
         <%}%>
         "";
     return tmp;
}
function searchPayContractList(){        
    $('#payContractList').datagrid('load',{  
          seqNo:$('#searchPayContractSeqNo').val(),//合同编号
          custId:$('#searchPayContractCustId').val(),//商户编号
          pactStatus:$('#searchPayContractPactStatus').combobox('getValue'),//合同状态
          pactLoseEffDateStart:$('#searchPayContractPactLoseEffDateStart').datebox('getValue'),//修改日期始
          pactLoseEffDateEnd:$('#searchPayContractPactLoseEffDateEnd').datebox('getValue'),//修改日期止
          lstUptTimeStart:$('#searchPayContractLstUptTimeStartStart').datebox('getValue'),//失效日期始
          lstUptTimeEnd:$('#searchPayContractLstUptTimeStartEnd').datebox('getValue')//失效日期止
    });  
}
function updatePayContractPageOpen(seqNo){
    openTab('updatePayContractPage',payContractUpdatePageTitle,'<%=path %>/updatePayContract.htm?flag=show&seqNo='+seqNo);
}
function startPayContract(seqNo){
    $.messager.confirm('提示', '启用确认?', function(r){
    if(!r)return;
    $('#payContractList').datagrid('loading');
    try{
        $.post('<%=path %>/startPayContract.htm',
            {seqNo:seqNo},
            function(data){
                $('#payContractList').datagrid('reload');
                if(data=='<%=JWebConstant.OK %>'){
                    topCenterMessage('<%=JWebConstant.OK %>','操作成功！');
                } else topCenterMessage('<%=JWebConstant.ERROR %>',data);
            },
           'json'
        );
    }catch(e){alert(e);}
   });
}
function endPayContract(seqNo){
    $.messager.confirm('提示', '禁用确认?', function(r){
    if(!r)return;
    $('#payContractList').datagrid('loading');
    try{
        $.post('<%=path %>/endPayContract.htm',
            {seqNo:seqNo},
            function(data){
                $('#payContractList').datagrid('reload');
                if(data=='<%=JWebConstant.OK %>'){
                    topCenterMessage('<%=JWebConstant.OK %>','操作成功！');
                } else topCenterMessage('<%=JWebConstant.ERROR %>',data);
            },
           'json'
        );
    }catch(e){alert(e);}
   });
}
//跳转到合同详情页面
function detailPayContractPageOpen(seqNo){
    openTab('detailPayContractPage',payContractDetailPageTitle,'<%=path %>/payContractDetail.htm?seqNo='+seqNo);
}
//跳转到添加合同页面
function addPayContractTab(){
    openTab('payContractAddPage',payContractAddPageTitle,'<%=path %>/jsp/pay/contract/pay_contract_add.jsp');
}
$('#remindPayContractForm').form({
    url:'<%=path %>/remindPayContract.htm',
    onSubmit: function(){
        return $(this).form('validate');
    },
    success:function(data){
        if(data=='<%=JWebConstant.OK %>'){
           topCenterMessage('<%=JWebConstant.OK %>','设置成功！');
           $('#remindPayContractWindow').window('close');
        } else topCenterMessage('<%=JWebConstant.ERROR %>',data);
    }
});
</script>
