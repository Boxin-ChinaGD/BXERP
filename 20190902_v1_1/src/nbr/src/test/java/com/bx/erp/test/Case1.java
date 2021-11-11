package com.bx.erp.test;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class Case1 { // extends BaseTransactionalTestNGSpringContextTest {

	@DataProvider
	public Object[][] testData1() {
		return new Object[][] { { 1, 2, 3 }, { 1, 3, 4 }, { 1, 3, 4 }, { -1, 3, 2 } };
	}

	@DataProvider
	public Object[][] testData2() {
		return new Object[][] { { 5, 2, 3 }, { 1, 2, -1 }, { 1, -3, 4 }, { 6, 3, 3 } };
	}

	public static int add(int a, int b) {
		return a + b;
	}

	public static int minus(int a, int b) {
		return a - b;
	}

	@BeforeClass
	public void beforeClass() {
		Shared.printTestClassStartInfo();

		System.out.println("This is Before Class");
	}

	@AfterClass
	public void tearDown() {
		Shared.printTestClassEndInfo();
	}

	@Test(groups = { "add" }, dataProvider = "testData1")
	public void addTest(int a, int b, int c) {
		Shared.printTestMethodStartInfo();

		System.out.println("This is test add method.  " + a + " + " + b + " = " + c);
		Assert.assertEquals(add(a, b), c);
	}

	@Test(groups = { "minus" }, dataProvider = "testData2")
	public void minusTest(int a, int b, int c) {
		Shared.printTestMethodStartInfo();

		System.out.println("This is test minus method.  " + a + " - " + b + " = " + c);
		Assert.assertEquals(minus(a, b), c);
	}

	@Test(expectedExceptions = IllegalArgumentException.class, expectedExceptionsMessageRegExp = "NullPoint")
	public void testException() {
		Shared.printTestMethodStartInfo();

		throw new IllegalArgumentException("NullPoint");
	}

	@Test(groups = { "systemtest" })
	public void testLogin() {
		Shared.printTestMethodStartInfo();

		System.out.println("this is test login");
	}

	@Test(groups = { "functiontest" })
	public void testOpenPage() {
		Shared.printTestMethodStartInfo();

		System.out.println("this is test Open Page");
	}

	@DataProvider(name = "user")
	public Object[][] Users() {
		return new Object[][] { { "root", "passowrd" }, { "cnblogs.com", "tankxiao" }, { "tank", "xiao" } };
	}

	@Test(dataProvider = "user")
	public void verifyUser(String userName, String password) {
		Shared.printTestMethodStartInfo();

		System.out.println("Username: " + userName + " Password: " + password);
	}

	@Test
	public void setupEnv() {
		Shared.printTestMethodStartInfo();

		System.out.println("this is setup Env");
	}

	@Test(dependsOnMethods = { "setupEnv" })
	public void testMessage() {
		Shared.printTestMethodStartInfo();

		System.out.println("this is test message");
	}

	@Test(enabled = false)
	public void testIgnore() {
		Shared.printTestMethodStartInfo();

		System.out.println("This test case will ignore");
	}

	@BeforeClass
	public void beforeMethod() {
		System.out.println("This is Before Method");
	}

	@AfterMethod
	public void afterMethod() {
		System.out.println("This is After Method");
	}

	@AfterClass
	public void afterClass() {
		System.out.println("This is After Class");
	}
}