<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	java.io.RandomAccessFile rf = new java.io.RandomAccessFile("xf-notify-withdraw.txt", "rw");
	rf.seek(rf.length());
	rf.write(("----------"+session.getId()).getBytes());
	rf.write((request.getProtocol().toLowerCase()+"//"+request.getLocalName()+":"+request.getLocalPort()+"\r\n").getBytes());
	rf.write(("--headers--start--\r\n").getBytes());
	Enumeration e = request.getHeaderNames();	
	while(e.hasMoreElements()){
		String key = (String)e.nextElement();
		rf.write((key+":"+request.getHeader(key)+"\r\n").getBytes());
	}
	rf.write(("--headers--end--\r\n").getBytes());
	rf.write(("--parameter--start--\r\n").getBytes());
	e = request.getParameterNames();
	while(e.hasMoreElements()){
		String key = (String)e.nextElement();
		rf.write((key+":"+request.getParameter(key)+"\r\n").getBytes());
	}
	rf.write(("--parameter--end--\r\n").getBytes());
	rf.write(("session attributes================\r\n").getBytes());  
	Enumeration enumeration=session.getAttributeNames();  
	while (enumeration.hasMoreElements()){  
	String objs = enumeration.nextElement().toString(); 
	rf.write((objs+"="+session.getAttribute(objs)+"\r\n").getBytes());  
	}
	rf.close();
%>

