package com.bx.erp.test;

import java.util.List;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.commodity.PackageUnit;
import com.bx.erp.util.DataSourceContextHolder;

public class BasePackageUnitTest extends BaseMapperTest {
	@BeforeClass
	public void setup() {
		super.setup();
	}

	@AfterClass
	public void tearDown() {
		super.tearDown();
	}

	public static class DataInput {
		private static PackageUnit puInput = null;

		public static final PackageUnit getPackageUnit() throws CloneNotSupportedException, InterruptedException {
			puInput = new PackageUnit();
			puInput.setName("箱" + String.valueOf(System.currentTimeMillis()).substring(6));
			Thread.sleep(1);

			return (PackageUnit) puInput.clone();
		}
	}

	/** 创建包装单位 */
	public static PackageUnit createViaMapper(PackageUnit packageUnit) {
		//
		Map<String, Object> paramsForCreate = packageUnit.getCreateParam(BaseBO.INVALID_CASE_ID, packageUnit);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		PackageUnit packageUnitCreate = (PackageUnit) packageUnitMapper.create(paramsForCreate);
		//
		System.out.println("创建包装单位所需的对象是" + packageUnit);
		System.out.println("创建的包装单位对象为" + packageUnitCreate);
		//
		packageUnit.setIgnoreIDInComparision(true);
		if (packageUnit.compareTo(packageUnitCreate) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		Assert.assertNotNull(packageUnitCreate);
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(paramsForCreate.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, paramsForCreate.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		// 字段检查
		String error = packageUnitCreate.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");

		return packageUnitCreate;
	}

	/** 删除包装单位 */
	public static void deleteViaMapper(PackageUnit packageUnit) {
		//
		Map<String, Object> paramsForDelete = packageUnit.getDeleteParam(BaseBO.INVALID_CASE_ID, packageUnit);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		packageUnitMapper.delete(paramsForDelete);
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(paramsForDelete.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, paramsForDelete.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());

		Map<String, Object> paramsRetrieve = packageUnit.getRetrieve1Param(BaseBO.INVALID_CASE_ID, packageUnit);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		PackageUnit packageUnitRetrieve1 = (PackageUnit) packageUnitMapper.retrieve1(paramsRetrieve);
		Assert.assertNull(packageUnitRetrieve1);
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(paramsRetrieve.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, paramsRetrieve.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
	}

	public static List<BaseModel> retrieveNViaMapper(PackageUnit packageUnit, String dbName) {
		Map<String, Object> param = packageUnit.getRetrieveNParam(BaseBO.INVALID_CASE_ID, packageUnit);
		//
		DataSourceContextHolder.setDbName(dbName);
		List<BaseModel> list = packageUnitMapper.retrieveN(param);
		//
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(param.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, param.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		return list;
	}
}
