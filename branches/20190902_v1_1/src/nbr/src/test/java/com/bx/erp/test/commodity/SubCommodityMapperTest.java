package com.bx.erp.test.commodity;

import java.util.List;
import java.util.Map;

import org.springframework.test.context.web.WebAppConfiguration;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.CommodityType.EnumCommodityType;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.commodity.Commodity;
import com.bx.erp.model.commodity.SubCommodity;
import com.bx.erp.model.commodity.Commodity.EnumStatusCommodity;
import com.bx.erp.test.BaseCommodityTest;
import com.bx.erp.test.BaseMapperTest;
import com.bx.erp.test.Shared;
import com.bx.erp.util.DataSourceContextHolder;

@WebAppConfiguration
public class SubCommodityMapperTest extends BaseMapperTest {

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

		// 创建商品测试数据
		Commodity commTemplate = BaseCommodityTest.DataInput.getCommodity();
		commTemplate.setOperatorStaffID(STAFF_ID3);
		commTemplate.setType(EnumStatusCommodity.ESC_ToEliminated.getIndex());
		Commodity commCreate = BaseCommodityTest.createCommodityViaMapper(commTemplate, BaseBO.CASE_Commodity_CreateComposition);

		// case1: 添加不重复的组合商品
		Shared.caseLog("case1: 添加不重复的组合商品");
		SubCommodity data = BaseCommodityTest.DataInput.getSubCommodity();
		data.setCommodityID(commCreate.getID());
		//
		SubCommodity subCommodity = BaseCommodityTest.createSubCommodityViaMapper(data);

		// 删除并验证测试数据
		BaseCommodityTest.deleteSubCommodityViaMapper(subCommodity);
		BaseCommodityTest.deleteCommodityViaMapper(commCreate, EnumErrorCode.EC_NoError);
	}

	@Test
	public void createTest2() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		// 创建测试数据
		Commodity commTemplate = BaseCommodityTest.DataInput.getCommodity(EnumCommodityType.ECT_Combination.getIndex());
		commTemplate.setOperatorStaffID(STAFF_ID3);
		Commodity commCreate = BaseCommodityTest.createCommodityViaMapper(commTemplate, BaseBO.CASE_Commodity_CreateComposition);
		//
		SubCommodity data = BaseCommodityTest.DataInput.getSubCommodity();
		data.setCommodityID(commCreate.getID());
		SubCommodity subCommodityCreate = BaseCommodityTest.createSubCommodityViaMapper(data);

		// case2: 添加重复的组合商品
		Shared.caseLog("case2: 添加重复的组合商品");
		SubCommodity subCommodity = BaseCommodityTest.DataInput.getSubCommodity();
		subCommodity.setCommodityID(commCreate.getID());
		subCommodity.setSubCommodityID(9);
		Map<String, Object> params = subCommodity.getCreateParam(BaseBO.INVALID_CASE_ID, subCommodity);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		SubCommodity subCommodityCreate1 = (SubCommodity) subCommodityMapper.create(params);
		//
		Assert.assertTrue(subCommodityCreate1 == null && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_Duplicated,
				params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString()); // ...??

		// 删除并验证测试数据
		BaseCommodityTest.deleteCommodityViaMapper(commCreate, EnumErrorCode.EC_NoError);
		BaseCommodityTest.deleteSubCommodityViaMapper(subCommodityCreate);
	}

	@Test
	public void createTest3() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		// 创建测试数据
		Commodity commTemplate = BaseCommodityTest.DataInput.getCommodity();
		commTemplate.setOperatorStaffID(STAFF_ID3);
		commTemplate.setType(EnumCommodityType.ECT_Normal.getIndex());
		Commodity commCreate = BaseCommodityTest.createCommodityViaMapper(commTemplate, BaseBO.CASE_Commodity_CreateSingle);

		// case3:将普通商品当做组合商品插入子商品
		Shared.caseLog("case3:将普通商品当做组合商品插入子商品");
		SubCommodity subCommodity = BaseCommodityTest.DataInput.getSubCommodity();
		subCommodity.setCommodityID(commCreate.getID());
		subCommodity.setSubCommodityID(10);
		Map<String, Object> params = subCommodity.getCreateParam(BaseBO.INVALID_CASE_ID, subCommodity);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		SubCommodity subCommodityCreate = (SubCommodity) subCommodityMapper.create(params);
		//
		Assert.assertTrue(subCommodityCreate == null && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined,
				params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());// ...??
	}

	@Test
	public void createTest4() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		// 创建商品测试数据
		Commodity commTemplate = BaseCommodityTest.DataInput.getCommodity(EnumCommodityType.ECT_Combination.getIndex());
		commTemplate.setOperatorStaffID(STAFF_ID3);
		Commodity commCreate = BaseCommodityTest.createCommodityViaMapper(commTemplate, BaseBO.CASE_Commodity_CreateComposition);

		// case4:用不存在的子商品创建一个组合商品
		Shared.caseLog("case4:用不存在的子商品创建一个组合商品");
		SubCommodity subCommodity = BaseCommodityTest.DataInput.getSubCommodity();
		subCommodity.setCommodityID(commCreate.getID());
		subCommodity.setSubCommodityID(999999999);
		Map<String, Object> params = subCommodity.getCreateParam(BaseBO.INVALID_CASE_ID, subCommodity);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		SubCommodity subCommodityCreate = (SubCommodity) subCommodityMapper.create(params);
		//
		Assert.assertTrue(subCommodityCreate == null && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined,
				params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());// ...??

		// 删除商品测试数据
		BaseCommodityTest.deleteCommodityViaMapper(commCreate, EnumErrorCode.EC_NoError);
	}

	@Test
	public void createTest5() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		// case5:用不存在的商品创建一个组合商品
		Shared.caseLog("case5:用不存在的商品创建一个组合商品");
		SubCommodity subCommodity = BaseCommodityTest.DataInput.getSubCommodity();
		subCommodity.setCommodityID(999999999);
		subCommodity.setSubCommodityID(9);
		Map<String, Object> params = subCommodity.getCreateParam(BaseBO.INVALID_CASE_ID, subCommodity);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		SubCommodity subCommodityCreate = (SubCommodity) subCommodityMapper.create(params);
		//
		Assert.assertTrue(subCommodityCreate == null && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined,
				params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());// ...??
	}

	@Test
	public void createTest6() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		// 创建商品测试数据
		Commodity commTemplate = BaseCommodityTest.DataInput.getCommodity(EnumCommodityType.ECT_Combination.getIndex());
		commTemplate.setOperatorStaffID(STAFF_ID3);
		Commodity commCreate = BaseCommodityTest.createCommodityViaMapper(commTemplate, BaseBO.CASE_Commodity_CreateComposition);

		// Case6:添加多包装商品为子商品，错误码为7
		Shared.caseLog("Case6:添加多包装商品为子商品，错误码为7");
		SubCommodity subCommodity = BaseCommodityTest.DataInput.getSubCommodity();
		subCommodity.setCommodityID(commCreate.getID());
		subCommodity.setSubCommodityID(51);
		Map<String, Object> params = subCommodity.getCreateParam(BaseBO.INVALID_CASE_ID, subCommodity);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		SubCommodity subCommodityCreate = (SubCommodity) subCommodityMapper.create(params);
		//
		Assert.assertTrue(subCommodityCreate == null && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined,
				params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());// ...??

		// 删除商品测试数据
		BaseCommodityTest.deleteCommodityViaMapper(commCreate, EnumErrorCode.EC_NoError);
	}

	@Test
	public void createTest7() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		// 创建商品测试数据
		Commodity commTemplate = BaseCommodityTest.DataInput.getCommodity(EnumCommodityType.ECT_Combination.getIndex());
		commTemplate.setOperatorStaffID(STAFF_ID3);
		Commodity commCreate = BaseCommodityTest.createCommodityViaMapper(commTemplate, BaseBO.CASE_Commodity_CreateComposition);

		// Case7:添加组合商品为子商品，错误码为7
		Shared.caseLog("Case7:添加组合商品为子商品，错误码为7");
		SubCommodity subCommodity = BaseCommodityTest.DataInput.getSubCommodity();
		subCommodity.setCommodityID(commCreate.getID());
		subCommodity.setSubCommodityID(49);
		Map<String, Object> params = subCommodity.getCreateParam(BaseBO.INVALID_CASE_ID, subCommodity);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		SubCommodity subCommodityCreate = (SubCommodity) subCommodityMapper.create(params);
		//
		Assert.assertTrue(subCommodityCreate == null && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined,
				params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());// ...??

		// 删除商品测试数据
		BaseCommodityTest.deleteCommodityViaMapper(commCreate, EnumErrorCode.EC_NoError);
	}

	@Test
	public void createTest8() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		// 创建商品测试数据
		Commodity commTemplate = BaseCommodityTest.DataInput.getCommodity(EnumCommodityType.ECT_Combination.getIndex());
		commTemplate.setOperatorStaffID(STAFF_ID3);
		Commodity commCreate = BaseCommodityTest.createCommodityViaMapper(commTemplate, BaseBO.CASE_Commodity_CreateComposition);

		// case8: 子商品价格为负数
		Shared.caseLog("case8: 子商品价格为负数");
		SubCommodity data = BaseCommodityTest.DataInput.getSubCommodity();
		data.setCommodityID(commCreate.getID());
		data.setPrice(-1);
		//
		Map<String, Object> subParams = data.getCreateParam(BaseBO.INVALID_CASE_ID, data);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		SubCommodity subCommodity = (SubCommodity) subCommodityMapper.create(subParams);
		//
		Assert.assertTrue(subCommodity == null && EnumErrorCode.values()[Integer.parseInt(subParams.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined,
				subParams.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());

		// 删除商品测试数据
		BaseCommodityTest.deleteCommodityViaMapper(commCreate, EnumErrorCode.EC_NoError);
	}

	@Test
	public void createTest9() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		// 创建测试数据
		Commodity commTemplate = BaseCommodityTest.DataInput.getCommodity();
		commTemplate.setOperatorStaffID(STAFF_ID3);
		commTemplate.setType(EnumCommodityType.ECT_MultiPackaging.getIndex());
		commTemplate.setRefCommodityID(1);
		commTemplate.setRefCommodityMultiple(3);
		commTemplate.setMultiPackagingInfo("123456789" + ";1;1;1;8;8;" + commTemplate.getBarcodes() + ";");
		Commodity commCreate = BaseCommodityTest.createCommodityViaMapper(commTemplate, BaseBO.CASE_Commodity_CreateMultiPackaging);

		// case9:将多包装商品当做组合商品插入子商品
		Shared.caseLog("case9:将多包装商品当做组合商品插入子商品");
		SubCommodity subCommodity = BaseCommodityTest.DataInput.getSubCommodity();
		subCommodity.setCommodityID(commCreate.getID());
		subCommodity.setSubCommodityID(10);
		Map<String, Object> params = subCommodity.getCreateParam(BaseBO.INVALID_CASE_ID, subCommodity);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		SubCommodity subCommodityCreate = (SubCommodity) subCommodityMapper.create(params);
		//
		Assert.assertTrue(subCommodityCreate == null && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined,
				params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());// ...??

		// 删除商品测试数据
		BaseCommodityTest.deleteCommodityViaMapper(commCreate, EnumErrorCode.EC_NoError);
	}

	@Test
	public void createTest10() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("将服务商品当做组合商品插入子商品");
		Commodity commTemplate = BaseCommodityTest.DataInput.getCommodity(EnumCommodityType.ECT_Service.getIndex());
		Commodity commCreate = BaseCommodityTest.createCommodityViaMapper(commTemplate, BaseBO.CASE_Commodity_CreateService);
		//
		SubCommodity subCommodity = BaseCommodityTest.DataInput.getSubCommodity();
		subCommodity.setCommodityID(commCreate.getID());
		String err = subCommodity.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertTrue("".equals(err), err);
		//
		Map<String, Object> params = subCommodity.getCreateParam(BaseBO.INVALID_CASE_ID, subCommodity);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		SubCommodity subCommodityCreate = (SubCommodity) subCommodityMapper.create(params);
		//
		Assert.assertTrue(subCommodityCreate == null && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined,
				params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());

		// 删除商品测试数据
		BaseCommodityTest.deleteCommodityViaMapper(commCreate, EnumErrorCode.EC_NoError);
	}

	@Test
	public void createTest11() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("组合商品添加服务商品为子商品");
		// 创建组合商品测试数据
		Commodity commTemplate = BaseCommodityTest.DataInput.getCommodity(EnumCommodityType.ECT_Combination.getIndex());
		commTemplate.setOperatorStaffID(STAFF_ID3);
		Commodity commCreate = BaseCommodityTest.createCommodityViaMapper(commTemplate, BaseBO.CASE_Commodity_CreateComposition);
		// 创建服务商品
		Commodity comm = BaseCommodityTest.DataInput.getCommodity(EnumCommodityType.ECT_Service.getIndex());
		Commodity serviceCommCreate = BaseCommodityTest.createCommodityViaMapper(comm, BaseBO.CASE_Commodity_CreateService);
		//
		SubCommodity subCommodity = BaseCommodityTest.DataInput.getSubCommodity();
		subCommodity.setCommodityID(commCreate.getID());
		subCommodity.setSubCommodityID(serviceCommCreate.getID());
		String err = subCommodity.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertTrue("".equals(err), err);
		//
		Map<String, Object> params = subCommodity.getCreateParam(BaseBO.INVALID_CASE_ID, subCommodity);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		SubCommodity subCommodityCreate = (SubCommodity) subCommodityMapper.create(params);
		//
		Assert.assertTrue(subCommodityCreate == null && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined,
				params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());

		// 删除商品测试数据
		BaseCommodityTest.deleteCommodityViaMapper(commCreate, EnumErrorCode.EC_NoError);
		BaseCommodityTest.deleteCommodityViaMapper(serviceCommCreate, EnumErrorCode.EC_NoError);
	}

	@Test
	public void deleteTest() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		// 创建测试数据
		Commodity commTemplate = BaseCommodityTest.DataInput.getCommodity(EnumCommodityType.ECT_Combination.getIndex());
		commTemplate.setOperatorStaffID(STAFF_ID3);
		Commodity commCreate = BaseCommodityTest.createCommodityViaMapper(commTemplate, BaseBO.CASE_Commodity_CreateComposition);
		//
		SubCommodity data = BaseCommodityTest.DataInput.getSubCommodity();
		data.setCommodityID(commCreate.getID());
		SubCommodity subCommodityCreate = BaseCommodityTest.createSubCommodityViaMapper(data);

		// case1：正常删除组合型商品
		Shared.caseLog("case1：正常删除组合型商品");
		SubCommodity subCommodity = new SubCommodity();
		subCommodity.setCommodityID(subCommodityCreate.getCommodityID());
		subCommodity.setSubCommodityID(subCommodityCreate.getSubCommodityID());
		BaseCommodityTest.deleteSubCommodityViaMapper(subCommodity);

		// 删除商品测试数据
		BaseCommodityTest.deleteCommodityViaMapper(commCreate, EnumErrorCode.EC_NoError);
	}

	@Test
	public void retrieveNTest() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		// 创建测试数据
		Commodity commTemplate = BaseCommodityTest.DataInput.getCommodity(EnumCommodityType.ECT_Combination.getIndex());
		commTemplate.setOperatorStaffID(STAFF_ID3);
		Commodity commCreate = BaseCommodityTest.createCommodityViaMapper(commTemplate, BaseBO.CASE_Commodity_CreateComposition);
		//
		SubCommodity data = BaseCommodityTest.DataInput.getSubCommodity();
		data.setCommodityID(commCreate.getID());
		SubCommodity subCommodityCreate = BaseCommodityTest.createSubCommodityViaMapper(data);

		SubCommodity subCommodity = new SubCommodity();
		subCommodity.setCommodityID(subCommodityCreate.getCommodityID());
		Map<String, Object> params = subCommodity.getRetrieveNParam(BaseBO.INVALID_CASE_ID, subCommodity);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> retrieveNList = subCommodityMapper.retrieveN(params);
		//
		Assert.assertTrue(retrieveNList.size() > 0 && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError,
				params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		// 字段验证
		for (BaseModel bm : retrieveNList) {
			String error = ((SubCommodity) bm).checkCreate(BaseBO.INVALID_CASE_ID);
			Assert.assertEquals(error, "");
		}

		// 删除并验证测试数据
		BaseCommodityTest.deleteSubCommodityViaMapper(subCommodityCreate);
	}

}
