package com.third.emaxPlus;

import java.io.Serializable;

/**
 * description
 * Created by lekaijun
 * Date 2017/3/8
 */
public class EmaxPlusCommonResponse implements Serializable {

    private static final long serialVersionUID = 7388424705536894380L;

    /**
     * 取值为：SUCCESS/FAIL
     */
    private String resultStatus;

    /**
     * 000000-成功
     */
    private String resultCode;

    /**
     *返回码信息提示
     */
    private String resultMsg;

    /**
     *预留字段。处理完成后，接口执行响应的系统时间
     */
    private String resultTime;
    
    /**
     *在线支付，溢+返回的form表单
     */
    private String formTxt;

    public String getResultStatus() {
        return resultStatus;
    }

    public void setResultStatus(String resultStatus) {
        this.resultStatus = resultStatus;
    }

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getResultMsg() {
        return resultMsg;
    }

    public void setResultMsg(String resultMsg) {
        this.resultMsg = resultMsg;
    }

    public String getResultTime() {
        return resultTime;
    }

    public void setResultTime(String resultTime) {
        this.resultTime = resultTime;
    }

	public String getFormTxt() {
		return formTxt;
	}

	public void setFormTxt(String formTxt) {
		this.formTxt = formTxt;
	}
}
