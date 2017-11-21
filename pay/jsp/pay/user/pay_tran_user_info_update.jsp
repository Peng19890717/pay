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
PayTranUserInfo payTranUserInfo = (PayTranUserInfo)request.getAttribute("payTranUserInfoUpdate");
%>
<script type="text/javascript">
$(document).ready(function(){});
</script>
<div class="easyui-layout" data-options="fit:true">
    <div data-options="region:'center'">
        <%if(payTranUserInfo != null){ %>
        <form id="updatePayTranUserInfoForm" method="post">
            <table cellpadding="5" width="100%" style="margin-top:-10px;">
                <tr><td width="100">&nbsp;</td><td>&nbsp;</td></tr>
                    <tr>
                        <td align="right">id：</td>
                        <td><input class="easyui-textbox" type="text" id="updatePayTranUserInfoId" name="id" missingMessage="请输入id"
                                validType="length[1,20]" invalidMessage="id为1-20个字符" data-options="required:true"
                                value="<%=payTranUserInfo.id != null ? payTranUserInfo.id : "" %>"/></td>
                    </tr>
                    <tr>
                        <td align="right">userId：</td>
                        <td><input class="easyui-textbox" type="text" id="updatePayTranUserInfoUserId" name="userId" missingMessage="请输入userId"
                                validType="length[1,20]" invalidMessage="userId为1-20个字符" data-options="required:true"
                                value="<%=payTranUserInfo.userId != null ? payTranUserInfo.userId : "" %>"/></td>
                    </tr>
                    <tr>
                        <td align="right">loginPassword：</td>
                        <td><input class="easyui-textbox" type="text" id="updatePayTranUserInfoLoginPassword" name="loginPassword" missingMessage="请输入loginPassword"
                                validType="length[1,20]" invalidMessage="loginPassword为1-20个字符" data-options="required:true"
                                value="<%=payTranUserInfo.loginPassword != null ? payTranUserInfo.loginPassword : "" %>"/></td>
                    </tr>
                    <tr>
                        <td align="right">passwordStrength：</td>
                        <td><input class="easyui-textbox" type="text" id="updatePayTranUserInfoPasswordStrength" name="passwordStrength" missingMessage="请输入passwordStrength"
                                validType="length[1,20]" invalidMessage="passwordStrength为1-20个字符" data-options="required:true"
                                value="<%=payTranUserInfo.passwordStrength != null ? payTranUserInfo.passwordStrength : "" %>"/></td>
                    </tr>
                    <tr>
                        <td align="right">loginPwdFailCount：</td>
                        <td><input class="easyui-numberbox" type="text" id="updatePayTranUserInfoLoginPwdFailCount" name="loginPwdFailCount" missingMessage="请输入loginPwdFailCount"
                                data-options="required:true" validType="length[1,10]" invalidMessage="请输入正确loginPwdFailCount"
                                value="<%=payTranUserInfo.loginPwdFailCount != null ? payTranUserInfo.loginPwdFailCount : "" %>"/></td>
                    </tr>
                    <tr>
                        <td align="right">loginPwdLastTime：</td>
                        <td><input class="easyui-datebox" data-options="editable:false,required:true" id="updatePayTranUserInfoLoginPwdLastTime" name="loginPwdLastTime"missingMessage="请输入loginPwdLastTime"
                                value="<%=payTranUserInfo.loginPwdLastTime != null ? new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payTranUserInfo.loginPwdLastTime):"" %>"/></td>
                    </tr>
                    <tr>
                        <td align="right">payPassword：</td>
                        <td><input class="easyui-textbox" type="text" id="updatePayTranUserInfoPayPassword" name="payPassword" missingMessage="请输入payPassword"
                                validType="length[1,20]" invalidMessage="payPassword为1-20个字符" data-options="required:true"
                                value="<%=payTranUserInfo.payPassword != null ? payTranUserInfo.payPassword : "" %>"/></td>
                    </tr>
                    <tr>
                        <td align="right">payPwdStrength：</td>
                        <td><input class="easyui-textbox" type="text" id="updatePayTranUserInfoPayPwdStrength" name="payPwdStrength" missingMessage="请输入payPwdStrength"
                                validType="length[1,20]" invalidMessage="payPwdStrength为1-20个字符" data-options="required:true"
                                value="<%=payTranUserInfo.payPwdStrength != null ? payTranUserInfo.payPwdStrength : "" %>"/></td>
                    </tr>
                    <tr>
                        <td align="right">payPwdFailCount：</td>
                        <td><input class="easyui-numberbox" type="text" id="updatePayTranUserInfoPayPwdFailCount" name="payPwdFailCount" missingMessage="请输入payPwdFailCount"
                                data-options="required:true" validType="length[1,10]" invalidMessage="请输入正确payPwdFailCount"
                                value="<%=payTranUserInfo.payPwdFailCount != null ? payTranUserInfo.payPwdFailCount : "" %>"/></td>
                    </tr>
                    <tr>
                        <td align="right">payPwdLastTime：</td>
                        <td><input class="easyui-datebox" data-options="editable:false,required:true" id="updatePayTranUserInfoPayPwdLastTime" name="payPwdLastTime"missingMessage="请输入payPwdLastTime"
                                value="<%=payTranUserInfo.payPwdLastTime != null ? new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payTranUserInfo.payPwdLastTime):"" %>"/></td>
                    </tr>
                    <tr>
                        <td align="right">befname：</td>
                        <td><input class="easyui-textbox" type="text" id="updatePayTranUserInfoBefname" name="befname" missingMessage="请输入befname"
                                validType="length[1,20]" invalidMessage="befname为1-20个字符" data-options="required:true"
                                value="<%=payTranUserInfo.befname != null ? payTranUserInfo.befname : "" %>"/></td>
                    </tr>
                    <tr>
                        <td align="right">realName：</td>
                        <td><input class="easyui-textbox" type="text" id="updatePayTranUserInfoRealName" name="realName" missingMessage="请输入realName"
                                validType="length[1,20]" invalidMessage="realName为1-20个字符" data-options="required:true"
                                value="<%=payTranUserInfo.realName != null ? payTranUserInfo.realName : "" %>"/></td>
                    </tr>
                    <tr>
                        <td align="right">cretType：</td>
                        <td><input class="easyui-textbox" type="text" id="updatePayTranUserInfoCretType" name="cretType" missingMessage="请输入cretType"
                                validType="length[1,20]" invalidMessage="cretType为1-20个字符" data-options="required:true"
                                value="<%=payTranUserInfo.cretType != null ? payTranUserInfo.cretType : "" %>"/></td>
                    </tr>
                    <tr>
                        <td align="right">cretNo：</td>
                        <td><input class="easyui-textbox" type="text" id="updatePayTranUserInfoCretNo" name="cretNo" missingMessage="请输入cretNo"
                                validType="length[1,20]" invalidMessage="cretNo为1-20个字符" data-options="required:true"
                                value="<%=payTranUserInfo.cretNo != null ? payTranUserInfo.cretNo : "" %>"/></td>
                    </tr>
                    <tr>
                        <td align="right">email：</td>
                        <td><input class="easyui-textbox" type="text" id="updatePayTranUserInfoEmail" name="email" missingMessage="请输入email"
                                validType="length[1,20]" invalidMessage="email为1-20个字符" data-options="required:true"
                                value="<%=payTranUserInfo.email != null ? payTranUserInfo.email : "" %>"/></td>
                    </tr>
                    <tr>
                        <td align="right">emailStatus：</td>
                        <td><input class="easyui-textbox" type="text" id="updatePayTranUserInfoEmailStatus" name="emailStatus" missingMessage="请输入emailStatus"
                                validType="length[1,20]" invalidMessage="emailStatus为1-20个字符" data-options="required:true"
                                value="<%=payTranUserInfo.emailStatus != null ? payTranUserInfo.emailStatus : "" %>"/></td>
                    </tr>
                    <tr>
                        <td align="right">fax：</td>
                        <td><input class="easyui-textbox" type="text" id="updatePayTranUserInfoFax" name="fax" missingMessage="请输入fax"
                                validType="length[1,20]" invalidMessage="fax为1-20个字符" data-options="required:true"
                                value="<%=payTranUserInfo.fax != null ? payTranUserInfo.fax : "" %>"/></td>
                    </tr>
                    <tr>
                        <td align="right">province：</td>
                        <td><input class="easyui-textbox" type="text" id="updatePayTranUserInfoProvince" name="province" missingMessage="请输入province"
                                validType="length[1,20]" invalidMessage="province为1-20个字符" data-options="required:true"
                                value="<%=payTranUserInfo.province != null ? payTranUserInfo.province : "" %>"/></td>
                    </tr>
                    <tr>
                        <td align="right">city：</td>
                        <td><input class="easyui-textbox" type="text" id="updatePayTranUserInfoCity" name="city" missingMessage="请输入city"
                                validType="length[1,20]" invalidMessage="city为1-20个字符" data-options="required:true"
                                value="<%=payTranUserInfo.city != null ? payTranUserInfo.city : "" %>"/></td>
                    </tr>
                    <tr>
                        <td align="right">area：</td>
                        <td><input class="easyui-textbox" type="text" id="updatePayTranUserInfoArea" name="area" missingMessage="请输入area"
                                validType="length[1,20]" invalidMessage="area为1-20个字符" data-options="required:true"
                                value="<%=payTranUserInfo.area != null ? payTranUserInfo.area : "" %>"/></td>
                    </tr>
                    <tr>
                        <td align="right">address：</td>
                        <td><input class="easyui-textbox" type="text" id="updatePayTranUserInfoAddress" name="address" missingMessage="请输入address"
                                validType="length[1,20]" invalidMessage="address为1-20个字符" data-options="required:true"
                                value="<%=payTranUserInfo.address != null ? payTranUserInfo.address : "" %>"/></td>
                    </tr>
                    <tr>
                        <td align="right">zip：</td>
                        <td><input class="easyui-textbox" type="text" id="updatePayTranUserInfoZip" name="zip" missingMessage="请输入zip"
                                validType="length[1,20]" invalidMessage="zip为1-20个字符" data-options="required:true"
                                value="<%=payTranUserInfo.zip != null ? payTranUserInfo.zip : "" %>"/></td>
                    </tr>
                    <tr>
                        <td align="right">nationality：</td>
                        <td><input class="easyui-textbox" type="text" id="updatePayTranUserInfoNationality" name="nationality" missingMessage="请输入nationality"
                                validType="length[1,20]" invalidMessage="nationality为1-20个字符" data-options="required:true"
                                value="<%=payTranUserInfo.nationality != null ? payTranUserInfo.nationality : "" %>"/></td>
                    </tr>
                    <tr>
                        <td align="right">job：</td>
                        <td><input class="easyui-textbox" type="text" id="updatePayTranUserInfoJob" name="job" missingMessage="请输入job"
                                validType="length[1,20]" invalidMessage="job为1-20个字符" data-options="required:true"
                                value="<%=payTranUserInfo.job != null ? payTranUserInfo.job : "" %>"/></td>
                    </tr>
                    <tr>
                        <td align="right">tel：</td>
                        <td><input class="easyui-textbox" type="text" id="updatePayTranUserInfoTel" name="tel" missingMessage="请输入tel"
                                validType="length[1,20]" invalidMessage="tel为1-20个字符" data-options="required:true"
                                value="<%=payTranUserInfo.tel != null ? payTranUserInfo.tel : "" %>"/></td>
                    </tr>
                    <tr>
                        <td align="right">mobile：</td>
                        <td><input class="easyui-textbox" type="text" id="updatePayTranUserInfoMobile" name="mobile" missingMessage="请输入mobile"
                                validType="length[1,20]" invalidMessage="mobile为1-20个字符" data-options="required:true"
                                value="<%=payTranUserInfo.mobile != null ? payTranUserInfo.mobile : "" %>"/></td>
                    </tr>
                    <tr>
                        <td align="right">birthday：</td>
                        <td><input class="easyui-textbox" type="text" id="updatePayTranUserInfoBirthday" name="birthday" missingMessage="请输入birthday"
                                validType="length[1,20]" invalidMessage="birthday为1-20个字符" data-options="required:true"
                                value="<%=payTranUserInfo.birthday != null ? payTranUserInfo.birthday : "" %>"/></td>
                    </tr>
                    <tr>
                        <td align="right">gender：</td>
                        <td><input class="easyui-textbox" type="text" id="updatePayTranUserInfoGender" name="gender" missingMessage="请输入gender"
                                validType="length[1,20]" invalidMessage="gender为1-20个字符" data-options="required:true"
                                value="<%=payTranUserInfo.gender != null ? payTranUserInfo.gender : "" %>"/></td>
                    </tr>
                    <tr>
                        <td align="right">publicPhoto：</td>
                        <td><input class="easyui-textbox" type="text" id="updatePayTranUserInfoPublicPhoto" name="publicPhoto" missingMessage="请输入publicPhoto"
                                validType="length[1,20]" invalidMessage="publicPhoto为1-20个字符" data-options="required:true"
                                value="<%=payTranUserInfo.publicPhoto != null ? payTranUserInfo.publicPhoto : "" %>"/></td>
                    </tr>
                    <tr>
                        <td align="right">credPhotoFront：</td>
                        <td><input class="easyui-textbox" type="text" id="updatePayTranUserInfoCredPhotoFront" name="credPhotoFront" missingMessage="请输入credPhotoFront"
                                validType="length[1,20]" invalidMessage="credPhotoFront为1-20个字符" data-options="required:true"
                                value="<%=payTranUserInfo.credPhotoFront != null ? payTranUserInfo.credPhotoFront : "" %>"/></td>
                    </tr>
                    <tr>
                        <td align="right">credPhotoBack：</td>
                        <td><input class="easyui-textbox" type="text" id="updatePayTranUserInfoCredPhotoBack" name="credPhotoBack" missingMessage="请输入credPhotoBack"
                                validType="length[1,20]" invalidMessage="credPhotoBack为1-20个字符" data-options="required:true"
                                value="<%=payTranUserInfo.credPhotoBack != null ? payTranUserInfo.credPhotoBack : "" %>"/></td>
                    </tr>
                    <tr>
                        <td align="right">sendType：</td>
                        <td><input class="easyui-textbox" type="text" id="updatePayTranUserInfoSendType" name="sendType" missingMessage="请输入sendType"
                                validType="length[1,20]" invalidMessage="sendType为1-20个字符" data-options="required:true"
                                value="<%=payTranUserInfo.sendType != null ? payTranUserInfo.sendType : "" %>"/></td>
                    </tr>
                    <tr>
                        <td align="right">userStatus：</td>
                        <td><input class="easyui-textbox" type="text" id="updatePayTranUserInfoUserStatus" name="userStatus" missingMessage="请输入userStatus"
                                validType="length[1,20]" invalidMessage="userStatus为1-20个字符" data-options="required:true"
                                value="<%=payTranUserInfo.userStatus != null ? payTranUserInfo.userStatus : "" %>"/></td>
                    </tr>
                    <tr>
                        <td align="right">checkUserId：</td>
                        <td><input class="easyui-textbox" type="text" id="updatePayTranUserInfoCheckUserId" name="checkUserId" missingMessage="请输入checkUserId"
                                validType="length[1,20]" invalidMessage="checkUserId为1-20个字符" data-options="required:true"
                                value="<%=payTranUserInfo.checkUserId != null ? payTranUserInfo.checkUserId : "" %>"/></td>
                    </tr>
                    <tr>
                        <td align="right">checkTime：</td>
                        <td><input class="easyui-datebox" data-options="editable:false,required:true" id="updatePayTranUserInfoCheckTime" name="checkTime"missingMessage="请输入checkTime"
                                value="<%=payTranUserInfo.checkTime != null ? new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payTranUserInfo.checkTime):"" %>"/></td>
                    </tr>
                    <tr>
                        <td align="right">remark：</td>
                        <td><input class="easyui-textbox" type="text" id="updatePayTranUserInfoRemark" name="remark" missingMessage="请输入remark"
                                validType="length[1,20]" invalidMessage="remark为1-20个字符" data-options="required:true"
                                value="<%=payTranUserInfo.remark != null ? payTranUserInfo.remark : "" %>"/></td>
                    </tr>
                    <tr>
                        <td align="right">revFlag：</td>
                        <td><input class="easyui-textbox" type="text" id="updatePayTranUserInfoRevFlag" name="revFlag" missingMessage="请输入revFlag"
                                validType="length[1,20]" invalidMessage="revFlag为1-20个字符" data-options="required:true"
                                value="<%=payTranUserInfo.revFlag != null ? payTranUserInfo.revFlag : "" %>"/></td>
                    </tr>
                    <tr>
                        <td align="right">mobileStatus：</td>
                        <td><input class="easyui-textbox" type="text" id="updatePayTranUserInfoMobileStatus" name="mobileStatus" missingMessage="请输入mobileStatus"
                                validType="length[1,20]" invalidMessage="mobileStatus为1-20个字符" data-options="required:true"
                                value="<%=payTranUserInfo.mobileStatus != null ? payTranUserInfo.mobileStatus : "" %>"/></td>
                    </tr>
                    <tr>
                        <td align="right">userType：</td>
                        <td><input class="easyui-textbox" type="text" id="updatePayTranUserInfoUserType" name="userType" missingMessage="请输入userType"
                                validType="length[1,20]" invalidMessage="userType为1-20个字符" data-options="required:true"
                                value="<%=payTranUserInfo.userType != null ? payTranUserInfo.userType : "" %>"/></td>
                    </tr>
                    <tr>
                        <td align="right">registerTime：</td>
                        <td><input class="easyui-datebox" data-options="editable:false,required:true" id="updatePayTranUserInfoRegisterTime" name="registerTime"missingMessage="请输入registerTime"
                                value="<%=payTranUserInfo.registerTime != null ? new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payTranUserInfo.registerTime):"" %>"/></td>
                    </tr>
                    <tr>
                        <td align="right">certSubmitTime：</td>
                        <td><input class="easyui-datebox" data-options="editable:false,required:true" id="updatePayTranUserInfoCertSubmitTime" name="certSubmitTime"missingMessage="请输入certSubmitTime"
                                value="<%=payTranUserInfo.certSubmitTime != null ? new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payTranUserInfo.certSubmitTime):"" %>"/></td>
                    </tr>
                    <tr>
                        <td align="right">registerType：</td>
                        <td><input class="easyui-textbox" type="text" id="updatePayTranUserInfoRegisterType" name="registerType" missingMessage="请输入registerType"
                                validType="length[1,20]" invalidMessage="registerType为1-20个字符" data-options="required:true"
                                value="<%=payTranUserInfo.registerType != null ? payTranUserInfo.registerType : "" %>"/></td>
                    </tr>
                    <tr>
                        <td align="right">trialStatus：</td>
                        <td><input class="easyui-textbox" type="text" id="updatePayTranUserInfoTrialStatus" name="trialStatus" missingMessage="请输入trialStatus"
                                validType="length[1,20]" invalidMessage="trialStatus为1-20个字符" data-options="required:true"
                                value="<%=payTranUserInfo.trialStatus != null ? payTranUserInfo.trialStatus : "" %>"/></td>
                    </tr>
            </table>
        </form>
        <%} %>
    </div>
    <div data-options="region:'south',border:false" style="text-align:right;padding:5px;">
        <a class="easyui-linkbutton" data-options="iconCls:'icon-ok'" href="javascript:updatePayTranUserInfoFormSubmit()" style="width:80px">确定</a>
        <a class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" href="javascript:void(0)" 
            onclick="$('#mainTabs').tabs('close',payTranUserInfoUpdatePageTitle)" style="width:80px">取消</a>
    </div>
</div>
<script type="text/javascript">
$('#updatePayTranUserInfoForm').form({
    url:'<%=path %>/updatePayTranUserInfo.htm',
    onSubmit: function(){
        return $(this).form('validate');
    },
    success:function(data){
        $('#payTranUserInfoList').datagrid('reload');
        if(data=='<%=JWebConstant.OK %>'){
            topCenterMessage('<%=JWebConstant.OK %>','修改成功！');
            closeTabFreshList('payTranUserInfo',payTranUserInfoUpdatePageTitle,payTranUserInfoListPageTitle,'payTranUserInfoList','<%=path %>/payTranUserInfo.htm');
        } else topCenterMessage('<%=JWebConstant.ERROR %>',data);
    }
});
function updatePayTranUserInfoFormSubmit(){
    $('#updatePayTranUserInfoForm').submit();
}
</script>
