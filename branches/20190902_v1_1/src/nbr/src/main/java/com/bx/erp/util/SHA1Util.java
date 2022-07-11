package com.bx.erp.util;

import java.security.MessageDigest;

public class SHA1Util {

	public final static String SHA1(String str) {

		if (str == null || str.length() == 0) {
			return null;
		}
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
		try {
			// 获得SHA1摘要算法的 MessageDigest 对象s
			MessageDigest mdTemp = MessageDigest.getInstance("SHA1");
			// 使用指定的字节更新摘要
			mdTemp.update(str.getBytes("UTF-8"));
			// 获得密文
			byte[] md = mdTemp.digest();

			int len = md.length;
			StringBuilder buf = new StringBuilder(len * 2);

			// 把密文转换成十六进制的字符串形式
			for (int i = 0; i < len; i++) {
				byte byte0 = md[i];
				buf.append(hexDigits[byte0 >>> 4 & 0xf]);
				buf.append(hexDigits[byte0 & 0xf]);
			}
			return buf.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
