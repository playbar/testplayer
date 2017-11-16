package com.mojing.vrplayer.publicc;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Util {
	private static final char HEX_DIGITS[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
        'a', 'b', 'c', 'd', 'e', 'f' };
	
	public final static String MD5(String s) {
		try {
			byte[] btInput = s.getBytes();
			MessageDigest mdInst = MessageDigest.getInstance("MD5");
			mdInst.update(btInput);
			byte[] md = mdInst.digest();
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < md.length; i++) {
				int val = ((int) md[i]) & 0xff;
				if (val < 16)
					sb.append("0");
				sb.append(Integer.toHexString(val));

			}
			return sb.toString();
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * 使用MD5算法对传入的key进行加密并返回。
	 */
	public final static String hashKeyForDisk(String key) {
		String cacheKey;
		try {
			final MessageDigest mDigest = MessageDigest.getInstance("MD5");
			mDigest.update(key.getBytes());
			cacheKey = bytesToHexString(mDigest.digest());
		} catch (NoSuchAlgorithmException e) {
			cacheKey = String.valueOf(key.hashCode());
		}
		return cacheKey;
	}
	
	private static String bytesToHexString(byte[] bytes) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < bytes.length; i++) {
			String hex = Integer.toHexString(0xFF & bytes[i]);
			if (hex.length() == 1) {
				sb.append('0');
			}
			sb.append(hex);
		}
		return sb.toString();
	}
	
	/**
	 * 字节转二进制字符串
	 * @param b
	 * @return
	 */
	public static String toHexString(byte[] b) {  
		 StringBuilder sb = new StringBuilder(b.length * 2);  
		 for (int i = 0; i < b.length; i++) {  
		     sb.append(HEX_DIGITS[(b[i] & 0xf0) >>> 4]);  
		     sb.append(HEX_DIGITS[b[i] & 0x0f]);  
		 }  
		 return sb.toString();  
	}

	/**
	 * 获取文件md5值
	 * @param filePath	文件路径
	 * @return
	 */
	public static String md5FileSum(String filePath) {
		InputStream fis;
		byte[] buffer = new byte[1024];
		int numRead = 0;
		MessageDigest md5;
		try{
			fis = new FileInputStream(filePath);
			md5 = MessageDigest.getInstance("MD5");
			while((numRead=fis.read(buffer)) > 0) {
				md5.update(buffer,0,numRead);
			}
			fis.close();
			return toHexString(md5.digest());	
		} catch (Exception e) {
			return "";
		}
	}

	public final static String getMessageDigest(byte[] buffer) {
		try {
			MessageDigest mdTemp = MessageDigest.getInstance("MD5");
			mdTemp.update(buffer);
			byte[] md = mdTemp.digest();
			int j = md.length;
			char str[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				str[k++] = HEX_DIGITS[byte0 >>> 4 & 0xf];
				str[k++] = HEX_DIGITS[byte0 & 0xf];
			}
			return new String(str);
		} catch (Exception e) {
			return "";
		}
	}


	/**
	 * 获取文件部分内容md5值
	 * @param filePath	文件路径
	 * @param size 取的大小 单位MB
	 * @return
	 */
	public static String md5FileSum(String filePath,int size) {
		InputStream fis;
		if(size<=0)
			return "";
		File file = new File(filePath);
		if(file==null||!file.exists())
			return "";
		long begin = System.currentTimeMillis();
		long lsize = size*1024*1024; //转成字节
		if(file.length()<lsize){
			lsize= file.length();
		}

		byte[] buffer = new byte[1024];
		int numRead = 0;
		long readLen = 0L;
		MessageDigest md5;
		try{
			fis = new FileInputStream(filePath);
			md5 = MessageDigest.getInstance("MD5");
			while((readLen) <=lsize) {
				numRead=fis.read(buffer);
				readLen+=numRead;
				if(numRead<=0) {
					break;
				}
				md5.update(buffer,0,numRead);
			}
			fis.close();

			String md5s = toHexString(md5.digest());
			long endtime = System.currentTimeMillis();
			return md5s;
		} catch (Exception e) {
			return null;
		}
	}
}