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
JWebAction actionSearch = ((JWebAction)user.actionMap.get("pFu0KEU2Q3rKTOK1"));
JWebAction actionDetail = ((JWebAction)user.actionMap.get("pFv8LsN2Q3rKTOY1"));
JWebAction actionUpdate = ((JWebAction)user.actionMap.get("pFAC5az2Q3rKTPy1")); 
JWebAction actionSetUserTypeSell = ((JWebAction)user.actionMap.get("pPXP9JW2Q3rKTOK1")); 
JWebAction actionSetUserTypeBuy = ((JWebAction)user.actionMap.get("pPXP9JW2Q3rKTOK2"));
String userId = request.getParameter("userId");
%>
<script type="text/javascript">
var payTranUserInfoListPageTitle="用户列表";
var payTranUserInfoDetailPageTitle="认证";
$(document).ready(function(){});
</script>
<div class="easyui-layout" data-options="fit:true">
<div data-options="region:'center'">
	<table id="payTranUserInfoList" style="width:100%;height:100%" rownumbers="true" pagination="true"
    fit="true" data-options="border:false,collapsible:true,autoRowHeight:true,pageSize:20,singleSelect:true,url:'<%=path 
        %>/payTranUserInfo.htm?flag=0',method:'post',toolbar:'#payTranUserInfoListToolBar'">
       <thead>
        <tr>
           <th field="userId" width="8%" align="left" sortable="true">客户账号</th>
            <th field="realName" width="6%" align="left" sortable="true" formatter="hideNameFormatter">客户名称</th>
            <th field="userType" width="3%" align="left" sortable="true" formatter="userTypeFormatter" align="left">客户类别</th>
            <th field="province" width="4%" align="left" sortable="true">省份</th>
            <th field="city" width="4%" align="left" sortable="true">城市</th>
            <th field="area" width="4%" align="left" sortable="">区/县</th>
            <th field="mobile" width="6%" align="left" sortable="true" formatter="hideMobileFormatter">联系电话</th>
            <th field="registerTime" width="8%" align="left" sortable="true">注册日期</th>
            <th field="registerType" width="4%" align="left" sortable="true" formatter="registerTypeFormatter" align="left">注册类型</th>
            <th field="cretType" width="5%" align="left" sortable="true" formatter="cretTypeFormatter" align="left">证件类型</th>
            <th field="cretNo" width="7%" align="left" sortable="true" formatter="hideIdCardFormatter">证件号</th>
            <th field="certSubmitTime" width="8%" align="left" sortable="true">证件上传日期</th>
            <th field="userStatus" width="3%" align="left" sortable="true" formatter="userStatusFormatter" align="left">身份认证</th>
            <th field="checkTime" width="6%" align="left" sortable="true">身份认证日期</th>
            <th field="trialStatus" width="4%" align="left" sortable="true" formatter="trialStatusFormatter" align="left">试用期状态</th>
            <th field="no" width="3%" align="left" sortable="true">预付卡号</th>
            <th field="no" width="3%" align="left" sortable="true">绑定状态</th>
            <th field="acstatus" width="4%" sortable="true" formatter="acstatusFormatter" align="left">账号状态</th>
            <th field="cashacbal" width="4%" align="left" sortable="true">现金余额</th>
            <th field="consacbal" width="4%" align="left" sortable="true">账面余额</th>
            <th field="frozbalance" width="4%" align="left" sortable="true">冻结余额</th>
            <th field="checkUserId" width="4%" align="left" sortable="true">操作人</th>
           <th field="operation" data-options="formatter:formatPayTranUserInfoOperator" width="8%" align="left">操作</th>
       </tr>
       </thead>
</table>
</div>
<div id="payTranUserInfoListToolBar" data-options="border:false" style="text-align:left;padding:5px 5px 5px 5px;">
    客户账号<input type="text" id="tranUserInfoUserId" name="userId" value="<%= userId==null? "":userId %>" class="easyui-textbox" style="width:100px"/>
    客户名称<input type="text" id="realName" name="realName" class="easyui-textbox" value=""  style="width:100px"/>
    联系电话<input type="text" id="mobile" name="mobile" class="easyui-textbox" value=""  style="width:100px"/>
 注册日期<input type="text" id="registerTime_start" name="registerTime_start"
		 data-options="editable:false" class="easyui-datebox" value=""  style="width:100px"/>
		 ~
	<input type="text" id="registerTime_end" name="registerTime_end"
		 data-options="editable:false" class="easyui-datebox" value=""  style="width:100px"/> 
认证日期<input type="text" id="checkTime_start" name="checkTime_start"
		 data-options="editable:false" class="easyui-datebox" value=""  style="width:100px"/>
		 ~
	<input type="text" id="checkTime_end" name="checkTime_end"
		 data-options="editable:false" class="easyui-datebox" value=""  style="width:100px"/> 
地址<select name="searchPayTranUserInfotranUserInfoProvinceId" id="tranUserInfoProvinceId"></select><select name="searchPayTranUserInfotranUserInfoCityId" id="tranUserInfoCityId"></select><select name="searchPayTranUserInfoRegion" id="tranUserInfoAreaId"></select>  
认证状态<select name="userStatus" id="userStatus" class="easyui-combobox" data-options="editable:false">
				   <option value="" selected="true">请选择</option>
				   <option value="0" >未认证</option>
				   <option value="1" >审核中</option>
				   <option value="2" >已通过 </option>
				   <option value="3" >未通过 </option>  					  
				 </select>
用户状态<select name="acstatus" id="acstatus" class="easyui-combobox" data-options="editable:false">
				   <option value="" selected="true">请选择</option>
				   <option value="0" >正常</option>
				   <option value="1" >未激活</option>
				   <option value="2" >冻结 </option>
				   <option value="9" >已销户 </option>  					  
				 </select>
				 用户类型<select name="userType" id="userType" class="easyui-combobox" data-options="editable:false">
				   <option value="" selected="true">请选择</option>
				   <option value="0" >买家</option>
				   <option value="1" >卖家</option>
				 </select>
				 
    <%if(actionSearch != null){//角色判断%>
    <a href="javascript:searchPayTranUserInfoList()" class="easyui-linkbutton" iconCls="icon-search"><%=actionSearch.name %></a>
    <%} %>
</div>
<div data-options="region:'south',border:false" style="text-align:left;margin-left:7px;margin-top:5px;margin-bottom:5px;">
		<%if(actionSearch != null){//角色判断%>
		<a href="javascript:payTranUserInfoExportExcel()"  class="easyui-linkbutton" iconCls="icon-config">excel导出</a>
		<%} %>
</div>
</div>
<script type="text/javascript">
$('#payTranUserInfoList').datagrid({
	onBeforeLoad : function(param){
		addressInit('tranUserInfoProvinceId', 'tranUserInfoCityId', 'tranUserInfoAreaId');
		param.flag = '0';
		<%if(userId != null){%>
        param.userId = $("#tranUserInfoUserId").val();
        <%}%>
    },
    onLoadError: function(data){
        topCenterMessage('<%=JWebConstant.ERROR %>','数据加载失败！');
    },
    onLoadSuccess: function(data){
        $(this).datagrid('fixRownumber');//行号自适应宽度
    }
});
function formatPayTranUserInfoOperator(val,row,index){
     var tmp=
        <%if(actionDetail != null ){//角色判断%>
           (row.userStatus!='2'?"<a href=\"javascript:detailPayTranUserInfoPageOpen('"+row.userId+"')\"><%=actionDetail.name %></a>&nbsp;&nbsp;":"")+
        <%}%>
        <%if(actionUpdate != null){//角色判断%>
        (row.acstatus=="0"?"<a href=\"javascript:FrozPayAccProfile('"+row.userId+"','2')\"><%=actionUpdate.name %></a>&nbsp;&nbsp;":"")+
        <%}%>
        <%if(actionUpdate != null){//角色判断%>
            (row.acstatus=="2"?"<a href=\"javascript:FrozPayAccProfile('"+row.userId+"','0')\">恢复</a>&nbsp;&nbsp;":"")+
         <%}%>
         <%if(actionSetUserTypeSell != null){//角色判断%>
             (row.userType=="0"?"<a href=\"javascript:setUserType('"+row.userId+"','1')\"><%=actionSetUserTypeSell.name %></a>&nbsp;&nbsp;":"")+
          <%}%>
         <%if(actionSetUserTypeBuy != null){//角色判断%>
             (row.userType=="1"?"<a href=\"javascript:setUserType('"+row.userId+"','0')\"><%=actionSetUserTypeBuy.name %></a>&nbsp;&nbsp;":"")+
          <%}%>
        "";
     return tmp;
}
function searchPayTranUserInfoList(){
    $('#payTranUserInfoList').datagrid('load',{
          userId:$('#tranUserInfoUserId').val(),
          realName:$('#realName').val(),
          mobile:$('#mobile').val(),
          registerTime_start:$('#registerTime_start').datebox('getValue'),
          registerTime_end:$('#registerTime_end').datebox('getValue'),
          checkTime_start:$('#checkTime_start').datebox('getValue'),
          checkTime_end:$('#checkTime_end').datebox('getValue'),
          province:$('#tranUserInfoProvinceId').val()=='省'?'':$('#tranUserInfoProvinceId').val(),
          city:$('#tranUserInfoProvinceId').val()=='省'?'':$('#tranUserInfoCityId').val(),
          area:$('#tranUserInfoProvinceId').val()=='省'?'':$('#tranUserInfoAreaId').val(),
          userStatus:$('#userStatus').combobox('getValue'),
          acstatus:$('#acstatus').combobox('getValue'),
          userType:$('#userType').combobox('getValue')
         
    });  
}
function addPayTranUserInfoPageOpen(){
    openTab('addPayTranUserInfoPage',payTranUserInfoAddPageTitle,'<%=path %>/jsp/pay/user/pay_tran_user_info_add.jsp');
}
function detailPayTranUserInfoPageOpen(id){
    openTab('detailPayTranUserInfoPage',payTranUserInfoDetailPageTitle,'<%=path %>/detailPayTranUserInfo.htm?id='+id);
}
function setUserType(userId,userType){
	var showMessage = '';
	if(userType=='0')
	{
		showMessage = '确定要设置成买家吗？';
		
	}else if(userType=='1'){
		
		showMessage = '确定要设置成卖家吗？';
	}
	 $.messager.confirm('提示', showMessage, function(r){
		    if(!r)return;
		    try{
		        $.post('<%=path %>/setUserType.htm',
		            {	userId:userId,
		        		userType:userType
		            },
		            function(data){
		                $('#payTranUserInfoList').datagrid('reload');
		                if(data=='<%=JWebConstant.OK %>'){
		                    topCenterMessage('<%=JWebConstant.OK %>','操作成功！');
		                } else topCenterMessage('<%=JWebConstant.ERROR %>',data);
		            },
		           'json'
		        );
		    }catch(e){alert(e);}
		   });
}
function FrozPayAccProfile(userId,type){
	var showMessage = '';
	if(type=='0')
	{
		showMessage+='确定要恢复该账户吗？';
		
	}else if(type=='2'){
		
		showMessage+='确定要冻结该账户吗？';
	}
	 $.messager.confirm('提示', showMessage, function(r){
		    if(!r)return;
		    try{
		        $.post('<%=path %>/FrozPayAccProfile.htm',
		            {	userId:userId,
		        		type:type
		            },
		            function(data){
		                $('#payTranUserInfoList').datagrid('reload');
		                if(data=='<%=JWebConstant.OK %>'){
		                    topCenterMessage('<%=JWebConstant.OK %>','操作成功！');
		                } else topCenterMessage('<%=JWebConstant.ERROR %>',data);
		            },
		           'json'
		        );
		    }catch(e){alert(e);}
		   });
	}
function removePayTranUserInfo(id){
    $.messager.confirm('提示', '确认删除?', function(r){
    if(!r)return;
    $('#testUserList').datagrid('loading');
    try{
        $.post('<%=path %>/removePayTranUserInfo.htm',
            {id:id},
            function(data){
                $('#payTranUserInfoList').datagrid('reload');
                if(data=='<%=JWebConstant.OK %>'){
                    topCenterMessage('<%=JWebConstant.OK %>','删除成功！');
                } else topCenterMessage('<%=JWebConstant.ERROR %>',data);
            },
           'json'
        );
    }catch(e){alert(e);}
   });
}
//excel导出js
function payTranUserInfoExportExcel(){
	$.messager.confirm('提示', '您确认将当前记录导出至excel吗？', function(r){
       if(!r)return;
	    try{
	    	var path ="<%=path %>/payTranUserInfoExportExcel.htm?"+
			"userId="+$('#tranUserInfoUserId').val()+
		    "&realName="+$('#realName').val()+
		    "&mobile="+$('#mobile').val()+
		    "&registerTime_start="+$('#registerTime_start').datebox('getValue')+
		    "&registerTime_end="+$('#registerTime_end').datebox('getValue')+
		    "&checkTime_start="+$('#checkTime_start').datebox('getValue')+
		    "&checkTime_end="+$('#checkTime_end').datebox('getValue')+
			"&userStatus="+$('#userStatus').combobox('getValue')+
			"&acstatus="+$('#acstatus').combobox('getValue')+
			"&userType="+$('#userType').combobox('getValue')+
			"&province="+encodeURIComponent(encodeURIComponent($('#tranUserInfoProvinceId').val()=='省'?'':$('#tranUserInfoProvinceId').val()))+
			"&city="+encodeURIComponent(encodeURIComponent($('#tranUserInfoProvinceId').val()=='省'?'':$('#tranUserInfoCityId').val()))+
			"&area="+encodeURIComponent(encodeURIComponent($('#tranUserInfoProvinceId').val()=='省'?'':$('#tranUserInfoAreaId').val()));
	    	window.location.href = path;
   	  	}catch(e){alert(e);}
   	  
   	});
}
//注册类型
function registerTypeFormatter(data,row,index){
	if(data == "0") {
		return "无";
	}else if(data == "1") {
		return "手机号";
	}
	else if(data == "2") {
		return "邮箱";
	}else {
		return "";
	}
}
// 证件类型 01身份证,02军官证,03护照,04回乡证,05台胞证,06警官证,07士兵证,99其他 默认01
function cretTypeFormatter(data,row,index){
	if(data == "01") {
		return "身份证";
	}else if(data == "02") {
		return "军官证";
	} 
	else if(data == "03") {
		return "护照";
	} 
	else if(data == "04") {
		return "回乡证";
	} 
	else if(data == "05") {
		return "台胞证";
	} 
	else if(data == "06") {
		return "警官证";
	} 
	else if(data == "07") {
		return "士兵证";
	} 
	else if(data == "99") {
		return "其他";
	} else {
		return "";
	}
}
//试用期状态
function trialStatusFormatter(data,row,index){
	if(data == "0") {
		return "试用";
	}else if(data == "1") {
		return "正常";
	} else {
		return "";
	}
}
//账户状态 0-正常；1-未激活；2-冻结；9-已销户
function acstatusFormatter(data,row,index){
	if(data == "0") {
		return "正常";
	}else if(data == "1") {
		return "未激活";
	} 
	else if(data == "2") {
		return "冻结";
	}
	else if(data == "9") {
		return "已销户";
	}else {
		return "";
	}
}
//认证状态 0：未认证 1：审核中 2：已通过  3：未通过 默认0
function userStatusFormatter(data,row,index){
	if(data == "0") {
		return "未认证";
	}else if(data == "1") {
		return "审核中";
	} 
	else if(data == "2") {
		return "已通过";
	}
	else if(data == "3") {
		return "未通过 ";
	}else {
		return "";
	}
}
function userTypeFormatter(data,row,index){
	if(data == "0") {
		return "买家";
	}else if(data == "1") {
		return "卖家";
	} else {
		return "";
	}
}

</script>
