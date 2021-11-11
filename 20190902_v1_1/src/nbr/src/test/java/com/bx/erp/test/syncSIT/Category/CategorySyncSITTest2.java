package com.bx.erp.test.syncSIT.Category;

import static org.testng.Assert.assertTrue;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.testng.Assert;
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
import com.bx.erp.model.commodity.Category;
import com.bx.erp.model.commodity.CategorySyncCache;
import com.bx.erp.model.commodity.CategorySyncCacheDispatcher;
import com.bx.erp.test.Shared;
import com.bx.erp.test.commodity.CategorySyncThread2;
import com.bx.erp.test.syncSIT.BaseSyncSITTest2;
import com.bx.erp.test.syncSIT.BaseSyncSITTestThread2;
import com.bx.erp.util.DataSourceContextHolder;

@WebAppConfiguration
public class CategorySyncSITTest2 extends BaseSyncSITTest2 {

	@Resource
	private com.bx.erp.cache.commodity.CategorySyncCache cacheCategorySyncCache;

	@Override
	protected BaseBO getBO() {
		return categoryBO;
	}

	@Override
	protected BaseBO getSyncCacheBO() {
		return categorySyncCacheBO;
	}

	@Override
	protected BaseBO getSyncCacheDispatcherBO() {
		return categorySyncCacheDispatcherBO;
	}

	@Override
	protected BaseSyncCache getSyncCache() {
		return new CategorySyncCache();
	}

	@Override
	protected EnumSyncCacheType getSyncCacheType() {
		return EnumSyncCacheType.ESCT_CategorySyncInfo;
	}

	@Override
	protected int getDeleteAllSyncCacheCaseID() {
		return BaseBO.CASE_X_DeleteAllCategorySyncCache;
	}

	@Override
	protected BaseSyncCacheDispatcher getSyncCacheDispatcher() {
		return new CategorySyncCacheDispatcher();
	}

	@Override
	protected BaseSyncCache getObjectSyncCache() {
		return new CategorySyncCache();
	}

	@Override
	protected BaseModel getModel() {
		return new Category();
	}

	@Override
	protected SyncCache getCacheObjectSyncCache() {
		return cacheCategorySyncCache;
	}

	@Override
	protected BaseSyncSITTestThread2 getThread2(MockMvc mvc, HttpSession session) {
		return new CategorySyncThread2(mvc, session);
	}

	public static class DataInput {
		private static Category category = new Category();

		protected static final Category getCategory() throws Exception {
			category.setName("类别测试" + System.currentTimeMillis() % 1000000);
			category.setParentID(1);
			return (Category) category.clone();
		}
	}

	@BeforeClass
	public void setup() {
		Shared.printTestClassStartInfo();

		doSetup();
	}

	@AfterClass
	public void tearDown() {
		super.tearDown();
	}

	/** 1.调用BO创建两个Category 2.对同步块主从表插入数据，pos2，3，4，5已经更新完成 */
	@Test(timeOut = 60000)
	public void createCategoryAndSyncPartOfPoses() throws Exception {
		Shared.printTestMethodStartInfo();

		createObjectAndSyncPartOfPoses();
	}

	@Test(dependsOnMethods = { "createCategoryAndSyncPartOfPoses" }, timeOut = 60000)
	public void syncCategoryInPos1() throws Exception {
		Shared.printTestMethodStartInfo();

		assertTrue(cacheCategorySyncCache.loadForTestNGTest(Shared.DBName_Test));

		syncObjectInPos1();
	}

	protected BaseModel createObject1() throws Exception {
		Category category = DataInput.getCategory();

		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Category categoryC = (Category) categoryBO.createObject(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, category);
		if (categoryBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
			System.out.println("Category创建失败：" + categoryBO.getLastErrorCode());
			return null;
		}

		category.setIgnoreIDInComparision(true);
		Assert.assertTrue(category.compareTo(categoryC) == 0, "查询的对象与创建的对象不一致");

		return categoryC;
	}
}
