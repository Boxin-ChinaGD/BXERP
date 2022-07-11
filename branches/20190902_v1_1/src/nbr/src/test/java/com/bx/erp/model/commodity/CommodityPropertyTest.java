package com.bx.erp.model.commodity;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.test.Shared;
import com.bx.erp.util.FieldFormat;

public class CommodityPropertyTest {

	@BeforeClass
	public void setup() {
		Shared.printTestClassStartInfo();
	}

	@AfterClass
	public void tearDown() {
		Shared.printTestClassEndInfo();
	}

	@Test
	public void checkRetrieve1Test() {
		Shared.printTestClassEndInfo();

		CommodityProperty cp = new CommodityProperty();
		String error = "";

		cp.setID(0);
		error = cp.checkRetrieve1(BaseBO.INVALID_CASE_ID);
		assertEquals(error, FieldFormat.FIELD_ERROR_ID);
		cp.setID(BaseAction.INVALID_ID);
		error = cp.checkRetrieve1(BaseBO.INVALID_CASE_ID);
		assertEquals(error, FieldFormat.FIELD_ERROR_ID);
		//
		cp.setID(1);
		error = cp.checkRetrieve1(BaseBO.INVALID_CASE_ID);
		assertEquals(error, "");
	}

	@Test
	public void checkUpdateTest() {
		Shared.printTestClassEndInfo();

		CommodityProperty cp = new CommodityProperty();

		cp.setName1("123456789123456789");
		String error = cp.checkUpdate(BaseBO.INVALID_CASE_ID);
		assertEquals(error, CommodityProperty.FIELD_ERROR_name);
		cp.setName1("111");

		cp.setName2("123456789123456789");
		error = cp.checkUpdate(BaseBO.INVALID_CASE_ID);
		assertEquals(error, CommodityProperty.FIELD_ERROR_name);
		cp.setName2("111");

		cp.setName3("123456789123456789");
		error = cp.checkUpdate(BaseBO.INVALID_CASE_ID);
		assertEquals(error, CommodityProperty.FIELD_ERROR_name);
		cp.setName3("111");

		cp.setName4("123456789123456789");
		error = cp.checkUpdate(BaseBO.INVALID_CASE_ID);
		assertEquals(error, CommodityProperty.FIELD_ERROR_name);
		cp.setName4("111");
		//
		error = cp.checkUpdate(BaseBO.INVALID_CASE_ID);
		assertEquals(error, "");
	}
}
