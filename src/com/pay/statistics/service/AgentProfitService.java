package com.pay.statistics.service;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import jxl.CellType;
import jxl.Workbook;
import jxl.format.Border;
import jxl.format.Colour;
import jxl.format.UnderlineStyle;
import jxl.write.BorderLineStyle;
import jxl.write.Label;
import jxl.write.WritableCell;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import util.JWebConstant;
import util.Tools;

import com.pay.fee.dao.PayFeeRate;
import com.pay.merchant.dao.PayMerchant;
import com.pay.merchant.dao.PayMerchantDAO;
import com.pay.statistics.dao.AgentProfitDAO;
import com.pay.statistics.dao.AgentProfitTrans;
import com.pay.statistics.dao.UserMerchantRalition;

/**
 * 代理分润服务类
 * @author Administrator
 *
 */
public class AgentProfitService {
	private static final Log log = LogFactory.getLog(AgentProfitService.class);
	static Map<String,String> PAY_TYPE = new HashMap<String,String>();
	static {
		PAY_TYPE.put("00","支付账户");
		PAY_TYPE.put("01","网上银行");
		PAY_TYPE.put("03","快捷支付");
		PAY_TYPE.put("07","微信扫码");
		PAY_TYPE.put("10","微信WAP");
		PAY_TYPE.put("11","支付宝扫码");
		PAY_TYPE.put("12","QQ扫码");
	}
	/**
	 * 交易分润表
	 * @param date
	 * @return
	 * @throws Exception 
	 */
	public byte [] getAgentProfitForTran(String dateStart,String dateEnd) throws Exception{
		AgentProfitDAO dao = new AgentProfitDAO();
		PayMerchantDAO merDao = new PayMerchantDAO();
		//取出所有业务员代理，key:【用户ID】，value:【代理商户信息】
		Map <String,List<UserMerchantRalition>> umMap = dao.getUserMerchantRalition();
		//分润信息表，key:【用户ID,代理商户ID】,value:【代理商交易】
		Map <String,Map<String,List<AgentProfitTrans>>> profitMap = new HashMap<String,Map<String,List<AgentProfitTrans>>>();
		//商户信息key:【商户ID】，value：【商户名称】
		Map<String,String> merMapTmp = new HashMap<String,String>();
		Map<String,String> userMap = new HashMap<String,String>();
		Iterator <String>it = umMap.keySet().iterator();
		int n=0,k=0;
		while(it.hasNext()){
			List<UserMerchantRalition> list = umMap.get(it.next());
			n=n+list.size();
		}
		it = umMap.keySet().iterator();
		while(it.hasNext()){
			String userId = it.next();
			List<UserMerchantRalition> list = umMap.get(userId);
			for(int i=0; i<list.size(); i++,k++){
				UserMerchantRalition um = list.get(i);
				PayMerchant agent = merDao.detailPayMerchantByCustId(um.merNo);
				merMapTmp.put(um.merNo, agent.storeName);
				userMap.put(um.userId, um.userName);
				//取出某代理商下所有商户交易
				Map<String,List<AgentProfitTrans>> tmp = new HashMap<String,List<AgentProfitTrans>>();
				tmp = profitMap.get(um.userId);
				log.info(k+"/"+n+"=========================");
				if(tmp == null){
					Map<String,List<AgentProfitTrans>> tmp1 = new HashMap<String,List<AgentProfitTrans>>();
					profitMap.put(um.userId, tmp1);
					List<AgentProfitTrans> l = dao.getAgentProfitByMer(agent,dateStart,dateEnd);
					if(l.size()>0)tmp1.put(um.merNo, l);
				} else {
					if(tmp.get(um.merNo) == null){
						List<AgentProfitTrans> l = dao.getAgentProfitByMer(agent,dateStart,dateEnd);
						if(l.size()>0)tmp.put(um.merNo, l);
					}
				}
			}
		}
		Map <String,PayMerchant>merMap = dao.getAllMerchantAndAgentByAgent(merMapTmp);
		//文件处理
		String randomName = Tools.getUniqueIdentify();
		//文件模板名称
		//模板文件
    	File templetFile = new File(JWebConstant.APP_PATH+"/templet/agentProfitForTran.xls");
    	try {
    		String fileName = JWebConstant.APP_PATH+"/dat/download/"+randomName+".xls";
    		log.info(" read file:"+fileName);
        	//取得业务数据
        	writeListToExcel(profitMap,merMap,userMap,templetFile, new File(fileName),dateStart+"到"+dateEnd);
			FileInputStream fis = new FileInputStream(fileName);
			byte [] b = new byte[fis.available()]; 
			fis.read(b);
			fis.close();
			//Tools.deleteFile(templetFile);
			return b;
		} catch (Exception e) {
			 e.printStackTrace();
		}
		return null;
	}
	/**
	 * 代付分润表
	 * @param date
	 * @return
	 */
	public byte [] getAgentProfitForPay(String dateStart,String dateEnd){
		return null;
	}
	private void writeListToExcel(Map <String,Map<String,List<AgentProfitTrans>>> profitMap,
			Map <String,PayMerchant>merMap,Map<String,String> userMap,File templetFile,File tmpFile,String date){
    	Workbook rw = null;
		WritableWorkbook wwb = null; 
		WritableSheet ws = null;
		try {
			if (!Tools.copy(templetFile.getAbsolutePath(), tmpFile.getAbsolutePath())) return;
			rw = jxl.Workbook.getWorkbook(tmpFile);
			wwb = Workbook.createWorkbook(tmpFile, rw);
			ws = wwb.getSheet(0);
			writeData(profitMap,merMap,userMap,ws,date);
			wwb.write();
			wwb.close();
			rw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
	//交易和费率对应关系（商户费率，代理费率）
	static Map<String,String> FEE_TO_AGENTFEE_MAP=new HashMap<String,String>();
	static Map<String,String> PAY_TYPE_MAP=new HashMap<String,String>();
	static{
		//支付方式，00 支付账户       
		FEE_TO_AGENTFEE_MAP.put("01","7,19");//01 网上银行--7消费B2C借记卡,19代理网银借记卡
//		FEE_TO_AGENTFEE_MAP.put("08","20");//8消费B2C信用卡,20代理网银贷记卡
//		FEE_TO_AGENTFEE_MAP.put("09","21");//9消费B2B,21代理B2B
		FEE_TO_AGENTFEE_MAP.put("07","10,22");//07微信扫码--10微信扫码,22代理微信
//		FEE_TO_AGENTFEE_MAP.put("11","23");//11快捷借记卡,23代理快捷借记卡
		FEE_TO_AGENTFEE_MAP.put("03","12,24");//03 快捷支付--12快捷贷记卡,24代理快捷贷记卡
		FEE_TO_AGENTFEE_MAP.put("11","17,25");//11支付宝扫码--17支付宝扫码,25代理支付宝扫码
		FEE_TO_AGENTFEE_MAP.put("10","18,26");//10微信WAP--18微信WAP,26代理微信wap
		FEE_TO_AGENTFEE_MAP.put("12","27,28");//12手Q--27手Q,28代理手Q
//		FEE_TO_AGENTFEE_MAP.put("15","29");//15代收,29代理代收费率
//		FEE_TO_AGENTFEE_MAP.put("16","30");//16代付,30代理代付费率
	}
	//循环输入数据
	public void writeData(Map <String,Map<String,List<AgentProfitTrans>>> profitMap,
			Map <String,PayMerchant>merMap,Map<String,String> userMap,WritableSheet ws,String date) throws Exception{
		AgentProfitDAO dao = new AgentProfitDAO();
		PayMerchantDAO merDao = new PayMerchantDAO();
		WritableFont font = new WritableFont(WritableFont.ARIAL,8,WritableFont.NO_BOLD,false,UnderlineStyle.NO_UNDERLINE,Colour.BLACK);
		WritableCellFormat cellFormat = new WritableCellFormat(font);
		WritableCellFormat cellFormat1 = new WritableCellFormat(font);
        cellFormat.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//设置垂直居中
        cellFormat.setWrap(true);//设置自动换行
        cellFormat.setBorder(Border.ALL, BorderLineStyle.THIN, Colour.BLACK);
        cellFormat1.setAlignment(jxl.format.Alignment.CENTRE);//设置水平居中
        cellFormat1.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//设置垂直居中
        cellFormat1.setBackground(Colour.LIGHT_GREEN);
        cellFormat1.setBorder(Border.ALL, BorderLineStyle.THIN, Colour.BLACK);
        int curRow = 2;
        Iterator <String>it = profitMap.keySet().iterator();
		while(it.hasNext()){
			String key = it.next();
			Map <String,List<AgentProfitTrans>>agentProfitMap = profitMap.get(key);
			Iterator <String> it1 = agentProfitMap.keySet().iterator();
			int userRowNo = 0;
			while(it1.hasNext()){
				String key1 = it1.next();
				List<AgentProfitTrans> list = agentProfitMap.get(key1);
				int agentRowNo=0;
				long sumCount=0,sumAmtAgent=0,sumFeeAgent=0,sumChannelFeeAgent=0,sumProfitAmt=0,sumProfitTax=0,realProfitAmt=0;
				for (int i=0; i < list.size(); i++,curRow++,agentRowNo++,userRowNo++) {
					AgentProfitTrans apts = list.get(i);
					PayMerchant mer = merDao.detailPayMerchantByCustId(apts.merno);
					String [] feeType = FEE_TO_AGENTFEE_MAP.get(apts.paytype)==null?new String[]{"",""}:FEE_TO_AGENTFEE_MAP.get(apts.paytype).split(",");
					Map<String,PayFeeRate> map = new HashMap<String,PayFeeRate>();
					if(FEE_TO_AGENTFEE_MAP.get(apts.paytype)!=null)map=dao.getCustFee(apts.merno, feeType[0], feeType[1]);
					PayFeeRate merFee = map.get(apts.merno+","+feeType[0]);
					PayFeeRate agentFee = map.get(apts.merno+","+feeType[1]);
					setCellValue(ws, 2, curRow, merMap.get(apts.merno).storeName);//商户名称
					setCellValue(ws, 3, curRow, PAY_TYPE.get(apts.paytype)==null?"未知":PAY_TYPE.get(apts.paytype));//支付类型
					setCellValue(ws, 4, curRow, merFee==null?"":transferFeeInfo(merFee.feeInfo));//商户签约费率
					setCellValue(ws, 5, curRow, agentFee==null?"":transferFeeInfo(agentFee.feeInfo));//代理费率
					setCellValue(ws, 6, curRow, String.valueOf(apts.sumCount));//笔数
					setCellValue(ws, 7, curRow, String.format("%.2f",(double)apts.sumAmt*0.01));//交易金额
					setCellValue(ws, 8, curRow, String.format("%.2f",(double)apts.sumFee*0.01));//手续费
					setCellValue(ws, 9, curRow, String.format("%.2f",(double)apts.sumChannelFee*0.01));//渠道费
					long profitAmt = (long)((double)((apts.sumAmt-apts.sumFee)*mer.agentPayRate)*0.01+0.5);
					setCellValue(ws, 10, curRow, String.format("%.2f",(double)profitAmt*0.01));//应得分润
					setCellValue(ws, 11, curRow, String.valueOf(mer.agentPayRate)+"%");//分润比例
					long profitTax = (long)((((double)profitAmt*mer.agentPayRate)*0.01)*mer.agentTaxRate*0.01+0.5);
					setCellValue(ws, 12, curRow, String.format("%.2f",(double)profitTax*0.01));//税金
					setCellValue(ws, 13, curRow, String.format("%.2f",(double)(profitAmt-profitTax)*0.01));//实得分润
					setCellValue(ws, 14, curRow, date);//交易时间
					sumCount=sumCount+apts.sumCount;
					sumAmtAgent=sumAmtAgent+apts.sumAmt;
					sumFeeAgent=sumFeeAgent+apts.sumFee;
					sumChannelFeeAgent=sumChannelFeeAgent+apts.sumChannelFee;
					sumProfitAmt=sumProfitAmt+profitAmt;
					sumProfitTax=sumProfitTax+profitTax;
					realProfitAmt=realProfitAmt+(profitAmt-profitTax);
				}
//				System.out.println(key+" "+merMap.get(key1).storeName+"("+list.size()+"),"+(curRow-agentRowNo)+"aaaaaa,"+1+","+(curRow-1));
				ws.mergeCells(1, curRow-agentRowNo, 1, curRow-1);				
				//第二列，代理名称合并单元格
				ws.addCell(new Label(1, curRow-agentRowNo,merMap.get(key1).storeName,cellFormat));
				ws.mergeCells(1, curRow, 5, curRow);
				ws.addCell(new Label(1, curRow,"小计",cellFormat1));
				ws.addCell(new Label(6, curRow,String.valueOf(sumCount),cellFormat1));//代理交易笔数
				ws.addCell(new Label(7, curRow, String.format("%.2f",(double)sumAmtAgent*0.01),cellFormat1));//代理交易总金额
				ws.addCell(new Label(8, curRow, String.format("%.2f",(double)sumFeeAgent*0.01),cellFormat1));//代理手续费总金额
				ws.addCell(new Label(9, curRow, String.format("%.2f",(double)sumChannelFeeAgent*0.01),cellFormat1));//代理渠道费总金额
				ws.addCell(new Label(10, curRow,String.format("%.2f",(double)sumProfitAmt*0.01),cellFormat1));
				ws.addCell(new Label(11, curRow,"",cellFormat1));
				ws.addCell(new Label(12, curRow,String.format("%.2f",(double)sumProfitTax*0.01),cellFormat1));
				ws.addCell(new Label(13, curRow,String.format("%.2f",(double)realProfitAmt*0.01),cellFormat1));
				ws.addCell(new Label(14, curRow,"",cellFormat1));
				curRow++;
			}
//			System.out.println(1+","+(curRow-userRowNo)+"mmmmmm,"+1+","+(curRow-1));
			ws.mergeCells(0, curRow-userRowNo-agentProfitMap.size(), 0, curRow-1);
			ws.addCell(new Label(0, curRow-userRowNo-agentProfitMap.size(),userMap.get(key),cellFormat));
		}
	}
  	private void setCellValue(WritableSheet writeSheet, int x, int y,
			String value) {
		try {
			WritableCell cell = writeSheet.getWritableCell(x, y);
			if (cell.getType() == CellType.EMPTY) {
				Label write = new Label(x, y, value);
				writeSheet.addCell(write);
			} else {
				Label write = (Label) cell;
				write.setString(value);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
  	public static String transferFeeInfo(String feeInfo){
    	if(feeInfo != null){
        	String [] fs = feeInfo.split(";");
        	String tmp = "";
	        for(int i = 0; i<fs.length; i++){
	        	String [] es = fs[i].split(",");
	        	if(es.length != 3)continue;
	        	String [] fees = es[0].split("-");
	        	if(fees.length != 2)continue;
	        	tmp = tmp + String.format("%.2f", ((double)Double.parseDouble(fees[0]))/100d)+"~"+
	        			String.format("%.2f", ((double)Double.parseDouble(fees[1]))/100d)+","+
	        			("0".equals(es[1])?"定额":"比例")+","+
	        			("0".equals(es[1])?String.format("%.2f", ((double)Double.parseDouble(es[2]))/100d):es[2]+"%")+";";
	        }
	        if(tmp.length()>0)return tmp.substring(0,tmp.length()-1);
	        else return "";
        } else return "";
    }
}
