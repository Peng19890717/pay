package com.pay.merchantinterface.service;

import java.util.List;

import com.pay.order.dao.PayReceiveAndPay;

public class ReceivePaySingleTest extends Thread{
	PayRequest payRequest;
	List<PayReceiveAndPay> notifyList;
	public ReceivePaySingleTest(PayRequest payRequest,List<PayReceiveAndPay> notifyList){
		this.payRequest=payRequest;
		this.notifyList=notifyList;
	}
	public void run(){
		try {
			new PayInterfaceOtherService().receivePayNotify(payRequest,notifyList);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
