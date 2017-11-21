<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ page import="com.pay.merchantinterface.service.PayRequest"%>
<%
PayRequest payRequest = (PayRequest)request.getAttribute("payRequest");
%>
<%if(payRequest != null){ %>
操作失败：<%=payRequest.respDesc %>（错误码：<%=payRequest.respCode %>）
<%} else {%>
未知错误
<%} %>