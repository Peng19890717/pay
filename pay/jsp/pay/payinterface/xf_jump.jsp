<%@ page import="java.util.Map" %>
<%@ page import="java.util.Iterator" %>
<%@page import="com.PayConstant"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    Map<String, String> xfReqData = (Map<String, String>) request.getAttribute("xfReqData");
%>
<html>
<body>
<form name="form" id="xfForm" action="<%= PayConstant.PAY_CONFIG.get("XF_PAY_UTL") %>" method="post">

    <% String tmp = "";
        for(Iterator<String> it = xfReqData.keySet().iterator(); it.hasNext(); ){
            tmp = it.next();
    %>
    <input type="hidden" name="<%= tmp %>" value='<%= xfReqData.get(tmp) %>'/>
    <%}%>
</form>
<script>
   document.getElementById("xfForm").submit();
</script>
</body>
</html>
