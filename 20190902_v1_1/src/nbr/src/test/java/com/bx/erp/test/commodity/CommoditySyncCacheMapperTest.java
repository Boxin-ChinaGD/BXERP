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
import com.bx.erp.model.Pos;
import com.bx.erp.model.commodity.CommoditySyncCache;
import com.bx.erp.model.commodity.CommoditySyncCacheDispatcher;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.test.BaseCommodityTest;
import com.bx.erp.test.BaseMapperTest;
import com.bx.erp.test.Shared;
import com.bx.erp.util.DataSourceContextHolder;

public class CommoditySyncCacheMapperTest extends BaseMapperTest {

	public static final int POS_ID1 = 1;
	public static final int POS_ID2 = 2;
	public static final int POS_ID3 = 3;

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

		// CASE1:正常添加，非U型，POSID > 0
		Shared.caseLog("CASE1:正常添加，非U型，POSID > 0");
		CommoditySyncCache commoditySyncCache = BaseCommodityTest.DataInput.getCommoditySyncCache(SyncCache.SYNC_Type_C);
		BaseCommodityTest.createCommoditySyncCacheViaMapper(commoditySyncCache);

		// 删除所有数据，避免对测试造成干扰
		BaseCommodityTest.deleteAllCommoditySyncCache();
	}

	@Test
	public void createTest2() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		// CASE2:重复添加，非U型，POSID > 0
		Shared.caseLog("CASE2:重复添加，非U型，POSID > 0");
		CommoditySyncCache commoditySyncCache = BaseCommodityTest.DataInput.getCommoditySyncCache(SyncCache.SYNC_Type_C);
		BaseCommodityTest.createCommoditySyncCacheViaMapper(commoditySyncCache);
		// 重复添加
		BaseCommodityTest.createCommoditySyncCacheViaMapper(commoditySyncCache);

		// 删除所有数据，避免对测试造成干扰
		BaseCommodityTest.deleteAllCommoditySyncCache();
	}

	@Test
	public void createTest3() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		// CASE3:正常添加，非U型，POSID <= 0
		Shared.caseLog("CASE3:正常添加，非U型，POSID <= 0");
		CommoditySyncCache commoditySyncCache = BaseCommodityTest.DataInput.getCommoditySyncCache(SyncCache.SYNC_Type_C);
		commoditySyncCache.setPosID(BaseAction.INVALID_POS_ID);
		BaseCommodityTest.createCommoditySyncCacheViaMapper(commoditySyncCache);

		// 删除所有数据，避免对测试造成干扰
		BaseCommodityTest.deleteAllCommoditySyncCache();
	}

	@Test
	public void createTest4() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		// CASE4:重复添加，非U型，POSID <= 0
		Shared.caseLog("CASE4:重复添加，非U型，POSID <= 0");
		CommoditySyncCache commoditySyncCache = BaseCommodityTest.DataInput.getCommoditySyncCache(SyncCache.SYNC_Type_C);
		commoditySyncCache.setPosID(0);
		BaseCommodityTest.createCommoditySyncCacheViaMapper(commoditySyncCache);
		// 重复添加
		BaseCommodityTest.createCommoditySyncCacheViaMapper(commoditySyncCache);

		// 删除所有数据，避免对测试造成干扰
		BaseCommodityTest.deleteAllCommoditySyncCache();
	}

	@Test
	public void createTest5() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		// CASE5:正常添加，U型，POSID > 0
		Shared.caseLog("CASE5:正常添加，U型，POSID > 0");
		CommoditySyncCache commoditySyncCache = BaseCommodityTest.DataInput.getCommoditySyncCache(SyncCache.SYNC_Type_U);
		commoditySyncCache.setPosID(POS_ID2);
		BaseCommodityTest.createCommoditySyncCacheViaMapper(commoditySyncCache);

		// 删除所有数据，避免对测试造成干扰
		BaseCommodityTest.deleteAllCommoditySyncCache();
	}

	@Test
	public void createTest6() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		// CASE6:重复添加，U型，POSID > 0
		Shared.caseLog("CASE6:重复添加，U型，POSID > 0");
		CommoditySyncCache commoditySyncCache = BaseCommodityTest.DataInput.getCommoditySyncCache(SyncCache.SYNC_Type_U);
		commoditySyncCache.setPosID(POS_ID2);
		BaseCommodityTest.createCommoditySyncCacheViaMapper(commoditySyncCache);
		// 重复添加
		BaseCommodityTest.createCommoditySyncCacheViaMapper(commoditySyncCache);

		// 删除所有数据，避免对测试造成干扰
		BaseCommodityTest.deleteAllCommoditySyncCache();
	}

	@Test
	public void createTest7() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		// CASE7:正常添加，U型，POSID <= 0
		Shared.caseLog("CASE7:正常添加，U型，POSID <= 0");
		CommoditySyncCache commoditySyncCache = BaseCommodityTest.DataInput.getCommoditySyncCache(SyncCache.SYNC_Type_U);
		commoditySyncCache.setPosID(BaseAction.INVALID_POS_ID);
		commoditySyncCache.setSyncData_ID(7);
		BaseCommodityTest.createCommoditySyncCacheViaMapper(commoditySyncCache);

		// 删除所有数据，避免对测试造成干扰
		BaseCommodityTest.deleteAllCommoditySyncCache();
	}

	@Test
	public void createTest8() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		// CASE8:重复添加，U型，POSID <= 0
		Shared.caseLog("CASE8:重复添加，U型，POSID <= 0");
		CommoditySyncCache commoditySyncCache = BaseCommodityTest.DataInput.getCommoditySyncCache(SyncCache.SYNC_Type_U);
		commoditySyncCache.setPosID(BaseAction.INVALID_POS_ID);
		commoditySyncCache.setSyncData_ID(7);
		BaseCommodityTest.createCommoditySyncCacheViaMapper(commoditySyncCache);
		// 重复添加
		BaseCommodityTest.createCommoditySyncCacheViaMapper(commoditySyncCache);

		// 删除所有数据，避免对测试造成干扰
		BaseCommodityTest.deleteAllCommoditySyncCache();
	}

	@Test
	public void createTest9() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		// CASE9:正常添加，D型，POSID > 0;
		Shared.caseLog("CASE9:正常添加，D型，POSID > 0");
		CommoditySyncCache commoditySyncCache = BaseCommodityTest.DataInput.getCommoditySyncCache(SyncCache.SYNC_Type_D);
		commoditySyncCache.setPosID(POS_ID3);
		BaseCommodityTest.createCommoditySyncCacheViaMapper(commoditySyncCache);

		// 删除所有数据，避免对测试造成干扰
		BaseCommodityTest.deleteAllCommoditySyncCache();
	}

	@Test
	public void createTest10() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		// CASE10:重复添加，D型，POSID > 0;
		Shared.caseLog("CASE10:重复添加，D型，POSID > 0");
		CommoditySyncCache commoditySyncCache = BaseCommodityTest.DataInput.getCommoditySyncCache(SyncCache.SYNC_Type_D);
		commoditySyncCache.setPosID(POS_ID3);
		BaseCommodityTest.createCommoditySyncCacheViaMapper(commoditySyncCache);
		// 重复添加
		BaseCommodityTest.createCommoditySyncCacheViaMapper(commoditySyncCache);

		// 删除所有数据，避免对测试造成干扰
		BaseCommodityTest.deleteAllCommoditySyncCache();
	}

	@Test
	public void createTest11() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		// CASE11:正常添加，D型， POSID <= 0;
		Shared.caseLog("CASE11:正常添加，D型， POSID <= 0");
		CommoditySyncCache commoditySyncCache = BaseCommodityTest.DataInput.getCommoditySyncCache(SyncCache.SYNC_Type_D);
		commoditySyncCache.setPosID(BaseAction.INVALID_POS_ID);
		BaseCommodityTest.createCommoditySyncCacheViaMapper(commoditySyncCache);

		// 删除所有数据，避免对测试造成干扰
		BaseCommodityTest.deleteAllCommoditySyncCache();
	}

	@Test
	public void createTest12() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		// CASE12:重复添加，D型， POSID <= 0;
		Shared.caseLog("CASE12:重复添加，D型， POSID <= 0");
		CommoditySyncCache commoditySyncCache = BaseCommodityTest.DataInput.getCommoditySyncCache(SyncCache.SYNC_Type_D);
		commoditySyncCache.setPosID(BaseAction.INVALID_POS_ID);
		BaseCommodityTest.createCommoditySyncCacheViaMapper(commoditySyncCache);
		// 重复添加
		BaseCommodityTest.createCommoditySyncCacheViaMapper(commoditySyncCache);

		// 删除所有数据，避免对测试造成干扰
		BaseCommodityTest.deleteAllCommoditySyncCache();
	}

	@Test
	public void retrieveNTest() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		// 创建测试数据
		CommoditySyncCache cc = BaseCommodityTest.DataInput.getCommoditySyncCache(SyncCache.SYNC_Type_C);
		BaseCommodityTest.createCommoditySyncCacheViaMapper(cc);

		Shared.caseLog("Case1：查询商品所有的同步缓存");
		Map<String, Object> paramsForRetrieveN = cc.getRetrieveNParam(BaseBO.INVALID_CASE_ID, cc);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> scList = (List<BaseModel>) commoditySyncCacheMapper.retrieveN(paramsForRetrieveN); // ...
		//
		Assert.assertTrue(scList.size() > 0 && EnumErrorCode.values()[Integer.parseInt(paramsForRetrieveN.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError,
				paramsForRetrieveN.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		//
		System.out.println("查询成功：" + scList);

		// 删除所有数据，避免对测试造成干扰
		BaseCommodityTest.deleteAllCommoditySyncCache();
	}

	@Test
	public void deleteTest1() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		// 创建测试数据
		CommoditySyncCache cc = BaseCommodityTest.DataInput.getCommoditySyncCache(SyncCache.SYNC_Type_C);
		List<List<BaseModel>> list = BaseCommodityTest.createCommoditySyncCacheViaMapper(cc);

		// 查询所有可用的pos机
		Pos posStatus = new Pos();
		posStatus.setShopID(BaseAction.INVALID_ID);
		posStatus.setStatus(0);
		//
		Map<String, Object> paramsStatus = posStatus.getRetrieveNParam(BaseBO.INVALID_CASE_ID, posStatus);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> posList = posMapper.retrieveN(paramsStatus);
		Assert.assertTrue(posList.size() > 0 && EnumErrorCode.values()[Integer.parseInt(paramsStatus.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError,
				paramsStatus.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());

		Shared.caseLog("Case1:POS机全部同步,删除成功");
		CommoditySyncCacheDispatcher CacheDispatcher = new CommoditySyncCacheDispatcher();
		int Cid = list.get(0).get(0).getID();
		for (int i = 0; i < posList.size(); i++) {
			CacheDispatcher.setSyncCacheID(Cid);
			CacheDispatcher.setPos_ID(posList.get(i).getID());
			Map<String, Object> dMap = CacheDispatcher.getCreateParam(BaseBO.INVALID_CASE_ID, CacheDispatcher);
			DataSourceContextHolder.setDbName(Shared.DBName_Test);
			CommoditySyncCacheDispatcher dispatcherCreate = (CommoditySyncCacheDispatcher) commoditySyncCacheDispatcherMapper.create(dMap);
			dispatcherCreate.setIgnoreIDInComparision(true);
			if (dispatcherCreate.compareTo(CacheDispatcher) != 0) {
				Assert.assertTrue(false, "创建对象的字段与DB读出来的字段不相等");
			}
			Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(dMap.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, dMap.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		}
		//
		List<BaseModel> list2 = list.get(0);
		CommoditySyncCache cache = (CommoditySyncCache) list2.get(0);
		//
		Map<String, Object> paramsForDelete = cache.getDeleteParam(BaseBO.INVALID_CASE_ID, cache);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		commoditySyncCacheMapper.delete(paramsForDelete);
		//
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(paramsForDelete.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, paramsForDelete.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());

		// 删除所有数据，避免对测试造成干扰
		BaseCommodityTest.deleteAllCommoditySyncCache();
	}

	@Test
	public void deleteTest2() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		// 创建测试数据
		CommoditySyncCache cc = BaseCommodityTest.DataInput.getCommoditySyncCache(SyncCache.SYNC_Type_C);
		List<List<BaseModel>> list = BaseCommodityTest.createCommoditySyncCacheViaMapper(cc);

		Shared.caseLog("Case2:POS机还没全部同步,无法删除");
		List<BaseModel> scList = (List<BaseModel>) list.get(0);
		CommoditySyncCache scCreate = (CommoditySyncCache) scList.get(0);
		Map<String, Object> paramsForDelete = scCreate.getDeleteParam(BaseBO.INVALID_CASE_ID, scCreate);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		commoditySyncCacheMapper.delete(paramsForDelete);
		//
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(paramsForDelete.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined,
				paramsForDelete.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());

		// 删除所有数据，避免对测试造成干扰
		BaseCommodityTest.deleteAllCommoditySyncCache();
	}

}
