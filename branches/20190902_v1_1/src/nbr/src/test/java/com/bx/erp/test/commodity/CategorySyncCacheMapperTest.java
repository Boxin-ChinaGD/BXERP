package com.bx.erp.test.commodity;

import static org.testng.Assert.assertTrue;

import java.util.HashMap;
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
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.commodity.CategorySyncCache;
import com.bx.erp.model.commodity.CategorySyncCacheDispatcher;
import com.bx.erp.test.BaseMapperTest;
import com.bx.erp.test.Shared;
import com.bx.erp.util.DataSourceContextHolder;

public class CategorySyncCacheMapperTest extends BaseMapperTest {

	public static final int POS_ID1 = 1;

	public static final int POS_ID2 = 2;

	public static final int POS_ID3 = 3;

	public static class DataInput {
		private static CategorySyncCache cc = new CategorySyncCache();

		protected static final CategorySyncCache getCategorySyncCache1() throws CloneNotSupportedException, InterruptedException {
			cc = new CategorySyncCache();
			cc.setSyncData_ID(3);
			cc.setSyncSequence(4);// 本字段在这里无关紧要
			cc.setSyncType(SyncCache.SYNC_Type_C);
			cc.setPosID(POS_ID1);

			return (CategorySyncCache) cc.clone();
		}

		protected static final CategorySyncCache getCategorySyncCache2() throws CloneNotSupportedException, InterruptedException {
			cc = new CategorySyncCache();
			cc.setSyncData_ID(4);
			cc.setSyncSequence(1);// 本字段在这里无关紧要
			cc.setSyncType(SyncCache.SYNC_Type_U);
			cc.setPosID(POS_ID2);

			return (CategorySyncCache) cc.clone();
		}

		protected static final CategorySyncCache getCategorySyncCache3() throws CloneNotSupportedException, InterruptedException {
			cc = new CategorySyncCache();
			cc.setSyncData_ID(5);
			cc.setSyncSequence(1);
			cc.setSyncType(SyncCache.SYNC_Type_D);
			cc.setPosID(POS_ID3);

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
	}

	@Test
	public void createTest1() throws Exception {
		Shared.printTestMethodStartInfo();

		// CASE1:正常添加，非U型，POSID > 0
		Shared.caseLog("CASE1:正常添加，非U型，POSID > 0");
		CategorySyncCache categorySyncCache = DataInput.getCategorySyncCache1();
		categorySyncCacheCreate(categorySyncCache);

		// 测试完成删除所有数据，以免影响其他测试
		categorySyncCacheDeleteAll();
	}

	@Test
	public void createTest2() throws Exception {
		Shared.printTestMethodStartInfo();

		// CASE2:重复添加，非U型，POSID > 0
		Shared.caseLog("CASE2:重复添加，非U型，POSID > 0");
		CategorySyncCache categorySyncCache = DataInput.getCategorySyncCache1();
		categorySyncCacheCreate(categorySyncCache);
		// 重复添加
		categorySyncCacheCreate(categorySyncCache);

		// 测试完成删除所有数据，以免影响其他测试
		categorySyncCacheDeleteAll();
	}

	@Test
	public void createTest3() throws Exception {
		Shared.printTestMethodStartInfo();

		// CASE3:正常添加，非U型，POSID <= 0
		Shared.caseLog("CASE3:正常添加，非U型，POSID <= 0");
		CategorySyncCache categorySyncCache = DataInput.getCategorySyncCache1();
		categorySyncCache.setPosID(BaseAction.INVALID_POS_ID);
		categorySyncCacheCreate(categorySyncCache);

		// 测试完成删除所有数据，以免影响其他测试
		categorySyncCacheDeleteAll();
	}

	@Test
	public void createTest4() throws Exception {
		Shared.printTestMethodStartInfo();

		// CASE4:重复添加，非U型，POSID <= 0
		Shared.caseLog("CASE4:重复添加，非U型，POSID <= 0");
		CategorySyncCache categorySyncCache = DataInput.getCategorySyncCache1();
		categorySyncCache.setPosID(BaseAction.INVALID_POS_ID);
		categorySyncCacheCreate(categorySyncCache);
		// 重复添加
		categorySyncCacheCreate(categorySyncCache);

		// 测试完成删除所有数据，以免影响其他测试
		categorySyncCacheDeleteAll();
	}

	@Test
	public void createTest5() throws Exception {
		Shared.printTestMethodStartInfo();

		// CASE5:正常添加，U型，POSID > 0
		Shared.caseLog("CASE5:正常添加，U型，POSID > 0");
		CategorySyncCache categorySyncCache = DataInput.getCategorySyncCache2();
		categorySyncCacheCreate(categorySyncCache);

		// 测试完成删除所有数据，以免影响其他测试
		categorySyncCacheDeleteAll();
	}

	@Test
	public void createTest6() throws Exception {
		Shared.printTestMethodStartInfo();

		// CASE6:重复添加，U型，POSID > 0
		Shared.caseLog("CASE6:重复添加，U型，POSID > 0");
		CategorySyncCache categorySyncCache = DataInput.getCategorySyncCache2();
		categorySyncCacheCreate(categorySyncCache);
		// 重复添加
		categorySyncCacheCreate(categorySyncCache);

		// 测试完成删除所有数据，以免影响其他测试
		categorySyncCacheDeleteAll();
	}

	@Test
	public void createTest7() throws Exception {
		Shared.printTestMethodStartInfo();

		// CASE7:正常添加，U型，POSID <= 0
		Shared.caseLog("CASE7:正常添加，U型，POSID <= 0");
		CategorySyncCache categorySyncCache = DataInput.getCategorySyncCache2();
		categorySyncCache.setPosID(BaseAction.INVALID_POS_ID);
		categorySyncCache.setSyncData_ID(7);
		categorySyncCacheCreate(categorySyncCache);

		// 测试完成删除所有数据，以免影响其他测试
		categorySyncCacheDeleteAll();
	}

	@Test
	public void createTest8() throws Exception {
		Shared.printTestMethodStartInfo();

		// CASE8:重复添加，U型，POSID <= 0
		Shared.caseLog("CASE8:重复添加，U型，POSID <= 0");
		CategorySyncCache categorySyncCache = DataInput.getCategorySyncCache2();
		categorySyncCache.setPosID(BaseAction.INVALID_POS_ID);
		categorySyncCache.setSyncData_ID(7);
		categorySyncCacheCreate(categorySyncCache);
		// 重复添加
		categorySyncCacheCreate(categorySyncCache);

		// 测试完成删除所有数据，以免影响其他测试
		categorySyncCacheDeleteAll();
	}

	@Test
	public void createTest9() throws Exception {
		Shared.printTestMethodStartInfo();

		// CASE9:正常添加，D型，POSID > 0;
		Shared.caseLog("CASE9:正常添加，D型，POSID > 0");
		CategorySyncCache categorySyncCache = DataInput.getCategorySyncCache3();
		categorySyncCacheCreate(categorySyncCache);

		// 测试完成删除所有数据，以免影响其他测试
		categorySyncCacheDeleteAll();
	}

	@Test
	public void createTest10() throws Exception {
		Shared.printTestMethodStartInfo();

		// CASE10:重复添加，D型，POSID > 0;
		Shared.caseLog("CASE10:重复添加，D型，POSID > 0");
		CategorySyncCache categorySyncCache = DataInput.getCategorySyncCache3();
		categorySyncCacheCreate(categorySyncCache);
		// 重复添加
		categorySyncCacheCreate(categorySyncCache);

		// 测试完成删除所有数据，以免影响其他测试
		categorySyncCacheDeleteAll();
	}

	@Test
	public void createTest11() throws Exception {
		Shared.printTestMethodStartInfo();

		// CASE11:正常添加，D型， POSID <= 0;
		Shared.caseLog("CASE11:正常添加，D型， POSID <= 0");
		CategorySyncCache categorySyncCache = DataInput.getCategorySyncCache3();
		categorySyncCache.setPosID(BaseAction.INVALID_POS_ID);
		categorySyncCacheCreate(categorySyncCache);

		// 测试完成删除所有数据，以免影响其他测试
		categorySyncCacheDeleteAll();
	}

	@Test
	public void createTest12() throws Exception {
		Shared.printTestMethodStartInfo();

		// CASE12:重复添加，D型， POSID <= 0;
		Shared.caseLog("CASE12:重复添加，D型， POSID <= 0");
		CategorySyncCache categorySyncCache = DataInput.getCategorySyncCache3();
		categorySyncCache.setPosID(BaseAction.INVALID_POS_ID);
		categorySyncCacheCreate(categorySyncCache);
		// 重复添加
		categorySyncCacheCreate(categorySyncCache);

		// 测试完成删除所有数据，以免影响其他测试
		categorySyncCacheDeleteAll();
	}

	@Test
	public void retrieveNTest() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		// 创建测试数据
		CategorySyncCache sc = DataInput.getCategorySyncCache1();
		categorySyncCacheCreate(sc);

		Shared.caseLog("CASE1:查询所有商品小类同步缓存");
		Map<String, Object> paramsForRetrieveN = sc.getRetrieveNParam(BaseBO.INVALID_CASE_ID, sc);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> scList = (List<BaseModel>) categorySyncCacheMapper.retrieveN(paramsForRetrieveN); // ...
		//
		System.out.println("查询结果：" + scList);
		//
		Assert.assertTrue(scList.size() > 0 && EnumErrorCode.values()[Integer.parseInt(paramsForRetrieveN.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError,
				paramsForRetrieveN.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());

		// 测试完成删除所有数据，以免影响其他测试
		categorySyncCacheDeleteAll();
	}

	@Test
	public void deleteTest1() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		// 创建测试数据
		CategorySyncCache sc = DataInput.getCategorySyncCache1();
		List<List<BaseModel>> createEx = categorySyncCacheCreate(sc);

		// 查询所有可用的pos机
		Pos posStatus = new Pos();
		posStatus.setStatus(0);
		posStatus.setShopID(-1);
		//
		Map<String, Object> paramsStatus = posStatus.getRetrieveNParam(BaseBO.INVALID_CASE_ID, posStatus);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> posList = posMapper.retrieveN(paramsStatus);
		Assert.assertTrue(posList.size() > 0 && EnumErrorCode.values()[Integer.parseInt(paramsStatus.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError,
				paramsStatus.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());

		Shared.caseLog("CASE1:POS机已经全部同步,删除主表和从表,错误码0");
		CategorySyncCacheDispatcher dispatcher = new CategorySyncCacheDispatcher();
		int Cid = createEx.get(0).get(0).getID();
		for (int i = 0; i < posList.size(); i++) {
			dispatcher.setSyncCacheID(Cid);
			dispatcher.setPos_ID(posList.get(i).getID());
			Map<String, Object> params = dispatcher.getCreateParam(BaseBO.INVALID_CASE_ID, dispatcher);
			DataSourceContextHolder.setDbName(Shared.DBName_Test);
			CategorySyncCacheDispatcher dispatcherCreate = (CategorySyncCacheDispatcher) categorySyncCacheDispatcherMapper.create(params);
			dispatcher.setIgnoreIDInComparision(true);
			if (dispatcher.compareTo(dispatcherCreate) != 0) {
				Assert.assertTrue(false, "创建对象的字段与DB读出的字段不相等！");
			}
			Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		}

		List<BaseModel> list2 = createEx.get(0);
		CategorySyncCache categorySyncCache = (CategorySyncCache) list2.get(0);

		Map<String, Object> paramsDelete2 = categorySyncCache.getDeleteParam(BaseBO.INVALID_CASE_ID, categorySyncCache);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		categorySyncCacheMapper.delete(paramsDelete2);
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(paramsDelete2.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, paramsDelete2.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());

		// 测试完成删除所有数据，以免影响其他测试
		categorySyncCacheDeleteAll();
	}

	@Test
	public void deleteTest2() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		// 创建测试数据
		CategorySyncCache sc = DataInput.getCategorySyncCache1();
		List<List<BaseModel>> createEx = categorySyncCacheCreate(sc);

		// 给所有的从表添加这个数据，才能删除
		Shared.caseLog("CASE2:给所有的从表添加这个数据，才能删除 错误码：7");
		List<BaseModel> list = createEx.get(0);
		CategorySyncCache ssc = (CategorySyncCache) list.get(0);
		//
		Map<String, Object> paramsForDelete = sc.getDeleteParam(BaseBO.INVALID_CASE_ID, ssc);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		categorySyncCacheMapper.delete(paramsForDelete);
		//
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(paramsForDelete.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined,
				paramsForDelete.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());

		// 测试完成删除所有数据，以免影响其他测试
		categorySyncCacheDeleteAll();
	}

	/** @param categorySyncCache
	 * @return
	 * 
	 * 		创建并结果验证商品小类同步缓存数据 */
	protected List<List<BaseModel>> categorySyncCacheCreate(CategorySyncCache categorySyncCache) {
		Map<String, Object> paramsForCreate = categorySyncCache.getCreateParamEx(BaseBO.INVALID_CASE_ID, categorySyncCache);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<List<BaseModel>> createExList = categorySyncCacheMapper.createEx(paramsForCreate); // ...
		//
		Assert.assertTrue(createExList.size() > 0 && EnumErrorCode.values()[Integer.parseInt(paramsForCreate.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError,
				paramsForCreate.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		// 结果验证
		if ((int) paramsForCreate.get(CategorySyncCache.field.getFIELD_NAME_posID()) > 0) {
			assertTrue(createExList.size() == 2);
		} else {
			assertTrue(createExList.size() == 1);
		}
		//
		System.out.println("创建数据成功 ： " + createExList);

		return createExList;
	}

	/** 删除全部主从表数据 */
	protected void categorySyncCacheDeleteAll() {
		Map<String, Object> param = new HashMap<String, Object>();
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		categorySyncCacheMapper.deleteAll(param);
	}
}
