package com.bx.erp.test;

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
import com.bx.erp.model.BarcodesSyncCache;
import com.bx.erp.model.BarcodesSyncCacheDispatcher;
import com.bx.erp.test.Shared;
import com.bx.erp.util.DataSourceContextHolder;

public class BarcodesSyncCacheDispatherMapperTest extends BaseMapperTest {

	public static final int POS_ID1 = 1;

	@BeforeClass
	public void setup() {
		super.setUp();
	}

	@AfterClass
	public void tearDown() {
		super.tearDown();
	}

	@Test
	public void createTest() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog(" Create BarcodesSyncCacheDispatcher Test ");

		BarcodesSyncCache barcodesSyncCache = BaseBarcodesTest.DataInput.getBarcodesSyncCache(new Random().nextInt(5) + 1, SyncCache.SYNC_Type_C, POS_ID1);
		barcodesSyncCache = BaseBarcodesTest.createBarcodesSyncCacheViaMapper(barcodesSyncCache);

		// 正常添加
		BarcodesSyncCacheDispatcher barcodesSyncCacheDispatcher = BaseBarcodesTest.DataInput.getBarcodesSyncCacheDispatcher();
		barcodesSyncCacheDispatcher.setSyncCacheID(barcodesSyncCache.getID());
		BaseBarcodesTest.createBarcodesSyncCacheDispatcherViaMapper(barcodesSyncCacheDispatcher);

		// 重复添加
		Map<String, Object> paramsForCreate2 = barcodesSyncCacheDispatcher.getCreateParam(BaseBO.INVALID_CASE_ID, barcodesSyncCacheDispatcher);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		barcodesSyncCacheDispatcherMapper.create(paramsForCreate2); // ...
		Assert.assertEquals(EnumErrorCode.values()[Integer.parseInt(paramsForCreate2.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())], EnumErrorCode.EC_NoError);
	}

	@Test
	public void retrieveNTest() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Retrieve1 BarcodesSyncCacheDispatcher Test");

		BarcodesSyncCache barcodesSyncCache = BaseBarcodesTest.DataInput.getBarcodesSyncCache(new Random().nextInt(5) + 1, SyncCache.SYNC_Type_C, POS_ID1);
		barcodesSyncCache = BaseBarcodesTest.createBarcodesSyncCacheViaMapper(barcodesSyncCache);

		BarcodesSyncCacheDispatcher barcodesSyncCacheDispatcher = BaseBarcodesTest.DataInput.getBarcodesSyncCacheDispatcher();
		barcodesSyncCacheDispatcher.setSyncCacheID(barcodesSyncCache.getID());
		BarcodesSyncCacheDispatcher barcodesSyncCacheDispatcherCreate = BaseBarcodesTest.createBarcodesSyncCacheDispatcherViaMapper(barcodesSyncCacheDispatcher);

		List<BaseModel> barcodesSyncCacheDispatcherList = BaseBarcodesTest.retrieveNBarcodesSyncCacheDispatcher(barcodesSyncCacheDispatcher);
		boolean exist = false;
		for (BaseModel baseModel : barcodesSyncCacheDispatcherList) {
			if (baseModel.compareTo(barcodesSyncCacheDispatcherCreate) == 0) {
				exist = true;
				break;
			}
		}
		Assert.assertTrue(exist, "未查询到新创建的对象");
	}
}
