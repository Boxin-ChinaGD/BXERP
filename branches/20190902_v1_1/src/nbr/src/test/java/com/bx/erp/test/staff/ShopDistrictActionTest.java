package com.bx.erp.test.staff;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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

import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.ShopDistrict;
import com.bx.erp.test.BaseActionTest;
import com.bx.erp.test.Shared;
import com.jayway.jsonpath.JsonPath;

import net.sf.json.JSONObject;

@WebAppConfiguration
public class ShopDistrictActionTest extends BaseActionTest {
//
//	@BeforeClass
//	public void setup() {
//		super.setUp();
//	}
//
//	@AfterClass
//	public void tearDown() {
//		super.tearDown();
//	}
//
//	@Test
//	public void testCreateEx() throws Exception {
//		Shared.printTestMethodStartInfo();
//
//		System.out.println("---------------------case1:正常创建---------------------------------");
//		// ShopDistrict sd = new ShopDistrict(); //
//		MvcResult mr = (MvcResult) mvc.perform(post("/shopDistrict/createEx.bx") //
//				.param(ShopDistrict.field.getFIELD_NAME_name(), "三亚" + System.currentTimeMillis() % 1000000) //
//				.contentType(MediaType.APPLICATION_JSON) //
//				.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))) //
//				.andExpect(status().isOk()).andDo(print()).andReturn(); //
//
//		Shared.checkJSONErrorCode(mr);
//
//		System.out.println("---------------------case2:重复添加区域---------------------------------");
//		MvcResult mr2 = (MvcResult) mvc.perform(post("/shopDistrict/createEx.bx") //
//				.param(ShopDistrict.field.getFIELD_NAME_name(), "广东") //
//				.contentType(MediaType.APPLICATION_JSON) //
//				.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))) //
//				.andExpect(status().isOk()).andDo(print()).andReturn(); //
//
//		Shared.checkJSONErrorCode(mr2, EnumErrorCode.EC_Duplicated);
//
//		System.out.println("---------------------case3:没有权限---------------------------------");
//		MvcResult mr3 = (MvcResult) mvc.perform(post("/shopDistrict/createEx.bx") //
//				.param(ShopDistrict.field.getFIELD_NAME_name(), "三亚" + System.currentTimeMillis() % 1000000) //
//				.contentType(MediaType.APPLICATION_JSON) //
//				.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfManager))) //
//				.andExpect(status().isOk()).andDo(print()).andReturn(); //
//
//		Shared.checkJSONErrorCode(mr3, EnumErrorCode.EC_NoPermission);
//	}
//
	@Test
	public void testRetrieveNEx() throws Exception {
		Shared.printTestMethodStartInfo();

		System.out.println("---------------------case1:正常查询---------------------------------");
		MvcResult mr = mvc.perform(get("/shopDistrict/retrieveNEx.bx") //
				.contentType(MediaType.APPLICATION_JSON) //
				.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))) //
				.andExpect(status().isOk()).andDo(print()).andReturn(); //

		JSONObject o = JSONObject.fromObject(mr.getResponse().getContentAsString());
		List<Integer> list = JsonPath.read(o, "$.objectList[*]");
		Assert.assertTrue(list.size() > 0);
		Shared.checkJSONErrorCode(mr);

		System.out.println("---------------------case2:没有权限---------------------------------");
//		MvcResult mr2 = mvc.perform(get("/shopDistrict/retrieveNEx.bx") //
//				.contentType(MediaType.APPLICATION_JSON) //
//				.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfManager))) //
//				.andExpect(status().isOk()).andDo(print()).andReturn(); //
//
//		Shared.checkJSONErrorCode(mr2, EnumErrorCode.EC_NoPermission);
	}

}
