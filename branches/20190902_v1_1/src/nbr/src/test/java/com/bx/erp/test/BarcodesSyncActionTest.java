package com.bx.erp.test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MvcResult;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import com.bx.erp.action.BaseAction;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.action.trade.promotion.BasePromotion.EnumPromotionScope;
import com.bx.erp.cache.CacheManager;
import com.bx.erp.cache.config.ConfigCacheSizeCache.EnumConfigCacheSizeCache;
import com.bx.erp.model.Barcodes;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.ReturnCommoditySheet;
import com.bx.erp.model.ReturnCommoditySheetCommodity;
import com.bx.erp.model.BaseModel.EnumBoolean;
import com.bx.erp.model.CacheType.EnumCacheType;
import com.bx.erp.model.CommodityType;
import com.bx.erp.model.CommodityType.EnumCommodityType;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.commodity.Commodity;
import com.bx.erp.model.config.ConfigCacheSize;
import com.bx.erp.model.purchasing.PurchasingOrder;
import com.bx.erp.model.purchasing.PurchasingOrderCommodity;
import com.bx.erp.model.trade.Promotion;
import com.bx.erp.model.trade.PromotionScope;
import com.bx.erp.model.trade.Promotion.EnumStatusPromotion;
import com.bx.erp.model.trade.Promotion.EnumTypePromotion;
import com.bx.erp.util.DataSourceContextHolder;
import com.jayway.jsonpath.JsonPath;

import net.sf.json.JSONObject;

@WebAppConfiguration
public class BarcodesSyncActionTest extends BaseActionTest {

	@BeforeClass
	public void setup() {
		super.setUp();

		// 由于商品的测试数据量超过缓存限制，所以在这里把缓存限制设置成最大，然后再在tear down设置回来。
		ConfigCacheSize ccs = new ConfigCacheSize();
		ccs.setValue(String.valueOf(BaseAction.PAGE_SIZE_Infinite));
		ccs.setID(EnumConfigCacheSizeCache.ECC_CommodityCacheSize.getIndex());
		ccs.setName(EnumConfigCacheSizeCache.ECC_CommodityCacheSize.getName());
		CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_ConfigCacheSize).write1(ccs, Shared.DBName_Test, STAFF_ID4);
	}

	@AfterClass
	public void tearDown() {
		super.tearDown();

		// 由于商品的测试数据量超过缓存限制，在setup把缓存限制改了，所以在这里把缓存限制设置回来。
		ConfigCacheSize ccs = new ConfigCacheSize();
		ccs.setValue("100");
		ccs.setID(EnumConfigCacheSizeCache.ECC_CommodityCacheSize.getIndex());
		ccs.setName(EnumConfigCacheSizeCache.ECC_CommodityCacheSize.getName());
		CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_ConfigCacheSize).write1(ccs, Shared.DBName_Test, STAFF_ID4);
	}

	@Test
	public void testCreateExCase1() throws Exception {
		Shared.printTestMethodStartInfo();

		Commodity commodity = BaseCommodityTest.DataInput.getCommodity(CommodityType.EnumCommodityType.ECT_Normal.getIndex());
		Commodity c = BaseCommodityTest.createCommodityViaAction(commodity, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		//
		Barcodes b = BaseBarcodesTest.DataInput.getBarcodes(c.getID());
		b.setOperatorStaffID(1);
		b.setReturnObject(1);

		BaseBarcodesTest.createBarcodesViaAction(b, mvc, sessionBoss, mapBO, Shared.DBName_Test);
	}

	@Test
	public void testCreateExCase2() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case2:缺失posID参数创建");
		Commodity tmpCommodity = BaseCommodityTest.DataInput.getCommodity(CommodityType.EnumCommodityType.ECT_Normal.getIndex());
		Commodity c2 = BaseCommodityTest.createCommodityViaAction(tmpCommodity, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		MvcResult mr2 = mvc.perform(//
				post("/barcodesSync/createEx.bx") //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss)) //
						.param(Barcodes.field.getFIELD_NAME_barcode(), String.valueOf(System.currentTimeMillis()))//
						.param(Barcodes.field.getFIELD_NAME_commodityID(), String.valueOf(c2.getID()))//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		Shared.checkJSONErrorCode(mr2);
	}

	@Test
	public void testCreateEx2() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case2: 组合商品再次创建一个条形码");
		Commodity c = BaseCommodityTest.DataInput.getCommodity(EnumCommodityType.ECT_Combination.getIndex());
		String json = JSONObject.fromObject(c).toString();
		c.setSubCommodityInfo(json);
		Commodity createcCommodity = BaseCommodityTest.createCommodityViaAction(c, mvc, sessionBoss, mapBO, Shared.DBName_Test);

		Barcodes barcodesB = BaseBarcodesTest.DataInput.getBarcodes(createcCommodity.getID());
		barcodesB.setOperatorStaffID(1);
		//
		MvcResult mr = mvc.perform(//
				post("/barcodesSync/createEx.bx") //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionBoss) //
						.param(Barcodes.field.getFIELD_NAME_barcode(), barcodesB.getBarcode())//
						.param(Barcodes.field.getFIELD_NAME_commodityID(), String.valueOf(createcCommodity.getID()))//
						.param(Barcodes.field.getFIELD_NAME_operatorStaffID(), String.valueOf(barcodesB.getOperatorStaffID()))//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		// 结果验证：检查错误码
		Assert.assertTrue("".equals(mr.getResponse().getContentAsString()), "组合商品创建多个条形码时不返回空");
	}

	@Test
	public void testCreateEx3() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case3: 多包装商品再次创建一个条形码");
		Commodity comm = BaseCommodityTest.DataInput.getCommodity();
		Commodity commCreated = BaseCommodityTest.createCommodityViaAction(comm, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		//
		Commodity comm2 = BaseCommodityTest.DataInput.getCommodity();
		comm2.setType(EnumCommodityType.ECT_MultiPackaging.getIndex());
		comm2.setRefCommodityID(commCreated.getID());
		comm2.setRefCommodityMultiple(2);
		comm2.setMultiPackagingInfo("123456789" + ";1;1;1;8;8;" + comm2.getBarcodes() + ";");
		//
		String error = comm2.checkCreate(BaseBO.CASE_Commodity_CreateMultiPackaging);
		Assert.assertTrue("".equals(error), error);
		//
		Map<String, Object> params = comm2.getCreateParamEx(BaseBO.CASE_Commodity_CreateMultiPackaging, comm2);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<List<BaseModel>> bmList = commodityMapper.createMultiPackagingEx(params);
		//
		Assert.assertTrue(bmList.size() > 0 && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		Commodity c = (Commodity) bmList.get(0).get(0);
		comm2.setIgnoreIDInComparision(true);
		if (comm2.compareTo(c) != 0) {
			Assert.assertTrue(false, "DB读出的对象和创建的不符合");
		}

		Barcodes barcodesB = BaseBarcodesTest.DataInput.getBarcodes(c.getID());
		barcodesB.setOperatorStaffID(1);
		//
		MvcResult mr = mvc.perform(//
				post("/barcodesSync/createEx.bx") //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionBoss) //
						.param(Barcodes.field.getFIELD_NAME_barcode(), barcodesB.getBarcode())//
						.param(Barcodes.field.getFIELD_NAME_commodityID(), String.valueOf(((BaseModel) bmList.get(0).get(0)).getID()))//
						.param(Barcodes.field.getFIELD_NAME_operatorStaffID(), String.valueOf(barcodesB.getOperatorStaffID()))//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		// 结果验证：检查错误码
		Assert.assertTrue("".equals(mr.getResponse().getContentAsString()), "组合商品创建多个条形码时不返回空");
	}

	@Test
	public void testCreateEx4() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case4: 服务商品再次创建一个条形码");
		Commodity comm = BaseCommodityTest.DataInput.getCommodity(EnumCommodityType.ECT_Service.getIndex());
		//
		Commodity commCreated = BaseCommodityTest.createCommodityViaAction(comm, mvc, sessionBoss, mapBO, Shared.DBName_Test);

		Barcodes barcodesB = BaseBarcodesTest.DataInput.getBarcodes(commCreated.getID());
		barcodesB.setOperatorStaffID(1);
		//
		MvcResult mr = mvc.perform(//
				post("/barcodesSync/createEx.bx") //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionBoss) //
						.param(Barcodes.field.getFIELD_NAME_barcode(), barcodesB.getBarcode())//
						.param(Barcodes.field.getFIELD_NAME_commodityID(), String.valueOf(commCreated.getID()))//
						.param(Barcodes.field.getFIELD_NAME_returnObject(), String.valueOf(barcodesB.getReturnObject()))//
						.param(Barcodes.field.getFIELD_NAME_operatorStaffID(), String.valueOf(barcodesB.getOperatorStaffID()))//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		// 结果验证：检查错误码
		Assert.assertTrue("".equals(mr.getResponse().getContentAsString()), "组合商品创建多个条形码时不返回空");
	}

	@Test
	public void testCreateEx5() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("普通商品多次创建条形码");
		Commodity commodity = BaseCommodityTest.DataInput.getCommodity(CommodityType.EnumCommodityType.ECT_Normal.getIndex());
		Commodity c = BaseCommodityTest.createCommodityViaAction(commodity, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		//
		Barcodes b = BaseBarcodesTest.DataInput.getBarcodes(c.getID());
		b.setOperatorStaffID(1);
		b.setReturnObject(1);
		BaseBarcodesTest.createBarcodesViaAction(b, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		//
		Thread.sleep(1000);
		b.setBarcode("12" + System.currentTimeMillis() % 1000000);
		BaseBarcodesTest.createBarcodesViaAction(b, mvc, sessionBoss, mapBO, Shared.DBName_Test);
	}

	@Test
	public void testCreateEx6() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("传入barcodeds长度为2进行创建");
		Commodity commodity = BaseCommodityTest.DataInput.getCommodity(CommodityType.EnumCommodityType.ECT_Normal.getIndex());
		Commodity c2 = BaseCommodityTest.createCommodityViaAction(commodity, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		MvcResult mr6 = mvc.perform(//
				post("/barcodesSync/createEx.bx") //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss)) //
						.param(Barcodes.field.getFIELD_NAME_barcode(), String.valueOf(System.currentTimeMillis() % 100))//
						.param(Barcodes.field.getFIELD_NAME_commodityID(), String.valueOf(c2.getID()))//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		Shared.checkJSONErrorCode(mr6, EnumErrorCode.EC_WrongFormatForInputField);
	}

	@Test
	public void testCreateEx7() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case7:同一商品重复创建条形码");
		Commodity commodity = BaseCommodityTest.DataInput.getCommodity(CommodityType.EnumCommodityType.ECT_Normal.getIndex());
		Commodity c = BaseCommodityTest.createCommodityViaAction(commodity, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		//
		Barcodes b = new Barcodes();
		b.setBarcode(Shared.generateStringByTime(8));
		b.setCommodityID(c.getID());
		b.setOperatorStaffID(STAFF_ID4);
		b.setReturnObject(EnumBoolean.EB_Yes.getIndex());
		// 创建条形码
		BaseBarcodesTest.createBarcodesViaAction(b, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		// 重复创建条形码
		MvcResult mr = mvc.perform(//
				post("/barcodesSync/createEx.bx") //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionBoss) //
						.param(Barcodes.field.getFIELD_NAME_barcode(), b.getBarcode())//
						.param(Barcodes.field.getFIELD_NAME_commodityID(), String.valueOf(c.getID()))//
						.param(Barcodes.field.getFIELD_NAME_returnObject(), String.valueOf(b.getReturnObject()))//
						.param(Barcodes.field.getFIELD_NAME_operatorStaffID(), String.valueOf(b.getOperatorStaffID()))//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		// 结果验证：检查错误码
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_Duplicated);
		Shared.checkJSONMsg(mr, "该条形码已存在", "testCreateEx7提示信息错误！");
	}

	@Test
	public void testRetrieveNEx() throws Exception {
		Shared.printTestMethodStartInfo();
		// 创建一个商品
		Commodity commodity = BaseCommodityTest.DataInput.getCommodity();
		commodity.setNO(Commodity.DEFAULT_VALUE_CommodityNO);
		Commodity commCreated = BaseCommodityTest.createCommodityViaAction(commodity, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		//
		Barcodes b = BaseBarcodesTest.DataInput.getBarcodes(commCreated.getID());
		b.setOperatorStaffID(STAFF_ID3);
		//
		Barcodes barcodesCreate = BaseBarcodesTest.createBarcodesViaMapper(b, BaseBO.INVALID_CASE_ID);

		BaseBarcodesTest.deleteBarcodesViaAction(barcodesCreate, mvc, sessionBoss, mapBO, Shared.DBName_Test);

		MvcResult mr = mvc.perform(//
				get("/barcodesSync/retrieveNEx.bx")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) Shared.getPosLoginSession(mvc, 3))//
		) //
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		Shared.checkJSONErrorCode(mr);
	}

	@Test
	public void testFeedbackEx() throws Exception {
		Shared.printTestMethodStartInfo();

		mvc.perform(//
				get("/barcodesSync/feedbackEx.bx?sID=86,87,88&errorCode=EC_NoError") //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionBoss)//
		) //
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
	}

	@Test
	public void testDeleteEx1() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case1:正常删除");
		Commodity commodity = BaseCommodityTest.DataInput.getCommodity(CommodityType.EnumCommodityType.ECT_Normal.getIndex());
		Commodity c = BaseCommodityTest.createCommodityViaAction(commodity, mvc, sessionBoss, mapBO, Shared.DBName_Test);

		Barcodes b = BaseBarcodesTest.DataInput.getBarcodes(c.getID());
		b.setOperatorStaffID(STAFF_ID3);
		Barcodes barcodesCreate = BaseBarcodesTest.createBarcodesViaMapper(b, BaseBO.INVALID_CASE_ID);

		BaseBarcodesTest.deleteBarcodesViaAction(barcodesCreate, mvc, sessionBoss, mapBO, Shared.DBName_Test);
	}

	@Test
	public void testDeleteEx2() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("缺少PosSession/POSID导致DB错误");
		Commodity commodity = BaseCommodityTest.DataInput.getCommodity(CommodityType.EnumCommodityType.ECT_Normal.getIndex());
		Commodity c = BaseCommodityTest.createCommodityViaAction(commodity, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		
		Barcodes b = BaseBarcodesTest.DataInput.getBarcodes(c.getID());
		b.setOperatorStaffID(STAFF_ID3);
		Barcodes barcodesCreate = BaseBarcodesTest.createBarcodesViaMapper(b, BaseBO.INVALID_CASE_ID);

		try {
			MvcResult result = mvc.perform(//
					get("/barcodesSync/deleteEx.bx")//
							.param(Barcodes.field.getFIELD_NAME_ID(), String.valueOf(barcodesCreate.getID()))//
							.contentType(MediaType.APPLICATION_JSON)//
			)//
					.andExpect(status().isOk())//
					.andDo(print())//
					.andReturn();
			Assert.assertTrue("".equals(result.getResponse().getContentAsString()), "没有session返回null");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testDeleteEx3() throws Exception {
		Shared.printTestMethodStartInfo();
		//
		String message = "删除的条形码在零售商品中有依赖";
		Shared.caseLog("case3:" + message);
		MvcResult mrl3 = mvc.perform(//
				get("/barcodesSync/deleteEx.bx") //
						.param(Barcodes.field.getFIELD_NAME_ID(), "1")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
		) //
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mrl3, EnumErrorCode.EC_BusinessLogicNotDefined);
		Shared.checkJSONMsg(mrl3, message, "错误信息不正确");
	}

	@Test
	public void testDeleteEx4() throws Exception {
		Shared.printTestMethodStartInfo();
		//
		String message = "删除的条形码在采购订单商品中有依赖";
		Shared.caseLog("case4:" + message);
		MvcResult mrl3 = mvc.perform(//
				get("/barcodesSync/deleteEx.bx") //
						.param(Barcodes.field.getFIELD_NAME_ID(), "101")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
		) //
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mrl3, EnumErrorCode.EC_BusinessLogicNotDefined);
		Shared.checkJSONMsg(mrl3, message, "错误信息不正确");
	}

	@Test
	public void testDeleteEx5() throws Exception {
		Shared.printTestMethodStartInfo();
		//
		String message = "删除的条形码在入库单商品中有依赖";
		Shared.caseLog("case5:" + message);
		MvcResult mrl3 = mvc.perform(//
				get("/barcodesSync/deleteEx.bx") //
						.param(Barcodes.field.getFIELD_NAME_ID(), "102")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
		) //
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mrl3, EnumErrorCode.EC_BusinessLogicNotDefined);
		Shared.checkJSONMsg(mrl3, message, "错误信息不正确");
	}

	@Test
	public void testDeleteEx6() throws Exception {
		Shared.printTestMethodStartInfo();
		//
		String message = "删除的条形码在盘点单商品中有依赖";
		Shared.caseLog("case4:" + message);
		MvcResult mrl3 = mvc.perform(//
				get("/barcodesSync/deleteEx.bx") //
						.param(Barcodes.field.getFIELD_NAME_ID(), "104")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
		) //
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mrl3, EnumErrorCode.EC_BusinessLogicNotDefined);
		Shared.checkJSONMsg(mrl3, message, "错误信息不正确");
	}

	@Test
	public void testDeleteEx7() throws Exception {
		Shared.printTestMethodStartInfo();
		//
		String message = "删除的条形码在零售商品中有依赖";

		Shared.caseLog("case7:如果商品在入库商品中有依赖，那么它的条形码都是不能够删除的");
		Barcodes barcodes = BaseBarcodesTest.DataInput.getBarcodes(1);
		barcodes = BaseBarcodesTest.createBarcodesViaMapper(barcodes, BaseBO.INVALID_CASE_ID);
		MvcResult mrl3 = mvc.perform(//
				get("/barcodesSync/deleteEx.bx") //
						.param(Barcodes.field.getFIELD_NAME_ID(), String.valueOf(barcodes.getID()))//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
		) //
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mrl3, EnumErrorCode.EC_BusinessLogicNotDefined);
		Shared.checkJSONMsg(mrl3, message, "错误信息不正确");
	}

	@Test
	public void testDeleteEx8() throws Exception {
		Shared.printTestMethodStartInfo();
		//
		String message = "删除的条形码在采购订单商品中有依赖";
		Shared.caseLog("case8:如果商品在采购订单商品中有依赖，那么它的条形码都是不能够删除的");
		Barcodes barcodes = BaseBarcodesTest.DataInput.getBarcodes(88);
		barcodes = BaseBarcodesTest.createBarcodesViaMapper(barcodes, BaseBO.INVALID_CASE_ID);
		MvcResult mrl3 = mvc.perform(//
				get("/barcodesSync/deleteEx.bx") //
						.param(Barcodes.field.getFIELD_NAME_ID(), String.valueOf(barcodes.getID()))//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
		) //
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mrl3, EnumErrorCode.EC_BusinessLogicNotDefined);
		Shared.checkJSONMsg(mrl3, message, "错误信息不正确");
	}

	@Test
	public void testDeleteEx9() throws Exception {
		Shared.printTestMethodStartInfo();
		//
		String message = "删除的条形码在入库单商品中有依赖";
		Barcodes barcodes = BaseBarcodesTest.DataInput.getBarcodes(89);
		barcodes = BaseBarcodesTest.createBarcodesViaMapper(barcodes, BaseBO.INVALID_CASE_ID);
		Shared.caseLog("case9:如果商品在入库单商品中有依赖，那么它的条形码都是不能够删除的");
		MvcResult mrl3 = mvc.perform(//
				get("/barcodesSync/deleteEx.bx") //
						.param(Barcodes.field.getFIELD_NAME_ID(), String.valueOf(barcodes.getID()))//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
		) //
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mrl3, EnumErrorCode.EC_BusinessLogicNotDefined);
		Shared.checkJSONMsg(mrl3, message, "错误信息不正确");
	}

	@Test
	public void testDeleteEx10() throws Exception {
		Shared.printTestMethodStartInfo();
		//
		String message = "删除的条形码在盘点单商品中有依赖";
		Barcodes barcodes = BaseBarcodesTest.DataInput.getBarcodes(91);
		barcodes = BaseBarcodesTest.createBarcodesViaMapper(barcodes, BaseBO.INVALID_CASE_ID);
		Shared.caseLog("case10:如果商品在盘点商品中有依赖，那么它的条形码都是不能够删除的");
		MvcResult mrl3 = mvc.perform(//
				get("/barcodesSync/deleteEx.bx") //
						.param(Barcodes.field.getFIELD_NAME_ID(), String.valueOf(barcodes.getID()))//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
		) //
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mrl3, EnumErrorCode.EC_BusinessLogicNotDefined);
		Shared.checkJSONMsg(mrl3, message, "错误信息不正确");
	}

	@Test
	public void testDeleteEx11() throws Exception {
		Shared.printTestMethodStartInfo();
		//
		String message = "删除的条形码在退货单商品中有依赖";
		Shared.caseLog("case11:如果商品在退货单商品中有依赖，那么它的条形码都是不能够删除的");
		// 创建一个商品
		Commodity commodity = BaseCommodityTest.DataInput.getCommodity();
		commodity.setNO(Commodity.DEFAULT_VALUE_CommodityNO);
		Commodity commCreated = BaseCommodityTest.createCommodityViaAction(commodity, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		// 根据商品ID创建一个条形码t1
		Barcodes barcodes = BaseBarcodesTest.DataInput.getBarcodes(commCreated.getID());
		Barcodes barcode1 = BaseBarcodesTest.createBarcodesViaMapper(barcodes, BaseBO.INVALID_CASE_ID);
		// 创建退货单
		ReturnCommoditySheet rcsCreate = createReturnCommoditySheet();
		// 根据商品和条形码创建一个退货单商品
		ReturnCommoditySheetCommodity returnCommoditySheetCommodity = new ReturnCommoditySheetCommodity();
		returnCommoditySheetCommodity.setReturnCommoditySheetID(rcsCreate.getID());
		returnCommoditySheetCommodity.setCommodityID(commCreated.getID());
		returnCommoditySheetCommodity.setBarcodeID(barcode1.getID());
		returnCommoditySheetCommodity.setNO(50);
		returnCommoditySheetCommodity.setSpecification("箱");
		returnCommoditySheetCommodity.setPurchasingPrice(5.5d);
		//
		Map<String, Object> createIcCommparams = returnCommoditySheetCommodity.getCreateParam(BaseBO.INVALID_CASE_ID, returnCommoditySheetCommodity);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		ReturnCommoditySheetCommodity returnCommoditySheetCommodityCreated = (ReturnCommoditySheetCommodity) returnCommoditySheetCommodityMapper.create(createIcCommparams);
		Assert.assertTrue(returnCommoditySheetCommodityCreated != null && EnumErrorCode.values()[Integer.parseInt(createIcCommparams.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "创建对象失败");
		returnCommoditySheetCommodity.setIgnoreIDInComparision(true);
		if (returnCommoditySheetCommodity.compareTo(returnCommoditySheetCommodityCreated) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		// 根据商品ID创建一个条形码t2
		Barcodes barcodes2 = BaseBarcodesTest.DataInput.getBarcodes(commCreated.getID());
		barcodes2 = BaseBarcodesTest.createBarcodesViaMapper(barcodes2, BaseBO.INVALID_CASE_ID);

		MvcResult mrl3 = mvc.perform(//
				get("/barcodesSync/deleteEx.bx") //
						.param(Barcodes.field.getFIELD_NAME_ID(), String.valueOf(barcodes2.getID()))//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
		) //
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mrl3, EnumErrorCode.EC_BusinessLogicNotDefined);
		Shared.checkJSONMsg(mrl3, message, "错误信息不正确");
		// 删除测试的退货单商品
		Map<String, Object> deletepOrderCommparams = returnCommoditySheetCommodityCreated.getDeleteParam(BaseBO.INVALID_CASE_ID, returnCommoditySheetCommodityCreated);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		returnCommoditySheetCommodityMapper.delete(deletepOrderCommparams);
		Assert.assertEquals(EnumErrorCode.values()[Integer.parseInt(deletepOrderCommparams.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())], EnumErrorCode.EC_NoError);
		// 删除测试的商品(删除商品时会连它的所有条形码都删除掉)
		commCreated.setOperatorStaffID(STAFF_ID3);
		BaseCommodityTest.deleteCommodityViaAction(commCreated, Shared.DBName_Test, mvc, sessionBoss, mapBO);
	}

	@Test
	public void testDeleteEx12() throws Exception {
		Shared.printTestMethodStartInfo();
		//
		String message = "删除的条形码在指定促销范围中有依赖";
		Shared.caseLog("case12:如果商品在指定促销范围中有依赖，那么它的条形码都是不能够删除的");
		// 创建商品
		Commodity c = BaseCommodityTest.DataInput.getCommodity();
		c.setPurchaseFlag(1);
		Commodity cCreate = BaseCommodityTest.createCommodityViaAction(c, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		// 创建促销范围
		PromotionScope ps = new PromotionScope();
		ps.setCommodityID(cCreate.getID());
		ps.setPromotionID(2);
		createPromotionScope(ps);
		//
		Barcodes barcode1 = BaseBarcodesTest.DataInput.getBarcodes(cCreate.getID());
		barcode1 = BaseBarcodesTest.createBarcodesViaMapper(barcode1, BaseBO.INVALID_CASE_ID);

		MvcResult mrl3 = mvc.perform(//
				get("/barcodesSync/deleteEx.bx") //
						.param(Barcodes.field.getFIELD_NAME_ID(), String.valueOf(barcode1.getID()))//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
		) //
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mrl3, EnumErrorCode.EC_BusinessLogicNotDefined);
		Shared.checkJSONMsg(mrl3, message, "错误信息不正确");
	}

	@Test
	public void testDeleteEx13() throws Exception {
		Shared.printTestMethodStartInfo();
		//
		Shared.caseLog("case13:如果商品在指定促销范围中有依赖，但是促销已经结束了，那么它的条形码还是不能够删除的");
		// 创建商品
		Commodity c = BaseCommodityTest.DataInput.getCommodity();
		c.setPurchaseFlag(1);
		Commodity cCreate = BaseCommodityTest.createCommodityViaAction(c, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		// 创建促销范围
		PromotionScope ps = new PromotionScope();
		ps.setCommodityID(cCreate.getID());
		ps.setPromotionID(4);
		createPromotionScope(ps);
		//
		Barcodes barcode1 = BaseBarcodesTest.DataInput.getBarcodes(cCreate.getID());
		barcode1 = BaseBarcodesTest.createBarcodesViaMapper(barcode1, BaseBO.INVALID_CASE_ID);

		MvcResult mrl3 = mvc.perform(//
				get("/barcodesSync/deleteEx.bx") //
						.param(Barcodes.field.getFIELD_NAME_ID(), String.valueOf(barcode1.getID()))//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
		) //
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mrl3, EnumErrorCode.EC_BusinessLogicNotDefined);
	}

	@Test
	public void testDeleteEx14() throws Exception {
		Shared.printTestMethodStartInfo();
		//
		Shared.caseLog("case14:如果商品在指定促销范围中有依赖，但是促销已经删除了，那么它的条形码是不能够删除的");
		// 创建商品
		Commodity c = BaseCommodityTest.DataInput.getCommodity();
		c.setPurchaseFlag(1);
		Commodity cCreate = BaseCommodityTest.createCommodityViaAction(c, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		// 创建促销
		Date date = new Date();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
		String dateTime = simpleDateFormat.format(date.getTime() + ((long) 3 * 24 * 60 * 60 * 1000));
		Promotion p = createExPromotion(dateTime);
		// 创建促销范围
		PromotionScope ps = new PromotionScope();
		ps.setCommodityID(cCreate.getID());
		ps.setPromotionID(p.getID());
		createPromotionScope(ps);
		//
		Barcodes barcode1 = BaseBarcodesTest.DataInput.getBarcodes(cCreate.getID());
		barcode1 = BaseBarcodesTest.createBarcodesViaMapper(barcode1, BaseBO.INVALID_CASE_ID);
		// 删除促销
		deleteExPromotionSync(p);

		MvcResult mrl3 = mvc.perform(//
				get("/barcodesSync/deleteEx.bx") //
						.param(Barcodes.field.getFIELD_NAME_ID(), String.valueOf(barcode1.getID()))//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
		) //
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mrl3, EnumErrorCode.EC_BusinessLogicNotDefined);
	}

	@Test
	public void testDeleteEx15() throws Exception {
		Shared.printTestMethodStartInfo();
		//
		Shared.caseLog("case15:如果商品参与了全场促销，商品又没有其他依赖，那么它的条形码是能够删除的");
		// 创建商品
		Commodity c = BaseCommodityTest.DataInput.getCommodity();
		c.setPurchaseFlag(1);
		Commodity cCreate = BaseCommodityTest.createCommodityViaAction(c, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		// 创建促销
		Promotion p = BasePromotionTest.DataInput.getPromotion();
		p.setType(EnumTypePromotion.ETP_DecreaseOnAmount.getIndex());
		p.setExcecutionDiscount(-1);
		p.setScope(0);
		createPromotion(p);
		//
		Barcodes barcode1 = BaseBarcodesTest.DataInput.getBarcodes(cCreate.getID());
		barcode1 = BaseBarcodesTest.createBarcodesViaMapper(barcode1, BaseBO.INVALID_CASE_ID);
		
		MvcResult mrl3 = mvc.perform(//
				get("/barcodesSync/deleteEx.bx") //
						.param(Barcodes.field.getFIELD_NAME_ID(), String.valueOf(barcode1.getID()))//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
		) //
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mrl3);
	}

	@Test
	public void testDeleteEx16() throws Exception {
		Shared.printTestMethodStartInfo();
		//
		Shared.caseLog("case16:如果商品参与了全场促销，促销已经被删除了，商品又没有其他依赖，那么它的条形码是能够删除的");
		// 创建商品
		Commodity c = BaseCommodityTest.DataInput.getCommodity();
		c.setPurchaseFlag(1);
		Commodity cCreate = BaseCommodityTest.createCommodityViaAction(c, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		// 创建促销
		Promotion p = BasePromotionTest.DataInput.getPromotion();
		p.setType(EnumTypePromotion.ETP_DecreaseOnAmount.getIndex());
		p.setExcecutionDiscount(-1);
		p.setScope(0);
		Promotion promotionCreated = createPromotion(p);
		//
		Barcodes barcode1 = BaseBarcodesTest.DataInput.getBarcodes(cCreate.getID());
		barcode1 = BaseBarcodesTest.createBarcodesViaMapper(barcode1, BaseBO.INVALID_CASE_ID);
		
		// 删除促销
		deleteExPromotionSync(promotionCreated);

		MvcResult mrl3 = mvc.perform(//
				get("/barcodesSync/deleteEx.bx") //
						.param(Barcodes.field.getFIELD_NAME_ID(), String.valueOf(barcode1.getID()))//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
		) //
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mrl3);
	}

	@Test
	public void testDeleteEx17() throws Exception {
		Shared.printTestMethodStartInfo();
		//
		Shared.caseLog("case17:如果商品在采购订单商品中有依赖，采购订单被删除了，商品无其他依赖，那么它的条形码是能够删除至一个的");
		// 创建一个采购订单
		PurchasingOrder po = BasePurchasingOrderTest.DataInput.getPurchasingOrder();
		PurchasingOrder purchasingOrder = purchasingOrderCreate(po);
		// 创建一个商品
		Commodity commodity = BaseCommodityTest.DataInput.getCommodity();
		Commodity commCreated = BaseCommodityTest.createCommodityViaAction(commodity, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		// 根据商品ID创建一个条形码t1
		Barcodes barcode1 = BaseBarcodesTest.DataInput.getBarcodes(commCreated.getID());
		barcode1 = BaseBarcodesTest.createBarcodesViaMapper(barcode1, BaseBO.INVALID_CASE_ID);
		// 根据商品和条形码创建一个采购订单商品
		PurchasingOrderCommodity pOrderComm = new PurchasingOrderCommodity();
		pOrderComm.setCommodityID(commCreated.getID());
		pOrderComm.setPurchasingOrderID(purchasingOrder.getID());
		pOrderComm.setCommodityNO(Math.abs(new Random().nextInt(300)));
		pOrderComm.setPriceSuggestion(1);
		pOrderComm.setBarcodeID(barcode1.getID());
		//
		Map<String, Object> createpOrderCommparams = pOrderComm.getCreateParam(BaseBO.INVALID_CASE_ID, pOrderComm);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		PurchasingOrderCommodity pocCreate = (PurchasingOrderCommodity) purchasingOrderCommodityMapper.create(createpOrderCommparams);
		Assert.assertTrue(pocCreate != null && EnumErrorCode.values()[Integer.parseInt(createpOrderCommparams.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "创建对象失败");
		pOrderComm.setIgnoreIDInComparision(true);
		if (pOrderComm.compareTo(pocCreate) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		// 删除该采购订单，会把从表信息删除掉
		deleteAndVerificationPurchasingOrder(purchasingOrder);
		// 删除条形码，删除成功
		MvcResult mrl3 = mvc.perform(//
				get("/barcodesSync/deleteEx.bx") //
						.param(Barcodes.field.getFIELD_NAME_ID(), String.valueOf(barcode1.getID()))//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
		) //
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mrl3);
	}

	protected void deleteExPromotionSync(Promotion p) throws Exception {
		MvcResult mr = mvc.perform(//
				get("/promotionSync/deleteEx.bx")//
						.param(Promotion.field.getFIELD_NAME_ID(), String.valueOf(p.getID()))//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr);
	}

	protected Promotion createExPromotion(String dateTime) throws Exception {
		MvcResult mr = mvc.perform(//
				post("/promotionSync/createEx.bx") //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionBoss) //
						.param(Promotion.field.getFIELD_NAME_name(), "测试一号全场满10减1")//
						.param(Promotion.field.getFIELD_NAME_status(), String.valueOf(EnumStatusPromotion.ESP_Active.getIndex()))//
						.param(Promotion.field.getFIELD_NAME_type(), String.valueOf(EnumTypePromotion.ETP_DecreaseOnAmount.getIndex()))//
						.param(Promotion.field.getFIELD_NAME_datetimeStart(), dateTime)//
						.param(Promotion.field.getFIELD_NAME_datetimeEnd(), "3000/10/22")//
						.param(Promotion.field.getFIELD_NAME_excecutionThreshold(), "10")//
						.param(Promotion.field.getFIELD_NAME_excecutionAmount(), "1")//
						.param(Promotion.field.getFIELD_NAME_excecutionDiscount(), "-1")//
						.param(Promotion.field.getFIELD_NAME_scope(), String.valueOf(EnumPromotionScope.EPS_SpecifiedCommodities.getIndex()))//
						.param(Promotion.field.getFIELD_NAME_staff(), "1")//
						.param(Promotion.field.getFIELD_NAME_returnObject(), String.valueOf(1))//
						.param(Promotion.field.getFIELD_NAME_commodityIDs(), "1,2")//
		).andExpect(status().isOk()).andDo(print()).andReturn();
		// 结果验证：检查错误码
		Shared.checkJSONErrorCode(mr);

		JSONObject o = JSONObject.fromObject(mr.getResponse().getContentAsString());
		Promotion p = new Promotion();
		Promotion p1 = (Promotion) p.parse1(JsonPath.read(o, "$.object").toString());

		return p1;
	}

	@Test
	public void updateExTest1() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case1:正常修改一个商品的条形码");
		Commodity commodityInput = BaseCommodityTest.DataInput.getCommodity();
		List<List<BaseModel>> bmList = BaseCommodityTest.createCommodityViaMapper(BaseBO.CASE_Commodity_CreateSingle, commodityInput);
		Barcodes barcodes = (Barcodes) bmList.get(1).get(0);
		
		barcodes.setBarcode("354" + System.currentTimeMillis() % 1000000);
		BaseBarcodesTest.updateBarcodesViaAction(barcodes, mvc, sessionBoss, mapBO, Shared.DBName_Test);
	}

	@Test
	public void updateExTest2() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case2:修改一个不存在的条形码");
		MvcResult mr1 = mvc.perform(//
				post("/barcodesSync/updateEx.bx")//
						.param(Barcodes.field.getFIELD_NAME_ID(), String.valueOf(Shared.BIG_ID))//
						.param(Barcodes.field.getFIELD_NAME_commodityID(), "1")//
						.param(Barcodes.field.getFIELD_NAME_barcode(), "322" + System.currentTimeMillis() % 1000000)//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr1, EnumErrorCode.EC_NoSuchData);
	}

	@Test
	public void updateExTest3() throws Exception {
		Shared.printTestMethodStartInfo();

		String message = "修改的条形码在零售单商品中有依赖";
		Shared.caseLog("case3:" + message);
		MvcResult mr1 = mvc.perform(//
				post("/barcodesSync/updateEx.bx")//
						.param(Barcodes.field.getFIELD_NAME_ID(), "1")//
						.param(Barcodes.field.getFIELD_NAME_commodityID(), "1")//
						.param(Barcodes.field.getFIELD_NAME_barcode(), "322" + System.currentTimeMillis() % 1000000)//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr1, EnumErrorCode.EC_BusinessLogicNotDefined);
		Shared.checkJSONMsg(mr1, message, "错误信息不正确！");
	}

	@Test
	public void updateExTest4() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case4:没有修改的权限");
		Commodity commodityInput = BaseCommodityTest.DataInput.getCommodity();
		List<List<BaseModel>> bmList = BaseCommodityTest.createCommodityViaMapper(BaseBO.CASE_Commodity_CreateSingle, commodityInput);
		Commodity comm = (Commodity) bmList.get(0).get(0);
		Barcodes barcodes = (Barcodes) bmList.get(1).get(0);

		MvcResult mr1 = mvc.perform(//
				post("/barcodesSync/updateEx.bx")//
						.param(Barcodes.field.getFIELD_NAME_ID(), String.valueOf(barcodes.getID()))//
						.param(Barcodes.field.getFIELD_NAME_commodityID(), String.valueOf(comm.getID()))//
						.param(Barcodes.field.getFIELD_NAME_barcode(), "322" + System.currentTimeMillis() % 1000000)//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfCashier))//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr1, EnumErrorCode.EC_NoPermission);
	}

	@Test
	public void updateExTest5() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case5:传入一个不存在的commodityID");
		Commodity commodityInput = BaseCommodityTest.DataInput.getCommodity();
		List<List<BaseModel>> bmList = BaseCommodityTest.createCommodityViaMapper(BaseBO.CASE_Commodity_CreateSingle, commodityInput);
		Barcodes barcodes = (Barcodes) bmList.get(1).get(0);

		MvcResult mr = mvc.perform(//
				post("/barcodesSync/updateEx.bx")//
						.param(Barcodes.field.getFIELD_NAME_ID(), String.valueOf(barcodes.getID()))//
						.param(Barcodes.field.getFIELD_NAME_commodityID(), String.valueOf(Shared.BIG_ID))//
						.param(Barcodes.field.getFIELD_NAME_barcode(), "322" + System.currentTimeMillis() % 1000000)//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_BusinessLogicNotDefined);
	}

	@Test
	public void updateExTest6() throws Exception {
		Shared.printTestMethodStartInfo();

		String message = "修改的条形码在采购订单商品中有依赖";
		Shared.caseLog("case6:" + message);
		MvcResult mr1 = mvc.perform(//
				post("/barcodesSync/updateEx.bx")//
						.param(Barcodes.field.getFIELD_NAME_ID(), "101")//
						.param(Barcodes.field.getFIELD_NAME_commodityID(), "1")//
						.param(Barcodes.field.getFIELD_NAME_barcode(), "322" + System.currentTimeMillis() % 1000000)//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr1, EnumErrorCode.EC_BusinessLogicNotDefined);
		Shared.checkJSONMsg(mr1, message, "错误信息不正确！");
	}

	@Test
	public void updateExTest7() throws Exception {
		Shared.printTestMethodStartInfo();

		String message = "修改的条形码在入库单商品中有依赖";
		Shared.caseLog("case7:" + message);
		MvcResult mr1 = mvc.perform(//
				post("/barcodesSync/updateEx.bx")//
						.param(Barcodes.field.getFIELD_NAME_ID(), "102")//
						.param(Barcodes.field.getFIELD_NAME_commodityID(), "1")//
						.param(Barcodes.field.getFIELD_NAME_barcode(), "322" + System.currentTimeMillis() % 1000000)//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr1, EnumErrorCode.EC_BusinessLogicNotDefined);
		Shared.checkJSONMsg(mr1, message, "错误信息不正确！");
	}

	@Test
	public void updateExTest8() throws Exception {
		Shared.printTestMethodStartInfo();

		String message = "修改的条形码在盘点单商品中有依赖";
		Shared.caseLog("case8:" + message);
		MvcResult mr1 = mvc.perform(//
				post("/barcodesSync/updateEx.bx")//
						.param(Barcodes.field.getFIELD_NAME_ID(), "104")//
						.param(Barcodes.field.getFIELD_NAME_commodityID(), "1")//
						.param(Barcodes.field.getFIELD_NAME_barcode(), "322" + System.currentTimeMillis() % 1000000)//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr1, EnumErrorCode.EC_BusinessLogicNotDefined);
		Shared.checkJSONMsg(mr1, message, "错误信息不正确！");
	}

	@Test
	public void updateExTest9() throws Exception {
		Shared.printTestMethodStartInfo();

		String message = "商品条码已存在，请重新修改";
		Shared.caseLog("case9:" + message);
		Commodity commodityInput = BaseCommodityTest.DataInput.getCommodity();
		List<List<BaseModel>> bmList = BaseCommodityTest.createCommodityViaMapper(BaseBO.CASE_Commodity_CreateSingle, commodityInput);
		Commodity comm = (Commodity) bmList.get(0).get(0);
		Barcodes barcodes = (Barcodes) bmList.get(1).get(0);
		// 创建条形码
		Barcodes barcodes2 = BaseBarcodesTest.DataInput.getBarcodes(comm.getID());
		barcodes2.setOperatorStaffID(STAFF_ID3);
		BaseBarcodesTest.createBarcodesViaMapper(barcodes2, BaseBO.INVALID_CASE_ID);
		//
		MvcResult mr1 = mvc.perform(//
				post("/barcodesSync/updateEx.bx")//
						.param(Barcodes.field.getFIELD_NAME_ID(), String.valueOf(barcodes.getID()))//
						.param(Barcodes.field.getFIELD_NAME_commodityID(), String.valueOf(comm.getID()))//
						.param(Barcodes.field.getFIELD_NAME_barcode(), barcodes2.getBarcode())//
						.param(Barcodes.field.getFIELD_NAME_returnObject(), "1")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr1, EnumErrorCode.EC_BusinessLogicNotDefined);
		Shared.checkJSONMsg(mr1, message, "错误信息不正确！");
	}
	
	@Test
	public void updateExTest10() throws Exception {
		Shared.printTestMethodStartInfo();
		// 
		String errorMsg = "修改的条形码在指定促销范围中有依赖";
		Shared.caseLog("case10:" + errorMsg);
		Commodity commodityGet = BaseCommodityTest.DataInput.getCommodity();
		List<List<BaseModel>> bmList = BaseCommodityTest.createCommodityViaMapper(BaseBO.CASE_Commodity_CreateSingle, commodityGet);
		Commodity commdityCreated = (Commodity) bmList.get(0).get(0);
		Barcodes barcodesCreated1 = (Barcodes) bmList.get(1).get(0);
		// 创建促销
		Promotion promotionGet = BasePromotionTest.DataInput.getPromotion();
		Promotion promotionCreated = BasePromotionTest.createPromotionViaMapper(promotionGet);
		// 创建促销范围
		PromotionScope promotionScope = new PromotionScope();
		promotionScope.setCommodityID(commdityCreated.getID());
		promotionScope.setPromotionID(promotionCreated.getID());
		createPromotionScope(promotionScope);
		// 将条形码1修改成条形码2
		Barcodes barcodes2 = BaseBarcodesTest.DataInput.getBarcodes(commdityCreated.getID());
		barcodes2.setOperatorStaffID(STAFF_ID3);
		barcodes2.setID(barcodesCreated1.getID());
		//
		MvcResult mr1 = mvc.perform(//
				post("/barcodesSync/updateEx.bx")//
						.param(Barcodes.field.getFIELD_NAME_ID(), String.valueOf(barcodesCreated1.getID()))//
						.param(Barcodes.field.getFIELD_NAME_commodityID(), String.valueOf(commdityCreated.getID()))//
						.param(Barcodes.field.getFIELD_NAME_barcode(), barcodes2.getBarcode())//
						.param(Barcodes.field.getFIELD_NAME_returnObject(), "1")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr1, EnumErrorCode.EC_BusinessLogicNotDefined);
		Shared.checkJSONMsg(mr1, errorMsg, "错误信息不正确！");
		BasePromotionTest.deletePromotionViaMapper(promotionCreated);
		// 商品有促销依赖，不能删除
//		BaseCommodityTest.deleteCommodityViaMapper(commCreated);
	}

	public ReturnCommoditySheet createReturnCommoditySheet() throws CloneNotSupportedException, InterruptedException {
		ReturnCommoditySheet rcs = new ReturnCommoditySheet();
		rcs.setStaffID(5);
		rcs.setProviderID(5);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Map<String, Object> rcsparams = rcs.getCreateParam(BaseBO.INVALID_CASE_ID, rcs);

		ReturnCommoditySheet rcsCreate = (ReturnCommoditySheet) returnCommoditySheetMapper.create(rcsparams);
		rcs.setIgnoreIDInComparision(true);
		if (rcs.compareTo(rcsCreate) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		System.out.println(rcsCreate == null ? "null" : rcsCreate);

		Assert.assertTrue(rcsCreate != null && EnumErrorCode.values()[Integer.parseInt(rcsparams.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "创建对象成功");
		System.out.println("创建退货单成功");

		return rcsCreate;
	}

	protected PromotionScope createPromotionScope(PromotionScope ps) {
		String err = ps.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");
		//
		Map<String, Object> paramsCreate = ps.getCreateParam(BaseBO.INVALID_CASE_ID, ps);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		PromotionScope psCreate = (PromotionScope) promotionScopeMapper.create(paramsCreate);
		//
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(paramsCreate.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, //
				paramsCreate.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		Assert.assertTrue(psCreate != null, "返回的对象为null");
		err = psCreate.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");
		//
		psCreate.setIgnoreIDInComparision(true);
		ps.setCommodityName(psCreate.getCommodityName());
		if (psCreate.compareTo(ps) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		//
		return psCreate;
	}

	protected Promotion createPromotion(Promotion p) {
		String err = p.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");
		//
		Map<String, Object> paramsCreate = p.getCreateParam(BaseBO.INVALID_CASE_ID, p);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Promotion pCreate = (Promotion) promotionMapper.create(paramsCreate);
		//
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(paramsCreate.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, paramsCreate.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		Assert.assertTrue(pCreate != null, "返回的对象为null");
		err = pCreate.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");
		//
		p.setIgnoreIDInComparision(true);
		if (p.compareTo(pCreate) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		//
		return pCreate;
	}

	protected PurchasingOrder purchasingOrderCreate(PurchasingOrder purchasingOrder) {

		// 传入参数字段验证
		String error = purchasingOrder.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
		//
		Map<String, Object> params = purchasingOrder.getCreateParam(BaseBO.INVALID_CASE_ID, purchasingOrder);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		PurchasingOrder createPo = (PurchasingOrder) purchasingOrderMapper.create(params);
		purchasingOrder.setIgnoreIDInComparision(true);
		if (purchasingOrder.compareTo(createPo) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		Assert.assertTrue(createPo != null && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		// 字段验证
		String error1 = createPo.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error1, "");

		return createPo;
	}

	/** 删除并结果验证创建的数据 */
	protected void deleteAndVerificationPurchasingOrder(PurchasingOrder createPo) {
		Map<String, Object> deleteParams1 = createPo.getDeleteParam(BaseBO.INVALID_CASE_ID, createPo);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		purchasingOrderMapper.delete(deleteParams1);
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(deleteParams1.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, deleteParams1.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());

		Map<String, Object> retrieve1Params = createPo.getRetrieve1ParamEx(BaseBO.INVALID_CASE_ID, createPo);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<List<BaseModel>> poList = (List<List<BaseModel>>) purchasingOrderMapper.retrieve1Ex(retrieve1Params);
		List<BaseModel> r1Po = (List<BaseModel>) poList.get(0);
		Assert.assertTrue(r1Po.size() == 0 && EnumErrorCode.values()[Integer.parseInt(retrieve1Params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError,
				retrieve1Params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
	}
}
