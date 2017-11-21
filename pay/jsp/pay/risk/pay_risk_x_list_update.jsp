<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="util.JWebConstant"%>
<%@ page import="com.jweb.dao.JWebUser"%>
<%@ page import="com.jweb.dao.JWebAction"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@ page import="com.jweb.service.JWebUserService"%>
<%@ page import="com.pay.risk.dao.PayRiskXList"%>
<%
String path = request.getContextPath();
//登录和权限判断
JWebUser user = (JWebUser)session.getAttribute("user");
if(!JWebUserService.checkUser(user,request)){
%><script>window.location.href='<%=path %>/jsp/jweb/login.jsp'</script><%
    return;
}
PayRiskXList payRiskXList = (PayRiskXList)request.getAttribute("payRiskXListUpdate");
%>
<script type="text/javascript">
$(document).ready(function(){});
</script>
<div class="easyui-layout" data-options="fit:true">
    <div data-options="region:'center'">
        <%if(payRiskXList != null){ %>
        <form id="updatePayRiskXListForm" method="post">
        	<input type="hidden" name="id" value="${payRiskXListUpdate.id }">
            <table cellpadding="5" width="100%" style="margin-top:-10px;">
                <tr><td width="100">&nbsp;</td><td>&nbsp;</td></tr>
                    <tr>
                        <td align="right">客户类型：</td>
                        <td>
                        	<c:if test="${payRiskXListUpdate.clientType eq '0' }">个人</c:if>
                        	<c:if test="${payRiskXListUpdate.clientType eq '1' }">商户</c:if>
                        	<c:if test="${payRiskXListUpdate.clientType eq '2' }">手机号</c:if>
                        	<c:if test="${payRiskXListUpdate.clientType eq '3' }">银行卡号</c:if>
                        </td>
                    </tr>
                    <tr>
                    <td align="right">
                    	<c:if test="${payRiskXListUpdate.clientType eq '0' }">客户编码：</c:if>
                    	<c:if test="${payRiskXListUpdate.clientType eq '1' }">客户编码：</c:if>
                    	<c:if test="${payRiskXListUpdate.clientType eq '2' }">手机号：</c:if>
                    	<c:if test="${payRiskXListUpdate.clientType eq '3' }">银行卡号：</c:if>
                     </td>   
                        <td>${payRiskXListUpdate.clientCode }</td>
                    </tr>
                    <tr>
                        <td align="right">登记日期：</td>
                        <td><%=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payRiskXList.regdtTime)%></td>
                    </tr>
                    <tr>
                        <td align="right">X名单状态：</td>
                        <td>
                            <select name="xType" id="updatePayRiskXListXType" class="easyui-combobox"  
                             panelHeight="auto" data-options="editable:false">
                        		<option value="1" <%="1".equals(payRiskXList.xType)?"selected":"" %>>白名单</option>
                        		<!-- option value="2" <%="2".equals(payRiskXList.xType)?"selected":"" %>>灰名单</option -->  
                        		<option value="3" <%="3".equals(payRiskXList.xType)?"selected":"" %>>黑名单</option>  
                        		<option value="4" <%="4".equals(payRiskXList.xType)?"selected":"" %>>红名单</option>
                    		</select> 
                        </td>
                    </tr>
                    <tr>
                        <td align="right">备注：</td>
                        <td>
                            <input class="easyui-textbox" type="text" id="updatePayRiskXListCasBuf" name="casBuf"
                                validType="length[0,200]" invalidMessage="备注控制在200个字符" data-options="multiline:true" 
                                value="${payRiskXListUpdate.casBuf}" style="width:240px;height:70px"/>
                        </td>
                    </tr>
            </table>
        </form>
        <%} %>
    </div>
    <div data-options="region:'south',border:false" style="text-align:right;padding:5px;">
        <a class="easyui-linkbutton" data-options="iconCls:'icon-ok'" href="javascript:updatePayRiskXListFormSubmit()" style="width:80px">确定</a>
        <a class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" href="javascript:void(0)" 
            onclick="$('#mainTabs').tabs('close',payRiskXListUpdatePageTitle)" style="width:80px">取消</a>
    </div>
</div>
<script type="text/javascript">
$('#updatePayRiskXListForm').form({
    url:'<%=path %>/updatePayRiskXList.htm',
    onSubmit: function(){
        return $(this).form('validate');
    },
    success:function(data){
        $('#payRiskXListList').datagrid('reload');
        if(data=='<%=JWebConstant.OK %>'){
            topCenterMessage('<%=JWebConstant.OK %>','修改成功！');
            closeTabFreshList('payRiskXList',payRiskXListUpdatePageTitle,payRiskXListListPageTitle,'payRiskXListList','<%=path %>/payRiskXList.htm');
        } else topCenterMessage('<%=JWebConstant.ERROR %>',data);
    }
});
function updatePayRiskXListFormSubmit(){
    $('#updatePayRiskXListForm').submit();
}
</script>