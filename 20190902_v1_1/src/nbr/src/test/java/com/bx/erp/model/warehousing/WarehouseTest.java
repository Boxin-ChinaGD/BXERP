package com.bx.erp.model.warehousing;

import static org.testng.Assert.assertEquals;

import org.apache.commons.lang.RandomStringUtils;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.model.warehousing.Warehouse;
import com.bx.erp.test.Shared;
import com.bx.erp.util.FieldFormat;

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
		String err = "";

		System.out.println("------------------------------检查修改时仓库名称-------------------------------");

		// 仓库名字英文或数字或中文的组合
		w.setName("%￥#@！*");// 传入非法参数
		err = w.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Warehouse.FIELD_ERROR_name);
		w.setName("rightName");// 传入合法参数

		// 仓库名字不能有空格
		w.setName("a b");
		err = w.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Warehouse.FIELD_ERROR_name);
		w.setName("rightName");

		// 仓库名字长度不能为0
		w.setName("");
		err = w.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Warehouse.FIELD_ERROR_name);
		w.setName("rightName");

		// 仓库名字长度不能大于32
		w.setName(RandomStringUtils.randomAlphanumeric(50));
		err = w.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Warehouse.FIELD_ERROR_name);
		w.setName("rightName");

		// 仓库名字正常输入
		w.setName("test001仓");
		err = w.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");

		System.out.println("------------------------------检查修改时仓库地址-------------------------------");

		// 仓库地址英文或数字或中文的组合
		w.setAddress("%￥#@！*");
		err = w.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Warehouse.FIELD_ERROR_address);
		w.setAddress("rightAddress");

		// 仓库地址长度不能大于32
		w.setAddress(RandomStringUtils.randomAlphanumeric(50));
		err = w.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Warehouse.FIELD_ERROR_address);
		w.setAddress("rightAddress");

		// 仓库名字正常输入
		w.setAddress("test001 地址");
		err = w.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");
		w.setAddress("rightAddress");

		System.out.println("------------------------------检查修改时staffID-------------------------------");
		w.setStaffID(0);
		err = w.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Warehouse.FIELD_ERROR_staffID);
		//
		w.setStaffID(BaseAction.INVALID_ID);
		err = w.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Warehouse.FIELD_ERROR_staffID);
		//
		w.setStaffID(1);
		err = w.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");

		System.out.println("------------------------------检查修改时手机号码-------------------------------");
		w.setPhone("   ");
		err = w.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Warehouse.FIELD_ERROR_phone);
		//
		w.setPhone("abcdefghijk");
		err = w.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Warehouse.FIELD_ERROR_phone);
		//
		w.setPhone("12ab5678910");
		err = w.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Warehouse.FIELD_ERROR_phone);
		//
		w.setPhone("12测试5678910");
		err = w.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Warehouse.FIELD_ERROR_phone);
		//
		w.setPhone("12!@5678910");
		err = w.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Warehouse.FIELD_ERROR_phone);
		//
		w.setPhone("123456789");
		err = w.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Warehouse.FIELD_ERROR_phone);
		//
		w.setPhone("12345678910");
		err = w.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");
		//
		w.setPhone(null);
		err = w.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");

		System.out.println("------------------------------检查修改时ID-------------------------------");
		w.setID(0);
		err = w.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, FieldFormat.FIELD_ERROR_ID);
		//
		w.setID(BaseAction.INVALID_ID);
		err = w.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, FieldFormat.FIELD_ERROR_ID);
		//
		w.setID(1);
		err = w.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");
	}

	@Test
	public void checkCreate() {
		Shared.printTestMethodStartInfo();

		Warehouse w = new Warehouse();
		w.setStaffID(1);
		w.setName("testUpdate");
		w.setAddress("testAddress");
		String err = "";

		System.out.println("------------------------------检查创建时仓库名称-------------------------------");

		// 仓库名字英文或数字或中文的组合
		w.setName("%￥#@！*");// 传入非法参数
		err = w.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Warehouse.FIELD_ERROR_name);
		w.setName("rightName");// 传入合法参数

		// 仓库名字不能有空格
		w.setName("a b");
		err = w.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Warehouse.FIELD_ERROR_name);
		w.setName("rightName");

		// 仓库名字长度不能为0
		w.setName("");
		err = w.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Warehouse.FIELD_ERROR_name);
		w.setName("rightName");

		// 仓库名字长度不能大于32
		w.setName(RandomStringUtils.randomAlphanumeric(50));
		err = w.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Warehouse.FIELD_ERROR_name);
		w.setName("rightName");

		// 仓库名字正常输入
		w.setName("test001仓");
		err = w.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");

		System.out.println("------------------------------检查创建时仓库地址-------------------------------");

		// 仓库地址英文或数字或中文的组合
		w.setAddress("%￥#@！*");
		err = w.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Warehouse.FIELD_ERROR_address);
		w.setAddress("rightAddress");

		// 仓库地址长度不能大于32
		w.setAddress(RandomStringUtils.randomAlphanumeric(50));
		err = w.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Warehouse.FIELD_ERROR_address);
		w.setAddress("rightAddress");

		// 仓库名字正常输入
		w.setAddress("test001 地址");
		err = w.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");

		System.out.println("------------------------------检查创建时手机号码-------------------------------");
		w.setPhone("   ");
		err = w.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Warehouse.FIELD_ERROR_phone);
		//
		w.setPhone("abcdefghijk");
		err = w.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Warehouse.FIELD_ERROR_phone);
		//
		w.setPhone("12ab5678910");
		err = w.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Warehouse.FIELD_ERROR_phone);
		//
		w.setPhone("12测试5678910");
		err = w.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Warehouse.FIELD_ERROR_phone);
		//
		w.setPhone("12!@5678910");
		err = w.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Warehouse.FIELD_ERROR_phone);
		//
		w.setPhone("123456789");
		err = w.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Warehouse.FIELD_ERROR_phone);
		//
		w.setPhone("12345678910");
		err = w.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");
		//
		w.setPhone(null);
		err = w.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");

		System.out.println("------------------------------检查创建时staffID-------------------------------");
		w.setStaffID(0);
		err = w.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Warehouse.FIELD_ERROR_staffID);
		//
		w.setStaffID(BaseAction.INVALID_ID);
		err = w.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, Warehouse.FIELD_ERROR_staffID);
		//
		w.setStaffID(1);
		err = w.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");
	}

	@Test
	public void checkDelete() {
		Warehouse w = new Warehouse();
		w.setID(0);
		assertEquals(w.checkDelete(BaseBO.INVALID_CASE_ID), FieldFormat.FIELD_ERROR_ID);
		//
		w.setID(BaseAction.INVALID_ID);
		assertEquals(w.checkDelete(BaseBO.INVALID_CASE_ID), FieldFormat.FIELD_ERROR_ID);
		//
		w.setID(1);
		assertEquals(w.checkDelete(BaseBO.INVALID_CASE_ID), "");
	}

	@Test
	public void checkRetrieveN() {
		Warehouse w = new Warehouse();
		
		w.setName(RandomStringUtils.randomAlphanumeric(50));
		assertEquals(w.checkRetrieveN(BaseBO.INVALID_CASE_ID), Warehouse.FIELD_ERROR_name);
		//
		w.setName("");
		assertEquals(w.checkRetrieveN(BaseBO.INVALID_CASE_ID), "");
		//
		w.setName(RandomStringUtils.randomAlphanumeric(10));
		assertEquals(w.checkRetrieveN(BaseBO.INVALID_CASE_ID), "");
		// 检查page indx
		w.setPageIndex(-1);
		w.setPageSize(BaseAction.PAGE_SIZE);
		assertEquals(w.checkRetrieveN(BaseBO.INVALID_CASE_ID), FieldFormat.FIELD_ERROR_Paging);
		//
		w.setPageIndex(BaseAction.PAGE_StartIndex);
		w.setPageSize(-1);
		assertEquals(w.checkRetrieveN(BaseBO.INVALID_CASE_ID), FieldFormat.FIELD_ERROR_Paging);
		//
		w.setPageIndex(BaseAction.PAGE_StartIndex);
		w.setPageSize(BaseAction.PAGE_SIZE);
		assertEquals(w.checkRetrieveN(BaseBO.INVALID_CASE_ID), "");
	}

	@Test
	public void checkRetrieve1() {
		Warehouse w = new Warehouse();
		w.setID(0);
		assertEquals(w.checkRetrieve1(BaseBO.INVALID_CASE_ID), FieldFormat.FIELD_ERROR_ID);
		//
		w.setID(BaseAction.INVALID_ID);
		assertEquals(w.checkRetrieve1(BaseBO.INVALID_CASE_ID), FieldFormat.FIELD_ERROR_ID);
		//
		w.setID(1);
		assertEquals(w.checkRetrieve1(BaseBO.INVALID_CASE_ID), "");

		//
		w.setID(0);
		assertEquals(w.checkRetrieve1(BaseBO.CASE_Warehouse_RetrieveInventory), "");
		w.setID(BaseAction.INVALID_ID);
		assertEquals(w.checkRetrieve1(BaseBO.CASE_Warehouse_RetrieveInventory), "");
		w.setID(1);
		assertEquals(w.checkRetrieve1(BaseBO.CASE_Warehouse_RetrieveInventory), "");
	}
}
