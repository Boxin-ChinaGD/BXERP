package com.bx.erp.test;

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
import com.bx.erp.model.PosSyncCacheDispatcher;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.util.DataSourceContextHolder;
import com.bx.erp.model.PosSyncCache;

public class PosSyncCacheDispatcherMapperTest extends BaseMapperTest {

	public static final int POS_ID1 = 1;

	public static class DataInput {
		private static PosSyncCacheDispatcher scd = new PosSyncCacheDispatcher();

		protected static final PosSyncCacheDispatcher getPosSyncCacheDispatcher() throws CloneNotSupportedException, InterruptedException {
			scd = new PosSyncCacheDispatcher();
			scd.setSyncCacheID(2);
			scd.setPos_ID(new Random().nextInt(5) + 1);
			return (PosSyncCacheDispatcher) scd.clone();
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

	@Test
	public void createTest() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		System.out.println("\n------------------------ Create PosSyncCacheDispatcher Test ------------------------");

		PosSyncCache ssc = new PosSyncCache();
		ssc.setSyncData_ID(2);
		ssc.setSyncSequence(1);// 本字段在这里无关紧要
		ssc.setSyncType(SyncCache.SYNC_Type_U);
		ssc.setPosID(POS_ID1);
		//
		Map<String, Object> createParam = ssc.getCreateParam(BaseBO.INVALID_CASE_ID, ssc);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<List<BaseModel>> sscCreate = posSyncCacheMapper.createEx(createParam);
		assertTrue(sscCreate != null && EnumErrorCode.values()[Integer.parseInt(createParam.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "创建对象成功");
		//
		ssc.setIgnoreIDInComparision(true);
		if (ssc.compareTo(sscCreate.get(0).get(0)) != 0) {
			assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}

		// 正常添加
		PosSyncCacheDispatcher scd = DataInput.getPosSyncCacheDispatcher();
		scd.setSyncCacheID(sscCreate.get(0).get(0).getID());

		Map<String, Object> paramsForCreate = scd.getCreateParam(BaseBO.INVALID_CASE_ID, scd);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		PosSyncCacheDispatcher scdCreate = (PosSyncCacheDispatcher) posSyncCacheDispatcherMapper.create(paramsForCreate); // ...
		scd.setIgnoreIDInComparision(true);
		if (scd.compareTo(scdCreate) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		Assert.assertEquals(EnumErrorCode.values()[Integer.parseInt(paramsForCreate.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())], EnumErrorCode.EC_NoError);
		// 重复添加
		Map<String, Object> paramsForCreate2 = scd.getCreateParam(BaseBO.INVALID_CASE_ID, scd);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		posSyncCacheDispatcherMapper.create(paramsForCreate2); // ...
		Assert.assertEquals(EnumErrorCode.values()[Integer.parseInt(paramsForCreate2.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())], EnumErrorCode.EC_NoError);
	}

	@Test
	public void retrieveNTest() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		System.out.println("\n------------------------ retrieveN PosSyncCacheDispatcher Test ------------------------");

		PosSyncCache ssc = new PosSyncCache();
		ssc.setSyncData_ID(2);
		ssc.setSyncSequence(1);// 本字段在这里无关紧要
		ssc.setSyncType(SyncCache.SYNC_Type_U);
		ssc.setPosID(POS_ID1);
		//
		Map<String, Object> createParam = ssc.getCreateParam(BaseBO.INVALID_CASE_ID, ssc);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<List<BaseModel>> sscCreate = posSyncCacheMapper.createEx(createParam);
		assertTrue(sscCreate != null && EnumErrorCode.values()[Integer.parseInt(createParam.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "创建对象成功");
		//
		ssc.setIgnoreIDInComparision(true);
		if (ssc.compareTo(sscCreate.get(0).get(0)) != 0) {
			assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}

		// 正常添加
		PosSyncCacheDispatcher scd = DataInput.getPosSyncCacheDispatcher();
		scd.setSyncCacheID(sscCreate.get(0).get(0).getID());

		Map<String, Object> paramsForCreate = scd.getCreateParam(BaseBO.INVALID_CASE_ID, scd);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		PosSyncCacheDispatcher scdCreate = (PosSyncCacheDispatcher) posSyncCacheDispatcherMapper.create(paramsForCreate); // ...
		scd.setIgnoreIDInComparision(true);
		if (scd.compareTo(scdCreate) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		Assert.assertEquals(EnumErrorCode.values()[Integer.parseInt(paramsForCreate.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())], EnumErrorCode.EC_NoError);

		PosSyncCacheDispatcher pcd = DataInput.getPosSyncCacheDispatcher();
		Map<String, Object> paramsForRetrieveN = pcd.getRetrieveNParam(BaseBO.INVALID_CASE_ID, pcd);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> scList = (List<BaseModel>) posSyncCacheDispatcherMapper.retrieveN(paramsForRetrieveN); // ...

		Assert.assertEquals(EnumErrorCode.values()[Integer.parseInt(paramsForRetrieveN.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())], EnumErrorCode.EC_NoError);
		Assert.assertTrue(scList.size() > 0, "返回0条数据");
	}
}
