package com.bx.erp.test.warehousing;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.testng.Assert.assertTrue;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import com.bx.erp.action.BaseAction;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.cache.CacheManager;
import com.bx.erp.cache.config.ConfigCacheSizeCache.EnumConfigCacheSizeCache;
import com.bx.erp.dao.purchasing.PurchasingOrderMapper;
import com.bx.erp.model.Barcodes;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.BaseModel.EnumBoolean;
import com.bx.erp.model.CacheType.EnumCacheType;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.model.Shop;
import com.bx.erp.model.Staff;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.Role.EnumTypeRole;
import com.bx.erp.model.commodity.Commodity;
import com.bx.erp.model.config.ConfigCacheSize;
import com.bx.erp.model.purchasing.Provider;
import com.bx.erp.model.purchasing.PurchasingOrder;
import com.bx.erp.model.purchasing.PurchasingOrderCommodity;
import com.bx.erp.model.warehousing.Warehousing;
import com.bx.erp.model.warehousing.Warehousing.EnumStatusWarehousing;
import com.bx.erp.model.warehousing.WarehousingCommodity;
import com.bx.erp.test.BaseActionTest;
import com.bx.erp.test.BaseCommodityTest;
import com.bx.erp.test.BaseStaffTest;
import com.bx.erp.test.BaseTestNGSpringContextTest;
import com.bx.erp.test.BaseWarehousingTest;
import com.bx.erp.test.Shared;
import com.bx.erp.test.checkPoint.StaffCP;
import com.bx.erp.test.checkPoint.WarehousingCP;
import com.bx.erp.test.staff.BaseShopTest;
import com.bx.erp.util.DataSourceContextHolder;
import com.bx.erp.util.FieldFormat;
import com.jayway.jsonpath.JsonPath;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Transactional
@WebAppConfiguration
public class WarehousingActionTest extends BaseActionTest {
	private static final int WS_STATUS1_ID = 4;
	private static final int WE_DEFAULT_ID = 1;
	private final String commIDs = "commIDs";
	private final String commNOs = "commNOs";
	private final String commPrices = "commPrices";
	private final String amounts = "amounts";
	private final String barcodeIDs = "barcodeIDs";

	@BeforeClass
	public void setup() {
		super.setUp();
		
		// ?????????????????????????????????????????????????????????????????????????????????????????????????????????tear down???????????????
		ConfigCacheSize ccs = new ConfigCacheSize();
		ccs.setValue(String.valueOf(BaseAction.PAGE_SIZE_Infinite));
		ccs.setID(EnumConfigCacheSizeCache.ECC_CommodityCacheSize.getIndex());
		ccs.setName(EnumConfigCacheSizeCache.ECC_CommodityCacheSize.getName());
		CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_ConfigCacheSize).write1(ccs, Shared.DBName_Test, STAFF_ID4);
		// ???????????????????????????,???????????????????????????
		Commodity commodity = new Commodity();
		commodity.setID(1);
		CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_Commodity).delete1(commodity);
		commodity.setID(10);
		CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_Commodity).delete1(commodity);
	}

	@AfterClass
	public void tearDown() {
		Shared.printTestClassEndInfo();
		// ????????????????????????????????????????????????setup?????????????????????????????????????????????????????????????????????
		ConfigCacheSize ccs = new ConfigCacheSize();
		ccs.setValue("100");
		ccs.setID(EnumConfigCacheSizeCache.ECC_CommodityCacheSize.getIndex());
		ccs.setName(EnumConfigCacheSizeCache.ECC_CommodityCacheSize.getName());
		CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_ConfigCacheSize).write1(ccs, Shared.DBName_Test, STAFF_ID4);
	}

	public Warehousing createWarehousing(Warehousing warehousing) throws CloneNotSupportedException, InterruptedException {
		warehousing.setProviderID(1);
		String error = warehousing.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertTrue(error == "", "????????????checkCreate?????????");
		Map<String, Object> paramsForCreate = warehousing.getCreateParam(BaseBO.INVALID_CASE_ID, warehousing);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Warehousing wsCreate = (Warehousing) warehousingMapper.create(paramsForCreate); // ...
		//
		warehousing.setIgnoreIDInComparision(true);
		if (warehousing.compareTo(wsCreate) != 0) {
			Assert.assertTrue(false, "???????????????????????????DB??????????????????");
		}
		assertTrue(wsCreate != null && EnumErrorCode.values()[Integer.parseInt(paramsForCreate.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "??????????????????");

		return (Warehousing) wsCreate.clone();
	}

	public WarehousingCommodity createWarehousingCommodity(int warehousingID, Commodity commodity, int barcodeID) throws CloneNotSupportedException, InterruptedException {
		WarehousingCommodity wc = new WarehousingCommodity();
		wc.setWarehousingID(warehousingID);
		wc.setCommodityID(commodity.getID());
		wc.setPackageUnitID(commodity.getPackageUnitID());
		wc.setNO(commodity.getNO());
		wc.setNoSalable(wc.getNO());
		wc.setBarcodeID(barcodeID);
		wc.setPrice(commodity.getPriceRetail());
		wc.setAmount(wc.getNO() * wc.getPrice());
		wc.setShelfLife(commodity.getShelfLife());
		//
		String errorMessage = wc.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(errorMessage, "");
		//
		Map<String, Object> paramsForCreate = wc.getCreateParam(BaseBO.INVALID_CASE_ID, wc);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		WarehousingCommodity wcCreate = (WarehousingCommodity) warehousingCommodityMapper.create(paramsForCreate); // ...
		//
		assertTrue(wcCreate != null && EnumErrorCode.values()[Integer.parseInt(paramsForCreate.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError,
				paramsForCreate.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		//
		wc.setIgnoreIDInComparision(true);
		if (wc.compareTo(wcCreate) != 0) {
			Assert.assertTrue(false, "???????????????????????????DB??????????????????");
		}

		return (WarehousingCommodity) wcCreate.clone();
	}

	public PurchasingOrder createPurchasingOrder() throws CloneNotSupportedException, InterruptedException {

		System.out.println("--------------------------------???????????????---------------------------------");
		PurchasingOrder po = new PurchasingOrder();
		po.setStaffID(Shared.BossID);
		po.setProviderID(1);
		po.setRemark("Remark:" + String.valueOf(System.currentTimeMillis()).substring(6));
		po.setShopID(Shared.DEFAULT_Shop_ID);
		Thread.sleep(1);
		String error = po.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertTrue(error == "", "???????????????checkCreate?????????");
		Map<String, Object> paramsForCreate = po.getCreateParam(BaseBO.INVALID_CASE_ID, po);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		PurchasingOrder poCreate = (PurchasingOrder) purchasingOrderMapper.create(paramsForCreate);
		//
		assertTrue(poCreate != null && EnumErrorCode.values()[Integer.parseInt(paramsForCreate.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError,
				paramsForCreate.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		//
		po.setIgnoreIDInComparision(true);
		if (po.compareTo(poCreate) != 0) {
			Assert.assertTrue(false, "???????????????????????????DB??????????????????");
		}

		System.out.println("--------------------------------?????????????????????---------------------------------");

		PurchasingOrderCommodity poc1 = new PurchasingOrderCommodity();
		poc1.setCommodityID(1);
		poc1.setCommodityNO(20);
		poc1.setPurchasingOrderID(poCreate.getID());
		poc1.setPriceSuggestion(1);
		poc1.setBarcodeID(1);
		error = poc1.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertTrue(error == "", "???????????????checkCreate?????????");
		Map<String, Object> params = poc1.getCreateParam(BaseBO.INVALID_CASE_ID, poc1);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		PurchasingOrderCommodity pocCreate = (PurchasingOrderCommodity) purchasingOrderCommodityMapper.create(params);
		//
		poc1.setIgnoreIDInComparision(true);
		if (poc1.compareTo(pocCreate) != 0) {
			Assert.assertTrue(false, "???????????????????????????DB??????????????????");
		}
		Assert.assertTrue(pocCreate != null && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "??????????????????");

		System.out.println("--------------------------------???????????????---------------------------------");

		poCreate.setApproverID(poCreate.getStaffID());
		Map<String, Object> params2 = poCreate.getUpdateParam(BaseBO.CASE_ApprovePurchasingOrder, poCreate);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		PurchasingOrder approvePo = (PurchasingOrder) purchasingOrderMapper.approve(params2);
		Assert.assertTrue(approvePo != null && EnumErrorCode.values()[Integer.parseInt(params2.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, params2.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString());

		return (PurchasingOrder) poCreate.clone();
	}

	@Test
	public void testIndex() throws Exception {
		Shared.printTestMethodStartInfo();

		mvc.perform(get("/warehousing.bx").session(sessionBoss) //
		).andExpect(status().isOk()).andReturn();
	}

	@Test
	public void testReturnPurchasingCommodity() throws Exception {
		Shared.printTestMethodStartInfo();

		mvc.perform(get("/warehousing/returnPurchasingCommodity.bx").session(sessionBoss) //
		).andExpect(status().isOk()).andReturn();
	}

	private HttpSession getLoginSession() throws Exception {
		// Warehousing w = BaseWarehousingTest.DataInput.getWarehousing();
		MvcResult result = (MvcResult) this.mvc.perform( //
				get("/warehousing/retrieve1Ex.bx?" + Warehousing.field.getFIELD_NAME_ID() + "=1") //
						.param(Warehousing.field.getFIELD_NAME_purchasingOrderID(), "7") //
						.session(sessionBoss) //
		) //
				.andExpect(status().isOk()) //
				.andReturn();
		return result.getRequest().getSession();
	}

	@Test
	public void testRetrieveNEx1() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("CASE1:????????????????????????,RN????????????????????????warehousing?????????warehousingcommodity???listCommodity????????????????????????????????????R1?????????????????????????????????\"");
		Commodity commodity = BaseCommodityTest.DataInput.getCommodity();
		commodity.setnOStart(20);
		commodity.setPurchasingPriceStart(10);
		commodity.setNO(commodity.getnOStart());
		List<List<BaseModel>> listBM = BaseCommodityTest.createCommodityViaMapper(BaseBO.CASE_Commodity_CreateSingle, commodity);
		Warehousing createWarehousing = (Warehousing) listBM.get(3).get(0);
		MvcResult mr = mvc.perform( //
				get("/warehousing/retrieveNEx.bx?" + Warehousing.field.getFIELD_NAME_ID() + "=" + BaseAction.INVALID_ID + "&" //
						+ Warehousing.field.getFIELD_NAME_warehouseID() + "=" + BaseAction.INVALID_ID + "&" //
						+ Warehousing.field.getFIELD_NAME_staffID() + "=" + BaseAction.INVALID_ID + "&" //
						+ Warehousing.field.getFIELD_NAME_purchasingOrderID() + "=" + BaseAction.INVALID_ID + "&" //
				) //
						.contentType(MediaType.APPLICATION_JSON) //
						.session(sessionBoss)) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn(); //
		Shared.checkJSONErrorCode(mr);
		//
		JSONObject o = JSONObject.fromObject(mr.getResponse().getContentAsString());
		//
		List<Integer> list = JsonPath.read(o, "$.warehousingList[*].ID");
		List<List<WarehousingCommodity>> whCommList = JsonPath.read(o, "$.warehousingList[*].listSlave1");
		Assert.assertTrue(whCommList.size() > 0, "?????????warehousing??????????????????");// size>0???????????????warehousing???????????????
		for (List<WarehousingCommodity> ls : whCommList) {
			Assert.assertTrue(ls.size() == 0, "warehousing????????????????????????RN?????????????????????????????????");
		}
		List<List<Commodity>> commList = JsonPath.read(o, "$.warehousingList[*].listCommodity");
		for (List<Commodity> ls2 : commList) {
			Assert.assertTrue(ls2.size() == 0, "warehousing?????????listCommodity?????????RN?????????????????????????????????");
		}
		Assert.assertTrue(createWarehousing.getID() == list.get(0));
	}

	@Test
	public void testRetrieveNEx2() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("CASE2:??????ID??????");
		MvcResult mr2 = mvc.perform( //
				get("/warehousing/retrieveNEx.bx?" + Warehousing.field.getFIELD_NAME_ID() + "=1&" //
						+ Warehousing.field.getFIELD_NAME_warehouseID() + "=" + BaseAction.INVALID_ID + "&" //
						+ Warehousing.field.getFIELD_NAME_staffID() + "=" + BaseAction.INVALID_ID + "&" //
						+ Warehousing.field.getFIELD_NAME_purchasingOrderID() + "=" + BaseAction.INVALID_ID + "&" //
				) //
						.contentType(MediaType.APPLICATION_JSON) //
						.session(sessionBoss)) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn(); //
		Shared.checkJSONErrorCode(mr2);
		//
		JSONObject o1 = JSONObject.fromObject(mr2.getResponse().getContentAsString()); //
		List<Integer> list1 = JsonPath.read(o1, "$.warehousingList[*].ID");
		for (int i = 0; i < list1.size(); i++) {
			assertTrue(list1.get(i) == 1);
		}
	}

	@Test
	public void testRetrieveNEx3() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("CASE3:????????????ID??????");
		MvcResult mr3 = mvc.perform( //
				get("/warehousing/retrieveNEx.bx?" + Warehousing.field.getFIELD_NAME_ID() + "=" + BaseAction.INVALID_ID + "&" //
						+ Warehousing.field.getFIELD_NAME_warehouseID() + "=1&" //
						+ Warehousing.field.getFIELD_NAME_staffID() + "=" + BaseAction.INVALID_ID + "&" //
						+ Warehousing.field.getFIELD_NAME_purchasingOrderID() + "=" + BaseAction.INVALID_ID + "&" //
				) //
						.contentType(MediaType.APPLICATION_JSON) //
						.session(sessionBoss)) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn(); //
		Shared.checkJSONErrorCode(mr3);
		//
		JSONObject o2 = JSONObject.fromObject(mr3.getResponse().getContentAsString()); //
		List<Integer> list3 = JsonPath.read(o2, "$.warehousingList[*].warehouseID");
		for (int i = 0; i < list3.size(); i++) {
			assertTrue(list3.get(i) == 1);
		}
	}

	@Test
	public void testRetrieveNEx4() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("CASE4:???????????????ID???");
		MvcResult mr4 = mvc.perform( //
				get("/warehousing/retrieveNEx.bx?" + Warehousing.field.getFIELD_NAME_ID() + "=" + BaseAction.INVALID_ID + "&" //
						+ Warehousing.field.getFIELD_NAME_warehouseID() + "=" + BaseAction.INVALID_ID + "&" //
						+ Warehousing.field.getFIELD_NAME_staffID() + "=1&" //
						+ Warehousing.field.getFIELD_NAME_purchasingOrderID() + "=" + BaseAction.INVALID_ID + "&" //
				) //
						.contentType(MediaType.APPLICATION_JSON) //
						.session(sessionBoss)) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn(); //
		Shared.checkJSONErrorCode(mr4);
		//
		JSONObject o3 = JSONObject.fromObject(mr4.getResponse().getContentAsString()); //
		List<Integer> list4 = JsonPath.read(o3, "$.warehousingList[*].staffID");
		for (int i = 0; i < list4.size(); i++) {
			assertTrue(list4.get(i) == 1);
		}
	}

	@Test
	public void testRetrieveNEx5() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("CASE5:???????????????ID???");
		MvcResult mr5 = mvc.perform( //
				get("/warehousing/retrieveNEx.bx?" + Warehousing.field.getFIELD_NAME_ID() + "=" + BaseAction.INVALID_ID + "&" //
						+ Warehousing.field.getFIELD_NAME_warehouseID() + "=" + BaseAction.INVALID_ID + "&" //
						+ Warehousing.field.getFIELD_NAME_staffID() + "=" + BaseAction.INVALID_ID + "&" //
						+ Warehousing.field.getFIELD_NAME_purchasingOrderID() + "=1" //
				) //
						.contentType(MediaType.APPLICATION_JSON) //
						.session(sessionBoss)) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn(); //
		Shared.checkJSONErrorCode(mr5);
		//
		JSONObject o4 = JSONObject.fromObject(mr5.getResponse().getContentAsString()); //
		List<Integer> list5 = JsonPath.read(o4, "$.warehousingList[*].purchasingOrderID");
		for (int i = 0; i < list5.size(); i++) {
			assertTrue(list5.get(i) == 1);
		}
	}

	@Test
	public void testRetrieveNEx6() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("CASE6:??????????????????ID?????????ID???");
		MvcResult mr6 = mvc.perform( //
				get("/warehousing/retrieveNEx.bx?" + Warehousing.field.getFIELD_NAME_ID() + "=" + BaseAction.INVALID_ID + "&" //
						+ Warehousing.field.getFIELD_NAME_warehouseID() + "=1&" //
						+ Warehousing.field.getFIELD_NAME_staffID() + "=" + BaseAction.INVALID_ID + "&" //
						+ Warehousing.field.getFIELD_NAME_purchasingOrderID() + "=1" //
				) //
						.contentType(MediaType.APPLICATION_JSON) //
						.session(sessionBoss)) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn(); //
		Shared.checkJSONErrorCode(mr6);
		//
		JSONObject o5 = JSONObject.fromObject(mr6.getResponse().getContentAsString()); //
		List<Integer> list6 = JsonPath.read(o5, "$.warehousingList[*].purchasingOrderID");
		List<Integer> list7 = JsonPath.read(o5, "$.warehousingList[*].warehouseID");
		assertTrue(list6.size() == list7.size());
		for (int i = 0; i < list6.size(); i++) {
			assertTrue(list6.get(i) == 1);
			assertTrue(list7.get(i) == 1);
		}
	}

	@Test
	public void testRetrieveNEx7() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("CASE7:????????????????????????ID???");
		MvcResult mr7 = mvc.perform( //
				get("/warehousing/retrieveNEx.bx?" + Warehousing.field.getFIELD_NAME_ID() + "=" + BaseAction.INVALID_ID + "&" //
						+ Warehousing.field.getFIELD_NAME_warehouseID() + "=1&" //
						+ Warehousing.field.getFIELD_NAME_staffID() + "=1&" //
						+ Warehousing.field.getFIELD_NAME_purchasingOrderID() + "=" + BaseAction.INVALID_ID + "&" //
				) //
						.contentType(MediaType.APPLICATION_JSON) //
						.session(sessionBoss)) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn(); //
		Shared.checkJSONErrorCode(mr7);
		//
		JSONObject o6 = JSONObject.fromObject(mr7.getResponse().getContentAsString()); //
		List<Integer> list8 = JsonPath.read(o6, "$.warehousingList[*].staffID");
		List<Integer> list9 = JsonPath.read(o6, "$.warehousingList[*].warehouseID");
		assertTrue(list8.size() == list9.size());
		for (int i = 0; i < list8.size(); i++) {
			assertTrue(list8.get(i) == 1);
			assertTrue(list9.get(i) == 1);
		}
	}

	@Test
	public void testRetrieveNEx8() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("CASE8:?????????????????????ID???");
		MvcResult mr8 = mvc.perform( //
				get("/warehousing/retrieveNEx.bx?" + Warehousing.field.getFIELD_NAME_ID() + "=1&" //
						+ Warehousing.field.getFIELD_NAME_warehouseID() + "=" + BaseAction.INVALID_ID + "&" //
						+ Warehousing.field.getFIELD_NAME_staffID() + "=-1&" //
						+ Warehousing.field.getFIELD_NAME_purchasingOrderID() + "=1" //
				) //
						.contentType(MediaType.APPLICATION_JSON) //
						.session(sessionBoss)) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn(); //
		Shared.checkJSONErrorCode(mr8);
		//
		JSONObject o7 = JSONObject.fromObject(mr8.getResponse().getContentAsString()); //
		List<Integer> list10 = JsonPath.read(o7, "$.warehousingList[*].ID");
		List<Integer> list11 = JsonPath.read(o7, "$.warehousingList[*].purchasingOrderID");
		assertTrue(list10.size() == list11.size());
		for (int i = 0; i < list10.size(); i++) {
			assertTrue(list10.get(i) == 1);
			assertTrue(list11.get(i) == 1);
		}
	}

	@Test
	public void testRetrieveNEx9() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("CASE9:??????????????????ID?????????ID????????????ID???");
		MvcResult mr9 = mvc.perform( //
				get("/warehousing/retrieveNEx.bx?" + Warehousing.field.getFIELD_NAME_ID() + "=" + BaseAction.INVALID_ID + "&" //
						+ Warehousing.field.getFIELD_NAME_warehouseID() + "=1&" //
						+ Warehousing.field.getFIELD_NAME_staffID() + "=1&" //
						+ Warehousing.field.getFIELD_NAME_purchasingOrderID() + "=10" //
				) //
						.contentType(MediaType.APPLICATION_JSON) //
						.session(sessionBoss)) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn(); //
		Shared.checkJSONErrorCode(mr9);
		//
		JSONObject o8 = JSONObject.fromObject(mr9.getResponse().getContentAsString()); //
		List<Integer> list12 = JsonPath.read(o8, "$.warehousingList[*].staffID");
		List<Integer> list13 = JsonPath.read(o8, "$.warehousingList[*].purchasingOrderID");
		List<Integer> list14 = JsonPath.read(o8, "$.warehousingList[*].warehouseID");
		assertTrue(list12.size() == list13.size() && list12.size() == list14.size());
		for (int i = 0; i < list12.size(); i++) {
			assertTrue(list12.get(i) == 1);
			assertTrue(list13.get(i) == 10);
			assertTrue(list14.get(i) == 1);
		}
	}

	@Test
	public void testRetrieveNEx10() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("CASE10:??????????????????????????????");
		MvcResult mr10 = mvc.perform( //
				get("/warehousing/retrieveNEx.bx?" + Warehousing.field.getFIELD_NAME_ID() + "=1&" //
						+ Warehousing.field.getFIELD_NAME_warehouseID() + "=1&" //
						+ Warehousing.field.getFIELD_NAME_staffID() + "=3&" //
						+ Warehousing.field.getFIELD_NAME_purchasingOrderID() + "=1" //
				) //
						.contentType(MediaType.APPLICATION_JSON) //
						.session(sessionBoss)) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn(); //
		Shared.checkJSONErrorCode(mr10);
		//
		JSONObject o9 = JSONObject.fromObject(mr10.getResponse().getContentAsString()); //
		List<Integer> list15 = JsonPath.read(o9, "$.warehousingList[*].staffID");
		List<Integer> list16 = JsonPath.read(o9, "$.warehousingList[*].purchasingOrderID");
		List<Integer> list17 = JsonPath.read(o9, "$.warehousingList[*].warehouseID");
		List<Integer> list18 = JsonPath.read(o9, "$.warehousingList[*].ID");
		assertTrue(list15.size() == list16.size() && list15.size() == list17.size() && list15.size() == list18.size());
		for (int i = 0; i < list15.size(); i++) {
			assertTrue(list15.get(i) == 3);
			assertTrue(list16.get(i) == 1);
			assertTrue(list17.get(i) == 1);
			assertTrue(list18.get(i) == 1);
		}
	}

	@Test
	public void testRetrieveNEx11() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("CASE11:??????????????????");
//		MvcResult mr11 = mvc.perform( //
//				get("/warehousing/retrieveNEx.bx?" + Warehousing.field.getFIELD_NAME_ID() + "=1&" //
//						+ Warehousing.field.getFIELD_NAME_warehouseID() + "=1&" //
//						+ Warehousing.field.getFIELD_NAME_staffID() + "=3&" //
//						+ Warehousing.field.getFIELD_NAME_purchasingOrderID() + "=1" //
//				) //
//						.contentType(MediaType.APPLICATION_JSON) //
//						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfManager))) //
//				.andExpect(status().isOk()) //
//				.andDo(print()) //
//				.andReturn(); //
//		Shared.checkJSONErrorCode(mr11, EnumErrorCode.EC_NoPermission);
	}

	@Test
	public void testRetrieveNEx12() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("CASE12:??????????????????ID??????");
		MvcResult mr12 = mvc.perform( //
				get("/warehousing/retrieveNEx.bx?" + Warehousing.field.getFIELD_NAME_ID() + "=" + BaseWarehousingTest.INVALID_ID + "&" //
						+ Warehousing.field.getFIELD_NAME_warehouseID() + "=" + BaseAction.INVALID_ID + "&" //
						+ Warehousing.field.getFIELD_NAME_staffID() + "=" + BaseAction.INVALID_ID + "&" //
						+ Warehousing.field.getFIELD_NAME_purchasingOrderID() + "=" + BaseAction.INVALID_ID + "&" //
				) //
						.contentType(MediaType.APPLICATION_JSON) //
						.session(sessionBoss)) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn(); //
		Shared.checkJSONErrorCode(mr12);

		JSONObject o10 = JSONObject.fromObject(mr12.getResponse().getContentAsString()); //
		List<?> list19 = JsonPath.read(o10, "$.warehousingList[*]");
		assertTrue(list19.size() == 0);
	}

	@Test
	public void testRetrieveNEx13() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("CASE13:??????????????????staffID??????");
		MvcResult mr13 = mvc.perform( //
				get("/warehousing/retrieveNEx.bx?" + Warehousing.field.getFIELD_NAME_ID() + "=" + BaseAction.INVALID_ID + "&" //
						+ Warehousing.field.getFIELD_NAME_warehouseID() + "=" + BaseAction.INVALID_ID + "&" //
						+ Warehousing.field.getFIELD_NAME_staffID() + "=" + BaseWarehousingTest.INVALID_ID + "&" //
						+ Warehousing.field.getFIELD_NAME_purchasingOrderID() + "=" + BaseAction.INVALID_ID + "&" //
				) //
						.contentType(MediaType.APPLICATION_JSON) //
						.session(sessionBoss)) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn(); //
		Shared.checkJSONErrorCode(mr13);

		JSONObject o11 = JSONObject.fromObject(mr13.getResponse().getContentAsString()); //
		List<?> list20 = JsonPath.read(o11, "$.warehousingList[*]");
		assertTrue(list20.size() == 0);
	}

	@Test
	public void testRetrieveNEx14() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("CASE14:??????????????????warehouseID??????");
		MvcResult mr14 = mvc.perform( //
				get("/warehousing/retrieveNEx.bx?" + Warehousing.field.getFIELD_NAME_ID() + "=" + BaseAction.INVALID_ID + "&" //
						+ Warehousing.field.getFIELD_NAME_warehouseID() + "=" + BaseWarehousingTest.INVALID_ID + "&" //
						+ Warehousing.field.getFIELD_NAME_staffID() + "=" + BaseAction.INVALID_ID + "&" //
						+ Warehousing.field.getFIELD_NAME_purchasingOrderID() + "=" + BaseAction.INVALID_ID + "&" //
				) //
						.contentType(MediaType.APPLICATION_JSON) //
						.session(sessionBoss)) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn(); //
		Shared.checkJSONErrorCode(mr14);

		JSONObject o12 = JSONObject.fromObject(mr14.getResponse().getContentAsString()); //
		List<?> list21 = JsonPath.read(o12, "$.warehousingList[*]");
		assertTrue(list21.size() == 0);
	}

	@Test
	public void testRetrieveNEx15() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("CASE15:??????????????????purchasingOrderID??????");
		MvcResult mr15 = mvc.perform( //
				get("/warehousing/retrieveNEx.bx?" + Warehousing.field.getFIELD_NAME_ID() + "=" + BaseAction.INVALID_ID + "&" //
						+ Warehousing.field.getFIELD_NAME_warehouseID() + "=" + BaseAction.INVALID_ID + "&" //
						+ Warehousing.field.getFIELD_NAME_staffID() + "=" + BaseAction.INVALID_ID + "&" //
						+ Warehousing.field.getFIELD_NAME_purchasingOrderID() + "=" + BaseWarehousingTest.INVALID_ID //
				) //
						.contentType(MediaType.APPLICATION_JSON) //
						.session(sessionBoss)) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn(); //
		Shared.checkJSONErrorCode(mr15);

		JSONObject o13 = JSONObject.fromObject(mr15.getResponse().getContentAsString()); //
		List<?> list22 = JsonPath.read(o13, "$.warehousingList[*]");
		assertTrue(list22.size() == 0);
	}

	@Test
	public void testRetrieveNEx16() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("CASE16:???????????????ID??????????????????");
		MvcResult mr16 = mvc.perform( //
				get("/warehousing/retrieveNEx.bx?" + Warehousing.field.getFIELD_NAME_ID() + "=-1&" //
						+ Warehousing.field.getFIELD_NAME_warehouseID() + "=-1&" //
						+ Warehousing.field.getFIELD_NAME_staffID() + "=-1&" //
						+ Warehousing.field.getFIELD_NAME_purchasingOrderID() + "=1&" //
						+ Warehousing.field.getFIELD_NAME_providerID() + "=1" //
				) //
						.contentType(MediaType.APPLICATION_JSON) //
						.session(sessionBoss)) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn(); //
		Shared.checkJSONErrorCode(mr16);

		JSONObject o13 = JSONObject.fromObject(mr16.getResponse().getContentAsString()); //
		List<Integer> list22 = JsonPath.read(o13, "$.warehousingList[*].purchasingOrderID");
		List<Integer> list23 = JsonPath.read(o13, "$.warehousingList[*].providerID");
		assertTrue(list22.size() == list23.size());
		for (int i = 0; i < list22.size(); i++) {
			assertTrue(list22.get(i) == 1 && list23.get(i) == 1);
		}
	}

	@Test
	public void testRetrieveNEx17() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("CASE17:???????????????????????????ID???");

		MvcResult mr17 = mvc.perform( //
				get("/warehousing/retrieveNEx.bx?" + Warehousing.field.getFIELD_NAME_ID() + "=" + BaseAction.INVALID_ID + "&" //
						+ Warehousing.field.getFIELD_NAME_warehouseID() + "=" + BaseAction.INVALID_ID + "&" //
						+ Warehousing.field.getFIELD_NAME_staffID() + "=" + BaseAction.INVALID_ID + "&" //
						+ Warehousing.field.getFIELD_NAME_purchasingOrderID() + "=" + BaseAction.INVALID_ID + "&" //
						+ Warehousing.field.getFIELD_NAME_providerID() + "=" + BaseWarehousingTest.INVALID_ID //
				) //
						.contentType(MediaType.APPLICATION_JSON) //
						.session(sessionBoss)) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn(); //
		Shared.checkJSONErrorCode(mr17);

		JSONObject o13 = JSONObject.fromObject(mr17.getResponse().getContentAsString()); //
		List<?> list22 = JsonPath.read(o13, "$.warehousingList[*]");
		assertTrue(list22.size() == 0);
	}

	private void deleteWS(Warehousing wh) {
		Map<String, Object> paramsForDelete = wh.getDeleteParam(BaseBO.INVALID_CASE_ID, wh);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		warehousingMapper.delete(paramsForDelete);
		//
		Map<String, Object> paramsForRetrieve1 = wh.getRetrieve1ParamEx(BaseBO.INVALID_CASE_ID, wh);
		List<List<BaseModel>> wcR1List = (List<List<BaseModel>>) warehousingMapper.retrieve1Ex(paramsForRetrieve1);
		//
		assertTrue(wcR1List.get(0).size() == 0 && EnumErrorCode.values()[Integer.parseInt(paramsForRetrieve1.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError,
				paramsForRetrieve1.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
	}

	private void deleteWC(WarehousingCommodity wc) {

		Map<String, Object> paramsForDelete = wc.getDeleteParam(BaseBO.INVALID_CASE_ID, wc);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		warehousingCommodityMapper.delete(paramsForDelete);

		Map<String, Object> paramsForRetrieveN = wc.getRetrieveNParam(BaseBO.INVALID_CASE_ID, wc);
		List<BaseModel> wcR1List = (List<BaseModel>) warehousingMapper.retrieveN(paramsForRetrieveN);

		assertTrue(wcR1List.size() == 0 && EnumErrorCode.values()[Integer.parseInt(paramsForRetrieveN.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "??????????????????");
	}

	@Test
	public void testRetrieve1Ex1() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("case1:??????ID??????");
		MvcResult mr = mvc.perform( //
				get("/warehousing/retrieve1Ex.bx?" //
						+ Warehousing.field.getFIELD_NAME_ID() + "=1&" //
						+ Warehousing.field.getFIELD_NAME_status() + "=" + EnumStatusWarehousing.ESW_ToApprove.getIndex()//
				) //
						.contentType(MediaType.APPLICATION_JSON) //
						.session(sessionBoss) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();
		Shared.checkJSONErrorCode(mr);
		String json = mr.getResponse().getContentAsString();
		JSONObject jsonObject = JSONObject.fromObject(json);
		JSONObject ws = jsonObject.getJSONObject(BaseAction.KEY_Object);

		assertTrue(ws.getInt(Warehousing.field.getFIELD_NAME_ID()) == 1);
		// ??????????????????
		List<?> wCommodityList = ws.getJSONArray(Warehousing.field.getFIELD_NAME_listSlave1());
		for (Object object : wCommodityList) {
			assertTrue(JSONObject.fromObject(object).getInt(WarehousingCommodity.field.getFIELD_NAME_warehousingID()) == ws.getInt(Warehousing.field.getFIELD_NAME_ID()));
		}

	}

	@Test
	public void testRetrieve1Ex2() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("case2???????????????");
//		MvcResult mr1 = mvc.perform( //
//				get("/warehousing/retrieve1Ex.bx?"//
//						+ Warehousing.field.getFIELD_NAME_ID() + "=1&" //
//						+ Warehousing.field.getFIELD_NAME_status() + "=" + EnumStatusWarehousing.ESW_ToApprove.getIndex()//
//				) //
//						.contentType(MediaType.APPLICATION_JSON) //
//						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfManager))) //
//				.andExpect(status().isOk()) //
//				.andDo(print()) //
//				.andReturn(); //
//		Shared.checkJSONErrorCode(mr1, EnumErrorCode.EC_NoPermission);
	}

	@Test
	public void testRetrieve1Ex3() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("case3??????????????????ID??????");
		MvcResult mr1 = mvc.perform( //
				get("/warehousing/retrieve1Ex.bx?" + Warehousing.field.getFIELD_NAME_ID() + "=" + Shared.BIG_ID + "&" //
						+ Warehousing.field.getFIELD_NAME_status() + "=" + EnumStatusWarehousing.ESW_ToApprove.getIndex() //
				) //
						.contentType(MediaType.APPLICATION_JSON) //
						.session(sessionBoss) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();
		Shared.checkJSONErrorCode(mr1);
		String json = mr1.getResponse().getContentAsString();
		Assert.assertTrue(JSONObject.fromObject(json).get(BaseAction.KEY_Object) == null);
	}

	@Test
	public void testRetrieve1Ex4() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("case4:???????????????????????????");
		MvcResult mr = mvc.perform( //
				get("/warehousing/retrieve1Ex.bx?" //
						+ Warehousing.field.getFIELD_NAME_ID() + "=11&"//
						+ Warehousing.field.getFIELD_NAME_status() + "=" + EnumStatusWarehousing.ESW_Approved.getIndex() //
				) //
						.contentType(MediaType.APPLICATION_JSON) //
						.session(sessionBoss) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();
		Shared.checkJSONErrorCode(mr);

		String json = mr.getResponse().getContentAsString();
		JSONObject jsonObject = JSONObject.fromObject(json);
		JSONObject ws = jsonObject.getJSONObject(BaseAction.KEY_Object);

		assertTrue(ws.getInt(Warehousing.field.getFIELD_NAME_ID()) == 11);
		// ??????????????????
		List<?> wCommodityList = ws.getJSONArray(Warehousing.field.getFIELD_NAME_listSlave1());
		for (Object object : wCommodityList) {
			assertTrue(JSONObject.fromObject(object).getInt(WarehousingCommodity.field.getFIELD_NAME_warehousingID()) == ws.getInt(Warehousing.field.getFIELD_NAME_ID()));
		}
	}

	@Test
	public void testRetrieve1Ex5() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("case5:?????????????????????????????????");
		MvcResult mr = mvc.perform( //
				get("/warehousing/retrieve1Ex.bx?" + Warehousing.field.getFIELD_NAME_ID() + "=" + BaseAction.INVALID_ID) //
						.contentType(MediaType.APPLICATION_JSON) //
						.session(sessionBoss) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_WrongFormatForInputField);
	}

	@Test
	public void testRetrieve1Ex6() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("case1:??????ID???????????????????????????????????????????????????????????????????????????");

		MvcResult mr = mvc.perform( //
				get("/warehousing/retrieve1Ex.bx?" //
						+ Warehousing.field.getFIELD_NAME_ID() + "=1&" //
						+ Warehousing.field.getFIELD_NAME_status() + "=" + EnumStatusWarehousing.ESW_ToApprove.getIndex()//
				) //
						.contentType(MediaType.APPLICATION_JSON) //
						.session(sessionBoss) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();
		Shared.checkJSONErrorCode(mr);
		String json = mr.getResponse().getContentAsString();
		JSONObject jsonObject = JSONObject.fromObject(json);
		JSONObject ws = jsonObject.getJSONObject(BaseAction.KEY_Object);

		assertTrue(ws.getInt(Warehousing.field.getFIELD_NAME_ID()) == 1);
		// ??????????????????
		List<?> wCommodityList = ws.getJSONArray(Warehousing.field.getFIELD_NAME_listSlave1());
		for (Object object : wCommodityList) {
			assertTrue(JSONObject.fromObject(object).getInt(WarehousingCommodity.field.getFIELD_NAME_warehousingID()) == ws.getInt(Warehousing.field.getFIELD_NAME_ID()));
		}
		//
		PurchasingOrder pOrder = new PurchasingOrder();
		pOrder.setID(ws.getInt(Warehousing.field.getFIELD_NAME_purchasingOrderID()));
		Map<String, Object> paramsPOrderR1 = pOrder.getRetrieve1Param(BaseBO.INVALID_CASE_ID, pOrder);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<List<BaseModel>> bmPOrderList = purchasingOrderMapper.retrieve1Ex(paramsPOrderR1);
		PurchasingOrder purchasingOrderR1 = (PurchasingOrder) bmPOrderList.get(0).get(0);
		Assert.assertTrue(ws.getString(Warehousing.field.getFIELD_NAME_purchasingOrderSN()).equals(purchasingOrderR1.getSn()), "R1????????????????????????????????????????????????");
	}

	@Test
	public void testCreateEx1() throws Exception { // ...
		Shared.printTestMethodStartInfo();
		HttpSession session = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);
		//
		Shared.caseLog("case1:????????????");
		PurchasingOrder po = createPurchasingOrder();
		//
		MvcResult result = mvc.perform( //
				post("/warehousing/retrieveNCommEx.bx") //
						.session((MockHttpSession) session) //
						.param(Warehousing.field.getFIELD_NAME_purchasingOrderID(), po.getID() + "") //
		) //
				.andExpect(status().isOk()) //
				.andReturn(); //
		Warehousing whousing = new Warehousing();
		whousing.setPurchasingOrderID(po.getID());
		whousing.setShopID(Shared.DEFAULT_Shop_ID);
		whousing.setProviderID(1);
		whousing.setWarehouseID(1);
		whousing.setStaffID(4);// 15854320895???????????????

		MvcResult mr2 = mvc.perform( //
				post("/warehousing/createEx.bx") //
						.param(Warehousing.field.getFIELD_NAME_purchasingOrderID(), String.valueOf(po.getID())) //
						.param(Warehousing.field.getFIELD_NAME_shopID(), String.valueOf(Shared.DEFAULT_Shop_ID)) //
						.param(Warehousing.field.getFIELD_NAME_providerID(), "1") //
						.param(Warehousing.field.getFIELD_NAME_warehouseID(), "1") //
						.param(commIDs, "2") //
						.param(commNOs, "4") //
						.param(commPrices, "11.1") //
						.param(amounts, "11.1") //
						.param(barcodeIDs, "3") //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) result.getRequest().getSession()) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();
		Shared.checkJSONErrorCode(mr2);
		//
		JSONObject o1 = JSONObject.fromObject(mr2.getResponse().getContentAsString());
		Integer wsID = JsonPath.read(o1, "$.object.ID");
		//
		ErrorInfo ecOut = new ErrorInfo();
		Warehousing wsR1 = (Warehousing) CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_Warehousing).read1(wsID, 3, ecOut, Shared.DBName_Test);
		String wsCreateDatetime = JsonPath.read(o1, "$.object.createDatetime");
		SimpleDateFormat sdf1 = new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default1);
		Date createDatetime = sdf1.parse(wsCreateDatetime);
		//
		Assert.assertTrue(createDatetime.getTime() == (wsR1.getCreateDatetime().getTime())); // ... TODO ???????????????????????? ?????? ?????? ??????

		WarehousingCP.verifyCreate(mr2, whousing, Shared.DBName_Test);
	}

	@Test
	public void testCreateEx2() throws Exception { // ...
		Shared.printTestMethodStartInfo();
		Shared.caseLog("case2:????????????????????????????????????2");
		PurchasingOrder po2 = createPurchasingOrder();
		//
		po2.setStatus(2);
		Map<String, Object> params3 = po2.getUpdateParam(BaseBO.CASE_UpdatePurchasingOrderStatus, po2);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		PurchasingOrder poUpdateStatus = purchasingOrderMapper.updateStatus(params3);
		po2.setIgnoreIDInComparision(true);
		//
		if (po2.compareTo(poUpdateStatus) != 0) {
			Assert.assertTrue(false, "???????????????????????????DB??????????????????");
		}
		// ?????????
		Warehousing whousing = new Warehousing();
		whousing.setPurchasingOrderID(po2.getID());
		whousing.setShopID(Shared.DEFAULT_Shop_ID);
		whousing.setProviderID(1);
		whousing.setWarehouseID(1);
		whousing.setStaffID(4);// 15854320895???????????????
		MvcResult mr6 = mvc.perform( //
				post("/warehousing/createEx.bx") //
						.param(Warehousing.field.getFIELD_NAME_shopID(), String.valueOf(Shared.DEFAULT_Shop_ID)) //
						.param(Warehousing.field.getFIELD_NAME_purchasingOrderID(), String.valueOf(po2.getID())) //
						.param(Warehousing.field.getFIELD_NAME_providerID(), "1") //
						.param(Warehousing.field.getFIELD_NAME_warehouseID(), "1") //
						.param(commIDs, "2") //
						.param(commNOs, "4") //
						.param(commPrices, "11.1") //
						.param(amounts, "11.1") //
						.param(barcodeIDs, "3") //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) getLoginSession()) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn(); //
		//
		Shared.checkJSONErrorCode(mr6);

		WarehousingCP.verifyCreate(mr6, whousing, Shared.DBName_Test);
	}

	@Test
	public void testCreateEx3() throws Exception { // ...
		Shared.printTestMethodStartInfo();
		Shared.caseLog("case3??????????????????????????????????????????????????????????????????");
		MvcResult mr3 = mvc.perform( //
				post("/warehousing/createEx.bx") //
						.param(Warehousing.field.getFIELD_NAME_purchasingOrderID(), "9") //
						.param(Warehousing.field.getFIELD_NAME_shopID(), String.valueOf(Shared.DEFAULT_Shop_ID)) //
						.param(Warehousing.field.getFIELD_NAME_providerID(), "1") //
						.param(Warehousing.field.getFIELD_NAME_warehouseID(), "1") //
						.param(commIDs, "2") //
						.param(commNOs, "4") //
						.param(commPrices, "11.1") //
						.param(amounts, "11.1") //
						.param(barcodeIDs, "3") //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) getLoginSession()) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();
		Shared.checkJSONErrorCode(mr3, EnumErrorCode.EC_BusinessLogicNotDefined);
	}

	@Test
	public void testCreateEx4() throws Exception { // ...
		Shared.printTestMethodStartInfo();
		Shared.caseLog("case4:?????????????????????????????????????????????????????????????????????");
		MvcResult mr4 = mvc.perform( //
				post("/warehousing/createEx.bx") //
						.param(Warehousing.field.getFIELD_NAME_purchasingOrderID(), "4") //
						.param(Warehousing.field.getFIELD_NAME_shopID(), String.valueOf(Shared.DEFAULT_Shop_ID)) //
						.param(Warehousing.field.getFIELD_NAME_providerID(), "1") //
						.param(Warehousing.field.getFIELD_NAME_warehouseID(), "1") //
						.param(commIDs, "2") //
						.param(commNOs, "4") //
						.param(commPrices, "11.1") //
						.param(amounts, "11.1") //
						.param(barcodeIDs, "4") //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) getLoginSession()) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn(); //
		//
		Shared.checkJSONErrorCode(mr4, EnumErrorCode.EC_BusinessLogicNotDefined);
	}

	@Test
	public void testCreateEx5() throws Exception { // ...
		Shared.printTestMethodStartInfo();
		Shared.caseLog("case5:?????????????????????");
		//
		Warehousing warehousing = new Warehousing();
		warehousing.setPurchasingOrderID(0);
		warehousing.setShopID(Shared.DEFAULT_Shop_ID);
		warehousing.setProviderID(1);
		warehousing.setWarehouseID(1);
		warehousing.setStaffID(Shared.BossID);
		//
		MvcResult mr5 = mvc.perform( //
				post("/warehousing/createEx.bx") //
						.param(Warehousing.field.getFIELD_NAME_purchasingOrderID(), String.valueOf(warehousing.getPurchasingOrderID())) //
						.param(Warehousing.field.getFIELD_NAME_shopID(), String.valueOf(Shared.DEFAULT_Shop_ID)) //
						.param(Warehousing.field.getFIELD_NAME_providerID(), String.valueOf(warehousing.getProviderID())) //
						.param(Warehousing.field.getFIELD_NAME_warehouseID(), String.valueOf(warehousing.getWarehouseID())) //
						.param(commIDs, "2") //
						.param(commNOs, "4") //
						.param(commPrices, "11.1") //
						.param(amounts, "11.1") //
						.param(barcodeIDs, "3") //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) getLoginSession()) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn(); //
		//
		Shared.checkJSONErrorCode(mr5);
		WarehousingCP.verifyCreate(mr5, warehousing, Shared.DBName_Test);
	}

	@Test
	public void testCreateEx6() throws Exception { // ...
		Shared.printTestMethodStartInfo();
		Shared.caseLog("case6:???????????????????????????");
//		PurchasingOrder po = createPurchasingOrder();
//		MvcResult mr7 = mvc.perform( //
//				post("/warehousing/createEx.bx") //
//						.param(Warehousing.field.getFIELD_NAME_purchasingOrderID(), String.valueOf(po.getID())) //
//						.param(Warehousing.field.getFIELD_NAME_shopID(), String.valueOf(Shared.DEFAULT_Shop_ID)) //
//						.param(Warehousing.field.getFIELD_NAME_providerID(), "1") //
//						.param(Warehousing.field.getFIELD_NAME_warehouseID(), "1") //
//						.param(commIDs, "2") //
//						.param(commNOs, "4") //
//						.param(commPrices, "11.1") //
//						.param(amounts, "11.1") //
//						.param(barcodeIDs, "4") //
//						.contentType(MediaType.APPLICATION_JSON) //
//						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfManager)) //
//		) //
//				.andExpect(status().isOk()) //
//				.andDo(print()) //
//				.andReturn();
//		Shared.checkJSONErrorCode(mr7, EnumErrorCode.EC_NoPermission);
	}

	@Test
	public void testCreateEx7() throws Exception { // ...
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case7:??????????????????????????????????????????");
		PurchasingOrder po = createPurchasingOrder();
		MvcResult mr8 = mvc.perform( //
				post("/warehousing/createEx.bx") //
						.param(Warehousing.field.getFIELD_NAME_purchasingOrderID(), String.valueOf(po.getID())) //
						.param(Warehousing.field.getFIELD_NAME_shopID(), String.valueOf(Shared.DEFAULT_Shop_ID)) //
						.param(Warehousing.field.getFIELD_NAME_providerID(), "1") //
						.param(Warehousing.field.getFIELD_NAME_warehouseID(), String.valueOf(BaseWarehousingTest.INVALID_ID)) //
						.param(commIDs, "2") //
						.param(commNOs, "4") //
						.param(commPrices, "11.1") //
						.param(amounts, "11.1") //
						.param(barcodeIDs, "4") //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) getLoginSession()) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();
		Shared.checkJSONErrorCode(mr8, EnumErrorCode.EC_BusinessLogicNotDefined);

	}

	@Test
	public void testCreateEx8() throws Exception { // ...
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case8:?????????????????????????????????ID????????????");
		PurchasingOrder po = createPurchasingOrder();
		MvcResult mr9 = mvc.perform( //
				post("/warehousing/createEx.bx") //
						.param(Warehousing.field.getFIELD_NAME_purchasingOrderID(), String.valueOf(po.getID())) //
						.param(Warehousing.field.getFIELD_NAME_shopID(), String.valueOf(Shared.DEFAULT_Shop_ID)) //
						.param(Warehousing.field.getFIELD_NAME_providerID(), String.valueOf(BaseWarehousingTest.INVALID_ID)) //
						.param(Warehousing.field.getFIELD_NAME_warehouseID(), "1") //
						.param(commIDs, "2") //
						.param(commNOs, "4") //
						.param(commPrices, "11.1") //
						.param(amounts, "11.1") //
						.param(barcodeIDs, "4") //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) getLoginSession()) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();
		Shared.checkJSONErrorCode(mr9, EnumErrorCode.EC_BusinessLogicNotDefined);
	}

	@Test
	public void testCreateEx9() throws Exception { // ...
		Shared.printTestMethodStartInfo();
		HttpSession session = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);
		//
		Shared.caseLog("case9:????????????????????????????????????????????????");
		PurchasingOrder po = createPurchasingOrder();
		//
		MvcResult mr = mvc.perform( //
				post("/warehousing/createEx.bx") //
						.param(Warehousing.field.getFIELD_NAME_purchasingOrderID(), String.valueOf(po.getID())) //
						.param(Warehousing.field.getFIELD_NAME_shopID(), String.valueOf(Shared.DEFAULT_Shop_ID)) //
						.param(Warehousing.field.getFIELD_NAME_providerID(), "1") //
						.param(Warehousing.field.getFIELD_NAME_warehouseID(), "1") //
						.param(commIDs, "52") //
						.param(commNOs, "4") //
						.param(commPrices, "11.1") //
						.param(amounts, "11.1") //
						.param(barcodeIDs, "56") //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) session) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_BusinessLogicNotDefined);
		Shared.checkJSONMsg(mr, "???????????????????????????????????????????????????????????????3????????????????????????", "?????????????????????");
	}

	@Test
	public void testCreateEx10() throws Exception { // ...
		Shared.printTestMethodStartInfo();
		HttpSession session = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);
		//
		Shared.caseLog("case10:????????????commodityID???????????????????????????");
		PurchasingOrder po = createPurchasingOrder();
		//
		MvcResult mr = mvc.perform( //
				post("/warehousing/createEx.bx") //
						.param(Warehousing.field.getFIELD_NAME_purchasingOrderID(), String.valueOf(po.getID())) //
						.param(Warehousing.field.getFIELD_NAME_shopID(), String.valueOf(Shared.DEFAULT_Shop_ID)) //
						.param(Warehousing.field.getFIELD_NAME_providerID(), "1") //
						.param(Warehousing.field.getFIELD_NAME_warehouseID(), "1") //
						.param(commIDs, "" + Shared.BIG_ID) //
						.param(commNOs, "4") //
						.param(commPrices, "11.1") //
						.param(amounts, "11.1") //
						.param(barcodeIDs, "1") //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) session) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_NoSuchData);
		Shared.checkJSONMsg(mr, "????????????????????????????????????", "?????????????????????");
	}

	@Test
	public void testCreateEx11() throws Exception { // ...
		Shared.printTestMethodStartInfo();
		HttpSession session = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);
		//
		Shared.caseLog("case11:????????????iBarcodeID???????????????????????????");
		PurchasingOrder po = createPurchasingOrder();
		//
		MvcResult mr = mvc.perform( //
				post("/warehousing/createEx.bx") //
						.param(Warehousing.field.getFIELD_NAME_purchasingOrderID(), String.valueOf(po.getID())) //
						.param(Warehousing.field.getFIELD_NAME_shopID(), String.valueOf(Shared.DEFAULT_Shop_ID)) //
						.param(Warehousing.field.getFIELD_NAME_providerID(), "1") //
						.param(Warehousing.field.getFIELD_NAME_warehouseID(), "1") //
						.param(commIDs, "2") //
						.param(barcodeIDs, "" + Shared.BIG_ID) //
						.param(commNOs, "4") //
						.param(commPrices, "11.1") //
						.param(amounts, "11.1") //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) session) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_NoSuchData);
		Shared.checkJSONMsg(mr, "?????????????????????", "?????????????????????");

	}

	@Test
	public void testCreateEx12() throws Exception { // ...
		Shared.printTestMethodStartInfo();
		HttpSession session = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);
		//
		Shared.caseLog("case12:?????????????????????????????????????????????");
		PurchasingOrder po = createPurchasingOrder();
		//
		MvcResult mr = mvc.perform( //
				post("/warehousing/createEx.bx") //
						.param(Warehousing.field.getFIELD_NAME_purchasingOrderID(), String.valueOf(po.getID())) //
						.param(Warehousing.field.getFIELD_NAME_shopID(), String.valueOf(Shared.DEFAULT_Shop_ID)) //
						.param(Warehousing.field.getFIELD_NAME_providerID(), "1") //
						.param(Warehousing.field.getFIELD_NAME_warehouseID(), "1") //
						.param(commIDs, "46") //
						.param(commNOs, "4") //
						.param(commPrices, "11.1") //
						.param(amounts, "11.1") //
						.param(barcodeIDs, "1") //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) session) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_BusinessLogicNotDefined);
		Shared.checkJSONMsg(mr, "???????????????????????????????????????????????????????????????????????????????????????", "?????????????????????");

	}

	@Test
	public void testCreateEx13() throws Exception { // ...
		Shared.printTestMethodStartInfo();
		HttpSession session = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);
		//
		Shared.caseLog("case13:??????????????????????????????????????????????????????case10?????????");
		PurchasingOrder po = createPurchasingOrder();
		//
		MvcResult mr = mvc.perform( //
				post("/warehousing/createEx.bx") //
						.param(Warehousing.field.getFIELD_NAME_purchasingOrderID(), String.valueOf(po.getID())) //
						.param(Warehousing.field.getFIELD_NAME_shopID(), String.valueOf(Shared.DEFAULT_Shop_ID)) //
						.param(Warehousing.field.getFIELD_NAME_providerID(), "1") //
						.param(Warehousing.field.getFIELD_NAME_warehouseID(), "1") //
						.param(commIDs, "49") //
						.param(commNOs, "4") //
						.param(commPrices, "11.1") //
						.param(amounts, "11.1") //
						.param(barcodeIDs, "1") //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) session) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_NoSuchData);
		Shared.checkJSONMsg(mr, "????????????????????????????????????", "?????????????????????");

	}

	@Test
	public void testCreateEx14() throws Exception { // ...
		Shared.printTestMethodStartInfo();
		HttpSession session = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);
		//
		Shared.caseLog("case14:????????????????????????????????????????????????");
		PurchasingOrder po = createPurchasingOrder();
		//
		MvcResult mr = mvc.perform( //
				post("/warehousing/createEx.bx") //
						.param(Warehousing.field.getFIELD_NAME_purchasingOrderID(), String.valueOf(po.getID())) //
						.param(Warehousing.field.getFIELD_NAME_shopID(), String.valueOf(Shared.DEFAULT_Shop_ID)) //
						.param(Warehousing.field.getFIELD_NAME_providerID(), "1") //
						.param(Warehousing.field.getFIELD_NAME_warehouseID(), "1") //
						.param(commIDs, "2,2") //
						.param(commNOs, "4,4") //
						.param(commPrices, "11.1,11.1") //
						.param(amounts, "11.1,11.1") //
						.param(barcodeIDs, "3,3") //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) session) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();
		Assert.assertTrue("".equals(mr.getResponse().getContentAsString()), "action??????????????????????????????");
	}

	@Test
	public void testCreateEx15() throws Exception { // ...
		Shared.printTestMethodStartInfo();
		HttpSession session = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);
		//
		Shared.caseLog("case15:?????????????????????????????????????????????0");
		PurchasingOrder po = createPurchasingOrder();
		//
		MvcResult mr = mvc.perform( //
				post("/warehousing/createEx.bx") //
						.param(Warehousing.field.getFIELD_NAME_purchasingOrderID(), String.valueOf(po.getID())) //
						.param(Warehousing.field.getFIELD_NAME_shopID(), String.valueOf(Shared.DEFAULT_Shop_ID)) //
						.param(Warehousing.field.getFIELD_NAME_providerID(), "1") //
						.param(Warehousing.field.getFIELD_NAME_warehouseID(), "1") //
						.param(commIDs, "2") //
						.param(commNOs, "0") //
						.param(commPrices, "11.1") //
						.param(amounts, "11.1") //
						.param(barcodeIDs, "1") //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) session) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_WrongFormatForInputField);
	}

	@Test
	public void testCreateEx16() throws Exception { // ...
		Shared.printTestMethodStartInfo();
		HttpSession session = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);
		//
		Shared.caseLog("case16:?????????????????????????????????");
		PurchasingOrder po = createPurchasingOrder();
		//
		MvcResult mr = mvc.perform( //
				post("/warehousing/createEx.bx") //
						.param(Warehousing.field.getFIELD_NAME_purchasingOrderID(), String.valueOf(po.getID())) //
						.param(Warehousing.field.getFIELD_NAME_shopID(), String.valueOf(Shared.DEFAULT_Shop_ID)) //
						.param(Warehousing.field.getFIELD_NAME_providerID(), "1") //
						.param(Warehousing.field.getFIELD_NAME_warehouseID(), String.valueOf(BaseAction.INVALID_ID)) //
						.param(commIDs, "2") //
						.param(commNOs, "0") //
						.param(commPrices, "11.1") //
						.param(amounts, "11.1") //
						.param(barcodeIDs, "1") //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) session) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_WrongFormatForInputField);
	}

	@Test
	public void testCreateEx17() throws Exception { // ...
		Shared.printTestMethodStartInfo();
		HttpSession session = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);
		//
		Shared.caseLog("case17:????????????????????????????????????");
		PurchasingOrder po = createPurchasingOrder();
		//
		MvcResult mr2 = mvc.perform( //
				post("/warehousing/createEx.bx") //
						.param(Warehousing.field.getFIELD_NAME_purchasingOrderID(), String.valueOf(po.getID())) //
						.param(Warehousing.field.getFIELD_NAME_shopID(), String.valueOf(Shared.DEFAULT_Shop_ID)) //
						.param(Warehousing.field.getFIELD_NAME_providerID(), "1") //
						.param(Warehousing.field.getFIELD_NAME_warehouseID(), "1") //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) session) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();
		// Shared.checkJSONErrorCode(mr2);
		String json = mr2.getResponse().getContentAsString();
		Assert.assertTrue(json.length() == 0, "CASE17?????????????????????????????????null");
	}

	@Test
	public void testCreateEx18() throws Exception { // ...
		Shared.printTestMethodStartInfo();
		Shared.caseLog("case18:????????????????????????????????????" + FieldFormat.MAX_OneCommodityPrice + "???????????????");
		//
		Commodity commodity = BaseCommodityTest.DataInput.getCommodity();
		Commodity commCreate = BaseCommodityTest.createCommodityViaAction(commodity, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		Barcodes barcodes = retrieveNBarcodes(commCreate);

		Warehousing warehousing = new Warehousing();
		warehousing.setPurchasingOrderID(0);
		warehousing.setShopID(Shared.DEFAULT_Shop_ID);
		warehousing.setProviderID(1);
		warehousing.setWarehouseID(1);
		warehousing.setStaffID(Shared.BossID);
		//
		MvcResult mr = mvc.perform( //
				post("/warehousing/createEx.bx") //
						.param(Warehousing.field.getFIELD_NAME_purchasingOrderID(), String.valueOf(warehousing.getPurchasingOrderID())) //
						.param(Warehousing.field.getFIELD_NAME_shopID(), String.valueOf(Shared.DEFAULT_Shop_ID)) //
						.param(Warehousing.field.getFIELD_NAME_providerID(), String.valueOf(warehousing.getProviderID())) //
						.param(Warehousing.field.getFIELD_NAME_warehouseID(), String.valueOf(warehousing.getWarehouseID())) //
						.param(commIDs, String.valueOf(commCreate.getID())) //
						.param(barcodeIDs, String.valueOf(barcodes.getID())) //
						.param(commNOs, "4") //
						.param(commPrices, String.valueOf(FieldFormat.MAX_OneCommodityPrice + 0.001)) //
						.param(amounts, "40000.003") //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) getLoginSession()) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn(); //
		//
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_WrongFormatForInputField);
	}

	@Test
	public void testCreateEx19() throws Exception { // ...
		Shared.printTestMethodStartInfo();
		Shared.caseLog("case19:????????????????????????????????????" + FieldFormat.MAX_OneCommodityNO + "???????????????");
		//
		Commodity commodity = BaseCommodityTest.DataInput.getCommodity();
		Commodity commCreate = BaseCommodityTest.createCommodityViaAction(commodity, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		Barcodes barcodes = retrieveNBarcodes(commCreate);

		Warehousing warehousing = new Warehousing();
		warehousing.setPurchasingOrderID(0);
		warehousing.setShopID(Shared.DEFAULT_Shop_ID);
		warehousing.setProviderID(1);
		warehousing.setWarehouseID(1);
		//
		MvcResult mr = mvc.perform( //
				post("/warehousing/createEx.bx") //
						.param(Warehousing.field.getFIELD_NAME_purchasingOrderID(), String.valueOf(warehousing.getPurchasingOrderID())) //
						.param(Warehousing.field.getFIELD_NAME_shopID(), String.valueOf(Shared.DEFAULT_Shop_ID)) //
						.param(Warehousing.field.getFIELD_NAME_providerID(), String.valueOf(warehousing.getProviderID())) //
						.param(Warehousing.field.getFIELD_NAME_warehouseID(), String.valueOf(warehousing.getWarehouseID())) //
						.param(commIDs, String.valueOf(commCreate.getID())) //
						.param(barcodeIDs, String.valueOf(barcodes.getID())) //
						.param(commNOs, String.valueOf(FieldFormat.MAX_OneCommodityNO + 1)) //
						.param(commPrices, "4") //
						.param(amounts, "40000.003") //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) getLoginSession()) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn(); //
		//
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_WrongFormatForInputField);
	}

	@Test
	public void testCreateEx20() throws Exception { // ...
		Shared.printTestMethodStartInfo();
		Shared.caseLog("case20:?????????????????????????????????commIDs?????????");
		//
		Commodity commodity = BaseCommodityTest.DataInput.getCommodity();
		Commodity commCreate = BaseCommodityTest.createCommodityViaAction(commodity, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		Barcodes barcodes = retrieveNBarcodes(commCreate);

		Warehousing warehousing = new Warehousing();
		warehousing.setPurchasingOrderID(0);
		warehousing.setShopID(Shared.DEFAULT_Shop_ID);
		warehousing.setProviderID(1);
		warehousing.setWarehouseID(1);
		//
		MvcResult mr = mvc.perform( //
				post("/warehousing/createEx.bx") //
						.param(Warehousing.field.getFIELD_NAME_purchasingOrderID(), String.valueOf(warehousing.getPurchasingOrderID())) //
						.param(Warehousing.field.getFIELD_NAME_shopID(), String.valueOf(Shared.DEFAULT_Shop_ID)) //
						.param(Warehousing.field.getFIELD_NAME_providerID(), String.valueOf(warehousing.getProviderID())) //
						.param(Warehousing.field.getFIELD_NAME_warehouseID(), String.valueOf(warehousing.getWarehouseID())) //
						.param(commIDs, "") //
						.param(barcodeIDs, String.valueOf(barcodes.getID())) //
						.param(commNOs, "1") //
						.param(commPrices, "4") //
						.param(amounts, "43") //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) getLoginSession()) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn(); //
		String o = mr.getResponse().getContentAsString();
		Assert.assertTrue(o.length() == 0, "case20?????????????????????????????????????????????");
	}

	@Test
	public void testCreateEx21() throws Exception { // ...
		Shared.printTestMethodStartInfo();
		HttpSession session = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);
		//
		Shared.caseLog("case21:??????????????????ID????????????ID???????????????????????????");
		PurchasingOrder po = createPurchasingOrder();
		//
		MvcResult mr = mvc.perform( //
				post("/warehousing/createEx.bx") //
						.param(Warehousing.field.getFIELD_NAME_purchasingOrderID(), String.valueOf(po.getID())) //
						.param(Warehousing.field.getFIELD_NAME_shopID(), String.valueOf(Shared.DEFAULT_Shop_ID)) //
						.param(Warehousing.field.getFIELD_NAME_providerID(), "1") //
						.param(Warehousing.field.getFIELD_NAME_warehouseID(), "1") //
						.param(commIDs, "2") //
						.param(barcodeIDs, "1") //
						.param(commNOs, "4") //
						.param(commPrices, "11.1") //
						.param(amounts, "11.1") //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) session) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_BusinessLogicNotDefined);
		Shared.checkJSONMsg(mr, "??????????????????????????????????????????", "?????????????????????");

	}
	
	@Test
	public void testCreateEx22() throws Exception { // ...
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case22:???????????????ID???????????????");
		PurchasingOrder po = createPurchasingOrder();
		MvcResult mr8 = mvc.perform( //
				post("/warehousing/createEx.bx") //
						.param(Warehousing.field.getFIELD_NAME_purchasingOrderID(), String.valueOf(po.getID())) //
						.param(Warehousing.field.getFIELD_NAME_shopID(), String.valueOf(BaseAction.VirtualShopID)) //
						.param(Warehousing.field.getFIELD_NAME_providerID(), "1") //
						.param(Warehousing.field.getFIELD_NAME_warehouseID(), "1") //
						.param(commIDs, "2") //
						.param(commNOs, "4") //
						.param(commPrices, "11.1") //
						.param(amounts, "11.1") //
						.param(barcodeIDs, "4") //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) getLoginSession()) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();
		Shared.checkJSONErrorCode(mr8, EnumErrorCode.EC_WrongFormatForInputField);

	}
	
	@Test
	public void testCreateEx23() throws Exception { // ...
		Shared.printTestMethodStartInfo();
		HttpSession session = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);
		//
		Shared.caseLog("case23:??????A?????????????????????B?????????????????????????????????????????????");
		PurchasingOrder po = createPurchasingOrder();
		//
		MvcResult result = mvc.perform( //
				post("/warehousing/retrieveNCommEx.bx") //
						.session((MockHttpSession) session) //
						.param(Warehousing.field.getFIELD_NAME_purchasingOrderID(), po.getID() + "") //
		) //
				.andExpect(status().isOk()) //
				.andReturn(); //
		Warehousing whousing = new Warehousing();
		whousing.setPurchasingOrderID(po.getID());
		whousing.setShopID(Shared.DEFAULT_Shop_ID);
		whousing.setProviderID(1);
		whousing.setWarehouseID(1);
		whousing.setStaffID(4);// 15854320895???????????????
		
		// ????????????B???????????????
		Shop shopGet = BaseShopTest.DataInput.getShop();
		Shop shopCreated = BaseShopTest.createViaCompanyAction(shopGet, mvc, mapBO, sessionOP, EnumErrorCode.EC_NoError);
		// ????????????????????????
		String Staff_Phone = Shared.getValidStaffPhone();
		MvcResult ret = BaseStaffTest.getToken(sessionBoss, mvc, Staff_Phone, Staff.FOR_CreateNewStaff);
		//
		String encrypt = Shared.encrypt(ret, Shared.PASSWORD_DEFAULT);
		Staff staff = BaseStaffTest.DataInput.getStaff();
		staff.setShopID(shopCreated.getID());
		staff.setName("??????" + Shared.generateCompanyName(6));
		staff.setRoleID(EnumTypeRole.ETR_Boss.getIndex());
		staff.setPhone(Staff_Phone);
		staff.setPwdEncrypted(encrypt);
		MvcResult mrStaffCreate = mvc.perform(//
				BaseStaffTest.DataInput.getBuilder("/staff/createEx.bx", MediaType.APPLICATION_JSON, staff)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		// ??????????????????????????????
		Shared.checkJSONErrorCode(mrStaffCreate);
		Staff staffCopy = (Staff) staff.clone(); // ??????????????????????????????pos??????????????????????????????????????????????????????????????????copy??????????????????????????????????????????
		StaffCP.verifyCreate(mrStaffCreate, staffCopy, staffRoleBO, Shared.DBName_Test);
		staff = (Staff) staffCopy.clone(); // ????????????
		//
		JSONObject object = JSONObject.fromObject(mrStaffCreate.getResponse().getContentAsString());
		Staff staff2 = new Staff();
		staff2 = (Staff) staff2.parse1(object.getString(BaseAction.KEY_Object));
		Assert.assertTrue(staff2 != null, "????????????");
		// ??????
		MockHttpSession sessionNewShopBoss = Shared.getStaffLoginSession(mvc, staff2.getPhone(), Shared.PASSWORD_DEFAULT, Shared.DB_SN_Test);
		MvcResult mr2 = mvc.perform( //
				post("/warehousing/createEx.bx") //
						.param(Warehousing.field.getFIELD_NAME_purchasingOrderID(), String.valueOf(po.getID())) //
						.param(Warehousing.field.getFIELD_NAME_shopID(), String.valueOf(Shared.DEFAULT_Shop_ID)) //
						.param(Warehousing.field.getFIELD_NAME_providerID(), "1") //
						.param(Warehousing.field.getFIELD_NAME_warehouseID(), "1") //
						.param(commIDs, "2") //
						.param(commNOs, "4") //
						.param(commPrices, "11.1") //
						.param(amounts, "11.1") //
						.param(barcodeIDs, "3") //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionNewShopBoss) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();
		Shared.checkJSONErrorCode(mr2, EnumErrorCode.EC_BusinessLogicNotDefined);
	}
	
	@Test
	public void testCreateEx24() throws Exception { // ...
		Shared.printTestMethodStartInfo();
		HttpSession session = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);
		//
		Shared.caseLog("case24:??????A?????????????????????B()?????????????????????????????????????????????");
		PurchasingOrder po = createPurchasingOrder();
		//
		MvcResult result = mvc.perform( //
				post("/warehousing/retrieveNCommEx.bx") //
						.session((MockHttpSession) session) //
						.param(Warehousing.field.getFIELD_NAME_purchasingOrderID(), po.getID() + "") //
		) //
				.andExpect(status().isOk()) //
				.andReturn(); //
		Warehousing whousing = new Warehousing();
		whousing.setPurchasingOrderID(po.getID());
		whousing.setShopID(Shared.DEFAULT_Shop_ID);
		whousing.setProviderID(1);
		whousing.setWarehouseID(1);
		whousing.setStaffID(4);// 15854320895???????????????
		// ????????????????????????
		String Staff_Phone = Shared.getValidStaffPhone();
		MvcResult ret = BaseStaffTest.getToken(sessionBoss, mvc, Staff_Phone, Staff.FOR_CreateNewStaff);
		//
		String encrypt = Shared.encrypt(ret, Shared.PASSWORD_DEFAULT);
		Staff staff = BaseStaffTest.DataInput.getStaff();
		staff.setShopID(BaseAction.VirtualShopID);
		staff.setName("??????" + Shared.generateCompanyName(6));
		staff.setRoleID(EnumTypeRole.ETR_Boss.getIndex());
		staff.setPhone(Staff_Phone);
		staff.setPwdEncrypted(encrypt);
		MvcResult mrStaffCreate = mvc.perform(//
				BaseStaffTest.DataInput.getBuilder("/staff/createEx.bx", MediaType.APPLICATION_JSON, staff)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		// ??????????????????????????????
		Shared.checkJSONErrorCode(mrStaffCreate);
		Staff staffCopy = (Staff) staff.clone(); // ??????????????????????????????pos??????????????????????????????????????????????????????????????????copy??????????????????????????????????????????
		StaffCP.verifyCreate(mrStaffCreate, staffCopy, staffRoleBO, Shared.DBName_Test);
		staff = (Staff) staffCopy.clone(); // ????????????
		//
		JSONObject object = JSONObject.fromObject(mrStaffCreate.getResponse().getContentAsString());
		Staff staff2 = new Staff();
		staff2 = (Staff) staff2.parse1(object.getString(BaseAction.KEY_Object));
		Assert.assertTrue(staff2 != null, "????????????");
		// ??????
		MockHttpSession sessionNewShopBoss = Shared.getStaffLoginSession(mvc, staff2.getPhone(), Shared.PASSWORD_DEFAULT, Shared.DB_SN_Test);
		MvcResult mr2 = mvc.perform( //
				post("/warehousing/createEx.bx") //
						.param(Warehousing.field.getFIELD_NAME_purchasingOrderID(), String.valueOf(po.getID())) //
						.param(Warehousing.field.getFIELD_NAME_shopID(), String.valueOf(Shared.DEFAULT_Shop_ID)) //
						.param(Warehousing.field.getFIELD_NAME_providerID(), "1") //
						.param(Warehousing.field.getFIELD_NAME_warehouseID(), "1") //
						.param(commIDs, "2") //
						.param(commNOs, "4") //
						.param(commPrices, "11.1") //
						.param(amounts, "11.1") //
						.param(barcodeIDs, "3") //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionNewShopBoss) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();
		Shared.checkJSONErrorCode(mr2);
	}
	// @Test
	// public void testRetrieveN() throws Exception {
	// Shared.printTestMethodStartInfo();
	// mvc.perform( //
	// get("/warehousing/retrieveN.bx") //
	// .session((MockHttpSession) Shared.getStaffLoginSession(mvc, phone)) //
	// ) //
	// .andExpect(status().isOk()) //
	// .andDo(print()) //
	// .andReturn();
	// }

	// @Test
	// public void testRetrieveNCommEx() throws Exception {
	// Shared.printTestMethodStartInfo();
	//
	// MvcResult mr = mvc.perform( //
	// post("/warehousing/retrieveNCommEx.bx") //
	// .contentType(MediaType.APPLICATION_JSON) //
	// .session((MockHttpSession) Shared.getStaffLoginSession(mvc, phone)) //
	// ) //
	// .andExpect(status().isOk()) //
	// .andDo(print()) //
	// .andReturn(); //
	// Shared.checkJSONErrorCode(mr);
	// }

	// @Test
	// public void testPrepareCommodityListEx() throws Exception {
	// Shared.printTestMethodStartInfo();
	//
	// System.out.println("----------------------????????????-----------------------------------");
	// MvcResult mr = mvc.perform( //
	// get("/warehousing/prepareCommodity.bx?commID=2,3") //
	// .contentType(MediaType.APPLICATION_JSON) //
	// .session((MockHttpSession) getLoginSession()) //
	// ) //
	// .andExpect(status().isOk()) //
	// .andDo(print()) //
	// .andReturn();
	// Shared.checkJSONErrorCode(mr);
	// }

	// @Test
	// public void testReduceCommodityEx() throws Exception {
	// Shared.printTestMethodStartInfo();
	//
	// MvcResult mr = mvc.perform( //
	// get("/warehousing/reduceEx.bx?commID= 3") //
	// .contentType(MediaType.APPLICATION_JSON) //
	// .session((MockHttpSession) getLoginSession()) //
	// ) //
	// .andExpect(status().isOk()) //
	// .andDo(print()) //
	// .andReturn();
	// Shared.checkJSONErrorCode(mr);
	// }

	@Test
	public void testApproveEx1() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("Case1:?????????????????????int1=1");
		Commodity commodity1 = BaseCommodityTest.DataInput.getCommodity();
		List<List<BaseModel>> listBM1 = BaseCommodityTest.createCommodityViaMapper(BaseBO.CASE_Commodity_CreateSingle, commodity1);
		Commodity createCommodity1 = (Commodity) listBM1.get(0).get(0);
		Barcodes createBarcodes1 = (Barcodes) listBM1.get(1).get(0);
		//
		Commodity commodity2 = BaseCommodityTest.DataInput.getCommodity();
		List<List<BaseModel>> listBM2 = BaseCommodityTest.createCommodityViaMapper(BaseBO.CASE_Commodity_CreateSingle, commodity2);
		Commodity createCommodity2 = (Commodity) listBM2.get(0).get(0);
		Barcodes createBarcodes2 = (Barcodes) listBM2.get(1).get(0);
		//
		Commodity commodity3 = BaseCommodityTest.DataInput.getCommodity();
		List<List<BaseModel>> listBM3 = BaseCommodityTest.createCommodityViaMapper(BaseBO.CASE_Commodity_CreateSingle, commodity3);
		Commodity createCommodity3 = (Commodity) listBM3.get(0).get(0);
		Barcodes createBarcodes3 = (Barcodes) listBM3.get(1).get(0);
		// ????????????????????????????????????F_NO????????????????????????????????????F_NO??????????????????
		List<Commodity> commList = new ArrayList<>();
		commList.add(createCommodity1);
		commList.add(createCommodity2);
		commList.add(createCommodity3);
		//
		String commIDsStr = createCommodity1.getID() + "," + createCommodity2.getID() + "," + createCommodity3.getID();
		String barcodeIDsStr = createBarcodes1.getID() + "," + createBarcodes2.getID() + "," + createBarcodes3.getID() + ",";
		Warehousing warehousing = BaseWarehousingTest.DataInput.getWarehousing();
		Warehousing ws = createWarehousing(warehousing);
		createCommodity1.setNO(20);
		createCommodity2.setNO(20);
		createCommodity3.setNO(20);
		createWarehousingCommodity(ws.getID(), createCommodity1, createBarcodes1.getID());
		createWarehousingCommodity(ws.getID(), createCommodity2, createBarcodes2.getID());
		createWarehousingCommodity(ws.getID(), createCommodity3, createBarcodes3.getID());
		createCommodity1.setNO(0);
		createCommodity2.setNO(0);
		createCommodity3.setNO(0);
		//
		ws.setIsModified(1);
		Provider provider = createProvider();
		MvcResult mr = mvc.perform( //
				post("/warehousing/approveEx.bx") //
						.param(Warehousing.field.getFIELD_NAME_warehouseID(), String.valueOf(ws.getWarehouseID()))//
						.param(Warehousing.field.getFIELD_NAME_ID(), String.valueOf(ws.getID()))//
						.param(Warehousing.field.getFIELD_NAME_purchasingOrderID(), String.valueOf(ws.getPurchasingOrderID()))//
						.param(Warehousing.field.getFIELD_NAME_providerID(), String.valueOf(provider.getID()))//
						.param(Warehousing.field.getFIELD_NAME_isModified(), String.valueOf(ws.getIsModified()))//
						.param(commIDs, commIDsStr)//
						.param(barcodeIDs, barcodeIDsStr)//
						.param(commNOs, "100,100,20")//
						// .param(commNOs, "100,100,300")//
						.param(commPrices, "1.1,1.2,1.3")//
						.param(amounts, "200,200,200")//
						.contentType(MediaType.APPLICATION_JSON) //
						.session(sessionBoss) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();
		Shared.checkJSONErrorCode(mr);
		WarehousingCP.verifyApprove(mr, ws, warehousingBO, purchasingOrderBO, commList, commodityHistoryBO, messageBO, Shared.DBName_Test);
	}

	@Test
	public void testApproveEx2() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("Case2:??????????????????????????????int1=0");
		Commodity commodity1 = BaseCommodityTest.DataInput.getCommodity();
		List<List<BaseModel>> listBM1 = BaseCommodityTest.createCommodityViaMapper(BaseBO.CASE_Commodity_CreateSingle, commodity1);
		Commodity createCommodity1 = (Commodity) listBM1.get(0).get(0);
		Barcodes createBarcodes1 = (Barcodes) listBM1.get(1).get(0);
		//
		List<Commodity> commList = new ArrayList<>();
		commList.add(createCommodity1);
		//
		Warehousing warehousing = BaseWarehousingTest.DataInput.getWarehousing();
		Warehousing ws = createWarehousing(warehousing);
		createCommodity1.setNO(20);
		createWarehousingCommodity(ws.getID(), createCommodity1, createBarcodes1.getID());
		createCommodity1.setNO(0);
		//
		MvcResult mr = mvc.perform( //
				post("/warehousing/approveEx.bx") //
						.param(Warehousing.field.getFIELD_NAME_warehouseID(), String.valueOf(ws.getWarehouseID()))//
						.param(Warehousing.field.getFIELD_NAME_ID(), String.valueOf(ws.getID()))//
						.param(Warehousing.field.getFIELD_NAME_purchasingOrderID(), String.valueOf(ws.getPurchasingOrderID()))//
						.param(Warehousing.field.getFIELD_NAME_isModified(), "0")//
						.contentType(MediaType.APPLICATION_JSON) //
						.session(sessionBoss) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();
		Shared.checkJSONErrorCode(mr);
		WarehousingCP.verifyApprove(mr, ws, warehousingBO, purchasingOrderBO, commList, commodityHistoryBO, messageBO, Shared.DBName_Test);
	}

	@Test
	public void testApproveEx3() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("Case3:????????????");

		MvcResult mr = mvc.perform( //
				post("/warehousing/approveEx.bx") //
						.param(Warehousing.field.getFIELD_NAME_isModified(), "1")//
						.contentType(MediaType.APPLICATION_JSON) //
						.session(sessionBoss) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();

		// Shared.checkJSONErrorCode(mr);
		String o = mr.getResponse().getContentAsString();
		Assert.assertTrue(o.length() == 0, "CSE3?????????????????????????????????????????????");
	}

	@Test
	public void testApproveEx4() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case4:??????????????????");
		MvcResult mr = mvc.perform( //
				post("/warehousing/approveEx.bx") //
						.param(Warehousing.field.getFIELD_NAME_warehouseID(), "1")//
						.param(Warehousing.field.getFIELD_NAME_ID(), String.valueOf(WS_STATUS1_ID)) //
						.param(Warehousing.field.getFIELD_NAME_purchasingOrderID(), "1") //
						.param(Warehousing.field.getFIELD_NAME_isModified(), "0").param(commIDs, "7")//
						.param(barcodeIDs, "1")//
						.param(commNOs, "200")//
						.param(commPrices, "11.0")//
						.param(amounts, "2200.0")//
						.param("shelfLifes", "36")//
						.contentType(MediaType.APPLICATION_JSON) //
						.session(sessionBoss) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();

		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_BusinessLogicNotDefined);
	}

	@Test
	public void testApproveEx5() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case5:????????????????????????");
//		Commodity commodity1 = BaseCommodityTest.DataInput.getCommodity();
//		List<List<BaseModel>> listBM1 = BaseCommodityTest.createCommodityViaMapper(BaseBO.CASE_Commodity_CreateSingle, commodity1);
//		Commodity createCommodity1 = (Commodity) listBM1.get(0).get(0);
//		Barcodes createBarcodes1 = (Barcodes) listBM1.get(1).get(0);
//		//
//		Warehousing warehousing = BaseWarehousingTest.DataInput.getWarehousing();
//		Warehousing ws = createWarehousing(warehousing);
//		createCommodity1.setNO(20);
//		createWarehousingCommodity(ws.getID(), createCommodity1, createBarcodes1.getID());
//		MvcResult mr = mvc.perform( //
//				post("/warehousing/approveEx.bx") //
//						.param(Warehousing.field.getFIELD_NAME_warehouseID(), String.valueOf(ws.getWarehouseID()))//
//						.param(Warehousing.field.getFIELD_NAME_ID(), String.valueOf(ws.getID()))//
//						.param(Warehousing.field.getFIELD_NAME_purchasingOrderID(), String.valueOf(ws.getPurchasingOrderID()))//
//						.param(Warehousing.field.getFIELD_NAME_isModified(), "0")//
//						.contentType(MediaType.APPLICATION_JSON) //
//						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfManager)) //
//		) //
//				.andExpect(status().isOk()) //
//				.andDo(print()) //
//				.andReturn();
//		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_NoPermission);
	}

	@Test
	public void testApproveEx6() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case6:???????????????????????????ID??????,?????????????????????NULL???????????????????????????????????????,????????????param????????????=EnumErrorCode.EC_NoError???MSG=??????????????????????????????????????????");
		// ?????????????????????????????????????????????????????????
		Commodity commodity1 = BaseCommodityTest.DataInput.getCommodity();
		List<List<BaseModel>> listBM1 = BaseCommodityTest.createCommodityViaMapper(BaseBO.CASE_Commodity_CreateSingle, commodity1);
		Commodity createCommodity1 = (Commodity) listBM1.get(0).get(0);
		Barcodes createBarcodes1 = (Barcodes) listBM1.get(1).get(0);
		//
		List<Commodity> commList = new ArrayList<>();
		commList.add(createCommodity1);
		//
		Warehousing warehousing = BaseWarehousingTest.DataInput.getWarehousing();
		warehousing.setPurchasingOrderID(0);
		Warehousing ws = createWarehousing(warehousing);
		createCommodity1.setNO(20);
		createWarehousingCommodity(ws.getID(), createCommodity1, createBarcodes1.getID());
		createCommodity1.setNO(0);
		MvcResult mr = mvc.perform( //
				post("/warehousing/approveEx.bx") //
						.param(Warehousing.field.getFIELD_NAME_warehouseID(), String.valueOf(ws.getWarehouseID()))//
						.param(Warehousing.field.getFIELD_NAME_ID(), String.valueOf(ws.getID()))//
						.param(Warehousing.field.getFIELD_NAME_purchasingOrderID(), String.valueOf(ws.getPurchasingOrderID()))//
						.param(Warehousing.field.getFIELD_NAME_isModified(), "0")//
						.contentType(MediaType.APPLICATION_JSON) //
						.session(sessionBoss) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();
		String msg = "??????????????????" + ws.getSn() + "?????????????????? ???";
		Shared.checkJSONErrorCode(mr);
		Shared.checkJSONMsg(mr, msg, "?????????msg?????????????????????");
		WarehousingCP.verifyApprove(mr, ws, warehousingBO, purchasingOrderBO, commList, commodityHistoryBO, messageBO, Shared.DBName_Test);
	}

	@Test
	public void testApproveEx7() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case7:????????????????????????????????????");
		Commodity commodity1 = BaseCommodityTest.DataInput.getCommodity();
		List<List<BaseModel>> listBM1 = BaseCommodityTest.createCommodityViaMapper(BaseBO.CASE_Commodity_CreateSingle, commodity1);
		Commodity createCommodity1 = (Commodity) listBM1.get(0).get(0);
		Barcodes createBarcodes1 = (Barcodes) listBM1.get(1).get(0);
		//
		Warehousing warehousing = BaseWarehousingTest.DataInput.getWarehousing();
		Warehousing ws = createWarehousing(warehousing);
		createCommodity1.setNO(20);
		WarehousingCommodity wc = createWarehousingCommodity(ws.getID(), createCommodity1, createBarcodes1.getID());
		createCommodity1.setNO(0);
		//
		MvcResult mr = mvc.perform( //
				post("/warehousing/approveEx.bx") //
						.param(Warehousing.field.getFIELD_NAME_warehouseID(), String.valueOf(ws.getWarehouseID())) //
						.param(Warehousing.field.getFIELD_NAME_ID(), String.valueOf(ws.getID())) //
						.param(Warehousing.field.getFIELD_NAME_purchasingOrderID(), String.valueOf(ws.getPurchasingOrderID())) //
						.param(Warehousing.field.getFIELD_NAME_isModified(), "0") //
						.contentType(MediaType.APPLICATION_JSON) //
						.session(sessionBoss) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();
		Shared.checkJSONErrorCode(mr);

		ErrorInfo ecOut = new ErrorInfo();
		Commodity comm = (Commodity) CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_Commodity).read1(wc.getCommodityID(), BaseBO.SYSTEM, ecOut, Shared.DBName_Test);
		if (ecOut.getErrorCode() != EnumErrorCode.EC_NoError || comm == null) {
			Assert.assertTrue(false, "?????????????????????");
		}
		if (wc.getNO() != comm.getNO()) {
			Assert.assertTrue(false, "??????????????????????????????");
		}
	}

	@Test
	public void testApproveEx8() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("Case8:?????????ID?????????");
		//
		Commodity commodity1 = BaseCommodityTest.DataInput.getCommodity();
		List<List<BaseModel>> listBM1 = BaseCommodityTest.createCommodityViaMapper(BaseBO.CASE_Commodity_CreateSingle, commodity1);
		Commodity createCommodity1 = (Commodity) listBM1.get(0).get(0);
		Barcodes createBarcodes1 = (Barcodes) listBM1.get(1).get(0);
		//
		List<Commodity> commList = new ArrayList<>();
		commList.add(createCommodity1);
		//
		Warehousing warehousing = BaseWarehousingTest.DataInput.getWarehousing();
		Warehousing ws = createWarehousing(warehousing);
		createCommodity1.setNO(20);
		WarehousingCommodity wc = createWarehousingCommodity(ws.getID(), createCommodity1, createBarcodes1.getID());
		createCommodity1.setNO(0);
		//
		MvcResult mr = mvc.perform( //
				post("/warehousing/approveEx.bx") //
						.param(Warehousing.field.getFIELD_NAME_warehouseID(), String.valueOf(ws.getWarehouseID()))//
						.param(Warehousing.field.getFIELD_NAME_ID(), String.valueOf(Shared.BIG_ID))//
						.param(Warehousing.field.getFIELD_NAME_purchasingOrderID(), String.valueOf(ws.getPurchasingOrderID()))//
						.param(Warehousing.field.getFIELD_NAME_isModified(), "0") //
						.contentType(MediaType.APPLICATION_JSON) //
						.session(sessionBoss) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_BusinessLogicNotDefined);
		// ???????????????????????????
		deleteWS(ws);
		deleteWC(wc);
		BaseCommodityTest.deleteCommodityViaMapper(createCommodity1, EnumErrorCode.EC_NoError);
	}

	@Test
	public void testApproveEx9() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case9:?????????????????????int1=1?????????????????????");
		Commodity commodity1 = BaseCommodityTest.DataInput.getCommodity();
		List<List<BaseModel>> listBM1 = BaseCommodityTest.createCommodityViaMapper(BaseBO.CASE_Commodity_CreateSingle, commodity1);
		Commodity createCommodity1 = (Commodity) listBM1.get(0).get(0);
		Barcodes createBarcodes1 = (Barcodes) listBM1.get(1).get(0);
		//
		Commodity commodity2 = BaseCommodityTest.DataInput.getCommodity();
		List<List<BaseModel>> listBM2 = BaseCommodityTest.createCommodityViaMapper(BaseBO.CASE_Commodity_CreateSingle, commodity2);
		Commodity createCommodity2 = (Commodity) listBM2.get(0).get(0);
		Barcodes createBarcodes2 = (Barcodes) listBM2.get(1).get(0);
		//
		Commodity commodity3 = BaseCommodityTest.DataInput.getCommodity();
		List<List<BaseModel>> listBM3 = BaseCommodityTest.createCommodityViaMapper(BaseBO.CASE_Commodity_CreateSingle, commodity3);
		Commodity createCommodity3 = (Commodity) listBM3.get(0).get(0);
		Barcodes createBarcodes3 = (Barcodes) listBM3.get(1).get(0);
		//
		String commIDsStr = createCommodity1.getID() + "," + createCommodity2.getID() + "," + createCommodity3.getID();
		String barcodeIDsStr = createBarcodes1.getID() + "," + createBarcodes2.getID() + "," + createBarcodes3.getID() + ",";
		//
		Warehousing warehousing = BaseWarehousingTest.DataInput.getWarehousing();
		Warehousing ws = createWarehousing(warehousing);
		createCommodity1.setNO(20);
		WarehousingCommodity wc = createWarehousingCommodity(ws.getID(), createCommodity1, createBarcodes1.getID());
		//
		Provider provider = createProvider();
		//
		MvcResult mr = mvc.perform( //
				post("/warehousing/approveEx.bx") //
						.param(Warehousing.field.getFIELD_NAME_warehouseID(), String.valueOf(Shared.BIG_ID))//
						.param(Warehousing.field.getFIELD_NAME_ID(), String.valueOf(ws.getID())).param(Warehousing.field.getFIELD_NAME_purchasingOrderID(), String.valueOf(ws.getPurchasingOrderID()))
						.param(Warehousing.field.getFIELD_NAME_providerID(), String.valueOf(provider.getID()))//
						.param(Warehousing.field.getFIELD_NAME_isModified(), "1") //
						.param(commIDs, commIDsStr)//
						.param(barcodeIDs, barcodeIDsStr)//
						.param(commNOs, "100,100,20")//
						.param(commPrices, "1.1,1.2,1.3")//
						.param(amounts, "200,200,200")//
						.param("shelfLifes", "36,36,36")//
						.contentType(MediaType.APPLICATION_JSON) //
						.session(sessionBoss) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();

		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_BusinessLogicNotDefined);
		// ???????????????????????????
		deleteWS(ws);
		deleteWC(wc);
		BaseCommodityTest.deleteCommodityViaMapper(createCommodity1, EnumErrorCode.EC_NoError);
		BaseCommodityTest.deleteCommodityViaMapper(createCommodity2, EnumErrorCode.EC_NoError);
		BaseCommodityTest.deleteCommodityViaMapper(createCommodity3, EnumErrorCode.EC_NoError);
	}

	@Test
	public void testApproveEx10() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case10:?????????????????????int1=1????????????????????????");
		Commodity commodity1 = BaseCommodityTest.DataInput.getCommodity();
		List<List<BaseModel>> listBM1 = BaseCommodityTest.createCommodityViaMapper(BaseBO.CASE_Commodity_CreateSingle, commodity1);
		Commodity createCommodity1 = (Commodity) listBM1.get(0).get(0);
		Barcodes createBarcodes1 = (Barcodes) listBM1.get(1).get(0);
		//
		Commodity commodity2 = BaseCommodityTest.DataInput.getCommodity();
		List<List<BaseModel>> listBM2 = BaseCommodityTest.createCommodityViaMapper(BaseBO.CASE_Commodity_CreateSingle, commodity2);
		Commodity createCommodity2 = (Commodity) listBM2.get(0).get(0);
		Barcodes createBarcodes2 = (Barcodes) listBM2.get(1).get(0);
		//
		Commodity commodity3 = BaseCommodityTest.DataInput.getCommodity();
		List<List<BaseModel>> listBM3 = BaseCommodityTest.createCommodityViaMapper(BaseBO.CASE_Commodity_CreateSingle, commodity3);
		Commodity createCommodity3 = (Commodity) listBM3.get(0).get(0);
		Barcodes createBarcodes3 = (Barcodes) listBM3.get(1).get(0);
		//
		String commIDsStr = createCommodity1.getID() + "," + createCommodity2.getID() + "," + createCommodity3.getID();
		String barcodeIDsStr = createBarcodes1.getID() + "," + createBarcodes2.getID() + "," + createBarcodes3.getID() + ",";
		//
		Warehousing warehousing = BaseWarehousingTest.DataInput.getWarehousing();
		Warehousing ws = createWarehousing(warehousing);
		createCommodity1.setNO(20);
		WarehousingCommodity wc = createWarehousingCommodity(ws.getID(), createCommodity1, createBarcodes1.getID());
		//
		MvcResult mr = mvc.perform( //
				post("/warehousing/approveEx.bx") //
						.param(Warehousing.field.getFIELD_NAME_warehouseID(), String.valueOf(ws.getWarehouseID()))//
						.param(Warehousing.field.getFIELD_NAME_ID(), String.valueOf(ws.getID()))//
						.param(Warehousing.field.getFIELD_NAME_purchasingOrderID(), String.valueOf(ws.getPurchasingOrderID()))//
						.param(Warehousing.field.getFIELD_NAME_providerID(), String.valueOf(Shared.BIG_ID))//
						.param(Warehousing.field.getFIELD_NAME_isModified(), "1").param(commIDs, commIDsStr)//
						.param(barcodeIDs, barcodeIDsStr)//
						.param(commNOs, "100,100,20")//
						.param(commPrices, "1.1,1.2,1.3")//
						.param(amounts, "200,200,200")//
						.contentType(MediaType.APPLICATION_JSON) //
						.session(sessionBoss) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();

		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_BusinessLogicNotDefined);
		// ???????????????????????????
		deleteWS(ws);
		deleteWC(wc);
		BaseCommodityTest.deleteCommodityViaMapper(createCommodity1, EnumErrorCode.EC_NoError);
		BaseCommodityTest.deleteCommodityViaMapper(createCommodity2, EnumErrorCode.EC_NoError);
		BaseCommodityTest.deleteCommodityViaMapper(createCommodity3, EnumErrorCode.EC_NoError);
	}

	@Test
	public void testApproveEx11() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case11:?????????????????????int1=1????????????ID?????????");
		Commodity commodity1 = BaseCommodityTest.DataInput.getCommodity();
		List<List<BaseModel>> listBM1 = BaseCommodityTest.createCommodityViaMapper(BaseBO.CASE_Commodity_CreateSingle, commodity1);
		Commodity createCommodity1 = (Commodity) listBM1.get(0).get(0);
		Barcodes createBarcodes1 = (Barcodes) listBM1.get(1).get(0);
		//
		Commodity commodity2 = BaseCommodityTest.DataInput.getCommodity();
		List<List<BaseModel>> listBM2 = BaseCommodityTest.createCommodityViaMapper(BaseBO.CASE_Commodity_CreateSingle, commodity2);
		Commodity createCommodity2 = (Commodity) listBM2.get(0).get(0);
		Barcodes createBarcodes2 = (Barcodes) listBM2.get(1).get(0);
		//
		Commodity commodity3 = BaseCommodityTest.DataInput.getCommodity();
		List<List<BaseModel>> listBM3 = BaseCommodityTest.createCommodityViaMapper(BaseBO.CASE_Commodity_CreateSingle, commodity3);
		Commodity createCommodity3 = (Commodity) listBM3.get(0).get(0);
		Barcodes createBarcodes3 = (Barcodes) listBM3.get(1).get(0);
		//
		String commIDsStr = createCommodity1.getID() + "," + createCommodity2.getID() + "," + Shared.BIG_ID;
		String barcodeIDsStr = createBarcodes1.getID() + "," + createBarcodes2.getID() + "," + createBarcodes3.getID() + ",";
		//
		Warehousing warehousing = BaseWarehousingTest.DataInput.getWarehousing();
		Warehousing ws = createWarehousing(warehousing);
		createCommodity1.setNO(20);
		WarehousingCommodity wc = createWarehousingCommodity(ws.getID(), createCommodity1, createBarcodes1.getID());
		//
		Provider provider = createProvider();
		//
		MvcResult mr = mvc.perform( //
				post("/warehousing/approveEx.bx") //
						.param(Warehousing.field.getFIELD_NAME_warehouseID(), String.valueOf(ws.getWarehouseID()))//
						.param(Warehousing.field.getFIELD_NAME_ID(), String.valueOf(ws.getID()))//
						.param(Warehousing.field.getFIELD_NAME_purchasingOrderID(), String.valueOf(ws.getPurchasingOrderID()))//
						.param(Warehousing.field.getFIELD_NAME_providerID(), String.valueOf(provider.getID()))//
						.param(Warehousing.field.getFIELD_NAME_isModified(), "1") //
						.param(commIDs, commIDsStr)//
						.param(barcodeIDs, barcodeIDsStr)//
						.param(commNOs, "100,100,20")//
						.param(commPrices, "1.1,1.2,1.3")//
						.param(amounts, "200,200,200")//
						.param("shelfLifes", "36,36,36")//
						.contentType(MediaType.APPLICATION_JSON) //
						.session(sessionBoss) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();

		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_NoSuchData);
		// ???????????????????????????
		deleteWS(ws);
		deleteWC(wc);
		BaseCommodityTest.deleteCommodityViaMapper(createCommodity1, EnumErrorCode.EC_NoError);
		BaseCommodityTest.deleteCommodityViaMapper(createCommodity2, EnumErrorCode.EC_NoError);
		BaseCommodityTest.deleteCommodityViaMapper(createCommodity3, EnumErrorCode.EC_NoError);
	}

	@Test
	public void testApproveEx12() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case12:?????????????????????int1=1?????????????????????0");
		Commodity commodity1 = BaseCommodityTest.DataInput.getCommodity();
		List<List<BaseModel>> listBM1 = BaseCommodityTest.createCommodityViaMapper(BaseBO.CASE_Commodity_CreateSingle, commodity1);
		Commodity createCommodity1 = (Commodity) listBM1.get(0).get(0);
		Barcodes createBarcodes1 = (Barcodes) listBM1.get(1).get(0);
		//
		Commodity commodity2 = BaseCommodityTest.DataInput.getCommodity();
		List<List<BaseModel>> listBM2 = BaseCommodityTest.createCommodityViaMapper(BaseBO.CASE_Commodity_CreateSingle, commodity2);
		Commodity createCommodity2 = (Commodity) listBM2.get(0).get(0);
		Barcodes createBarcodes2 = (Barcodes) listBM2.get(1).get(0);
		//
		Commodity commodity3 = BaseCommodityTest.DataInput.getCommodity();
		List<List<BaseModel>> listBM3 = BaseCommodityTest.createCommodityViaMapper(BaseBO.CASE_Commodity_CreateSingle, commodity3);
		Commodity createCommodity3 = (Commodity) listBM3.get(0).get(0);
		Barcodes createBarcodes3 = (Barcodes) listBM3.get(1).get(0);
		//
		String commIDsStr = createCommodity1.getID() + "," + createCommodity2.getID() + "," + createCommodity3.getID();
		String barcodeIDsStr = createBarcodes1.getID() + "," + createBarcodes2.getID() + "," + createBarcodes3.getID() + ",";
		//
		Warehousing warehousing = BaseWarehousingTest.DataInput.getWarehousing();
		Warehousing ws = createWarehousing(warehousing);
		createCommodity1.setNO(20);
		WarehousingCommodity wc = createWarehousingCommodity(ws.getID(), createCommodity1, createBarcodes1.getID());
		//
		Provider provider = createProvider();
		//
		MvcResult mr = mvc.perform( //
				post("/warehousing/approveEx.bx") //
						.param(Warehousing.field.getFIELD_NAME_warehouseID(), String.valueOf(ws.getWarehouseID()))//
						.param(Warehousing.field.getFIELD_NAME_ID(), String.valueOf(ws.getID())).param(Warehousing.field.getFIELD_NAME_purchasingOrderID(), String.valueOf(ws.getPurchasingOrderID()))
						.param(Warehousing.field.getFIELD_NAME_providerID(), String.valueOf(provider.getID()))//
						.param(Warehousing.field.getFIELD_NAME_isModified(), "1") //
						.param(commIDs, commIDsStr)//
						.param(barcodeIDs, barcodeIDsStr)//
						.param(commNOs, "100,100,0")//
						.param(commPrices, "1.1,1.2,1.3")//
						.param(amounts, "200,200,200")//
						.param("shelfLifes", "36,36,36")//
						.contentType(MediaType.APPLICATION_JSON) //
						.session(sessionBoss) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();

		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_WrongFormatForInputField);
		// ???????????????????????????
		deleteWS(ws);
		deleteWC(wc);
		BaseCommodityTest.deleteCommodityViaMapper(createCommodity1, EnumErrorCode.EC_NoError);
		BaseCommodityTest.deleteCommodityViaMapper(createCommodity2, EnumErrorCode.EC_NoError);
		BaseCommodityTest.deleteCommodityViaMapper(createCommodity3, EnumErrorCode.EC_NoError);
	}

	@Test
	public void testApproveEx13() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case13:?????????????????????int1=1???????????????????????????");
		Commodity commodity1 = BaseCommodityTest.DataInput.getCommodity();
		List<List<BaseModel>> listBM1 = BaseCommodityTest.createCommodityViaMapper(BaseBO.CASE_Commodity_CreateSingle, commodity1);
		Commodity createCommodity1 = (Commodity) listBM1.get(0).get(0);
		Barcodes createBarcodes1 = (Barcodes) listBM1.get(1).get(0);
		//
		Commodity commodity2 = BaseCommodityTest.DataInput.getCommodity();
		List<List<BaseModel>> listBM2 = BaseCommodityTest.createCommodityViaMapper(BaseBO.CASE_Commodity_CreateSingle, commodity2);
		Commodity createCommodity2 = (Commodity) listBM2.get(0).get(0);
		Barcodes createBarcodes2 = (Barcodes) listBM2.get(1).get(0);
		//
		Commodity commodity3 = BaseCommodityTest.DataInput.getCommodity();
		List<List<BaseModel>> listBM3 = BaseCommodityTest.createCommodityViaMapper(BaseBO.CASE_Commodity_CreateSingle, commodity3);
		Commodity createCommodity3 = (Commodity) listBM3.get(0).get(0);
		Barcodes createBarcodes3 = (Barcodes) listBM3.get(1).get(0);
		//
		String commIDsStr = createCommodity1.getID() + "," + createCommodity2.getID() + "," + createCommodity3.getID();
		String barcodeIDsStr = createBarcodes1.getID() + "," + createBarcodes2.getID() + "," + createBarcodes3.getID() + ",";
		//
		Warehousing warehousing = BaseWarehousingTest.DataInput.getWarehousing();
		Warehousing ws = createWarehousing(warehousing);
		createCommodity1.setNO(20);
		WarehousingCommodity wc = createWarehousingCommodity(ws.getID(), createCommodity1, createBarcodes1.getID());
		//
		Provider provider = createProvider();
		//
		MvcResult mr = mvc.perform( //
				post("/warehousing/approveEx.bx") //
						.param(Warehousing.field.getFIELD_NAME_warehouseID(), String.valueOf(ws.getWarehouseID()))//
						.param(Warehousing.field.getFIELD_NAME_ID(), String.valueOf(ws.getID()))//
						.param(Warehousing.field.getFIELD_NAME_purchasingOrderID(), String.valueOf(ws.getPurchasingOrderID()))//
						.param(Warehousing.field.getFIELD_NAME_providerID(), String.valueOf(provider.getID()))//
						.param(Warehousing.field.getFIELD_NAME_isModified(), "1")//
						.param(commIDs, commIDsStr)//
						.param(barcodeIDs, barcodeIDsStr)//
						.param(commNOs, "100,100")//
						.param(commPrices, "1.1,1.2,1.3")//
						.param(amounts, "200,200,200")//
						.param("shelfLifes", "36,36,36")//
						.contentType(MediaType.APPLICATION_JSON) //
						.session(sessionBoss) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();

		Assert.assertTrue("".equals(mr.getResponse().getContentAsString()), "action???????????????????????????");
		// ???????????????????????????
		deleteWS(ws);
		deleteWC(wc);
		BaseCommodityTest.deleteCommodityViaMapper(createCommodity1, EnumErrorCode.EC_NoError);
		BaseCommodityTest.deleteCommodityViaMapper(createCommodity2, EnumErrorCode.EC_NoError);
		BaseCommodityTest.deleteCommodityViaMapper(createCommodity3, EnumErrorCode.EC_NoError);
	}

	@Test
	public void testApproveEx14() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case14:?????????????????????int1=1????????????????????????");
		Commodity commodity1 = BaseCommodityTest.DataInput.getCommodity();
		List<List<BaseModel>> listBM1 = BaseCommodityTest.createCommodityViaMapper(BaseBO.CASE_Commodity_CreateSingle, commodity1);
		Commodity createCommodity1 = (Commodity) listBM1.get(0).get(0);
		Barcodes createBarcodes1 = (Barcodes) listBM1.get(1).get(0);
		//
		Commodity commodity2 = BaseCommodityTest.DataInput.getCommodity();
		List<List<BaseModel>> listBM2 = BaseCommodityTest.createCommodityViaMapper(BaseBO.CASE_Commodity_CreateSingle, commodity2);
		Commodity createCommodity2 = (Commodity) listBM2.get(0).get(0);
		Barcodes createBarcodes2 = (Barcodes) listBM2.get(1).get(0);
		//
		Commodity commodity3 = BaseCommodityTest.DataInput.getCommodity();
		List<List<BaseModel>> listBM3 = BaseCommodityTest.createCommodityViaMapper(BaseBO.CASE_Commodity_CreateSingle, commodity3);
		Commodity createCommodity3 = (Commodity) listBM3.get(0).get(0);
		Barcodes createBarcodes3 = (Barcodes) listBM3.get(1).get(0);
		//
		String commIDsStr = createCommodity1.getID() + "," + createCommodity2.getID() + "," + createCommodity3.getID();
		String barcodeIDsStr = createBarcodes1.getID() + "," + createBarcodes2.getID() + "," + createBarcodes3.getID() + ",";
		//
		Warehousing warehousing = BaseWarehousingTest.DataInput.getWarehousing();
		Warehousing ws = createWarehousing(warehousing);
		createCommodity1.setNO(20);
		WarehousingCommodity wc = createWarehousingCommodity(ws.getID(), createCommodity1, createBarcodes1.getID());
		//
		Provider provider = createProvider();
		//
		MvcResult mr = mvc.perform( //
				post("/warehousing/approveEx.bx") //
						.param(Warehousing.field.getFIELD_NAME_warehouseID(), String.valueOf(ws.getWarehouseID()))//
						.param(Warehousing.field.getFIELD_NAME_ID(), String.valueOf(ws.getID()))//
						.param(Warehousing.field.getFIELD_NAME_purchasingOrderID(), String.valueOf(ws.getPurchasingOrderID()))//
						.param(Warehousing.field.getFIELD_NAME_providerID(), String.valueOf(provider.getID()))//
						.param(Warehousing.field.getFIELD_NAME_isModified(), "1") //
						.param(commIDs, commIDsStr)//
						.param(barcodeIDs, barcodeIDsStr)//
						.param(commPrices, "1.1,1.2,1.3")//
						.param(amounts, "200,200,200")//
						.param("shelfLifes", "36,36,36")//
						.contentType(MediaType.APPLICATION_JSON) //
						.session(sessionBoss) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();
		Assert.assertTrue("".equals(mr.getResponse().getContentAsString()), "action???????????????????????????");
		// ???????????????????????????
		deleteWS(ws);
		deleteWC(wc);
		BaseCommodityTest.deleteCommodityViaMapper(createCommodity1, EnumErrorCode.EC_NoError);
		BaseCommodityTest.deleteCommodityViaMapper(createCommodity2, EnumErrorCode.EC_NoError);
		BaseCommodityTest.deleteCommodityViaMapper(createCommodity3, EnumErrorCode.EC_NoError);
	}

	@Test
	public void testApproveEx15() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case15:?????????????????????int1=1??????????????????ID??????");
		Commodity commodity1 = BaseCommodityTest.DataInput.getCommodity();
		List<List<BaseModel>> listBM1 = BaseCommodityTest.createCommodityViaMapper(BaseBO.CASE_Commodity_CreateSingle, commodity1);
		Commodity createCommodity1 = (Commodity) listBM1.get(0).get(0);
		Barcodes createBarcodes1 = (Barcodes) listBM1.get(1).get(0);
		//
		Commodity commodity2 = BaseCommodityTest.DataInput.getCommodity();
		List<List<BaseModel>> listBM2 = BaseCommodityTest.createCommodityViaMapper(BaseBO.CASE_Commodity_CreateSingle, commodity2);
		Commodity createCommodity2 = (Commodity) listBM2.get(0).get(0);
		Barcodes createBarcodes2 = (Barcodes) listBM2.get(1).get(0);
		//
		Commodity commodity3 = BaseCommodityTest.DataInput.getCommodity();
		List<List<BaseModel>> listBM3 = BaseCommodityTest.createCommodityViaMapper(BaseBO.CASE_Commodity_CreateSingle, commodity3);
		Commodity createCommodity3 = (Commodity) listBM3.get(0).get(0);
		Barcodes createBarcodes3 = (Barcodes) listBM3.get(1).get(0);
		//
		String commIDsStr = createCommodity1.getID() + "," + createCommodity2.getID() + "," + createCommodity2.getID();
		String barcodeIDsStr = createBarcodes1.getID() + "," + createBarcodes2.getID() + "," + createBarcodes3.getID() + ",";
		//
		Warehousing warehousing = BaseWarehousingTest.DataInput.getWarehousing();
		Warehousing ws = createWarehousing(warehousing);
		createCommodity1.setNO(20);
		WarehousingCommodity wc = createWarehousingCommodity(ws.getID(), createCommodity1, createBarcodes1.getID());
		//
		Provider provider = createProvider();
		//
		MvcResult mr = mvc.perform( //
				post("/warehousing/approveEx.bx") //
						.param(Warehousing.field.getFIELD_NAME_warehouseID(), String.valueOf(ws.getWarehouseID()))//
						.param(Warehousing.field.getFIELD_NAME_ID(), String.valueOf(ws.getID())).param(Warehousing.field.getFIELD_NAME_purchasingOrderID(), String.valueOf(ws.getPurchasingOrderID()))
						.param(Warehousing.field.getFIELD_NAME_providerID(), String.valueOf(provider.getID()))//
						.param(Warehousing.field.getFIELD_NAME_isModified(), "1")//
						.param(commIDs, commIDsStr)//
						.param(barcodeIDs, barcodeIDsStr)//
						.param(commNOs, "100,100,20")//
						.param(commPrices, "1.1,1.2,1.3")//
						.param(amounts, "200,200,200")//
						.param("shelfLifes", "36,36,36")//
						.contentType(MediaType.APPLICATION_JSON) //
						.session(sessionBoss) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();
		Assert.assertTrue("".equals(mr.getResponse().getContentAsString()), "action??????????????????????????????");
		// ???????????????????????????
		deleteWS(ws);
		deleteWC(wc);
		BaseCommodityTest.deleteCommodityViaMapper(createCommodity1, EnumErrorCode.EC_NoError);
		BaseCommodityTest.deleteCommodityViaMapper(createCommodity2, EnumErrorCode.EC_NoError);
		BaseCommodityTest.deleteCommodityViaMapper(createCommodity3, EnumErrorCode.EC_NoError);
	}

	@Test
	public void testApproveEx16() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("Case16:?????????????????????int1=1??????????????????????????????????????????????????????????????????????????????");
		// ???????????????????????????????????????LLL??????jira?????????????????????????????????????????????????????????
		// ???????????????????????????:
		// 1???????????????????????????????????????
		// 2????????????????????????????????????????????????????????????
		// 3????????????????????????????????????????????????????????????????????????????????????ID???????????????????????????ID
		// 4???????????????Warehousing???approveEx?????????????????????????????????????????????commPrices???????????????????????????????????????
		Commodity commodity1 = new Commodity();
		commodity1.setID(1);
		Commodity commodityCache1 = BaseCommodityTest.queryCommodityCache(commodity1.getID());
		Commodity commodity2 = new Commodity();
		commodity2.setID(2);
		Commodity commodityCache2 = BaseCommodityTest.queryCommodityCache(commodity2.getID());
		Commodity commodity3 = new Commodity();
		commodity3.setID(3);
		Commodity commodityCache3 = BaseCommodityTest.queryCommodityCache(commodity3.getID());
		Commodity commodity10 = new Commodity();
		commodity10.setID(10);
		Commodity commodityCache10 = BaseCommodityTest.queryCommodityCache(commodity10.getID());
		List<Commodity> commList = new ArrayList<>();
		commList.add(commodityCache1);
		commList.add(commodityCache2);
		commList.add(commodityCache3);
		commList.add(commodityCache10);
		String commIDsStr = commodity1.getID() + "," + commodity2.getID() + "," + commodity3.getID() + "," + commodity10.getID();
		// ?????????????????????barcodeID
		Barcodes barcodes = new Barcodes();
		barcodes.setOperatorStaffID(STAFF_ID3);
		barcodes.setBarcode("");
		barcodes.setCommodityID(commodityCache1.getID());
		Barcodes barcode1 = retrieveNBarcodes(barcodes);
		barcodes.setCommodityID(commodityCache2.getID());
		Barcodes barcode2 = retrieveNBarcodes(barcodes);
		barcodes.setCommodityID(commodityCache3.getID());
		Barcodes barcode3 = retrieveNBarcodes(barcodes);
		barcodes.setCommodityID(commodityCache10.getID());
		Barcodes barcode10 = retrieveNBarcodes(barcodes);
		//
		String barcodeIDsStr = barcode1.getID() + "," + barcode2.getID() + "," + barcode3.getID() + "," + barcode10.getID();
		Warehousing warehousing = BaseWarehousingTest.DataInput.getWarehousing();
		warehousing.setPurchasingOrderID(1);
		Warehousing ws = createWarehousing(warehousing);
		Commodity comm1 = (Commodity) commodityCache1.clone();
		comm1.setNO(20);
		Commodity comm2 = (Commodity) commodityCache2.clone();
		comm2.setNO(20);
		Commodity comm3 = (Commodity) commodityCache3.clone();
		comm3.setNO(20);
		Commodity comm10 = (Commodity) commodityCache10.clone();
		comm10.setNO(20);
		createWarehousingCommodity(ws.getID(), comm1, barcode1.getID());
		createWarehousingCommodity(ws.getID(), comm2, barcode2.getID());
		createWarehousingCommodity(ws.getID(), comm3, barcode3.getID());
		createWarehousingCommodity(ws.getID(), comm10, barcode10.getID());
		ws.setIsModified(1);
		Provider provider = createProvider();
		MvcResult mr = mvc.perform( //
				post("/warehousing/approveEx.bx") //
						.param(Warehousing.field.getFIELD_NAME_warehouseID(), String.valueOf(ws.getWarehouseID()))//
						.param(Warehousing.field.getFIELD_NAME_ID(), String.valueOf(ws.getID()))//
						.param(Warehousing.field.getFIELD_NAME_purchasingOrderID(), String.valueOf(ws.getPurchasingOrderID()))//
						.param(Warehousing.field.getFIELD_NAME_providerID(), String.valueOf(provider.getID()))//
						.param(Warehousing.field.getFIELD_NAME_isModified(), String.valueOf(ws.getIsModified()))//
						.param(commIDs, commIDsStr)//
						.param(barcodeIDs, barcodeIDsStr)//
						.param(commNOs, "100,100,20,20")//
						.param(commPrices, "1.1,1.2,1.3,3")//
						.param(amounts, "200,200,200,3")//
						.contentType(MediaType.APPLICATION_JSON) //
						.session(sessionBoss) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();
		// TODO dev???nbr?????????openid????????????null???????????????????????????????????????openid???null?????????????????????no_Error
//		???????????????openid??????null???????????????????????????????????????partSuccess
//		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_PartSuccess); 
		Shared.checkJSONErrorCode(mr);
		WarehousingCP.verifyApprove(mr, ws, warehousingBO, purchasingOrderBO, commList, commodityHistoryBO, messageBO, Shared.DBName_Test);
		// // ??????????????????
		// JSONObject object =
		// JSONObject.fromObject(mr.getResponse().getContentAsString());
		// String msg = (String) object.get(BaseAction.KEY_HTMLTable_Parameter_msg);
		// Assert.assertTrue(msg.contains("????????????????????????!"), "???????????????????????????????????????????????????"); //
		// ?????????????????????????????????????????????????????????DB????????????openid???????????????????????????????????????
	}

	@Test
	public void testApproveEx17() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("Case17:??????????????????????????????int1=0, ???????????????????????????????????????????????????purchasingOrderSN");
		Warehousing warehousing = BaseWarehousingTest.DataInput.getWarehousing();
		warehousing.setPurchasingOrderID(1);
		Warehousing ws = createWarehousing(warehousing);
		// ??????????????????
		Commodity commodity1 = new Commodity();
		commodity1.setID(1);
		Commodity commodityCache1 = BaseCommodityTest.queryCommodityCache(commodity1.getID());
		Barcodes barcodes = new Barcodes();
		barcodes.setOperatorStaffID(STAFF_ID3);
		barcodes.setBarcode("");
		barcodes.setCommodityID(commodityCache1.getID());
		Barcodes barcode1 = retrieveNBarcodes(barcodes);
		Commodity comm1 = (Commodity) commodityCache1.clone();
		comm1.setNO(20);
		WarehousingCommodity wc = createWarehousingCommodity(ws.getID(), comm1, barcode1.getID());
		//
		List<Commodity> commList = new ArrayList<>();
		commList.add(commodityCache1);

		MvcResult mr = mvc.perform( //
				post("/warehousing/approveEx.bx") //
						.param(Warehousing.field.getFIELD_NAME_warehouseID(), String.valueOf(ws.getWarehouseID()))//
						.param(Warehousing.field.getFIELD_NAME_ID(), String.valueOf(ws.getID())).param(Warehousing.field.getFIELD_NAME_purchasingOrderID(), String.valueOf(ws.getPurchasingOrderID()))
						.param(Warehousing.field.getFIELD_NAME_isModified(), "0")//
						.param(commIDs, String.valueOf(wc.getCommodityID()))//
						.param(barcodeIDs, String.valueOf(wc.getBarcodeID()))//
						.param(commNOs, String.valueOf(wc.getNO()))//
						.param(commPrices, String.valueOf(wc.getPrice()))//
						.param(amounts, String.valueOf(wc.getAmount()))//
						.param("shelfLifes", String.valueOf(wc.getShelfLife()))//
						.contentType(MediaType.APPLICATION_JSON) //
						.session(sessionBoss) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();
		// TODO dev???nbr?????????openid????????????null???????????????????????????????????????openid???null?????????????????????no_Error
//		???????????????openid??????null???????????????????????????????????????partSuccess
//		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_PartSuccess);
		Shared.checkJSONErrorCode(mr);
		WarehousingCP.verifyApprove(mr, ws, warehousingBO, purchasingOrderBO, commList, commodityHistoryBO, messageBO, Shared.DBName_Test);
		// ??????????????????
		// JSONObject object =
		// JSONObject.fromObject(mr.getResponse().getContentAsString());
		// String msg = (String) object.get(BaseAction.KEY_HTMLTable_Parameter_msg);
		// Assert.assertTrue(msg.contains("????????????????????????!"), "???????????????????????????????????????????????????");
		// // ?????????????????????????????????????????????????????????DB????????????openid???????????????????????????????????????
		//
		String json = mr.getResponse().getContentAsString();
		JSONObject jsonObject = JSONObject.fromObject(json);
		JSONObject wsJson = jsonObject.getJSONObject(BaseAction.KEY_Object);
		// ??????????????????
		List<?> wCommodityList = wsJson.getJSONArray(Warehousing.field.getFIELD_NAME_listSlave1());
		for (Object o : wCommodityList) {
			assertTrue(JSONObject.fromObject(o).getInt(WarehousingCommodity.field.getFIELD_NAME_warehousingID()) == wsJson.getInt(Warehousing.field.getFIELD_NAME_ID()));
		}
		//
		PurchasingOrder pOrder = new PurchasingOrder();
		pOrder.setID(wsJson.getInt(Warehousing.field.getFIELD_NAME_purchasingOrderID()));
		Map<String, Object> paramsPOrderR1 = pOrder.getRetrieve1Param(BaseBO.INVALID_CASE_ID, pOrder);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<List<BaseModel>> bmPOrderList = purchasingOrderMapper.retrieve1Ex(paramsPOrderR1);
		PurchasingOrder purchasingOrderR1 = (PurchasingOrder) bmPOrderList.get(0).get(0);
		Assert.assertTrue(wsJson.getString(Warehousing.field.getFIELD_NAME_purchasingOrderSN()).equals(purchasingOrderR1.getSn()), "??????????????????????????????????????????????????????");
	}

	@Test
	public void testApproveEx18() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case18:???????????????????????????????????????????????????????????????"); // int1???1?????????????????????
		Commodity commodity1 = BaseCommodityTest.DataInput.getCommodity();
		List<List<BaseModel>> listBM1 = BaseCommodityTest.createCommodityViaMapper(BaseBO.CASE_Commodity_CreateSingle, commodity1);
		Commodity createCommodity1 = (Commodity) listBM1.get(0).get(0);
		Barcodes createBarcodes1 = (Barcodes) listBM1.get(1).get(0);
		//
		Warehousing warehousing = BaseWarehousingTest.DataInput.getWarehousing();
		Warehousing ws = createWarehousing(warehousing);
		createCommodity1.setNO(20);
		WarehousingCommodity wc = createWarehousingCommodity(ws.getID(), createCommodity1, createBarcodes1.getID());
		//
		MvcResult mr = mvc.perform( //
				post("/warehousing/approveEx.bx") //
						.param(Warehousing.field.getFIELD_NAME_warehouseID(), String.valueOf(ws.getWarehouseID()))//
						.param(Warehousing.field.getFIELD_NAME_ID(), String.valueOf(ws.getID())) //
						.param(Warehousing.field.getFIELD_NAME_purchasingOrderID(), String.valueOf(ws.getPurchasingOrderID()))//
						.param(Warehousing.field.getFIELD_NAME_providerID(), "1")//
						.param(Warehousing.field.getFIELD_NAME_isModified(), "1")//
						.contentType(MediaType.APPLICATION_JSON) //
						.session(sessionBoss) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();

		String json = mr.getResponse().getContentAsString();
		Assert.assertTrue(json.length() == 0, "CASE18?????????????????????????????????????????????");
		// ???????????????????????????
		deleteWS(ws);
		deleteWC(wc);
		BaseCommodityTest.deleteCommodityViaMapper(createCommodity1, EnumErrorCode.EC_NoError);
	}

	@Test
	public void testApproveEx19() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("Case19:????????????????????????????????????"); // int1???0???????????????????????????
		Warehousing warehousing = BaseWarehousingTest.DataInput.getWarehousing();
		Warehousing ws = createWarehousing(warehousing);
		MvcResult mr = mvc.perform( //
				post("/warehousing/approveEx.bx") //
						.param(Warehousing.field.getFIELD_NAME_ID(), String.valueOf(ws.getID()))//
						.param(Warehousing.field.getFIELD_NAME_isModified(), "0")//
						.contentType(MediaType.APPLICATION_JSON) //
						.session(sessionBoss) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();

		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_BusinessLogicNotDefined);
		//
		deleteWS(ws);
	}

	@Test
	public void testApproveEx20() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("Case20:???????????????????????????"); // int1???1?????????????????????
		MvcResult mr = mvc.perform( //
				post("/warehousing/approveEx.bx") //
						.param(Warehousing.field.getFIELD_NAME_ID(), String.valueOf(Shared.BIG_ID))//
						.param(Warehousing.field.getFIELD_NAME_isModified(), "0")//
						.contentType(MediaType.APPLICATION_JSON) //
						.session(sessionBoss) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();

		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_BusinessLogicNotDefined);
	}

	@Test
	public void testApproveEx21() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("Case21:???????????????????????????(???????????????)"); // int1???1?????????????????????
		MvcResult mr = mvc.perform( //
				post("/warehousing/approveEx.bx") //
						.param(Warehousing.field.getFIELD_NAME_ID(), String.valueOf(Shared.BIG_ID))//
						.param(Warehousing.field.getFIELD_NAME_isModified(), "1")//
						.contentType(MediaType.APPLICATION_JSON) //
						.session(sessionBoss) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();

		String json = mr.getResponse().getContentAsString();
		Assert.assertTrue(json.length() == 0, "CASE21?????????????????????????????????????????????");
	}

	@Test
	public void testApproveEx22() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("Case22:?????????????????????int1=1????????????????????????????????????????????????????????????" + FieldFormat.MAX_OneCommodityNO + "???????????????");
		Commodity commodity1 = BaseCommodityTest.DataInput.getCommodity();
		List<List<BaseModel>> listBM1 = BaseCommodityTest.createCommodityViaMapper(BaseBO.CASE_Commodity_CreateSingle, commodity1);
		Commodity createCommodity1 = (Commodity) listBM1.get(0).get(0);
		Barcodes createBarcodes1 = (Barcodes) listBM1.get(1).get(0);
		//
		Commodity commodity2 = BaseCommodityTest.DataInput.getCommodity();
		List<List<BaseModel>> listBM2 = BaseCommodityTest.createCommodityViaMapper(BaseBO.CASE_Commodity_CreateSingle, commodity2);
		Commodity createCommodity2 = (Commodity) listBM2.get(0).get(0);
		Barcodes createBarcodes2 = (Barcodes) listBM2.get(1).get(0);
		//
		Commodity commodity3 = BaseCommodityTest.DataInput.getCommodity();
		List<List<BaseModel>> listBM3 = BaseCommodityTest.createCommodityViaMapper(BaseBO.CASE_Commodity_CreateSingle, commodity3);
		Commodity createCommodity3 = (Commodity) listBM3.get(0).get(0);
		Barcodes createBarcodes3 = (Barcodes) listBM3.get(1).get(0);
		// ????????????????????????????????????F_NO????????????????????????????????????F_NO??????????????????
		List<Commodity> commList = new ArrayList<>();
		commList.add(createCommodity1);
		commList.add(createCommodity2);
		commList.add(createCommodity3);
		//
		String commIDsStr = createCommodity1.getID() + "," + createCommodity2.getID() + "," + createCommodity3.getID();
		String barcodeIDsStr = createBarcodes1.getID() + "," + createBarcodes2.getID() + "," + createBarcodes3.getID() + ",";
		Warehousing warehousing = BaseWarehousingTest.DataInput.getWarehousing();
		Warehousing ws = createWarehousing(warehousing);
		createCommodity1.setNO(20);
		createCommodity2.setNO(20);
		createCommodity3.setNO(20);
		createWarehousingCommodity(ws.getID(), createCommodity1, createBarcodes1.getID());
		createWarehousingCommodity(ws.getID(), createCommodity2, createBarcodes2.getID());
		createWarehousingCommodity(ws.getID(), createCommodity3, createBarcodes3.getID());
		createCommodity1.setNO(0);
		createCommodity2.setNO(0);
		createCommodity3.setNO(0);
		//
		ws.setIsModified(1);
		Provider provider = createProvider();
		MvcResult mr = mvc.perform( //
				post("/warehousing/approveEx.bx") //
						.param(Warehousing.field.getFIELD_NAME_warehouseID(), String.valueOf(ws.getWarehouseID()))//
						.param(Warehousing.field.getFIELD_NAME_ID(), String.valueOf(ws.getID()))//
						.param(Warehousing.field.getFIELD_NAME_purchasingOrderID(), String.valueOf(ws.getPurchasingOrderID()))//
						.param(Warehousing.field.getFIELD_NAME_providerID(), String.valueOf(provider.getID()))//
						.param(Warehousing.field.getFIELD_NAME_isModified(), String.valueOf(ws.getIsModified()))//
						.param(commIDs, commIDsStr)//
						.param(barcodeIDs, barcodeIDsStr)//
						.param(commNOs, "100," + (FieldFormat.MAX_OneCommodityNO + 1) + ",20")//
						// .param(commNOs, "100,100,300")//
						.param(commPrices, "1,2,3")//
						.param(amounts, "100,20002,60")//
						.contentType(MediaType.APPLICATION_JSON) //
						.session(sessionBoss) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_WrongFormatForInputField);
	}

	@Test
	public void testApproveEx23() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("Case23:?????????????????????int1=1????????????????????????????????????????????????????????????" + FieldFormat.MAX_OneCommodityPrice + "???????????????");
		Commodity commodity1 = BaseCommodityTest.DataInput.getCommodity();
		List<List<BaseModel>> listBM1 = BaseCommodityTest.createCommodityViaMapper(BaseBO.CASE_Commodity_CreateSingle, commodity1);
		Commodity createCommodity1 = (Commodity) listBM1.get(0).get(0);
		Barcodes createBarcodes1 = (Barcodes) listBM1.get(1).get(0);
		// ????????????????????????????????????F_NO????????????????????????????????????F_NO??????????????????
		List<Commodity> commList = new ArrayList<>();
		commList.add(createCommodity1);
		//
		String commIDsStr = String.valueOf(createCommodity1.getID());
		String barcodeIDsStr = String.valueOf(createBarcodes1.getID());
		Warehousing warehousing = BaseWarehousingTest.DataInput.getWarehousing();
		Warehousing ws = createWarehousing(warehousing);
		createCommodity1.setNO(20);
		createWarehousingCommodity(ws.getID(), createCommodity1, createBarcodes1.getID());
		createCommodity1.setNO(0);
		//
		Provider provider = createProvider();
		MvcResult mr = mvc.perform( //
				post("/warehousing/approveEx.bx") //
						.param(Warehousing.field.getFIELD_NAME_warehouseID(), String.valueOf(ws.getWarehouseID()))//
						.param(Warehousing.field.getFIELD_NAME_ID(), String.valueOf(ws.getID()))//
						.param(Warehousing.field.getFIELD_NAME_purchasingOrderID(), String.valueOf(ws.getPurchasingOrderID()))//
						.param(Warehousing.field.getFIELD_NAME_providerID(), String.valueOf(provider.getID()))//
						.param(Warehousing.field.getFIELD_NAME_isModified(), String.valueOf(EnumBoolean.EB_Yes.getIndex()))//
						.param(commIDs, commIDsStr)//
						.param(barcodeIDs, barcodeIDsStr)//
						.param(commNOs, "100")//
						// .param(commNOs, "100,100,300")//
						.param(commPrices, String.valueOf(FieldFormat.MAX_OneCommodityPrice + 0.001d))//
						.param(amounts, "1000100")//
						.contentType(MediaType.APPLICATION_JSON) //
						.session(sessionBoss) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_WrongFormatForInputField);
	}

	@Test
	public void testApproveEx24() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case24:???????????????????????????????????????");
		Commodity commodity1 = BaseCommodityTest.DataInput.getCommodity();
		List<List<BaseModel>> listBM1 = BaseCommodityTest.createCommodityViaMapper(BaseBO.CASE_Commodity_CreateSingle, commodity1);
		Commodity createCommodity1 = (Commodity) listBM1.get(0).get(0);
		Barcodes createBarcodes1 = (Barcodes) listBM1.get(1).get(0);
		//
		String commIDsStr = String.valueOf(createCommodity1.getID());
		String barcodeIDsStr = String.valueOf(createBarcodes1.getID());
		Warehousing warehousing = BaseWarehousingTest.DataInput.getWarehousing();
		Warehousing ws = createWarehousing(warehousing);
		createCommodity1.setNO(20);
		createWarehousingCommodity(ws.getID(), createCommodity1, createBarcodes1.getID());
		//
		Provider provider = createProvider();
		MvcResult mr = mvc.perform( //
				post("/warehousing/approveEx.bx") //
						.param(Warehousing.field.getFIELD_NAME_warehouseID(), String.valueOf(ws.getWarehouseID()))//
						.param(Warehousing.field.getFIELD_NAME_ID(), String.valueOf(ws.getID()))//
						.param(Warehousing.field.getFIELD_NAME_purchasingOrderID(), String.valueOf(ws.getPurchasingOrderID()))//
						.param(Warehousing.field.getFIELD_NAME_providerID(), String.valueOf(provider.getID()))//
						.param(Warehousing.field.getFIELD_NAME_isModified(), String.valueOf(EnumBoolean.EB_Yes.getIndex()))//
						.param(commIDs, commIDsStr + ",52")//
						.param(barcodeIDs, barcodeIDsStr + ",1")//
						.param(commNOs, "100,100")//
						.param(commPrices, "100,100")//
						.param(amounts, "200,200")//
						.contentType(MediaType.APPLICATION_JSON) //
						.session(sessionBoss) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_BusinessLogicNotDefined);
	}

	@Test
	public void testApproveEx25() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE25: ????????????????????????????????????????????????????????????????????????????????????");
		// ???????????????????????????checkCreate?????????????????????????????????????????????????????????EC_PartSuccess????????????????????????????????????????????????DB?????????????????????
	}
	
	@Test
	public void testApproveEx26() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("Case26:??????A??????????????????????????????B?????????????????????????????????????????????????????????");
		Commodity commodity1 = BaseCommodityTest.DataInput.getCommodity();
		List<List<BaseModel>> listBM1 = BaseCommodityTest.createCommodityViaMapper(BaseBO.CASE_Commodity_CreateSingle, commodity1);
		Commodity createCommodity1 = (Commodity) listBM1.get(0).get(0);
		Barcodes createBarcodes1 = (Barcodes) listBM1.get(1).get(0);
		//
		List<Commodity> commList = new ArrayList<>();
		commList.add(createCommodity1);
		//
		Warehousing warehousing = BaseWarehousingTest.DataInput.getWarehousing();
		Warehousing ws = createWarehousing(warehousing);
		createCommodity1.setNO(20);
		createWarehousingCommodity(ws.getID(), createCommodity1, createBarcodes1.getID());
		createCommodity1.setNO(0);
		// ????????????B???????????????
		Shop shopGet = BaseShopTest.DataInput.getShop();
		Shop shopCreated = BaseShopTest.createViaCompanyAction(shopGet, mvc, mapBO, sessionOP, EnumErrorCode.EC_NoError);
		// ????????????????????????
		String Staff_Phone = Shared.getValidStaffPhone();
		MvcResult ret = BaseStaffTest.getToken(sessionBoss, mvc, Staff_Phone, Staff.FOR_CreateNewStaff);
		//
		String encrypt = Shared.encrypt(ret, Shared.PASSWORD_DEFAULT);
		Staff staff = BaseStaffTest.DataInput.getStaff();
		staff.setShopID(shopCreated.getID());
		staff.setName("??????" + Shared.generateCompanyName(6));
		staff.setRoleID(EnumTypeRole.ETR_Boss.getIndex());
		staff.setPhone(Staff_Phone);
		staff.setPwdEncrypted(encrypt);
		MvcResult mrStaffCreate = mvc.perform(//
				BaseStaffTest.DataInput.getBuilder("/staff/createEx.bx", MediaType.APPLICATION_JSON, staff)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		// ??????????????????????????????
		Shared.checkJSONErrorCode(mrStaffCreate);
		Staff staffCopy = (Staff) staff.clone(); // ??????????????????????????????pos??????????????????????????????????????????????????????????????????copy??????????????????????????????????????????
		StaffCP.verifyCreate(mrStaffCreate, staffCopy, staffRoleBO, Shared.DBName_Test);
		staff = (Staff) staffCopy.clone(); // ????????????
		//
		JSONObject object = JSONObject.fromObject(mrStaffCreate.getResponse().getContentAsString());
		Staff staff2 = new Staff();
		staff2 = (Staff) staff2.parse1(object.getString(BaseAction.KEY_Object));
		Assert.assertTrue(staff2 != null, "????????????");
		// ??????
		MockHttpSession sessionNewShopBoss = Shared.getStaffLoginSession(mvc, staff2.getPhone(), Shared.PASSWORD_DEFAULT, Shared.DB_SN_Test);
		MvcResult mr = mvc.perform( //
				post("/warehousing/approveEx.bx") //
						.param(Warehousing.field.getFIELD_NAME_warehouseID(), String.valueOf(ws.getWarehouseID()))//
						.param(Warehousing.field.getFIELD_NAME_ID(), String.valueOf(ws.getID()))//
						.param(Warehousing.field.getFIELD_NAME_purchasingOrderID(), String.valueOf(ws.getPurchasingOrderID()))//
						.param(Warehousing.field.getFIELD_NAME_isModified(), "0")//
						.contentType(MediaType.APPLICATION_JSON) //
						.session(sessionNewShopBoss) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_BusinessLogicNotDefined);
	}
	
	@Test
	public void testApproveEx27() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("Case27:??????A??????????????????????????????B(????????????)??????????????????????????????????????????????????????????????????????????????????????????");
		Commodity commodity1 = BaseCommodityTest.DataInput.getCommodity();
		List<List<BaseModel>> listBM1 = BaseCommodityTest.createCommodityViaMapper(BaseBO.CASE_Commodity_CreateSingle, commodity1);
		Commodity createCommodity1 = (Commodity) listBM1.get(0).get(0);
		Barcodes createBarcodes1 = (Barcodes) listBM1.get(1).get(0);
		//
		List<Commodity> commList = new ArrayList<>();
		commList.add(createCommodity1);
		//
		Warehousing warehousing = BaseWarehousingTest.DataInput.getWarehousing();
		Warehousing ws = createWarehousing(warehousing);
		createCommodity1.setNO(20);
		createWarehousingCommodity(ws.getID(), createCommodity1, createBarcodes1.getID());
		createCommodity1.setNO(0);
		// ???????????????????????????
		String Staff_Phone = Shared.getValidStaffPhone();
		MvcResult ret = BaseStaffTest.getToken(sessionBoss, mvc, Staff_Phone, Staff.FOR_CreateNewStaff);
		//
		String encrypt = Shared.encrypt(ret, Shared.PASSWORD_DEFAULT);
		Staff staff = BaseStaffTest.DataInput.getStaff();
		staff.setShopID(BaseAction.VirtualShopID);
		staff.setName("??????" + Shared.generateCompanyName(6));
		staff.setRoleID(EnumTypeRole.ETR_Boss.getIndex());
		staff.setPhone(Staff_Phone);
		staff.setPwdEncrypted(encrypt);
		MvcResult mrStaffCreate = mvc.perform(//
				BaseStaffTest.DataInput.getBuilder("/staff/createEx.bx", MediaType.APPLICATION_JSON, staff)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		// ??????????????????????????????
		Shared.checkJSONErrorCode(mrStaffCreate);
		Staff staffCopy = (Staff) staff.clone(); // ??????????????????????????????pos??????????????????????????????????????????????????????????????????copy??????????????????????????????????????????
		StaffCP.verifyCreate(mrStaffCreate, staffCopy, staffRoleBO, Shared.DBName_Test);
		staff = (Staff) staffCopy.clone(); // ????????????
		//
		JSONObject object = JSONObject.fromObject(mrStaffCreate.getResponse().getContentAsString());
		Staff staff2 = new Staff();
		staff2 = (Staff) staff2.parse1(object.getString(BaseAction.KEY_Object));
		Assert.assertTrue(staff2 != null, "????????????");
		// ??????
		MockHttpSession sessionNewShopBoss = Shared.getStaffLoginSession(mvc, staff2.getPhone(), Shared.PASSWORD_DEFAULT, Shared.DB_SN_Test);
		MvcResult mr = mvc.perform( //
				post("/warehousing/approveEx.bx") //
						.param(Warehousing.field.getFIELD_NAME_warehouseID(), String.valueOf(ws.getWarehouseID()))//
						.param(Warehousing.field.getFIELD_NAME_ID(), String.valueOf(ws.getID()))//
						.param(Warehousing.field.getFIELD_NAME_purchasingOrderID(), String.valueOf(ws.getPurchasingOrderID()))//
						.param(Warehousing.field.getFIELD_NAME_isModified(), "0")//
						.contentType(MediaType.APPLICATION_JSON) //
						.session(sessionNewShopBoss) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();
		Shared.checkJSONErrorCode(mr);
	}

	// @Test
	// public void testApproveListEx() throws Exception {
	// Shared.printTestMethodStartInfo();
	//
	// System.out.println("------------------------------Case1:??????1???????????????------------------------");
	// Warehousing bm = createWS();
	//
	// MvcResult mr = (MvcResult) mvc.perform( //
	// get("/warehousing/approveListEx.bx?WSListID=" + bm.getID()) //
	// .contentType(MediaType.APPLICATION_JSON) //
	// .session((MockHttpSession) Shared.getStaffLoginSession(mvc,
	// Shared.PhoneOfBoss)) //
	// ) //
	// .andExpect(status().isOk()) //
	// .andDo(print()) //
	// .andReturn();
	// Shared.checkJSONErrorCode(mr);
	//
	// System.out.println("-----------------------------------Case2:????????????????????????------------------------------");
	// Warehousing bm1 = createWS();
	// Warehousing bm2 = createWS();
	//
	// MvcResult mr2 = (MvcResult) mvc.perform( //
	// get("/warehousing/approveListEx.bx?WSListID=" + bm1.getID() + "," +
	// bm2.getID()) //
	// .contentType(MediaType.APPLICATION_JSON) //
	// .session((MockHttpSession) Shared.getStaffLoginSession(mvc,
	// Shared.PhoneOfBoss)) //
	// ) //
	// .andExpect(status().isOk()) //
	// .andDo(print()) //
	// .andReturn();
	// Shared.checkJSONErrorCode(mr2);
	//
	// System.out.println("-----------------------------------Case3:????????????????????????????????????------------------------------");
	// String msg1 = "?????????" + 4 + "?????????????????????????????????????????????<br />";
	//
	// MvcResult mr3 = mvc.perform( //
	// get("/warehousing/approveListEx.bx?WSListID=4") //
	// .session((MockHttpSession) Shared.getStaffLoginSession(mvc,
	// Shared.PhoneOfBoss)) //
	// ) //
	// .andExpect(status().isOk()) //
	// .andDo(print()) //
	// .andReturn();
	//
	// Shared.checkJSONErrorCode(mr3, EnumErrorCode.EC_PartSuccess);
	// Shared.checkJSONMsg(mr3, msg1, "????????????????????????");
	//
	// System.out.println("-----------------------------------Case4:????????????????????????????????????------------------------------");
	// String msg2 = "?????????" + 11 + "?????????????????????????????????????????????<br />";
	//
	// MvcResult mr4 = mvc.perform( //
	// get("/warehousing/approveListEx.bx?WSListID=4,11") //
	// .session((MockHttpSession) Shared.getStaffLoginSession(mvc,
	// Shared.PhoneOfBoss)) //
	// ) //
	// .andExpect(status().isOk()) //
	// .andDo(print()) //
	// .andReturn();
	//
	// Shared.checkJSONErrorCode(mr4, EnumErrorCode.EC_PartSuccess);
	// Shared.checkJSONMsg(mr4, msg1 + msg2, "????????????????????????");
	//
	// System.out.println("------------------------------Case5:??????0????????????-----------------------");
	// MvcResult mr5 = mvc.perform( //
	// get("/warehousing/approveListEx.bx") //
	// .session((MockHttpSession) Shared.getStaffLoginSession(mvc,
	// Shared.PhoneOfBoss)) //
	// ) //
	// .andExpect(status().isOk()) //
	// .andDo(print()) //
	// .andReturn();
	//
	// String json3 = mr5.getResponse().getContentAsString();
	// assertTrue(json3.equals(""));
	//
	// System.out.println("-----------------------case6????????????????????????????????????---------------");
	// try {
	// mvc.perform( //
	// get("/warehousing/approveListEx.bx?WSListID=" +
	// BaseWarehousingTest.INVALID_ID) //
	// .contentType(MediaType.APPLICATION_JSON) //
	// .session((MockHttpSession) Shared.getStaffLoginSession(mvc,
	// Shared.PhoneOfBoss)) //
	// ) //
	// .andExpect(status().isOk()) //
	// .andDo(print()) //
	// .andReturn();
	// } catch (Exception e) {
	// System.out.println(e.getMessage());// ???????????????????????????????????????
	// assertTrue(e.getMessage().equals("Request processing failed; nested exception
	// is java.lang.NullPointerException"));
	// }
	//
	// System.out.println("-----------------------------------Case7:????????????????????????????????????????????????------------------------------");
	// Warehousing wc4 = createWS();
	// MvcResult mr7 = mvc.perform( //
	// get("/warehousing/approveListEx.bx?WSListID=4" + "," + wc4.getID()) //
	// .session((MockHttpSession) Shared.getStaffLoginSession(mvc,
	// Shared.PhoneOfBoss)) //
	// ) //
	// .andExpect(status().isOk()) //
	// .andDo(print()) //
	// .andReturn();
	//
	// // Shared.checkJSONMsg(mr7, msg1, "????????????????????????");
	// Shared.checkJSONErrorCode(mr7, EnumErrorCode.EC_PartSuccess);
	//
	// System.out.println("-----------------------------------Case8:????????????????????????------------------------------");
	// Warehousing wc5 = createWS();
	//
	// MvcResult mr8 = mvc.perform( //
	// get("/warehousing/approveListEx.bx?WSListID=" + wc5.getID()) //
	// .session((MockHttpSession) Shared.getStaffLoginSession(mvc,
	// Shared.PhoneOfCashier)) //
	// ) //
	// .andExpect(status().isOk()) //
	// .andDo(print()) //
	// .andReturn(); //
	//
	// Shared.checkJSONErrorCode(mr8, EnumErrorCode.EC_PartSuccess);
	//
	// System.out.println("-Case9:????????????????????????,?????????????????????NULL???????????????????????????????????????,????????????param????????????=EnumErrorCode.EC_NoError???MSG=??????????????????????????????????????????--");
	// // ????????????????????????????????????????????????????????????
	// WarehousingMapper mapper = (WarehousingMapper)
	// applicationContext.getBean("warehousingMapper");
	// Warehousing wsCreate91 = BaseWarehousingTest.DataInput.getWarehousing();
	// wsCreate91.setPurchasingOrderID(0);
	// wsCreate91.setProviderID(1);
	// Map<String, Object> paramsForCreate91 =
	// wsCreate91.getCreateParam(BaseBO.INVALID_CASE_ID, wsCreate91);
	// DataSourceContextHolder.setDbName(Shared.DBName_Test);
	// Warehousing wsCreated91 = (Warehousing) mapper.create(paramsForCreate91); //
	// ...
	// assertTrue(wsCreated91 != null &&
	// EnumErrorCode.values()[Integer.parseInt(paramsForCreate91.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())]
	// == EnumErrorCode.EC_NoError, "??????????????????");
	// //
	// wsCreate91.setIgnoreIDInComparision(true);
	// if (wsCreate91.compareTo(wsCreated91) != 0) {
	// Assert.assertTrue(false, "???????????????????????????DB??????????????????");
	// }
	//
	// Warehousing wsCreate92 = BaseWarehousingTest.DataInput.getWarehousing();
	// wsCreate92.setPurchasingOrderID(0);
	// wsCreate92.setProviderID(1);
	// Map<String, Object> paramsForCreate92 =
	// wsCreate92.getCreateParam(BaseBO.INVALID_CASE_ID, wsCreate92);
	// DataSourceContextHolder.setDbName(Shared.DBName_Test);
	// Warehousing wsCreated92 = (Warehousing) mapper.create(paramsForCreate92); //
	// ...
	// assertTrue(wsCreated92 != null &&
	// EnumErrorCode.values()[Integer.parseInt(paramsForCreate92.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())]
	// == EnumErrorCode.EC_NoError, "??????????????????");
	// //
	// wsCreate92.setIgnoreIDInComparision(true);
	// if (wsCreate92.compareTo(wsCreated92) != 0) {
	// Assert.assertTrue(false, "???????????????????????????DB??????????????????");
	// }
	//
	// MvcResult mr9 = (MvcResult) mvc.perform( //
	// get("/warehousing/approveListEx.bx?WSListID=" + wsCreated91.getID() + "," +
	// wsCreated92.getID()) //
	// .contentType(MediaType.APPLICATION_JSON) //
	// .session((MockHttpSession) Shared.getStaffLoginSession(mvc,
	// Shared.PhoneOfBoss)) //
	// ) //
	// .andExpect(status().isOk()) //
	// .andDo(print()) //
	// .andReturn();
	// String msg91 = "ID???" + wsCreated91.getID() + "???????????????????????????????????????";
	// String msg92 = "ID???" + wsCreated92.getID() + "???????????????????????????????????????";
	// Shared.checkJSONErrorCode(mr9);
	// Shared.checkJSONMsg(mr9, msg91 + "<br />" + msg92 + "<br />",
	// "?????????msg?????????????????????");
	//
	// System.out.println("-Case10:????????????????????????,?????????????????????NULL???????????????????????????????????????,????????????param????????????=EnumErrorCode.EC_NoError???MSG=??????????????????????????????????????????--");
	// // ?????????????????????????????????????????????????????????????????????????????????????????????????????????
	// Warehousing wsCreated101 = createWS();
	// Warehousing wsCreate102 = BaseWarehousingTest.DataInput.getWarehousing();
	// wsCreate102.setPurchasingOrderID(0);
	// wsCreate102.setProviderID(1);
	// Map<String, Object> paramsForCreate102 =
	// wsCreate102.getCreateParam(BaseBO.INVALID_CASE_ID, wsCreate102);
	// DataSourceContextHolder.setDbName(Shared.DBName_Test);
	// Warehousing wsCreated102 = (Warehousing) mapper.create(paramsForCreate102);
	// // ...
	// assertTrue(wsCreated102 != null &&
	// EnumErrorCode.values()[Integer.parseInt(paramsForCreate102.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())]
	// == EnumErrorCode.EC_NoError, "??????????????????");
	// //
	// wsCreate102.setIgnoreIDInComparision(true);
	// if (wsCreate102.compareTo(wsCreated102) != 0) {
	// Assert.assertTrue(false, "???????????????????????????DB??????????????????");
	// }
	//
	// MvcResult mr10 = (MvcResult) mvc.perform( //
	// get("/warehousing/approveListEx.bx?WSListID=" + wsCreated101.getID() + "," +
	// wsCreated102.getID()) //
	// .contentType(MediaType.APPLICATION_JSON) //
	// .session((MockHttpSession) Shared.getStaffLoginSession(mvc,
	// Shared.PhoneOfBoss)) //
	// ) //
	// .andExpect(status().isOk()) //
	// .andDo(print()) //
	// .andReturn();
	// Shared.checkJSONErrorCode(mr10);
	// String msg101 = "??????ID??????" + wsCreated101.getID() + "?????????????????? ???";
	// String msg102 = "ID???" + wsCreated102.getID() + "???????????????????????????????????????";
	// Shared.checkJSONMsg(mr10, msg101 + "<br />" + msg102 + "<br />",
	// "?????????msg?????????????????????");
	// }

	@Test
	public void testRetrieveNByFieldsEx1() throws Exception {
		// ???warehousing/retrieveNByFieldsEx.bx???????????????????????????????????????????????????
		Shared.printTestMethodStartInfo();
		Warehousing warehousing = BaseWarehousingTest.DataInput.getWarehousing();
		Warehousing ws = createWarehousing(warehousing);
		SimpleDateFormat sdf = new SimpleDateFormat(BaseAction.DATE_FORMAT_RetailTradeSN);
		String SN = "RK" + sdf.format(new Date()).substring(0, 8);
		Shared.caseLog("case1???????????????(10)???????????????????????????????????????");
		//
		MvcResult mr = mvc.perform( //
				post("/warehousing/retrieveNByFieldsEx.bx") //
						.param(Warehousing.field.getFIELD_NAME_queryKeyword(), SN) //
						.param(Warehousing.field.getFIELD_NAME_status(), String.valueOf(BaseAction.INVALID_ID)) //
						.contentType(MediaType.APPLICATION_JSON) //
						.session(sessionBoss) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();
		Shared.checkJSONErrorCode(mr);

		JSONObject o = JSONObject.fromObject(mr.getResponse().getContentAsString()); //
		List<Integer> list = JsonPath.read(o, "$.warehousingList[*].ID");

		boolean flag = false;
		for (int i = 0; i < list.size(); i++) {
			System.out.println(list.get(i));
			int i1 = list.get(i);
			if (ws.getID() == i1) {
				flag = true;
				break;
			}
		}
		Assert.assertTrue(flag);
	}

	// ??????warehousing/retrieveNByFieldsEx.bx??????????????????????????????????????????????????????????????????
	// @Test
	public void testRetrieveNByFieldsEx2() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("case2??????????????????????????????????????????");
		//
		MvcResult mr2 = mvc.perform( //
				post("/warehousing/retrieveNByFieldsEx.bx") //
						.param(Warehousing.field.getFIELD_NAME_queryKeyword(), "A??????18") //
						.param(Warehousing.field.getFIELD_NAME_status(), String.valueOf(BaseAction.INVALID_ID)) //
						.contentType(MediaType.APPLICATION_JSON) //
						.session(sessionBoss) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();
		Shared.checkJSONErrorCode(mr2);

		JSONObject o1 = JSONObject.fromObject(mr2.getResponse().getContentAsString()); //
		List<?> list1 = JsonPath.read(o1, "$.warehousingList[*].listCommodity[*].name");

		String string = "A??????18";
		boolean b = false;
		for (int i = 0; i < list1.size(); i++) {
			String s1 = (String) list1.get(i);
			if (s1.contains(string)) {
				b = true;
				break;
			}
		}
		Assert.assertTrue(b);
	}

	@Test
	public void testRetrieveNByFieldsEx3() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("case3?????????????????????????????????????????????");
		//
		MvcResult mr3 = mvc.perform( //
				post("/warehousing/retrieveNByFieldsEx.bx") //
						.param(Warehousing.field.getFIELD_NAME_queryKeyword(), "??????") //
						.param(Warehousing.field.getFIELD_NAME_status(), String.valueOf(BaseAction.INVALID_ID)) //
						.contentType(MediaType.APPLICATION_JSON) //
						.session(sessionBoss) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();
		Shared.checkJSONErrorCode(mr3);
		//
		JSONObject o2 = JSONObject.fromObject(mr3.getResponse().getContentAsString()); //
		List<Integer> list2 = JsonPath.read(o2, "$.warehousingList[*].providerID");

		String providerName = "??????";

		// ???????????????????????????????????????
		for (int i = 0; i < list2.size(); i++) {
			Provider p = new Provider();
			int i1 = list2.get(i);
			p.setID(i1);
			Map<String, Object> retrieve1Params = p.getRetrieve1Param(BaseBO.INVALID_CASE_ID, p);
			DataSourceContextHolder.setDbName(Shared.DBName_Test);
			Provider r1Po = (Provider) providerMapper.retrieve1(retrieve1Params);
			p.setIgnoreIDInComparision(true);
			Assert.assertTrue(r1Po != null && EnumErrorCode.values()[Integer.parseInt(retrieve1Params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "??????????????????");
			Assert.assertTrue(r1Po.getName().contains(providerName));
		}
	}

	@Test
	public void testRetrieveNByFieldsEx4() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("case4???????????????????????????????????????????????????????????????");
		//
		MvcResult mr4 = mvc.perform( //
				post("/warehousing/retrieveNByFieldsEx.bx") //
						.param(Warehousing.field.getFIELD_NAME_queryKeyword(), "??????") //
						.param(Warehousing.field.getFIELD_NAME_status(), String.valueOf(Warehousing.EnumStatusWarehousing.ESW_Approved.getIndex())) //
						.contentType(MediaType.APPLICATION_JSON) //
						.session(sessionBoss) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();
		Shared.checkJSONErrorCode(mr4);
		//
		JSONObject o3 = JSONObject.fromObject(mr4.getResponse().getContentAsString()); //
		List<Integer> list3 = JsonPath.read(o3, "$.warehousingList[*].providerID");
		List<Integer> listStatus = JsonPath.read(o3, "$.warehousingList[*].status");

		// ???????????????????????????????????????
		for (int i = 0; i < list3.size(); i++) {
			Provider p = new Provider();
			int i1 = list3.get(i);
			p.setID(i1);
			Map<String, Object> retrieve1Params = p.getRetrieve1Param(BaseBO.INVALID_CASE_ID, p);
			DataSourceContextHolder.setDbName(Shared.DBName_Test);
			Provider r1Po = (Provider) providerMapper.retrieve1(retrieve1Params);
			p.setIgnoreIDInComparision(true);
			Assert.assertTrue(r1Po != null && EnumErrorCode.values()[Integer.parseInt(retrieve1Params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "??????????????????");
			Assert.assertTrue(r1Po.getName().contains("??????"));
		}

		// ????????????
		for (int i = 0; i < listStatus.size(); i++) {
			System.out.println(listStatus.get(i));
			int i1 = listStatus.get(i);
			Assert.assertTrue(i1 == Warehousing.EnumStatusWarehousing.ESW_Approved.getIndex());
		}
	}

	@Test
	public void testRetrieveNByFieldsEx5() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("case5?????????????????????ID?????????????????????????????????????????????");
		//
		MvcResult mr5 = mvc.perform( //
				post("/warehousing/retrieveNByFieldsEx.bx") //
						.param(Warehousing.field.getFIELD_NAME_queryKeyword(), "??????") //
						.param(Warehousing.field.getFIELD_NAME_status(), String.valueOf(BaseAction.INVALID_STATUS)) //
						.param(Warehousing.field.getFIELD_NAME_staffID(), "3") //
						.contentType(MediaType.APPLICATION_JSON) //
						.session(sessionBoss) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();
		Shared.checkJSONErrorCode(mr5);
		//
		JSONObject o4 = JSONObject.fromObject(mr5.getResponse().getContentAsString()); //
		List<Integer> list4 = JsonPath.read(o4, "$.warehousingList[*].providerID");
		List<Integer> liststaffID = JsonPath.read(o4, "$.warehousingList[*].staffID");

		// ???????????????????????????????????????
		for (int i = 0; i < list4.size(); i++) {
			Provider p = new Provider();
			int i1 = list4.get(i);
			p.setID(i1);
			Map<String, Object> retrieve1Params = p.getRetrieve1Param(BaseBO.INVALID_CASE_ID, p);
			DataSourceContextHolder.setDbName(Shared.DBName_Test);
			Provider r1Po = (Provider) providerMapper.retrieve1(retrieve1Params);
			p.setIgnoreIDInComparision(true);
			Assert.assertTrue(r1Po != null && EnumErrorCode.values()[Integer.parseInt(retrieve1Params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "??????????????????");
			Assert.assertTrue(r1Po.getName().contains("??????"));
		}

		// ???????????????ID
		for (int i = 0; i < liststaffID.size(); i++) {
			System.out.println(liststaffID.get(i));
			int i1 = liststaffID.get(i);
			Assert.assertTrue(i1 == 3);
		}
	}

	@Test
	public void testRetrieveNByFieldsEx6() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("case6?????????????????????ID?????????????????????(10)???????????????????????????????????????");
		//
		MvcResult mr6 = mvc.perform( //
				post("/warehousing/retrieveNByFieldsEx.bx") //
						.param(Warehousing.field.getFIELD_NAME_queryKeyword(), "RK20190605") //
						.param(Warehousing.field.getFIELD_NAME_status(), String.valueOf(BaseAction.INVALID_ID)) //
						.param(Warehousing.field.getFIELD_NAME_providerID(), "2") //
						.contentType(MediaType.APPLICATION_JSON) //
						.session(sessionBoss) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();
		Shared.checkJSONErrorCode(mr6);
		//
		JSONObject o5 = JSONObject.fromObject(mr6.getResponse().getContentAsString()); //
		List<String> list5 = JsonPath.read(o5, "$.warehousingList[*].sn");
		List<Integer> listPurchasingOrderID = JsonPath.read(o5, "$.warehousingList[*].purchasingOrderID");

		// ??????F_SN
		for (int i = 0; i < list5.size(); i++) {
			String SN = list5.get(i);
			Assert.assertTrue(SN.contains("RK20190605"), "?????????????????????" + SN + "???????????????");
		}

		// ???????????????ID
		for (int i = 0; i < listPurchasingOrderID.size(); i++) {
			PurchasingOrder po = new PurchasingOrder();
			int i1 = listPurchasingOrderID.get(i);
			po.setID(i1);
			PurchasingOrderMapper mapper = (PurchasingOrderMapper) wac.getBean("purchasingOrderMapper");
			Map<String, Object> retrieve1Params = po.getRetrieve1Param(BaseBO.INVALID_CASE_ID, po);
			DataSourceContextHolder.setDbName(Shared.DBName_Test);
			List<List<BaseModel>> bmList = mapper.retrieve1Ex(retrieve1Params);
			PurchasingOrder r1Po = (PurchasingOrder) bmList.get(0).get(0);
			po.setIgnoreIDInComparision(true);
			Assert.assertTrue(r1Po != null && EnumErrorCode.values()[Integer.parseInt(retrieve1Params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "??????????????????");
			Assert.assertTrue(r1Po.getProviderID() == 2);
		}
	}

	@Test
	public void testRetrieveNByFieldsEx7() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("case7????????????????????????????????????????????????????????????????????????");
		//
		SimpleDateFormat sdf3 = new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default3);
		SimpleDateFormat sdf1 = new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default1);
		String date1 = "2017/10/8 1:01:01";
		String date2 = "2020/01/01 00:00:00";

		MvcResult mr7 = mvc.perform( //
				post("/warehousing/retrieveNByFieldsEx.bx") //
						.param(Warehousing.field.getFIELD_NAME_queryKeyword(), "??????") //
						.param(Warehousing.field.getFIELD_NAME_status(), String.valueOf(BaseAction.INVALID_ID)) //
						.param(Warehousing.field.getFIELD_NAME_date1(), "2017/10/8 1:01:01") //
						.param(Warehousing.field.getFIELD_NAME_date2(), "2020/01/01 00:00:00") //
						.contentType(MediaType.APPLICATION_JSON) //
						.session(sessionBoss) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();
		Shared.checkJSONErrorCode(mr7);
		//
		JSONObject o6 = JSONObject.fromObject(mr7.getResponse().getContentAsString()); //
		List<Integer> list6 = JsonPath.read(o6, "$.warehousingList[*].providerID");
		List<Integer> listCreateDatetime = JsonPath.read(o6, "$.warehousingList[*].createDatetime");

		String providerName = "??????";
		// ???????????????????????????????????????
		for (int i = 0; i < list6.size(); i++) {
			Provider p = new Provider();
			int i1 = list6.get(i);
			p.setID(i1);
			Map<String, Object> retrieve1Params = p.getRetrieve1Param(BaseBO.INVALID_CASE_ID, p);
			DataSourceContextHolder.setDbName(Shared.DBName_Test);
			Provider r1Po = (Provider) providerMapper.retrieve1(retrieve1Params);
			p.setIgnoreIDInComparision(true);
			Assert.assertTrue(r1Po != null && EnumErrorCode.values()[Integer.parseInt(retrieve1Params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "??????????????????");
			Assert.assertTrue(r1Po.getName().contains(providerName));
		}

		// ??????????????????
		for (int i = 0; i < listCreateDatetime.size(); i++) {
			Date s1 = sdf1.parse(String.valueOf(listCreateDatetime.get(i)));
			Assert.assertTrue(s1.after(sdf3.parse(date1)) && s1.before(sdf3.parse(date2)));
		}
	}

	@Test
	public void testRetrieveNByFieldsEx8() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("case8??????????????????????????????????????????queryKeyword???");
		//
		MvcResult mr8 = mvc.perform( //
				post("/warehousing/retrieveNByFieldsEx.bx") //
						.param(Warehousing.field.getFIELD_NAME_status(), String.valueOf(Warehousing.EnumStatusWarehousing.ESW_Approved.getIndex())) //
						.contentType(MediaType.APPLICATION_JSON) //
						.session(sessionBoss) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();
		Shared.checkJSONErrorCode(mr8);
		//
		JSONObject o7 = JSONObject.fromObject(mr8.getResponse().getContentAsString()); //
		List<Integer> list7 = JsonPath.read(o7, "$.warehousingList[*].status");

		// ????????????
		for (int i = 0; i < list7.size(); i++) {
			System.out.println(list7.get(i));
			int i1 = list7.get(i);
			Assert.assertTrue(i1 == Warehousing.EnumStatusWarehousing.ESW_Approved.getIndex());
		}
	}

	@Test
	public void testRetrieveNByFieldsEx9() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("case9??????????????????ID???????????????????????????queryKeyword)");
		//
		MvcResult mr9 = mvc.perform( //
				post("/warehousing/retrieveNByFieldsEx.bx") //
						.param(Warehousing.field.getFIELD_NAME_status(), String.valueOf(BaseAction.INVALID_ID)) //
						.param(Warehousing.field.getFIELD_NAME_staffID(), "3") //
						.contentType(MediaType.APPLICATION_JSON) //
						.session(sessionBoss) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();
		Shared.checkJSONErrorCode(mr9);
		//
		JSONObject o8 = JSONObject.fromObject(mr9.getResponse().getContentAsString()); //
		List<Integer> list8 = JsonPath.read(o8, "$.warehousingList[*].staffID");
		// ???????????????ID
		for (int i = 0; i < list8.size(); i++) {
			System.out.println(list8.get(i));
			int i1 = list8.get(i);
			Assert.assertTrue(i1 == 3);
		}
	}

	@Test
	public void testRetrieveNByFieldsEx10() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("case10??????????????????ID???????????????????????????queryKeyword???");
		//
		MvcResult mr10 = mvc.perform( //
				post("/warehousing/retrieveNByFieldsEx.bx") //
						.param(Warehousing.field.getFIELD_NAME_status(), String.valueOf(BaseAction.INVALID_ID)) //
						.param(Warehousing.field.getFIELD_NAME_providerID(), "10") //
						.contentType(MediaType.APPLICATION_JSON) //
						.session(sessionBoss) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();
		Shared.checkJSONErrorCode(mr10);
		//
		JSONObject o9 = JSONObject.fromObject(mr10.getResponse().getContentAsString()); //
		List<Integer> list9 = JsonPath.read(o9, "$.warehousingList[*].providerID");
		// ???????????????ID
		for (int i = 0; i < list9.size(); i++) {
			Provider p = new Provider();
			int i1 = list9.get(i);
			p.setID(i1);
			Map<String, Object> retrieve1Params = p.getRetrieve1Param(BaseBO.INVALID_CASE_ID, p);
			DataSourceContextHolder.setDbName(Shared.DBName_Test);
			Provider r1Po = (Provider) providerMapper.retrieve1(retrieve1Params);
			p.setIgnoreIDInComparision(true);
			Assert.assertTrue(r1Po != null && EnumErrorCode.values()[Integer.parseInt(retrieve1Params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "??????????????????");
			Assert.assertTrue(r1Po.getID() == 10);
		}
	}

	@Test
	public void testRetrieveNByFieldsEx11() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("case11?????????????????????????????????????????????????????????queryKeyword???");
		//
		MvcResult mr11 = mvc.perform( //
				post("/warehousing/retrieveNByFieldsEx.bx") //
						.param(Warehousing.field.getFIELD_NAME_status(), String.valueOf(BaseAction.INVALID_ID)) //
						.param(Warehousing.field.getFIELD_NAME_date1(), "2019/1/8 1:01:00") //
						.param(Warehousing.field.getFIELD_NAME_date2(), "2020/01/01 00:00:00") //
						.contentType(MediaType.APPLICATION_JSON) //
						.session(sessionBoss) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();
		Shared.checkJSONErrorCode(mr11);
		//
		JSONObject o10 = JSONObject.fromObject(mr11.getResponse().getContentAsString()); //
		List<Integer> list10 = JsonPath.read(o10, "$.warehousingList[*].createDatetime");
		// ??????????????????
		SimpleDateFormat sdf3 = new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default3);
		SimpleDateFormat sdf1 = new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default1);
		String date1 = "2017/10/8 1:01:01";
		String date2 = "2020/01/01 00:00:00";
		for (int i = 0; i < list10.size(); i++) {
			Date s1 = sdf1.parse(String.valueOf(list10.get(i)));
			Assert.assertTrue(s1.after(sdf3.parse(date1)) && s1.before(sdf3.parse(date2)));
		}
	}

	@Test
	public void testRetrieveNByFieldsEx12() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("case12??????????????????????????????");
		//
		MvcResult mr12 = mvc.perform( //
				post("/warehousing/retrieveNByFieldsEx.bx") //
						.param(Warehousing.field.getFIELD_NAME_status(), String.valueOf(BaseAction.INVALID_ID)) //
						.contentType(MediaType.APPLICATION_JSON) //
						.session(sessionBoss) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();
		Shared.checkJSONErrorCode(mr12);
		//
		JSONObject o11 = JSONObject.fromObject(mr12.getResponse().getContentAsString()); //
		List<Integer> list11 = JsonPath.read(o11, "$.warehousingList[*].ID");
		Assert.assertTrue(list11.size() > 0);
	}

	@Test
	public void testRetrieveNByFieldsEx13() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("case13?????????????????????????????????RNByFieldsEx?????????warehousing?????????warehousingCommodity???listCommodity,??????????????????????????????R1???????????????????????????");
		//
		MvcResult mr13 = mvc.perform( //
				post("/warehousing/retrieveNByFieldsEx.bx") //
						.param(Warehousing.field.getFIELD_NAME_status(), String.valueOf(BaseAction.INVALID_ID)) //
						.contentType(MediaType.APPLICATION_JSON) //
						.session(sessionBoss) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();
		Shared.checkJSONErrorCode(mr13);
		//
		JSONObject o13 = JSONObject.fromObject(mr13.getResponse().getContentAsString()); //
		List<List<WarehousingCommodity>> whCommList = JsonPath.read(o13, "$.warehousingList[*].listSlave1");
		Assert.assertTrue(whCommList.size() > 0, "?????????warehousing??????????????????");// size>0???????????????warehousing???????????????
		for (List<WarehousingCommodity> ls : whCommList) {
			Assert.assertTrue(ls.size() == 0, "warehousing????????????????????????RNByField?????????????????????????????????");
		}
		List<List<Commodity>> commList = JsonPath.read(o13, "$.warehousingList[*].listCommodity");
		for (List<Commodity> ls2 : commList) {
			Assert.assertTrue(ls2.size() == 0, "warehousing?????????listCommodity?????????RNByField?????????????????????????????????");
		}
	}

	@Test
	public void testRetrieveNByFieldsEx14() throws Exception {
		// ???warehousing/retrieveNByFieldsEx.bx???????????????????????????????????????????????????
		Shared.printTestMethodStartInfo();
		Shared.caseLog("case14???????????????????????????(10)???????????????????????????????????????");
		//
		MvcResult mr = mvc.perform( //
				post("/warehousing/retrieveNByFieldsEx.bx") //
						.param(Warehousing.field.getFIELD_NAME_queryKeyword(), "RK2019060") //
						.param(Warehousing.field.getFIELD_NAME_status(), String.valueOf(BaseAction.INVALID_ID)) //
						.contentType(MediaType.APPLICATION_JSON) //
						.session(sessionBoss) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();
		Shared.checkJSONErrorCode(mr);

		JSONObject o = JSONObject.fromObject(mr.getResponse().getContentAsString()); //
		List<Integer> list = JsonPath.read(o, "$.warehousingList[*].ID");

		Assert.assertTrue(list.size() == 0);
	}

	@Test
	public void testRetrieveNByFieldsEx15() throws Exception {
		// ???warehousing/retrieveNByFieldsEx.bx???????????????????????????????????????????????????
		Shared.printTestMethodStartInfo();
		Shared.caseLog("case15???????????????????????????(20)???????????????????????????????????????");
		//
		MvcResult mr = mvc.perform( //
				post("/warehousing/retrieveNByFieldsEx.bx") //
						.param(Warehousing.field.getFIELD_NAME_queryKeyword(), "RK20190605123451234512345") //
						.param(Warehousing.field.getFIELD_NAME_status(), String.valueOf(BaseAction.INVALID_ID)) //
						.contentType(MediaType.APPLICATION_JSON) //
						.session(sessionBoss) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();
		Shared.checkJSONErrorCode(mr);

		JSONObject o = JSONObject.fromObject(mr.getResponse().getContentAsString()); //
		List<Integer> list = JsonPath.read(o, "$.warehousingList[*].ID");

		Assert.assertTrue(list.size() == 0);
	}

	@Test
	public void testRetrieveNByFieldsEx16() throws Exception {
		// ???warehousing/retrieveNByFieldsEx.bx???????????????????????????????????????????????????
		Shared.printTestMethodStartInfo();
		Shared.caseLog("case15???????????????????????????(20)???????????????????????????????????????");
		//
		MvcResult mr = mvc.perform( //
				post("/warehousing/retrieveNByFieldsEx.bx") //
						.param(Warehousing.field.getFIELD_NAME_queryKeyword(), "RK201906051234512345") //
						.param(Warehousing.field.getFIELD_NAME_status(), String.valueOf(BaseAction.INVALID_ID)) //
						.contentType(MediaType.APPLICATION_JSON) //
						.session(sessionBoss) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();
		Shared.checkJSONErrorCode(mr);

		JSONObject o = JSONObject.fromObject(mr.getResponse().getContentAsString()); //
		List<Integer> list = JsonPath.read(o, "$.warehousingList[*].ID");

		Assert.assertTrue(list.size() >= 0);
	}

	@Test
	public void testRetrieveNByFieldsEx17() throws Exception {
		// ???warehousing/retrieveNByFieldsEx.bx???????????????????????????????????????????????????
		Shared.printTestMethodStartInfo();
		Shared.caseLog("case17?????????10????????????????????????SN????????????????????????");
		//
		MvcResult mr = mvc.perform( //
				post("/warehousing/retrieveNByFieldsEx.bx") //
						.param(Warehousing.field.getFIELD_NAME_queryKeyword(), "CG2019") //
						.param(Warehousing.field.getFIELD_NAME_status(), String.valueOf(BaseAction.INVALID_ID)) //
						.contentType(MediaType.APPLICATION_JSON) //
						.session(sessionBoss) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();
		Shared.checkJSONErrorCode(mr);

		JSONObject o = JSONObject.fromObject(mr.getResponse().getContentAsString()); //
		List<Integer> list = JsonPath.read(o, "$.warehousingList[*].ID");

		Assert.assertTrue(list.size() == 0);
	}

	@Test
	public void testRetrieveNByFieldsEx18() throws Exception {
		// ???warehousing/retrieveNByFieldsEx.bx???????????????????????????????????????????????????
		Shared.printTestMethodStartInfo();
		Shared.caseLog("case18?????????10-20??????????????????SN????????????????????????");
		//
		MvcResult mr = mvc.perform( //
				post("/warehousing/retrieveNByFieldsEx.bx") //
						.param(Warehousing.field.getFIELD_NAME_queryKeyword(), "CG201906040007") //
						.param(Warehousing.field.getFIELD_NAME_status(), String.valueOf(BaseAction.INVALID_ID)) //
						.contentType(MediaType.APPLICATION_JSON) //
						.session(sessionBoss) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();
		Shared.checkJSONErrorCode(mr);

		JSONObject o = JSONObject.fromObject(mr.getResponse().getContentAsString()); //
		List<Integer> list = JsonPath.read(o, "$.warehousingList[*].ID");

		Assert.assertTrue(list.size() != 0);
	}

	@Test
	public void testRetrieveNByFieldsEx19() throws Exception {
		Shared.printTestMethodStartInfo();

		System.out.println("--------------- case19:??????queryKeyword??????_????????????????????????????????? ---------------");
		// ????????????
		Commodity commodity = BaseCommodityTest.DataInput.getCommodity();
		commodity.setName("????????????1111_54???" + System.currentTimeMillis());
		String barcode = "987623" + System.currentTimeMillis();
		commodity.setMultiPackagingInfo(barcode + ";" + commodity.getPackageUnitID() + ";" + commodity.getRefCommodityMultiple() + ";" + commodity.getPriceRetail() + ";" + commodity.getPriceVIP() + ";" + commodity.getPriceWholesale() + ";"
				+ commodity.getName() + ";");
		Commodity commodity1 = BaseCommodityTest.createCommodityViaAction(commodity, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		Barcodes barcodes1 = retrieveNBarcodes(commodity1);
		// ?????????????????????????????????
		MvcResult mr1 = mvc.perform( //
				post("/warehousing/createEx.bx") //
						.param(Warehousing.field.getFIELD_NAME_providerID(), "1") //
						.param(Warehousing.field.getFIELD_NAME_shopID(), String.valueOf(Shared.DEFAULT_Shop_ID)) //
						.param(Warehousing.field.getFIELD_NAME_warehouseID(), "1") //
						.param(commIDs, String.valueOf(commodity1.getID())) //
						.param(commNOs, "4") //
						.param(commPrices, "11.1") //
						.param(amounts, "11.1") //
						.param(barcodeIDs, String.valueOf(barcodes1.getID())) //
						.contentType(MediaType.APPLICATION_JSON) //
						.session(sessionBoss) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();
		Shared.checkJSONErrorCode(mr1);
		// ????????????????????????
		MvcResult mr2 = mvc.perform( //
				post("/warehousing/retrieveNByFieldsEx.bx") //
						.param(Warehousing.field.getFIELD_NAME_queryKeyword(), "_54???") //
						.param(Warehousing.field.getFIELD_NAME_status(), String.valueOf(BaseAction.INVALID_STATUS)) //
						.contentType(MediaType.APPLICATION_JSON) //
						.session(sessionBoss) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();
		// ????????????
		Shared.checkJSONErrorCode(mr2);

		JSONObject o = JSONObject.fromObject(mr2.getResponse().getContentAsString()); //

		List<Integer> list = JsonPath.read(o, "$.warehousingList[*].ID");//
		Assert.assertTrue(list.size() != 0);

		// ??????????????????ID??????????????????????????????JsonObject
		for (int wcID : list) {
			MvcResult mr = mvc.perform( //
					get("/warehousing/retrieve1Ex.bx?" //
							+ Warehousing.field.getFIELD_NAME_ID() + "=" + wcID + "&" //
							+ Warehousing.field.getFIELD_NAME_status() + "=" + EnumStatusWarehousing.ESW_ToApprove.getIndex()//
					) //
							.contentType(MediaType.APPLICATION_JSON) //
							.session(sessionBoss) //
			) //
					.andExpect(status().isOk()) //
					.andDo(print()) //
					.andReturn();
			Shared.checkJSONErrorCode(mr);
			String json = mr.getResponse().getContentAsString();
			JSONObject jsonObject = JSONObject.fromObject(json);
			List<?> warehousingCommodityList = JsonPath.read(jsonObject, "$.object.listSlave1[*]");
			Assert.assertTrue(warehousingCommodityList != null && warehousingCommodityList.size() != 0, "??????????????????????????????????????????");
			// ????????????????????????????????????
			for (int i = 0; i < warehousingCommodityList.size(); i++) {
				WarehousingCommodity warehousingCommodity = new WarehousingCommodity();
				warehousingCommodity.parse1(warehousingCommodityList.get(i).toString());
				// ????????????????????????????????????????????????"_"
				Assert.assertTrue(warehousingCommodity.getCommodityName().contains("_"), "??????????????????????????????_?????????");
			}
		}

	}
	
	@Test
	public void testRetrieveNByFieldsEx20() throws Exception {
		System.out.println("--------------- case20:????????????ID????????????????????? ---------------");
		
		Shared.printTestMethodStartInfo();
		Warehousing warehousing = BaseWarehousingTest.DataInput.getWarehousing();
		Warehousing ws = createWarehousing(warehousing);
		Shared.caseLog("case1???????????????(10)???????????????????????????????????????");
		//
		MvcResult mr = mvc.perform( //
				post("/warehousing/retrieveNByFieldsEx.bx") //
						.param(Warehousing.field.getFIELD_NAME_queryKeyword(), "") //
						.param(Warehousing.field.getFIELD_NAME_status(), String.valueOf(BaseAction.INVALID_ID)) //
						.param(Warehousing.field.getFIELD_NAME_shopID(), String.valueOf(Shared.DEFAULT_Shop_ID)) //
						.contentType(MediaType.APPLICATION_JSON) //
						.session(sessionBoss) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();
		Shared.checkJSONErrorCode(mr);

		JSONObject o = JSONObject.fromObject(mr.getResponse().getContentAsString()); //
		List<Integer> list = JsonPath.read(o, "$.warehousingList[*].ID");

		boolean flag = false;
		for (int i = 0; i < list.size(); i++) {
			System.out.println(list.get(i));
			int i1 = list.get(i);
			if (ws.getID() == i1) {
				flag = true;
				break;
			}
		}
		Assert.assertTrue(flag);
	}

	@SuppressWarnings("unchecked")
	protected Barcodes retrieveNBarcodes(Commodity commCreate) throws Exception, UnsupportedEncodingException {
		MvcResult mrl2 = mvc.perform(//
				get("/barcodes/retrieveNEx.bx?commodityID=" + commCreate.getID())//
						.contentType(MediaType.APPLICATION_JSON)//
						.session(sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mrl2);

		JSONObject o2 = JSONObject.fromObject(mrl2.getResponse().getContentAsString());
		JSONArray barJSONArray = o2.getJSONArray("barcodesList");
		Barcodes barcodes = new Barcodes();
		List<Barcodes> list = (List<Barcodes>) barcodes.parseN(barJSONArray);
		for (Barcodes b : list) {
			assertTrue(b.getCommodityID() == commCreate.getID());
		}

		return list.get(0);
	}

	@Test
	public void testDeleteEx1() throws Exception {
		Shared.printTestMethodStartInfo();
		Warehousing warehousing = BaseWarehousingTest.DataInput.getWarehousing();
		Warehousing ws = createWarehousing(warehousing);
		// ??????????????????
		Commodity commodity1 = new Commodity();
		commodity1.setID(1);
		Commodity commodityCache1 = BaseCommodityTest.queryCommodityCache(commodity1.getID());
		Barcodes barcodes = new Barcodes();
		barcodes.setOperatorStaffID(STAFF_ID3);
		barcodes.setBarcode("");
		barcodes.setCommodityID(commodityCache1.getID());
		Barcodes barcode1 = retrieveNBarcodes(barcodes);
		commodityCache1.setNO(20);
		createWarehousingCommodity(ws.getID(), commodityCache1, barcode1.getID());
		//
		Shared.caseLog("case1?????????ID???????????????");
		MvcResult mr = mvc.perform( //
				get("/warehousing/deleteEx.bx?" + Warehousing.field.getFIELD_NAME_ID() + "=" + ws.getID())
						//
						.contentType(MediaType.APPLICATION_JSON) //
						.session(sessionBoss) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();
		Shared.checkJSONErrorCode(mr);
		WarehousingCP.verifyDelete(ws, warehousingBO, Shared.DBName_Test);
	}

	@Test
	public void testDeleteEx2() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("case2????????????????????????????????????");
		MvcResult mr2 = mvc.perform( //
				get("/warehousing/deleteEx.bx?" + Warehousing.field.getFIELD_NAME_ID() + "=4") //
						.contentType(MediaType.APPLICATION_JSON) //
						.session(sessionBoss) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();
		Shared.checkJSONErrorCode(mr2, EnumErrorCode.EC_BusinessLogicNotDefined);
	}

	@Test
	public void testDeleteEx3() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("case3????????????????????????????????????");
		MvcResult mr3 = mvc.perform( //
				get("/warehousing/deleteEx.bx?" + Warehousing.field.getFIELD_NAME_ID() + "=99999") //
						.contentType(MediaType.APPLICATION_JSON) //
						.session(sessionBoss) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();
		Shared.checkJSONErrorCode(mr3, EnumErrorCode.EC_BusinessLogicNotDefined);
	}

	@Test
	public void testDeleteEx4() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("case4??????????????????????????????");
//		MvcResult mr4 = mvc.perform( //
//				get("/warehousing/deleteEx.bx?" + Warehousing.field.getFIELD_NAME_ID() + "=1") //
//						.contentType(MediaType.APPLICATION_JSON) //
//						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfManager)) //
//		) //
//				.andExpect(status().isOk()) //
//				.andDo(print()) //
//				.andReturn();
//		Shared.checkJSONErrorCode(mr4, EnumErrorCode.EC_NoPermission);
	}

	// @Test
	// public void testDeleteListEx() throws Exception {
	// Shared.printTestMethodStartInfo();
	//
	// System.out.println("------------------------------Case1:??????1????????????------------------------");
	// Warehousing wc = createWS();
	//
	// MvcResult mr = mvc.perform( //
	// get("/warehousing/deleteListEx.bx?WSListID=" + wc.getID()) //
	// .session((MockHttpSession) Shared.getStaffLoginSession(mvc,
	// Shared.PhoneOfBoss)) //
	// ) //
	// .andExpect(status().isOk()) //
	// .andDo(print()) //
	// .andReturn(); //
	//
	// Shared.checkJSONErrorCode(mr);
	//
	// System.out.println("-----------------------------------Case2:?????????????????????------------------------------");
	// Warehousing wc2 = createWS();
	// Warehousing wc3 = createWS();
	//
	// MvcResult mr2 = mvc.perform( //
	// get("/warehousing/deleteListEx.bx?WSListID=" + wc2.getID() + "," +
	// wc3.getID()) //
	// .session((MockHttpSession) Shared.getStaffLoginSession(mvc,
	// Shared.PhoneOfBoss)) //
	// ) //
	// .andExpect(status().isOk()) //
	// .andDo(print()) //
	// .andReturn();
	//
	// Shared.checkJSONErrorCode(mr2);
	//
	// System.out.println("-----------------------------------Case3:????????????????????????????????????------------------------------");
	// String msg1 = "?????????" + 4 + "??????????????????????????????????????????<br />";
	//
	// MvcResult mr3 = mvc.perform( //
	// get("/warehousing/deleteListEx.bx?WSListID=4") //
	// .session((MockHttpSession) Shared.getStaffLoginSession(mvc,
	// Shared.PhoneOfBoss)) //
	// ) //
	// .andExpect(status().isOk()) //
	// .andDo(print()) //
	// .andReturn();
	//
	// Shared.checkJSONErrorCode(mr3, EnumErrorCode.EC_PartSuccess);
	// Shared.checkJSONMsg(mr3, msg1, "????????????????????????");
	//
	// System.out.println("-----------------------------------Case4:????????????????????????????????????------------------------------");
	// String msg2 = "?????????" + 11 + "??????????????????????????????????????????<br />";
	//
	// MvcResult mr4 = mvc.perform( //
	// get("/warehousing/deleteListEx.bx?WSListID=4,11") //
	// .session((MockHttpSession) Shared.getStaffLoginSession(mvc,
	// Shared.PhoneOfBoss)) //
	// ) //
	// .andExpect(status().isOk()) //
	// .andDo(print()) //
	// .andReturn();
	//
	// Shared.checkJSONErrorCode(mr4, EnumErrorCode.EC_PartSuccess);
	// Shared.checkJSONMsg(mr4, msg1 + msg2, "????????????????????????");
	//
	// System.out.println("------------------------------Case5:??????0????????????-----------------------");
	// MvcResult mr5 = mvc.perform( //
	// get("/warehousing/deleteListEx.bx") //
	// .session((MockHttpSession) Shared.getStaffLoginSession(mvc,
	// Shared.PhoneOfBoss)) //
	// ) //
	// .andExpect(status().isOk()) //
	// .andDo(print()) //
	// .andReturn();
	//
	// String json3 = mr5.getResponse().getContentAsString();
	// assertTrue(json3.equals(""));
	//
	// System.out.println("-----------------------case6???POListID???ID???????????????????????????---------------");
	// String msg = "?????????" + BaseWarehousingTest.INVALID_ID + "??????????????????????????????????????????<br />";
	//
	// MvcResult mr6 = mvc.perform( //
	// get("/warehousing/deleteListEx.bx?WSListID=" +
	// BaseWarehousingTest.INVALID_ID) //
	// .session((MockHttpSession) Shared.getStaffLoginSession(mvc,
	// Shared.PhoneOfBoss)) //
	// ) //
	// .andExpect(status().isOk()) //
	// .andDo(print()) //
	// .andReturn(); //
	// Shared.checkJSONErrorCode(mr6, EnumErrorCode.EC_PartSuccess);
	// Shared.checkJSONMsg(mr6, msg, "????????????????????????");
	//
	// System.out.println("-----------------------------------Case7:????????????????????????????????????????????????------------------------------");
	// Warehousing wc4 = createWS();
	// MvcResult mr7 = mvc.perform( //
	// get("/warehousing/deleteListEx.bx?WSListID=4" + "," + wc4.getID()) //
	// .session((MockHttpSession) Shared.getStaffLoginSession(mvc,
	// Shared.PhoneOfBoss)) //
	// ) //
	// .andExpect(status().isOk()) //
	// .andDo(print()) //
	// .andReturn();
	//
	// Shared.checkJSONMsg(mr7, msg1, "????????????????????????");
	// Shared.checkJSONErrorCode(mr7, EnumErrorCode.EC_PartSuccess);
	//
	// System.out.println("-----------------------------------Case8:????????????????????????------------------------------");
	// Warehousing wc5 = createWS();
	// String msg3 = "?????????????????????????????????<br />";
	//
	// MvcResult mr8 = mvc.perform( //
	// get("/warehousing/deleteListEx.bx?WSListID=" + wc5.getID()) //
	// .session((MockHttpSession) Shared.getStaffLoginSession(mvc,
	// Shared.PhoneOfCashier)) //
	// ) //
	// .andExpect(status().isOk()) //
	// .andDo(print()) //
	// .andReturn(); //
	//
	// Shared.checkJSONMsg(mr8, msg3, "????????????????????????");
	// Shared.checkJSONErrorCode(mr8, EnumErrorCode.EC_PartSuccess);
	// }

	@Test
	public void testDeleteEx5() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("case5??????????????????,??????????????????");
		MvcResult mr5 = mvc.perform( //
				get("/warehousing/deleteEx.bx?" + Warehousing.field.getFIELD_NAME_ID() + "=" + BaseAction.INVALID_ID) //
						.contentType(MediaType.APPLICATION_JSON) //
						.session(sessionBoss) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();
		Shared.checkJSONErrorCode(mr5, EnumErrorCode.EC_WrongFormatForInputField);
	}

	@Test
	public void testDeleteEx6() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("case6??????????????????????????????????????????,???????????????????????????");
		Warehousing warehousing = BaseWarehousingTest.DataInput.getWarehousing();
		Warehousing wc = createWarehousing(warehousing);
		MvcResult mr6 = mvc.perform( //
				get("/warehousing/deleteEx.bx?" + Warehousing.field.getFIELD_NAME_ID() + "=" + wc.getID())
						//
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, BaseAction.ACCOUNT_Phone_PreSale)) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();
		Shared.checkJSONErrorCode(mr6, EnumErrorCode.EC_NoPermission);
	}

	@Test
	public void testDeleteEx7() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("case7??????????????????????????????????????????,???????????????????????????");
		MvcResult mr7 = mvc.perform( //
				get("/warehousing/deleteEx.bx?" + Warehousing.field.getFIELD_NAME_ID() + "=4") //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, BaseAction.ACCOUNT_Phone_PreSale)) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();
		Shared.checkJSONErrorCode(mr7, EnumErrorCode.EC_NoPermission);
	}

	// @Test
	// public void testDeleteListEx() throws Exception {
	// Shared.printTestMethodStartInfo();
	//
	// System.out.println("------------------------------Case1:??????1????????????------------------------");
	// Warehousing wc = createWS();
	//
	// MvcResult mr = mvc.perform( //
	// get("/warehousing/deleteListEx.bx?WSListID=" + wc.getID()) //
	// .session((MockHttpSession) Shared.getStaffLoginSession(mvc,
	// Shared.PhoneOfBoss)) //
	// ) //
	// .andExpect(status().isOk()) //
	// .andDo(print()) //
	// .andReturn(); //
	//
	// Shared.checkJSONErrorCode(mr);
	//
	// System.out.println("-----------------------------------Case2:?????????????????????------------------------------");
	// Warehousing wc2 = createWS();
	// Warehousing wc3 = createWS();
	//
	// MvcResult mr2 = mvc.perform( //
	// get("/warehousing/deleteListEx.bx?WSListID=" + wc2.getID() + "," +
	// wc3.getID()) //
	// .session((MockHttpSession) Shared.getStaffLoginSession(mvc,
	// Shared.PhoneOfBoss)) //
	// ) //
	// .andExpect(status().isOk()) //
	// .andDo(print()) //
	// .andReturn();
	//
	// Shared.checkJSONErrorCode(mr2);
	//
	// System.out.println("-----------------------------------Case3:????????????????????????????????????------------------------------");
	// String msg1 = "?????????" + 4 + "??????????????????????????????????????????<br />";
	//
	// MvcResult mr3 = mvc.perform( //
	// get("/warehousing/deleteListEx.bx?WSListID=4") //
	// .session((MockHttpSession) Shared.getStaffLoginSession(mvc,
	// Shared.PhoneOfBoss)) //
	// ) //
	// .andExpect(status().isOk()) //
	// .andDo(print()) //
	// .andReturn();
	//
	// Shared.checkJSONErrorCode(mr3, EnumErrorCode.EC_PartSuccess);
	// Shared.checkJSONMsg(mr3, msg1, "????????????????????????");
	//
	// System.out.println("-----------------------------------Case4:????????????????????????????????????------------------------------");
	// String msg2 = "?????????" + 11 + "??????????????????????????????????????????<br />";
	//
	// MvcResult mr4 = mvc.perform( //
	// get("/warehousing/deleteListEx.bx?WSListID=4,11") //
	// .session((MockHttpSession) Shared.getStaffLoginSession(mvc,
	// Shared.PhoneOfBoss)) //
	// ) //
	// .andExpect(status().isOk()) //
	// .andDo(print()) //
	// .andReturn();
	//
	// Shared.checkJSONErrorCode(mr4, EnumErrorCode.EC_PartSuccess);
	// Shared.checkJSONMsg(mr4, msg1 + msg2, "????????????????????????");
	//
	// System.out.println("------------------------------Case5:??????0????????????-----------------------");
	// MvcResult mr5 = mvc.perform( //
	// get("/warehousing/deleteListEx.bx") //
	// .session((MockHttpSession) Shared.getStaffLoginSession(mvc,
	// Shared.PhoneOfBoss)) //
	// ) //
	// .andExpect(status().isOk()) //
	// .andDo(print()) //
	// .andReturn();
	//
	// String json3 = mr5.getResponse().getContentAsString();
	// assertTrue(json3.equals(""));
	//
	// System.out.println("-----------------------case6???POListID???ID???????????????????????????---------------");
	// String msg = "?????????" + BaseWarehousingTest.INVALID_ID + "??????????????????????????????????????????<br />";
	//
	// MvcResult mr6 = mvc.perform( //
	// get("/warehousing/deleteListEx.bx?WSListID=" +
	// BaseWarehousingTest.INVALID_ID) //
	// .session((MockHttpSession) Shared.getStaffLoginSession(mvc,
	// Shared.PhoneOfBoss)) //
	// ) //
	// .andExpect(status().isOk()) //
	// .andDo(print()) //
	// .andReturn(); //
	// Shared.checkJSONErrorCode(mr6, EnumErrorCode.EC_PartSuccess);
	// Shared.checkJSONMsg(mr6, msg, "????????????????????????");
	//
	// System.out.println("-----------------------------------Case7:????????????????????????????????????????????????------------------------------");
	// Warehousing wc4 = createWS();
	// MvcResult mr7 = mvc.perform( //
	// get("/warehousing/deleteListEx.bx?WSListID=4" + "," + wc4.getID()) //
	// .session((MockHttpSession) Shared.getStaffLoginSession(mvc,
	// Shared.PhoneOfBoss)) //
	// ) //
	// .andExpect(status().isOk()) //
	// .andDo(print()) //
	// .andReturn();
	//
	// Shared.checkJSONMsg(mr7, msg1, "????????????????????????");
	// Shared.checkJSONErrorCode(mr7, EnumErrorCode.EC_PartSuccess);
	//
	// System.out.println("-----------------------------------Case8:????????????????????????------------------------------");
	// Warehousing wc5 = createWS();
	// String msg3 = "?????????????????????????????????<br />";
	//
	// MvcResult mr8 = mvc.perform( //
	// get("/warehousing/deleteListEx.bx?WSListID=" + wc5.getID()) //
	// .session((MockHttpSession) Shared.getStaffLoginSession(mvc,
	// Shared.PhoneOfCashier)) //
	// ) //
	// .andExpect(status().isOk()) //
	// .andDo(print()) //
	// .andReturn(); //
	//
	// Shared.checkJSONMsg(mr8, msg3, "????????????????????????");
	// Shared.checkJSONErrorCode(mr8, EnumErrorCode.EC_PartSuccess);
	// }

	@Test
	public void testUpdateEx1() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case1:????????????????????????");
		HttpSession session = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);

		Provider provider = createProvider();
		Warehousing warehousing = BaseWarehousingTest.DataInput.getWarehousing();
		Warehousing ws = createWarehousing(warehousing);
		Warehousing wsUpdate = new Warehousing();
		// ??????????????????
		Commodity commodity1 = new Commodity();
		commodity1.setID(1);
		Commodity commodityCache1 = BaseCommodityTest.queryCommodityCache(commodity1.getID());
		Barcodes barcodes = new Barcodes();
		barcodes.setOperatorStaffID(STAFF_ID3);
		barcodes.setBarcode("");
		barcodes.setCommodityID(commodityCache1.getID());
		Barcodes barcode1 = retrieveNBarcodes(barcodes);
		commodityCache1.setNO(20);
		createWarehousingCommodity(ws.getID(), commodityCache1, barcode1.getID());
		//
		wsUpdate.setID(ws.getID());
		wsUpdate.setShopID(Shared.DEFAULT_Shop_ID);
		wsUpdate.setProviderID(provider.getID());
		wsUpdate.setWarehouseID(ws.getWarehouseID());
		wsUpdate.setStaffID(ws.getStaffID());
		wsUpdate.setPurchasingOrderID(ws.getPurchasingOrderID());
		MvcResult mr3 = mvc.perform( //
				post("/warehousing/updateEx.bx") //
						.param(Warehousing.field.getFIELD_NAME_ID(), String.valueOf(ws.getID()))//
						.param(Warehousing.field.getFIELD_NAME_providerID(), String.valueOf(provider.getID()))//
						.param(Warehousing.field.getFIELD_NAME_warehouseID(), String.valueOf(ws.getWarehouseID()))//
						.param(commIDs, "1,2,3")//
						.param(barcodeIDs, "1,3,5")//
						.param(commNOs, "100,100,20")//
						.param(commPrices, "1.1,1.2,1.3")//
						.param(amounts, "200,200,200")//
						.param("shelfLifes", "36,36,36")//
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) session) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();

		Shared.checkJSONErrorCode(mr3);

		JSONObject o = JSONObject.fromObject(mr3.getResponse().getContentAsString());
		int providerID = JsonPath.read(o, "$.object.providerID");

		Assert.assertTrue(providerID == provider.getID());

		WarehousingCP.verifyUpdateEx(mr3, wsUpdate, Shared.DBName_Test);
	}

	@Test
	public void testUpdateEx2() throws Exception {
		Shared.printTestMethodStartInfo();

		HttpSession session = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);
		Shared.caseLog("Case2:????????????????????????");

		Provider provider = createProvider();
		MvcResult mr3 = mvc.perform( //
				post("/warehousing/updateEx.bx") //
						.param(Warehousing.field.getFIELD_NAME_ID(), String.valueOf(WS_STATUS1_ID))//
						.param(Warehousing.field.getFIELD_NAME_providerID(), String.valueOf(provider.getID()))//
						.param(Warehousing.field.getFIELD_NAME_warehouseID(), String.valueOf(WE_DEFAULT_ID))//
						.param(commIDs, "1,2,3")//
						.param(barcodeIDs, "1,3,5")//
						.param(commNOs, "100,100,20")//
						.param(commPrices, "1.1,1.2,1.3")//
						.param(amounts, "200,200,200")//
						.param("shelfLifes", "36,36,36")//
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) session) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();

		Shared.checkJSONErrorCode(mr3, EnumErrorCode.EC_BusinessLogicNotDefined);
	}

	@Test
	public void testUpdateEx3() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case3:?????????????????????????????????");
		HttpSession session = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);

		Warehousing warehousing = BaseWarehousingTest.DataInput.getWarehousing();
		Warehousing ws = createWarehousing(warehousing);
		MvcResult mr3 = mvc.perform( //
				post("/warehousing/updateEx.bx") //
						.param(Warehousing.field.getFIELD_NAME_ID(), String.valueOf(ws.getID()))//
						.param(Warehousing.field.getFIELD_NAME_providerID(), String.valueOf(ws.getProviderID()))//
						.param(Warehousing.field.getFIELD_NAME_warehouseID(), String.valueOf(BaseWarehousingTest.INVALID_ID))//
						.param(commIDs, "1,2,3")//
						.param(barcodeIDs, "1,3,5")//
						.param(commNOs, "100,100,20")//
						.param(commPrices, "1.1,1.2,1.3")//
						.param(amounts, "200,200,200")//
						.param("shelfLifes", "36,36,36")//
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) session) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();

		Shared.checkJSONErrorCode(mr3, EnumErrorCode.EC_BusinessLogicNotDefined);
	}

	@Test
	public void testUpdateEx4() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case4:????????????????????????????????????");
		HttpSession session = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);

		Warehousing warehousing = BaseWarehousingTest.DataInput.getWarehousing();
		Warehousing ws = createWarehousing(warehousing);
		MvcResult mr = mvc.perform( //
				post("/warehousing/updateEx.bx") //
						.param(Warehousing.field.getFIELD_NAME_ID(), String.valueOf(ws.getID()))//
						.param(Warehousing.field.getFIELD_NAME_providerID(), String.valueOf(Shared.BIG_ID))//
						.param(Warehousing.field.getFIELD_NAME_warehouseID(), String.valueOf(WE_DEFAULT_ID))//
						.param(commIDs, "1,2,3")//
						.param(barcodeIDs, "1,3,5")//
						.param(commNOs, "100,100,20")//
						.param(commPrices, "1.1,1.2,1.3")//
						.param(amounts, "200,200,200")//
						.param("shelfLifes", "36,36,36")//
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) session) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();

		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_BusinessLogicNotDefined);
	}

	@Test
	public void testUpdateEx5() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case5:????????????WarehousingID????????????WarehousingCommodity??????????????????7");
		HttpSession session = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);

		Provider provider = createProvider();
		MvcResult mr = mvc.perform( //
				post("/warehousing/updateEx.bx") //
						.param(Warehousing.field.getFIELD_NAME_ID(), String.valueOf(BaseWarehousingTest.INVALID_ID))//
						.param(Warehousing.field.getFIELD_NAME_providerID(), String.valueOf(provider.getID()))//
						.param(Warehousing.field.getFIELD_NAME_warehouseID(), String.valueOf(WE_DEFAULT_ID))//
						.param(commIDs, "1,2,3")//
						.param(barcodeIDs, "1,3,5")//
						.param(commNOs, "100,100,20")//
						.param(commPrices, "1.1,1.2,1.3")//
						.param(amounts, "200,200,200")//
						.param("shelfLifes", "36,36,36")//
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) session) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();

		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_BusinessLogicNotDefined);
	}

	@Test
	public void testUpdateEx6() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case6:????????????CommodityID????????????WarehousingCommodity");
		HttpSession session = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);

		Warehousing warehousing = BaseWarehousingTest.DataInput.getWarehousing();
		Warehousing ws = createWarehousing(warehousing);
		// ??????????????????
		Commodity commodity1 = new Commodity();
		commodity1.setID(1);
		Commodity commodityCache1 = BaseCommodityTest.queryCommodityCache(commodity1.getID());
		Barcodes barcodes = new Barcodes();
		barcodes.setOperatorStaffID(STAFF_ID3);
		barcodes.setBarcode("");
		barcodes.setCommodityID(commodityCache1.getID());
		Barcodes barcode1 = retrieveNBarcodes(barcodes);
		commodityCache1.setNO(20);
		createWarehousingCommodity(ws.getID(), commodityCache1, barcode1.getID());
		//
		MvcResult mr = mvc.perform( //
				post("/warehousing/updateEx.bx") //
						.param(Warehousing.field.getFIELD_NAME_ID(), String.valueOf(ws.getID()))//
						.param(Warehousing.field.getFIELD_NAME_providerID(), String.valueOf(ws.getProviderID()))//
						.param(Warehousing.field.getFIELD_NAME_warehouseID(), String.valueOf(WE_DEFAULT_ID))//
						.param(commIDs, "9999999,9999998,9999997")//
						.param(barcodeIDs, "1,2,3")//
						.param(commNOs, "100,100,20")//
						.param(commPrices, "1.1,1.2,1.3")//
						.param(amounts, "200,200,200")//
						.param("shelfLifes", "36,36,36")//
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) session) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();

		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_NoSuchData);
	}

	@Test
	public void testUpdateEx7() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case7:????????????WarehousingCommodity????????????????????????0????????????????????????");
		HttpSession session = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);

		Provider provider = createProvider();
		Warehousing warehousing = BaseWarehousingTest.DataInput.getWarehousing();
		createWarehousing(warehousing);
		MvcResult mr = mvc.perform( //
				post("/warehousing/updateEx.bx") //
						.param(Warehousing.field.getFIELD_NAME_ID(), String.valueOf(warehousing.getID()))//
						.param(Warehousing.field.getFIELD_NAME_providerID(), String.valueOf(provider.getID()))//
						.param(Warehousing.field.getFIELD_NAME_warehouseID(), String.valueOf(WE_DEFAULT_ID))//
						.param(commIDs, "1,2,3")//
						.param(barcodeIDs, "1,3,5")//
						.param(commNOs, "0,0,0")//
						.param(commPrices, "1.1,1.2,1.3")//
						.param(amounts, "200,200,200")//
						.param("shelfLifes", "36,36,36")//
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) session) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();

		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_WrongFormatForInputField);
	}

	@Test
	public void testUpdateEx8() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case8:???????????????????????????");
		HttpSession session = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);

		Warehousing warehousing = BaseWarehousingTest.DataInput.getWarehousing();
		Warehousing ws = createWarehousing(warehousing);
		MvcResult mr = mvc.perform( //
				post("/warehousing/updateEx.bx") //
						.param(Warehousing.field.getFIELD_NAME_ID(), String.valueOf(ws.getID()))//
						.param(Warehousing.field.getFIELD_NAME_providerID(), String.valueOf(ws.getProviderID()))//
						.param(Warehousing.field.getFIELD_NAME_warehouseID(), String.valueOf(WE_DEFAULT_ID))//
						.param(commIDs, "1,2,3")//
						.param(barcodeIDs, "1,3,5")//
						.param(commNOs, "100,100,20,100")//
						.param(commPrices, "1.1,1.2,1.3")//
						.param(amounts, "200,200,200")//
						.param("shelfLifes", "36,36,36")//
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) session) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();

		Assert.assertTrue("".equals(mr.getResponse().getContentAsString()), "action???????????????????????????");
	}

	@Test
	public void testUpdateEx9() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case9:??????????????????");
		HttpSession session = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);

		Warehousing warehousing = BaseWarehousingTest.DataInput.getWarehousing();
		Warehousing ws = createWarehousing(warehousing);
		MvcResult mr = mvc.perform( //
				post("/warehousing/updateEx.bx") //
						.param(Warehousing.field.getFIELD_NAME_ID(), String.valueOf(ws.getID()))//
						.param(Warehousing.field.getFIELD_NAME_providerID(), String.valueOf(ws.getProviderID()))//
						.param(Warehousing.field.getFIELD_NAME_warehouseID(), String.valueOf(WE_DEFAULT_ID))//
						.param(commIDs, "1,2,3")//
						.param(barcodeIDs, "1,3,5")//
						.param(commPrices, "1.1,1.2,1.3")//
						.param(amounts, "200,200,200")//
						.param("shelfLifes", "36,36,36")//
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) session) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();

		Assert.assertTrue("".equals(mr.getResponse().getContentAsString()), "action???????????????????????????");
	}

	@Test
	public void testUpdateEx10() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case10:????????????id??????");
		HttpSession session = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);

		Warehousing warehousing = BaseWarehousingTest.DataInput.getWarehousing();
		Warehousing ws = createWarehousing(warehousing);
		// ??????????????????
		Commodity commodity1 = new Commodity();
		commodity1.setID(1);
		Commodity commodityCache1 = BaseCommodityTest.queryCommodityCache(commodity1.getID());
		Barcodes barcodes = new Barcodes();
		barcodes.setOperatorStaffID(STAFF_ID3);
		barcodes.setBarcode("");
		barcodes.setCommodityID(commodityCache1.getID());
		Barcodes barcode1 = retrieveNBarcodes(barcodes);
		commodityCache1.setNO(20);
		createWarehousingCommodity(ws.getID(), commodityCache1, barcode1.getID());
		//
		MvcResult mr = mvc.perform( //
				post("/warehousing/updateEx.bx") //
						.param(Warehousing.field.getFIELD_NAME_ID(), String.valueOf(ws.getID()))//
						.param(Warehousing.field.getFIELD_NAME_providerID(), String.valueOf(ws.getProviderID()))//
						.param(Warehousing.field.getFIELD_NAME_warehouseID(), String.valueOf(WE_DEFAULT_ID))//
						.param(commIDs, "1,2,2")//
						.param(barcodeIDs, "1,3,3")//
						.param(commNOs, "100,100,20")//
						.param(commPrices, "1.1,1.2,1.3")//
						.param(amounts, "200,200,200")//
						.param("shelfLifes", "36,36,36")//
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) session) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();
		Assert.assertTrue("".equals(mr.getResponse().getContentAsString()), "action??????????????????????????????");
	}

	@Test
	public void testUpdateEx11() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case11:????????????");
//		HttpSession session = Shared.getStaffLoginSession(mvc, Shared.PhoneOfManager);
//
//		Provider provider = createProvider();
//		Warehousing warehousing = BaseWarehousingTest.DataInput.getWarehousing();
//		createWarehousing(warehousing);
//		MvcResult mr = mvc.perform( //
//				post("/warehousing/updateEx.bx") //
//						.param(Warehousing.field.getFIELD_NAME_ID(), String.valueOf(warehousing.getID()))//
//						.param(Warehousing.field.getFIELD_NAME_providerID(), String.valueOf(provider.getID()))//
//						.param(Warehousing.field.getFIELD_NAME_warehouseID(), String.valueOf(WE_DEFAULT_ID))//
//						.param(commIDs, "1,2,3")//
//						.param(barcodeIDs, "1,3,5")//
//						.param(commNOs, "100,100,20")//
//						.param(commPrices, "1.1,1.2,1.3")//
//						.param(amounts, "200,200,200")//
//						.param("shelfLifes", "36,36,36")//
//						.contentType(MediaType.APPLICATION_JSON) //
//						.session((MockHttpSession) session) //
//		) //
//				.andExpect(status().isOk()) //
//				.andDo(print()) //
//				.andReturn();
//
//		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_NoPermission);
	}

	@Test
	public void testUpdateEx12() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case12:????????????????????????????????????");
		HttpSession session = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);

		Provider provider = createProvider();
		Warehousing warehousing = BaseWarehousingTest.DataInput.getWarehousing();
		Warehousing ws = createWarehousing(warehousing);
		MvcResult mr3 = mvc.perform( //
				post("/warehousing/updateEx.bx") //
						.param(Warehousing.field.getFIELD_NAME_ID(), String.valueOf(ws.getID()))//
						.param(Warehousing.field.getFIELD_NAME_providerID(), String.valueOf(provider.getID()))//
						.param(Warehousing.field.getFIELD_NAME_warehouseID(), String.valueOf(ws.getWarehouseID()))//
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) session) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();

		// Shared.checkJSONErrorCode(mr3);
		String json = mr3.getResponse().getContentAsString();
		Assert.assertTrue(json.length() == 0, "CASE12?????????????????????????????????null");
	}

	@Test
	public void testUpdateEx13() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case13:???????????????????????????");
		HttpSession session = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);
		//
		MvcResult mr3 = mvc.perform( //
				post("/warehousing/updateEx.bx") //
						.param(Warehousing.field.getFIELD_NAME_ID(), String.valueOf(Shared.BIG_ID))//
						.param(Warehousing.field.getFIELD_NAME_providerID(), "1")//
						.param(Warehousing.field.getFIELD_NAME_warehouseID(), "1")//
						.param(commIDs, "1,2,3")//
						.param(barcodeIDs, "1,3,5")//
						.param(commNOs, "100,100,20")//
						.param(commPrices, "1.1,1.2,1.3")//
						.param(amounts, "200,200,200")//
						.param("shelfLifes", "36,36,36")//
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) session) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();

		Shared.checkJSONErrorCode(mr3, EnumErrorCode.EC_BusinessLogicNotDefined);
	}

	@Test
	public void testUpdateEx14() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case14:??????????????????????????????");
		HttpSession session = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);
		//
		MvcResult mr = mvc.perform( //
				post("/warehousing/updateEx.bx") //
						.param(Warehousing.field.getFIELD_NAME_ID(), String.valueOf(Shared.BIG_ID))//
						.param(Warehousing.field.getFIELD_NAME_providerID(), "1")//
						.param(Warehousing.field.getFIELD_NAME_warehouseID(), "1")//
						.param(commIDs, "1,2,52")//
						.param(barcodeIDs, "1,3,1")//
						.param(commNOs, "100,100,20")//
						.param(commPrices, "1.1,1.2,1.3")//
						.param(amounts, "200,200,200")//
						.param("shelfLifes", "36,36,36")//
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) session) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();

		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_BusinessLogicNotDefined);
	}
	
	@Test
	public void testUpdateEx15() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case15:??????A??????????????????????????????B?????????????????????????????????????????????????????????");
		
		Provider provider = createProvider();
		Warehousing warehousing = BaseWarehousingTest.DataInput.getWarehousing();
		Warehousing ws = createWarehousing(warehousing);
		Warehousing wsUpdate = new Warehousing();
		// ??????????????????
		Commodity commodity1 = new Commodity();
		commodity1.setID(1);
		Commodity commodityCache1 = BaseCommodityTest.queryCommodityCache(commodity1.getID());
		Barcodes barcodes = new Barcodes();
		barcodes.setOperatorStaffID(STAFF_ID3);
		barcodes.setBarcode("");
		barcodes.setCommodityID(commodityCache1.getID());
		Barcodes barcode1 = retrieveNBarcodes(barcodes);
		commodityCache1.setNO(20);
		createWarehousingCommodity(ws.getID(), commodityCache1, barcode1.getID());
		//
		wsUpdate.setID(ws.getID());
		wsUpdate.setShopID(Shared.DEFAULT_Shop_ID);
		wsUpdate.setProviderID(provider.getID());
		wsUpdate.setWarehouseID(ws.getWarehouseID());
		wsUpdate.setStaffID(ws.getStaffID());
		wsUpdate.setPurchasingOrderID(ws.getPurchasingOrderID());
		// ????????????B???????????????
		Shop shopGet = BaseShopTest.DataInput.getShop();
		Shop shopCreated = BaseShopTest.createViaCompanyAction(shopGet, mvc, mapBO, sessionOP, EnumErrorCode.EC_NoError);
		// ????????????????????????
		String Staff_Phone = Shared.getValidStaffPhone();
		MvcResult ret = BaseStaffTest.getToken(sessionBoss, mvc, Staff_Phone, Staff.FOR_CreateNewStaff);
		//
		String encrypt = Shared.encrypt(ret, Shared.PASSWORD_DEFAULT);
		Staff staff = BaseStaffTest.DataInput.getStaff();
		staff.setShopID(shopCreated.getID());
		staff.setName("??????" + Shared.generateCompanyName(6));
		staff.setRoleID(EnumTypeRole.ETR_Boss.getIndex());
		staff.setPhone(Staff_Phone);
		staff.setPwdEncrypted(encrypt);
		MvcResult mrStaffCreate = mvc.perform(//
				BaseStaffTest.DataInput.getBuilder("/staff/createEx.bx", MediaType.APPLICATION_JSON, staff)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		// ??????????????????????????????
		Shared.checkJSONErrorCode(mrStaffCreate);
		Staff staffCopy = (Staff) staff.clone(); // ??????????????????????????????pos??????????????????????????????????????????????????????????????????copy??????????????????????????????????????????
		StaffCP.verifyCreate(mrStaffCreate, staffCopy, staffRoleBO, Shared.DBName_Test);
		staff = (Staff) staffCopy.clone(); // ????????????
		//
		JSONObject object = JSONObject.fromObject(mrStaffCreate.getResponse().getContentAsString());
		Staff staff2 = new Staff();
		staff2 = (Staff) staff2.parse1(object.getString(BaseAction.KEY_Object));
		Assert.assertTrue(staff2 != null, "????????????");
		// ??????
		MockHttpSession sessionNewShopBoss = Shared.getStaffLoginSession(mvc, staff2.getPhone(), Shared.PASSWORD_DEFAULT, Shared.DB_SN_Test);
		MvcResult mr3 = mvc.perform( //
				post("/warehousing/updateEx.bx") //
						.param(Warehousing.field.getFIELD_NAME_ID(), String.valueOf(ws.getID()))//
						.param(Warehousing.field.getFIELD_NAME_providerID(), String.valueOf(provider.getID()))//
						.param(Warehousing.field.getFIELD_NAME_warehouseID(), String.valueOf(ws.getWarehouseID()))//
						.param(commIDs, "1,2,3")//
						.param(barcodeIDs, "1,3,5")//
						.param(commNOs, "100,100,20")//
						.param(commPrices, "1.1,1.2,1.3")//
						.param(amounts, "200,200,200")//
						.param("shelfLifes", "36,36,36")//
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionNewShopBoss) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();

		Shared.checkJSONErrorCode(mr3, EnumErrorCode.EC_BusinessLogicNotDefined);
	}
	
	@Test
	public void testUpdateEx16() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case16:??????A??????????????????????????????B(????????????)????????????????????????????????????");
		
		Provider provider = createProvider();
		Warehousing warehousing = BaseWarehousingTest.DataInput.getWarehousing();
		Warehousing ws = createWarehousing(warehousing);
		Warehousing wsUpdate = new Warehousing();
		// ??????????????????
		Commodity commodity1 = new Commodity();
		commodity1.setID(1);
		Commodity commodityCache1 = BaseCommodityTest.queryCommodityCache(commodity1.getID());
		Barcodes barcodes = new Barcodes();
		barcodes.setOperatorStaffID(STAFF_ID3);
		barcodes.setBarcode("");
		barcodes.setCommodityID(commodityCache1.getID());
		Barcodes barcode1 = retrieveNBarcodes(barcodes);
		commodityCache1.setNO(20);
		createWarehousingCommodity(ws.getID(), commodityCache1, barcode1.getID());
		//
		wsUpdate.setID(ws.getID());
		wsUpdate.setShopID(Shared.DEFAULT_Shop_ID);
		wsUpdate.setProviderID(provider.getID());
		wsUpdate.setWarehouseID(ws.getWarehouseID());
		wsUpdate.setStaffID(ws.getStaffID());
		wsUpdate.setPurchasingOrderID(ws.getPurchasingOrderID());
		// ????????????????????????
		String Staff_Phone = Shared.getValidStaffPhone();
		MvcResult ret = BaseStaffTest.getToken(sessionBoss, mvc, Staff_Phone, Staff.FOR_CreateNewStaff);
		//
		String encrypt = Shared.encrypt(ret, Shared.PASSWORD_DEFAULT);
		Staff staff = BaseStaffTest.DataInput.getStaff();
		staff.setShopID(BaseAction.VirtualShopID);
		staff.setName("??????" + Shared.generateCompanyName(6));
		staff.setRoleID(EnumTypeRole.ETR_Boss.getIndex());
		staff.setPhone(Staff_Phone);
		staff.setPwdEncrypted(encrypt);
		MvcResult mrStaffCreate = mvc.perform(//
				BaseStaffTest.DataInput.getBuilder("/staff/createEx.bx", MediaType.APPLICATION_JSON, staff)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		// ??????????????????????????????
		Shared.checkJSONErrorCode(mrStaffCreate);
		Staff staffCopy = (Staff) staff.clone(); // ??????????????????????????????pos??????????????????????????????????????????????????????????????????copy??????????????????????????????????????????
		StaffCP.verifyCreate(mrStaffCreate, staffCopy, staffRoleBO, Shared.DBName_Test);
		staff = (Staff) staffCopy.clone(); // ????????????
		//
		JSONObject object = JSONObject.fromObject(mrStaffCreate.getResponse().getContentAsString());
		Staff staff2 = new Staff();
		staff2 = (Staff) staff2.parse1(object.getString(BaseAction.KEY_Object));
		Assert.assertTrue(staff2 != null, "????????????");
		// ??????
		MockHttpSession sessionNewShopBoss = Shared.getStaffLoginSession(mvc, staff2.getPhone(), Shared.PASSWORD_DEFAULT, Shared.DB_SN_Test);
		MvcResult mr3 = mvc.perform( //
				post("/warehousing/updateEx.bx") //
						.param(Warehousing.field.getFIELD_NAME_ID(), String.valueOf(ws.getID()))//
						.param(Warehousing.field.getFIELD_NAME_providerID(), String.valueOf(provider.getID()))//
						.param(Warehousing.field.getFIELD_NAME_warehouseID(), String.valueOf(ws.getWarehouseID()))//
						.param(commIDs, "1,2,3")//
						.param(barcodeIDs, "1,3,5")//
						.param(commNOs, "100,100,20")//
						.param(commPrices, "1.1,1.2,1.3")//
						.param(amounts, "200,200,200")//
						.param("shelfLifes", "36,36,36")//
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionNewShopBoss) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();

		Shared.checkJSONErrorCode(mr3);
	}

	// ?????????????????????????????????????????????????????????
	// @Test
	public void testWarehousingApproveSendMsgToWX() throws Exception {
		HttpSession session = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);
		PurchasingOrder purchasingOrder = createPurchasingOrder();//
		MvcResult result = mvc.perform( //
				post("/warehousing/retrieveNCommEx.bx") //
						.session((MockHttpSession) session) //
						.param(Warehousing.field.getFIELD_NAME_purchasingOrderID(), purchasingOrder.getID() + "") //
		) //
				.andExpect(status().isOk()) //
				.andReturn(); //

		Warehousing ws = new Warehousing();

		MvcResult mr2 = mvc.perform( //
				post("/warehousing/createEx.bx") //
						.param(Warehousing.field.getFIELD_NAME_purchasingOrderID(), String.valueOf(purchasingOrder.getID())) //
						.param(Warehousing.field.getFIELD_NAME_warehouseID(), "1") //
						.param(commIDs, "2") //
						.param(commNOs, "4") //
						.param(commPrices, "11.1") //
						.param(amounts, "11.1") //
						.param(barcodeIDs, "1") //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) result.getRequest().getSession()) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();
		Shared.checkJSONErrorCode(mr2);

		MvcResult result1 = mvc.perform( //
				get("/warehousing/retrieve1Ex.bx?" + Warehousing.field.getFIELD_NAME_ID() + "=" + 1) //
						.contentType(MediaType.APPLICATION_JSON) //
						.session(sessionBoss) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();
		Shared.checkJSONErrorCode(result1);

		MvcResult mr = mvc.perform( //
				get("/warehousing/approveEx.bx?" + Warehousing.field.getFIELD_NAME_warehouseID() + "=1&isModified=1&" + Warehousing.field.getFIELD_NAME_ID() + "=" + ws.getID() + "&" + Warehousing.field.getFIELD_NAME_purchasingOrderID()
						+ "=" + ws.getPurchasingOrderID()) //
								.contentType(MediaType.APPLICATION_JSON) //
								.session((MockHttpSession) result1.getRequest().getSession()) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();
		Shared.checkJSONErrorCode(mr);

		// ????????????????????????????????????????????????????????????
		PurchasingOrder purchasingOrder2 = createPurchasingOrder();//

		Warehousing bm1 = BaseWarehousingTest.DataInput.getWarehousing(); //
		bm1.setPurchasingOrderID(purchasingOrder2.getID());
		Map<String, Object> params1 = bm1.getCreateParam(BaseBO.INVALID_CASE_ID, bm1);
		Warehousing bmCreate = (Warehousing) warehousingMapper.create(params1);
		bmCreate.setIgnoreIDInComparision(true);

		assertTrue(EnumErrorCode.values()[Integer.parseInt(params1.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "??????????????????");

		WarehousingCommodity wsc = DataInput.getWarehousingCommodity();
		wsc.setPrice(20);
		wsc.setWarehousingID(bmCreate.getID());
		Map<String, Object> params2 = wsc.getCreateParam(BaseBO.INVALID_CASE_ID, wsc);
		warehousingCommodityMapper.create(params2);

		assertTrue(EnumErrorCode.values()[Integer.parseInt(params2.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "??????????????????");

		MvcResult mrList = mvc.perform( //
				get("/warehousing/approveListEx.bx?WSListID=" + bmCreate.getID() + "&isModified=1&" + Warehousing.field.getFIELD_NAME_purchasingOrderID() + "=" + bmCreate.getPurchasingOrderID()) //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) result1.getRequest().getSession()) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();
		Shared.checkJSONErrorCode(mrList);
	}

	private Provider createProvider() throws CloneNotSupportedException, InterruptedException {
		// ?????????????????????
		Provider p = BaseTestNGSpringContextTest.DataInput.getProvider();
		String error = p.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertTrue(error == "", "????????????checkCreate?????????");
		Map<String, Object> params = p.getCreateParam(BaseBO.INVALID_CASE_ID, p);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Provider providerCreate = (Provider) providerMapper.create(params); // ...
		p.setIgnoreIDInComparision(true);
		if (p.compareTo(providerCreate) != 0) {
			Assert.assertTrue(false, "???????????????????????????DB??????????????????");
		}
		Assert.assertTrue(providerCreate != null && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "??????????????????");

		return providerCreate;
	}

	protected Barcodes retrieveNBarcodes(Barcodes barcodes) {
		Map<String, Object> params = barcodes.getRetrieveNParam(BaseBO.INVALID_CASE_ID, barcodes);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> retrieveN = barcodesMapper.retrieveN(params);
		//
		assertTrue(retrieveN.size() > 0 && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError);
		//
		for (BaseModel bm : retrieveN) {
			Barcodes b = (Barcodes) bm;
			b.setOperatorStaffID(1); // ...TODO ??????????????????????????????
			//
			String error = b.checkCreate(BaseBO.INVALID_CASE_ID);
			Assert.assertEquals(error, "");
			b.setOperatorStaffID(0); // ?????????
		}
		Barcodes bc = (Barcodes) retrieveN.get(0);
		return bc;
	}
}
