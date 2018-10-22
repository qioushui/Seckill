package cn.hfbin.seckill.util;

import org.apache.commons.codec.digest.DigestUtils;
//加密的工具类，比较密文的工具类
public class MD5Util {
	
	public static String md5(String src) {
		return DigestUtils.md5Hex(src);
	}
	
	private static final String salt = "9d5b364d";
	
	public static String inputPassToFormPass(String inputPass) {
		//默认的加盐
		String str = ""+salt.charAt(0)+salt.charAt(2) + inputPass +salt.charAt(5) + salt.charAt(4);
		return md5(str);
	}
	
	public static String formPassToDBPass(String formPass, String salt) {
		//自定义的规则
		String str = ""+salt.charAt(0)+salt.charAt(2) + formPass +salt.charAt(5) + salt.charAt(4);
		return md5(str);
	}
	
	public static String inputPassToDbPass(String inputPass, String saltDB) {
		
		String formPass = inputPassToFormPass(inputPass);
		//自定义盐
		String dbPass = formPassToDBPass(formPass, saltDB);
		return dbPass;
	}
	
	public static void main(String[] args) {
		System.out.println(inputPassToDbPass("12345678", "9d5b364d"));//cd235d8b395725d4c3352e9689f346b6
	} 
	
}
