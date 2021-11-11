
package com.bx.erp.test.commodity;

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
import com.bx.erp.model.commodity.Category;
import com.bx.erp.test.BaseActionTest;
import com.bx.erp.test.Shared;
import com.jayway.jsonpath.JsonPath;

import net.sf.json.JSONObject;

@WebAppConfiguration
public class CategoryActionTest extends BaseActionTest {

	@BeforeClass
	public void setup() {

		super.setUp();
	}

	@AfterClass
	public void tearDown() {

		super.tearDown();
	}

	// 运行Jenkins会跑不过,因为售前账号会被设为离职状态,在sit自动化测试CommodityRelatedTest里进行测试
	// @Test
	// public void testUpdateEx6() throws Exception {
	// Shared.printTestMethodStartInfo();
	// Category categoryCreate = CreateCategory();
	// Shared.caseLog("case6：售前人员修改商品小类,没有权限进行操作");
	// MvcResult mr6 = mvc.perform(//
	// post("/category/updateEx.bx")//
	// .contentType(MediaType.APPLICATION_JSON)//
	// .session((MockHttpSession) Shared.getStaffLoginSession(mvc,
	// Shared.PhoneOfPreSale))//
	// .param(Category.field.getFIELD_NAME_ID(),
	// String.valueOf(categoryCreate.getID()))//
	// .param(Category.field.getFIELD_NAME_name(), categoryCreate.getName())//
	// .param(Category.field.getFIELD_NAME_parentID(),
	// String.valueOf(categoryCreate.getParentID()))//
	// )//
	// .andExpect(status().isOk())//
	// .andDo(print())//
	// .andReturn();
	//
	// Shared.checkJSONErrorCode(mr6, EnumErrorCode.EC_NoPermission);
	// }

	

	// 运行Jenkins会跑不过,因为售前账号会被设为离职状态,在sit自动化测试CommodityRelatedTest里进行测试
	// @Test
	// public void testCreateEx6() throws Exception {
	// Shared.printTestMethodStartInfo();
	// Category category = DataInput.getCategory();
	// Shared.caseLog("售前人员进行创建商品小类，没有权限");
	// MvcResult mr5 = mvc.perform(//
	// post("/category/createEx.bx")//
	// .session((MockHttpSession) Shared.getStaffLoginSession(mvc,
	// Shared.PhoneOfPreSale))//
	// .param(Category.field.getFIELD_NAME_name(), category.getName())//
	// .param(Category.field.getFIELD_NAME_parentID(),
	// String.valueOf(category.getParentID()))//
	// .contentType(MediaType.APPLICATION_JSON)//
	// )//
	// .andExpect(status().isOk())//
	// .andDo(print())//
	// .andReturn();
	//
	// Shared.checkJSONErrorCode(mr5, EnumErrorCode.EC_NoPermission);
	// }

	@Test
	public void testIndex() throws Exception {
		Shared.printTestMethodStartInfo();

		mvc.perform(//
				get("/category.bx").session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//
		)//
				.andExpect(status().isOk());

		// ... 检查错误码
	}

	@Test
	public void testRetrieveAllEx() throws Exception {
		Shared.printTestMethodStartInfo();

		MvcResult mr = mvc.perform(//
				post("/category/retrieveAllEx.bx")//
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//
						.param(Category.field.getFIELD_NAME_name(), "默认分类")//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		String json = mr.getResponse().getContentAsString();
		JSONObject o = JSONObject.fromObject(json);
		String err = JsonPath.read(o, "$.ERROR");
		if (err.compareTo(EnumErrorCode.EC_NoError.toString()) != 0) {
			Assert.fail("错误码不正确！" + err);
		}
		List<String> names = JsonPath.read(o, "$.categoryList[*].name");
		if (!names.contains("默认分类")) {
			Assert.fail("商品类别不存在！");
		}

		Category c2 = new Category();
		MvcResult mr2 = mvc.perform(//
				post("/category/retrieveAllEx.bx")//
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//
						.param(Category.field.getFIELD_NAME_name(), c2.getName())//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr2);

//		MvcResult mr3 = mvc.perform(//
//				post("/category/retrieveAllEx.bx")//
//						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfManager))//
//						.param(Category.field.getFIELD_NAME_name(), "默认分类")//
//						.contentType(MediaType.APPLICATION_JSON)//
//		)//
//				.andExpect(status().isOk())//
//				.andDo(print())//
//				.andReturn();
//
//		Shared.checkJSONErrorCode(mr3, EnumErrorCode.EC_NoPermission);
	}

	@Test
	public void testRetrieveNByParent() throws Exception {
		Shared.printTestMethodStartInfo();

		MvcResult mr = mvc.perform(//
				post("/category/retrieveNByParent.bx")//
						.param(Category.field.getFIELD_NAME_ID(), "2")//
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		Shared.checkJSONErrorCode(mr);

//		Shared.caseLog("case2:没有权限");
//		MvcResult mr1 = mvc.perform(//
//				post("/category/retrieveAllEx.bx")//
//						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfManager))//
//						.param(Category.field.getFIELD_NAME_name(), "默认分类")//
//						.contentType(MediaType.APPLICATION_JSON)//
//		)//
//				.andExpect(status().isOk())//
//				.andDo(print())//
//				.andReturn();
//		Shared.checkJSONErrorCode(mr1, EnumErrorCode.EC_NoPermission);
	}

	@Test
	public void testRetrieveNToCheckUniqueFieldEx() throws Exception {
		Shared.printTestMethodStartInfo();

		sessionBoss = Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss);

		Shared.caseLog("Case1:查询一个不存在的商品小类");
		MvcResult mr1 = mvc.perform(//
				post("/category/retrieveNToCheckUniqueFieldEx.bx")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)// \
						.param(Category.field.getFIELD_NAME_fieldToCheckUnique(), "1").param(Category.field.getFIELD_NAME_uniqueField(), "阿萨德佛该会否")//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr1);

		Shared.caseLog("Case2:查询一个已经存在的商品小类");
		MvcResult mr2 = mvc.perform(//
				post("/category/retrieveNToCheckUniqueFieldEx.bx")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)// \
						.param(Category.field.getFIELD_NAME_fieldToCheckUnique(), "1").param(Category.field.getFIELD_NAME_uniqueField(), "饮料")//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr2, EnumErrorCode.EC_Duplicated);

		Shared.caseLog("Case3:查询一个已经存在的商品小类,但传入的ID与已存在的商品小类的商品小类ID相同");
		MvcResult mr3 = mvc.perform(//
				post("/category/retrieveNToCheckUniqueFieldEx.bx")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)// \
						.param(Category.field.getFIELD_NAME_ID(), "2").param(Category.field.getFIELD_NAME_fieldToCheckUnique(), "1").param(Category.field.getFIELD_NAME_uniqueField(), "饮料")//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr3);

		Shared.caseLog("Case4:传入的商品小类名称格式不正确(有特殊字符)");
		MvcResult mr4 = mvc.perform(//
				post("/category/retrieveNToCheckUniqueFieldEx.bx")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)// \
						.param(Category.field.getFIELD_NAME_fieldToCheckUnique(), "1").param(Category.field.getFIELD_NAME_uniqueField(), "饮料$6")//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr4, EnumErrorCode.EC_WrongFormatForInputField);

		Shared.caseLog("Case5:传入的商品小类名称格式不正确(长度大于10)");
		MvcResult mr5 = mvc.perform(//
				post("/category/retrieveNToCheckUniqueFieldEx.bx")//
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)// \
						.param(Category.field.getFIELD_NAME_fieldToCheckUnique(), "1").param(Category.field.getFIELD_NAME_queryKeyword(), "12345678901")//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr5, EnumErrorCode.EC_WrongFormatForInputField);
	}
}
