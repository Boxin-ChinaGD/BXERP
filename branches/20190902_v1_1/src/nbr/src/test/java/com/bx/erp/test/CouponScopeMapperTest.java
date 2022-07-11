package com.bx.erp.test;

import java.util.List;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.Coupon;
import com.bx.erp.model.CouponScope;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.commodity.Commodity;
import com.bx.erp.util.DataSourceContextHolder;

public class CouponScopeMapperTest extends BaseMapperTest {
	@BeforeClass
	public void setup() {
		super.setUp();
	}

	@AfterClass
	public void tearDown() {
		super.tearDown();
	}

	@Test
	public void createTest1() throws Exception {
		Shared.printTestMethodStartInfo();
		//
		Shared.caseLog("Case1:正常添加");
		// 创建商品
		Commodity commodity = BaseCommodityTest.DataInput.getCommodity();
		Commodity commodityCreate = BaseCommodityTest.createCommodityViaMapper(commodity, BaseBO.CASE_Commodity_CreateSingle);
		// 创建优惠券
		Coupon coupon = BaseCouponTest.DataInput.getCoupon();
		Coupon couponCreate = BaseCouponTest.createViaMapper(coupon);
		// 创建优惠券范围
		CouponScope couponScope = BaseCouponScopeTest.DataInput.getCouponScope(couponCreate.getID(), commodityCreate.getID());
		CouponScope couponScopeCreate = BaseCouponScopeTest.createViaMapper(couponScope);
		//
		BaseCouponScopeTest.deleteViaMapper(couponScopeCreate);
		BaseCouponTest.deleteViaMapper(couponCreate);
		BaseCommodityTest.deleteCommodityViaMapper(commodityCreate, EnumErrorCode.EC_NoError);
	}

	@Test
	public void createTest2() throws Exception {
		Shared.printTestMethodStartInfo();
		//
		Shared.caseLog("Case2:商品不存在");
		// 创建优惠券
		Coupon coupon = BaseCouponTest.DataInput.getCoupon();
		Coupon couponCreate = BaseCouponTest.createViaMapper(coupon);
		// 创建优惠券范围
		CouponScope couponScope = BaseCouponScopeTest.DataInput.getCouponScope(couponCreate.getID(), BaseAction.INVALID_ID);
		Map<String, Object> params = couponScope.getCreateParam(BaseBO.INVALID_CASE_ID, couponScope);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		CouponScope couponScopeCreate = (CouponScope) couponScopeMapper.create(params);
		Assert.assertTrue(couponScopeCreate == null && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoSuchData,
				"创建对象失败。param=" + params + "\n\r bonusRule=" + couponScope);
		//
		BaseCouponTest.deleteViaMapper(couponCreate);
	}

	@Test
	public void createTest3() throws Exception {
		Shared.printTestMethodStartInfo();
		//
		Shared.caseLog("Case3:优惠券不存在");
		// 创建商品
		Commodity commodity = BaseCommodityTest.DataInput.getCommodity();
		Commodity commodityCreate = BaseCommodityTest.createCommodityViaMapper(commodity, BaseBO.CASE_Commodity_CreateSingle);
		// 创建优惠券范围
		CouponScope couponScope = BaseCouponScopeTest.DataInput.getCouponScope(BaseAction.INVALID_ID, commodityCreate.getID());
		Map<String, Object> params = couponScope.getCreateParam(BaseBO.INVALID_CASE_ID, couponScope);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		CouponScope couponScopeCreate = (CouponScope) couponScopeMapper.create(params);
		Assert.assertTrue(couponScopeCreate == null && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoSuchData,
				"创建对象失败。param=" + params + "\n\r bonusRule=" + couponScope);
		//
		BaseCommodityTest.deleteCommodityViaMapper(commodityCreate, EnumErrorCode.EC_NoError);
	}

	@Test
	public void retrieve1Test1() throws Exception {
		Shared.printTestMethodStartInfo();
		//
		Shared.caseLog("Case1:查询单个优惠券范围");
		// 创建商品
		Commodity commodity = BaseCommodityTest.DataInput.getCommodity();
		Commodity commodityCreate = BaseCommodityTest.createCommodityViaMapper(commodity, BaseBO.CASE_Commodity_CreateSingle);
		// 创建优惠券
		Coupon coupon = BaseCouponTest.DataInput.getCoupon();
		Coupon couponCreate = BaseCouponTest.createViaMapper(coupon);
		// 创建优惠券范围
		CouponScope couponScope = BaseCouponScopeTest.DataInput.getCouponScope(couponCreate.getID(), commodityCreate.getID());
		CouponScope couponScopeCreate = BaseCouponScopeTest.createViaMapper(couponScope);
		//
		CouponScope couponScopeRetrieve1 = (CouponScope) BaseCouponScopeTest.retrieve1ViaMapper(couponScopeCreate);
		Assert.assertTrue(couponScopeRetrieve1 != null);
		//
		BaseCouponScopeTest.deleteViaMapper(couponScopeCreate);
		BaseCouponTest.deleteViaMapper(couponCreate);
		BaseCommodityTest.deleteCommodityViaMapper(commodityCreate, EnumErrorCode.EC_NoError);
	}

	@Test
	public void retrieveNTest1() throws Exception {
		Shared.printTestMethodStartInfo();
		//
		Shared.caseLog("Case1:查询多个优惠券范围");
		// 创建商品
		Commodity commodity = BaseCommodityTest.DataInput.getCommodity();
		Commodity commodityCreate = BaseCommodityTest.createCommodityViaMapper(commodity, BaseBO.CASE_Commodity_CreateSingle);
		// 创建优惠券
		Coupon coupon = BaseCouponTest.DataInput.getCoupon();
		Coupon couponCreate = BaseCouponTest.createViaMapper(coupon);
		// 创建优惠券范围
		CouponScope couponScope = BaseCouponScopeTest.DataInput.getCouponScope(couponCreate.getID(), commodityCreate.getID());
		CouponScope couponScopeCreate = BaseCouponScopeTest.createViaMapper(couponScope);
		//
		CouponScope couponScopeToRN = new CouponScope();
		couponScopeToRN.setCouponID(BaseAction.INVALID_ID);
		Map<String, Object> params = couponScope.getRetrieveNParam(BaseBO.INVALID_CASE_ID, couponScopeToRN);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> couponScopeRetrieveN = couponScopeMapper.retrieveN(params);
		//
		Assert.assertTrue(couponScopeRetrieveN.size() > 0 && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, //
				params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		//
		String err = "";
		for (BaseModel bm : couponScopeRetrieveN) {
			CouponScope cs = (CouponScope) bm;
			err = cs.checkCreate(BaseBO.INVALID_CASE_ID);
			Assert.assertTrue("".equals(err), err);
		}
		//
		BaseCouponScopeTest.deleteViaMapper(couponScopeCreate);
		BaseCouponTest.deleteViaMapper(couponCreate);
		BaseCommodityTest.deleteCommodityViaMapper(commodityCreate, EnumErrorCode.EC_NoError);
	}

	@Test
	public void retrieveNTest2() throws Exception {
		Shared.printTestMethodStartInfo();
		//
		Shared.caseLog("Case2:根据CouponID查询多个优惠券范围");
		// 创建商品
		Commodity commodity = BaseCommodityTest.DataInput.getCommodity();
		Commodity commodityCreate = BaseCommodityTest.createCommodityViaMapper(commodity, BaseBO.CASE_Commodity_CreateSingle);
		// 创建优惠券
		Coupon coupon = BaseCouponTest.DataInput.getCoupon();
		Coupon couponCreate = BaseCouponTest.createViaMapper(coupon);
		// 创建优惠券范围
		CouponScope couponScope = BaseCouponScopeTest.DataInput.getCouponScope(couponCreate.getID(), commodityCreate.getID());
		CouponScope couponScopeCreate = BaseCouponScopeTest.createViaMapper(couponScope);
		//
		CouponScope couponScopeToRN = new CouponScope();
		couponScopeToRN.setCouponID(couponCreate.getID());
		Map<String, Object> params = couponScope.getRetrieveNParam(BaseBO.INVALID_CASE_ID, couponScopeToRN);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> couponScopeRetrieveN = couponScopeMapper.retrieveN(params);
		//
		Assert.assertTrue(couponScopeRetrieveN.size() > 0 && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, //
				params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		//
		String err = "";
		for (BaseModel bm : couponScopeRetrieveN) {
			CouponScope cs = (CouponScope) bm;
			err = cs.checkCreate(BaseBO.INVALID_CASE_ID);
			Assert.assertTrue("".equals(err), err);
			Assert.assertTrue(cs.getCouponID() == couponCreate.getID(), "查询的优惠券范围不正确");
		}
		//
		BaseCouponScopeTest.deleteViaMapper(couponScopeCreate);
		BaseCouponTest.deleteViaMapper(couponCreate);
		BaseCommodityTest.deleteCommodityViaMapper(commodityCreate, EnumErrorCode.EC_NoError);
	}

	@Test
	public void retrieveNTest3() throws Exception {
		Shared.printTestMethodStartInfo();
		//
		Shared.caseLog("Case3:传递不存在的CouponID查询优惠券范围");

		CouponScope couponScopeToRN = new CouponScope();
		couponScopeToRN.setCouponID(Shared.BIG_ID);
		Map<String, Object> params = couponScopeToRN.getRetrieveNParam(BaseBO.INVALID_CASE_ID, couponScopeToRN);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> couponScopeRetrieveN = couponScopeMapper.retrieveN(params);
		//
		Assert.assertTrue(couponScopeRetrieveN.size() == 0 && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, //
				params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
	}

	@Test
	public void deleteTest1() throws Exception {
		Shared.printTestMethodStartInfo();
		//
		Shared.caseLog("Case1:正常删除");
		// 创建商品
		Commodity commodity = BaseCommodityTest.DataInput.getCommodity();
		Commodity commodityCreate = BaseCommodityTest.createCommodityViaMapper(commodity, BaseBO.CASE_Commodity_CreateSingle);
		// 创建优惠券
		Coupon coupon = BaseCouponTest.DataInput.getCoupon();
		Coupon couponCreate = BaseCouponTest.createViaMapper(coupon);
		// 创建优惠券范围
		CouponScope couponScope = BaseCouponScopeTest.DataInput.getCouponScope(couponCreate.getID(), commodityCreate.getID());
		CouponScope couponScopeCreate = BaseCouponScopeTest.createViaMapper(couponScope);
		// 删除优惠券范围
		BaseCouponScopeTest.deleteViaMapper(couponScopeCreate);
		//
		BaseCouponTest.deleteViaMapper(couponCreate);
		BaseCommodityTest.deleteCommodityViaMapper(commodityCreate, EnumErrorCode.EC_NoError);
	}
}
