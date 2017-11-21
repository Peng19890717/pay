package com.third.jingdong.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

public class Context {

	private static Properties props = new Properties();
	static{
		InputStream in = ClassLoader.getSystemResourceAsStream("context.properties");
		try {
			props.load(new BufferedReader(new InputStreamReader(in,"utf-8")));
 		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	public static String getValue(String key){
		return props.getProperty(key);
	}
}
