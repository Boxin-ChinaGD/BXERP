package com.bx.erp.test.staff;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.testng.Assert.assertTrue;

import java.util.List;

import org.apache.maven.model.ModelBase;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MvcResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.Role.EnumTypeRole;
import com.bx.erp.model.Staff;
import com.bx.erp.model.Staff.EnumStatusStaff;
import com.bx.erp.action.BaseAction;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.StaffRole;
import com.bx.erp.model.BaseModel.EnumBoolean;
import com.bx.erp.test.BaseActionTest;
import com.bx.erp.test.BaseStaffTest;
import com.bx.erp.test.Shared;
import com.jayway.jsonpath.JsonPath;

import net.sf.json.JSONObject;

@WebAppConfiguration
public class StaffRoleActionTest extends BaseActionTest {

	@BeforeClass
	public void setup() {
		super.setUp();
	}

	@AfterClass
	public void tearDown() {
		super.tearDown();
	}

	@Test
	public void testRetrieveN1() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case1：查询所有角色(在职和离职)");
		StaffRole staffRole1 = new StaffRole();
		staffRole1.setRoleID(StaffRoleMapperTest.DEFAULT_VALUE_RoleID);
		staffRole1.setStatus(StaffRoleMapperTest.DEFAULT_VALUE_Status);
		MvcResult mr1 = mvc.perform( //
				get("/staffRole/retrieveNEx.bx?" + StaffRole.field.getFIELD_NAME_roleID() + "=" + String.valueOf(staffRole1.getRoleID()) //
						+ "&" + StaffRole.field.getFIELD_NAME_status() + "=" + String.valueOf(staffRole1.getStatus())) //
								.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss)) //
								.contentType(MediaType.APPLICATION_JSON) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print())//
				.andReturn();

		Shared.checkJSONErrorCode(mr1);
		// 结果验证
		String json1 = mr1.getResponse().getContentAsString();
		JSONObject o1 = JSONObject.fromObject(json1);
		List<ModelBase> list1 = JsonPath.read(o1, "$." + BaseAction.KEY_ObjectList);
		assertTrue(list1.size() > 0, "case1测试失败,返回的数据条数不对");

	}

	@Test
	public void testRetrieveN2() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("case2：查询在职的角色");
		StaffRole staffRole2 = new StaffRole();
		staffRole2.setRoleID(StaffRoleMapperTest.DEFAULT_VALUE_RoleID);
		staffRole2.setStatus(EnumStatusStaff.ESS_Incumbent.getIndex());
		MvcResult mr2 = mvc.perform( //
				get("/staffRole/retrieveNEx.bx?" + StaffRole.field.getFIELD_NAME_roleID() + "=" + String.valueOf(staffRole2.getRoleID()) //
						+ "&" + StaffRole.field.getFIELD_NAME_status() + "=" + String.valueOf(staffRole2.getStatus())) //
								.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss)) //
								.contentType(MediaType.APPLICATION_JSON) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print())//
				.andReturn();

		Shared.checkJSONErrorCode(mr2);
		// 结果验证
		String json2 = mr2.getResponse().getContentAsString();
		JSONObject o2 = JSONObject.fromObject(json2);
		List<ModelBase> list2 = JsonPath.read(o2, "$." + BaseAction.KEY_ObjectList);
		assertTrue(list2.size() > 0, "case2测试失败,返回的数据条数不对");
	}

	@Test
	public void testRetrieveN3() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("case3：查询离职的角色");
		StaffRole staffRole3 = new StaffRole();
		staffRole3.setRoleID(StaffRoleMapperTest.DEFAULT_VALUE_RoleID);
		staffRole3.setStatus(EnumStatusStaff.ESS_Resigned.getIndex());
		MvcResult mr3 = mvc.perform( //
				get("/staffRole/retrieveNEx.bx?" + StaffRole.field.getFIELD_NAME_roleID() + "=" + String.valueOf(staffRole3.getRoleID()) //
						+ "&" + StaffRole.field.getFIELD_NAME_status() + "=" + String.valueOf(staffRole3.getStatus())) //
								.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss)) //
								.contentType(MediaType.APPLICATION_JSON) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print())//
				.andReturn();

		Shared.checkJSONErrorCode(mr3);
		// 结果验证
		String json3 = mr3.getResponse().getContentAsString();
		JSONObject o3 = JSONObject.fromObject(json3);
		List<ModelBase> list3 = JsonPath.read(o3, "$." + BaseAction.KEY_ObjectList);
		assertTrue(list3.size() > 0, "case3测试失败,返回的数据条数不对");
	}

	@Test
	public void testRetrieveN4() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("case4：查询所有收银员(在职和离职)");
		StaffRole staffRole4 = new StaffRole();
		staffRole4.setRoleID(EnumTypeRole.ETR_Cashier.getIndex());
		staffRole4.setStatus(StaffRoleMapperTest.DEFAULT_VALUE_Status);
		MvcResult mr4 = mvc.perform( //
				get("/staffRole/retrieveNEx.bx?" + StaffRole.field.getFIELD_NAME_roleID() + "=" + String.valueOf(staffRole4.getRoleID()) //
						+ "&" + StaffRole.field.getFIELD_NAME_status() + "=" + String.valueOf(staffRole4.getStatus())) //
								.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss)) //
								.contentType(MediaType.APPLICATION_JSON) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print())//
				.andReturn();

		Shared.checkJSONErrorCode(mr4);
		// 结果验证
		String json4 = mr4.getResponse().getContentAsString();
		JSONObject o4 = JSONObject.fromObject(json4);
		List<ModelBase> list4 = JsonPath.read(o4, "$." + BaseAction.KEY_ObjectList);
		assertTrue(list4.size() > 0, "case4测试失败,返回的数据条数不对");
	}

	@Test
	public void testRetrieveN5() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("case5：查询在职的收银员");
		StaffRole staffRole5 = new StaffRole();
		staffRole5.setRoleID(EnumTypeRole.ETR_Cashier.getIndex());
		staffRole5.setStatus(EnumStatusStaff.ESS_Incumbent.getIndex());
		MvcResult mr5 = mvc.perform( //
				get("/staffRole/retrieveNEx.bx?" + StaffRole.field.getFIELD_NAME_roleID() + "=" + String.valueOf(staffRole5.getRoleID()) //
						+ "&" + StaffRole.field.getFIELD_NAME_status() + "=" + String.valueOf(staffRole5.getStatus())) //
								.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss)) //
								.contentType(MediaType.APPLICATION_JSON) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print())//
				.andReturn();

		Shared.checkJSONErrorCode(mr5);
		// 结果验证
		String json5 = mr5.getResponse().getContentAsString();
		JSONObject o5 = JSONObject.fromObject(json5);
		List<ModelBase> list5 = JsonPath.read(o5, "$." + BaseAction.KEY_ObjectList);
		assertTrue(list5.size() > 0, "case5测试失败,返回的数据条数不对");
	}

	@Test
	public void testRetrieveN6() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("case6：查询离职的收银员");
		StaffRole staffRole6 = new StaffRole();
		staffRole6.setRoleID(EnumTypeRole.ETR_Cashier.getIndex());
		staffRole6.setStatus(EnumStatusStaff.ESS_Resigned.getIndex());
		MvcResult mr6 = mvc.perform( //
				get("/staffRole/retrieveNEx.bx?" + StaffRole.field.getFIELD_NAME_roleID() + "=" + String.valueOf(staffRole6.getRoleID()) //
						+ "&" + StaffRole.field.getFIELD_NAME_status() + "=" + String.valueOf(staffRole6.getStatus())) //
								.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss)) //
								.contentType(MediaType.APPLICATION_JSON) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print())//
				.andReturn();

		Shared.checkJSONErrorCode(mr6);
		// 结果验证
		String json6 = mr6.getResponse().getContentAsString();
		JSONObject o6 = JSONObject.fromObject(json6);
		List<ModelBase> list6 = JsonPath.read(o6, "$." + BaseAction.KEY_ObjectList);
		assertTrue(list6.size() > 0, "case6测试失败,返回的数据条数不对");
	}

	// 没有经理角色了
//	@Test
//	public void testRetrieveN7() throws Exception {
//		Shared.printTestMethodStartInfo();
//		Shared.caseLog("case7：查询所有经理(在职和离职)");
//		StaffRole staffRole7 = new StaffRole();
//		staffRole7.setRoleID(EnumTypeRole.ETR_Manager.getIndex());
//		staffRole7.setStatus(StaffRoleMapperTest.DEFAULT_VALUE_Status);
//		MvcResult mr7 = mvc.perform( //
//				get("/staffRole/retrieveNEx.bx?" + StaffRole.field.getFIELD_NAME_roleID() + "=" + String.valueOf(staffRole7.getRoleID()) //
//						+ "&" + StaffRole.field.getFIELD_NAME_status() + "=" + String.valueOf(staffRole7.getStatus())) //
//								.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss)) //
//								.contentType(MediaType.APPLICATION_JSON) //
//		) //
//				.andExpect(status().isOk()) //
//				.andDo(print())//
//				.andReturn();
//
//		Shared.checkJSONErrorCode(mr7);
//		// 结果验证
//		String json7 = mr7.getResponse().getContentAsString();
//		JSONObject o7 = JSONObject.fromObject(json7);
//		List<ModelBase> list7 = JsonPath.read(o7, "$." + BaseAction.KEY_ObjectList);
//		assertTrue(list7.size() > 0, "case7测试失败,返回的数据条数不对");
//	}
//
//	@Test
//	public void testRetrieveN8() throws Exception {
//		Shared.printTestMethodStartInfo();
//		Shared.caseLog("case8：查询在职的经理");
//		StaffRole staffRole8 = new StaffRole();
//		staffRole8.setRoleID(EnumTypeRole.ETR_Manager.getIndex());
//		staffRole8.setStatus(EnumStatusStaff.ESS_Incumbent.getIndex());
//		MvcResult mr8 = mvc.perform( //
//				get("/staffRole/retrieveNEx.bx?" + StaffRole.field.getFIELD_NAME_roleID() + "=" + String.valueOf(staffRole8.getRoleID()) //
//						+ "&" + StaffRole.field.getFIELD_NAME_status() + "=" + String.valueOf(staffRole8.getStatus())) //
//								.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss)) //
//								.contentType(MediaType.APPLICATION_JSON) //
//		) //
//				.andExpect(status().isOk()) //
//				.andDo(print())//
//				.andReturn();
//
//		Shared.checkJSONErrorCode(mr8);
//		// 结果验证
//		String json8 = mr8.getResponse().getContentAsString();
//		JSONObject o8 = JSONObject.fromObject(json8);
//		List<ModelBase> list8 = JsonPath.read(o8, "$." + BaseAction.KEY_ObjectList);
//		assertTrue(list8.size() > 0, "case8测试失败,返回的数据条数不对");
//	}
//
//	@Test
//	public void testRetrieveN9() throws Exception {
//		Shared.printTestMethodStartInfo();
//		Shared.caseLog("case9：查询离职的经理");
//		StaffRole staffRole9 = new StaffRole();
//		staffRole9.setRoleID(EnumTypeRole.ETR_Manager.getIndex());
//		staffRole9.setStatus(EnumStatusStaff.ESS_Resigned.getIndex());
//		MvcResult mr9 = mvc.perform( //
//				get("/staffRole/retrieveNEx.bx?" + StaffRole.field.getFIELD_NAME_roleID() + "=" + String.valueOf(staffRole9.getRoleID()) //
//						+ "&" + StaffRole.field.getFIELD_NAME_status() + "=" + String.valueOf(staffRole9.getStatus())) //
//								.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss)) //
//								.contentType(MediaType.APPLICATION_JSON) //
//		) //
//				.andExpect(status().isOk()) //
//				.andDo(print())//
//				.andReturn();
//
//		Shared.checkJSONErrorCode(mr9);
//		// 结果验证
//		String json9 = mr9.getResponse().getContentAsString();
//		JSONObject o9 = JSONObject.fromObject(json9);
//		List<ModelBase> list9 = JsonPath.read(o9, "$." + BaseAction.KEY_ObjectList);
//		assertTrue(list9.size() >= 0, "case9测试失败,返回的数据条数不对");
//	}

	// 没有副店长角色了
//	@Test
//	public void testRetrieveN10() throws Exception {
//		Shared.printTestMethodStartInfo();
//		Shared.caseLog("case10：查询所有副店长(在职和离职)");
//		StaffRole staffRole10 = new StaffRole();
//		staffRole10.setRoleID(EnumTypeRole.ETR_Assistant.getIndex());
//		staffRole10.setStatus(StaffRoleMapperTest.DEFAULT_VALUE_Status);
//		MvcResult mr10 = mvc.perform( //
//				get("/staffRole/retrieveNEx.bx?" + StaffRole.field.getFIELD_NAME_roleID() + "=" + String.valueOf(staffRole10.getRoleID()) //
//						+ "&" + StaffRole.field.getFIELD_NAME_status() + "=" + String.valueOf(staffRole10.getStatus())) //
//								.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss)) //
//								.contentType(MediaType.APPLICATION_JSON) //
//		) //
//				.andExpect(status().isOk()) //
//				.andDo(print())//
//				.andReturn();
//
//		Shared.checkJSONErrorCode(mr10);
//		// 结果验证
//		String json10 = mr10.getResponse().getContentAsString();
//		JSONObject o10 = JSONObject.fromObject(json10);
//		List<ModelBase> list10 = JsonPath.read(o10, "$." + BaseAction.KEY_ObjectList);
//		assertTrue(list10.size() > 0, "case10测试失败,返回的数据条数不对");
//	}
//
//	@Test
//	public void testRetrieveN11() throws Exception {
//		Shared.printTestMethodStartInfo();
//		Shared.caseLog("case11：查询在职的副店长");
//		StaffRole staffRole11 = new StaffRole();
//		staffRole11.setRoleID(EnumTypeRole.ETR_Assistant.getIndex());
//		staffRole11.setStatus(EnumStatusStaff.ESS_Incumbent.getIndex());
//		MvcResult mr11 = mvc.perform( //
//				get("/staffRole/retrieveNEx.bx?" + StaffRole.field.getFIELD_NAME_roleID() + "=" + String.valueOf(staffRole11.getRoleID()) //
//						+ "&" + StaffRole.field.getFIELD_NAME_status() + "=" + String.valueOf(staffRole11.getStatus())) //
//								.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss)) //
//								.contentType(MediaType.APPLICATION_JSON) //
//		) //
//				.andExpect(status().isOk()) //
//				.andDo(print())//
//				.andReturn();
//
//		Shared.checkJSONErrorCode(mr11);
//		// 结果验证
//		String json11 = mr11.getResponse().getContentAsString();
//		JSONObject o11 = JSONObject.fromObject(json11);
//		List<ModelBase> list11 = JsonPath.read(o11, "$." + BaseAction.KEY_ObjectList);
//		assertTrue(list11.size() > 0, "case11测试失败,返回的数据条数不对");
//	}
//
//	@Test
//	public void testRetrieveN12() throws Exception {
//		Shared.printTestMethodStartInfo();
//		Shared.caseLog("case12：查询离职的副店长");
//		StaffRole staffRole12 = new StaffRole();
//		staffRole12.setRoleID(EnumTypeRole.ETR_Assistant.getIndex());
//		staffRole12.setStatus(EnumStatusStaff.ESS_Resigned.getIndex());
//		MvcResult mr12 = mvc.perform( //
//				get("/staffRole/retrieveNEx.bx?" + StaffRole.field.getFIELD_NAME_roleID() + "=" + String.valueOf(staffRole12.getRoleID()) //
//						+ "&" + StaffRole.field.getFIELD_NAME_status() + "=" + String.valueOf(staffRole12.getStatus())) //
//								.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss)) //
//								.contentType(MediaType.APPLICATION_JSON) //
//		) //
//				.andExpect(status().isOk()) //
//				.andDo(print())//
//				.andReturn();
//
//		Shared.checkJSONErrorCode(mr12);
//		// 结果验证
//		String json12 = mr12.getResponse().getContentAsString();
//		JSONObject o12 = JSONObject.fromObject(json12);
//		List<ModelBase> list12 = JsonPath.read(o12, "$." + BaseAction.KEY_ObjectList);
//		assertTrue(list12.size() >= 0, "case12测试失败,返回的数据条数不对");
//	}
//
//	@Test
//	public void testRetrieveN13() throws Exception {
//		Shared.printTestMethodStartInfo();
//		Shared.caseLog("case13：查询所有店长(在职和离职)");
//		StaffRole staffRole13 = new StaffRole();
//		staffRole13.setRoleID(EnumTypeRole.ETR_Assistant.getIndex());
//		staffRole13.setStatus(StaffRoleMapperTest.DEFAULT_VALUE_Status);
//		MvcResult mr13 = mvc.perform( //
//				get("/staffRole/retrieveNEx.bx?" + StaffRole.field.getFIELD_NAME_roleID() + "=" + String.valueOf(staffRole13.getRoleID()) //
//						+ "&" + StaffRole.field.getFIELD_NAME_status() + "=" + String.valueOf(staffRole13.getStatus())) //
//								.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss)) //
//								.contentType(MediaType.APPLICATION_JSON) //
//		) //
//				.andExpect(status().isOk()) //
//				.andDo(print())//
//				.andReturn();
//
//		Shared.checkJSONErrorCode(mr13);
//		// 结果验证
//		String json13 = mr13.getResponse().getContentAsString();
//		JSONObject o13 = JSONObject.fromObject(json13);
//		List<ModelBase> list13 = JsonPath.read(o13, "$." + BaseAction.KEY_ObjectList);
//		assertTrue(list13.size() > 0, "case13测试失败,返回的数据条数不对");
//	}

	@Test
	public void testRetrieveN14() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("case14：查询在职的店长");
		StaffRole staffRole14 = new StaffRole();
		staffRole14.setRoleID(EnumTypeRole.ETR_Boss.getIndex());
		staffRole14.setStatus(EnumStatusStaff.ESS_Incumbent.getIndex());
		MvcResult mr14 = mvc.perform( //
				get("/staffRole/retrieveNEx.bx?" + StaffRole.field.getFIELD_NAME_roleID() + "=" + String.valueOf(staffRole14.getRoleID()) //
						+ "&" + StaffRole.field.getFIELD_NAME_status() + "=" + String.valueOf(staffRole14.getStatus())) //
								.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss)) //
								.contentType(MediaType.APPLICATION_JSON) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print())//
				.andReturn();

		Shared.checkJSONErrorCode(mr14);
		// 结果验证
		String json14 = mr14.getResponse().getContentAsString();
		JSONObject o14 = JSONObject.fromObject(json14);
		List<ModelBase> list14 = JsonPath.read(o14, "$." + BaseAction.KEY_ObjectList);
		assertTrue(list14.size() > 0, "case12测试失败,返回的数据条数不对");
	}

	@Test
	public void testRetrieveN15() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("case15：查询离职的店长");
		StaffRole staffRole15 = new StaffRole();
		staffRole15.setRoleID(EnumTypeRole.ETR_Boss.getIndex());
		staffRole15.setStatus(EnumStatusStaff.ESS_Resigned.getIndex());
		MvcResult mr15 = mvc.perform( //
				get("/staffRole/retrieveNEx.bx?" + StaffRole.field.getFIELD_NAME_roleID() + "=" + String.valueOf(staffRole15.getRoleID()) //
						+ "&" + StaffRole.field.getFIELD_NAME_status() + "=" + String.valueOf(staffRole15.getStatus())) //
								.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss)) //
								.contentType(MediaType.APPLICATION_JSON) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print())//
				.andReturn();

		Shared.checkJSONErrorCode(mr15);
		// 结果验证
		String json15 = mr15.getResponse().getContentAsString();
		JSONObject o15 = JSONObject.fromObject(json15);
		List<ModelBase> list15 = JsonPath.read(o15, "$." + BaseAction.KEY_ObjectList);
		assertTrue(list15.size() >= 0, "case15测试失败,返回的数据条数不对");
	}

	// 没有业务经理角色了
//	@Test
//	public void testRetrieveN16() throws Exception {
//		Shared.printTestMethodStartInfo();
//		Shared.caseLog("case16：查询所有业务经理(在职和离职)");
//		StaffRole staffRole16 = new StaffRole();
//		staffRole16.setRoleID(EnumTypeRole.ETR_CommercialManager.getIndex());
//		staffRole16.setStatus(StaffRoleMapperTest.DEFAULT_VALUE_Status);
//		MvcResult mr16 = mvc.perform( //
//				get("/staffRole/retrieveNEx.bx?" + StaffRole.field.getFIELD_NAME_roleID() + "=" + String.valueOf(staffRole16.getRoleID()) //
//						+ "&" + StaffRole.field.getFIELD_NAME_status() + "=" + String.valueOf(staffRole16.getStatus())) //
//								.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss)) //
//								.contentType(MediaType.APPLICATION_JSON) //
//		) //
//				.andExpect(status().isOk()) //
//				.andDo(print())//
//				.andReturn();
//
//		Shared.checkJSONErrorCode(mr16);
//		// 结果验证
//		String json16 = mr16.getResponse().getContentAsString();
//		JSONObject o16 = JSONObject.fromObject(json16);
//		List<ModelBase> list16 = JsonPath.read(o16, "$." + BaseAction.KEY_ObjectList);
//		assertTrue(list16.size() >= 0, "case16测试失败,返回的数据条数不对");
//	}
//
//	@Test
//	public void testRetrieveN17() throws Exception {
//		Shared.printTestMethodStartInfo();
//		Shared.caseLog("case17：查询在职的业务经理");
//		StaffRole staffRole17 = new StaffRole();
//		staffRole17.setRoleID(EnumTypeRole.ETR_CommercialManager.getIndex());
//		staffRole17.setStatus(EnumStatusStaff.ESS_Incumbent.getIndex());
//		MvcResult mr17 = mvc.perform( //
//				get("/staffRole/retrieveNEx.bx?" + StaffRole.field.getFIELD_NAME_roleID() + "=" + String.valueOf(staffRole17.getRoleID()) //
//						+ "&" + StaffRole.field.getFIELD_NAME_status() + "=" + String.valueOf(staffRole17.getStatus())) //
//								.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss)) //
//								.contentType(MediaType.APPLICATION_JSON) //
//		) //
//				.andExpect(status().isOk()) //
//				.andDo(print())//
//				.andReturn();
//
//		Shared.checkJSONErrorCode(mr17);
//		// 结果验证
//		String json17 = mr17.getResponse().getContentAsString();
//		JSONObject o17 = JSONObject.fromObject(json17);
//		List<ModelBase> list17 = JsonPath.read(o17, "$." + BaseAction.KEY_ObjectList);
//		assertTrue(list17.size() >= 0, "case17测试失败,返回的数据条数不对");
//	}
//
//	@Test
//	public void testRetrieveN18() throws Exception {
//		Shared.printTestMethodStartInfo();
//		Shared.caseLog("case18：查询离职的业务经理");
//		StaffRole staffRole18 = new StaffRole();
//		staffRole18.setRoleID(EnumTypeRole.ETR_CommercialManager.getIndex());
//		staffRole18.setStatus(EnumStatusStaff.ESS_Resigned.getIndex());
//		MvcResult mr18 = mvc.perform( //
//				get("/staffRole/retrieveNEx.bx?" + StaffRole.field.getFIELD_NAME_roleID() + "=" + String.valueOf(staffRole18.getRoleID()) //
//						+ "&" + StaffRole.field.getFIELD_NAME_status() + "=" + String.valueOf(staffRole18.getStatus())) //
//								.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss)) //
//								.contentType(MediaType.APPLICATION_JSON) //
//		) //
//				.andExpect(status().isOk()) //
//				.andDo(print())//
//				.andReturn();
//
//		Shared.checkJSONErrorCode(mr18);
//		// 结果验证
//		String json18 = mr18.getResponse().getContentAsString();
//		JSONObject o18 = JSONObject.fromObject(json18);
//		List<ModelBase> list18 = JsonPath.read(o18, "$." + BaseAction.KEY_ObjectList);
//		assertTrue(list18.size() >= 0, "case18测试失败,返回的数据条数不对");
//	}

	@Test
	public void testRetrieveN19_1() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("case19_1：老板查询所有博昕售前(在职和离职)");
		StaffRole staffRole19 = new StaffRole();
		staffRole19.setRoleID(EnumTypeRole.ETR_PreSale.getIndex());
		staffRole19.setStatus(StaffRoleMapperTest.DEFAULT_VALUE_Status);
		staffRole19.setOperator(EnumBoolean.EB_NO.getIndex());
		MvcResult mr19 = mvc.perform( //
				get("/staffRole/retrieveNEx.bx?" //
						+ StaffRole.field.getFIELD_NAME_roleID() + "=" + String.valueOf(staffRole19.getRoleID()) //
						+ "&" + StaffRole.field.getFIELD_NAME_status() + "=" + String.valueOf(staffRole19.getStatus()) //
						+ "&" + StaffRole.field.getFIELD_NAME_operator() + "=" + String.valueOf(staffRole19.getOperator()) //
				) //
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss)) //
						.contentType(MediaType.APPLICATION_JSON) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr19);
		//
		JSONObject o = JSONObject.fromObject(mr19.getResponse().getContentAsString()); //
		StaffRole staffRole = new StaffRole();
		List<BaseModel> bmList = staffRole.parseN(o.getString(BaseAction.KEY_ObjectList));
		for (BaseModel bm : bmList) {
			StaffRole staffRole2 = (StaffRole) bm;
			assertTrue(!(BaseAction.ACCOUNT_Phone_PreSale.equals(staffRole2.getPhone())), "RN StaffRole错误，不应该查询出售前账号。Phone=" + staffRole2.getPhone());
		}
	}

	@Test
	public void testRetrieveN19_2() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("case19_2：OP查询所有博昕售前(在职和离职)");
		StaffRole staffRole19 = new StaffRole();
		staffRole19.setRoleID(EnumTypeRole.ETR_PreSale.getIndex());
		staffRole19.setStatus(StaffRoleMapperTest.DEFAULT_VALUE_Status);
		staffRole19.setOperator(EnumBoolean.EB_Yes.getIndex());
		MvcResult mr19 = mvc.perform( //
				get("/staffRole/retrieveNEx.bx?" //
						+ StaffRole.field.getFIELD_NAME_roleID() + "=" + String.valueOf(staffRole19.getRoleID()) //
						+ "&" + StaffRole.field.getFIELD_NAME_status() + "=" + String.valueOf(staffRole19.getStatus()) //
						+ "&" + StaffRole.field.getFIELD_NAME_operator() + "=" + String.valueOf(staffRole19.getOperator()) //
				) //
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss)) //
						.contentType(MediaType.APPLICATION_JSON) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr19);
		//
		JSONObject o = JSONObject.fromObject(mr19.getResponse().getContentAsString()); //
		StaffRole staffRole = new StaffRole();
		List<BaseModel> bmList = staffRole.parseN(o.getString(BaseAction.KEY_ObjectList));
		//
		boolean existPreSale = false;
		for (BaseModel bm : bmList) {
			StaffRole staffRole2 = (StaffRole) bm;
			if (BaseAction.ACCOUNT_Phone_PreSale.equals(staffRole2.getPhone())) {
				existPreSale = true;
				break;
			}
		}
		assertTrue(existPreSale, "RN StaffRole错误，应该查询出售前账号。");
	}

	@Test
	public void testRetrieveN20_1() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("case20_1：老板查询在职的博昕售前");
		StaffRole staffRole20 = new StaffRole();
		staffRole20.setRoleID(EnumTypeRole.ETR_PreSale.getIndex());
		staffRole20.setStatus(EnumStatusStaff.ESS_Incumbent.getIndex());
		staffRole20.setOperator(EnumBoolean.EB_NO.getIndex());
		MvcResult mr20 = mvc.perform( //
				get("/staffRole/retrieveNEx.bx?" //
						+ StaffRole.field.getFIELD_NAME_roleID() + "=" + String.valueOf(staffRole20.getRoleID()) //
						+ "&" + StaffRole.field.getFIELD_NAME_status() + "=" + String.valueOf(staffRole20.getStatus()) //
						+ "&" + StaffRole.field.getFIELD_NAME_operator() + "=" + String.valueOf(staffRole20.getOperator()) //
				).session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss)) //
						.contentType(MediaType.APPLICATION_JSON) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print())//
				.andReturn();

		Shared.checkJSONErrorCode(mr20);
		//
		JSONObject o = JSONObject.fromObject(mr20.getResponse().getContentAsString()); //
		StaffRole staffRole = new StaffRole();
		List<BaseModel> bmList = staffRole.parseN(o.getString(BaseAction.KEY_ObjectList));
		for (BaseModel bm : bmList) {
			StaffRole staffRole2 = (StaffRole) bm;
			assertTrue(!(BaseAction.ACCOUNT_Phone_PreSale.equals(staffRole2.getPhone())), "RN StaffRole错误，不应该查询出售前账号。Phone=" + staffRole2.getPhone());
		}
	}

	@Test
	public void testRetrieveN20_2() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("case20_2：OP查询在职的博昕售前");
		StaffRole staffRole20 = new StaffRole();
		staffRole20.setRoleID(EnumTypeRole.ETR_PreSale.getIndex());
		staffRole20.setStatus(EnumStatusStaff.ESS_Incumbent.getIndex());
		staffRole20.setOperator(EnumBoolean.EB_Yes.getIndex());
		MvcResult mr20 = mvc.perform( //
				get("/staffRole/retrieveNEx.bx?" //
						+ StaffRole.field.getFIELD_NAME_roleID() + "=" + String.valueOf(staffRole20.getRoleID()) //
						+ "&" + StaffRole.field.getFIELD_NAME_status() + "=" + String.valueOf(staffRole20.getStatus()) //
						+ "&" + StaffRole.field.getFIELD_NAME_operator() + "=" + String.valueOf(staffRole20.getOperator()) //
				).session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss)) //
						.contentType(MediaType.APPLICATION_JSON) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr20);
		//
		JSONObject o = JSONObject.fromObject(mr20.getResponse().getContentAsString()); //
		StaffRole staffRole = new StaffRole();
		List<BaseModel> bmList = staffRole.parseN(o.getString(BaseAction.KEY_ObjectList));
		//
		boolean existPreSale = false;
		for (BaseModel bm : bmList) {
			StaffRole staffRole2 = (StaffRole) bm;
			if (BaseAction.ACCOUNT_Phone_PreSale.equals(staffRole2.getPhone())) {
				existPreSale = true;
				break;
			}
		}
		assertTrue(existPreSale, "RN StaffRole错误，应该查询出售前账号。");
	}

	@Test
	public void testRetrieveN21_1() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("case21_1：老板查询离职的博昕售前");
		Staff staff = new Staff();
		staff.setID(Shared.PreSaleID);
		BaseStaffTest.deleteStaffViaAction(staff, sessionBoss, mvc, mapBO, Shared.DBName_Test);
		//
		StaffRole staffRole21 = new StaffRole();
		staffRole21.setRoleID(EnumTypeRole.ETR_PreSale.getIndex());
		staffRole21.setStatus(EnumStatusStaff.ESS_Resigned.getIndex());
		staffRole21.setOperator(EnumBoolean.EB_NO.getIndex());
		MvcResult mr21 = mvc.perform( //
				get("/staffRole/retrieveNEx.bx?" //
						+ StaffRole.field.getFIELD_NAME_roleID() + "=" + String.valueOf(staffRole21.getRoleID()) //
						+ "&" + StaffRole.field.getFIELD_NAME_status() + "=" + String.valueOf(staffRole21.getStatus()) //
						+ "&" + StaffRole.field.getFIELD_NAME_operator() + "=" + String.valueOf(staffRole21.getOperator()) //
				).session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss)) //
						.contentType(MediaType.APPLICATION_JSON) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print())//
				.andReturn();

		Shared.checkJSONErrorCode(mr21);
		//
		JSONObject o = JSONObject.fromObject(mr21.getResponse().getContentAsString()); //
		StaffRole staffRole = new StaffRole();
		List<BaseModel> bmList = staffRole.parseN(o.getString(BaseAction.KEY_ObjectList));
		for (BaseModel bm : bmList) {
			StaffRole staffRole2 = (StaffRole) bm;
			assertTrue(!(BaseAction.ACCOUNT_Phone_PreSale.equals(staffRole2.getPhone())), "RN StaffRole错误，不应该查询出售前账号。Phone=" + staffRole2.getPhone());
		}
		//
		BaseStaffTest.updatePreSaleStatusOfIncumbentViaMapper(Shared.DBName_Test);
	}

	@Test
	public void testRetrieveN21_2() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("case21_2：OP查询离职的博昕售前");
		Staff staff = new Staff();
		staff.setID(Shared.PreSaleID);
		BaseStaffTest.deleteStaffViaAction(staff, sessionBoss, mvc, mapBO, Shared.DBName_Test);
		//
		StaffRole staffRole21 = new StaffRole();
		staffRole21.setRoleID(EnumTypeRole.ETR_PreSale.getIndex());
		staffRole21.setStatus(EnumStatusStaff.ESS_Resigned.getIndex());
		staffRole21.setOperator(EnumBoolean.EB_Yes.getIndex());
		MvcResult mr21 = mvc.perform( //
				get("/staffRole/retrieveNEx.bx?" //
						+ StaffRole.field.getFIELD_NAME_roleID() + "=" + String.valueOf(staffRole21.getRoleID()) //
						+ "&" + StaffRole.field.getFIELD_NAME_status() + "=" + String.valueOf(staffRole21.getStatus()) //
						+ "&" + StaffRole.field.getFIELD_NAME_operator() + "=" + String.valueOf(staffRole21.getOperator()) //
				).session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss)) //
						.contentType(MediaType.APPLICATION_JSON) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print())//
				.andReturn();

		Shared.checkJSONErrorCode(mr21);
		//
		JSONObject o = JSONObject.fromObject(mr21.getResponse().getContentAsString()); //
		StaffRole staffRole = new StaffRole();
		List<BaseModel> bmList = staffRole.parseN(o.getString(BaseAction.KEY_ObjectList));
		//
		boolean existPreSale = false;
		for (BaseModel bm : bmList) {
			StaffRole staffRole2 = (StaffRole) bm;
			if (BaseAction.ACCOUNT_Phone_PreSale.equals(staffRole2.getPhone())) {
				existPreSale = true;
				break;
			}
		}
		assertTrue(existPreSale, "RN StaffRole错误，应该查询出售前账号。");
		//
		BaseStaffTest.updatePreSaleStatusOfIncumbentViaMapper(Shared.DBName_Test);
	}

	@Test
	public void testRetrieveN22() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("case22：使用不存在的值(正数)查询角色");
		StaffRole staffRole22 = new StaffRole();
		staffRole22.setRoleID(Shared.BIG_ID);
		staffRole22.setStatus(999999999);
		MvcResult mr22 = mvc.perform( //
				get("/staffRole/retrieveNEx.bx?" + StaffRole.field.getFIELD_NAME_roleID() + "=" + String.valueOf(staffRole22.getRoleID()) //
						+ "&" + StaffRole.field.getFIELD_NAME_status() + "=" + String.valueOf(staffRole22.getStatus())) //
								.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss)) //
								.contentType(MediaType.APPLICATION_JSON) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print())//
				.andReturn();

		Shared.checkJSONErrorCode(mr22, EnumErrorCode.EC_WrongFormatForInputField);
	}

	@Test
	public void testRetrieveN23() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("case23：使用不存在的角色ID(正数)查询角色");
		StaffRole staffRole23 = new StaffRole();
		staffRole23.setRoleID(Shared.BIG_ID);
		staffRole23.setStatus(EnumStatusStaff.ESS_Incumbent.getIndex());
		MvcResult mr23 = mvc.perform( //
				get("/staffRole/retrieveNEx.bx?" + StaffRole.field.getFIELD_NAME_roleID() + "=" + String.valueOf(staffRole23.getRoleID()) //
						+ "&" + StaffRole.field.getFIELD_NAME_status() + "=" + String.valueOf(staffRole23.getStatus())) //
								.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss)) //
								.contentType(MediaType.APPLICATION_JSON) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print())//
				.andReturn();

		Shared.checkJSONErrorCode(mr23);
		// 结果验证
		String json23 = mr23.getResponse().getContentAsString();
		JSONObject o23 = JSONObject.fromObject(json23);
		List<ModelBase> list23 = JsonPath.read(o23, "$." + BaseAction.KEY_ObjectList);
		assertTrue(list23.size() == 0, "case21测试失败,返回的数据条数不对");
	}

	@Test
	public void testRetrieveN24() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("case24：使用不存在的状态(正数)查询角色");
		StaffRole staffRole24 = new StaffRole();
		staffRole24.setRoleID(StaffRoleMapperTest.DEFAULT_VALUE_RoleID);
		staffRole24.setStatus(999999999);
		MvcResult mr24 = mvc.perform( //
				get("/staffRole/retrieveNEx.bx?" + StaffRole.field.getFIELD_NAME_roleID() + "=" + String.valueOf(staffRole24.getRoleID()) //
						+ "&" + StaffRole.field.getFIELD_NAME_status() + "=" + String.valueOf(staffRole24.getStatus())) //
								.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss)) //
								.contentType(MediaType.APPLICATION_JSON) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print())//
				.andReturn();

		Shared.checkJSONErrorCode(mr24, EnumErrorCode.EC_WrongFormatForInputField);
	}

	@Test
	public void testRetrieveN25() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("case25：使用不存在的值(负数)查询角色");
		StaffRole staffRole25 = new StaffRole();
		staffRole25.setRoleID(-999999999);
		staffRole25.setStatus(-999999999);
		MvcResult mr25 = mvc.perform( //
				get("/staffRole/retrieveNEx.bx?" + StaffRole.field.getFIELD_NAME_roleID() + "=" + String.valueOf(staffRole25.getRoleID()) //
						+ "&" + StaffRole.field.getFIELD_NAME_status() + "=" + String.valueOf(staffRole25.getStatus())) //
								.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss)) //
								.contentType(MediaType.APPLICATION_JSON) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print())//
				.andReturn();

		Shared.checkJSONErrorCode(mr25, EnumErrorCode.EC_WrongFormatForInputField);
	}

	@Test
	public void testRetrieveN26() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("case26：使用不存在的角色ID(负数)查询角色");
		StaffRole staffRole26 = new StaffRole();
		staffRole26.setRoleID(-999999999);
		staffRole26.setStatus(EnumStatusStaff.ESS_Incumbent.getIndex());
		MvcResult mr26 = mvc.perform( //
				get("/staffRole/retrieveNEx.bx?" + StaffRole.field.getFIELD_NAME_roleID() + "=" + String.valueOf(staffRole26.getRoleID()) //
						+ "&" + StaffRole.field.getFIELD_NAME_status() + "=" + String.valueOf(staffRole26.getStatus())) //
								.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss)) //
								.contentType(MediaType.APPLICATION_JSON) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print())//
				.andReturn();

		Shared.checkJSONErrorCode(mr26, EnumErrorCode.EC_WrongFormatForInputField);
	}

	@Test
	public void testRetrieveN27() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("case27：使用不存在的状态(负数)查询角色");
		StaffRole staffRole27 = new StaffRole();
		staffRole27.setRoleID(StaffRoleMapperTest.DEFAULT_VALUE_RoleID);
		staffRole27.setStatus(-999999999);
		MvcResult mr27 = mvc.perform( //
				get("/staffRole/retrieveNEx.bx?" + StaffRole.field.getFIELD_NAME_roleID() + "=" + String.valueOf(staffRole27.getRoleID()) //
						+ "&" + StaffRole.field.getFIELD_NAME_status() + "=" + String.valueOf(staffRole27.getStatus())) //
								.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss)) //
								.contentType(MediaType.APPLICATION_JSON) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print())//
				.andReturn();

		Shared.checkJSONErrorCode(mr27, EnumErrorCode.EC_WrongFormatForInputField);
	}

	@Test
	public void testRetrieveN28() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("case28:没有权限进行操作");
//		MvcResult mr28 = mvc.perform( //
//				get("/staffRole/retrieveNEx.bx?" + StaffRole.field.getFIELD_NAME_roleID() + "=4") //
//						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfManager)) //
//						.contentType(MediaType.APPLICATION_JSON) //
//		) //
//				.andExpect(status().isOk()) //
//				.andDo(print())//
//				.andReturn();
//
//		Shared.checkJSONErrorCode(mr28, EnumErrorCode.EC_NoPermission);
	}
}
