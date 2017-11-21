package com.pay.merchantinterface.service;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import util.DataTransUtil;
import com.PayConstant;
import com.capinfo.crypt.Md5;
import com.pay.cardbin.dao.PayCardBin;
import com.pay.cardbin.service.PayCardBinService;
import com.pay.order.dao.PayOrder;
import com.pay.order.dao.PayOrderDAO;
import com.pay.order.dao.PayProductOrder;
import com.pay.order.dao.PayReceiveAndPay;
import com.pay.order.dao.PayReceiveAndPayDAO;
import com.pay.order.service.PayOrderService;
public class SXYPayService {
	private static final Log log = LogFactory.getLog(SXYPayService.class);
	public static Map<String, String> SXY_B2C_BANK_CODE = new HashMap<String, String>();
	public static Map<String, String> SXY_B2C_DF_BANK_CODE = new HashMap<String, String>();
	static {
		SXY_B2C_BANK_CODE.put("CMB","3");
		SXY_B2C_BANK_CODE.put("ICBC","9");
		SXY_B2C_BANK_CODE.put("CCB","4");
		SXY_B2C_BANK_CODE.put("PAB","14");
		SXY_B2C_BANK_CODE.put("CMBC","28");
		SXY_B2C_BANK_CODE.put("CIB","33");
		SXY_B2C_BANK_CODE.put("ABC","43");
		SXY_B2C_BANK_CODE.put("GDB","44");
		SXY_B2C_BANK_CODE.put("BCCB","50");
		SXY_B2C_BANK_CODE.put("PSBC","59");
		SXY_B2C_BANK_CODE.put("HXB","60");
		SXY_B2C_BANK_CODE.put("BOCOM","67");
		SXY_B2C_BANK_CODE.put("SPDB","69");
		SXY_B2C_BANK_CODE.put("CEB","74");
		SXY_B2C_BANK_CODE.put("BRCB","75");
		SXY_B2C_BANK_CODE.put("BOHC","83");
		SXY_B2C_BANK_CODE.put("CNCB","84");
		SXY_B2C_BANK_CODE.put("BOC","85");
		
		
		SXY_B2C_DF_BANK_CODE.put("PSBC","403100000004");//邮政储蓄银行
		SXY_B2C_DF_BANK_CODE.put("CMBC","305100000013");//民生银行
		SXY_B2C_DF_BANK_CODE.put("BCCB","313100000013");//北京银行
		SXY_B2C_DF_BANK_CODE.put("BOS","325290000012");//上海银行
		SXY_B2C_DF_BANK_CODE.put("CMB","308584000013");//招商银行
		SXY_B2C_DF_BANK_CODE.put("CNCB","302100011000");//中信银行
		SXY_B2C_DF_BANK_CODE.put("SPDB","310290000013");//浦发银行
		SXY_B2C_DF_BANK_CODE.put("CIB","309391000011");//兴业银行
		SXY_B2C_DF_BANK_CODE.put("HXB","304100040000");//华夏银行
		SXY_B2C_DF_BANK_CODE.put("ABC","103100000026");//农业银行
		SXY_B2C_DF_BANK_CODE.put("GDB","306581000003");//广发银行
		SXY_B2C_DF_BANK_CODE.put("ICBC","102100099996");//工商银行
		SXY_B2C_DF_BANK_CODE.put("BOC","104100000004");//中国银行
		SXY_B2C_DF_BANK_CODE.put("BOCOM","301290000007");//交通银行
		SXY_B2C_DF_BANK_CODE.put("CCB","105100000017");//建设银行
		SXY_B2C_DF_BANK_CODE.put("PAB","307584007998");//平安银行
		SXY_B2C_DF_BANK_CODE.put("CEB","303100000006");//光大银行
		
	}
	/**
	 * 收银台下单
	 * @param request
	 * @param prdOrder
	 * @param payOrder
	 * @throws Exception
	 */
	public void pay(HttpServletRequest request,PayProductOrder prdOrder,PayOrder payOrder){
		try{
			  payOrder.payChannel = PayConstant.PAY_CONFIG.get("PAY_CHANNEL_SXY"); // 支付渠道
			  payOrder.bankcod = request.getParameter("banks"); // 银行代码
			  payOrder.bankCardType = String.valueOf(Long.parseLong(request.getParameter("bankCardType")));
			  String v_mid = PayConstant.PAY_CONFIG.get("sxy_merchant_id");		//商户编号，签约由易支付分配。250是测试商户编号。
			  String v_ymd = new SimpleDateFormat("yyyyMMdd").format(new Date());//yyyymmdd 订单日期。
			  String v_oid = v_ymd+"-"+v_mid+"-"+payOrder.payordno;  //订单编号，订单编号的格式:yyyymmdd-v_mid-流水号。流水号可以按照自己的订单规则生成，但是要保证每一次提交，订单号是唯一值，否则会出错
			  String v_rcvname = PayConstant.PAY_CONFIG.get("sxy_merchant_id");	//收货人姓名，建议用商户编号代替。
			  String v_rcvaddr = PayConstant.PAY_CONFIG.get("sxy_merchant_id"); //收货地址，可以用常量
			  String v_rcvtel = PayConstant.PAY_CONFIG.get("sxy_merchant_id");  //收货人电话，可以用常量
			  String v_rcvpost = PayConstant.PAY_CONFIG.get("sxy_merchant_id");	//收货人邮编，可以用常量
			  String v_amount = String.format("%.2f", ((double)payOrder.txamt)/100d);//交易金额。
			  String v_orderstatus = "1";//配货状态，0-未配齐，1-已配齐
			  String v_ordername = PayConstant.PAY_CONFIG.get("sxy_merchant_id");//订货人姓名，可以用常量
			  String v_moneytype = "0";  //币种。0-人民币，1-美元。
			  String v_url= PayConstant.PAY_CONFIG.get("sxy_pay_notify_url");//支付完成后返回地址。此地址是支付完成后，订单信息实时的向这个地址做返回。返回参数详见接口文档第二部分。
			  String v_pmode = SXY_B2C_BANK_CODE.get(payOrder.bankcod);//支付银行。
			  String MD5Key=PayConstant.PAY_CONFIG.get("sxy_sign_type");//MD5Key   test ,签约后由商户自定义一个16位的数字字母组合作为密钥，发到kf@payeasenet.com.说明商户编号，公司名称和密钥。
			  //MD5算法
			  Md5 md5 = new Md5 ("") ;
			  md5.hmac_Md5(v_moneytype+v_ymd+v_amount+v_rcvname+v_oid+v_mid+v_url,MD5Key) ;
			  byte b[]= md5.getDigest();
			  String digestString = Md5.stringify(b) ;
			  request.setAttribute("digestString", digestString);
			  request.setAttribute("v_mid", v_mid);
			  request.setAttribute("v_oid", v_oid);
			  request.setAttribute("v_rcvname", v_rcvname);
			  request.setAttribute("v_rcvaddr", v_rcvaddr);
			  request.setAttribute("v_rcvtel", v_rcvtel);
			  request.setAttribute("v_rcvpost", v_rcvpost);
			  request.setAttribute("v_amount", v_amount);
			  request.setAttribute("v_ymd", v_ymd);
			  request.setAttribute("v_orderstatus", v_orderstatus);
			  request.setAttribute("v_ordername", v_ordername);
			  request.setAttribute("v_moneytype", v_moneytype);
			  request.setAttribute("v_pmode", v_pmode);
			  request.setAttribute("v_url", v_url);
			  payOrder.bankjrnno=v_oid;
			  payOrder.bankerror="首信易单号:"+v_oid;
			  PayOrderDAO payOrderDAO = new PayOrderDAO();
			  payOrderDAO.updateOrderBankjrnno(payOrder);
			  payOrderDAO.updateOrderError(payOrder);
			  new PayOrderService().updateOrderForBanks(payOrder);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	/**
	 * 商户接口下单
	 * @param request
	 * @param payRequest
	 * @param prdOrder
	 * @param payOrder
	 * @throws Exception
	 */
	public void pay(HttpServletRequest request,PayRequest payRequest,PayProductOrder prdOrder,PayOrder payOrder){
		try{
			  payOrder.payChannel = PayConstant.PAY_CONFIG.get("PAY_CHANNEL_SXY"); // 支付渠道
			  payOrder.bankcod = payRequest.bankId; // 银行代码
			  payOrder.bankCardType = payRequest.accountType;
			  String v_mid = PayConstant.PAY_CONFIG.get("sxy_merchant_id");		//商户编号，签约由易支付分配。250是测试商户编号。
			  String v_ymd = new SimpleDateFormat("yyyyMMdd").format(new Date());//yyyymmdd 订单日期。
			  String v_oid = v_ymd+"-"+v_mid+"-"+payOrder.payordno;  //订单编号，订单编号的格式:yyyymmdd-v_mid-流水号。流水号可以按照自己的订单规则生成，但是要保证每一次提交，订单号是唯一值，否则会出错
			  String v_rcvname = PayConstant.PAY_CONFIG.get("sxy_merchant_id");	//收货人姓名，建议用商户编号代替。
			  String v_rcvaddr = PayConstant.PAY_CONFIG.get("sxy_merchant_id"); //收货地址，可以用常量
			  String v_rcvtel = PayConstant.PAY_CONFIG.get("sxy_merchant_id");  //收货人电话，可以用常量
			  String v_rcvpost = PayConstant.PAY_CONFIG.get("sxy_merchant_id");	//收货人邮编，可以用常量
			  String v_amount = String.format("%.2f", ((double)payOrder.txamt)/100d);//交易金额。
			  String v_orderstatus = "1";//配货状态，0-未配齐，1-已配齐
			  String v_ordername = PayConstant.PAY_CONFIG.get("sxy_merchant_id");//订货人姓名，可以用常量
			  String v_moneytype = "0";  //币种。0-人民币，1-美元。
			  String v_url= PayConstant.PAY_CONFIG.get("sxy_pay_notify_url");//支付完成后返回地址。此地址是支付完成后，订单信息实时的向这个地址做返回。返回参数详见接口文档第二部分。
			  String v_pmode = SXY_B2C_BANK_CODE.get(payOrder.bankcod);//支付银行。
			  String MD5Key=PayConstant.PAY_CONFIG.get("sxy_sign_type");//MD5Key   test ,签约后由商户自定义一个16位的数字字母组合作为密钥，发到kf@payeasenet.com.说明商户编号，公司名称和密钥。
			  //MD5算法
			  Md5 md5 = new Md5 ("") ;
			  md5.hmac_Md5(v_moneytype+v_ymd+v_amount+v_rcvname+v_oid+v_mid+v_url,MD5Key) ;
			  byte b[]= md5.getDigest();
			  String digestString = Md5.stringify(b) ;
			  request.setAttribute("digestString", digestString);
			  request.setAttribute("v_mid", v_mid);
			  request.setAttribute("v_oid", v_oid);
			  request.setAttribute("v_rcvname", v_rcvname);
			  request.setAttribute("v_rcvaddr", v_rcvaddr);
			  request.setAttribute("v_rcvtel", v_rcvtel);
			  request.setAttribute("v_rcvpost", v_rcvpost);
			  request.setAttribute("v_amount", v_amount);
			  request.setAttribute("v_ymd", v_ymd);
			  request.setAttribute("v_orderstatus", v_orderstatus);
			  request.setAttribute("v_ordername", v_ordername);
			  request.setAttribute("v_moneytype", v_moneytype);
			  request.setAttribute("v_pmode", v_pmode);
			  request.setAttribute("v_url", v_url);
			  payOrder.bankjrnno=v_oid;
			  payOrder.bankerror="首信易单号:"+v_oid;
			  PayOrderDAO payOrderDAO = new PayOrderDAO();
			  payOrderDAO.updateOrderBankjrnno(payOrder);
			  payOrderDAO.updateOrderError(payOrder);
			  new PayOrderService().updateOrderForBanks(payOrder);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	/**
	 * 渠道补单---网关
	 * @param payordno 订单号
	 * @throws Exception 异常捕获
	 */
	public void channelQuery(PayOrder payOrder) throws Exception{
		try{
			  String v_mid=PayConstant.PAY_CONFIG.get("sxy_merchant_id");
			  String v_oid=payOrder.bankjrnno;
			  String MD5Key=PayConstant.PAY_CONFIG.get("sxy_sign_type");//MD5Key   test ,签约后由商户自定义一个16位的数字字母组合作为密钥，发到kf@payeasenet.com.说明商户编号，公司名称和密钥。
			  //MD5算法
			  Md5 md5 = new Md5 ("") ;
			  md5.hmac_Md5(v_mid+v_oid,MD5Key) ;
			  byte b[]= md5.getDigest();
			  String v_mac = Md5.stringify(b) ;
			  String sendData="v_mid="+v_mid+"&v_oid="+v_oid+"&v_mac="+v_mac;
			  log.info("首信易网关查询请求:"+sendData);
			  String payResult = new String(new DataTransUtil().doPost(PayConstant.PAY_CONFIG.get("sxy_pay_query_url"),sendData.getBytes("utf-8")),"utf-8");
			  log.info("首信易网关查询响应:"+payResult);
			  DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();   
			     DocumentBuilder builder = factory.newDocumentBuilder();   
			     Document document = builder.parse(new InputSource(new StringReader(payResult)));   
			     Element root = document.getDocumentElement();
			     NodeList list = root.getChildNodes();
			     Map<String,String> resultMap=new HashMap<String, String>();
			     
			     for(int i=0;i<list.getLength();i++){
			    	 Node node1 =list.item(i);
			    	 if(node1.hasChildNodes()){
			    		 NodeList list2 = node1.getChildNodes();
			    		 for(int j=0;j<list2.getLength();j++){
			    			 Node node2 = list2.item(j);
			    			 if("order".equals(node2.getNodeName())){
			    				 NodeList list3 = node2.getChildNodes();
			    				 for(int k=0;k<list3.getLength();k++){
			    					 Node node3 = list3.item(k);
			    					 resultMap.put(node3.getNodeName(), node3.getTextContent());
			    				 }
			    			 }else{
			    				 resultMap.put(node2.getNodeName(), node2.getTextContent());
			    			 }
			    		 }
			    	 }else{
			    		 resultMap.put(node1.getNodeName(), node1.getTextContent());
			    	 }
			     }
			  if("0".equals(resultMap.get("status"))){
				  if("1".equals(resultMap.get("pstatus"))){
					  payOrder.ordstatus="01";//支付成功
				      new NotifyInterface().notifyMer(payOrder);//支付成功
				  }else if("3".equals(resultMap.get("pstatus"))){
					  payOrder.ordstatus="02";//支付失败。
				      new NotifyInterface().notifyMer(payOrder);//支付失败。
				  }
			  }else{
				  new Exception("查询失败");
			  }
		} catch (Exception e){
			e.printStackTrace();
			throw e;
		}
	}
	 /**
     * 单笔代付
     * @param payRequest
     * @throws Exception 
     */
    public void receivePaySingle(PayRequest payRequest) throws Exception{
    	try {
    		PayReceiveAndPay rp = payRequest.receiveAndPaySingle;
    		if("1".equals(payRequest.receivePayType)){//代付业务。
    			PayCardBin cardBin = PayCardBinService.getCardBinByCardNo(payRequest.accNo);
    			if(cardBin == null)throw new Exception("无法识别银行账号"); ;
    			//商户号。
    			String v_mid = PayConstant.PAY_CONFIG.get("sxy_merchant_id");
    			//版本号。固定的。
    			String v_version="1.0"; 
    			//v_data=2|600.00$5400001|张三|北京建行东四支行|北京市|北京市|400.00|2001|105100000017
    			StringBuffer v_data = new StringBuffer();
    			//代付笔数。提现接口固定为  1。
    			v_data.append("1|");
    			//总金额。
    			v_data.append(String.format("%.2f", (Double.parseDouble(payRequest.amount))/100d)+"|");
    			v_data.append(v_mid+"-"+new SimpleDateFormat("yyyMMdd").format(new Date())+"-"+rp.id+"$");
    			//收款方账号。
    			v_data.append(payRequest.accNo+"|");
    			//开户名
    			v_data.append(payRequest.accName+"|");
    			//开户支行。
    			v_data.append(cardBin.bankName+"|");
    			//收方省份。
    			v_data.append("北京市|");
    			//收方城市。
    			v_data.append("北京市|");
    			//付款金额。
    			v_data.append(String.format("%.2f", (Double.parseDouble(payRequest.amount))/100d)+"|");
    			//客户标示，流水号，时间戳。
    			v_data.append(rp.id+"|");
    			//联行号
    			v_data.append(SXY_B2C_DF_BANK_CODE.get(cardBin.bankCode));
    			String tempStr = java.net.URLEncoder.encode(v_mid+v_data.toString(),"utf-8");
    			Md5 md5 = new Md5 ("") ;
    		    md5.hmac_Md5(tempStr,PayConstant.PAY_CONFIG.get("sxy_sign_type")) ;
    			byte b[]= md5.getDigest();
    			String v_mac = Md5.stringify(b) ;
    			String params="v_mid="+v_mid+"&v_version="+v_version+"&v_mac="+v_mac+"&v_data="+v_data.toString();
    			log.info("首信易代付请求参数:"+params);
    			String SyxDfUrl=PayConstant.PAY_CONFIG.get("sxy_merchant_df_url");
    			String rltXml=new String(new DataTransUtil().doPost(SyxDfUrl, params.getBytes("utf-8")),"utf-8");
    			log.info("首信易代付响应参数:"+rltXml);
    			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();   
			    DocumentBuilder builder = factory.newDocumentBuilder();   
			    Document document = builder.parse(new InputSource(new StringReader(rltXml)));   
			    Element root = document.getDocumentElement();
			    NodeList list = root.getChildNodes();
			    Map<String,String> resultMap=new HashMap<String, String>();
			    for(int i=0;i<list.getLength();i++){
			    	Node node =list.item(i);
			    	resultMap.put(node.getNodeName(), node.getTextContent());
			    }
			    if(resultMap!=null){
			    	if("0".equals(resultMap.get("status"))){
			    		rp.status="0";
    					rp.errorMsg = "提交成功";
    		        	try{new PayReceiveAndPayDAO().updatePayReceiveAndPayById(rp);} catch(Exception e){}
    		        	SXYDFquery query = new SXYDFquery(payRequest.receiveAndPaySingle.id,payRequest);
    		        	query.start();
			    	}else{
			    		rp.status="2";
						rp.errorMsg = resultMap.get("statusdesc");
			        	try{new PayReceiveAndPayDAO().updatePayReceiveAndPayById(rp);} catch(Exception e){}
			    	}
			    }
    		}
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    /**
     * 代收付查询(单笔)
     * @param request
     * @return
     * @throws Exception
     */
    public boolean receivePaySingleQuery(PayRequest request,PayReceiveAndPay rp) throws Exception{
		try {
			String v_mid = PayConstant.PAY_CONFIG.get("sxy_merchant_id");
			//版本号。固定的。
			String v_version="1.0"; 
			String v_data=rp.id;
			String tempStr = java.net.URLEncoder.encode(v_mid+v_data.toString(),"utf-8");
			Md5 md5 = new Md5 ("") ;
		    md5.hmac_Md5(tempStr,PayConstant.PAY_CONFIG.get("sxy_sign_type")) ;
			byte b[]= md5.getDigest();
			String v_mac = Md5.stringify(b) ;
			String params="v_mid="+v_mid+"&v_version="+v_version+"&v_mac="+v_mac+"&v_data="+v_data.toString();
			log.info("首信易代付查询请求参数:"+params);
			String SyxDfUrl=PayConstant.PAY_CONFIG.get("sxy_merchant_df_query_url");
			String rltXml=new String(new DataTransUtil().doPost(SyxDfUrl, params.getBytes("utf-8")),"utf-8");
			log.info("首信易代付查询响应参数:"+rltXml);
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();   
		    DocumentBuilder builder = factory.newDocumentBuilder();   
		    Document document = builder.parse(new InputSource(new StringReader(rltXml)));   
		    Element root = document.getDocumentElement();
		    NodeList list = root.getChildNodes();
		    Map<String,String> resultMap=new HashMap<String, String>();
		    for(int i=0;i<list.getLength();i++){
		    	Node node =list.item(i);
		    	resultMap.put(node.getNodeName(), node.getTextContent());
		    }
			if("1".equals(resultMap.get("status"))){
				request.setRespCode("000");
				request.receiveAndPaySingle.setRespCode("000");
				request.receivePayRes = "0";
				request.respDesc="交易成功";
				rp.errorMsg = "交易成功";
				return true;
			}else if("3".equals(resultMap.get("status"))){
				request.setRespCode("-1");
				request.receiveAndPaySingle.setRespCode("000");
				request.receivePayRes = "-1";
				request.respDesc="交易失败";
				rp.errorMsg = request.respDesc;
				return false;
			}else{
				request.setRespCode("0");//处理中
				request.receiveAndPaySingle.setRespCode("000");
				request.receivePayRes = "-1";
				request.respDesc="处理中";
				rp.errorMsg = request.respDesc;
				return false;
			}
		} catch (Exception e) {
				e.printStackTrace();
				request.setRespCode("-1");
				rp.setRespCode(request.respCode);
				request.receivePayRes = "-1";
				request.respDesc=e.getMessage();
				rp.errorMsg = e.getMessage();
				return false;
		}
    }
}
class SXYDFquery extends Thread{
	private static final Log log = LogFactory.getLog(SXYDFquery.class);
	public PayRequest payRequest= new PayRequest();
	public String merBatchNo="";
	public SXYDFquery(String merBatchNo,PayRequest payRequest) {
		this.merBatchNo = merBatchNo;
		this.payRequest=payRequest;
	}
	@Override
	public void run() {
		try {
//			Thread.sleep(1000*60*20);
//			for(int i=0;i<5;i++){
//				if(query()){
//					break;
//				}
//				Thread.sleep(1000*60);
//			}
			for(int i=0; i<CJPayService.CJ_RECEIVE_PAY_SEARCH_TIME_FREQUENCY.length; i++){
				try {if(query())break;} catch (Exception e) {}
				log.info("首信易代付查询第"+(i+1)+"次,等待时间"+CJPayService.CJ_RECEIVE_PAY_SEARCH_TIME_FREQUENCY[i]+"秒");
				try {Thread.sleep(CJPayService.CJ_RECEIVE_PAY_SEARCH_TIME_FREQUENCY[i]*1000);} catch (Exception e) {}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public boolean query(){
		try {
			String v_mid = PayConstant.PAY_CONFIG.get("sxy_merchant_id");
			//版本号。固定的。
			String v_version="1.0"; 
			String v_data=merBatchNo;
			String tempStr = java.net.URLEncoder.encode(v_mid+v_data.toString(),"utf-8");
			Md5 md5 = new Md5 ("") ;
		    md5.hmac_Md5(tempStr,PayConstant.PAY_CONFIG.get("sxy_sign_type")) ;
			byte b[]= md5.getDigest();
			String v_mac = Md5.stringify(b) ;
			String params="v_mid="+v_mid+"&v_version="+v_version+"&v_mac="+v_mac+"&v_data="+v_data.toString();
			log.info("首信易代付查询请求参数:"+params);
			String SyxDfUrl=PayConstant.PAY_CONFIG.get("sxy_merchant_df_query_url");
			String rltXml=new String(new DataTransUtil().doPost(SyxDfUrl, params.getBytes("utf-8")),"utf-8");
			log.info("首信易代付查询响应参数:"+rltXml);
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();   
		    DocumentBuilder builder = factory.newDocumentBuilder();   
		    Document document = builder.parse(new InputSource(new StringReader(rltXml)));   
		    Element root = document.getDocumentElement();
		    NodeList list = root.getChildNodes();
		    Map<String,String> resultMap=new HashMap<String, String>();
		    for(int i=0;i<list.getLength();i++){
		    	Node node =list.item(i);
		    	resultMap.put(node.getNodeName(), node.getTextContent());
		    }
		    if("1".equals(resultMap.get("status"))){
		    	PayReceiveAndPay rp = new PayReceiveAndPay();
				rp.id=merBatchNo;
				rp.status="1";
				rp.respCode="000";
				rp.errorMsg="交易成功";
				try{new PayReceiveAndPayDAO().updatePayReceiveAndPayById(rp);} catch(Exception e){}
				List<PayReceiveAndPay> list2 = new ArrayList<PayReceiveAndPay>();
				list2.add(rp);
				new PayInterfaceOtherService().receivePayNotify(payRequest,list2);
				return true;
			} else if ("3".equals(resultMap.get("status"))){
				PayReceiveAndPay rp = new PayReceiveAndPay();
				rp.id=merBatchNo;
				rp.status="2";
				rp.respCode="-1";
				rp.errorMsg="交易失败";
				try{new PayReceiveAndPayDAO().updatePayReceiveAndPayById(rp);} catch(Exception e){}
				List<PayReceiveAndPay> list3 = new ArrayList<PayReceiveAndPay>();
				list3.add(rp);
				new PayInterfaceOtherService().receivePayNotify(payRequest,list3);
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
}
