package com.bx.erp.test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.test.context.web.WebAppConfiguration;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.CommodityType;
import com.bx.erp.model.Coupon;
import com.bx.erp.model.CouponCode;
import com.bx.erp.model.CouponScope.EnumCouponScope;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.commodity.Commodity;
import com.bx.erp.util.DatetimeUtil;

import net.sf.json.JSONObject;

@WebAppConfiguration
public class CouponActionTest extends BaseActionTest {
	@BeforeClass
	public void setup() {
		super.setUp();
	}

	@AfterClass
	public void tearDown() {
		super.tearDown();
	}

	@Test
	public void testRetrieveNCase1() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case1: web查找全部优惠券");

		Coupon coupon = BaseCouponTest.DataInput.getCoupon();
		Coupon couponCreate = BaseCouponTest.createViaMapper(coupon);

		Assert.assertTrue(BaseCouponTest.retrieveNViaAction(couponCreate, sessionBoss, mvc).size() > 0, "查找优惠券失败");

		BaseCouponTest.deleteViaMapper(couponCreate);
	}

	@Test
	public void testRetrieveNCase2() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case2:pos查找。有种优惠券已经终止。返回数据中无此优惠券");

		Coupon coupon = BaseCouponTest.DataInput.getCoupon();
		coupon.setStatus(Coupon.EnumCouponStatus.ECS_Expired.getIndex());
		Coupon couponCreate = BaseCouponTest.createViaMapper(coupon);
		//
		Coupon queryCoupon = new Coupon();
		queryCoupon.setPosID(Shared.BIG_ID);
		queryCoupon.setPageIndex(BaseAction.PAGE_StartIndex);
		queryCoupon.setPageSize(BaseAction.PAGE_SIZE_Infinite);
		List<BaseModel> couponList = BaseCouponTest.retrieveNViaAction(queryCoupon, Shared.getPosLoginSession(mvc, Shared.POS_2_ID), mvc);
		for (BaseModel bm : couponList) {
			Assert.assertTrue(couponCreate.getID() != bm.getID(), "查到了刚刚创建状态为删除的优惠券");
		}
	}

	@Test
	public void testRetrieveNCase3() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case3:pos查找。有种优惠券已开始生效,还未到结束时间.返回的优惠券集合中有此优惠券");
		Coupon coupon = BaseCouponTest.DataInput.getCoupon();
		coupon.setBeginDateTime(DatetimeUtil.getDays(new Date(), -1));
		Coupon couponCreate = BaseCouponTest.createViaMapper(coupon);
		//
		Coupon queryCoupon = new Coupon();
		queryCoupon.setPosID(Shared.BIG_ID);
		queryCoupon.setPageIndex(BaseAction.PAGE_StartIndex);
		queryCoupon.setPageSize(BaseAction.PAGE_SIZE_Infinite);
		List<BaseModel> couponList = BaseCouponTest.retrieveNViaAction(queryCoupon, Shared.getPosLoginSession(mvc, Shared.POS_2_ID), mvc);
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
	public void testRetrieveNCase4() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case4:pos查找。有种优惠券已过结束时间。返回数据中无此优惠券");
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
		List<BaseModel> couponList = BaseCouponTest.retrieveNViaAction(queryCoupon, Shared.getPosLoginSession(mvc, Shared.POS_2_ID), mvc);
		for (BaseModel bm : couponList) {
			Assert.assertTrue(couponCreate.getID() != bm.getID(), "查到了刚刚创建状态为删除的优惠券");
		}
		//
		BaseCouponTest.deleteViaMapper(couponCreate);
	}

	@Test
	public void testRetrieveNCase5() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case5:pos查找。DB中优惠券有状态正常,已终止，未到开始时间，正在有效时间，已到结束时间。返回状态正常,未到开始时间，只在有效时间的优惠券");
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
		List<BaseModel> couponList = BaseCouponTest.retrieveNViaAction(queryCoupon, Shared.getPosLoginSession(mvc, Shared.POS_2_ID), mvc);

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
	public void testRetrieveNCase6() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case6:一种优惠券已经被删除，但是还是在有效期间。有vip领取了这种优惠券。返回此优惠券");
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
		List<BaseModel> couponList = BaseCouponTest.retrieveNViaAction(queryCoupon, Shared.getPosLoginSession(mvc, Shared.POS_2_ID), mvc);
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
	public void testRetrieveNCase7() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case7:POSID大于0时.一种优惠券已经被删除，但是还未到开始时间。有vip领取了这种优惠券。返回此优惠券");
		Coupon coupon = BaseCouponTest.DataInput.getCoupon();
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
		List<BaseModel> couponList = BaseCouponTest.retrieveNViaAction(queryCoupon, Shared.getPosLoginSession(mvc, Shared.POS_2_ID), mvc);
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
	public void testRetrieveNCase8() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case8:pos查找。一种优惠券已经被删除，并且过了结束时间。有vip领取了这种优惠券。返回无数据");
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
		List<BaseModel> couponList = BaseCouponTest.retrieveNViaAction(queryCoupon, Shared.getPosLoginSession(mvc, Shared.POS_2_ID), mvc);
		//
		for (BaseModel baseModel : couponList) {
			if (baseModel.getID() == couponCreate.getID()) {
				Assert.assertTrue(false, "返回了vip领取的过期优惠券类型");
			}
		}

		BaseCouponCodeTest.deleteViaMapper(couponCodeCreate);
	}
	
	@Test
	public void testRetrieveNCase9() throws Exception {
		Shared.printTestMethodStartInfo();
		//
		Shared.caseLog("Case9:传posID=-2,bonus=-1, type = -1,代表小程序请求，查询出所有优惠券(包括未开始的，不包括过期、和已删除的)");
		Coupon coupon = BaseCouponTest.DataInput.getCoupon();
		coupon.setPosID(Coupon.POS_ID_RetrieveNCoupon_FromMiniProgram);
		coupon.setBonus(BaseAction.INVALID_ID);
		coupon.setType(BaseAction.INVALID_ID);
		coupon.setPageIndex(BaseAction.PAGE_StartIndex);
		coupon.setPageSize(BaseAction.PAGE_SIZE_Infinite);
		List<BaseModel> couponList = BaseCouponTest.retrieveNViaAction(coupon, sessionBoss, mvc);
		Assert.assertTrue(couponList.size() > 0, "查询优惠券失败");
	}
	
	@Test
	public void testRetrieveNCase10() throws Exception {
		Shared.printTestMethodStartInfo();
		//
		Shared.caseLog("Case10:传posID=-2,bonus=-1, type = 0,代表小程序请求，查询出所有现金券(包括未开始的，不包括过期、和已删除的)");
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
		List<BaseModel> couponList = BaseCouponTest.retrieveNViaAction(queryCouponCondition, sessionBoss, mvc);
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
	public void testRetrieveNCase11() throws Exception {
		Shared.printTestMethodStartInfo();
		//
		Shared.caseLog("Case11:传posID=-2,bonus=-1, type = 1,代表小程序请求，查询出所有折扣券(包括未开始的，不包括过期、和已删除的)");
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
		List<BaseModel> couponList = BaseCouponTest.retrieveNViaAction(queryCouponCondition, sessionBoss, mvc);
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
	public void testRetrieveNCase12() throws Exception {
		Shared.printTestMethodStartInfo();
		//
		Shared.caseLog("Case12:传posID=-2,bonus=0, type = -1,代表小程序请求，查询出所有积分为0的优惠券(包括未开始的，不包括过期、和已删除的)");
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
		List<BaseModel> couponList = BaseCouponTest.retrieveNViaAction(queryCouponCondition, sessionBoss, mvc);
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
	public void testRetrieveNCase13() throws Exception {
		Shared.printTestMethodStartInfo();
		//
		Shared.caseLog("Case13:传posID=-2,bonus大于0, type = -1,代表小程序请求，查询出所有积分大于0的优惠券(包括未开始的，不包括过期、和已删除的)");
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
		List<BaseModel> couponList = BaseCouponTest.retrieveNViaAction(queryCouponCondition, sessionBoss, mvc);
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
	public void testRetrieveNCase14() throws Exception {
		Shared.printTestMethodStartInfo();
		//
		Shared.caseLog("Case14:传posID=-2,bonus=-1, type = -1,代表小程序请求，查询出的优惠券不包括过期和已删除的");
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
		List<BaseModel> couponList = BaseCouponTest.retrieveNViaAction(queryCouponCondition, sessionBoss, mvc);
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
	public void testRetrieveNCase15() throws Exception {
		Shared.printTestMethodStartInfo();
		//
		Shared.caseLog("Case15:传posID=-2,bonus=-1, type = -1,代表小程序请求，查询出的优惠券包括未开始的");
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
		List<BaseModel> couponList = BaseCouponTest.retrieveNViaAction(queryCouponCondition, sessionBoss, mvc);
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
	public void testCreateCase1() throws Exception {
		Shared.printTestMethodStartInfo();
		//
		Shared.caseLog("case1: 创建一种优惠券");
		//
		Coupon couponGet = BaseCouponTest.DataInput.getCoupon();
		Coupon couponCreate = BaseCouponTest.createViaAction(couponGet, sessionBoss, mvc, EnumErrorCode.EC_NoError);
		// 删除测试数据
		BaseCouponTest.deleteViaMapper(couponCreate);
	}

	@Test
	public void testCreateCase2() throws Exception {
		Shared.printTestMethodStartInfo();
		//
		Shared.caseLog("case2: 无权限");
		//
		Coupon couponGet = BaseCouponTest.DataInput.getCoupon();
		BaseCouponTest.createViaAction(couponGet, Shared.getStaffLoginSession(mvc, BaseAction.ACCOUNT_Phone_PreSale), mvc, EnumErrorCode.EC_NoPermission);
	}

	@Test
	public void testCreateCase3() throws Exception {
		Shared.printTestMethodStartInfo();
		//
		Shared.caseLog("case3: 限定参与优惠券兑换的商品为单品（即普通商品），如果传进来的都是普通商品，则创建成功");
		// 创建两个普通商品
		Commodity commodity = BaseCommodityTest.DataInput.getCommodity(CommodityType.EnumCommodityType.ECT_Normal.getIndex());
		Commodity commodityCreate51 = BaseCommodityTest.createCommodityViaAction(commodity, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		//
		Commodity commodity2 = BaseCommodityTest.DataInput.getCommodity(CommodityType.EnumCommodityType.ECT_Normal.getIndex());
		Commodity commodityCreate52 = BaseCommodityTest.createCommodityViaAction(commodity2, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		String string5 = commodityCreate51.getID() + "," + commodityCreate52.getID();
		//
		Coupon couponGet = BaseCouponTest.DataInput.getCoupon();
		couponGet.setCommodityIDs(string5);
		couponGet.setScope(EnumCouponScope.ECS_SpecifiedCommodities.getIndex());
		Coupon couponCreate = BaseCouponTest.createViaAction(couponGet, sessionBoss, mvc, EnumErrorCode.EC_NoError);
		BaseCouponTest.deleteViaAction(couponCreate, sessionBoss, mvc, EnumErrorCode.EC_NoError);
//		商品有优惠券范围依赖，不能删除
//		BaseCommodityTest.deleteCommodityViaMapper(commodityCreate51, EnumErrorCode.EC_NoError);
//		BaseCommodityTest.deleteCommodityViaMapper(commodityCreate52, EnumErrorCode.EC_NoError);
	}

	@Test
	public void testCreateCase4() throws Exception {
		Shared.printTestMethodStartInfo();
		//
		Shared.caseLog("case4:限定参与优惠券兑换的商品为单品（即普通商品），如果传入的商品一部分是普通商品，一部分是多包装商品");
		// 1、创建一个普通商品
		Commodity commodity = BaseCommodityTest.DataInput.getCommodity(CommodityType.EnumCommodityType.ECT_Normal.getIndex());
		Commodity commodityCreate61 = BaseCommodityTest.createCommodityViaAction(commodity, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		// 2、创建一个多包装商品
		Commodity commodity2 = BaseCommodityTest.DataInput.getCommodity(CommodityType.EnumCommodityType.ECT_MultiPackaging.getIndex());
		commodity2.setDefaultValueToCreate(BaseBO.CASE_Commodity_CreateMultiPackaging);
		commodity2.setRefCommodityID(commodityCreate61.getID());
		commodity2.setRefCommodityMultiple(2);
		commodity2.setMultiPackagingInfo("123456789" + ";1;1;1;8;8;" + commodity2.getBarcodes() + ";");
		Commodity commodityCreate62 = BaseCommodityTest.createCommodityViaMapper(commodity2, BaseBO.CASE_Commodity_CreateMultiPackaging);
		//
		String string6 = commodityCreate61.getID() + "," + commodityCreate62.getID();
		Coupon couponGet = BaseCouponTest.DataInput.getCoupon();
		couponGet.setCommodityIDs(string6);
		couponGet.setScope(EnumCouponScope.ECS_SpecifiedCommodities.getIndex());
		Coupon couponCreate = BaseCouponTest.createViaAction(couponGet, sessionBoss, mvc, EnumErrorCode.EC_Hack);
		Assert.assertTrue(couponCreate == null, "预期结果是返回null");
	}

	@Test
	public void testCreateCase5() throws Exception {
		Shared.printTestMethodStartInfo();
		//
		Shared.caseLog("case5:限定参与优惠券兑换的商品为单品（即普通商品），如果传入的商品是服务商品");
		// 1、创建一个服务商品
		Commodity commodity = BaseCommodityTest.DataInput.getCommodity(CommodityType.EnumCommodityType.ECT_Service.getIndex());
		Commodity commodityCreate13 = BaseCommodityTest.createCommodityViaAction(commodity, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		//
		String string6 = commodityCreate13 + "";
		Coupon couponGet = BaseCouponTest.DataInput.getCoupon();
		couponGet.setCommodityIDs(string6);
		couponGet.setScope(EnumCouponScope.ECS_SpecifiedCommodities.getIndex());
		Coupon couponCreate = BaseCouponTest.createViaAction(couponGet, sessionBoss, mvc, EnumErrorCode.EC_Hack);
		Assert.assertTrue(couponCreate == null, "预期结果是返回null");
	}

	@Test
	public void testCreateCase6() throws Exception {
		Shared.printTestMethodStartInfo();
		//
		Shared.caseLog("case6:限定参与优惠券兑换的商品为单品（即普通商品），如果传入的商品ID为null");
		Coupon couponGet = BaseCouponTest.DataInput.getCoupon();
		couponGet.setCommodityIDs(null);
		couponGet.setScope(EnumCouponScope.ECS_SpecifiedCommodities.getIndex());
		Coupon couponCreate = BaseCouponTest.createViaAction(couponGet, sessionBoss, mvc, EnumErrorCode.EC_Hack);
		Assert.assertTrue(couponCreate == null, "预期结果是返回null");
	}

	@Test
	public void testCreateCase7() throws Exception {
		Shared.printTestMethodStartInfo();
		//
		Shared.caseLog("case7:限定参与优惠券兑换的商品为单品（即普通商品），如果传入的商品ID为空串");
		Coupon couponGet = BaseCouponTest.DataInput.getCoupon();
		couponGet.setCommodityIDs("");
		couponGet.setScope(EnumCouponScope.ECS_SpecifiedCommodities.getIndex());
		Coupon couponCreate = BaseCouponTest.createViaAction(couponGet, sessionBoss, mvc, EnumErrorCode.EC_Hack);
		Assert.assertTrue(couponCreate == null, "预期结果是返回null");
	}

	@Test
	public void testCreateCase8() throws Exception {
		Shared.printTestMethodStartInfo();
		//
		Shared.caseLog("case8: 限定参与优惠券兑换的商品为单品（即普通商品），如果传进来的都是普通商品，但是单品是已删除的");
		// 已删除的商品
		Commodity commodity = BaseCommodityTest.DataInput.getCommodity(CommodityType.EnumCommodityType.ECT_Combination.getIndex());
		String subCommodityInfo = JSONObject.fromObject(commodity).toString();
		commodity.setSubCommodityInfo(subCommodityInfo);
		Commodity commodityCreate16 = BaseCommodityTest.createCommodityViaAction(commodity, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		BaseCommodityTest.deleteCommodityViaMapper(commodityCreate16, EnumErrorCode.EC_NoError);
		// commodityCreate16.setStatus(2); // TODO 疑惑？为了模仿Pos？
		//
		Coupon couponGet = BaseCouponTest.DataInput.getCoupon();
		couponGet.setCommodityIDs(String.valueOf(commodityCreate16.getID()));
		couponGet.setScope(EnumCouponScope.ECS_SpecifiedCommodities.getIndex());
		Coupon couponCreate = BaseCouponTest.createViaAction(couponGet, sessionBoss, mvc, EnumErrorCode.EC_NoSuchData);
		Assert.assertTrue(couponCreate == null, "预期结果是返回null");
	}

	@Test
	public void testCreateCase9() throws Exception {
		Shared.printTestMethodStartInfo();
		//
		Shared.caseLog("case9:限定参与优惠券兑换的商品为单品（即普通商品），如果传入的商品ID不合法多个逗号");
		Coupon couponGet = BaseCouponTest.DataInput.getCoupon();
		couponGet.setCommodityIDs("1,,2");
		couponGet.setScope(EnumCouponScope.ECS_SpecifiedCommodities.getIndex());
		Coupon couponCreate = BaseCouponTest.createViaAction(couponGet, sessionBoss, mvc, EnumErrorCode.EC_Hack);
		Assert.assertTrue(couponCreate == null, "预期结果是返回null");
	}

	@Test
	public void testCreateCase10() throws Exception {
		Shared.printTestMethodStartInfo();
		//
		Shared.caseLog("case10:限定参与优惠券兑换的商品为单品（即普通商品），如果传入的商品ID有英文");
		Coupon couponGet = BaseCouponTest.DataInput.getCoupon();
		couponGet.setCommodityIDs("1,2,a");
		couponGet.setScope(EnumCouponScope.ECS_SpecifiedCommodities.getIndex());
		Coupon couponCreate = BaseCouponTest.createViaAction(couponGet, sessionBoss, mvc, EnumErrorCode.EC_Hack);
		Assert.assertTrue(couponCreate == null, "预期结果是返回null");
	}

	@Test
	public void testCreateCase11() throws Exception {
		Shared.printTestMethodStartInfo();
		//
		Shared.caseLog("case11:限定参与优惠券兑换的商品为单品（即普通商品），如果传入的商品ID有中文");
		Coupon couponGet = BaseCouponTest.DataInput.getCoupon();
		couponGet.setCommodityIDs("1,2,商");
		couponGet.setScope(EnumCouponScope.ECS_SpecifiedCommodities.getIndex());
		Coupon couponCreate = BaseCouponTest.createViaAction(couponGet, sessionBoss, mvc, EnumErrorCode.EC_Hack);
		Assert.assertTrue(couponCreate == null, "预期结果是返回null");
	}

	@Test
	public void testCreateCase12() throws Exception {
		Shared.printTestMethodStartInfo();
		//
		Shared.caseLog("case11:限定参与优惠券兑换的商品为单品（即普通商品），如果传入的商品ID有重复,后端会进行去重");
		// 创建两个普通商品
		Commodity commodity = BaseCommodityTest.DataInput.getCommodity(CommodityType.EnumCommodityType.ECT_Normal.getIndex());
		Commodity commodityCreated = BaseCommodityTest.createCommodityViaAction(commodity, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		Coupon couponGet = BaseCouponTest.DataInput.getCoupon();
		couponGet.setCommodityIDs(commodityCreated.getID() + "," + commodityCreated.getID());
		couponGet.setScope(EnumCouponScope.ECS_SpecifiedCommodities.getIndex());
		Coupon couponCreate = BaseCouponTest.createViaAction(couponGet, sessionBoss, mvc, EnumErrorCode.EC_NoError);
		// 删除测试数据
		BaseCouponTest.deleteViaMapper(couponCreate);
//		商品有优惠券范围依赖，不能删除
//		BaseCommodityTest.deleteCommodityViaMapper(commodityCreated, EnumErrorCode.EC_NoError);
	}

	@Test
	public void testDeleteCase1() throws Exception {
		Shared.printTestMethodStartInfo();
		//
		Shared.caseLog("case1: 删除一种优惠券");
		//
		Coupon couponGet = BaseCouponTest.DataInput.getCoupon();
		Coupon couponCreate = BaseCouponTest.createViaAction(couponGet, sessionBoss, mvc, EnumErrorCode.EC_NoError);
		BaseCouponTest.deleteViaAction(couponCreate, sessionBoss, mvc, EnumErrorCode.EC_NoError);
	}

	@Test
	public void testDeleteCase2() throws Exception {
		Shared.printTestMethodStartInfo();
		//
		Shared.caseLog("case2: 删除一种优惠券, 传入的ID为NULL");
		//
		Coupon couponGet = BaseCouponTest.DataInput.getCoupon();
		BaseCouponTest.deleteViaAction(couponGet, sessionBoss, mvc, EnumErrorCode.EC_WrongFormatForInputField);
	}

	@Test
	public void testDeleteCase3() throws Exception {
		Shared.printTestMethodStartInfo();
		//
		Shared.caseLog("case3: 删除一种优惠券, 传入的ID小于0");
		//
		Coupon couponGet = BaseCouponTest.DataInput.getCoupon();
		couponGet.setID(BaseAction.INVALID_ID);
		BaseCouponTest.deleteViaAction(couponGet, sessionBoss, mvc, EnumErrorCode.EC_WrongFormatForInputField);
	}

	@Test
	public void testDeleteCase4() throws Exception {
		Shared.printTestMethodStartInfo();
		//
		Shared.caseLog("case4: 删除一种优惠券, 传入不存在的ID");
		//
		Coupon couponGet = BaseCouponTest.DataInput.getCoupon();
		couponGet.setID(Shared.BIG_ID);
		BaseCouponTest.deleteViaAction(couponGet, sessionBoss, mvc, EnumErrorCode.EC_NoError);
	}

	@Test
	public void testDeleteCase5() throws Exception {
		Shared.printTestMethodStartInfo();
		//
		Shared.caseLog("case5: 无权限");
		//
		Coupon couponGet = BaseCouponTest.DataInput.getCoupon();
		Coupon couponCreate = BaseCouponTest.createViaAction(couponGet, sessionBoss, mvc, EnumErrorCode.EC_NoError);
		BaseCouponTest.deleteViaAction(couponCreate, Shared.getStaffLoginSession(mvc, BaseAction.ACCOUNT_Phone_PreSale, Shared.PASSWORD_DEFAULT, Shared.DB_SN_Test), mvc, EnumErrorCode.EC_NoPermission);
		BaseCouponTest.deleteViaAction(couponCreate, sessionBoss, mvc, EnumErrorCode.EC_NoError);
	}

	@Test
	public void testRetrieve1Case1() throws Exception {
		Shared.printTestMethodStartInfo();
		//
		Shared.caseLog("case1:查询一种优惠券");
		Coupon couponGet = BaseCouponTest.DataInput.getCoupon();
		Coupon couponCreate = BaseCouponTest.createViaAction(couponGet, sessionBoss, mvc, EnumErrorCode.EC_NoError);
		Coupon couponR1 = BaseCouponTest.retrieve1ViaAction(couponCreate, sessionBoss, mvc, EnumErrorCode.EC_NoError);
		// 删除测试数据
		BaseCouponTest.deleteViaMapper(couponR1);
	}

	@Test
	public void testRetrieve1Case2() throws Exception {
		Shared.printTestMethodStartInfo();
		//
		Shared.caseLog("case2:查询一种优惠券, 传入的ID为NULL");
		Coupon couponGet = BaseCouponTest.DataInput.getCoupon();
		BaseCouponTest.retrieve1ViaAction(couponGet, sessionBoss, mvc, EnumErrorCode.EC_WrongFormatForInputField);
	}

	@Test
	public void testRetrieve1Case3() throws Exception {
		Shared.printTestMethodStartInfo();
		//
		Shared.caseLog("case3:查询一种优惠券, 传入的ID小于0");
		Coupon couponGet = BaseCouponTest.DataInput.getCoupon();
		couponGet.setID(BaseAction.INVALID_ID);
		BaseCouponTest.retrieve1ViaAction(couponGet, sessionBoss, mvc, EnumErrorCode.EC_WrongFormatForInputField);
	}

	@Test
	public void testRetrieve1Case4() throws Exception {
		Shared.printTestMethodStartInfo();
		//
		Shared.caseLog("case4:查询一种优惠券, 传入不存在的ID");
		Coupon couponGet = BaseCouponTest.DataInput.getCoupon();
		couponGet.setID(Shared.BIG_ID);
		BaseCouponTest.retrieve1ViaAction(couponGet, sessionBoss, mvc, EnumErrorCode.EC_NoSuchData);
	}
}
