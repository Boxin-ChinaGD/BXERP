package com.bx.erp.test;

import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.web.filter.CharacterEncodingFilter;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.model.RetailTrade;
import com.bx.erp.model.RetailTradeCommodity;
import com.bx.erp.model.RetailTradeCoupon;
import com.bx.erp.model.Vip;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.Coupon;
import com.bx.erp.model.CouponCode;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.util.DataSourceContextHolder;

public class RetailTradeCouponMapperTest extends BaseMapperTest {
	protected final int VIP_ID = 1;

	@BeforeClass
	public void setup() {
		super.setUp();

		CharacterEncodingFilter encodingFilter = new CharacterEncodingFilter();
		encodingFilter.setEncoding("utf-8");
		encodingFilter.setForceEncoding(true);
	}

	@AfterClass
	public void tearDown() {
		super.tearDown();
	}

	@Test
	public void testCreateCase1() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case1 正常添加零售单优惠券使用表");
		// 创建零售单
		RetailTrade rt = BaseRetailTradeTest.DataInput.getRetailTrade();
		//
		RetailTradeCommodity rtc = BaseRetailTradeTest.DataInput.getRetailTradeCommodity();
		List<RetailTradeCommodity> list = new ArrayList<RetailTradeCommodity>();
		list.add(rtc);
		rt.setListSlave1(list);
		//
		RetailTrade retailTradeCreate = BaseRetailTradeTest.createRetailtradeViaMapper(rt);

		// 创建优惠券
		Coupon coupon = BaseCouponTest.DataInput.getCoupon();
		Coupon couponCreate = BaseCouponTest.createViaMapper(coupon);
		// 领取优惠券
		CouponCode couponCode = BaseCouponCodeTest.DataInput.getCouponCode(VIP_ID, couponCreate.getID());
		CouponCode couponCodeCreate = BaseCouponCodeTest.createViaMapper(couponCode);
		// 创建零售单优惠券使用表
		RetailTradeCoupon retailTradeCoupon = BaseRetailTradeTest.DataInput.getRetailTradeCoupon(couponCodeCreate.getID(), retailTradeCreate.getID());
		BaseRetailTradeTest.createRetailTradeCouponViaMapper(retailTradeCoupon, EnumErrorCode.EC_NoError);
	}

	@Test
	public void testCreateCase2() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case2零售单ID不存在");
		// 创建优惠券
		Coupon coupon = BaseCouponTest.DataInput.getCoupon();
		Coupon couponCreate = BaseCouponTest.createViaMapper(coupon);
		// 领取优惠券
		CouponCode couponCode = BaseCouponCodeTest.DataInput.getCouponCode(VIP_ID, couponCreate.getID());
		CouponCode couponCodeCreate = BaseCouponCodeTest.createViaMapper(couponCode);
		// 创建零售单优惠券使用表
		RetailTradeCoupon retailTradeCoupon = BaseRetailTradeTest.DataInput.getRetailTradeCoupon(couponCodeCreate.getID(), BaseAction.INVALID_ID);
		Map<String, Object> param = retailTradeCoupon.getCreateParam(BaseBO.INVALID_CASE_ID, retailTradeCoupon);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		retailTradeCouponMapper.create(param);

		assertTrue(EnumErrorCode.values()[Integer.parseInt(param.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoSuchData, param.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
	}

	@Test
	public void testCreateCase3() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case3优惠券ID不存在");
		// 创建零售单
		RetailTrade rt = BaseRetailTradeTest.DataInput.getRetailTrade();
		//
		RetailTradeCommodity rtc = BaseRetailTradeTest.DataInput.getRetailTradeCommodity();
		List<RetailTradeCommodity> list = new ArrayList<RetailTradeCommodity>();
		list.add(rtc);
		rt.setListSlave1(list);
		//
		RetailTrade retailTradeCreate = BaseRetailTradeTest.createRetailtradeViaMapper(rt);

		// 创建零售单优惠券使用表
		RetailTradeCoupon retailTradeCoupon = BaseRetailTradeTest.DataInput.getRetailTradeCoupon(BaseAction.INVALID_ID, retailTradeCreate.getID());
		Map<String, Object> param = retailTradeCoupon.getCreateParam(BaseBO.INVALID_CASE_ID, retailTradeCoupon);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		retailTradeCouponMapper.create(param);

		assertTrue(EnumErrorCode.values()[Integer.parseInt(param.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoSuchData, param.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
	}

	@Test
	public void testCreateCase4() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case4优惠券vipid跟零售单的vipid不一致");
		// 创建零售单
		RetailTrade rt = BaseRetailTradeTest.DataInput.getRetailTrade();
		rt.setVipID(VIP_ID);
		//
		RetailTradeCommodity rtc = BaseRetailTradeTest.DataInput.getRetailTradeCommodity();
		List<RetailTradeCommodity> list = new ArrayList<RetailTradeCommodity>();
		list.add(rtc);
		rt.setListSlave1(list);
		//
		RetailTrade createRetailtrade = BaseRetailTradeTest.createRetailtradeViaMapper(rt);
		
		// 创建优惠券
		Coupon coupon = BaseCouponTest.DataInput.getCoupon();
		Coupon couponCreate = BaseCouponTest.createViaMapper(coupon);
		
		// 创建会员
		Vip vip = BaseVipTest.createViaMapper(BaseBO.INVALID_CASE_ID, BaseVipTest.DataInput.getVip(), EnumErrorCode.EC_NoError, Shared.DBName_Test);
		
		// 领取优惠券
		CouponCode couponCode = BaseCouponCodeTest.DataInput.getCouponCode(vip.getID(), couponCreate.getID());
		CouponCode couponCodeCreate = BaseCouponCodeTest.createViaMapper(couponCode);
		
		RetailTradeCoupon retailTradeCoupon = BaseRetailTradeTest.DataInput.getRetailTradeCoupon(couponCodeCreate.getID(), createRetailtrade.getID());
		BaseRetailTradeTest.createRetailTradeCouponViaMapper(retailTradeCoupon, EnumErrorCode.EC_BusinessLogicNotDefined);
	}

	@Test
	public void testRetrieveNCase1() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case1正常查询");

		// 创建零售单
		RetailTrade rt = BaseRetailTradeTest.DataInput.getRetailTrade();
		//
		RetailTradeCommodity rtc = BaseRetailTradeTest.DataInput.getRetailTradeCommodity();
		List<RetailTradeCommodity> list = new ArrayList<RetailTradeCommodity>();
		list.add(rtc);
		rt.setListSlave1(list);
		//
		RetailTrade retailTradeCreate = BaseRetailTradeTest.createRetailtradeViaMapper(rt);

		// 创建优惠券
		Coupon coupon = BaseCouponTest.DataInput.getCoupon();
		Coupon couponCreate = BaseCouponTest.createViaMapper(coupon);
		// 领取优惠券
		CouponCode couponCode = BaseCouponCodeTest.DataInput.getCouponCode(VIP_ID, couponCreate.getID());
		CouponCode couponCodeCreate = BaseCouponCodeTest.createViaMapper(couponCode);
		// 创建零售单优惠券使用表
		RetailTradeCoupon retailTradeCoupon = BaseRetailTradeTest.DataInput.getRetailTradeCoupon(couponCodeCreate.getID(), retailTradeCreate.getID());
		BaseRetailTradeTest.createRetailTradeCouponViaMapper(retailTradeCoupon, EnumErrorCode.EC_NoError);

		// 查询
		RetailTradeCoupon queryRetailTradeCoupon = new RetailTradeCoupon();
		Map<String, Object> queryParam = queryRetailTradeCoupon.getRetrieveNParam(BaseBO.INVALID_CASE_ID, queryRetailTradeCoupon);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> retailTradeCouponList = retailTradeCouponMapper.retrieveN(queryParam);

		assertTrue(retailTradeCouponList != null && retailTradeCouponList.size() > 0 && EnumErrorCode.values()[Integer.parseInt(queryParam.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError,
				queryParam.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
	}

	@Test
	public void testRetrieveNCase2() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case2使用retailTradeID进行查询");

		// 创建零售单
		RetailTrade rt = BaseRetailTradeTest.DataInput.getRetailTrade();
		//
		RetailTradeCommodity rtc = BaseRetailTradeTest.DataInput.getRetailTradeCommodity();
		List<RetailTradeCommodity> list = new ArrayList<RetailTradeCommodity>();
		list.add(rtc);
		rt.setListSlave1(list);
		//
		RetailTrade retailTradeCreate = BaseRetailTradeTest.createRetailtradeViaMapper(rt);

		// 创建优惠券
		Coupon coupon = BaseCouponTest.DataInput.getCoupon();
		Coupon couponCreate = BaseCouponTest.createViaMapper(coupon);
		// 领取优惠券
		CouponCode couponCode = BaseCouponCodeTest.DataInput.getCouponCode(VIP_ID, couponCreate.getID());
		CouponCode couponCodeCreate = BaseCouponCodeTest.createViaMapper(couponCode);
		// 创建零售单优惠券使用表
		RetailTradeCoupon retailTradeCoupon = BaseRetailTradeTest.DataInput.getRetailTradeCoupon(couponCodeCreate.getID(), retailTradeCreate.getID());
		BaseRetailTradeTest.createRetailTradeCouponViaMapper(retailTradeCoupon, EnumErrorCode.EC_NoError);

		// 查询
		RetailTradeCoupon queryRetailTradeCoupon = new RetailTradeCoupon();
		queryRetailTradeCoupon.setRetailTradeID(retailTradeCreate.getID());
		Map<String, Object> queryParam = queryRetailTradeCoupon.getRetrieveNParam(BaseBO.INVALID_CASE_ID, queryRetailTradeCoupon);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> retailTradeCouponList = retailTradeCouponMapper.retrieveN(queryParam);

		assertTrue(retailTradeCouponList != null && retailTradeCouponList.size() == 1 && EnumErrorCode.values()[Integer.parseInt(queryParam.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError,
				queryParam.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
	}

	@Test
	public void testRetrieveNCase3() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case3使用不存在的retailTradeID进行查询");

		RetailTradeCoupon queryRetailTradeCoupon = new RetailTradeCoupon();
		queryRetailTradeCoupon.setRetailTradeID(-99);
		Map<String, Object> queryParam = queryRetailTradeCoupon.getRetrieveNParam(BaseBO.INVALID_CASE_ID, queryRetailTradeCoupon);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> retailTradeCouponList = retailTradeCouponMapper.retrieveN(queryParam);

		assertTrue(retailTradeCouponList != null && retailTradeCouponList.size() == 0 && EnumErrorCode.values()[Integer.parseInt(queryParam.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError,
				queryParam.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
	}
}
