package com.bx.erp.test.syncSIT.promotion;

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
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.BaseSyncCache;
import com.bx.erp.model.BaseSyncCacheDispatcher;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.SyncCacheType.EnumSyncCacheType;
import com.bx.erp.model.trade.Promotion;
import com.bx.erp.model.trade.PromotionSyncCache;
import com.bx.erp.model.trade.PromotionSyncCacheDispatcher;
import com.bx.erp.test.BasePromotionTest;
import com.bx.erp.test.Shared;
import com.bx.erp.test.syncSIT.BaseSyncSITTest2;
import com.bx.erp.test.syncSIT.BaseSyncSITTestThread2;
import com.bx.erp.util.DataSourceContextHolder;

@WebAppConfiguration
public class PromotionSyncSITTest2 extends BaseSyncSITTest2 {
	@Resource
	private com.bx.erp.cache.trade.promotion.PromotionSyncCache cachePromotionSyncCache;

	@Override
	protected BaseBO getBO() {
		return promotionBO;
	}

	@Override
	protected BaseBO getSyncCacheBO() {
		return promotionSyncCacheBO;
	}

	@Override
	protected BaseBO getSyncCacheDispatcherBO() {
		return promotionSyncCacheDispatcherBO;
	}

	@Override
	protected BaseSyncCache getSyncCache() {
		return new PromotionSyncCache();
	}

	@Override
	protected EnumSyncCacheType getSyncCacheType() {
		return EnumSyncCacheType.ESCT_PromotionSyncInfo;
	}

	@Override
	protected int getDeleteAllSyncCacheCaseID() {
		return BaseBO.CASE_X_DeleteAllPromotionSyncCache;
	}

	@Override
	protected BaseSyncCacheDispatcher getSyncCacheDispatcher() {
		return new PromotionSyncCacheDispatcher();
	}

	@Override
	protected BaseSyncCache getObjectSyncCache() {
		return new PromotionSyncCache();
	}

	@Override
	protected BaseModel getModel() {
		return new Promotion();
	}

	@Override
	protected SyncCache getCacheObjectSyncCache() {
		return cachePromotionSyncCache;
	}

	@Override
	protected BaseSyncSITTestThread2 getThread2(MockMvc mvc, HttpSession session) {
		return new PromotionSyncThread2(mvc, session);
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

	/** 1.调用BO创建两个Promotion 2.对同步块主从表插入数据，pos2，3，4，5已经更新完成 */
	@Test(timeOut = 60000)
	public void createPromotionAndSyncPartOfPoses() throws Exception {
		Shared.printTestMethodStartInfo();

		createObjectAndSyncPartOfPoses();
	}

	@Test(dependsOnMethods = { "createPromotionAndSyncPartOfPoses" }, timeOut = 60000)
	public void syncPromotionInPos1() throws Exception {
		Shared.printTestMethodStartInfo();

		assertTrue(cachePromotionSyncCache.loadForTestNGTest(Shared.DBName_Test));

		syncObjectInPos1();
	}

	protected BaseModel createObject1() throws Exception {
		// 创建两个Promotion
		Promotion b1 = BasePromotionTest.DataInput.getPromotion();

		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Promotion s1Create = (Promotion) promotionBO.createObject(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, b1);
		if (promotionBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
			System.out.println("Promotion创建失败：" + promotionBO.getLastErrorCode());
			return null;
		}

		return s1Create;
	}
}
