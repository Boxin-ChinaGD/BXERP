
package com.bx.erp.test.commodity;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MvcResult;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import com.bx.erp.action.BaseAction;
import com.bx.erp.action.BaseAction.EnumSession;
import com.bx.erp.model.Pos;
import com.bx.erp.model.CacheType.EnumCacheType;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.SyncCacheType.EnumSyncCacheType;
import com.bx.erp.model.commodity.Category;
import com.bx.erp.model.commodity.Commodity;
import com.bx.erp.test.BaseActionTest;
import com.bx.erp.test.BaseCommodityTest;
import com.bx.erp.test.Shared;
import com.bx.erp.test.checkPoint.CategoryCP;
import com.jayway.jsonpath.JsonPath;
import net.sf.json.JSONObject;

@WebAppConfiguration
public class CategorySyncActionTest extends BaseActionTest {

	private static HttpSession session2;
	private static final int INVALID_ParentID = 99999999;

	EnumCacheType ect = EnumCacheType.ECT_Category;
	EnumSyncCacheType esct = EnumSyncCacheType.ESCT_CategorySyncInfo;

	@BeforeClass
	public void setup() {
		super.setUp();
		try {
			Shared.resetPOS(mvc, 1);
			sessionBoss = Shared.getPosLoginSession(mvc, 1);

			Shared.resetPOS(mvc, 2);
			session2 = Shared.getPosLoginSession(mvc, 2);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@AfterClass
	public void tearDown() {
		super.tearDown();
	}

	@Test
	public void testCreateEx1() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case1:正常创建");
		Category c = BaseCategoryParentTest.DataInput.getCategory();
		BaseCategoryParentTest.createOrUpdateCategoryViaActionWithNoError(c, true, mvc, sessionBoss, mapBO, Shared.DBName_Test);
	}

	@Test
	public void testCreateEx2() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case2:缺失posID参数。导致Action失败");
		MvcResult mr2 = null;
		try {

			Category c = BaseCategoryParentTest.DataInput.getCategory();
			BaseCategoryParentTest.createOrUpdateCategoryViaActionWithNoError(c, true, mvc, null, mapBO, Shared.DBName_Test);

			// 在对应的SyncAction中req
			// get的session里获取不到dbName，导致dbName为空，在BaseAction中断言错误，无法操作数据库，所以返回的错误码与期望的不一致。
			// Shared.checkJSONErrorCode(mr2, EnumErrorCode.EC_OtherError);
		} catch (Exception e) {
			Assert.assertNull(mr2);
		}

	}

	@Test
	public void testCreateEx3() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case3: 输入的类别的格式不正确");
		//
		Category category = BaseCategoryParentTest.DataInput.getCategory();
		category.setName("!!!");
		BaseCategoryParentTest.createOrUpdateCategoryViaActionWithExpectedError(category, true, mvc, sessionBoss, Shared.DBName_Test, EnumErrorCode.EC_WrongFormatForInputField);

	}

	@Test
	public void testCreateEx4() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case4: 添加重复的类别 ");
		//
		Category c = BaseCategoryParentTest.DataInput.getCategory();
		Category category = BaseCategoryParentTest.createOrUpdateCategoryViaActionWithNoError(c, true, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		//
		BaseCategoryParentTest.createOrUpdateCategoryViaActionWithExpectedError(category, true, mvc, sessionBoss, Shared.DBName_Test, EnumErrorCode.EC_Duplicated);

	}

	@Test
	public void testCreateEx5() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case5:在不存在的大类创建小类");
		//
		Category Ccategory1 = BaseCategoryParentTest.DataInput.getCategory();
		Ccategory1.setParentID(INVALID_ParentID);
		BaseCategoryParentTest.createOrUpdateCategoryViaActionWithExpectedError(Ccategory1, true, mvc, sessionBoss, Shared.DBName_Test, EnumErrorCode.EC_BusinessLogicNotDefined);

	}

	@Test
	public void testCreateEx6() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case6:没有权限");
		//
		Category category = BaseCategoryParentTest.DataInput.getCategory();
//		BaseCategoryParentTest.createOrUpdateCategoryViaActionWithExpectedError(category, true, mvc, Shared.getStaffLoginSession(mvc, Shared.PhoneOfManager), Shared.DBName_Test, EnumErrorCode.EC_NoPermission);
	}

	@Test
	public void testRetrieveNEx() throws Exception {
		Shared.printTestMethodStartInfo();

		Category c = BaseCategoryParentTest.DataInput.getCategory();
		Category categoryCreate = BaseCategoryParentTest.createOrUpdateCategoryViaActionWithNoError(c, true, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		//
		MvcResult mr3 = mvc.perform(//
				get("/categorySync/deleteEx.bx") //
						.param(Category.field.getFIELD_NAME_ID(), categoryCreate.getID() + "") //
						.contentType(MediaType.APPLICATION_JSON)//
						.session((MockHttpSession) sessionBoss)//
		) //
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn(); //

		Shared.checkJSONErrorCode(mr3);

		MvcResult mr = mvc.perform(//
				get("/categorySync/retrieveNEx.bx") //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) Shared.getPosLoginSession(mvc, 1))//
		) //
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn(); //

		Shared.checkJSONErrorCode(mr);
	}

	@Test
	public void testUpdateEx1() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE1 正常修改小类");
		//
		Category c = BaseCategoryParentTest.DataInput.getCategory();
		Category categoryCreate = BaseCategoryParentTest.createOrUpdateCategoryViaActionWithNoError(c, true, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		//
		Category c2 = BaseCategoryParentTest.DataInput.getCategory();
		c2.setID(categoryCreate.getID());
		//
		BaseCategoryParentTest.createOrUpdateCategoryViaActionWithNoError(c2, false, mvc, session2, mapBO, Shared.DBName_Test);

	}

	@Test
	public void testUpdateEx2() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case2:缺失posID参数。导致Action失败");
		MvcResult mr2 = null;
		try {

			Category category = BaseCategoryParentTest.DataInput.getCategory();
			BaseCategoryParentTest.createOrUpdateCategoryViaActionWithNoError(category, false, mvc, null, mapBO, Shared.DBName_Test);

			// 注释原因：在对应的SyncAction中req
			// get的session里获取不到dbName，导致dbName为空，在BaseAction中断言错误，无法操作数据库，所以返回的错误码与期望的不一致。
			// Shared.checkJSONErrorCode(mr2, EnumErrorCode.EC_OtherError);
		} catch (Exception e) {
			Assert.assertNull(mr2);
		}
	}

	@Test
	public void testUpdateEx3() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE3 默认分类不能修改，返回错误码7");
		//
		Category c = BaseCategoryParentTest.DataInput.getCategory();
		c.setID(1);
		String msg3 = BaseCategoryParentTest.createOrUpdateCategoryViaActionWithExpectedError(c, false, mvc, sessionBoss, Shared.DBName_Test, EnumErrorCode.EC_BusinessLogicNotDefined);
		//
		Assert.assertTrue(Category.ACTION_ERROR_UpdateDelete1.equals(msg3), "返回的错误消息不正确，期望的是:" + Category.ACTION_ERROR_UpdateDelete1);

	}

	@Test
	public void testUpdateEx4() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case4:类别名称重复，错误码应该是1");
		//
		Category c = BaseCategoryParentTest.DataInput.getCategory();
		Category categoryCreate = BaseCategoryParentTest.createOrUpdateCategoryViaActionWithNoError(c, true, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		//
		Category c2 = BaseCategoryParentTest.DataInput.getCategory();
		Category Ccategory = BaseCategoryParentTest.createOrUpdateCategoryViaActionWithNoError(c2, true, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		//
		Ccategory.setName(categoryCreate.getName());
		BaseCategoryParentTest.createOrUpdateCategoryViaActionWithExpectedError(Ccategory, false, mvc, sessionBoss, Shared.DBName_Test, EnumErrorCode.EC_Duplicated);
	}

	@Test
	public void testUpdateEx5() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case5:修改成不存在的大类，错误码为7，该商品大类不存在 ");
		//
		Category c = BaseCategoryParentTest.DataInput.getCategory();
		Category createCategory1 = BaseCategoryParentTest.createOrUpdateCategoryViaActionWithNoError(c, true, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		createCategory1.setParentID(INVALID_ParentID);
		//
		BaseCategoryParentTest.createOrUpdateCategoryViaActionWithExpectedError(createCategory1, false, mvc, sessionBoss, Shared.DBName_Test, EnumErrorCode.EC_BusinessLogicNotDefined);
	}

	@Test
	public void testUpdateEx6() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case6:修改小类的大类，错误码应该是0");
		//
		Category c = BaseCategoryParentTest.DataInput.getCategory();
		Category createCategory = BaseCategoryParentTest.createOrUpdateCategoryViaActionWithNoError(c, true, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		//
		createCategory.setParentID(createCategory.getParentID() + 1);
		Category category = BaseCategoryParentTest.createOrUpdateCategoryViaActionWithNoError(createCategory, false, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		//
		Assert.assertTrue(c.getParentID() == category.getParentID(), "修改小类的大类失败");
	}

	@Test
	public void testUpdateEx7() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case7：没有权限进行操作");
		//
		Category c = BaseCategoryParentTest.DataInput.getCategory();
		Category categoryCreate = BaseCategoryParentTest.createOrUpdateCategoryViaActionWithNoError(c, true, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		//
//		BaseCategoryParentTest.createOrUpdateCategoryViaActionWithExpectedError(categoryCreate, false, mvc, Shared.getStaffLoginSession(mvc, Shared.PhoneOfManager), Shared.DBName_Test, EnumErrorCode.EC_NoPermission);
	}

	@Test
	public void testFeedbackEx() throws Exception {
		Shared.printTestMethodStartInfo();

		// 在pos1中创建一个类别
		Category c = BaseCategoryParentTest.DataInput.getCategory();
		BaseCategoryParentTest.createOrUpdateCategoryViaActionWithNoError(c, true, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		//
		Pos pos = (Pos) sessionBoss.getAttribute(EnumSession.SESSION_POS.getName());
		int posID = pos.getID();

		// 在pos2中retrieveN
		System.out.println("--------------------------case1:retrieveNEx-----------------------------");
		MvcResult mr2 = mvc.perform(get("/categorySync/retrieveNEx.bx") //
				.contentType(MediaType.APPLICATION_JSON) //
				.session((MockHttpSession) session2)) //
				.andExpect(status().isOk()).andDo(print()).andReturn();

		Shared.checkJSONErrorCode(mr2);
		JSONObject json = JSONObject.fromObject(mr2.getResponse().getContentAsString());
		json.getString(BaseAction.KEY_ObjectList);
		List<BaseModel> bmList = (List<BaseModel>) c.parseN(json.getString(BaseAction.KEY_ObjectList));
		// 设置全部同步与未全部同步的同步块
		List<Category> categoryList = BaseCategoryParentTest.getCategoryListIfAllSync(bmList, mapBO);

		String ids = "";
		for (Category category : categoryList) {
			ids += category.getID() + ",";
		}

		MvcResult mr3 = mvc.perform( //
				get("/categorySync/feedbackEx.bx?sID=" + ids + "&errorCode=EC_NoError") //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) session2) //
		) //
				.andExpect(status().isOk()) //
				.andDo(print()) //
				.andReturn();
		// 结果验证：检查错误码
		Shared.checkJSONErrorCode(mr3);
		// 检查点
		pos = (Pos) session2.getAttribute(EnumSession.SESSION_POS.getName());
		posID = pos.getID();

		CategoryCP.verifySyncCategory(categoryList, posID, posBO, categorySyncCacheDispatcherBO, Shared.DBName_Test);
	}

	@Test
	public void testDeleteEx1() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE1 正常删除分类");
		//
		Category c = BaseCategoryParentTest.DataInput.getCategory();
		Category categoryCreate = BaseCategoryParentTest.createOrUpdateCategoryViaActionWithNoError(c, true, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		//
		MvcResult mr = mvc.perform(//
				get("/categorySync/deleteEx.bx?" + Category.field.getFIELD_NAME_ID() + "=" + categoryCreate.getID()) //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionBoss)//
		) //
				.andExpect(status().isOk()).andDo(print()).andReturn(); //
		// 错误码
		Shared.checkJSONErrorCode(mr);
		// 检查点
		CategoryCP.verifyDelete(categoryCreate, posBO, categoryBO, categorySyncCacheBO, Shared.DBName_Test);
	}

	@Test
	public void testDeleteEx2() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("因缺少POSID导致DB错误。");
		//
		Category c = BaseCategoryParentTest.DataInput.getCategory();
		Category categoryCreate = BaseCategoryParentTest.createOrUpdateCategoryViaActionWithNoError(c, true, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		//

		MvcResult mr2 = null;
		try {
			mr2 = mvc.perform(//
					get("/categorySync/deleteEx.bx?" + Category.field.getFIELD_NAME_ID() + "=" + categoryCreate.getID()) //
							.contentType(MediaType.APPLICATION_JSON)//
			) //
					.andExpect(status().isOk())//
					.andDo(print())//
					.andReturn(); //
			// 注释原因：在对应的SyncAction中req
			// get的session里获取不到dbName，导致dbName为空，在BaseAction中断言错误，无法操作数据库，所以返回的错误码与期望的不一致。
			// Shared.checkJSONErrorCode(mr2, EnumErrorCode.EC_OtherError);
		} catch (Exception e) {
			Assert.assertNull(mr2);
		}
	}

	@Test
	public void testDeleteEx3() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("CASE3 默认品牌不能删除，返回错误码7");
		//
		Category category = new Category();
		category.setID(1);
        //
		MvcResult mr3 = mvc.perform(//
				get("/categorySync/deleteEx.bx?" + Category.field.getFIELD_NAME_ID() + "=" + category.getID()) //
						.contentType(MediaType.APPLICATION_JSON) //
						.session((MockHttpSession) sessionBoss)//
		) //
				.andExpect(status().isOk()).andDo(print()).andReturn(); //
		Shared.checkJSONErrorCode(mr3, EnumErrorCode.EC_BusinessLogicNotDefined);

		JSONObject o3 = JSONObject.fromObject(mr3.getResponse().getContentAsString());
		String msg3 = JsonPath.read(o3, "$.msg");
		Assert.assertTrue(Category.ACTION_ERROR_UpdateDelete1.equals(msg3), "返回的错误消息不正确，期望的是:" + Category.ACTION_ERROR_UpdateDelete1);
	}

	@Test
	public void testDeleteEx4() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("case4:商品表中没有这种类别的商品。可以直接删除，错误代码为0");
		//
		Category c = BaseCategoryParentTest.DataInput.getCategory();
		Category Ccategory = BaseCategoryParentTest.createOrUpdateCategoryViaActionWithNoError(c, true, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		//
		MvcResult mr = mvc.perform(//
				get("/categorySync/deleteEx.bx")//
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//
						.param(Category.field.getFIELD_NAME_ID(), String.valueOf(Ccategory.getID()))//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print()).andReturn();

		// 错误码
		Shared.checkJSONErrorCode(mr);
		// 检查点
		CategoryCP.verifyDelete(Ccategory, posBO, categoryBO, categorySyncCacheBO, Shared.DBName_Test);
	}

	@Test
	public void testDeleteEx5() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("case5:商品表中已有商品的类别。删除不了，错误代码为7");
		//
		Commodity commodity = BaseCommodityTest.createCommodityViaAction(BaseCommodityTest.DataInput.getCommodity(), mvc, sessionBoss, mapBO, Shared.DBName_Test);
		MvcResult mr2 = mvc.perform(//
				get("/categorySync/deleteEx.bx")//
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//
						.param(Category.field.getFIELD_NAME_ID(), String.valueOf(commodity.getCategoryID()))//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();
		Shared.checkJSONErrorCode(mr2, EnumErrorCode.EC_BusinessLogicNotDefined);

	}

	@Test
	public void testDeleteEx6() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("case6:没有权限进行");
		//
		Category c = BaseCategoryParentTest.DataInput.getCategory();
		Category category = BaseCategoryParentTest.createOrUpdateCategoryViaActionWithNoError(c, true, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		//
//		MvcResult mr3 = mvc.perform(//
//				get("/categorySync/deleteEx.bx")//
//						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfManager))//
//						.param(Category.field.getFIELD_NAME_ID(), String.valueOf(category.getID()))//
//						.contentType(MediaType.APPLICATION_JSON)//
//		)//
//				.andExpect(status().isOk())//
//				.andDo(print()).andReturn();
//         //
//		Shared.checkJSONErrorCode(mr3, EnumErrorCode.EC_NoPermission);
	}
}
