package com.bx.erp.test.report;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.servlet.http.HttpSession;

import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MvcResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.model.report.WarehousingReport;
import com.bx.erp.test.BaseActionTest;
import com.bx.erp.test.Shared;

@WebAppConfiguration
public class WarehousingReportActionTest extends BaseActionTest {

	private HttpSession getSession() throws Exception {
		// WarehousingReport wr = new WarehousingReport();

		MvcResult mr1 = mvc.perform(//
				post("/warehousingReport/retrieveNEx.bx")//
						.param(WarehousingReport.field.getFIELD_NAME_requestNO(), "1")//
						.param(WarehousingReport.field.getFIELD_NAME_date1(), "2018/01/01").param(WarehousingReport.field.getFIELD_NAME_date2(), "2020/01/02")//
						.param(WarehousingReport.field.getFIELD_NAME_iOrderBy(), "0")//
						.param(WarehousingReport.field.getFIELD_NAME_isASC(), "0")//
						.param(WarehousingReport.field.getFIELD_NAME_iCategoryID(), "-1")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		Shared.checkJSONErrorCode(mr1);

		return mr1.getRequest().getSession();// ...
	}

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
		// WarehousingReport wr = new WarehousingReport();

		System.out.println("------------------------CASE1:传int1=0，只查询进货报表对应的表头数据----------------------");
		MvcResult mr = mvc.perform(//
				post("/warehousingReport/retrieveNEx.bx")//

						.param(WarehousingReport.field.getFIELD_NAME_date1(), "2018/01/01").param(WarehousingReport.field.getFIELD_NAME_date2(), "2020/01/02")//
						.param(WarehousingReport.field.getFIELD_NAME_requestNO(), "0")//
						.param(WarehousingReport.field.getFIELD_NAME_iOrderBy(), "2")//
						.param(WarehousingReport.field.getFIELD_NAME_isASC(), "0")//
						.param(WarehousingReport.field.getFIELD_NAME_iCategoryID(), "-1")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) getSession())//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		Shared.checkJSONErrorCode(mr);

		System.out.println("------------------------CASE2:传int1查询表格数据，只查询进货报表对应的表格数据----------------------");
		MvcResult mr1 = mvc.perform(//
				post("/warehousingReport/retrieveNEx.bx")//
						.param(WarehousingReport.field.getFIELD_NAME_requestNO(), "1")//
						.param(WarehousingReport.field.getFIELD_NAME_date1(), "2018/01/01")//
						.param(WarehousingReport.field.getFIELD_NAME_date2(), "2020/01/02")//
						.param(WarehousingReport.field.getFIELD_NAME_iOrderBy(), "0")//
						.param(WarehousingReport.field.getFIELD_NAME_isASC(), "0")//
						.param(WarehousingReport.field.getFIELD_NAME_iCategoryID(), "-1")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		Shared.checkJSONErrorCode(mr1);

		System.out.println("------------------------CASE3:传入商品名称进行模糊查询----------------------");
		MvcResult mr2 = mvc.perform(//
				post("/warehousingReport/retrieveNEx.bx")//
						.param(WarehousingReport.field.getFIELD_NAME_requestNO(), "1")//
						.param(WarehousingReport.field.getFIELD_NAME_date1(), "2018/01/01")//
						.param(WarehousingReport.field.getFIELD_NAME_date2(), "2020/01/02")//
						.param(WarehousingReport.field.getFIELD_NAME_queryKeyword(), "可乐").param(WarehousingReport.field.getFIELD_NAME_iOrderBy(), "0")//
						.param(WarehousingReport.field.getFIELD_NAME_isASC(), "0")//
						.param(WarehousingReport.field.getFIELD_NAME_iCategoryID(), "-1")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		Shared.checkJSONErrorCode(mr2);

		System.out.println("------------------------CASE4:传入条形码进行模糊查询----------------------");
		MvcResult mr3 = mvc.perform(//
				post("/warehousingReport/retrieveNEx.bx")//
						.param(WarehousingReport.field.getFIELD_NAME_requestNO(), "1")//
						.param(WarehousingReport.field.getFIELD_NAME_date1(), "2018/01/01")//
						.param(WarehousingReport.field.getFIELD_NAME_date2(), "2020/01/02")//
						.param(WarehousingReport.field.getFIELD_NAME_queryKeyword(), "123456789").param(WarehousingReport.field.getFIELD_NAME_iOrderBy(), "0")//
						.param(WarehousingReport.field.getFIELD_NAME_isASC(), "0")//
						.param(WarehousingReport.field.getFIELD_NAME_iCategoryID(), "-1")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		Shared.checkJSONErrorCode(mr3);

		System.out.println("------------------------CASE5:传入商品名称进行模糊查询,传入bIgnoreZeroNO=1过滤0库存----------------------");
		MvcResult mr4 = mvc.perform(//
				post("/warehousingReport/retrieveNEx.bx")//
						.param(WarehousingReport.field.getFIELD_NAME_requestNO(), "1")//
						.param(WarehousingReport.field.getFIELD_NAME_date1(), "2018/01/01")//
						.param(WarehousingReport.field.getFIELD_NAME_date2(), "2020/01/02")//
						.param(WarehousingReport.field.getFIELD_NAME_bIgnoreZeroNO(), "1")//
						.param(WarehousingReport.field.getFIELD_NAME_queryKeyword(), "雪晶灵肌密水").param(WarehousingReport.field.getFIELD_NAME_iOrderBy(), "0")//
						.param(WarehousingReport.field.getFIELD_NAME_isASC(), "0")//
						.param(WarehousingReport.field.getFIELD_NAME_iCategoryID(), "-1")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		Shared.checkJSONErrorCode(mr4);

		System.out.println("------------------------CASE6:传int3查询表格数据，按照商品类别查询----------------------");
		MvcResult mr5 = mvc.perform(//
				post("/warehousingReport/retrieveNEx.bx")//
						.param(WarehousingReport.field.getFIELD_NAME_requestNO(), "1")//
						.param(WarehousingReport.field.getFIELD_NAME_date1(), "2018/01/01")//
						.param(WarehousingReport.field.getFIELD_NAME_date2(), "2020/01/02")//
						.param(WarehousingReport.field.getFIELD_NAME_iOrderBy(), "0")//
						.param(WarehousingReport.field.getFIELD_NAME_isASC(), "0")//
						.param(WarehousingReport.field.getFIELD_NAME_iCategoryID(), "1")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();//
		Shared.checkJSONErrorCode(mr5);
	}
}
