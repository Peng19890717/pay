package com.third.gfb;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.X509Certificate;


public class SignDemo {

    public static void main(String[] args) {
        String resourceDir=SignDemo.class.getResource("./").getPath();
        System.out.println("资源目录="+resourceDir);
        String filePath = resourceDir+"cert/0000054444.pfx";
        String password = "";

        //获取商户私钥
        PrivateKey priKey = MerchantPfxAnalyser.getPrivateKey(filePath, password, null);
        String priKeyStr= MerchantPfxAnalyser.encodeBase64(priKey);
        System.out.println("商户私钥=" + priKeyStr);

        //获取商户公钥
        PublicKey pubKey = MerchantPfxAnalyser.getPublicKey(filePath, password, null);
        String pubKeyStr= MerchantPfxAnalyser.encodeBase64(pubKey);
        System.out.println("商户公钥=" +pubKeyStr );

        //显示商户PFX文件公钥属性
        StringBuilder certSignBuf=new StringBuilder();
        X509Certificate x509Cert = MerchantPfxAnalyser.getX509Certificate(filePath, password,certSignBuf);
        System.out.println("显示商户PFX文件公钥属性");
        System.out.println(MerchantPfxAnalyser.view(x509Cert,certSignBuf.toString()));

        //从cer文件获取国付宝公钥
        String gopayCerfilePath =  resourceDir+"cert/gopay.cer";
        String gopayPubKeyStr= GopayCerAnalyser.getPublicKey(gopayCerfilePath);
        System.out.println("国付宝公钥=" +gopayPubKeyStr );

        //报文
        String message="version=[2.2]tranCode=[8888]merchantID=[0000002739]merOrderNum=[B_5274_0]tranAmt=[5]feeAmt=[0.01]tranDateTime=[20170204161141]frontMerUrl=[http://demo.gopay.com.cn/webtest/result/webclient/front/recv.do]backgroundMerUrl=[http://demo.gopay.com.cn/webtest/result/webclient/background.do]orderId=[2017020404020511]gopayOutOrderId=[2017020404020511]tranIP=[223.203.208.101]respCode=[0000]gopayServerTime=[]VerficationCode=[123456789]";

        //使用商户私钥对上行报文签名
        String signValue= SignVerifyUtil.sign(message,priKeyStr);
        System.out.println("使用商户私钥对上行报文签名，signValue=" + signValue);
        
        //使用国付宝公钥对下行报文验签
        boolean verifyResult2= SignVerifyUtil.verify(message,signValue,gopayPubKeyStr);
        System.out.println("使用国付宝公钥对下行报文验签，验签结果=" + verifyResult2);


        //使用商户公钥对上行报文验签（生产环境这一步应由国付宝网关系统使用商户公钥来验签执行，商户系统可忽略）
        boolean verifyResult= SignVerifyUtil.verify(message,signValue,pubKeyStr);
        System.out.println("使用商户公钥对上行报文验签（生产环境这一步应由国付宝网关系统使用商户公钥来验签执行，商户系统可忽略），验签结果=" + verifyResult);

        
    }


}
