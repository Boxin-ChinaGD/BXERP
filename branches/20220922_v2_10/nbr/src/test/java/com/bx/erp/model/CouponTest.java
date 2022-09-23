package com.bx.erp.model;

import java.util.Calendar;
import java.util.Date;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.model.Coupon.EnumCouponCardType;
import com.bx.erp.model.Coupon.EnumCouponStatus;
import com.bx.erp.model.Coupon.EnumCouponType;
import com.bx.erp.test.BaseCouponTest;
import com.bx.erp.test.Shared;
import com.bx.erp.util.FieldFormat;

import junit.framework.Assert;

public class CouponTest {

	@BeforeClass
	public void setUp() {
		Shared.printTestClassStartInfo();
	}

	@AfterClass
	public void tearDown() {
		Shared.printTestClassEndInfo();
	}

	@Test
	public void testCheckCreate() throws CloneNotSupportedException {
		Shared.printTestMethodStartInfo();
		Coupon coupon = BaseCouponTest.DataInput.getCoupon();
		// case1:status
		coupon.setStatus(-1);
		String error = coupon.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(Coupon.FIELD_ERROR_status, error);
		coupon.setStatus(3);
		error = coupon.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(Coupon.FIELD_ERROR_status, error);
		coupon.setStatus(EnumCouponStatus.ECS_Normal.getIndex());
		error = coupon.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals("", error);

		// case2：cardType
		coupon.setType(-1);
		error = coupon.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(Coupon.FIELD_ERROR_cardType, error);
		coupon.setType(3);
		error = coupon.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(Coupon.FIELD_ERROR_cardType, error);
		coupon.setType(EnumCouponCardType.ECCT_CASH.getIndex());
		error = coupon.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals("", error);

		// case3:bonus
		coupon.setBonus(-1);
		error = coupon.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(Coupon.FIELD_ERROR_bonus, error);
		coupon.setBonus(0);
		error = coupon.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals("", error);

		// case4:leaseAmount
		coupon.setType(EnumCouponType.ECT_Cash.getIndex());
		coupon.setLeastAmount(-1);
		error = coupon.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(Coupon.FIELD_ERROR_leastAmount, error);
		coupon.setLeastAmount(1);
		coupon.setReduceAmount(0.1);
		error = coupon.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals("", error);

		// case5:reduceAmount
		coupon.setReduceAmount(-1);
		error = coupon.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(Coupon.FIELD_ERROR_reduceAmount, error);
		coupon.setReduceAmount(0.00);
		error = coupon.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(Coupon.FIELD_ERROR_reduceAmount, error);
		coupon.setReduceAmount(0.5);
		error = coupon.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals("", error);
		coupon.setReduceAmount(1);
		error = coupon.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals("", error);

		// case6:discount
		coupon.setType(EnumCouponType.ECT_Discount.getIndex());
		coupon.setDiscount(-1);
		error = coupon.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(Coupon.FIELD_ERROR_discount, error);
		coupon.setDiscount(0);
		error = coupon.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(Coupon.FIELD_ERROR_discount, error);
		coupon.setDiscount(Coupon.MaxDiscount + 0.1);
		error = coupon.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(Coupon.FIELD_ERROR_discount, error);
		coupon.setDiscount(Coupon.MaxDiscount);
		error = coupon.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals("", error);
		coupon.setDiscount(0.5);
		error = coupon.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals("", error);

		// case7:title
		coupon.setTitle("?.,");
		error = coupon.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(Coupon.FIELD_ERROR_title, error);
		coupon.setTitle("1234567890");
		error = coupon.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(Coupon.FIELD_ERROR_title, error);
		coupon.setTitle(" ");
		error = coupon.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(Coupon.FIELD_ERROR_title, error);
		coupon.setTitle("");
		error = coupon.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(Coupon.FIELD_ERROR_title, error);
		coupon.setTitle(null);
		error = coupon.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(Coupon.FIELD_ERROR_title, error);
		coupon.setTitle("1234567890");
		error = coupon.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(Coupon.FIELD_ERROR_title, error);
		coupon.setTitle("123456789");
		error = coupon.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals("", error);
		coupon.setTitle("标题123");
		error = coupon.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals("", error);

		// case8:color
		coupon.setColor(null);
		error = coupon.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(Coupon.FIELD_ERROR_color, error);
		coupon.setColor("12345678901234567");
		error = coupon.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(Coupon.FIELD_ERROR_color, error);
		coupon.setColor("");
		error = coupon.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals("", error);
		coupon.setColor("000000");
		error = coupon.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals("", error);

		// case9:description
		coupon.setDescription(null);
		error = coupon.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(Coupon.FIELD_ERROR_description, error);
		coupon.setDescription("0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789" + "0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789"
				+ "0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789" + "0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789"
				+ "0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789" + "0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789"
				+ "0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789" + "0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789"
				+ "0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789" + "0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789"
				+ "012345678901234567890123456789");
		error = coupon.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(Coupon.FIELD_ERROR_description, error);
		coupon.setDescription("0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789");
		error = coupon.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals("", error);

		// case10:type
		coupon.setWeekDayAvailable(-1);
		error = coupon.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(Coupon.FIELD_ERROR_type, error);
		coupon.setWeekDayAvailable(129);
		error = coupon.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(Coupon.FIELD_ERROR_type, error);
		coupon.setWeekDayAvailable(0);
		error = coupon.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals("", error);
		coupon.setWeekDayAvailable(64);
		error = coupon.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals("", error);

		// case11:beginTime
		coupon.setBeginTime("00:00:000");
		error = coupon.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(Coupon.FIELD_ERROR_beginTime, error);

		// case12:beginTime
		coupon.setBeginTime("00:00:00");
		error = coupon.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals("", error);
		coupon.setBeginTime(null);
		error = coupon.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals("", error);

		// case13:endTime
		coupon.setEndTime("00:00:000");
		error = coupon.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(Coupon.FIELD_ERROR_endTime, error);
		coupon.setEndTime("00:00:00");
		error = coupon.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals("", error);
		coupon.setEndTime(null);
		error = coupon.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals("", error);

		// case14:beginDateTime、endDateTime
		coupon.setBeginDateTime(null);
		coupon.setEndDateTime(new Date());
		error = coupon.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(Coupon.FIELD_ERROR_beginDateTime_endDateTime, error);
		coupon.setBeginDateTime(new Date());
		coupon.setEndDateTime(null);
		error = coupon.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(Coupon.FIELD_ERROR_beginDateTime_endDateTime, error);
		Date now = new Date();
		coupon.setBeginDateTime(now);
		coupon.setEndDateTime(now);
		error = coupon.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(Coupon.FIELD_ERROR_beginDateTime_endDateTime, error);
		coupon.setBeginDateTime(now);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(now);
		calendar.add(Calendar.DATE, 1);
		coupon.setEndDateTime(calendar.getTime());
		error = coupon.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals("", error);

		// case15:quanity
		coupon.setQuantity(Coupon.MinQuantity - 1);
		coupon.setRemainingQuantity(Coupon.MinQuantity - 1);
		error = coupon.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(Coupon.FIELD_ERROR_quantity, error);
		coupon.setQuantity(Coupon.MaxQuantity + 1);
		coupon.setRemainingQuantity(Coupon.MaxQuantity + 1);
		error = coupon.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(Coupon.FIELD_ERROR_quantity, error);
		coupon.setQuantity(Coupon.MinQuantity + 1);
		coupon.setRemainingQuantity(Coupon.MinQuantity + 1);
		error = coupon.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals("", error);
		coupon.setQuantity(Coupon.MaxQuantity - 1);
		coupon.setRemainingQuantity(Coupon.MaxQuantity - 1);
		error = coupon.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals("", error);

		// case16:remainingQuantity
		coupon.setQuantity(2);
		coupon.setRemainingQuantity(-1);
		error = coupon.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(Coupon.FIELD_ERROR_remainingQuantity, error);
		coupon.setQuantity(10);
		coupon.setRemainingQuantity(10);
		error = coupon.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals("", error);
		//
		coupon.setQuantity(1);
		coupon.setRemainingQuantity(2);
		error = coupon.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(Coupon.FIELD_ERROR_remainingQuantity, error);

		// case17:scope
		coupon.setQuantity(10);
		coupon.setRemainingQuantity(10);
		//
		coupon.setScope(-1);
		error = coupon.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(Coupon.FIELD_ERROR_scope, error);
		coupon.setScope(2);
		error = coupon.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(Coupon.FIELD_ERROR_scope, error);
		coupon.setScope(0);
		error = coupon.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals("", error);
		coupon.setScope(1);
		error = coupon.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals("", error);

		// case18 leastAmount_reduceAmount
		coupon.setType(EnumCouponCardType.ECCT_CASH.getIndex());
		coupon.setLeastAmount(2);
		coupon.setReduceAmount(3);
		error = coupon.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(Coupon.FIELD_ERROR_leastAmount_reduceAmount, error);
		coupon.setType(EnumCouponCardType.ECCT_CASH.getIndex());
		coupon.setLeastAmount(3);
		coupon.setReduceAmount(2);
		error = coupon.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals("", error);

		// case19 PersonalLimit
		coupon.setPersonalLimit(0);
		error = coupon.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(Coupon.FIELD_ERROR_personalLimit, error);
		coupon.setPersonalLimit(1);
		error = coupon.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals("", error);
	}

	@Test
	public void testCheckDelete() throws CloneNotSupportedException {
		Shared.printTestMethodStartInfo();
		// case1:ID
		Coupon coupon = BaseCouponTest.DataInput.getCoupon();
		String error = coupon.checkDelete(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(FieldFormat.FIELD_ERROR_ID, error);
		coupon.setID(1);
		error = coupon.checkDelete(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals("", error);
	}

	@Test
	public void testCheckRetrieve1() throws CloneNotSupportedException {
		Shared.printTestMethodStartInfo();
		// case1:ID
		Coupon coupon = BaseCouponTest.DataInput.getCoupon();
		String error = coupon.checkRetrieve1(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(FieldFormat.FIELD_ERROR_ID, error);
		coupon.setID(1);
		error = coupon.checkRetrieve1(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals("", error);
	}

	@Test
	public void testCheckRetrieveN() throws CloneNotSupportedException {
		Shared.printTestMethodStartInfo();

		// POSID
		Coupon coupon = new Coupon();
		coupon.setPosID(0);
		String error = coupon.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(Coupon.FIELD_ERROR_posID, error);
		coupon.setPosID(Shared.BIG_ID);

		// PageSize,PageIndex  只有POSID>0才会检查
		coupon.setPageIndex(BaseAction.PAGE_StartIndex);
		coupon.setPageSize(BaseAction.PAGE_SIZE_MAX);
		error = coupon.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(Coupon.FIELD_ERROR_Paging, error);
		//
		coupon.setPageSize(BaseAction.PAGE_SIZE_Infinite);
		coupon.setPageIndex(2);
		error = coupon.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(Coupon.FIELD_ERROR_Paging, error);
		//
		coupon.setPageIndex(BaseAction.PAGE_StartIndex);
		error = coupon.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals("", error);
	}
}
