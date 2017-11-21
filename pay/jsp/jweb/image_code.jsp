<%@ page contentType="image/jpeg" import="util.ImageCode" %> 
<%
String validCode = ImageCode.getRandImageCode();
validCode = "1111"; 
session.setAttribute("validCode",validCode.toLowerCase());
javax.imageio.ImageIO.write(ImageCode.createImageCode(validCode), "JPEG", response.getOutputStream()); 
%>
