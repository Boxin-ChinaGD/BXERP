package com.bx.erp.test.report;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.text.SimpleDateFormat;
import java.util.Date;
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
import com.bx.erp.model.report.RetailTradeMonthlyReportSummary;
import com.bx.erp.test.BaseActionTest;
import com.bx.erp.test.Shared;
import com.bx.erp.util.DatetimeUtil;
import com.jayway.jsonpath.JsonPath;

import net.sf.json.JSONObject;

@WebAppConfiguration
public class RetailTradeMonthlyReportSummaryActionTest extends BaseActionTest {

	@BeforeClass
	public void setup() {
		super.setUp();
	}

	@AfterClass
	public void tearDown() {
		super.tearDown();
	}

	@Test
	public void testRetrieveN() throws Exception {
		Shared.printTestMethodStartInfo();
		SimpleDateFormat sdf = new SimpleDateFormat(BaseAction.DATETIME_FORMAT_Default2);

		System.out.println("\n-------------------------Case1: 查询一年内的数据----------------------");
		MvcResult mr1 = mvc.perform(//
				post("/retailTradeMonthlyReportSummary/retrieveN.bx")//
						.param(RetailTradeMonthlyReportSummary.field.getFIELD_NAME_datetimeStart(), sdf.parse("2019-01-01 00:00:00") + "")//
						.param(RetailTradeMonthlyReportSummary.field.getFIELD_NAME_datetimeEnd(), sdf.parse("2019-12-31 23:59:59") + "")//
						.param(RetailTradeMonthlyReportSummary.field.getFIELD_NAME_shopID(), String.valueOf(Shared.DEFAULT_Shop_ID))//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//

		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		Shared.checkJSONErrorCode(mr1);

		JSONObject o = JSONObject.fromObject(mr1.getResponse().getContentAsString());
		List<String> dateList = JsonPath.read(o, "$.reportList[*].dateTime");

		String time1 = "2019-01-01 00:00:00";
		String time2 = "2019-12-31 23:59:59";
		Date date11 = sdf.parse(time1);
		Date date22 = sdf.parse(time2);
		String time = dateList.get(0).substring(0, 10);
		for (int i = 0; i < dateList.size(); i++) {
			if (time.equals(time1)) {
				continue;
			} else {
				Date d1 = sdf.parse(dateList.get(i) + "");
				Assert.assertTrue(DatetimeUtil.compareDate(date11, d1) || date11.before(d1) && date22.after(d1));
			}
		}

		System.out.println("\n-------------------------Case2: 查询不存在的时间段----------------------");
		MvcResult mr2 = mvc.perform(//
				post("/retailTradeMonthlyReportSummary/retrieveN.bx")//
						.param(RetailTradeMonthlyReportSummary.field.getFIELD_NAME_datetimeStart(), sdf.parse("2099-01-01 00:00:00") + "")//
						.param(RetailTradeMonthlyReportSummary.field.getFIELD_NAME_datetimeEnd(), sdf.parse("2099-12-31 23:59:59") + "")//
						.param(RetailTradeMonthlyReportSummary.field.getFIELD_NAME_shopID(), String.valueOf(Shared.DEFAULT_Shop_ID))//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//

		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		Shared.checkJSONErrorCode(mr2);
		
		System.out.println("\n-------------------------Case3: 查询不存在的门店ID----------------------");
		MvcResult mr3 = mvc.perform(//
				post("/retailTradeMonthlyReportSummary/retrieveN.bx")//
						.param(RetailTradeMonthlyReportSummary.field.getFIELD_NAME_datetimeStart(), sdf.parse("2099-01-01 00:00:00") + "")//
						.param(RetailTradeMonthlyReportSummary.field.getFIELD_NAME_datetimeEnd(), sdf.parse("2099-12-31 23:59:59") + "")//
						.param(RetailTradeMonthlyReportSummary.field.getFIELD_NAME_shopID(), String.valueOf(BaseAction.INVALID_ID))//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//

		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		Shared.checkJSONErrorCode(mr3);
	}
	
}
