package com.bx.erp.test;

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
import com.bx.erp.model.Vip;
import com.bx.erp.model.BaseModel.EnumBoolean;
import com.bx.erp.model.CouponCode.EnumCouponCodeStatus;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.util.DataSourceContextHolder;
import com.bx.erp.util.DatetimeUtil;

public class CouponCodeMapperTest extends BaseMapperTest {

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
		// 创建优惠券
		Coupon coupon = BaseCouponTest.DataInput.getCoupon();
		Coupon couponCreate = BaseCouponTest.createViaMapper(coupon);
		// 领取优惠券
		CouponCode couponCode = BaseCouponCodeTest.DataInput.getCouponCode(Shared.DEFAULT_VIP_ID, couponCreate.getID());
		CouponCode couponCodeCreate = BaseCouponCodeTest.createViaMapper(couponCode);
		//
		BaseCouponCodeTest.deleteViaMapper(couponCodeCreate);
		BaseCouponTest.deleteViaMapper(couponCreate);
	}

	@Test
	public void createTest2() throws Exception {
		Shared.printTestMethodStartInfo();
		//
		Shared.caseLog("Case2:会员ID不存在");
		// 创建优惠券
		Coupon coupon = BaseCouponTest.DataInput.getCoupon();
		Coupon couponCreate = BaseCouponTest.createViaMapper(coupon);
		// 领取优惠券
		CouponCode couponCode = BaseCouponCodeTest.DataInput.getCouponCode(BaseAction.INVALID_ID, couponCreate.getID());
		Map<String, Object> params = couponCode.getCreateParam(BaseBO.INVALID_CASE_ID, couponCode);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		CouponCode couponCodeCreate = (CouponCode) couponCodeMapper.create(params);
		Assert.assertTrue(couponCodeCreate == null && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoSuchData,
				"创建对象失败。param=" + params + "\n\r bonusRule=" + couponCode);
		//
		BaseCouponTest.deleteViaMapper(couponCreate);
	}

	@Test
	public void createTest3() throws Exception {
		Shared.printTestMethodStartInfo();
		//
		Shared.caseLog("Case3:优惠券ID不存在");
		// 领取优惠券
		CouponCode couponCode = BaseCouponCodeTest.DataInput.getCouponCode(1, BaseAction.INVALID_ID);
		Map<String, Object> params = couponCode.getCreateParam(BaseBO.INVALID_CASE_ID, couponCode);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		CouponCode couponCodeCreate = (CouponCode) couponCodeMapper.create(params);
		Assert.assertTrue(couponCodeCreate == null && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoSuchData,
				"创建对象失败。param=" + params + "\n\r bonusRule=" + couponCode);
	}

	@Test
	public void createTest4() throws Exception {
		Shared.printTestMethodStartInfo();
		//
		Shared.caseLog("Case4:优惠券库存不足不存在");
		// 创建优惠券
		Coupon coupon = BaseCouponTest.DataInput.getCoupon();
		coupon.setQuantity(1);
		coupon.setRemainingQuantity(1);
		Coupon couponCreate = BaseCouponTest.createViaMapper(coupon);
		// 领取一张，还剩0张
		CouponCode couponCode = BaseCouponCodeTest.DataInput.getCouponCode(Shared.DEFAULT_VIP_ID, couponCreate.getID());
		Map<String, Object> params = couponCode.getCreateParam(BaseBO.INVALID_CASE_ID, couponCode);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		CouponCode couponCodeCreate = (CouponCode) couponCodeMapper.create(params);
		Assert.assertTrue(couponCodeCreate != null && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError,
				"创建对象失败。param=" + params + "\n\r bonusRule=" + couponCode);
		// 再领取优惠券，就领取不了
		CouponCode couponCode2 = BaseCouponCodeTest.DataInput.getCouponCode(Shared.DEFAULT_VIP_ID, couponCreate.getID());
		Map<String, Object> params2 = couponCode.getCreateParam(BaseBO.INVALID_CASE_ID, couponCode2);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		CouponCode couponCodeCreate2 = (CouponCode) couponCodeMapper.create(params2);
		Assert.assertTrue(couponCodeCreate2 == null && EnumErrorCode.values()[Integer.parseInt(params2.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined,
				"创建对象失败。param=" + params2 + "\n\r bonusRule=" + couponCode2);
		//
		BaseCouponTest.deleteViaMapper(couponCreate);
	}

	@Test
	public void createTest5() throws Exception {
		Shared.printTestMethodStartInfo();
		//
		Shared.caseLog("Case5:优惠券已经被删除，不能再领取");
		// 创建优惠券
		Coupon coupon = BaseCouponTest.DataInput.getCoupon();
		Coupon couponCreate = BaseCouponTest.createViaMapper(coupon);
		// 领取优惠券
		CouponCode couponCode = BaseCouponCodeTest.DataInput.getCouponCode(Shared.DEFAULT_VIP_ID, couponCreate.getID());
		CouponCode couponCodeCreate = BaseCouponCodeTest.createViaMapper(couponCode);
		// 删除优惠券，就不能再被领取
		BaseCouponTest.deleteViaMapper(couponCreate);
		// 领取优惠券失败
		CouponCode couponCode2 = BaseCouponCodeTest.DataInput.getCouponCode(Shared.DEFAULT_VIP_ID, couponCreate.getID());
		Map<String, Object> params = couponCode2.getCreateParam(BaseBO.INVALID_CASE_ID, couponCode2);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		CouponCode couponCodeCreate2 = (CouponCode) couponCodeMapper.create(params);
		Assert.assertTrue(couponCodeCreate2 == null && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoSuchData, "错误码不正确");
		BaseCouponCodeTest.deleteViaMapper(couponCodeCreate);
	}

	@Test
	public void createTest6() throws Exception {
		Shared.printTestMethodStartInfo();
		//
		Shared.caseLog("Case6:会员已超过领取该优惠券的个人数目上限，无法领取");
		// 创建优惠券
		Coupon coupon = BaseCouponTest.DataInput.getCoupon();
		Coupon couponCreate = BaseCouponTest.createViaMapper(coupon);
		// 领取优惠券
		CouponCode couponCode = BaseCouponCodeTest.DataInput.getCouponCode(Shared.DEFAULT_VIP_ID, couponCreate.getID());
		CouponCode couponCodeCreate = BaseCouponCodeTest.createViaMapper(couponCode);
		// 第二次领取优惠券
		CouponCode couponCode2 = BaseCouponCodeTest.DataInput.getCouponCode(Shared.DEFAULT_VIP_ID, couponCreate.getID());
		Map<String, Object> params = couponCode2.getCreateParam(BaseBO.INVALID_CASE_ID, couponCode2);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		CouponCode couponCodeCreate2 = (CouponCode) couponCodeMapper.create(params);
		Assert.assertTrue(couponCodeCreate2 == null && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined, "错误码不正确");
		BaseCouponCodeTest.deleteViaMapper(couponCodeCreate);
	}

	@Test
	public void createTest7() throws Exception {
		Shared.printTestMethodStartInfo();
		//
		Shared.caseLog("Case7:使用积分领取优惠券");
		// 创建优惠券
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
		CouponCode couponCodeCreate = BaseCouponCodeTest.createViaMapper(couponCode);
		// 查询会员信息进行结果验证
		Vip vipR1 = BaseVipTest.retrieve1ViaMapper(vipCreate, Shared.DBName_Test);
		Assert.assertTrue(vipR1.getBonus() == vipUpdateInDB.getBonus() - couponCreate.getBonus(), "会员积分没有正确计算");
		//
		BaseCouponCodeTest.deleteViaMapper(couponCodeCreate);
		BaseCouponTest.deleteViaMapper(couponCreate);
	}

	@Test
	public void createTest8() throws Exception {
		Shared.printTestMethodStartInfo();
		//
		Shared.caseLog("Case8:会员积分不足,无法领取该优惠券");
		// 创建优惠券
		Coupon coupon = BaseCouponTest.DataInput.getCoupon();
		coupon.setBonus(20);
		Coupon couponCreate = BaseCouponTest.createViaMapper(coupon);
		// 领取优惠券
		CouponCode couponCode = BaseCouponCodeTest.DataInput.getCouponCode(Shared.DEFAULT_VIP_ID, couponCreate.getID());
		Map<String, Object> params = couponCode.getCreateParam(BaseBO.INVALID_CASE_ID, couponCode);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		CouponCode couponCodeCreate = (CouponCode) couponCodeMapper.create(params);
		Assert.assertTrue(couponCodeCreate == null && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined,
				"创建对象失败。param=" + params + "\n\r bonusRule=" + couponCode);
		//
		BaseCouponTest.deleteViaMapper(couponCreate);
	}

	@Test
	public void retrieve1Test1() throws Exception {
		Shared.printTestMethodStartInfo();
		//
		Shared.caseLog("Case1:正常查询单张优惠券");
		// 创建优惠券
		Coupon coupon = BaseCouponTest.DataInput.getCoupon();
		Coupon couponCreate = BaseCouponTest.createViaMapper(coupon);
		// 领取优惠券
		CouponCode couponCode = BaseCouponCodeTest.DataInput.getCouponCode(Shared.DEFAULT_VIP_ID, couponCreate.getID());
		CouponCode couponCodeCreate = BaseCouponCodeTest.createViaMapper(couponCode);
		//
		CouponCode couponCodeRetrieve1 = (CouponCode) BaseCouponCodeTest.retrieve1ViaMapper(couponCodeCreate);
		Assert.assertTrue(couponCodeRetrieve1 != null);
		//
		BaseCouponCodeTest.deleteViaMapper(couponCodeCreate);
		BaseCouponTest.deleteViaMapper(couponCreate);
	}

	@Test
	public void retrieveNTest1() throws Exception {
		Shared.printTestMethodStartInfo();
		//
		Shared.caseLog("Case1:查询多个优惠券");
		// 创建优惠券
		Coupon coupon = BaseCouponTest.DataInput.getCoupon();
		Coupon couponCreate = BaseCouponTest.createViaMapper(coupon);
		// 领取优惠券
		CouponCode couponCode = BaseCouponCodeTest.DataInput.getCouponCode(Shared.DEFAULT_VIP_ID, couponCreate.getID());
		CouponCode couponCodeCreate = BaseCouponCodeTest.createViaMapper(couponCode);
		//
		CouponCode couponCodeToRN = new CouponCode();
		couponCodeToRN.setVipID(BaseAction.INVALID_ID);
		couponCodeToRN.setCouponID(BaseAction.INVALID_ID);
		Map<String, Object> params = couponCode.getRetrieveNParam(BaseBO.INVALID_CASE_ID, couponCodeToRN);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> couponCodeRetrieveN = couponCodeMapper.retrieveN(params);
		//
		Assert.assertTrue(couponCodeRetrieveN.size() > 0 && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, //
				params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		//
		String err = "";
		for (BaseModel bm : couponCodeRetrieveN) {
			err = ((CouponCode) bm).checkCreate(BaseBO.INVALID_CASE_ID);
			Assert.assertTrue("".equals(err), err);
		}

		//
		BaseCouponCodeTest.deleteViaMapper(couponCodeCreate);
		BaseCouponTest.deleteViaMapper(couponCreate);
	}

	@Test
	public void retrieveNTest2() throws Exception {
		Shared.printTestMethodStartInfo();
		//
		Shared.caseLog("Case2:根据VipID查询多个优惠券");
		// 创建优惠券
		Coupon coupon = BaseCouponTest.DataInput.getCoupon();
		Coupon couponCreate = BaseCouponTest.createViaMapper(coupon);
		// 领取优惠券
		CouponCode couponCode = BaseCouponCodeTest.DataInput.getCouponCode(Shared.DEFAULT_VIP_ID, couponCreate.getID());
		CouponCode couponCodeCreate = BaseCouponCodeTest.createViaMapper(couponCode);
		//
		CouponCode couponCodeToRN = new CouponCode();
		couponCodeToRN.setVipID(Shared.DEFAULT_VIP_ID);
		couponCodeToRN.setCouponID(BaseAction.INVALID_ID);
		Map<String, Object> params = couponCode.getRetrieveNParam(BaseBO.INVALID_CASE_ID, couponCodeToRN);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> couponCodeRetrieveN = couponCodeMapper.retrieveN(params);
		//
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, //
				params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		//
		String err = "";
		for (BaseModel bm : couponCodeRetrieveN) {
			CouponCode cc = (CouponCode) bm;
			err = cc.checkCreate(BaseBO.INVALID_CASE_ID);
			Assert.assertTrue("".equals(err), err);
			Assert.assertTrue(cc.getVipID() == Shared.DEFAULT_VIP_ID, "查询出来的优惠券信息不正确");
		}

		//
		BaseCouponCodeTest.deleteViaMapper(couponCodeCreate);
		BaseCouponTest.deleteViaMapper(couponCreate);
	}

	@Test
	public void retrieveNTest3() throws Exception {
		Shared.printTestMethodStartInfo();
		//
		Shared.caseLog("Case3:根据CouponID查询多个优惠券");
		// 创建优惠券
		Coupon coupon = BaseCouponTest.DataInput.getCoupon();
		Coupon couponCreate = BaseCouponTest.createViaMapper(coupon);
		// 领取优惠券
		CouponCode couponCode = BaseCouponCodeTest.DataInput.getCouponCode(Shared.DEFAULT_VIP_ID, couponCreate.getID());
		CouponCode couponCodeCreate = BaseCouponCodeTest.createViaMapper(couponCode);
		//
		CouponCode couponCodeToRN = new CouponCode();
		couponCodeToRN.setVipID(BaseAction.INVALID_ID);
		couponCodeToRN.setCouponID(couponCreate.getID());
		Map<String, Object> params = couponCode.getRetrieveNParam(BaseBO.INVALID_CASE_ID, couponCodeToRN);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> couponCodeRetrieveN = couponCodeMapper.retrieveN(params);
		//
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, //
				params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		//
		String err = "";
		for (BaseModel bm : couponCodeRetrieveN) {
			CouponCode cc = (CouponCode) bm;
			err = cc.checkCreate(BaseBO.INVALID_CASE_ID);
			Assert.assertTrue("".equals(err), err);
			Assert.assertTrue(cc.getCouponID() == couponCreate.getID(), "查询出来的优惠券信息不正确");
		}

		//
		BaseCouponCodeTest.deleteViaMapper(couponCodeCreate);
		BaseCouponTest.deleteViaMapper(couponCreate);
	}

	@Test
	public void retrieveNTest4() throws Exception {
		Shared.printTestMethodStartInfo();
		//
		Shared.caseLog("Case4:根据多个条件查询多个优惠券");
		// 创建优惠券
		Coupon coupon = BaseCouponTest.DataInput.getCoupon();
		Coupon couponCreate = BaseCouponTest.createViaMapper(coupon);
		// 领取优惠券
		CouponCode couponCode = BaseCouponCodeTest.DataInput.getCouponCode(Shared.DEFAULT_VIP_ID, couponCreate.getID());
		CouponCode couponCodeCreate = BaseCouponCodeTest.createViaMapper(couponCode);
		//
		CouponCode couponCodeToRN = new CouponCode();
		couponCodeToRN.setVipID(Shared.DEFAULT_VIP_ID);
		couponCodeToRN.setCouponID(couponCreate.getID());
		Map<String, Object> params = couponCode.getRetrieveNParam(BaseBO.INVALID_CASE_ID, couponCodeToRN);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> couponCodeRetrieveN = couponCodeMapper.retrieveN(params);
		//
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, //
				params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		//
		String err = "";
		for (BaseModel bm : couponCodeRetrieveN) {
			CouponCode cc = (CouponCode) bm;
			err = cc.checkCreate(BaseBO.INVALID_CASE_ID);
			Assert.assertTrue("".equals(err), err);
			Assert.assertTrue(cc.getVipID() == Shared.DEFAULT_VIP_ID && cc.getCouponID() == couponCreate.getID(), "查询出来的优惠券信息不正确");
		}

		//
		BaseCouponCodeTest.deleteViaMapper(couponCodeCreate);
		BaseCouponTest.deleteViaMapper(couponCreate);
	}

	@Test
	public void retrieveNTest5() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case5:根据status查询多个优惠券");

		// 创建优惠券
		Coupon coupon = BaseCouponTest.DataInput.getCoupon();
		Coupon couponCreate = BaseCouponTest.createViaMapper(coupon);
		// 领取优惠券
		CouponCode couponCode = BaseCouponCodeTest.DataInput.getCouponCode(Shared.DEFAULT_VIP_ID, couponCreate.getID());
		CouponCode couponCodeCreate = BaseCouponCodeTest.createViaMapper(couponCode);
		// 核销优惠券
		CouponCode couponCodeConsumed = (CouponCode) BaseCouponCodeTest.updateViaMapper(couponCodeCreate);
		Assert.assertTrue(couponCodeConsumed.getStatus() == EnumCouponCodeStatus.ECCS_Consumed.getIndex(), "优惠券状态不正确，应该是已核销状态");

		CouponCode couponCodeRN = new CouponCode();
		couponCodeRN.setStatus(couponCodeConsumed.getStatus());
		List<BaseModel> list = BaseCouponCodeTest.retrieveNViaMapper(couponCodeRN, BaseBO.INVALID_CASE_ID);
		Assert.assertTrue(list != null && list.size() > 0, "根据状态查询失败");

		//
		BaseCouponCodeTest.deleteViaMapper(couponCodeCreate);
		BaseCouponTest.deleteViaMapper(couponCreate);
	}

	@Test
	public void updateTest1() throws Exception {
		Shared.printTestMethodStartInfo();
		//
		Shared.caseLog("Case1:正常核销优惠券");
		// 创建优惠券
		Coupon coupon = BaseCouponTest.DataInput.getCoupon();
		Coupon couponCreate = BaseCouponTest.createViaMapper(coupon);
		// 领取优惠券
		CouponCode couponCode = BaseCouponCodeTest.DataInput.getCouponCode(Shared.DEFAULT_VIP_ID, couponCreate.getID());
		CouponCode couponCodeCreate = BaseCouponCodeTest.createViaMapper(couponCode);
		//
		CouponCode couponCodeConsumed = (CouponCode) BaseCouponCodeTest.updateViaMapper(couponCodeCreate);
		Assert.assertTrue(couponCodeConsumed.getStatus() == EnumCouponCodeStatus.ECCS_Consumed.getIndex(), "优惠券状态不正确，应该是已核销状态");
		//
		BaseCouponCodeTest.deleteViaMapper(couponCodeCreate);
		BaseCouponTest.deleteViaMapper(couponCreate);
	}

	@Test
	public void updateTest2() throws Exception {
		Shared.printTestMethodStartInfo();
		//
		Shared.caseLog("Case2:核销优惠券优惠券不存在");
		//
		CouponCode couponCode = new CouponCode();
		Map<String, Object> params = couponCode.getUpdateParam(BaseBO.CASE_CouponCode_Consume, couponCode);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		CouponCode couponCodeConsumed = (CouponCode) couponCodeMapper.consume(params);
		//
		Assert.assertTrue(couponCodeConsumed == null && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoSuchData, //
				params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
	}

	@Test
	public void deleteTest1() throws Exception {
		Shared.printTestMethodStartInfo();
		//
		Shared.caseLog("Case1:正常删除优惠券");
		// 创建优惠券
		Coupon coupon = BaseCouponTest.DataInput.getCoupon();
		Coupon couponCreate = BaseCouponTest.createViaMapper(coupon);
		// 领取优惠券
		CouponCode couponCode = BaseCouponCodeTest.DataInput.getCouponCode(Shared.DEFAULT_VIP_ID, couponCreate.getID());
		CouponCode couponCodeCreate = BaseCouponCodeTest.createViaMapper(couponCode);
		//
		BaseCouponCodeTest.deleteViaMapper(couponCodeCreate);
		BaseCouponTest.deleteViaMapper(couponCreate);
	}

	@Test
	public void retrieveNTotalByVipIDTest1() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case1:查询某个会员的优惠券总数");
		// 创建会员
		Vip createVip = BaseVipTest.createViaMapper(BaseBO.INVALID_CASE_ID, BaseVipTest.DataInput.getVip(), EnumErrorCode.EC_NoError, Shared.DBName_Test);
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
		int total = BaseCouponCodeTest.retrieveNTotalByVipIDViaMapper(queryCouponCode, Shared.DBName_Test);
		Assert.assertTrue(total == 2, "查询失败");
		// 删除数据
		BaseCouponCodeTest.deleteViaMapper(couponCodeCreateA);
		BaseCouponCodeTest.deleteViaMapper(couponCodeCreateB);
		BaseCouponTest.deleteViaMapper(couponCreate);
	}

	@Test
	public void retrieveNTotalByVipIDTest2() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case2:查询不存在的会员优惠券总数");
		CouponCode queryCouponCode = new CouponCode();
		queryCouponCode.setVipID(BaseAction.INVALID_ID);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Map<String, Object> params = queryCouponCode.getRetrieveNParam(BaseBO.CASE_CouponCode_retrieveNTotalByVipID, queryCouponCode);
		couponCodeMapper.retrieveNTotalByVipID(params);
		//
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoSuchData, //
				params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
	}

	@Test
	public void retrieveNTotalByVipIDTest3() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case3:查询的会员优惠券全部都已核销");
		// 创建会员
		Vip createVip = BaseVipTest.createViaMapper(BaseBO.INVALID_CASE_ID, BaseVipTest.DataInput.getVip(), EnumErrorCode.EC_NoError, Shared.DBName_Test);
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
		int total = BaseCouponCodeTest.retrieveNTotalByVipIDViaMapper(queryCouponCode, Shared.DBName_Test);
		Assert.assertTrue(total == 0, "查询失败");
		// 删除数据
		BaseCouponCodeTest.deleteViaMapper(couponCodeCreateA);
		BaseCouponCodeTest.deleteViaMapper(couponCodeCreateB);
		BaseCouponTest.deleteViaMapper(couponCreate);
	}

	@Test
	public void retrieveNTotalByVipIDTest4() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case4:这种优惠券已经删除,但是会员领取优惠券还未使用(能查询出来)");
		// 创建会员
		Vip createVip = BaseVipTest.createViaMapper(BaseBO.INVALID_CASE_ID, BaseVipTest.DataInput.getVip(), EnumErrorCode.EC_NoError, Shared.DBName_Test);
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
		int total = BaseCouponCodeTest.retrieveNTotalByVipIDViaMapper(queryCouponCode, Shared.DBName_Test);
		Assert.assertTrue(total == 1, "查询失败");
		// 删除数据
		BaseCouponCodeTest.deleteViaMapper(couponCodeCreateA);
	}

	@Test
	public void retrieveNTotalByVipIDTest5() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case5:这种优惠券已经过期,但是会员领取优惠券还未使用(不能查询出来)");
		// 创建会员
		Vip createVip = BaseVipTest.createViaMapper(BaseBO.INVALID_CASE_ID, BaseVipTest.DataInput.getVip(), EnumErrorCode.EC_NoError, Shared.DBName_Test);
		// 创建优惠券
		Coupon coupon = BaseCouponTest.DataInput.getCoupon();
		coupon.setBeginDateTime(DatetimeUtil.getDays(new Date(), -5));
		coupon.setEndDateTime(DatetimeUtil.getDays(new Date(), -1));
		Coupon couponCreate = BaseCouponTest.createViaMapper(coupon);
		// 领取优惠券
		CouponCode couponCodeA = BaseCouponCodeTest.DataInput.getCouponCode(createVip.getID(), couponCreate.getID());
		CouponCode couponCodeCreateA = BaseCouponCodeTest.createViaMapper(couponCodeA);
		// 查询
		CouponCode queryCouponCode = new CouponCode();
		queryCouponCode.setVipID(createVip.getID());
		int total = BaseCouponCodeTest.retrieveNTotalByVipIDViaMapper(queryCouponCode, Shared.DBName_Test);
		Assert.assertTrue(total == 0, "查询失败");
		// 删除数据
		BaseCouponCodeTest.deleteViaMapper(couponCodeCreateA);
		BaseCouponTest.deleteViaMapper(couponCreate);
	}

	private void retrieveNByVipID(int total, int subStatus) throws Exception {
		// 创建会员
		Vip createVip = BaseVipTest.createViaMapper(BaseBO.INVALID_CASE_ID, BaseVipTest.DataInput.getVip(), EnumErrorCode.EC_NoError, Shared.DBName_Test);
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
		List<BaseModel> couponCodeList = BaseCouponCodeTest.retrieveNViaMapper(queryCouponCode, BaseBO.CASE_CouponCode_retrieveNByVipID);
		Assert.assertTrue(couponCodeList.size() == total, "查询此会员全部优惠券失败");
		// 删除数据
		BaseCouponCodeTest.deleteViaMapper(couponCodeCreateA);
		BaseCouponCodeTest.deleteViaMapper(couponCodeCreateB);
		BaseCouponTest.deleteViaMapper(couponCreate);
	}

	@Test
	public void retrieveNByVipIDTest1() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case1:查询某个会员的全部优惠券");
		retrieveNByVipID(2, CouponCode.EnumSubStatusCouponCode.ESSC_All.getIndex());
	}

	@Test
	public void retrieveNByVipIDTest2() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case2:查询不存在的会员");
		CouponCode queryCouponCode = new CouponCode();
		queryCouponCode.setVipID(BaseAction.INVALID_ID);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Map<String, Object> params = queryCouponCode.getRetrieveNParam(BaseBO.CASE_CouponCode_retrieveNByVipID, queryCouponCode);
		couponCodeMapper.retrieveNByVipID(params);
		//
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoSuchData, //
				params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
	}

	@Test
	public void retrieveNByVipIDTest3() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case3:查询某个会员的全部未使用优惠券");
		retrieveNByVipID(1, CouponCode.EnumSubStatusCouponCode.ESSC_NotConsumed.getIndex());
	}

	@Test
	public void retrieveNByVipIDTest4() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case4:查询某个会员的全部已使用优惠券");
		retrieveNByVipID(1, CouponCode.EnumSubStatusCouponCode.ESSC_Consumed.getIndex());
	}

	@Test
	public void retrieveNByVipIDTest5() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case5:查询某个会员的全部已过期优惠券");
		// 创建会员
		Vip createVip = BaseVipTest.createViaMapper(BaseBO.INVALID_CASE_ID, BaseVipTest.DataInput.getVip(), EnumErrorCode.EC_NoError, Shared.DBName_Test);
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
		List<BaseModel> couponCodeList = BaseCouponCodeTest.retrieveNViaMapper(queryCouponCode, BaseBO.CASE_CouponCode_retrieveNByVipID);
		Assert.assertTrue(couponCodeList.size() == 2, "查询此会员全部优惠券失败");
		// 删除数据
		BaseCouponCodeTest.deleteViaMapper(couponCodeCreateA);
		BaseCouponCodeTest.deleteViaMapper(couponCodeCreateB);
		BaseCouponTest.deleteViaMapper(couponCreate);
	}

	@Test
	public void retrieveNByVipIDTest6() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case6:一种优惠券已删除,此会员领取了该优惠券没有使用。查询会员的全部未使用惠券(能查询出)");
		// 创建会员
		Vip createVip = BaseVipTest.createViaMapper(BaseBO.INVALID_CASE_ID, BaseVipTest.DataInput.getVip(), EnumErrorCode.EC_NoError, Shared.DBName_Test);
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
		List<BaseModel> couponCodeList = BaseCouponCodeTest.retrieveNViaMapper(queryCouponCode, BaseBO.CASE_CouponCode_retrieveNByVipID);
		Assert.assertTrue(couponCodeList.size() == 2, "查询此会员全部优惠券失败");
		// 删除数据
		BaseCouponCodeTest.deleteViaMapper(couponCodeCreateA);
		BaseCouponCodeTest.deleteViaMapper(couponCodeCreateB);
	}
}
