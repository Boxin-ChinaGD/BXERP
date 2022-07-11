package com.bx.erp.test.commodity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.test.context.web.WebAppConfiguration;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.cache.SyncCache;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.commodity.Category;
import com.bx.erp.model.commodity.CategoryParent;
import com.bx.erp.model.commodity.CategorySyncCache;
import com.bx.erp.model.commodity.Commodity;
import com.bx.erp.test.BaseCommodityTest;
import com.bx.erp.test.BaseMapperTest;
import com.bx.erp.test.Shared;
import com.bx.erp.util.DataSourceContextHolder;

@WebAppConfiguration
public class CategoryParentMapperTest extends BaseMapperTest {

	@BeforeClass
	public void setup() {
		super.setUp();
	}

	@AfterClass
	public void tearDown() {

		super.tearDown();
	}

	@Test
	public void createTest1() throws Exception {
		Shared.printTestMethodStartInfo();

		// 正常添加。错误码为0
		Shared.caseLog("Case1:正常添加。错误码为0");
		CategoryParent categoryParent = BaseCategoryParentTest.DataInput.getCategoryParent();
		CategoryParent categoryParentCreate = BaseCategoryParentTest.createCategoryParentViaMapper(categoryParent);
		// 删除并验证测试数据
		BaseCategoryParentTest.deleteAndVerifyCategoryParent(categoryParentCreate);
	}

	@Test
	public void createTest2() throws Exception {
		Shared.printTestMethodStartInfo();

		// 创建测试数据
		CategoryParent categoryParent = BaseCategoryParentTest.DataInput.getCategoryParent();
		CategoryParent categoryParentCreate = BaseCategoryParentTest.createCategoryParentViaMapper(categoryParent);

		// 重复的名字添加。错误码为1
		Shared.caseLog("Case2:重复名字添加。错误码为1");
		CategoryParent categoryParent2 = BaseCategoryParentTest.DataInput.getCategoryParent();
		categoryParent2.setName(categoryParentCreate.getName());
		//
		Map<String, Object> createParam = categoryParent2.getCreateParam(BaseBO.INVALID_CASE_ID, categoryParent2);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		CategoryParent categoryParentCreateDuplicat = (CategoryParent) categoryParentMapper.create(createParam);
		//
		Assert.assertTrue(categoryParentCreateDuplicat == null && EnumErrorCode.values()[Integer.parseInt(createParam.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_Duplicated, "创建对象成功");

		// 删除并验证测试数据
		BaseCategoryParentTest.deleteAndVerifyCategoryParent(categoryParentCreate);
	}

	@Test
	public void updateTest1() throws Exception {
		Shared.printTestMethodStartInfo();

		// 创建测试数据
		CategoryParent categoryParent = BaseCategoryParentTest.DataInput.getCategoryParent();
		CategoryParent categoryParentCreate = BaseCategoryParentTest.createCategoryParentViaMapper(categoryParent);
		// case1:已有重复的名字，不可进行修改,错误码应该为1
		Shared.caseLog("case1:已有重复的名字，不可进行修改,错误码应该为1");
		CategoryParent c = new CategoryParent();
		c.setName(categoryParentCreate.getName());
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Map<String, Object> updateParam = c.getUpdateParam(BaseBO.INVALID_CASE_ID, c);
		categoryParentMapper.update(updateParam);
		//
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(updateParam.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_Duplicated, "更新对象成功");
		// 删除并验证测试数据
		BaseCategoryParentTest.deleteAndVerifyCategoryParent(categoryParentCreate);
	}

	@Test
	public void updateTest2() throws Exception {
		Shared.printTestMethodStartInfo();

		// 创建测试数据
		CategoryParent categoryParent = BaseCategoryParentTest.DataInput.getCategoryParent();
		CategoryParent categoryParentCreate = BaseCategoryParentTest.createCategoryParentViaMapper(categoryParent);
		// case2:还没重复的名字，可进行修改，错误应该为0
		Shared.caseLog("case2:还没重复的名字，可进行修改，错误应该为0");
		CategoryParent categoryParent1 = BaseCategoryParentTest.DataInput.getCategoryParent();
		categoryParent1.setID(categoryParentCreate.getID());
		//
		BaseCategoryParentTest.updateCategoryParentViaMapper(categoryParent1);
		// 删除并验证测试数据
		BaseCategoryParentTest.deleteAndVerifyCategoryParent(categoryParentCreate);
	}

	@Test
	public void deleteTest1() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case1: 删除的大类当中的小类有关联商品，不可删除，错误码为7");
		// 创建大类
		CategoryParent categoryParent = BaseCategoryParentTest.DataInput.getCategoryParent();
		CategoryParent categoryParentCreate = BaseCategoryParentTest.createCategoryParentViaMapper(categoryParent);
		// 创建小类1
		Category category = BaseCategoryParentTest.DataInput.getCategory();
		category.setParentID(categoryParentCreate.getID());
		Category createCategory = BaseCategoryParentTest.createCategoryViaMapper(category);
		// 创建小类2
		Category category2 = BaseCategoryParentTest.DataInput.getCategory();
		category2.setParentID(categoryParentCreate.getID());
		Category createCategory2 = BaseCategoryParentTest.createCategoryViaMapper(category2);
		// 创建商品
		Commodity commTemplate = BaseCommodityTest.DataInput.getCommodity();
		commTemplate.setCategoryID(createCategory.getID());
		Commodity createCommodity = BaseCommodityTest.createCommodityViaMapper(commTemplate, BaseBO.CASE_Commodity_CreateSingle);
		// 删除大类
		Map<String, Object> deleteParam = categoryParentCreate.getDeleteParam(BaseBO.INVALID_CASE_ID, categoryParentCreate);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		categoryParentMapper.delete(deleteParam);
		//
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(deleteParam.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined, "测试失败，期望的是小类有商品依赖删除失败！");
		// 验证没有生成小类的D型同步块
		CategorySyncCache csc = new CategorySyncCache();
		List<BaseModel> list = BaseCategoryParentTest.categorySyncCacheRetrieveN(csc);
		Assert.assertTrue(list.size() == 0);
		// 删除并验证测试数据
		BaseCommodityTest.deleteCommodityViaMapper(createCommodity, EnumErrorCode.EC_NoError);
		BaseCategoryParentTest.deleteAndVerifyCategory(createCategory);
		BaseCategoryParentTest.deleteAndVerifyCategory(createCategory2);
		BaseCategoryParentTest.deleteAndVerifyCategoryParent(categoryParentCreate);
	}

	@Test
	public void deleteTest2() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case2：删除的大类当中的小类没有关联商品，可删除，错误码为0");
		// 创建大类
		CategoryParent categoryParent = BaseCategoryParentTest.DataInput.getCategoryParent();
		CategoryParent categoryParentCreate = BaseCategoryParentTest.createCategoryParentViaMapper(categoryParent);
		// 创建小类
		Category category = BaseCategoryParentTest.DataInput.getCategory();
		category.setParentID(categoryParentCreate.getID());
		Category createCategory = BaseCategoryParentTest.createCategoryViaMapper(category);
		// 创建小类2
		Category category2 = BaseCategoryParentTest.DataInput.getCategory();
		category2.setParentID(categoryParentCreate.getID());
		Category createCategory2 = BaseCategoryParentTest.createCategoryViaMapper(category2);
		// 删除大类
		BaseCategoryParentTest.deleteAndVerifyCategoryParent(categoryParentCreate);
		// 验证是否成功删除
		BaseCategoryParentTest.verificationDelete_Category(createCategory);
		BaseCategoryParentTest.verificationDelete_Category(createCategory2);
		// 验证生成了小类的D型同步块
		CategorySyncCache csc = new CategorySyncCache();
		List<BaseModel> list = BaseCategoryParentTest.categorySyncCacheRetrieveN(csc);
		Assert.assertTrue(list != null && list.size() > 0);
		List<CategorySyncCache> listOfDelete = new ArrayList<CategorySyncCache>(); // 用于删除测试创建的数据
		boolean existC = false;
		boolean existC2 = false;
		for (BaseModel bm : list) {
			CategorySyncCache categorySyncCache = (CategorySyncCache) bm;
			if (categorySyncCache.getSyncData_ID() == createCategory.getID()) {
				existC = true;
				Assert.assertTrue(SyncCache.SYNC_Type_D.equals(categorySyncCache.getSyncType()), "测试失败！期望的是返回的小类同步块只有D型同步块");
				listOfDelete.add(categorySyncCache);
				continue;
			}
			if (categorySyncCache.getSyncData_ID() == createCategory2.getID()) {
				existC2 = true;
				Assert.assertTrue(SyncCache.SYNC_Type_D.equals(categorySyncCache.getSyncType()), "测试失败！期望的是返回的小类同步块只有D型同步块");
				listOfDelete.add(categorySyncCache);
			}
		}
		Assert.assertTrue(existC, "c小类被删除后没有在DB中生成同步块数据");
		Assert.assertTrue(existC2, "c2小类被删除后没有在DB中生成同步块数据");
		// 删除测试创建的数据，以免影响其他测试
		BaseCategoryParentTest.deleteCategorySyncCache(listOfDelete.get(0));
		BaseCategoryParentTest.deleteCategorySyncCache(listOfDelete.get(1));
	}

	@Test
	public void deleteTest3() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case3: 删除的大类当中的小类有关联商品，删除关联商品后可删除，错误码为0");
		// 创建大类
		CategoryParent categoryParent = BaseCategoryParentTest.DataInput.getCategoryParent();
		CategoryParent categoryParentCreate = BaseCategoryParentTest.createCategoryParentViaMapper(categoryParent);
		// 创建小类1
		Category category = BaseCategoryParentTest.DataInput.getCategory();
		category.setParentID(categoryParentCreate.getID());
		Category createCategory = BaseCategoryParentTest.createCategoryViaMapper(category);
		// 创建小类2
		Category category2 = BaseCategoryParentTest.DataInput.getCategory();
		category2.setParentID(categoryParentCreate.getID());
		Category createCategory2 = BaseCategoryParentTest.createCategoryViaMapper(category2);
		// 创建商品
		Commodity commTemplate = BaseCommodityTest.DataInput.getCommodity();
		commTemplate.setCategoryID(createCategory.getID());
		Commodity createCommodity = BaseCommodityTest.createCommodityViaMapper(commTemplate, BaseBO.CASE_Commodity_CreateSingle);
		// 删除创建的商品
		BaseCommodityTest.deleteCommodityViaMapper(createCommodity, EnumErrorCode.EC_NoError);
		// 删除大类
		BaseCategoryParentTest.deleteAndVerifyCategoryParent(categoryParentCreate);
		// 验证是否成功删除
		BaseCategoryParentTest.verificationDelete_Category(createCategory);
		BaseCategoryParentTest.verificationDelete_Category(createCategory2);
		// 验证生成了小类的D型同步块
		CategorySyncCache csc = new CategorySyncCache();
		List<BaseModel> list = BaseCategoryParentTest.categorySyncCacheRetrieveN(csc);
		Assert.assertTrue(list != null && list.size() > 0);
		List<CategorySyncCache> listOfDelete = new ArrayList<CategorySyncCache>(); // 用于删除测试创建的数据
		boolean existC = false;
		boolean existC2 = false;
		for (BaseModel bm : list) {
			CategorySyncCache categorySyncCache = (CategorySyncCache) bm;
			if (categorySyncCache.getSyncData_ID() == createCategory.getID()) {
				existC = true;
				Assert.assertTrue(SyncCache.SYNC_Type_D.equals(categorySyncCache.getSyncType()), "测试失败！期望的是返回的小类同步块只有D型同步块");
				listOfDelete.add(categorySyncCache);
				continue;
			}
			if (categorySyncCache.getSyncData_ID() == createCategory2.getID()) {
				existC2 = true;
				Assert.assertTrue(SyncCache.SYNC_Type_D.equals(categorySyncCache.getSyncType()), "测试失败！期望的是返回的小类同步块只有D型同步块");
				listOfDelete.add(categorySyncCache);
			}
		}
		Assert.assertTrue(existC, "c小类被删除后没有在DB中生成同步块数据");
		Assert.assertTrue(existC2, "c2小类被删除后没有在DB中生成同步块数据");
		// 删除测试生成的数据，以免影响其他测试
		BaseCategoryParentTest.deleteCategorySyncCache(listOfDelete.get(0));
		BaseCategoryParentTest.deleteCategorySyncCache(listOfDelete.get(1));
	}

	@Test
	public void deleteTest4() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case4：删除没有关联小类的大类，错误码为0");

		// 创建大类
		CategoryParent categoryParent = BaseCategoryParentTest.DataInput.getCategoryParent();
		CategoryParent categoryParentCreate = BaseCategoryParentTest.createCategoryParentViaMapper(categoryParent);
		// 删除大类
		BaseCategoryParentTest.deleteAndVerifyCategoryParent(categoryParentCreate);
	}

	@Test
	public void deleteTest5() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case5：删除的大类当中的小类没有关联商品，可删除，但只有一台有效的pos机，不会生成D型同步块,错误码为0");
		// 由于一些POS机存在零售单依赖，删除不了有依赖的POS机。很难实现仅有一台有效的POS机的测试环境，则实现空的测试方法.
	}

	@Test
	public void retrieve1Test() throws Exception {
		Shared.printTestMethodStartInfo();

		// 创建测试数据
		CategoryParent categoryParent = BaseCategoryParentTest.DataInput.getCategoryParent();
		CategoryParent categoryParentCreate = BaseCategoryParentTest.createCategoryParentViaMapper(categoryParent);

		// 根据ID正常查询一条数据
		Shared.caseLog("Case1: 根据ID正常查询一条数据");
		Map<String, Object> retrieve1Param = categoryParentCreate.getRetrieve1Param(BaseBO.INVALID_CASE_ID, categoryParentCreate);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		CategoryParent retrieve1C = (CategoryParent) categoryParentMapper.retrieve1(retrieve1Param);
		//
		Assert.assertTrue(retrieve1C != null && EnumErrorCode.values()[Integer.parseInt(retrieve1Param.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "查找对象成功");
		// 字段验证
		String error1 = retrieve1C.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error1, "");

		// 删除并验证测试数据
		BaseCategoryParentTest.deleteAndVerifyCategoryParent(categoryParentCreate);
	}

	@Test
	public void retrieveNTest1() throws Exception {
		Shared.printTestMethodStartInfo();

		// 查找所有的大类
		Shared.caseLog("Case1: 查找所有的大类");
		CategoryParent c = new CategoryParent();
		//
		Map<String, Object> retrieveNParam = c.getRetrieveNParam(BaseBO.INVALID_CASE_ID, c);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> list = (List<BaseModel>) categoryParentMapper.retrieveN(retrieveNParam);
		//
		Assert.assertTrue(list.size() > 0 && EnumErrorCode.values()[Integer.parseInt(retrieveNParam.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "查找对象成功");
		// 字段验证
		for (BaseModel bm : list) {
			String error1 = ((CategoryParent) bm).checkCreate(BaseBO.INVALID_CASE_ID);
			Assert.assertEquals(error1, "");
		}
	}

	@Test
	public void retrieveNTest2() throws Exception {
		Shared.printTestMethodStartInfo();

		// 创建测试数据
		CategoryParent categoryParent = BaseCategoryParentTest.DataInput.getCategoryParent();
		CategoryParent categoryParentCreate = BaseCategoryParentTest.createCategoryParentViaMapper(categoryParent);

		// 根据name去查找相应的大类
		CategoryParent c = new CategoryParent();
		c.setName(categoryParentCreate.getName());
		Map<String, Object> retrieveNParam2 = c.getRetrieveNParam(BaseBO.INVALID_CASE_ID, c);

		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> list = (List<BaseModel>) categoryParentMapper.retrieveN(retrieveNParam2);
		//
		Assert.assertTrue(list.size() > 0 && EnumErrorCode.values()[Integer.parseInt(retrieveNParam2.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "查找对象成功");
		// 字段验证
		for (BaseModel bm : list) {
			String error1 = ((CategoryParent) bm).checkCreate(BaseBO.INVALID_CASE_ID);
			Assert.assertEquals(error1, "");
		}
	}
}
