package com.bx.erp.test;

import java.util.ArrayList;
import java.util.Date;
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
import com.bx.erp.model.CouponCode;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.util.DataSourceContextHolder;
import com.bx.erp.util.DatetimeUtil;

public class CouponMapperTest extends BaseMapperTest {
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
		//
		Coupon coupon = BaseCouponTest.DataInput.getCoupon();
		Coupon couponCreate = BaseCouponTest.createViaMapper(coupon);
		//
		BaseCouponTest.deleteViaMapper(couponCreate);
	}

	@Test
	public void retrieve1Test1() throws Exception {
		Shared.printTestMethodStartInfo();
		//
		Shared.caseLog("Case1:正常查询单个优惠券");
		//
		Coupon coupon = BaseCouponTest.DataInput.getCoupon();
		Coupon couponCreate = BaseCouponTest.createViaMapper(coupon);
		//
		Coupon couponRetrieve1 = (Coupon) BaseCouponTest.retrieve1ViaMapper(couponCreate);
		Assert.assertTrue(couponRetrieve1 != null);
		//
		BaseCouponTest.deleteViaMapper(couponCreate);
	}

	@Test
	public void retrieveNTest1() throws Exception {
		Shared.printTestMethodStartInfo();
		//
		Shared.caseLog("Case1:查询多个优惠券");
		//
		Coupon coupon = BaseCouponTest.DataInput.getCoupon();
		Coupon couponCreate = BaseCouponTest.createViaMapper(coupon);
		//
		Map<String, Object> params = couponCreate.getRetrieveNParam(BaseBO.INVALID_CASE_ID, couponCreate);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> couponRetrieveN = couponMapper.retrieveN(params);
		//
		Assert.assertTrue(couponRetrieveN.size() > 0 && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, //
				params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		//
		BaseCouponTest.deleteViaMapper(couponCreate);
	}

	@Test
	public void retrieveNTest2() throws Exception {
		Shared.printTestMethodStartInfo();
		//
		Shared.caseLog("Case2:posID大于0时。有种优惠券已经终止。返回数据中无此优惠券");
		Coupon coupon = BaseCouponTest.DataInput.getCoupon();
		coupon.setStatus(Coupon.EnumCouponStatus.ECS_Expired.getIndex());
		Coupon couponCreate = BaseCouponTest.createViaMapper(coupon);
		//
		Coupon queryCoupon = new Coupon();
		queryCoupon.setPosID(Shared.BIG_ID);
		queryCoupon.setPageIndex(BaseAction.PAGE_StartIndex);
		queryCoupon.setPageSize(BaseAction.PAGE_SIZE_Infinite);
		List<BaseModel> couponList = BaseCouponTest.retrieveNViaMapper(queryCoupon);
		for (BaseModel bm : couponList) {
			Assert.assertTrue(couponCreate.getID() != bm.getID(), "查到了刚刚创建状态为删除的优惠券");
		}
	}

	@Test
	public void retrieveNTest3() throws Exception {
		Shared.printTestMethodStartInfo();
		//
		Shared.caseLog("Case3:POSID大于0时.有种优惠券已开始生效,还未到结束时间.返回的优惠券集合中有此优惠券");
		Coupon coupon = BaseCouponTest.DataInput.getCoupon();
		coupon.setBeginDateTime(DatetimeUtil.getDays(new Date(), -1));
		Coupon couponCreate = BaseCouponTest.createViaMapper(coupon);
		//
		Coupon queryCoupon = new Coupon();
		queryCoupon.setPosID(Shared.BIG_ID);
		queryCoupon.setPageIndex(BaseAction.PAGE_StartIndex);
		queryCoupon.setPageSize(BaseAction.PAGE_SIZE_Infinite);
		List<BaseModel> couponList = BaseCouponTest.retrieveNViaMapper(queryCoupon);
		boolean exists = false;
		for (BaseModel bm : couponList) {
			if (bm.getID() == couponCreate.getID()) {
				exists = true;
				break;
			}
		}
		Assert.assertTrue(exists, "返回的集合中不存在刚创建的优惠券");
		//
		BaseCouponTest.deleteViaMapper(couponCreate);
	}

	@Test
	public void retrieveNTest4() throws Exception {
		Shared.printTestMethodStartInfo();
		//
		Shared.caseLog("Case4:POSID大于0时，有种优惠券已过结束时间。返回数据中无此优惠券");
		Coupon coupon = BaseCouponTest.DataInput.getCoupon();
		Date now = new Date();
		coupon.setBeginDateTime(DatetimeUtil.getDate(now, -5));
		coupon.setEndDateTime(DatetimeUtil.getDate(now, -1));
		Coupon couponCreate = BaseCouponTest.createViaMapper(coupon);
		//
		Coupon queryCoupon = new Coupon();
		queryCoupon.setPosID(Shared.BIG_ID);
		queryCoupon.setPageIndex(BaseAction.PAGE_StartIndex);
		queryCoupon.setPageSize(BaseAction.PAGE_SIZE_Infinite);
		List<BaseModel> couponList = BaseCouponTest.retrieveNViaMapper(queryCoupon);
		for (BaseModel bm : couponList) {
			Assert.assertTrue(couponCreate.getID() != bm.getID(), "查到了刚刚创建状态为删除的优惠券");
		}
		//
		BaseCouponTest.deleteViaMapper(couponCreate);
	}

	@Test
	public void retrieveNTest5() throws Exception {
		Shared.printTestMethodStartInfo();
		//
		Shared.caseLog("Case5:POSID大于0时，DB中优惠券有状态正常,已终止，未到开始时间，正在有效时间，已到结束时间。返回状态正常,未到开始时间，只在有效时间的优惠券");
		// couponA:状态正常的优惠券
		Coupon couponA = BaseCouponTest.DataInput.getCoupon();
		Coupon couponCreateA = BaseCouponTest.createViaMapper(couponA);
		// couponB：状态为已终止的优惠券
		Coupon couponB = BaseCouponTest.DataInput.getCoupon();
		couponB.setStatus(Coupon.EnumCouponStatus.ECS_Expired.getIndex());
		Coupon couponCreateB = BaseCouponTest.createViaMapper(couponB);
		// couponC：状态正常，未到开始时间的优惠券
		Coupon couponC = BaseCouponTest.DataInput.getCoupon();
		Coupon couponCreateC = BaseCouponTest.createViaMapper(couponC);
		// couponD:状态正常，正在进行的优惠券
		Coupon couponD = BaseCouponTest.DataInput.getCoupon();
		Date now = new Date();
		couponD.setBeginDateTime(DatetimeUtil.getDays(now, -1));
		Coupon couponCreateD = BaseCouponTest.createViaMapper(couponD);
		// couponE：状态正常，已到结束时间的优惠券
		Coupon couponE = BaseCouponTest.DataInput.getCoupon();
		couponE.setBeginDateTime(DatetimeUtil.getDays(now, -5));
		couponE.setEndDateTime(DatetimeUtil.getDays(now, -1));
		Coupon couponCreateE = BaseCouponTest.createViaMapper(couponE);

		Coupon queryCoupon = new Coupon();
		queryCoupon.setPosID(Shared.BIG_ID);
		queryCoupon.setPageIndex(BaseAction.PAGE_StartIndex);
		queryCoupon.setPageSize(BaseAction.PAGE_SIZE_Infinite);
		List<BaseModel> couponList = BaseCouponTest.retrieveNViaMapper(queryCoupon);

		// 检查
		List<Integer> couponIDList = new ArrayList<Integer>();
		for (BaseModel bm : couponList) {
			couponIDList.add(bm.getID());
		}
		Assert.assertTrue(couponIDList.contains(couponCreateA.getID()), "状态正常的优惠券未返回");
		Assert.assertTrue(!couponIDList.contains(couponCreateB.getID()), "状态为已终止的优惠券返回了！");
		Assert.assertTrue(couponIDList.contains(couponCreateC.getID()), "状态正常，未到开始时间的优惠券未返回");
		Assert.assertTrue(couponIDList.contains(couponCreateD.getID()), "状态正常，正在进行的优惠券未返回");
		Assert.assertTrue(!couponIDList.contains(couponCreateE.getID()), "状态正常，已到结束时间的优惠券返回了！");

		BaseCouponTest.deleteViaMapper(couponCreateA);
		BaseCouponTest.deleteViaMapper(couponCreateB);
		BaseCouponTest.deleteViaMapper(couponCreateC);
		BaseCouponTest.deleteViaMapper(couponCreateD);
		BaseCouponTest.deleteViaMapper(couponCreateE);
	}

	@Test
	public void retrieveNTest6() throws Exception {
		Shared.printTestMethodStartInfo();
		//
		Shared.caseLog("Case6:POSID大于0时.一种优惠券已经被删除，但是还是在有效期间。有vip领取了这种优惠券。返回此优惠券");
		Coupon coupon = BaseCouponTest.DataInput.getCoupon();
		Date now = new Date();
		coupon.setBeginDateTime(DatetimeUtil.getDate(now, -1));
		Coupon couponCreate = BaseCouponTest.createViaMapper(coupon);
		// 领取优惠券
		CouponCode couponCode = BaseCouponCodeTest.DataInput.getCouponCode(Shared.DEFAULT_VIP_ID, couponCreate.getID());
		CouponCode couponCodeCreate = BaseCouponCodeTest.createViaMapper(couponCode);
		// 将这种优惠券删除
		BaseCouponTest.deleteViaMapper(couponCreate);
		// 查询
		Coupon queryCoupon = new Coupon();
		queryCoupon.setPosID(Shared.BIG_ID);
		queryCoupon.setPageIndex(BaseAction.PAGE_StartIndex);
		queryCoupon.setPageSize(BaseAction.PAGE_SIZE_Infinite);
		List<BaseModel> couponList = BaseCouponTest.retrieveNViaMapper(queryCoupon);
		//
		boolean exists = false;
		for (BaseModel baseModel : couponList) {
			if (baseModel.getID() == couponCreate.getID()) {
				exists = true;
				break;
			}
		}
		Assert.assertTrue(exists, "没返回vip领取的优惠券类型");

		BaseCouponCodeTest.deleteViaMapper(couponCodeCreate);
	}

	@Test
	public void retrieveNTest7() throws Exception {
		Shared.printTestMethodStartInfo();
		//
		Shared.caseLog("Case7:POSID大于0时.一种优惠券已经被删除，但是还未到开始时间。有vip领取了这种优惠券。返回此优惠券");
		Coupon coupon = BaseCouponTest.DataInput.getCoupon();
		coupon.setBeginDateTime(DatetimeUtil.addMinutes(new Date(), 1));
		Coupon couponCreate = BaseCouponTest.createViaMapper(coupon);
		// 领取优惠券
		CouponCode couponCode = BaseCouponCodeTest.DataInput.getCouponCode(Shared.DEFAULT_VIP_ID, couponCreate.getID());
		CouponCode couponCodeCreate = BaseCouponCodeTest.createViaMapper(couponCode);
		// 将这种优惠券删除
		BaseCouponTest.deleteViaMapper(couponCreate);
		// 查询
		Coupon queryCoupon = new Coupon();
		queryCoupon.setPosID(Shared.BIG_ID);
		queryCoupon.setPageIndex(BaseAction.PAGE_StartIndex);
		queryCoupon.setPageSize(BaseAction.PAGE_SIZE_Infinite);
		List<BaseModel> couponList = BaseCouponTest.retrieveNViaMapper(queryCoupon);
		//
		boolean exists = false;
		for (BaseModel baseModel : couponList) {
			if (baseModel.getID() == couponCreate.getID()) {
				exists = true;
				break;
			}
		}
		Assert.assertTrue(exists, "没返回vip领取的优惠券类型");

		BaseCouponCodeTest.deleteViaMapper(couponCodeCreate);
	}

	@Test
	public void retrieveNTest8() throws Exception {
		Shared.printTestMethodStartInfo();
		//
		Shared.caseLog("Case8:POSID大于0时。一种优惠券已经被删除，并且过了结束时间。有vip领取了这种优惠券。返回无数据");
		Coupon coupon = BaseCouponTest.DataInput.getCoupon();
		Date now = new Date();
		coupon.setBeginDateTime(DatetimeUtil.getDays(now, -5));
		coupon.setEndDateTime(DatetimeUtil.getDays(now, -1));
		Coupon couponCreate = BaseCouponTest.createViaMapper(coupon);
		// 领取优惠券
		CouponCode couponCode = BaseCouponCodeTest.DataInput.getCouponCode(Shared.DEFAULT_VIP_ID, couponCreate.getID());
		CouponCode couponCodeCreate = BaseCouponCodeTest.createViaMapper(couponCode);
		// 将这种优惠券删除
		BaseCouponTest.deleteViaMapper(couponCreate);
		// 查询
		Coupon queryCoupon = new Coupon();
		queryCoupon.setPosID(Shared.BIG_ID);
		queryCoupon.setPageIndex(BaseAction.PAGE_StartIndex);
		queryCoupon.setPageSize(BaseAction.PAGE_SIZE_Infinite);
		List<BaseModel> couponList = BaseCouponTest.retrieveNViaMapper(queryCoupon);
		//
		for (BaseModel baseModel : couponList) {
			if (baseModel.getID() == couponCreate.getID()) {
				Assert.assertTrue(false, "返回了vip领取的过期优惠券类型");
			}
		}

		BaseCouponCodeTest.deleteViaMapper(couponCodeCreate);
	}
	
	@Test
	public void retrieveNTest9() throws Exception {
		Shared.printTestMethodStartInfo();
		//
		Shared.caseLog("Case9:传iPosID=-2,bonus=-1, F_Type = -1,代表小程序请求，查询出所有优惠券(包括未开始的，不包括过期、和已删除的)");
		Coupon queryCouponCondition = BaseCouponTest.DataInput.getCoupon();
		queryCouponCondition.setPosID(Coupon.POS_ID_RetrieveNCoupon_FromMiniProgram);
		queryCouponCondition.setBonus(BaseAction.INVALID_ID);
		queryCouponCondition.setType(BaseAction.INVALID_ID);
		queryCouponCondition.setPageIndex(BaseAction.PAGE_StartIndex);
		queryCouponCondition.setPageSize(BaseAction.PAGE_SIZE_Infinite);
		List<BaseModel> couponList = BaseCouponTest.retrieveNViaMapper(queryCouponCondition);
		Assert.assertTrue(couponList.size() > 0, "查询优惠券失败");
	}
	
	@Test
	public void retrieveNTest10() throws Exception {
		Shared.printTestMethodStartInfo();
		//
		Shared.caseLog("Case10:传iPosID=-2,bonus=-1, F_Type = 0,代表小程序请求，查询出所有现金券(包括未开始的，不包括过期、和已删除的)");
		//
		Coupon queryCouponCondition = BaseCouponTest.DataInput.getCoupon();
		queryCouponCondition.setPosID(Coupon.POS_ID_RetrieveNCoupon_FromMiniProgram);
		queryCouponCondition.setBonus(BaseAction.INVALID_ID);
		queryCouponCondition.setType(Coupon.EnumCouponType.ECT_Cash.getIndex());
		queryCouponCondition.setPageIndex(BaseAction.PAGE_StartIndex);
		queryCouponCondition.setPageSize(BaseAction.PAGE_SIZE_Infinite);
		List<BaseModel> couponListOld = BaseCouponTest.retrieveNViaMapper(queryCouponCondition);
		// 创建两个现金券，一个优惠券用于测试
		Coupon couponCash1 = BaseCouponTest.DataInput.getCoupon();
		couponCash1.setBeginDateTime(DatetimeUtil.getDays(new Date(), -1));
		couponCash1.setType(Coupon.EnumCouponType.ECT_Cash.getIndex());
		Coupon couponCreated1 = BaseCouponTest.createViaMapper(couponCash1);
		//
		Coupon couponCash2 = BaseCouponTest.DataInput.getCoupon();
		couponCash2.setBeginDateTime(DatetimeUtil.getDays(new Date(), -1));
		couponCash2.setType(Coupon.EnumCouponType.ECT_Cash.getIndex());
		Coupon couponCreated2 = BaseCouponTest.createViaMapper(couponCash2);
		//
		Coupon couponDiscount3 = BaseCouponTest.DataInput.getCoupon();
		couponDiscount3.setBeginDateTime(DatetimeUtil.getDays(new Date(), -1));
		couponDiscount3.setType(Coupon.EnumCouponType.ECT_Discount.getIndex());
		Coupon couponCreated3 = BaseCouponTest.createViaMapper(couponDiscount3);
		List<BaseModel> couponList = BaseCouponTest.retrieveNViaMapper(queryCouponCondition);
		boolean exists1 = false;
		boolean exists2 = false;
		boolean exists3 = false;
		for(int i = 0; i<couponList.size(); i++) {
			if(couponCreated1.getID() == couponList.get(i).getID()) {
				exists1 = true;
			}
			else if (couponCreated2.getID() == couponList.get(i).getID()) {
				exists2 = true;
			}
			else if (couponCreated3.getID() == couponList.get(i).getID()) {
				exists3 = true;
			}
		}
		Assert.assertTrue(exists1 && exists2 && !exists3, "没有返回创建的现金券");
		Assert.assertTrue(couponList.size() == couponListOld.size() + 2, "返回的总数不正确");
		// 删除测试数据
		BaseCouponTest.deleteViaMapper(couponCreated1);
		BaseCouponTest.deleteViaMapper(couponCreated2);
		BaseCouponTest.deleteViaMapper(couponCreated3);
	}
	
	@Test
	public void retrieveNTest11() throws Exception {
		Shared.printTestMethodStartInfo();
		//
		Shared.caseLog("Case11:传iPosID=-2,bonus=-1, F_Type = 1,代表小程序请求，查询出所有折扣券(包括未开始的，不包括过期、和已删除的)");
		//
		Coupon queryCouponCondition = BaseCouponTest.DataInput.getCoupon();
		queryCouponCondition.setPosID(Coupon.POS_ID_RetrieveNCoupon_FromMiniProgram);
		queryCouponCondition.setBonus(BaseAction.INVALID_ID);
		queryCouponCondition.setType(Coupon.EnumCouponType.ECT_Discount.getIndex());
		queryCouponCondition.setPageIndex(BaseAction.PAGE_StartIndex);
		queryCouponCondition.setPageSize(BaseAction.PAGE_SIZE_Infinite);
		List<BaseModel> couponListOld = BaseCouponTest.retrieveNViaMapper(queryCouponCondition);
		// 创建2张现金券，一张折扣券用于测试
		Coupon couponCash1 = BaseCouponTest.DataInput.getCoupon();
		couponCash1.setBeginDateTime(DatetimeUtil.getDays(new Date(), -1));
		couponCash1.setType(Coupon.EnumCouponType.ECT_Cash.getIndex());
		Coupon couponCreated1 = BaseCouponTest.createViaMapper(couponCash1);
		//
		Coupon couponCash2 = BaseCouponTest.DataInput.getCoupon();
		couponCash2.setBeginDateTime(DatetimeUtil.getDays(new Date(), -1));
		couponCash2.setType(Coupon.EnumCouponType.ECT_Cash.getIndex());
		Coupon couponCreated2 = BaseCouponTest.createViaMapper(couponCash2);
		//
		Coupon couponDiscount3 = BaseCouponTest.DataInput.getCoupon();
		couponDiscount3.setBeginDateTime(DatetimeUtil.getDays(new Date(), -1));
		couponDiscount3.setType(Coupon.EnumCouponType.ECT_Discount.getIndex());
		Coupon couponCreated3 = BaseCouponTest.createViaMapper(couponDiscount3);
		List<BaseModel> couponList = BaseCouponTest.retrieveNViaMapper(queryCouponCondition);
		boolean exists1 = false;
		boolean exists2 = false;
		boolean exists3 = false;
		for(int i = 0; i<couponList.size(); i++) {
			if(couponCreated1.getID() == couponList.get(i).getID()) {
				exists1 = true;
			}
			else if (couponCreated2.getID() == couponList.get(i).getID()) {
				exists2 = true;
			}
			else if (couponCreated3.getID() == couponList.get(i).getID()) {
				exists3 = true;
			}
		}
		Assert.assertTrue(!exists1 && !exists2 && exists3, "没有返回创建的折扣券");
		Assert.assertTrue(couponList.size() == couponListOld.size() + 1, "返回的总数不正确");
		// 删除测试数据
		BaseCouponTest.deleteViaMapper(couponCreated1);
		BaseCouponTest.deleteViaMapper(couponCreated2);
		BaseCouponTest.deleteViaMapper(couponCreated3);
	}
	
	@Test
	public void retrieveNTest12() throws Exception {
		Shared.printTestMethodStartInfo();
		//
		Shared.caseLog("Case12:传iPosID=-2,bonus=0, F_Type = -1,代表小程序请求，查询出所有积分为0的优惠券(包括未开始的，不包括过期、和已删除的)");
		//
		Coupon queryCouponCondition = BaseCouponTest.DataInput.getCoupon();
		queryCouponCondition.setPosID(Coupon.POS_ID_RetrieveNCoupon_FromMiniProgram);
		queryCouponCondition.setBonus(Coupon.BONUS_RetrieveNCoupon_FromMiniProgram);
		queryCouponCondition.setType(BaseAction.INVALID_ID);
		queryCouponCondition.setPageIndex(BaseAction.PAGE_StartIndex);
		queryCouponCondition.setPageSize(BaseAction.PAGE_SIZE_Infinite);
		List<BaseModel> couponListOld = BaseCouponTest.retrieveNViaMapper(queryCouponCondition);
		// 创建1张积分等于0，2张积分大于0的优惠券
		Coupon couponCash1 = BaseCouponTest.DataInput.getCoupon();
		couponCash1.setBonus(0); // 积分等于0
		couponCash1.setBeginDateTime(DatetimeUtil.getDays(new Date(), -1));
		couponCash1.setType(Coupon.EnumCouponType.ECT_Cash.getIndex());
		Coupon couponCreated1 = BaseCouponTest.createViaMapper(couponCash1);
		//
		Coupon couponCash2 = BaseCouponTest.DataInput.getCoupon();
		couponCash2.setBonus(1); // 积分大于0
		couponCash2.setBeginDateTime(DatetimeUtil.getDays(new Date(), -1));
		couponCash2.setType(Coupon.EnumCouponType.ECT_Cash.getIndex());
		Coupon couponCreated2 = BaseCouponTest.createViaMapper(couponCash2);
		//
		Coupon couponDiscount3 = BaseCouponTest.DataInput.getCoupon();
		couponDiscount3.setBonus(1); // 积分大于0
		couponDiscount3.setBeginDateTime(DatetimeUtil.getDays(new Date(), -1));
		couponDiscount3.setType(Coupon.EnumCouponType.ECT_Discount.getIndex());
		Coupon couponCreated3 = BaseCouponTest.createViaMapper(couponDiscount3);
		List<BaseModel> couponList = BaseCouponTest.retrieveNViaMapper(queryCouponCondition);
		boolean exists1 = false;
		boolean exists2 = false;
		boolean exists3 = false;
		for(int i = 0; i<couponList.size(); i++) {
			if(couponCreated1.getID() == couponList.get(i).getID()) {
				exists1 = true;
			}
			else if (couponCreated2.getID() == couponList.get(i).getID()) {
				exists2 = true;
			}
			else if (couponCreated3.getID() == couponList.get(i).getID()) {
				exists3 = true;
			}
		}
		Assert.assertTrue(exists1 && !exists2 && !exists3, "返回的优惠券不正确");
		Assert.assertTrue(couponList.size() == couponListOld.size() + 1, "返回的总数不正确");
		// 删除测试数据
		BaseCouponTest.deleteViaMapper(couponCreated1);
		BaseCouponTest.deleteViaMapper(couponCreated2);
		BaseCouponTest.deleteViaMapper(couponCreated3);
	}
	
	@Test
	public void retrieveNTest13() throws Exception {
		Shared.printTestMethodStartInfo();
		//
		Shared.caseLog("Case13:传iPosID=-2,bonus大于0, F_Type = -1,代表小程序请求，查询出所有积分大于0的优惠券(包括未开始的，不包括过期、和已删除的)");
		//
		Coupon queryCouponCondition = BaseCouponTest.DataInput.getCoupon();
		queryCouponCondition.setPosID(Coupon.POS_ID_RetrieveNCoupon_FromMiniProgram);
		queryCouponCondition.setBonus(1);
		queryCouponCondition.setType(BaseAction.INVALID_ID);
		queryCouponCondition.setPageIndex(BaseAction.PAGE_StartIndex);
		queryCouponCondition.setPageSize(BaseAction.PAGE_SIZE_Infinite);
		List<BaseModel> couponListOld = BaseCouponTest.retrieveNViaMapper(queryCouponCondition);
		// 创建1张积分等于0，2张积分大于0的优惠券
		Coupon couponCash1 = BaseCouponTest.DataInput.getCoupon();
		couponCash1.setBonus(0); // 积分等于0
		couponCash1.setBeginDateTime(DatetimeUtil.getDays(new Date(), -1));
		couponCash1.setType(Coupon.EnumCouponType.ECT_Cash.getIndex());
		Coupon couponCreated1 = BaseCouponTest.createViaMapper(couponCash1);
		//
		Coupon couponCash2 = BaseCouponTest.DataInput.getCoupon();
		couponCash2.setBonus(1); // 积分大于0
		couponCash2.setBeginDateTime(DatetimeUtil.getDays(new Date(), -1));
		couponCash2.setType(Coupon.EnumCouponType.ECT_Cash.getIndex());
		Coupon couponCreated2 = BaseCouponTest.createViaMapper(couponCash2);
		//
		Coupon couponDiscount3 = BaseCouponTest.DataInput.getCoupon();
		couponDiscount3.setBonus(1); // 积分大于0
		couponDiscount3.setBeginDateTime(DatetimeUtil.getDays(new Date(), -1));
		couponDiscount3.setType(Coupon.EnumCouponType.ECT_Discount.getIndex());
		Coupon couponCreated3 = BaseCouponTest.createViaMapper(couponDiscount3);
		List<BaseModel> couponList = BaseCouponTest.retrieveNViaMapper(queryCouponCondition);
		boolean exists1 = false;
		boolean exists2 = false;
		boolean exists3 = false;
		for(int i = 0; i<couponList.size(); i++) {
			if(couponCreated1.getID() == couponList.get(i).getID()) {
				exists1 = true;
			}
			else if (couponCreated2.getID() == couponList.get(i).getID()) {
				exists2 = true;
			}
			else if (couponCreated3.getID() == couponList.get(i).getID()) {
				exists3 = true;
			}
		}
		Assert.assertTrue(!exists1 && exists2 && exists3, "返回的优惠券不正确");
		Assert.assertTrue(couponList.size() == couponListOld.size() + 2, "返回的总数不正确");
		// 删除测试数据
		BaseCouponTest.deleteViaMapper(couponCreated1);
		BaseCouponTest.deleteViaMapper(couponCreated2);
		BaseCouponTest.deleteViaMapper(couponCreated3);
	}
	
	
	@Test
	public void retrieveNTest14() throws Exception {
		Shared.printTestMethodStartInfo();
		//
		Shared.caseLog("Case14:传iPosID=-2,bonus=-1, F_Type = -1,代表小程序请求，查询出的优惠券不包括过期和已删除的");
		//
		Coupon queryCouponCondition = BaseCouponTest.DataInput.getCoupon();
		queryCouponCondition.setPosID(Coupon.POS_ID_RetrieveNCoupon_FromMiniProgram);
		queryCouponCondition.setBonus(BaseAction.INVALID_ID);
		queryCouponCondition.setType(BaseAction.INVALID_ID);
		queryCouponCondition.setPageIndex(BaseAction.PAGE_StartIndex);
		queryCouponCondition.setPageSize(BaseAction.PAGE_SIZE_Infinite);
		List<BaseModel> couponListOld = BaseCouponTest.retrieveNViaMapper(queryCouponCondition);
		// 创建1张已过期的优惠券
		Coupon couponCash2 = BaseCouponTest.DataInput.getCoupon();
		couponCash2.setBeginDateTime(DatetimeUtil.getDays(new Date(), -5));
		couponCash2.setEndDateTime(DatetimeUtil.getDays(new Date(), -1));
		couponCash2.setType(Coupon.EnumCouponType.ECT_Cash.getIndex());
		Coupon couponCreated2 = BaseCouponTest.createViaMapper(couponCash2);
		// 创建1张已删除的优惠券
		Coupon couponDiscount3 = BaseCouponTest.DataInput.getCoupon();
		couponDiscount3.setStatus(Coupon.EnumCouponStatus.ECS_Expired.getIndex());
		couponDiscount3.setBeginDateTime(DatetimeUtil.getDays(new Date(), -1));
		couponDiscount3.setType(Coupon.EnumCouponType.ECT_Discount.getIndex());
		Coupon couponCreated3 = BaseCouponTest.createViaMapper(couponDiscount3);
		List<BaseModel> couponList = BaseCouponTest.retrieveNViaMapper(queryCouponCondition);
		boolean exists2 = false;
		boolean exists3 = false;
		for(int i = 0; i<couponList.size(); i++) {
			if (couponCreated2.getID() == couponList.get(i).getID()) {
				exists2 = true;
			}
			else if (couponCreated3.getID() == couponList.get(i).getID()) {
				exists3 = true;
			}
		}
		Assert.assertTrue(!exists2 && !exists3, "返回的优惠券不正确");
		Assert.assertTrue(couponList.size() == couponListOld.size(), "返回的总数不正确");
		// 删除测试数据
		BaseCouponTest.deleteViaMapper(couponCreated2);
		BaseCouponTest.deleteViaMapper(couponCreated3);
	}
	
	@Test
	public void retrieveNTest15() throws Exception {
		Shared.printTestMethodStartInfo();
		//
		Shared.caseLog("Case14:传iPosID=-2,bonus=-1, F_Type = -1,代表小程序请求，查询出的优惠券包括未开始的");
		//
		Coupon queryCouponCondition = BaseCouponTest.DataInput.getCoupon();
		queryCouponCondition.setPosID(Coupon.POS_ID_RetrieveNCoupon_FromMiniProgram);
		queryCouponCondition.setBonus(BaseAction.INVALID_ID);
		queryCouponCondition.setType(BaseAction.INVALID_ID);
		queryCouponCondition.setPageIndex(BaseAction.PAGE_StartIndex);
		queryCouponCondition.setPageSize(BaseAction.PAGE_SIZE_Infinite);
		List<BaseModel> couponListOld = BaseCouponTest.retrieveNViaMapper(queryCouponCondition);
		// 创建1张未开始的优惠券
		Coupon couponCash1 = BaseCouponTest.DataInput.getCoupon();
		couponCash1.setBeginDateTime(DatetimeUtil.getDays(new Date(), 1));
		couponCash1.setEndDateTime(DatetimeUtil.getDays(new Date(), 5));
		couponCash1.setType(Coupon.EnumCouponType.ECT_Cash.getIndex());
		Coupon couponCreated1 = BaseCouponTest.createViaMapper(couponCash1);
		List<BaseModel> couponList = BaseCouponTest.retrieveNViaMapper(queryCouponCondition);
		boolean exists1 = false;
		for(int i = 0; i<couponList.size(); i++) {
			if(couponCreated1.getID() == couponList.get(i).getID()) {
				exists1 = true;
			}
		}
		Assert.assertTrue(exists1, "返回的优惠券不正确");
		Assert.assertTrue(couponList.size() == couponListOld.size() + 1, "返回的总数不正确");
		// 删除测试数据
		BaseCouponTest.deleteViaMapper(couponCreated1);
	}

	@Test
	public void deleteTest1() throws Exception {
		Shared.printTestMethodStartInfo();
		//
		Shared.caseLog("Case1:正常删除优惠券");
		//
		Coupon coupon = BaseCouponTest.DataInput.getCoupon();
		Coupon couponCreate = BaseCouponTest.createViaMapper(coupon);
		//
		BaseCouponTest.deleteViaMapper(couponCreate);
	}

}
