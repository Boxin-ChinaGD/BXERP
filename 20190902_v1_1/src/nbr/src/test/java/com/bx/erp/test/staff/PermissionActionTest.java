package com.bx.erp.test.staff;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MvcResult;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import com.bx.erp.action.BaseAction;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.Permission;
import com.bx.erp.test.BaseActionTest;
import com.bx.erp.test.Shared;
import com.bx.erp.util.DataSourceContextHolder;

@WebAppConfiguration
public class PermissionActionTest extends BaseActionTest {

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

		Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);

		// Permission p = new Permission();
		MvcResult mr = mvc.perform(//
				post("/permission/retrieveNEx.bx")//
						.param(Permission.field.getFIELD_NAME_name(), "")//
						.param(Permission.field.getFIELD_NAME_domain(), "")//
						.session((MockHttpSession) sessionBoss)//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr);

		System.out.println("------------------------case2:没有权限进行操作---------------------------------------");
//		MvcResult mr2 = mvc.perform(//
//				post("/permission/retrieveNEx.bx")//
//						.param(Permission.field.getFIELD_NAME_name(), "")//
//						.param(Permission.field.getFIELD_NAME_domain(), "")//
//						.contentType(MediaType.APPLICATION_JSON)//
//						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfManager)))//
//				.andExpect(status().isOk())//
//				.andDo(print())//
//				.andReturn();
//		Shared.checkJSONErrorCode(mr2, EnumErrorCode.EC_NoPermission);

	}

	@Test
	public void testDeleteEx() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);

		// Case1:正常创建
		Permission p = DataInput.getData();
		Map<String, Object> paramsForCreate = p.getCreateParam(BaseBO.INVALID_CASE_ID, p);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Permission permissionCreate = (Permission) permissionMapper.create(paramsForCreate);
		//
		System.out.println("创建权限所需的对象是" + p);
		System.out.println("创建的权限对象为" + permissionCreate);

		p.setIgnoreIDInComparision(true);
		if (p.compareTo(permissionCreate) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		Assert.assertNotNull(permissionCreate);
		Assert.assertEquals(EnumErrorCode.values()[Integer.parseInt(paramsForCreate.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())], EnumErrorCode.EC_NoError);

		MvcResult mr = mvc.perform(//
				post("/permission/deleteEx.bx")//
						.param(Permission.field.getFIELD_NAME_ID(), permissionCreate.getID() + "")//
						.param(Permission.field.getFIELD_NAME_forceDelete(), "1")//
						.session((MockHttpSession) sessionBoss)//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr);

		System.out.println("------------------------case2:没有权限进行操作---------------------------------------");
//		MvcResult mr2 = mvc.perform(//
//				post("/permission/deleteEx.bx")//
//						.param(Permission.field.getFIELD_NAME_ID(), permissionCreate.getID() + "")//
//						.param(Permission.field.getFIELD_NAME_forceDelete(), "1")//
//						.contentType(MediaType.APPLICATION_JSON)//
//						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfManager))//
//		)//
//				.andExpect(status().isOk())//
//				.andDo(print())//
//				.andReturn();
//		Shared.checkJSONErrorCode(mr2, EnumErrorCode.EC_NoPermission);
	}

	@Test
	public void testCreateEx() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);

		// Permission p = new Permission();
		MvcResult mr = mvc.perform(//
				post("/permission/createEx.bx")//
						.param(Permission.field.getFIELD_NAME_SP(), "SP_Staff_Create33")//
						.param(Permission.field.getFIELD_NAME_name(), String.valueOf(System.currentTimeMillis()).substring(6))//
						.param(Permission.field.getFIELD_NAME_domain(), "门店领域")//
						.param(Permission.field.getFIELD_NAME_remark(), "删除供应商23")//
						.session((MockHttpSession) sessionBoss)//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		Shared.checkJSONErrorCode(mr);

		System.out.println("------------------------case2:没有权限进行操作---------------------------------------");
//		MvcResult mr2 = mvc.perform(//
//				post("/permission/createEx.bx")//
//						.param(Permission.field.getFIELD_NAME_SP(), "SP_Staff_Create33")//
//						.param(Permission.field.getFIELD_NAME_name(), "删除12")//
//						.param(Permission.field.getFIELD_NAME_domain(), "门店领域")//
//						.param(Permission.field.getFIELD_NAME_remark(), "删除供应商23")//
//						.contentType(MediaType.APPLICATION_JSON)//
//						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfManager))//
//		)//
//				.andExpect(status().isOk())//
//				.andDo(print())//
//				.andReturn();
//
//		Shared.checkJSONErrorCode(mr2, EnumErrorCode.EC_NoPermission);

	}

	@Test
	public void testRetrieveAlsoRoleStaffEx() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);

		// Permission p = new Permission();
		MvcResult mr = mvc.perform(//
				post("/permission/retrieveAlsoRoleStaffEx.bx")//
						.param(Permission.field.getFIELD_NAME_ID(), "3")//
						.session((MockHttpSession) sessionBoss)//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr);

		System.out.println("------------------------case2:没有权限进行操作---------------------------------------");
//		MvcResult mr2 = mvc.perform(//
//				post("/permission/retrieveAlsoRoleStaffEx.bx")//
//						.param(Permission.field.getFIELD_NAME_ID(), "3")//
//						.contentType(MediaType.APPLICATION_JSON)//
//						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfManager)))//
//				.andExpect(status().isOk())//
//				.andDo(print())//
//				.andReturn();
//		Shared.checkJSONErrorCode(mr2, EnumErrorCode.EC_NoPermission);
	}
}
