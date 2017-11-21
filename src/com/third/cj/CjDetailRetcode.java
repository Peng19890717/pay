package com.third.cj;

public enum CjDetailRetcode {

	$0000_处理成功("0000", "处理成功"), //成功
	$0001_代理系统受理成功("0001", "代理系统受理成功"), //处理中
	$0002_提交银行成功等待查询结果("0002", "提交银行成功，等待查询结果"), //处理中
	$2013_收款行未开通业务("2013", "收款行未开通业务"), //失败
	$3999_其他错误("3999", "其他错误"), //失败

	;

	//------------------------------------------------------

	private String code;
	private String name;

	private CjDetailRetcode(String code, String name) {
		this.code = code;
		this.name = name;
	}

	public static CjDetailRetcode findByCode(String code) {
		if (code == null)
			return null;

		for (CjDetailRetcode item : CjDetailRetcode.values()) {
			if (item.getCode().equals(code)) {
				return item;
			}
		}
		return null;
	}//method

	public String getCode() {
		return this.code;
	}

	public String getName() {
		return this.name;
	}

}//class
