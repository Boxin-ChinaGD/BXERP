package com.bx.erp.model;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.test.Shared;

public class BaseAuthenticationModelTest {
	
	@BeforeClass
	public void setUp() {
		Shared.printTestClassStartInfo();
	}
	
	@AfterClass
	public void tearDown() {
		Shared.printTestClassEndInfo();
	}
	
	@Test
	public void testCheckCreate() {
		Shared.printTestMethodStartInfo();
		
	}
	
	@Test
	public void testCheckRetrieve1() {
		Shared.printTestMethodStartInfo();
		
	}
	
	@Test
	public void testCheckRetrieveN() {
		Shared.printTestMethodStartInfo();
		
	}
	
	@Test
	public void testCheckUpdate() {
		Shared.printTestMethodStartInfo();
		
	}
	
	@Test
	public void testCheckDelete() {
		Shared.printTestMethodStartInfo();
		
	}
}
