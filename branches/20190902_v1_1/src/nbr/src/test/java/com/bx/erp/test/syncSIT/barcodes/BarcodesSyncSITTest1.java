package com.bx.erp.test.syncSIT.barcodes;

import static org.testng.Assert.assertTrue;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.SyncCacheType.EnumSyncCacheType;
import com.bx.erp.model.commodity.Commodity;
import com.bx.erp.test.BaseBarcodesTest;
import com.bx.erp.test.BaseCommodityTest;
import com.bx.erp.test.Shared;
import com.bx.erp.test.syncSIT.BaseSyncSITTest1;
import com.bx.erp.test.syncSIT.BaseSyncSITTestThread1;
import com.bx.erp.util.DataSourceContextHolder;
import com.bx.erp.model.Barcodes;
import com.bx.erp.model.BarcodesSyncCache;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.BaseSyncCache;

@WebAppConfiguration
public class BarcodesSyncSITTest1 extends BaseSyncSITTest1 {
	protected static Commodity commodity;

	protected final String SyncActionDeleteExURL = "/barcodesSync/deleteEx.bx";

	@Override
	protected BaseBO getSyncCacheBO() {
		return barcodesSyncCacheBO;
	}

	@Override
	protected String getSyncActionDeleteExURL() {
		return SyncActionDeleteExURL;
	}

	@Override
	protected BaseModel getModel() {
		return new Barcodes();
	}

	@Override
	protected BaseBO getSyncCacheDispatcherBO() {
		return barcodesSyncCacheDispatcherBO;
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
	protected BaseSyncCache getSyncCache() {
		return new BarcodesSyncCache();
	}

	@Override
	protected BaseSyncSITTestThread1 getThread(MockMvc mvc, HttpSession session, int iPhase, int iPosNO, int iSyncBlockNO) {
		return new BarcodesSyncThread1(mvc, session, iPhase, iPosNO, iSyncBlockNO);
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

	/** 1.pos机1创建1个Barcodes，pos机2创建2个Barcodes，然后分别做同步更新。
	 * 2.pos3，4，5等待pos1，2更新完后，开启同步器 3.当所有pos机都已经同步完成后，删除DB和同存相关的数据 */
	@Test(timeOut = 60000)
	public void runBarcodesSyncProcess() throws Exception {
		Shared.printTestMethodStartInfo();

		runSITTest1();
	}

	protected boolean createObject1() throws Exception {
		Commodity commTemplate = BaseCommodityTest.DataInput.getCommodity();

		Map<String, Object> params = commTemplate.getCreateParamEx(BaseBO.INVALID_CASE_ID, commTemplate);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<List<BaseModel>> bm = commodityMapper.createSimpleEx(params);

		commodity = (Commodity) bm.get(0).get(0);

		Barcodes barcodes = (Barcodes) bm.get(1).get(0);
		assertTrue(barcodes != null, "条形码创建失败！");

		commTemplate.setIgnoreIDInComparision(true);
		if (commTemplate.compareTo(commodity) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		Assert.assertTrue(bm != null && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "创建对象成功");
		super.objectID1 = barcodes.getID();

		return true;
	}

	protected boolean createObject2AndObject3() throws Exception {
		// 3、pos2 创建Barcodes2 和 Barcodes3 ，创建后添加到普存和同存
		Barcodes barcodes = BaseBarcodesTest.DataInput.getBarcodes(commodity.getID());
		BaseBarcodesTest.createBarcodesViaAction(barcodes, mvc, getLoginSession(Shared.POS_2_ID), mapBO, Shared.DBName_Test);

		barcodes = BaseBarcodesTest.DataInput.getBarcodes(commodity.getID());
		BaseBarcodesTest.createBarcodesViaAction(barcodes, mvc, getLoginSession(Shared.POS_2_ID), mapBO, Shared.DBName_Test);
		return true;
	}

}
