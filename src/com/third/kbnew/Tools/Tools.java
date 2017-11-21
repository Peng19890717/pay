package com.third.kbnew.Tools;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.ProxySelector;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.AllowAllHostnameVerifier;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.ProxySelectorRoutePlanner;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;


/**
 * @author sunjie
 */
@SuppressWarnings("deprecation")
public class Tools {

    private static String EQUALS = "equals";
    private static String EQUALSIGNORECASE = "equalsIgnoreCase";
    private static String ENDSWITH = "endsWith";
    private static String STARTSWITH = "startsWith";
    private static String CONTAINS = "contains";
    private HashMap<String, String> header = new HashMap<String, String>();

    public String post(String url, String request) {
        String returnString = "";
        DefaultHttpClient client = new DefaultHttpClient();
        ProxySelectorRoutePlanner routePlanner = new ProxySelectorRoutePlanner(client.getConnectionManager().getSchemeRegistry(),ProxySelector.getDefault());
        client.setRoutePlanner(routePlanner);
        client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 60000);
        client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 60000);
        final HttpPost method = new HttpPost(url);
        method.setHeader("Content-Type", "application/x-www-form-urlencoded");
        if (!header.isEmpty()) {
            Set<String> keysSet = header.keySet();
            Iterator<String> iterator = keysSet.iterator();
            while (iterator.hasNext()) {
                String key = iterator.next();
                String value = header.get(key);
                method.addHeader(key, value);
            }
        }
        try {
            if (!request.equals("")) {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                String[] values = request.split("&");
                for (String value : values) {
                    String[] keyValue = value.split("=");
                    NameValuePair pair = null;
                    if (keyValue.length != 2) {
                        pair = new BasicNameValuePair(keyValue[0], "");
                    } else {
                        pair = new BasicNameValuePair(keyValue[0], keyValue[1]);
                    }
                    params.add(pair);
                }
                method.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
            }
            // 加上如下语句,是防止生产环境的https证书的验证
            SSLSocketFactory.getSocketFactory().setHostnameVerifier(
                    new AllowAllHostnameVerifier());
            HttpResponse response = client.execute(method);
            HttpEntity responseEntity = response.getEntity();
            if (responseEntity != null) {
                returnString = EntityUtils.toString(responseEntity);
            }
            EntityUtils.consume(responseEntity);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            client.getConnectionManager().shutdown();
        }
        return (returnString);
    }

    public String sortStringWithSeparator(String[] inputString, String separator) {
        Arrays.sort(inputString);
        StringBuilder outputString = new StringBuilder("");
        for (int i = 0; i < inputString.length; i++) {
            outputString.append(inputString[i]);
            outputString.append(separator);
        }
        String request = outputString.substring(0, outputString.length() - 1);
        return request.toString();
    }
    // 排序
    public String sortMapWithSeparator(Map<String, String> inputMap,String separator) {
        Set<String> set = inputMap.keySet();
        ArrayList<String> list = new ArrayList<String>();
        for (String str : set) {
            String k = str + "=" + inputMap.get(str) + separator;
            list.add(k);
        }
        String[] s = new String[list.size()];
        s = list.toArray(s);
        Arrays.sort(s);
        StringBuilder outputString = new StringBuilder("");
        for (int i = 0; i < s.length; i++) {
            outputString.append(s[i]);
        }
        String request = outputString.substring(0, outputString.length() - 1);
        return request.toString();
    }
    public String removeFromString(String inputString, String orgSeparator,String finalSeparator, String type, String key) {
        String[] arrayString = inputString.split(orgSeparator);
        ArrayList<String> list = new ArrayList<String>();
        for (int i = 0; i < arrayString.length; i++) {
            if (Tools.EQUALS.equalsIgnoreCase(type)) {
                if (!arrayString[i].equals(key)) {
                    list.add(arrayString[i]);
                }
            }
            if (Tools.EQUALSIGNORECASE.equalsIgnoreCase(type)) {
                if (!arrayString[i].equalsIgnoreCase(key)) {
                    list.add(arrayString[i]);
                }
            }
            if (Tools.ENDSWITH.equalsIgnoreCase(type)) {
                if (!arrayString[i].endsWith(key)) {
                    list.add(arrayString[i]);
                }
            }
            if (Tools.STARTSWITH.equalsIgnoreCase(type)) {
                if (!arrayString[i].startsWith(key)) {
                    list.add(arrayString[i]);
                }
            }
            if (Tools.CONTAINS.equalsIgnoreCase(type)) {
                if (!arrayString[i].contains(key)) {
                    list.add(arrayString[i]);
                }
            }
        }
        StringBuilder outputString = new StringBuilder("");
        for (int i = 0; i < list.size(); i++) {
            outputString.append(list.get(i));
            outputString.append(finalSeparator);
        }
        String finalString = outputString.substring(0,
                outputString.length() - 1);
        return finalString;
    }

    public String returnSystemDate() {
        Date date = new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        String systemCurrDate = df.format(date);
        return "" + systemCurrDate;
    }
    public String textEncode(String text, String codeType) throws IOException {
        text = java.net.URLEncoder.encode(text, codeType);
        return text;
    }
    public String textDncode(String text, String codeType) throws IOException {
        text = java.net.URLDecoder.decode(text, codeType);
        return text;
    }
    // 报文返回结果decode
    public String getDecode(String key) throws IOException {
        byte[] s = Base64.decodeBase64((key.getBytes("utf-8")));
        return new String(s);
    }
    // 进行encode
    public String encode(String str) throws UnsupportedEncodingException {
        return URLEncoder.encode(str, "UTF-8");
    }
    /***
     * sha256加密
     *
     * @param strSrc
     * @return
     */
    public String sha256Encrypt(String strSrc) {
        MessageDigest md = null;
        String strDes = null;
        byte[] bt = strSrc.getBytes();
        try {
            md = MessageDigest.getInstance("SHA-256");
            md.update(bt);
            strDes = bytes2Hex(md.digest()); // to HexString
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
        return strDes;
    }

    public static String bytes2Hex(byte[] bts) {
        String des = "";
        String tmp = null;
        for (int i = 0; i < bts.length; i++) {
            tmp = (Integer.toHexString(bts[i] & 0xFF));
            if (tmp.length() == 1) {
                des += "0";
            }
            des += tmp;
        }
        return des;
    }

    public String quickPayEncrypt(String quickyPayString, String encryptKey) {
        StringBuilder quickPayString = new StringBuilder("");
        RSAUtil rsa = new RSAUtil();
        String headString = quickyPayString.substring(0,
                quickyPayString.lastIndexOf("^") + 1);
        quickPayString.append(headString);
        String str = quickyPayString.substring(
                quickyPayString.lastIndexOf("^") + 1, quickyPayString.length());
        String[] splitQuickPayString = str.split(",");
        for (int i = 0; i < splitQuickPayString.length; i++) {
            if (!splitQuickPayString[i].isEmpty()) {
                if (i == 1 || i == 2 || i == 6 || i == 7 || i == 8 || i == 9) {
                    if (i == 1 || i == 2 || i == 6 || i == 7 || i == 8) {
                        try {
                            quickPayString.append(rsa.encrypt(
                                    splitQuickPayString[i], encryptKey)
                                    + "replaceFlag");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    if (i == 9) {
                        try {
                            quickPayString.append(rsa.encrypt(
                                    splitQuickPayString[i], encryptKey)
                                    + "replaceFlag");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                } else {
                    quickPayString.append(splitQuickPayString[i]
                            + "replaceFlag");
                }
            } else {
                quickPayString.append("replaceFlag");
            }
        }

        int k = splitQuickPayString.length;
        for (int i = k; i < 11; i++) {
            quickPayString.append("replaceFlag");
        }

        return quickPayString.toString();
    }

    public String collectMethodEncrypt(String collectMethod, String encryptKey) {
        StringBuilder collectMethodString = new StringBuilder("");
        collectMethodString.append(collectMethod.substring(0,
                collectMethod.lastIndexOf("^") + 1));
        RSAUtil rsa = new RSAUtil();
        String extend = (String) collectMethod.substring(
                collectMethod.lastIndexOf("^") + 1, collectMethod.length());
        String[] splitExtend = extend.split(",");
        for (int i = 0; i < splitExtend.length; i++) {
            if (!splitExtend[i].isEmpty()) {
                if (i == 1 || i == 2 || i == 7) {
                    if (i == 1 || i == 2) {
                        try {
                            collectMethodString
                                    .append(rsa.encrypt(splitExtend[i],
                                            encryptKey) + "replaceFlag");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    if (i == 7) {
                        collectMethodString.append(splitExtend[i]);
                    }

                } else {
                    collectMethodString.append(splitExtend[i] + "replaceFlag");
                }
            } else {
                collectMethodString.append("replaceFlag");
            }
        }

        return collectMethodString.toString();
    }

    public String tradeListEncrypt(String tradeList, String encryptKey) {
        StringBuilder tradeListString = new StringBuilder("");
        RSAUtil rsa = new RSAUtil();

        if (tradeList.indexOf("$") == -1) {
            String splitByPara[] = tradeList.split("~");
            int tailLenght = 0;
            if (splitByPara.length == 4) {
                tailLenght = splitByPara[2].length() + splitByPara[3].length()
                        + 3;
            } else if (splitByPara.length == 3) {
                tailLenght = splitByPara[2].length() + 3;
            }
            String tailString = tradeList.substring(tradeList.length()
                    - tailLenght, tradeList.length());
            String headString = tradeList.substring(0,
                    tradeList.lastIndexOf("^") + 1);
            tradeListString.append(headString);
            String extend = (String) tradeList.substring(
                    tradeList.lastIndexOf("^") + 1, tradeList.length()
                            - tailLenght);
            String[] splitExtend = extend.split(",");
            for (int i = 0; i < splitExtend.length; i++) {
                if (!splitExtend[i].isEmpty()) {
                    if (i == 1 || i == 2 || i == 7) {
                        if (i == 1 || i == 2) {
                            try {
                                tradeListString.append(rsa.encrypt(
                                        splitExtend[i], encryptKey)
                                        + "replaceFlag");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        if (i == 7) {
                            tradeListString.append(splitExtend[i]);
                        }

                    } else {
                        tradeListString.append(splitExtend[i] + "replaceFlag");
                    }
                } else {
                    tradeListString.append("replaceFlag");
                }
            }
            tradeListString.append(tailString);
        } else {
            String entry[] = tradeList.split("\\$");
            for (int k = 0; k < entry.length; k++) {
                String splitByPara[] = entry[k].split("~");
                int tailLenght = 0;
                if (splitByPara.length == 4) {
                    tailLenght = splitByPara[2].length()
                            + splitByPara[3].length() + 3;
                } else if (splitByPara.length == 3) {
                    tailLenght = splitByPara[2].length() + 3;
                }
                String tailString = entry[k].substring(entry[k].length()
                        - tailLenght, entry[k].length());
                String headString = entry[k].substring(0,
                        entry[k].lastIndexOf("^") + 1);
                tradeListString.append(headString);
                String extend = (String) entry[k].substring(
                        entry[k].lastIndexOf("^") + 1, entry[k].length()
                                - tailLenght);
                String[] splitExtend = extend.split(",");
                for (int i = 0; i < splitExtend.length; i++) {
                    if (!splitExtend[i].isEmpty()) {
                        if (i == 1 || i == 2 || i == 7) {
                            if (i == 1 || i == 2) {
                                try {
                                    tradeListString.append(rsa.encrypt(
                                            splitExtend[i], encryptKey)
                                            + "replaceFlag");
                                } catch (Exception e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                            }
                            if (i == 7) {
                                tradeListString.append(splitExtend[i]);
                            }

                        } else {
                            tradeListString.append(splitExtend[i]
                                    + "replaceFlag");
                        }
                    } else {
                        tradeListString.append("replaceFlag");
                    }
                }
                tradeListString.append(tailString + "$");
            }
        }

        return tradeListString.toString().substring(0,
                tradeListString.length() - 1);
    }

    public String detailListEncrypt(String detailList, String encryptKey) {
        StringBuilder detailListString = new StringBuilder("");
        RSAUtil rsa = new RSAUtil();

        if (detailList.indexOf("|") == -1) {
            String encryptList[] = detailList.split("\\^");
            for (int i = 0; i < encryptList.length; i++) {
                if (i == 1 || i == 2 || i == 3) {
                    if (i == 1 || i == 3) {
                        try {
                            detailListString.append(rsa.encrypt(encryptList[i],
                                    encryptKey) + "replaceFlag");
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                    if (i == 2 && !encryptList[2].isEmpty()) {
                        try {
                            detailListString.append(rsa.encrypt(encryptList[i],
                                    encryptKey) + "replaceFlag");
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    } else if (i == 2 && encryptList[2].isEmpty()) {
                        detailListString.append("replaceFlag");
                    }
                } else {
                    detailListString.append(encryptList[i] + "replaceFlag");
                }
            }
            return detailListString.toString();
        } else {
            String entry[] = detailList.split("\\|");
            System.out.println(entry[0]);
            System.out.println(entry[1]);
            for (int k = 0; k < entry.length; k++) {
                String encryptList[] = entry[k].split("\\^");
                for (int i = 0; i < encryptList.length; i++) {
                    if (i == 1 || i == 2 || i == 3) {
                        if (i == 1 || i == 3) {
                            try {
                                detailListString.append(rsa.encrypt(
                                        encryptList[i], encryptKey)
                                        + "replaceFlag");
                            } catch (Exception e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }
                        if (i == 2 && !encryptList[2].isEmpty()) {
                            try {
                                detailListString.append(rsa.encrypt(
                                        encryptList[i], encryptKey)
                                        + "replaceFlag");
                            } catch (Exception e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        } else if (i == 2 && encryptList[2].isEmpty()) {
                            detailListString.append("replaceFlag");
                        }
                    } else {
                        detailListString.append(encryptList[i] + "replaceFlag");
                    }
                }
                detailListString.append("flagReplace");
            }
            return detailListString.toString().substring(0,
                    detailListString.length() - 11);
        }

    }

    /**
     * Map转换为html字符串
     *
     * @param formData
     * @return
     */
    public String getFormData(Map<String, String> formData) {

        if (formData == null)
            return "";

        StringBuffer sBuffer = new StringBuffer();
        for (String key : formData.keySet()) {
            sBuffer.append("<input type=\"hidden\" name=\"").append(key)
                    .append("\" value=\"").append(formData.get(key))
                    .append("\" />");
        }

        return sBuffer.toString();
    }

    /**
     * 生成HTML表单
     *
     * @param formData
     * @param bankPostUrl
     * @return
     */
    public String getHtmlData(String formData, String bankPostUrl) {
        String method = "POST";
        if (formData == null || formData.isEmpty())
            method = "GET";

        StringBuffer sbBuffer = new StringBuffer();
        sbBuffer.append(
                "<html><head></head><body><form id=\"frmBankID\" name=\"frmBankName\" method=\"")
                .append(method)
                .append("\" action=\"")
                .append(bankPostUrl)
                .append("\">")
                .append(formData)
                .append("</form><script language=\"javascript\">document.getElementById(\"frmBankID\").submit();</script></body></html>");

        return sbBuffer.toString();
    }

    public String base64Decode(String data) {
        byte[] dataByte = data.getBytes();
        return new String(Base64.decodeBase64(dataByte));
    }
    
	public static String calculateSignPlain(Map<String, String> params) {
		Map<String, String> sortedParams = new TreeMap<String, String>(params);
		sortedParams.remove("sign");
		sortedParams.remove("sign_type");
		sortedParams.remove("sign_version");

		StringBuilder sb = new StringBuilder();
		for (Entry<String, String> e : sortedParams.entrySet()) {
			if (StringUtils.isBlank(e.getValue())) {
				continue;
			}
			sb.append(e.getKey()).append("=").append(e.getValue()).append("&");
		}
		sb.deleteCharAt(sb.length() - 1);
		return sb.toString();
	}

    public static void main(String[] args) {
        Tools tool = new Tools();
        System.out
                .println(tool
                        .detailListEncrypt(
                                "batch001^马云飞^123^6217001210023432972^中国建设银行^CCB^上海^上海市^中国建设银行股份有限公司上海分行^0.01^C^DEBIT^",
                                "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDv0rdsn5FYPn0EjsCPqDyIsYRawNWGJDRHJBcdCldodjM5bpve+XYb4Rgm36F6iDjxDbEQbp/HhVPj0XgGlCRKpbluyJJt8ga5qkqIhWoOd/Cma1fCtviMUep21hIlg1ZFcWKgHQoGoNX7xMT8/0bEsldaKdwxOlv3qGxWfqNV5QIDAQAB"));

    }
    
	  /**
     * 把数组所有元素排序，并按照“参数=参数值”的模式用“&”字符拼接成字符串
     *
     * @param params
     *            需要排序并参与字符拼接的参数组
     * @param encode 是否需要urlEncode
     * @return 拼接后字符串
     */
    public static String createLinkString(Map<String, String> params, boolean encode) {
        List<String> keys = new ArrayList<String>(params.keySet());
        Collections.sort(keys);
        String prestr = "";
        String charset = params.get("_input_charset");
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = params.get(key);
            if (encode) {
                try {
                    value =URLEncoder.encode(URLEncoder.encode(value, charset),charset);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
            if (i == keys.size() - 1) {// 拼接时，不包括最后一个&字符
                prestr = prestr + key + "=" + value;
            } else {
                prestr = prestr + key + "=" + value + "&";
            }
            }
        
        return prestr;
    }
    
}
