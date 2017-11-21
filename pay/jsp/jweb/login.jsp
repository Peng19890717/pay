<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ page import="util.JWebConstant"%>
<%
String path = request.getContextPath();
String loginInfo = (String)session.getAttribute("LOGIN_INFO");
if(loginInfo != null)session.removeAttribute("LOGIN_INFO");
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>系统登录</title>
<link rel="stylesheet" type="text/css" href="<%=path %>/css/jweb_main.css">
<link rel="stylesheet" type="text/css" href="<%=path %>/js/jquery-easyui-1.4/themes/default/easyui.css">
<!--[if IE]>
<link rel="stylesheet" type="text/css" href="<%=path %>/js/jquery-easyui-1.4/themes/easyui-ie.css">
<![endif]-->
<link rel="stylesheet" type="text/css" href="<%=path %>/js/jquery-easyui-1.4/themes/icon.css">
<script type="text/javascript" src="<%=path %>/js/jquery-easyui-1.4/jquery.min.js"></script>
<script type="text/javascript" src="<%=path %>/js/jquery-easyui-1.4/jquery.easyui.min.js"></script>
<script type="text/javascript" src="<%=path %>/js/jquery-easyui-1.4/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="<%=path %>/js/jweb.js"></script>
<script type="text/javascript">
var scriptPath='<%=path %>';
var loginAtOtherNo='<%=JWebConstant.LOGIN_AT_OTHER %>';
var loginTimeOut='<%=JWebConstant.LOGIN_TIMEOUT %>';
var undefinedError='<%=JWebConstant.UNDEFINED_ERROR %>';
</script>
<script  src="<%=path %>/js/PassGuardCtrl.js" type="text/javascript"  charset="UTF-8" language="javascript"></script>
<script type="text/javascript">
$(document).ready(function(){
	<%=loginInfo != null ? "topCenterMessage('"+JWebConstant.ERROR+"','"+JWebConstant.LOGIN_INFO_MAP.get(loginInfo)+"');" : "" %>
    $('#loginForm').form({
        url:'<%=path %>/login.htm',
        onSubmit: function(){
            var loginNameLen = $('#loginName').val().length;
            var passwordLen = $('#password').val().length;
            var validCodeLen = $('#validCode').val().length;
            <%if("1".equals(JWebConstant.SYS_CONFIG.get("SEND_MOBILE_MSG_FOR_LOGIN"))){ %>
            var shortMessageCodeLen = $('#shortMessageCode').val().length;
            <%} %>
            if(loginNameLen<5 || loginNameLen>15 || passwordLen<6||passwordLen>20 ||validCodeLen!=4
                <%="1".equals(JWebConstant.SYS_CONFIG.get("SEND_MOBILE_MSG_FOR_LOGIN"))?"|| shortMessageCodeLen !=6":"" %>)return false;
            $('#loginSubmitButton').linkbutton('disable');
            return true;
        },
        success:function(data){
        	$('#loginSubmitButton').linkbutton('enable');
            if(data=='<%=JWebConstant.OK %>'){
            	$(document).mask('页面载入中...');
            	location.href='<%=path %>/jsp/jweb/main.jsp';
            }
            else {
                document.getElementById('loginValidCode').src='<%=path %>/jsp/jweb/image_code.jsp?'+Math.random();
                topCenterMessage('<%=JWebConstant.ERROR %>',data);
            }
        }
    });
    <%if("1".equals(JWebConstant.SYS_CONFIG.get("SEND_MOBILE_MSG_FOR_LOGIN"))){ %>
    $('#loginName').textbox('textbox').bind('blur',function(){
            loginMobileMsg('0',$('#loginName').val(),'');
        }
    );
    <%} %>
    document.onkeydown = function(evt){
		var evt = window.event?window.event:evt;
		if (evt.keyCode==13) {
			document.getElementById('loginSubmitButton').focus();
            loginFormSubmit();
		}
	}
});
</script>
</head>
<body class="easyui-layout" style="background:url(<%=path %>/images/login_bg.gif) repeat-x">
    <div class="easyui-panel" style="border:0px;position: absolute;left:50%;top:50%;margin-left:-310px;margin-top:-120px;width:619px;height:250px;background:url(<%=path %>/images/login_box_bg.jpg);">
        <div style="text-align:right;">
        <form id="loginForm" method="post">
            <table cellpadding="5"  style="margin-left:320px;margin-top:<%=
                    "1".equals(JWebConstant.SYS_CONFIG.get("SEND_MOBILE_MSG_FOR_LOGIN"))?"30":"50" %>px">
                <tr>
                    <td>登录名：</td>
                    <td colspan="2" align="left">
                        <input class="easyui-textbox" type="text" id="loginName" style="width:150px;" value="admin"
                             name="loginName" data-options="prompt:'',iconWidth:38"></input></td>
                </tr>
                <tr>
                    <td>密码：</td>
                    <td colspan="2" align="left"> 
                    <input id="password" name="password" class="easyui-textbox" type="password" value="123456qwer"
                         value="" style="width:150px;" data-options="prompt:'',iconWidth:38"></input>
					</td>
                </tr>
                <tr>
                    <td>图片验证码：</td>
                    <td align="left"><input id="validCode" name="validCode" style="width:70px;" value="1111"
                        class="easyui-textbox" value=""></input>
                        </td>
                    <td align="center"><img id="loginValidCode" alt="载入中..."  width="70" height="30" src="<%=path 
                        %>/jsp/jweb/image_code.jsp" onclick="this.src='<%=path %>/jsp/jweb/image_code.jsp?'+Math.random()"/></td>
                </tr>
                <%if("1".equals(JWebConstant.SYS_CONFIG.get("SEND_MOBILE_MSG_FOR_LOGIN"))){ %>
                <tr>
                    <td>手机验证码：</td>
                    <td align="left"><input id="shortMessageCode" name="shortMessageCode" style="width:70px;"
                        class="easyui-textbox" value=""></input>
                        </td>
                    <td align="left"><a href="javascript:loginMobileMsg('1',$('#loginName').val(),$('#validCode').val())"
                        class="easyui-linkbutton" id="shortMessageCodeButton" style="width:65px;"><span id="shortMessageCodeLable">获取</span></a></td>
                </tr>
                <%} %>
                <tr>
                    <td></td>
                    <td align="left" colspan="2"><a href="javascript:loginFormSubmit()" class="easyui-linkbutton"
                         style="width:150px;" iconCls="icon-ok" id="loginSubmitButton">登录</a>
                        </td>
                    <td></td>
                </tr>
                <tr>
                    <td>&nbsp;</td>
                    <td align="left" colspan="2"><span id="info" style="color:#F00"></span></td>
                </tr>
            </table>
        </form>
        </div>
    </div>
<script type="text/javascript">
    function loginFormSubmit(){
        $('#loginForm').submit();
    }
    var sendMsgWaitHandle;
    var sendMsgWaitCount = 60;
    function loginMobileMsg(flag,loginName,validCode){
      if(loginName.length<5 || loginName.length>15){
      	topCenterMessage('<%=JWebConstant.ERROR %>','请正确输入登录名！');
      	return;
      }
      if(flag=='1'){
      	var validCodeLen = $('#validCode').val().length;
      	if(validCodeLen != 4){
	       	topCenterMessage('<%=JWebConstant.ERROR %>','请正确输入图片验证码！');
	       	return;
      	}
      }
      $.post('<%=path %>/sysLoginMobileMsg.htm?flag='+flag+'&loginName='+loginName+'&validCode='+validCode,
             function(data){
                 sendMsgWaitCount = 60;
                 if(data=="0"){
                     if(flag=="1"){
                        $('#shortMessageCodeButton').linkbutton('disable');
                        sendMsgWaitHandle = setInterval(sendMsgWait, 1000);
                        document.getElementById('shortMessageCodeLable').innerHTML='获取('+sendMsgWaitCount+')';
                    }
                 } else {
                     topCenterMessage('<%=JWebConstant.ERROR %>',data);
                 }
               }
        );
    }
    function sendMsgWait () {
        if (sendMsgWaitCount <= 1) {
            sendMsgWaitCount=60;
            $('#shortMessageCodeButton').linkbutton('enable');
            clearInterval(sendMsgWaitHandle);
            document.getElementById('shortMessageCodeLable').innerHTML='获取';
        } else {
            sendMsgWaitCount--;
            document.getElementById('shortMessageCodeLable').innerHTML='获取('+sendMsgWaitCount+')';
        }
    }
    document.getElementById('loginName').focus();
</script>
</body>
</html>