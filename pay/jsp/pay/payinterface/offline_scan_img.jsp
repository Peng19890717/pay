<%@ page language="java"  contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="util.QRCodeUtil"%>
<%@ page import="com.jweb.dao.JWebUser"%>
<%@ page import="com.jweb.service.JWebUserService"%>
<%
JWebUser user = (JWebUser)session.getAttribute("user");
if(!JWebUserService.checkUser(user,request))return;
byte [] b = QRCodeUtil.encode(request.getParameter("url"));
javax.imageio.ImageIO.write(javax.imageio.ImageIO.read(new java.io.ByteArrayInputStream(b)), "JPEG", response.getOutputStream()); 
%>
