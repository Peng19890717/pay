<%@ page import="java.util.Map" %>
<%@ page import="java.util.Iterator" %>
<%@page import="com.PayConstant"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    Map<String, String> data = (Map<String, String>) request.getAttribute("reqMap");
%>
<html>
<body>
<form name="form" id="form" action="<%=PayConstant.PAY_CONFIG.get("YHJ_ONLINEPAY_URL")%>" method="post">

    <% String tmp = "";
        for(Iterator<String> it=data.keySet().iterator(); it.hasNext(); ){
            tmp = it.next();
    %>
    <input type="hidden" name="<%=tmp%>" value='<%=data.get(tmp)%>'/>
    <%}%>
</form>

<script>
   document.getElementById("form").submit();
</script>
</body>
</html>
