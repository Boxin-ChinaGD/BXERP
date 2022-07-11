package com.bx.erp.util;

import static org.testng.Assert.assertTrue;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.HashMap;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.test.Shared;

public class RSAUtilsTest {
	@BeforeClass
	public void setup() {
		Shared.printTestClassStartInfo();
	}

	@AfterClass
	public void tearDown() {
		Shared.printTestClassEndInfo();
	}

	@Test
	public void testRSAEncryption() throws Exception {
		Shared.printTestMethodStartInfo();

		final String pwd = Shared.PASSWORD_DEFAULT;

		HashMap<String, Object> map = RSAUtils.getKeys();
		RSAPublicKey publicKey = (RSAPublicKey) map.get("public");
		RSAPrivateKey privateKey = (RSAPrivateKey) map.get("private");

		String mi = RSAUtils.encryptByPublicKey(pwd, publicKey);
		System.out.println("加密后：" + mi);

		String ming = RSAUtils.decryptByPrivateKey(mi, privateKey);
		System.out.println("解密后：" + ming);

		assertTrue(ming.equals(pwd), "解密失败");
	}
}
