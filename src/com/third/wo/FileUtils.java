package com.third.wo;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;

public class FileUtils {
	public static ByteArrayOutputStream readFile(String path) throws Exception{
		FileInputStream fis = new FileInputStream(new File(path));
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] bb = new byte[4096];
		int len;
		while((len=fis.read(bb)) != -1){
			baos.write(bb, 0, len);
		}
		fis.close();
		return baos;
	}
}
