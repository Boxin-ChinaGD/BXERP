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
import com.bx.erp.test.BaseRetailTradeTest;
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
		
		// 由于本测试数据量超过缓存限制，所以在这里把缓存限制设置成最大，然后再在tear down设置回来。
		ConfigCacheSize ccs = new ConfigCacheSize();
		ccs.setValue(String.valueOf(BaseAction.PAGE_SIZE_Infinite));
		ccs.setID(EnumConfigCacheSizeCache.ECC_CommodityCacheSize.getIndex());
		ccs.setName(EnumConfigCacheSizeCache.ECC_CommodityCacheSize.getName());
		CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_ConfigCacheSize).write1(ccs, Shared.DBName_Test, STAFF_ID4);
		// 删除普通对象的缓存,使他获取最新的缓存
		Commodity commodity = new Commodity();
		commodity.setID(1);
		CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_Commodity).delete1(commodity);
		commodity.setID(10);
		CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_Commodity).delete1(commodity);
	}

	@AfterClass
	public void tearDown() {
		Shared.printTestClassEndInfo();
		// 由于本测试数据量超过缓存限制，在setup把缓存限制改了，所以在这里把缓存限制设置回来。
		ConfigCacheSize ccs = new ConfigCacheSize();
		ccs.setValue("100");
		ccs.setID(EnumConfigCacheSizeCache.ECC_CommodityCacheSize.getIndex());
		ccs.setName(EnumConfigCacheSizeCache.ECC_CommodityCacheSize.getName());
		CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_ConfigCacheSize).write1(ccs, Shared.DBName_Test, STAFF_ID4);
	}

	public Warehousing createWarehousing(Warehousing warehousing) throws CloneNotSupportedException, InterruptedException {
		warehousing.setProviderID(1);
		String error = warehousing.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertTrue(error == "", "入库单的checkCreate不通过");
		Map<String, Object> paramsForCreate = warehousing.getCreateParam(BaseBO.INVALID_CASE_ID, warehousing);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Warehousing wsCreate = (Warehousing) warehousingMapper.create(paramsForCreate); // ...
		//
		warehousing.setIgnoreIDInComparision(true);
		if (warehousing.compareTo(wsCreate) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		assertTrue(wsCreate != null && EnumErrorCode.values()[Integer.parseInt(paramsForCreate.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "创建对象失败");

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
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}

		return (WarehousingCommodity) wcCreate.clone();
	}

	public PurchasingOrder createPurchasingOrder() throws CloneNotSupportedException, InterruptedException {

		System.out.println("--------------------------------创建采购单---------------------------------");
		PurchasingOrder po = new PurchasingOrder();
		po.setStaffID(Shared.BossID);
		po.setProviderID(1);
		po.setRemark("Remark:" + String.valueOf(System.currentTimeMillis()).substring(6));
		po.setShopID(Shared.DEFAULT_Shop_ID);
		Thread.sleep(1);
		String error = po.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertTrue(error == "", "采购订单的checkCreate不通过");
		Map<String, Object> paramsForCreate = po.getCreateParam(BaseBO.INVALID_CASE_ID, po);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		PurchasingOrder poCreate = (PurchasingOrder) purchasingOrderMapper.create(paramsForCreate);
		//
		assertTrue(poCreate != null && EnumErrorCode.values()[Integer.parseInt(paramsForCreate.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError,
				paramsForCreate.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		//
		po.setIgnoreIDInComparision(true);
		if (po.compareTo(poCreate) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}

		System.out.println("--------------------------------创建采购单商品---------------------------------");

		PurchasingOrderCommodity poc1 = new PurchasingOrderCommodity();
		poc1.setCommodityID(1);
		poc1.setCommodityNO(20);
		poc1.setPurchasingOrderID(poCreate.getID());
		poc1.setPriceSuggestion(1);
		poc1.setBarcodeID(1);
		error = poc1.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertTrue(error == "", "采购商品的checkCreate不通过");
		Map<String, Object> params = poc1.getCreateParam(BaseBO.INVALID_CASE_ID, poc1);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		PurchasingOrderCommodity pocCreate = (PurchasingOrderCommodity) purchasingOrderCommodityMapper.create(params);
		//
		poc1.setIgnoreIDInComparision(true);
		if (poc1.compareTo(pocCreate) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		Assert.assertTrue(pocCreate != null && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "创建对象失败");

		System.out.println("--------------------------------审核采购单---------------------------------");

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
		Shared.caseLog("CASE1:没有参数查询所有,RN时前端不需要用到warehousing的从表warehousingcommodity和listCommodity，所以不用这些返回数据，R1时前端才会查询这些信息\"");
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
		Assert.assertTrue(whCommList.size() > 0, "返回的warehousing没有从表字段");// size>0说明返回的warehousing有从表字段
		for (List<WarehousingCommodity> ls : whCommList) {
			Assert.assertTrue(ls.size() == 0, "warehousing返回了从表信息，RN时前端没有用到这些数据");
		}
		List<List<Commodity>> commList = JsonPath.read(o, "$.warehousingList[*].listCommodity");
		for (List<Commodity> ls2 : commList) {
			Assert.assertTrue(ls2.size() == 0, "warehousing返回了listCommodity信息，RN时前端没有用到这些数据");
		}
		Assert.assertTrue(createWarehousing.getID() == list.get(0));
	}

	@Test
	public void testRetrieveNEx2() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("CASE2:根据ID查询");
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
		Shared.caseLog("CASE3:根据仓库ID查询");
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
		Shared.caseLog("CASE4:根据业务员ID查");
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
		Shared.caseLog("CASE5:根据采购单ID查");
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
		Shared.caseLog("CASE6:根据采购订单ID和仓库ID查");
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
		Shared.caseLog("CASE7:根据业务员跟仓库ID查");
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
		Shared.caseLog("CASE8:根据采购订单跟ID查");
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
		Shared.caseLog("CASE9:根据采购订单ID，仓库ID，业务员ID查");
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
		Shared.caseLog("CASE10:根据根据所有条件查询");
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
		Shared.caseLog("CASE11:没有权限查询");
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
		Shared.caseLog("CASE12:根据不存在的ID查询");
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
		Shared.caseLog("CASE13:根据不存在的staffID查询");
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
		Shared.caseLog("CASE14:根据不存在的warehouseID查询");
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
		Shared.caseLog("CASE15:根据不存在的purchasingOrderID查询");
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
		Shared.caseLog("CASE16:根据供应商ID和采购订单查");
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
		Shared.caseLog("CASE17:根据不存在的供应商ID查");

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

		assertTrue(wcR1List.size() == 0 && EnumErrorCode.values()[Integer.parseInt(paramsForRetrieveN.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "删除对象失败");
	}

	@Test
	public void testRetrieve1Ex1() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("case1:根据ID查询");
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
		// 验证从表信息
		List<?> wCommodityList = ws.getJSONArray(Warehousing.field.getFIELD_NAME_listSlave1());
		for (Object object : wCommodityList) {
			assertTrue(JSONObject.fromObject(object).getInt(WarehousingCommodity.field.getFIELD_NAME_warehousingID()) == ws.getInt(Warehousing.field.getFIELD_NAME_ID()));
		}

	}

	@Test
	public void testRetrieve1Ex2() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("case2：没有权限");
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
		Shared.caseLog("case3：用不存在的ID查找");
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
		Shared.caseLog("case4:查询已审核的入库单");
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
		// 验证从表信息
		List<?> wCommodityList = ws.getJSONArray(Warehousing.field.getFIELD_NAME_listSlave1());
		for (Object object : wCommodityList) {
			assertTrue(JSONObject.fromObject(object).getInt(WarehousingCommodity.field.getFIELD_NAME_warehousingID()) == ws.getInt(Warehousing.field.getFIELD_NAME_ID()));
		}
	}

	@Test
	public void testRetrieve1Ex5() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("case5:传入的参数，不符合要求");
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
		Shared.caseLog("case1:根据ID查询，有采购订单与之相关联，需要返回采购订单的单号");

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
		// 验证从表信息
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
		Assert.assertTrue(ws.getString(Warehousing.field.getFIELD_NAME_purchasingOrderSN()).equals(purchasingOrderR1.getSn()), "R1入库单时，没有返回采购订单的单号");
	}

	@Test
	public void testCreateEx1() throws Exception { // ...
		Shared.printTestMethodStartInfo();
		HttpSession session = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);
		//
		Shared.caseLog("case1:正常添加");
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
		whousing.setStaffID(4);// 15854320895手机号登录

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
		Assert.assertTrue(createDatetime.getTime() == (wsR1.getCreateDatetime().getTime())); // ... TODO 需要增加从表验证 创建 修改 审核

		WarehousingCP.verifyCreate(mr2, whousing, Shared.DBName_Test);
	}

	@Test
	public void testCreateEx2() throws Exception { // ...
		Shared.printTestMethodStartInfo();
		Shared.caseLog("case2:创建入库单，采购单状态为2");
		PurchasingOrder po2 = createPurchasingOrder();
		//
		po2.setStatus(2);
		Map<String, Object> params3 = po2.getUpdateParam(BaseBO.CASE_UpdatePurchasingOrderStatus, po2);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		PurchasingOrder poUpdateStatus = purchasingOrderMapper.updateStatus(params3);
		po2.setIgnoreIDInComparision(true);
		//
		if (po2.compareTo(poUpdateStatus) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		// 检查点
		Warehousing whousing = new Warehousing();
		whousing.setPurchasingOrderID(po2.getID());
		whousing.setShopID(Shared.DEFAULT_Shop_ID);
		whousing.setProviderID(1);
		whousing.setWarehouseID(1);
		whousing.setStaffID(4);// 15854320895手机号登录
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
		Shared.caseLog("case3：在采购订单全部入库的状态下，不可以再引用之");
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
		Shared.caseLog("case4:采购订单的状态为未审核和删除状态时不能进行入库");
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
		Shared.caseLog("case5:不引用采购订单");
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
		Shared.caseLog("case6:没有创建入库单权限");
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

		Shared.caseLog("case7:不能使用不存在的仓库进行创建");
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

		Shared.caseLog("case8:不能使用不存在的供应商ID进行创建");
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
		Shared.caseLog("case9:用多包装商品创建有一个入库单商品");
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
		Shared.checkJSONMsg(mr, "不能入库单品以外的商品（商品乐事青椒味薯片3的类型不是单品）", "错误信息不一致");
	}

	@Test
	public void testCreateEx10() throws Exception { // ...
		Shared.printTestMethodStartInfo();
		HttpSession session = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);
		//
		Shared.caseLog("case10:创建一个commodityID不存在的入库商品表");
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
		Shared.checkJSONMsg(mr, "不能入库一个不存在的商品", "错误信息不一致");
	}

	@Test
	public void testCreateEx11() throws Exception { // ...
		Shared.printTestMethodStartInfo();
		HttpSession session = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);
		//
		Shared.caseLog("case11:创建一个iBarcodeID不存在的入库商品表");
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
		Shared.checkJSONMsg(mr, "该条形码不存在", "错误信息不正确");

	}

	@Test
	public void testCreateEx12() throws Exception { // ...
		Shared.printTestMethodStartInfo();
		HttpSession session = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);
		//
		Shared.caseLog("case12:用组合商品创建有一个入库单商品");
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
		Shared.checkJSONMsg(mr, "不能入库单品以外的商品（商品乐事香辣味薯片的类型不是单品）", "错误信息不一致");

	}

	@Test
	public void testCreateEx13() throws Exception { // ...
		Shared.printTestMethodStartInfo();
		HttpSession session = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);
		//
		Shared.caseLog("case13:用已删除商品创建有一个入库单商品（和case10重复）");
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
		Shared.checkJSONMsg(mr, "不能入库一个不存在的商品", "错误信息不一致");

	}

	@Test
	public void testCreateEx14() throws Exception { // ...
		Shared.printTestMethodStartInfo();
		HttpSession session = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);
		//
		Shared.caseLog("case14:一张入库单商品中有两个相同的商品");
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
		Assert.assertTrue("".equals(mr.getResponse().getContentAsString()), "action返回的错误信息不正确");
	}

	@Test
	public void testCreateEx15() throws Exception { // ...
		Shared.printTestMethodStartInfo();
		HttpSession session = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);
		//
		Shared.caseLog("case15:创建入库单商品的本次收货数量为0");
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
		Shared.caseLog("case16:传入的参数，不符合要求");
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
		Shared.caseLog("case17:创建没有入库商品的入库单");
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
		Assert.assertTrue(json.length() == 0, "CASE17测试失败！期望的是返回null");
	}

	@Test
	public void testCreateEx18() throws Exception { // ...
		Shared.printTestMethodStartInfo();
		Shared.caseLog("case18:创建的入库单商品单价大于" + FieldFormat.MAX_OneCommodityPrice + "，创建失败");
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
		Shared.caseLog("case19:创建的入库单商品数量大于" + FieldFormat.MAX_OneCommodityNO + "，创建失败");
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
		Shared.caseLog("case20:创建的入库单商品传递的commIDs是空串");
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
		Assert.assertTrue(o.length() == 0, "case20测试失败！返回的结果不是期望的");
	}

	@Test
	public void testCreateEx21() throws Exception { // ...
		Shared.printTestMethodStartInfo();
		HttpSession session = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);
		//
		Shared.caseLog("case21:创建一个商品ID和条形码ID不对应的入库商品表");
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
		Shared.checkJSONMsg(mr, "条形码与商品实际条形码不对应", "错误信息不正确");

	}
	
	@Test
	public void testCreateEx22() throws Exception { // ...
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case22:传入的门店ID为虚拟总部");
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
		Shared.caseLog("case23:门店A的店长创建门店B的入库，创建失败，不能跨店审核");
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
		whousing.setStaffID(4);// 15854320895手机号登录
		
		// 创建门店B的店长账号
		Shop shopGet = BaseShopTest.DataInput.getShop();
		Shop shopCreated = BaseShopTest.createViaCompanyAction(shopGet, mvc, mapBO, sessionOP, EnumErrorCode.EC_NoError);
		// 为该门店新增店长
		String Staff_Phone = Shared.getValidStaffPhone();
		MvcResult ret = BaseStaffTest.getToken(sessionBoss, mvc, Staff_Phone, Staff.FOR_CreateNewStaff);
		//
		String encrypt = Shared.encrypt(ret, Shared.PASSWORD_DEFAULT);
		Staff staff = BaseStaffTest.DataInput.getStaff();
		staff.setShopID(shopCreated.getID());
		staff.setName("店长" + Shared.generateCompanyName(6));
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
		// 结果验证：检查错误码
		Shared.checkJSONErrorCode(mrStaffCreate);
		Staff staffCopy = (Staff) staff.clone(); // 由于结果验证会重新对pos赋值，影响下面的其它测试用例，所以先保持一个copy，后面再恢复用于其它测试用例
		StaffCP.verifyCreate(mrStaffCreate, staffCopy, staffRoleBO, Shared.DBName_Test);
		staff = (Staff) staffCopy.clone(); // 恢复原样
		//
		JSONObject object = JSONObject.fromObject(mrStaffCreate.getResponse().getContentAsString());
		Staff staff2 = new Staff();
		staff2 = (Staff) staff2.parse1(object.getString(BaseAction.KEY_Object));
		Assert.assertTrue(staff2 != null, "解析异常");
		// 登录
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
		Shared.caseLog("case24:门店A的店长创建门店B()的入库，创建失败，不能跨店审核");
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
		whousing.setStaffID(4);// 15854320895手机号登录
		// 为该门店新增店长
		String Staff_Phone = Shared.getValidStaffPhone();
		MvcResult ret = BaseStaffTest.getToken(sessionBoss, mvc, Staff_Phone, Staff.FOR_CreateNewStaff);
		//
		String encrypt = Shared.encrypt(ret, Shared.PASSWORD_DEFAULT);
		Staff staff = BaseStaffTest.DataInput.getStaff();
		staff.setShopID(BaseAction.VirtualShopID);
		staff.setName("店长" + Shared.generateCompanyName(6));
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
		// 结果验证：检查错误码
		Shared.checkJSONErrorCode(mrStaffCreate);
		Staff staffCopy = (Staff) staff.clone(); // 由于结果验证会重新对pos赋值，影响下面的其它测试用例，所以先保持一个copy，后面再恢复用于其它测试用例
		StaffCP.verifyCreate(mrStaffCreate, staffCopy, staffRoleBO, Shared.DBName_Test);
		staff = (Staff) staffCopy.clone(); // 恢复原样
		//
		JSONObject object = JSONObject.fromObject(mrStaffCreate.getResponse().getContentAsString());
		Staff staff2 = new Staff();
		staff2 = (Staff) staff2.parse1(object.getString(BaseAction.KEY_Object));
		Assert.assertTrue(staff2 != null, "解析异常");
		// 登录
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
	// System.out.println("----------------------正常创建-----------------------------------");
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
		Shared.caseLog("Case1:先修改再审核，int1=1");
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
		// 审核入库单前，查询商品的F_NO，检查点判断入库单审核后F_NO是否正确计算
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
		WarehousingCP.verifyApprove(mr, ws, warehousingBO, purchasingOrderBO, commList, commodityHistoryBO, messageBO, Shared.DBName_Test, BaseRetailTradeTest.defaultShopID);
	}

	@Test
	public void testApproveEx2() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("Case2:直接审核，不用修改，int1=0");
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
		WarehousingCP.verifyApprove(mr, ws, warehousingBO, purchasingOrderBO, commList, commodityHistoryBO, messageBO, Shared.DBName_Test, BaseRetailTradeTest.defaultShopID);
	}

	@Test
	public void testApproveEx3() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("Case3:未传参数");

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
		Assert.assertTrue(o.length() == 0, "CSE3测试失败！返回的结果不是期望的");
	}

	@Test
	public void testApproveEx4() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case4:入库单已审核");
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

		Shared.caseLog("Case5:用户没有审核权限");
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

		Shared.caseLog("Case6:从对象中获取入库单ID审核,考虑采购订单为NULL的情况，返回一个新的错误码,正确设置param（错误码=EnumErrorCode.EC_NoError，MSG=这个入库单没有对应的采购订单");
		// 创建一个未审核，没有相应采购订单的入库
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
		String msg = "审核单号为：" + ws.getSn() + "的入库单成功 ！";
		Shared.checkJSONErrorCode(mr);
		Shared.checkJSONMsg(mr, msg, "返回的msg与预期的不一致");
		WarehousingCP.verifyApprove(mr, ws, warehousingBO, purchasingOrderBO, commList, commodityHistoryBO, messageBO, Shared.DBName_Test, BaseRetailTradeTest.defaultShopID);
	}

	@Test
	public void testApproveEx7() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case7:检查商品缓存是否更新成功");
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
			Assert.assertTrue(false, "查找商品失败！");
		}
		if (wc.getNO() != comm.getNO()) {
			Assert.assertTrue(false, "商品库存缓存更新失败");
		}
	}

	@Test
	public void testApproveEx8() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("Case8:入库单ID不存在");
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
		// 删除创建的测试数据
		deleteWS(ws);
		deleteWC(wc);
		BaseCommodityTest.deleteCommodityViaMapper(createCommodity1, EnumErrorCode.EC_NoError);
	}

	@Test
	public void testApproveEx9() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case9:先修改再审核，int1=1时，仓库不存在");
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
		// 删除创建的测试数据
		deleteWS(ws);
		deleteWC(wc);
		BaseCommodityTest.deleteCommodityViaMapper(createCommodity1, EnumErrorCode.EC_NoError);
		BaseCommodityTest.deleteCommodityViaMapper(createCommodity2, EnumErrorCode.EC_NoError);
		BaseCommodityTest.deleteCommodityViaMapper(createCommodity3, EnumErrorCode.EC_NoError);
	}

	@Test
	public void testApproveEx10() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case10:先修改再审核，int1=1时，供应商不存在");
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
		// 删除创建的测试数据
		deleteWS(ws);
		deleteWC(wc);
		BaseCommodityTest.deleteCommodityViaMapper(createCommodity1, EnumErrorCode.EC_NoError);
		BaseCommodityTest.deleteCommodityViaMapper(createCommodity2, EnumErrorCode.EC_NoError);
		BaseCommodityTest.deleteCommodityViaMapper(createCommodity3, EnumErrorCode.EC_NoError);
	}

	@Test
	public void testApproveEx11() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case11:先修改再审核，int1=1时，商品ID不存在");
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
		// 删除创建的测试数据
		deleteWS(ws);
		deleteWC(wc);
		BaseCommodityTest.deleteCommodityViaMapper(createCommodity1, EnumErrorCode.EC_NoError);
		BaseCommodityTest.deleteCommodityViaMapper(createCommodity2, EnumErrorCode.EC_NoError);
		BaseCommodityTest.deleteCommodityViaMapper(createCommodity3, EnumErrorCode.EC_NoError);
	}

	@Test
	public void testApproveEx12() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case12:先修改再审核，int1=1时，入库数量为0");
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
		// 删除创建的测试数据
		deleteWS(ws);
		deleteWC(wc);
		BaseCommodityTest.deleteCommodityViaMapper(createCommodity1, EnumErrorCode.EC_NoError);
		BaseCommodityTest.deleteCommodityViaMapper(createCommodity2, EnumErrorCode.EC_NoError);
		BaseCommodityTest.deleteCommodityViaMapper(createCommodity3, EnumErrorCode.EC_NoError);
	}

	@Test
	public void testApproveEx13() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case13:先修改再审核，int1=1时，参数维度不一致");
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

		Assert.assertTrue("".equals(mr.getResponse().getContentAsString()), "action返回的错误码不正确");
		// 删除创建的测试数据
		deleteWS(ws);
		deleteWC(wc);
		BaseCommodityTest.deleteCommodityViaMapper(createCommodity1, EnumErrorCode.EC_NoError);
		BaseCommodityTest.deleteCommodityViaMapper(createCommodity2, EnumErrorCode.EC_NoError);
		BaseCommodityTest.deleteCommodityViaMapper(createCommodity3, EnumErrorCode.EC_NoError);
	}

	@Test
	public void testApproveEx14() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case14:先修改再审核，int1=1时，传入参数缺失");
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
		Assert.assertTrue("".equals(mr.getResponse().getContentAsString()), "action返回的错误码不正确");
		// 删除创建的测试数据
		deleteWS(ws);
		deleteWC(wc);
		BaseCommodityTest.deleteCommodityViaMapper(createCommodity1, EnumErrorCode.EC_NoError);
		BaseCommodityTest.deleteCommodityViaMapper(createCommodity2, EnumErrorCode.EC_NoError);
		BaseCommodityTest.deleteCommodityViaMapper(createCommodity3, EnumErrorCode.EC_NoError);
	}

	@Test
	public void testApproveEx15() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case15:先修改再审核，int1=1时，传入商品ID重复");
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
		Assert.assertTrue("".equals(mr.getResponse().getContentAsString()), "action返回的错误信息不正确");
		// 删除创建的测试数据
		deleteWS(ws);
		deleteWC(wc);
		BaseCommodityTest.deleteCommodityViaMapper(createCommodity1, EnumErrorCode.EC_NoError);
		BaseCommodityTest.deleteCommodityViaMapper(createCommodity2, EnumErrorCode.EC_NoError);
		BaseCommodityTest.deleteCommodityViaMapper(createCommodity3, EnumErrorCode.EC_NoError);
	}

	@Test
	public void testApproveEx16() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("Case16:先修改再审核，int1=1，审核成功后，商品价格有变动，需要发送微信消息给老板");
		// 这个测试应该像上面重构，等LLL发的jira有人完成重构采购订单后，重构入库单时。
		// 这个测试的步骤应是:
		// 1、创建商品，创建采购订单。
		// 2、创建采购订单，采购订单商品为创建的商品
		// 3、创建入库单，入库单商品为刚创建的商品，入库单的采购订单ID为刚创建的采购订单ID
		// 4、后面调用Warehousing的approveEx接口参数对应改变，审核时入库价commPrices要和采购订单的采购价不一样
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
		// 查找商品对应的barcodeID
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
		// TODO dev的nbr老板的openid如果不为null，并且能正常发送消息，或者openid为null，则错误码应为no_Error
//		如果老板的openid不为null，又发送不成功，则错误码为partSuccess
//		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_PartSuccess); 
		Shared.checkJSONErrorCode(mr);
		WarehousingCP.verifyApprove(mr, ws, warehousingBO, purchasingOrderBO, commList, commodityHistoryBO, messageBO, Shared.DBName_Test, BaseRetailTradeTest.defaultShopID);
		// // 发送微信消息
		// JSONObject object =
		// JSONObject.fromObject(mr.getResponse().getContentAsString());
		// String msg = (String) object.get(BaseAction.KEY_HTMLTable_Parameter_msg);
		// Assert.assertTrue(msg.contains("发送微信消息失败!"), "预期发送微信消息失败，但是发送成功"); //
		// 发送微信消息成功，需要设置白名单和更改DB中店长的openid，才能测试发送微信消息成功
	}

	@Test
	public void testApproveEx17() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("Case17:直接审核，不用修改，int1=0, 审核后返回关联的采购订单单号，放在purchasingOrderSN");
		Warehousing warehousing = BaseWarehousingTest.DataInput.getWarehousing();
		warehousing.setPurchasingOrderID(1);
		Warehousing ws = createWarehousing(warehousing);
		// 创建入库商品
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
		// TODO dev的nbr老板的openid如果不为null，并且能正常发送消息，或者openid为null，则错误码应为no_Error
//		如果老板的openid不为null，又发送不成功，则错误码为partSuccess
//		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_PartSuccess);
		Shared.checkJSONErrorCode(mr);
		WarehousingCP.verifyApprove(mr, ws, warehousingBO, purchasingOrderBO, commList, commodityHistoryBO, messageBO, Shared.DBName_Test, BaseRetailTradeTest.defaultShopID);
		// 发送微信消息
		// JSONObject object =
		// JSONObject.fromObject(mr.getResponse().getContentAsString());
		// String msg = (String) object.get(BaseAction.KEY_HTMLTable_Parameter_msg);
		// Assert.assertTrue(msg.contains("发送微信消息失败!"), "预期发送微信消息失败，但是发送成功");
		// // 发送微信消息成功，需要设置白名单和更改DB中店长的openid，才能测试发送微信消息成功
		//
		String json = mr.getResponse().getContentAsString();
		JSONObject jsonObject = JSONObject.fromObject(json);
		JSONObject wsJson = jsonObject.getJSONObject(BaseAction.KEY_Object);
		// 验证从表信息
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
		Assert.assertTrue(wsJson.getString(Warehousing.field.getFIELD_NAME_purchasingOrderSN()).equals(purchasingOrderR1.getSn()), "审核入库单时，没有返回采购订单的单号");
	}

	@Test
	public void testApproveEx18() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case18:审核入库单时，没有传入库商品信息，审核失败"); // int1为1是修改后再审核
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
		Assert.assertTrue(json.length() == 0, "CASE18测试失败！返回的结果不是期望的");
		// 删除创建的测试数据
		deleteWS(ws);
		deleteWC(wc);
		BaseCommodityTest.deleteCommodityViaMapper(createCommodity1, EnumErrorCode.EC_NoError);
	}

	@Test
	public void testApproveEx19() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("Case19:审核没有入库商品的入库单"); // int1为0是没有修改直接审核
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
		Shared.caseLog("Case20:审核不存在的入库单"); // int1为1是修改后再审核
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
		Shared.caseLog("Case21:审核不存在的入库单(修改再审核)"); // int1为1是修改后再审核
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
		Assert.assertTrue(json.length() == 0, "CASE21测试失败！返回的结果不是期望的");
	}

	@Test
	public void testApproveEx22() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("Case22:先修改再审核，int1=1，审核时修改，把入库单商品数量修改成大于" + FieldFormat.MAX_OneCommodityNO + "，审核失败");
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
		// 审核入库单前，查询商品的F_NO，检查点判断入库单审核后F_NO是否正确计算
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
		Shared.caseLog("Case23:先修改再审核，int1=1，审核时修改，把入库单商品单价修改成大于" + FieldFormat.MAX_OneCommodityPrice + "，审核失败");
		Commodity commodity1 = BaseCommodityTest.DataInput.getCommodity();
		List<List<BaseModel>> listBM1 = BaseCommodityTest.createCommodityViaMapper(BaseBO.CASE_Commodity_CreateSingle, commodity1);
		Commodity createCommodity1 = (Commodity) listBM1.get(0).get(0);
		Barcodes createBarcodes1 = (Barcodes) listBM1.get(1).get(0);
		// 审核入库单前，查询商品的F_NO，检查点判断入库单审核后F_NO是否正确计算
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

		Shared.caseLog("Case24:审核前修改成入库多包装商品");
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

		Shared.caseLog("CASE25: 审核时修改，把入库单商品传能部分创建成功的商品，审核失败");
		// 目前在修改主表前会checkCreate检查从表，这样理论上不会出现部分成功（EC_PartSuccess）的情况，自动化测试测试不出（要DB错误才有可能）
	}
	
	@Test
	public void testApproveEx26() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("Case26:门店A的店长创建入库，门店B的店长进行审核，审核失败，不能跨店审核");
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
		// 创建门店B的店长账号
		Shop shopGet = BaseShopTest.DataInput.getShop();
		Shop shopCreated = BaseShopTest.createViaCompanyAction(shopGet, mvc, mapBO, sessionOP, EnumErrorCode.EC_NoError);
		// 为该门店新增店长
		String Staff_Phone = Shared.getValidStaffPhone();
		MvcResult ret = BaseStaffTest.getToken(sessionBoss, mvc, Staff_Phone, Staff.FOR_CreateNewStaff);
		//
		String encrypt = Shared.encrypt(ret, Shared.PASSWORD_DEFAULT);
		Staff staff = BaseStaffTest.DataInput.getStaff();
		staff.setShopID(shopCreated.getID());
		staff.setName("店长" + Shared.generateCompanyName(6));
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
		// 结果验证：检查错误码
		Shared.checkJSONErrorCode(mrStaffCreate);
		Staff staffCopy = (Staff) staff.clone(); // 由于结果验证会重新对pos赋值，影响下面的其它测试用例，所以先保持一个copy，后面再恢复用于其它测试用例
		StaffCP.verifyCreate(mrStaffCreate, staffCopy, staffRoleBO, Shared.DBName_Test);
		staff = (Staff) staffCopy.clone(); // 恢复原样
		//
		JSONObject object = JSONObject.fromObject(mrStaffCreate.getResponse().getContentAsString());
		Staff staff2 = new Staff();
		staff2 = (Staff) staff2.parse1(object.getString(BaseAction.KEY_Object));
		Assert.assertTrue(staff2 != null, "解析异常");
		// 登录
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
		Shared.caseLog("Case27:门店A的店长创建入库，门店B(虚拟总部)的店长进行审核，审核成功，虚拟总部店长可以作为多个门店的店长");
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
		// 为虚拟总部新增店长
		String Staff_Phone = Shared.getValidStaffPhone();
		MvcResult ret = BaseStaffTest.getToken(sessionBoss, mvc, Staff_Phone, Staff.FOR_CreateNewStaff);
		//
		String encrypt = Shared.encrypt(ret, Shared.PASSWORD_DEFAULT);
		Staff staff = BaseStaffTest.DataInput.getStaff();
		staff.setShopID(BaseAction.VirtualShopID);
		staff.setName("店长" + Shared.generateCompanyName(6));
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
		// 结果验证：检查错误码
		Shared.checkJSONErrorCode(mrStaffCreate);
		Staff staffCopy = (Staff) staff.clone(); // 由于结果验证会重新对pos赋值，影响下面的其它测试用例，所以先保持一个copy，后面再恢复用于其它测试用例
		StaffCP.verifyCreate(mrStaffCreate, staffCopy, staffRoleBO, Shared.DBName_Test);
		staff = (Staff) staffCopy.clone(); // 恢复原样
		//
		JSONObject object = JSONObject.fromObject(mrStaffCreate.getResponse().getContentAsString());
		Staff staff2 = new Staff();
		staff2 = (Staff) staff2.parse1(object.getString(BaseAction.KEY_Object));
		Assert.assertTrue(staff2 != null, "解析异常");
		// 登录
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
	// System.out.println("------------------------------Case1:审核1个的入库单------------------------");
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
	// System.out.println("-----------------------------------Case2:审核多个的入库单------------------------------");
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
	// System.out.println("-----------------------------------Case3:审核一个不可审核的入库单------------------------------");
	// String msg1 = "入库单" + 4 + "审核失败，因为它已经审核过了。<br />";
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
	// Shared.checkJSONMsg(mr3, msg1, "返回的结果不一致");
	//
	// System.out.println("-----------------------------------Case4:审核多个不可审核的入库单------------------------------");
	// String msg2 = "入库单" + 11 + "审核失败，因为它已经审核过了。<br />";
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
	// Shared.checkJSONMsg(mr4, msg1 + msg2, "返回的结果不一致");
	//
	// System.out.println("------------------------------Case5:审核0个入库单-----------------------");
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
	// System.out.println("-----------------------case6：审核一个不存在的采购单---------------");
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
	// System.out.println(e.getMessage());// 更新缓存时会出现空指针问题
	// assertTrue(e.getMessage().equals("Request processing failed; nested exception
	// is java.lang.NullPointerException"));
	// }
	//
	// System.out.println("-----------------------------------Case7:审核多个入库单，其中一个不可审核------------------------------");
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
	// // Shared.checkJSONMsg(mr7, msg1, "返回的结果不一致");
	// Shared.checkJSONErrorCode(mr7, EnumErrorCode.EC_PartSuccess);
	//
	// System.out.println("-----------------------------------Case8:没有权限进行审核------------------------------");
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
	// System.out.println("-Case9:审核多个的入库单,考虑采购订单为NULL的情况，返回一个新的错误码,正确设置param（错误码=EnumErrorCode.EC_NoError，MSG=这个入库单没有对应的采购订单--");
	// // 创建两个个未审核，没有相应采购订单的入库
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
	// == EnumErrorCode.EC_NoError, "创建对象失败");
	// //
	// wsCreate91.setIgnoreIDInComparision(true);
	// if (wsCreate91.compareTo(wsCreated91) != 0) {
	// Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
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
	// == EnumErrorCode.EC_NoError, "创建对象失败");
	// //
	// wsCreate92.setIgnoreIDInComparision(true);
	// if (wsCreate92.compareTo(wsCreated92) != 0) {
	// Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
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
	// String msg91 = "ID为" + wsCreated91.getID() + "的入库单没有对应的采购订单";
	// String msg92 = "ID为" + wsCreated92.getID() + "的入库单没有对应的采购订单";
	// Shared.checkJSONErrorCode(mr9);
	// Shared.checkJSONMsg(mr9, msg91 + "<br />" + msg92 + "<br />",
	// "返回的msg与预期的不一致");
	//
	// System.out.println("-Case10:审核多个的入库单,考虑采购订单为NULL的情况，返回一个新的错误码,正确设置param（错误码=EnumErrorCode.EC_NoError，MSG=这个入库单没有对应的采购订单--");
	// // 创建两个个未审核的入库单，一个有相应的采购订单，一个没有相应的采购订单
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
	// == EnumErrorCode.EC_NoError, "创建对象失败");
	// //
	// wsCreate102.setIgnoreIDInComparision(true);
	// if (wsCreate102.compareTo(wsCreated102) != 0) {
	// Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
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
	// String msg101 = "审核ID为：" + wsCreated101.getID() + "的入库单成功 ！";
	// String msg102 = "ID为" + wsCreated102.getID() + "的入库单没有对应的采购订单";
	// Shared.checkJSONMsg(mr10, msg101 + "<br />" + msg102 + "<br />",
	// "返回的msg与预期的不一致");
	// }

	@Test
	public void testRetrieveNByFieldsEx1() throws Exception {
		// 在warehousing/retrieveNByFieldsEx.bx中设置默认值，这里不传参代表默认值
		Shared.printTestMethodStartInfo();
		Warehousing warehousing = BaseWarehousingTest.DataInput.getWarehousing();
		Warehousing ws = createWarehousing(warehousing);
		SimpleDateFormat sdf = new SimpleDateFormat(BaseAction.DATE_FORMAT_RetailTradeSN);
		String SN = "RK" + sdf.format(new Date()).substring(0, 8);
		Shared.caseLog("case1：最小长度(10)的入库单单号模糊查询入库单");
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

	// 因为warehousing/retrieveNByFieldsEx.bx不需要从表数据给前端，所以先注释这个测试方法
	// @Test
	public void testRetrieveNByFieldsEx2() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("case2：根据商品名称模糊查询入库单");
		//
		MvcResult mr2 = mvc.perform( //
				post("/warehousing/retrieveNByFieldsEx.bx") //
						.param(Warehousing.field.getFIELD_NAME_queryKeyword(), "A可乐18") //
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

		String string = "A可乐18";
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
		Shared.caseLog("case3：根据供应商名称模糊查询入库单");
		//
		MvcResult mr3 = mvc.perform( //
				post("/warehousing/retrieveNByFieldsEx.bx") //
						.param(Warehousing.field.getFIELD_NAME_queryKeyword(), "默认") //
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

		String providerName = "默认";

		// 搜索出相应的供应商进行比对
		for (int i = 0; i < list2.size(); i++) {
			Provider p = new Provider();
			int i1 = list2.get(i);
			p.setID(i1);
			Map<String, Object> retrieve1Params = p.getRetrieve1Param(BaseBO.INVALID_CASE_ID, p);
			DataSourceContextHolder.setDbName(Shared.DBName_Test);
			Provider r1Po = (Provider) providerMapper.retrieve1(retrieve1Params);
			p.setIgnoreIDInComparision(true);
			Assert.assertTrue(r1Po != null && EnumErrorCode.values()[Integer.parseInt(retrieve1Params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "查询对象失败");
			Assert.assertTrue(r1Po.getName().contains(providerName));
		}
	}

	@Test
	public void testRetrieveNByFieldsEx4() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("case4：在限定状态下根据供应商名称模糊查询入库单");
		//
		MvcResult mr4 = mvc.perform( //
				post("/warehousing/retrieveNByFieldsEx.bx") //
						.param(Warehousing.field.getFIELD_NAME_queryKeyword(), "默认") //
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

		// 搜索出相应的供应商进行比对
		for (int i = 0; i < list3.size(); i++) {
			Provider p = new Provider();
			int i1 = list3.get(i);
			p.setID(i1);
			Map<String, Object> retrieve1Params = p.getRetrieve1Param(BaseBO.INVALID_CASE_ID, p);
			DataSourceContextHolder.setDbName(Shared.DBName_Test);
			Provider r1Po = (Provider) providerMapper.retrieve1(retrieve1Params);
			p.setIgnoreIDInComparision(true);
			Assert.assertTrue(r1Po != null && EnumErrorCode.values()[Integer.parseInt(retrieve1Params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "查询对象失败");
			Assert.assertTrue(r1Po.getName().contains("默认"));
		}

		// 对比状态
		for (int i = 0; i < listStatus.size(); i++) {
			System.out.println(listStatus.get(i));
			int i1 = listStatus.get(i);
			Assert.assertTrue(i1 == Warehousing.EnumStatusWarehousing.ESW_Approved.getIndex());
		}
	}

	@Test
	public void testRetrieveNByFieldsEx5() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("case5：在限定经办人ID下根据供应商名称模糊查询入库单");
		//
		MvcResult mr5 = mvc.perform( //
				post("/warehousing/retrieveNByFieldsEx.bx") //
						.param(Warehousing.field.getFIELD_NAME_queryKeyword(), "默认") //
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

		// 搜索出相应的供应商进行比对
		for (int i = 0; i < list4.size(); i++) {
			Provider p = new Provider();
			int i1 = list4.get(i);
			p.setID(i1);
			Map<String, Object> retrieve1Params = p.getRetrieve1Param(BaseBO.INVALID_CASE_ID, p);
			DataSourceContextHolder.setDbName(Shared.DBName_Test);
			Provider r1Po = (Provider) providerMapper.retrieve1(retrieve1Params);
			p.setIgnoreIDInComparision(true);
			Assert.assertTrue(r1Po != null && EnumErrorCode.values()[Integer.parseInt(retrieve1Params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "查询对象失败");
			Assert.assertTrue(r1Po.getName().contains("默认"));
		}

		// 对比经办人ID
		for (int i = 0; i < liststaffID.size(); i++) {
			System.out.println(liststaffID.get(i));
			int i1 = liststaffID.get(i);
			Assert.assertTrue(i1 == 3);
		}
	}

	@Test
	public void testRetrieveNByFieldsEx6() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("case6：在限定供应商ID下根据最小长度(10)的入库单单号模糊查询入库单");
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

		// 对比F_SN
		for (int i = 0; i < list5.size(); i++) {
			String SN = list5.get(i);
			Assert.assertTrue(SN.contains("RK20190605"), "该入库单单号：" + SN + "不是期望的");
		}

		// 对比供应商ID
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
			Assert.assertTrue(r1Po != null && EnumErrorCode.values()[Integer.parseInt(retrieve1Params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "查询对象失败");
			Assert.assertTrue(r1Po.getProviderID() == 2);
		}
	}

	@Test
	public void testRetrieveNByFieldsEx7() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("case7：在限定创建时间段下根据供应商名称查模糊询入库单");
		//
		SimpleDateFormat sdf3 = new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default3);
		SimpleDateFormat sdf1 = new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default1);
		String date1 = "2017/10/8 1:01:01";
		String date2 = "2020/01/01 00:00:00";

		MvcResult mr7 = mvc.perform( //
				post("/warehousing/retrieveNByFieldsEx.bx") //
						.param(Warehousing.field.getFIELD_NAME_queryKeyword(), "安徽") //
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

		String providerName = "安徽";
		// 搜索出相应的供应商进行比对
		for (int i = 0; i < list6.size(); i++) {
			Provider p = new Provider();
			int i1 = list6.get(i);
			p.setID(i1);
			Map<String, Object> retrieve1Params = p.getRetrieve1Param(BaseBO.INVALID_CASE_ID, p);
			DataSourceContextHolder.setDbName(Shared.DBName_Test);
			Provider r1Po = (Provider) providerMapper.retrieve1(retrieve1Params);
			p.setIgnoreIDInComparision(true);
			Assert.assertTrue(r1Po != null && EnumErrorCode.values()[Integer.parseInt(retrieve1Params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "查询对象失败");
			Assert.assertTrue(r1Po.getName().contains(providerName));
		}

		// 对比创建时间
		for (int i = 0; i < listCreateDatetime.size(); i++) {
			Date s1 = sdf1.parse(String.valueOf(listCreateDatetime.get(i)));
			Assert.assertTrue(s1.after(sdf3.parse(date1)) && s1.before(sdf3.parse(date2)));
		}
	}

	@Test
	public void testRetrieveNByFieldsEx8() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("case8：根据状态的进行搜索（不传入queryKeyword）");
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

		// 对比状态
		for (int i = 0; i < list7.size(); i++) {
			System.out.println(list7.get(i));
			int i1 = list7.get(i);
			Assert.assertTrue(i1 == Warehousing.EnumStatusWarehousing.ESW_Approved.getIndex());
		}
	}

	@Test
	public void testRetrieveNByFieldsEx9() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("case9：根据经办人ID的进行搜索（不传入queryKeyword)");
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
		// 对比经办人ID
		for (int i = 0; i < list8.size(); i++) {
			System.out.println(list8.get(i));
			int i1 = list8.get(i);
			Assert.assertTrue(i1 == 3);
		}
	}

	@Test
	public void testRetrieveNByFieldsEx10() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("case10：根据供应商ID的进行搜索（不传入queryKeyword）");
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
		// 对比供应商ID
		for (int i = 0; i < list9.size(); i++) {
			Provider p = new Provider();
			int i1 = list9.get(i);
			p.setID(i1);
			Map<String, Object> retrieve1Params = p.getRetrieve1Param(BaseBO.INVALID_CASE_ID, p);
			DataSourceContextHolder.setDbName(Shared.DBName_Test);
			Provider r1Po = (Provider) providerMapper.retrieve1(retrieve1Params);
			p.setIgnoreIDInComparision(true);
			Assert.assertTrue(r1Po != null && EnumErrorCode.values()[Integer.parseInt(retrieve1Params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "查询对象失败");
			Assert.assertTrue(r1Po.getID() == 10);
		}
	}

	@Test
	public void testRetrieveNByFieldsEx11() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("case11：根据传入创建时间段的进行搜索（不传入queryKeyword）");
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
		// 对比创建时间
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
		Shared.caseLog("case12：传值为空则查询全部");
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
		Shared.caseLog("case13：传值为空则查询全部，RNByFieldsEx不需要warehousing的从表warehousingCommodity和listCommodity,前端不需要这些数据，R1时才会查询这些信息");
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
		Assert.assertTrue(whCommList.size() > 0, "返回的warehousing没有从表字段");// size>0说明返回的warehousing有从表字段
		for (List<WarehousingCommodity> ls : whCommList) {
			Assert.assertTrue(ls.size() == 0, "warehousing返回了从表信息，RNByField时前端没有用到这些数据");
		}
		List<List<Commodity>> commList = JsonPath.read(o13, "$.warehousingList[*].listCommodity");
		for (List<Commodity> ls2 : commList) {
			Assert.assertTrue(ls2.size() == 0, "warehousing返回了listCommodity信息，RNByField时前端没有用到这些数据");
		}
	}

	@Test
	public void testRetrieveNByFieldsEx14() throws Exception {
		// 在warehousing/retrieveNByFieldsEx.bx中设置默认值，这里不传参代表默认值
		Shared.printTestMethodStartInfo();
		Shared.caseLog("case14：根据小于最小长度(10)的入库单单号模糊查询入库单");
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
		// 在warehousing/retrieveNByFieldsEx.bx中设置默认值，这里不传参代表默认值
		Shared.printTestMethodStartInfo();
		Shared.caseLog("case15：根据大于最大长度(20)的入库单单号模糊查询入库单");
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
		// 在warehousing/retrieveNByFieldsEx.bx中设置默认值，这里不传参代表默认值
		Shared.printTestMethodStartInfo();
		Shared.caseLog("case15：根据等于最大长度(20)的入库单单号模糊查询入库单");
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
		// 在warehousing/retrieveNByFieldsEx.bx中设置默认值，这里不传参代表默认值
		Shared.printTestMethodStartInfo();
		Shared.caseLog("case17：输入10位以下的采购订单SN的模糊查询入库单");
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
		// 在warehousing/retrieveNByFieldsEx.bx中设置默认值，这里不传参代表默认值
		Shared.printTestMethodStartInfo();
		Shared.caseLog("case18：输入10-20位的采购订单SN的模糊查询入库单");
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

		System.out.println("--------------- case19:传入queryKeyword包含_的特殊字符进行模糊搜索 ---------------");
		// 创建商品
		Commodity commodity = BaseCommodityTest.DataInput.getCommodity();
		commodity.setName("可口可乐1111_54（" + System.currentTimeMillis());
		String barcode = "987623" + System.currentTimeMillis();
		commodity.setMultiPackagingInfo(barcode + ";" + commodity.getPackageUnitID() + ";" + commodity.getRefCommodityMultiple() + ";" + commodity.getPriceRetail() + ";" + commodity.getPriceVIP() + ";" + commodity.getPriceWholesale() + ";"
				+ commodity.getName() + ";");
		Commodity commodity1 = BaseCommodityTest.createCommodityViaAction(commodity, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		Barcodes barcodes1 = retrieveNBarcodes(commodity1);
		// 创建入库单和入库单从表
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
		// 模糊查询入库订单
		MvcResult mr2 = mvc.perform( //
				post("/warehousing/retrieveNByFieldsEx.bx") //
						.param(Warehousing.field.getFIELD_NAME_queryKeyword(), "_54（") //
						.param(Warehousing.field.getFIELD_NAME_status(), String.valueOf(BaseAction.INVALID_STATUS)) //
						.contentType(MediaType.APPLICATION_JSON) //
						.session(sessionBoss) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();
		// 结果验证
		Shared.checkJSONErrorCode(mr2);

		JSONObject o = JSONObject.fromObject(mr2.getResponse().getContentAsString()); //

		List<Integer> list = JsonPath.read(o, "$.warehousingList[*].ID");//
		Assert.assertTrue(list.size() != 0);

		// 根据入库单的ID查，得到入库单商品的JsonObject
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
			Assert.assertTrue(warehousingCommodityList != null && warehousingCommodityList.size() != 0, "查询出来的数据不符合预想的！");
			// 把对应入库单商品解析出来
			for (int i = 0; i < warehousingCommodityList.size(); i++) {
				WarehousingCommodity warehousingCommodity = new WarehousingCommodity();
				warehousingCommodity.parse1(warehousingCommodityList.get(i).toString());
				// 判断入库单商品的名字是由包含字符"_"
				Assert.assertTrue(warehousingCommodity.getCommodityName().contains("_"), "查询商品名称没有包含_字符！");
			}
		}

	}
	
	@Test
	public void testRetrieveNByFieldsEx20() throws Exception {
		System.out.println("--------------- case20:根据门店ID模糊查询入库单 ---------------");
		
		Shared.printTestMethodStartInfo();
		Warehousing warehousing = BaseWarehousingTest.DataInput.getWarehousing();
		Warehousing ws = createWarehousing(warehousing);
		Shared.caseLog("case1：最小长度(10)的入库单单号模糊查询入库单");
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
		// 创建入库商品
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
		Shared.caseLog("case1：根据ID删除入库单");
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
		Shared.caseLog("case2：入库单已审核，无法删除");
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
		Shared.caseLog("case3：入库单不存在，无法删除");
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
		Shared.caseLog("case4：没有权限，无法删除");
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
	// System.out.println("------------------------------Case1:删除1个入库单------------------------");
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
	// System.out.println("-----------------------------------Case2:删除多个入库单------------------------------");
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
	// System.out.println("-----------------------------------Case3:删除一个不可删除的入库单------------------------------");
	// String msg1 = "入库单" + 4 + "删除失败，因为它已经审核过。<br />";
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
	// Shared.checkJSONMsg(mr3, msg1, "返回的结果不一致");
	//
	// System.out.println("-----------------------------------Case4:删除多个不可删除的入库单------------------------------");
	// String msg2 = "入库单" + 11 + "删除失败，因为它已经审核过。<br />";
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
	// Shared.checkJSONMsg(mr4, msg1 + msg2, "返回的结果不一致");
	//
	// System.out.println("------------------------------Case5:删除0个入库单-----------------------");
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
	// System.out.println("-----------------------case6：POListID的ID在入库单中不存在。---------------");
	// String msg = "入库单" + BaseWarehousingTest.INVALID_ID + "删除失败，因为它已经审核过。<br />";
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
	// Shared.checkJSONMsg(mr6, msg, "返回的结果不一致");
	//
	// System.out.println("-----------------------------------Case7:删除多个入库单，其中一个不可删除------------------------------");
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
	// Shared.checkJSONMsg(mr7, msg1, "返回的结果不一致");
	// Shared.checkJSONErrorCode(mr7, EnumErrorCode.EC_PartSuccess);
	//
	// System.out.println("-----------------------------------Case8:没有权限进行删除------------------------------");
	// Warehousing wc5 = createWS();
	// String msg3 = "你没有权限删除入库单。<br />";
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
	// Shared.checkJSONMsg(mr8, msg3, "返回的结果不一致");
	// Shared.checkJSONErrorCode(mr8, EnumErrorCode.EC_PartSuccess);
	// }

	@Test
	public void testDeleteEx5() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("case5：传入的参数,不符合要求！");
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
		Shared.caseLog("case6：售前人员删除未审核的入库单,没有权限，无法删除");
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
		Shared.caseLog("case7：售前人员删除已审核的入库单,没有权限，无法删除");
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
	// System.out.println("------------------------------Case1:删除1个入库单------------------------");
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
	// System.out.println("-----------------------------------Case2:删除多个入库单------------------------------");
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
	// System.out.println("-----------------------------------Case3:删除一个不可删除的入库单------------------------------");
	// String msg1 = "入库单" + 4 + "删除失败，因为它已经审核过。<br />";
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
	// Shared.checkJSONMsg(mr3, msg1, "返回的结果不一致");
	//
	// System.out.println("-----------------------------------Case4:删除多个不可删除的入库单------------------------------");
	// String msg2 = "入库单" + 11 + "删除失败，因为它已经审核过。<br />";
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
	// Shared.checkJSONMsg(mr4, msg1 + msg2, "返回的结果不一致");
	//
	// System.out.println("------------------------------Case5:删除0个入库单-----------------------");
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
	// System.out.println("-----------------------case6：POListID的ID在入库单中不存在。---------------");
	// String msg = "入库单" + BaseWarehousingTest.INVALID_ID + "删除失败，因为它已经审核过。<br />";
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
	// Shared.checkJSONMsg(mr6, msg, "返回的结果不一致");
	//
	// System.out.println("-----------------------------------Case7:删除多个入库单，其中一个不可删除------------------------------");
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
	// Shared.checkJSONMsg(mr7, msg1, "返回的结果不一致");
	// Shared.checkJSONErrorCode(mr7, EnumErrorCode.EC_PartSuccess);
	//
	// System.out.println("-----------------------------------Case8:没有权限进行删除------------------------------");
	// Warehousing wc5 = createWS();
	// String msg3 = "你没有权限删除入库单。<br />";
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
	// Shared.checkJSONMsg(mr8, msg3, "返回的结果不一致");
	// Shared.checkJSONErrorCode(mr8, EnumErrorCode.EC_PartSuccess);
	// }

	@Test
	public void testUpdateEx1() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case1:修改未审核入库单");
		HttpSession session = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);

		Provider provider = createProvider();
		Warehousing warehousing = BaseWarehousingTest.DataInput.getWarehousing();
		Warehousing ws = createWarehousing(warehousing);
		Warehousing wsUpdate = new Warehousing();
		// 创建入库商品
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
		Shared.caseLog("Case2:修改已审核入库单");

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

		Shared.caseLog("Case3:修改仓库不存在的入库单");
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

		Shared.caseLog("Case4:修改供应商不存在的入库单");
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

		Shared.caseLog("Case5:更新一个WarehousingID不存在的WarehousingCommodity，返回错误码7");
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

		Shared.caseLog("Case6:更新一个CommodityID不存在的WarehousingCommodity");
		HttpSession session = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);

		Warehousing warehousing = BaseWarehousingTest.DataInput.getWarehousing();
		Warehousing ws = createWarehousing(warehousing);
		// 创建入库商品
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

		Shared.caseLog("Case7:更新一个WarehousingCommodity的本次收货数量为0，字段验证不通过");
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

		Shared.caseLog("Case8:传入参数维度不一致");
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

		Assert.assertTrue("".equals(mr.getResponse().getContentAsString()), "action返回的错误码不正确");
	}

	@Test
	public void testUpdateEx9() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case9:传入参数缺失");
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

		Assert.assertTrue("".equals(mr.getResponse().getContentAsString()), "action返回的错误码不正确");
	}

	@Test
	public void testUpdateEx10() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case10:传入商品id重复");
		HttpSession session = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);

		Warehousing warehousing = BaseWarehousingTest.DataInput.getWarehousing();
		Warehousing ws = createWarehousing(warehousing);
		// 创建入库商品
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
		Assert.assertTrue("".equals(mr.getResponse().getContentAsString()), "action返回的错误信息不正确");
	}

	@Test
	public void testUpdateEx11() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case11:没有权限");
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

		Shared.caseLog("Case12:修改没有入库商品的入库单");
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
		Assert.assertTrue(json.length() == 0, "CASE12测试失败！期望的是返回null");
	}

	@Test
	public void testUpdateEx13() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case13:修改不存在的入库单");
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

		Shared.caseLog("Case14:修改成入库多包装商品");
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

		Shared.caseLog("Case15:门店A的店长创建入库，门店B的店长进行修改，修改失败，不能跨店修改");
		
		Provider provider = createProvider();
		Warehousing warehousing = BaseWarehousingTest.DataInput.getWarehousing();
		Warehousing ws = createWarehousing(warehousing);
		Warehousing wsUpdate = new Warehousing();
		// 创建入库商品
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
		// 创建门店B的店长账号
		Shop shopGet = BaseShopTest.DataInput.getShop();
		Shop shopCreated = BaseShopTest.createViaCompanyAction(shopGet, mvc, mapBO, sessionOP, EnumErrorCode.EC_NoError);
		// 为该门店新增店长
		String Staff_Phone = Shared.getValidStaffPhone();
		MvcResult ret = BaseStaffTest.getToken(sessionBoss, mvc, Staff_Phone, Staff.FOR_CreateNewStaff);
		//
		String encrypt = Shared.encrypt(ret, Shared.PASSWORD_DEFAULT);
		Staff staff = BaseStaffTest.DataInput.getStaff();
		staff.setShopID(shopCreated.getID());
		staff.setName("店长" + Shared.generateCompanyName(6));
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
		// 结果验证：检查错误码
		Shared.checkJSONErrorCode(mrStaffCreate);
		Staff staffCopy = (Staff) staff.clone(); // 由于结果验证会重新对pos赋值，影响下面的其它测试用例，所以先保持一个copy，后面再恢复用于其它测试用例
		StaffCP.verifyCreate(mrStaffCreate, staffCopy, staffRoleBO, Shared.DBName_Test);
		staff = (Staff) staffCopy.clone(); // 恢复原样
		//
		JSONObject object = JSONObject.fromObject(mrStaffCreate.getResponse().getContentAsString());
		Staff staff2 = new Staff();
		staff2 = (Staff) staff2.parse1(object.getString(BaseAction.KEY_Object));
		Assert.assertTrue(staff2 != null, "解析异常");
		// 登录
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

		Shared.caseLog("Case16:门店A的店长创建入库，门店B(虚拟总部)的店长进行修改，修改成功");
		
		Provider provider = createProvider();
		Warehousing warehousing = BaseWarehousingTest.DataInput.getWarehousing();
		Warehousing ws = createWarehousing(warehousing);
		Warehousing wsUpdate = new Warehousing();
		// 创建入库商品
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
		// 为该门店新增店长
		String Staff_Phone = Shared.getValidStaffPhone();
		MvcResult ret = BaseStaffTest.getToken(sessionBoss, mvc, Staff_Phone, Staff.FOR_CreateNewStaff);
		//
		String encrypt = Shared.encrypt(ret, Shared.PASSWORD_DEFAULT);
		Staff staff = BaseStaffTest.DataInput.getStaff();
		staff.setShopID(BaseAction.VirtualShopID);
		staff.setName("店长" + Shared.generateCompanyName(6));
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
		// 结果验证：检查错误码
		Shared.checkJSONErrorCode(mrStaffCreate);
		Staff staffCopy = (Staff) staff.clone(); // 由于结果验证会重新对pos赋值，影响下面的其它测试用例，所以先保持一个copy，后面再恢复用于其它测试用例
		StaffCP.verifyCreate(mrStaffCreate, staffCopy, staffRoleBO, Shared.DBName_Test);
		staff = (Staff) staffCopy.clone(); // 恢复原样
		//
		JSONObject object = JSONObject.fromObject(mrStaffCreate.getResponse().getContentAsString());
		Staff staff2 = new Staff();
		staff2 = (Staff) staff2.parse1(object.getString(BaseAction.KEY_Object));
		Assert.assertTrue(staff2 != null, "解析异常");
		// 登录
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

	// 发送入库价格与采购订单上的价格不符消息
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

		// 先创建采购单，进行审核，然后再创建入库单
		PurchasingOrder purchasingOrder2 = createPurchasingOrder();//

		Warehousing bm1 = BaseWarehousingTest.DataInput.getWarehousing(); //
		bm1.setPurchasingOrderID(purchasingOrder2.getID());
		Map<String, Object> params1 = bm1.getCreateParam(BaseBO.INVALID_CASE_ID, bm1);
		Warehousing bmCreate = (Warehousing) warehousingMapper.create(params1);
		bmCreate.setIgnoreIDInComparision(true);

		assertTrue(EnumErrorCode.values()[Integer.parseInt(params1.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "创建对象失败");

		WarehousingCommodity wsc = DataInput.getWarehousingCommodity();
		wsc.setPrice(20);
		wsc.setWarehousingID(bmCreate.getID());
		Map<String, Object> params2 = wsc.getCreateParam(BaseBO.INVALID_CASE_ID, wsc);
		warehousingCommodityMapper.create(params2);

		assertTrue(EnumErrorCode.values()[Integer.parseInt(params2.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "创建对象失败");

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
		// 创建一个供应商
		Provider p = BaseTestNGSpringContextTest.DataInput.getProvider();
		String error = p.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertTrue(error == "", "供应商的checkCreate不通过");
		Map<String, Object> params = p.getCreateParam(BaseBO.INVALID_CASE_ID, p);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Provider providerCreate = (Provider) providerMapper.create(params); // ...
		p.setIgnoreIDInComparision(true);
		if (p.compareTo(providerCreate) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		Assert.assertTrue(providerCreate != null && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "创建对象成功");

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
			b.setOperatorStaffID(1); // ...TODO 为了能通过下面的断言
			//
			String error = b.checkCreate(BaseBO.INVALID_CASE_ID);
			Assert.assertEquals(error, "");
			b.setOperatorStaffID(0); // 恢复值
		}
		Barcodes bc = (Barcodes) retrieveN.get(0);
		return bc;
	}
}
