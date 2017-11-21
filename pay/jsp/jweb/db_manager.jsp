<%@ page language="java" pageEncoding="utf-8"%>
<%@ page import="java.util.*"%>
<%@ page import="java.sql.Connection"%>
<%@ page import="java.sql.DriverManager"%>
<%@ page import="java.sql.PreparedStatement"%>
<%@ page import="java.sql.ResultSet"%>
<%@ page import="java.sql.ResultSetMetaData"%>
<%@ page import="java.sql.Types"%>
<%
	session.setAttribute("error",null);
	String driver = request.getParameter("driver");
	String url = request.getParameter("url");
	String userName = request.getParameter("userName");
	String password = request.getParameter("password");
	String action = request.getParameter("action");
	String sql = request.getParameter("sql");
	sql = sql == null ? "" : sql;
	sql = new String(sql.getBytes("ISO8859-1"),"utf-8");
	session.setAttribute("driver",driver);
	session.setAttribute("url",url);
	session.setAttribute("userName",userName);
	session.setAttribute("password",password);
	session.setAttribute("sql",sql);
	Connection con = null;
	PreparedStatement ps = null;
	ResultSet rs = null;
	ResultSetMetaData rsmd = null;
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<script language="JavaScript">
var drivers=new Array();
var dburls=new Array();
drivers[0]="oracle.jdbc.driver.OracleDriver";
drivers[1]="net.sourceforge.jtds.jdbc.Driver";
drivers[2]="com.mysql.jdbc.Driver";
dburls[0]="jdbc:oracle:thin:@127.0.0.1:1521:oradb";
dburls[1]="jdbc:jtds:sqlserver://127.0.0.1:1433/test;tds=8.0;lastupdatecount=true";
dburls[2]="jdbc:mysql://127.0.0.1:3306/test?useUnicode=true&characterEncoding=utf-8";
function getDbUrl()
{
	var i = document.form1.db.value;
	document.form1.driver.value = drivers[i];
	document.form1.url.value = dburls[i];
}
function mySubmit(action)
{
	if(action==0){
		if(!confirm("是否执行语句："+document.form1.sql.value)){
			return;
		}
	}
	document.form1.action="db_manager.jsp?action="+action;
	document.form1.submit();
}
</script>
<title>DB Manager</title>
</head>

<body>
  <form id="form1" name="form1" method="post" action="db_manager.jsp">
    <table width="100%" border="1">
      <tr>
        <td width="4%" align="right" valign="top"><br />Connection:</td>
        <td width="96%">&nbsp;DB:
          <select name="db" id="db" onChange="getDbUrl();">
			<option value="0">Oracle</option>
			<option value="1">MS SQL Server</option>
			<option value="2">My SQL</option>
		</select>
        Driver:
        <input name="driver" size="70" type="text" id="driver" 
        	value="<%=session.getAttribute("driver") == null ? "oracle.jdbc.driver.OracleDriver" : session.getAttribute("driver") %>" />
        <br />
        URL:
        <input name="url" size="100" type="text" id="url"
        	value="<%=session.getAttribute("url") == null ? "jdbc:oracle:thin:@127.0.0.1:1521:orcl" : session.getAttribute("url") %>" /> 
        <br />
        UserName
        <input name="userName" type="text" id="userName" 
        	value="<%=session.getAttribute("userName") == null ? "pay_db" : session.getAttribute("userName") %>" />
        Password
        <input name="password" type="password" id="password" 
        	value="<%=session.getAttribute("password") == null ? "" : session.getAttribute("password") %>" />	
		</td>
      </tr>
       <tr>
        <td align="right" valign="top">&nbsp;</td>
        <td><input type="button" name="button" value="执行sql" onclick="mySubmit('0')"/>&nbsp;&nbsp;
        <input type="button" name="button1" value="查询"  onclick="mySubmit('1')"/>&nbsp;&nbsp;
      </tr>
      <tr>
        <td align="right" valign="top">SQL:</td>
        <td><textarea name="sql" cols="80" rows="10" id="sql"><%=session.getAttribute("sql") == null ? "" : session.getAttribute("sql") %></textarea></td>
      </tr>
      <tr>
        <td align="right" valign="top">Result:</td>
        <td>        
        <%
        try {
	        Class.forName(driver);
	        con = DriverManager.getConnection(url,userName,password);
	        ps = con.prepareStatement(sql);
	        //查询
		    if("1".equals(action)){
		    	%><table width="100%" border="1"><%
		    	rs = ps.executeQuery();
		    	rsmd = rs.getMetaData();
		    	int columnCount = rsmd.getColumnCount();
				int [] columnsType = new int[columnCount];
				System.out.println(columnCount);
				%><tr><%
				for (int i = 0; i < columnCount; i++) {
					columnsType[i] = rsmd.getColumnType(i + 1);
					%><td><%=rsmd.getColumnName(i + 1) %></td><%
				}				
				%></tr><%
				while (rs.next()) {
					%><tr><%
					for (int i = 0; i < columnCount; i++) {	
						try{
							//integer
							if(columnsType[i] == Types.INTEGER || columnsType[i] == Types.TINYINT 
									|| columnsType[i] == Types.SMALLINT || columnsType[i] == Types.DECIMAL){
								%><td><%=rs.getInt(rsmd.getColumnName(i + 1)) %>&nbsp;</td><%
							}
							//long
							else if(columnsType[i] == Types.BIGINT || columnsType[i] == Types.NUMERIC){
								%><td><%=rs.getLong(rsmd.getColumnName(i + 1)) %>&nbsp;</td><%
							}
							//float
							else if(columnsType[i] == Types.FLOAT){
								%><td><%=rs.getFloat(rsmd.getColumnName(i + 1)) %>&nbsp;</td><%
							}
							//double
							else if(columnsType[i] == Types.DOUBLE ){
								%><td><%=rs.getDouble(rsmd.getColumnName(i + 1)) %>&nbsp;</td><%
							}
							//datetime
							else if(columnsType[i] == Types.DATE || columnsType[i] == Types.TIME 
									|| columnsType[i] == Types.TIMESTAMP ){
								Date date = rs.getTimestamp(rsmd.getColumnName(i + 1));
								%><td><%=date == null ? "&nbsp;" : new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date) %>&nbsp;</td><%
							}
							//String
							else if(columnsType[i] == Types.CHAR || columnsType[i] == Types.VARCHAR
									|| columnsType[i] == Types.CLOB || columnsType[i] == Types.NCLOB){
								%><td><%=rs.getString(rsmd.getColumnName(i + 1)) %>&nbsp;</td><%
							}
							//other
							else {
								%><td>二进制数据</td><%
							}
						} catch (Exception e){
							%><td>&nbsp;</td><%
						}				
					}
					%></tr><%
				}
		    	%></table><%
		    }
		    //更新
		    else if("0".equals(action)) {
		    	%><%=ps.executeUpdate() %><%
			}
	    } catch (Exception e) {
	    	StackTraceElement [] ste = e.getStackTrace();	    
	        out.println(e.toString());
	    } finally {
	    	try {
				if(rs != null)rs.close();
			} catch (Exception e) {}
	    	try {
				if(ps != null)ps.close();
			} catch (Exception e) {}
			try {
				if(con != null)con.close();
			} catch (Exception e) {}
	    }        
        %>
        </td>
      </tr>
    </table>
  </form>
</body>
</html>
