<%@page import="java.text.SimpleDateFormat"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ page import="util.JWebConstant"%>
<%@ page import="com.jweb.dao.JWebUser"%>
<%@ page import="com.jweb.dao.JWebAction"%>
<%@ page import="com.jweb.service.JWebUserService"%>
<%@ page import="com.pay.user.dao.PayTranUserInfo"%>
<%
String path = request.getContextPath();
//登录和权限判断
JWebUser user = (JWebUser)session.getAttribute("user");
if(!JWebUserService.checkUser(user,request)){
%><script>window.location.href='<%=path %>/jsp/jweb/login.jsp'</script><%
    return;
}
PayTranUserInfo payTranUserInfo = (PayTranUserInfo)request.getAttribute("payTranUserInfo");
%>
<div class="easyui-layout" data-options="fit:true">
    <div data-options="region:'center'">
        <form id="setPayTranUserInfoForm" method="post">
            <table cellpadding="5" width="100%" style="margin-top:20px;">
                <tr><td width="100">&nbsp;</td><td>&nbsp;</td></tr>
                	<input type="hidden" name="id"value="<%=payTranUserInfo.id  %>">
                    <tr>
                        <td align="right">客户账号：</td>
                        <td><input class="easyui-textbox" type="text" id="userId" name="userId" value="<%=payTranUserInfo.userId%>"/></td>
                    </tr>
                    <tr>
                        <td align="right">客户姓名：</td>
                        <td><input class="easyui-textbox" type="text" id="realName" name="realName" value="<%=payTranUserInfo.realName %>"/></td>
                    </tr>
                    <tr>
                        <td align="right">证件类型：</td>
                        <td><input type="hidden" id="cretType" name="cretType" value="<%=payTranUserInfo.cretType %>"/>
                        <input class="easyui-textbox" type="text" id="cretType_temp" name="cretType_temp" value="<%=payTranUserInfo.cretType %>"/></td>
                    </tr>
                     <tr>
                        <td align="right">证件号码：</td>
                        <td><input class="easyui-textbox" type="text" id="cretNo" name="cretNo" value="<%=payTranUserInfo.cretNo %>"/></td>
                    </tr>
                     <tr>
                        <td align="right">证件有效期：</td>
                        <td><input  type="radio" id="long_validTime" name="validTime" value="0" />永久<input  type="radio" id="short_validTime" name="validTime" value="1" checked="checked"/>
                        	按时间<input type="text" id="in_validTime" name="in_validTime" data-options="editable:false" class="easyui-datebox" value=""  style="width:100px"/> </td>
		 
                    </tr>
                    <tr>
                        <td align="right">身份认证标示：</td>
                        <td><select name="userStatus" id="userStatus" class="easyui-combobox" data-options="editable:false">
							  
							   <option value="0" <%=payTranUserInfo.userStatus.equals("0")?"selected=\"selected\"":""%>>未认证</option>
							   <option value="1"  <%=payTranUserInfo.userStatus.equals("1")?"selected=\"selected\"":""%>>审核中</option>
							   <option value="2" <%=payTranUserInfo.userStatus.equals("2")?"selected=\"selected\"":""%>>已通过 </option>
							   <option value="3" <%=payTranUserInfo.userStatus.equals("3")?"selected=\"selected\"":""%>>未通过 </option>  					  
							 </select></td>
                    </tr>
                      <tr>
                        <td align="right">证件照片：</td>
                        <td><img src="<%=payTranUserInfo.credPhotoFront%>"/></td>
                    </tr>
                    <tr>
                        <td align="right">认证未通过原因：</td>
                       <td><textarea rows="10" cols="80" id="remark" name="remark"></textarea></td>
                    </tr>
              </table>
        </form>
    </div>
    <div data-options="region:'south',border:false" style="text-align:right;padding:5px;">
     <a class="easyui-linkbutton" data-options="iconCls:'icon-ok'" href="javascript:setPayTranUserInfoFormSubmit()" style="width:80px">提交</a>
        <a class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" href="javascript:void(0)" 
            onclick="$('#mainTabs').tabs('close',payTranUserInfoDetailPageTitle)" style="width:80px">关闭</a>
    </div>
</div>
<script>
$(document).ready(function () {
	$(".easyui-textbox").attr('disabled','disabled');
	//转变证件类型值为可见的中文。证件类型 01身份证,02军官证,03护照,04回乡证,05台胞证,06警官证,07士兵证,99其他 默认01
	//转换开始
	var cretType_temp=$('#cretType_temp').val();
	if(cretType_temp=='01')
	{
		$('#cretType_temp').attr('value','身份证');
	}
	if(cretType_temp=='02')
	{
		$('#cretType_temp').attr('value','军官证');
	}
	if(cretType_temp=='03')
	{
		$('#cretType_temp').attr('value','护照');
	}
	if(cretType_temp=='04')
	{
		$('#cretType_temp').attr('value','回乡证');
	}
	if(cretType_temp=='05')
	{
		$('#cretType_temp').attr('value','台胞证');
	}
	if(cretType_temp=='06')
	{
		$('#cretType_temp').attr('value','警官证');
	}
	if(cretType_temp=='07')
	{
		$('#cretType_temp').attr('value','士兵证');
	}
	if(cretType_temp=='99')
	{
		$('#cretType_temp').attr('value','其他');
	}
	//转换结束。
});

function getCurentDateStr()  
{   
    //获取完整的日期  
	var date=new Date;  
	var year=date.getFullYear();   
	var month=date.getMonth()+1;  
	month =(month<10 ? "0"+month:month);   
	var mydate = (year.toString()+month.toString());  
	return mydate;
} 

$('#setPayTranUserInfoForm').form({
    url:'<%=path %>/updatePayTranUserInfo.htm',
    onSubmit: function(){
    	var short_validTime_flag = $('#short_validTime').is(":checked"); 
    	var selectDate= new Date(Date.parse($('#in_validTime').datebox('getValue').replace(/-/g,  "/")));   
        var d1 = new Date(new Date().getFullYear(),new Date().getMonth(),new Date().getDate());  
    	if(short_validTime_flag)
    	{
    		if($('#in_validTime').datebox('getValue')=='')
    		{
    			topCenterMessage('<%=JWebConstant.ERROR %>','按时间中值不能为空');
    			return false;
    		}
    		if(selectDate <= d1){
    			topCenterMessage('<%=JWebConstant.ERROR %>','证件有效期不合法');
    			return false;
    		}
    	}
    	
        return $(this).form('validate');
    },
    success:function(data){
    	 $('#payTranUserInfoList').datagrid('reload');
        if(data=='<%=JWebConstant.OK %>'){
            topCenterMessage('<%=JWebConstant.OK %>','操作成功！');
            closeTabFreshList('payTranUserInfo',payTranUserInfoDetailPageTitle,payTranUserInfoListPageTitle,'payTranUserInfoList','<%=path %>/payTranUserInfo.htm');
        } else topCenterMessage('<%=JWebConstant.ERROR %>',data);
    }
});
function setPayTranUserInfoFormSubmit(){
    $('#setPayTranUserInfoForm').submit();
}

</script>