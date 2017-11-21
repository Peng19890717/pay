<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ page import="util.JWebConstant"%>
<%@ page import="com.jweb.dao.JWebUser"%>
<%@ page import="com.jweb.service.JWebUserService"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.jweb.vo.Theme"%>
<%
	String path = request.getContextPath();
//登录和权限判断
JWebUser user = (JWebUser)session.getAttribute("user");
if(!JWebUserService.checkUser(user,request)){
%><script>window.location.href='<%=path %>/jsp/jweb/login.jsp';</script><%
	return;
}
Theme theme = (Theme)JWebConstant.SYS_THEME_MAP.get(user.theme);
theme = "default".equals(theme.name)?((Theme)JWebConstant.SYS_THEME_MAP.get(JWebConstant.SYS_CONFIG.get("THEME"))):theme;
theme = theme==null?(Theme)JWebConstant.SYS_THEME_MAP.get("default"):theme;
%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>系统主页</title>
<!--[if !IE]><!-->
<link rel="stylesheet" type="text/css" href="<%=path %>/js/jquery-easyui-1.4/themes/<%=theme.name %>/easyui.css">
<!--<![endif]-->
<!--[if IE]>
<link rel="stylesheet" type="text/css" href="<%=path %>/js/jquery-easyui-1.4/themes/<%=theme.name %>/easyui-ie.css">
<![endif]-->
<link rel="stylesheet" type="text/css" href="<%=path %>/js/jquery-easyui-1.4/themes/icon.css">
<link rel="stylesheet" type="text/css" href="<%=path %>/css/jweb_main.css">
<script type="text/javascript" src="<%=path %>/js/jquery-easyui-1.4/jquery.min.js"></script>
<script type="text/javascript" src="<%=path %>/js/jquery-easyui-1.4/jquery.easyui.min.js"></script>
<script type="text/javascript" src="<%=path %>/js/jquery-easyui-1.4/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="<%=path %>/js/jweb.js"></script>
<script type="text/javascript" src="<%=path %>/js/pro_city/jsAddress.js"></script>
<style type="text/css">
.linear{ 
height:40px;padding:10px;
filter:progid:DXImageTransform.Microsoft.Gradient(startColorStr='#FFffff',endColorStr='<%=theme.color.length()==0?"#ffffff":"#"+theme.color %>',gradientType='0'); /*IE 6 7 8*/ 
background: -ms-linear-gradient(top, #fff,  <%=theme.color.length()==0?"#ffffff":"#"+theme.color %>);        /* IE 10 */
background:-moz-linear-gradient(top,#fff,<%=theme.color.length()==0?"#ffffff":"#"+theme.color %>);/*火狐*/ 
background:-webkit-gradient(linear, 0% 0%, 0% 100%,from(#fff), to(<%=theme.color.length()==0?"#ffffff":"#"+theme.color %>));/*谷歌*/ 
background: -webkit-gradient(linear, 0% 0%, 0% 100%, from(#fff), to(<%=theme.color.length()==0?"#ffffff":"#"+theme.color %>));      /* Safari 4-5, Chrome 1-9*/
background: -webkit-linear-gradient(top, #fff, <%=theme.color.length()==0?"#ffffff":"#"+theme.color %>);   /*Safari5.1 Chrome 10+*/
background: -o-linear-gradient(top, #fff, <%=theme.color.length()==0?"#ffffff":"#"+theme.color %>);  /*Opera 11.10+*/
} 
</style>
<script type="text/javascript">
var scriptPath='<%=path %>';
var loginAtOtherNo='<%=JWebConstant.LOGIN_AT_OTHER %>';
var loginTimeOut='<%=JWebConstant.LOGIN_TIMEOUT %>';
var undefinedError='<%=JWebConstant.UNDEFINED_ERROR %>';
</script>
</head>
<body id="jwebBody" class="easyui-layout" onload="">
<div id="jwebTopPanel" data-options="region:'north',border:true" style="text-align:right;padding:5px;visibility:hidden" class="linear">
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
	  <tr>
	    <td align="left" valign="middle" 
	    	style="font-family:黑体;font-weight: bold;font-size: 18px;padding-top:10px;">钱通支付管理系统<!--公司内部业务维护系统 img src="<%=path %>/images/logo.gif"/ --></td>
	    <td align="right" valign="middle">
	    	<%="0".equals(JWebConstant.SYS_CONFIG.get("DEBUG"))?"测试环境":("1".equals(JWebConstant.SYS_CONFIG.get("DEBUG"))?"生产环境":"未知环境") %>&nbsp;&nbsp;|&nbsp;
		    欢迎您：<%=user.name %>&nbsp;&nbsp;|&nbsp;
			上次登录时间：<%=user.loginTime != null ? new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(user.loginTime) : "无" %>&nbsp;&nbsp;|&nbsp;
			登录次数：<%=user.loginCount %>次&nbsp;&nbsp;|&nbsp;
			<a href="javascript:modifySysPasswordPanelOpen()">修改密码</a>&nbsp;&nbsp;|&nbsp;
			<a href="javascript:sysExit()">退出</a>&nbsp;&nbsp;|&nbsp;
			<select id="sysTheme" name="sysTheme" style="width:100px;" data-options="editable:false">
				<%for(int i = 0; i<JWebConstant.SYS_THEME_LIST.size(); i++){
					Theme t = (Theme) JWebConstant.SYS_THEME_LIST.get(i); %>
					<option value="<%=t.name %>" <%=t.name.equals(theme.name)?"selected":"" %>><%=t.label %></option>
				<%} %>
		    </select>
		</td>
	  </tr>
	</table>
</div>
<div id="jwebMenuPanel" data-options="region:'west',split:true,title:'系统管理'" style="width:220px;visibility:hidden">
	<span id="sysMenuTreeLoadLabel">载入中...</span>
	<ul id="jwebMenu" class="easyui-tree"></ul>
</div>
   <div region="center" border="true">		
	<div id="mainTabs" data-options="border:false,region:'center'" class="easyui-tabs" fit="true"
		style="background-size:100% 100%;">
   	</div>
   	<div id="tabsMenu" class="easyui-menu" style="width:120px;visibility:hidden"  data-options="onClick:mainTabsMenuHandler">  
	    <div name="other">关闭其他</div>
	    <div name="all">关闭所有</div>
	</div>
</div>
   <div id="modifySysPasswordPanel" class="easyui-window" title="修改密码"
	data-options="closed:true,minimizable:false,collapsible:false,maximizable:false,resizable:false,iconCls:'icon-edit',modal:true" 
   	style="width:330px;padding-top:15px;padding-left:35px;padding-bottom:15px;visibility:hidden">
       <form id="modifySysPasswordForm" method="post">
           <table cellpadding="5">
               <tr>
                   <td>原密码:</td>
                   <td><input id="oldSysPassword" name="oldSysPassword" invalidMessage="密码为8-16位字母数字组合" missingMessage="请输入原密码"
                    	validType="checkOldPassword[]" class="easyui-textbox" required="true" type="password" value=""></input></td>
               </tr>
               <tr>
                   <td>密码:</td>
                   <td><input id="newSysPassword" name="newSysPassword" missingMessage="请输入新密码"
                    validType="checkNewPassword['#oldSysPassword']" class="easyui-textbox" required="true" type="password" value=""></input></td>
               </tr>
               <tr>
                   <td>密码确认:</td>
                   <td><input id="reNewSysPassword" name="reNewSysPassword" missingMessage="请再次输入新密码"
					validType="equalTo['#newSysPassword']" class="easyui-textbox" required="true" type="password" value=""></input></td>
               </tr>
               <tr>
               	<td></td>
                   <td>
		            <a href="javascript:modifySysPasswordFormSubmit()"
						class="easyui-linkbutton"  style="width:100%;" iconCls="icon-ok">修改</a>
				</td>
               </tr>
           </table>
       </form>
   </div>
<script type="text/javascript">
//绑定tabs的右键菜单
$("#mainTabs").tabs({
    onContextMenu : function (e, title) {
    	$(this).tabs('select',title);
        e.preventDefault();
        $('#tabsMenu').menu('show', {
            left : e.pageX,
            top : e.pageY
        }).data("tabTitle",title);
    }
});
function mainTabsMenuHandler(item){
	var tabs = $('#mainTabs').tabs('tabs');
	var selectedTabTitle = $('#mainTabs').tabs('getSelected').panel('options').tab.text();
	var tabsTitle = new Array();
	for(var i = 0; i < tabs.length; i++) tabsTitle[i] = tabs[i].panel('options').tab.text();
	if(item.name=='all'){
		for(var i = 0; i < tabsTitle.length; i++) $('#mainTabs').tabs('close', tabsTitle[i]);
	} else if(item.name=='other'){
		for(var i = 0; i < tabsTitle.length; i++) {
		    if(tabsTitle[i] != selectedTabTitle) $('#mainTabs').tabs('close', tabsTitle[i]);
		}
	}
}
function modifySysPasswordPanelOpen(){
	$('#modifySysPasswordForm').form('clear');
       $('#modifySysPasswordPanel').window('open');
}
function sysExit(){
	$.messager.confirm('提示', '确认退出?', function(r){
		if(!r)return;
		$(document).mask('退出中...');
		location.href='<%=path %>/sysExit.htm';
	});
}
function modifySysPasswordFormSubmit(){
	$('#modifySysPasswordForm').submit();
}
$(document).ready(function(){
	document.getElementById('jwebMenuPanel').style.visibility='';
	document.getElementById('jwebTopPanel').style.visibility='';
	document.getElementById('tabsMenu').style.visibility='';
	document.getElementById('modifySysPasswordPanel').style.visibility='';
	$.extend($.fn.textbox.defaults.rules, {
        equalTo: {
        	validator: function (value, param) {
        		$.fn.textbox.defaults.rules.equalTo.message = '两次输入密码不匹配';
        		return $(param[0]).val() == value; 
        	},
			message: ''
        },
       	checkOldPassword: { 
        	validator: function (value, param) {
        		var patternPassword = /^(([a-z]+[0-9]+)|([0-9]+[a-z]+))[a-z0-9]*$/i;
        		if(patternPassword.exec(value) && value.length >= 8 && value.length <= 16)return true;
        		return false; 
        	},
			message: '密码为8-16位数字、字母混合'
        },
       	checkNewPassword: { 
        	validator: function (value, param) {
        		var patternPassword = /^(([a-z]+[0-9]+)|([0-9]+[a-z]+))[a-z0-9]*$/i;
        		if(patternPassword.exec(value) && value.length >= 8 && value.length <= 16 && $(param[0]).val() != value)return true;
        		return false; 
        	},
			message: '密码为8-16位数字、字母混合，不能和原密码相同'
        }
     });
	$('#jwebMenu').tree({
		url:'<%=path %>/menu.htm',
		method:'post',
		animate:true,
		lines:true,
   		onClick: function(node){
   			if('<%=JWebConstant.ACTION_TYPE_PAGE %>'!=node.type)return;
   		 	var tabTmp = $('#mainTabs');
   		 	//var tabIndex = tabTmp.tabs('getTabIndex',$('#tab'+node.id));
   		 	//tabIndex = tabIndex==-1?tabTmp.length:tabIndex;
   		 	if(!tabTmp.tabs('exists',node.text)){
	   			tabTmp.tabs('add',{
	   				id:'tab'+node.id,
	                title: node.text,
	                closable: true,
	                border:false
	            });
	            $('#tab'+node.id).panel('refresh','<%=path %>/'+node.url);
            }
            //选中当前选中的tab
            tabTmp.tabs('select',node.text);
   		},
   		onLoadSuccess:function(node, param){
   			document.getElementById('sysMenuTreeLoadLabel').style.display="none";
   		}
	});
	$('#sysTheme').combobox({
		onChange:function(newValue,oldValue){
			//$('#test').mask('请稍后...');
			$(document).mask('请稍后...');
			$.post('<%=path %>/sysUserThemeUpdate.htm?sysTheme='+newValue,
	        	function(data){
	        		//$('#test').unmask();
	        		$(document).unmask();
	                if(data=='<%=JWebConstant.OK %>') window.location.href='<%=path %>/jsp/jweb/main.jsp';
	            }
            );
	   	}
	});
	$('#modifySysPasswordForm').form({
	    url:'<%=path %>/sysPasswordModify.htm',
	    onSubmit: function(){
	    	var modifySysPasswordCheck=$(this).form('validate');
	    	if(modifySysPasswordCheck)$('#modifySysPasswordPanel').window('close');
	    	return modifySysPasswordCheck;
	    },
	    success:function(data){
	   		if(data=='<%=JWebConstant.OK %>'){
        		topCenterMessage('<%=JWebConstant.OK %>','密码修改成功！');
           	} else topCenterMessage('<%=JWebConstant.ERROR %>',data);
	    }
	});
	//行号自适应宽度扩展
	$.extend($.fn.datagrid.methods, {
		fixRownumber : function (jq) {
	      return jq.each(function () {
	          var panel = $(this).datagrid('getPanel');
	          var clone = $('.datagrid-cell-rownumber', panel).last().clone();
	          clone.css({
	              'position' : 'absolute',
	              left : -1000
	          }).appendTo('body');
	          var width = clone.width('auto').width();
	          if (width > 25) {
	              $('.datagrid-header-rownumber,.datagrid-cell-rownumber', panel).width(width + 5);
	              $(this).datagrid('resize');
	              clone.remove();
	              clone = null;
	          } else {
	              $('.datagrid-header-rownumber,.datagrid-cell-rownumber', panel).removeAttr('style');
	          }
	      });
	  }
	});
});
</script>
</body>
</html>