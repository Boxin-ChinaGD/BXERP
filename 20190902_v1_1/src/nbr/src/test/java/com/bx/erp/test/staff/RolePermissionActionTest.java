package com.bx.erp.test.staff;

import org.springframework.test.context.web.WebAppConfiguration;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import com.bx.erp.test.BaseActionTest;

@WebAppConfiguration
public class RolePermissionActionTest extends BaseActionTest {

	@BeforeClass
	public void setup() {
		super.setUp();
	}

	@AfterClass
	public void tearDown() {
		super.tearDown();
	}

	// @Test
	// public void testDeleteEx() throws Exception {
	// Shared.printTestMethodStartInfo();
	//
	// HttpSession session = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);
	//
	// RolePermission rp = new RolePermission();
	// MvcResult mr =
	// mvc.perform(post("/rolePermission/deleteEx.bx").session((MockHttpSession)
	// session).param(rp.getFIELD_NAME_roleID(),
	// "1").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andDo(print()).andReturn();
	//
	// Shared.checkJSONErrorCode(mr);
	// }
}
