package com.bx.erp.test.commodity;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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

import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.commodity.CommodityProperty;
import com.bx.erp.test.BaseActionTest;
import com.bx.erp.test.Shared;

@WebAppConfiguration
public class CommodityPropertyActionTest extends BaseActionTest {

	@BeforeClass
	public void setup() {
		super.setUp();
	}

	@AfterClass
	public void tearDown() {
		super.tearDown();
	}

	@Test
	public void testRetrieve1Ex() throws Exception {
		Shared.printTestMethodStartInfo();

		MvcResult mr = mvc.perform(//
				get("/commodityProperty/retrieve1Ex.bx")//
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr);

		System.out.println("---------------------------case2:没有权限进行查询--------------------------");
//		MvcResult mr1 = mvc.perform(//
//				get("/commodityProperty/retrieve1Ex.bx")//
//						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfManager))//
//						.contentType(MediaType.APPLICATION_JSON)//
//		)//
//				.andExpect(status().isOk())//
//				.andDo(print())//
//				.andReturn();
//		Shared.checkJSONErrorCode(mr1, EnumErrorCode.EC_NoPermission);
	}

	@Test
	public void testUpdateEx() throws Exception {
		Shared.printTestMethodStartInfo();

		MvcResult mr = mvc.perform(//
				post("/commodityProperty/updateEx.bx")//
						.param(CommodityProperty.field.getFIELD_NAME_name1(), "66666666666666")//
						.param(CommodityProperty.field.getFIELD_NAME_name2(), "22222222222222")//
						.param(CommodityProperty.field.getFIELD_NAME_name3(), "333333333")//
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr);

		System.out.println("---------------------------case2:没有权限进行查询--------------------------");
//		MvcResult mr1 = mvc.perform(//
//				post("/commodityProperty/updateEx.bx")//
//						.param(CommodityProperty.field.getFIELD_NAME_name1(), "66666666666666")//
//						.param(CommodityProperty.field.getFIELD_NAME_name2(), "22222222222222")//
//						.param(CommodityProperty.field.getFIELD_NAME_name3(), "333333333")//
//						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfManager))//
//						.contentType(MediaType.APPLICATION_JSON)//
//		)//
//				.andExpect(status().isOk())//
//				.andDo(print())//
//				.andReturn();
//		Shared.checkJSONErrorCode(mr1, EnumErrorCode.EC_NoPermission);
	}
}
