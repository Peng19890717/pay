package com.third.bfb.util;

import java.io.Serializable;

/**
 * @Project 
 * @Description Get/Post
 * @Company 
 * @Create 2013-8-5
 * @author ren_zhl
 */
public class HttpSendResult implements Serializable {
	private static final long serialVersionUID = 3612208038316088287L;

	/**
	 * 
	 */
	private int status = -1;

	/**
	 * 
	 */
	private String responseBody;

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getResponseBody() {
		return responseBody;
	}

	public void setResponseBody(String responseBody) {
		this.responseBody = responseBody;
	}
}
