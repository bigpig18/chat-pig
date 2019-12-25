package com.chat.pig.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.Md5Crypt;

/**
 * 描述: 字符串加密
 *
 * @author liyun
 * @date 2019/12/25
 */
public class MD5Utils {

	/**
	 * 对字符串进行md5加密
	 */
	public static String getMd5Str(String strValue) throws Exception {
		MessageDigest md5 = MessageDigest.getInstance("MD5");
		return Base64.encodeBase64String(md5.digest(strValue.getBytes()));
	}

	public static void main(String[] args) {
		try {
			String md5 = getMd5Str("imooc");
			System.out.println(md5);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
