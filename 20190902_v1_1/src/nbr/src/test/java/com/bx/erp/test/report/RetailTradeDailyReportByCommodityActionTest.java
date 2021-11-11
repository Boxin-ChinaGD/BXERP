package com.bx.erp.test.report;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
import com.bx.erp.model.report.RetailTradeDailyReport;
import com.bx.erp.model.report.RetailTradeDailyReportByCommodity;
import com.bx.erp.model.report.RetailTradeDailyReportSummary;
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
public class RetailTradeDailyReportByCommodityActionTest extends BaseActionTest {

	private DateFormat df;

	@BeforeClass
	public void setup() {

		super.setUp();

		df = new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default3);
	}

	@AfterClass
	public void tearDown() {

		super.tearDown();
	}

	@Test
	public void testRetrieveN1() throws Exception {
		Shared.printTestMethodStartInfo();
		// RetailTradeDailyReportByCommodity r = new
		// RetailTradeDailyReportByCommodity();

		Shared.caseLog("Case1: 按商品销售金额降序排列");
		MvcResult mr1 = mvc.perform(//
				post("/retailTradeDailyReportByCommodity/retrieveN.bx")//
						.param(RetailTradeDailyReportByCommodity.field.getFIELD_NAME_datetimeStart(), "2018/02/11")//
						.param(RetailTradeDailyReportByCommodity.field.getFIELD_NAME_datetimeEnd(), "2018/02/11")//
						.param(RetailTradeDailyReportByCommodity.field.getFIELD_NAME_queryKeyword(), "")//
						.param(RetailTradeDailyReportByCommodity.field.getFIELD_NAME_iOrderBy(), "0")//
						.param(RetailTradeDailyReportByCommodity.field.getFIELD_NAME_isASC(), "0")//
						.param(RetailTradeDailyReportByCommodity.field.getFIELD_NAME_bIgnoreZeroNO(), "0")//
						.param(RetailTradeDailyReportByCommodity.field.getFIELD_NAME_iCategoryID(), "-1")//
						.param(RetailTradeDailyReportByCommodity.field.getFIELD_NAME_shopID(), String.valueOf(Shared.DEFAULT_Shop_ID))//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//

		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		Shared.checkJSONErrorCode(mr1);

		JSONObject o1 = JSONObject.fromObject(mr1.getResponse().getContentAsString());
		List<Integer> list1 = JsonPath.read(o1, "$.reportList[*].totalAmount");
		Integer i1 = list1.get(0);
		Integer i2 = list1.get(1);
		assertTrue(i1 >= i2);
	}

	@Test
	public void testRetrieveN2() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("Case2: 按商品销售金额升序排列");
		MvcResult mr2 = mvc.perform(//
				post("/retailTradeDailyReportByCommodity/retrieveN.bx")//
						.param(RetailTradeDailyReportByCommodity.field.getFIELD_NAME_datetimeStart(), "2018/02/11")//
						.param(RetailTradeDailyReportByCommodity.field.getFIELD_NAME_datetimeEnd(), "2018/02/11")//
						.param(RetailTradeDailyReportByCommodity.field.getFIELD_NAME_queryKeyword(), "")//
						.param(RetailTradeDailyReportByCommodity.field.getFIELD_NAME_iOrderBy(), "0")//
						.param(RetailTradeDailyReportByCommodity.field.getFIELD_NAME_isASC(), "1")//
						.param(RetailTradeDailyReportByCommodity.field.getFIELD_NAME_bIgnoreZeroNO(), "0")//
						.param(RetailTradeDailyReportByCommodity.field.getFIELD_NAME_iCategoryID(), "-1")//
						.param(RetailTradeDailyReportByCommodity.field.getFIELD_NAME_shopID(), String.valueOf(Shared.DEFAULT_Shop_ID))//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//

		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		Shared.checkJSONErrorCode(mr2);

		JSONObject o2 = JSONObject.fromObject(mr2.getResponse().getContentAsString());
		List<Integer> list2 = JsonPath.read(o2, "$.reportList[*].totalAmount");
		Integer i3 = list2.get(0);
		Integer i4 = list2.get(1);
		assertTrue(i3 <= i4);
	}

	@Test
	public void testRetrieveN3() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("Case3: 按销售数量降序排列");
		MvcResult mr3 = mvc.perform(//
				post("/retailTradeDailyReportByCommodity/retrieveN.bx")//
						.param(RetailTradeDailyReportByCommodity.field.getFIELD_NAME_datetimeStart(), "2018/02/11")//
						.param(RetailTradeDailyReportByCommodity.field.getFIELD_NAME_datetimeEnd(), "2018/02/11")//
						.param(RetailTradeDailyReportByCommodity.field.getFIELD_NAME_queryKeyword(), "")//
						.param(RetailTradeDailyReportByCommodity.field.getFIELD_NAME_iOrderBy(), "1")//
						.param(RetailTradeDailyReportByCommodity.field.getFIELD_NAME_isASC(), "0")//
						.param(RetailTradeDailyReportByCommodity.field.getFIELD_NAME_bIgnoreZeroNO(), "0")//
						.param(RetailTradeDailyReportByCommodity.field.getFIELD_NAME_iCategoryID(), "-1")//
						.param(RetailTradeDailyReportByCommodity.field.getFIELD_NAME_shopID(), String.valueOf(Shared.DEFAULT_Shop_ID))//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//

		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		Shared.checkJSONErrorCode(mr3);

		JSONObject o3 = JSONObject.fromObject(mr3.getResponse().getContentAsString());
		List<Integer> list3 = JsonPath.read(o3, "$.reportList[*].NO");
		Integer i5 = list3.get(0);
		Integer i6 = list3.get(1);
		assertTrue(i5 >= i6);
	}

	@Test
	public void testRetrieveN4() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("Case4: 按毛利降序排列");
		MvcResult mr4 = mvc.perform(//
				post("/retailTradeDailyReportByCommodity/retrieveN.bx")//
						.param(RetailTradeDailyReportByCommodity.field.getFIELD_NAME_datetimeStart(), "2018/02/11")//
						.param(RetailTradeDailyReportByCommodity.field.getFIELD_NAME_datetimeEnd(), "2018/02/11")//
						.param(RetailTradeDailyReportByCommodity.field.getFIELD_NAME_queryKeyword(), "")//
						.param(RetailTradeDailyReportByCommodity.field.getFIELD_NAME_iOrderBy(), "2")//
						.param(RetailTradeDailyReportByCommodity.field.getFIELD_NAME_isASC(), "0")//
						.param(RetailTradeDailyReportByCommodity.field.getFIELD_NAME_bIgnoreZeroNO(), "0")//
						.param(RetailTradeDailyReportByCommodity.field.getFIELD_NAME_iCategoryID(), "-1")//
						.param(RetailTradeDailyReportByCommodity.field.getFIELD_NAME_shopID(), String.valueOf(Shared.DEFAULT_Shop_ID))//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//

		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		Shared.checkJSONErrorCode(mr4);

		JSONObject o4 = JSONObject.fromObject(mr4.getResponse().getContentAsString());
		List<Integer> list4 = JsonPath.read(o4, "$.reportList[*].grossMargin");
		Integer i7 = list4.get(0);
		Integer i8 = list4.get(1);
		assertTrue(i7 >= i8);
	}

	@Test
	public void testRetrieveN5() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("Case5: 按名称进行模糊查询");
		MvcResult mr5 = mvc.perform(//
				post("/retailTradeDailyReportByCommodity/retrieveN.bx")//
						.param(RetailTradeDailyReportByCommodity.field.getFIELD_NAME_datetimeStart(), "2018/02/11")//
						.param(RetailTradeDailyReportByCommodity.field.getFIELD_NAME_datetimeEnd(), "2018/02/11")//
						.param(RetailTradeDailyReportByCommodity.field.getFIELD_NAME_queryKeyword(), "可")//
						.param(RetailTradeDailyReportByCommodity.field.getFIELD_NAME_iOrderBy(), "0")//
						.param(RetailTradeDailyReportByCommodity.field.getFIELD_NAME_isASC(), "0")//
						.param(RetailTradeDailyReportByCommodity.field.getFIELD_NAME_bIgnoreZeroNO(), "0")//
						.param(RetailTradeDailyReportByCommodity.field.getFIELD_NAME_iCategoryID(), "-1")//
						.param(RetailTradeDailyReportByCommodity.field.getFIELD_NAME_shopID(), String.valueOf(Shared.DEFAULT_Shop_ID))//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//

		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		Shared.checkJSONErrorCode(mr5); // ...json结果未进行校验
	}

	@Test
	public void testRetrieveN6() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("Case6: 按条形码进行精确查询");
		MvcResult mr6 = mvc.perform(//
				post("/retailTradeDailyReportByCommodity/retrieveN.bx")//
						.param(RetailTradeDailyReportByCommodity.field.getFIELD_NAME_datetimeStart(), "2018/02/11")//
						.param(RetailTradeDailyReportByCommodity.field.getFIELD_NAME_datetimeEnd(), "2018/02/11")//
						.param(RetailTradeDailyReportByCommodity.field.getFIELD_NAME_queryKeyword(), "3548293894545")//
						.param(RetailTradeDailyReportByCommodity.field.getFIELD_NAME_iOrderBy(), "0")//
						.param(RetailTradeDailyReportByCommodity.field.getFIELD_NAME_isASC(), "0")//
						.param(RetailTradeDailyReportByCommodity.field.getFIELD_NAME_bIgnoreZeroNO(), "0")//
						.param(RetailTradeDailyReportByCommodity.field.getFIELD_NAME_iCategoryID(), "-1")//
						.param(RetailTradeDailyReportByCommodity.field.getFIELD_NAME_shopID(), String.valueOf(Shared.DEFAULT_Shop_ID))//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//

		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		Shared.checkJSONErrorCode(mr6);

		JSONObject o6 = JSONObject.fromObject(mr6.getResponse().getContentAsString());
		List<String> list6 = JsonPath.read(o6, "$.reportList[*].barcode");
		for (int i = 0; i < list6.size(); i++) {
			assertTrue("3548293894545".equals(list6.get(i)));
		}
	}

	@Test
	public void testRetrieveN7() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("Case7: 按单号进行查询");
		MvcResult mr7 = mvc.perform(//
				post("/retailTradeDailyReportByCommodity/retrieveN.bx")//
						.param(RetailTradeDailyReportByCommodity.field.getFIELD_NAME_datetimeStart(), "2018/02/11")//
						.param(RetailTradeDailyReportByCommodity.field.getFIELD_NAME_datetimeEnd(), "2018/02/11")//
						.param(RetailTradeDailyReportByCommodity.field.getFIELD_NAME_queryKeyword(), "1")//
						.param(RetailTradeDailyReportByCommodity.field.getFIELD_NAME_iOrderBy(), "0")//
						.param(RetailTradeDailyReportByCommodity.field.getFIELD_NAME_isASC(), "0")//
						.param(RetailTradeDailyReportByCommodity.field.getFIELD_NAME_bIgnoreZeroNO(), "0")//
						.param(RetailTradeDailyReportByCommodity.field.getFIELD_NAME_iCategoryID(), "-1")//
						.param(RetailTradeDailyReportByCommodity.field.getFIELD_NAME_shopID(), String.valueOf(Shared.DEFAULT_Shop_ID))//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//

		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		Shared.checkJSONErrorCode(mr7);

		JSONObject o7 = JSONObject.fromObject(mr7.getResponse().getContentAsString());
		List<Integer> list7 = JsonPath.read(o7, "$.reportList[*].ID");
		for (int i = 0; i < list7.size(); i++) {
			assertTrue(list7.get(i) == 1);
		}
	}

	@Test
	public void testRetrieveN8() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("Case8: bIgnoreZeroNO=1忽略0进货量");
		MvcResult mr8 = mvc.perform(//
				post("/retailTradeDailyReportByCommodity/retrieveN.bx")//
						.param(RetailTradeDailyReportByCommodity.field.getFIELD_NAME_datetimeStart(), "2018/02/11")//
						.param(RetailTradeDailyReportByCommodity.field.getFIELD_NAME_datetimeEnd(), "2018/02/11")//
						.param(RetailTradeDailyReportByCommodity.field.getFIELD_NAME_queryKeyword(), "")//
						.param(RetailTradeDailyReportByCommodity.field.getFIELD_NAME_iOrderBy(), "0")//
						.param(RetailTradeDailyReportByCommodity.field.getFIELD_NAME_isASC(), "0")//
						.param(RetailTradeDailyReportByCommodity.field.getFIELD_NAME_bIgnoreZeroNO(), "1")//
						.param(RetailTradeDailyReportByCommodity.field.getFIELD_NAME_iCategoryID(), "-1")//
						.param(RetailTradeDailyReportByCommodity.field.getFIELD_NAME_shopID(), String.valueOf(Shared.DEFAULT_Shop_ID))//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//

		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		Shared.checkJSONErrorCode(mr8); // ...json结果未进行校验
	}

	@Test
	public void testRetrieveN9() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("Case9: 按照类别进行查询");
		MvcResult mr9 = mvc.perform(//
				post("/retailTradeDailyReportByCommodity/retrieveN.bx")//
						.param(RetailTradeDailyReportByCommodity.field.getFIELD_NAME_datetimeStart(), "2018/02/11")//
						.param(RetailTradeDailyReportByCommodity.field.getFIELD_NAME_datetimeEnd(), "2018/02/11")//
						.param(RetailTradeDailyReportByCommodity.field.getFIELD_NAME_queryKeyword(), "")//
						.param(RetailTradeDailyReportByCommodity.field.getFIELD_NAME_iOrderBy(), "0")//
						.param(RetailTradeDailyReportByCommodity.field.getFIELD_NAME_isASC(), "0")//
						.param(RetailTradeDailyReportByCommodity.field.getFIELD_NAME_bIgnoreZeroNO(), "0")//
						.param(RetailTradeDailyReportByCommodity.field.getFIELD_NAME_iCategoryID(), "1")//
						.param(RetailTradeDailyReportByCommodity.field.getFIELD_NAME_shopID(), String.valueOf(Shared.DEFAULT_Shop_ID))//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//

		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		Shared.checkJSONErrorCode(mr9);

		JSONObject o9 = JSONObject.fromObject(mr9.getResponse().getContentAsString());
		List<Integer> list9 = JsonPath.read(o9, "$.reportList[*].commodityID");
		for (int i = 0; i < list9.size(); i++) {
			assertTrue(list9.get(i) == 1);
		}
	}

	@Test
	public void testRetrieveN10() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("Case10: 查询一段时间的数据 按商品销售金额降序排列");
		MvcResult mr10 = mvc.perform(//
				post("/retailTradeDailyReportByCommodity/retrieveN.bx")//
						.param(RetailTradeDailyReportByCommodity.field.getFIELD_NAME_datetimeStart(), "2018/02/11")//
						.param(RetailTradeDailyReportByCommodity.field.getFIELD_NAME_datetimeEnd(), "2018/02/14")//
						.param(RetailTradeDailyReportByCommodity.field.getFIELD_NAME_queryKeyword(), "")//
						.param(RetailTradeDailyReportByCommodity.field.getFIELD_NAME_iOrderBy(), "0")//
						.param(RetailTradeDailyReportByCommodity.field.getFIELD_NAME_isASC(), "0")//
						.param(RetailTradeDailyReportByCommodity.field.getFIELD_NAME_bIgnoreZeroNO(), "0")//
						.param(RetailTradeDailyReportByCommodity.field.getFIELD_NAME_iCategoryID(), "-1")//
						.param(RetailTradeDailyReportByCommodity.field.getFIELD_NAME_shopID(), String.valueOf(Shared.DEFAULT_Shop_ID))//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//

		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		Shared.checkJSONErrorCode(mr10);

		JSONObject o10 = JSONObject.fromObject(mr10.getResponse().getContentAsString());
		List<Integer> list10 = JsonPath.read(o10, "$.reportList[*].totalAmount");
		Integer i10 = list10.get(0);
		Integer i11 = list10.get(1);
		assertTrue(i10 >= i11);
	}

	@Test
	public void testRetrieveN11() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("Case11: 查询不存在时间的数据");
		MvcResult mr11 = mvc.perform(//
				post("/retailTradeDailyReportByCommodity/retrieveN.bx")//
						.param(RetailTradeDailyReportByCommodity.field.getFIELD_NAME_datetimeStart(), "2025/02/11")//
						.param(RetailTradeDailyReportByCommodity.field.getFIELD_NAME_datetimeEnd(), "2025/02/14")//
						.param(RetailTradeDailyReportByCommodity.field.getFIELD_NAME_queryKeyword(), "")//
						.param(RetailTradeDailyReportByCommodity.field.getFIELD_NAME_iOrderBy(), "0")//
						.param(RetailTradeDailyReportByCommodity.field.getFIELD_NAME_isASC(), "0")//
						.param(RetailTradeDailyReportByCommodity.field.getFIELD_NAME_bIgnoreZeroNO(), "0")//
						.param(RetailTradeDailyReportByCommodity.field.getFIELD_NAME_iCategoryID(), "-1")//
						.param(RetailTradeDailyReportByCommodity.field.getFIELD_NAME_shopID(), String.valueOf(Shared.DEFAULT_Shop_ID))//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//

		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		Shared.checkJSONErrorCode(mr11); // ...
	}

	@Test
	public void retrieveNTest_CASE1() throws UnsupportedEncodingException, Exception {
		Shared.printTestMethodStartInfo();

		System.out.println("------------Case1： ：根据输入_特殊字符商品名称查询 商品零售日报表 ------------");
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
		// 创建日报表
		RetailTradeDailyReport retailTradeDailyReport = new RetailTradeDailyReport();
		retailTradeDailyReport.setSaleDatetime(df.parse(df.format(new Date())));
		retailTradeDailyReport.setDeleteOldData(1);
		retailTradeDailyReport.setShopID(Shared.DEFAULT_Shop_ID);
		//
		String errorMsg = retailTradeDailyReport.checkCreate(BaseBO.INVALID_CASE_ID);
		assertEquals(errorMsg, "");
		//
		createExRetailTradeDailyReport(retailTradeDailyReport);

		// 按名称进行模糊查询
		MvcResult mr = mvc.perform(//
				post("/retailTradeDailyReportByCommodity/retrieveN.bx")//
						.param(RetailTradeDailyReportByCommodity.field.getFIELD_NAME_datetimeStart(), "2000/07/19 00:00:00")//
						.param(RetailTradeDailyReportByCommodity.field.getFIELD_NAME_datetimeEnd(), String.valueOf(df.format(new Date())))//
						.param(RetailTradeDailyReportByCommodity.field.getFIELD_NAME_queryKeyword(), "_")//
						.param(RetailTradeDailyReportByCommodity.field.getFIELD_NAME_iOrderBy(), "0")//
						.param(RetailTradeDailyReportByCommodity.field.getFIELD_NAME_isASC(), "0")//
						.param(RetailTradeDailyReportByCommodity.field.getFIELD_NAME_bIgnoreZeroNO(), "0")//
						.param(RetailTradeDailyReportByCommodity.field.getFIELD_NAME_iCategoryID(), "-1")//
						.param(RetailTradeDailyReportByCommodity.field.getFIELD_NAME_shopID(), String.valueOf(Shared.DEFAULT_Shop_ID))//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//

		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		Shared.checkJSONErrorCode(mr);

		JSONObject o = JSONObject.fromObject(mr.getResponse().getContentAsString());
		List<String> lsitName = JsonPath.read(o, "$.reportList[*].name");
		for (String result : lsitName) {
			Assert.assertTrue(result.contains("_"), "查询商品名称没有包含_特殊字符");
		}

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

	private List<List<BaseModel>> createExRetailTradeDailyReport(RetailTradeDailyReport retailTradeDailyReport) {
		String err = retailTradeDailyReport.checkCreate(BaseBO.INVALID_CASE_ID); // Model中实际上没有对该字段的检查
		Assert.assertEquals(err, "");
		//
		Map<String, Object> params = retailTradeDailyReport.getCreateParamEx(BaseBO.INVALID_CASE_ID, retailTradeDailyReport);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<List<BaseModel>> list = retailTradeDailyReportMapper.createEx(params);
		if (list.size() > 0) { // 创建成功
			Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
			//
			for (List<BaseModel> bmList : list) {
				for (BaseModel bm : bmList) {
					if (bm instanceof RetailTradeDailyReport) {
						err = ((RetailTradeDailyReport) bm).checkCreate(BaseBO.INVALID_CASE_ID);
						Assert.assertEquals(err, "");
					} else if (bm instanceof RetailTradeDailyReportSummary) {
						err = ((RetailTradeDailyReportSummary) bm).checkCreate(BaseBO.INVALID_CASE_ID);
						Assert.assertEquals(err, "");
					} else {
						err = ((RetailTradeDailyReportByCommodity) bm).checkCreate(BaseBO.INVALID_CASE_ID);
						Assert.assertEquals(err, "");
					}
				}
			}
			//
			System.out.println("创建对象成功： " + list);
		} else { // 创建失败
			Assert.assertTrue(list.size() == 0 && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
			//
			System.out.println("创建对象失败： " + list);
		}
		return list;
	}

}
