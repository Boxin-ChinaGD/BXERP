package com.bx.erp.model;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.model.warehousing.Warehouse;
import com.bx.erp.test.Shared;

public class WarehouseTest {
	@BeforeClass
	public void setup() {
		Shared.printTestClassStartInfo();
	}

	@AfterClass
	public void tearDown() {
		Shared.printTestClassEndInfo();
	}
	
	@Test
	public void checkUpdate() {
		Shared.printTestMethodStartInfo();

		Warehouse w = new Warehouse();
		w.setID(1);
		w.setStaffID(1);
		w.setName("testUpdate");
		w.setAddress("testAddress");
		// 检查仓库名称,其余已在FieldFormatTest.checkName()已经作了充分测试
		w.setName("");
		Assert.assertTrue(w.checkCreate(-1).length() > 0);
		w.setName(" ");
		Assert.assertTrue(w.checkCreate(-1).length() > 0);
		w.setName("   ");
		Assert.assertTrue(w.checkCreate(-1).length() > 0);
		w.setName("239fwho");
		Assert.assertTrue(w.checkCreate(-1).length() == 0);
		
		// 检查仓库地址,其余已在FieldFormatTest.checkAddress()已经作了充分测试
		w.setAddress("");
		Assert.assertTrue(w.checkCreate(-1).length() == 0);
		w.setAddress(" ");
		Assert.assertTrue(w.checkCreate(-1).length() > 0);
		w.setAddress("   ");
		Assert.assertTrue(w.checkCreate(-1).length() > 0);
		w.setAddress("239fwho");
		Assert.assertTrue(w.checkCreate(-1).length() == 0);
	}
	
	@Test
	public void checkCreate() {
		Shared.printTestMethodStartInfo();

		Warehouse w = new Warehouse();
		w.setID(1);
		w.setStaffID(1);
		w.setName("testUpdate");
		w.setAddress("testAddress");
		// 检查仓库名称,其余已在FieldFormatTest.checkName()已经作了充分测试
		w.setName("");
		Assert.assertTrue(w.checkCreate(-1).length() > 0);
		w.setName(" ");
		Assert.assertTrue(w.checkCreate(-1).length() > 0);
		w.setName("   ");
		Assert.assertTrue(w.checkCreate(-1).length() > 0);
		w.setName("239fwho");
		Assert.assertTrue(w.checkCreate(-1).length() == 0);
		
		// 检查仓库地址,其余已在FieldFormatTest.checkAddress()已经作了充分测试
		w.setAddress("");
		Assert.assertTrue(w.checkCreate(-1).length() == 0);
		w.setAddress(" ");
		Assert.assertTrue(w.checkCreate(-1).length() > 0);
		w.setAddress("   ");
		Assert.assertTrue(w.checkCreate(-1).length() > 0);
		w.setAddress("239fwho");
		Assert.assertTrue(w.checkCreate(-1).length() == 0);
	}
}
