package com.bx.erp.test;

import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.annotation.Resource;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.action.bo.PosBO;
import com.bx.erp.cache.SyncCache;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.Pos;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.util.DataSourceContextHolder;
import com.bx.erp.model.BarcodesSyncCache;
import com.bx.erp.model.BarcodesSyncCacheDispatcher;

public class BarcodesSyncCacheMapperTest extends BaseMapperTest {
	@Resource
	private PosBO posBO;

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
	public void createTest1() throws Exception {
		Shared.printTestMethodStartInfo();

		// 删除所有的数据，保证测试正常
		BaseBarcodesTest.deleteAllBarcodesSyncCacheViaMappe();

		Shared.caseLog("CASE1:正常添加，非U型，POSID > 0");
		//
		BarcodesSyncCache barcodesSyncCahce = BaseBarcodesTest.DataInput.getBarcodesSyncCache(new Random().nextInt(5) + 1, SyncCache.SYNC_Type_C, POS_ID1);
		BaseBarcodesTest.createBarcodesSyncCacheViaMapper(barcodesSyncCahce);
		// 结果验证
		List<BaseModel> barcodesSyncCahceList = BaseBarcodesTest.retrieveNBarcodesSyncCacheViaMapper();
		Assert.assertEquals(barcodesSyncCahceList.size(), 1);

		List<BaseModel> barcodesSyncCacheDispatherList = BaseBarcodesTest.retrieveNBarcodesSyncCacheDispatherViaMapper();
		Assert.assertEquals(barcodesSyncCacheDispatherList.size(), 1);
	}

	@Test(dependsOnMethods = "createTest1")
	public void createTest2() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE2:重复添加，非U型，POSID > 0");
		//
		BarcodesSyncCache bcc2 = BaseBarcodesTest.DataInput.getBarcodesSyncCache(new Random().nextInt(5) + 1, SyncCache.SYNC_Type_C, POS_ID1);
		BaseBarcodesTest.createBarcodesSyncCacheViaMapper(bcc2);
		// 结果验证
		List<BaseModel> barcodesSyncCahceList = BaseBarcodesTest.retrieveNBarcodesSyncCacheViaMapper();
		;
		Assert.assertEquals(barcodesSyncCahceList.size(), 2);

		List<BaseModel> barcodesSyncCacheDispatherList = BaseBarcodesTest.retrieveNBarcodesSyncCacheDispatherViaMapper();
		Assert.assertEquals(barcodesSyncCacheDispatherList.size(), 2);
	}

	@Test(dependsOnMethods = "createTest2")
	public void createTest3() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE3:正常添加，非U型，POSID <= 0");
		//
		BarcodesSyncCache barcodesSyncCache = BaseBarcodesTest.DataInput.getBarcodesSyncCache(new Random().nextInt(5) + 1, SyncCache.SYNC_Type_C, BaseAction.INVALID_POS_ID);
		BaseBarcodesTest.createBarcodesSyncCacheViaMapper(barcodesSyncCache);

		// 结果验证
		List<BaseModel> barcodesSyncCahceList = BaseBarcodesTest.retrieveNBarcodesSyncCacheViaMapper();
		Assert.assertEquals(barcodesSyncCahceList.size(), 3);

		List<BaseModel> barcodesSyncCacheDispatherList = BaseBarcodesTest.retrieveNBarcodesSyncCacheDispatherViaMapper();
		Assert.assertEquals(barcodesSyncCacheDispatherList.size(), 2);
	}

	@Test(dependsOnMethods = "createTest3")
	public void createTest4() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE4:正常添加，非U型，POSID <= 0");
		//
		BarcodesSyncCache barcodesSyncCache = BaseBarcodesTest.DataInput.getBarcodesSyncCache(new Random().nextInt(5) + 1, SyncCache.SYNC_Type_C, BaseAction.INVALID_POS_ID);
		BaseBarcodesTest.createBarcodesSyncCacheViaMapper(barcodesSyncCache);
		// 结果验证
		List<BaseModel> barcodesSyncCahceList = BaseBarcodesTest.retrieveNBarcodesSyncCacheViaMapper();
		Assert.assertEquals(barcodesSyncCahceList.size(), 4);

		List<BaseModel> barcodesSyncCacheDispatherList = BaseBarcodesTest.retrieveNBarcodesSyncCacheDispatherViaMapper();
		Assert.assertEquals(barcodesSyncCacheDispatherList.size(), 2);
	}

	@Test(dependsOnMethods = "createTest4")
	public void createTest5() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE5:正常添加，U型，POSID > 0");
		//
		BarcodesSyncCache barcodesSyncCahce = BaseBarcodesTest.DataInput.getBarcodesSyncCache(5, SyncCache.SYNC_Type_U, POS_ID2);
		BaseBarcodesTest.createBarcodesSyncCacheViaMapper(barcodesSyncCahce);

		// 结果验证
		List<BaseModel> barcodesSyncCahceList = BaseBarcodesTest.retrieveNBarcodesSyncCacheViaMapper();
		Assert.assertEquals(barcodesSyncCahceList.size(), 5);

		List<BaseModel> barcodesSyncCacheDispatherList = BaseBarcodesTest.retrieveNBarcodesSyncCacheDispatherViaMapper();
		Assert.assertEquals(barcodesSyncCacheDispatherList.size(), 3);
	}

	@Test(dependsOnMethods = "createTest5")
	public void createTest6() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE6:重复添加，U型，POSID > 0");
		//
		BarcodesSyncCache barcodesSyncCahce = BaseBarcodesTest.DataInput.getBarcodesSyncCache(5, SyncCache.SYNC_Type_U, POS_ID2);
		BaseBarcodesTest.createBarcodesSyncCacheViaMapper(barcodesSyncCahce);

		// 结果验证
		List<BaseModel> barcodesSyncCahceList = BaseBarcodesTest.retrieveNBarcodesSyncCacheViaMapper();
		Assert.assertEquals(barcodesSyncCahceList.size(), 5);

		List<BaseModel> barcodesSyncCacheDispatherList = BaseBarcodesTest.retrieveNBarcodesSyncCacheDispatherViaMapper();
		Assert.assertEquals(barcodesSyncCacheDispatherList.size(), 3);
	}

	@Test(dependsOnMethods = "createTest6")
	public void createTest7() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE7:正常添加，U型，POSID <= 0");
		//
		BarcodesSyncCache cc7 = BaseBarcodesTest.DataInput.getBarcodesSyncCache(5, SyncCache.SYNC_Type_U, POS_ID2);
		cc7.setPosID(BaseAction.INVALID_POS_ID);
		cc7.setSyncData_ID(7);
		Map<String, Object> paramsForCreate7 = cc7.getCreateParam(BaseBO.INVALID_CASE_ID, cc7);

		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<List<BaseModel>> list7 = barcodesSyncCacheMapper.createEx(paramsForCreate7); // ...
		if (list7.size() == 1) {
			BarcodesSyncCache scCreate7 = (BarcodesSyncCache) list7.get(0);
			cc7.setIgnoreIDInComparision(true);
			if (cc7.compareTo(scCreate7) != 0) {
				Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
			}
		} else {
			Assert.assertTrue(false);
		}
		Assert.assertEquals(EnumErrorCode.values()[Integer.parseInt(paramsForCreate7.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())], EnumErrorCode.EC_NoError);
		// 结果验证
		List<BaseModel> barcodesSyncCahceList = BaseBarcodesTest.retrieveNBarcodesSyncCacheViaMapper();
		Assert.assertEquals(barcodesSyncCahceList.size(), 6);

		List<BaseModel> barcodesSyncCacheDispatherList = BaseBarcodesTest.retrieveNBarcodesSyncCacheDispatherViaMapper();
		Assert.assertEquals(barcodesSyncCacheDispatherList.size(), 3);
	}

	@Test(dependsOnMethods = "createTest7")
	public void createTest8() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE8:重复添加，U型，POSID <= 0");
		//
		BarcodesSyncCache cc8 = BaseBarcodesTest.DataInput.getBarcodesSyncCache(5, SyncCache.SYNC_Type_U, POS_ID2);
		cc8.setPosID(BaseAction.INVALID_POS_ID);
		cc8.setSyncData_ID(7);
		Map<String, Object> paramsForCreate8 = cc8.getCreateParam(BaseBO.INVALID_CASE_ID, cc8);

		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<List<BaseModel>> list8 = barcodesSyncCacheMapper.createEx(paramsForCreate8); // ...
		if (list8.size() == 1) {
			BarcodesSyncCache scCreate8 = (BarcodesSyncCache) list8.get(0);
			cc8.setIgnoreIDInComparision(true);
			if (cc8.compareTo(scCreate8) != 0) {
				Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
			}
		} else {
			Assert.assertTrue(false);
		}
		Assert.assertEquals(EnumErrorCode.values()[Integer.parseInt(paramsForCreate8.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())], EnumErrorCode.EC_NoError);
		// 结果验证
		List<BaseModel> barcodesSyncCahceList = BaseBarcodesTest.retrieveNBarcodesSyncCacheViaMapper();
		Assert.assertEquals(barcodesSyncCahceList.size(), 6);

		List<BaseModel> barcodesSyncCacheDispatherList = BaseBarcodesTest.retrieveNBarcodesSyncCacheDispatherViaMapper();
		Assert.assertEquals(barcodesSyncCacheDispatherList.size(), 3);
	}

	@Test(dependsOnMethods = "createTest8")
	public void createTest9() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE9:正常添加，D型，POSID > 0;");
		//
		BarcodesSyncCache barcodesSyncCahce = BaseBarcodesTest.DataInput.getBarcodesSyncCache(6, SyncCache.SYNC_Type_D, POS_ID3);
		BaseBarcodesTest.createBarcodesSyncCacheViaMapper(barcodesSyncCahce);

		// 结果验证
		List<BaseModel> barcodesSyncCahceList = BaseBarcodesTest.retrieveNBarcodesSyncCacheViaMapper();
		Assert.assertEquals(barcodesSyncCahceList.size(), 7);

		List<BaseModel> barcodesSyncCacheDispatherList = BaseBarcodesTest.retrieveNBarcodesSyncCacheDispatherViaMapper();
		Assert.assertEquals(barcodesSyncCacheDispatherList.size(), 4);
	}

	@Test(dependsOnMethods = "createTest9")
	public void createTest10() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE10:重复添加，D型，POSID > 0;");
		//
		BarcodesSyncCache barcodesSyncCahce = BaseBarcodesTest.DataInput.getBarcodesSyncCache(6, SyncCache.SYNC_Type_D, POS_ID3);
		BaseBarcodesTest.createBarcodesSyncCacheViaMapper(barcodesSyncCahce);

		// 结果验证
		List<BaseModel> barcodesSyncCahceList = BaseBarcodesTest.retrieveNBarcodesSyncCacheViaMapper();
		Assert.assertEquals(barcodesSyncCahceList.size(), 8);

		List<BaseModel> barcodesSyncCacheDispatherList = BaseBarcodesTest.retrieveNBarcodesSyncCacheDispatherViaMapper();
		Assert.assertEquals(barcodesSyncCacheDispatherList.size(), 5);
	}

	@Test(dependsOnMethods = "createTest10")
	public void createTest11() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE11:正常添加，D型， POSID <= 0;");
		//
		BarcodesSyncCache cc11 = BaseBarcodesTest.DataInput.getBarcodesSyncCache(6, SyncCache.SYNC_Type_D, POS_ID3);
		cc11.setPosID(BaseAction.INVALID_POS_ID);
		Map<String, Object> paramsForCreate11 = cc11.getCreateParam(BaseBO.INVALID_CASE_ID, cc11);

		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<List<BaseModel>> list11 = barcodesSyncCacheMapper.createEx(paramsForCreate11);
		if (list11.size() == 1) {
			BarcodesSyncCache scCreate11 = (BarcodesSyncCache) list11.get(0);
			cc11.setIgnoreIDInComparision(true);
			if (cc11.compareTo(scCreate11) != 0) {
				Assert.assertTrue(false, "创建的对象字段与DB读出的不相等");
			}
		} else {
			Assert.assertTrue(false);
		}
		Assert.assertEquals(EnumErrorCode.values()[Integer.parseInt(paramsForCreate11.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())], EnumErrorCode.EC_NoError);
		// 结果验证
		List<BaseModel> barcodesSyncCahceList = BaseBarcodesTest.retrieveNBarcodesSyncCacheViaMapper();
		Assert.assertEquals(barcodesSyncCahceList.size(), 9);

		List<BaseModel> barcodesSyncCacheDispatherList = BaseBarcodesTest.retrieveNBarcodesSyncCacheDispatherViaMapper();
		Assert.assertEquals(barcodesSyncCacheDispatherList.size(), 5);
	}

	@Test(dependsOnMethods = "createTest11")
	public void createTest12() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE12:重复添加，D型，POSID <= 0");
		//
		BarcodesSyncCache cc12 = BaseBarcodesTest.DataInput.getBarcodesSyncCache(6, SyncCache.SYNC_Type_D, POS_ID3);
		cc12.setPosID(BaseAction.INVALID_POS_ID);
		Map<String, Object> paramsForCreate12 = cc12.getCreateParam(BaseBO.INVALID_CASE_ID, cc12);

		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<List<BaseModel>> list12 = barcodesSyncCacheMapper.createEx(paramsForCreate12);
		if (list12.size() == 1) {
			BarcodesSyncCache scCreate12 = (BarcodesSyncCache) list12.get(0);
			cc12.setIgnoreIDInComparision(true);
			if (cc12.compareTo(scCreate12) != 0) {
				Assert.assertTrue(false, "创建的对象字段与DB读出的不相等");
			}
		} else {
			Assert.assertTrue(false);
		}
		Assert.assertEquals(EnumErrorCode.values()[Integer.parseInt(paramsForCreate12.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())], EnumErrorCode.EC_NoError);
		// 结果验证
		List<BaseModel> barcodesSyncCahceList = BaseBarcodesTest.retrieveNBarcodesSyncCacheViaMapper();
		Assert.assertEquals(barcodesSyncCahceList.size(), 10);

		List<BaseModel> barcodesSyncCacheDispatherList = BaseBarcodesTest.retrieveNBarcodesSyncCacheDispatherViaMapper();
		Assert.assertEquals(barcodesSyncCacheDispatherList.size(), 5);
	}

	@Test(dependsOnMethods = "createTest12")
	public void retrieveNTest() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog(" RetrieveN BarcodesSyncCache Test");

		BaseBarcodesTest.retrieveNBarcodesSyncCacheViaMapper();

		// 测试完删除所有数据
		BaseBarcodesTest.deleteAllBarcodesSyncCacheViaMappe();
	}

	@Test(dependsOnMethods = "retrieveNTest")
	public void deleteTest() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Delete BarcodesSyncCache Test");
		// 测试前删除所有数据，否则影响一下次测试
		BaseBarcodesTest.deleteAllBarcodesSyncCacheViaMappe();

		BarcodesSyncCache sc = BaseBarcodesTest.DataInput.getBarcodesSyncCache(1, SyncCache.SYNC_Type_C, POS_ID1);
		BarcodesSyncCache barcodesSyncCache = BaseBarcodesTest.createBarcodesSyncCacheViaMapper(sc);
		//
		Map<String, Object> paramsForDelete = sc.getDeleteParam(BaseBO.INVALID_CASE_ID, barcodesSyncCache);

		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		barcodesSyncCacheMapper.delete(paramsForDelete);

		Assert.assertEquals(EnumErrorCode.values()[Integer.parseInt(paramsForDelete.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())], EnumErrorCode.EC_BusinessLogicNotDefined);

		// POS机已经全部同步,删除主表和从表，返回错误码0
		List<Pos> listPos = Shared.getPosesFromDB(posBO, Shared.DBName_Test);
		BarcodesSyncCacheDispatcher dispatcher = new BarcodesSyncCacheDispatcher();
		int Bid = barcodesSyncCache.getID();
		for (int i = 2; i <= listPos.size(); i++) {
			dispatcher.setSyncCacheID(Bid);
			dispatcher.setPos_ID(i);
			BaseBarcodesTest.createBarcodesSyncCacheDispatcherViaMapper(dispatcher);
		}

		BaseBarcodesTest.deleteBarcodesSyncCacheViaMapper(barcodesSyncCache);
	}
}
