package com.bx.erp.test.syncSIT;

import static org.testng.Assert.assertTrue;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.cache.SyncCache;
import com.bx.erp.cache.SyncCacheManager;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.BaseSyncCache;
import com.bx.erp.model.BaseSyncCacheDispatcher;
import com.bx.erp.model.Pos;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.SyncCacheType.EnumSyncCacheType;
import com.bx.erp.test.BaseActionTest;
import com.bx.erp.test.Shared;
import com.bx.erp.test.syncSIT.BaseSyncSITTestThread2;
import com.bx.erp.util.DataSourceContextHolder;

@WebAppConfiguration
public class BaseSyncSITTest2 extends BaseActionTest {

	private List<Pos> listPos; // 其ID可能不连续

	public static final int POS_ID2 = 2;

	protected BaseBO getBO() {
		throw new RuntimeException("Not yet implemented!");
	}

	protected BaseBO getSyncCacheBO() {
		throw new RuntimeException("Not yet implemented!");
	}

	protected BaseBO getSyncCacheDispatcherBO() {
		throw new RuntimeException("Not yet implemented!");
	}

	protected BaseSyncCache getSyncCache() {
		throw new RuntimeException("Not yet implemented!");
	}

	protected EnumSyncCacheType getSyncCacheType() {
		throw new RuntimeException("Not yet implemented!");
	}

	protected int getDeleteAllSyncCacheCaseID() {
		throw new RuntimeException("Not yet implemented!");
	}

	protected BaseSyncCacheDispatcher getSyncCacheDispatcher() {
		throw new RuntimeException("Not yet implemented!");
	}

	protected BaseSyncCache getObjectSyncCache() {
		throw new RuntimeException("Not yet implemented!");
	}

	protected BaseModel getModel() {
		throw new RuntimeException("Not yet implemented!");
	}

	protected SyncCache getCacheObjectSyncCache() {
		throw new RuntimeException("Not yet implemented!");
	}

	protected BaseSyncSITTestThread2 getThread2(MockMvc mvc, HttpSession session) {
		throw new RuntimeException("Not yet implemented!");
	}

	public void doSetup() {
		super.setUp();

		listPos = (List<Pos>) Shared.getPosesFromDB(posBO, Shared.DBName_Test);
		assertTrue(listPos.size() > 2, "POS机的数目必须大于2才能支撑SIT测试！");
	}

	public void tearDown() {
		super.tearDown();
	}

	protected BaseModel createObject1() throws Exception {
		return null;
	}

	/** 1.调用BO创建两个Object 2.对同步块主从表插入数据，pos2，3，4，5已经更新完成 */
	public void createObjectAndSyncPartOfPoses() throws Exception {
		deleteSyncCacheInDBAndMemory();

		assertTrue(Shared.refreshPosCache(applicationContext));

		BaseModel bm = createObject1();
		assertTrue(bm != null);

		BaseModel bm1 = createObject1();
		assertTrue(bm1 != null);

		createSyncCacheAndCyncCacheDispatcher(bm, bm1);
	}

	protected void deleteSyncCacheInDBAndMemory() {
		// 先清空同存和DB中已经存在的同存DB数据，否则可能影响以下测试
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		getSyncCacheBO().deleteObject(BaseBO.SYSTEM, getDeleteAllSyncCacheCaseID(), getObjectSyncCache());
		assertTrue(getSyncCacheBO().getLastErrorCode() == EnumErrorCode.EC_NoError);
		SyncCacheManager.getCache(Shared.DBName_Test, getSyncCacheType()).deleteAll();
	}

	@SuppressWarnings("unchecked")
	protected void createSyncCacheAndCyncCacheDispatcher(BaseModel bm, BaseModel bm1) {
		// 插入同步块主表
		BaseSyncCache ssc1 = getSyncCache();
		ssc1.setSyncData_ID(bm.getID());
		ssc1.setSyncSequence(1);// 本字段在这里无关紧要
		ssc1.setSyncType(SyncCache.SYNC_Type_C);
		ssc1.setPosID(POS_ID2);

		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<List<BaseModel>> list1 = getSyncCacheBO().createObjectEx(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, ssc1);
		if (getSyncCacheBO().getLastErrorCode() != EnumErrorCode.EC_NoError) {
			System.out.println("同步块主表插入失败：" + getSyncCacheBO().getLastErrorCode());
			return;
		}

		BaseSyncCache ssc2 = getSyncCache();
		ssc2.setSyncData_ID(bm1.getID());
		ssc2.setSyncSequence(1);// 本字段在这里无关紧要
		ssc2.setSyncType(SyncCache.SYNC_Type_C);
		ssc2.setPosID(POS_ID2);

		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<List<BaseModel>> list2 = getSyncCacheBO().createObjectEx(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, ssc2);
		if (getSyncCacheBO().getLastErrorCode() != EnumErrorCode.EC_NoError) {
			System.out.println("同步块主表插入失败：" + getSyncCacheBO().getLastErrorCode());
			return;
		}

		// 插入同步块从表
		BaseSyncCacheDispatcher sscd1A = getSyncCacheDispatcher();
		sscd1A.setPos_ID(3);
		sscd1A.setSyncCacheID(list1.get(0).get(0).getID());

		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		BaseSyncCacheDispatcher sscd1ACreate = (BaseSyncCacheDispatcher) getSyncCacheDispatcherBO().createObject(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, sscd1A);
		if (getSyncCacheDispatcherBO().getLastErrorCode() != EnumErrorCode.EC_NoError) {
			System.out.println("同步块从表插入失败：" + getSyncCacheDispatcherBO().getLastErrorCode());
			return;
		} else {
			System.out.println("同步块从表插入成功：" + sscd1ACreate == null ? "null" : sscd1ACreate.toString());
		}

		BaseSyncCacheDispatcher sscd1B = getSyncCacheDispatcher();
		sscd1B.setPos_ID(3);
		sscd1B.setSyncCacheID(list2.get(0).get(0).getID());

		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		BaseSyncCacheDispatcher sscd1BCreate = (BaseSyncCacheDispatcher) getSyncCacheDispatcherBO().createObject(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, sscd1B);
		if (getSyncCacheDispatcherBO().getLastErrorCode() != EnumErrorCode.EC_NoError) {
			System.out.println("同步块从表插入失败：" + getSyncCacheDispatcherBO().getLastErrorCode());
			return;
		} else {
			System.out.println("同步块从表插入成功：" + sscd1BCreate == null ? "null" : sscd1BCreate.toString());
		}
		//
		BaseSyncCacheDispatcher sscd2A = getSyncCacheDispatcher();
		sscd2A.setPos_ID(4);
		sscd2A.setSyncCacheID(list1.get(0).get(0).getID());

		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		BaseSyncCacheDispatcher sscd2ACreate = (BaseSyncCacheDispatcher) getSyncCacheDispatcherBO().createObject(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, sscd2A);
		if (getSyncCacheDispatcherBO().getLastErrorCode() != EnumErrorCode.EC_NoError) {
			System.out.println("同步块从表插入失败：" + getSyncCacheDispatcherBO().getLastErrorCode());
			return;
		} else {
			System.out.println("同步块从表插入成功：" + sscd2ACreate == null ? "null" : sscd2ACreate.toString());
		}

		BaseSyncCacheDispatcher sscd2B = getSyncCacheDispatcher();
		sscd2B.setPos_ID(4);
		sscd2B.setSyncCacheID(list2.get(0).get(0).getID());

		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		BaseSyncCacheDispatcher sscd2BCreate = (BaseSyncCacheDispatcher) getSyncCacheDispatcherBO().createObject(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, sscd2B);
		if (getSyncCacheDispatcherBO().getLastErrorCode() != EnumErrorCode.EC_NoError) {
			System.out.println("同步块从表插入失败：" + getSyncCacheDispatcherBO().getLastErrorCode());
			return;
		} else {
			System.out.println("同步块从表插入成功：" + sscd2BCreate == null ? "null" : sscd2BCreate.toString());
		}
		//
		BaseSyncCacheDispatcher sscd3A = getSyncCacheDispatcher();
		sscd3A.setPos_ID(5);
		sscd3A.setSyncCacheID(list1.get(0).get(0).getID());

		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		BaseSyncCacheDispatcher sscd3ACreate = (BaseSyncCacheDispatcher) getSyncCacheDispatcherBO().createObject(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, sscd3A);
		if (getSyncCacheDispatcherBO().getLastErrorCode() != EnumErrorCode.EC_NoError) {
			System.out.println("同步块从表插入失败：" + getSyncCacheDispatcherBO().getLastErrorCode());
			return;
		} else {
			System.out.println("同步块从表插入成功：" + sscd3ACreate == null ? "null" : sscd3ACreate.toString());
		}

		BaseSyncCacheDispatcher sscd3B = getSyncCacheDispatcher();
		sscd3B.setPos_ID(5);
		sscd3B.setSyncCacheID(list2.get(0).get(0).getID());

		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		BaseSyncCacheDispatcher sscd3BCreate = (BaseSyncCacheDispatcher) getSyncCacheDispatcherBO().createObject(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, sscd3B);
		if (getSyncCacheDispatcherBO().getLastErrorCode() != EnumErrorCode.EC_NoError) {
			System.out.println("同步块从表插入失败：" + getSyncCacheDispatcherBO().getLastErrorCode());
			return;
		} else {
			System.out.println("同步块从表插入成功：" + sscd3BCreate == null ? "null" : sscd3BCreate.toString());
		}

		BaseSyncCacheDispatcher ssc = getSyncCacheDispatcher();

		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseSyncCacheDispatcher> vscdList = (List<BaseSyncCacheDispatcher>) getSyncCacheDispatcherBO().retrieveNObject(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, ssc);
		switch (getSyncCacheDispatcherBO().getLastErrorCode()) {
		case EC_NoError:
			assertTrue(vscdList.size() == 8, "同存数据没有正确插入");
			break;
		default:
			assertTrue(false);
		}
	}

	@SuppressWarnings("unchecked")
	public void syncObjectInPos1() throws Exception {
		BaseSyncSITTestThread2[] thread2 = new BaseSyncSITTestThread2[listPos.size()];
		for (int i = 0; i < thread2.length; i++) {
			int posID = listPos.get(i).getID();
			Shared.resetPOS(mvc, posID);
			thread2[i] = getThread2(mvc, Shared.getPosLoginSession(mvc, posID));
			thread2[i].setName("POS" + (posID + 1));
			thread2[i].start();
		}

		for (BaseSyncSITTestThread2 t : thread2) {
			t.join();
		}

		// 检查普存和同存是否正确，这个时候同存和同存DB应该为空
		BaseSyncCache ssc = getSyncCache();

		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseSyncCache> sscList = (List<BaseSyncCache>) getSyncCacheBO().retrieveNObject(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, ssc);
		switch (getSyncCacheBO().getLastErrorCode()) {
		case EC_NoError:
			assertTrue(SyncCacheManager.getCache(Shared.DBName_Test, getSyncCacheType()).readN(false, false).size() == 0 && sscList.size() == 0, "同存数据没有正确删除！");
			break;
		default:
			assertTrue(false);
		}
	}
}
