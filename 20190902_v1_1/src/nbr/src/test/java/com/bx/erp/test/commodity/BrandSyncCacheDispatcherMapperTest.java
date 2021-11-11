package com.bx.erp.test.commodity;

import static org.testng.Assert.assertTrue;

import java.util.List;
import java.util.Map;
import java.util.Random;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.cache.SyncCache;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.commodity.BrandSyncCache;
import com.bx.erp.model.commodity.BrandSyncCacheDispatcher;
import com.bx.erp.test.BaseMapperTest;
import com.bx.erp.test.Shared;
import com.bx.erp.util.DataSourceContextHolder;

public class BrandSyncCacheDispatcherMapperTest extends BaseMapperTest {

	public static final int POS_ID1 = 1;

	public static class DataInput {
		private static BrandSyncCacheDispatcher bcd = new BrandSyncCacheDispatcher();
		private static BrandSyncCache ssc = new BrandSyncCache();

		protected static final BrandSyncCacheDispatcher getSyncCacheDispatcher() throws Exception {
			bcd = new BrandSyncCacheDispatcher();
			bcd.setSyncCacheID(new Random().nextInt(5) + 1);
			bcd.setPos_ID(new Random().nextInt(5) + 1);

			return (BrandSyncCacheDispatcher) bcd.clone();
		}

		protected static final BrandSyncCache getSyncCache() throws CloneNotSupportedException {
			ssc = new BrandSyncCache();
			ssc.setSyncData_ID(2);
			ssc.setSyncSequence(1);// 本字段在这里无关紧要
			ssc.setSyncType(SyncCache.SYNC_Type_C);
			ssc.setPosID(POS_ID1);

			return (BrandSyncCache) ssc.clone();
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
	public void createTest1() throws Exception {
		Shared.printTestMethodStartInfo();

		// 正常添加
		Shared.caseLog("Case1:正常添加品牌同步缓存调度数据");
		brandSyncCacheDispatcherCreate();
	}

	@Test
	public void createTest2() throws Exception {
		Shared.printTestMethodStartInfo();

		// 创建测试数据
		BrandSyncCacheDispatcher brandSyncCacheDispatcher = brandSyncCacheDispatcherCreate();

		// 重复添加
		Shared.caseLog("Case2：重复添加品牌同步缓存调度数据");
		Map<String, Object> paramsForCreate = brandSyncCacheDispatcher.getCreateParam(BaseBO.INVALID_CASE_ID, brandSyncCacheDispatcher);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		BrandSyncCacheDispatcher brandSyncCacheDispatcherCreate = (BrandSyncCacheDispatcher) brandSyncCacheDispatcherMapper.create(paramsForCreate); // ...
		//
		brandSyncCacheDispatcher.setIgnoreIDInComparision(true);
		if (brandSyncCacheDispatcher.compareTo(brandSyncCacheDispatcherCreate) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(paramsForCreate.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, paramsForCreate.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
	}

	@Test
	public void retrieveNTest() throws Exception {
		Shared.printTestMethodStartInfo();

		// 创建测试数据
		BrandSyncCacheDispatcher brandSyncCacheDispatcher = brandSyncCacheDispatcherCreate();

		Shared.caseLog("Case1:正常查询品牌同步缓存调度数据");
		Map<String, Object> paramsForRetrieveN = brandSyncCacheDispatcher.getRetrieveNParam(BaseBO.INVALID_CASE_ID, brandSyncCacheDispatcher);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> scList = (List<BaseModel>) brandSyncCacheDispatcherMapper.retrieveN(paramsForRetrieveN); // ...
		System.out.println(scList.toString());
		//
		Assert.assertTrue(scList.size() > 0 && EnumErrorCode.values()[Integer.parseInt(paramsForRetrieveN.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError,
				paramsForRetrieveN.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		// 字段验证
		for (BaseModel bm : scList) {
			String error = ((BrandSyncCacheDispatcher) bm).checkCreate(BaseBO.INVALID_CASE_ID);
			Assert.assertEquals(error, "");
		}
	}

	/** @param sscCreate
	 * @return
	 * @throws Exception
	 */
	protected BrandSyncCacheDispatcher brandSyncCacheDispatcherCreate() throws Exception {
		// 创建同步缓存数据
		BrandSyncCache ssc = DataInput.getSyncCache();
		//
		Map<String, Object> createParam = ssc.getCreateParam(BaseBO.INVALID_CASE_ID, ssc);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<List<BaseModel>> sscCreate = brandSyncCacheMapper.createEx(createParam);
		//
		Assert.assertTrue(sscCreate != null && EnumErrorCode.values()[Integer.parseInt(createParam.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError,
				createParam.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		//
		BrandSyncCache ssc1 = (BrandSyncCache) sscCreate.get(0).get(0);
		//
		ssc.setIgnoreIDInComparision(true);
		if (ssc.compareTo(ssc1) != 0) {
			assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		// 字段验证
		String error = ssc1.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");

		// 创建同步缓存调度表数据
		BrandSyncCacheDispatcher scd = DataInput.getSyncCacheDispatcher();
		scd.setSyncCacheID(ssc1.getID());
		Map<String, Object> paramsForCreate = scd.getCreateParam(BaseBO.INVALID_CASE_ID, scd);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		BrandSyncCacheDispatcher scdCreate = (BrandSyncCacheDispatcher) brandSyncCacheDispatcherMapper.create(paramsForCreate); // ...
		//
		scd.setIgnoreIDInComparision(true);
		if (scd.compareTo(scdCreate) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		Assert.assertTrue(scdCreate != null && EnumErrorCode.values()[Integer.parseInt(paramsForCreate.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError,
				paramsForCreate.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		// 字段验证
		String error1 = scdCreate.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error1, "");

		return scdCreate;
	}
}
