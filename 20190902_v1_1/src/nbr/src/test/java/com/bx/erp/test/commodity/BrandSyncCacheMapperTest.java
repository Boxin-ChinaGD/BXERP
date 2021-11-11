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
import com.bx.erp.model.commodity.BrandSyncCache;
import com.bx.erp.model.commodity.BrandSyncCacheDispatcher;
import com.bx.erp.test.BaseMapperTest;
import com.bx.erp.test.Shared;
import com.bx.erp.util.DataSourceContextHolder;

public class BrandSyncCacheMapperTest extends BaseMapperTest {

	public static final int POS_ID1 = 1;

	public static final int POS_ID2 = 2;

	public static final int POS_ID3 = 3;

	public static class DataInput {
		private static BrandSyncCache cc = new BrandSyncCache();

		protected static final BrandSyncCache getBrandSyncCache1() throws CloneNotSupportedException, InterruptedException {
			cc = new BrandSyncCache();
			cc.setSyncData_ID(3);
			cc.setSyncSequence(1);// 本字段在这里无关紧要
			cc.setSyncType(SyncCache.SYNC_Type_C);
			cc.setPosID(POS_ID1);// iPOSID

			return (BrandSyncCache) cc.clone();
		}

		protected static final BrandSyncCache getBrandSyncCache2() throws CloneNotSupportedException, InterruptedException {
			cc = new BrandSyncCache();
			cc.setSyncData_ID(4);
			cc.setSyncSequence(1);// 本字段在这里无关紧要
			cc.setSyncType(SyncCache.SYNC_Type_U);
			cc.setPosID(POS_ID2);// iPOSID

			return (BrandSyncCache) cc.clone();
		}

		protected static final BrandSyncCache getBrandSyncCache3() throws CloneNotSupportedException, InterruptedException {
			cc = new BrandSyncCache();
			cc.setSyncData_ID(5);
			cc.setSyncSequence(1);
			cc.setSyncType(SyncCache.SYNC_Type_D);
			cc.setPosID(POS_ID3);

			return (BrandSyncCache) cc.clone();
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
		BrandSyncCache brandSyncCache = DataInput.getBrandSyncCache1();
		brandSyncCacheCreate(brandSyncCache);

		// 删除所有数据，避免对测试造成干扰
		brandSyncCacheDeleteAll();

		// // 结果验证
		// DataSourceContextHolder.setDbName(Shared.DBName_Test);
		// List<BaseModel> listOut2 = brandSyncCacheMapper.retrieveN(param);
		// Assert.assertEquals(listOut2.size(), 2);
		//
		// DataSourceContextHolder.setDbName(Shared.DBName_Test);
		// List<BaseModel> dlistOut2 = brandSyncCacheDispatcherMapper.retrieveN(param);
		// Assert.assertEquals(dlistOut2.size(), 2);
	}

	@Test
	public void createTest2() throws Exception {
		Shared.printTestMethodStartInfo();

		// CASE2:重复添加，非U型，POSID > 0
		Shared.caseLog("CASE2:重复添加，非U型，POSID > 0");
		BrandSyncCache brandSyncCache = DataInput.getBrandSyncCache1();
		brandSyncCacheCreate(brandSyncCache);
		// 重复添加
		brandSyncCacheCreate(brandSyncCache);

		// 删除所有数据，避免对测试造成干扰
		brandSyncCacheDeleteAll();
	}

	@Test
	public void createTest3() throws Exception {
		Shared.printTestMethodStartInfo();

		// CASE3:正常添加，非U型，POSID <= 0
		Shared.caseLog("CASE3:正常添加，非U型，POSID <= 0");
		BrandSyncCache brandSyncCache = DataInput.getBrandSyncCache1();
		brandSyncCache.setPosID(BaseAction.INVALID_POS_ID);
		brandSyncCacheCreate(brandSyncCache);

		// 删除所有数据，避免对测试造成干扰
		brandSyncCacheDeleteAll();
	}

	@Test
	public void createTest4() throws Exception {
		Shared.printTestMethodStartInfo();

		// CASE4:重复添加，非U型，POSID <= 0
		Shared.caseLog("CASE4:重复添加，非U型，POSID <= 0");
		BrandSyncCache brandSyncCache = DataInput.getBrandSyncCache1();
		brandSyncCache.setPosID(BaseAction.INVALID_POS_ID);
		brandSyncCacheCreate(brandSyncCache);
		// 重复添加
		brandSyncCacheCreate(brandSyncCache);

		// 删除所有数据，避免对测试造成干扰
		brandSyncCacheDeleteAll();
	}

	@Test
	public void createTest5() throws Exception {
		Shared.printTestMethodStartInfo();

		// CASE5:正常添加，U型，POSID > 0
		Shared.caseLog("CASE5:正常添加，U型，POSID > 0");
		BrandSyncCache brandSyncCache = DataInput.getBrandSyncCache2();
		brandSyncCacheCreate(brandSyncCache);

		// 删除所有数据，避免对测试造成干扰
		brandSyncCacheDeleteAll();
	}

	@Test
	public void createTest6() throws Exception {
		Shared.printTestMethodStartInfo();

		// CASE6:重复添加，U型，POSID > 0
		Shared.caseLog("CASE6:重复添加，U型，POSID > 0");
		BrandSyncCache brandSyncCache = DataInput.getBrandSyncCache2();
		brandSyncCacheCreate(brandSyncCache);
		// 重复添加
		brandSyncCacheCreate(brandSyncCache);

		// 删除所有数据，避免对测试造成干扰
		brandSyncCacheDeleteAll();
	}

	@Test
	public void createTest7() throws Exception {
		Shared.printTestMethodStartInfo();

		// CASE7:正常添加，U型，POSID <= 0
		Shared.caseLog("CASE7:正常添加，U型，POSID <= 0");
		BrandSyncCache brandSyncCache = DataInput.getBrandSyncCache2();
		brandSyncCache.setPosID(BaseAction.INVALID_POS_ID);
		brandSyncCache.setSyncData_ID(2);
		brandSyncCacheCreate(brandSyncCache);

		// 删除所有数据，避免对测试造成干扰
		brandSyncCacheDeleteAll();
	}

	@Test
	public void createTest8() throws Exception {
		Shared.printTestMethodStartInfo();

		// CASE8:重复添加，U型，POSID <= 0
		Shared.caseLog("CASE8:重复添加，U型，POSID <= 0");
		BrandSyncCache brandSyncCache = DataInput.getBrandSyncCache2();
		brandSyncCache.setPosID(BaseAction.INVALID_POS_ID);
		brandSyncCache.setSyncData_ID(2);
		brandSyncCacheCreate(brandSyncCache);
		// 重复添加
		brandSyncCacheCreate(brandSyncCache);

		// 删除所有数据，避免对测试造成干扰
		brandSyncCacheDeleteAll();
	}

	@Test
	public void createTest9() throws Exception {
		Shared.printTestMethodStartInfo();

		// CASE9:正常添加，D型，POSID > 0;
		Shared.caseLog("CASE9:正常添加，D型，POSID > 0");
		BrandSyncCache brandSyncCache = DataInput.getBrandSyncCache3();
		brandSyncCacheCreate(brandSyncCache);

		// 删除所有数据，避免对测试造成干扰
		brandSyncCacheDeleteAll();
	}

	@Test
	public void createTest10() throws Exception {
		Shared.printTestMethodStartInfo();

		// CASE10:重复添加，D型，POSID > 0;
		Shared.caseLog("CASE10:重复添加，D型，POSID > 0");
		BrandSyncCache brandSyncCache = DataInput.getBrandSyncCache3();
		brandSyncCacheCreate(brandSyncCache);
		// 重复添加
		brandSyncCacheCreate(brandSyncCache);

		// 删除所有数据，避免对测试造成干扰
		brandSyncCacheDeleteAll();
	}

	@Test
	public void createTest11() throws Exception {
		Shared.printTestMethodStartInfo();

		// CASE11:正常添加，D型， POSID <= 0;
		Shared.caseLog("CASE11:正常添加，D型， POSID <= 0");
		BrandSyncCache brandSyncCache = DataInput.getBrandSyncCache3();
		brandSyncCache.setPosID(BaseAction.INVALID_POS_ID);
		brandSyncCacheCreate(brandSyncCache);

		// 删除所有数据，避免对测试造成干扰
		brandSyncCacheDeleteAll();
	}

	@Test
	public void createTest12() throws Exception {
		Shared.printTestMethodStartInfo();

		// CASE12:重复添加，D型，POSID <= 0
		Shared.caseLog("CASE12:重复添加，D型，POSID <= 0");
		BrandSyncCache brandSyncCache = DataInput.getBrandSyncCache3();
		brandSyncCache.setPosID(BaseAction.INVALID_POS_ID);
		brandSyncCacheCreate(brandSyncCache);
		// 重复添加
		brandSyncCacheCreate(brandSyncCache);

		// 删除所有数据，避免对测试造成干扰
		brandSyncCacheDeleteAll();
	}

	@Test
	public void retrieveNTest() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		// 创建测试数据
		BrandSyncCache brandSyncCache = DataInput.getBrandSyncCache1();
		brandSyncCacheCreate(brandSyncCache);

		Shared.caseLog("CASE1:查询所有的品牌同步缓存");
		BrandSyncCache sc = new BrandSyncCache();
		//
		Map<String, Object> paramsForRetrieveN = sc.getRetrieveNParam(BaseBO.INVALID_CASE_ID, sc);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> scList = (List<BaseModel>) brandSyncCacheMapper.retrieveN(paramsForRetrieveN); // ...
		//
		System.out.println("读取的有" + scList.toString());
		//
		Assert.assertTrue(scList.size() > 0 && EnumErrorCode.values()[Integer.parseInt(paramsForRetrieveN.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError,
				paramsForRetrieveN.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());

		// 删除所有数据，避免对测试造成干扰
		brandSyncCacheDeleteAll();
	}

	@Test
	public void deleteTest1() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		// 创建测试数据
		BrandSyncCache sc = DataInput.getBrandSyncCache1();
		List<List<BaseModel>> createEx = brandSyncCacheCreate(sc);

		// 查询所有可用的pos机
		Pos posStatus = new Pos();
		posStatus.setStatus(Pos.EnumStatusPos.ESP_Active.getIndex());
		posStatus.setShopID(BaseAction.INVALID_ID);
		//
		Map<String, Object> paramsStatus = posStatus.getRetrieveNParam(BaseBO.INVALID_CASE_ID, posStatus);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> posList = posMapper.retrieveN(paramsStatus);
		Assert.assertTrue(posList.size() > 0 && EnumErrorCode.values()[Integer.parseInt(paramsStatus.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError,
				paramsStatus.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());

		// POS机已经全部同步,删除主表和从表，返回错误码0
		Shared.caseLog("CASE1:POS机已经全部同步,删除主表和从表,错误码0");
		BrandSyncCacheDispatcher dispatcher = new BrandSyncCacheDispatcher();
		int brandID = createEx.get(0).get(0).getID();
		for (int i = 0; i < posList.size(); i++) {
			dispatcher.setSyncCacheID(brandID);
			dispatcher.setPos_ID(posList.get(i).getID());
			Map<String, Object> paramsForCreate2 = dispatcher.getCreateParam(BaseBO.INVALID_CASE_ID, dispatcher);

			DataSourceContextHolder.setDbName(Shared.DBName_Test);
			BrandSyncCacheDispatcher scdCreate = (BrandSyncCacheDispatcher) brandSyncCacheDispatcherMapper.create(paramsForCreate2);
			dispatcher.setIgnoreIDInComparision(true);
			if (dispatcher.compareTo(scdCreate) != 0) {
				Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
			}
			Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(paramsForCreate2.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, paramsForCreate2.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		}
		//
		List<BaseModel> list2 = createEx.get(0);
		BrandSyncCache ssc2 = (BrandSyncCache) list2.get(0);
		//
		Map<String, Object> paramsForDelete2 = ssc2.getDeleteParam(BaseBO.INVALID_CASE_ID, ssc2);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		brandSyncCacheMapper.delete(paramsForDelete2);
		//
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(paramsForDelete2.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError,
				paramsForDelete2.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString() + "\r\n ssc2=" + ssc2);

		// 删除所有数据，避免对测试造成干扰
		brandSyncCacheDeleteAll();
	}

	@Test
	public void deleteTest2() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		// 创建测试数据
		BrandSyncCache sc = DataInput.getBrandSyncCache1();
		List<List<BaseModel>> createEx = brandSyncCacheCreate(sc);

		// POS机还没全部同步,无法删除返回错误码7
		Shared.caseLog("CASE2:给所有的从表添加这个数据，才能删除 错误码：7");
		List<BaseModel> list = createEx.get(0);
		BrandSyncCache ssc = (BrandSyncCache) list.get(0);
		//
		Map<String, Object> paramsForDelete = ssc.getDeleteParam(BaseBO.INVALID_CASE_ID, ssc);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		brandSyncCacheMapper.delete(paramsForDelete);
		//
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(paramsForDelete.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined,
				paramsForDelete.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());

		// 删除所有数据，避免对测试造成干扰
		brandSyncCacheDeleteAll();
	}

	/** @return
	 * @throws CloneNotSupportedException
	 * @throws InterruptedException
	 *             创建并结果验证同步缓存数据 */
	// ... *SyncCacheMapperTest都有类似的方法
	protected List<List<BaseModel>> brandSyncCacheCreate(BrandSyncCache sc) throws CloneNotSupportedException, InterruptedException {
		Map<String, Object> paramsForCreate = sc.getCreateParamEx(BaseBO.INVALID_CASE_ID, sc);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<List<BaseModel>> createEx = brandSyncCacheMapper.createEx(paramsForCreate); // ...
		//
		Assert.assertTrue(createEx.size() > 0 && EnumErrorCode.values()[Integer.parseInt(paramsForCreate.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError,
				paramsForCreate.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		// 结果验证
		if ((int) paramsForCreate.get(BrandSyncCache.field.getFIELD_NAME_posID()) > 0) {
			assertTrue(createEx.size() == 2);
		} else {
			assertTrue(createEx.size() == 1);
		}
		//
		System.out.println("创建数据成功 ： " + createEx);

		return createEx;
	}

	/** 删除所有主从表数据 */
	protected void brandSyncCacheDeleteAll() {
		Map<String, Object> param = new HashMap<String, Object>();
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		brandSyncCacheMapper.deleteAll(param);
	}

}
