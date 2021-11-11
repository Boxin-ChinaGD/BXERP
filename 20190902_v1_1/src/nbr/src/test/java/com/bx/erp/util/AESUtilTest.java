package com.bx.erp.util;

import org.apache.commons.codec.binary.Base64;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.bx.erp.test.BaseCompanyTest;

import net.sf.json.JSONObject;

public class AESUtilTest {
	@Test
	public void testAESUtil() {
		AESUtil aes = new AESUtil();
		String pass = aes.encrypt(BaseCompanyTest.DBUserPassword_Default);
		String user = aes.encrypt(BaseCompanyTest.DBUserName_Default);
		
		Assert.assertNotNull(pass);
		Assert.assertNotNull(user);
		
		
		Assert.assertNotNull(aes.decrypt(pass));
		Assert.assertNotNull(aes.decrypt(user));
		
		String appid = aes.encrypt("wxfbf3ccb2f339d42d");
		
		Assert.assertNotNull(appid);
		
		
		Assert.assertNotNull(aes.decrypt(appid));
		
	}
	
	@Test
	public void testGetphone() {
		String encryptedData = "eb69XzBi1bnYIM6iASK7wXknvqSW2V1y6KXXTy3IPBYPoysQ6vdnPkJGzU5YTkZcZzSnSTZ9dyfH4mFuQOfNyovmKDphXufvjjXAv2UWunjkhESs922vPWFTx5fAvBu/KnTRQmXfQQwVF788z4bNSsZ50n1K+PLA1/nYUGz8ftoWTFXexbvVUt7zMR+2cyJU54rf5NUtd6lIEHzx9p2bcA==";
		String ivData = "4WlEK7iPh67W8G7RDgxIaw==";
		String session_key = "owwHxJPIU+JnNcHQwEBbGw==";
		byte[] encrypted = Base64.decodeBase64(encryptedData);
		byte[] iv = Base64.decodeBase64(ivData);
		byte[] key = Base64.decodeBase64(session_key);
		//
		AESUtil aesUtil = new AESUtil(key, iv);
		String result = aesUtil.decrypt(encrypted);
		JSONObject Result = JSONObject.fromObject(result);
		String mobile = Result.getString("phoneNumber");
		if (mobile == null || "".equals(mobile) || mobile.length() != 11) {
			System.out.println("解密手机号失败!");
		} else {
			System.out.println("解密手机号成功!" + mobile);
		}
	}

}
