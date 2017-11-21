<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ page import="util.JWebConstant"%>
<%@ page import="com.jweb.dao.JWebUser"%>
<%@ page import="com.jweb.dao.JWebAction"%>
<%@ page import="com.jweb.service.JWebUserService"%>
<%@ page import="com.pay.merchant.dao.PayMerchantNews"%>
<%
String path = request.getContextPath();
//登录和权限判断
JWebUser user = (JWebUser)session.getAttribute("user");
if(!JWebUserService.checkUser(user,request)){
%><script>window.location.href='<%=path %>/jsp/jweb/login.jsp'</script><%
    return;
}
PayMerchantNews payMerchantNews = (PayMerchantNews)request.getAttribute("payMerchantNewsUpdate");
%>
<script type="text/javascript">
$(document).ready(function(){});
</script>
<div class="easyui-layout" data-options="fit:true">
    <div data-options="region:'center'">
        <%if(payMerchantNews != null){ %>
        <form id="updatePayMerchantNewsForm" method="post">
        	<input type="hidden" name="id" value="<%=payMerchantNews.id%>"/>
            <table cellpadding="5" width="100%" style="margin-top:-10px;">
                <tr><td width="100">&nbsp;</td><td>&nbsp;</td></tr>
                    <tr>
                        <td align="right">类型：</td>
                        <td>
                           <select id="updatePayMerchantNewsType" name="type" data-option="editable:false" class="easyui-combobox">
	                           	<option value="0" <%if("0".equals(payMerchantNews.type)){ %>selected<%} %>>新闻</option>
	                           	<option value="1" <%if("1".equals(payMerchantNews.type)){ %>selected<%} %>>公告</option>
	                           	<option value="2" <%if("2".equals(payMerchantNews.type)){ %>selected<%} %>>通知</option>
                           </select>
                        </td>
                    </tr>
                    <tr>
                        <td align="right">标题：</td>
                        <td><input class="easyui-textbox" type="text" style="width:250px;" id="updatePayMerchantNewsTitle" name="title" missingMessage="请输入标题"
                                validType="length[1,15]" invalidMessage="t标题为1-15个字符" data-options="required:true"
                                value="<%=payMerchantNews.title != null ? payMerchantNews.title : "" %>"/></td>
                    </tr>
                    <tr>
                        <td align="right">内容：</td>
                        <td><input class="easyui-textbox" id="updatePayMerchantNewsContent" style="width:300px;height:100px" name="content" missingMessage="请输入content"
                                validType="length[1,200]" invalidMessage="content为1-200个字符" data-options="multiline:true,required:true"
                                value="<%=payMerchantNews.content != null ? payMerchantNews.content : "" %>"/>
                            </td>
                    </tr>
                    <tr>
                        <td align="right">状态：</td>
                        <td>
                            <select id="updatePayMerchantNewsFlag" name="flag2" data-option="editable:false" class="easyui-combobox">
	                           	<option value="0" <%if("0".equals(payMerchantNews.flag2)){ %>selected<%} %>>有效</option>
	                           	<option value="1" <%if("1".equals(payMerchantNews.flag2)){ %>selected<%} %>>无效</option>
                           </select>
                        </td>
                    </tr>
            </table>
        </form>
        <%} %>
    </div>
    <div data-options="region:'south',border:false" style="text-align:right;padding:5px;">
        <a class="easyui-linkbutton" data-options="iconCls:'icon-ok'" href="javascript:updatePayMerchantNewsFormSubmit()" style="width:80px">确定</a>
        <a class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" href="javascript:void(0)" 
            onclick="$('#mainTabs').tabs('close',payMerchantNewsUpdatePageTitle)" style="width:80px">取消</a>
    </div>
</div>
<script type="text/javascript">
$('#updatePayMerchantNewsForm').form({
    url:'<%=path %>/updatePayMerchantNews.htm',
    onSubmit: function(){
        return $(this).form('validate');
    },
    success:function(data){
        $('#payMerchantNewsList').datagrid('reload');
        if(data=='<%=JWebConstant.OK %>'){
            topCenterMessage('<%=JWebConstant.OK %>','修改成功！');
            closeTabFreshList('payMerchantNews',payMerchantNewsUpdatePageTitle,payMerchantNewsListPageTitle,'payMerchantNewsList','<%=path %>/payMerchantNews.htm');
        } else topCenterMessage('<%=JWebConstant.ERROR %>',data);
    }
});
function updatePayMerchantNewsFormSubmit(){
    $('#updatePayMerchantNewsForm').submit();
}
</script>
