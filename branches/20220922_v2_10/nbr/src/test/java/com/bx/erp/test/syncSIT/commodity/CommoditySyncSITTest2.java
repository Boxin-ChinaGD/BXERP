package com.bx.erp.test.syncSIT.commodity;

import static org.testng.Assert.assertTrue;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.test.web.servlet.MockMvc;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.cache.SyncCache;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.BaseSyncCache;
import com.bx.erp.model.BaseSyncCacheDispatcher;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.SyncCacheType.EnumSyncCacheType;
import com.bx.erp.model.commodity.Commodity;
import com.bx.erp.model.commodity.CommoditySyncCache;
import com.bx.erp.model.commodity.CommoditySyncCacheDispatcher;
import com.bx.erp.test.BaseCommodityTest;
import com.bx.erp.test.Shared;
import com.bx.erp.test.syncSIT.BaseSyncSITTest2;
import com.bx.erp.test.syncSIT.BaseSyncSITTestThread2;
import com.bx.erp.util.DataSourceContextHolder;

public class CommoditySyncSITTest2 extends BaseSyncSITTest2 {

	@Resource
	private com.bx.erp.cache.commodity.CommoditySyncCache cacheCommoditySyncCache;

	@Override
	protected BaseBO getBO() {
		return commodityBO;
	}

	@Override
	protected BaseBO getSyncCacheBO() {
		return commoditySyncCacheBO;
	}

	@Override
	protected BaseBO getSyncCacheDispatcherBO() {
		return commoditySyncCacheDispatcherBO;
	}

	@Override
	protected BaseSyncCache getSyncCache() {
		return new CommoditySyncCache();
	}

	@Override
	protected EnumSyncCacheType getSyncCacheType() {
		return EnumSyncCacheType.ESCT_CommoditySyncInfo;
	}

	@Override
	protected int getDeleteAllSyncCacheCaseID() {
		return BaseBO.CASE_X_DeleteAllCommoditySyncCache;
	}

	@Override
	protected BaseSyncCacheDispatcher getSyncCacheDispatcher() {
		return new CommoditySyncCacheDispatcher();
	}

	@Override
	protected BaseSyncCache getObjectSyncCache() {
		return new CommoditySyncCache();
	}

	@Override
	protected BaseModel getModel() {
		return new Commodity();
	}

	@Override
	protected SyncCache getCacheObjectSyncCache() {
		return cacheCommoditySyncCache;
	}

	@Override
	protected BaseSyncSITTestThread2 getThread2(MockMvc mvc, HttpSession session) {
		return new CommoditySyncThread2(mvc, session);
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

	/** 1.调用BO创建两个Commodity 2.对同步块主从表插入数据，pos2，3，4，5已经更新完成 */
	@Test(timeOut = 60000)
	public void createCommodityAndSyncPartOfPoses() throws Exception {
		Shared.printTestMethodStartInfo();

		createObjectAndSyncPartOfPoses();
	}

	@Test(dependsOnMethods = { "createCommodityAndSyncPartOfPoses" }, timeOut = 60000)
	public void syncCommodityInPos1() throws Exception {
		Shared.printTestMethodStartInfo();

		assertTrue(cacheCommoditySyncCache.loadForTestNGTest(Shared.DBName_Test));

		syncObjectInPos1();
	}

	protected BaseModel createObject1() throws Exception {
		// 创建两个Commodity
		Commodity commodity = BaseCommodityTest.DataInput.getCommodity();

		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<List<BaseModel>> commodityList = (List<List<BaseModel>>) commodityBO.createObjectEx(BaseBO.SYSTEM, BaseBO.CASE_Commodity_CreateSingle, commodity);
		if (commodityBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
			System.out.println("commodity创建失败：" + commodityBO.printErrorInfo());
			return null;
		}

		Commodity commodityCreate = (Commodity) commodityList.get(0).get(0);

		return commodityCreate;
	}
}
