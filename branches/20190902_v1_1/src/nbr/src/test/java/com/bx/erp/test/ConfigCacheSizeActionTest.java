package com.bx.erp.test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MvcResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.config.ConfigCacheSize;
import com.bx.erp.util.FieldFormat;
import com.jayway.jsonpath.JsonPath;

import net.sf.json.JSONObject;

@WebAppConfiguration
public class ConfigCacheSizeActionTest extends BaseActionTest {

	@BeforeClass
	public void setup() {
		super.setUp();

	}

	@AfterClass
	public void tearDown() {
		super.tearDown();
	}

	@Test
	public void testUpdateEx1() throws Exception {
		Shared.printTestMethodStartInfo();

		// ConfigCacheSize c = new ConfigCacheSize();

		MvcResult mr = mvc.perform(//
				post("/configCacheSize/updateEx.bx")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
						.param(ConfigCacheSize.field.getFIELD_NAME_value(), "" + System.currentTimeMillis() % 1000000) //
						.param(ConfigCacheSize.field.getFIELD_NAME_ID(), String.valueOf(1)))//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		//
		Shared.checkJSONErrorCode(mr);
		JSONObject o = JSONObject.fromObject(mr.getResponse().getContentAsString()); //
		JSONObject o1 = JsonPath.read(o, "$.object");
		ConfigCacheSize ccs = (ConfigCacheSize) JSONObject.toBean(o1, ConfigCacheSize.class);
		String error = ccs.checkUpdate(BaseBO.INVALID_CASE_ID);
		assertEquals(error, "");
	}

	@Test
	public void testUpdateEx2() throws Exception {
		Shared.printTestMethodStartInfo();

		// ConfigCacheSize c = new ConfigCacheSize();

		MvcResult mr = mvc.perform(//
				post("/configCacheSize/updateEx.bx")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
						.param(ConfigCacheSize.field.getFIELD_NAME_value(), "") //
						.param(ConfigCacheSize.field.getFIELD_NAME_ID(), String.valueOf(1)))//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		//
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_WrongFormatForInputField);
		Shared.checkJSONMsg(mr, ConfigCacheSize.FIELD_ERROR_value, "?????????????????????????????????");
	}

	@Test
	public void testUpdateEx3() throws Exception {
		Shared.printTestMethodStartInfo();

		// ConfigCacheSize c = new ConfigCacheSize();

		MvcResult mr = mvc.perform(//
				post("/configCacheSize/updateEx.bx")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
						.param(ConfigCacheSize.field.getFIELD_NAME_value(), "jjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjj") //
						.param(ConfigCacheSize.field.getFIELD_NAME_ID(), String.valueOf(1)))//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		//
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_WrongFormatForInputField);
		Shared.checkJSONMsg(mr, ConfigCacheSize.FIELD_ERROR_value, "?????????????????????????????????");
	}

	@Test
	public void testRetrieve1Ex1() throws Exception {
		Shared.printTestMethodStartInfo();

		// ConfigCacheSize cacheSize = new ConfigCacheSize();

		MvcResult mr = mvc.perform(//
				get("/configCacheSize/retrieve1Ex.bx?" + ConfigCacheSize.field.getFIELD_NAME_ID() + "=1")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
		) //
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr);
		//
		JSONObject o = JSONObject.fromObject(mr.getResponse().getContentAsString()); //
		JSONObject o1 = JsonPath.read(o, "$.object");
		ConfigCacheSize ccs = (ConfigCacheSize) JSONObject.toBean(o1, ConfigCacheSize.class);
		assertTrue(ccs.getID() == 1);
	}

	@Test
	public void testRetrieve1Ex2() throws Exception {
		Shared.printTestMethodStartInfo();

		// ConfigCacheSize cacheSize = new ConfigCacheSize();
		Shared.caseLog("case2???????????????????????????");

		MvcResult mr = mvc.perform(//
				get("/configCacheSize/retrieve1Ex.bx?" + ConfigCacheSize.field.getFIELD_NAME_ID() + "=-99")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
		) //
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		//
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_WrongFormatForInputField);
		Shared.checkJSONMsg(mr, FieldFormat.FIELD_ERROR_ID, "??????????????????????????????");
	}

	@Test
	public void testRetrieve1Ex3() throws Exception {
		Shared.printTestMethodStartInfo();

		// ConfigCacheSize cacheSize = new ConfigCacheSize();
		Shared.caseLog("case3?????????0????????????");

		MvcResult mr = mvc.perform(//
				get("/configCacheSize/retrieve1Ex.bx?" + ConfigCacheSize.field.getFIELD_NAME_ID() + "=0")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
		) //
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		//
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_WrongFormatForInputField);
		Shared.checkJSONMsg(mr, FieldFormat.FIELD_ERROR_ID, "??????????????????????????????");
	}

	@Test
	public void testRetrieveNEx() throws Exception {
		Shared.printTestMethodStartInfo();

		System.out.println("-------------------------Case1:????????????????????????????????????-----------------------------------");
		MvcResult mr = mvc.perform(//
				get("/configCacheSize/retrieveNEx.bx")//
						.session((MockHttpSession) sessionBoss)//
						.contentType(MediaType.APPLICATION_JSON)//
		) //
				.andExpect(status().isOk())// 3
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr);

	}

}
