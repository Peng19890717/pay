<%@ page import="java.util.Map" %>
<%@ page import="java.util.Iterator" %>
<%@page import="com.PayConstant"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    Map<String, String> yltfReqData = (Map<String, String>) request.getAttribute("yltfReqData");
%>
<html>
<body>
<form name="form" id="yltfForm" action="<%= PayConstant.PAY_CONFIG.get("YLTF_ONLINEPAY_URL") %>" method="post">

    <% String tmp = "";
        for(Iterator<String> it = yltfReqData.keySet().iterator(); it.hasNext(); ){
            tmp = it.next();
    %>
    <input type="hidden" name="<%= tmp %>" value='<%= yltfReqData.get(tmp) %>'/>
    <%}%>
</form>
<script>
   document.getElementById("yltfForm").submit();
</script>
</body>
</html>
