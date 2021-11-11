package com.bx.erp.test.commodity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MvcResult;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import com.bx.erp.cache.SyncCacheManager;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.SyncCacheType.EnumSyncCacheType;
import com.bx.erp.model.commodity.Category;
import com.bx.erp.model.commodity.CategoryParent;
import com.bx.erp.model.commodity.CategorySyncCache;
import com.bx.erp.model.commodity.Commodity;
import com.bx.erp.test.BaseActionTest;
import com.bx.erp.test.BaseCommodityTest;
import com.bx.erp.test.Shared;
import com.bx.erp.test.checkPoint.CategoryParentCP;
import com.jayway.jsonpath.JsonPath;

import net.sf.json.JSONObject;

@WebAppConfiguration
public class CategoryParentActionTest extends BaseActionTest {

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

		Shared.caseLog("CASE1 正常修改分类");
		CategoryParent cParent = BaseCategoryParentTest.DataInput.getCategoryParent();
		CategoryParent categoryParentCreate = BaseCategoryParentTest.createOrUpdateCategoryParentViaActionWithNoError(cParent, true, mvc, sessionBoss, Shared.DBName_Test);
		//
		CategoryParent categoryParent = BaseCategoryParentTest.DataInput.getCategoryParent();
		categoryParent.setID(categoryParentCreate.getID());
		BaseCategoryParentTest.createOrUpdateCategoryParentViaActionWithNoError(categoryParent, false, mvc, sessionBoss, Shared.DBName_Test);
		BaseCategoryParentTest.deleteCategoryParent(categoryParent.getID());
	}

	@Test
	public void testUpdateEx2() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog(" case 2:修改大类为已有的名称，返回1");
		CategoryParent cParent = BaseCategoryParentTest.DataInput.getCategoryParent();
		CategoryParent categoryParent = BaseCategoryParentTest.createOrUpdateCategoryParentViaActionWithNoError(cParent, true, mvc, sessionBoss, Shared.DBName_Test);
		//
		CategoryParent cParent2 = BaseCategoryParentTest.DataInput.getCategoryParent();
		CategoryParent categoryParent2 = BaseCategoryParentTest.createOrUpdateCategoryParentViaActionWithNoError(cParent2, true, mvc, sessionBoss, Shared.DBName_Test);
		categoryParent2.setName(categoryParent.getName());
		//
		BaseCategoryParentTest.createOrUpdateCategoryParentViaActionWithExpectedError(categoryParent2, false, mvc, sessionBoss, Shared.DBName_Test,EnumErrorCode.EC_Duplicated);
		BaseCategoryParentTest.deleteCategoryParent(categoryParent.getID());
	}

	@Test
	public void testUpdateEx3() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case3:输入参数不合法。（中英文，数字，无空格，0-20）");
		CategoryParent cParent = BaseCategoryParentTest.DataInput.getCategoryParent();
		CategoryParent categoryParent = BaseCategoryParentTest.createOrUpdateCategoryParentViaActionWithNoError(cParent, true, mvc, sessionBoss, Shared.DBName_Test);
		categoryParent.setName("测试@#");
		//
		BaseCategoryParentTest.createOrUpdateCategoryParentViaActionWithExpectedError(categoryParent, false, mvc, sessionBoss, Shared.DBName_Test,EnumErrorCode.EC_WrongFormatForInputField);
		BaseCategoryParentTest.deleteCategoryParent(categoryParent.getID());
	}

	@Test
	public void testUpdateEx4() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE4 修改的分类名称中间有空格");
		CategoryParent cParent = BaseCategoryParentTest.DataInput.getCategoryParent();
		CategoryParent categoryParent = BaseCategoryParentTest.createOrUpdateCategoryParentViaActionWithNoError(cParent, true, mvc, sessionBoss, Shared.DBName_Test);		
		categoryParent.setName("测  试");
		//
		BaseCategoryParentTest.createOrUpdateCategoryParentViaActionWithExpectedError(categoryParent, false, mvc, sessionBoss, Shared.DBName_Test,EnumErrorCode.EC_WrongFormatForInputField);
		BaseCategoryParentTest.deleteCategoryParent(categoryParent.getID());
	}

	@Test
	public void testUpdateEx5() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE5 分类名称修改为空串");
		CategoryParent cParent = BaseCategoryParentTest.DataInput.getCategoryParent();
		CategoryParent categoryParent = BaseCategoryParentTest.createOrUpdateCategoryParentViaActionWithNoError(cParent, true, mvc, sessionBoss, Shared.DBName_Test);
		categoryParent.setName("");
		//
		BaseCategoryParentTest.createOrUpdateCategoryParentViaActionWithExpectedError(categoryParent, false, mvc, sessionBoss, Shared.DBName_Test,EnumErrorCode.EC_WrongFormatForInputField);
		BaseCategoryParentTest.deleteCategoryParent(categoryParent.getID());
	}

	@Test
	public void testUpdateEx6() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE6 分类名称修改为不合法");
		CategoryParent cParent = BaseCategoryParentTest.DataInput.getCategoryParent();
		CategoryParent categoryParent = BaseCategoryParentTest.createOrUpdateCategoryParentViaActionWithNoError(cParent, true, mvc, sessionBoss, Shared.DBName_Test);
		categoryParent.setName("修改商品大类的名字规定是不可以超过20位的超过则修改失败");
		//
		BaseCategoryParentTest.createOrUpdateCategoryParentViaActionWithExpectedError(categoryParent, false, mvc, sessionBoss, Shared.DBName_Test,EnumErrorCode.EC_WrongFormatForInputField);
		BaseCategoryParentTest.deleteCategoryParent(categoryParent.getID());
	}

	@Test
	public void testUpdateEx7() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case7:没有权限进行修改");
		CategoryParent cParent = BaseCategoryParentTest.DataInput.getCategoryParent();
		CategoryParent categoryParent = BaseCategoryParentTest.createOrUpdateCategoryParentViaActionWithNoError(cParent, true, mvc, sessionBoss, Shared.DBName_Test);
		//
//		BaseCategoryParentTest.createOrUpdateCategoryParentViaActionWithExpectedError(categoryParent, false, mvc, Shared.getStaffLoginSession(mvc, Shared.PhoneOfManager), Shared.DBName_Test, EnumErrorCode.EC_NoPermission);		
		BaseCategoryParentTest.deleteCategoryParent(categoryParent.getID());
	}

	@Test
	public void testUpdateEx8() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE8 默认的分类不能修改,返回错误码7");
        //
		CategoryParent cParent = BaseCategoryParentTest.DataInput.getCategoryParent();		
		cParent.setID(1);
		String msg = BaseCategoryParentTest.createOrUpdateCategoryParentViaActionWithExpectedError(cParent, false, mvc, sessionBoss, Shared.DBName_Test,EnumErrorCode.EC_BusinessLogicNotDefined);
		Assert.assertTrue(CategoryParent.ACTION_ERROR_UpdateDelete1.equals(msg), "返回的错误消息不正确，期望的是:" + CategoryParent.ACTION_ERROR_UpdateDelete1);
	}

	// 运行Jenkins会跑不过,因为售前账号会被设为离职状态,在sit自动化测试CommodityRelatedTest里进行测试
	// @Test
	// public void testUpdateEx9() throws Exception {
	// Shared.printTestMethodStartInfo();
	//
	// Shared.caseLog("case9:售前人员修改商品大类,没有权限进行修改");
	// CategoryParent categoryParent = CreateCategoryParent();
	// //
	// MvcResult mr9 = mvc.perform(//
	// post(false)//
	// .session((MockHttpSession) Shared.getStaffLoginSession(mvc,
	// Shared.PhoneOfPreSale))//
	// .contentType(MediaType.APPLICATION_JSON)//
	// .param(CategoryParent.field.getFIELD_NAME_ID(),
	// String.valueOf(categoryParent.getID()))//
	// .param(CategoryParent.field.getFIELD_NAME_name(), categoryParent.getName())//
	// )//
	// .andExpect(status().isOk())//
	// .andDo(print())//
	// .andReturn();
	//
	// Shared.checkJSONErrorCode(mr9, EnumErrorCode.EC_NoPermission);
	// DeleteCategoryParent(categoryParent.getID());
	// }

	@Test
	public void testCreateEx1() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("正常创建商品大类");
		CategoryParent categoryParent = BaseCategoryParentTest.DataInput.getCategoryParent();
		//
		BaseCategoryParentTest.createOrUpdateCategoryParentViaActionWithNoError(categoryParent, true, mvc, sessionBoss, Shared.DBName_Test);
	}

	@Test
	public void testCreateEx2() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case 2:新增大类为已有的名称，返回1");
		CategoryParent cParent = BaseCategoryParentTest.DataInput.getCategoryParent();
		CategoryParent categoryParent = BaseCategoryParentTest.createOrUpdateCategoryParentViaActionWithNoError(cParent, true, mvc, sessionBoss, Shared.DBName_Test);
		//
		BaseCategoryParentTest.createOrUpdateCategoryParentViaActionWithExpectedError(categoryParent, true, mvc, sessionBoss, Shared.DBName_Test, EnumErrorCode.EC_Duplicated);
	}

	@Test
	public void testCreateEx3() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case3:输入参数不合法。（中英文，数字，无空格，0-20）");
		
		CategoryParent categoryParent = BaseCategoryParentTest.DataInput.getCategoryParent();
		categoryParent.setName("测试用品@@@");
		BaseCategoryParentTest.createOrUpdateCategoryParentViaActionWithExpectedError(categoryParent, true, mvc, sessionBoss, Shared.DBName_Test, EnumErrorCode.EC_WrongFormatForInputField);
		//
		categoryParent.setName("测试  用品");
		BaseCategoryParentTest.createOrUpdateCategoryParentViaActionWithExpectedError(categoryParent, true, mvc, sessionBoss, Shared.DBName_Test, EnumErrorCode.EC_WrongFormatForInputField);		
		//
		categoryParent.setName("");
		BaseCategoryParentTest.createOrUpdateCategoryParentViaActionWithExpectedError(categoryParent, true, mvc, sessionBoss, Shared.DBName_Test, EnumErrorCode.EC_WrongFormatForInputField);		
		//
		categoryParent.setName("很快乐就返回来开会水电235536费拉克丝绝代风华");
		BaseCategoryParentTest.createOrUpdateCategoryParentViaActionWithExpectedError(categoryParent, true, mvc, sessionBoss, Shared.DBName_Test, EnumErrorCode.EC_WrongFormatForInputField);		
	}

	@Test
	public void testCreateEx4() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case4:没有权限进行操作");
		//
		CategoryParent categoryParent4 = BaseCategoryParentTest.DataInput.getCategoryParent();
//		BaseCategoryParentTest.createOrUpdateCategoryParentViaActionWithExpectedError(categoryParent4, true, mvc, Shared.getStaffLoginSession(mvc, Shared.PhoneOfManager), Shared.DBName_Test, EnumErrorCode.EC_NoPermission);
	
	}

	// 运行Jenkins会跑不过,因为售前账号会被设为离职状态,在sit自动化测试CommodityRelatedTest里进行测试
	// @Test
	// public void testCreateEx5() throws Exception {
	// Shared.printTestMethodStartInfo();
	//
	// Shared.caseLog("case5:售前人员进行创建商品大类,没有权限进行操作");
	// CategoryParent categoryParent4 =
	// BaseCategoryParentTest.DataInput.getCategoryParent();
	//
	// MvcResult mr7 = mvc.perform(//
	// post(true)//
	// .session((MockHttpSession) Shared.getStaffLoginSession(mvc,
	// Shared.PhoneOfPreSale))//
	// .contentType(MediaType.APPLICATION_JSON)//
	// .param(CategoryParent.field.getFIELD_NAME_name(),
	// categoryParent4.getName())//
	// )//
	// .andExpect(status().isOk())//
	// .andDo(print())//
	// .andReturn();
	//
	// Shared.checkJSONErrorCode(mr7, EnumErrorCode.EC_NoPermission);
	// }

	@Test
	public void testDeleteEx1() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE1 删除没有关联小类的大类");
		CategoryParent cParent = BaseCategoryParentTest.DataInput.getCategoryParent();
		CategoryParent categoryParent = BaseCategoryParentTest.createOrUpdateCategoryParentViaActionWithNoError(cParent, true, mvc, sessionBoss, Shared.DBName_Test);
		//
		MvcResult mr = mvc.perform(//
				get("/categoryParent/deleteEx.bx?" + CategoryParent.field.getFIELD_NAME_ID() + "=" + categoryParent.getID())//
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		Shared.checkJSONErrorCode(mr);
		List<BaseModel> categoryList = new ArrayList<BaseModel>();
		// 检查点
		CategoryParentCP.verifyDelete(categoryParent, categoryList, categoryParentBO, categoryBO, posBO, categorySyncCacheBO, Shared.DBName_Test);
	}

	@Test
	public void testDeleteEx2() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case2 删除该大类中小类有依赖的，返回7");
		// 创建大类
		CategoryParent cParent = BaseCategoryParentTest.DataInput.getCategoryParent();
		CategoryParent categoryParent = BaseCategoryParentTest.createOrUpdateCategoryParentViaActionWithNoError(cParent, true, mvc, sessionBoss, Shared.DBName_Test);
		// 创建小类
		Category category = BaseCategoryParentTest.DataInput.getCategory();
		category.setParentID(categoryParent.getID());
		Category categoryCreate = BaseCategoryParentTest.createOrUpdateCategoryViaActionWithNoError(category, true, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		// 创建小类2
		Category category2 = BaseCategoryParentTest.DataInput.getCategory();
		category2.setParentID(categoryParent.getID());
		Category categoryCreate2 = BaseCategoryParentTest.createOrUpdateCategoryViaActionWithNoError(category2, true, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		// 创建商品
		Commodity comm = BaseCommodityTest.DataInput.getCommodity();
		comm.setCategoryID(categoryCreate.getID());
		Commodity commCreate = BaseCommodityTest.createCommodityViaAction(comm, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		MvcResult mr2 = mvc.perform(//
				get("/categoryParent/deleteEx.bx?" + CategoryParent.field.getFIELD_NAME_ID() + "=" + categoryParent.getID())//
						.session((MockHttpSession) sessionBoss)//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		Shared.checkJSONErrorCode(mr2, EnumErrorCode.EC_BusinessLogicNotDefined);
		// 删除测试创建的数据
		BaseCommodityTest.deleteCommodityViaAction(commCreate, Shared.DBName_Test, mvc, sessionBoss, mapBO);
		BaseCategoryParentTest.deleteCategoryParent(categoryParent.getID());
		CategorySyncCache csc = new CategorySyncCache();
		List<BaseModel> list = BaseCategoryParentTest.categorySyncCacheRetrieveN(csc);
		Assert.assertTrue(list != null && list.size() > 0);
		for (BaseModel bm : list) {
			CategorySyncCache categorySyncCache = (CategorySyncCache) bm;
			if (categorySyncCache.getSyncData_ID() == categoryCreate.getID() || categorySyncCache.getSyncData_ID() == categoryCreate2.getID()) {
				BaseCategoryParentTest.deleteCategorySyncCache(categorySyncCache);
			}
		}
	}

	@Test
	public void testDeleteEx3() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case3:没有权限进行操作");
		CategoryParent cParent = BaseCategoryParentTest.DataInput.getCategoryParent();
		CategoryParent categoryParent = BaseCategoryParentTest.createOrUpdateCategoryParentViaActionWithNoError(cParent, true, mvc, sessionBoss, Shared.DBName_Test);
//
//		MvcResult mr3 = mvc.perform(//
//				get("/categoryParent/deleteEx.bx?" + CategoryParent.field.getFIELD_NAME_ID() + "=" + categoryParent.getID())//
//						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfManager))//
//						.contentType(MediaType.APPLICATION_JSON)//
//		)//
//				.andExpect(status().isOk())//
//				.andDo(print())//
//				.andReturn();
//
//		Shared.checkJSONErrorCode(mr3, EnumErrorCode.EC_NoPermission);

		BaseCategoryParentTest.deleteCategoryParent(categoryParent.getID());
	}

	@Test
	public void testDeleteEx4() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE4 默认分类不能删除，返回错误码7");
		MvcResult mr4 = mvc.perform(//
				get("/categoryParent/deleteEx.bx?" + CategoryParent.field.getFIELD_NAME_ID() + "=1")//
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		Shared.checkJSONErrorCode(mr4, EnumErrorCode.EC_BusinessLogicNotDefined);
		JSONObject o4 = JSONObject.fromObject(mr4.getResponse().getContentAsString());
		String msg4 = JsonPath.read(o4, "$.msg");
		Assert.assertTrue(CategoryParent.ACTION_ERROR_UpdateDelete1.equals(msg4), "返回的错误消息不正确，期望的是:" + CategoryParent.ACTION_ERROR_UpdateDelete1);
	}

	@Test
	public void testDeleteEx5() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case5 删除该大类中小类有关联商品，删除商品后可以删除大类");
		// 创建大类
		CategoryParent cParent = BaseCategoryParentTest.DataInput.getCategoryParent();
		CategoryParent categoryParentCreate = BaseCategoryParentTest.createOrUpdateCategoryParentViaActionWithNoError(cParent, true, mvc, sessionBoss, Shared.DBName_Test);
		// 创建小类
		Category category = BaseCategoryParentTest.DataInput.getCategory();
		category.setParentID(categoryParentCreate.getID());
		Category categoryCreate = BaseCategoryParentTest.createOrUpdateCategoryViaActionWithNoError(category, true, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		// 创建小类2
		Category category2 = BaseCategoryParentTest.DataInput.getCategory();
		category2.setParentID(categoryParentCreate.getID());
		Category categoryCreate2 = BaseCategoryParentTest.createOrUpdateCategoryViaActionWithNoError(category2, true, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		List<BaseModel> categoryList = new ArrayList<BaseModel>();
		categoryList.add(categoryCreate);
		categoryList.add(categoryCreate2);
		// 创建商品
		Commodity comm = BaseCommodityTest.DataInput.getCommodity();
		comm.setCategoryID(categoryCreate.getID());
		Commodity commCreate = BaseCommodityTest.createCommodityViaAction(comm, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		// 删除商品
		BaseCommodityTest.deleteCommodityViaAction(commCreate, Shared.DBName_Test, mvc, sessionBoss, mapBO);
		// 删除大类
		MvcResult mr = mvc.perform(//
				get("/categoryParent/deleteEx.bx?" + CategoryParent.field.getFIELD_NAME_ID() + "=" + categoryParentCreate.getID())//
						.session((MockHttpSession) sessionBoss)//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		Shared.checkJSONErrorCode(mr);
		// 结果验证：1、大类的DB数据是否删除2、大类的缓存是否删除3、小类的DB数据是否删除4、小类的D型同步块是否创建5、小类的缓存是否删除6、小类的同步块缓存是否创建
		CategoryParentCP.verifyDelete(categoryParentCreate, categoryList, categoryParentBO, categoryBO, posBO, categorySyncCacheBO, Shared.DBName_Test);
		// 删除测试创建的数据，以免影响其他测试
		CategorySyncCache csc = new CategorySyncCache();
		List<BaseModel> list = BaseCategoryParentTest.categorySyncCacheRetrieveN(csc);
		Assert.assertTrue(list != null && list.size() > 0);
		for (BaseModel bm : list) {
			CategorySyncCache categorySyncCache = (CategorySyncCache) bm;
			if (categorySyncCache.getSyncData_ID() == categoryCreate.getID() || categorySyncCache.getSyncData_ID() == categoryCreate2.getID()) {
				BaseCategoryParentTest.deleteCategorySyncCache(categorySyncCache);
				// 删除缓存中的同步块信息
				SyncCacheManager.getCache(Shared.DBName_Test, EnumSyncCacheType.ESCT_CategorySyncInfo).delete1(categorySyncCache);
			}
		}
	}

	@Test
	public void testDeleteEx6() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case6 大类关联的小类没有依赖，可以删除");
		// 创建大类
		CategoryParent cParent = BaseCategoryParentTest.DataInput.getCategoryParent();
		CategoryParent categoryParentCreate = BaseCategoryParentTest.createOrUpdateCategoryParentViaActionWithNoError(cParent, true, mvc, sessionBoss, Shared.DBName_Test);
		// 创建小类
		Category category = BaseCategoryParentTest.DataInput.getCategory();
		category.setParentID(categoryParentCreate.getID());
		Category categoryCreate = BaseCategoryParentTest.createOrUpdateCategoryViaActionWithNoError(category, true, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		// 创建小类2
		Category category2 = BaseCategoryParentTest.DataInput.getCategory();
		category2.setParentID(categoryParentCreate.getID());
		Category categoryCreate2 = BaseCategoryParentTest.createOrUpdateCategoryViaActionWithNoError(category2, true, mvc, sessionBoss, mapBO, Shared.DBName_Test);
		List<BaseModel> categoryList = new ArrayList<BaseModel>();
		categoryList.add(categoryCreate);
		categoryList.add(categoryCreate2);
		// 删除大类
		MvcResult mr = mvc.perform(//
				get("/categoryParent/deleteEx.bx?" + CategoryParent.field.getFIELD_NAME_ID() + "=" + categoryParentCreate.getID())//
						.session((MockHttpSession) sessionBoss)//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		Shared.checkJSONErrorCode(mr);
		// 检查点
		CategoryParentCP.verifyDelete(categoryParentCreate, categoryList, categoryParentBO, categoryBO, posBO, categorySyncCacheBO, Shared.DBName_Test);
		// 删除测试创建的数据，以免影响其他测试
		CategorySyncCache csc = new CategorySyncCache();
		List<BaseModel> list = BaseCategoryParentTest.categorySyncCacheRetrieveN(csc);
		Assert.assertTrue(list != null && list.size() > 0);
		for (BaseModel bm : list) {
			CategorySyncCache categorySyncCache = (CategorySyncCache) bm;
			if (categorySyncCache.getSyncData_ID() == categoryCreate.getID() || categorySyncCache.getSyncData_ID() == categoryCreate2.getID()) {
				BaseCategoryParentTest.deleteCategorySyncCache(categorySyncCache);
				// 删除缓存中的同步块信息
				SyncCacheManager.getCache(Shared.DBName_Test, EnumSyncCacheType.ESCT_CategorySyncInfo).delete1(categorySyncCache);
			}
		}
	}

	@Test
	public void testDeleteEx7() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case7：删除的大类当中的小类没有关联商品，可删除，但只有一台有效的pos机，不会生成D型同步块,错误码为0");
		// 由于一些POS机存在零售单依赖，删除不了有依赖的POS机。很难实现仅有一台有效的POS机的测试环境，则实现空的测试方法.
	}

	@Test
	public void testRetrieve1Ex() throws Exception {
		Shared.printTestMethodStartInfo();
        
		// 创建小类
		CategoryParent cParent = BaseCategoryParentTest.DataInput.getCategoryParent();
		CategoryParent CcategoryParent = BaseCategoryParentTest.createOrUpdateCategoryParentViaActionWithNoError(cParent, true, mvc, sessionBoss, Shared.DBName_Test);
	    //
		MvcResult mr = mvc.perform(//
				get("/categoryParent/retrieve1Ex.bx?" + CategoryParent.field.getFIELD_NAME_ID() + "=" + CcategoryParent.getID())//
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		Shared.checkJSONErrorCode(mr);

//		System.out.println("-------------------------case2:没有权限进行操作--------------------------------");
//		MvcResult mr1 = mvc.perform(//
//				get("/categoryParent/retrieve1Ex.bx?" + CategoryParent.field.getFIELD_NAME_ID() + "=" + CcategoryParent.getID())//
//						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfManager))//
//						.contentType(MediaType.APPLICATION_JSON)//
//		)//
//				.andExpect(status().isOk())//
//				.andDo(print())//
//				.andReturn();
//
//		Shared.checkJSONErrorCode(mr1, EnumErrorCode.EC_NoPermission);

		BaseCategoryParentTest.deleteCategoryParent(CcategoryParent.getID());
	}

	@Test
	public void testRetrieveNEx() throws Exception {
		Shared.printTestMethodStartInfo();

		MvcResult mr = mvc.perform(//
				get("/categoryParent/retrieveNEx.bx")//
						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfBoss))//
						.contentType(MediaType.APPLICATION_JSON)//
		)//
				.andExpect(status().isOk())//
				.andDo(print())//
				.andReturn();

		Shared.checkJSONErrorCode(mr);

//		System.out.println("-------------------------case2:没有权限进行操作--------------------------------");
//		MvcResult mr2 = mvc.perform(//
//				get("/categoryParent/retrieveNEx.bx")//
//						.session((MockHttpSession) Shared.getStaffLoginSession(mvc, Shared.PhoneOfManager))//
//						.contentType(MediaType.APPLICATION_JSON)//
//		)//
//				.andExpect(status().isOk())//
//				.andDo(print())//
//				.andReturn();
//
//		Shared.checkJSONErrorCode(mr2, EnumErrorCode.EC_NoPermission);
	}
	
}
