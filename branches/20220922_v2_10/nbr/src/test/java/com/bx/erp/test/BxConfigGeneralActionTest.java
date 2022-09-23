package com.bx.erp.test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.testng.Assert.assertTrue;

import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MvcResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.cache.BaseCache;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.config.BxConfigGeneral;
import com.jayway.jsonpath.JsonPath;

import net.sf.json.JSONObject;

@WebAppConfiguration
public class BxConfigGeneralActionTest extends BaseActionTest {

	@BeforeClass
	public void setup() {
		super.setUp();
	}

	@AfterClass
	public void tearDown() {
		super.tearDown();
	}

	// @Test
	// public void testUpdateEx() throws Exception {
	// Shared.printTestMethodStartInfo();
	//
	// BxConfigGeneral c = new BxConfigGeneral();
	// c.setID(BaseCache.CompanyBusinessLicensePictureVolumeMax);
	//
	// MvcResult mr = mvc.perform(//
	// post("/bxConfigGeneral/updateEx.bx")//
	// .contentType(MediaType.APPLICATION_JSON)//
	// .param(c.getFIELD_NAME_ID(), String.valueOf(c.getID())) //
	// .param(c.getFIELD_NAME_value(), "102401") //
	// .session((MockHttpSession) Shared.getBXStaffLoginSession(mvc,
	// Shared.PhoneOfOP)))//
	// .andExpect(status().isOk())//
	// .andDo(print())//
	// .andReturn();
	// Shared.checkJSONErrorCode(mr);
	//
	// //...其它结果验证？
	// }

	@Test
	public void testRetrieve1Ex1() throws Exception {
		Shared.printTestMethodStartInfo();

		BxConfigGeneral bxConfigGeneral = new BxConfigGeneral();
		bxConfigGeneral.setID(BaseCache.CommodityLogoDir);
		//
		MvcResult mr = mvc.perform(//
				get("/bxConfigGeneral/retrieve1Ex.bx?ID=" + bxConfigGeneral.getID())//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) Shared.getBXStaffLoginSession(mvc, Shared.PhoneOfOP))//
		) //
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr);
		//
		JSONObject o = JSONObject.fromObject(mr.getResponse().getContentAsString()); //
		JSONObject o1 = JsonPath.read(o, "$.object");
		BxConfigGeneral bcg = (BxConfigGeneral) JSONObject.toBean(o1, BxConfigGeneral.class);
		assertTrue(bcg.getID() == bxConfigGeneral.getID());

		// ...其它结果验证？
	}

	@Test
	public void testRetrieve1Ex2() throws Exception {
		Shared.printTestMethodStartInfo();

		BxConfigGeneral bxConfigGeneral = new BxConfigGeneral();
		bxConfigGeneral.setID(0);
		//
		MvcResult mr = mvc.perform(//
				get("/bxConfigGeneral/retrieve1Ex.bx?ID=" + bxConfigGeneral.getID())//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) Shared.getBXStaffLoginSession(mvc, Shared.PhoneOfOP))//
		) //
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		//
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_WrongFormatForInputField);
		Shared.checkJSONMsg(mr, BxConfigGeneral.FIELD_ERROR_BxConfigGeneralID, "错误信息与预期的不一致");
	}

	@Test
	public void testRetrieve1Ex3() throws Exception {
		Shared.printTestMethodStartInfo();

		BxConfigGeneral bxConfigGeneral = new BxConfigGeneral();
		bxConfigGeneral.setID(-99);
		//
		MvcResult mr = mvc.perform(//
				get("/bxConfigGeneral/retrieve1Ex.bx?ID=" + bxConfigGeneral.getID())//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) Shared.getBXStaffLoginSession(mvc, Shared.PhoneOfOP))//
		) //
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		//
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_WrongFormatForInputField);
		Shared.checkJSONMsg(mr, BxConfigGeneral.FIELD_ERROR_BxConfigGeneralID, "错误信息与预期的不一致");
	}

	@Test
	public void testRetrieve1Ex4() throws Exception {
		Shared.printTestMethodStartInfo();

		BxConfigGeneral bxConfigGeneral = new BxConfigGeneral();
		bxConfigGeneral.setID(BxConfigGeneral.MAX_BxConfigGeneralID + 1);
		//
		MvcResult mr = mvc.perform(//
				get("/bxConfigGeneral/retrieve1Ex.bx?ID=" + bxConfigGeneral.getID())//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) Shared.getBXStaffLoginSession(mvc, Shared.PhoneOfOP))//
		) //
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		//
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_WrongFormatForInputField);
		Shared.checkJSONMsg(mr, BxConfigGeneral.FIELD_ERROR_BxConfigGeneralID, "错误信息与预期的不一致");
	}

	@Test
	public void testRetrieveNEx1() throws Exception {
		Shared.printTestMethodStartInfo();

		System.out.println("-------------------------Case1:正常查询所有普通配置-----------------------------------");
		MvcResult mr = mvc.perform(//
				get("/bxConfigGeneral/retrieveNEx.bx")//
						.session((MockHttpSession) Shared.getBXStaffLoginSession(mvc, Shared.PhoneOfOP))//
						.contentType(MediaType.APPLICATION_JSON)//
		) //
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr);

		// ...其它结果验证？
	}

	@Test
	public void testRetrieveNEx2() throws Exception {
		Shared.printTestMethodStartInfo();

		System.out.println("-------------------------Case2:POS端查询所有普通配置-----------------------------------");
		MvcResult mr = mvc.perform(//
				get("/bxConfigGeneral/retrieveNEx.bx?" + BxConfigGeneral.field.getFIELD_NAME_isRequestFromPos() + "=1")//
						.session((MockHttpSession) Shared.getBXStaffLoginSession(mvc, Shared.PhoneOfOP))//
						.contentType(MediaType.APPLICATION_JSON)//
		) //
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr);

		// ...其它结果验证？
	}
}
