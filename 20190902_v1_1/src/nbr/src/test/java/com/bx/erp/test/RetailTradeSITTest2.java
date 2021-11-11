//package com.bx.erp.test;
//
//import static org.testng.Assert.assertTrue;
//
//import java.util.List;
//
//import javax.annotation.Resource;
//
//import org.springframework.test.context.web.WebAppConfiguration;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//import org.springframework.web.context.WebApplicationContext;
//import org.springframework.web.filter.CharacterEncodingFilter;
//import org.testng.Assert;
//import org.testng.annotations.AfterClass;
//import org.testng.annotations.BeforeClass;
//import org.testng.annotations.Test;
//
//import com.bx.erp.action.bo.BaseBO;
//import com.bx.erp.action.bo.PosBO;
//import com.bx.erp.action.bo.RetailTradeBO;
//import com.bx.erp.action.bo.RetailTradeSyncCacheBO;
//import com.bx.erp.action.bo.RetailTradeSyncCacheDispatcherBO;
//import com.bx.erp.cache.SyncCache;
//import com.bx.erp.cache.SyncCacheManager;
//import com.bx.erp.model.BaseModel;
//import com.bx.erp.model.Pos;
//import com.bx.erp.model.RetailTrade;
//import com.bx.erp.model.RetailTradeSyncCache;
//import com.bx.erp.model.RetailTradeSyncCacheDispatcher;
//import com.bx.erp.model.ErrorInfo.EnumErrorCode;
//import com.bx.erp.model.SyncCacheType.EnumSyncCacheType;
//import com.bx.erp.test.syncSIT.RetailTrade.RetailTradeSyncSITTest1.DataInput;
//import com.bx.erp.test.syncSIT.RetailTrade.RetailTradeSyncThread2;
//
///*
// * 零售单同步相关测试
// * */
//@WebAppConfiguration
//public class RetailTradeSITTest2 extends BaseTestNGSpringContextTest {
//
////	@Resource
////	private com.bx.erp.cache.RetailTradeSyncCache retailTradeSyncCache;
//
//	@Resource
//	private RetailTradeSyncCacheBO retailTradeSyncCacheBO;
//
//	@Resource
//	private RetailTradeSyncCacheDispatcherBO retailTradeSyncCacheDispatcherBO;
//
//	@Resource
//	private RetailTradeBO retailTradeBO;
//
//	private List<Pos> listPos; // 其ID可能不连续
//
//	@Resource
//	private WebApplicationContext wac;
//	private MockMvc mvc;
//
//	@Resource
//	private PosBO posBO;
//
//	@BeforeClass
//	public void setup() {
//		Shared.printTestClassStartInfo();
//
//		CharacterEncodingFilter encodingFilter = new CharacterEncodingFilter();
//		encodingFilter.setEncoding("utf-8");
//		encodingFilter.setForceEncoding(true);
//
//		mvc = MockMvcBuilders.webAppContextSetup(wac).addFilter(encodingFilter, "/*").build();
//
//		listPos = (List<Pos>) getPosesFromDB(posBO);
//		assertTrue(listPos.size() > 2, "POS机的数目必须大于2才能支撑SIT测试！");
//	}
//
//	@AfterClass
//	public void tearDown() {
//		Shared.printTestClassEndInfo();
//	}
//
//	/** 1.调用BO创建两个RetailTrade 2.对同步块主从表插入数据，pos2，3，4，5已经更新完成 */
//	@SuppressWarnings({ "unused", "unchecked" })
//	@Test
//	public void createRetailTradeAndSyncPartOfPoses() throws Exception {
//		Shared.printTestMethodStartInfo();
//
//		// 先清空同存和DB中已经存在的同存DB数据，否则可能影响以下测试
//		retailTradeSyncCacheBO.deleteObject(BaseBO.SYSTEM, BaseBO.CASE_X_DeleteAllRetailTradeSyncCache, new RetailTradeSyncCache());
//		assertTrue(retailTradeSyncCacheBO.getLastErrorCode() == EnumErrorCode.EC_NoError);
//		SyncCacheManager.getCache(EnumSyncCacheType.ESCT_RetailTradeSyncInfo).deleteAll();
//
//		// 创建两个RetailTrade
//		RetailTrade s1 = DataInput.getRetailTrade();
//		RetailTrade s1Create = (RetailTrade) retailTradeBO.createObject(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, s1);
//		Assert.assertEquals(retailTradeBO.getLastErrorCode(), EnumErrorCode.EC_NoError, "RetailTrade创建失败");
//
//		RetailTrade s2 = DataInput.getRetailTrade();
//		RetailTrade s2Create = (RetailTrade) retailTradeBO.createObject(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, s2);
//		Assert.assertEquals(retailTradeBO.getLastErrorCode(), EnumErrorCode.EC_NoError, "RetailTrade创建失败");
//
//		// 插入同步块主表
//		RetailTradeSyncCache ssc1 = new RetailTradeSyncCache();
//		ssc1.setSyncData_ID(s1Create.getID());
//		ssc1.setSyncSequence(1);// 本字段在这里无关紧要
//		ssc1.setSyncType(SyncCache.SYNC_Type_C);
//		ssc1.setInt1(2);
//		List<List<BaseModel>> list1 = retailTradeSyncCacheBO.createObjectEx(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, ssc1);
//		Assert.assertEquals(retailTradeSyncCacheBO.getLastErrorCode(), EnumErrorCode.EC_NoError, "创建失败");
//
//		RetailTradeSyncCache ssc2 = new RetailTradeSyncCache();
//		ssc2.setSyncData_ID(s2Create.getID());
//		ssc2.setSyncSequence(1);// 本字段在这里无关紧要
//		ssc2.setSyncType(SyncCache.SYNC_Type_C);
//		ssc2.setInt1(2);
//		List<List<BaseModel>> list2 = retailTradeSyncCacheBO.createObjectEx(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, ssc2);
//		Assert.assertEquals(retailTradeSyncCacheBO.getLastErrorCode(), EnumErrorCode.EC_NoError, "创建失败");
//
//		// 插入同步块从表
//		RetailTradeSyncCacheDispatcher sscd1A = new RetailTradeSyncCacheDispatcher();
//		sscd1A.setPos_ID(3);
//		sscd1A.setSyncCacheID(list1.get(0).get(0).getID());
//		RetailTradeSyncCacheDispatcher sscd1ACreate = (RetailTradeSyncCacheDispatcher) retailTradeSyncCacheDispatcherBO.createObject(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, sscd1A);
//		Assert.assertEquals(retailTradeSyncCacheDispatcherBO.getLastErrorCode(), EnumErrorCode.EC_NoError, "创建失败");
//
//		RetailTradeSyncCacheDispatcher sscd1B = new RetailTradeSyncCacheDispatcher();
//		sscd1B.setPos_ID(3);
//		sscd1B.setSyncCacheID(list2.get(0).get(0).getID());
//		RetailTradeSyncCacheDispatcher sscd1BCreate = (RetailTradeSyncCacheDispatcher) retailTradeSyncCacheDispatcherBO.createObject(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, sscd1B);
//		Assert.assertEquals(retailTradeSyncCacheDispatcherBO.getLastErrorCode(), EnumErrorCode.EC_NoError, "创建失败");
//		//
//		RetailTradeSyncCacheDispatcher sscd2A = new RetailTradeSyncCacheDispatcher();
//		sscd2A.setPos_ID(4);
//		sscd2A.setSyncCacheID(list1.get(0).get(0).getID());
//		RetailTradeSyncCacheDispatcher sscd2ACreate = (RetailTradeSyncCacheDispatcher) retailTradeSyncCacheDispatcherBO.createObject(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, sscd2A);
//		Assert.assertEquals(retailTradeSyncCacheDispatcherBO.getLastErrorCode(), EnumErrorCode.EC_NoError, "创建失败");
//
//		RetailTradeSyncCacheDispatcher sscd2B = new RetailTradeSyncCacheDispatcher();
//		sscd2B.setPos_ID(4);
//		sscd2B.setSyncCacheID(list2.get(0).get(0).getID());
//		RetailTradeSyncCacheDispatcher sscd2BCreate = (RetailTradeSyncCacheDispatcher) retailTradeSyncCacheDispatcherBO.createObject(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, sscd2B);
//		Assert.assertEquals(retailTradeSyncCacheDispatcherBO.getLastErrorCode(), EnumErrorCode.EC_NoError, "创建失败");
//		//
//		RetailTradeSyncCacheDispatcher sscd3A = new RetailTradeSyncCacheDispatcher();
//		sscd3A.setPos_ID(5);
//		sscd3A.setSyncCacheID(list1.get(0).get(0).getID());
//		RetailTradeSyncCacheDispatcher sscd3ACreate = (RetailTradeSyncCacheDispatcher) retailTradeSyncCacheDispatcherBO.createObject(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, sscd3A);
//		Assert.assertEquals(retailTradeSyncCacheDispatcherBO.getLastErrorCode(), EnumErrorCode.EC_NoError, "创建失败");
//		RetailTradeSyncCacheDispatcher sscd3B = new RetailTradeSyncCacheDispatcher();
//		sscd3B.setPos_ID(5);
//		sscd3B.setSyncCacheID(list2.get(0).get(0).getID());
//		RetailTradeSyncCacheDispatcher sscd3BCreate = (RetailTradeSyncCacheDispatcher) retailTradeSyncCacheDispatcherBO.createObject(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, sscd3B);
//		Assert.assertEquals(retailTradeSyncCacheDispatcherBO.getLastErrorCode(), EnumErrorCode.EC_NoError, "创建失败");
//
//		RetailTradeSyncCacheDispatcher ssc = new RetailTradeSyncCacheDispatcher();
//		List<RetailTradeSyncCacheDispatcher> vscdList = (List<RetailTradeSyncCacheDispatcher>) retailTradeSyncCacheDispatcherBO.retrieveNObject(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, ssc);
//		switch (retailTradeSyncCacheDispatcherBO.getLastErrorCode()) {
//		case EC_NoError:
//			assertTrue(vscdList.size() == 8, "同存数据没有正确插入");
//			break;
//		default:
//			assertTrue(false);
//		}
//	}
//
//	@SuppressWarnings("unchecked")
//	@Test(dependsOnMethods = { "createRetailTradeAndSyncPartOfPoses" }, timeOut = 1)
//	public void syncRetailTradeInPos1() throws Exception {
//		Shared.printTestMethodStartInfo();
//
////		assertTrue(retailTradeSyncCache.loadForTestNGTest());
//
//		RetailTradeSyncThread2[] thread2 = new RetailTradeSyncThread2[listPos.size()];
//		for (int i = 0; i < thread2.length; i++) {
//			int posID = listPos.get(i).getID();
//			Shared.resetPOS(mvc, posID);
//			thread2[i] = new RetailTradeSyncThread2(mvc, Shared.getPosLoginSession(mvc, posID));
//			thread2[i].setName("POS" + (posID + 1));
//			thread2[i].start();
//		}
//
//		for (RetailTradeSyncThread2 t : thread2) {
//			t.join();
//		}
//
//		// 检查普存和同存是否正确，这个时候同存和同存DB应该为空
//		RetailTradeSyncCache ssc = new RetailTradeSyncCache();
//		List<RetailTradeSyncCache> sscList = (List<RetailTradeSyncCache>) retailTradeSyncCacheBO.retrieveNObject(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, ssc);
//		switch (retailTradeSyncCacheBO.getLastErrorCode()) {
//		case EC_NoError:
//			assertTrue(SyncCacheManager.getCache(EnumSyncCacheType.ESCT_RetailTradeSyncInfo).readN().size() == 0 && sscList.size() == 0, "同存数据没有正确删除！");
//			break;
//		default:
//			assertTrue(false);
//		}
//	}
//}
