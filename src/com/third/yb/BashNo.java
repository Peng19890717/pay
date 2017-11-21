package com.third.yb;

import java.text.SimpleDateFormat;
import java.util.Date;

public class BashNo extends Thread{

	private static int seq = 0;  
	private static final int ROTATION = 99999;  
	
	
	
	@Override
	public void run() {
		for(int i=0;i<100;i++){
			System.out.println(this.getName());
			next();
		}
	}

	public static synchronized String next(){
		String date = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
		if(seq > ROTATION){
			seq = 0;
		}
		String tmp = date + String.format("%05d", seq++);
		System.out.println( tmp);
		return tmp.substring(tmp.length()-15, tmp.length());
	}
	
	public static void main(String[] args) {
		BashNo bashNo1 = new BashNo();
		BashNo bashNo2 = new BashNo();
		BashNo bashNo3 = new BashNo();
		
		bashNo1.setName("--1--");
		bashNo2.setName("--2--");
		bashNo3.setName("--3--");
		
		bashNo1.start();
		bashNo2.start();
		bashNo3.start();
	}
}
