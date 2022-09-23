package com.bx.erp.util;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.action.BaseAction;
import com.bx.erp.test.Shared;

public class SHA1UtilTest {

	@BeforeClass
	public void setup() {
		Shared.printTestClassStartInfo();
	}

	@AfterClass
	public void tearDown() {
		Shared.printTestClassEndInfo();
	}

	@Test
	public void SHA1() {
		Shared.printTestMethodStartInfo();

		String s1 = SHA1Util.SHA1(Shared.PASSWORD_DEFAULT + BaseAction.SHADOW);
		System.out.println("长度=" + s1.length() + "\t加密后=" + s1);

		String s2 = SHA1Util.SHA1("BoXin" + BaseAction.SHADOW);
		System.out.println("长度=" + s2.length() + "\t加密后=" + s2);

		String str = SHA1Util.SHA1(" asd4#@56" + BaseAction.SHADOW);
		System.out.println("长度=" + str.length() + "\t加密后=" + str);

	}
}
