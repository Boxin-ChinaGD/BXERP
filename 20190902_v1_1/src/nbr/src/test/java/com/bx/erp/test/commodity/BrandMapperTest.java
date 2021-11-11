package com.bx.erp.test.commodity;

import java.util.List;
import java.util.Map;

import org.springframework.test.annotation.Rollback;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.commodity.Brand;
import com.bx.erp.model.commodity.Commodity;
import com.bx.erp.test.BaseBrandTest;
import com.bx.erp.test.BaseCommodityTest;
import com.bx.erp.test.BaseMapperTest;
import com.bx.erp.test.Shared;
import com.bx.erp.util.DataSourceContextHolder;

public class BrandMapperTest extends BaseMapperTest {

	@BeforeClass
	public void setup() {

		super.setUp();
	}

	@AfterClass
	public void tearDown() {

		super.tearDown();
	}

	@Rollback(false)
	@Test
	public void createTest1() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		// Case1: 添加不重复的品牌，错误码为0
		Shared.caseLog("Case1: 添加不重复的品牌，错误码为0（正常添加）");
		Brand brand = BaseBrandTest.DataInput.getBrand();
		Brand brandCreate = BaseBrandTest.createBrandViaMapper(brand);
		// 删除添加后的数据
		BaseBrandTest.deleteAndVerifyBrand(brandCreate);

	}

	@Test
	public void createTest2() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		// 创建一条品牌数据
		Brand brand = BaseBrandTest.DataInput.getBrand();
		Brand brandCreate = BaseBrandTest.createBrandViaMapper(brand);

		// Case2: 添加重复品牌，不能添加，错误码为1
		Shared.caseLog("Case2: 添加重复品牌，不能添加，错误码为1");
		Brand b2 = BaseBrandTest.DataInput.getBrand();
		b2.setName(brandCreate.getName());
		// sp会返回重复创建的那条数据
		Map<String, Object> paramsForCreate2 = b2.getCreateParam(BaseBO.INVALID_CASE_ID, b2);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Brand brandCreateRepeatTest = (Brand) brandMapper.create(paramsForCreate2);
		//
		System.out.println("创建品牌对象所需要的对象：" + brandCreate);
		System.out.println("创建出的品牌对象是：" + brandCreateRepeatTest);
		//
		if (brandCreate.compareTo(brandCreateRepeatTest) != 0) {
			Assert.assertFalse(false, "两数据不一样");
		}
		Assert.assertEquals(EnumErrorCode.values()[Integer.parseInt(paramsForCreate2.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())], EnumErrorCode.EC_Duplicated);
		// 删除添加后的数据
		BaseBrandTest.deleteAndVerifyBrand(brandCreate);
	}

	@Test
	public void updateTest1() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		// 创建一条品牌数据
		Brand brand = BaseBrandTest.DataInput.getBrand();
		Brand brandCreate = BaseBrandTest.createBrandViaMapper(brand);

		// case1:品牌名称不重复，错误码应该是0
		Shared.caseLog("case1:品牌名称不重复，错误码应该是0");
		Brand b1 = BaseBrandTest.DataInput.getBrand();
		b1.setID(brandCreate.getID());
		BaseBrandTest.updateBrandViaMapper(b1);
		// 删除创建的数据
		BaseBrandTest.deleteAndVerifyBrand(b1);
		BaseBrandTest.deleteAndVerifyBrand(brandCreate);
	}

	@Test
	public void updateTest2() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		// 创建品牌测试数据
		Brand brand = BaseBrandTest.DataInput.getBrand();
		Brand brandCreate = BaseBrandTest.createBrandViaMapper(brand);
		//
		Brand brand2 = BaseBrandTest.DataInput.getBrand();
		Brand brandCreate2 = BaseBrandTest.createBrandViaMapper(brand2);

		// case2:更新品牌名称重复，错误码应该是1
		Shared.caseLog("case2:更新品牌名称重复，错误码应该是1");
		Brand brand3 = new Brand();
		brand3.setID(brandCreate2.getID());
		brand3.setName(brandCreate.getName());
		//
		Map<String, Object> paramsForUpdate = brand3.getUpdateParam(BaseBO.INVALID_CASE_ID, brand3);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Brand brandUpdate2 = (Brand) brandMapper.update(paramsForUpdate);
		//
		brand3.setIgnoreIDInComparision(true);
		if (brand3.compareTo(brandUpdate2) == 0) {
			Assert.assertTrue(false, "修改的对象字段与DB读出的相等");
		}
		Assert.assertNull(brandUpdate2);
		Assert.assertEquals(EnumErrorCode.values()[Integer.parseInt(paramsForUpdate.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())], EnumErrorCode.EC_Duplicated);

		// 删除创建的数据
		BaseBrandTest.deleteAndVerifyBrand(brandCreate);
		BaseBrandTest.deleteAndVerifyBrand(brandCreate2);
	}

	@Test
	public void deleteTest1() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		// 创建品牌测试数据
		Brand brand = BaseBrandTest.DataInput.getBrand();
		Brand brandCreate = BaseBrandTest.createBrandViaMapper(brand);

		// case1:品牌表中没有这种品牌的商品。可以直接删除，错误代码为0
		Shared.caseLog("case1:品牌表中没有这种品牌的商品。可以直接删除，错误代码为0");
		Brand brand2 = BaseBrandTest.DataInput.getBrand();
		brand2.setID(brandCreate.getID());
		BaseBrandTest.deleteAndVerifyBrand(brand2);
	}

	@Test
	public void deleteTest2() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		// 创建品牌表中已有商品的品牌的测试数据
		Brand brand = BaseBrandTest.DataInput.getBrand();
		Brand brandCreate = BaseBrandTest.createBrandViaMapper(brand);
		//
		Commodity commTemplate = BaseCommodityTest.DataInput.getCommodity();
		commTemplate.setBrandID(brandCreate.getID());
		commTemplate.setOperatorStaffID(STAFF_ID3);
		Commodity createCommodity = BaseCommodityTest.createCommodityViaMapper(commTemplate, BaseBO.CASE_Commodity_CreateSingle);

		// case2:品牌表中已有商品的品牌。删除不了，错误代码为7
		Shared.caseLog("case2:品牌表中已有商品的品牌。删除不了，错误代码为7");
		Map<String, Object> paramsForDelete2 = brandCreate.getDeleteParam(BaseBO.INVALID_CASE_ID, brandCreate);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		brandMapper.delete(paramsForDelete2);
		Assert.assertEquals(EnumErrorCode.values()[Integer.parseInt(paramsForDelete2.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())], EnumErrorCode.EC_BusinessLogicNotDefined);

		// 删除创建的数据
		BaseCommodityTest.deleteCommodityViaMapper(createCommodity, EnumErrorCode.EC_NoError);
		BaseBrandTest.deleteAndVerifyBrand(brandCreate);
	}

	@Test
	public void retrieve1Test() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		// 创建品牌测试数据
		Brand brand = BaseBrandTest.DataInput.getBrand();
		Brand brandCreate = BaseBrandTest.createBrandViaMapper(brand);

		// case1:正常查询一条数据
		Shared.caseLog("case1:正常查询一条数据");
		Map<String, Object> paramsRetrieve = brandCreate.getRetrieve1Param(BaseBO.INVALID_CASE_ID, brandCreate);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Brand brandRetrieve1 = (Brand) brandMapper.retrieve1(paramsRetrieve);// ...
		//
		brandCreate.setIgnoreIDInComparision(true);
		if (brandCreate.compareTo(brandRetrieve1) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		Assert.assertNotNull(brandRetrieve1);
		Assert.assertEquals(EnumErrorCode.values()[Integer.parseInt(paramsRetrieve.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())], EnumErrorCode.EC_NoError);
		// 字段验证
		String error1 = brandRetrieve1.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error1, "");

		// 删除添加后的数据
		BaseBrandTest.deleteAndVerifyBrand(brandCreate);
	}

	@Test
	public void retrieveNTest1() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		// 创建品牌测试数据
		Brand brandTemplate = BaseBrandTest.DataInput.getBrand();
		Brand brandCreate = BaseBrandTest.createBrandViaMapper(brandTemplate);

		// case1:有参数
		Shared.caseLog("case1:有参数查询");
		Brand brand = new Brand();
		brand.setName(brandCreate.getName());
		//
		Map<String, Object> params = brand.getRetrieveNParam(BaseBO.INVALID_CASE_ID, brand);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> retrieveNList = brandMapper.retrieveN(params);
		//
		Assert.assertEquals(EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())], EnumErrorCode.EC_NoError);
		System.out.println("【查询品牌】测试成功！");
		// 字段验证
		for (BaseModel bm : retrieveNList) {
			String error1 = ((Brand) bm).checkCreate(BaseBO.INVALID_CASE_ID);
			Assert.assertEquals(error1, "");
		}

		// 删除添加后的数据
		BaseBrandTest.deleteAndVerifyBrand(brandCreate);
	}

	@Test
	public void retrieveNTest2() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		// case2:无参数情况
		Shared.caseLog("case2:无参数查询");
		Brand brand = new Brand();
		//
		Map<String, Object> params2 = brand.getRetrieveNParam(BaseBO.INVALID_CASE_ID, brand);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> retrieveNList1 = brandMapper.retrieveN(params2);
		//
		Assert.assertEquals(EnumErrorCode.values()[Integer.parseInt(params2.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())], EnumErrorCode.EC_NoError);
		System.out.println("【查询品牌】测试成功！");
		// 字段验证
		for (BaseModel bm1 : retrieveNList1) {
			String error2 = ((Brand) bm1).checkCreate(BaseBO.INVALID_CASE_ID);
			Assert.assertEquals(error2, "");
		}
	}
}
