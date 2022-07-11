package com.bx.erp.test.report;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MvcResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.action.BaseAction;
import com.bx.erp.model.report.RetailTradeDailyReportByCategoryParent;
import com.bx.erp.test.BaseActionTest;
import com.bx.erp.test.Shared;

@WebAppConfiguration
public class RetailTradeDailyReportByCategoryParentActionTest extends BaseActionTest {

	@BeforeClass
	public void setup() {
		super.setUp();
	}

	@AfterClass
	public void tearDown() {
		super.tearDown();
	}

	@Test
	public void testRetrieveNEx() throws Exception {
		Shared.printTestMethodStartInfo();
		// RetailTradeDailyReportByCategoryParent reportByCategoryParent = new
		// RetailTradeDailyReportByCategoryParent();

		System.out.println("------------------------CASE1:查询一天----------------------");
		MvcResult mr1 = mvc.perform(//
				post("/retailTradeDailyReportByCategoryParent/retrieveNEx.bx")//
						.contentType(MediaType.APPLICATION_JSON)//
						.param(RetailTradeDailyReportByCategoryParent.field.getFIELD_NAME_datetimeStart(), "2019/01/16 00:00:00")//
						.param(RetailTradeDailyReportByCategoryParent.field.getFIELD_NAME_datetimeEnd(), "2019/01/16 23:59:59")//
						.param(RetailTradeDailyReportByCategoryParent.field.getFIELD_NAME_shopID(), String.valueOf(Shared.DEFAULT_Shop_ID))//
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr1);

		System.out.println("------------------------CASE2:查询一个月----------------------");
		MvcResult mr2 = mvc.perform(//
				post("/retailTradeDailyReportByCategoryParent/retrieveNEx.bx")//
						.contentType(MediaType.APPLICATION_JSON)//
						.param(RetailTradeDailyReportByCategoryParent.field.getFIELD_NAME_datetimeStart(), "2019/01/01 00:00:00")//
						.param(RetailTradeDailyReportByCategoryParent.field.getFIELD_NAME_datetimeEnd(), "2019/01/31 23:59:59")//
						.param(RetailTradeDailyReportByCategoryParent.field.getFIELD_NAME_shopID(), String.valueOf(Shared.DEFAULT_Shop_ID))//
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr2);

		System.out.println("------------------------CASE3:查询某个特定的时间----------------------");
		MvcResult mr3 = mvc.perform(//
				post("/retailTradeDailyReportByCategoryParent/retrieveNEx.bx")//
						.contentType(MediaType.APPLICATION_JSON)//
						.param(RetailTradeDailyReportByCategoryParent.field.getFIELD_NAME_datetimeStart(), "2019/01/16 00:00:00")//
						.param(RetailTradeDailyReportByCategoryParent.field.getFIELD_NAME_datetimeEnd(), "2019/01/17 23:59:59")//
						.param(RetailTradeDailyReportByCategoryParent.field.getFIELD_NAME_shopID(), String.valueOf(Shared.DEFAULT_Shop_ID))//
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr3);

		System.out.println("------------------------CASE4:查询不存在的时间段----------------------");
		MvcResult mr4 = mvc.perform(//
				post("/retailTradeDailyReportByCategoryParent/retrieveNEx.bx")//
						.contentType(MediaType.APPLICATION_JSON)//
						.param(RetailTradeDailyReportByCategoryParent.field.getFIELD_NAME_datetimeStart(), "2009/01/01 00:00:00")//
						.param(RetailTradeDailyReportByCategoryParent.field.getFIELD_NAME_datetimeEnd(), "2009/01/31 23:59:59")//
						.param(RetailTradeDailyReportByCategoryParent.field.getFIELD_NAME_shopID(), String.valueOf(Shared.DEFAULT_Shop_ID))//
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr4);
		
		System.out.println("------------------------CASE5:查询不存在的门店ID----------------------");
		MvcResult mr5 = mvc.perform(//
				post("/retailTradeDailyReportByCategoryParent/retrieveNEx.bx")//
						.contentType(MediaType.APPLICATION_JSON)//
						.param(RetailTradeDailyReportByCategoryParent.field.getFIELD_NAME_datetimeStart(), "2009/01/01 00:00:00")//
						.param(RetailTradeDailyReportByCategoryParent.field.getFIELD_NAME_datetimeEnd(), "2009/01/31 23:59:59")//
						.param(RetailTradeDailyReportByCategoryParent.field.getFIELD_NAME_shopID(), String.valueOf(BaseAction.INVALID_ID))//
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr5);
	}

}
