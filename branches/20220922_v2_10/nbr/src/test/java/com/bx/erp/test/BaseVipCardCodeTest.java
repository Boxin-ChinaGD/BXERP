package com.bx.erp.test;

import java.util.List;
import java.util.Map;

import org.junit.AfterClass;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.VipCardCode;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.util.DataSourceContextHolder;

public class BaseVipCardCodeTest extends BaseMapperTest {
	@BeforeClass
	public void setup() {
	}

	@AfterClass
	public void tearDown() {
	}

	public static class DataInput {
		private static VipCardCode vipCardCodeInput = null;

		public static final VipCardCode getVipCardCode(int vipID,int vipCardID) throws CloneNotSupportedException {
			vipCardCodeInput = new VipCardCode();
			vipCardCodeInput.setVipID(vipID);
			vipCardCodeInput.setVipCardID(vipCardID);
			vipCardCodeInput.setCompanySN(Shared.DB_SN_Test);

			return (VipCardCode) vipCardCodeInput.clone();
		}

	}
	
	public static VipCardCode createViaMapper(VipCardCode vipCardCode, String dbName, int iCaseID, EnumErrorCode eec) {
		Map<String, Object> params = vipCardCode.getCreateParam(iCaseID, vipCardCode);
		//
		DataSourceContextHolder.setDbName(dbName);
		VipCardCode vipCardCodeCreate = (VipCardCode) vipCardCodeMapper.create(params);
		if(eec == EnumErrorCode.EC_NoError) {
			Assert.assertTrue(vipCardCodeCreate != null && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError,
					"创建对象失败。param=" + params + "\n\r vipCardCode=" + vipCardCode);
			//
			vipCardCode.setIgnoreIDInComparision(true);
			if (vipCardCode.compareTo(vipCardCodeCreate) != 0) {
				Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
			}
			String sn = vipCardCodeCreate.getSN();
			vipCardCodeCreate.setSN(vipCardCode.getSN());
			//
			String error1 = vipCardCodeCreate.checkCreate(iCaseID);
			Assert.assertEquals(error1, "");
			//
			vipCardCodeCreate.setSN(sn);
			//
			return vipCardCodeCreate;
		} else {
			Assert.assertTrue(vipCardCodeCreate == null && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == eec,
					"创建对象失败。param=" + params + "\n\r vipCardCode=" + vipCardCode);
			return null;
		}
	}
	
	public static void deleteViaMapper(VipCardCode vipCardCode) {
		Map<String, Object> paramsDelete = vipCardCode.getDeleteParam(BaseBO.INVALID_CASE_ID, vipCardCode);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		vipCardCodeMapper.delete(paramsDelete);
		//
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(paramsDelete.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, //
				paramsDelete.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		// 验证是否查询成功
		BaseModel vipCardCodeRetrieve1 = retrieve1ViaMapper(vipCardCode);
		Assert.assertTrue(vipCardCodeRetrieve1 == null);
	}

	public static BaseModel retrieve1ViaMapper(VipCardCode vipCardCode) {
		Map<String, Object> params = vipCardCode.getRetrieve1Param(BaseBO.INVALID_CASE_ID, vipCardCode);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		VipCardCode vipCardCodeRetrieve1 = (VipCardCode) vipCardCodeMapper.retrieve1(params);
		//
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, //
				params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());

		if (vipCardCodeRetrieve1 != null) {
			String sn = vipCardCodeRetrieve1.getSN();
			vipCardCodeRetrieve1.setSN(null);
			//
			String err = vipCardCodeRetrieve1.checkCreate(BaseBO.INVALID_CASE_ID);
			Assert.assertTrue("".equals(err), err);
			//
			vipCardCodeRetrieve1.setSN(sn);
		}
		//
		return vipCardCodeRetrieve1;
	}
	
	public static List<BaseModel> retrieveNViaMapper(VipCardCode vipCardCode, String dbName) {
		Map<String, Object> params = vipCardCode.getRetrieveNParam(BaseBO.INVALID_CASE_ID, vipCardCode);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> vipCardCodeRetrieveN = vipCardCodeMapper.retrieveN(params);
		//
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, //
				params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		//
		String err = "";
		for (BaseModel bm : vipCardCodeRetrieveN) {
			VipCardCode vcc = (VipCardCode) bm;
			String sn = vcc.getSN();
			vcc.setSN(null);
			//
			err = vcc.checkCreate(BaseBO.INVALID_CASE_ID);
			Assert.assertTrue("".equals(err), err);
			//
			vcc.setSN(sn);
		}
		
		return vipCardCodeRetrieveN;
	}
}
