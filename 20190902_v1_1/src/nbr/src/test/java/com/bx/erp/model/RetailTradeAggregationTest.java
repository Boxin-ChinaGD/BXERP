package com.bx.erp.model;

import java.util.Date;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.test.Shared;
import com.bx.erp.util.DatetimeUtil;

public class RetailTradeAggregationTest {

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

		RetailTradeAggregation retailTradeAggregation = new RetailTradeAggregation();
		String error = "";
		//
		retailTradeAggregation.setStaffID(0);
		error = retailTradeAggregation.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, RetailTradeAggregation.FIELD_ERROR_StaffID);
		retailTradeAggregation.setStaffID(-98);
		error = retailTradeAggregation.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, RetailTradeAggregation.FIELD_ERROR_StaffID);
		retailTradeAggregation.setStaffID(1);
		//
		retailTradeAggregation.setPosID(0);
		error = retailTradeAggregation.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, RetailTradeAggregation.FIELD_ERROR_PosID);
		retailTradeAggregation.setPosID(-98);
		error = retailTradeAggregation.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, RetailTradeAggregation.FIELD_ERROR_PosID);
		retailTradeAggregation.setPosID(1);
		//
		retailTradeAggregation.setTradeNO(-98);
		error = retailTradeAggregation.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, RetailTradeAggregation.FIELD_ERROR_TradeNO);
		retailTradeAggregation.setTradeNO(1);
		//
		retailTradeAggregation.setWorkTimeStart(null);
		error = retailTradeAggregation.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, RetailTradeAggregation.FIELD_ERROR_workTimeStart);
		retailTradeAggregation.setWorkTimeStart(new Date());
		retailTradeAggregation.setWorkTimeEnd(DatetimeUtil.getDate(retailTradeAggregation.getWorkTimeStart(), -1));
		error = retailTradeAggregation.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, RetailTradeAggregation.FIELD_ERROR_workTimeStartAndEnd);
		retailTradeAggregation.setWorkTimeStart(new Date());
		retailTradeAggregation.setWorkTimeEnd(retailTradeAggregation.getWorkTimeStart());
		error = retailTradeAggregation.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, RetailTradeAggregation.FIELD_ERROR_workTimeStartAndEnd);
		retailTradeAggregation.setWorkTimeStart(new Date());
		retailTradeAggregation.setWorkTimeEnd(DatetimeUtil.getDate(retailTradeAggregation.getWorkTimeStart(), 1));
		//
		error = retailTradeAggregation.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
		// reserveAmount
		retailTradeAggregation.setReserveAmount(-1);
		error = retailTradeAggregation.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, RetailTradeAggregation.FIELD_ERROR_ReserveAmount);
		//
		retailTradeAggregation.setReserveAmount(10001);
		error = retailTradeAggregation.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, RetailTradeAggregation.FIELD_ERROR_ReserveAmount);
		//
		retailTradeAggregation.setReserveAmount(0);
		error = retailTradeAggregation.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
		//
		retailTradeAggregation.setReserveAmount(100);
		error = retailTradeAggregation.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
		//
		retailTradeAggregation.setReserveAmount(10000);
		error = retailTradeAggregation.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
		// amout
		retailTradeAggregation.setAmount(-1);
		error = retailTradeAggregation.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, String.format(RetailTradeAggregation.FIELD_ERROR_Amount, RetailTradeAggregation.field.getFIELD_NAME_amount()));
		retailTradeAggregation.setAmount(1000);
		retailTradeAggregation.setWechatAmount(1000);
		error = retailTradeAggregation.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
		retailTradeAggregation.setAmount(0);
		retailTradeAggregation.setWechatAmount(0);
		error = retailTradeAggregation.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
		// cashAmount
		retailTradeAggregation.setCashAmount(-1);
		error = retailTradeAggregation.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, String.format(RetailTradeAggregation.FIELD_ERROR_Amount, RetailTradeAggregation.field.getFIELD_NAME_cashAmount()));
		retailTradeAggregation.setAmount(1000);
		retailTradeAggregation.setCashAmount(1000);
		error = retailTradeAggregation.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
		retailTradeAggregation.setAmount(0);
		retailTradeAggregation.setCashAmount(0);
		error = retailTradeAggregation.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
		// wechatAmout
		retailTradeAggregation.setWechatAmount(-1);
		error = retailTradeAggregation.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, String.format(RetailTradeAggregation.FIELD_ERROR_Amount, RetailTradeAggregation.field.getFIELD_NAME_wechatAmount()));
		retailTradeAggregation.setAmount(1000);
		retailTradeAggregation.setWechatAmount(1000);
		error = retailTradeAggregation.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
		retailTradeAggregation.setAmount(0);
		retailTradeAggregation.setWechatAmount(0);
		error = retailTradeAggregation.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
		// amout != wechatAmout + cachAmout
		retailTradeAggregation.setAmount(200);;
		retailTradeAggregation.setWechatAmount(100);
		retailTradeAggregation.setCashAmount(200);
		error = retailTradeAggregation.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, RetailTradeAggregation.FIELD_ERROR_amountTotal);
		retailTradeAggregation.setAmount(400);;
		retailTradeAggregation.setWechatAmount(100);
		retailTradeAggregation.setCashAmount(200);
		error = retailTradeAggregation.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, RetailTradeAggregation.FIELD_ERROR_amountTotal);
		retailTradeAggregation.setAmount(300);;
		retailTradeAggregation.setWechatAmount(100);
		retailTradeAggregation.setCashAmount(200);
		error = retailTradeAggregation.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
	}

	@Test
	public void testCheckRetrieve1() {
		Shared.printTestMethodStartInfo();

		RetailTradeAggregation retailTradeAggregation = new RetailTradeAggregation();
		String error = "";
		error = retailTradeAggregation.checkRetrieve1(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
	}

	@Test
	public void testCheckRetrieveN() {
		Shared.printTestMethodStartInfo();

		RetailTradeAggregation retailTradeAggregation = new RetailTradeAggregation();
		String error = "";
		error = retailTradeAggregation.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
	}

	@Test
	public void testCheckUpdate() {
		Shared.printTestMethodStartInfo();

		RetailTradeAggregation retailTradeAggregation = new RetailTradeAggregation();
		String error = "";
		error = retailTradeAggregation.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
	}

	@Test
	public void testCheckDelete() {
		Shared.printTestMethodStartInfo();

		RetailTradeAggregation retailTradeAggregation = new RetailTradeAggregation();
		String error = "";
		error = retailTradeAggregation.checkDelete(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
	}
}
