package com.bx.erp.model.commodity;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.test.Shared;
import com.bx.erp.util.FieldFormat;

public class CommodityHistoryTest {

	@BeforeClass
	public void setup() {
		Shared.printTestClassStartInfo();
	}

	@AfterClass
	public void tearDown() {
		Shared.printTestClassEndInfo();
	}
	
	@Test
	public void testCheckRetrieveN() {
		CommodityHistory ch = new CommodityHistory();

		ch.setQueryKeyword("123123");
		ch.setFieldName("可口可乐");
		ch.setStaffID(1);
		ch.setCommodityID(1);
		ch.setPageIndex(1);
		ch.setPageSize(10);
		String err = ch.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");
		//测试QueryKeyword
		ch.setQueryKeyword("");
		err = ch.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");
		ch.setQueryKeyword("1234567812345678123456781234567812345678123456781234567812345678aaa");
		err = ch.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, CommodityHistory.FIELD_ERROR_queryKeyword);
		//测试fieldName
		ch.setQueryKeyword("123123");
		ch.setFieldName("1234512345123451234511");
		err = ch.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, CommodityHistory.FIELD_ERROR_fieldName);
		//测试staffID
		ch.setFieldName("可口可乐");
		ch.setStaffID(BaseAction.INVALID_ID);
		err = ch.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");
		ch.setStaffID(0);
		err = ch.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, CommodityHistory.FIELD_ERROR_staffID);
		//测试commodityID
		ch.setStaffID(1);
		ch.setCommodityID(BaseAction.INVALID_ID);
		err = ch.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");
		ch.setCommodityID(0);
		err = ch.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, CommodityHistory.FIELD_ERROR_commodityID);
		ch.setCommodityID(1);
		//测试PageIndex
		ch.setPageIndex(0);
		err = ch.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, FieldFormat.FIELD_ERROR_Paging);
		ch.setPageIndex(2);
		//测试PageSize
		ch.setPageSize(0);
		err = ch.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, FieldFormat.FIELD_ERROR_Paging);
		ch.setPageSize(5);
	}
}
