<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ page import="util.JWebConstant"%>
<%@ page import="com.jweb.dao.JWebUser"%>
<%@ page import="com.jweb.dao.JWebAction"%>
<%@ page import="com.jweb.service.JWebUserService"%>
<%@ page import="com.pay.usercard.dao.PayTranUserCard"%>
<%
String path = request.getContextPath();
//登录和权限判断
JWebUser user = (JWebUser)session.getAttribute("user");
if(!JWebUserService.checkUser(user,request)){
%><script>window.location.href='<%=path %>/jsp/jweb/login.jsp'</script><%
    return;
}
PayTranUserCard payTranUserCard = (PayTranUserCard)request.getAttribute("payTranUserCardUpdate");
%>
<script type="text/javascript">
$(document).ready(function(){});
</script>
<div class="easyui-layout" data-options="fit:true">
    <div data-options="region:'center'">
        <%if(payTranUserCard != null){ %>
        <form id="updatePayTranUserCardForm" method="post">
            <table cellpadding="5" width="100%" style="margin-top:-10px;">
                <tr><td width="100">&nbsp;</td><td>&nbsp;</td></tr>
                    <tr>
                        <td align="right">id：</td>
                        <td><input class="easyui-textbox" type="text" id="updatePayTranUserCardId" name="id" missingMessage="请输入id"
                                validType="length[1,20]" invalidMessage="id为1-20个字符" data-options="required:true"
                                value="<%=payTranUserCard.id != null ? payTranUserCard.id : "" %>"/></td>
                    </tr>
                    <tr>
                        <td align="right">userId：</td>
                        <td><input class="easyui-textbox" type="text" id="updatePayTranUserCardUserId" name="userId" missingMessage="请输入userId"
                                validType="length[1,20]" invalidMessage="userId为1-20个字符" data-options="required:true"
                                value="<%=payTranUserCard.userId != null ? payTranUserCard.userId : "" %>"/></td>
                    </tr>
                    <tr>
                        <td align="right">cardBank：</td>
                        <td><input class="easyui-textbox" type="text" id="updatePayTranUserCardCardBank" name="cardBank" missingMessage="请输入cardBank"
                                validType="length[1,20]" invalidMessage="cardBank为1-20个字符" data-options="required:true"
                                value="<%=payTranUserCard.cardBank != null ? payTranUserCard.cardBank : "" %>"/></td>
                    </tr>
                    <tr>
                        <td align="right">cardType：</td>
                        <td><input class="easyui-textbox" type="text" id="updatePayTranUserCardCardType" name="cardType" missingMessage="请输入cardType"
                                validType="length[1,20]" invalidMessage="cardType为1-20个字符" data-options="required:true"
                                value="<%=payTranUserCard.cardType != null ? payTranUserCard.cardType : "" %>"/></td>
                    </tr>
                    <tr>
                        <td align="right">cardStatus：</td>
                        <td><input class="easyui-textbox" type="text" id="updatePayTranUserCardCardStatus" name="cardStatus" missingMessage="请输入cardStatus"
                                validType="length[1,20]" invalidMessage="cardStatus为1-20个字符" data-options="required:true"
                                value="<%=payTranUserCard.cardStatus != null ? payTranUserCard.cardStatus : "" %>"/></td>
                    </tr>
                    <tr>
                        <td align="right">cardStaRes：</td>
                        <td><input class="easyui-textbox" type="text" id="updatePayTranUserCardCardStaRes" name="cardStaRes" missingMessage="请输入cardStaRes"
                                validType="length[1,20]" invalidMessage="cardStaRes为1-20个字符" data-options="required:true"
                                value="<%=payTranUserCard.cardStaRes != null ? payTranUserCard.cardStaRes : "" %>"/></td>
                    </tr>
                    <tr>
                        <td align="right">cardNo：</td>
                        <td><input class="easyui-textbox" type="text" id="updatePayTranUserCardCardNo" name="cardNo" missingMessage="请输入cardNo"
                                validType="length[1,20]" invalidMessage="cardNo为1-20个字符" data-options="required:true"
                                value="<%=payTranUserCard.cardNo != null ? payTranUserCard.cardNo : "" %>"/></td>
                    </tr>
                    <tr>
                        <td align="right">cardBankBranch：</td>
                        <td><input class="easyui-textbox" type="text" id="updatePayTranUserCardCardBankBranch" name="cardBankBranch" missingMessage="请输入cardBankBranch"
                                validType="length[1,20]" invalidMessage="cardBankBranch为1-20个字符" data-options="required:true"
                                value="<%=payTranUserCard.cardBankBranch != null ? payTranUserCard.cardBankBranch : "" %>"/></td>
                    </tr>
                    <tr>
                        <td align="right">bakOpenTime：</td>
                        <td><input class="easyui-datebox" data-options="editable:false,required:true" id="updatePayTranUserCardBakOpenTime" name="bakOpenTime"missingMessage="请输入bakOpenTime"
                                value="<%=payTranUserCard.bakOpenTime != null ? new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payTranUserCard.bakOpenTime):"" %>"/></td>
                    </tr>
                    <tr>
                        <td align="right">bakOpenNum：</td>
                        <td><input class="easyui-numberbox" type="text" id="updatePayTranUserCardBakOpenNum" name="bakOpenNum" missingMessage="请输入bakOpenNum"
                                data-options="required:true" validType="length[1,10]" invalidMessage="请输入正确bakOpenNum"
                                value="<%=payTranUserCard.bakOpenNum != null ? payTranUserCard.bakOpenNum : "" %>"/></td>
                    </tr>
                    <tr>
                        <td align="right">bakCloseTime：</td>
                        <td><input class="easyui-datebox" data-options="editable:false,required:true" id="updatePayTranUserCardBakCloseTime" name="bakCloseTime"missingMessage="请输入bakCloseTime"
                                value="<%=payTranUserCard.bakCloseTime != null ? new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payTranUserCard.bakCloseTime):"" %>"/></td>
                    </tr>
                    <tr>
                        <td align="right">bakCloseNum：</td>
                        <td><input class="easyui-numberbox" type="text" id="updatePayTranUserCardBakCloseNum" name="bakCloseNum" missingMessage="请输入bakCloseNum"
                                data-options="required:true" validType="length[1,10]" invalidMessage="请输入正确bakCloseNum"
                                value="<%=payTranUserCard.bakCloseNum != null ? payTranUserCard.bakCloseNum : "" %>"/></td>
                    </tr>
                    <tr>
                        <td align="right">bakCloseRes：</td>
                        <td><input class="easyui-textbox" type="text" id="updatePayTranUserCardBakCloseRes" name="bakCloseRes" missingMessage="请输入bakCloseRes"
                                validType="length[1,20]" invalidMessage="bakCloseRes为1-20个字符" data-options="required:true"
                                value="<%=payTranUserCard.bakCloseRes != null ? payTranUserCard.bakCloseRes : "" %>"/></td>
                    </tr>
                    <tr>
                        <td align="right">bakUserId：</td>
                        <td><input class="easyui-textbox" type="text" id="updatePayTranUserCardBakUserId" name="bakUserId" missingMessage="请输入bakUserId"
                                validType="length[1,20]" invalidMessage="bakUserId为1-20个字符" data-options="required:true"
                                value="<%=payTranUserCard.bakUserId != null ? payTranUserCard.bakUserId : "" %>"/></td>
                    </tr>
                    <tr>
                        <td align="right">bakUpdTime：</td>
                        <td><input class="easyui-datebox" data-options="editable:false,required:true" id="updatePayTranUserCardBakUpdTime" name="bakUpdTime"missingMessage="请输入bakUpdTime"
                                value="<%=payTranUserCard.bakUpdTime != null ? new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payTranUserCard.bakUpdTime):"" %>"/></td>
                    </tr>
                    <tr>
                        <td align="right">revFlag：</td>
                        <td><input class="easyui-textbox" type="text" id="updatePayTranUserCardRevFlag" name="revFlag" missingMessage="请输入revFlag"
                                validType="length[1,20]" invalidMessage="revFlag为1-20个字符" data-options="required:true"
                                value="<%=payTranUserCard.revFlag != null ? payTranUserCard.revFlag : "" %>"/></td>
                    </tr>
            </table>
        </form>
        <%} %>
    </div>
    <div data-options="region:'south',border:false" style="text-align:right;padding:5px;">
        <a class="easyui-linkbutton" data-options="iconCls:'icon-ok'" href="javascript:updatePayTranUserCardFormSubmit()" style="width:80px">确定</a>
        <a class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" href="javascript:void(0)" 
            onclick="$('#mainTabs').tabs('close',payTranUserCardUpdatePageTitle)" style="width:80px">取消</a>
    </div>
</div>
<script type="text/javascript">
$('#updatePayTranUserCardForm').form({
    url:'<%=path %>/updatePayTranUserCard.htm',
    onSubmit: function(){
        return $(this).form('validate');
    },
    success:function(data){
        $('#payTranUserCardList').datagrid('reload');
        if(data=='<%=JWebConstant.OK %>'){
            topCenterMessage('<%=JWebConstant.OK %>','修改成功！');
            closeTabFreshList('payTranUserCard',payTranUserCardUpdatePageTitle,payTranUserCardListPageTitle,'payTranUserCardList','<%=path %>/payTranUserCard.htm');
        } else topCenterMessage('<%=JWebConstant.ERROR %>',data);
    }
});
function updatePayTranUserCardFormSubmit(){
    $('#updatePayTranUserCardForm').submit();
}
</script>
