package com.bx.erp.test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MvcResult;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.Coupon;
import com.bx.erp.model.CouponCode;
import com.bx.erp.model.Vip;
import com.bx.erp.model.VipCard;
import com.bx.erp.model.VipCardCode;
import com.bx.erp.model.BaseModel.EnumBoolean;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.util.DataSourceContextHolder;
import com.bx.erp.util.DatetimeUtil;

@WebAppConfiguration
public class CouponCodeActionTest extends BaseActionTest {
	public static final int HAVE_COUPONCODE_VIPID = 1;
	public static final int ONLYHAVE_CONSUMEDCOUPONCODE_VIPID = 2; // 此会员只有一张已核销的优惠券
	public static final int NOTHAVE_COUPONCODE_VIPID = 3;

	@BeforeClass
	public void setup() {
		super.setUp();
	}

	@AfterClass
	public void tearDown() {
		super.tearDown();
	}

	@Test
	public void testCreateCase1() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case1:正常领取优惠券");
		// 创建一种优惠券
		Coupon coupon = BaseCouponTest.DataInput.getCoupon();
		Coupon couponCreate = BaseCouponTest.createViaMapper(coupon);
		// 领取优惠券
		CouponCode couponCode = BaseCouponCodeTest.DataInput.getCouponCode(Shared.DEFAULT_VIP_ID, couponCreate.getID());
		CouponCode couponCodeCreate = BaseCouponCodeTest.createViaAction(couponCode, mvc, sessionVip, EnumErrorCode.EC_NoError);
		Assert.assertTrue(couponCodeCreate != null, "领取优惠券失败");
		//
		BaseCouponCodeTest.deleteViaMapper(couponCodeCreate);
		BaseCouponTest.deleteViaMapper(couponCreate);
	}

	@Test
	public void testCreateCase2() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case2:会员ID不存在");
		// 创建一种优惠券
		Coupon coupon = BaseCouponTest.DataInput.getCoupon();
		Coupon couponCreate = BaseCouponTest.createViaMapper(coupon);
		// 领取优惠券
		CouponCode couponCode = BaseCouponCodeTest.DataInput.getCouponCode(BaseAction.INVALID_ID, couponCreate.getID());
		BaseCouponCodeTest.createViaAction(couponCode, mvc, sessionVip, EnumErrorCode.EC_NoSuchData);
		//
		BaseCouponTest.deleteViaMapper(couponCreate);
	}

	@Test
	public void testCreateCase3() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case3:优惠券ID不存在");
		// 领取优惠券
		CouponCode couponCode = BaseCouponCodeTest.DataInput.getCouponCode(Shared.DEFAULT_VIP_ID, BaseAction.INVALID_ID);
		BaseCouponCodeTest.createViaAction(couponCode, mvc, sessionVip, EnumErrorCode.EC_NoSuchData);
	}

	@Test
	public void testCreateCase4() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case4:优惠券库存不足");
		// 创建一种优惠券
		Coupon coupon = BaseCouponTest.DataInput.getCoupon();
		coupon.setQuantity(1);
		coupon.setRemainingQuantity(1);
		Coupon couponCreate = BaseCouponTest.createViaMapper(coupon);
		// 领取优惠券
		CouponCode couponCode = BaseCouponCodeTest.DataInput.getCouponCode(Shared.DEFAULT_VIP_ID, couponCreate.getID());
		CouponCode couponCodeCreate = BaseCouponCodeTest.createViaAction(couponCode, mvc, sessionVip, EnumErrorCode.EC_NoError);
		Assert.assertTrue(couponCodeCreate != null, "领取优惠券失败");
		//
		CouponCode couponCode2 = BaseCouponCodeTest.DataInput.getCouponCode(Shared.DEFAULT_VIP_ID, couponCreate.getID());
		BaseCouponCodeTest.createViaAction(couponCode2, mvc, sessionVip, EnumErrorCode.EC_BusinessLogicNotDefined);
		//
		BaseCouponCodeTest.deleteViaMapper(couponCodeCreate);
		BaseCouponTest.deleteViaMapper(couponCreate);
	}

	@Test
	public void testCreateCase5() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case5:会员已超过领取该优惠券的个人数目上限，无法领取");
		// 创建一种优惠券
		Coupon coupon = BaseCouponTest.DataInput.getCoupon();
		coupon.setPersonalLimit(1); // 限制每个会员只能领取1次
		Coupon couponCreate = BaseCouponTest.createViaMapper(coupon);
		// 领取优惠券
		CouponCode couponCode = BaseCouponCodeTest.DataInput.getCouponCode(Shared.DEFAULT_VIP_ID, couponCreate.getID());
		CouponCode couponCodeCreate = BaseCouponCodeTest.createViaAction(couponCode, mvc, sessionVip, EnumErrorCode.EC_NoError);
		Assert.assertTrue(couponCodeCreate != null, "领取优惠券失败");
		// 再次领取
		CouponCode couponCode2 = BaseCouponCodeTest.DataInput.getCouponCode(Shared.DEFAULT_VIP_ID, couponCreate.getID());
		BaseCouponCodeTest.createViaAction(couponCode2, mvc, sessionVip, EnumErrorCode.EC_BusinessLogicNotDefined);
		//
		BaseCouponCodeTest.deleteViaMapper(couponCodeCreate);
		BaseCouponTest.deleteViaMapper(couponCreate);
	}

	@Test
	public void testCreateCase6() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case6:使用积分领取优惠券");
		// 创建一种优惠券
		Coupon coupon = BaseCouponTest.DataInput.getCoupon();
		coupon.setBonus(20);
		Coupon couponCreate = BaseCouponTest.createViaMapper(coupon);
		// 创建会员
		Vip vip = BaseVipTest.DataInput.getVip();
		Vip vipCreate = BaseVipTest.createViaMapper(BaseBO.INVALID_CASE_ID, vip, EnumErrorCode.EC_NoError, Shared.DBName_Test);
		// 增加会员积分
		Vip vipToUpdate = (Vip) vipCreate.clone();
		vipToUpdate.setAmount(5000);
		vipToUpdate.setIsIncreaseBonus(EnumBoolean.EB_Yes.getIndex());
		Map<String, Object> paramsUpdateBonus = vipToUpdate.getUpdateParam(BaseBO.CASE_Vip_UpdateBonus, vipToUpdate);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Vip vipUpdateInDB = (Vip) vipMapper.updateBonus(paramsUpdateBonus);
		//
		vipToUpdate.setBonus(vipCreate.getBonus() + vipToUpdate.getAmount() / VipMapperTest.amountUnit * VipMapperTest.increaseBonus);
		if (vipUpdateInDB.compareTo(vipToUpdate) == -1) {
			Assert.assertTrue(false, "修改会员积分失败，积分和期望结果不一致");
		}
		Assert.assertTrue(vipUpdateInDB != null && EnumErrorCode.values()[Integer.parseInt(paramsUpdateBonus.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "修改积分失败");
		// 领取优惠券
		CouponCode couponCode = BaseCouponCodeTest.DataInput.getCouponCode(vipCreate.getID(), couponCreate.getID());
		CouponCode couponCodeCreate = BaseCouponCodeTest.createViaAction(couponCode, mvc, sessionVip, EnumErrorCode.EC_NoError);
		Assert.assertTrue(couponCodeCreate != null, "领取优惠券失败");
		// 查询会员信息进行结果验证
		Vip vipR1 = BaseVipTest.retrieve1ViaMapper(vipCreate, Shared.DBName_Test);
		Assert.assertTrue(vipR1.getBonus() == vipUpdateInDB.getBonus() - couponCreate.getBonus(), "会员积分没有正确计算");
		//
		BaseCouponCodeTest.deleteViaMapper(couponCodeCreate);
		BaseCouponTest.deleteViaMapper(couponCreate);
	}
	
	@Test
	public void testCreateCase7() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case7:会员积分不足,无法领取该优惠券");
		// 创建一种优惠券
		Coupon coupon = BaseCouponTest.DataInput.getCoupon();
		coupon.setBonus(20);
		Coupon couponCreate = BaseCouponTest.createViaMapper(coupon);
		// 领取优惠券
		CouponCode couponCode = BaseCouponCodeTest.DataInput.getCouponCode(Shared.DEFAULT_VIP_ID, couponCreate.getID());
		BaseCouponCodeTest.createViaAction(couponCode, mvc, sessionVip, EnumErrorCode.EC_BusinessLogicNotDefined);
		//
		BaseCouponTest.deleteViaMapper(couponCreate);
	}

	@Test
	public void testRetrieveNCase1() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case1:什么都不传，返回有效的优惠券");
		// 返回有效的优惠券，必须保证数据库有
		Coupon couponGet = BaseCouponTest.DataInput.getCoupon();
		couponGet.setWeekDayAvailable(127);
		couponGet.setBeginDateTime(DatetimeUtil.getDays(new Date(), -1));
		couponGet.setEndDateTime(DatetimeUtil.getDays(new Date(), 5));
		Coupon couponCreate = BaseCouponTest.createViaAction(couponGet, sessionBoss, mvc, EnumErrorCode.EC_NoError);
		//
		Vip vipGet = BaseVipTest.DataInput.getVip();
		Vip vipCreate = BaseVipTest.createViaAction(vipGet, sessionBoss, mvc, EnumErrorCode.EC_NoError);
		//
		CouponCode couponCodeGet = BaseCouponCodeTest.DataInput.getCouponCode(vipCreate.getID(), couponCreate.getID());
		CouponCode couponCodeCreate = BaseCouponCodeTest.createViaAction(couponCodeGet, mvc, sessionVip, EnumErrorCode.EC_NoError);
		//
		CouponCode couponCode = new CouponCode();
		Assert.assertTrue(BaseCouponCodeTest.retrieveNViaAction(couponCode, mvc, sessionVip, BaseBO.INVALID_CASE_ID).size() > 0, "并没有返回优惠券");
		//
		BaseCouponCodeTest.deleteViaMapper(couponCodeCreate);
		BaseCouponTest.deleteViaAction(couponCreate, sessionBoss, mvc, EnumErrorCode.EC_NoError);
	}

	@Test
	public void testRetrieveNCase2() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case2:传递会员ID，查找相关的优惠券");

		CouponCode couponCode = new CouponCode();
		couponCode.setVipID(HAVE_COUPONCODE_VIPID);
		List<BaseModel> list = BaseCouponCodeTest.retrieveNViaAction(couponCode, mvc, sessionBoss, BaseBO.INVALID_CASE_ID);
		//
		Assert.assertTrue(list.size() > 0, "并没有返回优惠券");
		for (BaseModel baseModel : list) {
			Assert.assertTrue(((CouponCode) baseModel).getVipID() == HAVE_COUPONCODE_VIPID, "返回了与此会员无关的优惠券");
		}
	}

	@Test
	public void testRetrieveNCase3() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case3:传递状态值，返回相关状态的优惠券");

		CouponCode couponCode = new CouponCode();
		couponCode.setStatus(CouponCode.EnumCouponCodeStatus.ECCS_Consumed.getIndex());
		List<BaseModel> list = BaseCouponCodeTest.retrieveNViaAction(couponCode, mvc, sessionBoss, BaseBO.INVALID_CASE_ID);
		for (int i = 0; i < list.size(); i++) {
			CouponCode queryCouponCode = (CouponCode) list.get(i);
			Assert.assertTrue(queryCouponCode.getStatus() == CouponCode.EnumCouponCodeStatus.ECCS_Consumed.getIndex(), "根据状态查找，查询出于状态不符的优惠券");
		}
	}

	@Test
	public void testRetrieveNCase4() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case4:传递会员ID跟状态值，返回此会员相关状态的可用优惠券");

		CouponCode couponCode = new CouponCode();
		couponCode.setVipID(HAVE_COUPONCODE_VIPID);
		couponCode.setStatus(CouponCode.EnumCouponCodeStatus.ECCS_Consumed.getIndex());
		List<BaseModel> list = BaseCouponCodeTest.retrieveNViaAction(couponCode, mvc, sessionBoss, BaseBO.INVALID_CASE_ID);
		Assert.assertTrue(list != null && list.size() > 0, "查询失败");
		for (int i = 0; i < list.size(); i++) {
			CouponCode queryCouponCode = (CouponCode) list.get(i);
			Assert.assertTrue(queryCouponCode.getStatus() == CouponCode.EnumCouponCodeStatus.ECCS_Consumed.getIndex() && queryCouponCode.getVipID() == HAVE_COUPONCODE_VIPID, "根据VIPID和状态查找，查询出于状态不符的优惠券");
		}
	}

	@Test
	public void testRetrieveNCase5() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case5:查到优惠券，但是还没有到起用日期。返回空");
		// 创建优惠券
		Coupon coupon = BaseCouponTest.DataInput.getCoupon();
		coupon.setBeginDateTime(DatetimeUtil.getDays(new Date(), 1));
		//
		Coupon couponCreate = BaseCouponTest.createViaMapper(coupon);

		// 领取优惠券
		CouponCode couponCode = BaseCouponCodeTest.DataInput.getCouponCode(HAVE_COUPONCODE_VIPID, couponCreate.getID());
		CouponCode couponCodeCreate = BaseCouponCodeTest.createViaMapper(couponCode);

		CouponCode queryCouponCode = new CouponCode();
		queryCouponCode.setCouponID(couponCreate.getID());
		List<BaseModel> list = BaseCouponCodeTest.retrieveNViaAction(queryCouponCode, mvc, sessionBoss, BaseBO.INVALID_CASE_ID);
		Assert.assertTrue(list != null && list.size() == 0, "查询失败");

		// 删除数据
		BaseCouponCodeTest.deleteViaMapper(couponCodeCreate);
		BaseCouponTest.deleteViaMapper(couponCreate);
	}

	@Test
	public void testRetrieveNCase6() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case6:查到优惠券，但是已经过了结算日期。返回空");
		// 创建优惠券
		Coupon coupon = BaseCouponTest.DataInput.getCoupon();
		coupon.setBeginDateTime(DatetimeUtil.getDays(new Date(), -2));
		coupon.setEndDateTime(DatetimeUtil.getDays(new Date(), -1));
		//
		Coupon couponCreate = BaseCouponTest.createViaMapper(coupon);

		// 领取优惠券
		CouponCode couponCode = BaseCouponCodeTest.DataInput.getCouponCode(HAVE_COUPONCODE_VIPID, couponCreate.getID());
		CouponCode couponCodeCreate = BaseCouponCodeTest.createViaMapper(couponCode);

		// 查找优惠券
		CouponCode queryCouponCode = new CouponCode();
		queryCouponCode.setCouponID(couponCreate.getID());
		List<BaseModel> list = BaseCouponCodeTest.retrieveNViaAction(queryCouponCode, mvc, sessionBoss, BaseBO.INVALID_CASE_ID);
		Assert.assertTrue(list != null && list.size() == 0, "查询失败");

		// 删除数据
		BaseCouponCodeTest.deleteViaMapper(couponCodeCreate);
		BaseCouponTest.deleteViaMapper(couponCreate);
	}

	@Test
	public void testRetrieveNCase7() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case7:优惠券限制星期x的某个时间点能够使用，会员查到优惠券且在起用日期中，今天是星期x，且不在使用时间中。返回空");

		// 创建优惠券
		Coupon coupon = BaseCouponTest.DataInput.getCoupon();
		BaseCouponTest.setCurrentWeekType(coupon);
		coupon.setBeginTime("00:00:01");
		coupon.setEndTime("00:00:02");
		coupon.setBeginDateTime(DatetimeUtil.getDays(new Date(), -1));
		//
		Coupon couponCreate = BaseCouponTest.createViaMapper(coupon);
		// 领取优惠券
		CouponCode couponCode = BaseCouponCodeTest.DataInput.getCouponCode(HAVE_COUPONCODE_VIPID, couponCreate.getID());
		CouponCode couponCodeCreate = BaseCouponCodeTest.createViaMapper(couponCode);
		// 查找优惠券
		CouponCode queryCouponCode = new CouponCode();
		queryCouponCode.setCouponID(couponCreate.getID());
		List<BaseModel> list = BaseCouponCodeTest.retrieveNViaAction(queryCouponCode, mvc, sessionBoss, BaseBO.INVALID_CASE_ID);
		Assert.assertTrue(list != null && list.size() == 0, "查询失败");

		// 删除数据
		BaseCouponCodeTest.deleteViaMapper(couponCodeCreate);
		BaseCouponTest.deleteViaMapper(couponCreate);
	}

	@Test
	public void testRetrieveNCase8() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case8:查到优惠券且在起用日期中。今天星期x，但是在起始时间和结束时间之间。返回优惠券");
		// 创建优惠券
		Coupon coupon = BaseCouponTest.DataInput.getCoupon();
		BaseCouponTest.setCurrentWeekType(coupon);
		coupon.setBeginTime("00:00:00");
		coupon.setEndTime("23:59:59");
		coupon.setBeginDateTime(DatetimeUtil.getDays(new Date(), -1));
		//
		Coupon couponCreate = BaseCouponTest.createViaMapper(coupon);
		// 领取优惠券
		CouponCode couponCode = BaseCouponCodeTest.DataInput.getCouponCode(HAVE_COUPONCODE_VIPID, couponCreate.getID());
		CouponCode couponCodeCreate = BaseCouponCodeTest.createViaMapper(couponCode);
		// 查找优惠券
		CouponCode queryCouponCode = new CouponCode();
		queryCouponCode.setCouponID(couponCreate.getID());
		List<BaseModel> list = BaseCouponCodeTest.retrieveNViaAction(queryCouponCode, mvc, sessionBoss, BaseBO.INVALID_CASE_ID);
		Assert.assertTrue(list != null && list.size() == 1, "查询失败");

		// 删除数据
		BaseCouponCodeTest.deleteViaMapper(couponCodeCreate);
		BaseCouponTest.deleteViaMapper(couponCreate);
	}


	@Test
	public void testRetrieveNCase9() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case9:查到优惠券且在起用日期中，不在限制使用的星期x。返回优惠券");
		// 创建优惠券
		Coupon coupon = BaseCouponTest.DataInput.getCoupon();
		coupon.setWeekDayAvailable(0);
		coupon.setBeginDateTime(DatetimeUtil.getDays(new Date(), -1));
		Coupon couponCreate = BaseCouponTest.createViaMapper(coupon);
		// 领取优惠券
		CouponCode couponCode = BaseCouponCodeTest.DataInput.getCouponCode(HAVE_COUPONCODE_VIPID, couponCreate.getID());
		CouponCode couponCodeCreate = BaseCouponCodeTest.createViaMapper(couponCode);

		CouponCode queryCouponCode = new CouponCode();
		queryCouponCode.setCouponID(couponCreate.getID());
		List<BaseModel> list = BaseCouponCodeTest.retrieveNViaAction(couponCode, mvc, sessionBoss, BaseBO.INVALID_CASE_ID);
		Assert.assertTrue(list != null && list.size() == 1, "查询失败");
		Assert.assertTrue(couponCodeCreate.compareTo(list.get(0)) == 0, Shared.CompareToErrorMsg);

		// 删除
		BaseCouponCodeTest.deleteViaMapper(couponCodeCreate);
		BaseCouponTest.deleteViaMapper(couponCreate);
	}

	@Test
	public void testRetrieveNCase10() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case10:该会员并没有一张可用的优惠券，返回空");
		CouponCode couponCode = new CouponCode();
		couponCode.setVipID(NOTHAVE_COUPONCODE_VIPID);
		Assert.assertTrue(BaseCouponCodeTest.retrieveNViaAction(couponCode, mvc, sessionBoss, BaseBO.INVALID_CASE_ID).size() == 0, "有返回优惠券");
	}

	@Test
	public void testRetrieveNCase11() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case11:查询会员已核销的优惠券。并在能使用的日期，返回此优惠券。");
		CouponCode couponCode = new CouponCode();
		couponCode.setVipID(ONLYHAVE_CONSUMEDCOUPONCODE_VIPID);
		couponCode.setStatus(CouponCode.EnumCouponCodeStatus.ECCS_Consumed.getIndex());
		//
		List<BaseModel> list = BaseCouponCodeTest.retrieveNViaAction(couponCode, mvc, sessionBoss, BaseBO.INVALID_CASE_ID);
		//
		Assert.assertTrue(list.size() > 0, "没有返回优惠券");
		for (BaseModel baseModel : list) {
			Assert.assertTrue(((CouponCode) baseModel).getStatus() == CouponCode.EnumCouponCodeStatus.ECCS_Consumed.getIndex(), "此会员返回了正常的优惠券");
		}
	}

	@Test
	public void testRetrieveNCase12() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case12：此会员有俩张优惠券是属于同一种类型的优惠券。只返回一张优惠券");

		CouponCode queryCouponCode = new CouponCode();
		queryCouponCode.setVipID(HAVE_COUPONCODE_VIPID);
		List<BaseModel> list = BaseCouponCodeTest.retrieveNViaAction(queryCouponCode, mvc, sessionBoss, BaseBO.INVALID_CASE_ID);
		//
		Assert.assertTrue(list.size() > 0, "并没有返回优惠券");
		for (BaseModel baseModel : list) {
			Assert.assertTrue(((CouponCode) baseModel).getVipID() == HAVE_COUPONCODE_VIPID, "返回了与此会员无关的优惠券");
			for (BaseModel couponCode : list) {
				if (baseModel.compareTo(couponCode) == 0) {
					continue;
				}
				if (((CouponCode) baseModel).getCouponID() == ((CouponCode) couponCode).getCouponID()) {
					Assert.assertTrue(false, "返回了多张同种类型的优惠券");
				}
			}
		}
	}

	private void retrieveNByVipID(int total, int subStatus) throws Exception {
		// 创建会员
		Vip createVip = BaseVipTest.createViaMapper(BaseBO.INVALID_CASE_ID, BaseVipTest.DataInput.getVip(), EnumErrorCode.EC_NoError, Shared.DBName_Test);
		// 创建一种会员卡
		VipCard vipCard = BaseVipCardTest.DataInput.getVipCard();
		VipCard vipCardCreate = BaseVipCardTest.createViaMapper(vipCard);

		// 会员领取会员卡
		VipCardCode vipCardCode = BaseVipCardCodeTest.DataInput.getVipCardCode(createVip.getID(), vipCardCreate.getID());
		VipCardCode vipCardCodeCreate = BaseVipCardCodeTest.createViaMapper(vipCardCode, Shared.DBName_Test, BaseBO.INVALID_CASE_ID, EnumErrorCode.EC_NoError);

		// 创建优惠券
		Coupon coupon = BaseCouponTest.DataInput.getCoupon();
		coupon.setPersonalLimit(2);
		Coupon couponCreate = BaseCouponTest.createViaMapper(coupon);
		// 领取优惠券
		CouponCode couponCodeA = BaseCouponCodeTest.DataInput.getCouponCode(createVip.getID(), couponCreate.getID());
		CouponCode couponCodeCreateA = BaseCouponCodeTest.createViaMapper(couponCodeA);
		CouponCode couponCodeB = BaseCouponCodeTest.DataInput.getCouponCode(createVip.getID(), couponCreate.getID());
		CouponCode couponCodeCreateB = BaseCouponCodeTest.createViaMapper(couponCodeB);
		// 核销优惠券B
		BaseCouponCodeTest.updateViaMapper(couponCodeCreateB);
		// 查询全部优惠券
		CouponCode queryCouponCode = new CouponCode();
		queryCouponCode.setVipID(createVip.getID());
		queryCouponCode.setSubStatus(subStatus);
		List<BaseModel> couponCodeList = BaseCouponCodeTest.retrieveNViaAction(queryCouponCode, mvc, Shared.getVipLoginSession(mvc, createVip, true), BaseBO.CASE_CouponCode_retrieveNByVipID);
		Assert.assertTrue(couponCodeList.size() == total, "查询此会员全部优惠券失败");
		// 删除数据
		BaseCouponCodeTest.deleteViaMapper(couponCodeCreateA);
		BaseCouponCodeTest.deleteViaMapper(couponCodeCreateB);
		BaseCouponTest.deleteViaMapper(couponCreate);
		BaseVipCardCodeTest.deleteViaMapper(vipCardCodeCreate);
		BaseVipCardTest.deleteViaMapper(vipCardCreate);
	}

	@Test
	public void testRetrieveNByVipIDCase1() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("查询某个会员的全部优惠券");
		retrieveNByVipID(2, CouponCode.EnumSubStatusCouponCode.ESSC_All.getIndex());
	}

	@Test
	public void testRetrieveNByVipIDCase2() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("传递的会员ID跟登录者不一致");
		// 创建会员
		Vip createVip = BaseVipTest.createViaMapper(BaseBO.INVALID_CASE_ID, BaseVipTest.DataInput.getVip(), EnumErrorCode.EC_NoError, Shared.DBName_Test);
		// 创建一种会员卡
		VipCard vipCard = BaseVipCardTest.DataInput.getVipCard();
		VipCard vipCardCreate = BaseVipCardTest.createViaMapper(vipCard);

		// 会员领取会员卡
		VipCardCode vipCardCode = BaseVipCardCodeTest.DataInput.getVipCardCode(createVip.getID(), vipCardCreate.getID());
		VipCardCode vipCardCodeCreate = BaseVipCardCodeTest.createViaMapper(vipCardCode, Shared.DBName_Test, BaseBO.INVALID_CASE_ID, EnumErrorCode.EC_NoError);

		CouponCode queryCouponCode = new CouponCode();
		queryCouponCode.setVipID(BaseAction.INVALID_ID);
		queryCouponCode.setSubStatus(CouponCode.EnumSubStatusCouponCode.ESSC_All.getIndex());

		MvcResult mr = mvc.perform(//
				post("/couponCode/retrieveNByVipIDEx.bx") //
						.param(CouponCode.field.getFIELD_NAME_vipID(), String.valueOf(queryCouponCode.getVipID())) //
						.param(CouponCode.field.getFIELD_NAME_subStatus(), String.valueOf(queryCouponCode.getSubStatus())) //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) Shared.getVipLoginSession(mvc, createVip, true)) //
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Assert.assertTrue("".equals(mr.getResponse().getContentAsString()), "传递的会员ID跟登录会员ID不一致未能处理");

		BaseVipCardCodeTest.deleteViaMapper(vipCardCodeCreate);
		BaseVipCardTest.deleteViaMapper(vipCardCreate);
	}

	@Test
	public void testRetrieveNByVipIDCase3() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("查询某个会员的全部未使用优惠券");
		retrieveNByVipID(1, CouponCode.EnumSubStatusCouponCode.ESSC_NotConsumed.getIndex());
	}

	@Test
	public void testRetrieveNByVipIDCase4() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("查询某个会员的全部已使用优惠券");
		retrieveNByVipID(1, CouponCode.EnumSubStatusCouponCode.ESSC_Consumed.getIndex());
	}

	@Test
	public void testRetrieveNByVipIDCase5() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case5:查询某个会员的全部已过期优惠券");
		// 创建会员
		Vip createVip = BaseVipTest.createViaMapper(BaseBO.INVALID_CASE_ID, BaseVipTest.DataInput.getVip(), EnumErrorCode.EC_NoError, Shared.DBName_Test);
		// 创建一种会员卡
		VipCard vipCard = BaseVipCardTest.DataInput.getVipCard();
		VipCard vipCardCreate = BaseVipCardTest.createViaMapper(vipCard);

		// 会员领取会员卡
		VipCardCode vipCardCode = BaseVipCardCodeTest.DataInput.getVipCardCode(createVip.getID(), vipCardCreate.getID());
		VipCardCode vipCardCodeCreate = BaseVipCardCodeTest.createViaMapper(vipCardCode, Shared.DBName_Test, BaseBO.INVALID_CASE_ID, EnumErrorCode.EC_NoError);
		// 创建优惠券
		Coupon coupon = BaseCouponTest.DataInput.getCoupon();
		coupon.setBeginDateTime(DatetimeUtil.getDays(new Date(), -5));
		coupon.setEndDateTime(DatetimeUtil.getDays(new Date(), -1));
		coupon.setPersonalLimit(2);
		Coupon couponCreate = BaseCouponTest.createViaMapper(coupon);
		// 领取优惠券
		CouponCode couponCodeA = BaseCouponCodeTest.DataInput.getCouponCode(createVip.getID(), couponCreate.getID());
		CouponCode couponCodeCreateA = BaseCouponCodeTest.createViaMapper(couponCodeA);
		CouponCode couponCodeB = BaseCouponCodeTest.DataInput.getCouponCode(createVip.getID(), couponCreate.getID());
		CouponCode couponCodeCreateB = BaseCouponCodeTest.createViaMapper(couponCodeB);
		// 查询全部优惠券
		CouponCode queryCouponCode = new CouponCode();
		queryCouponCode.setVipID(createVip.getID());
		queryCouponCode.setSubStatus(CouponCode.EnumSubStatusCouponCode.ESSC_Outdated.getIndex());
		List<BaseModel> couponCodeList = BaseCouponCodeTest.retrieveNViaAction(queryCouponCode, mvc, Shared.getVipLoginSession(mvc, createVip, true), BaseBO.CASE_CouponCode_retrieveNByVipID);
		Assert.assertTrue(couponCodeList.size() == 2, "查询此会员全部优惠券失败");
		// 删除数据
		BaseCouponCodeTest.deleteViaMapper(couponCodeCreateA);
		BaseCouponCodeTest.deleteViaMapper(couponCodeCreateB);
		BaseCouponTest.deleteViaMapper(couponCreate);
		BaseVipCardCodeTest.deleteViaMapper(vipCardCodeCreate);
		BaseVipCardTest.deleteViaMapper(vipCardCreate);
	}

	@Test
	public void testRetrieveNByVipIDCase6() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case6:一种优惠券已删除,此会员领取了该优惠券没有使用。查询会员的全部未使用惠券(能查询出)");
		// 创建会员
		Vip createVip = BaseVipTest.createViaMapper(BaseBO.INVALID_CASE_ID, BaseVipTest.DataInput.getVip(), EnumErrorCode.EC_NoError, Shared.DBName_Test);
		// 创建一种会员卡
		VipCard vipCard = BaseVipCardTest.DataInput.getVipCard();
		VipCard vipCardCreate = BaseVipCardTest.createViaMapper(vipCard);
		// 会员领取会员卡
		VipCardCode vipCardCode = BaseVipCardCodeTest.DataInput.getVipCardCode(createVip.getID(), vipCardCreate.getID());
		VipCardCode vipCardCodeCreate = BaseVipCardCodeTest.createViaMapper(vipCardCode, Shared.DBName_Test, BaseBO.INVALID_CASE_ID, EnumErrorCode.EC_NoError);
		// 创建优惠券
		Coupon coupon = BaseCouponTest.DataInput.getCoupon();
		coupon.setPersonalLimit(2);
		Coupon couponCreate = BaseCouponTest.createViaMapper(coupon);
		// 领取优惠券
		CouponCode couponCodeA = BaseCouponCodeTest.DataInput.getCouponCode(createVip.getID(), couponCreate.getID());
		CouponCode couponCodeCreateA = BaseCouponCodeTest.createViaMapper(couponCodeA);
		CouponCode couponCodeB = BaseCouponCodeTest.DataInput.getCouponCode(createVip.getID(), couponCreate.getID());
		CouponCode couponCodeCreateB = BaseCouponCodeTest.createViaMapper(couponCodeB);
		// 删除优惠券
		BaseCouponTest.deleteViaMapper(couponCreate);
		// 查询全部优惠券
		CouponCode queryCouponCode = new CouponCode();
		queryCouponCode.setVipID(createVip.getID());
		queryCouponCode.setSubStatus(CouponCode.EnumSubStatusCouponCode.ESSC_NotConsumed.getIndex());
		List<BaseModel> couponCodeList = BaseCouponCodeTest.retrieveNViaAction(queryCouponCode, mvc, Shared.getVipLoginSession(mvc, createVip, true), BaseBO.CASE_CouponCode_retrieveNByVipID);
		Assert.assertTrue(couponCodeList.size() == 2, "查询此会员全部优惠券失败");
		// 删除数据
		BaseCouponCodeTest.deleteViaMapper(couponCodeCreateA);
		BaseCouponCodeTest.deleteViaMapper(couponCodeCreateB);
		BaseVipCardCodeTest.deleteViaMapper(vipCardCodeCreate);
		BaseVipCardTest.deleteViaMapper(vipCardCreate);
	}

	@Test
	public void testRetrieveNByVipIDCase7() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("其他角色进行查询");

		MvcResult mr = mvc.perform(//
				post("/couponCode/retrieveNByVipIDEx.bx") //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionBoss) //
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Assert.assertTrue("".equals(mr.getResponse().getContentAsString()), "其他角色进行查询未能处理");
	}

	@Test
	public void retrieveNTotalByVipIDTest1() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case1:查询某个会员的优惠券总数");
		// 创建会员
		Vip createVip = BaseVipTest.createViaMapper(BaseBO.INVALID_CASE_ID, BaseVipTest.DataInput.getVip(), EnumErrorCode.EC_NoError, Shared.DBName_Test);
		// 创建一种会员卡
		VipCard vipCard = BaseVipCardTest.DataInput.getVipCard();
		VipCard vipCardCreate = BaseVipCardTest.createViaMapper(vipCard);
		// 会员领取会员卡
		VipCardCode vipCardCode = BaseVipCardCodeTest.DataInput.getVipCardCode(createVip.getID(), vipCardCreate.getID());
		VipCardCode vipCardCodeCreate = BaseVipCardCodeTest.createViaMapper(vipCardCode, Shared.DBName_Test, BaseBO.INVALID_CASE_ID, EnumErrorCode.EC_NoError);
		// 创建优惠券
		Coupon coupon = BaseCouponTest.DataInput.getCoupon();
		coupon.setPersonalLimit(2);
		Coupon couponCreate = BaseCouponTest.createViaMapper(coupon);
		// 领取优惠券
		CouponCode couponCodeA = BaseCouponCodeTest.DataInput.getCouponCode(createVip.getID(), couponCreate.getID());
		CouponCode couponCodeCreateA = BaseCouponCodeTest.createViaMapper(couponCodeA);
		CouponCode couponCodeB = BaseCouponCodeTest.DataInput.getCouponCode(createVip.getID(), couponCreate.getID());
		CouponCode couponCodeCreateB = BaseCouponCodeTest.createViaMapper(couponCodeB);
		// 查询
		CouponCode queryCouponCode = new CouponCode();
		queryCouponCode.setVipID(createVip.getID());
		int total = BaseCouponCodeTest.retrieveNTotalByVipIDViaAction(queryCouponCode, mvc, Shared.getVipLoginSession(mvc, createVip, true));
		Assert.assertTrue(total == 2, "查询失败");
		// 删除数据
		BaseCouponCodeTest.deleteViaMapper(couponCodeCreateA);
		BaseCouponCodeTest.deleteViaMapper(couponCodeCreateB);
		BaseCouponTest.deleteViaMapper(couponCreate);
		BaseVipCardCodeTest.deleteViaMapper(vipCardCodeCreate);
		BaseVipCardTest.deleteViaMapper(vipCardCreate);
	}

	@Test
	public void retrieveNTotalByVipIDTest2() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case2:传递的会员ID跟登录者不一致");

		// 创建会员
		Vip createVip = BaseVipTest.createViaMapper(BaseBO.INVALID_CASE_ID, BaseVipTest.DataInput.getVip(), EnumErrorCode.EC_NoError, Shared.DBName_Test);
		// 创建一种会员卡
		VipCard vipCard = BaseVipCardTest.DataInput.getVipCard();
		VipCard vipCardCreate = BaseVipCardTest.createViaMapper(vipCard);
		// 会员领取会员卡
		VipCardCode vipCardCode = BaseVipCardCodeTest.DataInput.getVipCardCode(createVip.getID(), vipCardCreate.getID());
		VipCardCode vipCardCodeCreate = BaseVipCardCodeTest.createViaMapper(vipCardCode, Shared.DBName_Test, BaseBO.INVALID_CASE_ID, EnumErrorCode.EC_NoError);

		CouponCode queryCouponCode = new CouponCode();
		queryCouponCode.setVipID(BaseAction.INVALID_ID);
		//
		MvcResult mr = mvc.perform(//
				post("/couponCode/retrieveNTotalByVipIDEx.bx") //
						.param(CouponCode.field.getFIELD_NAME_vipID(), String.valueOf(queryCouponCode.getVipID())) //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) Shared.getVipLoginSession(mvc, createVip, true)) //
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Assert.assertTrue("".equals(mr.getResponse().getContentAsString()), "传递的会员ID跟登录会员ID不一致未能处理");
		BaseVipCardCodeTest.deleteViaMapper(vipCardCodeCreate);
		BaseVipCardTest.deleteViaMapper(vipCardCreate);
	}

	@Test
	public void retrieveNTotalByVipIDTest3() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case3:查询的会员优惠券全部都已核销");
		// 创建会员
		Vip createVip = BaseVipTest.createViaMapper(BaseBO.INVALID_CASE_ID, BaseVipTest.DataInput.getVip(), EnumErrorCode.EC_NoError, Shared.DBName_Test);
		// 创建一种会员卡
		VipCard vipCard = BaseVipCardTest.DataInput.getVipCard();
		VipCard vipCardCreate = BaseVipCardTest.createViaMapper(vipCard);
		// 会员领取会员卡
		VipCardCode vipCardCode = BaseVipCardCodeTest.DataInput.getVipCardCode(createVip.getID(), vipCardCreate.getID());
		VipCardCode vipCardCodeCreate = BaseVipCardCodeTest.createViaMapper(vipCardCode, Shared.DBName_Test, BaseBO.INVALID_CASE_ID, EnumErrorCode.EC_NoError);
		// 创建优惠券
		Coupon coupon = BaseCouponTest.DataInput.getCoupon();
		coupon.setPersonalLimit(2);
		Coupon couponCreate = BaseCouponTest.createViaMapper(coupon);
		// 领取优惠券
		CouponCode couponCodeA = BaseCouponCodeTest.DataInput.getCouponCode(createVip.getID(), couponCreate.getID());
		CouponCode couponCodeCreateA = BaseCouponCodeTest.createViaMapper(couponCodeA);
		CouponCode couponCodeB = BaseCouponCodeTest.DataInput.getCouponCode(createVip.getID(), couponCreate.getID());
		CouponCode couponCodeCreateB = BaseCouponCodeTest.createViaMapper(couponCodeB);
		// 核销优惠券
		BaseCouponCodeTest.updateViaMapper(couponCodeCreateA);
		BaseCouponCodeTest.updateViaMapper(couponCodeCreateB);
		// 查询
		CouponCode queryCouponCode = new CouponCode();
		queryCouponCode.setVipID(createVip.getID());
		int total = BaseCouponCodeTest.retrieveNTotalByVipIDViaAction(queryCouponCode, mvc, Shared.getVipLoginSession(mvc, createVip, true));
		Assert.assertTrue(total == 0, "查询失败");
		// 删除数据
		BaseCouponCodeTest.deleteViaMapper(couponCodeCreateA);
		BaseCouponCodeTest.deleteViaMapper(couponCodeCreateB);
		BaseCouponTest.deleteViaMapper(couponCreate);
		BaseVipCardCodeTest.deleteViaMapper(vipCardCodeCreate);
		BaseVipCardTest.deleteViaMapper(vipCardCreate);
	}

	@Test
	public void retrieveNTotalByVipIDTest4() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case4:这种优惠券已经删除,但是会员领取优惠券还未使用(能查询出来)");
		// 创建会员
		Vip createVip = BaseVipTest.createViaMapper(BaseBO.INVALID_CASE_ID, BaseVipTest.DataInput.getVip(), EnumErrorCode.EC_NoError, Shared.DBName_Test);
		// 创建一种会员卡
		VipCard vipCard = BaseVipCardTest.DataInput.getVipCard();
		VipCard vipCardCreate = BaseVipCardTest.createViaMapper(vipCard);
		// 会员领取会员卡
		VipCardCode vipCardCode = BaseVipCardCodeTest.DataInput.getVipCardCode(createVip.getID(), vipCardCreate.getID());
		VipCardCode vipCardCodeCreate = BaseVipCardCodeTest.createViaMapper(vipCardCode, Shared.DBName_Test, BaseBO.INVALID_CASE_ID, EnumErrorCode.EC_NoError);
		// 创建优惠券
		Coupon coupon = BaseCouponTest.DataInput.getCoupon();
		coupon.setPersonalLimit(2);
		Coupon couponCreate = BaseCouponTest.createViaMapper(coupon);
		// 领取优惠券
		CouponCode couponCodeA = BaseCouponCodeTest.DataInput.getCouponCode(createVip.getID(), couponCreate.getID());
		CouponCode couponCodeCreateA = BaseCouponCodeTest.createViaMapper(couponCodeA);
		// 将优惠券删除
		BaseCouponTest.deleteViaMapper(couponCreate);
		// 查询
		CouponCode queryCouponCode = new CouponCode();
		queryCouponCode.setVipID(createVip.getID());
		int total = BaseCouponCodeTest.retrieveNTotalByVipIDViaAction(queryCouponCode, mvc, Shared.getVipLoginSession(mvc, createVip, true));
		Assert.assertTrue(total == 1, "查询失败");
		// 删除数据
		BaseCouponCodeTest.deleteViaMapper(couponCodeCreateA);
		BaseVipCardCodeTest.deleteViaMapper(vipCardCodeCreate);
		BaseVipCardTest.deleteViaMapper(vipCardCreate);
	}

	@Test
	public void retrieveNTotalByVipIDTest5() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case5:这种优惠券已经过期,但是会员领取优惠券还未使用(不能查询出来)");
		// 创建会员
		Vip createVip = BaseVipTest.createViaMapper(BaseBO.INVALID_CASE_ID, BaseVipTest.DataInput.getVip(), EnumErrorCode.EC_NoError, Shared.DBName_Test);
		// 创建一种会员卡
		VipCard vipCard = BaseVipCardTest.DataInput.getVipCard();
		VipCard vipCardCreate = BaseVipCardTest.createViaMapper(vipCard);
		// 会员领取会员卡
		VipCardCode vipCardCode = BaseVipCardCodeTest.DataInput.getVipCardCode(createVip.getID(), vipCardCreate.getID());
		VipCardCode vipCardCodeCreate = BaseVipCardCodeTest.createViaMapper(vipCardCode, Shared.DBName_Test, BaseBO.INVALID_CASE_ID, EnumErrorCode.EC_NoError);
		// 创建优惠券
		Coupon coupon = BaseCouponTest.DataInput.getCoupon();
		coupon.setBeginDateTime(DatetimeUtil.getDays(new Date(), -5));
		coupon.setEndDateTime(DatetimeUtil.getDays(new Date(), -1));
		coupon.setPersonalLimit(2);
		Coupon couponCreate = BaseCouponTest.createViaMapper(coupon);
		// 领取优惠券
		CouponCode couponCodeA = BaseCouponCodeTest.DataInput.getCouponCode(createVip.getID(), couponCreate.getID());
		CouponCode couponCodeCreateA = BaseCouponCodeTest.createViaMapper(couponCodeA);
		// 查询
		CouponCode queryCouponCode = new CouponCode();
		queryCouponCode.setVipID(createVip.getID());
		int total = BaseCouponCodeTest.retrieveNTotalByVipIDViaAction(queryCouponCode, mvc, Shared.getVipLoginSession(mvc, createVip, true));
		Assert.assertTrue(total == 0, "查询失败");
		// 删除数据
		BaseCouponCodeTest.deleteViaMapper(couponCodeCreateA);
		BaseCouponTest.deleteViaMapper(couponCreate);
		BaseVipCardCodeTest.deleteViaMapper(vipCardCodeCreate);
		BaseVipCardTest.deleteViaMapper(vipCardCreate);
	}

	@Test
	public void retrieveNTotalByVipIDTest6() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("其他角色进行查询");

		MvcResult mr = mvc.perform(//
				post("/couponCode/retrieveNTotalByVipIDEx.bx") //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionBoss) //
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Assert.assertTrue("".equals(mr.getResponse().getContentAsString()), "其他角色进行查询未能处理");
	}
}
