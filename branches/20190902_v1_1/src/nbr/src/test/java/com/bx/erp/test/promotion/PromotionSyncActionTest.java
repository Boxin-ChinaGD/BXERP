package com.bx.erp.test.promotion;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.testng.Assert.assertTrue;

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
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.action.AuthenticationAction;
import com.bx.erp.action.BaseAction;
import com.bx.erp.action.BaseAction.EnumSession;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.action.trade.promotion.BasePromotion.EnumPromotionScope;
import com.bx.erp.action.trade.promotion.BasePromotion.EnumPromotionShopScope;
import com.bx.erp.cache.CacheManager;
import com.bx.erp.cache.SyncCacheManager;
import com.bx.erp.cache.config.ConfigCacheSizeCache.EnumConfigCacheSizeCache;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.CommodityType;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.model.Pos;
import com.bx.erp.model.Shop;
import com.bx.erp.model.BaseModel.EnumBoolean;
import com.bx.erp.model.CacheType.EnumCacheType;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.SyncCacheType.EnumSyncCacheType;
import com.bx.erp.model.commodity.Commodity;
import com.bx.erp.model.config.ConfigCacheSize;
import com.bx.erp.model.trade.Promotion;
import com.bx.erp.model.trade.Promotion.EnumStatusPromotion;
import com.bx.erp.model.trade.Promotion.EnumTypePromotion;
import com.bx.erp.model.trade.PromotionScope;
import com.bx.erp.model.trade.PromotionShopScope;
import com.bx.erp.model.trade.RetailTradePromoting;
import com.bx.erp.model.trade.RetailTradePromotingFlow;
import com.bx.erp.test.BaseActionTest;
import com.bx.erp.test.BaseCommodityTest;
import com.bx.erp.test.BasePromotionTest;
import com.bx.erp.test.Shared;
import com.bx.erp.test.checkPoint.PromotionCP;
import com.bx.erp.test.staff.BaseShopTest;
import com.bx.erp.util.DataSourceContextHolder;
import com.bx.erp.util.DatetimeUtil;
import com.jayway.jsonpath.JsonPath;

import net.sf.json.JSONObject;

@WebAppConfiguration
public class PromotionSyncActionTest extends BaseActionTest {
	private HttpSession session;
	private HttpSession session1;
	EnumSyncCacheType esct = EnumSyncCacheType.ESCT_PromotionSyncInfo;

	public static final int RETURN_OBJECT = 1;

	@BeforeClass
	public void setup() {
		super.setUp();

		AuthenticationAction.resetLoginCount();
		
		try {
			Shared.resetPOS(mvc, 2);
			session = Shared.getPosLoginSession(mvc, 2);
			session1 = Shared.getPosLoginSession(mvc, 1);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// ?????????????????????????????????????????????????????????????????????????????????????????????????????????tear down???????????????
		ConfigCacheSize ccs = new ConfigCacheSize();
		ccs.setValue(String.valueOf(BaseAction.PAGE_SIZE_Infinite));
		ccs.setID(EnumConfigCacheSizeCache.ECC_CommodityCacheSize.getIndex());
		ccs.setName(EnumConfigCacheSizeCache.ECC_CommodityCacheSize.getName());
		CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_ConfigCacheSize).write1(ccs, Shared.DBName_Test, STAFF_ID4);
	}

	@AfterClass
	public void tearDown() {

		super.tearDown();

		// ????????????????????????????????????????????????setup?????????????????????????????????????????????????????????????????????
		ConfigCacheSize ccs = new ConfigCacheSize();
		ccs.setValue("100");
		ccs.setID(EnumConfigCacheSizeCache.ECC_CommodityCacheSize.getIndex());
		ccs.setName(EnumConfigCacheSizeCache.ECC_CommodityCacheSize.getName());
		CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_ConfigCacheSize).write1(ccs, Shared.DBName_Test, STAFF_ID4);
	}

	@Test
	public void testCreateEx1() throws Exception {
		Shared.printTestMethodStartInfo();

		Promotion p = new Promotion();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
		String l = simpleDateFormat.format(DatetimeUtil.getDays(new Date(), 3));

		MvcResult mr = mvc.perform(//
				post("/promotionSync/createEx.bx") //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) session) //
						.param(Promotion.field.getFIELD_NAME_name(), "?????????????????????10???1")//
						.param(Promotion.field.getFIELD_NAME_status(), String.valueOf(EnumStatusPromotion.ESP_Active.getIndex()))//
						.param(Promotion.field.getFIELD_NAME_type(), String.valueOf(EnumTypePromotion.ETP_DecreaseOnAmount.getIndex()))//
						.param(Promotion.field.getFIELD_NAME_datetimeStart(), l)//
						.param(Promotion.field.getFIELD_NAME_datetimeEnd(), "3000/10/22")//
						.param(Promotion.field.getFIELD_NAME_excecutionThreshold(), "10")//
						.param(Promotion.field.getFIELD_NAME_excecutionAmount(), "1")//
						.param(Promotion.field.getFIELD_NAME_excecutionDiscount(), "-1")//
						.param(Promotion.field.getFIELD_NAME_scope(), String.valueOf(EnumPromotionScope.EPS_SpecifiedCommodities.getIndex()))//
						.param(Promotion.field.getFIELD_NAME_staff(), "1")//
						.param(Promotion.field.getFIELD_NAME_returnObject(), String.valueOf(1))// iReturnObject
						.param(Promotion.field.getFIELD_NAME_commodityIDs(), "1,2")//
		).andExpect(status().isOk()).andDo(print()).andReturn();
		// ??????????????????????????????
		Shared.checkJSONErrorCode(mr);
		// ????????????????????????
		Pos pos = (Pos) session.getAttribute(EnumSession.SESSION_POS.getName());
		int posID = pos.getID();
		verifySyncCreateResult(mr, p, posBO, promotionSyncCacheBO, promotionSyncCacheDispatcherBO, EnumCacheType.ECT_Promotion, esct, posID, Shared.DBName_Test);// posID
		// >
		// 0??????pos??????????????????
		// ?????????
		Promotion pt = new Promotion();
		pt.setName("?????????????????????10???1");
		pt.setStatus(EnumStatusPromotion.ESP_Active.getIndex());
		pt.setType(EnumTypePromotion.ETP_DecreaseOnAmount.getIndex());
		Date dtStart = simpleDateFormat.parse(l);
		pt.setDatetimeStart(dtStart);
		Date dtEnd = simpleDateFormat.parse("3000/10/22");
		pt.setDatetimeEnd(dtEnd);
		pt.setExcecutionThreshold(10);
		pt.setExcecutionAmount(1);
		pt.setExcecutionDiscount(-1);
		pt.setScope(EnumPromotionScope.EPS_SpecifiedCommodities.getIndex());
		pt.setStaff(1);
		//
		PromotionCP.verifyCreate(mr, pt, Shared.DBName_Test, posBO, EnumSyncCacheType.ESCT_PromotionSyncInfo, promotionSyncCacheBO, promotionSyncCacheDispatcherBO, posID, promotionScopeMapper);
	}

	@Test
	public void testCreateEx2() throws Exception {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
		String l = simpleDateFormat.format(DatetimeUtil.getDays(new Date(), 3));
		Shared.caseLog("case2:??????posID???????????????Action??????");
		try {
			mvc.perform(//
					post("/promotionSync/createEx.bx") //
							.contentType(MediaType.APPLICATION_JSON) //
							.param(Promotion.field.getFIELD_NAME_name(), "?????????????????????10???12")//
							.param(Promotion.field.getFIELD_NAME_status(), String.valueOf(EnumStatusPromotion.ESP_Active.getIndex()))//
							.param(Promotion.field.getFIELD_NAME_type(), String.valueOf(EnumTypePromotion.ETP_DecreaseOnAmount.getIndex()))//
							.param(Promotion.field.getFIELD_NAME_datetimeStart(), l)//
							.param(Promotion.field.getFIELD_NAME_datetimeEnd(), "3000/10/22")//
							.param(Promotion.field.getFIELD_NAME_excecutionThreshold(), "10")//
							.param(Promotion.field.getFIELD_NAME_excecutionAmount(), "1")//
							.param(Promotion.field.getFIELD_NAME_excecutionDiscount(), "0")//
							.param(Promotion.field.getFIELD_NAME_scope(), String.valueOf(EnumPromotionScope.EPS_SpecifiedCommodities.getIndex()))//
							.param(Promotion.field.getFIELD_NAME_staff(), "1")//
							.param(Promotion.field.getFIELD_NAME_commodityIDs(), "1,2")//
			).andExpect(status().isOk()).andDo(print()).andReturn();
		} catch (Exception e) {
			System.out.println(e.getMessage());// ??????posID???????????????
			// ???????????????????????????SyncAction???req
			// get???session???????????????dbName?????????dbName????????????BaseAction??????????????????????????????????????????????????????????????????????????????????????????
			// assertTrue(e.getMessage().equals("Request processing failed; nested exception
			// is java.lang.RuntimeException: ?????????????????????"));
		}
	}

	@Test
	public void testCreateEx3() throws Exception {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
		String l = simpleDateFormat.format(DatetimeUtil.getDays(new Date(), 3));

		Shared.caseLog("case3???????????????????????????????????????????????????????????????????????????????????????????????????");
		// ????????????????????????
		Commodity commodity = BaseCommodityTest.DataInput.getCommodity(CommodityType.EnumCommodityType.ECT_Combination.getIndex());
		String subCommodityInfo = JSONObject.fromObject(commodity).toString();
		commodity.setSubCommodityInfo(subCommodityInfo);
		Commodity commodityCreate31 = BaseCommodityTest.createCommodityViaAction(commodity, mvc, session, mapBO, Shared.DBName_Test);
		//
		Commodity commodity2 = BaseCommodityTest.DataInput.getCommodity(CommodityType.EnumCommodityType.ECT_Combination.getIndex());
		String subCommodityInfo2 = JSONObject.fromObject(commodity).toString();
		commodity2.setSubCommodityInfo(subCommodityInfo2);
		Commodity commodityCreate32 = BaseCommodityTest.createCommodityViaAction(commodity2, mvc, session, mapBO, Shared.DBName_Test);

		String commodityIDList = commodityCreate31.getID() + "," + commodityCreate32.getID();
		MvcResult mr3 = mvc.perform(//
				post("/promotionSync/createEx.bx") //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) session) //
						.param(Promotion.field.getFIELD_NAME_name(), "?????????????????????10???1")//
						.param(Promotion.field.getFIELD_NAME_status(), String.valueOf(EnumStatusPromotion.ESP_Active.getIndex()))//
						.param(Promotion.field.getFIELD_NAME_type(), String.valueOf(EnumTypePromotion.ETP_DecreaseOnAmount.getIndex()))//
						.param(Promotion.field.getFIELD_NAME_datetimeStart(), l)//
						.param(Promotion.field.getFIELD_NAME_datetimeEnd(), "3000/10/22")//
						.param(Promotion.field.getFIELD_NAME_excecutionThreshold(), "10")//
						.param(Promotion.field.getFIELD_NAME_excecutionAmount(), "1")//
						.param(Promotion.field.getFIELD_NAME_excecutionDiscount(), "0")//
						.param(Promotion.field.getFIELD_NAME_scope(), String.valueOf(EnumPromotionScope.EPS_SpecifiedCommodities.getIndex()))//
						.param(Promotion.field.getFIELD_NAME_staff(), "1")//
						.param(Promotion.field.getFIELD_NAME_returnObject(), String.valueOf(1))// iReturnObject
						.param(Promotion.field.getFIELD_NAME_commodityIDs(), commodityIDList)//
		).andExpect(status().isOk()).andDo(print()).andReturn();

		// Shared.checkJSONErrorCode(mr3, EnumErrorCode.EC_BusinessLogicNotDefined);
		String o3 = mr3.getResponse().getContentAsString();
		assertTrue(o3.length() == 0, "CASE3?????????????????????????????????????????????");
	}

	@Test
	public void testCreateEx4() throws Exception {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
		String l = simpleDateFormat.format(DatetimeUtil.getDays(new Date(), 3));

		Shared.caseLog("case4????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????");
		// 1???????????????????????????
		Commodity commodity = BaseCommodityTest.DataInput.getCommodity(CommodityType.EnumCommodityType.ECT_Normal.getIndex());
		Commodity commodityCreate41 = BaseCommodityTest.createCommodityViaAction(commodity, mvc, session, mapBO, Shared.DBName_Test);

		// 2???????????????????????????
		Commodity commodity2 = BaseCommodityTest.DataInput.getCommodity(CommodityType.EnumCommodityType.ECT_Combination.getIndex());
		String subCommodityInfo2 = JSONObject.fromObject(commodity2).toString();
		commodity2.setSubCommodityInfo(subCommodityInfo2);
		Commodity commodityCreate42 = BaseCommodityTest.createCommodityViaAction(commodity2, mvc, session, mapBO, Shared.DBName_Test);

		String string4 = commodityCreate41.getID() + "," + commodityCreate42.getID();

		MvcResult mr4 = mvc.perform(//
				post("/promotionSync/createEx.bx") //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) session) //
						.param(Promotion.field.getFIELD_NAME_name(), "?????????????????????10???1")//
						.param(Promotion.field.getFIELD_NAME_status(), String.valueOf(EnumStatusPromotion.ESP_Active.getIndex()))//
						.param(Promotion.field.getFIELD_NAME_type(), String.valueOf(EnumTypePromotion.ETP_DecreaseOnAmount.getIndex()))//
						.param(Promotion.field.getFIELD_NAME_datetimeStart(), l)//
						.param(Promotion.field.getFIELD_NAME_datetimeEnd(), "3000/10/22")//
						.param(Promotion.field.getFIELD_NAME_excecutionThreshold(), "10")//
						.param(Promotion.field.getFIELD_NAME_excecutionAmount(), "1")//
						.param(Promotion.field.getFIELD_NAME_excecutionDiscount(), "0")//
						.param(Promotion.field.getFIELD_NAME_scope(), String.valueOf(EnumPromotionScope.EPS_SpecifiedCommodities.getIndex()))//
						.param(Promotion.field.getFIELD_NAME_staff(), "1")//
						.param(Promotion.field.getFIELD_NAME_returnObject(), String.valueOf(1))//
						.param(Promotion.field.getFIELD_NAME_commodityIDs(), string4)//
		).andExpect(status().isOk()).andDo(print()).andReturn();

		// Shared.checkJSONErrorCode(mr4, EnumErrorCode.EC_BusinessLogicNotDefined);
		String o4 = mr4.getResponse().getContentAsString();
		assertTrue(o4.length() == 0, "CASE4?????????????????????????????????????????????");
	}

	@Test
	public void testCreateEx5() throws Exception {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
		String l = simpleDateFormat.format(DatetimeUtil.getDays(new Date(), 3));

		Shared.caseLog("case5??????????????????????????????????????????????????????????????????????????????????????????????????????????????????");
		// ????????????????????????
		Commodity commodity = BaseCommodityTest.DataInput.getCommodity(CommodityType.EnumCommodityType.ECT_Normal.getIndex());
		Commodity commodityCreate51 = BaseCommodityTest.createCommodityViaAction(commodity, mvc, session, mapBO, Shared.DBName_Test);
		//
		Commodity commodity2 = BaseCommodityTest.DataInput.getCommodity(CommodityType.EnumCommodityType.ECT_Normal.getIndex());
		Commodity commodityCreate52 = BaseCommodityTest.createCommodityViaAction(commodity2, mvc, session, mapBO, Shared.DBName_Test);

		String string5 = commodityCreate51.getID() + "," + commodityCreate52.getID();

		Promotion p5 = BasePromotionTest.DataInput.getPromotion();
		p5.setReturnObject(RETURN_OBJECT);
		p5.setCommodityIDs(string5);
		p5.setDatetimeStart(simpleDateFormat.parse(l));
		p5.setDatetimeEnd(simpleDateFormat.parse("3000/10/22"));
		p5.setScope(EnumPromotionScope.EPS_SpecifiedCommodities.getIndex());
		PromotionScope promotionScopecope = new PromotionScope();
		promotionScopecope.setCommodityID(commodityCreate51.getID());
		promotionScopecope.setCommodityName(commodityCreate51.getName());
		promotionScopecope.setPromotionID(p5.getID());
		//
		PromotionScope promotionScopecope1 = new PromotionScope();
		promotionScopecope1.setCommodityID(commodityCreate52.getID());
		promotionScopecope1.setCommodityName(commodityCreate52.getName());
		promotionScopecope1.setPromotionID(p5.getID());
		//
		List<PromotionScope> listPromotionScope = new ArrayList<PromotionScope>();
		listPromotionScope.add(promotionScopecope);
		listPromotionScope.add(promotionScopecope1);
		//
		p5.setListSlave1(listPromotionScope);
		MvcResult mr5 = mvc.perform(//
				post("/promotionSync/createEx.bx") //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) session) //
						.param(Promotion.field.getFIELD_NAME_name(), p5.getName())//
						.param(Promotion.field.getFIELD_NAME_status(), String.valueOf(p5.getStatus()))//
						.param(Promotion.field.getFIELD_NAME_type(), String.valueOf(p5.getType()))//
						.param(Promotion.field.getFIELD_NAME_datetimeStart(), simpleDateFormat.format(p5.getDatetimeStart()))//
						.param(Promotion.field.getFIELD_NAME_datetimeEnd(), simpleDateFormat.format(p5.getDatetimeEnd()))//
						.param(Promotion.field.getFIELD_NAME_excecutionThreshold(), String.valueOf(p5.getExcecutionThreshold()))//
						.param(Promotion.field.getFIELD_NAME_excecutionAmount(), String.valueOf(p5.getExcecutionAmount()))//
						.param(Promotion.field.getFIELD_NAME_excecutionDiscount(), String.valueOf(p5.getExcecutionDiscount()))//
						.param(Promotion.field.getFIELD_NAME_scope(), String.valueOf(p5.getScope()))//
						.param(Promotion.field.getFIELD_NAME_staff(), String.valueOf(p5.getStaff()))//
						.param(Promotion.field.getFIELD_NAME_returnObject(), String.valueOf(p5.getReturnObject()))//
						.param(Promotion.field.getFIELD_NAME_commodityIDs(), p5.getCommodityIDs())//
		).andExpect(status().isOk()).andDo(print()).andReturn();

		Shared.checkJSONErrorCode(mr5);

		JSONObject o5 = JSONObject.fromObject(mr5.getResponse().getContentAsString());
		Promotion jsonToPromotion = (Promotion) new Promotion().parse1(String.valueOf(o5.getJSONObject(BaseAction.KEY_Object)));
		List<?> list = jsonToPromotion.getListSlave1(); // java?????????????????????????????????DB????????????????????????????????????
		jsonToPromotion.setListSlave1(p5.getListSlave1());
		p5.setIgnoreIDInComparision(true);
		if (p5.compareTo(jsonToPromotion) != 0) {
			Assert.assertTrue(false, "??????????????????DB????????????????????????");
		}
		jsonToPromotion.setListSlave1(list); // ????????????????????????
		// ?????????????????????
		PromotionScope ps = new PromotionScope();
		ps.setPromotionID(jsonToPromotion.getID());
		Map<String, Object> psParamsRN = ps.getRetrieveNParam(BaseBO.INVALID_CASE_ID, ps);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> psRN = promotionScopeMapper.retrieveN(psParamsRN);
		//
		for (BaseModel bm : psRN) {
			Assert.assertTrue(((PromotionScope) bm).getCommodityName().equals(commodityCreate51.getName()) //
					|| ((PromotionScope) bm).getCommodityName().equals(commodityCreate52.getName()), "????????????????????????F_CommodityName???????????????");
		}
		// ?????????
		Pos pos = (Pos) session.getAttribute(EnumSession.SESSION_POS.getName());
		int posID = pos.getID();
		PromotionCP.verifyCreate(mr5, p5, Shared.DBName_Test, posBO, EnumSyncCacheType.ESCT_PromotionSyncInfo, promotionSyncCacheBO, promotionSyncCacheDispatcherBO, posID, promotionScopeMapper);
	}

	@Test
	public void testCreateEx6() throws Exception {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
		String l = simpleDateFormat.format(DatetimeUtil.getDays(new Date(), 3));

		Shared.caseLog("case6???????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????");
		// 1???????????????????????????
		Commodity commodity = BaseCommodityTest.DataInput.getCommodity(CommodityType.EnumCommodityType.ECT_Normal.getIndex());
		Commodity commodityCreate61 = BaseCommodityTest.createCommodityViaAction(commodity, mvc, session, mapBO, Shared.DBName_Test);

		// 2??????????????????????????????
		Commodity commodity2 = BaseCommodityTest.DataInput.getCommodity(CommodityType.EnumCommodityType.ECT_MultiPackaging.getIndex());
		commodity2.setDefaultValueToCreate(BaseBO.CASE_Commodity_CreateMultiPackaging);
		commodity2.setRefCommodityID(commodityCreate61.getID());
		commodity2.setRefCommodityMultiple(2);
		commodity2.setMultiPackagingInfo("123456789" + ";1;1;1;8;8;" + commodity2.getBarcodes() + ";");
		Commodity commodityCreate62 = BaseCommodityTest.createCommodityViaMapper(commodity2, BaseBO.CASE_Commodity_CreateMultiPackaging);

		String string6 = commodityCreate61.getID() + "," + commodityCreate62.getID();
		MvcResult mr6 = mvc.perform(//
				post("/promotionSync/createEx.bx") //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) session) //
						.param(Promotion.field.getFIELD_NAME_name(), "?????????????????????10???1")//
						.param(Promotion.field.getFIELD_NAME_status(), String.valueOf(EnumStatusPromotion.ESP_Active.getIndex()))//
						.param(Promotion.field.getFIELD_NAME_type(), String.valueOf(EnumTypePromotion.ETP_DecreaseOnAmount.getIndex()))//
						.param(Promotion.field.getFIELD_NAME_datetimeStart(), l)//
						.param(Promotion.field.getFIELD_NAME_datetimeEnd(), "3000/10/22")//
						.param(Promotion.field.getFIELD_NAME_excecutionThreshold(), "10")//
						.param(Promotion.field.getFIELD_NAME_excecutionAmount(), "1")//
						.param(Promotion.field.getFIELD_NAME_excecutionDiscount(), "0")//
						.param(Promotion.field.getFIELD_NAME_scope(), String.valueOf(EnumPromotionScope.EPS_SpecifiedCommodities.getIndex()))//
						.param(Promotion.field.getFIELD_NAME_staff(), "1")//
						.param(Promotion.field.getFIELD_NAME_returnObject(), String.valueOf(1))//
						.param(Promotion.field.getFIELD_NAME_commodityIDs(), string6)//
		).andExpect(status().isOk()).andDo(print()).andReturn();

		// Shared.checkJSONErrorCode(mr6, EnumErrorCode.EC_BusinessLogicNotDefined);
		String o6 = mr6.getResponse().getContentAsString();
		assertTrue(o6.length() == 0, "CASE6?????????????????????????????????????????????");
	}

	@Test
	public void testCreateEx7() throws Exception {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
		String l = simpleDateFormat.format(DatetimeUtil.getDays(new Date(), 3));

		Shared.caseLog("case7??????????????????????????????????????????????????????????????????????????????????????????");
		// 1???????????????????????????
		Commodity commodity = BaseCommodityTest.DataInput.getCommodity(CommodityType.EnumCommodityType.ECT_Normal.getIndex());
		Commodity commodityCreate7 = BaseCommodityTest.createCommodityViaAction(commodity, mvc, session, mapBO, Shared.DBName_Test);

		MvcResult mr7 = mvc.perform(//
				post("/promotionSync/createEx.bx") //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) session) //
						.param(Promotion.field.getFIELD_NAME_name(), "?????????????????????10???1")//
						.param(Promotion.field.getFIELD_NAME_status(), String.valueOf(EnumStatusPromotion.ESP_Active.getIndex()))//
						.param(Promotion.field.getFIELD_NAME_type(), String.valueOf(EnumTypePromotion.ETP_DecreaseOnAmount.getIndex()))//
						.param(Promotion.field.getFIELD_NAME_datetimeStart(), l)//
						.param(Promotion.field.getFIELD_NAME_datetimeEnd(), "3000/10/22")//
						.param(Promotion.field.getFIELD_NAME_excecutionThreshold(), "10")//
						.param(Promotion.field.getFIELD_NAME_excecutionAmount(), "10")//
						.param(Promotion.field.getFIELD_NAME_excecutionDiscount(), "0")//
						.param(Promotion.field.getFIELD_NAME_scope(), String.valueOf(EnumPromotionScope.EPS_SpecifiedCommodities.getIndex()))//
						.param(Promotion.field.getFIELD_NAME_staff(), "1")//
						.param(Promotion.field.getFIELD_NAME_returnObject(), String.valueOf(1))//
						.param(Promotion.field.getFIELD_NAME_commodityIDs(), commodityCreate7.getID() + "")//
		).andExpect(status().isOk()).andDo(print()).andReturn();

		Shared.checkJSONErrorCode(mr7);

		// ?????????
		Pos pos = (Pos) session.getAttribute(EnumSession.SESSION_POS.getName());
		int posID = pos.getID();
		Promotion pt = new Promotion();
		pt.setName("?????????????????????10???1");
		pt.setStatus(EnumStatusPromotion.ESP_Active.getIndex());
		pt.setType(EnumTypePromotion.ETP_DecreaseOnAmount.getIndex());
		Date dtStart = simpleDateFormat.parse(l);
		pt.setDatetimeStart(dtStart);
		Date dtEnd = simpleDateFormat.parse("3000/10/22");
		pt.setDatetimeEnd(dtEnd);
		pt.setExcecutionThreshold(10);
		pt.setExcecutionAmount(10);
		pt.setExcecutionDiscount(0);
		pt.setScope(EnumPromotionScope.EPS_SpecifiedCommodities.getIndex());
		pt.setStaff(1);
		//
		PromotionCP.verifyCreate(mr7, pt, Shared.DBName_Test, posBO, EnumSyncCacheType.ESCT_PromotionSyncInfo, promotionSyncCacheBO, promotionSyncCacheDispatcherBO, posID, promotionScopeMapper);
	}

	@Test
	public void testCreateEx8() throws Exception {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
		String l = simpleDateFormat.format(DatetimeUtil.getDays(new Date(), 3));

		Shared.caseLog("case8??????????????????????????????????????????????????????????????????????????????????????????");
		// 1???????????????????????????
		Commodity commodity = BaseCommodityTest.DataInput.getCommodity(CommodityType.EnumCommodityType.ECT_Normal.getIndex());
		Commodity commodityCreate8 = BaseCommodityTest.createCommodityViaAction(commodity, mvc, session, mapBO, Shared.DBName_Test);

		MvcResult mr8 = mvc.perform(//
				post("/promotionSync/createEx.bx") //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) session) //
						.param(Promotion.field.getFIELD_NAME_name(), "?????????????????????20???10")//
						.param(Promotion.field.getFIELD_NAME_status(), String.valueOf(EnumStatusPromotion.ESP_Active.getIndex()))//
						.param(Promotion.field.getFIELD_NAME_type(), String.valueOf(EnumTypePromotion.ETP_DecreaseOnAmount.getIndex()))//
						.param(Promotion.field.getFIELD_NAME_datetimeStart(), l)//
						.param(Promotion.field.getFIELD_NAME_datetimeEnd(), "3000/10/22")//
						.param(Promotion.field.getFIELD_NAME_excecutionThreshold(), "20")//
						.param(Promotion.field.getFIELD_NAME_excecutionAmount(), "10")//
						.param(Promotion.field.getFIELD_NAME_excecutionDiscount(), "0")//
						.param(Promotion.field.getFIELD_NAME_scope(), String.valueOf(EnumPromotionScope.EPS_SpecifiedCommodities.getIndex()))//
						.param(Promotion.field.getFIELD_NAME_staff(), "1")//
						.param(Promotion.field.getFIELD_NAME_returnObject(), String.valueOf(1))//
						.param(Promotion.field.getFIELD_NAME_commodityIDs(), commodityCreate8.getID() + "")//
		).andExpect(status().isOk()).andDo(print()).andReturn();

		Shared.checkJSONErrorCode(mr8);

		// ?????????
		Pos pos = (Pos) session.getAttribute(EnumSession.SESSION_POS.getName());
		int posID = pos.getID();
		Promotion pt = new Promotion();
		pt.setName("?????????????????????20???10");
		pt.setStatus(EnumStatusPromotion.ESP_Active.getIndex());
		pt.setType(EnumTypePromotion.ETP_DecreaseOnAmount.getIndex());
		Date dtStart = simpleDateFormat.parse(l);
		pt.setDatetimeStart(dtStart);
		Date dtEnd = simpleDateFormat.parse("3000/10/22");
		pt.setDatetimeEnd(dtEnd);
		pt.setExcecutionThreshold(20);
		pt.setExcecutionAmount(10);
		pt.setExcecutionDiscount(0);
		pt.setScope(EnumPromotionScope.EPS_SpecifiedCommodities.getIndex());
		pt.setStaff(1);
		//
		PromotionCP.verifyCreate(mr8, pt, Shared.DBName_Test, posBO, EnumSyncCacheType.ESCT_PromotionSyncInfo, promotionSyncCacheBO, promotionSyncCacheDispatcherBO, posID, promotionScopeMapper);
	}

	@Test
	public void testCreateEx9() throws Exception {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
		String l = simpleDateFormat.format(DatetimeUtil.getDays(new Date(), 3));

		Shared.caseLog("case9??????????????????????????????????????????????????????????????????????????????????????????");
		// 1???????????????????????????
		Commodity commodity = BaseCommodityTest.DataInput.getCommodity(CommodityType.EnumCommodityType.ECT_Normal.getIndex());
		Commodity commodityCreate9 = BaseCommodityTest.createCommodityViaAction(commodity, mvc, session, mapBO, Shared.DBName_Test);

		MvcResult mr9 = mvc.perform(//
				post("/promotionSync/createEx.bx") //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) session) //
						.param(Promotion.field.getFIELD_NAME_name(), "?????????????????????10???20")//
						.param(Promotion.field.getFIELD_NAME_status(), String.valueOf(EnumStatusPromotion.ESP_Active.getIndex()))//
						.param(Promotion.field.getFIELD_NAME_type(), String.valueOf(EnumTypePromotion.ETP_DecreaseOnAmount.getIndex()))//
						.param(Promotion.field.getFIELD_NAME_datetimeStart(), l)//
						.param(Promotion.field.getFIELD_NAME_datetimeEnd(), "3000/10/22")//
						.param(Promotion.field.getFIELD_NAME_excecutionThreshold(), "10")//
						.param(Promotion.field.getFIELD_NAME_excecutionAmount(), "20")//
						.param(Promotion.field.getFIELD_NAME_excecutionDiscount(), "0")//
						.param(Promotion.field.getFIELD_NAME_scope(), String.valueOf(EnumPromotionScope.EPS_SpecifiedCommodities.getIndex()))//
						.param(Promotion.field.getFIELD_NAME_staff(), "1")//
						.param(Promotion.field.getFIELD_NAME_returnObject(), String.valueOf(1))//
						.param(Promotion.field.getFIELD_NAME_commodityIDs(), commodityCreate9.getID() + "")//
		).andExpect(status().isOk()).andDo(print()).andReturn();

		Shared.checkJSONErrorCode(mr9, EnumErrorCode.EC_WrongFormatForInputField);
	}

	@Test
	public void testCreateEx10() throws Exception {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
		String l = simpleDateFormat.format(DatetimeUtil.getDays(new Date(), 3));

		Shared.caseLog("case10???????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????");
		// 1???????????????????????????
		Commodity commodity = BaseCommodityTest.DataInput.getCommodity(CommodityType.EnumCommodityType.ECT_Normal.getIndex());
		Commodity commodityCreate10 = BaseCommodityTest.createCommodityViaAction(commodity, mvc, session, mapBO, Shared.DBName_Test);

		MvcResult mr10 = mvc.perform(//
				post("/promotionSync/createEx.bx") //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) session) //
						.param(Promotion.field.getFIELD_NAME_name(), "?????????????????????10001???10001")//
						.param(Promotion.field.getFIELD_NAME_status(), String.valueOf(EnumStatusPromotion.ESP_Active.getIndex()))//
						.param(Promotion.field.getFIELD_NAME_type(), String.valueOf(EnumTypePromotion.ETP_DecreaseOnAmount.getIndex()))//
						.param(Promotion.field.getFIELD_NAME_datetimeStart(), l)//
						.param(Promotion.field.getFIELD_NAME_datetimeEnd(), "3000/10/22")//
						.param(Promotion.field.getFIELD_NAME_excecutionThreshold(), "10001")//
						.param(Promotion.field.getFIELD_NAME_excecutionAmount(), "10001")//
						.param(Promotion.field.getFIELD_NAME_excecutionDiscount(), "0")//
						.param(Promotion.field.getFIELD_NAME_scope(), String.valueOf(EnumPromotionScope.EPS_SpecifiedCommodities.getIndex()))//
						.param(Promotion.field.getFIELD_NAME_staff(), "1")//
						.param(Promotion.field.getFIELD_NAME_returnObject(), String.valueOf(1))//
						.param(Promotion.field.getFIELD_NAME_commodityIDs(), commodityCreate10.getID() + "")//
		).andExpect(status().isOk()).andDo(print()).andReturn();

		Shared.checkJSONErrorCode(mr10, EnumErrorCode.EC_WrongFormatForInputField);

	}

	@Test
	public void testCreateEx11() throws Exception {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
		String l = simpleDateFormat.format(DatetimeUtil.getDays(new Date(), 3));

		Shared.caseLog("case11???????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????");
		// 1???????????????????????????
		Commodity commodity = BaseCommodityTest.DataInput.getCommodity(CommodityType.EnumCommodityType.ECT_Normal.getIndex());
		Commodity commodityCreate11 = BaseCommodityTest.createCommodityViaAction(commodity, mvc, session, mapBO, Shared.DBName_Test);

		MvcResult mr11 = mvc.perform(//
				post("/promotionSync/createEx.bx") //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) session) //
						.param(Promotion.field.getFIELD_NAME_name(), "?????????????????????10001???10")//
						.param(Promotion.field.getFIELD_NAME_status(), String.valueOf(EnumStatusPromotion.ESP_Active.getIndex()))//
						.param(Promotion.field.getFIELD_NAME_type(), String.valueOf(EnumTypePromotion.ETP_DiscountOnAmount.getIndex()))//
						.param(Promotion.field.getFIELD_NAME_datetimeStart(), l)//
						.param(Promotion.field.getFIELD_NAME_datetimeEnd(), "3000/10/22")//
						.param(Promotion.field.getFIELD_NAME_excecutionThreshold(), "10001")//
						.param(Promotion.field.getFIELD_NAME_excecutionAmount(), "10")//
						.param(Promotion.field.getFIELD_NAME_excecutionDiscount(), "0")//
						.param(Promotion.field.getFIELD_NAME_scope(), String.valueOf(EnumPromotionScope.EPS_SpecifiedCommodities.getIndex()))//
						.param(Promotion.field.getFIELD_NAME_staff(), "1")//
						.param(Promotion.field.getFIELD_NAME_returnObject(), String.valueOf(1))//
						.param(Promotion.field.getFIELD_NAME_commodityIDs(), commodityCreate11.getID() + "")//
		).andExpect(status().isOk()).andDo(print()).andReturn();

		Shared.checkJSONErrorCode(mr11, EnumErrorCode.EC_WrongFormatForInputField);
	}

	@Test
	public void testCreateEx12() throws Exception {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
		String l = simpleDateFormat.format(DatetimeUtil.getDays(new Date(), 3));

		Shared.caseLog("case12???????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????");
		// 1???????????????????????????
		Commodity commodity = BaseCommodityTest.DataInput.getCommodity(CommodityType.EnumCommodityType.ECT_Normal.getIndex());
		Commodity commodityCreate12 = BaseCommodityTest.createCommodityViaAction(commodity, mvc, session, mapBO, Shared.DBName_Test);

		MvcResult mr12 = mvc.perform(//
				post("/promotionSync/createEx.bx") //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) session) //
						.param(Promotion.field.getFIELD_NAME_name(), "?????????????????????10001???10")//
						.param(Promotion.field.getFIELD_NAME_status(), String.valueOf(EnumStatusPromotion.ESP_Active.getIndex()))//
						.param(Promotion.field.getFIELD_NAME_type(), String.valueOf(EnumTypePromotion.ETP_DiscountOnAmount.getIndex()))//
						.param(Promotion.field.getFIELD_NAME_datetimeStart(), l)//
						.param(Promotion.field.getFIELD_NAME_datetimeEnd(), "3000/10/22")//
						.param(Promotion.field.getFIELD_NAME_excecutionThreshold(), "10")//
						.param(Promotion.field.getFIELD_NAME_excecutionAmount(), "10001")//
						.param(Promotion.field.getFIELD_NAME_excecutionDiscount(), "0")//
						.param(Promotion.field.getFIELD_NAME_scope(), String.valueOf(EnumPromotionScope.EPS_SpecifiedCommodities.getIndex()))//
						.param(Promotion.field.getFIELD_NAME_staff(), "1")//
						.param(Promotion.field.getFIELD_NAME_returnObject(), String.valueOf(1))//
						.param(Promotion.field.getFIELD_NAME_commodityIDs(), commodityCreate12.getID() + "")//
		).andExpect(status().isOk()).andDo(print()).andReturn();

		Shared.checkJSONErrorCode(mr12, EnumErrorCode.EC_WrongFormatForInputField);
	}

	@Test
	public void testCreateEx13() throws Exception {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
		String l = simpleDateFormat.format(DatetimeUtil.getDays(new Date(), 3));

		Shared.caseLog("case13: ?????????????????????????????????");
		Commodity commodity = BaseCommodityTest.DataInput.getCommodity(CommodityType.EnumCommodityType.ECT_Service.getIndex());
		Commodity commodityCreate13 = BaseCommodityTest.createCommodityViaAction(commodity, mvc, session, mapBO, Shared.DBName_Test);
		//
		MvcResult mr13 = mvc.perform(//
				post("/promotionSync/createEx.bx") //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) session) //
						.param(Promotion.field.getFIELD_NAME_name(), "?????????????????????20???10")//
						.param(Promotion.field.getFIELD_NAME_status(), String.valueOf(EnumStatusPromotion.ESP_Active.getIndex()))//
						.param(Promotion.field.getFIELD_NAME_type(), String.valueOf(EnumTypePromotion.ETP_DiscountOnAmount.getIndex()))//
						.param(Promotion.field.getFIELD_NAME_datetimeStart(), l)//
						.param(Promotion.field.getFIELD_NAME_datetimeEnd(), "3000/10/22")//
						.param(Promotion.field.getFIELD_NAME_excecutionThreshold(), "20")//
						.param(Promotion.field.getFIELD_NAME_excecutionAmount(), "10")//
						.param(Promotion.field.getFIELD_NAME_excecutionDiscount(), "0")//
						.param(Promotion.field.getFIELD_NAME_scope(), String.valueOf(EnumPromotionScope.EPS_SpecifiedCommodities.getIndex()))//
						.param(Promotion.field.getFIELD_NAME_staff(), "1")//
						.param(Promotion.field.getFIELD_NAME_returnObject(), String.valueOf(1))//
						.param(Promotion.field.getFIELD_NAME_commodityIDs(), String.valueOf(commodityCreate13.getID()))//
		).andExpect(status().isOk()).andDo(print()).andReturn();
		//
		// Shared.checkJSONErrorCode(mr13, EnumErrorCode.EC_BusinessLogicNotDefined);
		String o13 = mr13.getResponse().getContentAsString();
		assertTrue(o13.length() == 0, "CASE13?????????????????????????????????????????????");

	}

	@Test
	public void testCreateEx14() throws Exception {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
		String l = simpleDateFormat.format(DatetimeUtil.getDays(new Date(), 3));

		Shared.caseLog("case14??????????????????????????????????????????????????????????????????(commodityID???null)");
		String commodityList = null;
		MvcResult mr14 = mvc.perform(//
				post("/promotionSync/createEx.bx") //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) session) //
						.param(Promotion.field.getFIELD_NAME_name(), "?????????????????????20???10")//
						.param(Promotion.field.getFIELD_NAME_status(), String.valueOf(EnumStatusPromotion.ESP_Active.getIndex()))//
						.param(Promotion.field.getFIELD_NAME_type(), String.valueOf(EnumTypePromotion.ETP_DecreaseOnAmount.getIndex()))//
						.param(Promotion.field.getFIELD_NAME_datetimeStart(), l)//
						.param(Promotion.field.getFIELD_NAME_datetimeEnd(), "3000/10/22")//
						.param(Promotion.field.getFIELD_NAME_excecutionThreshold(), "20")//
						.param(Promotion.field.getFIELD_NAME_excecutionAmount(), "10")//
						.param(Promotion.field.getFIELD_NAME_excecutionDiscount(), "0")//
						.param(Promotion.field.getFIELD_NAME_scope(), String.valueOf(EnumPromotionScope.EPS_SpecifiedCommodities.getIndex()))//
						.param(Promotion.field.getFIELD_NAME_staff(), "1")//
						.param(Promotion.field.getFIELD_NAME_returnObject(), String.valueOf(1))//
						.param(Promotion.field.getFIELD_NAME_commodityIDs(), commodityList)//
		).andExpect(status().isOk()).andDo(print()).andReturn();
		//
		String o14 = mr14.getResponse().getContentAsString();
		assertTrue(o14.length() == 0, "CASE14?????????????????????????????????????????????");
	}

	@Test
	public void testCreateEx15() throws Exception {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
		String l = simpleDateFormat.format(DatetimeUtil.getDays(new Date(), 3));

		//
		Shared.caseLog("case15??????????????????????????????????????????????????????????????????(commodityID?????????");
		MvcResult mr15 = mvc.perform(//
				post("/promotionSync/createEx.bx") //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) session) //
						.param(Promotion.field.getFIELD_NAME_name(), "?????????????????????20???10")//
						.param(Promotion.field.getFIELD_NAME_status(), String.valueOf(EnumStatusPromotion.ESP_Active.getIndex()))//
						.param(Promotion.field.getFIELD_NAME_type(), String.valueOf(EnumTypePromotion.ETP_DecreaseOnAmount.getIndex()))//
						.param(Promotion.field.getFIELD_NAME_datetimeStart(), l)//
						.param(Promotion.field.getFIELD_NAME_datetimeEnd(), "3000/10/22")//
						.param(Promotion.field.getFIELD_NAME_excecutionThreshold(), "20")//
						.param(Promotion.field.getFIELD_NAME_excecutionAmount(), "10")//
						.param(Promotion.field.getFIELD_NAME_excecutionDiscount(), "0")//
						.param(Promotion.field.getFIELD_NAME_scope(), String.valueOf(EnumPromotionScope.EPS_SpecifiedCommodities.getIndex()))//
						.param(Promotion.field.getFIELD_NAME_staff(), "1")//
						.param(Promotion.field.getFIELD_NAME_returnObject(), String.valueOf(1))//
						.param(Promotion.field.getFIELD_NAME_commodityIDs(), String.valueOf(""))//
		).andExpect(status().isOk()).andDo(print()).andReturn();
		//
		String o15 = mr15.getResponse().getContentAsString();
		assertTrue(o15.length() == 0, "CASE15?????????????????????????????????????????????");

	}

	@Test
	public void testCreateEx16() throws Exception {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
		String l = simpleDateFormat.format(DatetimeUtil.getDays(new Date(), 3));
		//
		Shared.caseLog("case16?????????????????????????????????????????????????????????????????????????????????");
		Commodity commodity = BaseCommodityTest.DataInput.getCommodity(CommodityType.EnumCommodityType.ECT_Combination.getIndex());
		String subCommodityInfo = JSONObject.fromObject(commodity).toString();
		commodity.setSubCommodityInfo(subCommodityInfo);
		Commodity commodityCreate16 = BaseCommodityTest.createCommodityViaAction(commodity, mvc, session, mapBO, Shared.DBName_Test);
		commodityCreate16.setStatus(2);
		//
		MvcResult mr16 = mvc.perform(//
				post("/promotionSync/createEx.bx") //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) session) //
						.param(Promotion.field.getFIELD_NAME_name(), "?????????????????????20???10")//
						.param(Promotion.field.getFIELD_NAME_status(), String.valueOf(EnumStatusPromotion.ESP_Active.getIndex()))//
						.param(Promotion.field.getFIELD_NAME_type(), "0")//
						.param(Promotion.field.getFIELD_NAME_datetimeStart(), l)//
						.param(Promotion.field.getFIELD_NAME_datetimeEnd(), "3000/10/22")//
						.param(Promotion.field.getFIELD_NAME_excecutionThreshold(), "20")//
						.param(Promotion.field.getFIELD_NAME_excecutionAmount(), "10")//
						.param(Promotion.field.getFIELD_NAME_excecutionDiscount(), "0")//
						.param(Promotion.field.getFIELD_NAME_scope(), "1")//
						.param(Promotion.field.getFIELD_NAME_staff(), "1")//
						.param(Promotion.field.getFIELD_NAME_returnObject(), "1")//
						.param(Promotion.field.getFIELD_NAME_commodityIDs(), String.valueOf(commodityCreate16.getID()))//
		).andExpect(status().isOk()).andDo(print()).andReturn();
		//
		String o16 = mr16.getResponse().getContentAsString();
		assertTrue(o16.length() == 0, "CASE16?????????????????????????????????????????????");
	}

	@Test
	public void testCreateEx17() throws Exception {
		Shared.caseLog("Case17 ?????????????????????????????????????????????");
		// ????????????
		Commodity c = BaseCommodityTest.DataInput.getCommodity(CommodityType.EnumCommodityType.ECT_MultiPackaging.getIndex());
		Commodity cCreate = BaseCommodityTest.createCommodityViaMapper(c, BaseBO.CASE_Commodity_CreateMultiPackaging);
		// ????????????
		Promotion promotion = BasePromotionTest.DataInput.getPromotion();
		promotion.setScope(EnumBoolean.EB_Yes.getIndex());
		//
		PromotionScope promotionScope = new PromotionScope();
		promotionScope.setCommodityID(cCreate.getID());
		List<PromotionScope> listPromotionScope = new ArrayList<PromotionScope>();
		listPromotionScope.add(promotionScope);
		promotion.setListSlave1(listPromotionScope);

		MvcResult mr = mvc.perform( //
				BasePromotionTest.DataInput.getBuilder("/promotionSync/createEx.bx", MediaType.APPLICATION_JSON, promotion) //
						.session((MockHttpSession) session) //
		).andExpect(status().isOk()).andDo(print()).andReturn(); //
		Assert.assertTrue("".equals(mr.getResponse().getContentAsString()), "??????????????????????????????????????????");

		BaseCommodityTest.deleteCommodityViaMapper(cCreate, EnumErrorCode.EC_NoError);
	}

	@Test
	public void testCreateEx18() throws Exception {
		Shared.caseLog("Case18 ??????????????????????????????????????????");
		// ????????????
		Commodity c = BaseCommodityTest.DataInput.getCommodity(CommodityType.EnumCommodityType.ECT_Combination.getIndex());
		Commodity cCreate = BaseCommodityTest.createCommodityViaMapper(c, BaseBO.CASE_Commodity_CreateComposition);
		// ????????????
		Promotion promotion = BasePromotionTest.DataInput.getPromotion();
		promotion.setScope(EnumBoolean.EB_Yes.getIndex());
		//
		PromotionScope promotionScope = new PromotionScope();
		promotionScope.setCommodityID(cCreate.getID());
		List<PromotionScope> listPromotionScope = new ArrayList<PromotionScope>();
		listPromotionScope.add(promotionScope);
		promotion.setListSlave1(listPromotionScope);

		MvcResult mr = mvc.perform( //
				BasePromotionTest.DataInput.getBuilder("/promotionSync/createEx.bx", MediaType.APPLICATION_JSON, promotion) //
						.session((MockHttpSession) session) //
		).andExpect(status().isOk()).andDo(print()).andReturn(); //
		Assert.assertTrue("".equals(mr.getResponse().getContentAsString()), "???????????????????????????????????????");

		BaseCommodityTest.deleteCommodityViaMapper(cCreate, EnumErrorCode.EC_NoError);
	}

	@Test
	public void testCreateEx19() throws Exception {
		Shared.caseLog("Case19 ???????????????,???????????????????????????");
		// ????????????
		Commodity c = BaseCommodityTest.DataInput.getCommodity(CommodityType.EnumCommodityType.ECT_Normal.getIndex());
		Commodity cCreate = BaseCommodityTest.createCommodityViaMapper(c, BaseBO.CASE_Commodity_CreateSingle);
		// ????????????
		Promotion promotion = BasePromotionTest.DataInput.getPromotion();
		promotion.setScope(EnumBoolean.EB_Yes.getIndex());
		//
		PromotionScope promotionScopeA = new PromotionScope();
		promotionScopeA.setCommodityID(cCreate.getID());
		List<PromotionScope> listPromotionScope = new ArrayList<PromotionScope>();
		listPromotionScope.add(promotionScopeA);
		//
		PromotionScope promotionScopeB = new PromotionScope();
		promotionScopeB.setCommodityID(cCreate.getID());
		listPromotionScope.add(promotionScopeB);
		//
		promotion.setListSlave1(listPromotionScope);

		MvcResult mr = mvc.perform( //
				BasePromotionTest.DataInput.getBuilder("/promotionSync/createEx.bx", MediaType.APPLICATION_JSON, promotion) //
						.session((MockHttpSession) session) //
		).andExpect(status().isOk()).andDo(print()).andReturn(); //
		Assert.assertTrue("".equals(mr.getResponse().getContentAsString()), "???????????????????????????????????????");

		BaseCommodityTest.deleteCommodityViaMapper(cCreate, EnumErrorCode.EC_NoError);
	}
	
	@Test
	public void testCreateEx20() throws Exception {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
		String l = simpleDateFormat.format(DatetimeUtil.getDays(new Date(), 3));

		Shared.caseLog("case20:??????????????????????????????????????????????????????????????????");
		// ????????????????????????
		Commodity commodity = BaseCommodityTest.DataInput.getCommodity(CommodityType.EnumCommodityType.ECT_Normal.getIndex());
		Commodity commodityCreate51 = BaseCommodityTest.createCommodityViaAction(commodity, mvc, session, mapBO, Shared.DBName_Test);
		//
		Commodity commodity2 = BaseCommodityTest.DataInput.getCommodity(CommodityType.EnumCommodityType.ECT_Normal.getIndex());
		Commodity commodityCreate52 = BaseCommodityTest.createCommodityViaAction(commodity2, mvc, session, mapBO, Shared.DBName_Test);

		String string5 = commodityCreate51.getID() + "," + commodityCreate52.getID();
		//	??????????????????
		Shop shopGet1 = BaseShopTest.DataInput.getShop();
		Shop shopCreate1 = BaseShopTest.createViaCompanyAction(shopGet1, mvc, mapBO, sessionOP, EnumErrorCode.EC_NoError);
		Shop shopGet2 = BaseShopTest.DataInput.getShop();
		Shop shopCreate2 = BaseShopTest.createViaCompanyAction(shopGet2, mvc, mapBO, sessionOP, EnumErrorCode.EC_NoError);
		String shopIdString = shopCreate1.getID() + "," + shopCreate2.getID();
		//
		Promotion p5 = BasePromotionTest.DataInput.getPromotion();
		p5.setReturnObject(RETURN_OBJECT);
		p5.setCommodityIDs(string5);
		p5.setShopIDs(shopIdString);
		p5.setDatetimeStart(simpleDateFormat.parse(l));
		p5.setDatetimeEnd(simpleDateFormat.parse("3000/10/22"));
		p5.setScope(EnumPromotionScope.EPS_SpecifiedCommodities.getIndex());
		p5.setShopScope(EnumPromotionShopScope.EPS_SpecifiedShops.getIndex());
		PromotionScope promotionScopecope = new PromotionScope();
		promotionScopecope.setCommodityID(commodityCreate51.getID());
		promotionScopecope.setCommodityName(commodityCreate51.getName());
		promotionScopecope.setPromotionID(p5.getID());
		//
		PromotionScope promotionScopecope1 = new PromotionScope();
		promotionScopecope1.setCommodityID(commodityCreate52.getID());
		promotionScopecope1.setCommodityName(commodityCreate52.getName());
		promotionScopecope1.setPromotionID(p5.getID());
		//
		List<PromotionScope> listPromotionScope = new ArrayList<PromotionScope>();
		listPromotionScope.add(promotionScopecope);
		listPromotionScope.add(promotionScopecope1);
		//
		p5.setListSlave1(listPromotionScope);
		
		PromotionShopScope promotionoShopScope1 = new PromotionShopScope();
		promotionoShopScope1.setPromotionID(p5.getID());
		promotionoShopScope1.setShopID(shopCreate1.getID());
		PromotionShopScope promotionoShopScope2 = new PromotionShopScope();
		promotionoShopScope2.setPromotionID(p5.getID());
		promotionoShopScope2.setShopID(shopCreate2.getID());
		List<PromotionShopScope> listPromotionShopScope = new ArrayList<>();
		listPromotionShopScope.add(promotionoShopScope1);
		listPromotionShopScope.add(promotionoShopScope2);
		p5.setListSlave2(listPromotionShopScope);
		//
		MvcResult mr5 = mvc.perform(//
				post("/promotionSync/createEx.bx") //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) session) //
						.param(Promotion.field.getFIELD_NAME_name(), p5.getName())//
						.param(Promotion.field.getFIELD_NAME_status(), String.valueOf(p5.getStatus()))//
						.param(Promotion.field.getFIELD_NAME_type(), String.valueOf(p5.getType()))//
						.param(Promotion.field.getFIELD_NAME_datetimeStart(), simpleDateFormat.format(p5.getDatetimeStart()))//
						.param(Promotion.field.getFIELD_NAME_datetimeEnd(), simpleDateFormat.format(p5.getDatetimeEnd()))//
						.param(Promotion.field.getFIELD_NAME_excecutionThreshold(), String.valueOf(p5.getExcecutionThreshold()))//
						.param(Promotion.field.getFIELD_NAME_excecutionAmount(), String.valueOf(p5.getExcecutionAmount()))//
						.param(Promotion.field.getFIELD_NAME_excecutionDiscount(), String.valueOf(p5.getExcecutionDiscount()))//
						.param(Promotion.field.getFIELD_NAME_scope(), String.valueOf(p5.getScope()))//
						.param(Promotion.field.getFIELD_NAME_shopScope(), String.valueOf(p5.getShopScope()))//
						.param(Promotion.field.getFIELD_NAME_staff(), String.valueOf(p5.getStaff()))//
						.param(Promotion.field.getFIELD_NAME_returnObject(), String.valueOf(p5.getReturnObject()))//
						.param(Promotion.field.getFIELD_NAME_commodityIDs(), p5.getCommodityIDs())//
						.param(Promotion.field.getFIELD_NAME_shopIDs(), p5.getShopIDs())//
		).andExpect(status().isOk()).andDo(print()).andReturn();

		Shared.checkJSONErrorCode(mr5);

		JSONObject o5 = JSONObject.fromObject(mr5.getResponse().getContentAsString());
		Promotion jsonToPromotion = (Promotion) new Promotion().parse1(String.valueOf(o5.getJSONObject(BaseAction.KEY_Object)));
		List<?> list = jsonToPromotion.getListSlave1(); // java?????????????????????????????????DB????????????????????????????????????
		List<?> list2 = jsonToPromotion.getListSlave2(); // java?????????????????????????????????DB????????????????????????????????????
		jsonToPromotion.setListSlave1(p5.getListSlave1());
		jsonToPromotion.setListSlave2(p5.getListSlave2());
		p5.setIgnoreIDInComparision(true);
		if (p5.compareTo(jsonToPromotion) != 0) {
			Assert.assertTrue(false, "??????????????????DB????????????????????????");
		}
		jsonToPromotion.setListSlave1(list); // ????????????????????????
		jsonToPromotion.setListSlave2(list2); // ????????????????????????
		// ?????????????????????
		PromotionScope ps = new PromotionScope();
		ps.setPromotionID(jsonToPromotion.getID());
		Map<String, Object> psParamsRN = ps.getRetrieveNParam(BaseBO.INVALID_CASE_ID, ps);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> psRN = promotionScopeMapper.retrieveN(psParamsRN);
		//
		for (BaseModel bm : psRN) {
			Assert.assertTrue(((PromotionScope) bm).getCommodityName().equals(commodityCreate51.getName()) //
					|| ((PromotionScope) bm).getCommodityName().equals(commodityCreate52.getName()), "????????????????????????F_CommodityName???????????????");
		}
		//
		PromotionShopScope pshopScope = new PromotionShopScope();
		pshopScope.setPromotionID(jsonToPromotion.getID());
		Map<String, Object> pshopScopeParamsRN = pshopScope.getRetrieveNParam(BaseBO.INVALID_CASE_ID, pshopScope);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> pshopScopeRN = promotionShopScopeMapper.retrieveN(pshopScopeParamsRN);
		Assert.assertTrue(pshopScopeRN != null && pshopScopeRN.size() == 2, "????????????????????????????????????");
		//
		for (BaseModel bm : pshopScopeRN) {
			Assert.assertTrue(((PromotionShopScope) bm).getShopName().equals(shopCreate1.getName()) //
					|| ((PromotionShopScope) bm).getShopName().equals(shopCreate2.getName()), "????????????????????????F_ShopName???????????????");
		}
		// ?????????
		Pos pos = (Pos) session.getAttribute(EnumSession.SESSION_POS.getName());
		int posID = pos.getID();
		PromotionCP.verifyCreate(mr5, p5, Shared.DBName_Test, posBO, EnumSyncCacheType.ESCT_PromotionSyncInfo, promotionSyncCacheBO, promotionSyncCacheDispatcherBO, posID, promotionScopeMapper);
	}
	
	@Test
	public void testCreateEx21() throws Exception {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
		String l = simpleDateFormat.format(DatetimeUtil.getDays(new Date(), 3));

		Shared.caseLog("case21:??????????????????????????????????????????");
		//	??????????????????
		Shop shopGet1 = BaseShopTest.DataInput.getShop();
		Shop shopCreate1 = BaseShopTest.createViaCompanyAction(shopGet1, mvc, mapBO, sessionOP, EnumErrorCode.EC_NoError);
		Shop shopGet2 = BaseShopTest.DataInput.getShop();
		Shop shopCreate2 = BaseShopTest.createViaCompanyAction(shopGet2, mvc, mapBO, sessionOP, EnumErrorCode.EC_NoError);
		String shopIdString = shopCreate1.getID() + "," + shopCreate2.getID();
		//
		Promotion p5 = BasePromotionTest.DataInput.getPromotion();
		p5.setReturnObject(RETURN_OBJECT);
		p5.setShopIDs(shopIdString);
		p5.setDatetimeStart(simpleDateFormat.parse(l));
		p5.setDatetimeEnd(simpleDateFormat.parse("3000/10/22"));
		p5.setScope(EnumPromotionScope.EPS_AllCommodities.getIndex());
		p5.setShopScope(EnumPromotionShopScope.EPS_SpecifiedShops.getIndex());
		//
		PromotionShopScope promotionoShopScope1 = new PromotionShopScope();
		promotionoShopScope1.setPromotionID(p5.getID());
		promotionoShopScope1.setShopID(shopCreate1.getID());
		PromotionShopScope promotionoShopScope2 = new PromotionShopScope();
		promotionoShopScope2.setPromotionID(p5.getID());
		promotionoShopScope2.setShopID(shopCreate2.getID());
		List<PromotionShopScope> listPromotionShopScope = new ArrayList<>();
		listPromotionShopScope.add(promotionoShopScope1);
		listPromotionShopScope.add(promotionoShopScope2);
		p5.setListSlave2(listPromotionShopScope);
		//
		MvcResult mr5 = mvc.perform(//
				post("/promotionSync/createEx.bx") //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) session) //
						.param(Promotion.field.getFIELD_NAME_name(), p5.getName())//
						.param(Promotion.field.getFIELD_NAME_status(), String.valueOf(p5.getStatus()))//
						.param(Promotion.field.getFIELD_NAME_type(), String.valueOf(p5.getType()))//
						.param(Promotion.field.getFIELD_NAME_datetimeStart(), simpleDateFormat.format(p5.getDatetimeStart()))//
						.param(Promotion.field.getFIELD_NAME_datetimeEnd(), simpleDateFormat.format(p5.getDatetimeEnd()))//
						.param(Promotion.field.getFIELD_NAME_excecutionThreshold(), String.valueOf(p5.getExcecutionThreshold()))//
						.param(Promotion.field.getFIELD_NAME_excecutionAmount(), String.valueOf(p5.getExcecutionAmount()))//
						.param(Promotion.field.getFIELD_NAME_excecutionDiscount(), String.valueOf(p5.getExcecutionDiscount()))//
						.param(Promotion.field.getFIELD_NAME_scope(), String.valueOf(p5.getScope()))//
						.param(Promotion.field.getFIELD_NAME_shopScope(), String.valueOf(p5.getShopScope()))//
						.param(Promotion.field.getFIELD_NAME_staff(), String.valueOf(p5.getStaff()))//
						.param(Promotion.field.getFIELD_NAME_returnObject(), String.valueOf(p5.getReturnObject()))//
						.param(Promotion.field.getFIELD_NAME_commodityIDs(), p5.getCommodityIDs())//
						.param(Promotion.field.getFIELD_NAME_shopIDs(), p5.getShopIDs())//
		).andExpect(status().isOk()).andDo(print()).andReturn();

		Shared.checkJSONErrorCode(mr5);

		JSONObject o5 = JSONObject.fromObject(mr5.getResponse().getContentAsString());
		Promotion jsonToPromotion = (Promotion) new Promotion().parse1(String.valueOf(o5.getJSONObject(BaseAction.KEY_Object)));
		List<?> list2 = jsonToPromotion.getListSlave2(); // java?????????????????????????????????DB????????????????????????????????????
		jsonToPromotion.setListSlave2(p5.getListSlave2());
		p5.setIgnoreIDInComparision(true);
		if (p5.compareTo(jsonToPromotion) != 0) {
			Assert.assertTrue(false, "??????????????????DB????????????????????????");
		}
		jsonToPromotion.setListSlave2(list2); // ????????????????????????
		// ?????????????????????
		PromotionShopScope pshopScope = new PromotionShopScope();
		pshopScope.setPromotionID(jsonToPromotion.getID());
		Map<String, Object> pshopScopeParamsRN = pshopScope.getRetrieveNParam(BaseBO.INVALID_CASE_ID, pshopScope);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> pshopScopeRN = promotionShopScopeMapper.retrieveN(pshopScopeParamsRN);
		Assert.assertTrue(pshopScopeRN != null && pshopScopeRN.size() == 2, "????????????????????????????????????");
		//
		for (BaseModel bm : pshopScopeRN) {
			Assert.assertTrue(((PromotionShopScope) bm).getShopName().equals(shopCreate1.getName()) //
					|| ((PromotionShopScope) bm).getShopName().equals(shopCreate2.getName()), "????????????????????????F_ShopName???????????????");
		}
		// ?????????
		Pos pos = (Pos) session.getAttribute(EnumSession.SESSION_POS.getName());
		int posID = pos.getID();
		PromotionCP.verifyCreate(mr5, p5, Shared.DBName_Test, posBO, EnumSyncCacheType.ESCT_PromotionSyncInfo, promotionSyncCacheBO, promotionSyncCacheDispatcherBO, posID, promotionScopeMapper);
	}
	
	@Test
	public void testCreateEx22() throws Exception {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
		String l = simpleDateFormat.format(DatetimeUtil.getDays(new Date(), 3));

		Shared.caseLog("case22:??????????????????????????????????????????, ??????shopIDs???null");
		//
		Promotion p5 = BasePromotionTest.DataInput.getPromotion();
		p5.setReturnObject(RETURN_OBJECT);
		p5.setShopIDs(null);
		p5.setDatetimeStart(simpleDateFormat.parse(l));
		p5.setDatetimeEnd(simpleDateFormat.parse("3000/10/22"));
		p5.setScope(EnumPromotionScope.EPS_AllCommodities.getIndex());
		p5.setShopScope(EnumPromotionShopScope.EPS_SpecifiedShops.getIndex());
		//
		MvcResult mr5 = mvc.perform(//
				post("/promotionSync/createEx.bx") //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) session) //
						.param(Promotion.field.getFIELD_NAME_name(), p5.getName())//
						.param(Promotion.field.getFIELD_NAME_status(), String.valueOf(p5.getStatus()))//
						.param(Promotion.field.getFIELD_NAME_type(), String.valueOf(p5.getType()))//
						.param(Promotion.field.getFIELD_NAME_datetimeStart(), simpleDateFormat.format(p5.getDatetimeStart()))//
						.param(Promotion.field.getFIELD_NAME_datetimeEnd(), simpleDateFormat.format(p5.getDatetimeEnd()))//
						.param(Promotion.field.getFIELD_NAME_excecutionThreshold(), String.valueOf(p5.getExcecutionThreshold()))//
						.param(Promotion.field.getFIELD_NAME_excecutionAmount(), String.valueOf(p5.getExcecutionAmount()))//
						.param(Promotion.field.getFIELD_NAME_excecutionDiscount(), String.valueOf(p5.getExcecutionDiscount()))//
						.param(Promotion.field.getFIELD_NAME_scope(), String.valueOf(p5.getScope()))//
						.param(Promotion.field.getFIELD_NAME_shopScope(), String.valueOf(p5.getShopScope()))//
						.param(Promotion.field.getFIELD_NAME_staff(), String.valueOf(p5.getStaff()))//
						.param(Promotion.field.getFIELD_NAME_returnObject(), String.valueOf(p5.getReturnObject()))//
						.param(Promotion.field.getFIELD_NAME_commodityIDs(), p5.getCommodityIDs())//
						.param(Promotion.field.getFIELD_NAME_shopIDs(), p5.getShopIDs())//
		).andExpect(status().isOk()).andDo(print()).andReturn();

		String mr5Reponse = mr5.getResponse().getContentAsString();
		Assert.assertTrue(mr5Reponse.length() == 0, "CASE22?????????????????????????????????????????????");
	}
	
	@Test
	public void testCreateEx23() throws Exception {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
		String l = simpleDateFormat.format(DatetimeUtil.getDays(new Date(), 3));

		Shared.caseLog("case23:??????????????????????????????????????????, ??????shopIDs???????????????");
		//
		Promotion p5 = BasePromotionTest.DataInput.getPromotion();
		p5.setReturnObject(RETURN_OBJECT);
		p5.setShopIDs("");
		p5.setDatetimeStart(simpleDateFormat.parse(l));
		p5.setDatetimeEnd(simpleDateFormat.parse("3000/10/22"));
		p5.setScope(EnumPromotionScope.EPS_AllCommodities.getIndex());
		p5.setShopScope(EnumPromotionShopScope.EPS_SpecifiedShops.getIndex());
		//
		MvcResult mr5 = mvc.perform(//
				post("/promotionSync/createEx.bx") //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) session) //
						.param(Promotion.field.getFIELD_NAME_name(), p5.getName())//
						.param(Promotion.field.getFIELD_NAME_status(), String.valueOf(p5.getStatus()))//
						.param(Promotion.field.getFIELD_NAME_type(), String.valueOf(p5.getType()))//
						.param(Promotion.field.getFIELD_NAME_datetimeStart(), simpleDateFormat.format(p5.getDatetimeStart()))//
						.param(Promotion.field.getFIELD_NAME_datetimeEnd(), simpleDateFormat.format(p5.getDatetimeEnd()))//
						.param(Promotion.field.getFIELD_NAME_excecutionThreshold(), String.valueOf(p5.getExcecutionThreshold()))//
						.param(Promotion.field.getFIELD_NAME_excecutionAmount(), String.valueOf(p5.getExcecutionAmount()))//
						.param(Promotion.field.getFIELD_NAME_excecutionDiscount(), String.valueOf(p5.getExcecutionDiscount()))//
						.param(Promotion.field.getFIELD_NAME_scope(), String.valueOf(p5.getScope()))//
						.param(Promotion.field.getFIELD_NAME_shopScope(), String.valueOf(p5.getShopScope()))//
						.param(Promotion.field.getFIELD_NAME_staff(), String.valueOf(p5.getStaff()))//
						.param(Promotion.field.getFIELD_NAME_returnObject(), String.valueOf(p5.getReturnObject()))//
						.param(Promotion.field.getFIELD_NAME_commodityIDs(), p5.getCommodityIDs())//
						.param(Promotion.field.getFIELD_NAME_shopIDs(), p5.getShopIDs())//
		).andExpect(status().isOk()).andDo(print()).andReturn();

		String mr5Reponse = mr5.getResponse().getContentAsString();
		Assert.assertTrue(mr5Reponse.length() == 0, "CASE23?????????????????????????????????????????????");
	}
	
	@Test
	public void testCreateEx24() throws Exception {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
		String l = simpleDateFormat.format(DatetimeUtil.getDays(new Date(), 3));

		Shared.caseLog("case24:??????????????????????????????????????????, ??????????????????????????????????????????");
		//
		Promotion p5 = BasePromotionTest.DataInput.getPromotion();
		p5.setReturnObject(RETURN_OBJECT);
		p5.setShopIDs("1");
		p5.setDatetimeStart(simpleDateFormat.parse(l));
		p5.setDatetimeEnd(simpleDateFormat.parse("3000/10/22"));
		p5.setScope(EnumPromotionScope.EPS_AllCommodities.getIndex());
		p5.setShopScope(EnumPromotionShopScope.EPS_SpecifiedShops.getIndex());
		//
		MvcResult mr5 = mvc.perform(//
				post("/promotionSync/createEx.bx") //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) session) //
						.param(Promotion.field.getFIELD_NAME_name(), p5.getName())//
						.param(Promotion.field.getFIELD_NAME_status(), String.valueOf(p5.getStatus()))//
						.param(Promotion.field.getFIELD_NAME_type(), String.valueOf(p5.getType()))//
						.param(Promotion.field.getFIELD_NAME_datetimeStart(), simpleDateFormat.format(p5.getDatetimeStart()))//
						.param(Promotion.field.getFIELD_NAME_datetimeEnd(), simpleDateFormat.format(p5.getDatetimeEnd()))//
						.param(Promotion.field.getFIELD_NAME_excecutionThreshold(), String.valueOf(p5.getExcecutionThreshold()))//
						.param(Promotion.field.getFIELD_NAME_excecutionAmount(), String.valueOf(p5.getExcecutionAmount()))//
						.param(Promotion.field.getFIELD_NAME_excecutionDiscount(), String.valueOf(p5.getExcecutionDiscount()))//
						.param(Promotion.field.getFIELD_NAME_scope(), String.valueOf(p5.getScope()))//
						.param(Promotion.field.getFIELD_NAME_shopScope(), String.valueOf(p5.getShopScope()))//
						.param(Promotion.field.getFIELD_NAME_staff(), String.valueOf(p5.getStaff()))//
						.param(Promotion.field.getFIELD_NAME_returnObject(), String.valueOf(p5.getReturnObject()))//
						.param(Promotion.field.getFIELD_NAME_commodityIDs(), p5.getCommodityIDs())//
						.param(Promotion.field.getFIELD_NAME_shopIDs(), p5.getShopIDs())//
		).andExpect(status().isOk()).andDo(print()).andReturn();
		//
		Shared.checkJSONErrorCode(mr5, EnumErrorCode.EC_BusinessLogicNotDefined);
	}
	
	@Test
	public void testCreateEx25() throws Exception {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
		String l = simpleDateFormat.format(DatetimeUtil.getDays(new Date(), 3));

		Shared.caseLog("case25:????????????????????????????????????????????????");
		//
		Promotion p5 = BasePromotionTest.DataInput.getPromotion();
		p5.setReturnObject(RETURN_OBJECT);
		p5.setDatetimeStart(simpleDateFormat.parse(l));
		p5.setDatetimeEnd(simpleDateFormat.parse("3000/10/22"));
		p5.setScope(EnumPromotionScope.EPS_AllCommodities.getIndex());
		p5.setShopScope(EnumPromotionShopScope.EPS_AllShops.getIndex());
		//
		MvcResult mr5 = mvc.perform(//
				post("/promotionSync/createEx.bx") //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) session) //
						.param(Promotion.field.getFIELD_NAME_name(), p5.getName())//
						.param(Promotion.field.getFIELD_NAME_status(), String.valueOf(p5.getStatus()))//
						.param(Promotion.field.getFIELD_NAME_type(), String.valueOf(p5.getType()))//
						.param(Promotion.field.getFIELD_NAME_datetimeStart(), simpleDateFormat.format(p5.getDatetimeStart()))//
						.param(Promotion.field.getFIELD_NAME_datetimeEnd(), simpleDateFormat.format(p5.getDatetimeEnd()))//
						.param(Promotion.field.getFIELD_NAME_excecutionThreshold(), String.valueOf(p5.getExcecutionThreshold()))//
						.param(Promotion.field.getFIELD_NAME_excecutionAmount(), String.valueOf(p5.getExcecutionAmount()))//
						.param(Promotion.field.getFIELD_NAME_excecutionDiscount(), String.valueOf(p5.getExcecutionDiscount()))//
						.param(Promotion.field.getFIELD_NAME_scope(), String.valueOf(p5.getScope()))//
						.param(Promotion.field.getFIELD_NAME_shopScope(), String.valueOf(p5.getShopScope()))//
						.param(Promotion.field.getFIELD_NAME_staff(), String.valueOf(p5.getStaff()))//
						.param(Promotion.field.getFIELD_NAME_returnObject(), String.valueOf(p5.getReturnObject()))//
						.param(Promotion.field.getFIELD_NAME_commodityIDs(), p5.getCommodityIDs())//
						.param(Promotion.field.getFIELD_NAME_shopIDs(), p5.getShopIDs())//
		).andExpect(status().isOk()).andDo(print()).andReturn();
		//
		Shared.checkJSONErrorCode(mr5);
	}


	@Test
	public void testRetrieveNEx() throws Exception {
		Shared.printTestMethodStartInfo();

		System.out.println("-----------------------case1:???????????????D????????????-------------------------------");
		Promotion p = BasePromotionTest.DataInput.getPromotion();
		Map<String, Object> paramsForCreate = p.getCreateParam(BaseBO.INVALID_CASE_ID, p);

		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Promotion PromotionCreate = (Promotion) promotionMapper.create(paramsForCreate);

		p.setIgnoreIDInComparision(true);
		if (p.compareTo(PromotionCreate) != 0) {
			Assert.assertTrue(false, "???????????????????????????DB??????????????????");
		}

		MvcResult mr3 = mvc.perform(//
				get("/promotionSync/deleteEx.bx")//
						.param(Promotion.field.getFIELD_NAME_ID(), String.valueOf(PromotionCreate.getID()))//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) session)//
		)//
				.andExpect(status().isOk())//
				.andDo(print()).andReturn();
		Shared.checkJSONErrorCode(mr3);

		MvcResult mr = mvc.perform(get("/promotionSync/retrieveNEx.bx").contentType(MediaType.APPLICATION_JSON).session((MockHttpSession) Shared.getPosLoginSession(mvc, 1))) //
				.andExpect(status().isOk()).andDo(print()).andReturn();

		Shared.checkJSONErrorCode(mr);

		System.out.println("-----------------------case2:???????????????C????????????-------------------------------");
		p = new Promotion();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
		String l = simpleDateFormat.format(DatetimeUtil.getDays(new Date(), 3));

		MvcResult mr1 = mvc.perform(//
				post("/promotionSync/createEx.bx") //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) session) //
						.param(Promotion.field.getFIELD_NAME_name(), "?????????????????????10???1")//
						.param(Promotion.field.getFIELD_NAME_status(), String.valueOf(EnumStatusPromotion.ESP_Active.getIndex()))//
						.param(Promotion.field.getFIELD_NAME_type(), "0")//
						.param(Promotion.field.getFIELD_NAME_datetimeStart(), l)//
						.param(Promotion.field.getFIELD_NAME_datetimeEnd(), "3000/10/22")//
						.param(Promotion.field.getFIELD_NAME_excecutionThreshold(), "10")//
						.param(Promotion.field.getFIELD_NAME_excecutionAmount(), "1")//
						.param(Promotion.field.getFIELD_NAME_excecutionDiscount(), "0")//
						.param(Promotion.field.getFIELD_NAME_scope(), "0")//
						.param(Promotion.field.getFIELD_NAME_staff(), "1")//
						.param(Promotion.field.getFIELD_NAME_returnObject(), "1"))
				.andExpect(status().isOk()).andDo(print()).andReturn();

		Shared.checkJSONErrorCode(mr1);

		MvcResult mr4 = mvc.perform(get("/promotionSync/retrieveNEx.bx").contentType(MediaType.APPLICATION_JSON).session((MockHttpSession) Shared.getPosLoginSession(mvc, 1))) //
				.andExpect(status().isOk()).andDo(print()).andReturn();

		Shared.checkJSONErrorCode(mr4);
	}

	// @Test
	public void testUpdateEx() throws Exception {
		Shared.printTestMethodStartInfo();

		Promotion p = new Promotion();
		p.setID(1);
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
		String l = simpleDateFormat.format(DatetimeUtil.getDays(new Date(), 3));

		System.out.println("???????????????????????????" + SyncCacheManager.getCache(Shared.DBName_Test, EnumSyncCacheType.ESCT_PromotionSyncInfo).readN(false, false));
		MvcResult mr2 = mvc.perform(//
				post("/promotionSync/createEx.bx") //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) session) //
						.param(Promotion.field.getFIELD_NAME_ID(), String.valueOf(p.getID()))//
						.param(Promotion.field.getFIELD_NAME_name(), "?????????????????????10-1")//
						.param(Promotion.field.getFIELD_NAME_status(), String.valueOf(EnumStatusPromotion.ESP_Active.getIndex()))//
						.param(Promotion.field.getFIELD_NAME_type(), "0")//
						.param(Promotion.field.getFIELD_NAME_datetimeStart(), l)//
						.param(Promotion.field.getFIELD_NAME_datetimeEnd(), "9999/10/22")//
						.param(Promotion.field.getFIELD_NAME_excecutionThreshold(), "10")//
						.param(Promotion.field.getFIELD_NAME_excecutionAmount(), "1")//
						.param(Promotion.field.getFIELD_NAME_excecutionDiscount(), "9.5")//
						.param(Promotion.field.getFIELD_NAME_scope(), "1")//
						.param(Promotion.field.getFIELD_NAME_staff(), "1")//
						.param("commodityIds", new String[] { "1", "2", "3", "4" })//
						.param(Promotion.field.getFIELD_NAME_returnObject(), "1")//
		)//
				.andExpect(status().isOk()).andDo(print()).andReturn();

		Shared.checkJSONErrorCode(mr2);

		String json = mr2.getResponse().getContentAsString();
		JSONObject o = JSONObject.fromObject(json);
		int id = JsonPath.read(o, "$.object.ID");

		MvcResult mr3 = mvc.perform(post("/promotionSync/updateEx.bx") //
				.contentType(MediaType.APPLICATION_JSON) //
				.session((MockHttpSession) session) //
				.param(Promotion.field.getFIELD_NAME_ID(), String.valueOf(id))//
				.param(Promotion.field.getFIELD_NAME_name(), "??????6")//
				.param(Promotion.field.getFIELD_NAME_name(), "?????????????????????10-1")//
				.param(Promotion.field.getFIELD_NAME_status(), String.valueOf(EnumStatusPromotion.ESP_Active.getIndex()))//
				.param(Promotion.field.getFIELD_NAME_type(), "0")//
				.param(Promotion.field.getFIELD_NAME_datetimeStart(), l)//
				.param(Promotion.field.getFIELD_NAME_datetimeEnd(), "9999/10/22")//
				.param(Promotion.field.getFIELD_NAME_excecutionThreshold(), "10")//
				.param(Promotion.field.getFIELD_NAME_excecutionAmount(), "1")//
				.param(Promotion.field.getFIELD_NAME_excecutionDiscount(), "9.5")//
				.param(Promotion.field.getFIELD_NAME_scope(), String.valueOf(EnumPromotionScope.EPS_SpecifiedCommodities.getIndex()))//
				.param(Promotion.field.getFIELD_NAME_staff(), "1")//
				.param("commodityIds", new String[] { "1", "2", "3", "4" })//
				.param(Promotion.field.getFIELD_NAME_returnObject(), "1")//
		).andExpect(status().isOk()).andDo(print()).andReturn();

		Shared.checkJSONErrorCode(mr3);
		System.out.println("update2?????????????????????" + SyncCacheManager.getCache(Shared.DBName_Test, EnumSyncCacheType.ESCT_PromotionSyncInfo).readN(false, false));
		// ???????????????????????????????????????
		Pos pos = (Pos) session.getAttribute(EnumSession.SESSION_POS.getName());
		int posID = pos.getID();
		verifySyncUpdateResult(mr3, p, posBO, promotionSyncCacheBO, promotionSyncCacheDispatcherBO, EnumCacheType.ECT_Promotion, esct, posID, Shared.DBName_Test);
		System.out.println("---------------------------case2:??????posID???????????????Action??????---------------------");
		// MvcResult mr2 = mvc.perform(post("/promotionSync/updateEx.bx") //
		// .contentType(MediaType.APPLICATION_JSON) //
		// .param(b.getFIELD_NAME_ID(), "1")//
		// .param(b.getFIELD_NAME_name(), "??????")//
		// ).andExpect(status().isOk()).andDo(print()).andReturn();
		//
		// Shared.checkJSONErrorCode(mr2, EnumErrorCode.EC_OtherError);
	}

	@Test
	public void testFeedbackEx() throws Exception {
		Shared.printTestMethodStartInfo();

		// ???pos1???????????????????????????
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
		String l = simpleDateFormat.format(DatetimeUtil.getDays(new Date(), 3));

		MvcResult mr = mvc.perform(//
				post("/promotionSync/createEx.bx") //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) session1) //
						.param(Promotion.field.getFIELD_NAME_name(), "?????????????????????10???1")//
						.param(Promotion.field.getFIELD_NAME_status(), String.valueOf(EnumStatusPromotion.ESP_Active.getIndex()))//
						.param(Promotion.field.getFIELD_NAME_type(), String.valueOf(EnumTypePromotion.ETP_DecreaseOnAmount.getIndex()))//
						.param(Promotion.field.getFIELD_NAME_datetimeStart(), l)//
						.param(Promotion.field.getFIELD_NAME_datetimeEnd(), "3000/10/22")//
						.param(Promotion.field.getFIELD_NAME_excecutionThreshold(), "10")//
						.param(Promotion.field.getFIELD_NAME_excecutionAmount(), "1")//
						.param(Promotion.field.getFIELD_NAME_excecutionDiscount(), "-1")//
						.param(Promotion.field.getFIELD_NAME_scope(), String.valueOf(EnumPromotionScope.EPS_SpecifiedCommodities.getIndex()))//
						.param(Promotion.field.getFIELD_NAME_staff(), "1")//
						.param(Promotion.field.getFIELD_NAME_returnObject(), String.valueOf(1))//
						.param(Promotion.field.getFIELD_NAME_commodityIDs(), "1,2")//
		).andExpect(status().isOk()).andDo(print()).andReturn();
		// ??????????????????????????????
		Shared.checkJSONErrorCode(mr);
		// ?????????
		Pos pos = (Pos) session.getAttribute(EnumSession.SESSION_POS.getName());
		int posID = pos.getID();
		Promotion pt = new Promotion();
		pt.setName("?????????????????????10???1");
		pt.setStatus(EnumStatusPromotion.ESP_Active.getIndex());
		pt.setType(EnumTypePromotion.ETP_DecreaseOnAmount.getIndex());
		Date dtStart = simpleDateFormat.parse(l);
		pt.setDatetimeStart(dtStart);
		Date dtEnd = simpleDateFormat.parse("3000/10/22");
		pt.setDatetimeEnd(dtEnd);
		pt.setExcecutionThreshold(10);
		pt.setExcecutionAmount(1);
		pt.setExcecutionDiscount(-1);
		pt.setScope(EnumPromotionScope.EPS_SpecifiedCommodities.getIndex());
		pt.setStaff(1);
		//
		PromotionCP.verifyCreate(mr, pt, Shared.DBName_Test, posBO, EnumSyncCacheType.ESCT_PromotionSyncInfo, promotionSyncCacheBO, promotionSyncCacheDispatcherBO, posID, promotionScopeMapper);

		// ???pos2???retrieveN
		MvcResult mr2 = mvc.perform(get("/promotionSync/retrieveNEx.bx").contentType(MediaType.APPLICATION_JSON).session((MockHttpSession) session)) //
				.andExpect(status().isOk()).andDo(print()).andReturn();

		Shared.checkJSONErrorCode(mr2);
		JSONObject json = JSONObject.fromObject(mr2.getResponse().getContentAsString());
		json.getString(BaseAction.KEY_ObjectList);
		List<BaseModel> bmList = (List<BaseModel>) pt.parseN(json.getString(BaseAction.KEY_ObjectList));
		// ????????????????????????????????????????????????
		List<Promotion> promotionList = BasePromotionTest.getPromotionListIfAllSync(mapBO, bmList);
		//
		String ids = "";
		for (Promotion promotion : promotionList) {
			ids += promotion.getID() + ",";
		}
		//
		MvcResult mr3 = mvc.perform(get("/promotionSync/feedbackEx.bx?sID=" + ids + "&errorCode=EC_NoError") //
				.contentType(MediaType.APPLICATION_JSON) //
				.session((MockHttpSession) session)) //
				.andExpect(status().isOk()).andDo(print()).andReturn();
		Shared.checkJSONErrorCode(mr3);
		// ?????????
		pos = (Pos) session.getAttribute(EnumSession.SESSION_POS.getName());
		posID = pos.getID();
		PromotionCP.verifySyncPromotion(promotionList, posID, posBO, promotionSyncCacheDispatcherBO, Shared.DBName_Test);
	}

	@Test
	public void testDeleteEx1() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE1:????????????");
		Promotion p = BasePromotionTest.DataInput.getPromotion();
		Map<String, Object> paramsForCreate = p.getCreateParam(BaseBO.INVALID_CASE_ID, p);

		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Promotion PromotionCreate = (Promotion) promotionMapper.create(paramsForCreate);

		p.setIgnoreIDInComparision(true);
		if (p.compareTo(PromotionCreate) != 0) {
			Assert.assertTrue(false, "???????????????????????????DB??????????????????");
		}

		MvcResult mr = mvc.perform(//
				get("/promotionSync/deleteEx.bx")//
						.param(Promotion.field.getFIELD_NAME_ID(), String.valueOf(PromotionCreate.getID()))//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) session)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr);
		// ???????????????????????????????????????????????????
		// ErrorInfo ecOut = new ErrorInfo();
		// Promotion bm = (Promotion) CacheManager.getCache(Shared.DBName_Test,
		// EnumCacheType.ECT_Promotion).read1(PromotionCreate.getID(), BaseBO.SYSTEM,
		// ecOut,
		// Shared.DBName_Test);
		// Assert.assertTrue(bm.getStatus() ==
		// Promotion.EnumStatusPromotion.ESP_Deleted.getIndex(), "???????????????????????????????????????");
		Pos pos = (Pos) session.getAttribute(EnumSession.SESSION_POS.getName());
		int posID = pos.getID();
		//
		// verifySyncDeleteSuccessResult(PromotionCreate, posBO, promotionSyncCacheBO,
		// promotionSyncCacheDispatcherBO, esct, posID, Shared.DBName_Test);
		// ?????????
		PromotionCP.verifyDelete(PromotionCreate, Shared.DBName_Test, promotionMapper, posBO, EnumSyncCacheType.ESCT_PromotionSyncInfo, promotionSyncCacheBO, promotionSyncCacheDispatcherBO, posID);
	}

	@Test
	public void testDeleteEx2() throws Exception {
		Shared.printTestMethodStartInfo();

		Promotion p = BasePromotionTest.DataInput.getPromotion();
		Shared.caseLog("CASE2:?????????POSID??????DB??????");
		Map<String, Object> paramsForCreate2 = p.getCreateParam(BaseBO.INVALID_CASE_ID, p);

		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Promotion PromotionCreate2 = (Promotion) promotionMapper.create(paramsForCreate2);

		p.setIgnoreIDInComparision(true);
		if (p.compareTo(PromotionCreate2) != 0) {
			Assert.assertTrue(false, "???????????????????????????DB??????????????????");
		}

		try {
			mvc.perform(//
					get("/promotionSync/deleteEx.bx")//
							.param(Promotion.field.getFIELD_NAME_ID(), PromotionCreate2.getID() + "")//
							.contentType(MediaType.APPLICATION_JSON)//
			)//
					.andExpect(status().isOk())//
					.andDo(print()).andReturn();
		} catch (Exception e) {
			System.out.println(e.getMessage());// ??????posID???????????????
			// ???????????????????????????SyncAction???req
			// get???session???????????????dbName?????????dbName????????????BaseAction??????????????????????????????????????????????????????????????????????????????????????????
			// assertTrue(e.getMessage().equals("Request processing failed; nested exception
			// is java.lang.RuntimeException: ?????????????????????"));
		}

	}

	@Test
	public void testDeleteEx3() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE3:??????????????????????????????????????????????????????");
		Promotion p = BasePromotionTest.DataInput.getPromotion();
		Promotion pCreate = BasePromotionTest.createPromotionViaMapper(p);
		//
		RetailTradePromoting retailTradePromoting = BasePromotionTest.createRetailTradePromotingViaMapper();
		// ??????????????????
		RetailTradePromotingFlow rtpf = BasePromotionTest.DataInput.getRetailTradePromotingFlow();
		rtpf.setRetailTradePromotingID(retailTradePromoting.getID());
		rtpf.setPromotionID(pCreate.getID());
		BasePromotionTest.createRetailTradePromotingFlowViaMapper(rtpf);
		MvcResult mr3 = mvc.perform(//
				get("/promotionSync/deleteEx.bx")//
						.param(Promotion.field.getFIELD_NAME_ID(), String.valueOf(pCreate.getID()))//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) session)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr3);
		// ???????????????????????????????????????????????????
		ErrorInfo ecOut3 = new ErrorInfo();
		Promotion bm3 = (Promotion) CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_Promotion).read1(pCreate.getID(), BaseBO.SYSTEM, ecOut3, Shared.DBName_Test);
		Assert.assertTrue(bm3 == null, "???????????????????????????????????????");
		Pos pos3 = (Pos) session.getAttribute(EnumSession.SESSION_POS.getName());
		int posID3 = pos3.getID();
		verifySyncDeleteSuccessResult(pCreate, posBO, promotionSyncCacheBO, promotionSyncCacheDispatcherBO, esct, posID3, Shared.DBName_Test);

		// ?????????
		PromotionCP.verifyDelete(pCreate, Shared.DBName_Test, promotionMapper, posBO, EnumSyncCacheType.ESCT_PromotionSyncInfo, promotionSyncCacheBO, promotionSyncCacheDispatcherBO, posID3);

	}

	@Test
	public void testDeleteEx4() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE4:????????????????????????????????????????????????????????????");
		// ????????????
		Promotion p4 = BasePromotionTest.DataInput.getPromotion();
		Map<String, Object> pParamsCreate4 = p4.getCreateParam(BaseBO.INVALID_CASE_ID, p4);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Promotion pCreate4 = (Promotion) promotionMapper.create(pParamsCreate4);
		// ????????????
		Commodity c4 = BaseCommodityTest.DataInput.getCommodity(CommodityType.EnumCommodityType.ECT_Normal.getIndex());
		Map<String, Object> cParamsCreate4 = c4.getCreateParamEx(BaseBO.INVALID_CASE_ID, c4);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<List<BaseModel>> bmList = commodityMapper.createSimpleEx(cParamsCreate4);
		Commodity cCreated4 = (Commodity) bmList.get(0).get(0);
		// ??????????????????
		PromotionScope ps4 = new PromotionScope();
		ps4.setCommodityID(cCreated4.getID());
		ps4.setPromotionID(pCreate4.getID());
		Map<String, Object> psParamsCreate4 = ps4.getCreateParam(BaseBO.INVALID_CASE_ID, ps4);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		PromotionScope psCreate4 = (PromotionScope) promotionScopeMapper.create(psParamsCreate4);
		//
		MvcResult mr4 = mvc.perform(//
				get("/promotionSync/deleteEx.bx")//
						.param(Promotion.field.getFIELD_NAME_ID(), String.valueOf(pCreate4.getID()))//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) session)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr4);
		// ???????????????????????????????????????????????????
		ErrorInfo ecOut4 = new ErrorInfo();
		Promotion bm4 = (Promotion) CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_Promotion).read1(pCreate4.getID(), BaseBO.SYSTEM, ecOut4, Shared.DBName_Test);
		Assert.assertTrue(bm4 == null, "???????????????????????????????????????");
		Pos pos4 = (Pos) session.getAttribute(EnumSession.SESSION_POS.getName());
		int posID4 = pos4.getID();
		//
		verifySyncDeleteSuccessResult(pCreate4, posBO, promotionSyncCacheBO, promotionSyncCacheDispatcherBO, esct, posID4, Shared.DBName_Test);
		// ??????????????????????????????:?????????????????????
		Map<String, Object> psParamsRN4 = psCreate4.getRetrieveNParam(BaseBO.INVALID_CASE_ID, psCreate4);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> list4 = promotionScopeMapper.retrieveN(psParamsRN4);
		//
		Assert.assertTrue(list4.size() > 0//
				&& EnumErrorCode.values()[Integer.parseInt(psParamsRN4.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, //
				psParamsRN4.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());

		// ?????????
		PromotionCP.verifyDelete(pCreate4, Shared.DBName_Test, promotionMapper, posBO, EnumSyncCacheType.ESCT_PromotionSyncInfo, promotionSyncCacheBO, promotionSyncCacheDispatcherBO, posID4);
	}

	@Test
	public void testDeleteEx5() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE5:??????????????????");
		Promotion p = BasePromotionTest.DataInput.getPromotion();
		Map<String, Object> paramsForCreate = p.getCreateParam(BaseBO.INVALID_CASE_ID, p);

		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Promotion PromotionCreate = (Promotion) promotionMapper.create(paramsForCreate);

		p.setIgnoreIDInComparision(true);
		if (p.compareTo(PromotionCreate) != 0) {
			Assert.assertTrue(false, "???????????????????????????DB??????????????????");
		}

		MvcResult mr = mvc.perform(//
				get("/promotionSync/deleteEx.bx")//
						.param(Promotion.field.getFIELD_NAME_ID(), String.valueOf(PromotionCreate.getID()))//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) session)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr);
		// ???????????????????????????????????????????????????
		ErrorInfo ecOut = new ErrorInfo();
		Promotion bm = (Promotion) CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_Promotion).read1(PromotionCreate.getID(), BaseBO.SYSTEM, ecOut, Shared.DBName_Test);
		Assert.assertTrue(bm == null, "???????????????????????????????????????");
		Pos pos = (Pos) session.getAttribute(EnumSession.SESSION_POS.getName());
		int posID = pos.getID();

		verifySyncDeleteSuccessResult(PromotionCreate, posBO, promotionSyncCacheBO, promotionSyncCacheDispatcherBO, esct, posID, Shared.DBName_Test);

		// ?????????
		PromotionCP.verifyDelete(PromotionCreate, Shared.DBName_Test, promotionMapper, posBO, EnumSyncCacheType.ESCT_PromotionSyncInfo, promotionSyncCacheBO, promotionSyncCacheDispatcherBO, posID);

		MvcResult mr5 = mvc.perform(//
				get("/promotionSync/deleteEx.bx")//
						.param(Promotion.field.getFIELD_NAME_ID(), String.valueOf(PromotionCreate.getID()))//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) session)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr5, EnumErrorCode.EC_BusinessLogicNotDefined);
	}
}
