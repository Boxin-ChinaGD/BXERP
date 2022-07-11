package com.bx.erp.test.commodity;

import static org.testng.Assert.assertTrue;

import java.util.List;
import java.util.Map;

import org.testng.Assert;
import org.springframework.test.context.web.WebAppConfiguration;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.commodity.Category;
import com.bx.erp.model.commodity.CategoryParent;
import com.bx.erp.model.commodity.Commodity;
import com.bx.erp.test.BaseCommodityTest;
import com.bx.erp.test.BaseMapperTest;
import com.bx.erp.test.Shared;
import com.bx.erp.util.DataSourceContextHolder;

@WebAppConfiguration
public class CategoryMapperTest extends BaseMapperTest {
	private static final int INVALID_ParentID = -999;

	/** 检查分类名字唯一字段是否已存在 */
	public static final int CASE_CHECK_UNIQUE_CATEGORYNAME = 1;

	@BeforeClass
	public void setup() {

		super.setUp();
	}

	@AfterClass
	public void tearDown() {

		super.tearDown();
	}

	@Test
	public void createTest1() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		// Case 1: 正常添加类别
		Shared.caseLog("Case1: 正常添加类别");
		Category category = BaseCategoryParentTest.DataInput.getCategory();
		Category createCategory = BaseCategoryParentTest.createCategoryViaMapper(category);
		// 删除测试数据
		BaseCategoryParentTest.deleteAndVerifyCategory(createCategory);
	}

	@Test
	public void createTest2() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		// 创建测试数据
		Category category = BaseCategoryParentTest.DataInput.getCategory();
		Category createCategory = BaseCategoryParentTest.createCategoryViaMapper(category);

		// Case 2: 添加重复的类别
		Shared.caseLog("Case2: 添加重复的类别");
		Category category2 = BaseCategoryParentTest.DataInput.getCategory();
		category2.setName(createCategory.getName());
		Map<String, Object> params = category2.getCreateParam(BaseBO.INVALID_CASE_ID, category2);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Category createCategoryCase2 = (Category) categoryMapper.create(params);
		// 重复创建sp会返回重复对象
		if (createCategory.compareTo(createCategoryCase2) != 0) {
			Assert.assertFalse(false, "两数据不一样");
		}
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_Duplicated, "意外：插入重复名字的商品类别失败");
		// 删除测试数据
		BaseCategoryParentTest.deleteAndVerifyCategory(createCategory);
	}

	@Test
	public void createTest3() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		// Case 3: 使用不存在的大类ID创建类别
		Shared.caseLog("Case3: 使用不存在的大类ID创建类别");
		Category category1 = BaseCategoryParentTest.DataInput.getCategory();
		category1.setParentID(INVALID_ParentID);
		Map<String, Object> params1 = category1.getCreateParam(BaseBO.INVALID_CASE_ID, category1);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Category createCategoryCase3 = (Category) categoryMapper.create(params1); // ...
		Assert.assertTrue(createCategoryCase3 == null && EnumErrorCode.values()[Integer.parseInt(params1.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined, "意外：插入不存在的大类，黑客行为");
	}

	@Test
	public void updateTest1() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		// 创建测试数据
		Category category = BaseCategoryParentTest.DataInput.getCategory();
		Category createCategory = BaseCategoryParentTest.createCategoryViaMapper(category);

		// 类别名称不重复，错误码应该是0（正常修改）
		Shared.caseLog("case1:类别名称不重复，错误码应该是0（正常修改）");
		Category category1 = BaseCategoryParentTest.DataInput.getCategory();
		category1.setID(createCategory.getID());
		category1.setParentID(1);
		//
		BaseCategoryParentTest.updateCategoryViaMapper(category1);
		// 删除测试数据
		BaseCategoryParentTest.deleteAndVerifyCategory(createCategory);
	}

	@Test
	public void updateTest2() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		// 创建测试数据
		Category category = BaseCategoryParentTest.DataInput.getCategory();
		Category createCategory = BaseCategoryParentTest.createCategoryViaMapper(category);
		//
		Category category2 = BaseCategoryParentTest.DataInput.getCategory();
		Category createCategory2 = BaseCategoryParentTest.createCategoryViaMapper(category2);

		// 类别名称重复，错误码应该是1
		Shared.caseLog("case2:类别名称重复，错误码应该是1");
		Category category3 = BaseCategoryParentTest.DataInput.getCategory();
		category3.setID(createCategory2.getID());
		category3.setName(createCategory.getName());
		category3.setParentID(createCategory2.getParentID());
		//
		Map<String, Object> paramsForUpdate2 = category3.getUpdateParam(BaseBO.INVALID_CASE_ID, category3);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Category updatecategory2 = (Category) categoryMapper.update(paramsForUpdate2);
		//
		Assert.assertNull(updatecategory2);
		Assert.assertEquals(EnumErrorCode.values()[Integer.parseInt(paramsForUpdate2.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())], EnumErrorCode.EC_Duplicated);

		// 删除测试数据
		BaseCategoryParentTest.deleteAndVerifyCategory(createCategory);
		BaseCategoryParentTest.deleteAndVerifyCategory(createCategory2);
	}

	@Test
	public void updateTest3() throws Exception {
		Shared.printTestMethodStartInfo();

		// 创建测试数据
		Category category = BaseCategoryParentTest.DataInput.getCategory();
		category.setParentID(1); // 修改前的ParentID为1
		Category createCategory = BaseCategoryParentTest.createCategoryViaMapper(category);
		//
		CategoryParent categoryParent = BaseCategoryParentTest.DataInput.getCategoryParent();// 创建大类
		CategoryParent createCategoryParent = BaseCategoryParentTest.createCategoryParentViaMapper(categoryParent);

		// 修改小类的大类，错误码应该是0
		Shared.caseLog("case3:修改小类的大类，错误码应该是0");
		Category category2 = BaseCategoryParentTest.DataInput.getCategory();
		category2.setID(createCategory.getID());
		category2.setName("测试a" + System.currentTimeMillis() % 100000);
		category2.setParentID(createCategoryParent.getID()); // 修改后ParentID
		//
		BaseCategoryParentTest.updateCategoryViaMapper(category2);
		// 删除测试数据
		BaseCategoryParentTest.deleteAndVerifyCategory(createCategory);
		BaseCategoryParentTest.deleteAndVerifyCategoryParent(createCategoryParent);
	}

	@Test
	public void updateTest4() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		// 创建测试数据
		Category category = BaseCategoryParentTest.DataInput.getCategory();
		Category createCategory = BaseCategoryParentTest.createCategoryViaMapper(category);

		// 修改成不存在的大类，错误码为7，该商品大类不存在
		Shared.caseLog("case4:修改成不存在的大类，错误码为7，该商品大类不存在");
		Category category2 = BaseCategoryParentTest.DataInput.getCategory();
		category2.setID(createCategory.getID());
		category2.setName("测试a" + System.currentTimeMillis() % 100000);
		category2.setParentID(INVALID_ParentID); // 修改后ParentID
		//
		Map<String, Object> paramsForUpdate4 = category2.getUpdateParam(BaseBO.INVALID_CASE_ID, category2);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Category updatecategory4 = (Category) categoryMapper.update(paramsForUpdate4);
		//
		Assert.assertNull(updatecategory4);
		Assert.assertEquals(EnumErrorCode.values()[Integer.parseInt(paramsForUpdate4.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())], EnumErrorCode.EC_BusinessLogicNotDefined);

		// 删除测试数据
		BaseCategoryParentTest.deleteAndVerifyCategory(createCategory);
	}

	@Test
	public void deleteTest1() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		// 创建测试数据
		Category category = BaseCategoryParentTest.DataInput.getCategory();
		Category createCategory = BaseCategoryParentTest.createCategoryViaMapper(category);

		// 商品表中没有这种类别的商品。可以直接删除，错误代码为0
		Shared.caseLog("case1:商品表中没有这种类别的商品。可以直接删除，错误代码为0");
		Category category2 = new Category();
		category2.setID(createCategory.getID());
		BaseCategoryParentTest.deleteAndVerifyCategory(category2);
	}

	@Test
	public void deleteTest2() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		// 创建测试数据
		Category category = BaseCategoryParentTest.DataInput.getCategory();
		Category createCategory = BaseCategoryParentTest.createCategoryViaMapper(category);
		//
		Commodity commTemplate = BaseCommodityTest.DataInput.getCommodity();
		commTemplate.setCategoryID(createCategory.getID());
		Commodity createCommodity = BaseCommodityTest.createCommodityViaMapper(commTemplate, BaseBO.CASE_Commodity_CreateSingle);

		// 商品表中已有商品的类别。删除不了，错误代码为7
		Shared.caseLog(" case2:商品表中已有商品的类别。删除不了，错误代码为7");
		Category c2 = new Category();
		c2.setID(createCategory.getID());
		//
		Map<String, Object> paramsForDelete2 = c2.getDeleteParam(BaseBO.INVALID_CASE_ID, c2);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		categoryMapper.delete(paramsForDelete2);
		//
		Assert.assertEquals(EnumErrorCode.values()[Integer.parseInt(paramsForDelete2.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())], EnumErrorCode.EC_BusinessLogicNotDefined);

		// 删除测试数据
		BaseCommodityTest.deleteCommodityViaMapper(createCommodity, EnumErrorCode.EC_NoError);
		BaseCategoryParentTest.deleteAndVerifyCategory(createCategory);
	}

	@Test
	public void retrieveNTest1() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		// 传入Name为空，查询全部。错误码为0
		Shared.caseLog("case1:传入Name为空，查询全部。错误码为0");
		Category c = new Category();
		Map<String, Object> params2 = c.getRetrieveNParam(BaseBO.INVALID_CASE_ID, c);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> list = categoryMapper.retrieveN(params2);
		//
		Assert.assertTrue(list.size() > 0);
		Assert.assertEquals(EnumErrorCode.values()[Integer.parseInt(params2.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())], EnumErrorCode.EC_NoError);
		// 字段验证
		for (BaseModel bm : list) {
			String error1 = ((Category) bm).checkCreate(BaseBO.INVALID_CASE_ID);
			Assert.assertEquals(error1, "");
		}
	}

	@Test
	public void retrieveNTest2() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		// 创建测试数据
		Category category = BaseCategoryParentTest.DataInput.getCategory();
		Category createCategory = BaseCategoryParentTest.createCategoryViaMapper(category);

		// 传入Nama有参数,错误码为0
		Shared.caseLog("case2:传入Nama有参数,错误码为0");
		Category c2 = new Category();
		c2.setName(createCategory.getName());
		//
		Map<String, Object> params3 = c2.getRetrieveNParam(BaseBO.INVALID_CASE_ID, c2);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> list = categoryMapper.retrieveN(params3);
		//
		Assert.assertTrue(list.size() > 0);
		Assert.assertEquals(EnumErrorCode.values()[Integer.parseInt(params3.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())], EnumErrorCode.EC_NoError);
		// 字段验证
		for (BaseModel bm : list) {
			String error1 = ((Category) bm).checkCreate(BaseBO.INVALID_CASE_ID);
			Assert.assertEquals(error1, "");
		}

		// 删除测试数据
		BaseCategoryParentTest.deleteAndVerifyCategory(createCategory);
	}

	@Test
	public void retrieve1Test() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		// 创建测试数据
		Category category = BaseCategoryParentTest.DataInput.getCategory();
		Category createCategory = BaseCategoryParentTest.createCategoryViaMapper(category);

		// 正常查询一条数据,错误码为0
		Shared.caseLog("case1:正常查询一条数据");
		Map<String, Object> paramsRetrieve1 = createCategory.getRetrieve1Param(BaseBO.INVALID_CASE_ID, createCategory);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Category categoryRetrieve1 = (Category) categoryMapper.retrieve1(paramsRetrieve1);
		//
		Assert.assertTrue(categoryRetrieve1 != null && EnumErrorCode.values()[Integer.parseInt(paramsRetrieve1.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "查询失败！");
		// 字段验证
		String error1 = categoryRetrieve1.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error1, "");

		// 删除测试数据
		BaseCategoryParentTest.deleteAndVerifyCategory(createCategory);
	}

	@Test
	public void retrieveNByParentTest() throws Exception {
		Shared.printTestMethodStartInfo();

		// 创建测试数据
		CategoryParent categoryParent = BaseCategoryParentTest.DataInput.getCategoryParent();// 创建大类
		CategoryParent createCategoryParent = BaseCategoryParentTest.createCategoryParentViaMapper(categoryParent);
		//
		Category category = BaseCategoryParentTest.DataInput.getCategory();
		category.setParentID(createCategoryParent.getID());
		Category createCategory = BaseCategoryParentTest.createCategoryViaMapper(category);

		Shared.caseLog("case1: 根据大类的id 查询出小类的id和名称");
		Category c = new Category();
		c.setID(createCategoryParent.getID());
		Map<String, Object> params = c.getRetrieveNParam(BaseBO.CASE_Category_RetrieveNByParent, c);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> list = categoryMapper.retrieveNByParent(params);
		//
		Assert.assertTrue(list.size() > 0);
		Assert.assertEquals(EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())], EnumErrorCode.EC_NoError);
		// 字段验证
		for (BaseModel bm : list) {
			String error1 = ((Category) bm).checkCreate(BaseBO.INVALID_CASE_ID);
			Assert.assertEquals(error1, "");
		}

		// 删除测试数据
		BaseCategoryParentTest.deleteAndVerifyCategory(createCategory);
		BaseCategoryParentTest.deleteAndVerifyCategoryParent(createCategoryParent);
	}

	@Test
	public void checkUniqueFieldTest1() throws Exception {
		Shared.caseLog("Case1:查询一个不存在的商品小类");
		Category category = new Category();
		category.setFieldToCheckUnique(CASE_CHECK_UNIQUE_CATEGORYNAME);
		category.setUniqueField("阿萨德佛该会否");

		Map<String, Object> params1 = category.getRetrieveNParam(BaseBO.CASE_CheckUniqueField, category);

		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		categoryMapper.checkUniqueField(params1);
		assertTrue(EnumErrorCode.values()[Integer.parseInt(params1.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError);
	}

	@Test
	public void checkUniqueFieldTest2() throws Exception {
		Shared.caseLog("Case2:查询一个已经存在的商品小类");
		Category category = new Category();
		category.setFieldToCheckUnique(CASE_CHECK_UNIQUE_CATEGORYNAME);
		category.setUniqueField("饮料");

		Map<String, Object> params2 = category.getRetrieveNParam(BaseBO.CASE_CheckUniqueField, category);

		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		categoryMapper.checkUniqueField(params2);
		assertTrue(EnumErrorCode.values()[Integer.parseInt(params2.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_Duplicated);

	}

	@Test
	public void checkUniqueFieldTest3() throws Exception {
		Shared.caseLog("Case3:查询一个已经存在的商品小类,但传入的ID与已存在的商品小类的商品小类ID相同 ");
		Category category = new Category();
		category.setID(2);
		category.setFieldToCheckUnique(CASE_CHECK_UNIQUE_CATEGORYNAME);
		category.setUniqueField("饮料");

		Map<String, Object> params3 = category.getRetrieveNParam(BaseBO.CASE_CheckUniqueField, category);

		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		categoryMapper.checkUniqueField(params3);
		assertTrue(EnumErrorCode.values()[Integer.parseInt(params3.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError);
	}

	@Test
	public void checkUniqueFieldTest4() throws Exception {
		Shared.caseLog("Case4:查询一个已经存在的商品小类,但传入的ID与已存在的商品小类的ID不相同");
		Category category = new Category();
		category.setID(3);
		category.setFieldToCheckUnique(CASE_CHECK_UNIQUE_CATEGORYNAME);
		category.setUniqueField("饮料");

		Map<String, Object> params3 = category.getRetrieveNParam(BaseBO.CASE_CheckUniqueField, category);

		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		categoryMapper.checkUniqueField(params3);
		assertTrue(EnumErrorCode.values()[Integer.parseInt(params3.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_Duplicated);
	}
}
