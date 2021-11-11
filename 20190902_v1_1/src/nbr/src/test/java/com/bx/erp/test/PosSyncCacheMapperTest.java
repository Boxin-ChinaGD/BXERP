package com.bx.erp.test;

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
import com.bx.erp.model.PosSyncCache;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.util.DataSourceContextHolder;

public class PosSyncCacheMapperTest extends BaseMapperTest {

	/** ID为1的POS机 */
	public static final int POS_ID1 = 1;

	public static class DataInput {
		private static PosSyncCache sc = new PosSyncCache();

		protected static final PosSyncCache getPosSyncCache() throws Exception {
			sc = new PosSyncCache();
			sc.setSyncData_ID(new Random().nextInt(5) + 1);
			sc.setSyncSequence(1);// 本字段在这里无关紧要
			sc.setSyncType(SyncCache.SYNC_Type_U);
			sc.setPosID(POS_ID1);

			return (PosSyncCache) sc.clone();
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
	public void createTest() throws Exception {
		Shared.printTestMethodStartInfo();

		System.out.println("\n------------------------ Create PosSyncCache Test ------------------------");

		// 正常添加
		PosSyncCache sc = DataInput.getPosSyncCache();
		Map<String, Object> paramsForCreate = sc.getCreateParam(BaseBO.INVALID_CASE_ID, sc);

		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<List<BaseModel>> list = posSyncCacheMapper.createEx(paramsForCreate); // ...
		List<BaseModel> scList = (List<BaseModel>) list.get(0);
		PosSyncCache scCreate = (PosSyncCache) scList.get(0);
		sc.setIgnoreIDInComparision(true);
		if (sc.compareTo(scCreate) != 0 && !sc.getSyncType().equals(scCreate.getSyncType())) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		Assert.assertEquals(EnumErrorCode.values()[Integer.parseInt(paramsForCreate.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())], EnumErrorCode.EC_NoError);

		// 重复添加U型数据
		Map<String, Object> paramsForCreate2 = sc.getCreateParam(BaseBO.INVALID_CASE_ID, sc);

		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<List<BaseModel>> createExList = posSyncCacheMapper.createEx(paramsForCreate2); // ...
		PosSyncCache scCreate2 = (PosSyncCache) createExList.get(0).get(0);
		sc.setIgnoreIDInComparision(true);
		if (sc.compareTo(scCreate2) != 0 && !sc.getSyncType().equals(scCreate2.getSyncType())) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		Assert.assertEquals(EnumErrorCode.values()[Integer.parseInt(paramsForCreate2.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())], EnumErrorCode.EC_NoError);
	}

	@Test
	public void retrieveNTest() throws Exception {
		Shared.printTestMethodStartInfo();

		System.out.println("\n------------------------ retrieveN PosSyncCache Test ------------------------");

		PosSyncCache sc = DataInput.getPosSyncCache();
		Map<String, Object> paramsForCreate = sc.getCreateParam(BaseBO.INVALID_CASE_ID, sc);

		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<List<BaseModel>> list = posSyncCacheMapper.createEx(paramsForCreate); // ...
		List<BaseModel> scList = (List<BaseModel>) list.get(0);
		PosSyncCache scCreate = (PosSyncCache) scList.get(0);
		sc.setIgnoreIDInComparision(true);
		if (sc.compareTo(scCreate) != 0 && !sc.getSyncType().equals(scCreate.getSyncType())) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		Assert.assertEquals(EnumErrorCode.values()[Integer.parseInt(paramsForCreate.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())], EnumErrorCode.EC_NoError);

		Map<String, Object> paramsForRetrieveN = sc.getRetrieveNParam(BaseBO.INVALID_CASE_ID, sc);

		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> posSyncCachelist = (List<BaseModel>) posSyncCacheMapper.retrieveN(paramsForRetrieveN); // ...
		Assert.assertEquals(EnumErrorCode.values()[Integer.parseInt(paramsForRetrieveN.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())], EnumErrorCode.EC_NoError);

		boolean is = false;
		for (BaseModel baseModel : posSyncCachelist) {
			PosSyncCache p = (PosSyncCache) baseModel;
			if (p.getSyncType().equals(sc.getSyncType()) && p.getSyncData_ID() == sc.getSyncData_ID()) {
				is = true;
				Assert.assertTrue(true);
				break;
			}
		}

		if (!is) {
			Assert.assertTrue(false, "查询失败");
		}
	}

	@Test
	public void deleteTest() throws Exception {
		Shared.printTestMethodStartInfo();

		System.out.println("\n------------------------ Delete PosSyncCache Test ------------------------");

		// 正常添加
		PosSyncCache sc = DataInput.getPosSyncCache();
		Map<String, Object> paramsForCreate = sc.getCreateParam(BaseBO.INVALID_CASE_ID, sc);

		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<List<BaseModel>> list = posSyncCacheMapper.createEx(paramsForCreate); // ...

		List<BaseModel> scList = (List<BaseModel>) list.get(0);
		PosSyncCache scCreate = (PosSyncCache) scList.get(0);
		sc.setIgnoreIDInComparision(true);
		if (sc.compareTo(scCreate) != 0 && !sc.getSyncType().equals(scCreate.getSyncType())) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		Assert.assertEquals(EnumErrorCode.values()[Integer.parseInt(paramsForCreate.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())], EnumErrorCode.EC_NoError);

		Map<String, Object> paramsForDelete = scCreate.getDeleteParam(BaseBO.INVALID_CASE_ID, scCreate);

		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		posSyncCacheMapper.delete(paramsForDelete);

		Assert.assertEquals(EnumErrorCode.values()[Integer.parseInt(paramsForDelete.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())], EnumErrorCode.EC_BusinessLogicNotDefined);
	}

	@Test
	public void deleteNTest() throws Exception {
		Shared.printTestMethodStartInfo();

		System.out.println("\n------------------------ DeleteN PosSyncCache Test ------------------------");

		// 正常添加
		PosSyncCache sc = DataInput.getPosSyncCache();
		Map<String, Object> paramsForCreate = sc.getCreateParam(BaseBO.INVALID_CASE_ID, sc);

		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<List<BaseModel>> list = posSyncCacheMapper.createEx(paramsForCreate); // ...

		List<BaseModel> scList = (List<BaseModel>) list.get(0);
		PosSyncCache scCreate = (PosSyncCache) scList.get(0);
		sc.setIgnoreIDInComparision(true);
		if (sc.compareTo(scCreate) != 0 && !sc.getSyncType().equals(scCreate.getSyncType())) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		Assert.assertEquals(EnumErrorCode.values()[Integer.parseInt(paramsForCreate.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())], EnumErrorCode.EC_NoError);

		Map<String, Object> paramsForDelete = scCreate.getDeleteParam(BaseBO.CASE_X_DeleteAllPosSyncCache, scCreate);

		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		posSyncCacheMapper.deleteAll(paramsForDelete);

		Assert.assertEquals(EnumErrorCode.values()[Integer.parseInt(paramsForDelete.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())], EnumErrorCode.EC_NoError);

		Map<String, Object> paramsForRetrieveN = sc.getRetrieveNParam(BaseBO.INVALID_CASE_ID, sc);

		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> posSyncCachelist = (List<BaseModel>) posSyncCacheMapper.retrieveN(paramsForRetrieveN); // ...
		Assert.assertEquals(EnumErrorCode.values()[Integer.parseInt(paramsForRetrieveN.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())], EnumErrorCode.EC_NoError);
		Assert.assertTrue(posSyncCachelist.size() == 0, "表中还有数据，DeleteAll失败");
	}

}
