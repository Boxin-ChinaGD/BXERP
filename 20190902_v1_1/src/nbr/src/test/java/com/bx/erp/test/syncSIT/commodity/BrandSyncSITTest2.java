package com.bx.erp.test.syncSIT.commodity;

import static org.testng.Assert.assertTrue;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.cache.SyncCache;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.BaseSyncCache;
import com.bx.erp.model.BaseSyncCacheDispatcher;
import com.bx.erp.model.SyncCacheType.EnumSyncCacheType;
import com.bx.erp.model.commodity.Brand;
import com.bx.erp.model.commodity.BrandSyncCache;
import com.bx.erp.model.commodity.BrandSyncCacheDispatcher;
import com.bx.erp.test.BaseBrandTest;
import com.bx.erp.test.Shared;
import com.bx.erp.test.syncSIT.BaseSyncSITTest2;
import com.bx.erp.test.syncSIT.BaseSyncSITTestThread2;
import com.bx.erp.util.DataSourceContextHolder;

@WebAppConfiguration
public class BrandSyncSITTest2 extends BaseSyncSITTest2 {
	@Resource
	private com.bx.erp.cache.commodity.BrandSyncCache cacheBrandSyncCache;

	@Override
	protected BaseBO getBO() {
		return brandBO;
	}

	@Override
	protected BaseBO getSyncCacheBO() {
		return brandSyncCacheBO;
	}

	@Override
	protected BaseBO getSyncCacheDispatcherBO() {
		return brandSyncCacheDispatcherBO;
	}

	@Override
	protected BaseSyncCache getSyncCache() {
		return new BrandSyncCache();
	}

	@Override
	protected EnumSyncCacheType getSyncCacheType() {
		return EnumSyncCacheType.ESCT_BrandSyncInfo;
	}

	@Override
	protected int getDeleteAllSyncCacheCaseID() {
		return BaseBO.CASE_X_DeleteAllBrandSyncCache;
	}

	@Override
	protected BaseSyncCacheDispatcher getSyncCacheDispatcher() {
		return new BrandSyncCacheDispatcher();
	}

	@Override
	protected BaseSyncCache getObjectSyncCache() {
		return new BrandSyncCache();
	}

	@Override
	protected BaseModel getModel() {
		return new Brand();
	}

	@Override
	protected SyncCache getCacheObjectSyncCache() {
		return cacheBrandSyncCache;
	}

	@Override
	protected BaseSyncSITTestThread2 getThread2(MockMvc mvc, HttpSession session) {
		return new BrandSyncThread2(mvc, session);
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

	/** 1.调用BO创建两个Brand 2.对同步块主从表插入数据，pos2，3，4，5已经更新完成 */
	@Test(timeOut = 60000)
	public void createBrandAndSyncPartOfPoses() throws Exception {
		Shared.printTestMethodStartInfo();

		assertTrue(cacheBrandSyncCache.loadForTestNGTest(Shared.DBName_Test));
		
		createObjectAndSyncPartOfPoses();
	}

	@Test(dependsOnMethods = { "createBrandAndSyncPartOfPoses" }, timeOut = 60000)
	public void syncCategoryInPos1() throws Exception {
		Shared.printTestMethodStartInfo();

		assertTrue(cacheBrandSyncCache.loadForTestNGTest(Shared.DBName_Test));

		syncObjectInPos1();
	}

	@Override
	protected BaseModel createObject1() throws Exception {
		// 创建两个Brand
		Brand b1 = BaseBrandTest.DataInput.getBrand();

		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Brand s1Create = (Brand) brandBO.createObject(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, b1);
		if (brandBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
			System.out.println("Brand创建失败：" + brandBO.getLastErrorCode());
			return null;
		}

		return s1Create;
	}
}
