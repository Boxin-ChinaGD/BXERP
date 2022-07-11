package com.bx.erp.test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.testng.Assert.assertTrue;

import java.util.List;
import java.util.Map;
import java.util.UUID;

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
import com.bx.erp.model.VipCategory;
import com.bx.erp.util.DataSourceContextHolder;
import com.jayway.jsonpath.JsonPath;

import net.sf.json.JSONObject;

@WebAppConfiguration
public class VipCategoryActionTest extends BaseActionTest {

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
		System.out.println("-----------case1:用一个存在的id进行查询-------------");
		MvcResult mr = mvc.perform(//
				get("/vipCategory/retrieve1Ex.bx?ID=1").session(//
						(MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss)//
				).contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk()).andDo(print()).andReturn();
		Shared.checkJSONErrorCode(mr);

		JSONObject o1 = JSONObject.fromObject(mr.getResponse().getContentAsString());
		Integer i1 = JsonPath.read(o1, "$.vipCategory.ID");
		assertTrue(i1 == 1);

		System.out.println("-----------case2:用一个不存在的id进行查询-------------");
		MvcResult mr2 = mvc.perform(//
				get("/vipCategory/retrieve1Ex.bx?ID=-11").session(//
						(MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss)//
				).contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk()).andDo(print()).andReturn();
		Shared.checkJSONErrorCode(mr2);

		JSONObject o2 = JSONObject.fromObject(mr2.getResponse().getContentAsString());
		String s2 = o2.getString("vipCategory");
		assertTrue("null".equals(s2));

		System.out.println("-----------case3:没有权限进行查询-------------");
//		MvcResult mr3 = mvc.perform(//
//				get("/vipCategory/retrieve1Ex.bx?ID=1").session(//
//						(MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfManager)//
//				).contentType(MediaType.APPLICATION_JSON)//
//		)//
//				.andExpect(status().isOk()).andDo(print()).andReturn();
//		Shared.checkJSONErrorCode(mr3, EnumErrorCode.EC_NoPermission);
	}

	@Test
	public void testRetrieveNEx() throws Exception {
		Shared.printTestMethodStartInfo();

		// VipCategory vc = new VipCategory();
		MvcResult mcreate = mvc.perform(post("/vipCategory/createEx.bx").session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//
				.param(VipCategory.field.getFIELD_NAME_name(), "黄金会员1" + System.currentTimeMillis() % 1000000)//
				.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk()).andDo(print()).andReturn();

		Shared.checkJSONErrorCode(mcreate);
		JSONObject o1 = JSONObject.fromObject(mcreate.getResponse().getContentAsString());
		Integer i1 = JsonPath.read(o1, "$.vipCategoryList.ID");

		MvcResult mr = mvc.perform(//
				post("/vipCategory/retrieveNEx.bx")//
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk()).andDo(print()).andReturn();
		Shared.checkJSONErrorCode(mr);

		JSONObject o2 = JSONObject.fromObject(mr.getResponse().getContentAsString());
		List<Integer> list2 = JsonPath.read(o2, "$.vipCategoryList[*].ID");
		assertTrue(list2.contains(i1));

		System.out.println("------------------------case2：没有权限进行操作-------------------------------------");
//		MvcResult mr2 = mvc.perform(//
//				post("/vipCategory/retrieveNEx.bx")//
//						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfManager))//
//						.contentType(MediaType.APPLICATION_JSON)//
//		)//
//				.andExpect(status().isOk()).andDo(print()).andReturn();
//		Shared.checkJSONErrorCode(mr2, EnumErrorCode.EC_NoPermission);
	}

	@Test
	public void testCreateEx() throws Exception {
		Shared.printTestMethodStartInfo();

		// VipCategory vc = new VipCategory();
		System.out.println("\n------------------------ Case1: 正常创建 ----------------------");
		MvcResult mr = mvc.perform(post("/vipCategory/createEx.bx").session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//
				.param(VipCategory.field.getFIELD_NAME_name(), "黄金会员1" + System.currentTimeMillis() % 1000000).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andDo(print()).andReturn();
		Shared.checkJSONErrorCode(mr);

		System.out.println("\n------------------------ Case2：会员类别名称重复----------------------");
		MvcResult mr2 = mvc.perform(post("/vipCategory/createEx.bx").session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//
				.param(VipCategory.field.getFIELD_NAME_name(), "黄金会员").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andDo(print()).andReturn();
		Shared.checkJSONErrorCode(mr2, EnumErrorCode.EC_Duplicated);

		System.out.println("\n------------------------ Case3:没有权限进行操作 ----------------------");
//		MvcResult mr3 = mvc.perform(post("/vipCategory/createEx.bx").session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfManager))//
//				.param(VipCategory.field.getFIELD_NAME_name(), "黄金会员1" + System.currentTimeMillis() % 1000000).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andDo(print()).andReturn();
//		Shared.checkJSONErrorCode(mr3, EnumErrorCode.EC_NoPermission);
	}

	@Test
	public void testUpdateEx() throws Exception {
		Shared.printTestMethodStartInfo();

		VipCategory vc1 = new VipCategory();
		vc1.setName("会员分类" + System.currentTimeMillis() % 1000000);

		Map<String, Object> params = vc1.getCreateParam(BaseBO.INVALID_CASE_ID, vc1);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		VipCategory vcCreated = (com.bx.erp.model.VipCategory) vipCategoryMapper.create(params);
		vc1.setIgnoreIDInComparision(true);
		if (vc1.compareTo(vcCreated) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		System.out.println("\n------------------------ case 1:会员类别名称重复，错误码应该是1----------------------");
		// VipCategory vc = new VipCategory();
		MvcResult mr = mvc.perform(//
				post("/vipCategory/updateEx.bx")//
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//
						.param(VipCategory.field.getFIELD_NAME_ID(), String.valueOf(vcCreated.getID()))//
						.param(VipCategory.field.getFIELD_NAME_name(), "黄金会员")//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr, EnumErrorCode.EC_Duplicated);

		System.out.println("\n------------------------ case 2: 会员类别名称不重复，错误码应该是0 修改成功----------------------");
		MvcResult mr2 = mvc.perform(//
				post("/vipCategory/updateEx.bx")//
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//
						.param(VipCategory.field.getFIELD_NAME_ID(), String.valueOf(vcCreated.getID()))//
						.param(VipCategory.field.getFIELD_NAME_name(), "黄金会员2" + UUID.randomUUID().toString().substring(1, 7))//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr2);

		System.out.println("\n------------------------ case 3:没有权限进行操作----------------------");
//		MvcResult mr3 = mvc.perform(//
//				post("/vipCategory/updateEx.bx")//
//						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfManager))//
//						.param(VipCategory.field.getFIELD_NAME_ID(), String.valueOf(vcCreated.getID()))//
//						.param(VipCategory.field.getFIELD_NAME_name(), "黄金会员2" + UUID.randomUUID().toString().substring(1, 7))//
//						.contentType(MediaType.APPLICATION_JSON)//
//		)//
//				.andExpect(status().isOk())//
//				.andDo(print())//
//				.andReturn();
//		Shared.checkJSONErrorCode(mr3, EnumErrorCode.EC_NoPermission);
	}

	@Test
	public void testDeleteEx() throws Exception {
		Shared.printTestMethodStartInfo();

		VipCategory vc = new VipCategory();
		vc.setName("会员分类" + System.currentTimeMillis() % 1000000);

		Map<String, Object> params = vc.getCreateParam(BaseBO.INVALID_CASE_ID, vc);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		VipCategory vcCreated = (com.bx.erp.model.VipCategory) vipCategoryMapper.create(params);
		vc.setIgnoreIDInComparision(true);
		if (vc.compareTo(vcCreated) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		Assert.assertNotNull(vcCreated);
		Assert.assertEquals(EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())], EnumErrorCode.EC_NoError);
		System.out.println("\n------------------------ CASE1:正常删除------------------------");
		MvcResult mr = mvc.perform(//
				post("/vipCategory/deleteEx.bx").session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//
						.param(VipCategory.field.getFIELD_NAME_ID(), String.valueOf(vcCreated.getID()))//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr);

		System.out.println("\n------------------------ case2:会员表中已有该会员的类别。删除不了，错误代码为7----------------------");
		MvcResult mr2 = mvc.perform(//
				post("/vipCategory/deleteEx.bx").session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//
						.param(VipCategory.field.getFIELD_NAME_ID(), String.valueOf(1))//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr2, EnumErrorCode.EC_BusinessLogicNotDefined);
		System.out.println("\n------------------------ case3:删除一个不存在的Id----------------------");
		MvcResult mr3 = mvc.perform(//
				post("/vipCategory/deleteEx.bx").session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//
						.param(VipCategory.field.getFIELD_NAME_ID(), String.valueOf(Shared.BIG_ID))//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr3);

		System.out.println("\n------------------------ CASE4：没有权限进行操作-----------------------");
//		MvcResult mr4 = mvc.perform(//
//				post("/vipCategory/deleteEx.bx").session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfManager))//
//						.param(VipCategory.field.getFIELD_NAME_ID(), String.valueOf(vcCreated.getID()))//
//						.contentType(MediaType.APPLICATION_JSON)//
//		)//
//				.andExpect(status().isOk())//
//				.andDo(print())//
//				.andReturn();
//		Shared.checkJSONErrorCode(mr4, EnumErrorCode.EC_NoPermission);
	}
}
