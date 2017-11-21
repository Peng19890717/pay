package com.third.cj;

public enum CjInfoRetcode {
	$0000_处理成功("0000", "处理成功"), //成功
	$1000_报文内容检查错或者处理错("1000", "报文内容检查错或者处理错"), //失败
	$2004_不通过受理("2004", "不通过受理"), //失败
	$2009_无此交易("2009", "无此交易"), //失败
	$2000_系统正在对数据处理("2000", "系统正在对数据处理"), //处理中

	;

	//------------------------------------------------------

	private String code;
	private String name;

	private CjInfoRetcode(String code, String name) {
		this.code = code;
		this.name = name;
	}

	public static CjInfoRetcode findByCode(String code) {
		if (code == null)
			return null;

		for (CjInfoRetcode item : CjInfoRetcode.values()) {
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
