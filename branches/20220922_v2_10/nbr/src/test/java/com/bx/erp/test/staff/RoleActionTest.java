package com.bx.erp.test.staff;

//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//import java.util.List;
//
//import org.springframework.http.MediaType;
//import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.web.WebAppConfiguration;
//import org.springframework.test.web.servlet.MvcResult;
//import org.testng.annotations.AfterClass;
//import org.testng.annotations.BeforeClass;
//import org.testng.annotations.Test;
//
//import com.bx.erp.action.bo.BaseBO;
//import com.bx.erp.model.ErrorInfo.EnumErrorCode;
//import com.bx.erp.model.Role;
//import com.bx.erp.model.Staff;
import com.bx.erp.test.BaseActionTest;
//import com.bx.erp.test.Shared;
//import com.bx.erp.util.DataSourceContextHolder;
//import com.jayway.jsonpath.JsonPath;

@WebAppConfiguration
public class RoleActionTest extends BaseActionTest {
//
//	private static String roleName;
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
//	public void testRetrieveNEx() throws Exception {
//		Shared.printTestMethodStartInfo();
//		Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);
//		// Role r = new Role();
//		MvcResult mr = mvc.perform(//
//				post("/role/retrieveNEx.bx")//
//						.param(Role.field.getFIELD_NAME_name(), "员工")//
//						.session((MockHttpSession) session)//
//						.contentType(MediaType.APPLICATION_JSON)//
//		)//
//				.andExpect(status().isOk())//
//				.andDo(print())//
//				.andReturn();
//		List<String> names = JsonPath.read(Shared.checkJSONErrorCode(mr), "$.roleList[*].name");
//		if (!names.contains(roleName)) {
//			System.out.println(names);
//			Shared.checkJSONErrorCode(mr);
//		}
//
//		System.out.println("------------------------case2:没有权限进行操作---------------------------------------");
//		MvcResult mr2 = mvc.perform(//
//				post("/role/retrieveNEx.bx")//
//						.param(Role.field.getFIELD_NAME_name(), "员工")//
//						.contentType(MediaType.APPLICATION_JSON)//
//						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfManager))//
//		)//
//				.andExpect(status().isOk())//
//				.andDo(print())//
//				.andReturn();
//		Shared.checkJSONErrorCode(mr2, EnumErrorCode.EC_NoPermission);
//	}
//
//	@Test
//	public void testDeleteEx() throws Exception {
//		Shared.printTestMethodStartInfo();
//
//		Role roleCreate = BaseRoleTest.createRoleViaMapper(BaseRoleTest.DataInput.getRole(), mapMapper);
//
//		BaseRoleTest.deleteRoleViaAction(roleCreate, mvc, Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss));
//	}
//	
//	@Test
//	public void testDeleteEx2() throws Exception {
//		Shared.printTestMethodStartInfo();
//		
//		Shared.caseLog("case2:没有权限进行操作");
//		
//		Role roleCreate = BaseRoleTest.createRoleViaMapper(BaseRoleTest.DataInput.getRole(), mapMapper);
//		
//		MvcResult mr2 = mvc.perform(//
//				get("/role/deleteEx.bx?" + Role.field.getFIELD_NAME_ID() + "=" + roleCreate.getID() + "&" + Role.field.getFIELD_NAME_bForceDelete() + "=1")//
//						.contentType(MediaType.APPLICATION_JSON)//
//						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfManager)))//
//				.andExpect(status().isOk())//
//				.andDo(print())//
//				.andReturn();
//		Shared.checkJSONErrorCode(mr2, EnumErrorCode.EC_NoPermission);
//		
//		// 测试完毕后删除数据
//		BaseRoleTest.deleteRoleViaAction(roleCreate, mvc, Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss));
//	}
//
//	// @Test
//	// public void testDeleteListEx() throws Exception {
//	// Shared.printTestMethodStartInfo();
//	//
//	// session = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);
//	// RoleMapper mapper = (RoleMapper) applicationContext.getBean("roleMapper");
//	// Role r = DataInput.getRole();
//	// Map<String, Object> createParam = r.getCreateParam(BaseBO.INVALID_CASE_ID,
//	// r);
//	// DataSourceContextHolder.setDbName(Shared.DBName_Test);
//	// Role createRole = (Role) mapper.create(createParam);
//	// Assert.assertEquals(EnumErrorCode.values()[Integer.parseInt(createParam.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())],
//	// EnumErrorCode.EC_NoError);
//	// r.setIgnoreIDInComparision(true);
//	// if (r.compareTo(createRole) != 0) {
//	// Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
//	// }
//	//
//	// Role r2 = DataInput.getRole();
//	// Map<String, Object> createParam2 = r2.getCreateParam(BaseBO.INVALID_CASE_ID,
//	// r2);
//	// DataSourceContextHolder.setDbName(Shared.DBName_Test);
//	// Role createRole2 = (Role) mapper.create(createParam2);
//	// Assert.assertEquals(EnumErrorCode.values()[Integer.parseInt(createParam2.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())],
//	// EnumErrorCode.EC_NoError);
//	// r2.setIgnoreIDInComparision(true);
//	// if (r2.compareTo(createRole2) != 0) {
//	// Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
//	// }
//	//
//	// MvcResult mr = mvc.perform(//
//	// get("/role/deleteListEx.bx?roleListID=" + createRole.getID() + "," +
//	// createRole2.getID())//
//	// .session((MockHttpSession) session)//
//	// .contentType(MediaType.APPLICATION_JSON)//
//	// )//
//	// .andExpect(status().isOk())//
//	// .andDo(print())//
//	// .andReturn();
//	// Shared.checkJSONErrorCode(mr);
//	//
//	// // 部分删除失败
//	// Role r3 = DataInput.getRole();
//	// Map<String, Object> createParam3 = r3.getCreateParam(BaseBO.INVALID_CASE_ID,
//	// r3);
//	// DataSourceContextHolder.setDbName(Shared.DBName_Test);
//	// Role createRole3 = (Role) mapper.create(createParam3);
//	// Assert.assertEquals(EnumErrorCode.values()[Integer.parseInt(createParam3.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())],
//	// EnumErrorCode.EC_NoError);
//	//
//	// r3.setIgnoreIDInComparision(true);
//	// if (r3.compareTo(createRole3) != 0) {
//	// Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
//	// }
//	//
//	// Role r4 = DataInput.getRole();
//	// Map<String, Object> createParam4 = r4.getCreateParam(BaseBO.INVALID_CASE_ID,
//	// r4);
//	// DataSourceContextHolder.setDbName(Shared.DBName_Test);
//	// Role createRole4 = (Role) mapper.create(createParam4);
//	// Assert.assertEquals(EnumErrorCode.values()[Integer.parseInt(createParam4.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())],
//	// EnumErrorCode.EC_NoError);
//	//
//	// r4.setIgnoreIDInComparision(true);
//	// if (r4.compareTo(createRole4) != 0) {
//	// Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
//	// }
//	//
//	// MvcResult mr2 = mvc.perform(//
//	// get("/role/deleteListEx.bx?roleListID=" + createRole3.getID() + ",4," +
//	// createRole4.getID())//
//	// .session((MockHttpSession) session)//
//	// .contentType(MediaType.APPLICATION_JSON)//
//	// )//
//	// .andExpect(status().isOk())//
//	// .andDo(print())//
//	// .andReturn();
//	// String json = mr2.getResponse().getContentAsString();
//	// JSONObject o = JSONObject.fromObject(json);
//	// String msg = JsonPath.read(o, "$.msg");
//	// if (msg.compareTo("选中的角色只有部分删除成功，请重试") != 0) {
//	// Assert.fail("错误码不正确！" + msg);
//	// }
//	//
//	// Shared.checkJSONErrorCode(mr2, EnumErrorCode.EC_PartSuccess);
//	// }
//
//	@Test
//	public void testCreateExCase1() throws Exception {
//		Shared.printTestMethodStartInfo();
//		Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);
//
//		Shared.caseLog("case1:正常创建");
//		Role role = BaseRoleTest.DataInput.getRole();
//		Role roleCreate = BaseRoleTest.createRoleViaAction(role, mvc, session);
//	
//		BaseRoleTest.deleteRoleViaAction(roleCreate, mvc, session);
//	}
//	
//	@Test
//	public void testCreateExCase2() throws Exception {
//		Shared.printTestMethodStartInfo();
//		
//		Shared.caseLog(" case2:创建相同名字。返回2");
//		//
//		Role role = BaseRoleTest.DataInput.getRole();
//		Role roleCreate = BaseRoleTest.createRoleViaAction(role, mvc, session);
//		
//		MvcResult mr2 = mvc.perform(//
//				post("/role/createEx.bx")//
//						.param(Role.field.getFIELD_NAME_name(), roleCreate.getName())//
//						.session((MockHttpSession) session)//
//						.contentType(MediaType.APPLICATION_JSON)//
//		)//
//				.andExpect(status().isOk())//
//				.andDo(print())//
//				.andReturn();
//
//		Shared.checkJSONErrorCode(mr2, EnumErrorCode.EC_Duplicated);
//		
//		BaseRoleTest.deleteRoleViaAction(roleCreate, mvc, session);
//	}
//	
//	@Test
//	public void testCreateExCase3() throws Exception {
//		Shared.printTestMethodStartInfo();
//		
//		Shared.caseLog("case3:创建的名字包含符号");
//		// 
//		MvcResult mr3 = mvc.perform(//
//				post("/role/createEx.bx")//
//						.param(Role.field.getFIELD_NAME_name(), "店长@#$")//
//						.session((MockHttpSession) session)//
//						.contentType(MediaType.APPLICATION_JSON)//
//		)//
//				.andExpect(status().isOk())//
//				.andDo(print())//
//				.andReturn();
//
//		Shared.checkJSONErrorCode(mr3, EnumErrorCode.EC_OtherError);
//	}
//	
//	@Test
//	public void testCreateExCase4() throws Exception {
//		Shared.printTestMethodStartInfo();
//		
//		Shared.caseLog("case4:创建的名字长度超过12位");
//		//
//		MvcResult mr4 = mvc.perform(//
//				post("/role/createEx.bx")//
//						.param(Role.field.getFIELD_NAME_name(), "店长店长店长店长店长店长xxxxxxxxxx")//
//						.session((MockHttpSession) session)//
//						.contentType(MediaType.APPLICATION_JSON)//
//		)//
//				.andExpect(status().isOk())//
//				.andDo(print())//
//				.andReturn();
//
//		Shared.checkJSONErrorCode(mr4, EnumErrorCode.EC_OtherError);
//	}
//	
//	@Test
//	public void testCreateExCase5() throws Exception {
//		Shared.printTestMethodStartInfo();
//		
//		Shared.caseLog("case5:创建的名字为空");
//		// 
//		MvcResult mr5 = mvc.perform(//
//				post("/role/createEx.bx")//
//						.param(Role.field.getFIELD_NAME_name(), "")//
//						.session((MockHttpSession) session)//
//						.contentType(MediaType.APPLICATION_JSON)//
//		)//
//				.andExpect(status().isOk())//
//				.andDo(print())//
//				.andReturn();
//
//		Shared.checkJSONErrorCode(mr5, EnumErrorCode.EC_OtherError);
//	}
//	
//	@Test
//	public void testCreateExCase6() throws Exception {
//		Shared.printTestMethodStartInfo();
//		
//		Shared.caseLog("case6:没有权限进行操作");
//		MvcResult mr6 = mvc.perform(//
//				post("/role/createEx.bx")//
//						.param(Role.field.getFIELD_NAME_name(), "删除12")//
//						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfManager))//
//						.contentType(MediaType.APPLICATION_JSON)//
//		)//
//				.andExpect(status().isOk())//
//				.andDo(print())//
//				.andReturn();
//		Shared.checkJSONErrorCode(mr6, EnumErrorCode.EC_NoPermission);
//	}
//
//	@Test
//	public void testUpdateExCase1() throws Exception {
//		Shared.printTestMethodStartInfo();
//
//		Shared.caseLog("正确更新");
//		//
//		Role roleCreate = BaseRoleTest.createRoleViaMapper(BaseRoleTest.DataInput.getRole(), mapMapper);
//		
//		String name = String.valueOf(System.currentTimeMillis()).substring(6);
//		roleCreate.setName(name);
//		BaseRoleTest.updateRoleViaAction(roleCreate, mvc, Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss));
//		
//		BaseRoleTest.deleteRoleViaAction(roleCreate, mvc, Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss));
//	}
//
//	@Test
//	public void testUpdateExCase2() throws Exception {
//		Shared.printTestMethodStartInfo();
//		
//		Shared.caseLog("更新已有名称的");
//		
//		Role roleCreate = BaseRoleTest.createRoleViaMapper(BaseRoleTest.DataInput.getRole(), mapMapper);
//		
//		MvcResult mr2 = mvc.perform(//
//				post("/role/updateEx.bx")//
//						.param(Role.field.getFIELD_NAME_ID(), "3")//
//						.param(Role.field.getFIELD_NAME_name(), roleCreate.getName())//
//						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//
//						.contentType(MediaType.APPLICATION_JSON)//
//		)//
//				.andExpect(status().isOk())//
//				.andDo(print())//
//				.andReturn();
//		Shared.checkJSONErrorCode(mr2, EnumErrorCode.EC_Duplicated);
//		
//		BaseRoleTest.deleteRoleViaAction(roleCreate, mvc, Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss));
//	}
//	
//	@Test
//	public void testUpdateExCase3() throws Exception {
//		Shared.printTestMethodStartInfo();
//		
//		Shared.caseLog("角色名字为符号。返回3");
//		//
//		MvcResult mr10 = mvc.perform(//
//				post("/role/updateEx.bx")//
//						.param(Role.field.getFIELD_NAME_ID(), "4")//
//						.param(Role.field.getFIELD_NAME_name(), "店长@￥#￥")//
//						.session((MockHttpSession) session)//
//						.contentType(MediaType.APPLICATION_JSON)//
//		)//
//				.andExpect(status().isOk())//
//				.andDo(print())//
//				.andReturn();
//		Shared.checkJSONErrorCode(mr10, EnumErrorCode.EC_OtherError);
//	}
//	
//	@Test
//	public void testUpdateExCase4() throws Exception {
//		Shared.printTestMethodStartInfo();
//		
//		Shared.caseLog("修改的角色名字不能超过12位");
//		//
//		MvcResult mr11 = mvc.perform(//
//				post("/role/updateEx.bx")//
//						.param(Role.field.getFIELD_NAME_ID(), "4")//
//						.param(Role.field.getFIELD_NAME_name(), "测试修改的角色名字是否能超过12位中英文数字")//
//						.session((MockHttpSession) session)//
//						.contentType(MediaType.APPLICATION_JSON)//
//		)//
//				.andExpect(status().isOk())//
//				.andDo(print())//
//				.andReturn();
//		Shared.checkJSONErrorCode(mr11, EnumErrorCode.EC_OtherError);
//	}
//	
//	@Test
//	public void testUpdateExCase5() throws Exception {
//		Shared.printTestMethodStartInfo();
//		
//		Shared.caseLog("修改的角色名字是否可以为空");
//		//
//		MvcResult mr12 = mvc.perform(//
//				post("/role/updateEx.bx")//
//						.param(Role.field.getFIELD_NAME_ID(), "4")//
//						.param(Role.field.getFIELD_NAME_name(), "")//
//						.session((MockHttpSession) session)//
//						.contentType(MediaType.APPLICATION_JSON)//
//		)//
//				.andExpect(status().isOk())//
//				.andDo(print())//
//				.andReturn();
//		Shared.checkJSONErrorCode(mr12, EnumErrorCode.EC_OtherError);
//	}
//	
//	@Test
//	public void testUpdateExCase6() throws Exception {
//		Shared.printTestMethodStartInfo();
//		
//		Shared.caseLog("没有权限进行操作");
//		
//		Role roleCreate = BaseRoleTest.createRoleViaMapper(BaseRoleTest.DataInput.getRole(), mapMapper);
//		
//		MvcResult mr13 = mvc.perform(//
//				post("/role/updateEx.bx")//
//						.param(Role.field.getFIELD_NAME_ID(), String.valueOf(roleCreate.getID()))//
//						.param(Role.field.getFIELD_NAME_name(), "测试")//
//						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfManager))//
//						.contentType(MediaType.APPLICATION_JSON)//
//		)//
//				.andExpect(status().isOk())//
//				.andDo(print())//
//				.andReturn();
//		Shared.checkJSONErrorCode(mr13, EnumErrorCode.EC_NoPermission);
//	}
//
//	@Test
//	public void testRetrieveAlsoStaffEx() throws Exception {
//		Shared.printTestMethodStartInfo();
//		Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);
//
//		Shared.caseLog("CASE1: 用存在的ID查询");
//		// Role r = new Role();
//		MvcResult mr = mvc.perform(//
//				post("/role/retrieveAlsoStaffEx.bx")//
//						.param(Role.field.getFIELD_NAME_ID(), "4")//
//						.session((MockHttpSession) session)//
//						.contentType(MediaType.APPLICATION_JSON)//
//		)//
//				.andExpect(status().isOk())//
//				.andDo(print())//
//				.andReturn();
//		Shared.checkJSONErrorCode(mr);
//
//		Shared.caseLog("CASE2: 用不存在的ID查询");
//		MvcResult mr1 = mvc.perform(//
//				post("/role/retrieveAlsoStaffEx.bx")//
//						.param(Role.field.getFIELD_NAME_ID(), "999999999")//
//						.param(Role.field.getFIELD_NAME_name(), "")//
//						.session((MockHttpSession) session)//
//						.contentType(MediaType.APPLICATION_JSON)//
//		)//
//				.andExpect(status().isOk())//
//				.andDo(print())//
//				.andReturn();
//		Shared.checkJSONErrorCode(mr1);
//
//		Shared.caseLog("CASE3 :没有权限查询");
//		MvcResult mr2 = mvc.perform(//
//				post("/role/retrieveAlsoStaffEx.bx")//
//						.param(Role.field.getFIELD_NAME_ID(), "4")//
//						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfManager))//
//						.contentType(MediaType.APPLICATION_JSON)//
//		)//
//				.andExpect(status().isOk())//
//				.andDo(print())//
//				.andReturn();
//		Shared.checkJSONErrorCode(mr2, EnumErrorCode.EC_NoPermission);
//	}
//
//	@SuppressWarnings("unchecked")
//	@Test
//	public void testGetStaffOpenid() {
//		Role r = new Role();
//		r.setID(4);
//		DataSourceContextHolder.setDbName(Shared.DBName_Test);
//		List<?> ls = roleBO.retrieveNObject(BaseBO.SYSTEM, BaseBO.CASE_Role_RetrieveAlsoStaff, r);
//		System.out.println("查询出来的集合：" + ls.get(1));
//		List<Staff> slist = (List<Staff>) ls.get(1);
//		System.out.println("获取到的Staff" + slist.get(0).getOpenid());
//	}
}
