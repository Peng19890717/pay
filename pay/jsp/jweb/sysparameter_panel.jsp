<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ page import="com.jweb.dao.JWebUser"%>
<%@ page import="com.jweb.service.JWebUserService"%>
<%@ page import="util.JWebConstant"%>
<%
	String path = request.getContextPath();
//登录和权限判断
JWebUser user = (JWebUser)session.getAttribute("user");
if(!JWebUserService.checkUser(user,request)){
%><script>window.location.href='<%=path %>/jsp/jweb/login.jsp'</script><%
	return;
}
%>
    <table id="sysParameterList" class="easyui-datagrid" style="width:100%;height:100%" rownumbers="true"
		fit="true" data-options="border:false,collapsible:true,autoRowHeight:true,singleSelect:true,url:'<%=path 
			%>/sysParameterManager.htm?flag=0',method:'post',toolbar:'#sysParameterListToolBar'">
    	<thead>
	        <tr>
	            <th field="name" width="30%" align="left">变量名</th>
	            <th field="value" width="30%" align="left">变量值</th>
	            <th field="remark" width="30%" align="left">备注</th>
	            <th field="operation" data-options="formatter:formatParaOperator" width="10%" align="left">操作</th>
	        </tr>
    	</thead>
	</table>
    <div id="sysParameterListToolBar" data-options="border:false" style="text-align:left;padding:5px 5px 5px 5px;">
   		<a class="easyui-linkbutton" data-options="iconCls:'icon-add'" href="javascript:addSysParameterPanelOpen(0)">添加</a>
        <a class="easyui-linkbutton" data-options="iconCls:'icon-config'" href="javascript:executeSysParameter()">执行</a>
        <!-- <a class="easyui-linkbutton" data-options="iconCls:'icon-undo'" href="javascript:recoverySysParameter()">恢复默认设置</a> -->
    </div>
    <div id="addSysParameterPanel" class="easyui-window" title="添加系统变量"
		data-options="closed:true,minimizable:false,collapsible:false,maximizable:false,resizable:false,iconCls:'icon-add',modal:true"
  		style="width:322px;padding-top:15px;padding-left:35px;padding-bottom:15px;">
      <form id="addSysParameterForm" method="post">
      	  <input type="hidden" id="sysParameterFlag" name="sysParameterFlag" value=""/>
          <table cellpadding="5">
              <tr>
                  <td>变量名:</td>
                  <td><input class="easyui-textbox" type="text" id="sysParameterName" name="sysParameterName"
					 missingMessage="请输入变量名" data-options="required:true"></input></td>
              </tr>
              <tr>
                  <td>变量值:</td>
                  <td><input class="easyui-textbox" type="text" id="sysParameterValue" name="sysParameterValue"
                    missingMessage="请输入变量值" data-options="required:true"></input></td>
              </tr>
              <tr>
                   <td>备注:</td>
                   <td><input class="easyui-textbox" id="sysParameterRemark" name="sysParameterRemark" 
                   		data-options="multiline:true" style="height:60px"></input></td>
               </tr>
               <tr>
                   <td></td>
                   <td><a href="javascript:addSysParameterFormSubmit()" class="easyui-linkbutton"  
                   			style="width:100%;" iconCls="icon-ok">保存</a></td>
               </tr>
           </table>
       </form>
   </div>
<script type="text/javascript">
	function formatParaOperator(val,row,index){
	 	return '<a href="javascript:addSysParameterPanelOpen(1)">修改</a>&nbsp;'+
	 	'<a href="javascript:removeSysParameter(\''+row.name+'\')">删除</a>'
	    	;  
	}
	function addSysParameterPanelOpen(flag){
		if(flag == 0){
			$('#sysParameterName').textbox('readonly',false);
			$('#addSysParameterForm').form('clear');
			$('#addSysParameterPanel').panel({title:'添加系统变量',iconCls:'icon-add'});
		}
		else {
			$('#sysParameterName').textbox('readonly',true);
			$('#addSysParameterPanel').panel({title:'修改系统变量',iconCls:'icon-edit'});
			var node = $('#sysParameterList').treegrid('getSelected');
	    	$('#sysParameterName').textbox('setValue',node.name);
	    	$('#sysParameterValue').textbox('setValue',node.value);
	    	$('#sysParameterRemark').textbox('setValue',node.remark);
		}
		document.getElementById('sysParameterFlag').value=flag;
	    $('#addSysParameterPanel').window('open');
	}
	$('#addSysParameterForm').form({
	    url:'<%=path %>/sysParameterManager.htm?flag=1',
	    onSubmit: function(){
	    	var addSysParameterCheck = $(this).form('validate');
	    	if(addSysParameterCheck){
	    		$('#addSysParameterPanel').window('close');
	    		$('#sysParameterList').datagrid('loading');
	    	}
	    	return addSysParameterCheck;
	    },
	    success:function(data){
	    	$('#sysParameterList').datagrid('reload');
	    	if(data=='<%=JWebConstant.OK %>'){
                topCenterMessage('<%=JWebConstant.OK %>','保存成功！');
            } else topCenterMessage('<%=JWebConstant.ERROR %>',data);
	    }
	});
	function removeSysParameter(sysParameterName){
		$.messager.confirm('提示', '确认删除?', function(r){
	    if(!r)return;
	    $('#sysParameterList').datagrid('loading');
	    try{
	   		$.post('<%=path %>/sysParameterManager.htm?flag=2',
	    		{sysParameterName:sysParameterName},
	    		function(data){
	    			$('#sysParameterList').datagrid('reload');
				    if(data=='<%=JWebConstant.OK %>'){
		            	topCenterMessage('<%=JWebConstant.OK %>','删除成功！');
		            } else topCenterMessage('<%=JWebConstant.ERROR %>',data);
	            },
	   			'json'
	        );
	    }catch(e){alert(e);}
	   });
	}
	function executeSysParameter(){
		$.messager.confirm('提示', '确认执行?', function(r){
	    if(!r)return;
	    try{
	   		$.post('<%=path %>/sysParameterManager.htm?flag=3',
	    		function(data){
				    if(data=='<%=JWebConstant.OK %>'){
                     	topCenterMessage('<%=JWebConstant.OK %>','执行成功！');
                    } else topCenterMessage('<%=JWebConstant.ERROR %>',data);
	            },
	   		'json'
	        );
	    }catch(e){alert(e);}
	   });
	}
	function recoverySysParameter(){
		$.messager.confirm('提示', '确认恢复默认设置?', function(r){
	    if(!r)return;
	    try{
	   		$.post('<%=path %>/sysParameterManager.htm?flag=4',
	    		function(data){
	    			$('#sysParameterList').datagrid('reload');
	    			if(data=='<%=JWebConstant.OK %>'){
                     	topCenterMessage('<%=JWebConstant.OK %>','恢复成功！');
                    } else topCenterMessage('<%=JWebConstant.ERROR %>','恢复失败！');
	            },
	   		'json'
	        );
	    }catch(e){alert(e);}
	   });
	}
	function addSysParameterFormSubmit(){
		$('#addSysParameterForm').submit();
	}
</script>
