<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ page import="util.JWebConstant"%>
<%@ page import="com.jweb.dao.JWebUser"%>
<%@ page import="com.jweb.dao.JWebAction"%>
<%@ page import="com.jweb.service.JWebUserService"%>
<%@ page import="com.PayConstant"%> 
<%@ page import="java.util.Map"%> 
<%
String path = request.getContextPath();
//登录和权限判断
JWebUser user = (JWebUser)session.getAttribute("user");
if(!JWebUserService.checkUser(user,request)){
%><script>window.location.href='<%=path %>/jsp/jweb/login.jsp'</script><%
    return;
}
JWebAction actionSearch = ((JWebAction)user.actionMap.get("pum6Obp2Q3rKTOQ1"));
JWebAction actionAdd = ((JWebAction)user.actionMap.get("pvGnjSi2Q3rKTON1"));
JWebAction actionDetail = ((JWebAction)user.actionMap.get("pus1lsf2Q3rKTP21"));
JWebAction actionUpdate = ((JWebAction)user.actionMap.get("pus1lsf2Q3rKTPk1"));
JWebAction actionDisable = ((JWebAction)user.actionMap.get("pvu2JNI2Q3rKTOI1"));
JWebAction actionAble = ((JWebAction)user.actionMap.get("pvu40E22Q3rKTOI1"));
JWebAction actionFreeze = ((JWebAction)user.actionMap.get("pvu40E22Q3rKTOI2"));
JWebAction actionThaw = ((JWebAction)user.actionMap.get("pvu40E22Q3rKTOI3"));
JWebAction actionCheck = ((JWebAction)user.actionMap.get("pvBsULb2Q3rKTOM1"));
//代理
JWebAction actionAgent = ((JWebAction)user.actionMap.get("pQOPbwa2Q3rKTPi1"));

%>
<script type="text/javascript">
var payMerchantListPageTitle="商户列表";
var payMerchantAddPageTitle="商户开户";
var payMerchantDetailPageTitle="商户详情";
var payMerchantUpdatePageTitle="修改商户";
var payMerchantCheckPageTitle="审核商户";
var showParentNodesPageTitle="代理";
$(document).ready(function(){
	var thisTab = $('#mainTabs').tabs('getSelected').panel('options').tab;
});

function payMerchantExportExcel(){
	$.messager.confirm('提示', '您确认将当前记录导出至excel吗？', function(r){
        if(!r)return;
	    $('#payMerchantSettlementList').datagrid('loading');
	    try{
	    	var tmp = "<%=path %>/payMerchantExportExcel.htm?"+
			"custId="+$('#searchPayMerchantCustId').val()+
	        "&storeName="+$('#searchPayMerchantStoreName').val()+
	        "&merStatus="+$('#searchPayMerchantMerStatus').combobox('getValue')+
	        "&checkStatus="+$('#searchPayMerchantCheckStatus').combobox('getValue')+
	        "&riskLevel="+$('#searchPayMerchantRiskLevel').combobox('getValue')+
	        "&merType="+$('#merType').combobox('getValue')+
	        "&province="+encodeURIComponent($('#province').val()=='省'?'':$('#province').val())+
	        "&city="+encodeURIComponent($('#province').val()=='省'?'':$('#city').val())+
	        "&region="+encodeURIComponent($('#province').val()=='省'?'':$('#area').val())+
	        "&createTime="+$('#searchPayMerchantCreateTime').datebox('getValue')+
	        "&createEndTime="+$('#searchPayMerchantCreateEndTime').datebox('getValue');
	        window.location.href= tmp;
	    }catch(e){alert(e);}
    });
}

</script>
<div class="easyui-layout" data-options="fit:true">
	 <div data-options="region:'center'">
		<table id="payMerchantList" style="height:100%" rownumbers="true" pagination="true"
		    fit="true" data-options="border:false,collapsible:true,autoRowHeight:true,pageSize:20,singleSelect:true,url:'<%=path 
		        %>/payMerchant.htm?flag=0',method:'post',toolbar:'#payMerchantListToolBar'">
		       <thead>
		  	   <tr>
		           <th field="custId" width="5%" sortable="true" align="left">商户编号</th>
		           <th field="storeName" width="15%" sortable="true" align="left">商户名称</th>
		           <th field="merType" width="5%" sortable="true" formatter="merType_formatter" align="left">商户类型</th>
		           <th field="bizType" width="8%" sortable="true" formatter="bizType_formatter" align="left">业务类型</th>
		           <th field="settlementWay" width="8%" sortable="true" formatter="settlementWay_formatter" align="left">结算类型</th>
		           <th field="merAddr" width="20%" sortable="true" align="left">地址</th>
		           <th field="merStatus" width="4%" sortable="true" formatter="merStatusFormatter" align="left">商户状态</th>
		           <th field="createTime" width="8%" sortable="true" align="left">登记时间</th>
		           <th field="checkStatus" width="4%" sortable="true" formatter="checkStatus_formatter" align="left">审核状态</th>
		           <th field="cashAcBal" width="6%" align="left">账户余额</th>
		           <th field="frozenBal" width="6%" align="left">冻结金额</th>
		           <th field="marginBal" width="6%" align="left">保证金金额</th>
		           <th field="preStorageFee" width="6%" align="left">预存手续费余额</th>
		           <th field="checkTime" width="8%" sortable="true" align="left">审核时间</th>
		           <th field="operation" data-options="formatter:formatPayMerchantOperator" width="16%" align="left">操作</th>
		       </tr>
		       </thead>
		</table>
	</div>
	<div id="payMerchantListToolBar" data-options="border:false" style="text-align:left;padding:5px 5px 5px 5px;">
	    	商户编号<input type="text" id="searchPayMerchantCustId" name="searchPayMerchantCustId" class="easyui-textbox" value=""  style="width:130px"/>
	    	商户名称<input type="text" id="searchPayMerchantStoreName" name="searchPayMerchantStoreName" class="easyui-textbox" value=""  style="width:130px"/>
	    	状态
		    <select name="searchPayMerchantMerStatus" id="searchPayMerchantMerStatus" style="width:90px" class="easyui-combobox" data-options="editable:false">
		            <option value="">请选择</option>
		            <option value="0">开启</option> 
		            <option value="1">关闭</option>
		     </select>
	    	登记日期
	    	<input type="text" id="searchPayMerchantCreateTime" name="searchPayMerchantCreateTime" class="easyui-datebox" value=""  style="width:100px" editable="fasle"/>
	    	~
	    	<input type="text" id="searchPayMerchantCreateEndTime" name="searchPayMerchantCreateEndTime" class="easyui-datebox" value=""  style="width:100px" editable="fasle"/>
	    	业务类型<select id="searchPayMerchantBizType" name="bizType">
	    		<option value="">请选择</option>
   				<%
   					Map<String, String> map = PayConstant.MER_BIZ_TYPE;
					for (String key : map.keySet()) {
						%>
							<option value="<%= key%>"><%= map.get(key) %></option>
						<%
				 	}
   				 %>
   				</select>
	    	商户类型<select name="merType" id="merType" class="easyui-combobox" data-options="editable:false">
				   <option value="" selected="true">请选择</option>
				   <option value="0" >一般商户 </option>
				   <option value="1" >平台商户 </option>
				   <option value="2" >担保商户 </option> 					  
				 </select>
			<!--  结算类型<select name="settlementWay" id="settlementWay" class="easyui-combobox" data-options="editable:false">
			   <option value="" selected="true">请选择</option>
			   <option value="0" >自动结算到虚拟账户</option>
			   <option value="1" >线下结算 </option>
			 </select> -->
			审核状态<select name="searchPayMerchantCheckStatus" id="searchPayMerchantCheckStatus" class="easyui-combobox" data-options="editable:false">
				   <option value="" selected="true">请选择</option>
				   <option value="0" >未审核 </option>
				   <option value="1" >审核通过</option>
				   <option value="2" >审核失败 </option> 					  
				 </select>
	                       风险级别<select name="searchPayMerchantRiskLevel" id="searchPayMerchantRiskLevel" class="easyui-combobox" data-options="editable:false">
				   <option value="" selected="true">请选择</option>
				   <option value="0" >低 </option>
				   <option value="1" >中</option>
				   <option value="2" >高 </option> 					  
				 </select>
	    	地址<select name="searchPayMerchantProvince" id="province"></select><select name="searchPayMerchantCity" id="city"></select><select name="searchPayMerchantRegion" id="area"></select>  
	    <%if(actionSearch != null){//角色判断%>
	    <a href="javascript:searchPayMerchantList()" id="searchPayMerchantListButton" class="easyui-linkbutton" iconCls="icon-search"><%=actionSearch.name %></a>
	    <%} %>
	    <%if(actionAdd != null){//角色判断%>
	    <a href="javascript:openPageOnTab()" class="easyui-linkbutton"
	        iconCls="icon-add"><%=actionAdd.name %></a>
	    <%} %>
	</div>
	<div data-options="region:'south',border:false" style="text-align:left;margin-left:7px;margin-top:5px;margin-bottom:5px;">
		<%if(actionSearch != null){//角色判断%>
		    <a href="javascript:payMerchantExportExcel()"  class="easyui-linkbutton" iconCls="icon-config">excel导出</a>
		<%} %>
		<span id="getTotalMoneyIdForMerchant">&nbsp;</span>
	</div>
</div>
<script type="text/javascript">
$('#payMerchantList').datagrid({
	onBeforeLoad : function(data){
        addressInit('province', 'city', 'area');
    },
    onLoadError: function(data){
        topCenterMessage('<%=JWebConstant.ERROR %>','数据加载失败！');
    },
    onLoadSuccess: function(data){
        $(this).datagrid('fixRownumber');//行号自适应宽度
        $("#getTotalMoneyIdForMerchant").html("账户总余额："+(parseFloat(data.totalCashAcBalMoney)*0.01).toFixed(2)
        	+"元，冻结总金额："+(parseFloat(data.totalFrozenBal)*0.01).toFixed(2)
            +"元，保证金总金额："+(parseFloat(data.totalMarginBal)*0.01).toFixed(2)
            +"元，预存手续费总余额："+(parseFloat(data.totalPreStorageFeeMoney)*0.01).toFixed(2)+"元");
    }
});
function formatPayMerchantOperator(val,row,index){
     var tmp=
     	// 商户详情
         <%if(actionDetail != null){//角色判断%>
         "<a href=\"javascript:openPageOnTabForDetail("+row.custId+")\"><%=actionDetail.name %></a>&nbsp;&nbsp;"+
         <%}%>
         // 商户修改
         <%if(actionUpdate != null){//角色判断%>
        "<a href=\"javascript:updatePayMerchantPageOpen('"+row.id+"')\"><%=actionUpdate.name %></a>&nbsp;&nbsp;"+
        <%}%>
        //商户审核后才能有这两项功能
        // 禁用与启用的显示判断
        <%if(actionDisable != null){//角色判断%>
        	(row.checkStatus == "1"?
        	(row.merStatus == 0 ? "<a href=\"javascript:updatePayMerchantStatus('disablePayMerchant.htm','"+row.custId+"','MER_STATUS','1','您确定要禁用（ "+row.storeName+" ）账户吗？')\"><%=actionDisable.name %></a>&nbsp;&nbsp;":"<a href=\"javascript:updatePayMerchantStatus('ablePayMerchant.htm','"+row.custId+"','MER_STATUS','0','您确定要启用（ "+row.storeName+" ）账户吗？')\"><%=actionAble.name %></a>&nbsp;&nbsp;"):"")+
        <%}%>
        // 冻结与解冻的显示
        <%if(actionFreeze != null){//角色判断%>
        	(row.checkStatus == "1"?
        	(row.frozStlSign == 0 ? "<a href=\"javascript:updatePayMerchantStatus('freezePayMerchant.htm','"+row.custId+"','FROZ_STL_SIGN','1','您确定要冻结（ "+row.storeName+" ）账户吗？')\"><%=actionFreeze.name %></a>&nbsp;&nbsp;":"<a href=\"javascript:updatePayMerchantStatus('thawPayMerchant.htm','"+row.custId+"','FROZ_STL_SIGN','0','您确定要解冻（ "+row.storeName+" ）账户吗？')\"><%=actionThaw.name %></a>&nbsp;&nbsp;"):"")+
        <%}%>
         // 商户审核
         <%if(actionCheck != null){//角色判断%>
         	(row.checkStatus == 0 ? "<a href=\"javascript:checkPayMerchantPageOpen('"+row.custId+"')\"><%=actionCheck.name %></a>&nbsp;&nbsp;" : "") +
        <%}%>
        //代理
         <%if(actionAgent != null){//角色判断
         %>
           (row.merType == "3" ? "<a href=\"javascript:openShowParentNodesOpen('"+row.custId+"')\"><%=actionAgent.name %></a>&nbsp;&nbsp;":"")+
        <%}%>
         "";
     return tmp;
}
// 弹出页面用于商户开户
function openPageOnTab(){
    openTab('payMerchantAddPage',payMerchantAddPageTitle,'<%=path %>/jsp/pay/merchant/pay_merchant_start.jsp');
}
// 弹出页面用于商户详情页
function openPageOnTabForDetail(custId){
    openTab('payMerchantDetailPage',payMerchantDetailPageTitle,'<%=path %>/payMerchantDetail.htm?flag=detail&custId=' + custId);
}
function updatePayMerchantPageOpen(id){
    openTab('updatePayMerchantPage',payMerchantUpdatePageTitle,'<%=path %>/updatePayMerchant.htm?flag=show&id='+id);
}
function checkPayMerchantPageOpen(id){
    openTab('checkPayMerchantPage',payMerchantCheckPageTitle,'<%=path %>/payMerchantDetail.htm?flag=check&custId='+id);
}
//代理
function openShowParentNodesOpen(custId){
    openTab('showParentNodesPage',showParentNodesPageTitle,'<%=path %>/jsp/pay/merchant/pay_merchant_showagent.jsp?custId='+custId);
}
// 更新商户状态
function updatePayMerchantStatus(url,custId,columName,value,info){
    $.messager.confirm('提示', info, function(r){
    if(!r)return;
    $('#testUserList').datagrid('loading');
    try{
        $.post('<%=path %>/'+url,
            {custId:custId,columName:columName,value:value},
            function(data){
                $('#payMerchantList').datagrid('reload');
                if(data=='<%=JWebConstant.OK %>'){
                    topCenterMessage('<%=JWebConstant.OK %>','操作成功!');
                } else topCenterMessage('<%=JWebConstant.ERROR %>',data);
            },
           'text'
        );
    }catch(e){alert(e);}
   });
}
function searchPayMerchantList(){        
    $('#payMerchantList').datagrid('load',{  
          custId:$('#searchPayMerchantCustId').val(),
          storeName:$('#searchPayMerchantStoreName').val(),
          merStatus:$('#searchPayMerchantMerStatus').combobox('getValue'),
          checkStatus:$('#searchPayMerchantCheckStatus').combobox('getValue'),
          riskLevel:$('#searchPayMerchantRiskLevel').combobox('getValue'),
          bizType:$('#searchPayMerchantBizType').val()=='请选择'?'':$('#searchPayMerchantBizType').val(),
          merType:$('#merType').combobox('getValue'),
          //settlementWay:$('#settlementWay').combobox('getValue'),
          province:$('#province').val()=='省'?'':$('#province').val(),
          city:$('#province').val()=='省'?'':$('#city').val(),
          region:$('#province').val()=='省'?'':$('#area').val(),
          createTime:$('#searchPayMerchantCreateTime').datebox('getValue'),
          createEndTime:$('#searchPayMerchantCreateEndTime').datebox('getValue')
    });  
}
// 商户状态
function merStatusFormatter(data,row,index){
	if(data == "0") {
		return "开启";
	}else if(data == "1") {
		return "关闭";
	} else {
		return "";
	}
}

function frozStlSignFormatter(data,row,index){
	if(data == "0") {
		return "未冻结";
	}else if(data == "1") {
		return "已冻结";
	} else {
		return "";
	}
}

// 法人证件类型
function lawPersonCretType_formatter(data,row,index){
	<%
	java.util.Iterator<String> it = PayConstant.CERT_TYPE.keySet().iterator();
	while(it.hasNext()){
	String key = (String)it.next();
	%>if(data=="<%=key %>")return "<%=PayConstant.CERT_TYPE.get(key) %>";<%
	}%>
}

// 商户类型
function merType_formatter(data,row,index){
	if(data=="0"){
		return "一般商户";
	} else if(data=="1"){
		return "平台商户 ";
	} else if(data=="2"){
		return "担保商户 ";
	} else if(data=="3"){
		return "代理商户 ";
	}
}
//业务类型
function bizType_formatter(data,row,index){
	<%
	java.util.Iterator<String> bizTypeit = PayConstant.MER_BIZ_TYPE.keySet().iterator();
	while(bizTypeit.hasNext()){
	String key = (String)bizTypeit.next();
	%>if(data=="<%=key %>")return "<%=PayConstant.MER_BIZ_TYPE.get(key) %>";<%
	}%>
}
//结算类型
function settlementWay_formatter(data,row,index){
	if(data=="0"){
		return "自动结算到虚拟账户";
	} else if(data=="1"){
		return "线下打款";
	} else if(data=="2"){
		return "手动结算到虚拟账户";
	}
}
// 业务类型
/* function bizType_formatter(data,row,index) {
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
} */

// 商户审核状态
function checkStatus_formatter(data,row,index){
	if(data=="0") {
		return "未审核";
	} else if(data=="1") {
		return "审核通过"
	} else if(data=="2"){
		return "审核失败";
	}
}

</script>
