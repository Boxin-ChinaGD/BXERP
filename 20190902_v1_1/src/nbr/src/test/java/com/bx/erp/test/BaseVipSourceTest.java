package com.bx.erp.test;

import java.util.List;
import java.util.Map;

import org.junit.AfterClass;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.VipSource;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.VipSource.EnumVipSourceCode;
import com.bx.erp.util.DataSourceContextHolder;

public class BaseVipSourceTest extends BaseMapperTest {
	@BeforeClass
	public void setup() {
	}

	@AfterClass
	public void tearDown() {
	}

	public static class DataInput {
		private static VipSource vipSourceInput = null;

		public static final VipSource getVipSource(int vipID) throws CloneNotSupportedException {
			vipSourceInput = new VipSource();
			vipSourceInput.setVipID(vipID);
			vipSourceInput.setSourceCode(EnumVipSourceCode.EVSC_WX.getIndex());
			vipSourceInput.setID1("1111111111111111111111");
			vipSourceInput.setID2("2222222222222222222222");
			vipSourceInput.setID3("3333333333333333333333");

			return (VipSource) vipSourceInput.clone();
		}

	}
	
	public static VipSource createViaMapper(VipSource vipSource) {
		Map<String, Object> params = vipSource.getCreateParam(BaseBO.INVALID_CASE_ID, vipSource);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		VipSource vipCardCodeCreate = (VipSource) vipSourceMapper.create(params);
		Assert.assertTrue(vipCardCodeCreate != null && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError,
				"创建对象失败。param=" + params + "\n\r vipSource=" + vipSource);
		//
		vipSource.setIgnoreIDInComparision(true);
		if (vipSource.compareTo(vipCardCodeCreate) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		String error1 = vipCardCodeCreate.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error1, "");
		//
		return vipCardCodeCreate;
	}
	
	public static void deleteViaMapper(VipSource vipSource) {
		Map<String, Object> paramsDelete = vipSource.getDeleteParam(BaseBO.INVALID_CASE_ID, vipSource);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		vipSourceMapper.delete(paramsDelete);
		//
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(paramsDelete.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, //
				paramsDelete.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		// 验证是否查询成功
		BaseModel vipSourceRetrieve1 = retrieve1ViaMapper(vipSource);
		Assert.assertTrue(vipSourceRetrieve1 == null);
	}

	public static BaseModel retrieve1ViaMapper(VipSource vipSource) {
		Map<String, Object> params = vipSource.getRetrieve1Param(BaseBO.INVALID_CASE_ID, vipSource);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		BaseModel vipSourceRetrieve1 = vipSourceMapper.retrieve1(params);
		//
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, //
				params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());

		if (vipSourceRetrieve1 != null) {
			String err = vipSourceRetrieve1.checkCreate(BaseBO.INVALID_CASE_ID);
			Assert.assertTrue("".equals(err), err);
		}
		//
		return vipSourceRetrieve1;
	}
	
	public static List<BaseModel> retrieveNViaMapper(VipSource vipSource, String dbName) {
		Map<String, Object> params = vipSource.getRetrieveNParam(BaseBO.INVALID_CASE_ID, vipSource);
		//
		DataSourceContextHolder.setDbName(dbName);
		List<BaseModel> vipSourceRetrieveN = vipSourceMapper.retrieveN(params);
		//
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, //
				params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		//
		String err = "";
		for (BaseModel bm : vipSourceRetrieveN) {
			VipSource vs = (VipSource) bm;
			err = vs.checkCreate(BaseBO.INVALID_CASE_ID);
			Assert.assertTrue("".equals(err), err);
		}
		//
		return vipSourceRetrieveN;
	}
}
