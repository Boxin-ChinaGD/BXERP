package com.bx.erp.test.syncSIT.commodity;
import javax.servlet.http.HttpSession;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.BaseSyncCache;
import com.bx.erp.model.SyncCacheType.EnumSyncCacheType;
import com.bx.erp.model.commodity.Brand;
import com.bx.erp.model.commodity.BrandSyncCache;
import com.bx.erp.test.BaseBrandTest;
import com.bx.erp.test.Shared;
import com.bx.erp.test.syncSIT.BaseSyncSITTest1;
import com.bx.erp.test.syncSIT.BaseSyncSITTestThread1;

@WebAppConfiguration
public class BrandSyncSITTest1 extends BaseSyncSITTest1 {

	protected final String SyncActionDeleteExURL = "/brandSync/deleteEx.bx";

	@Override
	protected BaseBO getSyncCacheBO() {
		return brandSyncCacheBO;
	}

	@Override
	protected String getSyncActionDeleteExURL() {
		return SyncActionDeleteExURL;
	}

	@Override
	protected BaseModel getModel() {
		return new Brand();
	}

	@Override
	protected BaseBO getSyncCacheDispatcherBO() {
		return brandSyncCacheDispatcherBO;
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
	protected BaseSyncCache getSyncCache() {
		return new BrandSyncCache();
	}

	@Override
	protected BaseSyncSITTestThread1 getThread(MockMvc mvc, HttpSession session, int iPhase, int iPosNO, int iSyncBlockNO) {
		return new BrandSyncThread1(mvc, session, iPhase, iPosNO, iSyncBlockNO);
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

	/** 1.pos机1创建1个Brand，pos机2创建2个Brand，然后分别做同步更新。 2.pos3，4，5等待pos1，2更新完后，开启同步器
	 * 3.当所有pos机都已经同步完成后，删除DB和同存相关的数据 */
	@Test(timeOut = 60000)
	public void runBrandSyncProcess() throws Exception {
		Shared.printTestMethodStartInfo();

		runSITTest1();
	}

	protected boolean createObject1() throws Exception {
		Brand b = BaseBrandTest.DataInput.getBrand();
		Brand brandCreate = BaseBrandTest.createOrUpdateBrandViaActionWithNoError(b, true, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		super.objectID1 = brandCreate.getID();
		b.setIgnoreIDInComparision(true);
		if (b.compareTo(brandCreate) != 0) {
			Assert.assertTrue(false, "查出的对象和创建的对象不一样！");
		}

		return true;
	}

	protected boolean createObject2AndObject3() throws Exception {

		Brand b = BaseBrandTest.DataInput.getBrand();
		Brand brandCreate = BaseBrandTest.createOrUpdateBrandViaActionWithNoError(b, true, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		b.setIgnoreIDInComparision(true);
		if (b.compareTo(brandCreate) != 0) {
			Assert.assertTrue(false, "查出的对象和创建的对象不一样！");
		}

		Brand b2 = BaseBrandTest.DataInput.getBrand();
		Brand brandCreate2 = BaseBrandTest.createOrUpdateBrandViaActionWithNoError(b2, true, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		b2.setIgnoreIDInComparision(true);
		if (b2.compareTo(brandCreate2) != 0) {
			Assert.assertTrue(false, "查出的对象和创建的对象不一样！");
		}

		return true;
	}
}
