<%@page import="com.pay.coopbank.dao.PayChannelBankRelation"%>
<%@page import="com.pay.coopbank.service.PayCoopBankService"%>
<%@page import="com.pay.bank.dao.PayBank"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ page import="util.JWebConstant"%>
<%@ page import="com.jweb.dao.JWebUser"%>
<%@ page import="com.jweb.dao.JWebAction"%>
<%@ page import="com.jweb.service.JWebUserService"%>
<%@ page import="com.pay.coopbank.dao.PayCoopBank"%>
<%
String path = request.getContextPath();
//登录和权限判断
JWebUser user = (JWebUser)session.getAttribute("user");
if(!JWebUserService.checkUser(user,request)){
%><script>window.location.href='<%=path %>/jsp/jweb/login.jsp'</script><%
    return;
}
PayCoopBank channel = (PayCoopBank)request.getAttribute("payCoopBank");
String arr[] = channel.payScanFlag.split(",");
Map cbRelationMap = (Map)request.getAttribute("cbRelationMap");
String payChannelMaxAmtB2CJUpdate = (String)request.getAttribute("payChannelMaxAmtB2CJUpdate");
String payChannelMaxAmtB2CXUpdate = (String)request.getAttribute("payChannelMaxAmtB2CXUpdate");
String payChannelMaxAmtB2BUpdate = (String)request.getAttribute("payChannelMaxAmtB2BUpdate");
String payChannelMaxAmtKJJUpdate = (String)request.getAttribute("payChannelMaxAmtKJJUpdate");
String payChannelMaxAmtKJDJUpdate = (String)request.getAttribute("payChannelMaxAmtKJDJUpdate");
String payChannelMaxAmtZFBUpdate = (String)request.getAttribute("payChannelMaxAmtZFBUpdate");
String payChannelMaxAmtQQUpdate = (String)request.getAttribute("payChannelMaxAmtQQUpdate");
String payChannelMaxAmtYHKUpdate = (String)request.getAttribute("payChannelMaxAmtYHKUpdate");
String payChannelMaxAmtWXWAPUpdate = (String)request.getAttribute("payChannelMaxAmtWXWAPUpdate");
String payChannelMaxAmtDSUpdate = (String)request.getAttribute("payChannelMaxAmtDSUpdate");
%>
<div class="easyui-layout" data-options="fit:true">
    <div data-options="region:'center'">
        <%if(channel != null){ %>
        <table cellpadding="5" width="100%" style="margin-top:-10px;">
            <tr><td width="100">&nbsp;</td><td width="350">&nbsp;</td><td width="100">&nbsp;</td><td>&nbsp;</td></tr>
            <tr>
                <td align="right">银行编码：</td>
                <td>${payCoopBank.bankCode}</td>
                <td align="right">支付系统ID：</td>
                <td>${payCoopBank.paySysId }</td>
            </tr>
            <tr>
                <td align="right">银行名称：</td>
                <td>${payCoopBank.bankName }</td>
                <td align="right">银行联行号：</td>
                <td>${payCoopBank.bankRelNo }</td>
            </tr>
            <tr>
                <td align="right">托管银行标志：</td>
                <td>
                	<c:if test="${payCoopBank.trtBankFlg eq 'Y' }">是</c:if>
                	<c:if test="${payCoopBank.trtBankFlg eq 'N' }">否</c:if>
                </td>
                <td align="right">银行结算账号：</td>
                <td>${payCoopBank.stlAcNo }</td>
            </tr>
            <tr>
                <td align="right">结算发起时间：</td>
                <td><fmt:formatDate value="${payCoopBank.accountTime }" pattern="yyyy-MM-dd"/></td>
                <td align="right">金融机构许可证：</td>
                <td>${payCoopBank.certNo }</td>
            </tr>
            <tr>
                <td align="right">工商注册号：</td>
                <td>${payCoopBank.regNo }</td>
                <td align="right">银行法人代表：</td>
                <td>${payCoopBank.legRep }</td>
            </tr>
            <tr>
                <td align="right">客户经理：</td>
                <td>${payCoopBank.custMgr }</td>
                <td align="right">联系电话：</td>
                <td>${payCoopBank.telNo }</td>
            </tr>
            <tr>
                <td align="right">Email地址：</td>
                <td>${payCoopBank.bankEmail }</td>
                <td align="right">通信地址：</td>
                <td>${payCoopBank.bankAddress }</td>
            </tr>
             <tr>
                <td align="right">线上退款：</td>
                <td>
                	<c:if test="${payCoopBank.refundOnline eq '0' }">支持</c:if>
                	<c:if test="${payCoopBank.refundOnline eq '1' }">不支持</c:if>
                </td>
                <td align="right">线上对账：</td>
                <td>
                	<c:if test="${payCoopBank.accountOnline eq '0' }">支持</c:if>
                	<c:if test="${payCoopBank.accountOnline eq '1' }">不支持</c:if>
                </td>
            </tr>
            <tr>
                <td align="right">快捷支持重发验证码：</td>
                <td>
                	<c:if test="${payCoopBank.quickResendSms eq '0' }">支持</c:if>
                	<c:if test="${payCoopBank.quickResendSms eq '1' }">不支持</c:if>
                </td>
                <td align="right">结算周期：</td>
                    <td>
                    <%if("D".equals(channel.stlTimeType))out.print("按天"); %>
				    <%if("D".equals(channel.stlTimeType))out.print("T+"+channel.stlTime); %>
				</td>
            </tr>
            <tr>
                <td align="right"><strong>消费费率</strong></td>
                <td></td>
                <td></td>
                <td></td>
            </tr>
            <tr>
                <td align="right" width="120px">B2C借记卡：</td>
                <td>${payCoopBank.b2cjjFeeName }&nbsp;&nbsp;<%-- 最大额：<%= payChannelMaxAmtB2CJUpdate == null ?"未指定":payChannelMaxAmtB2CJUpdate %>&nbsp;元 --%></td>
                <td align="right" width="120px">B2C信用卡：</td>
                <td>${payCoopBank.b2cxyFeeName }&nbsp;&nbsp;<%-- 最大额：<%= payChannelMaxAmtB2CXUpdate == null ?"未指定":payChannelMaxAmtB2CXUpdate %>&nbsp;元 --%></td>
            </tr>
            <tr>
                <td align="right" width="120px">B2B：</td>
                <td>${payCoopBank.b2bFeeName }&nbsp;&nbsp;<%-- 最大额：<%= payChannelMaxAmtB2BUpdate == null ?"未指定":payChannelMaxAmtB2BUpdate %>&nbsp;元 --%></td>
                <td align="right" width="120px">快捷借记卡：</td>
                <td>${payCoopBank.kjJJFeeName }&nbsp;&nbsp;<%-- 最大额：<%= payChannelMaxAmtKJJUpdate == null ?"未指定":payChannelMaxAmtKJJUpdate %>&nbsp;元 --%></td>
            </tr>
            <tr>
                <td align="right" width="120px">快捷信用卡：</td>
                <td>${payCoopBank.kjDJFeeName }&nbsp;&nbsp;<%-- 最大额：<%= payChannelMaxAmtKJDJUpdate == null ?"未指定":payChannelMaxAmtKJDJUpdate %>&nbsp;元 --%></td>
                <%if("1".equals(arr[0])) {%>
                <td align="right" width="120px">支付宝扫码：</td>
                <td>${payCoopBank.zfbFeeName }&nbsp;&nbsp;单笔最大额：<%= payChannelMaxAmtZFBUpdate == null ?"未指定":payChannelMaxAmtZFBUpdate %>&nbsp;元（0为无限制）</td>
                <%} %>
            </tr> 
            <tr>
            	<%if("1".equals(arr[1])) {%>
            	<td align="right" width="120px">微信扫码：</td>
                <td>${payCoopBank.yhkFeeName }&nbsp;&nbsp;单笔最大额：<%= payChannelMaxAmtYHKUpdate == null ?"未指定":payChannelMaxAmtYHKUpdate %>&nbsp;元（0为无限制）</td>
                <%} %>
                <%if("1".equals(arr[2])) {%>
            	<td align="right" width="120px">微信WAP：</td>
                <td>${payCoopBank.wxwapFeeName }&nbsp;&nbsp;单笔最大额：<%= payChannelMaxAmtWXWAPUpdate == null ?"未指定":payChannelMaxAmtWXWAPUpdate %>&nbsp;元（0为无限制）</td>
                <%} %>
            </tr>
            <tr>
            	<%if("1".equals(arr[3])) {%>
            	<td align="right" width="120px">QQ扫码：</td>
                <td>${payCoopBank.qqFeeName }&nbsp;&nbsp;单笔最大额：<%= payChannelMaxAmtQQUpdate == null ?"未指定":payChannelMaxAmtQQUpdate %>&nbsp;元（0为无限制）</td>
                <%} %>
                <td></td>
                <td></td>
            </tr>
            <tr>
                <td align="right"><strong>其他费率</strong></td>
                <td></td>
                <td></td>
                <td></td>
            </tr>
            <tr>
                <td align="right" width="120px">转账费率：</td>
                <td>${payCoopBank.zzFeeName }</td>
                <td align="right" width="120px">退款费率：</td>
                <td>${payCoopBank.tkFeeName }</td>
            </tr>
            <tr>
                <td align="right" width="120px">提现费率：</td>
                <td>${payCoopBank.txFeeName }</td>
                <td align="right" width="120px">代收费率：</td>
                <td>${payCoopBank.dsFeeName }&nbsp;&nbsp;<%-- 最大额：<%= payChannelMaxAmtDSUpdate == null ?"未指定":payChannelMaxAmtDSUpdate %>&nbsp;元 --%></td>
            </tr>
            <tr>
                <td align="right" width="120px">代付费率：</td>
                <td>${payCoopBank.dfFeeName }</td>
                <td></td>
                <td></td>
            </tr>
            <tr>
                <td align="right">创建人员：</td>
                <td>${payCoopBank.creOperId }</td>
                <td align="right">创建时间：</td>
                <td><fmt:formatDate value="${payCoopBank.creTime }" pattern="yyyy-MM-dd HH:mm:ss"/></td>
            </tr>
            <tr>
                <td align="right">最后维护人员：</td>
                <td>${payCoopBank.lstUptOperId }</td>
                <td align="right">最后维护时间：</td>
                <td><fmt:formatDate value="${payCoopBank.lstUptTime }" pattern="yyyy-MM-dd HH:mm:ss"/></td>
            </tr>
            <tr>
				<td align="right" valign="top">支持银行：</td>
				<td colspan="3">
				<strong>网银支付（<c:if test="${payCoopBank.payWebFlag eq '0' }">已开启</c:if><c:if test="${payCoopBank.payWebFlag eq '1' }">已关闭</c:if>）</strong><br/>
				<%
					String bankCodes = ","+(String)request.getAttribute(("bankCodes"));
					for(int i = 0 ; i < PayCoopBankService.BANK_CODE_NAME_LIST.size(); i++) {
						PayBank payBank = PayCoopBankService.BANK_CODE_NAME_LIST.get(i);
						PayChannelBankRelation r = (PayChannelBankRelation)cbRelationMap.get(channel.bankCode+","+payBank.bankCode);
						if(bankCodes.contains(","+payBank.bankCode+",")){
							if(r==null||r.supportedUserType==null)continue;
							out.print("【" + payBank.bankName + 
								(r!=null&&r.supportedUserType.indexOf("0,")!=-1?("—借记卡（"+ r.webDebitMaxAmt/100 +"元）"):"")+
								(r!=null&&r.supportedUserType.indexOf("1,")!=-1?("—贷记卡（"+r.webCreditMaxAmt/100 +"元）"):"")+
								(r!=null&&r.supportedUserType.indexOf("4,")!=-1?("—对公（"+ r.webPublicMaxAmt/100 +"元）"):"")+"】");
						}
					}%><br/><br/>
				<strong>快捷支付（<c:if test="${payCoopBank.payQuickFlag eq '0' }">已开启</c:if><c:if test="${payCoopBank.payQuickFlag eq '1' }">已关闭</c:if>） </strong><br/>
				<%
					bankCodes = ","+(String)request.getAttribute(("bankCodes"));
					for(int i = 0 ; i < PayCoopBankService.BANK_CODE_NAME_LIST.size(); i++) {
						PayBank payBank = PayCoopBankService.BANK_CODE_NAME_LIST.get(i);
						PayChannelBankRelation r = (PayChannelBankRelation)cbRelationMap.get(channel.bankCode+","+payBank.bankCode);
						if(bankCodes.contains(","+payBank.bankCode+",")){
							if(r==null||r.quickUserType==null)continue;
							out.print("【" + payBank.bankName + 
								(r!=null&&r.quickUserType.indexOf("0,")!=-1?("—借记卡（"+ r.quickDebitMaxAmt/100 +"元）"):"")+
								(r!=null&&r.quickUserType.indexOf("1,")!=-1?("—贷记卡（"+ r.quickCeditMaxAmt/100 +"元）"):"")+"】");
						}
					}%><br/><br/>
				<strong>代收（<c:if test="${payCoopBank.payReceiveFlag eq '0' }">已开启</c:if><c:if test="${payCoopBank.payReceiveFlag eq '1' }">已关闭</c:if>） </strong><br/>
				<%
					bankCodes = ","+(String)request.getAttribute(("bankCodes"));
					for(int i = 0 ; i < PayCoopBankService.BANK_CODE_NAME_LIST.size(); i++) {
						PayBank payBank = PayCoopBankService.BANK_CODE_NAME_LIST.get(i);
						PayChannelBankRelation r = (PayChannelBankRelation)cbRelationMap.get(channel.bankCode+","+payBank.bankCode);
						if(bankCodes.contains(","+payBank.bankCode+",")){
							if(r==null||r.receiveUserType==null)continue;
							out.print("【" + payBank.bankName + "（"+ r.receiveMaxAmt/100 +"元）】");
						}
					}%><br/><br/>
				<strong>代付（<c:if test="${payCoopBank.payWithdrawFlag eq '0' }">已开启</c:if><c:if test="${payCoopBank.payWithdrawFlag eq '1' }">已关闭</c:if>）</strong><br/>
				<%
				    bankCodes = ","+(String)request.getAttribute(("bankCodes"));
					for(int i = 0 ; i < PayCoopBankService.BANK_CODE_NAME_LIST.size(); i++) {
						PayBank payBank = PayCoopBankService.BANK_CODE_NAME_LIST.get(i);
						PayChannelBankRelation r = (PayChannelBankRelation)cbRelationMap.get(channel.bankCode+","+payBank.bankCode);
						if(bankCodes.contains(","+payBank.bankCode+",")){
							if(r==null||r.withdrawUserType==null)continue;
							out.print("【" + payBank.bankName + "】");
						}
					}%>
				</td>
			</tr>
            <tr>
                <td align="right" valign="top">备注信息：</td>
                <td colspan="3">${payCoopBank.remark }</td>
            </tr>
        </table>
        <%} %>
    </div>
    <div data-options="region:'south',border:false" style="text-align:right;padding:5px;">
        <a class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" href="javascript:void(0)" 
            onclick="$('#mainTabs').tabs('close',payCoopBankDetailPageTitle)" style="width:80px">关闭</a>
    </div>
</div>
