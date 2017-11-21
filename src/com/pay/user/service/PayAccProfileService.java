package com.pay.user.service;

import com.pay.user.dao.PayAccProfileDao;

public class PayAccProfileService {
	
	public static void ForzePayAccProfilebyUserId(String userId,String type){
		
		PayAccProfileDao dao=new PayAccProfileDao();
		dao.FrozePayAccProfile(userId,type);
		
	}

}
