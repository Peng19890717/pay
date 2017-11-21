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
JWebAction actionSearch = ((JWebAction)user.actionMap.get("pvGzNJB2Q3rKTP01"));
JWebAction actionAdd = ((JWebAction)user.actionMap.get("pvGzNJB2Q3rKTP71"));
JWebAction actionDisable = ((JWebAction)user.actionMap.get("pvGIqfm2Q3rKTOI1"));
JWebAction actionAble = ((JWebAction)user.actionMap.get("pvGJ7kV2Q3rKTOJ1"));
JWebAction actionResetPwd = ((JWebAction)user.actionMap.get("pvGJ7kV2Q3rKTOJ2"));
%>
<script type="text/javascript">
var payMerchantUserListPageTitle="商品操作员";
var payMerchantUserAddPageTitle="添加payMerchantUser";
var payMerchantUserDetailPageTitle="payMerchantUser详情";
var payMerchantUserUpdatePageTitle="修改payMerchantUser";
$(document).ready(function(){});
</script>
<table id="payMerchantUserList" style="width:100%;height:100%" rownumbers="true" pagination="true"
    fit="true" data-options="border:false,collapsible:true,autoRowHeight:true,pageSize:20,singleSelect:true,url:'<%=path 
        %>/payMerchantUser.htm?flag=0',method:'post',toolbar:'#payMerchantUserListToolBar'">
       <thead>
        <tr>
           <th field="userId" width="10%" align="left" sortable="true">用户ID</th>
           <th field="custId" width="10%" align="left" sortable="true">商户编号</th>
           <th field="userNam" width="10%" align="left" sortable="true">操作员</th>
           <th field="creaSign" width="5%" align="left" sortable="true" formatter="creaSign_formatter">创建标识</th>
           <th field="userDate" width="10%" align="left" sortable="true">用户创建日期</th>
           <th field="state" width="5%" align="left" sortable="true" formatter="state_formatter">状态</th>
           <th field="tel" width="10%" align="left" sortable="true" formatter="hideMobileFormatter">电话号码</th>
           <th field="email" width="15%" align="left" sortable="true" formatter="hideEmailFormatter">电子邮件</th>
           <th field="lastUppwdDate" width="15%" align="left" sortable="true">最后一次修改密码时间</th>
           <th field="operation" data-options="formatter:formatPayMerchantUserOperator" width="9%" align="left">操作</th>
       </tr>
       </thead>
</table>
<div id="payMerchantUserListToolBar" data-options="border:false" style="text-align:left;padding:5px 5px 5px 5px;">
    商户编号<input type="text" id="searchPayMerchantUserCustId" name="searchPayMerchantUserCustId" class="easyui-textbox" value=""  style="width:130px"/>
    操作员名称<input type="text" id="searchPayMerchantUserUserNam" name="searchPayMerchantUserUserNam" class="easyui-textbox" value=""  style="width:130px"/>
    电话号码<input type="text" id="searchPayMerchantUserTel" name="searchPayMerchantUserTel" class="easyui-textbox" value=""  style="width:130px"/>
    电子邮箱<input type="text" id="searchPayMerchantUserEmail" name="searchPayMerchantUserEmail" class="easyui-textbox" value=""  style="width:130px"/>
    <%if(actionSearch != null){//角色判断%>
    <a href="javascript:searchPayMerchantUserList()" class="easyui-linkbutton" iconCls="icon-search"><%=actionSearch.name %></a>
    <%} %>
</div>
<script type="text/javascript">
$('#payMerchantUserList').datagrid({
    onLoadError: function(data){
        topCenterMessage('<%=JWebConstant.ERROR %>','数据加载失败！');
    },
    onLoadSuccess: function(data){
        $(this).datagrid('fixRownumber');//行号自适应宽度
    }
});
function formatPayMerchantUserOperator(val,row,index){
     var tmp=
     	
     	// 禁用和启用
        <%if(actionDisable != null && actionAble != null){//角色判断%>
        	(0 == row.state ? "<a href=\"javascript:updatePayMerchantUserStatus('disabledPayMerchantUser.htm','"+row.userId+"','STATE','1','您确定要禁用（ "+row.userNam+" ）账户吗？')\"><%=actionDisable.name %></a>&nbsp;&nbsp;" :  "<a href=\"javascript:updatePayMerchantUserStatus('abledPayMerchantUser.htm','"+row.userId+"','STATE','0','您确定要启用（ "+row.userNam+" ）账户吗？')\"><%=actionAble.name %></a>&nbsp;&nbsp;")+
        <%}%>
        
        //重置密码
        <%if(actionResetPwd !=null ){//角色判断%>
        	(0 == row.state ? "<a href=\"javascript:resetPwdPayMerchantUser('"+row.custId+"','"+row.userId+"','"+row.userNam+"')\"><%=actionResetPwd.name %></a>&nbsp;" : "") + 
        <%}%>
        "";
     return tmp;
}


// 更新商户操作员状态
function updatePayMerchantUserStatus(url,userId,columName,value,info){
    $.messager.confirm('提示', info, function(r){
    if(!r)return;
    $('#testUserList').datagrid('loading');
    try{
        $.post('<%=path %>/'+url,
            {userId:userId,columName:columName,value:value},
            function(data){
                $('#payMerchantUserList').datagrid('reload');
                if(data=='<%=JWebConstant.OK %>'){
                    topCenterMessage('<%=JWebConstant.OK %>','操作成功!');
                } else topCenterMessage('<%=JWebConstant.ERROR %>',data);
            },
           'text'
        );
    }catch(e){alert(e);}
   });
}

// 重置商户操作员密码
function resetPwdPayMerchantUser(custId,userId,name){
    $.messager.confirm('提示', '您确定要重置（'+name+'）的密码？', function(r){
    if(!r)return;
    $('#testUserList').datagrid('loading');
    try{
        $.post('<%=path %>/resetPwdPayMerchantUser.htm',
            {custId:custId,userId:userId},
            function(data){
            	$('#payMerchantUserList').datagrid('reload');
                if(data=='<%=JWebConstant.OK %>'){
                    topCenterMessage('<%=JWebConstant.OK %>','操作成功!');
                } else topCenterMessage('<%=JWebConstant.ERROR %>',data);
            },
           'text'
        );
    }catch(e){alert(e);}
   });
}

function searchPayMerchantUserList(){
    $('#payMerchantUserList').datagrid('load',{
          custId:$('#searchPayMerchantUserCustId').val(),
          userNam:$('#searchPayMerchantUserUserNam').val(),
          tel:$('#searchPayMerchantUserTel').val(),
          email:$('#searchPayMerchantUserEmail').val()
    });  
}
function detailPayMerchantUserPageOpen(userId){
    openTab('detailPayMerchantUserPage',payMerchantUserDetailPageTitle,'<%=path %>/detailPayMerchantUser.htm?userId='+userId);
}
function updatePayMerchantUserPageOpen(userId){
    openTab('updatePayMerchantUserPage',payMerchantUserUpdatePageTitle,'<%=path %>/updatePayMerchantUser.htm?flag=show&userId='+userId);
}
function removePayMerchantUser(userId){
    $.messager.confirm('提示', '确认删除?', function(r){
    if(!r)return;
    $('#testUserList').datagrid('loading');
    try{
        $.post('<%=path %>/removePayMerchantUser.htm',
            {userId:userId},
            function(data){
                $('#payMerchantUserList').datagrid('reload');
                if(data=='<%=JWebConstant.OK %>'){
                    topCenterMessage('<%=JWebConstant.OK %>','删除成功！');
                } else topCenterMessage('<%=JWebConstant.ERROR %>',data);
            },
           'json'
        );
    }catch(e){alert(e);}
   });
}

// 创建者转换
function creaSign_formatter(data,row,index){
	// 0为初始化创建 1为用户创建
	if("0" == data) {
		return "初始化创建";
	} else if("1" == data) {
		return "用户创建";
	}
}

// 操作员状态
function state_formatter(data,row,index) {
	// 0正常1禁用
	if("0" == data) {
		return "正常";
	} else if("1" == data) {
		return "禁用";
	}
}

// 是否设置邮箱（暂未使用）
function mailflg_formatter(data,row,index) {
	// 0 未设置   1 已设置
	if("0" == data) {
		return "未设置";
	} else if("1" == data) {
		return "已设置";
	}
}

</script>
