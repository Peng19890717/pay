package com.third.bfb.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
/**
 * <p>版权所有: 版权所有 (C) 2014-2024</p >
 * <p>公    司: 帮付宝支付科技有限公司</p >
 *
 * FileUtils.java
 *
 * 创建时间: 2016年10月25日 下午4:54:24
 *
 * 作者: 魏军宏
 *
 * 功能描述:文件工具类
 *
 * 版本：1.0.0
 */
public class FileUtils {
	

	/**
     * 将文件转换成字节数组
     * 
     * @param filePath
     * @return byte[]
     * @throws IOException
     */
	public static byte[] getContent(String filePath) throws IOException {
		File file = new File(filePath);
		long fileSize = file.length();
		if (fileSize > Integer.MAX_VALUE) {
			return null;
		}
		FileInputStream fi = new FileInputStream(file);
		byte[] buffer = new byte[(int) fileSize];
		int offset = 0;
		int numRead = 0;
		while (offset < buffer.length
				&& (numRead = fi.read(buffer, offset, buffer.length - offset)) >= 0) {
			offset += numRead;
		}
		fi.close();
		// 确保所有数据均被读取
		if (offset != buffer.length) {
			throw new IOException("无法完全读取文件" + file.getName());
		}
		return buffer;
	}
	
	
	/**
	 * 处理大文件时,提升性能 
	 * 
	 * @param filename
	 * @return byte[]
	 * @throws IOException
	 */
	public static byte[] toByteArray3(String filename) throws IOException {  
        FileChannel fc = null;  
        try {  
            fc = new RandomAccessFile(filename, "r").getChannel();  
            MappedByteBuffer byteBuffer = fc.map(MapMode.READ_ONLY, 0,  
                    fc.size()).load();  
            System.out.println(byteBuffer.isLoaded());  
            byte[] result = new byte[(int) fc.size()];  
            if (byteBuffer.remaining() > 0) {  
                byteBuffer.get(result, 0, byteBuffer.remaining());  
            }  
            return result;  
        } catch (IOException e) {  
            e.printStackTrace();  
            throw e;  
        } finally {  
            try {  
                fc.close();  
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
        }  
    }  
	
	/**
	 *将图片转换成字节数组并且Base64加密
	 *@param filePath
	 *@return String
	 */
	public static String getBase64ImageStr(String filePath){
		String imgUrl = "";
		try {
			byte[] content = FileUtils.getContent(filePath);
			byte[] image = SecureUtil.base64Encode(content);
			imgUrl = new String(image);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return imgUrl;
	}
	
	/**
	 * 测试用获取本地磁盘下的所有图片
	 * @param path
	 * @return File[]
	 */
	public static File[] getFiles(String path){
		File file = new File(path);
	    File[] tempList = file.listFiles();
	    return tempList;
	}
}

