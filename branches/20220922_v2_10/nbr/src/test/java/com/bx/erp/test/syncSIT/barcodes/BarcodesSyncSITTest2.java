package com.bx.erp.test.syncSIT.barcodes;

import static org.testng.Assert.assertTrue;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.cache.SyncCache;
import com.bx.erp.model.Barcodes;
import com.bx.erp.model.BarcodesSyncCache;
import com.bx.erp.model.BarcodesSyncCacheDispatcher;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.BaseSyncCache;
import com.bx.erp.model.BaseSyncCacheDispatcher;
import com.bx.erp.model.SyncCacheType.EnumSyncCacheType;
import com.bx.erp.model.commodity.Commodity;
import com.bx.erp.test.BaseBarcodesTest;
import com.bx.erp.test.BaseCommodityTest;
import com.bx.erp.test.Shared;
import com.bx.erp.test.syncSIT.BaseSyncSITTest2;
import com.bx.erp.test.syncSIT.BaseSyncSITTestThread2;

@WebAppConfiguration
public class BarcodesSyncSITTest2 extends BaseSyncSITTest2 {
	@Resource
	private com.bx.erp.cache.BarcodesSyncCache cacheBarcodesSyncCache;

	@Override
	protected BaseBO getBO() {
		return barcodesBO;
	}

	@Override
	protected BaseBO getSyncCacheBO() {
		return barcodesSyncCacheBO;
	}

	@Override
	protected BaseBO getSyncCacheDispatcherBO() {
		return barcodesSyncCacheDispatcherBO;
	}

	@Override
	protected BaseSyncCache getSyncCache() {
		return new BarcodesSyncCache();
	}

	@Override
	protected EnumSyncCacheType getSyncCacheType() {
		return EnumSyncCacheType.ESCT_BarcodesSyncInfo;
	}

	@Override
	protected int getDeleteAllSyncCacheCaseID() {
		return BaseBO.CASE_X_DeleteAllBarcodesSyncCache;
	}

	@Override
	protected BaseSyncCacheDispatcher getSyncCacheDispatcher() {
		return new BarcodesSyncCacheDispatcher();
	}

	@Override
	protected BaseSyncCache getObjectSyncCache() {
		return new BarcodesSyncCache();
	}

	@Override
	protected BaseModel getModel() {
		return new Barcodes();
	}

	@Override
	protected SyncCache getCacheObjectSyncCache() {
		return cacheBarcodesSyncCache;
	}

	@Override
	protected BaseSyncSITTestThread2 getThread2(MockMvc mvc, HttpSession session) {
		return new BarcodesSyncThread2(mvc, session);
	}

	@BeforeClass
	public void setup() {
		Shared.printTestClassStartInfo();

		doSetup();
	}

	@AfterClass
	public void tearDown() {
		Shared.printTestClassEndInfo();
	}

	/** 1.调用BO创建两个Barcodes 2.对同步块主从表插入数据，pos2，3，4，5已经更新完成 */
	@Test(timeOut = 60000)
	public void createBarcodesAndSyncPartOfPoses() throws Exception {
		Shared.printTestMethodStartInfo();

		createObjectAndSyncPartOfPoses();
	}

	@Test(dependsOnMethods = { "createBarcodesAndSyncPartOfPoses" }, timeOut = 60000)
	public void syncBarcodesInPos1() throws Exception {
		Shared.printTestMethodStartInfo();

		assertTrue(cacheBarcodesSyncCache.loadForTestNGTest(Shared.DBName_Test));

		syncObjectInPos1();
	}

	protected BaseModel createObject1() throws Exception {
		// 创建两个Barcodes,创建商品的同时也创建了一个Barcodes
		Commodity commodity = BaseCommodityTest.createCommodityViaMapper(BaseCommodityTest.DataInput.getCommodity(), BaseBO.CASE_Commodity_CreateSingle);
		
		Barcodes barcodes = BaseBarcodesTest.DataInput.getBarcodes(commodity.getID());
		Barcodes barcodesCreate = BaseBarcodesTest.createBarcodesViaMapper(barcodes, BaseBO.INVALID_CASE_ID);
		
		return barcodesCreate;
	}
}
