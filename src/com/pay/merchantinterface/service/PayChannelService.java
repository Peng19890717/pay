package com.pay.merchantinterface.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.PayConstant;
import com.pay.order.dao.PayOrder;
import com.pay.order.dao.PayOrderDAO;

public class PayChannelService {
	private static final Log log = LogFactory.getLog(PayChannelService.class);
	private Timer timer = new Timer();
	public static List<Integer> NOTIFY_SEARCH_CHANNEL_TIME=new ArrayList<Integer>();
	static {
		init();
	}
	/**
	 * 载入渠道定时查单参数
	 */
	public static void init(){
		NOTIFY_SEARCH_CHANNEL_TIME=new ArrayList<Integer>();
		try {
			String tmp = PayConstant.PAY_CONFIG.get("NOTIFY_SEARCH_CHANNEL_TIME");
			if(tmp!=null){
				String [] e = tmp.replaceAll("，",",").split(",");
				int n=0;
				for(int i=0; i<e.length; i++){
					int time = -1;
					try {time = Integer.parseInt(e[i]);} catch (Exception e2) {}
					if(time > 0 && time <20){
						NOTIFY_SEARCH_CHANNEL_TIME.add(n++,time);
					}
				}
			}			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void searchOrderForChannelNotifyFail(String payordno){
		if(NOTIFY_SEARCH_CHANNEL_TIME.size()>0){
			timer.schedule(new SearchOrderForChannelNotifyFailThread(payordno,timer,1),
					NOTIFY_SEARCH_CHANNEL_TIME.get(0)*60*1000);
		}
	}
	/**
	 * 从支付渠道补单支付订单，同步成功状态
	 * @param payordno
	 * @throws Exception
	 */
	public void searchOrderFromChannel(String payordno) throws Exception{
		PayOrder payOrder = new PayOrder();
        payOrder.setPayordno(payordno);
        payOrder = new PayOrderDAO().getPayOrderDetailForAll(payOrder);
		if(payOrder == null || payOrder.payChannel == null)throw new Exception("支付渠道不存在或无效，补单失败");
		//订单状态，00:未付款  01:付款成功  02:付款失败 03:支付处理中 04:退款成功 05:退款失败 06:撤销成功 07:撤销失败 08：订单作废 默认00
		if("01".equals(payOrder.ordstatus))return;
		//先锋支付查单接口
		if(payOrder.payChannel.equals(PayConstant.PAY_CONFIG.get("PAY_CHANNEL_XF"))){
			if("01".equals(payOrder.paytype)){//网关查询。
				new XFPayService().channelQuery(payOrder, "REQ_ORDER_QUERY_BY_ID");
			}else if("03".equals(payOrder.paytype)){//快捷。
				new XFPayService().channelQuery(payOrder, "REQ_QUICK_QUERY_BY_ID");
			}
		//融保渠道查询接口
		} else if(payOrder.payChannel.equals(PayConstant.PAY_CONFIG.get("PAY_CHANNEL_RB"))){
			new RBPayService().channelQuery(payOrder);
		//富友渠道查询接口
		} else if(payOrder.payChannel.equals(PayConstant.PAY_CONFIG.get("PAY_CHANNEL_FY"))){
			//支付方式，00 支付账户  01 网上银行 03 快捷支付
			if("03".equals(payOrder.paytype))new FYPayService().quickPaychannelQuery(payOrder);
			else new FYPayService().channelQuery(payOrder);
		}else if(payOrder.payChannel.equals(PayConstant.PAY_CONFIG.get("PAY_CHANNEL_FY2"))){
			//支付方式，00 支付账户  01 网上银行 03 快捷支付
			if("03".equals(payOrder.paytype))new FY2PayService().quickPaychannelQuery(payOrder);
			else new FY2PayService().channelQuery(payOrder);
		}else if(payOrder.payChannel.equals(PayConstant.PAY_CONFIG.get("PAY_CHANNEL_FY3"))){
			//支付方式，00 支付账户  01 网上银行 03 快捷支付
			if("03".equals(payOrder.paytype))new FY3PayService().quickPaychannelQuery(payOrder);
			else new FY3PayService().channelQuery(payOrder);
		//畅捷渠道查询接口。
		} else if(payOrder.payChannel.equals(PayConstant.PAY_CONFIG.get("PAY_CHANNEL_CJ"))){
			new CJPayService().channelQuery(payOrder);
			new NotifyInterface().notifyMer(payOrder);
		//平台渠道查询接口。
		} else if(payOrder.payChannel.equals(PayConstant.PAY_CONFIG.get("PAY_CHANNEL_PLTPAY"))){
			new PLTPayService().channelQuery(payOrder);
		//宝付渠道查询接口。
		} else if(payOrder.payChannel.equals(PayConstant.PAY_CONFIG.get("PAY_CHANNEL_BF"))){
			//支付方式，00 支付账户  01 网上银行 03 快捷支付
			if("03".equals(payOrder.paytype))new BFPayService().quickPayChannelQuery(payOrder);
			else new BFPayService().channelQuery(payOrder);
		//创新支付微信扫码
		} else if(payOrder.payChannel.equals(PayConstant.PAY_CONFIG.get("PAY_CHANNEL_CX"))){
			//支付方式，00 支付账户  01 网上银行 03 快捷支付
			if("03".equals(payOrder.paytype)){
				new CXPayService().quickPaychannelQuery(payOrder);
			}else if("01".equals(payOrder.paytype)){
				new CXPayService().channelQuery(payOrder);
			}else new CXWeiXinService().channelQuery(payOrder);
		//酷宝支付
		} else if(payOrder.payChannel.equals(PayConstant.PAY_CONFIG.get("PAY_CHANNEL_KB"))){
			new KBPayService().channelQuery(payOrder);
		//雅酷微信扫码
//		} else if(payOrder.payChannel.equals(PayConstant.PAY_CONFIG.get("PAY_CHANNEL_YK"))){
//			new YKPayService().channelQuery_New(payOrder);
		//京东支付
		} else if(payOrder.payChannel.equals(PayConstant.PAY_CONFIG.get("PAY_CHANNEL_JD"))){
			//支付方式，00 支付账户  01 网上银行 03 快捷支付
			if("03".equals(payOrder.paytype))new JDPayService().quickPayChannelQuery(payOrder);
			else new JDPayService().channelQuery(payOrder);
		//中信查询。	
		}else if(payOrder.payChannel.equals(PayConstant.PAY_CONFIG.get("PAY_CHANNEL_ZX"))){
			new ZXPayService().channelQuery(payOrder);
		//银生宝网关支付。
		}else if(payOrder.payChannel.equals(PayConstant.PAY_CONFIG.get("PAY_CHANNEL_YSB"))){
			new YSBPayService().channelQuery(payOrder);
		//金运通快捷支付查询。
		}else if(payOrder.payChannel.equals(PayConstant.PAY_CONFIG.get("PAY_CHANNEL_JYT"))){
			//支付方式，00 支付账户  01 网上银行 03 快捷支付
			if("03".equals(payOrder.paytype))new JYTPayService().quickPayChannelQuery(payOrder);
			else new JYTPayService().channelQuery(payOrder);
		//商物通扫码支付查询。	
		}else if(payOrder.payChannel.equals(PayConstant.PAY_CONFIG.get("PAY_CHANNEL_SWT"))){
			new SWTPayService().channelQuery(payOrder);
		//中信北京扫码查询。
		}else if(payOrder.payChannel.equals(PayConstant.PAY_CONFIG.get("PAY_CHANNEL_ZXBJ"))){
			if("12".equals(payOrder.paytype)) new WFTPayService().channelQuery(payOrder);
			else new ZX_BJ_PayService().channelQuery(payOrder);
		//雅酷-道合扫码查询。
		}/**else if(payOrder.payChannel.equals(PayConstant.PAY_CONFIG.get("PAY_CHANNEL_DH"))){
			new DHPayService().channelQuer(payOrder);
		//网上有名扫码查询。
		}else if(payOrder.payChannel.equals(PayConstant.PAY_CONFIG.get("PAY_CHANNEL_WSYM"))){
			new WSYMPayService().channelQuery(payOrder);
		//恒丰扫码查单
		}else if(payOrder.payChannel.equals(PayConstant.PAY_CONFIG.get("PAY_CHANNEL_HF"))){
			new HFPayService().channelQuery(payOrder);
		//北京农商扫码查单
		}*/else if(payOrder.payChannel.equals(PayConstant.PAY_CONFIG.get("PAY_CHANNEL_BNS"))){
			new BNSPayService().channelQuery(payOrder);
		//支付通扫码查单。
		}else if(payOrder.payChannel.equals(PayConstant.PAY_CONFIG.get("PAY_CHANNEL_ZFT"))){
			new ZFTPayService().channelQuery(payOrder);
		//易势
		}else if(payOrder.payChannel.equals(PayConstant.PAY_CONFIG.get("PAY_CHANNEL_YS"))){
			//支付方式，00 支付账户  01 网上银行 03 快捷支付
			if("01".equals(payOrder.paytype)){//网关查询。
				new YSPayService().channelQuery(payOrder);
			}else{//扫码查询。
				new YSPayService().ScanchannelQuery(payOrder);
			}
		//银盈通网关。
		} else if(payOrder.payChannel.equals(PayConstant.PAY_CONFIG.get("PAY_CHANNEL_YYT"))){
			new YYTPayService().channelQuery(payOrder);
		}
		//溢+ 17.7.5
		else if (payOrder.payChannel.equals(PayConstant.PAY_CONFIG.get("PAY_CHANNEL_EMAX"))){
			new EjPayService().channelQuery(payOrder);
		}
		//首信易
		else if(payOrder.payChannel.equals(PayConstant.PAY_CONFIG.get("PAY_CHANNEL_SXY"))){
			new SXYPayService().channelQuery(payOrder);
		//易汇金
		}else if(payOrder.payChannel.equals(PayConstant.PAY_CONFIG.get("PAY_CHANNEL_YHJ"))){
			new YHJPayService().channelQuery(payOrder);
		//天付宝网关。
		}else if(payOrder.payChannel.equals(PayConstant.PAY_CONFIG.get("PAY_CHANNEL_TFB"))){
			new TFBpayService().channelQuery(payOrder);
		//随行付。
		}else if(payOrder.payChannel.equals(PayConstant.PAY_CONFIG.get("PAY_CHANNEL_SXF"))){
			//支付方式，00 支付账户  01 网上银行 03 快捷支付
			if("01".equals(payOrder.paytype)){//网关查询。
				new SXFpayService().channelQuery(payOrder);
			}
		//新随行付。
		}else if(payOrder.payChannel.equals(PayConstant.PAY_CONFIG.get("PAY_CHANNEL_SXFNEW"))){
			//支付方式，00 支付账户  01 网上银行 03 快捷支付
			if("01".equals(payOrder.paytype)){//网关查询。
				new SXFNEWpayService().channelQuery(payOrder);
			}
		//高汇通
		}else if(payOrder.payChannel.equals(PayConstant.PAY_CONFIG.get("PAY_CHANNEL_GHT"))){
			//支付方式，00 支付账户  01 网上银行 03 快捷支付
			if("01".equals(payOrder.paytype)){//网关查询。
				new GHTPayService().channelQuery(payOrder, "01");
			}else if("03".equals(payOrder.paytype)){//快捷。
				new GHTPayService().channelQuery(payOrder, "03");
			}
		//国付宝
		}else if(payOrder.payChannel.equals(PayConstant.PAY_CONFIG.get("PAY_CHANNEL_GFB"))){
			if ("01".equals(payOrder.paytype)) {//网银
				new GFBPayService().channelQuery(payOrder, "01");
			}
		//邦付宝查询。
		}else if(payOrder.payChannel.equals(PayConstant.PAY_CONFIG.get("PAY_CHANNEL_BFB"))){
				new BFBPayService().quickPaychannelQuery(payOrder);
		//亿联通付
		}else if(payOrder.payChannel.equals(PayConstant.PAY_CONFIG.get("PAY_CHANNEL_YLTF"))){
			new YLTFPayService().channelQuery(payOrder);
		//商银信查询
		}else if(payOrder.payChannel.equals(PayConstant.PAY_CONFIG.get("PAY_CHANNEL_SYX"))){
			new SYXPayService().channelQuery(payOrder);
		//联动查询
		}else if(payOrder.payChannel.equals(PayConstant.PAY_CONFIG.get("PAY_CHANNEL_LD"))){
			new LDPayService().channelQuery(payOrder);
		//酷宝新扫码接口查单。
		}else if(payOrder.payChannel.equals(PayConstant.PAY_CONFIG.get("PAY_CHANNEL_KBNEW"))){
			if("01".equals(payOrder.paytype)){//网关查询。
				new KBNEWPayService().channelQuery_WY(payOrder);
			}else{//扫码
				new KBNEWPayService().channelQuery(payOrder);
			}
		//易宝网银查询
		}else if(payOrder.payChannel.equals(PayConstant.PAY_CONFIG.get("PAY_CHANNEL_YB"))){
			new YBPayService().channelQuery(payOrder);
		//长盈查询
		}else if(payOrder.payChannel.equals(PayConstant.PAY_CONFIG.get("PAY_CHANNEL_CY"))){
		//沃雷特查询
		}else if(payOrder.payChannel.equals(PayConstant.PAY_CONFIG.get("PAY_CHANNEL_WLT"))){
			new WLTPayService().channelQuery(payOrder);
		//统统付查询
		}else if(payOrder.payChannel.equals(PayConstant.PAY_CONFIG.get("PAY_CHANNEL_TTF"))){
			new TTFPayService().channelQuery(payOrder);
		//快乐付扫码查询
		}else if(payOrder.payChannel.equals(PayConstant.PAY_CONFIG.get("PAY_CHANNEL_KLF"))){
			new KLFPayService().channelQuery(payOrder);
		//易通支付查单
		}else if(payOrder.payChannel.equals(PayConstant.PAY_CONFIG.get("PAY_CHANNEL_YTZF"))){
			new YTZFPayService().channelQuery(payOrder);
		}
	}
}
class SearchOrderForChannelNotifyFailThread extends TimerTask{
	String payordno;
	Timer timer;
	int times;
	public SearchOrderForChannelNotifyFailThread(String payordno,Timer timer,int times){
		this.payordno = payordno;
		this.timer = timer;
		this.times = times;
	}
	public void run(){
		try {
			new PayChannelService().searchOrderFromChannel(payordno);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(PayChannelService.NOTIFY_SEARCH_CHANNEL_TIME.size()>times){
			timer.schedule(new SearchOrderForChannelNotifyFailThread(payordno,timer,times+1),
				PayChannelService.NOTIFY_SEARCH_CHANNEL_TIME.get(times)*60*1000);
		} else timer.cancel();
	}
}