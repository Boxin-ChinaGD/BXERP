package com.bx.erp.test.report;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.testng.Assert.assertTrue;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
import com.bx.erp.cache.CacheManager;
import com.bx.erp.cache.config.ConfigCacheSizeCache.EnumConfigCacheSizeCache;
import com.bx.erp.model.Barcodes;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.CommodityType;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.model.RetailTrade;
import com.bx.erp.model.RetailTradeCommodity;
import com.bx.erp.model.BaseModel.EnumBoolean;
import com.bx.erp.model.CacheType.EnumCacheType;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.commodity.Commodity;
import com.bx.erp.model.commodity.SubCommodity;
import com.bx.erp.model.config.ConfigCacheSize;
import com.bx.erp.model.report.RetailTradeReportByCommodity;
import com.bx.erp.model.warehousing.Warehousing;
import com.bx.erp.model.warehousing.WarehousingCommodity;
import com.bx.erp.test.BaseActionTest;
import com.bx.erp.test.BaseBarcodesTest;
import com.bx.erp.test.BaseCommodityTest;
import com.bx.erp.test.BaseRetailTradeTest;
import com.bx.erp.test.BaseTestNGSpringContextTest;
import com.bx.erp.test.Shared;
import com.bx.erp.util.DataSourceContextHolder;
import com.jayway.jsonpath.JsonPath;

import net.sf.json.JSONObject;

@WebAppConfiguration
public class RetailTradeReportByCommodityActionTest extends BaseActionTest {

	@BeforeClass
	public void setup() {
		super.setUp();
		// 由于本测试数据量超过缓存限制，所以在这里把缓存限制设置成最大，然后再在tear down设置回来。
		ConfigCacheSize ccs = new ConfigCacheSize();
		ccs.setValue(String.valueOf(BaseAction.PAGE_SIZE_Infinite));
		ccs.setID(EnumConfigCacheSizeCache.ECC_CommodityCacheSize.getIndex());
		ccs.setName(EnumConfigCacheSizeCache.ECC_CommodityCacheSize.getName());
		CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_ConfigCacheSize).write1(ccs, Shared.DBName_Test, STAFF_ID4);
		//
		ccs.setValue(String.valueOf(BaseAction.PAGE_SIZE_Infinite));
		ccs.setID(EnumConfigCacheSizeCache.ECC_RetailTradeNumberCacheSize.getIndex());
		ccs.setName(EnumConfigCacheSizeCache.ECC_RetailTradeNumberCacheSize.getName());
		CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_ConfigCacheSize).write1(ccs, Shared.DBName_Test, STAFF_ID4);
	}

	@AfterClass
	public void tearDown() {
		super.tearDown();
		// 由于本测试数据量超过缓存限制，在setup把缓存限制改了，所以在这里把缓存限制设置回来。
		ConfigCacheSize ccs = new ConfigCacheSize();
		ccs.setValue("100");
		ccs.setID(EnumConfigCacheSizeCache.ECC_CommodityCacheSize.getIndex());
		ccs.setName(EnumConfigCacheSizeCache.ECC_CommodityCacheSize.getName());
		CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_ConfigCacheSize).write1(ccs, Shared.DBName_Test, STAFF_ID4);
		//
		ccs.setValue("50");
		ccs.setID(EnumConfigCacheSizeCache.ECC_RetailTradeNumberCacheSize.getIndex());
		ccs.setName(EnumConfigCacheSizeCache.ECC_RetailTradeNumberCacheSize.getName());
		CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_ConfigCacheSize).write1(ccs, Shared.DBName_Test, STAFF_ID4);
	}

	@Test
	public void testRetrieveN1() throws Exception {
		Shared.printTestMethodStartInfo();
		// RetailTradeReportByCommodity rr1 = new RetailTradeReportByCommodity();
		Shared.caseLog("CASE1:传requestNO=1,正常查询");
		MvcResult mr1 = mvc.perform(//
				post("/retailTradeReportByCommodityAction/retrieveN.bx")//
						.param(RetailTradeReportByCommodity.field.getFIELD_NAME_datetimeStart(), "2018/01/01")//
						.param(RetailTradeReportByCommodity.field.getFIELD_NAME_datetimeEnd(), "2020/01/02")//
						.param(RetailTradeReportByCommodity.field.getFIELD_NAME_requestNO(), "1")//
						.param(RetailTradeReportByCommodity.field.getFIELD_NAME_iOrderBy(), "2")//
						.param(RetailTradeReportByCommodity.field.getFIELD_NAME_isASC(), "0")//
						.param(RetailTradeReportByCommodity.field.getFIELD_NAME_iCategoryID(), "-1")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//

		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		Shared.checkJSONErrorCode(mr1);
	}

	@Test
	public void testRetrieveN2() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("CASE2:传requestNO查询表格数据，只查询销售报表对应的表格数据");
		MvcResult mr2 = mvc.perform(//
				post("/retailTradeReportByCommodityAction/retrieveN.bx")//
						.param(RetailTradeReportByCommodity.field.getFIELD_NAME_datetimeStart(), "2018/01/01")//
						.param(RetailTradeReportByCommodity.field.getFIELD_NAME_datetimeEnd(), "2020/01/02")//
						.param(RetailTradeReportByCommodity.field.getFIELD_NAME_requestNO(), "1")//
						.param(RetailTradeReportByCommodity.field.getFIELD_NAME_iOrderBy(), "0")//
						.param(RetailTradeReportByCommodity.field.getFIELD_NAME_isASC(), "0")//
						.param(RetailTradeReportByCommodity.field.getFIELD_NAME_iCategoryID(), "-1")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//

		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		Shared.checkJSONErrorCode(mr2);
	}

	@Test
	public void testRetrieveN3() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("CASE3:传入商品名称进行模糊查询并按金额进行降序排列");
		MvcResult mr3 = mvc.perform(//
				post("/retailTradeReportByCommodityAction/retrieveN.bx")//
						.param(RetailTradeReportByCommodity.field.getFIELD_NAME_datetimeStart(), "2018/01/01")//
						.param(RetailTradeReportByCommodity.field.getFIELD_NAME_datetimeEnd(), "2020/01/02")//
						.param(RetailTradeReportByCommodity.field.getFIELD_NAME_requestNO(), "1")//
						.param(RetailTradeReportByCommodity.field.getFIELD_NAME_queryKeyword(), "可乐").param(RetailTradeReportByCommodity.field.getFIELD_NAME_iOrderBy(), "0")//
						.param(RetailTradeReportByCommodity.field.getFIELD_NAME_isASC(), "0")//
						.param(RetailTradeReportByCommodity.field.getFIELD_NAME_iCategoryID(), "-1")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//

		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		Shared.checkJSONErrorCode(mr3);
	}

	@Test
	public void testRetrieveN4() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("CASE4:传入商品名称进行模糊查询并按金额进行升序排列");
		MvcResult mr4 = mvc.perform(//
				post("/retailTradeReportByCommodityAction/retrieveN.bx")//
						.param(RetailTradeReportByCommodity.field.getFIELD_NAME_datetimeStart(), "2018/01/01")//
						.param(RetailTradeReportByCommodity.field.getFIELD_NAME_datetimeEnd(), "2020/01/02")//
						.param(RetailTradeReportByCommodity.field.getFIELD_NAME_requestNO(), "1")//
						.param(RetailTradeReportByCommodity.field.getFIELD_NAME_queryKeyword(), "可乐").param(RetailTradeReportByCommodity.field.getFIELD_NAME_iOrderBy(), "0")//
						.param(RetailTradeReportByCommodity.field.getFIELD_NAME_isASC(), "0")//
						.param(RetailTradeReportByCommodity.field.getFIELD_NAME_iCategoryID(), "-1")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//

		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		Shared.checkJSONErrorCode(mr4);
	}

	@Test
	public void testRetrieveN5() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("CASE5:传入条形码进行查询并按金额进行降序排列");
		MvcResult mr5 = mvc.perform(//
				post("/retailTradeReportByCommodityAction/retrieveN.bx")//
						.param(RetailTradeReportByCommodity.field.getFIELD_NAME_datetimeStart(), "2018/01/01")//
						.param(RetailTradeReportByCommodity.field.getFIELD_NAME_datetimeEnd(), "2020/01/02")//
						.param(RetailTradeReportByCommodity.field.getFIELD_NAME_requestNO(), "1")//
						.param(RetailTradeReportByCommodity.field.getFIELD_NAME_queryKeyword(), "3548293894545").param(RetailTradeReportByCommodity.field.getFIELD_NAME_iOrderBy(), "0")//
						.param(RetailTradeReportByCommodity.field.getFIELD_NAME_isASC(), "0")//
						.param(RetailTradeReportByCommodity.field.getFIELD_NAME_iCategoryID(), "-1")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//

		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		Shared.checkJSONErrorCode(mr5);
	}

	@Test
	public void testRetrieveN6() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("CASE6:传入单号进行查询并按金额进行降序排列");
		MvcResult mr6 = mvc.perform(//
				post("/retailTradeReportByCommodityAction/retrieveN.bx")//
						.param(RetailTradeReportByCommodity.field.getFIELD_NAME_datetimeStart(), "2018/01/01")//
						.param(RetailTradeReportByCommodity.field.getFIELD_NAME_datetimeEnd(), "2020/01/02")//
						.param(RetailTradeReportByCommodity.field.getFIELD_NAME_requestNO(), "1")//
						.param(RetailTradeReportByCommodity.field.getFIELD_NAME_queryKeyword(), "7").param(RetailTradeReportByCommodity.field.getFIELD_NAME_iOrderBy(), "0")//
						.param(RetailTradeReportByCommodity.field.getFIELD_NAME_isASC(), "0")//
						.param(RetailTradeReportByCommodity.field.getFIELD_NAME_iCategoryID(), "-1")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//

		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		Shared.checkJSONErrorCode(mr6);
	}

	@Test
	public void testRetrieveN7() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("CASE7:传入商品名称进行模糊查询并按金额进行降序排列,bIgnoreZeroNO=1忽略0进货量");
		MvcResult mr7 = mvc.perform(//
				post("/retailTradeReportByCommodityAction/retrieveN.bx")//
						.param(RetailTradeReportByCommodity.field.getFIELD_NAME_datetimeStart(), "2018/01/01")//
						.param(RetailTradeReportByCommodity.field.getFIELD_NAME_datetimeEnd(), "2020/01/02")//
						.param(RetailTradeReportByCommodity.field.getFIELD_NAME_requestNO(), "1")//
						.param(RetailTradeReportByCommodity.field.getFIELD_NAME_queryKeyword(), "可乐")//
						.param(RetailTradeReportByCommodity.field.getFIELD_NAME_bIgnoreZeroNO(), "1")//
						.param(RetailTradeReportByCommodity.field.getFIELD_NAME_iOrderBy(), "0")//
						.param(RetailTradeReportByCommodity.field.getFIELD_NAME_isASC(), "0")//
						.param(RetailTradeReportByCommodity.field.getFIELD_NAME_iCategoryID(), "-1")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//

		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		Shared.checkJSONErrorCode(mr7);
	}

	@Test
	public void testRetrieveN8() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("CASE8:传int3查询表格数据，按照商品类别查询");
		MvcResult mr8 = mvc.perform(//
				post("/retailTradeReportByCommodityAction/retrieveN.bx")//
						.param(RetailTradeReportByCommodity.field.getFIELD_NAME_requestNO(), "1")//
						.param(RetailTradeReportByCommodity.field.getFIELD_NAME_datetimeStart(), "2018/01/01")//
						.param(RetailTradeReportByCommodity.field.getFIELD_NAME_datetimeEnd(), "2020/01/02")//
						.param(RetailTradeReportByCommodity.field.getFIELD_NAME_iOrderBy(), "0")//
						.param(RetailTradeReportByCommodity.field.getFIELD_NAME_isASC(), "0")//
						.param(RetailTradeReportByCommodity.field.getFIELD_NAME_iCategoryID(), "1")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//

		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		Shared.checkJSONErrorCode(mr8);

	}

	@Test
	public void retrieveNTest_CASE1() throws UnsupportedEncodingException, Exception {
		Shared.printTestMethodStartInfo();
		//
		Shared.caseLog("Case1:根据输入_特殊字符商品名称查询商品零售报表");
		// 创建商品
		Commodity commodity = BaseCommodityTest.DataInput.getCommodity();
		commodity.setName("111退货可口可乐1111_5)4（" + System.currentTimeMillis());
		String barcode = "987623" + System.currentTimeMillis();
		commodity.setMultiPackagingInfo(barcode + ";" + commodity.getPackageUnitID() + ";" + commodity.getRefCommodityMultiple() + ";" + commodity.getPriceRetail() + ";" + commodity.getPriceVIP() + ";" + commodity.getPriceWholesale() + ";"
				+ commodity.getName() + ";");
		Commodity commodity1 = BaseCommodityTest.createCommodityViaAction(commodity, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		Barcodes barcodes1 = BaseBarcodesTest.retrieveNBarcodes(commodity1.getID(), Shared.DBName_Test);
		// 零售
		RetailTrade rt = BaseTestNGSpringContextTest.DataInput.getRetailTradeAndRetailTradeCommodity();
		rt.setPaymentType(1);
		rt.setStaffID(4); // 登录的staffID
		//
		RetailTradeCommodity rtc = (RetailTradeCommodity) rt.getListSlave1().get(0);
		rtc.setCommodityID(commodity1.getID());
		rtc.setBarcodeID(barcodes1.getID());
		// 获取零售前的商品信息
		List<Commodity> commList1 = getCommodityListBefourRetail(rt);
		// 获取零售前的商品当值入库单
		Warehousing ws = new Warehousing();
		List<WarehousingCommodity> wscList = getWarehousingCommodityList(commList1);
		ws.setListSlave1(wscList);
		//
		BaseRetailTradeTest.createRetailTradeViaAction(mvc, sessionBoss, mapBO, rt, Shared.DBName_Test);
		// 根据商品名称模糊查询
		MvcResult mr = mvc.perform(//
				post("/retailTradeReportByCommodityAction/retrieveN.bx")//
						.param(RetailTradeReportByCommodity.field.getFIELD_NAME_datetimeStart(), "2018/01/01")//
						.param(RetailTradeReportByCommodity.field.getFIELD_NAME_datetimeEnd(), "2020/01/02")//
						.param(RetailTradeReportByCommodity.field.getFIELD_NAME_requestNO(), "1")//
						.param(RetailTradeReportByCommodity.field.getFIELD_NAME_queryKeyword(), "_")//
						.param(RetailTradeReportByCommodity.field.getFIELD_NAME_iOrderBy(), "0")//
						.param(RetailTradeReportByCommodity.field.getFIELD_NAME_isASC(), "0")//
						.param(RetailTradeReportByCommodity.field.getFIELD_NAME_iCategoryID(), "-1")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//

		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		// 结果验正
		Shared.checkJSONErrorCode(mr);
		// 解析出商品名称作对比
		JSONObject o = JSONObject.fromObject(mr.getResponse().getContentAsString());
		List<String> listName = JsonPath.read(o, "$.reportList[*].name");
		for (String result : listName) {
			Assert.assertTrue(result.contains("_"), "查询商品名称没有包含_特殊字符");
		}

	}

	private List<WarehousingCommodity> getWarehousingCommodityList(List<Commodity> commList) {
		WarehousingCommodity wsc = new WarehousingCommodity();
		List<WarehousingCommodity> wscList = new ArrayList<WarehousingCommodity>();
		for (Commodity commodity : commList) {
			if (commodity.getType() == CommodityType.EnumCommodityType.ECT_Normal.getIndex()) {
				wsc = getCommodityWarehousing(commodity);
				// assertTrue(wsc != null, "该商品没有入库单，不能零售");可以不入库就能销售

				wscList.add(wsc);
			} else if (commodity.getType() == CommodityType.EnumCommodityType.ECT_MultiPackaging.getIndex()) {
				Commodity normalCommodity = new Commodity();
				normalCommodity.setID(commodity.getRefCommodityID());
				normalCommodity.setCurrentWarehousingID(commodity.getCurrentWarehousingID());
				//
				wsc = getCommodityWarehousing(normalCommodity);
				// assertTrue(wsc != null, "该商品没有入库单，不能零售");
				//
				wscList.add(wsc);
			} else if (commodity.getType() == CommodityType.EnumCommodityType.ECT_Combination.getIndex()) {
				for (Object o : commodity.getListSlave2()) {
					Commodity normalCommodity = (Commodity) o;
					//
					wsc = getCommodityWarehousing(normalCommodity);
					// assertTrue(wsc != null, "该商品没有入库单，不能零售");
					//
					wscList.add(wsc);
				}
			}
		}
		return wscList;
	}

	@SuppressWarnings("unchecked")
	private WarehousingCommodity getCommodityWarehousing(Commodity commodity) {
		if (commodity.getCurrentWarehousingID() == 0) {
			Warehousing warehousing = new Warehousing();
			warehousing.setPageSize(BaseAction.PAGE_SIZE_Infinite);
			// 查询所有审核过的入库单
			ErrorInfo ecOut = new ErrorInfo();
			DataSourceContextHolder.setDbName(Shared.DBName_Test);
			List<Warehousing> warehousingList = (List<Warehousing>) warehousingBO.retrieveNObject(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, warehousing);
			if (warehousingList == null) {
				return null;
			}
			Warehousing tmpWarehousing = new Warehousing();
			tmpWarehousing.setIsASC(EnumBoolean.EB_NO.getIndex());
			Collections.sort(warehousingList, tmpWarehousing);

			for (Warehousing ws : warehousingList) {
				// 在缓存中获取入库单主从表信息
				Warehousing warehousingOfCache = (Warehousing) CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_Warehousing).read1(ws.getID(), BaseBO.SYSTEM, ecOut, Shared.DBName_Test);

				for (Object o : warehousingOfCache.getListSlave1()) {
					WarehousingCommodity wsc = (WarehousingCommodity) o;
					if (wsc.getCommodityID() == commodity.getID()) {
						return wsc;
					}
				}
			}
		} else {
			Warehousing warehousing = new Warehousing();
			warehousing.setID(commodity.getCurrentWarehousingID());
			DataSourceContextHolder.setDbName(Shared.DBName_Test);
			List<List<BaseModel>> bmList = warehousingBO.retrieve1ObjectEx(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, warehousing);

			for (Object o : bmList.get(1)) {
				WarehousingCommodity warehousingCommodity = (WarehousingCommodity) o;
				if (warehousingCommodity.getCommodityID() == commodity.getID()) {
					return warehousingCommodity;
				}
			}
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	private List<Commodity> getCommodityListBefourRetail(RetailTrade retailTrade) {
		List<Commodity> commList = new ArrayList<Commodity>();
		Commodity commodity = new Commodity();
		ErrorInfo ecOut = new ErrorInfo();
		for (Object o : retailTrade.getListSlave1()) {
			RetailTradeCommodity rtc = (RetailTradeCommodity) o;

			commodity = (Commodity) CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_Commodity).read1(rtc.getCommodityID(), BaseBO.SYSTEM, ecOut, Shared.DBName_Test);
			if (commodity == null || ecOut.getErrorCode() != EnumErrorCode.EC_NoError) {
				assertTrue(false, "查询商品缓存失败，错误码：" + ecOut.getErrorCode());
			}

			List<Commodity> listSlave2 = new ArrayList<Commodity>();
			if (commodity.getType() == CommodityType.EnumCommodityType.ECT_MultiPackaging.getIndex()) {
				Commodity multiCommodity = (Commodity) CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_Commodity).read1(commodity.getRefCommodityID(), BaseBO.SYSTEM, ecOut, Shared.DBName_Test);
				if (multiCommodity == null || ecOut.getErrorCode() != EnumErrorCode.EC_NoError) {
					assertTrue(false, "查询商品缓存失败，错误码：" + ecOut.getErrorCode());
				}
				// 将多包装的单品信息放到listSlave2中
				listSlave2.add(multiCommodity);
				commodity.setListSlave2(listSlave2);
			} else if (commodity.getType() == CommodityType.EnumCommodityType.ECT_Combination.getIndex()) {
				// 获取组合商品子商品
				SubCommodity subCommodity = new SubCommodity();
				subCommodity.setCommodityID(commodity.getID());
				//
				DataSourceContextHolder.setDbName(Shared.DBName_Test);
				List<SubCommodity> listSubCommodity = (List<SubCommodity>) subCommodityBO.retrieveNObject(BaseBO.SYSTEM, BaseBO.INVALID_CASE_ID, subCommodity);
				if (subCommodityBO.getLastErrorCode() != EnumErrorCode.EC_NoError) {
					assertTrue(false, "查询子商品失败，错误码：" + retailTradeCommoditySourceBO.getLastErrorCode());
				}

				// 查询到组合商品的子商品，将单品信息放到listSlave2中
				for (SubCommodity sc : listSubCommodity) {
					Commodity subCommodityOfCache = (Commodity) CacheManager.getCache(Shared.DBName_Test, EnumCacheType.ECT_Commodity).read1(sc.getSubCommodityID(), BaseBO.SYSTEM, ecOut, Shared.DBName_Test);
					if (subCommodityOfCache == null || ecOut.getErrorCode() != EnumErrorCode.EC_NoError) {
						assertTrue(false, "查询商品缓存失败，错误码：" + ecOut.getErrorCode());
					}

					listSlave2.add(subCommodityOfCache);
				}
				commodity.setListSlave2(listSlave2);
			}
			commList.add(commodity);
		}
		return commList;
	}

}
