package com.third.wlt;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.servlet.http.HttpServletRequest;

public class RequestUtil {

	/**
	 * 获取post方式的body中json、xml参数
	 * 
	 * @Title: RequestUtil.java
	 * @version V1.0
	 */
	public static String getRequestBody(HttpServletRequest request) {
		String json = null;
		InputStream is = null;
		BufferedReader br = null;
		InputStreamReader isr = null;
		try {
			is = request.getInputStream();
			StringBuilder messageBuffer = new StringBuilder();
			isr = new InputStreamReader(is, "utf-8");
			br = new BufferedReader(isr);
			String line = null;
			while ((line = br.readLine()) != null) {
				messageBuffer.append(line);
			}
			json = messageBuffer.toString();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (is != null) {
				try {
					is.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return json;
	}

	/**
	 * 获取请求IP
	 * 
	 * @param request
	 * @return
	 */
	public static String getRemortIP(HttpServletRequest request) {
		if (request.getHeader("x-forwarded-for") == null) {
			return request.getRemoteAddr();
		}
		return request.getHeader("x-forwarded-for");
	}
}
