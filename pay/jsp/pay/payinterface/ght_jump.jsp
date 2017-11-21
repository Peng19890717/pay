<%@ page import="java.util.Map" %>
<%@ page import="java.util.Iterator" %>
<%@page import="com.PayConstant"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    Map<String, String> ghtReqData = (Map<String, String>) request.getAttribute("ghtReqData");
%>
<html>
<body>
<form name="form" id="ghtForm" action="<%= PayConstant.PAY_CONFIG.get("GHT_ONLINEPAY_URL") %>" method="post">

    <% String tmp = "";
        for(Iterator<String> it = ghtReqData.keySet().iterator(); it.hasNext(); ){
            tmp = it.next();
    %>
    <input type="hidden" name="<%= tmp %>" value='<%= ghtReqData.get(tmp) %>'/>
    <%}%>
</form>
<script>
   document.getElementById("ghtForm").submit();
</script>
</body>
</html>
