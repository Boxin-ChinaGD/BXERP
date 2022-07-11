package com.bx.erp.test.promotion;

import static org.testng.Assert.assertTrue;

import java.util.List;
import java.util.Map;
import java.util.Random;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.cache.SyncCache;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.trade.PromotionSyncCache;
import com.bx.erp.model.trade.PromotionSyncCacheDispatcher;
import com.bx.erp.test.BaseMapperTest;
import com.bx.erp.test.Shared;
import com.bx.erp.util.DataSourceContextHolder;

public class PromotionSyncCacheDispatcherMapperTest extends BaseMapperTest {

	public static class DataInput {
		private static PromotionSyncCacheDispatcher bcd = new PromotionSyncCacheDispatcher();
		private static PromotionSyncCache ssc = new PromotionSyncCache();

		public static final int POS_ID1 = 1;

		protected static final PromotionSyncCacheDispatcher getSyncCacheDispatcher() throws Exception {
			bcd = new PromotionSyncCacheDispatcher();
			bcd.setSyncCacheID(new Random().nextInt(5) + 1);
			bcd.setPos_ID(new Random().nextInt(5) + 1);

			return (PromotionSyncCacheDispatcher) bcd.clone();
		}

		protected static final PromotionSyncCache getSyncCache() throws CloneNotSupportedException {
			ssc = new PromotionSyncCache();
			ssc.setSyncData_ID(2);
			ssc.setSyncSequence(1);// 本字段在这里无关紧要
			ssc.setSyncType(SyncCache.SYNC_Type_C);
			ssc.setPosID(POS_ID1);

			return (PromotionSyncCache) ssc.clone();
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

	protected PromotionSyncCacheDispatcher createPSCD(PromotionSyncCacheDispatcher pscd) {
		Map<String, Object> paramsForCreate = pscd.getCreateParam(BaseBO.INVALID_CASE_ID, pscd);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		PromotionSyncCacheDispatcher pscdCreate = (PromotionSyncCacheDispatcher) promotionSyncCacheDispatcherMapper.create(paramsForCreate); // ...
		//
		Assert.assertTrue(pscdCreate != null && EnumErrorCode.values()[Integer.parseInt(paramsForCreate.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError,
				paramsForCreate.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		//
		System.out.println("创建对象成功： " + pscdCreate);
		//
		return pscdCreate;
	}

	@Test
	public void createTest_CASE1() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("正常添加");

		PromotionSyncCache psc = DataInput.getSyncCache();
		List<List<BaseModel>> pscList = createPSC(psc);
		PromotionSyncCache pscCreate = (PromotionSyncCache) pscList.get(0).get(0);
		//
		PromotionSyncCacheDispatcher pscd = DataInput.getSyncCacheDispatcher();
		pscd.setSyncCacheID(pscCreate.getID());
		createPSCD(pscd);
	}

	@Test
	public void createTest_CASE2() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("重复添加");

		PromotionSyncCache psc = DataInput.getSyncCache();
		List<List<BaseModel>> pscList = createPSC(psc);
		PromotionSyncCache pscCreate = (PromotionSyncCache) pscList.get(0).get(0);
		//
		PromotionSyncCacheDispatcher pscd = DataInput.getSyncCacheDispatcher();
		pscd.setSyncCacheID(pscCreate.getID());
		// 首次添加
		createPSCD(pscd);
		// 重复添加
		createPSCD(pscd);
	}

	@Test
	public void retrieveNTest() throws Exception {
		Shared.printTestMethodStartInfo();

		PromotionSyncCacheDispatcher scd = DataInput.getSyncCacheDispatcher();
		Map<String, Object> paramsForRetrieveN = scd.getRetrieveNParam(BaseBO.INVALID_CASE_ID, scd);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> scList = (List<BaseModel>) promotionSyncCacheDispatcherMapper.retrieveN(paramsForRetrieveN); // ...
		//
		Assert.assertTrue(scList.size() >= 0 && EnumErrorCode.values()[Integer.parseInt(paramsForRetrieveN.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError,
				paramsForRetrieveN.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		//
		System.out.println("查询对象成功: " + scList);
	}
}
