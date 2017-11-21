<%@ page import="java.util.Map" %>
<%@ page import="java.util.Iterator" %>
<%@page import="com.PayConstant"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    Map<String, String> ttfReqData = (Map<String, String>) request.getAttribute("ttfReqData");
%>
<html>
<body>
<form name="form" id="ttfForm" action="<%= PayConstant.PAY_CONFIG.get("TTF_ONLINEPAY_URL") %>" method="post">

    <% String tmp = "";
        for(Iterator<String> it = ttfReqData.keySet().iterator(); it.hasNext(); ){
            tmp = it.next();
    %>
    <input type="hidden" name="<%= tmp %>" value='<%= ttfReqData.get(tmp) %>'/>
    <%}%>
</form>
<script>
   document.getElementById("ttfForm").submit();
</script>
</body>
</html>
