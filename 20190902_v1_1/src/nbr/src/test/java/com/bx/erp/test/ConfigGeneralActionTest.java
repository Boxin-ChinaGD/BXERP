package com.bx.erp.test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MvcResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.cache.BaseCache;
import com.bx.erp.model.config.ConfigGeneral;
import com.bx.erp.test.checkPoint.SmallSheetCP;

@WebAppConfiguration
public class ConfigGeneralActionTest extends BaseActionTest {

	@BeforeClass
	public void setup() {
		super.setUp();
	}

	@AfterClass
	public void tearDown() {
		super.tearDown();
	}

	@Test
	public void testUpdateEx() throws Exception {
		Shared.printTestMethodStartInfo();

		ConfigGeneral c = new ConfigGeneral();
		c.setID(BaseCache.ACTIVE_SMALLSHEET_ID);

		MvcResult mr = mvc.perform(//
				post("/configGeneral/updateEx.bx")//
						.contentType(MediaType.APPLICATION_JSON)//
						.param(ConfigGeneral.field.getFIELD_NAME_ID(), String.valueOf(c.getID())) //
						.param(ConfigGeneral.field.getFIELD_NAME_value(), "2") //
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss)))//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr);

		// ...其它结果验证？

		// 检查点
		c.setValue("2");
		c.setName("ACTIVE_SMALLSHEET_ID");
		SmallSheetCP.verifyUpdateDefaultSmallSheet(mr, c, Shared.DBName_Test);

	}

	@Test
	public void testRetrieve1Ex() throws Exception {
		Shared.printTestMethodStartInfo();

		ConfigGeneral configGeneral = new ConfigGeneral();
		configGeneral.setID(BaseCache.MaxProviderNOPerCommodity);

		MvcResult mr = mvc.perform(//
				get("/configGeneral/retrieve1Ex.bx?ID=" + configGeneral.getID())//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//
		) //
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr);

		// ...其它结果验证？
	}

	@Test
	public void testRetrieveNEx() throws Exception {
		Shared.printTestMethodStartInfo();

		System.out.println("-------------------------Case1:正常查询所有普通配置-----------------------------------");
		MvcResult mr = mvc.perform(//
				get("/configGeneral/retrieveNEx.bx")//
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//
						.contentType(MediaType.APPLICATION_JSON)//
		) //
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr);

		// ...其它结果验证？

		System.out.println("-------------------------Case2:查询POS机所需要的普通配置-----------------------------------");
		MvcResult mr2 = mvc.perform(//
				get("/configGeneral/retrieveNEx.bx?" + ConfigGeneral.field.getFIELD_NAME_isRequestFromPos() + "=1")//
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//
						.contentType(MediaType.APPLICATION_JSON)//
		) //
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr2);

		// ...其它结果验证？
	}
}
