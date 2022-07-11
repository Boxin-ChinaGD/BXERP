package com.bx.erp.test.syncSIT.Category;
import javax.servlet.http.HttpSession;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.BaseModel.EnumBoolean;
import com.bx.erp.model.BaseSyncCache;
import com.bx.erp.model.SyncCacheType.EnumSyncCacheType;
import com.bx.erp.model.commodity.Category;
import com.bx.erp.model.commodity.CategorySyncCache;
import com.bx.erp.test.Shared;
import com.bx.erp.test.commodity.BaseCategoryParentTest;
import com.bx.erp.test.commodity.CategorySyncThread1;
import com.bx.erp.test.syncSIT.BaseSyncSITTest1;
import com.bx.erp.test.syncSIT.BaseSyncSITTestThread1;

@WebAppConfiguration
public class CategorySyncSITTest1 extends BaseSyncSITTest1 {

	protected final String SyncActionDeleteExURL = "/categorySync/deleteEx.bx";
	
	@Override
	protected BaseBO getSyncCacheBO() {
		return categorySyncCacheBO;
	}

	@Override
	protected String getSyncActionDeleteExURL() {
		return SyncActionDeleteExURL;
	}

	@Override
	protected BaseModel getModel() {
		return new Category();
	}

	@Override
	protected BaseBO getSyncCacheDispatcherBO() {
		return categorySyncCacheDispatcherBO;
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
	protected BaseSyncCache getSyncCache() {
		return new CategorySyncCache();
	}

	@Override
	protected BaseSyncSITTestThread1 getThread(MockMvc mvc, HttpSession session, int iPhase, int iPosNO, int iSyncBlockNO) {
		return new CategorySyncThread1(mvc, session, iPhase, iPosNO, iSyncBlockNO);
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

	/** 1.pos机1创建1个Category，pos机2创建2个Category，然后分别做同步更新。
	 * 2.pos3，4，5等待pos1，2更新完后，开启同步器 3.当所有pos机都已经同步完成后，删除DB和同存相关的数据 */
	@Test(timeOut = 60000)
	public void runCategorySyncProcess() throws Exception {
		Shared.printTestMethodStartInfo();

		runSITTest1();
	}

	protected boolean createObject1() throws Exception {
		Category category = BaseCategoryParentTest.DataInput.getCategory();
		category.setReturnObject(EnumBoolean.EB_Yes.getIndex());
		Category categoryCreate = BaseCategoryParentTest.createOrUpdateCategoryViaActionWithNoError(category, true, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		super.objectID1 = categoryCreate.getID();
		//
		category.setIgnoreIDInComparision(true);
		if (category.compareTo(categoryCreate) != 0) {
			Assert.assertTrue(false, "查出的对象和创建的对象不一样！");
		}
		return true;
	}

	protected boolean createObject2AndObject3() throws Exception {
		// 3、pos2 创建Category2 和Category3 ，创建后添加到普存和同存
		Category category2 = BaseCategoryParentTest.DataInput.getCategory();
		category2.setReturnObject(EnumBoolean.EB_Yes.getIndex());		
		Category categoryCreate2 = BaseCategoryParentTest.createOrUpdateCategoryViaActionWithNoError(category2, true, mvc, sessionBoss, mapBO, Shared.DBName_Test);
        //
		category2.setIgnoreIDInComparision(true);
		if (category2.compareTo(categoryCreate2) != 0) {
			Assert.assertTrue(false, "查出的对象和创建的对象不一样！");
		}
         //
		Category category3 = BaseCategoryParentTest.DataInput.getCategory();
		category3.setReturnObject(EnumBoolean.EB_Yes.getIndex());
		Category categoryCreate3 = BaseCategoryParentTest.createOrUpdateCategoryViaActionWithNoError(category3, true, mvc, sessionBoss, mapBO, Shared.DBName_Test);
         //
		category3.setIgnoreIDInComparision(true);
		if (category3.compareTo(categoryCreate3) != 0) {
			Assert.assertTrue(false, "查出的对象和创建的对象不一样！");
		}
		return true;
	}
}
