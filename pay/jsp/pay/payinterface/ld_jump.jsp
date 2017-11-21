<%@ page import="java.util.Map" %>
<%@ page import="java.util.Iterator" %>
<%@page import="com.PayConstant"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    Map<String, String> ldReqData = (Map<String, String>) request.getAttribute("ldReqData");
%>
<html>
<body>
<form name="form" id="ldForm" action="<%= PayConstant.PAY_CONFIG.get("LD_URL") %>" method="post">

    <% String tmp = "";
        for(Iterator<String> it = ldReqData.keySet().iterator(); it.hasNext(); ){
            tmp = it.next();
    %>
    <input type="hidden" name="<%= tmp %>" value='<%= ldReqData.get(tmp) %>'/>
    <%}%>
</form>
<script>
   document.getElementById("ldForm").submit();
</script>
</body>
</html>
