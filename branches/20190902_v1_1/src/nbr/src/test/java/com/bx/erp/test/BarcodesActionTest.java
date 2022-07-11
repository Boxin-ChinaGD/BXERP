package com.bx.erp.test;

import javax.servlet.http.HttpSession;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.testng.Assert.assertTrue;

import java.util.List;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MvcResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import com.bx.erp.action.BaseAction;
import com.bx.erp.cache.CacheManager;
import com.bx.erp.cache.config.ConfigCacheSizeCache.EnumConfigCacheSizeCache;
import com.bx.erp.model.Barcodes;
import com.bx.erp.model.CacheType.EnumCacheType;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.config.ConfigCacheSize;
import com.bx.erp.util.FieldFormat;
import com.jayway.jsonpath.JsonPath;
import net.sf.json.JSONObject;

@WebAppConfiguration
public class BarcodesActionTest extends BaseActionTest {

	private HttpSession sessionOfBoss;
	private HttpSession sessionOfManager;

	@BeforeClass
	public void setup() {

		super.setUp();

		// 由于商品的测试数据量超过缓存限制，所以在这里把缓存限制设置成最大，然后再在tear down设置回来。
		ConfigCacheSize ccs = new ConfigCacheSize();
		ccs.setValue(String.valueOf(BaseAction.PAGE_SIZE_Infinite));
		ccs.setID(EnumConfigCacheSizeCache.ECC_CommodityCacheSize.getIndex());
		ccs.setName(EnumConfigCacheSizeCache.ECC_CommodityCacheSize.getName());
		CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_ConfigCacheSize).write1(ccs, Shared.DBName_Test, STAFF_ID4);
		try {
			sessionOfBoss = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);
//			sessionOfManager = Shared.getStaffLoginSession(mvc, Shared.PhoneOfManager);
		} catch (Exception e) {
			e.printStackTrace();
		}
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

//	/** 为该类定制的一个方法，可做优化，但无需放到BaseCommodityTest */
//	protected List<List<BaseModel>> getCommodityA() throws CloneNotSupportedException, InterruptedException {
//
//		Commodity commodityInput = BaseCommodityTest.DataInput.getCommodity();
//
//		Shared.caseLog("添加名称不重复的商品");
//		commodityInput.setnOStart(100);
//		commodityInput.setPurchasingPriceStart(100D);
//		List<List<BaseModel>> bm = BaseCommodityTest.createCommodityViaMapper(BaseBO.CASE_Commodity_CreateSingle, commodityInput, mapMapper);
//
//		return bm;
//	}
//
//	/** 为该类定制的一个方法，可做优化，但无需放到BaseCommodityTest */
//	protected List<List<BaseModel>> getCommodityB() throws CloneNotSupportedException, InterruptedException {
//		// 创建一个商品
//		Commodity commodityInput = BaseCommodityTest.DataInput.getCommodity();
//		List<List<BaseModel>> bm = BaseCommodityTest.createCommodityViaMapper(BaseBO.CASE_Commodity_CreateSingle, commodityInput, mapMapper);
//		//
//		Commodity commCreate = (Commodity) bm.get(0).get(0);
//		// 创建7个Barcode
//		for(int i = 0; i < 7; i++) {
//			Barcodes barcodes =  BaseBarcodesTest.DataInput.getBarcodes(commCreate.getID());
//			BaseBarcodesTest.createBarcodesViaMapper(barcodes, BaseBO.INVALID_CASE_ID, mapMapper);
//		}
//		return bm;
//	}

	@Test
	public void retrieveNExTest() throws Exception {
		Shared.printTestMethodStartInfo();

		System.out.println("-------------------- case1:不输入任何参数进行查询  --------------------");
		MvcResult mrl = mvc.perform(//
				get("/barcodes/retrieveNEx.bx")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionOfBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mrl);

		System.out.println("-------------------- case2：输入3位条形码进行查询  --------------------");
		MvcResult mrl1 = mvc.perform(//
				get("/barcodes/retrieveNEx.bx?barcode=354")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionOfBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mrl1, EnumErrorCode.EC_WrongFormatForInputField);

		// JSONObject o =
		// JSONObject.fromObject(mrl1.getResponse().getContentAsString());
		// List<String> stringList = JsonPath.read(o, "$.barcodesList[*].barcode");
		// String str = "354";
		// for (String s : stringList) {
		// assertTrue(s.contains(str));
		// }

		System.out.println("-------------------- case3：输入商品ID进行查询  --------------------");
		MvcResult mrl2 = mvc.perform(//
				get("/barcodes/retrieveNEx.bx?commodityID=1")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionOfBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mrl2);

		JSONObject o2 = JSONObject.fromObject(mrl2.getResponse().getContentAsString());
		List<Integer> list1 = JsonPath.read(o2, "$.barcodesList[*].commodityID");
		for (int i : list1) {
			assertTrue(i == 1);
		}

		System.out.println("-------------------- case4：用没有权限的店员进行查询  --------------------");
		MvcResult mrl3 = mvc.perform(//
				get("/barcodes/retrieveNEx.bx?commodityID=1")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionOfManager)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		Shared.checkJSONErrorCode(mrl3, EnumErrorCode.EC_NoPermission);

		System.out.println("-------------------- case5：输入不存在商品ID进行查询  --------------------");
		MvcResult mrl4 = mvc.perform(//
				get("/barcodes/retrieveNEx.bx?commodityID=" + "-5")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionOfBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mrl4, EnumErrorCode.EC_WrongFormatForInputField);
		Shared.checkJSONMsg(mrl4, String.format(FieldFormat.FIELD_ERROR_ID_ForRetrieveN, Barcodes.field.getFIELD_NAME_commodityID()), "错误信息与预期不相符");

		System.out.println("-------------------- case6：输入不存在的7位条形码进行查询  --------------------");
		MvcResult mrl5 = mvc.perform(//
				get("/barcodes/retrieveNEx.bx?barcode=HHHHH22")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionOfBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mrl5);

		JSONObject o4 = JSONObject.fromObject(mrl5.getResponse().getContentAsString());
		List<?> list3 = JsonPath.read(o4, "$.barcodesList[*]");
		assertTrue(list3.size() == 0);

		System.out.println("-------------------- case7：输入7-64位条形码进行查询  --------------------");
		MvcResult mrl7 = mvc.perform(//
				get("/barcodes/retrieveNEx.bx?barcode=1234567")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionOfBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mrl7);
	}

//	@Test
//	public void createExTest1() throws Exception {
//		Shared.printTestMethodStartInfo();
//
//		List<List<BaseModel>> bmList = getCommodityA();
//		Commodity comm = (Commodity) bmList.get(0).get(0);
//
//		Shared.caseLog("case1:正常创建条形码");
//		Barcodes tmpCreateBarodes = BaseBarcodesTest.DataInput.getBarcodes(comm.getID());
//		
//
//		MvcResult mrl = mvc.perform(//
//				post("/barcodes/createEx.bx") //
//						.param(Barcodes.field.getFIELD_NAME_barcode(), tmpCreateBarodes.getBarcode())//
//						.param(Barcodes.field.getFIELD_NAME_commodityID(), String.valueOf(comm.getID()))//
//						.contentType(MediaType.APPLICATION_JSON) //
//						.session((MockHttpSession) sessionOfBoss)//
//		)//
//				.andExpect(status().isOk())//
//				.andDo(print())//
//				.andReturn();
//		//
//		Shared.checkJSONErrorCode(mrl);
//		// 结果验证商品历史是否有创建成功
//		Assert.assertTrue(BaseCommodityTest.queryCommodityHistorySize(comm.getID(), Shared.DBName_Test, commodityHistoryBO) > 0, "验证商品历史失败");
//	}
//
//	@Test
//	public void createrExTest2() throws Exception {
//		Shared.printTestMethodStartInfo();
//		
//		Shared.caseLog("case2:创建重复条形码");
//		//创建条形码A
//		List<List<BaseModel>> bmList = getCommodityA();
//		Commodity comm = (Commodity) bmList.get(0).get(0);
//		
//		Barcodes tmpCreateBarodes = BaseBarcodesTest.DataInput.getBarcodes(comm.getID());
//		//
//		MvcResult mrl = mvc.perform(//
//				post("/barcodes/createEx.bx") //
//						.param(Barcodes.field.getFIELD_NAME_barcode(), tmpCreateBarodes.getBarcode())//
//						.param(Barcodes.field.getFIELD_NAME_commodityID(), String.valueOf(comm.getID()))//
//						.contentType(MediaType.APPLICATION_JSON) //
//						.session((MockHttpSession) sessionOfBoss)//
//		)//
//				.andExpect(status().isOk())//
//				.andDo(print())//
//				.andReturn();
//		//
//		Shared.checkJSONErrorCode(mrl);
//		
//		Assert.assertTrue(BaseCommodityTest.queryCommodityHistorySize(comm.getID(), Shared.DBName_Test, commodityHistoryBO) > 0, "验证商品历史失败");
//
//		//创建重复条形码B
//		MvcResult mrl1 = mvc.perform(//
//				post("/barcodes/createEx.bx") //
//						.param(Barcodes.field.getFIELD_NAME_barcode(), tmpCreateBarodes.getBarcode())//
//						.param(Barcodes.field.getFIELD_NAME_commodityID(), String.valueOf(tmpCreateBarodes.getCommodityID()))//
//						.contentType(MediaType.APPLICATION_JSON)//
//						.session((MockHttpSession) sessionOfBoss)//
//		) //
//				.andExpect(status().isOk())//
//				.andDo(print())//
//				.andReturn();
//
//		Shared.checkJSONErrorCode(mrl1, EnumErrorCode.EC_Duplicated);
//	}
//
//	@Test
//	public void createrExTest3() throws Exception {
//		Shared.printTestMethodStartInfo();
//
//		Shared.caseLog("case3:用不存在的商品创建条形码");
//		MvcResult mrl3 = mvc.perform(//
//				post("/barcodes/createEx.bx") //
//						.param(Barcodes.field.getFIELD_NAME_barcode(), "354" + System.currentTimeMillis() % 1000000)//
//						.param(Barcodes.field.getFIELD_NAME_commodityID(), String.valueOf(BaseAction.INVALID_ID))//
//						.contentType(MediaType.APPLICATION_JSON) //
//						.session((MockHttpSession) sessionOfBoss)//
//		)//
//				.andExpect(status().isOk())//
//				.andDo(print())//
//				.andReturn();
//
//		Shared.checkJSONErrorCode(mrl3, EnumErrorCode.EC_WrongFormatForInputField);
//		Shared.checkJSONMsg(mrl3, Barcodes.FIELD_ERROR_CommodityID, "错误信息与预期不相符");
//	}
//
//	@Test
//	public void createrExTest4() throws Exception {
//		Shared.printTestMethodStartInfo();
//
//		Shared.caseLog("case4:没有权限进行操作");
//		MvcResult mrl4 = mvc.perform(//
//				post("/barcodes/createEx.bx") //
//						.param(Barcodes.field.getFIELD_NAME_barcode(), "354" + System.currentTimeMillis() % 1000000)//
//						.param(Barcodes.field.getFIELD_NAME_commodityID(), "1")//
//						.contentType(MediaType.APPLICATION_JSON) //
//						.session((MockHttpSession) sessionOfManager)//
//		)//
//				.andExpect(status().isOk())//
//				.andDo(print())//
//				.andReturn();
//
//		Shared.checkJSONErrorCode(mrl4, EnumErrorCode.EC_NoPermission);
//	}
//
//	@Test
//	public void createrExTest5() throws Exception {
//		Shared.printTestMethodStartInfo();
//
//		Shared.caseLog("Case5:商品条形码中存在中文字符");
//		MvcResult mrl6 = mvc.perform(//
//				post("/barcodes/createEx.bx") //
//						.param(Barcodes.field.getFIELD_NAME_barcode(), "354" + System.currentTimeMillis() % 1000000 + "无敌")//
//						.param(Barcodes.field.getFIELD_NAME_commodityID(), "1")//
//						.contentType(MediaType.APPLICATION_JSON) //
//						.session((MockHttpSession) sessionOfBoss)//
//		)//
//				.andExpect(status().isOk())//
//				.andDo(print())//
//				.andReturn();
//
//		Shared.checkJSONErrorCode(mrl6, EnumErrorCode.EC_WrongFormatForInputField);
//
//	}
//	
//	@Test
//	public void createrExTest6() throws Exception {
//		Shared.printTestMethodStartInfo();
//		
//		Shared.caseLog("Case6:商品条形码中存在特殊字符");
//		MvcResult mrl7 = mvc.perform(//
//				post("/barcodes/createEx.bx") //
//						.param(Barcodes.field.getFIELD_NAME_barcode(), "354" + System.currentTimeMillis() % 1000000 + "%*%")//
//						.param(Barcodes.field.getFIELD_NAME_commodityID(), "1")//
//						.contentType(MediaType.APPLICATION_JSON) //
//						.session((MockHttpSession) sessionOfBoss)//
//		)//
//				.andExpect(status().isOk())//
//				.andDo(print())//
//				.andReturn();
//
//		Shared.checkJSONErrorCode(mrl7, EnumErrorCode.EC_WrongFormatForInputField);
//	}

//	@Test
//	public void deleteExTest1() throws Exception {
//		Shared.printTestMethodStartInfo();
//
//		Shared.caseLog("正常删除");
//		Commodity comm = BaseCommodityTest.DataInput.getCommodity(CommodityType.EnumCommodityType.ECT_Normal.getIndex());
//		Commodity commCreated = BaseCommodityTest.createCommodityViaAction(comm, mvc, session, mapBO, Shared.DBName_Test);
//		
//		Barcodes barcodes = BaseBarcodesTest.DataInput.getBarcodes(commCreated.getID());
//		Barcodes barcodesCreate = BaseBarcodesTest.createBarcodesViaMapper(barcodes, BaseBO.INVALID_CASE_ID, mapMapper);
//
//		MvcResult mrl = mvc.perform(//
//				post("/barcodes/deleteEx.bx") //
//						.param(Barcodes.field.getFIELD_NAME_ID(), String.valueOf(barcodesCreate.getID()))//
//						.contentType(MediaType.APPLICATION_JSON)//
//						.session((MockHttpSession) sessionOfBoss)//
//		) //
//				.andExpect(status().isOk())//
//				.andDo(print())//
//				.andReturn();
//		Shared.checkJSONErrorCode(mrl);
//		
//		Assert.assertTrue(BaseCommodityTest.queryCommodityHistorySize(barcodesCreate.getCommodityID(), Shared.DBName_Test, commodityHistoryBO) > 0, "验证商品历史失败");
//	}
//
//	@Test
//	public void deleteExTest2() throws Exception {
//		Shared.printTestMethodStartInfo();
//		//
//		Shared.caseLog("case2:没有权限进行操作");
//		MvcResult mrl2 = mvc.perform(//
//				post("/barcodes/deleteEx.bx") //
//						.param(Barcodes.field.getFIELD_NAME_ID(), "1")//
//						.contentType(MediaType.APPLICATION_JSON)//
//						.session((MockHttpSession) sessionOfManager)//
//		) //
//				.andExpect(status().isOk())//
//				.andDo(print())//
//				.andReturn();
//
//		Shared.checkJSONErrorCode(mrl2, EnumErrorCode.EC_NoPermission);
//	}
//
//	@Test
//	public void deleteExTest3() throws Exception {
//		Shared.printTestMethodStartInfo();
//		//
//		Shared.caseLog("case3:输入一个不存在barcodesID进行删除");
//		MvcResult mrl3 = mvc.perform(//
//				post("/barcodes/deleteEx.bx") //
//						.param(Barcodes.field.getFIELD_NAME_ID(), String.valueOf(BaseAction.INVALID_ID))//
//						.contentType(MediaType.APPLICATION_JSON)//
//						.session((MockHttpSession) sessionOfBoss)//
//		) //
//				.andExpect(status().isOk())//
//				.andDo(print())//
//				.andReturn();
//		Shared.checkJSONErrorCode(mrl3, EnumErrorCode.EC_WrongFormatForInputField);
//		Shared.checkJSONMsg(mrl3, FieldFormat.FIELD_ERROR_ID, "错误信息与预期不相符");
//	}
//
//	@Test
//	public void deleteExTest4() throws Exception {
//		Shared.printTestMethodStartInfo();
//		//
//		String message = "删除的条形码在零售商品中有依赖";
//		Shared.caseLog("case4:" + message);
//		MvcResult mrl3 = mvc.perform(//
//				post("/barcodes/deleteEx.bx") //
//						.param(Barcodes.field.getFIELD_NAME_ID(), "1")//
//						.contentType(MediaType.APPLICATION_JSON)//
//						.session((MockHttpSession) sessionOfBoss)//
//		) //
//				.andExpect(status().isOk())//
//				.andDo(print())//
//				.andReturn();
//		Shared.checkJSONErrorCode(mrl3, EnumErrorCode.EC_BusinessLogicNotDefined);
//		Shared.checkJSONMsg(mrl3, message, "错误信息不正确");
//	}
//
//	@Test
//	public void deleteExTest5() throws Exception {
//		Shared.printTestMethodStartInfo();
//		//
//		String message = "删除的条形码在采购订单商品中有依赖";
//		Shared.caseLog("case5:" + message);
//		MvcResult mrl3 = mvc.perform(//
//				post("/barcodes/deleteEx.bx") //
//						.param(Barcodes.field.getFIELD_NAME_ID(), "101")//
//						.contentType(MediaType.APPLICATION_JSON)//
//						.session((MockHttpSession) sessionOfBoss)//
//		) //
//				.andExpect(status().isOk())//
//				.andDo(print())//
//				.andReturn();
//		Shared.checkJSONErrorCode(mrl3, EnumErrorCode.EC_BusinessLogicNotDefined);
//		Shared.checkJSONMsg(mrl3, message, "错误信息不正确");
//	}
//
//	@Test
//	public void deleteExTest6() throws Exception {
//		Shared.printTestMethodStartInfo();
//		//
//		String message = "删除的条形码在入库单商品中有依赖";
//		Shared.caseLog("case6:" + message);
//		MvcResult mrl3 = mvc.perform(//
//				post("/barcodes/deleteEx.bx") //
//						.param(Barcodes.field.getFIELD_NAME_ID(), "102")//
//						.contentType(MediaType.APPLICATION_JSON)//
//						.session((MockHttpSession) sessionOfBoss)//
//		) //
//				.andExpect(status().isOk())//
//				.andDo(print())//
//				.andReturn();
//		Shared.checkJSONErrorCode(mrl3, EnumErrorCode.EC_BusinessLogicNotDefined);
//		Shared.checkJSONMsg(mrl3, message, "错误信息不正确");
//	}
//
//	@Test
//	public void deleteExTest7() throws Exception {
//		Shared.printTestMethodStartInfo();
//		//
//		String message = "删除的条形码在盘点单商品中有依赖";
//		Shared.caseLog("case7:" + message);
//		MvcResult mrl3 = mvc.perform(//
//				post("/barcodes/deleteEx.bx") //
//						.param(Barcodes.field.getFIELD_NAME_ID(), "104")//
//						.contentType(MediaType.APPLICATION_JSON)//
//						.session((MockHttpSession) sessionOfBoss)//
//		) //
//				.andExpect(status().isOk())//
//				.andDo(print())//
//				.andReturn();
//		Shared.checkJSONErrorCode(mrl3, EnumErrorCode.EC_BusinessLogicNotDefined);
//		Shared.checkJSONMsg(mrl3, message, "错误信息不正确");
//	}
//
//	@Test
//	public void deleteExTest8() throws Exception {
//		Shared.printTestMethodStartInfo();
//		//
//		String message = "删除的条形码在采购订单商品中有依赖";
//		Shared.caseLog("case8:如果商品在采购订单商品中有依赖，那么它的条形码都是不能够删除的");
//		Barcodes tmpCreateBarcodes = BaseBarcodesTest.DataInput.getBarcodes(88);
//		Barcodes barcode = BaseBarcodesTest.createBarcodesViaMapper(tmpCreateBarcodes, BaseBO.INVALID_CASE_ID, mapMapper);
//		MvcResult mr8 = mvc.perform(//
//				post("/barcodes/deleteEx.bx") //
//						.param(Barcodes.field.getFIELD_NAME_ID(), String.valueOf(barcode.getID()))//
//						.contentType(MediaType.APPLICATION_JSON)//
//						.session((MockHttpSession) sessionOfBoss)//
//		) //
//				.andExpect(status().isOk())//
//				.andDo(print())//
//				.andReturn();
//		Shared.checkJSONErrorCode(mr8, EnumErrorCode.EC_BusinessLogicNotDefined);
//		Shared.checkJSONMsg(mr8, message, "错误信息不正确");
//	}
//
//	@Test
//	public void deleteExTest9() throws Exception {
//		Shared.printTestMethodStartInfo();
//		String message = "删除的条形码在入库单商品中有依赖";
//		Shared.caseLog("case9:如果商品在入库单商品中有依赖，那么它的条形码都是不能够删除的");
//		Barcodes tmpCreateBarcodes = BaseBarcodesTest.DataInput.getBarcodes(89);
//		Barcodes barcode = BaseBarcodesTest.createBarcodesViaMapper(tmpCreateBarcodes, BaseBO.INVALID_CASE_ID, mapMapper);
//		MvcResult mr9 = mvc.perform(//
//				post("/barcodes/deleteEx.bx") //
//						.param(Barcodes.field.getFIELD_NAME_ID(), String.valueOf(barcode.getID()))//
//						.contentType(MediaType.APPLICATION_JSON)//
//						.session((MockHttpSession) sessionOfBoss)//
//		) //
//				.andExpect(status().isOk())//
//				.andDo(print())//
//				.andReturn();
//		Shared.checkJSONErrorCode(mr9, EnumErrorCode.EC_BusinessLogicNotDefined);
//		Shared.checkJSONMsg(mr9, message, "错误信息不正确");
//	}
//
//	@Test
//	public void deleteExTest10() throws Exception {
//		Shared.printTestMethodStartInfo();
//		String message = "删除的条形码在盘点单商品中有依赖";
//		Shared.caseLog("case10:如果商品在盘点商品中有依赖，那么它的条形码都是不能够删除的");
//		Barcodes tmpCreateBarcodes = BaseBarcodesTest.DataInput.getBarcodes(91);
//		Barcodes barcode = BaseBarcodesTest.createBarcodesViaMapper(tmpCreateBarcodes, BaseBO.INVALID_CASE_ID, mapMapper);
//		MvcResult mr10 = mvc.perform(//
//				post("/barcodes/deleteEx.bx") //
//						.param(Barcodes.field.getFIELD_NAME_ID(), String.valueOf(barcode.getID()))//
//						.contentType(MediaType.APPLICATION_JSON)//
//						.session((MockHttpSession) sessionOfBoss)//
//		) //
//				.andExpect(status().isOk())//
//				.andDo(print())//
//				.andReturn();
//		Shared.checkJSONErrorCode(mr10, EnumErrorCode.EC_BusinessLogicNotDefined);
//		Shared.checkJSONMsg(mr10, message, "错误信息不正确");
//	}
//
//	@Test
//	public void deleteExTest11() throws Exception {
//		Shared.printTestMethodStartInfo();
//		//
//		String message = "删除的条形码在零售商品中有依赖";
//		Shared.caseLog("case11:如果商品在零售单商品中有依赖，那么它的条形码都是不能够删除的");
//		Barcodes tmpCreateBarcodes = BaseBarcodesTest.DataInput.getBarcodes(1);
//		Barcodes barcode = BaseBarcodesTest.createBarcodesViaMapper(tmpCreateBarcodes, BaseBO.INVALID_CASE_ID, mapMapper);
//		MvcResult mr11 = mvc.perform(//
//				post("/barcodes/deleteEx.bx") //
//						.param(Barcodes.field.getFIELD_NAME_ID(), String.valueOf(barcode.getID()))//
//						.contentType(MediaType.APPLICATION_JSON)//
//						.session((MockHttpSession) sessionOfBoss)//
//		) //
//				.andExpect(status().isOk())//
//				.andDo(print())//
//				.andReturn();
//		Shared.checkJSONErrorCode(mr11, EnumErrorCode.EC_BusinessLogicNotDefined);
//		Shared.checkJSONMsg(mr11, message, "错误信息不正确");
//	}
//
//	@Test
//	public void deleteExTest12() throws Exception {
//		Shared.printTestMethodStartInfo();
//
//		// 创建一个商品
//		Commodity commodity = BaseCommodityTest.DataInput.getCommodity(CommodityType.EnumCommodityType.ECT_Normal.getIndex());
//		Commodity commCreated = BaseCommodityTest.createCommodityViaAction(commodity, mvc, session, mapBO, Shared.DBName_Test);
//		// 根据商品ID创建一个条形码t1
//		Barcodes tmpCreateBarcodes = BaseBarcodesTest.DataInput.getBarcodes(commCreated.getID());
//		Barcodes barcode1 = BaseBarcodesTest.createBarcodesViaMapper(tmpCreateBarcodes, BaseBO.INVALID_CASE_ID, mapMapper);
//		// 创建退货单
//		ReturnCommoditySheet rcsCreate = createReturnCommoditySheet();
//		// 根据商品和条形码创建一个退货单商品
//		ReturnCommoditySheetCommodity returnCommoditySheetCommodity = new ReturnCommoditySheetCommodity();
//		returnCommoditySheetCommodity.setReturnCommoditySheetID(rcsCreate.getID());
//		returnCommoditySheetCommodity.setCommodityID(commCreated.getID());
//		returnCommoditySheetCommodity.setBarcodeID(barcode1.getID());
//		returnCommoditySheetCommodity.setNO(50);
//		returnCommoditySheetCommodity.setSpecification("箱");
//		returnCommoditySheetCommodity.setPurchasingPrice(5.5d);
//		//
//		Map<String, Object> createIcCommparams = returnCommoditySheetCommodity.getCreateParam(BaseBO.INVALID_CASE_ID, returnCommoditySheetCommodity);
//		DataSourceContextHolder.setDbName(Shared.DBName_Test);
//		ReturnCommoditySheetCommodity returnCommoditySheetCommodityCreated = (ReturnCommoditySheetCommodity) returnCommoditySheetCommodityMapper.create(createIcCommparams);
//		Assert.assertTrue(returnCommoditySheetCommodityCreated != null && EnumErrorCode.values()[Integer.parseInt(createIcCommparams.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "创建对象失败");
//		returnCommoditySheetCommodity.setIgnoreIDInComparision(true);
//		if (returnCommoditySheetCommodity.compareTo(returnCommoditySheetCommodityCreated) != 0) {
//			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
//		}
//		// 根据商品ID创建一个条形码t2
//		tmpCreateBarcodes = BaseBarcodesTest.DataInput.getBarcodes(commCreated.getID());
//		Barcodes barcode2 = BaseBarcodesTest.createBarcodesViaMapper(tmpCreateBarcodes, BaseBO.INVALID_CASE_ID, mapMapper);
//		//
//		String message = "删除的条形码在退货单商品中有依赖";
//		Shared.caseLog("case12:如果商品在退货单商品中有依赖，那么它的条形码都是不能够删除的");
//		MvcResult mr12 = mvc.perform(//
//				post("/barcodes/deleteEx.bx") //
//						.param(Barcodes.field.getFIELD_NAME_ID(), String.valueOf(barcode2.getID()))//
//						.contentType(MediaType.APPLICATION_JSON)//
//						.session((MockHttpSession) sessionOfBoss)//
//		) //
//				.andExpect(status().isOk())//
//				.andDo(print())//
//				.andReturn();
//		Shared.checkJSONErrorCode(mr12, EnumErrorCode.EC_BusinessLogicNotDefined);
//		Shared.checkJSONMsg(mr12, message, "错误信息不正确");
//		// 删除测试的退货单商品
//		Map<String, Object> deletepOrderCommparams = returnCommoditySheetCommodityCreated.getDeleteParam(BaseBO.INVALID_CASE_ID, returnCommoditySheetCommodityCreated);
//		DataSourceContextHolder.setDbName(Shared.DBName_Test);
//		returnCommoditySheetCommodityMapper.delete(deletepOrderCommparams);
//		Assert.assertEquals(EnumErrorCode.values()[Integer.parseInt(deletepOrderCommparams.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())], EnumErrorCode.EC_NoError);
//		// 删除测试的商品(删除商品时会连它的所有条形码都删除掉)
//		commCreated.setOperatorStaffID(STAFF_ID3);
//		BaseCommodityTest.deleteCommodityViaAction(commCreated, Shared.DBName_Test, mvc, session, mapBO);
//	}
//
//	@Test
//	public void deleteExTest13() throws Exception {
//		Shared.printTestMethodStartInfo();
//		Shared.caseLog("case13:如果商品在指定促销范围中有依赖，那么它的条形码都是不能够删除的");
//		// 创建商品
//		Commodity c = BaseCommodityTest.DataInput.getCommodity();
//		c.setPurchaseFlag(1);
//		Commodity cCreate = BaseCommodityTest.createCommodityViaAction(c, mvc, session, mapBO, Shared.DBName_Test);
//		// 创建促销范围
//		PromotionScope ps = new PromotionScope();
//		ps.setCommodityID(cCreate.getID());
//		ps.setPromotionID(2);
//		createPromotionScope(ps);
//
//		Barcodes tmpCreateBarcodes = BaseBarcodesTest.DataInput.getBarcodes(cCreate.getID());
//		Barcodes barcode1 = BaseBarcodesTest.createBarcodesViaMapper(tmpCreateBarcodes, BaseBO.INVALID_CASE_ID, mapMapper);
//		//
//		String message = "删除的条形码在指定促销范围中有依赖";
//
//		MvcResult mrl3 = mvc.perform(//
//				post("/barcodes/deleteEx.bx") //
//						.param(Barcodes.field.getFIELD_NAME_ID(), String.valueOf(barcode1.getID()))//
//						.contentType(MediaType.APPLICATION_JSON)//
//						.session((MockHttpSession) sessionOfBoss)//
//		) //
//				.andExpect(status().isOk())//
//				.andDo(print())//
//				.andReturn();
//		Shared.checkJSONErrorCode(mrl3, EnumErrorCode.EC_BusinessLogicNotDefined);
//		Shared.checkJSONMsg(mrl3, message, "错误信息不正确");
//	}
//
//	@Test
//	public void deleteExTest14() throws Exception {
//		Shared.printTestMethodStartInfo();
//		Shared.caseLog("case14:如果商品在指定促销范围中有依赖，但是促销已经结束了，那么它的条形码还是不能够删除的");
//		// 创建商品
//		Commodity c = BaseCommodityTest.DataInput.getCommodity();
//		c.setPurchaseFlag(1);
//		Commodity cCreate = BaseCommodityTest.createCommodityViaAction(c, mvc, session, mapBO, Shared.DBName_Test);
//		// 创建促销范围
//		PromotionScope ps = new PromotionScope();
//		ps.setCommodityID(cCreate.getID());
//		ps.setPromotionID(4);
//		createPromotionScope(ps);
//
//		Barcodes tmpCreateBarcodes = BaseBarcodesTest.DataInput.getBarcodes(cCreate.getID());
//		Barcodes barcode1 = BaseBarcodesTest.createBarcodesViaMapper(tmpCreateBarcodes, BaseBO.INVALID_CASE_ID, mapMapper);
//		//
//		MvcResult mrl3 = mvc.perform(//
//				post("/barcodes/deleteEx.bx") //
//						.param(Barcodes.field.getFIELD_NAME_ID(), String.valueOf(barcode1.getID()))//
//						.contentType(MediaType.APPLICATION_JSON)//
//						.session((MockHttpSession) sessionOfBoss)//
//		) //
//				.andExpect(status().isOk())//
//				.andDo(print())//
//				.andReturn();
//		Shared.checkJSONErrorCode(mrl3, EnumErrorCode.EC_BusinessLogicNotDefined);
//	}
//
//	@Test
//	public void deleteExTest15() throws Exception {
//		Shared.printTestMethodStartInfo();
//		Shared.caseLog("case15:如果商品在指定促销范围中有依赖，但是促销已经删除了，那么它的条形码是不能够删除的");
//		// 创建商品
//		Commodity c = BaseCommodityTest.DataInput.getCommodity();
//		c.setPurchaseFlag(1);
//		Commodity cCreate = BaseCommodityTest.createCommodityViaAction(c, mvc, session, mapBO, Shared.DBName_Test);
//		// 创建促销
//		Date date = new Date();
//		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
//		String dateTime = simpleDateFormat.format(date.getTime() + ((long) 3 * 24 * 60 * 60 * 1000));
//		Promotion p = createExPromotion(dateTime);
//		// 创建促销范围
//		PromotionScope ps = new PromotionScope();
//		ps.setCommodityID(cCreate.getID());
//		ps.setPromotionID(p.getID());
//		createPromotionScope(ps);
//
//		Barcodes tmpCreateBarcodes = BaseBarcodesTest.DataInput.getBarcodes(cCreate.getID());
//		Barcodes barcode1 = BaseBarcodesTest.createBarcodesViaMapper(tmpCreateBarcodes, BaseBO.INVALID_CASE_ID, mapMapper);
//		// 删除促销
//		deleteExPromotionSync(p);
//		//
//		MvcResult mrl3 = mvc.perform(//
//				post("/barcodes/deleteEx.bx") //
//						.param(Barcodes.field.getFIELD_NAME_ID(), String.valueOf(barcode1.getID()))//
//						.contentType(MediaType.APPLICATION_JSON)//
//						.session((MockHttpSession) sessionOfBoss)//
//		) //
//				.andExpect(status().isOk())//
//				.andDo(print())//
//				.andReturn();
//		Shared.checkJSONErrorCode(mrl3, EnumErrorCode.EC_BusinessLogicNotDefined);
//	}

//	protected void deleteExPromotionSync(Promotion p) throws Exception { // ... 将来放到BasePromotionTest中
//		MvcResult mr = mvc.perform(//
//				get("/promotionSync/deleteEx.bx")//
//						.param(Promotion.field.getFIELD_NAME_ID(), String.valueOf(p.getID()))//
//						.contentType(MediaType.APPLICATION_JSON)//
//						.session((MockHttpSession) sessionOfBoss)//
//		)//
//				.andExpect(status().isOk())//
//				.andDo(print())//
//				.andReturn();
//		Shared.checkJSONErrorCode(mr);
//	}
//
//	protected Promotion createExPromotion(String dateTime) throws Exception { // ... 将来放到BasePromotionTest中
//		MvcResult mr = mvc.perform(//
//				post("/promotionSync/createEx.bx") //
//						.contentType(MediaType.APPLICATION_JSON) //
//						.session((MockHttpSession) sessionOfBoss) //
//						.param(Promotion.field.getFIELD_NAME_name(), "测试一号全场满10减1")//
//						.param(Promotion.field.getFIELD_NAME_status(), String.valueOf(EnumStatusPromotion.ESP_Active.getIndex()))//
//						.param(Promotion.field.getFIELD_NAME_type(), String.valueOf(EnumTypePromotion.ETP_DecreaseOnAmount.getIndex()))//
//						.param(Promotion.field.getFIELD_NAME_datetimeStart(), dateTime)//
//						.param(Promotion.field.getFIELD_NAME_datetimeEnd(), "3000/10/22")//
//						.param(Promotion.field.getFIELD_NAME_excecutionThreshold(), "10")//
//						.param(Promotion.field.getFIELD_NAME_excecutionAmount(), "1")//
//						.param(Promotion.field.getFIELD_NAME_excecutionDiscount(), "-1")//
//						.param(Promotion.field.getFIELD_NAME_scope(), String.valueOf(EnumPromotionScope.EPS_SpecifiedCommodities.getIndex()))//
//						.param(Promotion.field.getFIELD_NAME_staff(), "1")//
//						.param(Promotion.field.getFIELD_NAME_returnObject(), String.valueOf(1))//
//						.param(Promotion.field.getFIELD_NAME_commodityID(), "1,2")//
//		).andExpect(status().isOk()).andDo(print()).andReturn();
//		// 结果验证：检查错误码
//		Shared.checkJSONErrorCode(mr);
//
//		JSONObject o = JSONObject.fromObject(mr.getResponse().getContentAsString());
//		Promotion p = new Promotion();
//		Promotion p1 = (Promotion) p.parse1(JsonPath.read(o, "$.object").toString());
//
//		return p1;
//	}

	@Test
	public void retrieve1ExTest() throws Exception {
		Shared.printTestMethodStartInfo();

		MvcResult mrl = mvc.perform(//
				get("/barcodes/retrieve1Ex.bx")//
						.param(Barcodes.field.getFIELD_NAME_ID(), "1")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionOfBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mrl);

		System.out.println("-------------------- case2:没有权限进行操作  --------------------");
		MvcResult mr2 = mvc.perform(//
				get("/barcodes/retrieve1Ex.bx")//
						.param(Barcodes.field.getFIELD_NAME_ID(), "1")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionOfManager)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		Shared.checkJSONErrorCode(mr2, EnumErrorCode.EC_NoPermission);

		System.out.println("-------------------- case3:输入一个不存在barcodesID进行查询  --------------------");
		MvcResult mrl2 = mvc.perform(//
				get("/barcodes/retrieve1Ex.bx")//
						.param(Barcodes.field.getFIELD_NAME_ID(), String.valueOf(BaseAction.INVALID_ID))//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionOfBoss)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		Shared.checkJSONErrorCode(mrl2, EnumErrorCode.EC_WrongFormatForInputField);
		Shared.checkJSONMsg(mrl2, FieldFormat.FIELD_ERROR_ID, "错误信息与预期不相符");
	}

//	public ReturnCommoditySheet createReturnCommoditySheet() throws CloneNotSupportedException, InterruptedException { // ... 将来放到BaseReturnCommoditySheetTest中
//		ReturnCommoditySheet rcs = new ReturnCommoditySheet();
//		rcs.setStaffID(5);
//		rcs.setProviderID(5);
//		DataSourceContextHolder.setDbName(Shared.DBName_Test);
//		Map<String, Object> rcsparams = rcs.getCreateParam(BaseBO.INVALID_CASE_ID, rcs);
//
//		ReturnCommoditySheet rcsCreate = (ReturnCommoditySheet) returnCommoditySheetMapper.create(rcsparams);
//		rcs.setIgnoreIDInComparision(true);
//		if (rcs.compareTo(rcsCreate) != 0) {
//			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
//		}
//		System.out.println(rcsCreate == null ? "null" : rcsCreate);
//
//		Assert.assertTrue(rcsCreate != null && EnumErrorCode.values()[Integer.parseInt(rcsparams.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "创建对象成功");
//		System.out.println("创建退货单成功");
//
//		return rcsCreate;
//	}

//	protected PromotionScope createPromotionScope(PromotionScope ps) { // ... 将来放到BasePromotionTest中
//		String err = ps.checkCreate(BaseBO.INVALID_CASE_ID);
//		Assert.assertEquals(err, "");
//		//
//		Map<String, Object> paramsCreate = ps.getCreateParam(BaseBO.INVALID_CASE_ID, ps);
//		//
//		DataSourceContextHolder.setDbName(Shared.DBName_Test);
//		PromotionScope psCreate = (PromotionScope) promotionScopeMapper.create(paramsCreate);
//		//
//		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(paramsCreate.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, //
//				paramsCreate.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
//		Assert.assertTrue(psCreate != null, "返回的对象为null");
//		err = psCreate.checkCreate(BaseBO.INVALID_CASE_ID);
//		Assert.assertEquals(err, "");
//		//
//		psCreate.setIgnoreIDInComparision(true);
//		ps.setCommodityName(psCreate.getCommodityName());
//		if (psCreate.compareTo(ps) != 0) {
//			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
//		}
//		//
//		return psCreate;
//	}

}
