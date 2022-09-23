package com.bx.erp.test.commodity;

import java.util.HashMap;
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
import com.bx.erp.model.commodity.CategorySyncCache;
import com.bx.erp.model.commodity.CategorySyncCacheDispatcher;
import com.bx.erp.test.BaseMapperTest;
import com.bx.erp.test.Shared;
import com.bx.erp.util.DataSourceContextHolder;

public class CategorySyncCacheDispatcherMapperTest extends BaseMapperTest {

	public static final int POS_ID1 = 1;

	public static class DataInput {
		private static CategorySyncCacheDispatcher bcd = new CategorySyncCacheDispatcher();

		protected static final CategorySyncCacheDispatcher getSyncCacheDispatcher() throws Exception {
			bcd = new CategorySyncCacheDispatcher();
			bcd.setSyncCacheID(new Random().nextInt(5) + 1);
			bcd.setPos_ID(new Random().nextInt(5) + 1);

			return (CategorySyncCacheDispatcher) bcd.clone();
		}

		private static CategorySyncCache cc = new CategorySyncCache();

		protected static final CategorySyncCache getCategorySyncCache() throws CloneNotSupportedException, InterruptedException {
			cc = new CategorySyncCache();
			cc.setSyncData_ID(3);
			cc.setSyncSequence(4);// 本字段在这里无关紧要
			cc.setSyncType(SyncCache.SYNC_Type_C);
			cc.setPosID(POS_ID1);

			return (CategorySyncCache) cc.clone();
		}
	}

	@BeforeClass
	public void setup() {
		super.setUp();

	}

	@AfterClass
	public void tearDown() {
		super.tearDown();
		// 测试完成删除所有数据，以免影响其他测试
		categorySyncCacheDeleteAll();
	}

	/** 删除全部主从表数据 */
	protected void categorySyncCacheDeleteAll() {
		Map<String, Object> param = new HashMap<String, Object>();
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		categorySyncCacheMapper.deleteAll(param);
	}

	@Test
	public void createTest1() throws Exception {
		Shared.printTestMethodStartInfo();

		// case1:正常创建商品小类同步缓存调度数据
		Shared.caseLog("case1:正常创建商品小类同步缓存调度数据");
		categorySyncCacheDispatcherCreate();
	}

	@Test
	public void createTest2() throws Exception {
		Shared.printTestMethodStartInfo();

		// 创建测试数据
		CategorySyncCacheDispatcher categorySyncCacheDispatcher = categorySyncCacheDispatcherCreate();

		// case2:重复创建商品小类同步缓存调度数据
		Shared.caseLog("case2:重复创建商品小类同步缓存调度数据");
		Map<String, Object> paramsForCreate = categorySyncCacheDispatcher.getCreateParam(BaseBO.INVALID_CASE_ID, categorySyncCacheDispatcher);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		CategorySyncCacheDispatcher categorySyncCacheDispatcherCreate = (CategorySyncCacheDispatcher) categorySyncCacheDispatcherMapper.create(paramsForCreate); // ...
		//
		categorySyncCacheDispatcher.setIgnoreIDInComparision(true);
		if (categorySyncCacheDispatcher.compareTo(categorySyncCacheDispatcherCreate) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(paramsForCreate.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, paramsForCreate.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
	}

	@Test
	public void retrieveNTest() throws Exception {
		Shared.printTestMethodStartInfo();

		// 创建测试数据
		CategorySyncCacheDispatcher categorySyncCacheDispatcher = categorySyncCacheDispatcherCreate();

		// 正常查询商品小类同步缓存调度数据
		Map<String, Object> paramsForRetrieveN = categorySyncCacheDispatcher.getRetrieveNParam(BaseBO.INVALID_CASE_ID, categorySyncCacheDispatcher);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> scList = (List<BaseModel>) categorySyncCacheDispatcherMapper.retrieveN(paramsForRetrieveN); // ...
		//
		Assert.assertTrue(scList.size() > 0 && EnumErrorCode.values()[Integer.parseInt(paramsForRetrieveN.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError,
				paramsForRetrieveN.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		// 字段验证
		for (BaseModel bm : scList) {
			String error = ((CategorySyncCacheDispatcher) bm).checkCreate(BaseBO.INVALID_CASE_ID);
			Assert.assertEquals(error, "");
		}
	}

	protected CategorySyncCacheDispatcher categorySyncCacheDispatcherCreate() throws CloneNotSupportedException, InterruptedException, Exception {
		// 创建商品小类同步缓存数据
		CategorySyncCache ssc = DataInput.getCategorySyncCache();
		//
		Map<String, Object> createParam = ssc.getCreateParam(BaseBO.INVALID_CASE_ID, ssc);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<List<BaseModel>> sscCreate = categorySyncCacheMapper.createEx(createParam);
		Assert.assertTrue(sscCreate != null && EnumErrorCode.values()[Integer.parseInt(createParam.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError,
				createParam.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		//
		CategorySyncCache ssc1 = (CategorySyncCache) sscCreate.get(0).get(0);
		//
		ssc.setIgnoreIDInComparision(true);
		if (ssc.compareTo(ssc1) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		// 字段验证
		String error = ssc1.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");

		// 创建商品小类同步缓存调度数据
		CategorySyncCacheDispatcher scd = DataInput.getSyncCacheDispatcher();
		scd.setSyncCacheID(ssc1.getID());
		//
		Map<String, Object> paramsForCreate = scd.getCreateParam(BaseBO.INVALID_CASE_ID, scd);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		CategorySyncCacheDispatcher scdCreate = (CategorySyncCacheDispatcher) categorySyncCacheDispatcherMapper.create(paramsForCreate); // ...
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
