package com.pay.account.dao;

//此类用于获取科目表中科目编码和名称而单独做出一个类。
public class SimplePayAccSubject {
	
	public String glCode;
	public String glName;
	public String getGlCode() {
		return glCode;
	}
	public void setGlCode(String glCode) {
		this.glCode = glCode;
	}
	public String getGlName() {
		return glName;
	}
	public void setGlName(String glName) {
		this.glName = glName;
	}

}
