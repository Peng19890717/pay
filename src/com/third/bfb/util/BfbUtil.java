package com.third.bfb.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;


public class BfbUtil {

	public static void getChkFile(String url, String filePath) {
		try {
			File file = new File(filePath);
			if (!file.exists()) {
				file.createNewFile();
			}
			BufferedWriter bw = new BufferedWriter(new FileWriter(file));
			String res =sendAndRecv(url,"","GBK");
			if(!StringUtils.isBlank(res))
				bw.write(res);
			
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static String sendAndRecv(String url, String buf, String charType)
			throws IOException {

		String[] resArr = StringUtils.split(buf, "&");

		Map reqMap = new HashMap();
		for (int i = 0; i < resArr.length; i++) {
			String data = resArr[i];
			int index = StringUtils.indexOf(data, '=');
			String nm = StringUtils.substring(data, 0, index);
			String val = StringUtils.substring(data, index + 1);
			reqMap.put(nm, val);
		}

		SimpleHttpsClient httpsClient = new SimpleHttpsClient();
		HttpSendResult res = httpsClient.postRequest(url, reqMap, 120000,
				charType);

		String repMsg = res.getResponseBody();

		return repMsg;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		getChkFile("http://10.211.1.134:38080/mweb/service/merrecon.xhtml?MERC=800010000010003800010000010003_20141111092325_pay.txt6f4c97a44456143f5bfd31fe77e05227","D:/chkfile.txt");

	}

}
