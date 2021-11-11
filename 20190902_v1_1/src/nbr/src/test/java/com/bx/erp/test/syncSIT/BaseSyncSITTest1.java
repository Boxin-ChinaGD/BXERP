package com.bx.erp.test.syncSIT;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.testng.Assert.assertTrue;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.cache.SyncCacheManager;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.BaseModelField;
import com.bx.erp.model.BaseSyncCache;
import com.bx.erp.model.Pos;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.SyncCacheType.EnumSyncCacheType;
import com.bx.erp.test.BaseActionTest;
import com.bx.erp.test.Shared;
import com.bx.erp.util.DataSourceContextHolder;
import com.jayway.jsonpath.JsonPath;

import net.sf.json.JSONObject;

public class BaseSyncSITTest1 extends BaseActionTest {

	protected HttpSession[] posHttpSessions;

	/**
	 * pos创建的第一个对象，比如商品类别
	 */
	protected int objectID1;

	protected List<Pos> listPos; // 其ID可能不连续

	/** 记录同步块从表条数，用来校验数据是否正确 */
	protected int slaveSyncBlockNO;

	protected BaseBO getSyncCacheBO() {
		throw new RuntimeException("Not yet implemented!");
	}

	protected BaseBO getSyncCacheDispatcherBO() {
		throw new RuntimeException("Not yet implemented!");
	}

	protected String getSyncActionDeleteExURL() {
		return "";
	}

	protected BaseModel getModel() {
		throw new RuntimeException("Not yet implemented!");
	}

	protected EnumSyncCacheType getSyncCacheType() {
		throw new RuntimeException("Not yet implemented!");
	}

	protected int getDeleteAllSyncCacheCaseID() {
		throw new RuntimeException("Not yet implemented!");
	}

	protected BaseSyncCache getSyncCache() {
		throw new RuntimeException("Not yet implemented!");
	}

	protected BaseSyncSITTestThread1 getThread(MockMvc mvc, HttpSession session, int iPhase, int iPosNO, int iSyncBlockNO) {
		throw new RuntimeException("Not yet implemented!");
	}

	public void doSetup() {
		super.setUp();

		listPos = (List<Pos>) Shared.getPosesFromDB(posBO, Shared.DBName_Test);
		assertTrue(listPos.size() > 2, "POS机的数目必须大于2才能支撑SIT测试！");

		posHttpSessions = new MockHttpSession[listPos.size()];
		for (int i = 0; i < listPos.size(); i++) {
			posHttpSessions[i] = null;
		}
	}
	
	public void tearDown() {
		super.tearDown();
	}

	protected HttpSession getLoginSession(int index) throws Exception {
		if (posHttpSessions[index - 1] != null) {
			return posHttpSessions[index - 1];
		}

		int posID = listPos.get(index - 1).getID();
		Shared.resetPOS(mvc, posID);
		posHttpSessions[index - 1] = Shared.getPosLoginSession(mvc, posID);
		return posHttpSessions[index - 1];
	}

	protected void deleteSyncCacheInDBAndMemory() {
		// 先清空同存和DB中已经存在的同存DB数据，否则可能影响测试
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		getSyncCacheBO().deleteObject(BaseBO.SYSTEM, getDeleteAllSyncCacheCaseID(), getSyncCache());
		assertTrue(getSyncCacheBO().getLastErrorCode() == EnumErrorCode.EC_NoError);
		SyncCacheManager.getCache(Shared.DBName_Test, getSyncCacheType()).deleteAll();
	}

	protected boolean deleteObject1(BaseModel baseModel) throws UnsupportedEncodingException, Exception {
		BaseModelField bmf = new BaseModelField();
		MvcResult pos2DeleteObjectB = mvc.perform(//
				get(getSyncActionDeleteExURL())//
						.param(bmf.getFIELD_NAME_ID(), String.valueOf(this.objectID1))//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) getLoginSession(2))//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		// ...Same as Shared.checkJSONErrorCode(pos2DeleteObjectB);
		String json = pos2DeleteObjectB.getResponse().getContentAsString();
		JSONObject o = JSONObject.fromObject(json);
		String err = JsonPath.read(o, "$.ERROR");
		if (err.compareTo(EnumErrorCode.EC_NoError.toString()) != 0) {
			return false;
		}

		return true;
	}

	protected boolean createObject1() throws Exception {
		return false;
	}

	/** 1.pos机1创建1个Object，pos机2创建2个Object，然后分别做同步更新。 2.pos3，4，5等待pos1，2更新完后，开启同步器
	 * 3.当所有pos机都已经同步完成后，删除DB和同存相关的数据 */
	@SuppressWarnings("unchecked")
	protected void runSITTest1() throws Exception {
		Shared.printTestMethodStartInfo();

		// 先清空同存和DB中已经存在的同存DB数据，否则可能影响以下测试
		deleteSyncCacheInDBAndMemory();

		// 1、开启pos1 和 pos2的同步器
		BaseSyncSITTestThread1 t1 = getThread(mvc, getLoginSession(1), 1, 2, 3);
		BaseSyncSITTestThread1 t2 = getThread(mvc, getLoginSession(2), 1, 2, 3);
		t1.setName("POS1");
		t2.setName("POS2");
		t1.start();
		t2.start();
		t1.setDbName(Shared.DBName_Test);
		t2.setDbName(Shared.DBName_Test);

		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		assertTrue(createObject1());

		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		assertTrue(createObject2AndObject3());

		t1.join();
		t2.join();

		// 4、检查普存和同存是否正确，这时的同存应该有2 Poses X 3 Objects =
		// 6条数据，同存DB的主表应该有3条数据，代表3个Object块未给其它POS同步
		List<BaseModel> ssInfoList = SyncCacheManager.getCache(Shared.DBName_Test, getSyncCacheType()).readN(false, false);

		slaveSyncBlockNO = 0;
		for (BaseModel bm : ssInfoList) {
			slaveSyncBlockNO += bm.getListSlave1().size();
		}

		assertTrue(slaveSyncBlockNO == 2 * 3, "同存应该有2 Poses X 3 Vips = 6条数据！");

		System.out.println("pos1 和 pos2 同步完成！");

		if (getSyncActionDeleteExURL().length() > 0) {
			// pos2去删除Object1
			BaseSyncSITTestThread1 t1_2 = getThread(mvc, getLoginSession(1), 2, 2, 3);
			BaseSyncSITTestThread1 t2_2 = getThread(mvc, getLoginSession(2), 2, 2, 3);
			t1_2.setName("POS1");
			t2_2.setName("POS2");
			t1_2.start();
			t2_2.start();
			t1_2.setDbName(Shared.DBName_Test);
			t2_2.setDbName(Shared.DBName_Test);
			//
			assertTrue(deleteObject1(getModel()));

			t1_2.join();
			t2_2.join();
		} else {
			System.out.println("本业务不支持删除操作，所以不执行pos2去删除Object1");
		}

		// 检查普存和同存是否正确，这时的同存和同存DB应该有2条数据
		List<BaseModel> ssInfoList2 = SyncCacheManager.getCache(Shared.DBName_Test, getSyncCacheType()).readN(false, false);

		slaveSyncBlockNO = 0;
		for (BaseModel bm : ssInfoList2) {
			slaveSyncBlockNO += bm.getListSlave1().size();
		}

		assertTrue(slaveSyncBlockNO == 2 * 3, "同存应该有2 Poses X 3 Vips = 6条数据！");

		System.out.println("pos2去删除Object1后， pos1 和 pos2 同步完成！");

		// 5、等待 pos1 和 pos2 同步器完成后，开启 pos 3、4、5 同步器。此时同存内只有2个同步块
		List<Thread> listThread = new ArrayList<Thread>();
		for (int i = 2; i < listPos.size(); i++) {
			BaseSyncSITTestThread1 t = getThread(mvc, getLoginSession(i + 1), 3, 5, 3);
			t.setName("POS" + String.valueOf(listPos.get(i).getID()));
			t.start();
			t.setDbName(Shared.DBName_Test);
			listThread.add(t);
		}

		for (Thread thread : listThread) {
			thread.join();
		}

		// 6、检查普存和同存是否正确，这个时候同存和同存DB应该为空
		BaseSyncCache ssc = getSyncCache();
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseSyncCache> sscList = (List<BaseSyncCache>) getSyncCacheBO().retrieveNObject(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, ssc);
		switch (getSyncCacheBO().getLastErrorCode()) {
		case EC_NoError:
			assertTrue(SyncCacheManager.getCache(Shared.DBName_Test, getSyncCacheType()).readN(false, false).size() == 0 && sscList.size() == 0, "同存或数据库没有正确删除！");
			break;
		default:
			assertTrue(false);
		}
	}

	protected boolean createObject2AndObject3() throws Exception {
		return false;
	}
}
