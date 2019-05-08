package com.demo.api.commons.utils;

import java.security.SecureRandom;

/**
 * @Description: 字符串处理工具类
 */
public class RandomCodeUtil {
	private static SecureRandom secureRandom;
	private static final String baseNumber = "012340123456789560123456789789012345678901234567891234567890347";
	private static final String baseString = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLOMNOPQRSTUVWXYZ1234567890";
	static {
		try{
			secureRandom = SecureRandom.getInstance("SHA1PRNG");
		}catch(Exception e){
			secureRandom = new SecureRandom();
		}
	}
	/**
	 * 字符串首字母小写
	 * @param str
	 * @return
	 */
	public static String toLowerCaseFirstOne(String str) {
		if (str == null || "".equals(str))
			return str;
		if (Character.isLowerCase(str.charAt(0)))
			return str;
		else
			return (new StringBuilder()).append(Character.toLowerCase(str.charAt(0))).append(str.substring(1))
					.toString();
	}

	public static String random(int length,String base){
		secureRandom.setSeed(System.currentTimeMillis());
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length; i++) {
			int number = secureRandom.nextInt(base.length());
			sb.append(base.charAt(number));
		}
		return sb.toString();
	}

	public static String randomNumber(int length){
		return random(length,baseNumber);
	}

	public static String randomString(int length){
		return random(length,baseString);
	}

}
