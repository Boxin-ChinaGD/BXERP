package com.bx.erp.test.report;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
import com.bx.erp.model.report.RetailTradeDailyReportByStaff;
import com.bx.erp.test.BaseActionTest;
import com.bx.erp.test.Shared;
import com.jayway.jsonpath.JsonPath;

import net.sf.json.JSONObject;

@WebAppConfiguration
public class RetailTradeDailyReportByStaffActionTest extends BaseActionTest {

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

		System.out.println("\n-------------------------Case1: 查询一天的员工数据----------------------");
		MvcResult mr1 = mvc.perform(//
				post("/retailTradeDailyReportByStaff/retrieveNEx.bx")//
						.param(RetailTradeDailyReportByStaff.field.getFIELD_NAME_datetimeStart(), "2019/01/10")//
						.param(RetailTradeDailyReportByStaff.field.getFIELD_NAME_datetimeEnd(), "2019/01/10")//
						.param(RetailTradeDailyReportByStaff.field.getFIELD_NAME_shopID(), String.valueOf(Shared.DEFAULT_Shop_ID))//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//

		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		Shared.checkJSONErrorCode(mr1);

		JSONObject o = JSONObject.fromObject(mr1.getResponse().getContentAsString());
		List<Integer> rtList = JsonPath.read(o, "$.retailTradeDailyReportByStaff[*].NO");
		for (Integer rtNO : rtList) {
			Assert.assertTrue(rtNO > 0, "查询失败");
		}

		System.out.println("\n-------------------------Case2: 查询存在的时间段的员工数据 ----------------------");
		MvcResult mr2 = mvc.perform(//
				post("/retailTradeDailyReportByStaff/retrieveNEx.bx")//
						.param(RetailTradeDailyReportByStaff.field.getFIELD_NAME_datetimeStart(), "2019/01/10")//
						.param(RetailTradeDailyReportByStaff.field.getFIELD_NAME_datetimeEnd(), "2019/01/15")//
						.param(RetailTradeDailyReportByStaff.field.getFIELD_NAME_shopID(), String.valueOf(Shared.DEFAULT_Shop_ID))//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//

		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		Shared.checkJSONErrorCode(mr2);

		JSONObject o2 = JSONObject.fromObject(mr2.getResponse().getContentAsString());
		List<Integer> rtList2 = JsonPath.read(o2, "$.retailTradeDailyReportByStaff[*].NO");
		for (Integer rtNO : rtList2) {
			Assert.assertTrue(rtNO > 0, "查询失败");
		}

		System.out.println("\n-------------------------Case3: 查询不存在的时间段----------------------");
		MvcResult mr3 = mvc.perform(//
				post("/retailTradeDailyReportByStaff/retrieveNEx.bx")//
						.param(RetailTradeDailyReportByStaff.field.getFIELD_NAME_datetimeStart(), "2099/01/10")//
						.param(RetailTradeDailyReportByStaff.field.getFIELD_NAME_datetimeEnd(), "2099/01/10")//
						.param(RetailTradeDailyReportByStaff.field.getFIELD_NAME_shopID(), String.valueOf(Shared.DEFAULT_Shop_ID))//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//

		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		Shared.checkJSONErrorCode(mr3);
		
		System.out.println("\n-------------------------Case4: 查询不存在的门店ID----------------------");
		MvcResult mr4 = mvc.perform(//
				post("/retailTradeDailyReportByStaff/retrieveNEx.bx")//
						.param(RetailTradeDailyReportByStaff.field.getFIELD_NAME_datetimeStart(), "2099/01/10")//
						.param(RetailTradeDailyReportByStaff.field.getFIELD_NAME_datetimeEnd(), "2099/01/10")//
						.param(RetailTradeDailyReportByStaff.field.getFIELD_NAME_shopID(), String.valueOf(BaseAction.INVALID_ID))//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//

		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		Shared.checkJSONErrorCode(mr4);

	}

}
