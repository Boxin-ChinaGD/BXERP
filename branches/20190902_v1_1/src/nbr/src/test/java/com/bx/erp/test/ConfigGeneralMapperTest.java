package com.bx.erp.test;

import static org.testng.Assert.assertTrue;

import java.util.List;
import java.util.Map;
import java.util.Random;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.cache.BaseCache;
import com.bx.erp.model.config.ConfigGeneral;
import com.bx.erp.util.DataSourceContextHolder;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;

public class ConfigGeneralMapperTest extends BaseMapperTest {
	public static class DataInput {
		protected static final ConfigGeneral getConfigGeneral() throws Exception {
			ConfigGeneral c = new ConfigGeneral();
			// 配置不能乱设置名字和值 ！
			// c.setName("aa" + String.valueOf(System.currentTimeMillis()).substring(6));
			// Thread.sleep(1);
			// c.setValue(String.valueOf(System.currentTimeMillis()).substring(6));
			// Thread.sleep(1);

			return (ConfigGeneral) c.clone();
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

		System.out.println("\n------------------------ case 1: update ConfigGeneral：ACTIVE_SMALLSHEET_ID Test ------------------------");

		ConfigGeneral configGeneral = DataInput.getConfigGeneral();
		configGeneral.setID(BaseCache.ACTIVE_SMALLSHEET_ID);
		configGeneral.setValue(String.valueOf(new Random().nextInt(10) + 1));

		Map<String, Object> params = configGeneral.getUpdateParam(BaseBO.INVALID_CASE_ID, configGeneral);

		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		ConfigGeneral configGeneralUpdate = (ConfigGeneral) configGeneralMapper.update(params);

		assertTrue(configGeneralUpdate != null && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "修改对象失败");

		configGeneral.setIgnoreIDInComparision(true);
		if (configGeneral.compareTo(configGeneralUpdate) != 0) {
			assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}

		System.out.println("\n------------------------ case 2: update ConfigGeneral：RetailTradeMonthlyReportSummaryTaskScanStartTime Test ------------------------");

		configGeneral = DataInput.getConfigGeneral();
		configGeneral.setID(BaseCache.RetailTradeMonthlyReportSummaryTaskScanStartTime);
		configGeneral.setValue("00:18:01");

		params = configGeneral.getUpdateParam(BaseBO.INVALID_CASE_ID, configGeneral);

		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		configGeneralUpdate = (ConfigGeneral) configGeneralMapper.update(params);

		assertTrue(configGeneralUpdate != null && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "修改对象失败");

		configGeneral.setIgnoreIDInComparision(true);
		if (configGeneral.compareTo(configGeneralUpdate) != 0) {
			assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}

		// ...还有很多配置行可以编写测试
	}

	@Test
	public void retrieve1Test() throws Exception {
		Shared.printTestMethodStartInfo();

		System.out.println("\n------------------------ retrieve1 ConfigGeneral Test ------------------------");

		ConfigGeneral configGeneral = DataInput.getConfigGeneral();
		configGeneral.setID(BaseCache.RetailTradeMonthlyReportSummaryTaskScanEndTime);

		Map<String, Object> params = configGeneral.getRetrieve1Param(BaseBO.INVALID_CASE_ID, configGeneral);

		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		ConfigGeneral configGeneralCreate = (ConfigGeneral) configGeneralMapper.retrieve1(params);

		assertTrue(configGeneralCreate != null && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "查询对象失败");

		// ...还有很多配置行可以编写测试
	}

	@Test
	public void retrieveNTest() throws Exception {
		Shared.printTestMethodStartInfo();

		System.out.println("\n------------------------ retrieveN ConfigGeneral Test ------------------------");

		ConfigGeneral configGeneral = DataInput.getConfigGeneral();
		configGeneral.setPageSize(BaseAction.PAGE_SIZE_Infinite);

		Map<String, Object> params = configGeneral.getRetrieveNParam(BaseBO.INVALID_CASE_ID, configGeneral);

		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> configGeneralList = configGeneralMapper.retrieveN(params);

		assertTrue(configGeneralList != null //
				&& EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError //
				&& configGeneralList.size() == BaseCache.BonusTaskScanEndTime, "查询对象失败"); // 特殊的结果验证
	}
}
