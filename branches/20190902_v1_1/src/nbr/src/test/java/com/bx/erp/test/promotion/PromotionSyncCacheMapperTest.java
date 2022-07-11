package com.bx.erp.test.promotion;

import static org.testng.Assert.assertTrue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.cache.SyncCache;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.Pos;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.trade.PromotionSyncCache;
import com.bx.erp.model.trade.PromotionSyncCacheDispatcher;
import com.bx.erp.test.BaseMapperTest;
import com.bx.erp.test.Shared;
import com.bx.erp.util.DataSourceContextHolder;

public class PromotionSyncCacheMapperTest extends BaseMapperTest {

	public static final int POS_ID1 = 1;

	public static final int POS_ID2 = 2;

	public static final int POS_ID3 = 3;

	public static class DataInput {
		private static PromotionSyncCache cc = new PromotionSyncCache();

		protected static final PromotionSyncCache getPromotionSyncCache1() throws CloneNotSupportedException, InterruptedException {
			cc = new PromotionSyncCache();
			cc.setSyncData_ID(3);
			cc.setSyncSequence(1);// 本字段在这里无关紧要
			cc.setSyncType(SyncCache.SYNC_Type_C);
			cc.setPosID(POS_ID1);// iPOSID

			return (PromotionSyncCache) cc.clone();
		}

		protected static final PromotionSyncCache getPromotionSyncCache2() throws CloneNotSupportedException, InterruptedException {
			cc = new PromotionSyncCache();
			cc.setSyncData_ID(4);
			cc.setSyncSequence(1);// 本字段在这里无关紧要
			cc.setSyncType(SyncCache.SYNC_Type_U);
			cc.setPosID(POS_ID2);// iPOSID

			return (PromotionSyncCache) cc.clone();
		}

		protected static final PromotionSyncCache getPromotionSyncCache3() throws CloneNotSupportedException, InterruptedException {
			cc = new PromotionSyncCache();
			cc.setSyncData_ID(5);
			cc.setSyncSequence(1);
			cc.setSyncType(SyncCache.SYNC_Type_D);
			cc.setPosID(POS_ID3);

			return (PromotionSyncCache) cc.clone();
		}
	}

	@BeforeClass
	public void setup() {
		super.setUp();

	}

	@AfterClass
	public void tearDown() {
		super.tearDown();
	}

	protected List<List<BaseModel>> createPSC(PromotionSyncCache psc) {
		Map<String, Object> paramsCreate = psc.getCreateParam(BaseBO.INVALID_CASE_ID, psc);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<List<BaseModel>> pscList = promotionSyncCacheMapper.createEx(paramsCreate); // ...

		Assert.assertTrue(pscList.size() > 0);

		if ((int) paramsCreate.get(PromotionSyncCache.field.getFIELD_NAME_posID()) > 0) {
			assertTrue(pscList.size() == 2);
		} else {
			assertTrue(pscList.size() == 1);
		}
		//
		System.out.println("创建数据成功 ： " + pscList);
		//
		return pscList;
	}

	protected void deletePSC(PromotionSyncCache pscCreate) {
		Map<String, Object> paramsDelete = pscCreate.getDeleteParam(BaseBO.INVALID_CASE_ID, pscCreate);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		promotionSyncCacheMapper.delete(paramsDelete);
		//
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(paramsDelete.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined,
				paramsDelete.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		//
		Map<String, Object> paramsRN = pscCreate.getRetrieveNParam(BaseBO.INVALID_CASE_ID, pscCreate);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> list = (List<BaseModel>) promotionSyncCacheMapper.retrieveN(paramsRN);
		//
		Assert.assertTrue(list.size() == 0 && EnumErrorCode.values()[Integer.parseInt(paramsRN.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, paramsRN.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		//
		System.out.println("删除对象成功");
	}

	@Test
	public void createTest_CASE1() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog(" CASE1:正常添加，非U型，POSID > 0 ");

		PromotionSyncCache psc = DataInput.getPromotionSyncCache1();
		createPSC(psc);
		//
		Map<String, Object> param = new HashMap<String, Object>();
		// 删除所有数据，否则影响一下次测试
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		promotionSyncCacheMapper.deleteAll(param);
	}

	@Test
	public void createTest_CASE2() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("  CASE2:重复添加，非U型，POSID > 0 ");

		PromotionSyncCache psc = DataInput.getPromotionSyncCache1();
		// 首次添加
		createPSC(psc);
		// 重复添加
		createPSC(psc);
		//
		Map<String, Object> param = new HashMap<String, Object>();
		// 删除所有数据，否则影响一下次测试
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		promotionSyncCacheMapper.deleteAll(param);
	}

	@Test
	public void createTest_CASE3() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("  CASE3:正常添加，非U型，POSID <= 0 ");

		PromotionSyncCache psc = DataInput.getPromotionSyncCache1();
		psc.setPosID(BaseAction.INVALID_POS_ID);
		createPSC(psc);
		//
		Map<String, Object> param = new HashMap<String, Object>();
		// 删除所有数据，否则影响一下次测试
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		promotionSyncCacheMapper.deleteAll(param);
	}

	@Test
	public void createTest_CASE4() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("  CASE4:重复添加，非U型，POSID <= 0 ");

		PromotionSyncCache psc = DataInput.getPromotionSyncCache1();
		psc.setPosID(BaseAction.INVALID_POS_ID);
		// 首次添加
		createPSC(psc);
		// 重复添加
		createPSC(psc);
		//
		Map<String, Object> param = new HashMap<String, Object>();
		// 删除所有数据，否则影响一下次测试
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		promotionSyncCacheMapper.deleteAll(param);
	}

	@Test
	public void createTest_CASE5() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("  CASE5:正常添加，U型，POSID > 0 ");

		PromotionSyncCache psc = DataInput.getPromotionSyncCache2();
		createPSC(psc);
		//
		Map<String, Object> param = new HashMap<String, Object>();
		// 删除所有数据，否则影响一下次测试
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		promotionSyncCacheMapper.deleteAll(param);
	}

	@Test
	public void createTest_CASE6() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("  CASE6:重复添加，U型，POSID > 0 ");

		PromotionSyncCache psc = DataInput.getPromotionSyncCache2();
		// 首次添加
		createPSC(psc);
		// 重复添加
		createPSC(psc);
		//
		Map<String, Object> param = new HashMap<String, Object>();
		// 删除所有数据，否则影响一下次测试
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		promotionSyncCacheMapper.deleteAll(param);
	}

	@Test
	public void createTest_CASE7() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("  CASE7:正常添加，U型，POSID <= 0 ");

		PromotionSyncCache psc = DataInput.getPromotionSyncCache2();
		psc.setPosID(BaseAction.INVALID_POS_ID);
		psc.setSyncData_ID(2);
		createPSC(psc);
		//
		Map<String, Object> param = new HashMap<String, Object>();
		// 删除所有数据，否则影响一下次测试
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		promotionSyncCacheMapper.deleteAll(param);
	}

	@Test
	public void createTest_CASE8() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("  CASE8:重复添加，U型，POSID <= 0 ");

		PromotionSyncCache psc = DataInput.getPromotionSyncCache2();
		psc.setPosID(BaseAction.INVALID_POS_ID);
		psc.setSyncData_ID(2);
		// 首次添加
		createPSC(psc);
		// 重复添加
		createPSC(psc);
		//
		Map<String, Object> param = new HashMap<String, Object>();
		// 删除所有数据，否则影响一下次测试
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		promotionSyncCacheMapper.deleteAll(param);
	}

	@Test
	public void createTest_CASE9() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("  CASE9:正常添加，D型，POSID > 0 ");

		PromotionSyncCache psc = DataInput.getPromotionSyncCache3();
		createPSC(psc);
		//
		Map<String, Object> param = new HashMap<String, Object>();
		// 删除所有数据，否则影响一下次测试
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		promotionSyncCacheMapper.deleteAll(param);
	}

	@Test
	public void createTest_CASE10() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("  CASE10:重复添加，D型，POSID > 0 ");

		PromotionSyncCache psc = DataInput.getPromotionSyncCache3();
		// 首次添加
		createPSC(psc);
		// 重复添加
		createPSC(psc);
		//
		Map<String, Object> param = new HashMap<String, Object>();
		// 删除所有数据，否则影响一下次测试
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		promotionSyncCacheMapper.deleteAll(param);
	}

	@Test
	public void createTest_CASE11() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("  CASE11:正常添加，D型， POSID <= 0");

		PromotionSyncCache psc = DataInput.getPromotionSyncCache3();
		psc.setPosID(BaseAction.INVALID_POS_ID);
		createPSC(psc);
		//
		Map<String, Object> param = new HashMap<String, Object>();
		// 删除所有数据，否则影响一下次测试
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		promotionSyncCacheMapper.deleteAll(param);
	}

	@Test
	public void createTest_CASE12() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("  CASE12:重复添加，D型，POSID <= 0 ");

		PromotionSyncCache psc = DataInput.getPromotionSyncCache3();
		psc.setPosID(BaseAction.INVALID_POS_ID);
		// 首次添加
		createPSC(psc);
		// 重复添加
		createPSC(psc);
		//
		Map<String, Object> param = new HashMap<String, Object>();
		// 删除所有数据，否则影响一下次测试
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		promotionSyncCacheMapper.deleteAll(param);
	}

	@Test
	public void retrieveNTest() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		PromotionSyncCache sc = DataInput.getPromotionSyncCache1();
		//
		createPSC(sc);
		//
		Map<String, Object> paramsForRetrieveN = sc.getRetrieveNParam(BaseBO.INVALID_CASE_ID, sc);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> scList = (List<BaseModel>) promotionSyncCacheMapper.retrieveN(paramsForRetrieveN); // ...
		//
		Assert.assertTrue(scList.size() > 0 && EnumErrorCode.values()[Integer.parseInt(paramsForRetrieveN.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError,
				paramsForRetrieveN.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		//
		System.out.println("查询对象成功： " + scList);
	}

	@Test
	public void deleteTest_CASE1() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("  CASE1:POS机已经全部同步,删除主表和从表,错误码0");

		PromotionSyncCache psc = DataInput.getPromotionSyncCache1();
		List<List<BaseModel>> list = createPSC(psc);
		PromotionSyncCache pscCreate = (PromotionSyncCache) list.get(0).get(0);

		PromotionSyncCacheDispatcher dispatcher = new PromotionSyncCacheDispatcher();
		int Bid = pscCreate.getID();
		// 按这种方式同步的话，只同步了5个pos机，但Jenkins测试的时候回创建新的pos机，导致同步失败
		// for (int i = 2; i < 6; i++) {
		// dispatcher.setSyncCacheID(Bid);
		// dispatcher.setPos_ID(i);
		// Map<String, Object> paramsForCreate2 =
		// dispatcher.getCreateParam(BaseBO.INVALID_CASE_ID, dispatcher);
		// PromotionSyncCacheDispatcher scdCreate = (PromotionSyncCacheDispatcher)
		// dispatcherMapper.create(paramsForCreate2);
		// dispatcher.setIgnoreIDInComparision(true);
		// if (dispatcher.compareTo(scdCreate) != 0) {
		// Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		// }
		//
		// Assert.assertEquals(EnumErrorCode.values()[Integer.parseInt(paramsForCreate2.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())],
		// EnumErrorCode.EC_NoError);
		// }
		Pos posStatus = new Pos();
		posStatus.setShopID(-1);
		Map<String, Object> paramsStatus = posStatus.getRetrieveNParam(BaseBO.INVALID_CASE_ID, posStatus);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> retrieveNStatus = posMapper.retrieveN(paramsStatus);
		//
		Assert.assertTrue(retrieveNStatus != null && retrieveNStatus.size() > 0 && EnumErrorCode.values()[Integer.parseInt(paramsStatus.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError,
				paramsStatus.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		//
		for (BaseModel b : retrieveNStatus) {
			Pos p = (Pos) b;
			dispatcher.setSyncCacheID(Bid);
			dispatcher.setPos_ID(p.getID());
			//
			Map<String, Object> paramsCreate = dispatcher.getCreateParam(BaseBO.INVALID_CASE_ID, dispatcher);
			//
			DataSourceContextHolder.setDbName(Shared.DBName_Test);
			PromotionSyncCacheDispatcher scdCreate = (PromotionSyncCacheDispatcher) promotionSyncCacheDispatcherMapper.create(paramsCreate);
			//
			Assert.assertTrue(scdCreate != null && EnumErrorCode.values()[Integer.parseInt(paramsCreate.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError,
					paramsCreate.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString());
			//
			System.out.println("创建对象成功： " + scdCreate);
		}
		//
		List<BaseModel> list2 = list.get(0);
		PromotionSyncCache ssc2 = (PromotionSyncCache) list2.get(0);
		Map<String, Object> paramsDelete2 = ssc2.getDeleteParam(BaseBO.INVALID_CASE_ID, ssc2);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		promotionSyncCacheMapper.delete(paramsDelete2);
		//
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(paramsDelete2.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, paramsDelete2.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		//
		Map<String, Object> paramsRN2 = ssc2.getRetrieveNParam(BaseBO.INVALID_CASE_ID, ssc2);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> list3 = (List<BaseModel>) promotionSyncCacheMapper.retrieveN(paramsRN2);
		//
		Assert.assertTrue(list3.size() == 0 && EnumErrorCode.values()[Integer.parseInt(paramsRN2.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, paramsRN2.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		//
		System.out.println("删除对象成功");
	}

	@Test
	public void deleteTest_CASE2() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE2:给所有的从表添加这个数据，才能删除 错误码：7");

		PromotionSyncCache psc = DataInput.getPromotionSyncCache1();
		List<List<BaseModel>> list = createPSC(psc);

		PromotionSyncCache pscCreate = (PromotionSyncCache) list.get(0).get(0);

		Map<String, Object> paramsDelete = pscCreate.getDeleteParam(BaseBO.INVALID_CASE_ID, pscCreate);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		promotionSyncCacheMapper.delete(paramsDelete);
		//
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(paramsDelete.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined,
				paramsDelete.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		//
		Map<String, Object> paramsForRetrieveN = pscCreate.getRetrieveNParam(BaseBO.INVALID_CASE_ID, pscCreate);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> scList = (List<BaseModel>) promotionSyncCacheMapper.retrieveN(paramsForRetrieveN); // ...
		//
		Assert.assertTrue(scList.size() > 0 && EnumErrorCode.values()[Integer.parseInt(paramsForRetrieveN.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError,
				paramsForRetrieveN.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		//
		System.out.println("删除对象失败： " + scList);
		//
		Map<String, Object> param = new HashMap<String, Object>();
		// 删除所有数据，否则影响一下次测试
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		promotionSyncCacheMapper.deleteAll(param);
	}
}
