package util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.PayConstant;

public class PayUtil {
	private static final Log log = LogFactory.getLog(PayUtil.class);
    public static List<String> MOBILE_USER_AGENT = new ArrayList<String>();
    //browserList里面的值表示的是移动端（非PC端）  
    static {  
    	MOBILE_USER_AGENT.add("samsung");  
    	MOBILE_USER_AGENT.add("cldc1.1");  
    	MOBILE_USER_AGENT.add("maui");  
    	MOBILE_USER_AGENT.add("untrusted/1.0");  
    	MOBILE_USER_AGENT.add("android");  
    	MOBILE_USER_AGENT.add("ucweb");  
    	MOBILE_USER_AGENT.add("brew");  
    	MOBILE_USER_AGENT.add("j2me");  
    	MOBILE_USER_AGENT.add("yulong");  
    	MOBILE_USER_AGENT.add("coolpad");  
    	MOBILE_USER_AGENT.add("tianyu");  
    	MOBILE_USER_AGENT.add("k-touch");  
    	MOBILE_USER_AGENT.add("haier");  
    	MOBILE_USER_AGENT.add("dopod");  
    	MOBILE_USER_AGENT.add("lenovo");  
    	MOBILE_USER_AGENT.add("aigo-");  
    	MOBILE_USER_AGENT.add("ctc/1.0");  
    	MOBILE_USER_AGENT.add("ctc/2.0");  
    	MOBILE_USER_AGENT.add("cmcc");  
    	MOBILE_USER_AGENT.add("daxian");  
    	MOBILE_USER_AGENT.add("mot-");  
    	MOBILE_USER_AGENT.add("gionee");  
        MOBILE_USER_AGENT.add("zte");  
        MOBILE_USER_AGENT.add("huawei");  
        MOBILE_USER_AGENT.add("webos");  
        MOBILE_USER_AGENT.add("gobrowser");  
        MOBILE_USER_AGENT.add("wap2.0");  
        MOBILE_USER_AGENT.add("ucbrowser");  
        
        MOBILE_USER_AGENT.add("2.0 mmp");
        MOBILE_USER_AGENT.add("240320");
        MOBILE_USER_AGENT.add("avantgo");
        MOBILE_USER_AGENT.add("blackberry");
        MOBILE_USER_AGENT.add("blazer");
        MOBILE_USER_AGENT.add("cellphone");
        MOBILE_USER_AGENT.add("danger");
        MOBILE_USER_AGENT.add("docomo");
        MOBILE_USER_AGENT.add("elaine/3.0");
        MOBILE_USER_AGENT.add("eudoraweb");
        MOBILE_USER_AGENT.add("hiptop");
        MOBILE_USER_AGENT.add("iemobile");
        MOBILE_USER_AGENT.add("kyocera/wx310k");
        MOBILE_USER_AGENT.add("lg/u990");
        MOBILE_USER_AGENT.add("midp-2");
        MOBILE_USER_AGENT.add("mmef20");
        MOBILE_USER_AGENT.add("netfront");
        MOBILE_USER_AGENT.add("newt");
        MOBILE_USER_AGENT.add("nintendo wii");
        MOBILE_USER_AGENT.add("nitro");
        MOBILE_USER_AGENT.add("nokia");
        MOBILE_USER_AGENT.add("opera mini");
        MOBILE_USER_AGENT.add("opera mobi");
        MOBILE_USER_AGENT.add("palm");
        MOBILE_USER_AGENT.add("playstation portable");
        MOBILE_USER_AGENT.add("portalmmm");
        MOBILE_USER_AGENT.add("proxinet");
        MOBILE_USER_AGENT.add("proxinet");
        MOBILE_USER_AGENT.add("sharp-tq-gx10");
        MOBILE_USER_AGENT.add("small");
        MOBILE_USER_AGENT.add("sonyericsson");
        MOBILE_USER_AGENT.add("symbian os");
        MOBILE_USER_AGENT.add("symbianos");
        MOBILE_USER_AGENT.add("ts21i-10");
        MOBILE_USER_AGENT.add("up.browser");
        MOBILE_USER_AGENT.add("up.link");
        MOBILE_USER_AGENT.add("windows ce");
        MOBILE_USER_AGENT.add("winwap");
        MOBILE_USER_AGENT.add("androi");
        MOBILE_USER_AGENT.add("iphone");
        MOBILE_USER_AGENT.add("ipod");
        MOBILE_USER_AGENT.add("ipad");
        MOBILE_USER_AGENT.add("windows phone");
        MOBILE_USER_AGENT.add("htc");
    }
    public static boolean isMobile(String userAgent){
    	if(userAgent==null||userAgent.length()==0)return false;
    	for(int i=0; i<MOBILE_USER_AGENT.size(); i++){
    		if(userAgent.toLowerCase().indexOf(MOBILE_USER_AGENT.get(i))>=0)return true;
    	}
    	return false;
    }
	public static String [] WEEK_NAME={"日","一","二","三","四","五","六"};
	public static String hideInfoWithStar(String info){
		if(info==null||info.length()==0)return"";
		if(info.length()==1)return info;
		if(info.length()==2)return info.substring(0,1)+"*";
		String tmp = info.substring(0,1);
		for(int i=0; i<info.length()-2; i++)tmp=tmp+"*";
		return tmp+info.substring(info.length()-1);
	}
	public static String createWebUrl(Map<String,String> map){
		if(map==null)return "";
		String p = "";
		Iterator it = map.keySet().iterator();
        while(it.hasNext()){
        	String key = (String) it.next();
        	p = p+key+"="+java.net.URLEncoder.encode(map.get(key))+"&";
        }
        return p.endsWith("&")?p.substring(0,p.length()-1):p;
	}
	/**
	 * 字符串留首位，中间用*代替
	 * @param src
	 * @return
	 */
	public static String getStarInfo(String src){
    	if(src==null||src.length()==0)return "";
    	if(src.length()==1)return src+"*";
    	if(src.length()==2)return src.substring(0,1)+"*"+src.substring(1);
    	String tmp = src.substring(0,1);
    	for(int i=0; i<src.length()-2;i++)tmp = tmp+"*";
    	return tmp+src.substring(src.length()-1);
    }
	/**
	 * XML 跟元素属性转换成MAP
	 * @param xmlStr
	 * @return
	 */
	public static Map <String,String>parseXmlAttributeToMap(String xmlStr){
		ByteArrayInputStream inStream = null;
		Map<String,String> map = new HashMap<String,String>();
		try {
			inStream = new ByteArrayInputStream(xmlStr.getBytes("utf-8"));
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.newDocument();
			document = builder.parse(inStream);
			document.getDocumentElement().normalize();
			NamedNodeMap nnm = document.getFirstChild().getAttributes();
			for(int j = 0; j<nnm.getLength(); j++){
				Node nodeitm= nnm.item(j);
				String value = nodeitm.getNodeValue();
				map.put(nodeitm.getNodeName(), value);
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		} finally {
			if(inStream != null) try {inStream.close();} catch (Exception e2) {}
		}
		return map;
	}
	/**
	 * XML 某个节点的属性和子节点
	 * @param xmlStr
	 * @param path 
	 * @return 第一个元素属性 第二个元素子节点text
	 */
	public static Map [] parseXmlChildrenToMap(String xmlStr,String [] path){
		ByteArrayInputStream inStream = null;
		Map<String,String> attributes = new HashMap<String,String>();
		Map<String,String> children = new HashMap<String,String>();
		try {
			inStream = new ByteArrayInputStream(xmlStr.getBytes("utf-8"));
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.newDocument();
			document = builder.parse(inStream);
			document.getDocumentElement().normalize();
			List<Node> nodePathList = new ArrayList<Node>();
			parseXmlToPath(document.getFirstChild(),path,0,nodePathList);
			if(nodePathList.size()>0){
				Node node = nodePathList.get(nodePathList.size()-1);
				NamedNodeMap nnm = node.getAttributes();
				for(int j = 0; j<nnm.getLength(); j++){
					Node nodeitm= nnm.item(j);
					attributes.put(nodeitm.getNodeName(), nodeitm.getNodeValue().trim());
				}
				NodeList nodeList = node.getChildNodes();
				for(int i=0; i<nodeList.getLength(); i++){
					Node tmp = nodeList.item(i);
					children.put(tmp.getNodeName(),tmp.getTextContent().trim());
				}
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		} finally {
			if(inStream != null) try {inStream.close();} catch (Exception e2) {}
		}
		return new Map [] {attributes,children};
	}
	/**
	 * XML 取得某个指定节点
	 * @param xmlStr
	 * @param path 
	 * @return 指定节点
	 */
	public static Node getXmlNode(String xmlStr,String [] path){
		ByteArrayInputStream inStream = null;
		Map<String,String> attributes = new HashMap<String,String>();
		Map<String,String> children = new HashMap<String,String>();
		try {
			inStream = new ByteArrayInputStream(xmlStr.getBytes("utf-8"));
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.newDocument();
			document = builder.parse(inStream);
			document.getDocumentElement().normalize();
			List<Node> nodePathList = new ArrayList<Node>();
			parseXmlToPath(document.getFirstChild(),path,0,nodePathList);
			if(nodePathList.size()>0)return nodePathList.get(nodePathList.size()-1);
		} catch (Exception e1) {
			e1.printStackTrace();
		} finally {
			if(inStream != null) try {inStream.close();} catch (Exception e2) {}
		}
		return null;
	}
	private static void parseXmlToPath(Node node,String [] path,int n,List<Node> nodePathList){
		if(nodePathList.size()>=path.length)return;
		if(node.getNodeName().equals(path[n++])) {
			nodePathList.add(node);
			NodeList nodeList = node.getChildNodes();
			for(int i=0; i<nodeList.getLength(); i++){
				Node tmp = nodeList.item(i);
				parseXmlToPath(tmp,path,n,nodePathList);
			}
		}
	}
	public static String encodeString(String strData){
	     if (strData == null || strData.length()==0)return "";  
	     strData = Tools.replaceStr(strData, "&", "&amp;");
	     strData = Tools.replaceStr(strData, "<", "&lt;");
	     strData = Tools.replaceStr(strData, ">", "&gt;");
	     strData = Tools.replaceStr(strData, "'", "&apos;");
	     strData = Tools.replaceStr(strData, "\"", "&quot;");
	     return strData;  
	}
	public static String exceptionToString(Exception e){
		StringBuffer sb = new StringBuffer();
		try {
			StackTraceElement [] obj = e.getStackTrace();
			for(int k = 0; k<obj.length; k++)sb.append(obj[k]).append("\r\n");
		} catch (Exception e2) {}
		return sb.toString();
	}
	/**
	 * 结算周期为周时，结算时间点显示
	 * @param oldStr
	 * @return
	 */
	public static String parseShow(String oldStr){
		String newStr = "";
		if(oldStr != null && !"".equals(oldStr)){
			String[] nStr = oldStr.split("|");
			for (String str : nStr) {
				if(str.contains("1")){
					newStr += "星期一 ";
				}
				if(str.contains("2")){
					newStr += "星期二 ";
				}
				if(str.contains("3")){
					newStr += "星期三 ";
				}
				if(str.contains("4")){
					newStr += "星期四 ";
				}
				if(str.contains("5")){
					newStr += "星期五 ";
				}
				if(str.contains("6")){
					newStr += "星期六 ";
				}
				if(str.contains("7")){
					newStr += "星期日 ";
				}
			}
			return newStr;
		}
		return null;
	}
	/**
	 * 通知集群节点常驻内存变量
	 * @param type
	 */
	public static void synNotifyForAllNode(String type){
		try {
			if(!"0".equals(PayConstant.PAY_CONFIG.get("server_type")))throw new Exception("属性文件变量server_type错误，非后台服务器");
			//读取配置文件
			File tmpFile = new File(JWebConstant.APP_PATH+"/config/synchronise_constant_server.txt");
			if(!tmpFile.exists())return;
			BufferedReader br = new BufferedReader(new FileReader(tmpFile));
			List <String> serverList = new ArrayList<String>();
			String line = null;
			while((line=br.readLine())!=null){
				if(line!=null && (line.trim().startsWith("http")||line.trim().startsWith("https")))serverList.add(line);
			}
			//循环通知
			for(int i=0; i<serverList.size(); i++)new SynCasheNotify(serverList.get(i),type).start();
		} catch (Exception e) {
			log.info(exceptionToString(e));
		}
	}
    /**
     * 数字金额大写转换，思想先写个完整的然后将如零拾替换成零
     * 要用到正则表达式
     */
    public static String digitUppercase(double n){
        String fraction[] = {"角", "分"};
        String digit[] = { "零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖" };
        String unit[][] = {{"元", "万", "亿"},
                     {"", "拾", "佰", "仟"}};
 
        String head = n < 0? "负": "";
        n = Math.abs(n);
         
        String s = "";
        for (int i = 0; i < fraction.length; i++) {
        	double f1= new BigDecimal(n).setScale(2,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(10 * Math.pow(10, i))).doubleValue(); 
        	s += (digit[(int) (Math.floor(f1) % 10)] + fraction[i]).replaceAll("(零.)+", "");
        }
        if(s.length()<1){
            s = "整";    
        }
        int integerPart = (int)Math.floor(n);
 
        for (int i = 0; i < unit[0].length && integerPart > 0; i++) {
            String p ="";
            for (int j = 0; j < unit[1].length && n > 0; j++) {
                p = digit[integerPart%10]+unit[1][j] + p;
                integerPart = integerPart/10;
            }
            s = p.replaceAll("(零.)*零$", "").replaceAll("^$", "零") + unit[0][i] + s;
        }
        return head + s.replaceAll("(零.)*零元", "元").replaceFirst("(零.)+", "").replaceAll("(零.)+", "零").replaceAll("^整$", "零元整");
    }
	public static void main(String [] args){
        System.out.println(digitUppercase(123.34));        // 壹佰贰拾叁元叁角肆分
        System.out.println(digitUppercase(1000000.56));    // 壹佰万元伍角陆分 
	}
}
class SynCasheNotify extends Thread{
	private static final Log log = LogFactory.getLog(SynCasheNotify.class);
	String url="";
	String type="";
	public SynCasheNotify(String url,String type){
		this.url=url;
		this.type=type;
	}
	public void run(){
		try {
			String timestamp = String.valueOf(System.currentTimeMillis());
			String sign = util.SHA1.SHA1String(timestamp+PayConstant.PAY_CONFIG.get("SYS_COMM_PWD_WITH_INNER_PLT"));
			log.info("内存同步通知："+url);
			String msg = new String(new DataTransUtil().doPost(url,("type="+type+"&timestamp="+timestamp+"&sign="+sign).getBytes()));
			log.info("内存同步响应=============="+msg.trim());
		} catch (Exception e) {
			log.info(PayUtil.exceptionToString(e));
		}
	}
}
