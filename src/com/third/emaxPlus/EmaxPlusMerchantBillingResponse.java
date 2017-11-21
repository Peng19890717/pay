package com.third.emaxPlus;

/**
 * description
 * Created by peng
 * Date 2017/3/10
 */
public class EmaxPlusMerchantBillingResponse extends EmaxPlusCommonResponse {

    private String path;//对账文件的url地址

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
