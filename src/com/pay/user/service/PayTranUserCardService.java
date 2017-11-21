package com.pay.user.service;

import java.util.Map;

import util.Tools;

import com.PayConstant;
import com.jweb.service.TransactionService;
import com.pay.cardbin.dao.PayCardBin;
import com.pay.cardbin.service.PayCardBinService;
import com.pay.merchant.dao.PayAccProfile;
import com.pay.merchant.dao.PayAccProfileDAO;
import com.pay.merchant.service.PayAccProfileService;
import com.pay.merchantinterface.service.PayRequest;
import com.pay.order.dao.PayOrder;
import com.pay.user.dao.PayTranUserCard;
import com.pay.user.dao.PayTranUserCardDAO;
import com.pay.user.dao.PayTranUserInfo;
import com.pay.user.dao.PayTranUserInfoDAO;

/**
 * Object PAY_TRAN_USER_CARD service. 
 * @author Administrator
 *
 */
public class PayTranUserCardService extends TransactionService{
    /**
     * 快捷支付保存用户信息
     * @param payRequest
     * @throws Exception 
     */
//    public void addUserAndBindCardNo(PayRequest payRequest) throws Exception{
//    	PayTranUserInfoDAO userDao = new PayTranUserInfoDAO();
//    	PayTranUserCardDAO cardDao = new PayTranUserCardDAO();
//    	PayAccProfileDAO accDAO = new PayAccProfileDAO();
//    	//通过身份证取得用户信息，绑定卡号列表
//    	PayTranUserInfo tranUser = userDao.getTranUserByColumn("USER_ID",payRequest.payerId);
//    	PayTranUserCard bindCard = null;
//    	try {
//			transaction.beignTransaction(userDao,cardDao,accDAO);
//	    	//检查用户是否存在
//	    	if(tranUser != null){//存在，检查该卡号是否已经绑定
//	    		if(payRequest.cardNo.length() >0)bindCard = cardDao.getUserBindCardByCardNo(
//	    				payRequest.payerId,payRequest.cardNo);
//	    	} else {
//	    		//添加用户信息
//	    		tranUser = new PayTranUserInfo();
//	    		//设置用户信息
//	    		tranUser.id=Tools.getUniqueIdentify(); //记录ID
//	    		tranUser.userId=payRequest.payerId.length()==0?payRequest.credentialNo:payRequest.payerId; //用户账户
//	    		tranUser.loginPassword="";//登录密码
//	    		tranUser.passwordStrength=""; //登录密码强度 0:低1:中2:高
//	    		tranUser.loginPwdFailCount=0l;//登录密码输错次数 默认0
//	    		tranUser.payPassword="";//支付密码
//	    		tranUser.payPwdStrength=""; //支付密码强度 0:低1:中2:高
//	    		tranUser.payPwdFailCount=0l;//支付密码输错次数 默认0
//	    		tranUser.befname="";//曾用名 
//	    		tranUser.realName=payRequest.userName; //姓名
//	    		tranUser.cretType=payRequest.credentialType; //"证件类型 01身份证,02军官证,03护照,04回乡证,05台胞证,06警官证,07士兵证,99其他 默认01
//	    		tranUser.cretNo=payRequest.credentialNo; //证件号 
//	    		tranUser.email="";//电子邮箱
//	    		tranUser.emailStatus="0";//邮箱状态 0未激活 1激活 默认0 
//	    		tranUser.fax="";//传真
//	    		tranUser.province=""; //省 
//	    		tranUser.city=""; //市 
//	    		tranUser.area=""; //区/县 
//	    		tranUser.address="";//详细地址
//	    		tranUser.zip="";//邮政编码
//	    		tranUser.nationality="";//国籍
//	    		tranUser.job="";//职业
//	    		tranUser.tel="";//固定电话
//	    		tranUser.mobile=payRequest.userMobileNo; //手机号 
//	    		tranUser.birthday=""; //生日
//	    		tranUser.gender=""; //性别 0男 1女 2未知
//	    		tranUser.publicPhoto="";//公安部照片 
//	    		tranUser.credPhotoFront=""; //证件正面照片
//	    		tranUser.credPhotoBack="";//证件反面照片
//	    		tranUser.sendType="0"; //审核提交方式 0：未提交 1：上传2：传真 3：邮寄
//	    		tranUser.userStatus="0"; //认证状态 0：未认证 1：审核中 2：已通过3：未通过 默认0 
//	    		tranUser.remark=""; //备注
//	    		tranUser.revFlag="";//预留字段
//	    		tranUser.mobileStatus="0"; //手机号状态 0未激活 1激活 默认0
//	    		userDao.addPayTranUserInfo(tranUser);
//	    		// 增加商户虚拟帐号
//	        	PayAccProfile payAccProfile = new PayAccProfile();
//	        	payAccProfile.setId(Tools.getUniqueIdentify());
//	        	payAccProfile.setPayAcNo(tranUser.userId);
//	        	payAccProfile.setAcType(PayConstant.CUST_TYPE_USER);
//	        	accDAO.addPayAccProfile(payAccProfile);
//	    	}
//	    	//绑定卡不存在，创建卡信息（CARD_STATUS	绑定银行卡状态 值为2）
//			if(bindCard == null){
//				if(payRequest.cardNo.length()>0){
//					bindCard = new PayTranUserCard();
//					//设置绑定信息信息 
//					bindCard.id=Tools.getUniqueIdentify();//记录ID
//					bindCard.userId=tranUser.userId;//用户ID
//					bindCard.cardBank=payRequest.bankId;//绑定银行卡所属行如：ICBC
//					PayCardBin cardBin = PayCardBinService.getCardBinByCardNo(payRequest.cardNo);
//					//卡bin中，CRDOFF	0借记卡/1贷记卡/2准贷记卡/3预付卡
//					bindCard.cardType=cardBin!=null?cardBin.cardType:"";
//					bindCard.cardStatus="2";//绑定银行卡状态0绑定1解绑2无效默认0
//					bindCard.cardStaRes="";//银行卡非绑定状态是可填写状态变更原因
//					bindCard.cardNo=payRequest.cardNo;//绑定银行卡卡号
//					bindCard.cardBankBranch=payRequest.bankName;//卡号所属支行
//					cardDao.addPayTranUserCard(bindCard);
//				}
//			} else {
//				//未绑定成功的卡更新信息
//				if(!"0".equals(bindCard.cardStatus)){
//					bindCard.cardBank=payRequest.bankId;//绑定银行卡所属行如：ICBC
//					bindCard.cardNo=payRequest.cardNo;//绑定银行卡卡号
//					bindCard.cardBankBranch=payRequest.bankName;//卡号所属支行
//					cardDao.updatePayTranUserCardInfoForXF(bindCard);
//					tranUser.realName=payRequest.userName; //姓名
//		    		tranUser.cretType=payRequest.credentialType; //"证件类型 01身份证,02军官证,03护照,04回乡证,05台胞证,06警官证,07士兵证,99其他 默认01
//		    		tranUser.cretNo=payRequest.credentialNo; //证件号 
//		    		tranUser.mobile=payRequest.userMobileNo; //手机号 
//					userDao.updatePayTranUserInfoForXF(tranUser);
//				}
//			}
//			if(bindCard!=null)tranUser.bindCardMap.put(bindCard.cardNo, bindCard);
//	    	//保存用户信息到payRequest
//	    	payRequest.certPayUser = tranUser;
//	    	transaction.endTransaction();
//		} catch (Exception e) {
//			transaction.rollback();
//			throw e;
//		}
//    }
    /**
     * 更新先锋通知认证支付卡号绑定状态
     * @param signParameters
     * @param tmpPayOrder
     */
	public void xfNotifyBindCard(Map<String, String> signParameters,PayOrder tmpPayOrder) {
		PayTranUserCardDAO cardDao = new PayTranUserCardDAO();
    	try {
    		PayTranUserCard bindCard = new PayTranUserCard();
    		bindCard.userId = tmpPayOrder.custId;
    		bindCard.cardNo = signParameters.get("bankCardNo");//用户绑定卡号
    		bindCard.cardBank = signParameters.get("bankId");//银行编码
    		bindCard.cardBankBranch = signParameters.get("bankName");//银行名称
    		if(cardDao.updatePayTranUserCardForXF(bindCard)==0){
    			bindCard.id = Tools.getUniqueIdentify();
    			cardDao.addPayTranUserCard(bindCard);
    			PayCardBin cardBin = PayCardBinService.getCardBinByCardNo(bindCard.cardNo);
				//卡bin中，CRDOFF	0借记卡/1贷记卡/2准贷记卡/3预付卡
    			bindCard.cardType=cardBin!=null?cardBin.cardType:"";
    			bindCard.cardStatus="0";//绑定银行卡状态0绑定1解绑2无效默认0
    			bindCard.cardStaRes="";
    		}
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
}