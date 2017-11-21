package com.pay.merchantinterface.service;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import com.PayConstant;
import sun.misc.BASE64Decoder;
import util.DataTransUtil;
import util.Tools;

public class FYVerificationIDService {
	public static Map<String,String> VerificationID(String userName,String userId)throws Exception{
		try {
			String VERSION="1.0";
			String TYPEID = "YY";
			String MCHNTCD="0002900F0096235";
			String MCHNTORDERID=Tools.getUniqueIdentify();
			String NAME=userName;
			String IDNO=userId;
			String KEY=PayConstant.PAY_CONFIG.get("fy_sfzyz_key");
			String SIGN = FYMD5.MD5Encode(VERSION+"|"+TYPEID+"|"+MCHNTCD+"|"+MCHNTORDERID+"|"+NAME+"|"+IDNO+"|"+KEY);
			StringBuffer bufXml = new StringBuffer();
			bufXml.append("<ORDER>");
			bufXml.append("<VERSION>"+VERSION+"</VERSION>");
			bufXml.append("<TYPEID>"+TYPEID+"</TYPEID>");
			bufXml.append("<MCHNTCD>"+MCHNTCD+"</MCHNTCD>");
			bufXml.append("<MCHNTORDERID>"+MCHNTORDERID+"</MCHNTORDERID> ");
			bufXml.append("<NAME>"+NAME+"</NAME> ");
			bufXml.append("<IDNO>"+IDNO+"</IDNO>");
			bufXml.append("<REM1></REM1>");
			bufXml.append("<REM2></REM2>");
			bufXml.append("<REM3></REM3>");
			bufXml.append("<SIGN>"+SIGN+"</SIGN>");
			bufXml.append("</ORDER>");
			String params = "FM="+bufXml.toString();
			String result =  new String(new DataTransUtil().
					doPost(PayConstant.PAY_CONFIG.get("fy_sfzyz_url"), params.getBytes("utf-8")),"utf-8");
		    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();   
            DocumentBuilder builder = factory.newDocumentBuilder();   
            Document document = builder.parse(new InputSource(new StringReader(result)));   
            Element root = document.getDocumentElement();   
            NodeList list = root.getChildNodes();
            Map<String,String> resultMap = new HashMap<String,String>();
            for(int i=0;i<list.getLength();i++){
            	 Node node = list.item(i);
            	 if(node.getNodeName().equals("VERSION")){
            		 resultMap.put("version",node.getTextContent());
            	 }else if(node.getNodeName().equals("RESPONSECODE")){
            		 resultMap.put("responsecode",node.getTextContent());
            	 }else if(node.getNodeName().equals("RESPONSEMSG")){
            		 resultMap.put("responsemsg",node.getTextContent());
            	 }else if(node.getNodeName().equals("MCHNTORDERID")){
            		 resultMap.put("mchntorderid",node.getTextContent());
            	 }else if(node.getNodeName().equals("SIGN")){
            		 resultMap.put("sign",node.getTextContent());
            	 }else if(node.getNodeName().equals("USERID")){
            		 resultMap.put("userid",node.getTextContent());
            	 }
            }
            if(resultMap!=null){
            	String md5_2 = FYMD5.MD5Encode(resultMap.get("version")+"|"+resultMap.get("responsecode")+"|"+resultMap.get("responsemsg")+"|"+
            				resultMap.get("mchntorderid")+"|"+resultMap.get("userid")+"|"+KEY);
            	if(md5_2.equals(resultMap.get("sign"))){
            		if(resultMap.get("responsecode").equals("0000")){
            			//保存相片。
            			GenerateImage(resultMap.get("userid"),PayConstant.PAY_CONFIG.get("fy_sfzyz_imgpath")+NAME+IDNO+".jpg");
            		}
            		//返回结果集合。
            		return resultMap;
            	}else{
            		throw new Exception("富友身份证验证返回信息,验签失败!");
            	}
            }
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	//base64字符串转化成图片  
    public static boolean GenerateImage(String imgStr,String imgPath){   //对字节数组字符串进行Base64解码并生成图片  
        if (imgStr == null) //图像数据为空  
            return false;  
        BASE64Decoder decoder = new BASE64Decoder();  
        try{  
            //Base64解码  
            byte[] b = decoder.decodeBuffer(imgStr);  
            for(int i=0;i<b.length;++i){  
                if(b[i]<0){//调整异常数据  
                    b[i]+=25600;  
                }  
            }  
            OutputStream out = new FileOutputStream(imgPath);      
            out.write(b);  
            out.flush();  
            out.close();  
            return true;  
        }   
        catch (Exception e){  
            return false;  
        }  
    }  
	public static void main(String[] args) {
		try {
			VerificationID("张强","13072319881121081X");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
