<%@ page import="java.util.Map" %>
<%@ page import="java.util.Iterator" %>
<%@page import="com.PayConstant"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    Map<String, String> paramsMap = (Map<String, String>) request.getAttribute("paramsMap");
    String paras = "";
    for(Iterator<String> it = paramsMap.keySet().iterator(); it.hasNext(); ){
           String key = it.next();
           paras = paras+key+"="+java.net.URLEncoder.encode(paramsMap.get(key),"utf-8")+"&";
   	}
   	System.out.println(paras);
   	String url = PayConstant.PAY_CONFIG.get("YCF_DATA_TRANSFER")+"?url="+java.net.URLEncoder.encode(PayConstant.PAY_CONFIG.get("GFB_GATEWAYURL")+"?"+paras,"utf-8");
   	System.out.println(url);
%>
<html>
<head>
	<meta http-equiv="refresh" content="www.58ycf.com" />
</head>
<body>
<form name="form" id="gfbForm" action="<%=PayConstant.PAY_CONFIG.get("YCF_DATA_TRANSFER") %>" method="get">
	<input type="hidden" name="url" value="<%=PayConstant.PAY_CONFIG.get("GFB_GATEWAYURL")+"?"+paras %>"/>
	<input type="submit" value="提交" />
</form>
<script>
   //document.getElementById("gfbForm").submit();
</script>
</body>
</html>
