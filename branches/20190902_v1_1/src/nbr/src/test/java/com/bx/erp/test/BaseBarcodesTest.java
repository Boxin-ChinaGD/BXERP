package com.bx.erp.test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpSession;

import org.junit.AfterClass;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.BaseAction.EnumSession;
import com.bx.erp.action.bo.BarcodesSyncCacheBO;
import com.bx.erp.action.bo.BarcodesSyncCacheDispatcherBO;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.action.bo.PosBO;
import com.bx.erp.action.bo.commodity.CommodityHistoryBO;
import com.bx.erp.cache.CacheManager;
import com.bx.erp.cache.SyncCache;
import com.bx.erp.cache.SyncCacheManager;
import com.bx.erp.model.Barcodes;
import com.bx.erp.model.BarcodesSyncCache;
import com.bx.erp.model.BarcodesSyncCacheDispatcher;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.BaseSyncCache;
import com.bx.erp.model.BaseSyncCacheDispatcher;
import com.bx.erp.model.BaseModel.EnumBoolean;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.model.Pos;
import com.bx.erp.model.CacheType.EnumCacheType;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.SyncCacheType.EnumSyncCacheType;
import com.bx.erp.util.DataSourceContextHolder;

public class BaseBarcodesTest extends BaseMapperTest{
	@BeforeClass
	public void setup() {
	}

	@AfterClass
	public void tearDown() {
	}

	public static final int STAFF_ID3 = 3;

	public static class DataInput {
		private static Barcodes barcodesInput = null;
		private static BarcodesSyncCache barcodesSyncCacheInput = null;
		private static BarcodesSyncCacheDispatcher barcodesSyncCacheDispatcher = null;

		public static final Barcodes getBarcodes(int CommodityID) throws CloneNotSupportedException {
			barcodesInput = new Barcodes();
			barcodesInput.setCommodityID(CommodityID);
			barcodesInput.setBarcode("1234567" + System.currentTimeMillis() % 1000000);
			barcodesInput.setOperatorStaffID(STAFF_ID3); // staffID
			barcodesInput.setReturnObject(EnumBoolean.EB_Yes.getIndex());

			return (Barcodes) barcodesInput.clone();
		}
		
		public static final BarcodesSyncCache getBarcodesSyncCache(int SyncDataID, String syncType, int posID) throws CloneNotSupportedException {
			barcodesSyncCacheInput = new BarcodesSyncCache();
			barcodesSyncCacheInput.setSyncData_ID(SyncDataID);
			barcodesSyncCacheInput.setSyncSequence(1);// ??????????????????????????????
			barcodesSyncCacheInput.setSyncType(syncType);
			barcodesSyncCacheInput.setPosID(posID);

			return (BarcodesSyncCache) barcodesSyncCacheInput.clone();
		}
		
		public static final BarcodesSyncCacheDispatcher getBarcodesSyncCacheDispatcher() throws Exception {
			barcodesSyncCacheDispatcher = new BarcodesSyncCacheDispatcher();
			barcodesSyncCacheDispatcher.setSyncCacheID(new Random().nextInt(5) + 1);
			barcodesSyncCacheDispatcher.setPos_ID(new Random().nextInt(5) + 1);

			return (BarcodesSyncCacheDispatcher) barcodesSyncCacheDispatcher.clone();
		}
	}

	public static Barcodes createBarcodesViaMapper(Barcodes barcodes, int iUseCaseID) {
		Map<String, Object> params = barcodes.getCreateParam(BaseBO.INVALID_CASE_ID, barcodes);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Barcodes barcodesCreate = (Barcodes) barcodesMapper.create(params);
		Assert.assertTrue(barcodesCreate != null && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "?????????????????????param=" + params + "\n\r barcodes=" + barcodes);
		//
		barcodes.setIgnoreIDInComparision(true);
		if (barcodes.compareTo(barcodesCreate) != 0) {
			Assert.assertTrue(false, "???????????????????????????DB??????????????????");
		}
		barcodesCreate.setOperatorStaffID(barcodes.getOperatorStaffID()); // ...TODO ??????????????????????????????
		//
		String error1 = barcodesCreate.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error1, "");
		//
		barcodesCreate.setOperatorStaffID(0);// ??????????????????
		//
		BaseCommodityTest.retrieveNCommodityHistory(barcodes.getCommodityID(), barcodes.getOperatorStaffID(), commodityHistoryMapper);
		//
		return barcodesCreate;
	}
	
	public static BarcodesSyncCache createBarcodesSyncCacheViaMapper(BarcodesSyncCache barcodesSyncCache) {
		Map<String, Object> paramsForCreate = barcodesSyncCache.getCreateParam(BaseBO.INVALID_CASE_ID, barcodesSyncCache);

		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<List<BaseModel>> list = barcodesSyncCacheMapper.createEx(paramsForCreate); // ...
		
		Assert.assertEquals(EnumErrorCode.values()[Integer.parseInt(paramsForCreate.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())], EnumErrorCode.EC_NoError);
		
		BarcodesSyncCache bscCreate = null;
		//???POSID<=0???????????????DB????????????????????????????????????????????????????????????
		if(list.size() == 1) {
			bscCreate = (BarcodesSyncCache) list.get(0);
		} else {
			List<BaseModel> bscList = (List<BaseModel>) list.get(0);
			bscCreate = (BarcodesSyncCache) bscList.get(0);
		}
		//
		barcodesSyncCache.setIgnoreIDInComparision(true);
		if (barcodesSyncCache.compareTo(bscCreate) != 0) {
			Assert.assertTrue(false, "???????????????????????????DB??????????????????");
		}
		return bscCreate;
	}

	public static Barcodes updateBarcodesViaMapper(Barcodes barcodes, int iUseCaseID, ErrorInfo ecOut) {
		Map<String, Object> params = barcodes.getUpdateParam(BaseBO.INVALID_CASE_ID, barcodes);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Barcodes barcodesUpdate = (Barcodes) barcodesMapper.update(params);
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == ecOut.getErrorCode(), params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		//
		if (ecOut.getErrorCode() == EnumErrorCode.EC_NoError) {
			assertNotNull(barcodesUpdate);
			barcodes.setIgnoreIDInComparision(true);
			if (barcodes.compareTo(barcodesUpdate) != 0) {
				Assert.assertTrue(false, "???????????????????????????DB??????????????????");
			}
			//
			barcodesUpdate.setOperatorStaffID(barcodes.getOperatorStaffID()); // ...TODO ??????????????????????????????
			String error1 = barcodesUpdate.checkCreate(BaseBO.INVALID_CASE_ID);
			Assert.assertEquals(error1, "");
			//
			barcodesUpdate.setOperatorStaffID(0);// ?????????????????????
			//
			BaseCommodityTest.retrieveNCommodityHistory(barcodes.getCommodityID(), barcodes.getOperatorStaffID(), commodityHistoryMapper);
		}
		return barcodesUpdate;
	}

	public static List<BaseModel> retrieveNBarcodesViaMapper(Barcodes barcodes) {
		Map<String, Object> params = barcodes.getRetrieveNParam(BaseBO.INVALID_CASE_ID, barcodes);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> barcodesList = barcodesMapper.retrieveN(params);
		//
		Assert.assertTrue(barcodesList.size() > 0 && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		//
		for (BaseModel bm : barcodesList) {
			Barcodes b = (Barcodes) bm;
			b.setOperatorStaffID(1); // ...TODO ??????????????????????????????
			//
			String error = b.checkCreate(BaseBO.INVALID_CASE_ID);
			Assert.assertEquals(error, "");
			b.setOperatorStaffID(0); // ?????????
		}
		return barcodesList;
	}

	public static void deleteBarcodesViaMapper(Barcodes barcodes) {
		barcodes.setOperatorStaffID(STAFF_ID3);
		Map<String, Object> deleteParam = barcodes.getDeleteParam(BaseBO.INVALID_CASE_ID, barcodes);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		barcodesMapper.delete(deleteParam);
		//
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(deleteParam.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, deleteParam.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
	}

	public static void deleteBarcodesBySimpleCommodityIDViaMapper(Barcodes barcodes) {
		Map<String, Object> paramForDelete = barcodes.getDeleteParam(BaseBO.CASE_DeleteBarcodesByCommodityID, barcodes);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		barcodesMapper.deleteBySimpleCommodityID(paramForDelete);
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(paramForDelete.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, paramForDelete.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		//
		Map<String, Object> paramForRetrieveN = barcodes.getRetrieveNParam(BaseAction.INVALID_ID, barcodes);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> retrieveN = barcodesMapper.retrieveN(paramForRetrieveN);
		//
		Assert.assertTrue(retrieveN.size() == 0 && EnumErrorCode.values()[Integer.parseInt(paramForRetrieveN.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, paramForRetrieveN.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		//
		BaseCommodityTest.retrieveNCommodityHistory(barcodes.getCommodityID(), barcodes.getOperatorStaffID(), commodityHistoryMapper);
	}
	
	public static List<BaseModel> retrieveNBarcodesSyncCacheViaMapper(){
		Map<String, Object> param = new HashMap<String, Object>();
		
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> list = barcodesSyncCacheMapper.retrieveN(param);
		//
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(param.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, param.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());

		return list;
	}
	
	public static List<BaseModel> retrieveNBarcodesSyncCacheDispatherViaMapper(){
		Map<String, Object> param = new HashMap<String, Object>();
		
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> list = barcodesSyncCacheDispatcherMapper.retrieveN(param);
		//
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(param.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, param.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());

		return list;
	}
	
	public static void deleteBarcodesSyncCacheViaMapper(BarcodesSyncCache barcodesSyncCache) {
		Map<String, Object> paramsForDelete = barcodesSyncCache.getDeleteParam(BaseBO.INVALID_CASE_ID, barcodesSyncCache);

		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		barcodesSyncCacheMapper.delete(paramsForDelete);
		//
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(paramsForDelete.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, paramsForDelete.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
	}
	
	public static void deleteAllBarcodesSyncCacheViaMappe() {
		Map<String, Object> param = new HashMap<String, Object>();

		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		barcodesSyncCacheMapper.deleteAll(param);
		//
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(param.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, param.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
	}
	
	public static BarcodesSyncCacheDispatcher createBarcodesSyncCacheDispatcherViaMapper(BarcodesSyncCacheDispatcher barcodesSyncCacheDispatcher) {
		Map<String, Object> paramsForCreate2 = barcodesSyncCacheDispatcher.getCreateParam(BaseBO.INVALID_CASE_ID, barcodesSyncCacheDispatcher);

		String err = barcodesSyncCacheDispatcher.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertTrue("".equals(err), err);
		
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		BarcodesSyncCacheDispatcher scdCreate = (BarcodesSyncCacheDispatcher) barcodesSyncCacheDispatcherMapper.create(paramsForCreate2);
		
		Assert.assertEquals(EnumErrorCode.values()[Integer.parseInt(paramsForCreate2.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())], EnumErrorCode.EC_NoError);
		barcodesSyncCacheDispatcher.setIgnoreIDInComparision(true);
		if (barcodesSyncCacheDispatcher.compareTo(scdCreate) != 0) {
			Assert.assertTrue(false, "???????????????????????????DB??????????????????");
		}
		scdCreate.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertTrue("".equals(err), err);

		return scdCreate;
	}
	
	public static List<BaseModel> retrieveNBarcodesSyncCacheDispatcher(BarcodesSyncCacheDispatcher barcodesSyncCacheDispatcher){
		Map<String, Object> paramsForRetrieveN = barcodesSyncCacheDispatcher.getRetrieveNParam(BaseBO.INVALID_CASE_ID, barcodesSyncCacheDispatcher);
		
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> barcodesSyncCacheDispatcherList = (List<BaseModel>) barcodesSyncCacheDispatcherMapper.retrieveN(paramsForRetrieveN); // ...
		//
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(paramsForRetrieveN.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, paramsForRetrieveN.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());

		return barcodesSyncCacheDispatcherList;
	}

	public static Barcodes createBarcodesViaAction(Barcodes barcodes, MockMvc mvc, HttpSession session, Map<String, BaseBO> mapBO, String dbName) throws Exception {
		// ?????????????????????????????????????????????
		CommodityHistoryBO commodityHistoryBO = (CommodityHistoryBO) mapBO.get(CommodityHistoryBO.class.getSimpleName());
		int beforeCreateBarcodesCommodityHistorySize = BaseCommodityTest.queryCommodityHistorySize(barcodes.getCommodityID(), dbName, commodityHistoryBO);

		MvcResult mrl = mvc.perform(//
				post("/barcodesSync/createEx.bx") //
						.param(Barcodes.field.getFIELD_NAME_barcode(), barcodes.getBarcode())//
						.param(Barcodes.field.getFIELD_NAME_commodityID(), String.valueOf(barcodes.getCommodityID()))//
						.param(Barcodes.field.getFIELD_NAME_returnObject(), String.valueOf(barcodes.getReturnObject()))//
						.param(Barcodes.field.getFIELD_NAME_operatorStaffID(), String.valueOf(barcodes.getOperatorStaffID()))//
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) session)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		//
		Shared.checkJSONErrorCode(mrl);

		// ????????????
		Barcodes barcodesCreate = (Barcodes) Shared.parse1Object(mrl, barcodes, BaseAction.KEY_Object);
		Assert.assertTrue(barcodesCreate != null, "??????????????????");

		// ????????????????????????
		ErrorInfo ecOut = new ErrorInfo();
		Barcodes bm = (Barcodes) CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_Barcodes).read1(barcodesCreate.getID(), BaseBO.SYSTEM, ecOut, Shared.DBName_Test);
		Assert.assertTrue(bm != null, "????????????????????????????????????????????????");

		PosBO posBO = (PosBO) mapBO.get(PosBO.class.getSimpleName());
		BarcodesSyncCacheBO barcodesSyncCacheBO = (BarcodesSyncCacheBO) mapBO.get(BarcodesSyncCacheBO.class.getSimpleName());
		BarcodesSyncCacheDispatcherBO barcodesSyncCacheDispatcherBO = (BarcodesSyncCacheDispatcherBO) mapBO.get(BarcodesSyncCacheDispatcherBO.class.getSimpleName());

		Pos pos = (Pos) session.getAttribute(EnumSession.SESSION_POS.getName());
		int posID = pos.getID();
		BaseTestNGSpringContextTest.verifySyncCreateResult(mrl, barcodes, posBO, barcodesSyncCacheBO, barcodesSyncCacheDispatcherBO, EnumCacheType.ECT_Barcodes, EnumSyncCacheType.ESCT_BarcodesSyncInfo, posID, Shared.DBName_Test);

		// ??????????????????
		Assert.assertTrue(BaseCommodityTest.queryCommodityHistorySize(barcodes.getCommodityID(), dbName, commodityHistoryBO) == (beforeCreateBarcodesCommodityHistorySize + 1), "????????????????????????");

		return barcodesCreate;
	}

	public static void deleteBarcodesViaAction(Barcodes barcodes, MockMvc mvc, HttpSession session, Map<String, BaseBO> mapBO, String dbName) throws Exception {
		// ?????????????????????????????????????????????
		CommodityHistoryBO commodityHistoryBO = (CommodityHistoryBO) mapBO.get(CommodityHistoryBO.class.getSimpleName());
		int beforeDeleteBarcodesCommodityHistorySize = BaseCommodityTest.queryCommodityHistorySize(barcodes.getCommodityID(), dbName, commodityHistoryBO);

		MvcResult mrl = mvc.perform(//
				get("/barcodesSync/deleteEx.bx") //
						.param(Barcodes.field.getFIELD_NAME_ID(), String.valueOf(barcodes.getID()))//
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) session)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		//
		Shared.checkJSONErrorCode(mrl);

		// ??????????????????
		Assert.assertTrue(BaseCommodityTest.queryCommodityHistorySize(barcodes.getCommodityID(), dbName, commodityHistoryBO) == (beforeDeleteBarcodesCommodityHistorySize + 1), "????????????????????????");

		// ????????????????????????????????????????????????????????????
		ErrorInfo ecOut = new ErrorInfo();
		Barcodes bm = (Barcodes) CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_Barcodes).read1(barcodes.getID(), BaseBO.SYSTEM, ecOut, Shared.DBName_Test);
		Assert.assertTrue(bm == null, "?????????????????????????????????????????????");
		// ????????????????????????
		PosBO posBO = (PosBO) mapBO.get(PosBO.class.getSimpleName());
		BarcodesSyncCacheBO barcodesSyncCacheBO = (BarcodesSyncCacheBO) mapBO.get(BarcodesSyncCacheBO.class.getSimpleName());
		BarcodesSyncCacheDispatcherBO barcodesSyncCacheDispatcherBO = (BarcodesSyncCacheDispatcherBO) mapBO.get(BarcodesSyncCacheDispatcherBO.class.getSimpleName());

		Pos pos = (Pos) session.getAttribute(EnumSession.SESSION_POS.getName());
		int posID = pos.getID();
		BaseTestNGSpringContextTest.verifySyncDeleteSuccessResult(barcodes, posBO, barcodesSyncCacheBO, barcodesSyncCacheDispatcherBO, EnumSyncCacheType.ESCT_BarcodesSyncInfo, posID, Shared.DBName_Test);
	}

	public static Barcodes retrieve1BarcodesViaAction(Barcodes barcodes, MockMvc mvc, HttpSession session, Map<String, BaseBO> mapBO, String dbName) throws Exception {
		MvcResult mrl = mvc.perform(//
				post("/barcodes/retrieve1Ex.bx") //
						.param(Barcodes.field.getFIELD_NAME_ID(), String.valueOf(barcodes.getID()))//
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) session)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		//
		Shared.checkJSONErrorCode(mrl);
		return (Barcodes) Shared.parse1Object(mrl, barcodes, BaseAction.KEY_Object);
	}

	public static Barcodes updateBarcodesViaAction(Barcodes barcodes, MockMvc mvc, HttpSession session, Map<String, BaseBO> mapBO, String dbName) throws Exception {
		// ?????????????????????????????????????????????
		CommodityHistoryBO commodityHistoryBO = (CommodityHistoryBO) mapBO.get(CommodityHistoryBO.class.getSimpleName());
		int beforeUpdateBarcodesCommodityHistorySize = BaseCommodityTest.queryCommodityHistorySize(barcodes.getCommodityID(), dbName, commodityHistoryBO);

		MvcResult mr1 = mvc.perform(//
				post("/barcodesSync/updateEx.bx")//
						.param(Barcodes.field.getFIELD_NAME_ID(), String.valueOf(barcodes.getID()))//
						.param(Barcodes.field.getFIELD_NAME_commodityID(), String.valueOf(barcodes.getCommodityID()))//
						.param(Barcodes.field.getFIELD_NAME_barcode(), barcodes.getBarcode())//
						.param(Barcodes.field.getFIELD_NAME_returnObject(), String.valueOf(EnumBoolean.EB_Yes.getIndex()))//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr1);

		Barcodes barcodesCreate = (Barcodes) Shared.parse1Object(mr1, barcodes, BaseAction.KEY_Object);

		// ????????????????????????
		Assert.assertTrue(BaseCommodityTest.queryCommodityHistorySize(barcodes.getCommodityID(), dbName, commodityHistoryBO) == (beforeUpdateBarcodesCommodityHistorySize + 1), "????????????????????????");

		// ????????????????????????
		ErrorInfo ecOut = new ErrorInfo();
		Assert.assertNotNull(CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_Barcodes).read1(barcodesCreate.getID(), BaseBO.SYSTEM, ecOut, Shared.DBName_Test), "????????????????????????");

		// ??????????????????????????????????????????????????????????????????
		List<BaseModel> bmSyncCacheList = SyncCacheManager.getCache(dbName, EnumSyncCacheType.ESCT_BarcodesSyncInfo).readN(false, false);
		boolean existSync_Type_D = false;
		boolean existSync_Type_C = false;
		BaseSyncCache baseSyncCache = null;
		for (BaseModel bms : bmSyncCacheList) {
			baseSyncCache = (BaseSyncCache) bms;
			if (baseSyncCache.getSyncData_ID() == barcodesCreate.getID()) { // ????????????U????????????D???C,???????????????
				existSync_Type_C = true;
				continue;
			}
			
			if (baseSyncCache.getSyncData_ID() == (barcodesCreate.getID() - 1)) { //??????ID???1?????????????????????U??????????????????D??????C?????????????????????D??????????????????????????????????????????ID??????
				existSync_Type_D = true;
				continue;
			}
		}
		Assert.assertTrue(existSync_Type_C == true, "????????????????????????????????????c??????");
		Assert.assertTrue(existSync_Type_D == true, "????????????????????????????????????D??????");
		existSync_Type_C = false;
		existSync_Type_D = false;

		// ???????????????????????????DB???????????????????????????D?????????
		BarcodesSyncCacheBO barcodesSyncCacheBO = (BarcodesSyncCacheBO) mapBO.get(BarcodesSyncCacheBO.class.getSimpleName());
		BarcodesSyncCacheDispatcherBO barcodesSyncCacheDispatcherBO = (BarcodesSyncCacheDispatcherBO) mapBO.get(BarcodesSyncCacheDispatcherBO.class.getSimpleName());

		DataSourceContextHolder.setDbName(dbName);
		List<?> bcslist = barcodesSyncCacheBO.retrieveNObject(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, baseSyncCache);
		List<Integer> listSyncCacheID = new ArrayList<Integer>();
		for (Object obj2 : bcslist) {
			BaseSyncCache bsc = (BaseSyncCache) obj2;
			if (bsc.getSyncData_ID() == barcodesCreate.getID() && bsc.getSyncType().equals(SyncCache.SYNC_Type_C)) {
				listSyncCacheID.add(bsc.getID());
				existSync_Type_C = true;
				continue;
			}
			
			//??????ID???1?????????????????????U??????????????????D??????C?????????????????????D??????????????????????????????????????????ID??????
			if (bsc.getSyncData_ID() == (barcodesCreate.getID() - 1) && bsc.getSyncType().equals(SyncCache.SYNC_Type_D)) {
				listSyncCacheID.add(bsc.getID());
				existSync_Type_D = true;
				continue;
			}
		}
		Assert.assertTrue(existSync_Type_D == true, "????????????DB????????????????????????D??????");
		Assert.assertTrue(existSync_Type_C == true, "????????????DB????????????????????????C??????");
		existSync_Type_C = false;
		existSync_Type_D = false;
		
		//????????????????????????
		Pos pos = (Pos) session.getAttribute(EnumSession.SESSION_POS.getName());
		int posID = pos.getID();
		if (posID > 0) {
			boolean existSyncCacheDispatcher = false;
			DataSourceContextHolder.setDbName(dbName);
			List<?> bcsdlist = barcodesSyncCacheDispatcherBO.retrieveNObject(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, baseSyncCache);
			for (Object obj3 : bcsdlist) {
				BaseSyncCacheDispatcher bscd = (BaseSyncCacheDispatcher) obj3;
				if (listSyncCacheID.contains(bscd.getSyncCacheID())) {
					existSyncCacheDispatcher = true;
					break;
				}
			}
			Assert.assertTrue(existSyncCacheDispatcher == true, "??????????????????DB?????????????????????????????????");
		}

		return barcodesCreate;
	}
	
	
	public static Barcodes retrieveNBarcodes(int commodityID, String dbName) {
		Barcodes barcodes = new Barcodes();
		barcodes.setCommodityID(commodityID);
		barcodes.setBarcode("");
		barcodes.setOperatorStaffID(STAFF_ID3);
		Map<String, Object> params = barcodes.getRetrieveNParam(BaseBO.INVALID_CASE_ID, barcodes);
		//
		DataSourceContextHolder.setDbName(dbName);
		List<BaseModel> retrieveN = barcodesMapper.retrieveN(params);
		//
		assertTrue(retrieveN.size() > 0 && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError);
		//
		for (BaseModel bm : retrieveN) {
			Barcodes b = (Barcodes) bm;
			b.setOperatorStaffID(1); // ...??????????????????????????????
			//
			String error = b.checkCreate(BaseBO.INVALID_CASE_ID);
			Assert.assertEquals(error, "");
			b.setOperatorStaffID(0); // ?????????
		}
		Barcodes barcode = (Barcodes) retrieveN.get(0);
		return barcode;
	}
	
	public static Barcodes retrieve1ViaMapper(int barcodeID, String dbName) {
		Barcodes barcodes = new Barcodes();
		barcodes.setID(barcodeID);
		barcodes.setOperatorStaffID(STAFF_ID3);
		String err = barcodes.checkRetrieve1(BaseBO.INVALID_CASE_ID);
		Assert.assertTrue(err.equals(""), "checkRetrieve1???????????????");
		Map<String, Object> params = barcodes.getRetrieve1Param(BaseBO.INVALID_CASE_ID, barcodes);
		//
		DataSourceContextHolder.setDbName(dbName);
		Barcodes barcodesCreated = (Barcodes) barcodesMapper.retrieve1(params);
		assertTrue(EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError);
		assertTrue(barcodesCreated != null);
		return barcodesCreated;
	}
}
