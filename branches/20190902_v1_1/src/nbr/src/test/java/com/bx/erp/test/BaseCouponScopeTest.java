package com.bx.erp.test;

import java.util.Map;

import org.junit.AfterClass;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.CouponScope;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.util.DataSourceContextHolder;

public class BaseCouponScopeTest extends BaseMapperTest {
	@BeforeClass
	public void setup() {
	}

	@AfterClass
	public void tearDown() {
	}

	public static class DataInput {
		private static CouponScope couponScopeInput = null;

		public static final CouponScope getCouponScope(int couponID, int commodityID) throws CloneNotSupportedException {
			couponScopeInput = new CouponScope();
			couponScopeInput.setCouponID(couponID);
			couponScopeInput.setCommodityID(commodityID);

			return (CouponScope) couponScopeInput.clone();
		}

	}

	public static CouponScope createViaMapper(CouponScope couponScope) {
		Map<String, Object> params = couponScope.getCreateParam(BaseBO.INVALID_CASE_ID, couponScope);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		CouponScope couponScopeCreate = (CouponScope) couponScopeMapper.create(params);
		Assert.assertTrue(couponScopeCreate != null && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError,
				"创建对象失败。param=" + params + "\n\r bonusRule=" + couponScope);
		//
		couponScope.setIgnoreIDInComparision(true);
		if (couponScope.compareTo(couponScopeCreate) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		String error1 = couponScopeCreate.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error1, "");
		//
		return couponScopeCreate;
	}

	public static void deleteViaMapper(CouponScope couponScope) {
		Map<String, Object> paramsDelete = couponScope.getDeleteParam(BaseBO.INVALID_CASE_ID, couponScope);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		couponScopeMapper.delete(paramsDelete);
		//
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(paramsDelete.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, //
				paramsDelete.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		// 验证是否查询成功
		BaseModel bonusRuleRetrieve1 = retrieve1ViaMapper(couponScope);
		Assert.assertTrue(bonusRuleRetrieve1 == null);
	}

	public static BaseModel retrieve1ViaMapper(CouponScope couponScope) {
		Map<String, Object> params = couponScope.getRetrieve1Param(BaseBO.INVALID_CASE_ID, couponScope);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		BaseModel couponScopeRetrieve1 = couponScopeMapper.retrieve1(params);
		//
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, //
				params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());

		if (couponScopeRetrieve1 != null) {
			String err = couponScopeRetrieve1.checkCreate(BaseBO.INVALID_CASE_ID);
			Assert.assertTrue("".equals(err), err);
		}
		//
		return couponScopeRetrieve1;
	}
}
