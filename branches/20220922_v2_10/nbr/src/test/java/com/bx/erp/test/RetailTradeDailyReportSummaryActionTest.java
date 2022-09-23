package com.bx.erp.test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MvcResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.action.BaseAction;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.report.RetailTradeDailyReportSummary;

import net.sf.json.JSONObject;

@WebAppConfiguration
public class RetailTradeDailyReportSummaryActionTest extends BaseActionTest {

	@BeforeClass
	public void setup() {
		super.setUp();
	}

	@AfterClass
	public void tearDown() {
		super.tearDown();
	}

	@Test
	public void testRetrieve1() throws Exception {
		Shared.printTestMethodStartInfo();

		// RetailTradeDailyReportSummary dr = new RetailTradeDailyReportSummary();
		System.out.println("\n---------------------------------case1:查询一天的数据-----------------------------------------");
		MvcResult mr1 = mvc.perform( //
				get("/retailTradeDailyReportSummary/retrieve1Ex.bx") //
						.param(RetailTradeDailyReportSummary.field.getFIELD_NAME_datetimeStart(), "2019/01/14 00:00:00") //
						.param(RetailTradeDailyReportSummary.field.getFIELD_NAME_datetimeEnd(), "2019/01/14 23:59:59") //
						.param(RetailTradeDailyReportSummary.field.getFIELD_NAME_shopID(), String.valueOf(Shared.DEFAULT_Shop_ID)) //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//
		).andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();

		Shared.checkJSONErrorCode(mr1);

		JSONObject object = JSONObject.fromObject(mr1.getResponse().getContentAsString());
		System.out.println(object);

		System.out.println("\n---------------------------------case2:查询一个月的数据-----------------------------------------");
		MvcResult mr2 = mvc.perform( //
				get("/retailTradeDailyReportSummary/retrieve1Ex.bx") //
						.param(RetailTradeDailyReportSummary.field.getFIELD_NAME_datetimeStart(), "2019/01/01 00:00:00") //
						.param(RetailTradeDailyReportSummary.field.getFIELD_NAME_datetimeEnd(), "2019/01/31 23:59:59") //
						.param(RetailTradeDailyReportSummary.field.getFIELD_NAME_shopID(), String.valueOf(Shared.DEFAULT_Shop_ID)) //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//
		).andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();

		Shared.checkJSONErrorCode(mr2);

		System.out.println("\n---------------------------------case3:查询某一时间段的数据-----------------------------------------");
		MvcResult mr3 = mvc.perform( //
				get("/retailTradeDailyReportSummary/retrieve1Ex.bx") //
						.param(RetailTradeDailyReportSummary.field.getFIELD_NAME_datetimeStart(), "2019/01/01 00:00:00") //
						.param(RetailTradeDailyReportSummary.field.getFIELD_NAME_datetimeEnd(), "2019/01/15 23:59:59") //
						.param(RetailTradeDailyReportSummary.field.getFIELD_NAME_shopID(), String.valueOf(Shared.DEFAULT_Shop_ID)) //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//
		).andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();

		Shared.checkJSONErrorCode(mr3);

		System.out.println("\n---------------------------------case4:查询一个不存在时间的数据-----------------------------------------");
		MvcResult mr4 = mvc.perform( //
				get("/retailTradeDailyReportSummary/retrieve1Ex.bx") //
						.param(RetailTradeDailyReportSummary.field.getFIELD_NAME_datetimeStart(), "2020/01/01 00:00:00") //
						.param(RetailTradeDailyReportSummary.field.getFIELD_NAME_datetimeEnd(), "2020/01/31 23:59:59") //
						.param(RetailTradeDailyReportSummary.field.getFIELD_NAME_shopID(), String.valueOf(Shared.DEFAULT_Shop_ID)) //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//
		).andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();

		Shared.checkJSONErrorCode(mr4, EnumErrorCode.EC_NoSuchData);
		
		System.out.println("\n---------------------------------case5:查询一个不存在门店ID的数据-----------------------------------------");
		MvcResult mr5 = mvc.perform( //
				get("/retailTradeDailyReportSummary/retrieve1Ex.bx") //
						.param(RetailTradeDailyReportSummary.field.getFIELD_NAME_datetimeStart(), "2020/01/01 00:00:00") //
						.param(RetailTradeDailyReportSummary.field.getFIELD_NAME_datetimeEnd(), "2020/01/31 23:59:59") //
						.param(RetailTradeDailyReportSummary.field.getFIELD_NAME_shopID(), String.valueOf(BaseAction.INVALID_ID)) //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//
		).andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();

		Shared.checkJSONErrorCode(mr5, EnumErrorCode.EC_NoSuchData);

	}

	@Test
	public void testRetrieveNForChart() throws Exception {
		Shared.printTestMethodStartInfo();

		// RetailTradeDailyReportSummary dr = new RetailTradeDailyReportSummary();

		System.out.println("\n---------------------------------case1:查询一天的销售总额、销售毛利及日期-----------------------------------------");
		MvcResult mr1 = mvc.perform( //
				get("/retailTradeDailyReportSummary/retrieveNForChart.bx") //
						.param(RetailTradeDailyReportSummary.field.getFIELD_NAME_datetimeStart(), "2019/01/14 00:00:00") //
						.param(RetailTradeDailyReportSummary.field.getFIELD_NAME_datetimeEnd(), "2019/01/14 23:59:59") //
						.param(RetailTradeDailyReportSummary.field.getFIELD_NAME_shopID(), String.valueOf(Shared.DEFAULT_Shop_ID)) //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//
		).andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();

		Shared.checkJSONErrorCode(mr1);

		JSONObject object = JSONObject.fromObject(mr1.getResponse().getContentAsString());
		System.out.println(object);

		System.out.println("\n---------------------------------case2:查询一个月内的销售总额、销售毛利及日期-----------------------------------------");
		MvcResult mr2 = mvc.perform( //
				get("/retailTradeDailyReportSummary/retrieveNForChart.bx") //
						.param(RetailTradeDailyReportSummary.field.getFIELD_NAME_datetimeStart(), "2019/01/01 00:00:00") //
						.param(RetailTradeDailyReportSummary.field.getFIELD_NAME_datetimeEnd(), "2019/01/31 23:59:59") //
						.param(RetailTradeDailyReportSummary.field.getFIELD_NAME_shopID(), String.valueOf(Shared.DEFAULT_Shop_ID)) //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//
		).andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();

		Shared.checkJSONErrorCode(mr2);

		JSONObject object2 = JSONObject.fromObject(mr2.getResponse().getContentAsString());
		System.out.println(object2);

		System.out.println("\n---------------------------------case3:查询不存在的时间内的销售总额、销售毛利及日期-----------------------------------------");
		MvcResult mr3 = mvc.perform( //
				get("/retailTradeDailyReportSummary/retrieveNForChart.bx") //
						.param(RetailTradeDailyReportSummary.field.getFIELD_NAME_datetimeStart(), "2099/01/01 00:00:00") //
						.param(RetailTradeDailyReportSummary.field.getFIELD_NAME_datetimeEnd(), "2099/01/31 23:59:59") //
						.param(RetailTradeDailyReportSummary.field.getFIELD_NAME_shopID(), String.valueOf(Shared.DEFAULT_Shop_ID)) //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//
		).andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();

		Shared.checkJSONErrorCode(mr3);

		JSONObject object3 = JSONObject.fromObject(mr3.getResponse().getContentAsString());
		System.out.println(object3);
		
		System.out.println("\n---------------------------------case4:查询一个不存在的门店ID的销售总额和销售毛利-----------------------------------------");
		MvcResult mr4 = mvc.perform( //
				get("/retailTradeDailyReportSummary/retrieveNForChart.bx") //
						.param(RetailTradeDailyReportSummary.field.getFIELD_NAME_datetimeStart(), "2019/01/01 00:00:00") //
						.param(RetailTradeDailyReportSummary.field.getFIELD_NAME_datetimeEnd(), "2019/01/31 23:59:59") //
						.param(RetailTradeDailyReportSummary.field.getFIELD_NAME_shopID(), String.valueOf(BaseAction.INVALID_ID)) //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//
		).andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();

		Shared.checkJSONErrorCode(mr4);

		JSONObject object4 = JSONObject.fromObject(mr4.getResponse().getContentAsString());
		System.out.println(object4);
	}
}
