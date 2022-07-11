package com.bx.erp.test.commodity;

import java.util.List;
import java.util.Map;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.cache.SyncCache;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.commodity.CommoditySyncCache;
import com.bx.erp.model.commodity.CommoditySyncCacheDispatcher;
import com.bx.erp.test.BaseCommodityTest;
import com.bx.erp.test.BaseMapperTest;
import com.bx.erp.test.Shared;
import com.bx.erp.util.DataSourceContextHolder;

public class CommoditySyncCacheDispatcherMapperTest extends BaseMapperTest {

	@BeforeClass
	public void setup() {
		super.setUp();

	}

	@AfterClass
	public void tearDown() {
		super.tearDown();
	}

	@Test
	public void createTest1() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		// 正常添加
		Shared.caseLog("case1:正常添加商品同步缓存调度数据");
		CommoditySyncCache csc = BaseCommodityTest.DataInput.getCommoditySyncCache(SyncCache.SYNC_Type_C);
		List<List<BaseModel>> list = BaseCommodityTest.createCommoditySyncCacheViaMapper(csc);
		CommoditySyncCache commoditySyncCache = (CommoditySyncCache) list.get(0).get(0);
		BaseCommodityTest.createCommoditySyncCacheDispatcherViaMapper(commoditySyncCache);
	}

	@Test
	public void createTest2() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		// 创建测试数据
		CommoditySyncCache csc = BaseCommodityTest.DataInput.getCommoditySyncCache(SyncCache.SYNC_Type_C);
		List<List<BaseModel>> list = BaseCommodityTest.createCommoditySyncCacheViaMapper(csc);
		CommoditySyncCache commoditySyncCache = (CommoditySyncCache) list.get(0).get(0);
		CommoditySyncCacheDispatcher commoditySyncCacheDispatcher = BaseCommodityTest.createCommoditySyncCacheDispatcherViaMapper(commoditySyncCache);

		// 重复添加
		Shared.caseLog("case2:重复添加商品同步缓存调度数据");
		Map<String, Object> paramsForCreate = commoditySyncCacheDispatcher.getCreateParam(BaseBO.INVALID_CASE_ID, commoditySyncCacheDispatcher);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		CommoditySyncCacheDispatcher commoditySyncCacheDispatcherCreate = (CommoditySyncCacheDispatcher) commoditySyncCacheDispatcherMapper.create(paramsForCreate); // ...
		//
		commoditySyncCacheDispatcher.setIgnoreIDInComparision(true);
		if (commoditySyncCacheDispatcher.compareTo(commoditySyncCacheDispatcherCreate) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(paramsForCreate.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, paramsForCreate.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
	}

	@Test
	public void retrieveNTest() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		// 创建测试数据
		CommoditySyncCache csc = BaseCommodityTest.DataInput.getCommoditySyncCache(SyncCache.SYNC_Type_C);
		List<List<BaseModel>> list = BaseCommodityTest.createCommoditySyncCacheViaMapper(csc);
		CommoditySyncCache commoditySyncCache = (CommoditySyncCache) list.get(0).get(0);
		CommoditySyncCacheDispatcher commoditySyncCacheDispatcher = BaseCommodityTest.createCommoditySyncCacheDispatcherViaMapper(commoditySyncCache);

		Shared.caseLog("case1:正常查询商品同步缓存调度数据");
		Map<String, Object> paramsForRetrieveN = commoditySyncCacheDispatcher.getRetrieveNParam(BaseBO.INVALID_CASE_ID, commoditySyncCacheDispatcher);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> scList = (List<BaseModel>) commoditySyncCacheDispatcherMapper.retrieveN(paramsForRetrieveN); // ...
		//
		Assert.assertTrue(scList.size() > 0 && EnumErrorCode.values()[Integer.parseInt(paramsForRetrieveN.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError,
				paramsForRetrieveN.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		// 字段验证
		for (BaseModel bm : scList) {
			String error = ((CommoditySyncCacheDispatcher) bm).checkCreate(BaseBO.INVALID_CASE_ID);
			Assert.assertEquals(error, "");
		}
	}

}
