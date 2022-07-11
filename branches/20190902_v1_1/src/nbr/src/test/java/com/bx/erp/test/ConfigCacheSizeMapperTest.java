package com.bx.erp.test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.util.List;
import java.util.Map;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.config.ConfigCacheSize;
import com.bx.erp.util.DataSourceContextHolder;
import com.bx.erp.util.FieldFormat;

public class ConfigCacheSizeMapperTest extends BaseMapperTest {

	public static class DataInput {
		protected static final ConfigCacheSize getConfigCacheSize() throws Exception {
			ConfigCacheSize c = new ConfigCacheSize();
			c.setName("aa" + String.valueOf(System.currentTimeMillis()).substring(6));
			Thread.sleep(1);
			c.setValue(String.valueOf(System.currentTimeMillis()).substring(6));
			Thread.sleep(1);

			return (ConfigCacheSize) c.clone();
		}
	}

	@BeforeClass
	public void setup() {
		super.setUp();
	}

	@AfterClass
	public void tearDown() {
		super.tearDown();
	}

	@Test
	public void updateTest() throws Exception {
		Shared.printTestMethodStartInfo();

		System.out.println("\n------------------------ update ConfigCacheSize Test ------------------------");

		ConfigCacheSize configCacheSize = DataInput.getConfigCacheSize();
		configCacheSize.setID(1);
		//
		String error = configCacheSize.checkUpdate(BaseBO.INVALID_CASE_ID);
		assertEquals(error, "");
		//
		Map<String, Object> params = configCacheSize.getUpdateParam(BaseBO.INVALID_CASE_ID, configCacheSize);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		ConfigCacheSize configCacheSizeCreate = (ConfigCacheSize) configCacheSizeMapper.update(params);
		assertTrue(configCacheSizeCreate != null && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "修改对象失败");
		configCacheSize.setIgnoreIDInComparision(true);
		if (configCacheSize.compareTo(configCacheSizeCreate) != 0) {
			assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
	}

	@Test
	public void retrieve1Test1() throws Exception {
		Shared.printTestMethodStartInfo();

		System.out.println("\n------------------------ retrieve1 ConfigCacheSize Test ------------------------");

		ConfigCacheSize configCacheSize = DataInput.getConfigCacheSize();
		configCacheSize.setID(1);
		//
		String error = configCacheSize.checkRetrieve1(BaseBO.INVALID_CASE_ID);
		assertEquals(error, "");
		//
		Map<String, Object> params = configCacheSize.getRetrieve1Param(BaseBO.INVALID_CASE_ID, configCacheSize);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		ConfigCacheSize configCacheSizeCreate = (ConfigCacheSize) configCacheSizeMapper.retrieve1(params);
		assertTrue(configCacheSizeCreate != null && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "查询对象失败");
		assertTrue(configCacheSizeCreate.getID() == configCacheSize.getID());
	}

	@Test
	public void retrieve1Test2() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case2:使用0进行查询");
		//
		ConfigCacheSize configCacheSize = DataInput.getConfigCacheSize();
		configCacheSize.setID(0);
		//
		String error = configCacheSize.checkRetrieve1(BaseBO.INVALID_CASE_ID);
		assertEquals(error, FieldFormat.FIELD_ERROR_ID);
		//
		Map<String, Object> params = configCacheSize.getRetrieve1Param(BaseBO.INVALID_CASE_ID, configCacheSize);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		ConfigCacheSize configCacheSizeCreate = (ConfigCacheSize) configCacheSizeMapper.retrieve1(params);
		assertTrue(configCacheSizeCreate == null && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "查询对象失败");
	}

	@Test
	public void retrieve1Test3() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case3:使用负数进行查询");
		//
		ConfigCacheSize configCacheSize = DataInput.getConfigCacheSize();
		configCacheSize.setID(-99);
		//
		String error = configCacheSize.checkRetrieve1(BaseBO.INVALID_CASE_ID);
		assertEquals(error, FieldFormat.FIELD_ERROR_ID);
		//
		Map<String, Object> params = configCacheSize.getRetrieve1Param(BaseBO.INVALID_CASE_ID, configCacheSize);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		ConfigCacheSize configCacheSizeCreate = (ConfigCacheSize) configCacheSizeMapper.retrieve1(params);
		assertTrue(configCacheSizeCreate == null && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "查询对象失败");
	}

	@Test
	public void retrieveNTest() throws Exception {
		Shared.printTestMethodStartInfo();

		System.out.println("\n------------------------ retrieveN ConfigCacheSize Test ------------------------");

		ConfigCacheSize configCacheSize = DataInput.getConfigCacheSize();

		Map<String, Object> params = configCacheSize.getRetrieveNParam(BaseBO.INVALID_CASE_ID, configCacheSize);

		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> configCacheSizeCreate = configCacheSizeMapper.retrieveN(params);

		assertTrue(configCacheSizeCreate != null && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "查询对象失败");

		System.out.println("查询ConfigCacheSize 成功：" + configCacheSizeCreate);
	}
}
