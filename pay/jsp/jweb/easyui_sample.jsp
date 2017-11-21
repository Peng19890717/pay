<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ page import="util.JWebConstant"%>
<%
String path = request.getContextPath();
%>
	<link rel="stylesheet" type="text/css" href="<%=path %>/js/upload/uploadify.css"  />
    <script type="text/javascript" src="<%=path %>/js/upload/jquery.uploadify.min.js"></script>
<script type="text/javascript">
$(document).ready(function(){
 	  infoFormat();
});
//用样式格式化字符串
function infoFormat(){
	var info = $('.formatDateText');
	for(var i=0;i<info.length;i++) {
 		info[i].value = dateFormat(info[i].value);
	} 	
	info = $('.formatDate');
	for(var i=0;i<info.length;i++) {	  		
  		var date =info[i].innerText;
  		if(date==undefined || date==null) {
  			date = info[i].innerHTML;
  			info[i].innerHTML = dateFormat(date);
  		} else info[i].innerText = dateFormat(date);
	}
}
function dateFormat(date){
	var str = '';
    if(date.length==8) str = date.substr(0,4)+"-"+date.substr(4,2)+"-"+date.substr(6,2);
    else if(date.length==14) str = date.substr(0,4)+"-"+date.substr(4,2)+"-"+date.substr(6,2)+" "+date.substr(8,2)+":"+date.substr(10,2)+":"+date.substr(12,2);
    else str = date;
    return str;
}
//文件上传
$(function() { 
	$("#files").uploadify({
		'fileObjName' : 'files',
		'swf': '<%=path %>/js/upload/uploadify.swf?'+ Math.random(),
		'uploader': '<%=path %>/uploadFile.htm',
		'buttonClass':'easyui-filebox',
		'width':150,
		'height':22,
		'fileSizeLimit':'15MB',//KB,MB,GB
        'fileTypeExts' : '*.jpg;*.png;*.gif;*.bmp;*.doc;*.txt;*.rar;*.vob',//指定文件格式
		'onCancel' : function(file) {
		},
		'onUploadSuccess' : function(file,data,result) {
			document.getElementById("uploadImg").src="<%=path %>/upload/"+file.name;
			//alert(data);			
		},
		'onInit': function () {
        	$("#uploadify-queue").hide(); 
        } 
	});
});
//表格样式
$('#sysParameterListTest').datagrid({
	onLoadError: function(data){
		topCenterMessage('<%=JWebConstant.ERROR %>','数据加载失败！');
	},onLoadSuccess:function(data){  
        $('.editcls').linkbutton({text:'修改',iconCls:'icon-edit'});
        $('.removecls').linkbutton({text:'删除',plain:true,iconCls:'icon-remove'});
    }
});
function formatParaOperatorTest(val,row,index){
 	return "<a  class=\"editcls\" href=\"javascript:alert('"+row.name +"')\">修改</a>&nbsp;"+
 		"<a class=\"removecls\" href=\"javascript:alert('"+row.value+"')\">删除</a>&nbsp;";
}
</script>
<div align="center">
订单详情
	<div class="easyui-panel" title="文件上传" style="width:500px;">
		<table>
		<tr>
			<td width="100">身份证照片：</td>
			<td><input name="files" id="files" type="text"/></td>
		</tr>
		<tr>
			<td></td>
			<td><img src="" id="uploadImg"/></td>
		</tr>
		</table>
	</div>
	<br/>
	<div class="easyui-panel" title="表格样式" style="width:100%;height:150px">
	    <table id="sysParameterListTest" style="width:90%;height:100%" rownumbers="true"
			fit="true" data-options="border:false,collapsible:true,autoRowHeight:true,singleSelect:true,url:'<%=path 
				%>/sysParameterManager.htm?flag=0',method:'post'">
	    	<thead>
		        <tr>
		            <th field="name" width="20%" align="left">变量名</th>
		            <th field="value" width="30%" align="left">变量值</th>
		            <th field="remark" width="30%" align="left">备注</th>
		            <th field="operation" data-options="formatter:formatParaOperatorTest" width="10%" align="left">操作</th>
		        </tr>
	    	</thead>
		</table>
	</div>
	<br/>
	<div id="tabsMenu" class="easyui-menu" style="width:120px;">  
	    <div name="close">关闭</div>  
	    <div name="Other">关闭其他</div>  
	    <div name="All">关闭所有</div>
	</div> 
	<div class="easyui-panel" title="样式变换" style="width:700px;padding:5px" id="other">
		<div id="myStylePanel">你好</div>
		<input class="easyui-textbox" name="myStyleTest" id="myStyleTest" data-options="required:true,style:{width:'200px'}">
		<input type="checkbox" name="myStyleCheckboxTest" id="myStyleCheckboxTest" onclick="myStyleTestFun(this)">
		<a href="javascript:openPageWindow()" class="easyui-linkbutton" iconCls="icon-add">打开网页面板</a>
		<a href="javascript:openPageOnTab()">tab中打开页面</a>
		<script type="text/javascript">
			function openPageWindow(){
				$('#selectTestWindow').window('open');
				$('#selectTestWindow').window('refresh','<%=path %>/jsp/jweb/easyui_sample_select.jsp');
			}
			function openPageOnTab(){
				var tabTmp = $('#mainTabs'),title='测试页面';
	   			tabTmp.tabs('add',{id:'tabTest',title: title,closable: true,border:false});
	            $('#tabTest').panel('refresh','<%=path %>/jsp/jweb/easyui_sample_select.jsp');
	            tabTmp.tabs('select',title);
			}
			$(function(){
				$('#other').bind('contextmenu',function(e){
					e.preventDefault();
					//e.stopPropagation();
					$('#tabsMenu').menu('show', {
						left: e.pageX,
						top: e.pageY
					});
				});
			});
			$('#tabsMenu').menu({
		        onClick : function (item) {
		            alert(item.name);
		        }
		    });
			function myStyleTestFun(obj){
				if(obj.checked){
					$('#myStyleTest').textbox({
						required:true,
						style:{width:'100px'}
				    });
				} else {
				 	$('#myStyleTest').textbox({
						required:false,
						style:{width:'200px'}
				    });
				}
				//$.parser.parse($('#myStyleTest').parent());
				/*
				$("#myStylePanel").panel({
					title:"面板",
					class:"easyui-panel"
			    });
				$.parser.parse($('#myStyleTest').parent());
				*/
			}
		</script>
	</div>
	<input class="formatDateText" type="text" value="20150702"/><span class="formatDate">20150702</span>
</div>
<div id="selectTestWindow" class="easyui-window" title="选择用户" 
    data-options="iconCls:'icon-add',closed:true,modal:true" style="width:800px;height:500px;padding:5px;">
</div>
