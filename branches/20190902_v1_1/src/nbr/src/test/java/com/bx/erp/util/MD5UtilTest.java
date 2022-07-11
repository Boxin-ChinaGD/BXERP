package com.bx.erp.util;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.action.BaseAction;
import com.bx.erp.test.Shared;

public class MD5UtilTest {

	@BeforeClass
	public void setup() {
		Shared.printTestClassStartInfo();
	}

	@AfterClass
	public void tearDown() {
		Shared.printTestClassEndInfo();
	}

	@Test
	public void MD5() {
		Shared.printTestMethodStartInfo();

		String s1 = MD5Util.MD5(Shared.PASSWORD_DEFAULT + BaseAction.SHADOW);
		System.out.println("长度=" + s1.length() + "\t加密后=" + s1);

		s1 = MD5Util.MD5("BoXin" + BaseAction.SHADOW);
		System.out.println("长度=" + s1.length() + "\t加密后=" + s1);

		String str = MD5Util.MD5(" asd4#@56" + BaseAction.SHADOW);
		System.out.println("长度=" + str.length() + "\t加密后=" + str);
	}
}
