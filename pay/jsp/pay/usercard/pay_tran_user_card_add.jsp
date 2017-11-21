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
%>
<script type="text/javascript">
$(document).ready(function(){});
</script>
<div class="easyui-layout" data-options="fit:true">
    <div data-options="region:'center'">
        <form id="addPayTranUserCardForm" method="post">
            <table cellpadding="5" width="100%" style="margin-top:-10px;">
                <tr><td width="100">&nbsp;</td><td>&nbsp;</td></tr>
                    <tr>
                        <td align="right">id：</td>
                        <td><input class="easyui-textbox" type="text" id="addPayTranUserCardId" name="id" missingMessage="请输入id"
                                validType="length[1,20]" invalidMessage="id为1-20个字符" data-options="required:true"/></td>
                    </tr>
                    <tr>
                        <td align="right">userId：</td>
                        <td><input class="easyui-textbox" type="text" id="addPayTranUserCardUserId" name="userId" missingMessage="请输入userId"
                                validType="length[1,20]" invalidMessage="userId为1-20个字符" data-options="required:true"/></td>
                    </tr>
                    <tr>
                        <td align="right">cardBank：</td>
                        <td><input class="easyui-textbox" type="text" id="addPayTranUserCardCardBank" name="cardBank" missingMessage="请输入cardBank"
                                validType="length[1,20]" invalidMessage="cardBank为1-20个字符" data-options="required:true"/></td>
                    </tr>
                    <tr>
                        <td align="right">cardType：</td>
                        <td><input class="easyui-textbox" type="text" id="addPayTranUserCardCardType" name="cardType" missingMessage="请输入cardType"
                                validType="length[1,20]" invalidMessage="cardType为1-20个字符" data-options="required:true"/></td>
                    </tr>
                    <tr>
                        <td align="right">cardStatus：</td>
                        <td><input class="easyui-textbox" type="text" id="addPayTranUserCardCardStatus" name="cardStatus" missingMessage="请输入cardStatus"
                                validType="length[1,20]" invalidMessage="cardStatus为1-20个字符" data-options="required:true"/></td>
                    </tr>
                    <tr>
                        <td align="right">cardStaRes：</td>
                        <td><input class="easyui-textbox" type="text" id="addPayTranUserCardCardStaRes" name="cardStaRes" missingMessage="请输入cardStaRes"
                                validType="length[1,20]" invalidMessage="cardStaRes为1-20个字符" data-options="required:true"/></td>
                    </tr>
                    <tr>
                        <td align="right">cardNo：</td>
                        <td><input class="easyui-textbox" type="text" id="addPayTranUserCardCardNo" name="cardNo" missingMessage="请输入cardNo"
                                validType="length[1,20]" invalidMessage="cardNo为1-20个字符" data-options="required:true"/></td>
                    </tr>
                    <tr>
                        <td align="right">cardBankBranch：</td>
                        <td><input class="easyui-textbox" type="text" id="addPayTranUserCardCardBankBranch" name="cardBankBranch" missingMessage="请输入cardBankBranch"
                                validType="length[1,20]" invalidMessage="cardBankBranch为1-20个字符" data-options="required:true"/></td>
                    </tr>
                    <tr>
                        <td align="right">bakOpenTime：</td>
                        <td><input class="easyui-datebox" data-options="editable:false,required:true" id="PayTranUserCardBakOpenTime" name="bakOpenTime"missingMessage="请输入bakOpenTime"/></td>
                    </tr>
                    <tr>
                        <td align="right">bakOpenNum：</td>
                        <td><input class="easyui-numberbox" type="text" id="addPayTranUserCardBakOpenNum" name="bakOpenNum" missingMessage="请输入bakOpenNum"
                                data-options="required:true" validType="length[1,10]" invalidMessage="请输入正确bakOpenNum"/></td>
                    </tr>
                    <tr>
                        <td align="right">bakCloseTime：</td>
                        <td><input class="easyui-datebox" data-options="editable:false,required:true" id="PayTranUserCardBakCloseTime" name="bakCloseTime"missingMessage="请输入bakCloseTime"/></td>
                    </tr>
                    <tr>
                        <td align="right">bakCloseNum：</td>
                        <td><input class="easyui-numberbox" type="text" id="addPayTranUserCardBakCloseNum" name="bakCloseNum" missingMessage="请输入bakCloseNum"
                                data-options="required:true" validType="length[1,10]" invalidMessage="请输入正确bakCloseNum"/></td>
                    </tr>
                    <tr>
                        <td align="right">bakCloseRes：</td>
                        <td><input class="easyui-textbox" type="text" id="addPayTranUserCardBakCloseRes" name="bakCloseRes" missingMessage="请输入bakCloseRes"
                                validType="length[1,20]" invalidMessage="bakCloseRes为1-20个字符" data-options="required:true"/></td>
                    </tr>
                    <tr>
                        <td align="right">bakUserId：</td>
                        <td><input class="easyui-textbox" type="text" id="addPayTranUserCardBakUserId" name="bakUserId" missingMessage="请输入bakUserId"
                                validType="length[1,20]" invalidMessage="bakUserId为1-20个字符" data-options="required:true"/></td>
                    </tr>
                    <tr>
                        <td align="right">bakUpdTime：</td>
                        <td><input class="easyui-datebox" data-options="editable:false,required:true" id="PayTranUserCardBakUpdTime" name="bakUpdTime"missingMessage="请输入bakUpdTime"/></td>
                    </tr>
                    <tr>
                        <td align="right">revFlag：</td>
                        <td><input class="easyui-textbox" type="text" id="addPayTranUserCardRevFlag" name="revFlag" missingMessage="请输入revFlag"
                                validType="length[1,20]" invalidMessage="revFlag为1-20个字符" data-options="required:true"/></td>
                    </tr>
            </table>
        </form>
    </div>
    <div data-options="region:'south',border:false" style="text-align:right;padding:5px;">
        <a class="easyui-linkbutton" data-options="iconCls:'icon-ok'" href="javascript:addPayTranUserCardFormSubmit()" style="width:80px">确定</a>
        <a class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" href="javascript:void(0)" 
            onclick="$('#addPayTranUserCardForm').form('clear')" style="width:80px">取消</a>
    </div>
</div>
<script type="text/javascript">
$('#addPayTranUserCardForm').form({
    url:'<%=path %>/addPayTranUserCard.htm',
    onSubmit: function(){
        return $(this).form('validate');
    },
    success:function(data){
        if(data=='<%=JWebConstant.OK %>'){
            topCenterMessage('<%=JWebConstant.OK %>','添加成功！');
            closeTabFreshList('payTranUserCard',payTranUserCardAddPageTitle,payTranUserCardListPageTitle,'payTranUserCardList','<%=path %>/payTranUserCard.htm');
        } else topCenterMessage('<%=JWebConstant.ERROR %>',data);
    }
});
function addPayTranUserCardFormSubmit(){
    $('#addPayTranUserCardForm').submit();
}
</script>
