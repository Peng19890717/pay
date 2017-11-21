<%@ page import="java.util.Map" %>
<%@ page import="java.util.Iterator" %>
<%@page import="com.PayConstant"%>
<%@ page contentType="text/html;charset=GBK" language="java" %>
<%
    Map<String, String> paramsMap = (Map<String, String>) request.getAttribute("paramsMap");
%>
<html>
<body>
<form name="form" id="ybForm" action="<%= PayConstant.PAY_CONFIG.get("YB_GATEWAYURL") %>" method="post">
    <% String tmp = "";
        for(Iterator<String> it = paramsMap.keySet().iterator(); it.hasNext(); ){
            tmp = it.next();
    %>
    <input type="hidden" name="<%= tmp %>" value='<%= paramsMap.get(tmp) %>'/>
    <%}%>
</form>
<script>
   document.getElementById("ybForm").submit();
</script>
</body>
</html>
