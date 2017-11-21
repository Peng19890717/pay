package com.pay.merchantinterface.service;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import jxl.CellType;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableCell;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import util.DataTransUtil;
import util.JWebConstant;
import util.Tools;

import com.PayConstant;
import com.pay.coopbank.dao.PayChannelRotation;
import com.pay.coopbank.dao.PayChannelRotationDAO;
import com.pay.order.dao.PayOrder;
import com.pay.order.dao.PayProductOrder;
import com.third.zhongxin.util.MD5;
import com.third.zhongxin.util.SignUtils;
import com.third.zhongxin.util.XmlUtils;
/**
 * 北京中信服务类
 * @author Administrator
 *
 */
public class ZX_BJ_PayService {
	
	private static final Log log = LogFactory.getLog(ZX_BJ_PayService.class);
	/**
	 * 
	 * @param request
	 * @param payRequest
	 * @param prdOrder
	 * @param payOrder
	 * @param productType 业务标示：选择扫码机构的编码 支付宝pay.alipay.native ，微信pay.weixin.native
	 * @return
	 */
	public String scanPay(HttpServletRequest request,PayRequest payRequest,PayProductOrder prdOrder,PayOrder payOrder,
			String productType,PayChannelRotation channelRotation){
		try {
			 Map<String,String> map = new HashMap<String, String>();
	    	 map.put("service",productType);
	    	 map.put("mch_id", channelRotation!=null?channelRotation.merchantId:PayConstant.PAY_CONFIG.get("PAY_ZXBJ_WX_MERCHAT_NO"));//商户好。
	    	 map.put("version", "2.0");//版本号。
	    	 map.put("charset", "UTF-8");//字符集。
	    	 map.put("sign_type", "MD5");//签名类型，只支持MD5
	    	 map.put("out_trade_no", payOrder.payordno);//订单号。
	    	 map.put("body",PayConstant.PAY_CONFIG.get("COMMON_PRODUCT_DESC")==null||PayConstant.PAY_CONFIG.get("COMMON_PRODUCT_DESC").length()==0?
						"_":PayConstant.PAY_CONFIG.get("COMMON_PRODUCT_DESC"));//商品描述
	    	 map.put("total_fee", String.valueOf(payOrder.txamt));//总金额。单位分。
	    	 map.put("mch_create_ip", PayConstant.PAY_CONFIG.get("PAY_ZXBJ_WX_IP"));//终端IP
	    	 map.put("notify_url", PayConstant.PAY_CONFIG.get("PAY_ZXBJ_WX_NOTIFY_URL"));//通知地址。
	    	 map.put("nonce_str", String.valueOf(new Date().getTime()));//随机字符串。
	    	 Map<String,String> params = SignUtils.paraFilter(map);//过滤空值。
	         StringBuilder buf = new StringBuilder((params.size() +1) * 10);
	         SignUtils.buildPayParams(buf,params,false);
	         String preStr = buf.toString();
	         String sign = MD5.sign(preStr, "&key="+(channelRotation!=null?channelRotation.md5Key:PayConstant.PAY_CONFIG.get("PAY_ZXBJ_WX_KEY")), "utf-8");
	         map.put("sign", sign);
	         log.info("中信BJ微信支付下单请求报文:"+XmlUtils.parseXML(map));
			 String rltXml=new String(new DataTransUtil().doPost(PayConstant.PAY_CONFIG.get("PAY_ZXBJ_WX_URL"), XmlUtils.parseXML(map).getBytes("utf-8")),"utf-8");
			 log.info("中信BJ微信支付下单返回报文:"+rltXml);
			 DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();   
		     DocumentBuilder builder = factory.newDocumentBuilder();   
		     Document document = builder.parse(new InputSource(new StringReader(rltXml)));   
		     Element root = document.getDocumentElement();   
		     NodeList list = root.getChildNodes();
		     Map<String,String> resultMap=new HashMap<String, String>();
		     for(int i=0;i<list.getLength();i++){
		           Node node = list.item(i);
		           if(!"#text".equals(node.getNodeName())){
		        	   resultMap.put(node.getNodeName(), node.getTextContent());
		           }
		     }
		     if("0".equals(resultMap.get("status"))){
		    	 //验签名。
				 if(SignUtils.checkParam(resultMap,channelRotation!=null?channelRotation.md5Key:PayConstant.PAY_CONFIG.get("PAY_ZXBJ_WX_KEY"))){
					 if("0".equals(resultMap.get("status")) && "0".equals(resultMap.get("result_code"))){//下单成功处理。
						 return "<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"no\"?>" +
									"<message merchantId=\""+payRequest.merchantId+"\" merchantOrderId=\""+payRequest.merchantOrderId+"\" " +
									"codeUrl=\""+resultMap.get("code_url")+"\" respCode=\"000\" respDesc=\"处理成功\"/>";
			           }else if(!"0".equals(resultMap.get("result_code"))){//业务处理错误。
				        	 return "<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"no\"?>" +
						       		   "<message merchantId=\""+payRequest.merchantId+"\" merchantOrderId=\""+payRequest.merchantOrderId+"\" " +
						       			"codeUrl=\"\" respCode=\"-1\" respDesc=\""+resultMap.get("err_msg").toString()+"\"/>";  
				      }else{
				    	  return "<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"no\"?>" +
					       		   "<message merchantId=\""+payRequest.merchantId+"\" merchantOrderId=\""+payRequest.merchantOrderId+"\" " +
					       			"codeUrl=\"\" respCode=\"-1\" respDesc=\"(ZX)渠道错误\"/>";
				      }
				 }else throw new Exception("验签失败");
		     }else throw new Exception("(ZX)渠道错误！"+resultMap.get("message"));
		} catch(Exception e){
			e.printStackTrace();
			return "<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"no\"?>" +
				"<message merchantId=\""+payRequest.merchantId+"\" merchantOrderId=\""+payRequest.merchantOrderId+"\" " +
				"codeUrl=\"\" respCode=\"-1\" respDesc=\""+e.getMessage()+"\"/>";
		}
	}
	/**
	 * 渠道补单
	 * @param payordno 订单号
	 * @throws Exception 异常捕获
	 */
	public void channelQuery(PayOrder payOrder) throws Exception{
		try{
			PayChannelRotation rotation = new PayChannelRotationDAO().getPayChannelRotationOrderNo(payOrder.payordno);//轮询信息
			Map<String,String> map = new HashMap<String, String>();
		   	map.put("service","unified.trade.query");
		   	map.put("mch_id",  rotation!=null?rotation.merchantId:PayConstant.PAY_CONFIG.get("PAY_ZXBJ_WX_MERCHAT_NO"));//商户号。
		   	map.put("version", "2.0");//版本号。
		   	map.put("charset", "UTF-8");//字符集。
		   	map.put("sign_type", "MD5");//签名类型，只支持MD5
		   	map.put("out_trade_no", payOrder.payordno);//订单号。
		   	map.put("nonce_str", String.valueOf(new Date().getTime()));//随机字符串。
		   	Map<String,String> params = SignUtils.paraFilter(map);//过滤空值。
		    StringBuilder buf = new StringBuilder((params.size() +1) * 10);
		    SignUtils.buildPayParams(buf,params,false);
		    String preStr = buf.toString();
		    String sign = MD5.sign(preStr, "&key="+(rotation!=null?rotation.md5Key:PayConstant.PAY_CONFIG.get("PAY_ZXBJ_WX_KEY")), "utf-8");
		    map.put("sign", sign);
		    log.info("中信BJ微信支付查询请求报文:"+XmlUtils.parseXML(map));
			String rltXml=new String(new DataTransUtil().doPost(PayConstant.PAY_CONFIG.get("PAY_ZXBJ_WX_URL"), XmlUtils.parseXML(map).getBytes("utf-8")),"utf-8");
		    log.info("中信BJ微信支付查询返回报文:"+rltXml);
			Map<String,String> resultMap = XmlUtils.toMap(rltXml.getBytes("utf-8"), "utf8");
			if(SignUtils.checkParam(resultMap,rotation!=null?rotation.md5Key:PayConstant.PAY_CONFIG.get("PAY_ZXBJ_WX_KEY"))){
				if("0".equals(resultMap.get("status")) && "0".equals(resultMap.get("result_code"))){
			        if("SUCCESS".equalsIgnoreCase(resultMap.get("trade_state"))){
			            payOrder.ordstatus="01";//支付成功
				        new NotifyInterface().notifyMer(payOrder);//支付成功
			         } else throw new Exception("支付渠道状态："+resultMap.get("trade_state"));
				}
			} else throw new Exception("验签失败");
		} catch (Exception e){
			e.printStackTrace();
			throw e;
		}
	}
	
    /**
     * 对账文件下载
     * @param request
     * @param tempF 
     * @return
     */
	
	//  https://open.swiftpass.cn/openapi 请通过网页下载相关需要接口文档
	
	public  List<List<String>> bjxzAccountFileDownload(HttpServletRequest request, File tempF) {
		try {
		    String bill_date=request.getParameter("accountDate").replaceAll("-","");
			String accInfos=request.getParameter("accInfos"); 
			if(accInfos==null||bill_date==null)return null;
			String [] accs = accInfos.replaceAll("：",":").split("\n");
			Map<String,String> mapInfo = new HashMap<String,String>();
			for(int i=0; i<accs.length; i++){
				String [] tmp = accs[i].split(":");
				if(tmp.length != 2)continue;
				mapInfo.put(tmp[0].trim(),tmp[1].trim());
			}
			Iterator<String>it = mapInfo.keySet().iterator();
			String acc="";
			String key="";
			while(it.hasNext()){
				acc = it.next();
				key = mapInfo.get(acc);
				Map<String,String> map = new HashMap<String, String>();
			   	map.put("service","pay.bill.merchant");
			   	map.put("mch_id",  acc);//商户号。
			   	map.put("version", "1.0");//版本号。
			   	map.put("charset", "UTF-8");//字符集。
			   	map.put("sign_type", "MD5");//签名类型，只支持MD5
			   	map.put("bill_date", bill_date);//账单日期
			   	map.put("bill_type", "ALL");//随机字符串。
			   	map.put("nonce_str", String.valueOf(new Date().getTime()));//随机字符串。
			   	Map<String,String> params = SignUtils.paraFilter(map);//过滤空值。
			    StringBuilder buf = new StringBuilder((params.size() +1) * 10);
			    SignUtils.buildPayParams(buf,params,false);
			    String preStr = buf.toString();
			    String sign = MD5.sign(preStr, "&key="+key, "utf-8");
			    map.put("sign", sign);
			    log.info("中信BJ对账下载请求报文:"+XmlUtils.parseXML(map));
				String rltXml=new String(new DataTransUtil().doPost(PayConstant.PAY_CONFIG.get("PAY_ZXBJ_DZ_URL"), XmlUtils.parseXML(map).getBytes("utf-8")),"utf-8");	
				writeInFileByfb(rltXml+"\n",tempF);
			}
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(tempF)));
			String fileLinedata = null;
			int num=0,index1=0,index2=0,index3=0,index4=0,index5=0;
			List<List<String>> listC = new ArrayList<List<String>>();//总数据。
			fileLinedata = br.readLine();
			if(fileLinedata!=null){
				String[] datas = fileLinedata.split(",");
				for(int i=0;i<datas.length;i++){
		  			if("交易时间".equals(datas[i]))index1=i;
		  			else if("子商户号".equals(datas[i]))index2=i;
		  			else if("商户订单号".equals(datas[i]))index3=i;
		  			else if("交易状态".equals(datas[i]))index4=i;
		  			else if("总金额".equals(datas[i]))index5=i;	
				}
			}
			while((fileLinedata = br.readLine())!=null){
				List<String> listP = new ArrayList<String>();
				String[] datas = fileLinedata.split(",");
				if(datas.length<5)continue;
//			总交易单数,总交易额,总退款金额,总企业红包退款金额,总手续费金额,总实收金额
//			`181,`29.43,`7.06,`0,`0.20,`22.37
				if("交易时间".equals(datas[0]))continue;
				else if(datas.length==6){
					if("总交易单数".equals(datas[0]))continue;
					else{
						listP.add(datas[0].replace("`",""));
						listP.add(datas[1].replace("`",""));
						listP.add(datas[5].replace("`",""));
						listC.add(listP);
						continue;
					}
				}
				listP.add(datas[index1].replaceAll("`",""));
				listP.add(datas[index2].replaceAll("`",""));
				listP.add(datas[index3].replaceAll("`",""));
				listP.add(datas[index4].replaceAll("`",""));
				listP.add(datas[index5].replaceAll("`",""));
				listC.add(listP);		
			}
			return listC;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private  void writeListToExcel(List<List<String>> list,File templetFile,File tmpFile){
    	Workbook rw = null;
		WritableWorkbook wwb = null; 
		WritableSheet ws = null;
		WritableSheet ws2 = null;
		try {
			List<List<String>> listT=new ArrayList<List<String>>();
			for(int i=0;i<list.size();i++){
				List<String> list_temp = list.get(i);
				if(list_temp.size()==3){
					List<String> listE=new ArrayList<String>();
					listE.add(list.get(i-1).get(1));
					listE.add(list_temp.get(0));
					listE.add(list_temp.get(1));
					listE.add(list_temp.get(2));
					listT.add(listE);
					list.remove(list_temp);
				}
			}
			if (!Tools.copy(templetFile.getAbsolutePath(), tmpFile.getAbsolutePath())) return;
			rw = jxl.Workbook.getWorkbook(tmpFile);
			wwb = Workbook.createWorkbook(tmpFile, rw);
			ws = wwb.getSheet(0);
			ws2=wwb.getSheet(1);
			writeData(list, ws);
			writeData(listT, ws2);
			wwb.write();
			wwb.close();
			rw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }	
	public byte[] accountFileDownloadExportExcel(HttpServletRequest request) {
    	String randomName = Tools.getUniqueIdentify();
    	//临时目录
    	File tmpFile = new File(JWebConstant.APP_PATH+"/dat/download/"+randomName);
    	tmpFile.mkdir();
    	//文件模板名称
    	String templetName ="accountFile";
    	//模板文件
    	File templetFile = new File(JWebConstant.APP_PATH+JWebConstant.PATH_DIV+"templet"+JWebConstant.PATH_DIV+templetName+".xls");
    	log.info(" read templet file:"+JWebConstant.APP_PATH+JWebConstant.PATH_DIV+"templet"+JWebConstant.PATH_DIV+templetName+".xls");
    	//每个Excel最多条数
    	int fileNum = 0;
    	int fileN=0;
    	File tempF=new File(tmpFile.getAbsolutePath()+JWebConstant.PATH_DIV+(fileN++)+".txt");
        try {
        	List<List<String>> list=bjxzAccountFileDownload(request,tempF);
        	writeListToExcel(list,templetFile,new File(tmpFile.getAbsolutePath()+JWebConstant.PATH_DIV+(fileNum++)+".xls"));
        	File zipfile=new File(JWebConstant.APP_PATH+JWebConstant.PATH_DIV+"dat"+JWebConstant.PATH_DIV+"download"+JWebConstant.PATH_DIV+
        		new SimpleDateFormat("yyyy-MM-dd").format(new Date())+"_"+randomName+".zip");
        	File srcfile[] = new File[fileNum];
        	for(int i=0; i<srcfile.length; i++)srcfile[i] = new File(tmpFile.getAbsolutePath()+JWebConstant.PATH_DIV+(i)+".xls");
        	Tools.zipFiles(srcfile, zipfile);
			FileInputStream fis = new FileInputStream(zipfile);
			byte [] b = new byte[fis.available()]; 
			fis.read(b);
			fis.close();
			Tools.deleteFile(tmpFile);
			Tools.deleteFile(tempF);
			Tools.deleteFile(zipfile);
			return b;
        } catch (Exception e) {
            e.printStackTrace();
        }
		return null;
	}
	public static void writeInFileByfb(String content, File tempF) {	
        FileWriter fw=null;
        BufferedWriter bw=null;
        try{
            if(!tempF.exists()){
            	tempF.createNewFile();
            }
             fw=new FileWriter(tempF.getAbsoluteFile(),true);  //true表示可以追加新内容  
             bw=new BufferedWriter(fw);
             bw.write(content);
             bw.close();
        }catch(Exception e){
           e.printStackTrace();
        }   
    }	
	/**
	 * 循环输入数据
	 * @param list
	 * @param ws
	 */
	public static void writeData(List<List<String>> list, WritableSheet ws) {
  		for (int i=0; i < list.size(); i++) {
  			List<String> list_temp = list.get(i);
  			for(int j=0;j<list_temp.size();j++){
  				setCellValue(ws,j,i+1,list_temp.get(j));
  			}
  		} 			
	}
	public static void setCellValue(WritableSheet writeSheet, int x, int y, String value) {
		try {
			WritableCell cell = writeSheet.getWritableCell(x, y);
			if (cell.getType() == CellType.EMPTY) {
				Label write = new Label(x, y, value );
				writeSheet.addCell(write);
			} else {
				Label write = (Label) cell;
				write.setString(value);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
