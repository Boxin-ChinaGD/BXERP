package com.bx.erp.test;

import static org.testng.Assert.assertTrue;

import java.util.List;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.VipCategory;
import com.bx.erp.util.DataSourceContextHolder;

public class VipCategoryMapperTest extends BaseMapperTest {

	public static class DataInput {
		private static VipCategory vipCategoryInput = null;

		protected static final VipCategory getData() throws CloneNotSupportedException, InterruptedException {
			vipCategoryInput = new VipCategory();
			Thread.sleep(1000);
			vipCategoryInput.setName("黄金会员1" + String.valueOf(System.currentTimeMillis()).substring(6));

			return (VipCategory) vipCategoryInput.clone();
		}
	}

	@BeforeClass
	public void setup() {
		super.setUp();
	}

	@AfterClass
	public void tearDown() {
		super.tearDown();
	}

	@Test
	public void retrieveNTest() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		System.out.println("\n------------------------ Retrieve VipCategory Test ----------------------");

		VipCategory vc = new VipCategory();
		Map<String, Object> params = vc.getRetrieveNParam(BaseBO.INVALID_CASE_ID, vc);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> list = vipCategoryMapper.retrieveN(params);
		Assert.assertTrue(list.size() > 0 && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError);
	}

	@Test
	public void retrieve1Test() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();
		VipCategory vc = DataInput.getData();
		Map<String, Object> paramsForCreate = vc.getCreateParam(BaseBO.INVALID_CASE_ID, vc);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		VipCategory vipCategiryCreate = (VipCategory) vipCategoryMapper.create(paramsForCreate);
		vc.setIgnoreIDInComparision(true);
		if (vc.compareTo(vipCategiryCreate) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		Assert.assertNotNull(vipCategiryCreate);
		Assert.assertEquals(EnumErrorCode.values()[Integer.parseInt(paramsForCreate.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())], EnumErrorCode.EC_NoError);

		System.out.println("\n------------------------ Retrieve1 VipCategory Test ----------------------");

		System.out.println("\n------------------------ CASE1:用ID查询 ----------------------");
		VipCategory vc1 = new VipCategory();
		vc1.setID(vipCategiryCreate.getID());
		Map<String, Object> params1 = vc1.getRetrieve1Param(BaseBO.INVALID_CASE_ID, vc1);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		VipCategory vipCategory = (VipCategory) vipCategoryMapper.retrieve1(params1);
		Assert.assertNotNull(vipCategory, "查询失败");
		Assert.assertEquals(EnumErrorCode.values()[Integer.parseInt(params1.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())], EnumErrorCode.EC_NoError);

		System.out.println("\n------------------------ CASE2:用不存在的ID查询 ----------------------");
		vc1.setID(-22);
		Map<String, Object> params2 = vc1.getRetrieve1Param(BaseBO.INVALID_CASE_ID, vc1);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		VipCategory vipCategory2 = (VipCategory) vipCategoryMapper.retrieve1(params2);
		assertTrue(vipCategory2 == null && EnumErrorCode.values()[Integer.parseInt(params2.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError);
	}

	@Test
	public void createTest() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		System.out.println("\n------------------------ Create VipCategory Test ----------------------");
		// Case1: 正常创建
		System.out.println("\n------------------------ Case1: 正常创建 ----------------------");
		VipCategory vc = DataInput.getData();
		Map<String, Object> paramsForCreate = vc.getCreateParam(BaseBO.INVALID_CASE_ID, vc);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		VipCategory vipCategiryCreate = (VipCategory) vipCategoryMapper.create(paramsForCreate);
		vc.setIgnoreIDInComparision(true);
		if (vc.compareTo(vipCategiryCreate) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		Assert.assertNotNull(vipCategiryCreate);
		Assert.assertEquals(EnumErrorCode.values()[Integer.parseInt(paramsForCreate.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())], EnumErrorCode.EC_NoError);

		// case2：会员类别名称重复
		System.out.println("\n------------------------ Case2：会员类别名称重复----------------------");
		vc.setName("黄金会员");
		Map<String, Object> paramsForCreate1 = vc.getCreateParam(BaseBO.INVALID_CASE_ID, vc);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		vipCategoryMapper.create(paramsForCreate1);

		Assert.assertEquals(EnumErrorCode.values()[Integer.parseInt(paramsForCreate1.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())], EnumErrorCode.EC_Duplicated);
	}

	@Test
	public void updateTest() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		System.out.println("\n------------------------ Update VipCategory Test ----------------------");

		VipCategory vc = DataInput.getData();
		Map<String, Object> paramsForCreate = vc.getCreateParam(BaseBO.INVALID_CASE_ID, vc);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		VipCategory vipCategiryCreate = (VipCategory) vipCategoryMapper.create(paramsForCreate);
		//
		System.out.println("创建会员类别对象所需要的对象：" + vc);
		System.out.println("创建出的会员类别对象是：" + vipCategiryCreate);
		//
		vc.setIgnoreIDInComparision(true);
		if (vc.compareTo(vipCategiryCreate) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		Assert.assertNotNull(vipCategiryCreate);
		Assert.assertEquals(EnumErrorCode.values()[Integer.parseInt(paramsForCreate.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())], EnumErrorCode.EC_NoError);

		System.out.println("\n------------------------ case 1:会员类别名称重复，错误码应该是1----------------------");
		VipCategory vc1 = DataInput.getData();
		vc1.setID(vipCategiryCreate.getID());
		vc1.setName("黄金会员");
		Map<String, Object> paramsForUpdate1 = vc1.getUpdateParam(BaseBO.INVALID_CASE_ID, vc1);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		vipCategoryMapper.update(paramsForUpdate1);
		Assert.assertEquals(EnumErrorCode.values()[Integer.parseInt(paramsForUpdate1.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())], EnumErrorCode.EC_Duplicated);

		System.out.println("\n------------------------ case 2: 会员类别名称不重复，错误码应该是0 ----------------------");
		vc1.setName("萨德1" + String.valueOf(System.currentTimeMillis()).substring(6));
		Map<String, Object> paramsForUpdate = vc1.getUpdateParam(BaseBO.INVALID_CASE_ID, vc1);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		vipCategoryMapper.update(paramsForUpdate);
		Assert.assertEquals(EnumErrorCode.values()[Integer.parseInt(paramsForUpdate.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())], EnumErrorCode.EC_NoError);
	}

	@Test
	public void deleteTest() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		System.out.println("\n------------------------ Delete VipCategoty Test ------------------------");

		VipCategory vc = DataInput.getData();
		Map<String, Object> paramsForCreate = vc.getCreateParam(BaseBO.INVALID_CASE_ID, vc);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		VipCategory vipCategiryCreate = (VipCategory) vipCategoryMapper.create(paramsForCreate);
		vc.setIgnoreIDInComparision(true);
		if (vc.compareTo(vipCategiryCreate) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		Assert.assertNotNull(vipCategiryCreate);
		Assert.assertEquals(EnumErrorCode.values()[Integer.parseInt(paramsForCreate.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())], EnumErrorCode.EC_NoError);

		System.out.println("\n------------------------ case1:会员表中已有该会员的类别。删除不了，错误代码为7----------------------");
		vc.setID(1);
		Map<String, Object> paramsForDelete1 = vc.getDeleteParam(BaseBO.INVALID_CASE_ID, vc);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		vipCategoryMapper.delete(paramsForDelete1);
		Assert.assertEquals(EnumErrorCode.values()[Integer.parseInt(paramsForDelete1.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())], EnumErrorCode.EC_BusinessLogicNotDefined);

		System.out.println("\n------------------------ case2:会员表中没有这种会员的类别。可以直接删除，错误代码为0----------------------");
		VipCategory vc1 = DataInput.getData();
		vc1.setID(vipCategiryCreate.getID());
		Map<String, Object> paramsForDelete2 = vc1.getDeleteParam(BaseBO.INVALID_CASE_ID, vc1);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		vipCategoryMapper.delete(paramsForDelete2);
		Assert.assertEquals(EnumErrorCode.values()[Integer.parseInt(paramsForDelete2.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())], EnumErrorCode.EC_NoError);

		System.out.println("\n------------------------ case3:删除一个不存在的Id----------------------");
		vc1.setID(-22);
		Map<String, Object> paramsForDelete3 = vc1.getDeleteParam(BaseBO.INVALID_CASE_ID, vc1);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		vipCategoryMapper.delete(paramsForDelete3);
		Assert.assertEquals(EnumErrorCode.values()[Integer.parseInt(paramsForDelete3.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())], EnumErrorCode.EC_NoError);

	}
}
