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
JWebAction actionSearch = ((JWebAction)user.actionMap.get("pxhcJyK2Q3rKTOI1"));
JWebAction actionAdd = ((JWebAction)user.actionMap.get("ACTION_ID_ADD"));
JWebAction actionRemove = ((JWebAction)user.actionMap.get("ACTION_ID_REMOVE"));
JWebAction actionUpdate = ((JWebAction)user.actionMap.get("ACTION_ID_UPDATE"));
%>
<script type="text/javascript">
$(document).ready(function(){});
</script>
<table id="payBankCardBinList" style="width:100%;height:100%" rownumbers="true" pagination="true"
    fit="true" data-options="border:false,collapsible:true,autoRowHeight:true,pageSize:20,singleSelect:true,url:'<%=path 
        %>/payBankCardBin.htm?flag=0',method:'post',toolbar:'#payBankCardBinListToolBar'">
       <thead>
        <tr>
           <th field="issnam" width="20%" align="left" >发卡行名称</th>
           <th field="crdnam" width="15%" align="left" >卡名称</th>
           <th field="binctt" width="15%" align="left" >卡前缀</th>
           <th field="dcflag" width="15%" align="left"  formatter="format_dcflag">卡种</th>
           <th field="binlen" width="10%" align="left" >BIN长度</th>
           <th field="crdlen" width="10%" align="left" >卡号长度</th>
           <th field="issno"  width="15%"  align="left" >主账号</th>
           <!-- <th field="operation" data-options="formatter:formatPayBankCardBinOperator" width="10%" align="left">操作</th> -->
       </tr>
       </thead>
</table>
<div id="payBankCardBinListToolBar" data-options="border:false" style="text-align:left;padding:5px 5px 5px 5px;">
    	发卡行名称：<input type="text" id="searchPayBankCardBinIssnam" name="searchPayBankCardBinIssnam" class="easyui-textbox" value=""  style="width:130px"/>
     	卡前缀：<input type="text" id="searchPayBankCardBinctt" name="searchPayBankCardBinctt" class="easyui-textbox" value=""  style="width:130px"/>
    	卡种：
    	<select class="easyui-combobox" panelHeight="auto" id="searchPayBankCardBinCcflag" 
			data-options="editable:false" name="searchPayBankCardBinCcflag"  style="width:130px">
	    		   <option value="">请选择</option>
	               <option value="0">借记卡</option>
	               <option value="1">贷记卡</option>
	               <option value="2">准贷记卡</option>
	               <option value="3">预付卡</option>
	    </select>
    <%if(actionSearch != null){//角色判断%>
    <a href="javascript:searchPayBankCardBinList()" class="easyui-linkbutton" iconCls="icon-search"><%=actionSearch.name %></a>
    <%} %>
    <%if(actionAdd != null){//角色判断%>
    <a href="javascript:addPayBankCardBinWindowOpen()" class="easyui-linkbutton"
        iconCls="icon-add"><%=actionAdd.name %></a>
    <%} %>
</div>
<div id="updatePayBankCardBinWindow" class="easyui-window" title="修改PayBankCardBin" 
    data-options="iconCls:'icon-edit',closed:true,modal:true" style="width:400px;height:400px;padding:5px;">
    <div class="easyui-layout" data-options="fit:true">
        <div data-options="region:'center'">
            <form id="updatePayBankCardBinForm" method="post">
                <table cellpadding="5">
                    <tr><td width="40%">&nbsp;</td><td width="60%">&nbsp;</td></tr>
                    <tr>
                        <td align="right">发卡行名称：</td>
                        <td><input class="easyui-textbox" type="text" id="updatePayBankCardBinIssnam" name="issnam" missingMessage="请输入issnam"
                            validType="length[1,20]" invalidMessage="issnam为1-20个字符" data-options="required:true"/></td>
                    </tr>
                    <tr>
                        <td align="right">发卡行代码：</td>
                        <td><input class="easyui-textbox" type="text" id="updatePayBankCardBinIssno" name="issno" missingMessage="请输入issno"
                            validType="length[1,20]" invalidMessage="issno为1-20个字符" data-options="required:true"/></td>
                    </tr>
                    <tr>
                        <td align="right">卡名：</td>
                        <td><input class="easyui-textbox" type="text" id="updatePayBankCardBinBankcode" name="bankcode" missingMessage="请输入bankcode"
                            validType="length[1,20]" invalidMessage="bankcode为1-20个字符" data-options="required:true"/></td>
                    </tr>
                    <tr>
                        <td align="right">Fit所在磁道：</td>
                        <td><input class="easyui-textbox" type="text" id="updatePayBankCardBinCrdnam" name="crdnam" missingMessage="请输入crdnam"
                            validType="length[1,20]" invalidMessage="crdnam为1-20个字符" data-options="required:true"/></td>
                    </tr>
                    <tr>
                        <td align="right">Fit起始字节：</td>
                        <td><input class="easyui-textbox" type="text" id="updatePayBankCardBinFitcrk" name="fitcrk" missingMessage="请输入fitcrk"
                            validType="length[1,20]" invalidMessage="fitcrk为1-20个字符" data-options="required:true"/></td>
                    </tr>
                    <tr>
                        <td align="right">Fit长度：</td>
                        <td><input class="easyui-textbox" type="text" id="updatePayBankCardBinFitoff" name="fitoff" missingMessage="请输入fitoff"
                            validType="length[1,20]" invalidMessage="fitoff为1-20个字符" data-options="required:true"/></td>
                    </tr>
                    <tr>
                        <td align="right">主账号起始字节：</td>
                        <td><input class="easyui-textbox" type="text" id="updatePayBankCardBinFitlen" name="fitlen" missingMessage="请输入fitlen"
                            validType="length[1,20]" invalidMessage="fitlen为1-20个字符" data-options="required:true"/></td>
                    </tr>
                    <tr>
                        <td align="right">主账号长度：</td>
                        <td><input class="easyui-textbox" type="text" id="updatePayBankCardBinCrdoff" name="crdoff" missingMessage="请输入crdoff"
                            validType="length[1,20]" invalidMessage="crdoff为1-20个字符" data-options="required:true"/></td>
                    </tr>
                    <tr>
                        <td align="right">主账号：</td>
                        <td><input class="easyui-textbox" type="text" id="updatePayBankCardBinCrdlen" name="crdlen" missingMessage="请输入crdlen"
                            validType="length[1,20]" invalidMessage="crdlen为1-20个字符" data-options="required:true"/></td>
                    </tr>
                    <tr>
                        <td align="right">主账号所在磁道：</td>
                        <td><input class="easyui-textbox" type="text" id="updatePayBankCardBinCrdctt" name="crdctt" missingMessage="请输入crdctt"
                            validType="length[1,20]" invalidMessage="crdctt为1-20个字符" data-options="required:true"/></td>
                    </tr>
                    <tr>
                        <td align="right">发卡行标识起始字节：</td>
                        <td><input class="easyui-textbox" type="text" id="updatePayBankCardBinCrdcrk" name="crdcrk" missingMessage="请输入crdcrk"
                            validType="length[1,20]" invalidMessage="crdcrk为1-20个字符" data-options="required:true"/></td>
                    </tr>
                    <tr>
                        <td align="right">发卡行标识长度：</td>
                        <td><input class="easyui-textbox" type="text" id="updatePayBankCardBinBinoff" name="binoff" missingMessage="请输入binoff"
                            validType="length[1,20]" invalidMessage="binoff为1-20个字符" data-options="required:true"/></td>
                    </tr>
                    <tr>
                        <td align="right">发卡行标识取值：</td>
                        <td><input class="easyui-textbox" type="text" id="updatePayBankCardBinBinlen" name="binlen" missingMessage="请输入binlen"
                            validType="length[1,20]" invalidMessage="binlen为1-20个字符" data-options="required:true"/></td>
                    </tr>
                    <tr>
                        <td align="right">发卡行标识读取磁道：</td>
                        <td><input class="easyui-textbox" type="text" id="updatePayBankCardBinBinctt" name="binctt" missingMessage="请输入binctt"
                            validType="length[1,20]" invalidMessage="binctt为1-20个字符" data-options="required:true"/></td>
                    </tr>
                    <tr>
                        <td align="right">卡种：</td>
                        <td><input class="easyui-textbox" type="text" id="updatePayBankCardBinBincrk" name="bincrk" missingMessage="请输入bincrk"
                            validType="length[1,20]" invalidMessage="bincrk为1-20个字符" data-options="required:true"/></td>
                    </tr>
                </table>
            </form>
        </div>
        <div data-options="region:'south',border:false" style="text-align:right;padding:5px 0 0;">
            <a class="easyui-linkbutton" data-options="iconCls:'icon-ok'" href="javascript:updatePayBankCardBinFormSubmit()" style="width:80px">修改</a>
            <a class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" href="javascript:void(0)" 
                onclick="$('#updatePayBankCardBinWindow').window('close')" style="width:80px">返回</a>
        </div>
    </div>
</div>
<script type="text/javascript">
function format_dcflag(data,row, index){
	if(data=="0"){
		return "借记卡";
	}else if(data=="1"){
		return "贷计卡";
	}else if(data=="2"){
		return "准贷计卡";
	}else if(data=="3"){
		return "预付卡";
	}else{
		return "";
	}
}
$('#payBankCardBinList').datagrid({
    onLoadError: function(data){
        topCenterMessage('<%=JWebConstant.ERROR %>','数据加载失败！');
    },
    onLoadSuccess: function(data){
        $(this).datagrid('fixRownumber');//行号自适应宽度
    }
});
function formatPayBankCardBinOperator(val,row,index){
     var tmp=
         <%if(actionUpdate != null){//角色判断%>
         "<a href=\"javascript:updatePayBankCardBinWindowOpen()\"><%=actionUpdate.name %></a>&nbsp;"+
         <%}%>
         <%if(actionRemove !=null ){//角色判断%>
         "<a href=\"javascript:removePayBankCardBin('"+row.issnam+"')\"><%=actionRemove.name %></a>&nbsp;"+
         <%}%>
         "";
     return tmp;
}
function searchPayBankCardBinList(){        
    $('#payBankCardBinList').datagrid('load',{  
          issnam:$('#searchPayBankCardBinIssnam').val(),
          binctt:$('#searchPayBankCardBinctt').val(),//卡前缀
          dcflag:$('#searchPayBankCardBinCcflag').combobox('getValue')
    });  
}
$('#addPayBankCardBinForm').form({
    url:'<%=path %>/addPayBankCardBin.htm',
    onSubmit: function(){
        var addPayBankCardBinCheck=$(this).form('validate');
        if(addPayBankCardBinCheck){            $('#addPayBankCardBinWindow').window('close');
            $('#payBankCardBinList').datagrid('loading');
        }        return addPayBankCardBinCheck;
    },
    success:function(data){
        $('#payBankCardBinList').datagrid('reload');
        if(data=='<%=JWebConstant.OK %>'){
           topCenterMessage('<%=JWebConstant.OK %>','添加成功！');
        } else topCenterMessage('<%=JWebConstant.ERROR %>',data);
    }
});
function addPayBankCardBinFormSubmit(){
    $('#addPayBankCardBinForm').submit();
}
function addPayBankCardBinWindowOpen(){
    $('#addPayBankCardBinForm').form('clear');
    $('#addPayBankCardBinWindow').window('open');
}
$('#updatePayBankCardBinForm').form({
    url:'<%=path %>/updatePayBankCardBin.htm',
    onSubmit: function(){
        var updatePayBankCardBinCheck=$(this).form('validate');
        if(updatePayBankCardBinCheck){            $('#updatePayBankCardBinWindow').window('close');
            $('#payBankCardBinList').datagrid('loading');
        }        return updatePayBankCardBinCheck;
    },
    success:function(data){
        $('#payBankCardBinList').datagrid('reload');
        if(data=='<%=JWebConstant.OK %>'){
            topCenterMessage('<%=JWebConstant.OK %>','修改成功！');
        } else topCenterMessage('<%=JWebConstant.ERROR %>',data);
    }
});
function updatePayBankCardBinFormSubmit(){
    $('#updatePayBankCardBinForm').submit();
}
function updatePayBankCardBinWindowOpen(){
    var updatePayBankCardBinForm = $('#updatePayBankCardBinForm');
    updatePayBankCardBinForm.form('clear');
    $('#updatePayBankCardBinWindow').window('open');
    var node = $('#payBankCardBinList').treegrid('getSelected');
    $('#updatePayBankCardBinIssnam').textbox('setValue',node.issnam);
    $('#updatePayBankCardBinIssno').textbox('setValue',node.issno);
    $('#updatePayBankCardBinBankcode').textbox('setValue',node.bankcode);
    $('#updatePayBankCardBinCrdnam').textbox('setValue',node.crdnam);
    $('#updatePayBankCardBinFitcrk').textbox('setValue',node.fitcrk);
    $('#updatePayBankCardBinFitoff').textbox('setValue',node.fitoff);
    $('#updatePayBankCardBinFitlen').textbox('setValue',node.fitlen);
    $('#updatePayBankCardBinCrdoff').textbox('setValue',node.crdoff);
    $('#updatePayBankCardBinCrdlen').textbox('setValue',node.crdlen);
    $('#updatePayBankCardBinCrdctt').textbox('setValue',node.crdctt);
    $('#updatePayBankCardBinCrdcrk').textbox('setValue',node.crdcrk);
    $('#updatePayBankCardBinBinoff').textbox('setValue',node.binoff);
    $('#updatePayBankCardBinBinlen').textbox('setValue',node.binlen);
    $('#updatePayBankCardBinBinctt').textbox('setValue',node.binctt);
    $('#updatePayBankCardBinBincrk').textbox('setValue',node.bincrk);
    $('#updatePayBankCardBinDcflag').textbox('setValue',node.dcflag);
}
function removePayBankCardBin(issnam){
    $.messager.confirm('提示', '确认删除?', function(r){
    if(!r)return;
    $('#testUserList').datagrid('loading');
    try{
        $.post('<%=path %>/removePayBankCardBin.htm',
            {issnam:issnam},
            function(data){
                $('#payBankCardBinList').datagrid('reload');
                if(data=='<%=JWebConstant.OK %>'){
                    topCenterMessage('<%=JWebConstant.OK %>','删除成功！');
                } else topCenterMessage('<%=JWebConstant.ERROR %>',data);
            },
           'json'
        );
    }catch(e){alert(e);}
   });
}
</script>
